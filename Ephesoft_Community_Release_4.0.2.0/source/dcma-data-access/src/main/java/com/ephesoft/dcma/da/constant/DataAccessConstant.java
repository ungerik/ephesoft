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

package com.ephesoft.dcma.da.constant;

import java.io.File;

/**
 * This is a common constants file for Data Access plugin.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Apr 13, 2015 <br/>
 * @version 1.0 <br/>
 *          $LastChangedDate: 2015-06-24 11:56:16 +0530 (Wed, 24 Jun 2015) $ <br/>
 *          $LastChangedRevision: 23809 $ <br/>
 */
public interface DataAccessConstant {

	/** The properties ext. */
	String PROPERTIES_EXT = ".properties";

	/** The meta inf. */
	String META_INF = "META-INF";

	/**
	 * Intializing base folder variable.
	 */
	String BASE_FOLDER_LOCATION = "batch.base_folder";

	/** The property file. */
	String PROPERTY_FILE = "db-patch";

	/** The meta inf path. */
	String META_INF_PATH = "META-INF\\dcma-data-access";

	/** The and. */
	String AND = ",";

	/** The or. */
	String OR = "/";

	/** The problem closing stream for file. */
	String PROBLEM_CLOSING_STREAM_FOR_FILE = "Problem closing stream for file :";

	/** The error during de serializing the properties for database upgrade. */
	String ERROR_DURING_DE_SERIALIZING_THE_PROPERTIES_FOR_DATABASE_UPGRADE = "Error during de-serializing the properties for Database Upgrade: ";

	/** The error during reading the serialized file. */
	String ERROR_DURING_READING_THE_SERIALIZED_FILE = "Error during reading the serialized file. ";

	/** The updating batch classes. */
	String UPDATING_BATCH_CLASSES = "Updating Batch Classes....";

	/** The property file delimiter. */
	String PROPERTY_FILE_DELIMITER = ";";

	/** The dependency update. */
	String DEPENDENCY_UPDATE = "DependencyUpdate";

	/** The module config update. */
	String MODULE_CONFIG_UPDATE = "ModuleConfigUpdate";

	/** The plugin config update. */
	String PLUGIN_CONFIG_UPDATE = "PluginConfigUpdate";

	/** The scanner config update. */
	String SCANNER_CONFIG_UPDATE = "ScannerConfigUpdate";

	/** The plugin update. */
	String PLUGIN_UPDATE = "PluginUpdate";

	/** The batch class update. */
	String BATCH_CLASS_UPDATE = "BatchClassUpdate";

	/** The module update. */
	String MODULE_UPDATE = "ModuleUpdate";

	/** The executed. */
	String EXECUTED = "-executed";

	/** The dcma batch properties. */
	String DCMA_BATCH_PROPERTIES = "META-INF/dcma-batch/dcma-batch.properties";

	/** The comma. */
	String COMMA = ",";

	/** The upgrade patch enable. */
	String UPGRADE_PATCH_ENABLE = "upgradePatch.enable";

	/** The upgrade patch default batch class roles. */
	String UPGRADE_PATCH_DEFAULT_BATCH_CLASS_ROLES = "upgradePatch.defaultBatchClassRoles";

	/** The data access dcma db properties. */
	String DATA_ACCESS_DCMA_DB_PROPERTIES = META_INF + "/dcma-data-access/dcma-db" + PROPERTIES_EXT;
	/**
	 * The APPLICATION_PROPERTIES {@link String} is a constant for path to application properties file.
	 */
	String APPLICATION_PROPERTIES = "META-INF/application.properties";

	/**
	 * The SUPER_ADMIN_GROUP_UPDATE {@link String} is a constant for property 'update_super_admin_group' used to set whether the
	 * super-admin group has been updated or not.
	 */
	String UPDATE_SUPER_ADMIN_GROUP = "update_super_admin_group";

	/** The password encrypted property. */
	String PASSWORD_ENCRYPTED_PROPERTY = "password.encrypt";

	/** The encryption properties file. */
	String ENCRYPTION_PROPERTIES_FILE = "dcma-encryption" + File.separator + "dcma-encryption";

	/** The encryption properties file meta inf. */
	String ENCRYPTION_PROPERTIES_FILE_META_INF = META_INF + File.separator + "dcma-encryption" + File.separator + "dcma-encryption"
			+ PROPERTIES_EXT;

	/**
	 * Constant used for column name in a securtiy_user table.
	 */
	String USER_NAME = "user_name";

	/**
	 * Constant used for column name in a securtiy_group table.
	 */
	String GROUP_NAME = "group_name";

	/**
	 * Constant used for column name in a securtiy_group table.
	 */
	String IS_SUPER_ADMIN = "is_super_admin";

	/**
	 * Constant used for representing the column name used for defining fuzzy match threshold value for the key pattern.
	 */
	String KEY_FUZZINESS = "key_fuzziness";

	/**
	 * Constant used for defining table api technique in a table_extraction_api table.
	 */
	String TABLE_API = "table_api";

	/**
	 * Constant used for tabel info id column in a table_extraction_api table.
	 */
	String TABLE_INFO_ID = "table_info_id";

	/**
	 * Constant used for defining the table table_extraction_rule.
	 */
	String TABLE_EXTRACTION_RULE = "table_extraction_rule";

	/**
	 * Constant used for defining the column start_pattern for the table table_extraction_rule.
	 */
	String TABLE_EXTRACTION_START_PATTERN = "start_pattern";

	/**
	 * Constant used for defining the column end_pattern for the table table_extraction_rule.
	 */
	String TABLE_EXTRACTION_END_PATTERN = "end_pattern";

