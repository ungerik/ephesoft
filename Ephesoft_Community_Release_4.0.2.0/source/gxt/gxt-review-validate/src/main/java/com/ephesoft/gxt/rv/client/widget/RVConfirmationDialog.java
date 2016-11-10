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

package com.ephesoft.gxt.rv.client.widget;

import com.ephesoft.gxt.core.client.ui.widget.listener.DialogListener;
import com.ephesoft.gxt.core.client.ui.widget.util.CookieUtil;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleMessage;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.FocusInitializationEvent;
import com.ephesoft.gxt.rv.shared.ArrayUtils;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;

public class RVConfirmationDialog extends ConfirmationDialog {

	private final CheckBox responseRecordingCheckbox;
	private PredefinedButton[] recordingButtons;
	private String recordingIdentfier;
	private PredefinedButton currentButton;

	public RVConfirmationDialog(final String title, final String message) {
		super(title, message);
		this.addHideHandler();
		this.setOnEsc(true);
		this.setClosable(true);
		this.setModal(true);
		responseRecordingCheckbox = new CheckBox();
		responseRecordingCheckbox.setBoxLabel(LocaleMessage.DONT_ASK_AGAIN);
		buttonBar.insert(responseRecordingCheckbox, 0);
		responseRecordingCheckbox.addStyleName("confirmationCheckBox");
		WidgetUtil.setID(responseRecordingCheckbox, "response-recording-checkbox");
		addShowHandler();
		addStopPropogationHandler(getButton(PredefinedButton.OK));
		addStopPropogationHandler(getButton(PredefinedButton.CANCEL));
		setFocusWidget(getButton(PredefinedButton.OK));
	}

	private void addStopPropogationHandler(final TextButton button) {
		if (null != button) {
			button.addDomHandler(new KeyDownHandler() {

				@Override
				public void onKeyDown(final KeyDownEvent event) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_SPACE) {
						event.stopPropagation();
					}
				}
			}, KeyDownEvent.getType());
		}
	}

	@Override
	protected void onKeyPress(final Event event) {
		final int keyCode = event.getKeyCode();
		switch (keyCode) {
			case KeyCodes.KEY_SHIFT:
				responseRecordingCheckbox.setValue(!responseRecordingCheckbox.getValue());
				event.stopPropagation();
				event.preventDefault();
				break;
				
			case KeyCodes.KEY_TAB:
				break;
			// case KeyCodes.KEY_TAB:
			// if (currentButton == PredefinedButton.OK) {
			// final TextButton cancelButton = getButton(PredefinedButton.CANCEL);
			// cancelButton.focus();
			// currentButton = PredefinedButton.CANCEL;
			// } else {
			// final TextButton okButton = getButton(PredefinedButton.OK);
			// okButton.focus();
			// currentButton = PredefinedButton.OK;
			// }
			// break;

			default:
				super.onKeyPress(event);
				event.stopPropagation();
				event.preventDefault();
				break;
		}
	}

	private void addShowHandler() {
//		this.addShowHandler(new ShowHandler() {
//
//			@Override
//			public void onShow(final ShowEvent event) {
//				final TextButton button = getButton(PredefinedButton.OK);
//				currentButton = PredefinedButton.OK;
//				button.focus();
//			}
//		});
	}

	protected void createButtons() {
		super.createButtons();
		TextButton hiddenButton = new TextButton(CoreCommonConstant.EMPTY_STRING);
		hiddenButton.setBorders(false);
		hiddenButton.setHeight(0);
		hiddenButton.setWidth(0);
		hiddenButton.getElement().setOpacity(0.001);
		addButton(hiddenButton);
		hiddenButton.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				responseRecordingCheckbox.focus();
			}
		});
	}

	private void addHideHandler() {
		this.addHideHandler(new HideHandler() {

			@Override
			public void onHide(final HideEvent event) {
				ReviewValidateEventBus.fireEvent(new FocusInitializationEvent());
			}
		});
	}

	public void setResponseRecordingButtons(final PredefinedButton... predefinedButtons) {
		if (!ArrayUtils.isEmpty(predefinedButtons)) {
			recordingButtons = new PredefinedButton[predefinedButtons.length];
			int index = 0;
			for (final PredefinedButton button : predefinedButtons) {
				recordingButtons[index++] = button;
			}
		}
	}

	@Override
	protected void onButtonPressed(final TextButton textButton) {
		super.onButtonPressed(textButton);
		if (!ArrayUtils.isEmpty(recordingButtons) && responseRecordingCheckbox.getValue()) {
			final PredefinedButton predefinedButton = this.getPredefinedButton(textButton);
			final int buttonIndex = ArrayUtils.indexOf(recordingButtons, predefinedButton);
			if (buttonIndex != -1 && !StringUtil.isNullOrEmpty(recordingIdentfier)) {
				CookieUtil.storeCookie(recordingIdentfier, predefinedButton.toString());
			}
		}
	}

	@Override
	public void setDialogListener(final DialogListener dialogListener) {
		super.setDialogListener(dialogListener);
		final String recordedValue = CookieUtil.getCookieValue(recordingIdentfier);
		if (!StringUtil.isNullOrEmpty(recordedValue)) {
			final PredefinedButton button = PredefinedButton.valueOf(recordedValue);
			super.onButtonPressed(button);
			this.hide();
		}
	}

	public void setUserRecordingIdentfier(final String recordingIdentfier) {
		this.recordingIdentfier = recordingIdentfier;
	}

	public void setRecordingCheckBox() {
		buttonBar.insert(responseRecordingCheckbox, 0);
		responseRecordingCheckbox.addStyleName("confirmationCheckBox");
		WidgetUtil.setID(responseRecordingCheckbox, "response-recording-checkbox");
	}

}
