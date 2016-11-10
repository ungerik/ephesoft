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
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstancePresenter.Results;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.shared.CategorisedData;
import com.ephesoft.gxt.core.shared.SubCategorisedData;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceRetriesDTO;
import com.ephesoft.gxt.core.shared.dto.DataFilter;
import com.ephesoft.gxt.core.shared.dto.ModuleDTO;
import com.ephesoft.gxt.core.shared.dto.WorkflowDetailDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public interface BatchInstanceManagementServiceAsync extends DCMARemoteServiceAsync {

	void getBatchInstanceDTOs(final List<DataFilter> filters, final FilterPagingLoadConfig loadConfig, boolean applyPagination,
			AsyncCallback<PagingLoadResult<BatchInstanceDTO>> callback);

	void deleteBatchInstance(String identifier, AsyncCallback<Results> asyncCallback);

	void clearCurrentUser(String batchInstanceIdentifier, AsyncCallback<Void> asyncCallback);

	void getRestartOptions(String batchInstanceIdentifier, AsyncCallback<Map<String, String>> asyncCallback);

	void getBatchInstanceRetriesDTO(String identifier, AsyncCallback<BatchInstanceRetriesDTO> asyncCallback);

	void getBatchInstanceDTO(String identifier, AsyncCallback<BatchInstanceDTO> asyncCallback);

	void updateBatchInstanceStatus(String identifier, BatchInstanceStatus restartInProgress, AsyncCallback<Results> asyncCallback);

	void restartBatchInstance(String identifier, String module, AsyncCallback<Results> asyncCallback);

	void deleteBatchInstance(List<String> batchInstanceIdentifierList, AsyncCallback<Results> callback);

	void restartBatchInstanceCurrent(List<String> batchInstanceIdentifierList, AsyncCallback<Results> callback);

	void restartBatchInstance(final String batchInstanceStatus, AsyncCallback<Results> callback);

	void deleteInstance(final String batchInstanceStatus, AsyncCallback<Results> callback);

	void getBatchInstanceWorkflowProgress(String batchInstanceIdentifier,
			AsyncCallback<WorkflowDetailDTO> asyncCallback);

	// void getWorkflowDetails(String batchInstanceIdentifier, AsyncCallback<WorkflowDetailDTO> asyncCallback);
	void getBatchInstanceLogErrorDetails(String batchInstanceIdentifier, AsyncCallback<String> asyncCallback);

	void getBatchInstanceDTOs(String searchText, AsyncCallback<List<BatchInstanceDTO>> asyncCallback);

	void updateBatchInstancePriority(BatchInstanceDTO batchInstanceDTO, AsyncCallback<Boolean> asyncCallback);

	void restartBatchInstance(BatchInstanceDTO batchInstanceDTO, AsyncCallback<Results> callback);

	void restartBatchInstanceFirst(List<String> selectedBatchInstanceIdentifiers, AsyncCallback<Results> asyncCallback);

	void restartBatchInstancePrevious(List<String> selectedBatchInstanceIdentifiers, AsyncCallback<Results> asyncCallback);

	void restartBatchInstanceSelectedModule(List<String> selectedBatchInstanceIdentifiers, String moduleName,
			AsyncCallback<Results> asyncCallback);

	void getBatchInstanceStatusData(final FilterPagingLoadConfig loadConfig, AsyncCallback<List<CategorisedData>> asyncCallback);

	void getBatchInstanceExecutionDetailsData(AsyncCallback<List<SubCategorisedData>> asyncCallback);

	/**
	 * API to check whether the OS is unix or not.
	 * 
	 * @return {@link Boolean} <code>true</code> if the OS is unix.
	 */
	void isUnix(AsyncCallback<Boolean> asyncCallback);

	/**
	 * Loads the batch.properties file and returns enhanced logging switch value defined in batch.properties file.
	 * 
	 * @return {@link String} ON if the switch is On otherwise return OFF.
	 */
	void getEnhancedLoggingSwitch(AsyncCallback<String> asyncCallback);

	/**
	 * Returns the batch instance log error details for a prticular batch instance identifier. It returns a map which contains the
	 * cause because of which batch goes into error and the path of batch instance specific log file.
	 * 
	 * @param batchInstanceIdentifier {@link String} The batch Instance Identifier which is in ERROR state and logs information needs
	 *            to be fetched.
	 * @param asyncCallback {@link AsyncCallback {@link Map} .
	 * @return {@link Map} contains the cause because of which batch goes into error and the path of batch instance specific log file.
	 */
	void getOldBatchInstanceLogErrorDetails(String batchInstanceIdentifier, AsyncCallback<Map<String, Object>> asyncCallback);

	// Modules Listing
	void getAllModules(AsyncCallback<List<ModuleDTO>> callback);
}
