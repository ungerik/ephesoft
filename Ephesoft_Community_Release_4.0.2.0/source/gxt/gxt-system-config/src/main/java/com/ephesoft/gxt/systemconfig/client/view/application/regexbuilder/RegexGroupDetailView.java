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

import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.event.regexbuilder.RegexEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.presenter.application.regexbuilder.RegexGroupDetailPresenter;
import com.ephesoft.gxt.systemconfig.client.regexbuilder.RegexBuilderConstants;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.ContentPanel;

/**
 * Shows the view of Regex Group section of the Regex Builder dialog.This view provides regex group features options for generating
 * regex pattern.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014<br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see View
 * @see RegexGroupDetailPresenter
 */
public class RegexGroupDetailView extends SystemConfigInlineView<RegexGroupDetailPresenter> {

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
	interface Binder extends UiBinder<VerticalPanel, RegexGroupDetailView> {
	}

	/**
	 * BINDER {@link Binder}.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	// /**
	// * regexGroupHeading {@link Label} is used to show the heading for the panel.
	// */
	@UiField
	protected ContentPanel regexGroupContainer;

	/**
	 * caseInsensetiveCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox caseInsensetiveCheckBox;

	/**
	 * caseInsensetiveLabel {@link Label} is used for the label text to be shown.
	 */
	@UiField
	protected Label caseInsensetiveLabel;

	/**
	 * startGroupCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox startGroupCheckBox;

	/**
	 * startGroupLabel {@link Label} is used for the label text to be shown.
	 */
	@UiField
	protected Label startGroupLabel;

	/**
	 * endGroupCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox endGroupCheckBox;

	/**
	 * endGroupLabel {@link Label} is used for the label text to be shown.
	 */
	@UiField
	protected Label endGroupLabel;

	/**
	 * asFewAsPossibleCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox captureGroupCheckBox;

	/**
	 * captureGroupLabel {@link Label} is used for the label text to be shown.
	 */
	@UiField
	protected Label captureGroupLabel;

	/**
	 * nonCaptureGroupCheckBox {@link CheckBox} is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox nonCaptureGroupCheckBox;

	/**
	 * nonCaptureGroupLabel {@link Label} is used for the label text to be shown.
	 */
	@UiField
	protected Label nonCaptureGroupLabel;

	/**
	 * horizontalLinePanel {@link HorizontalPanel} is used for the horizontal line to be shown.
	 */
	@UiField
	protected HorizontalPanel horizontalLinePanel;

	/**
	 * applyQuantifierToGroupLabel {@link Label} is used for the label text to be shown.
	 */
	@UiField
	protected Label applyQuantifierToGroupLabel;

	/**
	 * applyQuantifierToGroupCheckBox {@link CheckBox}is used for the check box to be shown.
	 */
	@UiField
	protected CheckBox applyQuantifierToGroupCheckBox;

