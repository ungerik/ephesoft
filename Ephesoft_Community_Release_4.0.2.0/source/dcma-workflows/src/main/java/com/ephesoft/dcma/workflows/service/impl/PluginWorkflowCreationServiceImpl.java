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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.delegate.ExecutionListener;
import org.apache.commons.collections.CollectionUtils;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.PluginProcessCreationInfo;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.common.WorkflowVariables;

import static com.ephesoft.dcma.workflows.constant.WorkflowConstants.*;

import com.ephesoft.dcma.workflows.service.PluginWorkflowCreationService;
import com.ephesoft.dcma.workflows.util.WorkflowCreationUtil;

/**
 * This class represents a service to create a process definition for a plugin
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class PluginWorkflowCreationServiceImpl implements PluginWorkflowCreationService {

	/**
	 * {@link EphesoftLogger} Instance of EphesoftLogger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(PluginWorkflowCreationServiceImpl.class);
	private static final String SERVICE_TASK = "service_task";
	private static final String UNDERSCORE = "_";

	@Override
	public String writeProcessDefinitions(String workflowPath, String pluginProcessName,
			List<PluginProcessCreationInfo> subProcessNameList) throws DCMAException {
		String processDefinitionPath = ICommonConstants.EMPTY_STRING;
		String path = ICommonConstants.EMPTY_STRING;
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			{
				path = EphesoftStringUtil.concatenate(File.separator, pluginProcessName, FileType.BPMN.getExtensionWithDot());
				processDefinitionPath = EphesoftStringUtil.concatenate(workflowPath, path);
				LOGGER.info("Writing process definition at path: ", processDefinitionPath);
				fstream = new FileWriter(processDefinitionPath);
				out = new BufferedWriter(fstream);
				if (subProcessNameList != null) {
					String jpdlFileContent = getProcessDefinitionString(pluginProcessName, subProcessNameList);
					out.write(jpdlFileContent);
				}
			}
		} catch (final Exception exception) {
			LOGGER.error(exception, "Error while creating process definition for plugin: ", pluginProcessName, " ",
					exception.getMessage());
			throw new DCMAException("Exception while creating process definition for plugin");
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (fstream != null) {
					fstream.close();
				}
			} catch (IOException ioexception) {
				LOGGER.error(ioexception, "Error while creating process definition for ", pluginProcessName, " :",
						ioexception.getMessage());
			}
		}
		return path;
	}

	/**
	 * Creates delegate expression for a plugin service task
	 * 
	 * @param processDefinitionList {@link PluginProcessCreationInfo}>}
	 * @return {@link String}
	 */
	private String getDelegateExpression(PluginProcessCreationInfo serviceTaskInfo) {
		String argumentString = getMethodArgumentString(serviceTaskInfo.isScriptingPlugin());
		String delegateString = EphesoftStringUtil.concatenate(DOLLAR, CURLY_BRACES_BEGIN, serviceTaskInfo.getServiceName(), PERIOD,
				serviceTaskInfo.getMethodName(), BEGIN_ROUND_BRACKETS, argumentString, CLOSE_ROUND_BRACKETS, CURLY_BRACES_END);
		LOGGER.debug("The delegate string created is: ", delegateString);
		return delegateString;
	}

	/**
	 * Generates argument string for plugin service method
	 * 
	 * @param isScriptingPlugin
	 * @return {@link String}
	 */
	private String getMethodArgumentString(boolean isScriptingPlugin) {
		LOGGER.info("Entering method to create method argument string");
		String argumentString = EphesoftStringUtil.concatenate(WorkflowVariables.BATCH_INSTANCE_ID, COMMA, WorkflowVariables.KEY);
		if (isScriptingPlugin) {
			argumentString = EphesoftStringUtil.concatenate(argumentString, COMMA, BACKUP_FILE_NAME_VARIABLE, COMMA,
					SCRIPT_NAME_VARIABLE);
		}
		LOGGER.debug("Argument string created is: ", argumentString);
		return argumentString;
	}

	/**
	 * Finds the next subprocess name in the BPMN sequence flow
	 * 
	 * @param processDefinitionList {@link List<{@link PluginProcessCreationInfo}>}
	 * @param i
	 * @return {@link String}
	 */
	private String findTransitionprocess(final List<PluginProcessCreationInfo> processDefinitionInfoList, final int i) {
		LOGGER.info("Entering method to get the transition process");
		final int listSize = processDefinitionInfoList.size();
		final int nextIndex = i + 1;
		String nextSubProcessName;
		if (nextIndex < listSize) {
			final PluginProcessCreationInfo processDefinitionInfo = processDefinitionInfoList.get(nextIndex);
			nextSubProcessName = EphesoftStringUtil.concatenate(processDefinitionInfo.getMethodName(), UNDERSCORE, SERVICE_TASK);
		} else {
			nextSubProcessName = END_CONSTANT;
		}
		LOGGER.debug("The next transition will to process: ", nextSubProcessName);
		return nextSubProcessName;
	}

	/**
	 * Creates a BPMN XML string for a plugin process
	 * 
	 * @param processDefinitionName {@link String}
	 * @param processDefinitionList {@link List<{@link PluginProcessCreationInfo}>}
	 * @return {@link String}
	 */
	private String getProcessDefinitionString(final String processDefinitionName,
			final List<PluginProcessCreationInfo> processDefinitionList) {
		final BpmnModel model = new BpmnModel();
		final Process process = WorkflowCreationUtil.createProcess(processDefinitionName);
		ActivitiListener pluginActivitiListener = WorkflowCreationUtil.getExecutionListenerInstance(START_CONSTANT,
				PLUGIN_EXECUTION_LISTENER_EXPRESSION, ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
		process.getExecutionListeners().add(pluginActivitiListener);
		LOGGER.info("Creating Start event for: ", processDefinitionName);
		process.addFlowElement(WorkflowCreationUtil.createStartEvent(START_CONSTANT));
		if (CollectionUtils.isNotEmpty(processDefinitionList)) {
			int i = 0;
			for (final PluginProcessCreationInfo processInfo : processDefinitionList) {
				String serviceTaskID = EphesoftStringUtil.concatenate(processInfo.getMethodName(), UNDERSCORE, SERVICE_TASK);
				process.addFlowElement(WorkflowCreationUtil.createServiceTask(serviceTaskID,
						ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION, getDelegateExpression(processInfo)));
				final String nextSubProcess = findTransitionprocess(processDefinitionList, i);
				process.addFlowElement(WorkflowCreationUtil.createSequenceFlow(serviceTaskID, nextSubProcess, null, null));
				i++;
			}
		} else {
			process.addFlowElement(WorkflowCreationUtil.createSequenceFlow(START_CONSTANT, END_CONSTANT, null, null));
		}
		LOGGER.info("Creating end event for: ", processDefinitionName);
		process.addFlowElement(WorkflowCreationUtil.createEndEvent(END_CONSTANT));
		model.addProcess(process);
		return new String(new BpmnXMLConverter().convertToXML(model));
	}

}
