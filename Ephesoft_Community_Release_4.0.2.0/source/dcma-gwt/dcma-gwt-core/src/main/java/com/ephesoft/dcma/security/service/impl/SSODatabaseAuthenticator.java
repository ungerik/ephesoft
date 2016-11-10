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

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.da.service.SecurityUserService;
import com.ephesoft.dcma.gwt.core.shared.constants.CoreCommonConstants;
import com.ephesoft.dcma.security.service.AuthenticationService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.RequestUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * Provides the implementation for <code>AuthenticationService</code>.
 * 
 * <p>
 * It implements SSO authentication and authorisation.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class SSODatabaseAuthenticator implements AuthenticationService {

	/**
	 * Logger for <code>SSODatabaseAuthenticator</code> class.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(SSODatabaseAuthenticator.class);

	/**
	 * Constant for 'default_group' property added in appplication.properties.
	 */
	private static final String DEFAULT_GROUP = "default_group";

	/**
	 * Constant for default group for the logged in user.
	 */
	private static final String DEFAULT_GROUP_ADMIN = "admin";

	/**
	 * Services used for fetching user related details from the database.
	 */
	private final SecurityUserService securityUserService;

	/**
	 * Instantiates <code>SSODatabaseAuthenticator</code> and services used by the class.
	 */
	public SSODatabaseAuthenticator() {
		super();
		securityUserService = EphesoftContext.get(SecurityUserService.class);
	}

	@Override
	public String logout(final HttpServletRequest httpServletRequest) {
		LOGGER.info("Logging out of application.");
		String requestURL = null;
		if (null != httpServletRequest) {

			// In-validating session
			final HttpSession session = httpServletRequest.getSession();
			requestURL = RequestUtil.getSessionAttribute(httpServletRequest, CoreCommonConstants.LOGOUT_URL_ATTRIBUTE_KEY);
			session.invalidate();
			LOGGER.info("Session is successfully in-validated");
		}
		LOGGER.debug("Logging URL on which request will be re-directed", requestURL);
		return requestURL;

	}

	@Override
	public void setUserDetails(final HttpServletRequest httpServletRequest, final FilterConfig filterConfig) {
		LOGGER.debug("Setting logged in user details in session.");
		if (null == httpServletRequest || null == filterConfig) {
			LOGGER.error("Request is null, user details cannot be set");
		} else {

			// Add currently logged in user's name in session
			String userName = null;
			if (!RequestUtil.isSessionAttribute(httpServletRequest, CoreCommonConstants.USERNAME_ATTRIBUTE_KEY)) {
				final String userIdHeaderName = filterConfig.getInitParameter(CoreCommonConstants.REQUEST_USERNAME_HEADER);
				userName = RequestUtil.getAttributeFromHeader(httpServletRequest, userIdHeaderName);
				RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstants.USERNAME_ATTRIBUTE_KEY, userName);
			}

			// Add Logout URL to session
			String logoutUrl = null;
			if (!RequestUtil.isSessionAttribute(httpServletRequest, CoreCommonConstants.LOGOUT_URL_ATTRIBUTE_KEY)) {
				logoutUrl = filterConfig.getInitParameter(CoreCommonConstants.LOGOUT_URL_PARAM_NAME);
				RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstants.LOGOUT_URL_ATTRIBUTE_KEY, logoutUrl);
			}

			// Add Group Name
			String groupName = null;
			if (!RequestUtil.isSessionAttribute(httpServletRequest, CoreCommonConstants.GROUP_NAME_ATTRIBUTE_KEY)) {
				final String groupNameHeader = filterConfig.getInitParameter(CoreCommonConstants.GROUP_NAME_PARAM_NAME);
				groupName = RequestUtil.getAttributeFromHeader(httpServletRequest, groupNameHeader);
				RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstants.GROUP_NAME_ATTRIBUTE_KEY, groupName);
			}

			// Add Super-Admin
			Boolean isSuperAdmin = Boolean.FALSE;
			if (!RequestUtil.isSessionAttribute(httpServletRequest, CoreCommonConstants.SUPER_ADMIN_ATTRIBUTE_KEY)) {
				final String isSuperAdminHeader = filterConfig.getInitParameter(CoreCommonConstants.SUPER_ADMIN_PARAM_NAME);
				final String isSuperAdminString = RequestUtil.getAttributeFromHeader(httpServletRequest, isSuperAdminHeader);
				isSuperAdmin = (EphesoftStringUtil.isNullOrEmpty(isSuperAdminString)) ? null : Boolean.valueOf(isSuperAdminString);
				RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstants.SUPER_ADMIN_ATTRIBUTE_KEY, isSuperAdmin);
			}

			saveUserDetails(userName, groupName, isSuperAdmin);

			LOGGER.debug("User-name and logut URL are added to the session is:", userName, " and logout name: ", logoutUrl);
		}
	}

	/**
	 * Saves the user details in the Ephesoft database.
	 * 
	 * @param userName {@link String} user name of looged in user.
	 * @param groupName {@link String} group name of logged in user.
	 * @param isSuperAdmin {@link Boolean} true if logged in user role is super admin otherwise false.
	 */
	private void saveUserDetails(final String userName, final String groupName, final Boolean isSuperAdmin) {
		String tempGroupName = groupName;
		if (!EphesoftStringUtil.isNullOrEmpty(userName)) {
			if (null == tempGroupName || tempGroupName.isEmpty()) {
				tempGroupName = getDefaultGroup();
			}
			securityUserService.saveAndUpdateUser(userName, tempGroupName, isSuperAdmin);
		}
	}

	/**
	 * Gets the default group name from the configurable application.properties file.
	 * 
	 * <p>
	 * In case of error in fetching the property, default group is returned.
	 * 
	 * @return default group configured.
	 */
	private String getDefaultGroup() {
		String defaultGroup = null;
		try {
			defaultGroup = ApplicationConfigProperties.getApplicationConfigProperties().getProperty(DEFAULT_GROUP);
		} catch (IOException ioException) {
			LOGGER.error(ioException, "Could not retrieve default group from application.properties file.");
			defaultGroup = DEFAULT_GROUP_ADMIN;
		}
		return defaultGroup;
	}

}
