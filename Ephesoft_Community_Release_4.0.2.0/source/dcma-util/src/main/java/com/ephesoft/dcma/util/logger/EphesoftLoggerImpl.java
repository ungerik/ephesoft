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

import org.apache.log4j.Logger;

import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * The <code>EphesoftLoggerImpl</code> is a utility class producing Loggers for various logging APIs for various logging levels.
 * 
 * @author Ephesoft
 * @version 3.0
 * @see org.slf4j.LoggerFactory
 * 
 */
public class EphesoftLoggerImpl implements EphesoftLogger {

	/**
	 * logger {@link Logger}.
	 */
	private final Logger logger;

	/**
	 * An instance of EphesoftLoggerImpl.
	 * 
	 * @param logger The logger instance to be used for logging.
	 */
	public EphesoftLoggerImpl(final Logger logger) {
		this.logger = logger;
	}

	/**
	 * Log a message at the ERROR level.
	 * 
	 * @param msgParams {@link Object} the message string to be appended before logging.
	 */
	@Override
	public void error(final Object... msgParams) {
		final String msgStr = EphesoftStringUtil.concatenate(msgParams);
		this.logger.error(msgStr);
	}

	/**
	 * Log an exception (throwable) at the ERROR level with an accompanying message.
	 * 
	 * @param msgParams {@link Object} the message accompanying the exception.
	 * @param throwable {@link Throwable} the exception (throwable) to log
	 */
	@Override
	public void error(final Throwable throwable, final Object... msgParams) {
		final String msgStr = EphesoftStringUtil.concatenate(msgParams);
		this.logger.error(msgStr, throwable);
	}

	/**
	 * An API to log an array of message parameters at ERROR level for a specific InstanceType.
	 * 
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	@Override
	public void error(final InstanceType instanceType, final String identifier, final Object... msgParams) {
		if (identifier == null || instanceType == null) {
			logger.error(EphesoftStringUtil.concatenate(msgParams));
		} else {
			final LogFileInstance batchInstanceLog = new LogFileInstance(instanceType.getName(), identifier,
					EphesoftStringUtil.concatenate(msgParams));
			logger.error(batchInstanceLog);
		}
	}

	/**
	 * An API to log an array of message parameters at ERROR level for a specific InstanceType.
	 * 
	 * @param throwable {@link Throwable} The exception (throwable) to log.
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	@Override
	public void error(final Throwable throwable, final InstanceType instanceType, final String identifier, final Object... msgParams) {
		if (identifier == null || instanceType == null) {
			logger.error(EphesoftStringUtil.concatenate(msgParams), throwable);
		} else {
			final LogFileInstance batchInstanceLog = new LogFileInstance(instanceType.getName(), identifier,
					EphesoftStringUtil.concatenate(msgParams));
			logger.error(batchInstanceLog, throwable);
		}
	}

	/**
	 * Log a message at the DEBUG level.
	 * 
	 * @param msgParams {@link Object} the message string to be appended before logging.
	 */
	@Override
	public void debug(final Object... msgParams) {
		if (this.isDebugEnabled()) {
			final String msgStr = EphesoftStringUtil.concatenate(msgParams);
			this.logger.debug(msgStr);
		}
	}

	/**
	 * Log an exception (throwable) at the DEBUG level with an accompanying message.
	 * 
	 * @param msgParams {@link Object} the message accompanying the exception.
	 * @param throwable {@link Throwable} the exception (throwable) to log.
	 */
	@Override
	public void debug(final Throwable throwable, final Object... msgParams) {
		if (this.isDebugEnabled()) {
			final String msgStr = EphesoftStringUtil.concatenate(msgParams);
			this.logger.debug(msgStr, throwable);
		}

	}

