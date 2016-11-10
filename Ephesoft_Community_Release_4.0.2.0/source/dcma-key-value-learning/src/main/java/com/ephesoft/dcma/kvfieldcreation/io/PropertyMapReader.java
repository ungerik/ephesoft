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

package com.ephesoft.dcma.kvfieldcreation.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * This class has methods for reading the property file and return its data.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 02-Jan-2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class PropertyMapReader {

	/**
	 * LOGGER for this class.
	 */
	private final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(PropertyMapReader.class);

	/**
	 * This method reads a property file and returns its key value pairs in a {@link Map}.
	 * 
	 * @param propertyFilePath {@link String}
	 * @return {@link Map<{@link String}, {@link String}>} null if properties file is empty or could not be read.
	 * @throws {@link DCMAApplicationException}
	 */
	public Map<String, String> getPropertyMap(final String propertyFilePath) throws DCMAApplicationException {
		LOGGER.trace("Entering method to get properties map from property file.");
		LOGGER.debug(EphesoftStringUtil.concatenate("propertyFilePath is: ", propertyFilePath));

		// Map containing key as property type and value as strings.
		Map<String, String> propertyMap = null;
		Properties properties = loadProperties(propertyFilePath);
		if (null == properties) {
			LOGGER.error("Mapping properties could not be read.");
			throw new DCMAApplicationException("Mapping properties could not be read.");
		} else {
			Set<Object> propertyKeys = properties.keySet();
			if (null != propertyKeys) {
				propertyMap = new HashMap<String, String>(propertyKeys.size());
				Iterator<Object> propertyIterator = propertyKeys.iterator();
				while (propertyIterator.hasNext()) {
					Object property = propertyIterator.next();
					String keyString = (String) property;
					if (null != keyString) {
						String value = properties.getProperty(keyString);
						LOGGER.debug(EphesoftStringUtil.concatenate("Key read is ", keyString, " corresponding value is ", value));
						propertyMap.put(keyString, value);
					}
				}
			}
		}
		LOGGER.trace("Exiting method to get properties map from property file.");
		return propertyMap;
	}

	/**
	 * This method is used to load the property file.
	 * 
	 * @param propertyFilePath {@link String}
	 * @return {@link Properties} the loaded property file's mapped object.
	 * @throws DCMAApplicationException if properties file could not be loaded in map.
	 */
	private Properties loadProperties(final String propertyFilePath) throws DCMAApplicationException {
		LOGGER.trace("Entering method to load properties map from property file.");
		LOGGER.debug(EphesoftStringUtil.concatenate("propertyFilePath is: ", propertyFilePath));
		Properties properties = null;
		if (null != propertyFilePath) {
			properties = new Properties();
			FileInputStream inputStream = null;
			try {
				File file = new File(propertyFilePath);
				if (!file.exists()) {
					LOGGER.error("The mapping properties file not found on given property mapping file Path.");
					throw new DCMAApplicationException("The mapping properties file could not be found.");
				}
				inputStream = new FileInputStream(file);
				properties.load(inputStream);
			} catch (FileNotFoundException fileException) {
				LOGGER.error("The mapping properties file could not be read.");
				throw new DCMAApplicationException(
						"The mapping properties file could not be read. Either it doesnt exist or is opened in edit mode by some other user.",
						fileException);
			} catch (IOException ioException) {
				LOGGER.error("The mapping properties file could not be read, exception occured.");
				throw new DCMAApplicationException("The mapping properties file could not be read.", ioException);
			} finally {
				try {
					if (null != inputStream) {
						inputStream.close();
					}
				} catch (IOException ioException) {
					LOGGER.error("The mapping properties file not found on given property mapping file Path.");
					throw new DCMAApplicationException(
							EphesoftStringUtil.concatenate("Unable to close the input stream of the property file. "), ioException);
				}
			}
		} else {
			LOGGER.error("The mapping properties file not found on given property mapping file Path.");
			throw new DCMAApplicationException("The mapping properties file not found.");
		}
		LOGGER.trace("Exiting method to load properties map from property file.");
		return properties;
	}
}
