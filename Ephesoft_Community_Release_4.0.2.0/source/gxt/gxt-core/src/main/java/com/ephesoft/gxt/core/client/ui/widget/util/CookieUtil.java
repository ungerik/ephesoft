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

package com.ephesoft.gxt.core.client.ui.widget.util;

import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.user.client.Cookies;

public final class CookieUtil {

	private static final String LEFT_COOKIE_PARAMETER = "_LEFT";
	private static final String TOP_COOKIE_PARAMETER = "_TOP";
	private static final String WIDTH_COOKIE_PARAMETER = "_WIDTH";
	private static final String HEIGHT_COOKIE_PARAMETER = "_HEIGHT";

	public static String getCookieNameForAbsoluteLeft(String elementIdentifier) {
		return getCookieName(elementIdentifier, LEFT_COOKIE_PARAMETER);
	}

	public static String getCookieNameForAbsoluteTop(String elementIdentifier) {
		return getCookieName(elementIdentifier, TOP_COOKIE_PARAMETER);

	}

	public static String getCookieNameForWidth(String elementIdentifier) {
		return getCookieName(elementIdentifier, WIDTH_COOKIE_PARAMETER);

	}

	public static String getCookieNameForHeight(String elementIdentifier) {
		return getCookieName(elementIdentifier, HEIGHT_COOKIE_PARAMETER);
	}

	private static String getCookieName(String elementIdentifier, String concatenationParameter) {
		String cookieName = null;
		if (!StringUtil.isNullOrEmpty(elementIdentifier) && !StringUtil.isNullOrEmpty(concatenationParameter)) {
			cookieName = StringUtil.concatenate(elementIdentifier, concatenationParameter);
		}
		return cookieName;
	}

	public static String getCookieValue(String cookieName) {
		return Cookies.getCookie(cookieName);
	}

	public static void storeCookie(String cookieName, String value) {
		 Cookies.setCookie(cookieName, value);
	}
}
