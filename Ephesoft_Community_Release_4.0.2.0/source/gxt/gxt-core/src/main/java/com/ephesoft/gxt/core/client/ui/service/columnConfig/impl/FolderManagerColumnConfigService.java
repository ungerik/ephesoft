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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.shared.dto.FileType;
import com.ephesoft.gxt.core.shared.dto.FolderManagerDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.FolderManagerProperties;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

@SuppressWarnings("rawtypes")
public class FolderManagerColumnConfigService implements ColumnConfigService<FolderManagerDTO> {

	private List<ColumnConfig<FolderManagerDTO, ?>> columnConfigList;
	private Map<ColumnConfig, IsField> editorsMap;
	final GridIcons imageResources = GWT.create(GridIcons.class);

	public FolderManagerColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<FolderManagerDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();
		ColumnConfig<FolderManagerDTO, Boolean> modelSelector = new ColumnConfig<FolderManagerDTO, Boolean>(
				FolderManagerProperties.property.selected());

		CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);

		modelSelector.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.FM_SELECT_COLUMN_HEADER));
		modelSelector.setWidth(30);
		modelSelector.setFixed(true);
		modelSelector.setSortable(false);
		modelSelector.setHideable(false);

		ColumnConfig<FolderManagerDTO, String> fileName = new ColumnConfig<FolderManagerDTO, String>(
				FolderManagerProperties.property.fileName());
		fileName.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.FM_NAME_COLUMN_HEADER));
		fileName.setHideable(false);

		ColumnConfig<FolderManagerDTO, Float> fileSize = new ColumnConfig<FolderManagerDTO, Float>(
				FolderManagerProperties.property.size());
		fileSize.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.FM_SIZE_COLUMN_HEADER));

		ColumnConfig<FolderManagerDTO, FileType> fileType = new ColumnConfig<FolderManagerDTO, FileType>(
				FolderManagerProperties.property.kind());

		fileType.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.FM_FILE_TYPE_COLUMN_HEADER));
		ColumnConfig<FolderManagerDTO, Date> modifiedAt = new ColumnConfig<FolderManagerDTO, Date>(
				FolderManagerProperties.property.modifiedAt());
		modifiedAt.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.FM_MODIFIED_COLUMN_HEADER));
		modifiedAt.setCell(new DateCell(DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_MEDIUM)));

		@SuppressWarnings("unchecked")
		ColumnConfig<FolderManagerDTO, ImageResource> fileIcon = new ColumnConfig<FolderManagerDTO, ImageResource>(
				new ValueProvider() {

					@Override
					public ImageResource getValue(Object object) {
						ImageResource image = null;
						FolderManagerDTO dto = (FolderManagerDTO) object;
						if (dto.getKind() == FileType.DIR) {
							image = imageResources.icon_folder();
						} else if (dto.getKind() == FileType.DOC) {
							image = imageResources.icon_doc();
						} else if (dto.getKind() == FileType.MM) {
							image = imageResources.icon_mm();
						} else if (dto.getKind() == FileType.IMG) {
							image = imageResources.icon_img();
						} else if (dto.getKind() == FileType.OTHER) {
							image = imageResources.icon_other();
						}
						return image;
					}

					@Override
					public void setValue(Object object, Object value) {

					}

					@Override
					public String getPath() {
						return null;
					}

				});
		fileIcon.setSortable(false);
		fileIcon.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.FM_ICON_COLUMN_HEADER));
		ImageResourceCell imageCell = new ImageResourceCell() {

			@Override
			public void render(com.google.gwt.cell.client.Cell.Context context, ImageResource value, SafeHtmlBuilder sb) {
				super.render(context, value, sb);

			}
		};
		fileIcon.setCell(imageCell);
		columnConfigList.add(modelSelector);
		columnConfigList.add(fileIcon);
		columnConfigList.add(fileName);
		columnConfigList.add(modifiedAt);
		columnConfigList.add(fileType);
		columnConfigList.add(fileSize);
		editorsMap.put(fileName, new TextField());
	}

	@Override
	public <V> List<ColumnConfig<FolderManagerDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	@SuppressWarnings({"unchecked"})
	@Override
	public Map<ColumnConfig, IsField> getEditorsMap() {
		return editorsMap;
	}

	@Override
	public ModelKeyProvider<FolderManagerDTO> getKeyProvider() {
		return FolderManagerProperties.property.id();
	}

	@Override
	public List<Filter<FolderManagerDTO, ?>> getFilters() {
		return null;
	}

	public interface GridIcons extends ClientBundleWithLookup {

		ImageResource icon_folder();

		ImageResource icon_doc();

		ImageResource icon_img();

		ImageResource icon_mm();

		ImageResource icon_other();
	}
}
