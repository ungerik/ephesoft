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

package com.ephesoft.gxt.admin.client.view.plugin;

import java.util.List;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.i18n.PluginNameConstants;
import com.ephesoft.gxt.admin.client.presenter.plugin.PluginDetailPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.MandatoryLabel;
import com.ephesoft.gxt.core.client.validator.form.DateFormatValidator;
import com.ephesoft.gxt.core.client.validator.form.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.form.NumberValidator;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginConfigDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class PluginDetailView extends BatchClassInlineView<PluginDetailPresenter> {

	interface Binder extends UiBinder<Widget, PluginDetailView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	ScrollPanel scrollPanel;
	@UiField
	FieldSet fieldSet;
	VerticalLayoutContainer vlayoutContainer;

	public PluginDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
	}

	public void initializePanel() {
		fieldSet.setHeadingText(LocaleDictionary.getConstantValue(BatchClassConstants.PLUGIN_CONFIGURATION));
		fieldSet.setCollapsible(false);
		scrollPanel = new ScrollPanel();
		fieldSet.add(scrollPanel);
		vlayoutContainer = new VerticalLayoutContainer();
		scrollPanel.add(vlayoutContainer);
		fieldSet.addStyleName("fieldSet");
		vlayoutContainer.addStyleName("pluginVerticalLayout");
		scrollPanel.addStyleName("pluginDetailScrollPanel");
	}

	public void addNoFieldExists() {
		HorizontalPanel cont = new HorizontalPanel();
		cont.addStyleName("pluginPanel");
		Label label = new Label();
		label.setText(LocaleDictionary.getConstantValue(BatchClassConstants.NO_FIELD_EXISTS));
		label.addStyleName("noPluginLabel");
		cont.add(label);
		vlayoutContainer.add(cont);
	}

	public void addWidget(String labelText, Widget widget, boolean isMandatory) {
		HorizontalPanel cont = new HorizontalPanel();
		cont.addStyleName("pluginPanel");
		if (isMandatory) {
			MandatoryLabel label = new MandatoryLabel();
			label.setLabelText(labelText);
			label.addStyleName("pluginLabel");
			cont.add(label);
		} else {
			labelText = StringUtil.concatenate(labelText, AdminConstants.COLON);
			Label label = new Label();
			label.setText(labelText);
			label.addStyleName("pluginLabel");
			cont.add(label);
		}
		widget.addStyleName("widgetPluginLabel");
		cont.add(widget);
		vlayoutContainer.add(cont);
	}

	public ListView addMultipleSelectListBox(List<String> sampleValueList, int MAX_VISIBLE_ITEM_COUNT, String value) {
		ListStore<String> listStore = new ListStore<String>(new ModelKeyProvider<String>() {

			@Override
			public String getKey(String item) {
				return item;
			}
		});
		ListView<String, String> fieldValue = new ListView<String, String>(listStore, new ValueProvider<String, String>() {

			@Override
			public String getValue(String object) {
				return object;
			}

			@Override
			public void setValue(String object, String value) {

			}

			@Override
			public String getPath() {
				return null;
			}
		});

		for (String item : sampleValueList) {
			fieldValue.getStore().add(item);
		}
		String[] selectedValue = value.split(";");
		for (String string : selectedValue) {
			fieldValue.getSelectionModel().select(sampleValueList.indexOf(string), true);
		}

		fieldValue.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<String>() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent<String> event) {
				presenter.getController().getSelectedBatchClass().setDirty(true);
			}
		});
		
		return fieldValue;
	}

	public ComboBox addDropDown(List<String> sampleValueList, String selectedValue) {
		final ComboBox fieldValue = new ComboBox();
		fieldValue.getElement().applyStyles("pluginComboBox");
		fieldValue.setEditable(true);
		fieldValue.setAllowBlank(false);
		fieldValue.setForceSelection(true);
		fieldValue.setTypeAhead(true);
		ListStore<String> listStore = fieldValue.getStore();
		for (String item : sampleValueList) {
			listStore.add(item);
		}
		int index=listStore.indexOf(selectedValue);
		if(index!=-1){
			fieldValue.setValue(listStore.get(index), true);
		}
		
		fieldValue.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				presenter.getController().getSelectedBatchClass().setDirty(true);
			}
		});
		
		return fieldValue;
	}

	public Slider addConfidenceSlider(final BatchClassPluginConfigDTO dto, boolean readOnly) {
		Slider fieldValue = new Slider();
		fieldValue.setMaxValue(100);
		fieldValue.setMinValue(0);
		fieldValue.setIncrement(1);
		fieldValue.setValue(Integer.valueOf(dto.getValue()), true);
		fieldValue.setEnabled(!readOnly);
		fieldValue.setName(dto.getPluginConfig().getFieldName());
		return fieldValue;

	}

	public TextField addTextField(final BatchClassPluginConfigDTO dto, boolean readOnly) {
		TextField fieldValue = new TextField();
		fieldValue.setEnabled(!readOnly);
		fieldValue.setReadOnly(readOnly);
		fieldValue.setWidth("160px");
		fieldValue.getElement().applyStyles("pluginTextField");
		fieldValue.setName(dto.getPluginConfig().getFieldName());

		if (!readOnly) {
			if (dto.getPluginConfig() != null) {
				AbstractValidator<String> validator = getValidator(dto.getDataType(), dto.isMandatory());
				if (validator != null) {
					fieldValue.addValidator(validator);

				}
				if (fieldValue.getName().equalsIgnoreCase(PluginNameConstants.DA_PREDEFINED_DOC_TYPE)
						|| fieldValue.getName().equalsIgnoreCase(PluginNameConstants.DA_UNKNOWN_PREDEFINED_DOC_TYPE)) {
					presenter.addValidatorForDocumentField(fieldValue);
				}
			}
		}
		if(dto.getValue()!=null){
			fieldValue.setValue(dto.getValue(), true);
		}
		
		fieldValue.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				presenter.getController().getSelectedBatchClass().setDirty(true);
			}
		});
		
		return fieldValue;
	}

	public PasswordField addPasswordTextBox(final BatchClassPluginConfigDTO batchClassPluginConfigDto, boolean readOnly) {
		PasswordField fieldValue = new PasswordField();
		fieldValue.setEnabled(!readOnly);
		fieldValue.setReadOnly(readOnly);
		fieldValue.setWidth("160px");
		fieldValue.setName(batchClassPluginConfigDto.getPluginConfig().getFieldName());
		if (!readOnly) {
			if (batchClassPluginConfigDto.getPluginConfig() != null) {
				AbstractValidator<String> validator = getValidator(batchClassPluginConfigDto.getDataType(),
						batchClassPluginConfigDto.isMandatory());
				if (validator != null) {
					fieldValue.addValidator(validator);
				}
			}
		}
		if(batchClassPluginConfigDto.getValue()!=null){
			fieldValue.setValue(batchClassPluginConfigDto.getValue(), true);
		}
		
		fieldValue.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				presenter.getController().getSelectedBatchClass().setDirty(true);
			}
		});
		
		return fieldValue;
	}

	public AbstractValidator<String> getValidator(DataType dataType, boolean isMandatory) {
		AbstractValidator<String> typeValidator = null;
		switch (dataType) {
			case DATE:
				typeValidator = new DateFormatValidator(isMandatory, StringUtil.concatenate(
						LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_DATE_FORMAT),
						LocaleDictionary.getMessageValue(BatchClassMessages.VALID_DATE_FORMAT_IS)),
						LocaleDictionary.getConstantValue(BatchClassConstants.MANDATORY_FIELDS_ERROR_MSG));
				break;

			case DOUBLE:
				typeValidator = new NumberValidator(Double.class, isMandatory,
						LocaleDictionary.getMessageValue(BatchClassMessages.VALUE_NOT_COMPLIANT_WITH_FIELD_TYPE),
						LocaleDictionary.getConstantValue(BatchClassConstants.MANDATORY_FIELDS_ERROR_MSG));
				break;

			case FLOAT:
				typeValidator = new NumberValidator(Float.class, isMandatory,
						LocaleDictionary.getMessageValue(BatchClassMessages.VALUE_NOT_COMPLIANT_WITH_FIELD_TYPE),
						LocaleDictionary.getConstantValue(BatchClassConstants.MANDATORY_FIELDS_ERROR_MSG));
				break;

			case LONG:
				typeValidator = new NumberValidator(Long.class, isMandatory,
						LocaleDictionary.getMessageValue(BatchClassMessages.VALUE_NOT_COMPLIANT_WITH_FIELD_TYPE),
						LocaleDictionary.getConstantValue(BatchClassConstants.MANDATORY_FIELDS_ERROR_MSG));
				break;

			case INTEGER:
				typeValidator = new NumberValidator(Integer.class, isMandatory,
						LocaleDictionary.getMessageValue(BatchClassMessages.VALUE_NOT_COMPLIANT_WITH_FIELD_TYPE),
						LocaleDictionary.getConstantValue(BatchClassConstants.MANDATORY_FIELDS_ERROR_MSG));
				break;

			case STRING:
				if (isMandatory) {
					typeValidator = new EmptyValueValidator(
							LocaleDictionary.getConstantValue(BatchClassConstants.MANDATORY_FIELDS_ERROR_MSG));
				}
				break;
			case PASSWORD:
				typeValidator = new EmptyValueValidator(
						LocaleDictionary.getConstantValue(BatchClassConstants.MANDATORY_FIELDS_ERROR_MSG));
				break;

			default:
				break;
		}
		return typeValidator;
	}

	@Override
	public void refresh() {
	}
}
