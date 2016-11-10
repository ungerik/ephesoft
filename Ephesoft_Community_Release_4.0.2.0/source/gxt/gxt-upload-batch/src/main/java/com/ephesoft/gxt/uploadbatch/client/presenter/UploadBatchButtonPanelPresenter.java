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

package com.ephesoft.gxt.uploadbatch.client.presenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogListener;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.BatchClassFieldDTO;
import com.ephesoft.gxt.core.shared.dto.UploadBatchDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.uploadbatch.client.controller.UploadBatchController;
import com.ephesoft.gxt.uploadbatch.client.event.ReloadUploadedFilesGridEvent;
import com.ephesoft.gxt.uploadbatch.client.event.ShowAssociatedBCFViewEvent;
import com.ephesoft.gxt.uploadbatch.client.event.StartBatchEvent;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchMessages;
import com.ephesoft.gxt.uploadbatch.client.view.UploadBatchButtonPanelView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;

public class UploadBatchButtonPanelPresenter extends BasePresenter<UploadBatchController, UploadBatchButtonPanelView> {

	interface CustomEventBinder extends EventBinder<UploadBatchButtonPanelPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * The userType {@link Integer} is used for storing user type.
	 */
	private Integer userType;
	
	private boolean sortOnBatchClassID = false;

