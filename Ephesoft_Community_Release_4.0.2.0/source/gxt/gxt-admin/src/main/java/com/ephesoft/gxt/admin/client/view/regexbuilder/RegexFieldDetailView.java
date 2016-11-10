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

package com.ephesoft.gxt.admin.client.view.regexbuilder;

import java.util.HashSet;
import java.util.Set;

import com.ephesoft.gxt.admin.client.event.RegexEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.regexbuilder.RegexFieldDetailPresenter;
import com.ephesoft.gxt.admin.client.regexbuilder.RegexBuilderConstants;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Shows the view of Regex Field section of the Regex Builder dialog.This view provides input text options for generating regex
 * pattern.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see View
 * @see RegexFieldDetailPresenter
 */
public class RegexFieldDetailView extends BatchClassInlineView<RegexFieldDetailPresenter> {

	/**
	 * startsEndsStringListBox {@link ListBox} is a list box used to show all the options for start and end string.
	 */
	@UiField
	protected ComboBox startsEndsStringComboBox;

	/**
	 * followedByListBox {@link ListBox} is a list box used to show all the options for followedByListBox.
	 */
	@UiField
	protected ComboBox followedByComboBox;

	/**
	 * exactTextCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected RadioButton exactTextRadioButton;

	/**
	 * exactTextLabel {@link Label} is the label used to show the heading for the panel.
	 */
	@UiField
	protected Label exactTextLabel;

	/**
	 * textInRangeCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected RadioButton textInRangeRadioButton;

	/**
	 * textInRangeLabel {@link Label} is the label used to show the heading for the panel.
	 */
	@UiField
	protected Label textInRangeLabel;

	/**
	 * matchExactTextPanel {@link VerticalPanel} is the vertical panel for match exact text inputs.
	 */
	@UiField
	protected VerticalPanel matchExactTextPanel;

	/**
	 * matchTextInRangePanel {@link VerticalPanel} is the vertical panel for match text in range inputs.
	 */
	@UiField
	protected VerticalPanel matchTextInRangePanel;

	/**
	 * wordBoundaryPanel {@link HorizontalPanel} is the horizontal panel.
	 */
	@UiField
	protected HorizontalPanel wordBoundaryPanel;

	/**
	 * matchStringListBox {@link ListBox} is a list box used to show all the options for matchStringListBox.
	 */
	@UiField
	protected ComboBox matchStringComboBox;
	/**
	 * matchExactTextCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox matchExactTextCheckBox;

	/**
	 * matchExactTextLabel {@link Label} is the label used to show the heading for the panel.
	 */
	@UiField
	protected Label matchExactTextLabel;

	/**
	 * asFewAsPossibleCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected TextField matchExactTextTextField;

	/**
	 * anyCharacterCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox anyCharacterCheckBox;

	/**
	 * anyCharacterLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label anyCharacterLabel;

	/**
	 * lowerCaseLetterCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox lowerCaseLetterCheckBox;

	/**
	 * lowerCaseLetterLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label lowerCaseLetterLabel;

	/**
	 * upperCaseLetterCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox upperCaseLetterCheckBox;

	/**
	 * upperCaseLetterLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label upperCaseLetterLabel;

	/**
	 * digitCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox digitCheckBox;

	/**
	 * digitLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label digitLabel;

	/**
	 * wordBoundaryCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox wordBoundaryCheckBox;

	/**
	 * wordBoundaryLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label wordBoundaryLabel;

	/**
	 * wordBoundaryListBox {@link ListBox} is the list box used for defining options for word boundary.
	 */
	@UiField
	protected ComboBox wordBoundaryComboBox;

	/**
	 * asFewAsPossibleCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox freeTextCheckBox;

	/**
	 * freeTextLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label freeTextLabel;

	/**
	 * freeTextTextBox {@link TextBox} is a text box used for defining the free text.
	 */
	@UiField
	protected TextField freeTextTextField;

	/**
	 * specialCharCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox specialCharCheckBox;

	/**
	 * specialCharLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label specialCharLabel;

	/**
	 * specialCharTextBox {@link TextBox} is a text box used for taking input for special characters.
	 */
	@UiField
	protected TextField specialCharTextField;

	/**
	 * anyLetterCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox anyLetterCheckBox;

	/**
	 * anyLetterLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label anyLetterLabel;

	/**
	 * whiteSpaceCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox whiteSpaceCheckBox;

	/**
	 * whiteSpaceLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label whiteSpaceLabel;

	/**
	 * digitRangeCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox digitRangeCheckBox;

	/**
	 * digitRangeLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label digitRangeLabel;

	/**
	 * lowerDigitRangeTextBox {@link TextBox} is a text box used for taking input for lower digit range.
	 */
	@UiField
	protected TextField lowerDigitRangeTextField;

