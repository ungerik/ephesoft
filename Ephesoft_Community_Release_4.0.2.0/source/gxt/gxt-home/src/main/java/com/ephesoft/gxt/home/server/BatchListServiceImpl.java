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

package com.ephesoft.gxt.home.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.collections.CollectionUtils;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.EphesoftUser;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.property.BatchInstanceFilter;
import com.ephesoft.dcma.da.property.BatchInstanceProperty;
import com.ephesoft.dcma.da.property.BatchPriority;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.gxt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.gxt.core.shared.SubCategorisedData;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.DataFilter;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.home.client.BatchListService;
import com.ephesoft.gxt.home.client.i18n.BatchListConstants;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.data.shared.loader.FilterConfig;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

/**
 * The server side implementation for the calls coming from client side.
 * 
 * @author Ephesoft
 * 
 */
public class BatchListServiceImpl extends DCMARemoteServiceServlet implements BatchListService {

	/**
	 * Serial version id.
	 */
	private static final long serialVersionUID = 263606265567100312L;

	private BatchInstanceStatus getStatusFilter(final String value) {

		BatchInstanceStatus batchInstanceStatus = null;
		if (value != null) {
			int statusInt = Integer.parseInt(value);
			BatchInstanceStatus[] statusArray = BatchInstanceStatus.values();

			for (BatchInstanceStatus status : statusArray) {
				if (status.getId().intValue() == statusInt) {
					batchInstanceStatus = status;
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

	public PagingLoadResult<BatchInstanceDTO> getBatchInstanceDTOs(final List<DataFilter> filters,
			final FilterPagingLoadConfig loadConfig) {
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
		Set<String> userRoles = getUserRoles();
		EphesoftUser ephesoftUser = EphesoftUser.NORMAL_USER;

		batchInstanceList = batchInstanceService.getBatchInstances(batchIdentifierTextSearch, batchNameTextSearch, statusList, -1, -1,
				orderList, filterClauseList, batchPriorities, getUserName(), userRoles, ephesoftUser, null);
		BatchInstanceDTO batchInstanceDTO = null;
		ArrayList<BatchInstanceDTO> batches = new ArrayList<BatchInstanceDTO>();
		for (BatchInstance instance : batchInstanceList) {
			batchInstanceDTO = convertBatchInstanceToBatchInstanceDTO(instance);
			batches.add(batchInstanceDTO);
		}
		this.applyFilter(loadConfig, batches);
		List<BatchInstanceDTO> finalResult = applyPagination(loadConfig, batches);
		int totalSize = batches.size();
		return new PagingLoadResultBean<BatchInstanceDTO>(finalResult, totalSize, getOffSet(loadConfig, batches));
	}

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

	public List<BatchInstanceDTO> applyPagination(FilterPagingLoadConfig config, List<BatchInstanceDTO> totalList) {
		int totalRows = totalList.size();
		int offset = config.getOffset();
		int totalDisplayableRows = config.getLimit();
		int upperRowLimit = totalRows == 0 ? 0 : offset + totalDisplayableRows;
		upperRowLimit = upperRowLimit > totalRows ? totalRows : upperRowLimit;
		if(offset > upperRowLimit) {
			offset = 0;
		}
		List<BatchInstanceDTO> results = new ArrayList<BatchInstanceDTO>(totalList.subList(offset, upperRowLimit));
		return results;
	}
	

	public int getOffSet(FilterPagingLoadConfig config, List<BatchInstanceDTO> totalList) {
		int totalRows = totalList.size();
		int offset = config.getOffset();
		int totalDisplayableRows = config.getLimit();
		int upperRowLimit = totalRows == 0 ? 0 : offset + totalDisplayableRows;
		upperRowLimit = upperRowLimit > totalRows ? totalRows : upperRowLimit;
		if(offset > upperRowLimit) {
			offset = 0;
		}
		return offset;
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

	private List<BatchPriority> getPriorityList(final List<DataFilter> filters) {

		List<BatchPriority> batchPriorities = new ArrayList<BatchPriority>();
		batchPriorities.clear();
		if (!filters.isEmpty()) {
			for (DataFilter filter : filters) {
				if ((BatchListConstants.PRIORITY).equals(filter.getColumn())) {
					batchPriorities.add(getPriorityValue(filter));
				}
			}
		}
		return batchPriorities;
	}

	private BatchInstanceDTO convertBatchInstanceToBatchInstanceDTO(BatchInstance instance) {
		BatchClass batchClass = instance.getBatchClass();
		Date lastModifiedDate = instance.getLastModified();
		Date creationDate = instance.getCreationDate();
		SimpleDateFormat sdf = new SimpleDateFormat(BatchListConstants.DATE_FORMAT, Locale.getDefault());
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
		batchInstanceDTO.setLastModified(instance.getLastModified().toString());
		batchInstanceDTO.setCustomColumn1(instance.getCustomColumn1());
		batchInstanceDTO.setCustomColumn2(instance.getCustomColumn2());
		batchInstanceDTO.setCustomColumn3(instance.getCustomColumn3());
		batchInstanceDTO.setCustomColumn4(instance.getCustomColumn4());

		return batchInstanceDTO;
	}

	private List<BatchInstanceStatus> getStatusList(final List<DataFilter> filters) {
		List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		for (DataFilter filter : filters) {
			if (("status").equals(filter.getColumn())) {
				String value = filter.getValue();
				BatchInstanceStatus statusFilter = getStatusFilter(value);
				statusList.add(statusFilter);
			}
		}
		return statusList;
	}

	@Override
	public List<SubCategorisedData> getBatchListStackedChartData() {
		final List<SubCategorisedData> subCategoryPeriodList = new ArrayList<SubCategorisedData>();
		final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		final List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		List<Order> orderList = null;
		orderList = new ArrayList<Order>();
		Order sortingOrder = new Order(BatchInstanceProperty.PRIORITY, false);
		orderList.add(sortingOrder);
		List<BatchInstance> batchList = batchInstanceService.getBatchInstances(null, null, statusList, -1, -1, orderList, null, null,
				getUserName(), getUserRoles(), EphesoftUser.NORMAL_USER, null);

		SubCategorisedData subCategorisedData;

		for (final BatchInstance batchInstance : batchList) {
			if (batchInstance.getPriority() >= BatchPriority.URGENT.getLowerLimit()
					&& batchInstance.getPriority() <= BatchPriority.LOW.getUpperLimit()) {
				subCategorisedData = new SubCategorisedData();
				subCategorisedData.setCategory(batchInstance.getStatus().getName());
				subCategorisedData.setData(1);
				subCategorisedData.setSubCategory(BatchPriority.getBatchPriority(batchInstance.getPriority()).getBatchPriorityName());
				subCategoryPeriodList.add(subCategorisedData);
			}
		}
		return subCategoryPeriodList;
	}

}
