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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.encryption.service.EncryptionKeyService;
import com.ephesoft.dcma.batch.encryption.util.CryptoMarshaller;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.OSUtil;
import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;
import com.ephesoft.dcma.util.exception.KeyNotFoundException;
import com.ephesoft.dcma.batch.dao.xml.BatchSchemaDao;
import com.ephesoft.dcma.batch.schema.BatchLevelField;





/**
 * Implementation of functionality for exporting batch class which for COPY_BATCH_XML plugin.
 * 
 * @author Ephesoft
 * @version 3.0
 * 
 */
@Component
public class FolderExporter implements ICommonConstants {

	private static final String COPY_BATCH_XML_PLUGIN = "COPY_BATCH_XML";

	private static final Logger LOGGER = LoggerFactory.getLogger(FolderExporter.class);

	private String invalidChars;

	private String replaceChar;

	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Marshaler that is used to export the XML files to the corresponding plugin.
	 */
	@Autowired
	private CryptoMarshaller cryptoMarshaller;

	/**
	 * Service that provides access to the encryption keys and algorithm corresponding to the instance which needs to be exported.
	 */
	@Autowired
	private EncryptionKeyService encryptionKeyService;


	@Autowired
	private BatchInstanceDao batchInstanceDao;
	
	@Autowired
	private BatchSchemaDao batchSchemaDao;
	
	/**
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the pluginPropertiesService
	 */
	public PluginPropertiesService getPluginPropertiesService() {
		return pluginPropertiesService;
	}

	/**
	 * @param pluginPropertiesService the pluginPropertiesService to set
	 */
	public void setPluginPropertiesService(final PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * @return the invalid characters list.
	 */
	public String getInvalidChars() {
		return invalidChars;
	}

	/**
	 * @param invalidChars invalid characters list to set.
	 */
	public void setInvalidChars(final String invalidChars) {
		this.invalidChars = invalidChars;
	}

	/**
	 * @return the replace character for invalid chars.
	 */
	public String getReplaceChar() {
		return replaceChar;
	}

	/**
	 * @param replaceChar replcae character to be set.
	 */
	public void setReplaceChar(final String replaceChar) {
		this.replaceChar = replaceChar;
	}

	/**
	 * This method simply reads the batch.xml file in the sFolderToBeExported. It finds the names of multipage tiff and pdf files from
	 * the batch.xml. Then it moves these files to the export to folder.
	 * 
	 * @param batchInstanceID {@link String} batch instance identifier for current executing batch.
	 * @throws {@link JAXBException}
	 * @throws {@link DCMAApplicationException}
	 */
	public void exportFiles(final String batchInstanceID) throws JAXBException, DCMAApplicationException {
		final String exportToFolderSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID, COPY_BATCH_XML_PLUGIN,
				ExportProperties.EXPORT_TO_FOLDER_SWITCH);
		LOGGER.info("Export to folder switch: " + exportToFolderSwitch);
		//Fix for JIRA Issue : 15753 
		// Copy batch XML -> Copy batch XML with "Export to Folder Switch" is not working fine when switch is OFF.
		if (IExportCommonConstants.ON_SWITCH.equalsIgnoreCase(exportToFolderSwitch)) {
			// Initialize properties
			LOGGER.info("Initializing properties...");
		final String sFolderToBeExported = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
		BatchPlugin batchPlugin = pluginPropertiesService.getPluginProperties(batchInstanceID, COPY_BATCH_XML_PLUGIN);
		String exportToFolder = batchPlugin.getPluginConfigurationValue(ExportProperties.EXPORT_FOLDER);
		String batchXmlFolderName = batchPlugin.getPluginConfigurationValue(ExportProperties.BATCH_XML_EXPORT_FOLDER);
		String copyFileType = batchPlugin.getPluginConfigurationValue(ExportProperties.COPY_MULTIPAGE_FILE_TYPE);
		String exportFileName = batchPlugin.getPluginConfigurationValue(ExportProperties.EXPORT_MULTIPAGE_FILE_NAME);
		String exportMultipageFilePath = batchPlugin.getPluginConfigurationValue(ExportProperties.EXPORT_MULTIPAGE_FOLDER_PATH);

			final File fFolderToBeExported = new File(sFolderToBeExported);
		if (!fFolderToBeExported.exists() || !fFolderToBeExported.isDirectory()) {
			LOGGER.error("Folder does not exist folder name=" + fFolderToBeExported);
				throw new DCMABusinessException("Folder not found.");
			}
			LOGGER.info("Properties Initialized Successfully");
			final PrivateKeyEncryptionAlgorithm batchEncryptionAlgorithm = encryptionKeyService
					.getBatchInstanceEncryptionAlgorithm(batchInstanceID);
			byte[] encryptionKey = null;
			try {
			encryptionKey = encryptionKeyService.getBatchInstanceKey(batchInstanceID);
			} catch (final KeyNotFoundException keyNotFoundException) {
			throw new DCMAApplicationException("Batch Instance Key Could not be retrieved", keyNotFoundException);
			}
		final String[] invalidChars = this.invalidChars.split(IExportCommonConstants.INVALID_CHAR_SEPARATOR);

			File exportToBatchFolder = null;
		if (batchXmlFolderName == null || batchXmlFolderName.isEmpty()
				|| batchXmlFolderName.equalsIgnoreCase(IExportCommonConstants.FINAL_EXPORT_FOLDER)) {
				exportToBatchFolder = new File(exportToFolder);
			} else {
			exportToBatchFolder = new File(exportToFolder + File.separator + batchInstanceID);
			}
            exportToBatchFolder.mkdirs();
			if (!exportToBatchFolder.exists()) {
			throw new DCMABusinessException("Could not create folder= " + exportToBatchFolder.getAbsolutePath());
			}
		LOGGER.info("Created Export To Folder=" + exportToBatchFolder.getAbsolutePath());
		final String xmlFileName = batchInstanceID + ICommonConstants.UNDERSCORE_BATCH_XML;
			Batch oldBatch = batchSchemaService.getBatch(batchInstanceID);
		String batchXmlString = batchSchemaDao.marshalObjectAsString(oldBatch);
			final Batch batch = batchSchemaDao.unmarshalString(batchXmlString);
			final Documents documents = batch.getDocuments();
			if (documents != null && documents.getDocument() != null) {
				final List<Document> listOfDocuments = documents.getDocument();

				// Export batch XML file to exportToBatchFolder.
			final String destinationXmlFileLocation = exportToBatchFolder.getPath() + File.separator + xmlFileName;
				try {
				exportBatchXML(batchInstanceID, xmlFileName, destinationXmlFileLocation);

				// adding new property for specifying the type of file to be exported.
				if (!(EphesoftStringUtil.isNullOrEmpty(exportFileName) && EphesoftStringUtil.isNullOrEmpty(exportMultipageFilePath))) {
					exportMultiPagePdfAndTiffFiles(batchInstanceID, exportFileName, fFolderToBeExported, exportMultipageFilePath,
							listOfDocuments, invalidChars, copyFileType);
					}
				
				// Adding Final MultiPage Tiff and Pdf File Path to old batch.xml for DB export.
					updateOldBatchXml(oldBatch, batch);
				updateExportedBatchXml(batch, new File(destinationXmlFileLocation), batchEncryptionAlgorithm, encryptionKey);
				} catch (final Exception excep) {
				throw new DCMAApplicationException("Error ocurred while exporting files.", excep);
				}
			}
		}
	}

