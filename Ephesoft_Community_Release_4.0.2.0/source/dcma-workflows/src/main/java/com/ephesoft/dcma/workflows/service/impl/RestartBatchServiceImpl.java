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

package com.ephesoft.dcma.workflows.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.batch.service.ImportBatchService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchClassModuleService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ServerRegistryService;
import com.ephesoft.dcma.util.BackUpFileService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.client.EphesoftWebServiceClient;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;
import com.ephesoft.dcma.workflows.service.RestartBatchService;
import com.ephesoft.dcma.workflows.service.WorkflowService;
import com.ephesoft.dcma.workflows.service.engine.EngineService;
import com.ephesoft.dcma.workflows.util.RestartingBatchDetails;

/**
 * This class represents service to restart batch.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RestartBatchServiceImpl implements RestartBatchService {

	/**
	 * {@link BatchClassService} Instance of batch class service.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * {@link BatchInstanceService} Instance of batch instance service.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * {@link ServerRegistryService} Instance of registry service.
	 */
	@Autowired
	private ServerRegistryService registryService;

	/**
	 * {@link EphesoftWebServiceClient} Instance of Web service client.
	 */
	@Autowired
	private EphesoftWebServiceClient ephesoftWebServiceClient;

	/**
	 * {@link BatchClassModuleService} Instance of batch class module service.
	 */
	@Autowired
	private BatchClassModuleService batchClassModuleService;

	/**
	 * {@link BatchSchemaService} Instance of batch schema service.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * {@link ImportBatchService} Instance of import batch service.
	 */
	@Autowired
	private ImportBatchService importBatchService;

	/**
	 * {@link EngineService} Instance of engine service.
	 */
	@Autowired
	private EngineService engineService;

	/**
	 * {@link WorkflowService} Instance of workflow service.
	 */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * {@link EphesoftLogger} Instance of Logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(RestartBatchServiceImpl.class);

	@Override
	public boolean restartBatchInstanceWithoutCleaning(final String batchInstanceIdentifier, final String moduleName) {
		boolean result = false;
		try {
			result = restartBatch(batchInstanceIdentifier, moduleName, false, false, false);
		} catch (DCMAApplicationException dcmaApplicationException) {
			// Do Nothing.
		}
		return result;
	}

	@Override
	public boolean restartBatchInstance(String batchInstanceIdentifier, String moduleName, boolean throwException,
			boolean resumeBatchInstance) throws DCMAApplicationException {
		boolean result = false;
		result = restartBatch(batchInstanceIdentifier, moduleName, throwException, true, resumeBatchInstance);
		return result;
	}

	@Override
	public String getBatchLastExecutedModule(final BatchInstance batchInstance) {
		String moduleName;
		final String executedModules = batchInstance.getExecutedModules();
		if (EphesoftStringUtil.isNullOrEmpty(executedModules)) {
			moduleName = null;
		} else {
			final String[] splitString = EphesoftStringUtil.splitString(executedModules,
					String.valueOf(ICommonConstants.EXECUTED_MODULE_ID_SEPARATOR));
			if (null == splitString) {
				moduleName = null;
			} else {
				final long moduleId = Long.valueOf(splitString[splitString.length - 1]);
				BatchClass loadedBatchClass = batchInstanceService.getLoadedBatchClassByBatchIdentifier(batchInstance);
				if (null == loadedBatchClass) {
					moduleName = null;
				} else {
					moduleName = batchClassModuleService.getModuleNameByModuleId(loadedBatchClass, moduleId);
				}
			}
		}
		return moduleName;
	}

	/**
	 * Restarts a batch instance.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param moduleName {@link String}
	 * @param throwException boolean
	 * @param isClean boolean
	 * @param resumeBatchInstance boolean
	 * @return boolean
	 * @throws DCMAApplicationException
	 */
	private boolean restartBatch(final String batchInstanceIdentifier, final String moduleName, final boolean throwException,
			final boolean isClean, final boolean resumeBatchInstance) throws DCMAApplicationException {
		boolean result = true;
		LOGGER.info("Restarting batch instance: ", batchInstanceIdentifier);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		if (null != batchInstance) {
			if (!EphesoftStringUtil.isNullOrEmpty(batchInstance.getReviewUserName())
					|| !EphesoftStringUtil.isNullOrEmpty(batchInstance.getValidationUserName())) {
				clearReviewValidationUsers(batchInstance, moduleName);

			}
			try {
				resetBatchClassAndPriority(batchInstance);
				final String batchInstanceExecutingServer = batchInstance.getExecutingServer();
				boolean sameServer = engineService.isSameServer(batchInstanceExecutingServer);
				List<BatchClassModule> batchClassModulesInOrder = getBatchClassModules(batchInstance);
				BatchClassModule prescursorBatchClassModule;
				if (CollectionUtils.isNotEmpty(batchClassModulesInOrder)) {
					prescursorBatchClassModule = batchClassModulesInOrder.get(0);
				} else {
					prescursorBatchClassModule = null;
				}

				// if case is for remote web service or other server starting it.
				if (!sameServer && null != batchInstanceExecutingServer) {
					result = restartBatchRemotely(batchInstanceIdentifier, moduleName, throwException, batchInstance,
							batchInstanceExecutingServer, prescursorBatchClassModule);
				} else {
					RestartingBatchDetails restartingBatchDetails = new RestartingBatchDetails();
					restartingBatchDetails.setModuleName(moduleName);
					restartingBatchDetails.setBatchClassModulesInOrder(batchClassModulesInOrder);
					restartingBatchDetails.setBatchInstance(batchInstance);
					restartingBatchDetails.setBatchInstanceIdentifier(batchInstanceIdentifier);
					restartingBatchDetails.setPrescursorBatchClassModule(prescursorBatchClassModule);
					result = restartBatchInternally(restartingBatchDetails, throwException, isClean, resumeBatchInstance);
				}
			} catch (Exception exception) {
				result = false;
				LOGGER.error(exception, "Batch: ", batchInstanceIdentifier,
						" could not be restarted and went into error state due to: ", exception.getMessage());

				// update the batch instance to ERROR state.
				if (throwException) {
					throw new DCMAApplicationException(exception.getMessage());
				}
			}
		}
		return result;

	}

	private void clearReviewValidationUsers(BatchInstance batchInstance, String moduleName) {
		List<BatchClassModule> moduleList = batchInstance.getBatchClass().getBatchClassModuleInOrder();
		BatchClassModule restartModule = batchInstance.getBatchClass().getBatchClassModuleByDesc(moduleName);
		BatchClassModule lastExecutedModule = batchInstance.getBatchClass().getBatchClassModuleByDesc(
				getBatchLastExecutedModule(batchInstance));

		for (BatchClassModule batchClassModule : moduleList) {
			if (batchClassModule.compareTo(lastExecutedModule) <= 0 && batchClassModule.compareTo(restartModule) >= 0) {
				for (BatchClassPlugin batchClassPlugin : batchClassModule.getBatchClassPlugins()) {
					if (batchClassPlugin.getPlugin().getPluginName().equalsIgnoreCase("REVIEW_DOCUMENT")) {
						batchInstance.setReviewUserName(null);
					}
					if (batchClassPlugin.getPlugin().getPluginName().equalsIgnoreCase("VALIDATE_DOCUMENT")) {
						batchInstance.setValidationUserName(null);
					}
				}
			}
		}
		batchInstanceService.updateBatchInstance(batchInstance);
	}

	/**
	 * Gets modules for a batch class.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return {@link List}<{@link BatchClassModule}>
	 */
	private List<BatchClassModule> getBatchClassModules(final BatchInstance batchInstance) {
		List<BatchClassModule> batchClassModulesInOrder = null;

		// #5211: Batch class modules list is fetched from loaded batch class to avoid lazy initialization exception.
		BatchClass batchClassUpdated = batchInstanceService.getLoadedBatchClassByBatchIdentifier(batchInstance);
		if (null != batchClassUpdated) {
			batchClassModulesInOrder = batchClassUpdated.getBatchClassModuleInOrder();
		}
		return batchClassModulesInOrder;
	}

	/**
	 * Restarts a batch instance on the current server.
	 * 
	 * @param restartingBatchDetails {@link RestartingBatchDetails}
	 * @param throwException boolean
	 * @param isClean boolean
	 * @param resumeBatchInstance boolean
	 * @return boolean
	 * @throws DCMAApplicationException
	 */
	private boolean restartBatchInternally(final RestartingBatchDetails restartingBatchDetails, final boolean throwException,
			final boolean isClean, final boolean resumeBatchInstance) throws DCMAApplicationException {
		boolean result;
		String batchInstanceIdentifier = restartingBatchDetails.getBatchInstanceIdentifier();
		final BatchInstance batchInstance = restartingBatchDetails.getBatchInstance();

		// #5211: To stop batches stuck at RESTART_IN_PROGRESS
		if (isClean) {
			engineService.cleanBatchResources(batchInstanceIdentifier);
		}
		engineService.deleteProcessInstanceByBatchInstance(batchInstance, false);
		batchSchemaService.removeBatch(batchInstanceIdentifier);
		verifyRestartForWorkFlowContinue(batchInstanceIdentifier, throwException);

		// batchClassModulesInOrder same server restarting it or not started yet.
		String updatedExecutedModules = updateBatchInstance(restartingBatchDetails);
		if (resumeBatchInstance) {
			String moduleWorkFlowName = workflowService.getWorkflowNameByModuleName(restartingBatchDetails.getModuleName(),
					restartingBatchDetails.getBatchClassModulesInOrder());
			workflowService.startWorkflow(restartingBatchDetails.getBatchInstance().getBatchInstanceID(), moduleWorkFlowName);
		} else {
			batchInstance.setStatus(BatchInstanceStatus.READY);
		}
		batchInstance.setCurrentUser(null);
		batchInstance.setServerIP(null);
		batchInstance.setExecutingServer(null);
		batchInstance.setExecutedModules(updatedExecutedModules);
		batchInstanceService.updateBatchInstance(batchInstance);
		result = true;
		return result;
	}

	/**
	 * Verify if restart is valid.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param throwException boolean
	 * @throws DCMAApplicationException
	 */
	private void verifyRestartForWorkFlowContinue(final String batchInstanceIdentifier, final boolean throwException)
			throws DCMAApplicationException {
		String activeModule = workflowService.getActiveModuleName(batchInstanceIdentifier);
		if (null == activeModule) {
			activeModule = workflowService.getErrorBatchLastExecutedModule(batchInstanceIdentifier);
		}
		if (!EphesoftStringUtil.isNullOrEmpty(activeModule) && activeModule.contains(WorkflowConstants.WORKFLOW_CONTINUE_CHECK)) {
			LOGGER.error("Error in restarting batch instance : ", batchInstanceIdentifier);
			if (throwException) {
				throw new DCMAApplicationException(WorkflowConstants.RESTART_ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Updates batch instance for making it ready for restart.
	 * 
	 * @param restartingBatchDetails {@link RestartingBatchDetails}
	 * @return {@link String}
	 * @throws DCMAApplicationException
	 */
	private String updateBatchInstance(final RestartingBatchDetails restartingBatchDetails) throws DCMAApplicationException {
		String batchInstanceIdentifier = restartingBatchDetails.getBatchInstanceIdentifier();
		String moduleName = restartingBatchDetails.getModuleName();
		final BatchInstance batchInstance = restartingBatchDetails.getBatchInstance();
		final List<BatchClassModule> batchClassModulesInOrder = restartingBatchDetails.getBatchClassModulesInOrder();
		BatchClassModule precursorBatchClassModule = restartingBatchDetails.getPrescursorBatchClassModule();

		// Getting workflow name for the module to be restarted with.
		String moduleWorkFlowName = workflowService.getWorkflowNameByModuleName(moduleName, batchClassModulesInOrder);
		if (null == moduleWorkFlowName && null != batchInstance.getRemoteBatchInstance()) {
			moduleWorkFlowName = batchInstance.getRemoteBatchInstance().getSourceModule();
		}
		if (EphesoftStringUtil.isNullOrEmpty(moduleWorkFlowName)) {
			deleteBatchFolder(batchInstance);
			importBatchService.removeFolders(batchInstance);
			if (null != precursorBatchClassModule) {
				moduleWorkFlowName = precursorBatchClassModule.getWorkflowName();
			}
		} else {
			Properties properties = fetchConfig(BackUpFileService.BACKUP_PROPERTY_FILE);
			try {
				importBatchService.updateBatchFolders(properties, batchInstance, moduleWorkFlowName,
						batchSchemaService.isZipSwitchOn(), batchClassModulesInOrder);
			} catch (Exception updateBatchFolderException) {
				String errorMessage = EphesoftStringUtil.concatenate("Error while updating batch folders for the batch: ",
						batchInstanceIdentifier, " while restarting it.");
				LOGGER.error(errorMessage);
				throw new DCMAApplicationException(errorMessage, updateBatchFolderException);
			}
		}
		String executedModules = updateExecutedModules(batchInstanceIdentifier, moduleWorkFlowName, batchInstance);
		return executedModules;
	}

	/**
	 * Restart batch remotely on another server.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param moduleName {@link String}
	 * @param throwException boolean
	 * @param batchInstance {@link BatchInstance}
	 * @param batchInstanceExecutingServer {@link String}
	 * @param prescursorBatchClassModule {@link BatchClassModule}
	 * @return boolean
	 * @throws DCMAApplicationException {@link DCMAApplicationException}
	 */
	private boolean restartBatchRemotely(final String batchInstanceIdentifier, String moduleName, final boolean throwException,
			final BatchInstance batchInstance, final String batchInstanceExecutingServer,
			final BatchClassModule prescursorBatchClassModule) throws DCMAApplicationException {
		boolean result;
		if (moduleName == null) {
			if (null != batchInstance.getRemoteBatchInstance()) {
				moduleName = batchInstance.getRemoteBatchInstance().getSourceModule();
			} else if (null != prescursorBatchClassModule) {
				moduleName = prescursorBatchClassModule.getModule().getName();
			}
		}
		if (EphesoftContext.getCurrent().isSecure()) {
			result = ephesoftWebServiceClient.restartBatchInstance(EphesoftStringUtil.concatenate(ICommonConstants.HTTPS_SLASH,
					batchInstanceExecutingServer, ICommonConstants.FORWARD_SLASH, ICommonConstants.WEB_SERVICE),
					batchInstanceIdentifier, moduleName, String.valueOf(throwException));
		} else {
			result = ephesoftWebServiceClient.restartBatchInstance(EphesoftStringUtil.concatenate(ICommonConstants.HTTP_SLASH,
					batchInstanceExecutingServer, ICommonConstants.FORWARD_SLASH, ICommonConstants.WEB_SERVICE),
					batchInstanceIdentifier, moduleName, String.valueOf(throwException));
		}
		return result;
	}

	/**
	 * Update executed modules for restart.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param moduleWorkflowName {@link String}
	 * @param batchInstance {@link BatchInstance}
	 * @return {@link String}
	 */
	private String updateExecutedModules(final String batchInstanceIdentifier, final String moduleWorkflowName,
			final BatchInstance batchInstance) {
		String editedExecutedModules;
		if (EphesoftStringUtil.isNullOrEmpty(moduleWorkflowName)) {
			editedExecutedModules = ICommonConstants.EMPTY_STRING;
		} else {
			String executedModules = batchInstance.getExecutedModules();
			if (EphesoftStringUtil.isNullOrEmpty(executedModules)) {
				editedExecutedModules = ICommonConstants.EMPTY_STRING;
			} else {
				BatchClass batchClass = batchInstanceService.getLoadedBatchClassByBatchIdentifier(batchInstance);
				if (null == batchClass) {
					editedExecutedModules = ICommonConstants.EMPTY_STRING;
				} else {
					String batchClassIdentifier = batchClass.getIdentifier();
					long moduleId = batchClassModuleService.getModuleIdByWorkflowName(batchClassIdentifier, moduleWorkflowName);
					editedExecutedModules = editExecutedModules(moduleWorkflowName, executedModules, moduleId);
				}
			}
		}
		LOGGER.debug("Updated executed modules for restarting batch: ", batchInstanceIdentifier, " is: ", editedExecutedModules);
		return editedExecutedModules;
	}

	/**
	 * Edit executed modules for the restarting batches.
	 * 
	 * @param moduleName {@link String}
	 * @param executedModules {@link String}
	 * @param batchClassModuleByWorkflowName {@link BatchClassModule}
	 * @return {@link String}
	 */
	private String editExecutedModules(final String moduleName, final String executedModules, final long moduleId) {

		// Updating executed modules just till the module id(and separator) of the module restarted with.
		String editedExecutedModules;
		if (0 != moduleId) {
			LOGGER.debug("Module name: ", moduleName, " with module id: ", moduleId);
			int index = executedModules.indexOf(String.valueOf(moduleId));
			if (index > -1) {
				editedExecutedModules = executedModules.substring(0, index + 2);
			} else {
				editedExecutedModules = ICommonConstants.EMPTY_STRING;
			}
		} else {
			editedExecutedModules = ICommonConstants.EMPTY_STRING;
		}
		return editedExecutedModules;
	}

	/**
	 * Deletes a batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	private void deleteBatchFolder(final BatchInstance batchInstance) {
		String batchFilePath = EphesoftStringUtil.concatenate(batchInstance.getLocalFolder(), File.separator,
				batchInstance.getIdentifier());
		String batchInstanceIdentifier = batchInstance.getIdentifier();
		if (EphesoftStringUtil.isNullOrEmpty(batchFilePath)) {
			LOGGER.debug("Batch file path for batch: ", batchInstanceIdentifier, " for deleting its folders, is: ", batchFilePath);
			File batchInstanceFolder = new File(batchFilePath);
			if (batchInstanceFolder.exists()) {
				FileUtils.deleteDirectoryAndContentsRecursive(batchInstanceFolder);
			}
		}
		LOGGER.info("Batch folder deleted successfully for ", batchInstanceIdentifier);
	}

	/**
	 * Resets batch class for a batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	private void resetBatchClassAndPriority(final BatchInstance batchInstance) {
		String batchInstanceIdentifier = batchInstance.getIdentifier();
		LOGGER.trace("Entering method to reset batch class for batch instance: ", batchInstanceIdentifier);
		BatchClass oldBatchClass = batchInstance.getBatchClass();

		// Reverted change for batch instance priority with changed priority of batch class.
		if (null != oldBatchClass) {
			String oldBatchClassIdentifier = oldBatchClass.getIdentifier();
			BatchClass newBatchClass = batchClassService.getBatchClassbyName(oldBatchClass.getName());
			String newBatchClassIdentifier = newBatchClass.getIdentifier();
			LOGGER.debug("New batch class identifier for batch instance: ", batchInstanceIdentifier, "is: ", newBatchClassIdentifier);
			if (!EphesoftStringUtil.isNullOrEmpty(newBatchClassIdentifier) && !newBatchClassIdentifier.equals(oldBatchClassIdentifier)) {
				batchInstance.setBatchClass(newBatchClass);
			}
			batchInstanceService.updateBatchInstance(batchInstance);
		}
	}

	/**
	 * Fetches configuration for back up property service.
	 * 
	 * @param filePath {@link String}
	 * @return {@link Properties}
	 */
	private Properties fetchConfig(final String filePath) {
		ClassPathResource classPathResource = new ClassPathResource(filePath);
		Properties properties = new Properties();
		InputStream input = null;
		try {
			input = classPathResource.getInputStream();
			properties.load(input);
		} catch (IOException ioException) {
			LOGGER.error("Cannot open and load backUpService properties file.", ioException);
		} finally {
			try {
				if (null != input)
					input.close();
			} catch (IOException ioException2) {
				LOGGER.error("Cannot close backUpService properties file.", ioException2);
			}
		}
		return properties;
	}

}
