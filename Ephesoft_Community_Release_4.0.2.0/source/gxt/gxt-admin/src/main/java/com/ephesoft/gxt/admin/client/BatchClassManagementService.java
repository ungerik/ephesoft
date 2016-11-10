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

package com.ephesoft.gxt.admin.client;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.core.common.ImportExportDocument;
import com.ephesoft.dcma.core.common.ImportExportIndexField;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.gxt.admin.client.presenter.BatchClassPresenter.Results;
import com.ephesoft.gxt.core.client.DCMARemoteService;
import com.ephesoft.gxt.core.shared.CategorisedData;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassListDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginConfigDTO;
import com.ephesoft.gxt.core.shared.dto.BatchFolderListDTO;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.EmailConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.ImportBatchClassUserOptionDTO;
import com.ephesoft.gxt.core.shared.dto.KVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.ModuleDTO;
import com.ephesoft.gxt.core.shared.dto.OutputDataCarrierDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.dto.RoleDTO;
import com.ephesoft.gxt.core.shared.dto.RuleInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TestClassificationDataCarrierDTO;
import com.ephesoft.gxt.core.shared.dto.TestTableResultDTO;
import com.ephesoft.gxt.core.shared.dto.ViewLearnFileDTO;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

@RemoteServiceRelativePath("bmService")
public interface BatchClassManagementService extends DCMARemoteService {

	public PagingLoadResult<BatchClassDTO> getBatchClasses(final int offset, final int totalVisibleRows, final Order order);

	public String getBatchClassIdentifierForBatchClassName(String batchClassName);

	public BatchClassDTO getBatchClass(final String batchClassIdentifier);

	public Results deleteBatchClasses(List<BatchClassDTO> selectedBatchClasses);

	public Results clearCurrentUser(List<BatchClassDTO> selectedBatchClasses);

	public Boolean isBtachesInRunningOrErrorState(String batchClassIdentifier) throws Exception;

	String getCurrentBatchClassEncryptionAlgo(String batchClassIdentifier);

	int generateBatchClassLevelKey(String batchClassKey, String selectedEncryptionAlgo, String batchClassIdentifier) throws Exception;

	void sampleGeneration(List<String> batchClassIDList);

	void learnFileForBatchClass(String batchClassID) throws Exception;

	/**
	 * API to get All Batch Classes.
	 * 
	 * @return List<{@link BatchClassDTO}>
	 */
	public List<BatchClassDTO> getAllBatchClasses();

	/**
	 * API to create Unc Folder given the path
	 * 
	 * @param path {@link String}
	 * @throws GWTException {@link GWTException}
	 */
	void createUncFolder(String path) throws Exception;

	/**
	 * API to copy Batch Class given a BatchClassDTO.
	 * 
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @throws Exception {@link Exception}
	 */
	String copyBatchClass(BatchClassDTO batchClassDTO, Boolean isGridWorkflow) throws Exception;

	/**
	 * Retrieves list of all the table columns from table column pool.
	 * 
	 * @return {@link List}< {@link TableColumnInfoDTO}> The list of table columns.
	 */
	List<TableColumnInfoDTO> getAllTableColumnsFromPool();

	/**
	 * API to retrieve all table rules
	 * 
	 * @return {@link List<RuleInfoDTO>}
	 */
	List<RuleInfoDTO> getAllTableRules();

	/**
	 * Releases all the locks form the application features which are being locked by the current user i.e. by the user who have logged
	 * in into the application.
	 */
	void releaseAllLocks();

	/**
	 * API to update Batch Class given BatchClassDTO.
	 * 
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @return {@link BatchClassDTO}
	 * @throws UIException @{@link UIException} in case batch class is unlocked by super user or batch class is already deleted.
	 */
	BatchClassDTO updateBatchClass(BatchClassDTO batchClassDTO) throws UIException;

	/**
	 * Verifies whether the Email configuration present in the <code>EmailConfigurationDTO</code> object is correct or not. Works for
	 * all possible Email connections irrespective of connectivity protocol and SSL security constraints combination.
	 * 
	 * @param emailConfigDTO {@link EmailConfigurationDTO} containing the email configuration
	 * @return {@link Boolean}, <code>true</code> if connectivity was successful, <code>false</code> otherwise.
	 */
	Boolean verifyEmailConfig(EmailConfigurationDTO emailConfigDTO);

	/**
	 * API to retrieve web scanner master configuration as a map that has config name as a key and sample values for that key.
	 * 
	 * @return {@link Map<{@link String},{@link List<{@link String}>}>} map that has config name as a key and sample values for that
	 *         key.
	 */
	Map<String, List<String>> getScannerMasterConfigs();

