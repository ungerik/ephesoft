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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.SecurityUserDao;
import com.ephesoft.dcma.da.domain.SecurityGroup;
import com.ephesoft.dcma.da.domain.SecurityUser;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * Provides the implementation of <code>SecurityUserDao</code>.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Repository
public class SecurityUserDaoImpl extends HibernateDao<SecurityUser> implements SecurityUserDao {

	/**
	 * Logger of <code>SecurityUserDaoImpl</code> class.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(SecurityUserDaoImpl.class);

	/**
	 * Constant for user name property in the entity class.
	 */
	private static final String USER_NAME = "userName";

	@Override
	public Set<String> getAllUser() {
		LOGGER.debug("Fetching all users from Database.");
		Set<String> userNameSet = null;
		final DetachedCriteria criteria = criteria();
		if (null != criteria) {
			criteria.setProjection(Projections.property(USER_NAME));
			final List<String> userNameList = this.find(criteria);
			if (null != userNameList && !userNameList.isEmpty()) {
				userNameSet = new HashSet<String>(userNameList);
			}
		}
		LOGGER.debug("User Set is: ", userNameSet);
		return userNameSet;
	}

	@Override
	public Set<String> getUserRoles(final String userName) {
		LOGGER.debug("Fetching all user groups from database.");
		Set<String> userRolesSet = null;
		if (!EphesoftStringUtil.isNullOrEmpty(userName)) {
			SecurityUser user = null;
			user = findSecurityUserByName(userName);
			if (null != user) {
				userRolesSet = getAllGroupName(user.getUserGroups());
			}
		}
		LOGGER.debug("User Group Set for ", userName, " is: ", userRolesSet);
		return userRolesSet;
	}

	@Override
	public boolean isSuperAdmin(String userName) {
		LOGGER.debug("Checking of the user is super admin.");
		boolean isSuperAdmin = false;
		if (!EphesoftStringUtil.isNullOrEmpty(userName)) {
			SecurityUser user = null;
			user = findSecurityUserByName(userName);
			if (null != user) {
				isSuperAdmin = checkForSuperAdmin(user.getUserGroups());
			}
		}
		LOGGER.debug("User ", userName, " super admin status: ", isSuperAdmin);
		return isSuperAdmin;
	}

	@Override
	public SecurityUser findSecurityUserByName(final String userName) {
		LOGGER.debug("Finding user with the given user name");
		SecurityUser user = null;
		if (!EphesoftStringUtil.isNullOrEmpty(userName)) {
			final DetachedCriteria criteria = this.criteria();
			if (null != criteria) {
				criteria.add(Restrictions.eq(USER_NAME, userName));
				user = this.findSingle(criteria);
			}
		}
		LOGGER.debug("User with user name ", userName, (null == user) ? " not founded." : "found.");
		return user;
	}

	/**
	 * Gets the group name set from the user assigned user groups.
	 * 
	 * @param userGroups {@link SecurityGroup} set of security group assigned to user.
	 * @return {@link String} set of group names.
	 */
	private Set<String> getAllGroupName(final Set<SecurityGroup> userGroups) {
		Set<String> groupNameSet = null;
		if (null != userGroups && !userGroups.isEmpty()) {
			SecurityGroup securityGroup = null;
			groupNameSet = new HashSet<String>(userGroups.size());
			for (Iterator<SecurityGroup> iterator = userGroups.iterator(); iterator.hasNext();) {
				securityGroup = (SecurityGroup) iterator.next();
				groupNameSet.add(securityGroup.getGroupName());
			}
		}
		return groupNameSet;
	}

	/**
	 * Checks for a super admin group in the groups assigned to user.
	 * 
	 * @param userGroups {@link SecurityGroup} set of security group assigned to user.
	 * @return <code>true/false</code> if any user groups belongs to super admin role.
	 */
	private boolean checkForSuperAdmin(final Set<SecurityGroup> userGroups) {
		boolean isSuperAdmin = false;
		if (null != userGroups && !userGroups.isEmpty()) {
			SecurityGroup securityGroup = null;
			for (Iterator<SecurityGroup> iterator = userGroups.iterator(); iterator.hasNext();) {
				securityGroup = (SecurityGroup) iterator.next();
				if (securityGroup.isSuperAdmin()) {
					isSuperAdmin = true;
					break;
				}
			}
		}
		return isSuperAdmin;
	}
}
