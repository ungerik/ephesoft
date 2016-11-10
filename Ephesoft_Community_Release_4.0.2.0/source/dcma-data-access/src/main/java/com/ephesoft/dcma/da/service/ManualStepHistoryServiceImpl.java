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

package com.ephesoft.dcma.da.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.da.dao.ManualStepHistoryDao;
import com.ephesoft.dcma.da.domain.ManualStepHistoryInWorkflow;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * Implementation of service for manual step history records in database.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 14-Oct-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Service
public class ManualStepHistoryServiceImpl implements ManualStepHistoryService {

	/**
	 * LOGGER to print the Logging information.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ModuleConfigServiceImpl.class);

	/**
	 * Data access instance for Manual Step history.
	 */
	@Autowired
	private ManualStepHistoryDao manualStepHistoryDao;

	@Override
	@Transactional
	public void updateManualStepHistory(final ManualStepHistoryInWorkflow newManualStepHistory) {
		LOGGER.trace("Inside method to update manual step history, called at start of user work on RV.");
		if (newManualStepHistory == null) {
			LOGGER.error("ManualStepHistory object is null");
		} else {
			String batchInstanceId = newManualStepHistory.getBatchInstanceId();
			String userName = newManualStepHistory.getUserName();
			String batchStatus = newManualStepHistory.getBatchInstanceStatus();
			LOGGER.debug("Updating manual history object for ", batchInstanceId, "by user ", userName, "with status", batchStatus);
			ManualStepHistoryInWorkflow existingManualStepHistory = manualStepHistoryDao.getExistingManualStepHistory(batchInstanceId,
					batchStatus, userName);
			manualStepHistoryDao.updateManualStepHistory(newManualStepHistory, existingManualStepHistory);
		}
		LOGGER.trace("Exiting method to update manual step history.");
	}

	@Override
	@Transactional
	public void updateEndTimeAndCalculateDuration(final String batchInstanceId, final String batchInstanceStatus, final String userName) {
		LOGGER.trace("Inside method to update End Time and Update Duration");
		ManualStepHistoryInWorkflow existingManualStepHistoryInWorkflow = manualStepHistoryDao.getManualStepHistory(batchInstanceId,
				batchInstanceStatus, userName);
		if (null == existingManualStepHistoryInWorkflow) {
			LOGGER.warn("Existing manual history object for ", batchInstanceId, "by user ", userName, "with status",
					batchInstanceStatus, "is null.");
		} else {
			Date startTime = existingManualStepHistoryInWorkflow.getStartTime();
			Date endTime = new Date();
			ManualStepHistoryInWorkflow manualStepHistoryInWorkflow = new ManualStepHistoryInWorkflow();
			manualStepHistoryInWorkflow.setEndTime(endTime);
			manualStepHistoryInWorkflow.setStartTime(startTime);
			long updatedDuration;
			try {
				updatedDuration = endTime.getTime() - startTime.getTime();
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error("The review/validate is too big to save..so saving the largest allowable value for the batch instance:",
						batchInstanceId);
				updatedDuration = Long.MAX_VALUE;
			}
			LOGGER.debug("Current session's start time is :", startTime.toString(), " end time is : ", endTime.toString(),
					"Updated duration for current session is: ", updatedDuration);
			manualStepHistoryInWorkflow.setDuration(updatedDuration);
			manualStepHistoryDao.updateManualStepHistory(manualStepHistoryInWorkflow, existingManualStepHistoryInWorkflow);
			LOGGER.info("End time and duration has been updated successfully for batch instance:", batchInstanceId);
		}
		LOGGER.trace("Exiting method to update End Time and Update Duration");
	}
}
