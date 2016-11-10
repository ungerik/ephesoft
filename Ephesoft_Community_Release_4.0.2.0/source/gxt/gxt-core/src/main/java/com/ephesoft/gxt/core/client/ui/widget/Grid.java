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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.factory.GridAttributeFactory;
import com.ephesoft.gxt.core.client.ui.widget.constant.WidgetPropertiesConstant;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.validator.Validator;
import com.ephesoft.gxt.core.client.validator.Validator.Severity;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.DirtyCell;
import com.ephesoft.gxt.core.shared.dto.Selectable;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfigBean;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.data.shared.loader.MemoryProxy;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent.BeforeStartEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.Field;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.selection.CellSelection;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

@SuppressWarnings({"rawtypes", "unchecked"})
public class Grid<T extends Selectable> extends com.sencha.gxt.widget.core.client.grid.Grid<T> {

	private boolean editable = true;

	private boolean isGridValidated = false;

	protected GridInlineEditing<T> editingGrid;

	private GridView<T> gridView;

	private Map<String, ValueProvider<? super T, ?>> valueProviderMap;

	private List<ValueProvider<T, ?>> nonEditableVPList;

	private List<Filter<T, ?>> filtersList;

	private Map<ValueProvider, Set<T>> forcefullyInValidModels;

	private DataProxy<FilterPagingLoadConfig, PagingLoadResult<T>> proxy;

	private PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader;

	private boolean firstRowSelectedOnLoad = false;

	private boolean hasPagination = true;

	private GridFilter gridFilter;

	private ContentPanel resizablePanel;

	private CheckBox selectAllCheckBox;

	private Map<ValueProvider<? super T, ?>, List<Validator<T>>> validatorsMap;

	private FilterPagingLoadConfigBean configBean;

	private ValueProvider<T, String> idProvider;

	private ColumnConfig<T, ?> currentColumnConfig;

	private GridCell currentSelectedCell;

	private T currentSelectedModel;

	private static int EDITING_DELAY = 100;

	private Element lastSelectedRow;

	private Element lastSelectedColumn;

	private PropertyAccessModel propertyAccessModel;

	private static final String COLUMN_HEADER = "filterEnableColumnHeader";

	public Grid(final PropertyAccessModel propertyAccessModel) {
		this(null, propertyAccessModel);
	}

