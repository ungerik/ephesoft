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
import com.ephesoft.gxt.core.shared.dto.RuleInfoDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.RuleInfoProperties;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

/**
 * This column config service deals with table validation rule grid structure.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.TableRuleColumnConfigService
 */
@SuppressWarnings("rawtypes")
public class TableRuleColumnConfigService implements ColumnConfigService<RuleInfoDTO> {

	/** The column config list. */
	private final List<ColumnConfig<RuleInfoDTO, ?>> columnConfigList;

	/** The editors map. */
	private final Map<ColumnConfig, IsField> editorsMap;

	/**
	 * Instantiates a new table rule column config service.
	 */
	public TableRuleColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<RuleInfoDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();
		final ColumnConfig<RuleInfoDTO, Boolean> modelSelector = new ColumnConfig<RuleInfoDTO, Boolean>(
				RuleInfoProperties.INSTANCE.selected());
		final CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_SELECT));
		modelSelector.setWidth(45);
		modelSelector.setFixed(true);
		modelSelector.setSortable(false);
		final ColumnConfig<RuleInfoDTO, String> ruleIdentifier = new ColumnConfig<RuleInfoDTO, String>(
				RuleInfoProperties.INSTANCE.rule());
		ruleIdentifier.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_RULE));
		ruleIdentifier.setHideable(false);
		
		final ColumnConfig<RuleInfoDTO, String> description = new ColumnConfig<RuleInfoDTO, String>(
				RuleInfoProperties.INSTANCE.description());
		description.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_DESCRIPTION));
		description.setHideable(false);
		
		final ColumnConfig<RuleInfoDTO, RuleInfoDTO> ruleSetter = new ColumnConfig<RuleInfoDTO, RuleInfoDTO>(
				RuleInfoProperties.INSTANCE.identity());
		ruleSetter.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_SET_RULE));
		ruleSetter.setHideable(false);
		ruleSetter.setWidth(320);

		columnConfigList.add(modelSelector);
		columnConfigList.add(ruleIdentifier);
		columnConfigList.add(description);
		columnConfigList.add(ruleSetter);

		editorsMap.put(description, new TextField());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getColumnConfig()
	 */
	@Override
	public <V> List<ColumnConfig<RuleInfoDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getEditorsMap()
	 */
	@SuppressWarnings("unchecked")
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
	public ModelKeyProvider<RuleInfoDTO> getKeyProvider() {
		return RuleInfoProperties.INSTANCE.identifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getFilters()
	 */
	@Override
	public List<Filter<RuleInfoDTO, ?>> getFilters() {
		return null;
	}

}
