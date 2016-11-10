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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is the utility class for web services.
 * 
 * @author Ephesoft
 * @version 3.0
 * 
 */
public class WebServiceUtil {

	public static final String serverInputFolderName = "in";
	public static final String serverOutputFolderName = "out";
	public static final String EMPTY_STRING = "";
	public static final String DOT = ".";
	public static final String RSP_EXTENSION = ".rsp";
	public static final String ZON_EXTENSION = ".zon";
	public static final String ON_STRING = "ON";
	public static final String OFF_STRING = "OFF";
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	public static int bufferSize = 1024;
	private static final String BACKUP_PROPERTY_FILE = "META-INF" + File.separator + "dcma-util" + File.separator
			+ "dcma-backup-service.properties";
	public static final String DOCUMENTID = "docWS";
	public static final String BATCH_NOT_EXISTS = "Batch Class does not exist"; 

	/**
	 * Constant for content disposition header value in the response
	 */
	public static final String ATTACHMENT_FILENAME = "attachment; filename=\"";

	/**
	 * Constant for content disposition header name in the response
	 */
	public static final String CONTENT_DISPOSITION = "Content-Disposition";

	/**
	 * Constant for content type header in the response
	 */
	public static final String APPLICATION_X_ZIP = "application/x-zip\r\n";
	public static final String TESSERACT = "tesseract";
	public static final String RECOSTAR = "recostar";
	public static final String CMD_LANGUAGE = "cmdLanguage";
	public static final String TESSERACT_VERSION = "tesseractVersion";
	public static final String PROJECT_FILE = "projectFile";
	public static final String COLOR_SWITCH = "colorSwitch";
	public static final String BATCH_CLASS_IDENTIFIER = "batchClassIdentifier";
	public static final String IMAGE_NAME = "imageName";
	public static final String DOC_TYPE = "docType";
	public static final String OCR_ENGINE = "ocrEngine";
	public static final String OUTPUT_PARAMS = "outputParams";
	public static final String INPUT_PARAMS = "inputParams";
	public static final String IS_GHOSTSCRIPT = "isGhostscript";
	public static final String LUCENE_SEARCH_CLASSIFICATION_SAMPLE = "lucene-search-classification-sample";
	public static final String IMAGE_CLASSIFICATION_SAMPLE = "image-classification-sample";
	public static final String IDENTIFIER2 = "identifier";
	public static final String DCMA_GXT_ADMIN_EXPORT_BATCH_CLASS_DOWNLOAD = "/gxt-admin/exportBatchClassDownload";
	public static final String PG0 = "PG0";
	public static final String BUILD_XML = "build.xml";
	public static final String PDF_GENERATOR_ENGINE = "pdfGeneratorEngine";
	public static final String GHOSTSCRIPT_PDF_PARAMETERS = "ghostscriptPdfParameters";
	public static final String EXTRACTION_API = "extractionAPI";
	public static final String PDF_OPTIMIZATION_SWITCH = "pdfOptimizationSwitch";
	public static final String MULTIPAGE_TIF_SWITCH = "multipageTifSwitch";
	public static final String PDF_OPTIMIZATION_PARAMS = "pdfOptimizationParams";
	public static final String IMAGE_PROCESSING_API = "imageProcessingAPI";
	public static final String DOCUMENT_TYPE = "documentType";
	public static final String HOCR_FILE = "hocrFile";
	public static final String IMAGE_LIMIT = "imageLimit";
	public static final String FIXEDFORM_FOLDER_NAME = "fixed-form-extraction";
	public static final String PAGE_PROCESS_FILE_TYPE = "pageProcessFile";
	public static final String ZIP_OUTPUT_LOCATION = "zipOutputLocation";
	public static final String ZIP_NAME = "zipName";

	/**
	 * path of font file
	 */
	public static final String FONT_FILE_PATH = EphesoftStringUtil.concatenate("font", File.separator, "font.ttf");

	/**
	 * String representation for IMAGE_MAGICK
	 */
	public static final String IMAGE_MAGICK = "IMAGE_MAGICK";

