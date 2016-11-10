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

package com.ephesoft.dcma.imagemagick;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.imagemagick.impl.GhostScriptPDFCreator;
import com.ephesoft.dcma.imagemagick.impl.HOCRtoPDFCreator;
import com.ephesoft.dcma.imagemagick.impl.ITextPDFCreator;
import com.ephesoft.dcma.imagemagick.impl.ImageMagicKPDFCreator;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.IUtilCommonConstants;
import com.ephesoft.dcma.util.OSUtil;
import com.ephesoft.dcma.util.PDFUtil;
import com.ephesoft.dcma.util.TIFFUtil;

/**
 * This class is contains methods which can detect the batch.xml file in a folder and export it to the specified folder.
 * 
 * @author Ephesoft
 * 
 */
@Component
public class MultiPageTiffPdfCreator implements ICommonConstants {

	/**
	 * Variable for full stop.
	 */
	private static final char FULL_STOP = '.';

	/**
	 * Reference for LOGGER.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiPageTiffPdfCreator.class);

	/**
	 * Reference for batchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * List of commands for multipage tiff generation.
	 */
	private transient String tiffCmds;

	/**
	 * List of commands for unix for multipage tiff generation.
	 */
	private transient String unixTiffCmds;

	/**
	 * PDF compression to be set while creating multipage-pdf.
	 */
	private transient String tifCompression;

	/**
	 * generate the display png again or not.Default value: OFF.
	 */
	private transient String generateDisplayPng;

	/**
	 * Input image parameters for convert command
	 */
	private transient String inputParameters;

	/**
	 * Output image parameters for convert command
	 */
	private transient String outputParameters;

	/**
	 * Coloured output image parameters for convert command.
	 */
	private transient String coloredOutputParameters;

	/**
	 * Reference for ghostScriptPDFCreator.
	 */
	@Autowired
	private GhostScriptPDFCreator ghostScriptPDFCreator;

	/**
	 * Reference for imageMagicKPDFCreator.
	 */
	@Autowired
	private ImageMagicKPDFCreator imageMagicKPDFCreator;

	/**
	 * Reference for iTextPDFCreator.
	 */
	@Autowired
	private ITextPDFCreator iTextPDFCreator;

	/**
	 * Reference for hocrToPDFCreator.
	 */
	@Autowired
	private HOCRtoPDFCreator hocrToPDFCreator;

	/**
	 * Validating the number of page in document with output pdf.
	 */
	private transient String validateDocumentPage;

	/**
	 * Maximum number of files to be processed by to be processed by one Image magick command for multi page tiff creation.
	 */
	private transient int maxFilesProcessedPerIMCmd;

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @param ghostScriptPDFCreator the ghostScriptPDFCreator to set
	 */
	public void setGhostScriptPDFCreator(final GhostScriptPDFCreator ghostScriptPDFCreator) {
		this.ghostScriptPDFCreator = ghostScriptPDFCreator;
	}

	/**
	 * @param imageMagicKPDFCreator the imageMagicKPDFCreator to set
	 */
	public void setImageMagicKPDFCreator(final ImageMagicKPDFCreator imageMagicKPDFCreator) {
		this.imageMagicKPDFCreator = imageMagicKPDFCreator;
	}

	/**
	 * @param iTextPDFCreator the iTextPDFCreator to set
	 */
	public void setiTextPDFCreator(final ITextPDFCreator iTextPDFCreator) {
		this.iTextPDFCreator = iTextPDFCreator;
	}

	/**
	 * @param hocrToPDFCreator the hocrToPDFCreator to set
	 */
	public void setHocrToPDFCreator(final HOCRtoPDFCreator hocrToPDFCreator) {
		this.hocrToPDFCreator = hocrToPDFCreator;
	}

	/**
	 * @return the validateDocumentPage
	 */
	public String getValidateDocumentPage() {
		return validateDocumentPage;
	}

	/**
	 * @param validateDocumentPage the validateDocumentPage to set
	 */
	public void setValidateDocumentPage(final String validateDocumentPage) {
		this.validateDocumentPage = validateDocumentPage;
	}

	/**
	 * @return unix compatible tiff commands
	 */
	public String getUnixTiffCmds() {
		return unixTiffCmds;
	}

	/**
	 * @param unixTiffCmds unix compatible tiff commands
	 */
	public void setUnixTiffCmds(final String unixTiffCmds) {
		this.unixTiffCmds = unixTiffCmds;
	}

	/**
	 * @return the tiffCmds
	 */
	public String getTiffCmds() {
		String command = null;
		if (OSUtil.isWindows()) {
			command = tiffCmds;
		} else {
			command = unixTiffCmds;
		}
		return command;
	}

	/**
	 * @param tiffCmds the tiffCmds to set
	 */
	public void setTiffCmds(final String tiffCmds) {
		this.tiffCmds = tiffCmds;
	}

	/**
	 * @return the tifCompression
	 */
	public String getTifCompression() {
		return tifCompression;
	}

	/**
	 * @param tifCompression the pdfCompression to set
	 */
	public void setTifCompression(final String tifCompression) {
		this.tifCompression = tifCompression;
	}

	/**
	 * @param generateDisplayPng
	 */
	public void setGenerateDisplayPng(final String generateDisplayPng) {
		this.generateDisplayPng = generateDisplayPng;
	}

	/**
	 * @return generateDisplayPng
	 */
	public String getGenerateDisplayPng() {
		return generateDisplayPng;
	}

