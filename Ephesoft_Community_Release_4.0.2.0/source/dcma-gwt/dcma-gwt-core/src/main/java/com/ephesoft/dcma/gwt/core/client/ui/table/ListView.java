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

package com.ephesoft.dcma.gwt.core.client.ui.table;

import java.util.List;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

public class ListView extends ResizeComposite {

	interface Binder extends UiBinder<Widget, ListView> {
	}

	public interface RowSelectionListner {

		void onRowSelected(String identifer);
	}

	public interface DoubleClickListner {

		void onDoubleClickTable();
	}

	public interface PaginationListner {

		void onPagination(int startIndex, int maxResult, Order order);
	}

	public interface OrderingListner {

		void onOrdering(int startIndex, int maxResult, String selectedRowId, int swapIndex, int selectedRecordIndex);
	}

	@UiField
	LayoutPanel layoutPanel;

	Table table;

	private TableHeader header = new TableHeader();

	PaginationListner paginationListner;

	OrderingListner orderingListner;

	RowSelectionListner rowSelectionListner;

	private static final Binder BINDER = GWT.create(Binder.class);

	public ListView() {
		initWidget(BINDER.createAndBindUi(this));
	}

	public void setPaginationListner(final PaginationListner paginationListner) {
		this.paginationListner = paginationListner;
	}

	public void setOrderingListner(final OrderingListner orderingListner) {
		this.orderingListner = orderingListner;
	}

	public void setRowSelectionListner(final RowSelectionListner rowSelectionListner) {
		this.rowSelectionListner = rowSelectionListner;
	}

	public void addHeaderColumns(final HeaderColumn... columns) {
		for (int i = 0; i < columns.length; i++) {
			header.addHeaderColumn(columns[i]);
		}
	}
	
	/**
	 * creates the table with the given record list.
	 * 
	 * @param totalCount int. The size of record list.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 */
	public void initTable(final int totalCount, final List<Record> recordList) {
		initTable(totalCount, recordList, SelectionHandlers.NO_HANDLER, false);
	}
	
	/**
	 * creates the table with the given record list.
	 * 
	 * @param totalCount int. The size of record list.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param requireRadioButton boolean. true value adds the radiobutton in table.
	 */
	public void initTable(final int totalCount, final List<Record> recordList, final boolean requireRadioButton) {
		initTable(totalCount, null, recordList, requireRadioButton, false, null, false);
	}

	/**
	 * creates the table with the given record list.
	 * 
	 * @param totalCount int. The size of record list.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param requireRadioButton boolean. true value adds the radio button in table.
	 * @param fireEventForFirstRow boolean.
	 */
	public void initTable(final int totalCount, final List<Record> recordList, final boolean requireRadioButton, final boolean fireEventForFirstRow) {
		initTable(totalCount, null, recordList, requireRadioButton, fireEventForFirstRow, null, false);

	}

	public void addHeaderColumns(final List<HeaderColumn> headerColumns) {
		for (HeaderColumn headerColumn : headerColumns) {
			header.addHeaderColumn(headerColumn);
		}
	}
	
	/**
	 * Creates the table with the given record list.
	 *  
	 * @param totalCount  int. The size of record list.
	 * @param paginationListner {@link PaginationListner} The instance of PaginationListner.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param requireRadioButton boolean. true value adds the radio button in table.
	 * @param fireEventForFirstRow boolean.
	 * @param orderingListner {@link OrderingListner}.The instance of OrderingListner.
	 * @param isOrderedEntity boolean.
	 */
	public void initTable(final int totalCount, final PaginationListner paginationListner, final List<Record> recordList, final boolean requireRadioButton,
			final boolean fireEventForFirstRow, final OrderingListner orderingListner, final boolean isOrderedEntity) {
		initTable(totalCount, paginationListner, recordList, requireRadioButton, fireEventForFirstRow, null, null, isOrderedEntity);
	}
	
