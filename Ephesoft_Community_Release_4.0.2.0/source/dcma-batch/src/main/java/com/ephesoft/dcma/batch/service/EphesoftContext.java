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

package com.ephesoft.dcma.batch.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.core.common.AuthenticationType;
import com.ephesoft.dcma.core.common.ClusterPropertyType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.da.domain.ClusterProperty;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.service.ClusterPropertyService;
import com.ephesoft.dcma.da.service.ServerRegistryService;
import com.ephesoft.dcma.util.ApplicationContextUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.NumberUtil;

/**
 * The <code>EphesoftContext</code> class is responsible for providing host server information and updating the same information in
 * database.
 * 
 * The information provided includes:
 * <ul>
 * <li>Host Name e.g., server1
 * <li>Host Heart-beat URL
 * <li>Host server registry entry
 * <li>Other in network server registry entry
 * </ul>
 * 
 * It inserts and updated the host server registry entry in ephesoft database.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 10-Jun-2013 <br/>
 * @version 1.0 <br/>
 *          $LastChangedDate: 2015-02-18 12:12:22 +0530 (Wed, 18 Feb 2015) $ <br/>
 *          $LastChangedRevision: 19605 $ <br/>
 * 
 * @see Context
 * @see ApplicationContextAware
 */
@Component
public class EphesoftContext implements Context, ApplicationContextAware {

	/**
	 * LOGGER for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EphesoftContext.class);

	/**
	 * Constant to hold parameter name used to configure authentication type in web.xml.
	 */
	private static final String AUTHENTICATION_TYPE = "authenticationType";

	/**
	 * To hold ApplicationContext instance returned through {@link ApplicationContextAware}.
	 */
	private static ApplicationContext applicationContext;

	/**
	 * The service for CRUD operation on server_registry table in Ephesoft Database.
	 */
	@Autowired
	private ServerRegistryService registryService;

	/**
	 * {@link ServerBatchPriorityService} Instance of server's set of batch instance's priority service.
	 */
	@Autowired
	private ServerBatchPriorityService serverBatchPriorityService;

	/**
	 * The id of host server registry entry inside the server_registry table.
	 */
	private long id = 0;

	/**
	 * The port number configured inside web.xml. It is used to make host URL.
	 */
	private static int port = 0;

	/**
	 * To hold the context path of the host server.
	 */
	private static String context = "";

	/**
	 * To hold the host name for the host server.
	 */
	private static String hostName = null;

	/**
	 * To hold the protocol for the server.
	 */
	private static boolean isSecure;

	/**
	 * Configured authentication type value.
	 */
	private AuthenticationType authenticationType;

	/**
	 * Service Object to access Cluster properties.
	 */
	@Autowired
	private ClusterPropertyService clustorPropertyService;

	/**
	 * {@link String} Server Context Path of the server.
	 */
	private static String hostServerContextPath;

	/**
	 * {@link String} Server Protocol Path of the server.
	 */
	private static String serverProtocolPath;

	/**
	 * {@link String} Server Context Path of the server.
	 */
	private static String serverURIPath;

	@Override
	public void setApplicationContext(final ApplicationContext appContext) throws BeansException {
		applicationContext = appContext;
	}

	@PostConstruct
	public void init() {
		hostName = getHostAddress();
		createRegistry();
		// Set the authentication type configured for the application value
		setAuthenticationType();
	}

