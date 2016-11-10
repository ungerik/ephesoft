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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipOutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.service.ServerRegistryService;
/*import com.ephesoft.dcma.gwt.core.server.BatchClassUtil;*/
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.gxt.batchinstance.client.shared.constants.BatchInfoConstants;
import com.ephesoft.gxt.core.server.BatchClassUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;

/**
 * Provides methods to copy various artifacts selected by user to download or upload.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 27-Dec-2013 <br/>
 * @version 3.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */

public class CopyTroubleshootingArtifacts implements BatchInfoConstants {

	/**
	 * String containing semicolon separated list of artifacts not copied to the folder.
	 */
	private StringBuilder foldersNotCopied = new StringBuilder();

	/**
	 * local folder path {@link String}.
	 */
	private String localFolderPath;

	/**
	 * application folder path {@link String}.
	 */
	private String applicationFolderPath;

	/**
	 * LOGGER Logger {@link Logger}.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CopyTroubleshootingArtifacts.class);

	/**
	 * webServiceName {@link String} the web service name for copying jaav application server logs.
	 */
	private static final String WEB_SERVICE_NAME = "ws/logs";

	/**
	 * OCR Search Regex String
	 */
	private static final String ocrCoreRegex = "Number\\s*of\\s*cores\\s*for\\s*ocring\\s*:\\s*-?[0-9]*";

	/**
	 * OCR core Search String
	 */
	private static final String ocrCoreSearchString = "Number of cores for ocring : ";

	public CopyTroubleshootingArtifacts(String applicationFolderPath, String localFolderPath) {
		this.localFolderPath = localFolderPath;
		this.applicationFolderPath = applicationFolderPath;
	}

	/**
	 * Copies the java application server logs for localhost.
	 * 
	 * @param tempLogFolderLocation {@link String} the temporary log folder location to copy java application server logs
	 * @param logFolderPath {@link String} the path for java application server log folder
	 * @param localhost {@link String} the local host name
	 * 
	 */
	private void copyJavaAppServerLogsForLocalhost(final String tempLogFolderLocation, final String logFolderPath,
			final String localhost) {
		try {
			File logFolder = new File(logFolderPath);
			File tempLogFolder = new File(EphesoftStringUtil.concatenate(tempLogFolderLocation, File.separator,
					JAVA_APP_SERVER_LOG_FOLDER, File.separator, localhost));
			FileUtils.copyFolder(logFolder, tempLogFolder);
		} catch (IOException ioException) {
			LOGGER.error(EphesoftStringUtil.concatenate("Error occured while copying java application server log folder. ",
					ioException.getMessage()));
			foldersNotCopied.append(EphesoftStringUtil.concatenate(JAVA_APP_SERVER, SEMI_COLON));
		}
	}

	/**
	 * Gets the java app server logs by calling a web service on each server.
	 * 
	 * @param tempLogFolderLocation {@link String} the temporary folder to copy the logs
	 * @param applicationFolderPath {@link String} the application folder path
	 */
	private void copyJavaAppServerLogsForAllServers(final String tempLogFolderLocation, final String applicationFolderPath,
			final ServerRegistryService serverRegistryService) {
		String logFolderPath = EphesoftStringUtil.concatenate(
				applicationFolderPath.substring(0, applicationFolderPath.lastIndexOf(File.separator)), File.separator,
				JAVA_APP_SERVER_FOLDER, File.separator, LOG);
		String localhost = EphesoftContext.getHostServerRegistry().getIpAddress();
		boolean isSecure = EphesoftContext.getCurrent().isSecure();
		boolean copiedAllLogs = true;
		List<ServerRegistry> serverRegistryList = serverRegistryService.getActiveServers();
		if (!CollectionUtil.isEmpty(serverRegistryList)) {
			HttpClient httpClient = null;
			String webServiceURL = null;
			PostMethod postMethod = null;
			for (ServerRegistry serverRegistry : serverRegistryList) {
				if (!localhost.equalsIgnoreCase(serverRegistry.getIpAddress())) {
					httpClient = new HttpClient();
					webServiceURL = getWebServiceUrl(serverRegistry, isSecure, WEB_SERVICE_NAME);
					postMethod = createRequest(tempLogFolderLocation, webServiceURL, serverRegistry);
					try {
						int statusCode = httpClient.executeMethod(postMethod);

						if (statusCode == ICommonConstants.STATUS_OK) {
							LOGGER.debug(EphesoftStringUtil.concatenate("Logs service successful"));
						} else {
							LOGGER.error(EphesoftStringUtil.concatenate("Logs service unsuccessful and status code: ", statusCode));
						}
					} catch (HttpException httpException) {
						LOGGER.error(EphesoftStringUtil.concatenate(
								"Could not connect to server for log service due to a protocol exception :", ICommonConstants.SPACE,
								httpException));
					} catch (IOException ioException) {
						LOGGER.error(EphesoftStringUtil.concatenate(
								"Could not connect to server for log service due to a transport error :", ICommonConstants.SPACE,
								ioException));
					}
				}
			}
		}
		copyJavaAppServerLogsForLocalhost(tempLogFolderLocation, logFolderPath, localhost);
		if (!copiedAllLogs) {
			LOGGER.error("Not all logs copied successfully");
			foldersNotCopied.append(EphesoftStringUtil.concatenate(JAVA_APP_SERVER, SEMI_COLON));
		}
	}

