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

package com.ephesoft.gxt.admin.client.view.tablecolumninfo;

import java.util.Collection;
import java.util.List;

import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.tablecolumninfo.TableColumnInfoGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.view.regexPool.RegexGroupSelectionGridView;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexBuilderView;
import com.ephesoft.gxt.admin.client.widget.BatchClassManagementGrid;
import com.ephesoft.gxt.admin.client.widget.RegexComboBox;
import com.ephesoft.gxt.admin.client.widget.RegexComboBoxHandler;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.RegexPatternValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.TableColumnInfoProperties;
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

/**
 * This View deals with Table Column Info Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.view.tablecolumninfo.TableColumnInfoGridView
 */
public class TableColumnInfoGridView extends BatchClassInlineView<TableColumnInfoGridPresenter> implements HasResizableGrid {

	/**
	 * The Interface Binder.
	 */
	interface Binder extends UiBinder<Widget, TableColumnInfoGridView> {
	}

	/** The Constant binder. */
	private static final Binder binder = GWT.create(Binder.class);

	/** The grid view main panel. */
	@UiField
	protected VerticalPanel gridViewMainPanel;

	/** The table column grid. */
	@UiField(provided = true)
	protected BatchClassManagementGrid<TableColumnInfoDTO> tableColumnGrid;

	/** The paging tool bar. */
	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	private RegexComboBox validationPatternComboBox;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.view.BatchClassInlineView#initialize()
	 */
	@Override
	public void initialize() {

		validationPatternComboBox = new RegexComboBox();

		tableColumnGrid = new BatchClassManagementGrid<TableColumnInfoDTO>(PropertyAccessModel.TABLE_COLUMN) {

			@Override
			public void completeEditing(final CompleteEditEvent<TableColumnInfoDTO> completeEditEvent) {
				super.completeEditing(completeEditEvent);
				TableColumnInfoGridView.this.commitChanges();
				presenter.setBatchClassDirtyOnChange();
				refreshBCMGrid(false);
			}
		};

		tableColumnGrid.addEditor(TableColumnInfoProperties.properties.validationPattern(), validationPatternComboBox);

		tableColumnGrid.setIdProvider(TableColumnInfoProperties.properties.columnName());
		tableColumnGrid.addDirtyCellValueProvider(TableColumnInfoProperties.properties.validationPattern());
		tableColumnGrid.addSelectAllFunctionality(TableColumnInfoProperties.properties.selected());
		tableColumnGrid
				.addValidators(TableColumnInfoProperties.properties.columnName(), new EmptyValueValidator<TableColumnInfoDTO>());
		tableColumnGrid.addValidators(TableColumnInfoProperties.properties.columnName(),
				new UniqueValueValidator<TableColumnInfoDTO, String>());
		tableColumnGrid.addValidators(TableColumnInfoProperties.properties.validationPattern(),
				new RegexPatternValidator<TableColumnInfoDTO>(false));

		tableColumnGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<TableColumnInfoDTO>() {

			@Override
			public void onRecordChange(final StoreRecordChangeEvent<TableColumnInfoDTO> event) {

				final ValueProvider<? super TableColumnInfoDTO, ?> property = event.getProperty();
				if (null != property) {
					if (property != TableColumnInfoProperties.properties.selected()) {
						presenter.setBatchClassDirtyOnChange();
					}
					if (property == TableColumnInfoProperties.properties.columnName()) {
						final Change<TableColumnInfoDTO, String> change = event.getRecord().getChange(
								TableColumnInfoProperties.properties.columnName());
						if (null != change) {
							final String newValue = change.getValue();
							final TableColumnInfoDTO tableColumnInfoDTO = event.getRecord().getModel();
							presenter.actionOnColumnnValueChange(newValue, tableColumnInfoDTO);
						}
					}
				}
				TableColumnInfoGridView.this.commitChanges();
			}
		});
		pagingToolbar = new PagingToolbar(15, tableColumnGrid);
	}

	/**
	 * Instantiates a new table column info grid view.
	 */
	public TableColumnInfoGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		tableColumnGrid.addStyleName("gridView");
		final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<TableColumnInfoDTO>> loader = tableColumnGrid
				.getPaginationLoader();
		pagingToolbar.bind(loader);
	}

	/**
	 * load grid with table column info dtos.
	 */
	public void loadGrid() {
		final Collection<TableColumnInfoDTO> columnDTOCollection = presenter.getCurrentTableColumnDTOs();
		tableColumnGrid.setMemoryData(columnDTOCollection);
		this.reLoadGrid();
	}

	/**
	 * Gets the selected table column info dtos.
	 * 
	 * @return the selected table column info dtos {@link List< {@link TableColumnInfoDTO}>}
	 */
	public List<TableColumnInfoDTO> getSelectedTableColumns() {
		return tableColumnGrid.getSelectedModels();
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
		return tableColumnGrid;
	}

	/**
	 * Gets the table column grid.
	 * 
	 * @return the table column grid {@link BatchClassManagementGrid< {@link TableColumnInfoDTO}>}
	 */
	public BatchClassManagementGrid<TableColumnInfoDTO> getTableColumnGrid() {
		return tableColumnGrid;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isGridValidated() {
		return tableColumnGrid.isGridValidated();
	}

	/**
	 * Commit changes in grid.
	 */
	public void commitChanges() {
		tableColumnGrid.getStore().commitChanges();
	}

	/**
	 * Re load grid.
	 */
	public void reLoadGrid() {
		WidgetUtil.reLoadGrid(tableColumnGrid);
	}

	/**
	 * Adds the new item in grid.
	 * 
	 * @param columnInfoDTO the table column info dto {@link TableColumnInfoDTO}
	 */
	public boolean addNewItemInGrid(final TableColumnInfoDTO columnInfoDTO) {
		return tableColumnGrid.addNewItemInGrid(columnInfoDTO);
	}

	/**
	 * Removes the items from grid.
	 * 
	 * @param columnInfoDTOList the table column info dto list {@link List< {@link TableColumnInfoDTO}>}
	 */
	public void removeItemsFromGrid(final List<TableColumnInfoDTO> columnInfoDTOList) {
		tableColumnGrid.removeItemsFromGrid(columnInfoDTOList);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public List<TableColumnInfoDTO> getStoreRecords() {
		ListStore<TableColumnInfoDTO> tableColumnInfoList = null;
		List<TableColumnInfoDTO> columnList = null;
		tableColumnInfoList = tableColumnGrid.getStore();
		if (null != tableColumnInfoList && tableColumnInfoList.size() > 0) {
			columnList = tableColumnInfoList.getAll();
		}
		return columnList;
	}

	public void setComponents(final RegexBuilderView regexBuilderView, final RegexGroupSelectionGridView regexGroupSelectionGridView) {
		validationPatternComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		validationPatternComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				validationPatternComboBox.setText(newPattern);
				tableColumnGrid.reLoad();
			}
		});

	}
}
