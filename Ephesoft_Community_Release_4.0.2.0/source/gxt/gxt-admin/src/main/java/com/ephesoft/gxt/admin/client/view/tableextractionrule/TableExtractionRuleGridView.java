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

package com.ephesoft.gxt.admin.client.view.tableextractionrule;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.NavigationEvent;
import com.ephesoft.gxt.admin.client.presenter.tableextractionrule.TableExtractionRuleGridPresenter;
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
import com.ephesoft.gxt.core.client.ui.widget.TableExtractionAPICompositeCell;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.RegexPatternValidator;
import com.ephesoft.gxt.core.client.validator.TableExtractionAPIValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.dto.TableExtractionAPIModel;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.TableExtractionRuleProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.Store.Change;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

/**
 * This View deals with Table Extraction Rule Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.view.tableextractionrule.TableExtractionRuleGridView
 */
public class TableExtractionRuleGridView extends BatchClassInlineView<TableExtractionRuleGridPresenter> implements HasResizableGrid {

	/**
	 * The Interface Binder.
	 */
	interface Binder extends UiBinder<Widget, TableExtractionRuleGridView> {
	}

	/** The Constant binder. */
	private static final Binder binder = GWT.create(Binder.class);

	/** The grid view main panel. */
	@UiField
	protected VerticalPanel gridViewMainPanel;

	/** The table extraction rule grid. */
	@UiField(provided = true)
	protected BatchClassManagementGrid<TableExtractionRuleDTO> tableExtRuleGrid;

	/** The paging tool bar. */
	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	private RegexComboBox startPatternRegexComboBox;

