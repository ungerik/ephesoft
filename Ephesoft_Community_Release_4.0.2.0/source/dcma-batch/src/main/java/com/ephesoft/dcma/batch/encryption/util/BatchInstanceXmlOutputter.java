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

import java.io.IOException;
import java.io.OutputStream;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import com.ephesoft.dcma.batch.encryption.service.EncryptionKeyService;
import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;

/**
 * XML out-putter for the Batch instance. It encrypts the Batches on the basis of the encryption algorithm before writing the DOM
 * object to the XML file.
 * <p>
 * It also ensures that the appropriate action should be taken on the basis of the BatchInstance encryption algorithm provided i.e If
 * the instance is not encrypted then no action should be taken for the DOM element.
 * </p>
 * 
 * @author Ephesoft
 * @version 1.0.
 */
public class BatchInstanceXmlOutputter extends XMLOutputter {

	/**
	 * {@link String} Identifier for the Batch Instance which uniquely identifies the Batch instance.
	 */
	private String batchInstanceID;

	/**
	 * Service that provides access to the encryption keys used through out the application i.e instance key, class key fuzzy key etc.
	 */
	private EncryptionKeyService encryptionKeyService;

	public BatchInstanceXmlOutputter(String batchInstanceID) {
		super();
		this.batchInstanceID = batchInstanceID;
		encryptionKeyService = EphesoftContext.get(EncryptionKeyService.class);
	}

	/**
	 * Encrypts the data based on the Batch instance encryption Algorithm and the key used for encryption through out the batch
	 * processing.
	 */
	@Override
	public void output(Document documentToMarshal, OutputStream outputStream) throws IOException {
		if (documentToMarshal != null && outputStream != null) {
			try {
				PrivateKeyEncryptionAlgorithm encryptionAlgorithm = encryptionKeyService
						.getBatchInstanceEncryptionAlgorithm(batchInstanceID);
				byte[] encryptionKey = encryptionKeyService.getBatchInstanceKey(batchInstanceID);
				DOMEncryptionUtil.encryptDocument(documentToMarshal, encryptionAlgorithm, encryptionKey);
				super.output(documentToMarshal, outputStream);
			} catch (final Exception exception) {
				throw new RuntimeException("Batch Instance parameters could not be encrypted/decrypted. ", exception);
			}
		}
	}
}
