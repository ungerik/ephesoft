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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.client.ui.widget.util.CategoriesCollection.Category;
import com.ephesoft.gxt.core.client.ui.widget.util.CategoriesCollection.SubCategory;
import com.ephesoft.gxt.core.shared.CategorisedData;
import com.ephesoft.gxt.core.shared.SubCategorisedData;
import com.google.gwt.core.shared.GWT;
import com.sencha.gxt.chart.client.chart.Chart.Position;
import com.sencha.gxt.chart.client.chart.Legend;
import com.sencha.gxt.chart.client.chart.axis.CategoryAxis;
import com.sencha.gxt.chart.client.chart.axis.NumericAxis;
import com.sencha.gxt.chart.client.chart.series.BarSeries;
import com.sencha.gxt.chart.client.chart.series.Series.LabelPosition;
import com.sencha.gxt.chart.client.chart.series.SeriesLabelConfig;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.PropertyAccess;

public final class GraphUtil {

	private static List<Color> defaultColorsList;

	private static int MAXIMUM_DIVISION = 10;
	static {
		defaultColorsList = new LinkedList<Color>();
		defaultColorsList.add(new Color("mediumvioletred"));
		defaultColorsList.add(new Color("powderblue"));
		defaultColorsList.add(new Color("blue"));
		defaultColorsList.add(new Color("cyan"));
		defaultColorsList.add(new Color("violet"));
		defaultColorsList.add(new Color("grey"));
		defaultColorsList.add(new Color("aqua"));
		defaultColorsList.add(new Color("indigo"));
		defaultColorsList.add(new Color("lightcoral"));
		defaultColorsList.add(new Color("orange"));
		defaultColorsList.add(new Color("yellow"));
		defaultColorsList.add(new Color("red"));
		defaultColorsList.add(new Color("green"));
		defaultColorsList.add(new Color("brown"));
		defaultColorsList.add(new Color("lime"));
		defaultColorsList.add(new RGB("#9ACD32"));// YellowGreen
		defaultColorsList.add(new Color("coral"));
	}

	public static final PieChart getPieChart(final List<CategorisedData> categorisedDataList) {
		return getPieChart(categorisedDataList, defaultColorsList);
	}

	public static final PieChart getPieChart(final List<CategorisedData> categorisedDataList, List<Color> colorsList) {
		PieChart pieChart = null;
		if (!CollectionUtil.isEmpty(categorisedDataList)) {
			ListStore<CategorisedData> store = new ListStore<CategorisedData>(new UniqueIDProvider<CategorisedData>());
			store.addAll(categorisedDataList);
			pieChart = new PieChart(store, CategorisedDataPropertyAccess.INSTANCE.data(),
					CategorisedDataPropertyAccess.INSTANCE.category());
			pieChart.addColors(colorsList);
		}
		return pieChart;
	}

	public static final StackedChart getStackChart(final List<SubCategorisedData> dataList, final String categoryLabel,
			final String numericalAxisLabel) {
		return getStackChart(dataList, defaultColorsList, GraphOrientation.VERTICAL, categoryLabel, numericalAxisLabel);
	}

	public static final StackedChart getStackChart(final List<SubCategorisedData> dataList, final List<Color> colorList,
			final GraphOrientation graphAlignment, final String categoryLabel, final String numericalAxisLabel) {
		StackedChart chart = null;
		if (!CollectionUtil.isEmpty(dataList)) {
			final CategoriesCollection categoriesCollection = new CategoriesCollection();
			for (final SubCategorisedData subCategorisedData : dataList) {
				categoriesCollection.add(subCategorisedData);
			}
			chart = getStackedChart(categoriesCollection, colorList, categoryLabel, numericalAxisLabel, graphAlignment);
		}
		return chart;
	}

