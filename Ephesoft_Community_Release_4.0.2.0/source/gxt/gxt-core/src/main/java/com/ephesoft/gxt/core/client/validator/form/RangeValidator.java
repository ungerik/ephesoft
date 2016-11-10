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

package com.ephesoft.gxt.core.client.validator.form;

import java.util.List;

import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.core.shared.util.ValidationUtil;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;

public class RangeValidator extends AbstractValidator<String> {

	private Class<?> isClass;
	private Object minValue;
	private Object maxValue;
	private String errorMessage;

	public RangeValidator(Class<?> isClass, String errorMessage, Object minVal, Object maxVal) {
		this.isClass = isClass;
		this.errorMessage=errorMessage;
		this.minValue=minVal;
		this.maxValue =maxVal;
	}
	@Override
	public List<EditorError> validate(Editor<String> editor, String value) {
		if(value == null || value.equals("") || !isValidNumericalValue(value)){
			return createError(new DefaultEditorError(editor, errorMessage, value));
		}
		return null;
	}

	public boolean isValidNumericalValue(final String value) {
		boolean isValidNumber = false;
		if (null != value) {
			String valueToCheck = value.toString();
			if (!StringUtil.isNullOrEmpty(valueToCheck)) {
				try {
					if (isClass == Float.class) {
						Float fValue = Float.valueOf(valueToCheck);
						isValidNumber = ValidationUtil.isValueInRange(fValue, (Float) minValue, (Float) maxValue);
					}
					if (isClass == Double.class) {
						Double dValue = Double.valueOf(valueToCheck);
						isValidNumber = ValidationUtil.isValueInRange(dValue, (Double) minValue, (Double) maxValue);
					}
					if (isClass == Integer.class) {
						Integer iValue = Integer.valueOf(valueToCheck);
						isValidNumber = ValidationUtil.isValueInRange(iValue, (Integer) minValue, (Integer) maxValue);
					}
				} catch (final NumberFormatException exception) {
					isValidNumber = false;
				}
			}
		}
		return isValidNumber;
	}
}
