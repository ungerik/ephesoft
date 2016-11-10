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

package com.ephesoft.gxt.core.client.ui.widget.window;

import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogListener;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * Used when a confirmation is required from the user.
 * 
 * <p>
 * Two types of Confirmation Dialog Buttons are being supported.
 * </p>
 * 
 * <ul>
 * <li>Two Button Dialog box-having OK Button and Cancel/Discard Button.</li>
 * <li>Three Button Dialog box-having OK Button , Cancel/Discard Button , and Close Button.</li>
 * </ul>
 * </p>
 * 
 * @author Ephesoft
 * @version 1.0.
 * 
 */
public class ConfirmationDialog extends ConfirmMessageBox implements EventHandler {

	private int currentFocusIndex = 0;

	/**
	 * Listener that is associated to listen the events of operations performed by the Dialog Box which includes the button click etc..
	 */
	private DialogListener dialogListener;

	/**
	 * Creates a Non-Modal confirmation dialog with the given title and the message.
	 * 
	 * @param title {@link String} title to be set to the Dialog
	 * @param message {@link String} message to be set to the confirmation Dialog.
	 */
	public ConfirmationDialog(final String title, final String message) {
		this(title, message, false);
	}

	/**
	 * Creates a Non-Modal confirmation dialog with the given title and the message.
	 * 
	 * @param title {@link String} title to be set to the Dialog
	 * @param message {@link String} message to be set to the confirmation Dialog.
	 * @param showDiscardButton if true then shows the discard button, else donot show the button
	 */
	public ConfirmationDialog(final String title, final String message, final boolean showDiscardButton) {
		this(title, message, showDiscardButton, DialogIcon.QUESTION_MARK);
	}

