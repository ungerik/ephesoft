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

package com.ephesoft.gxt.admin.server.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.da.domain.RegexGroup;
import com.ephesoft.dcma.da.domain.RegexPattern;
import com.ephesoft.dcma.da.service.RegexGroupService;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.dto.RegexPatternDTO;

/**
 * The class <code>RegexUtil</code> is a utility for performing different operation on regex pattern related objects.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.server
 */
public final class RegexUtil {

	/**
	 * Constructor.
	 */
	private RegexUtil() {
		// Do nothing.
	}

	/**
	 * Converts a RegexGroup object to RegexGroupDTO object.
	 * 
	 * @param regexGroup {@link RegexGroup}
	 * @return {@link RegexGroupDTO} null if regexGroup is null
	 */
	public static RegexGroupDTO convertRegexGroupToRegexGroupDTO(final RegexGroup regexGroup) {
		RegexGroupDTO regexGroupDTO = null;
		if (null != regexGroup) {
			final Set<RegexPattern> regexPatterns = regexGroup.getRegexPatterns();
			if (null != regexPatterns) {
				regexGroupDTO = new RegexGroupDTO();
				regexGroupDTO.setIdentifier(String.valueOf(regexGroup.getId()));
				regexGroupDTO.setName(regexGroup.getName());
				regexGroupDTO.setType(regexGroup.getType());
				final Set<RegexPatternDTO> regexPatternDTOs = getRegexPatternDTOs(regexGroupDTO, regexPatterns);
				if (null == regexPatternDTOs) {
					regexGroupDTO.setRegexPatternDTOs(new LinkedHashSet<RegexPatternDTO>(0));

				} else {
					regexGroupDTO.setRegexPatternDTOs(regexPatternDTOs);
				}
			}
		}
		return regexGroupDTO;
	}

	/**
	 * Gets regex patterns for a group.
	 * 
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @param regexPattern {@link RegexPattern}
	 * @return {@link Set<{@link RegexPatternDTO}>} null if regexGroupDTO or regexPatterns is null.
	 */
	private static Set<RegexPatternDTO> getRegexPatternDTOs(final RegexGroupDTO regexGroupDTO, final Set<RegexPattern> regexPatterns) {
		Set<RegexPatternDTO> regexPatternDTOs = null;
		if (null != regexPatterns && null != regexGroupDTO) {
			regexPatternDTOs = new LinkedHashSet<RegexPatternDTO>(regexPatterns.size());
			for (RegexPattern regexPattern : regexPatterns) {
				if (null != regexPattern) {
					final RegexPatternDTO regexPatternDTO = convertRegexPatternToRegexPatternDTO(regexGroupDTO, regexPattern);
					if (null != regexPatternDTO && !(regexPatternDTO.isDeleted())) {
						regexPatternDTOs.add(regexPatternDTO);
					}
				}
			}
		}
		return regexPatternDTOs;
	}

	/**
	 * Converts a RegexPattern object to RegexPatternDTO object.
	 * 
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @param regexPattern {@link RegexPattern}
	 * @return {@link RegexPatternDTO} null if regexPattern is null
	 */
	private static RegexPatternDTO convertRegexPatternToRegexPatternDTO(final RegexGroupDTO regexGroupDTO,
			final RegexPattern regexPattern) {
		RegexPatternDTO regexPatternDTO = null;
		if (null != regexPattern) {
			regexPatternDTO = new RegexPatternDTO();
			regexPatternDTO.setIdentifier(String.valueOf(regexPattern.getId()));
			regexPatternDTO.setDescription(regexPattern.getDescription());
			regexPatternDTO.setPattern(regexPattern.getPattern());
			regexPatternDTO.setRegexGroupDTO(regexGroupDTO);
		}
		return regexPatternDTO;
	}

