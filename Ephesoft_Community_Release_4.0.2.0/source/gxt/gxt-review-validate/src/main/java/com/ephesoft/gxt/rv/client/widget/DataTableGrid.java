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

package com.ephesoft.gxt.rv.client.widget;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Column.AlternateValues;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DataTable.Rows;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.gxt.core.client.constant.NativeKeyCodes;
import com.ephesoft.gxt.core.client.ui.widget.OverlayImage.Overlay;
import com.ephesoft.gxt.core.client.validator.RowValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.event.FieldSelectionEvent;
import com.ephesoft.gxt.rv.client.event.PageSelectionEvent;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.resource.ColumnValueProvider;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent.BeforeQueryHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.StringComboBox;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.selection.CellSelection;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

@SuppressWarnings("rawtypes")
public class DataTableGrid extends com.sencha.gxt.widget.core.client.grid.Grid<Row> {

	private GridInlineEditing<Row> editingGrid;

	private final CellSelectionModel<Row> cellSelectionModel;

	private CellSelection<Row> lastSelectedCell;

	private final DataTable bindedTable;

	private static final SelectedRowValueProvider selectedRowValueProvider;

	private Row manualExtractionSampleRow;

	private final Map<String, Boolean> manualExtractionValidationPattern;

	private static final int SELECTION_COLUMN_WIDTH = 22;

	private static final int SELECTION_CHECKBOX_INDEX = 0;

	private GridCell copyFromCell;

	private Overlay overlay;

	static {
		selectedRowValueProvider = new SelectedRowValueProvider();
	}

	public DataTableGrid(final DataTable dataTable) {
		super(getListStore(dataTable), getColumnModel(dataTable));
		this.bindedTable = dataTable;
		this.addStyleName("dataTableGrid");
		this.getView().setForceFit(true);
		manualExtractionValidationPattern = new HashMap<String, Boolean>();
		cellSelectionModel = new CellSelectionModel<Row>() {

			@Override
			protected void onKeyPress(final NativeEvent nativeEvent) {

				final int nativeKeyCode = nativeEvent.getKeyCode();
				if (!((nativeEvent.getShiftKey()) && (nativeKeyCode == KeyCodes.KEY_DOWN || nativeKeyCode == KeyCodes.KEY_UP
						|| nativeKeyCode == KeyCodes.KEY_RIGHT || nativeKeyCode == KeyCodes.KEY_LEFT))) {
					super.onKeyPress(nativeEvent);
				}
			}
		};
		this.setSelectionModel(cellSelectionModel);
		this.setEditable();
		this.getView().setColumnLines(true);
		this.getView().setAutoFill(true);
		this.addCompleteEditHandler();
		this.setColumnReordering(true);
		this.setWidth("100%");
		store.setAutoCommit(true);
		this.getView().setViewConfig(getViewConfig());
		this.addKeyDownHandler();
		this.addCellSelectionChangeHandler();
		this.addSelectionHandler();
		this.setCurrentColumnCellSelection();
		this.addMouseMoveHandler();
		this.disableContextMenu();
	}

