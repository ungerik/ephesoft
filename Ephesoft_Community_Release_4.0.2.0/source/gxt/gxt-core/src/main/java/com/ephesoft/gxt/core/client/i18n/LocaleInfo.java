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

import java.util.ArrayList;
import java.util.List;

/**
 * The locale information specific to each screen(module).
 * 
 * @author Ephesoft
 * @version 1.0
 * @see LocaleDictionary
 *
 */
public class LocaleInfo {

	/**
	 * {@link String} the locale for which values have to be looked up.
	 */
	private String locale;
	/**
	 * {@link List <String>} the constant variable name list for the module.
	 */
	private List<String> constantVarNameList = new ArrayList<String>();
	/**
	 * {@link List <String>} the message variable name list for the module.
	 */
	private List<String> messageVarNameList = new ArrayList<String>();

	/**
	 * Adding constant and message name for single module.
	 * 
	 * @param locale {@link String} the locale for which the constant and messages need to be added.
	 * @param constantVarName {@link String} the constant variable name.
	 * @param messageVarName {@link String} message variable name.
	 */
	public LocaleInfo(String locale, String constantVarName, String messageVarName) {
		this.locale = locale;

		constantVarNameList.add(constantVarName);
		messageVarNameList.add(messageVarName);

		if (locale.length() > 0) {
			String str = null;
			for (int index = 0; index < constantVarNameList.size(); index++) {
				str = constantVarNameList.get(index) + CoreCommonConstants.UNDERSCORE + locale;
				constantVarNameList.remove(index);
				constantVarNameList.add(index, str);
			}
			for (int index = 0; index < messageVarNameList.size(); index++) {
				str = messageVarNameList.get(index) + CoreCommonConstants.UNDERSCORE + locale;
				messageVarNameList.remove(index);
				messageVarNameList.add(index, str);
			}
		}
	}

	/**
	 * Adding constant and message name for single module.
	 * 
	 * @param locale {@link String} the locale for which the constant and messages need to be added.
	 * @param constantVarName {@link List <String>} the list of constant variable name.
	 * @param messageVarName {@link List <String>} the list of message variable name.
	 */
	public LocaleInfo(String locale, List<String> constantVarNameList, List<String> messageVarNameList) {
		this.locale = locale;

		this.constantVarNameList = constantVarNameList;
		this.messageVarNameList = messageVarNameList;

		if (locale.length() > 0) {
			String str = null;
			for (int index = 0; index < constantVarNameList.size(); index++) {
				str = constantVarNameList.get(index) + CoreCommonConstants.UNDERSCORE + locale;
				constantVarNameList.remove(index);
				constantVarNameList.add(index, str);
			}
			for (int index = 0; index < messageVarNameList.size(); index++) {
				str = messageVarNameList.get(index) + CoreCommonConstants.UNDERSCORE + locale;
				messageVarNameList.remove(index);
				messageVarNameList.add(index, str);
			}
		}
	}

	public String getLocale() {
		return locale;
	}

	public List<String> getConstantVarNameList() {
		return constantVarNameList;
	}

	public List<String> getMessageVarNameList() {
		return messageVarNameList;
	}

}
