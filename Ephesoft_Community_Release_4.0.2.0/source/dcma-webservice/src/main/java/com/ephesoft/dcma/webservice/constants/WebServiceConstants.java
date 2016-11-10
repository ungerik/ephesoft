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

package com.ephesoft.dcma.webservice.constants;

import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileType;

/**
 * The Class WebServiceConstants for constants used in web services.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jun 10, 2013 <br/>
 * @version $LastChangedDate: 2014-12-08 15:27:03 +0530 (Mon, 08 Dec 2014) $ <br/>
 *          $LastChangedRevision: 16592 $ <br/>
 */
public final class WebServiceConstants {

	// private constructor to prevent instantiation added for removing PMD.
	private WebServiceConstants() {

	}

	/**
	 * Constant for total files required for barcode extraction.
	 */
	public static final int BAR_CODE_EXTRACTION_FILES = 2;

	/**
	 * Constant for total files required in importing batch class.
	 */
	public static final int IMPORT_BATCH_CLASS_FILES = 2;
	/**
	 * Constant for web service parameter batch class name.
	 */
	public static final String BATCH_CLASS_NAME = "batchClassName";

	/**
	 * Constant for web service parameter batch class description.
	 */
	public static final String BATCH_CLASS_DESCRIPTION = "batchClassdescription";

	/**
	 * Constant for web service parameter batch class priority.
	 */
	public static final String BATCH_CLASS_PRIORITY = "batchClassPriority";

	/**
	 * Constant for web service parameter batch class unc folder path.
	 */
	public static final String BATCH_UNC_FOLDER_NAME = "batchUNCFolder";

	/**
	 * Constant for web service parameter batch class grid workflow deployment variable.
	 */
	public static final String IS_GRIDWORKFLOW = "gridWorkflow";

	/**
	 * Constant for web service parameter batch class identifier.
	 */
	public static final String BATCH_CLASS_IDENTIFIER = "batchClassIdentifier";

	/**
	 * Constant for web service parameter batch class identifier.
	 */
	public static final String COPY_BATCH_CLASS_IDENTIFIER = "copyBatchClassIdentifier";

	/**
	 * Constant for web service parameter document type name.
	 */
	public static final String DOCUMENT_TYPE_NAME = "documentTypeName";

	/**
	 * Constant for web service parameter document type description.
	 */
	public static final String DOCUMENT_TYPE_DESCRIPTION = "documentTypeDescription";

	/**
	 * Constant for web service parameter minimum confidence threshold.
	 */
	public static final String MIN_CONFIDENCE_THRESHOLD = "minConfidenceThreshold";

	/**
	 * Constant for web service parameter document type processing file.
	 */
	public static final String FIRST_PAGE_PROJECT_FILE = "firstPageProjectFile";

	/**
	 * Constant for web service parameter document type hidden variable value.
	 */
	public static final String HIDDEN = "hidden";

	/**
	 * Constant for "TESSERACT_HOCR".
	 */
	public static final String TESSERACT_HOCR_PLUGIN = "TESSERACT_HOCR";

	/**
	 * Constant for "RECOSTAR_HOCR".
	 */
	public static final String RECOSTAR_HOCR_PLUGIN = "RECOSTAR_HOCR";

	/**
	 * Constant for "RECOSTAR_EXTRACTION_PLUGIN"
	 */
	public static final String RECOSTAR_EXTRACTION_PLUGIN = "RECOSTAR_EXTRACTION";

	/**
	 * Constant for plugin name NUANCE_HOCR.
	 */
	public static final String NUANCE_HOCR_PLUGIN = "NUANCE_HOCR";

	/**
	 * Constant for "CREATE_THUMBNAILS".
	 */
	public static final String CREATE_THUMBNAILS_PLUGIN = "CREATE_THUMBNAILS";

	/**
	 * Constant for "ON".
	 */
	public static final String SWITCH_ON = "ON";

	/**
	 * Constant for "0".
	 */
	public static final String ZERO = "0";

	/**
	 * Constant for empty string.
	 */
	public static final String EMPTY_STRING = "";

	/**
	 * Constant for any sort of error occurred.
	 */
	public static final String ERROR = "Error occurred";

	/**
	 * Constant for any success.
	 */
	public static final String SUCCESS = "Success";

	/**
	 * Default url entry.
	 */
	public static final String DEFAULT_URL = "http://www.ephesoft.com/wiki/index.php?title=Product_Documentation#Ephesoft_Web_Service";

	/**
	 * Default message for any kind of internal server error.
	 */
	public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error. Please check logs for further details.\n";

	/** The internal server error code. */
	public static final int INTERNAL_SERVER_ERROR_CODE = 500001;

	/**
	 * Constant used in utility class.
	 */
	public static final String HTTP_STATUS = " Http Status: ";

	/** The unauthorised access exception message. */
	public static final String UNAUTHORISED_ACCESS_EXCEPTION_MESSAGE = "User not authorized to perform the action.";

	/** The unauthorised access exception code. */
	public static final int UNAUTHORISED_ACCESS_EXCEPTION_CODE = 403001;

	/** The batch restarted success message. */
	public static final String BATCH_RESTARTED_SUCCESS_MESSAGE = "Batch restarted successfully.";

	/** The batch restarted fail message. */
	public static final String BATCH_RESTARTED_FAIL_MESSAGE = "Unable to restart batch.";

	/** The batch deleted success message. */
	public static final String BATCH_DELETED_SUCCESS_MESSAGE = "Batch deleted successfully.";

	/** The batch deleted failure message. */
	public static final String BATCH_DELETED_FAILURE_MESSAGE = "Failure while deleting batch instance.";

	/** The user role added success message. */
	public static final String USER_ROLE_ADDED_SUCCESS_MESSAGE = "User role is added successfully to batch instance.";

	/** The user role added failure message. */
	public static final String USER_ROLE_ADDED_FAILURE_MESSAGE = "Failure while adding roles to batch instance: ";

	/** The invalid role error message. */
	public static final String INVALID_ROLE_ERROR_MESSAGE = "Invalid role provided.";

	/** The invalid role error code. */
	public static final int INVALID_ROLE_ERROR_CODE = 422001;

	/** The invalid batch instance status message. */
	public static final String INVALID_BATCH_INSTANCE_STATUS_MESSAGE = "Invalid value for batch instance status.";

	/** The invalid batch instance status code. */
	public static final int INVALID_BATCH_INSTANCE_STATUS_CODE = 422002;

	/** The invalid batch class id message. */
	public static final String INVALID_BATCH_CLASS_ID_MESSAGE = "Batch Class does not exist with batch class identifier.";

	/** The invalid batch class id code. */
	public static final int INVALID_BATCH_CLASS_ID_CODE = 422003;

	/** The invalid module name to restart message. */
	public static final String INVALID_MODULE_NAME_TO_RESTART_MESSAGE = "Invalid Module name provided for restart.";

	/** The invalid module name to restart code. */
	public static final int INVALID_MODULE_NAME_TO_RESTART_CODE = 422004;

	/** The invalid batch instance identifier message. */
	public static final String INVALID_BATCH_INSTANCE_IDENTIFIER_MESSAGE = "Batch instance does not exist with batch instance identifier  ";

	/** The invalid batch instance identifier code. */
	public static final int INVALID_BATCH_INSTANCE_IDENTIFIER_CODE = 422005;

	/** The batch instance cannot be restarted message. */
	public static final String BATCH_INSTANCE_CANNOT_BE_RESTARTED_MESSAGE = "Batch exists with incorrect status to be restarted.";

	/** The batch instance cannot be restarted code. */
	public static final int BATCH_INSTANCE_CANNOT_BE_RESTARTED_CODE = 422006;

	/** The input xml not found message. */
	public static final String INPUT_FILES_NOT_FOUND_MESSAGE = "The required input XML is not found. ";

	/** The input xml not found message. */
	public static final String INPUT_XML_NOT_FOUND_MESSAGE = "Required XML file not found or is invalid. ";

