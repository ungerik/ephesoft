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

package com.ephesoft.gxt.admin.client.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.i18n.PluginNameConstants;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassModuleDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginDTO;
import com.ephesoft.gxt.core.shared.dto.DependencyDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;

public class ValidateWorkflowUtil {

	private static final String OR = "/";
	private static final String AND = ",";
	private static List<String> allPluginNames;

	static final Set<PluginDetailsDTO> pluginDetailsDTOs = new HashSet<PluginDetailsDTO>();
	static final Map<Integer, String> orderedPluginNames = new LinkedHashMap<Integer, String>(0);
	public ValidateWorkflowUtil() {
		super();
	}
	
	/**
	 * Checks if is clean up plugin exists.
	 *
	 * @param batchClassDTO the batch class dto
	 * @return true, if is clean up plugin exists
	 */
	public static boolean isCleanUpPluginExists(BatchClassDTO batchClassDTO){
		boolean isCleanUpExist = false;
		
		int index = 0;
		pluginDetailsDTOs.clear();
		orderedPluginNames.clear();
		// Get Plugin Names in order for above moduleWorkflowNames
		// and Distinct PDDTOs
		final List<BatchClassModuleDTO> batchClassModuleDTOs = new ArrayList<BatchClassModuleDTO>(batchClassDTO.getModules());
		sortModulesList(batchClassModuleDTOs);

		for (final BatchClassModuleDTO batchClassModuleDTO : batchClassModuleDTOs) {
			final List<BatchClassPluginDTO> batchClassPluginDTOs = new ArrayList<BatchClassPluginDTO>(
					batchClassModuleDTO.getBatchClassPlugins());
			Collections.sort(batchClassPluginDTOs, getPluginComparator());

			for (final BatchClassPluginDTO batchClassPluginDTO : batchClassPluginDTOs) {
				if (!batchClassPluginDTO.isDeleted()) {
					// Info.display("index "+index, ""+batchClassPluginDTO.getPlugin().getPluginName());
					orderedPluginNames.put(index++, batchClassPluginDTO.getPlugin().getPluginName());
					pluginDetailsDTOs.add(batchClassPluginDTO.getPlugin());
					
					if(PluginNameConstants.CLEANUP_PLUGIN.equals(batchClassPluginDTO.getPlugin().getPluginName())){
						isCleanUpExist=true;
					}
				}
			}
		}
		
		return isCleanUpExist;
	}