	/**
	 * String representation for ITEXT
	 */
	public static final String ITEXT = "ITEXT";

	/**
	 * Constant for string 'nuance'.
	 */
	public static final String NUANCE = "nuance";

	public static Properties fetchConfig() throws IOException {

		final ClassPathResource classPathResource = new ClassPathResource(BACKUP_PROPERTY_FILE);
		final Properties properties = new Properties();

		InputStream input = null;
		try {
			input = classPathResource.getInputStream();
			properties.load(input);
		} finally {
			if (input != null) {
				IOUtils.closeQuietly(input);
			}
		}
		return properties;
	}

	public static String createWebServiceWorkingDir(final String parentDir) throws Exception {
		final String currentWorkingFolderName = generateRandomFolderName();
		final File dir = new File(parentDir + File.separator + currentWorkingFolderName + File.separator + serverInputFolderName);

		boolean createDir = dir.mkdirs();
		if (!createDir) {
			createDir = dir.mkdirs();
			if (!createDir) {
				throw new Exception("Error occured while creating the working dir for web services. Returning from web services.");
			}
		}
		return dir.getAbsolutePath();
	}

	public static String generateRandomFolderName() {
		final Random random = new Random();
		return String.valueOf(System.nanoTime()) + String.valueOf(random.nextInt(1000)) + String.valueOf(random.nextInt(1000))
				+ String.valueOf(random.nextInt(1000));
	}

	public static String createWebServiceOutputDir(final String workingDir) throws Exception {
		final File dir = new File(new File(workingDir).getParent() + File.separator + WebServiceUtil.serverOutputFolderName);
		boolean createDir = dir.mkdirs();
		if (!createDir) {
			createDir = dir.mkdirs();
			if (!createDir) {
				throw new Exception("Error occured while creating the working dir for web services. Returning from web services.");
			}
		}
		return dir.getAbsolutePath();
	}

	/**
	 * Validates input for splitMultipageFile API
	 * 
	 * @param fileMap {@link MultiValueMap<String, MultipartFile>} is the list of input files
	 * @param isGSTool {@link String}is the input provided by user with <b>values</b> <i>TRUE or FALSE</i>
	 * @param outputParams {@link String}
	 * @param inputParams {@link String} should not be empty if isGSTool is true
	 * @return <code>errorMessage</code> the string with the error caused
	 */
	public static String validateSplitAPI(final MultiValueMap<String, MultipartFile> fileMap, final boolean isGSTool,
			final String outputParams, final String inputParams) {
		String errorMessage = EMPTY_STRING;

		errorMessage = validateInputAndOutputParams(inputParams, outputParams);

		if (fileMap.size() == 0 && !fileMap.keySet().isEmpty()) {
			errorMessage = "No files provided to split";
		} else if (isGSTool && inputParams.isEmpty()) {
			errorMessage = "Input Params expected with GhostScript tool flag. Please set the input params.Also check parameter case.";
		}

		return errorMessage;
	}

	public static String validateExportBatchClassAPI(final String imBaseFolderName, final String identifier,
			final String searchSampleName) {
		String results = EMPTY_STRING;
		if (imBaseFolderName.isEmpty()) {
			results = "Please input the valid imBaseFolderName.";
		} else if (identifier.isEmpty()) {
			results = "Please input the valid identifier.";
		} else if (searchSampleName.isEmpty()) {
			results = "Please input the valid searchSampleName.";
		}
		return results;
	}