	/** The invalid input for barcode extraction. */
	public static final String INVALID_FILES_FOR_BARCODE_EXTRACTION = "Invalid input for barcode extraction. Required 1 tif and 1 input xml file";

	/** The input xml not found code. */
	public static final int INPUT_XML_NOT_FOUND_CODE = 422007;

	/** The parameter xml incorrect message. */
	public static final String PARAMETER_XML_INCORRECT_MESSAGE = "Parameter XML is incorrect";

	/** The parameter xml incorrect code. */
	public static final int PARAMETER_XML_INCORRECT_CODE = 422008;

	/** The batch instance cannot be restarted message. */
	public static final String BATCH_INSTANCE_CANNOT_BE_DELETED_MESSAGE = "Batch exists with incorrect status to be deleted.";

	/** The batch instance cannot be restarted code. */
	public static final int BATCH_INSTANCE_CANNOT_BE_DELETED_CODE = 422009;

	/** The validation exception message. */
	public static final String VALIDATION_EXCEPTION_MESSAGE = "Validation Failed.";

	/** The validation exception code. */
	public static final int VALIDATION_EXCEPTION_CODE = 422022;

	/** The error retrieving folder path message. */
	public static final String ERROR_RETRIEVING_FOLDER_PATH_MESSAGE = "Error retrieving sampleBaseFolderPath";

	/** The error retrieving folder path code. */
	public static final int ERROR_RETRIEVING_FOLDER_PATH_CODE = 500007;

	/** The error retrieving thumbnail dimension message. */
	public static final String ERROR_RETRIEVING_THUMBNAIL_DIMENSION_MESSAGE = "Error retrieving thumbnailH/Error retrieving thumbnailW";

	/** The error retrieving thumbnail dimension code. */
	public static final int ERROR_RETRIEVING_THUMBNAIL_DIMENSION_CODE = 500008;

	/** The error retrieving thumbnail type message. */
	public static final String ERROR_RETRIEVING_THUMBNAIL_TYPE_MESSAGE = "Error retrieving thumbnailType";

	/** The error retrieving thumbnail type code. */
	public static final int ERROR_RETRIEVING_THUMBNAIL_TYPE_CODE = 500009;

	/** The no image magic properties found message. */
	public static final String NO_IMAGE_MAGIC_PROPERTIES_FOUND_MESSAGE = "No Image Magic Properties found in DB. Could not generate sample Thumbnails.";

	/** The no image magic properties found code. */
	public static final int NO_IMAGE_MAGIC_PROPERTIES_FOUND_CODE = 500010;

	/** The improper input to server message. */
	public static final String IMPROPER_INPUT_TO_SERVER_MESSAGE = "Improper input to server";

	/** The improper input to server code. */
	public static final int IMPROPER_INPUT_TO_SERVER_CODE = 500002;

	/**
	 * Constant for developers message for utility class.
	 */
	public static final String CLASS_WEB_SERVICE_UTILITY = "Class:WebServiceUtility";

	/** The batch instance already deleted message. */
	public static final String BATCH_INSTANCE_ALREADY_DELETED_MESSAGE = "Batch Instance already deleted.";

	/** The batch instance already deleted code. */
	public static final int BATCH_INSTANCE_ALREADY_DELETED_CODE = 422010;

	public static final String BATCH_IMPORTED_SUCCESS_MESSAGE = "Batch imported successfully.";

	public static final String BATCH_IMPORTED_FAILURE_MESSAGE = "Unable to import batch.";

	public static final String BATCH_UPLOADED_SUCCESS_MESSAGE = "Batch uploaded successfully.";

	public static final String BATCH_UPLOADED_FAILURE_MESSAGE = "Batch could not be uploaded.";

	public static final String MODULE_CANNOT_BE_RESTARTED_MESSAGE = "Cannot restart the batch. Please try after sometime.";

	public static final int MODULE_CANNOT_BE_RESTARTED_CODE = 500004;

	public static final String BATCH_INSTANCE_LOCKED_MESSAGE = "Batch instance is locked by some user.";

	public static final int BATCH_INSTANCE_LOCKED_CODE = 500003;

	public static final String INVALID_BATCH_INSTANCE_NAME_MESSAGE = "Invalid batch instance name provided.";

	public static final int INVALID_BATCH_INSTANCE_NAME_CODE = 422011;

	public static final String NO_USER_ROLE_EXCEPTION_MESSAGE = "Invalid Credentials to process the request.";

	public static final int NO_USER_ROLE_EXCEPTION_CODE = 422012;

	public static final String UNSUPPORTED_FILE_TYPE_EXCEPTION_MESSAGE = "Invalid file type provided. Expected only tif, tiff or pdf files";

	public static final int UNSUPPORTED_FILE_TYPE_EXCEPTION_CODE = 422013;

	public static final String BATCH_NAME_ALREADY_EXIST_EXCEPTION_MESSAGE = "The batch name already exists. Please choose a different name and try again.";

	public static final int BATCH_NAME_ALREADY_EXIST_EXCEPTION_CODE = 422014;

	/**
	 * Constant for last page type.
	 */
	public static final String LAST_PAGE = "_Last_Page";
	/**
	 * Constant for middle page type.
	 */
	public static final String MIDDLE_PAGE = "_Middle_Page";
	/**
	 * Constant for first page type.
	 */
	public static final String FIRST_PAGE = "_First_Page";

	/**
	 * Constant for Input Image Not Found
	 */
	public static final int INPUT_FILES_NOT_FOUND_CODE = 422015;

	public static final int INPUT_IMAGE_NOT_FOUND_CODE = 422015;

	public static final String INPUT_IMAGE_NOT_FOUND_MESSAGE = "Input Image is not found.Please upload tiff/tif/zip file";

	/**
	 * Constant used when cannot parse INPUT XML
	 */
	public static final int INPUT_XML_NOT_ABLE_TO_PARSE_CODE = 420017;
	/**
	 * Constant for Error found while Uploading Images
	 */
	public static final int INPUT_IMAGE_NOT_ABLE_TO_UPLOAD = 422016;

	public static final String INPUT_IMAGE_NOT_ABLE_TO_UPLOAD_MESSAGE = "Not able to upload image.Please check logs for detailed description";

	/** Response code when input parameters are invalid */
	public static final int INVALID_PARAMETERS_CODE = 422730;

	/** Constant used when parameters are invalid for barCode Extraction */
	public static final String INVALID_PARAMETERS_FOR_BARCODE_EXTRACTION_MESSAGE = "Invalid number of files. Expected input should be 1 tif/tiff file and 1 xml file";

	/** Message to be displayed when invalid arguments are there for recostar extraction */
	public static final String INVALID_ARGUMENTS_FOR_RECOSTAR_EXTRACTION = "Invalid files.Windows supported formats are tiff/png and xml.Also tiff/png, input xml and RSP are acceptable. Linux supported format is tiff and xml. It also supports tiff, xml and ZON file";

	/** Error Code when XML arguments are invalid */
	public static final int INVALID_ARGUMENTS_IN_XML_INPUT_CODE = 422731;

	/** Message when invalid arguments are there in XML File */
	public static final String INVALID_ARGUMENTS_IN_XML_INPUT_MESSAGE = "Input xml file has missing or invalid values";

	/** Message when Batch cannot be shown to the user */
	public static final String BATCH_CANNOT_BE_SHOWN_TO_USER = "User not authorized to view this batch class id:";

	/** Error code when batch cannot be shown to the user */
	public static final int BATCH_CANNOT_BE_SHOWN_TO_USER_STATUS_CODE = 422732;

	/** Message When XML input cannot be marshalled */
	public static final String CANNOT_MARSHALL_RESOURCE_MESSAGE = "Exception occured while marshalling the resource. Please contact administrator. ";

	/** Error Code when XML cannot be marshalled */
	public static final int CANNOT_MARSHALL_RESOURCE_CODE = 422733;

	/** Message When DCMA Exception is not caught */
	public static final String DCMA_EXCEPTION_MESSAGE = "Error in processing request. Make sure the plugin configurations are correct. Detailed exception is ";

