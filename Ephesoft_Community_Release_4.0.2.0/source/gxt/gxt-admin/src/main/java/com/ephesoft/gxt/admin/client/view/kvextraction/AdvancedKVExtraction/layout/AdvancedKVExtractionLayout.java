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

package com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.layout;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionInlineView;
import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;

public class AdvancedKVExtractionLayout extends Composite {

	interface Binder extends UiBinder<Component, AdvancedKVExtractionLayout> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected ContentPanel optionsPanel;

	@UiField
	protected ContentPanel imagePanel;

	@UiField
	protected ContentPanel bottomPanel;

	@UiField
	protected ContentPanel inputPanel;

	protected ContentPanel dropPanel;
	
	@UiField
	protected BorderLayoutContainer mainPage;
	
	@UiField
	protected ContentPanel leftPanel;
	
	@UiField
	protected ContentPanel logoPanel;
	
	@UiField
	protected ContentPanel mainPanel;
	
	@UiField
	protected ContentPanel buttonPanel;
	
	@UiField
	protected BorderLayoutContainer viewContainer;
	
	@UiField
	protected Label ephesoftPoweredLabel;
	
	public AdvancedKVExtractionLayout() {
		this(LocaleDictionary.getConstantValue(BatchClassConstants.ADVANCE_KV_TEST));
	}

	public AdvancedKVExtractionLayout(String bottomPanelHeading) {
		initWidget(binder.createAndBindUi(this));
		bottomPanel.setHeaderVisible(true);
		bottomPanel.setHeadingText(bottomPanelHeading);
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
		optionsPanel.addStyleName("optionsPanel");
		imagePanel.addStyleName("gridPanel");
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

	public void setOptionsPanelView(final Composite view) {
		optionsPanel.clear();
		if (view != null) {
			optionsPanel.add(view);
		}
	}

	public void setBottomPanelView(final Composite view) {
		bottomPanel.clear();
		if (view != null) {
			bottomPanel.add(view);
		}
	}

	public void setInputPanelView(final Composite view) {
		inputPanel.clear();
		if (view != null) {
			inputPanel.add(view);
		}
	}

	public void setGridView(final Composite view) {
		imagePanel.clear();
		if (view != null) {
			imagePanel.add(view);
		}
	}

	public void setDropFileView(final Composite view) {
		dropPanel.clear();
		if (view != null) {
			dropPanel.add(view);
		}
	}

	/**
	 * @return the gridPanel
	 */
	public ContentPanel getBottomsPanel() {
		return bottomPanel;
	}

	public ContentPanel getImagePanel() {
		return imagePanel;
	}

	public void setAdvKVBottomPanelView(final AdvancedKVExtractionInlineView<?> inlineView) {
		bottomPanel.clear();
		if (null != inlineView) {
			bottomPanel.add(inlineView);
		}
	}

	public void setResizeHandlerOnGrid(final AdvancedKVExtractionInlineView<?> inlineView) {
		if (inlineView instanceof HasResizableGrid) {
			Grid gridToView = ((HasResizableGrid) inlineView).getGrid();
			if (null != gridToView) {
				gridToView.setResizeHandler(bottomPanel, 0.999f, 0.92f);
			}
		}
	}

	public void setAdvKVOptionsPanelView(final AdvancedKVExtractionInlineView<?> inlineView) {
		optionsPanel.clear();
		if (null != inlineView) {
			optionsPanel.add(inlineView);
		}
	}

	public void setAdvKVListPanelView(final AdvancedKVExtractionInlineView<?> inlineView) {
		imagePanel.clear();
		if (null != inlineView) {
			imagePanel.add(inlineView);
		}

	}

	public void setAdvKVInputPanelView(final AdvancedKVExtractionInlineView<?> inlineView) {
		inputPanel.clear();
		if (null != inlineView) {
			inputPanel.add(inlineView);
		}
	}

	public void setAdvKVDropFilePanelView(final AdvancedKVExtractionInlineView<?> inlineView) {
		dropPanel.clear();
		if (null != inlineView) {
			dropPanel.add(inlineView);
		}
	}
}