	/**
	 * This method updates the exported batch xml.
	 * 
	 * @param batch {@link Batch}
	 * @param destinationXmlFile {@link File}
	 * @throws Exception
	 */
	private void updateExportedBatchXml(final Batch batch, final File destinationXmlFile,
			final PrivateKeyEncryptionAlgorithm encryptionAlgorithm, final byte[] encryptionKey) throws Exception {
		LOGGER.info("Entering updateExportedBatchXml method");
		FileOutputStream xmlFileStream = null;
		FileInputStream xmlInputSteam = null;
		try {
			LOGGER.info("Updating batch XML: " + destinationXmlFile.getAbsolutePath());
			xmlFileStream = new FileOutputStream(destinationXmlFile);
			StreamResult xmlStreamResult = new StreamResult(xmlFileStream);
			cryptoMarshaller.marshal(batch, xmlStreamResult, encryptionKey, encryptionAlgorithm);
			LOGGER.info("Batch XML updated successfully: " + destinationXmlFile.getAbsolutePath());

		} catch (final Exception exception) {
			LOGGER.error("Batch XML could not be updated: " + destinationXmlFile.getAbsolutePath(), exception);
			throw exception;
		} finally {
			IOUtils.closeQuietly(xmlFileStream);
		}

		if (encryptionKey != null) {
			xmlInputSteam = new FileInputStream(destinationXmlFile);
			org.jdom.Document document = XMLUtil.createJDOMDocumentFromInputStream(xmlInputSteam);
			IOUtils.closeQuietly(xmlInputSteam);
			xmlFileStream = new FileOutputStream(destinationXmlFile);
			try {
				XMLOutputter out = new XMLOutputter();
				out.output(document, xmlFileStream);
			} catch (Exception exception) {
				LOGGER.error(exception.getMessage());
			} finally {
				IOUtils.closeQuietly(xmlInputSteam);
				IOUtils.closeQuietly(xmlFileStream);
			}
		}
		LOGGER.info("Exiting updateExportedBatchXml method");
	}

