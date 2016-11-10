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

package com.ephesoft.dcma.dynamic.logging;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.ephesoft.dcma.dynamic.constant.Constants;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

public class Log4jConfigurator implements Log4jConfiguratorMXBean {

	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(Log4jConfigurator.class);

	@Override
	public List<String> getLoggers() {
		LOGGER.info("Get Logger method is called of dynamic logging services");
		List<String> list = new ArrayList<String>();
		for (Enumeration e = LogManager.getCurrentLoggers(); e.hasMoreElements();) {

			Logger log = (Logger) e.nextElement();
			if (log.getLevel() != null) {
				list.add(EphesoftStringUtil.concatenate(log.getName(), Constants.EQUAL_SYMBOL, log.getLevel().toString()));
			}
		}
		return list;
	}

	@Override
	public String getLogLevel(final String logger) {
		LOGGER.info("Get Log level method is called of dynamic logging services");
		String level = Constants.LOGGER_UNAVAILABLE;
		if (!EphesoftStringUtil.isNullOrEmpty(logger)) {
			Logger log = Logger.getLogger(logger);

			if (null != log && null != log.getLevel()) {
				level = log.getLevel().toString();
			}
		}
		return level;
	}

	@Override
	public String setLogLevel(final String logger, final String level) {
		LOGGER.info("Set Log level method is called of dynamic logging services");
		String returnMessage = Constants.EMPTY_STRING;

		if (!EphesoftStringUtil.isNullOrEmpty(logger) && !EphesoftStringUtil.isNullOrEmpty(level)) {
			Logger log = Logger.getLogger(logger);
			if (null != log && null != log.getLevel()) {
				log.setLevel(Level.toLevel(level.toUpperCase(), log.getLevel()));
				returnMessage = Constants.SET_SUCCESS_MESSAGE;
			} else {
				returnMessage = Constants.INVALID_INPUT;
			}
		} else {
			returnMessage = Constants.EMPTY_INPUT_PARAMETERS;
		}
		return returnMessage;
	}

}
