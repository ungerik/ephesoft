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

package com.ephesoft.dcma.tesseract.service;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.dao.xml.HocrSchemaDao;
import com.ephesoft.dcma.batch.encryption.service.XMLSignature;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.TesseractVersionProperty;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.tesseract.TesseractProperties;
import com.ephesoft.dcma.tesseract.TesseractReader;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.BackUpFileService;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.constant.HOCRGenerationPlugin;

public class TesseractServiceImpl implements TesseractService, ICommonConstants {

	private static final String TESSERACT_BASE_PATH_NOT_CONFIGURED = "Tesseract Base path not configured.";

	private static final Logger LOGGER = LoggerFactory.getLogger(TesseractServiceImpl.class);

	private static String tesseractVersion;

	@Autowired
	private transient TesseractReader tesseractReader;

	@Autowired
	private HocrSchemaDao hocrSchemaDao;

	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Provides signature to the XML File generated. This will allow provide a signature to the XML file generated from the Tesseract
	 * folder.
	 */
	@Autowired
	private XMLSignature xmlSignatureService;
	/**
	 * Instance of BatchClassPluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService batchClassPluginPropertiesService;

	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		BackUpFileService.backUpBatch(batchInstanceID.getID());
	}

	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		BackUpFileService.copyBatchXML(batchInstanceID.getID(), pluginWorkflow);
	}

	@Override
	public void generateHOCRFiles(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		LOGGER.info("Entering generate hocr files using Tesseract.....");
		try {
			tesseractReader.readOCR(batchInstanceID.getID(), pluginWorkflow);
		} catch (Exception exception) {
			LOGGER.error(EphesoftStringUtil.concatenate("Uncaught Exception in readOCR method ", exception.getMessage()), exception);
			throw new DCMAException(exception.getMessage(), exception);
		}
		LOGGER.info("Exiting generate hocr files using Tesseract.....");
	}

	@Override
	public void createOCR(String actualFolderLocation, String colorSwitch, String imageName, String outputFolderLocation,
			String cmdLanguage, String tesseractVersion) throws DCMAException {
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
		try {
			tesseractReader.createOCR(actualFolderLocation, colorSwitch, imageName, batchInstanceThread, outputFolderLocation,
					cmdLanguage, tesseractVersion);
			batchInstanceThread.execute();
			String hocrFileName = EphesoftStringUtil.concatenate(imageName.substring(0, imageName.lastIndexOf(EXTENSION_CHAR)),
					FileType.HTML.getExtensionWithDot());
			HocrPage hocrPage = new HocrPage();
			batchSchemaService.hocrGenerationInternal(
					actualFolderLocation,
					outputFolderLocation + File.separator + imageName.substring(0, imageName.lastIndexOf('.'))
							+ FileType.HOCR_XML.getExtension(), "PG0", outputFolderLocation + File.separator + hocrFileName, hocrPage);
			HocrPages hocrPages = new HocrPages();
			String fileName = EphesoftStringUtil.concatenate(imageName.substring(0, imageName.lastIndexOf('.')),
					FileType.HOCR_XML.getExtension());
			String fileSignature = xmlSignatureService.generateHOCRSignature(HOCRGenerationPlugin.TESSERACT_HOCR.toString(), fileName);
			hocrPages.setSignature(fileSignature);
			hocrPages.getHocrPage().add(hocrPage);
			hocrSchemaDao.create(hocrPages, EphesoftStringUtil.concatenate(outputFolderLocation, File.separator, fileName));
		} catch (Exception e) {
			LOGGER.error("Exception occurs in creating OCR file " + e.getMessage(), e);
			batchInstanceThread.remove();
			throw new DCMAException(e.getMessage(), e);
		}
	}

	/**
	 * Generates hocr files for all the images present inside a folder.
	 * 
	 * @param folderPath path of folder containing images
	 * @param batchClassIdentifier batch class identifier
	 * @throws DCMAApplicationException if an error occurs while creating the hocr file
	 * @throws IOException if an error occurs while creating the hocr file
	 */
	@Override
	public void generateHOCRFilesForFolder(String folderPath, String batchClassIdentifier) throws DCMAApplicationException,
			IOException {
		LOGGER.info("Entering generate hocr files for folder using Tesseract...");
		String tesseractVersion = getTesseractVersion(batchClassIdentifier);
		final String cmdLanguage = getTesseractLanguage(batchClassIdentifier);
		File imageFolder = new File(folderPath);
		String[] listOfInternalFolders = imageFolder.list();
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
		if (null != listOfInternalFolders && listOfInternalFolders.length > 0) {
			for (String currentFolder : listOfInternalFolders) {
				File documentFolder = new File(EphesoftStringUtil.concatenate(imageFolder.getAbsolutePath(), File.separator,
						currentFolder));
				if (documentFolder.isDirectory()) {
					String[] listOfPageFolders = documentFolder.list();
					if (null != listOfPageFolders && listOfPageFolders.length > 0) {
						for (String pageFolderName : listOfPageFolders) {
							String pageFolderPath = documentFolder + File.separator + pageFolderName;
							createHOCR(pageFolderPath, currentFolder, batchInstanceThread, cmdLanguage, tesseractVersion);
						}
					} else {
						LOGGER.info("No page types present.");
					}
				} else {
					createHOCR(folderPath, null, batchInstanceThread, cmdLanguage, tesseractVersion);
				}
			}
		} else {
			LOGGER.info("No Document types present.");
		}
		LOGGER.info("Exiting generate hocr files for folder using Tesseract...");
	}