	/**
	 * This method updates the document files based on the format and parameters specified by admin and exports them to the export
	 * batch folder.
	 * 
	 * @param batchInstanceID {@link String} batch instance identifier for current executing batch.
	 * @param fileNameFormat {@link String} format of file to be exported.
	 * @param fileNameArr {@link String} the array of file names present.
	 * @param fFolderToBeExported {@link File} the folder to be exported.
	 * @param exportToFolder {@link String} the folder to which files need to be exported.
	 * @param listOfDocuments {@link List<Document>} the list of documents present in batch xml.
	 * @param invalidChars {@link String[]} list of invalid characters provided.
	 * @param copyFileType {@link String} the type of file that needs to be exported.
	 * @throws DCMAApplicationException
	 * @throws IOException 
	 */
	private void exportMultiPagePdfAndTiffFiles(final String batchInstanceID, final String exportFileName,
			final File fFolderToBeExported, final String exportFolderPath, final List<Document> listOfDocuments,
			final String[] invalidChars, final String copyFileType) throws DCMAApplicationException {
		String basePath = batchSchemaService.getBaseFolderLocation();
		for (final Document document : listOfDocuments) {
			File exportToBatchFolder = null;
			String finalMultiPagePdf = EMPTY_STRING;
			String finalMultiPageTif = EMPTY_STRING;
			String fileNameSeperator = EMPTY_STRING;
			boolean isDocumentIDAppendRequired = false ;
			String fileName = generateDynamicFileName(exportFileName, false,document, batchInstanceID);
			if (!exportFileName.contains(IExportCommonConstants.DOCUMENT_ID)) {
				isDocumentIDAppendRequired = true;
			}
			LOGGER.info(EphesoftStringUtil.concatenate("file name obtained is ", fileName));
			String folderDestination = generateDynamicFileName(exportFolderPath,true, document, batchInstanceID);
			LOGGER.info(EphesoftStringUtil.concatenate("folder name obtained is ",folderDestination));
			if (EphesoftStringUtil.isNullOrEmpty(folderDestination)) {
				LOGGER.info("Multipage folder path is incorrect");
				continue;
			}
			String EFFECTIVE_REGEX_PATTERN = EphesoftStringUtil.concatenate(fileName, IExportCommonConstants.FILE_NAME_REGEX);
			File outputDirectory = null;
			try {
				outputDirectory = new File(folderDestination);
			} catch (Exception e) {
			}
			if (!EphesoftStringUtil.isNullOrEmpty(fileName)) {
				fileNameSeperator = IExportCommonConstants.MODIFIED_FILE_NAME_SEPERATOR;
			}
			String finalMultiPageFileName = fileName;
			if (isDocumentIDAppendRequired) {
				finalMultiPageFileName = EphesoftStringUtil.concatenate(fileName, fileNameSeperator, document.getIdentifier());
			}
			if (outputDirectory != null && outputDirectory.exists()) {
                List<String> uploadFileList = null;
                    uploadFileList = com.ephesoft.dcma.util.FileUtils.getFileNamesWithRegexMatchWithoutExtension(folderDestination,
                            EFFECTIVE_REGEX_PATTERN);
				if (uploadFileList.contains(finalMultiPageFileName)) {
					Collections.sort(uploadFileList, Collections.reverseOrder());
					finalMultiPageFileName = obtainEffectiveFileName(uploadFileList.get(0));
				}
			}
			final String sMultiPagePdf = document.getMultiPagePdfFile();
			final String sMultiPageTif = document.getMultiPageTiffFile();
			finalMultiPagePdf = finalMultiPageFileName + FileType.PDF.getExtensionWithDot();
			finalMultiPageTif = finalMultiPageFileName + FileType.TIF.getExtensionWithDot();
			exportToBatchFolder = new File(folderDestination);
			LOGGER.info("Exporting files to folder : " + exportToBatchFolder.getAbsolutePath());
			if (FileType.TIFF.getExtension().equalsIgnoreCase(copyFileType)) {
				exportingTiffFiles(fFolderToBeExported, exportToBatchFolder, document, sMultiPageTif, finalMultiPageTif);
				createWaterMark(exportToBatchFolder, finalMultiPageTif, basePath);
			} else if (FileType.PDF.getExtension().equalsIgnoreCase(copyFileType)) {
				exportingPDFFiles(fFolderToBeExported, exportToBatchFolder, document, sMultiPagePdf, finalMultiPagePdf);
				createWaterMark(exportToBatchFolder, finalMultiPagePdf, basePath);
			} else {
				exportingTiffFiles(fFolderToBeExported, exportToBatchFolder, document, sMultiPageTif, finalMultiPageTif);
				exportingPDFFiles(fFolderToBeExported, exportToBatchFolder, document, sMultiPagePdf, finalMultiPagePdf);
				createWaterMark(exportToBatchFolder, finalMultiPagePdf, basePath);
				createWaterMark(exportToBatchFolder, finalMultiPageTif, basePath);

			}
		}
	}
	
	private void createWaterMark(final File exportToBatchFolder,
			final String finalMultiPageTif, final String baseFolderPath) throws DCMAApplicationException {
		StringBuffer command = new StringBuffer(EMPTY_STRING);
		String[] cmds = null;
		final ArrayList<String> commandList = new ArrayList<String>();
		
		if (OSUtil.isWindows()) {
			 commandList.add(EphesoftStringUtil.concatenate(IExportCommonConstants.QUOTES, System.getenv(IExportCommonConstants.IM4JAVA_TOOLPATH),
			File.separator, IExportCommonConstants.WATERMARK_CONVERT_COMMAND,IExportCommonConstants.QUOTES, IExportCommonConstants.WATERMARK_DENSITY_COMMAND));
			 commandList.add(EphesoftStringUtil.concatenate(IExportCommonConstants.QUOTES,exportToBatchFolder, File.separator, finalMultiPageTif, IExportCommonConstants.QUOTES));
			 commandList.add(EphesoftStringUtil.concatenate(IExportCommonConstants.NULL,IExportCommonConstants.COLON));
			 commandList.add(EphesoftStringUtil.concatenate(IExportCommonConstants.QUOTES, baseFolderPath,  File.separator,IExportCommonConstants.WATERMARK_IMAGE_FOLDEER, File.separator, IExportCommonConstants.WATERMARK_IMAGE_NAME,IExportCommonConstants.QUOTES));
			 commandList.add(IExportCommonConstants.WATERMARK_SUBCOMMAND2);
			 commandList.add(EphesoftStringUtil.concatenate(IExportCommonConstants.QUOTES, exportToBatchFolder, File.separator, finalMultiPageTif, IExportCommonConstants.QUOTES));
		}  else {
			commandList.add(EphesoftStringUtil.concatenate(System.getenv(IExportCommonConstants.IM4JAVA_TOOLPATH), File.separator, IExportCommonConstants.WATERMARK_CONVERT_COMMAND, IExportCommonConstants.WATERMARK_DENSITY_COMMAND));
			 commandList.add(EphesoftStringUtil.concatenate(exportToBatchFolder, File.separator, finalMultiPageTif));
			 commandList.add(EphesoftStringUtil.concatenate(IExportCommonConstants.NULL,IExportCommonConstants.COLON));
			 commandList.add(EphesoftStringUtil.concatenate(baseFolderPath,  File.separator,IExportCommonConstants.WATERMARK_IMAGE_FOLDEER, File.separator, IExportCommonConstants.WATERMARK_IMAGE_NAME));
			 commandList.add(IExportCommonConstants.WATERMARK_SUBCOMMAND2);
			 commandList.add(EphesoftStringUtil.concatenate(exportToBatchFolder, File.separator, finalMultiPageTif));
		}
		 cmds = (String[]) commandList.toArray(new String[commandList.size()]);
		 LOGGER.info("Starting execution of ");
			for (int ind = 0; ind < cmds.length; ind++) {
				if (cmds[ind] == null) {
					cmds[ind] = "";
				}
				LOGGER.info(cmds[ind]);
				command.append(cmds[ind] + " ");
			}
			try {
				LOGGER.info("Command : "+command.toString());
			executeCommand(command);
			} catch(IOException ioEx)
			{
				throw new DCMAApplicationException("Problem executing image magick command for watermarking : ", ioEx);
			}
	}

