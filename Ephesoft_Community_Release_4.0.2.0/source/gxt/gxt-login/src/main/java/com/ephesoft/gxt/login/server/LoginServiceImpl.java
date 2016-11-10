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

package com.ephesoft.gxt.login.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.core.common.ClusterPropertyType;
import com.ephesoft.dcma.da.domain.ClusterProperty;
import com.ephesoft.dcma.da.domain.ServerRegistry;
import com.ephesoft.dcma.da.service.ClusterPropertyService;
import com.ephesoft.dcma.da.service.ServerRegistryService;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.gxt.login.client.LoginRemoteService;

public class LoginServiceImpl extends DCMARemoteServiceServlet implements LoginRemoteService {

	private static final long serialVersionUID = 1L;
	private static final String APPLICATION_PROPERTY_NAME = "application";
	private static final String META_INF = "META-INF";
	private Properties appProperties;

	/**
	 * Ephesoft Logger
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(LoginServiceImpl.class);

	@Override
	public String getProductVersion() throws Exception {
		appProperties = loadProperties(APPLICATION_PROPERTY_NAME);
		return appProperties.getProperty("ephesoft.product.version");
	}

	private final Properties loadProperties(String propertyName) throws IOException {
		InputStream propertyInStream = null;
		Properties properties = new Properties();
		try {
			String filePath = META_INF + File.separator + propertyName + ".properties";
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			properties.load(propertyInStream);
		} finally {
			if (propertyInStream != null) {
				propertyInStream.close();
			}
		}
		return properties;
	}

	/**
	 * API to get license expiry message.
	 */
	@Override
	public void getLicenseExpiryMsg() throws Exception {

	}

	@Override
	public boolean getFailOverMessage() throws Exception {
		boolean failOverSwitch = false;
		LOGGER.debug("Retreiving FailOver switch value");
		ClusterPropertyService clusterPropertyService = this.getSingleBeanOfType(ClusterPropertyService.class);

		ClusterProperty failOverProperty = clusterPropertyService
				.getClusterPropertyValue(ClusterPropertyType.LICENSE_FAILOVER_MECHANISM);
		if (null != failOverProperty) {
			String failOverSwitchStatus = failOverProperty.getPropertyValue();

			if (CoreCommonConstants.ON.equalsIgnoreCase(failOverSwitchStatus)) {
				ServerRegistryService serverRegistryService = this.getSingleBeanOfType(ServerRegistryService.class);
				List<ServerRegistry> activeServerList = serverRegistryService.getActiveLicenseServers();

				if (null != activeServerList && !activeServerList.isEmpty() && activeServerList.size() >= 2) {
					LOGGER.info("Fail Over message needs not to be displayed.");
					failOverSwitch = true;
				} else {
					LOGGER.info("Fail Over message needs to be displayed.");
					failOverSwitch = false;
				}
			} else {
				LOGGER.info("Fail Over message needs not to be displayed.");
				failOverSwitch = true;
			}
		} else {
			LOGGER.error("Fail Over Property value doesn't exists in db.");
			throw new Exception("NO such property is there in DB");
		}
		return failOverSwitch;
	}

}