	/**
	 * Creates host server registry entry in server_registry table. It first checks for the entry inside the database, if it is present
	 * there it updates the entry by setting Active column to TRUE otherwise it make the respective entry in the database.
	 * 
	 * It assumes correct port number inside the web.xml.
	 */
	private void createRegistry() {

		// Get port number from 'web.xml'
		if (applicationContext instanceof WebApplicationContext) {

			// Getting the instance of ServletContext.
			final ServletContext servletContext = ((WebApplicationContext) applicationContext).getServletContext();
			final String portNumber = servletContext.getInitParameter(ICommonConstants.PORT_PARAM);
			if (portNumber != null) {
				try {
					port = Integer.valueOf(portNumber);
				} catch (NumberFormatException numberFormatException) {
					LOGGER.error("No port number is defined in web.xml. Assigning port number 0");
				}
			}
			context = ((WebApplicationContext) applicationContext).getServletContext().getContextPath();

			// Getting the protocol from web.xml.
			final String protocol = servletContext.getInitParameter(ICommonConstants.PROTOCOL_PARAM);

			// Set isSeure to true if request is from secure channel otherwise set it to false;
			if (ICommonConstants.HTTPS.equals(protocol)) {
				isSecure = true;

				// Registers a protocol with the given identifier.
				Protocol.registerProtocol(ICommonConstants.HTTPS, new Protocol(ICommonConstants.HTTPS,
						(ProtocolSocketFactory) new EphesoftSSLProtocolSocketFactory(), port));
			}
		}

		List<ServerRegistry> activeServers = registryService.getActiveServers();
		ServerRegistry registry = registryService.getServerRegistry(this.hostName, String.valueOf(this.port), this.context);
		if (activeServers.isEmpty()) {
			registry = new ServerRegistry();
			registry.setIpAddress(this.hostName);
			registry.setPort(String.valueOf(this.port));
			registry.setAppContext(this.context);
			registry.setActive(true);
			registry.setLicenseServer(false); // Setting it to false to mark this server as not a License server. when License
												// server
												// will be started on activating License server config file in
												// ApplicationContext.xml
												// then this property will be changed to true by LicenseServerManager
			setWorkflowProperties(registry);
			registry.setFolderMoniterServer(false);
			// Reading and Setting the Execution Capacity for a new server
			registryService.createServerRegistry(registry);
		} else {
			// Get host server registry entry from database.
			if (registry != null) {
				registry.setActive(true);
				setWorkflowProperties(registry);

				// Updating the Execution Capacity
				registry.setLicenseServer(false); // Same thing as above
				registryService.updateServerRegistry(registry);
			}
		}

		// add LICENSE_FAILOVER_MECHANISM property default value.
		createOrCheckClusterProperty(ClusterPropertyType.LICENSE_FAILOVER_MECHANISM);
		id = registry.getId();
	}

	/**
	 * Gets stream to read workflow properties for the server.
	 * 
	 * @return {@link BufferedInputStream}
	 * @throws IOException
	 */
	private BufferedInputStream getWorkflowPropertiesReaderStream() throws IOException {
		BufferedInputStream propertyInputStream = null;
		String filePath = BatchConstants.DCMA_WORKFLOWS_PROPERTIES;
		ClassPathResource classPathResourse = new ClassPathResource(filePath);
		if (classPathResourse.exists()) {
			propertyInputStream = new BufferedInputStream(classPathResourse.getInputStream());
		}
		return propertyInputStream;
	}

	/**
	 * Sets workflow properties for server.
	 * 
	 * @param serverRegistry {@link ServerRegistry} Server registry.
	 */
	private void setWorkflowProperties(final ServerRegistry serverRegistry) {
		int executionCapacity = 0;
		String priorityRange = BatchConstants.DEFAULT_SERVER_INSTANCE_BATCH_PRIORITY_RANGE;
		BufferedInputStream propertyInputStream = null;
		try {
			propertyInputStream = getWorkflowPropertiesReaderStream();
			Properties properties = new Properties();
			properties.load(propertyInputStream);
			String stringExecutionCapacity = "1";
			boolean validExecutionCapacity = NumberUtil.isValidPositiveInteger(stringExecutionCapacity);
			if (validExecutionCapacity) {
				executionCapacity = Integer.parseInt(stringExecutionCapacity);
			} else {
				LOGGER.error("Server execution capacity is in invalid syntax. It must be a positive integer.");
			}
			priorityRange = properties.getProperty(BatchConstants.SERVER_INSTANCE_BATCH_PRIORITY_RANGE);
		} catch (IOException inaccessibleFilePath) {
			LOGGER.error(EphesoftStringUtil.concatenate(BatchConstants.DCMA_WORKFLOWS_PROPERTIES,
					" -Properties File is not accessible. Setting to Default Value(0)"));
		} catch (NumberFormatException wrongPropertyValue) {
			LOGGER.error(EphesoftStringUtil.concatenate(BatchConstants.SERVER_INSTANCE_MAX_PROCESS_CAPACITY,
					" contains Invalid Property Value. Setting to Default Value(0)"));
		} finally {
			try {
				propertyInputStream.close();
			} catch (IOException e) {
				LOGGER.error("Unable to close InputStream.");
			}
		}
		serverRegistry.setExecutionCapacity(executionCapacity);
		Set<Integer> setOfPriorityRange = serverBatchPriorityService.getRangeOfPriority(priorityRange);
		boolean isValidPriorityRange = serverBatchPriorityService.validatePriorityRange(setOfPriorityRange, serverRegistry, isSecure);
		if (isValidPriorityRange) {
			String modifiedPriorityRangeString = serverBatchPriorityService.getPriorityRangeString(setOfPriorityRange);
			serverRegistry.setPriorityRange(modifiedPriorityRangeString);
			registryService.setNumericSetOfPriorityRange(setOfPriorityRange);
		} else {
			String errorMessage = EphesoftStringUtil.concatenate("The server with context: ", this.context, ", IP: ", this.hostName,
					" and port: ", this.port, " has invalid value of Priority range for execution of batches: ", priorityRange,
					". Please make sure it is equal to or does not intersect with other server's priority range.");
			LOGGER.error(errorMessage);
			try {
				Thread.sleep(10000); // 1000 milliseconds is one second.
			} catch (InterruptedException interruptException) {
				// Do nothing.
			}
			System.exit(1);
		}
	}

