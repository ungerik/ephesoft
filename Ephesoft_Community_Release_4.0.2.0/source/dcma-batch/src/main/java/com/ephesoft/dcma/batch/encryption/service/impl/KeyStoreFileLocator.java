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

package com.ephesoft.dcma.batch.encryption.service.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.encryption.service.KeyFileLocatorService;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.encryption.constant.CryptographyConstant;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.exception.KeyNotFoundException;

@Service
public class KeyStoreFileLocator implements KeyFileLocatorService {

	/**
	 * Service that access the directory schema of the application to access the key store files.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	@Override
	public File getBatchClassKeyFile(final String batchClassIdentifier) throws KeyNotFoundException {
		String batchClassFolderLocation = getBatchClassKeyDirectory(batchClassIdentifier);
		return getKeyStoreFile(batchClassFolderLocation);
	}

	@Override
	public String getBatchClassKeyDirectory(final String batchClassIdentifier) {
		String sharedFolderLocation = batchSchemaService.getBaseFolderLocation();
		String batchClassFolderLocation = EphesoftStringUtil.concatenate(sharedFolderLocation, File.separator, batchClassIdentifier);
		return batchClassFolderLocation;
	}

	@Override
	public File getApplicationKeyFile() throws KeyNotFoundException {
		String path = getApplicationKeyDirectory();
		return getKeyStoreFile(path);
	}

	@Override
	public String getApplicationKeyDirectory() {
		return batchSchemaService.getBaseFolderLocation();
	}

	@Override
	public File getBatchInstanceKeyFile(final String batchInstanceIdentifier) throws KeyNotFoundException {
		String batchClassFolder = getBatchInstanceKeyDirectory(batchInstanceIdentifier);
		return getKeyStoreFile(batchClassFolder);
	}

	@Override
	public String getBatchInstanceKeyDirectory(final String batchInstanceIdentifier) {
		String systemFolderLocation = batchSchemaService.getLocalFolderLocation();
		String batchInstanceFolder = EphesoftStringUtil.concatenate(systemFolderLocation, File.separator, batchInstanceIdentifier);
		return batchInstanceFolder;
	}

	@Override
	public String getLuceneKeyDirectory(final String batchClassIdentifier) {
		String luceneIndexDirectory = batchSchemaService.getSearchClassSamplePath(batchClassIdentifier, false);
		return luceneIndexDirectory;
	}

	@Override
	public File getLuceneKeyFile(final String batchClassIdentifier) throws KeyNotFoundException {
		String luceneFolderLocation = getLuceneKeyDirectory(batchClassIdentifier);
		return getKeyStoreFile(luceneFolderLocation);
	}

	@Override
	public File getFuzzyKeyFile(final String batchClassIdentifier) throws KeyNotFoundException {
		String fuzzyDBFolderLocation = getFuzzyKeyDirectory(batchClassIdentifier);
		return getKeyStoreFile(fuzzyDBFolderLocation);
	}

	@Override
	public String getFuzzyKeyDirectory(final String batchClassIdentifier) {
		String fuzzyDBFolderLocation = batchSchemaService.getFuzzyDBIndexFolder(batchClassIdentifier, false);
		return fuzzyDBFolderLocation;
	}

	/**
	 * Validates the key store file. If it doesnot exist then raises an exception.
	 * 
	 * @param keyStoreFile {@link File} Key store file to be searched, which will be responsible for encryption.
	 * @throws KeyNotFoundException If the keyStoreFile is NULL or it doesn't exist.
	 */
	public void validateKeyStore(File keyStoreFile) throws KeyNotFoundException {
		if (keyStoreFile == null || !keyStoreFile.exists()) {
			throw new KeyNotFoundException("Key Store file doesnot exist.");
		}
	}

	/**
	 * Gets the key store file used for encryption.
	 * 
	 * @param directoryLocation {@link String} directory path in which the keystore file exist.
	 * @return {@link File} Soft object to the key store file.
	 * @throws KeyNotFoundException if the keyStore file doesn't exist at the specified path.
	 */
	public File getKeyStoreFile(String directoryLocation) throws KeyNotFoundException {
		File keyStoreFile = null;
		String keyStoreFilePath = EphesoftStringUtil.concatenate(directoryLocation, File.separator,
				CryptographyConstant.KEYSTORE_FILE_NAME);
		keyStoreFile = new File(keyStoreFilePath);
		validateKeyStore(keyStoreFile);
		return keyStoreFile;
	}

	@Override
	public File getTestKVKeyFile(String batchClassIdentifier) throws KeyNotFoundException {
		String testKVFolderPath = getTestKVKeyDirectory(batchClassIdentifier);
		return getKeyStoreFile(testKVFolderPath);
	}

	@Override
	public String getTestKVKeyDirectory(String batchClassIdentifier) {
		String testKVFolder = null;
		if (!EphesoftStringUtil.isNullOrEmpty(batchClassIdentifier)) {
			testKVFolder = batchSchemaService.getTestKVExtractionFolderPath(new BatchClassID(batchClassIdentifier), false);
		}
		return testKVFolder;
	}

	@Override
	public File getTestClassificationKeyFile(String batchClassIdentifier) throws KeyNotFoundException {
		String testClassificationFolderPath = getTestClassificationKeyDirectory(batchClassIdentifier);
		return getKeyStoreFile(testClassificationFolderPath);
	}

	@Override
	public String getTestClassificationKeyDirectory(String batchClassIdentifier) {
		String testClassificationFolderPath = EphesoftStringUtil.concatenate(batchSchemaService.getBaseFolderLocation(),
				File.separator, batchClassIdentifier, File.separator, BatchConstants.TEST_CONTENT_CLASSIFICATION_FOLDER_NAME);
		return testClassificationFolderPath;
	}

	@Override
	public File getTestTableKeyFile(String batchClassIdentifier) throws KeyNotFoundException {
		String testTableKeyDirectory = getTestTableKeyDirectory(batchClassIdentifier);
		return getKeyStoreFile(testTableKeyDirectory);
	}

	@Override
	public String getTestTableKeyDirectory(String batchClassIdentifier) {
		String testTableKeyStoreFilePath = null;
		if (!EphesoftStringUtil.isNullOrEmpty(batchClassIdentifier)) {
			testTableKeyStoreFilePath = batchSchemaService.getTestTableFolderPath(new BatchClassID(batchClassIdentifier), false);
		}
		return testTableKeyStoreFilePath;
	}

	@Override
	public File getTestAdvanceKVKeyFile(String batchClassIdentifier) throws KeyNotFoundException {
		String testAdvanceKVDirectory = getTestAdvanceKVDirectory(batchClassIdentifier);
		return getKeyStoreFile(testAdvanceKVDirectory);
	}

	@Override
	public String getTestAdvanceKVDirectory(String batchClassIdentifier) {
		String testAdvanceKVKeyDirectory = null;
		if(!EphesoftStringUtil.isNullOrEmpty(batchClassIdentifier)) {
			testAdvanceKVKeyDirectory = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassIdentifier, false); 
		}
		return testAdvanceKVKeyDirectory;
	}
}
