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
import com.ephesoft.gxt.core.shared.dto.FolderUploadedFilesDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.FolderUploadedFilesProperties;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.cell.core.client.ProgressBarCell;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

public class FolderUploadedFilesColumnConfigService implements ColumnConfigService<FolderUploadedFilesDTO> {

	private static final FolderUploadedFilesProperties properties = GWT.create(FolderUploadedFilesProperties.class);
	private List<ColumnConfig<FolderUploadedFilesDTO, ?>> columnConfigList;

	public FolderUploadedFilesColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<FolderUploadedFilesDTO, ?>>();
		ColumnConfig<FolderUploadedFilesDTO, Boolean> modelSelector = new ColumnConfig<FolderUploadedFilesDTO, Boolean>(
				properties.selected());

		// CheckBoxCell modelSelectionCell = new CheckBoxCell();
		// modelSelector.setCell(modelSelectionCell);
		// modelSelector.setHeader("Select");
		// modelSelector.setWidth(30);
		// modelSelector.setFixed(true);
		// modelSelector.setSortable(false);
		// modelSelector.setHideable(false);

		ColumnConfig<FolderUploadedFilesDTO, String> fileName = new ColumnConfig<FolderUploadedFilesDTO, String>(properties.fileName());
		fileName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.FM_NAME_COLUMN_HEADER));

		ColumnConfig<FolderUploadedFilesDTO, Double> progressCell = new ColumnConfig<FolderUploadedFilesDTO, Double>(
				properties.progress());
		ProgressBarCell modelProgressCell = new ProgressBarCell();
		modelProgressCell.setProgressText("{0}%");
		modelProgressCell.setWidth(progressCell.getWidth());
		progressCell.setCell(modelProgressCell);
		progressCell.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.FM_PROGRESS_COLUMN_HEADER));
		progressCell.setSortable(false);

		// columnConfigList.add(modelSelector);
		columnConfigList.add(fileName);
		columnConfigList.add(progressCell);
	}

	@Override
	public <V> List<ColumnConfig<FolderUploadedFilesDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	@Override
	public <X extends Comparable> Map<ColumnConfig<FolderUploadedFilesDTO, X>, IsField<X>> getEditorsMap() {
		return null;
	}

	@Override
	public ModelKeyProvider<FolderUploadedFilesDTO> getKeyProvider() {
		return properties.id();
	}

	@Override
	public List<Filter<FolderUploadedFilesDTO, ?>> getFilters() {
		return null;
	}

}
