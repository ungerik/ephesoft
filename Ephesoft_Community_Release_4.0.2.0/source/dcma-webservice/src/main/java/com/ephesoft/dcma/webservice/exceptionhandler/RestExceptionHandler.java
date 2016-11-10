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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

/**
 * <ol>
 * <li>Upon encountering an Exception, the configured {@link RestErrorResolver} is consulted to resolve the exception into a
 * {@link RestError} instance.</li>
 * <li>The HTTP Response's Status Code will be set to the {@code RestError}
 * <li>The {@code RestError} instance is presented to a configured {@link RestErrorConverter} to allow transforming the {@code
 * RestError} instance into an object potentially more suitable for rendering as the HTTP response body.</li>
 * <li>The {@link #setMessageConverters(org.springframework.http.converter.HttpMessageConverter[]) HttpMessageConverters} are consulted
 * (in iteration order) with this object result for rendering. The first {@code HttpMessageConverter} instance that
 * {@link HttpMessageConverter#canWrite(Class, org.springframework.http.MediaType) canWrite} the object based on the request's
 * supported {@code MediaType}s will be used to render this result object as the HTTP response body.</li>
 * <li>If no {@code HttpMessageConverter}s {@code canWrite} the result object, nothing is done, and this handler returns {@code null}
 * to indicate other ExceptionResolvers potentially further in the resolution chain should handle the exception instead.</li>
 * </ol>
 * 
 * @see DefaultRestErrorResolver
 * @see MapRestErrorConverter
 * @see HttpMessageConverter
 * @see org.springframework.http.converter.json.MappingJacksonHttpMessageConverter MappingJacksonHttpMessageConverter
 * 
 */
public class RestExceptionHandler extends AbstractHandlerExceptionResolver implements InitializingBean {

	/**
	 * Used for proper logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);
	/**
	 * Instance of {@link RestErrorResolver}.
	 */
	private RestErrorResolver errorResolver;
	/**
	 * Instance of {@link RestErrorConverter}.
	 */
	private RestErrorConverter<?> errorConverter;

	public RestExceptionHandler() {
		super();
		this.errorResolver = new DefaultRestErrorResolver();
		this.errorConverter = new MapRestErrorConverter();
	}

	public void setErrorResolver(final RestErrorResolver errorResolver) {
		this.errorResolver = errorResolver;
	}

	public RestErrorResolver getErrorResolver() {
		return this.errorResolver;
	}

	public RestErrorConverter<?> getErrorConverter() {
		return errorConverter;
	}

	public void setErrorConverter(final RestErrorConverter<?> errorConverter) {
		this.errorConverter = errorConverter;
	}

	@Override
	public void afterPropertiesSet() {
		LOGGER.debug("Properties are set.");
	}

	/**
	 * Actually resolve the given exception that got thrown during on handler execution, returning a ModelAndView that represents a
	 * specific error page if appropriate.
	 * <p/>
	 * May be overridden in subclasses, in order to apply specific exception checks. Note that this template method will be invoked
	 * <i>after</i> checking whether this resolved applies ("mappedHandlers" etc), so an implementation may simply proceed with its
	 * actual exception handling.
	 * 
	 * @param request current HTTP request
	 * @param response current HTTP response
	 * @param handler the executed handler, or <code>null</code> if none chosen at the time of the exception (for example, if multipart
	 *            resolution failed)
	 * @param exception the exception that got thrown during handler execution
	 * @return a corresponding ModelAndView to forward to, or <code>null</code> for default processing
	 */
	@Override
	protected ModelAndView doResolveException(final HttpServletRequest request, final HttpServletResponse response,
			final Object handler, final Exception exception) {
		return null;
	}
}
