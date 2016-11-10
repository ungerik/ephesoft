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

package com.ephesoft.dcma.da.service;

import java.util.Set;

/**
 * Provides services for accessing and saving data related to security user.
 * 
 * <p>
 * Service provided are:-
 * <ul>
 * <li>Fetch all user roles
 * <li>Fetch all users
 * <li>Whether the passed in user have super admin role
 * <li>Save the user details in database
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version 1.0 $LastChangedDate: 2015-04-14 12:53:03 +0530 (Tue, 14 Apr 2015) $ <br/>
 *          $LastChangedRevision: 21604 $ <br/>
 */
public interface SecurityUserService {

	/**
	 * Gets the set of roles assigned to the passed user name.
	 * 
	 * @param userName {@link String} user name of logged in user.
	 * @return {@link Set}<{@link String}> <code>NULL</code> if the user name passed is NULl or empty otherwise roles assigned to user.
	 */
	Set<String> getUserRoles(final String userName);

	/**
	 * Gets all the users in the Ephesoft application.
	 * 
	 * @return set of all the users. {@link Set}<{@link String}>
	 */
	Set<String> getAllUser();

	/**
	 * Checks if the passed user belongs to super-admin role or not.
	 * 
	 * @param userName {@link String} user-name of logged in user.
	 * @return {@link Boolean} <code>true/false</code> true if user belongs to super admin role otherwise false.
	 */
	boolean isSuperAdmin(final String userName);

	/**
	 * Save and update the user details in the database.
	 * 
	 * @param userName {@link String} user name of the logged in user.
	 * @param groupName {@link String} group name to which the user belongs to.
	 * @param isSuperAdmin {@link Boolean} whether the logged in user is super admin or not.
	 * @return {@link Boolean} <code>true/false</code> true if the transaction is successful otherwise false.
	 */
	boolean saveAndUpdateUser(final String userName, final String groupName, final Boolean isSuperAdmin);
}
