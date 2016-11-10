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

package com.ephesoft.gxt.systemconfig.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import com.ephesoft.gxt.core.server.DCMAHttpServlet;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.shared.SystemConfigSharedConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.util.FileUtils;

/**
 * Servlet for importing regex pool and table column pool.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Sep 12, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class ImportPoolServlet extends DCMAHttpServlet {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Overridden doGet method.
	 */
	@Override
	public final void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	/**
	 * Overridden doPost method.
	 */
	@Override
	public void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
		final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);

		final String lastAttachedZipSourcePath = req.getParameter(SystemConfigConstants.ATTACHED_ZIP_SOURCE_PATH);
		if (lastAttachedZipSourcePath != null && !lastAttachedZipSourcePath.isEmpty()) {
			final File zipSoucePath = new File(lastAttachedZipSourcePath);
			if (zipSoucePath.exists()) {
				FileUtils.deleteDirectoryAndContentsRecursive(zipSoucePath);
			}
		}
		attachFile(req, resp, batchSchemaService);
	}

	/**
	 * Unzip the attached zipped file.
	 * 
	 * @param req {@link HttpServletRequest}
	 * @param resp {@link HttpServletResponse}
	 * @param batchSchemaService {@link BatchSchemaService}
	 * @throws IOException
	 */
	private void attachFile(final HttpServletRequest req, final HttpServletResponse resp, final BatchSchemaService batchSchemaService)
			throws IOException {
		final PrintWriter printWriter = resp.getWriter();
		File tempZipFile = null;
		InputStream instream = null;
		OutputStream out = null;
		String tempOutputUnZipDir = CoreCommonConstant.EMPTY_STRING;

		if (ServletFileUpload.isMultipartContent(req)) {
			final FileItemFactory factory = new DiskFileItemFactory();
			final ServletFileUpload upload = new ServletFileUpload(factory);
			final String exportSerailizationFolderPath = batchSchemaService.getBatchExportFolderLocation();
			final File exportSerailizationFolder = new File(exportSerailizationFolderPath);
			if (!exportSerailizationFolder.exists()) {
				exportSerailizationFolder.mkdir();
			}

			String zipFileName = CoreCommonConstant.EMPTY_STRING;
			String zipPathname = CoreCommonConstant.EMPTY_STRING;
			List<FileItem> items;

			try {
				items = upload.parseRequest(req);
				for (final FileItem item : items) {

					if (!item.isFormField()) {
						zipFileName = item.getName();
						if (zipFileName != null) {
							zipFileName = zipFileName.substring(zipFileName.lastIndexOf(File.separator) + 1);
						}
						zipPathname = exportSerailizationFolderPath + File.separator + zipFileName;
						// get only the file name not whole path
						if (zipFileName != null) {
							zipFileName = FilenameUtils.getName(zipFileName);
						}
						try {
							instream = item.getInputStream();
							tempZipFile = new File(zipPathname);
							if (tempZipFile.exists()) {
								tempZipFile.delete();
							}
							out = new FileOutputStream(tempZipFile);
							final byte buf[] = new byte[1024];
							int len;
							while ((len = instream.read(buf)) > 0) {
								out.write(buf, 0, len);
							}
						} catch (final FileNotFoundException fileNotFoundException) {
							log.error("Unable to create the export folder." + fileNotFoundException, fileNotFoundException);
							printWriter.write("Unable to create the export folder.Please try again.");

						} catch (final IOException ioException) {
							log.error("Unable to read the file." + ioException, ioException);
							printWriter.write("Unable to read the file.Please try again.");
						} finally {
							if (out != null) {
								try {
									out.close();
								} catch (final IOException ioException) {
									log.info("Could not close stream for file." + tempZipFile);
								}
							}
							if (instream != null) {
								try {
									instream.close();
								} catch (final IOException ioException) {
									log.info("Could not close stream for file." + zipFileName);
								}
							}
						}
					}
				}
			} catch (final FileUploadException fileUploadException) {
				log.error("Unable to read the form contents." + fileUploadException, fileUploadException);
				printWriter.write("Unable to read the form contents. Please try again.");
			}
			tempOutputUnZipDir = exportSerailizationFolderPath + File.separator
					+ zipFileName.substring(0, zipFileName.lastIndexOf(CoreCommonConstant.DOT)) + System.nanoTime();
			try {
				FileUtils.unzip(tempZipFile, tempOutputUnZipDir);
			} catch (final Exception exception) {
				log.error("Unable to unzip the file." + exception, exception);
				printWriter.write("Unable to unzip the file. Please try again.");
				tempZipFile.delete();
			}

		} else {
			log.error("Request contents type is not supported.");
			printWriter.write("Request contents type is not supported.");
		}
		if (tempZipFile != null) {
			tempZipFile.delete();
		}

	printWriter.append(SystemConfigSharedConstants.FILE_PATH).append(tempOutputUnZipDir);
		//printWriter.append("filePath:").append(tempOutputUnZipDir);
		printWriter.append(CoreCommonConstant.PIPE);
		printWriter.flush();

	}
}
