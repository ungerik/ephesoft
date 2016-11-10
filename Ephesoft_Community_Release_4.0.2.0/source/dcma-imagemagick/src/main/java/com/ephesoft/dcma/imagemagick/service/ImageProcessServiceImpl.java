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

package com.ephesoft.dcma.imagemagick.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.annotation.PostProcess;
import com.ephesoft.dcma.core.annotation.PreProcess;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.ImageType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.imagemagick.IImageMagickCommonConstants;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.ImageRotator;
import com.ephesoft.dcma.imagemagick.MultiPageTiffPdfCreator;
import com.ephesoft.dcma.imagemagick.MultiPageToSinglePageConverter;
import com.ephesoft.dcma.imagemagick.ThumbnailPNGCreator;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.imagemagick.imageClassifier.ImageClassifier;
import com.ephesoft.dcma.imagemagick.impl.HOCRtoPDFCreator;
import com.ephesoft.dcma.imagemagick.impl.ITextPDFCreator;
import com.ephesoft.dcma.util.BackUpFileService;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.EphesoftStringUtil;

public class ImageProcessServiceImpl implements ImageProcessService, ICommonConstants {

	private static final String PROBLEM_ROTATING_IMAGES = "Problem rotating images";

	private static final String ERROR_IN_PNG_FILE_GENERATION = "Problem in generating PNG File";

	private static final String ON = "ON";

	private static final char DOT = '.';

