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

package com.ephesoft.dcma.imp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

import org.apache.commons.lang.SerializationUtils;
import org.artofsolving.jodconverter.office.ServiceManagerFailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.encryption.service.EncryptionKeyService;
import com.ephesoft.dcma.batch.encryption.service.XMLSignature;
import com.ephesoft.dcma.batch.exception.XMLSignatureException;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Batch.BatchLevelFields;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.BatchLevelField;
import com.ephesoft.dcma.batch.schema.BatchStatus;
import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.ObjectFactory;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.OpenOfficeFileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.service.FileFormatConvertor;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassField;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.imagemagick.impl.ITextPDFCreator;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.openoffice.OpenOfficeTask;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.OSUtil;
import com.ephesoft.dcma.util.PDFUtil;
import com.ephesoft.dcma.util.TIFFUtil;
import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;
import com.ephesoft.dcma.util.exception.KeyGenerationException;
import com.ephesoft.dcma.util.exception.KeyNotFoundException;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * Provides functionality of moving a particular folder to its destination folder and starts first level of processing by breaking
 * multipage tiff and PDF files . If the move is successful the original folder is deleted.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jun 20, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Component
public final class FolderImporter implements ICommonConstants {

	/**
	 * Constant for tiff size threshold that is 15 MB.
	 */
	private static final float TIFF_SIZE_THRESHOLD = 15.0f;

	private static final boolean IS_WINDOWS = OSUtil.isWindows();

	private static final String DOT_DELIMITER = ".";

	private static final String SEMICOLON_DELIMITER = ";";

	private static final String SER_EXTENSION = SEMICOLON_DELIMITER + FileType.SER.getExtension();

	/**
	 * String constant for import batch folder plugin.
	 */
	private static final String IMPORT_BATCH_FOLDER_PLUGIN = "IMPORT_BATCH_FOLDER";

	private static final String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();
	private static final String BCF_SER_FILE_NAME = "BCF_ASSO";
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(FolderImporter.class);

	@Autowired
	private BatchSchemaService batchSchemaService;

	@Autowired
	private ImageProcessService imageProcessService;
	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * File name ignore char list with semi colon.
	 */
	private String folderIgnoreCharList;

	/**
	 * Folder name replace char.
	 */
	private String ignoreReplaceChar;

	/**
	 * Reference of invalidCharList.
	 */
	private String invalidCharList;

	@Autowired
	private EncryptionKeyService encryptionService;

	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService batchPluginPropertiesService;

	@Autowired
	BatchClassService batchClassService;

	@Autowired
	private XMLSignature xmlSignature;

	/**
	 * OpenOffice converter service object.
	 */
	@Autowired
	@Qualifier("openOfficeConvertor")
	private FileFormatConvertor openOfficeConvertor;

	/**
	 * OpenOfficeTask service object to start and stop open office process.
	 */
	@Autowired
	private OpenOfficeTask openOfficeTask;

	/**
	 * List to store all the valid extensions specified by the user in folder-monitor properties file.
	 */
	private List<String> validExtentionList = null;

	/**
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(BatchSchemaService batchSchemaService) {
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
	public void setPluginPropertiesService(PluginPropertiesService pluginPropertiesService) {
		this.pluginPropertiesService = pluginPropertiesService;
	}

	/**
	 * @return the folderIgnoreCharList
	 */
	public String getFolderIgnoreCharList() {
		return folderIgnoreCharList;
	}

	/**
	 * @param folderIgnoreCharList the folderIgnoreCharList to set
	 */
	public void setFolderIgnoreCharList(String folderIgnoreCharList) {
		this.folderIgnoreCharList = folderIgnoreCharList;
	}

	/**
	 * @return the ignoreReplaceChar
	 */
	public String getIgnoreReplaceChar() {
		return ignoreReplaceChar;
	}

	/**
	 * @param ignoreReplaceChar the ignoreReplaceChar to set
	 */
	public void setIgnoreReplaceChar(String ignoreReplaceChar) {
		this.ignoreReplaceChar = ignoreReplaceChar;
	}

	/**
	 * invalidCharList.
	 * 
	 * @return {@link String} invalidCharList.
	 */
	public final String getInvalidCharList() {
		return invalidCharList;
	}

	/**
	 * invalidCharList.
	 * 
	 * @param invalidCharList {@link String}
	 */
	public final void setInvalidCharList(final String invalidCharList) {
		this.invalidCharList = invalidCharList;
	}

	/**
	 * This constructor takes the moveToFolder as an argument since the move to folder is going to be predefined we are storing it as a
	 * instance variable so that while calling copyAndMove method we need to specify the full path of the folder to be moved.
	 * 
	 * @throws DCMAApplicationException
	 */

	public FolderImporter() throws DCMAApplicationException {
		super();
	}

	/**
	 * This method moves the given folder under the moveToFolder (specified in the constructor)
	 * 
	 * @param batchInstance
	 * 
	 * @param sFolderToBeMoved
	 * @param batchInstanceIdentifier full path of the folder which has to be moved
	 * @return returns true if the folder move was sucsessful.False otherwise.
	 * @throws DCMAApplicationException
	 */
	public boolean copyAndMove(BatchInstance batchInstance, final String sFolderToBeMoved, final String batchInstanceIdentifier)
			throws DCMAException, DCMAApplicationException {
		String sMoveToFolder;
		File fFolderToBeMoved;
		File fMoveToFolder;
		boolean isCopySuccesful = false;
		fFolderToBeMoved = new File(sFolderToBeMoved);

		if (null == this.getInvalidCharList()) {
			throw new DCMAApplicationException("Unable to initialize in valid character list from properties file.");
		}

		String[] invalidCharList = this.getInvalidCharList().split(SEMICOLON_DELIMITER);
		// Initialize properties
		LOGGER.info("Initializing properties...");
		String validExt = pluginPropertiesService.getPropertyValue(batchInstance.getIdentifier(), IMPORT_BATCH_FOLDER_PLUGIN,
				FolderImporterProperties.FOLDER_IMPORTER_VALID_EXTNS);
		LOGGER.info("Properties Initialized Successfully");
		// If folder name contains invalid character throw batch to error.
		for (String inValidChar : invalidCharList) {
			if (inValidChar != null && !inValidChar.trim().isEmpty() && sFolderToBeMoved.contains(inValidChar)) {
				throw new DCMAApplicationException("Invalid characters present in folder name. Charater is " + inValidChar);
			}
		}

		// ADD extension to the serialized file in case of Batch class field association
		if (null != validExt) {
			validExt = validExt + SER_EXTENSION;
		}

		String[] validExtensions = validExt.split(SEMICOLON_DELIMITER);
		if (validExtensions == null || validExtensions.length == 0) {
			throw new DCMABusinessException("Could not find validExtensions properties in the property file");
		}

		if (!isFolderValid(sFolderToBeMoved, validExtensions, invalidCharList)) {
			throw new DCMABusinessException("Folder Invalid Folder name = " + sFolderToBeMoved);
		}
		sMoveToFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier;

		File folderToBeMovedTo = new File(sMoveToFolder);
		if (folderToBeMovedTo.exists()) {
			FileUtils.deleteContentsOnly(sMoveToFolder);
		} else {
			boolean newDirCreationSuccess = new File(sMoveToFolder).mkdir();
			if (!newDirCreationSuccess) {
				throw new DCMABusinessException("Unable To Create directory" + newDirCreationSuccess);

			}
		}
		fMoveToFolder = new File(sMoveToFolder);
		try {
			copyDirectoryWithContents(fFolderToBeMoved, fMoveToFolder);
			PrivateKeyEncryptionAlgorithm encryptionAlgorithm = batchInstance.getEncryptionAlgorithm();
			if (encryptionAlgorithm != null) {
				encryptionService.generateBatchInstanceKey(batchInstanceIdentifier, batchInstance.getBatchClass().getIdentifier(),
						encryptionAlgorithm);
			}
			isCopySuccesful = true;
		} catch (IOException e) {
			isCopySuccesful = false;
			throw new DCMAApplicationException("Unable to Copy Directory", e);
		} catch (final KeyNotFoundException keyNotFoundException) {
			throw new DCMAApplicationException("The key for the instance could not be genearated", keyNotFoundException);
		} catch (final KeyGenerationException keyGenerationException) {
			throw new DCMAApplicationException("The key for the instance could not be generated", keyGenerationException);
		}
		if (isCopySuccesful) {
			renameFiles(batchInstance, fMoveToFolder, batchInstanceIdentifier);
		}

		return isCopySuccesful;
	}

