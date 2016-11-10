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

package com.ephesoft.dcma.da.dao;

import java.util.List;
import java.util.Set;

import org.hibernate.LockMode;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.EphesoftUser;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.dao.Dao;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.property.BatchInstanceFilter;
import com.ephesoft.dcma.da.property.BatchPriority;

/**
 * This class is responsible to fetch data of batch instance table from data base.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface BatchInstanceDao extends Dao<BatchInstance> {

	/**
	 * An api to fetch all batch instances by matching batch name or batch identifier.
	 * 
	 * @param searchString {@link String}
	 * @param userRoles {@link Set}<{@link String}>
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstancesByBatchNameOrId(String searchString, Set<String> userRoles);

	/**
	 * An api to fetch a single batch instance by batch identifier.
	 * 
	 * @param identifier {@link String}
	 * @return {@link BatchInstance}
	 */
	BatchInstance getBatchInstancesForIdentifier(String identifier);

	/**
	 * An api to fetch all batch instance by batch class.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @return {@link List}<{@link BatchInstance}>
	 */

	List<BatchInstance> getBatchInstByBatchClass(BatchClass batchClass);

	/**
	 * An api to fetch all batch instance by review user name.
	 * 
	 * @param reviewUserName {@link String}
	 * @return {@link List}<{@link BatchInstance}>
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
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstByStatus(BatchInstanceStatus batchInstanceStatus);

	/**
	 * An api to fetch the count of the batch instances on batch instance status and batch priority basis. API will return those batch
	 * instance having access by the user roles on the basis of ephesoft user.
	 * 
	 * @param batchName {@link String}
	 * @param batchInstanceStatus {@link BatchInstanceStatus}
	 * @param userName {@link String}
	 * @param priority {@link BatchPriority}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return {@link Integer}
	 */
	int getCount(String batchName, BatchInstanceStatus batchInstanceStatus, String userName, BatchPriority priority,
			Set<String> userRoles, EphesoftUser ephesoftUser);

	/**
	 * An api to fetch all the batch instances by status list. Parameter firstResult set a limit upon the number of objects to be
	 * retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set the sort property and order of
	 * that property. If orderList parameter is null or empty then this parameter is avoided.
	 * 
	 * @param batchIdentifierTextSearch {@link String}
	 * @param batchNameTextSearch {@link String}
	 * @param statusList {@link List}<{@link BatchInstanceStatus}> status list of batch instance status.
	 * @param firstResult {@link Integer}the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResults {@link Integer} the maximum number of results
	 * @param orderList {@link List}<{@link Order}> orderList set the sort property and order of that property. If orderList parameter
	 *            is null or empty then this parameter is avoided.
	 * @param filterClauseList {@link List}<{@link BatchInstanceFilter}> this will add the where clause to the criteria query based on
	 *            the property name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities {@link List}<{@link BatchPriority}> this will add the where clause to the criteria query based on the
	 *            priority list selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 * @param userName {@link String}
	 * @param userRoles {@link Set}<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @param searchString {@link String} the searchString on which batch instances have to be fetched
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstances(String batchIdentifierTextSearch, String batchNameTextSearch,
			List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults, final List<Order> orderList,
			final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities, String userName,
			Set<String> userRoles, EphesoftUser ephesoftUser, String searchString);

	/**
	 * An api to fetch all the batch instance for status list input. API will return those batch instance having access by the user
	 * roles on the basis of ephesoft user.
	 * 
	 * @param statusList {@link List}<{@link BatchInstanceStatus}>
	 * @param firstResult {@link Integer}
	 * @param maxResults {@link Integer}
	 * @param userRoles {@link Set}<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final Set<String> userRoles, EphesoftUser ephesoftUser);

	/**
	 * An api to fetch count of the batch instance table for batch instance filters.
	 * 
	 * @param filterClauseList {@link List}<{@link BatchInstanceFilter}>
	 * @return {@link Integer} count of the batch instance present for the batch instance filters.
	 */
	int getCount(final List<BatchInstanceFilter> filterClauseList);

	/**
	 * An api to fetch count of the batch instance table for batch instance status list, batch priority list on the basis of the user
	 * roles. API will return the count for the batch instance having access by the user roles and current user name on the basis of
	 * the ephesoft user.
	 * 
	 * @param batchInstStatusList {@link List}<{@link BatchInstanceStatus}>
	 * @param batchPriorities {@link List}<{@link BatchPriority}>
	 * @param userRoles {@link Set}<{@link String}>
	 * @param currentUserName {@link String} current logged in user name.
	 * @param ephesoftUser {@link EphesoftUser} Enum for ephesoft user.
	 * @return {@link Integer} ,count of the batch instance present for the batch instance status list and batch priority list.
	 */
	int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final Set<String> userRoles, final String currentUserName, EphesoftUser ephesoftUser);

	/**
	 * An api to fetch count of the batch instances for a given status list and batch priority and isCurrUsrNotReq is used for adding
	 * the batch instance access by the current user. This API will return the batch instance having access by the user roles on the
	 * basis of ephesoft user.
	 * 
	 * @param batchInstStatusList {@link List}<{@link BatchInstanceStatus}>
	 * @param batchPriorities {@link List}<{@link BatchPriority}> the priority list of the batches	 * @param isCurrUsrNotReq boolean : true if the current user can be anyone. False if current user cannot be null.
	 * @param userRoles {@link Set}<{@link String}>
	 * @param currentUserName {@link String}
	 * @param ephesoftUser {@link EphesoftUser}
	 * @param searchString {@link String} the searchString on which batch instances have to be fetched
	 * @return {@link Integer}, the count satisfying the above requirements
	 */
	int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final boolean isCurrUsrNotReq, final Set<String> userRoles, final String currentUserName, EphesoftUser ephesoftUser,
			final String searchString);

	/**
	 * An api to return total count of batches from the batch instance table having access by the user roles on the basis of ephesoft
	 * user.
	 * 
	 * @param currentUser {@link String}
	 * @param currentRole {@link Set}<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return {@link Integer}, total count
	 */
	int getAllCount(final String currentUser, final Set<String> currentRole, EphesoftUser ephesoftUser);

	/**
	 * An api to fetch all the batch instance by BatchPriority.
	 * 
	 * @param batchPriority {@link BatchPriority} BatchPriority this will add the where clause to the criteria query based on the
	 *            priority column.
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
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstByBatchClassProcessName(String batchClassProcessName);

	/**
	 * This method will create the batch instance for input batch class, unc sub folder path and local folder path.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param uncSubFolder {@link String}
	 * @param localFolder {@link String}
	 * @param priority int
	 * @return BatchInstance {@link BatchInstance}
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
	 * @param batchInstanceIDList {@link List}<{@link String}>
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstanceList(String batchClassName, List<String> batchInstanceIDList);

	/**
	 * API to fetch BatchInstance for the current user.
	 * 
	 * @param currentUser {@link String}
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getAllBatchInstancesForCurrentUser(String currentUser);

	/**
	 * API to get all batch instances which are currently being executed for a specific IP Address. Output will be all batch instances
	 * which are not in NEW, ERROR or FINISHED state.
	 * 
	 * @param lockOwner {@link String}
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getRunningBatchInstancesForServer(String lockOwner);

	/**
	 * API to get a batch by applying Hibernate level optimistic locking.
	 * 
	 * @param batchId integer
	 * @param lockMode {@link LockMode}
	 * @return {@link BatchInstance}
	 */
	BatchInstance getBatch(long batchId, LockMode lockMode);

	/**
	 * API to get all the batch instances in sorted order.
	 * 
	 * @param orders {@link List}<{@link Order}>
	 * @return {@link List}<{@link BatchInstance}> List of all batch instances.
	 */
	List<BatchInstance> getAllBatchInstances(List<Order> orders);

	/**
	 * API to get all unfinished batch instances.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getAllUnFinishedBatchInstances(BatchClass batchClass);

	/**
	 * API to update local folder for a batch.
	 * 
	 * @param batchInsctance {@link BatchInstance}
	 * @param localFolder {@link String}
	 */
	void updateBatchInstanceLocalFolder(BatchInstance batchInsctance, String localFolder);

	/**
	 * API to update unc folder for a batch.
	 * 
	 * @param batchInsctance {@link BatchInstance}
	 * @param uncFolder {@link String}
	 */
	void updateBatchInstanceUncFolder(BatchInstance batchInsctance, String uncFolder);

	/**
	 * API to get all unfinished remotely executed batches.
	 * 
	 * @return {@link List}<{@link BatchInstance}>
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
	 * @param orderList {@link List}<{@link Order}> orderList set the sort property and order of that property. If orderList parameter
	 *            is null or empty then this parameter is avoided.
	 * @param filterClauseList {@link List}<{@link BatchInstanceFilter}> this will add the where clause to the criteria query based on
	 *            the property name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities {@link List}<{@link BatchPriority}> this will add the where clause to the criteria query based on the
	 *            priority list selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 * @param userName {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return {@link List}<{@link BatchInstance}> return the batch instance list.
	 */
	List<BatchInstance> getBatchInstancesExcludedRemoteBatch(final String batchNameToBeSearched, List<BatchInstanceStatus> statusList,
			final int firstResult, final int maxResults, final List<Order> orderList,
			final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities, String userName,
			final Set<String> userRoles, EphesoftUser ephesoftUser);

	/**
	 * An api to fetch all the batch instances only remotely executing batches by status list. Parameter firstResult set a limit upon
	 * the number of objects to be retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set the
	 * sort property and order of that property. If orderList parameter is null or empty then this parameter is avoided.
	 * 
	 * @param statusList {@link List}<{@link BatchInstanceStatus}> status list of batch instance status.
	 * @param firstResult the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResults maxResults the maximum number of results
	 * @param orderList {@link List}<{@link Order}> orderList set the sort property and order of that property. If orderList parameter
	 *            is null or empty then this parameter is avoided.
	 * @param filterClauseList {@link List}<{@link BatchInstanceFilter}> this will add the where clause to the criteria query based on
	 *            the property name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities {@link List}<{@link BatchPriority}> this will add the where clause to the criteria query based on the
	 *            priority list selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 * @param userName {@link String} Current User.
	 * @param userRoles {@link Set}<{@link String}> Current User Role.
	 * @return {@link List}<{@link BatchInstance}> return the batch instance list.
	 */
	List<BatchInstance> getRemoteBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities,
			String userName, final Set<String> userRoles);

	/**
	 * Returns the list of running job executing on the server by the server IP.
	 * 
	 * @param serverIP {@link String}
	 * @return {@link List}<{@list BatchInstance}>
	 */
	List<BatchInstance> getExecutingBatchesByServerIP(String serverIP);

	/**
	 * This API returns the list of batch instance on the basis of executing server IP and batch status.
	 * 
	 * @param executingServer {@link String}
	 * @param batchInstanceStatus {@link BatchInstanceStatus}
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstanceByExecutingServerAndStatus(String executingServer, BatchInstanceStatus batchInstanceStatus);

	/**
	 * This API returns the list of batch instances for current user by matching batch name and batch status.
	 * 
	 * @param batchName {@link String}
	 * @param batchStatus {@link BatchInstanceStatus}
	 * @param userName {@link String}
	 * @param userRoles {@link Set}<{@link String}>
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstanceListByBatchNameAndStatus(String batchName, BatchInstanceStatus batchStatus, String userName,
			Set<String> userRoles);

	/**
	 * This API for clearing the current user for given batch instance identifier.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 */
	void clearCurrentUser(String batchInstanceIdentifier);

	/**
	 * This API fetches all the batch instances on the basis of batch status list passed.
	 * 
	 * @param batchStatusList List<{@link BatchInstanceStatus}>
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstanceByStatusList(List<BatchInstanceStatus> batchStatusList);

	/**
	 * This API fetches batch instances which having access by the user roles on the basis of ephesoft user.
	 * 
	 * @param userRoles {@link Set}<{@link String}>
	 * @param batchInstanceIdentifier {@link String}
	 * @param currentUserName {@link String}
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return {@link BatchInstance}
	 */
	BatchInstance getBatchInstanceByUserRole(Set<String> userRoles, String batchInstanceIdentifier, String currentUserName,
			EphesoftUser ephesoftUser);

	/**
	 * This API performs a fetch over all the batch instances on the basis of status, priority and for the given user role.
	 * 
	 * @param statusList {@link List}<{@link BatchInstanceStatus}> batch status list
	 * @param batchPriorities {@link List}<{@link BatchPriority}> batch priorities list
	 * @param userRoles batch class id's for the role for which the current user is logged in
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstancesForStatusPriority(List<BatchInstanceStatus> statusList, List<BatchPriority> batchPriorities,
			Set<String> userRoles);

	/**
	 * This API performs a fetch all the batch instance on the basis of the staus and batchclass.
	 * 
	 * @param statusList {@link List}<{@link BatchInstanceStatus}>
	 * @param batchClass {@link BatchClass}
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getBatchInstByStatusAndBatchClass(List<BatchInstanceStatus> statusList, BatchClass batchClass);

	/**
	 * Gets the batch instance list filtered as per the arguments supplied.
	 * 
	 * @param batchInstStatusList {@link List}<{@link BatchInstanceStatus}>
	 * @param batchPriorities {@link List}<{@link BatchPriority}> the priority list of the batches
	 * @param isNotCurrentUserCheckReq boolean :true if the current user can be anyone. False if current user cannot be null.
	 * @param userRoles {@link Set}<{@link String}>
	 * @param currentUserName {@link String}
	 * @param ephesoftUser {@link EphesoftUser}
	 * @param searchString {@link String} the searchString on which batch instances have to be fetched
	 * @return {@link List}<{@link BatchInstance}> returns the batch instance list filtered as per the information passed as arguments
	 */
	List<BatchInstance> getBatchInstanceList(List<BatchInstanceStatus> batchInstStatusList, List<BatchPriority> batchPriorities,
			boolean isNotCurrentUserCheckReq, Set<String> userRoles, String currentUserName, EphesoftUser ephesoftUser,
			String searchString);

	/**
	 * Gets running batches by all servers.
	 * 
	 * @return {@link List}<{@link BatchInstance}>
	 */
	List<BatchInstance> getAllRunningBatches();

}
