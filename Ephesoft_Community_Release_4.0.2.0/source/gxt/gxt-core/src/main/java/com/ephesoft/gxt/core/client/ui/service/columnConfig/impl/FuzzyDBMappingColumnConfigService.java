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
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.shared.dto.FuzzyDBMappingDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.FuzzyDBMappingProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.FuzzyDBMappingProperties.MappingPropertiesValueProvider.FieldNameValueProvider;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

public class FuzzyDBMappingColumnConfigService implements ColumnConfigService<FuzzyDBMappingDTO> {

	private List<ColumnConfig<FuzzyDBMappingDTO, ?>> columnConfigList;

	public FuzzyDBMappingColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<FuzzyDBMappingDTO, ?>>();
		ValueProvider<FuzzyDBMappingDTO, String> columnNameValueProvider = FuzzyDBMappingProperties.INSTANCE
				.columnName();
		ValueProvider<FuzzyDBMappingDTO, String> fieldNameValueProvider = FieldNameValueProvider.getInstance();
		ColumnConfig<FuzzyDBMappingDTO, String> fieldName = new ColumnConfig<FuzzyDBMappingDTO, String>(
				fieldNameValueProvider);
		fieldName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_FIELD_NAME));
		ColumnConfig<FuzzyDBMappingDTO, String> columnName = new ColumnConfig<FuzzyDBMappingDTO, String>(
				columnNameValueProvider);
		columnName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.FUZZY_DB_TABLE_COLUMN_NAME));
		ValueProvider<FuzzyDBMappingDTO, Boolean> selectionValueProvider = FuzzyDBMappingProperties.INSTANCE.selected();
		ColumnConfig<FuzzyDBMappingDTO, Boolean> selector = new ColumnConfig<FuzzyDBMappingDTO, Boolean>(
				selectionValueProvider);
		CheckBoxCell modelSelector = new CheckBoxCell();
		modelSelector.setSelectOnFocus(true);
		selector.setCell(modelSelector);
		selector.setWidth(35);
		selector.setFixed(true);

		ColumnConfig<FuzzyDBMappingDTO, Boolean> isSearchable = new ColumnConfig<FuzzyDBMappingDTO, Boolean>(
				FuzzyDBMappingProperties.INSTANCE.searchable());
		isSearchable.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.FUZZY_DB_IS_SEARCHABLE));
		isSearchable.setWidth(35);
		CheckBoxCell searchableSelector = new CheckBoxCell();
		searchableSelector.setSelectOnFocus(true);
		isSearchable.setCell(searchableSelector);
		columnConfigList.add(selector);
		columnConfigList.add(fieldName);
		columnConfigList.add(columnName);
		columnConfigList.add(isSearchable);
	}

	@Override
	public <V> List<ColumnConfig<FuzzyDBMappingDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	@Override
	public <X extends Comparable> Map<ColumnConfig<FuzzyDBMappingDTO, X>, IsField<X>> getEditorsMap() {
		return null;
	}

	@Override
	public ModelKeyProvider<FuzzyDBMappingDTO> getKeyProvider() {
		return FuzzyDBMappingProperties.INSTANCE.id();
	}

	@Override
	public List<Filter<FuzzyDBMappingDTO, ?>> getFilters() {
		return null;
	}

}
