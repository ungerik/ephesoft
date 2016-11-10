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

package com.ephesoft.dcma.webservice.exceptionhandler;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

/**
 * Default {@code RestErrorResolver} implementation that converts discovered Exceptions to {@link RestError} instances.
 * 
 * @author Ephesoft
 * @version 3.0
 */
public class DefaultRestErrorResolver implements RestErrorResolver, MessageSourceAware, InitializingBean {

	/**
	 * Default exception message.
	 */
	public static final String DEFAULT_EXCEPTION_MSG = "_exmsg";
	/**
	 * Default users message.
	 */
	public static final String DEFAULT_MESSAGE = "_msg";
	/**
	 * Logger for proper logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRestErrorResolver.class);
	/**
	 * Map to store default exception and their rest error instance.
	 */
	private Map<String, RestError> exceptionMappings = Collections.emptyMap();
	/**
	 * Map to store the mappings definitions for exceptions.
	 */
	private Map<String, String> exceptionMappingDefinitions = Collections.emptyMap();
	/**
	 * Default url value.
	 */
	private String defaultInfoUrl;
	/**
	 * Default empty code status.
	 */
	private boolean defaultEmptyStatus;
	/**
	 * Default developers message.
	 */
	private String defaultDeveloperMessage;

	public DefaultRestErrorResolver() {
		this.defaultEmptyStatus = true;
		this.defaultDeveloperMessage = DEFAULT_EXCEPTION_MSG;
	}

	public void setExceptionMappingDefinitions(final Map<String, String> exceptionMappingDefinitions) {
		this.exceptionMappingDefinitions = exceptionMappingDefinitions;
	}

	public void setDefaultMoreInfoUrl(final String defaultMoreInfoUrl) {
		this.defaultInfoUrl = defaultMoreInfoUrl;
	}

	public void setDefaultEmptyCodeToStatus(final boolean defaultEmptyCodeToStatus) {
		this.defaultEmptyStatus = defaultEmptyCodeToStatus;
	}

	public void setDefaultDeveloperMessage(final String defaultDeveloperMessage) {
		this.defaultDeveloperMessage = defaultDeveloperMessage;
	}

	@Override
	public void afterPropertiesSet() {

		// populate with some defaults:
		final Map<String, String> definitions = createDefaultExceptionMappingDefinitions();

		// add in user-specified mappings (will override defaults as necessary):
		if (this.exceptionMappingDefinitions != null && !this.exceptionMappingDefinitions.isEmpty()) {
			definitions.putAll(this.exceptionMappingDefinitions);
		}
		// Get rest error definitions for exceptions for mapping.
		this.exceptionMappings = toRestErrors(definitions);
	}

	/**
	 * This method is used to create default exception mapping for some default {@link HttpStatus} error codes.
	 * 
	 * @return {@link Map} This map is used to extract the particular exception when any exception is thrown.
	 */
	protected final Map<String, String> createDefaultExceptionMappingDefinitions() {

		final Map<String, String> exceptionMap = new LinkedHashMap<String, String>();

		// 400
		applyDef(exceptionMap, HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST);
		applyDef(exceptionMap, MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST);
		applyDef(exceptionMap, TypeMismatchException.class, HttpStatus.BAD_REQUEST);

		// 404
		applyDef(exceptionMap, NoSuchRequestHandlingMethodException.class, HttpStatus.NOT_FOUND);

		// 405
		applyDef(exceptionMap, HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED);

		// 406
		applyDef(exceptionMap, HttpMediaTypeNotAcceptableException.class, HttpStatus.NOT_ACCEPTABLE);

		return exceptionMap;
	}

	private void applyDef(final Map<String, String> exceptionMap, final Class clazz, final HttpStatus status) {
		applyDef(exceptionMap, clazz.getName(), status);
	}

	private void applyDef(final Map<String, String> exceptionMap, final String key, final HttpStatus status) {
		exceptionMap.put(key, definitionFor(key, status));
	}

	private String definitionFor(final String key, final HttpStatus status) {
		return status.value() + ", " + key + ", " + DEFAULT_EXCEPTION_MSG;
	}

	/**
	 * This method is used for creating the {@link RestError} template for the received exception.
	 * 
	 * @param request {@link ServletWebRequest}
	 * @param handler {@link Object}
	 * @param exception {@link Exception}
	 * @return {@link RestError} This {@link RestError} instance carries the created object for received exception.
	 */
	@Override
	public RestError resolveError(final ServletWebRequest request, final Object handler, final Exception exception) {
		RestError returnTemplate = null;
		final RestError template = getRestErrorTemplate(exception);
		if (template != null) {
			final RestError.Builder builder = new RestError.Builder();
			builder.setStatus(getStatusValue(template, request, exception));
			builder.setCode(getCode(template, request, exception));
			builder.setMoreInfoUrl(getMoreInfoUrl(template, request, exception));

			String msg = getMessage(template, request, exception);
			if (msg != null) {
				builder.setMessage(msg);
			}
			msg = getDeveloperMessage(template, request, exception);
			if (msg != null) {
				builder.setDeveloperMessage(msg);
			}
			returnTemplate = builder.build();
		}

		return returnTemplate;
	}

