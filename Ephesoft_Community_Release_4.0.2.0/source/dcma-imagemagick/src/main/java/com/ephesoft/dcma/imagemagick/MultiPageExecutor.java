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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.AbstractRunnable;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.EphesoftProcessExecutor;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.OSUtil;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;

/**
 * Class is used for creating multipage tiff and pdf using ImageMagik, GhostScript and iText. This class provides functionalities that
 * process single page tiff files using ImageMagik, GhostScript and iText and create multipage file corresponding to single page tiff
 * files.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jun 20, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class MultiPageExecutor implements ImageMagicKConstants, ICommonConstants {

	private static final String SPACE = " ";

	private String[] pages;

	private String documentId;

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MultiPageExecutor.class);

	public static String GS_PDF_EXECUTOR_PATH = "GS_PDF_EXECUTOR_PATH";

	public static final String IM4JAVA_TOOLPATH = "IM4JAVA_TOOLPATH";

	public MultiPageExecutor(String[] cmds, File file, BatchInstanceThread batchInstanceThread, String[] pages) {
		this.pages = new String[pages.length];
		this.pages = pages.clone();
		// Replacing ProcessExecutor with EphesoftProcessExecutor
		batchInstanceThread.add(new EphesoftProcessExecutor(cmds, file, ApplicationConfigProperties
				.getWaitTimeProperty(IM_WAIT_TIME_PROPERTY)));
	}

	/**
	 * This method creates multi page pdf using ghost script command.
	 * 
	 * @param batchInstanceThread
	 * @param pages11
	 * @param gsCmdParam
	 * @param ghostScriptCommand
	 * @param maxFilesProcessedPerLoop
	 */
	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11, final String gsCmdParam,
			final String ghostScriptCommand, final Integer maxFilesProcessedPerLoop, final String documentIdInt) {
		if (pages11 != null && pages11.length > 0) {
			this.pages = new String[pages11.length];
			this.pages = pages11.clone();
			this.documentId = documentIdInt;
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					String ghostScriptPath = System.getenv(IImageMagickCommonConstants.GHOSTSCRIPT_ENV_VARIABLE);
					List<String> ghostScriptCommandList = new ArrayList<String>();
					File systemFileFolderPath = new File(pages[0]);
					String systemFolderPath = systemFileFolderPath.getParent();
					String outputFileName = "";
					String[] gsCmdParams = gsCmdParam.split(SPACE);
					int noOfPages = pages.length;
					int currPageNo = 0;
					int counter = 1;
					File fileToBeDeleted = null;
					String prevTempFilePath = null;
					String nextTempFilePath = null;
					String tempFilePath = pages[noOfPages - 1].substring(0, pages[noOfPages - 1].lastIndexOf('.') == -1
							? pages[noOfPages].length() : pages[noOfPages - 1].lastIndexOf('.'));
					boolean isLastPage = false;
					boolean isDBatchAddedToGSCmd = false;
					while (!isLastPage) {
						ghostScriptCommandList.clear();
						LOGGER.info("creating ghostscript command for multipage pdf creation.");
						int maxNoOfPages = counter * maxFilesProcessedPerLoop;
						for (String param : gsCmdParams) {
							if (!isDBatchAddedToGSCmd && param.trim().equalsIgnoreCase(GHOST_SCRIPT_COMMAND_OUTPUT_PARAMETERS)) {
								isDBatchAddedToGSCmd = true;
							}
							ghostScriptCommandList.add(param);
						}
						if (!isDBatchAddedToGSCmd) {
							ghostScriptCommandList.add(GHOST_SCRIPT_COMMAND_OUTPUT_PARAMETERS);
						}
						if (maxNoOfPages >= noOfPages - 1) {
							if (OSUtil.isWindows()) {
								outputFileName = pages[noOfPages - 1].substring(pages[noOfPages - 1].lastIndexOf(File.separator) + 1);
								ghostScriptCommandList.add(GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + outputFileName);
							} else if (OSUtil.isUnix()) {
								ghostScriptCommandList.add(GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + pages[noOfPages - 1]);
							}

							isLastPage = true;
						} else {
							nextTempFilePath = tempFilePath + '_' + '_' + counter + documentId + FileType.PDF.getExtensionWithDot();
							outputFileName = nextTempFilePath.substring(nextTempFilePath.lastIndexOf(File.separator) + 1);
							if (OSUtil.isWindows()) {
								ghostScriptCommandList.add(DOUBLE_QUOTES + GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + outputFileName
										+ DOUBLE_QUOTES);
							} else if (OSUtil.isUnix()) {
								ghostScriptCommandList.add(GHOST_SCRIPT_COMMAND_OUTPUT_FILE_PARAM + nextTempFilePath);
							}
						}
						if (prevTempFilePath != null) {
							ghostScriptCommandList.add(prevTempFilePath);
							fileToBeDeleted = new File(prevTempFilePath);
						}
						prevTempFilePath = nextTempFilePath;
						counter++;
						for (; currPageNo < noOfPages - 1 && currPageNo < maxNoOfPages; currPageNo++) {
							if (OSUtil.isWindows()) {
								ghostScriptCommandList.add(DOUBLE_QUOTES
										+ pages[currPageNo].substring(pages[currPageNo].lastIndexOf(File.separator) + 1)
										+ DOUBLE_QUOTES);

							} else if (OSUtil.isUnix()) {
								ghostScriptCommandList.add(pages[currPageNo]);
							}
						}
						List<String> commandList = new ArrayList<String>();
						if (OSUtil.isWindows()) {
							String absoluteFilePath = systemFolderPath + File.separator + outputFileName;
							String absoluteGhostScriptParametersFilePath = absoluteFilePath.substring(0, absoluteFilePath
									.lastIndexOf("."))
									+ FileType.SER.getExtensionWithDot();
							File ghostScriptCommandParametersFile = new File(absoluteGhostScriptParametersFilePath);
							writeGhosScriptParametersToFile(ghostScriptCommandParametersFile, ghostScriptCommandList);
							makeCommandForWindows(commandList, ghostScriptPath, systemFolderPath,
									absoluteGhostScriptParametersFilePath);
						} else if (OSUtil.isUnix()) {
							commandList.add(ghostScriptPath + File.separator + ghostScriptCommand);
							commandList.addAll(ghostScriptCommandList);
						}

						LOGGER.info("Command for multi page pdf creation : " + commandList);
						String[] cmds = (String[]) commandList.toArray(new String[commandList.size()]);
						try {
							Process process = Runtime.getRuntime().exec(cmds);
							InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
							BufferedReader input = new BufferedReader(inputStreamReader);
							String line = null;
							do {
								line = input.readLine();
								LOGGER.info(line);
							} while (line != null);
							int exitValue = process.exitValue();
							if (exitValue != 0) {
								LOGGER.error("Process exited with an invalid exit value : " + exitValue);
								setDcmaApplicationException(new DCMAApplicationException(
										"Error occured while running command for multipage pdf creation."));
							}
							if (fileToBeDeleted != null && fileToBeDeleted.exists()) {
								LOGGER.info("Deleting temporary file : " + fileToBeDeleted.getAbsolutePath());
								fileToBeDeleted.delete();
							}
						} catch (IOException e) {
							LOGGER.error("Error occured while running command for multipage pdf creation." + e.getMessage(), e);
							setDcmaApplicationException(new DCMAApplicationException(
									"Error occured while running command for multipage pdf creation." + e.getMessage(), e));
						} catch (SecurityException se) {
							LOGGER.error("Cannot delete the temporary file : " + fileToBeDeleted.getAbsolutePath() + se.getMessage(),
									se);
						}
					}
					if (fileToBeDeleted != null && fileToBeDeleted.exists()) {
						LOGGER.info("Deleting temporary file : " + fileToBeDeleted.getAbsolutePath());
						fileToBeDeleted.delete();
					}
				}

				private void writeGhosScriptParametersToFile(File file, List<String> ghostScriptCommandList) {
					LOGGER.info("Writing ghostscript parameters to :" + file.getAbsolutePath());
					StringBuffer ghostScriptParametersBuffer = new StringBuffer();
					for (String command : ghostScriptCommandList) {
						ghostScriptParametersBuffer.append(command + SPACE);
					}
					try {
						FileUtils.writeStringToFile(file, ghostScriptParametersBuffer.toString());
					} catch (IOException e) {
						LOGGER.error(e.getMessage(), e);
					}
				}

				private void makeCommandForWindows(List<String> commandList, String ghostScriptPath, String systemFolderPath,
						String ghostScriptParametersFileAbsolutePath) {
					LOGGER.info("Forming Command for Ghostscript Executor(Windows).");
					commandList.add(System.getenv(GS_PDF_EXECUTOR_PATH));
					commandList.add(DOUBLE_QUOTES + ghostScriptPath + File.separator + ghostScriptCommand + DOUBLE_QUOTES);
					commandList.add(DOUBLE_QUOTES + systemFolderPath + DOUBLE_QUOTES);
					commandList.add(DOUBLE_QUOTES + ghostScriptParametersFileAbsolutePath + DOUBLE_QUOTES);
				}
			});
		}
	}

	/**
	 * This method creates multi-page tiff file using imagemagick.
	 * 
	 * @param batchInstanceThread {@link BatchInstanceThread}
	 * @param tifPages {@link String[]}
	 * @param tifCompression {@link String}
	 */
	public MultiPageExecutor(final BatchInstanceThread batchInstanceThread, final String[] tifPages, final String tifCompression,
			final int maxFilesProcessedPerLoop, final String documentIdInt) {
		if (tifPages != null && tifPages.length > 0) {
			this.pages = new String[tifPages.length];
			this.pages = tifPages.clone();
			this.documentId = documentIdInt;
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					multiTiff(tifCompression, maxFilesProcessedPerLoop);
				}
			});
		}
	}

	/**
	 * This method creates multi page pdf using Image-Magick.
	 * 
	 * @param batchInstanceThread
	 * @param pages11
	 * @param pdfCompression
	 * @param pdfQuality
	 * @param coloredImage
	 */
	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11, final String pdfCompression,
			final String pdfQuality, final String coloredImage, final String pdfOptimizationSwitch) {
		this.pages = new String[pages11.length];
		this.pages = pages11.clone();
		batchInstanceThread.add(new AbstractRunnable() {

			@Override
			public void run() {
				LOGGER.info("Creating multipgae pdf using imagemagick....");
				IMOperation op = new IMOperation();
				op.addImage(pages.length - 1);
				if (pdfQuality != null) {
					LOGGER.info("Adding pdfQuality : " + pdfQuality);
					op.quality(new Double(pdfQuality));
				}

				if (coloredImage != null && ImageMagicKConstants.FALSE.equalsIgnoreCase(coloredImage)) {
					op.monochrome();
				}
				if (pdfCompression != null) {
					LOGGER.info("Adding pdfCompression : " + pdfCompression);
					op.compress(pdfCompression);
				}

				if (pdfOptimizationSwitch != null && pdfOptimizationSwitch.equalsIgnoreCase(ImageMagicKConstants.ON_SWITCH)) {
					LOGGER.info("Adding pdfOptimnisation.");
					// As per Ike suggestion, not performing any optimization with Imagemagick using PDF
					// op.type("optimize");
				}

				op.addImage();
				ConvertCmd convert = new ConvertCmd();
				try {
					convert.run(op, (Object[]) pages);
				} catch (Exception e) {
					LOGGER.error("Error occured while running command for multipage pdf creation." + e.getMessage(), e);
					setDcmaApplicationException(new DCMAApplicationException(
							"Error occured while running command for multipage pdf creation." + e.getMessage(), e));
				}
			}
		});
	}

	/**
	 * This method creates multi page pdf using IText.
	 * 
	 * @param batchInstanceThread
	 * @param pages11
	 * @param widthOfPdfPageInt
	 * @param heightOfPdfPageInt
	 */
	public MultiPageExecutor(BatchInstanceThread batchInstanceThread, final String[] pages11, final int widthOfPdfPage,
			final int heightOfPdfPage) {
		if (pages11 != null && pages11.length > 0) {
			this.pages = new String[pages11.length];
			this.pages = pages11.clone();
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					String pdf = pages[pages.length - 1];
					Document document = null;
					PdfWriter writer = null;
					RandomAccessFileOrArray ra = null;
					try {
						document = new Document(PageSize.LETTER, 0, 0, 0, 0);
						writer = PdfWriter.getInstance(document, new FileOutputStream(pdf));
						document.open();
						int comps = 1;
						int totalTiffImages = pages.length - 1;
						int index = 0;
						while (index < totalTiffImages) {
							try {
								ra = new RandomAccessFileOrArray(pages[index]);
								comps = TiffImage.getNumberOfPages(ra);
								// Conversion statement
								for (int tiffPageNumber = 0; tiffPageNumber < comps; ++tiffPageNumber) {
									Image img = TiffImage.getTiffImage(ra, tiffPageNumber + 1);

									// Getting the dots per inches in X and Y direction.
									float dotsPerPointX = img.getDpiX() / PDF_RESOLUTION;
									float dotsPerPointY = img.getDpiY() / PDF_RESOLUTION;

									// If dots per inches in X-direction or Y- direction is 0 then Scales the image to the default
									// width and height.
									if (dotsPerPointX == 0 || dotsPerPointY == 0) {
										img.scaleToFit(widthOfPdfPage, heightOfPdfPage);
									} else {

										// Calculating new width and height.
										float newWidth = img.getWidth() / dotsPerPointX;
										float newHeight = img.getHeight() / dotsPerPointY;
										document.setPageSize(new Rectangle(newWidth, newHeight));

										// Scales the image so that it fits a certain width and height.
										img.scaleToFit(img.getWidth() / dotsPerPointX, img.getHeight() / dotsPerPointY);
									}
									if (document.isOpen()) {
										document.newPage();
									} else {
										document.open();
									}
									document.add(img);
								}
							} finally {
								if (ra != null) {
									ra.close();
								}
							}
							index++;
						}
					} catch (Exception e) {
						LOGGER.error("Error while creating pdf using iText" + e.getMessage(), e);
						pdf = null;
					} finally {
						try {
							if (document != null) {
								document.close();
							}
							if (writer != null) {
								writer.close();
							}

						} catch (Exception e) {
							LOGGER.error("Error while closing I/O streams for write PDF. " + e.getMessage());
						}
					}
				}
			});
		}
	}

	/**
	 * The <code>MultiPageExecutor</code> method creates multi page searchable pdf using IText.
	 * 
	 * @param batchInstanceThread {@link BatchInstanceThread} thread instance of batch.
	 * @param imageHtmlMap {@link Map} map containing image url with corresponding hocr.
	 * @param isColoredPDF true for colored image pdf else otherwise.
	 * @param isSearchablePDF true for searchable pdf else otherwise.
	 * @param pdfFilePath {@link String} path where new pdf has to be created.
	 * @param pages {@link String[]} is the imageUrl
	 * @param fontFilePath {@link String} specifies the path of font file.
	 * @param convertParameters {@link String} specifying the convert command parameters for the image before iText pdf creation.
	 * @param iTextSearchablePDFType {@link String} IText searchable pdf type (Pdf or pdf Advanced).
	 */
	public MultiPageExecutor(final BatchInstanceThread batchInstanceThread, final Map<String, HocrPage> imageHocrMap,
			final boolean isColoredPDF, final boolean isSearchablePDF, final String pdfFilePath, final String[] pages,
			final String fontFilePath, final String convertParameters, final String iTextSearchablePDFType) {
		if (imageHocrMap != null && !imageHocrMap.isEmpty()) {
			this.pages = new String[pages.length];
			this.pages = pages.clone();
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					String pdf = pdfFilePath;
					Document document = null;
					PdfAWriter pdfAWriter = null;
					PdfWriter pdfWriter = null;
					FileOutputStream fileOutputStream = null;
					boolean isPdfSearchable = isSearchablePDF;
					boolean isColoredImage = isColoredPDF;
					LOGGER.info(EphesoftStringUtil.concatenate("is searchable pdf: ", isPdfSearchable, ", is Colored Image: ",
							isColoredImage));
					try {
						document = new Document(PageSize.LETTER, 0, 0, 0, 0);
						fileOutputStream = new FileOutputStream(pdf);
						// Switch is provided for PDF or PDF-A . Fixed against ZOHO [##1520##] LSI Releasing Searchable PDF has broke.
						if (ImageMagicKConstants.PDF_TYPE_ADVANCED.equals(iTextSearchablePDFType)) {
							pdfAWriter = PdfAWriter.getInstance(document, fileOutputStream, PdfAConformanceLevel.PDF_A_1B);
							pdfAWriter.createXmpMetadata();
						} else {
							pdfWriter = PdfWriter.getInstance(document, fileOutputStream);
						}

						Set<String> imageSet = imageHocrMap.keySet();
						for (String imageUrl : imageSet) {
							HocrPage hocrPage = imageHocrMap.get(imageUrl);
							String newImageUrl = getCompressedImage(isColoredImage, imageUrl, convertParameters);
							LOGGER.info(EphesoftStringUtil.concatenate("New Image URL: ", newImageUrl));
							if (ImageMagicKConstants.PDF_TYPE_ADVANCED.equalsIgnoreCase(iTextSearchablePDFType)) {
								addImageToPdf(pdfAWriter, hocrPage, newImageUrl, isPdfSearchable, fontFilePath, document);
							} else {
								addImageToPdf(pdfWriter, hocrPage, newImageUrl, isPdfSearchable, document);
							}
							(new File(newImageUrl)).delete();
						}
					} catch (IOException ioException) {
						LOGGER.error(EphesoftStringUtil.concatenate("Error occurred while creating pdf ", pdf, " : ", ioException
								.toString()));
					} catch (DocumentException documentException) {
						LOGGER.error(EphesoftStringUtil.concatenate("Error occurred while creating pdf ", pdf, " : ",
								documentException.toString()));
					} finally {
						if (document != null && document.isOpen()) {
							document.close();
						}
						// Closing pdf writer
						if (null != pdfAWriter) {
							pdfAWriter.close();
						}
						if (null != pdfWriter) {
							pdfWriter.close();
						}

						// Closing file output stream of pdf
						if (null != fileOutputStream) {
							try {
								fileOutputStream.close();
							} catch (IOException ioException) {
								LOGGER.error(EphesoftStringUtil.concatenate("Error occurred while closing stream for pdf ", pdf,
										" : ", ioException.toString()));
							}
						}
					}
				}
			});
		}
	}

	/**
	 * Adds image to PDF and make it searchable by adding image text using specified font in invisible mode w.r.t parameter
	 * 'isPdfSearchable' passed.
	 * 
	 * @param pdfAWriter {@link PdfWriter} writer of pdf in which image has to be added
	 * @param hocrPage {@link HocrPage} corresponding HOCR Xml file for fetching text and coordinates
	 * @param imageUrl {@link String} url of image to be added in pdf
	 * @param isPdfSearchable true for searchable pdf false otherwise
	 * @param fontFilePath {@link String} path of font file
	 * @param document {@link Document} The document to which images are to be added
	 */
	private void addImageToPdf(final PdfAWriter pdfAWriter, final HocrPage hocrPage, final String imageUrl,
			final boolean isPdfSearchable, final String fontFilePath, final Document document) {
		if (null != pdfAWriter && null != imageUrl && imageUrl.length() > 0) {
			try {
				LOGGER.info(EphesoftStringUtil.concatenate("Adding image", imageUrl, " to pdf using iText."));
				final Image pageImage = Image.getInstance(imageUrl);
				final float dotsPerPointX = pageImage.getDpiX() / PDF_RESOLUTION;
				final float dotsPerPointY = pageImage.getDpiY() / PDF_RESOLUTION;
				if (dotsPerPointX > 0.0F && dotsPerPointY > 0.0F) {
					final float newWidth = pageImage.getWidth() / dotsPerPointX;
					final float newHeight = pageImage.getHeight() / dotsPerPointY;

					document.setPageSize(new Rectangle(newWidth, newHeight));
					if (!document.isOpen()) {
						document.open();
					} else {
						document.newPage();
					}

					final PdfContentByte pdfContentByte = pdfAWriter.getDirectContent();
					pageImage.scaleToFit(newWidth, newHeight);
					pageImage.setAbsolutePosition(0, 0);

					// Add image to pdf
					pdfAWriter.getDirectContentUnder().addImage(pageImage);
					pdfAWriter.getDirectContentUnder().add(pdfContentByte);

					// If pdf is to be made searchable
					if (isPdfSearchable) {
						LOGGER.info(EphesoftStringUtil.concatenate("Adding invisible text for image: ", imageUrl));
						final float pageImagePixelHeight = pageImage.getHeight();
						final Font font = FontFactory.getFont(fontFilePath, BaseFont.CP1252, BaseFont.EMBEDDED, 8, Font.BOLD,
								CMYKColor.BLACK);
						// Fetch text and coordinates for image to be added
						final List<LineDataCarrier> lineDataCarriers = HocrUtil.getLineDataCarrierList(hocrPage.getSpans(), hocrPage
								.getPageID(), true);

						// Add text at specific location
						Coordinates coordinates;
						String rowData;
						float bboxWidthPt;
						float bboxHeightPt;
						float xCoordinate;
						float yCoordinate;
						int rowdataX0;
						int rowdataX1;
						int rowdataY0;
						int rowdataY1;
						for (LineDataCarrier lineDataCarrier : lineDataCarriers) {
							rowData = lineDataCarrier.getLineRowData();
							coordinates = lineDataCarrier.getRowCoordinates();
							rowdataX0 = coordinates.getX0().intValue();
							rowdataX1 = coordinates.getX1().intValue();
							rowdataY0 = coordinates.getY0().intValue();
							rowdataY1 = coordinates.getY1().intValue();
							bboxWidthPt = (rowdataX1 - rowdataX0) / dotsPerPointX;
							bboxHeightPt = (rowdataY1 - rowdataY0) / dotsPerPointY;
							pdfContentByte.beginText();

							// To make text added as invisible
							pdfContentByte.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);
							pdfContentByte.setLineWidth(Math.round(bboxWidthPt));

							// Ceil is used so that minimum font of any text is 1
							// For exception of unbalanced beginText() and endText()
							if (bboxHeightPt > 0.0) {
								pdfContentByte.setFontAndSize(font.getBaseFont(), (float) Math.ceil(bboxHeightPt));
							} else {
								pdfContentByte.setFontAndSize(font.getBaseFont(), 1);
							}
							xCoordinate = (float) (rowdataX0 / dotsPerPointX);
							yCoordinate = (float) ((pageImagePixelHeight - rowdataY1) / dotsPerPointY);
							pdfContentByte.moveText(xCoordinate, yCoordinate);
							pdfContentByte.showText(rowData);
							pdfContentByte.endText();
						}
					}
					pdfContentByte.closePath();
				} else {
					LOGGER.error("Unable to create PDF/SERCHABLE-PDF file for input image as image has invalid DPI.");
				}
			} catch (final BadElementException badElementException) {
				LOGGER.error(EphesoftStringUtil.concatenate("Element hasn't got the right form for image URL ",
						imageUrl, badElementException.toString()), badElementException);
			} catch (final DocumentException documentException) {
				LOGGER.error(EphesoftStringUtil.concatenate("An error has occurred in a Document for image URL ",
						imageUrl, documentException.toString()), documentException);
			} catch (final MalformedURLException malformedURLException) {
				LOGGER.error(EphesoftStringUtil.concatenate("Image URL is incorrect or can't be parsed ", imageUrl,
						malformedURLException.toString()), malformedURLException);
			} catch (final IOException ioException) {
				LOGGER.error(EphesoftStringUtil.concatenate("IO error occured while processing image URL ", imageUrl, ioException
						.toString()), ioException);
			}
		} else {
			LOGGER.error("Invalid parameter are specified while creating PDF/SERCHABLE-PDF. Unable to perform opeartion.");
		}
	}

	/**
	 * Adds image to pdf and make it searchable by adding image text in invisible mode w.r.t parameter 'isPdfSearchable' passed.
	 * 
	 * @param pdfWriter {@link PdfWriter} writer of pdf in which image has to be added
	 * @param hocrPage {@link HocrPage} corresponding HOCR Xml file for fetching text and coordinates
	 * @param imageUrl {@link String} url of image to be added in pdf
	 * @param isPdfSearchable true for searchable pdf false otherwise
	 * @param document {@link Document} The document to which images are to be added
	 */
	private void addImageToPdf(final PdfWriter pdfWriter, final HocrPage hocrPage, final String imageUrl,
			final boolean isPdfSearchable, final Document document) {
			LOGGER.info("************************ 1");
		if (null != pdfWriter && null != imageUrl && imageUrl.length() > 0) {
			try {
				LOGGER.info(EphesoftStringUtil.concatenate("Adding image", imageUrl, " to pdf using iText."));
				final Image pageImage = Image.getInstance(imageUrl);
				final float dotsPerPointX = pageImage.getDpiX() / PDF_RESOLUTION;
				final float dotsPerPointY = pageImage.getDpiY() / PDF_RESOLUTION;
				if (dotsPerPointX > 0.0F && dotsPerPointY > 0.0F) {
					final float newWidth = pageImage.getWidth() / dotsPerPointX;
					final float newHeight = pageImage.getHeight() / dotsPerPointY;

					document.setPageSize(new Rectangle(newWidth, newHeight));
					if (document.isOpen()) {
						document.newPage();
					} else {
						document.open();
					}

					final PdfContentByte pdfContentByte = pdfWriter.getDirectContent();

					pageImage.scaleToFit(pageImage.getWidth() / dotsPerPointX, pageImage.getHeight() / dotsPerPointY);

					pageImage.setAbsolutePosition(0, 0);

					// Add image to pdf
					pdfWriter.getDirectContentUnder().addImage(pageImage);
					pdfWriter.getDirectContentUnder().add(pdfContentByte);

					// If pdf is to be made searchable
					if (isPdfSearchable) {
						LOGGER.info(EphesoftStringUtil.concatenate("Adding invisible text for image: ", imageUrl));
						final float pageImagePixelHeight = pageImage.getHeight();
						final Font defaultFont = FontFactory.getFont(FontFactory.HELVETICA, ImageMagicKConstants.FONT_SIZE, Font.BOLD,
								CMYKColor.BLACK);

						// Fetch text and coordinates for image to be added
						final List<LineDataCarrier> lineDataCarriers = HocrUtil.getLineDataCarrierList(hocrPage.getSpans(), hocrPage
								.getPageID(), true);

						// Add text at specific location
						Coordinates coordinates;
						String rowData;
						float bboxWidthPt;
						float bboxHeightPt;
						float xCoordinate;
						float yCoordinate;
						int rowdataX0;
						int rowdataX1;
						int rowdataY0;
						int rowdataY1;
						for (LineDataCarrier lineDataCarrier : lineDataCarriers) {
							rowData = lineDataCarrier.getLineRowData();
							coordinates = lineDataCarrier.getRowCoordinates();
							rowdataX0 = coordinates.getX0().intValue();
							rowdataX1 = coordinates.getX1().intValue();
							rowdataY0 = coordinates.getY0().intValue();
							rowdataY1 = coordinates.getY1().intValue();
							bboxWidthPt = (rowdataX1 - rowdataX0) / dotsPerPointX;
							bboxHeightPt = (rowdataY1 - rowdataY0) / dotsPerPointY;
							pdfContentByte.beginText();

							// To make text added as invisible
							pdfContentByte.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_INVISIBLE);
							pdfContentByte.setLineWidth(Math.round(bboxWidthPt));

							// Ceil is used so that minimum font of any text is 1
							// For exception of unbalanced beginText() and endText()
							if (bboxHeightPt > 0.0) {
								pdfContentByte.setFontAndSize(defaultFont.getBaseFont(), (float) Math.ceil(bboxHeightPt));
							} else {
								pdfContentByte.setFontAndSize(defaultFont.getBaseFont(), 1);
							}
							xCoordinate = (float) (rowdataX0 / dotsPerPointX);
							yCoordinate = (float) ((pageImagePixelHeight - rowdataY1) / dotsPerPointY);
							pdfContentByte.moveText(xCoordinate, yCoordinate);
							pdfContentByte.showText(rowData);
							pdfContentByte.endText();
						}
					}
					pdfContentByte.closePath();
				} else {
					LOGGER.error("Unable to create PDF/SERCHABLE-PDF file for input image as image has invalid DPI.");
				}
			} catch (final BadElementException badElementException) {
				LOGGER.error(EphesoftStringUtil.concatenate("Element hasn't got the right form for image URL ",
						imageUrl, badElementException.toString()), badElementException);
			} catch (final DocumentException documentException) {
				LOGGER.error(EphesoftStringUtil.concatenate("An error has occurred in a Document for image URL ",
						imageUrl, documentException.toString()), documentException);
			} catch (final MalformedURLException malformedURLException) {
				LOGGER.error(EphesoftStringUtil.concatenate("Image URL is incorrect or can't be parsed ", imageUrl,
						malformedURLException.toString()), malformedURLException);
			} catch (final IOException ioException) {
				LOGGER.error(EphesoftStringUtil.concatenate("IO error occured while processing image URL ", imageUrl, ioException
						.toString()), ioException);
			}
		} else {
			LOGGER.error("Invalid parameter are specified while creating PDF/SERCHABLE-PDF. Unable to perform opeartion.");
		}
	}

	/**
	 * The <code>getCompressedImage</code> method is used to compress passed image according to compression type LZW, quality 100 and
	 * convert image to monochrome i.e., black and white w.r.t isColoredImage passed.
	 * 
	 * @param isColoredImage true for colored image and false otherwise
	 * @param imageUrl image to be converted
	 * @param convertParameters The convert command parameters for the image.
	 * @return url of converted image from passed image
	 */
	private String getCompressedImage(final boolean isColoredImage, final String imageUrl, final String convertParameters) {
		String newImageUrl = imageUrl;
		if (null != imageUrl && imageUrl.length() > 0) {
			newImageUrl = imageUrl.substring(0, imageUrl.lastIndexOf(DOTS)) + NEW + TIF_EXTENSION;
			// Switch is provided for PDF or PDF-A . Fixed against ZOHO [##1520##] LSI Releasing Searchable PDF has broke.
			String modifiedConvertParameters = convertParameters;
			if (!isColoredImage) {
				modifiedConvertParameters = EphesoftStringUtil.concatenate("-monochrome ", convertParameters);
			}
			String command = EphesoftStringUtil.concatenate(System.getenv(IM4JAVA_TOOLPATH), File.separator, "convert", " ", imageUrl,
					" ", modifiedConvertParameters, " ", newImageUrl);
			try {
				Process exec = Runtime.getRuntime().exec(command, null);
				int waitFor = exec.waitFor();
				if (waitFor != 0) {
					throw new DCMAApplicationException("Non-zero exit value error for command: " + command);
				}
			} catch (Exception e) {
				LOGGER
						.error(
								"Unable to execute the command for conversion of image before iText PDF Creation according to parameters specified in the property file.",
								e);
			}
		}
		return newImageUrl;
	}

	public String[] getPages() {
		return pages;
	}

	/**
	 * @param tifCompression
	 * @param maxFilesProcessedPerLoop
	 */
	private void multiTiff(final String tifCompression, final int maxFilesProcessedPerLoop) {
		LOGGER.info("Adding command for multipage tiff creation using imagemagick.");
		int noOfPages = pages.length;
		String outputFileName = pages[noOfPages - 1];
		LOGGER.info("Total number of input pages : " + (noOfPages - 1));
		int indexOf = outputFileName.lastIndexOf(File.separator);
		indexOf = indexOf == -1 ? outputFileName.length() : indexOf;
		String tempFilePath = outputFileName.substring(0, indexOf);
		List<File> filesToBeDeleted = new ArrayList<File>();
		List<String> pageList = new ArrayList<String>();
		String nextFileName = null;
		for (int index = 0, counter = 1; index < noOfPages; index++) {
			if (index < counter * maxFilesProcessedPerLoop && index < noOfPages - 1) {
				LOGGER.info("Adding input file name : " + pages[index]);
				pageList.add(pages[index]);
			} else {
				if (index == noOfPages - 1) {
					pageList.add(outputFileName);
					nextFileName = outputFileName;
					LOGGER.info("Adding output file name : " + outputFileName);
				} else {
					nextFileName = EphesoftStringUtil.concatenate(tempFilePath, File.separator, ImageMagicKConstants.TEMP_FILE_NAME,
							ImageMagicKConstants.UNDERSCORE, counter, ImageMagicKConstants.UNDERSCORE, documentId, FileType.TIF
									.getExtensionWithDot());
					pageList.add(nextFileName);
					filesToBeDeleted.add(new File(nextFileName));
					LOGGER.info("Adding output file name : " + nextFileName);
				}
				ConvertCmd convert = new ConvertCmd();
				IMOperation oper = new IMOperation();
				oper.addImage(pageList.size() - 1);
				oper.adjoin();
				oper.compress(tifCompression);
				oper.addImage();
				// oper.limit("10");
				try {
					long startNanoTime = System.nanoTime();
					System.out.println(startNanoTime);
					LOGGER.info("Running command for multipage tiff creation using imagemagick. tiff file name : " + nextFileName);
					convert.run(oper, (Object[]) pageList.toArray());
					LOGGER.info("Multipage tiff creation using imagemagick. tiff file name : " + nextFileName);
					pageList.clear();
					pageList.add(nextFileName);
					LOGGER.info("Adding input file name : " + nextFileName);
					pageList.add(pages[index]);
					LOGGER.info("Adding input file name : " + pages[index]);
					counter = counter + 1;
				} catch (Exception e) {
					LOGGER.error("Error occured while running command for multipage tiff creation for file : " + nextFileName, e);
				}
			}
		}
		if (filesToBeDeleted != null && !filesToBeDeleted.isEmpty()) {
			for (File file : filesToBeDeleted) {
				if (file.exists()) {
					LOGGER.info("Deleting temporary file : " + file.getAbsolutePath());
					file.delete();
				}
			}
		}
	}
}
