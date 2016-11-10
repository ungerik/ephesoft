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

package com.ephesoft.gxt.admin.client.presenter.indexFiled;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ephesoft.dcma.core.common.BarcodeType;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.ValidationOperator;
import com.ephesoft.dcma.core.common.WidgetType;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AddIndexFieldEvent;
import com.ephesoft.gxt.admin.client.event.BatchClassLoadEvent;
import com.ephesoft.gxt.admin.client.event.CopyIndexFieldsEvent;
import com.ephesoft.gxt.admin.client.event.DeleteIndexFieldsEvent;
import com.ephesoft.gxt.admin.client.event.ExportSelectedIndexFieldEvent;
import com.ephesoft.gxt.admin.client.event.LoadImportedIndexFieldEvent;
import com.ephesoft.gxt.admin.client.event.RetrieveSelectedIndexFieldListEvent;
import com.ephesoft.gxt.admin.client.event.SubTreeAdditionEvent.IndexFieldSubTreeAdditionEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.indexFields.IndexFieldGridView;
import com.ephesoft.gxt.core.client.constant.CategoryType;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.ui.widget.window.MessageDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.DTOUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

public class IndexFieldGridPresenter extends BatchClassInlinePresenter<IndexFieldGridView> {

	interface CustomEventBinder extends EventBinder<IndexFieldGridPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public IndexFieldGridPresenter(BatchClassManagementController controller, IndexFieldGridView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
		if (controller.doBatchClassRequireReload()) {
			BatchClassDTO selectedBatchClass = controller.getSelectedBatchClass();
			if (null != selectedBatchClass) {
				final MessageDialog dialog = DialogUtil.showMessageDialog(LocaleDictionary
						.getConstantValue(BatchClassConstants.INFO_TITLE), LocaleDictionary
						.getMessageValue(BatchClassMessages.LOADING_DOCUMENT_TYPE_FROM_SERVER));
				dialog.getButton(PredefinedButton.OK).hide();
				dialog.setAnimationDuration(100);
				rpcService.getBatchClass(selectedBatchClass.getIdentifier(), new AsyncCallback<BatchClassDTO>() {

					@Override
					public void onSuccess(BatchClassDTO result) {
						dialog.hide();
						controller.setSelectedBatchClass(result);
						controller.setBatchClassRequiresReload(false);
						view.reloadGrid();
						controller.setSelectedBatchClass(result);
						BatchClassManagementEventBus.fireEvent(new BatchClassLoadEvent(result));
					}

					@Override
					public void onFailure(Throwable caught) {
					}
				});
			}
		} else {
			view.reloadGrid();
		}

		view.setComponents(controller.getRegexBuilderView(), controller.getRegexGroupSelectionGridView());
	}