	/**
	 * Creates the table with the given record list.
	 *  
	 * @param totalCount  int. The size of record list.
	 * @param paginationListner {@link PaginationListner} The instance of PaginationListner.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param requireRadioButton boolean. true value adds the radio button in table.
	 * @param fireEventForFirstRow boolean.
	 * @param doubleClickListner {@link DoubleClickListner} The instance of DoubleClickListner.
	 * @param orderingListner {@link OrderingListner}.The instance of OrderingListner.
	 * @param isOrderedEntity boolean.
	 */
	public void initTable(final int totalCount, final PaginationListner paginationListner, final List<Record> recordList, final boolean requireRadioButton,
			final boolean fireEventForFirstRow, final DoubleClickListner doubleClickListner, final OrderingListner orderingListner,
			final boolean isOrderedEntity) {
		initTable(totalCount, paginationListner, null, recordList, requireRadioButton, fireEventForFirstRow, doubleClickListner,
				orderingListner, isOrderedEntity);
	}
	
	/**
	 * Creates the table with the given record list.
	 *  
	 * @param totalCount  int. The size of record list.
	 * @param paginationListner {@link PaginationListner} The instance of PaginationListner.
	 * @param rowSelectionListner {@link RowSelectionListner}The instance of RowSelectionListner.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param requireRadioButton boolean. true value adds the radio button in table.
	 * @param fireEventForFirstRow boolean.
	 * @param doubleClickListner {@link DoubleClickListner} The instance of DoubleClickListner.
	 * @param orderingListner {@link OrderingListner}.The instance of OrderingListner.
	 * @param isOrderedEntity boolean.
	 */
	public void initTable(final int totalCount, final PaginationListner paginationListner, final RowSelectionListner rowSelectionListner,
			final List<Record> recordList, final boolean requireRadioButton, final boolean fireEventForFirstRow, final OrderingListner orderingListner,
			final boolean isOrderedEntity) {
		initTable(totalCount, paginationListner, rowSelectionListner, recordList, requireRadioButton, fireEventForFirstRow, null,
				orderingListner, isOrderedEntity);
	}
	
	/**
	 * Creates the table with the given record list.
	 *  
	 * @param totalCount  int. The size of record list.
	 * @param paginationListner {@link PaginationListner} The instance of PaginationListner.
	 * @param rowSelectionListner {@link RowSelectionListner}The instance of RowSelectionListner.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param fireEventForFirstRow boolean.
	 * @param doubleClickListner {@link DoubleClickListner} The instance of DoubleClickListner.
	 * @param orderingListner {@link OrderingListner}.The instance of OrderingListner.
	 * @param isOrderedEntity boolean.
	 */
	public void initTable(final int totalCount, final PaginationListner paginationListner, final RowSelectionListner rowSelectionListner,
			final List<Record> recordList, final boolean requireRadioButton, final boolean fireEventForFirstRow, final DoubleClickListner doubleClickListner,
			final OrderingListner orderingListner, final boolean isOrderedEntity) {
		if (requireRadioButton) {
			table = new Table(totalCount, header, SelectionHandlers.RADIOBUTTONS, fireEventForFirstRow, doubleClickListner);
		} else {
			table = new Table(totalCount, header, SelectionHandlers.NO_HANDLER, fireEventForFirstRow, doubleClickListner);
		}
		table.setPaginationListner(paginationListner);
		table.setRowSelectionListener(rowSelectionListner);
		table.setOrderingListner(orderingListner);
		table.setOrderedEntity(isOrderedEntity);
		table.pushData(recordList, 0);
		layoutPanel.clear();
		layoutPanel.add(table);

	}

	/**
	 * Creates the table with the given record list.
	 *  
	 * @param totalCount  int. The size of record list.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param selectHandler {@link SelectionHandlers}.
	 */
	public void initTable(final int totalCount, final List<Record> recordList, final SelectionHandlers selectHandler) {
		initTable(totalCount, null, recordList, selectHandler, false, null, false);
	}
	
