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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.container.WorkflowDetailContainer;
import com.ephesoft.dcma.batch.dao.BatchClassWorkflowDetailDao;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

@Repository
public class BatchClassWorkflowDetailDaoImpl implements BatchClassWorkflowDetailDao {

	private final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(BatchClassWorkflowDetailDaoImpl.class);

	@Autowired
	protected BatchSchemaService batchSchemaService;

	@Override
	public void createBatchClassWorkflow(final WorkflowDetailContainer batchClassWorkflowContainer) {
		String batchClassIdentifier = batchClassWorkflowContainer.getBatchIdentifier();
		String baseFolderLocation = batchSchemaService.getBaseFolderLocation();
		String serilaizedFileName = EphesoftStringUtil.concatenate(baseFolderLocation, File.separator, batchClassIdentifier,
				File.separator, batchClassIdentifier, ICommonConstants.UNDERSCORE, BatchConstants.WORKFLOWS_CONSTANT);
		LOGGER.debug("Serialized file name is: ", serilaizedFileName);
		File serializedFile = new File(EphesoftStringUtil.concatenate(serilaizedFileName, FileType.SER.getExtensionWithDot()));
		FileOutputStream fileOutputStream = null;
		
		// Changed to make deletion of only serialised file containing batch class workflows.
		if (serializedFile.exists()) {
			serializedFile.delete();
		}
		try {
			fileOutputStream = new FileOutputStream(serializedFile);
			BufferedOutputStream bufferedOutputstream = new BufferedOutputStream(fileOutputStream);
			SerializationUtils.serialize(batchClassWorkflowContainer, bufferedOutputstream);
		} catch (FileNotFoundException fileNotFoundException) {
			LOGGER.error(
					fileNotFoundException,
					"Serialized file just created for workflows does not exist. Error during serializing the properties for Batch class: ",
					batchClassIdentifier);
		} catch (Exception exception) {
			LOGGER.error(exception, "Error during serializing the properties for Batch class: ", batchClassIdentifier);
		} finally {
			try {
				if (null != fileOutputStream) {
					fileOutputStream.close();
				}
			} catch (IOException ioException) {
				LOGGER.error(ioException, "Problem closing stream for file: ", serializedFile.getName());
			}
		}
	}
}