	/**
	 * @param inputParameters
	 */
	public void setInputParameters(final String inputParameters) {
		this.inputParameters = inputParameters;
	}

	/**
	 * @return inputParameters
	 */
	public String getInputParameters() {
		return inputParameters;
	}

	/**
	 * @param outputParameters
	 */
	public void setOutputParameters(final String outputParameters) {
		this.outputParameters = outputParameters;
	}

	/**
	 * @return outputParameters
	 */
	public String getOutputParameters() {
		return outputParameters;
	}

	/**
	 * Getter for max files processed by one IM command for multipage tiff creation.
	 * 
	 * @return
	 */
	public int getMaxFilesProcessedPerIMCmd() {
		return maxFilesProcessedPerIMCmd;
	}

	/**
	 * Setter for max files processed by one IM command for multipage tiff creation.
	 * 
	 * @param maxFilesProcessedPerIMCmd
	 */
	public void setMaxFilesProcessedPerIMCmd(final int maxFilesProcessedPerIMCmd) {
		this.maxFilesProcessedPerIMCmd = maxFilesProcessedPerIMCmd;
	}

	/**
	 * This method takes the FolderToBeExported path and creates multi page tiff and PDF files based on the batch.xml file found in the
	 * folder.
	 * 
	 * @param ghostscriptPdfParameters {@link String} ghost script pdf parameters.
	 * @param batchInstanceIdentifier {@link String} batch instance identifier.
	 * @param pdfOptimizationParams {@link String} pdf optimization parameters.
	 * @param checkPDFExportProcess {@link String} pdf export process parameters.
	 * @param pluginName {@link String} name of plugin.
	 * @param pdfOptimizationSwitch {@link String} pdf optimization switch.
	 * @param createMultipageFileType {@link String} file type for creating multipage files. Options: Tiff/PDF/Tiff and PDF.
	 * @param iTextSearchablePDFType {@link String} IText searchable pdf type (Pdf or pdf Advanced).
	 * @throws DCMAApplicationException if any error occurs while creating multi page tiff and pdf.
	 */
	public void createMultiPageFiles(final String ghostscriptPdfParameters, final String batchInstanceIdentifier,
			final String pdfOptimizationParams, final String checkPDFExportProcess, final String pluginName,
			final String pdfOptimizationSwitch, final String createMultipageFileType, final String rspFileName,
			final String iTextSearchablePDFType) throws DCMAApplicationException {
		LOGGER.info("Inside method createMultiPageFiles...");
		final String sFolderToBeExported = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceIdentifier;
		final Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		final File fFolderToBeExported = new File(sFolderToBeExported);

		if (!fFolderToBeExported.isDirectory()) {
			throw new DCMABusinessException(fFolderToBeExported.toString() + " is not a Directory.");
		}

		checkForUnknownDocument(batch);

		Map<String, List<File>> documentPageMap;
		try {
			documentPageMap = createDocumentPageMap(batch, batchInstanceIdentifier);
		} catch (final Exception e) {
			LOGGER.error("Error in creating document page map. " + e.getMessage(), e);
			// updateXMLFileFailiure(batchSchemaService, batch);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
		try {
			// New added property for version 3.1.
			createMultiPageTiffAndPDF(ghostscriptPdfParameters, documentPageMap, fFolderToBeExported, batch, batchInstanceIdentifier,
					checkPDFExportProcess, pluginName, pdfOptimizationParams, pdfOptimizationSwitch, createMultipageFileType,
					iTextSearchablePDFType);
		} catch (final Exception e) {
			LOGGER.error("Error in creating multi page tiff and pdf. " + e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
		batchSchemaService.updateBatch(batch);
	}

	/**
	 * This method takes the parameters to create multi page tiff and PDF files. This is used for web service API.
	 * 
	 * @param sFolderToBeExported
	 * @param batchInstanceIdentifier
	 * @param pluginName
	 * @throws DCMAApplicationException
	 */
	public void createMultiPageFilesAPI(final String ghostscriptPdfParameters, final String pdfOptimizationParams,
			final String multipageTifSwitch, final String toolName, final String pdfOptimizationSwitch, final String workingDir,
			final String outputDir, final List<File> singlePageFiles, final String batchInstanceIdentifier)
			throws DCMAApplicationException {
		LOGGER.info("Inside method createMultiPageFiles...");
		final File fFolderToBeExported = new File(outputDir);

		if (!fFolderToBeExported.isDirectory()) {
			throw new DCMABusinessException(fFolderToBeExported.toString() + " is not a Directory.");
		}

		try {
			createMultiPageTiffAndPDFAPI(ghostscriptPdfParameters, singlePageFiles, fFolderToBeExported, multipageTifSwitch, toolName,
					pdfOptimizationParams, pdfOptimizationSwitch, batchInstanceIdentifier, workingDir);
		} catch (final Exception e) {
			LOGGER.error("Error in creating multi page tiff and pdf. " + e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

	private void createMultiPageTiffAndPDFAPI(final String ghostscriptPdfParameters, final List<File> singlePageFiles,
			final File exportToFolder, final String multipageTifSwitch, final String toolName, final String pdfOptimizationParams,
			final String pdfOptimizationSwitch, final String batchInstanceIdentifier, final String workingDir)
			throws DCMAApplicationException {

		final String ghostScriptEnvVariable = System.getenv(IImageMagickCommonConstants.GHOSTSCRIPT_ENV_VARIABLE);
		if (ghostScriptEnvVariable == null || ghostScriptEnvVariable.isEmpty()) {
			LOGGER.info("ghostScriptEnvVariable is null or empty.");
			throw new DCMABusinessException("Enviornment Variable GHOSTSCRIPT_HOME not set.");
		}
		final String documentIdInt = "0";
		String documentName;
		String sTargetFileNameTif;
		File fTargetFileNameTif;
		String sTargetFileNamePdf = null;
		File fTargetFileNamePdf = null;
		String[] pages;
		String[] pdfPages;
		final BatchInstanceThread tifToPdfThread = new BatchInstanceThread(batchInstanceIdentifier);
		final BatchInstanceThread tifToTifThread = new BatchInstanceThread(batchInstanceIdentifier);
		final BatchInstanceThread pdfBatchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
		final BatchInstanceThread tifBatchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
		final BatchInstanceThread pdfOptimizationThread = new BatchInstanceThread(batchInstanceIdentifier);

		String tempFileName = null;
		final List<MultiPageExecutor> multiPageExecutorsTiff = new ArrayList<MultiPageExecutor>();
		final List<MultiPageExecutor> multiPageExecutorsPdf = new ArrayList<MultiPageExecutor>();
		final List<PdfOptimizer> pdfOptimizer = new ArrayList<PdfOptimizer>();
		LOGGER.info("pdf Optimization Switch value" + pdfOptimizationSwitch);
		try {
			documentName = IUtilCommonConstants.DOCUMENT_ID + documentIdInt + FileType.TIF.getExtensionWithDot();
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException("Problem getting file name", e);
		}
		sTargetFileNameTif = exportToFolder.getAbsolutePath() + File.separator + File.separator + documentName;
		fTargetFileNameTif = new File(sTargetFileNameTif);
		try {
			documentName = IUtilCommonConstants.DOCUMENT_ID + documentIdInt + FileType.PDF.getExtensionWithDot();
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException("Problem getting file name", e);
		}
		sTargetFileNamePdf = exportToFolder.getAbsolutePath() + File.separator + File.separator + documentName;
		fTargetFileNamePdf = new File(sTargetFileNamePdf);
		pages = new String[singlePageFiles.size() + 1];
		int index = 0;
		for (final File page : singlePageFiles) {
			pages[index] = page.getAbsolutePath();
			index++;
		}

		for (int pageIndex = 0; pageIndex < pages.length - 1; pageIndex++) {
			final File file = new File(pages[pageIndex]);
			tifToTifThread.add(new TifToTifCreator(file.getParent(), file.getName()));
		}
		for (int pageIndex = 0; pageIndex < pages.length - 1; pageIndex++) {
			final File file = new File(pages[pageIndex]);
			final TifToPDFCreator tifToPDFCreator = new TifToPDFCreator(file.getParent(), file.getName());
			tifToPdfThread.add(tifToPDFCreator);
		}
		pdfPages = new String[pages.length];
		pages[singlePageFiles.size()] = fTargetFileNameTif.getAbsolutePath();
		index = 0;
		for (final String page : pages) {
			if (page != null && !page.isEmpty()) {
				pdfPages[index++] = page.replace(page.substring(page.lastIndexOf(FULL_STOP)), FileType.PDF.getExtensionWithDot());
			}
		}
		// tifPages = new String[2];
		// tifPages[1] = fTargetFileNameTif.getAbsolutePath();
		if (multipageTifSwitch != null && multipageTifSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
			LOGGER.info("Adding command for multi page tiff execution");
			multiPageExecutorsTiff.add(new MultiPageExecutor(tifBatchInstanceThread, pages, tifCompression, maxFilesProcessedPerIMCmd,
					documentIdInt));
		}
		if (toolName != null && !toolName.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
			if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
				tempFileName = ImageMagicKConstants.TEMP_FILE_NAME + "_" + fTargetFileNamePdf.getName();
				pdfPages[singlePageFiles.size()] = workingDir + File.separator + tempFileName;
				pages[singlePageFiles.size()] = pdfPages[singlePageFiles.size()];
			} else {
				pages[singlePageFiles.size()] = fTargetFileNamePdf.getAbsolutePath();
				pdfPages[singlePageFiles.size()] = fTargetFileNamePdf.getAbsolutePath();
			}
		} else {
			pages[singlePageFiles.size()] = fTargetFileNamePdf.getAbsolutePath();
			pdfPages[singlePageFiles.size()] = fTargetFileNamePdf.getAbsolutePath();
		}
		LOGGER.info("toolName : " + toolName);
		LOGGER.info("Adding command for multi page pdf execution");

		// use this method to create pdf using itext API
		if (toolName != null && toolName.equalsIgnoreCase(ImageMagicKConstants.ITEXT)) {
			LOGGER.info("creating pdf using IText");
			iTextPDFCreator.createPDFUsingIText(pages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
		}
		// use this method to create pdf using ghost script command
		else if (toolName != null && toolName.equalsIgnoreCase(ImageMagicKConstants.GHOST_SCRIPT)) {
			// tifPages[0] = pdfPages[listofPages.size()];
			LOGGER.info("creating pdf using ghostscript command");

			ghostScriptPDFCreator.createPDFUsingGhostScript(ghostscriptPdfParameters, batchInstanceIdentifier,
					exportToFolder.getAbsolutePath(), pdfPages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
		}
		// use this default method to create pdf using image-magick convert command
		else if (toolName != null && toolName.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
			LOGGER.info("creating pdf using image-magick convert command");
			imageMagicKPDFCreator.createPDFUsingImageMagick(pages, pdfBatchInstanceThread, multiPageExecutorsPdf,
					pdfOptimizationSwitch);
		} else {
			LOGGER.info("creating pdf using Itext");
			iTextPDFCreator.createPDFUsingIText(pages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
		}

		// Fix for issue : ghostscript unable to optimise pdf produced by IM
		if (toolName != null && !toolName.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
			if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
				ghostScriptPDFCreator.createOptimizedPdfAPI(exportToFolder.getAbsolutePath(), pdfOptimizationParams,
						fTargetFileNamePdf, pdfOptimizationThread, tempFileName, pdfOptimizer);
			}
		}
		try {
			LOGGER.info("Executing commands for creation of muti page tiff and pdf using thread pool ..... ");
			tifToTifThread.execute();
			if (toolName != null && !ImageMagicKConstants.ITEXT.equalsIgnoreCase(toolName)) {
				tifToPdfThread.execute();
			}
			if (toolName != null && toolName.equalsIgnoreCase(ImageMagicKConstants.GHOST_SCRIPT)) {
				pdfBatchInstanceThread.setUsingGhostScript(Boolean.TRUE);
			}
			pdfBatchInstanceThread.execute();
			tifBatchInstanceThread.execute();
			LOGGER.info("Multipage tiff/pdf created .....");
		} catch (final DCMAApplicationException dcmae) {
			LOGGER.error("Error in executing command for multi page tiff and pdf using thread pool " + dcmae.getMessage(), dcmae);
			tifToTifThread.remove();
			tifToPdfThread.remove();
			pdfBatchInstanceThread.remove();
			tifBatchInstanceThread.remove();
			// Throw the exception to set the batch status to Error by Application aspect
			throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
		}
		try {
			LOGGER.info("Executing commands for optimizing multi page pdf using thread pool.");
			if (toolName != null && !toolName.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
				if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
					pdfOptimizationThread.execute();
				}
			}
		} catch (final DCMAApplicationException dcmae) {
			LOGGER.error("Error in executing command for optimizing multi page pdf using thread pool" + dcmae.getMessage(), dcmae);
			if (toolName != null && !toolName.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
				if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
					pdfOptimizationThread.remove();
				}
			}
			// Throw the exception to set the batch status to Error by Application aspect
			throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
		}

		Boolean checkDocumentPage = Boolean.FALSE;
		if (validateDocumentPage != null) {
			checkDocumentPage = Boolean.parseBoolean(validateDocumentPage);
		}
		// Copy the pdf file from working Dir to output Dir in case of GHOSTSCRIPT
		if (!fTargetFileNamePdf.exists()) {
			String inPdfFilePath = "";
			final File outDirFile = new File(sTargetFileNamePdf);
			if (toolName != null && !toolName.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
				if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
					new File(workingDir + File.separator + tempFileName).renameTo(outDirFile);
				} else {
					new File(workingDir + File.separator + fTargetFileNamePdf.getName()).renameTo(outDirFile);
				}
			} else {
				inPdfFilePath = workingDir + File.separator + fTargetFileNamePdf.getName();
				final File inDirFile = new File(inPdfFilePath);
				try {
					FileUtils.copyFile(inDirFile, outDirFile);
				} catch (final Exception e) {
					throw new DCMAApplicationException("Unable to copy file while processing." + e);
				}
			}
		}

		if (sTargetFileNamePdf != null && checkDocumentPage) {
			final int numberOfPDFPage = PDFUtil.getPDFPageCount(sTargetFileNamePdf);
			int numberOfTiffs = 0;
			for (final File page : singlePageFiles) {
				numberOfTiffs = numberOfTiffs + TIFFUtil.getTIFFPageCount(page.getAbsolutePath());
			}
			if (numberOfPDFPage != numberOfTiffs) {
				throw new DCMAApplicationException("Number of pages mismatched in multipage PDF and list of input files.");
			}
		}
	}

	/**
	 * Sanity check.Checks if there are still any unknown document in the batch.xml file. If any unknown document is found an exception
	 * is thrown.
	 * 
	 * @param pasrsedXMLFile
	 */
	private void checkForUnknownDocument(final Batch pasrsedXMLFile) {
		final Documents documents = pasrsedXMLFile.getDocuments();
		boolean valid = true;
		if (documents != null) {
			final List<Document> listOfDocuments = documents.getDocument();
			if (listOfDocuments == null) {
				valid = false;
			} else {
				for (final Document document : listOfDocuments) {
					if (document == null) {
						valid = false;
					}
					if (valid && document.getType().equalsIgnoreCase(EphesoftProperty.UNKNOWN.getProperty())) {
						final Pages pages = document.getPages();
						if (pages == null) {
							valid = false;
						} else {
							final List<Page> listOfPages = pages.getPage();
							if (listOfPages == null) {
								valid = false;
							}
							if (valid && listOfPages.isEmpty()) {
								valid = false;
							}
							throw new DCMABusinessException("Final xml document contains unknown documents. Cannot be exported.");
						}
					}
				}
			}
		}
	}

	/**
	 * This method generates the multi page tiff and pdf files using Image Magick(Wrapped using im4java)/GhostScript. The document
	 * classification is based on the documentPageMap.
	 * 
	 * @param ghostscriptPdfParameters {@link String} ghost script pdf parameters.
	 * @param documentPageMap {@link Map <String, List<File>>} document page map.
	 * @param exportToFolder {@link File} folder location.
	 * @param pasrsedXMLFile {@link Batch} batch xml.
	 * @param batchInstanceIdentifier {@link String} batch instance identifier.
	 * @param checkPDFExportProcess {@link String} pdf export process parameters.
	 * @param pluginName {@link String} name of plugin.
	 * @param pdfOptimizationParams {@link String} pdf optimization parameters.
	 * @param pdfOptimizationSwitch {@link String} pdf optimization switch.
	 * @param createMultipageFileType {@link String} multipage file type that has to be created.
	 * @param iTextSearchablePDFType {@link String} IText searchable pdf type (Pdf or pdf Advanced).
	 * @throws DCMAApplicationException
	 */
	private void createMultiPageTiffAndPDF(final String ghostscriptPdfParameters, final Map<String, List<File>> documentPageMap,
			final File exportToFolder, final Batch pasrsedXMLFile, final String batchInstanceIdentifier,
			final String checkPDFExportProcess, final String pluginName, final String pdfOptimizationParams,
			final String pdfOptimizationSwitch, final String createMultipageFileType, final String iTextSearchablePDFType)
			throws DCMAApplicationException {
		final String ghostScriptEnvVariable = System.getenv(IImageMagickCommonConstants.GHOSTSCRIPT_ENV_VARIABLE);
		if (ghostScriptEnvVariable == null || ghostScriptEnvVariable.isEmpty()) {
			LOGGER.info("ghostScriptEnvVariable is null or empty.");
			throw new DCMABusinessException("Enviornment Variable GHOSTSCRIPT_HOME not set.");
		}
		Set<String> documentNames;
		documentNames = documentPageMap.keySet();
		final Iterator<String> iterator = documentNames.iterator();
		String documentIdInt;
		String documentName;
		String sTargetFileNameTif;
		File fTargetFileNameTif;
		String sTargetFileNamePdf;
		File fTargetFileNamePdf = null;
		String[] pages;
		// String[] tifPages;
		String[] pdfPages;
		final BatchInstanceThread tifToPdfThread = new BatchInstanceThread(batchInstanceIdentifier);
		final BatchInstanceThread tifToTifThread = new BatchInstanceThread(batchInstanceIdentifier);
		final BatchInstanceThread pdfBatchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
		final BatchInstanceThread tifBatchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
		final BatchInstanceThread pdfOptimizationThread = new BatchInstanceThread(batchInstanceIdentifier);

		String tempFileName = null;
		final List<MultiPageExecutor> multiPageExecutorsTiff = new ArrayList<MultiPageExecutor>();
		final List<MultiPageExecutor> multiPageExecutorsPdf = new ArrayList<MultiPageExecutor>();
		final List<PdfOptimizer> pdfOptimizer = new ArrayList<PdfOptimizer>();
		LOGGER.info("pdf Optimization Switch value" + pdfOptimizationSwitch);
		while (iterator.hasNext()) {
			documentIdInt = iterator.next();
			LOGGER.info("Started creating multipage Tif and PDF for the " + "document with document id=" + documentIdInt);
			final List<File> listofPages = documentPageMap.get(documentIdInt);
			if (listofPages.size() == 0) {
				continue;
			}
			try {
				documentName = new FileNameFormatter().getMuliPageFileName(documentIdInt, batchInstanceIdentifier,
						FileType.TIF.getExtensionWithDot());
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
				throw new DCMAApplicationException("Problem getting file name", e);
			}
			sTargetFileNameTif = exportToFolder.getAbsolutePath() + File.separator + File.separator + documentName;
			fTargetFileNameTif = new File(sTargetFileNameTif);
			try {
				documentName = new FileNameFormatter().getMuliPageFileName(documentIdInt, batchInstanceIdentifier,
						FileType.PDF.getExtensionWithDot());
			} catch (final Exception e) {
				LOGGER.error(e.getMessage(), e);
				throw new DCMAApplicationException("Problem getting file name", e);
			}
			sTargetFileNamePdf = exportToFolder.getAbsolutePath() + File.separator + File.separator + documentName;
			fTargetFileNamePdf = new File(sTargetFileNamePdf);
			pages = new String[listofPages.size() + 1];
			int index = 0;
			for (final File page : listofPages) {
				pages[index] = page.getAbsolutePath();
				index++;
			}

			for (int pageIndex = 0; pageIndex < pages.length - 1; pageIndex++) {
				final File file = new File(pages[pageIndex]);
				tifToTifThread.add(new TifToTifCreator(file.getParent(), file.getName()));
			}

			pdfPages = new String[pages.length];
			pages[listofPages.size()] = fTargetFileNameTif.getAbsolutePath();
			index = 0;
			for (final String page : pages) {
				if (page != null && !page.isEmpty()) {
					pdfPages[index++] = page.replace(page.substring(page.lastIndexOf(FULL_STOP)), FileType.PDF.getExtensionWithDot());
				}
			}
			// If the file type given in Tiff/ Tiff and PDF then add commands to tiff batch instance thread.
			if (!FileType.PDF.getExtension().equalsIgnoreCase(createMultipageFileType)) {
				LOGGER.info("Adding command for multi page tiff execution");
				multiPageExecutorsTiff.add(new MultiPageExecutor(tifBatchInstanceThread, pages, tifCompression,
						maxFilesProcessedPerIMCmd, documentIdInt));
			}
			// If the file type given in PDF/ Tiff and PDF then add commands to tiff batch instance thread.
			if (!FileType.TIFF.getExtension().equalsIgnoreCase(createMultipageFileType)) {
				if (checkPDFExportProcess != null && !checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
					if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
						tempFileName = ImageMagicKConstants.TEMP_FILE_NAME + "_" + fTargetFileNamePdf.getName();
						pdfPages[listofPages.size()] = fTargetFileNamePdf.getParent() + File.separator + tempFileName;
						pages[listofPages.size()] = pdfPages[listofPages.size()];
					} else {
						pages[listofPages.size()] = fTargetFileNamePdf.getAbsolutePath();
						pdfPages[listofPages.size()] = fTargetFileNamePdf.getAbsolutePath();
					}
				} else {
					pages[listofPages.size()] = fTargetFileNamePdf.getAbsolutePath();
					pdfPages[listofPages.size()] = fTargetFileNamePdf.getAbsolutePath();
				}
				LOGGER.info("CheckPDFExportProcess : " + checkPDFExportProcess);
				LOGGER.info("Adding command for multi page pdf execution");

				// use this method to create pdf using itext API
				if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.ITEXT)) {
					LOGGER.info("creating pdf using IText");
					iTextPDFCreator.createPDFUsingIText(pages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
				}
				// use this method to create pdf using ghost script command
				else if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.GHOST_SCRIPT)) {
					LOGGER.info("creating pdf using ghostscript command");
					ghostScriptPDFCreator.createPDFUsingGhostScript(ghostscriptPdfParameters, batchInstanceIdentifier,
							exportToFolder.getAbsolutePath(), pdfPages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
				}
				// use this method to create pdf using hocrtopdf.jar
				else if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.HOCR_TO_PDF)) {
					LOGGER.info("creating pdf using hOCRToPDF");
					hocrToPDFCreator.createPDFFromHOCR(pasrsedXMLFile, batchInstanceIdentifier, exportToFolder.getAbsolutePath(),
							pages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
				}
				// use this default method to create pdf using image-magick convert command
				else if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
					LOGGER.info("creating pdf using image-magick convert command");
					imageMagicKPDFCreator.createPDFUsingImageMagick(pages, pdfBatchInstanceThread, multiPageExecutorsPdf,
							pdfOptimizationSwitch);
				} else if (checkPDFExportProcess != null
						&& checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.ITEXT_SEARCHABLE)) {
					LOGGER.info("creating pdf using ITEXT_SEARCHABLE");
					final String fontFilePath = batchSchemaService.getBaseFolderLocation() + File.separator
							+ ImageMagicKConstants.FONT_FOLDER_NAME + File.separator + ImageMagicKConstants.FONT_FILE_NAME;
					final File fontFile = new File(fontFilePath);
					if (!fontFile.exists()) {
						LOGGER.error("Font file not found. File required at path :" + fontFilePath);
						throw new DCMAApplicationException("Font file not found at required path.");
					}
					iTextPDFCreator.createPDFUsingIText(pages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt,
							pasrsedXMLFile, batchInstanceIdentifier, fontFilePath, iTextSearchablePDFType);

				} else {
					LOGGER.info("creating pdf using Itext");
					iTextPDFCreator.createPDFUsingIText(pages, pdfBatchInstanceThread, multiPageExecutorsPdf, documentIdInt);
				}

				// Fix for issue : ghostscript unable to optimise pdf produced by IM
				if (checkPDFExportProcess != null && !checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
					if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
						ghostScriptPDFCreator.createOptimizedPdf(batchInstanceIdentifier, pdfOptimizationParams, fTargetFileNamePdf,
								pdfOptimizationThread, tempFileName, pdfOptimizer);
					}
				}
			}
		}
		try {
			// code changes for version 3.1 features.
			LOGGER.info("Executing commands for creation of muti page tiff and pdf using thread pool ..... ");
			if (FileType.TIFF.getExtension().equalsIgnoreCase(createMultipageFileType)) {
				// Create only multipage tiff.
				tifToTifThread.execute();
				tifBatchInstanceThread.execute();
			} else if (FileType.PDF.getExtension().equalsIgnoreCase(createMultipageFileType)) {
				// Create only multipage pdf.
				if (checkPDFExportProcess != null && !ImageMagicKConstants.ITEXT.equalsIgnoreCase(checkPDFExportProcess)) {
					tifToPdfThread.execute();
				}
				if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.GHOST_SCRIPT)) {
					pdfBatchInstanceThread.setUsingGhostScript(Boolean.TRUE);
				}
				pdfBatchInstanceThread.execute();
			} else {
				// Create both multipage tiff and PDF.
				tifToTifThread.execute();
				if (checkPDFExportProcess != null && !ImageMagicKConstants.ITEXT.equalsIgnoreCase(checkPDFExportProcess)) {
					tifToPdfThread.execute();
				}
				if (checkPDFExportProcess != null && checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.GHOST_SCRIPT)) {
					pdfBatchInstanceThread.setUsingGhostScript(Boolean.TRUE);
				}
				pdfBatchInstanceThread.execute();
				tifBatchInstanceThread.execute();
			}
			LOGGER.info("Multipage tiff/pdf created .....");
		} catch (final DCMAApplicationException dcmae) {
			LOGGER.error("Error in executing command for multi page tiff and pdf using thread pool " + dcmae.getMessage(), dcmae);
			tifToTifThread.remove();
			tifToPdfThread.remove();
			pdfBatchInstanceThread.remove();
			tifBatchInstanceThread.remove();
			// Throw the exception to set the batch status to Error by Application aspect
			throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
		}
		try {
			LOGGER.info("Executing commands for optimizing multi page pdf using thread pool.");
			// If PDF is created then perform pdf optimization.
			if (!FileType.TIFF.getExtension().equalsIgnoreCase(createMultipageFileType) && checkPDFExportProcess != null
					&& !checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
				if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
					pdfOptimizationThread.execute();
				}
			}
		} catch (final DCMAApplicationException dcmae) {
			LOGGER.error("Error in executing command for optimizing multi page pdf using thread pool" + dcmae.getMessage(), dcmae);
			if (checkPDFExportProcess != null && !checkPDFExportProcess.equalsIgnoreCase(ImageMagicKConstants.IMAGE_MAGICK)) {
				if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
					pdfOptimizationThread.remove();
				}
			}
			// Throw the exception to set the batch status to Error by Application aspect
			throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
		}

		updateMultiPageTifToXml(documentPageMap, pasrsedXMLFile, createMultipageFileType, documentNames, multiPageExecutorsTiff,
				multiPageExecutorsPdf);

		Boolean checkDocumentPage = Boolean.FALSE;
		if (validateDocumentPage != null) {
			checkDocumentPage = Boolean.parseBoolean(validateDocumentPage);
		}

		// If PDF created then verify document vs PDF page count.
		if (!FileType.TIFF.getExtension().equalsIgnoreCase(createMultipageFileType) && checkDocumentPage) {
			validatingDocumentVsPdfPage(pasrsedXMLFile, batchInstanceIdentifier);
		}
	}

	private void validatingDocumentVsPdfPage(final Batch batch, final String batchInstanceIdentifier) throws DCMAApplicationException {
		final List<Document> documentList = batch.getDocuments().getDocument();
		final String systemPath = batch.getBatchLocalPath() + File.separator + batch.getBatchInstanceIdentifier();
		if (documentList != null && documentList.size() > 0) {
			for (final Document document : documentList) {
				final List<Page> pageList = document.getPages().getPage();
				if (pageList != null) {
					final int numberOfDocumentPage = pageList.size();
					final String pdfFilePath = systemPath + File.separator + document.getMultiPagePdfFile();
					final int numberOfPDFPage = PDFUtil.getPDFPageCount(pdfFilePath);
					if (numberOfDocumentPage != numberOfPDFPage) {
						throw new DCMAApplicationException("Number of pages mismatched in multipage PDF and batch xml for document "
								+ document.getIdentifier() + " for batch.Batch Instance ID:" + batchInstanceIdentifier);
					}
				}
			}
		}
	}

	/**
	 * Updation of batch xml for plugin execution details.
	 * 
	 * @param documentPageMap {@link Map<String, List<File>>} map for containing pages.
	 * @param pasrsedXMLFile {@link Batch} batch xml file.
	 * @param createMultipageFileType {@link String} multipage file type that has to be created.
	 * @param documentNames {@link Set<String>} name of documents mapped in batch xml.
	 * @param multiPageExecutorsTiff {@link List<MultiPageExecutor>} list of {@link MultiPageExecutor} for tiff.
	 * @param multiPageExecutorsPdf {@link List<MultiPageExecutor>} list of {@link MultiPageExecutor} for pdf.
	 */
	private void updateMultiPageTifToXml(final Map<String, List<File>> documentPageMap, final Batch pasrsedXMLFile,
			final String createMultipageFileType, final Set<String> documentNames,
			final List<MultiPageExecutor> multiPageExecutorsTiff, final List<MultiPageExecutor> multiPageExecutorsPdf) {
		if (FileType.TIFF.getExtension().equalsIgnoreCase(createMultipageFileType)) {
			// updating only for tiff.
			updateMultiPageTiffToBatchXML(documentPageMap, pasrsedXMLFile, documentNames, multiPageExecutorsTiff);
		} else if (FileType.PDF.getExtension().equalsIgnoreCase(createMultipageFileType)) {
			// updating only for pdf.
			updateMultiPagePDFToBatchXML(pasrsedXMLFile, multiPageExecutorsPdf);
		} else {
			// update multipage tiff.
			updateMultiPageTiffToBatchXML(documentPageMap, pasrsedXMLFile, documentNames, multiPageExecutorsTiff);
			// update multipage pdf.
			updateMultiPagePDFToBatchXML(pasrsedXMLFile, multiPageExecutorsPdf);
		}

		LOGGER.info("Processing complete at " + new Date());
	}

	/**
	 * Updating batch xml for mutipage pdf creation executed parameters.
	 * 
	 * @param pasrsedXMLFile {@link Batch} parsed batch xml.
	 * @param multiPageExecutorsPdf {@link List<MultiPageExecutor>} list of {@link MultiPageExecutor} for pdf.
	 */
	private void updateMultiPagePDFToBatchXML(final Batch pasrsedXMLFile, final List<MultiPageExecutor> multiPageExecutorsPdf) {
		LOGGER.info("Updating files for multi page pdf");
		for (final MultiPageExecutor multiPageExecutor : multiPageExecutorsPdf) {
			updateMultiPageTifToXmlObject(multiPageExecutor.getPages(), pasrsedXMLFile);
		}
	}

	/**
	 * Updating batch xml for mutipage tiff creation executed parameters.
	 * 
	 * @param documentPageMap {@link Map <String, List<File>>} map for page per document.
	 * @param pasrsedXMLFile {@link Batch} parsed batch xml.
	 * @param documentNames {@link Set <String>} name of document mapped in batch xml.
	 * @param multiPageExecutorsTiff {@link List <MultiPageExecutor>} list of {@link MultiPageExecutor} for tiff.
	 */
	private void updateMultiPageTiffToBatchXML(final Map<String, List<File>> documentPageMap, final Batch pasrsedXMLFile,
			final Set<String> documentNames, final List<MultiPageExecutor> multiPageExecutorsTiff) {
		String documentIdInt;
		LOGGER.info("Updating files for multi page tiff");
		final Iterator<String> iter = documentNames.iterator();
		for (final MultiPageExecutor multiPageExecutor : multiPageExecutorsTiff) {
			final String[] pageArray = multiPageExecutor.getPages();
			if (iter.hasNext()) {
				documentIdInt = iter.next();
				final List<File> listofPages = documentPageMap.get(documentIdInt);
				final String[] pages = new String[listofPages.size() + 1];
				int index = 0;
				for (final File page : listofPages) {
					pages[index++] = page.getAbsolutePath();
				}
				pages[index] = pageArray[pageArray.length - 1];
				updateMultiPageTifToXmlObject(pages, pasrsedXMLFile);
			}
		}
	}

	/**
	 * This method updates the Unmarsheled xml file with the information of multi page pdf or tif file.
	 * 
	 * @param pages
	 * @param pasrsedXMLFile
	 */
	private void updateMultiPageTifToXmlObject(final String[] pages, final Batch pasrsedXMLFile) {
		LOGGER.info("Started updating multi page tiff/pdf file in batch xml.");
		final String newFileName = new File(pages[0]).getName();
		String multiPageFileName = new File(pages[pages.length - 1]).getName();
		final String tempPrefix = ImageMagicKConstants.TEMP_FILE_NAME + "_";
		if (multiPageFileName.startsWith(tempPrefix)) {
			multiPageFileName = multiPageFileName.substring(tempPrefix.length());
		}
		final Documents documents = pasrsedXMLFile.getDocuments();
		if (documents != null) {
			final List<Document> listOfDocuments = documents.getDocument();
			if (listOfDocuments != null) {
				LOGGER.info("Number of documents in the batch." + listOfDocuments.size());
				for (final Document document : listOfDocuments) {
					final Pages pagesObj = document.getPages();
					if (pagesObj != null) {
						final List<Page> listOfPages = pagesObj.getPage();
						if (listOfPages != null) {
							LOGGER.info("Number of pages in document " + document.getIdentifier() + " are - " + listOfDocuments.size()
									+ ".");
							for (final Page page : listOfPages) {
								if (page != null) {
									final String fileName = page.getNewFileName();
									if (fileName.equals(newFileName)
											|| fileName.substring(0, fileName.lastIndexOf('.')).equals(
													newFileName.substring(0, newFileName.lastIndexOf('.')))) {
										LOGGER.info("Multi page file name is " + multiPageFileName);
										if (multiPageFileName.endsWith(FileType.PDF.getExtensionWithDot())) {
											document.setMultiPagePdfFile(multiPageFileName);
										}
										if (multiPageFileName.endsWith(FileType.TIF.getExtensionWithDot())) {
											document.setMultiPageTiffFile(multiPageFileName);
										}
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * This method creates a Map containing the Document id as the key and the a list of pages as the value. This map is created on the
	 * basis of the batch.xml file information of which is received by this method as the pasrsedXMLFile object
	 * 
	 * @param pasrsedXMLFile OO representation of the xml file
	 * @param batchInstanceID
	 * @return Map
	 */
	private Map<String, List<File>> createDocumentPageMap(final Batch pasrsedXMLFile, final String batchInstanceID) {

		final List<Document> xmlDocuments = pasrsedXMLFile.getDocuments().getDocument();
		final HashMap<String, List<File>> documentPageMap = new HashMap<String, List<File>>();

		for (final Document document : xmlDocuments) {
			final String documentId = document.getIdentifier();
			final List<Page> listOfPages = document.getPages().getPage();
			final List<File> listOfFiles = new LinkedList<File>();
			LOGGER.info("Document documentid =" + documentId + " contains the following pages:");
			for (final Page page : listOfPages) {
				final String sImageFile = page.getNewFileName();
				LOGGER.info("Page File Name:" + sImageFile);
				final File fImageFile = batchSchemaService.getFile(batchInstanceID, sImageFile);
				if (fImageFile.exists()) {
					listOfFiles.add(fImageFile);
				} else {
					throw new DCMABusinessException("File does not exist File Name=" + fImageFile);
				}
			}
			documentPageMap.put(documentId, listOfFiles);
		}
		return documentPageMap;
	}

	/**
	 * Returns colored output parameter.
	 * 
	 * @return {@link String}
	 */
	public String getColoredOutputParameters() {
		return coloredOutputParameters;
	}

	/**
	 * Sets colored output parameter.
	 * 
	 * @param coloredOutputParameters {@link String}
	 */
	public void setColoredOutputParameters(String coloredOutputParameters) {
		this.coloredOutputParameters = coloredOutputParameters;
	}
}
