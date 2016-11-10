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

package com.ephesoft.gxt.systemconfig.client.view.workflowmanagement;

import java.util.List;

import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.DependencyDTO;
import com.ephesoft.gxt.core.shared.dto.UploadBatchDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.DependencyProperties;
import com.ephesoft.gxt.systemconfig.client.presenter.workflowmanagement.PluginDependencyGridPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class PluginDependencyGridView extends SystemConfigInlineView<PluginDependencyGridPresenter> implements HasResizableGrid {

	interface Binder extends UiBinder<Widget, PluginDependencyGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField(provided = true)
	protected Grid<DependencyDTO> pluginDependenciesGrid;

	@UiField
	protected VerticalPanel gridViewMainPanel;

	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	/**
	 * Constructor.
	 */
	public PluginDependencyGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		pluginDependenciesGrid.addStyleName("gridView");
		PagingLoader<FilterPagingLoadConfig, PagingLoadResult<DependencyDTO>> pagingLoader = pluginDependenciesGrid
				.getPaginationLoader();
		pagingLoader.load();
		pagingToolbar.bind(pagingLoader);
		setWidgetIDs();
	}

	/**
	 * Setting widgets IDs.
	 */
	private void setWidgetIDs() {
		WidgetUtil.setID(pluginDependenciesGrid, "uploadedFilesGrid");
	}

	@Override
	public void initialize() {
		pluginDependenciesGrid = new Grid<DependencyDTO>(PropertyAccessModel.PLUGIN_DEPENDENCIES) {

			// getSelectionModel().addSelectionHandler(new SelectionHandler<TreeNode>() {
		};

		pagingToolbar = new PagingToolbar(15, pluginDependenciesGrid);
		pluginDependenciesGrid.addSelectAllFunctionality(DependencyProperties.properties.selected());
		pluginDependenciesGrid.setFirstRowSelectedOnLoad(true);
		this.addGridSelectionHandler(getGrid().getSelectionModel());
		this.pluginDependenciesGrid.setIdProvider(DependencyProperties.properties.dependencies());
	}

	public void addGridSelectionHandler(GridSelectionModel<DependencyDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<DependencyDTO> cellSelectionModel = (CellSelectionModel<DependencyDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<DependencyDTO>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<DependencyDTO> cellSelectionEvent) {
					// cellSelectionEvent.getSource().getSelectedItem()
					presenter.fireCustomGridSelectionChangeEvent(cellSelectionEvent.getSource().getSelectedItem());
				}
			});
		}
	}

	// private void addGridSelectionHandler(CellSelectionModel<DependencyDTO> selectionModel) {
	// // TODO Auto-generated method stub
	// // selectionModel.addSelectionChangedHandler(new SelectionChangedHandler<DependencyDTO>() {
	// //
	// // @Override
	// // public void onSelectionChanged(SelectionChangedEvent<DependencyDTO> event) {
	// // // TODO Auto-generated method stub
	// // List<DependencyDTO> selectedDependencyList = event.getSelection();
	// // DependencyDTO selectedDependency = selectedDependencyList.get(0);
	// // presenter.fireCustomGridSelectionChangeEvent(selectedDependency);
	// // }
	// // });
	// if (selectionModel instanceof CellSelectionModel) {
	// CellSelectionModel<DependencyDTO> cellSelectionModel = (CellSelectionModel<DependencyDTO>) selectionModel;
	//
	// cellSelectionModel
	// .addSelectionChangedHandler(new SelectionChangedHandler<DependencyDTO>() {
	//
	// @Override
	// public void onSelectionChanged(SelectionChangedEvent<DependencyDTO> event) {
	// // TODO Auto-generated method stub
	// presenter.fireCustomGridSelectionChangeEvent(event.getSelection().get(0));
	// }
	//
	// // @Override
	// // public void onCellSelectionChanged(
	// // CellSelectionChangedEvent<DependencyDTO> cellSelectionEvent) {
	// //// presenter.selectFolder(cellSelectionEvent);
	// // }
	// //
	// // @Override
	// // public void onSelectionChanged(SelectionChangedEvent<DependencyDTO> event) {
	// // // TODO Auto-generated method stub
	// //
	// // }
	// });
	//
	// }
	// }

	/**
	 * Gets the list of selected uploaded files.
	 * 
	 * @return {@link List}<{@link UploadBatchDTO}>
	 */
	public List<DependencyDTO> getSlectedDependenciesFromGrid() {
		List<DependencyDTO> selectedFilesList = null;
		if (null != pluginDependenciesGrid) {
			selectedFilesList = pluginDependenciesGrid.getSelectedModels();
		}
		return selectedFilesList;
	}

	/**
	 * Reloads Uploaded files grid.
	 */
	public void reloadGrid() {
		WidgetUtil.reLoadGrid(pluginDependenciesGrid);
	}

	/**
	 * Sets data into the uploaded files grid and reloads it.
	 * 
	 * @param uploadedFileList {@link List}<{@link UploadBatchDTO}>
	 */
	public void setDataAndReloadGrid(final List<DependencyDTO> uploadedFileList) {
		pluginDependenciesGrid.setMemoryData(uploadedFileList);
		reloadGrid();
	}

	@Override
	public Grid<DependencyDTO> getGrid() {
		// TODO Auto-generated method stub
		return pluginDependenciesGrid;
	}
}
