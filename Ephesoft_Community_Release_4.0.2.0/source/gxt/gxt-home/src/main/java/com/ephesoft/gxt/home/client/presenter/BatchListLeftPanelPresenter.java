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

package com.ephesoft.gxt.home.client.presenter;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.gxt.core.client.ui.widget.util.GraphOrientation;
import com.ephesoft.gxt.core.client.ui.widget.util.GraphUtil;
import com.ephesoft.gxt.core.client.ui.widget.util.StackedChart;
import com.ephesoft.gxt.core.shared.SubCategorisedData;
import com.ephesoft.gxt.home.client.controller.BatchListController;
import com.ephesoft.gxt.home.client.event.LoadBatchListChartsEvent;
import com.ephesoft.gxt.home.client.i18n.BatchListConstants;
import com.ephesoft.gxt.home.client.view.BatchListLeftPanelView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;

public class BatchListLeftPanelPresenter extends BatchListBasePresenter<BatchListLeftPanelView> {

	interface CustomEventBinder extends EventBinder<BatchListLeftPanelPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public BatchListLeftPanelPresenter(BatchListController controller, BatchListLeftPanelView view) {
		super(controller, view);

	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	/**
	 * Load batch execution details chart.
	 *
	 * @param batchListEvent the batch list event
	 */
	@EventHandler
	public void loadBatchExecutionDetailsChart(LoadBatchListChartsEvent batchListEvent) {
		rpcService.getBatchListStackedChartData(new AsyncCallback<List<SubCategorisedData>>() {

			@Override
			public void onFailure(Throwable caught) {
				view.removeStackChartContainer();
			}

			@Override
			public void onSuccess(List<SubCategorisedData> result) {
				List<Color> defaultColorsList = getColours(result);
				StackedChart batchListStatusStackChart = GraphUtil.getStackChart(result, defaultColorsList, GraphOrientation.VERTICAL,
						BatchListConstants.CATEGORY_TYPE, BatchListConstants.NO_OF_BATCHES);
				if (batchListStatusStackChart != null) {
					batchListStatusStackChart.setHeight(300);
					batchListStatusStackChart.setWidth(300);
					view.addBatchListStatusStackChart(batchListStatusStackChart);
				}
			}
		});
	}

	/**
	 * Gets the colours.
	 *
	 * @param data the data
	 * @return the colours
	 */
	private List<Color> getColours(List<SubCategorisedData> data) {
		List<Color> defaultColorsList = new LinkedList<Color>();
		boolean urgent = false, high = false, medium = false, low = false;
		for (SubCategorisedData subcategory : data) {
			if(urgent && high && medium && low){
				break;
			}
			String subCatPriority = subcategory.getSubCategory();
			if (!urgent && subCatPriority.equals(BatchListConstants.URGENT_PRIORITY)) {
				defaultColorsList.add(new RGB("#ff0000"));
				urgent = true;
			} else if (!high && subCatPriority.equals(BatchListConstants.HIGH_PRIORITY)) {
				defaultColorsList.add(new RGB("#ff9c00"));
				high = true;
			} else if (!medium && subCatPriority.equals(BatchListConstants.MEDIUM_PRIORITY)) {
				defaultColorsList.add(new RGB("#fff600"));
				medium = true;
			} else if (!low && subCatPriority.equals(BatchListConstants.LOW_PRIORITY)) {
				defaultColorsList.add(new RGB("#fbf9bb"));
				low = true;
			}
		}
		
		return defaultColorsList;
	}
}
