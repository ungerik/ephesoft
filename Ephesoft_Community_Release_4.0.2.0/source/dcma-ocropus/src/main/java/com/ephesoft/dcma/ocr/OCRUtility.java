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

package com.ephesoft.dcma.ocr;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.tesseract.TesseractProperties;
import com.ephesoft.dcma.util.IUtilCommonConstants;

public class OCRUtility {

	private static final Logger LOGGER = LoggerFactory.getLogger(OCRUtility.class);

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	@Autowired
	private BatchClassService batchClassService;

	String TESSERACT_HOCR_PLUGIN = "TESSERACT_HOCR";

	String SWITCH_ON = "ON";

	/**
	 * Constant for Nuance HOCR plugin name.
	 */

	/**
	 * API for fetching the OCR plugin configured in the batch class specified with the batch class identifier. If in the specified
	 * batch class from among Tesseract, Recostar and Nuance none is set to ON then this api will return the plugin with presence of
	 * the plugin in this priority: Nuance, Recostar , Tesseract If none of them is configured then this api will return null.
	 * 
	 * @param batchClassIdentifier {@link String} the identifier of batch class.
	 * @return {@link String} the name of the OCR engine.
	 */
	public String getFirstOnOCRPlugin(String batchClassIdentifier) {
		// PluginPropertiesService service = this.getBeanByName("batchClassPluginPropertiesService",
		// BatchClassPluginPropertiesService.class);
		LOGGER.info("Fetching the ocr engine to be used for learning.");
		String OCREngineName = null;
		String firstPriorityPluginName = null;
		String onOCRPlugin = null;
		final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		if (batchClassIdentifier != null && !batchClassIdentifier.isEmpty()) {

			List<BatchClassPlugin> batchClassPlugins = getOrderedListOfPlugins(batchClass);
			if (batchClassPlugins != null) {
				String pluginName = null;
				for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
					if (batchClassPlugin != null) {
						pluginName = batchClassPlugin.getPlugin().getPluginName();
					}
					if (IUtilCommonConstants.TESSERACT_HOCR_PLUGIN.equals(pluginName)) {
						// If there is no other plugin except Tesseract.
						if (null == firstPriorityPluginName) {
							firstPriorityPluginName = pluginName;
						}
						// If the Tesseract switch is ON.
						if (SWITCH_ON.equalsIgnoreCase(getOcrEngineSwitchValue(batchClassIdentifier, pluginPropertiesService,
								TESSERACT_HOCR_PLUGIN, TesseractProperties.TESSERACT_SWITCH))) {
							onOCRPlugin = TESSERACT_HOCR_PLUGIN;
						}
					}
				}
			}
		}
		if (null != onOCRPlugin) {
			OCREngineName = TESSERACT_HOCR_PLUGIN;
		} else {
			OCREngineName = firstPriorityPluginName;
		}
		LOGGER.info("OCR Engine set ON for learning = " + OCREngineName);
		return OCREngineName;
	}

	private List<BatchClassPlugin> getOrderedListOfPlugins(BatchClass batchClass) {
		List<BatchClassPlugin> allBatchClassPlugins = null;

		if (batchClass != null) {
			final List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
			if (batchClassModules != null) {

				if (allBatchClassPlugins == null) {
					allBatchClassPlugins = new ArrayList<BatchClassPlugin>();
				}
				for (BatchClassModule batchClassModule : batchClassModules) {
					List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
					allBatchClassPlugins.addAll(batchClassPlugins);
				}
			}
		}
		return allBatchClassPlugins;
	}

	private String getOcrEngineSwitchValue(String batchClassIdentifier, PluginPropertiesService service, String ocrEngineName,
			PluginProperty ocrEngineSwitch) {
		String ocrEngineSwitchValue = null;
		BatchPlugin plugin = service.getPluginProperties(batchClassIdentifier, ocrEngineName);
		if (plugin != null && plugin.getPropertiesSize() > 0) {
			List<BatchPluginConfiguration> pluginConfigurations = plugin.getPluginConfigurations(ocrEngineSwitch);
			if (pluginConfigurations != null && !pluginConfigurations.isEmpty()) {
				ocrEngineSwitchValue = pluginConfigurations.get(0).getValue();
				LOGGER.info(ocrEngineName + " switch = " + ocrEngineSwitchValue);
			}
		}
		return ocrEngineSwitchValue;
	}

}
