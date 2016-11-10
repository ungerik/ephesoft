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

/**
 * The <code>LocaleCommonConstants</code> is an interface for declaring the keys mapping with the locale for constants common to whole
 * application UI.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see CommonLocaleInfo
 * 
 */
public interface LocaleCommonConstants {

	String header_label_hi = "header_label_hi";

	String header_label_signOut = "header_label_signOut";

	String msg_mask_wait = "msg_mask_wait";

	String exc_consumerAmountIsNotPositive = "exc_consumerAmountIsNotPositive";

	String exc_consumerTypeIsNull = "exc_consumerTypeIsNull";

	String exc_licenseHasExpired = "exc_licenseHasExpired";

	String exc_licenseIsNotYetValid = "exc_licenseIsNotYetValid";

	String exc_issuedIsNull = "exc_issuedIsNull";

	String exc_issuerIsNull = "exc_issuerIsNull";

	String exc_holderIsNull = "exc_holderIsNull";

	String exc_invalidSubject = "exc_invalidSubject";

	String exc_cpu_count_voilation = "exc_cpu_count_voilation";

	String exc_system_date_voilation = "exc_system_date_voilation";

	String exc_system_mac_address_voilation = "exc_system_mac_address_voilation";

	String title_confirmation_ok = "title_confirmation_ok";

	String title_confirmation_close = "title_confirmation_close";

	String title_confirmation_cancel = "title_confirmation_cancel";

	String dialog_box_title = "dialog_box_title";

	String exc_ephesoft_license_cpu_limit_exhausted = "exc_ephesoft_license_cpu_limit_exhausted";

	String exc_ephesoft_license_server_error = "exc_ephesoft_license_server_error";

	String title_confirmation_save = "title_confirmation_save";

	String title_confirmation_discard = "title_confirmation_discard";

	String footer_information = "Powered by Ephesoft";

	String ephesoft_url = "http://www.ephesoft.com";

	String title_go_to_page = "title_go_to_page";

	String title_next = "title_next";

	String title_previous = "title_previous";

	String title_displaying = "title_displaying";

	String header_label_help = "header_label_help";

	String ERROR_TITLE = "error_title";

	String CONFIRMATION = "confirmation";

	String INFO_TITLE = "info_title";

	String help_url_error_msg = "error_in_getting_help_url";

	String up_record = "up_record";

	String down_record = "down_record";

	String FOOTER_WARNING_MESSAGE = "footer_warning_message";

	String BROWSE = "browse";

	String SERVER_DISCONNECTED = "server_disconnected";

	/**
	 * The BATCH_CLASS_MANAGEMENT_CONSTANT {@link String} constant for tab label "Batch Class Management"
	 */
	String BATCH_CLASS_MANAGEMENT_CONSTANT = "batch_class_management_tab_label";

	/**
	 * The BATCH_INSTANCE_MANAGEMENT_CONSTANT {@link String} constant for tab label "Batch Instance Management"
	 */
	String BATCH_INSTANCE_MANAGEMENT_CONSTANT = "batch_instance_management_tab_label";

	/**
	 * The WORKFLOW_MANAGEMENT_CONSTANT {@link String} constant for tab label "Workflow Management"
	 */
	String WORKFLOW_MANAGEMENT_CONSTANT = "workflow_management_tab_label";

	/**
	 * The FOLDER_MANAGEMENT_CONSTANT {@link String} constant for tab label "Folder Management"
	 */
	String FOLDER_MANAGEMENT_CONSTANT = "folder_management_tab_label";

	/**
	 * The REPORTS_CONSTANT {@link String} constant for tab label "Reports"
	 */
	String REPORTS_CONSTANT = "reports_tab_label";

	/**
	 * The THEME_CONSTANT {@link String} constant for tab label "Theme"
	 */
	String THEME_CONSTANT = "theme_tab_label";

	/**
	 * Constant for url separator.
	 */
	String URL_SEPARATOR = "/";

	/**
	 * Constant for themes used while creating url to access the user name selected theme folder.
	 */
	String THEMES = "themes";

	/**
	 * Constant for js extension.
	 */
	String JS_EXTENSION = ".js";

	/**
	 * Constant for users used while creating url to access the user name selected theme folder.
	 */
	String USERS = "users";

	/**
	 * Constant for environment variable DCMA_HOME.
	 */
	String DCMA_HOME = "DCMA_HOME";

	/**
	 * Constant for underscore.
	 */
	String UNDERSCORE = "_";

	/**
	 * Constant for Theme.html.
	 */
	String THEME_HTML = "Theme.html";

	/**
	 * Error message when Ephesoft license is not installed on the machine or is invalid.
	 */
	String EXC_EPHESOFT_LICENSE_INVALID_LICENSE_SERVER_DOWN = "exc_ephesoft_license_invalid_license_server_down";

	/**
	 * Constant for batch class management html.
	 */
	String BATCH_CLASS_MANAGEMENT_HTML = "BatchClassManagement.html";

	/**
	 * Constant for batch instance management html.
	 */
	String BATCH_INSTANCE_MANAGEMENT_HTML = "BatchInstanceManagement.html";

	/**
	 * Constant for workflow management html.
	 */
	String WORKFLOW_MANAGEMENT_HTML = "CustomWorkflowManagement.html";

	/**
	 * Constant for folder management html.
	 */
	String FOLDER_MANAGEMENT_HTML = "FolderManager.html";

	/**
	 * Constant for reporting html.
	 */
	String REPORTING_HTML = "Reporting.html";