	/**
	 * An API to log an array of message parameters at DEBUG level for a specific InstanceType.
	 * 
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	@Override
	public void debug(final InstanceType instanceType, final String identifier, final Object... msgParams) {
		if (this.isDebugEnabled()) {
			if (identifier == null || instanceType == null) {
				logger.error(EphesoftStringUtil.concatenate(msgParams));
			} else {
				final LogFileInstance batchInstanceLog = new LogFileInstance(instanceType.getName(), identifier,
						EphesoftStringUtil.concatenate(msgParams));
				logger.error(batchInstanceLog);
			}
		}
	}

	/**
	 * An API to log an array of message parameters at DEBUG level for a specific InstanceType.
	 * 
	 * @param throwable {@link Throwable} The exception (throwable) to log.
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	@Override
	public void debug(final Throwable throwable, final InstanceType instanceType, final String identifier, final Object... msgParams) {
		if (this.isDebugEnabled()) {
			if (identifier == null || instanceType == null) {
				logger.error(EphesoftStringUtil.concatenate(msgParams), throwable);
			} else {
				final LogFileInstance batchInstanceLog = new LogFileInstance(instanceType.getName(), identifier,
						EphesoftStringUtil.concatenate(msgParams));
				logger.error(batchInstanceLog, throwable);
			}
		}
	}

	/**
	 * Log a message at the TRACE level.
	 * 
	 * @param msgParams {@link Object} the message string to be appended before logging.
	 */
	@Override
	public void trace(final Object... msgParams) {
		if (this.isTraceEnabled()) {
			final String msgStr = EphesoftStringUtil.concatenate(msgParams);
			this.logger.trace(msgStr);
		}
	}

	/**
	 * Log an exception (throwable) at the TRACE level with an accompanying message.
	 * 
	 * @param msgParams {@link Object} the message accompanying the exception.
	 * @param throwable {@link Throwable} the exception (throwable) to log.
	 */
	@Override
	public void trace(final Throwable throwable, final Object... msgParams) {
		if (this.isTraceEnabled()) {
			final String msgStr = EphesoftStringUtil.concatenate(msgParams);
			this.logger.trace(msgStr, throwable);
		}
	}

	/**
	 * An API to log an array of message parameters at TRACE level for a specific InstanceType.
	 * 
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	@Override
	public void trace(final InstanceType instanceType, final String identifier, final Object... msgParams) {
		if (this.isTraceEnabled()) {
			if (identifier == null || instanceType == null) {
				logger.error(EphesoftStringUtil.concatenate(msgParams));
			} else {
				final LogFileInstance batchInstanceLog = new LogFileInstance(instanceType.getName(), identifier,
						EphesoftStringUtil.concatenate(msgParams));
				logger.error(batchInstanceLog);
			}
		}
	}

	/**
	 * An API to log an array of message parameters at TRACE level for a specific InstanceType.
	 * 
	 * @param throwable {@link Throwable} The exception (throwable) to log.
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	@Override
	public void trace(final Throwable throwable, final InstanceType instanceType, final String identifier, final Object... msgParams) {
		if (this.isTraceEnabled()) {
			if (identifier == null || instanceType == null) {
				logger.error(EphesoftStringUtil.concatenate(msgParams), throwable);
			} else {
				final LogFileInstance batchInstanceLog = new LogFileInstance(instanceType.getName(), identifier,
						EphesoftStringUtil.concatenate(msgParams));
				logger.error(batchInstanceLog, throwable);
			}
		}
	}

	/**
	 * Log a message at the INFO level.
	 * 
	 * @param msgParams {@link Object} the message string to be appended before logging.
	 */
	@Override
	public void info(final Object... msgParams) {
		if (this.isInfoEnabled()) {
			final String msgStr = EphesoftStringUtil.concatenate(msgParams);
			this.logger.info(msgStr);
		}
	}

	/**
	 * Log an exception (throwable) at the INFO level with an accompanying message.
	 * 
	 * @param msgParams {@link Object} the message accompanying the exception.
	 * @param throwable {@link Throwable} the exception (throwable) to log.
	 */
	@Override
	public void info(final Throwable throwable, final Object... msgParams) {
		if (this.isInfoEnabled()) {
			final String msgStr = EphesoftStringUtil.concatenate(null, msgParams);
			this.logger.info(msgStr, throwable);
		}
	}

