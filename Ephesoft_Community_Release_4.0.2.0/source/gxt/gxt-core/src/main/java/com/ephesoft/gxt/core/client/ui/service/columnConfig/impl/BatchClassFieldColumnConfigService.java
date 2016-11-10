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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.shared.dto.BatchClassFieldDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.BatchClassFieldProperties;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

/**
 * This column config service deals with batch class field grid structure.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.BatchClassFieldColumnConfigService
 */
@SuppressWarnings("rawtypes")
public class BatchClassFieldColumnConfigService implements ColumnConfigService<BatchClassFieldDTO> {

	/** The properties. */
	BatchClassFieldProperties properties = BatchClassFieldProperties.properties;

	/** The column config list. */
	private List<ColumnConfig<BatchClassFieldDTO, ?>> columnConfigList;

	/** The editors map. */
	private Map<ColumnConfig, IsField> editorsMap;

	/**
	 * Instantiates a new batch class field column config service.
	 */
	public BatchClassFieldColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<BatchClassFieldDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();

		ValueProvider<BatchClassFieldDTO, String> validationPatternValueProvider = properties.validationPattern();
		final ColumnConfig<BatchClassFieldDTO, Boolean> modelSelector = new ColumnConfig<BatchClassFieldDTO, Boolean>(
				properties.selected());
		final CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_SELECT));
		modelSelector.setWidth(45);
		modelSelector.setFixed(true);
		modelSelector.setResizable(false);
		modelSelector.setSortable(false);
		modelSelector.setHideable(false);

		final ColumnConfig<BatchClassFieldDTO, String> nameColumn = new ColumnConfig<BatchClassFieldDTO, String>(properties.name());
		nameColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_NAME));
		nameColumn.setHideable(false);

		final ColumnConfig<BatchClassFieldDTO, String> descriptionColumn = new ColumnConfig<BatchClassFieldDTO, String>(
				properties.description());
		descriptionColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_DESCRIPTION));
		descriptionColumn.setHideable(false);

		final ColumnConfig<BatchClassFieldDTO, DataType> dataTypeColumn = new ColumnConfig<BatchClassFieldDTO, DataType>(
				properties.dataType());
		dataTypeColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_TYPE));
		dataTypeColumn.setHideable(false);

		final ColumnConfig<BatchClassFieldDTO, Integer> fieldOrderColumn = new ColumnConfig<BatchClassFieldDTO, Integer>(
				properties.fieldOrderNumber());
		fieldOrderColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_FIELD_ORDER));
		fieldOrderColumn.setHideable(false);

		final ColumnConfig<BatchClassFieldDTO, String> sampleValueColumn = new ColumnConfig<BatchClassFieldDTO, String>(
				properties.sampleValue());
		sampleValueColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_SAMPLE_VALUE));

		final ColumnConfig<BatchClassFieldDTO, String> validationPatternColumn = new ColumnConfig<BatchClassFieldDTO, String>(
				validationPatternValueProvider);
		validationPatternColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_VALIDATION_PATTERN));

		final ColumnConfig<BatchClassFieldDTO, String> fieldOptionListColumn = new ColumnConfig<BatchClassFieldDTO, String>(
				properties.fieldOptionValueList());
		fieldOptionListColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_FIELD_OPTION_VALUE_LIST));
		fieldOptionListColumn.setWidth(135);
		fieldOptionListColumn.setResizable(true);

		columnConfigList.add(modelSelector);
		columnConfigList.add(nameColumn);
		columnConfigList.add(descriptionColumn);
		columnConfigList.add(dataTypeColumn);
		columnConfigList.add(fieldOrderColumn);
		columnConfigList.add(sampleValueColumn);
		columnConfigList.add(validationPatternColumn);
		columnConfigList.add(fieldOptionListColumn);

		final LabelProvider<DataType> dataTypeLabelProvider = new LabelProvider<DataType>() {

			@Override
			public String getLabel(final DataType item) {
				return item.toString();
			}
		};

		final SimpleComboBox<DataType> dataTypeCombo = new SimpleComboBox<DataType>(dataTypeLabelProvider);
		dataTypeCombo.setTriggerAction(TriggerAction.ALL);
		dataTypeCombo.add(Arrays.asList(DataType.values()));

		editorsMap.put(nameColumn, new TextField());
		editorsMap.put(descriptionColumn, new TextField());
		editorsMap.put(dataTypeColumn, dataTypeCombo);
		editorsMap.put(fieldOrderColumn, new IntegerField());
		editorsMap.put(sampleValueColumn, new TextField());
		// editorsMap.put(validationPatternColumn, new TextField());
		editorsMap.put(fieldOptionListColumn, new TextField());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getColumnConfig()
	 */
	@Override
	public <V> List<ColumnConfig<BatchClassFieldDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getEditorsMap()
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public Map<ColumnConfig, IsField> getEditorsMap() {
		return editorsMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getKeyProvider()
	 */
	@Override
	public ModelKeyProvider<BatchClassFieldDTO> getKeyProvider() {
		return properties.identifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getFilters()
	 */
	@Override
	public List<Filter<BatchClassFieldDTO, ?>> getFilters() {
		return null;
	}
}
