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

package com.ephesoft.dcma.core.threadpool;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.ProcessResultType;
import com.ephesoft.dcma.util.ProcessUtils;
import com.ephesoft.dcma.util.exception.ProcessTimeOutException;

/**
 * Class to execute and a control a process. This class provides proper retry mechanism if process fails to execute and wait time
 * property which defines maximum time to wait for a process to execute completely. If process can't execute within wait time then
 * process will be terminated and process will be re run by executor.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jun 20, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class EphesoftProcessExecutor extends AbstractRunnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(EphesoftProcessExecutor.class);

	private static final int DEFAULT_MAX_RETRIES = 0;

	private static Integer maximumRetries = null;

	/**
	 * String to hold read_process_error_logs switch value.
	 */
	private static String readProcessErrorLogs = null;

	/**
	 * String constant for maximum number of retries in case of executor command failure
	 */
	private static final String MAXIMUM_NUMBER_OF_RETRIES = "maximum_retries_on_executor_failure";

	/**
	 * String constant for read_process_error_logs switch.
	 */
	private static final String READ_PROCESS_ERROR_LOGS = "read_process_error_logs";

	/**
	 * Wait time for error logs processing.
	 */
	private static Integer errorLogsProcessingWaitTime = null;

	/**
	 * String constant for error_logs_processing_wait_time property.
	 */
	private static final String ERROR_LOGS_PROCESSING_WAIT_TIME = "error_logs_processing_wait_time";

	/**
	 * Default Wait time for error logs processing.
	 */
	private static final Integer DEFAULT_WAIT_TIME = 0;

	private final String[] command;
	private final File workingDirectory;
	private final boolean isRetry;

	/**
	 * Multipage image on which the process needs to be executed.
	 */
	private final File imagePath;

	/**
	 * Boolean value indicates whether or not to delete the image post process.
	 */
	private final boolean deleteImage;

	/**
	 * int value indicates wait-time for process. If process does not complete within wait-time then process will be terminated.
	 */
	private final int waitTime;

	/**
	 * Object used for synchronization.
	 */
	private static Object object = new Object();

	/**
	 * Boolean value indicates whether or not to perform learning of a batch class.
	 */
	private final boolean performLearning;

	/**
	 * Gets {@link EphesoftProcessExecutor} instance on the basis of the command given with no retry in case of failure.
	 * 
	 * @param command specifies the command to be executed
	 */
	public EphesoftProcessExecutor(final String[] command) {
		this(command, null);
	}

	/**
	 * Gets {@link EphesoftProcessExecutor} instance on the basis of the command given and in the specified working directory with no
	 * retry in case of failure.
	 * 
	 * @param command specifies the command to be executed
	 * @param workingDirectory in which the command is to be executed
	 */
	public EphesoftProcessExecutor(final String[] command, final File workingDirectory) {
		this(command, workingDirectory, 10);
	}

	/**
	 * Gets {@link EphesoftProcessExecutor} instance on the basis of the command given and in the specified working directory with no
	 * retry in case of failure.
	 * 
	 * @param command specifies the command to be executed
	 * @param waitTime Specifies Wait time for process and if process does not complete within wait time then it will be terminated
	 */
	public EphesoftProcessExecutor(final String[] command, final int waitTime) {
		this(command, null, waitTime);
	}

	/**
	 * Gets {@link EphesoftProcessExecutor} instance on the basis of the command , working directory and whether to retry in case of
	 * failure specified.
	 * 
	 * @param command specifies the command to be executed
	 * @param workingDirectory in which the command is to be executed
	 * @param waitTime for process and if process does not complete within wait time then it will be terminated
	 */
	public EphesoftProcessExecutor(final String[] command, final File workingDirectory, final int waitTime) {
		this(command, workingDirectory, waitTime, true);
	}

	/**
	 * Gets {@link EphesoftProcessExecutor} instance on the basis of the command , working directory and whether to retry in case of
	 * failure specified.
	 * 
	 * @param command specifies the command to be executed
	 * @param workingDirectory in which the command is to be executed
	 * @param waitTime for process and if process does not complete within wait time then process will be terminated
	 * @param isRetry Specifies whether the command needs to be retried in case of failure
	 */
	public EphesoftProcessExecutor(final String[] command, final File workingDirectory, final int waitTime, final boolean isRetry) {
		this(command, workingDirectory, waitTime, isRetry, false);
	}

	/**
	 * Gets {@link EphesoftProcessExecutor} instance on the basis of the command , working directory and whether to retry in case of
	 * failure specified.
	 * 
	 * @param command specifies the command to be executed
	 * @param workingDirectory in which the command is to be executed
	 * @param waitTime for process and if process does not complete within wait time then process will be terminated
	 * @param isRetry specifies whether the command needs to be retried in case of failure
	 * @param deleteImage specifies whether or not to delete the image post process
	 * 
	 */
	public EphesoftProcessExecutor(final String[] command, final File workingDirectory, final int waitTime, final boolean isRetry,
			final boolean deleteImage) {
		this(command, workingDirectory, waitTime, isRetry, deleteImage, null, false);
	}

	/**
	 * Gets {@link EphesoftProcessExecutor} instance on the basis of the command , working directory and whether to retry in case of
	 * failure specified.
	 * 
	 * @param command specifies the command to be executed
	 * @param workingDirectory in which the command is to be executed
	 * @param waitTime for process and if process does not complete within wait time then process will be terminated
	 * @param isRetry specifies whether the command needs to be retried in case of failure
	 * @param deleteImage specifies whether or not to delete the image post process
	 * @param imagePath The multipage image on which the process needs to be executed
	 * 
	 */
	public EphesoftProcessExecutor(final String[] command, final File workingDirectory, final int waitTime, final boolean isRetry,
			final boolean deleteImage, final File imagePath, final boolean performLearning) {
		super();
		this.command = command;
		this.workingDirectory = workingDirectory;
		this.waitTime = waitTime;
		this.isRetry = isRetry;
		this.deleteImage = deleteImage;
		this.imagePath = imagePath;
		this.performLearning = performLearning;
	}

	@Override
	public void run() {
		this.executeProcess(this.command, this.workingDirectory, 0, this.waitTime);
	}

	/**
	 * Defines the entire lifecycle of process execution:
	 * <ol>
	 * <li>Executes the command in the specified directory</li>
	 * <li>Fetches and logs the process log</li>
	 * <li>Waits for Command Completion</li>
	 * <li>Retries in case of unsuccessful termination of command</li>
	 * </ol>
	 * 
	 * @param command to be fired
	 * @param workingDirectory for command execution
	 * @param retryCount Indicates number-of-times a command will executed in case of failure
	 * @param waitTime is maximum time to wait for successful command execution
	 * 
	 */
	private void executeProcess(final String[] command, final File workingDirectory, final int retryCount, final int waitTime) {

		// Execution of the commmand in the specified working directory
		final Process process = ProcessUtils.executeCommand(command, workingDirectory);
		String debugString = "Executing command: \"";
		debugString = EphesoftStringUtil.concatenate(debugString, Arrays.toString(command));
		if (workingDirectory != null) {
			debugString = EphesoftStringUtil.concatenate(debugString, "\" in the working directory: \"", workingDirectory
					.getAbsolutePath());
		}
		debugString = EphesoftStringUtil.concatenate(debugString, "\". (Retry Number=", retryCount, ")\". \n");

		boolean timeOutOcccured = false;
		String outputLogs = null;
		boolean errorOcccured = false;
		String errorLogs = null;

		// Get error process logs
		if (ICommonConstants.ON.equalsIgnoreCase(this.getReadErrorLogsSwitch())) {
			errorLogs = ProcessUtils.getProcessErrorLog(process, this.getErrorLogsProcessingTime());
		}
		if (EphesoftStringUtil.isNullOrEmpty(errorLogs)) {
			LOGGER.debug("No error logs of process generated.");

			// Get input process logs
			try {
				outputLogs = ProcessUtils.getProcessLog(process, waitTime);
				LOGGER.debug("Input logs of process generated successfully.");
			} catch (final ProcessTimeOutException processTimeOutException) {
				LOGGER.error("Unable to get input logs of process due to timeout. ", processTimeOutException);
				timeOutOcccured = true;
			}
		} else {
			errorOcccured = true;
		}

		ProcessResultType exitResultType = null;
		if (errorOcccured || timeOutOcccured) {
			debugString = EphesoftStringUtil
					.concatenate(debugString,
							"The process log is: No error, output logs are produced by the process or process is taking too much time so destroying it.");
			LOGGER.error(debugString);
			process.destroy();
			exitResultType = ProcessResultType.TIMEOUT;
		} else {
			debugString = EphesoftStringUtil.concatenate(debugString, "The process log is: \n", outputLogs, "\n Error log is:  ",
					errorLogs);
			LOGGER.debug(debugString);
		}

		// Waiting for the process to end
		if (null == exitResultType) {
			exitResultType = ProcessUtils.waitFor(process, waitTime);
		}
		// Checking for the process success
		if (ProcessResultType.SUCCESS == exitResultType) {
			String debugLogString = "The command: \"";
			debugLogString = EphesoftStringUtil.concatenate(debugLogString, Arrays.toString(command), "\" executed successfully.");
			LOGGER.debug(debugLogString);

			// Check for if input multipage file has to be deleted or not
			if (this.deleteImage && (this.imagePath != null) && this.imagePath.isFile()) {
				LOGGER.debug(EphesoftStringUtil.concatenate("Deleting image at ", this.imagePath.getAbsolutePath(),
						" post conversion."));
				this.imagePath.delete();
			}
		} else {

			// Checking for retry
			if (this.isRetry && (retryCount < this.getMaximumRetries())) {

				// Retrying process execution
				this.executeProcess(command, workingDirectory, retryCount + 1, waitTime);
			} else {

				// Setting exception for the failure of command execution
				String errorString = "The command: \"";
				errorString = EphesoftStringUtil.concatenate(errorString, Arrays.toString(command));
				if (workingDirectory != null) {
					errorString = EphesoftStringUtil.concatenate(errorString, " executing in the working directory: \"",
							workingDirectory.getAbsolutePath());
				}
				errorString = EphesoftStringUtil.concatenate(errorString, "\" failed to execute successfully due to: ", exitResultType
						.getDescription());
				LOGGER.error(EphesoftStringUtil.concatenate(errorString, errorLogs));

				// NMI changes - In case of learning don't set exception as doing this will terminate whole learning process.
				if (this.performLearning) {
					LOGGER.error(EphesoftStringUtil.concatenate("Error occured while learning sample files. Command is ", Arrays
							.toString(command)));
				} else {
					this.setDcmaApplicationException(new DCMAApplicationException(errorString));
				}
			}
		}
	}

	/**
	 * Gets maximum number of retries for a failed command defined in the application.properties file
	 * 
	 * @returns maximum number of allowed retries for a failed command
	 */
	private int getMaximumRetries() {
		synchronized (object) {
			if (maximumRetries == null) {
				try {
					final ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties
							.getApplicationConfigProperties();
					final String property = applicationConfigProperties.getProperty(MAXIMUM_NUMBER_OF_RETRIES);
					maximumRetries = Integer.parseInt(property);
				} catch (final Exception exception) {
					LOGGER.info(EphesoftStringUtil.concatenate(
							"Could not fetch maximum retries from the property file. It has been assigned the default value: ",
							maximumRetries));
					maximumRetries = DEFAULT_MAX_RETRIES;
					/*
					 * deliberately ignoring the exception so that the default number of retries can still work for an inappropriate
					 * value in the property file
					 */
				}
			}
		}
		return maximumRetries;
	}

	/**
	 * Gets read_process_error_logs switch value from application.properties file and sets off in case unable to read from file.
	 * 
	 * @returns value defined in application.properties file for read_process_error_logs switch.
	 */
	private String getReadErrorLogsSwitch() {
		if (null == readProcessErrorLogs) {
			synchronized (object) {
				if (null == readProcessErrorLogs) {
					try {
						readProcessErrorLogs = ApplicationConfigProperties.getApplicationConfigProperties().getProperty(
								READ_PROCESS_ERROR_LOGS);
					} catch (final IOException ioException) {
						LOGGER
								.error("Could not fetch read_process_error_logs switch from application.properties file. It has been assigned the default value: OFF");
						readProcessErrorLogs = ICommonConstants.OFF;
					}
				}
			}
		}
		return readProcessErrorLogs;
	}

	/**
	 * Gets error_logs_processing_wait_time switch value from application.properties file and sets zero in case unable to read from
	 * file.
	 * 
	 * @returns value defined in application.properties file for error_logs_processing_wait_time property.
	 */
	private int getErrorLogsProcessingTime() {
		if (null == errorLogsProcessingWaitTime) {
			synchronized (object) {
				if (null == errorLogsProcessingWaitTime) {
					try {
						errorLogsProcessingWaitTime = Integer.parseInt(ApplicationConfigProperties.getApplicationConfigProperties()
								.getProperty(ERROR_LOGS_PROCESSING_WAIT_TIME));
					} catch (final IOException ioException) {
						LOGGER
								.error("Could not fetch error_logs_processing_wait_time from application.properties file. It has been assigned the default value: 0");
						errorLogsProcessingWaitTime = DEFAULT_WAIT_TIME;
					} catch (final NumberFormatException numberFormatException) {
						LOGGER
								.error("Unable to fetch error_logs_processing_wait_time from application.properties file properly. It has been assigned the default value: 0");
						errorLogsProcessingWaitTime = DEFAULT_WAIT_TIME;
					}

				}
			}
		}
		return errorLogsProcessingWaitTime;
	}
}
