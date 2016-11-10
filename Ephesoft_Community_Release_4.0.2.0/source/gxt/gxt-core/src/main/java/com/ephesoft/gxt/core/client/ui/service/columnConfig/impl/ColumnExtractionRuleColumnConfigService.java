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

package com.ephesoft.gxt.core.client.ui.service.columnConfig.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.ColumnExtractionRuleProperties;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

/**
 * This column config service deals with function key grid structure.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.ColumnExtractionRuleColumnConfigService
 */
@SuppressWarnings("rawtypes")
public class ColumnExtractionRuleColumnConfigService implements ColumnConfigService<TableColumnExtractionRuleDTO> {

	/** The properties. */
	ColumnExtractionRuleProperties properties = ColumnExtractionRuleProperties.getProperties();

	/** The column config list. */
	private final List<ColumnConfig<TableColumnExtractionRuleDTO, ?>> columnConfigList;

	/** The editors map. */
	private final Map<ColumnConfig, IsField> editorsMap;

	/**
	 * Instantiates a new email list column config service.
	 */
	public ColumnExtractionRuleColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<TableColumnExtractionRuleDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();

		ValueProvider<TableColumnExtractionRuleDTO, String> columnPatternValueProvider = properties.getColumnPattern();
		ValueProvider<TableColumnExtractionRuleDTO, String> columnHeaderPatternValueProvider = properties.getColumnHeaderPattern();
		ValueProvider<TableColumnExtractionRuleDTO, String> betLeftColumnValueProvider = properties.getBetweenLeft();
		ValueProvider<TableColumnExtractionRuleDTO, String> betRightColumnValueProvider = properties.getBetweenRight();

		final ColumnConfig<TableColumnExtractionRuleDTO, String> columnName = new ColumnConfig<TableColumnExtractionRuleDTO, String>(
				properties.getColumnName());
		columnName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_COLUMN_NAME));
		// columnName.setSortable(false);

		final ColumnConfig<TableColumnExtractionRuleDTO, String> columnPattern = new ColumnConfig<TableColumnExtractionRuleDTO, String>(
				columnPatternValueProvider);
		columnPattern.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_COLUMN_PATTERN));
		// columnPattern.setSortable(false);

		final ColumnConfig<TableColumnExtractionRuleDTO, String> betLeftColumn = new ColumnConfig<TableColumnExtractionRuleDTO, String>(
				betLeftColumnValueProvider);
		betLeftColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_BETWEEN_LEFT));
		// betLeftColumn.setSortable(false);

		final ColumnConfig<TableColumnExtractionRuleDTO, String> betRightColumn = new ColumnConfig<TableColumnExtractionRuleDTO, String>(
				betRightColumnValueProvider);
		betRightColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_BETWEEN_RIGHT));
		// betRightColumn.setSortable(false);

		final ColumnConfig<TableColumnExtractionRuleDTO, String> colHeaderPattern = new ColumnConfig<TableColumnExtractionRuleDTO, String>(
				columnHeaderPatternValueProvider);
		colHeaderPattern.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_COLUMN_HEADER_PATTERN));
		colHeaderPattern.setWidth(140);
		colHeaderPattern.setFixed(true);
		// colHeaderPattern.setSortable(false);

		final ColumnConfig<TableColumnExtractionRuleDTO, Integer> startCoordinate = new ColumnConfig<TableColumnExtractionRuleDTO, Integer>(
				properties.getColumnStartCoordinate());
		startCoordinate.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_START_COORDINATE));
		// startCoordinate.setSortable(false);

		final ColumnConfig<TableColumnExtractionRuleDTO, Integer> endCoordinate = new ColumnConfig<TableColumnExtractionRuleDTO, Integer>(
				properties.getColumnEndCoordinate());
		endCoordinate.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_END_COORDINATE));
		// endCoordinate.setSortable(false);

		final ColumnConfig<TableColumnExtractionRuleDTO, Boolean> multiLineAnchor = new ColumnConfig<TableColumnExtractionRuleDTO, Boolean>(
				properties.getMandatory());
		multiLineAnchor.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_MULTILINE_ANCHOR));
		multiLineAnchor.setWidth(100);
		multiLineAnchor.setFixed(true);
		// multiLineAnchor.setSortable(false);

		final ColumnConfig<TableColumnExtractionRuleDTO, Boolean> required = new ColumnConfig<TableColumnExtractionRuleDTO, Boolean>(
				properties.getRequired());
		required.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_REQUIRED));
		required.setWidth(60);
		required.setFixed(true);
		// required.setSortable(false);

		final ColumnConfig<TableColumnExtractionRuleDTO, String> extractColData = new ColumnConfig<TableColumnExtractionRuleDTO, String>(
				properties.getExtractedDataColumnName());
		extractColData.setWidth(140);
		extractColData.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_EXTRACTED_DATA_FROM_COLUMN));
		extractColData.setFixed(true);
		// extractColData.setSortable(false);

		final ColumnConfig<TableColumnExtractionRuleDTO, Boolean> currency = new ColumnConfig<TableColumnExtractionRuleDTO, Boolean>(
				properties.getCurrency());
		final CheckBoxCell currencyCell = new CheckBoxCell();
		currency.setCell(currencyCell);
		currency.setWidth(75);
		currency.setFixed(true);
		currency.setResizable(false);
		currency.setSortable(false);
		currency.setHideable(true);
		currency.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_CURRENCY));
		// currency.setSortable(false);

		columnConfigList.add(columnName);
		columnConfigList.add(columnPattern);
		columnConfigList.add(betLeftColumn);
		columnConfigList.add(betRightColumn);
		columnConfigList.add(colHeaderPattern);
		columnConfigList.add(startCoordinate);
		columnConfigList.add(endCoordinate);
		columnConfigList.add(multiLineAnchor);
		columnConfigList.add(required);
		columnConfigList.add(extractColData);
		columnConfigList.add(currency);

		// editorsMap.put(columnPattern, new TextField());
		//editorsMap.put(betLeftColumn, new TextField());
		//editorsMap.put(betRightColumn, new TextField());
		// editorsMap.put(colHeaderPattern, new TextField());
		editorsMap.put(startCoordinate, new IntegerField());
		editorsMap.put(endCoordinate, new IntegerField());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getColumnConfig()
	 */
	@Override
	public <V> List<ColumnConfig<TableColumnExtractionRuleDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getEditorsMap()
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public Map<ColumnConfig, IsField> getEditorsMap() {
		return editorsMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getKeyProvider()
	 */
	@Override
	public ModelKeyProvider<TableColumnExtractionRuleDTO> getKeyProvider() {
		return properties.identifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getFilters()
	 */
	@Override
	public List<Filter<TableColumnExtractionRuleDTO, ?>> getFilters() {
		return null;
	}

}
