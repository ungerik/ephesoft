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

import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.container.WorkflowDetailContainer;
import com.ephesoft.dcma.batch.dao.BatchInstanceWorkflowDetailDao;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

@Repository
public class BatchInstanceWorkflowDetailDaoImpl extends BatchClassWorkflowDetailDaoImpl implements BatchInstanceWorkflowDetailDao {

	private final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(BatchInstanceWorkflowDetailDaoImpl.class);

	@Autowired
	protected BatchSchemaService batchSchemaService;

	@Autowired
	protected BatchInstanceService batchInstanceService;

	@Override
	public WorkflowDetailContainer getWorkflowContainer(final String batchInstanceIdentifier) {
		final String localFolderLocation = batchSchemaService.getLocalFolderLocation();
		String pathToPropertiesFolder = getPathToPropertiesFile(localFolderLocation);
		String workflowSerializedFileSuffix = EphesoftStringUtil.concatenate(ICommonConstants.UNDERSCORE,
				BatchConstants.WORKFLOWS_CONSTANT, FileType.SER.getExtensionWithDot());
		String serializedFilePath = EphesoftStringUtil.concatenate(pathToPropertiesFolder, File.separator, batchInstanceIdentifier,
				workflowSerializedFileSuffix);
		File batchInstanceSerializedFile = new File(serializedFilePath);
		WorkflowDetailContainer workflowContainer = null;
		boolean workflowContainerExist = isBatchSerializedfileExisting(batchInstanceIdentifier, workflowSerializedFileSuffix,
				serializedFilePath, batchInstanceSerializedFile);
		if (workflowContainerExist) {
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(batchInstanceSerializedFile);
				BufferedInputStream bufferedInputstream = new BufferedInputStream(fileInputStream);
				workflowContainer = (WorkflowDetailContainer) SerializationUtils.deserialize(bufferedInputstream);
			} catch (Exception exception) {
				LOGGER.error(exception, "Error during de-serializing the workflow properties for Batch instance: ",
						batchInstanceIdentifier);
			} finally {
				try {
					if (null != fileInputStream) {
						fileInputStream.close();
					}
				} catch (IOException ioException) {
					LOGGER.error(ioException, "Problem closing stream for workflows file :", batchInstanceSerializedFile.getName());
				}
			}
		}
		return workflowContainer;
	}

	private String getPathToPropertiesFile(final String localFolderLocation) {
		String pathToPropertiesFolder = EphesoftStringUtil.concatenate(localFolderLocation, File.separator,
				BatchConstants.PROPERTIES_DIRECTORY);
		LOGGER.debug("Property folder path is: ", pathToPropertiesFolder);
		File file = new File(pathToPropertiesFolder);
		if (!file.exists()) {
			file.mkdir();
			LOGGER.info(pathToPropertiesFolder, " folder created");
		}
		return pathToPropertiesFolder;
	}

	private boolean isBatchSerializedfileExisting(final String batchInstanceIdentifier, final String workflowSerializedFileSuffix,
			final String serializedFilePath, final File batchInstanceSerializedFile) {
		boolean workflowContainerExist;
		if (null == batchInstanceSerializedFile) {
			workflowContainerExist = false;
		} else {
			if (batchInstanceSerializedFile.exists()) {
				workflowContainerExist = true;
			} else {
				String batchClassIdentifier = batchInstanceService.getBatchClassIdentifier(batchInstanceIdentifier);
				File serializedBatchClassFile = new File(EphesoftStringUtil.concatenate(batchSchemaService.getBaseFolderLocation(),
						File.separator, batchClassIdentifier, File.separator, batchClassIdentifier, workflowSerializedFileSuffix));
				if (serializedBatchClassFile.exists()) {
					try {
						copyPropertiesToBatchFolder(batchInstanceIdentifier, serializedFilePath, serializedBatchClassFile);
						workflowContainerExist = true;
					} catch (IOException ioException) {
						LOGGER.error("Error copying the serialized file: ", batchInstanceIdentifier);
						workflowContainerExist = false;
					}
				} else {
					LOGGER.error(
							"The batch class workflows serialized file does not exist. Therefore, no batch progress can not be monitored for batch instance: ",
							batchInstanceIdentifier);
					workflowContainerExist = false;
				}
			}
		}
		return workflowContainerExist;
	}

	private void copyPropertiesToBatchFolder(final String batchInstanceIdentifier, final String serializedFilePath,
			final File serilaizedBatchClassFile) throws IOException {

		// If serialised file created successfully, copy it to batch instance folder.
		if (serilaizedBatchClassFile.exists()) {
			FileUtils.copyFile(serilaizedBatchClassFile, new File(serializedFilePath));
		}
	}
}
