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

package com.ephesoft.dcma.webservice;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ephesoft.dcma.core.common.ServiceType;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * This class will hold Web Service methods that will be called on web scanner login and logout event.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see ServiceType
 */

@Controller
@RequestMapping("/webscannerWindow")
public class WebScannerWebService {

	/**
	 * Initializing logger {@link Logger}.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(WindowCloseWebService.class);

	/**
	 * Map use to for maintaining the current user logged with the current session as key
	 */
	public static Map<HttpSession, String> sessionVsUserMap = new HashMap<HttpSession, String>(5);

	/**
	 * user already exist
	 */
	private int existingUser = 1;

	/**
	 * new user
	 */
	private int nonExistingUser = 0;

	/**
	 * API that maintain the map for all the logged-in users, and checks whether current user is already logged in or not.This is
	 * called at the time webscanner log-in
	 * 
	 * @param resp {@link HttpServletResponse}
	 * @param request {@link HttpServletRequest}
	 */
	@RequestMapping(value = "/WebScannerLogin", method = RequestMethod.POST)
	public void onWindowOpen(final HttpServletResponse resp, final HttpServletRequest request) {
		HttpSession session = request.getSession();
		String userName = request.getRemoteUser();

		// checking whether user is logged in or not , if logged-in, it set response status to 1 , otherwise 0.
		if (!EphesoftStringUtil.isNullOrEmpty(userName)) {
			if (!sessionVsUserMap.values().contains(userName)) {
				synchronized (WebScannerWebService.class) {
					if (!sessionVsUserMap.values().contains(userName)) {
						sessionVsUserMap.put(session, userName);
						resp.setStatus(nonExistingUser);
					}
				}
			} else {
				resp.setStatus(existingUser);
				LOGGER.error("User Already logged in with the name " + userName);
			}

		}
	}

	/**
	 * API for removing the user name at the time of log-out.This is called at the time webscanner log-out
	 * 
	 * @param resp {@link HttpServletResponse}
	 * @param request {@link HttpServletRequest}
	 */
	@RequestMapping(value = "/WebScannerLogout", method = RequestMethod.POST)
	public void onWindowClose(final HttpServletResponse resp, final HttpServletRequest request) {
		String userName = request.getRemoteUser();
		HttpSession session = request.getSession();
		if (!EphesoftStringUtil.isNullOrEmpty(userName)) {
			if (sessionVsUserMap.containsValue(userName)) {
				sessionVsUserMap.remove(session);
			}
		}
	}
}
