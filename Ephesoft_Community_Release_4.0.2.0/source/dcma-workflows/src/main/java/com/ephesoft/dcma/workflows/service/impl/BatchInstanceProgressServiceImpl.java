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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.container.WorkflowDetailContainer;
import com.ephesoft.dcma.batch.service.WorkflowDetailService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.WorkflowDetail;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;
import com.ephesoft.dcma.workflows.service.BatchInstanceProgressService;
import com.ephesoft.dcma.workflows.service.RestartBatchService;
import com.ephesoft.dcma.workflows.service.WorkflowService;

/**
 * This class represents service class for batch instance progress status.
 * 
 * @author Ephesoft
 * @version 1.1
 * 
 */
public class BatchInstanceProgressServiceImpl implements BatchInstanceProgressService {

	/**
	 * {@link WorkflowService} Instance of workflow service.
	 */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * {@link WorkflowDetailService} Instance of workflow detail service.
	 */
	@Autowired
	private WorkflowDetailService workflowDetailService;

	/**
	 * {@link BatchInstanceService} Instance of batch instance service.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * {@link RestartBatchService} Instance of restart batch instance service.
	 */
	@Autowired
	private RestartBatchService restartBatchService;

	/**
	 * {@link EphesoftLogger} Instance of Logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(BatchInstanceProgressServiceImpl.class);

	@Override
	public WorkflowDetail getWorkflowDetails(final String batchInstanceIdentifier) {
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		BatchInstanceStatus batchStatus = batchInstance.getStatus();
		WorkflowDetail workflowDetail = new WorkflowDetail();
		if (batchStatus != BatchInstanceStatus.FINISHED) {
			WorkflowDetailContainer workflowDetailContainer = workflowDetailService.getWorkflowContainer(batchInstanceIdentifier);

			// Added null check to avoid exception if workflow for batch class is not deployed.
			if (null == workflowDetailContainer) {
				LOGGER.error(EphesoftStringUtil.concatenate("Unable to get workflow details for batch instance ",
						batchInstanceIdentifier));
			} else {
				Map<String, String> workflowModuleNameMap = workflowDetailContainer.getModuleNameMap();
				Map<String, Map<String, String>> workflowPluginMap = workflowDetailContainer.getModules();
				Collection<String> moduleNames = workflowModuleNameMap.values();
				workflowDetail.setModulePluginMap(getModulePluginMap(workflowModuleNameMap, workflowPluginMap));
				String currentModuleAndLastPlugin;
				if (null != batchInstance && batchStatus == BatchInstanceStatus.READY) {
					currentModuleAndLastPlugin = getCurrentModuleNameForReadyBatches(batchInstance);
				} else {
					currentModuleAndLastPlugin = workflowService.getWorkflowDetails(batchInstanceIdentifier);
				}
				LOGGER.debug("Current module and last executed plugin concatenated string is: ", currentModuleAndLastPlugin);
				if (EphesoftStringUtil.isNullOrEmpty(currentModuleAndLastPlugin)) {
					setListToDefaultValue(workflowDetail);
					setModuleList(moduleNames, null, workflowDetail);
				} else {
					String[] parts = currentModuleAndLastPlugin.split(ICommonConstants.COLON);

					// In workflowModuleNameMap, key = module workflow name and value = module name.
					String currentModuleWorkflowName = parts[0];
					if (currentModuleWorkflowName.equals(WorkflowConstants.WORKFLOW_UNIT_END_INDICATOR)) {
						setListToDefaultValue(workflowDetail);
						setModuleList(moduleNames, WorkflowConstants.WORKFLOW_UNIT_END_INDICATOR, workflowDetail);
					} else {
						String currentModuleName = workflowModuleNameMap.get(currentModuleWorkflowName);
						workflowDetail.setCurrentExecutingModule(currentModuleName);
						setModuleList(moduleNames, currentModuleName, workflowDetail);
						Map<String, String> pluginWorkflowNameMap = workflowPluginMap.get(currentModuleWorkflowName);
						if (parts.length == 1) {
							setPluginDetails(pluginWorkflowNameMap, null, workflowDetail);
						} else {
							String lastExecutedPluginWorkflowName = parts[1];
							setPluginDetails(pluginWorkflowNameMap, lastExecutedPluginWorkflowName, workflowDetail);
						}
					}
				}
			}
		}
		return workflowDetail;
	}

	/**
	 * Gets current module name for batches waiting in READY batch instance status.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return {@link String}
	 */
	private String getCurrentModuleNameForReadyBatches(final BatchInstance batchInstance) {
		String moduleName = restartBatchService.getBatchLastExecutedModule(batchInstance);
		BatchClass batchClass = batchInstance.getBatchClass();
		List<BatchClassModule> batchClassModulesInOrder = batchClass.getBatchClassModuleInOrder();
		String moduleWorkFlowName = workflowService.getWorkflowNameByModuleName(moduleName, batchClassModulesInOrder);
		return moduleWorkFlowName;
	}

