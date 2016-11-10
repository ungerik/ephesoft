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

package com.ephesoft.gxt.core.client.ui.widget.util;

import java.util.List;

import com.ephesoft.gxt.core.shared.CategorisedData;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.NumberUtil;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.chart.series.Series.LabelPosition;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;

public class BarChart extends Chart<CategorisedData> {

	BarSeries<CategorisedData> barSeries;

	public BarChart() {

	}

	public BarChart(final ListStore<CategorisedData> store, final GraphOrientation orientation,
			final ValueProvider<CategorisedData, String> categoriesProvider,
			final ValueProvider<CategorisedData, Double> numericalDataProvider, final String numericalAxisTitle,
			final String categoryAxisTitle, final int categoryAxisFontSize) {
		if (null != store && null != categoriesProvider && numericalDataProvider != null) {
			this.setStore(store);
			final Position categoryPosition = (orientation == GraphOrientation.VERTICAL) ? Position.BOTTOM : Position.LEFT;
			final Position numericalAxisPosition = (orientation == GraphOrientation.VERTICAL) ? Position.LEFT : Position.BOTTOM;
			final CategoryAxis<CategorisedData, String> categoryAxis = getChartCategoryAxis(categoryPosition, categoryAxisTitle,
					categoriesProvider, categoryAxisFontSize);
			final NumericAxis<CategorisedData> numericAxis = getNumericalAxis(numericalAxisPosition, numericalAxisTitle,
					numericalDataProvider);
			numericAxis.setMinimum(0);
			double maxValue = ValueProviderUtil.getMaxValue(numericalDataProvider, store);
			numericAxis.setSteps((int) Math.min(10, Math.ceil(maxValue)));
			numericAxis.setMaximum(NumberUtil.roundUpNumberByUsingMultipleValue(maxValue, 100));
			this.addAxis(categoryAxis);
			this.addAxis(numericAxis);
			barSeries = new BarSeries<CategorisedData>();
			barSeries.setYAxisPosition(numericalAxisPosition);
			barSeries.setHighlighting(true);
			barSeries.setColumn(numericalAxisPosition == Position.LEFT ? true : false);
			barSeries.addYField(numericalDataProvider);
			barSeries.setLabelConfig(getLabelConfig());
			this.addSeries(barSeries);
		}
	}

	private SeriesLabelConfig<CategorisedData> getLabelConfig() {
		TextSprite sprite = new TextSprite();
		sprite.setFill(RGB.BLACK);
		sprite.setFontSize(10);
		sprite.setTextAnchor(TextAnchor.START);
		SeriesLabelConfig<CategorisedData> labelConfig = new SeriesLabelConfig<CategorisedData>();
		labelConfig.setSpriteConfig(sprite);
		labelConfig.setLabelPosition(LabelPosition.OUTSIDE);
		return labelConfig;
	}

	private CategoryAxis<CategorisedData, String> getChartCategoryAxis(final Position categoryPosition, final String categoryLabel,
			final ValueProvider<CategorisedData, String> categoriesProvider, final int categoryAxisFontSize) {
		final CategoryAxis<CategorisedData, String> categoryAxis = new CategoryAxis<CategorisedData, String>();
		categoryAxis.setPosition(categoryPosition);
		categoryAxis.setField(categoriesProvider);

		TextSprite categoryAxisTextSprite = new TextSprite();

		categoryAxisTextSprite.setFontSize(categoryAxisFontSize);
		categoryAxisTextSprite.setTextBaseline(TextBaseline.MIDDLE);
		categoryAxis.setLabelConfig(categoryAxisTextSprite);
		categoryAxis.setTitleConfig(new TextSprite(categoryLabel));
		return categoryAxis;
	}

	private NumericAxis<CategorisedData> getNumericalAxis(final Position numericalAxisPosition, final String title,
			final ValueProvider<CategorisedData, Double> categoryValueProviderList) {
		final NumericAxis<CategorisedData> numericAxis = new NumericAxis<CategorisedData>();
		numericAxis.setPosition(numericalAxisPosition);
		numericAxis.addField(categoryValueProviderList);
		TextSprite numericalAxisTextSprite = new TextSprite();
		numericalAxisTextSprite.setFontSize(9);
		numericalAxisTextSprite.setTextAnchor(TextAnchor.MIDDLE);
		numericAxis.setLabelConfig(numericalAxisTextSprite);
		numericAxis.setTitleConfig(new TextSprite(title));
		return numericAxis;
	}

	public void addColors(final List<Color> colorList) {
		if (!CollectionUtil.isEmpty(colorList) && null != barSeries) {
			for (Color color : colorList) {
				barSeries.addColor(color);
			}
		}
	}
}
