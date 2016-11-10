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

package com.ephesoft.dcma.gwt.core.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The class <code>RegexGroupDTO</code> is a DTO for RegexGroup class.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 24-Jun-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * 
 * @see com.ephesoft.dcma.da.domain.RegexGroup
 */
public class RegexGroupDTO implements IsSerializable {

	/**
	 * Document type from which this regex group is reached. It is used only for breadcrumb.
	 */
	private DocumentTypeDTO docTypeDTO;

	/**
	 * Identifier of regex group.
	 */
	private String identifier;

	/**
	 * Reference to set of regex patterns for the group.
	 */
	private Set<RegexPatternDTO> regexPatternDTOs;

	/**
	 * Reference to name of the group.
	 */
	private String name;

	/**
	 * Reference to type of the group.
	 */
	private String type;

	/**
	 * True if is deleted by user.
	 */
	private boolean deleted;

	/**
	 * True if this is a new regex group.
	 */
	private boolean isNew;

	/**
	 * Sets whether this regex group is new or not.
	 * 
	 * @return boolean
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * Gets whether this regex group is new or not.
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
	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets regex patterns.
	 * 
	 * @param includeDeleted boolean includes the soft deleted patterns if set true.
	 * @return {@link Lists<{@link RegexPatternDTO}>} null if list could not be fetched.
	 */
	public List<RegexPatternDTO> getRegexPatternDTOs(final boolean includeDeleted) {
		List<RegexPatternDTO> regexPatternDTOs = null;
		if (null != this.regexPatternDTOs) {
			regexPatternDTOs = new ArrayList<RegexPatternDTO>(this.regexPatternDTOs.size());
			for (RegexPatternDTO regexPatternDTO : this.regexPatternDTOs) {
				if (null != regexPatternDTO) {
					if (includeDeleted) {
						regexPatternDTOs.add(regexPatternDTO);
					} else if (!regexPatternDTO.isDeleted()) {
						regexPatternDTOs.add(regexPatternDTO);
					}
				}
			}
		}
		return regexPatternDTOs;
	}

	/**
	 * Sets regex patterns.
	 * 
	 * @param regexPatternDTOs {@link Set<{@link RegexPatternDTO}>}
	 */
	public void setRegexPatternDTOs(final Set<RegexPatternDTO> regexPatternDTOs) {
		this.regexPatternDTOs = regexPatternDTOs;
	}

	/**
	 * Adds regex pattern to this regex group.
	 * 
	 * @param regexPatternDTO {@link RegexPatternDTO}
	 * @return true if regex pattern added successfully to set of regex pattern dtos.
	 */
	public boolean addRegexPatternDTO(final RegexPatternDTO regexPatternDTO) {
		boolean success = false;
		if (null != regexPatternDTO && null != regexPatternDTOs) {
			regexPatternDTOs.add(regexPatternDTO);
			success = true;
		}
		return success;
	}

	/**
	 * Removes regex pattern from this regex group.
	 * 
	 * @param regexPatternDTO {@link RegexPatternDTO}
	 * @return true if regex pattern removed successfully from set of regex apttern dtos.
	 */
	public boolean removeRegexPatternDTO(final RegexPatternDTO regexPatternDTO) {
		boolean success = false;
		if (null != regexPatternDTO && null != regexPatternDTOs) {
			regexPatternDTOs.remove(regexPatternDTO);
			success = true;
		}
		return success;
	}

	/**
	 * Gets regex pattern by identifier.
	 * 
	 * @param identifier {@link String}
	 * @return {@link RegexPatternDTO} null if DTO was not fetched.
	 */
	public RegexPatternDTO getRegexPatternDTOById(final String identifier) {
		RegexPatternDTO selectedRegexPatternDTO = null;
		if (null != identifier && null != regexPatternDTOs && !regexPatternDTOs.isEmpty()) {
			for (RegexPatternDTO regexPatternDTO : regexPatternDTOs) {
				if (null != regexPatternDTO && !regexPatternDTO.isDeleted() && identifier.equals(regexPatternDTO.getIdentifier())) {
					selectedRegexPatternDTO = regexPatternDTO;
					break;
				}
			}
		}
		return selectedRegexPatternDTO;
	}

	/**
	 * Gets regex pattern by pattern value.
	 * 
	 * @param pattern {@link String} the name of the regex pattern to be searched.
	 * @return {@link RegexPatternDTO} searched regex pattern DTO, returns <code>null</code> if no such regex pattern is found.
	 */
	public RegexPatternDTO getRegexPatternDTOByPattern(final String pattern) {
		RegexPatternDTO selectedRegexPatternDTO = null;
		if (null != pattern && null != regexPatternDTOs && !regexPatternDTOs.isEmpty()) {
			for (RegexPatternDTO regexPatternDTO : regexPatternDTOs) {
				if (null != regexPatternDTO && !regexPatternDTO.isDeleted() && pattern.equals(regexPatternDTO.getPattern())) {
					selectedRegexPatternDTO = regexPatternDTO;
					break;
				}
			}
		}
		return selectedRegexPatternDTO;
	}

	/**
	 * Checks if the provided value for a new regex pattern is already in use by some other regex pattern in the regex group.
	 * 
	 * @param pattern {@link String} the value of the regex pattern to check for availability.
	 * @return boolean, <code>true</code> if the provided value is not taken by any other existing pattern, <code>false</code>
	 *         otherwise.
	 */
	public boolean checkPattern(final String pattern) {
		boolean isPatternInUse = false;
		if (null != pattern && null != identifier) {
			if (null != getRegexPatternDTOByPattern(pattern)) {
				isPatternInUse = true;
			}
		}
		return isPatternInUse;
	}

	/**
	 * Checks if the edited value for a new regex pattern is already in use by some other regex group.
	 * 
	 * @param pattern {@link String} the edited value of the regex pattern to check for availability.
	 * @param identifier {@link String} identifier of the editing regex pattern.
	 * @return boolean, <code>true</code> if the provided value is not taken by any other existing pattern, <code>false</code>
	 *         otherwise.
	 */
	public boolean checkPattern(final String pattern, final String identifier) {
		boolean isPatternInUse = false;
		if (null != pattern && null != identifier) {
			RegexPatternDTO searchedRegexPatternDTO = getRegexPatternDTOByPattern(pattern);
			if (null != searchedRegexPatternDTO && !identifier.equals(searchedRegexPatternDTO.getIdentifier())) {
				isPatternInUse = true;
			}
		}
		return isPatternInUse;
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
	 * Sets name of the group.
	 * 
	 * @param name {@link String}
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Gets type of the group.
	 * 
	 * @return {@link String}
	 */
	public String getType() {
		return type;
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
	 * Gets regex pattern group is deleted or not.
	 * 
	 * @return boolean
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Sets regex pattern group is deleted or not.
	 * 
	 * @param deleted boolean
	 */
	public void setDeleted(final boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Gets document type DTO for the regex group.
	 * 
	 * @return {@link DocumentTypeDTO}
	 */
	public DocumentTypeDTO getDocTypeDTO() {
		return docTypeDTO;
	}

	/**
	 * Sets document type DTO for the regex group.
	 * 
	 * @param docTypeDTO {@link DocumentTypeDTO}
	 */
	public void setDocTypeDTO(final DocumentTypeDTO docTypeDTO) {
		this.docTypeDTO = docTypeDTO;
	}
}