	/**
	 * Validates the searchablePDF input parameters
	 * 
	 * @param outputPDFFileName {@link String} is the name which should end in.pdf extension
	 * @param projectFile {@link String} is the name should end in .rsp extension
	 * @param pdfFileType {@link String} is string representation of '.pdf'
	 * @param isSearchableImage {@link String}is the input provided by user with <b>values</b> <i>TRUE or FALSE</i>
	 * @param isColorImage {@link String} is the input provided by user with <b>values</b> <i>TRUE or FALSE</i>
	 * @return result which has the error which is occurred in the input
	 */
	public static String validateSearchableAPI(final String outputPDFFileName, final String projectFile, final String pdfFileType,
			final String isSearchableImage, final String isColorImage, final String ocrEngine) {

		String errorMessage = EMPTY_STRING;

		if (EphesoftStringUtil.isNullOrEmpty(outputPDFFileName) || !outputPDFFileName.endsWith(pdfFileType)) {
			errorMessage = "Problem in parameter outputPDFFileName. Either the PDF file name is empty or invalid.Also check parameter case.";
		} else if ((EphesoftStringUtil.isNullOrEmpty(isSearchableImage) || !(EphesoftStringUtil.isValidBooleanValue(isSearchableImage)))) {
			errorMessage = "Problem in parameter isSearchableImage. Either invalid value or not specified";
		} else if ((EphesoftStringUtil.isNullOrEmpty(isColorImage) || !(EphesoftStringUtil.isValidBooleanValue(isColorImage)))) {
			errorMessage = "Problem in parameter isColorImage. Either invalid value or  not specified.Also check parameter case.";
		} else if ((EphesoftStringUtil.isNullOrEmpty(projectFile) || !projectFile.endsWith(WebServiceUtil.RSP_EXTENSION))) {
			// check project file only if Nuance is not selected for ocr'ing.
			if (!NUANCE.equalsIgnoreCase(ocrEngine)) {
				errorMessage = "Problem in parameter projectFile. Invalid name for projectFile file name.Also check parameter case.";
			}
		}
		return errorMessage;
	}

	public static String validateCreateOCRAPI(final String workingDir, final String tool, final String colorSwitch,
			final String projectFile, final String tesseractVersion, final String cmdLanguage) {
		String results = EMPTY_STRING;
		final File rspFile = new File(workingDir + File.separator + projectFile);
		if (tool == null || tool.isEmpty()) {
			results = "Please select the tool for creating OCR.";
		}
		if (results.isEmpty() && tool.equalsIgnoreCase("tesseract")) {
			if (tesseractVersion == null || tesseractVersion.isEmpty()) {
				results = "Please input the valid tesseractVersion.";
			} else if (cmdLanguage == null || cmdLanguage.isEmpty()) {
				results = "Please input the valid cmdLanguage.";
			}
			// cmdLanguage and Tesseract Version expected
		} else if (tool.equalsIgnoreCase("recostar")) {
			if (projectFile == null || projectFile.isEmpty()) {
				results = "Please input the valid projectFile.";
			} else if (colorSwitch == null || colorSwitch.isEmpty()) {
				results = "Please input the valid colorSwitch.";
			} else if (!rspFile.exists()) {
				results = "RSP file as specified in the params is not supplied in valid input. Please specify the correct input params.";
			}
		} else if (tool.equalsIgnoreCase(NUANCE)) {
			if (EphesoftStringUtil.isNullOrEmpty(colorSwitch)) {
				results = "Please input the valid colorSwitch.";
			}
		} else {
			results = "Please select the valid tool for creating OCR.";
		}
		return results;
	}

	public static String validateBarcodeExtractionInput(final String batchClassIdentifier, final String imageName, final String docType) {
		String results = EMPTY_STRING;
		if (batchClassIdentifier == null || batchClassIdentifier.isEmpty()) {
			results = "Batch Class Identifier is not valid.";
		} else if ((imageName == null || imageName.isEmpty()) && results.isEmpty()) {
			results = "image name is not valid.";
		} else if ((docType == null || docType.isEmpty()) && results.isEmpty()) {
			results = "doc type is not valid.";
		}
		return results;
	}

