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

package com.ephesoft.dcma.batch.constant;

import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This is a common constants file for BatchConstant.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public final class BatchConstants {

	/**
	 * Private constructor to avoid instantiation of constants class.
	 */
	public BatchConstants() {
		// Do Nothing.
	}

	/**
	 * String constant for space.
	 */
	public static final String SPACE = " ";

	/**
	 * String constant for empty string.
	 */
	public static final String EMPTY = "";

	/**
	 * String constant for under score string.
	 */
	public static final String UNDER_SCORE = "_";

	/**
	 * String constant for semi colon string.
	 */
	public static final String SEMI_COLON = ";";

	/**
	 * String constant for properties directory.
	 */
	public static final String PROPERTIES_DIRECTORY = "properties";

	/**
	 * String constant for string "Unable to create FileNameFormatter.".
	 */
	public static final String FILE_NAME_FORMATTER_NOT_CREATED = "Unable to create FileNameFormatter.";

	/**
	 * String constant for string "Unable to get unique page id for duplicate page.".
	 */
	public static final String UNABLE_TO_GET_UNIQUE_PAGE_ID = "Unable to get unique page id for duplicate page.";

	/**
	 * String constant for document type list null statement.
	 */
	public static final String DOC_TYPE_LIST_NULL = "docTypesList is null.";

	/**
	 * String constant for png extension.
	 */
	public static final String PNG_EXTENSION = ".png";

	/**
	 * String constant for html extension.
	 */
	public static final String HTML_EXTENSION = ".html";

	/**
	 * String constant for tif extension.
	 */
	public static final String TIF_EXTENSION = ".tif";

	/**
	 * String constant for Page id prefix.
	 */
	public static final String PAGE_ID_PREFIX = "PG";

	/**
	 * String constant for dot.
	 */
	public static final String DOT = ".";

	/**
	 * {@link String} constant to represent the name for the BC1 batch class.
	 */
	public static final String BC1_BATCH_CLASS_NAME = "BC1";

	/**
	 * Constant that decides initial index - <code>OrderNo.</code> of KV's.
	 */
	public static final int IMPORT_BATCH_KV_STARTING_ORDER = 1;

	/**
	 * {@link String} constant to represent the name property for lucene valid extensions.
	 */
	public static final String LUCENE_VALID_EXTENSIONS = "lucene.valid_extensions";
	/**
	 * {@link String} constant to represent the default value to be set in lucene valid extensions.
	 */
	public static final String DEFAULT_VALUE_SETTING = "xml;html";
	/**
	 * {@link String} constant to represent the default value to be checked in lucene valid extensions.
	 */
	public static final String HTML_SETTING = "html";

	/**
	 * Constant that decides initial index - <code>OrderNo.</code>
	 */
	public static final int IMPORT_BATCH_STARTING_ORDER = 1;

	/**
	 * Constant for new table rule name - <code>Table extraction rule name</code>
	 */
	public static final String IMPORT_BATCH_TABLE_EXTRACTION_RULE_NAME = "Rule";

	/**
	 * Constant {@link Float} value which is the default initialization to the float variables in Batch.
	 */
	public static final Float DEFAULT_FLOAT_INITIALIZIATION_VALUE = 0.0f;

	/**
	 * Constant {@link Integer} value which is the default initialization to the float variables in Batch.
	 */
	public static final Integer DEFAULT_INTEGER_INITIALIZIATION_VALUE = 0;

	/**
	 * Constant {@link Double} value which is the default initialization to the float variables in Batch.
	 */
	public static final Double DEFAULT_DOUBLE_INITIALIZIATION_VALUE = 0.0;

	/**
	 * Constant {@link String} that contains the folder name of Test content Classification.
	 */
	public static final String TEST_CONTENT_CLASSIFICATION_FOLDER_NAME = "test-classification";

	/**
	 * Constant {@link String} that indicates the password for the Key Store files.
	 */
	public static final String KEYSTORE_FILE_PASSWORD = "123456";

	/**
	 * Constant {@link String} that indicates the Batch XML Signature's Groups Separator.
	 */
	public static final String BATCH_XML_GROUPS_SEPARATOR = ",";

	/**
	 * Constant {@link String} that is used a separator between the different Batch XML signature elements.
	 */
	public static final String BATCH_XML_SIGNATURE_ELEMENTS_SEPARATOR = "#";

	/**
	 * Constant {@link String} that indicates the Charset name for the UTF-8 Encoding.
	 */
	public static final String UTF_8_ENCODER = "UTF-8";

	/**
	 * Constant {@link String} that dencotes the algorithm to be used for HOCR Files signature.
	 */
	public static final String HOCR_SIGNATURE_DIGEST_ALGORITHM = "SHA-1";
	/**
	 * Constant {@link String} for encryption algorithm value NONE.
	 */
	public static final String ENCRYPTION_ALGO_NONE = "None";
	/**
	 * Constant {@link String} for encryption algorithm value AES-128.
	 */
	public static final String ENCRYPTION_ALGO_AES_128 = "AES_128";
	/**
	 * Constant {@link String} for encryption algorithm value AES-256.
	 */
	public static final String ENCRYPTION_ALGO_AES_256 = "AES_256";

	/**
	 * Constant {@link String} that indicates the element name of the HOCR Signature
	 */
	public static final String HOCR_SIGNATURE_ELEMENT = "Signature";

	/**
	 * CMIS_EXPORT, a {@link String} constant to represent the name for the Cmis Export plugin.
	 */
	public static final String CMIS_EXPORT = "CMIS_EXPORT";

	/**
	 * EXPORT_MODULE, a {@link String} constant to represent the name for the Export Module.
	 */
	public static final String EXPORT_MODULE = "Export";

	/**
	 * CREATEMULTIPAGE_FILES, a {@link String} constant to represent the name for the CREATEMULTIPAGE_FILES plugin.
	 */
	public static final String CREATEMULTIPAGE_FILES = "CREATEMULTIPAGE_FILES";

	/**
	 * Constant to hold file path of dcma-workflows.properties {@link String}
	 */
	public static final String DCMA_WORKFLOWS_PROPERTIES = "/META-INF/dcma-workflows/dcma-workflows.properties";

	/**
	 * {@link String} Constant to hold parameter name used to get Max Processing Capacity from dcma-workflows.properties.
	 */
	public static final String SERVER_INSTANCE_MAX_PROCESS_CAPACITY = "server.instance.max.process.capacity";

	/**
	 * {@link String} Constant for workflows serialized file suffix.
	 */
	public static final String WORKFLOWS_CONSTANT = "workflows";

	/** Constant to hold old name of document assembler classification type for importing batches from previous versions. */
	public static final String DA_SEARCHABLE_PDF_CLASSIFICATION = "SearchablePdfClassification";

	/** Constant to hold new name of document assembler classification type for importing batches from previous versions. */
	public static final String DA_ONE_DOCUMENT_CLASSIFICATION = "OneDocumentClassification";
	
	/**
	 * {@link String} Constant to hold parameter name used to get Batch instance priority range from dcma-workflows.properties.
	 */
	public static final String SERVER_INSTANCE_BATCH_PRIORITY_RANGE = "server.instance.batch.priority.range";

	/**
	 * {@link String} Constant for default value for Batch instance priority range.
	 */
	private static final String MINIUMUM_BATCH_PRIORITY_RANGE = "1";

	/**
	 * {@link String} Constant for default value for Batch instance priority range.
	 */
	private static final String MAXIMUM_BATCH_PRIORITY_RANGE = "100";

	/**
	 * {@link String} Constant for default value for Batch instance priority range.
	 */
	public static final String DEFAULT_SERVER_INSTANCE_BATCH_PRIORITY_RANGE = EphesoftStringUtil.concatenate(
			MINIUMUM_BATCH_PRIORITY_RANGE, ICommonConstants.PRIORITY_RANGE_SEPERATOR, MAXIMUM_BATCH_PRIORITY_RANGE);

	/**
	 * Constant for batch folder name
	 */
	public static final String BATCH_FOLDER_NAME = "batch.folder_name";
	
	/**
	 * Constant for batch file name
	 */
	public static final String BATCH_FILE_NAME= "batch.file_name";
	
	/**
	 * Constant for batch export to folder
	 */
	public static final String BATCH_EXPORT_FOLDER = "batch.export_to_folder";
	
	/**
	 * Constant for export folder path
	 */
	public static final String BATCH_EXPORT_FOLDER_PATH = "batch.multipage_export_folder_path";
	
	/**
	 * Constant for export file name
	 */
	public static final String BATCH_EXPORT_FILE_NAME = "batch.multipage_export_file_name";
	
	
	/**
	 * Constant for copy batch XML 
	 */
	public static final String COPY_BATCH_XML = "COPY_BATCH_XML";
	
	/**
	 * Constant for old batch identifier 
	 */
	public static final String EPHESOFT_BATCH_ID = "EphesoftBatchID";
	
	/**
	 * Constant for batch identifier
	 */
	public static final String BATCH_IDENTIFIER = "BATCH_IDENTIFIER";
	
	/**
	 * Constant for old document id
	 */
	public static final String EPHESOFT_DOC_ID = "EphesoftDOCID";
	
	/**
	 * Constant for updated document id
	 */
	public static final String DOCUMENT_IDENTIFIER ="DOCUMENT_ID";
	
	/**
	 * Constant for old file name delimiter
	 */
	public static final String OLD_FILENAME_DELIMETER= "&&";
	
	/**
	 * Constant for new file name delimiter
	 */
	public static final String NEW_FILENAME_DELIMETER = "&";	

/**
	 * Constant for cmis export folder path
	 */
	public static final String CMIS_ROOT_FOLDER = "cmis.root_folder";
	
	/**
	 * Constant for cmis export file name
	 */
	public static final String CMIS_FILE_NAME = "cmis.file_name";
	
	/**
	 * Constant for suffix in cmis export file name.
	 */
	public static final String CMIS_EXPORT_FILE_NAME_SUFFIX="$";

		
}