	/** Error Code when DCMA Exception occurs */
	public static final int DCMA_EXCEPTION_CODE = 422735;

	/** HOCR File Name Parameter in Request */
	public static final String HOCR_FILE_NAME_PARAMETER = "hocrFile";

	/** When Invalid Files are sent as a part of request */
	public static final String INVALID_FILES_SEND = "Invalid types of files send";

	/** Message When No batch Instance corresponding to the batch Instance ID are found */
	public static final String NO_BATCH_INSTANCE_FOUND = "No Batch Instance Found";

	/** Message When No batch Class corresponding to the batch Instance ID are found */
	public static final String NO_BATCH_CLASS_FOUND = "No Batch Instance Found";

	/** Message When request is not a multipart request */
	public static final String INVALID_MULTIPART_REQUEST = "Request expected some files to be a part of the request";

	/** Total Files for Recostar Extraction Request */
	public static final int TOTAL_FILES_FOR_RECOSTAR = 2;

	/** Message when multiPage page tif is uploaded and single page is required */
	public static final String SINGLE_PAGE_TIF_REQUIRED = "Invalid input, only single page tiff/tif required as an input";

	/** Message when Input tif is not found */
	public static final String INPUT_TIF_NOT_FOUND = "Tif which was expected as an input is not found";

	/** Message when Input PNG Not found */
	public static final String INPUT_PNG_NOT_FOUND = "PNG Image which was expected was not found";

	/** Message when Invalid Extraction API is found in the header */
	public static final String INVALID_EXTRACTION_API = "Invalid extraction api passed. Allowed API's are BARCODE_EXTARCTION, RECOSTAR_EXTARCTION, NUANCE_EXTRACTION, REGULAR_REGEX_EXTRACTION, KV_EXTRACTION, FUZZY_DB";

	/** Total Number of Files for Regex Extraction */
	public static final int TOTAL_FILES_FOR_REGEX_EXTRACTION = 2;

	/** Header for regular regex Extraction */
	public static final String REGULAR_REGEX_EXTRACTION = "REGULAR_REGEX_EXTRACTION";

	/** Message when regex is not configured for the batch Class */
	public static final String REGEX_NOT_CONFIGURED_FOR_BATCH_CLASS = "Regular Regex not configured for the Batch Class. Please select an appropriate Batch Class";

	/** Error code when the Regex is not configured for the batch class */
	public static final int REGULAR_REGEX_NOT_CONFIGURED_CODE = 400018;

	public static final int PAGE_PROCESSING_FILE_NOT_PRESENT_FOR_LINUX_CODE = 400;

	/** Message when there are invalid arguments for Regex extraction */
	public static final String INVALID_ARGUMENTS_FOR_REGEX_EXTRACTION = "Invalid Number of files for Regex Extraction. Expected a HOCR and XML file";

	/** Message when HOCR file is not found in the request */
	public static final String HOCR_FILE_NOT_FOUND = "Could not find the HOCR File at server";

	/** Message when NO DLF is found for the Batch class */
	public static final String NO_DLF_FOUND_FOR_BATCH_CLASS = "No DLF Found for the Batch Class ";

	/** Message when HOCR file parameter are missing */
	public static final String HOCR_FILE_PARAMETER_MISSING = "Request Parameter Of HOCR FILE is missing";

	/** Total files for KV Extraction */
	public static final int TOTAL_FILES_FOR_KV_EXTRACTION = 2;

	/** Message when there are invalid arguments for KV Extraction */
	public static final String INVALID_ARGUMENTS_FOR_KV_EXTRACTION = "Invalid number of Files for KV Extraction. Requires one Input XML and one HOCR File";

	/** Message when there are invalid arguments for Create OCR */
	public static final String INVALID_ARGUMENTS_FOR_CREATE_OCR = "Invalid number of files. For Recostar we are supposed 3 files of type XML, RSP and tif/tiff/png. For Nuance/Tesseract we are supposed 2 files of type XML and tiff/tif/png" ;

	/** Message when there are invalid OCRing Tool is found */
	public static final String INVALID_OCRING_TOOL = "Please select valid tool for generating OCR file.";

	/** Message when input tif is not found */
	public static final String INPUT_TIF_PNG_NOT_FOUND = "Requires a tif/png file which is not found";

	/** Message when there is an error while generating ThreadPool */
	public static final String ERROR_WHILE_GENERATING_THREADPOOL = "Exception while generating ocr using threadpool\n";

	/** Message when there is an error while copying Result File */
	public static final String ERROR_WHILE_COPYING_RESULT_FILE = "Error while copying result file.\n";

	/** Error Message while creating the zipped file */
	public static final String ERROR_WHILE_CREATING_ZIPPED_FILE = "Error while creating Zipped File\n";

	/** When there is an Invalid input for create HOCR */
	public static final String CREATE_HOCR_INVALID_INPUT = "Invalid number of files. We are supposed only 2 files each of type:Input XML file and tif/tiff file.";

	/** When location type is invalid */
	public static final String INVALID_LOCATION_TYPE = "Please provide the location type. Accepted values are: TOP, RIGHT, LEFT, BOTTOM, TOP_RIGHT, TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT.";

	/** When the KEY pattern for the request is invalid */
	public static final String INVALID_KEY_VALUE_PATTERN = "Please provide the key and value patterns.";

	/** When the multiplier for Advanced KV is invalid */
	public static final String INVALID_MULTIPLIER_FOR_ADVANCED_KV = "Please provide the multiplier for Advanced KV extraction. Range of values is between 0 to 1.";

	/** Error When Invalid KV fetch value for Advcanced KV extraction is found in the XML */
	public static final String INVALID_KV_FETCH_VALUE = "Please provide the KVFetchValue for Advanced KV extraction. Expected values are:ALL, FIRST, LAST";

	/** Error Message when parameters for Advance KV are invalid */
	public static final String INVALID_PARAM_LIST_FOR_ADVANCE_KV = "Please provide the length value greater than zero with advanced KV extraction.";

	/** Error Message when there are invalid number of words for advance KV */
	public static final String INVALID_NO_OF_WORDS_ADVANCED_KV = "Please provide the words greater than zero with advanced KV extraction.";

	/** Error Message when specified width for advanced KV is invalid */
	public static final String INVALID_WIDTH_FOR_ADVANCED_KV = "Please provide the width greater than zero with ADVANCED KV";

	/** Path to the fixed-form-extraction folder */
	public static final String FIXED_FORM_EXTRACTION_FOLDER = "fixed-form-extraction";

	/** Message when invalid tif file Name is found */
	public static final String INVALID_TIF_FILE_NAME = "Please enter valid input file name.Input file name should end with tif/tiff";

	/** When no configuration property is found against RECOSTAR HOCR */
	public static final String NO_PROPERTY_FOR_RECOSTAR_HOCR = "Unable to retrieve properties for RECOSTAR_HOCR Plugin.";

	/** When no configuration property is found against TESSERACT HOCR */
	public static final String NO_PROPERTY_FOR_TESSERACT_HOCR = "Unable to find the property for TESSERACT_HOCR Plugin";

	/** When no configuration property is found against NUANCE HOCR */
	public static final String NO_PROPERTY_FOR_NUANCE_HOCR = "Unable to find the property for NUANCE_HOCR Plugin";

	/** When No Colour switch is found for Recostar */
	public static final String NO_COLOUR_SWITCH = "No Colour Switch Properties Found for the Recostar ";

	/** When No project file for RecoStar extraction is found */
	public static final String NO_PROJECT_FILE = "No Project File Found for Recostar Processing";

	/** Tesseract's Currrent Version Support */
	public static final String TESSERACT_CURRENT_VERSION = "tesseract_version_3";

	/** NO COLOR Switch found for Tesseract */
	public static final String NO_TESSERACT_COLOR_SWITCH = "No Plugin Configuration for Tesseract Colout switch found\n";

	/** Error message for no color Switch found for Nuance. */
	public static final String NO_NUANCE_COLOR_SWITCH = "No Plugin Configuration for Nuance Color switch found\n";

