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
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.batch.dao.BatchPropertiesDao;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.util.EphesoftStringUtil;

@Repository("batchClassPluginPropertiesDao")
public class BatchClassPluginPropertiesDao extends BatchPropertiesDao {

	public BatchPluginPropertyContainer getPluginProperties(String batchClassIdentifier) {
		BatchPluginPropertyContainer container = null;
		File serFile = new File(EphesoftStringUtil.concatenate(batchSchemaService.getBaseFolderLocation(), File.separator,
				batchClassIdentifier, File.separator, batchClassIdentifier, FileType.SER.getExtensionWithDot()));
		if (!serFile.exists()) {
			container = createBatchPropertiesFile(batchClassIdentifier);
			log.info(EphesoftStringUtil.concatenate("Deserializing for: ", batchClassIdentifier));
		} else {
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(serFile);
				BufferedInputStream bufferedInputstream = new BufferedInputStream(fileInputStream);
				container = (BatchPluginPropertyContainer) SerializationUtils.deserialize(bufferedInputstream);
				log.debug("deserializing for " + batchClassIdentifier);
			} catch (Exception e) {
				log.error("Error during de-serializing the properties for Batch instance: " + batchClassIdentifier, e);
			} finally {
				try {
					if (fileInputStream != null) {
						fileInputStream.close();
					}
				} catch (IOException e) {
					log.error("Problem closing stream for file :" + serFile.getName(), e);
				}
			}
		}
		return container;
	}

	public void clearPluginProperties(String batchInstanceIdentifier) {
		throw new UnsupportedOperationException("Operation not supported.");
	}

	@Override
	public Set<String> getKeys() {
		
		//Do nothing
		return null;
	}

}
