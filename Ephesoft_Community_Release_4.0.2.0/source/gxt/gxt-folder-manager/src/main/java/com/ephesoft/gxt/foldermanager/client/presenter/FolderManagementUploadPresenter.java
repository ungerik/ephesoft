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

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.foldermanager.client.controller.FolderManagementController;
import com.ephesoft.gxt.foldermanager.client.event.CurrentPathRefreshEvent;
import com.ephesoft.gxt.foldermanager.client.event.FolderTreeRefreshEvent;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementConstants;
import com.ephesoft.gxt.foldermanager.client.view.FolderManagementUploadView;
import com.ephesoft.gxt.foldermanager.client.view.FolderManagementUploadedFilesView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class FolderManagementUploadPresenter extends FolderManagementBasePresenter<FolderManagementUploadView> {

	public FolderManagementUploadPresenter(FolderManagementController controller, FolderManagementUploadView view) {
		super(controller, view);
		setBottomPanelHeader(LocaleDictionary.getConstantValue(FolderManagementConstants.UPLOAD_VIEW_HEADER));
	}

	private String folderPath;

	interface CustomEventBinder extends EventBinder<FolderManagementUploadPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void addNewFileIntoMainGrid() {
		controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
	}

	@EventHandler
	public void onCurrentPathChange(CurrentPathRefreshEvent currentPathRefreshEvent) {
		this.folderPath = currentPathRefreshEvent.getFolderPath();
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public void clearBottomPanel() {
		ContentPanel bottomPanel = controller.getFolderManagementView().getBottomPanel();
		bottomPanel.clear();
	}

	public void addUploadGridView() {
		ContentPanel bottomPanel = controller.getFolderManagementView().getBottomPanel();
		if (null != getUploadedFilesView()) {
			bottomPanel.add(getUploadedFilesView());
			setBottomPanelHeader("");
		}

	}

	private FolderManagementUploadedFilesView getUploadedFilesView() {
		return controller.getFolderManagementView().getFolderManagementUploadedFilesPresenter().getView();
	}

	public FolderManagementUploadedFilesPresenter getUploadedFilesPresenter() {
		return controller.getFolderManagementView().getFolderManagementUploadedFilesPresenter();
	}

	public void addDragnDropView() {
		ContentPanel bottomPanel = controller.getFolderManagementView().getBottomPanel();
		if (null != view) {
			bottomPanel.add(view);
			setBottomPanelHeader(LocaleDictionary.getConstantValue(FolderManagementConstants.UPLOAD_VIEW_HEADER));
		}
	}

	public void setBottomPanelHeader(String header) {
		if (StringUtil.isNullOrEmpty(header)) {
			controller.getFolderManagementView().getBottomPanel().setHeaderVisible(false);
		} else {
			controller.getFolderManagementView().getBottomPanel().setHeaderVisible(true);
			controller.getFolderManagementView().getBottomPanel().setHeadingText(header);
		}
	}

	public void clearUploadedFilesList() {
		controller.clearUploadedFilesList();
	}
}