	/**
	 * Creates the web service url using server information and returns it.
	 * 
	 * @param serverRegistry {@link ServerRegistry} the server registry containing server information
	 * @param isSecure {@link String} the protocol http or https
	 * @return {@link String} the web service url
	 */
	private String getWebServiceUrl(final ServerRegistry serverRegistry, final boolean isSecure, final String webServiceName) {
		String webServiceURL = null;
		if (isSecure) {
			webServiceURL = EphesoftStringUtil.concatenate(ICommonConstants.HTTPS, ICommonConstants.COLON,
					ICommonConstants.DOUBLE_FORWARD_SLASH, serverRegistry.getIpAddress(), ICommonConstants.COLON,
					serverRegistry.getPort(), serverRegistry.getAppContext(), ICommonConstants.FORWARD_SLASH, webServiceName);
		} else {
			webServiceURL = EphesoftStringUtil.concatenate(ICommonConstants.HTTP, ICommonConstants.COLON,
					ICommonConstants.DOUBLE_FORWARD_SLASH, serverRegistry.getIpAddress(), ICommonConstants.COLON,
					serverRegistry.getPort(), serverRegistry.getAppContext(), ICommonConstants.FORWARD_SLASH, webServiceName);
		}

		LOGGER.debug(EphesoftStringUtil.concatenate("Web service URL for fetching logs : ", webServiceURL));
		return webServiceURL;
	}

	/**
	 * Creates a post request for the calling a web service with given url.
	 * 
	 * @param tempLogFolderLocation {@link String} the temporary log folder path
	 * @param webServiceURL {@link String} the url of web service
	 * @param serverRegistry {@link ServerRegistry} the server information
	 * @return {@link PostMethod} a post request
	 */
	private PostMethod createRequest(final String tempLogFolderLocation, final String webServiceURL,
			final ServerRegistry serverRegistry) {
		PostMethod postMethod = new PostMethod(webServiceURL);
		Part[] partArray = new Part[1];
		String zipFolderPath = EphesoftStringUtil.concatenate(tempLogFolderLocation, File.separator, JAVA_APP_SERVER_LOG_FOLDER,
				File.separator, serverRegistry.getIpAddress());

		partArray[0] = new StringPart(BatchInfoConstants.ZIP_FOLDER_PATH, zipFolderPath);
		LOGGER.debug(EphesoftStringUtil.concatenate("Log folder path for multiple server logs : ", zipFolderPath));
		MultipartRequestEntity entity = new MultipartRequestEntity(partArray, postMethod.getParams());
		postMethod.setRequestEntity(entity);
		return postMethod;
	}

