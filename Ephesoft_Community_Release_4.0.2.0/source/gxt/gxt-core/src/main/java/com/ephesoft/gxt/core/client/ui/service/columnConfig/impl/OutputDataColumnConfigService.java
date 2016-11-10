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
import com.ephesoft.gxt.core.shared.dto.CoordinatesDTO;
import com.ephesoft.gxt.core.shared.dto.OutputDataCarrierDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.OutputDataCarrierProperties;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.cell.core.client.ProgressBarCell;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

@SuppressWarnings("rawtypes")
public class OutputDataColumnConfigService implements ColumnConfigService<OutputDataCarrierDTO> {

	private static final OutputDataCarrierProperties properties = GWT.create(OutputDataCarrierProperties.class);
	private List<ColumnConfig<OutputDataCarrierDTO, ?>> columnConfigList;

	public OutputDataColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<OutputDataCarrierDTO, ?>>();
/*		ColumnConfig<OutputDataCarrierDTO, Boolean> modelSelector = new ColumnConfig<OutputDataCarrierDTO, Boolean>(properties.selected());
		CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setHeader("SELECT");
		modelSelector.setWidth(20);
		modelSelector.setFixed(true);
		modelSelector.setSortable(false);*/
		
		ColumnConfig<OutputDataCarrierDTO, String> key = new ColumnConfig<OutputDataCarrierDTO, String>(
				properties.key());
		key.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_KEY));
		
/*		ColumnConfig<OutputDataCarrierDTO, Double> progressCell = new ColumnConfig<OutputDataCarrierDTO, Double>(
				properties.progress());
		ProgressBarCell modelProgressCell = new ProgressBarCell();
		modelProgressCell.setProgressText("{0}%");
		modelProgressCell.setWidth(progressCell.getWidth());
		
		progressCell.setCell(modelProgressCell);
		progressCell.setHeader("PROGRESS");
		progressCell.setSortable(false);*/
		
		ColumnConfig<OutputDataCarrierDTO, String> value = new ColumnConfig<OutputDataCarrierDTO, String>(properties.value());
		value.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.COLUMN_CONFIG_VALUE));
		ColumnConfig<OutputDataCarrierDTO, Float> confidence = new ColumnConfig<OutputDataCarrierDTO, Float>(properties.confidence());
		confidence.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.ADVANCE_KV_CONFIDENCE));
		ColumnConfig<OutputDataCarrierDTO, CoordinatesDTO> keyCoordinates = new ColumnConfig<OutputDataCarrierDTO, CoordinatesDTO>(properties.keyCoordinates());
		keyCoordinates.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.ADVANCE_KV_KEY_COORDINATES));
		ColumnConfig<OutputDataCarrierDTO, CoordinatesDTO> coordinates = new ColumnConfig<OutputDataCarrierDTO, CoordinatesDTO>(properties.coordinates());
		coordinates.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.ADVANCE_KV_VALUE_COORDINATES));
		ColumnConfig<OutputDataCarrierDTO, String> colorCode = new ColumnConfig<OutputDataCarrierDTO, String>((properties.colorCode()));
		colorCode.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.ADVANCE_KV_COLORCODE));
		columnConfigList.add(key);
		columnConfigList.add(value);
		columnConfigList.add(confidence);
		columnConfigList.add(keyCoordinates);
		columnConfigList.add(coordinates);
		columnConfigList.add(colorCode);
	}

	@Override
	public <V> List<ColumnConfig<OutputDataCarrierDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public Map<ColumnConfig, IsField> getEditorsMap() {
		return null;
	}

	@Override
	public ModelKeyProvider<OutputDataCarrierDTO> getKeyProvider() {
		return properties.identifier();
	}

	@Override
	public List<Filter<OutputDataCarrierDTO, ?>> getFilters() {
		return null;
	}
}
