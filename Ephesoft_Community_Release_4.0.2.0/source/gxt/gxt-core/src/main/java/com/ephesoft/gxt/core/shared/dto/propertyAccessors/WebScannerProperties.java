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

package com.ephesoft.gxt.core.shared.dto.propertyAccessors;

import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.shared.dto.WebScannerBooleanValueProvider;
import com.ephesoft.gxt.core.shared.dto.WebScannerConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.WebScannerDoubleValueProvider;
import com.ephesoft.gxt.core.shared.dto.WebScannerIntegerValueProvider;
import com.ephesoft.gxt.core.shared.dto.WebScannerStringValueProvider;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public class WebScannerProperties {

	private static WebScannerProperties scannerProperties;

	private ModelKeyProvider<WebScannerConfigurationDTO> identifier;

	private WebScannerStringValueProvider<WebScannerConfigurationDTO, String> profileValueProvider;

	private ValueProvider<WebScannerConfigurationDTO, Boolean> selected;

	private WebScannerStringValueProvider<WebScannerConfigurationDTO, String> pixelValueProvider;

	private WebScannerIntegerValueProvider<WebScannerConfigurationDTO, Integer> bitDepthValueProvider;

	private WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> multiTransferValueProvider;

	private WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> hideUIValueProvider;

	private WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> selectFeederValueProvider;

	private WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> autoScanValueProvider;

	private WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> enableDuplexValueProvider;

	private WebScannerIntegerValueProvider<WebScannerConfigurationDTO, Integer> pageModeValueProvider;

	private WebScannerDoubleValueProvider<WebScannerConfigurationDTO, Double> pageThresholdValueProvider;

	private WebScannerStringValueProvider<WebScannerConfigurationDTO, String> dpiValueProvider;

	private WebScannerStringValueProvider<WebScannerConfigurationDTO, String> colorValueProvider;

	private WebScannerStringValueProvider<WebScannerConfigurationDTO, String> paperSizeValueProvider;

	private WebScannerStringValueProvider<WebScannerConfigurationDTO, String> backPageRotationValueProvider;

	private WebScannerIntegerValueProvider<WebScannerConfigurationDTO, Integer> cacheCountValueProvider;

	private WebScannerStringValueProvider<WebScannerConfigurationDTO, String> frontPageRotationValueProvider;

	private WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> multifeedValueProvider;

	private WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> enableRescanValueProvider;

	private WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> enableDeleteValueProvider;

	static {
		scannerProperties = new WebScannerProperties();
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private WebScannerProperties() {
		identifier = WebScannerConfigurationProperties.properties.identifier();
		selected = WebScannerConfigurationProperties.properties.selected();
		profileValueProvider = new WebScannerStringValueProvider(CoreCommonConstants.HEADER_PROFILE_NAME,
				String.class);
		pixelValueProvider = new WebScannerStringValueProvider(CoreCommonConstants.HEADER_CURRENT_PIXEL_TYPE,
				String.class);
		bitDepthValueProvider = new WebScannerIntegerValueProvider(CoreCommonConstants.HEADER_BIT_DEPTH,
				Integer.class);
		multiTransferValueProvider = new WebScannerBooleanValueProvider(
				CoreCommonConstants.HEADER_MULTI_TRANSFER, Boolean.class);
		hideUIValueProvider = new WebScannerBooleanValueProvider(CoreCommonConstants.HEADER_HIDE_UI,
				Boolean.class);
		selectFeederValueProvider = new WebScannerBooleanValueProvider(CoreCommonConstants.HEADER_SELECT_FEEDER,
				Boolean.class);
		autoScanValueProvider = new WebScannerBooleanValueProvider(CoreCommonConstants.HEADER_AUTO_SCAN,
				Boolean.class);
		enableDuplexValueProvider = new WebScannerBooleanValueProvider(CoreCommonConstants.HEADER_ENABLE_DUPLEX,
				Boolean.class);
		pageModeValueProvider = new WebScannerIntegerValueProvider(CoreCommonConstants.HEADER_BLANK_PAGE_MODE,
				Integer.class);
		pageThresholdValueProvider = new WebScannerDoubleValueProvider(
				CoreCommonConstants.HEADER_BLANK_PAGE_THRESHOLD, Double.class);
		dpiValueProvider = new WebScannerStringValueProvider(CoreCommonConstants.HEADER_DPI, String.class);
		colorValueProvider = new WebScannerStringValueProvider(CoreCommonConstants.HEADER_COLOR, String.class);
		paperSizeValueProvider = new WebScannerStringValueProvider(CoreCommonConstants.HEADER_PAPER_SIZE,
				String.class);
		backPageRotationValueProvider = new WebScannerStringValueProvider(
				CoreCommonConstants.HEADER_BACK_PAGE_ROTATION_MULTIPLE, String.class);
		cacheCountValueProvider = new WebScannerIntegerValueProvider(
				CoreCommonConstants.HEADER_PAGES_CACHE_CLEAR_COUNT, Integer.class);
		frontPageRotationValueProvider = new WebScannerStringValueProvider(
				CoreCommonConstants.HEADER_FRONT_PAGE_ROTATION_MULTIPLE, String.class);
		multifeedValueProvider = new WebScannerBooleanValueProvider(
				CoreCommonConstants.HEADER_ENABLE_MULTIFEED_ERROR_DETECTION, Boolean.class);
		enableRescanValueProvider = new WebScannerBooleanValueProvider(CoreCommonConstants.HEADER_ENABLE_RESCAN,
				Boolean.class);
		enableDeleteValueProvider = new WebScannerBooleanValueProvider(CoreCommonConstants.HEADER_ENABLE_DELETE,
				Boolean.class);
	}

	public ModelKeyProvider<WebScannerConfigurationDTO> identifier() {
		return identifier;
	}

	public static WebScannerProperties getScannerProperties() {
		return scannerProperties;
	}

	public ValueProvider<WebScannerConfigurationDTO, Boolean> selected() {
		return selected;
	}

	
	public WebScannerStringValueProvider<WebScannerConfigurationDTO, String> getProfileValueProvider() {
		return profileValueProvider;
	}

	
	public WebScannerStringValueProvider<WebScannerConfigurationDTO, String> getPixelValueProvider() {
		return pixelValueProvider;
	}

	
	public WebScannerIntegerValueProvider<WebScannerConfigurationDTO, Integer> getBitDepthValueProvider() {
		return bitDepthValueProvider;
	}

	
	public WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> getMultiTransferValueProvider() {
		return multiTransferValueProvider;
	}

	
	public WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> getHideUIValueProvider() {
		return hideUIValueProvider;
	}

	
	public WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> getSelectFeederValueProvider() {
		return selectFeederValueProvider;
	}

	
	public WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> getAutoScanValueProvider() {
		return autoScanValueProvider;
	}

	
	public WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> getEnableDuplexValueProvider() {
		return enableDuplexValueProvider;
	}

	
	public WebScannerIntegerValueProvider<WebScannerConfigurationDTO, Integer> getPageModeValueProvider() {
		return pageModeValueProvider;
	}

	
	public WebScannerDoubleValueProvider<WebScannerConfigurationDTO, Double> getPageThresholdValueProvider() {
		return pageThresholdValueProvider;
	}

	
	public WebScannerStringValueProvider<WebScannerConfigurationDTO, String> getDpiValueProvider() {
		return dpiValueProvider;
	}

	
	public WebScannerStringValueProvider<WebScannerConfigurationDTO, String> getColorValueProvider() {
		return colorValueProvider;
	}

	
	public WebScannerStringValueProvider<WebScannerConfigurationDTO, String> getPaperSizeValueProvider() {
		return paperSizeValueProvider;
	}

	
	public WebScannerStringValueProvider<WebScannerConfigurationDTO, String> getBackPageRotationValueProvider() {
		return backPageRotationValueProvider;
	}

	
	public WebScannerIntegerValueProvider<WebScannerConfigurationDTO, Integer> getCacheCountValueProvider() {
		return cacheCountValueProvider;
	}

	
	public WebScannerStringValueProvider<WebScannerConfigurationDTO, String> getFrontPageRotationValueProvider() {
		return frontPageRotationValueProvider;
	}

	
	public WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> getMultifeedValueProvider() {
		return multifeedValueProvider;
	}

	
	public WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> getEnableRescanValueProvider() {
		return enableRescanValueProvider;
	}

	
	public WebScannerBooleanValueProvider<WebScannerConfigurationDTO, Boolean> getEnableDeleteValueProvider() {
		return enableDeleteValueProvider;
	}

	interface WebScannerConfigurationProperties extends PropertyAccess<WebScannerConfigurationDTO> {

		public static WebScannerConfigurationProperties properties = GWT.create(WebScannerConfigurationProperties.class);

		ModelKeyProvider<WebScannerConfigurationDTO> identifier();

		ValueProvider<WebScannerConfigurationDTO, String> name();

		ValueProvider<WebScannerConfigurationDTO, String> description();

		ValueProvider<WebScannerConfigurationDTO, String> value();

		ValueProvider<WebScannerConfigurationDTO, String> dataType();

		ValueProvider<WebScannerConfigurationDTO, Boolean> isMultiValue();

		ValueProvider<WebScannerConfigurationDTO, Boolean> isMandatory();

		ValueProvider<WebScannerConfigurationDTO, Boolean> isDeleted();

		ValueProvider<WebScannerConfigurationDTO, Boolean> isNew();

		ValueProvider<WebScannerConfigurationDTO, Boolean> selected();
	}
}
