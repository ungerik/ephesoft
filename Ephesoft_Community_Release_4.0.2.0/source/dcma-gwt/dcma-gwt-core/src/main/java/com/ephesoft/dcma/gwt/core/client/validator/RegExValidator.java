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

import com.ephesoft.dcma.gwt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;

public class RegExValidator extends TableExtractionValidator {

	protected static final String INVALID_REGEX_PATTERN = "invalid_regex_pattern";
	private HasValue<String> _value;
	private String _pattern;
	private boolean isOnlyPatternValidator;
	private boolean isMandatory;
	private boolean isMultiplePattern;
	private String patternDelimiter;
	private DCMARemoteServiceAsync remoteService;
	private RegExValidatableWidget<?> validatableWidget;
	private boolean isJavaScriptPattern;

	public RegExValidator(final String pattern, final HasValue<String> value) {
		this._pattern = pattern;
		this._value = value;
		this.isOnlyPatternValidator = false;
		this.isMandatory = true;
		this.isMultiplePattern = false;
		this.remoteService = null;
		this.isJavaScriptPattern = false;
	}

	public RegExValidator(final RegExValidatableWidget<?> validatableWidget, final HasValue<String> value, final boolean isMandatory,
			final boolean isMultiplePattern, final boolean isPatternValidator, final String patternDelimiter,
			final DCMARemoteServiceAsync remoteServiceAsync) {
		this(validatableWidget, value, isMandatory, isMultiplePattern, isPatternValidator, patternDelimiter, remoteServiceAsync, false);
	}

	public RegExValidator(final RegExValidatableWidget<?> validatableWidget, final HasValue<String> value, final boolean isMandatory,
			final boolean isMultiplePattern, final boolean isPatternValidator, final String patternDelimiter,
			final DCMARemoteServiceAsync remoteServiceAsync, final boolean isJavaScriptPattern) {
		this.validatableWidget = validatableWidget;
		this._value = value;
		this.isMandatory = isMandatory;
		this.isMultiplePattern = isMultiplePattern;
		this.isOnlyPatternValidator = isPatternValidator;
		this.patternDelimiter = patternDelimiter;
		this.remoteService = remoteServiceAsync;
		this.isJavaScriptPattern = isJavaScriptPattern;
	}

	@Override
	public boolean validate() {
		boolean isPatternValid = true;
		if (isOnlyPatternValidator) {
			final String pattern = _value.getValue();
			if (pattern.isEmpty() && isMandatory) {
				isPatternValid = false;
			} else {
				if (isMultiplePattern) {
					isPatternValid = validateMultiplePattern(pattern);
				} else {
					isPatternValid = validatePattern(pattern);
				}
			}
		} else {

			// changes made for allowing blank values.
			if (_value.getValue() == null) {
				isPatternValid = false;
			} else {
				isPatternValid = _value.getValue().matches(_pattern);
			}
		}
		return isPatternValid;
	}

	public boolean validateMultiplePattern(final String multiplePattern) {
		boolean isPatternValid = true;
		String[] patternArr = null;
		if (multiplePattern.contains(patternDelimiter)) {
			patternArr = multiplePattern.split(patternDelimiter);
			for (final String pattern : patternArr) {
				if (!validatePattern(pattern)) {
					isPatternValid = false;
					break;
				}
			}
		} else {
			isPatternValid = validatePattern(multiplePattern);
		}
		return isPatternValid;
	}

	/**
	 * Checks if the provided pattern is a valid regular expression pattern.
	 * 
	 * @param pattern {@link String} the pattern to validate.
	 * @return {@link boolean} true.
	 */
	public boolean validatePattern(final String pattern) {
		boolean isPatternValid = true;

		// changes made for JIRA bug id #11023 - Incorrect regex is validated.
		if (isJavaScriptPattern) {
			isPatternValid = validateJavaScriptRegex(pattern);
		}
		if (isPatternValid && remoteService != null) {
			validatePatternOnServer(pattern);
		} else {
			ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(INVALID_REGEX_PATTERN));
		}
		return isPatternValid;
	}

	private native boolean validateJavaScriptRegex(String pattern) /*-{
																	return $wnd.validateRegex(pattern);
																	}-*/;

	private void validatePatternOnServer(final String pattern) {
		ScreenMaskUtility.maskScreen();
		remoteService.validateRegEx(pattern, new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(final Boolean isPatternValid) {
				validatableWidget.setValid(isPatternValid);
				ScreenMaskUtility.unmaskScreen();
				if (!isPatternValid) {
					ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(INVALID_REGEX_PATTERN));
				}
			}

			@Override
			public void onFailure(final Throwable arg0) {
				validatableWidget.setValid(false);
				ScreenMaskUtility.unmaskScreen();
				ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getMessageValue(INVALID_REGEX_PATTERN));
			}
		});
	}

	/**
	 * Returns the rule or pattern not satisfied by the widget.
	 * 
	 * @return {@link String} the pattern not satisfied by the widget's value.
	 */
	@Override
	public String getRuleOrPatternNotSatisfied() {
		return this._pattern;
	}
}