	/**
	 * Instantiates the RegexGroupDetailView.
	 */
	public RegexGroupDetailView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		regexGroupContainer.setHeadingText(StringUtil.concatenate(SystemConfigConstants.SPACE,
				LocaleDictionary.getConstantValue(RegexBuilderConstants.REGEX_BUILDER_GROUP)));
		regexGroupContainer.addStyleName("panelHeader");
		// regexGroupHeading.setText(LocaleDictionary.getLocaleDictionary().getConstantValue(RegexBuilderConstants.REGEX_BUILDER_GROUP));
		caseInsensetiveLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.CASE_INSENSETIVE));
		startGroupLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.START_GROUP));
		endGroupLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.END_GROUP));
		captureGroupLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.CAPTURE_GROUP));
		nonCaptureGroupLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.NON_CAPTURE_GROUP));
		applyQuantifierToGroupLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.APPLY_QUANTIFIER_TO_ENTIRE_REGEX));

		// Adding CSS to the component of View.
		addCSSStyles();

		// Add help messages to the components of the view.
		addHelpMessages();

	}

	/**
	 * Add CSS to the component of the view.
	 */
	private void addCSSStyles() {

		// Add CSS for Label.
		caseInsensetiveLabel.addStyleName(SystemConfigConstants.REGEX_BUILDER_LABEL_CSS);
		startGroupLabel.addStyleName(SystemConfigConstants.REGEX_BUILDER_LABEL_CSS);
		endGroupLabel.addStyleName(SystemConfigConstants.REGEX_BUILDER_LABEL_CSS);
		captureGroupLabel.addStyleName(SystemConfigConstants.REGEX_BUILDER_LABEL_CSS);
		nonCaptureGroupLabel.addStyleName(SystemConfigConstants.REGEX_BUILDER_LABEL_CSS);
		applyQuantifierToGroupLabel.addStyleName(SystemConfigConstants.REGEX_BUILDER_LABEL_CSS);

		caseInsensetiveLabel.addStyleName(SystemConfigConstants.LABEL_TEXT_CSS);
		startGroupLabel.addStyleName(SystemConfigConstants.LABEL_TEXT_CSS);
		endGroupLabel.addStyleName(SystemConfigConstants.LABEL_TEXT_CSS);
		captureGroupLabel.addStyleName(SystemConfigConstants.LABEL_TEXT_CSS);
		nonCaptureGroupLabel.addStyleName(SystemConfigConstants.LABEL_TEXT_CSS);
		applyQuantifierToGroupLabel.addStyleName(SystemConfigConstants.LABEL_TEXT_CSS);

		// Add CSS for Horizontal Line Panel.
		horizontalLinePanel.addStyleName(CoreCommonConstants.HORIZONTAL_LINE_CSS);
	}

	/**
	 * Fires the RegexEvent when checkboxes are being checked or unchecked.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of clickEvent.
	 */
	@UiHandler(value = {"caseInsensetiveCheckBox", "startGroupCheckBox", "endGroupCheckBox", "applyQuantifierToGroupCheckBox"})
	public void handleClickEvent(final ClickEvent clickEvent) {
		presenter.getController().getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Fires the RegexEvent when captureGroupCheckBox is being checked or unchecked.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of clickEvent.
	 */
	@UiHandler("captureGroupCheckBox")
	public void onCaptureGroupCheckBoxChecked(final ClickEvent clickEvent) {
		presenter.onCaptureGroupCheckBoxChecked(clickEvent);
	}

	/**
	 * Fires the RegexEvent when nonCaptureGroupCheckBox is being checked or unchecked.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of clickEvent.
	 */
	@UiHandler("nonCaptureGroupCheckBox")
	public void onNonCaptureGroupCheckBoxChecked(final ClickEvent clickEvent) {
		presenter.onNonCaptureGroupCheckBoxChecked(clickEvent);
	}

	/**
	 * Add help messages to the component of the view.
	 */
	private void addHelpMessages() {

		// Adding help messages to the components of the view.
		caseInsensetiveCheckBox.setTitle(LocaleDictionary.getConstantValue(RegexBuilderConstants.CASE_INSENSETIVE_HELP_MESSAGE));
		startGroupCheckBox.setTitle(LocaleDictionary.getConstantValue(RegexBuilderConstants.START_GROUP_HELP_MESSAGE));
		endGroupCheckBox.setTitle(LocaleDictionary.getConstantValue(RegexBuilderConstants.END_GROUP_HELP_MESSAGE));
		captureGroupCheckBox.setTitle(LocaleDictionary.getConstantValue(RegexBuilderConstants.CAPTURE_GROUP_HELP_MESSAGE));
		nonCaptureGroupCheckBox.setTitle(LocaleDictionary.getConstantValue(RegexBuilderConstants.NON_CAPTURE_GROUP_HELP_MESSAGE));
		applyQuantifierToGroupCheckBox.setTitle(LocaleDictionary
				.getConstantValue(RegexBuilderConstants.APPLY_QUANTIFIER_TO_ENTIRE_REGEX_HELP_MESSAGE));

	}

	/**
	 * 
	 * @return {@link CheckBox} The instance of caseInsensetiveCheckBox.
	 */
	public CheckBox getCaseInsensetiveCheckBox() {
		return caseInsensetiveCheckBox;
	}

	/**
	 * Getter for the instance of startGroupCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of startGroupCheckBox.
	 */
	public CheckBox getStartGroupCheckBox() {
		return startGroupCheckBox;
	}

	/**
	 * Getter for the instance of endGroupCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of endGroupCheckBox.
	 */
	public CheckBox getEndGroupCheckBox() {
		return endGroupCheckBox;
	}

	/**
	 * Getter for the instance of captureGroupCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of captureGroupCheckBox.
	 */
	public CheckBox getCaptureGroupCheckBox() {
		return captureGroupCheckBox;
	}

	/**
	 * Getter for the instance of nonCaptureGroupCheckBox.
	 * 
	 * @return {@link CheckBox} The instance of nonCaptureGroupCheckBox.
	 */
	public CheckBox getNonCaptureGroupCheckBox() {
		return nonCaptureGroupCheckBox;
	}

	/**
	 * @return the applyQuantifierToGroupCheckBox
	 */
	public CheckBox getApplyQuantifierToGroupCheckBox() {
		return applyQuantifierToGroupCheckBox;
	}
}