	/**
	 * This method copies the exported batch class to the download folder.
	 * 
	 * @param batchSchemaService {@link BatchSchemaService} instance of batch schema service
	 * @param imageMagickBaseFolderParam {@link String} the parameter value of checkbox for image magick base folder
	 * @param isSearchSampleNameParam {@link String} the parameter value of checkbox for lucene search classification
	 * @param batchClass {@link BatchClass} the batch class of batch instance
	 * @param tempFolderLocation {@link String} the temporary folder location
	 * @throws IOException if an exception occurs while creating the serialized file.
	 */
	private void createExportedBatchClassFolder(final BatchSchemaService batchSchemaService, final String imageMagickBaseFolderParam,
			final String isSearchSampleNameParam, final BatchClass batchClass, final String tempFolderLocation) throws IOException {

		File copiedFolder = new File(tempFolderLocation);

		if (copiedFolder.exists()) {
			copiedFolder.delete();
		}

		copiedFolder.mkdirs();

		BatchClassUtil.copyModules(batchClass);
		BatchClassUtil.copyDocumentTypes(batchClass);
		BatchClassUtil.copyScannerConfig(batchClass);
		BatchClassUtil.exportEmailConfiguration(batchClass);
		BatchClassUtil.exportUserGroups(batchClass);
		BatchClassUtil.exportBatchClassField(batchClass);
		BatchClassUtil.exportCMISConfiguration(batchClass);

		File serializedExportFile = new File(EphesoftStringUtil.concatenate(tempFolderLocation, File.separator,
				batchClass.getIdentifier(), SERIALIZATION_EXT));

		try {
			SerializationUtils.serialize(batchClass, new FileOutputStream(serializedExportFile));

			File originalFolder = new File(EphesoftStringUtil.concatenate(batchSchemaService.getBaseSampleFDLock(), File.separator,
					batchClass.getIdentifier()));

			if (originalFolder.isDirectory()) {

				final String[] folderList = originalFolder.list();
				Arrays.sort(folderList);

				for (int i = 0; i < folderList.length; i++) {
					if (folderList[i].endsWith(SERIALIZATION_EXT)) {
						// skip previous ser file since new is created.
					} else if (FilenameUtils.getName(folderList[i]).equalsIgnoreCase(
							batchSchemaService.getTestKVExtractionFolderName())
							|| FilenameUtils.getName(folderList[i]).equalsIgnoreCase(batchSchemaService.getTestTableFolderName())
							|| FilenameUtils.getName(folderList[i]).equalsIgnoreCase(
									batchSchemaService.getFileboundPluginMappingFolderName())) {
						// Skip this folder
						continue;
					} else if (FilenameUtils.getName(folderList[i])
							.equalsIgnoreCase(batchSchemaService.getImagemagickBaseFolderName())
							&& !EphesoftStringUtil.isNullOrEmpty(imageMagickBaseFolderParam)) {
						FileUtils.copyDirectoryWithContents(new File(originalFolder, folderList[i]), new File(copiedFolder,
								folderList[i]));
					} else if (FilenameUtils.getName(folderList[i]).equalsIgnoreCase(batchSchemaService.getSearchSampleName())
							&& !EphesoftStringUtil.isNullOrEmpty(isSearchSampleNameParam)) {
						FileUtils.copyDirectoryWithContents(new File(originalFolder, folderList[i]), new File(copiedFolder,
								folderList[i]));
					} else if (!(FilenameUtils.getName(folderList[i]).equalsIgnoreCase(
							batchSchemaService.getImagemagickBaseFolderName()) || FilenameUtils.getName(folderList[i])
							.equalsIgnoreCase(batchSchemaService.getSearchSampleName()))) {
						FileUtils.copyDirectoryWithContents(new File(originalFolder, folderList[i]), new File(copiedFolder,
								folderList[i]));
					}
				}
			}

		} catch (FileNotFoundException e) {
			// Unable to read serializable file
			LOGGER.error(EphesoftStringUtil.concatenate("Error occurred while creating the serializable file. ", e.getMessage()), e);
			foldersNotCopied.append(EphesoftStringUtil.concatenate(BATCH_CLASS_FOLDER, SEMI_COLON));
		} catch (IOException e) {
			// Unable to create the temporary export file(s)/folder(s)
			LOGGER.error(EphesoftStringUtil.concatenate("Error occurred while creating the serializable file.", e.getMessage()), e);
		}
	}

