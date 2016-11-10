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

package com.ephesoft.gxt.core.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The class <code>RegexPatternDTO</code> is a DTO for RegexPattern class.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 15-Jul-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * 
 * @see com.ephesoft.dcma.da.domain.RegexPattern
 */
public class RegexPatternDTO implements IsSerializable, Selectable {

	/**
	 * Identifier of regex pattern.
	 */
	private String identifier;

	/**
	 * Reference for regex group to which the pattern belongs to.
	 */
	private RegexGroupDTO regexGroupDTO;

	/**
	 * Reference for regexPattern.
	 */
	private String pattern;

	/**
	 * Reference for description about regex Pattern.
	 */
	private String description;

	/**
	 * True if is deleted by user.
	 */
	private boolean deleted;

	/**
	 * True if this is a new regex pattern.
	 */
	private boolean isNew;

	/** The selected. */
	private boolean selected;

	/**
	 * Sets whether this regex pattern is new or not.
	 * 
	 * @return boolean
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * Gets whether this regex pattern is new or not.
	 * 
	 * @param isNew boolean
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * Gets identifier.
	 * 
	 * @return {@link String}
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets identifier.
	 * 
	 * @param identifier {@link String}
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets regex group.
	 * 
	 * @return {@link RegexGroupDTO}
	 */
	public RegexGroupDTO getRegexGroupDTO() {
		return regexGroupDTO;
	}

	/**
	 * Sets regex group.
	 * 
	 * @param {@link RegexGroupDTO}
	 */
	public void setRegexGroupDTO(final RegexGroupDTO regexGroupDTO) {
		this.regexGroupDTO = regexGroupDTO;
	}

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
	 * Gets description.
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
	 * Gets regex pattern is deleted or not.
	 * 
	 * @return boolean
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Sets regex pattern is deleted or not.
	 * 
	 * @param deleted boolean
	 */
	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}

	@Override
	public boolean equals(final Object object) {
		if (null == object) {
			return false;
		}
		if (!(object instanceof RegexPatternDTO)) {
			return false;
		}
		RegexPatternDTO regexPatternDTO = (RegexPatternDTO) object;
		if (regexPatternDTO.getPattern().equals(pattern)
				&& regexGroupDTO.getIdentifier() == regexPatternDTO.getRegexGroupDTO().getIdentifier()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Long.valueOf(identifier).hashCode();
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		// TODO Auto-generated method stub
		this.selected = selected;
	}

}
