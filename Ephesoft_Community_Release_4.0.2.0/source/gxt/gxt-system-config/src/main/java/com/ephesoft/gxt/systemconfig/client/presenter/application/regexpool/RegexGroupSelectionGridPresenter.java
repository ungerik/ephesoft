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

package com.ephesoft.gxt.systemconfig.client.presenter.application.regexpool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.RandomIdGenerator;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.dto.RegexPatternDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.AddRegexGroupEvent;
import com.ephesoft.gxt.systemconfig.client.event.AddRegexGroupInTreeEvent;
import com.ephesoft.gxt.systemconfig.client.event.EditRegexGroupInTreeEvent;
import com.ephesoft.gxt.systemconfig.client.event.ImportRegexGroupEvent;
import com.ephesoft.gxt.systemconfig.client.event.RefreshRegexGroupGridEvent;
import com.ephesoft.gxt.systemconfig.client.event.RegexGroupSelectionEvent;
import com.ephesoft.gxt.systemconfig.client.event.RetreiveSelectedRegexGroupEvent;
import com.ephesoft.gxt.systemconfig.client.event.StartMultipleRegexGroupDeletionEvent;
import com.ephesoft.gxt.systemconfig.client.event.StartMultipleRegexGroupDeletionInTreeEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigInlinePresenter;
import com.ephesoft.gxt.systemconfig.client.view.application.regexpool.RegexGroupSelectionGridView;
import com.ephesoft.gxt.systemconfig.client.view.application.regexpool.RegexPatternSelectionGridView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

