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
import com.ephesoft.gxt.core.shared.constant.FunctionKey;
import com.ephesoft.gxt.core.shared.dto.FunctionKeyDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.FunctionKeyProperties;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

/**
 * This column config service deals with function key grid structure.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.FunctionKeyColumnConfigService
 */
@SuppressWarnings("rawtypes")
public class FunctionKeyColumnConfigService implements ColumnConfigService<FunctionKeyDTO> {

	/** The properties. */
	FunctionKeyProperties properties = FunctionKeyProperties.properties;

	/** The column config list. */
	private List<ColumnConfig<FunctionKeyDTO, ?>> columnConfigList;

	/** The editors map. */
	private Map<ColumnConfig, IsField> editorsMap;

	/**
	 * Instantiates a new email list column config service.
	 */
	public FunctionKeyColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<FunctionKeyDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();

		final ColumnConfig<FunctionKeyDTO, Boolean> modelSelector = new ColumnConfig<FunctionKeyDTO, Boolean>(properties.selected());
		final CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_SELECT));
		modelSelector.setWidth(35);
		modelSelector.setFixed(true);
		modelSelector.setResizable(false);
		modelSelector.setSortable(false);
		modelSelector.setHideable(false);

		final ColumnConfig<FunctionKeyDTO, String> methodNameColumn = new ColumnConfig<FunctionKeyDTO, String>(properties.methodName());
		methodNameColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_METHOD_NAME));
		methodNameColumn.setHideable(false);

		final ColumnConfig<FunctionKeyDTO, String> methodDescColumn = new ColumnConfig<FunctionKeyDTO, String>(
				properties.methodDescription());
		methodDescColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_METHOD_DESCRIPTION));
		methodDescColumn.setHideable(false);
		
		final ColumnConfig<FunctionKeyDTO, String> functionKeyColumn = new ColumnConfig<FunctionKeyDTO, String>(
				properties.shortcutKeyName());
		functionKeyColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_SHORTCUT_KEY));
		functionKeyColumn.setHideable(false);

		columnConfigList.add(modelSelector);
		columnConfigList.add(methodNameColumn);
		columnConfigList.add(methodDescColumn);
		columnConfigList.add(functionKeyColumn);

		final LabelProvider<String> comboLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox<String> functionKeysCombo = new SimpleComboBox<String>(comboLabelProvider);
		functionKeysCombo.setTriggerAction(TriggerAction.ALL);
		functionKeysCombo.add(FunctionKey.getKeyNamesList(FunctionKey.F5.getFunctionKeyName(), FunctionKey.F12.getFunctionKeyName()));
		editorsMap.put(methodNameColumn, new TextField());
		editorsMap.put(methodDescColumn, new TextField());
		editorsMap.put(functionKeyColumn, functionKeysCombo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getColumnConfig()
	 */
	@Override
	public <V> List<ColumnConfig<FunctionKeyDTO, ?>> getColumnConfig() {
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
	public ModelKeyProvider<FunctionKeyDTO> getKeyProvider() {
		return properties.identifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getFilters()
	 */
	@Override
	public List<Filter<FunctionKeyDTO, ?>> getFilters() {
		return null;
	}

}
