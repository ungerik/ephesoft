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

package com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.moxieapps.gwt.uploader.client.File;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessEvent;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessHandler;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionImportPresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.MultiFileUploader;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.UploadBatchDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class AdvancedKVExtractionImportView extends AdvancedKVExtractionInlineView<AdvancedKVExtractionImportPresenter> {

	interface Binder extends UiBinder<Widget, AdvancedKVExtractionImportView> {
	}

	private static final Binder binder = GWT.create(Binder.class);
	@UiField
	MultiFileUploader fileUploader;

	final Map<String, UploadBatchDTO> uploadingFilesMap = new LinkedHashMap<String, UploadBatchDTO>();

	private static final String UPLOAD_FORM_ACTION = "/dcma/uploadImageFile?";
	private int filesCount = 0;

	private List<String> invalidFiles = new LinkedList<String>();

	/**
	 * Constructor.
	 */
	public AdvancedKVExtractionImportView() {
		super();
		initWidget(binder.createAndBindUi(this));
		setWidgetIDs();
	}

	/**
	 * Initializes and sets properties of uploader.
	 */
	public void initializeUploader() {
		FileQueuedHandler fileQueuedHandler = new FileQueuedHandler() {

			public boolean onFileQueued(final FileQueuedEvent fileQueuedEvent) {
				final File newDroppedFile = fileQueuedEvent.getFile();
				if (FileType.isValidFileName(newDroppedFile.getName().toLowerCase())) {
					filesCount++;
					final String param = StringUtil.concatenate("batchClassId=", presenter.getController().getSelectedBatchClass()
							.getIdentifier(), "&", "docName=", presenter.getController().getSelectedDocumentType().getName());
					fileUploader.setUploadURL(StringUtil.concatenate(UPLOAD_FORM_ACTION, param));
					return true;
				} else {
					invalidFiles.add(newDroppedFile.getName());
					fileUploader.cancelUpload(fileQueuedEvent.getFile().getId(), false);
				}
				return false;
			}
		};
		fileUploader.addFileQueuedHandler(fileQueuedHandler);

		UploadCompleteHandler uploadCompleteHandler = new UploadCompleteHandler() {

			@Override
			public boolean onUploadComplete(UploadCompleteEvent uploadCompleteEvent) {
				filesCount--;
				if (filesCount == 0) {
					ScreenMaskUtility.unmaskScreen();
					presenter.loadNewImages();
				}
				return true;
			}
		};
		fileUploader.addUploadCompleteHandler(uploadCompleteHandler);

		UploadSuccessHandler uploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				
				String str1 = uploadSuccessEvent.getServerData();
				String[] array = str1.split(AdminConstants.PATTERN_DELIMITER);
				boolean fileAdded = presenter.addNewFileIntoList(array[0]);
//				if (filesCount == 0) {
//					presenter.loadNewImages();
//				}
				return fileAdded;
			}
		};

		fileUploader.addUploadSuccessHandler(uploadSuccessHandler);

		FileDialogCompleteHandler fileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
					if (fileUploader.getUploadsInProgress() <= 0) {
						ScreenMaskUtility.maskScreen();
						fileUploader.startUpload();
					}
				}
				if (!CollectionUtil.isEmpty(getInvalidFiles())) {
					ScreenMaskUtility.unmaskScreen();
					String message = LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_INPUT_FILE);
					for (String fileName : getInvalidFiles()) {
						message = StringUtil.concatenate(message, "<br>", fileName);
					}
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), message,
							DialogIcon.ERROR);
					getInvalidFiles().clear();
					return false;
				}
				return true;
			}
		};
		fileUploader.addFileDialogCompleteHandler(fileDialogCompleteHandler);
	}

	/**
	 * Setting IDs of widgets.
	 */
	private void setWidgetIDs() {
		WidgetUtil.setID(fileUploader, "advKVImportVPanel");
	}

	@Override
	public void initialize() {

	}

	public List<String> getInvalidFiles() {
		return invalidFiles;
	}

	public void setInvalidFiles(List<String> invalidFiles) {
		this.invalidFiles = invalidFiles;
	}

}
