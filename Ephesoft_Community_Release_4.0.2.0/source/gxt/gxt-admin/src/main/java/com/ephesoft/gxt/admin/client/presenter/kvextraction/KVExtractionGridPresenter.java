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

package com.ephesoft.gxt.admin.client.presenter.kvextraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AddKVExtractionEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedKVExtractionShowEvent;
import com.ephesoft.gxt.admin.client.event.DeleteKVExtractionEvent;
import com.ephesoft.gxt.admin.client.event.EnableDisableEditKVEvent;
import com.ephesoft.gxt.admin.client.event.ReloadKVExtractionGridEvent;
import com.ephesoft.gxt.admin.client.event.ReloadKVGridEvent;
import com.ephesoft.gxt.admin.client.event.ValidateKVForAdvKV;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.kvextraction.KVExtractionGridView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.AdvancedKVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.KVExtractionDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.google.web.bindery.event.shared.binder.GenericEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

public class KVExtractionGridPresenter extends BatchClassInlinePresenter<KVExtractionGridView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<KVExtractionGridPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public KVExtractionGridPresenter(BatchClassManagementController controller, KVExtractionGridView gridView) {
		super(controller, gridView);
	}

	@Override
	public void bind() {
		if (null != controller.getSelectedBatchClass()) {
			view.clearSelectionModel();
			view.reloadGrid();
			view.setComponents(controller.getRegexBuilderView(), controller.getRegexGroupSelectionGridView());
		}
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	/**
	 * Method to get the key Value Extraction dtos.
	 * 
	 * @return the key Value Extraction dtos {@link Collection{@link KVExtractionDTO}
	 */
	public Collection<KVExtractionDTO> getKVFromSelectedDTO() {
		FieldTypeDTO selectedFieldType = controller.getSelectedFieldType();
		Collection<KVExtractionDTO> kvExtractionList = null;
		if (selectedFieldType != null) {
			kvExtractionList = selectedFieldType.getKvExtractionList();

		}
		return kvExtractionList;
	}

	/**
	 * Handle deletion of multiple key Values.
	 * 
	 * @param deleteEvent the delete event {@link DeleteKVExtractionEvent}
	 */
	@EventHandler
	public void handleKVDeletion(DeleteKVExtractionEvent deleteEvent) {
		if (null != deleteEvent) {
			final List<KVExtractionDTO> selectedKVList = view.getSelectedKVs();
			if (!CollectionUtil.isEmpty(selectedKVList)) {

				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_KV_MSG), false, DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						confirmationDialog.hide();
						deleteKVExtractions(selectedKVList);
					}

					@Override
					public void onCancelClick() {
						confirmationDialog.hide();
					}
				});
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.NO_ROW_SELECTED), DialogIcon.INFO);
			}
		}
	}

	/**
	 * Delete selected key values.
	 * 
	 * @param kvExtractions the selected key values {@link List<{@link KVExtractionDTO}>}
	 */
	private void deleteKVExtractions(List<KVExtractionDTO> kvExtractions) {

		FieldTypeDTO docTypeDTO = controller.getSelectedFieldType();
		for (KVExtractionDTO kvExtraction : kvExtractions) {
			if (null != kvExtraction) {
				docTypeDTO.getKVExtractionDTOByIdentifier(kvExtraction.getIdentifier()).setDeleted(true);
			}
		}
		setBatchClassDirtyOnChange();
		view.getKVExtractionGrid().removeItemsFromGrid(kvExtractions);

		view.getKVExtractionGrid().getStore().commitChanges();
		view.reloadGrid();
		enableDisableEditButton(view.getKVExtractionGrid().getStore().getAll());
	}

	/**
	 * Handle new key value addition.
	 * 
	 * @param addEvent the add event
	 */
	@EventHandler
	public void handleKVAddition(AddKVExtractionEvent addEvent) {
		if (null != addEvent) {
			KVExtractionDTO kvExtractionDTO = createKVExtractionDTO();
			FieldTypeDTO fieldTypeDTO = controller.getSelectedFieldType();
			if (fieldTypeDTO != null && view.getKVExtractionGrid().addNewItemInGrid(kvExtractionDTO)) {
				fieldTypeDTO.addKvExtraction(kvExtractionDTO);
				setBatchClassDirtyOnChange();
				view.getKVExtractionGrid().getStore().commitChanges();
				enableDisableEditButton(view.getKVExtractionGrid().getStore().getAll());
			}
		}
	}

	/**
	 * Method to create the new key value Extraction dto.
	 * 
	 * @return the key value dto {@link KVExtractionDTO}
	 */
	private KVExtractionDTO createKVExtractionDTO() {
		KVExtractionDTO kvExtractionDTO = createKVExtractionDTOObject();
		kvExtractionDTO.setLocationType(LocationType.TOP);
		kvExtractionDTO.setWeight(BatchClassConstants.MAX_WEIGHT_RANGE);
		kvExtractionDTO.setFieldTypeDTO(controller.getSelectedFieldType());
		return kvExtractionDTO;

	}

	/**
	 * Method to set the batch class dirty on change.
	 * 
	 */
	public void setBatchClassDirtyOnChange() {
		controller.getSelectedFieldType().getDocTypeDTO().getBatchClass().setDirty(true);
	}

	public boolean isValid() {
		if (view.getGrid().getStore() != null && view.getGrid().getStore().size() != 0) {
			return view.isGridValidated();
		}
		return true;
	}

	@EventHandler
	public void validateKVForAdvKV(ValidateKVForAdvKV testEmailEvent) {
		controller.setSelectedDocumentType(controller.getSelectedFieldType().getDocTypeDTO());
		if (isValid()) {
			if (!testEmailEvent.isEdit()) {
				KVExtractionDTO kvExtractionDTO = createKVExtractionDTOObject();
				AdvancedKVExtractionDTO advancedKVExtractionDTO = new AdvancedKVExtractionDTO();
				kvExtractionDTO.setAdvancedKVExtractionDTO(advancedKVExtractionDTO);
				openAdvKVView(kvExtractionDTO, false);
			} else {
				view.getKVExtractionGrid().getStore().commitChanges();
				final List<KVExtractionDTO> selectedKVExtraction = view.getSelectedKVs();
				int totalKVExtraction = view.getKVExtractionGrid().getPaginationLoader().getTotalCount();
				if (!testEmailEvent.isCtrlKey()) {
					if (CollectionUtil.isEmpty(selectedKVExtraction)) {
						if (totalKVExtraction == 0) {
							showMessage(BatchClassMessages.NO_RECORD_TO_EDIT);
						} else {
							// List<KVExtractionDTO> kvExtractionDTOs = view.getKVExtractionGrid().getSelectionModel().getSelection();
							if (null != controller.getSelectedKVExtraction()/* !CollectionUtil.isEmpty(kvExtractionDTOs) */) {
								// KVExtractionDTO kvExtractionDTO =
								// view.getKVExtractionGrid().getSelectionModel().getSelection().get(0);
								openAdvKVView(controller.getSelectedKVExtraction(), testEmailEvent.isEdit());
							} else {
								showMessage(BatchClassMessages.NO_RECORD_TO_EDIT);
							}
						}
					} else {
						if (selectedKVExtraction.size() > 1) {
							showMessage(BatchClassMessages.SELECT_ONE_ROW_ONLY_MSG);
						} else {
							openAdvKVView(selectedKVExtraction.get(0), testEmailEvent.isEdit());
						}
					}
				} else {
					if (totalKVExtraction != 0) {
						if (null != controller.getSelectedKVExtraction()) {
							openAdvKVView(controller.getSelectedKVExtraction(), testEmailEvent.isEdit());
						}
					}
				}
			}
		} else {
			showMessage(BatchClassMessages.KV_INVALID);
		}

	}

	private void showMessage(String messageValue) {
		messageValue = LocaleDictionary.getMessageValue(messageValue);
		if (!messageValue.equals(BatchClassMessages.KV_INVALID)) {
			messageValue = StringUtil.concatenate(messageValue,
					LocaleDictionary.getMessageValue(BatchClassMessages.EDIT_KV_EXTRACTION));
		}
		DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE), messageValue,
				DialogIcon.WARNING);
	}

	private void openAdvKVView(KVExtractionDTO kvExtractionDTO, boolean isEdit) {
		controller.setSelectedKVExtraction(kvExtractionDTO);
		controller.getEventBus().fireEvent(new AdvancedKVExtractionShowEvent(isEdit));
	}

	public KVExtractionDTO createKVExtractionDTOObject() {
		KVExtractionDTO kvExtractionDTO = new KVExtractionDTO();
		kvExtractionDTO.setNew(true);
		kvExtractionDTO.setFieldTypeDTO(controller.getSelectedFieldType());
		kvExtractionDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		String kvFieldOrderNo = getKVFieldOrderNo();
		if (kvFieldOrderNo != null) {
			kvExtractionDTO.setOrderNumber(kvFieldOrderNo);
		}
		kvExtractionDTO.setWeight(BatchClassConstants.DEFAULT_WEIGHT);
		return kvExtractionDTO;
	}

	public String getKVFieldOrderNo() {
		String kvFieldOrderNo = null;
		if (controller.getSelectedFieldType().getDocTypeDTO() != null) {
			Collection<KVExtractionDTO> kvList = controller.getSelectedFieldType().getKvExtractionList();
			if (CollectionUtil.isEmpty(kvList) && kvList.size() != 0) {
				ArrayList<Integer> kvOrderList = new ArrayList<Integer>();
				for (KVExtractionDTO kvTypeDto : kvList) {
					kvOrderList.add(Integer.parseInt(kvTypeDto.getOrderNumber()));
				}
				Integer maxKVOrder = Collections.max(kvOrderList);
				if (maxKVOrder > (Integer.MAX_VALUE - BatchClassConstants.FIELD_ORDER_DIFFERENCE)) {

					// setting the field order to be empty if generated field order is not in integer range
					kvFieldOrderNo = BatchClassConstants.EMPTY_STRING;
				} else {
					Integer newKVOrder = Collections.max(kvOrderList) + BatchClassConstants.FIELD_ORDER_DIFFERENCE;
					kvFieldOrderNo = newKVOrder.toString();
				}
			} else {
				kvFieldOrderNo = BatchClassConstants.INITIAL_FIELD_ORDER_NUMBER.toString();
			}
		}
		return kvFieldOrderNo;
	}

	@EventHandler
	public void reloadKVGrid(ReloadKVGridEvent event) {
		view.reloadGrid();
	}

	public void enableDisableEditButton(List<KVExtractionDTO> extractionDTOs) {
		if (extractionDTOs != null && extractionDTOs.size() != 0) {
			BatchClassManagementEventBus.fireEvent(new EnableDisableEditKVEvent(true));
		} else {
			BatchClassManagementEventBus.fireEvent(new EnableDisableEditKVEvent(false));
		}
	}

	public void selectKVExtraction(CellSelectionChangedEvent<KVExtractionDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			KVExtractionDTO selectedKVExtraction = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			controller.setSelectedKVExtraction(selectedKVExtraction);
		}

	}
	
	/**
	 * Reload KV Extraction Grid.
	 *
	 * @param reloadGridEvent the reload grid event
	 */
	@EventHandler
	public void reloadGrid(ReloadKVExtractionGridEvent reloadGridEvent){
		if (null != controller.getSelectedBatchClass()) {
			view.clearSelectionModel();
			view.reloadGrid();
			view.setComponents(controller.getRegexBuilderView(), controller.getRegexGroupSelectionGridView());
		}
	}

}
