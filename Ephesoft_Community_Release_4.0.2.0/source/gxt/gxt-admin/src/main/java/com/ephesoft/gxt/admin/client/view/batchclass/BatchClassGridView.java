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

package com.ephesoft.gxt.admin.client.view.batchclass;

import java.util.List;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.property.BatchClassProperty;
import com.ephesoft.gxt.admin.client.BatchClassManagementServiceAsync;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.BatchClassListLoadEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.batchclass.BatchClassGridPresenter;
import com.ephesoft.gxt.admin.client.proxy.BatchClassRpcProxy;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.widget.BatchClassManagementGrid;
import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.MultiSelectComboBox;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.GridRangeValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.BatchClassProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.BatchClassProperties.BatchClassMappingValueProvider.BatchClassRoleValueProvider;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.SortChangeEvent;
import com.sencha.gxt.widget.core.client.event.SortChangeEvent.SortChangeHandler;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class BatchClassGridView extends BatchClassInlineView<BatchClassGridPresenter> implements HasResizableGrid {

	interface Binder extends UiBinder<Widget, BatchClassGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField(provided = true)
	protected BatchClassManagementGrid<BatchClassDTO> batchClassGrid;

	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	private BatchClassRpcProxy proxy;

	@UiField
	protected VerticalPanel gridViewMainPanel;

	private boolean gridLoaded = false;

	MultiSelectComboBox rolesComboBox;

	public BatchClassGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		batchClassGrid.addStyleName("gridView");
		batchClassGrid.getStore().setAutoCommit(true);
		batchClassGrid.setFirstRowSelectedOnLoad(true);
	}

	public void loadGrid() {
		if (!gridLoaded) {
			BatchClassManagementServiceAsync rpcService = presenter.getRpcService();
			proxy.setRpcService(rpcService);
			PagingLoader<FilterPagingLoadConfig, PagingLoadResult<BatchClassDTO>> loader = batchClassGrid.getPaginationLoader();
			pagingToolbar.bind(loader);
			WidgetUtil.loadGrid(batchClassGrid, pagingToolbar);
			gridLoaded = true;
		}
	}

	public void reloadGrid() {
		if (gridLoaded) {
			WidgetUtil.reLoadGrid(batchClassGrid);
		} else {
			this.loadGrid();
		}
	}

	@Override
	public void initialize() {
		proxy = new BatchClassRpcProxy();
		batchClassGrid = new BatchClassManagementGrid<BatchClassDTO>(proxy, PropertyAccessModel.BATCH_CLASS, true) {

			@Override
			public void completeEditing(CompleteEditEvent<BatchClassDTO> completeEditEvent) {
				batchClassGrid.getStore().commitChanges();
				if (null != completeEditEvent) {
					com.sencha.gxt.widget.core.client.grid.Grid.GridCell editCell = completeEditEvent.getEditCell();
					if (null != editCell) {
						int row = editCell.getRow();
						BatchClassDTO model = store.get(row);
						presenter.addBatchClassToUpdated(model);
					}
				}
			}

			@Override
			protected void onGridLoad() {
				super.onLoad();

				List<BatchClassDTO> batchClassList = store.getAll();
				presenter.setBatchClassList(batchClassList);
				BatchClassManagementEventBus.fireEvent(new BatchClassListLoadEvent(batchClassList));
			}
		};
		batchClassGrid.setIdProvider(BatchClassProperties.INSTANCE.name());

		batchClassGrid.addValidators(BatchClassProperties.INSTANCE.description(), new EmptyValueValidator<BatchClassDTO>());
		batchClassGrid.addValidators(
				BatchClassProperties.INSTANCE.priority(),
				new GridRangeValidator<BatchClassDTO>(LocaleDictionary
						.getMessageValue(BatchClassMessages.PRIORITY_SHOULD_BE_BETWEEN_1_AND_100), Integer.class,
						BatchClassConstants.PRIORITY_MIN_VALUE, BatchClassConstants.PRIORITY_MAX_VALUE));
		batchClassGrid.addValidators(BatchClassProperties.INSTANCE.uncFolder(), new EmptyValueValidator<BatchClassDTO>());
		batchClassGrid.addValidators(BatchClassProperties.INSTANCE.identifier(), new EmptyValueValidator<BatchClassDTO>());
		batchClassGrid.addSelectAllFunctionality(BatchClassProperties.INSTANCE.selected());
		this.addBatchClassSelectionHandler(batchClassGrid.getSelectionModel());

		batchClassGrid.addSortChangeHandler(new SortChangeHandler() {

			@Override
			public void onSortChange(SortChangeEvent event) {
				if (event != null) {
					setOrder(event.getSortInfo());
					reloadGrid();
				}

			}
		});

		pagingToolbar = new PagingToolbar(15, batchClassGrid);
	}

	private void setOrder(SortInfo sortInfo) {
		if (proxy != null && sortInfo != null) {
			if (!StringUtil.isNullOrEmpty(sortInfo.getSortField()) && sortInfo.getSortDir() != null) {
				boolean isAscending = false;
				if (sortInfo.getSortDir().name().equalsIgnoreCase(CoreCommonConstant.ASC)) {
					isAscending = true;
				}
				BatchClassProperty batchClassProperty = BatchClassProperty.getPropertyByUiProperty(sortInfo.getSortField());
				if (batchClassProperty != null) {
					proxy.setOrder(new Order(batchClassProperty, isAscending));
				}
			}
		}
	}

	public void addBatchClassSelectionHandler(GridSelectionModel<BatchClassDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<BatchClassDTO> cellSelectionModel = (CellSelectionModel<BatchClassDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<BatchClassDTO>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<BatchClassDTO> cellSelectionEvent) {
					presenter.selectBatchClass(cellSelectionEvent);
				}
			});
		}
	}

	@Override
	public void refresh() {
		WidgetUtil.reLoadGrid(batchClassGrid);
	}

	public void addBatchClass(BatchClassDTO batchClass) {
		batchClassGrid.getStore().add(batchClass);
	}

	public List<BatchClassDTO> getSelectedBatchClasses() {
		return batchClassGrid.getSelectedModels();
	}

	@Override
	public Grid getGrid() {
		return batchClassGrid;
	}

	public boolean isValid() {
		return batchClassGrid.isGridValidated();
	}

	public void setRoles(List<String> allRoles) {
		if (!CollectionUtil.isEmpty(allRoles)) {
			rolesComboBox = new MultiSelectComboBox(allRoles);
			rolesComboBox.setEditable(false);
			if (EphesoftUIContext.isSuperAdmin()) {
				batchClassGrid.addEditor(BatchClassRoleValueProvider.getInstance(), rolesComboBox);
			} else {
				rolesComboBox.setEnabled(false);
			}
		}
	}
}
