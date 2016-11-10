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

package com.ephesoft.dcma.gwt.core.shared.constants;


public class CoreCommonConstants {

	public static final String FOOTER_TEXT_KEY = "footer_text_key";

	public static final String FOOTER_LINK_KEY = "footer_link_key";

	/**
	 * Invalid characters for a file name.
	 */
	public static final String INVALID_FILE_EXTENSIONS = "/\\:*<>?\"|";

	/**
	 * Fuzzy config type document.type
	 */
	public static final String DOCUMENT_TYPE = "document.type";

	/**
	 * Fuzzy config type field.type
	 */
	public static final String FIELD_TYPE = "field.type";

	/**
	 * Fuzzy config type row.id
	 */
	public static final String ROW_TYPE = "row.id";

	/**
	 * Fuzzy DB Plugin Name
	 */
	public static final String FUZZY_DB_PLUGIN_NAME = "FUZZYDB";

	/**
	 * <code>ALL_CHARACTER_REGEX_STRING</code> a {@link String} constant represents the regex pattern used to identify all the
	 * characters in the string.
	 */
	public static final String ALL_CHARACTER_REGEX_STRING = ".";

	/**
	 * <code>PASSWORD_HIDDEN_STRING</code> a {@link String} constant to specify the hidden string for UI display while hiding the
	 * password field across the application.
	 */
	public static final String PASSWORD_HIDDEN_STRING = "*";

	/**
	 * The dafault number of rows in table.
	 */
	public static int DEFAULT_NO_OF_ROWS = 5;

	/**
	 * The minimum number of rows in table.
	 */
	public static int MIN_ROWS = 5;

	/**
	 * The maximum number of rows in table.
	 */
	public static int MAX_ROWS = 25;

	/**
	 * The height of one cell in pixels.
	 */
	public static int CELL_HEIGHT = 25;

	/**
	 * The default height in terms of row count.
	 */
	public static int DEFAULT_HEIGHT = 5;

	/**
	 * The class name of RuleValidator class.
	 */
	public static String RULE_VALIDATOR = "RuleValidator";

	/**
	 * Constant for and operator.
	 */
	public static final String AND_OPERATOR = "AND";

	/**
	 * CHAR_EQUAL{@link String} constant defined for Equal.
	 */
	public static final String CHAR_EQUAL = "==";

	/**
	 * AMPERSAND {@link String} constant defined for '&'.
	 */
	public static final String AMPERSAND = "&";

	/**
	 * String constant for ';'.
	 */
	public static final String SEMI_COLON = ";";

	/**
	 * String constant for '0'.
	 */
	public static final String ZERO = "0";

	/**
	 * String constant for '!='.
	 */
	public static final String NOT_EQUAL = "!=";

	/**
	 * String constant for '>='.
	 */
	public static final String GREATER_OR_EQUAL = ">=";

	/**
	 * String constant for '<='.
	 */
	public static final String LESS_OR_EQUAL = "<=";

	/** Constant for displaying error message for invalid data for a table column, i.e data which is not numeric in nature. */
	public static final String INVALID_DATA_ERROR_MSG = "INVALID DATA";

	/**
	 * Constant for key name used to set and get user group from session.
	 */
	public static final String SESSION_USER_GROUPS_KEY = "userGroup";

	/**
	 * Constant for header parameter name for getting super-admin header value.
	 */
	public static final String SUPER_ADMIN_PARAM_NAME = "requestSuperAdminHeader";

	/**
	 * Constant for key name used to set and get super admin from filter.
	 */
	public static final String SUPER_ADMIN_ATTRIBUTE_KEY = "superAdmin";

	/**
	 * Constant for header parameter name for getting group name header value.
	 */
	public static final String GROUP_NAME_PARAM_NAME = "requestGroupnameHeader";

	/**
	 * Constant for key name used to set and get group name from filter.
	 */
	public static final String GROUP_NAME_ATTRIBUTE_KEY = "groupName";

	/**
	 * Constant for header parameter name for getting logout URL header value.
	 */
	public static final String LOGOUT_URL_PARAM_NAME = "logoutUrl";

	/**
	 * Constant for header parameter name for getting user name header value.
	 */
	public static final String REQUEST_USERNAME_HEADER = "requestUsernameHeader";

	/**
	 * Constant for key name used to set and get user-name of authenticated user in/from the session.
	 */
	public static final String USERNAME_ATTRIBUTE_KEY = "userName";

	/**
	 * Constant for key name used to set and get all super-admin groups from session.
	 */
	public static final String ALL_SUPER_ADMIN_GROUPS = "allSuperAdminGroups";

	/**
	 * Constant for key name used to set and get all groups from session.
	 */
	public static final String ALL_GROUPS = "allGroups";

	/**
	 * Constant for key name used to set and get all users from session.
	 */
	public static final String ALL_USERS = "allUsers";

	/**
	 * Constant for key name used to set and get whether the current logged in user is super-admin or not from session.
	 */
	public static final String IS_SUPER_ADMIN = "isSuperAdmin";

	/**
	 * Constant for defining the name of error page for invalid license.
	 */
	public static final String LICENSE_ERROR_PAGE_PATH = "InvalidLicense.html";

