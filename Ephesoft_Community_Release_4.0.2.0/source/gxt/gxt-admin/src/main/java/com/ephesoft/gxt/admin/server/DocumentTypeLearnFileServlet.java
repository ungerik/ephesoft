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
import java.io.IOException;
import java.util.List;

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
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.OSUtil;
import com.ephesoft.dcma.util.PDFUtil;
import com.ephesoft.dcma.util.TIFFUtil;
import com.ephesoft.gxt.admin.client.i18n.DocumentTypeConstants;
import com.ephesoft.gxt.core.server.DCMAHttpServlet;

public class DocumentTypeLearnFileServlet extends DCMAHttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String FIRST_PAGE = "_First_Page";

	private static final String MIDDLE_PAGE = "_Middle_Page";

	private static final String LAST_PAGE = "_Last_Page";

	private static String uploadLearnFilePath = null;
	private static String documentType = null;
	private static String batchClassIdentifier = null;

	BatchInstanceThread batchInstanceThread = null;

	@Override
	public final void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		uploadLearnFilePath = req.getParameter(DocumentTypeConstants.UPLOAD_FILE_REQ_PARAMERTER);
		if (uploadLearnFilePath != null && !uploadLearnFilePath.isEmpty()) {
			if (new File(uploadLearnFilePath).exists()) {
				FileUtils.deleteContents(uploadLearnFilePath, false);
			}
		}
		attachFile(req, resp);
	}

	@SuppressWarnings("unchecked")
	private void attachFile(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		if (ServletFileUpload.isMultipartContent(req)) {

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			String exportSerailizationFolderPath = batchSchemaService.getBaseFolderLocation() + File.separator + "tempLearnFile";
			File exportSerailizationFolder = new File(exportSerailizationFolderPath);
			if (!exportSerailizationFolder.exists()) {
				exportSerailizationFolder.mkdirs();
			} else {
				FileUtils.deleteContents(exportSerailizationFolderPath, false);
				exportSerailizationFolder.mkdirs();
			}
			String learnFileName = null;
			String pathToLearnFile = null;
			String learnFileExtension = null;
			List<FileItem> items;

			try {
				items = upload.parseRequest(req);
				for (FileItem item : items) {
					if (!item.isFormField()) {
						learnFileName = item.getName();

						if (learnFileName != null) {
							learnFileName = learnFileName.substring(learnFileName.lastIndexOf(File.separator) + 1);
							learnFileExtension = learnFileName.substring(learnFileName.lastIndexOf(".") + 1);

						}

						pathToLearnFile = EphesoftStringUtil.concatenate(exportSerailizationFolderPath, File.separator, learnFileName);

						int numberOfPages = 0;

						File learnedFile = new File(pathToLearnFile);
						batchClassIdentifier = req.getParameter(DocumentTypeConstants.BATCH_CLASS_ID_REQ_PARAMETER);
						documentType = req.getParameter(DocumentTypeConstants.DOCUMENT_TYPE_REQ_PARAMETER);

						try {
							item.write(learnedFile);
							if (learnFileExtension.equalsIgnoreCase(FileType.PDF.getExtension())) {
								numberOfPages = PDFUtil.getPDFPageCount(pathToLearnFile);

								converPDFTotiffFile(pathToLearnFile, numberOfPages);
								copySinglePageFilesToLearnFolder(exportSerailizationFolderPath, learnFileName);

							} else if (learnFileExtension.equalsIgnoreCase(FileType.TIF.getExtension())
									|| learnFileExtension.equalsIgnoreCase(FileType.TIFF.getExtension())) {
								numberOfPages = TIFFUtil.getTIFFPageCount(pathToLearnFile);
								if (numberOfPages != 1) {
									convertMultiPageTiffToSinglePageTiff(pathToLearnFile, numberOfPages);
									copySinglePageFilesToLearnFolder(exportSerailizationFolderPath, learnFileName);
								} else {
									copyFilesFromTempToLearnFolders(exportSerailizationFolderPath, learnFileName, FIRST_PAGE);
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (FileUploadException e) {
				log.error("Unable to read the form contents." + e, e);
			}
		}
	}

	private void copySinglePageFilesToLearnFolder(String exportSerailizationFolderPath, String learnFileName) {
		File[] listOfFiles = new File(exportSerailizationFolderPath).listFiles(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
				FileType.TIFF.getExtensionWithDot()));
		int totLength = listOfFiles.length;
		for (File file : listOfFiles) {
			String fileName = file.getName();
			if (null != fileName) {
				if (totLength==1 || fileName.contains("0001")) {
					copyFilesFromTempToLearnFolders(exportSerailizationFolderPath, fileName, FIRST_PAGE);
				} else if (fileName.contains(String.valueOf(totLength) + ".tif")) {
					copyFilesFromTempToLearnFolders(exportSerailizationFolderPath, fileName, LAST_PAGE);
				} else {
					copyFilesFromTempToLearnFolders(exportSerailizationFolderPath, fileName, MIDDLE_PAGE);

				}
			}
		}
		FileUtils.deleteContents(exportSerailizationFolderPath, false);
	}

	private void copyFilesFromTempToLearnFolders(String temporaryFolderLocation, String fileName, String pageType) {
		String learnFileLocation = getLearnFileLocation(pageType);
		String imageFileLocation = getImageClassifyFolderLocation(pageType);
		if (!new File(temporaryFolderLocation).exists()) {
		} else {
			try {
				FileUtils.copyFile(new File(temporaryFolderLocation + File.separator + fileName), new File(learnFileLocation
						+ File.separator + fileName));

				FileUtils.copyFile(new File(temporaryFolderLocation + File.separator + fileName), new File(imageFileLocation
						+ File.separator + fileName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private String getLearnFileLocation(String pageType) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String learnFileLocation = null;

		learnFileLocation = EphesoftStringUtil.concatenate(batchSchemaService.getBaseFolderLocation(), File.separator,
				batchClassIdentifier, File.separator, batchSchemaService.getSearchSampleName(), File.separator, documentType,
				File.separator, documentType.concat(pageType));
		File batchClassSeachFile = new File(learnFileLocation);
		if (!batchClassSeachFile.exists()) {
			batchClassSeachFile.mkdirs();
		}
		return learnFileLocation;
	}

	private String getImageClassifyFolderLocation(String pageType) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String imageClassifyFolderLocation = null;

		imageClassifyFolderLocation = EphesoftStringUtil.concatenate(batchSchemaService.getBaseFolderLocation(), File.separator,
				batchClassIdentifier, File.separator, batchSchemaService.getImagemagickBaseFolderName(), File.separator, documentType,
				File.separator, documentType.concat(pageType));
		File batchClassSeachFile = new File(imageClassifyFolderLocation);
		if (!batchClassSeachFile.exists()) {
			batchClassSeachFile.mkdirs();
		}
		return imageClassifyFolderLocation;
	}

	private void convertMultiPageTiffToSinglePageTiff(String pathToLearnFile, int numberOfPages) {
		ImageProcessService imageProcessService = this.getSingleBeanOfType(ImageProcessService.class);
		batchInstanceThread = new BatchInstanceThread();

		try {
			imageProcessService.convertPdfOrMultiPageTiffToPNGOrTifUsingIM("-scene 1", new File(pathToLearnFile), "", new File(
					pathToLearnFile), batchInstanceThread, true, true);
			batchInstanceThread.execute();

		} catch (DCMAApplicationException dcmaApplicationException) {
			log.error("Unable to process multipageTiff into single page tif", dcmaApplicationException.getMessage());
		} catch (DCMAException dcmaException) {
			log.error("Unable to process multipageTiff into single page tif", dcmaException.getMessage());

		}

	}

	private void converPDFTotiffFile(String pathToLearnFile, int numberOfPages) {
		log.info(EphesoftStringUtil.concatenate("ConverPDFTotiffFile type:", pathToLearnFile, " no of pages:", numberOfPages));
		ImageProcessService imageProcessService = this.getSingleBeanOfType(ImageProcessService.class);

		// code to convert pdf into tiff by checking the OS on which the application is running.
		batchInstanceThread = new BatchInstanceThread();

		try {
			boolean isSinglePagePDF = false;
			if(numberOfPages == 1){
				isSinglePagePDF = true;
			}
			if (OSUtil.isWindows()) {

				imageProcessService.convertPdfToSinglePagePNGOrTifUsingGSAPI("-r150 -sDEVICE=tiffgray -dBATCH -dNOPAUSE", new File(
						pathToLearnFile), "", new File(pathToLearnFile), batchInstanceThread, isSinglePagePDF, true, true);

			} else {
				imageProcessService.convertPdfToSinglePageTiffs(batchClassIdentifier, new File(pathToLearnFile), batchInstanceThread,
						isSinglePagePDF);
			}
			batchInstanceThread.execute();
		} catch (DCMAApplicationException dcmaApplicationException) {
			log.error("Unable to process pdf file into tif", dcmaApplicationException.getMessage());
		} catch (DCMAException dcmaException) {
			log.error("Unable to process pdf file into tif", dcmaException.getMessage());

		}

	}
}
