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

package com.ephesoft.gxt.admin.client.i18n;

import com.ephesoft.gxt.core.client.i18n.LocaleCommonMessages;

/**
 * The interface is used to define all the messages used in the Batch Class UI
 * and support internationalization.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see LocaleCommonMessages
 * 
 */
public final class BatchClassMessages implements LocaleCommonMessages {

	public static final String SELECT_BATCHES_FOR_RESTARTING = "select_batches_for_restarting";
	public static final String PERSISTANCE_ERROR = "persistance_error";
	/**
	 * {@link String} The message to be shown when batch class selected is
	 * already deleted.
	 */
	public static final String BATCH_CLASS_ALREADY_DELETED = "batch_class_already_deleted";
	public static final String UNABLE_TO_ACQUIRE_LOCK = "unable_to_acquire_lock";
	public static final String BATCH_CLASSES_UNLOCK_SUCCESS = "batch_class_unlock_success";
	public static final String PARTIAL_BATCH_CLASSES_UNLOCK_SUCCESS = "partial_batch_class_unlock_success";
	public static final String BATCH_CLASSES_UNLOCK_FAILURE_MESSAGE = "batch_class_unlock_failure_message";
	public static final String SELECT_BATCH_CLASSES_UNLOCK_MESSAGE = "select_batch_class_unlock_message";
	public static final String BATCH_CLASSES_DELETION_SUCCESS = "batch_class_deletion_success";
	public static final String PARTIAL_BATCH_CLASSES_DELETION_SUCCESS = "partial_batch_class_deleteion_success";
	public static final String BATCH_CLASSES_DELETION_FAILED_MESSAGE = "batch_class_deletion_fail_message";
	public static final String SELECT_BATCH_CLASSES_DELETE_MESSAGE = "select_batch_class_deletion_message";
	public static final String NO_BATCH_CLASS_SELECTED_FOR_PROCESSING = "no_batch_class_deletion_message";

	public static final String BATCH_CLASS_OPEN_MESSAGE = "batch_class_open_message";

	/**
	 * {@link String} error message to be displayed if key could not be
	 * generated for the batch class.
	 */
	public static final String PROBLEM_GENERATING_KEY = "problem_generating_key";
	/**
	 * {@link String} Error message for empty batch class key entered.
	 */
	public static final String BATCH_CLASS_KEY_EMPTY = "batch_class_key_empty";
	/**
	 * {@link String} Success message for batch class key generated.
	 */
	public static final String BATCH_CLASS_KEY_SUCCESS = "batch_class_key_success";
	/**
	 * {@link String} error message for batch class key generated fuzzy issue.
	 */
	public static final String FUZZY_INDEX_ERROR = "fuzzy_index_error";
	/**
	 * {@link String} error message for batch class imported but fuzzy issue.
	 */
	public static final String FUZZY_INDEX_IMPORT_ERROR = "fuzzy_index_import_error";

	public static final String MANDATORY_FIELDS_ERROR_MSG = "mandatory_fields_error_msg";
	public static final String INVALID_CHARACTER_IN_NAME_MESSAGE = "invalid_character_message";
	public static final String BATCH_IN_NON_FINISHED_STATE = "batch_in_non_finished_state";
	public static final String BATCH_CLASS_EXPORT_SUCCESSFULLY = "batch_class_export_successfully";
	/**
	 * The Grid workflow check box's title's locale representation to be used on
	 * UI.
	 */
	public static final String GRID_WORKFLOW_CHECK_BOX_TITLE = "grid_workflow_check_box_title";
	public static final String UNABLE_TO_CREATE_COPY = "unable_to_create_copy";
	public static final String COPY_SUCCESSFUL = "copy_successfull";
	/**
	 * String constant to show message if UNC folder path is not empty.
	 */
	public static final String UNC_PATH_NOT_EMPTY = "unc_path_not_empty";
	public static final String UNC_PATH_NOT_VERIFIED = "unc_path_not_verified";
	public static final String UNC_PATH_NOT_UNIQUE = "unc_path_not_unique";
	public static final String BATCH_CLASS_NAME_NOT_UNIQUE = "batch_class_name_not_unique";
	public static final String EXPORT_UNSUCCESSFUL = "export_unsuccessful";

	public static final String PRIORITY_SHOULD_BE_NUMERIC = "priority_should_be_numeric";
	public static final String PRIORITY_SHOULD_BE_BETWEEN_1_AND_100 = "priority_should_be_between_1_to_100";
	public static final String BATCH_CLASS_NAME_ERROR = "batch_class_name_error";

	/**
	 * {@link String} Error message for batch class unlocked by super user.
	 */
	public static final String BATCH_CLASS_UNLOCKED_BY_SUPER_USER = "batch_class_unlocked_by_super_user";

	/**
	 * {@link String} Error message for duplicate emails exist.
	 */
	public static final String DUPLICATE_EMAIL_ERR_MSG = "duplicate_email_err_msg";

