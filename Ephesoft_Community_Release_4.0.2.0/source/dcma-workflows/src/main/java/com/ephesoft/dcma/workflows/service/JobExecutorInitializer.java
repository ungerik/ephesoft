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

package com.ephesoft.dcma.workflows.service;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.service.engine.EngineService;

/**
 * This class represents a listener to the start/refresh of the server.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 27-Nov-2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class JobExecutorInitializer implements ApplicationListener<ContextRefreshedEvent> {

	/**
	 * {@link EphesoftLogger} Instance of Logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(JobExecutorInitializer.class);

	/**
	 * {@link EngineService} Instance of engine service.
	 */
	@Autowired
	private EngineService engineService;

	/**
	 * {@link WorkflowService} Instance of workflow execution service.
	 */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * {@link ProcessCapacityService} Instance of process execution capacity service.
	 */
	@Autowired
	private ProcessCapacityService processCapacityService;

	/**
	 * {@link BatchInstanceService} Instance of batch instance service.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * {@link RestartBatchService} Instance of Restart batch service.
	 */
	@Autowired
	private RestartBatchService restartService;

	/**
	 * Executes tasks for cleaning server's pending jobs and restarting those batch instances form last module.
	 */
	@Override
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		if (!engineService.isServerReadyForJobExecution()) {
			String serverName = EphesoftContext.getHostServerContextPath();
			LOGGER.info("Removing old jobs for batches and restarting them from current module.");
			removeJobsForBatches(serverName);
			engineService.setServerReadyForJobExecution(true);
		}
	}

	/**
	 * Removes all jobs for batches in Running state being acquired by current server before its last shutdown.
	 * 
	 * @param serverName {@link String}
	 */
	private void removeJobsForBatches(final String serverName) {

		// Not based on jobs because a current job might not have lock owner assisgned when server last got down. It will lead to no
		// batch XML in memory exception.
		List<BatchInstance> batchInstances = batchInstanceService.getBatchInstanceByExecutingServerAndStatus(serverName,
				BatchInstanceStatus.RUNNING);
		if (CollectionUtils.isEmpty(batchInstances)) {
			LOGGER.info("There are no jobs to be resumed for current server.");
		} else {
			LOGGER.info("No. of jobs for resume for server: ", serverName, " is: ", batchInstances.size());
			int maxServerCapacity = processCapacityService.getMaxCapacity();
			int localCounter = 0;
			for (BatchInstance batchInstance : batchInstances) {
				localCounter = restartOldBatch(maxServerCapacity, localCounter, batchInstance);
			}
		}
	}

	private int restartOldBatch(int maxServerCapacity, int localCounter, BatchInstance batchInstance) {
		if (null != batchInstance) {
			String batchInstanceIdentifier = batchInstance.getIdentifier();
			String lastExecutedModule = restartService.getBatchLastExecutedModule(batchInstance);
			LOGGER.debug("The batch instance: ", batchInstanceIdentifier,
					" is restarting for its resume from last Executed Module: ", lastExecutedModule);
			try {
				if (localCounter < maxServerCapacity) {
					restartService.restartBatchInstance(batchInstanceIdentifier, lastExecutedModule, true, true);
				} else {
					restartService.restartBatchInstance(batchInstanceIdentifier, lastExecutedModule, true, false);
				}
				localCounter++;
			} catch (Exception exception) {
				LOGGER.error(EphesoftStringUtil.concatenate(exception, "Error while resuming batch instance: ",
						batchInstanceIdentifier, exception));
				workflowService.handleErrorBatch(batchInstance, exception, exception.getMessage());
			}
		}
		return localCounter;
	}
}
