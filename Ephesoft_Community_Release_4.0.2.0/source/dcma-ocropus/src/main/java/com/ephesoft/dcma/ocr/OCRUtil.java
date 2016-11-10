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
import org.springframework.stereotype.Service;

import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.tesseract.TesseractProperties;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.IUtilCommonConstants;

/**
 * Defines utility methods for fetching information related to OCR plugins.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 23-Apr-2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Service
public class OCRUtil implements ICommonConstants {

	private static final Logger LOGGER = LoggerFactory.getLogger(OCRUtil.class);

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	@Autowired
	private BatchClassService batchClassService;

	/**
	 * Constant for string " switch ".
	 */
	private static final String SWITCH = " switch = ";

	/**
	 * API for fetching the OCR plugin configured in the batch class specified with the batch class identifier. If in the specified
	 * batch class from among Tesseract none is set to ON then this api will return the plugin. If none of them is on then this api
	 * will return the first occuring ocr plugin.
	 * 
	 * @param batchClassIdentifier the identifier of batch class.
	 * @return the name of the OCR engine.
	 */
	public String getFirstOnOCRPlugin(final String batchClassIdentifier) {
		LOGGER.info("Fetching the ocr engine to be used for learning...");
		String firstPriorityPluginName = null;
		String firstOccuringPlugin = null;
		final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		if (batchClassIdentifier != null && !batchClassIdentifier.isEmpty()) {

			List<BatchClassPlugin> batchClassPlugins = getOrderedListOfPlugins(batchClass);
			if (batchClassPlugins != null) {
				String pluginName = null;
				for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
					if (batchClassPlugin != null && null != batchClassPlugin.getPlugin()) {
						pluginName = batchClassPlugin.getPlugin().getPluginName();
					}
					if (IUtilCommonConstants.TESSERACT_HOCR_PLUGIN.equals(pluginName)) {

						// Save the first occuring ocr plugin name.
						if (EphesoftStringUtil.isNullOrEmpty(firstOccuringPlugin)) {
							firstOccuringPlugin = pluginName;
						}

						// If the Tesseract switch is ON.
						if (ON.equalsIgnoreCase(getOcrEngineSwitchValue(batchClassIdentifier, pluginPropertiesService,
								TESSERACT_HOCR_PLUGIN, TesseractProperties.TESSERACT_SWITCH))) {
							if (EphesoftStringUtil.isNullOrEmpty(firstPriorityPluginName)) {
								firstPriorityPluginName = pluginName;
							}
						}
					}
				}
			}
		}
		if (EphesoftStringUtil.isNullOrEmpty(firstOccuringPlugin) || EphesoftStringUtil.isNullOrEmpty(firstPriorityPluginName)) {
			firstOccuringPlugin = IUtilCommonConstants.TESSERACT_HOCR_PLUGIN;
			firstPriorityPluginName = firstOccuringPlugin;
			LOGGER.error(EphesoftStringUtil.concatenate("No HOCR plugin is present in workflow so setting HOCR plugin to ",
					firstOccuringPlugin, " for Batch class identifier : ", batchClassIdentifier));
		}

		LOGGER.info(EphesoftStringUtil.concatenate("OCR Engine set ON for learning = ", firstPriorityPluginName));
		return firstPriorityPluginName;
	}

	/**
	 * Returns ordered list of plugins for a batch class.
	 * 
	 * @param batchClass{{@link BatchClass} the batch class instance whose plugin listing is required
	 * @return An ordered list of plugins configured in the batch class
	 */
	private List<BatchClassPlugin> getOrderedListOfPlugins(final BatchClass batchClass) {
		List<BatchClassPlugin> allBatchClassPlugins = null;
		LOGGER.info("Entering get ocr ordered list of plugins....");
		if (batchClass != null) {
			final List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
			if (!CollectionUtil.isEmpty(batchClassModules)) {
				if (allBatchClassPlugins == null) {
					allBatchClassPlugins = new ArrayList<BatchClassPlugin>();
				}
				for (BatchClassModule batchClassModule : batchClassModules) {
					List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
					allBatchClassPlugins.addAll(batchClassPlugins);
				}
			}
		}
		LOGGER.info("Exiting get ocr ordered list of plugins....");
		return allBatchClassPlugins;
	}

	/**
	 * Returns the Ocr Engine Switch Value of a given ocr engine, the value is on or off.
	 * 
	 * @param batchClassIdentifier the batch class identifier
	 * @param service{{@link PluginPropertiesService} an instance of plugin properties
	 * @param ocrEngineName the plugin whose switch value is required
	 * @param ocrEngineSwitch the ocr engine switch name
	 * @return the ocr engine switch value
	 */
	private String getOcrEngineSwitchValue(final String batchClassIdentifier, final PluginPropertiesService service,
			final String ocrEngineName, final PluginProperty ocrEngineSwitch) {
		LOGGER.info("Entering get ocr confidence switch value....");
		String ocrEngineSwitchValue = null;
		BatchPlugin plugin = service.getPluginProperties(batchClassIdentifier, ocrEngineName);
		if (plugin != null && plugin.getPropertiesSize() > 0) {
			List<BatchPluginConfiguration> pluginConfigurations = plugin.getPluginConfigurations(ocrEngineSwitch);
			if (pluginConfigurations != null && !pluginConfigurations.isEmpty()) {
				ocrEngineSwitchValue = pluginConfigurations.get(0).getValue();
				LOGGER.info(EphesoftStringUtil.concatenate(ocrEngineName, SWITCH, ocrEngineSwitchValue));
			}
		}
		LOGGER.info("Exiting get ocr confidence switch value....");
		return ocrEngineSwitchValue;
	}

}