	/**
	 * API to create or check for FailOver property in DB.
	 * 
	 * @param propertyType
	 */
	private String createOrCheckClusterProperty(ClusterPropertyType propertyType) {
		ClusterProperty property = clustorPropertyService.getClusterPropertyValue(propertyType);
		if (null == property) {
			property = new ClusterProperty();
			property.setPropertyName(propertyType);
			property.setPropertyValue(ICommonConstants.OFF);
			clustorPropertyService.createClusterProperty(property);
		} else {
			LOGGER.info(EphesoftStringUtil.concatenate(property.getPropertyName(),
					" property was already set in Common DB and its value is ", property.getPropertyValue()));
		}
		return property.getPropertyValue();
	}

	/**
	 * Returns the URL of HTML used to check the status of server. URL is made using the context path.
	 * 
	 * @return heartbeatUrl {@link String} heart beat URL.
	 */
	public String getHeartbeatUrl() {
		String heartbeatUrl = ICommonConstants.EMPTY_STRING;
		final String forwardSlashRequired = (null != context && context.contains(ICommonConstants.FORWARD_SLASH))
				? ICommonConstants.EMPTY_STRING : ICommonConstants.FORWARD_SLASH;

		// Create heart beat URL based on the isSecure variable.
		if (isSecure) {
			heartbeatUrl = EphesoftStringUtil.concatenate(ICommonConstants.HTTPS_SLASH, this.hostName, ICommonConstants.COLON,
					this.getPort(), forwardSlashRequired, this.getWebContextPath(), ICommonConstants.FORWARD_SLASH,
					ICommonConstants.HEALTH_STATUS_HTML);
		} else {
			heartbeatUrl = EphesoftStringUtil.concatenate(ICommonConstants.HTTP_SLASH, this.hostName, ICommonConstants.COLON,
					this.getPort(), forwardSlashRequired, this.getWebContextPath(), ICommonConstants.FORWARD_SLASH,
					ICommonConstants.HEALTH_STATUS_HTML);
		}
		return heartbeatUrl;
	}

