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

package com.ephesoft.dcma.da.dao;

import java.util.Set;

import com.ephesoft.dcma.core.dao.Dao;
import com.ephesoft.dcma.da.domain.SecurityUser;

/**
 * Provides data accessibility for security user entity like getting user roles, all user roles, logged in user is super admin or not
 * etc.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public interface SecurityUserDao extends Dao<SecurityUser> {

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
	 * @return {@link Set}<{@link String}> set of all the users.
	 */
	Set<String> getAllUser();

	/**
	 * Checks if the passed user belongs to super-admin role or not.
	 * 
	 * @param userName {@link String} user-name of logged in user.
	 * @return {@link boolean} <code>true/false</code> true if user belongs to super admin role otherwise false.
	 */
	boolean isSuperAdmin(final String userName);

	/**
	 * Finds the user with the given user-name.
	 * 
	 * @param userName {@link String} user-name of logged in user.
	 * @return {@link SecurityUser} if the user exists with the given name otherwise <code>NULL</code>.
	 */
	SecurityUser findSecurityUserByName(final String userName);
}
