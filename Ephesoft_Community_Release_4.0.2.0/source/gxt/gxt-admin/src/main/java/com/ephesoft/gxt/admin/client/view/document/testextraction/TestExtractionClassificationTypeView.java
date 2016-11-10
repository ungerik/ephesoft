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

import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.document.testextraction.TestExtractionClassificationTypePresenter;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class TestExtractionClassificationTypeView extends View<TestExtractionClassificationTypePresenter> {

	interface Binder extends UiBinder<Widget, TestExtractionClassificationTypeView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected VerticalLayoutContainer vPanel;

	@UiField
	protected HorizontalPanel hPanel;

	@UiField
	protected Label classificationTypeLabel;

	@UiField
	protected ComboBox classificationTypesComboBox;

	@UiField
	protected Label extractionModulesLabel;

	@UiField(provided = true)
	protected ListBox extractionModulesList;

	public TestExtractionClassificationTypeView() {
		super();
		initWidget(binder.createAndBindUi(this));
		this.classificationTypesComboBox.setEditable(false);
		setWidgetIds();
		initialiseView();
		hPanel.addStyleName("testExtractionHorizontalPanel");
		classificationTypeLabel.addStyleName("classificationTypeLabel");
		classificationTypesComboBox.addStyleName("classificationTypesComboBox");
		extractionModulesLabel.addStyleName("extractionModulesLabel");
		extractionModulesList.addStyleName("extractionModulesList");
	}

	private void initialiseView() {
		String classificationTypeLabelText = StringUtil.concatenate(
				LocaleDictionary.getConstantValue(BatchClassConstants.CLASSIFICATION_TYPES_LABEL), " ", BatchClassConstants.COLON);
		classificationTypeLabel.setText(classificationTypeLabelText);
		String extractionModulesLabelText = StringUtil.concatenate(
				LocaleDictionary.getConstantValue(BatchClassConstants.EXTRACTION_MODULES_LABEL), " ", BatchClassConstants.COLON);
		extractionModulesLabel.setText(extractionModulesLabelText);
		classificationTypesComboBox.add(AdminConstants.SEARCH_CLASSIFICATION);
		classificationTypesComboBox.add(AdminConstants.BARCODE_CLASSIFICATION);
		classificationTypesComboBox.add(AdminConstants.IMAGE_CLASSIFICATION);
		classificationTypesComboBox.setValue(AdminConstants.SEARCH_CLASSIFICATION, true);
		classificationTypesComboBox.setToolTip(classificationTypesComboBox.getValue());
		this.classificationTypesComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				classificationTypesComboBox.setToolTip(classificationTypesComboBox.getValue());
			}
		});
	}

	private void setWidgetIds() {
		WidgetUtil.setID(classificationTypeLabel, "testExtraction-classificationType-label");
		WidgetUtil.setID(classificationTypesComboBox, "testExtraction-classificationType-combobox");
		WidgetUtil.setID(extractionModulesLabel, "testExtraction-extractionModule-label");
		WidgetUtil.setID(extractionModulesList, "testExtraction-extractionModules-listbox");
	}

	@Override
	public void initialize() {
		this.extractionModulesList = new ListBox(true);
	}

	public ListBox getExtractionModulesList() {
		return extractionModulesList;
	}

	public void setExtractionModulesList(ListBox extractionModulesList) {
		this.extractionModulesList = extractionModulesList;
	}

	public String getClassificationType() {
		return classificationTypesComboBox.getValue();
	}

	public int getModuleListItemsCount() {
		return extractionModulesList.getItemCount();
	}

	public boolean isItemSelected(int isSelected) {
		return extractionModulesList.isItemSelected(isSelected);
	}

	public String getSelectedItem(int i) {
		return extractionModulesList.getItemText(i);
	}

	public void clearExtractionModuleList() {
		this.extractionModulesList.clear();
	}

	public ComboBox getClassificationTypesComboBox() {
		return classificationTypesComboBox;
	}

	
	
}
