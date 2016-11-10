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
import com.ephesoft.gxt.core.shared.dto.CmisConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.CmisImportConfigurationProperties;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

/**
 * This column config service deals with cmis import grid structure.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.CmisImportColumnConfigService
 */
@SuppressWarnings("rawtypes")
public class CmisImportColumnConfigService implements ColumnConfigService<CmisConfigurationDTO> {

	/** The properties. */
	CmisImportConfigurationProperties properties = CmisImportConfigurationProperties.getCmisProperties();

	/** The column config list. */
	private final List<ColumnConfig<CmisConfigurationDTO, ?>> columnConfigList;

	/** The editors map. */
	private final Map<ColumnConfig, IsField> editorsMap;

	/**
	 * Instantiates a new cmis import column config service.
	 */
	public CmisImportColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<CmisConfigurationDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();
		final ColumnConfig<CmisConfigurationDTO, Boolean> modelSelector = new ColumnConfig<CmisConfigurationDTO, Boolean>(
				properties.getSelected());
		final CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_SELECT));
		modelSelector.setWidth(45);
		modelSelector.setFixed(true);
		modelSelector.setResizable(false);
		modelSelector.setSortable(false);
		modelSelector.setHideable(false);

		final ColumnConfig<CmisConfigurationDTO, String> serverUrlColumn = new ColumnConfig<CmisConfigurationDTO, String>(
				properties.getServerURL());
		serverUrlColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_SERVERURL));
		serverUrlColumn.setHideable(false);

		final ColumnConfig<CmisConfigurationDTO, String> userNameColumn = new ColumnConfig<CmisConfigurationDTO, String>(
				properties.getUserName());
		userNameColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_USERNAME));
		userNameColumn.setHideable(false);

		final ColumnConfig<CmisConfigurationDTO, String> passwordColumn = new ColumnConfig<CmisConfigurationDTO, String>(
				properties.getPassword());
		passwordColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_PASSWORD));
		passwordColumn.setHideable(false);
		passwordColumn.setSortable(false);

		final ColumnConfig<CmisConfigurationDTO, String> repositoryIdColumn = new ColumnConfig<CmisConfigurationDTO, String>(
				properties.getRepositoryID());
		repositoryIdColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_REPOSITORYID));

		final ColumnConfig<CmisConfigurationDTO, String> fileExtnColumn = new ColumnConfig<CmisConfigurationDTO, String>(
				properties.getFileExtension());
		fileExtnColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_FILE_EXTENSION));
		fileExtnColumn.setSortable(false);

		final ColumnConfig<CmisConfigurationDTO, String> folderColumn = new ColumnConfig<CmisConfigurationDTO, String>(
				properties.getFolderName());
		folderColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_FOLDER));
		folderColumn.setHideable(false);

		final ColumnConfig<CmisConfigurationDTO, String> cmisPropertyColumn = new ColumnConfig<CmisConfigurationDTO, String>(
				properties.getCmisProperty());
		cmisPropertyColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_PROPERTY));
		cmisPropertyColumn.setHideable(false);

		final ColumnConfig<CmisConfigurationDTO, String> valueColumn = new ColumnConfig<CmisConfigurationDTO, String>(
				properties.getValue());
		valueColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_VALUE));
		valueColumn.setHideable(false);

		final ColumnConfig<CmisConfigurationDTO, String> newValueColumn = new ColumnConfig<CmisConfigurationDTO, String>(
				properties.getValueToUpdate());
		newValueColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_NEW_VALUE));
		newValueColumn.setHideable(false);

		columnConfigList.add(modelSelector);
		columnConfigList.add(serverUrlColumn);
		columnConfigList.add(userNameColumn);
		columnConfigList.add(passwordColumn);
		columnConfigList.add(repositoryIdColumn);
		columnConfigList.add(fileExtnColumn);
		columnConfigList.add(folderColumn);
		columnConfigList.add(cmisPropertyColumn);
		columnConfigList.add(valueColumn);
		columnConfigList.add(newValueColumn);

		editorsMap.put(serverUrlColumn, new TextField());
		editorsMap.put(userNameColumn, new TextField());
		editorsMap.put(passwordColumn, new PasswordField());
		editorsMap.put(repositoryIdColumn, new TextField());
		editorsMap.put(folderColumn, new TextField());
		editorsMap.put(cmisPropertyColumn, new TextField());
		editorsMap.put(valueColumn, new TextField());
		editorsMap.put(newValueColumn, new TextField());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getColumnConfig()
	 */
	@Override
	public <V> List<ColumnConfig<CmisConfigurationDTO, ?>> getColumnConfig() {
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
	public ModelKeyProvider<CmisConfigurationDTO> getKeyProvider() {
		return properties.getIdentifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getFilters()
	 */
	@Override
	public List<Filter<CmisConfigurationDTO, ?>> getFilters() {
		return null;
	}
}
