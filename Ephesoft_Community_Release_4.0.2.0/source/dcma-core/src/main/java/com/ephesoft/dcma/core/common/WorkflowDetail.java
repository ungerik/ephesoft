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

package com.ephesoft.dcma.core.common;

import java.util.List;
import java.util.Map;

public class WorkflowDetail {

	/**
	 * Contains the last plugin executed for given batch.
	 */
	private String currentExecutingPlugin;

	/**
	 * Contains the current module for given batch.
	 */
	private String currentExecutingModule;

	/**
	 * Contains per module list of plugin executed.
	 */
	private Map<String, List<String>> modulePluginMap;

	/**
	 * Contains per module list of plugin not executed.
	 */
	private List<String> executedPluginList;

	private List<String> executedModuleList;

	private List<String> unexecutedModuleList;

	private List<String> unexecutedPluginList;

	public List<String> getUnexecutedModuleList() {
		return unexecutedModuleList;
	}

	public void setUnexecutedModuleList(List<String> unexecutedModuleList) {
		this.unexecutedModuleList = unexecutedModuleList;
	}

	public List<String> getUnexecutedPluginList() {
		return unexecutedPluginList;
	}

	public void setUnexecutedPluginList(List<String> unexecutedPluginList) {
		this.unexecutedPluginList = unexecutedPluginList;
	}

	public String getCurrentExecutingPlugin() {
		return currentExecutingPlugin;
	}

	public void setCurrentExecutingPlugin(final String currentExecutingPlugin) {
		this.currentExecutingPlugin = currentExecutingPlugin;
	}

	public Map<String, List<String>> getModulePluginMap() {
		return modulePluginMap;
	}

	public void setModulePluginMap(final Map<String, List<String>> modulePluginMap) {
		this.modulePluginMap = modulePluginMap;
	}

	public String getCurrentExecutingModule() {
		return currentExecutingModule;
	}

	public void setCurrentExecutingModule(String currentExecutingModule) {
		this.currentExecutingModule = currentExecutingModule;
	}

	public List<String> getExecutedPluginList() {
		return executedPluginList;
	}

	public void setExecutedPluginList(List<String> executedPluginList) {
		this.executedPluginList = executedPluginList;
	}

	public List<String> getExecutedModuleList() {
		return executedModuleList;
	}

	public void setExecutedModuleList(List<String> executedModuleList) {
		this.executedModuleList = executedModuleList;
	}

}
