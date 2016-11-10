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

package com.ephesoft.dcma.gwt.core.shared;

import com.ephesoft.dcma.core.common.FeatureName;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * DTO for lock status.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 10-Jul-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see com.google.gwt.user.client.rpc.IsSerializable
 */
public class LockStatusDTO implements IsSerializable {

	/**
	 * locked boolean.true value indicates that the particular feature is locked false otherwise.
	 */
	private boolean locked;

	/**
	 * userName {@link String} represents the name of the user which has taken the lock on the table.
	 */
	private String userName;

	/**
	 * featureName {@link FeatureName} represents the Feature name for which lock is being acquired.
	 */
	private FeatureName featureName;

	/**
	 * currentUser {@link String} represents the name of the user which has login into the Ephesoft application.
	 */
	private String currentUser;

	/**
	 * id long.
	 */
	private long id;

	/**
	 * Getter for the currentUser.
	 * 
	 * @return currentUser {@link String} represents the name of the user which has login into the Ephesoft application.
	 */
	public String getCurrentUser() {
		return currentUser;
	}

	/**
	 * Setter for the currentUser.
	 * 
	 * @param currentUser {@link String} represents the name of the user which has login into the Ephesoft application.
	 */
	public void setCurrentUser(final String currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * Getter for the locked.
	 * 
	 * @return locked boolean. true value indicates that the particular feature is being locked false otherwise.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Setter for the locked.
	 * 
	 * @param locked boolean. true value indicates that the particular feature is locked false otherwise.
	 */
	public void setLocked(final boolean locked) {
		this.locked = locked;
	}

	/**
	 * Getter for the user name.
	 * 
	 * @return userName {@link String}.represents the name of the user which has taken the lock on the table.
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Setter for the userName.
	 * 
	 * @param userName {@link String}.represents the name of the user which has taken the lock on the table.
	 */
	public void setUserName(final String userName) {
		this.userName = userName;
	}

	/**
	 * Setter for the feature name.
	 * 
	 * @param featureName {@link FeatureName} represents the Feature name for which lock is being acquired.
	 */
	public void setFeatureName(final FeatureName featureName) {
		this.featureName = featureName;
	}

	/**
	 * Getter for the feature name.
	 * 
	 * @return featureName {@link FeatureName} represents the Feature name for which lock is being acquired.
	 */
	public FeatureName getFeatureName() {
		return featureName;
	}

	/**
	 * Returns true if the feature is locked by another user else returns false.
	 * 
	 * @param lockStatusDTO {@link LockStatusDTO} The instance of LockStatusDTO.
	 * @return isLocked boolean. Returns true if the feature is locked by another user else returns false.
	 */
	public static boolean isFeatureLockedByAnotherUser(final LockStatusDTO lockStatusDTO) {
		boolean isLocked = false;
		if (lockStatusDTO != null && lockStatusDTO.isLocked() && lockStatusDTO.getCurrentUser() != null
				&& lockStatusDTO.getUserName() != null
				&& !lockStatusDTO.getCurrentUser().equalsIgnoreCase(lockStatusDTO.getUserName())) {
			isLocked = true;
		}
		return isLocked;
	}

	/**
	 * Setter for the id.
	 * 
	 * @param id long.
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Getter for the id.
	 * 
	 * @return id.
	 */
	public long getId() {
		return id;
	}
}
