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

/**
 * 
 */
package com.ephesoft.dcma.user.connectivity.util;

import java.util.NoSuchElementException;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.user.connectivity.constant.UserConnectivityConstant;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 *
 */
public class UserConnectivityUtil {

	/**
	 * Used for handling logs.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(UserConnectivityUtil.class);

	public static String searchUser(DirContext dctx, final SearchControls constraints, final String paramName, final String paramConfig) {
		String result = "";
		NamingEnumeration<SearchResult> searchResults = null;
		try {
			LOG.info(EphesoftStringUtil.concatenate("Searching user with params = ", paramName, " under context directory = ",
					paramConfig));
			// perform directory search for the parameters
			searchResults = dctx.search(paramConfig, paramName, constraints);

			// parse the results
			result = parseUserSearchResult(searchResults);
		} catch (NamingException namingExp) {
			LOG.error(namingExp.getMessage(), namingExp);
		} catch (final NoSuchElementException noSuchEleExp) {
			LOG.error(noSuchEleExp.getMessage(), noSuchEleExp);
		}
		return result;
	}

	public static String[] getUserSearchAttributes() {
		final String[] attrIDs = new String[3];
		attrIDs[0] = UserConnectivityConstant.GIVEN_NAME;
		attrIDs[1] = UserConnectivityConstant.USER_NAME_INITIALS;
		attrIDs[2] = UserConnectivityConstant.SUR_NAME;
		return attrIDs;
	}

	private static String parseUserSearchResult(final NamingEnumeration<SearchResult> answer) throws NamingException {
		String result = "";
		if (answer.hasMore()) {
			final Attributes userAttrs = ((SearchResult) answer.next()).getAttributes();

			if (userAttrs != null) {
				final Attribute givenName = (userAttrs.get(UserConnectivityConstant.GIVEN_NAME));
				final Attribute initials = (userAttrs.get(UserConnectivityConstant.USER_NAME_INITIALS));
				final Attribute surName = (userAttrs.get(UserConnectivityConstant.SUR_NAME));
				final Attribute mail = (userAttrs.get(UserConnectivityConstant.MAIL));
				StringBuilder stringBuilder = new StringBuilder();
				if (null != givenName) {
					final String firstName = (String) givenName.get();
					if (!EphesoftStringUtil.isNullOrEmpty(firstName)) {
						stringBuilder.append(firstName);
					}
				}
				if (null != initials) {
					final String initial = (String) initials.get();
					stringBuilder.append(UserConnectivityConstant.WHITE_SPACE);
					if (!EphesoftStringUtil.isNullOrEmpty(initial)) {
						stringBuilder.append(initial);
					}
				}
				if (null != surName) {
					final String lastName = (String) surName.get();
					stringBuilder.append(UserConnectivityConstant.WHITE_SPACE);
					if (!EphesoftStringUtil.isNullOrEmpty(lastName)) {
						stringBuilder.append(lastName);
					}
				}
				if (null != mail) {
					final String email = (String) mail.get();
					if (!EphesoftStringUtil.isNullOrEmpty(email)) {
						stringBuilder.append(email);
					}
				}
				result = stringBuilder.toString().trim();
			}
			LOG.info(EphesoftStringUtil.concatenate("User found ", result));
		} else {
			LOG.info("No user found.");
		}
		return result;
	}
}