	/**
	 * This method is used to return the status value for {@link RestError} template with respect to exception.
	 * 
	 * @param template {@link RestError} The rest error template from which status has to be extracted.
	 * @param request {@link ServletWebRequest}
	 * @param exception {@link Exception}
	 * @return int The status value for http code.
	 */
	protected int getStatusValue(final RestError template, final ServletWebRequest request, final Exception exception) {
		return template.getStatus().value();
	}

	/**
	 * This method is used to return the user code value for {@link RestError} template with respect to exception.
	 * 
	 * @param template {@link RestError} The rest error template from which code has to be extracted.
	 * @param request {@link ServletWebRequest}
	 * @param exception {@link Exception}
	 * @return int The code for this template.
	 */
	protected int getCode(final RestError template, final ServletWebRequest request, final Exception exception) {
		int code = template.getCode();
		if (code <= 0 && defaultEmptyStatus) {
			code = getStatusValue(template, request, exception);
		}
		return code;
	}

	/**
	 * This method is used to return the user info url for {@link RestError} template with respect to exception.
	 * 
	 * @param template {@link RestError} The rest error template from which info url has to be extracted.
	 * @param request {@link ServletWebRequest}
	 * @param exception {@link Exception}
	 * @return {@link String} The info url for this template.
	 */
	protected String getMoreInfoUrl(final RestError template, final ServletWebRequest request, final Exception exception) {
		String moreInfoUrl = template.getMoreInfoUrl();
		if (moreInfoUrl == null) {
			moreInfoUrl = this.defaultInfoUrl;
		}
		return moreInfoUrl;
	}

	/**
	 * This method is used to return the user message for {@link RestError} template with respect to exception.
	 * 
	 * @param template {@link RestError} The rest error template from which message has to be extracted.
	 * @param request {@link ServletWebRequest}
	 * @param exception
	 * @return {@link String} The message to be returned.
	 */
	protected String getMessage(final RestError template, final ServletWebRequest request, final Exception exception) {
		return getMessage(template.getMessage(), request, exception);
	}

	/**
	 * This method is used to return the developers message for {@link RestError} template with respect to exception.
	 * 
	 * @param template {@link RestError} The rest error template from which message has to be extracted.
	 * @param request {@link ServletWebRequest}
	 * @param exception {@link Exception}
	 * @return {@link String} The developers message to be returned.
	 */
	protected String getDeveloperMessage(final RestError template, final ServletWebRequest request, final Exception exception) {
		String devMsg = template.getDeveloperMessage();
		if (devMsg == null && defaultDeveloperMessage != null) {
			devMsg = defaultDeveloperMessage;
		}
		if (DEFAULT_MESSAGE.equals(devMsg)) {
			devMsg = template.getMessage();
		}
		return getMessage(devMsg, request, exception);
	}

	/**
	 * Returns the response status message to return to the client, or {@code null} if no status message should be returned.
	 * 
	 * @param msg {@link String}
	 * @param webRequest {@link ServletWebRequest}
	 * @param exception {@link Exception}
	 * @return {@link String} the response status message to return to the client, or {@code null} if no status message should be
	 *         returned.
	 */
	protected String getMessage(final String msg, final ServletWebRequest webRequest, final Exception exception) {
		String message = msg;
		if (msg != null) {
			if (message.equalsIgnoreCase("null") || message.equalsIgnoreCase("off")) {
				message = null;
			}
			if (message.equalsIgnoreCase(DEFAULT_EXCEPTION_MSG)) {
				message = exception.getMessage();
			}
		}
		return message;
	}

	/**
	 * Returns the config-time 'template' RestError instance configured for the specified Exception, or {@code null} if a match was not
	 * found.
	 * <p/>
	 * The config-time template is used as the basis for the RestError constructed at runtime.
	 * 
	 * @param exception {@link Exception}
	 * @return {@link RestError} the template to use for the RestError instance to be constructed.
	 */
	private RestError getRestErrorTemplate(final Exception exception) {
		RestError template = null;
		final Map<String, RestError> mappings = this.exceptionMappings;
		if (!CollectionUtils.isEmpty(mappings)) {
			String dominantMapping = null;
			int deepest = Integer.MAX_VALUE;
			for (final Map.Entry<String, RestError> entry : mappings.entrySet()) {
				final String key = entry.getKey();
				final int depth = getDepth(key, exception);
				if (depth >= 0 && depth < deepest) {
					deepest = depth;
					dominantMapping = key;
					template = entry.getValue();
				}
			}
			if (template != null && LOGGER.isDebugEnabled()) {
				LOGGER.debug("Resolving to RestError template '" + template + "' for exception of type ["
						+ exception.getClass().getName() + "], based on exception mapping [" + dominantMapping + "]");
			}
		}
		return template;
	}

