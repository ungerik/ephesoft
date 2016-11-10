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
import java.util.List;

import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.ImageType;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.id.BatchInstanceID;

/**
 * This service provides image processing APIs. These APIs performs various operations like creation of thumbnail, rotation of images,
 * multipage tiff and PDF files creation, single page tiff creation from multipage.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jun 20, 2013 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see com.ephesoft.dcma.imagemagick.service.ImageProcessServiceImpl
 */
public interface ImageProcessService {

	/**
	 * This method creates OCR of input image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @param imageType {@link ImageType}
	 * @throws DCMAException
	 */
	void createOcrInputImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow, ImageType imageType)
			throws DCMAException;

	/**
	 * This method creates thumbnails of input image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	void createThumbnails(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method creates multi page files of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	void createMultiPageFiles(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method creates display image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	void createDisplayImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method classifies image of a batch for a particular plugin workflow.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAException
	 */
	void classifyImages(final BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException;

	/**
	 * This method rotates image of a batch for given document id and page id.
	 * 
	 * @param batchInstanceID {@link BatchInstanceID}
	 * @param documentId {@link String}
	 * @param pageId {@link String}
	 * @throws DCMAException
	 */
	void rotateImage(final BatchInstanceID batchInstanceID, String documentId, String pageId) throws DCMAException;

	/**
	 * This method rotates image at given path.
	 * 
	 * @param imagePath {@link String}
	 * @throws DCMAException
	 */
	void rotateImage(String imagePath) throws DCMAException;

	/**
	 * This method generates thumbnails and pngs for given image.
	 * 
	 * @param imagePath {@link File}
	 * @param thumbnailW {@link String}
	 * @param thumbnailH {@link String}
	 * @throws DCMAException
	 */
	void generateThumbnailsAndPNGsForImage(File imagePath, String thumbnailW, String thumbnailH) throws DCMAException;

	/**
	 * Generates thumbnail and corresponding JPG for the image passed as parameter.
	 * 
	 * <p>
	 * These generated images are used to display images on UI. Thus jpeg images takes less memory in comparison to other type images.
	 * The images may loose some information as they are converted to jpeg.
	 * 
	 * @param imagePath {@link String} path of image whose thumbnails and image needs to be generated. Cannot be NULL.
	 * @param thumbnailW {@link String} width of thumbnail image. Cannot be NULL.
	 * @param thumbnailH {@link String} height of thumbnail image. Cannot be NULL.
	 * @throws DCMAException if unable to generate images for the given file.
	 */
	void generateThumbnailsAndJPGsForImage(File imagePath, String thumbnailW, String thumbnailH) throws DCMAException;

	/**
	 * This method converts a given Multi page tiff into single page tiffs. Also depending upon the parameter allowPdfConversion it can
	 * convert a multi page pdf to single page tiffs too. The output files will be generated in the same folder.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param imagePath {@link File}
	 * @param outputFilePath (@link File}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param allowPdfConversion {@link Boolean}
	 * @throws DCMAException
	 */
	void convertPdfOrMultiPageTiffToTiff(BatchClass batchClass, File imagePath, File outputFilePath,
			BatchInstanceThread batchInstanceThread, Boolean allowPdfConversion, boolean deleteImage) throws DCMAException;

	/**
	 * This method converts a given Multi page tiff into single page tiffs. Also depending upon the parameter allowPdfConversion it can
	 * convert a multi page pdf to single page tiffs too. The output files will be generated in the same folder.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param imagePath {@link File}
	 * @param outputFilePath (@link File}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param allowPdfConversion {@link Boolean}
	 * @throws DCMAException
	 */
	void convertPdfOrMultiPageTiffToTiff(BatchInstance batchInstance, File imagePath, File outputFilePath,
			BatchInstanceThread batchInstanceThread, Boolean allowPdfConversion, boolean deleteImage) throws DCMAException;

	/**
	 * This method converts a given Multi page tiff into single page tiffs. Also depending upon the parameter allowPdfConversion it can
	 * convert a multi page pdf to single page tiffs too. The output files will be generated in the same folder.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param imagePath {@link File}
	 * @param outputFilePath (@link File}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param allowPdfConversion {@link Boolean}
	 * @throws DCMAException
	 */
	void convertPdfOrMultiPageTiffToTiff(BatchClass batchClass, File imagePath, File outputFilePath,
			BatchInstanceThread batchInstanceThread, Boolean allowPdfConversion) throws DCMAException;

	/**
	 * This method converts input tiff/pdf to output tiff file, using input and output params of batch class using imageMagick. The
	 * output files will be generated in the same folder.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param imagePath {@link File}
	 * @param outputFilePath (@link File}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @throws DCMAException
	 */
	void convertPdfOrMultiPageTiffToTiff(BatchClass batchClass, File imagePath, File outputFilePath,
			BatchInstanceThread batchInstanceThread) throws DCMAException;

	/**
	 * Method used for web services.
	 * 
	 * @param inputParams
	 * @param imagePath
	 * @param outputParams
	 * @param outputFilePath
	 * @param thread
	 * @throws DCMAException
	 */
	void convertPdfOrMultiPageTiffToTiffUsingIM(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread, boolean deleteImage) throws DCMAException;

	/**
	 * Method used for web services.
	 * 
	 * @param inputParams
	 * @param imagePath
	 * @param outputParams
	 * @param outputFilePath
	 * @param thread
	 * @throws DCMAException
	 */
	void convertPdfOrMultiPageTiffToTiffUsingIM(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread) throws DCMAException;

	/**
	 * This method converts all the pdf's and tiff's placed inside folder to single page tiff's. In case of a multi-page pdf single
	 * tiff's for each page will be created. The output files will be generated in the same folder.
	 * 
	 * @param batchClassID {@link BatchClassID}
	 * @param folderPath {@link String}
	 * @param testImageFile {@link File}
	 * @param isTestAdvancedKV {@link Boolean}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @return List<{@link File}> - file paths of all original files.
	 * @throws DCMAException
	 */
	List<File> convertPdfOrMultiPageTiffToTiff(BatchClassID batchClassID, String folderPath, File testImageFile,
			Boolean isTestAdvancedKV, BatchInstanceThread batchInstanceThread) throws DCMAException;

	/**
	 * Method to generate the PNG for a tiff file at the same location as input file
	 * 
	 * @param imagePath {@link File}
	 * @throws DCMAException
	 */
	void generatePNGForImage(final File imagePath) throws DCMAException;

	/**
	 * This method converts a given single page tiff to tiff. Also depending upon the parameter allowPdfConversion it can convert a
	 * single page pdf to single page tiff too. The output files will be generated in the same folder. Conversion is done by
	 * imagemagick.
	 * 
	 * @param batchClassID {@link BatchClassID}
	 * @param imagePath {@link File}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param allowPdfConversion {@link Boolean}
	 * @throws DCMAException
	 */
	/*
	 * void convertPdfOrSinglePageTiffToTiff(BatchClassID batchClassID, File imagePath, BatchInstanceThread batchInstanceThread,
	 * Boolean allowPdfConversion) throws DCMAException;
	 */

	/**
	 * This method converts a given pdf into tiff. The output files will be generated in the same folder.
	 * 
	 * @param batchClass {@link BatchClass}
	 * @param imagePath {@link File}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param isSinglePagePDF true if the pdf passed contains a single page only
	 * @throws DCMAException
	 */
	void convertPdfToSinglePageTiffs(BatchClass batchClass, File imagePath, BatchInstanceThread batchInstanceThread,
			final boolean isSinglePagePDF) throws DCMAException;

	/**
	 * This method converts a given pdf into tiff. The output files will be generated in the same folder.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param imagePath {@link File}
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param isSinglePagePDF true if the pdf passed contains a single page only
	 * @throws DCMAException
	 */
	void convertPdfToSinglePageTiffs(BatchInstance batchInstance, File imagePath, BatchInstanceThread batchInstanceThread,
			final boolean isSinglePagePDF) throws DCMAException;

	/**
	 * This method converts a given pdf into tiff. The output files will be generated in the output folder specified. Called from Web
	 * Service API.
	 * 
	 * @param inputParams
	 * @param imagePath
	 * @param outputParams
	 * @param outputFilePath
	 * @param thread
	 * @param isSinglePagePDF true if the pdf passed contains a single page only
	 * @throws DCMAException
	 */
	void convertPdfToSinglePageTiffsUsingGSAPI(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread, final boolean isSinglePagePDF) throws DCMAException;

	/**
	 * This API creates the searchable pdf for the image files on the basis of color image and searchable image at the output folder
	 * location.
	 * 
	 * @param checkColorImage {@link String}
	 * @param checkSearchableImage {@link String}
	 * @param inputFolderLocation {@link String}
	 * @param imageFiles String[]
	 * @param documentId {@link String}
	 */
	void createSearchablePDF(String checkColorImage, String checkSearchableImage, String inputFolderLocation, String[] imageFiles,
			String documentId) throws DCMAApplicationException;

	/**
	 * Method to create thumbnails for web service Image Classification API.
	 * 
	 * @param batchInstanceIdentifier
	 * @param folderPath
	 * @param sListOfTiffFiles
	 * @param outputImageParameters
	 * @param compareThumbnailH
	 * @param compareThumbnailW
	 * @return
	 * @throws DCMAException
	 */
	BatchInstanceThread createCompThumbForImage(String batchInstanceIdentifier, String folderPath, String[][] sListOfTiffFiles,
			String outputImageParameters, String compareThumbnailH, String compareThumbnailW) throws DCMAException;

	/**
	 * Method to perform image classification of PP module for web services
	 * 
	 * @param maxVal
	 * @param imMetric
	 * @param imFuzz
	 * @param batchInstanceIdentifier
	 * @param batchClassIdentifier
	 * @param sBatchFolder
	 * @param listOfPages
	 * @throws DCMAException
	 */
	void classifyImagesAPI(String maxVal, String imMetric, String imFuzz, String batchInstanceIdentifier, String batchClassIdentifier,
			String sBatchFolder, List<Page> listOfPages) throws DCMAException;

	/**
	 * API for creating pdf from single tif file.
	 * 
	 * @param pdfGeneratorEngine {@link String} pdfGeneration engine values can be ITEXT and IMAGE_MAGICK
	 * @param files {@link String[]} array of file size of 2 having input file absoulte path and output file absolute path.
	 * @param pdfBatchInstanceThread {@link BatchInstanceThread}
	 * @param inputParams {@link String} this parameter will used in case of IMAGE_MAGICK used as pdf generator engine
	 * @param outputParams {@link String} this parameter will used in case of IMAGE_MAGICK used as pdf generator engine
	 * @throws DCMAException if exception or error occurs during processing
	 */
	public void createTifToPDF(String pdfGeneratorEngine, String[] files, BatchInstanceThread pdfBatchInstanceThread,
			String inputParams, String outputParams) throws DCMAException;

	/**
	/**
	 * creates the searchable PDF using iTextPDFCreator. Output would be generated on stream.
	 * 
	 * @param imageUrl {@link String[]} is the absolute url of the image/tiff file
	 * @param hocrPageList {@link List<HocrPage>} is list of HOCR pages of image files
	 * @param coloredPDF {@link String} is indicator for colored PDF which can have values; True or False
	 * @param searchablePDF {@link String} is indicator for seachable PDF which can have values; True or False
	 * @param documentIdInt {@link String} is the name of the file for which searchable PDF is to be generated
	 * @param outputPDFFile {@link String} is the path of intermediate outputPDF which is copied at the end
	 * @param fontFilePath {@link String} is the path of font file
	 * @param iTextSearchablePDFType {@link String} can have values PDF or PDF-A which decides to generate normally or in advanced
	 *            manner respectively
	 * @throws DCMAApplicationException if error occurs while generating searchablePDF
	 */
	void createSearchablePDF(String[] imageUrl, List<HocrPage> hocrPageList, String coloredPDF, String searchablePDF,
			String documentIdInt, String outputPDFFile, String fontFilePath, String iTextSearchablePDFType)
			throws DCMAApplicationException;
	/*
	 * This method converts a given pdf into tiff. The output files will be generated in the same folder.
	 * 
	 * @param batchClass the batch class identifier
	 * @param imagePath {@link File} the iamge file
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param isSinglePagePDF true if the pdf passed contains a single page only
	 * @throws DCMAException if an error occurs during pdf to tiff conversion.
	 */
	void convertPdfToSinglePageTiffs(final String batchClassId, final File imageFile, final BatchInstanceThread batchInstanceThread,
			final boolean isSinglePagePDF) throws DCMAException;

	/**
	 * API to convert Pdf To Single Page PNG Or Tif Using GS
	 * 
	 * @param inputParams {@link String}
	 * @param imagePath {@link File}
	 * @param outputParams {@link String}
	 * @param outputFilePath {@link File}
	 * @param thread {@link BatchInstanceThread}
	 * @param isSinglePagePDF {@link boolean }
	 * @param isConvertTif {@link boolean}
	 * @param isDelete {@link boolean}
	 * @throws DCMAException
	 */
	void convertPdfToSinglePagePNGOrTifUsingGSAPI(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread, final boolean isSinglePagePDF, final boolean isConvertTif, final boolean isDelete)
			throws DCMAException;

	/**
	 * API to convert Pdf Or MultiPage Tiff To PNG Or Tiff Using IM
	 * 
	 * @param inputParams {@link String}
	 * @param imagePath {@link File}
	 * @param outputParams {@link String}
	 * @param outputFilePath {@link File}
	 * @param thread {@link BatchInstanceThread}
	 * @param deleteImage {@link boolean}
	 * @param isConvertToTif {@link boolean}
	 * @throws DCMAException
	 */
	void convertPdfOrMultiPageTiffToPNGOrTifUsingIM(String inputParams, File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread, boolean deleteImage, boolean isConvertToTif) throws DCMAException;

	void convertPdfToSinglePagePNGOrTifUsingGSAPI(File imagePath, String outputParams, File outputFilePath,
			BatchInstanceThread thread, final boolean isSinglePagePdf, final boolean isConvertTiff, final boolean isDelete,
			String batchClassId) throws DCMAApplicationException;
			
	void createMultiPageFilesAPI(String ghostscriptPdfParameters, String pdfOptimizationParams, String multipageTifSwitch,
			String toolName, String pdfOptimizationSwitch, String workingDir, String outputDir, List<File> singlePageFiles,
			String batchInstanceIdentifier) throws DCMAException;
}
