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

package com.ephesoft.gxt.systemconfig.client.view.application.regexbuilder;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.event.regexbuilder.RegexEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.presenter.application.regexbuilder.RegexQuantifierDetailPresenter;
import com.ephesoft.gxt.systemconfig.client.regexbuilder.RegexBuilderConstants;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Shows the view of Regex Quantifier section of the Regex Builder dialog.This view provides quantitative options used for generating
 * regex pattern.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see View
 * @see RegexQuantifierDetailPresenter
 */
public class RegexQuantifierDetailView extends SystemConfigInlineView<RegexQuantifierDetailPresenter> {

	/**
	 * regexQuantifierHeading {@link Label} is the label used to show the heading for the panel.
	 */
	@UiField
	protected ContentPanel regexQuantifierContainer;

	/**
	 * asFewAsPossibleCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox asFewAsPossibleCheckBox;

	/**
	 * asFewAsPossibleLabel {@link Label} is used for the label text to be shown.
	 */
	@UiField
	protected Label asFewAsPossibleLabel;

	/**
	 * exactlyOneTimesRadioButton {@link RadioButton} is used for the radio button to be shown.
	 */
	@UiField
	protected RadioButton exactlyOneTimesRadioButton;

	/**
	 * betweenZeroAndOneTimesRadioButton {@link RadioButton} is used for the radio button to be shown.
	 */
	@UiField
	protected RadioButton betweenZeroAndOneTimesRadioButton;

	/**
	 * anyNoOfTimesRadioButton {@link RadioButton} is used for the radio button to be shown.
	 */
	@UiField
	protected RadioButton anyNoOfTimesRadioButton;

	/**
	 * oneOrMoreTimesRadioButton {@link RadioButton} is used for the radio button to be shown.
	 */
	@UiField
	protected RadioButton oneOrMoreTimesRadioButton;

	/**
	 * exactlyNoOfTimesRadioButton {@link RadioButton} is used for the radio button to be shown.
	 */
	@UiField
	protected RadioButton exactlyNoOfTimesRadioButton;

	/**
	 * betweenMinAndMaxTimesRadioButton {@link RadioButton} is used for the radio button to be shown.
	 */
	@UiField
	protected RadioButton betweenMinAndMaxTimesRadioButton;

	/**
	 * txtNoOfTimes {@link TextBox} is used for the text box to be shown.
	 */
	@UiField
	protected TextField txtNoOfTimes;
	/**
	 * txtMinNoOfTimes {@link TextBox} is used for the text box to be shown.
	 */
	@UiField
	protected TextField txtMinNoOfTimes;

	/**
	 * dashLabel {@link Label} is the label used to show the text to be shown.
	 */
	@UiField
	protected Label dashLabel;

	/**
	 * txtMaxNoOfTimes {@link TextBox} is used for the text box to be shown.
	 */
	@UiField
	protected TextField txtMaxNoOfTimes;

	/**
	 * invalidRangeLabel {@link Label} is a label used to the show the message for invalid range in digit.
	 */
	@UiField
	protected Label invalidRangeLabel;

	/**
	 * horizontalLinePanel {@link HorizontalPanel} is used for the horizontal line to be shown.
	 */
	@UiField
	protected HorizontalPanel horizontalLinePanel;

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
	interface Binder extends UiBinder<VerticalPanel, RegexQuantifierDetailView> {
	}

