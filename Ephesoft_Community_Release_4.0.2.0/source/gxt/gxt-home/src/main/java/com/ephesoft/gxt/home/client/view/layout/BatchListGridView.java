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

package com.ephesoft.gxt.home.client.view.layout;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.BatchClassFieldProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.BatchInstanceProperties;
import com.ephesoft.gxt.home.client.BatchListServiceAsync;
import com.ephesoft.gxt.home.client.i18n.BatchListConstants;
import com.ephesoft.gxt.home.client.presenter.BatchListGridPresenter;
import com.ephesoft.gxt.home.client.proxy.BatchListRpcProxy;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.LoadEvent;
import com.sencha.gxt.data.shared.loader.LoadHandler;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent.RowDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class BatchListGridView extends View<BatchListGridPresenter> {

	interface Binder extends UiBinder<Widget, BatchListGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField(provided = true)
	protected Grid<BatchInstanceDTO> batchListGrid;

	@UiField
	protected VerticalPanel gridViewMainPanel;

	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	private boolean gridLoaded = false;

	private BatchListRpcProxy proxy;

	public BatchListGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName(CoreCommonConstants.GRID_VIEW_MAIN_PANEL_CSS);
		batchListGrid.addStyleName(CoreCommonConstants.GRID_VIEW_CSS);
		batchListGrid.addStyleName(BatchListConstants.GRID_VIEW_WITHOUT_CHECKBOX_CSS);
	}

	public void loadGrid() {
		if (!gridLoaded) {
			BatchListServiceAsync rpcService = presenter.getRpcService();
			proxy.setRpcService(rpcService);
			PagingLoader<FilterPagingLoadConfig, PagingLoadResult<BatchInstanceDTO>> loader = batchListGrid.getPaginationLoader();
			pagingToolbar.bind(loader);
			loader.load();
			gridLoaded = true;
			presenter.loadCharts();
			if (null != loadHandler) {
				batchListGrid.addLoadHandler(loadHandler);
			}
		}
	}

	public void reloadGrid() {
		if (gridLoaded) {
			PagingLoader<FilterPagingLoadConfig, PagingLoadResult<BatchInstanceDTO>> loader = batchListGrid.getPaginationLoader();
			loader.load();
		}
	}

	public void initialize() {
		proxy = new BatchListRpcProxy();
		batchListGrid = new Grid<BatchInstanceDTO>(proxy, PropertyAccessModel.BATCH_LIST, true);
		this.addBatchInstanceSelectionHandler(batchListGrid.getSelectionModel());
		pagingToolbar = new PagingToolbar(15, batchListGrid);
		batchListGrid.addRowDoubleClickHandler(new RowDoubleClickHandler() {

			public void onRowDoubleClick(RowDoubleClickEvent event) {
				presenter.gotoReviewAndValidatePage();
			}
		});
		batchListGrid.setIdProvider(BatchInstanceProperties.properties.batchIdentifier());
	}

	/**
	 * @return the batchListGrid
	 */
	public Grid<BatchInstanceDTO> getBatchListGrid() {
		return batchListGrid;
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

	/**
	 * @return the proxy
	 */
	public BatchListRpcProxy getProxy() {
		return proxy;
	}

	LoadHandler<FilterPagingLoadConfig, PagingLoadResult<BatchInstanceDTO>> loadHandler = new LoadHandler<FilterPagingLoadConfig, PagingLoadResult<BatchInstanceDTO>>() {

		@Override
		public void onLoad(LoadEvent<FilterPagingLoadConfig, PagingLoadResult<BatchInstanceDTO>> event) {
			presenter.checkRowCount();
		}
	};
}
