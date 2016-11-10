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

package com.ephesoft.gxt.admin.client.presenter.regexPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AddRegexPatternEvent;
import com.ephesoft.gxt.admin.client.event.RegexGroupSelectionEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.regexPool.RegexPatternSelectionGridView;
import com.ephesoft.gxt.core.client.RandomIdGenerator;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.dto.RegexPatternDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

/**
 * This class is a presenter for view that shows a list of all regex patterns for a regex group.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Dec-2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RegexPatternSelectionGridPresenter extends BatchClassInlinePresenter<RegexPatternSelectionGridView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<RegexPatternSelectionGridPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Constructor.
	 * 
	 * @param controller {@link SystemConfigController}
	 * @param view {@link RegexPatternSelectionView}
	 */
	public RegexPatternSelectionGridPresenter(final BatchClassManagementController controller, final RegexPatternSelectionGridView view) {
		super(controller, view);
		rpcService = controller.getRpcService();
	}

	// private RegexComboBox regexComboBox;
	//
	// private boolean gridOrForm;
	//
	// public RegexComboBox getRegexComboBox() {
	// return regexComboBox;
	// }
	//
	// public void setRegexComboBox(RegexComboBox regexComboBox) {
	// this.regexComboBox = regexComboBox;
	// }
	//
	// public boolean isGridOrForm() {
	// return gridOrForm;
	// }
	//
	// public void setGridOrForm(boolean gridOrForm) {
	// this.gridOrForm = gridOrForm;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.event.BindHandler#bind()
	 */
	@Override
	public void bind() {
		RegexGroupDTO selectedRegexGroup = controller.getSelectedRegexGroupDTO();
		if (selectedRegexGroup != null) {
			handleRegexGroupSelected(selectedRegexGroup);
		} else {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SOME_REGEX_GROUP_TO_OPEN), DialogIcon.ERROR);
		}
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
	 * updateRegexPattern method used for updating details of selected Regex pattern
	 * 
	 * @param editedRegexPattern {@link RegexPatternDTO}
	 */
	// public void updateRegexPattern(final RegexPatternDTO editedRegexPattern) {
	// if (null != editedRegexPattern) {
	// rpcService.updateRegexPattern(editedRegexPattern, controller.getSelectedRegexGroupDTO(), new AsyncCallback<Integer>() {
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// }
	//
	// @Override
	// public void onSuccess(Integer result) {
	// if (result >= 0) {
	// editedRegexPattern.setIdentifier(String.valueOf(result));
	// editedRegexPattern.setNew(false);
	// RegexGroupDTO selectedGroup = controller.getSelectedRegexGroupDTO();
	// final List<RegexPatternDTO> regexPatternDTOs = selectedGroup.getRegexPatternDTOs(false);
	// boolean patternExist = false;
	// for (RegexPatternDTO regexPattern : regexPatternDTOs) {
	// if (regexPattern.getIdentifier() == editedRegexPattern.getIdentifier()) {
	// patternExist = true;
	// break;
	// }
	// }
	// if (!patternExist) {
	// selectedGroup.addRegexPatternDTO(editedRegexPattern);
	// }
	//
	// } else {
	// DialogUtil.showMessageDialog(BatchClassMessages.UPDATION_FAILED,
	// BatchClassMessages.REGEX_PATTERN_UPDATE_FAILED, DialogIcon.ERROR);
	// }
	// }
	// });
	// }
	// }

	/**
	 * selectRegexPattern method used for selecting the regex Pattern on cell selection change of regex Pattern grid
	 * 
	 * @param cellSelectionChangeEvent { {@link CellSelectionChangedEvent<RegexPatternDTO>}
	 */
	public void selectRegexPattern(CellSelectionChangedEvent<RegexPatternDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			RegexPatternDTO selectedRegexPattern = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			if (selectedRegexPattern != null) {
				controller.setSelectedRegexPatternDTO(selectedRegexPattern);
			}
		}
	}

	/**
	 * Gets list of checked Batch Instances, if none is selected returns the current selected Batch Instance.
	 * 
	 * @return {@link List}<{@link BatchInstanceDTO}>
	 */
	public List<RegexPatternDTO> getSelectedRegexPatterns() {
		List<RegexPatternDTO> selectedRegexPatterns = view.getSelectedRegexPatterns();

		if (CollectionUtil.isEmpty(selectedRegexPatterns)) {
			selectedRegexPatterns = new ArrayList<RegexPatternDTO>();
			final RegexPatternDTO selectedRegexPattern = controller.getSelectedRegexPatternDTO();
			if (null != selectedRegexPattern) {
				selectedRegexPatterns.add(selectedRegexPattern);
			}
		}

		return selectedRegexPatterns;
	}

	/**
	 * Gets list of checked Batch Instances, if none is selected returns the current selected Batch Instance.
	 * 
	 * @return {@link List}<{@link BatchInstanceDTO}>
	 */
	// private void deleteSelectedRegexPatterns(List<RegexPatternDTO> selectedRegexPatterns) {
	// rpcService.deleteRegexPatterns(selectedRegexPatterns, new AsyncCallback<Boolean>() {
	//
	// @Override
	// public void onSuccess(Boolean result) {
	// if (result == true) {
	// Message.display(BatchClassMessages.DELETE_SELECTED_REGEX_PATTERN,
	// BatchClassMessages.SELECTED_REGEX_PATTERN_DELETED_SUCCESSFULLY);
	// } else {
	// Message.display(BatchClassMessages.DELETE_SELECTED_REGEX_PATTERN,
	// BatchClassMessages.SELECTED_REGEX_PATTERN_DELETED_SUCCESSFULLY);
	// }
	// controller.setSelectedRegexPatternDTO(null);
	// }
	//
	// @Override
	// public void onFailure(Throwable caught) {
	//
	// }
	// });
	// }

	/**
	 * eventhandler for deleting the new Regex Pattern
	 * 
	 * @param multipleDeletionStartEvent {@link StartMultipleRegexPatternDeletionEvent}
	 */
	// @EventHandler
	// public void handleMultipleDeletionStartEvent(StartMultipleRegexPatternDeletionEvent multipleDeletionStartEvent) {
	// if (null != multipleDeletionStartEvent) {
	// if (view.getGrid().getSelectedModels().size() > 0) {
	// final List<RegexPatternDTO> selectedRegexPatterns = getSelectedRegexPatterns();
	// if (CollectionUtil.isEmpty(selectedRegexPatterns)) {
	// DialogUtil.showMessageDialog(BatchClassConstants.ERROR, BatchClassMessages.PLEASE_SOME_REGEX_PATTERN_TO_DELETE,
	// DialogIcon.ERROR);
	// } else {
	// final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
	// BatchClassConstants.DELETE_SELECTED_REGEX_PATTERN,
	// BatchClassConstants.DELETE_THE_SELECTED_REGEX_PATTERN_MESSAGE, false, DialogIcon.QUESTION_MARK);
	// confirmationDialog.setDialogListener(new DialogListener() {
	//
	// @Override
	// public void onOkClick() {
	// confirmationDialog.hide();
	// deleteSelectedRegexPatterns(selectedRegexPatterns);
	// view.getGrid().removeItemsFromGrid(selectedRegexPatterns);
	//
	// RegexGroupDTO selectedGroup = controller.getSelectedRegexGroupDTO();
	// final List<RegexPatternDTO> regexPatternDTOs = selectedGroup.getRegexPatternDTOs(false);
	//
	// for (RegexPatternDTO regexPattern : regexPatternDTOs) {
	// for (RegexPatternDTO selectedPattern : selectedRegexPatterns) {
	// if (regexPattern.getIdentifier() == selectedPattern.getIdentifier()) {
	// selectedGroup.removeRegexPatternDTO(selectedPattern);
	// break;
	// }
	// }
	// }
	//
	// view.refreshGrid();
	// }
	//
	// @Override
	// public void onCloseClick() {
	//
	// }
	//
	// @Override
	// public void onCancelClick() {
	// confirmationDialog.hide();
	// }
	// });
	// }
	//
	// } else {
	// DialogUtil.showMessageDialog(BatchClassConstants.ERROR, BatchClassMessages.PLEASE_SOME_REGEX_PATTERN_TO_DELETE,
	// DialogIcon.ERROR);
	// }
	// }
	// }

	/**
	 * Gives view for selection of pattern for a selected group.
	 * 
	 * @param groupIdentifier {@link String}
	 */

	public void handleRegexGroupSelected(final RegexGroupDTO group) {
		if (null == group.getIdentifier() || group.getIdentifier().isEmpty()) {
			DialogUtil.showConfirmationDialog(
					LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
							.getMessageValue(BatchClassMessages.UNABLE_TO_READ_REGEX_GROUPS));
		} else {
			final List<RegexPatternDTO> regexPatternDTOs = group.getRegexPatternDTOs(false);
			if ((regexPatternDTOs == null)) {
				DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
								.getMessageValue(BatchClassMessages.NO_REGEX_PATTERNS));

			}
			showPatternList(regexPatternDTOs, group);
		}

	}

	/**
	 * Show list view of patterns for selected group.
	 * 
	 * @param regexPatternDTOs {@link List<{@link RegexPatternDTO}>}
	 * @param group {@link RegexGroupDTO}
	 */
	private void showPatternList(final List<RegexPatternDTO> regexPatternDTOs, final RegexGroupDTO group) {
		final RegexPatternSelectionGridView regexPatternSelectionGridView = getController().getRegexPatternSelectionGridView();
		final Map<String, RegexPatternDTO> patternMap = new HashMap<String, RegexPatternDTO>(regexPatternDTOs.size());
		for (RegexPatternDTO patternDTO : regexPatternDTOs) {
			if (null != patternDTO) {
				patternMap.put(patternDTO.getIdentifier(), patternDTO);
			}
		}
		regexPatternSelectionGridView.setData(patternMap.values());
		view.getGrid().selectGridCell(0, 0);
		view.showDialogWindow(view);
		// controller.showDialogWindow(regexPatternSelectionGridView, BatchClassConstants.REGEX_PATTERN);

	}

	/**
	 * groupSelectionEvent is invoked on the regex group event fire
	 * 
	 * @param regexGroupSelectionEvent {@link RegexGroupSelectionEvent}
	 */
	// @EventHandler
	// public void groupSelectionEvent(RegexGroupSelectionEvent regexGroupSelectionEvent) {
	// controller.getRegexPatternViewPresenter().layout(null);
	// }

	public RegexPatternDTO createRegexPatternDTOObject() {
		RegexPatternDTO regexPatternDTO = new RegexPatternDTO();

		regexPatternDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		regexPatternDTO.setNew(true);
		regexPatternDTO.setSelected(false);
		regexPatternDTO.setRegexGroupDTO(controller.getSelectedRegexGroupDTO());
		regexPatternDTO.setPattern(BatchClassConstants.EMPTY_STRING);
		regexPatternDTO.setDescription(BatchClassConstants.EMPTY_STRING);
		return regexPatternDTO;
	}

	/**
	 * createRegexPatternDTOObject() method used for creating new Regex Pattern instance
	 * 
	 * @return {@link RegexPatternDTO}
	 */
	// public void insertRegexPattern(final RegexPatternDTO regexPatternDTO) {
	// if (null != regexPatternDTO) {
	// rpcService.insertRegexPattern(regexPatternDTO, controller.getSelectedRegexGroupDTO(), new AsyncCallback<Boolean>() {
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// }
	//
	// @Override
	// public void onSuccess(Boolean result) {
	// if (result) {
	// } else {
	// DialogUtil.showMessageDialog(BatchClassMessages.INSERTION_FAILED,
	// BatchClassMessages.REGEX_PATTERN_INSERTION_FAILED, DialogIcon.ERROR);
	// }
	// }
	//
	// });
	// }
	// }

	/**
	 * insertRegexPattern Method used for inserting the new Regex Pattern
	 * 
	 * @param regexPattern {@link RegexPatternDTO}
	 */
	@EventHandler
	public void handleRegexPatternAdditionEvent(AddRegexPatternEvent addEvent) {
		view.getGrid().getStore().commitChanges();
		if (null != addEvent) {
			if (view.isValid()) {
				RegexPatternDTO regexPatternDTO = createRegexPatternDTOObject();
				if (null != regexPatternDTO) {
					controller.setSelectedRegexPatternDTO(regexPatternDTO);
					view.add(regexPatternDTO);
				}

			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						"Cannot add more fields in Regex Pattern Grid . Validate the current fields to add more fields.", DialogIcon.ERROR);
			}
		}
	}

	public boolean isValid() {
		return view.isValid();
	}

	public void handleDoubleClickEvent(final RegexPatternDTO selectedRegexPattern) {
		if (null != selectedRegexPattern) {
			controller.setSelectedRegexPatternDTO(selectedRegexPattern);
			view.hideRegexDialogWindow(view);
			// controller
			// .getRegexGroupSelectionGridPresenter()
			// .getCurrentValueProvider()
			// .setValue(controller.getRegexGroupSelectionGridPresenter().getCurrentSelectedObject(),
			// selectedRegexPattern.getPattern());
			if (null != controller.getRegexGroupSelectionGridView().getRegexComboBoxHandler()) {

				// controller.getRegexGroupSelectionGridView().getRegexComboBoxHandler().onRegexSelect(selectedRegexPattern.getPattern());
				controller
						.getRegexGroupSelectionGridView()
						.getRegexComboBoxHandler()
						.onRegexSelect(selectedRegexPattern.getPattern(),
								controller.getRegexGroupSelectionGridPresenter().getCurrentSelectedObject(),
								controller.getRegexGroupSelectionGridPresenter().getCurrentValueProvider());

			}

			// Object previousObject = controller.getCurrentSelectedObject();
			// controller.getCurrentValueProvider().setValue(previousObject, selectedRegexPattern.getPattern());
			// controller.getCurrentTblExtrRule().setRegexType(selectedRegexPattern.getPattern());
			// controller.getCurrentTblExtrRule().getStartRegexPatternModel()
			// .setPatternTextCellContent(selectedRegexPattern.getPattern());
			// controller.getGrid().reLoad();
		}
	}

	public void onBackButtonClick() {
		view.hideRegexDialogWindow(view);
		controller.getRegexGroupSelectionGridPresenter().bind();
	}
}