	/**
	 * This method copies the batch instance folder to the download folder.
	 * 
	 * @param downloadFolder {@link File}
	 * @param batchInstance {@link BatchInstance}
	 * @param localFolderPath {@link String}
	 * @param baseFolderPath {@link String}
	 */
	public void copyBatchInstanceFolder(final File downloadFolder, final BatchInstance batchInstance, final String folderPath) {
		if (null != batchInstance && null != downloadFolder && !EphesoftStringUtil.isNullOrEmpty(folderPath)) {
			String batchInstanceFolderPath = EphesoftStringUtil.concatenate(folderPath, File.separator, batchInstance.getIdentifier());
			String tempBatchInstanceFolderLocation = EphesoftStringUtil.concatenate(downloadFolder, File.separator,
					batchInstance.getIdentifier());

			File batchInstanceFolder = new File(batchInstanceFolderPath);
			try {
				FileUtils.copyFolder(batchInstanceFolder, new File(tempBatchInstanceFolderLocation));
			} catch (IOException e) {
				LOGGER.error(EphesoftStringUtil.concatenate("Error occured while copying batch instance folder. ", e.getMessage()));
				foldersNotCopied.append(EphesoftStringUtil.concatenate(BATCH_INSTANCE_FOLDER, SEMI_COLON));
				batchInstanceFolder.delete();
			}
		}
	}

	/**
	 * This method copies the unc folder to download folder.
	 * 
	 * @param downloadFolder {@link File} the download folder
	 * @param batchInstance {@link BatchInstance} the batch instance
	 */
	public void copyUncFolder(final File downloadFolder, final BatchInstance batchInstance) {
		if (null != batchInstance && null != downloadFolder) {
			String uncFolderPath = batchInstance.getBatchClass().getUncFolder();
			String tempUncFolderLocation = EphesoftStringUtil.concatenate(downloadFolder, File.separator, UNC_FOLDER_NAME);
			File uncFolder = new File(uncFolderPath);
			File tempUncFolder = new File(tempUncFolderLocation);
			try {
				FileUtils.copyFolder(uncFolder, tempUncFolder);
			} catch (IOException e) {
				LOGGER.error(EphesoftStringUtil.concatenate("Error occured while copying Unc Folder : ", e.getMessage()));
				foldersNotCopied.append(EphesoftStringUtil.concatenate(UNC_FOLDER, SEMI_COLON));
			}
		}
	}

	/**
	 * This method creates the database dump after reading the database properties from the property file and writes it to the solution
	 * folder.
	 * 
	 * @param dbPropertiesFileName {@link String} database property file hiddenPath
	 */
	public void createDBDump(final String downloadFolderPath) {
		if (!EphesoftStringUtil.isNullOrEmpty(downloadFolderPath)) {
			Properties dbProperties;
			Properties applicationProperties;
			try {
				dbProperties = ApplicationConfigProperties.getApplicationConfigProperties().getAllProperties(DCMA_DATA_ACCESS);
				String dbName = dbProperties.getProperty(DB_NAME);
				String username = dbProperties.getProperty(DB_USERNAME);
				String password = dbProperties.getProperty(DB_PASSWORD);
				String server = dbProperties.getProperty(DB_SERVER);
				String dialect = dbProperties.getProperty(DB_DIALECT);

				applicationProperties = ApplicationConfigProperties.getApplicationConfigProperties().getAllProperties(
						APPLICATION_PROPERTY_FILE_NAME);
				Process process;
				String dbDumpCommand = null;
				if (MYSQL_DIALECT.equals(dialect)) {
					dbDumpCommand = createDBDumpCommandForMySQL(downloadFolderPath, applicationProperties, dbName, username, password,
							server);
				} else if (MSSQL_DIALECT.equals(dialect)) {
					dbDumpCommand = createDBDumpCommandForMsSQL(downloadFolderPath, applicationProperties, dbName, username, password,
							server);
				} else {
					LOGGER.error("INCOMPATIBLE DATABASE");
					foldersNotCopied.append(EphesoftStringUtil.concatenate(DATABASE_DUMP_FOLDER, SEMI_COLON));
				}

				if (!EphesoftStringUtil.isNullOrEmpty(dbDumpCommand)) {
					LOGGER.debug("Creating dump.");
					LOGGER.debug(EphesoftStringUtil.concatenate("database dump command : ", dbDumpCommand));
					process = Runtime.getRuntime().exec(dbDumpCommand);
					try {
						// makes the current thread wait for the completion of the process
						int exitValue = process.waitFor();
						if (exitValue != 0) {
							LOGGER.debug("Error in creating database dump.");
							foldersNotCopied.append(EphesoftStringUtil.concatenate(DATABASE_DUMP_FOLDER, SEMI_COLON));
						}
					} catch (InterruptedException interruptedException) {
						LOGGER.error(EphesoftStringUtil.concatenate("Thread interrupted. ", interruptedException.getMessage()));
					}
				}
			} catch (IOException e) {
				LOGGER.error(EphesoftStringUtil.concatenate("Error in reading dcma-batch.properties. ", e.getMessage()));
			}
		}
	}

