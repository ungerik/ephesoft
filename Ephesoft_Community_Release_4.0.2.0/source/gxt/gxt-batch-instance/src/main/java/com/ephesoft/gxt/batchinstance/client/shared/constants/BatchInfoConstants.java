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

package com.ephesoft.gxt.batchinstance.client.shared.constants;

import com.ephesoft.dcma.core.common.FileType;

/**
 * This interface contains constants related to the troubleshoot pop up.
 * 
 * @author Ephesoft
 * @version 3.0
 * @see
 * 
 */
public interface BatchInfoConstants {

	/**
	 * WEB_INF constant for string 'WEB-INF'.
	 */
	String WEB_INF = "WEB-INF";

	/**
	 * CLASSES constant for string 'classes'.
	 */
	String CLASSES = "classes";

	/**
	 * META_INF constant for string 'META-INF'.
	 */
	String META_INF = "META-INF";

	/**
	 * DATA_ACCESS constant for folder name 'dcma-data-access'.
	 */
	String DATA_ACCESS = "dcma-data-access";

	/**
	 * PROPERTY_FILE constant for db property file name.
	 */
	String DB_PROPERTY_FILE = "dcma-db.properties";

	/**
	 * COPY {@link String}.
	 */
	String COPY = "_copy";

	/**
	 * LOG {@link String}.
	 */
	String LOG = "logs";

	/**
	 * constant for string 'Applicatiom logs'.
	 */
	String ALL_LOG_FOLDER = "Application logs";

	/**
	 * constant for dcma-all log file.
	 */
	String ALL_LOG_FILE = "dcma-all.log";

	/**
	 * constant for batch log folder.
	 */
	String BATCH_LOG_FOLDER = "Batch Instance logs";

	/**
	 * constant for java application server log folder.
	 */
	String JAVA_APP_SERVER_LOG_FOLDER = "JavaAppServer logs";

	/**
	 * constant for date format to be used in folder name.
	 */
	String DATE_FORMAT_MMDDYY = "MMddyy";

	/**
	 * constant for property database name.
	 */
	String DB_NAME = "dataSource.databaseName";

	/**
	 * constant for property database user name.
	 */
	String DB_USERNAME = "dataSource.username";

	/**
	 * constant for property database password.
	 */
	String DB_PASSWORD = "dataSource.password";

	/**
	 * constant for property server name.
	 */
	String DB_SERVER = "dataSource.serverName";

	/**
	 * constant for property mysql dump file.
	 */
	String MYSQL_DUMP_FILE = "Database_dump.sql";

	/**
	 * constant for property mssql dump file name.
	 */
	String MSSQL_DUMP_FILE = "Database_dump.bak";

	/**
	 * constant for property database dialect.
	 */
	String DB_DIALECT = "dataSource.dialect";

	/**
	 * constant for dialect name for mysql.
	 */
	String MYSQL_DIALECT = "org.hibernate.dialect.MySQL5InnoDBDialect";

	/**
	 * constant for dialect name for mssql.
	 */
	String MSSQL_DIALECT = "com.ephesoft.dcma.da.common.EphesoftSQLServerDialect";

	/**
	 * constant for form parameter name for database dump option.
	 */
	String DATABASE_DUMP_FOLDER = "database_dump";

	/**
	 * Constant for form parameter name for batch class option.
	 */
	String BATCH_CLASS_FOLDER = "batch_class_folder";

	/**
	 * Constant for form parameter name for batch instance option.
	 */
	String BATCH_INSTANCE_FOLDER = "batch_instance_folder";

	/**
	 * constant for form parameter name for batch instance identifier.
	 */
	String BATCH_INSTANCE_IDENTIFIER = "batchInstanceIdentifier";

	/**
	 * constant for form parameter name for radio button.
	 */
	String SELECTED_RADIO_BUTTON = "selectedRadioButton";

	/**
	 * constant for form parameter name for image classification sample option.
	 */
	String IMAGE_CLASSIFICATION_SAMPLE = "image_classification_sample";

	/**
	 * constant for form parameter name for lucene search classification option.
	 */
	String LUCENE_SEARCH_CLASSIFICATION_SAMPLE = "lucene_search_classification_sample";

	/**
	 * constant for form parameter name for lib option.
	 */
	String LIB = "lib";

	/**
	 * constant for string 'troubleshoot'.
	 */
	String TROUBLESHOOT = "troubleshoot";

	/**
	 * DCMA_HOME {@link String}.
	 */
	String DCMA_HOME = "DCMA_HOME";

	/**
	 * DCMA_DATA_ACCESS {@link String}.
	 */
	String DCMA_DATA_ACCESS = "dcma-data-access/dcma-db";

	/**
	 * LOG_EXT {@link String}.
	 */
	String LOG_EXT = ".log";

	/**
	 * UNDERSCORE {@link String}.
	 */
	String UNDERSCORE = "_";

