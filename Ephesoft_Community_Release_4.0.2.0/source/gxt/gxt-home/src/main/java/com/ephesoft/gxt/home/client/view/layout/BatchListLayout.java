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

package com.ephesoft.gxt.home.client.view.layout;

import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.home.client.view.BatchListLeftPanelView;
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
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;

public class BatchListLayout extends Composite {

	interface Binder extends UiBinder<Component, BatchListLayout> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected ContentPanel optionsPanel;

	@UiField
	protected ContentPanel gridPanel;

	@UiField
	protected BatchListMenuView batchListMenuView;

	@UiField
	protected BatchListGridView batchListGridView;

	@UiField
	protected BatchListLeftPanelView batchListLeftPanelView;

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
	protected ContentPanel buttonPanel;

	@UiField
	protected Label ephesoftPoweredLabel;

	public BatchListLayout() {
		initWidget(binder.createAndBindUi(this));
		addHandlers();
		addStyleNameforContentPanel();
		ephesoftPoweredLabel.setText(EphesoftUIContext.getFooterText());
		ephesoftPoweredLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(CoreCommonConstants.EPHESOFT_LINK, CoreCommonConstants.EMPTY_STRING, CoreCommonConstants.EMPTY_STRING);

			}

		});
	}

	private void addHandlers() {
		final Grid<BatchInstanceDTO> batchListGrid = batchListGridView.getBatchListGrid();
		if (null != batchListGrid) {
			gridPanel.addResizeHandler(new ResizeHandler() {

				@Override
				public void onResize(ResizeEvent event) {
					batchListGrid.setResizeHandler(gridPanel, 0.999999f, 0.999999f);
				}
			});
		}
	}

	/**
	 * @return the optionsPanel
	 */
	public ContentPanel getOptionsPanel() {
		return optionsPanel;
	}

	/**
	 * @param optionsPanel the optionsPanel to set
	 */
	public void setOptionsPanel(ContentPanel optionsPanel) {
		this.optionsPanel = optionsPanel;
	}

	/**
	 * @return the gridPanel
	 */
	public ContentPanel getGridPanel() {
		return gridPanel;
	}

	/**
	 * @param gridPanel the gridPanel to set
	 */
	public void setGridPanel(ContentPanel gridPanel) {
		this.gridPanel = gridPanel;
	}

	/**
	 * @return the batchListMenuView
	 */
	public BatchListMenuView getBatchListMenuView() {
		return batchListMenuView;
	}

	/**
	 * @return the batchListGridView
	 */
	public BatchListGridView getBatchListGridView() {
		return batchListGridView;
	}

	/**
	 * Gets the batch list left panel view.
	 *
	 * @return the batch list left panel view
	 */
	public BatchListLeftPanelView getBatchListLeftPanelView() {
		return batchListLeftPanelView;
	}

	private void addStyleNameforContentPanel() {
		optionsPanel.addStyleName(CoreCommonConstants.OPTIONS_PANEL);
		gridPanel.addStyleName(CoreCommonConstants.GRID_PANEL);
		mainPage.addStyleName(CoreCommonConstants.MAIN_PAGE);
		leftPanel.addStyleName(CoreCommonConstants.LEFT_PANEL);
		logoPanel.addStyleName(CoreCommonConstants.LOGO_PANEL);
		mainPanel.addStyleName(CoreCommonConstants.MAIN_PANEL);
		viewContainer.addStyleName(CoreCommonConstants.SUB_MAIN_PANEL);
		buttonPanel.addStyleName(CoreCommonConstants.BUTTON_PANEL);
		ephesoftPoweredLabel.addStyleName(CoreCommonConstants.POWERED_BY_EPHESOFT_LABEL);
	}
}
