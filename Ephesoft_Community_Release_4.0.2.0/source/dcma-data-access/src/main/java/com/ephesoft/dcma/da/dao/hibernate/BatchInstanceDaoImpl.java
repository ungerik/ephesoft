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

package com.ephesoft.dcma.da.dao.hibernate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.SerializationException;
import org.apache.commons.lang.SerializationUtils;
import org.hibernate.LockMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.EphesoftUser;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.core.hibernate.EphesoftCriteria;
import com.ephesoft.dcma.da.dao.BatchClassDao;
import com.ephesoft.dcma.da.dao.BatchClassGroupsDao;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.dao.BatchInstanceGroupsDao;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.property.BatchInstanceFilter;
import com.ephesoft.dcma.da.property.BatchInstanceProperty;
import com.ephesoft.dcma.da.property.BatchPriority;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;

/**
 * This class is responsible to fetch data of batch instance table from data base.
 * 
 * @author Ephesoft
 * @version 1.0
 */
@Repository
public class BatchInstanceDaoImpl extends HibernateDao<BatchInstance> implements BatchInstanceDao {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	private static final String BATCH_CLASS_PROCESS_NAME = "batchClass.processName";
	private static final String BATCH_CLASS_NAME = "batchClass.name";
	private static final String VALIDATION_USER_NAME = "validationUserName";
	private static final String REVIEW_USER_NAME = "reviewUserName";
	private static final String BATCH_NAME = "batchName";
	private static final String BATCH_CLASS_IDENTIFIER = "batchClass.identifier";
	private static final String CURRENT_USER = "currentUser";
	private static final String IS_REMOTE = "isRemote";
	private static final String PRIORITY = "priority";
	private static final String STATUS = "status";
	private static final String BATCH_CLASS = "batchClass";
	private static final String EXECUTING_SERVER = "executingServer";
	private static final String BATCH_INSTANCE_IDENTIFIER = "identifier";
	private static final String LAST_MODIFIED = "lastModified";
	// private static final String UNC_SUB_FOLDER = "uncSubfolder";
	private static final String BID_SER_FILE_NAME = "BID_ASSO";
	private static final String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();

	/**
	 * Instance of {@link BatchInstanceGroupsDao}.
	 */
	@Autowired
	BatchInstanceGroupsDao batchInstanceGroupsDao;

	/**
	 * Instance of {@link BatchClassGroups}.
	 */
	@Autowired
	BatchClassGroupsDao batchClassGroupsDao;

	/**
	 * Instance of {@link BatchClassDao}.
	 */
	@Autowired
	BatchClassDao batchClassDao;

	/**
	 * An api to fetch all batch instance by batch class.
	 * 
	 * @param batchClass BatchClass BatchClass
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByBatchClass(BatchClass batchClass) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(BATCH_CLASS, batchClass));
		return find(criteria);
	}

	@Override
	public List<BatchInstance> getBatchInstancesByBatchNameOrId(final String searchString, final Set<String> userRoles) {
		EphesoftCriteria criteria = criteria();

		List<BatchInstance> batchInstances = new ArrayList<BatchInstance>();

		Set<String> batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);

		String searchStringLocal = searchString.replaceAll("%", "\\\\%");
		Criterion nameLikeCriteria = Restrictions.like(BATCH_NAME, "%" + searchStringLocal + "%");
		Criterion idLikeCriteria = Restrictions.like(BATCH_INSTANCE_IDENTIFIER, "%" + searchStringLocal + "%");

		LogicalExpression searchCriteria = Restrictions.or(nameLikeCriteria, idLikeCriteria);
		criteria.add(searchCriteria);

		// Fixed for JIRA Issues - 7645,7761,7768.
		batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);
		Set<String> batchInstanceIdentifierSet = batchInstanceGroupsDao.getBatchInstanceIdentifiersExceptUserRoles(userRoles);
		Set<String> batchInstanceIdentifiers = batchInstanceGroupsDao.getBatchInstanceIdentifierForUserRoles(userRoles);
		batchInstanceIdentifierSet.removeAll(batchInstanceIdentifiers);

		if ((null != batchClassIdentifiers && batchClassIdentifiers.size() > 0)
				|| (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0)) {
			criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
			// criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifiers));
			// Fixed for JIRA ID - 6238. Order by default is set to descending.
			Order defaultOrder = new Order(BatchInstanceProperty.ID, false);
			batchInstances = find(criteria, defaultOrder);
			if (batchInstances != null && !batchInstances.isEmpty()) {
				batchInstances = updateBatchInstanceList(batchInstances, batchClassIdentifiers, batchInstanceIdentifiers,
						batchInstanceIdentifierSet, 0, batchInstances.size());
			}
		}
		return batchInstances;
	}

	@Override
	public BatchInstance getBatchInstancesForIdentifier(String identifier) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq("identifier", identifier));
		return findSingle(criteria);

	}

	/**
	 * An api to fetch all batch instance by review user name.
	 * 
	 * @param reviewUserName String
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByReviewUserName(String reviewUserName) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(REVIEW_USER_NAME, reviewUserName));
		return find(criteria);
	}

	/**
	 * An api to fetch all batch instance by validation user name.
	 * 
	 * @param validationUserName String
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByValidationUserName(String validationUserName) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(VALIDATION_USER_NAME, validationUserName));
		return find(criteria);
	}

	/**
	 * An api to fetch all batch instance by BatchInstanceStatus.
	 * 
	 * @param batchInstanceStatus BatchInstanceStatus
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByStatus(BatchInstanceStatus batchInstanceStatus) {
		EphesoftCriteria criteria = criteria();
		criteria.add(Restrictions.eq(STATUS, batchInstanceStatus));
		List<Order> orderList = new ArrayList<Order>();
		Order orderForHighestBatchPriority = new Order(BatchInstanceProperty.PRIORITY, true);
		Order orderForLastModified = new Order(BatchInstanceProperty.ID, true);
		orderList.add(orderForLastModified);
		orderList.add(orderForHighestBatchPriority);
		return find(criteria, orderList.toArray(new Order[orderList.size()]));
	}

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
	 * @return int, count of the batch instance present for the batch instance status.
	 */
	@Override
	public int getCount(String batchName, BatchInstanceStatus batchInstanceStatus, String userName, BatchPriority batchPriority,
			Set<String> userRoles, EphesoftUser ephesoftUser) {
		int count = 0;
		EphesoftCriteria criteria = criteria();
		if (batchName != null && !batchName.isEmpty()) {
			String batchNameLocal = batchName.replaceAll("%", "\\\\%");

			// Criteria added for search either for batch name or batch identifier(5723)
			Criterion nameLikeCriteria = Restrictions.like(BATCH_NAME, "%" + batchNameLocal + "%");
			Criterion idLikeCriteria = Restrictions.like(BATCH_INSTANCE_IDENTIFIER, "%" + batchNameLocal + "%");

			LogicalExpression searchCriteria = Restrictions.or(nameLikeCriteria, idLikeCriteria);
			criteria.add(searchCriteria);
		} else if (null != batchPriority) {
			Disjunction disjunction = Restrictions.disjunction();
			Integer lowValue = batchPriority.getLowerLimit();
			Integer upperValue = batchPriority.getUpperLimit();
			disjunction.add(Restrictions.between(PRIORITY, lowValue, upperValue));
			criteria.add(disjunction);
		}
		Set<String> batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);

