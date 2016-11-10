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

package com.ephesoft.gxt.batchinstance.client;

import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstancePresenter.Results;
import com.ephesoft.gxt.core.client.DCMARemoteService;
import com.ephesoft.gxt.core.shared.CategorisedData;
import com.ephesoft.gxt.core.shared.SubCategorisedData;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceRetriesDTO;
import com.ephesoft.gxt.core.shared.dto.DataFilter;
import com.ephesoft.gxt.core.shared.dto.ModuleDTO;
import com.ephesoft.gxt.core.shared.dto.WorkflowDetailDTO;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

@RemoteServiceRelativePath("batchInstanceService")
public interface BatchInstanceManagementService extends DCMARemoteService {

	/**
	 * API to get BatchInstanceDTO's for the given filter and in defined order.
	 * 
	 * @param startRow int
	 * @param rowsCount int
	 * @param filters {@link DataFilter}
	 * @param order {@link Order}
	 * @param searchString the searchString on which batch instances have to be fetched
	 * @return List<{@link BatchInstanceDTO}>
	 */
	PagingLoadResult<BatchInstanceDTO> getBatchInstanceDTOs(final List<DataFilter> filters, final FilterPagingLoadConfig loadConfig,  boolean applyPagination);

	public Results deleteBatchInstance(String identifier) throws UIException;

	public void clearCurrentUser(String batchInstanceIdentifier);

	Map<String, String> getRestartOptions(String batchInstanceIdentifier);

	/**
	 * API for getting the batch instance retries DTO.
	 * 
	 * @param identifier {@link String} The batch instance identifier.
	 * @return {@link BatchInstanceRetriesDTO} The DTO for batch instance Retries.
	 * @throws UIException
	 */
	BatchInstanceRetriesDTO getBatchInstanceRetriesDTO(String identifier) throws UIException;

	/**
	 * API to get BatchInstanceDTO given it's identifier.
	 * 
	 * @param identifier {@link String}
	 * @return {@link BatchInstanceDTO}
	 * @throws UIException
	 */
	BatchInstanceDTO getBatchInstanceDTO(String identifier) throws UIException;

	/**
	 * API to update BatchInstance Status given it's identifier, to the provided BatchInstanceStatus.
	 * 
	 * @param identifier {@link String}
	 * @param biStatus {@link BatchInstanceStatus}
	 * @return {@link Results}
	 * @throws UIException
	 */
	Results updateBatchInstanceStatus(String identifier, BatchInstanceStatus biStatus) throws UIException;

	/**
	 * API to restart BatchInstance given it's identifier and module name.
	 * 
	 * @param identifier {@link String}
	 * @param moduleName {@link String}
	 * @return {@link Results}
	 * @throws GWTException
	 */
	Results restartBatchInstance(String identifier, String moduleName) throws UIException;

	Results deleteBatchInstance(List<String> batchInstanceIdentifierList);

	Results restartBatchInstanceCurrent(List<String> batchInstanceIdentifierList);

	Results restartBatchInstancePrevious(List<String> batchInstanceIdentifierList);

	Results restartBatchInstanceSelectedModule(List<String> selectedBatchInstanceIdentifiers, String moduleName);

	Results restartBatchInstanceFirst(List<String> batchInstanceIdentifierList);

	Results restartBatchInstance(final String batchInstanceStatus);

	Results restartBatchInstance(final BatchInstanceDTO batchInstanceDTO);

	Results deleteInstance(final String batchInstanceStatus);

	public WorkflowDetailDTO getBatchInstanceWorkflowProgress(String batchInstanceIdentifier);

	// /**
	// * Gets the workflow details for batch instance identifier passed.
	// *
	// * <p>
	// * It will work only for non FINISHED batches.
	// * Following will be the information that can be retrieved from it.
	// * <ul>
	// * <li> Batch start workflow time.
	// * <li> Current Executing Plugin.
	// * <li> Modules and their last updated time
	// * <li> Plugins and their last updated time
	// * <li> Modules with number plugins executed for it.
	// *
	// * @param batchInstanceIdentifier identifier of batch instance.
	// * @return {@link WorkflowDetailDTO} DTO containg all the details.
	// */
	// WorkflowDetailDTO getWorkflowDetails(String batchInstanceIdentifier);

	String getBatchInstanceLogErrorDetails(String batchInstanceIdentifier);

	List<BatchInstanceDTO> getBatchInstanceDTOs(String searchText);

	Boolean updateBatchInstancePriority(BatchInstanceDTO batchInstanceDTO);

	List<CategorisedData> getBatchInstanceStatusData(final FilterPagingLoadConfig loadConfig);

	List<SubCategorisedData> getBatchInstanceExecutionDetailsData();

	/**
	 * API to check whether the OS is unix or not.
	 * 
	 * @return {@link Boolean} <code>true</code> if the OS is unix.
	 */
	boolean isUnix();

	/**
	 * Loads the batch.properties file and returns enhanced logging switch value defined in batch.properties file.
	 * 
	 * @return {@link String} ON if the switch is On otherwise return OFF.
	 */
	String getEnhancedLoggingSwitch();

	/**
	 * Returns the batch instance log error details for a prticular batch instance identifier. It returns a map which contains the
	 * cause because of which batch goes into error and the path of batch instance specific log file.
	 * 
	 * @param batchInstanceIdentifier {@link String} The batch Instance Identifier which is in ERROR state and logs information needs
	 *            to be fetched.
	 * @return {@link Map} contains the cause because of which batch goes into error and the path of batch instance specific log file.
	 */
	Map<String, Object> getOldBatchInstanceLogErrorDetails(String batchInstanceIdentifier);

	/**
	 * Get names of available modules.
	 * 
	 * @return {@link List<{@link ModuleDTO}>}
	 */
	List<ModuleDTO> getAllModules();
}