	public static String validateCreateMultiPageFile(final String ghostscriptPdfParameters, final String imageProcessingAPI,
			final String pdfOptimizationSwitch, final String multipageTifSwitch, final String pdfOptimizationParams) {
		String results = EMPTY_STRING;
		if (imageProcessingAPI.isEmpty()
				|| !(imageProcessingAPI.equalsIgnoreCase("IMAGE_MAGICK") || imageProcessingAPI.equalsIgnoreCase("GHOSTSCRIPT") || imageProcessingAPI
						.equalsIgnoreCase("ITEXT"))) {
			results = "ToolName incorrect or not specified. Please provide either of the following values:IMAGE_MAGICK or GHOSTSCRIPT or ITEXT.";
		} else if (!(pdfOptimizationSwitch.equalsIgnoreCase("ON") || pdfOptimizationSwitch.equalsIgnoreCase("OFF"))) {
			results = "pdfOptimizationSwitch incorrect or not specified. Please provide either of the following ON or OFF.";
		} else if (!(multipageTifSwitch.equalsIgnoreCase("ON") || multipageTifSwitch.equalsIgnoreCase("OFF"))) {
			results = "multipageTifSwitch incorrect or not specified. Please provide either of the following ON or OFF.";
		}
		if (results.isEmpty()) {
			if (imageProcessingAPI.equalsIgnoreCase("GHOSTSCRIPT")) {
				if (ghostscriptPdfParameters.isEmpty()) {
					results = "Please provide ghostscriptPdfParameters with GHOSTSCRIPT tool.";
				}
			}
			if (pdfOptimizationSwitch.equalsIgnoreCase("on") && !imageProcessingAPI.equalsIgnoreCase("IMAGE_MAGICK")
					&& pdfOptimizationParams.isEmpty()) {
				results = "Please provide pdfOptimizationParams with pdfOptimizationSwitch ON.";
			}
		}
		return results;
	}

	/**
	 * Validates input for extractFuzzyDB webService
	 * 
	 * @param workingDir {@link String} is the temporary working directory
	 * @param hocrFile {@link String} is the hocrFile name provided by the user
	 * @param batchClassIdentifier {@link String} is the valid Identifier such as BC1
	 * @param documentType {@link String} valid documentType should be present
	 * @param hocrFileName {@link String} provided file name must match with input file name
	 * @return
	 */
	public static String validateExtractFuzzyDBAPI(final String workingDir, final String hocrFile, final String batchClassIdentifier,
			final String documentType, final String hocrFileName) {

		String results = EMPTY_STRING;
		final File file = new File(EphesoftStringUtil.concatenate(workingDir, File.separator, hocrFile));

		if (EphesoftStringUtil.isNullOrEmpty(workingDir)) {
			results = "Problem in parameter 'workingDir'.Enter valid value for existing directory.Also check the case of parameter. ";
		} else if (EphesoftStringUtil.isNullOrEmpty(hocrFile)) {
			results = "Problem in parameter 'hocrFilePath'.Enter valid value for existing path.Also check the case of parameter. ";
		} else if (EphesoftStringUtil.isNullOrEmpty(batchClassIdentifier)) {
			results = "Problem in parameter 'batchClassIdentifier'.Enter valid value like BC1.Also check the case of parameter. ";
		} else if (EphesoftStringUtil.isNullOrEmpty(documentType)) {
			results = "Problem in parameter 'documentType'.Enter valid value.Also check the case of parameter. ";
		} else if (!hocrFile.equalsIgnoreCase(hocrFileName)) {
			results = "Problem in parameter 'hocrFile'. Enter the same name as the file provided. Also check the parameter case. ";
		} else if (!file.exists()) {
			results = "HOCR file as specified in the params is not supplied in valid input. Please specify the correct input params.";
		}
		return results;
	}

	/**
	 * validates input parameters to the web service
	 * 
	 * @param workingDirectory wotrking directory
	 * @param batchClassIdentifier batch class identifier
	 * @param docType document type
	 * @return
	 */
	public static String validateExtractFixedFormAPI(final String workingDirectory, final String batchClassIdentifier,
			final String docType) {
		String results = EMPTY_STRING;
		if (EphesoftStringUtil.isNullOrEmpty(workingDirectory)) {
			results = "Invalid working directory path.";
		} else if (EphesoftStringUtil.isNullOrEmpty(batchClassIdentifier)) {
			results = "Either invalid value or parameter batchClassIdentifier not specified.";
		} else if (EphesoftStringUtil.isNullOrEmpty(docType)) {
			results = "Either invalid value or parameter docType not specified.";
		}
		return results;
	}

