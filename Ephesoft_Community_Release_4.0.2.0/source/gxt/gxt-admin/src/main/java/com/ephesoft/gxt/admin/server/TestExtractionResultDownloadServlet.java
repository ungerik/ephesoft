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
import java.util.zip.ZipOutputStream;

import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.core.server.DCMAHttpServlet;

/**
 * Servlet implementation class TestContentResultDownloadServlet
 */
public class TestExtractionResultDownloadServlet extends DCMAHttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * CONTENT_TYPE String.
	 */
	private static final String APPLICATION_ZIP = "application/x-zip\r\n";

	/**
	 * CONTENT_DISPOSITION String.
	 */
	private static final String CONTENT_DISPOSITION = "Content-Disposition";

	/**
	 * UNDER_SCORE String.
	 */
	private static final String UNDER_SCORE = "_";

	/**
	 * LOGGER Logger.
	 */
	private static EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(TestContentResultDownloadServlet.class);

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TestExtractionResultDownloadServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		LOGGER.info("Downloading the result file.");
		final String identifier = request.getParameter("batchClassIdentifier");

		final String zipFileName = EphesoftStringUtil.concatenate(identifier, UNDER_SCORE, "Result");
		LOGGER.info("Zip File Name is " + zipFileName);
		response.setContentType(APPLICATION_ZIP);
		final String downloadedFile = EphesoftStringUtil.concatenate(zipFileName, ".zip", "\"\r\n");
		response.setHeader(CONTENT_DISPOSITION, "attachment; filename=\"" + downloadedFile);
		final BatchSchemaService batchService = this.getSingleBeanOfType(BatchSchemaService.class);
		StringBuilder folderPathBuilder = new StringBuilder(batchService.getBaseFolderLocation());
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(identifier);
		File batchClassFolder = new File(folderPathBuilder.toString());
		if (!batchClassFolder.exists()) {
			LOGGER.info("Batch Class Folder does not exist.");
			throw new IOException("Unable to download extraction result. Batch Class Folder does not exist.");
		}

		// The path for test classification folder is found.......
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(BatchClassConstants.TEST_EXTRACTION_FOLDER_NAME);
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append("test-extraction-result");
		System.out.println("The path is : " + folderPathBuilder.toString());
		File file = new File(folderPathBuilder.toString());
		if (!file.exists()) {
			throw new IOException("Unable to download the files. The file does not exist.");
		}
		LOGGER.info("File path is " + folderPathBuilder.toString());
		ServletOutputStream out = null;
		ZipOutputStream zout = null;
		try {
			out = response.getOutputStream();
			zout = new ZipOutputStream(out);
			FileUtils.zipDirectoryWithFullName(folderPathBuilder.toString(), zout);
		} catch (IOException ioException) {
			LOGGER.error("Unable to download the files." + ioException, ioException);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Problem occured in downloading.");
		} finally {
			IOUtils.closeQuietly(zout);
			IOUtils.closeQuietly(out);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
