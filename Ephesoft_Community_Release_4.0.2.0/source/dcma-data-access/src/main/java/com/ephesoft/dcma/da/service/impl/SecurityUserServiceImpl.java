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

package com.ephesoft.dcma.da.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.da.dao.BatchClassDao;
import com.ephesoft.dcma.da.dao.SecurityGroupDao;
import com.ephesoft.dcma.da.dao.SecurityUserDao;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.SecurityGroup;
import com.ephesoft.dcma.da.domain.SecurityUser;
import com.ephesoft.dcma.da.service.SecurityUserService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * Provides implementation of <code>SecurityUserService</code> services.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Service
public class SecurityUserServiceImpl implements SecurityUserService {

	/**
	 * Logger for <code>SecurityUserServiceImpl</code> class.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(SecurityUserServiceImpl.class);

	/**
	 * To hold the instance of DAO of user.
	 */
	@Autowired
	private SecurityUserDao securityUserDao;

	/**
	 * To hold the instance of DAO of group.
	 */
	@Autowired
	private SecurityGroupDao securityGroupDao;

	/**
	 * To hold the instance of DAO of batch class.
	 */
	@Autowired
	private BatchClassDao batchClassDao;

	@Override
	public Set<String> getAllUser() {
		return securityUserDao.getAllUser();
	}

	@Override
	public Set<String> getUserRoles(final String userName) {
		return securityUserDao.getUserRoles(userName);
	}

	@Override
	public boolean isSuperAdmin(final String userName) {
		return securityUserDao.isSuperAdmin(userName);
	}

	@Override
	@Transactional
	public boolean saveAndUpdateUser(final String userName, final String groupName, final Boolean isSuperAdmin) {
		LOGGER.debug("Saving and updating user details for username: ", userName, ", group name: ",
				groupName, " and is super admin: ", isSuperAdmin);
		boolean isSuccess = false;
		if (!EphesoftStringUtil.isNullOrEmpty(groupName) && !EphesoftStringUtil.isNullOrEmpty(userName)) {
			SecurityUser user = securityUserDao.findSecurityUserByName(userName);
			SecurityGroup group = securityGroupDao.findSecurityGroupByName(groupName);

			// For new user and group
			if (null == user && null == group) {
				user = createUserAndGroup(userName, groupName, isSuperAdmin);
			} else if (null == user && null != group) { // For new user and new group
				user = createUserAndAssignGroup(userName, group, isSuperAdmin);
			} else if (null != user && null == group) { // For existing user and new group
				user = createGroupAndAssignUser(user, groupName, isSuperAdmin);
			} else { // For existing user and group
				user = updateUserAndGroup(user, group, isSuperAdmin);
			}
			securityUserDao.saveOrUpdate(user);
			isSuccess = true;
		}
		LOGGER.debug("Result of save and update is: ", isSuccess);
		return isSuccess;
	}

	/**
	 * Updates the details of existing user and group.
	 * 
	 * @param user {@link SecurityUser} user to be updated.
	 * @param group {@link SecurityGroup} group to be updated.
	 * @param isSuperAdmin is user super admin
	 * @return {@link SecurityUser} updated user is returned.
	 */
	private SecurityUser updateUserAndGroup(final SecurityUser user, final SecurityGroup group, final Boolean isSuperAdmin) {
		LOGGER.debug("Updating user and group information.");
		Set<SecurityGroup> userGroups = user.getUserGroups();

		// If no group is assigned to user
		if (null == userGroups) {
			LOGGER.debug("Adding the only group for user.");
			userGroups = new HashSet<SecurityGroup>(1);
			userGroups.add(group);
		} else if (userGroups.isEmpty()) {
			LOGGER.debug("Only one group for user.");
			userGroups.add(group);
		} else {
			SecurityGroup securityGroup = null;
			SecurityGroup tempSecurityGroup = null;
			for (Iterator<SecurityGroup> iterator = userGroups.iterator(); iterator.hasNext();) {
				securityGroup = (SecurityGroup) iterator.next();
				if (securityGroup.getGroupName().equals(group.getGroupName())) {
					tempSecurityGroup = securityGroup;
					break;
				}
			}

			// If the passed groups is assigned to user
			if (null == tempSecurityGroup) {
				LOGGER.debug("Adding new group to user.");
				userGroups.add(group);
			} else {
				LOGGER.debug("Updating super admin property only.");
				if (null != isSuperAdmin && tempSecurityGroup.isSuperAdmin() != isSuperAdmin.booleanValue()) {
					tempSecurityGroup.setSuperAdmin(isSuperAdmin);
					updateBatchClassGroups(tempSecurityGroup.getGroupName(), isSuperAdmin);
				}
			}
		}
		user.setUserGroups(userGroups);
		return user;
	}

