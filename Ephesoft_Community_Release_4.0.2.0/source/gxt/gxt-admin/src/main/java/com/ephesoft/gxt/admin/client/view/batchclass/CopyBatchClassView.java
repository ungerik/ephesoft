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

package com.ephesoft.gxt.admin.client.view.batchclass;

import java.util.List;

import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.batchclass.CopyBatchClassPresenter;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.MandatoryLabel;
import com.ephesoft.gxt.core.client.validator.form.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.form.SpecialCharacterValidator;
import com.ephesoft.gxt.core.client.validator.form.UniqueNameValidator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
//import com.sencha.gxt.widget.core.client.Slider;
import com.ephesoft.gxt.core.client.ui.widget.Slider;
import com.sencha.gxt.widget.core.client.form.TextField;

public class CopyBatchClassView extends View<CopyBatchClassPresenter> {

	interface Binder extends UiBinder<VerticalPanel, CopyBatchClassView> {
	}

	@UiField
	protected Slider prioritySlider;
	@UiField
	protected TextField prioritySliderTextBox;

	private int sliderPreviousValue;

	@UiField
	protected TextField uncFolder;
	@UiField
	protected TextField description;
	@UiField
	protected TextField name;

	@UiField
	protected MandatoryLabel priorityLabel;
	@UiField
	protected MandatoryLabel descLabel;
	@UiField
	protected MandatoryLabel nameLabel;
	@UiField
	protected MandatoryLabel uncLabel;

	// @UiField
	// protected HorizontalPanel editBatchPanel;

	@UiField
	protected Label gridWorkflowLabel;

	@UiField
	protected CheckBox gridCheckBox;

	private DialogWindow dialogWindow;

	@UiField
	protected VerticalPanel editBatchClassViewPanel;

	private UniqueNameValidator uncFolderUniqueValidator;

	private UniqueNameValidator bcNameUniqueValidator;

	private SpecialCharacterValidator bcNameSpecialCharacterValidator;

	private static final Binder BINDER = GWT.create(Binder.class);

	public CopyBatchClassView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		name.setReadOnly(false);
		editBatchClassViewPanel.setSpacing(5);

		nameLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.NAME));
		priorityLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.PRIORITY));
		descLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.DESCRIPTION));
		uncLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.UNC_FOLDER));
		gridWorkflowLabel.setVisible(false);
		gridWorkflowLabel.setText(LocaleDictionary.getConstantValue(BatchClassConstants.GRID_WORKFLOW) + AdminConstants.COLON);

		nameLabel.setStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		priorityLabel.setStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		descLabel.setStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		uncLabel.setStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		gridWorkflowLabel.setStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		//prioritySlider.setPixelSize(150, 24);
		// editBatchPanel.setSpacing(10);
		prioritySlider.addStyleName("prioritySliderCopy");
		gridCheckBox.setVisible(false);
		gridCheckBox.setTitle(LocaleDictionary.getMessageValue(BatchClassMessages.GRID_WORKFLOW_CHECK_BOX_TITLE));
		addValidators();

		prioritySliderTextBox.addStyleName("copyBatchSliderTextBox");
		prioritySliderTextBox.setReadOnly(true);
		prioritySliderTextBox.setEnabled(false);
		this.addPrioritySliderMoveHandler();
	}

	private void addValidators() {
		final String emptyMessage = LocaleDictionary.getMessageValue(BatchClassMessages.VALUE_CANNOT_BE_EMPTY);
		name.addValidator(new EmptyValueValidator(emptyMessage));
		description.addValidator(new EmptyValueValidator(emptyMessage));
		uncFolder.addValidator(new EmptyValueValidator(emptyMessage));
		uncFolderUniqueValidator = new UniqueNameValidator(LocaleDictionary.getMessageValue(BatchClassMessages.UNC_PATH_NOT_UNIQUE));
		bcNameUniqueValidator = new UniqueNameValidator(
				LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_NAME_NOT_UNIQUE));
		bcNameSpecialCharacterValidator = new SpecialCharacterValidator(
				LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_CHARACTER_IN_NAME_MESSAGE));
		name.addValidator(bcNameUniqueValidator);
		uncFolder.addValidator(uncFolderUniqueValidator);
		name.addValidator(bcNameSpecialCharacterValidator);
	}

	public int getPrioritySlider() {
		return prioritySlider.getValue();
	}

	public void setPrioritySlider(int priority) {
		this.prioritySlider.setValue(priority);
	}

	public String getUncFolder() {
		return uncFolder.getValue().trim();
	}

	public void setUncFolder(String uncFolder) {
		this.uncFolder.setValue(uncFolder);
	}

	public String getDescription() {
		return description.getValue().trim();
	}

	public void setDescription(String description) {
		this.description.setValue(description);
	}

	public String getName() {
		return name.getValue().trim();
	}

	public void setName(String name) {
		this.name.setValue(name);
	}

	/**
	 * @return the dialogWindow
	 */
	public DialogWindow getDialogWindow() {
		return dialogWindow;
	}

	/**
	 * @param dialogWindow the dialogWindow to set
	 */
	public void setDialogWindow(DialogWindow dialogWindow) {
		this.dialogWindow = dialogWindow;
	}

	public Boolean getGridCheckBoxValue() {
		return gridCheckBox.getValue();
	}

	public void setGridCheckBoxValue(Boolean isGridWorkflow) {
		gridCheckBox.setValue(isGridWorkflow);
	}

	public CheckBox getGridCheckBox() {
		return gridCheckBox;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	public String getPrioritySliderString() {
		return prioritySlider.getValue().toString();
	}

	public TextField getPrioritySliderTextBox() {
		return prioritySliderTextBox;
	}

	public String getprioritySliderValue() {
		return prioritySliderTextBox.getText();
	}

	public void setprioritySliderValue(String priorityValue) {
		prioritySliderTextBox.setText(priorityValue);
	}

	public void addPrioritySliderMoveHandler() {

		prioritySlider.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				setprioritySliderValue(prioritySlider.getValue().toString());
			}
		});

	}

	public boolean validateInputs() {
		boolean valid = true;
		if (!(name.isValid() && description.isValid() && uncFolder.isValid())) {
			valid = false;
		}
		return valid;
	}

	public void fireValidations() {
		name.validate();
		description.validate();
		uncFolder.validate();
	}

	public void clearInvalids() {
		name.clearInvalid();
		description.clearInvalid();
		uncFolder.clearInvalid();
	}

	public void setUncFolderUniqueValidator(List<String> listOfValues) {
		this.uncFolderUniqueValidator.setListOfString(listOfValues);
	}

	public void setBcNameUniqueValidator(List<String> listOfValues) {
		this.bcNameUniqueValidator.setListOfString(listOfValues);
	}

	public void setBcNameSpecialCharacterValidator(String invalidSpecialChars) {
		this.bcNameSpecialCharacterValidator.setInvalidSpecialChars(invalidSpecialChars);
	}

	public void setInvalidCharacterMessage(String invalidCharacters) {
		bcNameSpecialCharacterValidator.setMessage(LocaleDictionary.getMessageValue(
				BatchClassMessages.INVALID_CHARACTER_IN_BCNAME_MESSAGE, invalidCharacters));
	}
	
	public TextField getNameTextField(){
		return name;
	}
}
