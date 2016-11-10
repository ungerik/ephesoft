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

import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.sencha.gxt.core.client.ValueProvider;

/**
 * This validator is used to validate numeric values.
 * 
 * @author Ephesoft
 * @version 1.0
 * @param <T>
 *            the generic type
 * @see com.ephesoft.gxt.core.client.validator.NumericValueValidator
 */
public class NumericValueValidator<T> implements Validator<T> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ephesoft.gxt.core.client.validator.Validator#validate(java.util.List,
	 * java.lang.Object, com.sencha.gxt.core.client.ValueProvider)
	 */
	@Override
	public boolean validate(List<T> listOfModels, T model,
			ValueProvider<T, ?> valueProvider) {
		boolean valid = true;
		if (null != model && null != valueProvider) {
			Object value = valueProvider.getValue(model);
			if (value != null && value.getClass() == Integer.class
					&& !StringUtil.isNullOrEmpty(value.toString().trim())
					&& Integer.parseInt(value.toString()) < 0) {
				valid = false;
			} else if (value != null && value.getClass() == Double.class
					&& !StringUtil.isNullOrEmpty(value.toString().trim())
					&& Double.parseDouble(value.toString()) < 0.00) {
				valid = false;
			} else if (value != null && value.getClass() == Float.class
					&& !StringUtil.isNullOrEmpty(value.toString().trim())
					&& Float.parseFloat(value.toString()) < 0.00f) {
				valid = false;
			} else if (value != null && value.getClass() == Long.class
					&& !StringUtil.isNullOrEmpty(value.toString().trim())
					&& Long.parseLong(value.toString()) < 0) {
				valid = false;
			}
		}
		return valid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.validator.Validator#getErrorMessage()
	 */
	@Override
	public String getErrorMessage() {
		return LocaleDictionary.getConstantValue(LocaleCommonConstants.TOOLTIP_VALUE_NONNEGATIVE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.validator.Validator#getSeverity()
	 */
	@Override
	public com.ephesoft.gxt.core.client.validator.Validator.Severity getSeverity() {
		return Severity.MEDIUM;
	}

}