	/**
	 * Validate dependencies.
	 *
	 * @param batchClassDTO the batch class dto
	 * @return true, if successful
	 */
	public static boolean validateDependencies(BatchClassDTO batchClassDTO) {

		boolean allDependenciesValidated = true;
		if (CollectionUtil.isEmpty(pluginDetailsDTOs) || CollectionUtil.isEmpty(orderedPluginNames)) {

			// final Set<PluginDetailsDTO> pluginDetailsDTOs = new HashSet<PluginDetailsDTO>();
			// final Map<Integer, String> orderedPluginNames = new LinkedHashMap<Integer, String>(0);
			pluginDetailsDTOs.clear();
			orderedPluginNames.clear();
			int index = 0;
			//
			// Get Plugin Names in order for above moduleWorkflowNames
			// and Distinct PDDTOs
			final List<BatchClassModuleDTO> batchClassModuleDTOs = new ArrayList<BatchClassModuleDTO>(batchClassDTO.getModules());
			sortModulesList(batchClassModuleDTOs);

			for (final BatchClassModuleDTO batchClassModuleDTO : batchClassModuleDTOs) {
				final List<BatchClassPluginDTO> batchClassPluginDTOs = new ArrayList<BatchClassPluginDTO>(
						batchClassModuleDTO.getBatchClassPlugins());
				Collections.sort(batchClassPluginDTOs, getPluginComparator());

				for (final BatchClassPluginDTO batchClassPluginDTO : batchClassPluginDTOs) {
					if (!batchClassPluginDTO.isDeleted()) {
						orderedPluginNames.put(index++, batchClassPluginDTO.getPlugin().getPluginName());
						pluginDetailsDTOs.add(batchClassPluginDTO.getPlugin());
					}
				}
			}
		}
		// Validate Dependency for each PDDTO
		for (final PluginDetailsDTO pluginDetailsDTO : pluginDetailsDTOs) {
			if (pluginDetailsDTO.getDependencies() != null && !pluginDetailsDTO.getDependencies().isEmpty()) {
				for (final DependencyDTO dependencyDTO : pluginDetailsDTO.getDependencies()) {
					if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.ORDER_BEFORE.getProperty())) {
						allDependenciesValidated = checkPluginsDependencies(dependencyDTO.getPluginDTO().getPluginName(),
								dependencyDTO.getDependencies(), orderedPluginNames);

						if (!allDependenciesValidated) {
							break;
						}
					} else if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.UNIQUE.getProperty())) {
						allDependenciesValidated = checkPluginsIfUnique(dependencyDTO.getPluginDTO().getPluginName(),
								orderedPluginNames);

						if (!allDependenciesValidated) {
							break;
						}
					}

				}
				if (!allDependenciesValidated) {
					break;
				}
			}

		}

		return allDependenciesValidated;
	}

	public static void sortModulesList(final List<BatchClassModuleDTO> modulesList) {
		Collections.sort(modulesList, new Comparator<BatchClassModuleDTO>() {

			@Override
			public int compare(final BatchClassModuleDTO batchClassModuleDTO1, final BatchClassModuleDTO batchClassModuleDTO2) {
				int result;
				final Integer orderNumberOne = batchClassModuleDTO1.getOrderNumber();
				final Integer orderNumberTwo = batchClassModuleDTO2.getOrderNumber();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
	}

	public static Comparator<BatchClassPluginDTO> getPluginComparator() {
		return new Comparator<BatchClassPluginDTO>() {

			@Override
			public int compare(final BatchClassPluginDTO batchClassPluginDTO1, final BatchClassPluginDTO batchClassPluginDTO2) {
				int result;
				final Integer orderNumberOne = batchClassPluginDTO1.getOrderNumber();
				final Integer orderNumberTwo = batchClassPluginDTO2.getOrderNumber();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		};
	}

	/**
	 * Check plugins dependencies.
	 *
	 * @param pluginName the plugin name
	 * @param dependencies the dependencies
	 * @param orderedPluginNames the ordered plugin names
	 * @return true, if successful
	 */
	public static boolean checkPluginsDependencies(final String pluginName, final String dependencies,
			final Map<Integer, String> orderedPluginNames) {
		boolean pluginDependencyValidated = true;
		int pluginNameIndex = 0;

		// Get index of the plugin under Consideration
		pluginNameIndex = getIndexForValueFromMap(pluginName, orderedPluginNames);

		// Get index of all the Dependencies and check if they are less than
		// plugin's index
		final String[] andDependencies = dependencies.split(AND);
		for (final String andDependency : andDependencies) {
			boolean validDependency = false;
			final String[] orDependencies = andDependency.split(OR);
			boolean orDependenciesValidated = true;
			for (final String dependencyName : orDependencies) {

				if (allPluginNames.contains(dependencyName)) {
					validDependency = true;
					final int dependencyIndex = getIndexForValueFromMap(dependencyName, orderedPluginNames);
					if (dependencyIndex == -1 || dependencyIndex >= pluginNameIndex) {
						// Dependency occurs after plugin or is not present
						orDependenciesValidated = false;
					} else {
						orDependenciesValidated = true;
						break;
					}
				}
			}
			if (validDependency) {
				pluginDependencyValidated = pluginDependencyValidated && orDependenciesValidated;
			}
			if (!pluginDependencyValidated) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), StringUtil
						.concatenate(LocaleDictionary.getMessageValue(BatchClassMessages.DEPENDENCY_VIOLATED),
								CoreCommonConstants.NEW_LINE,
								andDependency.replace(OR, LocaleDictionary.getMessageValue(BatchClassMessages.OR_TEXT)),
								LocaleDictionary.getMessageValue(BatchClassMessages.PLUGIN_MUST_OCCUR_BEFORE), pluginName,
								CoreCommonConstants.DOT), DialogIcon.ERROR);
				pluginDependencyValidated = false;
				break;
			}
		}
		return pluginDependencyValidated;
	}

	/**
	 * Check plugins if unique.
	 *
	 * @param pluginName the plugin name
	 * @param orderedPluginNames the ordered plugin names
	 * @return true, if successful
	 */
	public static boolean checkPluginsIfUnique(final String pluginName, final Map<Integer, String> orderedPluginNames) {
		boolean isUnique = true;
		int count = 0;
		final Set<Entry<Integer, String>> pluginEntrySet = orderedPluginNames.entrySet();
		for (final Entry<Integer, String> pluginEntry : pluginEntrySet) {
			if (pluginEntry.getValue().equals(pluginName)) {
				count++;
			}
			if (count > 1) {
				isUnique = false;
				DialogUtil.showMessageDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.DEPENDENCY_VIOLATED) + pluginName
								+ LocaleDictionary.getMessageValue(BatchClassMessages.MUST_BE_UNIQUE_IN_THE_WORKFLOW),
						DialogIcon.ERROR);
				break;
			}
		}
		return isUnique;
	}

	/**
	 * Gets the index for value from map.
	 *
	 * @param pluginName the plugin name
	 * @param orderedPluginNames the ordered plugin names
	 * @return the index for value from map
	 */
	public static int getIndexForValueFromMap(final String pluginName, final Map<Integer, String> orderedPluginNames) {
		int pluginNameIndex = -1;
		final Set<Entry<Integer, String>> pluginEntrySet = orderedPluginNames.entrySet();
		for (final Entry<Integer, String> pluginEntry : pluginEntrySet) {
			if (pluginEntry.getValue().equals(pluginName)) {
				pluginNameIndex = Integer.parseInt(pluginEntry.getKey().toString());
				break;
			}
		}
		return pluginNameIndex;
	}

	/**
	 * Sort plugin details dto list.
	 *
	 * @param pluginsList the plugins list
	 * @param ascending the ascending
	 */
	public void sortPluginDetailsDTOList(final List<PluginDetailsDTO> pluginsList, final boolean ascending) {
		Collections.sort(pluginsList, new Comparator<PluginDetailsDTO>() {

			@Override
			public int compare(final PluginDetailsDTO PluginDTO1, final PluginDetailsDTO PluginDTO2) {
				int result;
				final String orderNumberOne = PluginDTO1.getPluginName();
				final String orderNumberTwo = PluginDTO2.getPluginName();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
	}

	/**
	 * Sets the all plugin names.
	 *
	 * @param pluginDetailsDTOs the new all plugin names
	 */
	public static void setAllPluginNames(List<PluginDetailsDTO> pluginDetailsDTOs) {
		if (CollectionUtil.isEmpty(allPluginNames)) {
			allPluginNames = new LinkedList<String>();
			for (PluginDetailsDTO pluginDetailsDTO : pluginDetailsDTOs) {
				allPluginNames.add(pluginDetailsDTO.getPluginName());
			}
		}
	}

}
