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

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AddRegexBuilderFormEvent;
import com.ephesoft.gxt.admin.client.event.RegexEvent;
import com.ephesoft.gxt.admin.client.event.RegexEventHandler;
import com.ephesoft.gxt.admin.client.event.RemoveRegexBuilderFormEvent;
import com.ephesoft.gxt.admin.client.event.ResetRegexBuilderFormEvent;
import com.ephesoft.gxt.admin.client.event.SelectRegexBuilderFormEvent;
import com.ephesoft.gxt.admin.client.event.TestRegexBuilderFormEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.regexbuilder.RegexBuilderConstants;
import com.ephesoft.gxt.admin.client.regexbuilder.RegexEngine;
import com.ephesoft.gxt.admin.client.regexbuilder.RegexFieldDetails;
import com.ephesoft.gxt.admin.client.regexbuilder.RegexGroupDetails;
import com.ephesoft.gxt.admin.client.regexbuilder.RegexQuantifierDetails;
import com.ephesoft.gxt.admin.client.regexbuilder.DTO.RegexBuilderDetailsDTO;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexBuilderDetailView;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexBuilderMainView;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexFieldDetailView;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexGroupDetailView;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexQuantifierDetailView;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * Provides the functionality to handle events for RegexBuilderMainView. This presenter also handles the events for Regex Tester
 * utility.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see BatchClassInlinePresenter
 * @see RegexBuilderMainView
 */
