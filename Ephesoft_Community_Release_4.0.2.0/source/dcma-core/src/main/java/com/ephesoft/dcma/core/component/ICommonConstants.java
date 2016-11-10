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

package com.ephesoft.dcma.core.component;

import com.ephesoft.dcma.core.common.ServiceType;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This interface contains all the common constants.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jun 20, 2013 <br/>
 * @version 1.0 <br/>
 *          $LastChangedDate: 2015-06-26 10:12:22 +0530 (Fri, 26 Jun 2015) $ <br/>
 *          $LastChangedRevision: 23908 $ <br/>
 */
public interface ICommonConstants {

	/**
	 * Constant ENTER_METHOD is to store a String with value >>>Entering Method
	 */
	public final String ENTER_METHOD = ">>>Entering Method";

	/**
	 * Constant EXIT_METHOD is to store a String with value <<<Exiting Method
	 */
	public final String EXIT_METHOD = "<<<Exiting Method";

	/**
	 * Constant CLASS_NAME is to store a String with value >Class Name=
	 */
	public final String CLASS_NAME = " >Class Name=";

	/**
	 * Constant EXTENSION_TIF is to store String to represent .tif Extension.
	 */
	public final String EXTENSION_TIF = ".tif";

	/**
	 * Constant EXTENSION_PDF is to store String to represent .pdf Extension.
	 */
	public final String EXTENSION_PDF = ".pdf";

	/**
	 * Constant EXTENSION_PNG is to store String to represent .png Extension.
	 */
	public final String EXTENSION_PNG = ".png";

	/**
	 * Constant UNDERSCORE_BATCH_XML_ZIP is to store String(name of a .zip) with value _batch.xml.zip.
	 */
	public final String UNDERSCORE_BATCH_XML_ZIP = "_batch.xml.zip";

	/**
	 * Constant UNDERSCORE_BATCH_BAK_XML_ZIP is to store String(name of a .zip) with value _batch_bak.xml.zip.
	 */
	public final String UNDERSCORE_BATCH_BAK_XML_ZIP = "_batch_bak.xml.zip";

	/**
	 * Constant UNDERSCORE_BAK_BATCH_XML_ZIP is to store String(name of a .zip) with value _bak_batch.xml.zip.
	 */
	public final String UNDERSCORE_BAK_BATCH_XML_ZIP = "_bak_batch.xml.zip";

	/**
	 * Constant UNDERSCORE_BATCH_XML is to store String(name of a .xml) with value _batch.xml.
	 */
	public final String UNDERSCORE_BATCH_XML = "_batch.xml";

	/**
	 * Constant UNDERSCORE_BATCH_BAK_XML is to store String with value _batch_bak.xml.
	 */
	public final String UNDERSCORE_BATCH_BAK_XML = "_batch_bak.xml";

	/**
	 * Constant UNDERSCORE_BAK_BATCH_XML is to store String with value _bak_batch.xml.
	 */
	public final String UNDERSCORE_BAK_BATCH_XML = "_bak_batch.xml";

	/**
	 * Constant ZIP_SWITCH is to store String with value zip_switch.
	 */
	public static final String ZIP_SWITCH = "zip_switch";

	/**
	 * Constant BATCH_XSD_SCHEMA_PACKAGE is to store the package name com.ephesoft.dcma.batch.schema.
	 */
	public final String BATCH_XSD_SCHEMA_PACKAGE = "com.ephesoft.dcma.batch.schema";

	/**
	 * Constant PROPERTIES_FILE_BARCODE_READER is to store name of .properties file i.e reader.properties.
	 */
	public final String PROPERTIES_FILE_BARCODE_READER = "reader.properties";

	/**
	 * Constant PROPERTY_BARCODE_READER is to store image location String with value image.base.location.
	 */
	public final String PROPERTY_BARCODE_READER = "image.base.location";

	/**
	 * Constant FIRST_PAGE is to store a String with value _First_Page.
	 */
	public final String FIRST_PAGE = "_First_Page";

