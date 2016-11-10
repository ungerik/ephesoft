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

package com.ephesoft.dcma.batch.status;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.core.common.ServiceType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.domain.ServiceStatus;
import com.ephesoft.dcma.da.service.ServiceStatusService;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * Conveys and Notifies the status of services under failover mechanism. Provides utility to register services under host server.
 * 
 * <p>
 * The <code>StatusConveyor</code> class provides services for checking whether a given service type is allowed to be run on the server
 * or some other server is running the same services. It also registers the service under the server if it is not already registered
 * under some other servers.
 * </p>
 * 
 * @author Ephesoft
 * @version 1.0
 * @see ServiceType
 * 
 */

public final class StatusConveyor {

	/**
	 * The instance {@link StatusConveyor} is used to maintain the instance of status conveyor.
	 */
	private static StatusConveyor instance;

	/**
	 * The serviceRegisteredMap {@link Map} is used for tracking service registered within any server.
	 */
	private static Map<ServiceType, Boolean> serviceRegisteredMap;

	/**
	 * The serviceStatusService {@link ServiceStatusService} is for using services given by the business layer.
	 */
	@Autowired
	private ServiceStatusService serviceStatusService;

	/**
	 * LOGGER for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(StatusConveyor.class);

	/**
	 * Constant for web service URL which will handle service events.
	 */
	private static final String NOTIFY_SERVER_FOR_SERVICE = "ws/notifyServerForService";

	/**
	 * Constant for status code of successful handling of service event.
	 */
	private static final int STATUS_OK = 200;

	/**
	 * Private Constructor for restricting object creation outside this class.
	 */
	private StatusConveyor() {
		super();
	}

	/**
	 * Returns the instance of Status Conveyor and also initialises the map for service registration.
	 * 
	 * @return {@link StatusConveyor} instance of StatusConveyor.
	 */
	public static StatusConveyor getStatusConveyor() {
		if (null == instance) {
			instance = new StatusConveyor();
			initializeMap();
		}
		return instance;
	}

	/**
	 * Initialises the server register map for tracking whether a particular service is registered under some server or not.
	 */
	private static void initializeMap() {
		LOGGER.debug("Initializing service register map");

		// Get all service types
		final ServiceType[] serviceTypeArray = ServiceType.values();
		serviceRegisteredMap = new HashMap<ServiceType, Boolean>(serviceTypeArray.length);

		// Initialise the map using default false
		for (ServiceType serviceType : serviceTypeArray) {
			serviceRegisteredMap.put(serviceType, Boolean.FALSE);
		}
		LOGGER.debug(EphesoftStringUtil.concatenate("Map initialized: ", serviceRegisteredMap.toString()));
	}