	/**
	 * Converts a RegexGroupDTO object to RegexGroup object.
	 * 
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @return {@link RegexGroup} null if regexGroupDTO is null
	 */
	public static RegexGroup convertRegexGroupDTOToRegexGroup(final RegexGroupDTO regexGroupDTO) {
		RegexGroup regexGroup = null;
		if (null != regexGroupDTO) {
			final List<RegexPatternDTO> regexPatternDTOs = regexGroupDTO.getRegexPatternDTOs(true);
			if (null != regexPatternDTOs) {
				regexGroup = new RegexGroup();
				regexGroup.setName(regexGroupDTO.getName());
				regexGroup.setType(regexGroupDTO.getType());
				if (regexGroupDTO.isNew()) {
					regexGroup.setId(0);
				} else {
					regexGroup.setId(Integer.valueOf(regexGroupDTO.getIdentifier()));
				}
				final Set<RegexPattern> regexPatterns = getRegexPatterns(regexGroup, regexPatternDTOs);
				regexGroup.setRegexPatterns(regexPatterns);
			}
		}
		return regexGroup;
	}

	/**
	 * Gets list of regex patterns from a list of regex pattern DTOs for a group.
	 * 
	 * @param regexGroup {@link RegexGroup}
	 * @param regexPatternDTOList {@link List<{@link RegexPatternDTO}>}
	 * @return {@link Set<{@link RegexPattern}>}
	 */
	private static Set<RegexPattern> getRegexPatterns(final RegexGroup regexGroup, final List<RegexPatternDTO> regexPatternDTOList) {
		Set<RegexPattern> regexPatterns = null;
		if (null != regexPatternDTOList) {
			regexPatterns = new LinkedHashSet<RegexPattern>();
			for (RegexPatternDTO regexPatternDTO : regexPatternDTOList) {
				if (null != regexPatternDTO && !regexPatternDTO.isDeleted()) {
					final RegexPattern regexPattern = convertRegexPatternDTOToRegexPattern(regexPatternDTO, regexGroup);
					regexPatterns.add(regexPattern);
				}
			}
		}
		return regexPatterns;
	}

	/**
	 * Converts a RegexPatternDTO object to RegexPattern object.
	 * 
	 * @param regexPatternDTO {@link RegexPatternDTO}
	 * @param regexGroup {@link RegexGroup}
	 * @return {@link RegexPattern} null if regexPatternDTO is null
	 */
	private static RegexPattern convertRegexPatternDTOToRegexPattern(final RegexPatternDTO regexPatternDTO, final RegexGroup regexGroup) {
		RegexPattern regexPattern = null;
		if (null != regexPatternDTO) {
			regexPattern = new RegexPattern();
			if (regexPatternDTO.isNew()) {
				// regexPattern.setCustom(true);
				regexPattern.setId(0);
			} else {
				try {
					regexPattern.setId(Integer.valueOf(regexPatternDTO.getIdentifier()));
				} catch (NumberFormatException numberException) {
					regexPattern.setId(0);
				}
			}
			regexPattern.setDescription(regexPatternDTO.getDescription());
			regexPattern.setPattern(regexPatternDTO.getPattern());
			regexPattern.setRegexGroup(regexGroup);
		}
		return regexPattern;
	}

	/**
	 * Updates regex pool.
	 * 
	 * @param regexGroupDTOs {@link Collection<{@link RegexGroupDTO}>}
	 * @return boolean true if update was successful.
	 */
	@Transactional
	public static boolean updateRegexPool(final Collection<RegexGroupDTO> regexGroupDTOs) {
		boolean success = true;
		if (null != regexGroupDTOs) {
			final List<RegexGroup> listOfUpdatedOrNewGroups = new ArrayList<RegexGroup>();
			final List<RegexGroup> listOfGroupsToBeDeleted = new ArrayList<RegexGroup>();

			for (RegexGroupDTO regexGroupDTO : regexGroupDTOs) {
				addToUpdateDeleteLists(regexGroupDTO, listOfUpdatedOrNewGroups, listOfGroupsToBeDeleted);
			}
			boolean deleteSuccess = deleteRegexGroups(listOfGroupsToBeDeleted);
			boolean updateSuccess = updateRegexGroups(listOfUpdatedOrNewGroups);
			success |= (deleteSuccess && updateSuccess);
		}
		return success;
	}

