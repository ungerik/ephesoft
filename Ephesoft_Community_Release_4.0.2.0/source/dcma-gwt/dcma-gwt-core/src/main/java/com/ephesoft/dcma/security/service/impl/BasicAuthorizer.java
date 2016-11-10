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

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.ephesoft.dcma.gwt.core.shared.constants.CoreCommonConstants;
import com.ephesoft.dcma.util.RequestUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
* Provides the implementation for the <code>AuthorizationService</code>.
* 
* <p> It implements the basic authorisation provided by Ephesoft.
* 
* @author  Ephesoft
* 
* <b>created on</b>  18-Nov-2013 <br/>
* @version 1.0
* $LastChangedDate:$ <br/>
* $LastChangedRevision:$ <br/>
*/
public class BasicAuthorizer extends AbstractEphesoftAuthorizer {

	/**
	 * Logger for <code>BasicAuthorizer</code> class.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(BasicAuthorizer.class);

	@Override
	public Set<String> getUserRoles(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Fetching all user roles for logged in user.");
		Set<String> userRolesSet = null;
		if (null != httpServletRequest) {
			final Set<String> allGroups = getAllGroups(httpServletRequest);
			if (null != allGroups && !allGroups.isEmpty()) {
				userRolesSet = new HashSet<String>(allGroups.size());
				if (isSuperAdmin(httpServletRequest)) {
					LOGGER.debug("User is assigned all the group since user is super admin.");
					
					// Explicit adding groups due to TreeSet returned by the user connectivity service 
					for (String group : allGroups) {
						userRolesSet.add(group);
					}
				} else {
					
					// Only groups with which user is assigned are added
					for (String group : allGroups) {
						if (null != group && !group.isEmpty() && httpServletRequest.isUserInRole(group)) {
							userRolesSet.add(group);
						}
					}
				}
			}
		}
		LOGGER.debug("User roles fetched are: ", userRolesSet);
		return userRolesSet;
	}

	@Override
	public boolean isSuperAdmin(final HttpServletRequest httpServletRequest) {
		LOGGER.debug("Checking if the logged in user is super admin.");
		Boolean isSuperAdmin = RequestUtil.getSessionAttribute(httpServletRequest, CoreCommonConstants.IS_SUPER_ADMIN);
		if (null == isSuperAdmin && null != httpServletRequest) {
			isSuperAdmin = Boolean.FALSE;
			final Set<String> allSuperAdminGroups = getAllSuperAdminGroup(httpServletRequest);
			
			// Traverse through all the super-admin group and check if current user roles matches
			for (String superAdminGroup : allSuperAdminGroups) {
				if (httpServletRequest.isUserInRole(superAdminGroup)) {
					isSuperAdmin = Boolean.TRUE;
					break;
				}
			}
			RequestUtil.setSessionAttribute(httpServletRequest, CoreCommonConstants.IS_SUPER_ADMIN, isSuperAdmin);
		}
		LOGGER.debug("Status of logged in user as super admin user is: ", isSuperAdmin);
		return isSuperAdmin.booleanValue();
	}

}
