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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.activiti.engine.ManagementService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.batch.status.event.ResumeServiceEvent;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.ServiceType;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.domain.ServiceStatus;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ServerRegistryService;
import com.ephesoft.dcma.da.service.ServiceStatusService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.service.InactiveServerResumeService;
import com.ephesoft.dcma.workflows.service.RestartBatchService;
import com.ephesoft.dcma.workflows.service.WorkflowService;
import com.ephesoft.dcma.workflows.service.engine.EngineService;
import com.ephesoft.dcma.workflows.service.publisher.ResumeServiceEventPublisher;
import com.ephesoft.dcma.workflows.util.WorkflowUtil;

/**
 * This class represents service to resume services and batches that are orphaned.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class InactiveServerResumeServiceImpl implements InactiveServerResumeService {

	/**
	 * {@link EphesoftLogger} Instance of engine service.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(InactiveServerResumeServiceImpl.class);

	/**
	 * {@link ResumeServiceEventPublisher} Instance of event publisher of resume service.
	 */
	@Autowired
	private ResumeServiceEventPublisher resumeServiceEventPublisher;

	/**
	 * int Resume capacity of the server.
	 */
	private int resumeCapacity;

	/**
	 * int Max capacity of the server.
	 */
	private int maxCapacity = 1;

	/**
	 * {@link ServerRegistryService} Instance of registry service.
	 */
	@Autowired
	private ServerRegistryService serverRegistryService;

	/**
	 * {@link ManagementService} Instance of management service.
	 */
	@Autowired
	private ManagementService managementService;

	/**
	 * {@link ServiceStatusService} Instance of service status service.
	 */
	@Autowired
	private ServiceStatusService serviceStatusService;

	/**
	 * {@link EngineService} Instance of engine service.
	 */
	@Autowired
	private EngineService engineService;

	/**
	 * {@link WorkflowService} Instance of workflow service.
	 */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * {@link RestartBatchService} Instance of restart service.
	 */
	@Autowired
	private RestartBatchService restartService;

	/**
	 * {@link BatchInstanceService} Instance of batch instance service.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	@Override
	public void resumeServicesForInactiveServers() {
		List<ServerRegistry> inactiveServers = serverRegistryService.getInactiveServers();
		if (inactiveServers.size() == 0) {
			LOGGER.info("No inactive servers found.");
		} else {
			LOGGER.info("Resuming service is executing.");
			String serverContextPath = EphesoftContext.getHostServerContextPath();
			int count = 0;

			// Resuming services for inactive servers and resuming execution of
			// jobs of failed servers.
			for (ServerRegistry server : inactiveServers) {

				// Resuming inactive server's jobs.
				if (count <= resumeCapacity) {
					String inactiveServerContextPath = WorkflowUtil.getServerContextPath(server);
					LOGGER.debug("Inactive server resuming is: ", inactiveServerContextPath);
					List<BatchInstance> batchInstances = batchInstanceService.getBatchInstanceByExecutingServerAndStatus(
							inactiveServerContextPath, BatchInstanceStatus.RUNNING);
					count = resumeBatchesOfInactiveServer(serverContextPath, count, batchInstances);
					LOGGER.debug("No. of batches resumed by the server: ", serverContextPath, " is: ", count);
				}
				// Resuming inactive server's services.
				resumeServiceForInactiveServer(server.getId());
			}
		}
	}

	/**
	 * Resumes the services earlier availed by the in-active server passed and publishes the {@link ResumeServiceEvent} for the
	 * services to be resumed on the current server.
	 * 
	 * @param serverRegistryId {@link Long} server registry id of an inactive server.
	 */
	private void resumeServiceForInactiveServer(final Long serverRegistryId) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Resuming services under in-active server(Id): ", serverRegistryId));
		if (null != serverRegistryId) {

			// Get services for the inactive server passed
			Set<ServiceStatus> serviceStatusSet = serverRegistryService.getServicesForServer(serverRegistryId);

			// If there are services under in-active servers
			if (CollectionUtils.isEmpty(serviceStatusSet)) {
				LOGGER.debug(EphesoftStringUtil.concatenate("No service is registered under in-active server(Id): ", serverRegistryId));
			} else {
				List<ServiceType> serviceTypeList = new ArrayList<ServiceType>(serviceStatusSet.size());
				Iterator<ServiceStatus> serviceStatusIterator = serviceStatusSet.iterator();
				ServiceStatus serviceStatus = null;
				ServerRegistry hostServerRegistry = EphesoftContext.getHostServerRegistry();

				// Update current server entry for registering services
				while (serviceStatusIterator.hasNext()) {
					serviceStatus = serviceStatusIterator.next();
					if (ServiceType.LICENSE_SERVICE != serviceStatus.getServiceType()) {

						// to prevent licence service getting updating from
						// here. License service will get updated in
						// LicenseClient
						// class.
						serviceStatus.setServerRegistry(hostServerRegistry);
						serviceTypeList.add(serviceStatus.getServiceType());
						serviceStatusService.updateServiceStatus(serviceStatus);
						LOGGER.debug(EphesoftStringUtil.concatenate("Resuming service: ", serviceStatus.getServiceType()));
					}
				}

				// Publishing event to resume services
				resumeServiceEventPublisher.publishEvent(serviceTypeList);
			}
		}
	}

	/**
	 * Resumes batches of inactive server.
	 * 
	 * @param serverContextPath {@link String}
	 * @param count int
	 * @param inactiveBatches {@link List}<{@link BatchInstance}>
	 * @return int
	 */
	private int resumeBatchesOfInactiveServer(final String serverContextPath, int count, final List<BatchInstance> inactiveBatches) {
		if (CollectionUtils.isEmpty(inactiveBatches)) {
			LOGGER.debug("No. of inactive batches is 0 for server: ", serverContextPath);
		} else {
			LOGGER.debug("No. of inactive batches is: ", inactiveBatches.size(), "for server: ", serverContextPath);
			int runningJobCount;
			List<BatchInstance> batchListToDeleteExecution = new ArrayList<BatchInstance>(1);
			for (BatchInstance batchInstance : inactiveBatches) {
				Set<String> runningbatchIdentifiersForCurrentServer = engineService
						.getRunningBatchInstanceIdentifiers(serverContextPath);
				if (CollectionUtils.isEmpty(runningbatchIdentifiersForCurrentServer)) {
					runningJobCount = 0;
				} else {
					runningJobCount = runningbatchIdentifiersForCurrentServer.size();
				}

				// Resume can only be done for batches of this server's batch priority range.
				int batchPriority = batchInstance.getPriority();
				Set<Integer> setOfPriority = serverRegistryService.getNumericSetOfPriorityRange();
				if (null != setOfPriority && setOfPriority.contains(batchPriority)) {
					if (count + runningJobCount >= maxCapacity) {
						LOGGER.info("The server:", serverContextPath,
								" is running batches upto its max capacity. It cannot resume more batches from inactive servers.");
						break;
					} else if (count >= resumeCapacity) {
						LOGGER.info("The server:", serverContextPath, "'s resume limit has reached. It cannot instantiate more than ",
								resumeCapacity, " batches.");
						break;
					} else {
						LOGGER.debug("There is capacity for resume in server: ", serverContextPath);
						count = resumeABatch(serverContextPath, count, batchInstance, batchListToDeleteExecution);
					}
				}
			}
			cleanErrorBatchJobs(batchListToDeleteExecution);
		}
		return count;
	}

	/**
	 * Cleans error batch jobs.
	 * 
	 * @param batchListToDeleteExecution {@link List}<{@link BatchInstance}>
	 */
	private void cleanErrorBatchJobs(final List<BatchInstance> batchListToDeleteExecution) {
		if (CollectionUtils.isNotEmpty(batchListToDeleteExecution)) {
			for (BatchInstance batchInstance : batchListToDeleteExecution) {
				String errorMessage = EphesoftStringUtil.concatenate("The batch: ", batchInstance.getIdentifier(),
						" could not be resumed.");
				workflowService.handleErrorBatch(batchInstance, null, errorMessage);
			}
		}
	}

	/**
	 * Resumes a batch instance.
	 * 
	 * @param serverContextPath {@link String}
	 * @param count int
	 * @param batchInstance {@link BatchInstance}
	 * @param batchListToDeleteExecution {@link List}<{@link BatchInstance}>
	 * @return int
	 */
	private int resumeABatch(final String serverContextPath, int count, final BatchInstance batchInstance,
			final List<BatchInstance> batchListToDeleteExecution) {
		if (null == batchInstance) {
			LOGGER.info("Resuming batch instance is null. Cannot resume it.");
		} else {
			String batchInstanceIdentifier = batchInstance.getIdentifier();
			LOGGER.info("Resuming batch instance: ", batchInstanceIdentifier);
			try {
				BatchInstanceStatus batchStatus = batchInstance.getStatus();
				if (batchStatus == BatchInstanceStatus.RUNNING) {
					String lastExecutedModule = restartService.getBatchLastExecutedModule(batchInstance);
					LOGGER.debug("Last executed module is: ", lastExecutedModule, " for batch intance: ", batchInstanceIdentifier);
					restartService.restartBatchInstance(batchInstanceIdentifier, lastExecutedModule, true, true);
					count++;
				}
			} catch (Exception exception) {
				LOGGER.error("Error in resuming workflow for Batch Instance: ", exception.getMessage(), exception);
			}
		}
		return count;
	}

	/**
	 * Gets resume capacity.
	 * 
	 * @return int
	 */
	public int getResumeCapacity() {
		return resumeCapacity;
	}

	/**
	 * Sets resume capacity.
	 * 
	 * @param resumeCapacity int
	 */
	public void setResumeCapacity(final int resumeCapacity) {
		this.resumeCapacity = resumeCapacity;
	}

	/**
	 * Gets max process capacity.
	 * 
	 * @return int
	 */
	public int getMaxCapacity() {
		return maxCapacity;
	}

	/**
	 * Sets max process capacity.
	 * 
	 * @param maxCapacity int
	 */
	public void setMaxCapacity(final int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

}
