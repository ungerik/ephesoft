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

package com.ephesoft.gxt.batchinstance.client.i18n;

import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;

/**
 * The interface is used to define all the constants used in the Batch Instance page and support internationalization.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see LocaleCommonConstants
 * 
 */
public interface BatchInstanceConstants extends LocaleCommonConstants {

	public static final String DATE_FORMAT = "dd-MMM-yyyy HH:mm:ss";

	public static final String PROPERTIES_DIRECTORY = "properties";

	public static final String PRIORITY = "priority";

	public static final String STATUS = "status";

	public static final String IDENTIFIER = "identifier";

	public static final String NAME = "name";

	public static final String SEMICOLON = ";";

	public static final String DEFAULT_RESTART_MODULE = "Folder Import module";

	/**
	 * ERROR {@link public static final String}.
	 */
	public static final String ERROR = "Error";

	/**
	 * DOWNLOAD_LOG_FILE {@link public static final String}.
	 */
	public static final String DOWNLOAD_LOG_FILE = "download_log_file";

	/**
	 * META_INF {@link public static final String}.
	 */
	public static final String META_INF = "META-INF";

	/**
	 * ERROR_CODE_TEXT {@link public static final String}.
	 */
	public static final String ERROR_CODE_TEXT = "error";

	/**
	 * DCMA_BATCH {@link public static final String}.
	 */
	public static final String DCMA_BATCH = "dcma-batch";

	/**
	 * DOT_PROPERTIES {@link public static final String}.
	 */
	public static final String DOT_PROPERTIES = ".properties";

	/**
	 * Used in restarting batches from current module.
	 */
	public static final String RESTART_CURRENT = "restart_current";
	/**
	 * Used in restarting batches from previous module.
	 */
	public static final String RESTART_PREVIOUS = "restart_previous";
	/**
	 * Used in restarting batches from first module.
	 */
	public static final String RESTART_FIRST = "restart_first";

	public static final String NAVIGATE_TO_BATCH_DETAIL_SCREEN = "navigate_to_batch_detail_screen";

	public static final String QUESTION_MARK = "?";

	public static final String RESTART_BATCH = "restart_batch";

	public static final String DELETE_SELECTED_BATCH = "delete_selected_batch";

	public static final String ERROR_CAUSE = "error_cause";

	public static final String UNC_FOLDER_PATH = "unc_folder_path";

	public static final String CURRENT_STATUS = "current_status";

	public static final String BATCH_PRIORITY = "batch_priority";

	public static final String BATCH_NAME = "batch_name";

	public static final String BATCH_DESCRIPTION = "batch_description";

	public static final String BATCH_IDENTIFIER = "batch_identifier";

	public static final String BATCH_CLASS_NAME_LABEL = "batch_class_name_label";

	public static final String BATCH_DETAIL_FAILURE = "batch_detail_failure";

	public static final String EMPTY_STRING = "";

	public static final String WARNING_TITLE = "warning_title";

	/**
	 * DOUBLE_SLASH {@link public static final String}.
	 */
	public static final String DOUBLE_SLASH = "\\\\";

	public static final String COMMA = ",";

	public static final String BIM_STACK_CHART_TIME = "bim_stack_chart_time";

	public static final String BIM_STACK_CHART_NO_OF_BATCHES = "bim_stack_chart_no_of_batches";

	public static final String DETAILS_GRID_BATCH_ID_TITLE = "details_grid_batch_id_title";

	public static final String DETAILS_GRID_BATCH_STATE_TITLE = "details_grid_batch_state_title";

	public static final String DETAILS_GRID_BATCH_CLASS_NAME_TITLE = "details_grid_batch_class_name_title";

	public static final String DETAILS_GRID_BATCH_UNC_PATH_TITLE = "details_grid_batch_unc_path_title";

	public static final String DETAILS_GRID_BATCH_PRIORITY_TITLE = "details_grid_batch_priority_title";

	public static final String DETAILS_GRID_BATCH_NAME_TITLE = "details_grid_batch_name_title";

	public static final String DETAILS_GRID_BATCH_ERROR_CAUSE_TITLE = "details_grid_batch_error_cause_title";

	public static final String NO_PLUGINS_FOR_EXECUTION = "no_plugins_for_execution";

	public static final String CURRENTLY_EXECUTING_PLUGINS = "currently_executing_plugins";

	public static final String EXECUTED_PLUGINS = "executed_plugins";

	public static final String PENDING_EXECUTION_PLUGINS = "pending_execution_plugins";

	public static final String TROUBLESHOOT_HEADER = "troubleshoot_header";

