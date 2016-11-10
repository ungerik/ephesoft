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

package com.ephesoft.dcma.export;

/**
 * Interface for constants used.
 * 
 * @author Ephesoft
 * @version 3.0
 */
public interface IExportCommonConstants {

	/**
	 * Constant for batch id.
	 */
	String EPHESOFT_BATCH_ID = "EphesoftBatchID";

	/**
	 * Constant for document id in file name.
	 */
	String EPHESOFT_DOCUMENT_ID = "EphesoftDOCID";

	/**
	 * Empty string.
	 */
	String EMPTY_STRING = "";

	/**
	 * Parameter start delimeter.
	 */
	String PARAM_START_DELIMETER = "$";

	/**
	 * Default folder where document files are to be exported exported.
	 */
	String DEFAULT_FOLDER_NAME = "Unknown";

	/**
	 * Separator between the parameters specified by admin.
	 */
	String FILE_FORMAT_SEPARATOR = "&&";

	/**
	 * Constant for export folder name for batch xml.
	 */
	String FINAL_EXPORT_FOLDER = "Final Export Folder";

	/**
	 * Constant for batch instance folder name for batch xml.
	 */
	String BATCH_INSTANCE_FOLDER = "Batch Instance Folder";

	/**
	 * Replace character to be used while renaming file names.
	 */
	String DEFAULT_REPLACE_CHAR = "-";

	/**
	 * Separator for getting invalid characters from string.
	 */
	String INVALID_CHAR_SEPARATOR = ";";
	
	/**
	 * ON Switch.
	 */
	String ON_SWITCH = "ON";
	
	/**
	 * static string constant for document
	 */
	String STATIC_DOCUMENT_CONSTANT = "_DOC1";
	
	/**
	 * regex for obtaining the last number fron the string
	 */
	String REGEX1_FOR_OBTAINING_LAST_NUMBER_FROM_STRING = ".*(?<=\\D)(\\d+)\\D*";
	
	/**
	 * regex for incrementing the last number in a string
	 */
	String REGEX2_FOR_OBTAINING_LAST_NUMBER_FROM_STRING = "(.*)(?<=\\D)\\d+(\\D*)";
	
	/**
	 * regex denotion for first number
	 */
	String REGEX_FIRST_NUMBER = "$1";
	
	/**
	 * regex denotion for second number
	 */
	String REGEX_SECOND_NUMBER = "$2";
	
	/**
	 * invalid characters in a file name or folder path 
	 */
	String INVALID_CHARACTERS_LIST ="[[ * ?  < > |] :]";
	
	/**
	 * invalid characters in a file name or folder path except :
	 */
	String INVALID_CHARACTER_LIST_WITHOUT_COLON = "[[ * ?  < > |] ]" ;

	/**
	 * String constant for date
	 */
	String DATE = "DATE";
	
	/**
	 * Constant for time
	 */
	String TIME = "TIME";
	
	/**
	 * Constant for batch class
	 */
	String BATCH_CLASS_IDENTIFIER = "BATCH_CLASS";
	
	/**
	 * Constant for batch identifer
	 */
	String BATCH_INSTANCE_IDENTIFIER = "BATCH_IDENTIFIER";
	
	/**
	 * Constamt for batch field 
	 */
	String BATCH_FIELD_TYPE = "BATCH_FIELD_VALUE";
	
	/**
	 * Constamt for document type
	 */
	String DOCUMENT_TYPE = "DOCUMENT_TYPE";
	
	/**
	 * Constant for date format 
	 */
	String STATIC_DATE_FORMAT = "yyyy-MM-dd";
	
	/**
	 * Constamt for time format
	 */
	String STATIC_TIME_FORMAT = "hh-mm-ss";
	
	/**
	 * Constamt for server name
	 */
	String SERVER_NAME = "SERVER_NAME";
	
	/**
	 * Constant for file name seperator
	 */
	String FILE_NAME_SEPERATOR = "&";
	
	/**
	 * Constant for folder path seperator
	 */
	String FOLDER_PATH_SEPERATOR = "/";
	
	/**
	 * Constant for document level fields
	 */
	String DOCUMENT_LEVEL_FIELD_TAG = "DLF:";
	
	/**
	 * Constant for fields with dynamic values 
	 */
	String DYNAMIC_FILE_MARKER = "$";
	
	/**
	 * Constant for file name seperator
	 */
	String MODIFIED_FILE_NAME_SEPERATOR = "_";

	/**
	 * Constant for any file 
	 */
	String FILE_NAME_REGEX = "(.*)";
	
	String INVALID_FILE_PATH_MESSAGE = "Invalid path entered in multipage file export folder text box.";
	
	
	String QUOTES = "\"";
	
	String SPACE = " ";
	
	String NULL = "null";
	
	String COLON = ":";
	
	String IM4JAVA_TOOLPATH = "IM4JAVA_TOOLPATH";
	
	String WATERMARK_CONVERT_COMMAND = "convert";
	
	String WATERMARK_DENSITY_COMMAND = " -density 300 ";
	
	String WATERMARK_SUBCOMMAND2 = " -pointsize 18 -gravity center -compose multiply -layers composite ";
	
	String WATERMARK_IMAGE_FOLDEER = "Watermark";
	
	String WATERMARK_IMAGE_NAME = "stamp.png";
	
	/**
	 * Constant for document id
	 */
	String DOCUMENT_ID = "DOCUMENT_ID";
}