	/**
	 * digitDashLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label digitDashLabel;

	/**
	 * upperDigitRangeTextBox {@link TextBox} is a text box used for taking input for upper digit range.
	 */
	@UiField
	protected TextField upperDigitRangeTextField;

	/**
	 * invalidDigitRangeLabel {@link Label} is a label used to the show the message for invalid range in digit.
	 */
	@UiField
	protected Label invalidDigitRangeLabel;

	/**
	 * lowerCaseLetterRangeCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox lowerCaseLetterRangeCheckBox;

	/**
	 * regexQuantifierLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label lowerCaseLetterRangeLabel;

	/**
	 * lowerRangeLowerCaseLetterTextField {@link TextField} is a text box used for taking input for lower range for lower case letter.
	 */
	@UiField
	protected TextField lowerRangeLowerCaseLetterTextField;

	/**
	 * regexQuantifierLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label lowerCaseLetterDashLabel;

	/**
	 * upperRangeLowerCaseLetterTextBox {@link TextBox} is a text box used for taking input for upper range for lower case letter.
	 */
	@UiField
	protected TextField upperRangeLowerCaseLetterTextField;

	/**
	 * upperCaseLetterRangeCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox upperCaseLetterRangeCheckBox;

	/**
	 * invalidDigitRangeLabel {@link Label} is a label used to the show the message for invalid range for lower case letter.
	 */
	@UiField
	protected Label invalidLowerCaseLetterRangeLabel;

	/**
	 * regexQuantifierLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label upperCaseLetterRangeLabel;

	/**
	 * lowerRangeUpperCaseLetterTextBox {@link TextBox} is a text box used for taking input for lower range for upper case letter.
	 */
	@UiField
	protected TextField lowerRangeUpperCaseLetterTextField;
	/**
	 * upperCaseLetterDashLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label upperCaseLetterDashLabel;

	/**
	 * upperRangeUpperCaseLetterTextBox {@link TextBox} is a text box used for taking input for upper range for upper case letter.
	 */
	@UiField
	protected TextField upperRangeUpperCaseLetterTextField;
	/**
	 * invalidUpperCaseLetterRangeLabel {@link Label} is a label used to the show the message for invalid range for upper case letter.
	 */
	@UiField
	protected Label invalidUpperCaseLetterRangeLabel;

	/**
	 * horizontalLinePanel {@link HorizontalPanel} is used for the horizontal line to be shown.
	 */
	@UiField
	protected HorizontalPanel horizontalLinePanel;

	/**
	 * textRangeCheckBoxes {@link Set} is the set for all the check boxes of range.
	 */
	private Set<CheckBox> textRangeCheckBoxes;

	/**
	 * textRangeTextBoxes {@link Set} is the set for all the text boxes of range.
	 */
	private Set<TextField> textRangeTextFields;

	/**
	 * specialCharListBox {@link ListBox} is used for the special characters list to be shown.
	 */
	@UiField
	protected ComboBox specialCharComboBox;

	/**
	 * 
	 * Interface for the Binder.
	 * 
	 * @author Ephesoft
	 * 
	 *         <b>created on</b> 19-Dec-2014 <br/>
	 * @version $LastChangedDate:$ <br/>
	 *          $LastChangedRevision:$ <br/>
	 */
	interface Binder extends UiBinder<VerticalPanel, RegexFieldDetailView> {
	}

