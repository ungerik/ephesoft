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

package com.ephesoft.dcma.security.service;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * Provides authorisation services required for Ephesoft.
 * 
 * <p>
 * It provides all the services in accordance to the configuration provided in the web.xml through authenticationType parameter.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public interface AuthorizationService {

	/**
	 * Gets all the user roles available in the configured authentication type.
	 * 
	 * <p>
	 * It also saves the fetched user roles in the session for further use.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request attribute used to save the fetched result in session.
	 * @return <code>NULL</code> in case of errors otherwise set of user roles.
	 */
	Set<String> getUserRoles(final HttpServletRequest httpServletRequest);

	/**
	 * Checks if the current logged in user has a super-admin role or not.
	 * 
	 * <p>
	 * It also save the result in session for further use.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request attribute used to save the fetched result in session.
	 * @return <code>false</code> in case of errors otherwise respective result.
	 */
	boolean isSuperAdmin(final HttpServletRequest httpServletRequest);

	/**
	 * Gets all the groups which have super-admin roles assigned.
	 * 
	 * <p>
	 * It also saves the fetched result in the session for further use.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request attribute used to save the fetched result in session.
	 * @return <code>NULL</code> in case of errors otherwise set of groups with super-admin role.
	 */
	Set<String> getAllSuperAdminGroup(final HttpServletRequest httpServletRequest);

	/**
	 * Gets all the groups available in the configured authentication type.
	 * 
	 * <p>
	 * It also saves the fetched result in the session for further use.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request attribute used to save the fetched result in session.
	 * @return <code>NULL</code> in case of errors otherwise set of groups.
	 */
	Set<String> getAllGroups(final HttpServletRequest httpServletRequest);

	/**
	 * Gets all the users available in the configured authentication type.
	 * 
	 * <p>
	 * It also saves the fetched result in the session for further use.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request attribute used to save the fetched result in session.
	 * @return <code>NULL</code> in case of errors otherwise set of users.
	 */
	Set<String> getAllUser(final HttpServletRequest httpServletRequest);

	/**
	 * Gets the user name of the current logged in user.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request attribute used to save the fetched result in session.
	 * @return <code>NULL</code> in case of errors otherwise user name.
	 */
	String getUserName(final HttpServletRequest httpServletRequest);

	/**
	 * Gets the full name of the current logged in user.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request attribute used to save the fetched result in session.
	 * @return {@link String} full name of the current user based on its commonName.
	 */
	String getUserFullName(final HttpServletRequest httpServletRequest);

	/**
	 * Gets the full name of all the users.
	 * 
	 * @param allUserCommonName {@link Set}<{@link String}> Set of all Common Names.
	 * @return {@link Map}<{@link String}, {@link String}> Map of all the user's common name along with their full name.
	 */
	Map<String, String> getFullNameofAllUsers(final Set<String> allUserCommonName);
	
	/**
	 * Gets the Email Address of the user based on its commonName.
	 * 
	 * @param {@link String} commonName of the user.
	 * @return {@link String} Returns the email of the User.
	 */
	String getUserEmail(final HttpServletRequest httpServletRequest);
}