	private void executeCommand(StringBuffer command) throws IOException {
		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(command.toString());
		String cmdOutput = "";
		InputStreamReader inputStreamReader = null;
		BufferedReader sysErr = null;
		try {
			inputStreamReader = new InputStreamReader(proc.getErrorStream());
			sysErr = new BufferedReader(inputStreamReader);
			cmdOutput = sysErr.readLine();
			LOGGER.info("cmdOutput = " + cmdOutput);
		} catch (IOException ioe) {
			LOGGER.error("Exception while reading the buffer : " + ioe.getMessage(), ioe);
		} finally {
			if (null != sysErr) {
				sysErr.close();
			}
			if (null != inputStreamReader) {
				inputStreamReader.close();
			}
		}
		LOGGER.info("cmdOutput = " + cmdOutput);
}
	

	/**
	 * This method is used for excluding the functionality of exporting only PDF files. If exporting multipage pdf files fails then
	 * batch will be sent into error state.
	 * 
	 * @param fFolderToBeExported {@link File} the folder to be exported.
	 * @param exportToBatchFolder {@link File} the file folder where files has to be exported.
	 * @param document {@link Document} the current document being processed.
	 * @param sMultiPagePdf {@link String} the multipage pdf file path.
	 * @param finalMultiPagePdf {@link String} the final multipage pdf file path.
	 * @throws DCMAApplicationException
	 */
	private void exportingPDFFiles(final File fFolderToBeExported, final File exportToBatchFolder, final Document document,
			final String sMultiPagePdf, final String finalMultiPagePdf) throws DCMAApplicationException {
		if (sMultiPagePdf != null && !sMultiPagePdf.isEmpty()) {
			document.setMultiPagePdfFile(exportPdfFile(fFolderToBeExported, exportToBatchFolder, sMultiPagePdf, finalMultiPagePdf));
		} else {
			String errorMessage = "Multipage pdf files are not present in batch instance folder. Please verify the Create Multipage File Type property in CreateMultipageFiles plugin for creation of multipage pdf files.";
			LOGGER.error(errorMessage);
			throw new DCMAApplicationException(errorMessage);
		}
	}

	/**
	 * This method is used for excluding the functionality of exporting only tiff files. If exporting multipage tiff files fails then
	 * batch will be sent into error state.
	 * 
	 * @param fFolderToBeExported {@link File} the folder to be exported.
	 * @param exportToBatchFolder {@link File} the file folder where files has to be exported.
	 * @param document {@link Document} the current document being processed.
	 * @param sMultiPageTif {@link String} the multipage tiff file path.
	 * @param finalMultiPageTif {@link String} the final multipage tiff file path.
	 * @throws DCMAApplicationException
	 */
	private void exportingTiffFiles(final File fFolderToBeExported, final File exportToBatchFolder, final Document document,
			final String sMultiPageTif, final String finalMultiPageTif) throws DCMAApplicationException {
		if (sMultiPageTif != null && !sMultiPageTif.isEmpty()) {
			document.setMultiPageTiffFile(exportTiffFile(fFolderToBeExported, exportToBatchFolder, sMultiPageTif, finalMultiPageTif));
		} else {
			String errorMessage = "Multipage tiff files are not present in batch instance folder. Please verify the Cretae Multipage File Type property in CreateMultipageFiles plugin for creation of multipage tiff files.";
			LOGGER.error(errorMessage);
			throw new DCMAApplicationException(errorMessage);
		}
	}

	/**
	 * Method to replace invalid characters from file name {fileName} by the replace character specified by admin.
	 * 
	 * @param fileName name of the file from which invalid characters are to be replaced.
	 * @param invalidChars array of invalid characters which are to be replaced.
	 * @return
	 */
	private String replaceInvalidFileChars(final String fileName, final String[] invalidChars) {
		LOGGER.info("Entering removeInvalidFileChars method");
		String updatedFileName = fileName;
		if (fileName != null && !fileName.isEmpty() && invalidChars != null && invalidChars.length > 0) {
			if (replaceChar == null || replaceChar.isEmpty()) {
				LOGGER.info("Replace character not specified. Using default character '-' as a replace character.");
				replaceChar = IExportCommonConstants.DEFAULT_REPLACE_CHAR;
			}
			for (final String invalidChar : invalidChars) {
				if (replaceChar.equals(invalidChar)) {
					LOGGER.info("Replace character not specified or an invalid character. Using default character '-' as a replace character.");
					replaceChar = IExportCommonConstants.DEFAULT_REPLACE_CHAR;
				}
				updatedFileName = updatedFileName.replace(invalidChar, replaceChar);
			}
		}
		LOGGER.info("Exiting removeInvalidFileChars method");
		return updatedFileName;
	}

