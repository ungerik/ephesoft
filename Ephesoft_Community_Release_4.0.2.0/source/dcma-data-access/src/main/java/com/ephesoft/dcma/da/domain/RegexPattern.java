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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * The class <code>RegexPattern</code> represents a regex pattern. All regex patters learnt in Ephesoft, such as "\d{1,3}" with
 * description "One to three digits", are implemented as instances of this class.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
@Entity
@Table(name = "regex_pattern", uniqueConstraints = @UniqueConstraint(columnNames = {"regex_group_id", "pattern"}))
public class RegexPattern extends AbstractChangeableEntity {

	/**
	 * Serial version id of the class.
	 */
	private static final long serialVersionUID = -99545603275613794L;

	/**
	 * Reference for regex group to which the pattern belongs to.
	 */
	@OneToOne
	@JoinColumn(name = "regex_group_id")
	private RegexGroup regexGroup;

	/**
	 * Reference for pattern.
	 */
	@Column(name = "pattern")
	private String pattern;

	/**
	 * Reference for description about regex Pattern.
	 */
	@Column(name = "description")
	private String description;

	/**
	 * Gets pattern.
	 * 
	 * @return {@link String}
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * Sets pattern.
	 * 
	 * @param {@link String}
	 */
	public void setPattern(final String pattern) {
		this.pattern = pattern;
	}

	/**
	 * Gets pattern.
	 * 
	 * @return {@link String}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description.
	 * 
	 * @param {@link String}
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Sets regex group.
	 * 
	 * @param {@link RegexGroup}
	 */
	public void setRegexGroup(final RegexGroup regexGroup) {
		this.regexGroup = regexGroup;
	}

	/**
	 * Gets regex group.
	 * 
	 * @return {@link RegexGroup}
	 */
	public RegexGroup getRegexGroup() {
		return regexGroup;
	}

	@Override
	public boolean equals(final Object object) {
		boolean isEqual;
		if (null != object && (object instanceof RegexPattern)) {
			final RegexPattern regexPattern = (RegexPattern) object;
			isEqual = isRegexPatternEqual(regexPattern);
		} else {
			isEqual = false;
		}
		return isEqual;
	}

	/**
	 * Checks if regex pattern objects are equal or not.
	 * 
	 * @param regexPattern {@link RegexPattern}
	 * @return boolean true if objects are equal.
	 */
	private boolean isRegexPatternEqual(final RegexPattern regexPattern) {
		boolean isEqual;
		if (isPatternEqual(regexPattern) && null != regexGroup && regexGroup.equals(regexPattern.getRegexGroup())) {
			isEqual = true;
		} else {
			isEqual = false;
		}
		return isEqual;
	}
	
	/**
	 * Checks whether patterns are equal or not.
	 * 
	 * @param regexPattern {@link RegexPattern}
	 * @return boolean true if pattern string are equal.
	 */
	private boolean isPatternEqual(final RegexPattern regexPattern) {
		boolean isEqual;
		if (null != regexPattern && null != pattern && pattern.equals(regexPattern.getPattern())) {
			isEqual = true;
		} else {
			isEqual = false;
		}
		return isEqual;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}
}