	/**
	 * Creates the new group and assign the group to the existing user.
	 * 
	 * @param user {@link SecurityUser} user to be updated.
	 * @param group {@link SecurityGroup} group to be created.
	 * @param isSuperAdmin is user super admin
	 * @return {@link SecurityUser} updated user is returned.
	 */
	private SecurityUser createGroupAndAssignUser(final SecurityUser user, final String groupName, final Boolean isSuperAdmin) {
		LOGGER.debug("Creating new group and assigning to user.");
		boolean tempSuperAdmin = (null == isSuperAdmin) ? Boolean.FALSE : isSuperAdmin.booleanValue();
		SecurityGroup group = new SecurityGroup();
		group.setGroupName(groupName);
		group.setSuperAdmin(tempSuperAdmin);
		Set<SecurityGroup> userGroups = user.getUserGroups();
		if (null == userGroups) {
			LOGGER.debug("Added the only group of user.");
			userGroups = new HashSet<SecurityGroup>(1);
		}
		userGroups.add(group);
		user.setUserGroups(userGroups);

		// Update the batch classes groups for new super admin
		updateBatchClassGroups(groupName, tempSuperAdmin);
		return user;
	}

	/**
	 * Creates the new user and assign the user an existing group.
	 * 
	 * @param user {@link SecurityUser} user to be created.
	 * @param group {@link SecurityGroup} group to be updated.
	 * @param isSuperAdmin is user super admin
	 * @return {@link SecurityUser} updated user is returned.
	 */
	private SecurityUser createUserAndAssignGroup(final String userName, final SecurityGroup group, final Boolean isSuperAdmin) {
		LOGGER.debug("Creating new user and assigned group.");
		SecurityUser user = new SecurityUser();
		user.setUserName(userName);
		Set<SecurityGroup> userGroups = new HashSet<SecurityGroup>(1);
		userGroups.add(group);
		user.setUserGroups(userGroups);
		if (null != isSuperAdmin && group.isSuperAdmin() != isSuperAdmin.booleanValue()) {
			group.setSuperAdmin(isSuperAdmin);
			updateBatchClassGroups(group.getGroupName(), isSuperAdmin);
		}
		return user;
	}

	/**
	 * Creates the new user and new group.
	 * 
	 * @param user {@link SecurityUser} user to be created.
	 * @param group {@link SecurityGroup} group to be created.
	 * @param isSuperAdmin is user super admin
	 * @return {@link SecurityUser} updated user is returned.
	 */
	private SecurityUser createUserAndGroup(final String userName, final String groupName, final Boolean isSuperAdmin) {
		LOGGER.debug("Creating new group and new user.");
		boolean tempSuperAdmin = (null == isSuperAdmin) ? Boolean.FALSE : isSuperAdmin.booleanValue();
		SecurityGroup group = new SecurityGroup();
		group.setGroupName(groupName);
		group.setSuperAdmin(tempSuperAdmin);
		SecurityUser user = new SecurityUser();
		user.setUserName(userName);
		Set<SecurityGroup> userGroups = new HashSet<SecurityGroup>(1);
		userGroups.add(group);
		user.setUserGroups(userGroups);

		// Update the batch classes groups for new super admin
		updateBatchClassGroups(groupName, tempSuperAdmin);
		return user;
	}

	/**
	 * Updates the batch class groups for the new group created.
	 * 
	 * <p>
	 * It updates only in the cases where new group have a super-admin role.
	 * 
	 * @param groupName {@link String} group name.
	 * @param isSuperAdmin whether new group is super-admin or not.
	 */
	private void updateBatchClassGroups(final String groupName, final boolean isSuperAdmin) {
		if (isSuperAdmin && !EphesoftStringUtil.isNullOrEmpty(groupName)) {
			final Set<String> newAdminRoleSet = new HashSet<String>(1);
			newAdminRoleSet.add(groupName);
			assignAllBatchClassesSuperAdminRole(newAdminRoleSet);
		}
	}