	/**
	 * Constant for character '/'.
	 */
	public static final String FORWARD_SLASH = "/";

	/**
	 * Constant for an empty string.
	 */
	public static final String EMPTY_STRING = "";

	/**
	 * Constant for get and set of logout URL in the session.
	 */
	public static final String LOGOUT_URL_ATTRIBUTE_KEY = "logoutUrl";

	/**
	 * String constant for underscore.
	 */
	public static final String UNDERSCORE = "_";

	/**
	 * String constant for space.
	 */
	public static final String SPACE = " ";

	/**
	 * String constant for comma.
	 */
	public static final String COMMA = ",";

	/**
	 * String constant for colon.
	 */
	public static final String COLON = ":";

	/**
	 * Constant for maximum number of characters of a message that can be displayed in a confirmation dialog-box.
	 */
	public static final int MAX_CHARACTER_LIMIT = 55;

	/**
	 * String constant for ON.
	 */
	public static final String ON = "on";

	/**
	 * String constant for OFF.
	 */
	public static final String OFF = "off";

	/**
	 * String constant for TWO.
	 */
	public static final int TWO = 2;

	/**
	 * Constant for Dot.
	 */
	public static final char DOT = '.';

	/**
	 * String constant for Pipe.
	 */
	public static final String PIPE = "|";

	/**
	 * String constant for Date format.
	 */
	public static final String DATE_FORMAT = "MMddyy";

	/**
	 * {@link Float} Constant for default value of weight.
	 */
	public static final Float DEFAULT_WEIGHT = 1.0f;

	/**
	 * Constant which refer to the CSS to break test not wrapping the words.
	 */
	public static final String BREAK_ALL_TEXT_CSS = "break-text";

	/**
	 * Constant {@link String} which indicates the CSS style name for test table Dialog Box.
	 */
	public static final String TEST_TABLE_DIALOG_CSS = "testTableDialog";

	/**
	 * Constant for multi-server switch license property name.
	 */
	public static final String MUTLISERVER_SWITCH_LICENSE_PROPERTY_NAME = "Verify multiple Ephesoft application switch";

	/**
	 * Constant for expiry date license property name.
	 */
	public static final String EXPIRY_DATE_LICENSE_PROPERTY_NAME = "License expiry date";

	/**
	 * Constant for multi-server switch.
	 */
	public static final String MUTLISERVER_SWITCH = "Multiserver switch";

	/**
	 * Constant for expiry date message license property name.
	 */
	public static final String EXPIRY_DATE_MESSAGE_LICENSE_PROPERTY_NAME = "Expiry message will be shown before";

	/**
	 * Constant for GMT.
	 */
	public static final String GMT = "GMT";

	/**
	 * Constant for date format.
	 */
	public static final String EXTENDED_DATE_FORMAT = "EEE MMM dd HH:mm:ss z yyyy";

	/**
	 * Constant for days.
	 */
	public static final String DAYS = "days";

	/**
	 * Constant for expiry date message new license property name.
	 */
	public static final String LICENSE_EXPIRATION_DISPLAY_MESSGAE = "License Expiration Display Message";

	/**
	 * WindowClose web service URL
	 */
	public static final String WINDOW_CLOSE_SERVICE_URL = "/dcma/rest/closeWindow/BatchClassIdentifier/";

	/**
	 * CONTENT TYPE Variable
	 */
	public static final String CONTENT_TYPE = "content-type";

	/**
	 * MIME type
	 */
	public static final String MIME_TYPE = "application/x-www-form-urlencoded";

	/**
	 * {@link String} constant for advance reporting switch property name.
	 */
	public static final String ADVANCE_REPORTING_SWITCH_PROPERTY_NAME = "Advance reporting switch";

	/**
	 * {@link String } constant for advanced reporting switch.
	 */
	public static final String ADVANCED_REPORTING_SWITCH = "Advanced reporting switch";

	/**
	 * Constant for representing user full name required switch is on.
	 */
	public static final int USER_FULLNAME_REQUIRED_SWITCH_ON = 1;

	/**
	 * Constant for setting user full name required switch as off.
	 */
	public static final int USER_FULLNAME_REQUIRED_SWITCH_OFF = 0;

	/**
	 * Constant for key name used to set and get user-name of authenticated user in/from the session.
	 */
	public static final String USERFULLNAME_REQUIRED_PROP = "fullname.display";

	/**
	 * Constant for defining path of user-connectivity property folder name.
	 */
	public static final String USER_CONNECTIVITY_PROPERTIES_FOLDER = "dcma-user-connectivity";

	/**
	 * Constant for defining path of user-connectivity property file name.
	 */
	public static final String USER_CONNECTIVITY_PROPERTIES_FILE = "user-connectivity";

	/**
	 * Constant for key name used to set and get user-Full-Name of authenticated user in/from the session.
	 */
	public static final String USER_FULLNAME_ATTRIBUTE_KEY = "userFullName";
	
	/**
	 * Constant for key name used to set and get user-Email of authenticated user in/from the session.
	 */
	public static final String USER_EMAIL_ATTRIBUTE_KEY = "userEmail";
}
