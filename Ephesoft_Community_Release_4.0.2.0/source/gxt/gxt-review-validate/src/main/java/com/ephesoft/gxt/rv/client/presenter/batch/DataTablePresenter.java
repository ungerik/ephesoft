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

package com.ephesoft.gxt.rv.client.presenter.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Column.AlternateValues;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HeaderRow.Columns;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.gxt.core.client.Controller;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.client.DomainPresenter;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.ReviewValidateServiceAsync;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DefaultCellSelectionEndEvent;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.event.PageSelectionEvent;
import com.ephesoft.gxt.rv.client.view.batch.DataTableView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.DataTableGrid;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class DataTablePresenter extends DomainPresenter<DataTableView> {

	private ReviewValidateServiceAsync rpcService;

	private static Page pageBeforeScriptExecution;

	interface CustomEventBinder extends EventBinder<DataTablePresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	@Override
	public void injectEvents(EventBus eventBus) {
		super.injectEvents(eventBus);
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public DataTablePresenter(final Controller controller, final DataTableView view) {
		super(controller, view);
		final DCMARemoteServiceAsync dcmaService = (controller.getRpcService());
		if (dcmaService instanceof ReviewValidateServiceAsync) {
			rpcService = (ReviewValidateServiceAsync) controller.getRpcService();
		} else {
			throw new UnsupportedOperationException("Data Tables require Review Validate services only");
		}
	}

	public int insertRow(final Columns headerColumns, final boolean addToModel, final boolean beforeCurrentRow) {
		int rowIndex = 0;
		if (null != headerColumns) {
			final List<Column> headerColumnList = headerColumns.getColumn();
			final Row rowToAdd = createNewRow(headerColumnList);
			view.add(rowToAdd, beforeCurrentRow, addToModel);
			view.edit(rowToAdd);
			final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			rowIndex = view.indexOf(rowToAdd);
			ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
		}
		return rowIndex;
	}

	public void startManualExtraction(final Row manualExtractionSampleRow, final boolean isColumnExtraction) {
		if (null != manualExtractionSampleRow) {
			final Map<Integer, Coordinates> coordinatesMap = getCoordinateMap(manualExtractionSampleRow);
			final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			final DataTableGrid currentGrid = ReviewValidateNavigator.getCurrentGrid();
			final DataTable currentTable = currentGrid == null ? null : currentGrid.getBindedTable();
			final String batchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
			final Page currentPage = ReviewValidateNavigator.getCurrentPage();
			final String pageIndentifier = currentPage == null ? null : currentPage.getIdentifier();
			final String hocrFileName = currentPage == null ? null : currentPage.getHocrFileName();
			final String batchClassIdentifier = ReviewValidateNavigator.getBatchClassIdentifier();
			rpcService.getTableData(coordinatesMap, currentDocument, currentTable, batchClassIdentifier, batchInstanceIdentifier,
					pageIndentifier, hocrFileName, new AsyncCallback<List<Row>>() {

						@Override
						public void onSuccess(final List<Row> result) {
							if (isColumnExtraction) {
								mergeColumnExtractionRows(result);
								view.refresh();
							} else {
								view.removeManualExtractionRow();
								final List<Row> validManualExtractedRows = view.getValidManualExtractedRow(result);
								view.add(validManualExtractedRows);
							}
							ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
						}

						@Override
						public void onFailure(final Throwable caught) {
						}
					});
		}
	}

	private void mergeColumnExtractionRows(final List<Row> extractedRows) {
		final Column columnToMerge = view.getColumnExtractionColumn();
		if (null != columnToMerge) {
			final String columnName = columnToMerge.getName();
			final List<Row> rowListToMerge = view.getRowsToMergeForColumnExtraction();
			Row nearestRow = null;
			Column nearestRowColumn = null;
			Column rowToMergeColumn = null;
			if (!CollectionUtil.isEmpty(rowListToMerge)) {
				for (final Row row : rowListToMerge) {
					nearestRow = BatchSchemaUtil.getNearestRow(extractedRows, row);
					if (null != nearestRow && BatchSchemaUtil.canMerge(row, nearestRow)) {
						extractedRows.remove(nearestRow);
						nearestRowColumn = BatchSchemaUtil.getColumn(nearestRow, columnName);
						rowToMergeColumn = BatchSchemaUtil.getColumn(row, columnName);
						if (null != nearestRowColumn && null != rowToMergeColumn) {
							rowToMergeColumn.setValue(nearestRowColumn.getValue());
						}
					}
				}
			}
		}
	}

	private Map<Integer, Coordinates> getCoordinateMap(final Row manualExtractionSampleRow) {
		final Map<Integer, Coordinates> coordinateMap = new HashMap<Integer, Coordinates>();
		final com.ephesoft.dcma.batch.schema.Row.Columns columns = manualExtractionSampleRow.getColumns();
		if (null != columns) {
			final List<Column> columnList = columns.getColumn();
			final int totalColumns = columnList.size();
			Column currentColumn;
			CoordinatesList coordinatesList;
			List<Coordinates> listOfCoordinates;
			Coordinates columnCordinate;
			for (int index = 0; index < totalColumns; index++) {
				currentColumn = columnList.get(index);
				columnCordinate = null;
				if (null != currentColumn) {
					coordinatesList = currentColumn.getCoordinatesList();
					listOfCoordinates = coordinatesList == null ? null : coordinatesList.getCoordinates();
					columnCordinate = CollectionUtil.isEmpty(listOfCoordinates) ? null : listOfCoordinates.get(0);
				}
				coordinateMap.put(index, columnCordinate);
			}
		}
		return coordinateMap;
	}

	private Row createNewRow(final List<Column> headerColumnList) {
		final Row row = new Row();
		row.setMannualExtraction(false);
		row.setIsRuleValid(false);
		Row.Columns columnsRow = row.getColumns();
		if (null == columnsRow) {
			columnsRow = new Row.Columns();
			row.setColumns(columnsRow);
		}
		final List<Column> columnRowList = columnsRow.getColumn();
		int headerColumnIndex = 0;
		Column headerColumn = null;
		String headerColumnName = null;
		if (!CollectionUtil.isEmpty(headerColumnList)) {
			for (final Column colHeader : headerColumnList) {
				final Column column = new Column();
				headerColumn = headerColumnList.get(headerColumnIndex++);
				column.setValid(false);
				column.setValidationRequired(false);
				column.setConfidence(0.0f);
				column.setForceReview(false);
				column.setOcrConfidence(0.0f);
				column.setOcrConfidenceThreshold(0.0f);
				column.setValid(false);
				column.setValidationRequired(false);
				// CR Defect NULL Check should be on Column not on column name.
				headerColumnName = headerColumn == null ? null : headerColumn.getName();
				column.setName(headerColumnName);
				column.setValue(null);
				column.setConfidence(0.0f);
				column.setCoordinatesList(new CoordinatesList());
				column.setPage(null);
				column.setAlternateValues(new AlternateValues());
				// column.setPage(presenter.page.getIdentifier());
				column.setValid(true);
				columnRowList.add(column);
			}
		}
		return row;
	}

	@EventHandler
	public void handleDefaultCellSelectionEndEvent(final DefaultCellSelectionEndEvent defaultCellSelectionEndEvent) {
		String dataTableName = view.getTableName();
		if (null != defaultCellSelectionEndEvent && !StringUtil.isNullOrEmpty(dataTableName)
				&& dataTableName.equalsIgnoreCase(ReviewValidateNavigator.getInsertionScriptTableName())) {
			Scheduler scheduler = new SchedulerImpl();
			ScheduledCommand command = new ScheduledCommand() {

				@Override
				public void execute() {
					selectPageAfterInsertion(pageBeforeScriptExecution, ReviewValidateNavigator.getBeforeInsertionRowIndex());
					Timer timer = new Timer() {

						@Override
						public void run() {
							ReviewValidateNavigator.setInsertionScriptTableName(null);
						}
					};
					timer.schedule(3000);
				}
			};
			scheduler.scheduleDeferred(command);
		}
	}

	public void executeTableInsertionScript(final int rowIndex) {
	}

	private void selectPageAfterInsertion(final Page pageToSelect, final int rowToFocus) {
		if (pageToSelect != null) {
			ReviewValidateEventBus.fireEvent(new PageSelectionEvent(pageToSelect.getIdentifier()));
			view.select(rowToFocus);
		}
	}
}