	/**
	 * BINDER {@link Binder}.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Instantiates the RegexQuantifierDetailView.
	 */
	public RegexQuantifierDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		asFewAsPossibleLabel.setText(StringUtil.concatenate(SystemConfigConstants.SPACE,
				LocaleDictionary.getConstantValue(RegexBuilderConstants.AS_FEW_AS_POSSIBLE)));
		regexQuantifierContainer.setHeadingText(StringUtil.concatenate(SystemConfigConstants.SPACE,
				LocaleDictionary.getConstantValue(RegexBuilderConstants.REGEX_QUANTIFIER)));
		exactlyOneTimesRadioButton.setText(StringUtil.concatenate(SystemConfigConstants.SPACE,
				LocaleDictionary.getConstantValue(RegexBuilderConstants.EXACTLY_ONE_TIME)));
		betweenZeroAndOneTimesRadioButton.setText(StringUtil.concatenate(SystemConfigConstants.SPACE,
				LocaleDictionary.getConstantValue(RegexBuilderConstants.BETWEEN_0_AND_1_TIME)));
		anyNoOfTimesRadioButton.setText(StringUtil.concatenate(SystemConfigConstants.SPACE,
				LocaleDictionary.getConstantValue(RegexBuilderConstants.ANY_NO_OF_TIMES)));
		oneOrMoreTimesRadioButton.setText(StringUtil.concatenate(SystemConfigConstants.SPACE,
				LocaleDictionary.getConstantValue(RegexBuilderConstants.ONE_OR_MORE_TIMES)));
		exactlyNoOfTimesRadioButton.setText(StringUtil.concatenate(SystemConfigConstants.SPACE,
				LocaleDictionary.getConstantValue(RegexBuilderConstants.EXACTLY_NO_OF_TIMES)));
		betweenMinAndMaxTimesRadioButton.setText(StringUtil.concatenate(SystemConfigConstants.SPACE,
				LocaleDictionary.getConstantValue(RegexBuilderConstants.BETWEEN_MIN_AND_MAX_TIME)));
		dashLabel.setText(RegexBuilderConstants.DASH);
		invalidRangeLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.INVALID_DIGIT_RANGE));

		// Setting the Default values for the components of the view.
		setDefaultValues();

		// Adding CSS to the component of View.
		addCSSStyles();

		// Add help messages to the component of the view.
		addHelpMessages();

		this.textFieldEvents();
	}

	/**
	 * Sets the default values for the components of the view.
	 */
	public void setDefaultValues() {

		// Default Radio Button selected is exactlyOneTimesRadioButton.
		exactlyOneTimesRadioButton.setValue(true);

		// Default make invisible the label and text box.
		setVisibleMinMaxTextBox(false);
		setVisibleNoOfTimesTextBox(false);
		invalidRangeLabel.setVisible(false);

		// By default disabled the check box.
		setEnableAsFewAsPossibleCheckBox(false);
	}

	/**
	 * Add CSS to the component of the view.
	 */
	private void addCSSStyles() {

		// Add CSS for Label.

		asFewAsPossibleLabel.addStyleName(SystemConfigConstants.REGEX_BUILDER_LABEL_CSS);
		asFewAsPossibleLabel.addStyleName(SystemConfigConstants.LABEL_TEXT_CSS);
		regexQuantifierContainer.addStyleName("panelHeader");

		dashLabel.addStyleName(SystemConfigConstants.REGEX_BUILDER_LABEL_CSS);
		dashLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		dashLabel.addStyleName(SystemConfigConstants.LABEL_TEXT_CSS);
		dashLabel.addStyleName(SystemConfigConstants.HYPHEN_SPACE);

		invalidRangeLabel.addStyleName(SystemConfigConstants.REGEX_BUILDER_LABEL_CSS);
		invalidRangeLabel.addStyleName(CoreCommonConstants.HELP_CONTENT_CSS);
		invalidRangeLabel.addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		invalidRangeLabel.addStyleName(SystemConfigConstants.LABEL_TEXT_CSS);
		invalidRangeLabel.addStyleName(SystemConfigConstants.DATE_BOX_ERROR_MSG_NEW);

		// Add CSS for TextBox.
		txtNoOfTimes.addStyleName(CoreCommonConstants.WIDTH_30_PX);
		txtMinNoOfTimes.addStyleName(CoreCommonConstants.WIDTH_30_PX);
		txtMinNoOfTimes.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		txtMaxNoOfTimes.addStyleName(CoreCommonConstants.WIDTH_30_PX);
		txtMaxNoOfTimes.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);

		// Add CSS for radio button.
		exactlyNoOfTimesRadioButton.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		betweenMinAndMaxTimesRadioButton.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);

		// Add CSS for Horizontal Line Panel.
		horizontalLinePanel.addStyleName(CoreCommonConstants.HORIZONTAL_LINE_CSS);
	}

	/**
	 * Handles the event when exactlyNoOfTimesRadioButton is being checked or unchecked.
	 * 
	 * @param changeEvent {@link ValueChangeEvent} This event is generated when radio button is checked or unchecked.
	 */
	@UiHandler(value = "exactlyNoOfTimesRadioButton")
	public void onExactlyNoOfTimesRadioButtonClicked(final ValueChangeEvent<Boolean> changeEvent) {
		presenter.onExactlyNoOfTimesRadioButtonClicked(changeEvent.getValue());
	}

	/**
	 * Handles the event when betweenMinAndMaxTimesRadioButton is being checked or unchecked.
	 * 
	 * @param changeEvent {@link ValueChangeEvent} This event is generated when radio button is checked or unchecked.
	 */
	@UiHandler(value = "betweenMinAndMaxTimesRadioButton")
	public void onBetweenMinAndMaxTimesRadioButtonClicked(final ValueChangeEvent<Boolean> changeEvent) {
		presenter.onBetweenMinAndMaxTimesRadioButtonClicked(changeEvent.getValue());
	}

	/**
	 * Handles the event when exactlyOneTimesRadioButton is being checked or unchecked.
	 * 
	 * @param changeEvent {@link ValueChangeEvent} This event is generated when radio button is checked or unchecked.
	 */
	@UiHandler(value = "exactlyOneTimesRadioButton")
	public void onExactlyOneTimesRadioButtonClicked(final ValueChangeEvent<Boolean> changeEvent) {
		presenter.onExactlyOneTimesRadioButtonClicked(changeEvent.getValue());
	}

	/**
	 * Handles the event when betweenZeroAndOneTimesRadioButton is being checked or unchecked.
	 * 
	 * @param changeEvent {@link ValueChangeEvent} This event is generated when radio button is checked or unchecked.
	 */
	@UiHandler(value = "betweenZeroAndOneTimesRadioButton")
	public void onBetweenZeroAndOneTimesRadioButtonClicked(final ValueChangeEvent<Boolean> changeEvent) {
		presenter.onBetweenZeroAndOneTimesRadioButtonClicked(changeEvent.getValue());
	}

	/**
	 * Handles the event when anyNoOfTimesRadioButton is being checked or unchecked.
	 * 
	 * @param changeEvent {@link ValueChangeEvent} This event is generated when radio button is checked or unchecked.
	 */
	@UiHandler(value = "anyNoOfTimesRadioButton")
	public void onAnyNoOfTimesRadioButtonClicked(final ValueChangeEvent<Boolean> changeEvent) {
		presenter.onAnyNoOfTimesRadioButtonClicked(changeEvent.getValue());
	}

	/**
	 * Handles the event when oneOrMoreTimesRadioButton is being checked or unchecked.
	 * 
	 * @param changeEvent {@link ValueChangeEvent} This event is generated when radio button is checked or unchecked.
	 */
	@UiHandler(value = "oneOrMoreTimesRadioButton")
	public void onOneOrMoreTimesRadioButtonClicked(final ValueChangeEvent<Boolean> changeEvent) {
		presenter.onOneOrMoreTimesRadioButtonClicked(changeEvent.getValue());
	}

	/**
	 * Handles the event when the checkbox is being checked or unchecked.
	 * 
	 * @param clickEvent {@link ClickEvent} This event is being generated when check box is being clicked.
	 */
	@UiHandler(value = "asFewAsPossibleCheckBox")
	public void onAsFewAsPossibleCheckBoxClicked(final ClickEvent clickEvent) {
		presenter.getController().getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Makes the text box visible or unvisible based on the value paased. true value makes the text box visible and false value makes
	 * it invisible.
	 * 
	 * @param isVisibleNoOfTimesTextBoxes true value makes the txtNoOfTimes visible, false value makes the text box invisible.
	 * @param isVisibleMinMaxTimesTextBox true value makes the txtMinNoOfTimes,txtMaxNoOfTimes visible, false value makes the text
	 *            boxes invisible.
	 */
	public void setVisibleTextBox(final boolean isVisibleNoOfTimesTextBoxes, final boolean isVisibleMinMaxTimesTextBox) {
		setVisibleNoOfTimesTextBox(isVisibleNoOfTimesTextBoxes);
		setVisibleMinMaxTextBox(isVisibleMinMaxTimesTextBox);
	}

	/**
	 * Add help messages to the components of the view.
	 */
	private void addHelpMessages() {
		asFewAsPossibleCheckBox.setTitle(LocaleDictionary.getConstantValue(RegexBuilderConstants.AS_FEW_AS_POSSIBLE_HELP_MESSAGE));
		exactlyOneTimesRadioButton.setTitle(LocaleDictionary.getConstantValue(RegexBuilderConstants.EXACTLY_ONE_TIME_HELP_MESSAGE));

		betweenZeroAndOneTimesRadioButton.setTitle(LocaleDictionary
				.getConstantValue(RegexBuilderConstants.BETWEEN_0_OR_1_TIME_HELP_MESSAGE));
		anyNoOfTimesRadioButton.setTitle(LocaleDictionary.getConstantValue(RegexBuilderConstants.ANY_NO_OF_TIMES_HELP_MESSAGE));
		oneOrMoreTimesRadioButton.setTitle(LocaleDictionary.getConstantValue(RegexBuilderConstants.ONE_OR_MORE_TIME_HELP_MESSAGE));
		exactlyNoOfTimesRadioButton.setTitle(LocaleDictionary.getConstantValue(RegexBuilderConstants.REPEAT_N_TIMES_HELP_MESSAGE));
		betweenMinAndMaxTimesRadioButton.setTitle(LocaleDictionary
				.getConstantValue(RegexBuilderConstants.BETWEEN_MAX_AND_MIN_TIME_HELP_MESSAGE));

	}

	/**
	 * sets the group name for the radio buttons.
	 * 
	 * @param groupName {@link String} The group name for the radio buttons.
	 */
	public void setGroupName(final String groupName) {
		exactlyOneTimesRadioButton.setName(groupName);
		betweenZeroAndOneTimesRadioButton.setName(groupName);
		anyNoOfTimesRadioButton.setName(groupName);
		oneOrMoreTimesRadioButton.setName(groupName);
		exactlyNoOfTimesRadioButton.setName(groupName);
		betweenMinAndMaxTimesRadioButton.setName(groupName);
	}

	/**
	 * Makes visible or invisible the txtNoOfTimes {@link TextBox} based on the isVisible value passed.
	 * 
	 * @param isVisible boolean. true value makes the text box visible otherwise make text box invisible.
	 */
	private void setVisibleNoOfTimesTextBox(final boolean isVisible) {
		txtNoOfTimes.setVisible(isVisible);
		txtNoOfTimes.setText(SystemConfigConstants.EMPTY_STRING);
		txtNoOfTimes.removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
	}

	/**
	 * Makes visible or invisible the txtMaxNoOfTimes {@link TextBox} and txtMinNoOfTimes {@link TextBox} based on the isVisible value
	 * passed.
	 * 
	 * @param isVisible boolean. <code>true</code> value makes the text boxes visible otherwise makes text box invisible.
	 */
	private void setVisibleMinMaxTextBox(final boolean isVisible) {
		txtMaxNoOfTimes.setVisible(isVisible);
		txtMaxNoOfTimes.setText(SystemConfigConstants.EMPTY_STRING);
		txtMaxNoOfTimes.removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		txtMinNoOfTimes.setVisible(isVisible);
		txtMinNoOfTimes.setText(SystemConfigConstants.EMPTY_STRING);
		txtMinNoOfTimes.removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		dashLabel.setVisible(isVisible);
		invalidRangeLabel.setVisible(false);
	}

	/**
	 * sets the asFewAsPossibleCheckBox {@link CheckBox} enable true or false based on the isEnable passed.
	 * 
	 * @param isEnable true value makes the check box enable.
	 */
	public void setEnableAsFewAsPossibleCheckBox(final boolean isEnable) {
		asFewAsPossibleCheckBox.setEnabled(isEnable);
	}

	/**
	 * Getter for the instance of asFewAsPossibleCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of asFewAsPossibleCheckBox.
	 */
	public CheckBox getAsFewAsPossibleCheckBox() {
		return asFewAsPossibleCheckBox;
	}

	/**
	 * Getter for the instance of exactlyOneTimesRadioButton.
	 * 
	 * @return {@link RadioButton} The instance of exactlyOneTimesRadioButton.
	 */
	public RadioButton getExactlyOneTimesRadioButton() {
		return exactlyOneTimesRadioButton;
	}

	/**
	 * Getter for the instance of betweenZeroAndOneTimesRadioButton.
	 * 
	 * @return {@link RadioButton} The instance of betweenZeroAndOneTimesRadioButton.
	 */
	public RadioButton getBetweenZeroAndOneTimesRadioButton() {
		return betweenZeroAndOneTimesRadioButton;
	}

	/**
	 * Getter for the instance of anyNoOfTimesRadioButton.
	 * 
	 * @return {@link RadioButton} The instance of anyNoOfTimesRadioButton.
	 */
	public RadioButton getAnyNoOfTimesRadioButton() {
		return anyNoOfTimesRadioButton;
	}

	/**
	 * Getter for the instance of oneOrMoreTimesRadioButton.
	 * 
	 * @return {@link RadioButton} The instance of oneOrMoreTimesRadioButton.
	 */
	public RadioButton getOneOrMoreTimesRadioButton() {
		return oneOrMoreTimesRadioButton;
	}

	/**
	 * Getter for the instance of exactlyNoOfTimesRadioButton.
	 * 
	 * @return {@link RadioButton} The instance of exactlyNoOfTimesRadioButton.
	 */
	public RadioButton getExactlyNoOfTimesRadioButton() {
		return exactlyNoOfTimesRadioButton;
	}

	/**
	 * Getter for the instance of betweenMinAndMaxTimesRadioButton.
	 * 
	 * @return {@link RadioButton} The instance of betweenMinAndMaxTimesRadioButton.
	 */
	public RadioButton getBetweenMinAndMaxTimesRadioButton() {
		return betweenMinAndMaxTimesRadioButton;
	}

	/**
	 * Getter for the instance of txtNoOfTimes.
	 * 
	 * @return {@link TextBox} The instance of txtNoOfTimes.
	 */

	public TextField getTxtNoOfTimes() {
		return txtNoOfTimes;
	}

	/**
	 * Getter for the instance of txtMinNoOfTimes.
	 * 
	 * @return {@link TextBox} The instance of txtMinNoOfTimes.
	 */
	public TextField getTxtMinNoOfTimes() {
		return txtMinNoOfTimes;
	}

	/**
	 * Getter for the instance of txtMaxNoOfTimes.
	 * 
	 * @return {@link TextBox} The instance of txtMaxNoOfTimes.
	 */

	public TextField getTxtMaxNoOfTimes() {
		return txtMaxNoOfTimes;
	}

	/**
	 * @return the invalidRangeLabel
	 */
	public Label getInvalidRangeLabel() {
		return invalidRangeLabel;
	}

	public void textFieldEvents() {

		txtNoOfTimes.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				presenter.handleKeyPressEvent(event);

			}
		});

		txtMaxNoOfTimes.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				presenter.handleKeyPressEvent(event);

			}
		});

		txtMinNoOfTimes.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				presenter.handleKeyPressEvent(event);

			}
		});

		txtNoOfTimes.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.getController().getEventBus().fireEvent(new RegexEvent());

			}
		});

		txtMinNoOfTimes.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.handleKeyUpEventForTxtMinNoOfTimes();

			}
		});

		txtMaxNoOfTimes.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.handleKeyUpEventForTxtMaxNoOfTimes();

			}
		});
	}
}
