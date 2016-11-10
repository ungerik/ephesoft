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

package com.ephesoft.gxt.admin.client.view.tablecolumnextraction;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.i18n.BatchClassManagementConstants;
import com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction.ColumnExtractionRuleGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.view.regexPool.RegexGroupSelectionGridView;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexBuilderView;
import com.ephesoft.gxt.admin.client.widget.BatchClassManagementGrid;
import com.ephesoft.gxt.admin.client.widget.RegexComboBox;
import com.ephesoft.gxt.admin.client.widget.RegexComboBoxHandler;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.NumericValueValidator;
import com.ephesoft.gxt.core.client.validator.RegexPatternValidator;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.ColumnExtractionRuleProperties;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent.TriggerClickHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

/**
 * This View deals with Table Column Extraction Rule Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.view.tablecolumnextraction.ColumnExtractionRuleGridView
 */
public class ColumnExtractionRuleGridView extends BatchClassInlineView<ColumnExtractionRuleGridPresenter> implements HasResizableGrid {

	/**
	 * The Interface Binder.
	 */
	interface Binder extends UiBinder<Widget, ColumnExtractionRuleGridView> {
	}

	/** The Constant binder. */
	private static final Binder binder = GWT.create(Binder.class);

	/** The grid view main panel. */
	@UiField
	protected VerticalPanel gridViewMainPanel;

	/** The table column extraction rule grid. */
	@UiField(provided = true)
	protected BatchClassManagementGrid<TableColumnExtractionRuleDTO> colExtrGrid;