	/**
	 * validates for color switch
	 * 
	 * @param colorSwitch value of color switch
	 * @return empty if color switch is successfully validated
	 */
	public static String validateColorSwitch(final String colorSwitch) {
		String results = EMPTY_STRING;
		if (EphesoftStringUtil.isNullOrEmpty(colorSwitch)) {
			results = "Either invalid or null value for colorSwitch ";
		}
		return results;

	}

	/**
	 * Validates if the PPF file is present at location
	 * 
	 * @param workingDir {@link String} the path till shared folder within Ephesoft
	 * @param batchClassIdentifier {@link String} name of the batch class
	 * @param fileName {@link String} the name of the RSP /ZON file as obtained from document type
	 * 
	 * @return a blank string if the file is validated
	 * @throws IOException
	 */
	public static String getProjectProcessingFileFixedFormAPI(final String projectProcessFileLocation, final String workingDir,
			final String fileName) throws IOException {
		String results = EMPTY_STRING;
		if (EphesoftStringUtil.isNullOrEmpty(projectProcessFileLocation) || EphesoftStringUtil.isNullOrEmpty(fileName)) {
			results = "Unable to find projectProcessFileLocation or fileName ";
		} else {
			File srcFile = new File(projectProcessFileLocation);
			if (!srcFile.exists()) {
				results = "Unable to find the project file in working dir";
			} else {
				final File destFile = new File(EphesoftStringUtil.concatenate(workingDir, File.separator, fileName));
				try {
					FileUtils.copyFile(srcFile, destFile);
				} catch (IOException ioexception) {
					results = "Unable to copy files to the working dir";
				}
			}
		}
		return results;
	}

	/**
	 * returns the path of the page processing file
	 * 
	 * @return String with path of page processing file
	 */
	public static String obtainProcessFileLocation(String projectProcessFileLocation, String batchClassIdentifier,
			String PageProcessFile) {
		return EphesoftStringUtil.concatenate(projectProcessFileLocation, File.separator, batchClassIdentifier, File.separator,
				WebServiceUtil.FIXEDFORM_FOLDER_NAME, File.separator, PageProcessFile);
	}

	/**
	 * validates the input parameters for converTiffToPdf API
	 * 
	 * @param pdfGeneratorEngine {@link String} should be either IMAGE_MAGICK or ITEXT
	 * @param inputParams {@link String} works only if engine is IMAGE_MAGICK
	 * @param outputParams {@link String} works only if engine is IMAGE_MAGICK
	 * @return <code>errorMessage</code> the string which displays the problem
	 */
	public static String validateConvertTiffToPdfAPI(final String pdfGeneratorEngine, final String inputParams,
			final String outputParams) {
		String errorMessage = EMPTY_STRING;
		if (EphesoftStringUtil.isNullOrEmpty(pdfGeneratorEngine)
				|| (!pdfGeneratorEngine.equalsIgnoreCase(IMAGE_MAGICK) && !pdfGeneratorEngine.equalsIgnoreCase(ITEXT))) {
			errorMessage = "Problem in pdfGeneratorEngine parameter.Expected are 'IMAGE_MAGICK' or 'ITEXT'.Also check parameter case. ";

		} else if (pdfGeneratorEngine.equalsIgnoreCase(IMAGE_MAGICK)) {
			errorMessage = validateInputAndOutputParams(inputParams, outputParams);
		}
		return errorMessage;
	}

