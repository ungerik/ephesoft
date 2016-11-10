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

package com.ephesoft.gxt.admin.client.widget;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DataTable.Rows;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;

public class TestTableGrid extends com.sencha.gxt.widget.core.client.grid.Grid<Row> {

	private final DataTable bindedTable;

	public TestTableGrid(final DataTable dataTable, Integer width, Integer height) {
		super(getListStore(dataTable), getColumnModel(dataTable));
		this.getView().setAutoFill(true);
		this.getView().setForceFit(true);
		this.bindedTable = dataTable;
		this.addStyleName("dataTableGrid");
		this.getView().setColumnLines(true);
		this.setWidth(width);
		this.setHeight(height);
		this.getView().setEmptyText("No Records Found.");
		this.getView().setViewConfig(getViewConfig());
		store.setAutoCommit(true);
	}

	private GridViewConfig<Row> getViewConfig() {
		final GridViewConfig<Row> config = new GridViewConfig<Row>() {

			@Override
			public String getColStyle(final Row model, final ValueProvider<? super Row, ?> valueProvider, final int rowIndex,
					final int colIndex) {
				String cssStyle = CoreCommonConstant.EMPTY_STRING;
				if (valueProvider instanceof TestTableValueProvider) {
					final TestTableValueProvider testTableValueProvider = (TestTableValueProvider) valueProvider;
					final Column currentColumn = testTableValueProvider.getColumn(model);
					if (null != currentColumn && !currentColumn.isValid()) {
						cssStyle = "invalidColumn";
					}
				}
				return cssStyle;
			}

			@Override
			public String getRowStyle(final Row model, final int rowIndex) {
				String cssStyle = CoreCommonConstant.EMPTY_STRING;
				if (null != model && !model.isIsRuleValid()) {
					cssStyle = "invalidRow";
				}
				return cssStyle;
			}
		};
		return config;
	}

	private static ListStore<Row> getListStore(final DataTable dataTable) {
		final ListStore<Row> dataTableListStore = CollectionUtil.createListStore(new DataTableKeyProvider());
		if (null != dataTable) {
			final Rows rows = dataTable.getRows();
			if (null != rows) {
				dataTableListStore.addAll(rows.getRow());
			}
		}
		return dataTableListStore;
	}

	private static ColumnModel<Row> getColumnModel(final DataTable dataTable) {
		final List<ColumnConfig<Row, ?>> columnConfigList = new LinkedList<ColumnConfig<Row, ?>>();
		if (null != dataTable) {
			final List<Column> headerColumnList = BatchSchemaUtil.getHeaderColumn(dataTable);
			if (!CollectionUtil.isEmpty(headerColumnList)) {
				ColumnConfig<Row, String> columnConfig;
				TestTableValueProvider valueProvider;
				for (final Column headerColumn : headerColumnList) {
					valueProvider = new TestTableValueProvider(headerColumn, dataTable.getName());
					columnConfig = new ColumnConfig<Row, String>(valueProvider);
					columnConfig.setSortable(false);
					columnConfig.setMenuDisabled(true);
					columnConfig.setHeader(headerColumn.getName());
					columnConfigList.add(columnConfig);
				}
			}
		}
		final ColumnModel<Row> dataTableColumnModel = new ColumnModel<Row>(columnConfigList);
		return dataTableColumnModel;
	}

	public DataTable getBindedTable() {
		return bindedTable;
	}

	public static class DataTableKeyProvider implements ModelKeyProvider<Row> {

		private static int id;

		@Override
		public String getKey(final Row item) {
			return Integer.toString(id++);
		}
	}
}
