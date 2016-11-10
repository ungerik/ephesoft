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

package com.ephesoft.gxt.core.server.service.impl.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.gxt.core.shared.constant.CoreErrorConstant;
import com.google.gwt.logging.shared.RemoteLoggingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Remote Service which is responsible for remote logging.
 * 
 * <p>
 * GWT emulates {@link Logger} at the client side. This is an implementation which will emulate all the client side logs with their
 * Ephesoft's Logger representation, which is being used for logging through-out the application. .
 * </p>
 * 
 * @author Ephesoft.
 * @version 1.0.
 */
public class EphesoftRemoteLoggingServiceImpl extends RemoteServiceServlet implements RemoteLoggingService {

	/**
	 * Default Serialized Version UID of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Logger that is responsible for Logging for this class. All the client side logs are logged using the Loggger at the server.
	 */
	private final static EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(EphesoftRemoteLoggingServiceImpl.class);

	/**
	 * 
	 * Method invoked when a client side message is logged and remote Logging is enabled.
	 * 
	 * @param record {@link LogRecord} Record which needs to be logged at client's end.
	 * @return {@link String} Error Message , NULL when no error state is encountered.
	 */
	@Override
	public String logOnServer(final LogRecord record) {
		final Level level = record.getLevel();
		final String message = record.getMessage();
		String errorMessage = null;
		if (Level.SEVERE.equals(level)) {
			LOGGER.error(message);
		} else if (Level.INFO.equals(level)) {
			LOGGER.info(message);
		} else if (Level.WARNING.equals(level)) {
			LOGGER.warn(message);
		} else if (Level.FINE.equals(level)) {
			LOGGER.debug(message);
		} else {
			errorMessage = CoreErrorConstant.INVALID_LOGGING_LEVEL;
		}
		return errorMessage;
	}
}
