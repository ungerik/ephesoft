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

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HeaderRow;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.Row.Columns;
import com.ephesoft.gxt.core.client.DomainView;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleConstant;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleMessage;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.presenter.batch.DataTablePresenter;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.DataTableGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;

public class DataTableView extends DomainView<DataTable, DataTablePresenter> {

	interface Binder extends UiBinder<Widget, DataTableView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField(provided = true)
	protected DataTableGrid dataTableGrid;

	@UiField
	protected Button insertButton;

	@UiField
	protected Button deleteButton;

	@UiField
	protected Button deleteAllButton;

	@UiField
	protected Button rowExtractionButton;

	@UiField
	protected Button startExtractionButton;

	@UiField
	protected Button rejectExtractionButton;

	@UiField
	protected Label tableNameLabel;

	@UiField
	protected Button columnExtractionButton;

	@UiField
	protected FramedPanel headerPanel;

	private boolean isColumnExtraction;

	public DataTableView(final DataTable bindedObject) {
		super(bindedObject);
		if (null != bindedObject) {
			dataTableGrid = new DataTableGrid(bindedObject);
			addKeyDownHandler();
			this.initWidget(binder.createAndBindUi(this));
			insertButton.setText(LocaleConstant.INSERT_ROW_BUTTON_TEXT);
			deleteButton.setText(LocaleConstant.DELETE_ROW_BUTTON_TEXT);
			deleteAllButton.setText(LocaleConstant.DELETE_ALL_ROWS_BUTTON_TEXT);
			rowExtractionButton.setText(LocaleConstant.ROW_EXTRACTION_BUTTON_TEXT);
			columnExtractionButton.setText(LocaleConstant.COLUMN_EXTRACTION_BUTTON_TEXT);
			rejectExtractionButton.setText(LocaleConstant.REJECT_EXTRACTION_BUTTON_TEXT);
			startExtractionButton.setText(LocaleConstant.START_EXTRACTION_BUTTON_TEXT);
			setEnableExtractionButton(false);
			insertButton.addStyleName("tableOptionsFirstButton");
			rowExtractionButton.addStyleName("extractionFirstButton");
			setTableName(bindedObject.getName());
			final Resizable resizable = new Resizable(dataTableGrid, Dir.S);
			resizable.setEnabled(true);
		}

	}

	private void setTableName(String name) {
		if (!StringUtil.isNullOrEmpty(name)) {
			String nameToSet = (name.length() > 9) ? StringUtil.concatenate(name.substring(0, 6), "..") : name;
			tableNameLabel.setText(nameToSet);
			tableNameLabel.setTitle(name);
		}
	}

