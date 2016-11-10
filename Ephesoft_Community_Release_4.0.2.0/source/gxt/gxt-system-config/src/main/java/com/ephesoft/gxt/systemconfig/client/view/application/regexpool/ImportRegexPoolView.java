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

package com.ephesoft.gxt.systemconfig.client.view.application.regexpool;

import java.util.ArrayList;
import java.util.List;

import org.moxieapps.gwt.uploader.client.File;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadErrorEvent;
import org.moxieapps.gwt.uploader.client.events.UploadErrorHandler;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessEvent;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessHandler;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.MultiFileUploader;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.ui.widget.window.MessageDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.ImportPoolDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.application.regexpool.ImportRegexPoolPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.ephesoft.gxt.systemconfig.shared.SystemConfigSharedConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class ImportRegexPoolView extends SystemConfigInlineView<ImportRegexPoolPresenter> {

	interface Binder extends UiBinder<Widget, ImportRegexPoolView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected MultiFileUploader fileUploader;

	private ImportPoolDTO importPoolDTO;
	private List<String> filesNotSupportedList = new ArrayList<String>();

	private List<ImportPoolDTO> filesToBeUploaded = new ArrayList<ImportPoolDTO>();

	/**
	 * ATTACH_FORM_ACTION String.
	 */
	private static final String ATTACH_FORM_ACTION = "/dcma/gxt-system-config/importPool?";

	protected FileQueuedHandler fileQueuedHandler;

	protected FileDialogCompleteHandler fileDialogCompleteHandler;

	protected UploadSuccessHandler uploadSuccessHandler;

	protected UploadErrorHandler uploadErrorHandler;

	private boolean containZipFile;

	public ImportRegexPoolView() {
		super();
		initWidget(binder.createAndBindUi(this));
		fileUploader.setShowProgress(false);
		initialiseUploader();
		// fileUploader.setButtonText("Click here to import regex pool.");
		// fileUploader.setDragAndDropLabelText("Drag and Drop regex pool here.");
		fileUploader.addFileQueuedHandler(fileQueuedHandler);
		fileUploader.addFileDialogCompleteHandler(fileDialogCompleteHandler);
		fileUploader.addUploadSuccessHandler(uploadSuccessHandler);
		fileUploader.addUploadErrorHandler(uploadErrorHandler);
	}

	private void initialiseUploader() {
		fileQueuedHandler = new FileQueuedHandler() {

			@Override
			public boolean onFileQueued(FileQueuedEvent fileQueuedEvent) {
				boolean validFile = false;
				File droppedFile = fileQueuedEvent.getFile();
				String fileName = droppedFile.getName();
				final String fileExtension = fileName.substring(fileName.lastIndexOf(SystemConfigConstants.EXT_SEPARATOR) + 1);
				if (fileExtension.equalsIgnoreCase(SystemConfigConstants.ZIP)) {
					containZipFile = true;
					final String lastAttachedZipSourcePath = SystemConfigConstants.LAST_ATTACHED_ZIP_SOURCE_PATH;
					fileUploader.setUploadURL(StringUtil.concatenate(ATTACH_FORM_ACTION, lastAttachedZipSourcePath));
					validFile = true;
				} else {
					validFile = false;
					fileUploader.cancelUpload(droppedFile.getId(), false);
					filesNotSupportedList.add(fileName);
				}
				return validFile;
			}
		};
		fileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				if (filesNotSupportedList.size() != 0 && !containZipFile) {
					filesNotSupportedAction(filesNotSupportedList);
				}
				if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
					if (fileUploader.getUploadsInProgress() <= 0) {
						ScreenMaskUtility.maskScreen();
						fileUploader.startUpload();
					}
				}
				return true;
			}
		};

		uploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				importPoolDTO = new ImportPoolDTO();
				importPoolDTO.setZipFileName(uploadSuccessEvent.getFile().getName());
				String result = uploadSuccessEvent.getServerData();
				if (result.toLowerCase().indexOf(SystemConfigConstants.ERROR_CODE_TEXT) > -1) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.IMPORT_UNSUCCESSFUL), DialogIcon.ERROR);
					return false;
				}
				final String zipSourcePath = result.substring(result.indexOf(SystemConfigSharedConstants.FILE_PATH)
						+ SystemConfigSharedConstants.FILE_PATH.length(),
						result.indexOf(SystemConfigConstants.PIPE, result.indexOf(SystemConfigSharedConstants.FILE_PATH)));
				importPoolDTO.setZipFileLocation(zipSourcePath);
				filesToBeUploaded.add(importPoolDTO);
				if (fileUploader.getFilesQueued() == 1) {
					containZipFile = false;
					ScreenMaskUtility.unmaskScreen();
					presenter.onRegexUploadComplete(filesToBeUploaded);
				}
				return true;
			}
		};
		uploadErrorHandler = new UploadErrorHandler() {

			@Override
			public boolean onUploadError(UploadErrorEvent uploadErrorEvent) {
				filesNotSupportedList.add(uploadErrorEvent.getFile().getName());
				if (fileUploader.getFilesQueued() == 1) {
					ScreenMaskUtility.unmaskScreen();
					presenter.onRegexUploadComplete(filesToBeUploaded);
				}
				return true;
			}
		};

	}

	public void filesNotSupportedAction(List<String> filesNotSupported) {
		String filesNotImported = SystemConfigConstants.ORDERED_LIST_START_TAG;
		if (!CollectionUtil.isEmpty(filesNotSupported)) {
			for (String unsupportedFileName : filesNotSupported) {
				filesNotImported = StringUtil.concatenate(filesNotImported, SystemConfigConstants.LIST_START_TAG, unsupportedFileName,
						SystemConfigConstants.LIST_END_TAG);
			}
			final String regexUploadErrorMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.REGEX_UPLOAD_ERROR_MESSAGE,
					filesNotSupported.size());
			// final String message = StringUtil.concatenate("Unable to upload ", filesNotSupported.size(),
			// " file(s). Unsupported format.");
			final MessageDialog dialog = new MessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					regexUploadErrorMessage, DialogIcon.ERROR);
			final String filesList = StringUtil.concatenate(filesNotImported, SystemConfigConstants.ORDERED_LIST_END_TAG);

			dialog.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.YES);
			TextButton showFileList = dialog.getButton(PredefinedButton.YES);
			showFileList.setText("Show File List");

			showFileList.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.INFO_TITLE), filesList);
				}
			});

			dialog.addButton(showFileList);
			dialog.show();
			dialog.addDialogHideHandler(new DialogHideHandler() {

				@Override
				public void onDialogHide(DialogHideEvent event) {
					filesNotSupportedList.clear();
				}
			});
		} else {
			Message.display(LocaleDictionary.getConstantValue(SystemConfigConstants.SUCCESS_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.REGEX_POOL_IMPORT_SUCCESSFUL));
		}
	}

	public List<ImportPoolDTO> getFilesToBeUploaded() {
		return filesToBeUploaded;
	}

	public List<String> getFilesNotSupportedList() {
		return filesNotSupportedList;
	}

	public void clearUploadedFilesList() {
		this.filesToBeUploaded.clear();
	}
}