	/**
	 * Returns the host address of host server. If host name cannot be identified for any particular reason it returns LOCALHOST as
	 * host address.
	 * 
	 * @return hostName {@link String} host name of localhost.
	 */
	public static String getHostAddress() {
		String hostName = null;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException unknownHostException) {
			hostName = ICommonConstants.LOCALHOST;
		}
		return hostName;
	}

	/**
	 * Returns instance of {@link EphesoftContext}.
	 * 
	 * @return {@link EphesoftContext} instance of ephesoft context.
	 */
	public static EphesoftContext getCurrent() {
		return get(Context.class).getContext();
	}

	/**
	 * Returns the instance of {@link ServerRegistry} for the host server.
	 * 
	 * @return {@link ServerRegistry} instance for host server.
	 */
	public static ServerRegistry getHostServerRegistry() {
		final EphesoftContext context = get(Context.class).getContext();
		final ServerRegistryService registryService = get(ServerRegistryService.class);
		return registryService.getServerRegistry(context.getId());
	}

	/**
	 * Returns the instance of {@link ServerRegistry} with respect to ip address, port number and context passed as parameter.
	 * 
	 * @param ipAddress {@link String} ip address of the requested server.
	 * @param portNumber {@link String} port number on which the application is running on that server.
	 * @param applicationContext {@link String} application context.
	 * @return {@link ServerRegistry} instance for server.
	 */
	public static ServerRegistry getServerRegistry(final String ipAddress, final String portNumber, final String applicationContext) {
		final ServerRegistryService registryService = get(ServerRegistryService.class);
		return registryService.getServerRegistry(ipAddress, portNumber, applicationContext);
	}

	/**
	 * Returns the instance of type passed as parameter.
	 * 
	 * @param <T> type of instance.
	 * @param type instance type class.
	 * @return instance of type required.
	 */
	public static <T> T get(final Class<T> type) {
		if (applicationContext == null) {
			LOGGER.error("Ephesoft application Context yet not initialized.");
			throw new FactoryBeanNotInitializedException("Ephesoft application Context yet not initialized.");
		}
		return ApplicationContextUtil.getSingleBeanOfType(applicationContext, type);
	}

	/**
	 * Returns the instance of type and name passes as a parameter.
	 * 
	 * @param <T> type of instance.
	 * @param type instance type class.
	 * @param name instance type name.
	 * @return instance of type required.
	 */
	public static <T> T get(Class<T> type, String name) {
		if (applicationContext == null) {
			LOGGER.error("Ephesoft application Context yet not initialized.");
			throw new FactoryBeanNotInitializedException("Ephesoft application Context yet not initialized.");
		}
		return ApplicationContextUtil.getBeanFromContext(applicationContext, name, type);
	}

	/**
	 * Returns port number of the host server.
	 * 
	 * @return port number.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Returns host name for the host server.
	 * 
	 * @return host name.
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * Returns context path for the host server.
	 * 
	 * @return context path.
	 */
	public String getWebContextPath() {
		return context;
	}

	@Override
	public EphesoftContext getContext() {
		return this;
	}

	/**
	 * Returns the host server registry id in server_registry table.
	 * 
	 * @return id of host server registry.
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the isSecure
	 */
	public boolean isSecure() {
		return isSecure;
	}

	/**
	 * Returns the authentication type configured for the application.
	 * 
	 * <p>
	 * It will return the configured authentication type if present otherwise the default authentication type.
	 * 
	 * @return {@link AuthenticationType} authentication type configured.
	 */
	public AuthenticationType getAuthenticationType() {
		return this.authenticationType;
	}

	/**
	 * Sets the authentication type to be supported within the application.
	 * 
	 * <p>
	 * By default <code>EPHESOFT_AUTHENTICATION</code> will be set. Default will be set in the following cases:-
	 * <li>If parameter is not set in web.xml.
	 * <li>If parameter value is wrong.
	 */
	private void setAuthenticationType() {

		// Set the authentication type to default value
		this.authenticationType = AuthenticationType.EPHESOFT_AUTHENTICATION;
		if (null != applicationContext && applicationContext instanceof WebApplicationContext) {

			// Get the authentication type from web.xml
			final String authenticationType = ((WebApplicationContext) applicationContext).getServletContext().getInitParameter(
					AUTHENTICATION_TYPE);
			if (!EphesoftStringUtil.isNullOrEmpty(authenticationType)) {

				// Initialising the authentication type with the configured value
				final int tempAuthenticationType = Integer.valueOf(authenticationType);
				final AuthenticationType[] authenticationTypes = AuthenticationType.values();
				for (AuthenticationType authenticationTypeVar : authenticationTypes) {
					if (authenticationTypeVar.getAuthenticationType() == tempAuthenticationType) {
						this.authenticationType = authenticationTypeVar;
						break;
					}
				}
			}
		}
	}

	/**
	 * Gets current server context path.
	 * 
	 * @return {@link String}
	 */
	public static String getHostServerContextPath() {
		if (null == hostServerContextPath) {
			hostServerContextPath = EphesoftStringUtil.concatenate(hostName, ICommonConstants.COLON, port, context);
		}
		return hostServerContextPath;
	}

	/**
	 * Gets current server protocol path.
	 * 
	 * @return {@link String}
	 */
	public static String getServerSecurityProtocol() {
		if (null == serverProtocolPath) {
			if (isSecure) {
				serverProtocolPath = ICommonConstants.HTTPS_SLASH;
			} else {
				serverProtocolPath = ICommonConstants.HTTP_SLASH;
			}
		}
		return serverProtocolPath;
	}

	/**
	 * Gets current server host URI path.
	 * 
	 * @return {@link String}
	 */
	public static String getHostServerPath() {
		if (null == serverURIPath) {
			if (isSecure) {
				serverURIPath = EphesoftStringUtil.concatenate(hostName, ICommonConstants.COLON, port);
			} else {
				serverURIPath = EphesoftStringUtil.concatenate(hostName, ICommonConstants.COLON, port);
			}
		}
		return serverURIPath;
	}

}
