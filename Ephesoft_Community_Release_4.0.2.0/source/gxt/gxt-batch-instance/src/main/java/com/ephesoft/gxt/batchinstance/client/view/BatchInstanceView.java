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

package com.ephesoft.gxt.batchinstance.client.view;

import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstancePresenter;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.BorderLayoutContainer;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent;
import com.sencha.gxt.widget.core.client.event.CollapseItemEvent.CollapseItemHandler;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;

public class BatchInstanceView extends View<BatchInstancePresenter> {

	interface Binder extends UiBinder<Component, BatchInstanceView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected BorderLayoutContainer layoutContainer;

	@UiField
	protected BatchInstanceGridView gridView;

	@UiField
	protected BatchInstanceOptionsView batchInstanceOptionsView;

	@UiField
	protected BatchInstanceDetailView batchInstanceDetailView;

	@UiField
	protected BatchInstanceLeftPanelView batchInstanceLeftPanelView;

	@UiField
	protected ContentPanel gridContainer;

	@UiField
	protected ContentPanel batchInstanceDetailPanel;

	@UiField
	protected ContentPanel leftPanel;

	@UiField
	protected ContentPanel logoPanel;

	@UiField
	protected ContentPanel leftBottomPanel;

	@UiField
	protected ContentPanel mainPanel;

	@UiField
	protected ContentPanel buttonPanel;

	@UiField
	protected BorderLayoutContainer subMainPanel;

	@UiField
	protected Label ephesoftPoweredLabel;

	public BatchInstanceView() {
		super();
		initWidget(binder.createAndBindUi(this));
		addLayoutHandlers();
		addStyleNameforContentPanel();
		batchInstanceDetailPanel.setHeadingText(LocaleDictionary.getConstantValue(BatchInstanceConstants.BATCH_INSTANCE_BOTTOM_PANEL_HEADER));
		ephesoftPoweredLabel.setText(EphesoftUIContext.getFooterText());
		batchInstanceDetailPanel.addStyleName("panelHeader");

		ephesoftPoweredLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(CoreCommonConstants.EPHESOFT_LINK, "", "");

			}

		});

		// Bug fix for troubleshoot layout resize.
		subMainPanel.addExpandHandler(new ExpandItemHandler<ContentPanel>() {

			@Override
			public void onExpand(ExpandItemEvent<ContentPanel> event) {
				batchInstanceDetailView.getTroubleshootPanel().resizeTroubleshootPanel();
			}
		});
	}

	private void addLayoutHandlers() {
		gridContainer.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				Grid<BatchInstanceDTO> batchInstanceGrid = gridView.getBatchInstanceGrid();
				batchInstanceGrid.setResizeHandler(gridContainer, 0.9999f, 0.9999f);
				presenter.fireResizeEvent();
			}
		});

		layoutContainer.addExpandHandler(new ExpandItemHandler<ContentPanel>() {

			@Override
			public void onExpand(ExpandItemEvent<ContentPanel> event) {

				Timer timer = new Timer() {

					@Override
					public void run() {
						batchInstanceDetailView.forceLayout();
					}

				};
				timer.schedule(10);
			}
		});

		layoutContainer.addCollapseHandler(new CollapseItemHandler<ContentPanel>() {

			@Override
			public void onCollapse(CollapseItemEvent<ContentPanel> event) {

				Timer timer = new Timer() {

					@Override
					public void run() {
						batchInstanceDetailView.forceLayout();
					}

				};
				timer.schedule(10);
			}
		});

		batchInstanceDetailPanel.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						batchInstanceDetailView.forceLayout();
					}
				});
			}
		});
	}

	private void addStyleNameforContentPanel() {
		layoutContainer.addStyleName("mainPage");
		leftPanel.addStyleName("leftPanel");
		logoPanel.addStyleName("logoPanel");
		leftBottomPanel.addStyleName("leftBottomPanel");
		mainPanel.addStyleName("mainPanel");
		subMainPanel.addStyleName("subMainPanel");
		buttonPanel.addStyleName("buttonPanel");
		gridContainer.addStyleName("gridPanel");
		batchInstanceDetailPanel.addStyleName("bottomPanel");
		ephesoftPoweredLabel.addStyleName("poweredByEphesoftLabel");
	}

	@Override
	public void initialize() {
	}

	/**
	 * @return the gridView
	 */
	public BatchInstanceGridView getGridView() {
		return gridView;
	}

	/**
	 * @return the batchInstanceOptionsView
	 */
	public BatchInstanceOptionsView getBatchInstanceOptionsView() {
		return batchInstanceOptionsView;
	}

	/**
	 * @return the batchInstanceDetailView
	 */
	public BatchInstanceDetailView getBatchInstanceDetailView() {
		return batchInstanceDetailView;
	}

	public BatchInstanceLeftPanelView getBatchInstanceLeftPanelView() {
		return batchInstanceLeftPanelView;
	}

	public void setBatchInstanceLeftPanelView(BatchInstanceLeftPanelView batchInstanceLeftPanelView) {
		this.batchInstanceLeftPanelView = batchInstanceLeftPanelView;
	}

}