	/**
	 * Creates a Non-Modal confirmation dialog with the given title and the message.
	 * 
	 * @param title {@link String} title to be set to the Dialog
	 * @param message {@link String} message to be set to the confirmation Dialog.
	 * @param showDiscardButton if true then shows the discard button, else donot show the button
	 * @param icon {@link DialogIcon} Parameter which decides which type of icon should be displayed. If null QUESTION icon will be
	 *            displayed.
	 */
	public ConfirmationDialog(final String title, final String message, final boolean showDiscardButton, final DialogIcon icon) {
		super(title, message);
		this.setDialogMessages(new DialogMessages() {
			
			@Override
			public String yes() {
				return LocaleDictionary.getConstantValue(CoreCommonConstants.YES);
			}
			
			@Override
			public String ok() {
				return LocaleDictionary.getConstantValue(CoreCommonConstants.OK);
			}
			
			@Override
			public String no() {
				return LocaleDictionary.getConstantValue(CoreCommonConstants.NO);
			}
			
			@Override
			public String close() {
				return LocaleDictionary.getConstantValue(CoreCommonConstants.CLOSE);
			}
			
			@Override
			public String cancel() {
				return LocaleDictionary.getConstantValue(CoreCommonConstants.CANCEL);
			}
		});
		this.setModal(true);
		this.setOnEsc(true);
		this.setClosable(true);
		// Discard Button Here is the Close Button.
		if (showDiscardButton) {
			this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL, PredefinedButton.CLOSE);
		} else {
			this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		}
		this.setIcon(icon);
		this.addStyleName("dialog");
	}

	/**
	 * Called after a button in the button bar is selected. If {@link #setHideOnButtonClick(boolean)} is true, hides the dialog when
	 * any button is pressed.
	 * 
	 * @param textButton {@link TextButton} the button which was clicked when the event was fired.
	 * 
	 */
	@Override
	protected void onButtonPressed(final TextButton textButton) {
		super.onButtonPressed(textButton);
		final PredefinedButton predefinedButton = this.getPredefinedButton(textButton);
		onButtonPressed(predefinedButton);
	}

	protected void onButtonPressed(final PredefinedButton predefinedButton) {
		if (null != predefinedButton && dialogListener != null) {
			switch (predefinedButton) {
				case OK:
					dialogListener.onOkClick();
					break;
				case CANCEL:
					dialogListener.onCancelClick();
					break;
				case CLOSE:
					dialogListener.onCloseClick();
					break;
				case YES:
					dialogListener.onYesClick();
					break;
				default:
					break;
			}
		}
	}

	@Override
	public void setPredefinedButtons(PredefinedButton... buttons) {
		super.setPredefinedButtons(buttons);
	}

	@Override
	public void show() {
		super.show();
		focusButtonAtIndex(0);
	}

	private void focusButtonAtIndex(final int index) {
		DelayedTask task = new DelayedTask() {

			@Override
			public void onExecute() {
				Widget widget = buttonBar.getWidget((index));
				int totalWidget = buttonBar.getWidgetCount();
				if (widget instanceof TextButton && totalWidget > index) {
					((TextButton) widget).focus();
				}
				currentFocusIndex = index;
			}
		};
		task.delay(200);
	}

	@Override
	protected void onKeyPress(Event event) {
		super.onKeyPress(event);
		if (event.getKeyCode() == KeyCodes.KEY_TAB) {
			event.stopPropagation();
			event.preventDefault();
			int totalWidget = buttonBar.getWidgetCount();
			if (totalWidget > 0) {
				focusButtonAtIndex((currentFocusIndex + 1) % totalWidget);
			}
		}
	}

	/**
	 * Assigns a dialog Listener to the
	 * 
	 * @param dialogListener {@link DialogListener} listener which is to assigned to the dialog Box.
	 */
	public void setDialogListener(final DialogListener dialogListener) {
		this.dialogListener = dialogListener;
	}

	/**
	 * Sets the text of the OK Button. Ignores the request, if the OK Button doesn't exist, i.e. explicitly removed by the User.
	 * 
	 * @param text {@link String} text to be set. If <code>null</code> then ignores the request.
	 */
	public void setOkButtonText(final String text) {
		final TextButton button = this.getButton(PredefinedButton.OK);
		setText(button, text);
	}

	/**
	 * Sets the text of the CANCEL Button. Ignores the request, if the CANCEL Button doesn't exist, i.e. explicitly removed by the
	 * User.
	 * 
	 * @param text {@link String} text to be set. If <code>null</code> then ignores the request.
	 */
	public void setCancelButtonText(final String text) {
		final TextButton button = this.getButton(PredefinedButton.CANCEL);
		setText(button, text);
	}

	/**
	 * Sets the text of the CLOSE Button. Ignores the request, if the CLOSE Button doesn't exist, i.e. explicitly removed by the User.
	 * 
	 * @param text {@link String} text to be set. If <code>null</code> then ignores the request.
	 */
	public void setCloseButtonText(final String text) {
		final TextButton button = this.getButton(PredefinedButton.CLOSE);
		setText(button, text);
	}

	/**
	 * Sets the text of the YES Button. Ignores the request, if the YES Button doesn't exist, i.e. explicitly removed by the User.
	 * 
	 * @param text {@link String} text to be set. If <code>null</code> then ignores the request.
	 */
	public void setYesButtonText(final String text) {
		final TextButton button = this.getButton(PredefinedButton.YES);
		setText(button, text);
	}

	/**
	 * Sets the text of the TextButton. Ignores the request, if the Button is null.
	 * 
	 * @param text {@link String} text to be set. If <code>null</code> then ignores the request.
	 */
	public void setText(final TextButton button, final String text) {
		// Ignores the empty request as a user can set the text to be empty.
		if (button != null && text != null) {
			button.setText(text);
		}
	}

	/**
	 * Sets an icon to the Dialog Box on the basis of the <code> icon </code> value.
	 * 
	 * @param icon {@link DialogIcon} Parameter which decides which type of icon should be displayed. If null QUESTION icon will be
	 *            displayed.
	 */
	public void setIcon(final DialogIcon icon) {
		final MessageBoxIcons availableIcons = MessageBox.ICONS;
		ImageResource dialogIcon = availableIcons.question();
		if (null != icon) {
			switch (icon) {
				case ERROR:
					dialogIcon = availableIcons.error();
					break;
				case INFO:
					dialogIcon = availableIcons.info();
					break;
				case WARNING:
					dialogIcon = availableIcons.warning();
					break;
				case QUESTION_MARK:
					dialogIcon = availableIcons.question();
					break;
			}
		}
		super.setIcon(dialogIcon);
	}

	public ConfirmationDialog(final String title, final String message, final DialogIcon icon) {
		super(title, message);
		this.setModal(true);
		this.setCollapsible(false);
		this.setOnEsc(false);
		this.setClosable(false);
		// Discard Button Here is the Close Button.
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		this.setIcon(icon);
		this.addStyleName("dialog");
	}
}
