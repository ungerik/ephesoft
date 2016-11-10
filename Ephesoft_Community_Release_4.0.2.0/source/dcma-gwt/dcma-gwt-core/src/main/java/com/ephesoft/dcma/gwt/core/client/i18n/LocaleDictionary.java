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

package com.ephesoft.dcma.gwt.core.client.i18n;

import java.util.List;
import java.util.MissingResourceException;

import com.ephesoft.dcma.gwt.core.client.util.MessageFormat;
import com.google.gwt.i18n.client.Dictionary;

public class LocaleDictionary {

	private static LocaleDictionary localeDictionary;

	private static CommonLocaleInfo commonLocaleInfo;
	private static LocaleInfo pageLocaleInfo;

	private LocaleDictionary() {
	}

	public static LocaleDictionary create(LocaleInfo localeInfo) {
		commonLocaleInfo = CommonLocaleInfo.get(localeInfo.getLocale());
		pageLocaleInfo = localeInfo;
		if (localeDictionary == null) {
			localeDictionary = new LocaleDictionary();
		}
		return localeDictionary;
	}

	public static LocaleDictionary get() {
		if (localeDictionary == null)
			throw new RuntimeException("Dictionary not Initialized properly.");
		return localeDictionary;
	}

	/**
	 * Gets constant value from the locales.
	 * 
	 * @param key String
	 * @param arguments Object
	 * @return String
	 */
	public String getConstantValue(String key) {
		String result = null;
		boolean found = false;

		// getting constant value from common locale.
		if (commonLocaleInfo.isExist(key)) {
			result = Dictionary.getDictionary(commonLocaleInfo.getConstantVarNameList().get(0)).get(key);
		} else {

			// getting constant value from module locale
			List<String> constantsList = pageLocaleInfo.getConstantVarNameList();
			String listItem = null;
			for (int index = 0; index < constantsList.size(); index++) {
				listItem = constantsList.get(index);
				try {
					result = Dictionary.getDictionary(listItem).get(key);
					found = true;
				} catch (MissingResourceException missingResourceException) {
					found = false;
					continue;
				}
				break;
			}

			if (!found) {
				throw new MissingResourceException("Missing constant value: " + key, listItem, key);
			}
		}

		return result;
	}

	/**
	 * Gets message value from the locales.
	 * 
	 * @param key String
	 * @param arguments Object
	 * @return String
	 */
	public String getMessageValue(String key, Object... arguments) {
		String result = null;
		boolean found = false;

		// getting message value from common locale.
		if (commonLocaleInfo.isExist(key)) {
			result = MessageFormat.format(Dictionary.getDictionary(commonLocaleInfo.getMessageVarNameList().get(0)).get(key),
					arguments);
		} else {

			// getting message value from module locale
			List<String> messagesList = pageLocaleInfo.getMessageVarNameList();
			String listItem = null;
			for (int index = 0; index < messagesList.size(); index++) {
				listItem = messagesList.get(index);
				try {
					result = MessageFormat.format(Dictionary.getDictionary(listItem).get(key), arguments);
					found = true;
				} catch (MissingResourceException missingResourceException) {
					found = false;
					continue;
				}
				break;
			}

			if (!found) {
				throw new MissingResourceException("Missing message value: " + key, listItem, key);
			}
		}
		return result;
	}

	public LocaleInfo getLocaleInfo() {
		return pageLocaleInfo;
	}
}