	/**
	 * {@link String} Error message for port number.
	 */
	public static final String PORT_NUMBER_ERROR = "port_number_error";

	/**
	 * {@link String} Error message for nothing to save.
	 */
	public static final String NOTHING_TO_SAVE_MSG = "nothing_to_save_msg";

	/**
	 * {@link String} Message for no record exist.
	 */
	public static final String NO_RECORD_EXIST_MSG = "no_record_exist_msg";

	/**
	 * {@link String} Message for no record to test in test email configuration
	 * functionality.
	 */
	public static final String NO_RECORD_TO_TEST_MSG = "no_record_to_test_msg";

	/**
	 * {@link String} Message for select only one row.
	 */
	public static final String SELECT_ONE_ROW_ONLY_MSG = "select_one_row_only_msg";

	/**
	 * {@link String} Error message for email connection refused.
	 */
	public static final String UNABLE_TO_CONNECT_EMAIL_ERR_MSG = "unable_to_connect_email_err_msg";

	/**
	 * {@link String} Success message for email validated successfully.
	 */
	public static final String EMAIL_VALIDATED_SUCCESSFULLY = "email_validated_successfully";

	/**
	 * {@link String} Warning message for selected emails deletion.
	 */
	public static final String DELETE_THE_SELECTED_EMAILS_MSG = "delete_the_selected_emails_msg";

	/**
	 * {@link String} Message for no row selected.
	 */
	public static final String NO_ROW_SELECTED = "no_row_selected";

	/**
	 * {@link String} Success Message for batch class updated successfully.
	 */
	public static final String BATCH_CLASS_UPDATED_SUCCESSFULLY = "batch_class_updated_successfully";

	/**
	 * {@link String} Error Message on saving batch class.
	 */
	public static final String BATCH_CLASS_ON_SAVE_ERR_MSG = "batch_class_on_save_err_msg";

	/**
	 * {@link String} Error Message for positive numeric values.
	 */
	public static final String NUMERIC_VALUES_POSITIVE_ERROR_MSG = "numeric_values_positive_error_msg";

	/**
	 * {@link String} Error Message for duplicate scanner profile.
	 */
	public static final String DUPLICATE_SCANNER_PROFILE_ERR_MSG = "duplicate_scanner_profile_err_msg";

	/**
	 * {@link String} Error message for field order number.
	 */
	public static final String FIELD_ORDER_NUMBER_ERR_MSG = "field_order_number_err_msg";

	/**
	 * {@link String} Error message for regex pattern.
	 */
	public static final String REGEX_PATTERN_ERR_MSG = "regex_pattern_err_msg";

	/**
	 * {@link String} Error message for duplicate emails exist.
	 */
	public static final String DUPLICATE_BATCH_CLASS_FIELD_ERR_MSG = "duplicate_batch_class_field_err_msg";

	/**
	 * {@link String} Warning message for selected function keys deletion.
	 */
	public static final String DELETE_FUNCTION_KEYS_MSG = "delete_function_keys_msg";

	/**
	 * {@link String} Error message for duplicate function keys exist.
	 */
	public static final String DUPLICATE_FUNCTION_KEYS_ERR_MSG = "duplicate_function_keys_err_msg";

	/**
	 * {@link String} Error message for current view validation failure.
	 */
	public static final String CURRENT_VIEW_VALIDATION_FAILURE_ERR_MSG = "current_view_not_validated_err_msg";

	/**
	 * {@link String} confirmation message for deleting selected tables.
	 */
	public static final String DELETE_TABLE_INFO_MSG = "delete_table_info_msg";

	/**
	 * {@link String} Error message for number of rows of table.
	 */
	public static final String TABLE_NO_OF_ROWS_ERR_MSG = "table_no_of_rows_err_msg";

	/**
	 * {@link String} Error message for duplicate table name exist.
	 */
	public static final String DUPLICATE_TABLE_NAME_ERR_MSG = "duplicate_table_name_err_msg";

	/**
	 * {@link String} confirmation message for deleting selected table columns.
	 */
	public static final String DELETE_TABLE_COLUMNS_MSG = "delete_table_columns_msg";

	/**
	 * {@link String} confirmation message for deleting selected table columns
	 * with column extraction rule mapping.
	 */
	public static final String DELETE_TABLE_COLUMNS_WITH_MAPPING_MSG = "delete_table_columns_with_mapping_msg";

	/**
	 * {@link String} Error message for duplicate table column name exist.
	 */
	public static final String DUPLICATE_TABLE_COLUMN_ERR_MSG = "duplicate_table_column_err_msg";

	/**
	 * {@link String} Warning message for selected scanner configuration
	 * deletion.
	 */
	public static final String DELETE_SCANNNER_CONFIGS_MSG = "delete_scanner_configs_msg";

	/**
	 * {@link String} Warning message for selected batch class fields deletion.
	 */
	public static final String DELETE_BATCH_CLASS_FIELDS_MSG = "delete_batch_class_fields_msg";

