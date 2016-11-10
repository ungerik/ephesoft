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

package com.ephesoft.dcma.da.service;

import java.util.Collection;
import java.util.List;

import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;

/**
 * This service deals with Batch Class Email Configurations.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.BatchClassEmailConfigServiceImpl
 */
public interface BatchClassDynamicPluginConfigService {

	/**
	 * API to get Dynamic Plugin Properties For the given Batch Class and a particular plugin name.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @return {@link List}<{@link BatchClassDynamicPluginConfig}>
	 */
	List<BatchClassDynamicPluginConfig> getDynamicPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName);

	/**
	 * API to get dynamic plugin config for the given batch instance identifier.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link List}<{@link BatchClassDynamicPluginConfig}>
	 */
	List<BatchClassDynamicPluginConfig> getAllDynamicPluginPropertiesForBatchInstance(String batchInstanceIdentifier);

	/**
	 * API to get dynamic plugin config for the given batch class identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @return {@link List}<{@link BatchClassDynamicPluginConfig}>
	 */
	List<BatchClassDynamicPluginConfig> getAllDynamicPluginPropertiesForBatchClass(String batchClassIdentifier);

	/**
	 * API to get dynamic plugin config for a plugin of a given batch class satisfying the plugin property value.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param pluginName {@link String}
	 * @param pluginProperty {@link PluginProperty}
	 * @return {@link List}<{@link BatchClassDynamicPluginConfig}>
	 */
	List<BatchClassDynamicPluginConfig> getDynamicPluginPropertiesForBatchClass(String batchClassIdentifier, String pluginName,
			PluginProperty pluginProperty);

	/**
	 * API to get all dynamic plugin config.
	 * 
	 * @return {@link List}<{@link BatchClassDynamicPluginConfig}>
	 */
	List<BatchClassDynamicPluginConfig> getAllDynamicPluginProperties();

	/**
	 * API to remove the list of dynamic plugin configs.
	 * 
	 * @param batchClassDynamicPluginConfigs {@link List}<{@link BatchClassDynamicPluginConfig}>
	 */
	void removeAllDynamicConfigs(Collection<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs);

}
