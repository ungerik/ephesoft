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

import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.BatchInstanceErrorDetails;
import com.ephesoft.dcma.da.service.BatchInstanceErrorDetailsService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.logger.EphesoftFileAppender;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.util.logger.InstanceType;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;
import com.ephesoft.dcma.workflows.service.WorkflowService;
import com.ephesoft.dcma.workflows.service.engine.EngineService;

/**
 * This class represents a service for batches which fail due to some exception.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class ErrorBatchServiceRunnable implements Runnable {

	/**
	 * {@link EngineService} Instance of engine service.
	 */
	@Autowired
	private EngineService engineService;

	/**
	 * {@link BatchInstanceService} Instance of batch instance service.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * {@link BatchSchemaService} Instance of batch schema service.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * {@link WorkflowService} Instance of workflow service.
	 */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * {@link BatchInstanceErrorDetailsService} Instance of batch instance error details service.
	 */
	@Autowired
	private BatchInstanceErrorDetailsService batchInstanceErrorDetailsService;

	/**
	 * {@link BatchInstance} Batch instance that went into error.
	 */
	private BatchInstance batchInstance;;

	/**
	 * {@link String} Message from exception.
	 */
	private String exceptionMessage;

	/**
	 * {@link String} Exception instance.
	 */
	private Exception exception;

	/**
	 * {@link EphesoftLogger} Instance of Logger.
	 */
	private EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ErrorBatchServiceRunnable.class);

	/**
	 * Constructor that sets batch instance and message from exception.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param exceptionMessage {@link String}
	 */
	public ErrorBatchServiceRunnable(final BatchInstance batchInstance, final String exceptionMessage, final Exception exception) {
		this.batchInstance = batchInstance;
		this.exceptionMessage = exceptionMessage;
		this.exception = exception;
	}

	/**
	 * Default Constructor
	 */
	public ErrorBatchServiceRunnable() {
	}

	@Override
	public void run() {
		String batchInstanceIdentifier = batchInstance.getIdentifier();
		LOGGER.error(InstanceType.BATCHINSTANCE, batchInstanceIdentifier, exceptionMessage);

		// It sets error status on batch XML
		updateBatchStatusInBatchXML(batchInstanceIdentifier);

		// Performs enhanced logging if required.
		performEnhancedErrorLogging(batchInstanceIdentifier);

		// Sends batch to error state.
		engineService.sendBatchToErrorState(batchInstanceIdentifier);

		// Delete batch instance resources.
		engineService.deleteProcessInstanceByBatchInstance(batchInstance, false);

		try {
			if (null == exception) {
				exception = new DCMAException(exceptionMessage);
			}
			workflowService.mailOnError(batchInstance, exception);
		} catch (DCMAApplicationException dcmaApplicationException) {
			LOGGER.error("Error while sending mail on failure of batch: ", batchInstanceIdentifier);
		}

	}

	/**
	 * Performs enhanced logging by storing logs in error details table.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 */
	private void performEnhancedErrorLogging(final String batchInstanceIdentifier) {

		// Check if the Batch Instance specific logging switch is ON.
		if (WorkflowConstants.ON.equalsIgnoreCase(EphesoftFileAppender.getEnhancedLoggingSwitch())) {
			BatchInstanceErrorDetails batchInstanceErrorDetails = batchInstanceErrorDetailsService
					.getBatchInstanceErrorDetailByIdentifier(batchInstanceIdentifier);
			if (null == batchInstanceErrorDetails) {
				batchInstanceErrorDetails = new BatchInstanceErrorDetails();
				batchInstanceErrorDetails.setIdentifier(batchInstanceIdentifier);
			}
			batchInstanceErrorDetails.setErrorMessage(exceptionMessage);
			batchInstanceErrorDetailsService.saveOrUpdate(batchInstanceErrorDetails);
			LOGGER.debug("Batch Instance Error Details updated for batch: ", batchInstanceIdentifier);
		}
	}

	/**
	 * Updates ERROR status in batch XML.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 */
	private void updateBatchStatusInBatchXML(final String batchInstanceIdentifier) {
		// try {
		//
		// // update the batch instance status to ERROR in batch XML file
		// final Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
		// batch.setBatchStatus(BatchStatus.ERROR);
		// batchSchemaService.updateBatch(batch);
		// } catch (Exception exception) {
		// LOGGER.error(exception, InstanceType.BATCHINSTANCE, batchInstanceIdentifier,
		// "Error while updating the batch xml status to ERROR status. ", exception.getMessage());
		// }
	}

	public void setBatchInstance(BatchInstance batchInstance) {
		this.batchInstance = batchInstance;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

}