	/**
	 * image classification sample option.
	 */
	String IMAGE_CLASSIFICATION_SAMPLE_LOCALE = "image_classification_sample";

	/**
	 * lucene search classification option.
	 */
	String LUCENE_SEARCH_CLASSIFICATION_SAMPLE_LOCALE = "lucene_search_classification_sample";

	/**
	 * meta inf option.
	 */
	String META_INF_LOCALE = "meta_inf";

	/**
	 * constant for form parameter name for batch logs option.
	 */
	String BATCH_LOGS = "batch_instance";

	/**
	 * constant for form parameter name for batchClass option.
	 */
	String BATCH_CLASS = "batch_class";

	/**
	 * constant for form parameter name for application option.
	 */
	String APPLICATION = "application";

	/**
	 * extension for zip folder.
	 */
	String ZIP_EXT = FileType.ZIP.getExtensionWithDot();

	/**
	 * extension for serialised file.
	 */
	String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();

	/**
	 * constant for character ':' .
	 */
	String SEMI_COLON = ":";

	/**
	 * constant for form parameter name for unc folder.
	 */
	String UNC_FOLDER = "unc_folder";

	/**
	 * constant for form parameter name for download radio button.
	 */
	String DOWNLOAD_RADIO = "download_radio";

	/**
	 * constant for form parameter name for upload radio button.
	 */
	String UPLOAD_RADIO = "upload_radio";

	/**
	 * SELECT_ALL {@link String}.
	 */
	String SELECT_ALL = "select_all";

	/**
	 * LICENCE_DETAIL {@link String}.
	 */
	String LICENCE_DETAIL = "license_detail";

	/**
	 * constant for form parameter name for java app server logs option.
	 */
	String JAVA_APP_SERVER = "java_app_server";

	/**
	 * constant for form parameter name for path field.
	 */
	String PATH = "path";

	/**
	 * FTP_PATH {@link String}.
	 */
	String FTP_PATH = "ftp_path";

	/**
	 * USERNAME {@link String}.
	 */
	String USERNAME = "username";

	/**
	 * PASSWORD {@link String}.
	 */
	String PASSWORD = "password";

	/**
	 * USERNAME_TEXT_BOX {@link String}.
	 */
	String USERNAME_TEXT_BOX = "textBoxUserName";

	/**
	 * constant for form parameter name for password field.
	 */
	String PASSWORD_TEXT_BOX = "textBoxPassword";

	/**
	 * constant for form parameter name for download folder path field.
	 */
	String PATH_TEXT_BOX = "textboxPath";

	/**
	 * constant for application.properties file name.
	 */
	String APPLICATION_PROPERTY_FILE_NAME = "application";

	/**
	 * constant for ftp property filr path.
	 */
	String FTP_PROPERTY_FILE_NAME = "dcma-ftp/dcma-ftp";

	/**
	 * constant for property name ftp server url.
	 */
	String FTP_URL = "ftp.server.url";

	/**
	 * constant for property name ftp server username.
	 */
	String SERVER_USERNAME = "ftp.server.username";

	/**
	 * constant for property name ftp server password.
	 */
	String SERVER_PASSWORD = "ftp.server.password";

	/**
	 * TXT_EXT extension for text file.
	 */
	String TXT_EXT = FileType.TXT.getExtensionWithDot();

	/**
	 * constant for form parameter name for ftp path.
	 */
	String FTP_PATH_TEXT_BOX = "textBoxFtp";

	/**
	 * constant for property name ephesoft product version.
	 */
	String PRODUCT_VERSION = "ephesoft.product.version";

	/**
	 * constant for form parameter name for application logs.
	 */
	String APPLICATION_LOGS = "application_logs";

	/**
	 * constant for native folder name.
	 */
	String NATIVE = "native";

	/**
	 * COMMA constant for ','.
	 */
	String COMMA = ",";

	/**
	 * constant for download to radio button parameter name.
	 */
	String DOWNLOAD_TO_RADIO = "download_to_radio";

	/**
	 * constant for string 'JavaAppServer' .
	 */
	String JAVA_APP_SERVER_FOLDER = "JavaAppServer";

	/**
	 * constant for ftp property name number_of_retries.
	 */
	String NUM_OF_RETRIES = "ftp.number_of_retries";

	/**
	 * constant for ftp property name upload_troubleshoot_dir.
	 */
	String UPLOAD_BASE_DIR = "ftp.upload_troubleshoot_dir";

	/**
	 * constant for ftp property name upload_troubleshoot_dir.
	 */
	String DATA_TIMEOUT = "ftp.data.timeout";

	/**
	 * TEMP_FOLDER {@link String}.
	 */
	String TEMP_FOLDER = "temp";

	/**
	 * MSSQL_DB_DUMP_COMMAND {@link String}.
	 */
	String MSSQL_DB_DUMP_COMMAND = "dbDump.MSSQL.command";

