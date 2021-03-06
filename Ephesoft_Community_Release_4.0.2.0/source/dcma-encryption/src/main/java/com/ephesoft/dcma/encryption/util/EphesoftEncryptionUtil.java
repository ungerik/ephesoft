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

package com.ephesoft.dcma.encryption.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.encryption.core.EncryptorDecryptor;
import com.ephesoft.dcma.util.exception.CryptographyException;

public class EphesoftEncryptionUtil {

	private static String ephesoftPasswordSuffix = null;

	/**
	 * Logger for logging the messages.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EphesoftEncryptionUtil.class);

	/**
	 * Returns the hidden password string for UI display purpose by replacing all the characters in the password string with
	 * <code>*</code>.
	 * 
	 * @param password {@link String} original password string.
	 * @return {@link String} The hidden password string. Returns <code>null</code> if the input password is <code>null</code>.
	 */
	public static String getEncryptedPasswordString(final String password) {
		String encryptedPasswordString = password;
		if (password != null && !isPasswordStringEncrypted(password)) {
			try {
				StringBuilder encrytedPasswordBuilder = new StringBuilder();
				encrytedPasswordBuilder.append(EncryptorDecryptor.getEncryptorDecryptor().encryptString(password));
				encrytedPasswordBuilder.append(ephesoftPasswordSuffix);
				encryptedPasswordString = encrytedPasswordBuilder.toString();
			} catch (CryptographyException e) {
				LOGGER.error("Exception Encrypting the password string. " + e.getMessage(), e);
			}
		}
		return encryptedPasswordString;
	}

	/**
	 * Returns the hidden password string for UI display purpose by replacing all the characters in the password string with
	 * <code>*</code>.
	 * 
	 * @param password {@link String} original password string.
	 * @return {@link String} The hidden password string. Returns <code>null</code> if the input password is <code>null</code>.
	 */
	public static String getDecryptedPasswordString(final String password) {
		String decryptedPasswordString = password;
		if (password != null && isPasswordStringEncrypted(password)) {
			try {
				int passwordSuffixIndex = password.lastIndexOf(ephesoftPasswordSuffix);
				String passwordWithoutSuffix = password.substring(0, passwordSuffixIndex);
				decryptedPasswordString = EncryptorDecryptor.getEncryptorDecryptor().decryptString(passwordWithoutSuffix);
			} catch (CryptographyException e) {
				LOGGER.error("Exception Encrypting the password string. " + e.getMessage(), e);
			}
		}
		return decryptedPasswordString;
	}

	/**
	 * Returns the hidden password string for UI display purpose by replacing all the characters in the password string with
	 * <code>*</code>.
	 * 
	 * @param password {@link String} original password string.
	 * @return {@link String} The hidden password string. Returns <code>null</code> if the input password is <code>null</code>.
	 */
	public static boolean isPasswordStringEncrypted(final String password) {
		boolean isPasswordEncrypted = false;
		if (password != null && password.endsWith(ephesoftPasswordSuffix)) {
			isPasswordEncrypted = true;
		}
		return isPasswordEncrypted;
	}

	/**
	 * @return the ephesoftPasswordSuffix
	 */
	public static String getEphesoftPasswordSuffix() {
		return ephesoftPasswordSuffix;
	}

	/**
	 * @param ephesoftPasswordSuffix the ephesoftPasswordSuffix to set
	 */
	public void setEphesoftPasswordSuffix(String ephesoftPasswordSuffix) {
		EphesoftEncryptionUtil.ephesoftPasswordSuffix = ephesoftPasswordSuffix;
	}

}
