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

package com.ephesoft.dcma.util;

import java.util.Locale;

public interface IUtilCommonConstants {

	String BATCH_XML = "batch.xml";
	String FILENAME_FORMATTER_PROPERTIES = "FileNameFormatter.properties";
	String DOCUMENT = "document";
	String DOCUMENT_STRING = "document";
	String MULIPAGE_FILENAME_FORMAT = "multiPageFileNameFormat";
	String NEW_FILE_FILENAME_FORMAT = "newFileFileNameFormat";
	String NEW_FILE_FILENAME_SEPARATOR = "newFileFileNameSeparator";
	String MULIPAGE_FILENAME_SEPARATOR = "multiPageFileNameSeparator";
	String BATCH_ID = "batchId";
	String DOCUMENT_ID = "documentId";
	String PAGE_ID = "pageId";
	String OLD_FILE_NAME_WITH_EXT = "oldFileNameWithExt";
	String OLD_FILENAME_WO_EXT = "oldFileNameWOExt";
	String NEW_FILE_NAME_WITH_EXT = "newFileNameWithExt";
	String NEW_FILE_NAME_WO_EXT = "newFileNameWOExt";
	String EXTRACTION = "extraction";
	String EXTENSION = "extension";
	String SEPARATOR = "separator";
	String FILE_NAME_FORMAT_DELIMITOR = ";";
	String OCR_INPUT_FILE_NAME_WITH_EXT = "ocrInputFileNameWithExt";
	String OCR_INPUT_FILE_NAME_WO_EXT = "ocrInputFileNameWOExt";
	String HOCR_FILENAME_FORMAT = "hocrFileNameFormat";
	String HOCR_FILENAME_SEPARATOR = "hocrFileNameSeparator";
	String OCR_INPUT_FILENAME_FORMAT = "ocrInputFileNameFormat";
	String OCR_INPUT_FILENAME_SEPARATOR = "ocrInputFileNameSeparator";
	String THUMB_STRING = "thumb";
	String DISP_THUMB_STRING = "displayThumb";
	String COMPARE_THUMB_STRING = "compareThumb";
	String DISPLAY_THUMBNAIL_FILENAME_FORMAT = "displayThumbNailFileNameFormat";
	String COMPARE_THUMBNAIL_FILENAME_FORMAT = "compareThumbNailFileNameFormat";
	String THUMBNAIL_FILENAME_SEPARATOR = "thumbNailFileNameSeparator";
	String OVERLAY_FILENAME_SEPARATOR = "overlayFileNameSeparator";
	String OVERLAY_FILENAME_FORMAT = "overlayFileNameFormat";
	String ALTERNATE_VALUE_INDEX = "alternateValueIndex";
	String FIELD_NAME = "fieldname";
	String TESSERACT_MAIL_ROOM_NAME = "TesseractMailRoom";
	String EXTENSION_PNG = ".png";
	String UNDER_SCORE = "_";
	char DOT = '.';
	String EXTENSION_HTML = ".html";
	String EXTENSION_XML = ".xml";
	String EMPTY_STRING = "";
	String TESSERACT_HOCR_PLUGIN = "TESSERACT_HOCR";
	String RECOSTAR_HOCR_PLUGIN = "RECOSTAR_HOCR";
	/**
	 * Constant for Nuance HOCR plugin.
	 */
	String NUANCE_HOCR_PLUGIN = "NUANCE_HOCR";
	String PAGE_PROCESS_MODULE_NAME = "Page Process";
	String EXTENSION_TIFF = ".tiff";
	String EXTENSION_TIF = ".tif";

	/**
	 * The EXTENSION_PDF {@link String} is a constant for pdf file extension.
	 */
	String EXTENSION_PDF = ".pdf";

	/**
	 * Invalid characters for a file name.
	 */
	String INVALID_FILE_EXTENSIONS = "/;\\;:;*;<;>;?;\"";

	/**
	 * Invalid characters for a file name.
	 */
	String INVALID_CHAR_SEPARATOR = ";";

	/**
	 * Backward slash constant.
	 */
	String BACKWARD_SLASH = "\\";

	/**
	 * Forward slash constant.
	 */
	char FORWARD_SLASH = '/';

	/**
	 * Zip file extension.
	 */
	String ZIP_FILE_EXT = ".zip";

	/**
	 * Replace character to be used while renaming file names.
	 */
	String DEFAULT_REPLACE_CHAR = "-";

	/**
	 * String constant to represent a comment line in properties file.
	 */
	String HASH_STRING = "#";

	/**
	 * Character constant to separate key and value in property file.
	 */
	char EQUAL_TO = '=';

	/* List of mail specific constants */

	/**
	 * param value False.
	 */
	String FALSE_PARAM_VALUE = "false";

	/**
	 * IMAP Partial fetch param name.
	 */
	String IMAP_PARTIAL_FETCH_PARAM = "mail.imap.partialfetch";

	/**
	 * IMAP SOcket factory fallback param name.
	 */
	String IMAP_SOCKET_FACTORY_FALLBACK_PARAM = "mail.imap.socketFactory.fallback";

	/**
	 * IMAP Socket factory class param name.
	 */
	String IMAP_SOCKET_FACTORY_CLASS_PARAM = "mail.imap.socketFactory.class";

	/**
	 * POP3 Socket factory fallback param name.
	 */
	String POP3_SOCKET_FACTORY_FALLBACK_PARAM = "mail.pop3.socketFactory.fallback";

