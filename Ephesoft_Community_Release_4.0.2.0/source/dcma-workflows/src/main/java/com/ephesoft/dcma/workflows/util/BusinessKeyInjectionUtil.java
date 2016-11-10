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

package com.ephesoft.dcma.workflows.util;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.repository.ProcessDefinition;

import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.common.WorkflowVariables;

/**
 * This class represents utility class for business key injection for the process instance of workflow for batches.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class BusinessKeyInjectionUtil {

	/***
	 * {@link EphesoftLogger} Instance of logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(BusinessKeyInjectionUtil.class);

	/**
	 * Gets the business key for a workflow.
	 * 
	 * @param exec {@link DelegateExecution}
	 * @param workflowName {@link String}
	 * @return {@link String}
	 * @throws Exception {@link Exception}
	 */
	public static String getBusinessKey(final DelegateExecution exec, final String workflowName) {
		LOGGER.trace("Entering method to updated business key for Batch.");
		final String uniqueBatchID = (String) exec.getVariable(WorkflowVariables.KEY);
		String businessKey = createBusinessKey(uniqueBatchID, workflowName);
		LOGGER.debug("Business key formed so far is: ", businessKey);
		return businessKey;
	}

	/**
	 * Creates a business key for a workflow.
	 * 
	 * @param uniqueBatchID {@link String}
	 * @param workflowName {@link String}
	 * @return {@link String}
	 */
	private static String createBusinessKey(final String uniqueBatchID, final String workflowName) {
		LOGGER.trace("Entering method to prepare business key for Batch.");
		LOGGER.debug("Batch instance id is: ", uniqueBatchID);
		String businessKey;
		if (null == uniqueBatchID) {
			businessKey = null;
			LOGGER.error("Batch Instance id is null.");
		} else if (null == workflowName) {
			businessKey = null;
			LOGGER.error("workflow name is null.");
		} else {
			businessKey = EphesoftStringUtil.concatenate(uniqueBatchID, ICommonConstants.DOT, workflowName);
		}
		return businessKey;
	}

	/**
	 * Gets workflow name for the execution.
	 * 
	 * @param exec {@link DelegateExecution}
	 * @return {@link String}
	 */
	public static String getWorkflowName(final DelegateExecution exec, final RepositoryService repositoryService) {
		String workflowName;
		final String processDefinitionID = exec.getProcessDefinitionId();
		if (null == processDefinitionID) {
			workflowName = null;
			LOGGER.error("Process defintion id is null.");
		} else {
			final ProcessDefinition processDefinition = repositoryService.getProcessDefinition(processDefinitionID);
			if (null == processDefinition) {
				workflowName = null;
				LOGGER.error("workflow name is null.");
			} else {
				workflowName = processDefinition.getKey();
				LOGGER.debug("Workflow name is: ", workflowName);
			}
		}
		return workflowName;
	}

	/**
	 * Updates the business key for the process isntance object.
	 * 
	 * @param exec {@link DelegateExecution}
	 * @param businessKey {@link String}
	 * @param postBusinessKeyString {@link String}
	 * @return {@link String}
	 */
	public static String updateBusinessKey(final DelegateExecution exec, final String businessKey, final String postBusinessKeyString) {
		final String newBusinessKey;
		if (null == businessKey || null == postBusinessKeyString) {
			newBusinessKey = null;
			LOGGER.error("The module business key could not be updated because business key is null.");
		} else {
			LOGGER.debug("Business key initial so far is: ", businessKey);
			newBusinessKey = EphesoftStringUtil.concatenate(businessKey, postBusinessKeyString);
			final ExecutionEntity thisEntity = (ExecutionEntity) exec;

			// This update propagates change of business key for the current process instance in all the tables.
			thisEntity.updateProcessBusinessKey(newBusinessKey);
		}
		return newBusinessKey;
	}
}
