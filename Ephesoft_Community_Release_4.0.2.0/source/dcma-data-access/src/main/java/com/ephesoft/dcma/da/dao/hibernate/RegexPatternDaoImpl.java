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

import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.core.hibernate.EphesoftCriteria;
import com.ephesoft.dcma.da.dao.RegexPatternDao;
import com.ephesoft.dcma.da.domain.RegexPattern;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * The class<code>RegexPatternDaoImpl</code> is an implementation of RegexPatternDao interface for RegexPattern.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 15-Jul-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Repository
public final class RegexPatternDaoImpl extends HibernateDao<RegexPattern> implements RegexPatternDao {

	/**
	 * LOGGER to print the Logging information.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(RegexPatternDaoImpl.class);

	/**
	 * String constant for Regex pattern id.
	 */
	private static final String IDENTIFIER = "id";

	@Override
	public RegexPattern getRegexPatternByIdentifier(final Long identifier) {
		RegexPattern regexPattern = null;
		if (identifier != null) {
			final EphesoftCriteria criteria = criteria();
			criteria.add(Restrictions.eq(IDENTIFIER, identifier));
			try {
				regexPattern = this.findSingle(criteria);
			} catch (DataAccessException dataAccessException) {
				LOGGER.error(dataAccessException, "Error in getting regex pattern for pattern id: ", identifier);
			}
		}
		return regexPattern;
	}

	@Override
	public List<RegexPattern> getRegexPatternsByRegexGroupId(final Long identifier) {
		List<RegexPattern> regexPatternList = null;
		if (identifier != null) {
			final EphesoftCriteria criteria = criteria();
			criteria.createAlias("regexGroup", "regexGroup", JoinFragment.INNER_JOIN);
			criteria.add(Restrictions.eq("regexGroup.id", identifier));
			try {
				regexPatternList = find(criteria);
			} catch (DataAccessException dataAccessException) {
				LOGGER.error(dataAccessException, "Error in getting regex patterns for group id: ", identifier);
			}
		}
		return regexPatternList;

	}

	@Override
	public boolean insertPattern(final RegexPattern regexPattern) {
		boolean success = true;
		if (regexPattern == null) {
			success = false;
		} else {
			try {
				create(regexPattern);
			} catch (DataAccessException dataAccessException) {
				LOGGER.error(dataAccessException, "Error in inserting new regex pattern.");
				success = false;
			}
		}
		return success;

	}

	@Override
	public boolean updatePattern(final RegexPattern regexPattern) {
		boolean success = true;
		if (regexPattern == null) {
			success = false;
		} else {
			try {
				saveOrUpdate(regexPattern);
			} catch (DataAccessException dataAccessException) {
				LOGGER.error(dataAccessException, "Error in updating regex patterns for pattern id: ", regexPattern.getId());
				success = false;
			}
		}
		return success;

	}

	@Override
	public boolean removePattern(final Long identifier) {
		final RegexPattern regexPattern = getRegexPatternByIdentifier(identifier);
		boolean success = true;
		if (regexPattern == null) {
			success = false;
		} else {
			try {
				remove(regexPattern);
			} catch (DataAccessException dataAccessException) {
				LOGGER.error(dataAccessException, "Error in removing regex pattern.");
				success = false;
			}
		}
		return success;
	}
}
