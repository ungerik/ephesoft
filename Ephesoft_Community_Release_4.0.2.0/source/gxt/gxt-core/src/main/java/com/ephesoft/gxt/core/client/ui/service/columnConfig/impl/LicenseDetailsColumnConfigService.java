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
import com.ephesoft.gxt.core.client.ui.widget.property.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.LicenseDetailsDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.LicenseDetailsProperties;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

public class LicenseDetailsColumnConfigService implements ColumnConfigService<LicenseDetailsDTO> {

	private List<ColumnConfig<LicenseDetailsDTO, ?>> columnConfigList;
	private Map<ColumnConfig, IsField> editorsMap;
	@SuppressWarnings("rawtypes")
	private RandomIdGenerator customModelKeyProvider = new RandomIdGenerator();

	public LicenseDetailsColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<LicenseDetailsDTO, ?>>();

		ColumnConfig<LicenseDetailsDTO, String> propertyName = new ColumnConfig<LicenseDetailsDTO, String>(
				LicenseDetailsProperties.property.propertyName());
		propertyName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LICENSE_GRID_PROPERTY_NAME_HEADER));
		propertyName.setHideable(false);

		ColumnConfig<LicenseDetailsDTO, String> propertyValue = new ColumnConfig<LicenseDetailsDTO, String>(
				LicenseDetailsProperties.property.propertyValue());
		propertyValue.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LICENSE_GRID_PROPERTY_VALUE_HEADER));
		propertyValue.setHideable(false);

		columnConfigList.add(propertyName);
		columnConfigList.add(propertyValue);
	}

	@Override
	public <V> List<ColumnConfig<LicenseDetailsDTO, ?>> getColumnConfig() {
		// TODO Auto-generated method stub
		return columnConfigList;
	}

	@Override
	public <X extends Comparable> Map<ColumnConfig<LicenseDetailsDTO, X>, IsField<X>> getEditorsMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelKeyProvider<LicenseDetailsDTO> getKeyProvider() {
		return customModelKeyProvider;
	}

	@Override
	public List<Filter<LicenseDetailsDTO, ?>> getFilters() {
		// TODO Auto-generated method stub
		return null;
	}

}
