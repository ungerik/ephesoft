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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.da.dao.PluginConfigDao;
import com.ephesoft.dcma.da.dao.PluginDao;
import com.ephesoft.dcma.da.domain.Dependency;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;
import com.ephesoft.dcma.util.EphesoftStringUtil;

@Service
public class PluginServiceImpl implements PluginService {

	/**
	 * LOGGER to print the Logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassServiceImpl.class);

	@Autowired
	private PluginDao pluginDao;

	@Autowired
	private PluginConfigDao pluginConfigDao;

	@Override
	@Transactional
	public Plugin getPluginPropertiesForPluginId(Long pluginId) {
		LOGGER.info("plugin id : " + pluginId);
		return pluginDao.getPluginPropertiesForPluginId(pluginId);
	}

	@Override
	public Plugin getPluginPropertiesForPluginName(String pluginName) {
		LOGGER.info("plugin name : " + pluginName);
		return pluginDao.getPluginByName(pluginName);
	}

	/**
	 * @param moduleId Long
	 * @param startResult int
	 * @param maxResult int
	 * @return List<Plugin>
	 */
	@Override
	public List<Plugin> getPlugins(Long moduleId, int startResult, int maxResult) {
		List<Plugin> pluginList = null;
		if (null == moduleId) {
			LOGGER.info("moduleId is null");
		} else {
			List<Plugin> tempPluginList = pluginDao.getPlugins(moduleId, startResult, maxResult);
			for (Plugin plugin : tempPluginList) {
				pluginList = new ArrayList<Plugin>();
				if (!plugin.isDeleted()) {
					pluginList.add(plugin);
				} else {
					LOGGER.info(EphesoftStringUtil.concatenate(plugin.getPluginName(), " is deleted in moduleId ", +moduleId));
				}
			}
		}
		return pluginList;
	}

	@Override
	@Transactional
	public List<Plugin> getAllPlugins() {
		LOGGER.info("Getting list of all plugins");
		List<Plugin> tempAllPlugins = pluginDao.getAll();
		List<Plugin> pluginsList = new ArrayList<Plugin>();
		for(Plugin plugin : tempAllPlugins){
			if(!plugin.isDeleted()){
				pluginsList.add(plugin);
			} else {
				LOGGER.info(EphesoftStringUtil.concatenate(plugin.getPluginName()," is deleted."));
			}
		}
		for (Plugin plugin : pluginsList) {
			List<Dependency> dependencies = plugin.getDependencies();
			for (Dependency dependency : dependencies) {
				dependency.getDependencies();
			}
		}
		return pluginsList;
	}

	@Override
	@Transactional(readOnly = false)
	public void createNewPlugin(Plugin plugin) {
		LOGGER.info("Creating a new plugin: " + plugin.getPluginName());
		pluginDao.create(plugin);
	}

	@Override
	@Transactional(readOnly = false)
	public void mergePlugin(Plugin plugin) {
		LOGGER.info("Updating plugin: " + plugin.getPluginName());
		pluginDao.saveOrUpdate(plugin);
	}

	@Override
	@Transactional
	public void removePluginAndReferences(final Plugin plugin, final boolean removeReferences) {
		LOGGER.info("Entering method removePluginAndReferences.");
		if (plugin != null) {
			LOGGER.info("Deleting plugin with id: " + plugin.getId() + " with name: " + plugin.getPluginName());
			pluginDao.remove(plugin);

			LOGGER.debug("Remove references is " + removeReferences);
			if (removeReferences) {
				LOGGER.info("Removing the plugin configs associated with the plugin.");
				final List<PluginConfig> pluginConfigs = pluginConfigDao.getPluginConfigForPluginId(plugin.getId());

				for (PluginConfig pluginConfig : pluginConfigs) {
					LOGGER.info("Removing plugin config with id : " + pluginConfig.getId() + " and name : " + pluginConfig.getName());
					pluginConfigDao.remove(pluginConfig);
				}
			}
		}
		LOGGER.info("Exiting method removePluginAndReferences.");
	}

	@Override
	public List<String> getAllPluginsNames() {
		LOGGER.info("Retriving all plugin names.");
		return pluginDao.getAllPluginsNames();
	}

}
