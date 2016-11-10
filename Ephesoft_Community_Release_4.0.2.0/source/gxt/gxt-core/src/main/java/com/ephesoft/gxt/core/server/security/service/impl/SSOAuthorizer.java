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

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.server.util.RequestUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;


/**
* Provides the implementation for <code>SSOAuthorizer</code>.
* 
* <p>It implements SSO authorisation at Ephesoft end.
* 
* @author  Ephesoft
* 
* <b>created on</b>  18-Nov-2013 <br/>
* @version 1.0
* $LastChangedDate:$ <br/>
* $LastChangedRevision:$ <br/>
*/
public class SSOAuthorizer extends AbstractEphesoftAuthorizer {
	
	/**
	 * Logger for <code>SSOAuthorizer</code> class.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(SSOAuthorizer.class);

	@Override
	public Set<String> getUserRoles(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Getting all user roles.");
		Set<String> userGroups = null;
		Set<String> tempUserGroups = null;
		if (null != httpServletRequest) {
			final String userName = getUserName(httpServletRequest);
			userGroups = RequestUtil.getSessionSetAttribute(httpServletRequest, CoreCommonConstant.SESSION_USER_GROUPS_KEY);
			if (null == userGroups) {
				
				// If user is super-admin
				if (isSuperAdmin(httpServletRequest)) {
					tempUserGroups = getAllGroups(httpServletRequest);
					userGroups = new HashSet<String>(tempUserGroups.size());
					
					// Explicitly adding groups due to TreeSet returned by getAllGroups.
					for (String group: tempUserGroups) {
						userGroups.add(group);
					}
				} else {
					userGroups = userConnectivityService.getUserGroups(userName);
				}
				RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstant.SESSION_USER_GROUPS_KEY, userGroups);
			}
		}
		LOGGER.debug("User roles set is: ", userGroups);
		return userGroups;
	}

	@Override
	public boolean isSuperAdmin(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Checking if logged in user is super admin");
		Boolean isSuperAdmin = RequestUtil.getSessionAttribute(httpServletRequest, CoreCommonConstant.IS_SUPER_ADMIN);
		if (null == isSuperAdmin && null != httpServletRequest) {
			isSuperAdmin = Boolean.FALSE;
			final Set<String> allSuperAdminGroups = getAllSuperAdminGroup(httpServletRequest);
			final String userName = getUserName(httpServletRequest);
			final Set<String> userGroups = userConnectivityService.getUserGroups(userName);
			
			// Compare the user groups with all super-admin groups
			for (final String superAdminGroup : allSuperAdminGroups) {
				if (userGroups.contains(superAdminGroup)) {
					isSuperAdmin = Boolean.TRUE;
					break;
				}
			}
			RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstant.IS_SUPER_ADMIN, isSuperAdmin);
		}
		LOGGER.debug("Logged in user super admin status: ", isSuperAdmin);
		return isSuperAdmin;
	}

}
