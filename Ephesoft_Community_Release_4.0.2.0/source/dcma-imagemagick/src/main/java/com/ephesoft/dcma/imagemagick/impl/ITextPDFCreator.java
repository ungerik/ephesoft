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

package com.ephesoft.dcma.imagemagick.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.MultiPageExecutor;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileType;
import com.ephesoft.dcma.util.FileUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.io.FileChannelRandomAccessSource;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;

/**
 * This class is used to create the pdf file using IText API.
 * 
 * @author Ephesoft
 */
public class ITextPDFCreator {

	/**
	 * Variable for logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ITextPDFCreator.class);

	private static final String SWITCH_VALUE_TRUE = "true";

	/**
	 * Environment variable which contains the path of the itext license file.
	 */
	private static final String ENV_ITEXT_LICENSE_FILE = "ITEXT_LICENSE_FILE";

	/**
	 * boolean that determines that license file is already loaded or not.
	 */
	private static boolean isItextLicenseLoaded = false;

	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Variable for pdf height.
	 */
	private transient String heightOfPdfPage;

	/**
	 * Variable for pdf width.
	 */
	private transient String widthOfPdfPage;

	/**
	 * Variable for conversion parameters of image before iText pdf formation.
	 */
	private transient String convertParameters;

	/**
	 * @param heightOfPdfPage the heightOfPdfPage to set.
	 */
	public void setHeightOfPdfPage(String heightOfPdfPage) {
		this.heightOfPdfPage = heightOfPdfPage;
	}

	/**
	 * @param widthOfPdfPage the widthOfPdfPage to set.
	 */
	public void setWidthOfPdfPage(String widthOfPdfPage) {
		this.widthOfPdfPage = widthOfPdfPage;
	}

	/**
	 * Reference for pluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Set the convert command parameters for image before iText Creation
	 * 
	 * @param convertParameters The convert command parameters for image before iText Creation
	 */
	public void setConvertParameters(String convertParameters) {
		this.convertParameters = convertParameters;
	}

	/**
	 * Get the convert command parameters for image before iText Creation
	 * 
	 * @return The convert command parameters for image before iText Creation
	 */
	public String getConvertParameters() {
		return convertParameters;
	}

	/**
	 * Verifies whether the license of the object is valid or not.
	 * 
	 * @return <code>true</code> if a valid license is already loaded else false.
	 * @throws DCMAApplicationException : when could not read the license key file or an invalid license file is loaded.
	 */
	private boolean isValidItextLicense() throws DCMAApplicationException {
		return true;
	}

	/**
	 * API for creating the PDF using IText.
	 * 
	 * @param pages
	 * @param batchInstanceThread
	 * @param multiPageExecutors
	 * @param documentIdInt
	 * @throws DCMAApplicationException If itext license is invalid or could not generate the pdf because of some internal error.
	 */
	public void createPDFUsingIText(String[] pages, BatchInstanceThread batchInstanceThread,
			List<MultiPageExecutor> multiPageExecutors, String documentIdInt) throws DCMAApplicationException {
		// Creates a PDF with a valid license only.
		if (isValidItextLicense()) {
			createNonSearchablePDF(pages, batchInstanceThread, multiPageExecutors);
		}
	}

	/**
	 * API for creating the PDF using IText.
	 * 
	 * @param pages
	 * @param batchInstanceThread
	 * @param multiPageExecutors
	 * @param documentIdInt
	 * @param fontFilePath {@link String} specifies the path of font file.
	 * @param iTextSearchablePDFType {@link String} IText searchable pdf type (Pdf or pdf Advanced).
	 * @throws DCMAApplicationException : If itext license is invalid or could not generate the pdf because of some internal error.
	 */
	public void createPDFUsingIText(String[] pages, BatchInstanceThread batchInstanceThread,
			List<MultiPageExecutor> multiPageExecutors, String documentIdInt, Batch batchXml, String batchInstanceIdentifier,
			final String fontFilePath, final String iTextSearchablePDFType) throws DCMAApplicationException {
		if (isValidItextLicense()) {
			createSearchablePDF(pages, batchInstanceThread, multiPageExecutors, documentIdInt, batchXml, batchInstanceIdentifier,
					fontFilePath, iTextSearchablePDFType);
		}
	}

