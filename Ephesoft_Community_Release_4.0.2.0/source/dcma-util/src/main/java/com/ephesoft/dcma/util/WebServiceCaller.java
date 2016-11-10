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

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;

import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * APIs to call Webservices
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class WebServiceCaller {

	/**
	 * Initialising logger {@link EphesoftLogger}.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(WebServiceCaller.class);

	/**
	 * API to call Webservice target. It will take an int values that will specify the number of time the Webservice need to be tried
	 * before leaving off.
	 * 
	 * @param serviceURL
	 * @param numberOfPings
	 * @return
	 */
	public static boolean callWebservice(final String serviceURL, final int numberOfPings) {
		LOGGER.debug("Calling webservice in call webservice method");
		boolean isActive = false;
		LOGGER.info(serviceURL);

		for (int pingCount = 0; numberOfPings > pingCount; pingCount++) {
			isActive = hitWebservice(serviceURL);
			if (isActive) {
				break;
			}
		}
		LOGGER.info(EphesoftStringUtil.concatenate("Service execution status:  ", isActive));
		return isActive;
	}

	/**
	 * Api to simply hit webservice. It will return true or false if Webservice hit was successful or unsuccessful respectively
	 * 
	 * @param serviceURL
	 * @return
	 */
	private static boolean hitWebservice(final String serviceURL) {
		LOGGER.debug("Giving a hit at webservice URL.");
		boolean isActive = false;
		if (!EphesoftStringUtil.isNullOrEmpty(serviceURL)) {

			// Create an instance of HttpClient.
			HttpClient client = new HttpClient();

			// Create a method instance.
			PostMethod method = new PostMethod(serviceURL);

			try {
				// Execute the method.
				int statusCode = client.executeMethod(method);

				if (statusCode == HttpStatus.SC_OK) {
					isActive = true;
				} else {
					LOGGER.info(EphesoftStringUtil.concatenate("Execute Method failed: ", method.getStatusLine()));
				}
			} catch (HttpException httpException) {
				LOGGER.error(EphesoftStringUtil.concatenate("Fatal protocol violation: ", httpException.getMessage()));
			} catch (IOException ioException) {
				LOGGER.error(EphesoftStringUtil.concatenate("Fatal transport error: ", ioException.getMessage()));
			} finally {
				// Release the connection.
				if (method != null) {
					method.releaseConnection();
				}
			}
		}
		return isActive;
	}

}