	/**
	 * Creates the table with the given record list.
	 *  
	 * @param totalCount  int. The size of record list.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param selectHandler {@link SelectionHandlers}.
	 * @param fireEventForFirstRow boolean.
	 */
	public void initTable(final int totalCount, final List<Record> recordList, final SelectionHandlers selectHandler, final boolean fireEventForFirstRow) {
		initTable(totalCount, null, recordList, selectHandler, fireEventForFirstRow, null, false);

	}
	
	/**
	 * Creates the table with the given record list.
	 *  
	 * @param totalCount  int. The size of record list.
	 * @param paginationListner {@link PaginationListner} The instance of PaginationListner.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param selectHandler {@link SelectionHandlers}.
	 * @param fireEventForFirstRow boolean.
	 * @param orderingListner {@link OrderingListner}.The instance of OrderingListner.
	 * @param isOrderedEntity boolean.
	 */
	public void initTable(final int totalCount, final PaginationListner paginationListner, final List<Record> recordList,
			final SelectionHandlers selectHandler, final boolean fireEventForFirstRow, final OrderingListner orderingListner,
			final boolean isOrderedEntity) {
		initTable(totalCount, paginationListner, recordList, selectHandler, fireEventForFirstRow, null, null, isOrderedEntity);
	}
	
	/**
	 * Creates the table with the given record list.
	 *  
	 * @param totalCount  int. The size of record list.
	 * @param paginationListner {@link PaginationListner} The instance of PaginationListner.
	 * @param rowSelectionListner {@link RowSelectionListner}The instance of RowSelectionListner.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param fireEventForFirstRow boolean.
	 * @param doubleClickListner {@link DoubleClickListner} The instance of DoubleClickListner.
	 * @param orderingListner {@link OrderingListner}.The instance of OrderingListner.
	 * @param isOrderedEntity boolean.
	 */
	public void initTable(final int totalCount, final PaginationListner paginationListner, final List<Record> recordList,
			final SelectionHandlers selectHandler, final boolean fireEventForFirstRow, final DoubleClickListner doubleClickListner,
			final OrderingListner orderingListner, final boolean isOrderedEntity) {
		initTable(totalCount, paginationListner, null, recordList, selectHandler, fireEventForFirstRow, doubleClickListner,
				orderingListner, isOrderedEntity);
	}
	
	/**
	 * Creates the table with the given record list.
	 *  
	 * @param totalCount  int. The size of record list.
	 * @param paginationListner {@link PaginationListner} The instance of PaginationListner.
	 * @param rowSelectionListner {@link RowSelectionListner}The instance of RowSelectionListner.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param fireEventForFirstRow boolean.
	 * @param doubleClickListner {@link DoubleClickListner} The instance of DoubleClickListner.
	 * @param orderingListner {@link OrderingListner}.The instance of OrderingListner.
	 * @param isOrderedEntity boolean.
	 */
	public void initTable(final int totalCount, final PaginationListner paginationListner, final RowSelectionListner rowSelectionListner,
			final List<Record> recordList, final SelectionHandlers selectHandler, final boolean fireEventForFirstRow,
			final OrderingListner orderingListner, final boolean isOrderedEntity) {
		initTable(totalCount, paginationListner, rowSelectionListner, recordList, selectHandler, fireEventForFirstRow, null,
				orderingListner, isOrderedEntity);
	}
	
