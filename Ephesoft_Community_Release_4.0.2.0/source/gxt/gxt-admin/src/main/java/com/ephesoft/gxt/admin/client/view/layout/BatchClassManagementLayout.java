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

package com.ephesoft.gxt.admin.client.view.layout;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.DialogWindowResizeEvent;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.view.chart.BatchClassChartView;
import com.ephesoft.gxt.admin.client.view.navigator.BatchClassNavigatorView;
import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.ui.widget.BorderLayoutContainer;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.Style.LayoutRegion;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;

public class BatchClassManagementLayout extends Composite {

	interface Binder extends UiBinder<Component, BatchClassManagementLayout> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected ContentPanel optionsPanel;

	@UiField
	protected ContentPanel leftPanel;

	@UiField
	protected ContentPanel logoPanel;

	@UiField
	protected ContentPanel mainPanel;

	@UiField
	protected ContentPanel buttonPanel;

	@UiField
	protected ContentPanel gridPanel;

	@UiField
	protected ContentPanel bottomPanel;

	@UiField
	protected BorderLayoutContainer viewContainer;

	@UiField
	protected BorderLayoutData batchDetailPanelData;

	@UiField
	protected BorderLayoutContainer mainPage;

	@UiField
	protected BorderLayoutContainer navigationContainer;

	@UiField
	protected Label ephesoftPoweredLabel;

	protected BatchClassNavigatorView navigationView;

	protected BatchClassChartView chartView;

	public BatchClassManagementLayout() {
		initWidget(binder.createAndBindUi(this));
		addStyleNameforContentPanel();

		gridPanel.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				BatchClassManagementEventBus.fireEvent(new DialogWindowResizeEvent());
			}
		});
		ephesoftPoweredLabel.setText(EphesoftUIContext.getFooterText());
		ephesoftPoweredLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(CoreCommonConstants.EPHESOFT_LINK, "", "");

			}

		});

		navigationView = new BatchClassNavigatorView();
		chartView = new BatchClassChartView();
		setChartView();
	}

	/**
	 * 
	 */
	public void setChartView() {
		navigationContainer.setCenterWidget(chartView);
	}

	private void addStyleNameforContentPanel() {
		optionsPanel.addStyleName("optionsPanel");
		gridPanel.addStyleName("gridPanel");
		bottomPanel.addStyleName("bottomDetailPanel");
		mainPage.addStyleName("mainPage");
		leftPanel.addStyleName("leftPanel");
		logoPanel.addStyleName("logoPanel");
		mainPanel.addStyleName("mainPanel");
		viewContainer.addStyleName("subMainPanel");
		buttonPanel.addStyleName("buttonPanel");
		bottomPanel.addStyleName("bottomPanel");
		bottomPanel.addStyleName("panelHeader");
		ephesoftPoweredLabel.addStyleName("poweredByEphesoftLabel");
	}

	public BatchClassNavigatorView getNavigationView() {
		return navigationView;
	}

	public BatchClassChartView getBatchClassChartView() {
		return chartView;
	}

	public void setBottomPanelView(final BatchClassInlineView<?> view) {
		bottomPanel.clear();

		if (view != null) {
			viewContainer.show(LayoutRegion.SOUTH);
			bottomPanel.add(view);
			viewContainer.expand(LayoutRegion.SOUTH);
		} else {
			viewContainer.hide(LayoutRegion.SOUTH);
		}

		if (null != view) {
			bottomPanel.add(view);
			addGridResizeHandler(view, bottomPanel);
		}
	}

	public void setBottomPanelHeader(final String header) {
		if (StringUtil.isNullOrEmpty(header)) {
			bottomPanel.setHeaderVisible(false);
		} else {
			bottomPanel.setHeaderVisible(true);
			bottomPanel.setHeadingText(header);
		}
	}

	public void setGridView(final Composite view) {
		gridPanel.clear();
		if (view != null) {
			gridPanel.add(view);
		}
	}

	/**
	 * @return the gridPanel
	 */
	public ContentPanel getGridPanel() {
		return gridPanel;
	}

	public void setOptionsPanelView(final BatchClassInlineView<?> inlineView) {
		optionsPanel.clear();
		if (null != inlineView) {
			optionsPanel.add(inlineView);
		}
	}

	public ContentPanel getBottomContentPanel() {
		return bottomPanel;
	}

	@SuppressWarnings("rawtypes")
	public void setListPanelView(final BatchClassInlineView<?> inlineView) {
		gridPanel.clear();
		if (null != inlineView) {
			gridPanel.add(inlineView);
			addGridResizeHandler(inlineView, gridPanel);
		}
	}

	private void addGridResizeHandler(final BatchClassInlineView<?> inlineView, final ContentPanel panel) {
		if (inlineView instanceof HasResizableGrid) {
			Grid gridToView = ((HasResizableGrid) inlineView).getGrid();
			if (null != gridToView) {
				gridToView.setResizeHandler(panel, 1.0f, 0.9999f);
			}
		}
	}

	public ContentPanel getBottomPanel() {
		return bottomPanel;
	}

	public void setNavigationView() {
		navigationContainer.setCenterWidget(navigationView);
		navigationContainer.forceLayout();
	}

	public ContentPanel getLeftPanel() {
		return leftPanel;
	}
}