	/**
	 * Adds groups to be deleted or updated in their respected lists.
	 * 
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @param listOfUpdatedOrNewGroups {@link List<{@link RegexGroup}>}
	 * @param listOfGroupsToBeDeleted {@link List<{@link RegexGroup}>}
	 */
	private static void addToUpdateDeleteLists(final RegexGroupDTO regexGroupDTO, final List<RegexGroup> listOfUpdatedOrNewGroups,
			final List<RegexGroup> listOfGroupsToBeDeleted) {
		if (null != regexGroupDTO) {
			final RegexGroup regexGroup = convertRegexGroupDTOToRegexGroup(regexGroupDTO);
			if (null != regexGroup) {
				if (regexGroupDTO.isDeleted()) {
					listOfGroupsToBeDeleted.add(regexGroup);
				} else {
					listOfUpdatedOrNewGroups.add(regexGroup);
				}
			}
		}
	}

	/**
	 * Update regex groups.
	 * 
	 * @param listOfUpdatedOrNewGroups {@link List<{@link RegexGroup}>}
	 * @return boolean true if update was successful.
	 */
	private static boolean updateRegexGroups(final List<RegexGroup> listOfUpdatedOrNewGroups) {
		boolean updateSuccess = true;
		if (null != listOfUpdatedOrNewGroups && !listOfUpdatedOrNewGroups.isEmpty()) {
			RegexGroupService regexGroupService = EphesoftContext.get(RegexGroupService.class);
			for (RegexGroup regexGroup : listOfUpdatedOrNewGroups) {
				if (null != regexGroup && updateSuccess) {
					updateSuccess &= regexGroupService.updateRegexGroup(regexGroup);
				}
			}
		}
		return updateSuccess;
	}

	/**
	 * Deletes regex groups.
	 * 
	 * @param listOfGroupsToBeDeleted {@link List<{@link RegexGroup}>}
	 * @return boolean true if deletion was successful.
	 */
	private static boolean deleteRegexGroups(final List<RegexGroup> listOfGroupsToBeDeleted) {
		boolean deleteSuccess = true;
		if (listOfGroupsToBeDeleted != null && !listOfGroupsToBeDeleted.isEmpty()) {
			RegexGroupService regexGroupService = EphesoftContext.get(RegexGroupService.class);
			for (RegexGroup regexGroup : listOfGroupsToBeDeleted) {
				long regexGroupId = regexGroup.getId();
				if (null != regexGroup && deleteSuccess) {
					deleteSuccess &= regexGroupService.removeRegexGroup(regexGroupId);
				}
			}
		}
		return deleteSuccess;
	}

	/**
	 * Setting parent of regex pattern to "null" and identifier to "0" for export.
	 * 
	 * @param regexGroupList List<RegexGroup>
	 */
	public static void exportRegexPatterns(List<RegexGroup> regexGroupList) {
		if (null != regexGroupList) {
			Set<RegexPattern> regexPatternList = null;
			Set<RegexPattern> newRegexPatternList = null;
			for (RegexGroup regexGroup : regexGroupList) {
				regexPatternList = regexGroup.getRegexPatterns();
				newRegexPatternList = new HashSet<RegexPattern>(regexPatternList.size());
				for (RegexPattern regexPattern : regexPatternList) {
					newRegexPatternList.add(regexPattern);
					regexPattern.setId(0);
					regexPattern.setRegexGroup(null);
				}
				regexGroup.setRegexPatterns(newRegexPatternList);
			}
		}
	}

	/**
	 * Checks if {@link RegexGroup} is present in list provided.
	 * 
	 * @param regexGroupList {@Link List<{@link RegexGroup}>} in which presence of specific regex group is searched.
	 * @param regexGroup {@link RegexGroup} is to be searched in list on the basis of its name.
	 * @return {@link RegexGroup} which is found in list provided, if not present then returns null.
	 */
	public static RegexGroup getRegexGroupFromList(final List<RegexGroup> regexGroupList, final RegexGroup regexGroup) {
		RegexGroup resultedRegexGroup = null;
		if (regexGroupList != null && regexGroup != null) {
			for (RegexGroup currentRegexGroup : regexGroupList) {
				if (currentRegexGroup != null) {
					String currentRegexGroupName = currentRegexGroup.getName();
					if (currentRegexGroupName != null && currentRegexGroupName.equalsIgnoreCase(regexGroup.getName())) {
						resultedRegexGroup = currentRegexGroup;
						break;
					}
				}
			}
		}
		return resultedRegexGroup;
	}
}