public class RegexBuilderMainPresenter extends BatchClassInlinePresenter<RegexBuilderMainView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<RegexBuilderMainPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * index is used for maintating the count of tab added. This helps in adding , removing the tab and also used in generation of
	 * Regex Pattern logic.
	 */
	private int index = 1;

	/**
	 * regexBuilderResultCSS {@link String} is constant used for applying the CSS on the result of the Regex Tester.
	 */
	private String regexBuilderResultCSS;

	/**
	 * SPAN_FONT_END_TAG {@link String} specifies the end tag for the font and span.
	 */
	private static final String SPAN_FONT_END_TAG = "</span></font>";

	/**
	 * Constructor of the class.
	 * 
	 * @param controller {@link BatchClassManagementController} The instance of BatchClassManagementController.
	 * @param view {@link RegexBuilderMainView} The regex builder main view.
	 */
	public RegexBuilderMainPresenter(final BatchClassManagementController controller, final RegexBuilderMainView view) {
		super(controller, view);
		RegexBuilderDetailPresenter regexBuilderDetailPresenter = new RegexBuilderDetailPresenter(controller,
				view.getRegexBuilderDetailView());
		regexBuilderDetailPresenter.bind();
	}

	@Override
	public void bind() {
		// This method is called when the bind event is being fired.
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
		eventBus.addHandler(RegexEvent.type, new RegexEventHandler() {

			@Override
			public void generateRegex(final RegexEvent regexEvent) {
				generateRegexPattern();

			}
		});
	}

	/**
	 * Generates the regex pattern based on inputs.
	 */
	private String generateRegexPattern() {
		String regexPattern = RegexEngine.generateRegexPattern(getListRegexgeneratorDTO());
		// view.getResultTextArea().setText(regexPattern);
		view.getResultTextArea().setValue(regexPattern);

		if (regexPattern.equals(BatchClassConstants.EMPTY_STRING)) {

			controller.getRegexBuilderView().getRegexBuilderToolBar().toggleFindAllMatchesButton(false);
			controller.getRegexBuilderView().getRegexBuilderToolBar().toggleSelectRegexButton(false);
		} else {
			controller.getRegexBuilderView().getRegexBuilderToolBar().toggleFindAllMatchesButton(true);
			controller.getRegexBuilderView().getRegexBuilderToolBar().toggleSelectRegexButton(true);
		}
		// getController().getRegexBuilderView().getRegexBuilderToolBar().toggleFindAllMatchesButton(true);
		// getController().getRegexBuilderView().getRegexBuilderToolBar().toggleSelectRegexButton(true);
		return regexPattern;
	}

	/**
	 * Closes the regex builder dialog when the user clicks on close button.
	 */
	public void onCloseButtonClicked() {
		view.getDialogWindow().hide();
	}

	/**
	 * <p>
	 * Finds all the matches inside the string for the given regex pattern and matched area will be highlighted . In case if no match
	 * is found then a pop up saying no match is found will appear.
	 */

	@EventHandler
	public void onFindAllMatchesClicked(TestRegexBuilderFormEvent testEvent) {
		if (null != testEvent) {
			String regexPattern = view.getResultTextArea().getText();
			String strToBeMatched = view.getMatchStringTextArea().getText();
			if (StringUtil.isNullOrEmpty(regexPattern)) {

				DialogUtil.showMessageDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
								.getMessageValue(RegexBuilderConstants.REGEX_PATTERN_EMPTY_ERROR_MESSAGE), DialogIcon.ERROR);
			} else if (null == strToBeMatched) {

				DialogUtil.showMessageDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
								.getMessageValue(RegexBuilderConstants.STRING_MATCHED_EMPTY_ERROR_MESSAGE), DialogIcon.ERROR);
			} else {
//				ScreenMaskUtility.maskScreen();
//				findMatchedIndexesList(regexPattern,strToBeMatched);
//				ScreenMaskUtility.unmaskScreen();
				ScreenMaskUtility.maskScreen();
				controller.getRpcService().findMatchedIndexesList(regexPattern, strToBeMatched, new AsyncCallback<List<String>>() {

					@Override
					public void onSuccess(final List<String> matchedIndexList) {
						ScreenMaskUtility.unmaskScreen();
						if (!CollectionUtil.isEmpty(matchedIndexList)) {
							doHiglight(matchedIndexList);
						} else {
							DialogUtil.showMessageDialog(
									LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
											.getMessageValue(RegexBuilderConstants.NO_MATCH_FOUND_ERROR_MESSAGE), DialogIcon.ERROR);
							
							showMatchStringTextArea();
						}
					}

					@Override
					public void onFailure(final Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						DialogUtil.showMessageDialog(
								LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
										.getMessageValue(RegexBuilderConstants.ERROR_OCCURRED_VERIFYING_REGEX), DialogIcon.ERROR);
						
						showMatchStringTextArea();

					}
				});

			}
		}
	}

	private List<RegexBuilderDetailsDTO> getListRegexgeneratorDTO() {
		List<RegexBuilderDetailsDTO> listRegexBuilderDetailsDTOs = new ArrayList<RegexBuilderDetailsDTO>();
		RegexBuilderDetailsDTO regexBuilderDetailsDTO = null;
		RegexGroupDetails regexGroupDetails = null;
		RegexFieldDetails regexFieldDetails = null;
		RegexQuantifierDetails regexQuantifierDetails = null;
		for (RegexBuilderDetailView regexBuilderDetailView : view.getListBuilderDetailViews()) {
			if (regexBuilderDetailView != null) {
				regexBuilderDetailsDTO = new RegexBuilderDetailsDTO();
				regexQuantifierDetails = getRegexQuantifierDetails(regexBuilderDetailView.getRegexQuantifierDetailView());
				regexBuilderDetailsDTO.setRegexQuantifierDetails(regexQuantifierDetails);
				regexGroupDetails = getRegexGroupDetails(regexBuilderDetailView.getRegexGroupDetailView());
				regexBuilderDetailsDTO.setRegexGroupDetails(regexGroupDetails);
				regexFieldDetails = getRegexFieldDetails(regexBuilderDetailView.getRegexFieldDetailView());

				regexBuilderDetailsDTO.setRegexFieldDetails(regexFieldDetails);
				listRegexBuilderDetailsDTOs.add(regexBuilderDetailsDTO);
			}
		}
		return listRegexBuilderDetailsDTOs;
	}

	private RegexQuantifierDetails getRegexQuantifierDetails(final RegexQuantifierDetailView regexQuantifierDetailView) {
		RegexQuantifierDetails regexQuantifierDetails = null;
		if (regexQuantifierDetailView != null) {
			regexQuantifierDetails = new RegexQuantifierDetails();
			if (regexQuantifierDetailView.getAsFewAsPossibleCheckBox().getValue()) {
				regexQuantifierDetails.setAsFewAsPossible(true);
			}
			if (regexQuantifierDetailView.getBetweenZeroAndOneTimesRadioButton().getValue()) {
				regexQuantifierDetails.setBetweenZeroAndOneTimes(true);
			} else if (regexQuantifierDetailView.getAnyNoOfTimesRadioButton().getValue()) {
				regexQuantifierDetails.setAnyNumberOfTimes(true);
			} else if (regexQuantifierDetailView.getOneOrMoreTimesRadioButton().getValue()) {
				regexQuantifierDetails.setOneOrMoreTimes(true);
			} else if (regexQuantifierDetailView.getExactlyNoOfTimesRadioButton().getValue()) {
				regexQuantifierDetails.setNoOfTimes(regexQuantifierDetailView.getTxtNoOfTimes().getText());
			} else if (regexQuantifierDetailView.getBetweenMinAndMaxTimesRadioButton().getValue()) {
				regexQuantifierDetails.setMaximumNumberOfTimes(regexQuantifierDetailView.getTxtMaxNoOfTimes().getText());
				regexQuantifierDetails.setMinimumNumberOfTimes(regexQuantifierDetailView.getTxtMinNoOfTimes().getText());
			}
		}

		return regexQuantifierDetails;
	}

	private RegexGroupDetails getRegexGroupDetails(final RegexGroupDetailView regexGroupDetailView) {
		RegexGroupDetails regexGroupDetails = null;
		if (regexGroupDetailView != null) {
			regexGroupDetails = new RegexGroupDetails();
			if (regexGroupDetailView.getCaseInsensetiveCheckBox().getValue()) {
				regexGroupDetails.setCaseInsensetive(true);
			}
			if (regexGroupDetailView.getStartGroupCheckBox().getValue()) {
				regexGroupDetails.setStartGroup(true);
			}
			if (regexGroupDetailView.getEndGroupCheckBox().getValue()) {
				regexGroupDetails.setEndGroup(true);
			}
			if (regexGroupDetailView.getCaptureGroupCheckBox().getValue()) {
				regexGroupDetails.setCaptureGroup(true);
			}
			if (regexGroupDetailView.getNonCaptureGroupCheckBox().getValue()) {
				regexGroupDetails.setNonCaptureGroup(true);
			}
			if (regexGroupDetailView.getApplyQuantifierToGroupCheckBox().getValue()) {
				regexGroupDetails.setRegexQuantifierAppliedToEntireGroup(true);
			}
		}
		return regexGroupDetails;
	}

	private RegexFieldDetails getRegexFieldDetails(final RegexFieldDetailView regexFieldDetailView) {
		RegexFieldDetails regexFieldDetails = null;
		if (regexFieldDetailView != null) {
			regexFieldDetails = new RegexFieldDetails();

			String selectedValue = regexFieldDetailView.getStartsEndsStringComboBox().getValue();

			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.MATCH_STRING_CONTAINS)
					.equalsIgnoreCase(selectedValue)) {
				regexFieldDetails.setStringContains(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.MATCH_STRING_STARTS_WITH)
					.equalsIgnoreCase(selectedValue)) {
				regexFieldDetails.setStringStartsWith(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.MATCH_STRING_ENDS_IN)
					.equalsIgnoreCase(selectedValue)) {
				regexFieldDetails.setStringEndsIn(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.MATCH_STRING_STARTS_WITH_ENDS_IN)
					.equalsIgnoreCase(selectedValue)) {
				regexFieldDetails.setStringStartsWithAndEndsIn(true);
			}
			String selectedValueFollowed = regexFieldDetailView.getFollowedByComboBox().getValue();

			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.FOLLOWED_BY)
					.equalsIgnoreCase(selectedValueFollowed)) {
				regexFieldDetails.setFollowedBy(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.OR_REGEX)
					.equalsIgnoreCase(selectedValueFollowed)) {
				regexFieldDetails.setOrOperator(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.ENDS_IN)
					.equalsIgnoreCase(selectedValueFollowed)) {
				regexFieldDetails.setEndsIn(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.ONLY_IF_FOLLOWED_BY)
					.equalsIgnoreCase(selectedValueFollowed)) {
				regexFieldDetails.setOnlyIfFollowedBy(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.ONLY_IF_NOT_FOLLOWED_BY)
					.equalsIgnoreCase(selectedValueFollowed)) {
				regexFieldDetails.setOnlyIfNotFollowedBy(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.ONLY_IF_PRECEDED_BY)
					.equalsIgnoreCase(selectedValueFollowed)) {
				regexFieldDetails.setOnlyIfProceedBy(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.ONLY_IF_NOT_PRECEDED_BY)
					.equalsIgnoreCase(selectedValueFollowed)) {
				regexFieldDetails.setOnlyIfNotProceedBy(true);
			}
			String selectedValueMatched = regexFieldDetailView.getMatchStringComboBox().getValue();
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.MATCH_IF_PRESENT)
					.equalsIgnoreCase(selectedValueMatched)) {
				regexFieldDetails.setMatchOnlyIfPresent(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.MATCH_IF_ABSENT)
					.equalsIgnoreCase(selectedValueMatched)) {
				regexFieldDetails.setMatchOnlyIfAbsent(true);
			}

			String fieldValue = getFieldValue(regexFieldDetailView, regexFieldDetails);
			if (!RegexBuilderConstants.RANGE_START_END_REGEX.equalsIgnoreCase(fieldValue)) {
				regexFieldDetails.setFieldValue(fieldValue);
			}
		}
		return regexFieldDetails;
	}

	private String getFieldValue(final RegexFieldDetailView regexFieldDetailView, final RegexFieldDetails regexFieldDetailsDTO) {
		StringBuilder fieldValue = new StringBuilder();
		if (regexFieldDetailView.getWordBoundaryCheckBox().getValue()) {
			String selectedWordBoundaryMatch = regexFieldDetailView.getWordBoundaryComboBox().getValue();
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.STARTS_WITH)
					.equalsIgnoreCase(selectedWordBoundaryMatch)) {

				regexFieldDetailsDTO.setStartsWithWordBoundary(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.ENDS_IN)
					.equalsIgnoreCase(selectedWordBoundaryMatch)) {

				regexFieldDetailsDTO.setEndsInWordBoundary(true);
			}
			if (LocaleDictionary.getConstantValue(RegexBuilderConstants.STARTS_WITH_ENDS_IN)
					.equalsIgnoreCase(selectedWordBoundaryMatch)) {

				regexFieldDetailsDTO.setStartsWithEndsInWordBoundary(true);
			}
		}

		if (regexFieldDetailView.getMatchExactTextCheckBox().getValue()) {

			fieldValue.append(regexFieldDetailView.getMatchExactTextTextField().getText());

		} else if (regexFieldDetailView.getAnyCharacterCheckBox().getValue()) {
			fieldValue.append(RegexBuilderConstants.DOT_REGEX);
		} else if (regexFieldDetailView.getSpecialCharCheckBox().getValue()) {
			fieldValue.append(regexFieldDetailView.getSpecialCharTextField().getText());
		} else {
			fieldValue.append(RegexBuilderConstants.RANGE_START_REGEX);
			if (regexFieldDetailsDTO.isMatchOnlyIfAbsent()) {
				fieldValue.append(RegexBuilderConstants.MATCH_ONLY_IF_ABSENT_REGEX);
			}
			if (regexFieldDetailView.getLowerCaseLetterCheckBox().getValue()) {
				fieldValue.append(RegexBuilderConstants.LOWER_CASE_LETTER_REGEX);
			}
			if (regexFieldDetailView.getUpperCaseLetterCheckBox().getValue()) {
				fieldValue.append(RegexBuilderConstants.UPEER_CASE_LETTER_REGEX);
			}
			if (regexFieldDetailView.getDigitCheckBox().getValue()) {
				fieldValue.append(RegexBuilderConstants.DIGIT_RANGE_REGEX);
			}
			if (regexFieldDetailView.getAnyLetterCheckBox().getValue()) {
				fieldValue.append(RegexBuilderConstants.ANY_LETTER_REGEX);
			}
			if (regexFieldDetailView.getWhiteSpaceCheckBox().getValue()) {
				fieldValue.append(RegexBuilderConstants.WHITE_SPACE_CHARACTER_REGEX);
			}
			if (regexFieldDetailView.getFreeTextCheckBox().getValue()) {
				fieldValue.append(regexFieldDetailView.getFreeTextTextField().getText());
			}
			if (regexFieldDetailView.getDigitRangeCheckBox().getValue()) {
				fieldValue.append(regexFieldDetailView.getLowerDigitRangeTextField().getText());
				fieldValue.append(RegexBuilderConstants.DASH);
				fieldValue.append(regexFieldDetailView.getUpperDigitRangeTextField().getText());

			}
			if (regexFieldDetailView.getLowerCaseLetterRangeCheckBox().getValue()
					&& regexFieldDetailView.getLowerRangeLowerCaseLetterTextField().getText() != null
					&& regexFieldDetailView.getUpperRangeLowerCaseLetterTextField().getText() != null) {
				fieldValue.append(regexFieldDetailView.getLowerRangeLowerCaseLetterTextField().getText().toLowerCase());
				fieldValue.append(RegexBuilderConstants.DASH);
				fieldValue.append(regexFieldDetailView.getUpperRangeLowerCaseLetterTextField().getText().toLowerCase());

			}
			if (regexFieldDetailView.getUpperCaseLetterRangeCheckBox().getValue()
					&& regexFieldDetailView.getLowerRangeUpperCaseLetterTextField().getText() != null
					&& regexFieldDetailView.getUpperRangeUpperCaseLetterTextField().getText() != null) {
				fieldValue.append(regexFieldDetailView.getLowerRangeUpperCaseLetterTextField().getText().toUpperCase());
				fieldValue.append(RegexBuilderConstants.DASH);
				fieldValue.append(regexFieldDetailView.getUpperRangeUpperCaseLetterTextField().getText().toUpperCase());
			}
			fieldValue.append(RegexBuilderConstants.RANGE_END_REGEX);

		}

		return fieldValue.toString();
	}

	/**
	 * Shows the matched String text area.
	 */
	private void showMatchStringTextArea() {
		view.getTestPanel().add(view.getMatchStringTextArea());
		view.getMatchStringTextArea().setVisible(true);
		view.getMatchedHTML().setVisible(false);
	}

	/**
	 * Makes invisble the highlighted matched HTML panel and text area becomes visible.
	 */
	public void onMatchedAreaClicked() {
		view.getTestPanel().add(view.getMatchStringTextArea());
		view.getMatchStringTextArea().setText(view.getMatchedHTML().getText());
		view.getMatchStringTextArea().setVisible(true);
		view.getMatchedHTML().setVisible(false);
	}

	/**
	 * Removes the last opened tab if number of tabs is greater than 1 and fires the RegexEvent.
	 */
	@EventHandler
	public void onRemoveButtonClicked(RemoveRegexBuilderFormEvent removeEvent) {
		if (null != removeEvent) {
			if (index > 1) {
				index--;
				view.getListBuilderDetailViews().remove(index);
				view.getTabPanel().remove(index);
				view.getTabPanel().selectTab(index - 1);
			}
			if (view.getListBuilderDetailViews().size() == 1) {
				getController().getRegexBuilderView().getRegexBuilderToolBar().getRemoveButton().setEnabled(false);
			}
			controller.getEventBus().fireEvent(new RegexEvent());
		}
	}

	/**
	 * Adds a new tab on Regex Builder view for generating the regex pattern.
	 */
	@EventHandler
	public void onAddButtonClicked(AddRegexBuilderFormEvent addEvent) {
		if (null != addEvent) {
			RegexBuilderDetailView regexBuilderDetailView = new RegexBuilderDetailView();
			RegexBuilderDetailPresenter regexBuilderDetailPresenter = new RegexBuilderDetailPresenter(controller,
					regexBuilderDetailView);
			regexBuilderDetailPresenter.bind();

			regexBuilderDetailView.getRegexFieldDetailView().getStartsEndsStringComboBox().setVisible(false);

			view.getListBuilderDetailViews().add(regexBuilderDetailView);

			getController().getRegexBuilderView().getRegexBuilderToolBar().getRemoveButton().setEnabled(true);

			view.getTabPanel().add(
					regexBuilderDetailView,
					StringUtil.concatenate(LocaleDictionary.getConstantValue(RegexBuilderConstants.REGEX_STR),
							index));
			view.getTabPanel().selectTab(index);
			regexBuilderDetailView.getRegexQuantifierDetailView().setGroupName(
					StringUtil.concatenate(RegexBuilderConstants.REGEX_QUANTIFIER, index));
			regexBuilderDetailView.getRegexFieldDetailView().setGroupName(
					StringUtil.concatenate(LocaleDictionary.getConstantValue(RegexBuilderConstants.REGEX_STR),
							index));
			index++;
		}
	}

	/**
	 * Reset all the deatils filled on the Regex Builder dialog and fires the RegexEvent.
	 */
	@EventHandler
	public void onResetButtonClicked(ResetRegexBuilderFormEvent resetEvent) {
		if (null != resetEvent) {
			while (index >= 1) {
				index--;
				view.getTabPanel().remove(index);
			}
			RegexBuilderDetailView regexBuilderDetailView = new RegexBuilderDetailView();
			RegexBuilderDetailPresenter regexBuilderDetailPresenter = new RegexBuilderDetailPresenter(controller,
					regexBuilderDetailView);
			regexBuilderDetailPresenter.bind();
			view.getTabPanel().add(regexBuilderDetailView,
					LocaleDictionary.getConstantValue(RegexBuilderConstants.REGEX_STR));
			regexBuilderDetailView.getRegexQuantifierDetailView().setGroupName(RegexBuilderConstants.REGEX_QUANTIFIER);
			regexBuilderDetailView.getRegexFieldDetailView().setGroupName(RegexBuilderConstants.REGEX_STR);
			regexBuilderDetailView.getRegexFieldDetailView().getFollowedByComboBox().setVisible(false);
			view.getTabPanel().selectTab(0);
			view.getListBuilderDetailViews().clear();
			index = 1;
			view.getListBuilderDetailViews().add(regexBuilderDetailView);
			view.getTestPanel().add(view.getMatchStringTextArea());
			view.getMatchStringTextArea().setVisible(true);
			view.getMatchStringTextArea().setText(BatchClassConstants.EMPTY_STRING);
			view.getResultTextArea().setText(BatchClassConstants.EMPTY_STRING);
			view.getMatchedHTML().setVisible(false);
			getController().getRegexBuilderView().getRegexBuilderToolBar().toggleFindAllMatchesButton(false);
			getController().getRegexBuilderView().getRegexBuilderToolBar().toggleSelectRegexButton(false);
			getController().getRegexBuilderView().getRegexBuilderToolBar().getRemoveButton().setEnabled(false);
		}
	}

	public void resetForm() {
		while (index >= 1) {
			index--;
			view.getTabPanel().remove(index);
		}
		RegexBuilderDetailView regexBuilderDetailView = new RegexBuilderDetailView();
		RegexBuilderDetailPresenter regexBuilderDetailPresenter = new RegexBuilderDetailPresenter(controller, regexBuilderDetailView);
		regexBuilderDetailPresenter.bind();
		view.getTabPanel().add(regexBuilderDetailView,
				LocaleDictionary.getConstantValue(RegexBuilderConstants.REGEX_STR));
		regexBuilderDetailView.getRegexQuantifierDetailView().setGroupName(RegexBuilderConstants.REGEX_QUANTIFIER);
		regexBuilderDetailView.getRegexFieldDetailView().setGroupName(RegexBuilderConstants.REGEX_STR);
		regexBuilderDetailView.getRegexFieldDetailView().getFollowedByComboBox().setVisible(false);
		view.getTabPanel().selectTab(0);
		view.getListBuilderDetailViews().clear();
		index = 1;
		view.getListBuilderDetailViews().add(regexBuilderDetailView);
		view.getTestPanel().add(view.getMatchStringTextArea());
		view.getMatchStringTextArea().setVisible(true);
		view.getResultTextArea().setText(BatchClassConstants.EMPTY_STRING);
		view.getMatchedHTML().setVisible(false);
		getController().getRegexBuilderView().getRegexBuilderToolBar().toggleFindAllMatchesButton(false);
		controller.getRegexBuilderView().getRegexBuilderToolBar().toggleSelectRegexButton(false);
		getController().getRegexBuilderView().getRegexBuilderToolBar().getRemoveButton().setEnabled(false);
	}

	/**
	 * Highlights the matched area.
	 * 
	 * @param matchedIndexList {@link List <{@link String} >
	 */
	private void doHiglight(final List<String> matchedIndexList) {
		view.getMatchedHTML().setHTML(getHTMLString(matchedIndexList));
		view.getTestPanel().remove(view.getMatchStringTextArea());
		view.getMatchedHTML().setVisible(true);
		view.getTestPanel().add(view.getMatchedHTML());
		view.getMatchedHTML().addStyleName(CoreCommonConstants.MATCHED_AREA_REGEX_BUILDER_CSS);
	}

	private String getHTMLString(final List<String> matchedIndexList) {
		StringBuilder finalResult = new StringBuilder();
		String lastString = BatchClassConstants.EMPTY_STRING;
		int lastEndIndex = 0;
		regexBuilderResultCSS = CoreCommonConstants.REGEX_BUILDER_RESULT_CSS1;
		if (!CollectionUtil.isEmpty(matchedIndexList)) {
			for (int index = 0; index < matchedIndexList.size(); index += 2) {
				int startIndex = Integer.parseInt(matchedIndexList.get(index));
				int endIndex = Integer.parseInt(matchedIndexList.get(index + 1));
				String matchedString = view.getMatchStringTextArea().getText().substring(startIndex, endIndex);
				String firstString = view.getMatchStringTextArea().getText().substring(lastEndIndex, startIndex);
				lastEndIndex = endIndex;
				lastString = view.getMatchStringTextArea().getText().substring(lastEndIndex);
				finalResult.append(firstString);
				finalResult.append(appendCSS(matchedString));
			}
		}
		finalResult.append(lastString);
		return finalResult.toString();
	}

	private String appendCSS(final String matchedString) {

		// Adding Alternate color logic for the result of Regex builder.
		String resultString = StringUtil.concatenate(regexBuilderResultCSS, matchedString, SPAN_FONT_END_TAG);
		if (CoreCommonConstants.REGEX_BUILDER_RESULT_CSS1.equalsIgnoreCase(regexBuilderResultCSS)) {
			regexBuilderResultCSS = CoreCommonConstants.REGEX_BUILDER_RESULT_CSS2;
		} else {
			regexBuilderResultCSS = CoreCommonConstants.REGEX_BUILDER_RESULT_CSS1;
		}
		return resultString;
	}

	/**
	 * Shows the Regex Builder Main View.
	 */
	public void showRegexBuilderMainView() {
		view.getDialogWindow().setWidth("100%");
		view.getDialogWindow().add(view);
		view.getDialogWindow().setHeadingText(
				LocaleDictionary.getConstantValue(RegexBuilderConstants.REGEX_BUILDER_DIALOG_TITLE));
		view.getDialogWindow().center();
		view.getDialogWindow().show();
		// view.getRegexScrollPanel().scrollToTop();
	}

	/**
	 * Populates the generated regex pattern to the text box from which regex builder utility is opened and closes the Regex Builder
	 * Dialog.
	 * 
	 */
