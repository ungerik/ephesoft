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

package com.ephesoft.gxt.core.client.validator;

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;

public class NumberValidator extends AbstractValidator<String> {
	private Class<?> isClass;
	private String errorMessage;
	private boolean skipNullCheck;
	private boolean isOnlyPositive;
	
	public NumberValidator(final Class<?> isClass, final String errorMessage) {
		this(isClass, false, errorMessage);
	}
	public NumberValidator(final Class<?> isClass, final boolean skipNullCheck, final String errorMessage) {
		this.isClass = isClass;
		this.errorMessage=errorMessage;
		this.skipNullCheck=skipNullCheck;
	}

	@Override
	public List<EditorError> validate(Editor<String> editor, String value) {
		boolean isValid = true;
		try {
			if (value == null || value.length() == 0) {
				if (!skipNullCheck) {
					isValid = false;
				}
			} else {
				
				if (isClass == Float.class) {
					float floatValue = Float.parseFloat(value);
					
					//checking for the negative numbers
					if (isOnlyPositive && (floatValue < 0)) {
						isValid = false;
					}
				} else {
					long longValue = Long.parseLong(value);
					
					//checking for the negative numbers
					if (isOnlyPositive && (longValue < 0)) {
						isValid = false;
					}
				}
			}
		} catch (Exception e) {
			// the value couldn't be parsed by the pattern, return false
			isValid = false;
		}
		if(!isValid){
			return createError(new DefaultEditorError(editor, errorMessage, value));
		}
		return null;
	}

	
	public void setSkipNullCheck(boolean skipNullCheck) {
		this.skipNullCheck = skipNullCheck;
	}

	
	public void setOnlyPositive(boolean isOnlyPositive) {
		this.isOnlyPositive = isOnlyPositive;
	}
	
	
}