	/**
	 * Constructor.
	 * 
	 * @param controller {@link UploadBatchController}
	 * @param view {@link UploadBatchButtonPanelView}
	 */
	public UploadBatchButtonPanelPresenter(final UploadBatchController controller, final UploadBatchButtonPanelView view) {
		super(controller, view);
		setUserType();
		getBatchClassName(sortOnBatchClassID);
		getCurrentBatchUploadFolder();
	}

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);

	}

	/**
	 * Populates batch class combobox with list of values.
	 * 
	 * @param batchClassMap {@link Map}<{@link String},{@link String}>
	 */
	private void populateBatchClassComboBox(final Map<String, String> batchClassMap) {
		if (null != batchClassMap && !batchClassMap.isEmpty()) {
			final List<String> listItems = new ArrayList<String>();
			for (final String currentValue : batchClassMap.keySet()) {
				listItems.add(currentValue);
			}
			view.setBatchClassComboBoxValues(listItems);
		}
	}

	/**
	 * The <code>setUserType</code> method is used for setting current user type.
	 */
	private void setUserType() {
		ScreenMaskUtility.maskScreen(LocaleDictionary.getConstantValue(CoreCommonConstants.PLEASE_WAIT));
		controller.getRpcService().getUserType(new AsyncCallback<Integer>() {

			@Override
			public void onFailure(final Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				// Do nothing
			}

			@Override
			public void onSuccess(final Integer userType) {
				ScreenMaskUtility.unmaskScreen();
				if (null != userType) {
					setUserType(userType);
				}
			}
		});
	}

	/**
	 * Gets current upload batch folder name from the server and sets it into the controller.
	 */
	private void getCurrentBatchUploadFolder() {
		// rpc call for fetching the folder name
		ScreenMaskUtility.maskScreen(LocaleDictionary.getConstantValue(CoreCommonConstants.PLEASE_WAIT));
		controller.getRpcService().getCurrentBatchFolderName(new AsyncCallback<String>() {

			@Override
			public void onSuccess(final String currentBatchUploadFolder) {
				ScreenMaskUtility.unmaskScreen();
				controller.setCurrentBatchUploadFolder(currentBatchUploadFolder);
				generateBatchName();
			}

			@Override
			public void onFailure(final Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(UploadBatchMessages.UNABLE_TO_FETCH_CURRENT_UPLOAD_FOLDER_NAME), DialogIcon.ERROR);
			}
		});
	}

	/**
	 * Gets the batch class map containing id along with description and sets it into the combobox.
	 * 
	 * @param onBasisOfSorting
	 */
	public void getBatchClassName(final boolean onBasisOfSorting) {
		ScreenMaskUtility.maskScreen(LocaleDictionary.getConstantValue(CoreCommonConstants.PLEASE_WAIT));
		controller.getRpcService().getBatchClassName(onBasisOfSorting, new AsyncCallback<Map<String, String>>() {

			@Override
			public void onFailure(final Throwable throwable) {
				ScreenMaskUtility.unmaskScreen();
				DialogUtil.showMessageDialog(/*
											 * LocaleDictionary.get().getMessageValue(
											 * UploadBatchMessages.ERROR_GETTING_BATCH_CLASS_LIST)
											 */LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE), LocaleDictionary.getConstantValue(UploadBatchMessages.UNABLE_TO_GET_BATCH_CLASS_LIST), DialogIcon.ERROR);
			}

			@Override
			public void onSuccess(final Map<String, String> batchClassMap) {
				ScreenMaskUtility.unmaskScreen();
				if (null != batchClassMap) {
					view.setBatchClassMapping(batchClassMap);
					populateBatchClassComboBox(batchClassMap);
				}
			}

		});
	}

	/**
	 * Deletes selected uploaded files from the grid.
	 */
	public void onDeleteFilesClicked() {

		final String currentBatchUploadFolder = controller.getCurrentBatchUploadFolder();
		final List<UploadBatchDTO> selectedFile = controller.getSelectedFilesFromGrid();
		if (selectedFile.isEmpty()) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(UploadBatchMessages.NO_FILES_SELECTED_TO_DELETE), DialogIcon.ERROR);
		} else {
			ConfirmationDialog confirmation = DialogUtil.showConfirmationDialog(
					LocaleDictionary.getConstantValue(UploadBatchConstants.CONFIRMATION),
					LocaleDictionary.getMessageValue(UploadBatchMessages.DELETE_FILES_CONFIRMATION));
			confirmation.setDialogListener(new DialogAdapter() {
				
				@Override
				public void onOkClick() {
					deleteSelectedFiles(currentBatchUploadFolder, selectedFile);
				}
				
				@Override
				public void onCloseClick() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onCancelClick() {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}
	
	private void deleteSelectedFiles(final String currentBatchUploadFolder, final List<UploadBatchDTO> selectedFile) {
		ScreenMaskUtility.maskScreen(LocaleDictionary.getConstantValue(CoreCommonConstants.PLEASE_WAIT));
		controller.getRpcService().deleteSelectedFiles(currentBatchUploadFolder, selectedFile, new AsyncCallback<Void>() {

			@Override
			public void onFailure(final Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(UploadBatchMessages.UNABLE_TO_DELETE_TRY_AGAIN), DialogIcon.ERROR);

			}

			@Override
			public void onSuccess(final Void result) {
				ScreenMaskUtility.unmaskScreen();
				if (!controller.getUploadedFileList().isEmpty()) {
					controller.getUploadedFileList().removeAll(selectedFile);
				}
				controller.getEventBus().fireEvent(new ReloadUploadedFilesGridEvent());

			}

		});
	}

	/**
	 * Deletes all files from the grid.
	 */
	public void deleteAllFiles() {

		if (controller.getUploadedFileList().isEmpty()) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE), LocaleDictionary.getMessageValue(UploadBatchMessages.NO_FILES_UPLOADED), DialogIcon.ERROR);
		} else {
			final String currentBatchUploadFolder = controller.getCurrentBatchUploadFolder();
			ScreenMaskUtility.maskScreen(LocaleDictionary.getConstantValue(CoreCommonConstants.PLEASE_WAIT));
			controller.getRpcService().deleteAllFiles(currentBatchUploadFolder, new AsyncCallback<String>() {

				@Override
				public void onFailure(final Throwable caught) {
					ScreenMaskUtility.unmaskScreen();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(UploadBatchMessages.UNABLE_TO_DELETE_TRY_AGAIN), DialogIcon.ERROR);

				}

				@Override
				public void onSuccess(final String result) {
					ScreenMaskUtility.unmaskScreen();
					clearAllFields();
				}

			});
		}
	}

	/**
	 * Starts the batch with uploaded files and selected batch class.
	 * 
	 * @param selectedBatchClassId {@link String}
	 * @param batchDescription {@link String}
	 */
	public void startNewBatch(final String selectedBatchClassId, final String batchDescription) {
		if (controller.getUploadedFileList().isEmpty()) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(UploadBatchMessages.NO_FILES_UPLOADED_TO_START_BATCH), DialogIcon.ERROR);
		} else if (!CollectionUtil.isEmpty(controller.getBatchClassFieldDTOs()) && !controller.getMainPresenter().isBCFieldsSerilized()){
			controller.getMainPresenter().associateBCFPresenter.setOnStartBatchClicked(true);
			showFieldsView();
		} else {
			controller.getEventBus().fireEvent(new StartBatchEvent());
		}
	}

	/**
	 * Clears all Input Fields for fresh upload.
	 */
	public void clearAllFields() {
		controller.getUploadedFileList().clear();
		getCurrentBatchUploadFolder();
		final String selectedBatchClassID = view.getSelectedBatchClassComboBoxValue();
		setBatchClassFields(selectedBatchClassID);
		controller.getMainPresenter().setBCFieldsSerialized(false);
		//view.setSelectedBatchClassComboBoxValue(0);
		controller.getEventBus().fireEvent(new ReloadUploadedFilesGridEvent());
	}

	/**
	 * Sets the batch class fields on the basis of selected batch class combo box value.
	 * 
	 * @param selectedBatchClassListBoxValue {@link String}
	 */
	public void batchClassComboBoxValueChanged(final String selectedBatchClassListBoxValue) {
		if (!StringUtil.isNullOrEmpty(selectedBatchClassListBoxValue)) {
			setBatchClassFields(selectedBatchClassListBoxValue);
		}

	}

	/**
	 * Gets the batch class fields on the basis of batch id being passed and sets it into the controller.
	 * 
	 * @param selectedBatchClassListBoxValue {@link String}
	 */
	private void setBatchClassFields(final String selectedBatchClassListBoxValue) {
		ScreenMaskUtility.maskScreen(LocaleDictionary.getConstantValue(CoreCommonConstants.PLEASE_WAIT));
		controller.getRpcService().getBatchClassFieldDTOByBatchClassIdentifier(selectedBatchClassListBoxValue,
				new AsyncCallback<List<BatchClassFieldDTO>>() {

					@Override
					public void onFailure(final Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE),
								StringUtil.concatenate(LocaleDictionary.getMessageValue(UploadBatchMessages.UNABLE_TO_FETCH_BATCH_CLASS_FIELDS_FOR), selectedBatchClassListBoxValue), DialogIcon.ERROR);

					}

					@Override
					public void onSuccess(final List<BatchClassFieldDTO> batchClassFieldList) {
						ScreenMaskUtility.unmaskScreen();
						if (CollectionUtil.isEmpty(batchClassFieldList)) {
							view.enableFieldsButton(false);
							controller.setBatchClassFieldDTOs(null);
						} else {
							controller.setBatchClassFieldDTOs(batchClassFieldList);
							loadAndSortBCFList(batchClassFieldList);
							view.enableFieldsButton(true);
						}

					}

					// Compares and loads the batch class fields DTOs in the controller.
					private void loadAndSortBCFList(final List<BatchClassFieldDTO> batchClassFieldList) {
						List<BatchClassFieldDTO> batchClassFieldDTOs = controller.getBatchClassFieldDTOs();
						if (batchClassFieldList != null && batchClassFieldList.size() > 0) {
							if (batchClassFieldDTOs.size() == batchClassFieldList.size() && batchClassFieldDTOs.size() > 0) {
								if (!(batchClassFieldDTOs.get(0).getBatchClass().getIdentifier().equalsIgnoreCase(batchClassFieldList
										.get(0).getBatchClass().getIdentifier()))) {
									batchClassFieldDTOs = batchClassFieldList;
								}
							} else {
								batchClassFieldDTOs = batchClassFieldList;
							}

							Collections.sort(batchClassFieldList, new Comparator<BatchClassFieldDTO>() {

								@Override
								public int compare(final BatchClassFieldDTO batchClassFieldDTO1,
										final BatchClassFieldDTO batchClassFieldDTO2) {
									int compare = 0;
									if (batchClassFieldDTO1 != null && batchClassFieldDTO2 != null) {
										final int fieldOrderNumberOne = batchClassFieldDTO1.getFieldOrderNumber();
										final int fieldOrderNumberSec = batchClassFieldDTO2.getFieldOrderNumber();
										if (fieldOrderNumberOne > fieldOrderNumberSec) {
											compare = 1;
										} else {
											if (fieldOrderNumberOne < fieldOrderNumberSec) {
												compare = -1;
											} else {
												compare = 0;
											}
										}
									}
									return compare;
								};
							});
						} else {
							batchClassFieldDTOs = batchClassFieldList;
						}

					}

				});

	}

	/**
	 * Fires the event for showing Batch Class Fields View.
	 */
	public void onFieldsButtonClicked() {
		controller.getMainPresenter().associateBCFPresenter.setOnStartBatchClicked(false);
		showFieldsView();
	}
	
	private void showFieldsView() {
		if (!CollectionUtil.isEmpty(controller.getBatchClassFieldDTOs())) {
			controller.getEventBus().fireEvent(new ShowAssociatedBCFViewEvent());
		}
	}

	/**
	 * Getter for user type.
	 * 
	 * @return user type {@link Integer}
	 */
	public Integer getUserType() {
		return userType;
	}

	/**
	 * Setter for user type.
	 * 
	 * @param userType {@link Integer}
	 */
	public void setUserType(final Integer userType) {
		this.userType = userType;
	}
	
	public void generateBatchName() {
		final String selectedBatchClassID = view.getSelectedBatchClassComboBoxValue();

		final String batchName = StringUtil.concatenate(selectedBatchClassID, CoreCommonConstants.UNDERSCORE,
				controller.getCurrentBatchUploadFolder());
		final String batchDescription = view.getBatchDescriptionFromTextBox();
		final String oldBatchName = view.getBatchName();
		if (StringUtil.isNullOrEmpty(batchDescription) || batchDescription.equals(oldBatchName)) {
			view.setBatchDescriptionInTextBox(batchName);
		}
		view.setBatchName(batchName);
	}

	public void onSortButtonClicked() {
		sortOnBatchClassID = !sortOnBatchClassID;
		view.toggleBatchClassSortButtonToolTip(sortOnBatchClassID);
		getBatchClassName(sortOnBatchClassID);
	}
}
