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

package com.ephesoft.gxt.foldermanager.client.controller;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.client.Controller;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.shared.dto.FolderManagerDTO;
import com.ephesoft.gxt.core.shared.dto.FolderUploadedFilesDTO;
import com.ephesoft.gxt.foldermanager.client.FolderManagerServiceAsync;
import com.ephesoft.gxt.foldermanager.client.event.GridSelectionEvent;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementPresenter;
import com.ephesoft.gxt.foldermanager.client.view.FolderManagementView;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class FolderManagementController extends Controller {

	private FolderManagementView folderManagementView;
	private FolderManagementPresenter folderManagementPresenter;
	private FolderManagerDTO selectedFolder;
	private List<FolderUploadedFilesDTO> uploadedFilesList;

	public FolderManagementController(EventBus eventBus, DCMARemoteServiceAsync rpcService) {
		super(eventBus, rpcService);
		uploadedFilesList = new ArrayList<FolderUploadedFilesDTO>();
		this.addNavigationHandler();
	}

	@Override
	public void injectEvents(EventBus eventBus) {

	}

	public void addNavigationHandler() {
		RootPanel.get().addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.isControlKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_K) {
					event.preventDefault();
					eventBus.fireEvent(new GridSelectionEvent());
				}
			}
		}, KeyDownEvent.getType());
	}

	@Override
	public Widget createView() {
		this.folderManagementView = new FolderManagementView();
		this.folderManagementPresenter = new FolderManagementPresenter(this, folderManagementView);
		this.folderManagementPresenter.bind();
		return folderManagementView;
	}

	@Override
	public void refresh() {

	}

	@Override
	public FolderManagerServiceAsync getRpcService() {
		return (FolderManagerServiceAsync) super.getRpcService();
	}

	public FolderManagementView getFolderManagementView() {
		return folderManagementView;
	}

	public FolderManagementPresenter getFolderManagementPresenter() {
		return folderManagementPresenter;
	}

	public void setSelectedFolder(FolderManagerDTO selectedFolder) {
		this.selectedFolder = selectedFolder;
	}

	public FolderManagerDTO getSelectedFolder() {
		return selectedFolder;
	}

	public List<FolderUploadedFilesDTO> getUploadedFilesList() {
		return uploadedFilesList;
	}

	public void setUploadedFilesList(final List<FolderUploadedFilesDTO> uploadedFilesList) {
		this.uploadedFilesList = uploadedFilesList;
	}

	public void clearUploadedFilesList() {
		uploadedFilesList.clear();
	}
}
