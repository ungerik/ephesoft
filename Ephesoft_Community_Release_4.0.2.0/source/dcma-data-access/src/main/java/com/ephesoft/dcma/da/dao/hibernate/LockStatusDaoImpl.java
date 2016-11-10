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

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.common.FeatureName;
import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.LockStatusDao;
import com.ephesoft.dcma.da.domain.LockStatus;

/**
 * The <code>LockStatusDaoImpl</code> is the implementation of {@link LockStatusDao}.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 10-Jul-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Repository
public class LockStatusDaoImpl extends HibernateDao<LockStatus> implements LockStatusDao {

	/**
	 * FEATURE_NAME {@link String} represents the constant for the feature name.
	 */
	private static final String FEATURE_NAME = "featureName";

	/**
	 * USER_NAME {@link String} represents the constant for the user name.
	 */
	private static final String USER_NAME = "userName";

	/**
	 * IS_LOCKED {@link String} represents the constant whether the application feature is currently locked or not.
	 */
	private static final String IS_LOCKED = "isLocked";

	@Override
	public void acquireLock(final LockStatus lockStatus) {
		saveOrUpdate(lockStatus);
	}

	@Override
	public LockStatus getLockStatus(final FeatureName featureName) {
		final DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(FEATURE_NAME, featureName));
		return findSingle(criteria);
	}

	@Override
	public void releaseLock(final FeatureName featureName) {
		final LockStatus lockStatus = getLockStatus(featureName);
		if (lockStatus != null) {
			lockStatus.setLocked(false);
			lockStatus.setFeatureName(featureName);
			lockStatus.setUserName(null);
			saveOrUpdate(lockStatus);
		}
	}

	@Override
	public void releaseAllLocks(final String userName) {
		final List<LockStatus> listLockStatus = getAllFeaturesLockedByUser(userName);
		if (listLockStatus != null) {
			for (LockStatus lockStatus : listLockStatus) {
				if (lockStatus != null) {
					lockStatus.setLocked(false);
					lockStatus.setUserName(null);
					saveOrUpdate(lockStatus);
				}
			}
		}

	}

	@Override
	public List<LockStatus> getAllFeaturesLockedByUser(final String userName) {
		final DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(USER_NAME, userName));
		criteria.add(Restrictions.eq(IS_LOCKED, true));
		return find(criteria);
	}
}
