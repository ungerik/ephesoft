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

import com.ephesoft.gxt.core.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.client.ui.widget.ListFilter;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.BatchInstanceProperties;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;

@SuppressWarnings("rawtypes")
public class BatchInstanceColumnConfigService implements ColumnConfigService<BatchInstanceDTO> {

//	private static final BatchInstanceProperties properties = GWT.create(BatchInstanceProperties.class);
	private List<ColumnConfig<BatchInstanceDTO, ?>> columnConfigList;
	private Map<ColumnConfig, IsField> editorsMap;

	public BatchInstanceColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<BatchInstanceDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();
		ColumnConfig<BatchInstanceDTO, Boolean> modelSelector = new ColumnConfig<BatchInstanceDTO, Boolean>(BatchInstanceProperties.properties.selected());
		CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setWidth(30);
		modelSelector.setFixed(true);
		modelSelector.setSortable(false);
		ColumnConfig<BatchInstanceDTO, String> batchInstanceIdentifier = new ColumnConfig<BatchInstanceDTO, String>(
				BatchInstanceProperties.properties.batchIdentifier());
		batchInstanceIdentifier.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHID));
		ColumnConfig<BatchInstanceDTO, String> batchInstanceName = new ColumnConfig<BatchInstanceDTO, String>(BatchInstanceProperties.properties.batchName());
		batchInstanceName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BIM_COLUMN_CONFIG_BATCH_NAME));
		ColumnConfig<BatchInstanceDTO, String> currentUser = new ColumnConfig<BatchInstanceDTO, String>(BatchInstanceProperties.properties.currentUser());
		currentUser.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BIM_COLUMN_CONFIG_CURRENT_USER));
		ColumnConfig<BatchInstanceDTO, String> batchClassName = new ColumnConfig<BatchInstanceDTO, String>(BatchInstanceProperties.properties.batchClassName());
		batchClassName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BIM_COLUMN_CONFIG_BATCH_CLASS_NAME));
		ColumnConfig<BatchInstanceDTO, String> importedOn = new ColumnConfig<BatchInstanceDTO, String>(BatchInstanceProperties.properties.importedOn());
		importedOn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BIM_COLUMN_CONFIG_IMPORTED_ON));
		ColumnConfig<BatchInstanceDTO, String> status = new ColumnConfig<BatchInstanceDTO, String>(BatchInstanceProperties.properties.status());
		status.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BIM_COLUMN_CONFIG_STATUS));
		ColumnConfig<BatchInstanceDTO, Integer> priority = new ColumnConfig<BatchInstanceDTO, Integer>(BatchInstanceProperties.properties.priority());
		priority.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BIM_COLUMN_CONFIG_PRIORITY));
		
		// Removed duplicate column named Uploaded On.
		ColumnConfig<BatchInstanceDTO, String> lastModified = new ColumnConfig<BatchInstanceDTO, String>(BatchInstanceProperties.properties.lastModified());
		lastModified.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.BIM_COLUMN_CONFIG_LAST_MODIFIED));
		ColumnConfig<BatchInstanceDTO, String> customColumn1 = new ColumnConfig<BatchInstanceDTO, String>(BatchInstanceProperties.properties.customColumn1());
		customColumn1.setHeader(LocaleDictionary.getConstantValue(BatchClassConstants.CUSTOM_COLUMN1_HEADER));
		ColumnConfig<BatchInstanceDTO, String> customColumn2 = new ColumnConfig<BatchInstanceDTO, String>(BatchInstanceProperties.properties.customColumn2());
		customColumn2.setHeader(LocaleDictionary.getConstantValue(BatchClassConstants.CUSTOM_COLUMN2_HEADER));
		ColumnConfig<BatchInstanceDTO, String> customColumn3 = new ColumnConfig<BatchInstanceDTO, String>(BatchInstanceProperties.properties.customColumn3());
		customColumn3.setHeader(LocaleDictionary.getConstantValue(BatchClassConstants.CUSTOM_COLUMN3_HEADER));
		ColumnConfig<BatchInstanceDTO, String> customColumn4 = new ColumnConfig<BatchInstanceDTO, String>(BatchInstanceProperties.properties.customColumn4());
		customColumn4.setHeader(LocaleDictionary.getConstantValue(BatchClassConstants.CUSTOM_COLUMN4_HEADER));

		customColumn1.setHidden(true);
		customColumn2.setHidden(true);
		customColumn3.setHidden(true);
		customColumn4.setHidden(true);
		
		batchInstanceIdentifier.setSortable(true);
		columnConfigList.add(modelSelector);
		columnConfigList.add(batchInstanceIdentifier);
		columnConfigList.add(batchInstanceName);
		columnConfigList.add(status);
		columnConfigList.add(priority);
		columnConfigList.add(batchClassName);
		columnConfigList.add(currentUser);
		columnConfigList.add(importedOn);
		columnConfigList.add(lastModified);
		columnConfigList.add(customColumn1);
		columnConfigList.add(customColumn2);
		columnConfigList.add(customColumn3);
		columnConfigList.add(customColumn4);
		editorsMap.put(modelSelector, new CheckBox());
		IntegerField field = new IntegerField();
		WidgetUtil.setID(field, "bim-Priority-TextBox");
		editorsMap.put(priority, field);
	}


	@Override
	public <V> List<ColumnConfig<BatchInstanceDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public Map<ColumnConfig, IsField> getEditorsMap() {
		return editorsMap;
	}

	@Override
	public ModelKeyProvider<BatchInstanceDTO> getKeyProvider() {
		return BatchInstanceProperties.properties.id();
	}

	@Override
	public List<Filter<BatchInstanceDTO, ?>> getFilters() {
		final List<Filter<BatchInstanceDTO, ?>> filtersList = new ArrayList<Filter<BatchInstanceDTO, ?>>();
		final ListStore<String> statusStore = new ListStore<String>(new ModelKeyProvider<String>() {

			@Override
			public String getKey(String item) {
				return item;
			}
		});
		statusStore.add("NEW");
		statusStore.add("READY");
		statusStore.add("ERROR");
		statusStore.add("FINISHED");
		statusStore.add("RUNNING");
		statusStore.add("READY_FOR_REVIEW");
		statusStore.add("READY_FOR_VALIDATION");
		statusStore.add("RESTARTED");
		statusStore.add("TRANSFERRED");
		statusStore.add("DELETED");
		statusStore.add("RESTART_IN_PROGRESS");
		statusStore.add("REMOTE");
		final StringFilter<BatchInstanceDTO> identifierFilter = new StringFilter<BatchInstanceDTO>(BatchInstanceProperties.properties.batchIdentifier());
		final StringFilter<BatchInstanceDTO> batchNameFilter = new StringFilter<BatchInstanceDTO>(BatchInstanceProperties.properties.batchName());
		final StringFilter<BatchInstanceDTO> batchClassNameFilter = new StringFilter<BatchInstanceDTO>(BatchInstanceProperties.properties.batchClassName());
	    final ListFilter<BatchInstanceDTO, String> statusFilter = new ListFilter<BatchInstanceDTO, String>(BatchInstanceProperties.properties.status(),
				statusStore);
	    List<String> batchInstanceStatusSelectedFilter = new ArrayList<String>();
	    
	    //Default Filters.... Includes Non Deleted and non finished batches....
	    batchInstanceStatusSelectedFilter.add("NEW");
	    batchInstanceStatusSelectedFilter.add("READY");
	    batchInstanceStatusSelectedFilter.add("ERROR");
	    batchInstanceStatusSelectedFilter.add("RUNNING");
	    batchInstanceStatusSelectedFilter.add("READY_FOR_REVIEW");
	    batchInstanceStatusSelectedFilter.add("READY_FOR_VALIDATION");
		batchInstanceStatusSelectedFilter.add("RESTARTED");
		batchInstanceStatusSelectedFilter.add("TRANSFERRED");
		batchInstanceStatusSelectedFilter.add("RESTART_IN_PROGRESS");
		batchInstanceStatusSelectedFilter.add("REMOTE");
//		statusFilter.setSelected(batchInstanceStatusSelectedFilter);
//	    statusFilter.setActive(true, false);
	    filtersList.add(identifierFilter);
	    filtersList.add(batchNameFilter);
		filtersList.add(statusFilter);
		filtersList.add(batchClassNameFilter);
		WidgetUtil.setID(statusFilter.getMenu(), "bimStatusMenu");
		return filtersList;
	}
}