	/**
	 * String constant for Fpr.rsp file name.
	 */
	private static final String FPR_RSP_FILE = "FPR.rsp";

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageProcessServiceImpl.class);

	@Autowired
	private BatchSchemaService batchSchemaService;

	@Autowired
	private ImageRotator imageRotator;

	@Autowired
	private ThumbnailPNGCreator thumbnailPNGCreator;

	@Autowired
	private MultiPageToSinglePageConverter multiPageToSinglePageConverter;

	@Autowired
	private ImageClassifier imageClassifier;

	@Autowired
	private MultiPageTiffPdfCreator multipageTiffPdfCreator;

	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	@Autowired
	private BatchClassService batchClassService;

	@Autowired
	private HOCRtoPDFCreator hocrToPDFCreator;

	@Autowired
	private ITextPDFCreator iTextPDFCreator;

	/**
	 * batchInstanceService BatchInstanceService.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	@PreProcess
	public void preProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) throws DCMAApplicationException {
		Assert.notNull(batchInstanceID);
		BackUpFileService.backUpBatch(batchInstanceID.getID());
	}

	@PostProcess
	public void postProcess(final BatchInstanceID batchInstanceID, String pluginWorkflow) {
		Assert.notNull(batchInstanceID);
		BackUpFileService.copyBatchXML(batchInstanceID.getID(), pluginWorkflow);
	}

	@Override
	public void createOcrInputImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow, ImageType imageType)
			throws DCMAException {
		try {
			String sBatchFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
			String generateDisplayPng = multipageTiffPdfCreator.getGenerateDisplayPng();
			String inputParameters = multipageTiffPdfCreator.getInputParameters();
			String outputParameters = multipageTiffPdfCreator.getOutputParameters();
			thumbnailPNGCreator.generateFullFiles(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
					IImageMagickCommonConstants.OCR_INPUT_FILE, ImageMagicKConstants.CREATE_OCR_INPUT_PLUGIN, pluginWorkflow,
					generateDisplayPng, inputParameters, outputParameters);
		} catch (Exception ex) {
			LOGGER.error(ERROR_IN_PNG_FILE_GENERATION, ex);
			throw new DCMAException(ERROR_IN_PNG_FILE_GENERATION, ex);
		}

	}

	@Override
	public BatchInstanceThread createCompThumbForImage(final String batchInstanceIdentifier, final String folderPath,
			final String[][] sListOfTiffFiles, final String outputImageParameters, final String compareThumbnailH,
			final String compareThumbnailW) throws DCMAException {
		try {
			BatchInstanceThread batchInstanceThread = thumbnailPNGCreator.generateThumbnailInternal(batchInstanceIdentifier,
					IImageMagickCommonConstants.THUMB_TYPE_COMP, compareThumbnailH, compareThumbnailW, outputImageParameters,
					sListOfTiffFiles);
			return batchInstanceThread;
		} catch (Exception ex) {
			LOGGER.error("Problem generating thumbnalis exception->" + ex.getMessage(), ex);
			throw new DCMAException("Problem generating thumbnalis exception->" + ex.getMessage(), ex);
		}
	}

	@Override
	public void createThumbnails(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			String sBatchFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
			thumbnailPNGCreator.generateThumbnail(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
					IImageMagickCommonConstants.THUMB_TYPE_DISP, pluginWorkflow);
			if (pluginPropertiesService.getPropertyValue(batchInstanceID.getID(), ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN,
					ImageMagicProperties.CREATE_THUMBNAILS_SWITCH).equalsIgnoreCase(ON)) {
				thumbnailPNGCreator.generateThumbnail(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
						IImageMagickCommonConstants.THUMB_TYPE_COMP, pluginWorkflow);
			} else {
				LOGGER.info("Skipping creation of comparison thumbnails. Switch set as OFF");
			}
		} catch (Exception ex) {
			LOGGER.error("Problem generating thumbnalis exception->" + ex.getMessage(), ex);
			throw new DCMAException("Problem generating thumbnalis exception->" + ex.getMessage(), ex);
		}
	}

	@Override
	public void createMultiPageFiles(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {

		// removed multipage tiff switch for version 3.1.
		String checkPDFExportProcess = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CHECK_PDF_EXPORT_PROCESS);
		String pdfOptimizationParams = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.PDF_OPTIMIZATION_PARAMETERS);
		String pdfOptimizationSwitch = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.PDF_OPTIMIZATION_SWITCH);
		String ghostscriptPdfParameters = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.GHOSTSCRIPT_COMMAND_PDF_PARAMETERS);
		// Fetching the value for the IText searchable pdf type switch.
		String iTextSearchablePDFType = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.ITEXT_SEARCHABLE_PDF_TYPE);

		// code for version 3.1
		// Variable for file type to be created.
		String createMultipageFileType = pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.MULTIPAGE_FILE_TYPE);

		try {
			multipageTiffPdfCreator.createMultiPageFiles(ghostscriptPdfParameters, batchInstanceID.getID(), pdfOptimizationParams,
					checkPDFExportProcess, pluginWorkflow, pdfOptimizationSwitch, createMultipageFileType,
					ICommonConstants.PDF_FPR_FILE,
					iTextSearchablePDFType);
		} catch (Exception ex) {
			LOGGER.error("Problem generating overlayed Images exception->" + ex.getMessage());
			throw new DCMAException("Problem generating overlayed Images " + ex.getMessage(), ex);
		}

	}

	@Override
	public void createMultiPageFilesAPI(String ghostscriptPdfParameters, String pdfOptimizationParams, String multipageTifSwitch,
			String toolName, String pdfOptimizationSwitch, String workingDir, String outputDir, List<File> singlePageFiles,
			String batchInstanceIdentifier) throws DCMAException {
		try {
			multipageTiffPdfCreator.createMultiPageFilesAPI(ghostscriptPdfParameters, pdfOptimizationParams, multipageTifSwitch,
					toolName, pdfOptimizationSwitch, workingDir, outputDir, singlePageFiles, batchInstanceIdentifier);
		} catch (Exception ex) {
			LOGGER.error("Problem generating overlayed Images exception->" + ex.getMessage());
			throw new DCMAException("Problem generating overlayed Images " + ex.getMessage(), ex);
		}
	}

	@Override
	public void createDisplayImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			String sBatchFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
			String generateDisplayPng = multipageTiffPdfCreator.getGenerateDisplayPng();
			String inputParameters = multipageTiffPdfCreator.getInputParameters();
			String outputParameters = multipageTiffPdfCreator.getColoredOutputParameters();
			thumbnailPNGCreator.generateFullFiles(sBatchFolder, batchInstanceID.getID(), batchSchemaService,
					IImageMagickCommonConstants.DISPLAY_IMAGE, ImageMagicKConstants.CREATE_DISPLAY_IMAGE_PLUGIN, pluginWorkflow,
					generateDisplayPng, inputParameters, outputParameters);
		} catch (Exception ex) {
			LOGGER.error("Problem in generating Display File", ex);
			throw new DCMAException("Problem in generating Display File", ex);
		}
	}

	@Override
	public void classifyImagesAPI(String maxVal, String imMetric, String imFuzz, String batchInstanceIdentifier,
			String batchClassIdentifier, String sBatchFolder, List<Page> listOfPages) throws DCMAException {
		try {
			imageClassifier.classifyAllImgsOfBatchInternal(maxVal, imMetric, imFuzz, batchInstanceIdentifier, batchClassIdentifier,
					sBatchFolder, listOfPages);
		} catch (Exception ex) {
			LOGGER.error("Problem in Image Classification" + ex.getMessage(), ex);
			throw new DCMAException("Problem in Image Classification" + ex.getMessage(), ex);
		}

	}

	@Override
	public void classifyImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			if (ON.equalsIgnoreCase(pluginPropertiesService.getPropertyValue(batchInstanceID.getID(),
					ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN, ImageMagicProperties.CLASSIFY_IMAGES_SWITCH))) {
				String sBatchFolder = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
				LOGGER.info("sBatchFolder = " + sBatchFolder);
				imageClassifier.classifyAllImgsOfBatch(batchInstanceID.getID(), sBatchFolder);
			} else {
				LOGGER.info("Skipping image magic classification. Switch set as OFF");
			}
		} catch (Exception ex) {
			LOGGER.error("Problem in Image Classification" + ex.getMessage(), ex);
			throw new DCMAException("Problem in Image Classification" + ex.getMessage(), ex);
		}
	}

	@Override
	public void rotateImage(final BatchInstanceID batchInstanceID, String documentId, String pageId) throws DCMAException {
		String displayImageFilePath = null;
		String thumbnailFilePath = null;

		try {
			displayImageFilePath = batchSchemaService.getDisplayImageFilePath(batchInstanceID.getID(), documentId, pageId);
			thumbnailFilePath = batchSchemaService.getThumbnailFilePath(batchInstanceID.getID(), documentId, pageId);
			imageRotator.rotateImage(displayImageFilePath);
			imageRotator.rotateImage(thumbnailFilePath);
		} catch (Exception e) {
			LOGGER.error(PROBLEM_ROTATING_IMAGES);
			throw new DCMAException(PROBLEM_ROTATING_IMAGES, e);
		}

	}

	@Override
	public void generateThumbnailsAndPNGsForImage(final File imagePath, final String thumbnailW, final String thumbnailH)
			throws DCMAException {
		try {
			thumbnailPNGCreator.generateThumbnailsAndPNGForImage(imagePath, thumbnailW, thumbnailH);
		} catch (Exception e) {
			LOGGER.error("Problem generateThumbnailsAndPNGsForImage", e);
		}
	}

	@Override
	public void generateThumbnailsAndJPGsForImage(final File imagePath, final String thumbnailW, final String thumbnailH)
			throws DCMAException {
		try {
			thumbnailPNGCreator.generateThumbnailsAndJPGForImage(imagePath, thumbnailW, thumbnailH);
		} catch (Exception exception) {
			LOGGER.error("Problem generateThumbnailsAndPNGsForImage", exception);
		}
	}

	@Override
	public void rotateImage(String imagePath) throws DCMAException {
		try {
			imageRotator.rotateImage(imagePath);
		} catch (Exception e) {
			LOGGER.error(PROBLEM_ROTATING_IMAGES);
			throw new DCMAException(PROBLEM_ROTATING_IMAGES, e);
		}
	}

	@Override
	public void convertPdfOrMultiPageTiffToTiff(BatchClass batchClass, File imagePath, File outputFilePath,
			BatchInstanceThread thread, Boolean allowPdfConversion, boolean deleteImage) throws DCMAException {
		try {
			multiPageToSinglePageConverter.convertPdfOrMultiPageTiffToTiff(batchClass, imagePath, outputFilePath, thread,
					allowPdfConversion, deleteImage);
		} catch (DCMAApplicationException e) {
			throw new DCMAException(e.getMessage());
		}
	}

	@Override
	public void convertPdfOrMultiPageTiffToTiff(BatchInstance batchInstance, File imagePath, File outputFilePath,
			BatchInstanceThread batchInstanceThread, Boolean allowPdfConversion, boolean deleteImage) throws DCMAException {
		try {
			multiPageToSinglePageConverter.convertPdfOrMultiPageTiffToTiff(batchInstance, imagePath, outputFilePath,
					batchInstanceThread, allowPdfConversion, deleteImage);
		} catch (DCMAApplicationException e) {
			throw new DCMAException(e.getMessage());
		}
	}

	@Override
	public void convertPdfOrMultiPageTiffToTiff(final BatchClass batchClass, final File imageFile, final File outputFile,
			final BatchInstanceThread thread) throws DCMAException {
		try {
			multiPageToSinglePageConverter.convertPdfOrMultiPageTiffToTiff(batchClass, imageFile, outputFile, thread);
		} catch (Exception e) {
			throw new DCMAException(e.getMessage());
		}
	}

	@Override
	public void convertPdfOrMultiPageTiffToTiff(final BatchClass batchClass, final File imageFile, final File outputFile,
			final BatchInstanceThread thread, final Boolean allowPdfConversion) throws DCMAException {
		try {
			convertPdfOrMultiPageTiffToTiff(batchClass, imageFile, outputFile, thread, allowPdfConversion, false);
		} catch (Exception e) {
			throw new DCMAException(e.getMessage());
		}
	}

	@Override
	public void convertPdfOrMultiPageTiffToTiffUsingIM(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread, boolean deleteImage) throws DCMAException {
		try {
			String imageName = imagePath.getAbsolutePath();
			int indexOf = imageName.toLowerCase().indexOf(FileType.TIF.getExtensionWithDot());
			if (indexOf == -1) {
				indexOf = imageName.toLowerCase().indexOf(FileType.TIFF.getExtensionWithDot());
				if (indexOf == -1) {
					indexOf = imageName.toLowerCase().indexOf(FileType.PDF.getExtensionWithDot());
				}
			}
			if (indexOf == -1) {
				throw new DCMAException("Unsupported file format");
			}
			multiPageToSinglePageConverter.convertPdfOrMultiPageTiffToTiffUsingIM(inputParams, imagePath, outputParams,
					outputFilePath, thread, indexOf, deleteImage);
		} catch (DCMAApplicationException e) {
			throw new DCMAException(e.getMessage());
		}
	}

	// New Method
	@Override
	public void convertPdfOrMultiPageTiffToPNGOrTifUsingIM(String inputParams, File imagePath, String outputParams,
			File outputFilePath, BatchInstanceThread thread, boolean deleteImage, boolean isConvertToTiff) throws DCMAException {
		try {
			String imageName = imagePath.getAbsolutePath();
			int indexOf = imageName.toLowerCase().indexOf(FileType.TIF.getExtensionWithDot());
			if (indexOf == -1) {
				indexOf = imageName.toLowerCase().indexOf(FileType.TIFF.getExtensionWithDot());
				if (indexOf == -1) {
					indexOf = imageName.toLowerCase().indexOf(FileType.PDF.getExtensionWithDot());
				}
			}
			if (indexOf == -1) {
				throw new DCMAException("Unsupported file format");
			}
			multiPageToSinglePageConverter.convertPdfOrMultiPageTiffToPNGOrTifUsingIM(inputParams, imagePath, outputParams,
					outputFilePath, thread, indexOf, deleteImage, isConvertToTiff);
		} catch (DCMAApplicationException e) {
			throw new DCMAException(e.getMessage());
		}
	}

	@Override
	public void convertPdfOrMultiPageTiffToTiffUsingIM(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread) throws DCMAException {
		convertPdfOrMultiPageTiffToTiffUsingIM(inputParams, imagePath, outputParams, outputFilePath, thread, false);
	}

	@Override
	public List<File> convertPdfOrMultiPageTiffToTiff(final BatchClassID batchClassID, final String folderPath,
			final File testImageFile, final Boolean isTestAdvancedKV, final BatchInstanceThread thread) throws DCMAException {
		BatchClass batchClass = batchClassService.get(batchClassID.getID());
		List<File> allImageFiles = null;
		if (isTestAdvancedKV) {
			int lastIndexOfDot = testImageFile.getName().lastIndexOf(DOT);
			if (testImageFile.exists() && lastIndexOfDot > -1) {
				StringBuilder hocrFilePath = new StringBuilder();
				hocrFilePath.append(folderPath).append(File.separator).append(testImageFile.getName().substring(0, lastIndexOfDot))
						.append(FileType.HOCR_XML.getExtension());

				File hocrFile = new File(hocrFilePath.toString());
				if (!hocrFile.exists()) {
					allImageFiles = new ArrayList<File>();
					allImageFiles.add(testImageFile);
					convertPdfOrMultiPageTiffToTiff(batchClass, testImageFile, null, thread);
				}
			} else {
				LOGGER.info("File doesn't exist = " + testImageFile.getAbsolutePath());
			}
		} else {
			allImageFiles = getAllImagesPathInFolder(folderPath);
			if (allImageFiles != null) {
				for (File imageFile : allImageFiles) {
					convertPdfOrMultiPageTiffToTiff(batchClass, imageFile, null, thread, true, false);
				}
			}
		}
		return allImageFiles;
	}

	private List<File> getAllImagesPathInFolder(String testKvExtractionFolderPath) {
		List<File> allImageFiles = null;
		File folder = new File(testKvExtractionFolderPath);
		String[] imageNames = folder.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
				.getExtensionWithDot(), FileType.PDF.getExtensionWithDot()));
		String[] xmlFiles = folder.list(new CustomFileFilter(false, FileType.XML.getExtensionWithDot()));
		if (imageNames != null || xmlFiles != null) {
			allImageFiles = new ArrayList<File>();
			for (String fileName : imageNames) {
				isHOCRFilesGenerated(testKvExtractionFolderPath, allImageFiles, xmlFiles, fileName);
			}
		}
		return allImageFiles;
	}

	private void isHOCRFilesGenerated(final String testKvExtractionFolderPath, List<File> allImageFiles, final String[] xmlFiles,
			final String fileName) {
		boolean xmlFilesGenerated = false;
		int lastIndexOfDot = fileName.lastIndexOf(DOT);
		if (lastIndexOfDot > -1) {
			for (String xmlFile : xmlFiles) {
				int indexOfHOCR = xmlFile.lastIndexOf(FileType.HOCR_XML.getExtension());
				if (indexOfHOCR > -1) {
					if (xmlFile.substring(0, indexOfHOCR).equalsIgnoreCase(fileName.substring(0, lastIndexOfDot))) {
						xmlFilesGenerated = true;
						break;
					}
				}
			}
		}

		if (!xmlFilesGenerated) {
			StringBuilder imageFilePath = new StringBuilder();
			imageFilePath.append(testKvExtractionFolderPath);
			imageFilePath.append(File.separator);
			imageFilePath.append(fileName);
			File file = new File(imageFilePath.toString());
			if (allImageFiles == null) {
				allImageFiles = new ArrayList<File>();
			}
			allImageFiles.add(file);
		}
	}

	@Override
	public void generatePNGForImage(final File imagePath) throws DCMAException {
		try {
			thumbnailPNGCreator.generatePNGForImage(imagePath);
		} catch (Exception ex) {
			LOGGER.error(ERROR_IN_PNG_FILE_GENERATION, ex);
			throw new DCMAException(ERROR_IN_PNG_FILE_GENERATION, ex);
		}
	}

	@Override
	public void convertPdfToSinglePageTiffs(final BatchClass batchClass, final File imagePath, final BatchInstanceThread thread,
			final boolean isSinglePagePDF) throws DCMAException {
		try {
			multiPageToSinglePageConverter.convertPdfToSinglePageTiffs(batchClass, imagePath, null, thread, isSinglePagePDF);
		} catch (Exception e) {
			throw new DCMAException(e.getMessage());
		}
	}

	@Override
	public void convertPdfToSinglePageTiffs(BatchInstance batchInstance, File imagePath, BatchInstanceThread batchInstanceThread,
			final boolean isSinglePagePDF) throws DCMAException {
		try {
			multiPageToSinglePageConverter.convertPdfToSinglePageTiffs(batchInstance, imagePath, null, batchInstanceThread,
					isSinglePagePDF);
		} catch (Exception e) {
			throw new DCMAException(e.getMessage());
		}
	}

	@Override
	public void convertPdfToSinglePageTiffsUsingGSAPI(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread, final boolean isSinglePagePDF) throws DCMAException {
		try {
			multiPageToSinglePageConverter.convertPdfToSinglePageTiffsUsingGSAPI(inputParams, imagePath, outputParams, outputFilePath,
					thread, isSinglePagePDF);
		} catch (Exception e) {
			throw new DCMAException(e.getMessage());
		}
	}

	// New Method to generate PNGs using GSAPI
	public void convertPdfToSinglePagePNGOrTifUsingGSAPI(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread, final boolean isSinglePagePDF, final boolean isConvertTif, final boolean isDelete)
			throws DCMAException {
		try {
			multiPageToSinglePageConverter.convertPdfToSinglePagePNGOrTifUsingGSAPI(inputParams, imagePath, outputParams,
					outputFilePath, thread, isSinglePagePDF, isConvertTif, isDelete);
		} catch (Exception e) {
			throw new DCMAException(e.getMessage());
		}
	}

	@Override
	public void createSearchablePDF(String checkColorImage, String checkSearchableImage, String batchInstanceFolder, String[] pages,
			String documentId) throws DCMAApplicationException {
		try {
			hocrToPDFCreator.createPDFFromHOCR(checkColorImage, checkSearchableImage, batchInstanceFolder, pages, documentId);
		} catch (Exception e) {
			throw new DCMAApplicationException("Error in creating searchable pdf file" + e.getMessage(), e);
		}

	}

	@Override
	public void createTifToPDF(String pdfGeneratorEngine, String[] files, BatchInstanceThread pdfBatchInstanceThread,
			String inputParams, String outputParams) throws DCMAException {
		if (ImageMagicKConstants.IMAGE_MAGICK.equalsIgnoreCase(pdfGeneratorEngine)) {
			try {
				String inputFilePath = files[0];
				String outputFilePath = files[1];
				multiPageToSinglePageConverter.convertInputFileToOutputFileUsingIM(inputParams, inputFilePath, outputParams,
						outputFilePath, pdfBatchInstanceThread);
			} catch (Exception e) {
				throw new DCMAException("Error while generating output file", e);
			}
		} else if (ImageMagicKConstants.ITEXT.equalsIgnoreCase(pdfGeneratorEngine)) {
			try {
				iTextPDFCreator.createPDFUsingIText(files, pdfBatchInstanceThread);
			} catch (final DCMAApplicationException dcmaApplicationException) {
				throw new DCMAException(dcmaApplicationException.getMessage(), dcmaApplicationException);
			}
		}
	}

	@Override
	public void createSearchablePDF(String[] imageUrl, List<HocrPage> hocrPageList, String coloredPDF, String searchablePDF,
			String documentIdInt, String outputPDFFile, final String fontFilePath, final String iTextSearchablePDFType)
			throws DCMAApplicationException {
		try {
			iTextPDFCreator.createPDFUsingIText(imageUrl, hocrPageList, coloredPDF, searchablePDF, documentIdInt, outputPDFFile,
					fontFilePath, iTextSearchablePDFType);
		} catch (Exception e) {
			LOGGER.error(EphesoftStringUtil.concatenate("Unable to process web request. Internal Server Error", e.getMessage(), e));
			throw new DCMAApplicationException("Error in creating searchable pdf file" + e.getMessage(), e);
		}

	}
	
	@Override
	public void convertPdfToSinglePageTiffs(final String batchClassId, final File imageFile,
			final BatchInstanceThread batchInstanceThread, final boolean isSinglePagePDF) throws DCMAException {
		LOGGER.info(EphesoftStringUtil.concatenate("Entering conver pdf to single page tiffs with batch class id :", batchClassId));
		try {
			BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
			if (null != batchClass && imageFile.exists()) {
				convertPdfToSinglePageTiffs(batchClass, imageFile, batchInstanceThread, isSinglePagePDF);
			} else {
				throw new DCMAException(EphesoftStringUtil.concatenate("Either Batch class not found with identifer :", batchClassId,
						"or the image does not exists."));
			}
		} catch (DCMAException dcmaException) {
			LOGGER.error("Error occured in pdf to tiff conversion");
			throw new DCMAException("Error occured in pdf to tiff conversion", dcmaException);
		}
		LOGGER.info("Exiting conver pdf to single page tiffs...");
	}

	@Override
	public void convertPdfToSinglePagePNGOrTifUsingGSAPI(File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread, final boolean isSinglePagePdf, final boolean isConvertTiff, final boolean isDelete,
			String batchClassId) throws DCMAApplicationException {
		try {
			BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
			if (null != batchClass) {
				multiPageToSinglePageConverter.convertPdfToSinglePagePNGOrTifUsingGSAPI(batchClass, imagePath, outputParams,
						outputFilePath, thread, isSinglePagePdf, isConvertTiff, isDelete);
			} else {
				throw new DCMAApplicationException(EphesoftStringUtil.concatenate("Batch class not found with identifer :",
						batchClassId));
			}
		} catch (DCMAApplicationException dcmaException) {
			throw new DCMAApplicationException("Error occured in pdf to tiff conversion", dcmaException);
		}
	}

}