	private void addKeyDownHandler() {
		dataTableGrid.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(final KeyDownEvent event) {
				if (event.isControlKeyDown()) {
					final int keyCode = event.getNativeKeyCode();
					switch (keyCode) {
						case KeyCodes.KEY_I:
							event.preventDefault();
							if (canInsert()) {
								insertRow(event.isShiftKeyDown(), true);
							}
							break;

						case KeyCodes.KEY_J:
							event.preventDefault();
							dataTableGrid.cancelEditing();
							if (canDelete()) {
								if (!deleteAllRows(true)) {
									deleteCurrentRow();
								}
							}
							break;

						case KeyCodes.KEY_U:
							event.preventDefault();
							dataTableGrid.cancelEditing();
							if (canDeleteAll()) {
								deleteAllRows(false);
							}
							break;

						case KeyCodes.KEY_Y:
							event.preventDefault();
							if (canStartExtraction()) {
								startExtraction();
							} else {
								insertNewRowForRowExtraction();
							}
							break;
					}
				}
			}
		}, KeyDownEvent.getType());
	}

	@Override
	public void focus() {
		dataTableGrid.focus();
	}

	@UiHandler("insertButton")
	public void onInsertClick(final ClickEvent clickEvent) {
		insertRow(false, true);
	}

	private void insertRow(final boolean beforeCurrentCell, final boolean addToModel) {
		final DataTable bindedTable = dataTableGrid.getBindedTable();
		if (null != bindedTable) {
			final HeaderRow headerRow = bindedTable.getHeaderRow();
			int rowIndex = 0;
			if (null != headerRow) {
				rowIndex = presenter.insertRow(headerRow.getColumns(), addToModel, beforeCurrentCell);
			}
		}
	}

	@UiHandler("deleteButton")
	public void onDeleteClick(final ClickEvent clickEvent) {
		if (!deleteAllRows(true)) {
			deleteCurrentRow();
		}
	}

	private void deleteCurrentRow() {
		final int currentSelectionIndex = dataTableGrid.getCurrentRowSelectionIndex();
		dataTableGrid.deleteCurrentRow();
		final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
		if (null != currentDocument) {
			ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
		}
		dataTableGrid.select(currentSelectionIndex);
	}

	public String getTableName() {
		return bindedObject.getName();
	}

	@UiHandler("deleteAllButton")
	public void onDeleteAllButtonClick(final ClickEvent clickEvent) {
		deleteAllRows(false);
	}

	private boolean deleteAllRows(boolean selectedOnly) {
		boolean isDeleted = dataTableGrid.deleteAll(selectedOnly);
		final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
		if (null != currentDocument) {
			ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
		}
		return isDeleted;
	}

	@UiHandler("rowExtractionButton")
	public void onRowExtractionButtonClick(final ClickEvent clickEvent) {
		insertNewRowForRowExtraction();
	}

	private void insertNewRowForRowExtraction() {
		isColumnExtraction = false;
		executeBeforeRowExtractionOperation();
	}

	@UiHandler("rejectExtractionButton")
	public void onRejectExtractionButtonClick(final ClickEvent clickEvent) {
		rejectExtraction();
	}

	private void rejectExtraction() {
		setEnableCUDOperationButton(true);
		setEnableExtractionButton(false);
		dataTableGrid.removeManualExtractionRow();
	}

	public void setEnableCUDOperationButton(final boolean enable) {
		insertButton.setEnabled(enable);
		deleteButton.setEnabled(enable);
		deleteAllButton.setEnabled(enable);
		rowExtractionButton.setEnabled(enable);
		columnExtractionButton.setEnabled(enable);
	}

	public void setEnableExtractionButton(final boolean enable) {
		startExtractionButton.setEnabled(enable);
		rejectExtractionButton.setEnabled(enable);
	}

	@UiHandler("startExtractionButton")
	public void onStartExtractionClick(final ClickEvent clickEvent) {
		startExtraction();
	}

	private boolean canStartExtraction() {
		return startExtractionButton.isEnabled();
	}

	private void startExtraction() {
		Row initialRow;
		if (isColumnExtraction) {
			initialRow = dataTableGrid.getCurrentRow();
			setEnableCUDOperationButton(true);
			setEnableExtractionButton(false);
			presenter.startManualExtraction(initialRow, isColumnExtraction);
		} else {
			initialRow = dataTableGrid.getManualExtractionSampleRow();
			if (isValidManualRow(initialRow)) {
				setEnableCUDOperationButton(true);
				setEnableExtractionButton(false);
				presenter.startManualExtraction(initialRow, isColumnExtraction);
			} else {
				Message.display(LocaleConstant.INVALID_MANUAL_EXTRACTION_OPERATION, LocaleMessage.ALL_COLUMN_NOT_SELECTED);
			}
		}

	}

	private boolean isValidManualRow(final Row initalRow) {
		boolean isValid = true;
		if (null != initalRow) {
			Columns columns = initalRow.getColumns();
			if (null != columns) {
				List<Column> columnList = columns.getColumn();
				for (Column column : columnList) {
					if (null != column) {
						CoordinatesList coordinatesList = column.getCoordinatesList();
						if (null == coordinatesList || CollectionUtil.isEmpty(coordinatesList.getCoordinates())) {
							isValid = false;
							break;
						}
					}
				}
			}
		}
		return isValid;
	}

	@UiHandler("columnExtractionButton")
	public void onColumnExtractionClick(final ClickEvent clickEvent) {
		isColumnExtraction = true;
		setEnableCUDOperationButton(false);
		setEnableExtractionButton(true);
	}

	private void executeBeforeRowExtractionOperation() {
		setEnableCUDOperationButton(false);
		setEnableExtractionButton(true);
		final DataTable bindedTable = dataTableGrid.getBindedTable();
		if (null != bindedTable) {
			final HeaderRow headerRow = bindedTable.getHeaderRow();
			if (null != headerRow) {
				presenter.insertRow(headerRow.getColumns(), false, false);
			}
		}
	}

	public void add(final Row rowToAdd, final boolean beforeCurrentRow, final boolean addToModel) {
		dataTableGrid.addRow(rowToAdd, beforeCurrentRow, addToModel);
	}

	public int indexOf(final Row row) {
		return dataTableGrid.getStore().indexOf(row);
	}

	public void refresh() {
		dataTableGrid.refresh();
	}

	public Column getColumnExtractionColumn() {
		return dataTableGrid.getCurrentColumn();
	}

	public List<Row> getRowsToMergeForColumnExtraction() {
		return dataTableGrid.getRowsAfter(dataTableGrid.getCurrentRow());
	}

	public void edit(final Row rowToEdit) {
		dataTableGrid.startEditing(rowToEdit);
	}

	public void add(final List<Row> rowsListToAdd) {
		dataTableGrid.addAll(rowsListToAdd);
	}

	public List<Row> getValidManualExtractedRow(List<Row> extractedRowList) {
		List<Row> validRows = null;
		if (!CollectionUtil.isEmpty(extractedRowList)) {
			validRows = new LinkedList<Row>();
			for (Row extractedRow : extractedRowList) {
				if (null != extractedRow && dataTableGrid.isValidManuallyExtractedRow(extractedRow)) {
					validRows.add(extractedRow);
				}
			}
		}
		return validRows;
	}

	public void removeManualExtractionRow() {
		dataTableGrid.removeManualExtractionRow();
	}

	public boolean isValid() {
		return dataTableGrid.isValid();
	}

	public void selectFirstInvalidCell() {
		dataTableGrid.selectFirstInvalidCell();
	}

	public void select(final int row) {
		ReviewValidateNavigator.setCurrentGrid(dataTableGrid);
		dataTableGrid.select(row);
	}

	public void setGridHeight(final int height) {
		dataTableGrid.setHeight(height);
	}

	public void setGridWidth(final int width) {
		dataTableGrid.setWidth(width);
	}

	private boolean canInsert() {
		return insertButton.isEnabled();
	}

	private boolean canDelete() {
		return deleteButton.isEnabled();
	}

	private boolean canDeleteAll() {
		return deleteAllButton.isEnabled();
	}
}
