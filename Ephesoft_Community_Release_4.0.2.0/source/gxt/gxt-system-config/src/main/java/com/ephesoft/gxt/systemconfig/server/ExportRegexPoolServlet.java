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
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.SerializationUtils;

import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.da.domain.RegexGroup;
import com.ephesoft.dcma.da.service.RegexGroupService;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.gxt.core.server.DCMAHttpServlet;
import com.ephesoft.gxt.core.server.util.RegexUtil;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;

/**
 * Servlet for exporting regex pool.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Sep 12, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class ExportRegexPoolServlet extends DCMAHttpServlet {

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * LOGGER {@link EphesoftLogger} The instance of Logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ExportRegexPoolServlet.class);

	/**
	 * Overridden doPost method.
	 */
	@Override
	public final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		doGet(request, response);
	}

	/**
	 * Overridden doGet method.
	 */
	@Override
	public void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
		final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		final RegexGroupService regexGroupService = this.getSingleBeanOfType(RegexGroupService.class);
		final List<RegexGroup> regexGroupList = regexGroupService.getRegexGroups();

		if (regexGroupList == null) {
			LOGGER.error("Regex group list is null.");
			resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Regex group list is null.");
		} else {
			try {
				final Calendar cal = Calendar.getInstance();
				final String exportSerailizationFolderPath = batchSchemaService.getBatchExportFolderLocation();

				final SimpleDateFormat formatter = new SimpleDateFormat(CoreCommonConstant.DATE_FORMAT);
				final String formattedDate = formatter.format(new Date());
				final String zipFileName = StringUtil.concatenate(SystemConfigConstants.REGEX_POOL,
						CoreCommonConstant.UNDERSCORE, formattedDate, CoreCommonConstant.UNDERSCORE, cal.get(Calendar.HOUR_OF_DAY),
						cal.get(Calendar.SECOND));
				final String tempFolderLocation = StringUtil.concatenate(exportSerailizationFolderPath, File.separator,
						zipFileName);
				final File copiedFolder = new File(tempFolderLocation);

				if (copiedFolder.exists()) {
					copiedFolder.delete();
				}

				// Setting the parent of regex pattern list to null for exporting
				RegexUtil.exportRegexPatterns(regexGroupList);

				// Setting the id of regex group list to "0" for exporting
				for (final RegexGroup regexGroup : regexGroupList) {
					regexGroup.setId(0);
				}
				copiedFolder.mkdirs();

				final File serializedExportFile = new File(tempFolderLocation + File.separator + SystemConfigConstants.REGEX_POOL
						+ SystemConfigConstants.SERIALIZATION_EXT);

				try {
					SerializationUtils.serialize((Serializable) regexGroupList, new FileOutputStream(serializedExportFile));
					final File originalFolder = new File(StringUtil.concatenate(batchSchemaService.getBaseSampleFDLock(),
							File.separator, SystemConfigConstants.REGEX_POOL));

					if (originalFolder.isDirectory()) {
						final String[] folderList = originalFolder.list();
						Arrays.sort(folderList);
					}
				} catch (final FileNotFoundException fileNotFoundException) {
					// Unable to read serializable file
					LOGGER.error("Error occurred while creating the serializable file." + fileNotFoundException, fileNotFoundException);
					resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
							"Error occurred while creating the serializable file.");
				}

				resp.setContentType(SystemConfigConstants.APPLICATION_X_ZIP);
				resp.setHeader(SystemConfigConstants.CONTENT_DISPOSITION, StringUtil.concatenate(
						SystemConfigConstants.ATTACHMENT_FILENAME, zipFileName, SystemConfigConstants.ZIP_EXT,
						SystemConfigConstants.HEADER_MODE));
				ServletOutputStream out = null;
				ZipOutputStream zipOutput = null;
				try {
					out = resp.getOutputStream();
					zipOutput = new ZipOutputStream(out);
					FileUtils.zipDirectory(tempFolderLocation, zipOutput, zipFileName);
					resp.setStatus(HttpServletResponse.SC_OK);
				} catch (final IOException ioException) {
					// Unable to create the temporary export file(s)/folder(s)
					LOGGER.error("Error occurred while creating the zip file." + ioException, ioException);
					resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to export.Please try again.");
				} finally {
					// clean up code
					if (zipOutput != null) {
						zipOutput.close();
					}
					if (out != null) {
						out.flush();
					}
					FileUtils.deleteDirectoryAndContentsRecursive(copiedFolder);
				}
			} catch (final Exception exception) {
				LOGGER.error("ERROR IN EXPORT:  " + exception, exception);
				resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to export.Please try again.");
			}
		}
	}
}