	/**
	 * Creates backup of the unc sub folder if it does not exists. If the backup already exist it deletes all the files from the unc
	 * sub folder and restore the backup for batch restart purpose.
	 * 
	 * @param sFolderToBeMoved {@link String} UNC Sub folder path.
	 * @param backUpFolderName {@link String} Backup folder name.
	 */
	private String createUNCFolderBackup(final String sFolderToBeMoved, final String backUpFolderName) throws DCMAApplicationException {
		String backUpFolderPath = null;

		if (sFolderToBeMoved != null) {
			File folderBackup = null;

			// Removed unused code.
			backUpFolderPath = EphesoftStringUtil.concatenate(sFolderToBeMoved, File.separator, backUpFolderName);
			folderBackup = new File(backUpFolderPath);

			if (folderBackup != null && !folderBackup.exists()) {
				final File[] filesToCopy = new File(sFolderToBeMoved).listFiles();
				if (filesToCopy != null && (filesToCopy.length != 0)) {
					folderBackup.mkdir();
					for (final File srcFile : filesToCopy) {
						if (srcFile != null && srcFile.exists()) {
							final File destFile = new File(EphesoftStringUtil.concatenate(folderBackup.getAbsolutePath(),
									File.separator, srcFile.getName()));
							try {
								if (srcFile.isFile()) {
									FileUtils.copyFile(srcFile, destFile);
								}
							} catch (final FileNotFoundException fileNotFoundException) {
								LOGGER.error(EphesoftStringUtil.concatenate(
										"Exception while copying file from source to destination folder",
										fileNotFoundException.getMessage()));
							} catch (final IOException ioException) {
								LOGGER.error(EphesoftStringUtil.concatenate("Exception while reading or writing file",
										ioException.getMessage()));
							} catch (final Exception exception) {
								LOGGER.error(EphesoftStringUtil.concatenate("Exception while reading or writing file",
										exception.getMessage()));
							}
						}
					}
				}
			} else if (folderBackup.isDirectory() && folderBackup.exists()) {
				FileUtils.deleteContentsOnly(sFolderToBeMoved);
				try {
					FileUtils.copyDirectoryWithContents(folderBackup.getAbsolutePath(), sFolderToBeMoved);
				} catch (final IOException ioException) {
					throw new DCMAApplicationException(EphesoftStringUtil.concatenate(
							"Error in copying file from backup to unc folder. Throwing batch into error", ioException.getMessage()),
							ioException);
				}
			}
		}
		return backUpFolderPath;
	}

