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

package com.ephesoft.dcma.webservice.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.naming.NamingException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.ephesoft.dcma.batch.dao.xml.BatchSchemaDao;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.UserInformation;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.exception.FTPDataDownloadException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassCloudConfig;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.RemoteBatchInstance;
import com.ephesoft.dcma.da.domain.User;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassCloudConfigService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.UserService;
import com.ephesoft.dcma.ftp.service.FTPService;
import com.ephesoft.dcma.mail.WizardMailException;
import com.ephesoft.dcma.mail.service.WizardMailService;
import com.ephesoft.dcma.user.connectivity.exception.InvalidCredentials;
import com.ephesoft.dcma.user.service.UserConnectivityService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.WebServiceUtil;
import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;
import com.ephesoft.dcma.workflows.service.DeploymentService;
import com.ephesoft.dcma.workflows.service.RestartBatchService;
import com.ephesoft.dcma.workflows.service.WorkflowService;
import com.ephesoft.dcma.workflows.service.engine.EngineService;

/**
 * This Class provides the functionality of the Web services feature used to preparation and restarting batch instances.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jul 25, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Controller
public class EphesoftWebService {

	/**
	 * Initializing pluginPropertiesService {@link PluginPropertiesService}.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Initializing batchSchemaService {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Initializing batchClassService {@link BatchClassService}.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * Initializing batchInstanceService {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Initializing workflowService {@link WorkflowService}.
	 */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * Initializing ftpService {@link FTPService}.
	 */
	@Autowired
	private FTPService ftpService;

	/**
	 * Initializing userService {@link UserService}.
	 */
	@Autowired
	private UserService userService;

	/**
	 * Initializing batchClassCloudConfigService {@link BatchClassCloudConfigService}.
	 */
	@Autowired
	private BatchClassCloudConfigService batchClassCloudConfigService;

	/** The user connectivity service. */
	@Autowired
	private UserConnectivityService userConnectivityService;

	/** The batch schema dao. */
	@Autowired
	private BatchSchemaDao batchSchemaDao;

	/** The wizard mail service. */
	@Autowired
	private WizardMailService wizardMailService;

	/** The deployment service. */
	@Autowired
	private DeploymentService deploymentService;

	/** {@link EngineService} The engine service. */
	@Autowired
	private EngineService engineService;

	/** {@link RestartBatchService} The restart batch service. */
	@Autowired
	private RestartBatchService restartService;

	/**
	 * LOG {@link String} constant for log folder name.
	 */
	private String LOG = "logs";

	/**
	 * JAVA_APP_SERVER_FOLDER {@link String} constant for java app server folder.
	 */
	private String JAVA_APP_SERVER_FOLDER = "JavaAppServer";

	/**
	 * DCMA_HOME {@link String} constant for environment variable DCMA_HOME.
	 */
	private String DCMA_HOME = "DCMA_HOME";

	/**
	 * zipFolderPath {@link String} the path to copy log files.
	 */
	private String zipFolderPath = "zipFolderPath";

	/**
	 * Initializing logger {@link Logger}.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EphesoftWebService.class);

	/**
	 * Prepare data and restart batch.
	 * 
	 * @param batchId the batch id
	 * @param serverURL the server url
	 * @param folderPath the folder path
	 * @param batchClassId the batch class id
	 * @param moduleName the module name
	 * @param newBatchInstanceIdentifier the new batch instance identifier
	 * @param batchName the batch name
	 * @return the string
	 */
	@RequestMapping(value = "/batchIdentifier/{batchId}/server/{serverURL}/folderLocation/{folderPath}/batchClassId/{batchClassId}/moduleName/{moduleName}/newBatchInstanceIdentifier/{newBatchInstanceIdentifier}/batchName/{batchName}", method = RequestMethod.GET)
	@ResponseBody
	public String prepareDataAndRestartBatch(@PathVariable("batchId") final String batchId,
			@PathVariable("serverURL") final String serverURL, @PathVariable("folderPath") final String folderPath,
			@PathVariable("batchClassId") final String batchClassId, @PathVariable("moduleName") final String moduleName,
			@PathVariable("newBatchInstanceIdentifier") final String newBatchInstanceIdentifier,
			@PathVariable("batchName") final String batchName) {
		String newBatchInstanceID = null;
		newBatchInstanceID = EphesoftStringUtil.getDecodedString(newBatchInstanceIdentifier);
		String folderPathLocalVariable = folderPath;
		String serverURLLocalVariable = EphesoftStringUtil.getDecodedString(serverURL);
		String moduleNameDecoded = EphesoftStringUtil.getDecodedString(moduleName);
		folderPathLocalVariable = EphesoftStringUtil.getDecodedString(folderPathLocalVariable).replace(WorkflowConstants.CARET,
				WorkflowConstants.BACK_SLASH);
		serverURLLocalVariable = EphesoftStringUtil.getDecodedString(serverURLLocalVariable).replace(WorkflowConstants.CARET,
				WorkflowConstants.FORWARD_SLASH);
		String batchNameLocalVariable = EphesoftStringUtil.getDecodedString(batchName);
		LOGGER.info("==========Inside EphesoftWebService=============");
		LOGGER.info("Folder path is " + folderPathLocalVariable);
		LOGGER.info("Server URL is" + serverURLLocalVariable);
		Random random = new Random();
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);

		boolean isZipSwitchOn = batchSchemaService.isZipSwitchOn();
		LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);

		String sourceDirectory = batchId;
		String oldBatchId = batchId.split(ICommonConstants.UNDERSCORE)[0];
		LOGGER.info("Source URL Directory is" + sourceDirectory);
		long folderName = random.nextLong();
		if (folderName < 0) {
			folderName = Math.abs(folderName);
		}
		String downloadDirectory = batchSchemaService.getLocalFolderLocation() + File.separator + folderName;
		LOGGER.info("Preparing to download data from the FTP server");
		try {
			ftpService.downloadDirectory(sourceDirectory, downloadDirectory, 0, true);
			newBatchInstanceID = checkBatchInstanceIdentifier(newBatchInstanceIdentifier, serverURLLocalVariable, oldBatchId,
					batchClassId, batchNameLocalVariable, moduleNameDecoded);
			if (newBatchInstanceID != null) {
				boolean isPreparedData = preparedFiles(newBatchInstanceID, batchClass, oldBatchId, folderName, batchNameLocalVariable,
						isZipSwitchOn);
				if (isPreparedData) {
					LOGGER.info("Restarting workflow batchInstanceIdentifier" + newBatchInstanceIdentifier + "module name"
							+ moduleNameDecoded);
					LOGGER.info("Starting to create serialize file");
					pluginPropertiesService.getPluginProperties(newBatchInstanceID);
					LOGGER.info("Created serialize file");
					String moduleWorkflowName = getModuleWorkflowNameForBatchClassId(batchClassId, moduleNameDecoded);
					workflowService.startWorkflow(new BatchInstanceID(newBatchInstanceID), moduleWorkflowName);
				} else {
					LOGGER.info("Error in preparing data " + newBatchInstanceIdentifier + ".Returning null");
				}
				LOGGER.info("Returning New batch instance identifier" + newBatchInstanceID);
			}
		} catch (FTPDataDownloadException e) {
			LOGGER.error("Error in downloading data from FTP. Marking batch as error.... " + e.getMessage(), e);
		}
		return newBatchInstanceID;
	}

	/**
	 * Gets the module workflow name for batch class id.
	 * 
	 * @param batchClassId the batch class id
	 * @param moduleNameDecoded the module name decoded
	 * @return the module workflow name for batch class id
	 */
	private String getModuleWorkflowNameForBatchClassId(final String batchClassId, final String moduleNameDecoded) {
		String moduleName = "";
		LOGGER.info("Retrieving batch class module workflow name for " + moduleNameDecoded + " module in the batch class with id: "
				+ batchClassId);
		final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
		for (BatchClassModule bcm : batchClass.getBatchClassModules()) {
			if (bcm.getModule().getName().equalsIgnoreCase(moduleNameDecoded)) {
				moduleName = bcm.getWorkflowName();
				break;
			}
		}
		if (moduleName.isEmpty()) {
			LOGGER.info("No Batch Class Module with module name" + moduleNameDecoded + " found in the batch class with id: "
					+ batchClassId);
		} else {
			LOGGER.info("Batch Class Module with module name" + moduleNameDecoded + " found in the batch class with id: "
					+ batchClassId);
			LOGGER.info("Batch Class Workflow name: " + moduleName);
		}
		return moduleName;
	}

	/**
	 * API to check the batch instance is existing batch or not. If its existing batch its return old batch instance identifier else it
	 * will create new batch instance and return new batch instance identifier.
	 * 
	 * @param newBatchInstanceIdentifier {@link String}
	 * @param serverURLLocalVariable {@link String}
	 * @param batchId {@link String}
	 * @param batchClassId {@link String}
	 * @param batchName {@link String}
	 * @param moduleName the module name
	 * @return newBatchInstanceID {@link String}
	 */
	private String checkBatchInstanceIdentifier(final String newBatchInstanceIdentifier, final String serverURLLocalVariable,
			final String batchId, final String batchClassId, final String batchName, final String moduleName) {
		String newBatchInstanceID = null;
		try {
			if (!newBatchInstanceIdentifier.equalsIgnoreCase(WorkflowConstants.NOT_APPLICABLE)) {
				String moduleNameValue = null;
				newBatchInstanceID = newBatchInstanceIdentifier;
				BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(newBatchInstanceIdentifier);
				List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
				for (BatchClassModule bcm : batchClassModules) {
					if (bcm.getModule().getName().equalsIgnoreCase(moduleName)) {
						moduleNameValue = bcm.getModule().getName();
						break;
					}
				}
				RemoteBatchInstance remoteBatchInstance = new RemoteBatchInstance();
				remoteBatchInstance.setPreviousRemoteBatchInstanceIdentifier(batchId);
				remoteBatchInstance.setPreviousRemoteURL(serverURLLocalVariable);
				remoteBatchInstance.setId(0);
				remoteBatchInstance.setSourceModule(moduleNameValue);
				batchInstance.setRemoteBatchInstance(remoteBatchInstance);
				batchInstance.setRemote(false);
				batchInstanceService.updateBatchInstance(batchInstance);
			} else {
				newBatchInstanceID = getNewBatchInstanceIdentifier(batchClassId, serverURLLocalVariable, batchId, batchName,
						moduleName);
			}
		} catch (Exception e) {
			LOGGER.error("Unable to get newBatchInstanceIdentifier :" + newBatchInstanceID + " " + e.getMessage(), e);
		}
		return newBatchInstanceID;
	}

	/**
	 * API to generate new batch instance identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param previousEphesoftInstance {@link String}
	 * @param previousBatchInstanceID {@link String}
	 * @param batchName {@link String}
	 * @param moduleName the module name
	 * @return newBatchInstanceIdentfier {@link String}
	 */
	public String getNewBatchInstanceIdentifier(final String batchClassIdentifier, final String previousEphesoftInstance,
			final String previousBatchInstanceID, final String batchName, final String moduleName) {
		BatchInstance updatedbatchInstance = null;
		String newBatchInstanceIdentfier = null;
		boolean isValid = true;
		if (batchClassIdentifier == null) {
			LOGGER.error("batchClassIdentifier is null");
			if (isValid) {
				isValid = false;
			}
		}
		if (previousEphesoftInstance == null) {
			LOGGER.error("previousEphesoftInstance is null");
			if (isValid) {
				isValid = false;
			}
		}
		if (previousBatchInstanceID == null) {
			LOGGER.error("previousBatchInstanceID is null");
			if (isValid) {
				isValid = false;
			}
		}
		if (batchName == null) {
			LOGGER.error("batchName is null");
			if (isValid) {
				isValid = false;
			}
		}
		if (moduleName == null) {
			LOGGER.error("moduleName is null");
			if (isValid) {
				isValid = false;
			}
		}
		if (isValid) {
			String moduleNameValue = null;
			LOGGER.info("Generating new batch instance identifier");
			BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
			BatchInstance batchInstance = new BatchInstance();
			List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
			for (BatchClassModule bcm : batchClassModules) {

				if (bcm.getModule().getName().equalsIgnoreCase(moduleName)) {
					moduleNameValue = bcm.getModule().getName();
					break;
				}
			}
			batchInstanceService.updateBatchInstance(batchInstance);
			batchInstance.setBatchClass(batchClass);
			batchInstance.setLocalFolder(batchSchemaService.getLocalFolderLocation());
			batchInstance.setBatchName(batchName);
			batchInstance.setPriority(batchClass.getPriority());
			batchInstance.setStatus(BatchInstanceStatus.REMOTE);
			RemoteBatchInstance remoteBatchInstance = new RemoteBatchInstance();
			remoteBatchInstance.setPreviousRemoteBatchInstanceIdentifier(previousBatchInstanceID);
			remoteBatchInstance.setPreviousRemoteURL(previousEphesoftInstance);
			remoteBatchInstance.setId(0);
			if (remoteBatchInstance.getSourceModule() == null) {
				remoteBatchInstance.setSourceModule(moduleNameValue);
			}
			batchInstance.setRemoteBatchInstance(remoteBatchInstance);
			updatedbatchInstance = batchInstanceService.merge(batchInstance);
			newBatchInstanceIdentfier = updatedbatchInstance.getIdentifier();
		}
		return newBatchInstanceIdentfier;
	}

	/**
	 * API to prepare files and modify the internal contents of the oldBatchInstance with newBatchInstance.
	 * 
	 * @param newBatchInstanceIdentfier {@link String}
	 * @param batchClass {@link String}
	 * @param oldBatchInstanceIdentifier {@link String}
	 * @param folderName {@link String}
	 * @param batchName {@link String}
	 * @param isZipSwitchOn the is zip switch on
	 * @return true, if successful
	 */
	public boolean preparedFiles(final String newBatchInstanceIdentfier, final BatchClass batchClass,
			final String oldBatchInstanceIdentifier, final long folderName, final String batchName, final boolean isZipSwitchOn) {
		boolean isPreparedData = false;
		if (oldBatchInstanceIdentifier != null && newBatchInstanceIdentfier != null && batchClass != null) {
			boolean isRenameComplete = false;
			final String folderPath = batchSchemaService.getLocalFolderLocation() + File.separator + folderName;
			LOGGER.info("Folder path is: " + folderPath);
			final File ftpDownloadFolder = new File(folderPath);
			if (ftpDownloadFolder.isDirectory()) {
				final String[] string = ftpDownloadFolder.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
						FileType.TIFF.getExtensionWithDot(), FileType.PNG.getExtensionWithDot(), FileType.HTML.getExtensionWithDot(),
						FileType.XML.getExtensionWithDot(), FileType.ZIP.getExtensionWithDot()));
				for (final String fileName : string) {
					isRenameComplete = false;
					LOGGER.info("File name is: " + fileName);
					final String newFileName = changeDataName(fileName, oldBatchInstanceIdentifier, newBatchInstanceIdentfier);
					final File newFile = new File(ftpDownloadFolder.getAbsolutePath() + File.separator + fileName);
					final String finalFilePath = ftpDownloadFolder.getAbsolutePath() + File.separator + newFileName;
					while (!isRenameComplete) {
						LOGGER.info("Renaming file" + newFile.getAbsolutePath() + " to : " + finalFilePath);
						isRenameComplete = newFile.renameTo(new File(finalFilePath));
					}
					LOGGER.info("Renamed file :" + finalFilePath);
				}
				final String batchInstanceSystemPath = batchSchemaService.getLocalFolderLocation() + File.separator
						+ newBatchInstanceIdentfier;
				final File file = new File(batchInstanceSystemPath);
				if (file.exists()) {
					LOGGER.info("Batch Instance folder exists, so deleting it's content " + batchInstanceSystemPath);
					final boolean isDeletedSucessfully = FileUtils.deleteContentsOnly(batchInstanceSystemPath);
					if (!isDeletedSucessfully) {
						LOGGER.error("Error in deleting existing " + newBatchInstanceIdentfier + " proceeding with copying files");
					}
					try {
						LOGGER.info("Copying contents from " + folderPath + " to " + batchInstanceSystemPath);
						FileUtils.copyDirectoryWithContents(folderPath, batchInstanceSystemPath);
					} catch (final IOException e) {
						LOGGER.error(
								"Error in copying files from :" + folderPath + " to :" + batchInstanceSystemPath + " "
										+ e.getMessage(), e);
					} finally {
						LOGGER.info("Deleting contents from " + ftpDownloadFolder.getAbsolutePath());
						FileUtils.deleteDirectoryAndContents(ftpDownloadFolder);
					}
				} else {
					try {
						LOGGER.info("Batch Instance folder does not exists, so Renaming FTP Folder "
								+ ftpDownloadFolder.getAbsolutePath() + " to " + file.getAbsolutePath());
						isRenameComplete = ftpDownloadFolder.renameTo(file);
						while (!isRenameComplete) {
							LOGGER.info("Trying again. Batch Instance folder does not exists, so Renaming FTP Folder "
									+ ftpDownloadFolder.getAbsolutePath() + " to " + file.getAbsolutePath());
							isRenameComplete = ftpDownloadFolder.renameTo(file);
						}
					} catch (final SecurityException e) {
						LOGGER.error("Security exception naming the ftp folder.");
					}
				}
				if (isRenameComplete) {
					renameAllFiles(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier, file, isZipSwitchOn);
					isPreparedData = true;
				} else {
					isPreparedData = false;
				}
			}
		}

		return isPreparedData;
	}

	/**
	 * API to rename all the files and changing the internal contents of the oldBatchInstanceIdentifier with
	 * newBatchInstanceIdentifier.
	 * 
	 * @param newBatchInstanceIdentfier {@link String}
	 * @param batchClass {@link BatchClass}
	 * @param oldBatchInstanceIdentifier {@link String}
	 * @param file {@link File}
	 * @param isZipSwitchOn the is zip switch on
	 */
	private void renameAllFiles(final String newBatchInstanceIdentfier, final BatchClass batchClass,
			final String oldBatchInstanceIdentifier, final File file, final boolean isZipSwitchOn) {
		LOGGER.info("Renaming Batch Files....");
		renameBatchFile(newBatchInstanceIdentfier, file, isZipSwitchOn);

		// Removed Renaming of HTML files method as its no longer needed.

		// Zip Switch handling new code
		LOGGER.info("Renaming Backup Files....");
		renameBackUpFiles(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier, file);
		LOGGER.info("Renaming Original Backup Files....");
		renameOriginalBatchFile(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier, file, isZipSwitchOn);
	}

	/**
	 * Rename original batch file.
	 * 
	 * @param newBatchInstanceIdentfier the new batch instance identfier
	 * @param batchClass the batch class
	 * @param oldBatchInstanceIdentifier the old batch instance identifier
	 * @param file the file
	 * @param isZipSwitchOn the is zip switch on
	 */
	private void renameOriginalBatchFile(final String newBatchInstanceIdentfier, final BatchClass batchClass,
			final String oldBatchInstanceIdentifier, final File file, final boolean isZipSwitchOn) {
		String srcFilePath;
		String destFilePath;
		boolean isRenamed = false;

		srcFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier + ICommonConstants.UNDERSCORE_BATCH_XML
				+ ICommonConstants.UNDERSCORE_ABC;

		destFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier + ICommonConstants.UNDERSCORE_BATCH_XML;

		if (isZipSwitchOn) {
			srcFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
					+ ICommonConstants.UNDERSCORE_BATCH_XML_ZIP + ICommonConstants.UNDERSCORE_ABC;

			destFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
					+ ICommonConstants.UNDERSCORE_BATCH_XML_ZIP;
		}

		final File newFileName = new File(srcFilePath);
		final File finalZipFile = new File(destFilePath);
		while (!isRenamed) {
			isRenamed = newFileName.renameTo(finalZipFile);
		}

		if (isZipSwitchOn) {
			renameZipFileEntries(finalZipFile, batchClass, oldBatchInstanceIdentifier, newBatchInstanceIdentfier);
		} else {
			updateTranferredBatch(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier);
		}
	}

	/**
	 * Rename back up files.
	 * 
	 * @param newBatchInstanceIdentfier the new batch instance identfier
	 * @param batchClass the batch class
	 * @param oldBatchInstanceIdentifier the old batch instance identifier
	 * @param file the file
	 */
	private void renameBackUpFiles(final String newBatchInstanceIdentfier, final BatchClass batchClass,
			final String oldBatchInstanceIdentifier, final File file) {
		boolean isRenamed = false;

		final String[] backUpZipFiles = file.list(new CustomFileFilter(false, FileType.ZIP.getExtensionWithDot()));

		for (final String backUpZipFileName : backUpZipFiles) {
			final File newXMLFile = new File(file.getAbsoluteFile() + File.separator + backUpZipFileName);
			if (backUpZipFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_XML_ZIP)
					|| backUpZipFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_BAK_XML_ZIP)
					|| backUpZipFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_XML)
					|| backUpZipFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_BAK_XML)) {

				isRenamed = false;
				String newXMLFileName = newBatchInstanceIdentfier + ICommonConstants.UNDERSCORE_BATCH_XML_ZIP;
				if (backUpZipFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_XML)
						|| backUpZipFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_BAK_XML)) {
					newXMLFileName = newBatchInstanceIdentfier + ICommonConstants.UNDERSCORE_BATCH_XML;
				}
				final File tempBatchFile = new File(file.getAbsoluteFile() + File.separator + newXMLFileName);
				while (!isRenamed) {
					isRenamed = newXMLFile.renameTo(tempBatchFile);
				}

				if (backUpZipFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_XML_ZIP)
						|| backUpZipFileName.endsWith(ICommonConstants.UNDERSCORE_BATCH_BAK_XML_ZIP)) {
					renameZipFileEntries(tempBatchFile, batchClass, oldBatchInstanceIdentifier, newBatchInstanceIdentfier);
				} else {
					updateTranferredBatch(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier);
				}
				final File newFileName = tempBatchFile;
				isRenamed = false;
				while (!isRenamed) {
					isRenamed = newFileName.renameTo(new File(file.getAbsoluteFile() + File.separator + backUpZipFileName));
				}
			}
		}
	}

	/**
	 * Rename batch file.
	 * 
	 * @param newBatchInstanceIdentfier the new batch instance identfier
	 * @param file the file
	 * @param isZipSwitchOn the is zip switch on
	 */
	private void renameBatchFile(final String newBatchInstanceIdentfier, final File file, final boolean isZipSwitchOn) {
		LOGGER.info("======Inside renameBatch File========");
		boolean isRenamed = false;
		String srcFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
				+ ICommonConstants.UNDERSCORE_BATCH_XML;
		LOGGER.info(EphesoftStringUtil.concatenate("Source File Path : ", srcFilePath));

		String destFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
				+ ICommonConstants.UNDERSCORE_BATCH_XML + ICommonConstants.UNDERSCORE_ABC;
		LOGGER.info(EphesoftStringUtil.concatenate("Destination File Path : ", destFilePath));
		if (isZipSwitchOn && FileUtils.isZipFileExists(srcFilePath)) {
			srcFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
					+ ICommonConstants.UNDERSCORE_BATCH_XML_ZIP;
			destFilePath = file.getAbsoluteFile() + File.separator + newBatchInstanceIdentfier
					+ ICommonConstants.UNDERSCORE_BATCH_XML_ZIP + ICommonConstants.UNDERSCORE_ABC;
		}

		final File srcZipFile = new File(srcFilePath);
		final File finalZipPath = new File(destFilePath);
		while (!isRenamed) {
			LOGGER.info(EphesoftStringUtil.concatenate("Renaming File : ", srcZipFile.getName(), "to", finalZipPath.getName()));
			isRenamed = srcZipFile.renameTo(finalZipPath);
		}
	}

	/**
	 * Update tranferred batch.
	 * 
	 * @param newBatchInstanceIdentfier the new batch instance identfier
	 * @param batchClass the batch class
	 * @param oldBatchInstanceIdentifier the old batch instance identifier
	 */
	private void updateTranferredBatch(final String newBatchInstanceIdentfier, final BatchClass batchClass,
			final String oldBatchInstanceIdentifier) {
		Batch batch = batchSchemaService.getBatch(newBatchInstanceIdentfier);
		if (batch != null) {
			batch.setBatchClassIdentifier(batchClass.getIdentifier());
			batch.setBatchClassDescription(batchClass.getDescription());
			batch.setBatchClassName(batchClass.getName());
			batch.setBatchClassVersion(batchClass.getVersion());
			batch.setBatchLocalPath(batchSchemaService.getLocalFolderLocation());
			batch.setBatchPriority(String.valueOf(batchClass.getPriority()));
			batch.setBatchInstanceIdentifier(newBatchInstanceIdentfier);
			Documents documents = batch.getDocuments();
			List<Document> documentList = documents.getDocument();
			for (Document document : documentList) {
				if (document != null) {
					Pages pagesList = document.getPages();
					List<Page> pageList = pagesList.getPage();
					for (Page page : pageList) {
						if (page != null) {
							page.setNewFileName(changeDataName(page.getNewFileName(), oldBatchInstanceIdentifier,
									newBatchInstanceIdentfier));
							page.setHocrFileName(changeDataName(page.getHocrFileName(), oldBatchInstanceIdentifier,
									newBatchInstanceIdentfier));
							page.setThumbnailFileName(changeDataName(page.getThumbnailFileName(), oldBatchInstanceIdentifier,
									newBatchInstanceIdentfier));
							page.setComparisonThumbnailFileName(changeDataName(page.getComparisonThumbnailFileName(),
									oldBatchInstanceIdentifier, newBatchInstanceIdentfier));
							page.setDisplayFileName(changeDataName(page.getDisplayFileName(), oldBatchInstanceIdentifier,
									newBatchInstanceIdentfier));
							page.setOCRInputFileName(changeDataName(page.getOCRInputFileName(), oldBatchInstanceIdentifier,
									newBatchInstanceIdentfier));
						}
					}
				}
			}
			batchSchemaService.updateBatch(batch);
		}
	}

	/**
	 * Rename zip file entries.
	 * 
	 * @param oldZipFile the old zip file
	 * @param batchClass the batch class
	 * @param oldBatchInstanceIdentifier the old batch instance identifier
	 * @param newBatchInstanceIdentfier the new batch instance identfier
	 */
	private void renameZipFileEntries(final File oldZipFile, final BatchClass batchClass, final String oldBatchInstanceIdentifier,
			final String newBatchInstanceIdentfier) {
		LOGGER.info(EphesoftStringUtil.concatenate("Zip files to be modified ", oldZipFile.getAbsolutePath()));
		final String finalZipPath = oldZipFile.getAbsolutePath();
		final File finalZipFile = new File(finalZipPath);

		final String destDirectory = finalZipFile.getParent();
		final String unzippedFilePath = finalZipFile.getAbsolutePath();
		LOGGER.info(EphesoftStringUtil.concatenate("Unziping the ", unzippedFilePath, " to ", destDirectory));

		FileUtils.unzip(finalZipFile, destDirectory);
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(finalZipFile);

			// Only 1st entry picked. Considering that our zip file contains
			// only a single file.
			String xmlFileName = zipFile.entries().nextElement().getName();
			final StringBuilder xmlFileNameBuilder = new StringBuilder();
			xmlFileNameBuilder.append(destDirectory);
			xmlFileNameBuilder.append(File.separator);
			xmlFileNameBuilder.append(xmlFileName);
			final String xmlFilePath = xmlFileNameBuilder.toString();
			final File xmlFile = new File(xmlFilePath);

			xmlFileName = xmlFileName.replaceAll(oldBatchInstanceIdentifier, newBatchInstanceIdentfier);
			final StringBuilder newXmlFileNameBuilder = new StringBuilder();
			newXmlFileNameBuilder.append(destDirectory);
			newXmlFileNameBuilder.append(File.separator);
			newXmlFileNameBuilder.append(xmlFileName);
			final String newXmlFilePath = newXmlFileNameBuilder.toString();
			final File newXmlFile = new File(newXmlFilePath);

			LOGGER.info(EphesoftStringUtil.concatenate("Renaming ", unzippedFilePath, " to ", xmlFileName));
			xmlFile.renameTo(newXmlFile);

			final List<String> fileNames = new ArrayList<String>();
			fileNames.add(newXmlFile.getAbsolutePath());

			LOGGER.info(EphesoftStringUtil.concatenate("Zipping the altered files to ", finalZipPath));
			FileUtils.zipMultipleFiles(fileNames, finalZipPath);
			updateTranferredBatch(newBatchInstanceIdentfier, batchClass, oldBatchInstanceIdentifier);
			newXmlFile.delete();
		} catch (final ZipException e) {
			LOGGER.error(EphesoftStringUtil.concatenate("Error Creating zip file ", e.getMessage(), e));
		} catch (final IOException e) {
			LOGGER.error(EphesoftStringUtil.concatenate("I/O Error while Creating zip file ", e.getMessage(), e));
		}

	}

	/**
	 * API to changes the content of the file name from oldBatchIdentifier to newBatchIdentifier.
	 * 
	 * @param fileName {@link String}
	 * @param oldBatchIdentifier {@link String}
	 * @param newBatchIdentifier {@link String}
	 * @return newFileName {@link String}
	 */
	public String changeDataName(final String fileName, final String oldBatchIdentifier, final String newBatchIdentifier) {
		String newFileName = null;
		if (fileName != null) {
			newFileName = fileName.replace(oldBatchIdentifier, newBatchIdentifier);
		}
		return newFileName;
	}

	/**
	 * Gets the batch statusof remote batch.
	 * 
	 * @param remoteBatchInstanceIdentifier the remote batch instance identifier
	 * @return the batch statusof remote batch
	 */
	@RequestMapping(value = "/remoteBatchInstanceIdentifier/{remoteBatchInstanceIdentifier}", method = RequestMethod.GET)
	@ResponseBody
	public BatchInstanceStatus getBatchStatusofRemoteBatch(
			@PathVariable("remoteBatchInstanceIdentifier") final String remoteBatchInstanceIdentifier) {
		BatchInstanceStatus batchInstanceStatus = null;
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(remoteBatchInstanceIdentifier);
		if (batchInstance != null) {
			batchInstanceStatus = batchInstance.getStatus();
		}
		return batchInstanceStatus;
	}

	/**
	 * Gets the previous batch instance of remote batch.
	 * 
	 * @param previousRemoteBatchInstanceIdentifier the previous remote batch instance identifier
	 */
	@RequestMapping(value = "/previousRemoteBatchInstanceIdentifier/{previousRemoteBatchInstanceIdentifier}", method = RequestMethod.GET)
	@ResponseBody
	public void removeDependencyAndUpdateRemoteBi(
			@PathVariable("previousRemoteBatchInstanceIdentifier") final String previousRemoteBatchInstanceIdentifier) {
		// Method shifted from client side to server side.
		final BatchInstance remoteBatch = batchInstanceService.getBatchInstanceByIdentifier(previousRemoteBatchInstanceIdentifier);
		if (remoteBatch.getRemoteBatchInstance() != null) {
			final RemoteBatchInstance remoteBatchInstance = remoteBatch.getRemoteBatchInstance();
			final String previousRemoteBatchURL = remoteBatchInstance.getPreviousRemoteURL();
			final String previousRemoteBatchID = remoteBatchInstance.getPreviousRemoteBatchInstanceIdentifier();
			final String nextRemoteBatchURL = remoteBatchInstance.getRemoteURL();
			final String nextRemoteBatchID = remoteBatchInstance.getRemoteBatchInstanceIdentifier();
			LOGGER.info("Cyclic Dependency Removeal Details");
			LOGGER.info("Previous Remote Batch URL :" + previousRemoteBatchURL);
			LOGGER.info("Next Remote Batch URL :" + nextRemoteBatchURL);
			LOGGER.info("Previous Remote Batch ID :" + previousRemoteBatchID);
			LOGGER.info("Next Remote Batch ID :" + nextRemoteBatchID);

			if (previousRemoteBatchURL != null && previousRemoteBatchID != null) {
				LOGGER.info("Previous Remote URL and ID is not null.......................");
				updateInfoOfRemoteBatchInstance(previousRemoteBatchID, null, null, nextRemoteBatchURL, nextRemoteBatchID, true);
			}
			if (nextRemoteBatchURL != null && nextRemoteBatchID != null) {
				LOGGER.info("nextRemoteBatchURL and nextRemoteBatchID is not null.................");
				updateInfoOfRemoteBatchInstance(nextRemoteBatchID, previousRemoteBatchURL, previousRemoteBatchID, null, null, true);
			}
		}
	}

	/**
	 * Update info of remote batch instance.
	 * 
	 * @param targetBatchInstanceIdentifier the target batch instance identifier
	 * @param previousURL the previous url
	 * @param previousBatchInstanceIdentifier the previous batch instance identifier
	 * @param nextURL the next url
	 * @param nextBatchInstanceIdentifier the next batch instance identifier
	 * @param isRemote the is remote
	 */
	@RequestMapping(value = "/targetBatchInstanceIdentifier/{targetBatchInstanceIdentifier}/previousURL/{previousURL}/previousBatchInstanceIdentifier/{previousBatchInstanceIdentifier}/nextURL/{nextURL}/nextBatchInstanceIdentifier/{nextBatchInstanceIdentifier}/isRemote/{isRemote}", method = RequestMethod.GET)
	@ResponseBody
	public void updateInfoOfRemoteBatchInstance(
			@PathVariable("targetBatchInstanceIdentifier") final String targetBatchInstanceIdentifier,
			@PathVariable("previousURL") final String previousURL,
			@PathVariable("previousBatchInstanceIdentifier") final String previousBatchInstanceIdentifier,
			@PathVariable("nextURL") final String nextURL,
			@PathVariable("nextBatchInstanceIdentifier") final String nextBatchInstanceIdentifier,
			@PathVariable("isRemote") final boolean isRemote) {
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(targetBatchInstanceIdentifier);
		RemoteBatchInstance remoteBatchInstance = batchInstance.getRemoteBatchInstance();
		if (remoteBatchInstance != null) {
			if (previousBatchInstanceIdentifier.equalsIgnoreCase(WorkflowConstants.NOT_APPLICABLE)
					&& previousURL.equalsIgnoreCase(WorkflowConstants.NOT_APPLICABLE)
					&& nextBatchInstanceIdentifier.equalsIgnoreCase(WorkflowConstants.NOT_APPLICABLE)
					&& nextURL.equalsIgnoreCase(WorkflowConstants.NOT_APPLICABLE)) {
				remoteBatchInstance.setPreviousRemoteBatchInstanceIdentifier(null);
				remoteBatchInstance.setPreviousRemoteURL(null);
				remoteBatchInstance.setRemoteBatchInstanceIdentifier(null);
				remoteBatchInstance.setRemoteURL(null);
			} else if (previousBatchInstanceIdentifier.equalsIgnoreCase(WorkflowConstants.NOT_APPLICABLE)
					&& previousURL.equalsIgnoreCase(WorkflowConstants.NOT_APPLICABLE)) {
				remoteBatchInstance.setRemoteBatchInstanceIdentifier(null);
				remoteBatchInstance.setRemoteURL(null);
			} else {
				remoteBatchInstance.setPreviousRemoteBatchInstanceIdentifier(null);
				remoteBatchInstance.setPreviousRemoteURL(null);
			}
		}
		batchInstance.setRemote(isRemote);
		batchInstance.setRemoteBatchInstance(remoteBatchInstance);
		batchInstanceService.updateBatchInstance(batchInstance);
	}

	/**
	 * Check user existence.
	 * 
	 * @param request the request
	 * @param response the response
	 * @return true, if successful
	 */
	@RequestMapping(value = "/checkUserExistence", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkUserExistence(final HttpServletRequest request, final HttpServletResponse response) {
		final String userName = request.getParameter("userName");
		return userConnectivityService.checkUserExistence(userName);
	}

	/**
	 * Sign up.
	 * 
	 * @param request the request
	 * @param response the response
	 */
	@RequestMapping(value = "/signUp", method = RequestMethod.POST)
	@ResponseBody
	public void signUp(final HttpServletRequest request, final HttpServletResponse response) {
		LOGGER.info("Start processing sign up process");
		String workingDir = WebServiceUtil.EMPTY_STRING;
		InputStream instream = null;
		if (request instanceof DefaultMultipartHttpServletRequest) {
			UserInformation userInformation = null;
			User user = null;
			String receiverName = null;
			try {
				final String webServiceFolderPath = batchSchemaService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				LOGGER.info("workingDir:" + workingDir);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);
				LOGGER.info("outputDir:" + outputDir);
				final DefaultMultipartHttpServletRequest multipartRequest = (DefaultMultipartHttpServletRequest) request;

				final String batchClassId = request.getParameter("batchClassId");
				final String batchClassPriority = request.getParameter("batchClassPriority");
				final String batchClassDescription = request.getParameter("batchClassDescription");
				String batchClassName = request.getParameter("batchClassName");

				batchClassName = getUniqueBatchClassName(batchClassName);

				final String batchInstanceLimit = request.getParameter("batchInstanceLimit");
				final String noOfDays = request.getParameter("noOfDays");
				final String pageCount = request.getParameter("pageCount");
				String uncFolder = "unc" + ICommonConstants.HYPHEN + batchClassName;
				LOGGER.info("Batch Class ID value is: " + batchClassId);
				LOGGER.info("Batch Class Priority value is: " + batchClassPriority);
				LOGGER.info("Batch Class Description value is: " + batchClassDescription);
				LOGGER.info("Batch Class Name value is: " + batchClassName);
				LOGGER.info("UNC Folder value is: " + uncFolder);

				final MultiValueMap<String, MultipartFile> fileMap = multipartRequest.getMultiFileMap();
				for (final String fileName : fileMap.keySet()) {
					if (fileName.toLowerCase().indexOf(FileType.XML.getExtension().toLowerCase()) > -1) {
						final MultipartFile multipartFile = multipartRequest.getFile(fileName);
						instream = multipartFile.getInputStream();
						final Source source = XMLUtil.createSourceFromStream(instream);
						userInformation = (UserInformation) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
						user = createUserObjectFromUserInformation(userInformation);
						break;
					}
				}
				if (userInformation != null && user != null) {
					LOGGER.info("Recevier name created: " + receiverName);
					userConnectivityService.addUser(userInformation);
					LOGGER.info("Successfully added user for email id: " + userInformation.getEmail());
					userConnectivityService.addGroup(userInformation);
					LOGGER.info("Successfully added group for email id: " + userInformation.getEmail());
					BatchClass batchClass = batchClassService.copyBatchClass(batchClassId, batchClassName, batchClassDescription,
							userInformation.getCompanyName() + ICommonConstants.UNDERSCORE + userInformation.getEmail(),
							batchClassPriority, uncFolder, true);
					LOGGER.info("Adding user information into database");
					user.setBatchClass(batchClass);
					userService.createUser(user);
					LOGGER.info("Successfully added user information into database");
					BatchClassCloudConfig batchClassCloudConfig = createBatchClassCloudConfig(batchInstanceLimit, noOfDays, pageCount,
							batchClass);

					batchClassCloudConfigService.createBatchClassCloudConfig(batchClassCloudConfig);

					LOGGER.info("Successfully copied batch class for batch class: " + batchClassId);
					deploymentService.createAndDeployProcessDefinition(batchClass, false);
					LOGGER.info("Batch Class deployed successfully");
					wizardMailService.sendConfirmationMail(userInformation, false, null);
					LOGGER.info("User login information sent for email id: " + userInformation.getEmail());
				} else {
					LOGGER.error("user Information file is invalid. Unable create the User Information Object from XML.");
				}
			} catch (WizardMailException wizardMailException) {
				try {
					response.sendError(HttpServletResponse.SC_CREATED);
				} catch (IOException e) {
					LOGGER.error("Error in sending status using web service");
				}
			} catch (Exception e) {
				LOGGER.error("Exception occurs while sign up process: " + e.getMessage(), e);
				if (userInformation != null && user != null) {
					LOGGER.info("Deleting created user/groups while signup for : " + userInformation.getEmail());
					userConnectivityService.deleteUser(userInformation.getEmail());
					userConnectivityService.deleteGroup(userInformation.getCompanyName() + ICommonConstants.UNDERSCORE
							+ userInformation.getEmail());
					userService.deleteUser(user);
					LOGGER.info("Successfully deleted user/groups while signup for : " + userInformation.getEmail());
					LOGGER.info("Sending error mail");
					try {
						wizardMailService.sendConfirmationMail(userInformation, true, ExceptionUtils.getStackTrace(e));
						LOGGER.info("Error mail sent succesfully");
					} catch (WizardMailException e1) {
						LOGGER.error("Error in sending error mail to client");
					}
				}
				try {
					response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				} catch (IOException e1) {
					LOGGER.error("Error in sending status using web service");
				}

			}
		}
	}

	/**
	 * Creates the batch class cloud config.
	 * 
	 * @param batchInstanceLimit the batch instance limit
	 * @param noOfDays the no of days
	 * @param pageCount the page count
	 * @param batchClass the batch class
	 * @return the batch class cloud config
	 */
	private BatchClassCloudConfig createBatchClassCloudConfig(final String batchInstanceLimit, final String noOfDays,
			final String pageCount, final BatchClass batchClass) {
		Integer batchInstanceLimitInt = null;
		if (!batchInstanceLimit.isEmpty()) {
			batchInstanceLimitInt = ICommonConstants.DEFAULT_BATCH_INSTANCE_LIMIT;
			try {
				batchInstanceLimitInt = Integer.parseInt(batchInstanceLimit);
			} catch (NumberFormatException e) {
				LOGGER.error("Invalid batchInstance Limit is passed. Using default value is "
						+ ICommonConstants.DEFAULT_BATCH_INSTANCE_LIMIT);
			}
		}

		Integer pageCountInt = null;
		if (!pageCount.isEmpty()) {
			pageCountInt = ICommonConstants.DEFAULT_PAGE_COUNT_LIMIT;
			try {
				pageCountInt = Integer.parseInt(pageCount);
			} catch (NumberFormatException e) {
				LOGGER.error("Invalid pageCount is passed. Using default value is " + ICommonConstants.DEFAULT_PAGE_COUNT_LIMIT);
			}
		}

		Integer noOfDaysInt = null;
		if (!noOfDays.isEmpty()) {
			noOfDaysInt = ICommonConstants.DEFAULT_NO_OF_DAYS_LIMIT;
			try {
				noOfDaysInt = Integer.parseInt(noOfDays);
			} catch (NumberFormatException e) {
				LOGGER.error("Invalid noOfDays is passed. Using default value is " + ICommonConstants.DEFAULT_NO_OF_DAYS_LIMIT);
			}
		}

		BatchClassCloudConfig batchClassCloudConfig = new BatchClassCloudConfig();
		batchClassCloudConfig.setBatchInstanceLimit(batchInstanceLimitInt);
		batchClassCloudConfig.setPageCount(pageCountInt);
		batchClassCloudConfig.setCurrentCounter(0);
		batchClassCloudConfig.setLastReset(new Date());
		batchClassCloudConfig.setNoOfDays(noOfDaysInt);
		batchClassCloudConfig.setBatchClass(batchClass);
		return batchClassCloudConfig;
	}

	/**
	 * Modify password.
	 * 
	 * @param request the request
	 * @param response the response
	 */
	@RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
	@ResponseBody
	public void modifyPassword(final HttpServletRequest request, final HttpServletResponse response) {
		final String userName = request.getParameter("userName");
		final String newPassword = request.getParameter("newPassword");
		try {
			LOGGER.info("Modifying password for username: " + userName + " and password: " + newPassword);
			userConnectivityService.modifyUserPassword(userName, newPassword);
			LOGGER.info("LDAP account update, now updating DB");
			User user = userService.getUser(userName);
			user.setPassword(newPassword);
			user = userService.updateUser(user);
			LOGGER.info("DB updated and now sendin mail");
			wizardMailService.sendResetPasswordMail(user);
		} catch (NamingException namingException) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException ioException) {
				LOGGER.error("Error in sending status using web service" + ioException.toString());
			}
		} catch (WizardMailException wizardMailException) {
			try {
				response.sendError(HttpServletResponse.SC_CREATED);
			} catch (IOException ioMailException) {
				LOGGER.error("Error in sending status using web service" + ioMailException.toString());
			}
		}
	}

	/**
	 * Change password.
	 * 
	 * @param request the request
	 * @param response the response
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	@ResponseBody
	public void changePassword(final HttpServletRequest request, final HttpServletResponse response) {
		final String userName = request.getParameter("userName");
		final String oldPassword = request.getParameter("oldPassword");
		final String newPassword = request.getParameter("newPassword");
		try {
			LOGGER.info("Change password for username: " + userName + " and old password: " + newPassword + " with new password: "
					+ newPassword);
			userConnectivityService.verifyandmodifyUserPassword(userName, oldPassword, newPassword);
			LOGGER.info("LDAP account updated, now updating DB");
			User user = userService.getUser(userName);
			user.setPassword(newPassword);
			user = userService.updateUser(user);
			LOGGER.info("DB updated and now sending mail");
			wizardMailService.sendChangePasswordMail(user);
		} catch (InvalidCredentials invalidCredentialException) {
			try {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
			} catch (IOException ioException) {
				LOGGER.error("Error in sending status using web service" + ioException.toString());
			}
		} catch (NamingException namingException) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException ioException) {
				LOGGER.error("Error in sending status using web service" + ioException.toString());
			}
		} catch (WizardMailException wizardMailException) {
			try {
				response.sendError(HttpServletResponse.SC_CREATED);
			} catch (IOException ioMailException) {
				LOGGER.error("Error in sending status using web service" + ioMailException.toString());
			}
		}
	}

	/**
	 * Gets the unique batch class name.
	 * 
	 * @param batchClassName the batch class name
	 * @return the unique batch class name
	 */
	private String getUniqueBatchClassName(final String batchClassName) {
		BatchClass batchClass = batchClassService.getBatchClassbyName(batchClassName);
		String newbatchClassName = batchClassName;
		if (batchClass != null) {
			for (int i = 1; i < Integer.MAX_VALUE; i++) {
				newbatchClassName = batchClassName + i;
				BatchClass batchClass2 = batchClassService.getBatchClassbyName(newbatchClassName);
				if (batchClass2 == null) {
					break;
				}
			}
		}
		return newbatchClassName;
	}

	/**
	 * Creates the user object from user information.
	 * 
	 * @param userInformation the user information
	 * @return the user
	 */
	private User createUserObjectFromUserInformation(final UserInformation userInformation) {
		User user = new User();
		user.setFirstName(userInformation.getFirstName());
		user.setLastName(userInformation.getLastName());
		user.setEmail(userInformation.getEmail());
		user.setPassword(userInformation.getPassword());
		user.setCompanyName(userInformation.getCompanyName());
		user.setPhoneNumber(userInformation.getPhoneNumber());
		return user;
	}

	/**
	 * Restart batch instance.
	 * 
	 * @param batchInstanceIdentifier the batch instance identifier
	 * @param moduleName the module name
	 * @param throwException the throw exception
	 * @return true, if successful
	 * @throws DCMAApplicationException the dCMA application exception
	 */
	@RequestMapping(value = "/batchInstanceIdentifier/{batchInstanceIdentifier}/moduleName/{moduleName}/throwException/{throwException}", method = RequestMethod.GET)
	@ResponseBody
	public boolean restartBatchInstance(@PathVariable("batchInstanceIdentifier") final String batchInstanceIdentifier,
			@PathVariable("moduleName") final String moduleName, @PathVariable("throwException") final boolean throwException)
			throws DCMAApplicationException {
		return restartService.restartBatchInstance(batchInstanceIdentifier, moduleName, throwException, false);
	}

	/**
	 * Delete batch instance.
	 * 
	 * @param batchInstanceIdentifier the batch instance identifier
	 * @return true, if successful
	 * @throws DCMAApplicationException the dCMA application exception
	 */
	@RequestMapping(value = "/batchInstanceIdentifier/{batchInstanceIdentifier}", method = RequestMethod.GET)
	@ResponseBody
	public boolean deleteBatchInstance(@PathVariable("batchInstanceIdentifier") final String batchInstanceIdentifier)
			throws DCMAApplicationException {
		return engineService.deleteBatchInstance(batchInstanceIdentifier);
	}

	/**
	 * Verify ephesoft license.
	 * 
	 * @param request the request
	 * @param response the response
	 */
	@RequestMapping(value = "/verifyEphesoftLicense", method = RequestMethod.POST)
	@ResponseBody
	public void verifyEphesoftLicense(final HttpServletRequest request, final HttpServletResponse response) {
		LOGGER.info("Start processing verify Ephesoft License web service");
		Boolean isLicenseInstalled = Boolean.FALSE;
		LOGGER.info("Successfully executed verify Ephesoft License web service. Ephesoft License installed value is "
				+ isLicenseInstalled);
		try {
			response.getWriter().write(isLicenseInstalled.toString());
		} catch (final IOException ioe) {
			LOGGER.error("Exception in sending message to client. Logged the exception for debugging.", ioe);
		} catch (final Exception e) {
			LOGGER.error("Exception in sending message to client. Logged the exception for debugging.", e);
		}
	}

	/**
	 * Web service API to get value of advance reporting switch.
	 * 
	 * @param request {@link HttpServletRequest}
	 * @param response {@link HttpServletResponse}
	 */
	@RequestMapping(value = "/verifyAdvanceReportingSwitch", method = RequestMethod.POST)
	@ResponseBody
	public void verifyAdvanceReportingSwitch(final HttpServletRequest request, final HttpServletResponse response) {
		LOGGER.info("Start processing verify Advance reporting switch web service.");
		Boolean isReportingSwitchOn = Boolean.FALSE;
		LOGGER.info("Successfully executed verify Advance reporting switch web service. Reporting switch value is "
				+ isReportingSwitchOn);
		try {
			response.getWriter().write(isReportingSwitchOn.toString());
		} catch (final IOException ioe) {
			LOGGER.error("Exception in sending message to client. Logged the exception for debugging.", ioe);
		} catch (final Exception ex) {
			LOGGER.error("Exception in sending message to client. Logged the exception for debugging.", ex);
		}
	}

	/**
	 * Notifies the server with service event originated on other server but passed to it. It will notify the Corresponding service and
	 * return the status code.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request.
	 * @param httpServletResponse {@link HttpServletResponse} response.
	 */
	@RequestMapping(value = "/notifyServerForService", method = RequestMethod.POST)
	@ResponseBody
	public void notifyServerForService(final HttpServletRequest httpServletRequest, final HttpServletResponse httpServletResponse) {
		LOGGER.debug("Server is notified to run service event");
		if (null != httpServletRequest && null != httpServletResponse) {
		}
	}

	/**
	 * Copies the log files of java app server to the path specified.
	 * 
	 * @param req the request {@link HttpServletRequest} the request
	 * @param resp the response {@link HttpServletResponse} the response
	 */
	@RequestMapping(value = "/logs", method = RequestMethod.POST)
	@ResponseBody
	public void getLogs(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse) {
		LOGGER.info("Starting get logs web service...");
		try {
			String applicationFolderPath = ApplicationConfigProperties.getApplicationFolderPath(DCMA_HOME);
			if (applicationFolderPath != null) {
				String logFolderPath = getLogFolderPath(applicationFolderPath);
				LOGGER.info("log folder path : " + logFolderPath);
				String copyLogFolderPath = httpRequest.getParameter(zipFolderPath);
				LOGGER.info("copy log folder path : " + copyLogFolderPath);
				File copyLogFolder = new File(copyLogFolderPath);
				File logFolder = new File(logFolderPath);
				FileUtils.copyFolder(logFolder, copyLogFolder);
				httpResponse.setStatus(HttpServletResponse.SC_OK);
			}
		} catch (IOException ioException) {
			LOGGER.error(EphesoftStringUtil.concatenate("an exception occured while copying the logs : ", ioException.toString()));
			try {
				httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e) {
				LOGGER.info(EphesoftStringUtil.concatenate("An exception occured while sending the response ", ioException.toString()));
			}
		} catch (Exception exception) {
			LOGGER.error(exception.toString());
		}
	}

	/**
	 * Returns the java application server log folder path.
	 * 
	 * @param applicationFolderPath {@link String} application folder path
	 * @return {@link String} the log folder path
	 */
	private String getLogFolderPath(String applicationFolderPath) {
		String logFolderPath = EphesoftStringUtil.concatenate(
				applicationFolderPath.substring(0, applicationFolderPath.lastIndexOf(File.separator)), File.separator,
				JAVA_APP_SERVER_FOLDER, File.separator, LOG);
		return logFolderPath;
	}

	static {
		try {
			TrustManager[] trustAllCerts = {new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			}};
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

			HostnameVerifier hostNameVerifier = new HostnameVerifier() {

				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(hostNameVerifier);
		} catch (Exception localException) {
			LOGGER.error("Exception Occurred : " + localException.getMessage());
		}
	}

}
