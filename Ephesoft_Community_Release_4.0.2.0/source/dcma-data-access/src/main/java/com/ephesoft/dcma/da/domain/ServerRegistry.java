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

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * 
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Nov 10, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Entity
@Table(name = "server_registry")
public class ServerRegistry extends AbstractChangeableEntity {

	/**
	 * Serial Version UID long
	 */
	private static final long serialVersionUID = 6213724039823490819L;

	/**
	 * IP Address of the server {@link String}
	 */
	@Column(name = "ip_address")
	private String ipAddress;

	/**
	 * Port used by application {@link String}
	 */
	@Column(name = "port_number")
	private String port;

	/**
	 * ApplicationContext {@link String}
	 */
	@Column(name = "app_context")
	private String appContext;

	/**
	 * Active flag boolean
	 */
	@Column(name = "is_active")
	private boolean active;

	/**
	 * Last Updated Time for Batch {@link Date}
	 */
	@Column(name = "last_updated_batch_time")
	private Date lastUpdatedBatchTime;

	/**
	 * licenseServer flag boolean
	 */
	@Column(name = "is_license_server", columnDefinition = "bit default 0")
	private boolean licenseServer;

	@Column(name = "is_folderMoniter_server", columnDefinition = "bit default 0")
	private boolean folderMoniterServer;

	/**
	 * Set of services linked to given server {@link Set}<{@link ServiceStatus}>
	 */
	@OneToMany(mappedBy = "serverRegistry", fetch = FetchType.LAZY)
	private Set<ServiceStatus> serviceStatusSet;

	/**
	 * {@link String} Column to store the Priority range of Server.
	 */
	@Column(name = "priority_range", length = 700)
	private String priorityRange;

	/**
	 * int Column to store the Max Execution Capacity of Server.
	 */
	@Column(name = "execution_capacity", columnDefinition = "int default 0")
	private int executionCapacity;

	/**
	 * Gets Execution Capacity used by Application
	 * 
	 * @return int
	 */
	public int getExecutionCapacity() {
		return executionCapacity;
	}

	/**
	 * Sets Execution Capacity used by Application
	 * 
	 * @param executionCapacity int
	 */
	public void setExecutionCapacity(int executionCapacity) {
		this.executionCapacity = executionCapacity;
	}

	/**
	 * Gets IP Address used by Application
	 * 
	 * @return {@link String}
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets IP Address used by Application
	 * 
	 * @param ipAddress {@link String}
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * Gets Port used by application
	 * 
	 * @return {@link String}
	 */
	public String getPort() {
		return port;
	}

	/**
	 * Sets Port used by application
	 * 
	 * @param port {@link String}
	 */
	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * Gets Last Application Context
	 * 
	 * @return {@link String}
	 */
	public String getAppContext() {
		return appContext;
	}

	/**
	 * Sets Last Application Context
	 * 
	 * @param appContext {@link String}
	 */
	public void setAppContext(String appContext) {
		this.appContext = appContext;
	}

	/**
	 * Gets Last IsActive Flag
	 * 
	 * @return boolean
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Sets Last IsActive Flag
	 * 
	 * @param active boolean
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * Gets Last Updated Batch Time
	 * 
	 * @return {@link Date}
	 */
	public Date getLastUpdatedBatchTime() {
		return lastUpdatedBatchTime;
	}

	/**
	 * Sets Last Updated Batch Time
	 * 
	 * @param lastUpdatedBatchTime {@link Date}
	 */
	public void setLastUpdatedBatchTime(Date lastUpdatedBatchTime) {
		this.lastUpdatedBatchTime = lastUpdatedBatchTime;
	}

	/**
	 * 
	 * @param serviceStatusSet {@link Set}<{@link ServiceStatus}>
	 */
	public void setServiceStatusSet(Set<ServiceStatus> serviceStatusSet) {
		this.serviceStatusSet = serviceStatusSet;
	}

	/**
	 * 
	 * @return {@link Set}<{@link ServiceStatus}>
	 */
	public Set<ServiceStatus> getServiceStatusSet() {
		return serviceStatusSet;
	}

	/**
	 * Gets License Server Flag
	 * 
	 * @return boolean
	 */
	public boolean isLicenseServer() {
		return licenseServer;
	}

	/**
	 * Sets License Server Flag
	 * 
	 * @param isLicenseServer boolean
	 */
	public void setLicenseServer(boolean isLicenseServer) {
		this.licenseServer = isLicenseServer;
	}

	public boolean isFolderMoniterServer() {
		return folderMoniterServer;
	}

	public void setFolderMoniterServer(boolean folderMoniterServer) {
		this.folderMoniterServer = folderMoniterServer;
	}

	/**
	 * Gets priority Range string for the server.
	 * 
	 * @return {@link String}
	 */
	public String getPriorityRange() {
		return priorityRange;
	}

	/**
	 * Sets priority Range string for the server.
	 * 
	 * @param priorityRange {@link String}
	 */
	public void setPriorityRange(final String priorityRange) {
		this.priorityRange = priorityRange;
	}
}