	/**
	 * BINDER {@link Binder}.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Instantiates the RegexFieldDetailView.
	 */
	public RegexFieldDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		exactTextLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.MATCH_EXACT_TEXT));
		textInRangeLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.TEXT_IN_RANGE));
		matchExactTextLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.MATCH_EXACT_TEXT));
		lowerCaseLetterLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.LOWER_CASE_LETTER));
		upperCaseLetterLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.UPPER_CASE_LETTER));
		digitLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.ANY_DIGIT));
		anyCharacterLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.ANY_CHARACTER));
		wordBoundaryLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.WORD_BOUNDARY));
		freeTextLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.FREE_TEXT));
		specialCharLabel.setText(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.ANY_SPECIAL_CHARACTER_TEXT));
		anyLetterLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.ANY_LETTER_TEXT));
		whiteSpaceLabel.setText(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.ANY_WHITE_SPACE_CHARACTER));
		digitRangeLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.ANY_DIGIT_IN_RANGE));
		invalidDigitRangeLabel.setText(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.INVALID_DIGIT_RANGE));
		invalidLowerCaseLetterRangeLabel.setText(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.INVALID_LOWER_CASE_RANGE));
		invalidUpperCaseLetterRangeLabel.setText(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.INVALID_UPPER_CASE_RANGE));
		lowerCaseLetterRangeLabel.setText(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.LOWER_CASE_LETTER_RANGE));
		upperCaseLetterRangeLabel.setText(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.UPPER_CASE_LETTER_RANGE));

		startsEndsStringComboBox.add(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.MATCH_STRING_CONTAINS));
		startsEndsStringComboBox.add(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.MATCH_STRING_STARTS_WITH));
		startsEndsStringComboBox.add(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.MATCH_STRING_ENDS_IN));
		startsEndsStringComboBox.add(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.MATCH_STRING_STARTS_WITH_ENDS_IN));

		matchStringComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.MATCH_IF_PRESENT));
		matchStringComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.MATCH_IF_ABSENT));
		followedByComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.OR_REGEX));
		followedByComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.ENDS_IN));
		followedByComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.ONLY_IF_FOLLOWED_BY));
		followedByComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.ONLY_IF_NOT_FOLLOWED_BY));
		followedByComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.ONLY_IF_PRECEDED_BY));
		followedByComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.ONLY_IF_NOT_PRECEDED_BY));
		wordBoundaryComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.STARTS_WITH));
		wordBoundaryComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.ENDS_IN));
		wordBoundaryComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.STARTS_WITH_ENDS_IN));

		specialCharComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.SELECT_LABEL));
		specialCharComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.BACKSLASH_CHARACTER));
		specialCharComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.WHITESPACE_CHARACTER));
		specialCharComboBox.add(LocaleDictionary
				.getConstantValue(RegexBuilderConstants.NON_WHITESPACE_CHARACTER));
		specialCharComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.WORD_CHARACTER));
		specialCharComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.NON_WORD_CHARACTER));
		specialCharComboBox.add(LocaleDictionary.getConstantValue(RegexBuilderConstants.NON_DIGIT));

		digitDashLabel.setText(RegexBuilderConstants.DASH);

		lowerCaseLetterDashLabel.setText(RegexBuilderConstants.DASH);
		upperCaseLetterDashLabel.setText(RegexBuilderConstants.DASH);

		addTextRangeCheckBoxes();
		addTextRangeTextFields();

		// Setting the Default values for the components of the view.
		setDefaultValues();

		// Add CSS to the components of the view.
		addCSSStyles();

		// Add Help Messages to the component of the view.
		addHelpMessages();

		this.comboBoxChangeEvent();
		this.textFieldEvents();

	}

	/**
	 * Sets the default values for the components of the view.
	 */
	private void setDefaultValues() {

		// Put all the text box in Range as disabled initially.
		setEnableTextRangeTextFields(false);
		setCheckedTextRangeCheckBoxes(false);
		specialCharComboBox.setEnabled(false);
		specialCharComboBox.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.SELECT_LABEL));

		wordBoundaryComboBox.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.STARTS_WITH));

		startsEndsStringComboBox.setWidth("215px");
		startsEndsStringComboBox.setValue(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.MATCH_STRING_CONTAINS));
		startsEndsStringComboBox.setText(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.MATCH_STRING_CONTAINS));
		followedByComboBox.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.FOLLOWED_BY));
		matchStringComboBox.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.MATCH_IF_PRESENT));
		matchExactTextPanel.setVisible(true);
		matchTextInRangePanel.setVisible(false);
		exactTextRadioButton.setValue(true);
		matchExactTextTextField.setEnabled(false);
		specialCharTextField.setEnabled(false);
		specialCharTextField.setText(BatchClassConstants.EMPTY_STRING);
		invalidDigitRangeLabel.setVisible(false);
		invalidLowerCaseLetterRangeLabel.setVisible(false);
		invalidUpperCaseLetterRangeLabel.setVisible(false);

	}

	/**
	 * Add CSS to the components of the view.
	 */
	private void addCSSStyles() {

		// CSS for Label.
		exactTextLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		exactTextLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		exactTextLabel.addStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		exactTextLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		textInRangeLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		textInRangeLabel.addStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		textInRangeLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		matchExactTextLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		matchExactTextLabel.addStyleName(CoreCommonConstants.WIDTH_200_PX);
		matchExactTextLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		lowerCaseLetterLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		lowerCaseLetterLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		lowerCaseLetterLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		upperCaseLetterLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		upperCaseLetterLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		upperCaseLetterLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		digitLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		digitLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		digitLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		freeTextLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		freeTextLabel.addStyleName(CoreCommonConstants.WIDTH_200_PX);
		freeTextLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		anyCharacterLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		anyCharacterLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		anyCharacterLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		wordBoundaryLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		wordBoundaryLabel.addStyleName(CoreCommonConstants.WIDTH_200_PX);
		wordBoundaryLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		specialCharLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		specialCharLabel.addStyleName(CoreCommonConstants.WIDTH_200_PX);
		specialCharLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		anyLetterLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		anyLetterLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);

		whiteSpaceLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		whiteSpaceLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		whiteSpaceLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		digitRangeLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		digitRangeLabel.addStyleName(CoreCommonConstants.WIDTH_200_PX);

		digitDashLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		digitDashLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		digitDashLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);
		digitDashLabel.addStyleName(BatchClassConstants.HYPHEN_SPACE);

		lowerCaseLetterRangeLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		lowerCaseLetterRangeLabel.addStyleName(CoreCommonConstants.WIDTH_200_PX);
		lowerCaseLetterRangeLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		lowerCaseLetterDashLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		lowerCaseLetterDashLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		lowerCaseLetterDashLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);
		lowerCaseLetterDashLabel.addStyleName(BatchClassConstants.HYPHEN_SPACE);

		upperCaseLetterRangeLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		upperCaseLetterRangeLabel.addStyleName(CoreCommonConstants.WIDTH_200_PX);
		upperCaseLetterRangeLabel.addStyleName(CoreCommonConstants.WIDTH_200_PX);
		upperCaseLetterRangeLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		upperCaseLetterDashLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		upperCaseLetterDashLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		upperCaseLetterDashLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);
		upperCaseLetterDashLabel.addStyleName(BatchClassConstants.HYPHEN_SPACE);

		invalidDigitRangeLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		invalidDigitRangeLabel.addStyleName(CoreCommonConstants.HELP_CONTENT_CSS);
		invalidDigitRangeLabel.addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		invalidDigitRangeLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		invalidLowerCaseLetterRangeLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		invalidLowerCaseLetterRangeLabel.addStyleName(CoreCommonConstants.HELP_CONTENT_CSS);
		invalidLowerCaseLetterRangeLabel.addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		invalidLowerCaseLetterRangeLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		invalidUpperCaseLetterRangeLabel.addStyleName(BatchClassConstants.REGEX_BUILDER_LABEL_CSS);
		invalidUpperCaseLetterRangeLabel.addStyleName(CoreCommonConstants.HELP_CONTENT_CSS);
		invalidUpperCaseLetterRangeLabel.addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		invalidUpperCaseLetterRangeLabel.addStyleName(BatchClassConstants.LABEL_TEXT_CSS);

		invalidDigitRangeLabel.addStyleName(BatchClassConstants.DATE_BOX_ERROR_MSG_NEW);
		invalidLowerCaseLetterRangeLabel.addStyleName(BatchClassConstants.DATE_BOX_ERROR_MSG_NEW);
		invalidUpperCaseLetterRangeLabel.addStyleName(BatchClassConstants.DATE_BOX_ERROR_MSG_NEW);

		// CSS for List Box.
		startsEndsStringComboBox.addStyleName(CoreCommonConstants.MARGIN_BOTTOM_5_CSS);
		followedByComboBox.addStyleName(CoreCommonConstants.MARGIN_BOTTOM_5_CSS);
		wordBoundaryComboBox.addStyleName(CoreCommonConstants.MARGIN_BOTTOM_5_CSS);
		wordBoundaryComboBox.addStyleName(CoreCommonConstants.WIDTH_144_PX);
		matchStringComboBox.addStyleName(CoreCommonConstants.MARGIN_BOTTOM_5_CSS);
		specialCharComboBox.addStyleName(CoreCommonConstants.WIDTH_144_PX);

		// CSS for Text Box.

		lowerDigitRangeTextField.addStyleName(CoreCommonConstants.WIDTH_30_PX);
		upperDigitRangeTextField.addStyleName(CoreCommonConstants.WIDTH_30_PX);
		upperDigitRangeTextField.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		lowerRangeLowerCaseLetterTextField.addStyleName(CoreCommonConstants.WIDTH_30_PX);
		upperRangeLowerCaseLetterTextField.addStyleName(CoreCommonConstants.WIDTH_30_PX);
		upperRangeLowerCaseLetterTextField.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		lowerRangeUpperCaseLetterTextField.addStyleName(CoreCommonConstants.WIDTH_30_PX);
		upperRangeUpperCaseLetterTextField.addStyleName(CoreCommonConstants.WIDTH_30_PX);
		upperRangeUpperCaseLetterTextField.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		lowerDigitRangeTextField.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		lowerRangeLowerCaseLetterTextField.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		lowerRangeUpperCaseLetterTextField.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);

		specialCharTextField.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		specialCharTextField.addStyleName("marRight20");
		matchExactTextTextField.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		// CSS for Horizontal Panel.
		horizontalLinePanel.addStyleName(CoreCommonConstants.HORIZONTAL_LINE_CSS);
	}

	/**
	 * Add help messages to the component of the view.
	 */
	private void addHelpMessages() {
		exactTextRadioButton.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.MATCH_EXACT_TEXT_HELP_MESSAGE));
		textInRangeRadioButton.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.TEXT_IN_RANGE_HELP_MESSAGE));
		matchExactTextCheckBox.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.MATCH_EXACT_TEXT_HELP_MESSAGE));
		anyCharacterCheckBox.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.ANY_CHARACTER_HELP_MESSAGE));
		lowerCaseLetterCheckBox.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.LOWER_CASE_LETTER_RANGE_HELP_MESSAGE));
		upperCaseLetterCheckBox.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.UPPER_CASE_LETTER_HELP_MESSAGE));
		digitCheckBox.setTitle(LocaleDictionary.getConstantValue(RegexBuilderConstants.ANY_DIGIT_HELP_MESSAGE));
		freeTextCheckBox.setTitle(LocaleDictionary
				.getConstantValue(RegexBuilderConstants.FREE_TEXT_HELP_MESSAGE));
		specialCharCheckBox.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.ANY_SPECIAL_CHARACTER_TEXT_HELP_MESSAGE));
		anyLetterCheckBox.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.ANY_LETTER_TEXT_HELP_MESSAGE));
		whiteSpaceCheckBox.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.ANY_WHITE_SPACE_CHARACTER_HELP_MESSAGE));
		digitRangeCheckBox.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.ANY_DIGIT_IN_RANGE_HELP_MESSAGE));
		lowerCaseLetterRangeCheckBox.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.LOWER_CASE_LETTER_RANGE_HELP_MESSAGE));
		upperCaseLetterRangeCheckBox.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.UPPER_CASE_LETTER_RANGE_HELP_MESSAGE));
		wordBoundaryCheckBox.setTitle(LocaleDictionary.getConstantValue(
				RegexBuilderConstants.WORD_BOUNDARY_HELP_MESSAGE));

	}

	/**
	 * sets the group name for the radio buttons.
	 * 
	 * @param groupName {@link String} The group name for the radio buttons.
	 */
	public void setGroupName(final String groupName) {
		exactTextRadioButton.setName(groupName);
		textInRangeRadioButton.setName(groupName);
	}

	/**
	 * Handles the click event for lowerCaseLetterCheckBox, upperCaseLetterCheckBox, digitCheckBox.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = {"lowerCaseLetterCheckBox", "upperCaseLetterCheckBox", "digitCheckBox"})
	public void hendleClickEvents(final ClickEvent clickEvent) {
		presenter.getController().getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Handles the click event for lowerCaseLetterRangeCheckBox. This fires the regex event when a valid value is entered in the field.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "lowerCaseLetterRangeCheckBox")
	public void onLowerCaseLetterRangeCheckBoxClicked(final ClickEvent event) {
		presenter.onLowerCaseLetterRangeCheckBoxClicked(event);
	}

	/**
	 * Handles the click event for upperCaseLetterRangeCheckBox. This fires the regex event when a valid value is entered in the field.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "upperCaseLetterRangeCheckBox")
	public void onUpperCaseLetterRangeCheckBoxClicked(final ClickEvent event) {
		presenter.onUpperCaseLetterRangeCheckBoxClicked(event);
	}

	/**
	 * Handles the click event for digitRangeCheckBox. This fires the regex event when a valid value is entered in the field.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "digitRangeCheckBox")
	public void onDigitRangeCheckBoxClicked(final ClickEvent event) {
		presenter.onDigitRangeCheckBoxClicked(event);
	}

	/**
	 * Handles the click event for freeTextCheckBox. This fires the regex event when a valid value is entered in the field.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "freeTextCheckBox")
	public void onFreeTextCheckBoxClicked(final ClickEvent event) {
		presenter.onFreeTextCheckBoxClicked(event);

	}

	/**
	 * Handles the click event for specialCharCheckBox. This fires the regex event when a valid value is entered in the field.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "specialCharCheckBox")
	public void onSpecialCharCheckBoxClicked(final ClickEvent event) {
		presenter.onSpecialCharCheckBoxClicked(event);
	}

	/**
	 * Handles the click event for matchExactTextCheckBox. This fires the regex event when a valid value is entered in the field.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "matchExactTextCheckBox")
	public void onMatchExactTextCheckBoxClicked(final ClickEvent event) {
		presenter.onMatchExactTextCheckBoxClicked(event);
	}

	/**
	 * Handles the click event for anyCharacterCheckBox. This fires the regex event when a valid value is entered in the field.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "anyCharacterCheckBox")
	public void onAnyCharacterCheckBoxClicked(final ClickEvent event) {
		presenter.onAnyCharacterCheckBoxClicked(event);
	}

	/**
	 * Handles the click event for wordBoundaryCheckBox. This fires the regex event when a valid value is entered in the field.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "wordBoundaryCheckBox")
	public void onWordBoundaryCheckBoxClicked(final ClickEvent event) {
		presenter.onWordBoundaryCheckBoxClicked(event);
	}

	/**
	 * Handles the click event for anyLetterCheckBox. This fires the regex event when a valid value is entered in the field.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "anyLetterCheckBox")
	public void onAnyLetterCheckBoxClicked(final ClickEvent event) {
		presenter.getController().getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Handles the click event for whiteSpaceCheckBox. This fires the regex event when a valid value is entered in the field.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "whiteSpaceCheckBox")
	public void onWhiteSpaceCheckBoxClicked(final ClickEvent event) {
		presenter.getController().getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Handles the click event for exactTextRadioButton. This fires the regex event when a valid radiobutton is selected.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "exactTextRadioButton")
	public void onExactTextRadioButtonChecked(final ValueChangeEvent<Boolean> event) {
		presenter.onExactTextRadioButtonChecked(event.getValue());
	}

	/**
	 * Handles the click event for textInRangeRadioButton. This fires the regex event when a valid radiobutton is selected.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "textInRangeRadioButton")
	public void onTextInRangeRadioButtonChecked(final ValueChangeEvent<Boolean> event) {
		presenter.onTextInRangeRadioButtonChecked(event.getValue());
	}

	private void addTextRangeCheckBoxes() {
		if (textRangeCheckBoxes == null) {
			textRangeCheckBoxes = new HashSet<CheckBox>();
			textRangeCheckBoxes.add(lowerCaseLetterCheckBox);
			textRangeCheckBoxes.add(upperCaseLetterCheckBox);
			textRangeCheckBoxes.add(digitCheckBox);
			textRangeCheckBoxes.add(freeTextCheckBox);
			textRangeCheckBoxes.add(anyLetterCheckBox);
			textRangeCheckBoxes.add(whiteSpaceCheckBox);
			textRangeCheckBoxes.add(digitRangeCheckBox);
			textRangeCheckBoxes.add(lowerCaseLetterRangeCheckBox);
			textRangeCheckBoxes.add(upperCaseLetterRangeCheckBox);
		}
	}

	private void addTextRangeTextFields() {
		if (textRangeTextFields == null) {
			textRangeTextFields = new HashSet<TextField>();
			textRangeTextFields.add(freeTextTextField);
			textRangeTextFields.add(lowerDigitRangeTextField);
			textRangeTextFields.add(upperDigitRangeTextField);
			textRangeTextFields.add(lowerRangeLowerCaseLetterTextField);
			textRangeTextFields.add(upperRangeLowerCaseLetterTextField);
			textRangeTextFields.add(lowerRangeUpperCaseLetterTextField);
			textRangeTextFields.add(upperRangeUpperCaseLetterTextField);
		}
	}

	/**
	 * Enables or diables all the checkboxes boxes in range section based on the value passed. If <code>isEnabled</code> is true then
	 * it will enable all the check boxes otherwise disables all the check boxes.
	 * 
	 * @param isEnabled <code>true</code> value enables the check boxes in range and false value will disables the check boxes.
	 */
	public void setEnableTextRangeCheckBoxes(final boolean isEnabled) {
		if (!CollectionUtil.isEmpty(textRangeCheckBoxes)) {
			for (CheckBox checkBox : textRangeCheckBoxes) {
				checkBox.setEnabled(isEnabled);
			}
		}
	}

	/**
	 * Enables or diables all the text boxes in range section based on the value passed. If <code>isEnabled</code> is true then it will
	 * enable all the text boxes otherwise disables all the text boxes.
	 * 
	 * @param isEnabled <code>true</code> value enables the text boxes in range and false value will disables the text boxes.
	 */

	public void setEnableTextRangeTextFields(final boolean isEnabled) {
		if (!CollectionUtil.isEmpty(textRangeTextFields)) {
			for (TextField textField : textRangeTextFields) {
				textField.setEnabled(isEnabled);
				textField.setText(BatchClassConstants.EMPTY_STRING);
			}
		}
	}

	/**
	 * Checks or unchecks all the check boxes in range section based on the value passed. If <code>isChecked</code> is true then it
	 * will checked all the checkboxes otherwise unchecked all the checkboxes.
	 * 
	 * @param isChecked <code>true</code> value will checked all the checkboxes otherwise unchecked all the checkboxes.
	 */
	public void setCheckedTextRangeCheckBoxes(final boolean isChecked) {
		if (!CollectionUtil.isEmpty(textRangeCheckBoxes)) {
			for (CheckBox checkBox : textRangeCheckBoxes) {
				checkBox.setValue(isChecked);
			}
		}
	}

	/**
	 * Getter for the startsEndsStringListBox.
	 * 
	 * @return {@link ListBox}The instance of startsEndsStringListBox.
	 */

	public ComboBox getStartsEndsStringComboBox() {
		return startsEndsStringComboBox;
	}

	/**
	 * Getter for the followedByListBox.
	 * 
	 * @return {@link ListBox}The instance of followedByListBox.
	 */

	public ComboBox getFollowedByComboBox() {
		return followedByComboBox;
	}

	/**
	 * Getter for the matchStringComboBox.
	 * 
	 * @return {@link ListBox}The instance of matchStringComboBox.
	 */

	public ComboBox getMatchStringComboBox() {
		return matchStringComboBox;
	}

	/**
	 * Getter for the instance of matchExactTextCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of matchExactTextCheckBox.
	 */
	public CheckBox getMatchExactTextCheckBox() {
		return matchExactTextCheckBox;
	}

	/**
	 * Getter for the instance of matchExactTextTextBox.
	 * 
	 * @return {@link TextBox} The instance of matchExactTextTextBox.
	 */

	public TextField getMatchExactTextTextField() {
		return matchExactTextTextField;
	}

	/**
	 * Getter for the instance of lowerCaseLetterCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of lowerCaseLetterCheckBox.
	 */
	public CheckBox getLowerCaseLetterCheckBox() {
		return lowerCaseLetterCheckBox;
	}

	/**
	 * Getter for the instance of upperCaseLetterCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of upperCaseLetterCheckBox.
	 */
	public CheckBox getUpperCaseLetterCheckBox() {
		return upperCaseLetterCheckBox;
	}

	/**
	 * Getter for the instance of digitCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of digitCheckBox.
	 */
	public CheckBox getDigitCheckBox() {
		return digitCheckBox;
	}

	/**
	 * Getter for the instance of anyCharacterCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of anyCharacterCheckBox.
	 */
	public CheckBox getAnyCharacterCheckBox() {
		return anyCharacterCheckBox;
	}

	/**
	 * Getter for the instance of wordBoundaryCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of wordBoundaryCheckBox.
	 */
	public CheckBox getWordBoundaryCheckBox() {
		return wordBoundaryCheckBox;
	}

	/**
	 * Getter for the instance of freeTextCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of freeTextCheckBox.
	 */
	public CheckBox getFreeTextCheckBox() {
		return freeTextCheckBox;
	}

	/**
	 * Getter for the instance of freeTextTextBox.
	 * 
	 * @return {@link TextBox} The instance of freeTextTextBox.
	 */

	public TextField getFreeTextTextField() {
		return freeTextTextField;
	}

	/**
	 * Getter for the instance of specialCharCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of specialCharCheckBox.
	 */
	public CheckBox getSpecialCharCheckBox() {
		return specialCharCheckBox;
	}

	/**
	 * Getter for the instance of specialCharTextBox.
	 * 
	 * @return {@link TextBox} The instance of specialCharTextBox.
	 */
	public TextField getSpecialCharTextField() {
		return specialCharTextField;
	}

	/**
	 * Getter for the instance of anyLetterCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of anyLetterCheckBox.
	 */
	public CheckBox getAnyLetterCheckBox() {
		return anyLetterCheckBox;
	}

	/**
	 * Getter for the instance of asFewAsPossibleCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of asFewAsPossibleCheckBox.
	 */
	public CheckBox getWhiteSpaceCheckBox() {
		return whiteSpaceCheckBox;
	}

	/**
	 * Getter for the instance of digitRangeCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of digitRangeCheckBox.
	 */
	public CheckBox getDigitRangeCheckBox() {
		return digitRangeCheckBox;
	}

	/**
	 * Getter for the instance of lowerDigitRangeTextBox.
	 * 
	 * @return {@link TextBox} The instance of lowerDigitRangeTextBox.
	 */

	public TextField getLowerDigitRangeTextField() {
		return lowerDigitRangeTextField;
	}

	/**
	 * Getter for the instance of upperDigitRangeTextBox.
	 * 
	 * @return {@link TextBox} The instance of upperDigitRangeTextBox.
	 */

	public TextField getUpperDigitRangeTextField() {
		return upperDigitRangeTextField;
	}

	/**
	 * Getter for the instance of lowerCaseLetterRangeCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of lowerCaseLetterRangeCheckBox.
	 */
	public CheckBox getLowerCaseLetterRangeCheckBox() {
		return lowerCaseLetterRangeCheckBox;
	}

	/**
	 * Getter for the instance of lowerRangeLowerCaseLetterTextField.
	 * 
	 * @return {@link TextBox} The instance of lowerRangeLowerCaseLetterTextBox.
	 */

	public TextField getLowerRangeLowerCaseLetterTextField() {
		return lowerRangeLowerCaseLetterTextField;
	}

	/**
	 * Getter for the instance of upperRangeLowerCaseLetterTextBox.
	 * 
	 * @return {@link TextBox} The instance of upperRangeLowerCaseLetterTextBox.
	 */

	public TextField getUpperRangeLowerCaseLetterTextField() {
		return upperRangeLowerCaseLetterTextField;
	}

	/**
	 * Getter for the instance of upperCaseLetterRangeCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of upperCaseLetterRangeCheckBox.
	 */
	public CheckBox getUpperCaseLetterRangeCheckBox() {
		return upperCaseLetterRangeCheckBox;
	}

	/**
	 * Getter for the instance of lowerRangeUpperCaseLetterTextField.
	 * 
	 * @return {@link TextBox} The instance of lowerRangeUpperCaseLetterTextField.
	 */

	public TextField getLowerRangeUpperCaseLetterTextField() {
		return lowerRangeUpperCaseLetterTextField;
	}

	/**
	 * Getter for the instance of upperRangeUpperCaseLetterTextBox.
	 * 
	 * @return {@link TextBox} The instance of upperRangeUpperCaseLetterTextBox.
	 */

	public TextField getUpperRangeUpperCaseLetterTextField() {
		return upperRangeUpperCaseLetterTextField;
	}

	/**
	 * Getter for the wordBoundaryComboBox.
	 * 
	 * @return {@link ListBox}The instance of wordBoundaryComboBox.
	 */

	public ComboBox getWordBoundaryComboBox() {
		return wordBoundaryComboBox;
	}

	/**
	 * @return the invalidDigitRangeLabel
	 */
	public Label getInvalidDigitRangeLabel() {
		return invalidDigitRangeLabel;
	}

	/**
	 * @return the invalidLowerCaseLetterRangeLabel
	 */
	public Label getInvalidLowerCaseLetterRangeLabel() {
		return invalidLowerCaseLetterRangeLabel;
	}

	/**
	 * @return the invalidUpperCaseLetterRangeLabel
	 */
	public Label getInvalidUpperCaseLetterRangeLabel() {
		return invalidUpperCaseLetterRangeLabel;
	}

	/**
	 * @return the matchExactTextPanel
	 */
	public VerticalPanel getMatchExactTextPanel() {
		return matchExactTextPanel;
	}

	/**
	 * @return the matchTextInRangePanel
	 */
	public VerticalPanel getMatchTextInRangePanel() {
		return matchTextInRangePanel;
	}

	/**
	 * @return the specialCharComboBox
	 */

	public ComboBox getSpecialCharComboBox() {
		return specialCharComboBox;
	}

	public void comboBoxChangeEvent() {
		startsEndsStringComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				presenter.getController().getEventBus().fireEvent(new RegexEvent());
			}
		});

		specialCharComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				presenter.onSpecialCharactersSelected();
			}
		});

		followedByComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				presenter.getController().getEventBus().fireEvent(new RegexEvent());
			}
		});

		matchStringComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				presenter.getController().getEventBus().fireEvent(new RegexEvent());
			}
		});

		wordBoundaryComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				presenter.getController().getEventBus().fireEvent(new RegexEvent());
			}
		});

	}

	public void textFieldEvents() {

		matchExactTextTextField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.getController().getEventBus().fireEvent(new RegexEvent());
			}
		});

		freeTextTextField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.getController().getEventBus().fireEvent(new RegexEvent());
			}
		});

		specialCharTextField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.getController().getEventBus().fireEvent(new RegexEvent());
			}
		});

		lowerDigitRangeTextField.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				presenter.handleKeyPressEventForDigit(event);

			}
		});

		lowerDigitRangeTextField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.handleLowerDigitRangeTextBoxKeyUpEvents();

			}
		});

		upperDigitRangeTextField.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				presenter.handleKeyPressEventForDigit(event);

			}
		});

		upperDigitRangeTextField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.handleUpperDigitRangeTextBoxKeyUpEvents();

			}
		});

		lowerRangeLowerCaseLetterTextField.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				presenter.handleKeyPressEventForLetter(event);

			}
		});

		lowerRangeLowerCaseLetterTextField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.handleLowerRangeLowerCaseLetterTextBoxKeyUpEvents();

			}
		});

		upperRangeLowerCaseLetterTextField.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				presenter.handleKeyPressEventForLetter(event);

			}
		});

		upperRangeLowerCaseLetterTextField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.handleUpperRangeLowerCaseLetterTextBoxKeyUpEvents();

			}
		});

		lowerRangeUpperCaseLetterTextField.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				presenter.handleKeyPressEventForLetter(event);

			}
		});

		lowerRangeUpperCaseLetterTextField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.handleLowerRangeUpperCaseLetterTextBoxKeyUpEvents();

			}
		});

		upperRangeUpperCaseLetterTextField.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				presenter.handleKeyPressEventForLetter(event);

			}
		});

		upperRangeUpperCaseLetterTextField.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.handleUpperRangeUpperCaseLetterTextBoxKeyUpEvents();

			}
		});
	}
}
