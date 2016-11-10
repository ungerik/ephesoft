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
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.ImplementationType;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.ProcessDefinitionInfo;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

import static com.ephesoft.dcma.workflows.constant.WorkflowConstants.*;

import com.ephesoft.dcma.workflows.constant.WorkflowConstants;
import com.ephesoft.dcma.workflows.listener.ModuleExecutionStartListener;
import com.ephesoft.dcma.workflows.service.WorkflowCreationService;
import com.ephesoft.dcma.workflows.util.WorkflowCreationUtil;
import com.sun.star.lib.uno.environments.java.java_environment;

import org.activiti.bpmn.model.Process;
import org.apache.commons.collections.CollectionUtils;

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
public class WorkflowCreationServiceImpl implements WorkflowCreationService {

	/**
	 * {@link EphesoftLogger} Instance of logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(WorkflowCreationServiceImpl.class);

	/**
	 * Creates a BPMN XML string for a process
	 * 
	 * @param processDefinitionInfo {@link ProcessDefinitionInfo}
	 * @param processDefinitionList {@link List<{@link ProcessDefinitionInfo}>}
	 * @param isWorkflow
	 * @return {@link String}
	 */
	private String getProcessDefinitionString(final String processName, final List<ProcessDefinitionInfo> processDefinitionInfoList,
			final boolean isWorkflow) {
		String activityIdSuffix = isWorkflow ? ACTIVITY_ID_IN_MODULE : ACTIVITY_ID_IN_PLUGIN;
		BpmnModel model = new BpmnModel();
		Process process = WorkflowCreationUtil.createProcess(processName);
		LOGGER.info("Creating Start event for: ", processName);
		process.addFlowElement(WorkflowCreationUtil.createStartEvent(START_CONSTANT));

		// looping through the sub processes for a process
		if (CollectionUtils.isNotEmpty(processDefinitionInfoList)) {
			addInitialProcessDefinition(processDefinitionInfoList, isWorkflow, process);
			int i = 0;
			for (ProcessDefinitionInfo processInfo : processDefinitionInfoList) {
				CallActivity callActivity = WorkflowCreationUtil.createSubProcess(processInfo, true, activityIdSuffix);
				String subProcessName = EphesoftStringUtil.concatenate(processInfo.getSubProcessName(), activityIdSuffix);
				process.addFlowElement(callActivity);
				String nextSubProcess = findTransitionprocess(processDefinitionInfoList, i, activityIdSuffix);
				process.addFlowElement(WorkflowCreationUtil.createSequenceFlow(subProcessName, nextSubProcess, null, null));
				i++;
			}
		} else {

			// for a case where there is no sub-process(plugin or module) in a process definition
			process.addFlowElement(WorkflowCreationUtil.createSequenceFlow(START_CONSTANT, END_CONSTANT, null, null));
		}
		LOGGER.info("Creating end event for: ", processName);
		process.addFlowElement(WorkflowCreationUtil.createEndEvent(END_CONSTANT));
		model.addProcess(process);
		return new String(new BpmnXMLConverter().convertToXML(model));
	}

	/**
	 * Finds the next subprocess name in the BPMN sequence flow
	 * 
	 * @param processDefinitionInfoList {@link List<{@link ProcessDefinitionInfo}>}
	 * @param i
	 * @param activityIdSuffix {@link String}
	 * @return {@link String}
	 */
	private String findTransitionprocess(final List<ProcessDefinitionInfo> processDefinitionInfoList, final int i,
			final String activityIdSuffix) {
		int listSize = processDefinitionInfoList.size();
		int nextIndex = i + 1;
		String nextSubProcessName;
		if (nextIndex < listSize) {
			ProcessDefinitionInfo processDefinitionInfo = processDefinitionInfoList.get(nextIndex);
			String processName = processDefinitionInfo.getSubProcessName();
			nextSubProcessName = getNextSubProcess(processName, activityIdSuffix);
		} else {
			nextSubProcessName = END_CONSTANT;
		}
		LOGGER.info("Next process transition will be to: ", nextSubProcessName);
		return nextSubProcessName;
	}

	/**
	 * Finds the next subprocess name in the BPMN sequence flow
	 * 
	 * @param processName {@link String}
	 * @param activityIdSuffix {@link String}
	 * @return {@link String}
	 */
	private String getNextSubProcess(final String processName, final String activityIdSuffix) {
		String nextProcessName;
		if (processName.equalsIgnoreCase(REVIEW_DOCUMENT_PLUGIN)) {
			nextProcessName = IS_REVIEW_REQUIRED;
		} else if (processName.equalsIgnoreCase(VALIDATE_DOCUMENT_PLUGIN)) {
			nextProcessName = IS_VALIDATION_REQUIRED;
		} else {
			nextProcessName = EphesoftStringUtil.concatenate(processName, activityIdSuffix);
		}
		return nextProcessName;
	}

