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

package com.ephesoft.gxt.rv.client.view.batch;

import java.util.List;

import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.core.common.WidgetType;
import com.ephesoft.gxt.core.client.DomainView;
import com.ephesoft.gxt.core.client.ui.widget.property.Validatable;
import com.ephesoft.gxt.core.client.validator.impl.RegexValidator;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.RegexDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.presenter.batch.DLFPresenter;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.DLFMultilineBox;
import com.ephesoft.gxt.rv.client.widget.DLFSuggestionBox;
import com.ephesoft.gxt.rv.client.widget.DatePickerWidget;
import com.ephesoft.gxt.rv.client.widget.ValidatableCheckBox;
import com.ephesoft.gxt.rv.client.widget.ValidatableMultiSelectListView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;

public class DLFView extends DomainView<DocField, DLFPresenter> {

	interface Binder extends UiBinder<Widget, DLFView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private Validatable widget;

	@UiField
	protected VerticalPanel documentLevelFieldPanel;

	@UiField
	protected Label dlfNameLabel;
	
	private Timer labelTextTimer ;

	public DLFView(final DocField bindedObject) {
		super(bindedObject);
		initWidget(binder.createAndBindUi(this));
		this.addFieldValueWidget(bindedObject);
	}
	
	@Override
	protected void onDetach() {
		super.onDetach();
		if(null != labelTextTimer) {
			labelTextTimer.cancel();
		}
	}

	private void addFieldValueWidget(final DocField docField) {
		if (null != docField) {
			Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			DocumentTypeDTO docType = ReviewValidateNavigator.getDocumentTypeForDocument(currentDocument);
			FieldTypeDTO fieldType = ReviewValidateNavigator.getField(docField, docType);
			if (null != fieldType) {
				dlfNameLabel.setText(fieldType.getDescription());
				WidgetType widgetType = fieldType.getWidgetType();
				String fieldOptionValueList = fieldType.getFieldOptionValueList();
				if ((widgetType == WidgetType.LIST || widgetType == WidgetType.COMBO)
						&& StringUtil.isNullOrEmpty(fieldOptionValueList)) {
					widgetType = WidgetType.TEXT;
				}
				switch (widgetType) {
					case DATE:
						DatePickerWidget datePickerBox = new DatePickerWidget(docField);
						DLFSuggestionBox suggestionBox = datePickerBox.getSuggestionBox();
						addValidationMetadata(fieldType, suggestionBox);
						widget = datePickerBox;
						break;
					case LIST:
						ValidatableMultiSelectListView listView = new ValidatableMultiSelectListView(docField);
						String fieldValuesOptionsList = fieldOptionValueList;
						listView.addFieldValueOptions(fieldValuesOptionsList);
						listView.setValues(docField.getValue());
						listView.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
						listView.setValidationRulesORing("OR".equalsIgnoreCase(fieldType.getRegexListingSeparator()));
						setValidator(listView, fieldType);
						widget = listView;
						break;
					case CHECKBOX:
						ValidatableCheckBox checkBox = new ValidatableCheckBox(docField);
						checkBox.setValidationRulesORing("OR".equalsIgnoreCase(fieldType.getRegexListingSeparator()));
						setValidator(checkBox, fieldType);
						widget = checkBox;
						break;
					case MULTILINE:
						widget = new DLFMultilineBox(docField);
						break;
					case TEXT:
						DLFSuggestionBox textBox = new DLFSuggestionBox(docField);
						addValidationMetadata(fieldType, textBox);
						widget = textBox;
						break;

					case COMBO:
						DLFSuggestionBox suggestBox = new DLFSuggestionBox(docField);
						addValidationMetadata(fieldType, suggestBox);
						suggestBox.setFieldValueOptions(fieldOptionValueList);
						widget = suggestBox;
						break;
				}
			}
			
			labelTextTimer = new Timer() {

				@Override
				public void run() {
					documentLevelFieldPanel.add(widget);
				}
			};
			labelTextTimer.schedule(1);
		}
	}

	private void addValidationMetadata(FieldTypeDTO fieldType, DLFSuggestionBox suggestionBox) {
		suggestionBox.setValidationRulesORing("OR".equalsIgnoreCase(fieldType.getRegexListingSeparator()));
		setValidator(suggestionBox, fieldType);
	}

	private void setValidator(DLFSuggestionBox suggestionBox, FieldTypeDTO field) {
		List<RegexDTO> regexList = field.getRegexList();
		if (!CollectionUtil.isEmpty(regexList)) {
			for (RegexDTO regexDTO : regexList) {
				if (null != regexDTO) {
					suggestionBox.addValidator(new RegexValidator(regexDTO.getPattern()));
				}
			}
		}
	}

	private void setValidator(ValidatableCheckBox checkBox, FieldTypeDTO field) {
		List<RegexDTO> regexList = field.getRegexList();
		if (!CollectionUtil.isEmpty(regexList)) {
			for (RegexDTO regexDTO : regexList) {
				if (null != regexDTO) {
					checkBox.addValidator(new RegexValidator(regexDTO.getPattern()));
				}
			}
		}
	}

	private void setValidator(ValidatableMultiSelectListView listView, FieldTypeDTO field) {
		List<RegexDTO> regexList = field.getRegexList();
		if (!CollectionUtil.isEmpty(regexList)) {
			for (RegexDTO regexDTO : regexList) {
				if (null != regexDTO) {
					listView.addValidator(new RegexValidator(regexDTO.getPattern()));
				}
			}
		}
	}

	@Override
	public boolean isValid() {
		return widget == null ? true : widget.isValid();
	}

	@Override
	public void focus() {
		if (widget instanceof Widget) {
			final Widget inputWidget = (Widget) (widget);
			if (inputWidget.isAttached()) {
				setFocus();
			} else {
				Timer timer = new Timer() {

					@Override
					public void run() {
						if (inputWidget.isAttached()) {
							cancel();
							setFocus();
						}
					}

				};
				timer.scheduleRepeating(20);
			}
		}
	}

	private void setFocus() {
		if (null != widget) {
			Widget bindedWidget = widget.asWidget();
			if (bindedWidget instanceof Validatable) {
				Validatable validatable = ReviewValidateNavigator.getLastSelectedWidget();
				if (null != validatable) {
					validatable.blur();
				}
				scheduleFocus((Validatable) bindedWidget);
			}
		}
	}

	private void scheduleFocus(final Validatable validatableWidget) {
		validatableWidget.focus();
		if (validatableWidget instanceof Widget) {
			Timer timer = new Timer() {

				@Override
				public void run() {
					((Widget) validatableWidget).getElement().scrollIntoView();
				}
			};
			timer.schedule(30);
		}
	}

	@Override
	public boolean canFocus() {
		DocField bindedObject = getBindedObject();
		return bindedObject == null ? true : !bindedObject.isReadOnly();
	}
}
