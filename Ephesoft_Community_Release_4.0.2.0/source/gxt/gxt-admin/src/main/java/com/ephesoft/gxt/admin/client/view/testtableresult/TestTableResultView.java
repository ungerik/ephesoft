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

package com.ephesoft.gxt.admin.client.view.testtableresult;

import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.DomainView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DataTableGrid;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

@SuppressWarnings("rawtypes")
public class TestTableResultView extends DomainView<DataTable, BasePresenter> {

	interface Binder extends UiBinder<Widget, TestTableResultView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected HorizontalLayoutContainer hPanel;

	@UiField
	protected Label inputFileLabel;

	@UiField
	protected Label ruleNameLabel;

	@UiField
	protected Label inputFileName;

	@UiField
	protected Label ruleName;

	@UiField(provided = true)
	protected DataTableGrid testTableGrid;

	public TestTableResultView(final DataTable bindedObject, Integer width, Integer height) {

		super(bindedObject);
		if (null != bindedObject) {
			testTableGrid = new DataTableGrid(bindedObject, width, height, true);
			testTableGrid.getView().setForceFit(true);
			this.initWidget(binder.createAndBindUi(this));
			hPanel.setHeight(30);
			hPanel.setWidth(width);
			String fileNameLabelText = StringUtil.concatenate(
					LocaleDictionary.getConstantValue(BatchClassConstants.INPUT_FILE_NAME_LABEL), BatchClassConstants.COLON,
					BatchClassConstants.SPACE);
			inputFileLabel.setStyleName("fontBold");
			inputFileLabel.setText(fileNameLabelText);
			String ruleNameLabelText = StringUtil.concatenate(LocaleDictionary.getConstantValue(BatchClassConstants.RULE_NAME_LABEL),
					BatchClassConstants.COLON, BatchClassConstants.SPACE);
			ruleNameLabel.setStyleName("fontBold");
			ruleNameLabel.setText(ruleNameLabelText);
		}
	}

	@Override
	public void focus() {
	}

	public void setGridHeight(final int height) {
		testTableGrid.setHeight(height);
	}

	public void setGridWidth(final int width) {
		testTableGrid.setWidth(width);
	}

	public void setHPanelWidth(final int width) {
		hPanel.setWidth(width);
	}

	public String getInputFileName() {
		return inputFileName.getText();
	}

	public void setInputFileName(String inputfileName) {
		this.inputFileName.setText(inputfileName);
	}

	public String getRuleName() {
		return ruleName.getText();
	}

	public void setRuleName(String ruleName) {
		this.ruleName.setText(ruleName);
	}

	public void refreshGrid() {
		testTableGrid.getView().refresh(true);
	}
}