	/**
	 * Assigns existing batch classes with new super-admin role.
	 * 
	 * @param allSuperAdminRoles set of super admin roles
	 */
	private void assignAllBatchClassesSuperAdminRole(final Set<String> allSuperAdminRoles) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Assigning new super-admin group to all the existing batch classes: ",
				allSuperAdminRoles));
		if (null != allSuperAdminRoles && !allSuperAdminRoles.isEmpty()) {
			if (allSuperAdminRoles != null && !allSuperAdminRoles.isEmpty()) {

				// Getting all batch classes exclude deleted.
				List<BatchClass> allBatchClasses = batchClassDao.getAllBatchClassExcludeDeleted();

				if (allBatchClasses != null) {
					for (BatchClass batchClass : allBatchClasses) {

						// Assign super admin roles to each batch class
						batchClass = assignSuperAdminRoleToBatchClass(batchClass, allSuperAdminRoles);

						// Updating the batch class with newly assigned groups.
						batchClassDao.saveOrUpdate(batchClass);
					}
				} else {
					LOGGER.info("No batch classes excluding deleted found");
				}
			} else {
				LOGGER.info("No super-admin roles found.");
			}
		}
	}

	/**
	 * Assigns existing batch class with new super-admin role.
	 * 
	 * @param batchClass {@link BatchClass} batch class to be updated.
	 * @param allSuperAdminRoles {@link Set} set of super admin roles
	 * @return {@link BatchClass} updated batch class.
	 */
	private BatchClass assignSuperAdminRoleToBatchClass(final BatchClass batchClass, final Set<String> allSuperAdminRoles) {
		BatchClass updatedBatchClass = batchClass;
		if (null != batchClass && null != allSuperAdminRoles && !allSuperAdminRoles.isEmpty()) {
			List<BatchClassGroups> assignedBatchClassGroups = updatedBatchClass.getAssignedGroups();
			if (null == assignedBatchClassGroups) {
				assignedBatchClassGroups = new ArrayList<BatchClassGroups>(allSuperAdminRoles.size());
			}
			Set<String> nonExixtentRoles = getNonExixtentRoles(allSuperAdminRoles, assignedBatchClassGroups);
			BatchClassGroups newBatchClassGroups = null;
			for (String newAdminRole : nonExixtentRoles) {
				newBatchClassGroups = createBatchClassGroup(newAdminRole);
				assignedBatchClassGroups.add(newBatchClassGroups);
			}
			updatedBatchClass.setAssignedGroups(assignedBatchClassGroups);
		}
		return updatedBatchClass;
	}

	/**
	 * Gets the roles that are to be updated.
	 * 
	 * @param allSuperAdminRoles {@link Set} all super admin roles.
	 * @param assignedBatchClassGroups {@link List} all existing group roles.
	 * @return {@link Set} set of new roles.
	 */
	private Set<String> getNonExixtentRoles(Set<String> allSuperAdminRoles, List<BatchClassGroups> assignedBatchClassGroups) {
		Set<String> nonExixtentRolesSet = null;
		if (null != allSuperAdminRoles && null != assignedBatchClassGroups && !allSuperAdminRoles.isEmpty()) {
			String groupName = null;
			nonExixtentRolesSet = new HashSet<String>(allSuperAdminRoles);
			for (BatchClassGroups batchClassGroup : assignedBatchClassGroups) {
				groupName = batchClassGroup.getGroupName();
				if (allSuperAdminRoles.contains(groupName)) {
					nonExixtentRolesSet.remove(groupName);
				}
			}
		}
		return nonExixtentRolesSet;
	}

	/**
	 * Creates the new batch class group.
	 * 
	 * @param groupName {@link String} new batch class name.
	 * @return {@link BatchClassGroups} created batch class group.
	 */
	private BatchClassGroups createBatchClassGroup(String groupName) {
		BatchClassGroups batchClassGroup = new BatchClassGroups();
		batchClassGroup.setGroupName(groupName);
		return batchClassGroup;
	}

}
