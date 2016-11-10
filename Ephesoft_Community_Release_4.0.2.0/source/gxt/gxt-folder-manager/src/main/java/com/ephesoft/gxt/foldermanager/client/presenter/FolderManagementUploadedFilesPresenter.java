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

package com.ephesoft.gxt.foldermanager.client.presenter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.moxieapps.gwt.uploader.client.File;

import com.ephesoft.gxt.core.shared.dto.FolderUploadedFilesDTO;
import com.ephesoft.gxt.foldermanager.client.controller.FolderManagementController;
import com.ephesoft.gxt.foldermanager.client.event.ReloadUplodedFilesEvent;
import com.ephesoft.gxt.foldermanager.client.view.FolderManagementUploadedFilesView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class FolderManagementUploadedFilesPresenter extends FolderManagementBasePresenter<FolderManagementUploadedFilesView> {

	public FolderManagementUploadedFilesPresenter(FolderManagementController controller, FolderManagementUploadedFilesView view) {
		super(controller, view);
	}

	final Map<String, FolderUploadedFilesDTO> uploadingFilesMap = new LinkedHashMap<String, FolderUploadedFilesDTO>();

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@Override
	public void bind() {

	}

	interface CustomEventBinder extends EventBinder<FolderManagementUploadedFilesPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	@EventHandler
	public void reloadGrid(final ReloadUplodedFilesEvent reloadGridEvent) {
		view.setDataAndReloadGrid(controller.getUploadedFilesList());
	}

	/**
	 * Added the information of the file dropped into the Grid.
	 * 
	 * @param newDroppedFile {@link File}
	 */
	public boolean addNewFileIntoGrid(final File newDroppedFile) {
		boolean fileAdded = false;
		if (null != newDroppedFile) {
			fileAdded = true;
			final FolderUploadedFilesDTO newDroppedFileDTO = new FolderUploadedFilesDTO();
			newDroppedFileDTO.setId(newDroppedFile.getId());
			newDroppedFileDTO.setSelected(false);
			newDroppedFileDTO.setFileName(newDroppedFile.getName());
			newDroppedFileDTO.setSize(String.valueOf(newDroppedFile.getSize() / 1024));
			newDroppedFileDTO.setType(newDroppedFile.getType());
			newDroppedFileDTO.setProgress(newDroppedFile.getPercentUploaded());
			uploadingFilesMap.put(newDroppedFile.getId(), newDroppedFileDTO);
			controller.getUploadedFilesList().add(newDroppedFileDTO);
			controller.getEventBus().fireEvent(new ReloadUplodedFilesEvent());
		}
		return fileAdded;
	}

	public void updateFileUploadProgress(String uploadingFileId, double uploadedPercentage) {
		final FolderUploadedFilesDTO uploadingFile = uploadingFilesMap.get(uploadingFileId);
		if (null != uploadingFile) {
			uploadingFile.setProgress(uploadedPercentage);
			final List<FolderUploadedFilesDTO> uploadedFileList = controller.getUploadedFilesList();
			final int index = uploadedFileList.indexOf(uploadingFile);
			final FolderUploadedFilesDTO uploadBatchDTO = uploadedFileList.get(index);
			uploadBatchDTO.setProgress(uploadingFile.isProgress());
			int rowIndex = view.getRowIndex(uploadBatchDTO);
			view.getGridView().getRow(rowIndex).scrollIntoView();
			refreshRow(rowIndex);
		}
	}

	public void refreshRow(int rowIndex) {
		if (rowIndex >= 0) {
			view.refreshGridRow(rowIndex);
		}
	}

	public void clearUploadedFilesList() {
		controller.clearUploadedFilesList();
	}

}
