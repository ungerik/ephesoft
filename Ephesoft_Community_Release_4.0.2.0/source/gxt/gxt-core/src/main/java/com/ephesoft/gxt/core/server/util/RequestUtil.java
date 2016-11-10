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

package com.ephesoft.gxt.core.server.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * Provides methods for retrieving and setting data in session and request. The utility method provided automatically casts session and
 * request attributes to the required type.
 * 
 * <p>
 * <ul>
 * Following utilities have been provided:-
 * <li>Check for the existence of session variable.
 * <li>Fetch session and request attribute in many ways as follows:-
 * <ul>
 * <li>Directly fetch object of any type, e.g ,<code> T objectOfTypeT = RequestUtil.getSessionAttribute(request, key) </code>;
 * <li>Get list of any type of object, e.g List<T> listOfTypeT = RequestUtil.getSessionListAttribute(request, key);
 * <li>Get set of any type of object.
 * <li>Get map of any type of object.
 * </ul>
 * <li>Set session and request attributes.
 * <ul>
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RequestUtil {

	/**
	 * LOGGER for <code>RequestUtil</code> class.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(RequestUtil.class);

	/**
	 * Checks the existence of the given session variable.
	 * 
	 * <p>
	 * In case if any parameter provided is NULL, <code>false</code> is returned otherwise according to the availability.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request attribute from which session variable needs to be checked.
	 * @param attributeKey {@link String} Key of the attribute in session.
	 * @return <code>true/false</code>.
	 */
	public static boolean isSessionAttribute(final HttpServletRequest httpServletRequest, final String attributeKey) {
		LOGGER.debug("Checking for session attribute existence.");
		boolean isAvailable = false;
		if (null != httpServletRequest && !EphesoftStringUtil.isNullOrEmpty(attributeKey)) {
			isAvailable = (null == httpServletRequest.getSession().getAttribute(attributeKey)) ? false : true;
		}
		LOGGER.debug("Status of attribute ", attributeKey, " is: ", isAvailable);
		return isAvailable;
	}

	/**
	 * Gets the session attribute from the session by checking if the given attribute is available. It automatically cast the session
	 * attributes according to the type needed.
	 * 
	 * <p>
	 * In case attribute is not present or any parameter provided is <code> NULL </code>, it returns <code> NULL </code>.
	 * 
	 * <p>
	 * In case the type to be returned is not compatible or not correct with the session attribute, corresponding exception is logged
	 * and <code>NULL</code> is returned.
	 * 
	 * @param <T> type of session object to be returned.
	 * @param httpServletRequest {@link HttpServletRequest} request attribute from which session variable needs to fetched.
	 * @param attributeKey {@link String} Key of the attribute in session.
	 * @return session attribute of the given type or NULL in above mentioned cases.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getSessionAttribute(final HttpServletRequest httpServletRequest, final String attributeKey) {
		LOGGER.debug("Getting session attribute for the attribute key: ", attributeKey);
		T castType = null;
		if (isSessionAttribute(httpServletRequest, attributeKey)) {
			try {
				castType = (T) httpServletRequest.getSession().getAttribute(attributeKey);
			} catch (ClassCastException classCastException) {
				LOGGER.error(classCastException, "Type of attribute fetched from session is wrong. Attribute is: ",
						attributeKey);
			}
		}
		LOGGER.debug("Session attribute for the attribute key ", attributeKey, " is: ", castType);
		return castType;
	}

	/**
	 * Gets the session list attribute from the session by checking if the given attribute is available. It automatically cast the
	 * session list attributes according to the type needed.
	 * 
	 * <p>
	 * In case attribute is not present or any parameter provided is <code> NULL </code>, it returns <code> NULL </code>.
	 * 
	 * <p>
	 * In case the type to be returned is not compatible or not correct with the session attribute, corresponding exception is logged
	 * and <code>NULL</code> is returned.
	 * 
	 * @param <T> type of session list object to be returned.
	 * @param httpServletRequest {@link HttpServletRequest} request attribute from which session list needs to fetched.
	 * @param attributeKey {@link String} Key of the attribute in session.
	 * @return session list attribute of the given type or NULL in above mentioned cases.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getSessionListAttribute(final HttpServletRequest httpServletRequest, final String attributeKey) {
		LOGGER.debug("Getting session list attribute for the attribute key: ", attributeKey);
		List<T> castList = null;
		if (isSessionAttribute(httpServletRequest, attributeKey)) {
			try {
				castList = (List<T>) httpServletRequest.getSession().getAttribute(attributeKey);
			} catch (ClassCastException classCastException) {
				LOGGER.error(classCastException, "Type of attribute list fetched from session is wrong. Attribute is: ",
						attributeKey);
			}
		}
		LOGGER.debug("Session attribute list for the attribute key ", attributeKey, " is: ", castList);
		return castList;
	}

	/**
	 * Gets the session set attribute from the session by checking if the given attribute is available. It automatically cast the
	 * session set attribute according to the type needed.
	 * 
	 * <p>
	 * In case attribute is not present or any parameter provided is <code> NULL </code>, it returns <code> NULL </code>.
	 * 
	 * <p>
	 * In case the type to be returned is not compatible or not correct with the session attribute, corresponding exception is logged
	 * and <code>NULL</code> is returned.
	 * 
	 * @param <T> type of session set object to be returned.
	 * @param httpServletRequest {@link HttpServletRequest} request attribute from which session set needs to fetched.
	 * @param attributeKey {@link String} Key of the attribute in session.
	 * @return session set attribute of the given type or NULL in above mentioned cases.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> getSessionSetAttribute(final HttpServletRequest httpServletRequest, final String attributeKey) {
		LOGGER.debug("Getting session set attribute for the attribute key: ", attributeKey);
		Set<T> castSet = null;
		if (isSessionAttribute(httpServletRequest, attributeKey)) {
			try {
				castSet = (Set<T>) httpServletRequest.getSession().getAttribute(attributeKey);
			} catch (ClassCastException classCastException) {
				LOGGER.error(classCastException, "Type of attribute set fetched from session is wrong. Attribute is: ",
						attributeKey);
			}
		}
		LOGGER.debug("Session attribute set for the attribute key ", attributeKey, " is: ", castSet);
		return castSet;
	}

	/**
	 * Gets the session map attribute from the session by checking if the given attribute is available. It automatically cast the
	 * session map attribute according to the types needed.
	 * 
	 * <p>
	 * In case attribute is not present or any parameter provided is <code> NULL </code>, it returns <code> NULL </code>.
	 * 
	 * <p>
	 * In case the type to be returned is not compatible or not correct with the session attribute, corresponding exception is logged
	 * and <code>NULL</code> is returned.
	 * 
	 * @param <K> type of map key object.
	 * @param <V> type of map value object.
	 * @param httpServletRequest {@link HttpServletRequest} request attribute from which session map needs to fetched.
	 * @param attributeKey {@link String} Key of the attribute in session.
	 * @return session map attribute of the given type or NULL in above mentioned cases.
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> getSessionMapAttribute(final HttpServletRequest httpServletRequest, final String attributeKey) {
		LOGGER.debug("Getting session map attribute for the attribute key: ", attributeKey);
		Map<K, V> castMap = null;
		if (isSessionAttribute(httpServletRequest, attributeKey)) {
			try {
				castMap = (Map<K, V>) httpServletRequest.getSession().getAttribute(attributeKey);
			} catch (ClassCastException classCastException) {
				LOGGER.error(classCastException, "Type of attribute map fetched from session is wrong. Attribute is: ",
						attributeKey);
			}
		}
		LOGGER.debug("Session attribute map for the attribute key ", attributeKey, " is: ", castMap);
		return castMap;
	}

	/**
	 * Sets the attribute provided in the request session.
	 * 
	 * <p>
	 * Attribute is not set in session in case the parameters provided are <code>NULL</code>.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request in which attribute needs to be set in the session.
	 * @param attributeKey {@link String} attribute key.
	 * @param attributeValue {@link Object} attribute value.
	 */
	public static void setSessionAttribute(final HttpServletRequest httpServletRequest, final String attributeKey,
			final Object attributeValue) {
		LOGGER.debug("Setting session attribute ", attributeKey, " with value: ", attributeValue);
		if (null != httpServletRequest && !EphesoftStringUtil.isNullOrEmpty(attributeKey) && null != attributeValue) {
			httpServletRequest.getSession().setAttribute(attributeKey, attributeValue);
			LOGGER.debug("Setting of attribute was successful.");
		}
	}

	/**
	 * Gets the header value for the header name passed from the given request.
	 * 
	 * <p>
	 * In case any parameters provide are <code>NULL</code>, it returns <code>NULL</code> as header value.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request from which header value needs to be fetched.
	 * @param headerName {@link String} header name for the header value.
	 * @return header vale for the given parameters.
	 */
	public static String getAttributeFromHeader(final HttpServletRequest httpServletRequest, final String headerName) {
		String headerValue = null;
		LOGGER.debug("Getting request header attribute for the header name: ", headerName);
		if (null != httpServletRequest && !EphesoftStringUtil.isNullOrEmpty(headerName)) {
			headerValue = httpServletRequest.getHeader(headerName);
		}
		LOGGER.debug("Header attribute for the header name ", headerName, " is: ", headerValue);
		return headerValue;
	}

	/**
	 * Gets the request attribute from the session by checking if the given attribute is available. It automatically cast the request
	 * attributes according to the type needed.
	 * 
	 * <p>
	 * In case attribute is not present or any parameter provided is <code> NULL </code>, it returns <code> NULL </code>.
	 * 
	 * <p>
	 * In case the type to be returned is not compatible or not correct with the request attribute, corresponding exception is logged
	 * and <code>NULL</code> is returned.
	 * 
	 * @param <T> type of request object to be returned.
	 * @param httpServletRequest {@link HttpServletRequest} request attribute from which request variable needs to fetched.
	 * @param attributeKey {@link String} Key of the attribute in request.
	 * @return request attribute of the given type or NULL in above mentioned cases.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getRequestAttribute(final HttpServletRequest httpServletRequest, final String attributeKey) {
		LOGGER.debug("Getting request attribute for the attribute key: ", attributeKey);
		T attributeValue = null;
		if (null != httpServletRequest && !EphesoftStringUtil.isNullOrEmpty(attributeKey)) {
			try {
				attributeValue = (T) httpServletRequest.getAttribute(attributeKey);
			} catch (ClassCastException classCastException) {
				LOGGER.error(classCastException, "Type of attribute fetched from request is wrong. Attribute is: ",
						attributeKey);
			}
		}
		LOGGER
				.debug("Session attribute for the attribute key ", attributeKey, " is: ",
						attributeValue);
		return attributeValue;
	}

	/**
	 * Checks the existence of the given request variable.
	 * 
	 * <p>
	 * In case if any parameter provided is NULL, <code>false</code> is returned otherwise according to the availability.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request attribute from which request variable needs to be checked.
	 * @param attributeKey {@link String} Key of the attribute in request.
	 * @return <code>true/false</code>.
	 */
	public static boolean isRequestAttribute(final HttpServletRequest httpServletRequest, final String attributeKey) {
		LOGGER.debug("Checking for request attribute existence.");
		boolean isAvailable = false;
		if (null != httpServletRequest && !EphesoftStringUtil.isNullOrEmpty(attributeKey)) {
			isAvailable = (null == httpServletRequest.getAttribute(attributeKey)) ? false : true;
		}
		LOGGER.debug("Status of attribute ", attributeKey, " is: ", isAvailable);
		return isAvailable;
	}

	/**
	 * Gets the request list attribute from the request by checking if the given attribute is available. It automatically cast the
	 * request list attributes according to the type needed.
	 * 
	 * <p>
	 * In case attribute is not present or any parameter provided is <code> NULL </code>, it returns <code> NULL </code>.
	 * 
	 * <p>
	 * In case the type to be returned is not compatible or not correct with the request attribute, corresponding exception is logged
	 * and <code>NULL</code> is returned.
	 * 
	 * @param <T> type of request list object to be returned.
	 * @param httpServletRequest {@link HttpServletRequest} request attribute from which request list needs to fetched.
	 * @param attributeKey {@link String} Key of the attribute in request.
	 * @return request list attribute of the given type or NULL in above mentioned cases.
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> getRequestListAttribute(final HttpServletRequest httpServletRequest, final String attributeKey) {
		LOGGER.debug("Getting request list attribute for the attribute key: ", attributeKey);
		List<T> castList = null;
		if (isRequestAttribute(httpServletRequest, attributeKey)) {
			try {
				castList = (List<T>) httpServletRequest.getAttribute(attributeKey);
			} catch (ClassCastException classCastException) {
				LOGGER.error(classCastException, "Type of attribute list fetched from request is wrong. Attribute is: ",
						attributeKey);
			}
		}
		LOGGER.debug(EphesoftStringUtil.concatenate("Request attribute list for the attribute key ", attributeKey, " is: ", castList));
		return castList;
	}

	/**
	 * Gets the request set attribute from the request by checking if the given attribute is available. It automatically cast the
	 * request set attribute according to the type needed.
	 * 
	 * <p>
	 * In case attribute is not present or any parameter provided is <code> NULL </code>, it returns <code> NULL </code>.
	 * 
	 * <p>
	 * In case the type to be returned is not compatible or not correct with the request attribute, corresponding exception is logged
	 * and <code>NULL</code> is returned.
	 * 
	 * @param <T> type of request set object to be returned.
	 * @param httpServletRequest {@link HttpServletRequest} request attribute from which request set needs to fetched.
	 * @param attributeKey {@link String} Key of the attribute in request.
	 * @return request set attribute of the given type or NULL in above mentioned cases.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Set<T> getRequestSetAttribute(final HttpServletRequest httpServletRequest, final String attributeKey) {
		LOGGER.debug("Getting request set attribute for the attribute key: ", attributeKey);
		Set<T> castSet = null;
		if (isRequestAttribute(httpServletRequest, attributeKey)) {
			try {
				castSet = (Set<T>) httpServletRequest.getAttribute(attributeKey);
			} catch (ClassCastException classCastException) {
				LOGGER.error(classCastException, "Type of attribute set fetched from request is wrong. Attribute is: ",
						attributeKey);
			}
		}
		LOGGER.debug("Request attribute set for the attribute key ", attributeKey, " is: ", castSet);
		return castSet;
	}

	/**
	 * Gets the request map attribute from the request by checking if the given attribute is available. It automatically cast the
	 * request map attribute according to the types needed.
	 * 
	 * <p>
	 * In case attribute is not present or any parameter provided is <code> NULL </code>, it returns <code> NULL </code>.
	 * 
	 * <p>
	 * In case the type to be returned is not compatible or not correct with the request attribute, corresponding exception is logged
	 * and <code>NULL</code> is returned.
	 * 
	 * @param <K> type of map key object.
	 * @param <V> type of map value object.
	 * @param httpServletRequest {@link HttpServletRequest} request attribute from which request map needs to fetched.
	 * @param attributeKey {@link String} Key of the attribute in request.
	 * @return request map attribute of the given type or NULL in above mentioned cases.
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> getRequestMapAttribute(final HttpServletRequest httpServletRequest, final String attributeKey) {
		LOGGER.debug("Getting request map attribute for the attribute key: ", attributeKey);
		Map<K, V> castMap = null;
		if (isRequestAttribute(httpServletRequest, attributeKey)) {
			try {
				castMap = (Map<K, V>) httpServletRequest.getAttribute(attributeKey);
			} catch (ClassCastException classCastException) {
				LOGGER.error(classCastException, "Type of attribute map fetched from request is wrong. Attribute is: ",
						attributeKey);
			}
		}
		LOGGER.debug("Request attribute map for the attribute key ", attributeKey, " is: ", castMap);
		return castMap;
	}

	/**
	 * Sets the attribute provided in the request.
	 * 
	 * <p>
	 * Attribute is not set in request in case the parameters provided are <code>NULL</code>.
	 * 
	 * @param httpServletRequest {@link HttpServletRequest} request in which attribute needs to be set.
	 * @param attributeKey {@link String} attribute key.
	 * @param attributeValue {@link Object} attribute value.
	 */
	public static void setRequestAttribute(final HttpServletRequest httpServletRequest, final String attributeKey,
			final Object attributeValue) {
		LOGGER.debug("Setting request attribute ", attributeKey, " with value: ", attributeValue);
		if (null != httpServletRequest && !EphesoftStringUtil.isNullOrEmpty(attributeKey) && null != attributeValue) {
			httpServletRequest.setAttribute(attributeKey, attributeValue);
			LOGGER.debug("Setting of attribute was successful.");
		}
	}

}
