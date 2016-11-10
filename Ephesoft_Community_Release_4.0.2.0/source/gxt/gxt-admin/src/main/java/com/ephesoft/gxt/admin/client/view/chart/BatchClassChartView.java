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

package com.ephesoft.gxt.admin.client.view.chart;

import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.chart.BatchClassChartPresenter;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;

@SuppressWarnings({"rawtypes"})
public class BatchClassChartView extends View<BatchClassChartPresenter> {

	@Override
	public void initialize() {
	}

	@UiField
	protected ContentPanel treePanel;

	@UiField
	protected ContentPanel gaugeChartContainer;

	@UiField
	protected ContentPanel barChartContainer;
	
	@UiField
	protected ContentPanel notificationContainer;
	

	interface Binder extends UiBinder<Component, BatchClassChartView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public BatchClassChartView() {
		initWidget(binder.createAndBindUi(this));
		treePanel.addStyleName("navigationPanel");
		barChartContainer.setHeadingText(LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_PPM_COMPARISON));
		gaugeChartContainer.setHeadingText(LocaleDictionary.getMessageValue(BatchClassMessages.PPM_OF_SELECTED_BATCH_CLASS));
		barChartContainer.addStyleName("panelHeader");
		gaugeChartContainer.addStyleName("panelHeader");
		notificationContainer.addStyleName("notificationNotDynamicChart");
		notificationContainer.setHeadingText("Dynamic charts are available in the Ephesoft Enterprise edition.");
		notificationContainer.setVisible(true);
		barChartContainer.setVisible(false);
		gaugeChartContainer.setVisible(false);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		barChartContainer.forceLayout();
		gaugeChartContainer.forceLayout();
		notificationContainer.forceLayout();
	}

	public void addBatchClassmanagementPPMGaugeChart(Chart batchClassPPMChart) {
		if (gaugeChartContainer.isAttached()) {
			gaugeChartContainer.setVisible(true);
			gaugeChartContainer.clear();
			gaugeChartContainer.add(batchClassPPMChart);
		}
	}

	public void addBatchClassmanagementPPMBarChart(Chart batchClassPPMChart) {
		if (barChartContainer.isAttached()) {
			barChartContainer.setVisible(true);
			barChartContainer.clear();
			barChartContainer.add(batchClassPPMChart);
		}
	}

	public void removeChartContainer() {
		barChartContainer.setVisible(false);
		gaugeChartContainer.setVisible(false);
		notificationContainer.setVisible(false);
	}
}