	/**
	 * {@link String} Warning message for selected cmis configurations deletion.
	 */
	public static final String DELETE_CMIS_CONFIGURATIONS_MSG = "delete_cmis_configurations_msg";

	/**
	 * {@link String} confirmation message for deleting selected table
	 * extraction rules.
	 */
	public static final String DELETE_TABLE_EXTR_RULES_MSG = "delete_table_extr_rules_msg";

	public static final String ERROR_RETRIEVING_PATH = "error_retrieving_path";
	public static final String ERROR_RETRIEVING_PATH_OR_UPLOADING_FILE = "error_retrieving_path_or_uploading_file";
	public static final String ERROR_UPLOAD_IMAGE = "error_upload_image";
	public static final String NO_KEY_FIELD_SELECTED = "no_key_field_selected";
	public static final String MANDATORY_FIELDS_BLANK = "mandatory_fields_cannot_be_blank";
	public static final String VALIDATE_THE_REGEX_PATTERN = "validate_the_regex_pattern";
	public static final String INTEGER_ERROR = "integer_error";
	public static final String WEIGHT_ERROR = "weight_error";
	public static final String KEY_PATTERN_LENGTH_FUZZINESS_ERROR = "key_pattern_fuzziness_error";
	public static final String TITLE_TEST_FAILURE = "title_test_failure";
	public static final String TEST_KV_FAILURE = "test_kv_failure";
	public static final String ADVANCED_KV_ERROR = "advanced_kv_failure";
	public static final String INVALID_KEY_PATTERN = "invalid_key_pattern";
	public static final String INVALID_VALUE_PATTERN = "invalid_value_pattern";
	public static final String ERROR = "error";
	public static final String ERROR_WHILE_UPLOADING = "error_while_uploading";
	public static final String INVALID_INPUT_FILE = "invalid_input_file";
	/**
	 * Module Listing
	 */
	public static final String MODULE_ADDED_SUCCESFULLY = "module_added_succesfully";
	public static final String ERROR_ADDING_NEW_MODULE = "error_adding_new_module";
	public static final String SAVING_MODULES = "saving_modules";
	public static final String APPLY_SUCCESSFUL = "apply_successful";
	public static final String MESSAGE = "message";
	public static final String NO_PLUGIN_SELECTED = "no_plugin_selected";
	public static final String PLUGIN_ALREADY_EXIST = "plugin_already_exist";
	public static final String ADD_ALREADY_EXIST_MODULE = "add_already_existing_module";
	public static final String PLUGINS_ALREADY_EXIST = "plugins_already_exist";
	public static final String WARNING = "warning";
	public static final String HIGHLIGHT_DEPENDENCY_ADD_MESSAGE = "highlight_dependency_and_msg";
	public static final String UNABLE_TO_GET_PLUGINs_LIST = "unable_to_get_plugins_list";
	public static final String DEPLOYING_WORKFLOW = "deploying_workflow";
	public static final String AN_ERROR_OCCURED_WHILE_DEPLOYING_THE_WORKFLOW = "an_error_occured_while_deploying_the_workflow";
	public static final String WORKFLOW_DEPLOYED_SUCCESSFULLY = "workflow_deployed_successfully";
	public static final String DEPENDENCY_VIOLATED = "dependency_violated";
	public static final String OR_TEXT = "or";
	public static final String PLUGIN_MUST_OCCUR_BEFORE = "plugin_must_occur_before";
	public static final String MUST_BE_UNIQUE_IN_THE_WORKFLOW = "must_be_unique_in_the_workflow";

	public static final String NO_EXTRACTION_MODULE_SELECTED = "no_extraction_module_selected_msg";

	public static final String UNABLE_TO_CLEAR_FOLDER = "unable_to_clear_folder";

	public static final String NO_EXTRACTION_MODULE_EXIST = "no_extraction_module_exist_msg";

	/**
	 * String constant message for confirming selection of mandatory option for
	 * the column of table, if required option is used.
	 */
	public static final String SELECT_MANDATORY_OPTION_REQUIRED_SET = "select_mandatory_option_required_set";

	/**
	 * String constant message for confirming selection of required option for
	 * the column of table, if mandatory option is used.
	 */
	public static final String SELECT_REQUIRED_OPTION_MANDATORY_SET = "select_required_option_mandatory_set";

	/**
	 * String constant message for confirming selection of mandatory option for
	 * the column of table, if extract column data from other column is used.
	 */
	public static final String SELECT_MANDATORY_OPTION_EXTRACT_COLUMN_SET = "select_mandatory_option_extract_column_set";

	/**
	 * String constant message for confirming selection of extract column data
	 * from other column option for the column of table, if mandatory is used.
	 */
	public static final String SELECT_EXTRACT_COLUMN_MANDATORY_OPTION_SET = "select_extract_column_mandatory_option_set";

