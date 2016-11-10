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

package com.ephesoft.dcma.workflows.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.service.WorkflowService;

/**
 * This class represents the aspects for processing for exception handling for batches for every service.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Aspect
public class DCMAExceptionAspect {

	/**
	 * {@link EphesoftLogger} Instance of logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(DCMAExceptionAspect.class);

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
	 * Executes tasks for a batch that received an exception while its execution in a service.
	 * 
	 * @param joinPoint {@link JoinPoint}
	 * @param dcmaException {@link DCMAException}
	 * @throws Exception
	 */
	@AfterThrowing(pointcut = "execution(* com.ephesoft.dcma.*.service.*.*(..))", throwing = "dcmaException")
	public void afterThrowing(final JoinPoint joinPoint, final DCMAException dcmaException) throws Exception {
		LOGGER.debug("In method to catch the exception in a service.");
		Object[] args = joinPoint.getArgs();
		if (args[0] instanceof BatchInstanceID) {
			final String batchInstanceIdentifier = args[0].toString();
			final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(batchInstanceIdentifier);
			if (null == batchInstance) {
				throw new Exception("Batch Instance cannot be null to handle this aspect.");
			}
			if (batchInstance.getStatus() != BatchInstanceStatus.ERROR) {
				LOGGER.info("Going to handle batch: ", batchInstanceIdentifier, " for sending it to Error state.");
				workflowService.handleErrorBatch(batchInstance, dcmaException, dcmaException.getMessage());
			}
		}
	}
}
