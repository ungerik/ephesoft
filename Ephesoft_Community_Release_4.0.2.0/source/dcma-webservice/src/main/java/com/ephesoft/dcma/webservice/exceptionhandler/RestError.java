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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;

/**
 * This class is the pojo for the properties that error has to carry.
 * 
 * @author Ephesoft
 * @version 3.0
 */
public class RestError {

	/**
	 * Used for proper logging.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(RestError.class);
	/**
	 * HTTP Status error category for exception.
	 */
	private final HttpStatus status;
	/**
	 * Custom code for exact exception caught.
	 */
	private final int code;
	/**
	 * Message for clients.
	 */
	private final String message;
	/**
	 * Developers message for full description of exception.
	 */
	private final String developerMessage;
	/**
	 * URL for wiki page with description of custom errors.
	 */
	private final String moreInfoUrl;

	/**
	 * Parameterized constructor.
	 * 
	 * @param status {@link HttpStatus}
	 * @param code int
	 * @param message {@link String}
	 * @param developerMessage {@link String}
	 * @param moreInfoUrl {@link String}
	 */
	public RestError(final HttpStatus status, final int code, final String message, final String developerMessage,
			final String moreInfoUrl) {
		if (status == null) {
			LOGGER.error("HttpStatus argument cannot be null. Setting the status to null.");
		}
		this.status = status;
		this.code = code;
		this.message = message;
		this.developerMessage = developerMessage;
		this.moreInfoUrl = moreInfoUrl;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDeveloperMessage() {
		return developerMessage;
	}

	public String getMoreInfoUrl() {
		return moreInfoUrl;
	}

	@Override
	public boolean equals(final Object obj) {
		boolean returnVal = false;
		if (this == obj) {
			returnVal = true;
		}
		if (obj instanceof RestError) {
			final RestError restError = (RestError) obj;
			returnVal = ObjectUtils.nullSafeEquals(getStatus(), restError.getStatus()) && getCode() == restError.getCode()
					&& ObjectUtils.nullSafeEquals(getMessage(), restError.getMessage())
					&& ObjectUtils.nullSafeEquals(getDeveloperMessage(), restError.getDeveloperMessage())
					&& ObjectUtils.nullSafeEquals(getMoreInfoUrl(), restError.getMoreInfoUrl());
		}

		return returnVal;
	}

	@Override
	public int hashCode() {
		return ObjectUtils.nullSafeHashCode(new Object[] {getStatus(), getCode(), getMessage(), getDeveloperMessage(),
				getMoreInfoUrl()});
	}

	public String toString() {
		return new StringBuilder().append(getStatus().value()).append(" (").append(getStatus().toString()).append(" )")
				.toString();
	}

	/**
	 * Builder class to provide the population of {@link RestError} object. This class is used for implementation of builder design
	 * pattern for {@link RestError} object.
	 * 
	 * @author Ephesoft
	 * @version 3.0
	 * @see RestError
	 */
	public static class Builder {

		/**
		 * {@link HttpStatus} status for rest error code.
		 */
		private HttpStatus status;
		/**
		 * Custom code for {@link HttpStatus} rest error received.
		 */
		private int code;
		/**
		 * String message for rest error.
		 */
		private String message;
		/**
		 * Developers message for http status error code.
		 */
		private String developerMessage;
		/**
		 * String url for more information on error.
		 */
		private String moreInfoUrl;

		public Builder setStatus(final int statusCode) {
			this.status = HttpStatus.valueOf(statusCode);
			return this;
		}

		public Builder setStatus(final HttpStatus status) {
			this.status = status;
			return this;
		}

		public Builder setCode(final int code) {
			this.code = code;
			return this;
		}

		public Builder setMessage(final String message) {
			this.message = message;
			return this;
		}

		public Builder setDeveloperMessage(final String developerMessage) {
			this.developerMessage = developerMessage;
			return this;
		}

		public Builder setMoreInfoUrl(final String moreInfoUrl) {
			this.moreInfoUrl = moreInfoUrl;
			return this;
		}

		/**
		 * Method to implement creation of RestError object.
		 * 
		 * @return {@link RestError}
		 */
		public RestError build() {
			if (this.status == null) {
				this.status = HttpStatus.INTERNAL_SERVER_ERROR;
			}
			return new RestError(this.status, this.code, this.message, this.developerMessage, this.moreInfoUrl);
		}
	}
}
