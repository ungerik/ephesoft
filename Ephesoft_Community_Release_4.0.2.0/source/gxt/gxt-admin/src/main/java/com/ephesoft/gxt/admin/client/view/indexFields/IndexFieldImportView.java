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

package com.ephesoft.gxt.admin.client.view.indexFields;

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
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.indexFields.ImportIndexFieldPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.MultiFileUploader;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class IndexFieldImportView extends BatchClassInlineView<ImportIndexFieldPresenter> {

	interface Binder extends UiBinder<Widget, IndexFieldImportView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	protected static String attachedFileName = null;

	private boolean isValid = true;

	@UiField
	MultiFileUploader importIndexFieldUploader;

	private static final String BATCH_FORM_ACTION = "/dcma/gxt-admin/importIndexField?";

	public IndexFieldImportView() {
		super();
		initWidget(binder.createAndBindUi(this));
		initialiseUploader();

	}

	private void initialiseUploader() {
		FileQueuedHandler fileQueuedHandler = new FileQueuedHandler() {

			public boolean onFileQueued(final FileQueuedEvent fileQueuedEvent) {
				boolean isFileValid = false;
				final File newDroppedFile = fileQueuedEvent.getFile();
				if (FileType.isValidZipFileName(newDroppedFile.getName().toLowerCase())) {
					final String param = StringUtil.concatenate("importIndexFieldLocation=", fileQueuedEvent.getFile().getName());
					importIndexFieldUploader.setUploadURL(StringUtil.concatenate(BATCH_FORM_ACTION, param));
					isFileValid = true;
				} else {
					if (isValid()) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.VALID_INPUT_FILE_FORMATS_IS_ZIP_DISCARDING_INVALID_FILES), DialogIcon.ERROR);
						setValid(false);
						isFileValid = false;
					}
					importIndexFieldUploader.cancelUpload(fileQueuedEvent.getFile().getId(), false);
				}
				return isFileValid;
			}
		};
		importIndexFieldUploader.addFileQueuedHandler(fileQueuedHandler);

		UploadCompleteHandler uploadCompleteHandler = new UploadCompleteHandler() {

			@Override
			public boolean onUploadComplete(UploadCompleteEvent uploadCompleteEvent) {
				ScreenMaskUtility.unmaskScreen();
				return true;
			}
		};
		importIndexFieldUploader.addUploadCompleteHandler(uploadCompleteHandler);

		UploadSuccessHandler uploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				ScreenMaskUtility.unmaskScreen();
				String tempZipFileLocation = uploadSuccessEvent.getServerData();
				presenter.importIndexField(tempZipFileLocation);
				return true;
			}
		};

		importIndexFieldUploader.addUploadSuccessHandler(uploadSuccessHandler);

		FileDialogCompleteHandler fileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				if (fileDialogCompleteEvent.getNumberOfFilesSelected() > 1 && isValid()) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.MULTIPLE_FILE_UPLOAD_NOT_SUPPORTED),
							DialogIcon.WARNING);
				} else if (fileDialogCompleteEvent.getNumberOfFilesSelected() == 1 && isValid()) {
					ScreenMaskUtility.maskScreen();
					importIndexFieldUploader.startUpload();
					return true;
				}
				setValid(true);
				return false;
			}
		};
		importIndexFieldUploader.addFileDialogCompleteHandler(fileDialogCompleteHandler);
		
	}

	@Override
	public void initialize() {
	}

	@Override
	public void refresh() {
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
