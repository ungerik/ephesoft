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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.batch.status.StatusConveyor;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.BatchPickingAlgo;
import com.ephesoft.dcma.core.common.ServiceType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.service.BatchClassModuleService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ModuleService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.common.FifoPickUpComparator;
import com.ephesoft.dcma.workflows.common.RoundRobinPickUpComparator;
import com.ephesoft.dcma.workflows.service.DeploymentService;
import com.ephesoft.dcma.workflows.service.PickUpService;
import com.ephesoft.dcma.workflows.service.WorkflowService;
import com.ephesoft.dcma.workflows.service.engine.EngineService;
import com.ephesoft.dcma.workflows.util.WorkflowUtil;

/**
 * This class represents service to pick up batches for workflow start.
 * 
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class PickUpServiceImpl implements PickUpService {

	/**
	 * {@link Logger}
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(PickUpServiceImpl.class);

	/**
	 * {@link BatchInstanceService}
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * {@link WorkflowService}
	 */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * {@link EngineService} Instance of engine service.
	 */
	@Autowired
	private EngineService engineService;

	/**
	 * {@link ModuleService}
	 */
	@Autowired
	private ModuleService moduleService;

	/**
	 * {@link BatchClassModuleService}
	 */
	@Autowired
	private BatchClassModuleService batchClassModuleService;

	/**
	 * {@link BatchClassService}
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * {@link DeploymentService}
	 */
	@Autowired
	private DeploymentService deploymentService;

	/**
	 * {@link StatusConveyor}
	 */
	@Autowired
	private StatusConveyor statusConveyor;

	/**
	 * int Batch pick up(NEW/READY to RUNNING) capacity of the server.
	 */
	private int pickCapacity = 1;

	/**
	 * int Batch picking algorithm value as integer.
	 */
	private int batchPickingAlgoInt;;

	/**
	 * {@link BatchPickingAlgo} Instance of batch priority for pick up algorithm.
	 */
	private BatchPickingAlgo batchPickingAlgo;

	/**
	 * Pickup will run only if Pickup Service is registered for the current service.
	 */
	@Override
	public void pickUpBatchInstances() {
		if (statusConveyor.checkForServiceStatus(ServiceType.PICKUP_SERVICE)) {
			pickupBatches();
			LOGGER.debug("PickUp Service is running on a current server");
		} else {
			LOGGER.debug("PickUp Service is running on a different server");
		}
	}

	/**
	 * To pick up batch instances.
	 */
	private void pickupBatches() {
		LOGGER.trace("Entering method to start with pick up batch service.");
		ServerRegistry server = EphesoftContext.getHostServerRegistry();
		String serverContextPath = EphesoftContext.getHostServerContextPath();
		// set pick up to pickCapacity * no. of servers

		// start task
		setBatchPickUpAlgo();
		Map<Set<Integer>, Integer> prioritySetMaxCapacityMap = workflowService.getAllServerExecutionCapacity();
		int localPickUpCounter = 0;
		List<BatchInstance> batchesWaitingForPickUp = getBatchInstanceList(pickCapacity);
		for (BatchInstance batchInstance : batchesWaitingForPickUp) {
			if (null != batchInstance) {
				int priority = batchInstance.getPriority();
				String batchInstanceIdentifier = batchInstance.getIdentifier();

				// Changed to calculate pick up capacity available with respect to priority set of servers.
				Map<Integer, Integer> priorityRunningCount = workflowService.getBatchPriorityRunningCount();
				int remainingPickUpCapacity = workflowService.getRemainingCapacityForPriority(priority, prioritySetMaxCapacityMap,
						priorityRunningCount);
				LOGGER.debug("Remaining pick up capacity is: ", remainingPickUpCapacity);
				if (remainingPickUpCapacity > 0) {

					// Remaining processing capacity of all active servers. Must calculate every time this loop runs because some
					// server might go down while this loop is executing (its processing capacity must not be taken into count).
					LOGGER.debug("Remaining capacity for batch instance: ", batchInstanceIdentifier, " priority: ", priority, " is: ",
							remainingPickUpCapacity);
					if (localPickUpCounter >= pickCapacity) {
						LOGGER.debug("Remaining capacity is has reached 0. Exiting pickup service.");
						break;
					} else {
						localPickUpCounter = updateAndPickABatch(server, localPickUpCounter, batchInstance);
					}
				} else {
					LOGGER.info(" All server's max capacity has already reached for batch instance: ", batchInstanceIdentifier,
							" with priority: ", priority, ".");

				}
			}
		}
	}

	/**
	 * Updates batch instance and pick up a batch for start of workflow.
	 * 
	 * @param server {@link ServerRegistry}
	 * @param localPickUpCounter
	 * @param batchInstance {@link BatchInstance}
	 * @return int
	 */
	private int updateAndPickABatch(final ServerRegistry server, int localPickUpCounter, final BatchInstance batchInstance) {
		if (null != batchInstance) {
			BatchClass batchClass = batchInstance.getBatchClass();
			if (null != batchClass) {
				final String batchClassName = batchClass.getName();
				final String batchClassIdentifier = batchClass.getIdentifier();
				final String batchInstanceIdentifier = batchInstance.getIdentifier();
				if (!deploymentService.isDeployed(batchClassName)) {
					LOGGER.error("The batch class: ", batchClassName, "is not deployed. Batch instance: ", batchInstanceIdentifier,
							" in cannot be run.");
				} else {
					localPickUpCounter += pickABatch(server, batchClassIdentifier, batchInstance);
				}
				batchClass.setProcessName(String.valueOf(System.nanoTime()));
				batchClassService.saveOrUpdate(batchClass);
			}
		}
		return localPickUpCounter;
	}

	/**
	 * Picks a batch for start of workflow.
	 * 
	 * @param server {@link ServerRegistry}
	 * @param batchClassIdentifier {@link String}
	 * @param batchInstance {@link BatchInstance}
	 * @return int
	 */
	private int pickABatch(final ServerRegistry server, String batchClassIdentifier, final BatchInstance batchInstance) {
		int localPickUpCounter = 0;
		final BatchInstanceStatus batchInstanceStatus = batchInstance.getStatus();
		final String batchIdentifier = batchInstance.getIdentifier();
		LOGGER.debug("Trying to pick batch: ", batchIdentifier, "with current status: ", batchInstanceStatus);
		try {
			switch (batchInstanceStatus) {
				case READY:
					workflowService.clearCurrentUser(batchInstance);
					LOGGER.info("The batch: ", batchIdentifier, "'s current user is successfully cleaned.");
					localPickUpCounter = startWorkflowForBatch(batchInstance, batchClassIdentifier);
					break;
				case NEW:
					localPickUpCounter = startWorkflowForBatch(batchInstance, batchClassIdentifier);
					break;
				default:
					break;
			}
		} catch (DCMAApplicationException dcmaApplicationException) {
			String errorMessage = dcmaApplicationException.getMessage();
			LOGGER.error(dcmaApplicationException, "Unable to start/signal worklow for batch instance: ",
					batchInstance.getIdentifier(), " .Error: ", errorMessage);

			/* Move the batch to ERROR, so that batch is not stuck in RUNNING */
			workflowService.handleErrorBatch(batchInstance, dcmaApplicationException, errorMessage);
		}
		return localPickUpCounter;
	}

	/**
	 * Starts workflow for a batch.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param batchClassIdentifier {@link String}
	 * @return int
	 * @throws DCMAApplicationException {@link DCMAApplicationException}
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	private int startWorkflowForBatch(final BatchInstance batchInstance, final String batchClassIdentifier)
			throws DCMAApplicationException {
		String batchInstanceIdentifier = batchInstance.getIdentifier();
		LOGGER.trace("Entering method to start/signal workflow for a batch: ", batchInstanceIdentifier);
		int localPickUpCounter = 0;
		if (workflowService.getActiveModuleName(batchInstanceIdentifier) == null) {

			// Batch Restart or a new Batch.
			String workflowName = getWorkflowName(batchClassIdentifier, batchInstance.getExecutedModules(), batchInstanceIdentifier);
			LOGGER.debug("Workflow name for: ", batchInstanceIdentifier, " is ", workflowName);
			localPickUpCounter = startWorkFlowForNewOrRestartingBatch(batchInstance, workflowName);
		} else {

			// RR RV
			boolean isWorkflowStarted = workflowService.signalWorkflow(batchInstance);
			if (isWorkflowStarted) {
				localPickUpCounter++;
			}
		}
		LOGGER.trace("Exiting method to start/signal workflow for a batch: ", batchInstanceIdentifier);
		return localPickUpCounter;
	}

	/**
	 * Starts workflow for a NEW/READY stattus batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param workflowName {@link String}
	 * @return int
	 */
	private int startWorkFlowForNewOrRestartingBatch(final BatchInstance batchInstance, final String workflowName) {
		int localPickUpCounter = 0;
		boolean isWorkflowStarted = workflowService.startWorkflow(batchInstance.getBatchInstanceID(), workflowName);
		if (isWorkflowStarted) {
			localPickUpCounter++;
		}
		return localPickUpCounter;
	}

	/**
	 * Gets id of the last executed module.
	 * 
	 * @param executedModules {@link String}
	 * @return {@link String}
	 */
	private String getLastModuleId(final String executedModules) {
		int lastIndexOfSeparator = executedModules.lastIndexOf(ICommonConstants.EXECUTED_MODULE_ID_SEPARATOR);
		String lastExecutedModuleId = null;
		if (-1 != lastIndexOfSeparator) {
			lastExecutedModuleId = executedModules.substring(lastIndexOfSeparator - 1, lastIndexOfSeparator);
		}
		return lastExecutedModuleId;
	}

	/**
	 * Gets batch instance list waiting for pick up in a sorted order by batch picking algorithm.
	 * 
	 * @param singleTimeCapacity int
	 * @return {@link List}<{@link BatchInstance}>
	 */
	private List<BatchInstance> getBatchInstanceList(final int singleTimeCapacity) {
		final List<BatchInstanceStatus> statusList = WorkflowUtil.getBatchPickUpStatusList();
		final List<BatchInstance> batchInstanceList = batchInstanceService.getBatchInstanceByStatusList(statusList);
		List<BatchInstance> orderedBatchInstances = new ArrayList<BatchInstance>();
		BatchPickingAlgo batchPickingAlgo = getBatchPickingAlgo();
		LOGGER.debug("Picking batches using: ", batchPickingAlgo.name());
		switch (batchPickingAlgo) {
			case FIFO:
				orderedBatchInstances = batchesInBIPriorityOrder(batchInstanceList);
				break;

			case ROUND_ROBIN:
				orderedBatchInstances = sortBatchesWithRoundRobin(batchInstanceList);
				break;

			default:

				// In case of any other value, pick up batches using round robin.
				orderedBatchInstances = sortBatchesWithRoundRobin(batchInstanceList);
				break;
		}
		LOGGER.debug("Final batch instances in pick up queue are:");
		for (BatchInstance batchInstance : orderedBatchInstances) {
			LOGGER.debug("Batch Instance Identifier: ", batchInstance.getIdentifier(), " , Batch Class Identifier: ", batchInstance
					.getBatchClass().getIdentifier(), " , Priority: ", batchInstance.getPriority(), " , Status: ", batchInstance
					.getStatus().name());
		}
		return orderedBatchInstances;
	}

	/**
	 * Gets name of the workflow for the to be executed module.
	 * 
	 * @param batchClassIdentifier {@link String}
	 * @param executedModules {@link String}
	 * @param batchIdentifier {@link String}
	 * @return {@link String}
	 */
	private String getWorkflowName(final String batchClassIdentifier, final String executedModules, final String batchIdentifier) {
		String workflowName;
		if (EphesoftStringUtil.isNullOrEmpty(executedModules)) {
			workflowName = null;
		} else {
			LOGGER.debug("Executed modules is: ", executedModules, " for current picking up batch: ", batchIdentifier);
			String lastExecutedModuleId = getLastModuleId(executedModules);
			long moduleId = Long.parseLong(lastExecutedModuleId);
			Module module = moduleService.getModulePropertiesForModuleId(moduleId);
			BatchClassModule batchClassModuleByName = batchClassModuleService.getBatchClassModuleByName(batchClassIdentifier,
					module.getName());
			if (null == batchClassModuleByName) {
				workflowName = null;
			} else {
				workflowName = batchClassModuleByName.getWorkflowName();
			}
		}
		return workflowName;
	}

	/**
	 * Sets batch pick up algorithm.
	 */
	private void setBatchPickUpAlgo() {
		if (null == batchPickingAlgo) {
			batchPickingAlgo = BatchPickingAlgo.getBatchPickingAlgo(batchPickingAlgoInt);
			LOGGER.info(batchPickingAlgo, " is used as batch pick up algorithm.");
		}
	}

	/**
	 * Sorts batch instance in Round Robin order.
	 * 
	 * @param list {@link List}<{@link BatchInstance}>
	 * @return {@link List}<{@link BatchInstance}>
	 */
	private List<BatchInstance> sortBatchesWithRoundRobin(final List<BatchInstance> list) {
		LOGGER.trace("Inside pickUpBatchesUsingRoundRobin.");
		java.util.Collections.sort(list, new RoundRobinPickUpComparator());
		List<BatchInstance> finalBatchInstanceList = new ArrayList<BatchInstance>(list.size());
		if (null != list) {
			finalBatchInstanceList = sortBatchListForRoundRobin(list);
		}
		LOGGER.debug("Final batch list: ");
		for (BatchInstance batchInstance : finalBatchInstanceList) {
			LOGGER.debug("Batch Instance: ", batchInstance.getIdentifier());
		}
		LOGGER.trace("Exiting pickUp Batches Using Round Robin Algorithm.");
		return finalBatchInstanceList;
	}

	/**
	 * Sorts batch instance in Round Robin order.
	 * 
	 * @param batchInstanceList {@link List<{@link BatchInstance}>}
	 * @return {@link List<{@link BatchInstance}>}
	 */
	private List<BatchInstance> sortBatchListForRoundRobin(final List<BatchInstance> batchInstanceList) {
		LOGGER.info("Inside sortBatchInstanceList...");
		int previousBatchInstancePriority = 0;
		int batchInstancePriority = 0;
		List<BatchInstance> finalBatchInstanceList = new ArrayList<BatchInstance>();
		BatchInstance batchInstance;
		BatchClass batchClass;
		String batchClassIdentifier;
		boolean isFirst = true;
		Integer occurence = 0;
		Map<String, Integer> batchClassMap = new LinkedHashMap<String, Integer>();
		Map<String, Integer> localBatchClassMap = new LinkedHashMap<String, Integer>();
		Map<String, Integer> newBatchClassMap = new LinkedHashMap<String, Integer>();
		int end = 0;
		boolean isBottom = true;

		int batchSize = batchInstanceList.size();
		for (int index = 0; index < batchSize; index++) {
			batchInstance = batchInstanceList.get(index);
			batchClass = batchInstance.getBatchClass();
			batchClassIdentifier = batchClass.getIdentifier();
			LOGGER.info("Processing batch instance :: " + batchInstance.getIdentifier() + " ; batch class :: " + batchClassIdentifier);

			if (isFirst) {
				if (index == batchSize - 1) {
					finalBatchInstanceList.add(batchInstance);
				} else {
					batchClassMap.put(batchClassIdentifier, 1);
					previousBatchInstancePriority = batchInstance.getPriority();
					isFirst = false;
				}
			} else {
				batchInstancePriority = batchInstance.getPriority();
				if ((!(index == batchSize - 1) || isBottom) && (previousBatchInstancePriority == batchInstancePriority)) {
					if (!batchClassMap.containsKey(batchClassIdentifier)) {
						batchClassMap.put(batchClassIdentifier, 1);
					} else {
						occurence = (Integer) batchClassMap.get(batchClassIdentifier);
						batchClassMap.put(batchClassIdentifier, ++occurence);
					}
					if (!(index == batchSize - 1)) {
						continue;
					}
				} else {
					end = nextBatchPriority(batchInstanceList, finalBatchInstanceList, batchClassMap, localBatchClassMap, end, index);
				}

				previousBatchInstancePriority = batchInstancePriority;
				if (end == batchSize - 1) {
					finalBatchInstanceList.add(batchInstance);
					break;
				}
				if (end == batchSize) {
					break;
				}
				int loopCheck = 0;
				newBatchClassMap.clear();

				for (int depthIndex = 0; depthIndex < batchClassMap.size();) {
					for (String key : batchClassMap.keySet()) {
						if (loopCheck == batchClassMap.get(key)) {
							newBatchClassMap.put(key, 0);
							depthIndex++;
						}
					}
					loopCheck++;
				}

				batchClassMap.clear();
				isBottom = false;
				for (int innerIndex = end; innerIndex < batchSize; ++innerIndex, index = innerIndex - 2) {
					batchInstance = batchInstanceList.get(innerIndex);
					batchClass = batchInstance.getBatchClass();
					batchClassIdentifier = batchClass.getIdentifier();
					batchInstancePriority = batchInstance.getPriority();
					if (previousBatchInstancePriority == batchInstancePriority) {
						if (!newBatchClassMap.containsKey(batchClassIdentifier)) {
							if (!batchClassMap.containsKey(batchClassIdentifier)) {
								batchClassMap.put(batchClassIdentifier, 1);
							} else {
								occurence = (Integer) batchClassMap.get(batchClassIdentifier);
								batchClassMap.put(batchClassIdentifier, ++occurence);
							}
						} else {
							occurence = (Integer) newBatchClassMap.get(batchClassIdentifier);
							newBatchClassMap.put(batchClassIdentifier, ++occurence);
						}
					} else {
						index = innerIndex - 1;
						break;
					}
				}
				batchClassMap.putAll(newBatchClassMap);
			}
		}
		return finalBatchInstanceList;
	}

	/**
	 * Gets priority of the next batch.
	 * 
	 * @param batchInstanceList {@link List}<{@link BatchInstance}>
	 * @param finalBatchInstanceList {@link List}<{@link BatchInstance}>
	 * @param batchClassMap {@link Map}<{@link String},{@link Integer}>
	 * @param localBatchClassMap {@link Map}<{@link String},{@link Integer}>
	 * @param end int
	 * @param index int
	 * @return int
	 */
	private int nextBatchPriority(final List<BatchInstance> batchInstanceList, List<BatchInstance> finalBatchInstanceList,
			Map<String, Integer> batchClassMap, Map<String, Integer> localBatchClassMap, int end, int index) {
		Integer value;
		BatchInstance newBatchInstance;
		boolean isContinue;
		int begin;
		int count;
		begin = end;
		if (index == batchInstanceList.size() - 1) { // if last element in list.
			end = batchInstanceList.size();
		} else {
			end = index;
		}

		// count contains no. of batch classes.
		count = 0;
		for (Integer values : batchClassMap.values()) {
			if (values != 0) {
				count++;
			}
		}
		if (count > 1) {
			localBatchClassMap.clear();
			for (String key : batchClassMap.keySet()) {
				localBatchClassMap.put(key, 0);
			}
			do {
				isContinue = false;
				for (String key : batchClassMap.keySet()) {
					Integer find = 0;
					if (batchClassMap.containsKey(key) && localBatchClassMap.containsKey(key)
							&& !batchClassMap.get(key).equals(localBatchClassMap.get(key))) {
						for (int newIndex = begin; newIndex < end; newIndex++) {
							newBatchInstance = batchInstanceList.get(newIndex);
							if ((newBatchInstance.getBatchClass().getIdentifier()).equals(key)) {
								if (!find.equals(localBatchClassMap.get(key))) {
									find++;
								} else {
									finalBatchInstanceList.add(newBatchInstance);
									value = (Integer) localBatchClassMap.get(key);
									localBatchClassMap.put(key, ++value);
									break;
								}
							}
						}
					}
				}

				for (String key : batchClassMap.keySet()) {
					if (!batchClassMap.get(key).equals(localBatchClassMap.get(key))) {
						isContinue = true;
						break;
					}
				}
			} while (isContinue);

		} else if (count == 1) {
			for (int subIndex = begin; subIndex < end; subIndex++) {
				finalBatchInstanceList.add(batchInstanceList.get(subIndex));
			}
		}
		return end;
	}

	/**
	 * 
	 * @param list {@link List<{@link BatchInstance}>}
	 * @return {@link List<{@link BatchInstance}>}
	 */
	private List<BatchInstance> batchesInBIPriorityOrder(final List<BatchInstance> list) {
		LOGGER.trace("Entering pick up for batches in FIFO order.");
		java.util.Collections.sort(list, new FifoPickUpComparator());
		LOGGER.trace("Exiting pick up for batches in FIFO order.");
		return list;
	}

	/**
	 * 
	 * @param pickCapacity int
	 */
	public void setPickCapacity(final int pickCapacity) {
		this.pickCapacity = pickCapacity;
	}

	/**
	 * 
	 * @return {@link BatchPickingAlgo}
	 */
	public BatchPickingAlgo getBatchPickingAlgo() {
		return batchPickingAlgo;
	}

	/**
	 * Gets batch pick up algorith as integer.
	 * 
	 * @return int
	 */
	public int getBatchPickingAlgoInt() {
		return batchPickingAlgoInt;
	}

	/**
	 * Sets batch pick up algorith as integer.
	 * 
	 * @param batchPickingAlgoInt
	 */
	public void setBatchPickingAlgoInt(final int batchPickingAlgoInt) {
		this.batchPickingAlgoInt = batchPickingAlgoInt;
	}

}
