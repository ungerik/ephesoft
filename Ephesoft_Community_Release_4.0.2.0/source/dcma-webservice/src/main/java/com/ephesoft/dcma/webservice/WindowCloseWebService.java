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

package com.ephesoft.dcma.webservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.ServiceType;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ManualStepHistoryService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * This class will hold Web Service methods that will be called on window close event.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see ServiceType
 */

@Controller
@RequestMapping("/closeWindow")
public class WindowCloseWebService {

	/**
	 * Object to access batch instance related methods.
	 * 
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Object to access manual step history service related methods.
	 */
	@Autowired
	private ManualStepHistoryService manualStepHistoryServiceImpl;

	/**
	 * Initializing logger {@link Logger}.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(WindowCloseWebService.class);

	/**
	 * Error message String
	 */
	private static final String COULD_NOT_CLEAR_CURRENT_USER_ERROR_MSG = "Could not clear current user for batch id: ";
	private static final String MODULE_ID_FOR_REVIEW = "5";
	private static final String MODULE_ID_FOR_VALIDATE = "6";

	/**
	 * To represent Empty String
	 */
	private static final String EMPTY_STRING = "";

	/**
	 * To represent UserName attribute key in session object
	 */
	private static final String USER_NAME_ATTRIBUTE_KEY = "userName";

	@RequestMapping(value = "/BatchClassIdentifier/{batchClassIdentifier}", method = RequestMethod.POST)
	public void onRVWindowClose(@PathVariable("batchClassIdentifier") final String batchClassIdentifier,
			final HttpServletResponse resp, final HttpServletRequest request) {
		LOGGER.debug("Executing onRVWindowClose web service");
		final String userName = getUserName(request);
		if (EphesoftStringUtil.isNullOrEmpty(batchClassIdentifier) || EphesoftStringUtil.isNullOrEmpty(userName)) {
			LOGGER.error(" Either batch intance indentifier or userName is null. ");
		} else {
			LOGGER.info("Clearing owner lock and updating time for Batch Instance: ", batchClassIdentifier, " userName: ", userName);
			this.cleanUpCurrentBatch(batchClassIdentifier, userName);
			this.updateEndTimeAndCalculateDuration(batchClassIdentifier, userName);
		}
	}

	/**
	 * API to clear owner value for the Batch instance identified by input variable batchIdentifier. The owner value will be cleared
	 * only when current owner is same as the owner identified by input variable userName
	 * 
	 * @param batchIdentifier {@link String}
	 * @param userName {@link String}
	 */
	private void cleanUpCurrentBatch(final String batchIdentifier, final String userName) {
		LOGGER.debug("Cleaning up current owner from batch Instance.");

		final BatchInstance batchInstance = this.batchInstanceService.getBatchInstanceByIdentifier(batchIdentifier);
		if (batchInstance != null) {

			final String currentUser = batchInstance.getCurrentUser();
			if (currentUser != null && currentUser.equalsIgnoreCase(userName)) {
				LOGGER.info("Unlocking batch instance for modification to other user.");
				this.batchInstanceService.unlockCurrentBatchInstance(batchIdentifier);
			}
		} else {
			LOGGER.error(EphesoftStringUtil.concatenate(COULD_NOT_CLEAR_CURRENT_USER_ERROR_MSG, batchIdentifier));
		}
	}

	/**
	 * API to update the end time of review or validation of batchInstance identified by batchInstanceId.
	 * 
	 * @param batchInstanceId {@link String}
	 * @param userName {@link String}
	 */
	private void updateEndTimeAndCalculateDuration(final String batchInstanceId, final String userName) {
		LOGGER.debug("Inside updateEndTimeAndCalculateDuration of WindowCloseWebService.");
		final BatchInstance batchInstance = this.batchInstanceService.getBatchInstanceByIdentifier(batchInstanceId);
		if (batchInstance != null) {
			BatchInstanceStatus batchInstanceStatus = batchInstance.getStatus();

			// Checking the batch instance status, which may be 'RUNNING' or 'READY' after review or validation has been done
			// and based on the executed modules setting the batch instance status as READY_FOR_REVIEW' or 'READY_FOR_VALIDATION'
			// to store it in 'hist_manual_steps_in_workflow' table
			if (batchInstanceStatus.equals(BatchInstanceStatus.RUNNING) || batchInstanceStatus.equals(BatchInstanceStatus.READY)) {
				final String executedModules = batchInstance.getExecutedModules();
				if (executedModules.contains(MODULE_ID_FOR_REVIEW) && !executedModules.contains(MODULE_ID_FOR_VALIDATE)) {
					batchInstanceStatus = BatchInstanceStatus.READY_FOR_REVIEW;
				} else {
					batchInstanceStatus = BatchInstanceStatus.READY_FOR_VALIDATION;
				}
			}
			final ManualStepHistoryService manualStepHistoryService = this.manualStepHistoryServiceImpl;
			manualStepHistoryService.updateEndTimeAndCalculateDuration(batchInstanceId, batchInstanceStatus.name(), userName);
		} else {
			LOGGER.error("Not able to update End Time. BatchInstance  is null");
		}
	}

	/**
	 * API to get user name of currently logged user from request attribute
	 * 
	 * @param request {@link HttpServletRequest}
	 * @return {@link String}
	 */
	private String getUserName(HttpServletRequest request) {

		String userName = null;
		LOGGER.debug("Getting session attribute for the attribute key: ", USER_NAME_ATTRIBUTE_KEY);

		if (isSessionAttribute(request, USER_NAME_ATTRIBUTE_KEY)) {
			try {
				userName = (String) request.getSession().getAttribute(USER_NAME_ATTRIBUTE_KEY);
			} catch (ClassCastException classCastException) {
				LOGGER.error(classCastException, "Type of attribute fetched from session is wrong. Attribute is: ",
						USER_NAME_ATTRIBUTE_KEY);
			}
		}
		LOGGER.debug("Session attribute for the attribute key ", USER_NAME_ATTRIBUTE_KEY, " is: ", userName);
		return (null == userName) ? WindowCloseWebService.EMPTY_STRING : userName;
	}

	/**
	 * API to check whether some attribute is present inside session object or not.
	 * 
	 * @param httpServletRequest
	 * @param attributeKey
	 * @return boolean
	 */
	private static boolean isSessionAttribute(final HttpServletRequest httpServletRequest, final String attributeKey) {
		LOGGER.debug("Checking for session attribute existence.");
		boolean isAvailable = false;
		if (null != httpServletRequest && !EphesoftStringUtil.isNullOrEmpty(attributeKey)) {
			isAvailable = (null == httpServletRequest.getSession().getAttribute(attributeKey)) ? false : true;
		}
		LOGGER.debug("Status of attribute ", attributeKey, " is: ", isAvailable);
		return isAvailable;
	}

}
