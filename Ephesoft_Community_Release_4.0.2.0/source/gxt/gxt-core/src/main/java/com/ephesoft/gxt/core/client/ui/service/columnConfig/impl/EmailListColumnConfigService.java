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
import com.ephesoft.gxt.core.shared.dto.BatchConstants;
import com.ephesoft.gxt.core.shared.dto.EmailConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.EmailImportConfigurationProperties;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

/**
 * This column config service deals with email import grid structure.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.EmailListColumnConfigService
 */
@SuppressWarnings("rawtypes")
public class EmailListColumnConfigService implements ColumnConfigService<EmailConfigurationDTO> {

	/** The properties. */
	EmailImportConfigurationProperties properties = EmailImportConfigurationProperties.getEmailProperties();

	/** The column config list. */
	private List<ColumnConfig<EmailConfigurationDTO, ?>> columnConfigList;

	/** The editors map. */
	private Map<ColumnConfig, IsField> editorsMap;

	/**
	 * Instantiates a new email list column config service.
	 */
	public EmailListColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<EmailConfigurationDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();

		final ColumnConfig<EmailConfigurationDTO, Boolean> modelSelector = new ColumnConfig<EmailConfigurationDTO, Boolean>(
				properties.getSelected());
		final CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_SELECT));
		modelSelector.setWidth(45);
		modelSelector.setFixed(true);
		modelSelector.setResizable(false);
		modelSelector.setSortable(false);
		modelSelector.setHideable(false);

		final ColumnConfig<EmailConfigurationDTO, String> userNameColumn = new ColumnConfig<EmailConfigurationDTO, String>(
				properties.getUserName());
		userNameColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_USERNAME));
		userNameColumn.setHideable(false);

		final ColumnConfig<EmailConfigurationDTO, String> passwordColumn = new ColumnConfig<EmailConfigurationDTO, String>(
				properties.getPassword());
		passwordColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_PASSWORD));
		passwordColumn.setHideable(false);
		passwordColumn.setSortable(false);

		final ColumnConfig<EmailConfigurationDTO, String> serverNameColumn = new ColumnConfig<EmailConfigurationDTO, String>(
				properties.getServerName());
		serverNameColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_INCOMING_SERVER));
		serverNameColumn.setHideable(false);

		final ColumnConfig<EmailConfigurationDTO, String> serverTypeColumn = new ColumnConfig<EmailConfigurationDTO, String>(
				properties.getServerType());
		serverTypeColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_SERVERTYPE));
		serverTypeColumn.setHideable(false);

		final ColumnConfig<EmailConfigurationDTO, String> folderColumn = new ColumnConfig<EmailConfigurationDTO, String>(
				properties.getFolderName());
		folderColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_FOLDER));
		folderColumn.setSortable(false);

		final ColumnConfig<EmailConfigurationDTO, Boolean> sslColumn = new ColumnConfig<EmailConfigurationDTO, Boolean>(
				properties.getIsSSL());
		final CheckBoxCell columnCell = new CheckBoxCell();
		sslColumn.setCell(columnCell);
		sslColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_SSL));
		sslColumn.setSortable(false);
		sslColumn.setHideable(false);

		final ColumnConfig<EmailConfigurationDTO, Boolean> isEnableColumn = new ColumnConfig<EmailConfigurationDTO, Boolean>(
				properties.getIsEnabled());
		final CheckBoxCell isEnabledCell = new CheckBoxCell();
		isEnableColumn.setCell(isEnabledCell);
		isEnableColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_ENABLE));
		isEnableColumn.setSortable(false);

		final ColumnConfig<EmailConfigurationDTO, Integer> portColumn = new ColumnConfig<EmailConfigurationDTO, Integer>(
				properties.getPortNumber());
		portColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_PORT));
		portColumn.setSortable(false);
		portColumn.setHideable(false);

		columnConfigList.add(modelSelector);
		columnConfigList.add(userNameColumn);
		columnConfigList.add(passwordColumn);
		columnConfigList.add(serverNameColumn);
		columnConfigList.add(serverTypeColumn);
		columnConfigList.add(folderColumn);
		columnConfigList.add(sslColumn);
		columnConfigList.add(isEnableColumn);
		columnConfigList.add(portColumn);


		final LabelProvider<String> serverTypeLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox<String> serverTypeCombo = new SimpleComboBox<String>(serverTypeLabelProvider);
		serverTypeCombo.setTriggerAction(TriggerAction.ALL);
		serverTypeCombo.add(BatchConstants.POP3_SERVER_TYPE);
		serverTypeCombo.add(BatchConstants.IMAP_SERVER_TYPE);
		
		editorsMap.put(userNameColumn, new TextField());
		editorsMap.put(passwordColumn, new PasswordField());
		editorsMap.put(serverNameColumn, new TextField());
		editorsMap.put(serverTypeColumn, serverTypeCombo);
		editorsMap.put(portColumn, new IntegerField());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getColumnConfig()
	 */
	@Override
	public <V> List<ColumnConfig<EmailConfigurationDTO, ?>> getColumnConfig() {
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
	public ModelKeyProvider<EmailConfigurationDTO> getKeyProvider() {
		return properties.getIdentifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getFilters()
	 */
	@Override
	public List<Filter<EmailConfigurationDTO, ?>> getFilters() {
		return null;
	}

}
