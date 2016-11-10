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

package com.ephesoft.gxt.admin.client.view.batchclass;

import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.UploadErrorHandler;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessEvent;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessHandler;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.BatchClassImportStartEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.batchclass.BatchClassImportPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.shared.constant.AdminSharedConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.MultiFileUploader;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.ImportBatchClassUserOptionDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class BatchClassImportView extends BatchClassInlineView<BatchClassImportPresenter> {

	final ImportBatchClassUserOptionDTO importBatchClassUserOptionDTO = new ImportBatchClassUserOptionDTO();

	interface Binder extends UiBinder<Widget, BatchClassImportView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected MultiFileUploader importBatchClassUploader;

	protected static String attachedFileName = null;

	DialogWindow dialogWindow;

	private String result = null;

	ImportBatchClassDialogWindowView importBatchClassView;

	private static final int IMPORT_BATCH_CLASS_VIEW_WIDTH = 434;

	private static final int IMPORT_BATCH_CLASS_VIEW_HEIGHT = 350;

	protected FileQueuedHandler fileQueuedHandler;

	protected UploadCompleteHandler uploadCompleteHandler;

	protected UploadSuccessHandler uploadSuccessHandler;

	protected UploadErrorHandler uploadErrorHandler;

	protected FileDialogCompleteHandler fileDialogCompleteHandler;

	private boolean isValid = true;

	public ImportBatchClassDialogWindowView getImportBatchClassView() {
		return importBatchClassView;
	}

	private static final String BATCH_FORM_ACTION = "/dcma/gxt-admin/importBatchClassUpload?";

	public BatchClassImportView() {
		super();
		// dialogWindow = new DialogWindow();
		dialogWindow = new DialogWindow(true, null);

		initWidget(binder.createAndBindUi(this));
		importBatchClassUploader.setUploaderID("batchClassImportId");
		importBatchClassUploader.setShowProgress(true);
		initialiseFileUploadContainter();
		importBatchClassUserOptionDTO.setUseSource(Boolean.TRUE);
		importBatchClassUserOptionDTO.setImportExisting(Boolean.FALSE);
		importBatchClassView = new ImportBatchClassDialogWindowView();
		importBatchClassUploader.addFileQueuedHandler(fileQueuedHandler);
		importBatchClassUploader.addUploadCompleteHandler(uploadCompleteHandler);
		importBatchClassUploader.addUploadSuccessHandler(uploadSuccessHandler);
		importBatchClassUploader.addUploadErrorHandler(uploadErrorHandler);
		importBatchClassUploader.addFileDialogCompleteHandler(fileDialogCompleteHandler);
		dialogWindow.setDialogListener(new DialogAdapter() {

			@Override
			public void onOkClick() {
				BatchClassManagementEventBus.fireEvent(new BatchClassImportStartEvent(importBatchClassUserOptionDTO));
			}

			@Override
			public void onCloseClick() {
				dialogWindow.hide();
			}

			@Override
			public void onCancelClick() {
				dialogWindow.hide();

			}
		});
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
	}

	@Override
	public void refresh() {
	}

	public void hideImportView() {
		if (null != dialogWindow) {
			dialogWindow.hide();
		}
	}

	private void initialiseFileUploadContainter() {
		fileQueuedHandler = new FileQueuedHandler() {

			public boolean onFileQueued(final FileQueuedEvent fileQueuedEvent) {
				boolean isFileValid = false;
				final String fileName = fileQueuedEvent.getFile().getName();
				final String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
				if (fileExtension.equalsIgnoreCase("zip")) {
					String lastAttachedZipSourcePath = "lastAttachedZipSourcePath=" + fileQueuedEvent.getFile().getName();
					importBatchClassUploader.setUploadURL(BATCH_FORM_ACTION + lastAttachedZipSourcePath);
					isFileValid = true;
				} else {
					if (isValid()) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary
										.getMessageValue(BatchClassMessages.VALID_INPUT_FILE_FORMATS_IS_ZIP_DISCARDING_INVALID_FILES),
								DialogIcon.ERROR);
						setValid(false);
						isFileValid = false;
					}
					importBatchClassUploader.cancelUpload(fileQueuedEvent.getFile().getId(), false);
				}

				return isFileValid;
			}

		};
		fileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				if (fileDialogCompleteEvent.getNumberOfFilesSelected() > 1 && isValid()) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.MULTIPLE_FILE_UPLOAD_NOT_SUPPORTED),
							DialogIcon.WARNING);
				} else if (fileDialogCompleteEvent.getNumberOfFilesSelected() == 1 && isValid()) {
					importBatchClassUploader.startUpload();
					return true;
				}
				setValid(true);
				return false;
			}
		};
		uploadCompleteHandler = new UploadCompleteHandler() {

			@Override
			public boolean onUploadComplete(UploadCompleteEvent uploadCompleteEvent) {
				String fileName = uploadCompleteEvent.getFile().getName();
				final String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
				if (fileExtension.equalsIgnoreCase("zip")) {
					if (importBatchClassUserOptionDTO.getName().isEmpty() || importBatchClassUserOptionDTO.getName() == null) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_IMPORT_BATCH_CLASS), DialogIcon.ERROR);
					} else {
						importBatchClassView.setBatchClassName(importBatchClassUserOptionDTO.getName());
						importBatchClassView.setBatchClassDescription(importBatchClassUserOptionDTO.getDescription());
						importBatchClassView.setPriority(importBatchClassUserOptionDTO.getPriority());

						importBatchClassView.setprioritySliderValue(importBatchClassView.getPrioritySliderString());

						dialogWindow.setHeadingText(LocaleDictionary.getConstantValue(BatchClassConstants.IMPORT_BATCH_CLASS));
						dialogWindow.add(importBatchClassView);

						// Set Focus Widget
						dialogWindow.setFocusWidget(importBatchClassView.batchClassNameTextField);

						dialogWindow.setHeight(IMPORT_BATCH_CLASS_VIEW_HEIGHT);
						dialogWindow.setWidth(IMPORT_BATCH_CLASS_VIEW_WIDTH);
						dialogWindow.setPosition((Window.getClientWidth() - IMPORT_BATCH_CLASS_VIEW_WIDTH) / 2,
								(Window.getClientHeight() - IMPORT_BATCH_CLASS_VIEW_HEIGHT) / 2);
						dialogWindow.setPreferCookieDimension(false);
						dialogWindow.show();
					}
				}
				return true;
			}
		};
		uploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				result = uploadSuccessEvent.getServerData();
				String workFlowName = result.substring(result.indexOf(AdminSharedConstants.WORK_FLOW_NAME)
						+ AdminSharedConstants.WORK_FLOW_NAME.length(),
						result.indexOf('|', result.indexOf(AdminSharedConstants.WORK_FLOW_NAME)));

				String workflowDescription = result.substring(result.indexOf(AdminSharedConstants.WORK_FLOW_DESC)
						+ AdminSharedConstants.WORK_FLOW_DESC.length(),
						result.indexOf('|', result.indexOf(AdminSharedConstants.WORK_FLOW_DESC)));
				String workflowPriority = result.substring(result.indexOf(AdminSharedConstants.WORK_FLOW_PRIORITY)
						+ AdminSharedConstants.WORK_FLOW_PRIORITY.length(),
						result.indexOf('|', result.indexOf(AdminSharedConstants.WORK_FLOW_PRIORITY)));

				String zipSourcePath = result
						.substring(result.indexOf(AdminSharedConstants.FILE_PATH) + AdminSharedConstants.FILE_PATH.length(),
								result.indexOf('|', result.indexOf(AdminSharedConstants.FILE_PATH)));
				String workflowDeployed = result.substring(result.indexOf(AdminSharedConstants.WORKFLOW_DEPLOYED)
						+ AdminSharedConstants.WORKFLOW_DEPLOYED.length(),
						result.indexOf('|', result.indexOf(AdminSharedConstants.WORKFLOW_DEPLOYED)));
				String workflowEqual = result.substring(result.indexOf(AdminSharedConstants.WORKFLOW_EQUAL)
						+ AdminSharedConstants.WORKFLOW_EQUAL.length(),
						result.indexOf('|', result.indexOf(AdminSharedConstants.WORKFLOW_EQUAL)));
				String workflowExistInBatchClass = result.substring(result.indexOf(AdminSharedConstants.WORKFLOW_EXIST_IN_BATCH_CLASS)
						+ AdminSharedConstants.WORKFLOW_EXIST_IN_BATCH_CLASS.length(),
						result.indexOf('|', result.indexOf(AdminSharedConstants.WORKFLOW_EXIST_IN_BATCH_CLASS)));

				importBatchClassUserOptionDTO.setName(workFlowName);
				importBatchClassUserOptionDTO.setDescription(workflowDescription);
				importBatchClassUserOptionDTO.setPriority(Integer.valueOf(workflowPriority));
				importBatchClassUserOptionDTO.setZipFileName(zipSourcePath);
				importBatchClassUserOptionDTO.setWorkflowDeployed(Boolean.valueOf(workflowDeployed));
				importBatchClassUserOptionDTO.setWorkflowEqual(Boolean.valueOf(workflowEqual));
				importBatchClassUserOptionDTO.setWorkflowExistsInBatchClass(Boolean.valueOf(workflowExistInBatchClass));

				return true;
			}
		};
	}

	/**
	 * Checks if files are valid.
	 * 
	 * @return true, if files are valid
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * Sets the valid.
	 * 
	 * @param isValid the new valid
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}
