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

package com.ephesoft.dcma.da.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.ManualStepHistoryDao;
import com.ephesoft.dcma.da.domain.ManualStepHistoryInWorkflow;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * Implementation class for data access to manual step history table in ephesoft database.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 14-Oct-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Repository
public class ManualStepHistoryDaoImpl extends HibernateDao<ManualStepHistoryInWorkflow> implements ManualStepHistoryDao {

	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ManualStepHistoryDaoImpl.class);

	@Override
	public void updateManualStepHistory(final ManualStepHistoryInWorkflow manualStepHistoryInWorkflow,
			final ManualStepHistoryInWorkflow existingManualStepHistoryInWorkflow) {
		LOGGER.trace("Inside method to update Manual Step History at data access layer.");
		if (null == manualStepHistoryInWorkflow) {
			LOGGER.error("Update maunual history content is null.");
		} else {
			if (existingManualStepHistoryInWorkflow != null) {
				LOGGER.debug("Existing Start time is : ", existingManualStepHistoryInWorkflow.getStartTime(),
						". Existing End time is : ", existingManualStepHistoryInWorkflow.getEndTime(), ".");
				final Date newStartTime = manualStepHistoryInWorkflow.getStartTime();
				final Date newEndtime = manualStepHistoryInWorkflow.getEndTime();
				existingManualStepHistoryInWorkflow.setStartTime(newStartTime);
				existingManualStepHistoryInWorkflow.setEndTime(newEndtime);
				LOGGER.debug("Updated Start time is : ", newStartTime, ". Updated End time is : ", newEndtime, ".");
				long oldDuration = existingManualStepHistoryInWorkflow.getDuration();
				long nextDuration = manualStepHistoryInWorkflow.getDuration();
				long updatedDuration;
				try {
					updatedDuration = nextDuration + oldDuration;
				} catch (NumberFormatException numberFormatException) {
					updatedDuration = Long.MAX_VALUE;
					LOGGER.error("Total review/validate duration is too large to save.Only saving the maximum allowable time");
				}
				LOGGER.debug("Old duration was : ", oldDuration, ". Next duration for addition is : ", nextDuration,
						". Updated Duration is : ", updatedDuration, ".");
				existingManualStepHistoryInWorkflow.setDuration(updatedDuration);
				saveOrUpdate(existingManualStepHistoryInWorkflow);
				LOGGER.debug("Successfully created new isntance for batch :",
						existingManualStepHistoryInWorkflow.getBatchInstanceId(), " with status ",
						existingManualStepHistoryInWorkflow.getBatchInstanceStatus(), " for user",
						existingManualStepHistoryInWorkflow.getUserName());
			} else {
				create(manualStepHistoryInWorkflow);
				LOGGER.debug("Successfully created new isntance for batch :", manualStepHistoryInWorkflow.getBatchInstanceId(),
						" with status ", manualStepHistoryInWorkflow.getBatchInstanceStatus(), " for user",
						manualStepHistoryInWorkflow.getUserName());
			}
		}
		LOGGER.trace("Exiting method to update Manual Step History at data access layer.");
	}

	@Override
	public ManualStepHistoryInWorkflow getManualStepHistory(final String batchInstanceId, final String batchInstanceStatus,
			final String userName) {
		LOGGER.trace("Inside method to get ManualStepHistory for batch whose details are in update progress at data access layer.");
		LOGGER.debug("Getting existing (in details update progress) manual history object for ", batchInstanceId, "by user ",
				userName, "with status", batchInstanceStatus);
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq("batchInstanceId", batchInstanceId));
		criteria.add(Restrictions.eq("batchInstanceStatus", batchInstanceStatus));
		criteria.add(Restrictions.eq("userName", userName));
		criteria.add(Restrictions.eq("endTime", new Date(0L)));
		ManualStepHistoryInWorkflow manualStepHistoryInWorkflow = null;
		List<ManualStepHistoryInWorkflow> resultList = find(criteria);
		if (resultList != null && resultList.size() > 0) {
			manualStepHistoryInWorkflow = resultList.get(0);
		}
		LOGGER.trace("Exiting method to get ManualStepHistory for batch whose details are in update progress at data access layer.");
		return manualStepHistoryInWorkflow;
	}

	@Override
	public ManualStepHistoryInWorkflow getExistingManualStepHistory(final String batchInstanceId, final String batchInstanceStatus,
			final String userName) {
		LOGGER.trace("Inside method to get manual step history for the batch in execution of a manual Review or Validate step at data access layer.");
		LOGGER.debug("Getting existing manual history object for ", batchInstanceId, "by user ", userName, "with status",
				batchInstanceStatus);
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq("batchInstanceId", batchInstanceId));
		criteria.add(Restrictions.eq("batchInstanceStatus", batchInstanceStatus));
		criteria.add(Restrictions.eq("userName", userName));
		ManualStepHistoryInWorkflow manualStepHistoryInWorkflow = null;
		List<ManualStepHistoryInWorkflow> resultList = find(criteria);
		if (resultList != null && resultList.size() > 0) {
			manualStepHistoryInWorkflow = resultList.get(0);
		}
		LOGGER.trace("Exiting method to get manual step history for the batch in execution of a manual Review or Validate step at data access layer.");
		return manualStepHistoryInWorkflow;
	}

}