	/**
	 * This method gets the updated filename as per the file format specified.
	 * 
	 * @param batchInstanceID
	 * @param documentIdentifier
	 * @param fileNameFormat
	 * @param docFieldList
	 * @return
	 */
	private String getUpdatedFileName(final String batchInstanceID, final String documentIdentifier, final String[] fileNameFormat,
			final List<DocField> docFieldList) {
		LOGGER.info("Entering getUpdatedFileName method.");
		final StringBuffer updatedFileName = new StringBuffer();
		boolean isValidParamForFileName = false;
		String dlfValue = null;
		for (String fileFormat : fileNameFormat) {
			fileFormat = fileFormat.trim();
			LOGGER.info("Paramter : " + fileFormat);
			if (fileFormat.startsWith(IExportCommonConstants.PARAM_START_DELIMETER)) {
				fileFormat = fileFormat.substring(1);
				if (fileFormat.equalsIgnoreCase(IExportCommonConstants.EPHESOFT_BATCH_ID)) {
					isValidParamForFileName = true;
					updatedFileName.append(batchInstanceID);
				} else if (fileFormat.equalsIgnoreCase(IExportCommonConstants.EPHESOFT_DOCUMENT_ID)) {
					isValidParamForFileName = true;
					updatedFileName.append(documentIdentifier);
				} else if (docFieldList != null && !docFieldList.isEmpty()) {
					dlfValue = getDlfValue(docFieldList, fileFormat);
					if (dlfValue != null && !dlfValue.isEmpty()) {
						isValidParamForFileName = true;
						updatedFileName.append(dlfValue);
					}
				}
			} else if (isValidParamForFileName) {
				isValidParamForFileName = false;
				updatedFileName.append(fileFormat);
			}
			LOGGER.info("Updated file name: " + updatedFileName);
		}
		LOGGER.info("Exiting getUpdatedFileName method.");
		return updatedFileName.toString();
	}

	/**
	 * This method gets the document level field value for the passed document level field.
	 * 
	 * @param docFieldList {link @List<DocField>}
	 * @param dlfName {@link String}
	 * @return
	 */
	private String getDlfValue(final List<DocField> docFieldList, final String dlfName) {
		LOGGER.info("Entering getDlfValue method.");
		String dlfValue = null;
		boolean dlfFound = false;
		LOGGER.info("Get value for dlf: " + dlfName);
		for (final DocField docField : docFieldList) {
			if (docField.getName() != null && docField.getName().equalsIgnoreCase(dlfName)) {
				final String value = docField.getValue();
				dlfFound = true;
				if (value != null && !value.trim().isEmpty()) {
					LOGGER.info("Dlf found, Value for Dlf: " + value);
					dlfValue = value.trim();
				}
				break;
			}
		}
		LOGGER.info("Dlf found: " + dlfFound);
		LOGGER.info("Exiting getDlfValue method.");
		return dlfValue;
	}

	/**
	 * This method exports the multipage pdf file from fFolderToBeExported to the exportToBatchFolder.
	 * 
	 * @param fFolderToBeExported {@link File} Source folder.
	 * @param exportToBatchFolder {@link File} Destination folder.
	 * @param sMultiPageTif {@link String} File name of file to be exported.
	 * @param dMultiPageTif {@link String} Final file name for the exported file.
	 * @return
	 */
	private String exportTiffFile(final File fFolderToBeExported, final File exportToBatchFolder, final String sMultiPageTif,
			final String dMultiPageTif) {
		File fDestinationTifFile = null;
		if (fFolderToBeExported != null && exportToBatchFolder != null) {
			final File fSourceTifFile = new File(fFolderToBeExported.getPath() + File.separator + sMultiPageTif);
			fDestinationTifFile = new File(exportToBatchFolder.getPath() + File.separator + dMultiPageTif);
			try {
				FileUtils.copyFile(fSourceTifFile, fDestinationTifFile, false);
			} catch (final IOException e) {
				LOGGER.error("Problem copying Tiff file : " + fSourceTifFile, e);
				// throw new DCMAApplicationException("Problem copying Tiff file : " + fSourceTifFile,e);
			}
			if (!fDestinationTifFile.exists()) {
				throw new DCMABusinessException("Unable to export file " + fSourceTifFile);
			}
		}
		return fDestinationTifFile.getAbsolutePath();
	}

	/**
	 * This method exports the multipage tiff file from fFolderToBeExported to the exportToBatchFolder.
	 * 
	 * @param fFolderToBeExported {@link File} Source folder.
	 * @param exportToBatchFolder {@link File} Destination folder.
	 * @param sMultiPagePdf {@link String} File name of file to be exported.
	 * @param dMultiPagePdf {@link String} Final file name for the exported file.
	 * @return
	 */
	private String exportPdfFile(final File fFolderToBeExported, final File exportToBatchFolder, final String sMultiPagePdf,
			final String dMultiPagePdf) {
		File fDestinationPdfFile = null;
		if (fFolderToBeExported != null && exportToBatchFolder != null) {
			final File fSourcePdfFile = new File(fFolderToBeExported.getPath() + File.separator + sMultiPagePdf);
			fDestinationPdfFile = new File(exportToBatchFolder.getPath() + File.separator + dMultiPagePdf);
			try {
				FileUtils.copyFile(fSourcePdfFile, fDestinationPdfFile, false);
			} catch (final IOException e) {
				LOGGER.error("Problem copying PDF file : " + fSourcePdfFile, e);
				// throw new DCMAApplicationException("Problem copying PDF file : " + fSourcePdfFile,e);
			}
			if (!fDestinationPdfFile.exists()) {
				throw new DCMABusinessException("Unable to export file " + fSourcePdfFile);
			}
		}
		return fDestinationPdfFile.getAbsolutePath();
	}

