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

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.IOParameter;
import org.activiti.bpmn.model.ImplementationType;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.StartEvent;

import com.ephesoft.dcma.core.common.ProcessDefinitionInfo;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.common.WorkflowVariables;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;

/**
 * Represents a utility class to create various BPMN constructs.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class WorkflowCreationUtil {

	private WorkflowCreationUtil() {
	}

	/**
	 * {@link EphesoftLogger} Instance of logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(WorkflowCreationUtil.class);

	/**
	 * Creates a Process
	 * 
	 * @param processName {@link String}
	 * @return {@link Process}
	 */
	public static Process createProcess(final String processName) {
		final Process process = new Process();
		process.setId(processName);
		process.setName(processName);
		return process;
	}

	/**
	 * Creates a BPMN StartEvent
	 * 
	 * @param eventId {@link String}
	 * @return {@link StartEvent}
	 */
	public static StartEvent createStartEvent(final String eventId) {
		final StartEvent startEvent = new StartEvent();
		startEvent.setId(eventId);
		return startEvent;
	}

	/**
	 * Creates an EndEvent
	 * 
	 * @param eventId {@link String}
	 * @return {@link EndEvent}
	 */
	public static EndEvent createEndEvent(final String eventId) {
		final EndEvent endEvent = new EndEvent();
		endEvent.setId(eventId);
		return endEvent;
	}

	/**
	 * Creates a SequenceFlow
	 * 
	 * @param from {@link String}
	 * @param to {@link String}
	 * @param condition {@link String}
	 * @param activityIdSuffix {@link String}
	 * @return {@link SequenceFlow}
	 */
	public static SequenceFlow createSequenceFlow(final String from, final String to, final String condition,
			final String activityIdSuffix) {
		final SequenceFlow flow = new SequenceFlow(from, to);
		if (condition != null) {
			flow.setConditionExpression(condition);
		}
		return flow;
	}

	/**
	 * Creates a ServiceTask
	 * 
	 * @param identifier {@link String}
	 * @param implementationType {@link String}
	 * @param delegate {@link String}
	 * @return {@link ServiceTask}
	 */
	public static ServiceTask createServiceTask(final String identifier, final String implementationType,
			final String delegateExpression) {
		final ServiceTask serviceTask = new ServiceTask();
		serviceTask.setId(identifier);
		serviceTask.setImplementationType(implementationType);
		serviceTask.setImplementation(delegateExpression);
		return serviceTask;
	}

	/**
	 * Creates an Exclusive Gateway
	 * 
	 * @param indentifier {@link String}
	 * @return {@link ExclusiveGateway}
	 */
	public static ExclusiveGateway createExclusiveGateway(final String indentifier) {
		LOGGER.info("Creating exclusive gateway with id: ", indentifier);
		final ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
		exclusiveGateway.setId(indentifier);
		return exclusiveGateway;
	}

	/**
	 * Creates a list of IOParameters
	 * 
	 * @param subProcessInfo {@link ProcessDefinitionInfo}
	 * @return {@link List{@link IOParameter}>}
	 */
	public static List<IOParameter> createIOParameterList(final ProcessDefinitionInfo subProcessInfo) {
		LOGGER.info("Creating parameter list for process definition");
		final List<IOParameter> parameterList = new ArrayList<IOParameter>();
		parameterList.add(createIOParameter(WorkflowVariables.BATCH_INSTANCE_ID, WorkflowVariables.BATCH_INSTANCE_ID, null));
		parameterList.add(createIOParameter(WorkflowVariables.KEY, WorkflowVariables.KEY, null));
		if (null != subProcessInfo && subProcessInfo.isScriptingPlugin()) {
			LOGGER.info("The sub process is a scripting plugin");
			parameterList.add(createIOParameter(null, WorkflowConstants.SCRIPT_NAME_VARIABLE, subProcessInfo.getScriptingFileName()));
			parameterList
					.add(createIOParameter(null, WorkflowConstants.BACKUP_FILE_NAME_VARIABLE, subProcessInfo.getBackUpFileName()));
		}
		return parameterList;
	}

	/**
	 * Creates an IOParameter
	 * 
	 * @param source {@link String}
	 * @param target {@link String}
	 * @param expression {@link String}
	 * @return {@link IOParameter}
	 */
	public static IOParameter createIOParameter(final String source, final String target, final String expression) {
		LOGGER.info("Creating IO parameter: ", target);
		final IOParameter parameter = new IOParameter();
		if (source != null) {
			parameter.setSource(source);
		}
		if (expression != null) {
			parameter.setSourceExpression(expression);
		}
		parameter.setTarget(target);
		return parameter;
	}

	/**
	 * Creates a CallActiviti element
	 * 
	 * @param subProcess {@link ProcessDefinitionInfo}
	 * @param async
	 * @param activityIdSuffix {@link String}
	 * @return {@link CallActivity}
	 */
	public static CallActivity createSubProcess(final ProcessDefinitionInfo subProcess, final boolean async,
			final String activityIdSuffix) {
		LOGGER.info("Creating sub process ", subProcess.getSubProcessName());
		final String identifier = EphesoftStringUtil.concatenate(subProcess.getSubProcessName(), activityIdSuffix);
		final CallActivity callActivity = new CallActivity();
		callActivity.setId(identifier);
		callActivity.setCalledElement(subProcess.getSubProcessKey());
		callActivity.setAsynchronous(async);
		callActivity.setInParameters(WorkflowCreationUtil.createIOParameterList(subProcess));
		return callActivity;
	}

	/**
	 * Creates a listener
	 * 
	 * @param event  {@link String}
	 * @param implementation {@link String}
	 * @param implementationType  {@link String}
	 * @return {@link ActivitiListener}
	 */
	public static ActivitiListener getExecutionListenerInstance(final String event, final String implementation, final String implementationType) {
		final ActivitiListener listener = new ActivitiListener();
		listener.setImplementationType(implementationType);
		listener.setImplementation(implementation);
		listener.setEvent(event);
		return listener;

	}

	/**
	 * Create a condition string
	 * 
	 * @param conditionVar {@link String}
	 * @param value {@link String}
	 * @return {@link String}
	 */
	public static String createConditionString(final String conditionVar, final String value) {
		final String conditionString = EphesoftStringUtil.concatenate(WorkflowConstants.DOLLAR, WorkflowConstants.CURLY_BRACES_BEGIN,
				conditionVar, WorkflowConstants.CHECK_EQUALITY, WorkflowConstants.SINGLE_QUOTE, value, WorkflowConstants.SINGLE_QUOTE,
				WorkflowConstants.CURLY_BRACES_END);
		return conditionString;
	}
}
