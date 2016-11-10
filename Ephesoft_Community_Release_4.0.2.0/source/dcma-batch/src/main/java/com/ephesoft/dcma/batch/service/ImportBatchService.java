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

package com.ephesoft.dcma.batch.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions;
import com.ephesoft.dcma.common.ImportBatchClassResultCarrier;
import com.ephesoft.dcma.core.common.ImportExportDocument;
import com.ephesoft.dcma.core.common.ImportExportIndexField;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;

/**
 * Service to import the batch class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.ImportBatchServiceImpl
 */
public interface ImportBatchService {

	/**
	 * Method to import the batch class as specified with the options XML.
	 * 
	 * @param optionXML {@link ImportBatchClassOptions} :options to import the batch class
	 * @param isDeployed boolean :is the batch class deployed
	 * @param isFromWebService boolean :is the call from web service.
	 * @param gropsToAssign {@link Set}<{@link String}> :is the set of groups to assign.
	 * @return {@link ImportBatchClassResultCarrier}
	 */
	ImportBatchClassResultCarrier importBatchClass(ImportBatchClassOptions optionXML, boolean isDeployed, boolean isFromWebService,
			Set<String> gropsToAssign);

	/**
	 * Method to validate the user provided option XML while calling from Web Service.
	 * 
	 * @param option {@link ImportBatchClassOptions} :option option XML to verify.
	 * @return {@link Map}<{@link Boolean}, {@link String}> :map containing the results of validation along with the appropriate error
	 *         message.
	 */
	Map<Boolean, String> validateInputXML(ImportBatchClassOptions option);

	/**
	 * Method to determine if the batch class being imported is equal to the batch class of the workflow name specified by user.
	 * 
	 * @param importBatchClass {@link BatchClass} :batch class being imported.
	 * @param userInputWorkflowName {@link String} : user inpur workflow name.
	 * @return boolean :true/false
	 */
	boolean isImportWorkflowEqualDeployedWorkflow(BatchClass importBatchClass, String userInputWorkflowName);

	/**
	 * Method to act as utility for Restart Batch API.
	 * 
	 * @param properties {@link Properties}
	 * @param batchInstance {@link BatchInstance}
	 * @param moduleName {@link String}
	 * @param isZipSwitchOn boolean
	 * @throws Exception the exception
	 */
	void updateBatchFolders(Properties properties, BatchInstance batchInstance, String moduleName, boolean isZipSwitchOn)
			throws Exception;

	/**
	 * Method to remove the batch folders and .SER files for batch instances
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return boolean true/false
	 */
	boolean removeFolders(BatchInstance batchInstance);

	/**
	 * Updates batch instance folders. Used while restarting the batches.
	 * 
	 * @param properties {@link Properties} :batch properties.
	 * @param batchInstance {@link BatchInstance} :batch instance to be started.
	 * @param moduleName {@link String} :module name from which batch is restarted.
	 * @param isZipSwitchOn boolean :variable holding switch to check batch xml XIP file.
	 * @param batchClassModules {@link List}<{@link BatchClassModule}> :respective batch class modules list.
	 * @throws Exception in case back up cannot be created.
	 */
	void updateBatchFolders(Properties properties, BatchInstance batchInstance, String moduleName, boolean isZipSwitchOn,
			List<BatchClassModule> batchClassModules) throws Exception;

	/**
	 * Method to import document types for a batch class.
	 * 
	 * @param tempOutputUnZipDir {@link String} UnZip directory path in export-batch-folder.
	 * @param isClassificationSample boolean if classification sample selected then true else false.
	 * @param bCIdentifier {@link String} batch class identifier.
	 * @return {@link Map}<{@link ImportExportDocument}, {@link DocumentType}> copied, already exist and not inserted document types.
	 */
	Map<ImportExportDocument, DocumentType> importDocumentTypes(String tempOutputUnZipDir, boolean isClassificationSample,
			String bCIdentifier);

	/**
	 * Method to Import index fields for a batch class.
	 * 
	 * @param tempOutputUnZipDir {@link String} : the temp output un zip dir
	 * @param documentTypeName {@link String} :the document type name
	 * @param bCIdentifier {@link String} : the b c identifier
	 * @return {@link Map}<{@link ImportExportIndexField}, {@link FieldType}>
	 */
	Map<ImportExportIndexField, FieldType> importIndexField(String tempOutputUnZipDir, String documentTypeName, String bCIdentifier);

	/**
	 * Deserialize index field obj.
	 * 
	 * @param tempDocDirPath {@link String}
	 * @return {@link FieldType}
	 */
	FieldType deserializeIndexFieldObj(final String tempDocDirPath);

	/**
	 * Boolean to check if number of document types to be imported exceeds limit or not.
	 *
	 * @return {@link Boolean}
	 */
	boolean isDocTypeLimitExceed();

}