	/**
	 * Creates the table with the given record list.
	 *  
	 * @param totalCount  int. The size of record list.
	 * @param paginationListner {@link PaginationListner} The instance of PaginationListner.
	 * @param rowSelectionListner {@link RowSelectionListner}The instance of RowSelectionListner.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param fireEventForFirstRow boolean.
	 * @param doubleClickListner {@link DoubleClickListner} The instance of DoubleClickListner.
	 * @param orderingListner {@link OrderingListner}.The instance of OrderingListner.
	 * @param isOrderedEntity boolean.
	 */
	public void initTable(final int totalCount, final PaginationListner paginationListner, final RowSelectionListner rowSelectionListner,
			final List<Record> recordList, final SelectionHandlers selectHandler, final boolean fireEventForFirstRow,
			final DoubleClickListner doubleClickListner, final OrderingListner orderingListner, final boolean isOrderedEntity) {
		table = new Table(totalCount, header, selectHandler, fireEventForFirstRow, doubleClickListner);
		table.setPaginationListner(paginationListner);
		table.setRowSelectionListener(rowSelectionListner);
		table.setOrderingListner(orderingListner);
		table.setOrderedEntity(isOrderedEntity);
		table.pushData(recordList, 0);
		layoutPanel.clear();
		layoutPanel.add(table);

	}
	
	/**
	 * Creates the table with the given record list.
	 *  
	 * @param totalCount  int. The size of record list.
	 * @param paginationListner {@link PaginationListner} The instance of PaginationListner.
	 * @param rowSelectionListner {@link RowSelectionListner}The instance of RowSelectionListner.
	 * @param recordList {@link List} <{@link Record}> The list of records.
	 * @param fireEventForFirstRow boolean.
	 * @param doubleClickListner {@link DoubleClickListner} The instance of DoubleClickListner.
	 * @param orderingListner {@link OrderingListner}.The instance of OrderingListner.
	 * @param isOrderedEntity boolean.
	 */
	public void initTable(final int totalCount, final PaginationListner paginationListner, final RowSelectionListner rowSelectionListner,
			final List<Record> recordList, final SelectionHandlers selectHandler, final boolean fireEventForFirstRow,
			final DoubleClickListner doubleClickListner, final OrderingListner orderingListner, final boolean isOrderedEntity,
			final boolean isSelectAllRows) {
		table = new Table(totalCount, header, selectHandler, fireEventForFirstRow, doubleClickListner);
		table.setPaginationListner(paginationListner);
		table.setRowSelectionListener(rowSelectionListner);
		table.setOrderingListner(orderingListner);
		table.setOrderedEntity(isOrderedEntity);
		if (selectHandler == SelectionHandlers.CHECKBOX) {
			table.pushData(recordList, 0, totalCount, isSelectAllRows);
		} else {
			table.pushData(recordList, 0);
		}
		layoutPanel.clear();
		layoutPanel.add(table);

	}
	
	
	
	
	public void updateRecords(final List<Record> recordList, final int startIndex) {
		table.pushData(recordList, startIndex);
	}

	public void updateRecords(final List<Record> recordList, final int startIndex, final int count) {
		table.pushData(recordList, startIndex, count);
	}

	public void updateRecords(final List<Record> recordList, final int startIndex, final int count, final int selectedIndex) {
		table.pushData(recordList, startIndex, count, selectedIndex);
	}

	/**
	 * Update the previously selected record only, instead of the first record in case of refresh and unlock.
	 * 
	 * @param recordList List<Record>
	 * @param startIndex int
	 * @param count int
	 */
	public void updateSelectedRecords(final List<Record> recordList, final int startIndex, final int count) {
		table.pushData(recordList, startIndex, count, table.getSelectedIndex());
	}

	public String getSelectedRowIndex() {
		return table.getSelectedRowId();
	}

	/**
	 * To get the index of the rows selected.
	 * 
	 * @return List<String>
	 */
	public List<String> getSelectedRowsIndices() {
		return table.getSelectedRowIds();
	}

	public TableHeader getHeader() {
		return header;
	}

	public int getTableRowCount() {
		return Table.VISIBLE_RECORD_COUNT;
	}

	public void setTableRowCount(final int count) {
		Table.VISIBLE_RECORD_COUNT = count;
	}

	public int getTableRecordCount() {
		return table.getTableRecordCount();
	}

	public Order getTableOrder() {
		return table.getOrder();
	}

	public int getStartIndex() {
		return table.getStartIndex();
	}
}