	private static StackedChart getStackedChart(final CategoriesCollection categoriesCollection, final List<Color> colorList,
			final String categoryLabel, final String numericalAxisLabel, final GraphOrientation graphAlignment) {
		final Set<String> uniqueSubCategoriesName = categoriesCollection.getUniqueSubCategories();
		StackedChart stackedChart = null;
		if (null != uniqueSubCategoriesName) {
			Position categoryPosition = (graphAlignment == GraphOrientation.VERTICAL) ? Position.BOTTOM : Position.LEFT;
			Position numericalAxisPosition = (graphAlignment == GraphOrientation.VERTICAL) ? Position.LEFT : Position.BOTTOM;
			final List<CategoryValueProvider> categoryValueProviderList = getCategoryValueProviders(uniqueSubCategoriesName);
			final ListStore<Category> categoryStore = new ListStore<Category>(new UniqueIDProvider<Category>());
			categoryStore.addAll(categoriesCollection.getCategoryList());
			CategoryAxis<Category, String> categoryAxis = getStackedChartCategoryAxis(categoryPosition, categoryLabel);
			stackedChart = new StackedChart(categoryStore);
			stackedChart.addAxis(categoryAxis);
			TextSprite spriteCategory = new TextSprite();
			spriteCategory.setFontSize(8);
			spriteCategory.setTextAnchor(TextAnchor.MIDDLE);
			categoryAxis.setLabelConfig(spriteCategory);

			final NumericAxis<Category> numericAxis = new NumericAxis<Category>();
			numericAxis.setPosition(numericalAxisPosition);
			addValueProviders(numericAxis, categoryValueProviderList);
			numericAxis.setHidden(false);
			double maximumValue = categoriesCollection.getMaximumCategoryValue();
			numericAxis.setSteps((int) Math.min(10, Math.ceil(maximumValue)));
			numericAxis.setMinimum(0);
			numericAxis.setLabelConfig(spriteCategory);
			stackedChart.addAxis(numericAxis);
			numericAxis.setTitleConfig(new TextSprite(numericalAxisLabel));
			final BarSeries<Category> barSeries = new BarSeries<Category>();
			barSeries.setYAxisPosition(numericalAxisPosition);
			barSeries.setHighlighting(true);
			barSeries.setColumn(numericalAxisPosition == Position.LEFT ? true : false);
			addValueProviders(barSeries, categoryValueProviderList);
			TextSprite sprite = new TextSprite();
			sprite.setFill(RGB.BLACK);
			sprite.setFontSize(10);
			sprite.setTextAnchor(TextAnchor.MIDDLE);
			SeriesLabelConfig<Category> labelConfig = new SeriesLabelConfig<Category>();
			labelConfig.setSpriteConfig(sprite);
			labelConfig.setLabelPosition(LabelPosition.END);
			barSeries.setLabelConfig(labelConfig);
			barSeries.setStacked(true);
			barSeries.setLegendTitles(getLegendTitles(uniqueSubCategoriesName));
			addColors(barSeries, colorList);
			stackedChart.addSeries(barSeries);
			stackedChart.setLegend(getStackedGraphLegend());
		}
		return stackedChart;
	}

	public static BarChart getBarChart(List<CategorisedData> categorisedDataList, final String numericalAxisTitle,
			final String categoryAxisTitle, final int categoryAxisFontSize) {
		BarChart chart = null;
		if (!CollectionUtil.isEmpty(categorisedDataList) && categoryAxisTitle != null && numericalAxisTitle != null) {
			ListStore<CategorisedData> store = new ListStore<CategorisedData>(new UniqueIDProvider<CategorisedData>());
			store.addAll(categorisedDataList);
			chart = new BarChart(store, GraphOrientation.HORIZONTAL, CategorisedDataPropertyAccess.INSTANCE.category(),
					CategorisedDataPropertyAccess.INSTANCE.data(), numericalAxisTitle, categoryAxisTitle, categoryAxisFontSize);
			chart.addColors(defaultColorsList);
		}
		return chart;
	}