	/**
	 * This method renames all the moved files.
	 * 
	 * @param fMoveToFolder
	 * @param batchInstanceIdentifier
	 * @throws DCMAApplicationException
	 */
	private void renameFiles(final BatchInstance batchInstance, File fMoveToFolder, String batchInstanceIdentifier)
			throws DCMAApplicationException {
		ObjectFactory objectFactory = new ObjectFactory();
		String[] files = fMoveToFolder.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
				.getExtensionWithDot(), FileType.PDF.getExtensionWithDot()));
		Batch batchXmlObj = generateBatchObject(batchInstance, batchInstanceIdentifier);
		Pages pages = new Pages();
		List<Page> listOfPages = pages.getPage();
		Arrays.sort(files);

		LOGGER.info("Starting rename of folder <<" + fMoveToFolder + ">>" + " total number of files=" + files.length);
		int pageId = 0;
		for (String fileName : files) {
			File movedFile = new File(fMoveToFolder.getAbsolutePath() + File.separator + fileName);
			FileNameFormatter formatter = null;
			try {
				formatter = new FileNameFormatter();
			} catch (Exception e) {
				throw new DCMAApplicationException("Could not instantiate FileNameFormatter", e);
			}
			String extension = getFileExtension(fileName);
			String newFileName = "";
			try {
				newFileName = formatter.getNewFileName(batchInstanceIdentifier, fileName, Integer.toString(pageId), extension);
			} catch (Exception e) {
				throw new DCMAApplicationException("Problem in obtaining the new file name", e);
			}
			File renamedFile = new File(fMoveToFolder.getAbsolutePath() + File.separator + newFileName);
			Page pageType = objectFactory.createPage();
			pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + pageId);
			pageType.setNewFileName(newFileName);
			pageType.setOldFileName(fileName);
			pageType.setDirection(Direction.NORTH);
			pageType.setIsRotated(false);
			listOfPages.add(pageType);

			movedFile.renameTo(renamedFile);
			pageId++;
		}

		Documents documents = new Documents();

		List<Document> listOfDocuments = documents.getDocument();
		Document document = objectFactory.createDocument();
		document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + IFolderImporterConstants.ZERO);
		document.setConfidence(Float.valueOf(IFolderImporterConstants.ZERO));
		document.setType(EphesoftProperty.UNKNOWN.getProperty());
		document.setDescription(EphesoftProperty.UNKNOWN.getProperty());
		document.setPages(pages);
		document.setErrorMessage("");
		document.setDocumentDisplayInfo(BatchConstants.EMPTY);
		listOfDocuments.add(document);

		batchXmlObj.setDocuments(documents);
		addBatchClassFieldToBatch(batchXmlObj, batchInstance, objectFactory);
		batchSchemaService.updateBatch(batchXmlObj, true);

	}

	/**
	 * 
	 * @param batchXmlObj
	 * @param batchInstance
	 * @param objectFactory
	 */
	private void addBatchClassFieldToBatch(Batch batchXmlObj, BatchInstance batchInstance, ObjectFactory objectFactory) {

		String localFolder = batchInstance.getLocalFolder();
		String batchInstanceIdentifier = batchInstance.getIdentifier();
		String sFinalFolder = localFolder + File.separator + batchInstanceIdentifier;
		List<BatchClassField> batchClassFieldList = readBatchClassFieldSerializeFile(sFinalFolder);
		if (batchClassFieldList != null && !batchClassFieldList.isEmpty()) {
			BatchLevelFields batchLevelFields = new BatchLevelFields();
			List<BatchLevelField> listOfBatchLevelFields = batchLevelFields.getBatchLevelField();
			for (BatchClassField batchClassField : batchClassFieldList) {

				BatchLevelField batchlLevelField = objectFactory.createBatchLevelField();
				batchlLevelField.setName(batchClassField.getName());
				batchlLevelField.setValue(batchClassField.getValue());
				batchlLevelField.setType(batchClassField.getDataType().name());
				listOfBatchLevelFields.add(batchlLevelField);
			}
			batchXmlObj.setBatchLevelFields(batchLevelFields);
		}
	}

	/**
	 * This method returns the extension of the file.
	 * 
	 * @param fileName
	 * @return extension of file name.
	 */
	private String getFileExtension(String fileName) {
		String extension = "";
		String[] strArr = fileName.split("\\.");
		if (strArr.length == 2) {
			extension = strArr[1];
		}
		if (strArr.length > 2) {
			extension = strArr[strArr.length - 1];
		}
		return DOT_DELIMITER + extension;
	}

	/**
	 * This method copies a folder with all its contents from the source path to the destination path if the destination path does not
	 * exist it is created first.
	 * 
	 * @param batchInstance
	 * 
	 * @param srcPath The source folder whose contents are to be moved
	 * @param dstPath The destination folder to which all the contents are to be moved.
	 * @throws IOException
	 */
	private void copyDirectoryWithContents(final File srcPath, final File dstPath) throws IOException {

		if (srcPath.isDirectory()) {
			if (!dstPath.exists()) {
				dstPath.mkdir();
			}

			String[] files = srcPath.list(new CustomFileFilter(false, FileType.PDF.getExtension(), FileType.TIF.getExtension(),
					FileType.TIFF.getExtension(), FileType.SER.getExtension()));
			if (null == files || files.length == IFolderImporterConstants.ZERO) {
				throw new DCMABusinessException("Source directory is empty" + srcPath);
			}

			Arrays.sort(files);
			LOGGER.info("Starting copy of folder <<" + srcPath + ">>" + " total number of files=" + files.length);
			for (String fileName : files) {
				LOGGER.info("\tcopying file " + fileName);
				copyDirectoryWithContents(new File(srcPath, fileName), new File(dstPath, fileName));
			}
			LOGGER.info("Files copied sucsessfully to folder <<" + dstPath + ">>" + " total number of files=" + files.length);

		} else {
			if (!srcPath.exists()) {
				LOGGER.error("File or directory does not exist.");
				throw new DCMABusinessException("Source file does not exist Path=" + srcPath);
			} else {
				InputStream inStream = new FileInputStream(srcPath);
				OutputStream out = new FileOutputStream(dstPath);
				// Transfer bytes from in to out
				byte[] buf = new byte[IFolderImporterConstants.KBYTE];
				int len;
				while ((len = inStream.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				if (inStream != null) {
					inStream.close();
				}
				if (out != null) {
					out.close();
				}
			}
		}

	}

	/**
	 * Validates Business rules for folder validity. A folder is valid if it contains files with valid extensions a folder is invalid
	 * if 1. It is empty. 2. It contains sub folders. 3. It contains files with extensions other than the valid extensions.
	 * 
	 * @param sFolderToBeMoved
	 * @return boolean
	 */
	private boolean isFolderValid(final String sFolderToBeMoved, final String[] validExtensions, final String[] invalidCharList) {
		boolean folderValid = true;
		LOGGER.info("Validating buisiness Rules for folder <<" + sFolderToBeMoved + ">>");

		File fFolderToBeMoved = new File(sFolderToBeMoved);
		String[] files = fFolderToBeMoved.list();
		Arrays.sort(files);

		// Do not Move if Folder Empty
		if (files.length == 0) {
			LOGGER.error("\tBuisiness Rule Violation --Empty Folder--  Folder" + fFolderToBeMoved + " will not be moved");
			folderValid = false;
		}

		for (String fileName : files) {
			// if any of file name contains invalid character move batch to error.
			for (String inValidChar : invalidCharList) {
				if (inValidChar != null && !inValidChar.trim().isEmpty() && fileName.contains(inValidChar)) {
					folderValid = false;
					LOGGER.error("In valid characters present in the file name. File name is " + fileName + ". Charater is "
							+ inValidChar);
					break;
				}
			}

			if (folderValid) {
				File indivisualFile = new File(fFolderToBeMoved, fileName);

				/*
				 * if (indivisualFile.isDirectory()) { LOGGER.info("\tBuisiness Rule Violation Folder --Contains " + "Subfolders -- " +
				 * fFolderToBeMoved + " will not be moved"); folderValid = false; break; }
				 */

				String nameOfFile = fileName;
				boolean invalidFileExtension = true;
				for (String validExt : validExtensions) {
					if (indivisualFile.isDirectory()
							|| nameOfFile.substring(nameOfFile.indexOf(DOT_DELIMITER.charAt(0)) + 1).equalsIgnoreCase(validExt)) {
						invalidFileExtension = false;
					}

				}
				if (invalidFileExtension) {
					LOGGER.info("\tBuisiness Rule Violation Folder --" + "File with Invalid Extensions (or is directory) -- "
							+ indivisualFile.getAbsolutePath() + " will not be moved");
				}

			}
		}

		LOGGER.info("Folder <<" + sFolderToBeMoved + ">> is valid");
		return folderValid;

	}

	/**
	 * Generates the batch.xml file.
	 * 
	 * @param batchInstance
	 * @param batchInstanceID
	 * @throws DCMAApplicationException
	 */
	public void generateBatchXML(final BatchInstance batchInstance, final String batchInstanceID) throws DCMAApplicationException {
		BatchClass batchClass = batchInstance.getBatchClass();
		String localFolder = batchInstance.getLocalFolder();
		String batchInstanceIdentifier = batchInstance.getIdentifier();
		String sFinalFolder = localFolder + File.separator + batchInstanceIdentifier;
		File fFinalFolder = new File(sFinalFolder);
		ObjectFactory objectFactory = new ObjectFactory();

		Batch objBatchXml = objectFactory.createBatch();
		objBatchXml.setBatchInstanceIdentifier(batchInstanceID);
		objBatchXml.setBatchClassIdentifier(batchClass.getIdentifier());
		objBatchXml.setBatchClassName(batchClass.getName());
		objBatchXml.setBatchClassDescription(batchClass.getDescription());
		objBatchXml.setBatchClassVersion(batchClass.getVersion());
		// Setting UNC sub folder path as UNC folder path in batch xml for ibml
		objBatchXml.setUNCFolderPath(batchInstance.getUncSubfolder());
		objBatchXml.setBatchName(batchInstance.getBatchName());
		objBatchXml.setBatchDescription(batchInstance.getBatchDescription());
		objBatchXml.setBatchLocalPath(batchInstance.getLocalFolder());
		objBatchXml.setBatchPriority(Integer.toString(batchClass.getPriority()));
		objBatchXml.setBatchStatus(BatchStatus.READY);
		// changes made for adding the batch creation date in batch xml.
		objBatchXml.setBatchCreationDate(batchInstance.getCreationDate().toString());
		String[] listOfFiles = fFinalFolder.list();
		Arrays.sort(listOfFiles);
		Pages pages = new Pages();
		List<Page> listOfPages = pages.getPage();
		int identifierValue = 0;
		for (String fileName : listOfFiles) {
			Page pageType = objectFactory.createPage();
			pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + identifierValue);
			pageType.setNewFileName(fileName);
			String[] strArr = fileName.split(batchInstanceIdentifier + "_");
			pageType.setOldFileName(strArr[1]);
			pageType.setDirection(Direction.NORTH);
			pageType.setIsRotated(false);
			listOfPages.add(pageType);
			identifierValue++;
		}

		Documents documents = new Documents();
		BatchLevelFields batchLevelFields = new BatchLevelFields();
		if (listOfFiles.length > IFolderImporterConstants.ZERO) {
			List<Document> listOfDocuments = documents.getDocument();
			Document document = objectFactory.createDocument();
			document.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + IFolderImporterConstants.ONE);
			document.setConfidence(Float.valueOf(IFolderImporterConstants.ZERO));
			document.setType(EphesoftProperty.UNKNOWN.getProperty());
			document.setDescription(EphesoftProperty.UNKNOWN.getProperty());
			document.setPages(pages);
			document.setErrorMessage("");
			document.setDocumentDisplayInfo(BatchConstants.EMPTY);
			listOfDocuments.add(document);

			ArrayList<BatchClassField> batchClassFieldList = readBatchClassFieldSerializeFile(sFinalFolder);
			List<BatchLevelField> listOfBatchLevelFields = batchLevelFields.getBatchLevelField();
			for (BatchClassField batchClassField : batchClassFieldList) {
				BatchLevelField batchlLevelField = objectFactory.createBatchLevelField();
				batchlLevelField.setName(batchClassField.getName());
				batchlLevelField.setValue(batchClassField.getValue());
				batchlLevelField.setType(batchClassField.getDataType().name());
				listOfBatchLevelFields.add(batchlLevelField);
			}
		}
		objBatchXml.setDocuments(documents);
		objBatchXml.setBatchLevelFields(batchLevelFields);

		batchSchemaService.updateBatch(objBatchXml);

	}

	private ArrayList<BatchClassField> readBatchClassFieldSerializeFile(String sFinalFolder) {
		FileInputStream fileInputStream = null;
		ArrayList<BatchClassField> batchClassFieldList = null;
		File serializedFile = null;
		try {
			String serializedFilePath = sFinalFolder + File.separator + BCF_SER_FILE_NAME + SERIALIZATION_EXT;
			serializedFile = new File(serializedFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			batchClassFieldList = (ArrayList<BatchClassField>) SerializationUtils.deserialize(fileInputStream);
			// updateFile(serializedFile, serializedFilePath);
		} catch (IOException e) {
			LOGGER.info("Error during reading the serialized file. ");
		} catch (Exception e) {
			LOGGER.error("Error during de-serializing the properties for Database Upgrade: ", e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null) {
					LOGGER.error("Problem closing stream for file :" + serializedFile.getName());
				}
			}
		}

		return batchClassFieldList;
	}

	/**
	 * Generates the batch.xml file.
	 * 
	 * @param batchInstance
	 * @param batchInstanceID
	 * @throws DCMAApplicationException
	 */
	public Batch generateBatchObject(final BatchInstance batchInstance, final String batchInstanceID) throws DCMAApplicationException {
		BatchClass batchClass = batchInstance.getBatchClass();

		ObjectFactory objectFactory = new ObjectFactory();

		Batch objBatchXml = objectFactory.createBatch();
		objBatchXml.setBatchInstanceIdentifier(batchInstanceID);
		objBatchXml.setBatchClassIdentifier(batchClass.getIdentifier());
		objBatchXml.setBatchClassName(batchClass.getName());
		objBatchXml.setBatchClassDescription(batchClass.getDescription());
		objBatchXml.setBatchName(batchInstance.getBatchName());
		objBatchXml.setBatchDescription(batchInstance.getBatchDescription());
		objBatchXml.setBatchClassVersion(batchClass.getVersion());
		// Setting UNC sub folder path as UNC folder path in batch xml for ibml
		objBatchXml.setUNCFolderPath(batchInstance.getUncSubfolder());
		objBatchXml.setBatchLocalPath(batchInstance.getLocalFolder());
		objBatchXml.setBatchPriority(Integer.toString(batchClass.getPriority()));
		objBatchXml.setBatchStatus(BatchStatus.READY);
		// changes made for adding the batch creation date in batch xml.
		objBatchXml.setBatchCreationDate(batchInstance.getCreationDate().toString());
		// changes made for adding the signature in batch xml.
		if (null != batchInstance.getEncryptionAlgorithm()) {
			try {
				objBatchXml.setSignature(new HexBinaryAdapter().unmarshal(xmlSignature.generateBatchSignature(batchInstanceID)));
			} catch (XMLSignatureException xMLSignatureException) {
				throw new DCMAApplicationException("Error ocurred while generating signature for batch.", xMLSignatureException);
			}
		}
		return objBatchXml;

	}

	/**
	 * Breaks multipage PDF and multipage Tiff into single page tiff files.
	 * 
	 * @param batchInstance {@link BatchInstance} Batch Instance to be process
	 * @param folderPath {@link String} unc sub folder path for batch instance
	 * @throws DCMAApplicationException If any error occurs while breaking multipage pdf or tiff file
	 */
	public void breakMultiPagePDFandTiff(final BatchInstance batchInstance, final String folderPath) throws DCMAApplicationException {
		final String batchInstanceId = batchInstance.getIdentifier();
		final String importMultiPage = pluginPropertiesService.getPropertyValue(batchInstanceId, IMPORT_MULTIPAGE_FILES_PLUGIN,
				FolderImporterProperties.FOLDER_IMPORTER_MULTI_PAGE_IMPORT);
		final File uncSubFolder = new File(folderPath);
		if (uncSubFolder.exists()) {
			if ((null != importMultiPage)
					&& (importMultiPage.equalsIgnoreCase(IFolderImporterConstants.TRUE) || importMultiPage
							.equalsIgnoreCase(IFolderImporterConstants.YES))) {
				final String pdfToTiffConverter = pluginPropertiesService.getPropertyValue(batchInstanceId,
						IMPORT_MULTIPAGE_FILES_PLUGIN, FolderImporterProperties.FOLDER_IMPORTER_PDF_TO_TIFF_CONVERTER);

				if (EphesoftStringUtil.isNullOrEmpty(pdfToTiffConverter)) {
					LOGGER.error("Unable to fetch proper value of PDF to Tiff conversion process property of import multipage plugin from database.");
				} else {
					// Temporary backup of pdf and tiff files which will be used to validate the count of converted tiffs.
					final String backUpFolderName = EphesoftStringUtil.concatenate(uncSubFolder.getName(),
							ICommonConstants.PDF_AND_TIFF_BACK_UP_FOLDER_NAME);
					final String backUpFolderPath = createUNCFolderBackup(folderPath, backUpFolderName);
					final BatchClass batchClass = batchInstance.getBatchClass();
					String compressionRatio = null;
					final File backUpFolder = new File(backUpFolderPath);
					if (null != backUpFolder && backUpFolder.exists()) {
						final int expectedTifFilesCount = getTiffPagesCount(backUpFolder);
						final String folderIgnoreCharList = getFolderIgnoreCharList();
						if (null == folderIgnoreCharList || getIgnoreReplaceChar() == null || folderIgnoreCharList.isEmpty()
								|| getIgnoreReplaceChar().isEmpty() || getIgnoreReplaceChar().length() > 1) {
							throw new DCMAApplicationException("Invalid property file configuration....");
						}
						final String fdIgList[] = folderIgnoreCharList.split(SEMICOLON_DELIMITER);
						final List<File> deleteFileList = new ArrayList<File>();
						BatchInstanceThread batchInstanceThread = new BatchInstanceThread(batchInstanceId);

						batchInstanceThread.setUsingGhostScript(true);
						this.convertLargeTiffIntoPdf(uncSubFolder);
						final String[] pdfFileList = uncSubFolder.list(new CustomFileFilter(false, FileType.PDF.getExtension()));
						processInputPDFFiles(folderPath, batchInstance, pdfFileList, fdIgList, batchInstanceThread,
								compressionRatio);

						batchInstanceThread = new BatchInstanceThread(batchInstanceId);
						processInputTiffFiles(folderPath, batchInstance, fdIgList, batchInstanceThread, uncSubFolder);

						// Deleting the intermediate files.
						deleteTempFiles(deleteFileList);

						// Counting the number of files converted in the batch folder.
						final int actualTiffFilesCount = uncSubFolder.list(new CustomFileFilter(false, FileType.TIF.getExtension(),
								FileType.TIFF.getExtension())).length;
						LOGGER.info(EphesoftStringUtil.concatenate("expectedTifFilesCount :: ", expectedTifFilesCount));
						LOGGER.info(EphesoftStringUtil.concatenate("Actual tiff files count :: ", actualTiffFilesCount));
						if (actualTiffFilesCount < expectedTifFilesCount) {
							LOGGER.error(EphesoftStringUtil.concatenate(
									"Converted Tiff files count not equal to the the TIFF pages count. actualTiffFilesCount :: ",
									actualTiffFilesCount, ", expectedTifFilesCount :: ", expectedTifFilesCount,
									" for batch instance ::", batchInstanceId));
							throw new DCMAApplicationException("Converted Tiff files count not equal to the the TIFF pages count.");
						}

						// Deletes the temporary backup of pdf and tiff files which was used to validate the count of converted tiffs.
						FileUtils.deleteDirectoryAndContents(backUpFolderPath);
					}

				}
			} else {
				final String[] pdfFileList = uncSubFolder.list(new CustomFileFilter(false, FileType.PDF.getExtension()));
				if (null != pdfFileList && pdfFileList.length > 0) {
					throw new DCMAApplicationException("Multi page import switch is OFF so unable to process PDF files.");
				} else {
					LOGGER.error("Unable to fetch proper value of import multipage file property of import multipage plugin from database.");
				}
			}
		} else {
			throw new DCMAApplicationException(EphesoftStringUtil.concatenate(
					"Invalid UNC sub folder path is specified for Batch instance : ", batchInstanceId));
		}
	}

	/**
	 * Converts tiff files present in batch folder into pdf files if tiff files have size greater than 10 MB.
	 * 
	 * @param uncSubFolder {@link File} reference of batch folder inside UNC folder of batch class.
	 * @throws DCMAApplicationException if any error occurs while conversion.
	 */
	private void convertLargeTiffIntoPdf(final File uncSubFolder) throws DCMAApplicationException {
		final String[] tiffFiles = uncSubFolder.list(new CustomFileFilter(false, FileType.TIFF.getExtension(), FileType.TIF
				.getExtensionWithDot()));
		if ((null != tiffFiles) && (tiffFiles.length > 0)) {
			final String uncSubFolderPath = uncSubFolder.getAbsolutePath();
			for (final String tiffFileName : tiffFiles) {
				if (!EphesoftStringUtil.isNullOrEmpty(tiffFileName)) {
					final File tiffFile = new File(EphesoftStringUtil.concatenate(uncSubFolderPath, File.separator, tiffFileName));
					if (tiffFile.isFile() && tiffFile.exists()) {
						final float tiffFileSize = tiffFile.length() / (float) (1024 * 1024);
						if (tiffFileSize > TIFF_SIZE_THRESHOLD) {
							ITextPDFCreator.convertTiffIntoPdf(tiffFile);
							tiffFile.delete();
						}
					}
				}
			}
		}
	}

	private void deleteTempFiles(final List<File> deleteFileList) {
		if (deleteFileList != null) {
			LOGGER.info("Cleaning up the old pdf and tiff files.");
			for (File file : deleteFileList) {
				boolean isDeleted = file.delete();
				LOGGER.debug(" File " + file.getAbsolutePath() + " deleted " + isDeleted);
			}
		}
	}

	private void processInputTiffFiles(final String folderPath, final BatchClass batchClass, final String[] fdIgList,
			final BatchInstanceThread threadList, final File folder) throws DCMAApplicationException {
		/*
		 * if (deleteFileList == null) { deleteFileList = new ArrayList<File>(); }
		 */
		final String[] tiffFolderList = folder.list(new CustomFileFilter(false, FileType.TIF.getExtension(), FileType.TIFF
				.getExtension()));
		boolean isFound = false;
		if (tiffFolderList != null) {
			for (String fileName : tiffFolderList) {
				if (fileName == null || fileName.isEmpty()) {
					continue;
				}
				try {
					isFound = false;
					LOGGER.info(EphesoftStringUtil.concatenate("File Name = ", fileName));
					final File fileOriginal = new File(EphesoftStringUtil.concatenate(folderPath, File.separator, fileName));
					int noOfPages = TIFFUtil.getTIFFPageCount(fileOriginal.getPath());
					if (noOfPages != 1) {
						File fileNew = null;
						for (final String nameStr : fdIgList) {
							if (fileName.contains(nameStr)) {
								isFound = true;
								fileName = fileName.replace(nameStr, getIgnoreReplaceChar());
							}
						}

						if (isFound) {
							fileNew = new File(EphesoftStringUtil.concatenate(folderPath, File.separator, fileName));
							fileOriginal.renameTo(fileNew);
							LOGGER.info(EphesoftStringUtil.concatenate("Converting multi page tiff file : ", fileNew.getAbsolutePath()));
							imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClass, fileNew, null, threadList, false, true);
						} else {
							LOGGER.info(EphesoftStringUtil.concatenate("Converting multi page tiff file : ",
									fileOriginal.getAbsolutePath()));
							imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClass, fileOriginal, null, threadList, false,
									true);
						}
					}
				} catch (final Exception exception) {
					LOGGER.error("Error in converting multi page tiff file to single page tiff files", exception);
					throw new DCMAApplicationException(EphesoftStringUtil.concatenate("Error in breaking image to single pages ",
							exception.getMessage()), exception);
				}
			}
			try {
				LOGGER.info("Executing conversion of multi page tiff file using thread pool");
				threadList.execute();
				LOGGER.info("Completed conversion of multi page tiff file using thread pool");
			} catch (final DCMAApplicationException exception) {
				LOGGER.error(exception.getMessage(), exception);
				throw new DCMAApplicationException(exception.getMessage(), exception);
			}
		}
	}

	private void processInputTiffFiles(final String folderPath, final BatchInstance batchInstance, final String[] fdIgList,
			final BatchInstanceThread threadList, final File folder) throws DCMAApplicationException {
		/*
		 * if (deleteFileList == null) { deleteFileList = new ArrayList<File>(); }
		 */
		final String[] tiffFolderList = folder.list(new CustomFileFilter(false, FileType.TIF.getExtension(), FileType.TIFF
				.getExtension()));
		boolean isFound = false;
		if (tiffFolderList != null) {
			for (String fileName : tiffFolderList) {
				if (fileName == null || fileName.isEmpty()) {
					continue;
				}
				try {
					isFound = false;
					LOGGER.info(EphesoftStringUtil.concatenate("File Name = ", fileName));
					final File fileOriginal = new File(EphesoftStringUtil.concatenate(folderPath, File.separator, fileName));
					int noOfPages = TIFFUtil.getTIFFPageCount(fileOriginal.getPath());
					if (noOfPages != 1) {
						File fileNew = null;
						for (final String nameStr : fdIgList) {
							if (fileName.contains(nameStr)) {
								isFound = true;
								fileName = fileName.replace(nameStr, getIgnoreReplaceChar());
							}
						}

						if (isFound) {
							fileNew = new File(EphesoftStringUtil.concatenate(folderPath, File.separator, fileName));
							fileOriginal.renameTo(fileNew);
							LOGGER.info(EphesoftStringUtil.concatenate("Converting multi page tiff file : ", fileNew.getAbsolutePath()));
							imageProcessService.convertPdfOrMultiPageTiffToTiff(batchInstance, fileNew, null, threadList, false, true);
						} else {
							LOGGER.info(EphesoftStringUtil.concatenate("Converting multi page tiff file : ",
									fileOriginal.getAbsolutePath()));
							imageProcessService.convertPdfOrMultiPageTiffToTiff(batchInstance, fileOriginal, null, threadList, false,
									true);
						}
					}
				} catch (final Exception exception) {
					LOGGER.error("Error in converting multi page tiff file to single page tiff files", exception);
					throw new DCMAApplicationException(EphesoftStringUtil.concatenate("Error in breaking image to single pages ",
							exception.getMessage()), exception);
				}
			}
			try {
				LOGGER.info("Executing conversion of multi page tiff file using thread pool");
				threadList.execute();
				LOGGER.info("Completed conversion of multi page tiff file using thread pool");
			} catch (final DCMAApplicationException exception) {
				LOGGER.error(exception.getMessage(), exception);
				throw new DCMAApplicationException(exception.getMessage(), exception);
			}
		}
	}

	private void processInputPDFFiles(final String folderPath, final BatchClass batchClass, final String[] pdfFileList,
			final String[] fdIgList, final BatchInstanceThread batchInstanceThread, final String compressionRatio)
			throws DCMAApplicationException {
		boolean isFound = false;
		if (null != pdfFileList && pdfFileList.length > 0) {
			for (String fileName : pdfFileList) {

				if (fileName == null || fileName.isEmpty()) {
					continue;
				}

				try {
					isFound = false;
					final File fileOriginal = new File(EphesoftStringUtil.concatenate(folderPath, File.separator, fileName));
					LOGGER.info(EphesoftStringUtil.concatenate("File Name = ", fileName));
					File fileNew = null;
					for (final String nameStr : fdIgList) {
						if (fileName.contains(nameStr)) {
							isFound = true;
							fileName = fileName.replace(nameStr, getIgnoreReplaceChar());
						}
					}

					if (isFound) {
						fileNew = new File(EphesoftStringUtil.concatenate(folderPath, File.separator, fileName));
						fileOriginal.renameTo(fileNew);
						LOGGER.info(EphesoftStringUtil.concatenate("Converting multi page pdf file : ", fileNew.getAbsolutePath()));
						if (compressionRatio == null) {
							imageProcessService.convertPdfToSinglePageTiffs(batchClass, fileNew, batchInstanceThread, false);
						}
					} else {
						LOGGER.info(EphesoftStringUtil.concatenate("Converting multi page pdf file : ", fileOriginal.getAbsolutePath()));
						if (compressionRatio == null) {
							imageProcessService.convertPdfToSinglePageTiffs(batchClass, fileOriginal, batchInstanceThread, false);
						}
					}
				} catch (final Exception exception) {
					LOGGER.error("Error in converting multi page pdf file to mutli page tiff file", exception);
					throw new DCMAApplicationException(EphesoftStringUtil.concatenate(
							"Error in converting pdf file to multi page tiff file", exception.getMessage()), exception);
				}
			}
			try {
				LOGGER.info("Executing conversion of multi page pdf file using thread pool");
				batchInstanceThread.execute();
				LOGGER.info("Completed conversion of multi page pdf file using thread pool");
			} catch (final DCMAApplicationException dcmaAppException) {
				LOGGER.error(dcmaAppException.getMessage(), dcmaAppException);
				throw new DCMAApplicationException(dcmaAppException.getMessage(), dcmaAppException);
			}
		}
	}

	private void processInputPDFFiles(final String folderPath, final BatchInstance batchInstance, final String[] pdfFileList,
			final String[] fdIgList, final BatchInstanceThread batchInstanceThread, final String compressionRatio)
			throws DCMAApplicationException {
		boolean isFound = false;
		if (null != pdfFileList && pdfFileList.length > 0) {
			for (String fileName : pdfFileList) {

				if (fileName == null || fileName.isEmpty()) {
					continue;
				}

				try {
					isFound = false;
					final File fileOriginal = new File(EphesoftStringUtil.concatenate(folderPath, File.separator, fileName));
					LOGGER.info(EphesoftStringUtil.concatenate("File Name = ", fileName));
					File fileNew = null;
					for (final String nameStr : fdIgList) {
						if (fileName.contains(nameStr)) {
							isFound = true;
							fileName = fileName.replace(nameStr, getIgnoreReplaceChar());
						}
					}

					if (isFound) {
						fileNew = new File(EphesoftStringUtil.concatenate(folderPath, File.separator, fileName));
						fileOriginal.renameTo(fileNew);
						LOGGER.info(EphesoftStringUtil.concatenate("Converting multi page pdf file : ", fileNew.getAbsolutePath()));
						if (compressionRatio == null) {
							imageProcessService.convertPdfToSinglePageTiffs(batchInstance, fileNew, batchInstanceThread, false);
						}
					} else {
						LOGGER.info(EphesoftStringUtil.concatenate("Converting multi page pdf file : ", fileOriginal.getAbsolutePath()));
						if (compressionRatio == null) {
							imageProcessService.convertPdfToSinglePageTiffs(batchInstance, fileOriginal, batchInstanceThread, false);
						}
					}
				} catch (final Exception exception) {
					LOGGER.error("Error in converting multi page pdf file to mutli page tiff file", exception);
					throw new DCMAApplicationException(EphesoftStringUtil.concatenate(
							"Error in converting pdf file to multi page tiff file", exception.getMessage()), exception);
				}
			}
			try {
				LOGGER.info("Executing conversion of multi page pdf file using thread pool");
				batchInstanceThread.execute();
				LOGGER.info("Completed conversion of multi page pdf file using thread pool");
			} catch (final DCMAApplicationException dcmaAppException) {
				LOGGER.error(dcmaAppException.getMessage(), dcmaAppException);
				throw new DCMAApplicationException(dcmaAppException.getMessage(), dcmaAppException);
			}
		}
	}

	private int getTiffPagesCount(final File folder) {
		LOGGER.info("Inside getTiffFilesCount ....");
		int tiffFileCount = 0;
		File[] pdfFileList = folder.listFiles(new CustomFileFilter(false, FileType.PDF.getExtension()));
		File[] tiffFilesList = folder
				.listFiles(new CustomFileFilter(false, FileType.TIF.getExtension(), FileType.TIFF.getExtension()));
		if (pdfFileList != null) {
			for (File file : pdfFileList) {
				tiffFileCount += PDFUtil.getPDFPageCount(file.getAbsolutePath());
			}
		}
		if (tiffFilesList != null) {
			for (File file : tiffFilesList) {
				tiffFileCount += TIFFUtil.getTIFFPageCount(file.getAbsolutePath());
			}
		}
		LOGGER.info("Exiting getTiffFilesCount ....");
		return tiffFileCount;
	}

	/**
	 * Converts the supported office file into pdf files for further processing. It creates a complete backup of the UNC folder first
	 * then converts the supported files into pdf and removes them from the unc subfolder.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param uncSubFolderPath {@link String}
	 * @throws DCMAApplicationException
	 */
	public void convertValidOfficeFiles(final BatchInstance batchInstance, final String uncSubFolderPath)
			throws DCMAApplicationException {
		if (batchInstance != null) {
			final File uncSubFolder = new File(uncSubFolderPath);
			if (uncSubFolder.exists()) {
				checkFilePathLength(batchInstance,
						EphesoftStringUtil.concatenate(uncSubFolderPath, File.separator, uncSubFolder.listFiles()[0].getName()));
				final String backUpFolderName = EphesoftStringUtil.concatenate(uncSubFolder.getName(),
						ICommonConstants.BACK_UP_FOLDER_NAME);
				// creates backup of unc folder, if it already exist it removes all the files from unc sub folder and restores the
				// backup.
				final String backUpAllFilesPath = createUNCFolderBackup(uncSubFolderPath, backUpFolderName);
				final File backupAllFilesFolder = new File(backUpAllFilesPath);
				final int expectedCount = backupAllFilesFolder.list().length;
				convertAndDeleteAllOfficeFiles(uncSubFolderPath);
				final int actualCount = getActualFileCount(uncSubFolder);
				if (actualCount != expectedCount) {
					throw new DCMAApplicationException(EphesoftStringUtil.concatenate(
							"Expected File count and Actual File Count doesn't match after conversion. Throwing ",
							batchInstance.getIdentifier(), " into ERROR."));
				}
			} else {
				throw new DCMAApplicationException(EphesoftStringUtil.concatenate(
						"Batch UNC folder path does not exist at specified location : ", uncSubFolderPath));
			}
		} else {
			throw new DCMAApplicationException("Batch Instance does not exist.");
		}

	}

	/**
	 * This method converts all the supported office files inside the UNC Sub folder and deletes those files after conversions. Only
	 * the valid file formats which are specified by the user are treated for conversion.
	 * 
	 * @param uncSubFolder {@link String} Unc Sub folder path.
	 * @throws DCMAApplicationException
	 */
	private void convertAndDeleteAllOfficeFiles(final String uncSubFolder) throws DCMAApplicationException {
		if (EphesoftStringUtil.isNullOrEmpty(uncSubFolder)) {
			LOGGER.info("Cannot convert office files. Folder Path Or Destination Path is NULL or EMPTY.");
		} else {
			final List<String> allSupportedExtentions = getAllSupportedExtentions();
			if (CollectionUtil.isEmpty(allSupportedExtentions)) {
				LOGGER.info("No valid office file extentions defined.");
			} else {
				final File sourceFolder = new File(uncSubFolder);
				try {
					if (sourceFolder.exists() && sourceFolder.isDirectory()) {
						for (final String validExtention : allSupportedExtentions) {
							final File[] filteredFiles = sourceFolder.listFiles(new CustomFileFilter(false, validExtention));
							for (final File currentFile : filteredFiles) {
								if (currentFile.exists() && currentFile.isFile()) {
									final String currentFileName = currentFile.getName();
									final String newFileName = FileUtils.changeFileExtension(currentFileName,
											FileType.PDF.getExtension());
									LOGGER.info(EphesoftStringUtil.concatenate("Converting ", currentFileName, " to ", newFileName));
									final String newFilePath = EphesoftStringUtil.concatenate(uncSubFolder, File.separator,
											newFileName);
									// checking if the file is currently in use.
									boolean fileLocked = FileUtils.isFileLocked(currentFile);
									if (!fileLocked) {
										try {
											openOfficeConvertor.convert(currentFile.getAbsolutePath(), newFilePath, FileType.PDF);
										} catch (final org.artofsolving.jodconverter.office.ServiceManagerFailException serviceFailed) {

											LOGGER.info("File conversion failed becuase of open office service failure. Now open office conversion will be tried once again in synchronised manner");
											synchronized (FolderImporter.class) {
												try {
													openOfficeServiceRestart();

													// trying conversion once again in synchronized context
													openOfficeConvertor.convert(currentFile.getAbsolutePath(), newFilePath,
															FileType.PDF);
												} catch (final ServiceManagerFailException serviceFailedAgain) {
													LOGGER.info("Unsupported file type conversion.");
													throw new DCMAApplicationException(EphesoftStringUtil.concatenate(
															"Open Office service failed exception was detected. ",
															"Sending this batch to error. ", serviceFailedAgain.getMessage()),
															serviceFailed);
												}
											}
										}
										currentFile.delete();
									} else {
										throw new DCMAApplicationException(EphesoftStringUtil.concatenate("Cannot convert  ",
												currentFile.getAbsolutePath(),
												"file is currently locked. Sending this batch to error. "));
									}
								}
							}
						}
					}
				} catch (final SecurityException securityException) {
					LOGGER.error(EphesoftStringUtil.concatenate("Cannot acces the UNC Folder.", securityException.getMessage()));
				}
			}
		}
	}

	/**
	 * This method will restart open office service again and refresh componentContext Object for openOffice service.
	 * 
	 * @param openOfficeConvertorObject
	 */
	private synchronized void openOfficeServiceRestart() {
		LOGGER.info("Restarting Open Office service");
		try {
			LOGGER.info("Open office service retstart will be tried here:");
			openOfficeTask.start();

			Thread.sleep(2000L);

			LOGGER.info("Open office conpornentContext will be refreshed here");
			openOfficeConvertor.refreshOpenOfficeComponentContext();

		} catch (final InterruptedException inturruptException) {
			LOGGER.error(EphesoftStringUtil.concatenate("Error in openOfficeServiceRestart", inturruptException.getMessage()),
					inturruptException);
		}
		LOGGER.info("Open Office service restarted successfully");
	}

	/**
	 * Gets all the valid file extension's list specified by the user. These specified format are verified with a pre-existing set of
	 * supportable formats specified in {@link OpenOfficeFileType} enum and a list of valid file extensions is created.
	 * 
	 * @return {@link List}<{@link String}> list of valid file extension specified by the user.
	 */
	private List<String> getAllSupportedExtentions() {
		if (null == validExtentionList) {
			String[] userSpecifiedFormatsArray = null;
			final String userSpecifiedFormats = "tif;tiff;pdf";
			if (!EphesoftStringUtil.isNullOrEmpty(userSpecifiedFormats)) {
				userSpecifiedFormatsArray = userSpecifiedFormats.split(String.valueOf(ICommonConstants.SEMI_COLON));
			}
			if (null != userSpecifiedFormatsArray) {
				validExtentionList = new ArrayList<String>();
				for (final String fileFormat : userSpecifiedFormatsArray) {
					// remove the check....
					if (OpenOfficeFileType.isFileSupported(fileFormat)) {
						validExtentionList.add(fileFormat);
					}
				}
			}
		}
		return validExtentionList;
	}

	/**
	 * Gets actual files count from the specified directory. It does not include sub folders while counting no of files.
	 * 
	 * @param uncSubFolder {@link File}
	 * @return Returns no of files in the specified directory.
	 */
	private int getActualFileCount(final File uncSubFolder) {
		int fileCount = 0;
		if (uncSubFolder == null || !uncSubFolder.exists()) {
			LOGGER.info("The specified UNC Folder is NULL or does not exist.");
		} else if (!uncSubFolder.isDirectory()) {
			LOGGER.info(EphesoftStringUtil.concatenate("The specified folder ", uncSubFolder.getAbsolutePath(),
					" is not a valid directory."));
		} else {
			fileCount = uncSubFolder.list().length;
			for (final File currentFile : uncSubFolder.listFiles()) {
				if (currentFile.isDirectory()) {
					fileCount--;
				}
			}
		}
		return fileCount;
	}

	/**
	 * checks for permissible file path length of the batch, and throws an Exception if it exceeds the limit
	 * 
	 * @param batchInstance batch instance ID
	 * @param uncFilePath unc file location
	 * @throws DCMAApplicationException
	 */
	public void checkFilePathLength(final BatchInstance batchInstance, final String uncFilePath) throws DCMAApplicationException {
		int permissibleLength = PERMISSIBLE_LIMIT_PATH_LENGTH - BACK_UP_FOLDER_NAME.length() - 13 - 10 + returnValidCount(uncFilePath);
		if (uncFilePath.length() > permissibleLength) {
			LOGGER.error("Expected File path length exceeds the maximum permissible limit");
			LOGGER.info("UNC File path " + uncFilePath);
			LOGGER.info("permissible Length of file path " + permissibleLength);
			throw new DCMAApplicationException(EphesoftStringUtil.concatenate(
					"Expected File path length exceeds the maximum permissible limit Sending batch ", batchInstance.getIdentifier(),
					" into ERROR."));
		}
	}

	public int returnValidCount(String uncPath) {
		int count = 0;
		try {
			String pathWithoutExtension = uncPath.substring(0, uncPath.length() - 4);
			int lengthofPath = pathWithoutExtension.length();
			String last5characters = pathWithoutExtension.substring(lengthofPath - 5, lengthofPath);
			String last9characters = pathWithoutExtension.substring(lengthofPath - 10, lengthofPath);
			if (last5characters.matches(ICommonConstants.LAST_5_DIGIT_REGEX)) {
				count = count + 4;
			}
			if (last9characters.matches(ICommonConstants.LAST_9_DIGIT_REGEX)) {
				count = count + 5;
			}
		} catch (StringIndexOutOfBoundsException e) {
			LOGGER.info("Insufficiant length to begin break up of file path");
		}
		return count;
	}

	/**
	 * Breaks multipage PDF and multipage Tiff into single page tiff files.
	 * 
	 * @param batchInstance {@link BatchInstance} Batch Instance to be process
	 * @param folderPath {@link String} unc sub folder path for batch instance
	 * @throws DCMAApplicationException If any error occurs while breaking multipage pdf or tiff file
	 */
	public void breakPDFandTifForExtraction(final String batchClassId, final String folderPath) throws DCMAApplicationException {
		// final String batchClassId = batchClass.getIdentifier();
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
		final File testExtSubFolder = new File(folderPath);
		final String importMultiPage = batchPluginPropertiesService.getPropertyValue(batchClassId, IMPORT_MULTIPAGE_FILES_PLUGIN,
				FolderImporterProperties.FOLDER_IMPORTER_MULTI_PAGE_IMPORT);

		final File testExtractionFolder = new File(folderPath);
		if (testExtractionFolder.exists()) {
			if ((null != importMultiPage)
					&& (importMultiPage.equalsIgnoreCase(IFolderImporterConstants.TRUE) || importMultiPage
							.equalsIgnoreCase(IFolderImporterConstants.YES))) {

				final String pdfToTiffConverter = batchPluginPropertiesService.getPropertyValue(batchClassId,
						IMPORT_MULTIPAGE_FILES_PLUGIN, FolderImporterProperties.FOLDER_IMPORTER_PDF_TO_TIFF_CONVERTER);

				if (EphesoftStringUtil.isNullOrEmpty(pdfToTiffConverter)) {
					LOGGER.error("Unable to fetch proper value of PDF to Tiff conversion process property of import multipage plugin from database.");
				} else {

					// Temporary backup of pdf and tiff files which will be used to validate the count of converted tiffs.
					final String backUpFolderName = EphesoftStringUtil.concatenate(testExtSubFolder.getName(),
							ICommonConstants.PDF_AND_TIFF_BACK_UP_FOLDER_NAME);
					final String backUpFolderPath = createUNCFolderBackup(folderPath, backUpFolderName);

					// final BatchClass batchClass = batchInstance.getBatchClass();

					String compressionRatio = null;
					final File backUpFolder = new File(backUpFolderPath);
					try {
						if (null != backUpFolder && backUpFolder.exists()) {
							final int expectedTifFilesCount = getTiffPagesCount(backUpFolder);
							final String folderIgnoreCharList = getFolderIgnoreCharList();
							if (null == folderIgnoreCharList || getIgnoreReplaceChar() == null || folderIgnoreCharList.isEmpty()
									|| getIgnoreReplaceChar().isEmpty() || getIgnoreReplaceChar().length() > 1) {
								throw new DCMAApplicationException("Invalid property file configuration....");
							}
							final String fdIgList[] = folderIgnoreCharList.split(SEMICOLON_DELIMITER);
							final List<File> deleteFileList = new ArrayList<File>();
							BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
							batchInstanceThread.setUsingGhostScript(true);
							this.convertLargeTiffIntoPdf(testExtSubFolder);
							final String[] pdfFileList = testExtSubFolder
									.list(new CustomFileFilter(false, FileType.PDF.getExtension()));
							processInputPDFFiles(folderPath, batchClass, pdfFileList, fdIgList, batchInstanceThread,
									compressionRatio);

							batchInstanceThread = new BatchInstanceThread(batchClassId);
							processInputTiffFiles(folderPath, batchClass, fdIgList, batchInstanceThread, testExtSubFolder);

							// Deleting the intermediate files.
							deleteTempFiles(deleteFileList);

							// Counting the number of files converted in the batch folder.
							final int actualTiffFilesCount = testExtractionFolder.list(new CustomFileFilter(false, FileType.TIF
									.getExtension(), FileType.TIFF.getExtension())).length;
							LOGGER.info(EphesoftStringUtil.concatenate("expectedTifFilesCount :: ", expectedTifFilesCount));
							LOGGER.info(EphesoftStringUtil.concatenate("Actual tiff files count :: ", actualTiffFilesCount));
							if (actualTiffFilesCount < expectedTifFilesCount) {
								LOGGER.error(EphesoftStringUtil.concatenate(
										"Converted Tiff files count not equal to the the TIFF pages count. actualTiffFilesCount :: ",
										actualTiffFilesCount, ", expectedTifFilesCount :: ", expectedTifFilesCount,
										" for batch instance ::", batchClassId));
								throw new DCMAApplicationException("Converted Tiff files count not equal to the the TIFF pages count.");
							}

						}
					} catch (Exception exception) {
						LOGGER.error(exception.getMessage(), exception);
						throw new DCMAApplicationException("Exception accured while converting input files into tif files");
					} finally {
						// Deletes the temporary backup of pdf and tiff files which was used to validate the count of converted tiffs.
						FileUtils.deleteDirectoryAndContents(backUpFolderPath);
					}
				}
			} else {
				final String[] pdfFileList = testExtSubFolder.list(new CustomFileFilter(false, FileType.PDF.getExtension()));
				if (null != pdfFileList && pdfFileList.length > 0) {
					throw new DCMAApplicationException("Multi page import switch is OFF so unable to process PDF files.");
				} else {
					LOGGER.error("Unable to fetch proper value of import multipage file property of import multipage plugin from database.");
				}
			}
		} else {
			throw new DCMAApplicationException(EphesoftStringUtil.concatenate("Invalid Test Extraction folder for batch class ",
					batchClassId));
		}
	}

}