	/**
	 * Adds the initial elements to a Bpmn i.e. a resume decision in case of a batch class, a module execution listener and a yes-no
	 * decision in case of a module
	 * 
	 * @param processDefinitionList {@link List<{@link ProcessDefinitionInfo>}
	 * @param isWorkflow
	 * @param process {@link Process}
	 */
	private void addInitialProcessDefinition(final List<ProcessDefinitionInfo> processDefinitionList, final boolean isWorkflow,
			final Process process) {
		String firstSubProcessName;
		String processName = process.getId();
		if (isWorkflow) {

			// for adding resume decision handling to batch class process definition to facilitate batch restart from a particular
			// module
			LOGGER.info("Creating initial workflow elements for batch class: ", processName);
			firstSubProcessName = RESUME_EVALUATOR_TASK_NAME;
			addResumeDecision(process, RESUME_DECISION_CLASS, processDefinitionList);
		} else {
			LOGGER.info("Creating initial workflow elements for module: ", processName);
			firstSubProcessName = findFirstSubProcessName(processDefinitionList);
			LOGGER.info("Adding module execution listener to module definition: ", processName);
			ActivitiListener moduleStartListener = WorkflowCreationUtil.getExecutionListenerInstance(WorkflowConstants.START_CONSTANT,
					MODULE_EXECUTION_START_LISTENER_EXPRESSION, ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
			ActivitiListener moduleEndListener = WorkflowCreationUtil.getExecutionListenerInstance(WorkflowConstants.END_CONSTANT,
					MODULE_EXECUTION_END_LISTENER_EXPRESSION, ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
			process.getExecutionListeners().add(moduleStartListener);
			process.getExecutionListeners().add(moduleEndListener);
			addDecisionHandler(processDefinitionList, process);
		}
		process.addFlowElement(WorkflowCreationUtil.createSequenceFlow(START_CONSTANT, firstSubProcessName, null, null));
	}

	/**
	 * Adds a decision node to evaluate the module from which the workflow should resume.
	 * 
	 * @param process {@link Process}
	 * @param delegate {@link String}
	 * @param subProcessList {@link List<{@link ProcessDefinitionInfo}>}
	 */
	private void addResumeDecision(final Process process, final String delegate, final List<ProcessDefinitionInfo> subProcessList) {
		LOGGER.info("Creating resume decision for ", process.getId());
		process.addFlowElement(WorkflowCreationUtil.createServiceTask(RESUME_EVALUATOR_TASK_NAME,
				ImplementationType.IMPLEMENTATION_TYPE_CLASS, delegate));
		process.addFlowElement(WorkflowCreationUtil
				.createSequenceFlow(RESUME_EVALUATOR_TASK_NAME, RESUME_DECISION_GATEWAY, null, null));
		process.addFlowElement(WorkflowCreationUtil.createExclusiveGateway(RESUME_DECISION_GATEWAY));
		for (final ProcessDefinitionInfo proccessCreationInfo : subProcessList) {
			String subprocessName = proccessCreationInfo.getSubProcessName();
			String conditionString = WorkflowCreationUtil.createConditionString(RESUME_EVALUATION_RESULT_VARIABLE, subprocessName);
			process.addFlowElement(WorkflowCreationUtil.createSequenceFlow(RESUME_DECISION_GATEWAY,
					EphesoftStringUtil.concatenate(subprocessName, ACTIVITY_ID_IN_MODULE), conditionString, null));
		}
	}

	/**
	 * Adds a decision which evaluates to either yes or no
	 * 
	 * @param decisionType {@link String}
	 * @param process {@link Process}
	 * @param delegate {@link String}
	 * @param decisionToNo {@link String}
	 * @param decisionToYes {@link String}
	 */
	private void addYesNoDecision(final String decisionType, final Process process, final String delegate, final String decisionToNo,
			final String decisionToYes) {
		String conditionVariable;
		String decisionGatewayName;
		if (decisionType.equalsIgnoreCase(IS_REVIEW_REQUIRED)) {
			conditionVariable = REVIEW_EVALUATION_RESULT_VARIABLE;
			decisionGatewayName = REVIEW_DECISION_GATEWAY;
		} else {
			conditionVariable = VALIDATION_EVALUATION_RESULT_VARIABLE;
			decisionGatewayName = VALIDATION_DECISION_GATEWAY;
		}
		process.addFlowElement(WorkflowCreationUtil.createServiceTask(decisionType, ImplementationType.IMPLEMENTATION_TYPE_CLASS,
				delegate));
		process.addFlowElement(WorkflowCreationUtil.createSequenceFlow(decisionType, decisionGatewayName, null, null));
		process.addFlowElement(WorkflowCreationUtil.createExclusiveGateway(decisionGatewayName));
		String yesCondition = WorkflowCreationUtil.createConditionString(conditionVariable, YES);
		String noCondition = WorkflowCreationUtil.createConditionString(conditionVariable, NO);
		process.addFlowElement(WorkflowCreationUtil.createSequenceFlow(decisionGatewayName, decisionToYes, yesCondition, null));
		process.addFlowElement(WorkflowCreationUtil.createSequenceFlow(decisionGatewayName, decisionToNo, noCondition, null));
	}

	/**
	 * Adds a decision handler to a BPMN if a module contains either of REVIEW_DOCUMENT_PLUGIN or VALIDATE_DOCUMENT_PLUGIN
	 * 
	 * @param subProcessNameList {@link List<{@link ProcessDefinitionInfo}>}
	 * @param process {@link Process}
	 */
	private void addDecisionHandler(final List<ProcessDefinitionInfo> subProcessNameList, final Process process) {
		String decisionToNo;
		String decisionToYes;
		String decisionClass;
		String decisionType;
		int indexOfReview;
		String subProcessName;
		ProcessDefinitionInfo reviewPlugin = new ProcessDefinitionInfo(REVIEW_DOCUMENT_PLUGIN);
		ProcessDefinitionInfo validationPlugin = new ProcessDefinitionInfo(VALIDATE_DOCUMENT_PLUGIN);
		boolean isReviewPluginContained = subProcessNameList.contains(reviewPlugin);
		boolean isValidationPluginContained = subProcessNameList.contains(validationPlugin);

		// check if sub process list contains either of review or validation plugin
		if (isReviewPluginContained || isValidationPluginContained) {
			if (isReviewPluginContained) {
				LOGGER.info("The sub-process list for the process", process.getId(), " contains READY_FOR_REVIEW plugin");
				decisionType = IS_REVIEW_REQUIRED;
				decisionClass = REVIEW_DECISION_CLASS;
				indexOfReview = subProcessNameList.indexOf(reviewPlugin);
				subProcessName = REVIEW_DOCUMENT_PLUGIN;
			} else {
				LOGGER.info("The sub-process list for the process", process.getId(), " contains READY_FOR_VALIDATION plugin");
				decisionType = IS_VALIDATION_REQUIRED;
				decisionClass = VALIDATION_DECISION_CLASS;
				indexOfReview = subProcessNameList.indexOf(validationPlugin);
				subProcessName = VALIDATE_DOCUMENT_PLUGIN;
			}
			if (indexOfReview < subProcessNameList.size() - 1) {
				ProcessDefinitionInfo transitionProcess = subProcessNameList.get(indexOfReview + 1);
				decisionToNo = EphesoftStringUtil.concatenate(transitionProcess.getSubProcessName(), ACTIVITY_ID_IN_PLUGIN);
			} else {
				decisionToNo = END_CONSTANT;
			}
			decisionToYes = EphesoftStringUtil.concatenate(subProcessName, ACTIVITY_ID_IN_PLUGIN);
			addYesNoDecision(decisionType, process, decisionClass, decisionToNo, decisionToYes);
		}
	}

	/**
	 * Finds the name of the first sub process out of all the subprocesses
	 * 
	 * @param processList {@link List<{@link ProcessDefinitionInfo}>}
	 * @return {@link String}
	 */
	private String findFirstSubProcessName(final List<ProcessDefinitionInfo> processList) {
		ProcessDefinitionInfo processDefinition = processList.get(0);
		String firstSubProcessName = processDefinition.getSubProcessName();
		if (firstSubProcessName.equalsIgnoreCase(REVIEW_DOCUMENT_PLUGIN)) {
			firstSubProcessName = IS_REVIEW_REQUIRED;
		} else if (firstSubProcessName.equalsIgnoreCase(VALIDATE_DOCUMENT_PLUGIN)) {
			firstSubProcessName = IS_VALIDATION_REQUIRED;
		} else {
			firstSubProcessName = EphesoftStringUtil.concatenate(firstSubProcessName, ACTIVITY_ID_IN_PLUGIN);
		}
		LOGGER.info("The first node in the process definition is: ", firstSubProcessName);
		return firstSubProcessName;
	}

	@Override
	public String writeProcessDefinitions(final String workflowPath, final String processName,
			final List<ProcessDefinitionInfo> subProcessList, final boolean isWorkflow) throws DCMAException {
		String processDefinitionPath;
		FileWriter fstream = null;
		BufferedWriter out = null;
		try {
			String path = EphesoftStringUtil.concatenate(File.separator, processName, FileType.BPMN.getExtensionWithDot());
			processDefinitionPath = EphesoftStringUtil.concatenate(workflowPath, path);
			LOGGER.info("Writing process definition at path: ", processDefinitionPath);
			fstream = new FileWriter(processDefinitionPath);
			out = new BufferedWriter(fstream);
			String processDefinitionFileContent = getProcessDefinitionString(processName, subProcessList, isWorkflow);
			out.write(processDefinitionFileContent);
		} catch (final Exception exception) {
			LOGGER.error(exception, "Exception while generating process definition: ", processName, " ", exception.getMessage());
			throw new DCMAException("Exception while generating process definition");
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (fstream != null) {
					fstream.close();
				}
			} catch (final IOException ioException) {
				LOGGER.error(ioException, "IOException while creating process definition: ", ioException.getMessage());
			}
		}
		return processDefinitionPath;
	}
}
