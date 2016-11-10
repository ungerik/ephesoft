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

package com.ephesoft.gxt.uploadbatch.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.validator.form.RegexMatcherValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.BatchClassFieldDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.uploadbatch.client.controller.UploadBatchController;
import com.ephesoft.gxt.uploadbatch.client.event.StartBatchEvent;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchMessages;
import com.ephesoft.gxt.uploadbatch.client.view.AssociateBCFView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.form.TextField;

public class AssociateBCFPresenter extends BasePresenter<UploadBatchController, AssociateBCFView> {

	private List<EditableWidgetStorage> docFieldWidgets;

	private List<BatchClassFieldDTO> batchClassFieldDTOs;

	private boolean fieldsValid;

	RegexMatcherValidator regexValidator = null;

	/**
	 * Boolean variable to check whether the start batch button is clicked or not.
	 */
	private boolean onStartBatchClicked = false;

	/**
	 * Boolean variable to check whether the start batch button is clicked or not.
	 */
	private boolean batchClassFeildsSerialized = false;

	public AssociateBCFPresenter(final UploadBatchController controller, final AssociateBCFView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
		view.setView();
		setProperties();
	}

	public void onSave() {
		validateBatchClassFields();
		int index = 0;
		for (BatchClassFieldDTO batchClassFieldDTO : batchClassFieldDTOs) {
			EditableWidgetStorage widget = docFieldWidgets.get(index);
			if (widget.isComboBox()) {
				batchClassFieldDTO.setValue(widget.getComboBoxwidget().getValue());
			} else {
				batchClassFieldDTO.setValue(widget.getTextBoxWidget().getValue());
			}
			index++;
		}
		controller.setBatchClassFieldDTOs(batchClassFieldDTOs);
		if (fieldsValid) {
			view.hideDialogBox(true);
			batchClassFeildsSerialized = true;
			if (onStartBatchClicked) {
				controller.getEventBus().fireEvent(new StartBatchEvent());
			}
		}
	}