	/**
	 * Returns the hidden password string for UI display purpose by replacing all the characters in the password string with
	 * <code>*</code>.
	 * 
	 * @param password {@link String} original password string.
	 * @return {@link String} The hidden password string. Returns <code>null</code> if the input password is <code>null</code>.
	 */
	String getEncryptedPasswordString(EmailConfigurationDTO emailConfigDTO);

	Batch testExtraction(String batchClassID, String classificationType, List<String> extractionPluginNames) throws Exception;

	void clearExtractionFolder(String batchClassID) throws Exception;

	List<String> getBatchClassExtractionPlugins(String batchClassID) throws UIException;

	Boolean isExtractionFolderEmpty(String batchClassID) throws UIException;

	/**
	 * API to create and deploy the JPDL's for the new workflow created.
	 * 
	 * @param workflowName {@link String}
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @param boolean
	 * @return {@link BatchClassDTO}
	 * @throws GWTException throws Exception if some issue occur or problem occur in updating batch class.
	 */
	BatchClassDTO createAndDeployWorkflowJPDL(String workflowName, BatchClassDTO batchClassDTO, boolean isGridWorkflow)
			throws UIException;

	/**
	 * API to get Batch Folder List.
	 * 
	 * @return {@link BatchClassDTO}
	 */
	public BatchFolderListDTO getBatchFolderList();

	/**
	 * Retrieves Folder Path For Advanced KV Image
	 * 
	 * @return{@link String}
	 */
	List<String> getAdvancedKVImageUploadPath(String batchClassId, String docName, String testImageName, boolean getPageCount);

	int getPageCount(String imageUrl);

	List<OutputDataCarrierDTO> testAdvancedKVExtraction(BatchClassDTO batchClassDTO, KVExtractionDTO kvExtractionDTO, String docName,
			String imageName) throws Exception;

	List<Span> getSpanValues(final BatchClassDTO batchClassDTO, String imageName, String docName, LinkedList<Integer> pageProp)
			throws Exception;

	void deleteEmptyFile(final BatchClassDTO batchClassDTO, String imageName, String docName) throws Exception;

	// Module
	/**
	 * Get names of available modules.
	 * 
	 * @return {@link List<{@link ModuleDTO}>}
	 */
	List<ModuleDTO> getAllModules();

	/**
	 * API to get list of all plug-ins.
	 * 
	 * @return {@link List<{@link PluginDetailsDTO}>}
	 */
	List<PluginDetailsDTO> getAllPluginDetailDTOs();

	/**
	 * Creates a new module.
	 * 
	 * @param moduleDTO {@link ModuleDTO}
	 * @throws GWTException {@link GWTException}
	 */
	ModuleDTO createNewModule(ModuleDTO moduleDTO) throws UIException;

	/**
	 * API to import Batch Class for the given ImportBatchClassUserOptionDTO object.
	 * 
	 * @param userOptions {@link ImportBatchClassUserOptionDTO}
	 * @return boolean
	 * @throws GWTException {@link GWTException}
	 */
	boolean importBatchClass(ImportBatchClassUserOptionDTO userOptions, boolean isGridWorkflow) throws UIException;

	/**
	 * API to get Project Files For Document Type given the batch Class Identifier and document Type Name.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param documentTypeName {@link String}
	 * @return List<{@link String}>
	 * @throws UIException
	 */
	List<String> getProjectFilesForDocumentType(String batchClassIdentifier, String documentTypeName) throws UIException;

	List<ViewLearnFileDTO> getLearnFileDetails(String batchClassIdentifier, String documentType) throws UIException;

	void updateBatchClassList(BatchClassListDTO batchClassListdto) throws UIException;

	/**
	 * API to get the files/folders on a path
	 * 
	 * @param file
	 * @return
	 */
	Map<ImportExportDocument, DocumentTypeDTO> importDocumentType(String tempImportDocLocation, boolean learnFileImport,
			String batchClassIdentifier) throws UIException;

	Map<ImportExportIndexField, FieldTypeDTO> importIndexField(final String tempFolderPath, final DocumentTypeDTO documentType)
			throws UIException;

	/**
	 * Testing the sample content given in classification .
	 * 
	 * @param batchClassID {@link String} the batch class identifier.
	 * @param classificationType {@link String} the classification type to be done.
	 * @return list<{@link OutputDataCarrierDTO}> the list of output DTO's.
	 * @throws UIException
	 */
	List<TestClassificationDataCarrierDTO> testContentClassification(String batchClassID, String classificationType)
			throws UIException;