	private void exportBatchXML(final String batchInstanceID, final String xmlFileName, final String destinationFilePath)
			throws DCMAApplicationException {
		batchSchemaService.createBatchXMLAtPath(batchInstanceID, destinationFilePath);
		LOGGER.info("Successfully export file for batch Instance identifier : " + batchInstanceID);
		if (!(new File(destinationFilePath).exists())) {
			throw new DCMABusinessException("Unable to export file " + destinationFilePath);
		}
	}

	/**
	 * Method to update old batch xml. Add Final MultiPage Tiff and Pdf File Path.
	 * 
	 * @param oldBatch {@link Batch}
	 * @param updatedBatch {@link Batch}
	 */
	private void updateOldBatchXml(final Batch oldBatch, final Batch updatedBatch) {
		if (null != oldBatch && null != updatedBatch) {
			final Documents oldBatchDocuments = oldBatch.getDocuments();
			final Documents updatedBatchDocuments = updatedBatch.getDocuments();
			if (null != oldBatchDocuments && null != updatedBatchDocuments) {
				final List<Document> oldDocuments = oldBatchDocuments.getDocument();
				final List<Document> updatedDocuments = updatedBatchDocuments.getDocument();
				if (CollectionUtils.isNotEmpty(oldDocuments) && CollectionUtils.isNotEmpty(updatedDocuments)) {
					for (int docIndex = 0; docIndex < updatedDocuments.size(); docIndex++) {
						final Document updatedDoc = updatedDocuments.get(docIndex);
						final String tiffPath = updatedDoc.getMultiPageTiffFile();
						final String pdfPath = updatedDoc.getMultiPagePdfFile();
						Document oldDocument = oldDocuments.get(docIndex);
						if (null != oldDocument) {
							if (!EphesoftStringUtil.isNullOrEmpty(tiffPath)) {
								oldDocument.setFinalMultiPageTiffFilePath(tiffPath);
							}
							if (!EphesoftStringUtil.isNullOrEmpty(pdfPath)) {
								oldDocument.setFinalMultiPagePdfFilePath(pdfPath);
							}
						}

					}
				}
				batchSchemaService.updateBatch(oldBatch);
			}
		}

	}
	
	
	/**
	 * Generates dynamic name for files or folder path, <b>$<b> symbol denotes fields dynamic in nature and <b>&</b> denotes parts of a file that are to be concatinated.
	 * 
	 * @param folderPath
	 * @param isStringDirectoryPath
	 * @param document
	 * @param batchInstanceID
	 * @return
	 */
	private String generateDynamicFileName(final String folderPath, final boolean isStringDirectoryPath,
			com.ephesoft.dcma.batch.schema.Document document,final String batchInstanceID) throws DCMAApplicationException {
		boolean isValidPathEntered = true;
		String fileNameOrFolderPathStr = IExportCommonConstants.EMPTY_STRING;
		boolean isNetworkPath = false;
		boolean isOSWindows = OSUtil.isWindows();
		StringBuffer finalFolderPath = new StringBuffer();
		String networkFolderSeperator = EphesoftStringUtil.concatenate(File.separator, File.separator);
		if ((isStringDirectoryPath) && (isOSWindows) && (folderPath.trim().startsWith(File.separator))) {
			isNetworkPath = true;
		}
		String[] folderPathSegments = folderPath.trim().split(Pattern.quote(File.separator));
		List<String> documentLevelFields = new ArrayList<String>();
		if (null != document.getDocumentLevelFields()) {
			List<DocField> docFieldList = document.getDocumentLevelFields().getDocumentLevelField();
			for (DocField docfield : docFieldList) {
				documentLevelFields.add(docfield.getName());
			}
		}
		boolean isFolderSubPartBlank = false;
		List<String> reservedKeyList = getReservedList();
		for (String folderPathSegment : folderPathSegments) {
			if (!EphesoftStringUtil.isNullOrEmpty(folderPathSegment)) {
				String[] folderSubParts = folderPathSegment.split(IExportCommonConstants.FILE_NAME_SEPERATOR);
				StringBuffer subFolderName = new StringBuffer();
				for (String folderSubPart : folderSubParts) {
					isFolderSubPartBlank = folderSubPart.trim().equals(IExportCommonConstants.EMPTY_STRING);
					if (!isFolderSubPartBlank) {
						folderSubPart = folderSubPart.trim();
					}
					boolean isFieldDocumentLevelField = false;
					String folderPathSegemntValue = folderSubPart;
					if (folderSubPart.contains(IExportCommonConstants.DOCUMENT_LEVEL_FIELD_TAG)) {
						folderSubPart = folderSubPart.replaceAll(IExportCommonConstants.DOCUMENT_LEVEL_FIELD_TAG,
								IExportCommonConstants.EMPTY_STRING);

						isFieldDocumentLevelField = true;
						String documentLevelFieldName = folderSubPart.substring(1);
						if (!documentLevelFields.contains(documentLevelFieldName)) {
							String errorMessage = EphesoftStringUtil.concatenate(new String[] {documentLevelFieldName,
									" field does not exist in the document type"});

							LOGGER.error(errorMessage);
							continue;
						}
					}
					String folderPart = EphesoftStringUtil.isNullOrEmpty(folderSubPart) ? IExportCommonConstants.EMPTY_STRING
							: folderSubPart.substring(1, folderSubPart.length());
					boolean isFolderPartDateTimeOrBatchField = false;
					if ((folderPart.contains(IExportCommonConstants.DATE)) || (folderPart.contains(IExportCommonConstants.TIME))|| (folderPart.contains(IExportCommonConstants.BATCH_FIELD_TYPE))) {
						isFolderPartDateTimeOrBatchField = true;
					}
					if (((reservedKeyList.contains(folderPart)) && (folderSubPart.contains(IExportCommonConstants.DYNAMIC_FILE_MARKER)))
							|| (isFieldDocumentLevelField) || (isFolderPartDateTimeOrBatchField)) {
						folderPathSegemntValue = returnDynamicValues(folderSubPart, document, batchInstanceID, documentLevelFields);
					} else {
						folderPathSegemntValue = folderSubPart;
					}
					if ((!EphesoftStringUtil.isNullOrEmpty(folderPathSegemntValue)) || (isFieldDocumentLevelField)) {
						subFolderName.append(folderPathSegemntValue);
					}
				}
				String folderSubNameStr = subFolderName.toString();
				if ((EphesoftStringUtil.isNullOrEmpty(finalFolderPath.toString())) && (isStringDirectoryPath)) {
					String directoryPathSeperator = IExportCommonConstants.EMPTY_STRING;
					if (isOSWindows) {
						if (isNetworkPath) {
							directoryPathSeperator = networkFolderSeperator;
						}
					} else {
						directoryPathSeperator = File.separator;
					}
					folderSubNameStr = EphesoftStringUtil.concatenate(new String[] {directoryPathSeperator,
							folderSubNameStr.replaceAll(IExportCommonConstants.INVALID_CHARACTER_LIST_WITHOUT_COLON, this.replaceChar)});
				} else {
					folderSubNameStr = folderSubNameStr.replaceAll(IExportCommonConstants.INVALID_CHARACTERS_LIST,
							this.replaceChar);
				}
				finalFolderPath.append(folderSubNameStr);
				finalFolderPath.append(File.separator);
			}
		}
		if ((isStringDirectoryPath) && (!isNetworkPath)) {
			String finalFolderPathStr = EphesoftStringUtil.concatenate(new String[] {finalFolderPath.toString()});
			String dropFileDestination = EphesoftStringUtil.concatenate(new String[] {
					new File(finalFolderPathStr).getAbsolutePath().toString(), File.separator});
			if (!finalFolderPathStr.equals(dropFileDestination)) {
				LOGGER.error("Invalid path entered in multipage file export folder text box.");
				isValidPathEntered = false;
			}
		}
		if (isValidPathEntered) {
			if (!isStringDirectoryPath) {
				fileNameOrFolderPathStr = finalFolderPath.toString().substring(0, finalFolderPath.length() - 1);
			} else {
				fileNameOrFolderPathStr = finalFolderPath.toString();
			}
		}
		return fileNameOrFolderPathStr;
	}
	/**
	 * Returns dynamic value for a string in the file name or folder path
	 *
	 * @param folderPathSegment
	 * @param document
	 * @param batchInstanceID
	 * @param documentLevelFields
	 * @return
	 */
	private String returnDynamicValues(String folderPathSegment, final Document document, final String batchInstanceID,
			final List<String> documentLevelFields) {
		String folderPathSegemntValue = EMPTY_STRING;
		String identifier = folderPathSegment.substring(1, folderPathSegment.length());
		if (IExportCommonConstants.BATCH_INSTANCE_IDENTIFIER.equals(identifier)) {
			folderPathSegemntValue = batchInstanceID;
		} else if (IExportCommonConstants.BATCH_CLASS_IDENTIFIER.equals(identifier)) {
			if (!EphesoftStringUtil.isNullOrEmpty(batchInstanceID)) {
				folderPathSegemntValue = batchSchemaService.getBatch(batchInstanceID).getBatchClassIdentifier();
			}
		} else if (IExportCommonConstants.DOCUMENT_TYPE.equals(identifier)) {
			folderPathSegemntValue = document.getType();
		} else if (IExportCommonConstants.DOCUMENT_ID.equals(identifier)) {
			folderPathSegemntValue = document.getIdentifier();
		} else if (IExportCommonConstants.SERVER_NAME.equals(identifier)) {
			BatchInstance batchInstance = batchInstanceDao.getBatchInstancesForIdentifier(batchInstanceID);
			String executingServerDetails = batchInstance.getExecutingServer();
			folderPathSegemntValue = executingServerDetails.split(IExportCommonConstants.COLON)[0];
		} else if (identifier.contains(IExportCommonConstants.DATE)) {
			String dateFormat = validateDateTimeFormat(identifier, IExportCommonConstants.DATE);
			folderPathSegemntValue = new SimpleDateFormat(dateFormat).format(new Date());
		} else if (identifier.contains(IExportCommonConstants.TIME)) {
			String timeFormat = validateDateTimeFormat(identifier, IExportCommonConstants.TIME);
			folderPathSegemntValue = new SimpleDateFormat(timeFormat).format(new Date());
		} else if (identifier.contains(IExportCommonConstants.BATCH_FIELD_TYPE)) {
			folderPathSegemntValue = returnBatchClassFieldValue(batchInstanceID, identifier);
		} else {
			folderPathSegemntValue = returnDocumentLevelFieldValue(identifier, documentLevelFields, document);
		}
		return folderPathSegemntValue;
	}

