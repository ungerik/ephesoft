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

package com.ephesoft.dcma.workflows.client;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchInstance;

/**
 * This class is used to initializing the web services. It is used to preparing and transferring information from one instance to
 * another instance.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.client.EphesoftWebServiceClientImpl
 * 
 */
public interface EphesoftWebServiceClient {

	/**
	 * API to prepare and restart batch instance.
	 * 
	 * @param targetServerURL {@link String}
	 * @param batchInstanceId {@link String}
	 * @param sourceServerURL {@link String}
	 * @param folderPath {@link String}
	 * @param batchClassId {@link String}
	 * @param moduleName {@link String}
	 * @param newBatchInstanceIdentifier {@link String}
	 * @param batchName {@link String}
	 * @return batchInstanceIdentifier {@link String}
	 */
	String prepareDataAndRestartBatch(String targetServerURL, String batchInstanceId, String sourceServerURL, String folderPath,
			String batchClassId, String moduleName, String newBatchInstanceIdentifier, String batchName);

	/**
	 * API to return batch instance status of remote batch.
	 * 
	 * @param remoteURL {@link String}
	 * @param remoteBatchInstanceIdentifier {@link String}
	 * @return batchInstanceStatus {@link BatchInstanceStatus}
	 */
	BatchInstanceStatus getBatchStatusofRemoteBatch(String remoteURL, String remoteBatchInstanceIdentifier);

	/**
	 * API to return the remote batch instance of remotely executing batch.
	 * 
	 * @param previousRemoteURL {@link String}
	 * @param previousRemoteBatchInstanceIdentifier {@link String}
	 * @return batchInstance {@link BatchInstance}
	 */
	BatchInstance getRemoteBatchInstanceOfRemoteBatch(String previousRemoteURL, String previousRemoteBatchInstanceIdentifier);

	/**
	 * API to update the information of the remote batch instance.
	 * 
	 * @param targetURL {@link String}
	 * @param targetBatchInstanceIdentifier {@link String}
	 * @param previousURL {@link String}
	 * @param previousBatchInstanceIdentifier {@link String}
	 * @param nextURL {@link String}
	 * @param nextBatchInstanceIdentifier {@link String}
	 * @param isRemote boolean
	 * @param hostURL {@link String}
	 */
	void updateInfoOfRemoteBatchInstance(String targetURL, String targetBatchInstanceIdentifier, String previousURL,
			String previousBatchInstanceIdentifier, String nextURL, String nextBatchInstanceIdentifier, boolean isRemote,
			String hostURL);

	/**
	 * API for restarting batch instance from defined module name.
	 * 
	 * @param targetURL {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @param moduleName {@link String}
	 * @param throwException {@link String}
	 * @return boolean
	 * @throws DCMAApplicationException
	 */
	Boolean restartBatchInstance(String targetURL, String batchInstanceIdentifier, String moduleName, String throwException)
			throws DCMAApplicationException;

	/**
	 * API for deleting batch instance for provided batch instance identifier.
	 * 
	 * @param targetURL {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @return boolean
	 * @throws DCMAApplicationException
	 */
	Boolean deleteBatchInstance(String targetURL, String batchInstanceIdentifier) throws DCMAApplicationException;

	void pullingRemoteBatchStatus();
}
