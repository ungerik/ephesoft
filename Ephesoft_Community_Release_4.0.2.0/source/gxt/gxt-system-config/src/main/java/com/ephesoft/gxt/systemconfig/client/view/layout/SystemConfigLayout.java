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

package com.ephesoft.gxt.systemconfig.client.view.layout;

import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.shared.dto.Selectable;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.ephesoft.gxt.systemconfig.client.view.navigator.SystemConfigNavigatorView;
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
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;

public class SystemConfigLayout extends Composite {

	interface Binder extends UiBinder<Component, SystemConfigLayout> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected ContentPanel topPanel;

	@UiField
	protected ContentPanel leftPanel;

	@UiField
	protected ContentPanel logoPanel;

	@UiField
	protected ContentPanel mainPanel;

	@UiField
	protected BorderLayoutContainer viewContainer;

	@UiField
	protected BorderLayoutContainer mainPage;

	@UiField
	protected ContentPanel buttonPanel;

	@UiField
	protected ContentPanel centerPanel;

	@UiField
	protected ContentPanel bottomPanel;

	@UiField
	protected ContentPanel treeContentPanel;

	@UiField
	protected SystemConfigNavigatorView navigationView;

	@UiField
	protected Label ephesoftPoweredLabel;

	public SystemConfigLayout() {
		initWidget(binder.createAndBindUi(this));
		addStyleNameforContentPanel();
		ephesoftPoweredLabel.setText(EphesoftUIContext.getFooterText());
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
		topPanel.addStyleName("optionsPanel");
		centerPanel.addStyleName("gridPanel");
		bottomPanel.addStyleName("bottomDetailPanel");
		bottomPanel.addStyleName("bottomPanel");
		bottomPanel.addStyleName("panelHeader");
		treeContentPanel.addStyleName("leftBottomPanel");
		ephesoftPoweredLabel.addStyleName("poweredByEphesoftLabel");
	}

	public void setBottomPanelView(final SystemConfigInlineView<?> inlineView) {
		bottomPanel.clear();
		if (null == inlineView) {
			viewContainer.hide(LayoutRegion.SOUTH);
			bottomPanel.setHeadingText(SystemConfigConstants.EMPTY_STRING);
		} else {
			viewContainer.show(LayoutRegion.SOUTH);
			bottomPanel.add(inlineView);
		}
	}

	public void setCenterPanelView(final SystemConfigInlineView<?> inlineView) {
		centerPanel.clear();
		if (inlineView instanceof HasResizableGrid) {
			final Grid<? extends Selectable> grid = ((HasResizableGrid) inlineView).getGrid();
			if (null != grid) {
				grid.setWidth(centerPanel.getOffsetWidth());
				grid.setHeight((int) (centerPanel.getOffsetHeight() * .90));
				centerPanel.addResizeHandler(new ResizeHandler() {

					@Override
					public void onResize(final ResizeEvent event) {

						grid.setWidth(event.getWidth());
						grid.setHeight((int) (event.getHeight() * .90));
					}
				});
			}
		}
		if (inlineView != null) {
			centerPanel.add(inlineView);
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

	public void setTopPanelView(final SystemConfigInlineView<?> inlineView) {
		topPanel.clear();
		if (inlineView != null) {
			topPanel.add(inlineView);
		}
	}

	public SystemConfigNavigatorView getNavigationView() {
		return navigationView;
	}
}
