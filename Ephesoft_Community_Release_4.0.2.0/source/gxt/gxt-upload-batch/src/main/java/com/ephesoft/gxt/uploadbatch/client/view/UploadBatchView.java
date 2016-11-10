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

import java.util.List;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.ui.widget.BorderLayoutContainer;
import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.shared.dto.UploadBatchDTO;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.gxt.uploadbatch.client.presenter.UploadBatchPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class UploadBatchView extends View<UploadBatchPresenter> {

	@UiField
	protected UploadBatchButtonPanelView inputPanel;

	@UiField
	protected UploadFilePanelView uploadPanel;

	@UiField
	protected UploadBatchGridView uploadedFileGridView;

	@UiField
	protected UploadBatchLeftPanelView uploadBatchLeftPanelView;

	@UiField
	protected ContentPanel gridContainer;

	@UiField
	protected ContentPanel optionsPanel;

	@UiField
	protected ContentPanel leftPanel;

	@UiField
	protected ContentPanel logoPanel;

	@UiField
	protected ContentPanel leftPanelContentPanel;

	@UiField
	protected ContentPanel mainPanel;

	@UiField
	protected ContentPanel bottomPanel;

	@UiField
	protected BorderLayoutContainer viewContainer;

	@UiField
	protected BorderLayoutContainer mainPage;

	@UiField
	protected Label ephesoftPoweredLabel;

	protected AssociateBCFView associateBCFView;

	interface Binder extends UiBinder<Component, UploadBatchView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@Override
	public void initialize() {

	}

	/**
	 * Constructor.
	 */
	public UploadBatchView() {
		super();

		initWidget(binder.createAndBindUi(this));
		ephesoftPoweredLabel.setText(EphesoftUIContext.getFooterText());
		addStyleNameforContentPanel();
		associateBCFView = new AssociateBCFView();
		gridContainer.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				uploadedFileGridView.reSizeGrid(event);

			}

		});

		ephesoftPoweredLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(CoreCommonConstants.EPHESOFT_LINK, CoreCommonConstants.EMPTY_STRING, CoreCommonConstants.EMPTY_STRING);

			}

		});
		bottomPanel.setHeadingText(LocaleDictionary.getConstantValue(UploadBatchConstants.UPLOAD_FILES));
	}

	/**
	 * Gets Upload Batch Button Panel View.
	 * 
	 * @return {@link UploadBatchButtonPanelView}
	 */
	public UploadBatchButtonPanelView getInputPanel() {
		return inputPanel;
	}

	/**
	 * Sets Upload Batch Button Panel View.
	 * 
	 * @param {@link UploadBatchButtonPanelView}
	 */
	public void setInputPanel(final UploadBatchButtonPanelView inputPanel) {
		this.inputPanel = inputPanel;
	}

	/**
	 * Gets Upload File Panel View.
	 * 
	 * @return {@link UploadFilePanelView}
	 */
	public UploadFilePanelView getUploadPanel() {
		return uploadPanel;
	}

	/**
	 * Sets Upload File Panel View.
	 * 
	 * @param {@link UploadFilePanelView}
	 */
	public void setUploadPanel(final UploadFilePanelView uploadPanel) {
		this.uploadPanel = uploadPanel;
	}

	/**
	 * Gets Upload Batch Grid View.
	 * 
	 * @return {@link UploadBatchGridView}
	 */
	public UploadBatchGridView getUploadedFileGridView() {
		return uploadedFileGridView;
	}

	/**
	 * Sets Upload Batch Grid View.
	 * 
	 * @param {@link UploadBatchGridView}
	 */
	public void setUploadedFileGridView(final UploadBatchGridView uploadedFileGridView) {
		this.uploadedFileGridView = uploadedFileGridView;
	}

	/**
	 * Gets Associate Batch Class Fields View.
	 * 
	 * @return {@link AssociateBCFView}
	 */
	public AssociateBCFView getAssociateBCFView() {
		return associateBCFView;
	}

	/**
	 * Sets Associate Batch Class Field View.
	 * 
	 * @param {@link AssociateBCFView}
	 */
	public void setAssociateBCFView(final AssociateBCFView associateBCFView) {
		this.associateBCFView = associateBCFView;
	}

	/**
	 * Gets selected uploaded files list from Upload Batch Grid View.
	 * 
	 * @return {@link List}<{@link UploadBatchDTO}>
	 */
	public List<UploadBatchDTO> getSelectedFilesFromGridView() {
		return uploadedFileGridView.getSlectedFilesFromGrid();

	}

	private void addStyleNameforContentPanel() {
		optionsPanel.addStyleName(CoreCommonConstants.OPTIONS_PANEL);
		gridContainer.addStyleName(CoreCommonConstants.GRID_PANEL);
		bottomPanel.addStyleName(CoreCommonConstants.BOTTOM_DETAIL_PANEL);
		mainPage.addStyleName(CoreCommonConstants.MAIN_PAGE);
		leftPanel.addStyleName(CoreCommonConstants.LEFT_PANEL);
		logoPanel.addStyleName(CoreCommonConstants.LOGO_PANEL);
		leftPanelContentPanel.addStyleName(CoreCommonConstants.LEFT_PANEL_CONTENT_PANEL);
		mainPanel.addStyleName(CoreCommonConstants.MAIN_PANEL);
		viewContainer.addStyleName(CoreCommonConstants.SUB_MAIN_PANEL);
		optionsPanel.addStyleName(CoreCommonConstants.BUTTON_PANEL);
		bottomPanel.addStyleName(CoreCommonConstants.BOTTOM_PANEL);
		bottomPanel.addStyleName(CoreCommonConstants.PANEL_HEADER);
		ephesoftPoweredLabel.addStyleName(CoreCommonConstants.POWERED_BY_EPHESOFT_LABEL);
	}

	/**
	 * Gets the upload batch left panel view.
	 *
	 * @return the upload batch left panel view
	 */
	public UploadBatchLeftPanelView getUploadBatchLeftPanelView() {
		return uploadBatchLeftPanelView;
	}

	public void setUploadBatchLeftPanelView(UploadBatchLeftPanelView uploadBatchLeftPanelView) {
		this.uploadBatchLeftPanelView = uploadBatchLeftPanelView;
	}
}
