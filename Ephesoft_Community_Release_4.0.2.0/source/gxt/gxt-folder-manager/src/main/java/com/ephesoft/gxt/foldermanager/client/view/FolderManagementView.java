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

import java.util.Map;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.shared.dto.FolderManagerDTO;
import com.ephesoft.gxt.foldermanager.client.controller.FolderManagementController;
import com.ephesoft.gxt.foldermanager.client.event.FolderTreeRefreshEvent;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementGridPresenter;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementNavigatorPresenter;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementOptionsPresenter;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementPresenter;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementUploadPresenter;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementUploadedFilesPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;

public class FolderManagementView extends View<FolderManagementPresenter> {

	interface Binder extends UiBinder<Component, FolderManagementView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected BorderLayoutContainer mainPage;

	@UiField
	protected ContentPanel leftPanel;

	@UiField
	protected ContentPanel logoPanel;

	@UiField
	protected ContentPanel mainPanel;

	@UiField
	protected BorderLayoutContainer viewContainer;

	@UiField
	protected FolderManagementOptionsView folderManagementOptionsView;

	@UiField
	protected FolderManagementGridView folderManagementGridView;

	@UiField
	protected FolderManagementNavigatorView folderManagementNavigatorView;

	@UiField
	protected FolderManagementUploadView folderManagementUploadView;

	@UiField
	protected ContentPanel bottomPanel;

	@UiField
	protected ContentPanel navigatorPanel;

	@UiField
	protected ContentPanel gridPanel;

	@UiField
	protected ContentPanel buttonPanel;

	@UiField
	protected Label ephesoftPoweredLabel;

	private FolderManagementUploadedFilesView folderManagementUploadedFilesView;

	private FolderManagementUploadedFilesPresenter folderManagementUploadedFilesPresenter;

	private FolderManagementNavigatorPresenter folderManagementNavigatorPresenter;

	private FolderManagementGridPresenter folderManagementGridPresenter;

	private FolderManagementUploadPresenter folderManagementUploadPresenter;

	private FolderManagementOptionsPresenter folderManagementOptionsPresenter;

	public FolderManagementView() {
		super();
		initWidget(binder.createAndBindUi(this));
		navigatorPanel.addStyleName("navigatorViewContentPanel");
		folderManagementUploadedFilesView = new FolderManagementUploadedFilesView();
		ephesoftPoweredLabel.setText(EphesoftUIContext.getFooterText());
		addGridResizeHandler();
		addStyleNameforContentPanel();

		ephesoftPoweredLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(CoreCommonConstants.EPHESOFT_LINK, "", "");

			}

		});
	}

	private void addStyleNameforContentPanel() {
		mainPage.addStyleName("mainPage");
		leftPanel.addStyleName("leftPanel");
		logoPanel.addStyleName("logoPanel");
		mainPanel.addStyleName("mainPanel");
		viewContainer.addStyleName("subMainPanel");
		buttonPanel.addStyleName("buttonPanel");
		gridPanel.addStyleName("gridPanel");
		bottomPanel.addStyleName("bottomPanel");
		bottomPanel.addStyleName("panelHeader");
		bottomPanel.addStyleName("bottomDetailPanel");
		navigatorPanel.addStyleName("leftBottomPanel");
		ephesoftPoweredLabel.addStyleName("poweredByEphesoftLabel");
	}

	private void addGridResizeHandler() {
		gridPanel.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				setGridViewSize();
			}
		});
		bottomPanel.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				folderManagementUploadedFilesView.resizeGrid(event);
			}
		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		setGridViewSize();
	}

	private void setGridViewSize() {
		Grid<FolderManagerDTO> folderManagerGrid = folderManagementGridView.getGrid();
		if (null != folderManagerGrid) {
			folderManagerGrid.setWidth(Math.round(gridPanel.getOffsetWidth() * 1.001f));
			folderManagerGrid.setHeight((int) (gridPanel.getOffsetHeight() * .99f - 28));
		}
	}

	@Override
	public void initialize() {
	}

	public void setInitialFolderManagementView(FolderManagementController controller, String parentFolderPath,
			Map<String, String> batchClassesMap, String baseFolderUrl) {
		Label footer = folderManagementGridView.getFooter();
		this.setFolderManagementNavigatorPresenter(new FolderManagementNavigatorPresenter(controller, folderManagementNavigatorView,
				parentFolderPath, batchClassesMap, footer));
		this.getFolderManagementNavigatorPresenter().loadComboBox();
		this.setFolderManagementGridPresenter(new FolderManagementGridPresenter(controller, folderManagementGridView, parentFolderPath));
		this.setFolderManagementUploadPresenter(new FolderManagementUploadPresenter(controller, folderManagementUploadView));
		this.setFolderManagementOptionsPresenter(new FolderManagementOptionsPresenter(controller, folderManagementOptionsView,
				baseFolderUrl));
		EventBus eventBus = controller.getEventBus();
		eventBus.fireEvent(new FolderTreeRefreshEvent());
		this.setFolderManagementUploadedFilesPresenter(new FolderManagementUploadedFilesPresenter(controller,
				folderManagementUploadedFilesView));
	}

	public FolderManagementNavigatorPresenter getFolderManagementNavigatorPresenter() {
		return folderManagementNavigatorPresenter;
	}

	public void setFolderManagementNavigatorPresenter(FolderManagementNavigatorPresenter folderManagementNavigatorPresenter) {
		this.folderManagementNavigatorPresenter = folderManagementNavigatorPresenter;
	}

	public FolderManagementGridPresenter getFolderManagementGridPresenter() {
		return folderManagementGridPresenter;
	}

	public void setFolderManagementGridPresenter(FolderManagementGridPresenter folderManagementGridPresenter) {
		this.folderManagementGridPresenter = folderManagementGridPresenter;
	}

	public FolderManagementUploadPresenter getFolderManagementUploadPresenter() {
		return folderManagementUploadPresenter;
	}

	public void setFolderManagementUploadPresenter(FolderManagementUploadPresenter folderManagementUploadPresenter) {
		this.folderManagementUploadPresenter = folderManagementUploadPresenter;
	}

	public FolderManagementOptionsPresenter getFolderManagementOptionsPresenter() {
		return folderManagementOptionsPresenter;
	}

	public void setFolderManagementOptionsPresenter(FolderManagementOptionsPresenter folderManagementOptionsPresenter) {
		this.folderManagementOptionsPresenter = folderManagementOptionsPresenter;
	}

	public ContentPanel getBottomPanel() {
		return bottomPanel;
	}

	public void setBottomPanel(ContentPanel bottomPanel) {
		this.bottomPanel = bottomPanel;
	}

	public FolderManagementUploadedFilesPresenter getFolderManagementUploadedFilesPresenter() {
		return folderManagementUploadedFilesPresenter;
	}

	public void setFolderManagementUploadedFilesPresenter(FolderManagementUploadedFilesPresenter folderManagementUploadedFilesPresenter) {
		this.folderManagementUploadedFilesPresenter = folderManagementUploadedFilesPresenter;
	}

}
