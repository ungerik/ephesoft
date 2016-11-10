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

package com.ephesoft.dcma.workflows.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;

/**
 * This interface represents the service for workflow execution.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 29-Oct-2014 <br/>
 * @version 1.0 $LastChangedDate: 2015-04-14 12:42:19 +0530 (Tue, 14 Apr 2015) $ <br/>
 *          $LastChangedRevision: 21594 $ <br/>
 */
public interface WorkflowService {

	/**
	 * Gets active module workflow name for a batch instance.
	 * 
	 * @param batchInstance {@link String}
	 * @return {@link String}
	 */
	String getActiveModuleName(String batchInstanceId);

	/**
	 * Gets active plugin workflow for a batch instance.
	 * 
	 * @param batchInstanceId {@link String}
	 * @return {@link String}
	 */
	String getActivePluginWorkflow(String batchInstanceId);

	/**
	 * Gets last executed module for for Error Status batch instance.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link String}
	 */
	String getErrorBatchLastExecutedModule(String batchInstanceIdentifier);

	/**
	 * Starts workflow for a batch instance.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param module {@link String}
	 * @return boolean
	 */
	boolean startWorkflow(BatchInstanceID batchInstanceID, String module);

	/**
	 * Signals workflow for a batch in a pause state.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return boolean
	 * @throws DCMAApplicationException
	 */
	boolean signalWorkflow(BatchInstance batchInstance) throws DCMAApplicationException;

	/**
	 * Clears current user of the batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	void clearCurrentUser(BatchInstance batchInstance);

	/**
	 * Signals workflow for a batch in a pause state.
	 * 
	 * @param batchId {@link String}
	 * @throws DCMAApplicationException
	 */
	void signalWorkflow(String batchId) throws DCMAApplicationException;

	/**
	 * Updates batch instance status for Review/Validate states.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param status {@link BatchInstanceStatus}
	 */
	void updateBatchInstanceStatusForReviewAndValidation(BatchInstanceID batchInstanceID, BatchInstanceStatus status);

	/**
	 * Gets workflow name by module name.
	 * 
	 * @param moduleName {@link String}
	 * @param batchClassModuleList {@link List}<{@link BatchClassModule}>
	 * @return {@link String}
	 */
	String getWorkflowNameByModuleName(String moduleName, List<BatchClassModule> batchClassModuleList);

	/**
	 * Handles cleaning and updates for all error batches.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param exception {@link Exception}
	 * @param exceptionMessage {@link String}
	 */
	void handleErrorBatch(BatchInstance batchInstance, Exception exception, String exceptionMessage);

	/**
	 * Reads total execution capacity for all servers that are active
	 * 
	 * @return {@link Map}<{@link Set}<{@link Integer}>, {@link Integer}>
	 */
	Map<Set<Integer>, Integer> getAllServerExecutionCapacity();

	/**
	 * Sends an error notification mail when a batch instance is send into error.
	 * 
	 * @param {@link BatchInstance} batchInstance
	 * @param {@link Exception} exception
	 */
	public void mailOnError(final BatchInstance batchInstance, final Exception exception) throws DCMAApplicationException;

	/**
	 * Creates a backup XML before cleanup
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 */
	void createBackupBeforeCleanup(BatchInstanceID batchInstanceID);

	String getWorkflowDetails(String batchInstanceIdentifier);

	/**
	 * Signals workflow for a batch in a pause state.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @throws DCMAApplicationException
	 */
	void signalWorkflow(String batchInstanceIdentifier, String userName) throws DCMAApplicationException;

	/**
	 * Gets an empty map of for all batch priorities.
	 * 
	 * @return {@link Map}<{@link Integer},{@link List}<{@link Integer}>>
	 */
	Map<Integer, List<Integer>> getBatchPriorityCapacityMap();

	/**
	 * Gets map of batch priority with running batches by all servers.
	 * 
	 * @return {@link Map}<{@link String}, {@link Integer}>
	 */
	Map<Integer, Integer> getBatchPriorityRunningCount();

	/**
	 * Gets remaining capacity for a priority from all servers.
	 * 
	 * @param priority int
	 * @param priorityServerMaxCapacity {@link Map}<{@link Set}<{@link Integer}>,{@link Integer}>
	 * @param serverRunningBatchCount
	 * @return
	 */
	int getRemainingCapacityForPriority(final int priority, final Map<Set<Integer>, Integer> priorityServerMaxCapacity,
			final Map<Integer, Integer> serverRunningBatchCount);
}