	/**
	 * Creates command for generating database dump for MsSQL database.
	 * 
	 * @param downloadFolderPath the folder path where dump will be created.
	 * @param applicationProperties property instance for application.properties.
	 * @param dbName the database name.
	 * @param username database user name.
	 * @param password database password.
	 * @param server database server.
	 * @return the db dump command for MsSQL.
	 */
	private String createDBDumpCommandForMsSQL(final String downloadFolderPath, final Properties applicationProperties,
			final String dbName, final String username, final String password, String server) {
		String dbDumpCommand;
		server = server.replace(":", COMMA);
		String commandName = applicationProperties.getProperty(MSSQL_DB_DUMP_COMMAND);
		String serverParam = applicationProperties.getProperty(MSSQL_SERVER);
		String userParam = applicationProperties.getProperty(MSSQL_USERNAME);
		String passwordParam = applicationProperties.getProperty(MSSQL_PASSWORD);
		String queryParam = applicationProperties.getProperty(MSSQL_QUERY);
		String echoParam = applicationProperties.getProperty(MSSQL_ECHO);
		dbDumpCommand = EphesoftStringUtil.concatenate(commandName, SPACE, echoParam, SPACE, serverParam, SPACE, server, SPACE,
				userParam, SPACE, username, SPACE, passwordParam, SPACE, password, SPACE, queryParam, SPACE, "\"BACKUP DATABASE [",
				dbName, "] TO DISK = '", downloadFolderPath, File.separator, MSSQL_DUMP_FILE, "';\"");
		return dbDumpCommand;
	}

	/**
	 * Creates command for generating database dump for MySQL database.
	 * 
	 * @param downloadFolderPath the folder path where dump will be created.
	 * @param applicationProperties property instance for application.properties.
	 * @param dbName the database name.
	 * @param username database user name.
	 * @param password database password.
	 * @param server database server.
	 * @return the db dump command for MySQL.
	 */
	private String createDBDumpCommandForMySQL(final String downloadFolderPath, final Properties applicationProperties,
			final String dbName, final String username, final String password, final String server) {
		String dbDumpCommand;
		String commandName = applicationProperties.getProperty(MYSQL_DB_DUMP_COMMAND);
		String serverParam = applicationProperties.getProperty(MYSQL_SERVER);
		String userParam = applicationProperties.getProperty(MYSQL_USERNAME);
		String passwordParam = applicationProperties.getProperty(MYSQL_PASSWORD);
		String addDropDB = applicationProperties.getProperty(MYSQL_ADD_DROP);
		String dumpSeveralParam = applicationProperties.getProperty(MYSQL_DUMP_SEVERAL);
		String dumpPathParam = applicationProperties.getProperty(MYSQL_DB_DUMP_PATH);
		dbDumpCommand = EphesoftStringUtil.concatenate(commandName, SPACE, serverParam, SPACE, server, SPACE, userParam, SPACE,
				username, SPACE, passwordParam, password, SPACE, addDropDB, SPACE, dumpSeveralParam, SPACE, dbName, SPACE,
				dumpPathParam, SPACE, downloadFolderPath, File.separator, MYSQL_DUMP_FILE);
		return dbDumpCommand;
	}