	/**
	 * @param pages
	 * @param batchInstanceThread
	 * @param multiPageExecutors
	 */
	private void createNonSearchablePDF(String[] pages, BatchInstanceThread batchInstanceThread,
			List<MultiPageExecutor> multiPageExecutors) {
		LOGGER.info("Adding command for multi page pdf execution");
		int widthOfPdfPageInt = getWidthOfPdfPageInt();
		int heightOfPdfPageInt = getHeightOfPdfPageInt();
		multiPageExecutors.add(new MultiPageExecutor(batchInstanceThread, pages, widthOfPdfPageInt, heightOfPdfPageInt));
	}

	/**
	 * @param pages
	 * @param batchInstanceThread
	 * @param multiPageExecutors
	 * @param documentIdInt
	 * @param batchXml
	 * @param batchInstanceIdentifier
	 * @param widthOfLine
	 * @param fontFilePath {@link String} specifies the path of font file.
	 * @param iTextSearchablePDFType {@link String} IText searchable pdf type (Pdf or pdf Advanced).
	 */
	private void createSearchablePDF(String[] pages, BatchInstanceThread batchInstanceThread,
			List<MultiPageExecutor> multiPageExecutors, String documentIdInt, Batch batchXml, String batchInstanceIdentifier,
			final String fontFilePath, final String iTextSearchablePDFType) {
		LOGGER.info("Adding command for multi page searchable pdf execution for batch : " + batchInstanceIdentifier + " and document: "
				+ documentIdInt);
		Map<String, HocrPage> imageHtmlMap = null;
		final boolean isColoredPDF = SWITCH_VALUE_TRUE.equalsIgnoreCase(pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CHECK_COLOURED_PDF));
		boolean isSearchablePDF = SWITCH_VALUE_TRUE.equalsIgnoreCase(pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				ImageMagicKConstants.CREATEMULTIPAGE_FILES_PLUGIN, ImageMagicProperties.CHECK_SEARCHABLE_PDF));
		String pdfFilePath = null;
		if (null != pages && pages.length > 0) {
			final int lastIndex = pages.length - 1;
			imageHtmlMap = new LinkedHashMap<String, HocrPage>(lastIndex);
			for (int i = 0; i < lastIndex; i++) {
				imageHtmlMap.put(pages[i], (isSearchablePDF) ? getHOCRFileForImage(batchXml, pages[i]) : null);
			}
			pdfFilePath = pages[lastIndex];
		}
		multiPageExecutors.add(new MultiPageExecutor(batchInstanceThread, imageHtmlMap, isColoredPDF, isSearchablePDF, pdfFilePath,
				pages, fontFilePath, convertParameters, iTextSearchablePDFType));
	}

	/**
	 * This API will create the pdf using array of pages provided in it.
	 * 
	 * @param pages
	 * @param batchInstanceThread
	 * @throws DCMAApplicationException If itext license is invalid.
	 */
	public void createPDFUsingIText(String[] pages, BatchInstanceThread batchInstanceThread) throws DCMAApplicationException {
		LOGGER.info("Adding command for multi page pdf execution");
		if (isValidItextLicense()) {
			int widthOfPdfPageInt = getWidthOfPdfPageInt();
			int heightOfPdfPageInt = getHeightOfPdfPageInt();
			new MultiPageExecutor(batchInstanceThread, pages, widthOfPdfPageInt, heightOfPdfPageInt);
		}
	}

	/**
	 * API for getting the height for output pdf.
	 * 
	 * @return
	 */
	private int getHeightOfPdfPageInt() {
		int heightOfPdfPageInt = ImageMagicKConstants.DEFAULT_PDF_PAGE_HEIGHT;
		try {
			heightOfPdfPageInt = Integer.parseInt(heightOfPdfPage);
		} catch (NumberFormatException nfe) {
			LOGGER.info("Invalid or no value for heightOfPdfPage specified in properties file. Setting it its default value 792.");
		}
		return heightOfPdfPageInt;
	}

	/**
	 * API for getting the width for output pdf.
	 * 
	 * @return
	 */
	private int getWidthOfPdfPageInt() {
		int widthOfPdfPageInt = ImageMagicKConstants.DEFAULT_PDF_PAGE_WIDTH;
		try {
			widthOfPdfPageInt = Integer.parseInt(widthOfPdfPage);
		} catch (NumberFormatException nfe) {
			LOGGER.info("Invalid or no value for heightOfPdfPage specified in properties file. Setting it its default value 792.");
		}
		return widthOfPdfPageInt;
	}

	/**
	 * API for getting HOCR file name for specified image.
	 * 
	 * @param batchXML
	 * @param imageName
	 * @return
	 */
	private HocrPage getHOCRFileForImage(Batch batchXML, String imageName) {
		List<Document> xmlDocuments = batchXML.getDocuments().getDocument();
		HocrPages hocrPages = null;
		HocrPage hocrPage = null;
		List<HocrPage> hocrPageList = null;
		for (Document document : xmlDocuments) {
			List<Page> listOfPages = document.getPages().getPage();
			for (Page page : listOfPages) {
				String fileName = imageName.substring(imageName.lastIndexOf(File.separator) + 1, imageName.length());
				if (fileName != null && fileName.equalsIgnoreCase(page.getNewFileName())) {
					hocrPages = batchSchemaService.getHocrPages(batchXML.getBatchInstanceIdentifier(), page.getHocrFileName());
					if (hocrPages != null) {
						hocrPageList = hocrPages.getHocrPage();
						if (!hocrPageList.isEmpty()) {
							hocrPage = hocrPageList.get(0);
							break;
						}
					}
				}
			}
		}
		return hocrPage;
	}

	/**
	 * specifies width of the page for generating PDF using iTextPDFCreator
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
	 * @throws DCMAApplicationException if some problem occurs while generating searchable PDF through iTextPDFCreator or its license
	 *             is invalid.
	 */
	public void createPDFUsingIText(String[] imageUrl, List<HocrPage> hocrPageList, final String coloredPDF,
			final String searchablePDF, final String documentIdInt, final String outputPDFFile, final String fontFilePath,
			final String iTextSearchablePDFType) throws DCMAApplicationException {
		if (isValidItextLicense()) {
			createSearchablePDF(imageUrl, hocrPageList, coloredPDF, searchablePDF, documentIdInt, outputPDFFile, fontFilePath,
					iTextSearchablePDFType);
		}
	}

	/**
	 * creates ImageURL- {@link HocrPage } maps for generating PDF using iTextPDFCreator
	 * 
	 * @param imageUrl {@link String[]} is the absolute url of the image/tiff file
	 * @param hocrPageList {@link List<HocrPage>} is list of HOCR pages of image files
	 * @param coloredPDF {@link String} is indicator for colored PDF which can have values @value True or False
	 * @param searchablePDF {@link String} is indicator for seachable PDF which can have values; True or False
	 * @param documentIdInt {@link String} is the name of the file for which searchable PDF is to be generated
	 * @param outputPDFFile {@link String} is the path of intermediate outputPDF which is copied at the end
	 * @param fontFilePath {@link String} is the path of font file
	 * @param iTextSearchablePDFType {@link String} can have values PDF or PDF-A which decides to generate normally or in advanced
	 *            manner respectively
	 * @throws DCMAApplicationException if some problem occurs while generating searchable PDF through iTextPDFCreator
	 */

	private void createSearchablePDF(String[] imageUrl, List<HocrPage> hocrPageList, final String coloredPDF,
			final String searchablePDF, final String documentIdInt, final String outputPDFFile, final String fontFilePath,
			final String iTextSearchablePDFType) throws DCMAApplicationException {

		LOGGER.info("Adding command for multi page searchable pdf execution for document: ");

		Map<String, HocrPage> imageHocrMap = null;
		final boolean isColoredPDF = Boolean.valueOf(coloredPDF);
		final boolean isSearchablePDF = Boolean.valueOf(searchablePDF);
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
		final List<MultiPageExecutor> multiPageExecutors = new ArrayList<MultiPageExecutor>();

		try {
			if (imageUrl != null && imageUrl.length > 0) {

				imageHocrMap = new LinkedHashMap<String, HocrPage>(0);
				imageHocrMap.put(imageUrl[0], hocrPageList.get(0));
			}
			multiPageExecutors.add(new MultiPageExecutor(batchInstanceThread, imageHocrMap, isColoredPDF, isSearchablePDF,
					outputPDFFile, imageUrl, fontFilePath, convertParameters, iTextSearchablePDFType));
			batchInstanceThread.execute();
		} catch (Exception e) {
			LOGGER.error(EphesoftStringUtil.concatenate("Exception while generating PDF", e.getMessage(), e));
			batchInstanceThread.remove();
			throw new DCMAApplicationException(EphesoftStringUtil.concatenate("Exception while generating PDF", e.getMessage(), e));
		}

	}

	/**
	 * Converts specified tiff file into pdf.
	 * 
	 * @param tiffFile{@link File} to be converted into pdf.
	 * @throws DCMAApplicationException if any error occurs while conversion.
	 */
	public static void convertTiffIntoPdf(final File tiffFile) throws DCMAApplicationException {
		if (null == tiffFile) {
			LOGGER.error("Unable to convert tiff file as specified file is null.");
		} else {
			com.itextpdf.text.Document document = null;
			RandomAccessFile randomAccessFile = null;
			RandomAccessFileOrArray randomAccessFileOrArray = null;
			FileChannelRandomAccessSource fileChannelRandomAccessSource = null;
			try {
				final String tiffFilePath = tiffFile.getAbsolutePath();
				if (tiffFilePath.endsWith(FileType.TIF.getExtensionWithDot())
						|| tiffFilePath.endsWith(FileType.TIFF.getExtensionWithDot())) {
					randomAccessFile = new RandomAccessFile(tiffFile, ICommonConstants.READ_MODE);
					fileChannelRandomAccessSource = new FileChannelRandomAccessSource(randomAccessFile.getChannel());
					document = new com.itextpdf.text.Document();
					final int lastIndexofTiffExtension = tiffFilePath.toLowerCase().lastIndexOf(FileType.TIF.getExtensionWithDot());
					PdfWriter.getInstance(document, new FileOutputStream(EphesoftStringUtil.concatenate(tiffFilePath.substring(0,
							lastIndexofTiffExtension), FileType.PDF.getExtensionWithDot())));
					document.open();
					randomAccessFileOrArray = new RandomAccessFileOrArray(fileChannelRandomAccessSource);
					final int pageCount = TiffImage.getNumberOfPages(randomAccessFileOrArray);
					Image image;
					for (int index = 1; index <= pageCount; index++) {
						image = TiffImage.getTiffImage(randomAccessFileOrArray, index);
						final Rectangle pageSize = new Rectangle(image.getWidth(), image.getHeight());
						document.setPageSize(pageSize);
						document.newPage();
						document.add(image);
					}
					LOGGER.info(EphesoftStringUtil.concatenate(tiffFilePath, " successfully converted into PDF."));
				} else {
					LOGGER.error("Unable to convert as specified file is not a valid tiff file.");
				}
			} catch (final DocumentException e) {
				LOGGER.error("DocumentException is occurred while processing specified tiff file for conversion.");
				throw new DCMAApplicationException(EphesoftStringUtil.concatenate("DocumentException occured while generating PDF", e
						.getMessage()), e);
			} catch (final IOException e) {
				LOGGER.error("IOException is occurred while processing specified tiff file for conversion.");
				throw new DCMAApplicationException(EphesoftStringUtil.concatenate("IOException occured while generating PDF", e
						.getMessage()), e);
			} finally {
				FileUtils.closeStream(randomAccessFileOrArray);
				FileUtils.closeFileChannelRandomAccessSource(fileChannelRandomAccessSource);
				FileUtils.closeResource(randomAccessFile);
				document.close();
			}

		}
	}
}
