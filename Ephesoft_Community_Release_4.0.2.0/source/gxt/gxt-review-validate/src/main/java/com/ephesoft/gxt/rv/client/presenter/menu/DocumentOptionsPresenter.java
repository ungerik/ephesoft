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

package com.ephesoft.gxt.rv.client.presenter.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DataTable.Rows;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.HeaderRow;
import com.ephesoft.dcma.batch.schema.HeaderRow.Columns;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.client.ui.widget.DataTableGrid;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.util.CookieUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.event.DocumentOpenEvent;
import com.ephesoft.gxt.rv.client.event.DocumentSelectionEvent;
import com.ephesoft.gxt.rv.client.event.DocumentTreeCreationEvent;
import com.ephesoft.gxt.rv.client.event.DocumentTypeChangeEvent;
import com.ephesoft.gxt.rv.client.event.FocusInitializationEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.DocumentTypeFieldFocusEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.FuzzyFieldFocusInitializationEvent;
import com.ephesoft.gxt.rv.client.presenter.ReviewValidateBasePresenter;
import com.ephesoft.gxt.rv.client.view.menu.DocumentOptionsView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.ThumbnailWidgetPanel;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class DocumentOptionsPresenter extends ReviewValidateBasePresenter<DocumentOptionsView> {

	interface CustomEventBinder extends EventBinder<DocumentOptionsPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private Element lastSelectedFuzzyRow;

	public DocumentOptionsPresenter(final ReviewValidateController controller, final DocumentOptionsView view) {
		super(controller, view);
	}

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void handleDocumentTreeCreationEvent(final DocumentTreeCreationEvent documentTreeCreationEvent) {
		if (null != documentTreeCreationEvent) {
			final List<String> documentTypeList = ReviewValidateNavigator.getDocumentTypeList();
			view.addDocumentTypes(documentTypeList);
			boolean isFuzzyPanelVisible = ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_VALIDATION;
			view.setDocumentTypeComboWidth(isFuzzyPanelVisible ? 150 : 320);
		}
	}

	@EventHandler
	public void handleDocumentOpenEvent(final DocumentOpenEvent documentOpenEvent) {
		if (null != documentOpenEvent) {
			final ThumbnailWidgetPanel bindedWidget = documentOpenEvent.getDocumentThumbnail();
			final Document bindedDocument = bindedWidget == null ? null : bindedWidget.getDocument();
			if (null != bindedDocument) {
				view.selectDocumentType(bindedDocument.getType());
			}
			if (ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_REVIEW) {
				view.setFocusOnDocumentTypeField();
			}
		}
	}

	@EventHandler
	public void handleFocusInitializationEvent(final FocusInitializationEvent focusInitializationEvent) {
		if (null != focusInitializationEvent
				&& ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_REVIEW) {
			view.setFocusOnDocumentTypeField();
		}
	}

	@EventHandler
	public void handleFuzzyFocusInitializationEvent(final FuzzyFieldFocusInitializationEvent fuzzyFocusInitializationEvent) {
		if (null != fuzzyFocusInitializationEvent) {
		}
	}

	public void setDocumentType(final String newDocumentType) {
		if (!StringUtil.isNullOrEmpty(newDocumentType)) {
			final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			if (null != currentDocument) {
				final String currentDocumentType = currentDocument.getType();
				if (!newDocumentType.equalsIgnoreCase(currentDocumentType)) {
					currentDocument.setType(newDocumentType);
					currentDocument.setReviewed(false);
					ReviewValidateNavigator.validateDocument(currentDocument.getIdentifier(), false);
					ReviewValidateEventBus.fireEvent(new DocumentTypeChangeEvent(currentDocument));
					ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
				}
			}
		}
	}

	@EventHandler
	public void handleDocumentTypeFieldFocusInitializationEvent(final DocumentTypeFieldFocusEvent documentTypeFieldFocusEvent) {
		if (null != documentTypeFieldFocusEvent) {
			view.setFocusOnDocumentTypeField();
			view.selectDocumentTypeText();
		}
	}

	public void doFuzzySearch(final String textSearch) {
		if (!StringUtil.isNullOrEmpty(textSearch)) {
			String currentBatchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
			Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			if (null != currentDocument && !StringUtil.isNullOrEmpty(currentBatchInstanceIdentifier)) {
				String currentDocumentType = currentDocument.getType();
			}
		}
	}

	private DataTable getDataTable(List<List<String>> result) {
		DataTable table = null;
		if (!CollectionUtil.isEmpty(result)) {
			table = new DataTable();
			List<String> columnHeaders = result.get(0);
			HeaderRow headerRow = getHeaderRow(columnHeaders);
			List<String> cookieMergedColumns = getOrderedFuzzyColumn(columnHeaders);
			HeaderRow cookieHeaderRow = getHeaderRow(cookieMergedColumns);
			table.setHeaderRow(cookieHeaderRow);
			Rows rows = new Rows();
			table.setRows(rows);
			List<Row> rowList = rows.getRow();
			if (null != headerRow) {
				int headersSize = columnHeaders.size();
				for (int i = 1; i < result.size(); i++) {
					List<String> data = result.get(i);
					if (!CollectionUtil.isEmpty(data) && headersSize == data.size()) {
						Row newRow = getRow(headerRow, data);
						rowList.add(newRow);
						newRow.setIsRuleValid(true);
					}
				}
			}
		}
		return table;
	}

	private void highlightSelectedRow(final DataTableGrid grid) {
		if (null != grid) {
			grid.getSelectionModel().addCellSelectionChangedHandler(new CellSelectionChangedHandler<Row>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<Row> event) {
					int row = com.ephesoft.gxt.core.client.util.EventUtil.getSelectedGridRow(event);
					if (row != -1) {
						Element rowElement = grid.getView().getRow(row);
						if (null != rowElement) {
							if (null != lastSelectedFuzzyRow) {
								lastSelectedFuzzyRow.removeClassName("selectedRow");
							}
							rowElement.addClassName("selectedRow");
							lastSelectedFuzzyRow = rowElement;
						}
					}
				}
			});
		}
	}

	private void setFuzzySelection(final DataTableGrid fuzzyGrid, final DialogWindow window) {
		if (null != window && null != fuzzyGrid) {
			window.setDialogListener(new DialogAdapter() {

				@Override
				public void onCancelClick() {
					super.onCancelClick();
					window.hide();
				}

				@Override
				public void onOkClick() {
					super.onOkClick();
					Row currentRow = fuzzyGrid.getCurrentRow();
					Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
					if (null != currentDocument && null != currentRow) {
						String documentTypeName = currentDocument.getType();
						if (!StringUtil.isNullOrEmpty(documentTypeName)) {
							DocumentTypeDTO documentType = ReviewValidateNavigator.getDocumentTypeByName(documentTypeName);
							if (null != documentType) {
								performFuzzyExtractionResult(documentType, currentRow);
							}
						}
						window.hide();
					}
				}
			});
		}
	}

	private void performFuzzyExtractionResult(final DocumentTypeDTO documentType, final Row currentRow) {
		bindFuzzyMapping(currentRow);

	}

	private void bindFuzzyMapping(Row currentRow) {
		if (null != currentRow) {
			Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			com.ephesoft.dcma.batch.schema.Row.Columns columns = currentRow.getColumns();
			if (null != columns) {
				List<Column> listOfColumns = columns.getColumn();
				if (!CollectionUtil.isEmpty(listOfColumns)) {
					for (Column column : listOfColumns) {
						if (null != column) {
							String indexFieldName = column.getName();
							DocField field = BatchSchemaUtil.getDocFieldByName(currentDocument, indexFieldName);
							if (null != field) {
								field.setValue(column.getValue());
							}
						}
					}
				}
				ReviewValidateEventBus.fireEvent(new DocumentSelectionEvent(currentDocument.getIdentifier()));
				ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
			}
		}
	}

	private HeaderRow getHeaderRow(List<String> headersList) {
		HeaderRow headerRow = null;
		if (!CollectionUtil.isEmpty(headersList)) {
			headerRow = new HeaderRow();
			final Columns headerColumns = new Columns();
			headerRow.setColumns(headerColumns);
			List<Column> headerColumnList = headerColumns.getColumn();
			for (final String header : headersList) {
				String actualHeader = header == null ? CoreCommonConstant.EMPTY_STRING : header;
				Column headerColumn = new Column();
				headerColumn.setName(actualHeader);
				headerColumnList.add(headerColumn);
			}
		}
		return headerRow;
	}

	private Row getRow(final HeaderRow headerRow, final List<String> columnValues) {
		Row row = null;
		if (null != headerRow && !CollectionUtil.isEmpty(columnValues)) {
			row = new Row();
			com.ephesoft.dcma.batch.schema.Row.Columns columns = new com.ephesoft.dcma.batch.schema.Row.Columns();
			row.setColumns(columns);
			List<Column> valueColumns = columns.getColumn();
			List<Column> columnList = headerRow.getColumns().getColumn();
			int index = 0;
			for (final String columnValue : columnValues) {
				Column newColumn = new Column();
				newColumn.setName(columnList.get(index++).getName());
				newColumn.setValue(columnValue);
				newColumn.setValid(true);
				valueColumns.add(newColumn);
			}
		}
		return row;
	}

	private void addFuzzyResultToCookie(final DialogWindow window, final DataTableGrid grid) {
		window.addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent event) {
				String columnOrderCookieName = getFuzzyResultCookieName();
				String hiddenColumnCookieName = getFuzzyResultHiddenCookieName();
				if (!StringUtil.isNullOrEmpty(hiddenColumnCookieName) && !StringUtil.isNullOrEmpty(columnOrderCookieName)) {
					setFuzzyCookies(grid, columnOrderCookieName, hiddenColumnCookieName);
				}
			}
		});
	}

	private void setFuzzyCookies(final DataTableGrid grid, final String columnOrderCookieName, final String hiddenColumnCookieName) {
		final Object[] columnNames = grid.getColumnNames().toArray();
		final Object[] hiddenColumnNames = grid.getHiddenColumnNames().toArray();
		String columnNamesValue = StringUtil.concatenateUsingSeperator(CoreCommonConstant.COMMA, columnNames);
		String columnHiddenValue = StringUtil.concatenateUsingSeperator(CoreCommonConstant.COMMA, hiddenColumnNames);
		CookieUtil.storeCookie(columnOrderCookieName, columnNamesValue);
		CookieUtil.storeCookie(hiddenColumnCookieName, columnHiddenValue);
	}

	private String getFuzzyResultCookieName() {
		return getFuzzyCookie("fuzzy-column-order");
	}

	private String getFuzzyResultHiddenCookieName() {
		return getFuzzyCookie("fuzzy-hidden-column");
	}

	private String getFuzzyCookie(String prefix) {
		String cookieName = null;
		Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
		String documentType = currentDocument.getType();
		if (!StringUtil.isNullOrEmpty(documentType)) {
			String batchClassIdentifier = ReviewValidateNavigator.getBatchClassIdentifier();
			cookieName = StringUtil.concatenate(prefix, CoreCommonConstant.DOT, batchClassIdentifier, CoreCommonConstant.DOT,
					documentType);
		}
		return cookieName;
	}

	private List<String> getOrderedFuzzyColumn(final List<String> tableResult) {
		List<String> cookieColumns = null;
		String fuzzyResultCookieName = getFuzzyResultCookieName();
		String columnsCookieValue = CookieUtil.getCookieValue(fuzzyResultCookieName);
		if (!StringUtil.isNullOrEmpty(columnsCookieValue)) {
			String[] columnNameArray = columnsCookieValue.split(CoreCommonConstant.COMMA);
			cookieColumns = Arrays.asList(columnNameArray);
		}
		return getOrderedFuzzyColumn(cookieColumns, tableResult);
	}

	private List<String> getOrderedFuzzyColumn(final List<String> cookieValues, final List<String> tableResult) {
		List<String> finalResult = null;
		if (!CollectionUtil.isEmpty(cookieValues) && !CollectionUtil.isEmpty(tableResult)) {
			finalResult = new ArrayList<String>();
			for (String cookie : cookieValues) {
				if (tableResult.contains(cookie)) {
					finalResult.add(cookie);
				}
			}
			for (String columnName : tableResult) {
				if (!finalResult.contains(columnName)) {
					finalResult.add(columnName);
				}
			}
		}
		return finalResult == null ? new ArrayList<String>(tableResult) : finalResult;
	}

	private List<String> getColumnsToHide() {
		String hiddenColumnCookieName = getFuzzyResultHiddenCookieName();
		List<String> hiddenColumnList = null;
		String hiddenColumnCookievalue = CookieUtil.getCookieValue(hiddenColumnCookieName);
		if (!StringUtil.isNullOrEmpty(hiddenColumnCookievalue)) {
			String[] hiddenColumns = hiddenColumnCookievalue.split(CoreCommonConstant.COMMA);
			hiddenColumnList = Arrays.asList(hiddenColumns);
		}
		return hiddenColumnList;
	}
}
