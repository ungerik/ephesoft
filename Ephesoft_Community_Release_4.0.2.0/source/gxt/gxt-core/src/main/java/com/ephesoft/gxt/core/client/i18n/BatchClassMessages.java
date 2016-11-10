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

package com.ephesoft.gxt.core.client.i18n;

import com.ephesoft.gxt.core.client.i18n.LocaleCommonMessages;

/**
 * The interface is used to define all the messages used in the Batch Class UI and support internationalization.
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
	 * {@link String} The message to be shown when batch class selected is already deleted.
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
	 * {@link String} error message to be displayed if key could not be generated for the batch class.
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
	public static final String BATCH_IN_NON_FINISHED_STATE = "batch_in_non_finished_state";
	public static final String BATCH_CLASS_EXPORT_SUCCESSFULLY = "batch_class_export_successfully";
	/**
	 * The Grid workflow check box's title's locale representation to be used on UI.
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
	 * {@link String} Message for no record to test in test email configuration functionality.
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
	
	

}
