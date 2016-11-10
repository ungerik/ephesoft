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

import java.util.ArrayList;
import java.util.List;

import org.moxieapps.gwt.uploader.client.File;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DetailGrid;
import com.ephesoft.gxt.core.client.ui.widget.util.GaugeChart;
import com.ephesoft.gxt.core.client.ui.widget.util.GraphUtil;
import com.ephesoft.gxt.core.shared.CategorisedData;
import com.ephesoft.gxt.core.shared.dto.DetailsDTO;
import com.ephesoft.gxt.core.shared.util.ParseUtil;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.gxt.uploadbatch.client.presenter.UploadBatchLeftPanelPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.chart.axis.GaugeAxis;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class UploadBatchLeftPanelView extends View<UploadBatchLeftPanelPresenter> {

	/**
	 * The Interface Binder.
	 */
	interface Binder extends UiBinder<Widget, UploadBatchLeftPanelView> {
	}

	/** The Constant binder. */
	private static final Binder binder = GWT.create(Binder.class);

	/** The bar graph container. */
	@UiField
	protected ContentPanel gaugeChartContainer;

	@UiField
	protected ContentPanel detailGridContentPanel;

	@UiField
	protected DetailGrid uploadSpeedDetailsGrid;

	private GaugeChart chart;

	private File file;

	/**
	 * Instantiates a new batch list left panel view.
	 */
	public UploadBatchLeftPanelView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gaugeChartContainer.setHeadingText(LocaleDictionary.getConstantValue(UploadBatchConstants.UPLOAD_SPEED_IN_KB));
		gaugeChartContainer.addStyleName(UploadBatchConstants.UPLOAD_SPEED_GUAGE_CHART_CONTAINER_CSS);
		gaugeChartContainer.setHeight(250);
		gaugeChartContainer.setBorders(true);
		detailGridContentPanel.setStyleName(UploadBatchConstants.UPLOAD_SPEED_DETAIL_CONTENT_PANEL_CSS);
		detailGridContentPanel.setHeadingText(LocaleDictionary.getConstantValue(UploadBatchConstants.UPLOAD_DETAILS));
		uploadSpeedDetailsGrid.setVisible(false);
		uploadSpeedDetailsGrid.setStyleName(UploadBatchConstants.UPLOAD_SPEED_DETAIL_GRID_CSS);
		uploadSpeedDetailsGrid.setHideHeaders(true);
	}

	@Override
	public void initialize() {
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		final List<CategorisedData> listOfCategorisedData = new ArrayList<CategorisedData>();
		if (file != null) {
			String averageSpeed = ParseUtil.covertFromBit(file.getAverageSpeed());
			listOfCategorisedData.add(new CategorisedData(UploadBatchConstants.CATEGORY, Double.parseDouble(averageSpeed.split(" ")[0])));
			List<DetailsDTO> detailsList = setDetailsList(file);
			initializeUploadSpeedDetails(detailsList);
		} else {
			listOfCategorisedData.add(new CategorisedData(UploadBatchConstants.CATEGORY, 0.1));
			List<DetailsDTO> detailsList = setDetailsList(file);
			initializeUploadSpeedDetails(detailsList);
		}
		Timer timer = new Timer() {

			@Override
			public void run() {
				chart = GraphUtil.getGaugeChart(listOfCategorisedData);
				chart.setHeight(200);
				chart.setWidth(300);
				gaugeChartContainer.add(chart);
			}
		};
		timer.schedule(0);

		detailGridContentPanel.forceLayout();

	}

	public void initializeUploadSpeedDetails(List<DetailsDTO> detailsList) {
		uploadSpeedDetailsGrid.setData(detailsList);
		uploadSpeedDetailsGrid.setBorders(true);
		uploadSpeedDetailsGrid.setVisible(true);
	}

	/**
	 * Adds the batch list status stack chart.
	 * 
	 * @param gaugeChart the gauge chart
	 * @param label the label
	 * @param file
	 */
	public void addUploadBatchGaugeChart(GaugeChart gaugeChart, String label, File file) {
		if (gaugeChartContainer.isAttached()) {
			@SuppressWarnings("unchecked")
			GaugeAxis<CategorisedData> oldAxis = (GaugeAxis<CategorisedData>) chart.getAxis(null);
			@SuppressWarnings("unchecked")
			GaugeAxis<CategorisedData> newAxis = (GaugeAxis<CategorisedData>) gaugeChart.getAxis(null);

			if (oldAxis.getMaximum() == newAxis.getMaximum()) {
				chart.getStore().clear();
				chart.setStore(gaugeChart.getStore());
				chart.redrawChart();
			} else {
				chart = gaugeChart;
			}
			gaugeChartContainer.add(chart);
			gaugeChartContainer.setHeadingText(LocaleDictionary.getConstantValue(UploadBatchConstants.UPLOAD_SPEED_IN) + label + LocaleDictionary.getConstantValue(UploadBatchConstants.SLASH_S));
			this.file = file;
			List<DetailsDTO> detailsList = setDetailsList(file);
			initializeUploadSpeedDetails(detailsList);
		} else {
			this.file = file;
		}
	}

	private List<DetailsDTO> setDetailsList(File file) {
		List<DetailsDTO> detailsList = new ArrayList<DetailsDTO>();
		if (file != null) {
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.FILE_NAME), file.getName()));
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.SIZE), ParseUtil.covertFromBit(8 * file.getSize())));
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.UPLOADED), ParseUtil.covertFromBit(8 * file.getSizeUploaded())));
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.ELAPSED), file.getTimeElapsed() + LocaleDictionary.getConstantValue(UploadBatchConstants.SECS)));
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.CURRENT_SPEED), ParseUtil.covertFromBit(file.getCurrentSpeed()) + LocaleDictionary.getConstantValue(UploadBatchConstants.SLASH_S)));
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.AVERAGE_SPEED), ParseUtil.covertFromBit(file.getAverageSpeed()) + LocaleDictionary.getConstantValue(UploadBatchConstants.SLASH_S)));
		} else {
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.FILE_NAME), CoreCommonConstants.EMPTY_STRING));
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.SIZE), CoreCommonConstants.EMPTY_STRING));
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.UPLOADED), CoreCommonConstants.EMPTY_STRING));
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.ELAPSED), CoreCommonConstants.EMPTY_STRING));
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.CURRENT_SPEED), CoreCommonConstants.EMPTY_STRING));
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(UploadBatchConstants.AVERAGE_SPEED), CoreCommonConstants.EMPTY_STRING));
		}
		return detailsList;
	}

}
