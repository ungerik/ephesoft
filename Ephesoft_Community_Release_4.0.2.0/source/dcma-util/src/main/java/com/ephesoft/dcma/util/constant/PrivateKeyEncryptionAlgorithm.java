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

package com.ephesoft.dcma.util.constant;

/**
 * Encapsulates various parameters that would be required to perform the encryption using encryption algorithm. These parameters
 * include the
 * <p>
 * <li>
 * Digestion Algorithm for Key digestion. eg SHA,MD5</li>
 * <li>
 * Length of private key.</li>
 * <li>
 * name of the algorithm used to encrypt and decrypt the data.</li>
 * </p>
 * 
 * @author Ephesoft
 * @version 1.0.
 */
/**
 * @author Ephesoft
 * 
 */
public enum PrivateKeyEncryptionAlgorithm {

	AES_128("MD5", 16, "AES", "AES/CBC/PKCS5PADDING"), AES_256("SHA-256", 32, "AES", "AES/CBC/PKCS5PADDING");

	/**
	 * Initializes the private key algorithm with the digest algorithm , private key length and name of the algorithm
	 * 
	 * @param keyDigestAlgorithm {@link String} Name of the digest algorithm to be used for private key hashing.
	 * @param privateKeyLength int Length of Secret key in bytes which will be generated using keyDigestAlgorithm.
	 * @param algorithm {@link String} Name of the algorithm used for encryption and decryption.
	 */
	private PrivateKeyEncryptionAlgorithm(final String keyDigestAlgorithm, final int privateKeyLength, final String algorithm,
			final String paddingParameter) {
		this.keyDigestAlgorithm = keyDigestAlgorithm;
		this.privateKeyLength = privateKeyLength;
		this.algorithm = algorithm;
		this.paddingParameter = paddingParameter;
	}

	/**
	 * Name of the digest algorithm to be used for private key hashing.
	 */
	private String keyDigestAlgorithm;

	/**
	 * Required when padding is to be applied with the encryption algorithm. If no padding is required then it should be set to
	 * Encryption algorithm name.
	 */
	private String paddingParameter;

	/**
	 * Length of Secret key in bytes which will be generated using keyDigestAlgorithm.
	 */
	private int privateKeyLength;

	/**
	 * Name of the algorithm used for encryption and decryption.
	 */
	private String algorithm;

	/**
	 * @return the keyDigestAlgorithm {@link String} Name of the digest algorithm to be used for private key hashing.
	 */
	public String getKeyDigestAlgorithm() {
		return keyDigestAlgorithm;
	}

	/**
	 * @return the privateKeyLength int Length of Secret key in bytes which will be generated using keyDigestAlgorithm.
	 */
	public int getPrivateKeyLength() {
		return privateKeyLength;
	}

	/**
	 * @return the algorithm {@link String} Name of the algorithm used for encryption and decryption
	 */
	public String getAlgorithm() {
		return algorithm;
	}

	/**
	 * @return the paddingParameter {@link String} When padding is to be applied with the encryption algorithm. If no padding is
	 *         required then it is set to Encryption algorithm name.
	 */
	public String getPaddingParameter() {
		return paddingParameter;
	}

}
