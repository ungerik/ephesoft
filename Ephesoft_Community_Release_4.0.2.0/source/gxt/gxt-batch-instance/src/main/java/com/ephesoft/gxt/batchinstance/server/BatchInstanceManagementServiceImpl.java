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

package com.ephesoft.gxt.batchinstance.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.BatchInstancePeriod;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.EphesoftUser;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.common.WorkflowDetail;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ThreadPool;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.BatchInstanceErrorDetails;
import com.ephesoft.dcma.da.domain.BatchInstanceRetries;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.property.BatchInstanceFilter;
import com.ephesoft.dcma.da.property.BatchInstanceProperty;
import com.ephesoft.dcma.da.property.BatchPriority;
import com.ephesoft.dcma.da.service.BatchInstanceErrorDetailsService;
import com.ephesoft.dcma.da.service.BatchInstanceRetriesService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ModuleService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.OSUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.service.BatchInstanceProgressService;
import com.ephesoft.dcma.workflows.service.RestartBatchService;
import com.ephesoft.dcma.workflows.service.WorkflowService;
import com.ephesoft.dcma.workflows.service.engine.EngineService;
import com.ephesoft.gxt.batchinstance.client.BatchInstanceManagementService;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstancePresenter.Results;
import com.ephesoft.gxt.batchinstance.client.shared.BatchInstanceProgressConvertor;
import com.ephesoft.gxt.batchinstance.client.shared.constants.BatchInfoConstants;
import com.ephesoft.gxt.core.server.BatchClassUtil;
import com.ephesoft.gxt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.gxt.core.server.cache.BatchCache;
import com.ephesoft.gxt.core.shared.CategorisedData;
import com.ephesoft.gxt.core.shared.SubCategorisedData;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceRetriesDTO;
import com.ephesoft.gxt.core.shared.dto.DataFilter;
import com.ephesoft.gxt.core.shared.dto.ModuleDTO;
import com.ephesoft.gxt.core.shared.dto.WorkflowDetailDTO;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.DateUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class BatchInstanceManagementServiceImpl extends DCMARemoteServiceServlet implements BatchInstanceManagementService {

	/**
	 * Constant for ' ' character.
	 */
	private static final char SPACE = ' ';

	/**
	 * Constant for '_' character.
	 */
	private static final char UNDERSCORE = '_';

	/**
	 * Constant for '.' character.
	 */
	private static final char DOT = '.';

	/**
	 * Suffix appended at the end of module name in JBPM4_HIST_PROCINST table.
	 */
	private static final String MODULE_SUFFIX = "-m";

	private static final long serialVersionUID = 1L;

	/**
	 * LOGGER {@link Logger} the logger object.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(BatchInstanceManagementServiceImpl.class);

	/**
	 * Constant for validate document module name.
	 */
	private static final String VALIDATE_DOCUMENT = "Validate Document";

	/**
	 * Constant for review document module name.
	 */
	private static final String REVIEW_DOCUMENT = "Review Document";

	@Override
	public PagingLoadResult<BatchInstanceDTO> getBatchInstanceDTOs(final List<DataFilter> filters,
			final FilterPagingLoadConfig loadConfig, boolean applyPagination) {
		List<Order> orderList = null;
		orderList = new ArrayList<Order>();
		Order sortingOrder = this.getOrder(loadConfig);
		if (sortingOrder == null) {
			sortingOrder = new Order(BatchInstanceProperty.ID, false);
		}
		orderList.add(sortingOrder);
		List<BatchInstanceFilter> filterClauseList = null;
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		List<BatchInstance> batchInstanceList = null;
		List<BatchPriority> batchPriorities = getPriorityList(filters);
		List<BatchInstanceStatus> statusList = getStatusList(filters);
		String batchIdentifierTextSearch = getBatchTextSearch("batchIdentifier", loadConfig.getFilters());
		String batchNameTextSearch = getBatchTextSearch("batchName", loadConfig.getFilters());
		String batchClassNameTextSearch = getBatchTextSearch("batchClassName", loadConfig.getFilters());
		Set<String> userRoles = getUserRoles();
		EphesoftUser ephesoftUser = EphesoftUser.NORMAL_USER;
		batchInstanceList = batchInstanceService.getBatchInstances(batchIdentifierTextSearch, batchNameTextSearch, statusList, -1, -1,
				orderList, filterClauseList, batchPriorities, getUserName(), userRoles, ephesoftUser, null);
		batchInstanceList = batchInstanceService.filterBatchInstanceByBatchClassName(batchInstanceList, batchClassNameTextSearch);
		BatchInstanceDTO batchInstanceDTO = null;
		ArrayList<BatchInstanceDTO> batches = new ArrayList<BatchInstanceDTO>();
		for (BatchInstance instance : batchInstanceList) {
			batchInstanceDTO = convertBatchInstanceToBatchInstanceDTO(instance);
			batches.add(batchInstanceDTO);
		}
		this.applyFilter(loadConfig, batches);
		PagingLoadResult<BatchInstanceDTO> result = null;
		int totalSize = batches.size();
		if (applyPagination) {
			List<BatchInstanceDTO> finalResult = applyPagination(loadConfig, batches);
			int offSet = getCalculatedOffSet(loadConfig, batches);
			result = new PagingLoadResultBean<BatchInstanceDTO>(finalResult, totalSize, offSet);
		} else {
			result = new PagingLoadResultBean<BatchInstanceDTO>(batches, totalSize, 0);
		}

		return result;
	}

	private String getBatchTextSearch(String columnType, List<FilterConfig> list) {
		String batchSearchText = "";
		if (!list.isEmpty()) {
			for (FilterConfig filter : list) {
				if (columnType.equals(filter.getField())) {
					batchSearchText = filter.getValue();
				}
			}
		}
		return batchSearchText;
	}

	public List<BatchInstanceDTO> applyPagination(FilterPagingLoadConfig config, List<BatchInstanceDTO> totalList) {
		int totalRows = totalList.size();
		int offset = config.getOffset();
		if (offset > totalRows) {
			offset = 0;
		}
		int totalDisplayableRows = config.getLimit();
		int upperRowLimit = totalRows == 0 ? 0 : offset + totalDisplayableRows;
		upperRowLimit = upperRowLimit > totalRows ? totalRows : upperRowLimit;
		List<BatchInstanceDTO> results = new ArrayList<BatchInstanceDTO>(totalList.subList(offset, upperRowLimit));
		return results;
	}

	public int getCalculatedOffSet(FilterPagingLoadConfig config, List<BatchInstanceDTO> totalList) {
		int totalRows = totalList.size();
		int offset = config.getOffset();
		if (offset > totalRows) {
			offset = 0;
		}
		return offset;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Order getOrder(FilterPagingLoadConfig loadConfig) {
		List<? extends SortInfo> sortInfoList = loadConfig.getSortInfo();
		Order sortingOrder = null;
		if (!CollectionUtil.isEmpty(sortInfoList)) {
			SortInfo info = sortInfoList.get(0);
			boolean ascending = info.getSortDir() == SortDir.ASC ? true : false;
			BatchInstanceProperty property = BatchInstanceProperty.getDTOProperty(info.getSortField());
			sortingOrder = new Order(property, ascending);
		}
		return sortingOrder;
	}

	private BatchInstanceDTO convertBatchInstanceToBatchInstanceDTO(BatchInstance instance) {
		BatchClass batchClass = instance.getBatchClass();
		Date lastModifiedDate = instance.getLastModified();
		Date creationDate = instance.getCreationDate();
		SimpleDateFormat sdf = new SimpleDateFormat(BatchInstanceConstants.DATE_FORMAT, Locale.getDefault());
		BatchInstanceDTO batchInstanceDTO = new BatchInstanceDTO();
		batchInstanceDTO.setPriority(instance.getPriority());
		batchInstanceDTO.setId(instance.getId());
		batchInstanceDTO.setBatchIdentifier(instance.getIdentifier());
		batchInstanceDTO.setBatchName(instance.getBatchName());
		batchInstanceDTO.setBatchClassName(batchClass.getDescription());
		batchInstanceDTO.setUploadedOn(sdf.format(lastModifiedDate));
		batchInstanceDTO.setImportedOn(sdf.format(creationDate));
		batchInstanceDTO.setNoOfDocuments(null);
		batchInstanceDTO.setExecutedModules(instance.getExecutedModules());
		batchInstanceDTO.setReviewStatus(null);
		batchInstanceDTO.setValidationStatus(null);
		batchInstanceDTO.setNoOfPages(null);
		batchInstanceDTO.setStatus(instance.getStatus().name());
		batchInstanceDTO.setCurrentUser(instance.getCurrentUser() != null ? instance.getCurrentUser() : "");
		batchInstanceDTO.setRemote(instance.isRemote());
		batchInstanceDTO.setUncSubFolderPath(instance.getUncSubfolder());
		batchInstanceDTO.setLastModified(sdf.format(lastModifiedDate));
		batchInstanceDTO.setCustomColumn1(instance.getCustomColumn1());
		batchInstanceDTO.setCustomColumn2(instance.getCustomColumn2());
		batchInstanceDTO.setCustomColumn3(instance.getCustomColumn3());
		batchInstanceDTO.setCustomColumn4(instance.getCustomColumn4());
		batchInstanceDTO.setBatchDescription(instance.getBatchDescription());
		return batchInstanceDTO;
	}

	private List<BatchInstanceStatus> getStatusList(final List<DataFilter> filters) {

		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.clear();
		boolean found = false;
		if (filters.isEmpty()) {
			statusList.addAll(getStatusFilter("-2"));
		} else {
			for (DataFilter filter : filters) {
				if ((BatchInstanceConstants.STATUS).equals(filter.getColumn())) {
					String value = filter.getValue();
					List<BatchInstanceStatus> statusFilter = getStatusFilter(value);
					statusList.addAll(statusFilter);
					found = true;
				}
			}
			if (!found) {
				statusList.addAll(getStatusFilter("-2"));
			}
		}
		return statusList;
	}

	private List<BatchPriority> getPriorityList(final List<DataFilter> filters) {

		List<BatchPriority> batchPriorities = new ArrayList<BatchPriority>();
		batchPriorities.clear();
		if (!filters.isEmpty()) {
			for (DataFilter filter : filters) {
				if ((BatchInstanceConstants.PRIORITY).equals(filter.getColumn())) {
					batchPriorities.add(getPriorityValue(filter));
				}
			}
		}
		return batchPriorities;
	}

	private List<BatchInstanceStatus> getStatusFilter(final String value) {

		List<BatchInstanceStatus> batchInstanceStatus = new ArrayList<BatchInstanceStatus>();
		if (value != null) {
			int statusInt = -2;
			try {
				statusInt = Integer.parseInt(value);
			} catch (NumberFormatException e) {
			}
			BatchInstanceStatus[] statusArray = BatchInstanceStatus.values();
			if (statusInt == -2) {
				for (BatchInstanceStatus status : statusArray) {
					if (!(status.equals(BatchInstanceStatus.FINISHED) || status.equals(BatchInstanceStatus.DELETED))) {
						batchInstanceStatus.add(status);
					}
				}
			} else if (statusInt == -1) {
				for (BatchInstanceStatus status : statusArray) {
					batchInstanceStatus.add(status);
				}
			} else {
				for (BatchInstanceStatus status : statusArray) {
					if (status.getId().intValue() == statusInt) {
						batchInstanceStatus.add(status);
					}
				}
			}
		}
		return batchInstanceStatus;
	}

	private BatchPriority getPriorityValue(final DataFilter filter) {
		BatchPriority priorityValue = null;
		if (filter != null) {
			int priorityInt = Integer.parseInt(filter.getValue());
			BatchPriority[] priorities = BatchPriority.values();

			for (BatchPriority priority : priorities) {
				if (priority.getLowerLimit() != null && priority.getLowerLimit().intValue() == priorityInt) {
					priorityValue = priority;
				}
			}
		}
		return priorityValue;
	}

	@Override
	public Results deleteBatchInstance(String identifier) throws UIException {
		Results result = null;
		EngineService engineService = this.getSingleBeanOfType(EngineService.class);
		if (identifier != null) {
			try {
				boolean isDeleted = engineService.deleteBatchInstance(identifier);
				if (isDeleted) {
					result = Results.SUCCESSFUL;
					this.deleteBatchFolders(identifier);
				}
				log.debug("Deleted batch: ", identifier);
			} catch (DCMAApplicationException e) {
				throw new UIException(e.getMessage());
			}
		}
		return result;
	}

	public Results deleteBatchFolders(String batchInstanceIdentifier) throws UIException {
		Results deleteResult = Results.SUCCESSFUL;
		BatchInstanceThread batchInstanceThread = ThreadPool.getBatchInstanceThreadList(batchInstanceIdentifier);
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		try {
			if (batchInstanceThread != null) {
				batchInstanceThread.remove();
			}
			removeFolders(batchInstance);
		} catch (Exception e) {
			deleteResult = Results.FAILURE;
		}
		return deleteResult;
	}

	private boolean removeFolders(BatchInstance batchInstance) throws IOException, DCMAApplicationException {
		File systemFolderFile = new File(batchInstance.getLocalFolder() + File.separator + batchInstance.getIdentifier());
		File propertiesFile = new File(batchInstance.getLocalFolder() + File.separator + BatchInstanceConstants.PROPERTIES_DIRECTORY
				+ File.separator + batchInstance.getIdentifier() + FileType.SER.getExtensionWithDot());
		boolean deleted = true;

		if (null != systemFolderFile) {
			deleted &= FileUtils.cleanUpDirectory(systemFolderFile);
		}
		if (null != propertiesFile) {
			deleted &= propertiesFile.delete();
		}
		return deleted;
	}

	@Override
	public void clearCurrentUser(String batchInstanceIdentifier) {
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		batchInstanceService.clearCurrentUser(batchInstanceIdentifier);
	}

	@Override
	public Map<String, String> getRestartOptions(String batchInstanceIdentifier) {
		Map<String, String> moduleList = null;
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
		WorkflowService workflowService = this.getSingleBeanOfType(WorkflowService.class);
		String activeModule = workflowService.getActiveModuleName(batchInstanceIdentifier);
		if (null == activeModule) {
			activeModule = workflowService.getErrorBatchLastExecutedModule(batchInstanceIdentifier);
		}
		List<BatchClassModule> batchClassModuleList = batchInstance.getBatchClass().getBatchClassModules();
		BatchClassModule currentBatchClassModule = null;
		moduleList = new LinkedHashMap<String, String>();
		if (!EphesoftStringUtil.isNullOrEmpty(activeModule) && !activeModule.contains(ICommonConstants.FOLDER_IMPORT_MODULE)
				&& EphesoftStringUtil.isNullOrEmpty(batchInstance.getExecutedModules())
				&& null == batchInstance.getRemoteBatchInstance()) {
			for (BatchClassModule batchClassModule : batchClassModuleList) {
				if (activeModule.contains(batchClassModule.getWorkflowName())) {
					currentBatchClassModule = batchClassModule;
					break;
				}
			}
			if (currentBatchClassModule != null) {
				for (BatchClassModule batchClassModule : batchClassModuleList) {
					if (currentBatchClassModule.getOrderNumber() > batchClassModule.getOrderNumber()) {
						moduleList.put(batchClassModule.getWorkflowName(), batchClassModule.getModule().getDescription());
					}
				}
			}

		} else {
			String executedModuleIds = batchInstance.getExecutedModules();
			if (executedModuleIds != null) {
				StringTokenizer tokenizer = new StringTokenizer(executedModuleIds, BatchInstanceConstants.SEMICOLON);
				while (tokenizer.hasMoreTokens()) {
					String moduleId = tokenizer.nextToken();
					for (BatchClassModule batchClassModule : batchClassModuleList) {
						if (batchClassModule.getModule().getId() == Long.valueOf(moduleId).longValue()) {
							moduleList.put(batchClassModule.getWorkflowName(), batchClassModule.getModule().getDescription());
						}
					}
				}
			}
		}
		return moduleList;
	}

	/**
	 * API for getting the batch instance retries DTO.
	 * 
	 * @param identifier {@link String} The batch instance identifier.
	 * @return {@link BatchInstanceRetriesDTO} The DTO for batch instance Retries.
	 * @throws GWTException
	 */
	@Override
	public BatchInstanceRetriesDTO getBatchInstanceRetriesDTO(String identifier) throws UIException {
		BatchInstanceRetriesDTO batchInstanceRetriesDTO = null;
		if (identifier != null && !identifier.isEmpty()) {
			log.info("Batch Instance Identifier is " + identifier);
			BatchInstanceRetriesService batchInstanceRetriesService = this.getSingleBeanOfType(BatchInstanceRetriesService.class);
			BatchInstanceRetries batchInstanceRetries = batchInstanceRetriesService.getBatchInstanceRetries(identifier);
			if (batchInstanceRetries != null) {
				batchInstanceRetriesDTO = convertBIRetriesToBIRetriesDTO(batchInstanceRetries);
			}
		}
		return batchInstanceRetriesDTO;
	}

	/**
	 * This method converts the Batch instance Retries object to the DTO.
	 * 
	 * @param batchInstanceRetries {@link BatchInstanceRetries}
	 * @return {@link BatchInstanceRetriesDTO}
	 */
	private BatchInstanceRetriesDTO convertBIRetriesToBIRetriesDTO(BatchInstanceRetries batchInstanceRetries) {
		BatchInstanceRetriesDTO batchInstanceRetriesDTO = new BatchInstanceRetriesDTO();
		batchInstanceRetriesDTO.setIdentifier(batchInstanceRetries.getIdentifier());
		batchInstanceRetriesDTO.setRetries(batchInstanceRetries.getRetries());
		batchInstanceRetriesDTO.setRestartAllowableFlag(batchInstanceRetries.getRestartAllowableFlag());
		return batchInstanceRetriesDTO;
	}

	/* The following function will fetch the BatchInstanceDTO by Batch Id */
	@Override
	public BatchInstanceDTO getBatchInstanceDTO(final String identifier) throws UIException {
		BatchInstanceDTO batchInstanceDTO = null;
		if (identifier != null && !identifier.isEmpty()) {
			BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(identifier);
			final List<BatchInstance> listBatchInstances = batchInstanceService.getBatchInstancesByBatchNameOrId(identifier,
					getUserRoles());

			// Fixed for JIRA 10221 [Batch Instance list]: No action is getting performed on batch instance, if it is assigned from DB.
			if (listBatchInstances != null && !listBatchInstances.isEmpty()) {
				for (BatchInstance batchInstance2 : listBatchInstances) {
					if (batchInstance2 != null && batchInstance2.getIdentifier() != null
							&& batchInstance2.getIdentifier().equalsIgnoreCase(batchInstance.getIdentifier())) {
						batchInstanceDTO = convertBatchInstanceToBatchInstanceDTO(batchInstance);
					}
				}
			}
		}
		return batchInstanceDTO;
	}

	@Override
	public Results updateBatchInstanceStatus(String identifier, BatchInstanceStatus batchInstanceStatus) throws UIException {
		Results result = Results.FAILURE;
		try {
			if (!StringUtil.isNullOrEmpty(identifier)) {
				BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
				batchInstanceService.updateBatchInstanceStatusByIdentifier(identifier, batchInstanceStatus);
			}
		} catch (Exception e) {
			result = Results.FAILURE;
			log.error("Error in updating batch instance status" + e.getMessage(), e);
			throw new UIException(e.getMessage());
		}

		return result;
	}

	@Override
	public Results restartBatchInstance(String identifier, String moduleName) throws UIException {
		RestartBatchService restartBatchService = this.getSingleBeanOfType(RestartBatchService.class);
		Results result = Results.FAILURE;
		if (identifier != null) {
			try {
				BatchCache.invalidate(identifier);
				boolean isRestarted = restartBatchService.restartBatchInstance(identifier, moduleName, true, false);
				if (isRestarted) {
					result = Results.SUCCESSFUL;
				}
				log.debug("Restarted batch: ", identifier);
			} catch (DCMAApplicationException e) {
				throw new UIException(e.getMessage());
			}
		}
		return result;
	}

	@Override
	public Results deleteBatchInstance(List<String> batchInstanceIdentifierList) {
		Results result = Results.SUCCESSFUL;
		if (!CollectionUtil.isEmpty(batchInstanceIdentifierList)) {
			for (final String batchInstanceIdentifier : batchInstanceIdentifierList) {
				try {
					this.deleteBatchInstance(batchInstanceIdentifier);
				} catch (UIException uiException) {
					LOGGER.error("Batch Instance could not be deleted ", uiException);
					result = Results.FAILURE;
				}
			}
		}
		return result;
	}

	@Override
	public Results restartBatchInstance(final String batchInstanceStatus) {
		Results results = null;
		if (!EphesoftStringUtil.isNullOrEmpty(batchInstanceStatus)) {

			BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			BatchInstanceStatus restartBatchStatus = BatchInstanceStatus.valueOf(batchInstanceStatus);
			if (null != restartBatchStatus && null != batchInstanceService) {
				List<BatchInstanceStatus> batchInstanceStatusList = new ArrayList<BatchInstanceStatus>(1);
				if (restartBatchStatus.isRestartableStatus()) {
					batchInstanceStatusList.add(restartBatchStatus);
					List<BatchInstance> batchInstanceList = batchInstanceService.getBatchInstanceByStatusList(batchInstanceStatusList);
					results = this.restartBatchInstances(batchInstanceList);
				}
			}
		}
		return results;
	}

	private Results restartBatchFromModuleInstance(String batchInstanceID, String restartModulePoint) throws UIException {
		Results results = null;
		if (!EphesoftStringUtil.isNullOrEmpty(batchInstanceID)) {
			BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			if (null != batchInstanceService) {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceID);
				if (null != batchInstance) {
					String moduleName = null;
					Map<String, String> executedModuleMap = getRestartOptions(batchInstanceID);
					if (BatchInstanceConstants.RESTART_CURRENT.equalsIgnoreCase(restartModulePoint)) {
						moduleName = getModuleNameForCurrent(executedModuleMap);
						if (EphesoftStringUtil.isNullOrEmpty(moduleName)) {
							moduleName = getModuleNameForPrevious(executedModuleMap);
						}
					} else if (BatchInstanceConstants.RESTART_PREVIOUS.equalsIgnoreCase(restartModulePoint)) {
						moduleName = getModuleNameForPrevious(executedModuleMap);
					} else if (BatchInstanceConstants.RESTART_FIRST.equalsIgnoreCase(restartModulePoint)) {
						moduleName = getModuleNameForFirst(executedModuleMap);
					} else {
						moduleName = getModuleNameForSelectedRestart(executedModuleMap, restartModulePoint);
					}
					Results tempResult = this.restartBatchInstance(batchInstanceID, moduleName);
					results = tempResult == Results.FAILURE ? tempResult : Results.SUCCESSFUL;
				}
			}
		}
		return results;
	}

	private String getModuleNameForSelectedRestart(Map<String, String> executedModuleMap, String restartModulePoint) {
		String moduleName = BatchInstanceConstants.EMPTY_STRING;
		if (!CollectionUtil.isEmpty(executedModuleMap)) {
			List<String> moduleNamesList = new ArrayList<String>();
			for (Map.Entry<String, String> entryValue : executedModuleMap.entrySet()) {
				moduleNamesList.add(entryValue.getValue().toLowerCase());
			}
			if (moduleNamesList.contains(restartModulePoint.toLowerCase())) {
				moduleName = restartModulePoint;
			} else {
				moduleName = getModuleNameForPrevious(executedModuleMap);
			}
		}
		return moduleName;
	}

	private String getModuleNameForCurrent(Map<String, String> executedModuleMap) {
		String moduleName = BatchInstanceConstants.DEFAULT_RESTART_MODULE;
		if (!CollectionUtil.isEmpty(executedModuleMap)) {
			for (Entry<String, String> entryset : executedModuleMap.entrySet()) {
				if (null != entryset) {
					moduleName = entryset.getValue();
				}
			}
		}
		return moduleName;
	}

	private String getModuleNameForPrevious(Map<String, String> executedModuleMap) {
		String moduleName = BatchInstanceConstants.EMPTY_STRING;
		String previousModuleName = BatchInstanceConstants.DEFAULT_RESTART_MODULE;
		if (!CollectionUtil.isEmpty(executedModuleMap)) {
			for (Entry<String, String> entryset : executedModuleMap.entrySet()) {
				if (null != entryset) {
					if (BatchInstanceConstants.EMPTY_STRING.equalsIgnoreCase(moduleName)) {
						moduleName = entryset.getValue();
					} else {
						previousModuleName = moduleName;
						moduleName = entryset.getValue();
					}
				}
			}
		}
		return previousModuleName;
	}

	private String getModuleNameForFirst(Map<String, String> executedModuleMap) {
		String moduleName = BatchInstanceConstants.DEFAULT_RESTART_MODULE;
		if (!CollectionUtil.isEmpty(executedModuleMap)) {
			for (Entry<String, String> entryset : executedModuleMap.entrySet()) {
				if (null != entryset) {
					moduleName = entryset.getValue();
					break;
				}
			}
		}
		return moduleName;
	}

	@Override
	public Results deleteInstance(final String batchInstanceStatus) {
		Results results = null;
		if (!EphesoftStringUtil.isNullOrEmpty(batchInstanceStatus)) {
			BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			BatchInstanceStatus deleteBatchStatus = BatchInstanceStatus.valueOf(batchInstanceStatus);
			if (null != deleteBatchStatus && null != batchInstanceService) {
				List<BatchInstanceStatus> batchInstanceStatusList = new ArrayList<BatchInstanceStatus>(1);
				if (deleteBatchStatus.isDeletableStatus()) {
					batchInstanceStatusList.add(deleteBatchStatus);
					List<BatchInstance> batchInstanceList = batchInstanceService.getBatchInstanceByStatusList(batchInstanceStatusList);
					results = this.deleteBatchInstances(batchInstanceList);
				}
			}
		}
		return results;

	}

	private Results restartBatchInstances(final List<BatchInstance> batchInstanceList) {
		Results finalResult = Results.SUCCESSFUL;
		if (!CollectionUtil.isEmpty(batchInstanceList)) {
			for (final BatchInstance batchInstance : batchInstanceList) {
				if (null != batchInstance) {
					try {
						BatchCache.invalidate(batchInstance.getIdentifier());
						Results tempResult = this.restartBatchInstance(batchInstance.getIdentifier(), null);
						finalResult = tempResult == Results.FAILURE ? tempResult : finalResult;
					} catch (UIException uiException) {
						LOGGER.error("Could not restart the batch ", uiException);
						finalResult = Results.FAILURE;
					}
				}
			}
		}
		return finalResult;
	}

	private Results deleteBatchInstances(final List<BatchInstance> batchInstanceList) {
		Results finalResult = Results.SUCCESSFUL;
		if (!CollectionUtil.isEmpty(batchInstanceList)) {
			for (final BatchInstance batchInstance : batchInstanceList) {
				if (null != batchInstance) {
					try {
						Results tempResult = this.deleteBatchInstance(batchInstance.getIdentifier());
						finalResult = tempResult == Results.FAILURE ? tempResult : finalResult;
					} catch (UIException uiException) {
						LOGGER.error("Could not delete the batch ", uiException);
						finalResult = Results.FAILURE;
					}
				}
			}
		}
		return finalResult;
	}

	// TODO Code is not optimized
	private void applyFilter(final FilterPagingLoadConfig loadConfig, final List<BatchInstanceDTO> batchInstanceList) {

		boolean refreshList = false;
		List<BatchInstanceDTO> filteredList = new LinkedList<BatchInstanceDTO>();
		if (null != loadConfig) {

			List<FilterConfig> filtersList = loadConfig.getFilters();
			if (!CollectionUtil.isEmpty(filtersList)) {
				String filterName = null;
				String[] values = null;
				for (FilterConfig configFilter : filtersList) {
					filterName = configFilter.getField();
					if (filterName.equalsIgnoreCase("status")) {
						refreshList = true;
						values = configFilter.getValue().split("::");
						for (String valueFilter : values) {
							List<BatchInstanceDTO> newBatchInstanceList = new ArrayList<BatchInstanceDTO>(batchInstanceList);
							BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate(filterName, valueFilter);
							CollectionUtils.filter(newBatchInstanceList, predicate);
							filteredList.addAll(newBatchInstanceList);
						}
					}
				}
				if (refreshList) {
					batchInstanceList.clear();
					batchInstanceList.addAll(filteredList);
				}
			}
		}
	}

	@Override
	public WorkflowDetailDTO getBatchInstanceWorkflowProgress(String batchInstanceIdentifier) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Getting workflow details for: ", batchInstanceIdentifier));
		WorkflowDetailDTO workflowDetailDTO = null;
		if (!EphesoftStringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			BatchInstanceProgressService batchInstanceProgressService = this.getSingleBeanOfType(BatchInstanceProgressService.class);
			WorkflowDetail workflowDetail = batchInstanceProgressService.getWorkflowDetails(batchInstanceIdentifier);
			workflowDetailDTO = BatchInstanceProgressConvertor.getWorkflowDetailDTO(workflowDetail, batchInstanceIdentifier);
			// Prepare WorkflowDetailDTO here for the time being.

		}
		LOGGER.debug("Workflow details DTO returned for the respective batch class.");
		return workflowDetailDTO;
	}

	@Override
	public String getBatchInstanceLogErrorDetails(final String batchInstanceIdentifier) {
		String errorMessage = "";
		final BatchInstanceErrorDetailsService batchInstanceErrorDetailsService = this
				.getSingleBeanOfType(BatchInstanceErrorDetailsService.class);
		final BatchInstanceErrorDetails batchInstanceErrorDetails = batchInstanceErrorDetailsService
				.getBatchInstanceErrorDetailByIdentifier(batchInstanceIdentifier);
		if (batchInstanceErrorDetails != null) {
			errorMessage = batchInstanceErrorDetails.getErrorMessage();
			LOGGER.debug(EphesoftStringUtil.concatenate("Error Message is : ", errorMessage));
		}
		return errorMessage;
	}

	/* The following function will fetch the list of BatchInstanceDTOs by matching batch name or identifier. */
	@Override
	public List<BatchInstanceDTO> getBatchInstanceDTOs(String searchString) {
		List<BatchInstanceDTO> batchInstanceDTOs = new ArrayList<BatchInstanceDTO>();
		if (searchString != null && !searchString.isEmpty()) {
			BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			List<BatchInstance> batchInstances = batchInstanceService.getBatchInstancesByBatchNameOrId(searchString, getUserRoles());
			if (batchInstances != null && !batchInstances.isEmpty()) {
				for (BatchInstance batchInstance : batchInstances) {
					if (batchInstance != null) {
						batchInstanceDTOs.add(convertBatchInstanceToBatchInstanceDTO(batchInstance));
					}
				}
			}
		}
		return batchInstanceDTOs;
	}

	@Override
	public Boolean updateBatchInstancePriority(BatchInstanceDTO batchInstanceDTO) {
		Boolean result = Boolean.FALSE;
		if (null != batchInstanceDTO) {
			BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			if (null != batchInstanceService) {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceDTO.getBatchIdentifier());
				if (batchInstance != null) {
					batchInstance.setPriority(batchInstanceDTO.getPriority());
					batchInstanceService.merge(batchInstance);
					result = Boolean.TRUE;
				}
			}
		}
		return result;
	}

	@Override
	public Results restartBatchInstanceCurrent(List<String> batchInstanceIdentifierList) {
		Results result = Results.SUCCESSFUL;
		if (!CollectionUtil.isEmpty(batchInstanceIdentifierList)) {
			for (final String batchInstanceIdentifier : batchInstanceIdentifierList) {
				try {
					BatchCache.invalidate(batchInstanceIdentifier);
					this.restartBatchFromModuleInstance(batchInstanceIdentifier, BatchInstanceConstants.RESTART_CURRENT);
				} catch (UIException uiException) {
					LOGGER.error("Batch Instance could not be restarted ", uiException);
					result = Results.FAILURE;
				}
			}
		}
		return result;
	}

	@Override
	public Results restartBatchInstancePrevious(List<String> batchInstanceIdentifierList) {
		Results result = Results.SUCCESSFUL;
		if (!CollectionUtil.isEmpty(batchInstanceIdentifierList)) {
			for (final String batchInstanceIdentifier : batchInstanceIdentifierList) {
				try {
					BatchCache.invalidate(batchInstanceIdentifier);
					this.restartBatchFromModuleInstance(batchInstanceIdentifier, BatchInstanceConstants.RESTART_PREVIOUS);
				} catch (UIException uiException) {
					LOGGER.error("Batch Instance could not be restarted ", uiException);
					result = Results.FAILURE;
				}
			}
		}
		return result;
	}

	@Override
	public Results restartBatchInstanceFirst(List<String> batchInstanceIdentifierList) {
		Results result = Results.SUCCESSFUL;
		if (!CollectionUtil.isEmpty(batchInstanceIdentifierList)) {
			for (final String batchInstanceIdentifier : batchInstanceIdentifierList) {
				try {
					BatchCache.invalidate(batchInstanceIdentifier);
					this.restartBatchFromModuleInstance(batchInstanceIdentifier, BatchInstanceConstants.RESTART_FIRST);
				} catch (UIException uiException) {
					LOGGER.error("Batch Instance could not be restarted ", uiException);
					result = Results.FAILURE;
				}
			}
		}
		return result;
	}

	@Override
	public Results restartBatchInstance(BatchInstanceDTO batchInstanceDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Results restartBatchInstanceSelectedModule(List<String> selectedBatchInstanceIdentifiers, String moduleName) {
		Results result = Results.SUCCESSFUL;
		if (!CollectionUtil.isEmpty(selectedBatchInstanceIdentifiers)) {
			for (final String batchInstanceIdentifier : selectedBatchInstanceIdentifiers) {
				try {
					BatchCache.invalidate(batchInstanceIdentifier);
					this.restartBatchFromModuleInstance(batchInstanceIdentifier, moduleName);
				} catch (UIException uiException) {
					LOGGER.error("Batch Instance could not be restarted ", uiException);
					result = Results.FAILURE;
				}
			}
		}
		return result;
	}

	@Override
	public List<CategorisedData> getBatchInstanceStatusData(final FilterPagingLoadConfig loadConfig) {
		final List<CategorisedData> pieChartData = new ArrayList<CategorisedData>();
		final ArrayList<DataFilter> dataFilterList = new ArrayList<DataFilter>(1);
		dataFilterList.add(new DataFilter(BatchInstanceConstants.STATUS, "-1"));
		dataFilterList.add(new DataFilter(BatchInstanceConstants.NAME, ""));
		dataFilterList.add(new DataFilter(BatchInstanceConstants.IDENTIFIER, ""));
		final List<BatchInstanceDTO> instanceList = this.getBatchInstanceDTOs(dataFilterList, loadConfig, false).getData();
		final Map<String, Integer> storeMap = new HashMap<String, Integer>();
		for (BatchInstanceDTO batchInstanceDTO : instanceList) {
			BatchInstanceStatus instanceStatus = BatchInstanceStatus.getBatchInstanceStatus(batchInstanceDTO.getStatus());
			if (instanceStatus != null) {
				String status = instanceStatus.getName();
				if (storeMap.containsKey(status)) {
					storeMap.put(status, storeMap.get(status) + 1);
				} else {
					storeMap.put(status, 1);
				}
			}
		}
		CategorisedData batchInstanceCategorisedData;
		for (Entry<String, Integer> entry : storeMap.entrySet()) {
			batchInstanceCategorisedData = new CategorisedData(entry.getKey(), entry.getValue());
			pieChartData.add(batchInstanceCategorisedData);
		}

		return pieChartData;
	}

	@Override
	public List<SubCategorisedData> getBatchInstanceExecutionDetailsData() {
		final List<SubCategorisedData> subCategoryPeriodList = new ArrayList<SubCategorisedData>();
		final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		final List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		final List<BatchInstance> batchList = batchInstanceService.getBatchInstances(null, null, statusList, -1, -1, null, null, null,
				getUserName(), getUserRoles(), EphesoftUser.NORMAL_USER, null);
		for (final BatchInstance batchInstance : batchList) {
			// get current date time with Date()
			final Date date = new Date();
			final Double diffInTime = DateUtil.getDateDiffInHours(batchInstance.getLastModified(), date);
			for (BatchInstancePeriod period : BatchInstancePeriod.values()) {
				if (period.getLowerLimit() <= diffInTime && period.getUpperLimit() > diffInTime) {
					final SubCategorisedData subCategorisedData = new SubCategorisedData();
					subCategorisedData.setCategory(period.toString());
					subCategorisedData.setData(1);
					subCategorisedData.setSubCategory(batchInstance.getStatus().getName());
					subCategoryPeriodList.add(subCategorisedData);
				}
			}
		}

		return subCategoryPeriodList;
	}

	/**
	 * API to check whether the OS is unix or not.
	 * 
	 * @return {@link Boolean} <code>true</code> if the OS is unix.
	 */
	public boolean isUnix() {
		return OSUtil.isUnix();
	}

	@Override
	public String getEnhancedLoggingSwitch() {
		InputStream propertyInStream = null;
		final Properties batchProperties = new Properties();
		String enhancedLoggingSwitch = null;
		try {
			final String filePath = EphesoftStringUtil.concatenate(BatchInstanceConstants.META_INF, File.separator,
					BatchInstanceConstants.DCMA_BATCH, File.separator, BatchInstanceConstants.DCMA_BATCH,
					BatchInstanceConstants.DOT_PROPERTIES);
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			batchProperties.load(propertyInStream);
			enhancedLoggingSwitch = batchProperties.getProperty(BatchInfoConstants.BATCH_INSTANCE_LOGGING);
			LOGGER.debug(EphesoftStringUtil.concatenate("Enhanced Error Logging Switch is : ", enhancedLoggingSwitch));
		} catch (final IOException ioException) {
			LOGGER.error("Error while loading the batch property file: ", ioException);
		} finally {
			IOUtils.closeQuietly(propertyInStream);
		}
		return enhancedLoggingSwitch;
	}

	@Override
	public Map<String, Object> getOldBatchInstanceLogErrorDetails(final String batchInstanceIdentifier) {
		final Map<String, Object> batchInstanceErrorDetailsMap = new HashMap<String, Object>();
		final BatchInstanceErrorDetailsService batchInstanceErrorDetailsService = this
				.getSingleBeanOfType(BatchInstanceErrorDetailsService.class);
		final BatchInstanceErrorDetails batchInstanceErrorDetails = batchInstanceErrorDetailsService
				.getBatchInstanceErrorDetailByIdentifier(batchInstanceIdentifier);
		if (batchInstanceErrorDetails != null) {
			final String errorMessage = batchInstanceErrorDetails.getErrorMessage();
			LOGGER.debug(EphesoftStringUtil.concatenate("Error Message is : ", errorMessage));
			batchInstanceErrorDetailsMap.put(BatchInfoConstants.BI_ERROR_MESSAGE, errorMessage);
		}
		final BatchSchemaService batchService = this.getSingleBeanOfType(BatchSchemaService.class);
		final String folderPath = EphesoftStringUtil.concatenate(batchService.getLocalFolderLocation(), File.separator,
				batchInstanceIdentifier, File.separator, BatchInfoConstants.LOG_FOLDER);
		if (!EphesoftStringUtil.isNullOrEmpty(folderPath)) {
			File file = new File(folderPath);
			LOGGER.debug(EphesoftStringUtil.concatenate("Batch Instance Log File Path is : ", folderPath));
			if (file.exists()) {
				batchInstanceErrorDetailsMap.put(BatchInfoConstants.BI_LOG_FILE_PATH, folderPath);
			}
		}
		return batchInstanceErrorDetailsMap;
	}

	@Override
	public List<ModuleDTO> getAllModules() {
		LOGGER.info("Getting list of modules.");
		ModuleService moduleService = this.getSingleBeanOfType(ModuleService.class);
		List<Module> modulesList = moduleService.getAllModules();
		List<ModuleDTO> moduleDTOsList = new ArrayList<ModuleDTO>();
		for (Module module : modulesList) {
			LOGGER.info("Adding " + module.getName() + " module to the list.");
			moduleDTOsList.add(BatchClassUtil.createModuleDTO(module));
		}
		return moduleDTOsList;
	}
}
