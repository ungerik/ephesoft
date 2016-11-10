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

package com.ephesoft.dcma.batch.encryption.util;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

import com.ephesoft.dcma.batch.util.BatchEncryptionUtil;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;

/**
 * A utility to encrypt and decrypt the DOM Objects. It provides an external interface to crypt the Document object and provide the
 * same to the external interface.
 * <p>
 * This utility class will help in exporting DOM objects of the encrypted XML files in the decrypted form. This interface wil help the
 * Scripting/Export Plugin's to work without any hurdle
 * </p>
 * 
 * @author Ephesoft
 * @version 1.0.
 * 
 */
public class DOMEncryptionUtil {

	/**
	 * Encrypt's the {@link Document} object recursively on the basis of encryptionAlgorithm as well as on the basis of the encryption
	 * key.
	 * <p>
	 * Encrypt's each and every element under the document. It uses the same scheme as used throughout the XML encrypt
	 * </p>
	 * 
	 * @param document {@link Document} The object to be encrypted.
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm} algorithm on the basis of which the data needs to be encrypted
	 * @param encryptionKey byte[] The key which is responsible to encrypt/decrypt the data.
	 * @throws Exception when the Document could not be encrypted.
	 */
	public static void encryptDocument(final Document document, final PrivateKeyEncryptionAlgorithm encryptionAlgorithm,
			final byte[] encryptionKey) throws Exception {
		if (document != null) {
			final Element rootElement = document.getRootElement();
			encryptElementTextRecursively(rootElement, encryptionAlgorithm, encryptionKey);
		}
	}

	/**
	 * Encrypt's the {@link Element} and the subelement's under the DOM Element. This API recursively Encrypt's the content DOM
	 * element.
	 * 
	 * @param domElement {@link Element} which needs to be encrypted.
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm} encryptionAlgorithm on the basis of which encryption needs to
	 *            be done.
	 * @param encryptionKey key which is responsible to actually encrypt/decrypt the data.
	 * @throws Exception When the Element cannot be encrypted.
	 */
	public static void encryptElementTextRecursively(final Element domElement,
			final PrivateKeyEncryptionAlgorithm encryptionAlgorithm, final byte[] encryptionKey) throws Exception {
		if (domElement != null) {
			final String elementText = domElement.getText();
			if (!EphesoftStringUtil.isNullOrEmpty(elementText)) {
				final String encryptedText = BatchEncryptionUtil.encryptXMLStringContent(elementText, encryptionAlgorithm,
						encryptionKey);
				domElement.setText(encryptedText);
			} else {
				final List<?> childElements = domElement.getChildren();
				if (!CollectionUtil.isEmpty(childElements)) {
					for (final Object childObject : childElements) {
						if (childObject instanceof Element) {
							encryptElementTextRecursively((Element) childObject, encryptionAlgorithm, encryptionKey);
						}
					}
				}
			}
		}
	}

	/**
	 * Decrypt's the {@link Document} object recursively on the basis of encryptionAlgorithm as well as on the basis of the encryption
	 * key.
	 * <p>
	 * Decrypt's each and every element under the document. It uses the same scheme as used throughout the XML encrypt
	 * </p>
	 * 
	 * @param document {@link Document} The object to be encrypted.
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm} algorithm on the basis of which the data needs to be encrypted
	 * @param encryptionKey byte[] The key which is responsible to encrypt/decrypt the data.
	 * @throws Exception when the Document could not be encrypted.
	 */
	public static void decryptDocument(final Document document, final PrivateKeyEncryptionAlgorithm encryptionAlgorithm,
			final byte[] encryptionKey) throws Exception {
		if (document != null) {
			final Element rootElement = document.getRootElement();
			decryptElementTextRecursively(rootElement, encryptionAlgorithm, encryptionKey);
		}
	}

	/**
	 * Decrypt's the {@link Element} and the subelement's under the DOM Element. This API recursively decrypt's the content DOM
	 * element.
	 * 
	 * @param domElement {@link Element} which needs to be decrypted.
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm} encryptionAlgorithm on the basis of which decryption needs to
	 *            be done.
	 * @param encryptionKey key which is responsible to actually encrypt/decrypt the data.
	 * @throws Exception When the Element cannot be encrypted.
	 */
	public static void decryptElementTextRecursively(final Element domElement,
			final PrivateKeyEncryptionAlgorithm encryptionAlgorithm, final byte[] encryptionKey) throws Exception {
		if (domElement != null) {
			final String elementText = domElement.getText();
			if (!EphesoftStringUtil.isNullOrEmpty(elementText)) {
				final String decryptedText = BatchEncryptionUtil.decryptXMLStringContent(elementText, encryptionAlgorithm,
						encryptionKey);
				domElement.setText(decryptedText);
			} else {
				final List<?> childElements = domElement.getChildren();
				if (!CollectionUtil.isEmpty(childElements)) {
					for (final Object childObject : childElements) {
						if (childObject instanceof Element) {
							decryptElementTextRecursively((Element) childObject, encryptionAlgorithm, encryptionKey);
						}
					}
				}
			}
		}
	}
}
