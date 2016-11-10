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

package com.ephesoft.dcma.core.hibernate.util;

import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao;
import com.ephesoft.dcma.util.DatabaseUtil;

public final class HibernateDaoUtil {

	/**
	 * Private method to avoid instantiation.
	 */
	public HibernateDaoUtil() {
		// Do Nothing
	}

	public static DynamicHibernateDao createHibernateDaoConnection(final String driverName, String dbConnectionURL,
			final String userName, final String dbPassword) throws DCMAException {
		String schemaName = null;
		String dialectName = null;
		if (DatabaseUtil.ORACLE_DRIVER.equals(driverName)) {
			dialectName = DatabaseUtil.ORACLE_DIALECT_NAME;
			schemaName = DatabaseUtil.getDatabaseName(dbConnectionURL, driverName).toUpperCase();
			dbConnectionURL = dbConnectionURL.substring(0, dbConnectionURL.lastIndexOf(DatabaseUtil.URL_PARAM_SEPARATOR));
		} else if (DatabaseUtil.JTDS_DRIVER.equals(driverName)) {
			schemaName = DatabaseUtil.MS_SQL_DEFAULT_SCHEMA_NAME;
		}
		final DynamicHibernateDao dao = new DynamicHibernateDao(userName, dbPassword, driverName, dbConnectionURL, dialectName,
				schemaName);
		return dao;
	}
	
	/**
	 * Gets the DynamicHibernateDao object for the provided credentials.
	 * 
	 * @param driverName{@link String}
	 * @param dbConnectionUrl{@link String}
	 * @param userName{@link String}
	 * @param dbPassword{@link String}
	 * @return {@link DynamicHibernateDao}
	 */
	public static DynamicHibernateDao testHibernateDaoConnection(final String driverName, String dbConnectionURL,
			final String userName, final String dbPassword) throws DCMAException {
		String schemaName = null;
		String dialectName = null;
		if (DatabaseUtil.ORACLE_DRIVER.equals(driverName)) {
			dialectName = DatabaseUtil.ORACLE_DIALECT_NAME;
			schemaName = DatabaseUtil.getDatabaseName(dbConnectionURL, driverName).toUpperCase();
			dbConnectionURL = dbConnectionURL.substring(0, dbConnectionURL.lastIndexOf(DatabaseUtil.URL_PARAM_SEPARATOR));
		} else if (DatabaseUtil.JTDS_DRIVER.equals(driverName)) {
			dialectName = DatabaseUtil.MSSQL_DIALECT_NAME;
			schemaName = DatabaseUtil.MS_SQL_DEFAULT_SCHEMA_NAME;
		}else if (DatabaseUtil.MYSQL_DRIVER.equals(driverName)){
			dialectName = DatabaseUtil.MYSQL_DIALECT_NAME;
			schemaName = DatabaseUtil.getDatabaseName(dbConnectionURL, driverName).toUpperCase();
		}else if (DatabaseUtil.MSSQL_DRIVER.equals(driverName)){
			dialectName = DatabaseUtil.MYSQL_DIALECT_NAME;
			schemaName = DatabaseUtil.getDatabaseName(dbConnectionURL, driverName).toUpperCase();
		}
		
		final DynamicHibernateDao dao = new DynamicHibernateDao(userName, dbPassword, driverName, dbConnectionURL, dialectName,
				schemaName);
		return dao;
	}
}