	/**
	 * validates date time format 
	 * 
	 * @param identifier
	 * @param identifierType
	 * @return
	 */
	private String validateDateTimeFormat(final String identifier, final String identifierType) {
		String dateTimeFormat = EMPTY_STRING;
		try {
			dateTimeFormat = (String) identifier.subSequence(5, identifier.length() - 1);
		} catch (java.lang.StringIndexOutOfBoundsException exception) {
			if (identifierType.equalsIgnoreCase(IExportCommonConstants.DATE)) {
				dateTimeFormat = IExportCommonConstants.STATIC_DATE_FORMAT;
			} else {
				dateTimeFormat = IExportCommonConstants.STATIC_TIME_FORMAT;
			}
		}
		if (!EphesoftStringUtil.isNullOrEmpty(dateTimeFormat)) {
			dateTimeFormat = dateTimeFormat.trim();
		}
		return dateTimeFormat;
	}

	/**
	 * obtain effective file name from the given name
	 * @param fileName
	 * @return
	 */
	private String obtainEffectiveFileName(String fileName) {
		String effectiveFileName = EMPTY_STRING;
		try {
			String number = fileName.replaceAll(IExportCommonConstants.REGEX1_FOR_OBTAINING_LAST_NUMBER_FROM_STRING,
					IExportCommonConstants.REGEX_FIRST_NUMBER);
			int next = Integer.parseInt(number);
			next++;
			effectiveFileName = fileName.replaceAll(IExportCommonConstants.REGEX2_FOR_OBTAINING_LAST_NUMBER_FROM_STRING,
					IExportCommonConstants.REGEX_FIRST_NUMBER + next + IExportCommonConstants.REGEX_SECOND_NUMBER);
		} catch (NumberFormatException nmberFormatException) {
			LOGGER.error("Error increasing count ");
		}
		return effectiveFileName;
	}
	
