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

import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.client.ui.widget.property.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.ExtractedDlfDTO;
import com.ephesoft.gxt.core.shared.dto.LicenseDetailsDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.ExtractedDlfProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.LicenseDetailsProperties;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

public class ExtractedDlfColumnConfigService implements ColumnConfigService<ExtractedDlfDTO> {

	private List<ColumnConfig<ExtractedDlfDTO, ?>> columnConfigList;
	private Map<ColumnConfig, IsField> editorsMap;
	private RandomIdGenerator customModelKeyProvider = new RandomIdGenerator();

	public ExtractedDlfColumnConfigService() {

		columnConfigList = new ArrayList<ColumnConfig<ExtractedDlfDTO, ?>>();

		ColumnConfig<ExtractedDlfDTO, String> pageID = new ColumnConfig<ExtractedDlfDTO, String>(
				ExtractedDlfProperties.property.pageId());
		pageID.setHeader("Page ID");
		// pageID.setSortable(false);
		// pageID.setMenuDisabled(true);
		// pageID.setFixed(true);

		ColumnConfig<ExtractedDlfDTO, String> pageName = new ColumnConfig<ExtractedDlfDTO, String>(
				ExtractedDlfProperties.property.pageName());
		pageName.setHeader("Page Name");
		// pageName.setSortable(false);
		// pageName.setMenuDisabled(true);
		// pageName.setFixed(true);

		ColumnConfig<ExtractedDlfDTO, Float> confidence = new ColumnConfig<ExtractedDlfDTO, Float>(
				ExtractedDlfProperties.property.confidence());
		confidence.setHeader("Confidence");
		// confidence.setSortable(false);
		// confidence.setMenuDisabled(true);
		// confidence.setFixed(true);

		ColumnConfig<ExtractedDlfDTO, String> extractionType = new ColumnConfig<ExtractedDlfDTO, String>(
				ExtractedDlfProperties.property.extractionType());
		extractionType.setHeader("Extraction Type");
		// extractionType.setSortable(false);
		// extractionType.setMenuDisabled(true);
		// extractionType.setFixed(true);

		ColumnConfig<ExtractedDlfDTO, String> fieldDetails = new ColumnConfig<ExtractedDlfDTO, String>(
				ExtractedDlfProperties.property.fieldDetails());
		fieldDetails.setHeader("Field Details");
		// fieldDetails.setSortable(false);
		// fieldDetails.setMenuDisabled(true);
		// fieldDetails.setFixed(true);
		//
		ColumnConfig<ExtractedDlfDTO, String> valueExtracted = new ColumnConfig<ExtractedDlfDTO, String>(
				ExtractedDlfProperties.property.valueExtracted());
		valueExtracted.setHeader("Value Extracted");
		// valueExtracted.setSortable(false);
		// valueExtracted.setMenuDisabled(true);
		// valueExtracted.setFixed(true);
		//
		columnConfigList.add(pageID);
		columnConfigList.add(pageName);
		columnConfigList.add(valueExtracted);
		columnConfigList.add(fieldDetails);
		columnConfigList.add(extractionType);
		columnConfigList.add(confidence);
	}

	@Override
	public <V> List<ColumnConfig<ExtractedDlfDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	@Override
	public <X extends Comparable> Map<ColumnConfig<ExtractedDlfDTO, X>, IsField<X>> getEditorsMap() {
		return null;
	}

	@Override
	public ModelKeyProvider<ExtractedDlfDTO> getKeyProvider() {
		return customModelKeyProvider;
	}

	@Override
	public List<Filter<ExtractedDlfDTO, ?>> getFilters() {
		return null;
	}

}