	/**
	 * POP3 Socket factory class param name.
	 */
	String POP3_SOCKET_FACTORY_CLASS_PARAM = "mail.pop3.socketFactory.class";

	/**
	 * Default port value for pop3 server type using SSL connection.
	 */
	Integer POP3_SSL_DEFAULT_PORT_NUMBER = 995;

	/**
	 * Default port value for Imap server type using SSL connection.
	 */
	Integer IMAP_SSL_DEFAULT_PORT_NUMBER = 993;

	/**
	 * Default port value for Imap server type using non SSL connection.
	 */
	int IMAP_NON_SSL_DEFAULT_PORT = 143;

	/**
	 * Default port value for pop3 server type using non SSL connection.
	 */
	int POP3_NON_SSL_DEFAULT_PORT = 110;

	/**
	 * SSL factory implementation class name.
	 */
	String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	/**
	 * Constant for server type imap value.
	 */
	String SERVER_TYPE_IMAP = "imap";

	/**
	 * Constant for server type pop3 value.
	 */
	String SERVER_TYPE_POP3 = "pop3";

	/**
	 * specifies the initial size of byte array.
	 */
	int INITIAL_SIZE = 4096;

	/**
	 * content type for html.
	 */
	String CONTENT_TYPE_HTML = "text/html";

	/**
	 * content type for zip.
	 */
	String CONTENT_TYPE_ZIP = "application/x-zip\r\n";

	/**
	 * Default locale for the Java Virtual Machine installed on system Ephesoft is running.
	 */
	Locale DEAFULT_LOCALE = Locale.getDefault();

	/**
	 * <code>MIME_TYPE_MULTIPART</code> {@link String} constant used for representing the <code>MULTIPART</code> Message part type.
	 */
	String MIME_TYPE_MULTIPART = "multipart/*";

	/**
	 * <code>MIME_TYPE_TEXT_HTML</code> {@link String} constant used for representing the <code>TEXT/HTML</code> Message part type.
	 */
	String MIME_TYPE_TEXT_HTML = "text/html";

	/**
	 * <code>MIME_TYPE_TEXT_PLAIN</code> {@link String} constant used for representing the <code>TEXT/PLAIN</code> Message part type.
	 */
	String MIME_TYPE_TEXT_PLAIN = "text/plain";

	/**
	 * <code>MIME_TYPE_MULTIPART_ALTERNATIVE</code> {@link String} constant used for representing the
	 * <code>MULTIPART/ALTERNATIVE</code> Message part type.
	 */
	String MIME_TYPE_MULTIPART_ALTERNATIVE = "multipart/alternative";

	/**
	 * <code>MIME_TYPE_IMAGE</code> {@link String} constant used for representing the <code>IMAGE</code> Message part type.
	 */
	String MIME_TYPE_IMAGE = "image/*";

	/**
	 * <code>MIME_TYPE_TEXT</code> {@link String} constant used for representing the <code>TEXT</code> Message part type.
	 */
	String MIME_TYPE_TEXT = "text/*";

	/**
	 * <code>IMAGE_INLINE_KEYWORD</code> a {@link String} constant to represent the text to be added when displaying an inline image in
	 * the mail.html file.
	 */
	String IMAGE_INLINE_KEYWORD = "[Image][Inline]";

	/**
	 * <code>PLAIN_TEXT_BODY_HTML_TAG_END</code> a {@link String} constant to represent the HTML end tag for Plain text mails.
	 */
	String PLAIN_TEXT_BODY_HTML_TAG_END = "</pre>";

	/**
	 * <code>PLAIN_TEXT_BODY_HTML_TAG_START</code> a {@link String} constant to represent the HTML start tag for Plain text mails.
	 */
	String PLAIN_TEXT_BODY_HTML_TAG_START = "<pre>";

	/**
	 * <code>DELETE_RETRY_INDEX</code> is an integer constant which defines how many times a file deletion will be attempted.
	 */
	int DELETE_RETRY_MAX_COUNT = 3;

	/**
	 * String constant for OFF.
	 * 
	 */
	String OFF = "OFF";

	/**
	 * <code>SECONDS_PER_MINUTE</code> is an integer constant which defines sixty seconds.
	 */
	int SECONDS_PER_MINUTE = 60;

	/**
	 * <code>MILLISECOND_PER_SECOND</code> is an integer constant which defines thousand milliseconds.
	 */
	int MILLISECOND_PER_SECOND = 1000;

	/**
	 * <code>ZERO</code> is an integer constant for zero.
	 */
	int ZERO = 0;

	/**
	 * <code>SPACE</code> is a string constant for ' '.
	 */
	String SPACE = " ";

	/**
	 * <code>NUANCE_EXTRACTION_PLUGIN</code> is a string constant for NUANCE_EXTRACTION.
	 */
	String NUANCE_EXTRACTION_PLUGIN = "NUANCE_EXTRACTION_PLUGIN";

	/**
	 * <code>EXTRACTION_KEYWORD</code> is a string constant for EXTRACTION.
	 */
	String EXTRACTION_KEYWORD = "Extraction";

	/**
	 * <code>NON_ASCII_CHARACTER_REGEX</code> is a string constant for non ascii characters.
	 */
	String NON_ASCII_CHARACTER_REGEX = "[^\\x0A\\x0D\\x20-\\x7E]";
	
	/**
	 * Regex to replace charcters those are not valid in windows file name.i.e. \ / * < > : | " ?
	 */
	String INVALID_CHARACTER_REGEX= "[\\\\*<>?:/\"|]";
}