	public static final String BATCH_INSTANCE_CATEGORIES_HEADER = "batch_instance_categories_header";

	public static final String REVIEW_VALIDATION_BACKLOG_HEADER = "review_validation_backlog_header";

	public static final String OPEN_MENU_ITEM_TITLE = "open_menu_item_title";

	public static final String UNLOCK_MENU_ITEM_TITLE = "unlock_menu_item_title";

	public static final String DELETE_MENU_ITEM_TITLE = "delete_menu_item_title";

	public static final String RESTART_MENU_ITEM_TITLE = "restart_menu_item_title";

	public static final String RESTART_AT_CURRENT_MODULE_MENU_ITEM_TITLE = "restart_at_current_module_menu_item_title";

	public static final String RESTART_AT_PREVIOUS_MODULE_MENU_ITEM_TITLE = "restart_at_previous_module_menu_item_title";

	public static final String RESTART_AT_FIRST_MODULE_MENU_ITEM_TITLE = "restart_at_first_module_menu_item_title";

	public static final String RESTART_AT_SELECTED_MODULE_MENU_ITEM_TITLE = "restart_at_selected_module_menu_item_title";

	public static final String BATCH_INSTANCE_BOTTOM_PANEL_HEADER = "batch_instance_bottom_panel_header";

	public static final String TROUBLESHOOT_DOWNLOAD_LABEL = "troubleshoot_download_label";

	public static final String TROUBLESHOOT_DOWNLOAD_TO_LABEL = "troubleshoot_download_to_label";

	public static final String TROUBLESHOOT_UPLOAD_TO_LABEL = "troubleshoot_upload_to_label";

	public static final String TROUBLESHOOT_SELECT_ALL_TITLE = "troubleshoot_select_all_title";

	public static final String TROUBLESHOOT_PATH_TITLE = "troubleshoot_path_title";

	public static final String TROUBLESHOOT_USERNAME_TITLE = "troubleshoot_username_title";

	public static final String TROUBLESHOOT_PASSWORD_TITLE = "troubleshoot_password_title";

	public static final String TROUBLESHOOT_SERVER_URL_TITLE = "troubleshoot_server_url_title";

	public static final String TROUBLESHOOT_TICKET_ID_TITLE = "troubleshoot_ticket_id_title";

	public static final String TROUBLESHOOT_ARTIFACT_BATCH_CLASS = "troubleshoot_artifact_batch_class";

	public static final String TROUBLESHOOT_ARTIFACT_APPLICATION = "troubleshoot_artifact_application";

	public static final String TROUBLESHOOT_ARTIFACT_LOGS = "troubleshoot_artifact_logs";

	public static final String TROUBLESHOOT_ARTIFACT_DEFAULT_BATCH_CLASS = "troubleshoot_artifact_default_batch_class";

	public static final String TROUBLESHOOT_ARTIFACT_IMAGE_CLASSIFICATION = "troubleshoot_artifact_image_classification";

	public static final String TROUBLESHOOT_ARTIFACT_LUCENE_CLASSIFICATION = "troubleshoot_artifact_lucene_classification";

	public static final String TROUBLESHOOT_ARTIFACT_LIB = "troubleshoot_artifact_lib";

	public static final String TROUBLESHOOT_ARTIFACT_META_INF = "troubleshoot_artifact_meta_inf";

	public static final String TROUBLESHOOT_ARTIFACT_APPLICATION_FOLDER = "troubleshoot_artifact_application_folder";

	public static final String TROUBLESHOOT_ARTIFACT_APPLICATION_LOGS = "troubleshoot_artifact_application_logs";

	public static final String TROUBLESHOOT_ARTIFACT_JAVA_APPSERVER_LOGS = "troubleshoot_artifact_java_appserver_logs";

	public static final String TROUBLESHOOT_ARTIFACT_BATCH_INSTANCE_LOGS = "troubleshoot_artifact_batch_instance_logs";

	public static final String TROUBLESHOOT_ARTIFACT_BATCH_INSTANCE_FOLDER = "troubleshoot_artifact_batch_instance_folder";

	public static final String TROUBLESHOOT_ARTIFACT_DATABASE_DUMP = "troubleshoot_artifact_database_dump";

	public static final String TROUBLESHOOT_ARTIFACT_UNC_FOLDER = "troubleshoot_artifact_unc_folder";

	public static final String TROUBLESHOOT_ARTIFACT_LICENSE_DETAILS = "troubleshoot_artifact_license_details";

	public static final String RESTART_BATCHES = "Restart Batches";
}
