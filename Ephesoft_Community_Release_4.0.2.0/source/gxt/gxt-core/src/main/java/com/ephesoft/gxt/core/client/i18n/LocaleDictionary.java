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

package com.ephesoft.gxt.core.client.i18n;

import java.util.List;
import java.util.MissingResourceException;

import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.util.MessageFormat;
import com.google.gwt.i18n.client.Dictionary;

/**
 * The dictionary which stores all the locale information with respect to application level and screen specific locales.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see CommonLocaleInfo
 * @see LocaleInfo
 * 
 */
public class LocaleDictionary {

	private static LocaleDictionary localeDictionary;

	/**
	 * {@link CommonLocaleInfo} for the locale specific to application.
	 */
	private static CommonLocaleInfo commonLocaleInfo;
	/**
	 * {@link LocaleInfo} for the locale specific to each screen.
	 */
	private static LocaleInfo pageLocaleInfo;

	private LocaleDictionary() {
	}

	/**
	 * Initialization of dictionary for provided {@link LocaleInfo}.
	 * 
	 * @param localeInfo {@link LocaleInfo} the details of locale which has to be initialized.
	 * @return instance of {@link LocaleDictionary} which contains all details common and screen specific locale.
	 */
	public static LocaleDictionary create(LocaleInfo localeInfo) {
		commonLocaleInfo = CommonLocaleInfo.get(localeInfo.getLocale());
		pageLocaleInfo = localeInfo;
		if (localeDictionary == null) {
			localeDictionary = new LocaleDictionary();
		}
		return localeDictionary;
	}

	public static LocaleDictionary getLocaleDictionary() {
		if (localeDictionary == null)
			throw new RuntimeException("Dictionary not Initialized properly.");
		return localeDictionary;
	}

	/**
	 * Gets constant value from the locales.
	 * 
	 * @param key {@link String} the constant key for which locale lookup has to be performed for value.
	 * @return {@link String} the value in accord with the specified key.
	 */
	public static String getConstantValue(String key) {
		String result = null;
		boolean found = false;

		// getting constant value from common locale.
		try {
			if (commonLocaleInfo.isExist(key)) {
				result = Dictionary.getDictionary(commonLocaleInfo.getConstantVarNameList().get(0)).get(key);
				found = true;
			} else {

				// getting constant value from module locale
				final List<String> constantsList = pageLocaleInfo.getConstantVarNameList();
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
			}
		} catch (Exception exception) {
			Message.display("Locale Variable not found", "" + key);
		}

		if (!found) {
			result = key;
		}
		return result;
	}

	/**
	 * Gets message value from the locales.
	 * 
	 * @param key {@link String} the key for which locale lookup has to be performed to find message value.
	 * @param arguments Object
	 * @return {@link String} the value of message for provided key.
	 */
	public static String getMessageValue(String key, Object... arguments) {
		String result = null;
		boolean found = false;

		// Getting message value from common locale.
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
				result = key;
			}
		}
		return result;
	}

	public LocaleInfo getLocaleInfo() {
		return pageLocaleInfo;
	}
}
