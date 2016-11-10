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

package com.ephesoft.dcma.util.logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.MDC;
import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.springframework.core.io.ClassPathResource;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This customised Log4j appender will separate the log messages based on message which is to be passed in logging event and will write
 * them into both the identifier specific file and default file defined in log4j.xml.
 * 
 * @author Ephesoft
 * @version 3.0
 * @see org.apache.log4j.RollingFileAppender
 * 
 */
public class EphesoftFileAppender extends RollingFileAppender {

	/**
	 * batchProperties {@link Properties}.
	 */
	private static Properties batchProperties;

	/**
	 * enhancedLoggingSwitch {@link String}.
	 */
	private static String enhancedLoggingSwitch;

	/**
	 * The default constructor simply calls its {@link FileAppender#FileAppender parents constructor}.
	 */
	public EphesoftFileAppender() {
		super();
	}

	/**
	 * Instantiate a RollingFileAppender and open the file designated by <code>filename</code>. The opened filename will become the
	 * output destination for this appender.
	 * 
	 * <p>
	 * If the <code>append</code> parameter is true, the file will be appended to. Otherwise, the file designated by
	 * <code>filename</code> will be truncated before being opened.
	 */
	public EphesoftFileAppender(final Layout layout, final String fileName, final boolean append) throws IOException {
		super(layout, fileName, append);
	}

	/**
	 * Instantiate a FileAppender and open the file designated by <code>filename</code>. The opened filename will become the output
	 * destination for this appender.
	 * 
	 * <p>
	 * The file will be appended to.
	 */
	public EphesoftFileAppender(final Layout layout, final String fileName) throws IOException {
		super(layout, fileName);
	}

	/**
	 * If the value of <b>File</b> is not <code>null</code>, then {@link #setFile} is called with the values of <b>File</b> and
	 * <b>Append</b> properties.
	 * 
	 */
	@Override
	public void activateOptions() {
		MDC.put(EphesoftLoggerConstants.ORIGNAL_LOG_FILE, fileName);
		super.activateOptions();
	}

	/**
	 * This method is called by the {@link AppenderSkeleton#doAppend} method.
	 * 
	 * <p>
	 * If the output stream exists and is writable then write a log statement to the output stream. Otherwise, write a single warning
	 * message to <code>System.err</code>.
	 * 
	 * <p>
	 * The format of the output will depend on this appender's layout.
	 */
	@Override
	public void append(final LoggingEvent event) {
		try {
			if (batchProperties == null) {
				batchProperties = loadBatchProperties();
				enhancedLoggingSwitch = batchProperties.getProperty(EphesoftLoggerConstants.BI_LOG_PROPERTY);
			}
			if (EphesoftLoggerConstants.STRING_ON.equalsIgnoreCase(enhancedLoggingSwitch)) {
				String logFileName = (String) MDC.get(EphesoftLoggerConstants.ORIGNAL_LOG_FILE);
				final Object message = event.getMessage();

				if (message instanceof LogFileInstance) {
					final String identifier = ((LogFileInstance) message).getIdentifier();
					final String instanceType = ((LogFileInstance) message).getInstanceType();
					logFileName = getLogFileName(instanceType, identifier);
				}

				// Handling null check for {@code fileName}.
				if (null != fileName && !(fileName.equalsIgnoreCase(logFileName))) {
					setFile(logFileName, fileAppend, bufferedIO, bufferSize);
					super.append(event);
					setFile((String) MDC.get(EphesoftLoggerConstants.ORIGNAL_LOG_FILE), fileAppend, bufferedIO, bufferSize);
				}
			}
		} catch (IOException ioException) {
			errorHandler.error("Error occured:  " + ioException);
		}
		super.append(event);
	}

	/**
	 * Loads the batch.properties file and returns the batch properties object.
	 * 
	 * @return {@link Properties}
	 * @throws {@link IOException}
	 */
	private static Properties loadBatchProperties() throws IOException {
		InputStream propertyInStream = null;
		final Properties batchProperties = new Properties();
		try {
			final String filePath = EphesoftStringUtil.concatenate(EphesoftLoggerConstants.META_INF, File.separator,
					EphesoftLoggerConstants.DCMA_BATCH, File.separator, EphesoftLoggerConstants.DCMA_BATCH,
					EphesoftLoggerConstants.DOT_PROPERTIES);
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			batchProperties.load(propertyInStream);
		} finally {
			IOUtils.closeQuietly(propertyInStream);
		}
		return batchProperties;
	}

	/**
	 * Returns the file name based on the instance type passed.
	 * 
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @return The path of the log file name.
	 */
	private String getLogFileName(final String instanceType, final String identifier) {
		String logFileName = null;
		if (InstanceType.BATCHINSTANCE.getName().equalsIgnoreCase(instanceType)) {
			logFileName = EphesoftStringUtil.concatenate(batchProperties.getProperty(EphesoftLoggerConstants.LOCAL_FOLDER_PATH),
					File.separator, identifier, File.separator, EphesoftLoggerConstants.LOG_FOLDER, File.separator, identifier,
					EphesoftLoggerConstants.LOG_EXTENSION);
		}
		return logFileName;
	}

	/**
	 * Returns ON if enhanced logging switch is ON else returns OFF.
	 * 
	 * @return {@link String} The enhanced logging switch.
	 */
	public static String getEnhancedLoggingSwitch() {
		try {
			if (batchProperties == null) {
				batchProperties = loadBatchProperties();
				enhancedLoggingSwitch = batchProperties.getProperty(EphesoftLoggerConstants.BI_LOG_PROPERTY);
			}
		} catch (IOException e) {
			enhancedLoggingSwitch = EphesoftLoggerConstants.STRING_OFF;
		}
		return enhancedLoggingSwitch;
	}

}
