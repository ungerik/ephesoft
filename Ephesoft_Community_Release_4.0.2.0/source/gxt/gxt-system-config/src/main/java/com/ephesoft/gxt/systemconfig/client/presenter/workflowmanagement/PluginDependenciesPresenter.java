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

package com.ephesoft.gxt.systemconfig.client.presenter.workflowmanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.DependencyDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.HidePluginDependenciesViewEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigInlinePresenter;
import com.ephesoft.gxt.systemconfig.client.view.workflowmanagement.PluginDependenciesView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;

public class PluginDependenciesPresenter extends SystemConfigInlinePresenter<PluginDependenciesView> {

	interface CustomEventBinder extends EventBinder<PluginDependenciesPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	List<String> dependenciesListBuffer = new ArrayList<String>();

	public DependencyDTO currentDisplayedDependency;

	public PluginDetailsDTO andDependencyRelation;

	public PluginDetailsDTO orDependencyRelation;

	public boolean isCyclic = false;

	public boolean isValidDependency = true;

	boolean isDuplicate = false;

	boolean isValidEntry = true;

	public String selectedDependencyName;

	public String conflictingDependencyName;

	private Map<Integer, String> dependencyTypeMap;

	private List<PluginDetailsDTO> availablePluginsForSelection;

	public PluginDependenciesPresenter(SystemConfigController controller, PluginDependenciesView view) {
		super(controller, view);
		// dependencyTypeList = new ArrayList<String>();
		andDependencyRelation = new PluginDetailsDTO();
		andDependencyRelation.setPluginName(SystemConfigConstants.AND_DEPENDENCY_NAME);
		orDependencyRelation = new PluginDetailsDTO();
		orDependencyRelation.setPluginName(SystemConfigConstants.OR_DEPENDENCY_NAME);
		initializePluginDependencyMap();
		loadDependencyTypeList();

	}

	private void initializePluginDependencyMap() {
		int index = 0;
		dependencyTypeMap = new HashMap<Integer, String>();
		for (DependencyTypeProperty dependencyType : DependencyTypeProperty.values()) {
			dependencyTypeMap.put(index, dependencyType.getDependencyType());
			index++;
		}

	}

	@Override
	public void bind() {
		setSelectedDependencyType();
		setSelectedDependencies();
	}

	private void setSelectedDependencies() {
		if (null == currentDisplayedDependency) {
			view.clearSelectedDependencies();
			view.enableDualDependencySelector();
		} else {
			final String selectedDependencyType = currentDisplayedDependency.getDependencyType();
			if (!StringUtil.isNullOrEmpty(selectedDependencyType)) {
				boolean uniqueDependency = checkAndDisableDualDependencySelector(selectedDependencyType);
				if (!uniqueDependency) {
					String selectedDependencies = currentDisplayedDependency.getDependencies();
					if (StringUtil.isNullOrEmpty(selectedDependencies)) {
						view.clearSelectedDependencies();
					} else {
						List<PluginDetailsDTO> selectedDependenciesInDTO = convertSelectedDependencyString(selectedDependencies);
						if (!CollectionUtil.isEmpty(selectedDependenciesInDTO)) {
							view.clearSelectedDependencies();
							view.setSelectedDependencies(selectedDependenciesInDTO);
						}
					}
				}
			}
		}
	}

	public boolean checkAndDisableDualDependencySelector(final String selectedItem) {
		boolean isUniqueDependency = false;
		if (DependencyTypeProperty.UNIQUE.getDependencyType().equalsIgnoreCase(selectedItem)) {
			view.clearSelectedDependencies();
			view.disableDualDependencySelector();
			isUniqueDependency = true;
		} else if (DependencyTypeProperty.ORDER_BEFORE.getDependencyType().equalsIgnoreCase(selectedItem)) {
			view.enableDualDependencySelector();
			isUniqueDependency = false;
		}
		return isUniqueDependency;
	}

