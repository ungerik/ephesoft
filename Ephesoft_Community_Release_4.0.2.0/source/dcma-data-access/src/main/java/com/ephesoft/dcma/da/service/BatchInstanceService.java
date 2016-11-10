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

package com.ephesoft.dcma.da.service;

import java.util.List;
import java.util.Set;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.EphesoftUser;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.property.BatchInstanceFilter;
import com.ephesoft.dcma.da.property.BatchPriority;

/**
 * This is a database service to read data required by Batch Instance Service.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.BatchInstanceServiceImpl
 */
public interface BatchInstanceService {

	/**
	 * An api to fetch all batch instance by batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstByBatchClass(BatchClass batchClass);

	/**
	 * An api to fetch all batch instance by review user name.
	 * 
	 * @param reviewUserName {@link String}
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstByReviewUserName(String reviewUserName);

	/**
	 * An api to fetch all batch instance by validation user name.
	 * 
	 * @param validationUserName {@link String}
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstByValidationUserName(String validationUserName);

	/**
	 * An api to fetch all batch instance by BatchInstanceStatus.
	 * 
	 * @param batchInstanceStatus {@link BatchInstanceStatus}
	 * @return List<{@link BatchInstance}>
	 */

	List<BatchInstance> getBatchInstByStatus(BatchInstanceStatus batchInstanceStatus);

	/**
	 * An api to fetch count of the batch instance table for batch instance status and batch priority. API will return those batch
	 * instance having access by the user roles on the basis of ephesoft user.
	 * 
	 * @param batchName {@link String}
	 * @param batchInstanceStatus {@link BatchInstanceStatus}
	 * @param userName {@link String}
	 * @param priority {@link BatchPriority}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return int, count of the batch instance present for the batch instance status. {@link Integer}
	 */
	int getCount(String batchName, BatchInstanceStatus batchInstanceStatus, String userName, BatchPriority priority,
			Set<String> userRoles, EphesoftUser ephesoftUser);

	/**
	 * An api to fetch all the batch instances by status list. Parameter firstResult set a limit upon the number of objects to be
	 * retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set the sort property and order of
	 * that property. If orderList parameter is null or empty then this parameter is avoided.
	 * 
	 * @param statusList List<{@link BatchInstanceStatus}> status list of batch instance status.
	 * @param firstResult the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResults maxResults the maximum number of results
	 * @param orderList List<{@link Order}> orderList set the sort property and order of that property. If orderList parameter is null
	 *            or empty then this parameter is avoided.
	 * @param filterClauseList List<{@link BatchInstanceFilter}> this will add the where clause to the criteria query based on the
	 *            property name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities List<{@link BatchPriority}> this will add the where clause to the criteria query based on the priority
	 *            list selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 * @param currentUser {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param EphesoftUser {@link String}
	 * @param searchString the identifier on which batch instances have to be fetched
	 * @return List<{@link BatchInstance}> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstances(String batchIdentifierTextSearch, String batchNameTextSearch,
			List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults, final List<Order> orderList,
			final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities, String currentUser,
			Set<String> userRoles, EphesoftUser ephesoftUser, String searchString);

	/**
	 * An api to fetch all the batch instances on the basis of BatchInstanceFilters list.
	 * 
	 * @param batchInstanceFilters List<{@link BatchInstanceFilter}> this will add the where clause to the criteria query based on the
	 *            property name and value.
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstances(final List<BatchInstanceFilter> batchInstanceFilters);

	/**
	 * An api to fetch next batch instance from batch instance table for status READY_FOR_REVIEW and READY_FOR_VALIDATION. This will
	 * have minimum priority with above restrictions and result of the query will be order by last modified. API will return those
	 * batch instance having the access by the user role on the basis of ephesoft user.
	 * 
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return {@link BatchInstance} batch instance for status READY_FOR_REVIEW and READY_FOR_VALIDATION and result of the query will
	 *         be order by last modified.
	 */
	BatchInstance getHighestPriorityBatchInstance(final Set<String> userRoles, EphesoftUser ephesoftUser);