	/**
	 * Constant MIDDLE_PAGE is to store a String with value _Middle_Page.
	 */
	public final String MIDDLE_PAGE = "_Middle_Page";

	/**
	 * Constant LAST_PAGE is to store a String with value _Last_Page.
	 */
	public final String LAST_PAGE = "_Last_Page";

	/**
	 * Constant SEARCH_CLASSIFICATION_PLUGIN is to store a String with value SEARCH_CLASSIFICATION.
	 */
	public final String SEARCH_CLASSIFICATION_PLUGIN = "SEARCH_CLASSIFICATION";

	/**
	 * Constant BARCODE_READER_PLUGIN is to store a String with value BARCODE_READER.
	 */
	public final String BARCODE_READER_PLUGIN = "BARCODE_READER";

	/**
	 * Constant FOLDER_NAME_SEPERATOR is to store a File name separator String with value -.
	 */
	public final String FOLDER_NAME_SEPERATOR = "-";
	/**
	 * Folder Import Module Workflow Name
	 */
	public final String FOLDER_IMPORT_MODULE = "Folder_Import_Module";
	String DOWNLOAD_FOLDER_NAME = "downloaded-email-attachments";
	String BACK_UP_FOLDER_NAME = "_backup";
	String ON = "ON";
	String OFF = "OFF";
	String CREATE_BATCH_INSTANCE_BACKUP = "create_batch_instance_backup";
	String SPACE = " ";
	String HYPHEN = "-";
	String UNC = "unc";
	String VERSION = "1.0.0.0";
	String UNDERSCORE = "_";
	int PERMISSIBLE_LIMIT_PATH_LENGTH = 248;

	/**
	 * The DEFAULT_BATCH_INSTANCE_LIMIT is a default constant for batch class instance limit.
	 */
	int DEFAULT_BATCH_INSTANCE_LIMIT = 10;

	/**
	 * The DEFAULT_PAGE_COUNT_LIMIT is a default constant for page count limit.
	 */
	int DEFAULT_PAGE_COUNT_LIMIT = 10;

	/**
	 * The DEFAULT_BATCH_INSTANCE_LIMIT is a default constant for number of days limit.
	 */
	int DEFAULT_NO_OF_DAYS_LIMIT = 1;

	float TIME_IN_MILLISECOND = 1 * 60 * 60 * 24 * 1000;

	/**
	 * Constant for maximum number of retries in case of executor command failure
	 */
	String MAXIMUM_NUMBER_OF_RETRIES = "maximum_retries_on_executor_failure";

	/**
	 * Constant for service type parameter used to send type of service {@link ServiceType} during web service call to notify server
	 * about service event.
	 */
	String SERVICE_TYPE_PARAMETER = "serviceType";

	/**
	 * Constant for batch class ID parameter for sending batch class ID over a web service call.
	 */
	String BATCH_CLASS_ID_PARAMETER = "batchClassID";

	/**
	 * Constant for event type parameter for sending type of event over a web service call.
	 */
	String EVENT_TYPE_PARAMETER = "eventType";

	/**
	 * Constant for HTTP header.
	 */
	String HTTP = "http";

	/**
	 * Constant for colon.
	 */
	String COLON = ":";

	/**
	 * Constant for forward slash.
	 */
	String FORWARD_SLASH = "/";

	/**
	 * Constant for double forward slash.
	 */
	String DOUBLE_FORWARD_SLASH = "//";

	/**
	 * Constants for UNC folder of batch class parameter for sending over a web service call.
	 */
	String UNC_FOLDER_PARAMETER = "uncFolder";

	/**
	 * String constant for GHOSTSCRIPT.
	 */
	String GHOSTSCRIPT = "Ghostscript";

	/**
	 * Constant for '.' character.
	 */
	char EXTENSION_CHAR = '.';

	/**
	 * To hold the empty string as a constant.
	 */
	String EMPTY_STRING = "";

	/**
	 * To hold protocol address http with slashes.
	 */
	String HTTP_SLASH = "http://";

	/**
	 * To hold the name of HTML used for checking status of server i.e., HealthStatus.html.
	 */
	String HEALTH_STATUS_HTML = "HealthStatus.html";