/**
 * This class is a presenter for view that shows a list of all regex groups for a regex type.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Dec-2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RegexGroupSelectionGridPresenter extends SystemConfigInlinePresenter<RegexGroupSelectionGridView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<RegexGroupSelectionGridPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private Collection<RegexGroupDTO> regexGroupList;

	/**
	 * Collection of regex group DTOs, set when a type of regex pool is configured.
	 */
	private Map<String, RegexGroupDTO> regexGroupDTOMap;

	/**
	 * Gets regexGroupDTOMap.
	 * 
	 * @return {@link Map<{@link String},{@link RegexGroupDTO}>}
	 */
	public Map<String, RegexGroupDTO> getRegexGroupDTOMap() {
		return regexGroupDTOMap;
	}

	/**
	 * Sets regexGroupDTOMap.
	 * 
	 * @param regexGroupDTOMap {@link Map<{@link String},{@link RegexGroupDTO}>}
	 */
	public void setRegexGroupDTOMap(final Map<String, RegexGroupDTO> regexGroupDTOMap) {
		this.regexGroupDTOMap = regexGroupDTOMap;
	}

	/**
	 * Constructor.
	 * 
	 * @param controller {@link SystemConfigController}
	 * @param view {@link RegexGroupSelectionView}
	 */
	public RegexGroupSelectionGridPresenter(final SystemConfigController controller, final RegexGroupSelectionGridView view) {
		super(controller, view);
		rpcService = controller.getRpcService();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.event.BindHandler#bind()
	 */
	@Override
	public void bind() {
		view.getGrid().getSelectAllCheckBox().setValue(false);
		view.getGrid().deSelectModels();
		getregexGroupsFromDb();
	}

	/**
	 * Gives view for selection of pattern for a selected group.
	 * 
	 * @param groupIdentifier {@link String}
	 */
	public void handleRegexGroupSelected(final String groupIdentifier) {
		if (null == groupIdentifier || groupIdentifier.isEmpty()) {
			DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_READ_REGEX_PATTERNS), false, DialogIcon.ERROR);
		} else {
			final RegexGroupDTO group = regexGroupDTOMap.get(groupIdentifier);
			if (group == null) {
				DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_READ_REGEX_PATTERNS), false, DialogIcon.ERROR);
			} else {
				final List<RegexPatternDTO> regexPatternDTOs = group.getRegexPatternDTOs(false);
				if (regexPatternDTOs == null) {
					DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_READ_REGEX_PATTERNS), false,
							DialogIcon.ERROR);
				} else if (regexPatternDTOs.isEmpty()) {
					DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.NO_REGEX_PATTERNS));

				} else {
					showPatternList(regexPatternDTOs, group);
				}
			}
		}
	}

	/**
	 * Show list view of patterns for selected group.
	 * 
	 * @param regexPatternDTOs {@link List<{@link RegexPatternDTO}>}
	 * @param group {@link RegexGroupDTO}
	 */
	private void showPatternList(final List<RegexPatternDTO> regexPatternDTOs, final RegexGroupDTO group) {
		final RegexPatternSelectionGridView regexPatternSelectionGridView = getController().getRegexPatternView()
				.getRegexPatternGrid();

		final Map<String, RegexPatternDTO> patternMap = new HashMap<String, RegexPatternDTO>(regexPatternDTOs.size());
		for (RegexPatternDTO patternDTO : regexPatternDTOs) {
			if (null != patternDTO) {
				patternMap.put(patternDTO.getIdentifier(), patternDTO);
			}
		}
		regexPatternSelectionGridView.setData(patternMap.values());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());

	}

	/**
	 * selectRegexGroup method used for selecting the regex group on cell selection change of regex group grid
	 * 
	 * @param cellSelectionChangeEvent { {@link CellSelectionChangedEvent<RegexGroupDTO>}
	 */
	public void selectRegexGroup(CellSelectionChangedEvent<RegexGroupDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			RegexGroupDTO selectedRegexGroup = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			if (selectedRegexGroup != null) {
				controller.setSelectedRegexGroupDTO(selectedRegexGroup);
			}
		}
	}

	/**
	 * getRegexGroups method use for getting the list of Regex group list from regex group map
	 * 
	 * @param regexGroupMap {@link Map<String, RegexGroupDTO>}
	 * @return list of regex groups {@link Collection<RegexGroupDTO>}
	 */
	public Collection<RegexGroupDTO> getRegexGroups(final Map<String, RegexGroupDTO> regexGroupMap) {
		Collection<RegexGroupDTO> regexGroupDTOList = null;
		if (null != regexGroupMap) {
			regexGroupDTOList = new ArrayList<RegexGroupDTO>(regexGroupMap.size());
			for (RegexGroupDTO regexGroupDTO : regexGroupMap.values()) {
				if (null != regexGroupDTO && !regexGroupDTO.isDeleted()) {
					regexGroupDTOList.add(regexGroupDTO);
				}
			}
		}
		return regexGroupDTOList;
	}

	/**
	 * updateRegexGroup method used for updating details of selected Regex Group
	 * 
	 * @param editedRegexGroup {@link RegexGroupDTO}
	 */
	public void updateRegexGroup(final RegexGroupDTO editedRegexGroup) {
		if (null != editedRegexGroup) {
			rpcService.updateRegexGroup(editedRegexGroup, new AsyncCallback<Integer>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(Integer regexId) {
					if (regexId >= 0) {
						editedRegexGroup.setIdentifier(String.valueOf(regexId));
						if (editedRegexGroup.isNew()) {
							editedRegexGroup.setNew(false);
							getregexGroupsFromDb();

						}

						controller.setSelectedRegexGroupDTO(editedRegexGroup);

					} else {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(SystemConfigMessages.REGEX_GROUP_UPDATE_FAILED), DialogIcon.ERROR);
					}
				}
			});
		}
	}

	/**
	 * Gets list of checked Groups, if none is selected returns the current selected Group.
	 * 
	 * @return {@link List}<{@link BatchInstanceDTO}>
	 */
	public List<RegexGroupDTO> getSelectedRegexGroups() {
		List<RegexGroupDTO> selectedRegexGroups = view.getSelectedRegexGroups();

		if (CollectionUtil.isEmpty(selectedRegexGroups)) {
			selectedRegexGroups = new ArrayList<RegexGroupDTO>();
			final RegexGroupDTO selectedRegexGroup = controller.getSelectedRegexGroupDTO();
			if (null != selectedRegexGroup) {
				selectedRegexGroups.add(selectedRegexGroup);
			}
		}

		return selectedRegexGroups;
	}

	/**
	 * deleteSelectedRegexGroups used for deleting the selected list of Regex groups
	 * 
	 * @param selectedRegexGroups {@link List<RegexGroupDTO>}
	 */
	private void deleteSelectedRegexGroups(List<RegexGroupDTO> selectedRegexGroups) {
		rpcService.deleteRegexGroups(selectedRegexGroups, new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if (result == true) {
					Message.display(LocaleDictionary.getMessageValue(SystemConfigMessages.DELETE_SELECTED_REGEX_GROUP),
							LocaleDictionary.getMessageValue(SystemConfigMessages.SELECTED_REGEX_GROUP_DELETED_SUCCESSFULLY));
				} else {
					Message.display(LocaleDictionary.getMessageValue(SystemConfigMessages.DELETE_SELECTED_REGEX_GROUP),
							LocaleDictionary.getMessageValue(SystemConfigMessages.SELECTED_REGEX_GROUP_DELETED_SUCCESSFULLY));
				}
				controller.setSelectedRegexGroupDTO(null);
				view.refreshGrid();
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

	/**
	 * event handler for deleting the new Regex Group
	 * 
	 * @param multipleDeletionStartEvent {@link StartMultipleRegexGroupDeletionEvent}
	 */
	@EventHandler
	public void handleMultipleDeletionStartEvent(StartMultipleRegexGroupDeletionEvent multipleDeletionStartEvent) {
		if (null != multipleDeletionStartEvent) {
			if (view.getGrid().getSelectedModels().size() > 0) {
				final List<RegexGroupDTO> selectedRegexGroups = getSelectedRegexGroups();
				if (CollectionUtil.isEmpty(selectedRegexGroups)) {
					DialogUtil
							.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
									LocaleDictionary.getMessageValue(SystemConfigMessages.PLEASE_SOME_REGEX_GROUP_TO_DELETE),
									DialogIcon.ERROR);
				} else {
					final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
							LocaleDictionary.getConstantValue(SystemConfigConstants.CONFIRMATION),
							LocaleDictionary.getMessageValue(SystemConfigMessages.DELETE_THE_SELECTED_REGEX_GROUP_MESSAGE), false,
							DialogIcon.QUESTION_MARK);
					confirmationDialog.setDialogListener(new DialogAdapter() {

						@Override
						public void onOkClick() {
							confirmationDialog.hide();
							controller.getEventBus().fireEvent(
									new StartMultipleRegexGroupDeletionInTreeEvent(view.getGrid().getSelectedModels()));
							deleteSelectedRegexGroups(selectedRegexGroups);
							view.getGrid().removeItemsFromGrid(selectedRegexGroups);
							view.getGrid().getSelectAllCheckBox().setValue(false);
							view.getGrid().deSelectModels();

						}

						@Override
						public void onCloseClick() {
							confirmationDialog.hide();

						}

						@Override
						public void onCancelClick() {
							confirmationDialog.hide();
						}
					});
				}
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
						LocaleDictionary.getMessageValue(SystemConfigMessages.PLEASE_SOME_REGEX_GROUP_TO_DELETE), DialogIcon.ERROR);
			}
		}
	}

	/**
	 * createRegexGroupDTOObject() method used for creating new Regex group instance
	 * 
	 * @return {@link RegexGroupDTO}
	 */
	public RegexGroupDTO createRegexGroupDTOObject() {
		RegexGroupDTO regexGroupDTO = new RegexGroupDTO();
		regexGroupDTO.setNew(true);
		regexGroupDTO.setName(SystemConfigConstants.EMPTY_STRING);
		regexGroupDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		regexGroupDTO.setRegexPatternDTOs(new LinkedHashSet<RegexPatternDTO>());
		regexGroupDTO.setSelected(false);
		return regexGroupDTO;
	}

	/**
	 * eventhandler for adding the new Regex Group
	 * 
	 * @param addEvent {@link AddRegexGroupEvent}
	 */
	@EventHandler
	public void handleRegexGroupAdditionEvent(AddRegexGroupEvent addEvent) {
		view.getGrid().getStore().commitChanges();
		if (null != addEvent) {
			if (view.isValid()) {
				RegexGroupDTO regexGroupDTO = createRegexGroupDTOObject();
				if (null != regexGroupDTO) {
					controller.getEventBus().fireEvent(new AddRegexGroupInTreeEvent(regexGroupDTO));
					controller.setSelectedRegexGroupDTO(regexGroupDTO);
					view.add(regexGroupDTO);
				}

			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
						LocaleDictionary.getMessageValue(SystemConfigMessages.VALIDATE_REGEX_GROUPS), DialogIcon.ERROR);
			}
		}
	}

	/**
	 * insertRegexGroup Method used for inserting the new Regex Group
	 * 
	 * @param regexGroup {@link RegexGroupDTO}
	 */
	public void insertRegexGroup(final RegexGroupDTO regexGroup) {
		if (null != regexGroup) {
			rpcService.insertRegexGroup(regexGroup, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
				}

				@Override
				public void onSuccess(Boolean result) {
					if (result) {
					} else {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(SystemConfigMessages.REGEX_GROUP_INSERTION_FAILED), DialogIcon.ERROR);
					}
				}

			});
		}
	}

	/**
	 * getregexGroupsFromDb is used to get the Regroup list from the Database
	 */
	public void getregexGroupsFromDb() {
		rpcService.getRegexGroupMap(new AsyncCallback<Map<String, RegexGroupDTO>>() {

			@Override
			public void onSuccess(final Map<String, RegexGroupDTO> regexGroupListMap) {
				if (regexGroupListMap == null) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_READ_REGEX_GROUPS), DialogIcon.ERROR);

				} else {
					if (regexGroupListMap.isEmpty()) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
								LocaleDictionary.getMessageValue(SystemConfigMessages.NO_REGEX_GROUPS), DialogIcon.ERROR);

					} else {
						regexGroupList = getRegexGroups(regexGroupListMap);
						final Map<String, RegexGroupDTO> regexGroupDTOMap = new HashMap<String, RegexGroupDTO>(regexGroupList.size());
						for (RegexGroupDTO groupDTO : regexGroupList) {
							if (null != groupDTO) {
								regexGroupDTOMap.put(groupDTO.getIdentifier(), groupDTO);
							}
						}

						setRegexGroupDTOMap(regexGroupDTOMap);
						view.setData(regexGroupList);
						controller.setSelectedRegexGroupDTO(null);

					}
				}
			}

			@Override
			public void onFailure(final Throwable caught) {
			}
		});
	}

	/**
	 * editRegexGroupEvent fires the edit regex group name event for tree
	 * 
	 * @param editedRegexGroup {@link RegexGroupDTO}, regex group to be edited
	 */
	public void editRegexGroupEvent(RegexGroupDTO editedRegexGroup) {
		controller.getEventBus().fireEvent(new EditRegexGroupInTreeEvent(editedRegexGroup));
	}

	public boolean isValid() {
		return view.isValid();
	}

	@EventHandler
	public void refershGridEvent(RefreshRegexGroupGridEvent refreshEvent) {
		if (null != refreshEvent) {
			getregexGroupsOnRefresh();

		}

	}

	/**
	 * getregexGroupsFromDb is used to get the Regroup list from the Database
	 */
	public void getregexGroupsOnRefresh() {
		rpcService.getRegexGroupMap(new AsyncCallback<Map<String, RegexGroupDTO>>() {

			@Override
			public void onSuccess(final Map<String, RegexGroupDTO> regexGroupListMap) {
				if (regexGroupListMap == null) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_READ_REGEX_GROUPS), DialogIcon.ERROR);

				} else {
					if (regexGroupListMap.isEmpty()) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
								LocaleDictionary.getMessageValue(SystemConfigMessages.NO_REGEX_GROUPS), DialogIcon.ERROR);

					} else {
						regexGroupList = getRegexGroups(regexGroupListMap);
						final Map<String, RegexGroupDTO> regexGroupDTOMap = new HashMap<String, RegexGroupDTO>(regexGroupList.size());
						for (RegexGroupDTO groupDTO : regexGroupList) {
							if (null != groupDTO) {
								regexGroupDTOMap.put(groupDTO.getIdentifier(), groupDTO);
							}
						}

						setRegexGroupDTOMap(regexGroupDTOMap);
						view.setData(regexGroupList);
						controller.setSelectedRegexGroupDTO(null);
						view.getGrid().getSelectAllCheckBox().setValue(false);
						view.getGrid().deSelectModels();
						controller.getEventBus().fireEvent(new ImportRegexGroupEvent((List<RegexGroupDTO>) regexGroupList));

					}
				}
			}

			@Override
			public void onFailure(final Throwable caught) {
			}
		});
	}

	@EventHandler
	public void getSelectedRegexGroup(RetreiveSelectedRegexGroupEvent event) {
		if (null != event && view.isValid()) {
			List<RegexGroupDTO> selectedDTOs = view.getSelectedRegexGroups();
			if (CollectionUtil.isEmpty(selectedDTOs)) {
				if (null != controller.getSelectedRegexGroupDTO()) {
					selectedDTOs = Collections.singletonList(controller.getSelectedRegexGroupDTO());
				}
			}
			if (!CollectionUtil.isEmpty(selectedDTOs)) {
				if (selectedDTOs.size() > 1) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.SELECT_ONE_REGEX_GROUP_TO_OPEN), DialogIcon.ERROR);
				} else if (selectedDTOs.size() == 1) {
					controller.getEventBus().fireEvent(new RegexGroupSelectionEvent(selectedDTOs.get(0)));
				}
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
						LocaleDictionary.getMessageValue(SystemConfigMessages.PLEASE_SOME_REGEX_GROUP_TO_OPEN), DialogIcon.ERROR);
			}
		}
	}

	public void checkNewGroupExist(List<RegexGroupDTO> regexGroupList) {

		for (RegexGroupDTO group : regexGroupList) {
			if (group.isNew()) {
				updateRegexGroup(group);
				controller.setSelectedRegexGroupDTO(group);
				view.getGrid().refreshGrid(group, false);
			}
		}
	}

}
