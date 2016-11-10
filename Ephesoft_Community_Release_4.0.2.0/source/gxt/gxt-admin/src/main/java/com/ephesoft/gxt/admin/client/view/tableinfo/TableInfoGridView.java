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

package com.ephesoft.gxt.admin.client.view.tableinfo;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.NavigationEvent;
import com.ephesoft.gxt.admin.client.presenter.tableinfo.TableInfoGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.widget.BatchClassManagementGrid;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.TableInfoProperties;
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
 * This View deals with Table Info Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.view.tableinfo.TableInfoGridView
 */
public class TableInfoGridView extends BatchClassInlineView<TableInfoGridPresenter> implements HasResizableGrid {

	/**
	 * The Interface Binder.
	 */
	interface Binder extends UiBinder<Widget, TableInfoGridView> {
	}

	/** The Constant binder. */
	private static final Binder binder = GWT.create(Binder.class);

	/** The grid view main panel. */
	@UiField
	protected VerticalPanel gridViewMainPanel;

	/** The table info configuration grid. */
	@UiField(provided = true)
	protected BatchClassManagementGrid<TableInfoDTO> tableInfoGrid;

	/** The paging tool bar. */
	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.view.BatchClassInlineView#initialize()
	 */
	@Override
	public void initialize() {
		tableInfoGrid = new BatchClassManagementGrid<TableInfoDTO>(PropertyAccessModel.TABLE_INFO) {

			@Override
			public void completeEditing(final CompleteEditEvent<TableInfoDTO> completeEditEvent) {
				TableInfoGridView.this.commitChanges();
				presenter.setBatchClassDirtyOnChange();
				refreshBCMGrid(false);
			}
		};
		tableInfoGrid.setIdProvider(TableInfoProperties.properties.name());

		tableInfoGrid.addSelectAllFunctionality(TableInfoProperties.properties.selected());
		tableInfoGrid.addValidators(TableInfoProperties.properties.name(), new EmptyValueValidator<TableInfoDTO>());
		tableInfoGrid.addValidators(TableInfoProperties.properties.name(), new UniqueValueValidator<TableInfoDTO, String>());
		tableInfoGrid.addValidators(TableInfoProperties.properties.ruleOperator(), new EmptyValueValidator<TableInfoDTO>());

		tableInfoGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<TableInfoDTO>() {

			@Override
			public void onRecordChange(final StoreRecordChangeEvent<TableInfoDTO> event) {
				ValueProvider<? super TableInfoDTO, ?> property = event.getProperty();
				if (null != property) {
					if (property != TableInfoProperties.properties.selected()) {
						presenter.setBatchClassDirtyOnChange();
					}
					if (property == TableInfoProperties.properties.name()) {
						Change<TableInfoDTO, String> changedRecord = event.getRecord()
								.getChange(TableInfoProperties.properties.name());
						if (null != changedRecord) {
							String newValue = changedRecord.getValue();
							presenter.setBatchClassDirtyOnChange();
							BatchClassManagementEventBus.fireEvent(new NavigationEvent.NavigationNodeRenameEvent(event.getRecord()
									.getModel(), newValue));
						}
					}
				}
				TableInfoGridView.this.commitChanges();
			}
		});
		pagingToolbar = new PagingToolbar(15, tableInfoGrid);
	}

	/**
	 * Instantiates a new table info grid view.
	 */
	public TableInfoGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		tableInfoGrid.addStyleName("gridView");
		this.addTableInfoSelectionHandler(tableInfoGrid.getSelectionModel());
		final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<TableInfoDTO>> loader = tableInfoGrid.getPaginationLoader();
		pagingToolbar.bind(loader);
	}

	/**
	 * Adds the table info selection handler.
	 * 
	 * @param selectionModel the selection model {@link GridSelectionModel< {@link TableInfoDTO}>}
	 */
	public void addTableInfoSelectionHandler(final GridSelectionModel<TableInfoDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			final CellSelectionModel<TableInfoDTO> cellSelectionModel = (CellSelectionModel<TableInfoDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<TableInfoDTO>() {

				@Override
				public void onCellSelectionChanged(final CellSelectionChangedEvent<TableInfoDTO> cellSelectionEvent) {
					presenter.setSelectTableInfo(cellSelectionEvent);
				}
			});
		}
	}

	/**
	 * Load grid with table info dtos.
	 */
	public void loadGrid() {
		final Collection<TableInfoDTO> tableInfoCollection = presenter.getCurrentTableInfoDTOs();
		tableInfoGrid.setMemoryData(tableInfoCollection);
		this.reLoadGrid();
	}

	/**
	 * Gets the selected table info dtos.
	 * 
	 * @return the selected table info dtos {@link List<{@link TableInfoDTO}>}
	 */
	public List<TableInfoDTO> getSelectedTableInfos() {
		return tableInfoGrid.getSelectedModels();
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
		return tableInfoGrid;
	}

	/**
	 * Gets the table info grid.
	 * 
	 * @return the table info grid
	 */
	public BatchClassManagementGrid<TableInfoDTO> getTableInfoGrid() {
		return tableInfoGrid;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isGridValidated() {
		return tableInfoGrid.isGridValidated();
	}

	/**
	 * Commit changes in grid.
	 */
	public void commitChanges() {
		tableInfoGrid.getStore().commitChanges();
	}

	/**
	 * Re load grid.
	 */
	public void reLoadGrid() {
		WidgetUtil.reLoadGrid(tableInfoGrid);
	}

	/**
	 * Adds the new item in grid.
	 * 
	 * @param tableInfoDTO the table info dto {@link TableInfoDTO}
	 */
	public boolean addNewItemInGrid(final TableInfoDTO tableInfoDTO) {
		return tableInfoGrid.addNewItemInGrid(tableInfoDTO);
	}

	/**
	 * Removes the items from grid.
	 * 
	 * @param tableInfoDTOList the table info dto list {@link List<{@link TableInfoDTO}>}
	 */
	public void removeItemsFromGrid(final List<TableInfoDTO> tableInfoDTOList) {
		tableInfoGrid.removeItemsFromGrid(tableInfoDTOList);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public List<TableInfoDTO> getTableDTOs() {
		List<TableInfoDTO> tableDTOList = null;
		ListStore<TableInfoDTO> listStore = tableInfoGrid.getStore();
		if (null != listStore) {
			tableDTOList = listStore.getAll();
		}
		return tableDTOList;
	}
}
