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

package com.ephesoft.dcma.security.service.impl;

import java.security.Principal;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

import com.ephesoft.dcma.gwt.core.shared.constants.CoreCommonConstants;
import com.ephesoft.dcma.security.service.AuthenticationService;
import com.ephesoft.dcma.util.RequestUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;


/**
* Provides the implementation for the <code>AuthenticationService</code>.
* 
* <p> It implements the basic authentication provided by Ephesoft.
* 
* @author  Ephesoft
* 
* <b>created on</b>  18-Nov-2013 <br/>
* @version 1.0
* $LastChangedDate:$ <br/>
* $LastChangedRevision:$ <br/>
*/
public class BasicAuthenticator implements AuthenticationService {

	/**
	 * Logger for <code>BasicAuthenticator</code> class.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(BasicAuthenticator.class);

	@Override
	public String logout(final HttpServletRequest httpServletRequest) {
		LOGGER.info("Logging out of application.");
		String requestURL = null;
		if (null != httpServletRequest) {

			// In-validating session
			httpServletRequest.getSession().invalidate();
			LOGGER.info("Session is successfully in-validated");
		}
		return requestURL;
	}

	@Override
	public void setUserDetails(final HttpServletRequest httpServletRequest, final FilterConfig filterConfig) {
		LOGGER.debug("Setting logged in user details in session.");
		if (null == httpServletRequest) {
			LOGGER.error("Request is null, user details cannot be set");
		} else {
			final Principal userPrincipal = httpServletRequest.getUserPrincipal();
			String userName = null;
			if (null != userPrincipal && null == userName
					&& !RequestUtil.isSessionAttribute(httpServletRequest, CoreCommonConstants.USERNAME_ATTRIBUTE_KEY)) {
				userName = userPrincipal.getName();
				RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstants.USERNAME_ATTRIBUTE_KEY, userName);
			}
			LOGGER.debug("User-name added to the session is:", userName);
		}
	}
}