	/**
	 * MSSQL_ECHO {@link String}.
	 */
	String MSSQL_ECHO = "dbDump.MSSQL.echo";

	/**
	 * MSSQL_SERVER {@link String}.
	 */
	String MSSQL_SERVER = "dbDump.MSSQL.server";

	/**
	 * MSSQL_USERNAME {@link String}.
	 */
	String MSSQL_USERNAME = "dbDump.MSSQL.username";

	/**
	 * MSSQL_PASSWORD {@link String}.
	 */
	String MSSQL_PASSWORD = "dbDump.MSSQL.password";

	/**
	 * MSSQL_QUERY {@link String}.
	 */
	String MSSQL_QUERY = "dbDump.MSSQL.query";

	/**
	 * MYSQL_DB_DUMP_COMMAND {@link String}.
	 */
	String MYSQL_DB_DUMP_COMMAND = "dbDump.MySQL.command";

	/**
	 * MYSQL_SERVER {@link String}.
	 */
	String MYSQL_SERVER = "dbDump.MySQL.server";

	/**
	 * MYSQL_USERNAME {@link String}.
	 */
	String MYSQL_USERNAME = "dbDump.MySQL.username";

	/**
	 * MYSQL_PASSWORD {@link String}.
	 */
	String MYSQL_PASSWORD = "dbDump.MySQL.password";

	/**
	 * MYSQL_DB_DUMP_PATH {@link String}.
	 */
	String MYSQL_DB_DUMP_PATH = "dbDump.MySQL.path";

	/**
	 * MYSQL_ADD_DROP {@link String}.
	 */
	String MYSQL_ADD_DROP = "dbDump.MySQL.add_drop";

	/**
	 * MYSQL_DUMP_SEVERAL {@link String}.
	 */
	String MYSQL_DUMP_SEVERAL = "dbDump.MySQL.dumpSeveral";

	/**
	 * SPACE constant for ' '.
	 */
	String SPACE = " ";

	/**
	 * UNC_FOLDER_NAME the unc folder name.
	 */
	String UNC_FOLDER_NAME = "Unc Folder";

	/**
	 * APPLICATION_FOLDER_NAME the application folder name.
	 */
	String APPLICATION_FOLDER_NAME = "Application";

	/**
	 * LICENCE_DETAIL_FILENAME the license file name.
	 */
	String LICENCE_DETAIL_FILENAME = "License_details";

	/**
	 * CONTENT_TYPE {@link String} represents the content type of the response being sent to the client.
	 */
	String APPLICATION_ZIP = "application/x-zip\r\n";

	/**
	 * CONTENT_DISPOSITION {@link String} represents a name of the response header,
	 */
	String CONTENT_DISPOSITION = "Content-Disposition";

	/**
	 * UNDER_SCORE {@link String} represents the constant for under score.
	 */
	String UNDER_SCORE = "_";

	/**
	 * STRING_ON {@link String} is a constant used for comapring the ON/OFF comaprision of string.
	 */
	String STRING_ON = "ON";

	/**
	 * BATCH_INSTANCE_LOGGING {@link String} is a constant used for getting the property from dcma-batch.properties file whether the
	 * enhanced error logging switch is ON or OFF.
	 */
	String BATCH_INSTANCE_LOGGING = "batch.instance_logging";

	/**
	 * BI_ERROR_MESSAGE {@link String} is a constants used as a key in the hashmap for storing the batch instance error message when
	 * enhanced error logging switch is on.
	 */
	String BI_ERROR_MESSAGE = "errorMessage";

	/**
	 * BI_LOG_FILE_PATH {@link String} is a constants used as a key in the hashmap for storing the batch instance log file path when
	 * enhanced error logging switch is on.
	 */
	String BI_LOG_FILE_PATH = "logFilePath";

	/**
	 * LOG_FOLDER {@link String} is a constants used for showing the name of the log files folder.
	 */
	String LOG_FOLDER = "logs";

	/**
	 * TICKET_TEXT_BOX {@link String} is a constant for textBoxTicket.
	 */
	String TICKET_TEXT_BOX = "textBoxTicket";

	/**
	 * zipFolderPath {@link String} the path to copy log files.
	 */
	String ZIP_FOLDER_PATH = "zipFolderPath";

	/**
	 * Constant {@link String} for adding LICENCE_SERVER_FAILOVER_SWITCH value in licence details file while downloading artifacts
	 * through troubleshooting feature.
	 */
	final String LICENCE_SERVER_FAILOVER_SWITCH = "License Server Fail Over Switch";
	
	final String WIDTH_100_PERCENT = "100%";
	
	final String WIDTH_89_PERCENT = "89%";
	
	final String TROUBLESHOOT_INVALID_CREDENTIALS_ERROR_TEXT = "INVALID_CREDENTIALS";
}
