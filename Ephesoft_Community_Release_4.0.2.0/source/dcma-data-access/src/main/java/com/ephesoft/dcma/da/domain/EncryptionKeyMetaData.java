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
import javax.persistence.Table;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.encryption.util.EphesoftEncryptionUtil;

/**
 * Stores the additional parameters related to the Key's generated through out the application.
 * 
 * <p>
 * This is an singleton class which is because of the uniformity in the application level as well as the other keys generated through
 * out the application key.
 * </p>
 * 
 * @author Ephesoft
 * @version 1.0.
 */
@Entity
@Table(name = "encryption_key_metadata")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public final class EncryptionKeyMetaData extends AbstractChangeableEntity implements Serializable {

	/**
	 * {@link Serializable} version UID of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to the own class which makes it singleton as the meta-data to be used for the application level keys is uniform.
	 */
	private static EncryptionKeyMetaData applicationEncryptionKeyMetadata;

	/**
	 * {@link String} password that protects the Keystore Files.
	 */
	@Column(name = "key_password")
	private String password;

	/**
	 * boolean which indicates that the key is generated or not.
	 */
	@Column(name = "key_generated")
	private boolean keyGenerated;

	/**
	 * @return {@link String} the decrypted password
	 */
	public String getPassword() {
		return EphesoftEncryptionUtil.getDecryptedPasswordString(password);
	}

	/**
	 * Encrypts the password and sets the password for protecting the key.
	 * 
	 * @param password the password to set
	 */
	public void setPassword(final String password) {
		this.password = EphesoftEncryptionUtil.getEncryptedPasswordString(password);
	}

	/**
	 * Returns that is the <code>keyGenerated</code> at a specific level.
	 * 
	 * @return the keyGenerated
	 */
	public boolean isKeyGenerated() {
		return keyGenerated;
	}

	/**
	 * Indicates that the key is already set or not.
	 * 
	 * @param keyGenerated the keyGenerated to set
	 */
	public void setKeyGenerated(final boolean keyGenerated) {
		this.keyGenerated = keyGenerated;
	}

	/**
	 * Reference to the application level keys metadata.
	 * 
	 * @return the applicationEncryptionKeyMetadata
	 */
	public static EncryptionKeyMetaData getApplicationEncryptionKeyMetadata() {
		return applicationEncryptionKeyMetadata;
	}

	/**
	 * @param applicationEncryptionKeyMetadata the applicationEncryptionKeyMetadata to set
	 */
	public static void setApplicationEncryptionKeyMetadata(final EncryptionKeyMetaData applicationEncryptionKeyMetadata) {
		EncryptionKeyMetaData.applicationEncryptionKeyMetadata = applicationEncryptionKeyMetadata;
	}

}