	public void validateBatchClassFields() {
		if (null != docFieldWidgets) {
			for (EditableWidgetStorage widget : docFieldWidgets) {
				if (widget.isComboBox()) {
					fieldsValid = widget.getComboBoxwidget().isValid();
				} else {
					if (!StringUtil.isNullOrEmpty(widget.getValidationPattern())) {
						fieldsValid = widget.getTextBoxWidget().isValid();
					} else {
						fieldsValid = true;
					}
				}

				if (!fieldsValid) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(UploadBatchMessages.INCORRECT_VALUE_FOR_BATCH_CLASS_FIELD),
							DialogIcon.ERROR);
					// if (!widget.isComboBox() && onStartBatchClicked) {
					// DialogUtil.showConfirmationDialog(
					// LocaleDictionary.getLocaleDictionary().getConstantValue("Error"),
					// LocaleDictionary.getLocaleDictionary().getMessageValue("Invalid Regex Pattern"));
					// view.hideDialogBox(false);
					// }
					break;
				}
			}
		}
	}

	private void setProperties() {
		if (!CollectionUtil.isEmpty(batchClassFieldDTOs)) {
			docFieldWidgets = new ArrayList<EditableWidgetStorage>(batchClassFieldDTOs.size());
			int row = 0;
			for (final BatchClassFieldDTO batchClassFieldDTO : batchClassFieldDTOs) {
				view.formatRow(row);
				view.addWidget(row, 0,
						new Label(StringUtil.concatenate(batchClassFieldDTO.getDescription(), CoreCommonConstant.COLON)));

				if (!StringUtil.isNullOrEmpty(batchClassFieldDTO.getFieldOptionValueList())) {
					// Create a drop down
					final ComboBox fieldValue = view.addDropDown(batchClassFieldDTO);
					view.addWidget(row, 1, fieldValue);
					if (StringUtil.isNullOrEmpty(batchClassFieldDTO.getValue())) {
						fieldValue.setValue(batchClassFieldDTO.getValue());
					}
					docFieldWidgets.add(new EditableWidgetStorage(fieldValue));
				} else {
					// Create a text box
					final TextField validatableTextBox = view.addTextBox(batchClassFieldDTO);
					view.addWidget(row, 1, validatableTextBox);
					if (StringUtil.isNullOrEmpty(batchClassFieldDTO.getValue())) {
						validatableTextBox.setValue(batchClassFieldDTO.getValue());
					}

					docFieldWidgets.add(new EditableWidgetStorage(validatableTextBox, batchClassFieldDTO.getValidationPattern()));
				}
				row++;
			}
			if (!CollectionUtil.isEmpty(docFieldWidgets)) {
				EditableWidgetStorage widget = docFieldWidgets.get(0);
				if (null != widget.getTextBoxWidget()) {
					view.getDialogBox().setFocusWidget(widget.getTextBoxWidget());
				} else if (null != widget.getComboBoxwidget()) {
					view.getDialogBox().setFocusWidget(widget.getComboBoxwidget());
				}
			}
		}
	}

	public void clearBatchClassFieldValues() {
		docFieldWidgets = null;
		controller.setBatchClassFieldDTOs(null);
	}

	private static class EditableWidgetStorage {

		private TextField widget;
		private ComboBox comboBoxwidget;
		private final boolean isComboBox;
		private String validationPattern;

		public EditableWidgetStorage(final TextField widget, final String validationPattern) {
			this.widget = widget;
			this.isComboBox = false;
			this.validationPattern = validationPattern;
		}

		public EditableWidgetStorage(final ComboBox comboBoxwidget) {
			this.comboBoxwidget = comboBoxwidget;
			this.isComboBox = true;
			this.validationPattern = null;
		}

		public TextField getTextBoxWidget() {
			return widget;
		}

		public ComboBox getComboBoxwidget() {
			return comboBoxwidget;
		}

		public boolean isComboBox() {
			return isComboBox;
		}

		public String getValidationPattern() {
			return validationPattern;
		}

		public void setValidationPattern(String validationPattern) {
			this.validationPattern = validationPattern;
		}

	}

	public void showAssociateBCFView() {
		view.getDialogBox().add(view);
		view.getDialogBox().setHeadingText(LocaleDictionary.getConstantValue(UploadBatchConstants.BATCH_CLASS_FIELDS));
		view.getDialogBox().show();
		view.getScrollPanel().scrollToTop();
	}

	public List<BatchClassFieldDTO> getBatchClassFieldDTOs() {
		return batchClassFieldDTOs;
	}

	@Override
	public void injectEvents(final EventBus eventBus) {

	}

	public void setBatchClassFieldDTOs(final List<BatchClassFieldDTO> batchClassFieldDTOs) {
		this.batchClassFieldDTOs = batchClassFieldDTOs;
	}

	public void setAssociatedBCFVeiwDialogBox(final DialogWindow dialogBox) {
		if (null != dialogBox) {
			dialogBox.setPreferCookieDimension(false);
			view.setDialogBox(dialogBox);
		}
	}

	/**
	 * Returns <code>true</code> if onFinish is true otherwise returns <code>false</code>.
	 * 
	 * @return boolean- returns true or false
	 */
	public boolean isStartBatchClicked() {
		return onStartBatchClicked;
	}

	/**
	 * Sets onFinish to <code>true</code> or <code>false</code>.
	 * 
	 * @param onFinish boolean- the onFinish takes value true or false depending on whether the finish button is clicked or not
	 */
	public void setOnStartBatchClicked(boolean onFinish) {
		this.onStartBatchClicked = onFinish;
	}

	public boolean isFieldsValid() {
		return fieldsValid;
	}

	public void setFieldsValid(boolean fieldsValid) {
		this.fieldsValid = fieldsValid;
	}

	public boolean isBatchClassFeildsSerialized() {
		return batchClassFeildsSerialized;
	}

	public void setBatchClassFeildsSerialized(boolean batchClassFeildsSerialized) {
		this.batchClassFeildsSerialized = batchClassFeildsSerialized;
	}
}
