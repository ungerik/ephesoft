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

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.presenter.application.regexbuilder.RegexBuilderMainPresenter;
import com.ephesoft.gxt.systemconfig.client.regexbuilder.RegexBuilderConstants;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * Shows the view of Regex builder. This view shows the all the options for generating the regex pattern. This view contains the Regex
 * Tester utility. User can also test the generated regex pattern with the help of this utility.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see View
 * @see RegexBuilderMainPresenter
 * @see RegexBuilderDetailView
 * 
 */
public class RegexBuilderMainView extends SystemConfigInlineView<RegexBuilderMainPresenter> {

	// /**
	// * regexScrollPanel {@link ScrollPanel} The instance of scroll panel.
	// */
	// @UiField
	// protected ScrollPanel regexScrollPanel;

	/**
	 * tabPanel {@link TabPanel} The instance of tabPanel.
	 */
	@UiField
	protected TabPanel tabPanel;

	// /**
	// * regexResultButtonPanel {@link HorizontalPanel} The instance of regexResultButtonPanel.
	// */
	// @UiField
	// protected HorizontalPanel regexResultButtonPanel;

	/**
	 * testPanel {@link HorizontalPanel} The instance of testPanel.
	 */
	@UiField
	protected HorizontalPanel testPanel;

	/**
	 * resultLabel {@link Label} The instance of resultLabel.
	 */
	@UiField
	protected Label resultLabel;

	/**
	 * resultTextArea{@link TextArea} The instance of resultTextArea.
	 */
	@UiField
	protected TextArea resultTextArea;

	/**
	 * strToBeMatchedLabel {@link Label} The instance of strToBeMatchedLabel.
	 */
	@UiField
	protected Label strToBeMatchedLabel;

	/**
	 * strToBeMatchedTextArea{@link TextArea} The instance of strToBeMatchedTextArea.
	 */
	@UiField
	protected TextArea strToBeMatchedTextArea;

	/**
	 * findAllMatchesButton {@link Button} The instance of findAllMatchesButton.
	 */
	// @UiField
	// protected CustomMenuBar findAllMatchesButtonMenuBar;

	// protected CustomMenuItem findAllMatchesButton;

	/**
	 * selectRegexButton {@link Button} The instance of selectRegexButton.
	 */
	// @UiField
	// protected CustomMenuBar selectRegexButtonMenuBar;
	// protected CustomMenuItem selectRegexButton;

	/**
	 * textBoxToWhichRegexNeedToBeCopied {@link TextBox} Instance of Text Box on which generated regex need to be copied.
	 */
	private TextField textBoxToWhichRegexNeedToBeCopied = new TextField();

	/**
	 * matchedHTML {@link HTML} The instance of matchedHTML.
	 */
	@UiField
	protected HTML matchedHTML;

	/**
	 * dialogWindow {@link DialogWindow} The instance of dialogWindow.
	 */
	private DialogWindow dialogWindow = new DialogWindow(false);

	/**
	 * regexBuilderDetailView {@link RegexBuilderDetailView} The instance of regexBuilderDetailView.
	 */
	private final RegexBuilderDetailView regexBuilderDetailView;

	/**
	 * listBuilderDetailViews {@link List <{@link RegexBuilderDetailView} The list of RegexBuilderDetailView.
	 */
	private final List<RegexBuilderDetailView> listBuilderDetailViews;

	@UiField
	protected VerticalPanel testPanelVerticalPanel;

	@UiField
	protected VerticalLayoutContainer tabPanelBottomScroll;

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
	interface Binder extends UiBinder<Widget, RegexBuilderMainView> {
	}

	/**
	 * BINDER {@link Binder}.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Instantiates the RegexBuilderMainView.
	 */
	public RegexBuilderMainView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		regexBuilderDetailView = new RegexBuilderDetailView();

		// regexBuilderMainContainer.addStyleName("regex_builder_main_view_vertical_panel");
		tabPanelBottomScroll.addStyleName("tabPanelBottomScroll");
		tabPanel.add(regexBuilderDetailView, LocaleDictionary.getConstantValue(RegexBuilderConstants.REGEX_STR));
		tabPanel.selectTab(0);

