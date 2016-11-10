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

/**
 * <code>EphesoftLogger</code> a wrapper of <code>org.apache.log4j.Logger</code> for logging info, errors and other logging level log.
 * statements.
 * 
 * @author Ephesoft
 * @version 3.0
 * @see org.apache.log4j.Logger
 * 
 */
public interface EphesoftLogger {

	/**
	 * Log a message at the ERROR level.
	 * 
	 * @param msgParams {@link Object} the message string to be appended before logging.
	 */
	void error(final Object... msgParams);

	/**
	 * Log a message at the DEBUG level.
	 * 
	 * @param msgParams {@link Object} the message string to be appended before logging.
	 */
	void debug(final Object... msgParams);

	/**
	 * Log a message at the TRACE level.
	 * 
	 * @param msgParams {@link Object} the message string to be appended before logging.
	 */
	void trace(final Object... msgParams);

	/**
	 * Log a message at the INFO level.
	 * 
	 * @param msgParams {@link Object} the message string to be appended before logging.
	 */
	void info(final Object... msgParams);

	/**
	 * Log a message at the warn level.
	 * 
	 * @param msgParams {@link Object} the message string to be appended before logging.
	 */
	void warn(final Object... msgParams);

	/**
	 * Log an exception (throwable) at the ERROR level with an accompanying message.
	 * 
	 * @param throwable {@link Throwable} the exception (throwable) to log
	 * @param msgParams {@link Object} the message accompanying the exception.
	 */
	void error(final Throwable throwable, final Object... msgParams);

	/**
	 * Log an exception (throwable) at the DEBUG level with an accompanying message.
	 * 
	 * @param throwable {@link Throwable} the exception (throwable) to log.
	 * @param msgParams {@link Object} the message accompanying the exception.
	 */
	void debug(final Throwable throwable, final Object... msgParams);

	/**
	 * Log an exception (throwable) at the TRACE level with an accompanying message.
	 * 
	 * @param msgParams {@link Object} the message accompanying the exception.
	 * @param throwable {@link Throwable} the exception (throwable) to log.
	 */
	void trace(final Throwable throwable, final Object... msgParams);

	/**
	 * Log an exception (throwable) at the INFO level with an accompanying message.
	 * 
	 * @param msgParams {@link Object} the message accompanying the exception.
	 * @param throwable {@link Throwable} the exception (throwable) to log.
	 */
	void info(final Throwable throwable, final Object... msgParams);

	/**
	 * Log an exception (throwable) at the warn level with an accompanying message.
	 * 
	 * @param msgParams {@link Object} the message accompanying the exception.
	 * @param throwable {@link Throwable} the exception (throwable) to log.
	 */
	void warn(final Throwable throwable, final Object... msgParams);

	/**
	 * An API to log an array of message parameters at ERROR level for a specific InstaneType.
	 * 
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	void error(final InstanceType instanceType, final String identifier, final Object... msgParams);

	/**
	 * An API to log an array of message parameters at DEBUG level for a specific InstaneType.
	 * 
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	void debug(final InstanceType instanceType, final String identifier, final Object... msgParams);

	/**
	 * An API to log an array of message parameters at TRACE level for a specific InstaneType.
	 * 
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	void trace(final InstanceType instanceType, final String identifier, final Object... msgParams);

	/**
	 * An API to log an array of message parameters at INFO level for a specific InstaneType.
	 * 
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	void info(final InstanceType instanceType, final String identifier, final Object... msgParams);

	/**
	 * An API to log an array of message parameters at WARN level for a specific InstaneType.
	 * 
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	void warn(final InstanceType instanceType, final String identifier, final Object... msgParams);

	/**
	 * An API to log an array of message parameters at ERROR level for a specific InstaneType.
	 * 
	 * @param throwable {@link Throwable} The exception (throwable) to log.
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	void error(final Throwable throwable, final InstanceType instanceType, final String identifier, final Object... msgParams);

	/**
	 * An API to log an array of message parameters at DEBUG level for a specific InstaneType.
	 * 
	 * @param throwable {@link Throwable} The exception (throwable) to log.
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	void debug(final Throwable throwable, final InstanceType instanceType, final String identifier, final Object... msgParams);

	/**
	 * An API to log an array of message parameters at TRACE level for a specific InstaneType.
	 * 
	 * @param throwable {@link Throwable} The exception (throwable) to log.
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	void trace(final Throwable throwable, final InstanceType instanceType, final String identifier, final Object... msgParams);

	/**
	 * An API to log an array of message parameters at INFO level for a specific InstaneType.
	 * 
	 * @param throwable {@link Throwable} The exception (throwable) to log.
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	void info(final Throwable throwable, final InstanceType instanceType, final String identifier, final Object... msgParams);

	/**
	 * An API to log an array of message parameters at WARN level for a specific InstaneType.
	 * 
	 * @param throwable {@link Throwable} The exception (throwable) to log.
	 * @param instanceType The instance type for which logging need to be done like batchInstance.
	 * @param identifier The instance type identifier.
	 * @param msgParams The array of messages to be concatenated and logged.
	 */
	void warn(final Throwable throwable, final InstanceType instanceType, final String identifier, final Object... msgParams);

	/**
	 * An API to check if TRACE level logs are enabled.
	 */

	boolean isTraceEnabled();

	/**
	 * An API to check if INFO level logs are enabled.
	 */
	boolean isInfoEnabled();

	/**
	 * An API to check if DEBUG level logs are enabled.
	 */
	boolean isDebugEnabled();

}
