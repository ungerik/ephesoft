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

package com.ephesoft.gxt.uploadbatch.client.view;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.moxieapps.gwt.uploader.client.File;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadErrorEvent;
import org.moxieapps.gwt.uploader.client.events.UploadErrorHandler;
import org.moxieapps.gwt.uploader.client.events.UploadProgressEvent;
import org.moxieapps.gwt.uploader.client.events.UploadProgressHandler;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.MultiFileUploader;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.constant.SupportedUploadFileTypes;
import com.ephesoft.gxt.core.shared.dto.UploadBatchDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchMessages;
import com.ephesoft.gxt.uploadbatch.client.presenter.UploadFilePanelPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 08-Aug-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class UploadFilePanelView extends View<UploadFilePanelPresenter> {

	interface Binder extends UiBinder<Widget, UploadFilePanelView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected MultiFileUploader uploader;

	final Map<String, UploadBatchDTO> uploadingFilesMap = new LinkedHashMap<String, UploadBatchDTO>();

	private List<String> filesNotSupportedList = new ArrayList<String>();

	private static final String UPLOAD_FORM_ACTION = "/dcma/dcma-gwt-upload-batch/uploadBatch?";

	private boolean showErrorPopup = false;

	/**
	 * Constructor.
	 */
	public UploadFilePanelView() {
		super();
		initWidget(binder.createAndBindUi(this));
		initializeUploader();
		setWidgetIDs();
	}

	/**
	 * Initialises and sets properties of uploader.
	 */
	private void initializeUploader() {
		FileQueuedHandler fileQueuedHandler = new FileQueuedHandler() {

			public boolean onFileQueued(final FileQueuedEvent fileQueuedEvent) {
				File uploadedFile = fileQueuedEvent.getFile();
				final String fileName = uploadedFile.getName();
				List<Boolean> result = presenter.isValidFile(fileName);
				boolean validFile = result.get(0);
				boolean officeFile = result.get(1);
				presenter.removeFileWithSameNameFromGrid(fileName);
				if (validFile) {
					if (officeFile) {
						uploader.cancelUpload(uploadedFile.getId(), false);
						showErrorPopup = true;
					} else {
						uploader.setUploadURL(StringUtil.concatenate(UPLOAD_FORM_ACTION,
								UploadBatchConstants.CURRENT_BATCH_UPLOAD_FOLDER_NAME_EQUALS, presenter.getCurrentBatchUploadFolder()));
						presenter.addNewFileIntoGrid(fileQueuedEvent.getFile());
					}
				} else {
					uploader.cancelUpload(uploadedFile.getId(), false);
					filesNotSupportedList.add(fileName);
				}
				return validFile;
			}
		};

		FileDialogCompleteHandler fileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				if (filesNotSupportedList.size() != 0) {
					presenter.filesNotSupportedAction(filesNotSupportedList);
				}

				if (showErrorPopup) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.INFO_TITLE),
							LocaleDictionary.getMessageValue(UploadBatchMessages.OFFICE_FILE_SUPPORT_MESSAGE), DialogIcon.INFO);
					showErrorPopup = false;
				}

				if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
					if (uploader.getStats().getUploadsInProgress() <= 0) {
						uploader.startUpload();
					}
				}

				return true;
			}

		};

		UploadProgressHandler uploadProgressHandler = new UploadProgressHandler() {

			public boolean onUploadProgress(final UploadProgressEvent uploadProgressEvent) {
				final String uploadingFileId = uploadProgressEvent.getFile().getId();
				if (uploadProgressEvent.getBytesTotal() == 0) {
					uploader.cancelUpload(uploadingFileId, true);
				} else {
					final double uploadedPercentage = uploadProgressEvent.getFile().getPercentUploaded() / 100;
					presenter.updateFileUploadProgress(uploadingFileId, uploadedPercentage);
					presenter.updateUploadSpeed(uploadProgressEvent.getFile());
				}
				return true;
			}
		};

		UploadErrorHandler uploadErrorHandler = new UploadErrorHandler() {

			@Override
			public boolean onUploadError(UploadErrorEvent uploadErrorEvent) {
				File errenousFile = uploadErrorEvent.getFile();
				presenter.removeErrenousFileFromGrid(errenousFile.getId());
				return true;
			}
		};
		uploader.addFileQueuedHandler(fileQueuedHandler);
		uploader.addFileDialogCompleteHandler(fileDialogCompleteHandler);
		uploader.addUploadProgressHandler(uploadProgressHandler);
		uploader.addUploadErrorHandler(uploadErrorHandler);
		uploader.setShowProgress(false);

		String supportedFileTypes = CoreCommonConstants.EMPTY_STRING;
		for (SupportedUploadFileTypes fileType : SupportedUploadFileTypes.values()) {
			supportedFileTypes = StringUtil.concatenate(supportedFileTypes, UploadBatchConstants.STAR, UploadBatchConstants.DOT,
					fileType.getExtension(), UploadBatchConstants.SEMICOLON);
		}
		// uploader.setFileTypes(supportedFileTypes);
	}

	/**
	 * Setting IDs of widgets.
	 */
	private void setWidgetIDs() {
	}

	@Override
	public void initialize() {

	}

	public void clearFilesNotSupportedList() {
		this.filesNotSupportedList.clear();
	}
}
