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

/**
 * This is a common constants file for AdminConstant.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.admin.bm.client
 */
public final class AdminConstants {

	/**
	 * Label to be displayed for key generator button on batch class management screen.
	 */
	public static final String KEY_GENERATOR = "key_generator";
	/**
	 * Label for the button of batch class key.
	 */
	public static final String GENERATE_KEY_BUTTON = "generate";
	/**
	 * Label for batch class key.
	 */
	public static final String BATCH_CLASS_KEY_LABEL = "batch_class_key";
	/**
	 * Label for encryption algorithm drop down list.
	 */
	public static final String ENCRYPTION_ALGO_LABEL = "encryption_algo";
	/**
	 * Constant for encryption algorithm AES_128.
	 */
	public static final String ENCRYPTION_ALGO_AES_128 = "AES_128";
	/**
	 * Constant for encryption algorithm AES_256.
	 */
	public static final String ENCRYPTION_ALGO_AES_256 = "AES_256";
	/**
	 * Constant if no encryption algorithm is set.
	 */
	public static final String ENCRYPTION_ALGO_NONE = "None";
	/**
	 * <code>USE_EXISTING</code> a {@link String} constant used to represent the locale object name for the constant to be displayed
	 * while user imports batch class.
	 */
	public static final String USE_EXISTING_KEY = "use_existing_key";

	/**
	 * Constant for specifying max decimal value for confidence score.
	 */
	public static final int MAX_DECIMAL_PLACES = 2;

	/**
	 * Constant {@link String} for cmis import mapping key checkbox.
	 */
	public static final String CMIS_IMPORT_MAPPING_KEY = "cmis-plugin-mapping";

	/**
	 * Constant for CMIS plugin properties checkbox name.
	 */
	public static final String CMIS_PLUGIN_PROPERTIES = "cmisPluginProperties";
	public static final String EMPTY_STRING = "";
	public static final String STAR = "*";
	/**
	 * CSS defined to show rows where validation fails in red color.
	 */
	public static final String DATE_BOX_FORMAT_ERROR = "dateBoxFormatError";
	public static final String SAVE_BUTTON = "save";
	public static final String CANCEL_BUTTON = "cancel";
	public static final String SWITCH_ON = "ON";
	public static final String COLON = ":";
	public static final String UNDERSCORE = "_";
	public static final String SPACE = " ";
	public static final String VERSION = "1.0.0.0";
	public static final String ERROR_CODE_TEXT = "error";
	public static final int PRIORITY_LOWER_LIMIT = 1;
	public static final int PRIORITY_UPPER_LIMIT = 100;

	public static final String WEB_SCANNER_PROFILE_TEXT_CONST = "ProfileName";
	/**
	 * IMAGE_CLASSIFICATION {@link String} Constant for Image Classification.
	 */
	public static final String IMAGE_CLASSIFICATION = "ImageClassification";
	/**
	 * BARCODE_CLASSIFICATION {@link String} Constant for Barcode classification.
	 */
	public static final String BARCODE_CLASSIFICATION = "BarcodeClassification";
	/**
	 * SEARCH_CLASSIFICATION {@link String} Constant for Search Classification.
	 */
	public static final String SEARCH_CLASSIFICATION = "SearchClassification";

	public static final String AUTOMATIC_CLASSIFICATION = "AutomaticClassification";

	public static final String DOCUMENT_TYPE_UNKNOWN = "unknown";

	public static final String PATTERN_DELIMITER = ";";

	/**
	 * <code>SELECT_LABEL</code> a {@link String} constant used to represent the locale object name for the constant to be displayed
	 * Select value in the combo box.
	 */
	public static final String SELECT_LABEL = "select";

	/**
	 * CMIS_FILE_EXTENSIONS {@link String} Constant for supported cmis file extensions.
	 */
	public static final String CMIS_FILE_EXTENSIONS = "pdf;tif";

	public static final String GXT_ADVANCED_KV_TEXT_BOX = "gxt-Advanced-KV-TextBox";
	public static final String VALIDATE_BUTTON = "validate";
	public static final String VALIDATE_BUTTON_IMAGE = "validateButtonImage";
	public static final String OK_BUTTON = "ok";
	public static final String GXT_SMALL_BUTTON_CSS = "gxt-small-button";
	public static final String SOURCE_ATTRIBUTE = "src";
	public static final Double WEIGHT_START_RANGE = 0.1;
	public static final Double WEIGHT_END_RANGE = 1.0;
	public static final int MINIMUM_ALLOWED_CHARACTERS_FUZZINESS = 4;
	public static final String PREVIOUS = "<";
	public static final String NEXT = ">";
	public static final String GXT_ADVANCED_KV_INPUT_COMBO_BOX = "gxt-Advanced-KV-Input-Combo-Box";

	/**
	 * Module Level
	 */

	public static final int INITIAL_ORDER_NUMBER = 1;
	public static final int ORDER_NUMBER_OFFSET = 10;
	public static final String AND = ",";
	public static final String OR = "/";
	public static final String WROND_DOC_TYPE_PROVIDED = "wrong_doc_type_provided";

	/**
	 * Constant for imagemagick properties folder name.
	 */
	public static final String IMAGEMAGICK_PROPERTIES_FOLDER = "dcma-imagemagick";

	/**
	 * Constant for imagemagick properties file name.
	 */
	public static final String IMAGEMAGICK_PROPERTIES_FILE = "imagemagick";

	/**
	 * Constant for imagemagick unsupported characters property name.
	 */
	public static final String IMAGEMAGICK_UNSUPPORTED_CHARACTER_PROPERTY = "imagemagick.unsupported_characters";
	/**
	 * Constant for specifying semicolon.
	 */
	public static final String SEMICOLON = ";";
	public static final String CHARACTER_ENCODING_UTF8 = "UTF-8";

	public static final String DATABASE_TYPE_STRING = "STRING";
	public static final String DATABASE_TYPE_DATE = "DATE";
	public static final String DATABASE_TYPE_DOUBLE = "DOUBLE";
	public static final String DATABASE_TYPE_LONG = "LONG";
	public static final String DATABASE_TYPE_INTEGER = "INTEGER";
	public static final String DATABASE_TYPE_FLOAT = "FLOAT";
	public static final String DATABASE_TYPE_BIGDECIMAL = "BIGDECIMAL";

	public static final String JAVA_TYPE_STRING = "String";
	public static final String JAVA_TYPE_DATE = "Timestamp";
	public static final String JAVA_TYPE_DOUBLE = "Double";
	public static final String JAVA_TYPE_LONG = "Long";
	public static final String JAVA_TYPE_INTEGER = "Integer";
	public static final String JAVA_TYPE_FLOAT = "Float";
	public static final String JAVA_TYPE_BIGDECIMAL = "BigDecimal";
}
