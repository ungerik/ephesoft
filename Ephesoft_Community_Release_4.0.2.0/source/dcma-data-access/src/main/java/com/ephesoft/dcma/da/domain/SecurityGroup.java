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

package com.ephesoft.dcma.da.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.constant.DataAccessConstant;

/**
 * Entity class for Group entity used for storing group names along with super admin roles for all kind of users.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Entity
@Table(name = "security_group")
// @SequenceGenerator(name = "ephesoft_sequence", sequenceName = "ephesoft_sequence")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class SecurityGroup extends AbstractChangeableEntity {

	/**
	 * Generated serial version id.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Group name which can be assigned to different users.
	 */
	@Column(name = DataAccessConstant.GROUP_NAME)
	private String groupName;

	/**
	 * Defines whther the group belong to super admin role or not.
	 */
	@Column(name = DataAccessConstant.IS_SUPER_ADMIN)
	private boolean isSuperAdmin;

	/**
	 * Sets the group name.
	 * 
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * Gets the group name.
	 * 
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Sets the super admin role
	 * 
	 * @param isSuperAdmin the isSuperAdmin to set
	 */
	public void setSuperAdmin(boolean isSuperAdmin) {
		this.isSuperAdmin = isSuperAdmin;
	}

	/**
	 * Gets the super admin role
	 * 
	 * @return the isSuperAdmin
	 */
	public boolean isSuperAdmin() {
		return isSuperAdmin;
	}

}