	/**
	 * Constant for batch list html.
	 */
	String BATCH_LIST_HTML = "BatchList.html";

	/**
	 * Constant for review validate html.
	 */
	String REVIEW_VALIDATE_HTML = "ReviewValidate.html";

	/**
	 * Constant for web scanner html.
	 */
	String WEB_SCANNER_HTML = "WebScanner.html";

	/**
	 * Constant for upload batch html.
	 */
	String UPLOAD_BATCH_HTML = "UploadBatch.html";

	/**
	 * Constant for login html.
	 */
	String LOGIN_HTML = "Login.html";

	/**
	 * Constant for theme property file name used while creating URL to get the selected theme by the current user.
	 */
	String THEME_PROPERTY_FILENAME = "theme.properties";

	/**
	 * <code>SUCCESS_TITLE</code> a {@link String} constant for displaying dialog box title.
	 */
	String SUCCESS_TITLE = "success_title";

	/**
	 * Constant for theme folder name used while creating URL to get the selected theme by the current user.
	 */
	String THEME_FOLDERNAME = "theme";

	/**
	 * Constant for system config tab label.
	 */
	String SYSTEM_CONFIG_MANAGEMENT_CONSTANT = "system_config_tab_label";

	/**
	 * Constant for system config html.
	 */
	String SYSTEM_CONFIG_MANAGEMENT_HTML = "SystemConfig.html";

	/**
	 * String constant for tool tip for batch class sort button.
	 */
	String SORT_ON_ID = "sort_on_id";

	/**
	 * String constant for tool tip for batch class sort button.
	 */
	String SORT_ON_DESCRIPTION = "sort_on_description";

	/**
	 * String constant for tool tip for theme change icon.
	 */
	String CHANGE_THEME = "change_theme";

	/**
	 * String constant for displaying error message while storing URL in session.
	 */
	String ERROR_STORING_URL_IN_SESSION = "error_storing_url_in_session";

	/**
	 * Constant for ACCESS_HTML_FILE_URL.
	 */
	public static final String ACCESS_HTML_FILE_URL = "../access-denied.html";

	/**
	 * String constant for the previous url before accessing theme url.
	 */
	public static final String PREVIOUS_URL = "previousUrl";

	/**
	 * Field Name displayed in GUI for the check box document level field while spliting pages
	 */
	String RETAIN_DOCUMENT_LEVEL_FIELD = "retain_document_level_field";
	/**
	 * Field Name displayed in GUI for the check box document level field while spliting pages
	 */
	String TABLE_LEVEL_FIELD = "retain_table_field";

	/**
	 * Constant for single quote.
	 */
	String SINGLE_QUOTE = "'";

	/**
	 * Constant for module remove button label.
	 */
	String REMOVE = "remove";

	/**
	 * Constant for module add button label.
	 */
	String ADD = "add";

	/**
	 * Constant for module up button label.
	 */
	String UP = "up";

	/**
	 * Constant for module down button label.
	 */
	String DOWN = "down";

	/**
	 * Message notifying user to click on the icon to activate the image magnification.
	 */
	public static String ACTIVATE_MAGNIFICATION_MSG = "activate_magnification_msg";

	/**
	 * Message notifying user to click on the icon to deactivate the image magnification.
	 */
	public static String DEACTIVATE_MAGNIFICATION_MSG = "deactivate_magnification_msg";

	/**
	 * Constant for displaying message while error is there in getting the user name.
	 */
	String ERROR_IN_GETTING_USERNAME = "error_in_getting_username";

	/**
	 * Constant for message shown when no record is found in database.
	 */
	String NO_RECORD_FOUND = "no_record_found";

	/**
	 * Constant for login html.
	 */
	String HOME_HTML = "home.html";

	String TOOLTIP_VALUE_NOT_EMPTY = "tooltip_value_not_empty";

	String TOOLTIP_UNIQUE_VALUE = "tooltip_unique_value";

	String TOOLTIP_PORT_NUMBER_RANGE = "tooltip_port_number_range";

	String TOOLTIP_VALUE_NONNEGATIVE = "tooltip_value_nonnegative";

	String TOOLTIP_UNIQUE_CMIS_CONFIGURATION = "tooltip_unique_cmis_configuration";

	String TOOLTIP_REGEX_NOT_VALID = "tooltip_regex_not_valid";

	String TOOLTIP_TABLE_EXTRACTION_API = "tooltip_table_extraction_api";

	String TOOLTIP_INCORRECT_RULE = "tooltip_incorrect_rule";

	String SELECT_FILES = "select_files";

	String OR_LABEL = "or_label";

	String CLICK_HERE_TO_UPLOAD = "click_here_to_upload";

	String RESET = "reset";

	String SHOWING = "Showing";

	String ACCEPT_VALUES_BETWEEN_10_TO_50 = "accept_values_between_10_to_50";

	String RECORDS_PER_PAGE = "records_per_page";

	String DISPLAYING_ZERO_OF_ZERO = "displaying_zero_of_zero";

	String LOG_OUT = "log_out";

	String MOVE_RIGHT = "move_right";

	String MOVE_LEFT = "move_left";

	String DRAG_AND_DROP_FILES_HERE = "drag_and_drop_files_here";

	String DISPLAYING = "displaying";

	String PAGE = "page";

	String TERABYTE = "terabyte";

	String GIGABYTE = "gigabyte";

	String MEGABYTE = "megabyte";

	String KILOBYTE = "kilobyte";

	String BYTE = "byte";

	String DOC_LIMIT_MESSAGE = "doc_limit_message";

}