	/**
	 * To hold the host name of localhost.
	 */
	String LOCALHOST = "localhost";

	/**
	 * To hold the port parameter used in web.xml.
	 */
	String PORT_PARAM = "port";

	/**
	 * String constant for IMPORT_MULTIPAGE_FILES plugin name. This plugin name is needed at the time of batch import.
	 */
	String IMPORT_MULTIPAGE_FILES_PLUGIN = "IMPORT_MULTIPAGE_FILES";

	/**
	 * Constant for double backward slash.
	 */
	String DOUBLE_BACKWARD_SLASH = "\\";

	/**
	 * String constant for wait time property for ghostScript.
	 */
	String GS_WAIT_TIME_PROPERTY = "gs.command.wait.time";

	/**
	 * String constant for wait time property for tesseract.
	 */
	String TESSERACT_WAIT_TIME_PROPERTY = "tesseract.command.wait.time";

	/**
	 * String constant for wait time property for imageMagik.
	 */
	String IM_WAIT_TIME_PROPERTY = "imageMagik.command.wait.time";

	/**
	 * Constant for integer 4.
	 */
	int FIRST_PAGE_OF_DOC = 0;

	/**
	 * Constant for integer 1.
	 */
	int SECOND_PAGE_OF_DOC = 1;

	/**
	 * Constant for integer 2.
	 */
	int THIRD_PAGE_OF_DOC = 2;

	/**
	 * Constant for integer 3.
	 */
	int THREE = 3;

	/**
	 * Constant for integer 4.
	 */
	int FOUR = 4;

	/**
	 * Constant for width of thumbnails shown in Web Scanner.
	 */
	int THUMBNAIL_IMAGE_WIDTH = 200;

	/**
	 * Constant for height of thumbnails shown in Web Scanner.
	 */
	int THUMBNAIL_IMAGE_HEIGHT = 150;

	/**
	 * Constant for weight start range for KV Extraction Pattern Value.
	 */
	double WEIGHT_START_RANGE = 0.1;

	/**
	 * Constant for weight end range for KV Extraction Pattern Value.
	 */
	double WEIGHT_END_RANGE = 1.0;

	/**
	 * Constant for HTTPS header.
	 */
	String HTTPS = "https";

	/**
	 * To hold the protocol parameter used in web.xml.
	 */
	String PROTOCOL_PARAM = "protocol";

	/**
	 * To hold protocol address https with slashes.
	 */
	String HTTPS_SLASH = "https://";
	/**
	 * Full page ocring with barcode detection file name.
	 */
	String FULLPAGE_BARCODE_OCRING_FILE = "FPR_Barcode.rsp";

	/**
	 * Full page ocring with multilanguage detection file name.
	 */
	String FULLPAGE_MULTILANGUAGE_OCRING_FILE = "FPR_MultiLanguage.rsp";

	/**
	 * Default value for number of rows to be shown in a table.
	 */
	int DEFAULT_NUMBER_OF_ROWS = 5;

	/**
	 * String constant for 0.
	 */
	String ZERO_STRING = "0";

	/**
	 * String constant for ghostscript parameter file name.
	 */
	String GS_ARGUMENT_FILENAME = "gsArguments.txt";

	/**
	 * String constant for ghostscript file name prefix.
	 */
	String GS_ARGUMENT_FILENAME_PERFIX = "@";

	/**
	 * String constant for double quote.
	 */
	String DOUBLE_QUOTE = "\"";

	/**
	 * STATUS_OK {@link String} Constant for status code of successful handling of service event.
	 */
	public final int STATUS_OK = 200;

	/**
	 * Constant used to retrieve the value corresponding to the super admin role from the property file.
	 */
	String USER_SUPER_ADMIN = "user.super_admin";

	/**
	 * Constant used to store the name of Serialized file used for storing the name already processed barcode files.
	 */
	String BARCODE_CLASSIFICATION_BACKUP_FILE_NAME = "barcode.ser";

	/**
	 * Constant for semi-colon character generally used as separator.
	 */
	public static final char SEMI_COLON = ';';

