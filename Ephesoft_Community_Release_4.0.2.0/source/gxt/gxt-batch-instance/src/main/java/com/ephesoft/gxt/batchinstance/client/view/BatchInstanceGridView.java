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

package com.ephesoft.gxt.batchinstance.client.view;

import java.util.List;

import com.ephesoft.gxt.batchinstance.client.BatchInstanceManagementServiceAsync;
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstanceGridPresenter;
import com.ephesoft.gxt.batchinstance.client.proxy.BatchInstanceRpcProxy;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.GridRangeValidator;
import com.ephesoft.gxt.core.client.validator.NumericValueValidator;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.BatchInstanceProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class BatchInstanceGridView extends View<BatchInstanceGridPresenter> {

	interface Binder extends UiBinder<Widget, BatchInstanceGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField(provided = true)
	protected Grid<BatchInstanceDTO> batchInstanceGrid;

	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	@UiField
	protected VerticalPanel gridViewMainPanel;

	private boolean gridLoaded = false;

	private BatchInstanceRpcProxy proxy;

	public BatchInstanceGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		batchInstanceGrid.addStyleName("gridView");
		batchInstanceGrid.setFirstRowSelectedOnLoad(true);
		batchInstanceGrid.getStore().setAutoCommit(true);
	}

	public void loadGrid() {
		if (!gridLoaded) {
			BatchInstanceManagementServiceAsync rpcService = presenter.rpcService;
			proxy.setRpcService(rpcService);
			PagingLoader<FilterPagingLoadConfig, PagingLoadResult<BatchInstanceDTO>> loader = batchInstanceGrid.getPaginationLoader();
			pagingToolbar.bind(loader);
			loader.load();
			gridLoaded = true;
		}
	}

	public void reloadGrid() {
		if (gridLoaded) {
			PagingLoader<FilterPagingLoadConfig, PagingLoadResult<BatchInstanceDTO>> loader = batchInstanceGrid.getPaginationLoader();
			loader.load();
		}
	}

	@Override
	public void initialize() {
		proxy = new BatchInstanceRpcProxy();
		batchInstanceGrid = new Grid<BatchInstanceDTO>(proxy, PropertyAccessModel.BATCH_INSTANCE, true) {

			@Override
			public void completeEditing(CompleteEditEvent<BatchInstanceDTO> completeEditEvent) {
				batchInstanceGrid.getStore().commitChanges();
				int index = completeEditEvent.getEditCell().getRow();
				BatchInstanceDTO editedBatchInstance = batchInstanceGrid.getModel(index);
				if (batchInstanceGrid.isGridValidated()) {
					presenter.savePriority(editedBatchInstance);
				}
			}

			@Override
			protected void onGridLoad() {
				super.onGridLoad();
				// Fires the event to create charts.
				presenter.loadBatchInstanceCharts(this.getConfigBean());
				presenter.setTroubleshootArtifacts(batchInstanceGrid.getStore());
				if(batchInstanceGrid.getStore().size() < 1) { 
					presenter.clearDetailsOfPreviouslySelectedBatch();
				}
				//presenter.onFirstRowSelection(batchInstanceGrid.getStore().get(0));
				// DelayedTask task = new DelayedTask() {
				//
				// @Override
				// public void onExecute() {
				// Element ele = elementInFocus(null);
				// Window.alert("Focus on Element " + ele.getClassName() + "  ,  " + ele);
				// batchInstanceGrid.focus();
				// }
				// };
				// task.delay(2000);
			}
		};
		batchInstanceGrid.setIdProvider(BatchInstanceProperties.properties.batchName());

		this.addBatchInstanceSelectionHandler(batchInstanceGrid.getSelectionModel());
		pagingToolbar = new PagingToolbar(15, batchInstanceGrid);
		batchInstanceGrid.addSelectAllFunctionality(BatchInstanceProperties.properties.selected());
		batchInstanceGrid.setFirstRowSelectedOnLoad(true);
		batchInstanceGrid.addValidators(BatchInstanceProperties.properties.priority(), new EmptyValueValidator<BatchInstanceDTO>());
		batchInstanceGrid.addValidators(BatchInstanceProperties.properties.priority(), new NumericValueValidator<BatchInstanceDTO>());
		batchInstanceGrid.addValidators(BatchInstanceProperties.properties.priority(), new GridRangeValidator<BatchInstanceDTO>(
				"Priority should be between 1 and 100", Integer.class, 1, 100));
	}

	public void addBatchInstanceSelectionHandler(GridSelectionModel<BatchInstanceDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<BatchInstanceDTO> cellSelectionModel = (CellSelectionModel<BatchInstanceDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<BatchInstanceDTO>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<BatchInstanceDTO> event) {
					presenter.selectBatchInstance(event);
				}
			});
		}
	}

	public List<BatchInstanceDTO> getSelectedBatchInstances() {
		return batchInstanceGrid.getSelectedModels();
	}

	public Grid<BatchInstanceDTO> getBatchInstanceGrid() {
		return batchInstanceGrid;
	}

	public void reSizeGrid(ResizeEvent event) {
		batchInstanceGrid.setWidth(event.getWidth());
	}

	// public static native Element elementInFocus(Element element) /*-{
	// return document.activeElement;
	// }-*/;
}