	void clearClassificationFolder(String batchClassID) throws Exception;

	Boolean isClassificationFolderEmpty(String batchClassID) throws UIException;

	/**
	 * Tests Table Pattern given the BatchClassDTO and TableInfoDTO. If Rule Identifier is provided test will be performed only for
	 * that particular Extraction Rule.
	 * 
	 * @param batchClassDTO {@link BatchClassDTO}
	 * @param tableInfoDTO {@link TableInfoDTO}
	 * @param ruleIndex {@link String}
	 * @return {@link List<{@link TestTableResultDTO}>}
	 * @throws UIException {@link UIException}
	 */
	public List<TestTableResultDTO> testTablePattern(final BatchClassDTO batchClassDTO, final TableInfoDTO tableInfoDTO,
			final String ruleIndex) throws UIException;

	/**
	 * API to acquire Lock on a batch class given it's identifier.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @throws GWTException
	 */
	void acquireLock(String batchClassIdentifier) throws UIException;

	/**
	 * Gets the batch class management PPM data.
	 * 
	 * @param batchIdentifierList the batch identifier list
	 * @return the batch class management PPM data
	 */
	List<CategorisedData> getBatchClassManagementPPMData(List<String> batchIdentifierList);

	/**
	 * Gets Advanced test table Image Upload Path for the given image for a particular batch class id.
	 * 
	 * @param batchClassId {@link String}
	 * @param docName {@link String}
	 * @param testImageName {@link String}
	 * @return {@link String}
	 */
	String getAdvancedTEImageUploadPath(String batchClassId, String docName, String testImageName);

	Map<String, String> getAllColumnsForTable(final ConnectionsDTO connection, final String tableName) throws Exception;

	Map<String, List<String>> getAllTables(final ConnectionsDTO connection) throws Exception;

	/**
	 * Finds all the matches inside the string for the given regex pattern and returns the list of matched indexes.
	 * 
	 * @param regex {@link String} The regex pattern generated.
	 * @param strToBeMatched {@link String} The string which is to be matched.
	 * @return {@link List<{@link String}> The list of matched indexes.
	 * @throws Exception if any exception or error occur.
	 */
	List<String> findMatchedIndexesList(String regex, String strToBeMatched) throws UIException;

	/**
	 * used for getting the map of avaliable REgex groups from DB
	 * 
	 * @return {@link Map<String, RegexGroupDTO>}
	 */
	Map<String, RegexGroupDTO> getRegexGroupMap();

	/**
	 * API for getting the CMIS configuration.
	 * 
	 * @param pluginConfigDTOValues {@link Collection<{@link BatchClassPluginConfigDTO}>} plugin configurations of the batch class.
	 * @return {@link Map<{@link String},{@link String}>}
	 * @throws {@link GWTException} If not able to connect to repository server.
	 */
	Map<String, String> getCmisConfiguration(Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues) throws UIException;

	/**
	 * API for getting the authentication URL.
	 * 
	 * @param pluginConfigDTOValues {@link Collection<BatchClassPluginConfigDTO>} plugin configurations of the batch class.
	 * @return {@link AsyncCallback<{@link String}>}
	 * @throws {@link GWTException} If not able to connect to repository server.
	 */
	String getAuthenticationURL(Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues) throws UIException;

	/**
	 * API to get All Primary Keys For Table given DB config and table name.
	 * 
	 * @param driverName {@link String}
	 * @param url {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param table {@link String}
	 * @param tableType {@link String}
	 * @return {@link List<{@link String}>}
	 * @throws Exception {@link Exception}
	 */
	List<String> getAllPrimaryKeysForTable(String driverName, String url, String userName, String password, String table,
			String tableType) throws UIException;

	/**
	 * API for getting the primary keys of a table.
	 * 
	 * @param connectionDTO {@link ConnectionsDTO}
	 * @param table {@link String}
	 * @param tableType {@link String}
	 */
	List<String> getAllPrimaryKeysForTable(ConnectionsDTO connectionDTO, String table, String tableType) throws UIException;

	/**
	 * API to get All Roles available.
	 * 
	 * @return {@link List<{@link RoleDTO}>}
	 */
	List<RoleDTO> getAllRoles();

	/**
	 * Boolean to check if number of document types to be imported exceeds limit or not.
	 *
	 * @return {@link Boolean}
	 */
	boolean isDocTypeLimitExceed();

}
