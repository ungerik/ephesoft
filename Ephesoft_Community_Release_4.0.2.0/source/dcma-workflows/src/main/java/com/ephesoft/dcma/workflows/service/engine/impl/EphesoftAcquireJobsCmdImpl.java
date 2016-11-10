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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.jobexecutor.AcquiredJobs;
import org.activiti.engine.impl.jobexecutor.JobExecutor;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.apache.commons.collections.CollectionUtils;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ServerRegistryService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.service.ProcessCapacityService;
import com.ephesoft.dcma.workflows.service.engine.EngineService;
import com.ephesoft.dcma.workflows.service.engine.EphesoftAcquireJobsCmd;
import com.ephesoft.dcma.workflows.util.ServerDetails;

/**
 * This class represents an implementation of acquire job command.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class EphesoftAcquireJobsCmdImpl implements Command<AcquiredJobs>, EphesoftAcquireJobsCmd {

	/**
	 * {@link JobExecutor} Instance of job executor.
	 */
	private JobExecutor jobExecutor;

	/**
	 * {@link EngineService} Instance of engine service.
	 */
	private EngineService engineService;

	/**
	 * {@link ServerDetails} Instance of engine service.
	 */
	private ServerDetails serverDetails;

	/**
	 * {@link EphesoftLogger} Instance of Logger.
	 */
	private EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(EphesoftAcquireJobsCmdImpl.class);

	/**
	 * {@link JobEntity} object after set of retries and locked by a server for execution.
	 */
	private JobEntity lockedJobEntity;

	/**
	 * Default constructor of class, sets the job executor.
	 * 
	 * @param jobExecutor {@link JobExecutor}
	 */
	public EphesoftAcquireJobsCmdImpl(final JobExecutor jobExecutor) {
		this.jobExecutor = jobExecutor;
		serverDetails = new ServerDetails();
		serverDetails.setLockTimeInMillis(jobExecutor.getLockTimeInMillis());
		ServerRegistryService serverRegistryService = EphesoftContext.get(ServerRegistryService.class);
		serverDetails.setSetOfPriority(serverRegistryService.getNumericSetOfPriorityRange());
		String serverContextPath = EphesoftContext.getHostServerContextPath();
		serverDetails.setServerContextPath(serverContextPath);
		serverDetails.setServerIPAddress(EphesoftContext.getHostAddress());
		ServerRegistry server = EphesoftContext.getHostServerRegistry();
		serverDetails.setLastServerModified(server.getLastModified());
	}

	/**
	 * Executes the acquire job task. By default it is executed by Third party engine itself.
	 * 
	 * @param commandContext {@link CommandContext}
	 */
	@Override
	public AcquiredJobs execute(final CommandContext commandContext) {
		EngineService engineService = getEngineServiceInstance();
		AcquiredJobs acquiredJobs = new AcquiredJobs();
		if (null != engineService && engineService.isServerReadyForJobExecution()) {
			List<JobEntity> jobs = commandContext.getJobEntityManager().findNextJobsToExecute(null);
			// List<Job> jobs = engineService.getWithRetriesJobs();
			if (CollectionUtils.isNotEmpty(jobs)) {

				// Getting all details.
				String serverContextPath = jobExecutor.getName();
				int lockTimeInMillis = jobExecutor.getLockTimeInMillis();
				int maxNonExclusiveJobsPerAcquisition = jobExecutor.getMaxJobsPerAcquisition();
				LOGGER.debug("Current jobExecutor's lock owner is: ", serverContextPath, ". Lock time in miliseconds is: ",
						lockTimeInMillis, ". Max Non exclusive jobs per acquisition is: ", maxNonExclusiveJobsPerAcquisition,
						" No. of Jobs waiting to get acquired are: ", jobs.size());
				int localCounter = 0;

				// Loop over all jobs to find and acquire jobs.
				for (JobEntity jobEntity : jobs) {
					if (null != jobEntity) {
						if (localCounter < maxNonExclusiveJobsPerAcquisition) {
							localCounter = acquireBatchExecution(commandContext, acquiredJobs, localCounter, jobEntity);
						} else {
							LOGGER.trace("No more jobs can be acquired by server: ", serverContextPath);
							break;
						}
					}
				}
			}
		}
		return acquiredJobs;
	}

	/**
	 * Acquires an batch instance for its current job execution.
	 * 
	 * @param commandContext {@link CommandContext}
	 * @param acquiredJobs {@link AcquiredJobs}
	 * @param localCounter int
	 * @param jobEntity {@link JobEntity}
	 * @return int
	 */
	private int acquireBatchExecution(final CommandContext commandContext, final AcquiredJobs acquiredJobs, int localCounter,
			final JobEntity jobEntity) {
		String batchLockOwner = getBatchLockOwnerByJob(jobEntity);
		if (null != jobEntity && isSameBatchLockOwner(batchLockOwner)) {
			localCounter += updateBatchInstanceAndAcquireJob(commandContext, acquiredJobs, jobEntity);
		}
		return localCounter;
	}

	/**
	 * Checks if the current job lock owner is same as the last lock owner of the batch's jobs.
	 * 
	 * @param batchLockOwner {@link String}
	 * @return boolean
	 */
	private boolean isSameBatchLockOwner(final String batchLockOwner) {
		boolean result;
		String serverContextPath = serverDetails.getServerContextPath();
		if (EphesoftStringUtil.isNullOrEmpty(batchLockOwner) || batchLockOwner.equalsIgnoreCase(serverContextPath)) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Gets the batch lock owner for current job entity.
	 * 
	 * @param jobEntity {@link JobEntity}
	 * @return {@link String}
	 */
	private String getBatchLockOwnerByJob(final JobEntity jobEntity) {
		String batchLockOwner;
		if (null == jobEntity) {
			batchLockOwner = null;
		} else {
			String executionId = jobEntity.getExecutionId();
			EngineService engineService = getEngineServiceInstance();
			BatchInstance batchInstance = engineService.getBatchInstanceByExecutionId(executionId);
			batchLockOwner = batchInstance.getExecutingServer();
		}
		return batchLockOwner;
	}

	/**
	 * Updates batch instance and acquires job for that batch instance.
	 * 
	 * @param commandContext {@link CommandContext}
	 * @param acquiredJobs {@link AcquiredJobs}
	 * @param jobEntity {@link JobEntity}
	 * @return int
	 */
	private int updateBatchInstanceAndAcquireJob(final CommandContext commandContext, final AcquiredJobs acquiredJobs,
			final JobEntity jobEntity) {
		int localCounter = 0;
		if (null != jobEntity) {
			EngineService engineService = getEngineServiceInstance();
			String executionId = jobEntity.getExecutionId();
			BatchInstance batchInstance = engineService.getBatchInstanceByExecutionId(executionId);
			if (null != batchInstance) {
				String serverContextPath = serverDetails.getServerContextPath();
				if (!EphesoftStringUtil.isNullOrEmpty(serverContextPath)) {
					Set<Integer> setOfPriority = serverDetails.getSetOfPriority();
					int priority = batchInstance.getPriority();
					if (null != setOfPriority && setOfPriority.contains(priority)) {
						LOGGER.debug("Batch instance: ", batchInstance.getIdentifier(), " with priority: ", priority,
								", its job is going to be picked by: ", serverContextPath);
						String batchInstanceIdentifier = batchInstance.getIdentifier();
						Set<String> runningbatchIdentifiersForCurrentServer = engineService
								.getRunningBatchInstanceIdentifiers(serverContextPath);

						// Getting current server's executing job count. Need to check it every time before locking a job because other
						// services
						// like resume service may change this count.
						boolean batchOwnedByCurrentServer = engineService.isBatchOwnedByCurrentServer(
								runningbatchIdentifiersForCurrentServer, batchInstanceIdentifier, serverContextPath);

						// a batch's first job if picked by this server then this server must pick that batch's new job without
						// checking
						// for remaining capacity.
						if (batchOwnedByCurrentServer) {
							LOGGER.debug("Batch: ", batchInstanceIdentifier, " is owned by: ", serverContextPath,
									" and its new job is going to be executed now");
							pickAJob(batchInstance, jobEntity, executionId, commandContext, acquiredJobs);
							localCounter++;
						} else {
							ProcessCapacityService processCapacityService = EphesoftContext.get(ProcessCapacityService.class);
							int runningBatchCount;
							if (CollectionUtils.isEmpty(runningbatchIdentifiersForCurrentServer)) {
								runningBatchCount = 0;
							} else {
								runningBatchCount = runningbatchIdentifiersForCurrentServer.size();
							}
							// If current server has more executing capacity then it goes for picking a job.
							if (runningBatchCount < processCapacityService.getMaxCapacity()) {
								LOGGER.debug("Batch: ", batchInstanceIdentifier,
										" is not yet owned by any server, but will now be owned by: ", serverContextPath,
										" and its new job is going to be executed now.");
								pickAJob(batchInstance, jobEntity, executionId, commandContext, acquiredJobs);
								localCounter++;
							}
						}
					}
				}
			}
		}
		return localCounter;
	}

	/**
	 * Picks a job of an already chosen batch for this server or a new batch with some remaining capacity for execution.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param jobEntity {@link JobEntity}
	 * @param executionId {@link String}
	 * @param commandContext {@link CommandContext}
	 * @param acquiredJobs {@link AcquiredJobs}
	 */
	private void pickAJob(final BatchInstance batchInstance, final JobEntity jobEntity, final String executionId,
			final CommandContext commandContext, final AcquiredJobs acquiredJobs) {
		String serverContextPath = serverDetails.getServerContextPath();
		String batchInstanceIdentifier = batchInstance.getIdentifier();
		int lockTimeInMillis = serverDetails.getLockTimeInMillis();
		
		// Added check to proceed if lock was successful.
		boolean jobLockSuccessful = acquireJob(commandContext, serverContextPath, lockTimeInMillis, acquiredJobs, jobEntity);
		if (jobLockSuccessful && null != lockedJobEntity) {
			LOGGER.debug("Acquired job with job id: ", lockedJobEntity.getId(), " and execution id as: ", executionId,
					" for batch instance: ", batchInstanceIdentifier, "along with other exclusive jobs.");
			setBatchToRunning(batchInstance);
			setBatchLockOwner(serverDetails.getServerIPAddress(), serverContextPath, batchInstance);
			BatchInstanceService batchInstanceService = EphesoftContext.get(BatchInstanceService.class);
			batchInstanceService.merge(batchInstance);
			updateBatchInMemory(lockedJobEntity);
			LOGGER.info("Batch instance: ", batchInstanceIdentifier, " with status: ", batchInstance.getStatus(), " for execution: ",
					executionId, "is successfully acquired by ", serverContextPath, ".");
		}
	}

	/**
	 * Sets batch instance's executing lock owner.
	 * 
	 * @param lockOwner {@link String}
	 * @param batchInstance {@link BatchInstance}
	 */
	private void setBatchLockOwner(final String serverIPAddress, final String lockOwner, final BatchInstance batchInstance) {
		if (null != batchInstance) {
			batchInstance.setExecutingServer(lockOwner);
			batchInstance.setServerIP(serverIPAddress);
		}
	}

	/**
	 * Set batch status to RUNNING.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	private void setBatchToRunning(final BatchInstance batchInstance) {
		BatchInstanceStatus batchStatus = batchInstance.getStatus();

		// Setting batch instance status to Running before acquiring its job (in
		// case its not set yet).
		if (null != batchStatus && !batchStatus.equals(BatchInstanceStatus.RUNNING)) {
			LOGGER.debug("Setting the batch instance", batchInstance.getIdentifier(), " to RUNNING.");
		}
	}

	/**
	 * Acquire a job and its exclusive jobs for execution.
	 * 
	 * @param commandContext {@link CommandContext}
	 * @param lockOwner {@link String}
	 * @param lockTimeInMillis
	 * @param acquiredJobs {@link AcquiredJobs}
	 * @param jobEntity {@link JobEntity}
	 * @return boolean True if lock job was successful.
	 */
	private boolean acquireJob(final CommandContext commandContext, final String lockOwner, final int lockTimeInMillis,
			final AcquiredJobs acquiredJobs, final JobEntity jobEntity) {
		
		// Boolean to verify if at least one job lock was successful.
		boolean jobLockSuccessful = false;
		List<String> jobIds = new ArrayList<String>();
		if (null != acquiredJobs && null != commandContext) {
			if (null != jobEntity && !acquiredJobs.contains(jobEntity.getId())) {
				if (jobEntity.isExclusive() && null != jobEntity.getProcessInstanceId()) {
					LOGGER.info("Exclusive acquirable job found with id: ", jobEntity.getId(),
							". Querying for other exclusive jobs to lock them all in one transaction.");

					// wait to get exclusive jobs within 100ms. Default code by Activiti Engine.
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// Do Nothing.
					}

					// acquire all exclusive jobs in the same process instance
					// (includes the current job)
					List<JobEntity> exclusiveJobs = commandContext.getJobEntityManager().findExclusiveJobsToExecute(
							jobEntity.getProcessInstanceId());
					for (JobEntity exclusiveJob : exclusiveJobs) {
						if (exclusiveJob != null && !jobIds.contains(exclusiveJob.getId())) {
							jobLockSuccessful |= lockJob(commandContext, lockOwner, lockTimeInMillis, exclusiveJob, jobIds);
						}
					}
				} else {
					jobLockSuccessful |= lockJob(commandContext, lockOwner, lockTimeInMillis, jobEntity, jobIds);
				}
			}
			LOGGER.info("Acquired job count is: ", jobIds.size(), ", for lock owner server: ", lockOwner);
			acquiredJobs.addJobIdBatch(jobIds);
		}
		return jobLockSuccessful;
	}

	/**
	 * Locks a job for execution.
	 * 
	 * @param commandContext {@link CommandContext}
	 * @param lockOwner {@link String}
	 * @param lockTimeInMillis int
	 * @param jobEntity {@link JobEntity}
	 * @param acquiredJobs {@link List}<{@link String}>
	 * @return boolean True if lock job was successful.
	 */
	private boolean lockJob(final CommandContext commandContext, final String lockOwner, final int lockTimeInMillis,
			final JobEntity jobEntity, final List<String> jobIds) {
		EngineService engineService = getEngineServiceInstance();
		lockedJobEntity = engineService.lockJob(commandContext, jobEntity, lockOwner, lockTimeInMillis);
		boolean jobLockSuccessful;
		if (null == lockedJobEntity) {
			jobLockSuccessful = false;
		} else {
			jobLockSuccessful = true;
			jobIds.add(lockedJobEntity.getId());
		}
		return jobLockSuccessful;
	}

	/**
	 * Gets engine service instance.
	 * 
	 * @return {@link EngineService}
	 */
	private EngineService getEngineServiceInstance() {
		if (null == engineService) {
			engineService = EphesoftContext.get(EngineService.class);
		}
		return engineService;
	}

	private void updateBatchInMemory(JobEntity jobEntity) {
		BatchInstance batchinstance = engineService.getBatchInstanceByJobEntity(jobEntity);
		if (null != batchinstance) {
			String batchIdentifier = batchinstance.getIdentifier();
			LOGGER.debug("Updating batch in memory for batch instance: ", batchIdentifier);
			BatchSchemaService batchSchemaService = EphesoftContext.get(BatchSchemaService.class);
			boolean isBatchInMemory = batchSchemaService.isBatchInMemory(batchIdentifier);
			LOGGER.debug("Is batch object present in-memory for batch instance ", batchIdentifier, " :", isBatchInMemory);
			if (!isBatchInMemory) {
				Batch batch = batchSchemaService.getBatchFromXML(batchIdentifier, false);
				batchSchemaService.updateBatch(batch);
			}
		}
	}
}
