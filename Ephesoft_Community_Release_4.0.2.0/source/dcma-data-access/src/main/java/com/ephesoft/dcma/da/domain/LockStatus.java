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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.ephesoft.dcma.core.common.FeatureName;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * The <code>LockStatus</code> is a POJO that defines an entity for lock_status table in Ephesoft DB. This domain object is used for
 * handling the locking capability for different feature of Ephesoft.
 * 
 * @author Ephesoft
 * @version 3.0
 * @see AbstractChangeableEntity
 * 
 */
@Entity
@Table(name = "lock_status")
public class LockStatus extends AbstractChangeableEntity implements Serializable {

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = -1798477740895175153L;

	/**
	 * islocked boolean.true value indicates that the particular feature is locked false otherwise.
	 */
	@Column(name = "is_locked")
	private boolean isLocked;

	/**
	 * userName {@link String} represents the name of the user which has taken the lock on the table.
	 */
	@Column(name = "user_name")
	private String userName;

	/**
	 * featureName {@link FeatureName} represents the Feature name for which lock is being acquired.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "feature_name", unique = true)
	private FeatureName featureName;

	/**
	 * Getter for the locked.
	 * 
	 * @return locked boolean. true value indicates that the particular feature is being locked false otherwise.
	 */
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * Setter for the locked.
	 * 
	 * @param locked boolean. true value indicates that the particular feature is locked false otherwise.
	 */
	public void setLocked(final boolean locked) {
		this.isLocked = locked;
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

	@Override
	public String toString() {
		return EphesoftStringUtil.concatenate("Lock is taken on ", featureName.getName(), " by user :", userName, ".");
	}

	@Override
	public int hashCode() {
		return Long.valueOf(id).hashCode();
	}

	@Override
	public boolean equals(final Object object) {
		boolean isEqual = false;
		final LockStatus lockStatus = (LockStatus) object;
		if (this.id == lockStatus.id || this.featureName.getName().equalsIgnoreCase(lockStatus.featureName.getName())) {
			isEqual = true;
		}
		return isEqual;
	}
}
