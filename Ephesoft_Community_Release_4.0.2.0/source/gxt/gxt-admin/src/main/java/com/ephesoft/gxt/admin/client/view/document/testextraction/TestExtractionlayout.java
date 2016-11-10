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

package com.ephesoft.gxt.admin.client.view.document.testextraction;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.view.document.testextraction.navigator.TestExtractionNavigatorView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;

public class TestExtractionlayout extends Composite {

	interface Binder extends UiBinder<Component, TestExtractionlayout> {
	}

	@UiField
	protected TestExtractionNavigatorView testExtractionNavigatorView;

	@UiField
	protected TestExtractionClassificationTypeView testExtractionClassificationTypeView;

	@UiField
	protected TestExtractionMenuView testExtractionMenuView;

	@UiField
	protected ContentPanel centerPanel;

	@UiField
	protected ContentPanel firstContentPanel;

	@UiField
	protected BorderLayoutContainer borderLayoutContainer;

	private VerticalPanel vPanel;

	private Label noResultLabel;

	private static final Binder binder = GWT.create(Binder.class);

	public TestExtractionlayout() {
		initWidget(binder.createAndBindUi(this));
		centerPanel.addStyleName("centerPanel");
		vPanel = new VerticalPanel();
		noResultLabel = new Label(LocaleDictionary.getConstantValue(BatchClassConstants.NO_EXTRACTION_RESULTS));
		vPanel.add(noResultLabel);
		firstContentPanel.setHeaderVisible(true);
		firstContentPanel.setHeadingText(LocaleDictionary.getConstantValue(BatchClassConstants.TEST_EXTRACTION));
		vPanel.addStyleName("noResultViewPanel");
		noResultLabel.addStyleName("noResultViewLabel");
	}

	public void setCenterPanelView(final TestExtractionInlineView<?> inlineView) {
		centerPanel.clear();
		if (inlineView != null) {
			centerPanel.add(inlineView);
			addGridResizeHandler(inlineView, centerPanel);
		} else {
			vPanel.clear();
			vPanel.add(noResultLabel);
			centerPanel.add(vPanel);
		}
	}

	private void addGridResizeHandler(final TestExtractionInlineView<?> inlineView, final ContentPanel panel) {
		if (inlineView instanceof HasResizableGrid) {
			Grid gridToView = ((HasResizableGrid) inlineView).getGrid();
			if (null != gridToView) {
				gridToView.setResizeHandler(panel, 0.9999f, 0.9999f);
			}
		}
	}

	public TestExtractionNavigatorView getNavigationView() {
		return testExtractionNavigatorView;
	}

	public TestExtractionClassificationTypeView getTestExtractionClassificationTypeView() {
		return testExtractionClassificationTypeView;
	}

	public TestExtractionMenuView getTestExtractionMenuView() {
		return testExtractionMenuView;
	}

	public void setBorderLayoutContainer(int offsetHeight, int offsetWidth) {
		borderLayoutContainer.setWidth(offsetWidth);
		borderLayoutContainer.setHeight(offsetHeight);
		DelayedTask task = new DelayedTask() {

			@Override
			public void onExecute() {
				borderLayoutContainer.forceLayout();
			}
		};
		task.delay(200);
	}

	public void clearNoExtractionResultView() {
		vPanel.clear();
	}
}
