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

import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.client.ui.widget.IntegerField;
import com.ephesoft.gxt.core.client.ui.widget.MultiSelectComboBox;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.BatchClassProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.BatchClassProperties.BatchClassMappingValueProvider.BatchClassRoleValueProvider;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.CheckBox;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

@SuppressWarnings("rawtypes")
public class BatchClassColumnConfigService implements ColumnConfigService<BatchClassDTO> {

	// private static final BatchClassProperties properties = GWT.create(BatchClassProperties.class);

	private List<ColumnConfig<BatchClassDTO, ?>> columnConfigList;

	private Map<ColumnConfig, IsField> editorsMap;

	private ListView<String, String> fieldValue;

	public BatchClassColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<BatchClassDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();
		ColumnConfig<BatchClassDTO, Boolean> modelSelector = new ColumnConfig<BatchClassDTO, Boolean>(
				BatchClassProperties.INSTANCE.selected());
		CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setWidth(35);
		modelSelector.setFixed(true);
		modelSelector.setSortable(false);
		modelSelector.setHideable(false);
		CheckBox checkbox = new CheckBox();
		checkbox.getElement().getStyle().setZIndex(1000);
		modelSelector.setHeader(SafeHtmlUtils.fromSafeConstant(checkbox.getElement().getInnerHTML()));
		ColumnConfig<BatchClassDTO, String> batchClassIdentifier = new ColumnConfig<BatchClassDTO, String>(
				BatchClassProperties.INSTANCE.identifier());
		batchClassIdentifier.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_IDENTIFIER));
		ColumnConfig<BatchClassDTO, String> batchClassName = new ColumnConfig<BatchClassDTO, String>(
				BatchClassProperties.INSTANCE.name());
		batchClassName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_NAME));
		ColumnConfig<BatchClassDTO, String> batchClassDescription = new ColumnConfig<BatchClassDTO, String>(
				BatchClassProperties.INSTANCE.description());
		batchClassDescription.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_DESCRIPTION));
		ColumnConfig<BatchClassDTO, Integer> batchClassPriority = new ColumnConfig<BatchClassDTO, Integer>(
				BatchClassProperties.INSTANCE.priority());
		batchClassPriority.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_PRIORITY));
		ColumnConfig<BatchClassDTO, String> batchClassUnc = new ColumnConfig<BatchClassDTO, String>(
				BatchClassProperties.INSTANCE.uncFolder());
		batchClassUnc.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BCC_UNC_PATH));
		ColumnConfig<BatchClassDTO, String> batchClassVersion = new ColumnConfig<BatchClassDTO, String>(
				BatchClassProperties.INSTANCE.version());
		batchClassVersion.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BCC_VERSION));
		batchClassVersion.setSortable(false);
		ColumnConfig<BatchClassDTO, String> currentUser = new ColumnConfig<BatchClassDTO, String>(
				BatchClassProperties.INSTANCE.currentUser());
		currentUser.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BCC_CURRENT_USER));
		ColumnConfig<BatchClassDTO, String> encryptionAlgorithm = new ColumnConfig<BatchClassDTO, String>(
				BatchClassProperties.INSTANCE.encryptionAlgo());
		encryptionAlgorithm.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BCC_ENCRYPTION));
		encryptionAlgorithm.setSortable(false);
		
		ValueProvider<BatchClassDTO, String> rolesValueProvider = BatchClassRoleValueProvider.getInstance();
		ColumnConfig<BatchClassDTO, String> roles = new ColumnConfig<BatchClassDTO, String>(
				rolesValueProvider);
		
		roles.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BCC_ROLES));
		roles.setSortable(false);
		columnConfigList.add(modelSelector);
		columnConfigList.add(batchClassIdentifier);
		columnConfigList.add(batchClassName);
		columnConfigList.add(batchClassDescription);
		columnConfigList.add(batchClassUnc);
		columnConfigList.add(batchClassVersion);
		columnConfigList.add(batchClassPriority);
		columnConfigList.add(currentUser);
		if (EphesoftUIContext.isWindows()) {
			columnConfigList.add(encryptionAlgorithm);
		}
		columnConfigList.add(roles);

		editorsMap.put(batchClassDescription, new TextField());
		editorsMap.put(batchClassPriority, new IntegerField());
		editorsMap.put(batchClassUnc, new TextField());
	}

	@Override
	public <V> List<ColumnConfig<BatchClassDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	@Override
	public Map<ColumnConfig, IsField> getEditorsMap() {
		return editorsMap;
	}

	@Override
	public ModelKeyProvider<BatchClassDTO> getKeyProvider() {
		return BatchClassProperties.INSTANCE.id();
	}

	@Override
	public List<Filter<BatchClassDTO, ?>> getFilters() {
		return null;
	}

}
