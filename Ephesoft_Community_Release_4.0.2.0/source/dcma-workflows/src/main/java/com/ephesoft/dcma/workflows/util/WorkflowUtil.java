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

package com.ephesoft.dcma.workflows.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;

/**
 * This class represents a utility class for the Workflow.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class WorkflowUtil {

	/**
	 * {@link Map}<{@link BatchInstanceStatus}, {@link Integer}> Collection of a batch status mapped to its priority.
	 */
	private static Map<BatchInstanceStatus, Integer> pickUpStatusToPriorityMap = null;

	/**
	 * {@link List}<{@link BatchInstanceStatus}> Collection of Batch instance status liable for pick up.
	 */
	private static List<BatchInstanceStatus> pickUpStatusList = null;

	/**
	 * {@link List}<{@link BatchInstanceStatus}> Collection of Batch instance status liable for a pause in batch processing.
	 */
	private static List<BatchInstanceStatus> pauseStateStatusList = null;

	/**
	 * int Number of Batch instance status liable for pick up.
	 */
	private static int batchPickUpStatusListSize = WorkflowConstants.NUMBER_OF_PICK_UP_STATUS;

	/**
	 * int Number of Batch instance status liable for a pause in batch processing.
	 */
	private static int batchPauseStateStatusList = WorkflowConstants.NUMBER_OF_PAUSE_STATES;

	/**
	 * Default constructor.
	 */
	private WorkflowUtil() {
	}

	/**
	 * Gets collection of batch status valid for pick up.
	 * 
	 * @return {@link List}<{@link BatchInstanceStatus}>
	 */
	public static List<BatchInstanceStatus> getBatchPickUpStatusList() {
		initilizeBatchStatusForPickUpInPriority();
		return pickUpStatusList;
	}

	/**
	 * Compare batch instance on the basis of priority of their status for Pick up.
	 * 
	 * @param batchInstance1 {@link BatchInstance}
	 * @param batchInstance2 {@link BatchInstance}
	 * @return int -1 if batchInstance1 is of higher priority than batchInstance2, -1 if batchInstance1 is of lower priority than
	 *         batchInstance2, 0 otherwise.
	 */
	public static int comparePickUpStatus(final BatchInstance batchInstance1, final BatchInstance batchInstance2) {
		int result;
		initializeBatchStatusPriorityMap();
		if (null == pickUpStatusToPriorityMap) {
			result = -1;
		} else {
			final Integer statusPriority1 = pickUpStatusToPriorityMap.get(batchInstance1.getStatus());
			final Integer statusPriority2 = pickUpStatusToPriorityMap.get(batchInstance2.getStatus());
			result = statusPriority1.compareTo(statusPriority2);
		}
		return result;
	}

	/**
	 * Initialise collection of batch status valid for pick up.
	 */
	private static void initilizeBatchStatusForPickUpInPriority() {
		if (null == pickUpStatusList) {
			pickUpStatusList = new ArrayList<BatchInstanceStatus>(batchPickUpStatusListSize);
			pickUpStatusList.add(BatchInstanceStatus.READY);
			pickUpStatusList.add(BatchInstanceStatus.NEW);
		}
	}

	/**
	 * Initialise collection of batch status valid for a pause.
	 */
	private static void initilizePauseSateStatusList() {
		if (null == pauseStateStatusList) {
			pauseStateStatusList = new ArrayList<BatchInstanceStatus>(batchPauseStateStatusList);
			pauseStateStatusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
			pauseStateStatusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		}
	}

	/**
	 * Gets collection of batch status valid for pause.
	 * 
	 * @return {@link List}<{@link BatchInstanceStatus}>
	 */
	public static List<BatchInstanceStatus> getPauseStateStatusList() {
		initilizePauseSateStatusList();
		return pauseStateStatusList;
	}

	/**
	 * Gets collection of batch status mapped to its priority.
	 * 
	 * @return {@link Map<{@link BatchInstanceStatus}, {@link Integer>}
	 */
	private static Map<BatchInstanceStatus, Integer> initializeBatchStatusPriorityMap() {
		if (null == pickUpStatusToPriorityMap) {
			initilizeBatchStatusForPickUpInPriority();
			pickUpStatusToPriorityMap = new HashMap<BatchInstanceStatus, Integer>(batchPickUpStatusListSize);
			int index = 0;
			int priority = 1;
			while (index < batchPickUpStatusListSize) {
				pickUpStatusToPriorityMap.put(pickUpStatusList.get(index), priority);
				index++;
				priority++;
			}
		}
		return pickUpStatusToPriorityMap;
	}

	/**
	 * Gets server context path.
	 * 
	 * @param server {@link ServerRegistry}
	 * @return {@link String}
	 */
	public static String getServerContextPath(final ServerRegistry server) {
		String serverContextPath;
		if (null == server) {
			serverContextPath = null;
		} else {
			serverContextPath = EphesoftStringUtil.concatenate(server.getIpAddress(), ICommonConstants.COLON, server.getPort(),
					server.getAppContext());
		}
		return serverContextPath;
	}

	/**
	 * Gets server registry.
	 * 
	 * @param serverContextPath {@link String}
	 * @return {@link ServerRegistry}
	 */
	public static ServerRegistry getServerRegistry(final String serverContextPath) {
		ServerRegistry serverRegistry = null;
		if (!EphesoftStringUtil.isNullOrEmpty(serverContextPath)) {
			String[] parts1 = serverContextPath.split(ICommonConstants.COLON);
			if (null != parts1 && parts1.length == 2) {
				final String IPAddress = parts1[0];
				String portAndContext = parts1[1];
				String[] parts2 = portAndContext.split(ICommonConstants.FORWARD_SLASH);
				if (null != parts2 && parts2.length == 2) {
					final String port = parts2[0];
					final String context = EphesoftStringUtil.concatenate(ICommonConstants.FORWARD_SLASH, parts2[1]);
					serverRegistry = EphesoftContext.getServerRegistry(IPAddress, port, context);
				}
			}
		}
		return serverRegistry;
	}

	/**
	 * Checks whether batch instance status is a pause state or not.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @return boolean
	 */
	public static boolean checkPauseStatus(final BatchInstance batchInstance) {
		boolean isValid = false;
		BatchInstanceStatus batchStatus = batchInstance.getStatus();
		if (null != batchStatus) {
			List<BatchInstanceStatus> list = WorkflowUtil.getPauseStateStatusList();
			if (CollectionUtils.isNotEmpty(list)) {
				for (final BatchInstanceStatus aPickUpStatus : list) {
					if (batchStatus == aPickUpStatus) {
						isValid = true;
						break;
					}
				}
			}
		}
		return isValid;
	}
}
