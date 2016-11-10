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

import java.io.Serializable;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * This DTO deals with connection management profile.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.domain
 */
public class ConnectionManagementDTO extends AbstractChangeableEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String identifier;
	
	private String connectionName;
	
	private String description;
	
	private String userName;
	
	private String password;
	
	private String databaseType;
	
	private int port;
	
	private String connectionURL;
	
	private String databaseName;
	
	private String hostName;
	
	private String databaseDriver;
	
	public String getIdentifier() {
		return identifier;
	}


	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}


	
	public String getConnectionName() {
		return connectionName;
	}

	public void setConnectionName(String connectionName) {
		this.connectionName = connectionName;
	}

	
	public String getDescription() {
		return description;
	}

	
	public void setDescription(String description) {
		this.description = description;
	}

	
	public String getUserName() {
		return userName;
	}

	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	public String getPassword() {
		return password;
	}

	
	public void setPassword(String password) {
		this.password = password;
	}

	
	public String getDatabaseType() {
		return databaseType;
	}

	
	public void setDatabaseType(String databaseType) {
		this.databaseType = databaseType;
	}

	
	public int getPort() {
		return port;
	}

	
	public void setPort(int port) {
		this.port = port;
	}

	
	public String getDatabaseDriver() {
		return databaseDriver;
	}

	
	public void setDatabaseDriver(String databaseDriver) {
		this.databaseDriver = databaseDriver;
	}

	
	public String getConnectionURL() {
		return connectionURL;
	}

	
	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
	}

	
	public String getDatabaseName() {
		return databaseName;
	}

	
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	
	public String getHostName() {
		return hostName;
	}

	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
}
