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

package com.ephesoft.dcma.workflows.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.activiti.engine.ManagementService;
import org.activiti.engine.impl.persistence.entity.JobEntity;
import org.activiti.engine.runtime.Job;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.service.CleanErrorJobService;
import com.ephesoft.dcma.workflows.service.WorkflowService;
import com.ephesoft.dcma.workflows.service.engine.EngineService;

/**
 * This class represents the implementation of Cleaning error batches job service for a third party workflow engine.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class CleanErrorJobServiceImpl implements CleanErrorJobService {

	/**
	 * {@link Logger} Instance of Logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(CleanErrorJobServiceImpl.class);

	/**
	 * {@link EngineService} Instance of engine service.
	 */
	@Autowired
	EngineService engineService;

	/**
	 * {@link WorkflowService} Instance of engine service.
	 */
	@Autowired
	WorkflowService workflowService;

	/**
	 * {@link ManagementService} Instance of management service.
	 */
	@Autowired
	private ManagementService managementService;

	/**
	 * {@link BatchSchemaService} Instance of Batch Schema service.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * {@link PluginPropertiesService} Instance of Plugin Properties service.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * {@link BatchInstanceService} Instance of Batch Instance service.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	@Override
	public void cleanErroredJobs() {
		clearInMemoryResources();
		List<Job> errorJobs = managementService.createJobQuery().noRetriesLeft().withException().list();
		LOGGER.info("Cleaning error jobs from runtime engine tables..");
		if (CollectionUtils.isNotEmpty(errorJobs)) {
			for (Job job : errorJobs) {
				if (null != job) {
					JobEntity jobEntity = (JobEntity) job;
					BatchInstance batchInstance = engineService.getBatchInstanceByJobEntity(jobEntity);
					workflowService
							.handleErrorBatch(batchInstance, null, EphesoftStringUtil.concatenate("The batch instance: ",
									batchInstance.getIdentifier(), " went into error."));
					LOGGER.info("Job with id:", jobEntity.getId(), " has been deleted for batch instance: ",
							batchInstance.getIdentifier());
				}
			}
		}
	}

	/**
	 * Clears all Resources that are associated with Non-Running Batches.
	 */
	private void clearInMemoryResources() {
		LOGGER.trace("Checking for Non-running Batch Instance Resources in Memory...");
		Set<String> propertiesKeySet = pluginPropertiesService.getInMemoryPropertiesKeys();
		Set<String> batchKeySet = batchSchemaService.getInMemoryBatchKeys();
		if (!(null == batchKeySet) && !(null == propertiesKeySet)) {
			if (!batchKeySet.isEmpty() && !propertiesKeySet.isEmpty()) {
				Set<String> combinedKeySet = new HashSet<String>();
				combinedKeySet.addAll(propertiesKeySet);
				combinedKeySet.addAll(batchKeySet);
				for (String batchInstanceIdentifier : combinedKeySet) {
					BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
					if (checkForDeletion(batchInstance)) {
						pluginPropertiesService.clearCache(batchInstanceIdentifier);
						batchSchemaService.removeBatch(batchInstanceIdentifier);
						LOGGER.debug(EphesoftStringUtil.concatenate("Deleted in-Memory resources for ", batchInstanceIdentifier));
					}
				}
			}
		}

	}

	/**
	 * Checks if the given batch instance has a status of ERROR/FINISHED/READY_FOR_REVIEW/READY_FOR_VALIDATION
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return
	 */
	private boolean checkForDeletion(BatchInstance batchInstance) {
		boolean toBeDeleted = false;
		if (!(null == batchInstance)) {
			BatchInstanceStatus currentStatus = batchInstance.getStatus();
			if (BatchInstanceStatus.ERROR == currentStatus || BatchInstanceStatus.FINISHED == currentStatus
					|| BatchInstanceStatus.READY_FOR_REVIEW == currentStatus
					|| BatchInstanceStatus.READY_FOR_VALIDATION == currentStatus) {
				LOGGER.info(EphesoftStringUtil.concatenate("Memory Resources found for :", batchInstance.getBatchInstanceID().getID(),
						"having status: ", currentStatus));
				toBeDeleted = true;
			}
		}
		return toBeDeleted;
	}
}
