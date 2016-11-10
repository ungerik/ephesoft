/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2015 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.gxt.admin.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.PDFUtil;
import com.ephesoft.dcma.util.TIFFUtil;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.core.server.DCMAHttpServlet;
import com.ephesoft.gxt.core.shared.util.StringUtil;

public class UploadImageFileServlet extends DCMAHttpServlet implements ICommonConstants {

	private static final long serialVersionUID = 1L;

	private static final String GHOSTSCRIPT_PNG_PARAMS = "ghostScript.command.png.params";

	@Override
	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String batchClassId = null;
		String docName = null;
		String fileName = null;
		String isAdvancedTableInfo = null;
		InputStream instream = null;
		OutputStream out = null;
		PrintWriter printWriter = resp.getWriter();
		if (ServletFileUpload.isMultipartContent(req)) {

			// Create a factory for disk-based file items
			FileItemFactory factory = new DiskFileItemFactory();

			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setHeaderEncoding(AdminConstants.CHARACTER_ENCODING_UTF8);
			List<FileItem> items;
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			String uploadPath = null;
			try {
				items = upload.parseRequest(req);
				if (req.getParameter("isAdvancedTableInfo") != null) {
					isAdvancedTableInfo = req.getParameter("isAdvancedTableInfo").toString();
				}
				batchClassId = req.getParameter("batchClassId");
				docName = req.getParameter("docName");
				log.debug("Executing Servlet for batchClassId" + batchClassId + " docName: " + docName);
				String fileExtension = null;

				for (FileItem item : items) {

					// process only file upload - discard other form item types

					if (!item.isFormField()) {
						fileName = item.getName();
						log.debug("Executing Servlet for fileName from item:" + fileName);

						// Checks for invalid characters present in uploaded filename.
						fileName = checkFileNameForInvalidChars(fileName);
						log.debug("FileName of File after removing invalid characters :" + fileName);
						if (fileName != null) {
							fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
							fileExtension = fileName.substring(fileName.lastIndexOf(EXTENSION_CHAR) + 1);
						}
						instream = item.getInputStream();
						printWriter.write(fileName);
						printWriter.write(AdminConstants.PATTERN_DELIMITER);
					}
				}
				if (batchClassId == null || docName == null) {
					log.error("Error while loading image... Either batchClassId or doc type is null. Batch Class Id :: "
							+ batchClassId + " Doc Type :: " + docName);
					printWriter.write("Error while loading image... Either batchClassId or doc type is null.");
				} else {
					batchClassId = batchClassId.trim();
					docName = docName.trim();
					if (isAdvancedTableInfo != null && isAdvancedTableInfo.equalsIgnoreCase(String.valueOf(Boolean.TRUE))) {
						uploadPath = batchSchemaService.getAdvancedTestTableFolderPath(batchClassId, true);
					} else {
						uploadPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassId, true);
					}
					uploadPath += File.separator + docName.trim() + File.separator;
					File uploadFolder = new File(uploadPath);

					if (!uploadFolder.exists()) {
						try {
							boolean tempPath = uploadFolder.mkdirs();
							if (!tempPath) {
								log.error("Unable to create the folders in the temp directory specified. Change the path and permissions in dcma-batch.properties");
								printWriter
										.write("Unable to create the folders in the temp directory specified. Change the path and permissions in dcma-batch.properties");
								return;
							}
						} catch (Exception e) {
							log.error("Unable to create the folders in the temp directory.", e);
							printWriter.write("Unable to create the folders in the temp directory." + e.getMessage());
							return;
						}
					}
					uploadPath += File.separator + fileName;
					uploadPath = uploadPath.substring(0, uploadPath.lastIndexOf(EXTENSION_CHAR) + 1).concat(
							fileExtension.toLowerCase());
					out = new FileOutputStream(uploadPath);
					byte buf[] = new byte[1024];
					int len;
					while ((len = instream.read(buf)) > 0) {
						out.write(buf, 0, len);
					}
					ImageProcessService imageProcessService = this.getSingleBeanOfType(ImageProcessService.class);
					int numberOfPages = 0;
					// deleteFiles(uploadFolder,fileName);
					if (fileExtension.equalsIgnoreCase(FileType.PDF.getExtension())) {
						numberOfPages = PDFUtil.getPDFPageCount(uploadPath);

						// code to convert pdf into tiff by checking the OS on which the application is running.
						BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
						if (null != isAdvancedTableInfo && isAdvancedTableInfo.equalsIgnoreCase(String.valueOf(Boolean.TRUE))
								&& numberOfPages != 1) {
							printWriter.write(" MultiPage_error not supported for Advanced Table Extraction.");
							log.error("MultiPage File not supported for Advanced Table Extraction.");
						} else {

							imageProcessService.convertPdfToSinglePagePNGOrTifUsingGSAPI(
									new File(uploadPath), "", new File(uploadPath), batchInstanceThread, numberOfPages == 1, true, false, batchClassId);
							String outputParams = ApplicationConfigProperties.getApplicationConfigProperties().getProperty(GHOSTSCRIPT_PNG_PARAMS);
							imageProcessService.convertPdfToSinglePagePNGOrTifUsingGSAPI(outputParams,
									new File(uploadPath), "", new File(uploadPath), batchInstanceThread, numberOfPages == 1, false,false);
							batchInstanceThread.execute();
						}
					} else if (fileExtension.equalsIgnoreCase(FileType.TIF.getExtension())
							|| fileExtension.equalsIgnoreCase(FileType.TIFF.getExtension())) {
						numberOfPages = TIFFUtil.getTIFFPageCount(uploadPath);
						if (null != isAdvancedTableInfo && isAdvancedTableInfo.equalsIgnoreCase(String.valueOf(Boolean.TRUE))
								&& numberOfPages != 1) {
							printWriter.write(" MultiPage_error File not supported for Advanced Table Extraction.");
							log.error("MultiPage File not supported for Advanced Table Extraction.");
						} else {
							if (numberOfPages != 1) {
								BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
								imageProcessService.convertPdfOrMultiPageTiffToPNGOrTifUsingIM("", new File(uploadPath), "",
										new File(uploadPath), batchInstanceThread, false, false);
								imageProcessService.convertPdfOrMultiPageTiffToPNGOrTifUsingIM("", new File(uploadPath), "",
										new File(uploadPath), batchInstanceThread, false, true);
								batchInstanceThread.execute();
							} else {
								imageProcessService.generatePNGForImage(new File(uploadPath));
							}
						}
						log.info("Png file created successfully for file: " + uploadPath);
					}
				}
			} catch (FileUploadException e) {
				log.error("Unable to read the form contents." + e, e);
				printWriter.write("Unable to read the form contents.Please try again.");
			} catch (DCMAException e) {
				log.error("Unable to generate PNG." + e, e);
				printWriter.write("Unable to generate PNG.Please try again.");
			} catch (DCMAApplicationException exception) {
				log.error("Unable to upload Multipage file." + exception, exception);
				printWriter.write("Unable to upload Multipage file.");
			} finally {
				if (out != null) {
					out.close();
				}
				if (instream != null) {
					instream.close();
				}
			}
			printWriter.write("file_seperator:" + File.separator);
			printWriter.write("|");
		}
	}

	public void deleteFiles(File uploadFolder, final String fileName) {
		final File[] files = uploadFolder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(final File dir, final String name) {
				return name.startsWith(StringUtil.concatenate(fileName, HYPHEN));
			}
		});
		for (final File file : files) {
			if (!file.delete()) {
				log.debug("Can't remove " + file.getAbsolutePath());
			} else {
				log.debug("Deleted file  " + file.getAbsolutePath());
			}
		}
	}

	/**
	 * Checks for any Invalid Character present in uploaded File Name.
	 * 
	 * @param fileName{@link String}
	 */
	private String checkFileNameForInvalidChars(String fileName) {
		final String[] imageMagickUnsupportedCharatersArray = getUnsupportedCharacterArray();
		if (!StringUtil.isNullOrEmpty(fileName) && null != imageMagickUnsupportedCharatersArray) {
			for (final String unsupportedCharacter : imageMagickUnsupportedCharatersArray) {
				if (fileName.contains(unsupportedCharacter)) {
					fileName = fileName.replaceAll(unsupportedCharacter, AdminConstants.EMPTY_STRING);
				}
			}
		}
		return fileName;
	}

	/**
	 * Gets unsupported character array from imagemagick properties file.
	 * 
	 * @return array of imagemagick unsupported characters.
	 */
	public String[] getUnsupportedCharacterArray() {
		String[] unsupportedCharacterArray = null;
		try {
			final Properties imageMagickProperties = ApplicationConfigProperties.getApplicationConfigProperties()
					.getAllUTF8Properties(
							StringUtil.concatenate(AdminConstants.IMAGEMAGICK_PROPERTIES_FOLDER, File.separator,
									AdminConstants.IMAGEMAGICK_PROPERTIES_FILE));
			if (null != imageMagickProperties) {
				final String unsupportedCharater = imageMagickProperties
						.getProperty(AdminConstants.IMAGEMAGICK_UNSUPPORTED_CHARACTER_PROPERTY);
				if (!StringUtil.isNullOrEmpty(unsupportedCharater)) {
					unsupportedCharacterArray = unsupportedCharater.split(AdminConstants.SEMICOLON);
				}
			}
		} catch (final IOException ioException) {
			log.error(StringUtil.concatenate("Unable to fetch unsupported character list from imageMagick property file.",
					ioException.getMessage()));
		}
		return unsupportedCharacterArray;
	}
}
