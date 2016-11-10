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

import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchInstance;

/**
 * This interface represents the service for restart of a batch.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version 1.0 $LastChangedDate: 2015-04-14 12:42:19 +0530 (Tue, 14 Apr 2015) $ <br/>
 *          $LastChangedRevision: 21594 $ <br/>
 */
public interface RestartBatchService {

	/**
	 * Restarts a batch instance.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param moduleName {@link String}
	 * @param throwException boolean
	 * @param resumeBatchInstance boolean
	 * @return boolean
	 * @throws DCMAApplicationException {@link DCMAApplicationException}
	 */
	boolean restartBatchInstance(String batchInstanceIdentifier, String moduleName, boolean throwException, boolean resumeBatchInstance)
			throws DCMAApplicationException;

	/**
	 * Gets Last executed module of the batch instance.
	 * 
	 * @param batchInstance {@link BatchInstance}
	 * @param batchClass {@link BatchClass}
	 * @return {@link String}
	 */
	String getBatchLastExecutedModule(BatchInstance batchInstance);

	/**
	 * Restarts a batch instance without cleaning of held resources by batch instance.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param moduleName {@link String}
	 * @return boolean
	 */
	boolean restartBatchInstanceWithoutCleaning(String batchInstanceIdentifier, String moduleName);
}
