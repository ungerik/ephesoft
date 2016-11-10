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

package com.ephesoft.gxt.uploadbatch.client.i18n;

import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;

/**
 * The class is used to define all the constants used in the Upload Batch module and support internationalization.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see LocaleCommonConstants
 */
public interface UploadBatchConstants extends LocaleCommonConstants {

	String TAB_LABEL_HOME = "tabLabel_home";
	String TAB_LABEL_BATCH_DETAIL = "tabLabel_batch_detail";
	String TAB_LABEL_WEB_SCANNER = "tabLabel_web_scanner";
	String TAB_LABEL_UPLOAD_BATCH = "tabLabel_upload_batch";
	String STAR = "*";

	String UPLOAD_TEXT = "upload_text";
	String UPLOAD_BUTTON = "upload_button_label";
	String ERROR_CODE_TEXT = "error";
	String RESET_BUTTON = "reset";
	String ASSOCIATE_BCF_BUTTON = "associate_bcf_button";
	String FINISH_BUTTON = "finish_button_label";
	String BUTTON_TEXT_OK = "ok";
	String CANCEL = "cancel";
	String DELETE_BUTTON = "delete_button_label";

	String SERIALIZATION_EXT = ".ser";
	String BCF_SER_FILE_NAME = "BCF_ASSO";

	String SELECT_ALL = "select_all";

	String FILE_TYPES = "file_types";
	String UPLOAD_PROGRESS = "upload_progress";

	/**
	 * The IE_BROWSER {@link String} is a constant for IE Browser.
	 */
	String IE_BROWSER = "msie";

	/**
	 * The WIDTH_26_EM {@link String} is a constant for width in EM of 26.
	 */
	String WIDTH_26_EM = "26em";

	/**
	 * The FREEMIUM_USER_TYPE {@link String} is a constant for Ephesoft Cloud freemium user type
	 */
	String FREEMIUM_USER_TYPE = "Freemium";

	/**
	 * The INSTANCE_ERROR {@link String} is a constant for instance error.
	 */
	String INSTANCE_ERROR = "Instance Error";

	/**
	 * The IMAGE_ERROR {@link String} is a constant for image error.
	 */
	String IMAGE_ERROR = "Image Error";

	/**
	 * The SPACE {@link String} is a constant for space ' '.
	 */
	String SPACE = " ";

	/**
	 * The MEGA_BYTE {@link String} is a constant for mega bytes short form notation.
	 */
	String MEGA_BYTE = "MB";

	/**
	 * The UPLOAD_FILE_LABEL {@link String} is a constant for CSS class name for uploaded files shown on upload batch UI.
	 */
	String UPLOAD_FILE_LABEL = "uploadFileLabel";

	/**
	 * BATCH CLASS {@link String}.
	 */
	String BATCH_CLASS = "batch_class";

	/**
	 * {@link String} constant for empty folder error while uploading images.
	 */
	String NO_IMAGE_ERROR = "No Image Error";

	/**
	 * Error message to be displayed when there is an error uploading any image.
	 */
	String UNABLE_TO_UPLOAD_ERROR = "unable_to_upload_error";

	/**
	 * Constant used for displaying the title of the upload batch page
	 */
	String UPLOAD_BATCH_PAGE_TITLE = "upload_batch_title";
	
	
	/*NEW ADDITIONS	NEW ADDITIONS	NEW ADDITIONS	NEW ADDITIONS	NEW ADDITIONS	NEW ADDITIONS*/
	
	/**
	 * Constant used for semicolon.
	 */
	char SEMICOLON = ';';

	/**
	 * Constant used for spiting file extension with file name.
	 */
	char DOT = '.';
	
	/**
	 * Constant used for ordered list start tag.
	 */
	String ORDERED_LIST_START_TAG = "<ol>";

	/**
	 * Constant used for ordered list end start tag.
	 */
	String ORDERED_LIST_END_TAG = "<//ol>";
	
	/**
	 * Constant used for list start tag.
	 */
	String LIST_START_TAG = "<li>";
	
	/**
	 * Constant used for list end tag.
	 */
	String LIST_END_TAG = "<//li>";
	
	/**
	 * String constant for date format.
	 */
	public final String DATE_FORMAT = "dd-MMM-yyyy_HH-mm-ss-SSS";
	
	/**
	 * Constant for illegal characters in file name
	 */
	final String INVALID_CHARACTER_LIST = "[ * ?  < > | : / \\ \" ]";
	
	/**
	 * Constant for modified file name separator
	 */
	final String MODIFIED_FILE_NAME_SEPERATOR = "_";

	/**
	 * Max char limit for batch description.
	 */
	final int BATCH_DESCRIPTION_LENGTH_LIMIT = 255;
	
	final String TEXT_BOX = "Textbox";
	
	final String COMBO_BOX = "ComboBox";
	
	final String BATCH_CLASS_LABEL = "batch_class"; 
	
	final String DESCRIPTION_LABEL = "description"; 
	
	final String START_BATCH = "start_batch";
	
	final String RESET_ALL = "reset_all";
	
	final String DELETE_FILES = "delete_files";
	
	final String FIELDS = "fields";
	
	final String MENU_BAR_BATCH_INSTANCE_STYLE = "menuBarBatchInstance";
	
	final String UNABLE_TO_START_SELCT_VALID_BATCH_CLASS = "unable_to_start_batch_please_select_a_valid_batch_class";
	
	final String UNABLE_TO_START_ENTER_BATCH_DESCRIPTION = "unable_to_start_batch_please_enter_batch_description";
	
	final String UPLOAD_SPEED_IN_KB = "upload_speed_in_KBs";
	
	final String UPLOAD_SPEED_GUAGE_CHART_CONTAINER_CSS = "upload_speed_gauge_chart_container";
	
	final String UPLOAD_SPEED_DETAIL_CONTENT_PANEL_CSS = "upload_speed_detail_content_panel";
	
	final String UPLOAD_SPEED_DETAIL_GRID_CSS =  "upload_speed_detail_grid";
	
	final String CATEGORY = "Category";
	
	final String UPLOAD_DETAILS = "upload_details";
	
	final String UPLOAD_FILES = "upload_files";
	
	final String CURRENT_BATCH_UPLOAD_FOLDER_NAME_EQUALS = "currentBatchUploadFolderName=";
	
	final String UPLOAD_BATCH_CONSTANTS = "uploadBatchConstants";
	
	final String UPLOAD_BATCH_MESSAGES = "uploadBatchMesseges";
	
	final String BATCH_CLASS_FIELDS = "batch_class_fields";
	
	final String FILE_NAME = "file_name";
	final String SIZE = "size";
	final String UPLOADED = "uploaded";
			final String  ELAPSED = "elapsed";
			final String  CURRENT_SPEED = "current_speed";
			final String  AVERAGE_SPEED = "average_speed";
			final String  SLASH_S = "slash_s";
					final String  SECS = "secs";
					
					final String UPLOAD_SPEED_IN =  "upload_speed_in";
					
					final String PLEASE_WAIT = "please_wait";
					
					final String QUEUED_FOR_PROCESSING = "queued_for_processing";
					
					final String BATCH = "batch";
	
	
	}
