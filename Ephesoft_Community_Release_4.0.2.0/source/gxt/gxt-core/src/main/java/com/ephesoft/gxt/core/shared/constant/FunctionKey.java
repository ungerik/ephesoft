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

package com.ephesoft.gxt.core.shared.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.event.dom.client.KeyCodes;

public enum FunctionKey {

	F1("F1", KeyCodes.KEY_F1), F2("F2", KeyCodes.KEY_F2), F3("F3", KeyCodes.KEY_F3), F4("F4", KeyCodes.KEY_F4), F5("F5",
			KeyCodes.KEY_F5), F6("F6", KeyCodes.KEY_F6), F7("F7", KeyCodes.KEY_F7), F8("F8", KeyCodes.KEY_F8), F9("F9",
			KeyCodes.KEY_F9), F10("F10", KeyCodes.KEY_F10), F11("F11", KeyCodes.KEY_F11), F12("F12", KeyCodes.KEY_F12);

	private String functionKeyName;
	private int keyCode;

	private FunctionKey(final String keyName, final int keyCode) {
		this.functionKeyName = keyName;
		this.keyCode = keyCode;
	}

	public String getFunctionKeyName() {
		return functionKeyName;
	}

	public int getKeyCode() {
		return keyCode;
	}

	public static FunctionKey getKeyByName(final String keyName) {
		FunctionKey associatedKey = null;
		if (!StringUtil.isNullOrEmpty(keyName)) {
			final FunctionKey[] keysArray = FunctionKey.values();
			for (final FunctionKey key : keysArray) {
				if (keyName.equalsIgnoreCase(key.getFunctionKeyName())) {
					associatedKey = key;
					break;
				}
			}
		}
		return associatedKey;
	}

	public static FunctionKey getKeyByCode(final int keyCode) {
		FunctionKey associatedKey = null;
		final FunctionKey[] keysArray = FunctionKey.values();
		for (final FunctionKey key : keysArray) {
			if (keyCode == key.getKeyCode()) {
				associatedKey = key;
				break;
			}
		}
		return associatedKey;
	}

	public static List<String> getKeyNamesList(final String... ignoreKeyNames) {
		List<String> keyNamesIgnoreList = null;
		if (ignoreKeyNames != null && ignoreKeyNames.length > 0) {
			keyNamesIgnoreList = new ArrayList<String>();
			keyNamesIgnoreList.addAll(Arrays.asList(ignoreKeyNames));
		}
		final List<String> keyNamesList = new ArrayList<String>();
		final FunctionKey[] keysArray = FunctionKey.values();
		for (final FunctionKey key : keysArray) {
			if (keyNamesIgnoreList == null || !keyNamesIgnoreList.contains(key.getFunctionKeyName())) {
				keyNamesList.add(key.getFunctionKeyName());
			}
		}
		return keyNamesList;
	}

	public static boolean checkFunctionKey(final int keyCode, final FunctionKey... skipKeys) {
		boolean isValidFunctionKey = false;
		final FunctionKey[] keysArray = FunctionKey.values();
		for (final FunctionKey key : keysArray) {
			if (keyCode == key.getKeyCode() && !isSkippedFunctionKey(key, skipKeys)) {
				isValidFunctionKey = true;
				break;
			}
		}
		return isValidFunctionKey;
	}

	private static boolean isSkippedFunctionKey(final FunctionKey key, final FunctionKey... skipKeys) {
		boolean isSkippedKey = false;
		if (skipKeys != null && skipKeys.length > 0) {
			for (final FunctionKey skipKey : skipKeys) {
				if (key == skipKey) {
					isSkippedKey = true;
					break;
				}
			}
		}
		return isSkippedKey;
	}
}