//	@EventHandler
//	public void onSelectRegexButtonClicked11(SelectRegexBuilderFormEvent selectEvent) {
//		if (null != selectEvent) {
//			String regexPattern = view.getResultTextArea().getText();
//			// resetForm();
//			if (StringUtil.isNullOrEmpty(regexPattern)) {
//
//				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
//						LocaleDictionary
//								.getMessageValue(RegexBuilderConstants.REGEX_PATTERN_EMPTY_ERROR_MESSAGE), DialogIcon.INFO);
//
//			} else {
//				controller.getRegexBuilderView().hideRegexDialogWindow();
//				// controller.hideRegexDialogWindow(view);
//				// if (view.isGridOrForm()) {
//				// controller.getCurrentValueProvider().setValue(controller.getCurrentSelectedObject(), regexPattern);
//				// controller.getEventBus().fireEvent(new RefreshEvent(controller.getCurrentSelectedObject()));
//				// } else {
//				// view.getRegexComboBox().setText(regexPattern);
//				// }
//				controller.getRegexBuilderView().getRegexBuilderToolBar().getPresenter().resetRegexBuilderForm();
//				// controller.getCurrentValueProvider().setValue(controller.getCurrentSelectedObject(), regexPattern);
//				if (null != controller.getRegexBuilderView().getRegexComboBoxHandler()) {
//					// controller.getRegexBuilderView().getRegexComboBoxHandler().onRegexSelect(regexPattern);
//					controller.getRegexBuilderView().getRegexComboBoxHandler()
//							.onRegexSelect(regexPattern, controller.getCurrentSelectedObject(), controller.getCurrentValueProvider());
//				}
//
//				// controller.getCurrentTblExtrRule().setRegexType(regexPattern);
//
//				// controller.getCurrentTblExtrRule().getStartRegexPatternModel().setPatternTextCellContent(regexPattern);
//				// controller.getGrid().reLoad();
//			}
//		}
//	}

	public void resultTextAreaHandler() {
		if (view.getResultTextArea().getText().equals(BatchClassConstants.EMPTY_STRING)) {

			controller.getRegexBuilderView().getRegexBuilderToolBar().toggleFindAllMatchesButton(false);
			controller.getRegexBuilderView().getRegexBuilderToolBar().toggleSelectRegexButton(false);
		} else {
			controller.getRegexBuilderView().getRegexBuilderToolBar().toggleFindAllMatchesButton(true);
			controller.getRegexBuilderView().getRegexBuilderToolBar().toggleSelectRegexButton(true);
		}
	}
	
	