	/**
	 * String constant message for confirming selection of mandatory option for
	 * the column of table, if extract column data from other column and
	 * required are used.
	 */
	public static final String SELECT_MANDATORY_OPTION_EXTRACT_COLUMN_AND_REQUIRED_SET = "select_mandatory_option_extract_column_and_required_set";

	/**
	 * {@link String} Warning message for selected table validation rules
	 * deletion.
	 */
	public static final String DELETE_TABLE_RULES_MSG = "delete_table_rules_msg";

	public static final String DELETE_KV_MSG = "delete_kv_msg";
	public static final String KV_INVALID = "kv_invalid";
	public static final String MODULE_ALREADY_PRESENT = "module_already_present";
	public static final String VALUE_NOT_COMPLIANT_WITH_FIELD_TYPE = "value_not_compliant_with_field_type";
	public static final String INVALID_DATE_FORMAT = "invalid_date_format";
	public static final String VALID_DATE_FORMAT_IS = "valid_date_format_is";
	public static final String NOTHING_TO_DEPLOY = "nothing to deploy";
	public static final String PLEASE_DEPLOY_WORKFLOW = "please_deploy_workflow";
	public static final String NO_RECORD_TO_EDIT = "no_record_to_edit";
	public static final String NO_RESULT_FOUND = "no_result_found";
	public static final String KV_HOCR_FAILURE = "kv_hocr_failure";

	public static final String DELETE_EMAILS_SUCCESS_MSG = "delete_emails_success_msg";

	public static final String DELETE_SCANNER_PROFILES_SUCCESS_MSG = "delete_scanner_profiles_success_msg";

	public static final String DELETE_CMIS_CONFIGS_SUCCESS_MSG = "delete_cmis_configs_success_msg";

	public static final String DELETE_BATCH_CLASS_FIELDS_SUCCESS_MSG = "delete_batch_class_fields_success_msg";

	public static final String DELETE_TABLES_SUCCESS_MSG = "delete_tables_success_msg";

	public static final String DELETE_FUNCTION_KEYS_SUCCESS_MSG = "delete_function_keys_success_msg";

	public static final String DELETE_TABLE_COLUMNS_SUCCESS_MSG = "delete_table_columns_success_msg";

	public static final String DELETE_TABLE_EXTR_RULES_SUCCESS_MSG = "delete_table_extr_rules_success_msg";

	public static final String DELETE_TABLE_RULES_SUCCESS_MSG = "delete_table_rules_success_msg";

	public static final String NO_COLUMN_EXIST_IN_TABLE_MSG = "no_column_exist_in_table_msg";

	public static final String TEST_TABLE_FAILURE_MSG = "test_table_failure_msg";

	public static final String NOTHING_TO_SHOW = "nothing_to_display";
	public static final String ERROR_RETRIEVING_PAGE_COUNT = "error_retrieving_page_count";
	public static final String PLUGIN_ADDITION = "plugin_addition";
	public static final String DELETE_PLUGIN = "delete_plugin";
	/**
	 * Constant for showing message for clearing the classification folder.
	 */
	public static final String CLEAR_CONFIRMATION_MESSAGE = "clear_classification_folder_confirmation";
	public static final String ONLY_SINGLE_PAGE_ONE_FILE_SUPPORTED = "only_single_page_one_file_supported";

	public static final String TEST_TABLE_EXTR_RULE_FAILURE_MSG = "test_table_extr_rule_failure_msg";
	public static final String MULTIPLE_FILE_UPLOAD_NOT_SUPPORTED = "multiple_file_upload_not_supported";
	public static final String INVALID_INPUT_FILE_FORMAT = "invalid_input_file_format";

	/**
	 * {@link String} error message for invalid email.
	 */
	public static final String INVALID_EMAIL_MSG = "invalid_email_msg";

	/** The Constant ONLY_ONE_RECORD_CAN_BE_OPENED_AT_TIME. */
	public static final String ONLY_ONE_RECORD_CAN_BE_OPENED_AT_TIME = "only_one_record_can_be_opened_at_time";

	/** The Constant TEST_AN_EMAIL. */
	public static final String TEST_AN_EMAIL = "test_an_email";

	/** The Constant COPY_BATCH_CLASS. */
	public static final String COPY_BATCH_CLASS = "copy_batch_class";

	/** The Constant EXPORT_BATCH_CLASS. */
	public static final String EXPORT_BATCH_CLASS = "export_batch_class";

	/** The Constant COPY_DOCUMENT_TYPE. */
	public static final String COPY_DOCUMENT_TYPE = "copy_document_type";

	/** The Constant COPY_INDEX_FIELD. */
	public static final String COPY_INDEX_FIELD = "copy_index_field";

	/** The Constant EDIT_KV_EXTRACTION. */
	public static final String EDIT_KV_EXTRACTION = "edit_kv_extraction";

	/** The Constant EDIT_SET_COORDINATES. */
	public static final String EDIT_SET_COORDINATES = "edit_set_coordinates";