	/**
	 * Constant used for defining the column rule_name for the table table_extraction_rule.
	 */
	String TABLE_EXTRACTION_RULE_NAME = "rule_name";

	/**
	 * Constant used for defining the column column_name for the table table_column_extraction_rule.
	 */
	String COLUMN_NAME = "column_name";

	/**
	 * Constant used for defining the column column_description for the table table_columns_info.
	 */
	String COLUMN_DESCRIPTION = "column_description";

	/**
	 * Constant used for defining the column column_header_pattern for the table table_column_extraction_rule.
	 */
	String COLUMN_HEADER_PATTERN = "column_header_pattern";

	/**
	 * Constant used for defining the column column_start_coordinate for the table table_column_extraction_rule.
	 */
	String COLUMN_START_CORDINATE = "column_start_coordinate";

	/**
	 * Constant used for defining the column column_end_coordinate for the table table_column_extraction_rule.
	 */
	String COLUMN_END_CORDINATE = "column_end_coordinate";

	/**
	 * Constant used for defining the column column_coordinate_y0 for the table table_column_extraction_rule.
	 */
	String COLUMN_CORDINATE_Y0 = "column_coordinate_y0";

	/**
	 * Constant used for defining the column column_coordinate_y1 for the table table_column_extraction_rule.
	 */
	String COLUMN_CORDINATE_Y1 = "column_coordinate_y1";

	/**
	 * Constant used for defining the column column_pattern for the table table_column_extraction_rule.
	 */
	String COLUMN_PATTERN = "column_pattern";

	/**
	 * Constant used for defining the column extracted_column_name for the table table_column_extraction_rule.
	 */
	String EXTRACTED_COLUMN_NAME = "extracted_column_name";

	/**
	 * Constant used for defining the column between_left for the table table_column_extraction_rule.
	 */
	String BETWEEN_LEFT = "between_left";

	/**
	 * Constant used for defining the column between_right for the table table_column_extraction_rule.
	 */
	String BETWEEN_RIGHT = "between_right";

	/**
	 * Constant used for defining the column is_required for the table table_column_extraction_rule.
	 */
	String IS_REQUIRED = "is_required";

	/**
	 * Constant used for defining the column is_mandatory for the table table_column_extraction_rule.
	 */
	String IS_MANDATORY = "is_mandatory";

	/**
	 * Constant used for defining the column sample_values for the table table_column_extraction_rule.
	 */
	String SAMPLE_VALUES = "sample_values";

	/**
	 * Constant used for defining the column order_number for the table table_column_extraction_rule.
	 */
	String ORDER_NUMBER = "order_number";

	/**
	 * Constant used for defining the column is_currency for the table table_column_extraction_rule.
	 */
	String IS_CURRENCY = "is_currency";

	/**
	 * Constant used for defining the identifier for the table table_extraction_rule.
	 */
	String TABLE_EXTRACTION_RULE_ID = "table_extraction_rule_id";

	/**
	 * Constant used for defining the name of table table_column_extraction_rule.
	 */
	String TABLE_COLUMN_EXTRACTION_RULE = "table_column_extraction_rule";

	/**
	 * Constant {@link Long} that indicates the identity of the tuple storing the application key.
	 */
	public static final long APPLICATION_KEY_ENTITY_ID_VALUE = 1L;

	/**
	 * Constant {@link String} that indicates the parameter storing the key for the encryption metadata tupple.
	 */
	public static final String APPLICATION_KEY_ATTRIBUTE_NAME = "id";

	/**
	 * {@link String} constant for queries with like match for any characters of any length.
	 */
	public final String LIKE_QUERY_MATCH_ANYTHING_CONSTANT = "%";

	/**
	 * Constant used for defining the identifier for the table table_columns_info.
	 */
	String TABLE_COLUMNS_INFO_ID = "table_columns_info_id";

	/** Constant used for Copy batch xml plugin. */
	public final String COPY_BATCH_XML = "COPY_BATCH_XML";

	/** Constant used for plugin configuration of Copy Batch XML plugin. */
	public final String BATCH_FOLDER_NAME = "batch.folder_name";

	/** Constant used for plugin configuration of Copy Batch XML plugin. */
	public final String BATCH_MULTIPAGE_EXPORT_FOLDER_PATH = "batch.multipage_export_folder_path";

	/** Constant used for plugin configuration of Copy Batch XML plugin. */
	public final String BATCH_MULTIPAGE_EXPORT_FILE_NAME = "batch.multipage_export_file_name";

	/** Constant used for plugin configuration of Copy Batch XML plugin. */
	public final String BATCH_EXPORT_TO_FOLDER = "batch.export_to_folder";
	
	/** Constant used for plugin configuration of Copy Batch XML plugin. */
	public final String BATCH_FILE_NAME = "batch.file_name";

	/** Constant used for the old batch identifier. */
	public final String OLD_BATCH_IDENTIFIER = "EphesoftBatchID";

	/** Constant used for the new batch identifier. */
	public final String NEW_BATCH_IDENTIFIER = "BATCH_IDENTIFIER";

	/** Constant used for the old doc identifier. */
	public final String OLD_DOC_IDENTIFIER = "EphesoftDOCID";

	/** Constant used for the new doc identifier. */
	public final String NEW_DOC_IDENTIFIER = "DOCUMENT_ID";

	/** Constant used for double ampersand. */
	public final String DOUBLE_AMPERSAND = "&&";

	/** Constant used for single ampersand. */
	public final String AMPERSAND = "&";

	/** Constant used for logging message. */
	public final String WITH_COPY_BATCH_XML_PLUGIN = " with Copy Batch XML plugin configurations.";

	/** Constant used for dummy. */
	public final String DUMMY = "dummy";
}
