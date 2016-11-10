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
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.regexbuilder.RegexEngine;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexQuantifierDetailView;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Provides the functionality to handle events for RegexQuantifierDetailView. This presenter handles the event for the quantifier
 * section of the Regex Builder dialog.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see BatchClassInlinePresenter
 * @see RegexQuantifierDetailView
 */
public class RegexQuantifierDetailPresenter extends BatchClassInlinePresenter<RegexQuantifierDetailView> {

	/**
	 * Constructor of the class.
	 * 
	 * @param controller {@link BatchClassManagementController} The instance of BatchClassManagementController.
	 * @param view {@link RegexQuantifierDetailView} The regex quantifier details view.
	 */
	public RegexQuantifierDetailPresenter(final BatchClassManagementController controller, final RegexQuantifierDetailView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Also based on value paased, this will make visible or invisble the
	 * text boxes.
	 * 
	 * @param isValue <code>true</code> value makes visible the txtNoOfTimes text box and invisble max and min no of times text box.
	 *            Also true value makes enable asFewAsPossibleCheckBox. <code> false </code> value makes invisble the txtNoOfTimes, min
	 *            and max no of times text boxes.
	 */
	public void onExactlyNoOfTimesRadioButtonClicked(final boolean isValue) {
		if (isValue) {
			view.setVisibleTextBox(true, false);
			view.setEnableAsFewAsPossibleCheckBox(true);
		} else {
			view.setVisibleTextBox(false, false);
		}
		controller.getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Also based on value paased, this will make visible or invisble the
	 * text boxes.
	 * 
	 * @param isValue <code>true</code> value makes invisible the txtNoOfTimes text box and makes visble max and min no of times text
	 *            boxes. Also true value makes enable asFewAsPossibleCheckBox. <code>false</code> value makes invisble the
	 *            txtNoOfTimes, min and max no of times text boxes.
	 */
	public void onBetweenMinAndMaxTimesRadioButtonClicked(final boolean isValue) {
		if (isValue) {
			view.setVisibleTextBox(false, true);
			view.setEnableAsFewAsPossibleCheckBox(true);
		} else {
			view.setVisibleTextBox(false, false);
		}
		controller.getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Also based on value paased, this will make visible or invisble the
	 * text boxes and enable the checkbox.
	 * 
	 * @param isValue <code>true</code> value makes invisible the txtNoOfTimes text box and max and min no of times text box. Also true
	 *            value makes disable asFewAsPossibleCheckBox. <code>false</code> value makes enable the asFewAsPossibleCheckBox.
	 */
	public void onExactlyOneTimesRadioButtonClicked(final boolean isValue) {
		if (isValue) {
			view.setVisibleTextBox(false, false);
			view.setEnableAsFewAsPossibleCheckBox(false);
			view.getAsFewAsPossibleCheckBox().setValue(false);
		} else {
			view.setEnableAsFewAsPossibleCheckBox(true);
		}
		controller.getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Also based on value paased, this will make invisble the text boxes and
	 * enable the checkbox.
	 * 
	 * @param isValue <code>true</code> value makes invisible the txtNoOfTimes text box and max and min no of times text box. Also true
	 *            value makes enable the asFewAsPossibleCheckBox.
	 */
	public void onBetweenZeroAndOneTimesRadioButtonClicked(final boolean isValue) {
		if (isValue) {
			view.setVisibleTextBox(false, false);
			view.setEnableAsFewAsPossibleCheckBox(true);
		}
		controller.getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Also based on value paased, this will make invisble the text boxes and
	 * enable the checkbox.
	 * 
	 * @param isValue <code>true</code> value makes invisible the txtNoOfTimes text box and max and min no of times text box. Also true
	 *            value makes enable the asFewAsPossibleCheckBox.
	 */
	public void onAnyNoOfTimesRadioButtonClicked(final boolean isValue) {
		if (isValue) {
			view.setVisibleTextBox(false, false);
			view.setEnableAsFewAsPossibleCheckBox(true);
		}
		controller.getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Also based on value paased, this will make invisble the text boxes and
	 * enable the checkbox.
	 * 
	 * @param isValue <code>true</code> value makes invisible the txtNoOfTimes text box and max and min no of times text box. Also true
	 *            value makes enable the asFewAsPossibleCheckBox.
	 */
	public void onOneOrMoreTimesRadioButtonClicked(final boolean isValue) {
		if (isValue) {
			view.setVisibleTextBox(false, false);
			view.setEnableAsFewAsPossibleCheckBox(true);
		}
		controller.getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Event will be canceled in case if key other than digit , tab or
	 * backspace is being pressed as we are allowing only digits to be entered in the textbox.
	 * 
	 * @param keyPressEvent {@link KeyPressEvent} The instance of KeyPressEvent.
	 */
	public void handleKeyPressEvent(final KeyPressEvent keyPressEvent) {
		if (!Character.isDigit(keyPressEvent.getCharCode()) && KeyCodes.KEY_BACKSPACE != keyPressEvent.getNativeEvent().getKeyCode()
				&& KeyCodes.KEY_TAB != keyPressEvent.getNativeEvent().getKeyCode()) {
			((TextBox) keyPressEvent.getSource()).cancelKey();
		} else {
			controller.getEventBus().fireEvent(new RegexEvent());
		}
	}

	/**
	 * Fires the RegexEvent if the digit entered into txtMaxNoOfTimes textbox is greater than txtMinNoOfTimes textbox.
	 * Otherwise,invalid range label will be shown and txtMaxNoOfTimes field will be shown red.
	 */
	public void handleKeyUpEventForTxtMaxNoOfTimes() {
		if (RegexEngine.isDigitGreaterThan(view.getTxtMaxNoOfTimes().getText(), view.getTxtMinNoOfTimes().getText())) {
			controller.getEventBus().fireEvent(new RegexEvent());
			view.getTxtMaxNoOfTimes().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getTxtMinNoOfTimes().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getInvalidRangeLabel().setVisible(false);
		} else {
			view.getInvalidRangeLabel().setVisible(true);
			view.getTxtMaxNoOfTimes().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		}
	}

	/**
	 * Fires the RegexEvent if the digit entered into txtMaxNoOfTimes textbox is greater than txtMinNoOfTimes textbox.
	 * Otherwise,invalid range label will be shown and txtMinNoOfTimes field will be shown red.
	 */
	public void handleKeyUpEventForTxtMinNoOfTimes() {
		if (RegexEngine.isDigitGreaterThan(view.getTxtMaxNoOfTimes().getText(), view.getTxtMinNoOfTimes().getText())) {
			controller.getEventBus().fireEvent(new RegexEvent());
			view.getTxtMaxNoOfTimes().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getTxtMinNoOfTimes().removeStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
			view.getInvalidRangeLabel().setVisible(false);
		} else {
			view.getInvalidRangeLabel().setVisible(true);
			view.getTxtMinNoOfTimes().addStyleName(CoreCommonConstants.DATE_BOX_FORMAT_ERROR_CSS);
		}
	}

	@Override
	public void injectEvents(EventBus eventBus) {

	}
}