	private void createHOCR(String pageFolderPath, String currentFolder, BatchInstanceThread batchInstanceThread, String cmdLanguage,
			String tesseractVersion2) throws DCMAApplicationException {
		File pageFolder = new File(pageFolderPath);
		String[] listOfXMLFiles = pageFolder.list(new CustomFileFilter(false, FileType.HOCR_XML.getExtension()));
		// Including all the files having tif and tiff extension.
		String[] listOfImageFiles = pageFolder.list(new CustomFileFilter(false, FileType.TIF.getExtension(), FileType.TIFF
				.getExtension()));
		if (null != listOfImageFiles && listOfImageFiles.length > 0) {
			for (String currentImageFile : listOfImageFiles) {
				// Checking for the index of '.' in the image file name for getting the actual name of image file.
				int indexOfDot = currentImageFile.lastIndexOf(ICommonConstants.EXTENSION_CHAR);
				if (indexOfDot != -1) {
					String hocrFileName = EphesoftStringUtil.concatenate(currentImageFile.substring(0, indexOfDot),
							FileType.HOCR_XML.getExtension());
					boolean hocrFileExists = checkForExistense(hocrFileName, listOfXMLFiles);
					if (!hocrFileExists) {
						try {
							tesseractReader.createOCR(pageFolderPath, OFF, currentImageFile, batchInstanceThread, pageFolderPath,
									cmdLanguage, tesseractVersion2);
							batchInstanceThread.execute();
							HocrPage hocrPage = new HocrPage();
							batchSchemaService.hocrGenerationInternal(pageFolderPath, EphesoftStringUtil.concatenate(pageFolderPath,
									File.separator, currentImageFile.substring(0, currentImageFile.lastIndexOf(EXTENSION_CHAR)),
									FileType.HOCR_XML.getExtension()), PG0, EphesoftStringUtil.concatenate(pageFolderPath,
									File.separator, currentImageFile.substring(0, currentImageFile.lastIndexOf(EXTENSION_CHAR)),
									FileType.HTML.getExtensionWithDot()), hocrPage);
							HocrPages hocrPages = new HocrPages();
							hocrPages.getHocrPage().add(hocrPage);
							hocrSchemaDao.create(hocrPages, EphesoftStringUtil.concatenate(pageFolderPath, File.separator,
									currentImageFile.substring(0, currentImageFile.lastIndexOf(EXTENSION_CHAR)),
									FileType.HOCR_XML.getExtension()));
						} catch (DCMAApplicationException applicationException) {
							LOGGER.error(EphesoftStringUtil.concatenate("Error occured while creating hocr using Tesseract.",
									applicationException.getMessage()));
							batchInstanceThread.remove();
							throw new DCMAApplicationException(applicationException.getMessage(), applicationException);
						} catch (Exception exception) {
							LOGGER.error(EphesoftStringUtil.concatenate(
									"Error occured while creating hocr from html file generated using tesseract",
									exception.getMessage()));
							throw new DCMAApplicationException(exception.getMessage(), exception);
						}
					}
				}
			}
		}
	}

	private boolean checkForExistense(String fileName, String[] listOfXMLFiles) {
		boolean exists = false;
		if (listOfXMLFiles != null && listOfXMLFiles.length > 0) {
			for (String eachFile : listOfXMLFiles) {
				if (eachFile.equalsIgnoreCase(fileName)) {
					exists = true;
					break;
				}
			}
		}
		return exists;
	}