	/**
	 * Scrolls the selected grid cell into view.
	 * 
	 * @param selectionModel
	 */
	private void addGridSelectionHandler(GridSelectionModel<T> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<T> cellSelectionModel = (CellSelectionModel<T>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<T>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<T> cellSelectionEvent) {
					List<CellSelection<T>> list = cellSelectionEvent.getSelection();
					if (!CollectionUtil.isEmpty(list)) {
						int colId = list.get(0).getCell();
						int rowId = list.get(0).getRow();
						Grid.this.getView().getCell(rowId, colId).scrollIntoView();
					}
				}
			});
		}
	}

	public Grid(final List<T> dataToAdd, final PropertyAccessModel propertyAccessModel) {
		this(dataToAdd, propertyAccessModel, true, null);
	}

	public Grid(final RpcProxy<FilterPagingLoadConfig, PagingLoadResult<T>> proxy, final PropertyAccessModel propertyAccessModel,
			final boolean isEditable) {
		this(null, propertyAccessModel, isEditable, proxy);
	}

	public Grid(final List<T> dataToAdd, final PropertyAccessModel propertyAccessModel, final boolean isEditable,
			final RpcProxy<FilterPagingLoadConfig, PagingLoadResult<T>> proxy) {
		super(Grid.<T> createListStore(propertyAccessModel), Grid.<T> createColumnModel(propertyAccessModel), new GridView<T>());
		this.editable = isEditable;
		this.proxy = proxy;
		this.propertyAccessModel = propertyAccessModel;
		if (isEditable) {
			this.setEditable(propertyAccessModel);
		}
		gridView = (GridView<T>) this.getView();
		this.addStyleName("gridStyle");
		configBean = new FilterPagingLoadConfigBean();
		valueProviderMap = new HashMap<String, ValueProvider<? super T, ?>>();
		nonEditableVPList = new ArrayList<ValueProvider<T, ?>>();
		validatorsMap = new HashMap<ValueProvider<? super T, ?>, List<Validator<T>>>();
		gridView.setShowDirtyCells(false);
		gridView.setTrackMouseOver(false);
		this.mapValueProviders(propertyAccessModel);
		gridView.setStripeRows(true);
		gridView.setColumnLines(true);
		gridView.setAutoExpandMin(10);
		this.setPagination(dataToAdd, propertyAccessModel);
		this.applyFilter(propertyAccessModel);
		this.setLazyRowRender(WidgetPropertiesConstant.DEFAULT_GRID_LAZY_ROW_RENDER);
		this.getView().setEmptyText(LocaleDictionary.getConstantValue(CoreCommonConstants.NO_RECORDS_FOUND));
		this.setAutoFit(true);
		Grid.this.setToolTipConfig(new ToolTipConfig());
		this.validateValueProviders();
		this.addDefaultToolTip();
		setHeaderTooltip();
		forcefullyInValidModels = new HashMap<ValueProvider, Set<T>>();
		this.addGridSelectionHandler(this.getSelectionModel());
		Grid.this.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				NativeEvent natev = event.getNativeEvent();
				if (Element.is(natev.getEventTarget())) {
					int hoverCellIdx = Grid.this.getView().findCellIndex(Element.as(natev.getEventTarget()), null);
					int hoverRowIdx = Grid.this.getView().findRowIndex(Element.as(natev.getEventTarget()));
					ValueProvider<? super T, Object> valueProvider = cm.getValueProvider(hoverCellIdx);
					T model = getStore().get(hoverRowIdx);
					ListWidget errorList = getErrors(valueProvider, model);
					Element cellElement = getView().getCell(hoverRowIdx, hoverCellIdx);
					setToolTip(cellElement, errorList);
				}
			}
		}, MouseMoveEvent.getType());
		this.addKeyDownHandler();
	}

	public void setIdProvider(ValueProvider<T, String> idProvider) {
		this.idProvider = idProvider;
	}

	private void setHeaderTooltip() {
		ColumnModel<T> columnModel = getColumnModel();
		List<ColumnConfig<T, ?>> columnConfigList = null;
		if (columnModel != null) {
			columnConfigList = columnModel.getColumns();
			if (!CollectionUtil.isEmpty(columnConfigList)) {
				for (ColumnConfig<T, ?> columnConfig : columnConfigList) {
					columnConfig.setToolTip(columnConfig.getHeader());
				}
			}
		}
	}

	public void addDirtyCellValueProvider(final ValueProvider valueProvder) {
		if (null != valueProvder && !forcefullyInValidModels.containsKey(valueProvder)) {
			forcefullyInValidModels.put(valueProvder, new HashSet<T>());
		}
	}

	private boolean isDirty(T modelToCheck, ValueProvider valueProviderToValidate) {
		boolean isDirty = false;
		if (null != modelToCheck && valueProviderToValidate != null) {
			Set<T> invalidModelsList = forcefullyInValidModels.get(valueProviderToValidate);
			if (!CollectionUtil.isEmpty(invalidModelsList)) {
				for (T model : invalidModelsList) {
					if (model == modelToCheck) {
						isDirty = true;
						break;
					}
				}
			}
		}
		return isDirty;
	}

	public Map<String, List<DirtyCell>> getDityCells() {
		Map<String, List<DirtyCell>> dirtyCellsMap = new HashMap<String, List<DirtyCell>>();
		for (final Entry<ValueProvider, Set<T>> entry : forcefullyInValidModels.entrySet()) {
			Set<T> modelsList = entry.getValue();
			ValueProvider valueProvider = entry.getKey();
			if (!CollectionUtil.isEmpty(modelsList) && null != valueProvider) {
				String path = valueProvider.getPath();
				ModelKeyProvider<? super T> keyProvider = store.getKeyProvider();
				if (!StringUtil.isNullOrEmpty(path)) {
					for (final T model : modelsList) {
						if (null != model) {
							Object cellValue = valueProvider.getValue(model);
							String modelKey = keyProvider.getKey(model);
							addDirtCellEntry(dirtyCellsMap, cellValue, path, modelKey);
						}
					}
				}
			}
		}
		return dirtyCellsMap;
	}

	public void removeDirtyCell(final ValueProvider<T, ?> valueProvider, T model) {
		if (null != model && null != valueProvider) {
			Set<T> invalidModelsSet = forcefullyInValidModels.get(valueProvider);
			if (!CollectionUtil.isEmpty(invalidModelsSet)) {
				invalidModelsSet.remove(model);
				// Don't remove this code. This is done for invalid validation.
				// int rowIndex = store.indexOf(model);
				// int columnCount = cm.getColumnCount();
				// int columnIndex = -1;
				// for (int i = 0; i < columnCount; i++) {
				// if (cm.getValueProvider(i) == valueProvider) {
				// columnIndex = i;
				// break;
				// }
				// }
				// if (rowIndex > -1 && columnIndex > -1) {
				// Element cell = view.getCell(rowIndex, columnIndex);
				// }
			}
		}
	}

	public void setDirtyCells(Map<String, List<DirtyCell>> dirtyCellMap) {
		for (Set<T> entry : forcefullyInValidModels.values()) {
			if (null != entry) {
				entry.clear();
			}
		}
		if (!CollectionUtil.isEmpty(dirtyCellMap)) {
			for (Entry<String, List<DirtyCell>> invalidColumnEntry : dirtyCellMap.entrySet()) {
				List<DirtyCell> invalidCells = invalidColumnEntry.getValue();
				if (!CollectionUtil.isEmpty(invalidCells)) {
					ValueProvider modelValueProvider = getValueProvider(invalidColumnEntry.getKey());
					if (null != modelValueProvider) {
						addDirtyCellEntry(modelValueProvider, invalidCells);
					}
				}
			}
		}
	}

	private void addDirtyCellEntry(ValueProvider modelValueProvider, List<DirtyCell> invalidCells) {
		Set<T> modelsSet = forcefullyInValidModels.get(modelValueProvider);
		if (null != modelsSet) {
			for (DirtyCell cell : invalidCells) {
				String key = cell.getKey();
				if (!StringUtil.isNullOrEmpty(key)) {
					T model = store.findModelWithKey(key);

					if (null != model) {
						modelsSet.add(model);
					}
				}
			}
		}
	}

	public ValueProvider getValueProvider(final String path) {
		ValueProvider valueProviderToFind = null;
		if (!StringUtil.isNullOrEmpty(path)) {
			int totalColumns = cm.getColumnCount();
			for (int i = 0; i < totalColumns; i++) {
				ValueProvider currentValueProvider = cm.getValueProvider(i);
				if (path.equals(currentValueProvider.getPath())) {
					valueProviderToFind = currentValueProvider;
					break;
				}
			}
		}
		return valueProviderToFind;
	}

	private void addDirtCellEntry(Map<String, List<DirtyCell>> dirtyCellsMap, Object cellValue, String path, String modelKey) {
		String value = cellValue == null ? CoreCommonConstant.EMPTY_STRING : cellValue.toString();
		List<DirtyCell> dirtyCellList = dirtyCellsMap.get(path);
		if (null == dirtyCellList) {
			dirtyCellList = new ArrayList<DirtyCell>();
			dirtyCellsMap.put(path, dirtyCellList);
		}
		dirtyCellList.add(new DirtyCell(modelKey, value));
	}

	private int getDirtyCellsCount() {
		int count = 0;
		for (Entry<ValueProvider, Set<T>> entry : forcefullyInValidModels.entrySet()) {
			Set<T> modelsList = entry.getValue();
			if (!CollectionUtil.isEmpty(modelsList)) {
				count += modelsList.size();
			}
		}
		return count;
	}

	private void addKeyDownHandler() {
		this.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				switch (event.getNativeKeyCode()) {
					case KeyCodes.KEY_SPACE:
						if (currentColumnConfig != null && null != currentSelectedModel && null != currentSelectedCell) {
							Cell<?> cell = currentColumnConfig.getCell();
							if (cell instanceof CheckBoxCell) {
								store.commitChanges();
								ValueProvider valueProvider = currentColumnConfig.getValueProvider();
								Object value = valueProvider.getValue(currentSelectedModel);
								if (value instanceof Boolean) {
									event.preventDefault();
									valueProvider.setValue(currentSelectedModel, !((Boolean) value));
									refreshRow(currentSelectedCell.getRow());
								}
							}
						}
						break;

				}
			}
		}, KeyDownEvent.getType());
	}

	/**
	 * 
	 */
	private void validateValueProviders() {
		this.getView().setViewConfig(new GridViewConfig<T>() {

			@Override
			public String getColStyle(T model, ValueProvider<? super T, ?> valueProvider, int rowIndex, int colIndex) {
				String cssStyle = StringUtil.concatenate(cm.getColumnHeader(colIndex).asString(), CoreCommonConstant.SPACE);
				if (getErrors(valueProvider, model) != null) {
					cssStyle = "styleName";
				}
				return cssStyle;
			}

			@Override
			public String getRowStyle(T model, int rowIndex) {
				String style = rowIndex % 2 == 1 ? "evenRow" : CoreCommonConstant.EMPTY_STRING;
				if (null != idProvider) {
					style = StringUtil.concatenate(style, CoreCommonConstant.SPACE, idProvider.getValue(model));
				}
				return style;
			}
		});
	}

	private void addDefaultToolTip() {
		GridSelectionModel<T> selectionModel = this.getSelectionModel();
		if (selectionModel instanceof CellSelectionModel<?>) {
			CellSelectionModel<T> cellSelectionModel = (CellSelectionModel<T>) selectionModel;
			addBeforeCellSelectionChangeEvent(cellSelectionModel);
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<T>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<T> event) {
					List<CellSelection<T>> gridSelection = event.getSelection();
					if (!CollectionUtil.isEmpty(gridSelection)) {
						CellSelection<T> currentSelection = gridSelection.get(0);
						if (null != currentSelection) {
							int column = currentSelection.getCell();
							int row = currentSelection.getRow();
							T model = currentSelection.getModel();
							ValueProvider<? super T, Object> valueProvider = cm.getValueProvider(column);
							currentColumnConfig = getColumn(valueProvider);
							currentSelectedModel = model;
							onCellSelectionChange(model);
							currentSelectedCell = new GridCell(row, column);
							Element rowElement = gridView.getRow(row);
							Element columnElement = gridView.getCell(row, column);
							if (null != rowElement) {
								if (null != lastSelectedRow && null != lastSelectedColumn) {
									lastSelectedRow.removeClassName("selectedRow");
									lastSelectedColumn.removeClassName("selectedColumn");
								}
								lastSelectedRow = rowElement;
								lastSelectedColumn = columnElement;
								lastSelectedRow.addClassName("selectedRow");
								lastSelectedColumn.addClassName("selectedColumn");
							}
							ListWidget errorsList = getErrors(valueProvider, model);
							Element cellElement = getView().getCell(row, column);
							setToolTip(cellElement, errorsList);

						}
					}
				}
			});
		}
	}

	private void addBeforeCellSelectionChangeEvent(final CellSelectionModel<T> cellSelectionModel) {
		cellSelectionModel.addBeforeSelectionHandler(new BeforeSelectionHandler<T>() {

			@Override
			public void onBeforeSelection(BeforeSelectionEvent<T> event) {
				onBeforeCellSelectionChange(event, currentSelectedModel);
			}
		});
	}

	private void setToolTip(final Element cellElement, final ListWidget errorList) {
		ToolTip toolTip = this.getToolTip();
		if (null != cellElement && null != errorList) {
			if (null == toolTip) {
				ToolTipConfig config = new ToolTipConfig();
				toolTip = new ToolTip(config);
			}
			if (null != errorList) {
				String htmlText = errorList.getElement().getInnerHTML();
				ToolTipConfig config = new ToolTipConfig();
				config.setBodyHtml(htmlText);
				toolTip.update(config);
				toolTip.showAt(cellElement.getAbsoluteLeft() + 5, cellElement.getAbsoluteTop() + 40);
			}
		} else {
			if (null != toolTip) {
				toolTip.hide();
			}
		}
	}

	private ListWidget getErrors(ValueProvider<? super T, ?> valueProvider, T model) {
		ListWidget htmlErrorList = null;
		if (null != validatorsMap && !validatorsMap.isEmpty()) {
			if (validatorsMap.containsKey(valueProvider)) {
				List<Validator<T>> listOfValidators = validatorsMap.get(valueProvider);
				ListStore<T> memoryData = getMemoryData();
				memoryData = memoryData == null ? getStore() : memoryData;
				if (null != listOfValidators && !listOfValidators.isEmpty() && null != memoryData) {
					for (Validator<T> validator : listOfValidators) {
						if (!validator.validate(memoryData.getAll(), model, (ValueProvider<T, ?>) valueProvider)) {
							if (htmlErrorList == null) {
								htmlErrorList = new ListWidget();
							}
							htmlErrorList.addItem(validator.getErrorMessage(), validator.getSeverity().name());
						}
					}
				}
			}
			if (isDirty(model, valueProvider)) {
				if (htmlErrorList == null) {
					htmlErrorList = new ListWidget();
				}
				htmlErrorList.addItem("Either the value is invalid or it requires explicit validation", Severity.HIGH.name());
			}
		}
		return htmlErrorList;
	}

	public void addNonEditableValueProviders(List<ValueProvider<T, ?>> valueProviders) {
		nonEditableVPList.addAll(valueProviders);
	}

	public void addNonEditableValueProviders(ValueProvider<T, ?> valueProvider) {
		if (null != valueProvider) {
			nonEditableVPList.add(valueProvider);
		}
	}

	public void beforeStartEditing(BeforeStartEditEvent<T> beforeEditEvent) {
		final int colIndex = beforeEditEvent.getEditCell().getCol();
		final int rowIndex = beforeEditEvent.getEditCell().getRow();
		if (!CollectionUtil.isEmpty(nonEditableVPList)) {
			for (ValueProvider<T, ?> valueProvider : nonEditableVPList) {
				if (Grid.this.getColumnModel().getColumns().get(colIndex).getValueProvider() == valueProvider) {
					if (!Grid.this.getStore().get(rowIndex).isNew()) {
						beforeEditEvent.setCancelled(true);
						break;
					}
				}
			}
		}
	}

	public void completeEditing(final CompleteEditEvent<T> completeEditEvent) {
		if (null != completeEditEvent) {
			GridCell editCell = completeEditEvent.getEditCell();
			if (null != editCell) {
				int row = editCell.getRow();
				int column = editCell.getCol();
				validateDirtyCell(row, column);
			}
		}
	}

	private void validateDirtyCell(final int row, final int column) {
		// Issue with Sencha's library firing complete edit 2 times on Selection.
		Timer timer = new Timer() {

			@Override
			public void run() {
				com.sencha.gxt.widget.core.client.grid.Grid.GridCell activeCell = editingGrid.getActiveCell();
				if (!editingGrid.isEditing() || !(activeCell != null && activeCell.getRow() == row && activeCell.getCol() == column)) {
					if (row >= 0 && column >= 0) {
						T model = store.get(row);
						ValueProvider valueProvider = cm.getValueProvider(column);
						Set<T> invalidModelsSet = forcefullyInValidModels.get(valueProvider);
						if (null != invalidModelsSet) {
							Element cellElement = gridView.getCell(row, column);
							if (null != cellElement) {
								cellElement.addClassName("styleName");
							}
							invalidModelsSet.add(model);
						}
					}
				}
			}
		};
		timer.schedule(5);
	}

	public PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> getPaginationLoader() {
		return loader;
	}

	public void setResizeHandler(final ContentPanel contentPanel, final float width, final float height) {
		if (null != contentPanel && null == this.resizablePanel && width <= 1.0 && height <= 1.0) {
			this.resizablePanel = contentPanel;
			final int contentPanelWidth = contentPanel.getOffsetWidth();
			final int contentPanelHeight = contentPanel.getOffsetHeight();
			final int gridNewHeight = (int) Math.round((contentPanelHeight * height));
			final int gridNewWidth = (int) Math.round((contentPanelWidth * width));
			setPixelSize(gridNewWidth, gridNewHeight);

			if (hasPagination) {
				int gridHeight = this.getOffsetHeight();
				int gridWidth = this.getOffsetWidth();
				this.setPixelSize(gridWidth - 4, gridHeight - 37);
			}
			contentPanel.addResizeHandler(new ResizeHandler() {

				@Override
				public void onResize(final ResizeEvent event) {
					setGridDimension(contentPanel, width, height);
				}
			});
		}
	}

	private void setGridDimension(final ContentPanel contentPanel, final float width, final float height) {
		final int contentPanelWidth = contentPanel.getOffsetWidth();
		final int contentPanelHeight = contentPanel.getOffsetHeight();
		final int gridNewHeight = (int) Math.round((contentPanelHeight * height));
		final int gridNewWidth = (int) Math.round((contentPanelWidth * width));
		setPixelSize(gridNewWidth, gridNewHeight);
		if (hasPagination) {
			int newHeight = Grid.this.getOffsetHeight();
			int newWidth = Grid.this.getOffsetWidth();
			Grid.this.setPixelSize(newWidth - 4, newHeight - 37);
		}
	}

	public FilterPagingLoadConfigBean getConfigBean() {
		return configBean;
	}

	public void setCell(ValueProvider valueProvider, Cell cell, String className) {
		ColumnConfig columnConfig = getColumn(valueProvider);
		if (null != columnConfig) {
			columnConfig.setCell(cell);
			columnConfig.setCellClassName(className);
		}
	}

	private static <T> ColumnModel<T> createColumnModel(final PropertyAccessModel propertyAccessModel) {
		ColumnModel<T> columnModel = null;
		final List<ColumnConfig<T, ?>> columnConfigList = GridAttributeFactory.<T> getColumnConfig(propertyAccessModel);
		if (!CollectionUtil.isEmpty(columnConfigList)) {
			columnModel = new ColumnModel<T>(columnConfigList);
		}
		return columnModel;
	}

	private void applyFilter(final PropertyAccessModel propertyAccessModel) {
		gridFilter = new GridFilter(loader);
		gridFilter.initPlugin(this);

		// Removing this will improve efficiency of filters
		// gridFilter.setUpdateBuffer(WidgetPropertiesConstant.DEFAULT_UPDATE_BUFFER);
		filtersList = GridAttributeFactory.<T> getFilters(propertyAccessModel);
		gridFilter.addFilters(filtersList);
	}

	private void setPagination(final List<T> dataToAdd, final PropertyAccessModel propertyAccessModel) {
		final ModelKeyProvider<T> keyProvider = GridAttributeFactory.<T> getKeyProvider(propertyAccessModel);
		if (null == proxy) {
			this.proxy = new PagingMemoryProxy(dataToAdd, keyProvider);
		}

		loader = new PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>>(proxy) {

			@Override
			protected FilterPagingLoadConfig newLoadConfig() {
				return configBean;
			}
		};
		loader.setRemoteSort(true);
		loader.addLoadHandler(new LoadResultListStoreBinding<FilterPagingLoadConfig, T, PagingLoadResult<T>>(this.store));
		this.setLoader(loader);
		loader.addLoadHandler(new LoadHandler<FilterPagingLoadConfig, PagingLoadResult<T>>() {

			@Override
			public void onLoad(LoadEvent<FilterPagingLoadConfig, PagingLoadResult<T>> event) {
				if (Grid.this.store != null && Grid.this.store.size() > 0 && firstRowSelectedOnLoad) {
					((CellSelectionModel<T>) Grid.this.getSelectionModel()).selectCell(0, 0);
				}
				if (null != selectAllCheckBox) {
					/*
					 * It is done to make sure whenever a grid is loaded it is loaded with no selection. Explicitly firing value change
					 * event.
					 */
					selectAllCheckBox.setValue(true);
					selectAllCheckBox.setValue(false);
				}
				onGridLoad();
			}
		});
	}

	protected void onGridLoad() {

	}

	private static <T> ListStore<T> createListStore(final PropertyAccessModel propertyAccessModel) {
		ListStore<T> listStore = null;
		final ModelKeyProvider<T> keyProvider = GridAttributeFactory.<T> getKeyProvider(propertyAccessModel);
		if (null != keyProvider) {
			listStore = CollectionUtil.createListStore(keyProvider);
		}
		return listStore;
	}

	public void setSelectedAllModels(final boolean selected) {
		List<T> modelsList = this.store.getAll();
		if (!CollectionUtil.isEmpty(modelsList)) {
			for (T selectableModel : modelsList) {
				if (null != selectableModel) {
					selectableModel.setSelected(selected);
				}
			}
		}
	}

	public void selectGridCell(int rowId, int cellId) {
		((CellSelectionModel<T>) Grid.this.getSelectionModel()).selectCell(rowId, cellId);
	}

	public void startEditing(final int row, final int column) {
		if (editingGrid != null) {
			editingGrid.startEditing(new GridCell(row, column));
		}
	}

	private void mapValueProviders(final PropertyAccessModel propertyAccessModel) {
		final List<ColumnConfig<T, ?>> columnConfigList = GridAttributeFactory.<T> getColumnConfig(propertyAccessModel);
		if (!CollectionUtil.isEmpty(columnConfigList)) {
			ValueProvider<? super T, ?> valueProvider = null;
			String path = null;
			for (ColumnConfig<T, ?> columnConfig : columnConfigList) {
				valueProvider = columnConfig == null ? null : columnConfig.getValueProvider();
				path = valueProvider == null ? null : valueProvider.getPath();
				if (!StringUtil.isNullOrEmpty(path)) {
					valueProviderMap.put(path, valueProvider);
				}
			}
		}
	}

	public ColumnConfig<T, Comparable> getColumn(ValueProvider<?, ?> valueProvider) {
		ColumnConfig<T, Comparable> colConfig = null;
		if (null != valueProvider) {
			int totalColumn = cm.getColumnCount();
			ValueProvider<? super T, Object> currentValueProvider = null;
			for (int columnIndex = 0; columnIndex < totalColumn; columnIndex++) {
				currentValueProvider = cm.getValueProvider(columnIndex);
				if (currentValueProvider == valueProvider) {
					colConfig = cm.getColumn(columnIndex);
					break;
				}
			}
		}
		return colConfig;
	}

	public int getColumnIndex(ValueProvider<?, ?> valueProvider) {
		int index = -1;
		if (null != valueProvider) {
			int totalColumn = cm.getColumnCount();
			ValueProvider<? super T, Object> currentValueProvider = null;
			for (int columnIndex = 0; columnIndex < totalColumn; columnIndex++) {
				currentValueProvider = cm.getValueProvider(columnIndex);
				if (currentValueProvider == valueProvider) {
					index = columnIndex;
					break;
				}
			}
		}
		return index;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		addFilterHeader();

	}

	@Override
	protected void onAttach() {
		super.onAttach();
		Timer timer = new Timer() {

			@Override
			public void run() {
				setGridDimension(resizablePanel, 0.9999f, 0.9999f);
			}
		};
		timer.schedule(80);
	}

	private void addFilterHeader() {
		Timer timer = new Timer() {

			@Override
			public void run() {
				if (!CollectionUtil.isEmpty(filtersList)) {
					for (final Filter<T, ?> gridFilter : filtersList) {
						if (null != gridFilter) {
							ValueProvider<? super T, ?> valueProvider = gridFilter.getValueProvider();
							if (null != valueProvider) {
								int columnIndex = getColumnIndex(valueProvider);
								if (columnIndex != -1) {
									ColumnHeader<T> header = view.getHeader();
									if (null != header) {
										header.getHead(columnIndex).addStyleName(COLUMN_HEADER);
									}
								}
							}
						}
					}
				}
			}
		};
		timer.schedule(100);
	}

	public void addEditor(ValueProvider valueProvider, Field field) {
		ColumnConfig<T, ?> columnConfig = getColumn(valueProvider);
		if (null != columnConfig && editingGrid != null) {
			addEditor(columnConfig, field);
		}
	}

	public List<T> getSelectedModels() {
		List<T> componentsList = this.store.getAll();
		this.store.commitChanges();
		List<T> selectedComponentList = new ArrayList<T>();
		if (!CollectionUtil.isEmpty(componentsList)) {
			for (T component : componentsList) {
				if (component != null && component.isSelected()) {
					selectedComponentList.add(component);
				}
			}
		}
		return selectedComponentList;
	}

	private GridInlineEditing<T> getGridEditing() {
		return new GridInlineEditing<T>(this) {

			@Override
			protected <N, O> void doStartEditing(com.sencha.gxt.widget.core.client.grid.Grid.GridCell cell) {
				super.doStartEditing(cell);
				ColumnModel<T> columnModel = getColumnModel();
				if (null != columnModel) {
					ColumnConfig<T, ?> columnConfig = columnModel.getColumn(cell.getCol());
					if (null != columnConfig) {
						IsField<T> editor = getEditor(columnConfig);
						Element cellElement = Grid.this.getView().getCell(cell.getRow(), cell.getCol());
						if (editor instanceof Field) {
							Field<T> field = (Field<T>) editor;
							Widget editorWidget = editor.asWidget();
							int client_y = cellElement.getOffsetTop();
							int client_x = cellElement.getOffsetLeft();
							int width = cellElement.getAbsoluteRight() - cellElement.getAbsoluteLeft();
							int height = cellElement.getAbsoluteBottom() - cellElement.getAbsoluteTop() + 3;
							field.setPixelSize(width, height);
							editorWidget.getElement().getStyle().setTop(client_y, Unit.PX);
							editorWidget.getElement().getStyle().setLeft(client_x, Unit.PX);
						}
					}
				}
			}
		};
	}

	public void setEditable(final PropertyAccessModel propertyAccessor) {
		if (editingGrid == null) {
			this.editable = true;
			editingGrid = getGridEditing();
			this.setSelectionModel(new CellSelectionModel<T>());
			Map<ColumnConfig<T, Comparable>, IsField<Comparable>> editorMap = GridAttributeFactory.getEditors(propertyAccessor);
			this.addEditor(editorMap);
			editingGrid.addBeforeStartEditHandler(new BeforeStartEditHandler<T>() {

				@Override
				public void onBeforeStartEdit(BeforeStartEditEvent<T> event) {
					Grid.this.beforeStartEditing(event);
				}
			});
			editingGrid.addCompleteEditHandler(new CompleteEditHandler<T>() {

				@Override
				public void onCompleteEdit(CompleteEditEvent<T> event) {
					Grid.this.completeEditing(event);
				}
			});
		}
	}

	public void startEditing(final GridCell cell) {
		if (editingGrid != null && cell != null) {

			Timer timer = new Timer() {

				@Override
				public void run() {
					editingGrid.startEditing(cell);
				}
			};
			timer.schedule(300);
		}
	}

	public void refreshRow(int rowIndex) {
		if (gridView != null && !(rowIndex < 0)) {
			gridView.refreshGridRow(rowIndex);

			// Added missing null check for selected row.
			if (null != currentSelectedCell) {
				int row = currentSelectedCell.getRow();
				int col = currentSelectedCell.getCol();
				selectGridCell(row, col);
				Element rowElement = gridView.getRow(row);
				Element columnElement = gridView.getCell(row, col);
				if (null != rowElement && null != columnElement) {
					if (null != lastSelectedRow && null != lastSelectedColumn) {
						lastSelectedRow.removeClassName("selectedRow");
						lastSelectedColumn.removeClassName("selectedColumn");
					}
					lastSelectedRow = rowElement;
					lastSelectedColumn = columnElement;
					lastSelectedRow.addClassName("selectedRow");
					lastSelectedColumn.addClassName("selectedColumn");
				}
			}
		}
	}

	public void reLoad() {
		this.getView().refresh(false);
	}

	/**
	 * @return the editable
	 */
	public boolean isEditable() {
		return editable;
	}

	public <W> void addEditor(final ColumnConfig<T, W> columnConfig, final IsField<W> editorWidget) {
		if (editingGrid != null && columnConfig != null && editorWidget != null) {
			editingGrid.addEditor(columnConfig, editorWidget);
		}
	}

	public void addEditor(final Map<ColumnConfig<T, Comparable>, IsField<Comparable>> editorsMap) {
		if (!CollectionUtil.isEmpty(editorsMap)) {
			ColumnConfig<T, Comparable> columnConfigEntry;
			IsField<Comparable> fieldWidget;
			for (final Entry<ColumnConfig<T, Comparable>, IsField<Comparable>> editorEntry : editorsMap.entrySet()) {
				columnConfigEntry = editorEntry.getKey();
				fieldWidget = editorEntry.getValue();
				addEditor(columnConfigEntry, fieldWidget);
			}
		}
	}

	public void setAutoFit(boolean isAutoFilt) {
		if (gridView != null) {
			gridView.setForceFit(isAutoFilt);
		}
	}

	public void setMemoryData(Collection<T> dataToSet) {
		if (proxy instanceof Grid.PagingMemoryProxy) {
			if (hasPagination) {
				PagingMemoryProxy memoryProxy = (PagingMemoryProxy) proxy;
				memoryProxy.setData(dataToSet);
			} else {
				store.clear();
				if (!CollectionUtil.isEmpty(dataToSet)) {
					store.addAll(dataToSet);
				}
			}
		}
	}

	public ListStore<T> getMemoryData() {
		ListStore<T> dataList = null;
		if (proxy instanceof Grid.PagingMemoryProxy) {
			if (hasPagination) {
				PagingMemoryProxy memoryProxy = (PagingMemoryProxy) proxy;
				dataList = memoryProxy.getDataItems();
			} else {
				dataList = store;
			}
		}
		return dataList;
	}

	/**
	 * 
	 * @author Ephesoft.
	 * @version 1.0.
	 * 
	 */
	private class PagingMemoryProxy extends MemoryProxy<FilterPagingLoadConfig, PagingLoadResult<T>> {

		private ListStore<T> totalList;

		public PagingMemoryProxy(List<T> totalList, ModelKeyProvider<T> keyProvider) {
			super(null);
			this.totalList = CollectionUtil.createListStore(keyProvider);
			if (!CollectionUtil.isEmpty(totalList)) {
				this.totalList.addAll(totalList);
			}
		}

		public void setData(Collection<T> dataToSet) {
			totalList.clear();
			if (!CollectionUtil.isEmpty(dataToSet)) {
				totalList.addAll(dataToSet);
			}
		}

		@Override
		public void load(FilterPagingLoadConfig loadConfig,
				com.google.gwt.core.client.Callback<PagingLoadResult<T>, Throwable> callback) {
			if (loadConfig != null && callback != null && totalList != null) {
				ListStore<T> proxyStore = null;

				proxyStore = hasPagination ? totalList : store;
				if (gridFilter != null) {
					StoreFilter<T> currentFilterStore = gridFilter.getCurrentFilters();
					proxyStore.addFilter(currentFilterStore);
				}
				int totalRows = proxyStore.size();
				int offset = loadConfig.getOffset();
				int totalDisplayableRows = loadConfig.getLimit();
				int upperRowLimit = totalRows == 0 ? 0 : offset + totalDisplayableRows;
				if (hasPagination) {
					upperRowLimit = upperRowLimit > totalRows ? totalRows : upperRowLimit;
				} else {
					upperRowLimit = store.size();
				}
				List<? extends SortInfo> sortInfoList = loadConfig.getSortInfo();
				this.applySort(proxyStore, loadConfig, sortInfoList);

				List<T> results = proxyStore.subList(offset, upperRowLimit);
				PagingLoadResultBean<T> bean = new PagingLoadResultBean<T>(results, totalRows, offset);
				callback.onSuccess(bean);
			}
		}

		private void applySort(ListStore<T> proxyStore, FilterPagingLoadConfig loadConfig, List<? extends SortInfo> sortInfoList) {
			if (!CollectionUtil.isEmpty(sortInfoList)) {
				proxyStore.clearSortInfo();
				for (SortInfo sortInfo : loadConfig.getSortInfo()) {
					this.applySort(proxyStore, sortInfo);
				}
			}
		}

		private void applySort(ListStore<T> proxyStore, SortInfo sortInfo) {
			if (sortInfo != null) {
				Map<String, ValueProvider<? super T, ?>> valueProviderMap = Grid.this.valueProviderMap;
				String fieldPath = sortInfo.getSortField();
				ValueProvider<? super T, Comparable> fieldValueProvider = (ValueProvider<? super T, Comparable>) valueProviderMap
						.get(fieldPath);
				if (null != fieldValueProvider) {
					StoreSortInfo<T> sortField = new StoreSortInfo<T>(fieldValueProvider, sortInfo.getSortDir());
					proxyStore.addSortInfo(sortField);
				}
			}
		}

		public ListStore<T> getDataItems() {
			return totalList;
		}
	}

	private class GridFilter extends GridFilters<T> {

		public GridFilter(Loader<FilterPagingLoadConfig, ?> loader) {
			super(loader);
		}

		public StoreFilter<T> getCurrentFilters() {
			return this.getModelFilter();
		}

		public void addFilters(List<Filter<T, ?>> filtersList) {
			if (!CollectionUtil.isEmpty(filtersList)) {
				for (Filter<T, ?> gridFilter : filtersList) {
					if (null != gridFilter) {
						this.addFilter(gridFilter);
					}
				}
			}
		}

		@Override
		protected void onStateChange(Filter<T, ?> filter) {
			super.onStateChange(filter);
		}

	}

	public int getGridRowCount() {
		int size = 0;
		if (null != store)
			size = store.size();
		return size;
	}

	public void clearFilter() {
		if (null != gridFilter) {
			gridFilter.clearFilters();
		}
	}

	public void addLoadHandler(LoadHandler<FilterPagingLoadConfig, PagingLoadResult<T>> loadHandler) {
		if (null != loader) {
			loader.addLoadHandler(loadHandler);
		}
	}

	public void removeItemsFromGrid(List<T> itemList) {
		ListStore<T> componentsList = getMemoryData();
		if (!CollectionUtil.isEmpty(itemList)) {
			for (T component : itemList) {
				componentsList.remove(component);
			}
		}
		componentsList.commitChanges();
	}

	public boolean addNewItemInGrid(T item) {
		boolean itemAdded = false;
		if (null != item) {
			if (isGridValidated()) {
				ListStore<T> componentsList = getMemoryData();
				componentsList.clearSortInfo();
				componentsList.commitChanges();
				int activePage = (loader.getOffset() / loader.getLimit()) + 1;
				int insertionIndex = getInsertionIndexForItem(activePage, loader.getLimit());
				componentsList.add(insertionIndex, item);
				itemAdded = true;
				componentsList.commitChanges();
				List<? extends SortInfo> sortList = new ArrayList<SortInfo>(this.loader.getSortInfo());
				this.loader.clearSortInfo();
				this.loader.load(loader.getOffset(), loader.getLimit());
				addSortInfosToLoader(sortList);
				startEditing(this.store.indexOf(item));
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(LocaleCommonConstants.ERROR_TITLE),
						LocaleDictionary.getConstantValue(CoreCommonConstants.VALIDATE_GRID_ERR_MSG), DialogIcon.ERROR);
			}
		}
		return itemAdded;
	}

	private void addSortInfosToLoader(List<? extends SortInfo> sortInfos) {
		if (!CollectionUtil.isEmpty(sortInfos)) {
			for (SortInfo sortInfo : sortInfos) {
				this.loader.addSortInfo(sortInfo);
			}
		}
	}

	public void startEditing(final int rowIndex) {
		Timer timer = new Timer() {

			@Override
			public void run() {
				if (null != editingGrid) {
					int totalColumn = cm.getColumnCount();
					for (int index = 0; index < totalColumn; index++) {
						ColumnConfig<T, ?> columnConfig = cm.getColumn(index);
						if (null != columnConfig && editingGrid.getEditor(columnConfig) != null) {
							GridCell cell = new GridCell(rowIndex, index);
							editingGrid.startEditing(cell);
							break;
						}
					}
				}
			}
		};
		timer.schedule(EDITING_DELAY);
	}

	public void addSelectAllFunctionality(final ValueProvider<T, ?> valueProvider) {
		if (null != valueProvider) {
			ColumnConfig<T, ?> columnConfig = getColumn(valueProvider);
			if (null != columnConfig) {
				columnConfig.setSortable(false);
				columnConfig.setMenuDisabled(true);
				columnConfig.setHideable(false);
				columnConfig.setWidget(getAllModelsSelector(), SafeHtmlUtils.fromSafeConstant("."));
			}
		}
	}

	private Widget getAllModelsSelector() {
		if (null == selectAllCheckBox) {
			selectAllCheckBox = new CheckBox();
			selectAllCheckBox.addStyleName("selectAllCheckBox");
			selectAllCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					setSelectedAllModels(selectAllCheckBox.getValue());
					reLoad();
					store.commitChanges();
				}
			});
		}
		return selectAllCheckBox;
	}

	public int getInsertionIndexForItem(int currentPage, int pageSize) {
		int count = 0;
		boolean isSelected = false;
		int totalItems = this.store.size();
		List<T> componentsList = this.store.getAll();
		this.store.commitChanges();
		if (!CollectionUtil.isEmpty(componentsList)) {
			for (T component : componentsList) {
				count++;
				if (component != null && component.isSelected()) {
					isSelected = true;
					break;
				}
			}
		} else {
			return count;
		}
		return isSelected ? (currentPage - 1) * pageSize + count - 1 : (pageSize > totalItems ? (currentPage - 1) * pageSize
				+ totalItems : (currentPage - 1) * pageSize + totalItems - 1);
	}

	public void deSelectModels() {
		List<T> componentsList = this.store.getAll();
		this.store.commitChanges();
		if (!CollectionUtil.isEmpty(componentsList)) {
			for (T component : componentsList) {
				if (component != null && component.isSelected()) {
					component.setSelected(false);
				}
			}
		}
		this.store.commitChanges();
	}

	public void addValidators(ValueProvider<T, ?> key, Validator<T> value) {
		if (validatorsMap.containsKey(key)) {
			validatorsMap.get(key).add(value);
		} else {
			List<Validator<T>> validators = new ArrayList<Validator<T>>();
			validators.add(value);
			validatorsMap.put(key, validators);
		}
	}

	public void setFirstRowSelectedOnLoad(boolean firstRowSelectedOnLoad) {
		this.firstRowSelectedOnLoad = firstRowSelectedOnLoad;
	}

	public void finishEditing() {
		if (null != editingGrid) {
			editingGrid.completeEditing();
		}
	}

	public boolean isHasPagination() {
		return hasPagination;
	}

	public void setHasPagination(boolean hasPagination) {
		this.hasPagination = hasPagination;
	}

	public boolean isGridValidated() {
		int totalModels = store.size();
		int totalColumn = cm.getColumnCount();
		boolean isGridValidated = true;
		gridValidationLoop: for (int currentModel = 0; currentModel < totalModels; currentModel++) {
			for (int currrentColumn = 0; currrentColumn < totalColumn; currrentColumn++) {
				ValueProvider<? super T, Object> currentValueProvider = cm.getValueProvider(currrentColumn);
				isGridValidated = getErrors(currentValueProvider, store.get(currentModel)) == null;
				if (!isGridValidated) {
					break gridValidationLoop;
				}
			}
		}
		return isGridValidated;
	}

	public void setGridValidated(boolean isGridValidated) {
		// this.isGridValidated = isGridValidated;
	}

	public T getModel(int index) {
		T model = null;
		if (store.size() > index) {
			model = store.get(index);
		}
		return model;
	}

	public void addEditorWidget(ValueProvider<T, ?> valueProvider, IsField<Comparable> fieldWidget) {
		if (null != valueProvider && null != fieldWidget) {
			ColumnConfig<T, Comparable> columnConfig = getColumn(valueProvider);
			if (null != columnConfig) {
				addEditor(columnConfig, fieldWidget);
			}
		}
	}

	public void setCell(ValueProvider<T, ?> valueProvider, Cell cell) {
		if (null != valueProvider && null != cell) {
			ColumnConfig<T, Comparable> columnConfig = getColumn(valueProvider);
			if (null != columnConfig) {
				columnConfig.setCell(cell);
			}
		}
	}

	public <W> IsField<W> getEditorWidget(ValueProvider<T, ?> valueProvider) {
		IsField<W> editorWidget = null;
		if (editingGrid != null && valueProvider != null) {
			ColumnConfig<T, Comparable> columnConfig = getColumn(valueProvider);
			if (null != columnConfig) {
				editorWidget = editingGrid.getEditor(columnConfig);
			}
		}
		return editorWidget;
	}

	public void refreshGrid(T modelToCheck, boolean isCurrentSelectedRefreshed) {
		if (isCurrentSelectedRefreshed) {
			reLoad();
		} else {
			ListStore<T> listStore = getStore();
			if (null != listStore && listStore.size() > 0) {
				List<T> modelList = listStore.getAll();
				if (!CollectionUtil.isEmpty(modelList)) {
					int rowIndex = 0;
					for (T model : modelList) {
						if (null != modelToCheck && modelToCheck != model) {
							refreshRow(rowIndex);
						}
						rowIndex++;
					}
				}
			}

		}
	}

	public void cancelEditing() {
		if (null != editingGrid) {
			editingGrid.cancelEditing();
		}
	}

	/**
	 * Returns the index of the first occurrence of the specified element in this list, or -1 if this list does not contain the
	 * element.
	 * 
	 * @param item the visible item
	 * @return the index of the item
	 */
	public int getRowIndex(T modelToCheck) {
		int rowIndex = -1;
		final ListStore<T> listStore = getStore();
		if (null != listStore && listStore.size() > 0) {
			rowIndex = listStore.indexOf(modelToCheck);
		}
		return rowIndex;
	}

	public boolean isValid(T model) {
		boolean isModelValid = true;
		if (null != model) {
			int totalColumn = cm.getColumnCount();
			for (int currrentColumn = 0; currrentColumn < totalColumn; currrentColumn++) {
				ValueProvider<? super T, Object> currentValueProvider = cm.getValueProvider(currrentColumn);
				isModelValid = getErrors(currentValueProvider, model) == null;
				if (!isModelValid) {
					break;
				}
			}
		}
		return isModelValid;
	}

	/**
	 * @return the currentSelectedModel
	 */
	public T getCurrentSelectedModel() {
		return currentSelectedModel;
	}

	protected void setCurrentModel(T model) {
		this.currentSelectedModel = null;
	}

	protected void onCellSelectionChange(T model) {

	}

	protected void onBeforeCellSelectionChange(BeforeSelectionEvent<T> event, T model) {

	}

	public GridCell getCurrentSelectedCell() {
		return currentSelectedCell;
	}

	public PropertyAccessModel getPropertyAccessModel() {
		return propertyAccessModel;
	}

	public CheckBox getSelectAllCheckBox() {
		return selectAllCheckBox;
	}

}
