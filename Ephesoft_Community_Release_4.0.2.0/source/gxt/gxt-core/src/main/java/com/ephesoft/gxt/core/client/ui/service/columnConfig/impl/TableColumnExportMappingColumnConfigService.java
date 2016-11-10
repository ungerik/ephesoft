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

import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.shared.dto.TableColumnDBExportMappingDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.TableColumnDBExportMappingProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.TableColumnDBExportMappingProperties.TableColumnNameValueProvider;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.TableColumnDBExportMappingProperties.TableNameValueProvider;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

public class TableColumnExportMappingColumnConfigService implements ColumnConfigService<TableColumnDBExportMappingDTO> {

	private List<ColumnConfig<TableColumnDBExportMappingDTO, ?>> columnConfigList;

	public TableColumnExportMappingColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<TableColumnDBExportMappingDTO, ?>>();

		ValueProvider<TableColumnDBExportMappingDTO, String> tableInfoProvider = TableNameValueProvider.INSTANCE;
		ColumnConfig<TableColumnDBExportMappingDTO, String> tableInfo = new ColumnConfig<TableColumnDBExportMappingDTO, String>(
				tableInfoProvider);
		tableInfo.setHeader("Table");
		ValueProvider<TableColumnDBExportMappingDTO, String> tableColumnValueProvider = TableColumnNameValueProvider.INSTANCE;
		ColumnConfig<TableColumnDBExportMappingDTO, String> tableColumnInfo = new ColumnConfig<TableColumnDBExportMappingDTO, String>(
				tableColumnValueProvider);
		tableColumnInfo.setHeader("Column");

		ValueProvider<TableColumnDBExportMappingDTO, String> tableNameValueProvider = TableColumnDBExportMappingProperties.INSTANCE
				.tableName();
		ValueProvider<TableColumnDBExportMappingDTO, String> columnNameValueProvider = TableColumnDBExportMappingProperties.INSTANCE
				.columnName();
		ColumnConfig<TableColumnDBExportMappingDTO, String> tableName = new ColumnConfig<TableColumnDBExportMappingDTO, String>(
				tableNameValueProvider);
		tableName.setHeader("Database Table Name");
		ColumnConfig<TableColumnDBExportMappingDTO, String> columnName = new ColumnConfig<TableColumnDBExportMappingDTO, String>(
				columnNameValueProvider);
		columnName.setHeader("Database Column Name");
		ValueProvider<TableColumnDBExportMappingDTO, Boolean> selectionValueProvider = TableColumnDBExportMappingProperties.INSTANCE
				.selected();
		ColumnConfig<TableColumnDBExportMappingDTO, Boolean> selector = new ColumnConfig<TableColumnDBExportMappingDTO, Boolean>(
				selectionValueProvider);
		selector.setCell(new CheckBoxCell());
		selector.setWidth(35);
		selector.setFixed(true);
		columnConfigList.add(selector);
		columnConfigList.add(tableInfo);
		columnConfigList.add(tableColumnInfo);
		columnConfigList.add(tableName);
		columnConfigList.add(columnName);
	}

	@Override
	public <V> List<ColumnConfig<TableColumnDBExportMappingDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	@Override
	public <X extends Comparable> Map<ColumnConfig<TableColumnDBExportMappingDTO, X>, IsField<X>> getEditorsMap() {
		return null;
	}

	@Override
	public ModelKeyProvider<TableColumnDBExportMappingDTO> getKeyProvider() {
		return TableColumnDBExportMappingProperties.INSTANCE.id();
	}

	@Override
	public List<Filter<TableColumnDBExportMappingDTO, ?>> getFilters() {
		return null;
	}

}
