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

package com.ephesoft.gxt.core.client.ui.widget;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DataTable.Rows;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.grid.RowNumberer;
import com.sencha.gxt.widget.core.client.selection.CellSelection;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class DataTableGrid extends
		com.sencha.gxt.widget.core.client.grid.Grid<Row> {

	private final CellSelectionModel<Row> cellSelectionModel;

	private final DataTable bindedTable;

	private Row currentRow;
	
	private static RowNumberer<Row> numberer;

	public DataTableGrid(final DataTable dataTable,final boolean isSequencerRequired) {
		this(dataTable, -1, -1,isSequencerRequired);
	}

	public DataTableGrid(final DataTable dataTable, final Integer width,
			final Integer height,final boolean isSequencerRequired) {
		super(getListStore(dataTable), getColumnModel(dataTable,isSequencerRequired));
		if(isSequencerRequired){
		numberer.initPlugin(this);
		}
		// this.getView().setAutoFill(true);
		// this.getView().setForceFit(true);
		this.bindedTable = dataTable;
		this.addStyleName("dataTableGrid");
		cellSelectionModel = new CellSelectionModel<Row>() {
		};
		this.setSelectionModel(cellSelectionModel);
		this.getView().setColumnLines(true);
		if (width != -1 && height != -1) {
			this.setPixelSize(width, height);
		}
		this.getView().setEmptyText(LocaleDictionary.getConstantValue(CoreCommonConstants.NO_RECORDS_FOUND));
		this.getView().setViewConfig(getViewConfig());
		store.setAutoCommit(true);
		setCurrentSelection();
	}

	private void setCurrentSelection() {
		cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<Row>() {

			@Override
			public void onCellSelectionChanged(CellSelectionChangedEvent<Row> event) {
				if (null != event) {
					List<CellSelection<Row>> selection = event.getSelection();
					if (!CollectionUtil.isEmpty(selection)) {
						CellSelection<Row> cellSelection = selection.get(0);
						if (null != cellSelection) {
							currentRow = cellSelection.getModel();
						}
					}
				}
			}
		});
	}

	private GridViewConfig<Row> getViewConfig() {
		final GridViewConfig<Row> config = new GridViewConfig<Row>() {

			@Override
			public String getColStyle(final Row model,
					final ValueProvider<? super Row, ?> valueProvider,
					final int rowIndex, final int colIndex) {
				String cssStyle = CoreCommonConstant.EMPTY_STRING;
				if (valueProvider instanceof ColumnValueProvider) {
					final ColumnValueProvider columnValueProvider = (ColumnValueProvider) valueProvider;
					final Column currentColumn = columnValueProvider
							.getColumn(model);
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
		final ListStore<Row> dataTableListStore = CollectionUtil
				.createListStore(new DataTableKeyProvider());
		if (null != dataTable) {
			final Rows rows = dataTable.getRows();
			if (null != rows) {
				dataTableListStore.addAll(rows.getRow());
			}
		}
		return dataTableListStore;
	}

	private static ColumnModel<Row> getColumnModel(final DataTable dataTable,final boolean isSequencerRequired) {
		final List<ColumnConfig<Row, ?>> columnConfigList = new LinkedList<ColumnConfig<Row, ?>>();
		if (null != dataTable) {
			final List<Column> headerColumnList = BatchSchemaUtil
					.getHeaderColumn(dataTable);
			if (!CollectionUtil.isEmpty(headerColumnList)) {
				if (isSequencerRequired) {
					numberer = new RowNumberer<Row>();
					numberer.setSortable(false);
					numberer.setWidth(55);
					numberer.setHeader(CoreCommonConstant.SEQUENCE_NO);
					columnConfigList.add(numberer);
				}
				ColumnConfig<Row, String> columnConfig;
				ColumnValueProvider valueProvider;
				for (final Column headerColumn : headerColumnList) {
					valueProvider = new ColumnValueProvider(headerColumn,
							dataTable.getName());
					columnConfig = new ColumnConfig<Row, String>(valueProvider);
					// columnConfig.setResizable(false);
					columnConfig.setSortable(true);
					// columnConfig.setMenuDisabled(true);
					columnConfig.setHeader(headerColumn.getName());
					columnConfigList.add(columnConfig);
				}
			}
		}
		final ColumnModel<Row> dataTableColumnModel = new ColumnModel<Row>(
				columnConfigList);
		return dataTableColumnModel;
	}

	public DataTable getBindedTable() {
		return bindedTable;
	}

	public List<String> getColumnNames() {
		return getColumnNames(false);
	}

	public List<String> getHiddenColumnNames() {
		return getColumnNames(true);
	}

	private List<String> getColumnNames(boolean onlyHidden) {
		int totalColumn = cm.getColumnCount();
		List<String> columnNames = new LinkedList<String>();
		for (int index = 0; index < totalColumn; index++) {
			ColumnConfig<?, ?> column = cm.getColumn(index);
			if (null != column && (!onlyHidden || column.isHidden())) {
				columnNames.add(column.getHeader().asString());
			}
		}
		return columnNames;
	}

	public void hideColumns(final List<String> columnsToHide) {
		if (!CollectionUtil.isEmpty(columnsToHide)) {
			int totalColumns = cm.getColumnCount();
			for (int i = 0; i < totalColumns; i++) {
				ColumnConfig<?, ?> columnConfig = cm.getColumn(i);
				SafeHtml header = columnConfig.getHeader();
				if (null != columnConfig && columnsToHide.contains(header.asString())) {
					columnConfig.setHidden(true);
				}
			}
		}
	}
	
	

	
	public Row getCurrentRow() {
		return cellSelectionModel.getSelectedItem();
	}
	
	@Override
	public CellSelectionModel<Row> getSelectionModel() {
		return cellSelectionModel;
	}



	public static class DataTableKeyProvider implements ModelKeyProvider<Row> {

		private static int id;

		@Override
		public String getKey(final Row item) {
			return Integer.toString(id++);
		}
	}

}