		regexBuilderDetailView.getRegexFieldDetailView().getFollowedByComboBox().setVisible(false);
		regexBuilderDetailView.getRegexQuantifierDetailView().setGroupName(RegexBuilderConstants.REGEX_QUANTIFIER);
		regexBuilderDetailView.getRegexFieldDetailView().setGroupName(RegexBuilderConstants.REGEX_STR);

		strToBeMatchedLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.STR_TO_BE_MATCHED));
		resultLabel.setText(LocaleDictionary.getConstantValue(RegexBuilderConstants.FIND_ALL_MATCHES));

		listBuilderDetailViews = new ArrayList<RegexBuilderDetailView>();
		listBuilderDetailViews.add(regexBuilderDetailView);

		// Make HTML panel invisible by deafult.
		matchedHTML.setVisible(false);

		testPanelVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		resultTextArea.setWidth("220px");
		strToBeMatchedTextArea.setWidth("220px");
		// Add CSS to the component of the view.
		addCSSStyles();
		this.resultTextAreaHandler();

	}

	/**
	 * Add CSS to the components of the view.
	 */
	private void addCSSStyles() {

		// Adding CSS for tab panel and scroll panel.
		tabPanel.addStyleName(CoreCommonConstants.TAB_PANEL_REGEX_BUILDER_CSS);
		// regexScrollPanel.addStyleName(CoreCommonConstants.WIDTH_610_PX);
		testPanel.addStyleName("testPanelRegexBuider");
		// Adding CSS for Label.
		resultLabel.addStyleName(SystemConfigConstants.REGEX_BUILDER_LABEL_CSS);
		resultLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		resultLabel.addStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		strToBeMatchedLabel.addStyleName(SystemConfigConstants.REGEX_BUILDER_LABEL_CSS);
		strToBeMatchedLabel.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		strToBeMatchedLabel.addStyleName(CoreCommonConstants.BOLD_TEXT_CSS);

	}

	/**
	 * Converts the matched Text Area highlighted HTML panel to Text Area.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	@UiHandler(value = "matchedHTML")
	public void onMatchedAreaClicked(final ClickEvent clickEvent) {
		presenter.onMatchedAreaClicked();
	}

	/**
	 * @return the resultTextArea {@link TextArea}.
	 */
	public TextArea getResultTextArea() {
		return resultTextArea;
	}

	/**
	 * @return the strToBeMatchedTextArea {@link TextArea}.
	 */
	public TextArea getMatchStringTextArea() {
		return strToBeMatchedTextArea;
	}

	/**
	 * @return the testPanel {@link HorizontalPanel}.
	 */
	public HorizontalPanel getTestPanel() {
		return testPanel;
	}

	/**
	 * @return the matchedHTML {@link HTML}.
	 */
	public HTML getMatchedHTML() {
		return matchedHTML;
	}

	/**
	 * @return the regexBuilderDetailView {@link RegexBuilderDetailView}.
	 */
	public RegexBuilderDetailView getRegexBuilderDetailView() {
		return regexBuilderDetailView;
	}

	/**
	 * @param dialogBox {@link DialogBox} the dialogBox to set.
	 */
	public void setDialogWindow(final DialogWindow dialogWindow) {
		this.dialogWindow = dialogWindow;
	}

	/**
	 * @return the dialogBox.
	 */
	public DialogWindow getDialogWindow() {
		return dialogWindow;
	}

	/**
	 * @return the listBuilderDetailViews.
	 */
	public List<RegexBuilderDetailView> getListBuilderDetailViews() {
		return listBuilderDetailViews;
	}

	/**
	 * @return the tabPanel
	 */
	public TabPanel getTabPanel() {
		return tabPanel;
	}

	/**
	 * @return the textFieldToWhichRegexNeedToBeCopied
	 */

	public TextField getTextBoxToWhichRegexNeedToBeCopied() {
		return textBoxToWhichRegexNeedToBeCopied;
	}

	/**
	 * @param textFieldToWhichRegexNeedToBeCopied the textFieldToWhichRegexNeedToBeCopied to set
	 */
	public void setTextBoxToWhichRegexNeedToBeCopied(final TextField textBoxToWhichRegexNeedToBeCopied) {
		this.textBoxToWhichRegexNeedToBeCopied = textBoxToWhichRegexNeedToBeCopied;
	}

	public void resultTextAreaHandler() {
		resultTextArea.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				presenter.resultTextAreaHandler();
			}
		});
	}
}
