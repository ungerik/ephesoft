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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.service.DataAccessService;
import com.ephesoft.dcma.da.dao.RegexGroupDao;
import com.ephesoft.dcma.da.domain.RegexGroup;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * This class is an implementation of RegexGroupService class.
 * 
 * @author Ephesoft
 * @version 1.0
 */
@Service
public final class RegexGroupServiceImpl extends DataAccessService implements RegexGroupService {

	/**
	 * LOGGER to print the Logging information.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(RegexGroupServiceImpl.class);

	/**
	 * String constant for Regex Group type log message.
	 */
	private static final String TYPE_MSG = "Regex Group type is : ";

	/**
	 * String constant for Regex Group name log message.
	 */
	private static final String NAME_MSG = "Regex Group name is : ";

	/**
	 * String constant for Regex pattern object null log message.
	 */
	private static final String GROUP_NULL_MSG = "RegexGroup object is null.";

	/**
	 * Dao object for database access.
	 */
	@Autowired
	private RegexGroupDao regexGroupDao;

	@Transactional(readOnly = true)
	@Override
	public RegexGroup getRegexGroupByIdentifier(final Long identifier) {
		LOGGER.info(EphesoftStringUtil.concatenate("Entering service method to get Regex Group by group identifier: ", identifier));
		RegexGroup regexGroup = null;
		if (identifier != null) {
			regexGroup = regexGroupDao.getRegexGroupByIdentifier(identifier);
		}
		if (regexGroup == null) {
			LOGGER.info(GROUP_NULL_MSG);
		} else {
			LOGGER.debug(EphesoftStringUtil.concatenate("Regex Group is: ", regexGroup));
		}
		return regexGroup;
	}

	@Transactional(readOnly = true)
	@Override
	public List<RegexGroup> getRegexGroups(final String type) {
		LOGGER.info(EphesoftStringUtil.concatenate("Entering service method to get Regex Groups of type: ", type));
		List<RegexGroup> regexGroupList = null;
		if (type != null) {
			regexGroupList = regexGroupDao.getRegexGroups(type);
		}
		if (regexGroupList == null) {
			LOGGER.info("Regex Group List is null.");
		} else {
			LOGGER.debug(EphesoftStringUtil.concatenate("Regex Pattern List size is: ", regexGroupList.size()));
		}
		return regexGroupList;
	}

	@Transactional(readOnly = true)
	@Override
	public List<RegexGroup> getRegexGroups() {
		LOGGER.trace("Entering service method to get Regex Groups");
		List<RegexGroup> regexGroupList = null;
		regexGroupList = regexGroupDao.getRegexGroups();
		if (regexGroupList == null) {
			LOGGER.info("Regex Group List is null.");
		} else {
			LOGGER.debug(EphesoftStringUtil.concatenate("Regex Pattern List size is: ", regexGroupList.size()));
		}
		return regexGroupList;
	}

	@Transactional
	@Override
	public boolean removeRegexGroup(final Long identifier) {
		boolean success = true;
		if (null == identifier) {
			LOGGER.error("Regex Group identifier is null.");
			success = false;
		} else {
			success = regexGroupDao.removeGroup(identifier);
		}
		if (success) {
			LOGGER.info("Remove regex pattern operation successfully done.");
		} else {
			LOGGER.error("Remove regex pattern operation could not be performed.");
		}
		return success;
	}

	@Transactional
	@Override
	public boolean insertRegexGroup(final RegexGroup regexGroup) {
		boolean success = true;
		if (null == regexGroup) {
			LOGGER.info(GROUP_NULL_MSG);
			success = false;
		} else {
			LOGGER.debug(EphesoftStringUtil.concatenate(TYPE_MSG, regexGroup.getType()));
			LOGGER.debug(EphesoftStringUtil.concatenate(NAME_MSG, regexGroup.getName()));
			success = regexGroupDao.insertGroup(regexGroup);
		}
		if (success) {
			LOGGER.info("Insert regex group operation successfully done.");
		} else {
			LOGGER.error("Insert regex group operation could not be performed.");
		}
		return success;
	}

	@Transactional
	@Override
	public boolean updateRegexGroup(final RegexGroup regexGroup) {
		boolean success = true;
		if (null == regexGroup) {
			LOGGER.error(GROUP_NULL_MSG);
			success = false;
		} else {
			LOGGER.debug(EphesoftStringUtil.concatenate(TYPE_MSG, regexGroup.getType()));
			LOGGER.debug(EphesoftStringUtil.concatenate(NAME_MSG, regexGroup.getName()));

			regexGroupDao.saveOrUpdate(regexGroup);
			success = true;
		}
		if (success) {
			LOGGER.info("Update regex group operation successfully done.");
		} else {
			LOGGER.error("Update regex group operation could not be performed.");
		}
		return success;
	}

}