	/**
	 * Return the depth to the superclass matching.
	 * <p>
	 * 0 means exception matches exactly. Returns -1 if there's no match. Otherwise, returns depth. Lowest depth wins.
	 * 
	 * @param exceptionMapping {@link String}
	 * @param exception {@link Exception}
	 * @return int
	 */
	protected int getDepth(final String exceptionMapping, final Exception exception) {
		return getDepth(exceptionMapping, exception.getClass(), 0);
	}

	private int getDepth(final String exceptionMapping, final Class exceptionClass, final int depth) {
		if (exceptionClass.getName().contains(exceptionMapping)) {
			// Found it!
			return depth;
		}
		// If we've gone as far as we can go and haven't found it...
		if (exceptionClass.equals(Throwable.class)) {
			return -1;
		}
		return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
	}

	/**
	 * This method is used to return all the {@link RestError} instances mapped with a key.
	 * 
	 * @param smap {@link Map} this map stores the default mapping for the defined exceptions.
	 * @return {@link Map} map that contains a key and its mapped {@link RestError} template for it.
	 */
	protected Map<String, RestError> toRestErrors(final Map<String, String> smap) {
		if (CollectionUtils.isEmpty(smap)) {
			return Collections.emptyMap();
		}
		final Map<String, RestError> map = new LinkedHashMap<String, RestError>(smap.size());
		for (final Map.Entry<String, String> entry : smap.entrySet()) {
			final String key = entry.getKey();
			final String value = entry.getValue();
			final RestError template = toRestError(value);
			map.put(key, template);
		}

		return map;
	}

	/**
	 * This method forms a {@link RestError} template for the given exception mapping value.
	 * 
	 * @param exceptionConfig {@link String} this string carries the value from exception mapping map for a key depicting the
	 *            exception.
	 * @return {@link RestError} the formed rest error template for defined mappings.
	 */
	protected RestError toRestError(final String exceptionConfig) {
		final String[] values = StringUtils.commaDelimitedListToStringArray(exceptionConfig);
		if (values == null || values.length == 0) {
			throw new IllegalStateException("Invalid config mapping. Exception names must map to a string configuration.");
		}

		final RestError.Builder builder = new RestError.Builder();

		boolean statusSet = false;
		boolean codeSet = false;
		boolean msgSet = false;
		boolean devMsgSet = false;
		boolean moreInfoSet = false;

		for (final String value : values) {

			final String trimmedVal = StringUtils.trimWhitespace(value);
			// not a key/value pair - use heuristics to determine what value is being set:
			int val;
			if (!statusSet) {
				val = getInt("status", trimmedVal);
				if (val > 0) {
					builder.setStatus(val);
					statusSet = true;
					continue;
				}
			}
			if (!codeSet) {
				val = getInt("code", trimmedVal);
				if (val > 0) {
					builder.setCode(val);
					codeSet = true;
					continue;
				}
			}
			if (!moreInfoSet && trimmedVal.toLowerCase().startsWith("http")) {
				builder.setMoreInfoUrl(trimmedVal);
				moreInfoSet = true;
				continue;
			}
			if (!msgSet) {
				builder.setMessage(trimmedVal);
				msgSet = true;
				continue;
			}
			if (!devMsgSet) {
				builder.setDeveloperMessage(trimmedVal);
				devMsgSet = true;
				continue;
			}
			if (!moreInfoSet) {
				builder.setMoreInfoUrl(trimmedVal);
				moreInfoSet = true;
			}
		}

		return builder.build();
	}

	private static int getRequiredInt(final String key, final String value) {
		try {
			final int anInt = Integer.valueOf(value);
			return Math.max(-1, anInt);
		} catch (final NumberFormatException e) {
			final String msg = "Configuration element '" + key + "' requires an integer value. The value " + "specified: " + value;
			throw new IllegalArgumentException(msg, e);
		}
	}

	private static int getInt(final String key, final String value) {
		int returnVal;
		try {
			returnVal = getRequiredInt(key, value);
		} catch (final IllegalArgumentException iae) {
			returnVal = 0;
		}
		return returnVal;
	}

	@Override
	public void setMessageSource(final MessageSource messageSource) {
		// TODO Auto-generated method stub

	}
}
