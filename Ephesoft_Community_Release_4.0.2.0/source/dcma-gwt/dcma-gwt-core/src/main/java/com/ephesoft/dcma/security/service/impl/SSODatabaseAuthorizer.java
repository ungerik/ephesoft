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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.da.service.SecurityGroupService;
import com.ephesoft.dcma.da.service.SecurityUserService;
import com.ephesoft.dcma.gwt.core.shared.constants.CoreCommonConstants;
import com.ephesoft.dcma.security.service.AuthorizationService;
import com.ephesoft.dcma.util.RequestUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * Provides the implementation of <code>AuthorizationService</code>.
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
public class SSODatabaseAuthorizer implements AuthorizationService {

	/**
	 * Logger for <code>SSODatabaseAuthorizer</code> class.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(SSODatabaseAuthorizer.class);

	/**
	 * Services used for fetching logged in user details.
	 */
	private final SecurityUserService securityUserService;

	/**
	 * Services used for fetching group and roles details.
	 */
	private final SecurityGroupService securityGroupService;

	/**
	 * Instantiates <code>SSODatabaseAuthorizer</code> and services used by the class.
	 */
	public SSODatabaseAuthorizer() {
		super();
		securityUserService = EphesoftContext.get(SecurityUserService.class);
		securityGroupService = EphesoftContext.get(SecurityGroupService.class);
	}

	@Override
	public Set<String> getAllGroups(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Getting all groups.");
		Set<String> allGroupsSet = RequestUtil.getSessionSetAttribute(httpServletRequest, CoreCommonConstants.ALL_GROUPS);
		if (null == allGroupsSet) {
			allGroupsSet = securityGroupService.getAllGroups();
			RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstants.ALL_GROUPS, allGroupsSet);
		}
		LOGGER.debug("Groups set is: ", allGroupsSet);
		return allGroupsSet;
	}

	@Override
	public Set<String> getAllSuperAdminGroup(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Getting all super admin groups.");
		Set<String> superAdminGroupSet = RequestUtil.getSessionSetAttribute(httpServletRequest,
				CoreCommonConstants.ALL_SUPER_ADMIN_GROUPS);
		if (null == superAdminGroupSet) {
			superAdminGroupSet = securityGroupService.getAllSuperAdminGroups();
			RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstants.ALL_SUPER_ADMIN_GROUPS, superAdminGroupSet);
		}
		LOGGER.debug("Super Admin Groups set is: ", superAdminGroupSet);
		return superAdminGroupSet;
	}

	@Override
	public Set<String> getAllUser(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Getting all users.");
		Set<String> allUserSet = RequestUtil.getSessionSetAttribute(httpServletRequest, CoreCommonConstants.ALL_USERS);
		if (null == allUserSet) {
			allUserSet = securityUserService.getAllUser();
			RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstants.ALL_USERS, allUserSet);
		}
		LOGGER.debug("All user set is: ", allUserSet);
		return allUserSet;
	}

	@Override
	public String getUserName(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Getting user name from the session.");
		return RequestUtil.getSessionAttribute(httpServletRequest, CoreCommonConstants.USERNAME_ATTRIBUTE_KEY);
	}

	@Override
	public Set<String> getUserRoles(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Getting all user's specific roles.");
		Set<String> userGroupSet = RequestUtil.getSessionSetAttribute(httpServletRequest, CoreCommonConstants.SESSION_USER_GROUPS_KEY);
		if (null == userGroupSet) {
			userGroupSet = securityUserService.getUserRoles(getUserName(httpServletRequest));
			RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstants.SESSION_USER_GROUPS_KEY, userGroupSet);
		}
		LOGGER.debug("All user specific roles set is: ", userGroupSet);
		return userGroupSet;
	}

	@Override
	public boolean isSuperAdmin(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Checking is the logged in user is super admin or not.");
		Boolean isSuperAdmin = RequestUtil.getSessionAttribute(httpServletRequest, CoreCommonConstants.IS_SUPER_ADMIN);
		if (null == isSuperAdmin) {
			isSuperAdmin = securityUserService.isSuperAdmin(getUserName(httpServletRequest));
			RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstants.IS_SUPER_ADMIN, isSuperAdmin);
		}
		LOGGER.debug("Status of logged in user super admin: ", isSuperAdmin);
		return isSuperAdmin;
	}

	@Override
	public String getUserFullName(final HttpServletRequest httpServletRequest) {
		return getUserName(httpServletRequest);
	}

	@Override
	public Map<String, String> getFullNameofAllUsers(final Set<String> allUserCommonName) {
		Map<String, String> allUserFullName = null;
		if (allUserCommonName == null) {
			LOGGER.info("Set of Common Names of the users is empty. Returning null...");
		} else {
			allUserFullName = new HashMap<String, String>(allUserCommonName.size());
			for (final String commonName : allUserCommonName) {
				allUserFullName.put(commonName, null);
			}
		}
		return allUserFullName;
	}

	@Override
	public String getUserEmail(HttpServletRequest httpServletRequest) {
		LOGGER.info("Unable to fetch user email address from SSO Database Authorizer.");
		return null;
	}
}
