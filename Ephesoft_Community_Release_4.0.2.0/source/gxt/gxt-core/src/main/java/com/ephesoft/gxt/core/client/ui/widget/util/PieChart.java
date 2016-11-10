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
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.RoundNumberProvider;
import com.sencha.gxt.chart.client.chart.event.LegendHandler;
import com.sencha.gxt.chart.client.chart.series.PieSeries;
import com.sencha.gxt.chart.client.chart.series.Series.LabelPosition;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.chart.series.SeriesRenderer;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;

public class PieChart extends Chart<CategorisedData> {

	final PieSeries<CategorisedData> series;

	public PieChart(final ListStore<CategorisedData> store, final ValueProvider<CategorisedData, Double> angleField,
			final ValueProvider<CategorisedData, String> legendProvider) {
		this.setStore(store);
		series = new PieSeries<CategorisedData>();
		series.setAngleField(angleField);
		TextSprite textConfig = new TextSprite();
		textConfig.setTextBaseline(TextBaseline.MIDDLE);
		textConfig.setTextAnchor(TextAnchor.MIDDLE);
		textConfig.setZIndex(15);
		SeriesLabelConfig<CategorisedData> labelConfig = new SeriesLabelConfig<CategorisedData>();
		labelConfig.setSpriteConfig(textConfig);
		labelConfig.setLabelPosition(LabelPosition.START);
		labelConfig.setValueProvider(angleField, new RoundNumberProvider<Double>());
		series.setLabelConfig(labelConfig);
		series.setShownInLegend(true);
		series.setLegendValueProvider(legendProvider, new LabelProvider<String>() {

			@Override
			public String getLabel(String item) {
				return item;
			}
		});
		series.setHighlighting(true);
		this.setDefaultLegend();
		this.addSeries(series);
	}

	private void setDefaultLegend() {
		final Legend<CategorisedData> legend = new Legend<CategorisedData>();
		legend.setPosition(Position.RIGHT);
		legend.setItemHighlighting(true);
		legend.setItemHiding(false);
		legend.getBorderConfig().setStrokeWidth(0);
		this.setLegend(legend);
	}

	public void addColors(final List<Color> colorList) {
		if (null != colorList) {
			for (Color color : colorList) {
				series.addColor(color);
			}
		}
	}
}
