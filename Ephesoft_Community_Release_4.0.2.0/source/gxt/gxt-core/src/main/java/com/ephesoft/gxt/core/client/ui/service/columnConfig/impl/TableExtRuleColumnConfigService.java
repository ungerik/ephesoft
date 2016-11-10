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
import com.ephesoft.gxt.core.shared.dto.TableExtractionAPIModel;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.TableExtractionRuleProperties;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

/**
 * This column config service deals with table info grid structure.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.TableInfoColumnConfigService
 */
@SuppressWarnings("rawtypes")
public class TableExtRuleColumnConfigService implements ColumnConfigService<TableExtractionRuleDTO> {

	/** The properties. */
	TableExtractionRuleProperties properties = TableExtractionRuleProperties.properties;

	/** The column config list. */
	private final List<ColumnConfig<TableExtractionRuleDTO, ?>> columnConfigList;

	/** The editors map. */
	private final Map<ColumnConfig, IsWidget> editorsMap;

	/**
	 * Instantiates a new email list column config service.
	 */
	public TableExtRuleColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<TableExtractionRuleDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsWidget>();

		ValueProvider<TableExtractionRuleDTO, String> startPatternValueProvider = properties.startPattern();

		ValueProvider<TableExtractionRuleDTO, String> endPatternValueProvider = properties.endPattern();

		final ColumnConfig<TableExtractionRuleDTO, Boolean> modelSelector = new ColumnConfig<TableExtractionRuleDTO, Boolean>(
				properties.selected());
		final CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector
				.setHeader(LocaleDictionary
						.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_SELECT));
		modelSelector.setWidth(45);
		modelSelector.setFixed(true);
		modelSelector.setResizable(false);
		modelSelector.setSortable(false);
		modelSelector.setHideable(false);

		final ColumnConfig<TableExtractionRuleDTO, String> ruleNameColumn = new ColumnConfig<TableExtractionRuleDTO, String>(
				properties.ruleName());
		ruleNameColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_RULE_NAME));
		ruleNameColumn.setHideable(false);

		final ColumnConfig<TableExtractionRuleDTO, String> startPatternColumn = new ColumnConfig<TableExtractionRuleDTO, String>(
				startPatternValueProvider);
		startPatternColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_START_PATTERN));
		startPatternColumn.setHideable(false);

		final ColumnConfig<TableExtractionRuleDTO, String> endPatternColumn = new ColumnConfig<TableExtractionRuleDTO, String>(
				endPatternValueProvider);
		endPatternColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_END_PATTERN));
		endPatternColumn.setHideable(false);

		final ColumnConfig<TableExtractionRuleDTO, TableExtractionAPIModel> extAPIColumn = new ColumnConfig<TableExtractionRuleDTO, TableExtractionAPIModel>(
				properties.extAPIModel());
		extAPIColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_TABLE_EXTRACTION_API));
		extAPIColumn.setSortable(false);
		extAPIColumn.setWidth(420);
		extAPIColumn.setHideable(false);

		columnConfigList.add(modelSelector);
		columnConfigList.add(ruleNameColumn);
		columnConfigList.add(startPatternColumn);
		columnConfigList.add(endPatternColumn);
		columnConfigList.add(extAPIColumn);

		editorsMap.put(ruleNameColumn, new TextField());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService #getColumnConfig()
	 */
	@Override
	public <V> List<ColumnConfig<TableExtractionRuleDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService #getEditorsMap()
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public Map<ColumnConfig, IsWidget> getEditorsMap() {
		return editorsMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService #getKeyProvider()
	 */
	@Override
	public ModelKeyProvider<TableExtractionRuleDTO> getKeyProvider() {
		return properties.identifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService #getFilters()
	 */
	@Override
	public List<Filter<TableExtractionRuleDTO, ?>> getFilters() {
		return null;
	}

}
