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

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.common.WorkflowVariables;
import com.ephesoft.dcma.workflows.util.BusinessKeyInjectionUtil;

/**
 * This class represents the actions taken by the listener executed at the end of a module for a batch.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class ModuleExecutionEndListener implements ExecutionListener {

	/**
	 * Serial version of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link EphesoftLogger} Instance of logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ModuleExecutionStartListener.class);

	/**
	 * {@link RepositoryService} Instance of repository service.
	 */
	@Autowired
	RepositoryService repositoryService;

	/**
	 * {@link BatchSchemaService} Instance of batch schema service.
	 */
	@Autowired
	BatchSchemaService batchSchemaService;

	@Override
	public void notify(DelegateExecution delegateExecution) {
		LOGGER.trace("Module Execution Start Event Fired.");
		final String workflowName = BusinessKeyInjectionUtil.getWorkflowName(delegateExecution, repositoryService);
		Map<String, Object> variableMap = delegateExecution.getVariables();
		String batchInstanceIdentifier = (String) variableMap.get(WorkflowVariables.KEY);
		LOGGER.debug("Initiating module backup for batch instance ", batchInstanceIdentifier, " for module ", workflowName);

		// for creating a backup XML for module
		batchSchemaService.createBatchXml(batchInstanceIdentifier, workflowName);
	}

}
