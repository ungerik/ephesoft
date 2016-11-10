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

package com.ephesoft.gxt.admin.client.view.tablevalidationrule;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.presenter.tablevalidationrule.TableRuleGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.widget.BatchClassManagementGrid;
import com.ephesoft.gxt.admin.client.widget.TableRuleCell;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.TableValidationRuleValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.dto.RuleInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.RuleInfoProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;

/**
 * This View deals with Table Validation Rule Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.view.tablevalidationrule.TableRuleGridView
 */
public class TableRuleGridView extends BatchClassInlineView<TableRuleGridPresenter> implements HasResizableGrid {

	/** The grid view main panel. */
	@UiField
	protected VerticalPanel gridViewMainPanel;

	/** The table rule grid. */
	@UiField(provided = true)
	protected BatchClassManagementGrid<RuleInfoDTO> tableRuleGrid;

	/** The paging tool bar. */
	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	/**
	 * The Interface Binder.
	 */
	interface Binder extends UiBinder<Widget, TableRuleGridView> {
	}

	/** The Constant binder. */
	private static final Binder binder = GWT.create(Binder.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.view.BatchClassInlineView#initialize()
	 */
	@Override
	public void initialize() {
		tableRuleGrid = new BatchClassManagementGrid<RuleInfoDTO>(PropertyAccessModel.TABLE_RULE) {

			@Override
			public void completeEditing(final CompleteEditEvent<RuleInfoDTO> completeEditEvent) {
				TableRuleGridView.this.commitChanges();
				presenter.setBatchClassDirtyOnChange();
				refreshBCMGrid(false);
			}
		};
		tableRuleGrid.setIdProvider(RuleInfoProperties.INSTANCE.rule());

		tableRuleGrid.addSelectAllFunctionality(RuleInfoProperties.INSTANCE.selected());
		tableRuleGrid.addValidators(RuleInfoProperties.INSTANCE.rule(), new EmptyValueValidator<RuleInfoDTO>());
		tableRuleGrid.addValidators(RuleInfoProperties.INSTANCE.rule(), new UniqueValueValidator<RuleInfoDTO, String>());
		tableRuleGrid.addValidators(RuleInfoProperties.INSTANCE.rule(), new TableValidationRuleValidator<RuleInfoDTO>());
		tableRuleGrid.addValidators(RuleInfoProperties.INSTANCE.description(), new EmptyValueValidator<RuleInfoDTO>());

		tableRuleGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<RuleInfoDTO>() {

			@Override
			public void onRecordChange(final StoreRecordChangeEvent<RuleInfoDTO> event) {
				if (event.getProperty() != RuleInfoProperties.INSTANCE.selected()) {
					presenter.setBatchClassDirtyOnChange();
				}
				TableRuleGridView.this.commitChanges();
			}
		});
		pagingToolbar = new PagingToolbar(15, tableRuleGrid);
	}

	/**
	 * Instantiates a new table rule grid view.
	 */
	public TableRuleGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		tableRuleGrid.addStyleName("gridView");
		final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<RuleInfoDTO>> loader = tableRuleGrid.getPaginationLoader();
		pagingToolbar.bind(loader);
	}

	/**
	 * Load grid with rule info dtos.
	 * 
	 * @param ruleInfoDTOList the rule info dto list {@link Collection<{@link RuleInfoDTO} >}
	 */
	public void loadGrid(final Collection<RuleInfoDTO> ruleInfoDTOList) {
		tableRuleGrid.setMemoryData(ruleInfoDTOList);
		WidgetUtil.reLoadGrid(tableRuleGrid);
	}

	/**
	 * Sets the rule view.
	 * 
	 * @param tableInfoDTO the new rule view {@link TableInfoDTO}
	 */
	public void setRuleView(final TableInfoDTO tableInfoDTO) {
		if (null != tableInfoDTO) {
			final TableRuleCell cell = new TableRuleCell(tableInfoDTO, tableRuleGrid);
			tableRuleGrid.setCell(RuleInfoProperties.INSTANCE.identity(), cell, "tableRulesCell");
		}
	}

	/**
	 * Gets the selected table validation rule dtos.
	 * 
	 * @return the selected table validation rule dtos {@link List< {@link RuleInfoDTO}>}
	 */
	public List<RuleInfoDTO> getSelectedTableRules() {
		return tableRuleGrid.getSelectedModels();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid#getGrid()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Grid getGrid() {
		return tableRuleGrid;
	}

	/**
	 * Gets the table validation rule grid.
	 * 
	 * @return the table validation rule grid {@link BatchClassManagementGrid< {@link RuleInfoDTO}>}
	 */
	public BatchClassManagementGrid<RuleInfoDTO> getTableInfoGrid() {
		return tableRuleGrid;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isGridValidated() {
		return tableRuleGrid.isGridValidated();
	}

	/**
	 * Commit changes in grid.
	 */
	public void commitChanges() {
		tableRuleGrid.getStore().commitChanges();
	}

	/**
	 * Re load grid.
	 */
	public void reLoadGrid() {
		WidgetUtil.reLoadGrid(tableRuleGrid);
	}

	/**
	 * Adds the new item in grid.
	 * 
	 * @param ruleInfoDTO the rule info dto {@link RuleInfoDTO}
	 */
	public boolean addNewItemInGrid(final RuleInfoDTO ruleInfoDTO) {
		return tableRuleGrid.addNewItemInGrid(ruleInfoDTO);
	}

	/**
	 * Removes the items from grid.
	 * 
	 * @param ruleInfoDTOList the rule info dto list {@link List<{@link RuleInfoDTO}>}
	 */
	public void removeItemsFromGrid(final List<RuleInfoDTO> ruleInfoDTOList) {
		tableRuleGrid.removeItemsFromGrid(ruleInfoDTOList);
	}

	@Override
	public void refresh() {
	}
}
