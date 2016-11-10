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
import com.ephesoft.gxt.core.shared.util.NumberUtil;
import com.sencha.gxt.chart.client.chart.Chart;
import com.sencha.gxt.chart.client.chart.axis.GaugeAxis;
import com.sencha.gxt.chart.client.chart.series.GaugeSeries;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;

public class GaugeChart extends Chart<CategorisedData> {

	final GaugeSeries<CategorisedData> series;

	public GaugeChart(final ListStore<CategorisedData> store, final List<Color> colorList,
			final ValueProvider<CategorisedData, Double> provider) {
		this.setStore(store);
		this.setDefaultInsets(10);
		GaugeAxis<CategorisedData> axis = new GaugeAxis<CategorisedData>();
		axis.setMargin(-30);
		axis.setDisplayGrid(true);
		axis.setMinimum(0);
		double maxValue = ValueProviderUtil.getMaxValue(provider, store);
		axis.setSteps((int) Math.min(10, Math.ceil(maxValue)));

		axis.setMaximum(NumberUtil.roundUpNumberByUsingMultipleValue(store.get(0).getData(), 100));
		TextSprite sprite = new TextSprite();
		sprite.setFill(RGB.BLACK);
		sprite.setFontSize(8);
		sprite.setTextAnchor(TextAnchor.MIDDLE);
		axis.setLabelConfig(sprite);
		this.addAxis(axis);

		series = new GaugeSeries<CategorisedData>();
		series.addColor(colorList.get(1));
		series.addColor(colorList.get(0));
		series.setAngleField(provider);
		series.setNeedle(true);
		series.setDonut(90);
		series.setHighlighting(true);
		this.addSeries(series);

	}

}