	/** When Tesseract doesnot have Language suipport */
	public static final String NO_TESSERACT_LANGUAGE_SUPPORT = "No Plugin Configuration for Tesseract Language Found\n";

	/** When Some file is not found */
	public static final String FILE_NOT_FOUND = "System Error. File not found while processing. File Name ";

	public static final String INPUT_XML_NOT_ABLE_TO_PARSE_MESSAGE = "Error in parsing input XML.Please check input XML";

	/**
	 * <code>I_TEXT_SEARCHABLE_PDF_PARAMETER</code> is a field which is by default set to 'PDF' and can be changed to PDF-A.
	 */

	public static final String NO_PROJECT_PROCESS_FILE_FOUND_FOR_DOCUMENT = "Project processing file not found for  batch class";

	public static final String NO_DOCUMENT_FOUND_FOR_BATCH = "Document type not found for Batch";

	public static final String I_TEXT_SEARCHABLE_PDF_PARAMETER = "PDF";

	/** The batch instance's status invalid message */
	public static final String BATCH_INSTANCE_STATUS_INVALID_MESSAGE = "Batch Instance should be in READY_FOR_REVIEW or READY_FOR_VALIDATION state.";

	/** The batch instance's status invalid code. */
	public static final int BATCH_INSTANCE_STATUS_INVALID_CODE = 422018;

	/**
	 * if the parameter is provided in incorrect case
	 */
	public static final String CHECK_PARAMETER_CASE = "Also check parameter case. ";
	/**
	 * Improper number of files
	 */
	public static final String IMPROPER_NUMBER_OF_FILES_MESSAGE = "Invalid number or no files. ";

	/**
	 * String representation of error response at server
	 */
	public static final String ERROR_RESPONSE_AT_SERVER_MESSAGE = "Error response at server: ";

	/**
	 * String representation of deleting working directory
	 */
	public static final String DELETING_WORKING_DIR_MESSAGE = "deleting working directory";
	/**
	 * Message thrown if the request is not an instance of {@link DefaultMultiPartRequest} for create Searchable PDF
	 */
	public static final String NOT_MULTIPART_REQUEST_MESSAGE = "No or invalid input provided. ";

	/**
	 * Message thrown if the input files are null or not in proper number
	 */
	public static final String IMPROPER_NUMBER_OF_FILES_CREATE_SEARCHABLE_PDF_MESSAGE = "Invalid number or no files. Only 2 files each of type: RSP and tif/tiff are expected.";

	/**
	 * Message thrown if Unable to input file
	 */
	public static final String UNABLE_TO_INPUT_FILE_MESSAGE = "Unable to input file. ";

	/**
	 * Message thrown if not an single page TIFF
	 */
	public static final String NOT_SINGLE_PAGE_TIFF_MESSAGE = "Improper input to server. Expected only one single page tiff file. Returning without processing the results. ";

	/**
	 * Message thrown if no tif/tiff file found for processing
	 */
	public static final String NO_TIFF_FILE_FOUND_MESSAGE = "No or Invalid tif/tiff file found for processing. ";

	/**
	 * Message thrown if invalid Project File
	 */
	public static final String INVALID_PROJECT_FILE_MESSAGE = "Invalid project file. Please verify the project file. ";

	/**
	 * Message thrown if no HOCR pages found
	 */
	public static final String NO_HOCR_PAGES_FOUND_MESSAGE = "No Hocr pages found. ";

	/**
	 * Message thrown if not able to OCR pages
	 */
	public static final String NOT_ABLE_TO_OCR_MESSAGE = "Not able to OCR pages. ";
	/**
	 * Message thrown if error in generating searchable PDF
	 */
	public static final String ERROR_GENERATING_SEARCHABLE_PDF_MESSAGE = "Error in generating searchable pdf. ";

	/**
	 * Message thrown if error in generating searchable PDF through ItextPDFCreator
	 */
	public static final String ERROR_GENERATING_SEARCHABLE_PDF_ITEXTPDFCREATOR_MESSAGE = "Error in generating searchable pdf through ItextPDFCreator. ";

	/**
	 * Message thrown if unable to generate output stream
	 */
	public static final String CANNOT_GENERATE_STREAM_MESSAGE = "Unable to generate output stream. ";

	/**
	 * Message thrown if unable to split multipage tiff file
	 */
	public static final String CANNOT_SPLIT_MULTI_PAGE_FILE_MESSAGE = "Unable to split multipage file. ";

	/**
	 * Message thrown if unable to convert TIFF to PDF
	 */
	public static final String CANNOT_CONVERT_TIFF_TO_PDF_MESSAGE = "Unable to convert TIFF to PDF. ";

	/**
	 * Message thrown if unable to convert TIFF to PDF
	 */
	public static final String CANNOT_CLASSIFY_BARCODE_IMAGE_MESSAGE = "Unable to classify barcode image. ";

	/**
	 * Message thrown if unable to convert TIFF to PDF
	 */
	public static final String CANNOT_CLASSIFY_HOCR_MESSAGE = "Unable to classify hocr image. ";

	/**
	 * Message thrown if input file should only be pdf with GhostScript(GS) tool
	 */
	public static final String ONLY_PDF_WITH_GS_TOOL_MESSAGE = "Only PDF files expected with GhostScript tool. ";

	/**
	 * Message thrown if cannot execute thread instance using thread pool
	 */
	public static final String THREAD_INSTANCE_FAILED_MESSAGE = "Cannot execute thread instance using thread pool. ";

	/**
	 * Message thrown if batch class ID is null or empty
	 */
	public static final String BATCH_ID_NULL_OR_EMPTY_MESSAGE = "Batch class Id is either null or empty. ";

	/**
	 * Message is appended behind the names of plugin which does not exist
	 */

	public static final String PLUGIN_DOES_NOT_EXIST_MESSAGE = "plugin does not exist.check your application ";

	/**
	 * Message thrown if the input files are null or not in proper number
	 */
	public static final String IMPROPER_NUMBER_OF_FILES_CLASSIFY_IMAGE_MESSAGE = "Improper input to server. Either extra or no file found. Expected only one tiff file. Returning without processing the results. ";

	/**
	 * Message thrown if the input files are null or not in proper number
	 */
	public static final String IMPROPER_NUMBER_OF_FILES_CLASSIFY_HOCR_MESSAGE = "Improper input to server. Either extra or no files found. Expected only one html/xml file. Returning without processing the results. ";

	/**
	 * Message thrown if the input files are null or not in proper number
	 */
	public static final String IMPROPER_NUMBER_OF_FILES_CLASSIFY_MULTIPAGE_HOCR_MESSAGE = "Improper input to server. Either extra or no files found. Expected only one zip file. Returning without processing the results. ";

	/**
	 * Message thrown if unable to classify image
	 */
	public static final String CANNOT_CLASSIFY_IMAGE_MESSAGE = "Unable to classify image. ";

	/**
	 * Message thrown if create Thumbnails Height or width or output image parameters does not exist for the specified batch class id.
	 */

	public static final String INVALID_PARAM_CREATE_THUMBNAIL_PLUGIN_MESSAGE = "Create Thumbnails Height or width or output image parameters does not exist for the specified batch class id. ";

	/**
	 * Message thrown if incomplete properties of the Document assembler plugin for the specified batch class id.
	 */

	public static final String INVALID_PARAM_DOCUMENT_ASSEMBLER_PLUGIN_MESSAGE = " Incomplete properties of the Document assembler plugin for the specified batch class id. ";

	/**
	 * Message thrown incomplete properties of the Search Classification plugin for the specified batch class id.
	 */

	public static final String INVALID_PARAM_SEARCH_CLASSIFICATION_PLUGIN_MESSAGE = " Incomplete properties of the Search Classification plugin for the specified batch class id. ";

	/**
	 * Message thrown incomplete properties of the FuzzyDB plugin for the specified batch class id.
	 */

	public static final String INVALID_PARAM_FUZZY_DB_PLUGIN_MESSAGE = " Incomplete properties of the Fuzzy DB plugin for the specified batch class id. ";

