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

package com.ephesoft.dcma.imagemagick.constant;

/**
 * Interface for constants used in Image Magick.
 * 
 * @author Ephesoft
 * @version 3.0
 * 
 */
public interface ImageMagicKConstants {

	/**
	 * Image magic plugin name
	 */
	String CLASSIFY_IMAGES_PLUGIN = "CLASSIFY_IMAGES";

	String CREATE_THUMBNAILS_PLUGIN = "CREATE_THUMBNAILS";

	String CREATE_OCR_INPUT_PLUGIN = "CREATE_OCR_INPUT";

	String CREATE_DISPLAY_IMAGE_PLUGIN = "CREATE_DISPLAY_IMAGE";

	String CREATEMULTIPAGE_FILES_PLUGIN = "CREATEMULTIPAGE_FILES";

	String IMPORT_MULTIPAGE_FILES_PLUGIN = "IMPORT_MULTIPAGE_FILES";

	String IMAGE_MAGICK = "IMAGE_MAGICK";

	String HOCR_TO_PDF = "HOCRtoPDF";

	String ITEXT_SEARCHABLE = "ITEXT-SEARCHABLE";

	String GHOST_SCRIPT_COMMAND_PARAMETERS = "-dQUIET -dNOPAUSE -r300 -sDEVICE=pdfwrite";

	String GHOST_SCRIPT_COMMAND_OUTPUT_PARAMETERS = "-dBATCH";

	String GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM = "-sOutputFile=";

	String GHOST_SCRIPT = "GHOSTSCRIPT";

	String DEFAULT_TIF_DEVICE = "tiffg4";

	String PDF_DEVICE = "pdfwrite";

	String ON_SWITCH = "ON";

	String TEMP_FILE_NAME = "tempfile";

	String DOUBLE_QUOTES = "\"";

	String SPACE = " ";

	String GHOSTSCRIPT_HOME = "GHOSTSCRIPT_HOME";

	String GHOSTSCRIPT_COMMAND = "gswin32c.exe";

	String FALSE = "false";

	int MAX_FILES_PER_GS_COMMAND = 75;

	String ITEXT = "ITEXT";

	int DEFAULT_PDF_PAGE_HEIGHT = 792;

	int DEFAULT_PDF_PAGE_WIDTH = 612;

	char UNDERSCORE = '_';

	/**
	 * The SPAN_ATTRIBUTE {@link String} is a constant for span tag inside html file.
	 */
	String SPAN_TAG = "span";

	/**
	 * The CLASS_ATTRIBUTE {@link String} is a constant for class attribute inside html file.
	 */
	String CLASS_ATTRIBUTE = "class";

	/**
	 * The TITLE_ATTRIBUTE {@link String} is a constant title span attribute uninsideder html file.
	 */
	String TITLE_ATTRIBUTE = "title";

	/**
	 * The CLASS_ATTRIBUTE_VALUE {@link String} is a constant for class attribute value inside html file.
	 */
	String CLASS_ATTRIBUTE_VALUE = "ocr_line";

	/**
	 * The TITLE_ATTRIBUTE_VALUE_PATTERN {@link String} is a constant for title attribute value regex pattern inside html file.
	 */
	String TITLE_ATTRIBUTE_VALUE_PATTERN = "bbox(\\s+\\d+){4}";

	/**
	 * The TITLE_ATTRIBUTE_COORDINATES_PATTERN {@link String} is a constant for title coordinate attribute value regex pattern inside
	 * html file.
	 */
	String TITLE_ATTRIBUTE_COORDINATES_PATTERN = "(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)";

	/**
	 * The PDF_RESOLUTION is a constant for pdf resolution set for searchable pdf.
	 */
	float PDF_RESOLUTION = 72.0f;

	/**
	 * The IMAGE_QUALITY is a constant for image quality parameter required for image compression using image magic.
	 */
	double IMAGE_QUALITY = 100.0;

	/**
	 * The OPERATION_TYPE {@link String} is a constant for operation type parameter used while converting image using image magic.
	 */
	String OPERATION_TYPE = "optimize";

	/**
	 * The COMPRESSION_TYPE {@link String} is a constant for compression type parameter used while converting image using image magic.
	 */
	String COMPRESSION_TYPE = "LZW";

	/**
	 * The DOTS {@link String} is a constant for character '.'.
	 */
	String DOTS = ".";

	/**
	 * The NEW {@link String} is a constant for new.
	 */
	String NEW = "_new";

	/**
	 * The TIF_EXTENSION {@link String} is a constant for tif file extension.
	 */
	String TIF_EXTENSION = ".tif";

	/**
	 * Default width of line.
	 */
	int DEFAULT_WIDTH_OF_LINE = 15;

	/**
	 * String constant for xpath expression for a TextLine in an hocr xml file.
	 */
	String TEXTLINE_XPATH_EXPRESSION = "Documents//Document//ReadAreas//ReadArea//Blocks//Block//TextLines//TextLine";

	/**
	 * FONT_FOLDER_NAME {@link String} specifies the name of the font folder.
	 */
	String FONT_FOLDER_NAME = "font";

	/**
	 * FONT_FILE_NAME {@link String} specifies the name of the font file.
	 */
	String FONT_FILE_NAME = "font.ttf";

	/**
	 * String constant for TifToPdf.
	 */
	String TIFF_TO_PDF = "TifToPdf";

	/**
	 * String constant for file separator.
	 */
	String FILE_SEPARATOR = "\\";

	/**
	 * PDF_TYPE_ADVANCED {@link String} specifies the type of pdf advanced.
	 */
	String PDF_TYPE_ADVANCED = "PDF Advanced";
	
	/**
	 * FONT_SIZE specifies the size of font.
	 */
	int FONT_SIZE = 8;
	
	/**
	 * Empty {@link String} is a constant for empty string.
	 */
	String EMPTY = "";
}
