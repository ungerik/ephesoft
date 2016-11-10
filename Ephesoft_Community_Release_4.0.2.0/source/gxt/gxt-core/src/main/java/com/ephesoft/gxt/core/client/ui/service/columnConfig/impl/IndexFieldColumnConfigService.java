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

package com.ephesoft.gxt.core.client.ui.service.columnConfig.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.BarcodeType;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.ValidationOperator;
import com.ephesoft.dcma.core.common.WidgetType;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.client.ui.widget.util.UniqueIDProvider;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.IndexFieldProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FloatField;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

public class IndexFieldColumnConfigService implements ColumnConfigService<FieldTypeDTO> {

	private static final IndexFieldProperties properties = GWT.create(IndexFieldProperties.class);

	private List<ColumnConfig<FieldTypeDTO, ?>> columnConfigList;
	@SuppressWarnings("unchecked")
	private Map<ColumnConfig, IsField> editorsMap;

	@SuppressWarnings("unchecked")
	public IndexFieldColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<FieldTypeDTO, ?>>();

		editorsMap = new HashMap<ColumnConfig, IsField>();

		ValueProvider<FieldTypeDTO, String> patternValueProvider = properties.pattern();

		ColumnConfig<FieldTypeDTO, Boolean> modelSelector = new ColumnConfig<FieldTypeDTO, Boolean>(properties.selected());
		CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setHeader(".");
		modelSelector.setWidth(30);
		modelSelector.setFixed(true);
		modelSelector.setSortable(false);
		modelSelector.setHideable(false);