	/**
	 * Message thrown if incomplete properties of the Barcode reader plugin for the specified batch class id.
	 */

	public static final String INVALID_PARAM_BARCODE_READER_PLUGIN_MESSAGE = " Incomplete properties of the Barcode reader plugin for the specified batch class id. ";

	/**
	 * Message thrown if Classify Images comp metric or fuzz percent or max results does not exist for the specified batch class id .
	 */

	public static final String INVALID_PARAM_CLASSIFY_IMAGE_PLUGIN_MESSAGE = " Classify Images comp metric or fuzz percent or max results does not exist for the specified batch class id. ";

	/**
	 * Constant th
	 */
	public static final String THUMBNAIL_PREFIX = "th";

	/**
	 * Parameter locationType not found for extractKv webservice
	 */
	public static final String LOCATION_TYPE_NOT_FOUND_EXTRACT_KV_MESSAGE = "Please provide the location type. Accepted values are: TOP, RIGHT, LEFT, BOTTOM, TOP_RIGHT, TOP_LEFT, BOTTOM_LEFT, BOTTOM_RIGHT. ";

	/**
	 * Parameter key or value pattern not found for extractKv webservice
	 */
	public static final String KEY_VALUE_PATTERN_NOT_FOUND_EXTRACT_KV_MESSAGE = "Please provide the key and value patterns. ";

	/**
	 * Parameter multiplier not valid for extractKv webservice
	 */
	public static final String MULTIPLIER_NOT_VALID_EXTRACT_KV_MESSAGE = "Please provide the multiplier for Advanced KV extraction. Range of values is between 0 to 1. ";

	/**
	 * Parameter KVFetchValue not valid for extractKv webservice
	 */
	public static final String KV_FETCH_VALUE_NOT_VALID_EXTRACT_KV_MESSAGE = "Please provide the KVFetchValue for Advanced KV extraction. Expected values are:ALL, FIRST, LAST. ";

	/**
	 * Parameter length not valid for extractKv webservice
	 */
	public static final String LENGTH_NOT_VALID_EXTRACT_KV_MESSAGE = "Please provide the length value greater than zero with advanced KV extraction. ";

	/**
	 * Parameter width not valid for extractKv webservice
	 */
	public static final String WIDTH_NOT_VALID_EXTRACT_KV_MESSAGE = "Please provide the width value greater than zero with advanced KV extraction. ";

	/**
	 * Parameter noOfWords not valid for extractKv webservice
	 */
	public static final String NUMBER_OF_WORDS_NOT_VALID_EXTRACT_KV_MESSAGE = "Please provide positive value for no of words with advanced KV extraction. ";

	/**
	 * Message thrown if not able to extractKv document field from HOCR
	 */
	public static final String CANNOT_EXTRACT_KV_DOCUMENT_FIELD_FROM_HOCR_MESSAGE = "Cannot extract Key value document field from HOCR provided. ";

	/**
	 * Message thrown if not able to extractFuzzyDb document field from HOCR
	 */
	public static final String CANNOT_EXTRACT_EXTRACT_FUZZY_DB_MESSAGE = "Cannot extract document level for specified document type. ";

	/**
	 * Message thrown if not able to extractFuzzyDb document field from HOCR
	 */
	public static final String CANNOT_EXTRACT_EXTRACT_FIELD_VALUE_MESSAGE = "Cannot extract field value for specified document type. ";

	/** The input zip not found message. */
	public static final String INPUT_ZIP_NOT_FOUND_MESSAGE = "Required ZIP file not found. ";

	/**
	 * Message thrown if fieldValue is null or empty
	 */
	public static final String FIELD_VALUE_NULL_OR_EMPTY_MESSAGE = "fieldValue is either null or empty. ";

	/**
	 * Message thrown if document Level Fields doesn't exist for document type
	 */

	public static String DOCUMENT_TYPE_INVALID_MESSAGE = "Document Level Fields doesn't exist for given document type. ";

	/**
	 * Constant for "FUZZYDB".
	 */
	public static final String FUZZY_DB_PLUGIN = "FUZZYDB";

	/**
	 * Code for Invalid input while creating searchable PDF
	 */
	public static final int INVALID_INPUT_CREATE_SEARCHABLE_PDF_CODE = 500700;

	/**
	 * Code for error in creating searchable PDF
	 */
	public static final int CANNOT_GENERATE_SEARCHABLE_PDF_CODE = 500701;

	/**
	 * Code for Invalid input while converting TIFF to PDF
	 */
	public static final int INVALID_INPUT_CONVERT_TIFF_TO_PDF_CODE = 500702;

	/**
	 * Code for error in converting TIFF to PDF
	 */
	public static final int CANNOT_CONVERT_TIFF_TO_PDF_CODE = 500703;

	/**
	 * Code for error in splitting multipage file
	 */
	public static final int CANNOT_SPLIT_MULTI_PAGE_FILE_CODE = 500704;

	/**
	 * Code for Invalid input while splitting multipage file
	 */
	public static final int INVALID_INPUT_SPLIT_MULTI_PAGE_FILE_CODE = 500705;

	/**
	 * Code for not able to OCR pages
	 */
	public static final int NOT_ABLE_TO_OCR_CODE = 500706;

	/**
	 * Code for not able generate PDF through {@link com.ephesoft.dcma.imagemagick.impl.ITextPDFCreator}
	 */
	public static final int ERROR_GENERATING_SEARCHABLE_PDF_ITEXTPDFCREATOR_CODE = 500707;

	/**
	 * Code for not able generate output stream
	 */
	public static final int CANNOT_GENERATE_STREAM_CODE = 500708;

	/**
	 * Code for no TIFF file found
	 */
	public static final int NO_TIFF_FILE_FOUND_CODE = 500709;

	/**
	 * Code for improper number of files
	 */
	public static final int IMPROPER_NUMBER_OF_FILES_CODE = 500710;

	/**
	 * Code for improper number of files
	 */
	public static final int INVALID_PROJECT_FILE_CODE = 500711;

	/**
	 * Code for not an single page TIFF file
	 */
	public static final int NOT_SINGLE_PAGE_TIFF_CODE = 422712;

	/**
	 * Code for not an multipart request
	 */

	public static final int NOT_MULTIPART_REQUEST_CODE = 500713;

	/**
	 * Code for not able to execute thread instance
	 */
	public static final int THREAD_INSTANCE_FAILED_CODE = 500714;

	/**
	 * Code for batch class ID null or empty
	 */
	public static final int BATCH_ID_NULL_OR_EMPTY_CODE = 422713;

	/**
	 * Code for plugin does not exist
	 */
	public static final int PLUGIN_DOES_NOT_EXIST_CODE = 500716;

	/**
	 * Expected number of files in convert TIFF to PDF
	 */
	public static final int EXPECTED_NUMBER_OF_FILES_CONVERT_TIFF_TO_PDF = 2;

	/**
	 * Expected number of files in classifyImage
	 */
	public static final int EXPECTED_NUMBER_OF_FILES_CLASSIFY_IMAGE = 1;

	/**
	 * Expected number of files in extractKv
	 */
	public static final int EXPECTED_NUMBER_OF_FILES_EXTRACT_KV = 2;

	/**
	 * Expected number of files in extractFuzzyDB
	 */
	public static final int EXPECTED_NUMBER_OF_FILES_EXTRACT_FUZZY_DB = 1;

	/**
	 * Expected number of files in extractFieldValueFromHOCR
	 */
	public static final int EXPECTED_NUMBER_OF_FILES_EXTRACT_FIELD_VALUE_HOCR = 1;

	/**
	 * Expected number of files in classifyBarcodeImage
	 */
	public static final int EXPECTED_NUMBER_OF_FILES_CLASSIFY_BARCODE_IMAGE = 1;

	/**
	 * Expected number of files in classifyHocrImage
	 */
	public static final int EXPECTED_NUMBER_OF_FILES_CLASSIFY_HOCR_IMAGE = 1;

