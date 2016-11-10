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
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.util.constant.CommonConstants;

/**
 * This file is used to maintain the back up file at every plugin level.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class BackUpFileService {

	/**
	 *
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BackUpFileService.class);

	public static final String BACKUP_PROPERTY_FILE = "META-INF" + File.separator + "dcma-util" + File.separator
			+ "dcma-backup-service.properties";

	private static final String APPLICATION_PROPERTY_FILE = "META-INF" + File.separator + "application.properties";

	private static final String PRE_BATCH_XML = "PRE_STATE_";

	private static final String BASE_FOL_LOC = "backup.local_folder";

	private static final String INPUT_BATCH_XML = "backup.input_batch_xml";

	private static final String INPUT_BATCH_XML_ZIP = "backup.input_batch_xml_zip";

	private static final String REPORT_BASE_FOL_LOC = "backup.report_folder";

	private static final String ENABLE_REPORTING = "enable.reporting";

	private static final String OUTPUT_BATCH_XML = "backup.output_batch_xml";

	private static final String BACK_UP_XML_SWITCH = "backup.batch_xml_backup_switch";

	private static final String OUTPUT_BATCH_XML_ZIP = "backup.output_batch_xml_zip";

	private static final String ZIP_SWITCH = "zip_switch";

	private static Properties backUpFileConfig = new Properties();

	private static Properties applicationConfig = new Properties();

	private static String backUpSwitchStatus = null;

	private static final String ON = "ON";

	private static final int WAIT_TIME = 1000;

	/**
	 * Reporting switch value.
	 */
	private static Boolean reportingSwitch;

	/**
	 * An object used for synchronization.
	 */
	private static Object object = new Object();

	static {
		fetchConfig();
		fetchApplicationConfig();
	}

	public static Boolean isBackupRequired() {
		getBackupSwitchFromProperty();
		boolean backupRequired = false;
		if (ON.equalsIgnoreCase(backUpSwitchStatus)) {
			backupRequired = true;
		}
		return backupRequired;
	}

	/**
	 * Open application.properties containing backup configuration parameters, and populate a corresponding Properties object.
	 **/
	private static void fetchApplicationConfig() {

		ClassPathResource classPathResource = new ClassPathResource(APPLICATION_PROPERTY_FILE);

		InputStream input = null;
		try {
			input = classPathResource.getInputStream();
			applicationConfig.load(input);
		} catch (IOException ex) {
			LOGGER.error("Cannot open and load application properties file.", ex);
			// System.exit(1);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				LOGGER.error("Cannot close application properties file.", ex);
			}
		}
	}

	/**
	 * Open backUpService.properties containing backup configuration parameters, and populate a corresponding Properties object.
	 **/
	private static void fetchConfig() {

		ClassPathResource classPathResource = new ClassPathResource(BACKUP_PROPERTY_FILE);

		InputStream input = null;
		try {
			input = classPathResource.getInputStream();
			backUpFileConfig.load(input);
		} catch (IOException ex) {
			LOGGER.error("Cannot open and load backUpService properties file.", ex);
			// System.exit(1);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException ex) {
				LOGGER.error("Cannot close backUpService properties file.", ex);
			}
		}
	}

	/**
	 * This method will called before service method call to generate the back up file.
	 * 
	 * @param batchInstanceIdentifier
	 */
	public static void backUpBatch(final String batchInstanceIdentifier) {
		LOGGER.info("Entering backUpBatch method.");
		boolean backUpRequired = isBackupRequired();
		if (backUpRequired) {
			boolean preserveFileDate = false;
			boolean isZipSwitchOn = Boolean.parseBoolean(applicationConfig.getProperty(ZIP_SWITCH));
			LOGGER.info(EphesoftStringUtil.concatenate("Zipped Batch XML switch is:", isZipSwitchOn));
			String baseFolLoc = FileUtils.getAbsoluteFilePath(backUpFileConfig.getProperty(BASE_FOL_LOC));
			String inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML);
			String baseFolPath = EphesoftStringUtil.concatenate(baseFolLoc, File.separator, batchInstanceIdentifier, File.separator);
			String inputFileName = EphesoftStringUtil.concatenate(batchInstanceIdentifier, inputBatchXml);
			String outputFileName = EphesoftStringUtil.concatenate(PRE_BATCH_XML, inputFileName);
			LOGGER.info(EphesoftStringUtil.concatenate("batchInstanceIdentifier : ", batchInstanceIdentifier, "  inputFileName : ",
					inputFileName, "  outputFileName : ", outputFileName));
			String srcFilePath = EphesoftStringUtil.concatenate(baseFolPath, inputFileName);
			String destFilePath = EphesoftStringUtil.concatenate(baseFolPath, outputFileName);
			boolean isZip = false;
			isZip = checkZipFile(isZipSwitchOn, srcFilePath, isZip);
			LOGGER.info(EphesoftStringUtil.concatenate("isZip in back service is : ", isZip));
			final String xmlOutputFileName = outputFileName;
			if (isZip) {
				inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML_ZIP);
				inputFileName = EphesoftStringUtil.concatenate(batchInstanceIdentifier, inputBatchXml);
				outputFileName = EphesoftStringUtil.concatenate(PRE_BATCH_XML, inputFileName);
				srcFilePath = EphesoftStringUtil.concatenate(baseFolPath, inputFileName);
				destFilePath = EphesoftStringUtil.concatenate(baseFolPath, outputFileName);
			}

			// The source file name to be copied.
			File srcFile = new File(srcFilePath);

			// changes with respect to ticket 2692: invalid batch xml creation in report-data folder for import multipage plugin.
			if (null != srcFile && srcFile.exists()) {

				// The target file name to which the source file will be copied.
				File destFile = new File(destFilePath);
				garbageCollector();
				try {

					// Changes done to accommodate ticket 2536: java.lang.Error
					if (FileUtils.isFileLocked(srcFile)) {
						LOGGER.error(EphesoftStringUtil.concatenate("Back up of file: ", srcFilePath,
								" cannot be made since it is used by another process."));
					} else {

						// Use of Ephesoft FileUtils instead of apache FileUtils. Fix for ticket id 5262.
						FileUtils.copyFile(srcFile, destFile);
					}
					LOGGER.info(EphesoftStringUtil.concatenate("Successfully copy of the file for batch Instance identifier : ",
							batchInstanceIdentifier));
				} catch (IOException e) {
					LOGGER.trace(EphesoftStringUtil.concatenate("Unable to copy the file for batch Instance identifier : ",
							batchInstanceIdentifier));
					LOGGER.trace(e.getMessage());
				}
				if (getReportingSwitchValue()) {
					createBackUpForReporting(batchInstanceIdentifier, null, preserveFileDate,
							EphesoftStringUtil.concatenate(backUpFileConfig.getProperty(REPORT_BASE_FOL_LOC), File.separator,
									batchInstanceIdentifier, File.separator), outputFileName, xmlOutputFileName, srcFile);
				} else {
					LOGGER.info("Reporting Sevice is disabled.");
				}
			}
		}
		LOGGER.info("Exiting backUpBatch method.");
	}

	/**
	 * Fetches reporting switch value from application.properties file.
	 * 
	 * @return Reporting switch value as {@link Boolean} true or false.
	 * 
	 */
	private static Boolean getReportingSwitchValue() {
		if (null == reportingSwitch) {
			synchronized (object) {
				if (null == reportingSwitch) {
					reportingSwitch = Boolean.parseBoolean(applicationConfig.getProperty(ENABLE_REPORTING));
				}
			}
		}
		return reportingSwitch;
	}

	/**
	 * This method will called with resume service method to generate the back up file.
	 * 
	 * @param batchInstanceIdentifier
	 */
	public static void restoreBatch(final String batchInstanceIdentifier) {
		LOGGER.info("Entering restore batch method.");
		boolean isZipSwitchOn = Boolean.parseBoolean(applicationConfig.getProperty(ZIP_SWITCH));
		LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);
		String baseFolLoc = backUpFileConfig.getProperty(BASE_FOL_LOC);
		String inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML);
		String baseFolPath = baseFolLoc + File.separator + batchInstanceIdentifier + File.separator;
		String inputFileName = batchInstanceIdentifier + inputBatchXml;
		String outputFileName = PRE_BATCH_XML + inputFileName;
		LOGGER.info(EphesoftStringUtil.concatenate("Batch instance identifier: ", batchInstanceIdentifier, "  inputFileName : ",
				inputFileName, "  outputFileName : ", outputFileName));
		String srcFilePath = baseFolPath + outputFileName;
		String destFilePath = baseFolPath + inputFileName;
		boolean isZip = false;
		isZip = checkZipFile(isZipSwitchOn, srcFilePath, isZip);
		if (isZip) {
			inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML_ZIP);
			inputFileName = batchInstanceIdentifier + inputBatchXml;
			outputFileName = PRE_BATCH_XML + inputFileName;
			srcFilePath = baseFolPath + outputFileName;
			destFilePath = baseFolPath + inputFileName;
		}
		copyBackUpFiles(batchInstanceIdentifier, srcFilePath, destFilePath);
		LOGGER.info("Exiting restore batch method.");
	}

	/**
	 * @param batchInstanceIdentifier
	 * @param srcFilePath
	 * @param destFilePath
	 */
	private static void copyBackUpFiles(final String batchInstanceIdentifier, String srcFilePath, String destFilePath) {
		// The source file name to be copied.
		File srcFile = new File(srcFilePath);

		// changes with respect to ticket 2692: invalid batch xml creation in report-data folder for import multipage plugin.
		if (null != srcFile && srcFile.exists()) {

			// The target file name to which the source file will be copied.
			File destFile = new File(destFilePath);
			garbageCollector();
			try {

				// Changes done to accommodate ticket 2536: java.lang.Error
				if (FileUtils.isFileLocked(srcFile)) {
					LOGGER.error(EphesoftStringUtil.concatenate("Back up of file: ", srcFilePath,
							" cannot be made since it is used by another process."));
				} else {
					// Use of Ephesoft FileUtils instead of apache FileUtils. Fix for ticket id 5262.
					FileUtils.copyFile(srcFile, destFile);
				}
				LOGGER.info("Successfully copy of the file for batch Instance identifier : " + batchInstanceIdentifier);
			} catch (IOException e) {
				LOGGER.error("Unable to copy the file for batch Instance identifier : " + batchInstanceIdentifier);
				LOGGER.error(e.getMessage());
			}
		}
	}

	/**
	 * @param isZipSwitchOn
	 * @param srcFilePath
	 * @param isZip
	 * @return
	 */
	private static boolean checkZipFile(boolean isZipSwitchOn, String srcFilePath, boolean isZip) {
		if (isZipSwitchOn) {
			if (FileUtils.isZipFileExists(srcFilePath)) {
				LOGGER.info("zip file exists for the xml file whose File path is: " + srcFilePath);
				isZip = true;
			}
		} else {
			if (new File(srcFilePath).exists()) {
				LOGGER.info("zip file exists. File path is: " + srcFilePath);
				isZip = false;
			} else if (FileUtils.isZipFileExists(srcFilePath)) {
				LOGGER.info("zip file exists for the xml file whose File path is: " + srcFilePath);
				isZip = true;

			}
		}
		return isZip;
	}

	public static String getOutputFileProperty() {
		String outputBatchXml = backUpFileConfig.getProperty(OUTPUT_BATCH_XML);
		return outputBatchXml;

	}

	/**
	 * This method will called after service method call to generate the back up file.
	 * 
	 * @param batchInstanceIdentifier
	 * @param pluginName
	 */
	public static void copyBatchXML(final String batchInstanceIdentifier, final String moduleName) {
		LOGGER.info("Entering copyBatchXML method.");

		boolean backUpRequired = isBackupRequired();
		if (backUpRequired && getReportingSwitchValue()) {

			boolean isZipSwitchOn = Boolean.parseBoolean(applicationConfig.getProperty(ZIP_SWITCH));
			LOGGER.info(EphesoftStringUtil.concatenate(EphesoftStringUtil.concatenate("Zipped Batch XML switch is:", isZipSwitchOn)));
			try {
				String baseFolLoc = backUpFileConfig.getProperty(BASE_FOL_LOC);
				if (EphesoftStringUtil.isNullOrEmpty(baseFolLoc)) {
					throw new Exception(EphesoftStringUtil.concatenate("Invalid Property:", BASE_FOL_LOC));
				}
				String outputBatchXml = backUpFileConfig.getProperty(OUTPUT_BATCH_XML);
				if (EphesoftStringUtil.isNullOrEmpty(outputBatchXml)) {
					throw new Exception(EphesoftStringUtil.concatenate("Invalid Property:", BASE_FOL_LOC));
				}
				String baseFolPath = EphesoftStringUtil.concatenate(baseFolLoc, File.separator, batchInstanceIdentifier,
						File.separator);
				String reportBaseFolPath = EphesoftStringUtil.concatenate(backUpFileConfig.getProperty(REPORT_BASE_FOL_LOC),
						File.separator, batchInstanceIdentifier, File.separator);
				String inputFileName = EphesoftStringUtil.concatenate(batchInstanceIdentifier, CommonConstants.FILE_NAME_SEPARATOR,
						moduleName, outputBatchXml);
				String outputFileName = inputFileName;

				LOGGER.info(EphesoftStringUtil.concatenate("batchInstanceIdentifierIdentifier : ", batchInstanceIdentifier,
						"  inputFileName : ", inputFileName, "  outputFileName : ", outputFileName));
				String srcFilePath = EphesoftStringUtil.concatenate(baseFolPath, inputFileName);
				String destFilePath = EphesoftStringUtil.concatenate(reportBaseFolPath, outputFileName);
				boolean isZip = false;
				isZip = checkZipFile(isZipSwitchOn, srcFilePath, isZip);
				if (isZip) {
					outputBatchXml = backUpFileConfig.getProperty(OUTPUT_BATCH_XML_ZIP);
					if (EphesoftStringUtil.isNullOrEmpty(outputBatchXml)) {
						throw new Exception(EphesoftStringUtil.concatenate("Invalid Property:", OUTPUT_BATCH_XML_ZIP));
					}
					inputFileName = EphesoftStringUtil.concatenate(batchInstanceIdentifier, CommonConstants.FILE_NAME_SEPARATOR,
							moduleName, outputBatchXml);
					outputFileName = EphesoftStringUtil.concatenate(batchInstanceIdentifier, CommonConstants.FILE_NAME_SEPARATOR,
							moduleName, outputBatchXml);
					srcFilePath = EphesoftStringUtil.concatenate(baseFolPath, inputFileName);
					destFilePath = EphesoftStringUtil.concatenate(reportBaseFolPath, outputFileName);
				}

				LOGGER.info("Source File in copying the back up is:" + srcFilePath);
				LOGGER.info("Destination File in copying the back up is:" + destFilePath);

				// The source file name to be copied.
				File srcFile = new File(srcFilePath);

				if (null != srcFile && null != moduleName && srcFile.exists() && !srcFile.isDirectory()) {
					// The target file name to which the source file will be copied.
					File destFile = new File(destFilePath);

					try {
						garbageCollector();

						// Changes done to accommodate ticket 2536: java.lang.Error
						if (FileUtils.isFileLocked(srcFile)) {
							LOGGER.error(EphesoftStringUtil.concatenate("Back up of file: ", srcFilePath,
									" cannot be made since it is used by another process."));
						} else {
							// Use of Ephesoft FileUtils instead of apache FileUtils. Fix for ticket id 5262.
							FileUtils.copyFile(srcFile, destFile);
							LOGGER.info(EphesoftStringUtil.concatenate("Destination file status for copy operation is :",
									destFile.exists(), " .Destination file path is :", destFilePath));
							if (!destFile.exists()) {
								LOGGER.info(EphesoftStringUtil.concatenate(
										"Destination file does not exist.Trying to copy it again. Batch instance id :",
										batchInstanceIdentifier));
								try {
									Thread.sleep(WAIT_TIME);
								} catch (InterruptedException interruptedException) {
									LOGGER.error("Some problem occur while calling sleep method.");
								}
								// Use of Ephesoft FileUtils instead of apache FileUtils. Fix for ticket id 5262.
								FileUtils.copyFile(srcFile, destFile);
								LOGGER.info("Copy operation performed again succesfully.");
							}
						}
						LOGGER.info("Successfully copy of the file for the plugin : " + moduleName + " and batch Instance Id : "
								+ batchInstanceIdentifier);
					} catch (IOException e) {
						LOGGER.error("Unable to copy the file for the plugin : " + moduleName + " and batch Instance Id : "
								+ batchInstanceIdentifier);
						LOGGER.error(e.getMessage());
					}
				}

			} catch (Exception ex) {
				LOGGER.error(ex.getMessage());
			}
		}
		LOGGER.info("Exiting backup batch method.");
	}

	public static void backUpBatchXml(final String batchInstanceIdentifier, final String pluginName) {
		LOGGER.info("Entering backUpBatch method.");

		getBackupSwitchFromProperty();

		if (ON.equalsIgnoreCase(backUpSwitchStatus)) {

			boolean preserveFileDate = false;

			boolean isZipSwitchOn = Boolean.parseBoolean(applicationConfig.getProperty(ZIP_SWITCH));
			LOGGER.info("Zipped Batch XML switch is:" + isZipSwitchOn);

			String baseFolLoc = backUpFileConfig.getProperty(BASE_FOL_LOC);

			String inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML);

			String outputBatchXml = backUpFileConfig.getProperty(OUTPUT_BATCH_XML);

			String baseFolPath = baseFolLoc + File.separator + batchInstanceIdentifier + File.separator;

			String reportBaseFolPath = backUpFileConfig.getProperty(REPORT_BASE_FOL_LOC) + File.separator + batchInstanceIdentifier
					+ File.separator;

			String inputFileName = batchInstanceIdentifier + inputBatchXml;

			String outputFileName = batchInstanceIdentifier + "_" + pluginName + outputBatchXml;

			String xmlOutputFileName = outputFileName;

			LOGGER.info("batchInstanceIdentifierIdentifier : " + batchInstanceIdentifier + "  inputFileName : " + inputFileName
					+ "  outputFileName : " + outputFileName);

			String srcFilePath = baseFolPath + inputFileName;
			String destFilePath = baseFolPath + outputFileName;

			boolean isZip = false;

			isZip = checkZipFile(isZipSwitchOn, srcFilePath, isZip);
			if (isZip) {
				inputBatchXml = backUpFileConfig.getProperty(INPUT_BATCH_XML_ZIP);
				outputBatchXml = backUpFileConfig.getProperty(OUTPUT_BATCH_XML_ZIP);
				inputFileName = batchInstanceIdentifier + inputBatchXml;
				outputFileName = batchInstanceIdentifier + "_" + pluginName + outputBatchXml;
				srcFilePath = baseFolPath + inputFileName;
				destFilePath = baseFolPath + outputFileName;
			}

			LOGGER.info("Source File in copying the back up is:" + srcFilePath);
			LOGGER.info("Destination File in copying the back up is:" + destFilePath);

			// The source file name to be copied.
			File srcFile = new File(srcFilePath);

			if (null != srcFile && null != pluginName && srcFile.exists() && !srcFile.isDirectory()) {
				// The target file name to which the source file will be copied.
				File destFile = new File(destFilePath);

				try {
					garbageCollector();

					// Changes done to accommodate ticket 2536: java.lang.Error
					if (FileUtils.isFileLocked(srcFile)) {
						LOGGER.error(EphesoftStringUtil.concatenate("Back up of file: ", srcFilePath,
								" cannot be made since it is used by another process."));
					} else {
						// Use of Ephesoft FileUtils instead of apache FileUtils. Fix for ticket id 5262.
						FileUtils.copyFile(srcFile, destFile);
						LOGGER.info(EphesoftStringUtil.concatenate("Destination file status for copy operation is :",
								destFile.exists(), " .Destination file path is :", destFilePath));
						if (!destFile.exists()) {
							LOGGER.info(EphesoftStringUtil.concatenate(
									"Destination file does not exist.Trying to copy it again. Batch instance id :",
									batchInstanceIdentifier));
							try {
								Thread.sleep(WAIT_TIME);
							} catch (InterruptedException interruptedException) {
								LOGGER.error("Some problem occur while calling sleep method.");
							}
							// Use of Ephesoft FileUtils instead of apache FileUtils. Fix for ticket id 5262.
							FileUtils.copyFile(srcFile, destFile);
							LOGGER.info("Copy operation performed again succesfully.");
						}
					}
					LOGGER.info("Successfully copy of the file for the plugin : " + pluginName + " and batch Instance Id : "
							+ batchInstanceIdentifier);
				} catch (IOException e) {
					LOGGER.error("Unable to copy the file for the plugin : " + pluginName + " and batch Instance Id : "
							+ batchInstanceIdentifier);
					LOGGER.error(e.getMessage());
				}
				if (getReportingSwitchValue()) {
					createBackUpForReporting(batchInstanceIdentifier, pluginName, preserveFileDate, reportBaseFolPath, outputFileName,
							xmlOutputFileName, srcFile);
				} else {
					LOGGER.info("Reporting Sevice is disabled.");
				}
			}
		}
		LOGGER.info("Exiting backup batch method.");
	}

	/**
	 * 
	 */
	private static void getBackupSwitchFromProperty() {
		if (EphesoftStringUtil.isNullOrEmpty(backUpSwitchStatus)) {
			backUpSwitchStatus = backUpFileConfig.getProperty(BACK_UP_XML_SWITCH);
			LOGGER.info(EphesoftStringUtil.concatenate("Back Up Switch status : ", backUpSwitchStatus));
			// If backup.batch_xml_backup_switch property is not specified properly set it's default value as ON.
			if (EphesoftStringUtil.isNullOrEmpty(backUpSwitchStatus)) {
				backUpSwitchStatus = ON;
				LOGGER.error("Either backup.batch_xml_backup_switch property is not defined or it's defined value is not correct. By default setting it's value as ON.");
			}
		}
	}

	/**
	 * Creates back-up of batch xml file in report-data folder present inside SharedFolders.
	 * 
	 * @param batchInstanceIdentifier {@link String} of current batch.
	 * @param pluginName {@link String} name of plugin.
	 * @param preserveFileDate {@link boolean} flag to indicate whether to preserve file date or not.
	 * @param reportBaseFolPath {@link String} reporting meta data folder path.
	 * @param outputFileName {@link String} name of output batch xml zip file.
	 * @param xmlOutputFileName {@link String} name of output batch xml file.
	 * @param srcFilePath {@link File} source file to be copied.
	 */
	private static void createBackUpForReporting(final String batchInstanceIdentifier, final String pluginName,
			final boolean preserveFileDate, final String reportBaseFolPath, final String outputFileName,
			final String xmlOutputFileName, final File srcFile) {

		// back-up file to be created.
		final File destZipFile = new File(EphesoftStringUtil.concatenate(reportBaseFolPath, outputFileName));
		final File destXMLFile = new File(EphesoftStringUtil.concatenate(reportBaseFolPath, xmlOutputFileName));

		// Delete xml file if already exists.
		if (null != xmlOutputFileName && destXMLFile.exists()) {
			FileUtils.deleteQuietly(destXMLFile);
		}

		// Delete destination file if already exists.
		if (null != destZipFile && destZipFile.exists()) {
			FileUtils.deleteQuietly(destZipFile);
		}
		try {

			// Explicit call to Garbage collector
			garbageCollector();

			// Log error message if source file is in use by some other process.
			if (FileUtils.isFileLocked(srcFile)) {
				LOGGER.error(EphesoftStringUtil.concatenate("Back up of file: ", srcFile.getAbsolutePath(),
						" cannot be made since it is used by another process."));
			} else {
				// Use of Ephesoft FileUtils instead of apache FileUtils. Fix for ticket id 5262.
				FileUtils.copyFile(srcFile, destZipFile);
				LOGGER.info(EphesoftStringUtil.concatenate("Report : Successfully copy of the file for the plugin : ", pluginName,
						" and batch Instance Id : ", batchInstanceIdentifier));
			}
		} catch (final IOException exception) {
			LOGGER.error(EphesoftStringUtil.concatenate("Report : Unable to copy the file for the plugin : ", pluginName,
					" and batch Instance Id : ", batchInstanceIdentifier, ": Exception is ", exception.getMessage()), exception);
		}
	}

	/**
	 * Explicit call to garbage collector.
	 * 
	 */
	private static void garbageCollector() {
		System.gc();
	}
}
