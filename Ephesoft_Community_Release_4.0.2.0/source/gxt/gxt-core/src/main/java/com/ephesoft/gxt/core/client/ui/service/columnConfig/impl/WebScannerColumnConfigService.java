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
import com.ephesoft.gxt.core.shared.dto.WebScannerConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.WebScannerProperties;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.DoubleField;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

/**
 * This column config service deals with cmis import grid structure.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.CmisImportColumnConfigService
 */
@SuppressWarnings("rawtypes")
public class WebScannerColumnConfigService implements ColumnConfigService<WebScannerConfigurationDTO> {

	/** The properties. */
	WebScannerProperties properties = WebScannerProperties.getScannerProperties();

	/** The column config list. */
	private final List<ColumnConfig<WebScannerConfigurationDTO, ?>> columnConfigList;

	/** The editors map. */
	private final Map<ColumnConfig, IsField> editorsMap;

	/**
	 * Instantiates a new web scanner column config service.
	 */
	public WebScannerColumnConfigService() {
		columnConfigList = new ArrayList<ColumnConfig<WebScannerConfigurationDTO, ?>>();
		editorsMap = new HashMap<ColumnConfig, IsField>();

		final ColumnConfig<WebScannerConfigurationDTO, Boolean> modelSelector = new ColumnConfig<WebScannerConfigurationDTO, Boolean>(
				properties.selected());
		final CheckBoxCell modelSelectionCell = new CheckBoxCell();
		modelSelector.setCell(modelSelectionCell);
		modelSelector.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_TABLE_COLUMN_SELECT));
		modelSelector.setWidth(40);
		modelSelector.setFixed(true);
		modelSelector.setResizable(false);
		modelSelector.setSortable(false);
		modelSelector.setHideable(false);

		final ColumnConfig<WebScannerConfigurationDTO, String> profileNameColumn = new ColumnConfig<WebScannerConfigurationDTO, String>(
				properties.getProfileValueProvider());
		profileNameColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_PROFILE_NAME));
		profileNameColumn.setFixed(true);
		profileNameColumn.setHideable(false);
		// profileNameColumn.setWidth(40);

		final ColumnConfig<WebScannerConfigurationDTO, String> pixelTypeColumn = new ColumnConfig<WebScannerConfigurationDTO, String>(
				properties.getPixelValueProvider());
		pixelTypeColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_CURRENT_PIXEL_TYPE));
		pixelTypeColumn.setFixed(true);
		pixelTypeColumn.setHideable(false);

		final ColumnConfig<WebScannerConfigurationDTO, Integer> bitDepthColumn = new ColumnConfig<WebScannerConfigurationDTO, Integer>(
				properties.getBitDepthValueProvider());
		bitDepthColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_BIT_DEPTH));
		bitDepthColumn.setFixed(true);
		bitDepthColumn.setHideable(false);

		final ColumnConfig<WebScannerConfigurationDTO, Boolean> multiTransferColumn = new ColumnConfig<WebScannerConfigurationDTO, Boolean>(
				properties.getMultiTransferValueProvider());
		final CheckBoxCell multiTransferCell = new CheckBoxCell();
		multiTransferColumn.setCell(multiTransferCell);
		multiTransferColumn.setFixed(true);
		multiTransferColumn.setSortable(false);
		multiTransferColumn.setResizable(false);
		multiTransferColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_MULTI_TRANSFER));

		final ColumnConfig<WebScannerConfigurationDTO, Boolean> hideUIColumn = new ColumnConfig<WebScannerConfigurationDTO, Boolean>(
				properties.getHideUIValueProvider());
		final CheckBoxCell hideUICell = new CheckBoxCell();
		hideUIColumn.setCell(hideUICell);
		hideUIColumn.setFixed(true);
		hideUIColumn.setSortable(false);
		hideUIColumn.setResizable(false);
		hideUIColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_HIDE_UI));

		final ColumnConfig<WebScannerConfigurationDTO, Boolean> feederColumn = new ColumnConfig<WebScannerConfigurationDTO, Boolean>(
				properties.getSelectFeederValueProvider());
		final CheckBoxCell feederCell = new CheckBoxCell();
		feederColumn.setCell(feederCell);
		feederColumn.setFixed(true);
		feederColumn.setSortable(false);
		feederColumn.setResizable(false);
		feederColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_SELECT_FEEDER));

		final ColumnConfig<WebScannerConfigurationDTO, Boolean> autoScanColumn = new ColumnConfig<WebScannerConfigurationDTO, Boolean>(
				properties.getAutoScanValueProvider());
		final CheckBoxCell autoScanCell = new CheckBoxCell();
		autoScanColumn.setCell(autoScanCell);
		autoScanColumn.setFixed(true);
		autoScanColumn.setSortable(false);
		autoScanColumn.setResizable(false);
		autoScanColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_AUTO_SCAN));

		final ColumnConfig<WebScannerConfigurationDTO, Boolean> duplexColumn = new ColumnConfig<WebScannerConfigurationDTO, Boolean>(
				properties.getEnableDuplexValueProvider());
		final CheckBoxCell duplexCell = new CheckBoxCell();
		duplexColumn.setCell(duplexCell);
		duplexColumn.setFixed(true);
		duplexColumn.setSortable(false);
		duplexColumn.setResizable(false);
		duplexColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_ENABLE_DUPLEX));

		final ColumnConfig<WebScannerConfigurationDTO, Integer> pageModeColumn = new ColumnConfig<WebScannerConfigurationDTO, Integer>(
				properties.getPageModeValueProvider());
		pageModeColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_BALNK_PAGE_MODE));
		pageModeColumn.setFixed(true);
		pageModeColumn.setHideable(false);

		final ColumnConfig<WebScannerConfigurationDTO, Double> thresoldColumn = new ColumnConfig<WebScannerConfigurationDTO, Double>(
				properties.getPageThresholdValueProvider());
		thresoldColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_BALNK_PAGE_THRESHOLD));
		thresoldColumn.setFixed(true);
		thresoldColumn.setWidth(140);
		thresoldColumn.setHideable(false);

		final ColumnConfig<WebScannerConfigurationDTO, String> dpiColumn = new ColumnConfig<WebScannerConfigurationDTO, String>(
				properties.getDpiValueProvider());
		dpiColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_DPI));
		dpiColumn.setFixed(true);
		dpiColumn.setHideable(false);

		final ColumnConfig<WebScannerConfigurationDTO, String> colorColumn = new ColumnConfig<WebScannerConfigurationDTO, String>(
				properties.getColorValueProvider());
		colorColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_COLOR));
		colorColumn.setFixed(true);
		colorColumn.setHideable(false);

		final ColumnConfig<WebScannerConfigurationDTO, String> paperSizeColumn = new ColumnConfig<WebScannerConfigurationDTO, String>(
				properties.getPaperSizeValueProvider());
		paperSizeColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_PAPER_SIZE));
		paperSizeColumn.setFixed(true);
		paperSizeColumn.setHideable(false);

		final ColumnConfig<WebScannerConfigurationDTO, String> backPageRotationColumn = new ColumnConfig<WebScannerConfigurationDTO, String>(
				properties.getBackPageRotationValueProvider());
		backPageRotationColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_BACK_PAGE_ROTATION));
		backPageRotationColumn.setFixed(true);
		backPageRotationColumn.setWidth(140);
		backPageRotationColumn.setHideable(false);

		final ColumnConfig<WebScannerConfigurationDTO, Integer> cacheClearCountColumn = new ColumnConfig<WebScannerConfigurationDTO, Integer>(
				properties.getCacheCountValueProvider());
		cacheClearCountColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_PAGE_CACHE_COUNT));
		cacheClearCountColumn.setFixed(true);
		cacheClearCountColumn.setWidth(140);
		cacheClearCountColumn.setHideable(false);

		final ColumnConfig<WebScannerConfigurationDTO, String> frontPageRotationColumn = new ColumnConfig<WebScannerConfigurationDTO, String>(
				properties.getFrontPageRotationValueProvider());
		frontPageRotationColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_FRONT_PAGE_ROTATION));
		frontPageRotationColumn.setFixed(true);
		frontPageRotationColumn.setWidth(140);
		frontPageRotationColumn.setHideable(false);

		final ColumnConfig<WebScannerConfigurationDTO, Boolean> errorDetectionColumn = new ColumnConfig<WebScannerConfigurationDTO, Boolean>(
				properties.getMultifeedValueProvider());
		final CheckBoxCell errorDetectionCell = new CheckBoxCell();
		errorDetectionColumn.setCell(errorDetectionCell);
		errorDetectionColumn.setFixed(true);
		errorDetectionColumn.setSortable(false);
		errorDetectionColumn.setResizable(false);
		errorDetectionColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_ERROR_DETECTION));
		errorDetectionColumn.setWidth(180);

		final ColumnConfig<WebScannerConfigurationDTO, Boolean> enableRescanColumn = new ColumnConfig<WebScannerConfigurationDTO, Boolean>(
				properties.getEnableRescanValueProvider());
		final CheckBoxCell enableScanCell = new CheckBoxCell();
		enableRescanColumn.setCell(enableScanCell);
		enableRescanColumn.setFixed(true);
		enableRescanColumn.setSortable(false);
		enableRescanColumn.setResizable(false);
		enableRescanColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_ENABLE_RESCAN));

		final ColumnConfig<WebScannerConfigurationDTO, Boolean> enableDeleteColumn = new ColumnConfig<WebScannerConfigurationDTO, Boolean>(
				properties.getEnableDeleteValueProvider());
		final CheckBoxCell enableDeleteCell = new CheckBoxCell();
		enableDeleteColumn.setCell(enableDeleteCell);
		enableDeleteColumn.setFixed(true);
		enableDeleteColumn.setSortable(false);
		enableDeleteColumn.setResizable(false);
		enableDeleteColumn.setHeader(LocaleDictionary.getConstantValue(CoreCommonConstants.LABEL_COLUMN_ENABLE_DELETE));

		columnConfigList.add(modelSelector);
		columnConfigList.add(profileNameColumn);
		columnConfigList.add(pixelTypeColumn);
		columnConfigList.add(bitDepthColumn);
		columnConfigList.add(multiTransferColumn);
		columnConfigList.add(hideUIColumn);
		columnConfigList.add(feederColumn);
		columnConfigList.add(autoScanColumn);
		columnConfigList.add(duplexColumn);
		columnConfigList.add(pageModeColumn);
		columnConfigList.add(thresoldColumn);
		columnConfigList.add(dpiColumn);
		columnConfigList.add(colorColumn);
		columnConfigList.add(paperSizeColumn);
		columnConfigList.add(backPageRotationColumn);
		columnConfigList.add(cacheClearCountColumn);
		columnConfigList.add(frontPageRotationColumn);
		columnConfigList.add(errorDetectionColumn);
		columnConfigList.add(enableRescanColumn);
		columnConfigList.add(enableDeleteColumn);

		IntegerField bitDepth = new IntegerField();
		bitDepth.setAllowNegative(false);
		IntegerField pageMode = new IntegerField();
		pageMode.setAllowNegative(false);
		DoubleField pageThresold = new DoubleField();
		pageThresold.setAllowNegative(false);
		IntegerField cacheClearCount = new IntegerField();
		cacheClearCount.setAllowNegative(false);
		editorsMap.put(profileNameColumn, new TextField());
		editorsMap.put(bitDepthColumn, bitDepth);
		editorsMap.put(pageModeColumn, pageMode);
		editorsMap.put(thresoldColumn, pageThresold);
		editorsMap.put(cacheClearCountColumn, cacheClearCount);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getColumnConfig()
	 */
	@Override
	public <V> List<ColumnConfig<WebScannerConfigurationDTO, ?>> getColumnConfig() {
		return columnConfigList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getEditorsMap()
	 */
	@SuppressWarnings({"unchecked"})
	@Override
	public Map<ColumnConfig, IsField> getEditorsMap() {
		return editorsMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getKeyProvider()
	 */
	@Override
	public ModelKeyProvider<WebScannerConfigurationDTO> getKeyProvider() {
		return properties.identifier();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService#getFilters()
	 */
	@Override
	public List<Filter<WebScannerConfigurationDTO, ?>> getFilters() {
		return null;
	}

}
