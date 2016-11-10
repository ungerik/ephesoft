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

import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.shared.constant.KeyFuzziness;
import com.ephesoft.gxt.core.shared.dto.KVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.KVExtractionProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.valueprovider.KeyFuzzinessValueProvider;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.FloatField;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

public class KVExtractionColumnConfigService implements ColumnConfigService<KVExtractionDTO> {

	/** The properties. */
	KVExtractionProperties properties = KVExtractionProperties.properties;

	/** The column config list. */
	private List<ColumnConfig<KVExtractionDTO, ?>> columnConfigList;

	/** The editors map. */
	private Map<ColumnConfig, IsField> editorsMap;

	/**
	 * Instantiates a new email list column config service.
	 */
	public KVExtractionColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<KVExtractionDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();

		ValueProvider<KVExtractionDTO, String> keyPatternValueProvider = properties.keyPattern();
		ValueProvider<KVExtractionDTO, String> valuePatternValueProvider = properties.valuePattern();

		ColumnConfig<KVExtractionDTO, Boolean> modelSelector = new ColumnConfig<KVExtractionDTO, Boolean>(properties.selected());
		CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_SELECT));
		modelSelector.setWidth(35);
		modelSelector.setFixed(true);
		modelSelector.setResizable(false);
		modelSelector.setSortable(false);
		modelSelector.setHideable(false);

		ColumnConfig<KVExtractionDTO, String> keyPatternColumn = new ColumnConfig<KVExtractionDTO, String>(keyPatternValueProvider);
		keyPatternColumn.setHideable(false);
		keyPatternColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_KEY));

		ColumnConfig<KVExtractionDTO, String> valuePatternColumn = new ColumnConfig<KVExtractionDTO, String>(valuePatternValueProvider);
		valuePatternColumn.setHideable(false);
		valuePatternColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_VALUE));

		ColumnConfig<KVExtractionDTO, Integer> noOfWordsColumn = new ColumnConfig<KVExtractionDTO, Integer>(properties.noOfWords());
		noOfWordsColumn.setHideable(false);
		noOfWordsColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.KV_NO_OF_WORDS));

		ColumnConfig<KVExtractionDTO, String> fuziness = new ColumnConfig<KVExtractionDTO, String>(
				new KeyFuzzinessValueProvider<KVExtractionDTO, String>());
		fuziness.setHideable(false);
		fuziness.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.KV_FUZINESS));

		ColumnConfig<KVExtractionDTO, LocationType> location = new ColumnConfig<KVExtractionDTO, LocationType>(
				properties.locationType());
		location.setHideable(false);
		location.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.KV_LOCATION));

		ColumnConfig<KVExtractionDTO, Float> weight = new ColumnConfig<KVExtractionDTO, Float>(properties.weight());
		weight.setHideable(false);
		weight.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.KV_WEIGHT));

		columnConfigList.add(modelSelector);
		columnConfigList.add(keyPatternColumn);
		columnConfigList.add(valuePatternColumn);
		columnConfigList.add(fuziness);
		columnConfigList.add(location);
		columnConfigList.add(noOfWordsColumn);
		columnConfigList.add(weight);

		LabelProvider<String> fuzzinessLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(String item) {
				return item;
			}
		};

		final SimpleComboBox<String> fuzinessCombo = new SimpleComboBox<String>(fuzzinessLabelProvider);
		fuzinessCombo.setTriggerAction(TriggerAction.ALL);
		for (KeyFuzziness value : KeyFuzziness.values()) {
			fuzinessCombo.add(value.getPercentageFuzzinessValue());
		}
		fuzinessCombo.setEditable(false);
		LabelProvider<LocationType> locationLabelProvider = new LabelProvider<LocationType>() {

			@Override
			public String getLabel(LocationType item) {
				// TODO Auto-generated method stub
				return item.valueAsString();
			}
		};
		final SimpleComboBox<LocationType> locationCombo = new SimpleComboBox<LocationType>(locationLabelProvider);
		locationCombo.setTriggerAction(TriggerAction.ALL);
		locationCombo.add(Arrays.asList(LocationType.values()));
		locationCombo.setEditable(false);
		// FunctionKey.getKeyNamesList(FunctionKey.F5.getFunctionKeyName(), FunctionKey.F12.getFunctionKeyName()));
		// editorsMap.put(keyPatternColumn, new TextField());
		// editorsMap.put(valuePatternColumn, new TextField());
		editorsMap.put(fuziness, fuzinessCombo);
		editorsMap.put(location, locationCombo);
		editorsMap.put(noOfWordsColumn, new IntegerField());
		editorsMap.put(weight, new FloatField());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getColumnConfig()
	 */
	@Override
	public <V> List<ColumnConfig<KVExtractionDTO, ?>> getColumnConfig() {
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
	public ModelKeyProvider<KVExtractionDTO> getKeyProvider() {
		return properties.identifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getFilters()
	 */
	@Override
	public List<Filter<KVExtractionDTO, ?>> getFilters() {
		return null;
	}

}
