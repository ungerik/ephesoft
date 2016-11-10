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

package com.ephesoft.gxt.core.shared.dto;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Class acting as transfer object to transfer data service to UI and vice-versa.
 * 
 * <p>
 * Data related to work flow details for a particular batch instance would be transferred.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
public class WorkflowDetailDTO implements IsSerializable {

	// /**
	// * Contains the batch workflow start time.
	// */
	// private Date workflowStartTime;

	/**
	 * Contains the last plugin executed for given batch.
	 */
	private String currentExecutingPlugin;

	/**
	 * Contains the current module for given batch.
	 */
	private String currentExecutingModule;

	// /**
	// * Contains per module last updated time of execution.
	// */
	// private Map<String, PluginTime> moduleTimeMap;
	//
	// /**
	// * Contains per plugin last updated time of execution.
	// */
	// private Map<String, PluginTime> pluginTimeMap;

	/**
	 * Map to consist list of plugins in order for a module.
	 */
	private Map<String, List<String>> modulePluginMap;

	/**
	 * {@link List}<{@link String}> of all executed plugins for current module.
	 */
	private List<String> executedPluginList;

	/**
	 * {@link List}<{@link String}> of all executed modules for current batch instance.
	 */
	private List<String> executedModuleList;

	/**
	 * {@link List}<{@link String}> of all non-executed modules for current batch instance.
	 */
	private List<String> nonExecutedModuleList;

	/**
	 * {@link List}<{@link String}> of all non-executed plugins for current module.
	 */
	private List<String> nonExecutedPluginList;

	/**
	 * Gets list of all non executed module list.
	 * 
	 * @return {@link List}<{@link String}>
	 */
	public List<String> getNonExecutedModuleList() {
		return nonExecutedModuleList;
	}

	/**
	 * Gets list of all non executed module list.
	 * 
	 * @param nonExecutedModuleList {@link List}<{@link String}>
	 */
	public void setNonExecutedModuleList(final List<String> nonExecutedModuleList) {
		this.nonExecutedModuleList = nonExecutedModuleList;
	}

	/**
	 * Gets list of all non executed plugin list.
	 * 
	 * @return {@link List}<{@link String}>
	 */
	public List<String> getNonExecutedPluginList() {
		return nonExecutedPluginList;
	}

	/**
	 * Sets list of all non executed plugin list.
	 * 
	 * @param nonExecutedPluginList {@link List}<{@link String}>
	 */
	public void setNonExecutedPluginList(final List<String> nonExecutedPluginList) {
		this.nonExecutedPluginList = nonExecutedPluginList;
	}

	/**
	 * Gets current executing plugin.
	 * 
	 * @return {@link String}
	 */
	public String getCurrentExecutingPlugin() {
		return currentExecutingPlugin;
	}

	/**
	 * Sets current executing plugin
	 * 
	 * @param currentExecutingPlugin {@link String}
	 */
	public void setCurrentExecutingPlugin(final String currentExecutingPlugin) {
		this.currentExecutingPlugin = currentExecutingPlugin;
	}

	/**
	 * Gets a map for plugins corresponding modules.
	 * 
	 * @return {@link Map}<{@link String}, {@link String}>
	 */
	public Map<String, List<String>> getModulePluginMap() {
		return modulePluginMap;
	}

	/**
	 * 
	 * @param modulePluginMap 
	 */
	public void setModulePluginMap(final Map<String, List<String>> modulePluginMap) {
		this.modulePluginMap = modulePluginMap;
	}

	public String getCurrentExecutingModule() {
		return currentExecutingModule;
	}

	public void setCurrentExecutingModule(final String currentExecutingModule) {
		this.currentExecutingModule = currentExecutingModule;
	}

	public List<String> getExecutedPluginList() {
		return executedPluginList;
	}

	public void setExecutedPluginList(final List<String> executedPluginList) {
		this.executedPluginList = executedPluginList;
	}

	public List<String> getExecutedModuleList() {
		return executedModuleList;
	}

	public void setExecutedModuleList(final List<String> executedModuleList) {
		this.executedModuleList = executedModuleList;
	}

}
