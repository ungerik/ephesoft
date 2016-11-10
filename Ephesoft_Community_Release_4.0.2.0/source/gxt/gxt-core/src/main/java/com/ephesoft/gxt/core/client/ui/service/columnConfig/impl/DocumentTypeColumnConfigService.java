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
import com.ephesoft.gxt.core.client.ui.widget.FloatField;
import com.ephesoft.gxt.core.client.ui.widget.util.UniqueIDProvider;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.DocumentTypeProperties;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

public class DocumentTypeColumnConfigService implements ColumnConfigService<DocumentTypeDTO> {

	DocumentTypeProperties properties = DocumentTypeProperties.INSTANCE;

	private List<ColumnConfig<DocumentTypeDTO, ?>> columnConfigList;
	private Map<ColumnConfig, IsField> editorsMap;

	public DocumentTypeColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<DocumentTypeDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();
		ColumnConfig<DocumentTypeDTO, Boolean> modelSelector = new ColumnConfig<DocumentTypeDTO, Boolean>(properties.selected());
		CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setHeader(".");
		modelSelector.setWidth(30);
		modelSelector.setFixed(true);
		modelSelector.setSortable(false);
		modelSelector.setHideable(false);
		
		ColumnConfig<DocumentTypeDTO, String> nameColumn = new ColumnConfig<DocumentTypeDTO, String>(properties.name());
		nameColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_NAME));
		nameColumn.setHideable(false);


		ColumnConfig<DocumentTypeDTO, String> descriptionColumn = new ColumnConfig<DocumentTypeDTO, String>(properties.description());
		descriptionColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_DESCRIPTION));
		descriptionColumn.setHideable(false);

		ColumnConfig<DocumentTypeDTO, Boolean> hiddenValueColumn = new ColumnConfig<DocumentTypeDTO, Boolean>(properties.hidden());
		hiddenValueColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_HIDDEN));
		hiddenValueColumn.setCell(new CheckBoxCell());
		hiddenValueColumn.setSortable(false);

		ColumnConfig<DocumentTypeDTO, Float> minimumConfidenceThreshold = new ColumnConfig<DocumentTypeDTO, Float>(
				properties.minConfidenceThreshold());
		minimumConfidenceThreshold.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.DT_MINIMUM_CONFIDENCE_THRESHOLD));
		minimumConfidenceThreshold.setHideable(false);


		ColumnConfig<DocumentTypeDTO, String> firstPageColumn = new ColumnConfig<DocumentTypeDTO, String>(
				properties.firstPageProjectFileName());
		firstPageColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.DT_FIRST_PAGE));

		ColumnConfig<DocumentTypeDTO, String> secondPageColumn = new ColumnConfig<DocumentTypeDTO, String>(
				properties.secondPageProjectFileName());
		secondPageColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.DT_SECOND_PAGE));

		ColumnConfig<DocumentTypeDTO, String> thirdPageColumn = new ColumnConfig<DocumentTypeDTO, String>(
				properties.thirdPageProjectFileName());
		thirdPageColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.DT_THIRD_PAGE));

		ColumnConfig<DocumentTypeDTO, String> fourthPageColumn = new ColumnConfig<DocumentTypeDTO, String>(
				properties.fourthPageProjectFileName());
		fourthPageColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.DT_LASTGREATERTHAN3_PAGE));

		columnConfigList.add(modelSelector);
		columnConfigList.add(nameColumn);
		columnConfigList.add(descriptionColumn);
		// columnConfigList.add(hiddenValueColumn);
		columnConfigList.add(minimumConfidenceThreshold);
		columnConfigList.add(firstPageColumn);
		columnConfigList.add(secondPageColumn);
		columnConfigList.add(thirdPageColumn);
		columnConfigList.add(fourthPageColumn);
		LabelProvider<String> comboLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(String item) {
				return item;
			}
		};

		final SimpleComboBox<String> rspFirstPageComboBox = new SimpleComboBox<String>(comboLabelProvider);
		rspFirstPageComboBox.setTriggerAction(TriggerAction.ALL);
		addItemToRspComboBox(rspFirstPageComboBox);
		final SimpleComboBox<String> rspSecondPageComboBox = new SimpleComboBox<String>(comboLabelProvider);
		rspSecondPageComboBox.setTriggerAction(TriggerAction.ALL);
		addItemToRspComboBox(rspSecondPageComboBox);
		final SimpleComboBox<String> rspThirdPageComboBox = new SimpleComboBox<String>(comboLabelProvider);
		rspThirdPageComboBox.setTriggerAction(TriggerAction.ALL);
		addItemToRspComboBox(rspThirdPageComboBox);
		final SimpleComboBox<String> rspFourthPageComboBox = new SimpleComboBox<String>(comboLabelProvider);
		rspFourthPageComboBox.setTriggerAction(TriggerAction.ALL);
		addItemToRspComboBox(rspFourthPageComboBox);
		editorsMap.put(nameColumn, new TextField());
		editorsMap.put(descriptionColumn, new TextField());
		editorsMap.put(minimumConfidenceThreshold,  new FloatField());
		editorsMap.put(firstPageColumn, rspFirstPageComboBox);
		editorsMap.put(secondPageColumn, rspSecondPageComboBox);
		editorsMap.put(thirdPageColumn, rspThirdPageComboBox);
		editorsMap.put(fourthPageColumn, rspFourthPageComboBox);
	}

	@Override
	public <V> List<ColumnConfig<DocumentTypeDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	@Override
	public Map<ColumnConfig, IsField> getEditorsMap() {
		return editorsMap;
	}

	@Override
	public ModelKeyProvider<DocumentTypeDTO> getKeyProvider() {
		return new UniqueIDProvider<DocumentTypeDTO>();
	}

	@Override
	public List<Filter<DocumentTypeDTO, ?>> getFilters() {
		return null;
	}

	private SimpleComboBox<String> addItemToRspComboBox(SimpleComboBox<String> rspFirstPageComboBox) {
		rspFirstPageComboBox.add("FPR.rsp");
		rspFirstPageComboBox.add("FPR_Barcode.rsp");
		rspFirstPageComboBox.add("FPR_MultiLanguage.rsp");
		rspFirstPageComboBox.add("FPR_Pdf.rsp");
		return rspFirstPageComboBox;
	}

}
