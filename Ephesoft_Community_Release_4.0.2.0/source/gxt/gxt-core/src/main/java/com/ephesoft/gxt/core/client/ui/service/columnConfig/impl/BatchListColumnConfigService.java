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
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.BatchInstanceProperties;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;

@SuppressWarnings("rawtypes")
public class BatchListColumnConfigService  implements ColumnConfigService<BatchInstanceDTO>{
	private static final BatchInstanceProperties properties = GWT.create(BatchInstanceProperties.class);
	private List<ColumnConfig<BatchInstanceDTO, ?>> columnConfigList;

	public BatchListColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<BatchInstanceDTO, ?>>();
		
		ColumnConfig<BatchInstanceDTO, Integer> priority = new ColumnConfig<BatchInstanceDTO, Integer>(properties.priority());
		priority.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_PRIORITY));
		ColumnConfig<BatchInstanceDTO, String> batchInstanceIdentifier = new ColumnConfig<BatchInstanceDTO, String>(
				properties.batchIdentifier());
		batchInstanceIdentifier.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHID));
		ColumnConfig<BatchInstanceDTO, String> batchClassName = new ColumnConfig<BatchInstanceDTO, String>(properties.batchClassName());
		batchClassName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHCLASSNAME));
		ColumnConfig<BatchInstanceDTO, String> batchInstanceName = new ColumnConfig<BatchInstanceDTO, String>(properties.batchName());
		batchInstanceName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHNAME));
		
		ColumnConfig<BatchInstanceDTO, String> importedOn = new ColumnConfig<BatchInstanceDTO, String>(properties.importedOn());
		importedOn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHIMPORTEDON));
		ColumnConfig<BatchInstanceDTO, String> uploadedOn = new ColumnConfig<BatchInstanceDTO, String>(properties.uploadedOn());
		uploadedOn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHUPDATEDON));
		
		ColumnConfig<BatchInstanceDTO, String> status = new ColumnConfig<BatchInstanceDTO, String>(properties.status());
		status.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_BATCHSTATUS));

		columnConfigList.add(batchInstanceIdentifier);
		columnConfigList.add(batchClassName);
		columnConfigList.add(batchInstanceName);
		columnConfigList.add(priority);
		columnConfigList.add(uploadedOn);
		columnConfigList.add(importedOn);
		columnConfigList.add(status);
	}


	@Override
	public <V> List<ColumnConfig<BatchInstanceDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}


	@Override
	public ModelKeyProvider<BatchInstanceDTO> getKeyProvider() {
		return properties.id();
	}

	@Override
	public List<Filter<BatchInstanceDTO, ?>> getFilters() {
		final List<Filter<BatchInstanceDTO, ?>> filtersList = new ArrayList<Filter<BatchInstanceDTO, ?>>();
		final StringFilter<BatchInstanceDTO> identifierFilter = new StringFilter<BatchInstanceDTO>(properties.batchIdentifier());
		final StringFilter<BatchInstanceDTO> batchNameFilter = new StringFilter<BatchInstanceDTO>(properties.batchName());
	    filtersList.add(identifierFilter);
	    filtersList.add(batchNameFilter);
		return filtersList;
	}


	@Override
	public <X extends Comparable> Map<ColumnConfig<BatchInstanceDTO, X>, IsField<X>> getEditorsMap() {
		return null;
	}
}
