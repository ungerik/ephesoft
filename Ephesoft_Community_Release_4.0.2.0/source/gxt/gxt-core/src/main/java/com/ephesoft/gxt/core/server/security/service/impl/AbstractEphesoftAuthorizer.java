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

package com.ephesoft.gxt.core.server.security.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.user.service.UserConnectivityService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.gxt.core.server.security.service.AuthorizationService;
import com.ephesoft.gxt.core.server.util.RequestUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.StringUtil;

/**
 * Provides implementation of <code>AuthorizationService</code> common in case where authorisation is configured with Ephesoft.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public abstract class AbstractEphesoftAuthorizer implements AuthorizationService {

	/**
	 * Logger for <code>AbstractEphesoftAuthorizer</code> class.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(AbstractEphesoftAuthorizer.class);

	/**
	 * Service used to get group and roles information according to within Ephesoft configuration.
	 */
	protected final UserConnectivityService userConnectivityService;

	/**
	 * Instantiates the service required by the Ephesoft Authoriser.
	 */
	protected AbstractEphesoftAuthorizer() {
		userConnectivityService = EphesoftContext.get(UserConnectivityService.class);
	}

	@Override
	public Set<String> getAllSuperAdminGroup(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Finding all super admin groups.");
		Set<String> superAdminSet = RequestUtil.getSessionSetAttribute(httpServletRequest, CoreCommonConstant.ALL_SUPER_ADMIN_GROUPS);
		if (null == superAdminSet) {
			superAdminSet = userConnectivityService.getAllSuperAdminGroups();
			RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstant.ALL_SUPER_ADMIN_GROUPS, superAdminSet);
		}
		LOGGER.debug("Super Admin Groups Set Returned: ", superAdminSet);
		return superAdminSet;
	}

	@Override
	public Set<String> getAllGroups(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Getting all the groups.");
		Set<String> allGroupsSet = RequestUtil.getSessionSetAttribute(httpServletRequest, CoreCommonConstant.ALL_GROUPS);
		if (null == allGroupsSet) {
			allGroupsSet = userConnectivityService.getAllGroups();
			RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstant.ALL_GROUPS, allGroupsSet);
		}
		LOGGER.debug("All group names are: ", allGroupsSet);
		return allGroupsSet;
	}

	@Override
	public Set<String> getAllUser(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Getting all the users.");
		Set<String> allUserSet = RequestUtil.getSessionSetAttribute(httpServletRequest, CoreCommonConstant.ALL_USERS);
		if (null == allUserSet) {
			allUserSet = userConnectivityService.getAllUser();
			RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstant.ALL_USERS, allUserSet);
		}
		LOGGER.debug("All users are: ", allUserSet);
		return allUserSet;
	}

	@Override
	public String getUserName(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Fetching user name from session.");
		return RequestUtil.getSessionAttribute(httpServletRequest, CoreCommonConstant.USERNAME_ATTRIBUTE_KEY);
	}

	@Override
	public String getUserFullName(final HttpServletRequest httpServletRequest) {
		final String commonName = getUserName(httpServletRequest);

		String fullName = RequestUtil.getSessionAttribute(httpServletRequest, CoreCommonConstant.USER_FULLNAME_ATTRIBUTE_KEY);
		if (!StringUtil.isNullOrEmpty(commonName) && StringUtil.isNullOrEmpty(fullName)) {
			fullName = userConnectivityService.getUserFullName(commonName);
			fullName = fullName == null ? commonName : fullName;
			RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstant.USER_FULLNAME_ATTRIBUTE_KEY, fullName);
		}
		return fullName;
	}

	@Override
	public Map<String, String> getFullNameofAllUsers(final Set<String> allUserCommonName) {
		Map<String, String> allUserFullName = null;
		if (allUserCommonName == null) {
			LOGGER.info("Set of Common Names of the users is empty. Returning null...");
		} else {
			allUserFullName = new HashMap<String, String>(allUserCommonName.size());
			for (final String commonName : allUserCommonName) {
				LOGGER.info(EphesoftStringUtil.concatenate("Fetching Full Name for ", commonName));
				final String fullName = userConnectivityService.getUserFullName(commonName);
				if (EphesoftStringUtil.isNullOrEmpty(fullName)) {
					allUserFullName.put(commonName, null);
				} else {
					allUserFullName.put(commonName, fullName);
				}
			}
		}
		return allUserFullName;
	}

}
