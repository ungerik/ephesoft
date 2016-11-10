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

package com.ephesoft.dcma.da.dao;

import java.util.List;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.core.dao.CacheableDao;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;

/**
 * The Interface BatchClassPluginConfigDao.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Apr 13, 2015 <br/>
 * @version 1.0 <br/>
 *          $LastChangedDate: 2015-04-14 12:40:00 +0530 (Tue, 14 Apr 2015) $ <br/>
 *          $LastChangedRevision: 21592 $ <br/>
 */
public interface BatchClassPluginConfigDao extends CacheableDao<BatchClassPluginConfig> {

	/**
	 * Gets the plugin properties for batch.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @return the plugin properties for batch
	 */
	List<BatchClassPluginConfig> getPluginPropertiesForBatch(String batchInstanceIdentifier, String pluginName);

	/**
	 * Gets the plugin configuration for plugin id.
	 * 
	 * @param pluginId the plugin id
	 * @return the plugin configuration for plugin id
	 */
	List<BatchClassPluginConfig> getPluginConfigurationForPluginId(Long pluginId);

	/**
	 * Gets the plugin properties for batch class.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @return the plugin properties for batch class
	 */
	List<BatchClassPluginConfig> getPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName);

	/**
	 * Update plugin configuration.
	 * 
	 * @param batchClassPluginConfigs {@link List}<{@link BatchClassPluginConfig}>
	 */
	void updatePluginConfiguration(List<BatchClassPluginConfig> batchClassPluginConfigs);

	/**
	 * Update single plugin configuration.
	 * 
	 * @param batchClassPluginConfig {@link BatchClassPluginConfig}
	 */
	void updateSinglePluginConfiguration(BatchClassPluginConfig batchClassPluginConfig);

	/**
	 * Removes the batch class plugin config.
	 * 
	 * @param batchClassPluginConfig {@link BatchClassPluginConfig}
	 */
	void removeBatchClassPluginConfig(BatchClassPluginConfig batchClassPluginConfig);

	/**
	 * Gets the all plugin properties for batch instance.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link List}<{@link BatchClassPluginConfig}>
	 */
	List<BatchClassPluginConfig> getAllPluginPropertiesForBatchInstance(String batchInstanceIdentifier);

	/**
	 * Gets the all plugin properties for batch class.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link List}<{@link BatchClassPluginConfig}>
	 */
	List<BatchClassPluginConfig> getAllPluginPropertiesForBatchClass(String batchClassIdentifier);

	/**
	 * Gets the all plugin properties for batch class by qualifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param qualifier {@link String}
	 * @return {@link List}<{@link BatchClassPluginConfig}>
	 */
	List<BatchClassPluginConfig> getAllPluginPropertiesForBatchClassByQualifier(String batchClassIdentifier, String pluginName,
			String qualifier);

	/**
	 * Gets the plugin properties for batch class.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return {@link List}<{@link BatchClassPluginConfig}>
	 */
	List<BatchClassPluginConfig> getPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName,
			PluginProperty pluginProperty);

	/**
	 * API to get the batch class plugin configuration details by plugin config Id.
	 * 
	 * @param pluginConfigId {@link Long}
	 * @return {@link List}<{@link BatchClassPluginConfig}>
	 */
	List<BatchClassPluginConfig> getBatchClassPluginConfigurationForPluginConfigId(Long pluginConfigId);

	/**
	 * API to get the batch class plugin configuration details by the data type of the config.
	 * 
	 * @param dataType {@link DataType}
	 * @return {@link List}<{@link BatchClassPluginConfig}>
	 */
	List<BatchClassPluginConfig> getBatchClassPluginConfigByDataType(final DataType dataType);
}
