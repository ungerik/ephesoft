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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.AbstractRunnable;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.EphesoftProcessExecutor;
import com.ephesoft.dcma.imagemagick.IImageMagickCommonConstants;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.OSUtil;

/**
 * Provides APIs using which one can generate thumbnails and PNG for a given tif file.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jun 20, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class ThumbnailPNGCreator implements ICommonConstants, IImageMagickCommonConstants {

	private static final String INVALID_THUMBAIL_HEIGHT_SPECIFIED = "Invalid thumbail height for scanned images specified in imagemagick property file.";

	private static final String INVALID_THUMBAIL_WIDTH_SPECIFIED = "Invalid thumbail width for scanned images specified in imagemagick property file.";

	private static final String INVALID_PNG_WIDTH_SPECIFIED = "Invalid PNG width for scanned images specified in imagemagick property file.";

	private static final String INVALID_PNG_HEIGHT_SPECIFIED = "Invalid PNG height for scanned images specified in imagemagick property file.";

	private static final String SETTING_TO_DEFAULT_VALUE = "Setting to default value:";

	private static final String EMPTY_STRING = "";

	private static final char DOT = '.';

	private static final String PROBLEM_GENERATING_THUMBNAILS = "Problem generating thumbnails";

	protected static final Logger LOGGER = LoggerFactory.getLogger(ThumbnailPNGCreator.class);

	private static final String GRAY_COLOR = "gray";

	private static final String QUOTES = "\"";

	private static final String SPACE = " ";

	private static final Integer DEFAULT_THUMBNAIL_WIDTH = 200;

	private static final Integer DEFAULT_THUMBNAIL_HEIGHT = 150;

	private static final Integer DEFAULT_PNG_WIDTH = 800;

	private static final Integer DEFAULT_PNG_HEIGHT = 600;

	/**
	 * Default output parameters of imagemagick for generating thumbnail images. 
	 */
	private static final String DEFAULT_THUMBNAIL_OUTPUT_PARAM = "-colorspace gray -thumbnail 200x150";
	
	/**
	 * Default output parameters of imagemagick for generating display images. 
	 */
	private static final String DEFAULT_IMAGE_OUTPUT_PARAM = "-colorspace gray -resize 800x600";

	private transient String thumbnailHeightForScannedImage;

	private transient String thumbnailWidthForScannedImage;

	private transient String pngWidthForScannedImage;

	private transient String pngHeightForScannedImage;
	
	/**
	 * Flag to indicating error occurred while generating thumbnails.
	 */
	private static boolean isThumbnailParamError = false;
	
	/**
	 * Flag to indicating error occurred while generating display image.
	 */
	private static boolean isDisplayImageParamError = false;
	
	/**
	 * Output parameter configured through property file for generating thumbnails.
	 */
	private String outputParamThumbnail;
	
	/**
	 * Output parameter configured through property file for generating display image.
	 */
	private String outputParamImage;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

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
	 * This constructor checks weather the IM4JAVA_TOOLPATH enviornment variable is set.
	 */
	public ThumbnailPNGCreator() {
		final String envVariable = System.getenv(IMAGEMAGICK_ENV_VARIABLE);
		LOGGER.debug("checking if the Image Magick Enviornment variable IM4JAVA_TOOLPATH is set");
		if (envVariable == null || envVariable.isEmpty()) {
			LOGGER.error("Enviornment Variable IM4JAVA_TOOLPATH not set");
			throw new DCMABusinessException("Enviornment Variable IM4JAVA_TOOLPATH not set.");
		}
		LOGGER.debug("Enviornment variable IM4JAVA_TOOLPATH is set value=" + envVariable);
	}

	
	/*
	 * Generating thumbnail and display image for the given file name and type passed as a parameter.
	 */
	private void generateThumbnailsAndImage(File imagePath, String thumbnailW, String thumbnailH, FileType fileType) throws DCMAApplicationException  {
		String imageExtension = fileType.getExtensionWithDot();
		String thumbExtension = EphesoftStringUtil.concatenate(SUFFIX_THUMBNAIL_SAMPLE, imageExtension);
		String imageName = imagePath.getAbsolutePath();
		String pngPath = imageName.substring(0, imageName.indexOf(FileType.TIF.getExtensionWithDot()));
		Integer thumbnailHeight = getThumbnailHeight();
		Integer thumbnailWidth = getThumbnailWidth();
		Integer pngHeight = getPngWidth();
		Integer pngWidth = getPngHeight();
		String outputParamsForThumbnail = EphesoftStringUtil.concatenate("-colorspace ", GRAY_COLOR, " -thumbnail ", thumbnailWidth, "x", thumbnailHeight);
		try {
			createAndExecuteCommand(outputParamsForThumbnail, imageName, pngPath, thumbExtension);
		} catch (Exception exception) {
			throw new DCMAApplicationException(PROBLEM_GENERATING_THUMBNAILS, exception);
		}
		String outputParamsForPNGCreation = EphesoftStringUtil.concatenate("-colorspace ", GRAY_COLOR, " -resize ", pngWidth, "x", pngHeight);
		try {
			createAndExecuteCommand(outputParamsForPNGCreation, imageName, pngPath, imageExtension);
		} catch (Exception exception) {
			throw new DCMAApplicationException("Problem generating PNGs", exception);
		}
	}
	
	/*
	 * Generating thumbnail and display image for the given file name and type passed as a parameter.
	 */
	private void generateThumbnailsAndImage(final File imagePath, final String thumbnailW, final String thumbnailH,
			final FileType fileType, final String outputParmThumbnail, final String outputParamImage) throws DCMAApplicationException {
		
		LOGGER.debug(EphesoftStringUtil.concatenate("Generating thumbnail and display image for: ", imagePath));
		final String imageExtension = fileType.getExtensionWithDot();
		final String thumbExtension = EphesoftStringUtil.concatenate(SUFFIX_THUMBNAIL_SAMPLE, imageExtension);
		final String imageName = imagePath.getAbsolutePath();
		final String pngPath = imageName.substring(0, imageName.indexOf(FileType.TIF.getExtensionWithDot()));
		try {
			createAndExecuteCommand(outputParmThumbnail, imageName, pngPath, thumbExtension);
		} catch (final Exception exception) {

			/* Once error is handled here i.e, if some how parameters in properties file for imagemagick is wrong then we try again
			 once with default parameters. */
			if (isThumbnailParamError) {
				throw new DCMAApplicationException(PROBLEM_GENERATING_THUMBNAILS, exception);
			} else {
				LOGGER.error(
						"Parameters for thumbnail imagemagick provided may be wrong. Default settings applied. Issue is due to: ",
						exception);
				isThumbnailParamError = true;
				generateThumbnailsAndImage(imagePath, thumbnailW, thumbnailH, fileType, DEFAULT_THUMBNAIL_OUTPUT_PARAM,
						outputParamImage);
			}
		}
		try {
			createAndExecuteCommand(outputParamImage, imageName, pngPath, imageExtension);
		} catch (final Exception exception) {

			/* Once error is handled here i.e, if some how parameters in properties file for imagemagick is wrong then we try again
			 once with default parameters.*/
			if (isDisplayImageParamError) {
				throw new DCMAApplicationException("Problem generating PNGs", exception);
			} else {
				LOGGER.error("Parameters for image imagemagick provided may be wrong. Default settings applied. Issue is due to: ",
						exception);
				isDisplayImageParamError = true;
				generateThumbnailsAndImage(imagePath, thumbnailW, thumbnailH, fileType, DEFAULT_IMAGE_OUTPUT_PARAM, outputParamImage);
			}
		}
		LOGGER.debug(EphesoftStringUtil.concatenate("Thumbnail and display image generated for: ", imagePath));
	}
	
	
	/*
	 * Creates and executes imagemagick command for converting tiff files to the specified format.
	 */
	private void createAndExecuteCommand(final String outputParams, final String imageName, final String path, final String imageExtension) throws IOException {

		final ArrayList<String> commandList = new ArrayList<String>();
		final String inputParams = "";
		String[] cmds = null;
		final StringBuffer command = new StringBuffer(EMPTY_STRING);
		// Changes made for generating JPG Images
		if (OSUtil.isWindows()) {
			createCommandforWindows(commandList, inputParams, outputParams, QUOTES + System.getenv(IMAGEMAGICK_ENV_VARIABLE)
					+ File.separator + "convert\"", QUOTES + imageName + QUOTES, QUOTES + path + imageExtension + QUOTES);

		} else {
			commandList.add(System.getenv(IMAGEMAGICK_ENV_VARIABLE) + File.separator + "convert");
			createCommandForLinux(commandList, inputParams, outputParams, imageName, path + imageExtension);
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
		LOGGER.debug(EphesoftStringUtil.concatenate("Convert command of Imagemagick is = ", command));
		executeCommand(command);
	}

	private Integer getPngHeight() {
		Integer pngHeight = null;
		try {
			pngHeight = Integer.parseInt(pngHeightForScannedImage);
			if (pngHeight < 0) {
				pngHeight = DEFAULT_PNG_HEIGHT;
				LOGGER.info(INVALID_PNG_HEIGHT_SPECIFIED + SETTING_TO_DEFAULT_VALUE + DEFAULT_PNG_HEIGHT);
			}
		} catch (Exception e) {
			pngHeight = DEFAULT_PNG_HEIGHT;
			LOGGER.info(INVALID_PNG_HEIGHT_SPECIFIED + SETTING_TO_DEFAULT_VALUE + DEFAULT_PNG_HEIGHT);
		}
		return pngHeight;
	}

	private Integer getPngWidth() {
		Integer pngWidth = null;
		try {
			pngWidth = Integer.parseInt(pngWidthForScannedImage);
			if (pngWidth < 0) {
				pngWidth = DEFAULT_PNG_WIDTH;
				LOGGER.info(INVALID_PNG_WIDTH_SPECIFIED + SETTING_TO_DEFAULT_VALUE + DEFAULT_PNG_WIDTH);
			}
		} catch (Exception e) {
			pngWidth = DEFAULT_PNG_WIDTH;
			LOGGER.info(INVALID_PNG_WIDTH_SPECIFIED + SETTING_TO_DEFAULT_VALUE + DEFAULT_PNG_WIDTH);
		}
		return pngWidth;
	}

	private Integer getThumbnailWidth() {
		Integer thumbnailWidth = null;
		try {
			thumbnailWidth = Integer.parseInt(thumbnailWidthForScannedImage);
			if (thumbnailWidth <= 0) {
				thumbnailWidth = DEFAULT_THUMBNAIL_WIDTH;
				LOGGER.info(INVALID_THUMBAIL_WIDTH_SPECIFIED + SETTING_TO_DEFAULT_VALUE + DEFAULT_THUMBNAIL_WIDTH);
			}
		} catch (Exception e) {
			thumbnailWidth = DEFAULT_THUMBNAIL_WIDTH;
			LOGGER.info(INVALID_THUMBAIL_WIDTH_SPECIFIED + SETTING_TO_DEFAULT_VALUE + DEFAULT_THUMBNAIL_WIDTH);
		}
		return thumbnailWidth;
	}

	private Integer getThumbnailHeight() {
		Integer thumbnailHeight = null;
		try {
			thumbnailHeight = Integer.parseInt(thumbnailHeightForScannedImage);
			if (thumbnailHeight <= 0) {
				thumbnailHeight = DEFAULT_THUMBNAIL_HEIGHT;
				LOGGER.info(INVALID_THUMBAIL_HEIGHT_SPECIFIED + SETTING_TO_DEFAULT_VALUE + DEFAULT_THUMBNAIL_HEIGHT);
			}
		} catch (Exception e) {
			thumbnailHeight = DEFAULT_THUMBNAIL_HEIGHT;
			LOGGER.info(INVALID_THUMBAIL_HEIGHT_SPECIFIED + SETTING_TO_DEFAULT_VALUE + DEFAULT_THUMBNAIL_HEIGHT);
		}
		return thumbnailHeight;
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
	 * Generates thumbnail and corresponding PNG for the image passed as parameter.
	 * 
	 * <p>
	 * These generated images are used to display images on UI. No information is lossed when images are converted to PNG format but
	 * size of images would be large.
	 * 
	 * @param imagePath {@link String} path of image whose thumbnails and image needs to be generated.
	 * @param thumbnailW {@link String} width of thumbnail image.
	 * @param thumbnailH {@link String} height of thumbnail image.
	 * @throws DCMAApplicationException if unable to generate images for the given file.
	 */
	public void generateThumbnailsAndPNGForImage(File imagePath, String thumbnailW, String thumbnailH) throws DCMAApplicationException {
		generateThumbnailsAndImage(imagePath, thumbnailW, thumbnailH, FileType.PNG);
	}

	/**
	 * Generates thumbnail and corresponding JPG for the image passed as parameter.
	 * 
	 * <p>
	 * These generated images are used to display images on UI. Thus jpeg images takes less memory in comparison to other type images.
	 * The images may loose some information as they are converted to jpeg.
	 * 
	 * @param imagePath {@link String} path of image whose thumbnails and image needs to be generated.
	 * @param thumbnailW {@link String} width of thumbnail image.
	 * @param thumbnailH {@link String} height of thumbnail image.
	 * @throws DCMAApplicationException if unable to generate images for the given file.
	 */
	public void generateThumbnailsAndJPGForImage(File imagePath, String thumbnailW, String thumbnailH) throws DCMAApplicationException {
		final String outputParamThumbnail =  isThumbnailParamError ? DEFAULT_THUMBNAIL_OUTPUT_PARAM : getOutputParamThumbnail();
		final String outputParamImage  = isDisplayImageParamError ? DEFAULT_IMAGE_OUTPUT_PARAM : getOutputParamImage();
		generateThumbnailsAndImage(imagePath, thumbnailW, thumbnailH, FileType.JPG, outputParamThumbnail, outputParamImage);
	}

	/**
	 * Gets the output parameters for the imagemagick command for generating display image.
	 * 
	 * <p>
	 * If error occurred with the configurable parameters, default parameters will be returned.
	 */
	public String getOutputParamImage() {
		if (isDisplayImageParamError) {
			outputParamImage = DEFAULT_IMAGE_OUTPUT_PARAM;
		}
		return outputParamImage;
	}

	/**
	 * Gets the output parameters for the imagemagick command for generating thumbnail image.
	 * 
	 * <p>
	 * If error occurred with the configurable parameters, default parameters will be returned.
	 */
	public String getOutputParamThumbnail() {
		if (isThumbnailParamError) {
			outputParamThumbnail = DEFAULT_THUMBNAIL_OUTPUT_PARAM;
		}
		return outputParamThumbnail;
	}

	/**
	 * This method generates the thumbnails for all the tif files in the batch folder.
	 * 
	 * @param sBatchFolder
	 * @param batchInstanceIdentifier
	 * @param pluginWorkflowName
	 * @throws DCMAApplicationException
	 * @throws JAXBException
	 */
	public void generateThumbnail(final String sBatchFolder, final String batchInstanceIdentifier,
			BatchSchemaService batchSchemaService, String thumbnailType, String pluginWorkflowName) throws DCMAApplicationException,
			JAXBException {

		final File fBatchFolder = new File(sBatchFolder);
		String thumbnailH = EMPTY_STRING;
		String thumbnailW = EMPTY_STRING;

		// Initialize properties
		LOGGER.info("Initializing properties...");
		String displayThumbnailH = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_DISP_THUMB_HEIGHT);
		String displayThumbnailW = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_DISP_THUMB_WIDTH);
		String compareThumbnailH = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_HEIGHT);
		String compareThumbnailW = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_WIDTH);
		final String outputImageParameters = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_OUTPUT_IMAGE_PARAMETERS);

		LOGGER.info("Properties Initialized Successfully");

		if (thumbnailType.equals(IImageMagickCommonConstants.THUMB_TYPE_COMP)) {
			thumbnailH = compareThumbnailH;
			thumbnailW = compareThumbnailW;
		}

		if (thumbnailType.equals(IImageMagickCommonConstants.THUMB_TYPE_DISP)) {
			thumbnailH = displayThumbnailH;
			thumbnailW = displayThumbnailW;
		}
		String[][] sListOfTiffFiles;
		if (!fBatchFolder.exists() || !fBatchFolder.isDirectory()) {
			throw new DCMABusinessException("Improper Folder Specified folder name->" + sBatchFolder);
		}
		LOGGER.info("Finding xml file for thumbnail generation in the folder--> " + fBatchFolder);

		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		try {
			sListOfTiffFiles = getListOfTiffFiles(fBatchFolder, batch, batchInstanceIdentifier, true, thumbnailType,
					batchSchemaService, ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN);
		} catch (Exception e) {
			throw new DCMAApplicationException("Problem generating list of files", e);

		}
		BatchInstanceThread batchInstanceThread = generateThumbnailInternal(batchInstanceIdentifier, thumbnailType, thumbnailH,
				thumbnailW, outputImageParameters, sListOfTiffFiles);
		try {
			LOGGER.info("Executing thumbnail generation by thread pool for " + batchInstanceIdentifier);
			batchInstanceThread.execute();
			LOGGER.info("Finished thumbnail generation by thread pool for " + batchInstanceIdentifier);
		} catch (DCMAApplicationException dcmae) {
			LOGGER.error("Error in generating thumbnails");
			batchInstanceThread.remove();
			batchSchemaService.updateBatch(batch);

			// Throw the exception to set the batch status to Error by Application aspect
			LOGGER.error("Error in generating thumbnails" + dcmae.getMessage(), dcmae);
			throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
		}

		for (int i = 0; i < sListOfTiffFiles.length; i++) {
			addFileToPageSchema(batch, sListOfTiffFiles[i][2], sListOfTiffFiles[i][1], thumbnailType);
			LOGGER.debug("thumbnail " + sListOfTiffFiles[i][1] + " sucsesfully generated");
		}
		LOGGER.info("Persisting all thumbnail info to batch.xml file");
		batchSchemaService.updateBatch(batch);
		LOGGER.info(sListOfTiffFiles.length + " thumbnails succesfully generated");

	}

	public BatchInstanceThread generateThumbnailInternal(final String batchInstanceIdentifier, String thumbnailType,
			String thumbnailH, String thumbnailW, final String outputImageParameters, String[][] sListOfTiffFiles) {
		LOGGER.info("Generating thumbnais");
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);

		final String thumbNailH = thumbnailH;
		final String thumbNailW = thumbnailW;
		final String thumbnailT = thumbnailType;

		for (int i = 0; i < sListOfTiffFiles.length; i++) {
			final String[] files = sListOfTiffFiles[i];
			batchInstanceThread.add(new AbstractRunnable() {

				@Override
				public void run() {
					LOGGER.info("Generating thumbnail for file" + files[0]);

					String inputImageName = files[0];
					String outputImageName = files[1];
					StringBuffer command = new StringBuffer(EMPTY_STRING);
					ArrayList<String> commandList = new ArrayList<String>();
					String outputParams = "";
					// added for color pdf handling.
					// Generate the display thumbnails as gray and compare thumbnails as per the color of input tiff.
					if (thumbnailT.equals(IImageMagickCommonConstants.THUMB_TYPE_DISP)) {
						outputParams = outputImageParameters;
					}

					outputParams += " -thumbnail " + Integer.parseInt(thumbNailH) + "x" + Integer.parseInt(thumbNailW);
					String inputParams = "";
					if (OSUtil.isWindows()) {
						createCommandforWindows(commandList, inputParams, outputParams,
								QUOTES + System.getenv(IMAGEMAGICK_ENV_VARIABLE) + File.separator + "convert\"", QUOTES
										+ inputImageName + QUOTES, QUOTES + outputImageName + QUOTES);

					} else {
						commandList.add(System.getenv(IMAGEMAGICK_ENV_VARIABLE) + File.separator + "convert");
						createCommandForLinux(commandList, inputParams, outputParams, inputImageName, outputImageName);
					}
					String[] cmds = (String[]) commandList.toArray(new String[commandList.size()]);
					LOGGER.info("Starting execution of generating thumbnails");
					for (int ind = 0; ind < cmds.length; ind++) {
						if (cmds[ind] == null) {
							cmds[ind] = "";
						}
						LOGGER.info(cmds[ind]);
						command.append(cmds[ind] + " ");
					}
					LOGGER.info("Convert command = " + command);
					try {
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

					} catch (Exception ex) {
						LOGGER.error(PROBLEM_GENERATING_THUMBNAILS);
						setDcmaApplicationException(new DCMAApplicationException(PROBLEM_GENERATING_THUMBNAILS, ex));
					}
				}
			});
		}
		return batchInstanceThread;
	}

	/**
	 * This method generates the png files which will be used for OCR.
	 * 
	 * @param sBatchFolder
	 * @param batchInstanceIdentifier
	 * @param pluginWorkflowName
	 * @throws DCMAApplicationException
	 * @throws JAXBException
	 */
	public void generateFullFiles(final String sBatchFolder, final String batchInstanceIdentifier,
			BatchSchemaService batchSchemaService, String fileType, String pluginName, String pluginWorkflowName,
			String generateDisplayPng, String inputParameters, String outputParameters) throws DCMAApplicationException, JAXBException {
		String repairImageMagickFileUtilityPath = System.getenv(REPAIR_IMAGE_MAGICK_FILES_ENV_VARIABLE);
		boolean isGenerateDisplayPng = true;
		if (fileType.equals(DISPLAY_IMAGE)) {
			if (generateDisplayPng == null || generateDisplayPng.equalsIgnoreCase("OFF")) {
				isGenerateDisplayPng = false;
			}
		}

		File fBatchFolder = new File(sBatchFolder);
		String[][] sListOfTiffFiles;
		if (!fBatchFolder.exists() || !fBatchFolder.isDirectory()) {
			throw new DCMABusinessException("Improper Folder Specified folder name->" + sBatchFolder);
		}
		LOGGER.info("Finding xml file for PNG generation in the folder--> " + fBatchFolder);
		Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

		try {
			sListOfTiffFiles = getListOfTiffFiles(fBatchFolder, batch, batchInstanceIdentifier, false, EMPTY_STRING,
					batchSchemaService, pluginName);
		} catch (Exception e) {
			throw new DCMAApplicationException("Problem generating list of files", e);
		}
		if (isGenerateDisplayPng) {
			LOGGER.info("Create Display PNG is ON");
			LOGGER.info("Generating PNG Files");
			BatchInstanceThread batchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
			for (int i = 0; i < sListOfTiffFiles.length; i++) {
				final String[] files = sListOfTiffFiles[i];
				try {
					String command = EMPTY_STRING;
					ArrayList<String> commandList = new ArrayList<String>();
					if (OSUtil.isWindows()) {

						commandList.add(repairImageMagickFileUtilityPath + File.separator + "EphesoftImageMagickExecutor.exe");
						createCommandforIMExecutorWindows(commandList, inputParameters, outputParameters,
								System.getenv(IMAGEMAGICK_ENV_VARIABLE) + File.separator + "convert", files[0], files[1]);
					} else {
						commandList.add("convert");
						createCommandForLinux(commandList, inputParameters, outputParameters, SPACE + files[0] + SPACE, SPACE
								+ files[1] + SPACE);
					}
					String[] cmds = (String[]) commandList.toArray(new String[commandList.size()]);
					if (batchInstanceThread != null) {

						LOGGER.info("Generating PNG for file" + files[0]);
						LOGGER.info("Adding generated command to thread pool. Command is : ");
						for (int ind = 0; ind < cmds.length; ind++) {
							LOGGER.info(cmds[ind] + SPACE);
						}
						if (OSUtil.isWindows()) {
							// Replacing ProcessExecutor with EphesoftProcessExecutor
							batchInstanceThread.add(new EphesoftProcessExecutor(cmds, ApplicationConfigProperties
									.getWaitTimeProperty(IM_WAIT_TIME_PROPERTY)));
						} else {
							// Replacing ProcessExecutor with EphesoftProcessExecutor
							batchInstanceThread.add(new EphesoftProcessExecutor(cmds,
									new File(System.getenv(IMAGEMAGICK_ENV_VARIABLE)), ApplicationConfigProperties
											.getWaitTimeProperty(IM_WAIT_TIME_PROPERTY)));
						}
					} else {
						LOGGER.error("Command " + command + " cannot be run");
						throw new DCMAApplicationException("Command " + command + " cannot be run");
					}
				} catch (Exception ex) {
					LOGGER.error("Problem generating PNG");
					throw new DCMAApplicationException("Problem generating PNG", ex);
				}

			}
			try {
				LOGGER.info("Executing PNG generation by thread pool for " + batchInstanceIdentifier);
				batchInstanceThread.execute();
				LOGGER.info("Finished PNG generation by thread pool for " + batchInstanceIdentifier);
			} catch (DCMAApplicationException dcmae) {
				LOGGER.error("Problem generating thumbnails. Setting batch Status to error state." + dcmae.getMessage(), dcmae);
				batchInstanceThread.remove();
				batchSchemaService.updateBatch(batch);

				// Throw the exception to set the batch status to Error by Application aspect
				throw new DCMAApplicationException(dcmae.getMessage(), dcmae);
			}
		} else {
			LOGGER.info("Create Display PNG is OFF");
		}
		for (int i = 0; i < sListOfTiffFiles.length; i++) {
			if (fileType.equals(OCR_INPUT_FILE)) {
				addFileToPageSchema(batch, sListOfTiffFiles[i][2], sListOfTiffFiles[i][1], OCR_INPUT_FILE);
			}
			if (fileType.equals(DISPLAY_IMAGE)) {
				addFileToPageSchema(batch, sListOfTiffFiles[i][2], sListOfTiffFiles[i][1], DISPLAY_IMAGE);
			}
			LOGGER.debug("PNG file " + sListOfTiffFiles[i][1] + " sucsesfully generated");
		}
		LOGGER.info("Persisting all PNG info to batch.xml file ");
		batchSchemaService.updateBatch(batch);
		LOGGER.info(sListOfTiffFiles.length + " PNG files succesfully generated");

	}

	/**
	 * This method adds the newly created file to the object of the batch.xml file. It should be noted that after the execution of this
	 * method the new information added is in the memory and has not yet been persisted to the xml file.
	 * 
	 * @param parsedXmlFile
	 * @param index
	 * @param filePath
	 * @param fileType
	 */
	private void addFileToPageSchema(final Batch parsedXmlFile, final String index, final String filePath, final String fileType) {
		String fileName = new File(filePath).getName();
		LOGGER.debug("Updating xml Object for file" + fileName);
		List<Page> listOfPages = parsedXmlFile.getDocuments().getDocument().get(0).getPages().getPage();

		Page page = listOfPages.get(Integer.parseInt(index));
		if (fileType.equalsIgnoreCase(THUMB_TYPE_DISP)) {
			page.setThumbnailFileName(fileName);
		}
		if (fileType.equalsIgnoreCase(THUMB_TYPE_COMP)) {
			page.setComparisonThumbnailFileName(fileName);
		}
		if (fileType.equalsIgnoreCase(OCR_INPUT_FILE)) {
			page.setOCRInputFileName(fileName);
		}
		if (fileType.equalsIgnoreCase(DISPLAY_IMAGE)) {
			page.setDisplayFileName(fileName);
		}

		LOGGER.debug("List Of Pages=" + listOfPages);

	}

	/**
	 * Returns an array containing the list of thumbnail and PNG files. which have to be created.
	 * 
	 * @param fBatchFolder
	 * @param parsedXmlFile
	 * @param batchInstanceIdentifier
	 * @param thumbNailGeneration
	 * @return
	 * @throws DCMAApplicationException
	 */
	private String[][] getListOfTiffFiles(File fBatchFolder, Batch parsedXmlFile, String batchInstanceIdentifier,
			boolean thumbNailGeneration, String thumbnailType, BatchSchemaService batchSchemaService, String pluginName)
			throws DCMAApplicationException {
		LOGGER.info("Getting the list of tif files from the xml for batchInstanceIdentifier=" + batchInstanceIdentifier);
		FileNameFormatter formatter = null;
		boolean displayImageGeneration = false;
		// Initialize properties
		String displayThumbnailType = null;
		String compareThumbnailType = null;
		LOGGER.info("Initializing properties...");
		if (pluginName.equals(ImageMagicKConstants.CREATE_OCR_INPUT_PLUGIN)) {
			displayThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_OCR_INPUT_PLUGIN, ImageMagicProperties.CREATE_OCR_INPUT_DISP_THUMB_TYPE);
			compareThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_OCR_INPUT_PLUGIN, ImageMagicProperties.CREATE_OCR_INPUT_COMP_THUMB_TYPE);
		} else if (pluginName.equals(ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN)) {
			displayThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_DISP_THUMB_TYPE);
			compareThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_TYPE);
		} else {
			displayImageGeneration = true;
			displayThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_DISPLAY_IMAGE_PLUGIN, ImageMagicProperties.CREATE_DISPLAY_IMAGE_DISP_THUMB_TYPE);
			compareThumbnailType = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
					ImageMagicKConstants.CREATE_DISPLAY_IMAGE_PLUGIN, ImageMagicProperties.CREATE_DISPLAY_IMAGE_COMP_THUMB_TYPE);
		}

		LOGGER.info("Properties Initialized Successfully");

		try {
			formatter = new FileNameFormatter();
		} catch (Exception e) {
			throw new DCMAApplicationException("Problem Initiating formatter", e);
		}
		String[][] pageTiffFileArr;

		List<Document> listOfDocuments = parsedXmlFile.getDocuments().getDocument();
		Document document = listOfDocuments.get(0);
		List<Page> pages = document.getPages().getPage();
		pageTiffFileArr = new String[pages.size()][3];

		StringBuffer sbThumbNailFileName = new StringBuffer();
		StringBuffer sbPNGFileName = new StringBuffer();
		for (int i = 0; i < pages.size(); i++) {
			// Put the New File name int the array

			File pageTiffFile = batchSchemaService.getFile(batchInstanceIdentifier, pages.get(i).getNewFileName());
			String pageTiffFilePath = pageTiffFile.getAbsolutePath();
			pageTiffFileArr[i][0] = pageTiffFilePath;

			if (thumbNailGeneration) {
				LOGGER.info("Generating the list thumbnail files for batchInstanceIdentifier = " + batchInstanceIdentifier);
				sbThumbNailFileName.append(fBatchFolder.getAbsolutePath());
				sbThumbNailFileName.append(File.separator);
				String thumbNailFileName = EMPTY_STRING;
				try {
					if (thumbnailType.equals(IImageMagickCommonConstants.THUMB_TYPE_DISP)) {
						thumbNailFileName = formatter.getDisplayThumbnailFileName(batchInstanceIdentifier, pages.get(i)
								.getOldFileName(), pages.get(i).getNewFileName(), pages.get(i).getOCRInputFileName(),
								displayThumbnailType, pages.get(i).getIdentifier());
					}
					if (thumbnailType.equals(IImageMagickCommonConstants.THUMB_TYPE_COMP)) {
						thumbNailFileName = formatter.getCompareThumbnailFileName(batchInstanceIdentifier, pages.get(i)
								.getOldFileName(), pages.get(i).getNewFileName(), pages.get(i).getOCRInputFileName(),
								compareThumbnailType, pages.get(i).getIdentifier());
					}
				} catch (Exception ex) {
					LOGGER.error("Problem Generating Thumbnail File Name");
					throw new DCMABusinessException("Problem Generating Thumbnail File Name", ex);
				}

				sbThumbNailFileName.append(thumbNailFileName);
				String thumbnailFilePath = sbThumbNailFileName.toString();
				pageTiffFileArr[i][1] = thumbnailFilePath;
				sbThumbNailFileName.delete(0, sbThumbNailFileName.length());
			} else {
				LOGGER.info("Generating the list PNG files for batchInstanceIdentifier = " + batchInstanceIdentifier);
				sbPNGFileName.append(fBatchFolder.getAbsolutePath());
				sbPNGFileName.append(File.separator);
				String pngFileName = null;
				try {
					// To create coloured display images
					if (displayImageGeneration) {
						pngFileName = formatter.getOCRInputFileName(batchInstanceIdentifier, pages.get(i).getOldFileName(),
								pages.get(i).getNewFileName(), FileType.PNG.getExtensionWithDot(), EphesoftStringUtil.concatenate(
										pages.get(i).getIdentifier(), ImageMagicKConstants.UNDERSCORE, DISPLAY_IMAGE));
					} else {
						pngFileName = formatter.getOCRInputFileName(batchInstanceIdentifier, pages.get(i).getOldFileName(),
								pages.get(i).getNewFileName(), FileType.PNG.getExtensionWithDot(), pages.get(i).getIdentifier());
					}
				} catch (Exception exception) {
					throw new DCMAApplicationException("Problem Getting Name from formatter", exception);
				}
				sbPNGFileName.append(pngFileName);
				String pngFilePath = sbPNGFileName.toString();
				pageTiffFileArr[i][1] = pngFilePath;
				sbPNGFileName.delete(0, sbPNGFileName.length());
			}
			pageTiffFileArr[i][2] = Integer.toString(i);

		}

		return pageTiffFileArr;

	}

	public void generatePNGForImage(File imagePath) throws DCMAApplicationException {
		ConvertCmd convertcmd = new ConvertCmd();
		IMOperation operationPNG = new IMOperation();
		operationPNG.addImage();
		operationPNG.addImage();

		String imageName = imagePath.getAbsolutePath();
		// TODO remove hard coding _thumb.png, .png and .tif
		String pngPath = imageName.substring(0, imageName.lastIndexOf(DOT));
		try {
			String[] listOfFiles = {imageName, pngPath + FileType.PNG.getExtensionWithDot()};
			convertcmd.run(operationPNG, (Object[]) listOfFiles);
		} catch (Exception ex) {
			LOGGER.error("Problem generating png.");
			throw new DCMAApplicationException("Problem generating png.", ex);
		}
	}

	private String createCommandforWindows(ArrayList<String> commandList, String inputParams, String outputParams, String environment,
			String inputImageName, String outputImageName) {
		if (environment != null) {
			commandList.add(environment);
		}
		StringBuffer command = new StringBuffer(EMPTY_STRING);
		if (!inputParams.trim().isEmpty()) {
			commandList.add(inputParams.trim());
		}
		commandList.add(inputImageName.trim());
		if (!outputParams.trim().isEmpty()) {
			commandList.add(outputParams.trim());
		}
		commandList.add(outputImageName.trim());
		return command.toString();
	}

	private String createCommandforIMExecutorWindows(ArrayList<String> commandList, String inputParams, String outputParams,
			String environment, String inputImageName, String outputImageName) {
		if (environment != null) {
			commandList.add(QUOTES + environment + QUOTES);
		}
		StringBuffer command = new StringBuffer(EMPTY_STRING);
		commandList.add(QUOTES + inputParams.trim() + QUOTES);
		commandList.add(QUOTES + inputImageName.trim() + QUOTES);

		commandList.add(QUOTES + outputParams.trim() + QUOTES);
		commandList.add(QUOTES + outputImageName.trim() + QUOTES);
		return command.toString();
	}

	private String createCommandForLinux(ArrayList<String> commandList, String inputParams, String outputParams,
			String inputImageName, String outputImageName) {
		StringBuffer command = new StringBuffer(EMPTY_STRING);
		String inputParamsArr[] = inputParams.split(SPACE);
		for (String string : inputParamsArr) {
			if (!string.trim().isEmpty()) {
				commandList.add(string.trim());
			}
		}
		commandList.add(inputImageName.trim());
		String outputParamsArr[] = outputParams.split(SPACE);
		for (String string : outputParamsArr) {
			if (!string.trim().isEmpty()) {
				commandList.add(string.trim());
			}
		}
		commandList.add(outputImageName.trim());
		return command.toString();
	}

	public String getThumbnailHeightForScannedImage() {
		return thumbnailHeightForScannedImage;
	}

	public void setThumbnailHeightForScannedImage(String thumbnailHeightForScannedImage) {
		this.thumbnailHeightForScannedImage = thumbnailHeightForScannedImage;
	}

	public String getThumbnailWidthForScannedImage() {
		return thumbnailWidthForScannedImage;
	}

	public void setThumbnailWidthForScannedImage(String thumbnailWidthForScannedImage) {
		this.thumbnailWidthForScannedImage = thumbnailWidthForScannedImage;
	}

	public void setPngWidthForScannedImage(String pngWidthForScannedImage) {
		this.pngWidthForScannedImage = pngWidthForScannedImage;
	}

	public String getPngWidthForScannedImage() {
		return pngWidthForScannedImage;
	}

	public void setPngHeightForScannedImage(String pngHeightForScannedImage) {
		this.pngHeightForScannedImage = pngHeightForScannedImage;
	}

	public String getPngHeightForScannedImage() {
		return pngHeightForScannedImage;
	}
	
	/**
	 * Sets the output parameter for thumbnail image.
	 */
	public void setOutputParamThumbnail(String outputParamThumbnail) {
		this.outputParamThumbnail = outputParamThumbnail;
	}

	/**
	 * Sets the output parameter for display image.
	 */
	public void setOutputParamImage(String outputParamImage) {
		this.outputParamImage = outputParamImage;
	}
}