	/**
	 * Creates the hocr for a file using the plugin selected for ocr'ing in the batch class.
	 * 
	 * @param batchClassIdentifier the batch class identifier.
	 * @param inputFolderLocation folder containing the images
	 * @param imageFileName image file to be used for ocr'ing
	 * @param outputFolderLocation folder where hocr will be generated
	 * @throws DCMAException if an error occurs while creating the hocr file
	 * @throws DCMAApplicationException if an error occurs while creating the hocr file
	 */
	@Override
	public void createOCRforFile(final String batchClassIdentifier, final String inputFolderLocation, final String imageFileName,
			final String outputFolderLocation) throws DCMAException, DCMAApplicationException {
		LOGGER.info("Entering create ocr for file using Tesseract...");
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
		try {
			String tesseractVersion = getTesseractVersion(batchClassIdentifier);
			final String cmdLanguage = getTesseractLanguage(batchClassIdentifier);
			tesseractReader.createOCR(inputFolderLocation, OFF, imageFileName, batchInstanceThread, outputFolderLocation, cmdLanguage,
					tesseractVersion);
			batchInstanceThread.execute();
			String hocrFileName = EphesoftStringUtil.concatenate(
					imageFileName.substring(0, imageFileName.lastIndexOf(EXTENSION_CHAR)), FileType.HTML.getExtensionWithDot());
			HocrPage hocrPage = new HocrPage();
			batchSchemaService.hocrGenerationInternal(
					inputFolderLocation,
					EphesoftStringUtil.concatenate(outputFolderLocation, File.separator,
							imageFileName.substring(0, imageFileName.lastIndexOf(EXTENSION_CHAR)), FileType.HOCR_XML.getExtension()),
					PG0, EphesoftStringUtil.concatenate(outputFolderLocation, File.separator, hocrFileName), hocrPage);
			HocrPages hocrPages = new HocrPages();
			hocrPages.getHocrPage().add(hocrPage);
			hocrSchemaDao.create(
					hocrPages,
					EphesoftStringUtil.concatenate(outputFolderLocation, File.separator,
							imageFileName.substring(0, imageFileName.lastIndexOf(EXTENSION_CHAR)), FileType.HOCR_XML.getExtension()));
		} catch (DCMAApplicationException dcmaApplicationException) {
			LOGGER.error(EphesoftStringUtil.concatenate("Exception occured in creating OCR file.",
					dcmaApplicationException.getMessage(), dcmaApplicationException.getStackTrace().toString()),
					dcmaApplicationException);
			batchInstanceThread.remove();
			throw new DCMAException(dcmaApplicationException.getMessage(), dcmaApplicationException);
		} catch (IOException ioException) {
			LOGGER.error(EphesoftStringUtil.concatenate(TESSERACT_BASE_PATH_NOT_CONFIGURED, ioException), ioException);
			throw new DCMAApplicationException(TESSERACT_BASE_PATH_NOT_CONFIGURED, ioException);
		} catch (Exception exception) {
			LOGGER.error(EphesoftStringUtil.concatenate("Exception occured in internal hocr generation.", exception.getMessage(),
					exception.getStackTrace().toString()), exception);
			throw new DCMAException(exception.getMessage(), exception);
		}
		LOGGER.info("Exiting create ocr for file using Tesseract...");
	}

	/**
	 * Fetches the tesseract version and checks for the tesseract base path.
	 * 
	 * @param batchClassIdentifier the batch class identifier
	 * @return {@link String} tesseract version
	 * @throws IOException if an error occurs in reading the tesseract base path property
	 * @throws DCMAApplicationException if the tesseract base path is not configured
	 */
	private String getTesseractVersion(final String batchClassIdentifier) throws IOException, DCMAApplicationException {
		LOGGER.info("Enetring get tesseract version...");
		if (EphesoftStringUtil.isNullOrEmpty(tesseractVersion)) {
			tesseractVersion = EMPTY_STRING;
			String tesseractBasePath = EMPTY_STRING;
			BatchPluginConfiguration[] pluginConfiguration = batchClassPluginPropertiesService.getPluginProperties(
					batchClassIdentifier, TESSERACT_HOCR_PLUGIN, TesseractVersionProperty.TESSERACT_VERSIONS);
			if (pluginConfiguration != null && pluginConfiguration.length > 0 && pluginConfiguration[0].getValue() != null
					&& pluginConfiguration[0].getValue().length() > 0) {
				tesseractVersion = pluginConfiguration[0].getValue();
			}
			ApplicationConfigProperties app = ApplicationConfigProperties.getApplicationConfigProperties();
			tesseractBasePath = app.getProperty(tesseractVersion);
			if (tesseractBasePath == null) {
				LOGGER.error(TESSERACT_BASE_PATH_NOT_CONFIGURED);
				throw new DCMAApplicationException(TESSERACT_BASE_PATH_NOT_CONFIGURED);
			}
		}
		LOGGER.info("Exiting get tesseract version...");
		return tesseractVersion;
	}

	/**
	 * Fetches the tesseract language.
	 * 
	 * @param batchClassIdentifier the batch class identifier.
	 * @return {@link String} tesseract language.
	 */
	private String getTesseractLanguage(final String batchClassIdentifier) {
		final BatchPluginConfiguration[] pluginConfiguration = batchClassPluginPropertiesService.getPluginProperties(
				batchClassIdentifier, TESSERACT_HOCR_PLUGIN, TesseractProperties.TESSERACT_LANGUAGE);
		String cmdLanguage = CMD_LANGUAGE_ENG;
		if (null != pluginConfiguration && pluginConfiguration.length > 0) {
			final String pluginConfig = pluginConfiguration[0].getValue();
			if (null != pluginConfig && pluginConfig.length() > 0) {
				cmdLanguage = pluginConfig;
			}
		}
		return cmdLanguage;
	}

}
