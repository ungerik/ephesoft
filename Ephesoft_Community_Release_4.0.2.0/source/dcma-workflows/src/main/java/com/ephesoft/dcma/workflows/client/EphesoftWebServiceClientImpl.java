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

import java.io.IOException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.ephesoft.dcma.cleanup.service.CleanupService;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.RemoteBatchInstance;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.constant.WebServiceConstants;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;

/**
 * This class is used to initializing the web services. It is used to preparing and transferring information from one instance to
 * another instance.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.workflow.client.EphesoftWebServiceClient
 * 
 */
public class EphesoftWebServiceClientImpl implements EphesoftWebServiceClient {

	/**
	 * Initializing restTemplate {@link RestTemplate}.
	 */
	private RestTemplate restTemplate;

	/**
	 * Initializing logger {@link Logger}.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(EphesoftWebServiceClientImpl.class);

	/**
	 * Initializing batchInstanceService {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Initializing cleanupService {@link CleanupService}.
	 */
	@Autowired
	private CleanupService cleanupService;

	/**
	 * 
	 * @param restTemplate {@link RestTemplate} 
	 */
	public final void setRestTemplate(final RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

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
	@Override
	public String prepareDataAndRestartBatch(final String targetServerURL, final String batchInstanceId, final String sourceServerURL,
			final String folderPath, final String batchClassId, final String moduleName, final String newBatchInstanceIdentifier,
			final String batchName) {
		String string = WorkflowConstants.NOT_APPLICABLE;
		boolean isValid = true;
		LOGGER.trace("Inside prepareDataAndRestartBatch......");
		if (EphesoftStringUtil.isNullOrEmpty(targetServerURL)) {
			isValid = false;
			LOGGER.error("targetServerURL is null");
		}
		if (EphesoftStringUtil.isNullOrEmpty(batchInstanceId)) {
			isValid = false;
			LOGGER.error("batchInstanceId is null");
		}
		if (EphesoftStringUtil.isNullOrEmpty(sourceServerURL)) {
			isValid = false;
			LOGGER.error("Source Server URL is null.");
		}
		if (EphesoftStringUtil.isNullOrEmpty(folderPath)) {
			isValid = false;
			LOGGER.error("Folder Path is null");
		}
		if (EphesoftStringUtil.isNullOrEmpty(batchClassId)) {
			isValid = false;
			LOGGER.error("batchClassId is null");
		}
		if (EphesoftStringUtil.isNullOrEmpty(moduleName)) {
			isValid = false;
			LOGGER.error("moduleName is null");
		}
		if (EphesoftStringUtil.isNullOrEmpty(newBatchInstanceIdentifier)) {
			isValid = false;
			LOGGER.error("newBatchInstanceIdentifier is null");
		}
		if (EphesoftStringUtil.isNullOrEmpty(batchName)) {
			isValid = false;
			LOGGER.error("batchName is null");
		}
		if (isValid) {
			LOGGER.info("Starting preparing data and restart batch");
			String sourceFolderPath = folderPath;
			LOGGER.debug("Source folder path is :", sourceFolderPath);
			String sourceServerURLPath = sourceServerURL;
			LOGGER.debug("Source server URL path is :", sourceServerURL);
			sourceFolderPath = sourceFolderPath.replace(WorkflowConstants.BACK_SLASH, WorkflowConstants.CARET);
			LOGGER.debug("Updated source folder path is :", sourceFolderPath);
			sourceServerURLPath = sourceServerURLPath.replace(WorkflowConstants.BACK_SLASH, WorkflowConstants.CARET).replace(
					WorkflowConstants.BACK_SLASH, WorkflowConstants.CARET);
			LOGGER.debug("Updated source server URL path is :", sourceServerURLPath);
			Object obj = hitWebServiceURL(targetServerURL, batchInstanceId, sourceServerURLPath, sourceFolderPath, batchClassId,
					moduleName, newBatchInstanceIdentifier, batchName);
			if (obj != null) {
				string = (String) obj;
			}
			LOGGER.debug("New Batch Instance Identifier: ", string);
		}
		return string;
	}

	/**
	 * API to return batch instance status of remote batch.
	 * 
	 * @param remoteURL {@link String}
	 * @param remoteBatchInstanceIdentifier {@link String}
	 * @return batchInstanceStatus {@link BatchInstanceStatus}
	 */
	@Override
	public BatchInstanceStatus getBatchStatusofRemoteBatch(final String remoteURL, final String remoteBatchInstanceIdentifier) {
		boolean isValid = true;
		BatchInstanceStatus batchInstanceStatus = null;
		String batchInstanceStatusResponse = null;
		if (EphesoftStringUtil.isNullOrEmpty(remoteURL) || EphesoftStringUtil.isNullOrEmpty(remoteBatchInstanceIdentifier)) {
			isValid = false;
		}
		if (isValid) {
			HttpClient client = new HttpClient();

			StringBuilder urlStringBuilder = new StringBuilder(remoteURL);
			urlStringBuilder.append(WebServiceConstants.REMOTE_BATCH_INSTANCE_IDENTIFIER);
			urlStringBuilder.append(remoteBatchInstanceIdentifier);

			// URL path to be hit for getting the batch class list having
			// accessed by the role specified.
			String url = urlStringBuilder.toString();
			// response = (String)restTemplate.getForObject(url, String.class);
			GetMethod getMethod = new GetMethod(url);

			LOGGER.debug("URL for calling remote server : ", url);
			int statusCode;
			try {
				statusCode = client.executeMethod(getMethod);

				LOGGER.debug("Status code for the Url: ", statusCode);
				if (statusCode == WebServiceConstants.STATUS_CODE_200) {
					batchInstanceStatusResponse = getMethod.getResponseBodyAsString();
					LOGGER.debug("Response found: ", batchInstanceStatusResponse);
				} else if (statusCode == WebServiceConstants.STATUS_CODE_403) {
					LOGGER.error("Invalid URL.");
					batchInstanceStatusResponse = WorkflowConstants.NOT_APPLICABLE;
				} else {
					batchInstanceStatusResponse = WorkflowConstants.NOT_APPLICABLE;
					LOGGER.error("Unable to load URL.");
				}
			} catch (HttpException e) {
				LOGGER.error(e, "Http Exception while calling remote server with Url: ", url);
			} catch (IOException e) {
				LOGGER.error(e, "I/O Exception while calling remote server with Url: ", url);
			} finally {
				if (null != getMethod) {
					getMethod.releaseConnection();
				}
			}
			try {
				batchInstanceStatus = BatchInstanceStatus.valueOf(batchInstanceStatusResponse.replace("\"", ""));
			} catch (Exception e) {
				LOGGER.error(e, "Exception while parsing the remote batch instance status for remote batchID : ",
						remoteBatchInstanceIdentifier, " and recieved status is :", batchInstanceStatusResponse);
			}
		}
		return batchInstanceStatus;
	}

	/**
	 * API to return the remote batch instance of remotely executing batch.
	 * 
	 * @param previousRemoteURL {@link String}
	 * @param previousRemoteBatchInstanceIdentifier {@link String}
	 * @return batchInstance {@link BatchInstance}
	 */
	@Override
	public BatchInstance getRemoteBatchInstanceOfRemoteBatch(final String previousRemoteURL,
			final String previousRemoteBatchInstanceIdentifier) {
		LOGGER.trace("=================Get Remote BatchInstance Of Remote Batch======================");
		boolean isValid = true;
		BatchInstance batchInstance = null;
		if (previousRemoteURL == null || previousRemoteBatchInstanceIdentifier == null) {
			isValid = false;
		}
		if (isValid) {
			String previousRemoteURLName = previousRemoteURL;
			LOGGER.debug("Previous Remote URL is : ", previousRemoteURLName);
			LOGGER.debug("Previous Remote Batch Identifier is : ", previousRemoteBatchInstanceIdentifier);
			HttpClient client = new HttpClient();
			StringBuilder urlStringBuilder = new StringBuilder(previousRemoteURLName);
			urlStringBuilder.append("/previousRemoteBatchInstanceIdentifier/");
			urlStringBuilder.append(previousRemoteBatchInstanceIdentifier);

			// URL path to be hit for getting the batch class list having
			// accessed by the role specified.
			String url = urlStringBuilder.toString();
			// response = (String)restTemplate.getForObject(url, String.class);
			GetMethod getMethod = new GetMethod(url);

			LOGGER.debug("URL for calling remote server : ", url);
			int statusCode;
			try {
				statusCode = client.executeMethod(getMethod);
				LOGGER.debug("Status code for the Url: ", statusCode);
				if (statusCode == WebServiceConstants.STATUS_CODE_200) {
					LOGGER.debug("Response found: ", getMethod.getResponseBodyAsString());
				} else if (statusCode == WebServiceConstants.STATUS_CODE_403) {
					LOGGER.error("Invalid URL.");
				} else {
					LOGGER.error("Unable to load URL.");
				}
			} catch (HttpException e) {
				LOGGER.error(e, "Http Exception while calling remote server with Url: ", url);
			} catch (IOException e) {
				LOGGER.error(e, "I/O Exception while calling remote server with Url: ", url);
			} finally {
				if (getMethod != null) {
					getMethod.releaseConnection();
				}
			}

		}
		return batchInstance;
	}

	/**
	 * 
	 */
	public void pullingRemoteBatchStatus() {
		LOGGER.trace("Pulling batch status");
		List<BatchInstance> batchInstances = batchInstanceService.getAllUnfinshedRemotelyExecutedBatchInstance();
		if (CollectionUtils.isNotEmpty(batchInstances)) {
			for (BatchInstance batchInstance : batchInstances) {
				if (null != batchInstance && null != batchInstance.getRemoteBatchInstance()) {
					BatchInstanceStatus status = null;
					RemoteBatchInstance remoteBatchInstance = batchInstance.getRemoteBatchInstance();
					String remoteBatchInstanceURL = remoteBatchInstance.getRemoteURL();
					String remoteBatchInstanceID = remoteBatchInstance.getRemoteBatchInstanceIdentifier();
					try {
						status = getBatchStatusofRemoteBatch(remoteBatchInstanceURL, remoteBatchInstanceID);
					} catch (Exception e) {
						LOGGER.error(e, "Invalid connection for batch instance is : ", batchInstance.getIdentifier());
					}
					if (null == status) {
						LOGGER.error("Could not get the status for the batch instance.");
					} else {
						LOGGER.info("Updated status for batch instance :", batchInstance, " status :", status.toString());
						batchInstance.setStatus(status);
						batchInstanceService.updateBatchInstance(batchInstance);
					}
					if (null != status && status.equals(BatchInstanceStatus.FINISHED)) {
						try {
							cleanupService.cleanup(batchInstance.getBatchInstanceID(), ICommonConstants.EMPTY_STRING);
						} catch (DCMAException e) {
							LOGGER.error(e, "Error in cleaning up files :", e.getMessage());
						}
					}
				}
			}
		}
	}

	/**
	 * API to update the information of the remote batch instance
	 * 
	 * @param targetURL {@link String}
	 * @param targetBatchInstanceIdentifier {@link String}
	 * @param previousURL {@link String}
	 * @param previousBatchInstanceIdentifier {@link String}
	 * @param nextURL {@link String}
	 * @param nextBatchInstanceIdentifier {@link String}
	 * @param isRemote
	 * @param hostURL
	 */
	@Override
	public void updateInfoOfRemoteBatchInstance(final String targetURL, final String targetBatchInstanceIdentifier,
			final String previousURL, final String previousBatchInstanceIdentifier, final String nextURL,
			final String nextBatchInstanceIdentifier, final boolean isRemote, final String hostURL) {
		boolean isValid = true;
		if (targetURL == null || targetBatchInstanceIdentifier == null) {
			isValid = false;
		}
		if (isValid) {
			String previousRemoteURLName = previousURL;
			if (previousRemoteURLName != null) {
				previousRemoteURLName = previousRemoteURLName.replace(WorkflowConstants.CARET, WorkflowConstants.FORWARD_SLASH)
						.replace(WorkflowConstants.BACK_SLASH, WorkflowConstants.FORWARD_SLASH);
			}
			String nextRemoteURLName = nextURL;
			if (nextRemoteURLName != null) {
				nextRemoteURLName = nextRemoteURLName.replace(WorkflowConstants.CARET, WorkflowConstants.FORWARD_SLASH).replace(
						WorkflowConstants.BACK_SLASH, WorkflowConstants.FORWARD_SLASH);
			}
			if (!targetURL.equalsIgnoreCase(hostURL)) {
				String previousBatchInstanceIdentifierName = previousBatchInstanceIdentifier;
				String previousURLName = previousURL;
				String nextBatchInstanceIdentifierName = nextBatchInstanceIdentifier;
				String nextURLName = nextURL;
				if (previousBatchInstanceIdentifierName == null) {
					previousBatchInstanceIdentifierName = WorkflowConstants.NOT_APPLICABLE;
				}
				if (previousURLName == null) {
					previousURLName = WorkflowConstants.NOT_APPLICABLE;
				}
				if (nextBatchInstanceIdentifierName == null) {
					nextBatchInstanceIdentifierName = WorkflowConstants.NOT_APPLICABLE;
				}
				if (nextURLName == null) {
					nextURLName = WorkflowConstants.NOT_APPLICABLE;
				}
				restTemplate
						.getForObject(
								targetURL
										+ "/targetBatchInstanceIdentifier/{targetBatchInstanceIdentifier}/previousURL/{previousRemoteURLName}/previousBatchInstanceIdentifier/{previousBatchInstanceIdentifier}/nextURL/{nextURL}/nextBatchInstanceIdentifier/{nextBatchInstanceIdentifier}/isRemote/{isRemote}",
								BatchInstance.class, targetBatchInstanceIdentifier, previousURL, previousBatchInstanceIdentifier,
								nextURL, nextBatchInstanceIdentifier, isRemote);
			} else {
				BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(targetBatchInstanceIdentifier);
				RemoteBatchInstance remoteBatchInstance = batchInstance.getRemoteBatchInstance();
				if (remoteBatchInstance != null) {
					remoteBatchInstance.setPreviousRemoteBatchInstanceIdentifier(previousBatchInstanceIdentifier);
					remoteBatchInstance.setPreviousRemoteURL(previousURL);
					remoteBatchInstance.setRemoteBatchInstanceIdentifier(nextBatchInstanceIdentifier);
					remoteBatchInstance.setRemoteURL(nextURL);
					batchInstance.setRemoteBatchInstance(remoteBatchInstance);
					batchInstanceService.updateBatchInstance(batchInstance);
				}
			}
		}
	}

	/**
	 * API to hit the web service URL.
	 * 
	 * @param targetServerURL
	 * @param batchInstanceId
	 * @param sourceServerURL
	 * @param folderPath
	 * @param batchClassId
	 * @param moduleName
	 * @param newBatchInstanceIdentifier
	 * @param batchName
	 */
	private String hitWebServiceURL(String targetServerURL, String batchInstanceId, String sourceServerURL, String folderPath,
			String batchClassId, String moduleName, String newBatchInstanceIdentifier, String batchName) {
		String response = WorkflowConstants.NOT_APPLICABLE;
		HttpClient client = new HttpClient();

		// URL path to be hit for getting the batch class list having accessed
		// by the role specified.
		String url = EphesoftStringUtil.concatenate(targetServerURL, WebServiceConstants.CONSTANT_BATCH_IDENTIFIER,
				EphesoftStringUtil.getEncodedString(batchInstanceId), WebServiceConstants.CONSTANT_SERVER,
				EphesoftStringUtil.getEncodedString(sourceServerURL), WebServiceConstants.CONSTANT_FOLDER_LOCATION,
				EphesoftStringUtil.getEncodedString(folderPath), WebServiceConstants.CONSTANT_BATCH_CLASS_ID,
				EphesoftStringUtil.getEncodedString(batchClassId), WebServiceConstants.CONSTANT_MODULE_NAME,
				EphesoftStringUtil.getEncodedString(moduleName), WebServiceConstants.CONSTANT_NEW_BATCH_INSTANCE_IDENTIFIER,
				EphesoftStringUtil.getEncodedString(newBatchInstanceIdentifier), WebServiceConstants.CONSTANT_BATCH_NAME,
				EphesoftStringUtil.getEncodedString(batchName));
		GetMethod getMethod = new GetMethod(url);
		LOGGER.debug("URL for calling remote server : ", url);
		int statusCode;
		try {
			statusCode = client.executeMethod(getMethod);

			LOGGER.debug("Status code for the Url: ", statusCode);
			if (statusCode == WebServiceConstants.STATUS_CODE_200) {
				response = getMethod.getResponseBodyAsString();
				LOGGER.debug("Response found: ", response);
			} else if (statusCode == WebServiceConstants.STATUS_CODE_403) {
				LOGGER.error("Invalid URL.");
				response = WorkflowConstants.NOT_APPLICABLE;
			} else {
				response = WorkflowConstants.NOT_APPLICABLE;
				LOGGER.error("Unable to load URL.");
			}
		} catch (HttpException e) {
			LOGGER.error(e, "Http Exception while calling remote server with Url: ", url);
		} catch (IOException e) {
			LOGGER.error(e, "I/O Exception while calling remote server with Url: ", url);
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
		}
		return response;
	}

	@Override
	public Boolean restartBatchInstance(String targetURL, String batchInstanceIdentifier, String moduleName, String throwException)
			throws DCMAApplicationException {
		Boolean isRestarted = false;
		HttpClient client = new HttpClient();
		if (null != moduleName && moduleName.indexOf(" ") != -1) {
			moduleName = moduleName.replace(" ", "%20");
		}
		String finalURL = EphesoftStringUtil.concatenate(targetURL, WebServiceConstants.CONSTANT_BATCH_INSTANCE_IDENTIFIER,
				batchInstanceIdentifier, WebServiceConstants.CONSTANT_MODULE_NAME, moduleName,
				WebServiceConstants.CONSTANT_THROW_EXCEPTION, throwException);
		GetMethod getMethod = new GetMethod(finalURL);
		LOGGER.info(EphesoftStringUtil.concatenate("URL for calling remote server : ", finalURL));
		try {
			int statusCode = client.executeMethod(getMethod);
			LOGGER.info(EphesoftStringUtil.concatenate("Status code for the Url: ", statusCode));
			if (statusCode == WebServiceConstants.STATUS_CODE_200) {
				isRestarted = true;
				LOGGER.info("Response found: ");
			} else if (statusCode == WebServiceConstants.STATUS_CODE_403) {
				LOGGER.error(EphesoftStringUtil.concatenate("Invalid URL.", finalURL));
			} else {
				LOGGER.error("Unable to load URL.");
			}
		} catch (final HttpException httpException) {
			LOGGER.error(EphesoftStringUtil.concatenate("Http Exception while calling remote server with Url: ", finalURL),
					httpException);
		} catch (final IOException ioException) {
			LOGGER.error(EphesoftStringUtil.concatenate("I/O Exception while calling remote server with Url: ", finalURL), ioException);
		} finally {
			if (getMethod != null) {
				getMethod.releaseConnection();
			}
		}
		return isRestarted;
	}

	/**
	 * 
	 * @param client {@link HttpClient} 
	 * @param finalURL {@link String}
	 * @return {@link Boolean}
	 */
	private Boolean executeMethod(HttpClient client, String finalURL) {
		Boolean isRestarted;
		GetMethod getMethod = new GetMethod(finalURL);
		LOGGER.debug(EphesoftStringUtil.concatenate("URL for calling remote server: ", finalURL));
		try {
			int statusCode = client.executeMethod(getMethod);
			LOGGER.debug(EphesoftStringUtil.concatenate("Status code for the Url: ", statusCode));
			if (statusCode == WebServiceConstants.STATUS_CODE_200) {
				isRestarted = true;
				LOGGER.info("Response found.");
			} else if (statusCode == WebServiceConstants.STATUS_CODE_403) {
				LOGGER.error(EphesoftStringUtil.concatenate("Invalid URL!:", finalURL));
				isRestarted = false;
			} else {
				LOGGER.error("Unable to load URL.");
				isRestarted = false;
			}
		} catch (final HttpException httpException) {
			LOGGER.error(EphesoftStringUtil.concatenate("Http Exception while calling remote server with Url: ", finalURL),
					httpException);
			isRestarted = false;
		} catch (final IOException ioException) {
			LOGGER.error(EphesoftStringUtil.concatenate("I/O Exception while calling remote server with Url: ", finalURL), ioException);
			isRestarted = false;
		} finally {
			if (null != getMethod) {
				getMethod.releaseConnection();
			}
		}
		return isRestarted;
	}

	@Override
	public Boolean deleteBatchInstance(final String targetURL, final String batchInstanceIdentifier) throws DCMAApplicationException {
		final HttpClient client = new HttpClient();
		final String finalURL = EphesoftStringUtil.concatenate(targetURL, WebServiceConstants.CONSTANT_BATCH_INSTANCE_IDENTIFIER,
				batchInstanceIdentifier);
		return executeMethod(client, finalURL);
	}

}
