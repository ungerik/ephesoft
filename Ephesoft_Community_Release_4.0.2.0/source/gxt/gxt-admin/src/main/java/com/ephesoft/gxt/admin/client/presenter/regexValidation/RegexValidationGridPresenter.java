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

package com.ephesoft.gxt.admin.client.presenter.regexValidation;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AddRegexEvent;
import com.ephesoft.gxt.admin.client.event.BatchClassLoadEvent;
import com.ephesoft.gxt.admin.client.event.DeleteRegexEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.regexValidation.RegexValidationGridView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogListener;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.ui.widget.window.MessageDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.RegexDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

public class RegexValidationGridPresenter extends BatchClassInlinePresenter<RegexValidationGridView> {

	interface CustomEventBinder extends EventBinder<RegexValidationGridPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public RegexValidationGridPresenter(BatchClassManagementController controller, RegexValidationGridView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
		if (controller.doBatchClassRequireReload()) {
			BatchClassDTO selectedBatchClass = controller.getSelectedBatchClass();
			if (null != selectedBatchClass) {
				final MessageDialog dialog = DialogUtil.showMessageDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE), LocaleDictionary.getMessageValue(BatchClassMessages.LOADING_INDEX_FIELD_FROM_SERVER));
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

	public void selectRegexValidationField(CellSelectionChangedEvent<RegexDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			RegexDTO selectedRegexField = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			controller.setSelectedRegexField(selectedRegexField);
		}
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	public Collection<RegexDTO> getCurrentRegexFields() {
		FieldTypeDTO selectedFieldType = controller.getSelectedFieldType();
		Collection<RegexDTO> regexValidationList = null;
		if (selectedFieldType != null) {
			regexValidationList = selectedFieldType.getRegexList(false);
		}
		return regexValidationList;
	}

	@EventHandler
	public void handleRegexValidationAdditionEvent(AddRegexEvent addEvent) {
		view.getRegexValidationFieldGrid().getStore().commitChanges();
		if (null != addEvent) {
			RegexDTO regexDTO = createNewRegexValidation();
			FieldTypeDTO fieldType = controller.getSelectedFieldType();
			if (null != fieldType && view.getRegexValidationFieldGrid().addNewItemInGrid(regexDTO)) {
				fieldType.addRegex(regexDTO);
				controller.getSelectedBatchClass().addRegexValidationConfiguration(regexDTO);
				controller.getSelectedBatchClass().setDirty(true);
			}
		}
	}

	private RegexDTO createNewRegexValidation() {
		RegexDTO regexDTO = new RegexDTO();
		regexDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		regexDTO.setPattern(BatchClassConstants.EMPTY_STRING);
		regexDTO.setNew(true);
		regexDTO.setFieldTypeDTO(controller.getSelectedFieldType());
		return regexDTO;

	}

	@SuppressWarnings("static-access")
	@EventHandler
	public void handleMultipleIndexFieldDeletion(DeleteRegexEvent deleteEvent) {
		if (null != deleteEvent) {

			final List<RegexDTO> selectedRegexFields = view.getSelectedRegexFields();
			if (!CollectionUtil.isEmpty(selectedRegexFields)) {

				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION), LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_SELECTED_VALIDATION_RULES)
						,false, DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						confirmationDialog.hide();
						deleteSelectedIndexFields(selectedRegexFields);
					}

					@Override
					public void onCancelClick() {
						confirmationDialog.hide();
					}
				});
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE), LocaleDictionary
						.getLocaleDictionary().getMessageValue(BatchClassMessages.NO_ROW_SELECTED), DialogIcon.INFO);
			}
		}
	}

	private void deleteSelectedIndexFields(List<RegexDTO> selectedRegexValidation) {
		BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
		for (RegexDTO regexValidation : selectedRegexValidation) {
			if (null != regexValidation) {
				batchClassDTO.getDocTypeByName(regexValidation.getFieldTypeDTO().getDocTypeDTO().getName()).getFieldTypeByIdentifier(
						regexValidation.getFieldTypeDTO().getIdentifier()).getRegexDTOByIdentifier(regexValidation.getIdentifier())
						.setDeleted(true);
			}
		}
		controller.getSelectedBatchClass().setDirty(true);
		view.getRegexValidationFieldGrid().removeItemsFromGrid(selectedRegexValidation);
		view.getRegexValidationFieldGrid().getStore().commitChanges();
		WidgetUtil.reLoadGrid(view.getRegexValidationFieldGrid());
	}

	public boolean isValid() {
		return view.isValid();
	}

	public void setBatchClassDirtyOnChange() {
		controller.getSelectedBatchClass().setDirty(true);
	}

}