	/**
	 * An api to fetch count of the batch instance table for batch instance filters.
	 * 
	 * @param filterClauseList List<{@link BatchInstanceFilter}>
	 * @return count of the batch instance present for the batch instance filters. {@link Integer}
	 */
	int getCount(final List<BatchInstanceFilter> filterClauseList);

	/**
	 * An api to fetch count of the batch instance table for batch instance status list, batch priority list on the basis of the user
	 * roles. API will return the count for the batch instance having access by the user roles and current user name on the basis of
	 * the ephesoft user.
	 * 
	 * @param batchInstStatusList List<{@link BatchInstanceStatus}>
	 * @param batchPriorities List<{@link BatchPriority}>
	 * @param userRoles Set<{@link String}>
	 * @param currentUserName {@link String} current logged in user name.
	 * @param ephesoftUser Enum for ephesoft user.
	 * @return int,count of the batch instance present for the batch instance status list and batch priority list.
	 */
	int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final Set<String> userRoles, final String currentUserName, EphesoftUser ephesoftUser);

	/**
	 * An api to fetch count of the batch instances for a given status list and batch priority and isCurrUsrNotReq is used for adding
	 * the batch instance access by the current user. This API will return the batch instance having access by the user roles on the
	 * basis of ephesoft user.
	 * 
	 * @param batchInstStatusList List<{@link BatchInstanceStatus}>
	 * @param batchPriorities the priority list of the batches
	 * @param isCurrUsrNotReq true if the current user can be anyone. False if current user cannot be null.
	 * @param currentUser {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @param searchString the searchString on which batch instances have to be fetched
	 * @return int, the count satisfying the above requirements
	 */
	int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final boolean isCurrUsrNotReq, final String currentUser, final Set<String> userRoles, EphesoftUser ephesoftUser,
			final String searchString);

	/**
	 * An api to return total count of batches from the batch instance table having access by the user roles on the basis of ephesoft
	 * user.
	 * 
	 * @param currentUser {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return total count {@link Integer}
	 */
	int getAllCount(final String currentUser, final Set<String> userRoles, EphesoftUser ephesoftUser);

	/**
	 * An api to fetch all the batch instance by BatchPriority.
	 * 
	 * @param batchPriority {@link BatchPriority} this will add the where clause to the criteria query based on the priority column.
	 * @return {@link List}<{@link BatchInstance}> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstance(BatchPriority batchPriority);

	/**
	 * An api to fetch all batch instance by batch Class Name.
	 * 
	 * @param batchClassName {@link String}
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstByBatchClassName(String batchClassName);

	/**
	 * An api to fetch all batch instance by batch Class Process Name.
	 * 
	 * @param batchClassProcessName {@link String}
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstByBatchClassProcessName(String batchClassProcessName);

	/**
	 * This method will create the batch instance for input batch class, unc sub folder path and local folder path.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param uncSubFolder {@link String}
	 * @param localFolder {@link String}
	 * @param priority int
	 * @return {@link BatchInstance}
	 */
	BatchInstance createBatchInstance(BatchClass batchClass, String uncSubFolder, String localFolder, int priority);

	/**
	 * This method will update the status for batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param status {@link BatchInstanceStatus}
	 */
	void updateBatchInstanceStatus(BatchInstance batchInstance, BatchInstanceStatus status);

	/**
	 * This method will update the status of batch instance for input id.
	 * 
	 * @param id {@link String}
	 * @param status {@link BatchInstanceStatus}
	 */
	void updateBatchInstanceStatusByIdentifier(String identifier, BatchInstanceStatus status);

	/**
	 * This method will update the status of batch instance for input id and status.
	 * 
	 * @param identifier {@link String}
	 * @param status {@link String}
	 */
	void updateBatchInstanceStatusByIdentifier(String identifier, String status);

	/**
	 * This method will create a new batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	void createBatchInstance(BatchInstance batchInstance);

	/**
	 * This method will update the existing batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	void updateBatchInstance(BatchInstance batchInstance);

	/**
	 * This method will remove the existing batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	void removeBatchInstance(BatchInstance batchInstance);

	/**
	 * An api to fetch all batch instance by batch Class Name and batch instance id's list.
	 * 
	 * @param batchClassName {@link String}
	 * @param batchInstanceIdentifier List<{@link String}>
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstanceList(String batchClassName, List<String> batchInstanceIdentifier);

	/**
	 * An api to fetch batch class ID for batch instance ID.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link String} batchClassID
	 */
	String getBatchClassIdentifier(String batchInstanceIdentifier);

	/**
	 * API to fetch BatchInstance on the basis of batchInstanceIdentifier
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link BatchInstance}
	 */
	BatchInstance getBatchInstanceByIdentifier(String batchInstanceIdentifier);

	/**
	 * API to acquire a batch on the basis of batchInstanceIdentifier and userName.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param userName {@link String}
	 * 
	 * @return {@link BatchInstance}
	 */
	BatchInstance acquireBatchInstance(String batchInstanceIdentifier, String currentUser) throws BatchAlreadyLockedException;

	/**
	 * Unlocks the currently acquired batch by the user (currentUser).
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * 
	 */
	void unlockCurrentBatchInstance(String batchInstanceIdentifier);

	/**
	 * Unlocks all the batches acquired by the user.
	 * 
	 * @param currentUser {@link String}
	 */
	void unlockAllBatchInstancesForCurrentUser(String currentUser);

	/**
	 * API to get all batch instances which are currently being executed for a specific IP Address. Output will be all batch instances
	 * which are not in NEW, ERROR or FINISHED state.
	 * 
	 * @param lockOwner {@link List}
	 * @return
	 */
	List<BatchInstance> getRunningBatchInstancesForServer(String lockOwner);

	/**
	 * API to update the batch instance status.
	 * 
	 * @param identifier {@link String}
	 * @param status {@link BatchInstanceStatus}
	 */
	void updateBatchInstanceStatus(String identifier, BatchInstanceStatus status);

	/**
	 * API to get all the batch instances in sorted order.
	 * 
	 * @param order List<{@link Order}>
	 * @return List<{@link BatchInstance}> - List of all batch instances.
	 */
	List<BatchInstance> getAllBatchInstances(List<Order> order);

	/**
	 * API to get all the batch instances by matching Batch Name or batch identifier.
	 * 
	 * @param searchString {@link String}
	 * @param userRoles Set<{@link String}>
	 * @return List<{@link BatchInstance}> - List of all batch instances.
	 */
	List<BatchInstance> getBatchInstancesByBatchNameOrId(String searchString, Set<String> userRoles);

	/**
	 * API to return all unfinished batch instances.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @return List<{@link BatchInstance}> - List of all unfinished batch instances.
	 */
	List<BatchInstance> getAllUnFinishedBatchInstances(BatchClass batchClass);

	/**
	 * API to merge batch instance and returning updated batch instance.
	 * 
	 * @param {@link BatchInstance}
	 * @return BatchInstance {@link BatchInstance}
	 */
	BatchInstance merge(BatchInstance batchInstance);

	/**
	 * API to get all unfinshedRemotelyExecutedBatchInstance.
	 * 
	 * @return List<{@link BatchInstance}> - List of batch instances.
	 */
	List<BatchInstance> getAllUnfinshedRemotelyExecutedBatchInstance();

	/**
	 * An api to fetch all the batch instances excluding remotely executing batches by status list. Parameter firstResult set a limit
	 * upon the number of objects to be retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set
	 * the sort property and order of that property. If orderList parameter is null or empty then this parameter is avoided. This will
	 * return only those batch instance which having access by the user roles on the basis of the ephesoft user.
	 * 
	 * @param batchNameToBeSearched {@link String}
	 * @param statusList List<{@link BatchInstanceStatus}> status list of batch instance status.
	 * @param firstResult the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResults maxResults the maximum number of results
	 * @param orderList List<{@link Order}> orderList set the sort property and order of that property. If orderList parameter is null
	 *            or empty then this parameter is avoided.
	 * @param filterClauseList List<{@link BatchInstanceFilter}> this will add the where clause to the criteria query based on the
	 *            property name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities List<{@link BatchPriority}> this will add the where clause to the criteria query based on the priority
	 *            list selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 * @param currentUser {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return List<{@link BatchInstance}> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstancesExcludedRemoteBatch(final String batchNameToBeSearched, List<BatchInstanceStatus> statusList,
			final int firstResult, final int maxResults, final List<Order> orderList,
			final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities, String currentUser,
			Set<String> userRoles, EphesoftUser ephesoftUser);

	/**
	 * An api to fetch all the batch instances remotely executing batches by status list. Parameter firstResult set a limit upon the
	 * number of objects to be retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set the sort
	 * property and order of that property. If orderList parameter is null or empty then this parameter is avoided.
	 * 
	 * @param statusList List<{@link BatchInstanceStatus}> status list of batch instance status.
	 * @param firstResult the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResults maxResults the maximum number of results
	 * @param orderList List<{@link Order}> orderList set the sort property and order of that property. If orderList parameter is null
	 *            or empty then this parameter is avoided.
	 * @param filterClauseList List<{@link BatchInstanceFilter}> this will add the where clause to the criteria query based on the
	 *            property name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities List<{@link BatchPriority}> this will add the where clause to the criteria query based on the priority
	 *            list selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 * @param currentUser {@link String}
	 * @param userRoles Set<{@link String}>
	 * @return List<{@link BatchInstance}> return the batch instance list.
	 */
	List<BatchInstance> getRemoteBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities,
			String currentUser, Set<String> userRoles);

	/**
	 * This API returns the list of running job executing on the server by the server IP.
	 * 
	 * @param serverIP {@link String}
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getExecutingBatchesByServerIP(String serverIP);

	/**
	 * This API returns the list of batch instance on the basis of executing server IP and batch status.
	 * 
	 * @param executingServerIP {@link String}
	 * @param batchInstanceStatus {@link BatchInstanceStatus}
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstanceByExecutingServerAndStatus(String executingServerIP, BatchInstanceStatus batchInstanceStatus);

	/**
	 * API to evict/remove the batch instance from session.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 */
	void evict(BatchInstance batchInstance);

	/**
	 * This API returns the list of batch instances for current user by matching batch name and batch status.
	 * 
	 * @param batchName {@link String} batch name to be searched
	 * @param userName {@link String} current user
	 * @param batchStatus {@link BatchInstanceStatus} batch status to be matched
	 * @param userRoles Set<{@link String}>
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstanceListByBatchNameAndStatus(String batchName, BatchInstanceStatus batchStatus, String userName,
			Set<String> userRoles);

	/**
	 * API to clear current user for a batch instance given it's identifier.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 */
	void clearCurrentUser(String batchInstanceIdentifier);

	/**
	 * This API fetches all the batch instances on the basis of batch status list passed.
	 * 
	 * @param batchStatusList List<{@link BatchInstanceStatus}>
	 * @return List<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstanceByStatusList(List<BatchInstanceStatus> batchStatusList);

	/**
	 * This API fetches batch instances which having access by the user roles on the basis of ephesoft user.
	 * 
	 * @param userRoles {@link Set}<{@link String} >
	 * @param batchInstanceIdentifier {@link String}
	 * @param currentUserName {@link String}
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return BatchInstance {@link BatchInstance}
	 */
	BatchInstance getBatchInstanceByUserRole(Set<String> userRoles, String batchInstanceIdentifier, String currentUserName,
			EphesoftUser ephesoftUser);

	/**
	 * This API fetches over all the batch instances on the basis of status, priority and for the given user role.
	 * 
	 * @param statusList List<{@link BatchInstanceStatus}>
	 * @param batchPriorities List<{@link BatchPriority}>
	 * @param userRoles Set<{@link String}>
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstancesForStatusPriority(List<BatchInstanceStatus> statusList, List<BatchPriority> batchPriorities,
			Set<String> userRoles);

	/**
	 * This API fetches the roles for a batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return {@link Set}< {@link String}>
	 */
	Set<String> getRolesForBatchInstance(BatchInstance batchInstance);

	/**
	 * This API performs a fetch all the batch instance on the basis of the staus and batchclass.
	 * 
	 * @param statusList {@link List}<@link BatchInstanceStatus>
	 * @param batchClass {@link BatchClass}
	 * @return {@link List}<{@link BatchInstance}>
	 */
	public List<BatchInstance> getBatchInstByStatusAndBatchClass(List<BatchInstanceStatus> statusList, BatchClass batchClass);

	/**
	 * Gets the batch instance list filtered as per the arguments supplied.
	 * 
	 * @param batchInstStatusList List<{@link BatchInstanceStatus}>
	 * @param batchPriorities the priority list of the batches {@link BatchPriority}
	 * @param isNotCurrentUserCheckReq true if the current user can be anyone. False if current user cannot be null.
	 * @param userRoles Set<{@link String}>
	 * @param currentUserName {@link String}
	 * @param ephesoftUser {@link EphesoftUser}
	 * @param searchString the searchString on which batch instances have to be fetched
	 * @return List<BatchInstance> returns the batch instance list filtered as per the information passed as arguments
	 */
	List<BatchInstance> getBatchInstanceList(List<BatchInstanceStatus> batchInstStatusList, List<BatchPriority> batchPriorities,
			boolean isNotCurrentUserCheckReq, Set<String> userRoles, String currentUserName, EphesoftUser ephesoftUser,
			String searchString);

	/**
	 * Gets the list of active batches statuses i.e., batches excluding deleted and finished.
	 * 
	 * @return {@link List}<{@link BatchInstanceStatus}> list of batch instance status.
	 */
	List<BatchInstanceStatus> getActiveBatchStatusList();

	/**
	 * It is used to get an batch instance from the database with read committed isolation.
	 * 
	 * @param batchId long
	 * @return {@link BatchInstance} latest read from database.
	 */
	BatchInstance getBatch(long batchId);

	/**
	 * Gets all running batches on all servers.
	 * 
	 * @return {@link List}<{@link BatchInstanceStatus}>
	 */
	List<BatchInstance> getAllRunningBatches();

	/**
	 * Method to get BatchInstancePriority by identifier.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return batch instance priority{@link Integer}
	 */
	int getBatchInstancePriorityByIdentifier(String batchInstanceIdentifier);

	/**
	 * Gets reloaded batch class.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return BatchClass{@link BatchClass}
	 */
	BatchClass getLoadedBatchClassByBatchIdentifier(final BatchInstance batchInstance);

	/**
	 * Clears mapping of Plugin Properties and BackupXML maps for given Batch Instance ID.
	 * 
	 * @param batchInstanceId {@link String}
	 */
	void clearInMemoryMapByBatchIdentifier(final String batchInstanceId);

	/**
	 * Gets list of filtered batch instances from the provided batch instance list based on the batch class name provided.
	 * 
	 * @param batchInstanceList {@link List}<{@link BatchInstance}>
	 * @param batchClassFilterText {@link String}
	 * @return batchInstanceList {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> filterBatchInstanceByBatchClassName(final List<BatchInstance> batchInstanceList,
			final String batchClassFilterText);
}