	private List<PluginDetailsDTO> convertSelectedDependencyString(String selectedDependencies) {
		List<PluginDetailsDTO> selectedDependencyList = null;
		selectedDependencies = selectedDependencies.trim();
		if (!StringUtil.isNullOrEmpty(selectedDependencies)) {
			selectedDependencyList = new ArrayList<PluginDetailsDTO>();
			List<String> andDependencies = splitString(selectedDependencies, SystemConfigConstants.AND_DEPENDENCY_SYMBOL);
			for (String andDependency : andDependencies) {
				List<String> orDependencies = splitString(andDependency, SystemConfigConstants.OR_DEPENDENCY_SYMBOL);
				for (String orDependency : orDependencies) {
					if (orDependency.equals(SystemConfigConstants.AND_DEPENDENCY_SYMBOL)) {
						selectedDependencyList.add(andDependencyRelation);
					} else if (orDependency.equals(SystemConfigConstants.OR_DEPENDENCY_SYMBOL)) {
						selectedDependencyList.add(orDependencyRelation);
					} else {
						PluginDetailsDTO dependency = controller.getSelectedPluginByName(orDependency);
						if (null != dependency) {
							PluginDetailsDTO newPluginDTO = new PluginDetailsDTO();
							newPluginDTO.setPluginName(dependency.getPluginName());
							selectedDependencyList.add(newPluginDTO);
						}
					}
				}
			}
		}
		return selectedDependencyList;
	}

	private List<String> splitString(final String stringToSplit, final String character) {
		List<String> splitedStringList = new ArrayList<String>();
		if (!stringToSplit.isEmpty() && !character.isEmpty()) {
			int index = stringToSplit.indexOf(character);
			if (index == -1) {
				splitedStringList.add(stringToSplit);
			} else if (index == 0) {
				splitedStringList.add(character);
				String remainingString = stringToSplit.substring(1);
				splitedStringList.addAll(splitString(remainingString, character));
			} else {
				String splitedString = stringToSplit.substring(0, index);
				splitedStringList.add(splitedString);
				splitedStringList.add(character);
				String remainingString = stringToSplit.substring(index + 1);
				splitedStringList.addAll(splitString(remainingString, character));
			}
		}
		return splitedStringList;
	}

	private void setSelectedDependencyType() {
		int selectIndex = 0;
		if (null != currentDisplayedDependency) {
			String selectedDependencyType = currentDisplayedDependency.getDependencyType();
			if (!StringUtil.isNullOrEmpty(selectedDependencyType)) {
				for (int dependencyIndex : dependencyTypeMap.keySet()) {
					final String dependencyType = dependencyTypeMap.get(dependencyIndex);
					if (selectedDependencyType.equalsIgnoreCase(dependencyType)) {
						selectIndex = dependencyIndex;
						break;
					}
				}
			}
		}
		view.setSelectedDependencyType(selectIndex);
	}

