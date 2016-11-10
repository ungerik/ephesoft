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

package com.ephesoft.gxt.batchinstance.client.presenter;

import java.util.List;

import com.ephesoft.gxt.batchinstance.client.controller.BatchInstanceController;
import com.ephesoft.gxt.batchinstance.client.event.GridResizeEvent;
import com.ephesoft.gxt.batchinstance.client.event.LoadBatchInstanceChartsEvent;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.view.BatchInstanceLeftPanelView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.util.GraphUtil;
import com.ephesoft.gxt.core.client.ui.widget.util.PieChart;
import com.ephesoft.gxt.core.client.ui.widget.util.StackedChart;
import com.ephesoft.gxt.core.shared.CategorisedData;
import com.ephesoft.gxt.core.shared.SubCategorisedData;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class BatchInstanceLeftPanelPresenter extends BatchInstanceBasePresenter<BatchInstanceLeftPanelView> {

	interface CustomEventBinder extends EventBinder<BatchInstanceLeftPanelPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);
	private boolean refresh;

	public BatchInstanceLeftPanelPresenter(BatchInstanceController controller, BatchInstanceLeftPanelView view) {
		super(controller, view);
		
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void loadBatchInstanceChartsEvent(LoadBatchInstanceChartsEvent batchInstanceDeletionEvent) {

		// Fire RPC here to fetch the list of CategorisedData to populate the pie chart.
		rpcService.getBatchInstanceStatusData(controller.getLoadConfig(), new AsyncCallback<List<CategorisedData>>() {

			@Override
			public void onFailure(Throwable caught) {
				view.removePieChartContainer();
			}

			@Override
			public void onSuccess(List<CategorisedData> result) {
				PieChart batchInstancePieChart = GraphUtil.getPieChart(result);
				view.clearBatchInstanceStatusPieChartContainer();
				if (batchInstancePieChart != null) {
					batchInstancePieChart.setHeight(200);
					batchInstancePieChart.setWidth(300);
					view.addBatchInstanceStatusPieChart(batchInstancePieChart);
				}
			}

		});
	}

	@EventHandler
	public void loadBatchExecutionDetailsChart(LoadBatchInstanceChartsEvent batchInstanceDeletionEvent) {
		rpcService.getBatchInstanceExecutionDetailsData(new AsyncCallback<List<SubCategorisedData>>() {

			@Override
			public void onFailure(Throwable caught) {
				view.removeStackChartContainer();
			}

			@Override
			public void onSuccess(List<SubCategorisedData> result) {
				StackedChart batchInstanceStatusStackChart = GraphUtil.getStackChart(result, LocaleDictionary.getConstantValue(BatchInstanceConstants.BIM_STACK_CHART_TIME), LocaleDictionary.getConstantValue(BatchInstanceConstants.BIM_STACK_CHART_NO_OF_BATCHES));
				view.clearBatchInstanceStatusStackChartContainer();
				if (batchInstanceStatusStackChart != null) {
					batchInstanceStatusStackChart.setHeight(300);
					batchInstanceStatusStackChart.setWidth(300);
					view.addBatchInstanceStatusStackChart(batchInstanceStatusStackChart);
				}
			}
		});
	}

	@EventHandler
	public void loadBatchInstanceChart(GridResizeEvent gridResizeEvent) {
		if (controller.getLeftPanelAbsoluteWidth() == 0) {
			refresh = true;
		}
		if (controller.getLeftPanelAbsoluteWidth() > 0 && refresh) {
			controller.getEventBus().fireEvent(new LoadBatchInstanceChartsEvent());
			refresh = false;
		}
	}
}
