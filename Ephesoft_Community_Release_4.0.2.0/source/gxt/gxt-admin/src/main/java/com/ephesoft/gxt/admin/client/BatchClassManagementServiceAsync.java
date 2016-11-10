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
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface BatchClassManagementServiceAsync extends DCMARemoteServiceAsync {

	void getBatchClasses(int offset, int totalVisibleRows, Order order, AsyncCallback<PagingLoadResult<BatchClassDTO>> callback);

	void getBatchClass(String batchClassIdentifier, AsyncCallback<BatchClassDTO> callback);

	void deleteBatchClasses(List<BatchClassDTO> selectedBatchClasses, AsyncCallback<Results> asyncCallback);

	void clearCurrentUser(List<BatchClassDTO> selectedBatchClasses, AsyncCallback<Results> asyncCallback);

	void isBtachesInRunningOrErrorState(String batchClassIdentifier, AsyncCallback<Boolean> asyncCallback);

	void getBatchClassIdentifierForBatchClassName(String batchClassName, AsyncCallback<String> asyncCallback);

	void getCurrentBatchClassEncryptionAlgo(String batchClassIdentifier, AsyncCallback<String> asyncCallback);

	void generateBatchClassLevelKey(String batchClassKey, String selectedEncryptionAlgo, String batchClassIdentifier,
			AsyncCallback<Integer> asyncCallback);

	void sampleGeneration(List<String> batchClassIDList, AsyncCallback<Void> callback);

	void learnFileForBatchClass(String batchClassID, AsyncCallback<Void> callback);

	void getAllBatchClasses(AsyncCallback<List<BatchClassDTO>> asyncCallback);

	void createUncFolder(String uncFolder, AsyncCallback<Void> asyncCallback);

	void copyBatchClass(BatchClassDTO batchClassDTO, Boolean gridCheckBoxValue, AsyncCallback<String> asyncCallback);

	void updateBatchClass(BatchClassDTO batchClassDTO, AsyncCallback<BatchClassDTO> callback);

	void getScannerMasterConfigs(AsyncCallback<Map<String, List<String>>> callback);

	void releaseAllLocks(AsyncCallback<Void> callback);

	void getAllTableRules(AsyncCallback<List<RuleInfoDTO>> callback);

	void getAllTableColumnsFromPool(AsyncCallback<List<TableColumnInfoDTO>> callback);

	void verifyEmailConfig(EmailConfigurationDTO emailConfigDTO, AsyncCallback<Boolean> callback);

	void getEncryptedPasswordString(EmailConfigurationDTO emailDTO, AsyncCallback<String> callback);

	void testExtraction(String batchClassID, String classificationType, List<String> extractionPluginNames,
			AsyncCallback<Batch> callback);

	void clearExtractionFolder(String batchClassID, AsyncCallback<Void> callback);

	void getBatchClassExtractionPlugins(String batchClassID, AsyncCallback<List<String>> callback);

	void isExtractionFolderEmpty(String batchClassID, AsyncCallback<Boolean> callback);

	void getAdvancedKVImageUploadPath(String batchClassId, String docName, String imageName, boolean getPageCount,
			AsyncCallback<List<String>> asyncCallback);

	// New Method
	void getPageCount(String imageUrl, AsyncCallback<Integer> asyncCallback);

	void testAdvancedKVExtraction(BatchClassDTO batchClassDTO, KVExtractionDTO kvExtractionDTO, String docName, String imageName,
			AsyncCallback<List<OutputDataCarrierDTO>> callback);

	void getSpanValues(BatchClassDTO batchClassDTO, String imageName, String docName, LinkedList<Integer> pageProp,
			AsyncCallback<List<Span>> callback);

	void deleteEmptyFile(BatchClassDTO batchClassDTO, String imageName, String docName, AsyncCallback<Void> callback);

	// Modules Listing
	void getAllModules(AsyncCallback<List<ModuleDTO>> callback);

	void getAllPluginDetailDTOs(AsyncCallback<List<PluginDetailsDTO>> callback);

	void createNewModule(ModuleDTO moduleDTO, AsyncCallback<ModuleDTO> callback);

	void createAndDeployWorkflowJPDL(String workflowName, BatchClassDTO batchClassDTO, boolean isGridWorkflow,
			AsyncCallback<BatchClassDTO> callback);

	void getBatchFolderList(AsyncCallback<BatchFolderListDTO> asyncCallback);

	/**
	 * API to import Batch Class for the given ImportBatchClassUserOptionDTO object.
	 * 
	 * @param userOptions {@link ImportBatchClassUserOptionDTO}
	 * @return boolean
	 */
	void importBatchClass(ImportBatchClassUserOptionDTO userOptions, boolean isGridWorkflow, AsyncCallback<Boolean> asyncCallback);

	void getProjectFilesForDocumentType(String batchClassIdentifier, String documentTypeName, AsyncCallback<List<String>> callback);

	void updateBatchClassList(BatchClassListDTO batchClassListdto, AsyncCallback<Void> callback) throws UIException;

	void getLearnFileDetails(String pathLearnFiles, String pageType, AsyncCallback<List<ViewLearnFileDTO>> callback);

	void importDocumentType(String tempImportDocLocation, boolean isLuceneImport, String batchClassIdentifier,
			AsyncCallback<Map<ImportExportDocument, DocumentTypeDTO>> asyncCallBack);

	void importIndexField(String tempFolderPath, DocumentTypeDTO documentType,
			AsyncCallback<Map<ImportExportIndexField, FieldTypeDTO>> asyncCallBack);

	void testContentClassification(String batchClassID, String classificationType,
			AsyncCallback<List<TestClassificationDataCarrierDTO>> asyncCallback);

	void clearClassificationFolder(String batchClassID, AsyncCallback<Void> callback);

	void isClassificationFolderEmpty(String batchClassID, AsyncCallback<Boolean> callback);

	void testTablePattern(BatchClassDTO batchClassDTO, TableInfoDTO tableInfoDTO, String ruleIndex,
			AsyncCallback<List<TestTableResultDTO>> asyncCallback);

	/**
	 * API to acquire Lock on a batch class given it's identifier asynchronously.
	 * 
	 * @param batchIdentifier
	 * @param callback
	 */
	void acquireLock(String batchIdentifier, AsyncCallback<Void> callback);

	/**
	 * Gets the batch class management PPM data.
	 * 
	 * @param batchIdentifierList the batch identifier list
	 * @param asyncCallback the async callback
	 * @return the batch class management PPM data
	 */
	void getBatchClassManagementPPMData(List<String> batchIdentifierList, AsyncCallback<List<CategorisedData>> asyncCallback);

	void getAdvancedTEImageUploadPath(String batchClassId, String documentName, String imageName, AsyncCallback<String> asyncCallback);

	void getAllTables(final ConnectionsDTO connection, final AsyncCallback<Map<String, List<String>>> callback);

	void getAllColumnsForTable(final ConnectionsDTO connection, final String tableName,
			final AsyncCallback<Map<String, String>> callback);

	/**
	 * Finds all the matches inside the string for the given regex pattern and returns the list of matched indexes.
	 * 
	 * @param regex {@link String} The regex pattern generated.
	 * @param strToBeMatched {@link String} The string which is to be matched.
	 * @param asyncCallback
	 * @return {@link List<{@link String}> The list of matched indexes.
	 */
	void findMatchedIndexesList(String regex, String strToBeMatched, AsyncCallback<List<String>> asyncCallback);

	/**
	 * getter for getting the map of available RegexGroups from Db
	 * 
	 * @param asyncCallback {@link AsyncCallback<Map<String, RegexGroupDTO>>}
	 */
	void getRegexGroupMap(AsyncCallback<Map<String, RegexGroupDTO>> asyncCallback);

	/**
	 * API for getting the CMIS configuration.
	 * 
	 * @param pluginConfigDTOValues {@link Collection<{@link BatchClassPluginConfigDTO}>} plugin configurations of the batch class.
	 * @param callback {@link Map<{@link String},{@link String}>}
	 * @throws {@link GWTException} If not able to connect to repository server.
	 */
	void getCmisConfiguration(Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues, AsyncCallback<Map<String, String>> callback);

	/**
	 * API for getting the authentication URL.
	 * 
	 * @param pluginConfigDTOValues {@link Collection<{@link BatchClassPluginConfigDTO}>} plugin configurations of the batch class.
	 * @param callback {@link AsyncCallback<{@link String}>}
	 * @throws GWTException {@link GWTException} If not able to connect to repository server.
	 */
	void getAuthenticationURL(Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues, AsyncCallback<String> callback);

	/**
	 * API for getting the primary keys of a table.
	 * 
	 * @param connectionDTO {@link ConnectionsDTO}
	 * @param table {@link String}
	 * @param tableType {@link String}
	 * @param callback {@link AsyncCallback<{@link String}>}
	 */
	void getAllPrimaryKeysForTable(ConnectionsDTO connectionDTO, String table, String tableType, AsyncCallback<List<String>> callback);

	/**
	 * API for getting the primary keys of a table.
	 * 
	 * @param driverName {@link String}
	 * @param url {@link String}
	 * @param userName {@link String}
	 * @param password {@link String}
	 * @param table {@link String}
	 * @param tableType {@link String}
	 * @param callback {@link AsyncCallback<{@link String}>}
	 */
	void getAllPrimaryKeysForTable(String driverName, String url, String userName, String password, String table, String tableType,
			AsyncCallback<List<String>> callback);

	void getAllRoles(AsyncCallback<List<RoleDTO>> callback);

	/**
	 * Boolean to check if number of document types to be imported exceeds limit or not.
	 *
	 * @return {@link Boolean}
	 */
	void isDocTypeLimitExceed(AsyncCallback<Boolean> callback);
}
