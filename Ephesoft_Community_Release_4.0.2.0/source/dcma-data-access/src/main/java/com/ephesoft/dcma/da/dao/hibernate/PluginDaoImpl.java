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

package com.ephesoft.dcma.da.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.PluginDao;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.Plugin;

@Repository
public class PluginDaoImpl extends HibernateDao<Plugin> implements PluginDao {

	private static final String PLUGIN_NAME = "pluginName";
	private static final String PLUGIN = "plugin";
	private static final String MODULE_ID = "module.id";
	private static final String PLUGIN_ID = "id";
	private static final String MODULE = "module";
	private static final String BATCH_CLASS_MODULE_MODULE = "batchClassModule.module";
	private static final String BATCH_CLASS_MODULE = "batchClassModule";
	private static final Logger LOG = LoggerFactory.getLogger(PluginDaoImpl.class);

	@Override
	public Plugin getPluginPropertiesForPluginId(Long pluginId) {
		LOG.info("pluginId : " + pluginId);
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(PLUGIN_ID, pluginId));
		return this.findSingle(criteria);
	}

	/**
	 * @param moduleId Long
	 * @param startResult int
	 * @param maxResult int
	 * @return List<Plugin>
	 */
	@Override
	public List<Plugin> getPlugins(Long moduleId, int startResult, int maxResult) {
		DetachedCriteria criteria = criteria(BatchClassPlugin.class);
		criteria.createAlias(BATCH_CLASS_MODULE, BATCH_CLASS_MODULE, JoinFragment.INNER_JOIN);
		criteria.createAlias(BATCH_CLASS_MODULE_MODULE, MODULE, JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq(MODULE_ID, moduleId));
		criteria.setProjection(Projections.property(PLUGIN));
		return find(criteria, startResult, maxResult);
	}

	@Override
	public Plugin getPluginByName(String pluginName) {
		DetachedCriteria criteria = criteria(Plugin.class);
		criteria.add(Restrictions.eq(PLUGIN_NAME, pluginName));
		return this.findSingle(criteria);
	}

	@Override
	public List<String> getAllPluginsNames() {
		DetachedCriteria criteria = criteria();
		criteria.setProjection(Projections.property(PLUGIN_NAME));
		return find(criteria);
	}
}
