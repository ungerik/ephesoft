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

package com.ephesoft.dcma.workflows.listener;

import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchClassModuleService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.common.WorkflowVariables;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;
import com.ephesoft.dcma.workflows.util.BusinessKeyInjectionUtil;

/**
 * This class represents the actions taken by the listener executed at the start of a module for a batch.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class ModuleExecutionStartListener implements ExecutionListener {

	/**
	 * Serial version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link EphesoftLogger} Instance of logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ModuleExecutionStartListener.class);

	/**
	 * {@link BatchInstanceService} Instance of batch instance service.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * {@link BatchClassModuleService} Instance of batch class module service.
	 */
	@Autowired
	private BatchClassModuleService batchClassModuleService;

	/**
	 * {@link RepositoryService} Instance of repository service.
	 */
	@Autowired
	RepositoryService repositoryService;

	/**
	 * Executes steps for listener of the module start for a batch.
	 */
	@Override
	public void notify(final DelegateExecution delegateExecution) throws Exception {
		final String workflowName = BusinessKeyInjectionUtil.getWorkflowName(delegateExecution, repositoryService);
		final String businessKey = BusinessKeyInjectionUtil.getBusinessKey(delegateExecution, workflowName);
		String moduleBusinessKey = BusinessKeyInjectionUtil.updateBusinessKey(delegateExecution, businessKey,
				WorkflowConstants.MODULE_BUSINESS_KEY_SIGNATURE);
		LOGGER.debug("Updated module business key is: ", moduleBusinessKey);
		LOGGER.info("Successfully Updated business key for Batch.");
		updateExecutedModules(delegateExecution, workflowName);
	}

	/**
	 * Updates executed modules for a batch.
	 * 
	 * @param delegateExecution {@link DelegateExecution}
	 * @param workflowName {@link String}
	 */
	private void updateExecutedModules(final DelegateExecution delegateExecution, final String workflowName) {
		LOGGER.trace("Module Execution Start Event Fired.");
		LOGGER.debug("Module Name (Execution's activity name): ", workflowName);
		BatchInstance batchInstance = getBatchInstance(delegateExecution);
		BatchClass batchClass = batchInstance.getBatchClass();
		String moduleId;
		if (null == batchClass) {
			moduleId = ICommonConstants.EMPTY_STRING;
		} else {
			moduleId = getModuleId(batchClass.getIdentifier(), workflowName);
		}
		if (null == moduleId) {
			moduleId = ICommonConstants.EMPTY_STRING;
		}
		String executedModulesString = batchInstance.getExecutedModules();
		LOGGER.debug("Non updated executed modules is: ", executedModulesString);
		if (null == executedModulesString) {
			executedModulesString = ICommonConstants.EMPTY_STRING;
		}
		if (isStringUpdated(executedModulesString, moduleId)) {
			LOGGER.info("Executed modules id is already updated.");
		} else {
			LOGGER.info("Executed modules id is getting updated.");
			updateExecutedModuleId(batchInstance, executedModulesString, moduleId);
		}
		LOGGER.trace("Module Execution Start Event Ends.");
	}

	/**
	 * Update the executed module identifier for the given batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param executedModuleIdString {@link String}
	 * @param moduleId {@link String}
	 */
	private void updateExecutedModuleId(final BatchInstance batchInstance, String executedModuleIdString, final String moduleId) {
		if (null == batchInstance) {
			LOGGER.error("BatchInstance object is null. The batch executed modules acnnot be updated.");
		} else {
			String batchInstanceIdentifier = batchInstance.getIdentifier();
			String updatedExecutedModuleIdString = EphesoftStringUtil.concatenate(executedModuleIdString, moduleId);
			LOGGER.debug("For batch: ", batchInstanceIdentifier, ",  to be updated executed modules is: ",
					updatedExecutedModuleIdString);
			batchInstance.setExecutedModules(updatedExecutedModuleIdString);
			batchInstanceService.updateBatchInstance(batchInstance);
			LOGGER.info("Executed modules updated for the batch: ", batchInstanceIdentifier);
		}
	}

	/**
	 * Get the module id for the batch class module.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param workflowName {@link String}
	 * @return {@link String}
	 */
	private String getModuleId(final String batchClassIdentifier, final String workflowName) {
		String moduleId;
		long id = batchClassModuleService.getModuleIdByWorkflowName(batchClassIdentifier, workflowName);
		if (0 == id) {
			moduleId = ICommonConstants.EMPTY_STRING;
		} else {
			moduleId = EphesoftStringUtil.concatenate(id, ICommonConstants.SEMI_COLON);
		}
		LOGGER.debug("Module id is: ", moduleId, " for workflow name: ", workflowName, "for batch class: ", batchClassIdentifier);
		return moduleId;
	}

	/**
	 * Gets batch instance from execution object.
	 * 
	 * @param delegateExecution {@link DelegateExecution}
	 * @return {@link BatchInstance}
	 */
	private BatchInstance getBatchInstance(final DelegateExecution delegateExecution) {
		Map<String, Object> variableMap = delegateExecution.getVariables();
		String batchInstanceIdentifier = (String) variableMap.get(WorkflowVariables.KEY);
		return batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
	}

	/**
	 * Check if stringContainer is updated with new string.
	 * 
	 * @param stringContainer
	 * @param string {@link String}
	 * @return boolean true id module id is updated.
	 */
	private boolean isStringUpdated(final String stringContainer, final String string) {
		boolean isContained = stringContainer.endsWith(string);
		return isContained;
	}
}
