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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.batch.service.WorkflowDetailService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.constants.CommonConstants;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.BatchInstanceErrorDetails;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceErrorDetailsService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ServerRegistryService;
import com.ephesoft.dcma.mail.service.MailService;
import com.ephesoft.dcma.util.BackUpFileService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.common.WorkflowVariables;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;
import com.ephesoft.dcma.workflows.service.PickUpService;
import com.ephesoft.dcma.workflows.service.RestartBatchService;
import com.ephesoft.dcma.workflows.service.WorkflowService;
import com.ephesoft.dcma.workflows.service.engine.comparator.HistoricProcessComparator;
import com.ephesoft.dcma.workflows.util.WorkflowUtil;

/**
 * This class presents an implementation of workflow Service with use of a third party engine.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class WorkflowServiceImpl implements WorkflowService {

	/**
	 * {@link EphesoftLogger} Instance of logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(WorkflowServiceImpl.class);

	/**
	 * {@link RuntimeService} Instance of runtime service.
	 */
	@Autowired
	private RuntimeService runtimeService;

	/**
	 * {@link BatchInstanceService} Instance of batch instance service.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * {@link PickUpService} Instance of pick up service.
	 */
	@Autowired
	private PickUpService pickupService;

	/**
	 * The service for CRUD operation on server_registry table in Ephesoft Database. {@link ServerRegistry}
	 */
	@Autowired
	private ServerRegistryService serverRegistryService;

	/**
	 * {@link BatchInstanceErrorDetailsService} Instance of batch instance error service.
	 */
	@Autowired
	private BatchInstanceErrorDetailsService batchInstanceErrorService;

	/**
	 * {@link BatchSchemaService} Instance of batch schema service.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * {@link String} Instance of subject of error batch mail.
	 */
	private String subjectErrorMail;

	/**
	 * {@link String}
	 */
	private String mailTemplatePath;

	@Autowired
	private RestartBatchService restartService;

	@Autowired
	private BatchInstanceDao batchInstanceDao;

	/**
	 * 
	 */
	@Autowired
	private ErrorBatchServiceRunnable errorBatchServiceRunnable;

	/**
	 * 
	 */
	@Autowired
	private HistoryService historyService;

	@Autowired
	private WorkflowDetailService workflowDetailService;

	/**
	 * {@link PluginPropertiesService} Instance of plugin properties service.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Gets subject error mail.
	 * 
	 * @return {@link String}
	 */
	public String getSubjectErrorMail() {
		return subjectErrorMail;
	}

	/**
	 * Sets subject error mail.
	 * 
	 * @param subject {@link String}
	 */
	public void setSubjectErrorMail(final String subjectErrorMail) {
		this.subjectErrorMail = subjectErrorMail;
	}

	/**
	 * Gets mail template path.
	 * 
	 * @return {@link String}
	 */
	public String getMailTemplatePath() {
		return mailTemplatePath;
	}

	/**
	 * Sets mail template path.
	 * 
	 * @param mailTemplatePath {@link String}
	 */
	public void setMailTemplatePath(String mailTemplatePath) {
		this.mailTemplatePath = mailTemplatePath;
	}

	@Override
	public boolean startWorkflow(BatchInstanceID batchInstanceID, String moduleName) {
		boolean isWorkflowStarted;
		String batchIdentifier = batchInstanceID.getID();
		LOGGER.info("Starting Workflow for batch instance id:", batchIdentifier, " for module name:", moduleName);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceID.getID());
		isWorkflowStarted = triggerBatchExecution(batchInstanceID, moduleName, batchIdentifier, batchInstance);
		return isWorkflowStarted;
	}

	@Override
	public void mailOnError(final BatchInstance batchInstance, final Exception exception) throws DCMAApplicationException {
		LOGGER.trace("Entering method to call mail service to send mail on batch error.");
		if (null != batchInstance && null != exception) {
			MailService mailService = EphesoftContext.get(MailService.class);
			String activeWorkflowName = getErrorBatchLastWorkflowInExecutionName(batchInstance.getIdentifier());
			mailService.mailOnWorkflowError(batchInstance, exception, subjectErrorMail, mailTemplatePath, activeWorkflowName);
		} else {
			LOGGER.error("Either or both batchInstance or exception object is null. Error notification mail can't be sent.");
		}
	}

	@Override
	public boolean signalWorkflow(BatchInstance batchInstance) throws DCMAApplicationException {
		boolean isWorkflowStarted;
		String batchIdentifier = batchInstance.getIdentifier();
		LOGGER.info("Signal Workflow for batch instance id:", batchIdentifier);
		final List<Execution> executions = getExecutionsForBatch(batchIdentifier);
		if (CollectionUtils.isNotEmpty(executions)) {
			final String waitingExecutionId = getWaitingExecutionId(executions);
			if (null == waitingExecutionId) {
				LOGGER.info("No active execution found for batch: ", batchIdentifier, ".");
				throw new DCMAApplicationException(EphesoftStringUtil.concatenate("Batch instance: ", batchIdentifier,
						"as there is no active execution found. Jobs are in inconsistent state."));
			} else {
				isWorkflowStarted = signalExecution(batchIdentifier, waitingExecutionId);
			}
		} else {
			isWorkflowStarted = startWorkflow(batchInstance.getBatchInstanceID(), null);
		}
		return isWorkflowStarted;
	}

	@Override
	public String getWorkflowNameByModuleName(final String moduleName, final List<BatchClassModule> batchClassModuleList) {
		String workflowName = null;
		if (!EphesoftStringUtil.isNullOrEmpty(moduleName) && CollectionUtils.isNotEmpty(batchClassModuleList)) {
			for (BatchClassModule batchClassModule : batchClassModuleList) {
				if (null != batchClassModule) {
					Module module = batchClassModule.getModule();
					if (null != module && moduleName.equalsIgnoreCase(module.getDescription())) {
						workflowName = batchClassModule.getWorkflowName();
						break;
					}
				}
			}

		}
		return workflowName;
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public void signalWorkflow(String batchId) throws DCMAApplicationException {
		LOGGER.debug("Signal workflow for batch instance id:", batchId);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchId);
		boolean isValidPauseStateStatus = WorkflowUtil.checkPauseStatus(batchInstance);
		if (isValidPauseStateStatus) {
			reloadBatchAndBackupXML(batchInstance);

			// Move batch to READY state always.
			setPauseStateBatchToReady(batchInstance);
		}
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	@Override
	public void signalWorkflow(String batchInstanceIdentifier, String userName) throws DCMAApplicationException {
		LOGGER.debug("Signal workflow for batch instance id:", batchInstanceIdentifier);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		boolean isValidPauseStateStatus = WorkflowUtil.checkPauseStatus(batchInstance);
		if (isValidPauseStateStatus) {
			switch (batchInstance.getStatus()) {
				case READY_FOR_REVIEW:
					batchInstance.setReviewUserName(userName);
					break;
				case READY_FOR_VALIDATION:
					batchInstance.setValidationUserName(userName);
					break;
			}
			this.batchInstanceService.updateBatchInstance(batchInstance);
			reloadBatchAndBackupXML(batchInstance);

			// Move batch to READY state always.
			setPauseStateBatchToReady(batchInstance);
		}
	}

	@Override
	public Map<Integer, Integer> getBatchPriorityRunningCount() {
		Map<Integer, Integer> priorityWithRunningBatchCount = null;
		List<BatchInstance> batchInstanceListForAllServer = batchInstanceService.getAllRunningBatches();
		if (CollectionUtils.isNotEmpty(batchInstanceListForAllServer)) {
			priorityWithRunningBatchCount = new HashMap<Integer, Integer>();
			for (BatchInstance batchInstance : batchInstanceListForAllServer) {
				int batchPriority = batchInstance.getPriority();
				Integer executionCount = priorityWithRunningBatchCount.get(batchPriority);
				if (null == executionCount) {
					priorityWithRunningBatchCount.put(batchPriority, 1);
				} else {
					executionCount++;
					priorityWithRunningBatchCount.put(batchPriority, executionCount);
				}
			}
		}
		return priorityWithRunningBatchCount;
	}

	@Override
	public int getRemainingCapacityForPriority(final int priority, final Map<Set<Integer>, Integer> priorityServerMaxCapacity,
			final Map<Integer, Integer> serverRunningBatchCount) {
		int capacity = 0;
		if (null == priorityServerMaxCapacity || 0 == priorityServerMaxCapacity.size()) {
			LOGGER.error("Priority range for no server is found.");
		} else {
			LOGGER.info("Getting remaining capacity for all servers for priority: ", priority);
			Set<Set<Integer>> setOfPrioritySets = priorityServerMaxCapacity.keySet();
			Iterator<Set<Integer>> prioritySetIterator = setOfPrioritySets.iterator();
			while (prioritySetIterator.hasNext()) {
				Set<Integer> setOfPriority = (Set<Integer>) prioritySetIterator.next();
				if (CollectionUtils.isNotEmpty(setOfPriority) && setOfPriority.contains(priority)) {
					capacity = getCapacityForPriority(priority, priorityServerMaxCapacity, serverRunningBatchCount, setOfPriority);
					break;
				}
			}
		}
		LOGGER.debug("Remaining capacity for priority: ", priority, " is: ", capacity);
		return capacity;
	}

	// @Override
	public int getRemainingCapacityForPriority(final int batchPriority, final int totalCapacity,
			final Map<Integer, Integer> serverRunningBatchCount) {
		int capacity = 0;
		if (null != serverRunningBatchCount) {
			Integer aPriorityRunningBatchCountInteger = serverRunningBatchCount.get(batchPriority);
			int aPriorityRunningBatchCount;
			if (null == aPriorityRunningBatchCountInteger) {
				aPriorityRunningBatchCount = 0;
			} else {
				aPriorityRunningBatchCount = aPriorityRunningBatchCountInteger;
			}
			LOGGER.debug("Total running capacity for a set containing priority: ", batchPriority, " is: ", aPriorityRunningBatchCount);
			capacity = totalCapacity - aPriorityRunningBatchCount;
			LOGGER.debug("Remaining capacity for priority: ", batchPriority, " is: ", capacity);
		}
		return capacity;
	}

	private int getCapacityForPriority(final int priority, final Map<Set<Integer>, Integer> priorityServerMaxCapacity,
			final Map<Integer, Integer> serverRunningBatchCount, Set<Integer> setOfPriority) {
		int capacity;
		if (null == priorityServerMaxCapacity || 0 == priorityServerMaxCapacity.size() || CollectionUtils.isEmpty(setOfPriority)) {
			capacity = 0;
		} else {
			Iterator<Integer> priorityIterator = setOfPriority.iterator();
			int totalCapacity = priorityServerMaxCapacity.get(setOfPriority);
			int totalPriorityBatchCount = 0;
			if (null != serverRunningBatchCount && !serverRunningBatchCount.isEmpty()) {
				while (priorityIterator.hasNext()) {
					Integer aPriority = priorityIterator.next();
					Integer aPriorityRunningBatchCountInteger = serverRunningBatchCount.get(aPriority);
					int aPriorityRunningBatchCount;
					if (null == aPriorityRunningBatchCountInteger) {
						aPriorityRunningBatchCount = 0;
					} else {
						aPriorityRunningBatchCount = aPriorityRunningBatchCountInteger;
					}
					totalPriorityBatchCount += aPriorityRunningBatchCount;
				}
				LOGGER.debug("Total running capacity for a set containing priority: ", priority, " is: ", totalPriorityBatchCount);
			}
			capacity = totalCapacity - totalPriorityBatchCount;
		}
		return capacity;
	}

	private void reloadBatchAndBackupXML(BatchInstance batchInstance) {
		LOGGER.trace("Entering method reloadBatchAndBackupXML");
		String backupXmlName = ICommonConstants.EMPTY_STRING;
		BatchInstanceStatus status = batchInstance.getStatus();
		if (status == BatchInstanceStatus.READY_FOR_REVIEW) {
			backupXmlName = WorkflowConstants.POST_REVIEW_BACKUP_NAME;
		} else if (status == BatchInstanceStatus.READY_FOR_VALIDATION) {
			backupXmlName = WorkflowConstants.POST_VALIDATION_BACKUP_NAME;
		}
		LOGGER.info("Creating backup XML ", backupXmlName, " for batch instance: ", batchInstance.getIdentifier());
		String batchIdentifier = batchInstance.getIdentifier();
		Batch batch = batchSchemaService.getBatchFromXML(batchIdentifier, true);
		batchSchemaService.updateBatch(batch);
		BackUpFileService.backUpBatchXml(batchInstance.getIdentifier(), backupXmlName);
	}

	@Override
	public void updateBatchInstanceStatusForReviewAndValidation(final BatchInstanceID batchInstanceID, final BatchInstanceStatus status) {
		LOGGER.trace("Entering method to update batch instance status");
		String batchInstanceIdentifier = batchInstanceID.getID();
		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
		if (null == batch) {
			LOGGER.info("No in memory batch found for batch instance: ", batchInstanceIdentifier);
			throw new DCMABusinessException(EphesoftStringUtil.concatenate("No In-memory  batch found for batch instance: ",
					batchInstanceIdentifier));
		}

		// for updating the batch XML and create a copy for the same
		batchSchemaService.updateBatchXML(batch);
		String backupXmlName;
		if (status == BatchInstanceStatus.READY_FOR_REVIEW) {
			backupXmlName = WorkflowConstants.PRE_REVIEW_BACKUP_NAME;
		} else {
			backupXmlName = WorkflowConstants.PRE_VALIDATION_BACKUP_NAME;
		}
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		batchInstance.setStatus(status);
		batchInstance.setExecutingServer(null);
		batchInstance.setServerIP(null);
		BackUpFileService.backUpBatchXml(batch.getBatchInstanceIdentifier(), backupXmlName);
		batchSchemaService.removeBatch(batchInstanceIdentifier);
		pluginPropertiesService.clearCache(batchInstanceIdentifier);
		batchInstanceService.merge(batchInstance);
	}

	@Override
	public void clearCurrentUser(final BatchInstance batchInstance) {
		batchInstance.setCurrentUser(null);
		batchInstanceService.merge(batchInstance);
	}

	/**
	 * Signals execution for a batch in a waiting execution state.
	 * 
	 * @param batchInstanceId {@link String}
	 * @param waitingExecutionId {@link String}
	 * @return boolean
	 */
	private boolean signalExecution(final String batchInstanceId, final String waitingExecutionId) {
		batchInstanceService.updateBatchInstanceStatusByIdentifier(batchInstanceId, BatchInstanceStatus.RUNNING);
		runtimeService.signal(waitingExecutionId);
		return true;
	}

	/**
	 * Gets all execution for batch instance.
	 * 
	 * @param batchInstanceId {@link String}
	 * @return {@link List}<{@link Execution}>
	 */
	private List<Execution> getExecutionsForBatch(final String batchInstanceId) {
		List<Execution> executions = runtimeService.createExecutionQuery()
				.processVariableValueEquals(WorkflowVariables.KEY, batchInstanceId).list();
		return executions;
	}

	/**
	 * Sets Paused batch to READY status.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	private void setPauseStateBatchToReady(final BatchInstance batchInstance) {
		batchInstance.setStatus(BatchInstanceStatus.READY);
		batchInstance.setCurrentUser(null);
		batchInstanceService.updateBatchInstance(batchInstance);
	}

	/**
	 * Gets execution id of a batch in a wait state.
	 * 
	 * @param executions {@link List}<{@link Execution}
	 * @return {@link String}
	 */
	private String getWaitingExecutionId(final List<Execution> executions) {
		Map<String, String> map = getExecutionHeirarchy(executions);
		String temporaryInstanceId = map.get(null);
		String waitingExecutionId = null;
		while (null != temporaryInstanceId) {
			waitingExecutionId = temporaryInstanceId;
			temporaryInstanceId = map.get(temporaryInstanceId);
		}
		return waitingExecutionId;
	}

	/**
	 * Gets hierarchy of executions for a batch instance.
	 * 
	 * @param executions {@link List}<{@link Execution}>
	 * @return {@link Map}<{@link String},{@link String}>
	 */
	private Map<String, String> getExecutionHeirarchy(final List<Execution> executions) {
		Map<String, String> map = new HashMap<String, String>(executions.size());
		for (Execution execution : executions) {
			ExecutionEntity executionEntity = (ExecutionEntity) execution;
			String superExecution = executionEntity.getSuperExecutionId();
			String parentExecution = executionEntity.getParentId();
			if (null != superExecution) {
				map.put(superExecution, executionEntity.getId());
			} else if (null != parentExecution) {
				map.put(parentExecution, executionEntity.getId());
			} else {
				map.put(null, executionEntity.getId());
			}
		}
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String getActiveModuleName(final String batchInstanceId) {
		LOGGER.debug("Get active module for batch instance id:", batchInstanceId);
		String returnValue = null;
		List<Execution> executions = getExecutionsForBatch(batchInstanceId);

		if (CollectionUtils.isNotEmpty(executions)) {
			// The activity's process definition contains the name of the module in execution.
			for (Execution execution : executions) {
				ProcessInstance processInstance = ((ProcessInstance) execution);
				if (null != processInstance) {
					String activity = processInstance.getActivityId();
					if (null != activity && activity.contains(WorkflowConstants.ACTIVITY_ID_IN_PLUGIN)) {
						String processDefintionId = processInstance.getProcessDefinitionId();
						int index = processDefintionId.indexOf(ICommonConstants.COLON);
						if (index > 0) {
							returnValue = processDefintionId.substring(0, index);
						}
						break;
					}
				}
			}
		}
		LOGGER.debug("Active module workflow for batch instance id: ", batchInstanceId, " is: ", returnValue);
		return returnValue;
	}

	@Override
	public String getActivePluginWorkflow(final String batchInstanceId) {
		LOGGER.debug("Get active plugin workflow for batch instance id:", batchInstanceId);
		String activePluginName = null;
		List<Execution> executions = getExecutionsForBatch(batchInstanceId);
		if (null != executions) {

			// The activity contains the name of the plugin in execution.
			for (Execution execution : executions) {
				if (null != execution) {
					ProcessInstance processInstance = ((ProcessInstance) execution);
					if (null != processInstance) {
						String activityId = processInstance.getActivityId();
						if (null != activityId) {
							int index = activityId.indexOf(WorkflowConstants.ACTIVITY_ID_IN_PLUGIN);
							if (index > 0) {
								activePluginName = activityId.substring(0, index);
								break;
							}
						}
					}
				}
			}
		}
		LOGGER.debug("Active plugin workflow for batch instance id:", batchInstanceId, " is:", activePluginName);
		return activePluginName;
	}

	/**
	 * Gets last workflow in execution's name for an error batch.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link String}
	 */
	private String getErrorBatchLastWorkflowInExecutionName(final String batchInstanceIdentifier) {
		BatchInstanceErrorDetails batchInstanceErrorDetails = batchInstanceErrorService
				.getBatchInstanceErrorDetailByIdentifier(batchInstanceIdentifier);
		String activeWorkflowName;
		String lastPluginName;
		if (null == batchInstanceErrorDetails) {
			lastPluginName = null;
		} else {
			lastPluginName = batchInstanceErrorDetails.getLastPluginName();
		}
		if (EphesoftStringUtil.isNullOrEmpty(lastPluginName) && null != batchInstanceErrorDetails) {
			activeWorkflowName = batchInstanceErrorDetails.getLastModuleName();
		} else {
			activeWorkflowName = lastPluginName;
		}
		return activeWorkflowName;
	}

	@Override
	public String getErrorBatchLastExecutedModule(String batchInstanceIdentifier) {
		BatchInstanceErrorDetails batchInstanceErrorDetails = batchInstanceErrorService
				.getBatchInstanceErrorDetailByIdentifier(batchInstanceIdentifier);
		String lastModuleName;
		if (null == batchInstanceErrorDetails) {
			lastModuleName = null;
		} else {
			lastModuleName = batchInstanceErrorDetails.getLastModuleName();

		}
		return lastModuleName;
	}

	/**
	 * Triggers a batch execution y starting its workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param moduleName {@link String}
	 * @param batchIdentifier {@link String}
	 * @param batchInstance {@link BatchInstance}
	 * @return boolean
	 */
	private boolean triggerBatchExecution(final BatchInstanceID batchInstanceID, final String moduleName,
			final String batchInstanceIdentifier, final BatchInstance batchInstance) {
		LOGGER.trace("Starting workflow for a batch: ", batchInstanceIdentifier);
		boolean isWorkflowStarted;

		// Variables floating through every process instance of a batch.
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put(WorkflowVariables.BATCH_INSTANCE_ID, batchInstanceID);
		vars.put(WorkflowVariables.RESTART_WORKFLOW, moduleName);
		vars.put(WorkflowVariables.KEY, batchInstanceIdentifier);
		vars.put(WorkflowVariables.IS_MODULE_REMOTE, CommonConstants.NO);
		batchInstanceService.updateBatchInstanceStatusByIdentifier(batchInstanceID.getID(), BatchInstanceStatus.RUNNING);
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(batchInstance.getBatchClass().getName(),
				batchInstanceIdentifier, vars);
		runtimeService.setProcessInstanceName(processInstance.getId(), batchInstance.getProcessInstanceKey());
		isWorkflowStarted = true;
		LOGGER.info("Started Workflow successfully for batch instance id:", batchInstanceID, " for module name:", moduleName);
		return isWorkflowStarted;
	}

	@Override
	public void handleErrorBatch(final BatchInstance batchInstance, final Exception exception, String exceptionMessage) {
		setParameters(batchInstance, exception, exceptionMessage);
		Thread thread = new Thread(this.errorBatchServiceRunnable);
		thread.start();
	}

	/**
	 * @param batchInstance
	 * @param exception
	 * @param exceptionMessage
	 */
	private void setParameters(final BatchInstance batchInstance, final Exception exception, String exceptionMessage) {
		this.errorBatchServiceRunnable.setBatchInstance(batchInstance);
		this.errorBatchServiceRunnable.setException(exception);
		this.errorBatchServiceRunnable.setExceptionMessage(exceptionMessage);
	}

	@Override
	public Map<Set<Integer>, Integer> getAllServerExecutionCapacity() {
		LOGGER.info("Fetching All Server Execution Capacity");
		Map<Set<Integer>, Integer> allServerExecutionCapacityMap = serverRegistryService.getPriorityLoadedActiveServers();
		return allServerExecutionCapacityMap;
	}

	private int getAllServerExecutionCapacity(final int priority) {
		int capacity = 0;
		Map<Set<Integer>, Integer> prioritySetCapacityMap = getAllServerExecutionCapacity();
		if (null != prioritySetCapacityMap && !prioritySetCapacityMap.isEmpty()) {
			Set<Set<Integer>> priorityCapacityMapKeys = prioritySetCapacityMap.keySet();
			for (Set<Integer> aPrioritySet : priorityCapacityMapKeys) {
				if (CollectionUtils.isNotEmpty(aPrioritySet) && aPrioritySet.contains(priority)) {
					LOGGER.debug("A priority set contaning priority: ", priority, "found.");
					capacity = prioritySetCapacityMap.get(aPrioritySet);
					break;
				}
			}
		}
		return capacity;
	}

	@Override
	public void createBackupBeforeCleanup(BatchInstanceID batchInstanceID) {
		final BatchInstance batchInstance = batchInstanceDao.getBatchInstancesForIdentifier(batchInstanceID.getID());

		// Create module backup XML for the module before cleanup
		if (null != batchInstance && BackUpFileService.isBackupRequired()) {
			String batchInstanceIdentifier = batchInstance.getIdentifier();
			String moduleName = restartService.getBatchLastExecutedModule(batchInstance);
			String workflow = getWorkflowNameByModuleName(moduleName, batchInstance.getBatchClass().getBatchClassModules());
			// batchSchemaService.createBatchXml(batchInstanceIdentifier, workflow);
			batchSchemaService.createBatchXml(batchInstanceIdentifier, "Final");
		}

	}

	@Override
	public String getWorkflowDetails(String batchInstanceIdentifier) {
		String workflowDetail;
		List<HistoricProcessInstance> historicProcessList = getHistoricData(batchInstanceIdentifier);
		String currentModuleWorkflowName = null;
		String lastPluginWorkflowName = null;
		if (CollectionUtils.isEmpty(historicProcessList)) {
			workflowDetail = null;
		} else {
			HistoricProcessInstance lastHistoricProcess = historicProcessList.get(0);
			String lastBusinessKey = lastHistoricProcess.getBusinessKey();
			boolean workflowStarting = isWorkflowStarting(lastBusinessKey, batchInstanceIdentifier);
			boolean workflowFinished = isWorkflowFinished(lastBusinessKey, batchInstanceIdentifier);
			if (workflowStarting) {
				workflowDetail = null;
				LOGGER.debug("Batch instance: ", batchInstanceIdentifier, " has just started.");
			} else if (workflowFinished) {
				workflowDetail = WorkflowConstants.WORKFLOW_UNIT_END_INDICATOR;
				LOGGER.debug("Batch instance: ", batchInstanceIdentifier, " has finished.");
			} else {
				String businessKey;
				for (HistoricProcessInstance historicProcessInstance : historicProcessList) {

					// searching for first -m and -p entry
					if (null != historicProcessInstance) {
						businessKey = historicProcessInstance.getBusinessKey();
						if (!EphesoftStringUtil.isNullOrEmpty(businessKey)) {

							// added endtime check to count Ready_for_review and Ready_for_validation halt states as current executing
							// plugin and not as executed plugins.
							if (null != historicProcessInstance.getEndTime() && null == lastPluginWorkflowName
									&& businessKey.endsWith(WorkflowConstants.PLUGIN_BUSINESS_KEY_SIGNATURE)) {
								lastPluginWorkflowName = getWorkflowNameFromBusinessKey(businessKey, ICommonConstants.DOT,
										WorkflowConstants.PLUGIN_BUSINESS_KEY_SIGNATURE);
							} else if (businessKey.endsWith(WorkflowConstants.MODULE_BUSINESS_KEY_SIGNATURE)) {
								currentModuleWorkflowName = getWorkflowNameFromBusinessKey(businessKey, ICommonConstants.DOT,
										WorkflowConstants.MODULE_BUSINESS_KEY_SIGNATURE);
								break;
							}
						}
					}
				}
				LOGGER.debug("For batch instance: ", batchInstanceIdentifier, ", Current executing module is: ",
						currentModuleWorkflowName, " and last executed plugin is: ", lastPluginWorkflowName);
				StringBuilder currentModuleLastPluginBuilder = new StringBuilder();
				if (null != currentModuleWorkflowName) {
					currentModuleLastPluginBuilder.append(currentModuleWorkflowName);
				}
				if (null != lastPluginWorkflowName) {
					currentModuleLastPluginBuilder.append(ICommonConstants.COLON);
					currentModuleLastPluginBuilder.append(lastPluginWorkflowName);
				}
				workflowDetail = currentModuleLastPluginBuilder.toString();
			}
		}
		LOGGER.debug("Workflow Detail for batch instance: ", batchInstanceIdentifier, " is: ", workflowDetail);
		return workflowDetail;
	}

	private String getWorkflowNameFromBusinessKey(String businessKey, String delimeter, String suffix) {
		String workflowName;
		if (EphesoftStringUtil.isNullOrEmpty(businessKey)) {
			workflowName = null;
		} else {
			int index = businessKey.indexOf(delimeter);
			if (-1 == index) {
				workflowName = null;
			} else {
				workflowName = businessKey.substring(index + 1).replace(suffix, ICommonConstants.EMPTY_STRING);
				LOGGER.info("Workflow name for business key: ", businessKey, " is: ", workflowName);
			}
		}
		return workflowName;
	}

	private boolean isWorkflowStarting(final String businessKey, String batchInstanceIdentifier) {
		boolean result;
		if (businessKey.contains(WorkflowConstants.WORKFLOW_STATUS_RUNNING) || businessKey.equalsIgnoreCase(batchInstanceIdentifier)) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	private boolean isWorkflowFinished(final String businessKey, String batchInstanceIdentifier) {
		boolean result;
		if (businessKey.contains(WorkflowConstants.WORKFLOW_STATUS_FINISHED)) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	private List<HistoricProcessInstance> getHistoricData(String batchInstanceIdentifier) {
		List<HistoricProcessInstance> historyProcessList = null;

		// Edited because ordered list from database is sorted on varchar process instance ids, but we need numeric descending sorting.
		if (!EphesoftStringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			historyProcessList = historyService.createHistoricProcessInstanceQuery()
					.variableValueEquals(WorkflowVariables.KEY, batchInstanceIdentifier).list();
		}
		if (CollectionUtils.isNotEmpty(historyProcessList)) {
			Comparator<HistoricProcessInstance> reverseComparator = Collections.reverseOrder(new HistoricProcessComparator());
			Collections.sort(historyProcessList, reverseComparator);
		}
		return historyProcessList;
	}

	@Override
	public Map<Integer, List<Integer>> getBatchPriorityCapacityMap() {
		// MultiKeyMap batchPriorityCapacityMap = MultiKeyMap.decorate(new LinkedMap(ICommonConstants.MAX_BATCH_INSTANCE_PRIORITY));
		Map<Integer, List<Integer>> batchPriorityCapacityMap = new HashMap<Integer, List<Integer>>(
				ICommonConstants.MAX_BATCH_INSTANCE_PRIORITY);
		for (int index = 0; index < ICommonConstants.MAX_BATCH_INSTANCE_PRIORITY; index++) {
			batchPriorityCapacityMap.put(index, null);
		}
		return batchPriorityCapacityMap;
	}
}
