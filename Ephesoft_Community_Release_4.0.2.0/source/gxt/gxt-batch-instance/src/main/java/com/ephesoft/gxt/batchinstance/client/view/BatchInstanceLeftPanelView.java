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

import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstanceGridPresenter;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.util.PieChart;
import com.ephesoft.gxt.core.client.ui.widget.util.StackedChart;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class BatchInstanceLeftPanelView extends View<BatchInstanceGridPresenter> {

	interface Binder extends UiBinder<Widget, BatchInstanceLeftPanelView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	/** The pie chart container. */
	@UiField
	protected ContentPanel pieChartContainer;

	/** The stack chart container. */
	@UiField
	protected ContentPanel stackChartContainer;

	/**
	 * Instantiates a new batch instance left panel view.
	 */
	public BatchInstanceLeftPanelView() {
		super();
		initWidget(binder.createAndBindUi(this));
		// WidgetUtil.setID(batchInstanceStatusPieChart, "batchInstanceStatusPieChart");
		pieChartContainer.setHeadingText(LocaleDictionary.getConstantValue(BatchInstanceConstants.BATCH_INSTANCE_CATEGORIES_HEADER));
		stackChartContainer.setHeadingText(LocaleDictionary.getConstantValue(BatchInstanceConstants.REVIEW_VALIDATION_BACKLOG_HEADER));
		pieChartContainer.addStyleName("panelHeader");
		stackChartContainer.addStyleName("panelHeader");
		pieChartContainer.addStyleName("pieChartContainer");
		stackChartContainer.addStyleName("stackChartContainer");
		pieChartContainer.setVisible(false);
		stackChartContainer.setVisible(false);
	}

	@Override
	public void initialize() {
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		stackChartContainer.forceLayout();
		pieChartContainer.forceLayout();
	}

	/**
	 * Adds the batch instance status pie chart.
	 *
	 * @param batchInstanceStatusPieChart the batch instance status pie chart
	 */
	public void addBatchInstanceStatusPieChart(PieChart batchInstanceStatusPieChart) {
		if (pieChartContainer.isAttached()) {
			pieChartContainer.setVisible(true);
			pieChartContainer.add(batchInstanceStatusPieChart);
		}
	}

	/**
	 * Adds the batch instance status stack chart.
	 *
	 * @param batchInstanceStatusStackChart the batch instance status stack chart
	 */
	public void addBatchInstanceStatusStackChart(StackedChart batchInstanceStatusStackChart) {
		if (stackChartContainer.isAttached()) {
			stackChartContainer.setVisible(true);
			stackChartContainer.add(batchInstanceStatusStackChart);
		}
	}

	/**
	 * Clear batch instance status pie chart container.
	 */
	public void clearBatchInstanceStatusPieChartContainer() {
		if (pieChartContainer.isAttached()) {
			pieChartContainer.clear();
		}
	}

	/**
	 * Clear batch instance status stack chart container.
	 */
	public void clearBatchInstanceStatusStackChartContainer() {
		if (stackChartContainer.isAttached()) {
			stackChartContainer.clear();
		}
	}

	/**
	 * Removes the pie chart container.
	 */
	public void removePieChartContainer() {
		pieChartContainer.setVisible(false);
	}

	/**
	 * Removes the stack chart container.
	 */
	public void removeStackChartContainer() {
		stackChartContainer.setVisible(false);
	}
}
