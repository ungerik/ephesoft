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

package com.ephesoft.dcma.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import com.ephesoft.dcma.util.constant.UtilConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class ApplicationConfigProperties {

	private static final long serialVersionUID = 1L;
	private static final String APPLICATION_PROPERTY_NAME = "application";
	private static final String META_INF = "META-INF";
	private Properties properties;

	private static ApplicationConfigProperties applicationConfigProperties = null;

	/**
	 * Logger instance for logging using slf4j.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfigProperties.class);

	/**
	 * Constant for default wait time for a command to wait for successful completion of command.
	 */
	private static final int DEFAULT_WAIT_TIME = 10;

	public Properties getProperties() {
		return properties;
	}

	private ApplicationConfigProperties() throws IOException {
		InputStream propertyInStream = null;
		this.properties = new Properties();
		try {
			String filePath = META_INF + File.separator + APPLICATION_PROPERTY_NAME + ".properties";
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			this.properties.load(propertyInStream);
		} finally {
			if (propertyInStream != null) {
				propertyInStream.close();
			}
		}
	}

	public Properties getAllProperties(String propertyFileName) throws IOException {
		InputStream propertyInStream = null;
		Properties properties = new Properties();
		try {
			String filePath = META_INF + File.separator + propertyFileName + ".properties";
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			properties.load(propertyInStream);
		} finally {
			if (propertyInStream != null) {
				propertyInStream.close();
			}
		}
		return properties;
	}

	public static ApplicationConfigProperties getApplicationConfigProperties() throws IOException {
		if (applicationConfigProperties == null) {
			applicationConfigProperties = new ApplicationConfigProperties();
		}
		return applicationConfigProperties;
	}

	public String getProperty(String propertyKey) throws IOException {
		return applicationConfigProperties.properties.getProperty(propertyKey);
	}

	/**
	 * Utility method to get wait time property value.
	 * 
	 * @return Wait time
	 */
	public static int getWaitTimeProperty(String waitTimePropertyName) {
		int waitTime = DEFAULT_WAIT_TIME;
		try {
			final String waitTimeProp = ApplicationConfigProperties.getApplicationConfigProperties().getProperty(waitTimePropertyName);
			waitTime = Integer.parseInt(waitTimeProp);
		} catch (final IOException ioe) {
			LOGGER.error(EphesoftStringUtil.concatenate("Error retrieving property ", waitTimePropertyName));
		} catch (final NumberFormatException nfe) {
			LOGGER.error(EphesoftStringUtil.concatenate("Error while parsing property ", waitTimePropertyName));
		}
		return waitTime;
	}

	/**
	 * Returns the application folder path.
	 * 
	 * @param environmentVariable {@link String} the environment variable whose value need to be fetched
	 * @return {@link String} the application folder path
	 */
	public static String getApplicationFolderPath(String environmentVariable) {
		String applicationFolderPath = System.getenv(environmentVariable);
		return applicationFolderPath;
	}
	
	public Properties getAllUTF8Properties(final String propertyFilePath) throws IOException {
		InputStream propertyInStream = null;
		BufferedReader bufferedPropertyReader = null;
		final Properties properties = new Properties();
		try {
			final String filePath = EphesoftStringUtil.concatenate(META_INF, File.separator, propertyFilePath, UtilConstants.DOT, UtilConstants.PROPERTIES);
			propertyInStream = new ClassPathResource(filePath).getInputStream();
			bufferedPropertyReader = new BufferedReader(new InputStreamReader(propertyInStream, UtilConstants.UTF8_ENCODING));
			properties.load(bufferedPropertyReader);
		} finally {
			if (propertyInStream != null) {
				propertyInStream.close();
			}
		}
		return properties;
	}
}
