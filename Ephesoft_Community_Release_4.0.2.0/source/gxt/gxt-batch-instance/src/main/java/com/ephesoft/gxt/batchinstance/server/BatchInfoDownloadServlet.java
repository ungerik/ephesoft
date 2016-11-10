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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ClusterPropertyService;
import com.ephesoft.dcma.da.service.ServerRegistryService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FTPInformation;
import com.ephesoft.dcma.util.FTPUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.IUtilCommonConstants;
import com.ephesoft.dcma.util.exception.FTPDataUploadException;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.shared.constants.BatchInfoConstants;
import com.ephesoft.gxt.core.server.DCMAHttpServlet;

/**
 * Servlet for downloading the batch instance folder, log file, database dump and batch class folder.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jul 25, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class BatchInfoDownloadServlet extends DCMAHttpServlet implements BatchInfoConstants {

	/**
	 * PATH_SPECIFIED_IS_NOT_VALID {@link String} the error message for invalid path.
	 */
	private static final String PATH_SPECIFIED_IS_NOT_VALID = "Path specified is not valid.";

	/**
	 * serialVersionUID long.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * LOGGER Logger {@link Logger}.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchInfoDownloadServlet.class);

	/**
	 * downloadToFolderPath {@link String}.
	 */
	private String downloadToFolderPath;

	/**
	 * batchClassCheckBox {@link String}.
	 */
	private String batchClassCheckBox;

	/**
	 * batchInstanceCheckBox {@link String}.
	 */
	private String batchInstanceCheckBox;

	/**
	 * logFileCheckBox {@link String}.
	 */
	private String logFileCheckBox;

	/**
	 * dataBaseDumpCheckBox {@link String}.
	 */
	private String dataBaseDumpCheckBox;

	/**
	 * javaAppServerCheckBox {@link String}.
	 */
	private String javaAppServerCheckBox;

	/**
	 * libFolderCheckBox {@link String}.
	 */
	private String libFolderCheckBox;

	/**
	 * imageClassificationCheckBox {@link String}.
	 */
	private String imageClassificationCheckBox;

	/**
	 * luceneSearchClassificationSample {@link String}.
	 */
	private String luceneSearchClassificationSample;

	/**
	 * batchlogCheckBox {@link String}.
	 */
	private String batchlogCheckBox;

	/**
	 * metaInfCheckBox {@link String}.
	 */
	private String metaInfCheckBox;

	/**
	 * uncFolderCheckBox; {@link String}.
	 */
	private String uncFolderCheckBox;

	/**
	 * licenseFolderCheckBox {@link String}.
	 */
	private String licenseFolderCheckBox;

	/**
	 * applicationFolderCheckBox {@link String}.
	 */
	private String applicationFolderCheckBox;

	/**
	 * selectedRadioButton {@link String}.
	 */
	private String selectedRadioButton;

	/**
	 * base folder path {@link String}.
	 */
	private String baseFolderPath;

	/**
	 * local folder path {@link String}.
	 */
	private String localFolderPath;

	/**
	 * application folder path {@link String}.
	 */
	private String applicationFolderPath;

	/**
	 * username {@link String} the username for ftp login.
	 */
	private String username;

	/**
	 * password {@link String} the password for ftp login.
	 */
	private String password;

	/**
	 * ftpServerURL {@link String} ftp URL.
	 */
	private String ftpServerURL;

	/**
	 * ticketNo {@link String} the client raised ticket id for which trouble shooting resources are needed.
	 */
	private String ticketNo;

	/** Instance of CopyTroubleshootingArtifacts for copying artifacts. */
	private CopyTroubleshootingArtifacts copyTroubleshootingArtifacts;

	@Override
	public final void doPost(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		PrintWriter writer = null;
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(request
				.getParameter(BATCH_INSTANCE_IDENTIFIER));
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		BatchClass batchClass = null;
		if (null != batchInstance) {
			batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchInstance.getBatchClass().getIdentifier());
		}
		baseFolderPath = batchSchemaService.getBaseFolderLocation();
		localFolderPath = batchSchemaService.getLocalFolderLocation();
		applicationFolderPath = System.getenv(DCMA_HOME);
		getParameterValues(request);
		copyTroubleshootingArtifacts = new CopyTroubleshootingArtifacts(applicationFolderPath, localFolderPath);

		if (UPLOAD_RADIO.equalsIgnoreCase(selectedRadioButton)) {
			String ftpFolderPath = uploadToFTPPath(batchInstance, batchSchemaService, batchClass, response);
			if (ftpFolderPath == null) {

				// Removed response.sendError code to fix JIRA bug id EPHESOFT-12355. Did not returned expected results.
				writer = response.getWriter();
				writer.write(BatchInstanceConstants.ERROR_CODE_TEXT);
				response.setStatus(HttpServletResponse.SC_OK);
				return;
			} else {
				writer = response.getWriter();
				response.setContentType(IUtilCommonConstants.CONTENT_TYPE_HTML);
			}
			if (writer != null) {
				writer.write(ftpFolderPath);
				writer.flush();
			}
			response.setStatus(HttpServletResponse.SC_OK);

		} else {
			downloadOrDownloadtoPath(response, writer, batchInstance, batchSchemaService, batchClass);
		}
	}

	/**
	 * Creates a zipped file containing the selected artifacts and downloads it if Download was selected or copies it to the path
	 * entered by user if DownloadTo was selected.
	 * 
	 * @param response {@link HttpServletResponse} the response object.
	 * @param writer {@link PrintWriter} the writer object associated with response.
	 * @param batchInstance {@link BatchInstance} th batch instance.
	 * @param batchSchemaService {@link BatchSchemaService}
	 * @param batchClass {@link BatchClass} the batch class.
	 * @throws IOException {@link IOException}
	 */
	private void downloadOrDownloadtoPath(final HttpServletResponse response, PrintWriter writer, final BatchInstance batchInstance,
			final BatchSchemaService batchSchemaService, final BatchClass batchClass) throws IOException {
		File downloadToFolder = null;

		// checks if the download to folder path is valid
		if (DOWNLOAD_TO_RADIO.equalsIgnoreCase(selectedRadioButton)) {
			downloadToFolder = new File(downloadToFolderPath);
			downloadToFolder.mkdirs();
			if (!downloadToFolder.exists()) {

				// Removed response.sendError code to fix JIRA bug id EPHESOFT-12355. Did not returned expected results.
				log.error(PATH_SPECIFIED_IS_NOT_VALID);
				writer = response.getWriter();
				writer.write(BatchInstanceConstants.ERROR_CODE_TEXT);
				response.setStatus(HttpServletResponse.SC_OK);
				return;
			}
			writer = response.getWriter();
			response.setContentType(IUtilCommonConstants.CONTENT_TYPE_HTML);
		}

		ServletOutputStream out = null;
		ZipOutputStream zout = null;
		FileOutputStream fout = null;
		String zippedFilePath = null;
		String batchInstanceIdentifier = null;
		if (null != batchInstance) {
			batchInstanceIdentifier = batchInstance.getIdentifier();
		}
		String zipFileName = createZipFileName(batchInstanceIdentifier);
		String downloadFolderPath = createDownloadFolderPathName(batchInstanceIdentifier, baseFolderPath);
		File downloadFolder = new File(downloadFolderPath);
		downloadFolder.mkdirs();

		if (downloadFolder.exists()) {
			// copies the selected folders to download folder
			copySelectedFolders(batchInstance, batchSchemaService, batchClass, downloadFolderPath, downloadFolder);
			if (writer != null) {
				writer.write(copyTroubleshootingArtifacts.getFoldersNotCopied());
			}
			if (downloadFolderPath.lastIndexOf(File.separator) != -1) {
				zippedFilePath = EphesoftStringUtil.concatenate(
						downloadFolderPath.substring(0, downloadFolderPath.lastIndexOf(File.separator)), File.separator, zipFileName,
						ZIP_EXT);
				LOGGER.info(zippedFilePath);
			} else {
				LOGGER.error(EphesoftStringUtil.concatenate("zipped file path ", zippedFilePath));
				return;
			}
			try {
				if (0 != downloadFolder.list().length) {
					// when download button is clicked
					if (DOWNLOAD_RADIO.equalsIgnoreCase(selectedRadioButton)) {
						response.setContentType(IUtilCommonConstants.CONTENT_TYPE_ZIP);
						response.setHeader("Content-Disposition",
								EphesoftStringUtil.concatenate("attachment; filename=\"", zipFileName, ZIP_EXT, "\"\r\n"));
						out = response.getOutputStream();
						zout = new ZipOutputStream(out);
					} else {
						fout = new FileOutputStream(zippedFilePath);
						zout = new ZipOutputStream(fout);
					}
					FileUtils.zipDirectoryWithFullName(downloadFolderPath, zout);
				}

				// if download to is selected copy the zip file to the download to path
				if (DOWNLOAD_TO_RADIO.equalsIgnoreCase(selectedRadioButton)
						&& !new File(EphesoftStringUtil.concatenate(downloadToFolderPath, File.separator, zipFileName, ZIP_EXT))
								.exists() && 0 != downloadFolder.list().length) {
					copyZipFileToDownloadToFolder(zippedFilePath, zipFileName);
				}
				if (out != null) {
					out.flush();
				}
				if (writer != null) {
					writer.flush();
				}
				response.setStatus(HttpServletResponse.SC_OK);
			} catch (IOException ioException) {

				// Unable to create the temporary export file(s)/folder(s)
				log.error(EphesoftStringUtil.concatenate("Error occurred while creating the zip file. ", ioException.getMessage()),
						ioException);
			} finally {

				// clean up code
				closeResources(out, zout, fout, writer);
				if (downloadFolder != null) {
					FileUtils.deleteDirectoryAndContentsRecursive(downloadFolder);
				}
			}
		} else {
			LOGGER.error("Unable to create download folder.");
		}
	}

	@Override
	public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		doPost(request, response);
	}

	/**
	 * This method copies the selected folders to the download folder.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param batchSchemaService {@link BatchSchemaService}
	 * @param batchClass {@link BatchClass}
	 * @param downloadFolderPath {@link String}
	 * @param downloadFolder {@link File}
	 */
	private void copySelectedFolders(final BatchInstance batchInstance, final BatchSchemaService batchSchemaService,
			final BatchClass batchClass, final String downloadFolderPath, final File downloadFolder) {
		ServerRegistryService serverRegistryService = this.getSingleBeanOfType(ServerRegistryService.class);

		// get the property of licence server failover switch. Fix for issue EPHESOFT-12358.
		ClusterPropertyService clusterPropertyService = this.getSingleBeanOfType(ClusterPropertyService.class);

		// batch instance checkbox is checked
		if (batchInstanceCheckBox != null) {
			// Bug fix for JIRA ID - 8997 the batch instance folder is downloaded from final drop folder when it is in finished state.
			if (batchInstance.getStatus() == BatchInstanceStatus.FINISHED) {
				copyTroubleshootingArtifacts.copyBatchInstanceFolder(downloadFolder, batchInstance,
						batchSchemaService.getExportFolderLocation());
			} else {
				copyTroubleshootingArtifacts.copyBatchInstanceFolder(downloadFolder, batchInstance, localFolderPath);
			}
		}

		// log file checkbox is checked
		if (logFileCheckBox != null || javaAppServerCheckBox != null || batchlogCheckBox != null) {
			copyTroubleshootingArtifacts.copyLogFolder(downloadFolder, batchInstance, javaAppServerCheckBox, logFileCheckBox,
					batchlogCheckBox, serverRegistryService);
		}

		// batch class checkbox is checked
		// Bug fix for JIRA ID - 8996 the default exported batch class is downloaded even if the check box for it is not selected when
		// either lucene search or image classification
		// checkbox is selected.
		if (batchClassCheckBox != null || imageClassificationCheckBox != null || luceneSearchClassificationSample != null) {
			String tempFolderLocation = EphesoftStringUtil.concatenate(downloadFolder, File.separator, batchInstance.getBatchClass()
					.getIdentifier());
			copyTroubleshootingArtifacts.copyBatchClassFolder(batchSchemaService, imageClassificationCheckBox,
					luceneSearchClassificationSample, batchClass, tempFolderLocation, downloadFolder);
		}

		// database dump checkbox is checked
		if (dataBaseDumpCheckBox != null) {
			copyTroubleshootingArtifacts.createDBDump(downloadFolderPath);
		}

		// lib checkbox is checked
		if (libFolderCheckBox != null && applicationFolderCheckBox == null) {
			copyTroubleshootingArtifacts.copyLibFolder(downloadFolder);
		}

		// meta-inf checkbox is checked
		if (metaInfCheckBox != null && applicationFolderCheckBox == null) {
			copyTroubleshootingArtifacts.copyMetaInfFolder(downloadFolder);
		}

		// unc folder checkbox is checked
		if (uncFolderCheckBox != null) {
			copyTroubleshootingArtifacts.copyUncFolder(downloadFolder, batchInstance);
		}

		// application folder checkbox is checked
		if (applicationFolderCheckBox != null) {
			copyTroubleshootingArtifacts.copyApplicationFolder(downloadFolder);
		}
	}

	/**
	 * This method creates the zip file name.
	 * 
	 * @param batchInstanceIdentifier {@link String}.
	 * @return zip file name
	 */
	private String createZipFileName(final String batchInstanceIdentifier) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_MMDDYY, Locale.getDefault());
		String formattedDate = formatter.format(new Date());
		String zipFileName = null;
		if (!EphesoftStringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			zipFileName = EphesoftStringUtil.concatenate(getVersionNumber(), UNDERSCORE, batchInstanceIdentifier, UNDERSCORE,
					TROUBLESHOOT, UNDERSCORE, formattedDate, UNDERSCORE, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.SECOND));
		} else {
			zipFileName = EphesoftStringUtil.concatenate(getVersionNumber(), UNDERSCORE, TROUBLESHOOT, UNDERSCORE, formattedDate,
					UNDERSCORE, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.SECOND));
		}
		if (!EphesoftStringUtil.isNullOrEmpty(ticketNo)) {
			zipFileName = EphesoftStringUtil.concatenate(zipFileName, UNDERSCORE, ticketNo);
		}
		return zipFileName;
	}

	/**
	 * This method creates the download folder path name.
	 * 
	 * @param batchInstanceIdentifier {@link String}.
	 * @return download folder path name
	 */
	private String createDownloadFolderPathName(final String batchInstanceIdentifier, final String parentPath) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_MMDDYY, Locale.getDefault());
		String formattedDate = formatter.format(new Date());
		String downloadFolderPath;
		if (!EphesoftStringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			downloadFolderPath = EphesoftStringUtil.concatenate(parentPath, File.separator, TROUBLESHOOT, File.separator,
					batchInstanceIdentifier, UNDERSCORE, formattedDate, UNDERSCORE, cal.get(Calendar.HOUR_OF_DAY), UNDERSCORE,
					cal.get(Calendar.SECOND));
		} else {
			downloadFolderPath = EphesoftStringUtil.concatenate(parentPath, File.separator, TROUBLESHOOT, File.separator,
					formattedDate, UNDERSCORE, cal.get(Calendar.HOUR_OF_DAY), UNDERSCORE, cal.get(Calendar.SECOND));
		}
		return downloadFolderPath;
	}

	/**
	 * This method gets the parameter values from request.
	 * 
	 * @param request {@link HttpServletRequest}.
	 */
	private void getParameterValues(final HttpServletRequest request) {

		batchClassCheckBox = request.getParameter(BATCH_CLASS_FOLDER);
		imageClassificationCheckBox = request.getParameter(IMAGE_CLASSIFICATION_SAMPLE);
		luceneSearchClassificationSample = request.getParameter(LUCENE_SEARCH_CLASSIFICATION_SAMPLE);

		libFolderCheckBox = request.getParameter(LIB);
		metaInfCheckBox = request.getParameter(META_INF);
		applicationFolderCheckBox = request.getParameter(APPLICATION);

		logFileCheckBox = request.getParameter(APPLICATION_LOGS);
		javaAppServerCheckBox = request.getParameter(JAVA_APP_SERVER);
		batchlogCheckBox = request.getParameter(BATCH_LOGS);

		batchInstanceCheckBox = request.getParameter(BATCH_INSTANCE_FOLDER);

		dataBaseDumpCheckBox = request.getParameter(DATABASE_DUMP_FOLDER);

		uncFolderCheckBox = request.getParameter(UNC_FOLDER);

		licenseFolderCheckBox = request.getParameter(LICENCE_DETAIL);

		username = request.getParameter(USERNAME_TEXT_BOX);
		password = request.getParameter(PASSWORD_TEXT_BOX);
		ftpServerURL = request.getParameter(FTP_PATH_TEXT_BOX);
		ticketNo = request.getParameter(TICKET_TEXT_BOX);

		selectedRadioButton = request.getParameter(SELECTED_RADIO_BUTTON);
		downloadToFolderPath = request.getParameter(PATH_TEXT_BOX);
	}

	/**
	 * This method fetches the version number.
	 * 
	 * @return versionNumber the version number.
	 */
	private String getVersionNumber() {
		String versionNumber = "";
		try {
			Properties properties = ApplicationConfigProperties.getApplicationConfigProperties().getAllProperties(
					APPLICATION_PROPERTY_FILE_NAME);
			if (null != properties) {
				versionNumber = properties.getProperty(PRODUCT_VERSION);
			}
		} catch (IOException e) {
			LOGGER.error(EphesoftStringUtil.concatenate("Error occured in getting version number from application.properties.",
					e.getMessage()));
		}
		return versionNumber;
	}

	/**
	 * This method uploads the folder containing information related to a batch on a ftp server.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param batchSchemaService {@link BatchSchemaService}
	 * @param batchClass {@link BatchClass}
	 * @param response {@link HttpServletResponse}
	 * 
	 * @return {@link String} the path where file is uploaded
	 */
	private String uploadToFTPPath(final BatchInstance batchInstance, final BatchSchemaService batchSchemaService,
			final BatchClass batchClass, final HttpServletResponse response) {
		File zipFolder = null;
		String ftpFolderPath = null;
		String batchInstanceIdentifier = null;
		if (null != batchInstance) {
			batchInstanceIdentifier = batchInstance.getIdentifier();
		}
		File tempFolder = new File(createDownloadFolderPathName(batchInstanceIdentifier, baseFolderPath));
		tempFolder.mkdirs();
		if (tempFolder.exists()) {
			FileOutputStream fout = null;
			ZipOutputStream zout = null;
			try {
				PrintWriter writer = response.getWriter();
				response.setContentType(IUtilCommonConstants.CONTENT_TYPE_HTML);
				String zipFileName = createZipFileName(batchInstanceIdentifier);
				String zipFolderPath = EphesoftStringUtil.concatenate(baseFolderPath, File.separator, TROUBLESHOOT, File.separator,
						zipFileName);

				String zipFilePath = EphesoftStringUtil.concatenate(zipFolderPath, File.separator, zipFileName, ZIP_EXT);
				zipFolder = new File(zipFolderPath);
				zipFolder.mkdirs();
				fout = new FileOutputStream(zipFilePath);
				zout = new ZipOutputStream(fout);
				Properties properties = getFTPProperties();
				if (null != properties) {
					ftpFolderPath = EphesoftStringUtil.concatenate(TROUBLESHOOT, File.separator, zipFileName, ZIP_EXT);
					FTPInformation ftpInformation = new FTPInformation(ftpServerURL, username, password,
							properties.getProperty(UPLOAD_BASE_DIR), Integer.parseInt(properties.getProperty(NUM_OF_RETRIES)),
							properties.getProperty(DATA_TIMEOUT), zipFolderPath, TROUBLESHOOT, 2);
					try {
						FTPClient client = new FTPClient();
						FTPUtil.createConnection(client, ftpServerURL, username, password, properties.getProperty(DATA_TIMEOUT));
						client.disconnect();
						copySelectedFolders(batchInstance, batchSchemaService, batchClass, tempFolder.getPath(), tempFolder);
						if (null != tempFolder.list()) {
							FileUtils.zipDirectoryWithFullName(tempFolder.getPath(), zout);
							FTPUtil.uploadDirectory(ftpInformation, false);
						}
						writer.write(copyTroubleshootingArtifacts.getFoldersNotCopied());
					} catch (SocketException socketException) {
						LOGGER.error("Could not connect to the FTP client. Socket Exception", socketException.getMessage());
						ftpFolderPath = null;

						// Removed response.sendError code to fix JIRA bug id EPHESOFT-12355. Did not returned expected results.
					} catch (final IOException ioException) {
						LOGGER.error(" Invalid Credentials. Could not connect to the FTP client.", ioException.getMessage());
						ftpFolderPath = null;
						writer.write("INVALID_CREDENTIALS");
						response.setStatus(HttpServletResponse.SC_OK);
						// Removed response.sendError code to fix JIRA bug id EPHESOFT-12355. Did not returned expected results.
					}
				}
			} catch (FTPDataUploadException e) {
				LOGGER.error(EphesoftStringUtil.concatenate("Error occured in uploading the file to path ", ftpFolderPath));
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e) {
				LOGGER.error(EphesoftStringUtil.concatenate("Error occured in getting writer from response.", e.getMessage()));
			} catch (NumberFormatException e) {
				LOGGER.error(EphesoftStringUtil.concatenate("Exception occured in parsing number of retries.", e.getMessage()));
			} finally {
				try {
					closeResources(null, zout, fout, null);
					if (tempFolder != null) {
						FileUtils.deleteDirectoryAndContentsRecursive(tempFolder);
					}
					if (zipFolder != null) {
						FileUtils.deleteDirectoryAndContentsRecursive(zipFolder);
					}
				} catch (IOException e) {
					LOGGER.error(EphesoftStringUtil.concatenate("Exception occured while closing outputstream.", e.getMessage()));
				}
			}
		} else {
			LOGGER.error("Could not create the folder for copying the required folders.");
		}
		return ftpFolderPath;
	}

	/**
	 * This method fetches all the properties from application.properties file.
	 * 
	 * @return properties {@link Properties}
	 */
	private Properties getFTPProperties() {
		Properties properties = null;
		try {
			properties = ApplicationConfigProperties.getApplicationConfigProperties().getAllProperties(FTP_PROPERTY_FILE_NAME);
		} catch (IOException e) {
			LOGGER.error(EphesoftStringUtil.concatenate("Error occured in getting property file name.", e.getMessage()));
		}
		return properties;
	}

	/**
	 * This method copies the zip file to the download to folder path.
	 * 
	 * @param zippedFilePath {@link String} path of zip file to copy.
	 * @param zipFileName {@link String} zip file name.
	 * @throws IOException if an exception occurs while copying the zip file
	 */
	private void copyZipFileToDownloadToFolder(final String zippedFilePath, final String zipFileName) throws IOException {
		downloadToFolderPath = EphesoftStringUtil.concatenate(downloadToFolderPath, File.separator, zipFileName, ZIP_EXT);
		LOGGER.debug(EphesoftStringUtil.concatenate("the download folder path : ", downloadToFolderPath));
		File zippedFile = new File(zippedFilePath);
		FileUtils.copyFile(zippedFile, new File(downloadToFolderPath));
		zippedFile.delete();
	}

	/**
	 * Closes the open resources.
	 * 
	 * @param out {@link ServletOutputStream} the servlet output stream
	 * @param zout {@link ZipOutputStream} the output stream for zip file
	 * @param fout {@link FileOutputStream} the file output stream of zip file
	 * @throws IOException if an exception occurs while closing the resources
	 */
	private void closeResources(final ServletOutputStream out, final ZipOutputStream zout, final FileOutputStream fout,
			final PrintWriter writer) throws IOException {
		if (writer != null) {
			writer.close();
		}
		if (zout != null) {
			zout.close();
		}
		if (out != null) {
			out.close();
		}
		if (fout != null) {
			fout.close();
		}
	}

}
