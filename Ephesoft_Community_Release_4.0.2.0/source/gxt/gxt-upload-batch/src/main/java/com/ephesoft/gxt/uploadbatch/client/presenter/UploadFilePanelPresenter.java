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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.moxieapps.gwt.uploader.client.File;

import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.ui.widget.window.MessageDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.constant.SupportedUploadFileTypes;
import com.ephesoft.gxt.core.shared.dto.UploadBatchDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.uploadbatch.client.controller.UploadBatchController;
import com.ephesoft.gxt.uploadbatch.client.event.DeleteFilesFromUploadedFilesGridEvent;
import com.ephesoft.gxt.uploadbatch.client.event.FileUploadProgressEvent;
import com.ephesoft.gxt.uploadbatch.client.event.RefreshUploadedFilesGridEvent;
import com.ephesoft.gxt.uploadbatch.client.event.ReloadUploadedFilesGridEvent;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchMessages;
import com.ephesoft.gxt.uploadbatch.client.view.UploadFilePanelView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class UploadFilePanelPresenter extends BasePresenter<UploadBatchController, UploadFilePanelView> {

	interface CustomEventBinder extends EventBinder<UploadFilePanelPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	final Map<String, UploadBatchDTO> uploadingFilesMap = new LinkedHashMap<String, UploadBatchDTO>();

	/**
	 * Constructor.
	 * 
	 * @param controller {@link UploadBatchController}
	 * @param view {@link UploadFilePanelView}
	 */
	public UploadFilePanelPresenter(final UploadBatchController controller, final UploadFilePanelView view) {
		super(controller, view);
	}

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);

	}

	/**
	 * Added the information of the file dropped into the Grid.
	 * 
	 * @param newDroppedFile {@link File}
	 */
	public void addNewFileIntoGrid(final File newDroppedFile) {
		if (null != newDroppedFile && !StringUtil.isNullOrEmpty(newDroppedFile.getType())) {
			final UploadBatchDTO newDroppedFileDTO = new UploadBatchDTO();
			newDroppedFileDTO.setId(newDroppedFile.getId());
			newDroppedFileDTO.setSelected(false);
			newDroppedFileDTO.setFileName(newDroppedFile.getName());
			newDroppedFileDTO.setSize((int) (newDroppedFile.getSize() / 1024));
			newDroppedFileDTO.setType(newDroppedFile.getType());
			newDroppedFileDTO.setProgress(newDroppedFile.getPercentUploaded());
			uploadingFilesMap.put(newDroppedFile.getId(), newDroppedFileDTO);
			controller.getUploadedFileList().add(newDroppedFileDTO);
			controller.getEventBus().fireEvent(new ReloadUploadedFilesGridEvent());
		}
	}

	/**
	 * Updates the progress of the uploading file.
	 * 
	 * @param uploadingFileId {@link String}
	 * @param uploadedPercentage
	 */
	public void updateFileUploadProgress(final String uploadingFileId, final double uploadedPercentage) {

		final UploadBatchDTO uploadingFile = uploadingFilesMap.get(uploadingFileId);

		if (null != uploadingFile) {
			uploadingFile.setProgress(uploadedPercentage);
			final List<UploadBatchDTO> uploadedFileList = controller.getUploadedFileList();
			final int index = uploadedFileList.indexOf(uploadingFile);

			final UploadBatchDTO uploadBatchDTO = uploadedFileList.get(index);
			uploadBatchDTO.setProgress(uploadingFile.isProgress());
			controller.getEventBus().fireEvent(new RefreshUploadedFilesGridEvent(uploadingFile));
		}

	}

	/**
	 * Gets current Upload Batch Folder Name from the controller.
	 * 
	 * @return {@link String}
	 */
	public String getCurrentBatchUploadFolder() {
		return controller.getCurrentBatchUploadFolder();
	}

	public List<Boolean> isValidFile(String fileName) {
		String fileType = null;
		boolean validFile = false;
		boolean officeFile = true;
		List<Boolean> result = new ArrayList<Boolean>(2);
		SupportedUploadFileTypes[] supportedFileTypes = SupportedUploadFileTypes.values();
		if (!StringUtil.isNullOrEmpty(fileName)) {
			final int extensionIndex = fileName.lastIndexOf(UploadBatchConstants.DOT);
			if (extensionIndex != -1 && extensionIndex != fileName.length()) {
				fileType = fileName.substring(extensionIndex + 1);
			}
		}

		if (!StringUtil.isNullOrEmpty(fileType)) {
			for (SupportedUploadFileTypes supportedFileType : supportedFileTypes) {
				if (fileType.equalsIgnoreCase(supportedFileType.getExtension())) {
					validFile = true;
					break;
				}
			}
		}

		// check if office file
		if ((fileType.equalsIgnoreCase(SupportedUploadFileTypes.TIF.getExtension()))
				|| (fileType.equalsIgnoreCase(SupportedUploadFileTypes.TIFF.getExtension()))
				|| (fileType.equalsIgnoreCase(SupportedUploadFileTypes.PDF.getExtension()))) {
			officeFile = false;
		}

		result.add(validFile);
		result.add(officeFile);
		return result;
	}

	public void filesNotSupportedAction(List<String> filesNotSupported) {
		String filesNotImported = UploadBatchConstants.ORDERED_LIST_START_TAG;
		for (String unsupportedFileName : filesNotSupported) {
			filesNotImported = StringUtil.concatenate(filesNotImported, UploadBatchConstants.LIST_START_TAG, unsupportedFileName,
					UploadBatchConstants.LIST_END_TAG);
		}
		final String message = StringUtil.concatenate(LocaleDictionary.getMessageValue(UploadBatchMessages.UNABLE_TO_UPLOAD),
				filesNotSupported.size(), LocaleDictionary.getMessageValue(UploadBatchMessages.UNSUPPORTED_FORMAT_FILES));
		final MessageDialog dialog = new MessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE), message,
				DialogIcon.ERROR);
		dialog.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.YES);
		TextButton showFileList = dialog.getButton(PredefinedButton.YES);
		showFileList.setText(LocaleDictionary.getMessageValue(UploadBatchMessages.SHOW_FILE_LIST));
		final String filesList = StringUtil.concatenate(filesNotImported, UploadBatchConstants.ORDERED_LIST_END_TAG);

		showFileList.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE), filesList,
						DialogIcon.ERROR);

			}
		});

		dialog.addButton(showFileList);
		dialog.show();
		dialog.addDialogHideHandler(new DialogHideHandler() {

			@Override
			public void onDialogHide(DialogHideEvent event) {
				view.clearFilesNotSupportedList();
			}
		});
	}

	public void updateUploadSpeed(File file) {
		controller.getEventBus().fireEvent(new FileUploadProgressEvent(file));
	}

	public void removeFileWithSameNameFromGrid(String fileName) {
		final List<UploadBatchDTO> uploadedFileList = controller.getUploadedFileList();
		for (UploadBatchDTO uploadedFile : uploadedFileList) {
			if (uploadedFile.getFileName().equals(fileName)) {
				controller.getUploadedFileList().remove(uploadedFile);
				break;
			}
		}
	}

	// @EventHandler
	// public void onBatchInstanceViewLoadEvent(UploadBatchViewAdditionEvent
	// additionEvent) {
	// Timer timer = new Timer() {
	//
	// @Override
	// public void run() {
	// view.forceLayout();
	// }
	// };
	// timer.schedule(500);
	// }

	public void removeErrenousFileFromGrid(String uploadingFileId) {
		final UploadBatchDTO uploadingFile = uploadingFilesMap.get(uploadingFileId);
		if (null != uploadingFile) {
			controller.getUploadedFileList().remove(uploadingFile);
			List<UploadBatchDTO> itemsToBeRemoved = new ArrayList<UploadBatchDTO>();
			itemsToBeRemoved.add(uploadingFile);
			controller.getEventBus().fireEvent(new DeleteFilesFromUploadedFilesGridEvent(itemsToBeRemoved));
		}
	}
}