	private void disableContextMenu() {
		this.addDomHandler(new ContextMenuHandler() {

			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();
			}
		}, ContextMenuEvent.getType());
	}

	@Override
	protected void onMouseDown(Event event) {
		super.onMouseDown(event);
		if (event.getButton() == Event.BUTTON_RIGHT) {
			if (overlay == null) {
				copyFromCell = getCell(event);
				if (copyFromCell != null && copyFromCell.getCol() > 0) {
					overlay = new Overlay();
					disableOverlayContextMenu(overlay);
					overlay.setPosition(event.getClientX(), event.getClientY());
					RootPanel.get().add(overlay);
				}
			} else {
				RootPanel.get().remove(overlay);
				overlay = null;
				GridCell mouseUpCell = getCell(event);
				if (copyFromCell != null && mouseUpCell != null) {
					int fromRow = copyFromCell.getRow();
					int toRow = mouseUpCell.getRow();
					int fromColumn = copyFromCell.getCol();
					int toColumn = mouseUpCell.getCol();
					copyValues(fromRow, toRow, fromColumn, toColumn);
					refresh();
					cellSelectionModel.selectCell(toRow, toColumn);
					Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
					ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
				}
			}
		}
	}

	private void copyValues(final int fromRow, final int toRow, final int fromColumn, final int toColumn) {
		if (fromRow < toRow && fromColumn <= toColumn) {
			Row initialRow = store.get(fromRow);
			for (int colIndex = fromColumn; colIndex <= toColumn; colIndex++) {
				ValueProvider valueProvider = cm.getValueProvider(colIndex);
				if (valueProvider instanceof ColumnValueProvider) {
					ColumnValueProvider columnValueProvider = ((ColumnValueProvider) valueProvider);
					String value = columnValueProvider.getValue(initialRow);
					for (int rowIndex = fromRow + 1; rowIndex <= toRow; rowIndex++) {
						Row rowToCopy = store.get(rowIndex);
						columnValueProvider.setValue(rowToCopy, value);
					}
				}
			}
		}
	}

	private void disableOverlayContextMenu(final Overlay overLay) {
		if (null != overLay) {
			overLay.addDomHandler(new ContextMenuHandler() {

				@Override
				public void onContextMenu(ContextMenuEvent event) {
					event.preventDefault();
				}
			}, ContextMenuEvent.getType());
		}
	}

	private void addMouseMoveHandler() {
		this.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (null != overlay) {
					int absoluteLeft = overlay.getAbsoluteLeft();
					int absoluteTop = overlay.getAbsoluteTop();
					int mouseLeft = event.getClientX();
					int mouseTop = event.getClientY();
					if (mouseLeft > absoluteLeft && mouseTop > absoluteTop) {
						overlay.setWidth(mouseLeft - absoluteLeft);
						overlay.setHeight(mouseTop - absoluteTop);
					}
				}
			}
		}, MouseMoveEvent.getType());
	}

	@Override
	protected void onMouseUp(Event event) {
		super.onMouseUp(event);
	}

	private GridCell getCell(Event event) {
		GridCell cell = null;
		Element target = Element.as(event.getEventTarget());
		int rowIndex = view.findRowIndex(target);
		if (rowIndex != -1) {
			int colIndex = view.findCellIndex(target, null);
			cell = new GridCell(rowIndex, colIndex);
		}
		return cell;
	}

	private void addSelectionHandler() {
		final ValueProvider selectionValueProvider = cm.getValueProvider(SELECTION_CHECKBOX_INDEX);
		if (selectionValueProvider instanceof SelectedRowValueProvider) {
			final ColumnConfig<Row, ?> columnConfig = cm.getColumn(SELECTION_CHECKBOX_INDEX);
			final Widget selectionWidget = columnConfig.getWidget();
			if (selectionWidget instanceof CheckBox) {
				final CheckBox selectionCheckBox = (CheckBox) selectionWidget;
				selectionCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(final ValueChangeEvent<Boolean> event) {
						final Boolean selectionValue = selectionCheckBox.getValue();
						if (null != selectionValue) {
							selectAll(selectionValue);
							cellSelectionModel.selectCell(0, 1);
						}
					}
				});
			}
		}
	}

	private void setCurrentColumnCellSelection() {
		cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<Row>() {

			@Override
			public void onCellSelectionChanged(final CellSelectionChangedEvent<Row> event) {
				final CellSelection<Row> currentSelection = cellSelectionModel.getSelectCell();
				if (null != currentSelection) {
					final int column = currentSelection.getCell();
					final int row = currentSelection.getRow();
					final ValueProvider valueProvider = cm.getValueProvider(column);
					Column currentColumn = null;
					final Element cellElement = DataTableGrid.this.getView().getCell(row, column);
					if (valueProvider instanceof ColumnValueProvider) {
						currentColumn = ((ColumnValueProvider) valueProvider).getColumn(store.get(row));
					}
					if (null != cellElement) {
						cellElement.scrollIntoView();
					}
					final String pageIdentifier = currentColumn == null ? null : currentColumn.getPage();
					lastSelectedCell = currentSelection;
					if (ReviewValidateNavigator.isPageSelectionEnable()) {
						ReviewValidateEventBus.fireEvent(new PageSelectionEvent(pageIdentifier));
					}
					ReviewValidateNavigator.setCurrentSelectedColumnField(currentColumn);
					ReviewValidateNavigator.setCurrentGrid(DataTableGrid.this);
					ReviewValidateEventBus.fireEvent(new FieldSelectionEvent(currentColumn));
				}
			}
		});
	}

	public void refresh() {
		this.getView().refresh(true);
		if (null != lastSelectedCell) {
			final int column = lastSelectedCell.getCell();
			final int row = lastSelectedCell.getRow();
			cellSelectionModel.selectCell(row, column);
		}
	}

	public void addCellSelectionChangeHandler() {
		cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<Row>() {

			@Override
			public void onCellSelectionChanged(final CellSelectionChangedEvent<Row> event) {
				if (null != event) {
					final List<CellSelection<Row>> selectionList = event.getSelection();
					if (!CollectionUtil.isEmpty(selectionList)) {
						final CellSelection<Row> selectedCell = selectionList.get(0);
						if (null != selectedCell) {
							lastSelectedCell = selectedCell;
						}
					}
				}
			}
		});
	}

	public int getCurrentRowSelectionIndex() {
		int currentSelectedRowIndex = -1;
		final CellSelection<Row> currentSelection = cellSelectionModel.getSelectCell();
		if (null != currentSelection) {
			currentSelectedRowIndex = currentSelection.getRow();
		}
		return currentSelectedRowIndex;
	}

	public void select(final int rowIndex) {
		int rowToSelect = rowIndex;
		final int totalRows = store.size();
		if (totalRows > 0) {
			if (rowIndex < 0) {
				rowToSelect = 0;
			} else {
				if (rowToSelect >= totalRows) {
					rowToSelect = totalRows - 1;
				}
			}
			cellSelectionModel.selectCell(rowToSelect, 1);
		}
	}

	public void deleteCurrentRow() {
		final Row currentRow = cellSelectionModel.getSelectedItem();
		this.deleteRow(currentRow);
	}

	public void deleteRow(final Row rowToDelete) {
		if (null != rowToDelete) {
			store.remove(rowToDelete);
			final Rows rows = bindedTable.getRows();
			if (null != rows) {
				rows.getRow().remove(rowToDelete);
			}
		}
	}

	private void addKeyDownHandler() {
		this.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(final KeyDownEvent event) {
				final int nativeKeyCode = event.getNativeKeyCode();
				if (event.isControlKeyDown()) {
					switch (nativeKeyCode) {
						case KeyCodes.KEY_B:
							event.preventDefault();
							if (null != lastSelectedCell) {
								final int column = lastSelectedCell.getCell();
								final int row = lastSelectedCell.getRow();
								final ColumnModel<Row> columnModel = DataTableGrid.this.getColumnModel();
								if (null != columnModel) {
									final ColumnConfig<Row, String> gridColumnConfig = columnModel.getColumn(column);
									if (null != gridColumnConfig) {
										final ValueProvider<?, ?> valueProvider = gridColumnConfig.getValueProvider();
										if (valueProvider instanceof ColumnValueProvider) {
											((ColumnValueProvider) valueProvider).toggleApplyValidation();
											getView().refresh(false);
											cellSelectionModel.selectCell(row, column);
										}
									}
								}
							}
							break;
						case NativeKeyCodes.SINGLE_QUOTE:
							event.preventDefault();
							Column currentColumn = getCurrentColumn();
							toggleManualColumnPattern(currentColumn);
							refresh();
							break;
					}

				} else {
					if (KeyCodes.KEY_SPACE == nativeKeyCode) {
						if (handleRowSelection()) {
							event.preventDefault();
						}
					}
				}
			}
		}, KeyDownEvent.getType());
	}

	public void cancelEditing() {
		editingGrid.cancelEditing();
	}

	public boolean handleRowSelection() {
		final Row selectedItem = cellSelectionModel.getSelectedItem();
		final CellSelection<Row> currentSelection = cellSelectionModel.getSelectCell();
		boolean isRowSelected = false;
		if (null != currentSelection && null != selectedItem) {
			final int column = currentSelection.getCell();
			final int row = currentSelection.getRow();
			final ValueProvider currentValueProvider = cm.getValueProvider(column);
			if (currentValueProvider instanceof SelectedRowValueProvider) {
				final SelectedRowValueProvider selectionProvider = (SelectedRowValueProvider) currentValueProvider;
				final boolean isSelected = selectionProvider.getValue(selectedItem);
				selectionProvider.setValue(selectedItem, !isSelected);
				this.getView().refresh(false);
				cellSelectionModel.selectCell(row, column);
				isRowSelected = true;
			}
		}
		return isRowSelected;
	}

	public Row getManualExtractionSampleRow() {
		return manualExtractionSampleRow;
	}

	public Row getCurrentRow() {
		return cellSelectionModel.getSelectedItem();
	}

	public Column getCurrentColumn() {
		final Row currentRow = cellSelectionModel.getSelectedItem();
		Column column = null;
		if (null != currentRow) {
			final int columnIndex = getCurrentColumnIndex();
			if (columnIndex != -1) {
				final ValueProvider currentValueProvider = cm.getValueProvider(columnIndex);
				column = currentValueProvider instanceof ColumnValueProvider ? ((ColumnValueProvider) currentValueProvider)
						.getColumn(currentRow) : null;
			}
		}
		return column;
	}

	public List<Row> getRowsAfter(final Row row) {
		List<Row> rowsList = null;
		if (null != row) {
			final int rowIndex = store.indexOf(row);
			rowsList = store.subList(rowIndex + 1, store.size());
		}
		return rowsList;
	}

	public boolean isValidManuallyExtractedRow(final Row model) {
		boolean isValid = false;
		if (null != model) {
			isValid = true;
			int totalColumn = cm.getColumnCount();
			ValueProvider valueProvider;
			ColumnValueProvider columnValueProvider;
			Column column;
			for (int columnIndex = 0; columnIndex < totalColumn; columnIndex++) {
				valueProvider = cm.getValueProvider(columnIndex);
				if (valueProvider instanceof ColumnValueProvider) {
					columnValueProvider = (ColumnValueProvider) valueProvider;
					column = columnValueProvider.getColumn(model);
					if (isApplyValidationPatternOnColumn(column) && !columnValueProvider.isValid(model)) {
						isValid = false;
						break;
					}
				}
			}
		}
		return isValid;
	}

	private int getCurrentColumnIndex() {
		final CellSelection<Row> currentSelection = cellSelectionModel.getSelectCell();
		int currentSelectionColumnIndex = -1;
		if (null != currentSelection) {
			currentSelectionColumnIndex = currentSelection.getCell();
		}
		return currentSelectionColumnIndex;
	}

	private GridViewConfig<Row> getViewConfig() {
		final GridViewConfig<Row> config = new GridViewConfig<Row>() {

			@Override
			public String getColStyle(final Row model, final ValueProvider<? super Row, ?> valueProvider, final int rowIndex,
					final int colIndex) {
				String cssStyle = CoreCommonConstant.EMPTY_STRING;
				if (valueProvider instanceof ColumnValueProvider) {
					final ColumnValueProvider columnValueProvider = (ColumnValueProvider) valueProvider;
					final Column currentColumn = columnValueProvider.getColumn(model);
					if (model == manualExtractionSampleRow && isApplyValidationPatternOnColumn(currentColumn)) {
						cssStyle = "manualValidatedColumn";
					} else {
						if (!columnValueProvider.isValid(model)) {
							cssStyle = "invalidColumn";
						}
					}
				}
				return cssStyle;
			}

			@Override
			public String getRowStyle(final Row model, final int rowIndex) {
				String cssStyle = CoreCommonConstant.EMPTY_STRING;
				if (null != bindedTable) {
					final Document cuurentDocument = ReviewValidateNavigator.getCurrentDocument();
					final DocumentTypeDTO documentType = ReviewValidateNavigator.getDocumentTypeForDocument(cuurentDocument);
					final TableInfoDTO table = ReviewValidateNavigator.getTableByName(documentType, bindedTable.getName());
					cssStyle = RowValidator.isValid(model, table) ? CoreCommonConstant.EMPTY_STRING : "invalidRow";
				}

				return cssStyle;
			}
		};
		return config;
	}

	private void addCompleteEditHandler() {
		editingGrid.addCompleteEditHandler(new CompleteEditHandler<Row>() {

			@Override
			public void onCompleteEdit(final CompleteEditEvent<Row> event) {
				final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
				ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
				DataTableGrid.this.getStore().commitChanges();
			}
		});
	}

	private void setEditable() {
		editingGrid = new GridInlineEditing<Row>(this) {

			@Override
			protected <N, O> void doStartEditing(final com.sencha.gxt.widget.core.client.grid.Grid.GridCell cell) {
				super.doStartEditing(cell);
				final ColumnModel<Row> columnModel = getColumnModel();
				if (null != columnModel) {
					final ColumnConfig<Row, ?> columnConfig = columnModel.getColumn(cell.getCol());
					if (null != columnConfig) {
						final IsField editor = getEditor(columnConfig);
						final Element cellElement = DataTableGrid.this.getView().getCell(cell.getRow(), cell.getCol());
						if (editor instanceof Field) {
							final Field field = (Field) editor;
							final int client_y = cellElement.getOffsetTop();
							final int client_x = cellElement.getOffsetLeft();
							final int margin = Math.abs(cellElement.getAbsoluteLeft() - client_x) / 2;
							final int width = cellElement.getAbsoluteRight() - client_x - margin;
							field.setWidth(width);
							field.setPosition(client_x, client_y);
							if (field instanceof ColumnSuggestionBox) {
								ColumnSuggestionBox columnSuggestionBox = (ColumnSuggestionBox) field;
								addAlternateValue(columnSuggestionBox, cell);
							}
						}
					}
				}
			}
		};
		this.setEditors();
	}

	private void addAlternateValue(final ColumnSuggestionBox columnSuggestionBox, final GridCell cell) {
		final int rowIndex = cell.getRow();
		final int columnIndex = cell.getCol();
		final Row row = store.get(rowIndex);
		final ValueProvider valueProvider = cm.getValueProvider(columnIndex);
		if (null != row && valueProvider instanceof ColumnValueProvider) {
			final Column column = ((ColumnValueProvider) valueProvider).getColumn(row);
			columnSuggestionBox.setColumn(column);
		}
	}

	public Row getFirstInvalidRuleViolationRow() {
		final int totalRecords = store.size();
		Row currentRow = null;
		Row firstInValidRow = null;
		for (int currentRowIndex = 0; currentRowIndex < totalRecords; currentRowIndex++) {
			currentRow = store.get(currentRowIndex);
			if (null != currentRow) {
				final Document cuurentDocument = ReviewValidateNavigator.getCurrentDocument();
				final DocumentTypeDTO documentType = ReviewValidateNavigator.getDocumentTypeForDocument(cuurentDocument);
				final TableInfoDTO tableInfo = ReviewValidateNavigator.getTableByName(documentType, bindedTable.getName());
				if (!RowValidator.isValid(currentRow, tableInfo)) {
					firstInValidRow = currentRow;
					break;
				}
			}
		}
		return firstInValidRow;
	}

	public GridCell getFirstColumnValueViolation() {
		final int totalColumns = cm.getColumnCount();
		final int totalRows = store.size();
		GridCell invalidCell = null;
		ValueProvider currentValueProvider = null;
		Row currentRow = null;
		for (int currentRowIndex = 0; currentRowIndex < totalRows; currentRowIndex++) {
			currentRow = store.get(currentRowIndex);
			for (int currentColumnIndex = 0; currentColumnIndex < totalColumns; currentColumnIndex++) {
				currentValueProvider = cm.getValueProvider(currentColumnIndex);
				if (currentValueProvider instanceof ColumnValueProvider) {
					if (!((ColumnValueProvider) currentValueProvider).isValid(currentRow)) {
						invalidCell = new GridCell(currentRowIndex, currentColumnIndex);
						break;
					}
				}
			}
			if (null != invalidCell) {
				break;
			}
		}
		return invalidCell;
	}

	public void selectFirstInvalidCell() {
		final Row firstInvalidRow = getFirstInvalidRuleViolationRow();
		final GridCell firstInvalidCell = getFirstColumnValueViolation();
		final int totalRows = store.size();
		final int columnViolatedRowIndex = firstInvalidCell == null ? Integer.MAX_VALUE : firstInvalidCell.getRow();
		final int ruleViolatedRowIndex = firstInvalidRow == null ? Integer.MAX_VALUE : store.indexOf(firstInvalidRow);
		final int firstInvalidRowIndex = Math.min(columnViolatedRowIndex, ruleViolatedRowIndex);
		if (firstInvalidRowIndex < totalRows) {
			if (firstInvalidRowIndex == ruleViolatedRowIndex) {
				cellSelectionModel.selectCell(firstInvalidRowIndex, 0);
			} else {
				if (null != firstInvalidCell) {
					final int column = firstInvalidCell.getCol();
					cellSelectionModel.selectCell(firstInvalidRowIndex, column);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void setEditors() {
		final int totalColumn = cm.getColumnCount();
		ColumnConfig colConfig;
		ValueProvider valueProvider;
		for (int columnIndex = 0; columnIndex < totalColumn; columnIndex++) {
			colConfig = cm.getColumn(columnIndex);
			valueProvider = cm.getValueProvider(columnIndex);
			if (valueProvider instanceof ColumnValueProvider) {
				editingGrid.addEditor(colConfig, new ColumnSuggestionBox());
			}
		}
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

	private static CheckBox getSelectionCheckBox() {
		final CheckBox headerCheckBox = new CheckBox();
		headerCheckBox.addStyleName("headerCheckBox");
		return headerCheckBox;
	}

	private static ColumnModel<Row> getColumnModel(final DataTable dataTable) {
		final List<ColumnConfig<Row, ?>> columnConfigList = new LinkedList<ColumnConfig<Row, ?>>();
		if (null != dataTable) {
			final List<Column> headerColumnList = BatchSchemaUtil.getHeaderColumn(dataTable);
			if (!CollectionUtil.isEmpty(headerColumnList)) {
				final ColumnConfig<Row, Boolean> selectionColumn = new ColumnConfig<Row, Boolean>(selectedRowValueProvider);
				columnConfigList.add(selectionColumn);
				selectionColumn.setWidth(SELECTION_COLUMN_WIDTH);
				selectionColumn.setCell(getSelectionCell());
				selectionColumn.setWidget(getSelectionCheckBox(), SafeHtmlUtils.fromSafeConstant("."));
				selectionColumn.setSortable(false);
				selectionColumn.setFixed(true);
				selectionColumn.setMenuDisabled(true);
				ColumnConfig<Row, String> columnConfig;
				ColumnValueProvider valueProvider;
				for (final Column headerColumn : headerColumnList) {
					valueProvider = new ColumnValueProvider(headerColumn, dataTable.getName());
					columnConfig = new ColumnConfig<Row, String>(valueProvider);
					columnConfig.setFixed(false);
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

	private static CheckBoxCell getSelectionCell() {
		final CheckBoxCell selectionCell = new CheckBoxCell();
		selectionCell.setSelectOnFocus(true);
		return selectionCell;
	}

	public void addRow(final Row rowToAdd) {
		this.addRow(rowToAdd, false, false);
	}

	private void selectAll(final boolean selection) {
		final CellSelection<Row> currentSelection = cellSelectionModel.getSelectCell();
		final int totalRows = store.size();
		Row currentRow;
		for (int currentRowIndex = 0; currentRowIndex < totalRows; currentRowIndex++) {
			currentRow = store.get(currentRowIndex);
			selectedRowValueProvider.setValue(currentRow, selection);
		}
		this.getView().refresh(false);
		if (null != currentSelection) {
			cellSelectionModel.selectCell(currentSelection.getRow(), currentSelection.getCell());
		}
	}

	public void addAll(final List<Row> listToAdd) {
		int index = 0;
		final CellSelection<Row> cellSelection = cellSelectionModel.getSelectCell();
		if (null != cellSelection) {
			index = cellSelection.getRow() + 1;
		}
		int size = store.size();
		if(index > size) {
			index = size;
		}
		if (!CollectionUtil.isEmpty(listToAdd)) {
			store.addAll(index, listToAdd);
			getRowList().addAll(index, listToAdd);
		}
		cellSelectionModel.selectCell(index, 0);
	}

	public void removeManualExtractionRow() {
		if (manualExtractionSampleRow != null) {
			final int currentRowIndex = getCurrentRowSelectionIndex();
			final int manualExtractionRowIndex = store.indexOf(manualExtractionSampleRow);
			store.remove(manualExtractionSampleRow);
			manualExtractionSampleRow = null;
			final int indexToSelect = manualExtractionRowIndex > currentRowIndex ? currentRowIndex : currentRowIndex - 1;
			select(indexToSelect);
		}
	}

	public boolean deleteAll(boolean selectedOnly) {
		int totalRows = store.size();
		boolean isDeleted = false;
		Row currentRow;
		for (int row = 0; row < totalRows; row++) {
			currentRow = store.get(row);
			if (!selectedOnly || isSelected(currentRow)) {
				deleteRow(currentRow);
				isDeleted = true;
				totalRows--;
				row--;
			}
		}
		if (isDeleted) {
			select(0);
		}
		return isDeleted;
	}

	public boolean isValid() {
		return getFirstInvalidRuleViolationRow() == null && getFirstColumnValueViolation() == null;
	}

	public void startEditing(final Row rowToEdit) {
		if (null != rowToEdit) {
			final int rowIndex = store.indexOf(rowToEdit);
			editingGrid.startEditing(new GridCell(rowIndex, 1));
		}
	}

	public void addRow(final Row rowToAdd, final boolean beforeCurrentCell) {
		this.addRow(rowToAdd, beforeCurrentCell, true);
	}

	public void addRow(final Row rowToAdd, final boolean beforeCurrentCell, final boolean addToModel) {
		if (null != rowToAdd && null != bindedTable) {
			final CellSelection<Row> currentRowCell = cellSelectionModel.getSelectCell();
			int index;
			if (beforeCurrentCell) {
				index = currentRowCell == null ? 0 : currentRowCell.getRow();
			} else {
				index = currentRowCell == null ? 0 : currentRowCell.getRow() + 1;
			}
			final int totalRows = store.size();
			if (index > totalRows) {
				index = totalRows;
			}
			if (addToModel) {
				getRowList().add(index, rowToAdd);
			} else {
				manualExtractionSampleRow = rowToAdd;
			}
			store.add(index, rowToAdd);
		}
	}

	private boolean isApplyValidationPatternOnColumn(final Column column) {
		Boolean applyValidation = Boolean.TRUE;
		if (null != column) {
			String columnName = column.getName();
			applyValidation = manualExtractionValidationPattern.get(columnName);
		}
		return applyValidation == null ? true : applyValidation;
	}

	private void toggleManualColumnPattern(Column column) {
		if (null != column) {
			Boolean validationApplied;
			String columnName = column.getName();
			validationApplied = manualExtractionValidationPattern.get(columnName);
			validationApplied = validationApplied == null ? true : validationApplied;
			manualExtractionValidationPattern.put(columnName, !validationApplied);
		}
	}

	private List<Row> getRowList() {
		Rows rows = bindedTable.getRows();
		if (rows == null) {
			rows = new Rows();
			bindedTable.setRows(rows);
		}
		return rows.getRow();
	}

	public DataTable getBindedTable() {
		return bindedTable;
	}

	public boolean isSelected(final Row row) {
		return selectedRowValueProvider.getValue(row);
	}

	public static class DataTableKeyProvider implements ModelKeyProvider<Row> {

		private static int id;

		@Override
		public String getKey(final Row item) {
			return Integer.toString(id++);
		}
	}

	private static class SelectedRowValueProvider implements ValueProvider<Row, Boolean> {

		private final Map<Row, Boolean> selectionMap;

		private SelectedRowValueProvider() {
			this.selectionMap = new HashMap<Row, Boolean>();
		}

		@Override
		public Boolean getValue(final Row row) {
			Boolean isSelected = null;
			if (null != row) {
				isSelected = selectionMap.get(row);
			}
			return isSelected == null ? false : isSelected;
		}

		@Override
		public void setValue(final Row object, final Boolean value) {
			if (null != object && null != value) {
				selectionMap.put(object, value);
			}
		}

		@Override
		public String getPath() {
			return null;
		}
	}

	private static class ColumnSuggestionBox extends StringComboBox {

		private Column column;

		private String originalValue;

		private CoordinatesList originalCoordinatesList;

		private String pageIdentifier;

		public ColumnSuggestionBox() {
			super();
			this.setStyleName("dlfSuggestionBox");
			this.setTriggerAction(TriggerAction.ALL);
			this.addSelectionHandler();
			this.setHideTrigger(true);
			this.addBeforeQueryHandler(new BeforeQueryHandler<String>() {

				@Override
				public void onBeforeQuery(BeforeQueryEvent<String> event) {
					if(!ReviewValidateNavigator.isShowTablesSuggestions()) {
						event.setCancelled(true);
						collapse();
					}
				}
			});
		}

		public void setColumn(final Column column) {
			this.column = column;
			this.getStore().clear();
			if (null != column) {
				final AlternateValues columnAlternateValues = column.getAlternateValues();
				if (null != columnAlternateValues) {
					this.originalValue = column.getValue();
					this.originalCoordinatesList = column.getCoordinatesList();
					this.pageIdentifier = column.getPage();
					this.add(originalValue);
					final List<com.ephesoft.dcma.batch.schema.Field> alternateValuesList = columnAlternateValues.getAlternateValue();
					if (!CollectionUtil.isEmpty(alternateValuesList)) {
						for (final com.ephesoft.dcma.batch.schema.Field field : alternateValuesList) {
							if (null != field) {
								this.add(field.getValue());
							}
						}
					}
				}
				this.addUserInputAlternateValues();
			}
		}

		private void addUserInputAlternateValues() {
			if (null != column) {
				final TableColumnInfoDTO tableColumnInfo = ReviewValidateNavigator.getCurrentTableColumn(column);
				if (null != tableColumnInfo) {
					final String alternateValues = tableColumnInfo.getAlternateValues();
					if (!StringUtil.isNullOrEmpty(alternateValues)) {
						final String[] alternateValuesListing = alternateValues.split(CoreCommonConstant.SEMI_COLON);
						for (final String value : alternateValuesListing) {
							this.add(value);
						}
					}
				}
			}
		}

		private void addSelectionHandler() {
			this.addSelectionHandler(new SelectionHandler<String>() {

				@Override
				public void onSelection(final SelectionEvent<String> event) {
					final String selectedItem = event.getSelectedItem();
					if (!StringUtil.isNullOrEmpty(selectedItem) && !selectedItem.equals(originalValue)) {
						final com.ephesoft.dcma.batch.schema.Field alternateField = BatchSchemaUtil.getAlternateValue(column,
								selectedItem);
						if (null != alternateField) {
							column.setValue(alternateField.getValue());
							column.setCoordinatesList(alternateField.getCoordinatesList());
							column.setPage(alternateField.getPage());
							alternateField.setValue(originalValue);
							alternateField.setCoordinatesList(originalCoordinatesList);
							alternateField.setPage(pageIdentifier);
							final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
							ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
							ReviewValidateEventBus.fireEvent(new FieldSelectionEvent(column));
						}
					}
				}
			});
		}
	}
}