	private RegexComboBox endPatternRegexComboBox;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.view.BatchClassInlineView#initialize()
	 */
	@Override
	public void initialize() {

		startPatternRegexComboBox = new RegexComboBox();
		endPatternRegexComboBox = new RegexComboBox();

		tableExtRuleGrid = new BatchClassManagementGrid<TableExtractionRuleDTO>(PropertyAccessModel.TABLE_EXTRACTION_RULE) {

			@Override
			public void completeEditing(final CompleteEditEvent<TableExtractionRuleDTO> completeEditEvent) {
				super.completeEditing(completeEditEvent);
				TableExtractionRuleGridView.this.commitChanges();
				presenter.setBatchClassDirtyOnChange();
				refreshBCMGrid(false);
			}
		};

		tableExtRuleGrid.addEditor(TableExtractionRuleProperties.properties.startPattern(), startPatternRegexComboBox);
		tableExtRuleGrid.addEditor(TableExtractionRuleProperties.properties.endPattern(), endPatternRegexComboBox);

		tableExtRuleGrid.setIdProvider(TableExtractionRuleProperties.properties.ruleName());

		tableExtRuleGrid.addSelectAllFunctionality(TableExtractionRuleProperties.properties.selected());
		tableExtRuleGrid.addValidators(TableExtractionRuleProperties.properties.ruleName(),
				new EmptyValueValidator<TableExtractionRuleDTO>());
		tableExtRuleGrid.addValidators(TableExtractionRuleProperties.properties.ruleName(),
				new UniqueValueValidator<TableExtractionRuleDTO, String>());
		tableExtRuleGrid.addValidators(TableExtractionRuleProperties.properties.startPattern(),
				new EmptyValueValidator<TableExtractionRuleDTO>());
		tableExtRuleGrid.addValidators(TableExtractionRuleProperties.properties.endPattern(),
				new EmptyValueValidator<TableExtractionRuleDTO>());
		tableExtRuleGrid.addValidators(TableExtractionRuleProperties.properties.startPattern(),
				new RegexPatternValidator<TableExtractionRuleDTO>(false));
		tableExtRuleGrid.addValidators(TableExtractionRuleProperties.properties.endPattern(),
				new RegexPatternValidator<TableExtractionRuleDTO>(false));
		tableExtRuleGrid.addValidators(TableExtractionRuleProperties.properties.extAPIModel(),
				new TableExtractionAPIValidator<TableExtractionRuleDTO, TableExtractionAPIModel>());
		tableExtRuleGrid.addDirtyCellValueProvider(TableExtractionRuleProperties.properties.startPattern());
		tableExtRuleGrid.addDirtyCellValueProvider(TableExtractionRuleProperties.properties.endPattern());
		tableExtRuleGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<TableExtractionRuleDTO>() {

			@Override
			public void onRecordChange(final StoreRecordChangeEvent<TableExtractionRuleDTO> event) {

				// TODO Auto-generated method stub
				ValueProvider<? super TableExtractionRuleDTO, ?> property = event.getProperty();
				if (null != property) {
					if (property != TableExtractionRuleProperties.properties.selected()) {
						presenter.setBatchClassDirtyOnChange();
					}
					if (property == TableExtractionRuleProperties.properties.ruleName()) {
						Change<TableExtractionRuleDTO, String> changedValue = event.getRecord().getChange(
								TableExtractionRuleProperties.properties.ruleName());
						if (null != changedValue) {
							String newValue = changedValue.getValue();
							presenter.setBatchClassDirtyOnChange();
							BatchClassManagementEventBus.fireEvent(new NavigationEvent.NavigationNodeRenameEvent(event.getRecord()
									.getModel(), newValue));
						}
					}
				}
				TableExtractionRuleGridView.this.commitChanges();
				tableExtRuleGrid.refreshGrid(true);
			}
		});
		pagingToolbar = new PagingToolbar(15, tableExtRuleGrid);
	}

	/**
	 * Instantiates a new table extraction rule grid view.
	 */
	public TableExtractionRuleGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		tableExtRuleGrid.addStyleName("gridView");
		this.addTableExtractionRuleSelectionHandler(tableExtRuleGrid.getSelectionModel());
		final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<TableExtractionRuleDTO>> loader = tableExtRuleGrid
				.getPaginationLoader();
		pagingToolbar.bind(loader);
	}

	/**
	 * Sets the table extraction rule cell.
	 * 
	 */
	public void setExtractionRuleCell() {
		final TableExtractionAPICompositeCell<TableExtractionAPIModel> extAPICompositeCell = new TableExtractionAPICompositeCell<TableExtractionAPIModel>();

		tableExtRuleGrid.setCell(TableExtractionRuleProperties.properties.extAPIModel(), extAPICompositeCell.createCompositeCell(),
				"compositeCellStyle");
	}

	/**
	 * Adds the table extraction rule selection handler.
	 * 
	 * @param selectionModel the selection model {@link GridSelectionModel< {@link TableExtractionRuleDTO}>}
	 */
	public void addTableExtractionRuleSelectionHandler(final GridSelectionModel<TableExtractionRuleDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			final CellSelectionModel<TableExtractionRuleDTO> cellSelectionModel = (CellSelectionModel<TableExtractionRuleDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<TableExtractionRuleDTO>() {

				@Override
				public void onCellSelectionChanged(final CellSelectionChangedEvent<TableExtractionRuleDTO> cellSelectionEvent) {
					presenter.setSelectTableExtractionRule(cellSelectionEvent);

				}
			});
		}
	}

	/**
	 * load grid with table extraction rule dtos.
	 */
	public void loadGrid() {
		final Collection<TableExtractionRuleDTO> tableExtRuleDTOs = presenter.getTableExtractionRules();
		tableExtRuleGrid.setMemoryData(tableExtRuleDTOs);
		this.reLoadGrid();
	}

	/**
	 * Gets the selected table extraction rule dtos.
	 * 
	 * @return the selected table extraction rule dtos {@link List< {@link TableExtractionRuleDTO}>}
	 */
	public List<TableExtractionRuleDTO> getSelectedTableExtractionRules() {
		return tableExtRuleGrid.getSelectedModels();
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
		// TODO Auto-generated method stub
		return tableExtRuleGrid;
	}

	/**
	 * Gets the table extraction rule grid.
	 * 
	 * @return the table extraction rule grid {@link BatchClassManagementGrid< {@link TableExtractionRuleDTO}>}
	 */
	public BatchClassManagementGrid<TableExtractionRuleDTO> getTableExtRuleGrid() {
		return tableExtRuleGrid;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isGridValidated() {
		return tableExtRuleGrid.isGridValidated();
	}

	/**
	 * Commit changes in grid.
	 */
	public void commitChanges() {
		tableExtRuleGrid.getStore().commitChanges();
	}

	/**
	 * Re load grid.
	 */
	public void reLoadGrid() {
		WidgetUtil.reLoadGrid(tableExtRuleGrid);
	}

	/**
	 * Adds the new item in grid.
	 * 
	 * @param tabExtRuleDTO the table extraction rule dto {@link TableExtractionRuleDTO}
	 */
	public boolean addNewItemInGrid(final TableExtractionRuleDTO tabExtRuleDTO) {
		return tableExtRuleGrid.addNewItemInGrid(tabExtRuleDTO);
	}

	/**
	 * Removes the items from grid.
	 * 
	 * @param tabExtRuleDTOs the table extraction rule dto list {@link List< {@link TableExtractionRuleDTO}>}
	 */
	public void removeItemsFromGrid(final List<TableExtractionRuleDTO> tabExtRuleDTOs) {
		tableExtRuleGrid.removeItemsFromGrid(tabExtRuleDTOs);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public List<TableExtractionRuleDTO> getTableExtrRuleDTOs() {
		List<TableExtractionRuleDTO> tableExtrRuleDTOList = null;
		ListStore<TableExtractionRuleDTO> listStore = tableExtRuleGrid.getStore();
		if (null != listStore) {
			tableExtrRuleDTOList = listStore.getAll();
		}
		return tableExtrRuleDTOList;
	}

	public void setComponents(final RegexBuilderView regexBuilderView, final RegexGroupSelectionGridView regexGroupSelectionGridView) {
		startPatternRegexComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		startPatternRegexComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				startPatternRegexComboBox.setText(newPattern);
				tableExtRuleGrid.reLoad();

			}
		});
		endPatternRegexComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		endPatternRegexComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				startPatternRegexComboBox.setText(newPattern);
				tableExtRuleGrid.reLoad();

			}
		});

	}
}