//	public void findMatchedIndexesList(final String regex, final String strToBeMatched) {
//		List<String> matchedIndexList = null;
//		if (!StringUtil.isNullOrEmpty(regex) && !StringUtil.isNullOrEmpty(strToBeMatched)) {
//			if(ValidationUtil.isRegexPatternValid(regex)) {
//				matchedIndexList = new ArrayList<String>();
//				RegExp regExp = RegExp.compile(regex, "g");	    
//			    for (MatchResult matcher = regExp.exec(strToBeMatched); matcher != null; matcher = regExp.exec(strToBeMatched)) {
//			    	matchedIndexList.add(String.valueOf(matcher.getIndex()));
//			    	matchedIndexList.add(String.valueOf(regExp.getLastIndex()));
//			    }
//			    if(matchedIndexList.size() < 1) {
//			    	DialogUtil.showMessageDialog(
//						LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
//						LocaleDictionary.getMessageValue(
//								RegexBuilderConstants.NO_MATCH_FOUND_ERROR_MESSAGE), DialogIcon.INFO);
//
//					showMatchStringTextArea();
//			    } else {
//			    	doHiglight(matchedIndexList);
//			    }
//			    
//			} else {
//				DialogUtil.showMessageDialog(
//						LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITTLE),
//						LocaleDictionary.getMessageValue(
//								RegexBuilderConstants.REGEX_PATTERN_INVALID), DialogIcon.ERROR);
//				
//				showMatchStringTextArea();
//			}
//		}
//	}
	
	
	@EventHandler
	public void onSelectRegexButtonClicked(SelectRegexBuilderFormEvent selectEvent) {
		if (null != selectEvent) {
			final String regexPattern = view.getResultTextArea().getText();
//			final String strToBeMatched = view.getMatchStringTextArea().getText();
			//final String strToBeMatched = BatchClassConstants.EMPTY_STRING;
			if (StringUtil.isNullOrEmpty(regexPattern)) {

				DialogUtil.showMessageDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
								.getMessageValue(RegexBuilderConstants.REGEX_PATTERN_EMPTY_ERROR_MESSAGE), DialogIcon.ERROR);
			}else {
				ScreenMaskUtility.maskScreen();
				controller.getRpcService().findMatchedIndexesList(regexPattern, BatchClassConstants.EMPTY_STRING, new AsyncCallback<List<String>>() {

					@Override
					public void onSuccess(final List<String> matchedIndexList) {
						ScreenMaskUtility.unmaskScreen();
						controller.getRegexBuilderView().hideRegexDialogWindow();
						controller.getRegexBuilderView().getRegexBuilderToolBar().getPresenter().resetRegexBuilderForm();
						if (null != controller.getRegexBuilderView().getRegexComboBoxHandler()) {
							controller.getRegexBuilderView().getRegexComboBoxHandler()
									.onRegexSelect(regexPattern, controller.getCurrentSelectedObject(), controller.getCurrentValueProvider());
						}
					}

					@Override
					public void onFailure(final Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						DialogUtil.showMessageDialog(
								LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
										.getMessageValue(RegexBuilderConstants.ERROR_OCCURRED_VERIFYING_REGEX), DialogIcon.ERROR);
						
						//showMatchStringTextArea();

					}
				});

			}
		}
	}
}
