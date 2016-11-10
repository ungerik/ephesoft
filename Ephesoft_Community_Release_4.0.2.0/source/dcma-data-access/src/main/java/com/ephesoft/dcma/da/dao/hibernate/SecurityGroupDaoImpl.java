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
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.SecurityGroupDao;
import com.ephesoft.dcma.da.domain.SecurityGroup;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * Provides the implementation of <code>SecurityGroupDao</code>.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Repository
public class SecurityGroupDaoImpl extends HibernateDao<SecurityGroup> implements
		SecurityGroupDao {

	/**
	 * Logger of <code>SecurityUserDaoImpl</code> class.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory
			.getLogger(SecurityGroupDaoImpl.class);

	/**
	 * Constant for group name property in the entity class.
	 */
	private static final String GROUP_NAME = "groupName";

	/**
	 * Constant for is super admin property in the entity class.
	 */
	private static final String IS_SUPER_ADMIN = "isSuperAdmin";

	@Override
	public Set<String> getAllGroups() {
		LOGGER.debug("Fetching all the group names in database");
		Set<String> groupNameSet = null;
		final DetachedCriteria criteria = this.criteria();
		if (null != criteria) {
			criteria.setProjection(Projections.property(GROUP_NAME));
			final List<String> groupNameList = this.find(criteria);
			if (null != groupNameList && !groupNameList.isEmpty()) {
				groupNameSet = new HashSet<String>(groupNameList);
			}
		}
		LOGGER.debug("Group Name set is: ", groupNameSet);
		return groupNameSet;
	}

	@Override
	public Set<String> getAllSuperAdminGroups() {
		LOGGER.debug("Fetching all super admin groups in database.");
		Set<String> superAdminSet = null;
		final DetachedCriteria criteria = this.criteria();
		if (null != criteria) {
			criteria.setProjection(Projections.property(GROUP_NAME));
			criteria.add(Restrictions.eq(IS_SUPER_ADMIN, true));
			final List<String> superAdminList = this.find(criteria);
			if (null != superAdminList && !superAdminList.isEmpty()) {
				superAdminSet = new HashSet<String>(superAdminList);
			}
		}
		LOGGER.debug("Super Admin Set is: ", superAdminSet);
		return superAdminSet;
	}

	@Override
	public SecurityGroup findSecurityGroupByName(final String groupName) {
		LOGGER.debug("Finding group with the given group name");
		SecurityGroup group = null;
		if (!EphesoftStringUtil.isNullOrEmpty(groupName)) {
			final DetachedCriteria criteria = this.criteria();
			if (null != criteria) {
				criteria.add(Restrictions.eq(GROUP_NAME, groupName));
				group = this.findSingle(criteria);
			}
		}
		LOGGER.debug("Group with group name ", groupName,
				(null == group) ? " not found." : "founded.");
		return group;
	}

}
