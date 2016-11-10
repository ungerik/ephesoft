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

package com.ephesoft.gxt.foldermanager.client.i18n;

import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;

/**
 * The interface is used to define all the constants used in the Folder Management page and support internationalization.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 13-Apr-2015 <br/>
 * @version 1.0.0 <br/>
 *          $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see LocaleCommonConstants
 */
public interface FolderManagementConstants extends LocaleCommonConstants {

	String NEW_FOLDER = "New Folder";
	String OPENING_BRACKET = "(";
	String CLOSING_BRACKET = ")";
	String ERROR_TYPE_1 = "File Empty Error";
	String EMPTY_STRING = "";
	String HIDDEN_FOLDERS_NAMES = "hidden_folders_names";
	String DELIMITER = ";";
	String QUESTION_MARK = "?";
	String AMPERSAND = "&";
	String UPLOAD_DOWNLOAD_SERVLET_PATH = "filesUploadDownload";
	String EQUALS = "=";
	String CURRENT_FILE_DOWNLOAD_PATH = "currentFileDownloadPath";
	String CURRENT_UPLOAD_FOLDER_NAME = "currentUploadFolderName";
	String UPLOAD_FAILURE = "Upload failure";
	String UPLOAD_FAILURE_MESSAGE = "This file type is cannot be uploaded.";
	String FOOTER = "folderManagerFooter";
	String DOT = ".";
	String COPY = "Copy";
	String SPACE = "  ";
	String QUOTES = "\"";
	String CUT = "cut";
	String COPY_PASTE = "copy_paste";
	String CUT_PASTE = "cut_paste";
	String DELETE = "delete_string";
	String DELETE_CONFIRM = "delete";
	String SEMI_COLON = ";";
	String PATH_SEPARATOR_STRING = "\\";
	String CREATE_NEW_FOLDER = "create_new_folder";
	String ICON_FOLDER = "<img src='images/icon_folder.gif' />";
	String OF = "of";
	String FOLDER_UP_BUTTON = "folder_up";
	String FOLDER_REFRESH_BUTTON = "refresh";
	String FOLDER_COPY_BUTTON = "copy";
	String FOLDER_PASTE_BUTTON = "paste";
	String OPEN_BUTTON = "open";
	String DOWNLOAD_MENU_ITEM = "download";
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

	String UPLOAD_VIEW_HEADER = "upload_view_header";
}