	/**
	 * OCR_CONFIDENCE_THRESHOLD {@link Float}- constant for default OCR confidence threshold.
	 */
	Float DEFAULT_OCR_CONFIDENCE_THRESHOLD = 90f;

	/**
	 * Weight {@link Float} Constant to store default value for weight.
	 */
	Float DEFAULT_WEIGHT = 1f;

	/**
	 * Constant for maximum confidence.
	 */
	float MAX_CONFIDENCE = 100;

	/**
	 * FPR rsp file name for PDF Processing.
	 */
	String PDF_FPR_FILE = "FPR_Pdf.rsp";

	/**
	 * TRUE is constant for true.
	 */
	String TRUE = "TRUE";

	/**
	 * Appender used in deleted batch class's unc folder path.
	 */
	String DELETED_BATCH_CLASS_APPENDER = "-deleted";

	/**
	 * Driver name for Oracle
	 */
	public final String ORACLE_DRIVER = "oracle.jdbc.OracleDriver";

	/**
	 * Constant for cmd language to be used by tesseract.
	 */
	String CMD_LANGUAGE_ENG = "eng";

	/**
	 * Constant for string 'PG0'.
	 */
	String PG0 = "PG0";

	/**
	 * Constant for string 'PG0'.
	 */
	String PG1 = "PG1";

	/**
	 * Constant for string 'PG0'.
	 */
	String PG2 = "PG2";

	/**
	 * Constant for string 'PG0'.
	 */
	String PG3 = "PG3";

	/**
	 * Constant for string 'PG4'.
	 */
	String PG4 = "PG4";
	/**
	 * Constant for last page
	 */
	String PGL = "PGL";

	String STRING_FORMAT_FOR_IMAGE_SEQUENCE = "%04d";

	String TESSERACT_HOCR_PLUGIN = "TESSERACT_HOCR";

	String DOT = ".";

	/**
	 * String constant for secondary back up folder which contains all the converted and non converted files.
	 */
	public final String PDF_AND_TIFF_BACK_UP_FOLDER_NAME = "_pdf_and_tiff_backup";

	/**
	 * Integer constant for number of active batch status.
	 */
	public final int NUMBER_OF_ACTIVE_BATCH_STATUS = 8;

	/**
	 * String constant for read mode.
	 */
	String READ_MODE = "r";

	/**
	 * Constant value to display when image cannot be converted to PNG
	 */
	public final String INCORRECT_IMAGE_CONVERSION_MESSAGE = "Problem generating png. from tiff file";

	/**
	 * last 5 digit reger pattern
	 */
	public final String LAST_5_DIGIT_REGEX = "-[0-9]{4}";
	/**
	 * last 9 digit regex pattern
	 */
	public final String LAST_9_DIGIT_REGEX = "-[0-9]{4}-[0-9]{4}";

	/**
	 * Acronym for Web service.
	 */
	public static final String WEB_SERVICE = "ws";

	/**
	 * Variable for separator for executed modules.
	 */
	public static final String EXECUTED_MODULE_ID_SEPARATOR = ";";

	/**
	 * Variable for underscore abc.
	 */
	String UNDERSCORE_ABC = "_abc";

	public static final String PRIORITY_RANGE_SEPERATOR = HYPHEN;

	public static final String PRIORITY_SET_SEPERATOR = "|";

	public static final String OR_REGEX = EphesoftStringUtil.concatenate(DOUBLE_BACKWARD_SLASH, PRIORITY_SET_SEPERATOR);

	public static final int MAX_BATCH_INSTANCE_PRIORITY = 100;

	/**
	 * Constant to hold file path of dcma-heart-beat.properties {@link String}
	 */
	public static final String DCMA_HEART_BEAT_PROPERTIES = "/META-INF/dcma-heart-beat/heart-beat.properties";

	/**
	 * {@link String} Constant to hold parameter name used to get heart beat number of pings.
	 */
	public static final String HEART_BEAT_NUMBER_OF_PINGS = "heartbeat.number_of_pings";
}
