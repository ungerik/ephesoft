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

package com.ephesoft.dcma.workflows.service.engine.impl;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.ActivitiOptimisticLockingException;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.constants.CommonConstants;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ThreadPool;
import com.ephesoft.dcma.da.dao.BatchInstanceGroupsDao;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.BatchInstanceErrorDetails;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.service.BatchInstanceErrorDetailsService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.client.EphesoftWebServiceClient;
import com.ephesoft.dcma.workflows.common.WorkflowVariables;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;
import com.ephesoft.dcma.workflows.service.WorkflowService;
import com.ephesoft.dcma.workflows.service.engine.EngineService;
import com.ephesoft.dcma.workflows.util.WorkflowUtil;

/**
 * This class represents the implementation of Engine service for a third party workflow engine.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class EngineServiceImpl implements EngineService {

	/**
	 * {@link EphesoftLogger} Instance of Logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(EngineServiceImpl.class);

	/**
	 * {@link BatchInstanceService} Instance of batch instance service.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * boolean Signal for job acquire command to acquire jobs.
	 */
	private boolean serverReadyForJobExecution;

	/**
	 * {@link BatchInstanceGroupsDao} Instance of batch instance group Dao.
	 */
	@Autowired
	private BatchInstanceGroupsDao batchInstanceGroupsDao;

	/**
	 * {@link RuntimeService} Instance of runtime service.
	 */
	@Autowired
	private RuntimeService runtimeService;

	/**
	 * {@link WorkflowService} Instance of workflow service.
	 */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * {@link EphesoftWebServiceClient} Instance of web service client.
	 */
	@Autowired
	private EphesoftWebServiceClient ephesoftWebServiceClient;

	/**
	 * {@link ManagementService} Instance of management service.
	 */
	@Autowired
	private ManagementService managementService;

	/**
	 * {@link BatchInstanceErrorDetailsService} Instance of batch instance error details service.
	 */
	@Autowired
	private BatchInstanceErrorDetailsService batchInstanceErrorDetailsService;

	/**
	 * {@link PluginPropertiesService}
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * {@link BatchSchemaService} Instance of batch schema service
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;
	/**
	 * Time for which we will wait for resources to get completed before a batch can be restarted.
	 */
	private long waitTime;

	/**
	 * {@link DateFormat} Instance of Date format.
	 */
	private final DateFormat timeFormat = new SimpleDateFormat(CommonConstants.SIMPLE_DATE_FORMAT, Locale.getDefault());

	/**
	 * Sets wait time for cleaning of resources for a batch.
	 * 
	 * @param waitTime long
	 */
	public void setWaitTime(final long waitTime) {
		this.waitTime = waitTime;
	}

	/**
	 * Default constructor.
	 */
	public EngineServiceImpl() {
		this.serverReadyForJobExecution = false;
	}

	@Override
	@Transactional
	public BatchInstance getBatchInstanceByExecutionId(String executionId) {
		BatchInstance batchInstance = null;
		if (null == executionId) {
			LOGGER.error("Execution id for getting abtch instance id is null.");
		} else {
			String batchInstanceId = (String) runtimeService.getVariable(executionId, WorkflowVariables.KEY);
			LOGGER.info("Batch Instance Identifier:", batchInstanceId, " found for execution id : ", executionId);
			batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
		}
		return batchInstance;
	}

	@Override
	@Transactional
	public void deleteProcessInstanceByBatchInstance(final BatchInstance batchInstance, final boolean isExceptionAllowed)
			throws DCMABusinessException {
		String batchInstanceIdentifier = batchInstance.getIdentifier();
		cleanBatchResources(batchInstanceIdentifier);
		try {
			Execution execution = getExecution(batchInstanceIdentifier);
			if (null == execution) {
				LOGGER.warn("Batch instance:", batchInstanceIdentifier, " job state process instance is already deleted.");
			} else {
				String executionId = execution.getId();
				if (EphesoftStringUtil.isNullOrEmpty(executionId)) {
					doIfErrorDeletingBatch(batchInstance, isExceptionAllowed, batchInstanceIdentifier, null);
				} else {
					runtimeService.deleteProcessInstance(executionId, "Batch process is no more required.");
				}
			}
		} catch (final ActivitiOptimisticLockingException activitiOptimisticLockingException) {

			// Added retry of delete in case locking exception comes while deleting a batch.
			int index;
			for (index = 0; index < 3; index++) {
				Execution execution = getExecution(batchInstanceIdentifier);
				if (null == execution) {
					break;
				} else {
					String executionId = execution.getId();
					try {
						runtimeService.deleteProcessInstance(executionId, "Batch process is no more required.");
					} catch (final ActivitiOptimisticLockingException activitiOptimisticLockingExceptionIntenal) {
						// do nothing.
					}
				}
			}
			if (index == 3) {
				Execution execution = getExecution(batchInstanceIdentifier);
				if (null == execution) {
					LOGGER.info("After retrying multiple times, batch instance id:", batchInstanceIdentifier,
							" current job is deleted.");
				} else {
					doIfErrorDeletingBatch(batchInstance, isExceptionAllowed, batchInstanceIdentifier,
							activitiOptimisticLockingException);
				}
			} else {
				LOGGER.info("After retrying multiple times, batch instance id:", batchInstanceIdentifier, " current job is deleted.");
			}
		} catch (final ActivitiObjectNotFoundException exception) {
			doIfErrorDeletingBatch(batchInstance, isExceptionAllowed, batchInstanceIdentifier, exception);
		}
	}

	/**
	 * Gets current execution object for a batch instance.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link Execution}
	 */
	private Execution getExecution(final String batchInstanceIdentifier) {
		Execution execution = null;
		ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(
				batchInstanceIdentifier);
		if (null != processInstanceQuery) {
			execution = processInstanceQuery.singleResult();
		}
		return execution;
	}

	@Override
	@Transactional
	public Job getJobById(final String jobId) {
		Job job = managementService.createJobQuery().jobId(jobId).singleResult();
		return job;
	}

	/**
	 * Sends batch to error state and throws exception if applicable, when error comes while deleting a batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param isExceptionAllowed boolean
	 * @param batchInstanceIdentifier {@link String}
	 * @param exception {@link Exception}
	 * @throws DCMABusinessException
	 */
	private void doIfErrorDeletingBatch(final BatchInstance batchInstance, final boolean isExceptionAllowed,
			String batchInstanceIdentifier, final Exception exception) throws DCMABusinessException {
		LOGGER.error(exception, "Updating batch instance status into ERROR for batch Instance ID :", batchInstanceIdentifier);
		if (isExceptionAllowed) {
			String errorMessage = "Error occur while deletion of batch instance process key from database. ";
			if (null == exception) {
				workflowService.handleErrorBatch(batchInstance, exception, errorMessage);
				throw new DCMABusinessException(errorMessage);
			} else {
				String newErrorMessage = EphesoftStringUtil.concatenate(errorMessage, exception.getMessage());
				workflowService.handleErrorBatch(batchInstance, exception, newErrorMessage);
				throw new DCMABusinessException(newErrorMessage, exception);
			}
		}
	}

	/**
	 * Saves workflow module and plugin name in error details when batch is going to go in ERROR state.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 */
	private void saveWorkflowNames(final String batchInstanceIdentifier) {
		BatchInstanceErrorDetails batchInstanceErrorDetails = batchInstanceErrorDetailsService
				.getBatchInstanceErrorDetailByIdentifier(batchInstanceIdentifier);
		if (null == batchInstanceErrorDetails) {
			batchInstanceErrorDetails = new BatchInstanceErrorDetails();
		}
		batchInstanceErrorDetails.setIdentifier(batchInstanceIdentifier);
		setLastExecutedPluginName(batchInstanceIdentifier, batchInstanceErrorDetails);
		setLastExecutedModuleName(batchInstanceIdentifier, batchInstanceErrorDetails);
		batchInstanceErrorDetailsService.saveOrUpdate(batchInstanceErrorDetails);
	}

	/**
	 * Sets last executed module name for a batch instance in batch instance error details.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param batchInstanceErrorDetails {@link BatchInstanceErrorDetails}
	 */
	private void setLastExecutedModuleName(String batchInstanceIdentifier, final BatchInstanceErrorDetails batchInstanceErrorDetails) {
		if (null != batchInstanceErrorDetails) {
			String moduleName = workflowService.getActiveModuleName(batchInstanceIdentifier);
			if (null == moduleName) {
				batchInstanceErrorDetails.setLastModuleName(WorkflowConstants.ACTIVE_MODULE_NOT_FOUND);
			} else {
				batchInstanceErrorDetails.setLastModuleName(moduleName);
			}
		}
	}

	/**
	 * Sets last executed plugin name for a batch instance in batch instance error details.
	 * 
	 * @param executionId {@link String}
	 * @return {@link BatchInstanceErrorDetails}
	 */
	private void setLastExecutedPluginName(String batchInstanceIdentifier, final BatchInstanceErrorDetails batchInstanceErrorDetails) {
		if (null != batchInstanceErrorDetails) {
			String pluginName = workflowService.getActivePluginWorkflow(batchInstanceIdentifier);
			if (null == pluginName) {
				batchInstanceErrorDetails.setLastPluginName(WorkflowConstants.ACTIVE_PLUGIN_NOT_FOUND);
			} else {
				batchInstanceErrorDetails.setLastPluginName(pluginName);
			}
		}
	}

	@Override
	@Transactional
	public boolean isBatchOwnedByCurrentServer(final Set<String> setOfBatches, final String batchInstanceIdentifier,
			final String serverContextPath) {
		boolean currentServerBatch;
		LOGGER.info("Counting no. of executing jobs by server: ", serverContextPath);
		if (CollectionUtils.isNotEmpty(setOfBatches) && setOfBatches.contains(batchInstanceIdentifier)) {
			currentServerBatch = true;
		} else {
			currentServerBatch = false;
		}
		return currentServerBatch;
	}

	@Override
	public Set<String> getRunningBatchInstanceIdentifiers(final String serverContextPath) {
		Set<String> batchIntanceIdentifiers = null;
		List<BatchInstance> runningBatchesforServer = batchInstanceService.getRunningBatchInstancesForServer(serverContextPath);
		if (CollectionUtils.isNotEmpty(runningBatchesforServer)) {
			batchIntanceIdentifiers = new HashSet<String>(runningBatchesforServer.size());
			for (BatchInstance batchInstance : runningBatchesforServer) {
				if (null != batchInstance) {
					batchIntanceIdentifiers.add(batchInstance.getIdentifier());
				}
			}
		}
		return batchIntanceIdentifiers;
	}

	@Override
	@Transactional
	public JobEntity lockJob(final CommandContext commandContext, final JobEntity job, String lockOwner, final int lockTimeInMillis) {
		JobEntity modifiedJobEntity;

		// Providing check again before locking a job and using activiti command listener class for reset retry count for job.
		if (EphesoftStringUtil.isNullOrEmpty(job.getLockOwner())) {
			String jobId = job.getId();
			resetJobRetries(jobId, WorkflowConstants.MAX_NUMBER_OF_JOB_RETRIES);
			modifiedJobEntity = commandContext.getJobEntityManager().findJobById(jobId);
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			Date currentTime = getCurrentTime(commandContext);
			gregorianCalendar.setTime(currentTime);
			gregorianCalendar.add(Calendar.MILLISECOND, lockTimeInMillis);
			Date date = gregorianCalendar.getTime();
			LOGGER.info("Trying to obtain a lock for job with id: ", jobId, " with expiration time: ", timeFormat.format(date),
					" by lock owner: ", lockOwner);
			modifiedJobEntity.setLockOwner(lockOwner);
			modifiedJobEntity.setLockExpirationTime(date);
		} else {
			modifiedJobEntity = null;
		}
		return modifiedJobEntity;
	}

	/**
	 * Sets job retries to a count that makes batch to go to error without any retries.
	 * 
	 * @param job {@link JobEntity}
	 */
	public void resetJobRetries(final String jobId, int retries) {
		managementService.setJobRetries(jobId, retries);
		LOGGER.debug("Job id: ", jobId, ", No of retries: ", WorkflowConstants.MAX_NUMBER_OF_JOB_RETRIES);
	}

	@Override
	public Date getCurrentTime(final CommandContext commandContext) {
		Date currentTime;
		if (null == commandContext) {
			currentTime = Context.getProcessEngineConfiguration().getClock().getCurrentTime();
		} else {
			currentTime = commandContext.getProcessEngineConfiguration().getClock().getCurrentTime();
		}
		return currentTime;
	}

	@Override
	public BatchInstance getBatchInstanceByJobEntity(final JobEntity jobEntity) {
		BatchInstance batchInstance = null;
		if (null != jobEntity) {
			String jobId = jobEntity.getId();
			String executionId = jobEntity.getExecutionId();
			if (null == executionId) {
				LOGGER.error("The job: ", jobId,
						"'s execution id is null. Batch instance corresponding to this job could not be sent to error.");
			} else {
				batchInstance = getBatchInstanceByExecutionId(executionId);
			}
		}
		return batchInstance;
	}

	@Override
	@Transactional
	public void sendBatchToErrorState(String batchInstanceIdentifier) {
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		if (null == batchInstance) {
			LOGGER.error("The batch Instance object for batch identifier:", batchInstanceIdentifier,
					"is null. Batch instance corresponding to this job could not be sent to error.");
		} else {
			BatchInstanceStatus batchStatus = batchInstance.getStatus();
			if (batchStatus == BatchInstanceStatus.ERROR) {
				LOGGER.warn("The batch: ", batchInstanceIdentifier, " status is ERROR. Thus Batch instance is already sent to error.");
			} else if (batchStatus == BatchInstanceStatus.FINISHED) {

				// This case because sometimes moveBatches to finished state calls for deleting batches.
				LOGGER.warn("The batch: ", batchInstanceIdentifier,
						" status is FINISHED. Thus Batch instance could not be sent to error.");
			} else {

				// Save names of last execution information for the purpose of reference for mails.
				saveWorkflowNames(batchInstanceIdentifier);
				batchInstanceService.updateBatchInstanceStatus(batchInstanceIdentifier, BatchInstanceStatus.ERROR);
				pluginPropertiesService.clearCache(batchInstanceIdentifier);
				batchSchemaService.removeBatch(batchInstanceIdentifier);
				LOGGER.info("The batch: ", batchInstanceIdentifier, " is sent to error.");
			}
		}
	}

	@Override
	public boolean deleteBatchInstance(String batchInstanceIdentifier) throws DCMAApplicationException {
		boolean isDeleted = true;
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		String batchInstanceExecutingServer = batchInstance.getExecutingServer();
		boolean sameServer = isSameServer(batchInstanceExecutingServer);
		if (!sameServer && !EphesoftStringUtil.isNullOrEmpty(batchInstanceExecutingServer)) {
			if (EphesoftContext.getCurrent().isSecure()) {
				isDeleted = ephesoftWebServiceClient.deleteBatchInstance(EphesoftStringUtil.concatenate(ICommonConstants.HTTPS_SLASH,
						batchInstanceExecutingServer, ICommonConstants.FORWARD_SLASH, ICommonConstants.WEB_SERVICE),
						batchInstanceIdentifier);
			} else {
				isDeleted = ephesoftWebServiceClient.deleteBatchInstance(EphesoftStringUtil.concatenate(ICommonConstants.HTTP_SLASH,
						batchInstanceExecutingServer, ICommonConstants.FORWARD_SLASH, ICommonConstants.WEB_SERVICE),
						batchInstanceIdentifier);
			}
		} else {
			try {
				deleteProcessInstanceByBatchInstance(batchInstance, true);
				pluginPropertiesService.clearCache(batchInstanceIdentifier);
				batchInstance.setStatus(BatchInstanceStatus.DELETED);
				batchInstanceService.updateBatchInstance(batchInstance);
				batchSchemaService.removeBatch(batchInstanceIdentifier);
				batchInstanceGroupsDao.deleteBatchInstanceInGroups(batchInstanceIdentifier);
				removeUncFolder(batchInstance);
			} catch (Exception e) {
				isDeleted = false;
				throw new DCMAApplicationException(e.getMessage());
			}
		}
		return isDeleted;
	}

	@Override
	public void cleanBatchResources(final String batchInstanceIdentifier) {
		final BatchInstanceThread batchInstanceThread = ThreadPool.getBatchInstanceThreadList(batchInstanceIdentifier);
		if (null != batchInstanceThread) {
			batchInstanceThread.remove(getWaitTime());
		}
	}

	@Override
	public boolean isSameServer(final String batchInstanceExecutingServer) {
		final ServerRegistry batchInstanceServerRegistry = WorkflowUtil.getServerRegistry(batchInstanceExecutingServer);
		String serverContextPath = EphesoftContext.getHostServerContextPath();
		boolean isSameServer;
		if (null != batchInstanceServerRegistry && batchInstanceServerRegistry.isActive()
				&& !EphesoftStringUtil.isNullOrEmpty(batchInstanceExecutingServer)
				&& !(batchInstanceExecutingServer.equals(serverContextPath))) {
			isSameServer = false;
		} else {
			isSameServer = true;
		}
		return isSameServer;
	}

	/**
	 * Gets wait time for waiting for a batch while cleaning its resources.
	 * 
	 * @return long
	 */
	private long getWaitTime() {
		long waitTime;
		if (this.waitTime == 0L) {
			LOGGER.error("Wait time property does not have a valid value.");
			waitTime = WorkflowConstants.DEFAULT_BATCH_RESOURCE_CLEAN_WAIT_TIME;
		} else {
			waitTime = this.waitTime;
		}
		LOGGER.debug(EphesoftStringUtil.concatenate("Waiting time for cleaning up batch resources is: ", waitTime));
		return waitTime;
	}

	/**
	 * Remove UNC folder for a batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	private void removeUncFolder(final BatchInstance batchInstance) {
		File uncFile = new File(batchInstance.getUncSubfolder());
		if (null != uncFile) {
			FileUtils.deleteDirectoryAndContentsRecursive(uncFile);
		}
	}

	@Override
	public boolean isServerReadyForJobExecution() {
		return serverReadyForJobExecution;
	}

	@Override
	public void setServerReadyForJobExecution(final boolean serverReadyForJobExecution) {
		this.serverReadyForJobExecution = serverReadyForJobExecution;
	}

}
