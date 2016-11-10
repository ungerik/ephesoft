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

package com.ephesoft.dcma.util;

import java.io.UnsupportedEncodingException;

import javax.crypto.SecretKey;

import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;
import com.ephesoft.dcma.util.exception.CryptographyException;

/**
 * Responsible for cipher operations on the XML. A utility that can be used to generate private keys, hashing operations ,encryption/
 * decryption of data etc.
 * 
 * 
 * @author Ephesoft
 * @version 1.0
 */
public final class CipherUtil {

	/**
	 * Constructor declared so that the object of the class cannot be instantiated.
	 */
	private CipherUtil() {
		throw new UnsupportedOperationException("CipherUtil object cannot be initialized");
	}

	public static synchronized byte[] getEncryptedData(final byte[] originalData,
			final PrivateKeyEncryptionAlgorithm privateKeyAlgorithm, final byte[] key) throws CryptographyException {
		return originalData;
	}

	public static String getEncryptedData(final String originalData, final PrivateKeyEncryptionAlgorithm privateKeyAlgorithm,
			final byte[] key) throws CryptographyException {
		return originalData;
	}

	
	public static String getEncryptedData(final Number originalData, final PrivateKeyEncryptionAlgorithm privateKeyAlgorithm,
			final byte[] key) throws CryptographyException {
		return null;
	}

	public static SecretKey getSecretKey(final String key, final PrivateKeyEncryptionAlgorithm privateKeyAlgorithm) {
		return null;
	}

	public static SecretKey getSecretKey(final String key, final String algorithmName) {
		return null;
	}

	public static SecretKey getSecretKey(final byte[] key, final String algorithmName) {
		return null;
	}

	public static SecretKey getSecretKey(final String key, final PrivateKeyEncryptionAlgorithm privateKeyAlgorithm,
			final boolean applyDigestion) throws CryptographyException {
		return null;
	}

	public static byte[] getDigest(final byte[] keyBytes, final String digestionAglorithm) throws CryptographyException {
		return keyBytes;
	}


	public static synchronized byte[] decode(final byte[] originalData, final PrivateKeyEncryptionAlgorithm algorithm,
			final byte[] digest) throws CryptographyException {
		return originalData;
	}

	public static String getDecryptedData(final byte[] originalData, final PrivateKeyEncryptionAlgorithm algorithm, final byte[] digest)
			throws CryptographyException, UnsupportedEncodingException {
		return new String(originalData);
	}

	public static byte[] mergeKeys(final byte[] firstKey, final byte[] secondKey,
			final PrivateKeyEncryptionAlgorithm encryptionAlgorithm) throws CryptographyException {
		return firstKey;
	}
}