		ColumnConfig<FieldTypeDTO, String> nameColumn = new ColumnConfig<FieldTypeDTO, String>(properties.name());
		nameColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_NAME));
		nameColumn.setHideable(false);

		ColumnConfig<FieldTypeDTO, String> descriptionColumn = new ColumnConfig<FieldTypeDTO, String>(properties.description());
		descriptionColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_DESCRIPTION));
		descriptionColumn.setHideable(false);

		ColumnConfig<FieldTypeDTO, DataType> dataTypeValueColumn = new ColumnConfig<FieldTypeDTO, DataType>(properties.dataType());
		dataTypeValueColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.INDEX_FIELD_DATA_TYPE));
		ColumnConfig<FieldTypeDTO, String> patternValueColumn = new ColumnConfig<FieldTypeDTO, String>(patternValueProvider);
		patternValueColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_PATTERN));
		ColumnConfig<FieldTypeDTO, Float> ocrConfidenceThresholdValueColumn = new ColumnConfig<FieldTypeDTO, Float>(properties
				.ocrConfidenceThreshold());
		ocrConfidenceThresholdValueColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.INDEX_FIELD_OCR_THRESHOLD));

		ColumnConfig<FieldTypeDTO, WidgetType> widgetTypeValueColumn = new ColumnConfig<FieldTypeDTO, WidgetType>(properties
				.widgetType());
		widgetTypeValueColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.INDEX_FIELD_TYPE));
		ColumnConfig<FieldTypeDTO, String> categoryGroupColumn = new ColumnConfig<FieldTypeDTO, String>(properties.category());
		categoryGroupColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.INDEX_FIELD_CATEGORY));
		ColumnConfig<FieldTypeDTO, Integer> fieldOrderColummn = new ColumnConfig<FieldTypeDTO, Integer>(properties.fieldOrderNumber());
		fieldOrderColummn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.INDEX_FIELD_ORDER));
		fieldOrderColummn.setHideable(false);

		ColumnConfig<FieldTypeDTO, String> sampleValueColumn = new ColumnConfig<FieldTypeDTO, String>(properties.sampleValue());
		sampleValueColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.INDEX_FIELD_SAMPLE_VALUE));
		ColumnConfig<FieldTypeDTO, String> fieldOptionValuesList = new ColumnConfig<FieldTypeDTO, String>(properties
				.fieldOptionValueList());
		fieldOptionValuesList.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.INDEX_FIELD_OPTION_VALUES_LIST));
		ColumnConfig<FieldTypeDTO, String> barcodeColumn = new ColumnConfig<FieldTypeDTO, String>(properties.barcodeType());
		barcodeColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.INDEX_FIELD_BARCODE));
		ColumnConfig<FieldTypeDTO, Boolean> isHiddenColumn = new ColumnConfig<FieldTypeDTO, Boolean>(properties.hidden());
		isHiddenColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_HIDDEN));
		final CheckBoxCell isEnabledCell = new CheckBoxCell();
		isHiddenColumn.setWidth(45);
		isHiddenColumn.setFixed(true);
		isHiddenColumn.setSortable(false);
		isHiddenColumn.setResizable(false);
		isHiddenColumn.setCell(isEnabledCell);

		ColumnConfig<FieldTypeDTO, Boolean> isReadOnlyColumn = new ColumnConfig<FieldTypeDTO, Boolean>(properties.readOnly());
		isReadOnlyColumn.setHeader("Read Only ");
		isReadOnlyColumn.setCell(new CheckBoxCell());
		isReadOnlyColumn.setSortable(false);

		ColumnConfig<FieldTypeDTO, Boolean> isFieldValueChangeScriptColumn = new ColumnConfig<FieldTypeDTO, Boolean>(properties
				.fieldValueChangeScriptEnabled());
		isFieldValueChangeScriptColumn.setHeader("Field Value Change Script ");
		isFieldValueChangeScriptColumn.setCell(new CheckBoxCell());
		isFieldValueChangeScriptColumn.setSortable(false);

		ColumnConfig<FieldTypeDTO, String> validationOperatorColumn = new ColumnConfig<FieldTypeDTO, String>(properties
				.regexListingSeparator());
		validationOperatorColumn.setHeader("Validation Operator ");

		columnConfigList.add(modelSelector);
		columnConfigList.add(nameColumn);
		columnConfigList.add(descriptionColumn);
		columnConfigList.add(dataTypeValueColumn);
		columnConfigList.add(patternValueColumn);
		columnConfigList.add(ocrConfidenceThresholdValueColumn);
		columnConfigList.add(widgetTypeValueColumn);
		columnConfigList.add(categoryGroupColumn);
		columnConfigList.add(fieldOrderColummn);
		columnConfigList.add(sampleValueColumn);
		columnConfigList.add(fieldOptionValuesList);
		columnConfigList.add(barcodeColumn);
		columnConfigList.add(isHiddenColumn);
		columnConfigList.add(isReadOnlyColumn);
		columnConfigList.add(isFieldValueChangeScriptColumn);
		columnConfigList.add(validationOperatorColumn);

		editorsMap.put(nameColumn, new TextField());
		editorsMap.put(descriptionColumn, new TextField());
		// editorsMap.put(patternValueColumn, new TextField());
		editorsMap.put(ocrConfidenceThresholdValueColumn, new FloatField());
		editorsMap.put(categoryGroupColumn, new TextField());
		editorsMap.put(fieldOrderColummn, new IntegerField());
		editorsMap.put(sampleValueColumn, new TextField());
		editorsMap.put(barcodeColumn, new TextField());
		editorsMap.put(validationOperatorColumn, new TextField());
		editorsMap.put(fieldOptionValuesList, new TextField());
		ListStore<DataType> dataTypeStore = new ListStore<DataType>(new UniqueIDProvider<DataType>());
		dataTypeStore.addAll(DataType.getValuesAsListDataType());
		ComboBox<DataType> dataTypeComboBox = new ComboBox<DataType>(new ComboBoxCell<DataType>(dataTypeStore,
				new StringLabelProvider<DataType>()));
		editorsMap.put(dataTypeValueColumn, dataTypeComboBox);
		dataTypeComboBox.setTriggerAction(TriggerAction.ALL);
		final ListView<DataType, ?> listView = dataTypeComboBox.getListView();
		if (null != listView) {
			listView.addDomHandler(new MouseMoveHandler() {

				@Override
				public void onMouseMove(MouseMoveEvent event) {

					Element target = event.getNativeEvent().getEventTarget().<Element> cast();
					if (null != target) {
						target = listView.findElement(target);
						if (target != null) {
							int index = listView.indexOf(target);
							if (index != -1) {
								listView.setTitle(listView.getStore().get(index).name());
							}
						}
					}
				}
			}, MouseMoveEvent.getType());
		}
		
		ListStore<String> validatorTypeStore = new ListStore<String>(new UniqueIDProvider<String>());
		validatorTypeStore.addAll(ValidationOperator.getValuesAsListString());
		ComboBox<String> validatorTypeComboBox = new ComboBox<String>(new ComboBoxCell<String>(validatorTypeStore,
				new StringLabelProvider<String>()));
		validatorTypeComboBox.setTriggerAction(TriggerAction.ALL);
		final ListView<String, ?> listViewValidator = validatorTypeComboBox.getListView();
		if (null != listViewValidator) {
			listViewValidator.addDomHandler(new MouseMoveHandler() {

				@Override
				public void onMouseMove(MouseMoveEvent event) {

					Element target = event.getNativeEvent().getEventTarget().<Element> cast();
					if (null != target) {
						target = listViewValidator.findElement(target);
						if (target != null) {
							int index = listViewValidator.indexOf(target);
							if (index != -1) {
								listViewValidator.setTitle(listViewValidator.getStore().get(index));
							}
						}
					}
				}
			}, MouseMoveEvent.getType());
		}
		editorsMap.put(validationOperatorColumn, validatorTypeComboBox);

		ListStore<WidgetType> widgetTypeStore = new ListStore<WidgetType>(new UniqueIDProvider<WidgetType>());
		widgetTypeStore.addAll(WidgetType.getValuesAsListWidgetType());
		ComboBox<WidgetType> widgetTypeComboBox = new ComboBox<WidgetType>(new ComboBoxCell<WidgetType>(widgetTypeStore,
				new StringLabelProvider<WidgetType>()));
		widgetTypeComboBox.setTriggerAction(TriggerAction.ALL);
		final ListView<WidgetType, ?> listViewWidget = widgetTypeComboBox.getListView();
		if (null != listViewWidget) {
			listViewWidget.addDomHandler(new MouseMoveHandler() {

				@Override
				public void onMouseMove(MouseMoveEvent event) {

					Element target = event.getNativeEvent().getEventTarget().<Element> cast();
					if (null != target) {
						target = listViewWidget.findElement(target);
						if (target != null) {
							int index = listViewWidget.indexOf(target);
							if (index != -1) {
								listViewWidget.setTitle(listViewWidget.getStore().get(index).name());
							}
						}
					}
				}
			}, MouseMoveEvent.getType());
		}
		editorsMap.put(widgetTypeValueColumn, widgetTypeComboBox);

		ListStore<String> barcodeTypeStore = new ListStore<String>(new UniqueIDProvider<String>());
		barcodeTypeStore.addAll(BarcodeType.getValuesAsListString());
		ComboBox<String> barcodeTypeComboBox = new ComboBox<String>(new ComboBoxCell<String>(barcodeTypeStore,
				new StringLabelProvider<String>()));
		final ListView<String, ?> listViewBarcode = barcodeTypeComboBox.getListView();
		if (null != listViewBarcode) {
			listViewBarcode.addDomHandler(new MouseMoveHandler() {

				@Override
				public void onMouseMove(MouseMoveEvent event) {

					Element target = event.getNativeEvent().getEventTarget().<Element> cast();
					if (null != target) {
						target = listViewBarcode.findElement(target);
						if (target != null) {
							int index = listViewBarcode.indexOf(target);
							if (index != -1) {
								listViewBarcode.setTitle(listViewBarcode.getStore().get(index));
							}
						}
					}
				}
			}, MouseMoveEvent.getType());
		}
		editorsMap.put(barcodeColumn, barcodeTypeComboBox);
	}

	@Override
	public <V> List<ColumnConfig<FieldTypeDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	@Override
	public Map<ColumnConfig, IsField> getEditorsMap() {
		return editorsMap;
	}

	@Override
	public ModelKeyProvider<FieldTypeDTO> getKeyProvider() {
		return IndexFieldProperties.properties.identifier();
	}

	@Override
	public List<Filter<FieldTypeDTO, ?>> getFilters() {
		return null;
	}

}
