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

package com.ephesoft.dcma.batch.dao.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.batch.dao.BatchPropertiesDao;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;

@Repository("batchInstancePluginPropertiesDao")
public class BatchInstancePluginPropertiesDao extends BatchPropertiesDao {

	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Map maintained for storing Plugin Properties for each Batch instance as an in-memory resource. {@link Map}<{@link String},
	 * {@link BatchPluginPropertyContainer}>
	 */
	private Map<String, BatchPluginPropertyContainer> inMemoryBatchPropertiesContainer;

	/**
	 * @param batchInstanceIdentifier {@link String}
	 * @return {@link BatchPluginPropertyContainer}
	 */
	// @Cacheable(cacheName = "pluginPropertiesCache", keyGenerator =
	// @KeyGenerator(name =
	// "StringCacheKeyGenerator",properties=@Property(name="includeMethod",value="false")))
	public BatchPluginPropertyContainer getPluginProperties(String batchInstanceIdentifier) {
		if (null == this.inMemoryBatchPropertiesContainer) {
			inMemoryBatchPropertiesContainer = new HashMap<String, BatchPluginPropertyContainer>();
		}
		BatchPluginPropertyContainer container = inMemoryBatchPropertiesContainer.get(batchInstanceIdentifier);
		if (null == container) {
			log.info(EphesoftStringUtil.concatenate("In Memory Properties container not found for :", batchInstanceIdentifier));
			String localFolderLocation = batchSchemaService.getLocalFolderLocation();
			boolean isContainerCreated = false;
			String pathToPropertiesFolder = EphesoftStringUtil.concatenate(localFolderLocation, File.separator, "properties");
			File file = new File(pathToPropertiesFolder);
			if (!file.exists()) {
				file.mkdir();
				log.info(EphesoftStringUtil.concatenate(pathToPropertiesFolder, " folder created"));
			}
			File batchInstanceSerializedFile = new File(EphesoftStringUtil.concatenate(pathToPropertiesFolder, File.separator,
					batchInstanceIdentifier, FileType.SER.getExtensionWithDot()));

			if (!batchInstanceSerializedFile.exists()) {
				String batchClassIdentifier = batchInstanceService.getBatchClassIdentifier(batchInstanceIdentifier);
				File serFile = new File(EphesoftStringUtil.concatenate(batchSchemaService.getBaseFolderLocation(), File.separator,
						batchClassIdentifier, File.separator, batchClassIdentifier, FileType.SER.getExtensionWithDot()));
				if (serFile.exists()) {
					copyPropertiesToBatchFolder(batchInstanceIdentifier, pathToPropertiesFolder, serFile);
				} else {
					container = createBatchPropertiesFile(batchClassIdentifier);
					copyPropertiesToBatchFolder(batchInstanceIdentifier, pathToPropertiesFolder, serFile);
					isContainerCreated = true;
					log.info(EphesoftStringUtil.concatenate("Deserializing unoptimised for: ", batchInstanceIdentifier));
				}
			}
			if (!isContainerCreated) {
				BufferedInputStream bufferedInputStream = null;
				try {
					bufferedInputStream = new BufferedInputStream(new FileInputStream(batchInstanceSerializedFile));
					container = (BatchPluginPropertyContainer) SerializationUtils.deserialize(bufferedInputStream);
				} catch (Exception e) {
					log.error("Error during de-serializing the properties for Batch instance: " + batchInstanceIdentifier, e);
				} finally {
					try {
						if (bufferedInputStream != null) {
							bufferedInputStream.close();
						}
					} catch (IOException e) {
						log.error("Problem closing stream for file :" + batchInstanceSerializedFile.getName(), e);
					}
				}
			}
			this.inMemoryBatchPropertiesContainer.put(batchInstanceIdentifier, container);
			log.debug(EphesoftStringUtil.concatenate("Added Plugin Properties Container in In-Memory Map for: ",
					batchInstanceIdentifier));
		}
		return container;
	}

	private void copyPropertiesToBatchFolder(final String batchInstanceIdentifier, final String pathToPropertiesFolder,
			final File serFile) {
		try {
			// If serialised file created successfully, copy it to batch instance folder.
			if (serFile.exists()) {
				FileUtils.copyFile(
						serFile,
						new File(EphesoftStringUtil.concatenate(pathToPropertiesFolder, File.separator, batchInstanceIdentifier,
								FileType.SER.getExtensionWithDot())));
			}
		} catch (final Exception e) {
			log.error("Error copying the serialized file: " + batchInstanceIdentifier, e);
		}
	}

	// @TriggersRemove(cacheName = "pluginPropertiesCache", keyGenerator = @KeyGenerator(name =
	// "StringCacheKeyGenerator",properties=@Property(name="includeMethod",value="false")))
	public void clearPluginProperties(String batchInstanceIdentifier) {
		log.info(EphesoftStringUtil
				.concatenate("Clearing Plugin Properties Container in In-Memory Map for: ", batchInstanceIdentifier));
		if (!(null == this.inMemoryBatchPropertiesContainer)) {
			if (!EphesoftStringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
				inMemoryBatchPropertiesContainer.remove(batchInstanceIdentifier);
				log.debug("Cleared the pluginPropertiesCache for batch Instance: " + batchInstanceIdentifier);
			}
		}
	}

	@Override
	public Set<String> getKeys() {
		Set<String> keySet = null;
		if (null != inMemoryBatchPropertiesContainer) {
			log.debug("Fetching Keys for In-Memory Plugin Properties map");
			keySet = inMemoryBatchPropertiesContainer.keySet();
		}
		return keySet;
	}
}