	private static Legend<Category> getStackedGraphLegend() {
		final Legend<Category> legend = new Legend<Category>();
		legend.setItemHiding(false);
		legend.setItemHighlighting(true);
		return legend;
	}

	private static List<String> getLegendTitles(Set<String> legendTitles) {
		List<String> titleList = new ArrayList<String>(legendTitles.size());
		for (String title : legendTitles) {
			titleList.add(title);
		}
		return titleList;
	}

	private static CategoryAxis<Category, String> getStackedChartCategoryAxis(Position categoryPosition, String categoryLabel) {
		final CategoryAxis<Category, String> categoryAxis = new CategoryAxis<Category, String>();
		categoryAxis.setPosition(categoryPosition);
		categoryAxis.setField(CategoryPropertyAccess.INSTANCE.categoryName());
		categoryAxis.setTitleConfig(new TextSprite(categoryLabel));
		return categoryAxis;
	}

	private static void addColors(final BarSeries<?> barSeries, final List<Color> colorsList) {
		if (null != colorsList) {
			for (Color color : colorsList) {
				barSeries.addColor(color);
			}
		}
	}

	private static void addValueProviders(final NumericAxis<Category> numericAxis,
			final List<CategoryValueProvider> categoryValueProviderList) {
		for (final CategoryValueProvider categoryValueProvider : categoryValueProviderList) {
			if (null != categoryValueProvider) {
				numericAxis.addField(categoryValueProvider);
			}
		}
	}

	private static void addValueProviders(final BarSeries<Category> bar, final List<CategoryValueProvider> categoryValueProviderList) {
		for (final CategoryValueProvider categoryValueProvider : categoryValueProviderList) {
			if (null != categoryValueProvider) {
				bar.addYField(categoryValueProvider);
			}
		}
	}

	private static List<CategoryValueProvider> getCategoryValueProviders(final Set<String> uniqueSubCategoriesName) {
		final List<CategoryValueProvider> categoryValueProviders = new LinkedList<CategoryValueProvider>();
		for (final String subCategoryName : uniqueSubCategoriesName) {
			if (null != subCategoryName) {
				categoryValueProviders.add(new CategoryValueProvider(subCategoryName));
			}
		}
		return categoryValueProviders;
	}

	public static interface CategoryPropertyAccess extends PropertyAccess<Category> {

		ValueProvider<Category, String> categoryName();

		CategoryPropertyAccess INSTANCE = GWT.create(CategoryPropertyAccess.class);
	}

	public static interface CategorisedDataPropertyAccess extends PropertyAccess<CategorisedData> {

		ValueProvider<CategorisedData, String> category();

		ValueProvider<CategorisedData, Double> data();

		CategorisedDataPropertyAccess INSTANCE = GWT.create(CategorisedDataPropertyAccess.class);
	}

	private static final class CategoryValueProvider implements ValueProvider<Category, Double> {

		final String subCategoryName;

		private CategoryValueProvider(final String subCategoryName) {
			this.subCategoryName = subCategoryName;
		}

		@Override
		public Double getValue(final Category category) {
			double value = 0;
			if (null != category && null != subCategoryName) {
				final SubCategory subCategory = category.getSubCategoryByName(subCategoryName);
				if (null != subCategory) {
					value = subCategory.getData();
				}
			}
			return value;
		}

		@Override
		public void setValue(final Category object, final Double value) {
		}

		@Override
		public String getPath() {
			return null;
		}
	}

	public static final GaugeChart getGaugeChart(final List<CategorisedData> categorisedDataList) {
		GaugeChart chart = null;
		if (!CollectionUtil.isEmpty(categorisedDataList)) {
			ListStore<CategorisedData> store = new ListStore<CategorisedData>(new UniqueIDProvider<CategorisedData>());
			store.addAll(categorisedDataList);
			chart = new GaugeChart(store, defaultColorsList, CategorisedDataPropertyAccess.INSTANCE.data());
			chart.setAnimated(true);
		}
		return chart;

	}
}