	/**
	 * Checks the status of service passed by first checking whether service is registered under some server or not, if not it adds
	 * that service under the current server otherwise check it checks if it is registered under current server. If it is registered
	 * under current server it return true otherwise false.
	 * 
	 * 
	 * @param serviceType {@link ServiceType} type of service for which status has to be checked.
	 * @return true if registered under current server otherwise false.
	 */
	public boolean checkForServiceStatus(final ServiceType serviceType) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Checking status of service: ", serviceType.toString()));
		boolean isServiceAvailable = false;
		final ServerRegistry hostServerRegistry = EphesoftContext.getHostServerRegistry();
		if (null != hostServerRegistry) {

			// First we check whether given service type is registered or not.
			if (!serviceRegisteredMap.get(serviceType)) {
				registerService(hostServerRegistry, serviceType);
			}

			final List<ServiceStatus> serviceStatusList = serviceStatusService.getServiceStatusListForServer(hostServerRegistry,
					serviceType);
			isServiceAvailable = (null != serviceStatusList && !serviceStatusList.isEmpty());
			LOGGER.debug(EphesoftStringUtil.concatenate("Status of service ", serviceType.toString(), " is:", isServiceAvailable,
					" for server registry: ", hostServerRegistry.getAppContext()));
		} else {
			LOGGER.error("Server registry for the host server is null!");
		}
		return isServiceAvailable;
	}

	/**
	 * Notifies any service event to fired on current server but needs to be handled by the server under which the service is
	 * registered. It hits the particular server with the details required and registered server handles it respectively.
	 * 
	 * @param paramMap {@link Map<String, String>} details to be send to the server.
	 * @param serviceType {@link ServiceType} type of service.
	 */
	public void notifyServiceEvent(final Map<String, String> paramMap, final ServiceType serviceType) {
		if (null != serviceType) {
			LOGGER.debug(EphesoftStringUtil.concatenate("Notifying server for service: ", serviceType.toString()));
			final String notifyWebServiceUrl = getNotifyWebServiceUrl(serviceType);
			if (null != notifyWebServiceUrl && !notifyWebServiceUrl.isEmpty()) {
				final HttpClient httpClient = new HttpClient();
				final PostMethod postMethod = new PostMethod(notifyWebServiceUrl);
				Part[] partArray = null;
				int index = 0;

				// If the details are to be send to web service
				if (null == paramMap || paramMap.isEmpty()) {
					partArray = new Part[1];
				} else {
					LOGGER.debug(EphesoftStringUtil.concatenate("Parameter passed are: ", paramMap.toString()));
					partArray = new Part[(paramMap.size() + 1)];
					final Iterator<String> keyIterator = paramMap.keySet().iterator();
					String key = null;
					while (keyIterator.hasNext()) {
						key = keyIterator.next();
						partArray[index] = new StringPart(key, paramMap.get(key));
						index++;
					}
				}

				// Type of service which is a required parameter to be send
				partArray[index] = new StringPart(ICommonConstants.SERVICE_TYPE_PARAMETER, String
						.valueOf(serviceType.getServiceType()));
				MultipartRequestEntity entity = new MultipartRequestEntity(partArray, postMethod.getParams());
				postMethod.setRequestEntity(entity);
				try {
					int statusCode = httpClient.executeMethod(postMethod);

					if (statusCode == STATUS_OK) {
						LOGGER.debug(EphesoftStringUtil.concatenate("Server was notified successfully for service: ", serviceType
								.toString()));
					} else {
						LOGGER.error(EphesoftStringUtil.concatenate("Server was not able to be notified for service: ", serviceType
								.toString(), " and status code: ", statusCode));
					}
				} catch (HttpException httpException) {
					LOGGER.error(EphesoftStringUtil.concatenate("Could not connect to server for notifying service: ", serviceType
							.toString(), ICommonConstants.SPACE, httpException.getMessage()));
				} catch (IOException ioException) {
					LOGGER.error(EphesoftStringUtil.concatenate("Could not connect to server for notifying service: ", serviceType
							.toString(), ICommonConstants.SPACE, ioException.getMessage()));
				}
			}
		}
	}

	/**
	 * Gets the URL for notify event web service for the service type passed. It fetches the server under which the service type is
	 * currently registered and using the server registry it makes the URL of web service on that server.
	 * 
	 * @param serviceType {@link ServiceType} type of service.
	 * @return {@link String} web service URL.
	 */
	private String getNotifyWebServiceUrl(final ServiceType serviceType) {
		String notifyWebServiceUrl = null;
		if (null != serviceType) {
			LOGGER.debug(EphesoftStringUtil.concatenate("Getting web service URL for service: ", serviceType.toString()));

			// Fetch the service status for the service type
			List<ServiceStatus> serviceStatusList = serviceStatusService.getServiceStatusList(serviceType);
			if (null != serviceStatusList && !serviceStatusList.isEmpty()) {

				// A service can be registered only under one server
				ServiceStatus serviceStatus = serviceStatusList.get(0);

				// Get the server information
				ServerRegistry serverRegistry = serviceStatus.getServerRegistry();
				LOGGER.debug(EphesoftStringUtil.concatenate("Server to be notifed is: ", serverRegistry.getIpAddress()));
				if (null != serverRegistry) {
					
					//Check for if request is from secure channel. 
					if (EphesoftContext.getCurrent().isSecure()) {
						notifyWebServiceUrl = EphesoftStringUtil.concatenate(ICommonConstants.HTTPS, ICommonConstants.COLON,
								ICommonConstants.DOUBLE_FORWARD_SLASH, serverRegistry.getIpAddress(), ICommonConstants.COLON,
								serverRegistry.getPort(), serverRegistry.getAppContext(), ICommonConstants.FORWARD_SLASH,
								NOTIFY_SERVER_FOR_SERVICE);
					} else {
						notifyWebServiceUrl = EphesoftStringUtil.concatenate(ICommonConstants.HTTP, ICommonConstants.COLON,
								ICommonConstants.DOUBLE_FORWARD_SLASH, serverRegistry.getIpAddress(), ICommonConstants.COLON,
								serverRegistry.getPort(), serverRegistry.getAppContext(), ICommonConstants.FORWARD_SLASH,
								NOTIFY_SERVER_FOR_SERVICE);
					}
					LOGGER.debug(EphesoftStringUtil.concatenate("URL generated is: ", notifyWebServiceUrl));
				}
			} else {
				LOGGER.debug(EphesoftStringUtil.concatenate("Service: ", serviceType.toString(),
						" is not registered under any server."));
			}
		}
		return notifyWebServiceUrl;
	}

	/**
	 * Registers service under current server if it is not registered under any other server. This creates an entry for the service
	 * type passed.
	 * 
	 * @param hostServerRegistry {@link ServerRegistry} server registry of the host server.
	 * @param serviceType {@link ServiceType} type of service passed.
	 */
	private void registerService(final ServerRegistry hostServerRegistry, final ServiceType serviceType) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Checking registration for service: ", serviceType.toString(),
				" under host server", hostServerRegistry.getIpAddress()));
		if (null != hostServerRegistry) {
			final List<ServiceStatus> serviceStatusList = serviceStatusService.getServiceStatusList(serviceType);
			if (null == serviceStatusList || serviceStatusList.isEmpty()) {
				LOGGER.debug(EphesoftStringUtil.concatenate("Registering service: ", serviceType.toString(), "under host server",
						hostServerRegistry.getIpAddress()));
				final ServiceStatus serviceStatus = new ServiceStatus();
				serviceStatus.setServerRegistry(hostServerRegistry);
				serviceStatus.setServiceType(serviceType);
				serviceStatusService.createServiceStatus(serviceStatus);
			} else {
				LOGGER.debug(EphesoftStringUtil
						.concatenate(serviceType.toString(), " Service is already registered under some server"));
			}
			serviceRegisteredMap.put(serviceType, Boolean.TRUE);
		}
	}
}