	public void selectIndexField(CellSelectionChangedEvent<FieldTypeDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			FieldTypeDTO selectedIndexField = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			controller.setSelectedFieldType(selectedIndexField);
		}
	}

	@EventHandler
	public void handleIndexFieldAdditionEvent(AddIndexFieldEvent addEvent) {
		view.getIndexFieldGrid().getStore().commitChanges();
		if (null != addEvent) {
			if (isValid()) {
				FieldTypeDTO fieldTypeDTO = createNewIndexField();
				DocumentTypeDTO documentType = controller.getSelectedDocumentType();
				if (null != documentType) {
					documentType.addFieldType(fieldTypeDTO);
					fieldTypeDTO.setDocTypeDTO(documentType);
					controller.setSelectedFieldType(fieldTypeDTO);
					controller.getSelectedBatchClass().setDirty(true);
					view.getIndexFieldGrid().addNewItemInGrid(fieldTypeDTO);
					if (!controller.getIndexFieldView().getIndexFieldsMenuView().isExportEnable()) {
						toggleExportButton(true);
					}
				}
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE), LocaleDictionary
						.getMessageValue(BatchClassMessages.CANNOT_ADD_MORE_FIELDS_VALIDATE_VIEW_FIRST));
			}
		}
	}

	private FieldTypeDTO createNewIndexField() {
		FieldTypeDTO indexFieldDTO = new FieldTypeDTO();
		indexFieldDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		indexFieldDTO.setName(BatchClassConstants.EMPTY_STRING);
		indexFieldDTO.setDescription(BatchClassConstants.EMPTY_STRING);
		indexFieldDTO.setDataType(DataType.STRING);
		indexFieldDTO.setWidgetType(WidgetType.TEXT);
		// indexFieldDTO.setCategory("Uncategorised");
		indexFieldDTO.setCategory(CategoryType.GROUP_1.getCategoryName());
		indexFieldDTO.setPattern(BatchClassConstants.EMPTY_STRING);
		indexFieldDTO.setOcrConfidenceThreshold(90);
		indexFieldDTO.setSampleValue(BatchClassConstants.EMPTY_STRING);
		indexFieldDTO.setBarcodeType(BarcodeType.NONE.getBarcodeTypeValue());
		indexFieldDTO.setHidden(false);
		indexFieldDTO.setNew(true);
		indexFieldDTO.setReadOnly(false);
		indexFieldDTO.setFieldValueChangeScriptEnabled(false);
		indexFieldDTO.setRegexListingSeparator(ValidationOperator.OR.name());

		int fieldOrderNumber = getFieldOrderNumber();
		indexFieldDTO.setFieldOrderNumber(fieldOrderNumber);

		return indexFieldDTO;

	}

	public int getFieldOrderNumber() {
		int fieldOrderNumber = 0;
		if (controller.getSelectedDocumentType() != null) {
			Collection<FieldTypeDTO> dlfList = controller.getSelectedDocumentType().getFields();
			if (null != dlfList && !dlfList.isEmpty()) {
				ArrayList<Integer> fieldOrderList = new ArrayList<Integer>();
				for (FieldTypeDTO fieldTypeDto : dlfList) {
					fieldOrderList.add(fieldTypeDto.getFieldOrderNumber());
				}
				Integer maxFieldOrder = Collections.max(fieldOrderList);
				if (maxFieldOrder > (Integer.MAX_VALUE - 1)) {
					fieldOrderNumber = 0;
				} else {
					Integer newFieldOrder = Collections.max(fieldOrderList) + 1;
					fieldOrderNumber = newFieldOrder;
				}
			}
		}
		return fieldOrderNumber;
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	public Collection<FieldTypeDTO> getCurrentFieldTypes() {
		DocumentTypeDTO selectedDocumentType = controller.getSelectedDocumentType();
		Collection<FieldTypeDTO> indexFieldCollection = null;
		if (selectedDocumentType != null) {
			indexFieldCollection = selectedDocumentType.getFields(false);
		}
		return indexFieldCollection;
	}

	@EventHandler
	public void handleMultipleIndexFieldDeletion(DeleteIndexFieldsEvent deleteEvent) {
		if (null != deleteEvent) {

			final List<FieldTypeDTO> selectedIndexFields = view.getSelectedIndexFields();
			if (!CollectionUtil.isEmpty(selectedIndexFields)) {

				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(LocaleDictionary
						.getConstantValue(BatchClassConstants.CONFIRMATION), LocaleDictionary
						.getMessageValue(BatchClassMessages.SURE_TO_DELETE_INDEX_FIELD), false, DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						confirmationDialog.hide();
						deleteSelectedIndexFields(selectedIndexFields);
						Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.DELETE_INDEX_FIELD), LocaleDictionary
								.getMessageValue(BatchClassMessages.INDEX_FIELD_DELETED_SUCESSFULLY));
						if (view.getSizeOfGrid() < 1) {
							toggleExportButton(false);
						}

					}

					@Override
					public void onCloseClick() {

					}

					@Override
					public void onCancelClick() {
						confirmationDialog.hide();
					}
				});
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
						.getMessageValue(BatchClassMessages.SELECT_INDEX_FIELD_TO_DELETE), DialogIcon.ERROR);
			}
		}
	}

	private void deleteSelectedIndexFields(final List<FieldTypeDTO> selectedIndexFields) {
		BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
		for (FieldTypeDTO indexField : selectedIndexFields) {
			if (null != indexField) {
				batchClassDTO.getDocTypeByName(indexField.getDocTypeDTO().getName()).getFieldTypeByIdentifier(
						indexField.getIdentifier()).setDeleted(true);
			}
		}
		controller.getSelectedBatchClass().setDirty(true);
		view.getIndexFieldGrid().removeItemsFromGrid(selectedIndexFields);
		view.getIndexFieldGrid().getStore().commitChanges();
		controller.setSelectedFieldType(null);
		WidgetUtil.reLoadGrid(view.getIndexFieldGrid());
	}

	public void setBatchClassDirtyOnChange() {
		controller.getSelectedBatchClass().setDirty(true);
	}

	public boolean isValid() {
		return view.isValid();
	}

	@EventHandler
	public void handleRetrieveIndexFieldListEvent(RetrieveSelectedIndexFieldListEvent retrieveDocumentTypeEvent) {
		if (null != retrieveDocumentTypeEvent) {
			List<FieldTypeDTO> listOfField = view.getSelectedIndexFields();
			if (!CollectionUtil.isEmpty(listOfField)) {
				BatchClassManagementEventBus.fireEvent(new ExportSelectedIndexFieldEvent(listOfField));
			} else {
				if (null != controller.getSelectedFieldType()) {
					List<FieldTypeDTO> selectedFieldType = new ArrayList<FieldTypeDTO>();
					selectedFieldType.add(controller.getSelectedFieldType());
					BatchClassManagementEventBus.fireEvent(new ExportSelectedIndexFieldEvent(selectedFieldType));
				} else {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SELECT_ATLEAST_ONE_INDEX_FIELDS_TO_EXPORT),
							DialogIcon.WARNING);
				}
			}
		}
	}

	@EventHandler
	public void handleCopyIndexFieldEvent(CopyIndexFieldsEvent copyEvent) {
		if (null != copyEvent) {
			view.getIndexFieldGrid().getStore().commitChanges();
			final List<FieldTypeDTO> selectedBatchClasses = view.getSelectedIndexFields();
			if (CollectionUtil.isEmpty(selectedBatchClasses)) {
				FieldTypeDTO batchClassDTO = controller.getSelectedFieldType();
				if (null != batchClassDTO) {
					copyIndexField(batchClassDTO);
				} else {
					showMessage(BatchClassMessages.NO_RECORD_TO_EDIT);
				}
			} else {
				if (selectedBatchClasses.size() > 1) {
					showMessage(BatchClassMessages.SELECT_ONE_ROW_ONLY_MSG);
				} else {
					copyIndexField(selectedBatchClasses.get(0));
				}
			}
		}
	}

	private void copyIndexField(FieldTypeDTO selectedFieldType) {
		if (null != selectedFieldType) {
			if (view.isValid()) {
				FieldTypeDTO fieldTypeDTO = createCopyIndexField(selectedFieldType);
				DocumentTypeDTO documentType = controller.getSelectedDocumentType();
				if (null != documentType) {
					documentType.addFieldType(fieldTypeDTO);
					fieldTypeDTO.setDocTypeDTO(documentType);
					controller.setSelectedFieldType(fieldTypeDTO);
					controller.getSelectedBatchClass().setDirty(true);
					view.getIndexFieldGrid().addNewItemInGrid(fieldTypeDTO);
					Info.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS_TITLE), LocaleDictionary
							.getMessageValue(BatchClassMessages.INDEX_FIELD_COPIED_SUCCESSFULLY));
				}
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
						.getMessageValue(BatchClassMessages.VALIDATE_INDEX_FIELD_FIRST_TO_COPY), DialogIcon.ERROR);
			}
		} else {
			showMessage(BatchClassMessages.NO_RECORD_TO_EDIT);
		}
	}

	private void showMessage(String messageValue) {
		messageValue = LocaleDictionary.getMessageValue(messageValue);
		messageValue = StringUtil.concatenate(messageValue, LocaleDictionary.getMessageValue(BatchClassMessages.COPY_INDEX_FIELD));
		DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE), messageValue,
				DialogIcon.WARNING);
	}

	private FieldTypeDTO createCopyIndexField(FieldTypeDTO selectedFieldType) {
		FieldTypeDTO indexFieldDTO = new FieldTypeDTO();
		indexFieldDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		indexFieldDTO.setName(selectedFieldType.getName());
		indexFieldDTO.setDescription(selectedFieldType.getDescription());
		indexFieldDTO.setDataType(selectedFieldType.getDataType());
		indexFieldDTO.setWidgetType(selectedFieldType.getWidgetType());
		indexFieldDTO.setCategory(selectedFieldType.getCategory());
		indexFieldDTO.setPattern(selectedFieldType.getPattern());
		indexFieldDTO.setOcrConfidenceThreshold(selectedFieldType.getOcrConfidenceThreshold());
		indexFieldDTO.setSampleValue(selectedFieldType.getSampleValue());
		indexFieldDTO.setBarcodeType(selectedFieldType.getBarcodeType());
		indexFieldDTO.setHidden(selectedFieldType.isHidden());
		indexFieldDTO.setNew(selectedFieldType.isNew());
		indexFieldDTO.setReadOnly(selectedFieldType.getReadOnly());
		indexFieldDTO.setFieldValueChangeScriptEnabled(false);
		indexFieldDTO.setRegexListingSeparator(selectedFieldType.getRegexListingSeparator());
		indexFieldDTO.setFieldOptionValueList(selectedFieldType.getFieldOptionValueList());
		indexFieldDTO.setNew(true);
		indexFieldDTO.setKvExtractionList(DTOUtil.createKVExtractionDTOList(selectedFieldType.getKvExtractionList()));
		indexFieldDTO.setRegexList(DTOUtil.createRegExDTOList(selectedFieldType.getRegexList()));

		int fieldOrderNumber = getFieldOrderNumber();
		indexFieldDTO.setFieldOrderNumber(fieldOrderNumber);

		return indexFieldDTO;

	}

	@EventHandler
	public void handleLoadImportIndexField(LoadImportedIndexFieldEvent importedIndexField) {
		if (null != importedIndexField) {
			if (null != importedIndexField.getImportedIndexField() && importedIndexField.getImportedIndexField().size() > 0) {
				controller.getSelectedBatchClass().setDirty(true);
				for (FieldTypeDTO fieldTypeDTO : importedIndexField.getImportedIndexField()) {
					fieldTypeDTO.setDocTypeDTO(controller.getSelectedDocumentType());
					controller.getSelectedDocumentType().addFieldType(fieldTypeDTO);
					view.getIndexFieldGrid().addNewItemInGrid(fieldTypeDTO);
					BatchClassManagementEventBus.fireEvent(new IndexFieldSubTreeAdditionEvent(fieldTypeDTO));
				}
				Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.IMPORT_INDEX_FIELD), LocaleDictionary
						.getMessageValue(BatchClassMessages.INDEX_FIELD_IMPORTED_SUCCESSFULLY));
				if (!controller.getIndexFieldView().getIndexFieldsMenuView().isExportEnable()) {
					toggleExportButton(true);
				}
				view.getIndexFieldGrid().completeEditing(
						new CompleteEditEvent<FieldTypeDTO>(view.getIndexFieldGrid().getCurrentSelectedCell()));
			}
			String message = importedIndexField.getMessage();
			if (null != message && !CoreCommonConstants.EMPTY_STRING.equals(message)) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), message,
						DialogIcon.ERROR);
			}
		}
	}

	public void toggleExportButton(boolean status) {
		controller.getIndexFieldView().getIndexFieldsMenuView().toggleExportButton(status);
	}
}
