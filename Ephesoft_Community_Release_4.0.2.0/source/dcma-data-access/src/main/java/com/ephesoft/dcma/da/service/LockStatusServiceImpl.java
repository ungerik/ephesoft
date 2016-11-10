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

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.common.FeatureName;
import com.ephesoft.dcma.da.dao.LockStatusDao;
import com.ephesoft.dcma.da.domain.LockStatus;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * The <code>LockStatusServiceImpl</code> is a class that implements defined by {@link LockStatusService} to implement business rules
 * for locking mechanism for Ephesoft application features.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see LockStatusService
 * 
 */
@Service
public class LockStatusServiceImpl implements LockStatusService {

	/**
	 * The lockStatusDao {@link LockStatusDao} is used to perform dao operation needed to perform services..
	 */
	@Autowired
	private LockStatusDao lockStatusDao;

	/**
	 * LOGGER {@link Logger} The instance of Logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(LockStatusServiceImpl.class);

	@Override
	@Transactional
	public List<LockStatus> acquireLock(final LockStatus lockStatus) {
		List<LockStatus> listLockStatus = null;
		if (lockStatus == null) {
			LOGGER.info("lockStatus Object is null.");
		} else {
			lockStatusDao.acquireLock(lockStatus);
			listLockStatus = getAllFeaturesLockedByUser(lockStatus.getUserName());
		}
		return listLockStatus;
	}

	@Override
	@Transactional
	public List<LockStatus> releaseLock(final LockStatus lockStatus) {
		List<LockStatus> listLockStatus = null;
		if (lockStatus == null) {
			LOGGER.info("lockStatus Object is null.");
		} else {
			lockStatusDao.releaseLock(lockStatus.getFeatureName());
			listLockStatus = getAllFeaturesLockedByUser(lockStatus.getUserName());
		}
		return listLockStatus;
	}

	@Override
	@Transactional
	public LockStatus getLockStatus(final FeatureName featureName) {
		LockStatus lockStatus = null;
		if (featureName == null) {
			LOGGER.info("Feature Name is null.");
		} else {
			lockStatus = lockStatusDao.getLockStatus(featureName);
		}
		return lockStatus;
	}

	@Override
	@Transactional
	public void releaseAllLocks(final String userName) {
		if (userName == null) {
			LOGGER.info("User Name is null.");
		} else {
			lockStatusDao.releaseAllLocks(userName);
		}
	}

	@Override
	@Transactional
	public List<LockStatus> getAllFeaturesLockedByUser(final String userName) {
		List<LockStatus> listLockStatus = null;
		if (userName == null) {
			LOGGER.info("User Name is null.");
		} else {
			listLockStatus = lockStatusDao.getAllFeaturesLockedByUser(userName);
		}
		return listLockStatus;
	}

}