	/** The Constant TEST_TABLE. */
	public static final String TEST_TABLE = "test_table";

	/** The Constant TEST_TABLE_COLUMN_EXTR_RULE. */
	public static final String TEST_TABLE_COLUMN_EXTR_RULE = "test_table_column_extr_rule";
	public static final String INVALID_VIEW = "invalid_view";

	/** Regex Pool messages **/

	/**
	 * Message shown when some field is expected to be selected.
	 */
	public static final String NONE_SELECTED_WARNING = "none_selected_warning";

	/**
	 * String constant to confirmation message on deletion of a regex group.
	 */
	public static final String DELETE_REGEX_GROUP = "delete_regex_group";

	/**
	 * String constant for error message on regex pattern configuration.
	 */
	public static final String UNABLE_TO_READ_REGEX_GROUPS = "regex_group_fail";

	/**
	 * Message when there are no regex groups of a type.
	 */
	public static final String NO_REGEX_GROUPS = "no_regex_groups";

	/**
	 * String constant for error message on regex pattern selection.
	 */
	public static final String UNABLE_TO_SELECT_REGEX_PATTERNS = "regex_pattern_select_fail";

	/**
	 * String constant for error message on regex pattern configuration.
	 */
	public static final String UNABLE_TO_READ_REGEX_PATTERNS = "regex_pattern_fail";

	/**
	 * Message when there are no regex patterns in a group.
	 */
	public static final String NO_REGEX_PATTERNS = "no_regex_patterns";

	/**
	 * String constant to confirmation message on deletion of a regex pattern.
	 */
	public static final String DELETE_REGEX_PATTERN = "delete_regex_pattern";

	/**
	 * Error message shown when duplicate name is entered.
	 */
	public static final String NAME_COMMON_ERROR = "name_common_error";

	/**
	 * Message shown when regex group addition is expected.
	 */
	public static final String ADD_GROUP_INFO = "add_group_info";

	/**
	 * Error message shown when duplicate name is entered.
	 */
	public static final String PATTERN_COMMON_ERROR = "pattern_common_error";

	// public static final String VALIDATE_THE_REGEX_PATTERN =
	// "validate_the_regex_pattern";

	// public static final String MANDATORY_FIELDS_BLANK =
	// "mandatory_fields_cannot_be_blank";

	public static final String REGEX_PATTERN_UPDATE_FAILED = "Regex Pattern Update Failed.";

	public static final String REGEX_GROUP_UPDATE_FAILED = "Regex Group Update Failed.";

	public static final String UPDATION_FAILED = "Updation Failed";

	public static final String INSERTION_FAILED = "Insertion Failed";

	public static final String SOME_REGEX_PATTERN_DELETED_SUCCESSFULLY = "Selected Regex Patterns have been deleted successfully.";
	public static final String DELETE_SELECTED_REGEX_PATTERN = "Delete Selected Regex Pattern(s)";

	public static final String SELECTED_REGEX_PATTERN_DELETED_SUCCESSFULLY = "Selected Regex Pattern(s) have been deleted successfully.";

	public static final String SOME_REGEX_GROUP_DELETED_SUCCESSFULLY = "Selected Regex Groups have been deleted successfully.";
	public static final String DELETE_SELECTED_REGEX_GROUP = "Delete Selected Regex Group(s)";

	public static final String SELECTED_REGEX_GROUP_DELETED_SUCCESSFULLY = "Selected Regex Group(s) have been deleted successfully.";

	public static final String PLEASE_SOME_REGEX_GROUP_TO_OPEN = "please_select_regex_group_to_open";

	public static final String PLEASE_SOME_REGEX_PATTERN_TO_DELETE = "Please select Regex Pattern to delete.";

	public static final String PLEASE_SOME_REGEX_GROUP_TO_DELETE = "Please select Regex Group to delete.";

	public static final String REGEX_GROUP_INSERTION_FAILED = "Regex Group Insertion Failed.";

	public static final String REGEX_PATTERN_INSERTION_FAILED = "Regex Pattern Insertion Failed.";

	public static final String CONNECTION_WITH_SAME_NAME_ALREADY_EXISTS = "connection_with_same_name_already_exists";

	public static final String PLEASE_GIVE_VALID_REGEX_GROUP_NAME = "Please give valid Regex Group name";

	public static final String PLEASE_GIVE_VALID_REGEX_GROUP_NAME_NULL = "Please give valid Regex Group name is null";

	public static final String INVALID_INPUT = "Invalid Input";

	public static final String INVALID_GENERATED_PATTERN = "Generated Regex Pattern is Invalid";
	public static final String NO_ROW_SELECTED_SET_COORDINATE = "no_row_selected_set_coordinate";
	public static final String INVALID_CHARACTER_IN_BCNAME_MESSAGE = "invalid_character_msg";
	public static final String SELECT_INDEX_FIELD_TO_DELETE = "select_index_field_to_delete";
	public static final String SURE_TO_DELETE_INDEX_FIELD = "sure_to_delete_index_field";

