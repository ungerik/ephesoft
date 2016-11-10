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

package com.ephesoft.gxt.batchinstance.client.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.ephesoft.dcma.core.common.WorkflowDetail;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.gxt.core.shared.dto.WorkflowDetailDTO;

public final class BatchInstanceProgressConvertor {

	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(BatchInstanceProgressConvertor.class);

	private BatchInstanceProgressConvertor() {
		// Do nothing.
	}

	public static final WorkflowDetailDTO getWorkflowDetailDTO(final WorkflowDetail workflowDetail,
			final String batchInstanceIdentifier) {
		WorkflowDetailDTO workflowDetailDTO = new WorkflowDetailDTO();
		if (null != workflowDetail) {
			workflowDetailDTO.setCurrentExecutingModule(workflowDetail.getCurrentExecutingModule());
			workflowDetailDTO.setCurrentExecutingPlugin(workflowDetail.getCurrentExecutingPlugin());
			List<String> executedModules = workflowDetail.getExecutedModuleList();
			if (CollectionUtils.isNotEmpty(executedModules)) {
				List<String> executedModuleDTOList = new ArrayList<String>(executedModules.size());
				executedModuleDTOList.addAll(executedModules);
				workflowDetailDTO.setExecutedModuleList(executedModuleDTOList);
				LOGGER.debug("Executed modules are set for batch instance: ", batchInstanceIdentifier);
			}
			List<String> unexecutedModules = workflowDetail.getUnexecutedModuleList();
			if (CollectionUtils.isNotEmpty(unexecutedModules)) {
				List<String> unexecutedModuleDTOList = new ArrayList<String>(unexecutedModules.size());
				unexecutedModuleDTOList.addAll(unexecutedModules);
				workflowDetailDTO.setNonExecutedModuleList(unexecutedModuleDTOList);
				LOGGER.debug("Unexecuted modules are set for batch instance: ", batchInstanceIdentifier);
			}
			List<String> executedPlugins = workflowDetail.getExecutedPluginList();
			if (CollectionUtils.isNotEmpty(executedPlugins)) {
				List<String> executedPluginDTOList = new ArrayList<String>(executedPlugins.size());
				executedPluginDTOList.addAll(executedPlugins);
				workflowDetailDTO.setExecutedPluginList(executedPluginDTOList);
				LOGGER.debug("Executed plugins are set for batch instance: ", batchInstanceIdentifier);
			}
			List<String> unexecutedPlugins = workflowDetail.getUnexecutedPluginList();
			if (CollectionUtils.isNotEmpty(unexecutedPlugins)) {
				List<String> unexecutedPluginDTOList = new ArrayList<String>(unexecutedPlugins.size());
				unexecutedPluginDTOList.addAll(unexecutedPlugins);
				workflowDetailDTO.setNonExecutedPluginList(unexecutedPluginDTOList);
				LOGGER.debug("Unexecuted plugins are set for batch instance: ", batchInstanceIdentifier);
			}
			Map<String, List<String>> modulePluginMap = workflowDetail.getModulePluginMap();
			if (null != modulePluginMap) {
				Map<String, List<String>> modulePluginMapDTO = new HashMap<String, List<String>>(modulePluginMap.size());
				modulePluginMapDTO.putAll(modulePluginMap);
				workflowDetailDTO.setModulePluginMap(modulePluginMapDTO);
				LOGGER.debug("Module-plugin map is set for batch instance: ", batchInstanceIdentifier);
			}
		}
		return workflowDetailDTO;
	}
}