	/**
	 * Sets plugin details for the workflow detail of a batch instance.
	 * 
	 * @param pluginWorkflowNameMap {@link Map}<{@link String}, {@link String}>
	 * @param lastExecutedPluginWorkflowName {@link String}
	 * @param workflowDetail {@link WorkflowDetail}
	 */
	private void setPluginDetails(Map<String, String> pluginWorkflowNameMap, String lastExecutedPluginWorkflowName,
			WorkflowDetail workflowDetail) {
		if (null != workflowDetail) {
			String currentExecutingPlugin = null;
			int pluginListSize = pluginWorkflowNameMap.size();
			List<String> executedPlugins = null;
			List<String> unexecutedPlugins = null;
			Set<String> pluginWorkflowSet = pluginWorkflowNameMap.keySet();
			Iterator<String> pluginWorkflowNameIterator = pluginWorkflowSet.iterator();
			if (EphesoftStringUtil.isNullOrEmpty(lastExecutedPluginWorkflowName)) {
				unexecutedPlugins = new ArrayList<String>(pluginListSize);
				if (pluginWorkflowNameIterator.hasNext()) {
					String aPluginWorkflowName = pluginWorkflowNameIterator.next();
					currentExecutingPlugin = pluginWorkflowNameMap.get(aPluginWorkflowName);
				}
				while (pluginWorkflowNameIterator.hasNext()) {
					String aPluginWorkflowName = pluginWorkflowNameIterator.next();
					unexecutedPlugins.add(pluginWorkflowNameMap.get(aPluginWorkflowName));
				}
			} else {
				unexecutedPlugins = new ArrayList<String>(pluginListSize);
				executedPlugins = new ArrayList<String>(pluginListSize);
				boolean lastPluginFound = false;
				while (pluginWorkflowNameIterator.hasNext()) {
					String aPluginWorkflowName = pluginWorkflowNameIterator.next();
					if (lastPluginFound) {
						if (null == currentExecutingPlugin) {
							currentExecutingPlugin = pluginWorkflowNameMap.get(aPluginWorkflowName);
						} else {
							unexecutedPlugins.add(pluginWorkflowNameMap.get(aPluginWorkflowName));
						}
					} else {
						if (lastExecutedPluginWorkflowName.equalsIgnoreCase(aPluginWorkflowName)) {
							lastPluginFound = true;
						}
						executedPlugins.add(pluginWorkflowNameMap.get(aPluginWorkflowName));
					}
				}
			}
			workflowDetail.setCurrentExecutingPlugin(currentExecutingPlugin);
			workflowDetail.setExecutedPluginList(executedPlugins);
			workflowDetail.setUnexecutedPluginList(unexecutedPlugins);
		}
	}

	/**
	 * Sets lists of workflow detail to default null values.
	 * 
	 * @param workflowDetail {@link WorkflowDetail}
	 */
	private void setListToDefaultValue(final WorkflowDetail workflowDetail) {
		workflowDetail.setCurrentExecutingModule(null);
		workflowDetail.setCurrentExecutingPlugin(null);
		workflowDetail.setExecutedPluginList(null);
		workflowDetail.setUnexecutedPluginList(null);
	}

	/**
	 * Sets lists of modules for the workflow detail of a batch instance.
	 * 
	 * @param moduleNames {@link Collection}<{@link String}>
	 * @param currentModuleName {@link String}
	 * @param workflowDetail {@link WorkflowDetail}
	 */
	private void setModuleList(final Collection<String> moduleNames, final String currentModuleName,
			final WorkflowDetail workflowDetail) {
		if (null != workflowDetail) {
			int moduleListSize = moduleNames.size();
			List<String> executedModules = null;
			List<String> unExecutedModules = null;
			if (CollectionUtils.isNotEmpty(moduleNames)) {
				if (EphesoftStringUtil.isNullOrEmpty(currentModuleName)) {
					unExecutedModules = new ArrayList<String>(moduleListSize);
					unExecutedModules.addAll(moduleNames);
				} else if (currentModuleName.equals(WorkflowConstants.WORKFLOW_UNIT_END_INDICATOR)) {
					executedModules = new ArrayList<String>(moduleListSize);
					executedModules.addAll(moduleNames);
				} else {
					executedModules = new ArrayList<String>(moduleListSize);
					unExecutedModules = new ArrayList<String>(moduleListSize);
					boolean currentModuleFound = false;
					for (String moduleName : moduleNames) {
						if (currentModuleFound) {
							unExecutedModules.add(moduleName);
						} else {
							if (currentModuleName.equalsIgnoreCase(moduleName)) {
								currentModuleFound = true;
							} else {
								executedModules.add(moduleName);
							}
						}
					}
				}
			}
			workflowDetail.setExecutedModuleList(executedModules);
			workflowDetail.setUnexecutedModuleList(unExecutedModules);
		}
	}

	/**
	 * Gets a map for module names with a list of plugin names for that module.
	 * 
	 * @param workflowModuleNameMap {@link Map}<{@link String}, {@link String}>
	 * @param workflowPluginMap {@link Map}<{@link String}, {@link Map}<{@link String}, {@link String}>
	 * @return {@link Map}<{@link String}, {@link String}>
	 */
	private Map<String, List<String>> getModulePluginMap(Map<String, String> workflowModuleNameMap,
			Map<String, Map<String, String>> workflowPluginMap) {
		Map<String, List<String>> modulePluginMap = null;
		if (null != workflowModuleNameMap && null != workflowPluginMap) {
			modulePluginMap = new HashMap<String, List<String>>(workflowModuleNameMap.size());
			Set<String> moduleWorkflowNames = workflowModuleNameMap.keySet();
			Iterator<String> moduleWorkflowNameIterator = moduleWorkflowNames.iterator();
			while (moduleWorkflowNameIterator.hasNext()) {
				String aModuleWorkflowName = moduleWorkflowNameIterator.next();
				Map<String, String> pluginMap = workflowPluginMap.get(aModuleWorkflowName);
				List<String> pluginNames = new ArrayList<String>();
				pluginNames.addAll(pluginMap.values());
				modulePluginMap.put(workflowModuleNameMap.get(aModuleWorkflowName), pluginNames);
			}
		}
		return modulePluginMap;
	}
}
