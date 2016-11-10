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

package com.ephesoft.gxt.batchinstance.server;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.gxt.batchinstance.client.shared.constants.BatchInfoConstants;
import com.ephesoft.gxt.core.server.DCMAHttpServlet;

/**
 * This class is the servlet for the downloading the batch instance log file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.server.DCMAHttpServlet
 * 
 */
public class LogFileDownloadServlet extends DCMAHttpServlet {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Instance of Logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(LogFileDownloadServlet.class);

	@Override
	public final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		doGet(request, response);
	}

	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		LOGGER.info("Downloading the log files.");
		final String batchInstanceIdentifier = request.getParameter(BatchInfoConstants.BATCH_INSTANCE_IDENTIFIER);
		final String batchInstanceLogFilePath = request.getParameter(BatchInfoConstants.BI_LOG_FILE_PATH);
		LOGGER.debug("Batch Instance Identifier is : ", batchInstanceIdentifier);
		LOGGER.debug("Batch Instance log file path is : ", batchInstanceLogFilePath);

		if (EphesoftStringUtil.isNullOrEmpty(batchInstanceIdentifier) || EphesoftStringUtil.isNullOrEmpty(batchInstanceLogFilePath)) {
			LOGGER.error("Either the batch instance identifier or batch instance log file path is null.");
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occured in downloading.");
		} else {
			final String zipFileName = EphesoftStringUtil.concatenate(batchInstanceIdentifier, BatchInfoConstants.UNDER_SCORE,
					BatchInfoConstants.LOG_FOLDER);
			LOGGER.debug("Zip File Name is ", zipFileName);
			response.setContentType(BatchInfoConstants.APPLICATION_ZIP);
			final String downloadedFile = EphesoftStringUtil.concatenate(zipFileName, BatchInfoConstants.ZIP_EXT, "\"\r\n");
			response.setHeader(BatchInfoConstants.CONTENT_DISPOSITION, "attachment; filename=\"" + downloadedFile);
			LOGGER.debug("File path is ", batchInstanceLogFilePath);
			File file = new File(batchInstanceLogFilePath);
			if (file.exists()) {
				ServletOutputStream out = null;
				ZipOutputStream zout = null;
				try {
					out = response.getOutputStream();
					zout = new ZipOutputStream(out);
					FileUtils.zipMultipleFiles(batchInstanceLogFilePath, zout, downloadedFile);
					response.setStatus(HttpServletResponse.SC_OK);
				} catch (IOException ioException) {
					LOGGER.error(ioException, "Unable to download the files.");
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error occured in downloading.");
				} finally {
					IOUtils.closeQuietly(zout);
					IOUtils.closeQuietly(out);
				}
			} else {
				throw new IOException("Error Occurred in download log file because no log file exists.");
			}
		}
	}
}
