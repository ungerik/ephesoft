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

package com.ephesoft.dcma.batch.encryption.service;

import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;
import com.ephesoft.dcma.util.exception.KeyGenerationException;
import com.ephesoft.dcma.util.exception.KeyNotFoundException;

/**
 * Provides the encryption key to be used at different levels through out the application. Encryption key can be used at Application
 * level, Batch class level, Instance Level, Learning Folder Key, Fuzzy Search Key etc.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Apr 10, 2015 <br/>
 * @version 1.0 <br/>
 *          $LastChangedDate: 2015-04-14 11:40:05 +0530 (Tue, 14 Apr 2015) $ <br/>
 *          $LastChangedRevision: 21582 $ <br/>
 */
public interface EncryptionKeyService {

	/**
	 * Gets the application level key. It is configured one per application. This key acts as a signature for the application. This key
	 * should uniquely identify an application should be used to generate Instance Key, Lucene Key, Fuzzy key etc.
	 * 
	 * @return byte[] Application level key i.e. signature of the application.
	 * @throws KeyNotFoundException when the application key is not found.
	 */
	public byte[] getApplicationKey() throws KeyNotFoundException;

	/**
	 * Generates the private key for the whole application. This key will be one key per application. This key should be used to
	 * generate Instance level, fuzzy level, lucene level key.
	 * 
	 * @param key byte[] key to be assigned to the application.
	 * @throws KeyGenerationException when the key could not be generated for the application.
	 */
	public void generateApplicationKey(final byte[] key) throws KeyGenerationException;

	/**
	 * Generates the private key for the batch class. This key will be one key per batch class. This key should be used to generate
	 * Instance level, fuzzy level, lucene level key.
	 * 
	 * @param batchClassIdentifier {@link String} identity of the batch class.
	 * @param key byte[] key to be assigned to the batch class.
	 * @throws KeyGenerationException when the key could not be generated for the batch class.
	 */
	public void generateBatchClassKey(String batchClassIdentifier, byte[] key) throws KeyGenerationException;

	/**
	 * Gets the Batch Class private Key. This key will be used to generate the batch instance level key , fuzzy key, lucene key. etc.
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @return byte[] key of the batch class.
	 * @throws KeyNotFoundException when the batch class could not be found.
	 */
	public byte[] getBatchClassKey(String batchClassIdentifier) throws KeyNotFoundException;

	/**
	 * Generates the private key for the batch instance. This key will be one key per batch batch Instance. This key might internally
	 * use batch class level key, application key etc.
	 * 
	 * @param batchInstanceIdentifier {@link String} identity of the batch instance.
	 * @param batchClassIdentifier {@link String} identity of the batch class.
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm} the encryption algorithm
	 * @throws KeyGenerationException when the key could not be generated for the batch instance.
	 * @throws KeyNotFoundException the key not found exception
	 */
	public void generateBatchInstanceKey(String batchInstanceIdentifier, String batchClassIdentifier,
			PrivateKeyEncryptionAlgorithm encryptionAlgorithm) throws KeyGenerationException, KeyNotFoundException;

	/**
	 * Gets the Batch Instance private Key. This key might use the application level key and the batch class key for generating
	 * instance key .
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @return byte[] key of the batch instance.
	 * @throws KeyNotFoundException when the batch class could not be found.
	 */
	public byte[] getBatchInstanceKey(String batchInstanceKey) throws KeyNotFoundException;

	/**
	 * Generates the Lucene Index Key for the batch Class Identifier. This key might use the application level key and the batch class
	 * key for generating lucene key .
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm}
	 * @throws KeyGenerationException when the lucene key cannot be generated.
	 * @throws KeyNotFoundException the key not found exception
	 */
	public void generateLuceneIndexKey(String batchClassIdentifier, PrivateKeyEncryptionAlgorithm encryptionAlgorithm)
			throws KeyGenerationException, KeyNotFoundException;

	/**
	 * Gets the Lucene Index Key for the batch Class Identifier.
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @return byte[] key used for lucene index files.
	 * @throws KeyNotFoundException when the lucene key cannot be generated.
	 */
	public byte[] getLuceneIndexKey(String batchClassIdentifier) throws KeyNotFoundException;

	/**
	 * Generates the Fuzzy Index Key for the batch class. This key might use the application level key and the batch class key for
	 * generating fuzzy key .
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm} the encryption algorithm
	 * @throws KeyGenerationException when the fuzzy key cannot be generated.
	 * @throws KeyNotFoundException the key not found exception
	 */

	public void generateFuzzyIndexKey(String batchClassIdentifier, PrivateKeyEncryptionAlgorithm encryptionAlgorithm)
			throws KeyGenerationException, KeyNotFoundException;

	/**
	 * Gets the Fuzzy Index Key for the batch Class Identifier.
	 * 
	 * @param batchClassIdentifier {@link String} Identity of the batch class.
	 * @return byte[] Key associated with the fuzzy index.
	 * @throws KeyNotFoundException when the fuzzy index key could not be found.
	 */
	public byte[] getFuzzyIndexKey(String batchClassIdentifier) throws KeyNotFoundException;