	private void loadDependencyTypeList() {
		if (null != dependencyTypeMap) {
			for (int index : dependencyTypeMap.keySet()) {
				final String dependencyType = dependencyTypeMap.get(index);
				view.insertValuesInDependencyListBox(dependencyType, index);
			}
		}

	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void loadDependencies(final PluginDetailsDTO selectedPlugin) {
		if (null != selectedPlugin) {
			String pluginName = selectedPlugin.getPluginName();
			String pluginDescription = selectedPlugin.getPluginDescription();
			view.setPluginName(pluginName);
			view.setPluginDescription(pluginDescription);
			List<PluginDetailsDTO> allPlugins = controller.getAllPluginsList();
			List<PluginDetailsDTO> allPluginsExceptSelected = new ArrayList<PluginDetailsDTO>(allPlugins.size() - 1);
			allPluginsExceptSelected.addAll(allPlugins);
			
			// Removing selected plugin from list.
			for (PluginDetailsDTO pluginDetail : allPluginsExceptSelected){
				String selectedPluginID = selectedPlugin.getIdentifier();
				String pluginID = pluginDetail.getIdentifier();
				if (!StringUtil.isNullOrEmpty(selectedPluginID) && !StringUtil.isNullOrEmpty(pluginID)){
					if (selectedPluginID.equals(pluginID)){
						allPluginsExceptSelected.remove(pluginDetail);
					}
				}
			}
			availablePluginsForSelection = allPluginsExceptSelected;
			view.setValuesInDualList(availablePluginsForSelection);
		}
	}
	
	public void onResetClicked() {
		setSelectedDependencyType();
		setSelectedDependencies();
	}

	public void onCancelClicked() {
		// TODO Auto-generated method stub
		hidePluginDependency();
	}

	public void hidePluginDependency() {
		controller.getEventBus().fireEvent(new HidePluginDependenciesViewEvent());
	}

	public void onSaveClicked() {
		// TODO Auto-generated method stub
		updatePluginDependency();
	}

	public DependencyDTO getCurrentDisplayedDependency() {
		return currentDisplayedDependency;
	}

	public void setCurrentDisplayedDependency(DependencyDTO currentDisplayedDependency) {
		this.currentDisplayedDependency = currentDisplayedDependency;
	}

	public void updatePluginDependency() {
		reinitializeValidationParameters();
		boolean dependenciesValidated = validateCurrentDependencies();
		if (dependenciesValidated) {
//			Message.display("Dependencies Validated ", "TRUE");
			ScreenMaskUtility.maskScreen(LocaleDictionary.getMessageValue(SystemConfigMessages.UPDATING_DEPENDENCIES_LIST));
			// PluginDetailsDTO pluginDetailsDTO = getPluginForName(controller.getAllPlugins(), getEditDependencyPresenter().getView()
			// .getPluginNamesList().getText());
			PluginDetailsDTO pluginDetailsDTO = controller.getWorkflowManagmentPresenter().getSelectedPluginDTO();
			pluginDetailsDTO.setDirty(true);
			DependencyDTO dependencyDTO = createDependencyDTO();
			if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.UNIQUE.getProperty())) {
				dependencyDTO.setDependencies(SystemConfigConstants.EMPTY_STRING);
			}
			if (pluginDetailsDTO.getDependencies() == null) {
				pluginDetailsDTO.setDependencies(new ArrayList<DependencyDTO>(0));
				pluginDetailsDTO.getDependencies().clear();
			}
			if (controller.getWorkflowManagmentPresenter().getPluginDependenciesPresenter().getCurrentDisplayedDependency() == null) {
				// dependencyDTO.setIdentifier(SystemConfigConstants.NEW + newIdentifier++);
				dependencyDTO.setPluginDTO(pluginDetailsDTO);
				dependencyDTO.setNew(true);
				controller.getWorkflowManagmentPresenter().setSelectedDependencyDTO(dependencyDTO);
				pluginDetailsDTO.setDirty(true);
				pluginDetailsDTO.getDependencies().add(dependencyDTO);
			} else {
				for (DependencyDTO dependency : pluginDetailsDTO.getDependencies()) {
					if (dependency.getIdentifier().equals(
							controller.getWorkflowManagmentPresenter().getSelectedDependencyDTO().getIdentifier())) {
						dependency.setDependencyType(dependencyDTO.getDependencyType());
						dependency.setDependencies(dependencyDTO.getDependencies());
						dependency.setDirty(true);
						dependencyDTO = dependency;
						break;
					}
				}
			}
			// Message.display(/*
			// * LocaleDictionary.getConstantValue(SystemConfigConstants.SUCCESS_TITLE), LocaleDictionary
			// * .getLocaleDictionary().getMessageValue(SystemConfigMessages.DEPENDENCIES_LIST_UPDATED_SUCCESSFULLY)
			// */"Success", "Dependencies Updated.");
			// ScreenMaskUtility.unmaskScreen();
			// showDependenciesView(controller.getAllPlugins());
			// populatePluginName(dependencyDTO.getPluginDTO().getPluginName());
			ScreenMaskUtility.unmaskScreen();
			applyChangesToDB();
		}
	}

	private void applyChangesToDB() {
		ScreenMaskUtility.maskScreen(LocaleDictionary.getMessageValue(SystemConfigMessages.UPDATING_PLUGINS_LIST));
		final String identifier = controller.getWorkflowManagmentPresenter().getSelectedPluginDTO().getIdentifier();
		controller.getRpcService().updateAllPluginDetailsDTOs(controller.getAllPluginsList(),
				new AsyncCallback<List<PluginDetailsDTO>>() {

					@Override
					public void onFailure(Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(SystemConfigMessages.ERROR_UPDATING_PLUGINS), DialogIcon.ERROR);
					}

					@Override
					public void onSuccess(List<PluginDetailsDTO> result) {
						ScreenMaskUtility.unmaskScreen();
						controller.setAllPluginsList(result);
						PluginDetailsDTO selectedDTO = controller.getPluginDTOForID(identifier);
						if (null != selectedDTO) {
							controller.getWorkflowManagmentPresenter().init(selectedDTO);
							controller.getWorkflowManagmentPresenter().bind();
						}
						hidePluginDependency();
						Message.display(LocaleDictionary.getConstantValue(SystemConfigConstants.SUCCESS_TITLE), LocaleDictionary.getMessageValue(SystemConfigMessages.DEPENDENCIES_LIST_UPDATED_SUCCESSFULLY));
					}

				});

	}

	/**
	 * The <code>reinitializeValidationParameters</code> is a method that re initialize validation parameters used for validating
	 * dependency update.
	 */
	private void reinitializeValidationParameters() {
		isCyclic = false;
		isValidDependency = true;
		isDuplicate = false;
		isValidEntry = true;
	}

	/**
	 * The validateCurrentDependencies method validates entered dependencies for duplication, cycle etc.
	 * 
	 * @return true/false.
	 */
	public boolean validateCurrentDependencies() {
		boolean dependenciesValidated = false;

		boolean isCyclic = false;

		// boolean isValidDependency = true;

		boolean isDuplicate = false;

		boolean isValidEntry = true;

		String newDependencies = getSelectedDependenciesAsString();
		selectedDependencyName = controller.getWorkflowManagmentPresenter().getSelectedPluginDTO().getPluginName();

		dependenciesListBuffer.clear();
		dependenciesListBuffer.add(selectedDependencyName);

		PluginDetailsDTO pluginDetailsDTO = controller.getWorkflowManagmentPresenter().getSelectedPluginDTO();

		List<DependencyDTO> dependenciesList = new ArrayList<DependencyDTO>(pluginDetailsDTO.getDependencies());
		DependencyDTO selectedDependencyDTO = getCurrentDisplayedDependency();
		if (selectedDependencyDTO != null) {
			dependenciesList.remove(selectedDependencyDTO);
		}
		if (view.isDualDependencySelectorEnabled() && !newDependencies.isEmpty()) {
			isCyclic = false;

			isValidDependency = true;
			isDuplicate = checkForDuplicateDependenciesEntry(dependenciesList, newDependencies);
			if (!isDuplicate) {
				isValidEntry = checkInvalidDependencyEntry(newDependencies);
				// Message.display("checkInvalidDependencyEntry", "" + isValidEntry);
				if (isValidEntry) {
					isCyclic = checkCyclicDependencies(newDependencies.trim(), selectedDependencyName);
					// Message.display("checkCyclicDependencies", "" + isCyclic);
				}
			}
		} else if (!view.isDualDependencySelectorEnabled()) {
			isCyclic = false;
			isValidDependency = true;
			isValidEntry = true;

			isDuplicate = checkForDuplicateUniqueDependencyEntry(dependenciesList, selectedDependencyName);
			// Message.display("checkForDuplicateUniqueDependencyEntry", "" + isDuplicate);
			if (isDuplicate) {
				String dependencyNameView = addStringAround(selectedDependencyName, SystemConfigConstants.OR_DEPENDENCY_SYMBOL,
						SystemConfigConstants.SPACE);
				// DialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
				// CustomWorkflowMessages.UNIQUE_DEPENDENCY_FOR)
				// + dependencyNameView
				// + LocaleDictionary.get().getMessageValue(CustomWorkflowMessages.ALREADY_EXISTS_CORRECT_AND_TRY_AGAIN));
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE), StringUtil
						.concatenate(LocaleDictionary.getMessageValue(SystemConfigMessages.UNIQUE_DEPENDENCY_FOR), dependencyNameView,
								LocaleDictionary.getMessageValue(SystemConfigMessages.ALREADY_EXISTS_CORRECT_AND_TRY_AGAIN)),
						DialogIcon.ERROR);
			}
		} else {
			isValidDependency = false;
			// DialogUtil.showConfirmationDialog(LocaleDictionary.get().getMessageValue(
			// CustomWorkflowMessages.NO_DEPENDENCY_SELECTED));
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.NO_DEPENDENCY_SELECTED), DialogIcon.ERROR);
		}
		dependenciesValidated = !isCyclic && isValidDependency && !isDuplicate && isValidEntry;
		return dependenciesValidated;
	}

	private boolean checkForDuplicateUniqueDependencyEntry(List<DependencyDTO> dependenciesList, String dependency) {

		boolean isDuplicate = false;
		for (DependencyDTO dependencyDTO : dependenciesList) {

			if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.UNIQUE.getProperty())) {
				isDuplicate = true;
				break;
			}
		}
		return isDuplicate;
	}

	private boolean checkCyclicDependencies(String newDependencies, String selectedDependency) {
		Set<String> uniqueDependency = new HashSet<String>(0);

		String trimmedNewDependencies = newDependencies.trim();
		String[] andDependencies = trimmedNewDependencies.split(SystemConfigConstants.AND_DEPENDENCY_SYMBOL);

		for (String andDependency : andDependencies) {

			String[] orDependencies = andDependency.split(SystemConfigConstants.OR_DEPENDENCY_SYMBOL);

			for (String dependencyName : orDependencies) {
				boolean validDependencyname = false;
				// Collection<String> validDependenciesNamesList = controller.getPluginIndexToNameMap().values();
				// validDependencyname = validDependenciesNamesList.contains(dependencyName);
				validDependencyname = checkAvailablePluginForDependency(dependencyName);
//				Message.display("validDependencyname is ", " " + validDependencyname);
				if (validDependencyname) {
					uniqueDependency.add(dependencyName);
					if (uniqueDependency.contains(selectedDependencyName)) {
//						Message.display("if uniqueDependency.contains(selectedDependencyName)", "TRUE   " + selectedDependencyName);
						// cyclic
						if (isCyclic == true) {
							break;
						}
						DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
								StringUtil.concatenate(LocaleDictionary.getMessageValue(SystemConfigMessages.DEPENDENCY_VIOLATED),
										selectedDependencyName,
										LocaleDictionary.getMessageValue(SystemConfigMessages.PLUGIN_ALREADY_DEPENDENT),
										dependenciesListBuffer.get(1)), false, DialogIcon.ERROR);
						isCyclic = true;
						break;
					} else {
						String nextDependency = getOrderBeforeDependenciesForPluginName(dependencyName);
						conflictingDependencyName = dependencyName;
						dependenciesListBuffer.add(conflictingDependencyName);
						if (!nextDependency.isEmpty()) {
							checkCyclicDependencies(nextDependency, dependencyName);
						} else {
							dependenciesListBuffer.remove(dependenciesListBuffer.size() - 1);
							continue;
						}
					}
				} else if (!isCyclic) {
					isValidDependency = false;
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE), StringUtil
							.concatenate(LocaleDictionary.getMessageValue(SystemConfigMessages.DEPENDENCY_VIOLATED),
									LocaleDictionary.getMessageValue(SystemConfigMessages.INVALID_DEPENDENCY_NAME), dependencyName),
							DialogIcon.ERROR);
					break;
				}
			}
			if (isCyclic == true || !isValidDependency) {
				break;
			}

		}
		if (!(isCyclic || !isValidDependency) && (dependenciesListBuffer.contains(selectedDependency))) {
			dependenciesListBuffer.remove(selectedDependency);
		}
		return isCyclic || !isValidDependency;
	}

	private boolean checkAvailablePluginForDependency(String dependencyName) {
		boolean validDependency = false;
		if (!CollectionUtil.isEmpty(availablePluginsForSelection)) {
			for (PluginDetailsDTO plugin : availablePluginsForSelection) {
				if (plugin.getPluginName().equalsIgnoreCase(dependencyName)) {
					validDependency = true;
					break;
				}
			}
		}
		return validDependency;
	}

	private String getOrderBeforeDependenciesForPluginName(String pluginName) {

		StringBuffer dependenciesBuffer = new StringBuffer();
		List<DependencyDTO> dependencyDTOs = controller.getSelectedPluginByName(pluginName).getDependencies();
		if (dependencyDTOs != null) {
			for (DependencyDTO dependencyDTO : dependencyDTOs) {
				if (dependencyDTO != null
						&& dependencyDTO.getDependencyType().equals(DependencyTypeProperty.ORDER_BEFORE.getProperty())) {
					String dependencies = SystemConfigConstants.EMPTY_STRING;
					dependencies = (dependencyDTO.getDependencies());
					if (!dependenciesBuffer.toString().isEmpty()) {
						dependenciesBuffer.append(SystemConfigConstants.AND_DEPENDENCY_SYMBOL);
					}
					dependenciesBuffer.append(dependencies);

				}
			}
		}
		return dependenciesBuffer.toString();
	}

	private boolean checkInvalidDependencyEntry(String newDependenciesString) {
		isValidEntry = true;
		List<String> uniqueAndDependency = getAndDependenciesAsList(newDependenciesString);

		Set<String> uniqueDependencySet = new HashSet<String>();

		for (String dependency : uniqueAndDependency) {

			if (!uniqueDependencySet.add(dependency)) {
				// dependency is duplicate
				isValidEntry = false;
				String dependencyViewName = addStringAround(dependency, SystemConfigConstants.OR_DEPENDENCY_SYMBOL,
						SystemConfigConstants.SPACE);
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE), StringUtil
						.concatenate(LocaleDictionary.getMessageValue(SystemConfigMessages.DEPENDENCIES_CONTAINS_DUPLICATE_ENTRY)
								+ dependencyViewName + LocaleDictionary.getMessageValue(SystemConfigMessages.TRY_AGAIN)),
						DialogIcon.ERROR);
				break;
			}

		}

		return isValidEntry;
	}

	private boolean checkForDuplicateDependenciesEntry(List<DependencyDTO> dependencyDTOs, String newDependenciesString) {

		isDuplicate = false;
		// Get list of and dependencies
		List<String> uniqueAndDependency = getAndDependenciesAsList(newDependenciesString);

		for (DependencyDTO dependencyDTO : dependencyDTOs) {
			if (!dependencyDTO.isDeleted()) {
				if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.ORDER_BEFORE.getProperty())) {
					String existingDependencies = dependencyDTO.getDependencies();

					List<String> existingUniqueAndDependency = getAndDependenciesAsList(existingDependencies);
					for (String andDependency : uniqueAndDependency) {
						// if (existingUniqueAndDependency.contains(andDependency.replaceAll(CustomWorkflowConstants.NEXT_LINE,
						// CustomWorkflowConstants.EMPTY_STRING).replaceAll(CustomWorkflowConstants.CARRIAGE_RETURN,
						// CustomWorkflowConstants.EMPTY_STRING))) {
						if (existingUniqueAndDependency.contains(andDependency)) {

							isDuplicate = true;
							String dependencyViewName = addStringAround(andDependency, SystemConfigConstants.OR_DEPENDENCY_SYMBOL,
									SystemConfigConstants.SPACE);
							DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
											StringUtil.concatenate(dependencyViewName, LocaleDictionary
													.getMessageValue(SystemConfigMessages.DEPENDENCY_ALREADY_EXISTS_TRY_AGAIN)), false,
											DialogIcon.ERROR);
							break;
						}
					}
					if (isDuplicate) {
						break;
					}
				}
			}
		}
		return isDuplicate;
	}

	private String addStringAround(String originalString, String markedCharacter, String surrondingCharacter) {
		StringBuffer replacementStringBuffer = new StringBuffer();
		replacementStringBuffer.append(surrondingCharacter);
		replacementStringBuffer.append(markedCharacter);
		replacementStringBuffer.append(surrondingCharacter);

		return originalString.replaceAll(markedCharacter, replacementStringBuffer.toString());
	}

	/**
	 * @param newDependenciesString
	 * @param uniqueAndDependency
	 */
	private List<String> getAndDependenciesAsList(String newDependenciesString) {
		List<String> uniqueAndDependency = new ArrayList<String>(0);
		String[] andDependencies = newDependenciesString.split(SystemConfigConstants.AND_DEPENDENCY_SYMBOL);

		for (String andDependency : andDependencies) {
			String sortedOrDependencies = sortOrDependencies(andDependency);
			uniqueAndDependency.add(sortedOrDependencies.trim());
		}
		return uniqueAndDependency;
	}

	private String sortOrDependencies(String orDependenciesString) {
		List<String> sortedDependenciesString = new ArrayList<String>();
		String sortedOrDependenciesString = null;

		String[] orDependencies = orDependenciesString.split(SystemConfigConstants.OR_DEPENDENCY_SYMBOL);

		for (String dependency : orDependencies) {
			sortedDependenciesString.add(dependency.trim());
		}
		Collections.sort(sortedDependenciesString);
		StringBuffer sortedDependenciesStringBuffer = new StringBuffer();
		// Prepare a new string for the sorted dependencies
		for (String dependency : sortedDependenciesString) {
			if (!sortedDependenciesStringBuffer.toString().isEmpty()) {
				sortedDependenciesStringBuffer.append(SystemConfigConstants.OR_DEPENDENCY_SYMBOL);
			}
			sortedDependenciesStringBuffer.append(dependency);
		}
		sortedOrDependenciesString = sortedDependenciesStringBuffer.toString();
		return sortedOrDependenciesString;
	}

	private DependencyDTO createDependencyDTO() {
		DependencyDTO dependencyDTO = new DependencyDTO();

		int selectedIndex = view.getSelectedDependencyType();
		String dependencyType = dependencyTypeMap.get(selectedIndex);
		dependencyDTO.setDependencyType(dependencyType);

		String dependencies = getSelectedDependenciesAsString();
		// dependencies = dependencies.replaceAll(CustomWorkflowConstants.NEXT_LINE, CustomWorkflowConstants.EMPTY_STRING).replaceAll(
		// CustomWorkflowConstants.CARRIAGE_RETURN, CustomWorkflowConstants.EMPTY_STRING);
		dependencyDTO.setDependencies(dependencies);

		return dependencyDTO;
	}

	private String getSelectedDependenciesAsString() {
		String finalDependencyString = SystemConfigConstants.EMPTY_STRING;
		List<PluginDetailsDTO> selectedDependencies = view.getSelectedDependencies();
		if (!CollectionUtil.isEmpty(selectedDependencies)) {
			for (PluginDetailsDTO dto : selectedDependencies) {
				String dependencyName = dto.getPluginName();
				if (dependencyName.equalsIgnoreCase(SystemConfigConstants.AND_DEPENDENCY_NAME)) {
					finalDependencyString = StringUtil.concatenate(finalDependencyString, SystemConfigConstants.AND_DEPENDENCY_SYMBOL);
				} else if (dependencyName.equalsIgnoreCase(SystemConfigConstants.OR_DEPENDENCY_NAME)) {
					finalDependencyString = StringUtil.concatenate(finalDependencyString, SystemConfigConstants.OR_DEPENDENCY_SYMBOL);
				} else {
					finalDependencyString = StringUtil.concatenate(finalDependencyString, dependencyName);
				}
			}
		}
		return finalDependencyString;
	}

}
