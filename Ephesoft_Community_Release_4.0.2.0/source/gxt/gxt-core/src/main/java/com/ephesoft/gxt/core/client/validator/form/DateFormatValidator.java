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
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.AbstractValidator;

public class DateFormatValidator extends AbstractValidator<String> {

	private DateTimeFormat format;
	private String dateFormatErrorMessage;
	private String mandatoryErrorMessage;
	private boolean isMandatory = false;
	private String pattern;

	public DateFormatValidator(boolean isMandatory, String errorString) {
		this("dd/MM/yyyy", isMandatory, errorString);
	}

	public DateFormatValidator(String pattern, boolean isMandatory, String dateFormatErrorMessage) {
		this(pattern, isMandatory, dateFormatErrorMessage, dateFormatErrorMessage);
	}

	public DateFormatValidator(boolean isMandatory, String errorString, String mandatoryErrorMessage) {
		this("dd/MM/yyyy", isMandatory, errorString, mandatoryErrorMessage);
	}

	public DateFormatValidator(String pattern, boolean isMandatory, String dateFormatErrorMessage, String mandatoryErrorMessage) {
		format = DateTimeFormat.getFormat(pattern);
		this.dateFormatErrorMessage = dateFormatErrorMessage;
		this.isMandatory = true;
		this.mandatoryErrorMessage = mandatoryErrorMessage;
		this.pattern = pattern;
	}

	@Override
	public List<EditorError> validate(Editor<String> editor, String value) {

		if (StringUtil.isNullOrEmpty(value) && isMandatory) {
			return createError(new DefaultEditorError(editor, mandatoryErrorMessage, value));
		} else {
			try {
				format.parse(value);
			} catch (IllegalArgumentException exception) {
				return createError(new DefaultEditorError(editor, dateFormatErrorMessage, value));
			}
		}
		return null;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

}
