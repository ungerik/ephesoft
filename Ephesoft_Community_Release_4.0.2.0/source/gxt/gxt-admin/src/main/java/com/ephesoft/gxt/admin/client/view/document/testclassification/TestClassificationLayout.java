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

package com.ephesoft.gxt.admin.client.view.document.testclassification;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.shared.dto.TestClassificationDataCarrierDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;

public class TestClassificationLayout extends Composite {

	interface Binder extends UiBinder<Component, TestClassificationLayout> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected TestClassificationGridView testClassificationGridView;

	@UiField
	protected TestClassificationMenuView testClassificationMenuView;

	@UiField
	protected TestClassifyTypeView testClassifyTypeView;

	@UiField
	protected BorderLayoutContainer borderLayoutContainer;

	@UiField
	protected ContentPanel testClassificationGridPanel;

	@UiField
	protected ContentPanel testClassificationNorthContentPanel;

	public TestClassificationLayout() {
		initWidget(binder.createAndBindUi(this));
		testClassificationGridPanel.addStyleName("gridViewContentPanel");
		addGridResizeHandler();
		testClassificationNorthContentPanel.setHeaderVisible(true);
		testClassificationNorthContentPanel.setHeadingText(LocaleDictionary.getConstantValue(BatchClassConstants.TEST_CLASSIFICATION));
	}

	private void addGridResizeHandler() {
		Grid<TestClassificationDataCarrierDTO> testClassificationGrid = testClassificationGridView.getGrid();
		if (null != testClassificationGrid) {
			testClassificationGrid.setResizeHandler(testClassificationGridPanel, 0.9999f, 0.9999f);
		}
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

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	public TestClassificationMenuView getTestClassificationMenuView() {
		return testClassificationMenuView;
	}

	public TestClassifyTypeView getTestClassifyTypeView() {
		return testClassifyTypeView;
	}

	public TestClassificationGridView getTestClassificationGridView() {
		return testClassificationGridView;
	}
}