	/**
	 * This method copies the exported batch class folder to download folder.
	 * 
	 * @param downloadFolder {@link File}
	 * @param batchInstance {@link BatchInstance}
	 * @param baseFolderPath {@link String}
	 */
	public void copyBatchClassFolder(final BatchSchemaService batchSchemaService, final String isImagemagickBaseFolder,
			final String isSearchSamplName, final BatchClass batchClass, final String tempFolderLocation, final File downloadFolder) {

		// Removed null/empty check for isSearchSamplName & isImagemagickBaseFolder to allow copying of default batch class.
		if ((null != batchSchemaService) && (null != batchClass) && (null != downloadFolder)
				&& !EphesoftStringUtil.isNullOrEmpty(tempFolderLocation)) {
			ZipOutputStream zout;
			FileOutputStream fout;
			try {
				createExportedBatchClassFolder(batchSchemaService, isImagemagickBaseFolder, isSearchSamplName, batchClass,
						tempFolderLocation);
				String zipFileName = batchClass.getIdentifier();
				String zipFilePath = EphesoftStringUtil.concatenate(downloadFolder, File.separator, zipFileName, ZIP_EXT);
				fout = new FileOutputStream(zipFilePath);
				zout = new ZipOutputStream(fout);
				FileUtils.zipDirectory(tempFolderLocation, zout, zipFileName);
				FileUtils.deleteDirectoryAndContentsRecursive(new File(tempFolderLocation));
			} catch (IOException e) {
				LOGGER.error(EphesoftStringUtil.concatenate("Error in creating exported batch class folder. ", e.getMessage()));
				foldersNotCopied.append(EphesoftStringUtil.concatenate(BATCH_CLASS_FOLDER, SEMI_COLON));
			}
		}
	}

	/**
	 * This method copies the log folder to download folder.
	 * 
	 * @param downloadFolder {@link File}
	 * @param batchInstance {@link BatchInstance}
	 * @param baseFolderPath {@link String}
	 */
	public void copyLogFolder(final File downloadFolder, final BatchInstance batchInstance, final String javaAppServerLogs,
			final String dcmaLogs, final String batchLogs, final ServerRegistryService serverRegistryService) {
		if (null != downloadFolder) {
			String tempLogFolderLocation = EphesoftStringUtil.concatenate(downloadFolder, File.separator, LOG);

			String logFolderPath = EphesoftStringUtil.concatenate(applicationFolderPath, File.separator, LOG);
			String applicationFolderPath = System.getenv(DCMA_HOME);

			if (!EphesoftStringUtil.isNullOrEmpty(dcmaLogs)) {
				File allLogFolder = new File(EphesoftStringUtil.concatenate(tempLogFolderLocation, File.separator, ALL_LOG_FOLDER));
				allLogFolder.mkdirs();
				File dcmaLogFolder = new File(logFolderPath);
				try {
					FileUtils.copyFolder(dcmaLogFolder, allLogFolder);
				} catch (IOException e) {
					LOGGER.error(EphesoftStringUtil.concatenate("Error occured while copying application log folder. ", e.getMessage()));
					foldersNotCopied.append(EphesoftStringUtil.concatenate(APPLICATION_LOGS, SEMI_COLON));
				}
			}

			if (!EphesoftStringUtil.isNullOrEmpty(batchLogs) && null != batchInstance) {
				logFolderPath = EphesoftStringUtil.concatenate(localFolderPath, File.separator, batchInstance.getIdentifier(),
						File.separator, LOG);
				File batchLogFolder = new File(logFolderPath);
				try {
					FileUtils.copyFolder(batchLogFolder,
							new File(EphesoftStringUtil.concatenate(tempLogFolderLocation, File.separator, BATCH_LOG_FOLDER)));
				} catch (IOException e) {
					LOGGER.error(EphesoftStringUtil.concatenate("Error occured while copying batch log folder. ", e.getMessage()));
					foldersNotCopied.append(EphesoftStringUtil.concatenate(BATCH_LOGS, SEMI_COLON));
				}
			}

			if (!EphesoftStringUtil.isNullOrEmpty(javaAppServerLogs) && null != serverRegistryService) {
				copyJavaAppServerLogsForAllServers(tempLogFolderLocation, applicationFolderPath, serverRegistryService);
			}
		}
	}

