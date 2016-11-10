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

package com.ephesoft.gxt.admin.client.presenter.chart;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.BatchClassListLoadEvent;
import com.ephesoft.gxt.admin.client.event.BatchClassSelectionEvent;
import com.ephesoft.gxt.admin.client.event.DialogWindowResizeEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.view.chart.BatchClassChartView;
import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.util.BarChart;
import com.ephesoft.gxt.core.client.ui.widget.util.GaugeChart;
import com.ephesoft.gxt.core.client.ui.widget.util.GraphUtil;
import com.ephesoft.gxt.core.shared.CategorisedData;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class BatchClassChartPresenter extends BasePresenter<BatchClassManagementController, BatchClassChartView> {

	private boolean refresh;

	interface CustomEventBinder extends EventBinder<BatchClassChartPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public BatchClassChartPresenter(BatchClassManagementController controller, BatchClassChartView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	@EventHandler
	public void loadBatchClassPPMBarChart(BatchClassListLoadEvent batchClassChartsEvent) {
		loadCharts(batchClassChartsEvent.getListOfBatchClassDTOs());
	}

	private void loadCharts(List<BatchClassDTO> listOfBC) {
		final List<String> batchIdentifierList = new ArrayList<String>();
		for (BatchClassDTO dto : listOfBC) {
			batchIdentifierList.add(dto.getIdentifier());

		}
		controller.getRpcService().getBatchClassManagementPPMData(batchIdentifierList, new AsyncCallback<List<CategorisedData>>() {

			@Override
			public void onFailure(Throwable caught) {
				final List<CategorisedData> categorisedDataList = new ArrayList<CategorisedData>();
				for (String identifier : batchIdentifierList) {
					final CategorisedData data = new CategorisedData();
					data.setCategory(identifier);
					data.setData(0);
					categorisedDataList.add(data);
				}
				drawChart(categorisedDataList);
			}

			@Override
			public void onSuccess(List<CategorisedData> result) {
				drawChart(result);
			}

			private void drawChart(List<CategorisedData> result) {
				if (result.size() >= 1) {
					int categoryAxisLabelFontSize = 10;
					if (result.size() > 10) {
						categoryAxisLabelFontSize = 8;
					}
					BarChart batchClassPPMChart =  GraphUtil.getBarChart(result,
					 LocaleDictionary.getConstantValue(BatchClassConstants.PAGE_PER_MINUTE),LocaleDictionary.getConstantValue(BatchClassConstants.BATCH_CLASS),
					 categoryAxisLabelFontSize);

					batchClassPPMChart.setHeight(300);
					batchClassPPMChart.setWidth(300);
					batchClassPPMChart.setStyleName("chartCSS");
					view.addBatchClassmanagementPPMBarChart(batchClassPPMChart);
					controller.setBatchClassPPMDetails(result);
					if (controller.getSelectedBatchClass() != null) {
						loadBatchClassPPMGaugeChart();
					}
				}

			}

		});

	}

	public void loadBatchClassPPMGaugeChart() {
		BatchClassDTO selectedBatchClass = controller.getSelectedBatchClass();
		if ((selectedBatchClass != null) && (controller.isBatchClassSelectionChanged())) {
			List<CategorisedData> result = controller.getBatchClassPPMDetail(selectedBatchClass.getIdentifier());
			GaugeChart batchClassPPMChart = GraphUtil.getGaugeChart(result);
			if (batchClassPPMChart != null) {
				batchClassPPMChart.setHeight(200);
				batchClassPPMChart.setWidth(300);
				view.addBatchClassmanagementPPMGaugeChart(batchClassPPMChart);
			}
		}
	}

	@EventHandler
	public void loadBatchClassPPMGaugeChart(BatchClassSelectionEvent batchClassChartsEvent) {
		BatchClassDTO selectedBatchClass = controller.getSelectedBatchClass();
		if ((selectedBatchClass != null) && !CollectionUtil.isEmpty(controller.getBatchClassPPMDetails())) {
			loadBatchClassPPMGaugeChart();
		}
	}

	@EventHandler
	public void loadBatchClassPPMChart(DialogWindowResizeEvent dialogWindowResizeEvent) {
		if (controller.getLeftPanelAbsoluteWidth() == 0) {
			refresh = true;
		}
		if (controller.getLeftPanelAbsoluteWidth() > 0 && refresh) {
			loadCharts(controller.getBatchClassList());
			refresh = false;
		}
	}

}