	/**
	 * An API to log an array of message parameters at INFO level for a specific InstanceType.
	 * 
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	@Override
	public void info(final InstanceType instanceType, final String identifier, final Object... msgParams) {
		if (this.isInfoEnabled()) {
			if (identifier == null || instanceType == null) {
				logger.error(EphesoftStringUtil.concatenate(msgParams));
			} else {
				final LogFileInstance batchInstanceLog = new LogFileInstance(instanceType.getName(), identifier,
						EphesoftStringUtil.concatenate(msgParams));
				logger.error(batchInstanceLog);
			}
		}
	}

	/**
	 * An API to log an array of message parameters at INFO level for a specific InstanceType.
	 * 
	 * @param throwable {@link Throwable} The exception (throwable) to log.
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	@Override
	public void info(final Throwable throwable, final InstanceType instanceType, final String identifier, final Object... msgParams) {
		if (this.isInfoEnabled()) {
			if (identifier == null || instanceType == null) {
				logger.error(EphesoftStringUtil.concatenate(msgParams), throwable);
			} else {
				final LogFileInstance batchInstanceLog = new LogFileInstance(instanceType.getName(), identifier,
						EphesoftStringUtil.concatenate(msgParams));
				logger.error(batchInstanceLog, throwable);
			}
		}
	}

	/**
	 * Log a message at the warn level.
	 * 
	 * @param msgParams {@link Object} the message string to be appended before logging.
	 */
	@Override
	public void warn(final Object... msgParams) {
		final String msgStr = EphesoftStringUtil.concatenate(msgParams);
		this.logger.warn(msgStr);
	}

	/**
	 * Log an exception (throwable) at the warn level with an accompanying message.
	 * 
	 * @param msgParams {@link Object} the message accompanying the exception.
	 * @param throwable {@link Throwable} the exception (throwable) to log.
	 */
	@Override
	public void warn(final Throwable throwable, final Object... msgParams) {

		final String msgStr = EphesoftStringUtil.concatenate(msgParams);
		this.logger.warn(msgStr, throwable);

	}

	/**
	 * An API to log an array of message parameters at WARN level for a specific InstanceType.
	 * 
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	@Override
	public void warn(final InstanceType instanceType, final String identifier, final Object... msgParams) {
		if (identifier == null || instanceType == null) {
			logger.error(EphesoftStringUtil.concatenate(msgParams));
		} else {
			final LogFileInstance batchInstanceLog = new LogFileInstance(instanceType.getName(), identifier,
					EphesoftStringUtil.concatenate(msgParams));
			logger.error(batchInstanceLog);
		}

	}

	/**
	 * An API to log an array of message parameters at WARN level for a specific InstanceType.
	 * 
	 * @param throwable {@link Throwable} The exception (throwable) to log.
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	@Override
	public void warn(final Throwable throwable, final InstanceType instanceType, final String identifier, final Object... msgParams) {

		if (identifier == null || instanceType == null) {
			logger.error(EphesoftStringUtil.concatenate(msgParams), throwable);
		} else {
			final LogFileInstance batchInstanceLog = new LogFileInstance(instanceType.getName(), identifier,
					EphesoftStringUtil.concatenate(msgParams));
			logger.error(batchInstanceLog, throwable);
		}

	}

	/**
	 * Is the logger instance enabled for the DEBUG level?
	 * 
	 * @return True if this Logger is enabled for the DEBUG level, false otherwise.
	 * 
	 */
	public boolean isDebugEnabled() {
		return this.logger.isDebugEnabled();
	}

	/**
	 * Is the logger instance enabled for the INFO level?
	 * 
	 * @return True if this Logger is enabled for the INFO level, false otherwise.
	 * 
	 */
	@Override
	public boolean isInfoEnabled() {
		return this.logger.isInfoEnabled();
	}

	/**
	 * Is the logger instance enabled for the TRACE level?
	 * 
	 * @return True if this Logger is enabled for the TRACE level, false otherwise.
	 * 
	 */
	@Override
	public boolean isTraceEnabled() {
		return this.logger.isTraceEnabled();
	}

}
