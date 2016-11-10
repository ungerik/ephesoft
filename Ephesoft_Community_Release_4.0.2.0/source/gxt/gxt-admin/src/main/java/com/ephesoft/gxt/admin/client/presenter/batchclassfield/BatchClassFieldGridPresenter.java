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

package com.ephesoft.gxt.admin.client.presenter.batchclassfield;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AddBatchClassFieldEvent;
import com.ephesoft.gxt.admin.client.event.DeleteBatchClassFieldsEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.batchclassfield.BatchClassFieldGridView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassFieldDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * This presenter deals with Batch Class Field Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.presenter.batchclassfield.BatchClassFieldGridPresenter
 */
public class BatchClassFieldGridPresenter extends BatchClassInlinePresenter<BatchClassFieldGridView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<BatchClassFieldGridPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Instantiates a new batch class field grid presenter.
	 * 
	 * @param controller the controller
	 * @param view the view
	 */
	public BatchClassFieldGridPresenter(final BatchClassManagementController controller, final BatchClassFieldGridView view) {
		super(controller, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.event.BindHandler#bind()
	 */
	@Override
	public void bind() {
		if (null != controller.getSelectedBatchClass()) {
			view.loadGrid();
			view.setComponents(controller.getRegexBuilderView(), controller.getRegexGroupSelectionGridView());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	/**
	 * Method to get the batch class field dtos.
	 * 
	 * @return the batch class field dtos {@link Collection<{@link BatchClassFieldDTO}>}.
	 */
	public Collection<BatchClassFieldDTO> getBatchClassFieldDTOs() {
		final BatchClassDTO selectedBatchClass = controller.getSelectedBatchClass();
		Collection<BatchClassFieldDTO> bcFieldCollection = null;
		if (selectedBatchClass != null) {
			bcFieldCollection = selectedBatchClass.getBatchClassField();
		}
		return bcFieldCollection;
	}

	/**
	 * Handle multiple batch class fields deletion.
	 * 
	 * @param deleteEvent the delete event {@link DeleteBatchClassFieldsEvent}
	 */
	@EventHandler
	public void handleBatchClassFieldsDeletion(final DeleteBatchClassFieldsEvent deleteEvent) {
		if (null != deleteEvent) {
			final List<BatchClassFieldDTO> selectedBCFields = view.getSelectedBatchClassField();
			if (!CollectionUtil.isEmpty(selectedBCFields)) {
				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_BATCH_CLASS_FIELDS_MSG), false,
						DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
						deleteSelectedBatchClassFields(selectedBCFields);
					}

					@Override
					public void onCancelClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
					}
				});
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.NO_ROW_SELECTED), DialogIcon.WARNING);
			}
		}
	}

	/**
	 * Delete the selected batch class fields.
	 * 
	 * @param selectedBCFields the selected batch class fields {@link List<{@link BatchClassFieldDTO}>}
	 */
	private void deleteSelectedBatchClassFields(final List<BatchClassFieldDTO> selectedBCFields) {

		final BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
		for (final BatchClassFieldDTO bcField : selectedBCFields) {
			if (null != bcField) {
				batchClassDTO.getBatchClassFieldDTOByIdentifier(bcField.getIdentifier()).setDeleted(true);
			}
		}
		controller.setBatchClassDirtyFlg(true);
		view.removeItemsFromGrid(selectedBCFields);
		view.commitChanges();
		view.reLoadGrid();
		Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS),
				LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_BATCH_CLASS_FIELDS_SUCCESS_MSG));
	}

	/**
	 * Handle batch class field addition.
	 * 
	 * @param addEvent the add event {@link AddBatchClassFieldEvent}.
	 */
	@EventHandler
	public void handleBatchClassFieldAddition(final AddBatchClassFieldEvent addEvent) {
		view.commitChanges();
		if (null != addEvent) {
			final BatchClassFieldDTO bcFieldDTO = createNewBatchClassField();
			if (view.addNewItemInGrid(bcFieldDTO)) {
				controller.getSelectedBatchClass().addBatchClassField(bcFieldDTO);
				controller.setBatchClassDirtyFlg(true);
			}
		}
	}

	/**
	 * Method to create the new batch class field dto.
	 * 
	 * @return the batch class field dto {@link BatchClassFieldDTO}
	 */
	private BatchClassFieldDTO createNewBatchClassField() {
		final BatchClassFieldDTO batchClassFieldDTO = new BatchClassFieldDTO();
		batchClassFieldDTO.setNew(true);
		batchClassFieldDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		batchClassFieldDTO.setBatchClass(controller.getSelectedBatchClass());
		batchClassFieldDTO.setDataType(DataType.STRING);
		if (controller.getSelectedBatchClass() != null) {
			// setting the field order
			final Collection<BatchClassFieldDTO> bcflist = controller.getSelectedBatchClass().getBatchClassField();
			if (!CollectionUtil.isEmpty(bcflist)) {
				final ArrayList<Integer> batchClassFieldOrderList = new ArrayList<Integer>();
				for (final BatchClassFieldDTO batchClassFieldDto : bcflist) {
					batchClassFieldOrderList.add(batchClassFieldDto.getFieldOrderNumber());
				}
				final Integer maxFieldOrder = Collections.max(batchClassFieldOrderList);
				if (maxFieldOrder > (Integer.MAX_VALUE - BatchClassConstants.FIELD_ORDER_DIFFERENCE)) {
					// setting the field order to be empty if generated field
					// order is not in integer range
					// batchClassFieldDTO.setFieldOrderNumber(BatchClassConstants.EMPTY_STRING);
					batchClassFieldDTO.setFieldOrderNumber(null);
				} else {
					final Integer newFieldOrder = Collections.max(batchClassFieldOrderList)
							+ BatchClassConstants.FIELD_ORDER_DIFFERENCE;
					batchClassFieldDTO.setFieldOrderNumber(newFieldOrder);
				}
			} else {
				batchClassFieldDTO.setFieldOrderNumber(BatchClassConstants.INITIAL_FIELD_ORDER_NUMBER);
			}
		}
		return batchClassFieldDTO;
	}

	/**
	 * Method to set the batch class dirty on change.
	 * 
	 */
	public void setBatchClassDirtyOnChange() {
		controller.setBatchClassDirtyFlg(true);
		;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isValid() {
		return view.isGridValidated();
	}

}
