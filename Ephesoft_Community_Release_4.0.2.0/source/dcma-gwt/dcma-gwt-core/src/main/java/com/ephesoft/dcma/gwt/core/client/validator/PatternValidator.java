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

package com.ephesoft.dcma.gwt.core.client.validator;

import java.util.List;

import com.ephesoft.dcma.gwt.core.client.i18n.CoreCommonConstants;
import com.google.gwt.user.client.ui.HasValue;

public class PatternValidator implements Validator {

	private HasValue<String> _value;
	private List<String> patternStr;
	
	/**
	 * patternStrSeparator {@link String} The logical separator(AND,OR) which is to be used for the list of regex pattern.
	 */
	private String patternStrSeparator;

	public static final String EMPTY = "";

	public PatternValidator(HasValue<String> value, List<String> patternStr) {
		_value = value;
		this.patternStr = patternStr;
	}
	
	/**
	 * Instantiates the PatternValidator with the values passed.
	 * 
	 * @param value {@link HasValue} The instance of HasValue.
	 * @param patternStr {@link List} The list of regex patterns.
	 * @param patternStrSeparator {@link String}The logical separator used for the list or regex patterns.
	 */
	public PatternValidator(final HasValue<String> value, final List<String> patternStr, final String patternStrSeparator) {
		_value = value;
		this.patternStr = patternStr;
		this.patternStrSeparator = patternStrSeparator;
	}

	public void setValue(HasValue<String> value) {
		_value = value;
	}

	@Override
	public boolean validate() {
		String inputStr = _value.getValue();
		boolean allPatternMatched = false;
		if (null == patternStr || patternStr.size() == 0) {
			return true;
		}
		if (null == inputStr) {
			inputStr = "";
		}
		for (String patternString : patternStr) {

			if(!CoreCommonConstants.AND.equalsIgnoreCase(patternStrSeparator)){
				allPatternMatched = matchPattern(inputStr, patternString);
				if(allPatternMatched){
					break;
				}
			}else{
				allPatternMatched = matchPattern(inputStr, patternString);
				if(!allPatternMatched){
					break;
				}
			}
		}
		return allPatternMatched;
	}

	private boolean matchPattern(String inputStr, String patternString) {
		boolean returnVal = false;
		try {
			returnVal = inputStr.matches(patternString);
		} catch (Exception e) {
		}
		return returnVal;
	}
}
