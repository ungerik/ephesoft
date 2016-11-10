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

package com.ephesoft.gxt.admin.client.view.module;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.module.AddNewModulePresenter;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.MandatoryLabel;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.form.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.form.ListContainsValueValidator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class AddNewModuleView extends View<AddNewModulePresenter> {

	interface Binder extends UiBinder<VerticalPanel, AddNewModuleView> {
	}

	@UiField
	protected TextField descriptionTextField;
	@UiField
	protected TextField nameTextField;

	@UiField
	protected MandatoryLabel nameLabel;

	@UiField
	protected MandatoryLabel descLabel;

	@UiField
	protected HorizontalPanel addNewModulePanel;

	private DialogWindow dialogWindow;

	@UiField
	protected VerticalPanel addNewModuleViewPanel;

	private static final Binder BINDER = GWT.create(Binder.class);

	public AddNewModuleView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		nameTextField.setReadOnly(false);
		addNewModuleViewPanel.setSpacing(5);
		nameLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.NAME));
		descLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.DESCRIPTION));
		nameLabel.setStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		descLabel.setStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		addNewModulePanel.setSpacing(10);
		
		nameTextField.addStyleName("addModuleTextField");
		descriptionTextField.addStyleName("addModuleTextField");
		setWidgetIDs();
	}

	private void setWidgetIDs() {
		WidgetUtil.setID(nameLabel, "addModuleNameLabel");
		WidgetUtil.setID(descLabel, "addModuleDescLabel");
		WidgetUtil.setID(nameTextField, "addModuleNameField");
		WidgetUtil.setID(descriptionTextField, "addModuleDescField");
	}

	public void addValidators(){
		nameTextField.addValidator(new EmptyValueValidator(LocaleDictionary.getMessageValue(BatchClassMessages.MANDATORY_FIELDS_ERROR_MSG)));
		descriptionTextField.addValidator(new EmptyValueValidator(LocaleDictionary.getMessageValue(BatchClassMessages.MANDATORY_FIELDS_ERROR_MSG)));
		nameTextField.addValidator(presenter.getListValidator());
	}
	@Override
	public void initialize() {

	}

	public String getDescription() {
		return descriptionTextField.getValue();
	}

	public void setDescription(String description) {
		this.descriptionTextField.setValue(description);
	}

	public TextField getDescriptionTextBox() {
		return descriptionTextField;
	}

	public TextField getNameTextBox() {
		return nameTextField;
	}

	public String getName() {
		return nameTextField.getValue();
	}

	public void setName(String name) {
		this.nameTextField.setValue(name);
	}

	public DialogWindow getDialogWindow() {
		return dialogWindow;
	}

	public void setDialogWindow(DialogWindow dialogWindow) {
		this.dialogWindow = dialogWindow;
	}

}