	/**
	 * Expected number of files in classifyMultipageHocrImage
	 */
	public static final int EXPECTED_NUMBER_OF_FILES_CLASSIFY_MULTIPAGE_HOCR_IMAGE = 1;

	/**
	 * Expected number of files for create searchable pdf.
	 */
	public static final int EXPECTED_NUMBER_OF_FILES_CREATE_SEARCHABLE_PDF = 2;

	/**
	 * Expected number of file expected for extract image.
	 */
	public static final int EXPECTED_NUMBER_OF_FILES_EXTRACT_IMAGE = 1;

	/**
	 * Maximum number of files in classifyImage
	 */
	public static final int MAX_NUMBER_OF_FILES_CLASSIFY_IMAGE = 3;

	/**
	 * maximum number of page preocess files
	 */
	public static final int EXPECTED_NUMBER_OF_PAGE_PROCESS_FILES = 4;

	/**
	 * Code for Create Thumbnails Height or width or output image parameters does not exist for the specified batch class id
	 */
	public static int INVALID_PARAM_CREATE_THUMBNAIL_PLUGIN_CODE = 422717;

	/**
	 * Code for Classify Images comp metric or fuzz percent or max results does not exist for the specified batch class id
	 */
	public static int INVALID_PARAM_CLASSIFY_IMAGE_PLUGIN_CODE = 422718;

	/**
	 * Code for incomplete properties of the Document assembler plugin for the specified batch class id
	 */
	public static int INVALID_PARAM_DOCUMENT_ASSEMBLER_PLUGIN_CODE = 422719;

	/**
	 * Code for incomplete properties of the Search Classification plugin for the specified batch class id
	 */
	public static int INVALID_PARAM_SEARCH_CLASSIFICATION_PLUGIN_CODE = 422724;

	/**
	 * Code for error in splitting multipage file
	 */
	public static final int CANNOT_CLASSIFY_IMAGE_CODE = 500720;

	/**
	 * Code for oncomplete properties of the Barcode Reader plugin for the specified batch class id
	 */
	public static int INVALID_PARAM_BARCODE_READER_PLUGIN_CODE = 500721;

	/**
	 * Code for error in classify barcode image
	 */
	public static final int CANNOT_CLASSIFY_BARCODE_IMAGE_CODE = 500722;

	/**
	 * Code for error in classify hocr image
	 */
	public static final int CANNOT_CLASSIFY_HOCR_CODE = 500723;

	/**
	 * Code for error in classify multipage hocr image
	 */
	public static final int CANNOT_CLASSIFY_MULTIPAGE_HOCR_CODE = 500727;

	/** The input zip not found code. */
	public static final int INPUT_ZIP_NOT_FOUND_CODE = 422700;

	/** Code for location type param not found in extractKv webservice */
	public static final int LOCATION_TYPE_NOT_FOUND_EXTRACT_KV_CODE = 422701;

	/** Code for key and value pattern param not found in extractKv webservice */
	public static final int KEY_VALUE_PATTERN_NOT_FOUND_EXTRACT_KV_CODE = 422702;

	/**
	 * Code for multiplier not valid for extractKv webservice
	 */
	public static final int MULTIPLIER_NOT_VALID_EXTRACT_KV_CODE = 422703;

	/**
	 * Code for KVFetchValue not valid for extractKv webservice
	 */
	public static final int KV_FETCH_VALUE_NOT_VALID_EXTRACT_KV_CODE = 422704;

	/**
	 * Code for length not valid for extractKv webservice
	 */
	public static final int LENGTH_NOT_VALID_EXTRACT_KV_CODE = 422705;

	/**
	 * Code for width not valid for extractKv webservice
	 */
	public static final int WIDTH_NOT_VALID_EXTRACT_KV_CODE = 422706;

	/**
	 * Code no of words not valid for extractKv webservice
	 */
	public static final int NUMBER_OF_WORDS_NOT_VALID_EXTRACT_KV_CODE = 422707;

	/**
	 * Code for not able to extractKv document field from HOCR
	 */
	public static final int CANNOT_EXTRACT_KV_DOCUMENT_FIELD_FROM_HOCR_CODE = 500725;

	/**
	 * Code for error in creating extractKv
	 */
	public static final int CANNOT_EXTRACT_KV_CODE = 500726;

	/**
	 * Code for incomplete properties of the FuzzyDB plugin for the specified batch class id
	 */
	public static int INVALID_PARAM_FUZZY_DB_PLUGIN_CODE = 500728;

	/**
	 * Code for invalid document type
	 */
	public static int DOCUMENT_TYPE_INVALID_CODE = 500729;

	/**
	 * Code for error in creating searchable PDF
	 */
	public static final int CANNOT_EXTRACT_FUZZY_DB_CODE = 500730;

	/**
	 * Code for error in creating searchable PDF
	 */
	public static final int CANNOT_EXTRACT_FIELD_VALUE_CODE = 500731;

	/**
	 * Code for fieldValue is null or empty
	 */
	public static final int FIELD_VALUE_NULL_OR_EMPTY_CODE = 500732;

	/**
	 * Code for input file should only be pdf with GhostScript(GS) tool
	 */
	public static final int ONLY_PDF_WITH_GS_TOOL_CODE = 422708;

	/**
	 * Empty error code
	 */
	public static final int EMPTY_ERROR_CODE = 0;

	/**
	 * Constant for web service parameter isColorImage. Used in createSearchablePDF
	 */
	public static final String IS_COLOR_IMAGE = "isColorImage";

	/**
	 * Constant for web service parameter outputPDFFileName. Used in createSearchablePDF
	 */
	public static final String OUTPUT_PDF_FILE_NAME = "outputPDFFileName";

	/**
	 * Constant for web service parameter isSearchableImage. Used in createSearchablePDF
	 */
	public static final String IS_SEARCHABLE_IMAGE = "isSearchableImage";

	/**
	 * Constant for web service parameter document type name.
	 */
	public static final String DOCUMENT_TYPE = "documentType";

	/**
	 * Constant for web service parameter document type name.
	 */
	public static final String HOCR_FILE_NAME = "hocrFile";

	/**
	 * Divisor to convert custom code to status code.
	 * <p>
	 * For eg. if custom code is 422103 <br>
	 * than 422013/100 will become 422 <br>
	 * which is status code for UNPROCESSABLE_ENTITY
	 * </p>
	 */
	public static final int DIVISOR_ERROR_CODE = 1000;

	/**
	 * <code>BOOLEAN_TRUE</code> is string representation of true
	 */
	public static final String BOOLEAN_TRUE = "True";

	/**
	 * Constant for web service parameter batch class identifier. Input parameters for webservice
	 */
	public static final String BATCH_CLASS_ID = "batchClassId";

	/**
	 * Constant for web service parameter field value.
	 */
	public static final String FIELD_VALUE = "fieldValue";

	/**
	 * Error Code for fieldValue is null or empty
	 */
	public static final int BATCH_CLASS_IDENTIFIER_NULL_OR_EMPTY_CODE = 500733;

	/**
	 * Error Code for fieldValue is null or empty
	 */
	public static final int KV_EXTRACTION_LIST_NULL_CODE = 500734;

	/**
	 * Error Code for ADMIN roles not found for access
	 */
	public static final int ADMIN_ROLES_NOT_FOUND = 403002;

	/**
	 * Error Message when invalid input is provided to upload batch web service.
	 */
	public static final String INVALID_INPUT_FOR_UPLOAD_BATCH = "Invalid Input for Upload Batch. Please uplad a tiff/pdf file.";

	/**
	 * Error message when HOCR file cannot be created by Nuance Engine.
	 */
	public static final String NUANCE_HOCR_CREATION_ISSUE = "Nuance could not create HOCR file.";

	/**
	 * Error code when HOCR file cannot be created by Nuance Engine.
	 */
	public static final int NUANCE_HOCR_CREATION_ISSUE_CODE = 422734;

	/**
	 * Error message when HOCR file cannot be created by Recostar Engine.
	 */
	public static final String RECOSTAR_HOCR_CREATION_ISSUE = "Recostar could not create HOCR file. Make sure your input project file is valid";

