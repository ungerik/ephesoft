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

package com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction;

import java.util.List;
import com.google.gwt.user.client.Timer;

import com.ephesoft.gxt.admin.client.presenter.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionGridPresenter;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.OutputDataCarrierDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.SortChangeEvent;
import com.sencha.gxt.widget.core.client.event.SortChangeEvent.SortChangeHandler;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class AdvanvcedKVExtractionGridView extends AdvancedKVExtractionInlineView<AdvancedKVExtractionGridPresenter> implements
		HasResizableGrid {

	interface Binder extends UiBinder<Widget, AdvanvcedKVExtractionGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField(provided = true)
	protected Grid<OutputDataCarrierDTO> outputDataGrid;

	@UiField
	protected VerticalPanel gridViewMainPanel;

	protected List<OutputDataCarrierDTO> outputDataList;

	/**
	 * Constructor.
	 */
	public AdvanvcedKVExtractionGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		gridViewMainPanel.addStyleName("advKVGridPanel");
		outputDataGrid.addStyleName("gridView");
		outputDataGrid.addStyleName("advKVGridView");
		outputDataGrid.getView().setStripeRows(false);
		setWidgetIDs();
	}

	/**
	 * Setting widgets IDs.
	 */
	private void setWidgetIDs() {
		WidgetUtil.setID(outputDataGrid, "advKVOutputDataGrid");
		WidgetUtil.setID(gridViewMainPanel, "advKVGridViewMainPanel");
	}

	@Override
	public void initialize() {
		outputDataGrid = new Grid<OutputDataCarrierDTO>(PropertyAccessModel.OUTPUT_DATA_CARRIER) {
		};
		outputDataGrid.setHasPagination(false);
		this.addKeyValueSelectionHandler(outputDataGrid.getSelectionModel());
		outputDataGrid.addSortChangeHandler(new SortChangeHandler() {

			@Override
			public void onSortChange(SortChangeEvent event) {
				applyColorCodingToGrid();
			}
		});
	}

	public void addKeyValueSelectionHandler(GridSelectionModel<OutputDataCarrierDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<OutputDataCarrierDTO> cellSelectionModel = (CellSelectionModel<OutputDataCarrierDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<OutputDataCarrierDTO>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<OutputDataCarrierDTO> cellSelectionEvent) {
					presenter.scrollImageToSelectedKey(cellSelectionEvent);
				}
			});
		}
	}

	/**
	 * Gets the list of selected uploaded files.
	 * 
	 * @return {@link List}<{@link OutputDataCarrierDTO}>
	 */
	public List<OutputDataCarrierDTO> getSlectedFilesFromGrid() {
		List<OutputDataCarrierDTO> selectedFilesList = null;
		if (null != outputDataGrid) {
			selectedFilesList = outputDataGrid.getSelectedModels();
		}
		return selectedFilesList;
	}

	/**
	 * Reloads Uploaded files grid.
	 */
	public void reloadGrid() {
		WidgetUtil.reLoadGrid(outputDataGrid);
	}

	/**
	 * Sets data into the uploaded files grid and reloads it.
	 * 
	 * @param outputDataList {@link List}<{@link OutputDataCarrierDTO}>
	 */
	public void setDataAndReloadGrid(final List<OutputDataCarrierDTO> outputDataList) {
		outputDataGrid.setMemoryData(outputDataList);
		reloadGrid();
		applyColorCodingToGrid();

	}

	private void applyColorCodingToGrid() {
		Timer timer = new Timer() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

				for (int i = 0; i < outputDataGrid.getGridRowCount(); i++) {
					String value = outputDataGrid.getView().getCell(i, 5).getInnerText();
					if (null != outputDataGrid.getView().getCell(i, 5).getFirstChildElement()) {
						outputDataGrid.getView().getCell(i, 5).getFirstChildElement().getStyle().setBackgroundColor(value);
					}
					outputDataGrid.getView().getCell(i, 5).getStyle().setBackgroundColor(value);
					outputDataGrid.getView().getCell(i, 5).getStyle().setOpacity(0.4);
					outputDataGrid.getView().getCell(i, 5).getStyle().setColor(value);
				}
			}
		};
		timer.schedule(30);
	}

	public Grid<OutputDataCarrierDTO> getOutputDataGrid() {
		return outputDataGrid;
	}

	public void setOutputDataGrid(Grid<OutputDataCarrierDTO> outputDataGrid) {
		this.outputDataGrid = outputDataGrid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid#getGrid()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Grid getGrid() {
		return this.outputDataGrid;
	}

}
