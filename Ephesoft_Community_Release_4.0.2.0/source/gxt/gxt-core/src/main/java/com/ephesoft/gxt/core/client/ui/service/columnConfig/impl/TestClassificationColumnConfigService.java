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
import com.ephesoft.gxt.core.shared.dto.TestClassificationDataCarrierDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.TestClassificationProperties;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

public class TestClassificationColumnConfigService implements ColumnConfigService<TestClassificationDataCarrierDTO> {

	private List<ColumnConfig<TestClassificationDataCarrierDTO, ?>> columnConfigList;
	private Map<ColumnConfig, IsField> editorsMap;
	@SuppressWarnings("rawtypes")
	private RandomIdGenerator customModelKeyProvider = new RandomIdGenerator();

	public TestClassificationColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<TestClassificationDataCarrierDTO, ?>>();

		ColumnConfig<TestClassificationDataCarrierDTO, String> classificationType = new ColumnConfig<TestClassificationDataCarrierDTO, String>(
				TestClassificationProperties.property.classificationType());
		classificationType.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.CLASSIFICATION_TYPE));

		ColumnConfig<TestClassificationDataCarrierDTO, String> docType = new ColumnConfig<TestClassificationDataCarrierDTO, String>(
				TestClassificationProperties.property.docType());
		docType.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.DOCUMENT_TYPE));

		ColumnConfig<TestClassificationDataCarrierDTO, String> docIdentifier = new ColumnConfig<TestClassificationDataCarrierDTO, String>(
				TestClassificationProperties.property.docIdentifier());
		docIdentifier.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.DOCUMENT_ID));

		ColumnConfig<TestClassificationDataCarrierDTO, Float> docConfidence = new ColumnConfig<TestClassificationDataCarrierDTO, Float>(
				TestClassificationProperties.property.documentConfidence());
		docConfidence.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.DOCUMENT_CONFIDENCE));
		
		ColumnConfig<TestClassificationDataCarrierDTO, String> pageName = new ColumnConfig<TestClassificationDataCarrierDTO, String>(
				TestClassificationProperties.property.pageName());
		pageName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.PAGE_NAME));

		ColumnConfig<TestClassificationDataCarrierDTO, String> pageID = new ColumnConfig<TestClassificationDataCarrierDTO, String>(
				TestClassificationProperties.property.pageID());
		pageID.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.PAGE_ID));

		ColumnConfig<TestClassificationDataCarrierDTO, String> pageClassificationValue = new ColumnConfig<TestClassificationDataCarrierDTO, String>(
				TestClassificationProperties.property.pageClassificationValue());
		pageClassificationValue.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.PAGE_CLASSIFICATION_VALUE));

		ColumnConfig<TestClassificationDataCarrierDTO, Float> pageConfidence = new ColumnConfig<TestClassificationDataCarrierDTO, Float>(
				TestClassificationProperties.property.pageConfidence());
		pageConfidence.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.PAGE_CONFIDENCE));

		ColumnConfig<TestClassificationDataCarrierDTO, String> learnedFileName = new ColumnConfig<TestClassificationDataCarrierDTO, String>(
				TestClassificationProperties.property.learnedFileName());
		learnedFileName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.CLASSIFICATION_SAMPLE));

		columnConfigList.add(classificationType);
		columnConfigList.add(docType);
		columnConfigList.add(docIdentifier);
		columnConfigList.add(docConfidence);
		columnConfigList.add(pageName);
		columnConfigList.add(pageID);
		columnConfigList.add(pageClassificationValue);
		columnConfigList.add(pageConfidence);
		columnConfigList.add(learnedFileName);
	}

	@Override
	public <V> List<ColumnConfig<TestClassificationDataCarrierDTO, ?>> getColumnConfig() {
		// TODO Auto-generated method stub
		return columnConfigList;
	}

	@Override
	public <X extends Comparable> Map<ColumnConfig<TestClassificationDataCarrierDTO, X>, IsField<X>> getEditorsMap() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelKeyProvider<TestClassificationDataCarrierDTO> getKeyProvider() {
		// TODO Auto-generated method stub
		return customModelKeyProvider;
	}

	@Override
	public List<Filter<TestClassificationDataCarrierDTO, ?>> getFilters() {
		// TODO Auto-generated method stub
		return null;
	}

}
