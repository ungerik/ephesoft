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

package com.ephesoft.dcma.da.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * The class <code>RegexGroup</code> represents a group of regex patterns. All regex sample group for one type are implemented as
 * instances of this class.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 24-Jun-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Entity
@Table(name = "regex_group", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class RegexGroup extends AbstractChangeableEntity {

	/**
	 * Serial version id of the class.
	 */
	private static final long serialVersionUID = 2350081900483231178L;

	/**
	 * Reference to set of regex patterns for the group.
	 */
	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "regex_group_id")
	private Set<RegexPattern> regexPatterns;

	/**
	 * Reference to name of the group.
	 */
	@Column(name = "name")
	private String name;

	/**
	 * Reference to type of the group.
	 */
	@Column(name = "type")
	private String type;

	/**
	 * Gets regex patterns.
	 * 
	 * @return {@link Set<{@link RegexPattern}>}
	 */
	public Set<RegexPattern> getRegexPatterns() {
		return regexPatterns;
	}

	/**
	 * Sets regex patterns.
	 * 
	 * @param regexPatterns {@link Set<{@link RegexPattern}>}
	 */
	public void setRegexPatterns(final Set<RegexPattern> regexPatterns) {
		this.regexPatterns = regexPatterns;
	}

	/**
	 * Sets name of the group.
	 * 
	 * @param name {@link String}
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Gets name of the group.
	 * 
	 * @return {@link String}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets type of the group.
	 * 
	 * @param type {@link String}
	 */
	public void setType(final String type) {
		this.type = type;
	}

	/**
	 * Gets type of the group.
	 * 
	 * @return {@link String}
	 */
	public String getType() {
		return type;
	}

	@Override
	public boolean equals(final Object object) {
		boolean isEqual;
		if (null != object && (object instanceof RegexGroup)) {
			final RegexGroup regexGroup = (RegexGroup) object;
			isEqual = isGroupIdEqual(regexGroup) && isGroupNameEqual(regexGroup); // It will change if a parent class is added to regex
																					// group. Refer regex pattern's equal method.
		} else {
			isEqual = false;
		}
		return isEqual;
	}

	/**
	 * Checks if group ids are equal.
	 * 
	 * @param regexGroup {@link RegexGroup}
	 * @return boolean true if group ids are equal.
	 */
	private boolean isGroupIdEqual(final RegexGroup regexGroup) {
		boolean isEqual;
		if (null != regexGroup && regexGroup.getId() == id) {
			isEqual = true;
		} else {
			isEqual = false;
		}
		return isEqual;
	}

	/**
	 * Checks if group names are equal.
	 * 
	 * @param regexGroup {@link RegexGroup}
	 * @return boolean true if group names are equal.
	 */
	private boolean isGroupNameEqual(final RegexGroup regexGroup) {
		boolean isEqual;
		if (null != regexGroup && null != name && name.equals(regexGroup.getName())) {
			isEqual = true;
		} else {
			isEqual = false;
		}
		return isEqual;
	}
	/**
	 * Checks If regex pattern already exist in set.
	 * 
	 * @param regexPattern{@link RegexPattern} whose existance is to be checked in set present ,on the basis of patterns already
	 *            present.
	 * 
	 * @return status whether regex pattern is already present in list or not.
	 */
	public boolean isRegexPatternExist(final RegexPattern regexPattern) {
		boolean isRegexPatternPresent = false;
		if (regexPatterns != null) {
			for (RegexPattern pattern : regexPatterns) {
				if (pattern != null && pattern.getPattern().equals(regexPattern.getPattern())) {
					isRegexPatternPresent = true;
					break;
				}
			}
		}
		return isRegexPatternPresent;
	}

	/**
	 * Adds a regex pattern to the set already present.
	 * 
	 * @param regexPattern {@link RegexPattern} to be added to regex group.
	 */
	public void addRegexPattern(final RegexPattern regexPattern) {
		if (regexPattern != null) {
			if (regexPatterns == null) {
				regexPatterns = new LinkedHashSet<RegexPattern>();
			}
			regexPatterns.add(regexPattern);
		}
	}
}