	/** The paging toolbar. */
	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	private RegexComboBox columnPatternComboBox;
	private RegexComboBox columnHeaderPatternComboBox;
	private RegexComboBox betLeftPatternComboBox;
	private RegexComboBox betRightPatternComboBox;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.view.BatchClassInlineView#initialize()
	 */
	@Override
	public void initialize() {
		columnPatternComboBox = new RegexComboBox();
		columnHeaderPatternComboBox = new RegexComboBox();
		betLeftPatternComboBox = new RegexComboBox();
		betRightPatternComboBox = new RegexComboBox();

		colExtrGrid = new BatchClassManagementGrid<TableColumnExtractionRuleDTO>(PropertyAccessModel.COLUMN_EXTRACTION_RULE) {

			@Override
			public void completeEditing(final CompleteEditEvent<TableColumnExtractionRuleDTO> completeEditEvent) {
				super.completeEditing(completeEditEvent);
				ColumnExtractionRuleGridView.this.commitChanges();
				presenter.setBatchClassDirtyOnChange();
				refreshBCMGrid(false);
			}

			@Override
			protected void onCellSelectionChange(TableColumnExtractionRuleDTO model) {
				super.onCellSelectionChange(model);
				presenter.getController().setSelectedTableColumnExtractionRuleDTO(model);
			}
		};

		colExtrGrid.addEditor(ColumnExtractionRuleProperties.getProperties().getColumnPattern(), columnPatternComboBox);
		colExtrGrid.addEditor(ColumnExtractionRuleProperties.getProperties().getColumnHeaderPattern(), columnHeaderPatternComboBox);
		colExtrGrid.addEditor(ColumnExtractionRuleProperties.getProperties().getBetweenLeft(), betLeftPatternComboBox);
		colExtrGrid.addEditor(ColumnExtractionRuleProperties.getProperties().getBetweenRight(), betRightPatternComboBox);
		colExtrGrid.setIdProvider(ColumnExtractionRuleProperties.getProperties().getColumnName());

		colExtrGrid.addSelectAllFunctionality(ColumnExtractionRuleProperties.getProperties().selected());
		colExtrGrid.addValidators(ColumnExtractionRuleProperties.getProperties().getColumnPattern(),
				new RegexPatternValidator<TableColumnExtractionRuleDTO>());
		colExtrGrid.addValidators(ColumnExtractionRuleProperties.getProperties().getBetweenLeft(),
				new RegexPatternValidator<TableColumnExtractionRuleDTO>());
		colExtrGrid.addValidators(ColumnExtractionRuleProperties.getProperties().getBetweenRight(),
				new RegexPatternValidator<TableColumnExtractionRuleDTO>());
		colExtrGrid.addDirtyCellValueProvider(ColumnExtractionRuleProperties.getProperties().getBetweenLeft());
		colExtrGrid.addDirtyCellValueProvider(ColumnExtractionRuleProperties.getProperties().getBetweenRight());
		colExtrGrid.addDirtyCellValueProvider(ColumnExtractionRuleProperties.getProperties().getColumnHeaderPattern());
		colExtrGrid.addDirtyCellValueProvider(ColumnExtractionRuleProperties.getProperties().getColumnPattern());
		colExtrGrid.addValidators(ColumnExtractionRuleProperties.getProperties().getColumnHeaderPattern(),
				new RegexPatternValidator<TableColumnExtractionRuleDTO>());
		colExtrGrid.addValidators(ColumnExtractionRuleProperties.getProperties().getColumnStartCoordinate(),
				new NumericValueValidator<TableColumnExtractionRuleDTO>());
		colExtrGrid.addValidators(ColumnExtractionRuleProperties.getProperties().getColumnEndCoordinate(),
				new NumericValueValidator<TableColumnExtractionRuleDTO>());

		colExtrGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<TableColumnExtractionRuleDTO>() {

			@Override
			public void onRecordChange(final StoreRecordChangeEvent<TableColumnExtractionRuleDTO> event) {
				if (event.getProperty() != ColumnExtractionRuleProperties.getProperties().selected()) {
					presenter.setBatchClassDirtyOnChange();
				}
				ColumnExtractionRuleGridView.this.commitChanges();
			}
		});
		colExtrGrid.setFirstRowSelectedOnLoad(true);
		pagingToolbar = new PagingToolbar(15, colExtrGrid);
	}

	/**
	 * Instantiates a new table column extraction rule grid view.
	 */
	public ColumnExtractionRuleGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		colExtrGrid.addStyleName("gridView");
		this.addColumnExtractionSelectionHandler(colExtrGrid.getSelectionModel());
		final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<TableColumnExtractionRuleDTO>> loader = colExtrGrid
				.getPaginationLoader();
		pagingToolbar.bind(loader);
	}

	/**
	 * Adds the table column extraction rule selection handler.
	 * 
	 * @param selectionModel the selection model {@link GridSelectionModel< {@link TableColumnExtractionRuleDTO}>}
	 */
	public void addColumnExtractionSelectionHandler(final GridSelectionModel<TableColumnExtractionRuleDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			final CellSelectionModel<TableColumnExtractionRuleDTO> cellSelectionModel = (CellSelectionModel<TableColumnExtractionRuleDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<TableColumnExtractionRuleDTO>() {

				@Override
				public void onCellSelectionChanged(final CellSelectionChangedEvent<TableColumnExtractionRuleDTO> cellSelectionEvent) {
					presenter.setSelectColumnExtrRule(cellSelectionEvent);
				}
			});
		}
	}

	/**
	 * Action for column combo.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void actionForColumnCells() {

		LabelProvider<String> comboLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(String item) {
				return item;
			}
		};

		final SimpleComboBox columnCombo = new SimpleComboBox<String>(comboLabelProvider);
		columnCombo.addTriggerClickHandler(new TriggerClickHandler() {

			@Override
			public void onTriggerClick(TriggerClickEvent event) {
				columnCombo.getStore().clear();
				columnCombo.setUseQueryCache(false);
				columnCombo.redraw();
				presenter.setColumnValues(columnCombo);
				columnCombo.setTriggerAction(TriggerAction.ALL);
			}
		});
		columnCombo.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(final SelectionEvent<String> event) {
				if (event != null && !StringUtil.isNullOrEmpty(event.getSelectedItem())) {
					presenter.actionOnColumnSelection();
				}

			}
		});
		colExtrGrid.addEditorWidget(ColumnExtractionRuleProperties.getProperties().getExtractedDataColumnName(), columnCombo);

		CheckBoxCell multilineAnchorCell = new CheckBoxCell() {

			@Override
			public void onBrowserEvent(Context context, Element parent, Boolean value, NativeEvent event,
					ValueUpdater<Boolean> valueUpdater) {
				final InputElement input = getInputElement(parent);
				Boolean checked = input.isChecked();
				if (null != event.getType() && null != checked && BatchClassManagementConstants.LABEL_EVENT_CLICK.equals(event.getType()) && checked) {
					presenter.actionOnMandatorySelection();
				}
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
			}
		};
		colExtrGrid.setCell(ColumnExtractionRuleProperties.getProperties().getMandatory(), multilineAnchorCell);

		CheckBoxCell requiredCell = new CheckBoxCell() {

			@Override
			public void onBrowserEvent(Context context, Element parent, Boolean value, NativeEvent event,
					ValueUpdater<Boolean> valueUpdater) {
				final InputElement input = getInputElement(parent);
				Boolean checked = input.isChecked();
				if (null != event.getType() && null != checked && BatchClassManagementConstants.LABEL_EVENT_CLICK.equals(event.getType()) && checked) {
					presenter.actionOnRequiredSelection();
				}
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
			}
		};
		colExtrGrid.setCell(ColumnExtractionRuleProperties.getProperties().getRequired(), requiredCell);
	}

	/**
	 * load grid with table column extraction rule dtos.
	 */
	public void loadGrid() {
		final Collection<TableColumnExtractionRuleDTO> columnRulesCollection = presenter.getTableColumnRules();
		colExtrGrid.setMemoryData(columnRulesCollection);
		this.reLoadGrid();
	}

	/**
	 * Gets the selected table column extraction rule dtos.
	 * 
	 * @return the selected table column extraction rule dtos {@link List< {@link TableColumnExtractionRuleDTO}>}
	 */
	public List<TableColumnExtractionRuleDTO> getSelectedColExtrRules() {
		return colExtrGrid.getSelectedModels();
	}

	/**
	 * Gets the paging tool bar.
	 * 
	 * @return the paging tool bar {@link PagingToolbar}
	 */
	public PagingToolbar getPagingToolbar() {
		return pagingToolbar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid#getGrid()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Grid getGrid() {
		return colExtrGrid;
	}

	/**
	 * Gets the column extraction grid.
	 * 
	 * @return the column extraction grid
	 */
	public BatchClassManagementGrid<TableColumnExtractionRuleDTO> getColExtrGrid() {
		return colExtrGrid;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isGridValidated() {
		return colExtrGrid.isGridValidated();
	}

	/**
	 * Commit changes in grid.
	 */
	public void commitChanges() {
		colExtrGrid.getStore().commitChanges();
	}

	/**
	 * Re load grid.
	 */
	public void reLoadGrid() {
		WidgetUtil.reLoadGrid(colExtrGrid);
	}

	/**
	 * Adds the new item in grid.
	 * 
	 * @param colRuleDTO the table column extraction rule dto
	 */
	public void addNewItemInGrid(final TableColumnExtractionRuleDTO colRuleDTO) {
		colExtrGrid.addNewItemInGrid(colRuleDTO);
	}

	/**
	 * Removes the items from grid.
	 * 
	 * @param colRulesList the table column extraction rules list
	 */
	public void removeItemsFromGrid(final List<TableColumnExtractionRuleDTO> colRulesList) {
		colExtrGrid.removeItemsFromGrid(colRulesList);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public void setComponents(final RegexBuilderView regexBuilderView, final RegexGroupSelectionGridView regexGroupSelectionGridView) {
		columnPatternComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		columnPatternComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				columnPatternComboBox.setText(newPattern);
				colExtrGrid.reLoad();

			}
		});
		columnHeaderPatternComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		columnHeaderPatternComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				columnHeaderPatternComboBox.setText(newPattern);
				colExtrGrid.reLoad();

			}
		});
		betLeftPatternComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		betLeftPatternComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				betLeftPatternComboBox.setText(newPattern);
				colExtrGrid.reLoad();

			}
		});
		betRightPatternComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		betRightPatternComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				betRightPatternComboBox.setText(newPattern);
				colExtrGrid.reLoad();

			}
		});
	}
}
