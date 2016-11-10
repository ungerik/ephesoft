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

package com.ephesoft.gxt.foldermanager.client.view;

import org.moxieapps.gwt.uploader.client.File;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadErrorEvent;
import org.moxieapps.gwt.uploader.client.events.UploadErrorHandler;
import org.moxieapps.gwt.uploader.client.events.UploadProgressEvent;
import org.moxieapps.gwt.uploader.client.events.UploadProgressHandler;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessEvent;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessHandler;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.MultiFileUploader;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementConstants;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementMessages;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementUploadPresenter;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementUploadedFilesPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

public class FolderManagementUploadView extends View<FolderManagementUploadPresenter> {

	@UiField
	protected MultiFileUploader fileUploader;

	interface Binder extends UiBinder<Widget, FolderManagementUploadView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	protected UploadSuccessHandler uploadSuccessHandler;

	protected UploadErrorHandler uploadErrorHandler;

	protected FileQueuedHandler fileQueuedHandler;

	protected UploadProgressHandler uploadProgressHandler;

	protected FileDialogCompleteHandler fileDialogCompleteHandler;

	final String uploadFormAction = "filesUploadDownload";

	private String folderPath;

	public FolderManagementUploadView() {
		super();
		initWidget(binder.createAndBindUi(this));
		fileUploader.setShowProgress(false);
		initialiseUploader();
		fileUploader.addFileQueuedHandler(fileQueuedHandler);
		fileUploader.addFileDialogCompleteHandler(fileDialogCompleteHandler);
		fileUploader.addUploadProgressHandler(uploadProgressHandler);
		fileUploader.addUploadSuccessHandler(uploadSuccessHandler);
		fileUploader.addUploadErrorHandler(uploadErrorHandler);
	}

	private void initialiseUploader() {
		fileQueuedHandler = new FileQueuedHandler() {

			@Override
			public boolean onFileQueued(FileQueuedEvent fileQueuedEvent) {
				String uploadURL = getUploadURL(uploadFormAction);
				File newFile = fileQueuedEvent.getFile();
				fileUploader.setUploadURL(uploadURL);
				boolean validFile = getUploadedFilesPresenter().addNewFileIntoGrid(newFile);
				return validFile;
			}
		};
		uploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				if (fileUploader.getFilesQueued() == 1) {
					clearBottomPanel();
					presenter.addDragnDropView();
					presenter.addNewFileIntoMainGrid();
					ScreenMaskUtility.unmaskScreen();
					getUploadedFilesPresenter().clearUploadedFilesList();
				}
				return true;
			}
		};
		uploadProgressHandler = new UploadProgressHandler() {

			@Override
			public boolean onUploadProgress(UploadProgressEvent uploadProgressEvent) {
				final String uploadingFileId = uploadProgressEvent.getFile().getId();
				final double uploadedPercentage = uploadProgressEvent.getFile().getPercentUploaded() / 100;
				getUploadedFilesPresenter().updateFileUploadProgress(uploadingFileId, uploadedPercentage);
				return true;
			}
		};
		fileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
					clearBottomPanel();
					presenter.addUploadGridView();
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

		uploadErrorHandler = new UploadErrorHandler() {

			@Override
			public boolean onUploadError(UploadErrorEvent uploadErrorEvent) {
				String fileName = uploadErrorEvent.getFile().getName();
				String errorMessage = LocaleDictionary.getMessageValue(FolderManagementMessages.UPLOAD_ERROR_MESSAGE, fileName);
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE), errorMessage,
						DialogIcon.ERROR);
				if (fileUploader.getFilesQueued() == 1) {
					clearBottomPanel();
					presenter.addDragnDropView();
					presenter.addNewFileIntoMainGrid();
					ScreenMaskUtility.unmaskScreen();
					getUploadedFilesPresenter().clearUploadedFilesList();
				}
				return true;
			}
		};

	}

	private String getUploadURL(String uploadFormAction) {
		StringBuffer urlBuffer = new StringBuffer(uploadFormAction);
		if (Window.Location.getHref().contains(FolderManagementConstants.QUESTION_MARK)) {
			urlBuffer.append(FolderManagementConstants.AMPERSAND);
		} else {
			urlBuffer.append(FolderManagementConstants.QUESTION_MARK);
		}
		urlBuffer.append(FolderManagementConstants.CURRENT_UPLOAD_FOLDER_NAME);
		urlBuffer.append(FolderManagementConstants.EQUALS);
		this.folderPath = presenter.getFolderPath();
		urlBuffer.append(folderPath);
		return urlBuffer.toString();
	}

	@Override
	public void initialize() {
	}

	private void clearBottomPanel() {
		presenter.clearBottomPanel();
	}

	private FolderManagementUploadedFilesPresenter getUploadedFilesPresenter() {
		return presenter.getUploadedFilesPresenter();
	}
}