	/**
	 * Gets the Batch Class {@link PrivateKeyEncryptionAlgorithm} on the basis of <code>batchClassIdentifier</code>. It is the
	 * algorithm on the basis of which the batch class learn files instances, fuzzy files needs to be encrypted.
	 * 
	 * @param batchClassIdentifier {@link String} identity of the batch class.
	 * @return {@link PrivateKeyEncryptionAlgorithm} associated with the batch class. If batch class is un-encrypted then it should
	 *         return null.
	 */
	public PrivateKeyEncryptionAlgorithm getBatchClassEncryptionAlgorithm(String batchClassIdentifier);

	/**
	 * Gets the Batch Instance {@link PrivateKeyEncryptionAlgorithm} on the basis of <code>batchInstanceIdentifier.</code> It is the
	 * algorithm on the basis of which batch instance needs to be encrypted.
	 * 
	 * @param batchInstanceIdentifier {@link String} identity of the batch instance.
	 * @return {@link PrivateKeyEncryptionAlgorithm} associated with the batch instance.
	 */
	public PrivateKeyEncryptionAlgorithm getBatchInstanceEncryptionAlgorithm(String batchInstanceIdentifier);

	/**
	 * Removes the Batch Class Key for <code>batchClassIdentifier</code>. Returns true if the batch class key is removed, false if
	 * cannot be removed. If no key is configured for the batch class, then returns false.
	 * 
	 * @param batchClassIdentifier {@link String} batch class identifier of the batch class whose key needs to be removed.
	 * @return true if the batch class key was removed else return true.
	 */
	public boolean removeBatchClassKey(final String batchClassIdentifier);

	/**
	 * Generates the Test Table Key for the batch Class Identifier. This key might use the application level key and the batch class
	 * key for generating test table key .
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm} the encryption algorithm
	 * @throws KeyGenerationException when the test table key cannot be generated.
	 * @throws KeyNotFoundException the key not found exception
	 */
	public void generateTestTableKey(String batchClassIdentifier, PrivateKeyEncryptionAlgorithm encryptionAlgorithm)
			throws KeyGenerationException, KeyNotFoundException;

	/**
	 * Gets the Test Table Key for the batch Class Identifier.
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @return byte[] key used for test table index files.
	 * @throws KeyNotFoundException when the test table key cannot be generated.
	 */
	public byte[] getTestTableKey(String batchClassIdentifier) throws KeyNotFoundException;

	/**
	 * Generates the Test KV Key for the batch Class Identifier. This key might use the application level key and the batch class key
	 * for generating test KV key .
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm} the encryption algorithm
	 * @throws KeyGenerationException when the Test KV key cannot be generated.
	 * @throws KeyNotFoundException the key not found exception
	 */
	public void generateTestKVKey(String batchClassIdentifier, PrivateKeyEncryptionAlgorithm encryptionAlgorithm)
			throws KeyGenerationException, KeyNotFoundException;

	/**
	 * Gets the Test KV Key for the batch Class Identifier.
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @return byte[] key used for Test KV files.
	 * @throws KeyNotFoundException when the Test KV key cannot be generated.
	 */
	public byte[] getTestKVKey(String batchClassIdentifier) throws KeyNotFoundException;

	/**
	 * Generates the Test Classification Key for the batch Class Identifier. This key might use the application level key and the batch
	 * class key for generating test classification key .
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm} the encryption algorithm
	 * @throws KeyGenerationException when the test classification key cannot be generated.
	 * @throws KeyNotFoundException the key not found exception
	 */
	public void generateTestClassificationKey(String batchClassIdentifier, PrivateKeyEncryptionAlgorithm encryptionAlgorithm)
			throws KeyGenerationException, KeyNotFoundException;

	/**
	 * Gets the Test Classification Key for the batch Class Identifier.
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @return byte[] key used for test classification index files.
	 * @throws KeyNotFoundException when the test classification key cannot be generated.
	 */
	public byte[] getTestClassificationKey(String batchClassIdentifier) throws KeyNotFoundException;

	/**
	 * Returns whether or not the application key is generated or not.
	 * 
	 * @return boolean true if the application key is generated else returns false.
	 * 
	 */
	public boolean isApplicationKeyGenerated();

	/**
	 * Fetches the private key encryption algorithm for the provided encryption algorithm name.
	 * 
	 * @param encryptionAlgorithmName {@link String} the name of the encryption algorithm.
	 * @return {@link PrivateKeyEncryptionAlgorithm} the encryption algorithm object for provided algorithm name.
	 */
	public PrivateKeyEncryptionAlgorithm getPrivateKeyAlgorithm(String encryptionAlgorithmName);

	/**
	 * Generates the Test Advance KV key for the batch Class Identifier. This key might use the application level key and the batch
	 * class key for generating advance KV test key .
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm}
	 * @throws KeyGenerationException when the test KV key cannot be generated.
	 * @throws KeyNotFoundException the key not found exception
	 */
	public void generateTestAdvanceKVKey(String batchClassIdentifier, PrivateKeyEncryptionAlgorithm encryptionAlgorithm)
			throws KeyGenerationException, KeyNotFoundException;

	/**
	 * Gets the Test Advance KV Key for the batch Class Identifier.
	 * 
	 * @param batchClassIdentifier {@link String} unique identity of the batch class.
	 * @return byte[] key used for test advance KV HOCR files.
	 * @throws KeyNotFoundException when the test classification key cannot be generated.
	 */
	public byte[] getTestAdvanceKVKey(String batchClassIdentifier) throws KeyNotFoundException;

}
