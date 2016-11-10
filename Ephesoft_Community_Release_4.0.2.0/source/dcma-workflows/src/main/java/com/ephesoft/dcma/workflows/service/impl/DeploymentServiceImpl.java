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

import static com.ephesoft.dcma.workflows.constant.WorkflowConstants.DCMA_WORKFLOWS_PROPERTIES;
import static com.ephesoft.dcma.workflows.constant.WorkflowConstants.META_INF_DCMA_WORKFLOWS;
import static com.ephesoft.dcma.workflows.constant.WorkflowConstants.MODULES_CONSTANT;
import static com.ephesoft.dcma.workflows.constant.WorkflowConstants.PLUGINS_CONSTANT;
import static com.ephesoft.dcma.workflows.constant.WorkflowConstants.UNDERSCORE;
import static com.ephesoft.dcma.workflows.constant.WorkflowConstants.WORKFLOWS_CONSTANT;
import static com.ephesoft.dcma.workflows.constant.WorkflowConstants.WORKFLOW_DEPLOY;
import static com.ephesoft.dcma.workflows.constant.WorkflowConstants.WORKFLOW_STATUS_FINISHED;
import static com.ephesoft.dcma.workflows.constant.WorkflowConstants.WORKFLOW_STATUS_RUNNING;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.batch.container.WorkflowDetailContainer;
import com.ephesoft.dcma.batch.service.WorkflowDetailService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.ProcessDefinitionInfo;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;
import com.ephesoft.dcma.workflows.service.DeploymentService;
import com.ephesoft.dcma.workflows.service.WorkflowCreationService;