	public static String validateExtractRegexFieldsAPI(final String workingDir, final String batchClassIdentifier,
			final String documentType, final String hocrFileName) {
		String results = EMPTY_STRING;
		if (results.isEmpty() && (workingDir == null || workingDir.isEmpty())) {
			results = "Invalid working directory path.";
		}

		if (results.isEmpty() && (batchClassIdentifier == null || batchClassIdentifier.isEmpty())) {
			results = "Either invalid or null value for batchClassIdentifier...";
		}

		if (results.isEmpty() && (documentType == null || documentType.isEmpty())) {
			results = "Either invalid or null value for documenType...";
		}

		if (results.isEmpty() && (hocrFileName == null || hocrFileName.isEmpty())) {
			results = "Either invalid or null value for hocrFileName...";
		}
		return results;
	}

	/**
	 * 
	 * Validates input for extractImage WebService
	 * 
	 * @param workingDir {@link String} is the temporary working directory
	 * @param ocrTool {@link String} is the tool used for ocr. Eg. Tesseract
	 * @param colorSwitch {@link String} can be on or off
	 * @param projectFile {@link String} is absolute path of project file
	 * @param cmdLanguage {@link String} is input by user
	 * @param tifFileName {@link String} is name fo the file
	 * @return
	 */
	public static String validateExtractFromImageAPI(final String workingDir, final String ocrTool, final String colorSwitch,
			final String projectFile, final String cmdLanguage, final String tifFileName) {
		String results = EMPTY_STRING;

		if (EphesoftStringUtil.isNullOrEmpty(ocrTool)) {
			results = "OCR ENGINE is null or empty. Please provide a valid value.";
		} else if (ocrTool.equalsIgnoreCase(WebServiceUtil.TESSERACT)) {
			if (EphesoftStringUtil.isNullOrEmpty(cmdLanguage)) {
				results = "Please input the valid cmdLanguage.";
			}
			// cmdLanguage and Tesseract Version expected
		} else if (ocrTool.equalsIgnoreCase(WebServiceUtil.RECOSTAR)) {
			if (results.isEmpty() && EphesoftStringUtil.isNullOrEmpty(projectFile)) {
				results = "Please input the valid projectFile.";
			} else if (EphesoftStringUtil.isNullOrEmpty(colorSwitch)
					|| (!(colorSwitch.equalsIgnoreCase("ON") && colorSwitch.equalsIgnoreCase("OFF")))) {
				results = "Please input the valid colorSwitch.";
			} else if (!new File(projectFile).exists()) {
				results = "RSP file as specified in the params is not supplied in valid input. Please specify the correct input params.";
			}
		} else if (EphesoftStringUtil.isNullOrEmpty(tifFileName)) {
			results = "No Image selected. Please make sure an image with an extension tif/tiff/png is attached.";
		} else {
			results = "Please select the valid tool for creating OCR.";
		}
		return results;
	}

	/**
	 * Validates input and output parameters for null value
	 * 
	 * @param inputParams {@link String} are provided by user and can be empty used to compress PDF
	 * @param outputParams {@link String} are provided by user and can be empty used to compress PDF
	 * @return <code>errorMessage</code> specifying the problem
	 */
	public static String validateInputAndOutputParams(String inputParams, String outputParams) {

		String errorMessage = EMPTY_STRING;

		if (inputParams == null || outputParams == null) {

			errorMessage = "Either inputParams or outputParams may have not been specified.These arguments can remain empty but they need to be declared.Also check parameter case.";
		}

		return errorMessage;
	}
	

	
	/**
	 * copies file from source to destination 
	 * 
	 * @param workingDir working folder path
	 * @param inputFileName name of file 
	 * @param instream object of input stream of the input file
	 * @throws IOException
	 */
	public static void copyFile(final String workingDir,final String inputFileName,final InputStream instream) throws IOException
	{
		OutputStream outStream = null;
		try
		{
		final File inputFile = new File(EphesoftStringUtil.concatenate(workingDir, File.separator, inputFileName));
		outStream = new FileOutputStream(inputFile);
		final byte[] buf = new byte[WebServiceUtil.bufferSize];
		int len;
		while ((len = instream.read(buf)) > 0) {
			outStream.write(buf, 0, len);
		}
		
	} finally {
		IOUtils.closeQuietly(instream);
		IOUtils.closeQuietly(outStream);
	}
	}
}