	public static final String INVALID_BIT_DEPTH_MSG = "invalid_bit_depth_msg";

	public static final String INVALID_BLANK_PAGE_THRESHOLD_MSG = "invalid_blank_page_threshold_msg";

	public static final String INVALID_PAGE_CACHE_CLEAR_COUNT_MSG = "invalid_page_cache_clear_count_msg";

	public static final String NO_RECORDS_FOUND = "no_records_found";

	public static final String DELETE_VALIDATION_RULES_EXIST_ERR_MSG = "delete_validation_rules_exist_err_msg";
	public static final String CLEANUP_PLUGIN_DOES_NOT_EXIST = "cleanup_plugin_does_not_exist";
	public static final String ENCRYPT_BATCH_CLASS = "encrypt_batch_class";

	/** end Regex Pool messages **/

	public static final String EDIT_TABLE_COLUMN_CONFIRMATION_MSG = "edit_table_column_confirmation_msg";

	public static final String TEST_TABLE_VALIDATE_GRID_ERR_MSG = "test_table_validate_grid_err_msg";

	public static final String DELETE_SELECTED_VALIDATION_RULES = "delete_selected_validation_rules";

	public static final String LOADING_INDEX_FIELD_FROM_SERVER = "loading_index_field_from_server";

	public static final String LOCKED_BY_SOMEONE = "locked_by_someone";

	public static final String DB_EXPORT_RECORD_DELETE_CONFIRMATION = "db_export_record_delete_confirmation";
	public static final String SELECT_ROW_TO_DELETE = "select_row_to_delete";
	public static final String RESET_MAPPING_CONFIRMATION = "reset_mapping_confirmation";
	public static final String FILES_IMPORTED_SUCCESSFULLY = "files_imported_successfully";
	public static final String UNABLE_TO_UPLOAD = "unable_to_upload";
	public static final String FILES_UNSUPPORTED_FORMAT = "files_unsupported_format";
	public static final String SHOW_FILE_LIST = "show_file_list";
	public static final String UNSUPPORTED_FILE_LIST = "unsupported_file_list";

