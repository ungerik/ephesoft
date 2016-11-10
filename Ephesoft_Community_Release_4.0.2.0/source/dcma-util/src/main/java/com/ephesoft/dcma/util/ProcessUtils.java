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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.util.exception.ProcessTimeOutException;

/**
 * Utility with various APIs for functions that can be performed on a Process.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jun 20, 2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class ProcessUtils {

	/**
	 * Logger for logging the messages.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUtils.class);

	/**
	 * Successful exit value of a process
	 */
	private static final int SUCCESSFUL_EXIT_VALUE = 0;

	/**
	 * Performs the operation of firing the command in the given directory location
	 * 
	 * @param {@link String[]} command to execute
	 * @param {@link File} workingDirectory of command
	 * @return reference of executing Process or null
	 */
	public static Process executeCommand(final String[] command, final File workingDirectory) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command, null, workingDirectory);
		} catch (final Exception ioException) {
			final StringBuilder stringBuilder = new StringBuilder("Error occured while executing the command: ").append(
					Arrays.toString(command)).append(" in the working directory: ").append(workingDirectory);
			LOGGER.error(stringBuilder.toString(), ioException);
		}
		return process;
	}

	/**
	 * Waits for the completion of the process for time period mentioned in waitTime.
	 * 
	 * @param {@link Process} underlying process
	 * @param waitTime time period for which application has to wait for completion of process
	 * @return {@link ProcessResultType} result of executed process
	 */
	public static ProcessResultType waitFor(final Process process, final int waitTime) {
		ProcessResultType exitResultType = ProcessResultType.UNKNOWN;
		final FutureTask<ProcessResultType> futureTask = new FutureTask<ProcessResultType>(new Callable<ProcessResultType>() {

			@Override
			public ProcessResultType call() throws Exception {
				ProcessResultType exitResultType = ProcessResultType.UNKNOWN;
				if (process != null) {
					final int exitValue = process.waitFor();
					exitResultType = (SUCCESSFUL_EXIT_VALUE == exitValue) ? ProcessResultType.SUCCESS
							: ProcessResultType.NON_ZERO_EXIT_FAILURE;
				}
				return exitResultType;
			}
		});
		try {
			futureTask.run();
			exitResultType = futureTask.get(waitTime, TimeUnit.MINUTES);
			process.destroy();
		} catch (final InterruptedException e) {
			process.destroy();
			exitResultType = ProcessResultType.NON_ZERO_EXIT_FAILURE;
		} catch (final ExecutionException e) {
			process.destroy();
			exitResultType = ProcessResultType.NON_ZERO_EXIT_FAILURE;
		} catch (final TimeoutException e) {
			process.destroy();
			exitResultType = ProcessResultType.TIMEOUT;
		}
		return exitResultType;
	}

	/**
	 * Returns the information produced during the execution of the process
	 * 
	 * @param process the underlying {@link Process}
	 * @param waitTime maximum time defined for process to execute in minutes
	 * @return The process log {@link String}
	 * @throws ProcessTimeOutException if process is unable to complete in waitTime duration
	 */
	public static String getProcessLog(final Process process, final int waitTime) throws ProcessTimeOutException {
		LOGGER.debug(EphesoftStringUtil.concatenate("Executing getProcessLog API with wait time value ", waitTime));
		final StringBuilder debugStringBuilder = new StringBuilder();
		boolean isDebug = LOGGER.isDebugEnabled();
		if ((null != process) && (waitTime > IUtilCommonConstants.ZERO)) {
			BufferedReader input = null;
			InputStreamReader inputStreamReader = null;
			try {
				inputStreamReader = new InputStreamReader(process.getInputStream());
				input = new BufferedReader(inputStreamReader);
				final long startTime = System.currentTimeMillis();
				boolean timeOutOccured = false;
				do {
					final String readLine = input.readLine();
					if (null != readLine) {
						if (isDebug) {
							LOGGER.debug("Processing input buffer of process.");
							debugStringBuilder.append(readLine);
						}
					} else {
						LOGGER.debug("No data is available in input buffer.");
						break;
					}

					// Break loop in case timeout occurs.
					if ((System.currentTimeMillis() - startTime) > (waitTime * IUtilCommonConstants.SECONDS_PER_MINUTE * IUtilCommonConstants.MILLISECOND_PER_SECOND)) {
						LOGGER.info("Timeout occured while reading input stream. So exiting loop and setting timeOutOccured true.");
						timeOutOccured = true;
						break;
					}
				} while (true);

				// throw ProcessTimeOutException if process is unable to complete in waitTime duration
				if (timeOutOccured) {
					throw new ProcessTimeOutException(
							"Process is unable to complete in maximum defined wait time so going to retry process after destroying it.");
				}
			} catch (final IOException ioException) {
				LOGGER.warn("Problem in getting process input log.", ioException);
			} finally {
				FileUtils.closeStream(input);
				FileUtils.closeStream(inputStreamReader);
			}
		} else {
			debugStringBuilder.append(EphesoftStringUtil.concatenate(
					"The process for which information is being fetched is null or invalid wait time is specified. Wait time is : ",
					waitTime));
		}
		LOGGER.debug("Executed getProcessLog API successfully.");
		String processLogs = null;
		if (isDebug) {
			processLogs = debugStringBuilder.toString();
		} else {
			processLogs = "Process executed.";
		}
		return processLogs;
	}

	/**
	 * Returns the error information produced during the execution of the process
	 * 
	 * @param process the underlying {@link Process}
	 * @param waitTime maximum time defined for process to execute in minutes
	 * @return The process error log {@link String}
	 */
	public static String getProcessErrorLog(final Process process, final int waitTime) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Executing getProcessErrorLog API with wait time value ", waitTime));
		final StringBuilder debugStringBuilder = new StringBuilder();
		if ((null != process) && (waitTime > IUtilCommonConstants.ZERO)) {
			BufferedReader input = null;
			InputStreamReader errorStreamReader = null;
			try {
				errorStreamReader = new InputStreamReader(process.getErrorStream());
				input = new BufferedReader(errorStreamReader);
				final long startTime = System.currentTimeMillis();
				String readLine = null;
				boolean timeOutOccurred = false;
				boolean isInputReady = input.ready();
				do {
					// Read buffer only when buffer is ready
					if (isInputReady) {
						LOGGER.debug("Processing error buffer of process.");
						readLine = input.readLine();
						if (null != readLine) {
							debugStringBuilder.append(readLine);
						}
					} else {
						LOGGER.debug("Process error buffer is not ready.");

						// Break loop in case timeout occurs.
						if ((System.currentTimeMillis() - startTime) > waitTime) {
							LOGGER.info("Timeout occured while reading error stream of process. So exiting the loop.");
							timeOutOccurred = true;
						} else {
							isInputReady = input.ready();
						}
					}
				} while ((null != readLine) || !timeOutOccurred);
			} catch (final IOException ioException) {
				LOGGER.warn("Problem in getting process error log.", ioException);
			} finally {
				FileUtils.closeStream(input);
				FileUtils.closeStream(errorStreamReader);
			}
		} else {
			debugStringBuilder
					.append(EphesoftStringUtil
							.concatenate(
									"The process for which error information is being fetched is null or invalid wait time is specified. Wait time is : ",
									waitTime));
		}
		LOGGER.debug("Executed getProcessErrorLog API successfully.");
		return debugStringBuilder.toString();
	}
}