	/**
	 * Error code when HOCR file cannot be created by Recostar Engine.
	 */
	public static final int RECOSTAR_HOCR_CREATION_ISSUE_CODE = 422734;

	/**
	 * Constant used to fetch Restart all batch instance property from the property file.
	 */
	public static final String RESTART_ALL_BATCH_INSTANCE_PROPERTY = "enable.restart_all_batch";

	/**
	 * Error message when restart all batch instance property not set in property file.
	 */
	public static final String RESTART_ALL_PROPERTY_NOT_SET = "Cannot restart the batch instances. Please try again later.";

	/**
	 * Error code when restart all batch instance property not set in property file.
	 */
	public static final int RESTART_ALL_PROPERTY_NOT_SET_CODE = 422736;

	/**
	 * Constant used to denote a opening brace to enclose a parameter
	 */
	public static final String OPENING_SQUARE_BRACES = "[";

	/**
	 * Constant used to denote a closing brace to enclose a parameter
	 */
	public static final String CLOSING_SQUARE_BRACES = "]";

	/**
	 * Error Message indicating the invalid number of files are sent for upload learning.
	 */
	public static final String INVALID_NUMBER_OF_FILES_FOR_UPLOAD_LEARNING = "There is a mismatch between the total number of files sent and input xml. Please verify your input.";

	/**
	 * Error Message indicating no module name is provided as input..
	 */
	public static final String NO_MODULE_NAME = "No module name is provided to restart the Batch Instance. Please provide an appropriate module name.";

	/**
	 * Error Message indicating no batch instance identifier is provided as inpit
	 */
	public static final String NO_INSTANCE_NAME = "No module name is provided to restart the Batch Instance. Please provide an appropriate module name.";

	/**
	 * Error Message indicating no batch instance identifier is provided as input
	 */
	public static final int NO_INSTANCE_NAME_CODE = 422737;

	/**
	 * Error Message indicating no module name is provided as input
	 */
	public static final int NO_MODULE_NAME_CODE = 422737;

	/**
	 * Code for error in creating multipage file
	 */
	public static final int CANNOT_CREATE_MULTIPAGE_FILE_CODE = 500735;

	/**
	 * Constant for web service parameter document type processing file.
	 */
	public static final String SECOND_PAGE_PROJECT_FILE = "secondPageProjectFile";

	/**
	 * Constant for web service parameter document type processing file.
	 */
	public static final String THIRD_PAGE_PROJECT_FILE = "thirdPageProjectFile";
	/**
	 * Constant for web service parameter document type processing file.
	 */
	public static final String LAST_PAGE_PROJECT_FILE = "lastPageProjectFile";
	/**
	 * Code for weight not valid for extractKv webservice
	 */
	public static final int WEIGHT_NOT_VALID_EXTRACT_KV_CODE = 422023;
	/**
	 * Parameter weight not valid for extractKv webservice
	 */
	public static final String WEIGHT_NOT_VALID_EXTRACT_KV_MESSAGE = "Please provide the appropriate weight for KV extraction. Range of values is between 0 to 1. ";
	/**
	 * Constant for representing 10% value of <code>keyFuzziness</code> in extractKV webservice
	 */
	public static final float KEY_FUZZINESS_FIRST_VALUE = .1f;
	/**
	 * Constant for representing 20% value of <code>keyFuzziness</code> in extractKV webservice
	 */
	public static final float KEY_FUZZINESS_SECOND_VALUE = .2f;
	/**
	 * Constant for representing 30% value of <code>keyFuzziness</code> in extractKV webservice
	 */
	public static final float KEY_FUZZINESS_THIRD_VALUE = .3f;
	/**
	 * Code for keyFuzziness not valid for extractKv webservice
	 */
	public static final int KEY_FUZZINESS_NOT_VALID_EXTRACT_KV_CODE = 422024;
	/**
	 * Parameter keyFuzziness not valid for extractKv webservice
	 */
	public static final String KEY_FUZZINESS_NOT_VALID_EXTRACT_KV_MESSAGE = "Please provide the appropriate keyFuzziness for KV extraction. Values should be either .1, .2 or .3.  ";
	/**
	 * Constant of maximum weight value
	 */
	public static final Float MAXIMUM_WEIGHT = 1f;
	/**
	 * Constant of minimum weight value
	 */
	public static final Float MINIMUM_WEIGHT = 0f;
	/**
	 * Constant for the batch xml tag which stores the signature key.
	 */
	public static final String SIGNATURE = "Signature";
	/**
	 * Constant for the wrong application key.
	 */
	public static final String WRONG_APP_KEY_EXCEPTION_MESSAGE = "Application key in the batch XML doesnt match to the aplication key of server running.";

	/** The parameter xml incorrect code. */
	public static final int PARAMETER_APPLICATION_KEY_INCORRECT_CODE = 422025;
	/**
	 * Folder name for test classification folder in batch class folder.
	 */
	public static final String TEST_CLASSIFICATION_FOLDER_NAME = "test-classification";

	/**
	 * Constant for showing the message for unavailability for Linux.
	 */
	public static final String WEB_SERVICE_NOT_AVAILABLE_UNIX = "This web service does not support Encryption feature for Unix.";

	/** Code represents the unavailability for Web Service for Linux */
	public static final int WEB_SERVICE_NOT_AVAILABLE_UNIX_CODE = 404001;

	/**
	 * Expected number of files for create searchable pdf.
	 */
	public static final int EXPECTED_NUMBER_OF_FILES_CREATE_SEARCHABLE_PDF_LINUX = 1;

	/**
	 * Property constant for report base folder location.
	 */
	public static final String REPORT_BASE_FOL_LOC = "backup.report_folder";

	/**
	 * Code for invalid plugin name parameter in web service url.
	 */
	public static int INVALID_PLUGIN_NAME_PARAM_FOR_DECRYPTION_CODE = 422738;

	/** The invalid plugin name parameter message. */
	public static final String INVALID_PLUGIN_NAME_PARAM_FOR_DECRYPTION_MESSAGE = "Specified plugin name is not valid. Please specify valid plugin name in web service parameters. ";

	/**
	 * Code for invalid report data folder path.
	 */
	public static int INVALID_REPORT_DATA_PATH_CODE = 422739;

	/**
	 * 
	 */
	public static int INCOMPATIBLE_COLOR_SWITCH_IMAGE_FORMAT = 422743;
	/**
	 * color switch value not found
	 */
	public static int UNKNOWN_COLOR_SWITCH = 422745;

	/** The invalid report data folder path message. */
	public static final String INVALID_REPORT_DATA_PATH_MESSAGE = "Either report-data folder path is not specified in dcma-backup-service.properties or specified path is empty. ";

	/**
	 * Code for invlid sinature in XML file.
	 */
	public static int SIGNATURE_NOT_FOUND_ERROR_CODE = 422740;

	/** The invalid signature message. */
	public static final String SIGNATURE_NOT_FOUND_ERROR_MESSAGE = "Signature information is not present in XML file. Please provide a valid input XML with Signature information. ";

	/** String constant for underscore. */
	public static final String UNDERSCORE_SEPARATOR = "_";

	/** String constant for hocr. */
	public static final String HOCR = "HOCR";

	/**
	 * Constant for page.
	 */
	public static final String PAGE = "Page";

	/**
	 * Constant {@link String} that contains the folder name of Test content Classification.
	 */
	public static final String TEST_CONTENT_CLASSIFICATION_FOLDER_NAME = "test-classification";

	/** Message when there are invalid arguments for Create OCR */
	public static final String INVALID_ARGUMENTS_FOR_CREATE_HOCR = "Invalid number of files. We are supposed only 2 files each of type: XML and zip file.";

	/** No files found in the zip directory. */
	public static final String NO_FILES_IN_ZIP_DIR = "No files found in the zip directory.";

	/**
	 * Code for No files found in the zip directory.
	 */
	public static int NO_FILES_IN_ZIP_DIR_CODE = 422741;

