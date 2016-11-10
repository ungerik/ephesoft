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

package com.ephesoft.dcma.batch.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.da.dao.BatchClassDynamicPluginConfigDao;
import com.ephesoft.dcma.da.dao.BatchClassPluginConfigDao;
import com.ephesoft.dcma.da.dao.DocumentTypeDao;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.KVPageProcess;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * @author Ephesoft
 * @version 1.0
 */
@Repository("batchPropertiesDao")
public abstract class BatchPropertiesDao implements PluginPropertiesDao {

	public static final String EXTN = "ser";
	protected final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected BatchSchemaService batchSchemaService;

	@Autowired
	protected BatchClassPluginConfigDao batchClassPluginConfigDao;

	@Autowired
	protected DocumentTypeDao documentTypeDao;

	@Autowired
	protected BatchClassDynamicPluginConfigDao batchClassDynamicPluginConfigDao;

	@Autowired
	protected DocumentTypeService documentTypeService;

	/**
	 * Method to create serialised file for Batch class properties.
	 * 
	 * @param batchClassIdentifier batch class identifier corresponding to which serialised file is to be created.
	 * @return Returns the {@link BatchPluginPropertyContainer} instance.
	 */
	public BatchPluginPropertyContainer createBatchPropertiesFile(String batchClassIdentifier) {
		String baseFolderLocation = batchSchemaService.getBaseFolderLocation();
		File serializedFile = new File(EphesoftStringUtil.concatenate(baseFolderLocation, File.separator, batchClassIdentifier,
				File.separator, batchClassIdentifier, BatchConstants.DOT, EXTN));
		BatchPluginPropertyContainer container = null;
		List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPluginConfigDao
				.getAllPluginPropertiesForBatchClass(batchClassIdentifier);

		// to lazily load KVPageProcess objects
		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			for (KVPageProcess eachKvPageProcess : batchClassPluginConfig.getKvPageProcesses()) {
				if (log.isDebugEnabled() && eachKvPageProcess != null) {
					log.debug(eachKvPageProcess.getKeyPattern());
				}
			}
		}

		List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassDynamicPluginConfigDao
				.getAllDynamicPluginPropertiesForBatchClass(batchClassIdentifier);
		container = new BatchPluginPropertyContainer(String.valueOf(batchClassIdentifier));
		container.populate(batchClassPluginConfigs);
		container.populateDynamicPluginConfigs(batchClassDynamicPluginConfigs);
		List<DocumentType> documentTypes = documentTypeService.getDocTypeByBatchClassIdentifier(batchClassIdentifier);
		container.populateDocumentTypes(documentTypes, batchClassIdentifier);
		FileOutputStream fileOutputStream = null;
		
		// Changed to make deletion of only serialised file containing batch class data.
		if (serializedFile.exists()) {
			serializedFile.delete();
		}
		try {
			fileOutputStream = new FileOutputStream(serializedFile);
			SerializationUtils.serialize(container, fileOutputStream);

		} catch (Exception e) {
			log.error("Error during serializing the properties for Batch class: " + batchClassIdentifier, e);
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			} catch (IOException e) {
				log.error("Problem closing stream for file :" + serializedFile.getName(), e);
			}
		}

		return container;
	}

}
