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

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

/**
 * Simple {@code RestErrorConverter} implementation that creates a new Map instance based on the specified RestError instance. Some
 * {@link org.springframework.http.converter.HttpMessageConverter HttpMessageConverter}s (like a JSON converter) can easily
 * automatically convert Maps to response bodies. The map key names are customizable via setter methods (setStatusKey, setMessageKey,
 * etc).
 * 
 * @author Ephesoft
 * @version 3.0
 * @see RestErrorConverter
 */
public class MapRestErrorConverter implements RestErrorConverter<Map> {

	/**
	 * Constant for status.
	 */
	private static final String DEFAULT_STATUS_KEY = "status";
	/**
	 * Constant for code.
	 */
	private static final String DEFAULT_CODE_KEY = "code";
	/**
	 * Constant for message.
	 */
	private static final String DEFAULT_MESSAGE_KEY = "message";
	/**
	 * Constant for developers message.
	 */
	private static final String DEFAULT_DEVELOPER_MESSAGE_KEY = "developerMessage";
	/**
	 * Constant for info url.
	 */
	private static final String DEFAULT_MORE_INFO_URL_KEY = "moreInfoUrl";
	/**
	 * Key for status.
	 */
	private String statusKey = DEFAULT_STATUS_KEY;
	/**
	 * Key for code.
	 */
	private String codeKey = DEFAULT_CODE_KEY;
	/**
	 * Key for message.
	 */
	private String messageKey = DEFAULT_MESSAGE_KEY;
	/**
	 * Key for developers message.
	 */
	private String developerMessageKey = DEFAULT_DEVELOPER_MESSAGE_KEY;
	/**
	 * Key for info url.
	 */
	private String moreInfoUrlKey = DEFAULT_MORE_INFO_URL_KEY;

	@Override
	public Map convert(final RestError restError) {
		final Map<String, Object> map = createMap();
		final HttpStatus status = restError.getStatus();
		map.put(getStatusKey(), status.value());

		final int code = restError.getCode();
		if (code > 0) {
			map.put(getCodeKey(), code);
		}

		final String message = restError.getMessage();
		if (message != null) {
			map.put(getMessageKey(), message);
		}

		final String devMsg = restError.getDeveloperMessage();
		if (devMsg != null) {
			map.put(getDeveloperMessageKey(), devMsg);
		}

		final String moreInfoUrl = restError.getMoreInfoUrl();
		if (moreInfoUrl != null) {
			map.put(getMoreInfoUrlKey(), moreInfoUrl);
		}

		return map;
	}

	protected Map<String, Object> createMap() {
		return new LinkedHashMap<String, Object>();
	}

	public String getStatusKey() {
		return statusKey;
	}

	public void setStatusKey(final String statusKey) {
		this.statusKey = statusKey;
	}

	public String getCodeKey() {
		return codeKey;
	}

	public void setCodeKey(final String codeKey) {
		this.codeKey = codeKey;
	}

	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(final String messageKey) {
		this.messageKey = messageKey;
	}

	public String getDeveloperMessageKey() {
		return developerMessageKey;
	}

	public void setDeveloperMessageKey(final String developerMessageKey) {
		this.developerMessageKey = developerMessageKey;
	}

	public String getMoreInfoUrlKey() {
		return moreInfoUrlKey;
	}

	public void setMoreInfoUrlKey(final String moreInfoUrlKey) {
		this.moreInfoUrlKey = moreInfoUrlKey;
	}
}