/**
 * This class represents a service to deploy all the existing process definitions, check if a workflow is deployed and to create and
 * deploy process definitions for a batch class
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class DeploymentServiceImpl implements DeploymentService {

	/**
	 * {@link EphesoftLogger} Instance of logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(DeploymentServiceImpl.class);

	/**
	 * {@link RepositoryService} Instance of repository service.
	 */
	@Autowired
	private RepositoryService repositoryService;

	/**
	 * {@link WorkflowDetailService} Instance of repository service.
	 */
	@Autowired
	private WorkflowDetailService workflowDetailService;

	/**
	 * {@link WorkflowCreationService} Instance of Workflow creation service.
	 */
	@Autowired
	WorkflowCreationService workflowCreationService;

	/**
	 * {@link List{@link String} List of workflow definitions
	 */
	private List<String> workflowsDefinitionList;

	/**
	 * boolean deploy all workflows
	 */
	private boolean deployAllWorkflow;

	/**
	 * {@link String} Base path for new workflows
	 */
	private String newWorkflowsBasePath;

	@Autowired
	private BatchClassService batchClassService;

	@Override
	public void deploy(final String processDefinitionPath) {
		if (!EphesoftStringUtil.isNullOrEmpty(processDefinitionPath)) {
			LOGGER.info("Deploying process definition at path: ", processDefinitionPath);
			DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
			try {
				InputStream bpmnInputStream = new FileInputStream(processDefinitionPath);
				deploymentBuilder = deploymentBuilder.addInputStream(processDefinitionPath, bpmnInputStream);
				deploymentBuilder.deploy();
			} catch (final IOException ioException) {
				LOGGER.error(ioException, "IOException occurred while deploying process definition: ", ioException.getMessage());
			}
		} else {
			LOGGER.info("Process definition path is either null or empty");
		}
	}

	@Override
	public boolean isDeployed(final String processDefinition) {
		boolean isWorkflowDeployed = false;
		final List<ProcessDefinition> deploymentList = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey(processDefinition).list();
		if (CollectionUtils.isNotEmpty(deploymentList)) {
			isWorkflowDeployed = true;
		}
		LOGGER.debug("Is workflow ", processDefinition, " deployed: ", isWorkflowDeployed);
		return isWorkflowDeployed;
	}

	@Override
	@PostConstruct
	public void deployAll() {
		if (deployAllWorkflow) {
			LOGGER.info("workflow.deploy property is set to true, All process definitions will be deployed");
			try {
				File newWorkflowFolder = new File(newWorkflowsBasePath);
				LOGGER.info("Base path for newly created workflows: ", newWorkflowsBasePath);
				deployProcessDefinitionFromSubdirectory(newWorkflowFolder, PLUGINS_CONSTANT);
				for (String processDefinition : workflowsDefinitionList) {
					LOGGER.info("Deploying default process definition at: ", processDefinition);
					DeploymentBuilder deployment = repositoryService.createDeployment();
					deployment.addClasspathResource(new ClassPathResource(processDefinition).getPath());
					deployment.deploy();
				}
				deployProcessDefinitionFromSubdirectory(newWorkflowFolder, MODULES_CONSTANT);
				deployProcessDefinitionFromSubdirectory(newWorkflowFolder, WORKFLOWS_CONSTANT);
				ClassPathResource workflowsClassPathResource = new ClassPathResource(META_INF_DCMA_WORKFLOWS);
				File workflowDirectory = workflowsClassPathResource.getFile();
				upgradeExistingBatchClasses();
				updatePropertyFile(workflowDirectory.getAbsolutePath());
				upgradeExistingBatchClasses();
			} catch (IOException exception) {
				LOGGER.error(exception, "IOException occurred: ", exception.getMessage());
				throw new DCMABusinessException("An error occured while trying to deploy process definition", exception);
			}
		} else {
			LOGGER.info("workflow.deploy property is set to false, Process definitions will not be deployed");
		}
	}

	/**
	 * Updates deploy property in the properties file
	 * 
	 * @param workflowDirectoryPath {@link String}
	 * @throws IOException {@link IOException}
	 */
	private void updatePropertyFile(final String workflowDirectoryPath) throws IOException {
		LOGGER.trace("Entering method to update dcma-workflows properties file");
		File propertyFile = new File(EphesoftStringUtil.concatenate(workflowDirectoryPath, File.separator, DCMA_WORKFLOWS_PROPERTIES));
		Map<String, String> propertyMap = new HashMap<String, String>();
		propertyMap.put(WORKFLOW_DEPLOY, Boolean.toString(false));
		String comments = "Workflow deploy property changed.";
		FileUtils.updateProperty(propertyFile, propertyMap, comments);
	}

	/**
	 * Deploys process definition inside a subdirectory
	 * 
	 * @param workflowDirectory {@link File}
	 * @param subDirectoryName {@link String}
	 */
	private void deployProcessDefinitionFromSubdirectory(final File workflowDirectory, final String subDirectoryName)
			throws IOException {
		String workflowDirectoryPath = workflowDirectory.getAbsolutePath();
		String subDirectory = EphesoftStringUtil.concatenate(workflowDirectoryPath, File.separator, subDirectoryName);
		LOGGER.info("Deploying process definitions in ", subDirectory);
		File workflowSubDirectory = new File(subDirectory);
		if (workflowSubDirectory.exists()) {
			List<String> filePaths;
			filePaths = FileUtils.listDirectory(workflowSubDirectory, false);
			String filePath;
			for (String fileName : filePaths) {
				filePath = EphesoftStringUtil.concatenate(workflowDirectoryPath, File.separator, fileName);
				LOGGER.info("Deploying process definition at path: ", filePath);
				deploy(filePath);
			}
		}
	}

	@Override
	public void createAndDeployProcessDefinition(final BatchClass batchClass, final boolean isGridWorkflow) throws DCMAException {
		if (null != batchClass) {
			String batchClassIdentifier = batchClass.getIdentifier();
			LOGGER.info("Entering method to create and deploy process definitions for batch class: ", batchClassIdentifier);

			// check and create folders if they don't exist
			try {
				File workflowDirectory = new File(newWorkflowsBasePath);
				if (!workflowDirectory.exists()) {
					workflowDirectory.mkdir();
				}
				String workflowDirectoryPath = workflowDirectory.getAbsolutePath();
				String newModulesDirectoryPath = EphesoftStringUtil.concatenate(workflowDirectoryPath, File.separator,
						MODULES_CONSTANT, File.separator, batchClassIdentifier);
				String newWorkflowDirectoryPath = EphesoftStringUtil.concatenate(workflowDirectoryPath, File.separator,
						WORKFLOWS_CONSTANT, File.separator, batchClassIdentifier);
				File newWorkflowDirectory = new File(newModulesDirectoryPath);
				LOGGER.info("Creating workflow directory ", newWorkflowDirectory.getPath(), " if it does not exist");
				newWorkflowDirectory.mkdirs();
				File newModulesDirectory = new File(newWorkflowDirectoryPath);
				LOGGER.info("Creating modules directory ", newModulesDirectoryPath, " if it does not exist");
				newModulesDirectory.mkdirs();
				List<String> processDefinitionPaths = createProcessDefinitions(batchClass, newModulesDirectoryPath,
						newWorkflowDirectoryPath);

				// deploy process definitions at paths in the list
				for (String path : processDefinitionPaths) {
					deploy(path);
				}
			} catch (final Exception exception) {
				LOGGER.error(exception, "Exception while deploying", exception.getMessage());
				throw new DCMAException(exception.getMessage());
			}
		} else {
			LOGGER.info("null received for batch class");
			throw new DCMAException("null received for batch class argument");
		}
	}

	/**
	 * Creates process definition for the given batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param newModulesDirectoryPath {@link String}
	 * @throws newWorkflowDirectoryPath {@link String}
	 */
	private List<String> createProcessDefinitions(final BatchClass batchClass, String newModulesDirectoryPath,
			String newWorkflowDirectoryPath) throws DCMAException {
		String batchClassIdentifier = batchClass.getIdentifier();

		// #5211: Batch class modules list is fetched from loaded batch
		// class to avoid lazy initialisation exception.
		BatchClass batchClassUpdated = batchClassService.getLoadedBatchClassByIdentifier(batchClassIdentifier);
		List<String> processDefinitionPaths = new ArrayList<String>();
		List<ProcessDefinitionInfo> subProcessDefinitionInfo = new ArrayList<ProcessDefinitionInfo>();
		List<BatchClassModule> batchClassModules = getBatchClassModules(batchClassUpdated);
		List<String> moduleWorkflowNameList = getModuleWorkflowNames(batchClassModules);
		WorkflowDetailContainer batchClassWorkflowContainer = new WorkflowDetailContainer(batchClassIdentifier);
		Map<String, String> moduleNames = getModuleNames(batchClassModules);
		batchClassWorkflowContainer.setModuleNameMap(moduleNames);
		if (CollectionUtils.isNotEmpty(moduleWorkflowNameList)) {
			for (String moduleWorkflowName : moduleWorkflowNameList) {
				LOGGER.info("Creating process definition file for module: ", moduleWorkflowName);
				Map<String, String> pluginWorkflowNames = new LinkedHashMap<String, String>();
				subProcessDefinitionInfo = getProcessDefinitionCreationInfo(batchClassUpdated, moduleWorkflowName, pluginWorkflowNames);
				batchClassWorkflowContainer.addModule(moduleWorkflowName, pluginWorkflowNames);
				if (null != subProcessDefinitionInfo) {
					String processDefinitionPath = workflowCreationService.writeProcessDefinitions(newModulesDirectoryPath,
							moduleWorkflowName, subProcessDefinitionInfo, false);
					if (!EphesoftStringUtil.isNullOrEmpty(processDefinitionPath)) {
						processDefinitionPaths.add(processDefinitionPath);
					}
				}
			}
			workflowDetailService.createBatchClassWorkflow(batchClassWorkflowContainer);
		}
		if (CollectionUtils.isEmpty(moduleWorkflowNameList)) {
			moduleWorkflowNameList = new ArrayList<String>(2);
		}

		// add Workflow_Status_Running at the beginning and
		// Workflow_Status_Finished at the end to update the status for batch instance
		moduleWorkflowNameList.add(0, WORKFLOW_STATUS_RUNNING);
		moduleWorkflowNameList.add(moduleWorkflowNameList.size(), WORKFLOW_STATUS_FINISHED);
		subProcessDefinitionInfo.clear();
		for (String moduleName : moduleWorkflowNameList) {
			subProcessDefinitionInfo.add(new ProcessDefinitionInfo(moduleName));
		}
		LOGGER.info("Creating process definition file for batch class: ", batchClass.getIdentifier());
		String workflowProcessDefinitionPath = workflowCreationService.writeProcessDefinitions(newWorkflowDirectoryPath,
				batchClass.getName(), subProcessDefinitionInfo, true);
		if (!EphesoftStringUtil.isNullOrEmpty(workflowProcessDefinitionPath)) {
			processDefinitionPaths.add(workflowProcessDefinitionPath);
		}
		return processDefinitionPaths;
	}

	/**
	 * Fetches the list of module workflow names in the batch class.
	 * 
	 * @param batchClassModules {@link List{@link BatchClassModule}>
	 * @return {@link List<{@link String}>}
	 */
	private List<String> getModuleWorkflowNames(final List<BatchClassModule> batchClassModules) {
		List<String> moduleWorkflowNameList = null;
		if (CollectionUtils.isNotEmpty(batchClassModules)) {
			moduleWorkflowNameList = new ArrayList<String>();
			for (BatchClassModule batchClassModule : batchClassModules) {
				moduleWorkflowNameList.add(batchClassModule.getWorkflowName());
			}
		}
		return moduleWorkflowNameList;
	}

	/**
	 * Fetches the list of module names in the batch class
	 * 
	 * @param batchClassModules {@link List{@link BatchClassModule}>
	 * @return {@link List<{@link String}, {@link String}>}
	 */
	private Map<String, String> getModuleNames(final List<BatchClassModule> batchClassModules) {
		Map<String, String> moduleNames = null;
		if (CollectionUtils.isNotEmpty(batchClassModules)) {
			moduleNames = new LinkedHashMap<String, String>();
			for (BatchClassModule batchClassModule : batchClassModules) {
				if (null != batchClassModule) {
					Module module = batchClassModule.getModule();
					String workflowName = batchClassModule.getWorkflowName();
					if (null == module) {
						LOGGER.error("Could not get module name for module with workflow: ", workflowName);
						moduleNames.put(workflowName, workflowName);
					} else {
						
						// Change display of module/plugin keywords to camelcase in batch progress bar.
						String moduleName = module.getDescription();
						moduleName = moduleName.replace(WorkflowConstants.MODULE_KEYWORD_LOWER_CASE,
								WorkflowConstants.MODULE_KEYWORD_CAMEL_CASE);
						moduleNames.put(workflowName, moduleName);
					}
				}
			}
		}
		return moduleNames;
	}

	/**
	 * Fetches the list of modules in the batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 */
	private List<BatchClassModule> getBatchClassModules(final BatchClass batchClass) {
		List<BatchClassModule> batchClassModules = null;
		if (null != batchClass) {
			LOGGER.info("Getting list of modules in batch class ", batchClass.getIdentifier());
			String batchClassIdentifier = batchClass.getIdentifier();

			// #5211: Batch class modules list is fetched from loaded batch
			// class to avoid lazy initialization exception.
			BatchClass batchClassUpdated = batchClassService.getLoadedBatchClassByIdentifier(batchClassIdentifier);
			if (null != batchClassUpdated) {
				batchClassModules = batchClassUpdated.getBatchClassModuleInOrder();
			}
		}
		return batchClassModules;
	}

	/**
	 * Creates a list of process definition information for each subprocess
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param moduleName {@link String}
	 * @return {@link List}<{@link ProcessDefinitionInfo}>
	 */

	private List<ProcessDefinitionInfo> getProcessDefinitionCreationInfo(final BatchClass batchClass, final String moduleName,
			final Map<String, String> pluginWorkflowNames) {
		LOGGER.info("Getting process definition info for module: ", moduleName);
		List<ProcessDefinitionInfo> processDefinitionInfo = null;
		BatchClassModule module = batchClass.getBatchClassModuleByWorkflowName(moduleName);
		if (null != module) {
			processDefinitionInfo = new ArrayList<ProcessDefinitionInfo>();
			List<BatchClassPlugin> pluginList = module.getBatchClassPluginInOrder();
			if (CollectionUtils.isNotEmpty(pluginList)) {

				// List for batch class workflow names.
				// List<String> pluginWorkflowNames = new LinkedList<String>();
				for (BatchClassPlugin batchClassPlugin : pluginList) {
					String subProcessName = ICommonConstants.EMPTY_STRING;
					String scriptName = ICommonConstants.EMPTY_STRING;
					String backUpFileName = ICommonConstants.EMPTY_STRING;
					boolean isScriptingPlugin = false;
					String subProcessKey = ICommonConstants.EMPTY_STRING;
					String pluginScriptName = ICommonConstants.EMPTY_STRING;
					if (null != batchClassPlugin) {
						Plugin plugin = batchClassPlugin.getPlugin();
						if (null != plugin) {
							String pluginWorkflowName = plugin.getWorkflowName();
							String pluginDescription = plugin.getDescription();

							// Change display of module/plugin keywords to camelcase in batch progress bar.
							pluginDescription = pluginDescription.replace(WorkflowConstants.PLUGIN_KEYWORD_LOWER_CASE,
									WorkflowConstants.PLUGIN_KEYWORD_CAMEL_CASE);
							if (null != pluginWorkflowNames) {
								if (EphesoftStringUtil.isNullOrEmpty(pluginDescription)) {
									LOGGER.error("Plugin workflow name is missing for: ", pluginDescription);
									pluginWorkflowNames.put(pluginWorkflowName, pluginWorkflowName);
								} else {
									LOGGER.debug("Plugin workflow name for plugin: ", pluginDescription, "is: ", pluginWorkflowName);
									pluginWorkflowNames.put(pluginWorkflowName, pluginDescription);
								}
							}
							subProcessKey = pluginWorkflowName;
							subProcessName = plugin.getWorkflowName();
							subProcessName = checkForPluginMultipleInstance(processDefinitionInfo, subProcessKey);
							pluginScriptName = plugin.getScriptName();
						}
					}
					if (null != pluginScriptName) {
						LOGGER.info(subProcessKey, " plugin is a scripting plugin.");
						isScriptingPlugin = true;
						scriptName = pluginScriptName;
						backUpFileName = subProcessKey;
					}
					processDefinitionInfo.add(new ProcessDefinitionInfo(subProcessName, isScriptingPlugin, scriptName, backUpFileName,
							subProcessKey));
				}
			}
		} else {
			LOGGER.info("No workflow found with the name ", moduleName);
		}
		return processDefinitionInfo;
	}

	/**
	 * Checks if there are multiple instance of subprocess
	 * 
	 * @param processDefinitionInfo {@link List<{@link ProcessDefinitionInfo}>}
	 * @param subProcessKey
	 * @return
	 */
	private String checkForPluginMultipleInstance(final List<ProcessDefinitionInfo> processDefinitionInfo, final String subProcessKey) {
		String subProcessName = subProcessKey;
		final List<String> subProcessnames = new ArrayList<String>();
		LOGGER.info("Creating list of already used sub process names.");
		for (final ProcessDefinitionInfo modulePluginCreationInfo : processDefinitionInfo) {
			subProcessnames.add(modulePluginCreationInfo.getSubProcessName());
		}
		subProcessName = generateWorkflowName(subProcessName, subProcessnames);
		return subProcessName;
	}

	/**
	 * Generates new workflow name for duplicate subprocess
	 * 
	 * @param subProcessName {@link String}
	 * @param subProcessnames {@link List<{@link String}>}
	 * @return {@link String}
	 */

	private String generateWorkflowName(String subProcessName, final List<String> subProcessnames) {
		boolean nameAlreadyInUse;
		int index = 0;
		nameAlreadyInUse = subProcessnames.contains(subProcessName);
		String tempSubProcessName = subProcessName;
		while (nameAlreadyInUse) {
			index++;
			tempSubProcessName = EphesoftStringUtil.concatenate(subProcessName, UNDERSCORE, index);
			nameAlreadyInUse = subProcessnames.contains(tempSubProcessName);
			LOGGER.info(tempSubProcessName, " sub process name already in use ");
		}
		subProcessName = tempSubProcessName;
		return subProcessName;
	}

	/**
	 * API to upgrade existing JBPM driven process definitions to the newer Activiti driven definitions.
	 */
	private void upgradeExistingBatchClasses() {
		List<BatchClass> allBatchClasses = batchClassService.getAllBatchClasses();
		if (!(allBatchClasses.isEmpty()) || !(allBatchClasses == null)) {
			for (BatchClass batchClass : allBatchClasses) {
				if (!batchClass.isDeleted() && !(batchClass.getIdentifier().equalsIgnoreCase(WorkflowConstants.BC3))) {
					try {
						createAndDeployProcessDefinition(batchClass, true);
					} catch (DCMAException e) {
						LOGGER.info("Error while deploying process definitions: " + e.getMessage());
					}
				}
			}
		}

	}

	/**
	 * Gets list of workflow definitions
	 * 
	 * @return {@link List<{@link String}>}
	 */
	public List<String> getWorkflowsDefinitionList() {
		return workflowsDefinitionList;
	}

	/**
	 * Sets workflowsDefinitionList
	 * 
	 * @param workflowsDefinitionList {@link List<{@link String}>}
	 */
	public void setWorkflowsDefinitionList(final List<String> workflowsDefinitionList) {
		this.workflowsDefinitionList = workflowsDefinitionList;
	}

	/**
	 * Gets deployAllWorkflow property
	 * 
	 * @return {@link Boolean}
	 */
	public boolean getDeployAllWorkflow() {
		return deployAllWorkflow;
	}

	/**
	 * Sets deployAllWorkflow property
	 * 
	 * @param deploy
	 */
	public void setDeployAllWorkflow(final boolean deployAllWorkflow) {
		this.deployAllWorkflow = deployAllWorkflow;
	}

	/**
	 * Gets newWorkflowsBasePath.
	 * 
	 * @return {@link String}
	 */
	public String getNewWorkflowsBasePath() {
		return newWorkflowsBasePath;
	}

	/**
	 * Sets newWorkflowsBasePath.
	 * 
	 * @param newWorkflowsBasePath {@link String}
	 */
	public void setNewWorkflowsBasePath(final String newWorkflowsBasePath) {
		this.newWorkflowsBasePath = newWorkflowsBasePath;
	}
}
