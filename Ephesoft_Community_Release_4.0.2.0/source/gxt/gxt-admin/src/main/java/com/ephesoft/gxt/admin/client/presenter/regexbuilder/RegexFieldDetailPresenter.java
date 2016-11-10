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

package com.ephesoft.gxt.admin.client.presenter.regexbuilder;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.RegexEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.regexbuilder.RegexBuilderConstants;
import com.ephesoft.gxt.admin.client.regexbuilder.RegexEngine;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexFieldDetailView;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Provides the functionality to handle events for RegexFieldDetailView. This presenter handles the events for the text section of the
 * regex builder.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see BatchClassInlinePresenter
 * @see RegexFieldDetailView
 */
public class RegexFieldDetailPresenter extends BatchClassInlinePresenter<RegexFieldDetailView> {

	// /**
	// * The Interface CustomEventBinder.
	// */
	// interface CustomEventBinder extends EventBinder<RegexFieldDetailPresenter> {
	// }
	//
	// /** The Constant eventBinder. */
	// private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Constructor of the class.
	 * 
	 * @param controller {@link BatchClassManagementController} The instance of BatchClassManagementController.
	 * @param view {@link RegexFieldDetailView} The regex field detail view.
	 */
	public RegexFieldDetailPresenter(final BatchClassManagementController controller, final RegexFieldDetailView view) {
		super(controller, view);
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		// eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	@Override
	public void bind() {
	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern . RegexEvent will be fired only if key pressed is lower case
	 * letter,upper case letter ,tab or backspace .
	 * 
	 * @param event {@link KeyPressEvent} The instance of KeyPressEvent.
	 */
	public void handleKeyPressEventForLetter(final KeyPressEvent event) {
		if (!Character.isLowerCase(event.getCharCode()) && !Character.isUpperCase(event.getCharCode())
				&& KeyCodes.KEY_BACKSPACE != event.getNativeEvent().getKeyCode()
				&& KeyCodes.KEY_TAB != event.getNativeEvent().getKeyCode()) {
			((TextBox) event.getSource()).cancelKey();
		} else {
			controller.getEventBus().fireEvent(new RegexEvent());
		}
	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern . RegexEvent will be fired only if key pressed is lower case
	 * letter,upper case letter ,tab or backspace .
	 * 
	 * @param event {@link KeyPressEvent} The instance of KeyPressEvent.
	 */
	public void handleKeyPressEventForDigit(final KeyPressEvent event) {
		if (!Character.isDigit(event.getCharCode()) && KeyCodes.KEY_BACKSPACE != event.getNativeEvent().getKeyCode()
				&& KeyCodes.KEY_TAB != event.getNativeEvent().getKeyCode()) {
			((TextBox) event.getSource()).cancelKey();
		} else {
			controller.getEventBus().fireEvent(new RegexEvent());
		}
	}

	/**
	 * Fires the RegexEvent if the character entered is in right range.
	 */
	public void handleLowerRangeLowerCaseLetterTextBoxKeyUpEvents() {
		if (!RegexEngine.isValidValueInRange(view.getLowerRangeLowerCaseLetterTextField().getText(),
				RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			view.getLowerRangeLowerCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (!RegexEngine.isValidValueInRange(view.getUpperRangeLowerCaseLetterTextField().getText(),
				RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			view.getLowerRangeLowerCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getUpperRangeLowerCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (RegexEngine.isCharacterGreaterThan(view.getUpperRangeLowerCaseLetterTextField().getText(), view
				.getLowerRangeLowerCaseLetterTextField().getText(), RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			controller.getEventBus().fireEvent(new RegexEvent());
			view.getLowerRangeLowerCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getUpperRangeLowerCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getInvalidLowerCaseLetterRangeLabel().setVisible(false);
		} else {
			view.getInvalidLowerCaseLetterRangeLabel().setVisible(true);
			view.getLowerRangeLowerCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		}

	}

	/**
	 * Fires the RegexEvent if the character entered is in right range.
	 */
	public void handleUpperRangeLowerCaseLetterTextBoxKeyUpEvents() {
		if (!RegexEngine.isValidValueInRange(view.getUpperRangeLowerCaseLetterTextField().getText(),
				RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			view.getUpperRangeLowerCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (!RegexEngine.isValidValueInRange(view.getLowerRangeLowerCaseLetterTextField().getText(),
				RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			view.getUpperRangeLowerCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getLowerRangeLowerCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (RegexEngine.isCharacterGreaterThan(view.getUpperRangeLowerCaseLetterTextField().getText(), view
				.getLowerRangeLowerCaseLetterTextField().getText(), RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			controller.getEventBus().fireEvent(new RegexEvent());
			view.getLowerRangeLowerCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getUpperRangeLowerCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getInvalidLowerCaseLetterRangeLabel().setVisible(false);
		} else {
			view.getInvalidLowerCaseLetterRangeLabel().setVisible(true);
			view.getUpperRangeLowerCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		}

	}

	/**
	 * Fires the RegexEvent if the character entered is in right range.
	 */
	public void handleLowerRangeUpperCaseLetterTextBoxKeyUpEvents() {
		if (!RegexEngine.isValidValueInRange(view.getLowerRangeUpperCaseLetterTextField().getText(),
				RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			view.getLowerRangeUpperCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (!RegexEngine.isValidValueInRange(view.getUpperRangeUpperCaseLetterTextField().getText(),
				RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			view.getLowerRangeUpperCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getUpperRangeUpperCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (RegexEngine.isCharacterGreaterThan(view.getUpperRangeUpperCaseLetterTextField().getText(), view
				.getLowerRangeUpperCaseLetterTextField().getText(), RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			controller.getEventBus().fireEvent(new RegexEvent());
			view.getLowerRangeUpperCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getUpperRangeUpperCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getInvalidUpperCaseLetterRangeLabel().setVisible(false);
		} else {
			view.getInvalidUpperCaseLetterRangeLabel().setVisible(true);
			view.getLowerRangeUpperCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		}

	}

	/**
	 * Fires the RegexEvent if the character entered is in right range.
	 */
	public void handleUpperRangeUpperCaseLetterTextBoxKeyUpEvents() {
		if (!RegexEngine.isValidValueInRange(view.getUpperRangeUpperCaseLetterTextField().getText(),
				RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			view.getUpperRangeUpperCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (!RegexEngine.isValidValueInRange(view.getLowerRangeUpperCaseLetterTextField().getText(),
				RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			view.getUpperRangeUpperCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getLowerRangeUpperCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (RegexEngine.isCharacterGreaterThan(view.getUpperRangeUpperCaseLetterTextField().getText(), view
				.getLowerRangeUpperCaseLetterTextField().getText(), RegexBuilderConstants.LETTER_RANGE_REGEX)) {
			controller.getEventBus().fireEvent(new RegexEvent());
			view.getLowerRangeUpperCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getUpperRangeUpperCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getInvalidUpperCaseLetterRangeLabel().setVisible(false);
		} else {
			view.getInvalidUpperCaseLetterRangeLabel().setVisible(true);
			view.getUpperRangeUpperCaseLetterTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		}

	}

	/**
	 * Fires the RegexEvent if the digit entered is in right range.
	 */
	public void handleLowerDigitRangeTextBoxKeyUpEvents() {
		if (!RegexEngine.isValidValueInRange(view.getLowerDigitRangeTextField().getText(),
				RegexBuilderConstants.DIGIT_RANGE_REGEX_STRING)) {
			view.getLowerDigitRangeTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (!RegexEngine.isValidValueInRange(view.getUpperDigitRangeTextField().getText(),
				RegexBuilderConstants.DIGIT_RANGE_REGEX_STRING)) {
			view.getUpperDigitRangeTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (RegexEngine.isDigitGreaterThan(view.getUpperDigitRangeTextField().getText(), view.getLowerDigitRangeTextField()
				.getText(), RegexBuilderConstants.DIGIT_RANGE_REGEX_STRING)) {
			controller.getEventBus().fireEvent(new RegexEvent());
			view.getUpperDigitRangeTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getLowerDigitRangeTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getInvalidDigitRangeLabel().setVisible(false);
		} else {
			
			view.getInvalidDigitRangeLabel().setVisible(true);
			view.getLowerDigitRangeTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		}

	}

	/**
	 * Fires the RegexEvent if the digit entered is in right range.
	 */
	public void handleUpperDigitRangeTextBoxKeyUpEvents() {
		if (!RegexEngine.isValidValueInRange(view.getUpperDigitRangeTextField().getText(),
				RegexBuilderConstants.DIGIT_RANGE_REGEX_STRING)) {
			view.getUpperDigitRangeTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (!RegexEngine.isValidValueInRange(view.getLowerDigitRangeTextField().getText(),
				RegexBuilderConstants.DIGIT_RANGE_REGEX_STRING)) {
			view.getLowerDigitRangeTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		} else if (RegexEngine.isDigitGreaterThan(view.getUpperDigitRangeTextField().getText(), view.getLowerDigitRangeTextField()
				.getText(), RegexBuilderConstants.DIGIT_RANGE_REGEX_STRING)) {
			controller.getEventBus().fireEvent(new RegexEvent());
			view.getUpperDigitRangeTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getLowerDigitRangeTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getInvalidDigitRangeLabel().setVisible(false);
		} else {
			
			view.getInvalidDigitRangeLabel().setVisible(true);
			view.getUpperDigitRangeTextField().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		}

	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Makes upperRangeLowerCaseLetterTextBox,
	 * lowerRangeLowerCaseLetterTextBox enabled if lowerCaseLetterRangeCheckBox is being checked otherwise disabled the
	 * lowerRangeLowerCaseLetterTextBox,upperRangeLowerCaseLetterTextBox.
	 * 
	 * @param event {@link ClickEvent}The instance of ClickEvent.
	 */
	public void onLowerCaseLetterRangeCheckBoxClicked(final ClickEvent event) {
		if (((CheckBox) event.getSource()).getValue()) {
			view.getLowerRangeLowerCaseLetterTextField().setEnabled(true);
			view.getUpperRangeLowerCaseLetterTextField().setEnabled(true);
		} else {
			view.getLowerRangeLowerCaseLetterTextField().setEnabled(false);
			view.getUpperRangeLowerCaseLetterTextField().setEnabled(false);
			view.getLowerRangeLowerCaseLetterTextField().setText(BatchClassConstants.EMPTY_STRING);
			view.getUpperRangeLowerCaseLetterTextField().setText(BatchClassConstants.EMPTY_STRING);
			view.getUpperRangeLowerCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getLowerRangeLowerCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getInvalidLowerCaseLetterRangeLabel().setVisible(false);

		}
		controller.getEventBus().fireEvent(new RegexEvent());

	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Makes lowerRangeUpperCaseLetterTextBox,
	 * upperRangeUpperCaseLetterTextBox enabled if upperCaseLetterRangeCheckBox is being checked otherwise disabled the
	 * lowerRangeUpperCaseLetterTextBox,upperRangeUpperCaseLetterTextBox.
	 * 
	 * @param event {@link ClickEvent}The instance of ClickEvent.
	 */
	public void onUpperCaseLetterRangeCheckBoxClicked(final ClickEvent event) {
		if (((CheckBox) event.getSource()).getValue()) {
			view.getLowerRangeUpperCaseLetterTextField().setEnabled(true);
			view.getUpperRangeUpperCaseLetterTextField().setEnabled(true);
		} else {
			view.getLowerRangeUpperCaseLetterTextField().setEnabled(false);
			view.getUpperRangeUpperCaseLetterTextField().setEnabled(false);
			view.getLowerRangeUpperCaseLetterTextField().setText(BatchClassConstants.EMPTY_STRING);
			view.getUpperRangeUpperCaseLetterTextField().setText(BatchClassConstants.EMPTY_STRING);
			view.getLowerRangeUpperCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getUpperRangeUpperCaseLetterTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getInvalidUpperCaseLetterRangeLabel().setVisible(false);
		}
		controller.getEventBus().fireEvent(new RegexEvent());

	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Makes lowerDigitRangeTextBox, upperDigitRangeTextBox enabled if
	 * digitRangeCheckBox is being checked otherwise disabled the upperDigitRangeTextBox,upperDigitRangeTextBox.
	 * 
	 * @param event {@link ClickEvent}The instance of ClickEvent.
	 */
	public void onDigitRangeCheckBoxClicked(final ClickEvent event) {
		if (((CheckBox) event.getSource()).getValue()) {
			view.getLowerDigitRangeTextField().setEnabled(true);
			view.getUpperDigitRangeTextField().setEnabled(true);
		} else {
			view.getLowerDigitRangeTextField().setEnabled(false);
			view.getUpperDigitRangeTextField().setEnabled(false);
			view.getLowerDigitRangeTextField().setText(BatchClassConstants.EMPTY_STRING);
			view.getUpperDigitRangeTextField().setText(BatchClassConstants.EMPTY_STRING);
			view.getLowerDigitRangeTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getUpperDigitRangeTextField().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			
			view.getInvalidDigitRangeLabel().setVisible(false);
		}
		controller.getEventBus().fireEvent(new RegexEvent());

	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Makes freeTextTextBox enabled if freeTextCheckBox is being checked
	 * otherwise disabled the freeTextTextBox.
	 * 
	 * @param event {@link ClickEvent}The instance of ClickEvent.
	 */
	public void onFreeTextCheckBoxClicked(final ClickEvent event) {
		if (((CheckBox) event.getSource()).getValue()) {

			view.getFreeTextTextField().setEnabled(true);
			view.getFreeTextTextField().focus();
		} else {
			view.getFreeTextTextField().setEnabled(false);
			view.getFreeTextTextField().setText(BatchClassConstants.EMPTY_STRING);
		}
		controller.getEventBus().fireEvent(new RegexEvent());

	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Makes specialCharCheckBox enabled if freeTextCheckBox is being checked
	 * otherwise disabled the specialCharTextBox.
	 * 
	 * @param event {@link ClickEvent}The instance of ClickEvent.
	 */
	public void onSpecialCharCheckBoxClicked(final ClickEvent event) {
		if (((CheckBox) event.getSource()).getValue()) {
			view.getSpecialCharTextField().setEnabled(true);
			view.getSpecialCharTextField().focus();
			view.getSpecialCharComboBox().setEnabled(true);

			view.getSpecialCharComboBox().setValue(view.getSpecialCharComboBox().getStore().get(0), true);
			view.getMatchExactTextCheckBox().setEnabled(false);

			view.getMatchExactTextTextField().setEnabled(false);
			view.getMatchExactTextTextField().setText(BatchClassConstants.EMPTY_STRING);

			view.getAnyCharacterCheckBox().setEnabled(false);
		} else {
			view.getSpecialCharTextField().setEnabled(false);
			view.getSpecialCharTextField().setText(BatchClassConstants.EMPTY_STRING);
			view.getSpecialCharComboBox().setEnabled(false);

			view.getSpecialCharComboBox().setValue(view.getSpecialCharComboBox().getStore().get(0), true);
			view.getMatchExactTextCheckBox().setEnabled(true);
			view.getAnyCharacterCheckBox().setEnabled(true);
		}
		controller.getEventBus().fireEvent(new RegexEvent());

	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Makes matchExactTextTextBox enabled if matchExactTextCheckBox is being
	 * checked otherwise disabled the matchExactTextTextBox.
	 * 
	 * @param event {@link ClickEvent}The instance of ClickEvent.
	 */
	public void onMatchExactTextCheckBoxClicked(final ClickEvent event) {
		if (((CheckBox) event.getSource()).getValue()) {

			view.getMatchExactTextTextField().setEnabled(true);

			view.getMatchStringComboBox().setValue(view.getMatchStringComboBox().getStore().get(0), true);
			view.getAnyCharacterCheckBox().setEnabled(false);

			view.getMatchExactTextTextField().focus();

			view.getSpecialCharCheckBox().setEnabled(false);
			view.getSpecialCharCheckBox().setValue(false);
			view.getSpecialCharComboBox().setEnabled(false);

			view.getSpecialCharComboBox().setValue(view.getSpecialCharComboBox().getStore().get(0), true);

			view.getSpecialCharTextField().setEnabled(false);
			view.getSpecialCharTextField().setText(BatchClassConstants.EMPTY_STRING);
		} else {

			view.getMatchExactTextTextField().setEnabled(false);
			view.getMatchExactTextTextField().setText(BatchClassConstants.EMPTY_STRING);

			view.getAnyCharacterCheckBox().setEnabled(true);
			view.getSpecialCharCheckBox().setEnabled(true);
		}
		controller.getEventBus().fireEvent(new RegexEvent());

	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Makes matchExactTextCheckBox enabled if anyCharacterCheckBox is being
	 * unchecked otherwise disabled the matchExactTextCheckBox.
	 * 
	 * @param event {@link ClickEvent}The instance of ClickEvent.
	 */
	public void onAnyCharacterCheckBoxClicked(final ClickEvent event) {
		if (((CheckBox) event.getSource()).getValue()) {
			view.getMatchExactTextCheckBox().setEnabled(false);

			view.getMatchExactTextTextField().setEnabled(false);
			view.getMatchExactTextTextField().setText(BatchClassConstants.EMPTY_STRING);

			view.getSpecialCharCheckBox().setEnabled(false);
			view.getSpecialCharCheckBox().setValue(false);
			view.getSpecialCharComboBox().setEnabled(false);

			view.getSpecialCharComboBox().setValue(view.getSpecialCharComboBox().getStore().get(0), true);

			view.getSpecialCharTextField().setEnabled(false);
			view.getSpecialCharTextField().setText(BatchClassConstants.EMPTY_STRING);
		} else {

			view.getMatchExactTextTextField().setEnabled(false);
			view.getMatchExactTextTextField().setText(BatchClassConstants.EMPTY_STRING);

			view.getMatchExactTextCheckBox().setEnabled(true);
			view.getSpecialCharCheckBox().setEnabled(true);
		}
		controller.getEventBus().fireEvent(new RegexEvent());

	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Makes wordBoundaryComboBox enabled if wordBoundaryCheckBox is being
	 * checked otherwise disabled the wordBoundaryComboBox.
	 * 
	 * @param event {@link ClickEvent}The instance of ClickEvent.
	 */
	public void onWordBoundaryCheckBoxClicked(final ClickEvent event) {
		if (((CheckBox) event.getSource()).getValue()) {
			view.getWordBoundaryComboBox().setEnabled(true);
			view.getWordBoundaryComboBox().setValue(view.getWordBoundaryComboBox().getStore().get(0), true);
		} else {
			view.getWordBoundaryComboBox().setEnabled(false);
			view.getWordBoundaryComboBox().setValue(view.getWordBoundaryComboBox().getStore().get(0), true);
		}

		controller.getEventBus().fireEvent(new RegexEvent());

	}

	/**
	 * Makes visible or invisible the text in range section or exact text section based on the value paased. If <code>isValue</code> is
	 * true then text in range section makes it invisible or false value makes Exact text match panel visible.
	 * 
	 * @param isValue boolean.
	 */
	public void onExactTextRadioButtonChecked(final boolean isValue) {
		if (isValue) {
			view.getMatchExactTextPanel().setVisible(true);
			view.getMatchTextInRangePanel().setVisible(false);
			view.setEnableTextRangeTextFields(false);
			view.setCheckedTextRangeCheckBoxes(false);
			view.setEnableTextRangeCheckBoxes(false);
			view.getMatchExactTextCheckBox().setEnabled(true);

			view.getMatchExactTextTextField().setText(BatchClassConstants.EMPTY_STRING);
			view.getMatchExactTextTextField().setEnabled(false);

			view.getAnyCharacterCheckBox().setEnabled(true);
			view.getSpecialCharCheckBox().setEnabled(true);
			view.getWordBoundaryCheckBox().setValue(false);
			view.getWordBoundaryComboBox().setEnabled(false);

			view.getWordBoundaryComboBox().setValue(view.getWordBoundaryComboBox().getStore().get(0), true);
		} else {
			view.getMatchExactTextPanel().setVisible(false);
		}
		controller.getEventBus().fireEvent(new RegexEvent());

	}

	/**
	 * Makes visible or invisible the text in range section or exact text section based on the value paased. If <code>isValue</code> is
	 * true then text in range section will be visible or false value makes Exact text match panel invisible.
	 * 
	 * @param isValue boolean.
	 */
	public void onTextInRangeRadioButtonChecked(final boolean isValue) {
		if (isValue) {
			view.getMatchExactTextPanel().setVisible(false);
			view.getMatchExactTextCheckBox().setEnabled(false);
			view.getMatchExactTextCheckBox().setValue(false);

			view.getMatchExactTextTextField().setText(BatchClassConstants.EMPTY_STRING);

			view.getSpecialCharCheckBox().setEnabled(false);
			view.getSpecialCharCheckBox().setValue(false);
			view.getSpecialCharTextField().setText(BatchClassConstants.EMPTY_STRING);
			view.getSpecialCharTextField().setEnabled(false);

			view.getSpecialCharComboBox().setValue(view.getSpecialCharComboBox().getStore().get(0), true);

			view.getAnyCharacterCheckBox().setEnabled(false);
			view.setEnableTextRangeTextFields(false);
			view.setEnableTextRangeCheckBoxes(true);
			view.getMatchTextInRangePanel().setVisible(true);

			view.getWordBoundaryCheckBox().setValue(false);
			view.getWordBoundaryComboBox().setEnabled(false);
			
			view.getInvalidDigitRangeLabel().setVisible(false);
			view.getInvalidLowerCaseLetterRangeLabel().setVisible(false);
			view.getInvalidUpperCaseLetterRangeLabel().setVisible(false);

		} else {
			view.getMatchTextInRangePanel().setVisible(false);

		}
		controller.getEventBus().fireEvent(new RegexEvent());

	}

	/**
	 * Populate the regex pattern based on the special character option selected from the list of special characters.
	 */
	public void onSpecialCharactersSelected() {
		int selectedIndex = view.getSpecialCharComboBox().getSelectedIndex();
		String selectedValue = view.getSpecialCharComboBox().getStore().get(selectedIndex);

		if (LocaleDictionary.getConstantValue(RegexBuilderConstants.BACKSLASH_CHARACTER).equals(selectedValue)) {
			view.getSpecialCharTextField().setText(
					view.getSpecialCharTextField().getText().concat(RegexBuilderConstants.BACKSLASH_REGEX));
		} else if (LocaleDictionary.getConstantValue(RegexBuilderConstants.WHITESPACE_CHARACTER)
				.equals(selectedValue)) {
			view.getSpecialCharTextField().setText(
					view.getSpecialCharTextField().getText().concat(RegexBuilderConstants.WHITE_SPACE_CHARACTER_REGEX));
		} else if (LocaleDictionary.getConstantValue(RegexBuilderConstants.NON_WHITESPACE_CHARACTER)
				.equals(selectedValue)) {
			view.getSpecialCharTextField().setText(
					view.getSpecialCharTextField().getText().concat(RegexBuilderConstants.NON_WHITESPACE_CHARACTER_REGEX));
		} else if (LocaleDictionary.getConstantValue(RegexBuilderConstants.WORD_CHARACTER).equals(selectedValue)) {
			view.getSpecialCharTextField().setText(
					view.getSpecialCharTextField().getText().concat(RegexBuilderConstants.WORD_CAHARCTER_REGEX));
		} else if (LocaleDictionary.getConstantValue(RegexBuilderConstants.NON_WORD_CHARACTER)
				.equals(selectedValue)) {
			view.getSpecialCharTextField().setText(
					view.getSpecialCharTextField().getText().concat(RegexBuilderConstants.NON_WORD_CAHARCTER_REGEX));
		} else if (LocaleDictionary.getConstantValue(RegexBuilderConstants.NON_DIGIT).equals(selectedValue)) {
			view.getSpecialCharTextField().setText(
					view.getSpecialCharTextField().getText().concat(RegexBuilderConstants.NON_DIGIT_REGEX));
		}

		controller.getEventBus().fireEvent(new RegexEvent());

	}

}
