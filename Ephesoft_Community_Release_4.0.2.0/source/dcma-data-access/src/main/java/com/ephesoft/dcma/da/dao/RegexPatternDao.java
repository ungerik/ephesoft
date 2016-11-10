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

import com.ephesoft.dcma.core.dao.CacheableDao;
import com.ephesoft.dcma.da.domain.RegexPattern;

/**
 * The interface <code>RegexPatternDao</code> is a database access interface for Regex Pool Service for RegexPattern.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 15-Jul-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public interface RegexPatternDao extends CacheableDao<RegexPattern> {

	/**
	 * Gets regex pattern by identifier.
	 * 
	 * @param identifier {@link Long}
	 * @return {@link RegexPattern} null if action was unsuccessful.
	 */
	RegexPattern getRegexPatternByIdentifier(Long identifier);

	/**
	 * Gets RegexPatterns by regexGroup id.
	 * 
	 * @param identifier {@link Long}
	 * @return {@link List<{@link RegexPattern}>} null if action was unsuccessful.
	 */
	public List<RegexPattern> getRegexPatternsByRegexGroupId(Long identifier);

	/**
	 * Inserts the RegexPattern object.
	 * 
	 * @param regexPattern {@link RegexPattern}
	 * @return boolean true if insertion was successful.
	 */
	boolean insertPattern(RegexPattern regexPattern);

	/**
	 * Updates the RegexPattern object.
	 * 
	 * @param regexPattern {@link RegexPattern}
	 * @return boolean true if update was successful.
	 */
	boolean updatePattern(RegexPattern regexPattern);

	/**
	 * Removes the RegexPattern object.
	 * 
	 * @param regexPattern {@link Long}
	 * @return boolean true if removal was successful.
	 */
	boolean removePattern(Long identifier);

}
