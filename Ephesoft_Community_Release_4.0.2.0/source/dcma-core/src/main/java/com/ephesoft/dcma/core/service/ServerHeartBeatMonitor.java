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

package com.ephesoft.dcma.core.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.NumberUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

public class ServerHeartBeatMonitor {

	/**
	 * Logger.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(ServerHeartBeatMonitor.class);

	public boolean getServerActiveStatus(final String ipAddress, final String portNumber, final String context, final boolean isSecure) {
		boolean isActive = false;
		String pathURL = getURL(ipAddress, portNumber, context, isSecure);
		isActive = checkHealth(pathURL);
		if (!isActive) {
			int noOfPings = 1;
			try {
				noOfPings = getNumberOfPings();
			} catch (final NumberFormatException nfe) {
				LOGGER.error(nfe.getMessage());
			}
			for (int index = 1; index < noOfPings; index++) {
				if (checkHealth(pathURL)) {
					break;
				}
			}
		}
		return isActive;
	}

	/**
	 * Gets stream to read workflow properties for the server.
	 * 
	 * @return {@link BufferedInputStream}
	 * @throws IOException
	 */
	private BufferedInputStream getHeartBeatPropertiesReaderStream() throws IOException {
		BufferedInputStream propertyInputStream = null;
		String filePath = ICommonConstants.DCMA_HEART_BEAT_PROPERTIES;
		ClassPathResource classPathResourse = new ClassPathResource(filePath);
		if (classPathResourse.exists()) {
			propertyInputStream = new BufferedInputStream(classPathResourse.getInputStream());
		}
		return propertyInputStream;
	}

	/**
	 * Gets heart beat number of pings property.
	 * 
	 * @return int
	 */
	private int getNumberOfPings() {
		BufferedInputStream propertyInputStream = null;
		int numberOfPings = 1;
		try {
			propertyInputStream = getHeartBeatPropertiesReaderStream();
			Properties properties = new Properties();
			properties.load(propertyInputStream);
			String numberOfPingsString = properties.getProperty(ICommonConstants.HEART_BEAT_NUMBER_OF_PINGS);
			boolean validNumber = NumberUtil.isValidPositiveInteger(numberOfPingsString);
			if (validNumber) {
				numberOfPings = Integer.parseInt(numberOfPingsString);
			} else {
				LOGGER.error("Server execution capacity is in invalid syntax. It must be a positive integer.");
			}
		} catch (IOException inaccessibleFilePath) {
			LOGGER.error(EphesoftStringUtil.concatenate(ICommonConstants.DCMA_HEART_BEAT_PROPERTIES,
					" -Properties File is not accessible. Setting to Default Value(1)"));
		} catch (NumberFormatException wrongPropertyValue) {
			LOGGER.error(EphesoftStringUtil.concatenate(ICommonConstants.HEART_BEAT_NUMBER_OF_PINGS,
					" contains Invalid Property Value. Setting to Default Value(1)"));
		} finally {
			try {
				propertyInputStream.close();
			} catch (IOException e) {
				LOGGER.error("Unable to close InputStream.");
			}
		}
		return numberOfPings;
	}

	/**
	 * This method will return true if the server is active other wise false.
	 * 
	 * @param url {@link String} URL of the Heart beat service.
	 * @return true if the serve is active other wise false.
	 */
	private boolean checkHealth(final String serviceURL) {
		boolean isActive = false;
		LOGGER.info(serviceURL);
		if (!EphesoftStringUtil.isNullOrEmpty(serviceURL)) {

			// Create an instance of HttpClient.
			HttpClient client = new HttpClient();
			client.setConnectionTimeout(30000);
			client.setTimeout(30000);

			// Create a method instance.
			GetMethod method = new GetMethod(serviceURL);

			try {
				// Execute the method.
				int statusCode = client.executeMethod(method);

				if (statusCode == HttpStatus.SC_OK) {
					isActive = true;
				} else {
					LOGGER.info("Method failed: " + method.getStatusLine());
				}
			} catch (HttpException httpException) {
				LOGGER.error("Fatal protocol violation: " + httpException.getMessage());
			} catch (IOException ioException) {
				LOGGER.error("Fatal transport error: " + ioException.getMessage());
			} finally {
				// Release the connection.
				if (method != null) {
					method.releaseConnection();
				}
			}

			return isActive;
		}
		return isActive;
	}

	/**
	 * Returns the url of the heart beat services based on the parameter whether SSL is enabled or not.
	 * 
	 * @param serverRegistry {@link ServerRegistry} instance of ServerRegistry.
	 * @return the url of the heartbeat service running.
	 */
	private String getURL(final String ipAddress, String portNumber, String context, boolean isSecure) {
		// String ipAddress = serverRegistry.getIpAddress();
		// String portNumber = serverRegistry.getPort();
		// String context = serverRegistry.getAppContext();
		StringBuilder url = new StringBuilder();
		// EphesoftContext.getCurrent().isSecure()

		if (ipAddress == null || null == portNumber || null == context) {
			LOGGER.error("Problem in creating. Server Registry Info is Null.");
		} else {
			if (isSecure) {
				url.append(ICommonConstants.HTTPS_SLASH);
			} else {
				url.append(ICommonConstants.HTTP_SLASH);
			}
			url.append(ipAddress);
			url.append(ICommonConstants.COLON);
			url.append(portNumber);
			if (!context.contains(ICommonConstants.FORWARD_SLASH)) {
				url.append(ICommonConstants.FORWARD_SLASH);
			}
			url.append(context);
			url.append(ICommonConstants.FORWARD_SLASH);
			url.append(ICommonConstants.HEALTH_STATUS_HTML);
		}
		return url.toString();
	}
}
