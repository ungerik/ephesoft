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

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.core.hibernate.EphesoftCriteria;
import com.ephesoft.dcma.da.dao.RegexGroupDao;
import com.ephesoft.dcma.da.domain.RegexGroup;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * The class <code>RegexGroupDaoImpl</code>is an implementation of RegexGroupDao for RegexGroup.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 24-Jun-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Repository
public final class RegexGroupDaoImpl extends HibernateDao<RegexGroup> implements RegexGroupDao {

	/**
	 * LOGGER to print the Logging information.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(RegexGroupDaoImpl.class);

	/**
	 * String constant for Regex group type.
	 */
	private static final String TYPE = "type";

	/**
	 * String constant for Regex group id.
	 */
	private static final String IDENTIFIER = "id";

	@Override
	public RegexGroup getRegexGroupByIdentifier(final Long identifier) {
		RegexGroup regexGroup = null;
		if (identifier != null) {
			final EphesoftCriteria criteria = criteria();
			criteria.add(Restrictions.eq(IDENTIFIER, identifier));
			try {
				regexGroup = this.findSingle(criteria);
			} catch (DataAccessException dataAccessException) {
				LOGGER.error(dataAccessException, "Error in getting regex group for group id: ", identifier);
			}
		}
		return regexGroup;
	}

	@Override
	public List<RegexGroup> getRegexGroups(final String type) {
		List<RegexGroup> regexGroupList = null;
		if (type != null) {
			final EphesoftCriteria criteria = criteria();
			final Criterion typeCriteria = Restrictions.eq(TYPE, type);
			criteria.add(typeCriteria);
			try {
				regexGroupList = find(criteria);
			} catch (DataAccessException dataAccessException) {
				LOGGER.error(dataAccessException, "Error in getting regex groups for type: ", type);
			}
		}
		return regexGroupList;
	}

	@Override
	public List<RegexGroup> getRegexGroups() {
		List<RegexGroup> regexGroupList = null;
		final EphesoftCriteria criteria = criteria();
		try {
			regexGroupList = find(criteria);
		} catch (DataAccessException dataAccessException) {
			LOGGER.error(dataAccessException, "Error in getting regex groups");
		}
		return regexGroupList;
	}

	@Override
	public boolean insertGroup(final RegexGroup regexGroup) {
		boolean success = true;
		if (regexGroup == null) {
			success = false;
		} else {
			try {
				create(regexGroup);
			} catch (DataAccessException dataAccessException) {
				LOGGER.error(dataAccessException, "Error in inserting new regex group.");
				success = false;
			}
		}
		return success;
	}

	@Override
	public boolean removeGroup(final Long identifier) {
		boolean success = true;
		final RegexGroup regexGroup = getRegexGroupByIdentifier(identifier);
		if (regexGroup == null) {
			success = false;
		} else {
			try {
				remove(regexGroup);
			} catch (DataAccessException dataAccessException) {
				LOGGER.error(dataAccessException, "Error in removing regex group with group id: ", identifier);
				success = false;
			}
		}
		return success;

	}

	@Override
	public boolean updateGroup(final RegexGroup regexGroup) {
		boolean success = true;
		if (regexGroup == null) {
			success = false;
		} else {
			try {
				saveOrUpdate(regexGroup);
			} catch (DataAccessException dataAccessException) {
				LOGGER.error(dataAccessException, "Error in updating regex group with group id:", regexGroup.getId());
				success = false;
			}
		}
		return success;
	}
}