	/**
	 * returns a list of all the reserved keywords used in the copy batch xml plugin export file and folder name dynamic configurations
	 * 
	 * @return
	 */
	private List<String> getReservedList()
	{
		List<String> reservedKeyList = new ArrayList<String>();
		reservedKeyList.add(IExportCommonConstants.BATCH_INSTANCE_IDENTIFIER);
		reservedKeyList.add(IExportCommonConstants.BATCH_CLASS_IDENTIFIER);
		reservedKeyList.add(IExportCommonConstants.DOCUMENT_TYPE);
		reservedKeyList.add(IExportCommonConstants.SERVER_NAME);
		reservedKeyList.add(IExportCommonConstants.DATE);
		reservedKeyList.add(IExportCommonConstants.TIME);
		reservedKeyList.add(IExportCommonConstants.DOCUMENT_ID);
		reservedKeyList.add(IExportCommonConstants.EPHESOFT_BATCH_ID);
		reservedKeyList.add(IExportCommonConstants.EPHESOFT_DOCUMENT_ID);
		return reservedKeyList;
	}
	
	/**
	 * obtain batch class field value from the given identifier based on the name of the batch class field variable input by the user
	 * 
	 * @param batchInstanceID
	 * @param identifier
	 * @return
	 */
	private String returnBatchClassFieldValue(final String batchInstanceID, final String identifier)
	{
		String folderPathSegemntValue = EMPTY_STRING;
		String batchFieldName = identifier
				.replaceAll(IExportCommonConstants.BATCH_FIELD_TYPE, IExportCommonConstants.EMPTY_STRING).replaceAll(
						IExportCommonConstants.COLON, IExportCommonConstants.EMPTY_STRING);
		batchFieldName = batchFieldName.trim();
		try {
			List<BatchLevelField> batchClassFieldList = batchSchemaService.getBatch(batchInstanceID).getBatchLevelFields()
					.getBatchLevelField();
			if (null != batchClassFieldList && !batchClassFieldList.isEmpty()) {
				for (BatchLevelField batchClassField : batchClassFieldList) {
					if (batchClassField.getName().equals(batchFieldName)) {
						folderPathSegemntValue = batchClassField.getValue();
						break;
					}
				}
			}
		} catch (NullPointerException nullPointerException) {
			LOGGER.error("No batch class field defined for the batch");
		}
		
		return folderPathSegemntValue; 
	}
	
	/**
	 * returns document level field values for the given document according to the name input by the user
	 * 
	 * @param identifier
	 * @param documentLevelFields
	 * @param document
	 * @return
	 */
	private String returnDocumentLevelFieldValue(final String identifier,final List<String> documentLevelFields, final Document document)
	{
		String folderPathSegemntValue = EMPTY_STRING;
		try {
			int indexOfDocumentLevelField = documentLevelFields.indexOf(identifier);
			if (indexOfDocumentLevelField > -1) {
				String documentLevelFieldValue = document.getDocumentLevelFields().getDocumentLevelField()
						.get(indexOfDocumentLevelField).getValue();
				folderPathSegemntValue = documentLevelFieldValue;
			}
		} catch (NullPointerException nullPointerException) {
			LOGGER.error("Document level field value not found");
		}
		return folderPathSegemntValue;
	}

}
