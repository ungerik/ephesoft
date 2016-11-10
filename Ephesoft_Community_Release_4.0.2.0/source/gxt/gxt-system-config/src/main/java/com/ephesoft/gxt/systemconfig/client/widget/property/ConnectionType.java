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

package com.ephesoft.gxt.systemconfig.client.widget.property;

import com.ephesoft.gxt.core.shared.util.StringUtil;

public enum ConnectionType {

	MYSQL("MYSQL", "jdbc:mysql:", "com.mysql.jdbc.Driver"), MSSQL("MSSQL", "jdbc:jtds:sqlserver:", "net.sourceforge.jtds.jdbc.Driver"),
	MSSQL_ALWAYSON("MSSQL Windows Authentication", "jdbc:jtds:sqlserver:", "net.sourceforge.jtds.jdbc.Driver"), ORACLE("ORACLE",
			"jdbc:oracle:thin:", "oracle.jdbc.OracleDriver"), MARIADB("Maria DB", "jdbc:mysql:", "org.mariadb.jdbc.Driver");

	/* "oracle.jdbc.driver.OracleDriver" */
	private String name;
	private String driverURLAnnotation;
	private String driver;

	private ConnectionType(String name, String driverAnnotation, String driver) {
		this.name = name;
		this.driverURLAnnotation = driverAnnotation;
		this.driver = driver;
	}

	public String getName() {
		return name;
	}

	public String getDriverURLAnnotation() {
		return driverURLAnnotation;
	}

	public String getDriver() {
		return driver;
	}

	public String getDriver(String databaseType) {
		String driverName = null;

		if (!StringUtil.isNullOrEmpty(databaseType)) {
			ConnectionType[] connectionTypes = ConnectionType.values();
			for (final ConnectionType connection : connectionTypes) {
				if(databaseType.equalsIgnoreCase(connection.getName())) {
					driverName = connection.getDriver();
					break;
				}
			}
		}
		return driverName;
	}
}
