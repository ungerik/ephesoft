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

package com.ephesoft.gxt.uploadbatch.client.view;

import java.util.List;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.UploadBatchDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.UploadBatchProperties;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.uploadbatch.client.presenter.UploadBatchGridPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;

public class UploadBatchGridView extends View<UploadBatchGridPresenter> {

	interface Binder extends UiBinder<Widget, UploadBatchGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField(provided = true)
	protected Grid<UploadBatchDTO> uploadedFilesGrid;

	@UiField
	protected VerticalPanel gridViewMainPanel;

	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	protected List<UploadBatchDTO> uploadedFileList;

	/**
	 * Constructor.
	 */
	public UploadBatchGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName(CoreCommonConstants.GRID_VIEW_MAIN_PANEL_CSS);
		uploadedFilesGrid.addStyleName(CoreCommonConstants.GRID_VIEW_CSS);

		PagingLoader<FilterPagingLoadConfig, PagingLoadResult<UploadBatchDTO>> loader = uploadedFilesGrid.getPaginationLoader();
		loader.load();
		pagingToolbar.bind(loader);
		uploadedFilesGrid.getStore().setAutoCommit(true);
		setWidgetIDs();
	}

	/**
	 * Setting widgets IDs.
	 */
	private void setWidgetIDs() {
		WidgetUtil.setID(uploadedFilesGrid, "uploadedFilesGrid");
	}

	@Override
	public void initialize() {
		uploadedFilesGrid = new Grid<UploadBatchDTO>(PropertyAccessModel.UPLOAD_BATCH) {

		};

		pagingToolbar = new PagingToolbar(15, uploadedFilesGrid);
		uploadedFilesGrid.addSelectAllFunctionality(UploadBatchProperties.properties.selected());
	}

	/*
	 * public void addBatchInstanceSelectionHandler(GridSelectionModel<UploadBatchDTO> selectionModel) {
	 * 
	 * }
	 */

	/**
	 * Gets the list of selected uploaded files.
	 * 
	 * @return {@link List}<{@link UploadBatchDTO}>
	 */
	public List<UploadBatchDTO> getSlectedFilesFromGrid() {
		List<UploadBatchDTO> selectedFilesList = null;
		if (null != uploadedFilesGrid) {
			selectedFilesList = uploadedFilesGrid.getSelectedModels();
		}
		return selectedFilesList;
	}

	/**
	 * Reloads Uploaded files grid.
	 */
	public void reloadGrid() {
		WidgetUtil.reLoadGrid(uploadedFilesGrid);
	}

	/**
	 * Sets data into the uploaded files grid and reloads it.
	 * 
	 * @param uploadedFileList {@link List}<{@link UploadBatchDTO}>
	 */
	public void setDataAndReloadGrid(final List<UploadBatchDTO> uploadedFileList) {
		uploadedFilesGrid.setMemoryData(uploadedFileList);
		reloadGrid();
	}

	public void reSizeGrid(ResizeEvent event) {
		uploadedFilesGrid.setWidth(event.getWidth());
		uploadedFilesGrid.setHeight((int) (event.getHeight() * .90));
	}

	/**
	 * Refresh the row with with the provided model.
	 * 
	 * @param row {@link UploadBatchDTO}
	 */
	public void refreshRecord(UploadBatchDTO row) {
		final int indexOfRow = uploadedFilesGrid.getRowIndex(row);
		if (indexOfRow != -1) {
			uploadedFilesGrid.refreshRow(indexOfRow);
		}
	}

	/**
	 * Delete the items present in the list from grid.
	 * 
	 * @param selectedModels
	 */
	public void deleteModels(List<UploadBatchDTO> selectedModels) {
		if (!CollectionUtil.isEmpty(selectedModels)) {
			uploadedFilesGrid.removeItemsFromGrid(selectedModels);
			WidgetUtil.reLoadGrid(uploadedFilesGrid);
		}
	}
}
