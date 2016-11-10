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

package com.ephesoft.gxt.admin.client.view.tableinfo;

import java.util.ArrayList;
import java.util.List;

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
import com.ephesoft.gxt.admin.client.presenter.tableinfo.TableInfoImportPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.MultiFileUploader;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.ui.widget.window.MessageDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class TableInfoImportView extends BatchClassInlineView<TableInfoImportPresenter> {

	private List<String> filesNotSupportedList = new ArrayList<String>();

	interface Binder extends UiBinder<Widget, TableInfoImportView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected MultiFileUploader importTableInfoImporter;

	DialogWindow dialogWindow;

	private static final String TABLE_FORM_ACTION = "/dcma/gxt-admin/importTableUpload?";

	public TableInfoImportView() {
		super();
		initWidget(binder.createAndBindUi(this));
		dialogWindow = new DialogWindow();
		importTableInfoImporter.addFileQueuedHandler(new FileQueuedHandler() {

			@Override
			public boolean onFileQueued(FileQueuedEvent fileQueuedEvent) {
				String uploadedFileName = fileQueuedEvent.getFile().getName().toLowerCase();
				boolean isFileValid = false;
				if (FileType.isValidFileName(uploadedFileName)) {
					String batchClassIdentifier = presenter.getBatchClassIdentifer();
					importTableInfoImporter.setUploadURL(StringUtil.concatenate(TABLE_FORM_ACTION, "batchClassIdentifier=",
							batchClassIdentifier));
					isFileValid = true;
				} else {
					isFileValid = false;
					filesNotSupportedList.add(uploadedFileName);
					importTableInfoImporter.cancelUpload(fileQueuedEvent.getFile().getId(), false);
				}
				return isFileValid;
			}
		});
		importTableInfoImporter.addFileDialogCompleteHandler(new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				boolean isValidUpload = true;
				if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
					if (importTableInfoImporter.getUploadsInProgress() <= 0) {
						ScreenMaskUtility.maskScreen();
						importTableInfoImporter.startUpload();
					}
				}
				if (filesNotSupportedList.size() != 0) {
					filesNotSupportedAction(filesNotSupportedList);
					isValidUpload = false;
				}
				return isValidUpload;
			}
		});

		UploadCompleteHandler uploadCompleteHandler = new UploadCompleteHandler() {

			@Override
			public boolean onUploadComplete(UploadCompleteEvent uploadCompleteEvent) {
				ScreenMaskUtility.unmaskScreen();
				return true;
			}
		};
		importTableInfoImporter.addUploadCompleteHandler(uploadCompleteHandler);
		importTableInfoImporter.addUploadSuccessHandler(new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				ScreenMaskUtility.unmaskScreen();
				if (importTableInfoImporter.getFilesQueued() == 1) {
					Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS), LocaleDictionary.getMessageValue(BatchClassMessages.FILES_IMPORTED_SUCCESSFULLY));
				}
				return true;
			}
		});
		initialiseFileUploadContainter();
	}

	private void addHideHandler() {
		dialogWindow.addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent event) {
			}
		});
	}

	@Override
	public void initialize() {
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
	}

	private void filesNotSupportedAction(List<String> filesNotSupported) {
		String filesNotImported = "<ol>";
		for (String unsupportedFileName : filesNotSupported) {
			filesNotImported = StringUtil.concatenate(filesNotImported, "<li>", unsupportedFileName, "<//li>");
		}
		final String message = StringUtil.concatenate(LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_UPLOAD), filesNotSupported.size(), LocaleDictionary.getMessageValue(BatchClassMessages.FILES_UNSUPPORTED_FORMAT));
		final MessageDialog dialog = new MessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR), message, DialogIcon.ERROR);
		Button showFileList = new Button(LocaleDictionary.getMessageValue(BatchClassMessages.SHOW_FILE_LIST));
		final String filesList = StringUtil.concatenate(filesNotImported, "<//ol>");
		showFileList.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				DialogUtil.showMessageDialog(LocaleDictionary.getMessageValue(BatchClassMessages.UNSUPPORTED_FILE_LIST), filesList);
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
	}
}