	/** Zip Output location invalid. */
	public static final String ZIP_OUTPUT_LOCATION_INVALID_MESSAGE = "Zip output location is invalid in xml file.";

	/**
	 * Code for Zip Output location invalid.
	 */
	public static int ZIP_OUTPUT_LOCATION_INVALID_CODE = 422742;

	/** File name null or empty. */
	public static final String INVALID_FILE_NAME = "Input File name is null or empty.";

	/**
	 * Constant for extraction folder name
	 */
	public static final String FIXEDFORM_FOLDER_NAME = "fixed-form-extraction";

	/**
	 * windows project processing file name
	 */
	public static final String WINDOWS_PROJECT_FILE = "FPR.rsp";

	/**
	 * Linux project processing file
	 */
	public static final String LINUX_PROJECT_FILE = "";
	/**
	 * Color switch on incompatible image file
	 */
	public static final String COLOR_SWITCH_ON_IMAGEFORMAT_MESSAGE = "With color switch 'ON', uploaded image must be png.";

	/**
	 * Color switch off incompatible image file
	 */
	public static final String COLOR_SWITCH_OFF_IMAGEFORMAT_MESSAGE = "With color switch 'OFF', uploaded image must be tif/tiff.";

	/**
	 * constant for project process file first page
	 */
	public static final String PROJECT_PROCCESS_FILE_FIRST_PAGE = "PG1";

	/**
	 * constant for project process file second page
	 */
	public static final String PROJECT_PROCCESS_FILE_SECOND_PAGE = "PG2";

	/**
	 * constant for project process file third page
	 */
	public static final String PROJECT_PROCCESS_FILE_THIRD_PAGE = "PG3";

	/**
	 * constant for project process file last page
	 */
	public static final String PROJECT_PROCCESS_FILE_FOURTH_PAGE = "PG4";

	/**
	 * message displayed when page processing file is not presnet for linux system
	 */
	public static final String PAGE_PROCESSING_FILE_NOT_PRESENT_FOR_LINUX = "Page processing file for the document is not present";

	public static final String PAGE_PROCESSING_FILE_NOT_PRESENT = "Page processing file for the document is not present";

	/**
	 * path of recostar
	 */
	public static final String WINDOWS_ENVIRONMENT_VARIABLE_PAGE_PROCESS_FILE_LOCATION = "RECOSTAR_PATH";

	public static final String COLOR_SWITCH_NOT_FOUND = "Color switch value not found ";
	/**
	 * constant for error message , if uploaded image is png for linux
	 */
	public static final String ERROR_INCOMPATIBLE_IMAGE_TYPE_LINUX_MESSAGE = "Invalid files. Expected 2 files : one XML, one tif/tiff.";

	/**
	 * Constant for web service parameter hocrFileName.
	 */
	public static final String HOCR_FILENAME = "hocrFileName";

	/**
	 * Constant for web service parameter batchInstanceIdentifier.
	 */
	public static final String BATCH_INSTANCE_IDENTIFIER = "batchInstanceIdentifier";

	/**
	 * Constant for web service parameter pageType.
	 */
	public static final String PAGE_TYPE = "pageType";

	/**
	 * Constant for web service parameter testType.
	 */
	public static final String TEST_TYPE = "testType";

	/**
	 * Constant for web service error message when the input xml is not correct
	 */
	public static final String INVALID_XML_CONTENT = "Invalid parameters in input XML file. Web service accepts input xml with color switch and project file as parameter when the uploads "
			+ "are tiff/png and page processing file. It can also accept input xml with batch class identifier and doc type as parameter when the upload is only tiff/png.";

	/**
	 * Constant for web service error message when only inputs allowed are single page tiff or png
	 */
	public static final String SINGLE_PAGE_TIFF_PNG_REQUIRED_MESSAGE = "Invalid input, single page tiff/tif or png required as an input";

	/**
	 * Constant for error message when page processing file not supplied as input
	 */
	public static final String EXPECTED_PAGE_PROCESSING_FILE_ERROR_MESSAGE = "Expected page processing file as input";

	/**
	 * error code for page processing file not supplied as input parameter
	 */
	public static int PAGE_PROCESSING_FILE_NOT_FOUND = 422746;

	/**
	 * Constant for error message when input page processing file name is dissimilar
	 */
	public static final String PAGE_PROCESS_FILE_MISMATCH_IN_XML = "Project processing file name mismatch in XML and uploaded file name";
	
	/**
	 * Constant for minimum number of parameters required for table extraction Web service 
	 */
	public static final int MINIMUM_FILE_COUNT_FOR_TABLE_EXTRACTION = 2 ; 
	
	/**
	 * Constant for error messafe to display if the number of parameters for tabble extraction is less than required
	 */
	public static final String TABLE_EXTRACTION_MINIMUM_PARAMETERS_REQUIRED_ERROR_MESSAGE = "Minimum number of files required for table extraction is "+MINIMUM_FILE_COUNT_FOR_TABLE_EXTRACTION+". Expected files :input xml, HOCR pages.";
	
	/**
	 * Constant for invalid number of files in table extraction 
	 */
	public static final String INVALID_FILES_TABLE_EXTRACTION = "Invalid files. Expected mimuminm "+MINIMUM_FILE_COUNT_FOR_TABLE_EXTRACTION+" files, one XML and HOCR page/pages.";	
	
	/**
	 * error message when input xml has hocr files that are not supplied as input
	 */
	public static final String INVALID_MAPPING_DOCUMENT_HOCR_PAGES = "Incorrect/Invalid mapping for document type and hocr pages in input xml.";
	/**
	 *message to be displayed if the extraction wswitch is OFF 
	 */
	public static String TABLE_EXTRACCTION_SWITCH_OFF_MESSAGE = "Table Extraction switch is OFF for the batch class ";
	/**
	 * Constant for batch class identifier
	 */
	public static final String BCX = "BCX";
	/**
	 * Constant for table extraction plugin
	 */
	public static final String TABLE_EXTRACTION_PLUGIN = "TABLE_EXTRACTION";
	/**
	 * switch value for table extraction 
	 */
	public static final String TABLE_EXTRACTION_SWITCH = "tableextarction.switch";
	/**
	 * Constant code when table extraction switch is OFF
	 */
	public static int TABLE_EXTRACTION_SWITCH_OFF_CODE =  42243;
	/**
	 * message to be displayed when the uploaded HOCR is invalid
	 */
	public static final String INVALID_HOCR_FILE_UPLOAD_MESSAGE = "Invalid HOCR File is uploaded.";
	/**
	 * constant for error code when invalid HOCR 
	 */
	public static int INVALID_HOCR_FILE_UPLOADED_CODE = 42244;
	/**
	 * HOCR file extension
	 */
	public static final String HOCR_EXTENSION = "HOCR.xml";
	/**
	 * constant for undefined batch class 
	 */
	public static final String UNDEFINED_BATCH_IDENTIFIER = "Batch class identifier not defined in the input xml.";
	/**
	 * Constant for undefined table extraction switch
	 */
	public static final String UNDEFINED_TABLE_EXTRACTION_SWITCH = "Undefined table extraction switch for Batch class ";
	
	/**
	 * Constant for error message
	 */
	public static final String ERROR_CODE_MESSAGE = "Error code :";
	
	/** Message when there are invalid arguments for Create OCR */
	public static final String INVALID_ARGUMENTS_FOR_CREATE_OCR_BATCH_CLASS = "Invalid number of files. We are supposed only 2 files of type:XML and ZIP.";
	
	/** Message for invalid batch identifier */
	public static final String INVALID_BATCH_IDENTIFIER = "Batch class does not exist";
	
	/**
	 * Proerpties folder constant
	 */
	public static final String PROPERTIES_FOLDER = "properties";
	
	/**
	 * Dot extension constant
	 */
	public static final String DOT_EXTENSION=".";
		/**
	 * .Ser file extension constant
	 */
	public static final String SER_FILE =  EphesoftStringUtil.concatenate(DOT_EXTENSION + FileType.SER.getExtension());
	

}
