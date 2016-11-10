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

package com.ephesoft.dcma.da.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

@Entity
@Table(name = "batch_class_module")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BatchClassModule extends AbstractChangeableEntity implements Comparable<BatchClassModule> {

	private static final long serialVersionUID = -5366469561526543577L;

	@OneToOne
	@JoinColumn(name = "batch_class_id")
	private BatchClass batchClass;

	@OneToOne
	@JoinColumn(name = "module_id", nullable = false)
	private Module module;

	@Column(name = "order_number")
	private int orderNumber;

	@Column(name = "workflow_name")
	private String workflowName;

	@Column(name = "remote_url")
	private String remoteURL;

	@Column(name = "remote_batch_class_id")
	private String remoteBatchClassIdentifier;

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_module_id")
	@javax.persistence.OrderBy("orderNumber")
	private List<BatchClassPlugin> batchClassPlugins;

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "batch_class_module_id")
	private List<BatchClassModuleConfig> batchClassModuleConfig;

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public BatchClass getBatchClass() {
		return batchClass;
	}

	public void setBatchClass(BatchClass batchClass) {
		this.batchClass = batchClass;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public List<BatchClassPlugin> getBatchClassPlugins() {
		return batchClassPlugins;
	}

	public void setBatchClassPlugins(List<BatchClassPlugin> batchClassPlugins) {
		this.batchClassPlugins = batchClassPlugins;
	}

	public BatchClassPlugin getBatchClassPluginById(long identifier) {
		BatchClassPlugin plugin = null;
		if (getBatchClassPlugins() != null && getBatchClassPlugins().size() > 0) {
			for (BatchClassPlugin batchClassPlugin : getBatchClassPlugins()) {
				if (batchClassPlugin.getId() == identifier) {
					plugin = batchClassPlugin;
					break;
				}
			}
		}
		return plugin;
	}

	public List<BatchClassPlugin> getBatchClassPluginsByPluginName(String pluginName) {
		List<BatchClassPlugin> batchClassPlugins = new ArrayList<BatchClassPlugin>();
		if (getBatchClassPlugins() != null && getBatchClassPlugins().size() > 0) {
			for (BatchClassPlugin batchClassPlugin : getBatchClassPlugins()) {
				if (batchClassPlugin.getPlugin().getPluginName().equals(pluginName)) {
					batchClassPlugins.add(batchClassPlugin);
				}
			}
		}
		return batchClassPlugins;
	}

	public void removeBatchClassPluginById(long identifier) {
		BatchClassPlugin plugin = getBatchClassPluginById(identifier);
		batchClassPlugins.remove(plugin);
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	public List<BatchClassModuleConfig> getBatchClassModuleConfig() {
		return batchClassModuleConfig;
	}

	public void setBatchClassModuleConfig(List<BatchClassModuleConfig> batchClassModuleConfig) {
		this.batchClassModuleConfig = batchClassModuleConfig;
	}

	public void setRemoteBatchClassIdentifier(String remoteBatchClassIdentifier) {
		this.remoteBatchClassIdentifier = remoteBatchClassIdentifier;
	}

	public String getRemoteBatchClassIdentifier() {
		return remoteBatchClassIdentifier;
	}

	public void setRemoteURL(String remoteURL) {
		this.remoteURL = remoteURL;
	}

	public String getRemoteURL() {
		return remoteURL;
	}

	public List<BatchClassPluginConfig> getBatchClassPluginConfigsByDataType(DataType dataType) {
		List<BatchClassPluginConfig> batchClassPluginConfigs = null;
		if (this.batchClassPlugins != null && this.batchClassPlugins.size() > 0) {
			for (BatchClassPlugin batchClassPlugin : this.batchClassPlugins) {
				if (batchClassPlugin != null) {
					if (batchClassPluginConfigs == null) {
						batchClassPluginConfigs = new ArrayList<BatchClassPluginConfig>();
					}
					List<BatchClassPluginConfig> batchClassPluginConfigByDataType = batchClassPlugin
							.getBatchClassPluginConfigByDataType(dataType);
					if (batchClassPluginConfigByDataType != null) {
						batchClassPluginConfigs.addAll(batchClassPluginConfigByDataType);
					}
				}
			}
		}
		return batchClassPluginConfigs;
	}

	/**
	 * Compare modules on the basis of their order of execution in a batch class.
	 * 
	 * @param module {@link BatchClassModule}
	 */
	@Override
	public int compareTo(BatchClassModule module) {
		int result;
		if (orderNumber < module.getOrderNumber()) {
			result = -1;
		} else if (orderNumber > module.getOrderNumber()) {
			result = 1;
		} else {
			result = 0;
		}
		return result;
	}

	/**
	 * Gets batch class plugins in order of their execution in batch class worklfow.
	 * 
	 * @return {@link List}<{@link BatchClassPlugin}>
	 */
	public List<BatchClassPlugin> getBatchClassPluginInOrder() {
		List<BatchClassPlugin> plugins = getBatchClassPlugins();
		List<BatchClassPlugin> listWithoutDeletedPlugins = new ArrayList<BatchClassPlugin>(plugins.size());
		if (plugins != null) {
			for (BatchClassPlugin batchClassPlugin : plugins) {
				if (!(isBatchClassPluginDeleted(batchClassPlugin))) {
					listWithoutDeletedPlugins.add(batchClassPlugin);
				}
			}
		}
		if (null != listWithoutDeletedPlugins) {
			Collections.sort(listWithoutDeletedPlugins);
		}
		return listWithoutDeletedPlugins;
	}

	/**
	 * Checks if is batch class plugin deleted.
	 *
	 * @param batchClassPlugin the batch class plugin
	 * @return true, if is batch class plugin deleted
	 */
	private boolean isBatchClassPluginDeleted(BatchClassPlugin batchClassPlugin) {
		return batchClassPlugin != null && batchClassPlugin.getPlugin() != null && batchClassPlugin.getPlugin().isDeleted() != null
				&& batchClassPlugin.getPlugin().isDeleted();
	}
}
