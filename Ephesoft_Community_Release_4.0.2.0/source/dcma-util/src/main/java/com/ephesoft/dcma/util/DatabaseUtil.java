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

package com.ephesoft.dcma.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides methods for performing database related operations.
 * 
 * <p>
 * Following utilities have been provided:-
 * <li>Retrieve database name from the connection URL on the basis of database driver.
 * <ul>
 * <li>Database driver com.mysql.jdbc.Driver character '/' separates URL from database name.
 * <li>Database driver com.microsoft.jdbc.sqlserver.SQLServerDriver keyword database separates URL from database name.
 * <li>Database driver net.sourceforge.jtds.jdbc.Driver keyword databaseName separates URL from database name.
 * <li>Database driver oracle.jdbc.OracleDriver keyword dbName separates URL from database name.
 * </ul>
 * <li>Extracts database name from the URL on the basis of the string appended in connection URL.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */

public class DatabaseUtil {

	/**
	 * Logger instance for logging using slf4j.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseUtil.class);
	/**
	 *Constant defined for representing an empty string.
	 */
	public static final String EMPTY_STRING = "";
	/**
	 * Variable contains driver used for connecting to database through MYSQL.
	 */
	public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
	/**
	 * Variable contains separator of database name from connection URL for MYSQL.
	 */
	public static final char URL_SEPERATOR = '/';
	/**
	 * Variable contains driver used for connecting to database through MYSQL.
	 */
	public static final String MSSQL_DRIVER = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	/**
	 * Variable contains word database.
	 */
	public static final String DATABASE = "database";
	/**
	 * Variable contains word database name.
	 */
	public static final String DATABASE_NAME = "databaseName";
	/**
	 * Variable contains driver value for JTDS.
	 */
	public static final String JTDS_DRIVER = "net.sourceforge.jtds.jdbc.Driver";
	/**
	 *Variable contains driver used for connecting to database through Oracle.
	 */
	public static final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";
	/**
	 * Variable contains word dbName
	 */
	public static final String DB_NAME = "dbName";
	/**
	 * Constant defined for semicolon.
	 */
	public static final String SEMICOLON = ";";
	/**
	 * A constant defined for equal sign.
	 */
	public static final String EQUAL = "=";

	/**
	 * Constant defined for " enclosing character
	 */
	public static final String DOUBLE_QUOTES = "\"";

	/**
	 * Constant defined for ` enclosing character
	 */
	public static final String SINGLE_QUOTES = "`";

	/**
	 * Constant defined for DB URL separator
	 */
	public static final String URL_PARAM_SEPARATOR = ";";

	/**
	 * Variable contains default schema name for connecting to database through MYSQL.
	 */
	public static final String MS_SQL_DEFAULT_SCHEMA_NAME = "dbo";
	
	/**
	 * Dialect name for Oracle
	 */
	public static final String ORACLE_DIALECT_NAME = "org.hibernate.dialect.Oracle10gDialect";

	/**
	 * Dialect name for MSSQL
	 */
	public static final String MSSQL_DIALECT_NAME = "org.hibernate.dialect.SQLServerDialect";
	
	/**
	 * Dialect name for MySQL
	 */
	public static final String MYSQL_DIALECT_NAME = "org.hibernate.dialect.MySQLDialect";
	
	/**
	 *Gets database name from the connection URL on the basis of driver provided.
	 * 
	 *<p>
	 *In case if any parameter provided is NULL, empty string is returned otherwise according to the availability.
	 * 
	 *@param dbConnectionURL{@link String} connection URL for connecting to database
	 *@param dbDriver{@link String} driver name used for connecting database
	 *@return dbName{@link String} returns database name from on basis of driver passed as an argument
	 */
	public static String getDatabaseName(final String dbConnectionURL, final String dbDriver) {
		LOGGER.info("Inside method getDatabaseName");
		String dbName = EMPTY_STRING;
		if (!EphesoftStringUtil.isNullOrEmpty(dbConnectionURL) && !EphesoftStringUtil.isNullOrEmpty(dbDriver)) {
			if (MYSQL_DRIVER.equals(dbDriver)) {
				dbName = dbConnectionURL.substring(dbConnectionURL.lastIndexOf(URL_SEPERATOR) + 1, dbConnectionURL.length());
			} else if (MSSQL_DRIVER.equals(dbDriver)) {
				dbName = extractDbNameValueFromUrl(dbConnectionURL, DATABASE);
			} else if (JTDS_DRIVER.equals(dbDriver)) {
				dbName = extractDbNameValueFromUrl(dbConnectionURL, DATABASE_NAME);
			} else if (ORACLE_DRIVER.equals(dbDriver)) {
				dbName = extractDbNameValueFromUrl(dbConnectionURL, DB_NAME);
			} else {
				LOGGER.error("Database driver not supported");
			}
			dbName = com.ephesoft.dcma.util.FileUtils.replaceInvalidFileChars(dbName);
		}
		return dbName;
	}

	/**
	 *Extracts database name from connection URL and keyword string used to separate database name from the connection URL.
	 *<p>
	 * In case if any parameter provided is NULL, empty string is returned otherwise according to the availability.
	 * 
	 * @param dbConnectionURL{@link String}contains connection URL for specifying database connection URL.
	 * @param dbKeywordString{@link {@link String} contains keyword that separates database name from connection URL.
	 * @return {@link String} database name.
	 */
	private static String extractDbNameValueFromUrl(final String dbConnectionURL, final String dbKeywordString) {

		String dbName = EMPTY_STRING;
		if (!EphesoftStringUtil.isNullOrEmpty(dbConnectionURL) && !EphesoftStringUtil.isNullOrEmpty(dbKeywordString)) {
			final String[] urlSplitArr = EphesoftStringUtil.splitString(dbConnectionURL, SEMICOLON);
			if (urlSplitArr.length > 0) {
				for (final String urlSplit : urlSplitArr) {
					if (urlSplit.contains(dbKeywordString)) {
						dbName = urlSplit.substring(urlSplit.indexOf(EQUAL) + 1);
						break;
					}
				}
			}
		}
		return dbName;
	}

	/**
	 * Returns the enclosing string for the identifiers based on the database driver.
	 * 
	 * @param dbDriver {@link String}
	 * @return {@link String}
	 */
	public static String getEnclosingStringByDb(final String dbDriver) {
		String enclosingString = "";
		if (dbDriver.equals(MYSQL_DRIVER)) {
			enclosingString = SINGLE_QUOTES;
		} else {
			// MS-SQL, ORACLE both support " as the enclosing character.
			enclosingString = DOUBLE_QUOTES;
		}
		return enclosingString;
	}
	public static String getDatabaseDriverFromDatabaseType(String databaseType){
		String databaseDriver=null;
		if("MYSQL".equalsIgnoreCase(databaseType)|| "Maria DB".equalsIgnoreCase(databaseType)){
			databaseDriver="com.mysql.jdbc.Driver";
		}
		else if("MSSQL".equalsIgnoreCase(databaseType) || "MSSQL Windows Authentication".equalsIgnoreCase(databaseType)){
			databaseDriver="net.sourceforge.jtds.jdbc.Driver";
		}
		else if("ORACLE".equalsIgnoreCase(databaseType)){
			databaseDriver="oracle.jdbc.OracleDriver";
		}
		return databaseDriver;
	}

	
}