	public static final String UNSAVED_BATCH_CLASS_SAVE_NEEDED = "unsaved_batch_class_save_needed";
	public static final String VALIDATE_FIELDS_FIRST_TO_NAVIGATE = "validate_fields_first_to_navigate";
	public static final String UNABLE_TO_LOAD_ROLES = "unable_to_load_roles";
	public static final String PRIMARY_BATCH_CLASSES_CANNOT_BE_DELETED = "primary_batch_classes_cannot_be_deleted";
	public static final String UNABLE_TO_UPDATE_BATCH_CLASS_INFO = "unable_to_update_batch_class_info";
	public static final String ERROR_CODE_1 = "update_batch_class_error_code_1";
	public static final String ERROR_CODE_2 = "update_batch_class_error_code_2";
	public static final String PLEASE_VALIDATE_BATCH_CLASS = "please_validate_batch_class";
	public static final String BATCH_CLASS_VIEW_INVALID = "batch_class_view_invalid";
	public static final String BATCH_CLASS_COPIED_SUCCESSFULLY = "batch_class_copied_successfully";
	public static final String ERROR_WHILE_FETCHING_CHARACTER_LIST_FOR_BATCH_CLASS_NAME = "error_while_fetching_character_list_for_batch_class_name";
	public static final String BATCH_CLASS_NAME_SHOULD_BE_UNIQUE = "batch_class_name_should_be_unique";
	public static final String BATCH_CLASS_CANNNOT_BE_IMPORTED = "batch_class_cannot_be_imported";
	public static final String ERROR_WHILE_IMPORTING_BATCH = "error_while_importing_batch";
	public static final String BATCH_CLASS_IMPORTED_SUCCESSFULLY = "batch_class_imported_successfully";
	public static final String BATCH_CLASS_IMPORTED_SUCCESSFULLY_ERROR_IN_GENERATING_BC_KEY = "batch_class_imported_successfully_error_in_generating_bc_key";
	public static final String BATCH_CLASS_IMPORTED_SUCCESSFULLY_ERROR_IN_GENERATING_FUZZY_INDEX = "batch_class_imported_successfully_error_in_generating_fuzzy_index";
	public static final String CANNOT_ADD_MORE_FIELDS_VALIDATE_VIEW_FIRST = "cannot_add_more_fields_validate_view_first";
	public static final String PLEASE_SAVE_PENDING_CHANGES_FIRST = "please_save_pending_changes_first";
	public static final String PLEASE_SELECT_DOCUMENT_TYPE_TO_VIEW_LEARN_FILES = "please_select_document_type_to_view_learn_files";
	public static final String PLEASE_SELECT_ONLY_ONE_DOCUMENT_TYPE_TO_VIEW_LEARN_FILES = "please_select_only_one_document_type_to_view_learn_files";
	public static final String DOCUMENT_TYPE_IMPORTED_SUCCESSFULLY = "document_type_imported_successfully";
	public static final String SURE_TO_DELETE_DOCUMENT_TYPE = "sure_to_delete_document_type";
	public static final String DOCUMENT_TYPE_DELETED_SUCCESSFULLY = "document_type_deleted_successfully";
	public static final String FUZZYDB_MAPPING_DELETED_SUCCESSFULLY = "fuzzy_db_deleted_successfully";
	public static final String DOCUMENT_TYPE_COPIED_SUCCESSFULLY = "document_type_copied_successfully";
	public static final String SELECT_DOCUMENT_TYPE_TO_COPY = "select_document_type_to_copy";
	public static final String SELECT_INDEX_LEVEL_FIELD_TO_DELETE = "select_index_level_field_to_delete";
	public static final String PLEASE_WAIT_LEARNING_FILES = "please_wait_learning_files";
	public static final String LEARNING_FAILED_FOR_FEW_SAMPLE_FILES_PLEASE_REFER_APPLICATION_LOGS = "learning_failed_for_few_sample_files_please_refer_application_logs";
	public static final String FILES_LEARNED_SUCCESSFULLY = "files_learned_successfully";
	public static final String UNABLE_TO_GET_BATCH_CLASS_INFORMATION = "unable_to_get_batch_class_information";
	public static final String NO_DOCUMENT_TYPE_EXIST = "no_document_type_exist";
	public static final String ERROR_WHILE_IMPORTING_DOCUMENT_TYPES = "error_while_importing_document_types";
	public static final String ERROR_WHILE_IMPORTING_SOME_DOCUMENT_TYPES = "error_while_importing_some_document_types";
	public static final String SELECT_AT_LEAST_ONE_DOCUMENT_TYPE_TO_EXPORT = "select_at_least_one_document_type_to_export";
	public static final String UNABLE_TO_GET_LEARN_FILE_INFORMATION = "unable_to_get_learn_file_information";
	public static final String VALID_INPUT_FILE_FORMATS_IS_ZIP_DISCARDING_INVALID_FILES = "valid_input_file_formats_is_zip_discarding_invalid_files";
	public static final String UNABLE_TO_IMPORT_BATCH_CLASS = "unable_to_import_batch_class";
	public static final String VALUE_CANNOT_BE_EMPTY = "value_cannot_be_empty";
	public static final String ATLEAST_ONE_CHKBOX_SELECTED = "select_any_isSearchable_checkbox";
	public static final String BATCH_CLASS_DESCRIPTION_CANNOT_BE_EMPTY = "batch_class_description_cannot_be_empty";
	public static final String BATCH_CLASS_NAME_CANNOT_BE_EMPTY = "batch_class_name_cannot_be_empty";
	public static final String UNC_PATH_CANNOT_BE_EMPTY = "unc_path_cannot_be_empty";
	public static final String UNC_FOLDER_CANNOT_BE_EMPTY = "unc_folder_cannot_be_empty";
	public static final String ENCRYPTION_KEY_CANNOT_BE_EMPTY_WHEN_ENCRYPTION_ALGORITHM_IS_SELECTED = "encryption_key_cannot_be_empty_when_encryption_algorithm_is_selected";
	public static final String BATCH_CLASS_NAME_CANNOT_CONTAIN_SPACE_OR_HYPHEN = "batch_class_name_cannot_contain_space_or_hyphen";
	public static final String BATCH_CLASS_PPM_COMPARISON = "batch_class_ppm_comparison";
	public static final String PPM_OF_SELECTED_BATCH_CLASS = "ppm_of_selected_batch_class";
	public static final String MINIMUM_CONFIDENCE_THRESHOLD_SHOULD_BE_BETWEEN_0_AND_100 = "minimum_confidence_threshold_should_be_between_0_and_100";
	public static final String FILE_UPLOADED_SUCCESSFULLY_FOR_EXTRACTION = "file_uploaded_successfully_for_extraction";
	public static final String SELECT_DOCUMENT_TYPE_TO_UPLOAD_FILES_FOR_THEIR_LEARNING = "select_document_type_to_upload_files_for_their_learning";
	public static final String FILE_UPLOADED_SUCCESSFULLY_FOR_CLASSIFICATION = "file_uploaded_successfully_for_classification";
	public static final String UNABLE_TO_UPLOAD_FILE_UNSUPPORTED_FORMAT = "unable_to_upload_file_unsupported_format";
	public static final String PLEASE_SAVE_PENDING_CHANGES_FIRST_TO_LEARN_FILE_IN_A_DOCUMENT = "please_save_pending_changes_first_to_learn_file_in_a_document";
	public static final String DOWNLOAD_UNSUCCESSFUL_PLEASE_CHECK_LOGS_FOR_MORE_INFORMATION = "download_unsuccessful_please_check_logs_for_more_information";
	public static final String ERROR_WHILE_PERFORMING_EXTRACTION = "error_while_performing_extraction";
	public static final String SURE_YOU_WANT_TO_CLEAR_CONTENTS_OF_TEST_EXTRACTION_FOLDER = "sure_you_want_to_clear_contents_of_test_extraction_folder";
	public static final String SURE_YOU_WANT_TO_RESET_FUZZY_DB_MAPPINGS = "sure_you_want_to_reset_fuzzy_db_mappings";
	public static final String UNABLE_TO_FETCH_CONNECTIONS = "unable_to_fetch_connections";
	public static final String UNABLE_TO_CONNECT_TO_THE_DATABASE = "unable_to_connect_to_the_database";
	public static final String UNABLE_TO_FETCH_COLUMNS_FOR_TABLE = "unable_to_fetch_columns_for_table";
	public static final String PLEASE_SELECT_A_CONNECTION_FIRST = "please_select_a_connection_first";
	public static final String PLEASE_SELECT_A_TABLE_FIRST = "please_select_a_table_first";
	public static final String PLEASE_SELECT_A_ROW_ID_FIRST = "please_select_a_row_id_first";
	public static final String NO_RECORDS_TO_DELETE = "no_records_to_delete";
	public static final String PLEASE_SELECT_MAPPINGS_TO_DELETE_FIRST = "please_select_mappings_to_delete_first";
	public static final String SURE_YOU_WANT_TO_DELETE_THE_SELECTED_MAPPINGS = "sure_you_want_to_delete_the_selected_mappings";
	public static final String NO_RECORDS_TO_RESET = "no_records_to_reset";
	public static final String UNABLE_TO_FETCH_PRIMARY_KEYS_FOR_SELECTED_TABLE = "unable_to_fetch_primary_keys_for_selected_table";
	public static final String DATABASE_LEARNED_SUCCESSFULLY = "database_learned_successfully";
	public static final String UNABLE_TO_LEARN_DATABASE = "unable_to_learn_database";
	public static final String OCR_CONFIDENCE_THRESHOLD_SHOULD_BE_BETWEEN_0_AND_100 = "ocr_confidence_threshold_should_be_between_0_and_100";
	public static final String CATEGORY_VALUE_SHOULD_NOT_BE_GREATER_THAN_256_CHARACTERS = "category_value_should_not_be_greater_than_256_characters";
	public static final String INVALID_ZIP_FILE = "invalid_zip_file";
	public static final String FAILED_IMPORTS_OF_INDEX_FIELD = "failed_imports_of_index_field";
	public static final String FAILURE_CAUSE_INDEX_FIELD_ALREADY_EXISTS = "failure_cause_index_field_already_exists";
	public static final String FAILURE_CAUSE_INDEX_FIELD_IMPORT_FAILED_CHECK_LOGS = "failure_cause_index_field_import_failed_check_logs";
	public static final String FAILURE_CAUSE_UNABLE_TO_DESERIALIZE_THE_INDEX_FIELD = "failure_cause_unable_to_deserialize_the_index_field";
	public static final String FAILED_TO_IMPORT_INDEX_FIELD = "failed_to_import_index_field";
	public static final String LOADING_DOCUMENT_TYPE_FROM_SERVER = "loading_document_type_from_server";
	public static final String INDEX_FIELD_DELETED_SUCESSFULLY = "index_field_deleted_sucessfully";
	public static final String PLEASE_SELECT_ATLEAST_ONE_INDEX_FIELDS_TO_EXPORT = "please_select_atleast_one_index_fields_to_export";
	public static final String INDEX_FIELD_COPIED_SUCCESSFULLY = "index_field_copied_successfully";
	public static final String INDEX_FIELD_IMPORTED_SUCCESSFULLY = "index_field_imported_successfully";
	public static final String PLEASE_SELECT_INDEX_FIELDS_TO_EXPORT = "please_select_index_fields_to_export";
	public static final String PLEASE_SAVE_PENDING_CHANGES_FIRST_TO_EXPORT_INDEX_FIELD = "please_save_pending_changes_first_to_export_index_field";
	public static final String ERROR_WHILE_IMPORTING_INDEX_FIELDS = "error_while_importing_index_fields";
	public static final String ERROR_WHILE_IMPORTING_SOME_INDEX_FIELDS = "error_while_importing_some_index_fields";
	public static final String VALIDATE_DOCUMENT_TYPE_FIRST_TO_COPY = "validate_document_type_first_to_copy";
	public static final String VALIDATE_INDEX_FIELD_FIRST_TO_COPY = "validate_index_field_first_to_copy";
	public static final String YOU_HAVE_UNSAVED_CHANGES = "you_have_unsaved_changes";
	public static final String SELECT_DOCUMENT_TYPE_TO_DELETE = "select_document_type_to_delete";
	public static final String SELECT_INDEX_FIELD_TO_COPY = "select_index_field_to_copy";
}