		switch (ephesoftUser) {
			case NORMAL_USER:
				Set<String> batchInstanceIdentifiers = batchInstanceGroupsDao.getBatchInstanceIdentifierForUserRoles(userRoles);
				Set<String> batchInstanceIdentifierSet = batchInstanceGroupsDao.getBatchInstanceIdentifiersExceptUserRoles(userRoles);
				batchInstanceIdentifierSet.removeAll(batchInstanceIdentifiers);

				if ((null != batchClassIdentifiers && batchClassIdentifiers.size() > 0)
						|| (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0)) {
					criteria.add(Restrictions.eq(STATUS, batchInstanceStatus));
					criteria.add(Restrictions.eq(IS_REMOTE, false));
					criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, userName)));

					// Fixed against JIRA-1018 User not able to navigate through batch list.
					List<BatchInstance> batchInstanceList = find(criteria);
					batchInstanceList = updateBatchInstanceList(batchInstanceList, batchClassIdentifiers, batchInstanceIdentifiers,
							batchInstanceIdentifierSet, 0, batchInstanceList.size());
					count = batchInstanceList.size();
				}
				break;
			default:
				if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0) {
					criteria.add(Restrictions.eq(STATUS, batchInstanceStatus));
					criteria.add(Restrictions.eq(IS_REMOTE, false));
					criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, userName)));
					criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
					criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifiers));
					count = count(criteria);
				}
				break;
		}
		return count;

	}

	/**
	 * An api to fetch all the batch instances by status list. Parameter firstResult set a limit upon the number of objects to be
	 * retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set the sort property and order of
	 * that property. If orderList parameter is null or empty then this parameter is avoided.
	 * 
	 * @param statusList List<BatchInstanceStatus> status list of batch instance status.
	 * @param firstResult the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResults maxResults the maximum number of results
	 * @param orderList List<Order> orderList set the sort property and order of that property. If orderList parameter is null or empty
	 *            then this parameter is avoided.
	 * @param filterClauseList List<BatchInstanceFilter> this will add the where clause to the criteria query based on the property
	 *            name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities List<BatchPriority> this will add the where clause to the criteria query based on the priority list
	 *            selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 * @param userName Current user name.
	 * @param currentUserRoles
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Override
	public List<BatchInstance> getBatchInstances(String batchIdentifierTextSearch, String batchNameTextSearch,
			List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults, final List<Order> orderList,
			final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities, String userName,
			final Set<String> userRoles, EphesoftUser ephesoftUser, String identifier) {
		return getBatchInstances(batchIdentifierTextSearch, batchNameTextSearch, statusList, firstResult, maxResults, orderList,
				filterClauseList, batchPriorities, userName, userRoles, ephesoftUser, null, identifier);

	}

	/**
	 * An api to fetch all the batch instances excluding remotely executing batches by status list. Parameter firstResult set a limit
	 * upon the number of objects to be retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set
	 * the sort property and order of that property. If orderList parameter is null or empty then this parameter is avoided. This will
	 * return only those batch instance which having access by the user roles on the basis of the ephesoft user.
	 * 
	 * @param searchString {@link String}
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
	@Override
	public List<BatchInstance> getBatchInstancesExcludedRemoteBatch(final String searchString, List<BatchInstanceStatus> statusList,
			final int firstResult, final int maxResults, final List<Order> orderList,
			final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities, String userName,
			final Set<String> userRoles, EphesoftUser ephesoftUser) {
		EphesoftCriteria criteria = criteria();

		if (searchString != null && !searchString.isEmpty()) {
			String batchNameLocal = searchString.replaceAll("%", "\\\\%");
			Criterion nameLikeCriteria = Restrictions.like(BATCH_NAME, "%" + batchNameLocal + "%");
			Criterion idLikeCriteria = Restrictions.like(BATCH_INSTANCE_IDENTIFIER, "%" + batchNameLocal + "%");

			LogicalExpression searchCriteria = Restrictions.or(nameLikeCriteria, idLikeCriteria);
			criteria.add(searchCriteria);
		}
		if (null != statusList) {
			criteria.add(Restrictions.in(STATUS, statusList));
			criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, userName)));
			criteria.add(Restrictions.eq(IS_REMOTE, false));
		}
		if (null != batchPriorities && !(batchPriorities.isEmpty())) {
			Disjunction disjunction = Restrictions.disjunction();
			for (BatchPriority batchPriority : batchPriorities) {
				if (null != batchPriority) {
					Integer lowValue = batchPriority.getLowerLimit();
					Integer upperValue = batchPriority.getUpperLimit();
					disjunction.add(Restrictions.between(PRIORITY, lowValue, upperValue));
				} else {
					disjunction = Restrictions.disjunction();
					break;
				}
			}
			criteria.add(disjunction);

		}
		List<BatchInstance> batchInstanceList = new ArrayList<BatchInstance>();

		Set<String> batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);

		switch (ephesoftUser) {
			case NORMAL_USER:
				batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);
				Set<String> batchInstanceIdentifierSet = batchInstanceGroupsDao.getBatchInstanceIdentifiersExceptUserRoles(userRoles);
				Set<String> batchInstanceIdentifiers = batchInstanceGroupsDao.getBatchInstanceIdentifierForUserRoles(userRoles);
				batchInstanceIdentifierSet.removeAll(batchInstanceIdentifiers);

				if ((null != batchClassIdentifiers && batchClassIdentifiers.size() > 0)
						|| (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0)) {
					BatchInstanceFilter[] filters = null;
					if (filterClauseList != null) {
						filters = filterClauseList.toArray(new BatchInstanceFilter[filterClauseList.size()]);
					}
					Order[] orders = null;
					if (orderList != null) {
						orders = orderList.toArray(new Order[orderList.size()]);
					}

					batchInstanceList = find(criteria, 0, -1, filters, orders);
					// Fixed against JIRA-1018 User not able to navigate through batch list.
					batchInstanceList = updateBatchInstanceList(batchInstanceList, batchClassIdentifiers, batchInstanceIdentifiers,
							batchInstanceIdentifierSet, firstResult, maxResults);
				}
				break;
			default:
				if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0) {
					BatchInstanceFilter[] filters = null;
					if (filterClauseList != null) {
						filters = filterClauseList.toArray(new BatchInstanceFilter[filterClauseList.size()]);
					}
					Order[] orders = null;
					if (orderList != null) {
						orders = orderList.toArray(new Order[orderList.size()]);
					}

					criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
					criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifiers));
					batchInstanceList = find(criteria, firstResult, maxResults, filters, orders);
				}
				break;
		}
		return batchInstanceList;
	}

	/**
	 * An api to fetch all the batch instance for status list input. API will return those batch instance having access by the user
	 * roles on the basis of ephesoft user.
	 * 
	 * @param statusList List<BatchInstanceStatus>
	 * @param firstResult int
	 * @param maxResults int
	 * @param ephesoftUser EphesoftUser
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults,
			final Set<String> userRoles, EphesoftUser ephesoftUser) {
		List<BatchInstance> batchInstances = new ArrayList<BatchInstance>();

		Set<String> batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);

		EphesoftCriteria criteria = criteria();
		criteria.add(Restrictions.in(STATUS, statusList));
		criteria.add(Restrictions.isNull(CURRENT_USER));
		criteria.add(Restrictions.eq(IS_REMOTE, false));

		switch (ephesoftUser) {
			case NORMAL_USER:
				Set<String> batchInstanceIdentifiers = batchInstanceGroupsDao.getBatchInstanceIdentifierForUserRoles(userRoles);
				Set<String> batchInstanceIdentifierSet = batchInstanceGroupsDao.getBatchInstanceIdentifiersExceptUserRoles(userRoles);
				batchInstanceIdentifierSet.removeAll(batchInstanceIdentifiers);
				if ((null != batchClassIdentifiers && batchClassIdentifiers.size() > 0)
						|| (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0)) {
					List<Order> orderList = new ArrayList<Order>();
					Order orderForHighestBatchPriority = new Order(BatchInstanceProperty.PRIORITY, true);
					Order orderForLastModified = new Order(BatchInstanceProperty.LASTMODIFIED, false);
					orderList.add(orderForHighestBatchPriority);
					orderList.add(orderForLastModified);
					batchInstances = find(criteria, 0, -1, orderList.toArray(new Order[orderList.size()]));
					// Fixed against JIRA-1018 User not able to navigate through batch list.
					batchInstances = updateBatchInstanceList(batchInstances, batchClassIdentifiers, batchInstanceIdentifiers,
							batchInstanceIdentifierSet, firstResult, maxResults);
				}
				break;
			default:
				if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0) {
					criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
					criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifiers));
					List<Order> orderList = new ArrayList<Order>();
					Order orderForHighestBatchPriority = new Order(BatchInstanceProperty.PRIORITY, true);
					Order orderForLastModified = new Order(BatchInstanceProperty.LASTMODIFIED, false);
					orderList.add(orderForHighestBatchPriority);
					orderList.add(orderForLastModified);
					batchInstances = find(criteria, firstResult, maxResults, orderList.toArray(new Order[orderList.size()]));
				}
				break;
		}
		return batchInstances;
	}

	/**
	 * This API fetches batch instance on the basis of user name and the roles defined for that user name in the batch class.
	 * 
	 * @param userRoles
	 * @param batchInstanceIdentifier
	 * @param currentUserName
	 * @param ephesoftUser
	 * @return
	 */
	@Override
	public BatchInstance getBatchInstanceByUserRole(final Set<String> userRoles, String batchInstanceIdentifier,
			String currentUserName, EphesoftUser ephesoftUser) {

		BatchInstance batchInstances = null;

		Set<String> batchClassIdentifiers = null;

		switch (ephesoftUser) {
			case NORMAL_USER:
				batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);
				Set<String> batchInstanceIdentifiers = batchInstanceGroupsDao.getBatchInstanceIdentifierForUserRoles(userRoles);
				Set<String> batchInstanceIdentifierSet = batchInstanceGroupsDao.getBatchInstanceIdentifiersExceptUserRoles(userRoles);
				batchInstanceIdentifierSet.removeAll(batchInstanceIdentifiers);
				if ((null != batchClassIdentifiers && batchClassIdentifiers.size() > 0)
						|| (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0)) {
					EphesoftCriteria criteria = criteria();
					criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, currentUserName)));
					criteria.add(Restrictions.eq(IS_REMOTE, false));
					criteria.add(Restrictions.eq(BATCH_INSTANCE_IDENTIFIER, batchInstanceIdentifier));
					batchInstances = findSingle(criteria);

					String batchClassId = batchInstances.getBatchClass().getBatchClassID().getID();
					boolean isValid = true;
					boolean notInValidity = checkNotInBatchInstanceId(batchInstanceIdentifier, batchInstanceIdentifierSet);
					boolean batchClassValidity = checkBatchClassId(batchClassIdentifiers, batchClassId);
					boolean inValididty = checkValidBatchId(batchInstanceIdentifier, batchInstanceIdentifiers);
					if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0 && null != batchInstanceIdentifiers
							&& batchInstanceIdentifiers.size() > 0 && null != batchInstanceIdentifierSet
							&& batchInstanceIdentifierSet.size() > 0) {
						isValid = notInValidity && (batchClassValidity || inValididty);
					} else if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0 && null != batchInstanceIdentifiers
							&& batchInstanceIdentifiers.size() > 0) {
						isValid = batchClassValidity || inValididty;
					} else if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0 && null != batchInstanceIdentifierSet
							&& batchInstanceIdentifierSet.size() > 0) {
						isValid = notInValidity && batchClassValidity;
					} else if (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0
							&& null != batchInstanceIdentifierSet && batchInstanceIdentifierSet.size() > 0) {
						isValid = notInValidity && inValididty;
					} else if (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0) {
						isValid = inValididty;
					} else if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0) {
						isValid = batchClassValidity;
					}
					if (!isValid) {
						batchInstances = null;
					}
				}
				break;
			default:
				batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);
				if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0) {
					EphesoftCriteria criteria = criteria();
					criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, currentUserName)));
					criteria.add(Restrictions.eq(IS_REMOTE, false));
					criteria.add(Restrictions.eq(BATCH_INSTANCE_IDENTIFIER, batchInstanceIdentifier));
					criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
					criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifiers));

					batchInstances = findSingle(criteria);
				}
				break;
		}
		return batchInstances;
	}

	private boolean checkValidBatchId(String batchInstanceIdentifier, Set<String> batchInstanceIdentifiers) {
		boolean isValid = false;
		for (String inBatchInstanceId : batchInstanceIdentifiers) {
			if (batchInstanceIdentifier.equalsIgnoreCase(inBatchInstanceId)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}

	private boolean checkBatchClassId(Set<String> batchClassIdentifiers, String batchClassId) {
		boolean isValid = false;
		for (String batchClassIdentifier : batchClassIdentifiers) {
			if (batchClassId.equals(batchClassIdentifier)) {
				isValid = true;
				break;
			}
		}
		return isValid;
	}

	private boolean checkNotInBatchInstanceId(String batchInstanceIdentifier, Set<String> batchInstanceIdentifierSet) {
		boolean isValid = true;
		for (String notInBatchInstanceId : batchInstanceIdentifierSet) {
			if (batchInstanceIdentifier.equalsIgnoreCase(notInBatchInstanceId)) {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	/**
	 * An api to fetch count of the batch instance table for batch instance filters.
	 * 
	 * @param filterClauseList List<BatchInstanceFilter>
	 * @return count of the batch instance present for the batch instance filters.
	 */
	@Override
	public int getCount(final List<BatchInstanceFilter> filterClauseList) {
		int count = -1;
		DetachedCriteria criteria = criteria();
		if (null != filterClauseList) {
			for (BatchInstanceFilter batchInstanceFilter : filterClauseList) {
				criteria.add(Restrictions.eq(batchInstanceFilter.getNameProperty().getProperty(),
						batchInstanceFilter.getValueProperty()));
			}
			count = count(criteria);
		}
		return count;
	}

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
	@Override
	public int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final Set<String> userRoles, final String currentUserName, EphesoftUser ephesoftUser) {
		return getCount(batchInstStatusList, batchPriorities, false, userRoles, currentUserName, ephesoftUser, null);
	}

	/**
	 * An api to return total count of batches from the batch instance table having access by the user roles on the basis of ephesoft
	 * user.
	 * 
	 * @param currentUser {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @return int, total count
	 */
	@Override
	public int getAllCount(final String currentUser, final Set<String> userRoles, EphesoftUser ephesoftUser) {
		return getCount(null, null, true, userRoles, currentUser, ephesoftUser, null);
	}

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
	@Override
	public int getCount(final List<BatchInstanceStatus> batchInstStatusList, final List<BatchPriority> batchPriorities,
			final boolean isNotCurrentUserCheckReq, final Set<String> userRoles, final String currentUserName,
			EphesoftUser ephesoftUser, final String searchString) {
		int count = 0;
		List<BatchInstance> batchInstanceList = getBatchInstanceList(batchInstStatusList, batchPriorities, isNotCurrentUserCheckReq,
				userRoles, currentUserName, ephesoftUser, searchString);
		if (null != batchInstanceList) {
			count = batchInstanceList.size();
		}
		return count;
	}

	/**
	 * Gets the batch instance list filtered as per the arguments supplied.
	 * 
	 * @param batchInstStatusList List<{@link BatchInstanceStatus}>
	 * @param batchPriorities the priority list of the batches
	 * @param isCurrUsrNotReq true if the current user can be anyone. False if current user cannot be null.
	 * @param currentUser {@link String}
	 * @param userRoles Set<{@link String}>
	 * @param ephesoftUser {@link EphesoftUser}
	 * @param searchString the searchString on which batch instances have to be fetched
	 * @return List<BatchInstance> returns the batch instance list filtered as per the information passed as arguments
	 */
	@Override
	public List<BatchInstance> getBatchInstanceList(final List<BatchInstanceStatus> batchInstStatusList,
			final List<BatchPriority> batchPriorities, final boolean isNotCurrentUserCheckReq, final Set<String> userRoles,
			final String currentUserName, EphesoftUser ephesoftUser, final String searchString) {
		EphesoftCriteria criteria = criteria();

		if (null != batchInstStatusList) {
			criteria.add(Restrictions.in(STATUS, batchInstStatusList));
		}

		if (null != searchString && !searchString.isEmpty()) {
			String searchStringLocal = searchString.replaceAll("%", "\\\\%");
			Criterion nameLikeCriteria = Restrictions.like(BATCH_NAME, "%" + searchStringLocal + "%");
			Criterion idLikeCriteria = Restrictions.like(BATCH_INSTANCE_IDENTIFIER, "%" + searchStringLocal + "%");

			LogicalExpression searchCriteria = Restrictions.or(nameLikeCriteria, idLikeCriteria);
			criteria.add(searchCriteria);
		}

		if (null != batchPriorities && !(batchPriorities.isEmpty())) {
			Disjunction disjunction = Restrictions.disjunction();
			for (BatchPriority batchPriority : batchPriorities) {
				if (null != batchPriority) {
					Integer lowValue = batchPriority.getLowerLimit();
					Integer upperValue = batchPriority.getUpperLimit();
					disjunction.add(Restrictions.between(PRIORITY, lowValue, upperValue));
				} else {
					disjunction = Restrictions.disjunction();
					break;
				}
			}
			criteria.add(disjunction);
		}

		Set<String> batchClassIdentifiers = null;
		Set<String> batchInstanceIdentifiers = null;
		List<BatchInstance> batchInstanceList = null;
		switch (ephesoftUser) {
			case ADMIN_USER:
				batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);
				batchInstanceIdentifiers = batchInstanceGroupsDao.getBatchInstanceIdentifierForUserRoles(userRoles);
				if ((null != batchClassIdentifiers && batchClassIdentifiers.size() > 0)
						|| (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0)) {
					// Add check for null current users only.
					// Now we will count only for those current users those are null.
					if (!isNotCurrentUserCheckReq && null != currentUserName) {
						criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, currentUserName)));
					}
					List<BatchInstance> batchInstances = find(criteria);

					batchInstanceList = new ArrayList<BatchInstance>();
					if (batchInstances != null && !batchInstances.isEmpty()) {
						batchInstanceList.addAll(batchInstances);
						for (BatchInstance batchInstance : batchInstances) {
							String batchInstanceIdentifier = batchInstance.getIdentifier();
							String batchClassIdentifier = batchInstance.getBatchClass().getBatchClassID().getID();
							boolean isValid = true;
							boolean batchClassValidity = checkBatchClassId(batchClassIdentifiers, batchClassIdentifier);
							boolean inValididty = checkValidBatchId(batchInstanceIdentifier, batchInstanceIdentifiers);

							if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0 && null != batchInstanceIdentifiers
									&& batchInstanceIdentifiers.size() > 0) {
								isValid = batchClassValidity || inValididty;
							} else if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0) {
								isValid = batchClassValidity;
							} else if (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0) {
								isValid = inValididty;
							}
							if (!isValid) {
								batchInstanceList.remove(batchInstance);
							}
						}
					}
				}
				break;
			default:
				batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);
				batchInstanceIdentifiers = batchInstanceGroupsDao.getBatchInstanceIdentifierForUserRoles(userRoles);

				if ((null != batchClassIdentifiers && batchClassIdentifiers.size() > 0)
						|| (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0)) {
					Set<String> batchInstanceIdentifierSet = batchInstanceGroupsDao
							.getBatchInstanceIdentifiersExceptUserRoles(userRoles);
					batchInstanceIdentifierSet.removeAll(batchInstanceIdentifiers);
					if (!isNotCurrentUserCheckReq && null != currentUserName) {
						criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, currentUserName)));
					}
					batchInstanceList = find(criteria);
					batchInstanceList = updateBatchInstanceList(batchInstanceList, batchClassIdentifiers, batchInstanceIdentifiers,
							batchInstanceIdentifierSet, 0, batchInstanceList.size());
				}
				break;
		}
		return batchInstanceList;
	}

	private List<BatchInstance> updateBatchInstanceList(List<BatchInstance> batchInstances, Set<String> batchClassIdentifiers,
			Set<String> batchInstanceIdentifiers, Set<String> batchInstanceIdentifierSet, int firstResult, int maxResults) {
		List<BatchInstance> batchInstanceList = new ArrayList<BatchInstance>();
		if (batchInstances != null && !batchInstances.isEmpty()) {
			int localCounter = 0;
			if (maxResults == -1) {
				maxResults = batchInstances.size();
			}
			if (firstResult == -1) {
				firstResult = 0;
			}
			int maxCount = firstResult + maxResults;
			for (BatchInstance batchInstance : batchInstances) {
				String batchInstanceIdentifier = batchInstance.getIdentifier();
				String batchClassIdentifier = batchInstance.getBatchClass().getBatchClassID().getID();
				boolean isValid = true;
				boolean notInValidity = checkNotInBatchInstanceId(batchInstanceIdentifier, batchInstanceIdentifierSet);
				boolean batchClassValidity = checkBatchClassId(batchClassIdentifiers, batchClassIdentifier);
				boolean inValididty = checkValidBatchId(batchInstanceIdentifier, batchInstanceIdentifiers);
				if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0 && null != batchInstanceIdentifiers
						&& batchInstanceIdentifiers.size() > 0 && null != batchInstanceIdentifierSet
						&& batchInstanceIdentifierSet.size() > 0) {
					isValid = notInValidity && (batchClassValidity || inValididty);
				} else if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0 && null != batchInstanceIdentifiers
						&& batchInstanceIdentifiers.size() > 0) {
					isValid = batchClassValidity || inValididty;
				} else if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0 && null != batchInstanceIdentifierSet
						&& batchInstanceIdentifierSet.size() > 0) {
					isValid = notInValidity && batchClassValidity;
				} else if (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0
						&& null != batchInstanceIdentifierSet && batchInstanceIdentifierSet.size() > 0) {
					isValid = notInValidity && inValididty;
				} else if (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0) {
					isValid = inValididty;
				} else if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0) {
					isValid = batchClassValidity;
				}
				if (isValid) {
					if (localCounter >= firstResult && localCounter < maxCount) {
						batchInstanceList.add(batchInstance);
					} else if (localCounter >= maxCount) {
						break;
					}
					localCounter = localCounter + 1;
				}
			}
		}
		return batchInstanceList;
	}

	/**
	 * An api to fetch all the batch instance by BatchPriority.
	 * 
	 * @param batchPriority BatchPriority this will add the where clause to the criteria query based on the priority column.
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Override
	public List<BatchInstance> getBatchInstance(BatchPriority batchPriority) {
		DetachedCriteria criteria = criteria();
		Integer lowValue = batchPriority.getLowerLimit();
		Integer upperValue = batchPriority.getUpperLimit();
		criteria.add(Restrictions.between(PRIORITY, lowValue, upperValue));
		return find(criteria);
	}

	/**
	 * An api to fetch all batch instance by batch Class Name.
	 * 
	 * @param batchClassName String
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByBatchClassName(String batchClassName) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(BATCH_CLASS_NAME, batchClassName));
		return find(criteria);
	}

	/**
	 * An api to fetch all batch instance by batch Class Process Name.
	 * 
	 * @param batchClassProcessName String
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByBatchClassProcessName(String batchClassProcessName) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(BATCH_CLASS_PROCESS_NAME, batchClassProcessName));
		return find(criteria);
	}

	/**
	 * This method will create the batch instance for input batch class, unc sub folder path and local folder path.
	 * 
	 * @param batchClass BatchClass
	 * @param uncSubFolder String
	 * @param localFolder String
	 * @param priority int
	 * @return BatchInstance
	 */
	@Override
	public BatchInstance createBatchInstance(BatchClass batchClass, String uncSubfolder, String localFolder, int priority) {
		BatchInstance batchInstance = new BatchInstance();
		batchInstance.setBatchClass(batchClass);
		batchInstance.setUncSubfolder(uncSubfolder);
		batchInstance.setLocalFolder(localFolder);
		batchInstance.setPriority(priority);
		batchInstance.setStatus(BatchInstanceStatus.NEW);
		PrivateKeyEncryptionAlgorithm encryptionAlgorithm = batchClass == null ? null : batchClass.getEncryptionAlgorithm();
		batchInstance.setEncryptionAlgorithm(encryptionAlgorithm);
		String batchName = null;
		if (null != uncSubfolder) {
			int index = uncSubfolder.lastIndexOf('\\');
			if (index != -1) {
				batchName = uncSubfolder.substring(index + 1);
			} else {
				index = uncSubfolder.lastIndexOf('/');
				if (index != -1) {
					batchName = uncSubfolder.substring(index + 1);
				}
			}
		}

		batchInstance.setBatchName(batchName);
		final String batchDescription = getBatchDescriptionFromSERFile(uncSubfolder, batchName);
		batchInstance.setBatchDescription(batchDescription);

		this.create(batchInstance);
		return batchInstance;
	}

	/**
	 * This method will update the status for batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 * @param status BatchInstanceStatus
	 */
	@Override
	@Transactional
	public void updateBatchInstanceStatus(BatchInstance batchInstance, BatchInstanceStatus status) {
		batchInstance.setStatus(status);
		this.saveOrUpdate(batchInstance);
	}

	/**
	 * This method will create a new batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	@Override
	public void createBatchInstance(BatchInstance batchInstance) {
		create(batchInstance);
	}

	/**
	 * This method will update the existing batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	@Override
	public void updateBatchInstance(BatchInstance batchInstance) {
		saveOrUpdate(batchInstance);
	}

	/**
	 * This method will remove the existing batch instance.
	 * 
	 * @param batchInstance BatchInstance
	 */
	@Override
	public void removeBatchInstance(BatchInstance batchInstance) {
		remove(batchInstance);
	}

	/**
	 * An api to fetch all batch instance by batch Class Process and batch instance id's list.
	 * 
	 * @param batchClassName String
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstanceList(String batchClassName, List<String> batchInstanceIDList) {
		DetachedCriteria criteria = criteria();
		criteria.createAlias(BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(BATCH_CLASS_NAME, batchClassName));
		criteria.add(Restrictions.in("identifier", batchInstanceIDList));
		return find(criteria);
	}

	@Override
	public List<BatchInstance> getAllBatchInstancesForCurrentUser(String currentUser) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(CURRENT_USER, currentUser));
		return find(criteria);
	}

	@Override
	public BatchInstance getBatch(long batchId, LockMode lockMode) {
		return get(batchId);
	}

	@Override
	public List<BatchInstance> getRunningBatchInstancesForServer(String lockOwner) {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(STATUS, BatchInstanceStatus.RUNNING));
		criteria.add(Restrictions.eq(EXECUTING_SERVER, lockOwner));
		return find(criteria);
	}

	@Override
	public List<BatchInstance> getAllBatchInstances(List<Order> orders) {
		EphesoftCriteria criteria = criteria();
		return find(criteria, orders.toArray(new Order[orders.size()]));
	}

	@Override
	public List<BatchInstance> getAllUnFinishedBatchInstances(BatchClass batchClass) {
		DetachedCriteria criteria = criteria();
		// criteria.createAlias(BATCH_CLASS, BATCH_CLASS, JoinFragment.INNER_JOIN);
		// criteria.add(Restrictions.eq("batchClass.uncFolder", uncFolder));

		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.NEW);
		statusList.add(BatchInstanceStatus.ERROR);
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		statusList.add(BatchInstanceStatus.RUNNING);
		statusList.add(BatchInstanceStatus.READY);
		statusList.add(BatchInstanceStatus.RESTART_IN_PROGRESS);
		statusList.add(BatchInstanceStatus.LOCKED);
		criteria.add(Restrictions.in(STATUS, statusList));
		// Below line commented, as instead of passing unc folder path list, now passing the batch class
		// criteria.add(Restrictions.in(UNC_SUB_FOLDER, uncSubFolderPathList));
		criteria.add(Restrictions.eq(BATCH_CLASS, batchClass));

		return find(criteria);
	}

	@Override
	public void updateBatchInstanceLocalFolder(BatchInstance batchInsctance, String localFolder) {
		batchInsctance.setLocalFolder(localFolder);
		saveOrUpdate(batchInsctance);
	}

	@Override
	public void updateBatchInstanceUncFolder(BatchInstance batchInsctance, String uncFolder) {
		batchInsctance.setUncSubfolder(uncFolder);
		saveOrUpdate(batchInsctance);
	}

	/**
	 * An API return all unfinished remotely executing batch instances.
	 */
	@Override
	public List<BatchInstance> getAllUnfinshedRemotelyExecutedBatchInstance() {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(IS_REMOTE, true));
		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		statusList.add(BatchInstanceStatus.RUNNING);
		statusList.add(BatchInstanceStatus.TRANSFERRED);
		statusList.add(BatchInstanceStatus.READY);
		statusList.add(BatchInstanceStatus.REMOTE);
		statusList.add(BatchInstanceStatus.ERROR);
		criteria.add(Restrictions.in(STATUS, statusList));
		return find(criteria);

	}

	/**
	 * An api to fetch all the batch instances only remotely executing batches by status list. Parameter firstResult set a limit upon
	 * the number of objects to be retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set the
	 * sort property and order of that property. If orderList parameter is null or empty then this parameter is avoided.
	 * 
	 * @param statusList List<BatchInstanceStatus> status list of batch instance status.
	 * @param firstResult the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResults maxResults the maximum number of results
	 * @param orderList List<Order> orderList set the sort property and order of that property. If orderList parameter is null or empty
	 *            then this parameter is avoided.
	 * @param filterClauseList List<BatchInstanceFilter> this will add the where clause to the criteria query based on the property
	 *            name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities List<BatchPriority> this will add the where clause to the criteria query based on the priority list
	 *            selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 * @param userName Current user name.
	 * @param currentUserRoles
	 * @return List<BatchInstance> return the batch instance list.
	 */
	@Override
	public List<BatchInstance> getRemoteBatchInstances(List<BatchInstanceStatus> statusList, final int firstResult,
			final int maxResults, final List<Order> orderList, final List<BatchInstanceFilter> filterClauseList,
			final List<BatchPriority> batchPriorities, String userName, final Set<String> userRoles) {
		EphesoftCriteria criteria = criteria();

		if (null != statusList) {
			criteria.add(Restrictions.in(STATUS, statusList));
			criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, userName)));
			criteria.add(Restrictions.eq(IS_REMOTE, true));
		}

		if (null != batchPriorities && !(batchPriorities.isEmpty())) {
			Disjunction disjunction = Restrictions.disjunction();
			for (BatchPriority batchPriority : batchPriorities) {
				if (null != batchPriority) {
					Integer lowValue = batchPriority.getLowerLimit();
					Integer upperValue = batchPriority.getUpperLimit();
					disjunction.add(Restrictions.between(PRIORITY, lowValue, upperValue));
				} else {
					disjunction = Restrictions.disjunction();
					break;
				}
			}
			criteria.add(disjunction);

		}

		List<BatchInstance> batchInstances = new ArrayList<BatchInstance>();

		Set<String> batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);

		if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0) {
			BatchInstanceFilter[] filters = null;
			if (filterClauseList != null) {
				filters = filterClauseList.toArray(new BatchInstanceFilter[filterClauseList.size()]);
			}
			Order[] orders = null;
			if (orderList != null) {
				orders = orderList.toArray(new Order[orderList.size()]);
			}

			criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
			criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifiers));
			batchInstances = find(criteria, firstResult, maxResults, filters, orders);
		}

		return batchInstances;
	}

	@Override
	public List<BatchInstance> getExecutingBatchesByServerIP(String serverIP) {
		EphesoftCriteria criteria = criteria();
		if (null != serverIP) {
			criteria.add(Restrictions.eq(EXECUTING_SERVER, serverIP));
			criteria.add(Restrictions.eq(STATUS, BatchInstanceStatus.RUNNING));
		}
		return find(criteria);
	}

	@Override
	public List<BatchInstance> getBatchInstanceByExecutingServerAndStatus(String executingServer,
			BatchInstanceStatus batchInstanceStatus) {
		EphesoftCriteria criteria = criteria();
		if (null != executingServer) {
			criteria.add(Restrictions.eq(EXECUTING_SERVER, executingServer));
			criteria.add(Restrictions.eq(STATUS, batchInstanceStatus));
		}
		return find(criteria);
	}

	@Override
	public List<BatchInstance> getBatchInstanceListByBatchNameAndStatus(String batchName, BatchInstanceStatus batchStatus,
			String userName, Set<String> userRoles) {
		EphesoftCriteria criteria = criteria();
		List<BatchInstance> batchInstanceList = null;
		String batchNameLocal = batchName.replaceAll("%", "\\\\%");
		if (batchStatus != null) {
			criteria.add(Restrictions.like(BATCH_NAME, "%" + batchNameLocal + "%"));
			criteria.add(Restrictions.eq(STATUS, batchStatus));
			criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, userName)));

			Set<String> batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);

			if (null != batchClassIdentifiers && batchClassIdentifiers.size() > 0) {
				criteria.createAlias(BATCH_CLASS, BATCH_CLASS);
				criteria.add(Restrictions.in(BATCH_CLASS_IDENTIFIER, batchClassIdentifiers));
				batchInstanceList = find(criteria);
			}
		}
		return batchInstanceList;
	}

	/**
	 * This API fetches all the batch instances on the basis of batch status list passed.
	 * 
	 * @param batchStatusList List<{@link BatchInstanceStatus}>
	 * @return List<{@link BatchInstance}>
	 */
	@Override
	public List<BatchInstance> getBatchInstanceByStatusList(List<BatchInstanceStatus> batchStatusList) {
		/* Changed order by, because not working correctly in MySQL */
		EphesoftCriteria criteria = criteria();
		if (null != batchStatusList && !batchStatusList.isEmpty()) {
			criteria.add(Restrictions.in(STATUS, batchStatusList));
		}
		List<Order> orderList = new ArrayList<Order>();
		Order orderForHighestBatchPriority = new Order(BatchInstanceProperty.PRIORITY, true);
		Order orderForLastModified = new Order(BatchInstanceProperty.ID, true);
		orderList.add(orderForHighestBatchPriority);
		orderList.add(orderForLastModified);
		List<BatchInstance> batchInstances = find(criteria, orderList.toArray(new Order[orderList.size()]));
		return batchInstances;
	}

	@Override
	public void clearCurrentUser(String batchInstanceIdentifier) {
		BatchInstance batchInstance = getBatchInstancesForIdentifier(batchInstanceIdentifier);
		batchInstance.setCurrentUser(null);
		updateBatchInstance(batchInstance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.dcma.da.dao.BatchInstanceDao#getBatchInstancesForStatusPriority(java.util.List, java.util.List, java.util.Set)
	 */
	@Override
	public List<BatchInstance> getBatchInstancesForStatusPriority(List<BatchInstanceStatus> statusList,
			final List<BatchPriority> batchPriorities, final Set<String> userRoles) {
		EphesoftCriteria criteria = criteria();
		criteria.add(Restrictions.isNull(CURRENT_USER));
		return getBatchInstances("", "", statusList, -1, -1, null, null, batchPriorities, null, userRoles, EphesoftUser.ADMIN_USER,
				criteria, null);
	}

	/**
	 * An api to fetch all the batch instances by status list. Parameter firstResult set a limit upon the number of objects to be
	 * retrieved. Parameter maxResults set the first result to be retrieved. Parameter orderList set the sort property and order of
	 * that property. If orderList parameter is null or empty then this parameter is avoided.
	 * 
	 * @param statusList List<BatchInstanceStatus> status list of batch instance status.
	 * @param firstResultTemp the first result to retrieve, numbered from <tt>0</tt>
	 * @param maxResultsTemp maxResults the maximum number of results
	 * @param orderList List<Order> orderList set the sort property and order of that property. If orderList parameter is null or empty
	 *            then this parameter is avoided.
	 * @param filterClauseList List<BatchInstanceFilter> this will add the where clause to the criteria query based on the property
	 *            name and value. If filterClauseList parameter is null or empty then this parameter is avoided.
	 * @param batchPriorities List<BatchPriority> this will add the where clause to the criteria query based on the priority list
	 *            selected. If batchPriorities parameter is null or empty then this parameter is avoided.
	 * @param userName Current user name.
	 * @param userRoles currentUserRoles
	 * @param EphesoftUser current ephesoft-user
	 * @param searchString the searchString on which batch instances have to be fetched
	 * @return List<BatchInstance> return the batch instance list.
	 */
	public List<BatchInstance> getBatchInstances(String batchIdentifierTextSearch, String batchNameTextSearch,
			List<BatchInstanceStatus> statusList, final int firstResult, final int maxResults, final List<Order> orderList,
			final List<BatchInstanceFilter> filterClauseList, final List<BatchPriority> batchPriorities, String userName,
			final Set<String> userRoles, EphesoftUser ephesoftUser, final EphesoftCriteria criteria, final String searchString) {
		EphesoftCriteria criteriaLocal = null;
		if (criteria == null) {
			criteriaLocal = criteria();
		} else {
			criteriaLocal = criteria;
		}

		int firstResultTemp = firstResult;
		int maxResultsTemp = maxResults;

		// For adding identifier as an criteria for result
		if (null != searchString && !searchString.isEmpty()) {
			String searchStringLocal = searchString.replaceAll("%", "\\\\%");
			Criterion nameLikeCriteria = Restrictions.like(BATCH_NAME, "%" + searchStringLocal + "%");
			Criterion idLikeCriteria = Restrictions.like(BATCH_INSTANCE_IDENTIFIER, "%" + searchStringLocal + "%");

			LogicalExpression searchCriteria = Restrictions.or(nameLikeCriteria, idLikeCriteria);
			criteriaLocal.add(searchCriteria);
		}

		if (null != batchIdentifierTextSearch && !batchIdentifierTextSearch.isEmpty()
				&& !"-1".equalsIgnoreCase(batchIdentifierTextSearch)) {
			System.out.println("Batch identifier text is as follows : " + batchIdentifierTextSearch);
			String searchStringLocal = batchIdentifierTextSearch.replaceAll("%", "\\\\%");
			Criterion idLikeCriteria = Restrictions.like(BATCH_INSTANCE_IDENTIFIER, "%" + searchStringLocal + "%");
			criteriaLocal.add(idLikeCriteria);
		} else if (null != batchNameTextSearch && !batchNameTextSearch.isEmpty() && !"-1".equalsIgnoreCase(batchNameTextSearch)) {
			System.out.println("batch name text search is as follows : " + batchNameTextSearch);
			String searchStringLocal = batchNameTextSearch.replaceAll("%", "\\\\%");
			Criterion nameLikeCriteria = Restrictions.like(BATCH_NAME, "%" + searchStringLocal + "%");
			criteriaLocal.add(nameLikeCriteria);
		}

		if (null != statusList) {
			criteriaLocal.add(Restrictions.in(STATUS, statusList));
			// criteria.add(Restrictions.or(Restrictions.isNull(CURRENT_USER), Restrictions.eq(CURRENT_USER, userName)));
		}

		if (null != batchPriorities && !(batchPriorities.isEmpty())) {
			Disjunction disjunction = Restrictions.disjunction();
			for (BatchPriority batchPriority : batchPriorities) {
				if (null != batchPriority) {
					Integer lowValue = batchPriority.getLowerLimit();
					Integer upperValue = batchPriority.getUpperLimit();
					disjunction.add(Restrictions.between(PRIORITY, lowValue, upperValue));
				} else {
					disjunction = Restrictions.disjunction();
					break;
				}
			}
			criteriaLocal.add(disjunction);

		}

		List<BatchInstance> batchInstances = new ArrayList<BatchInstance>();
		BatchInstanceFilter[] filters = null;
		Order[] orders = null;
		switch (ephesoftUser) {
			default:

				// Fixed for JIRA 10220 Batch assigned to some other user from DB is getting visible to the user, to whom corresponding
				// batch class is assigned.
				Set<String> batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);
				batchClassIdentifiers = batchClassGroupsDao.getBatchClassIdentifierForUserRoles(userRoles);
				final Set<String> batchInstanceIdentifierSet = batchInstanceGroupsDao
						.getBatchInstanceIdentifiersExceptUserRoles(userRoles);
				final Set<String> batchInstanceIdentifiers = batchInstanceGroupsDao.getBatchInstanceIdentifierForUserRoles(userRoles);
				if (!CollectionUtil.isEmpty(batchInstanceIdentifiers)) {
					batchInstanceIdentifierSet.removeAll(batchInstanceIdentifiers);
				}
				if ((null != batchClassIdentifiers && batchClassIdentifiers.size() > 0)
						|| (null != batchInstanceIdentifiers && batchInstanceIdentifiers.size() > 0)) {
					if (filterClauseList != null) {
						filters = filterClauseList.toArray(new BatchInstanceFilter[filterClauseList.size()]);
					}
					if (orderList != null) {
						orders = orderList.toArray(new Order[orderList.size()]);
					}
					batchInstances = find(criteriaLocal, 0, -1, filters, orders);
					if (batchInstances != null && !batchInstances.isEmpty()) {
						batchInstances = updateBatchInstanceList(batchInstances, batchClassIdentifiers, batchInstanceIdentifiers,
								batchInstanceIdentifierSet, firstResultTemp, maxResultsTemp);
					}
				}
				break;
		}
		return batchInstances;
	}

	/**
	 * Api to fetch all batch instance by BatchInstanceStatus for a batch class.
	 * 
	 * @param batchInstanceStatus BatchInstanceStatus
	 * @return List<BatchInstance>
	 */
	@Override
	public List<BatchInstance> getBatchInstByStatusAndBatchClass(List<BatchInstanceStatus> statusList, BatchClass batchClass) {
		List<BatchInstance> batchInstances = null;
		DetachedCriteria criteria = criteria();

		if (statusList == null) {
			batchInstances = new ArrayList<BatchInstance>();
		} else {
			criteria.add(Restrictions.in(STATUS, statusList));
			criteria.add(Restrictions.eq(BATCH_CLASS, batchClass));
			criteria.addOrder(org.hibernate.criterion.Order.asc(PRIORITY));
			batchInstances = find(criteria);
		}
		return batchInstances;
	}

	/**
	 * Gets the Batch Description from the SER file if available.
	 * 
	 * @param uncSubfolder {@link String}
	 * @return batchDescription {@link String}
	 */
	private String getBatchDescriptionFromSERFile(final String uncSubfolder, final String batchName) {
		String batchDescription = null;
		FileInputStream fileInputStream = null;
		if (!EphesoftStringUtil.isNullOrEmpty(uncSubfolder)) {
			final String serializedFilePath = EphesoftStringUtil.concatenate(uncSubfolder, File.separator, BID_SER_FILE_NAME,
					SERIALIZATION_EXT);
			final File serializedFile = new File(serializedFilePath);
			if (serializedFile.exists()) {
				try {

					fileInputStream = new FileInputStream(serializedFile);
					batchDescription = SerializationUtils.deserialize(fileInputStream).toString();
					serializedFile.delete();
				} catch (final IOException ioException) {
					log.info(EphesoftStringUtil.concatenate("Error during reading the serialized file. ", ioException.getMessage()));
				} catch (final SerializationException serException) {
					log.error("Error during de-serializing the Batch Description: ", serException.getMessage());
				} catch (final IllegalArgumentException illegalArgumentException) {
					log.error("Error during parsing File Input Stream : ", illegalArgumentException.getMessage());
				} catch (final SecurityException securityException) {
					log.info("Unable to delete serialized file : ", securityException.getMessage());
				} finally {
					try {
						if (fileInputStream != null) {
							fileInputStream.close();
						}
					} catch (final IOException ioException) {
						if (serializedFile != null) {
							log.error(EphesoftStringUtil.concatenate("Problem closing stream for file : ", serializedFile.getName(),
									ioException.getMessage()));
						}
					}
				}

			} else {
				log.info("Serialised file not found in UNC sub folder. Setting Batch Name as Batch Description.");
				batchDescription = batchName;
			}
		}
		return batchDescription;
	}
	
	@Override
	public List<BatchInstance> getAllRunningBatches() {
		EphesoftCriteria criteria = criteria();
		criteria.add(Restrictions.eq(STATUS, BatchInstanceStatus.RUNNING));
		return find(criteria);
	}
}
