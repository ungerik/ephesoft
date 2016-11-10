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

package com.ephesoft.gxt.core.shared.util.logging;

import java.util.MissingResourceException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.StringUtil;

/**
 * Logger which will be responsible for doing the client side logging.
 * 
 * <p>
 * Client Side logging is achieved using the </code> emulation </code>. GWT emulates the {@link Logger}
 * </p>
 * 
 * @author Ephesoft
 * @version 1.0.
 * @see Logger
 */
public final class UILogger extends Logger {

	/**
	 * {@link UILogger} which is a Singleton reference to the LOGGER for UI Logging.
	 */
	private static UILogger LOGGER = null;

	/**
	 * Instantiate the Logger with the name and resourceBundleName.
	 * 
	 * @param name {@link String} name by which logger needs to be implemented.
	 * @param resourceBundleName {@link String} bundle name to which the resource is associated to. If the resource is not associated
	 *            to the given name then {@link MissingResourceException} is raised.
	 */
	protected UILogger(final String name, final String resourceBundleName) {
		super(name, resourceBundleName);
	}

	/**
	 * Returns a reference to the UI-Logger which will be used for logging.
	 * 
	 * @return {@link UILogger} that will be used for logging as a singleton.
	 */
	public synchronized static final UILogger getLogger() {
		if (null == LOGGER) {
			LOGGER = new UILogger(CoreCommonConstant.DEFAULT_UI_LOGGER_NAME, null);
		}
		return LOGGER;
	}

	/**
	 * Log a message at the ERROR level.
	 * 
	 * @param msgParams {@link Object} varArgs the message string to be appended before logging.
	 */
	public void error(final Object... msgParams) {
		this.log(Level.SEVERE, StringUtil.concatenate(msgParams));
	}

	/**
	 * Log a message at the DEBUG level.
	 * 
	 * @param msgParams {@link Object} varArgs the message string to be appended before logging.
	 */
	public void debug(final Object... msgParams) {
		this.log(Level.FINE, StringUtil.concatenate(msgParams));
	}

	/**
	 * Log a message at the INFO level.
	 * 
	 * @param msgParams {@link Object} varArgs the message string to be appended before logging.
	 */
	public void info(final Object... msgParams) {
		this.log(Level.INFO, StringUtil.concatenate(msgParams));
	}

	/**
	 * Log a message at the warn level.
	 * 
	 * @param msgParams {@link Object} varArgs the message string to be appended before logging.
	 */
	public void warn(final Object... msgParams) {
		this.log(Level.WARNING, StringUtil.concatenate(msgParams));
	}

	/**
	 * Log a message at the ERROR level.
	 * 
	 * @param msgParams {@link String} varArgs the message string to be appended before logging.
	 */
	public void error(final String... msgParams) {
		this.log(Level.SEVERE, StringUtil.concatenate(msgParams));
	}

	/**
	 * Log a message at the DEBUG level.
	 * 
	 * @param msgParams {@link String} varArgs the message strings to be appended before logging.
	 */
	public void debug(final String... msgParams) {
		this.log(Level.FINE, StringUtil.concatenate(msgParams));
	}

	/**
	 * Log a message at the INFO level.
	 * 
	 * @param msgParams {@link String} varArgs the message strings to be appended before logging.
	 */
	public void info(final String... msgParams) {
		this.log(Level.INFO, StringUtil.concatenate(msgParams));
	}

	/**
	 * Log a message at the warn level.
	 * 
	 * @param msgParams {@link String} varArgs the message strings to be appended before logging.
	 */
	public void warn(final String... msgParams) {
		this.log(Level.WARNING, StringUtil.concatenate(msgParams));
	}
}
