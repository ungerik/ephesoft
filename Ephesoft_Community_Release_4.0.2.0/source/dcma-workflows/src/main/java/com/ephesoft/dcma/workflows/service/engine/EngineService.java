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

package com.ephesoft.dcma.workflows.service.engine;

import java.util.Date;
import java.util.Set;

import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.activiti.engine.runtime.Job;

import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.ServerRegistry;

/**
 * This interface represents a service for interaction with third party workflow engine and act as a provider for all services expected
 * from a workflow engine.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version 1.0 $LastChangedDate: 2015-04-14 12:42:19 +0530 (Tue, 14 Apr 2015) $ <br/>
 *          $LastChangedRevision: 21594 $ <br/>
 */
public interface EngineService {

	/**
	 * Gets batch instance by its execution id.
	 * 
	 * @param ExecutionId {@link String}
	 * @return {@link BatchInstance}
	 */
	BatchInstance getBatchInstanceByExecutionId(String ExecutionId);

	/**
	 * Check if batch is owned by a server or not.
	 * 
	 * @param setOfBatches {@link Set}<{@link String}>
	 * @param batchInstanceIdentifier {@link String}
	 * @param serverContextPath {@link String}
	 * @return boolean
	 */
	boolean isBatchOwnedByCurrentServer(Set<String> setOfBatches, String batchInstanceIdentifier, String serverContextPath);

	/**
	 * Gets count of running batches by a server.
	 * 
	 * @return {@link Set}<{@link String}>
	 */
	Set<String> getRunningBatchInstanceIdentifiers(String serverContextPath);

	/**
	 * Locks a job.
	 * 
	 * @param commandContext {@link CommandContext}
	 * @param job {@link JobEntity}
	 * @param lockOwner {@link String}
	 * @param lockTimeInMillis in
	 * @return JobEntity Null if job already locked.
	 * */
	JobEntity lockJob(CommandContext commandContext, JobEntity job, String lockOwner, int lockTimeInMillis);

	/**
	 * Gets a job by its id.
	 * 
	 * @param jobId {@link String}
	 * @return {@link Job}
	 */
	Job getJobById(String jobId);

	/**
	 * Sends a batch to error.
	 * 
	 * @param jobEntity {@link JobEntity}
	 * @return {@link BatchInstance}
	 */
	BatchInstance getBatchInstanceByJobEntity(JobEntity jobEntity);

	/**
	 * Deletes a process instance by batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param isExceptionAllowed
	 * @throws DCMABusinessException
	 */
	void deleteProcessInstanceByBatchInstance(BatchInstance batchInstance, boolean isExceptionAllowed) throws DCMABusinessException;

	/**
	 * Deletes a batch instance.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return boolean
	 * @throws DCMAApplicationException
	 */
	boolean deleteBatchInstance(String batchInstanceIdentifier) throws DCMAApplicationException;

	/**
	 * Clean the resources held by a batch during its job execution.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 */
	void cleanBatchResources(String batchInstanceIdentifier);

	/**
	 * Checks if the server context path is equal to the Executing Instance Server.
	 * 
	 * @param serverContextPath {@link String}
	 * @param batchInstanceExecutingServer {@link String}
	 * @param batchInstanceServerRegistry {@link ServerRegistry}
	 * @return boolean
	 */
	boolean isSameServer(String batchInstanceExecutingServer);

	/**
	 * Sends a batch to batch status of ERROR.
	 * 
	 * @param batchIdentifier {@link String}
	 */
	void sendBatchToErrorState(String batchIdentifier);

	/**
	 * Gets current time.
	 * 
	 * @param commandContext {@link CommandContext}
	 * @return {@link Date}
	 */
	Date getCurrentTime(CommandContext commandContext);

	/**
	 * Gets is server ready for job execution or not.
	 * 
	 * @return boolean
	 */
	boolean isServerReadyForJobExecution();

	/**
	 * Sets is server ready for job execution or not.
	 * 
	 * @param serverReadyForJobExecution boolean
	 */
	void setServerReadyForJobExecution(boolean serverReadyForJobExecution);

	/**
	 * Resets job retry count.
	 * 
	 * @param jobId {@link String}
	 * @param retries int
	 */
	void resetJobRetries(String jobId, int retries);
}