	/**
	 * This method copies the lib folder to download folder.
	 * 
	 * @param downloadFolder {@link File}
	 * @param batchInstance {@link BatchInstance}
	 * @param baseFolderPath {@link String}
	 */
	public void copyLibFolder(final File downloadFolder) {
		if (null != downloadFolder) {
			String tempLibFolderLocation = EphesoftStringUtil.concatenate(downloadFolder, File.separator, LIB);

			String libFolderPath = EphesoftStringUtil.concatenate(applicationFolderPath, File.separator, WEB_INF, File.separator, LIB);

			try {
				FileUtils.copyFolder(new File(libFolderPath), new File(tempLibFolderLocation));
			} catch (IOException e) {
				LOGGER.error(EphesoftStringUtil.concatenate("Error occured while copying lib folder. ", e.getMessage()));
				foldersNotCopied.append(EphesoftStringUtil.concatenate(LIB, SEMI_COLON));
			}
		}
	}

	/**
	 * This method copies the META-INF folder to download folder.
	 * 
	 * @param downloadFolder {@link File}
	 * @param batchInstance {@link BatchInstance}
	 * @param applicationFolderPath {@link String}
	 */
	public void copyMetaInfFolder(final File downloadFolder) {
		if (null != downloadFolder) {
			String tempMetaInfFolderLocation = EphesoftStringUtil.concatenate(downloadFolder, File.separator, META_INF);

			String metaInfFolderPath = EphesoftStringUtil.concatenate(applicationFolderPath, File.separator, WEB_INF, File.separator,
					CLASSES, File.separator, META_INF);

			try {
				FileUtils.copyFolder(new File(metaInfFolderPath), new File(tempMetaInfFolderLocation));
			} catch (IOException e) {
				LOGGER.error(EphesoftStringUtil.concatenate("Error occured while copying META-INF folder. ", e.getMessage()));
				foldersNotCopied.append(EphesoftStringUtil.concatenate(META_INF_LOCALE, SEMI_COLON));
			}
		}
	}

	/**
	 * This method copies the application folder to download folder.
	 * 
	 * @param downloadFolder {@link File} the download folder.
	 */
	public void copyApplicationFolder(final File downloadFolder) {
		if (null != downloadFolder) {
			String tempApplicationFolderLocation = EphesoftStringUtil.concatenate(downloadFolder, File.separator,
					APPLICATION_FOLDER_NAME);

			File applicationFolder = new File(applicationFolderPath);
			File tempApplicationFolder = new File(tempApplicationFolderLocation);
			if (applicationFolder.isDirectory()) {
				String[] folderList = applicationFolder.list();

				if (null != folderList) {
					for (int i = 0; i < folderList.length; i++) {
						String folderName = FilenameUtils.getName(folderList[i]);
						if (!folderName.equalsIgnoreCase(NATIVE) && !folderName.equalsIgnoreCase(LOG)) {
							try {
								if (new File(EphesoftStringUtil.concatenate(applicationFolderPath, File.separator, folderList[i]))
										.isDirectory()) {
									FileUtils.copyDirectoryWithContents(new File(applicationFolder, folderList[i]), new File(
											tempApplicationFolder, folderList[i]));
								} else {
									org.apache.commons.io.FileUtils
											.copyFile(
													new File(EphesoftStringUtil.concatenate(applicationFolder, File.separator,
															folderList[i])),
													new File(EphesoftStringUtil.concatenate(tempApplicationFolder, File.separator,
															folderList[i])));
								}
							} catch (IOException e) {
								LOGGER.error(EphesoftStringUtil.concatenate("Error occured in copying application folder.",
										e.getMessage()));
								foldersNotCopied.append(EphesoftStringUtil.concatenate(APPLICATION, SEMI_COLON));
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Returns a semicolon separated list of artifacts that could not be copied.
	 * 
	 * @return {@link String} the string containing artifact names that could not be copied.
	 */
	public String getFoldersNotCopied() {
		return foldersNotCopied.toString();
	}
}
