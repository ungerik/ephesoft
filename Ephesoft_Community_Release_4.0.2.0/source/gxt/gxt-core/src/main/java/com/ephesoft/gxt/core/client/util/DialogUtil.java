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

package com.ephesoft.gxt.core.client.util;

import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.ui.widget.window.MessageDialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;

/**
 * Utility to get {@link MessageDialog} or {@link ConfirmationDialog} according to requirements with different details for the dialog
 * box.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see MessageDialog
 * @see ConfirmationDialog
 * 
 */
public class DialogUtil {

	private static ConfirmationDialog confirmationDialog;

	private static MessageDialog messageDialog;

	/**
	 * Initializes the Non-Modal <code> Message Dialog Box</code> with the title <code>title</code> and the <code>message</code>
	 * 
	 * @param title {@link String} Heading to be set. Accepts HTML {@link String} for the heading.
	 * @param message {@link String} Message to be displayed in the MessageBox. Accepts HTML {@link String} as a Message.
	 * @return {@link MessageDialog} a message dialog box.
	 */
	public static MessageDialog showMessageDialog(final String title, final String message) {
		return showMessageDialog(title, message, DialogIcon.INFO);
	}

	/**
	 * Initializes the Non-Modal <code> Message Dialog Box</code> with the title <code>title</code> and the <code>message</code>
	 * 
	 * @param title {@link String} Heading to be set. Accepts HTML {@link String} for the heading.
	 * @param message {@link String} Message to be displayed in the MessageBox. Accepts HTML {@link String} as a Message.
	 * @param icon {@link DialogIcon} Parameter which decides which type of icon should be displayed. If null INFO icon will be
	 *            displayed.
	 * @return {@link MessageDialog} a message dialog box.
	 */
	public static MessageDialog showMessageDialog(final String title, final String message, final DialogIcon icon) {

		messageDialog = new MessageDialog(title, message, icon);
		messageDialog.setBlinkModal(false);

		messageDialog.setHeadingText(title);
		messageDialog.setMessage(message);
		messageDialog.setIcon(icon);

		messageDialog.show();
		messageDialog.center();
		messageDialog.getButton(PredefinedButton.OK).focus();
		return messageDialog;
	}

	/**
	 * Creates a Non-Modal confirmation dialog with the given title and the message. By default no cancel button will be displayed and
	 * default icon will be shown for confirmation dialog.
	 * 
	 * @param title {@link String} title to be set to the Dialog
	 * @param message {@link String} message to be set to the confirmation Dialog.
	 * @return {@link ConfirmationDialog} a confirmation dialog box.
	 */
	public static ConfirmationDialog showConfirmationDialog(final String title, final String message) {
		return showConfirmationDialog(title, message, false);
	}

	/**
	 * Creates a Non-Modal confirmation dialog with the given title and the message. By default question mark icon will be displayed.
	 * 
	 * @param title {@link String} title to be set to the Dialog
	 * @param message {@link String} message to be set to the confirmation Dialog.
	 * @param showDiscardButton if true then shows the discard button, else donot show the button
	 * @return {@link ConfirmationDialog} a confirmation dialog box.
	 */
	public static ConfirmationDialog showConfirmationDialog(final String title, final String message, boolean showDiscardButton) {
		return showConfirmationDialog(title, message, showDiscardButton, DialogIcon.QUESTION_MARK);
	}

	/**
	 * Creates a Non-Modal confirmation dialog with the given title and the message.
	 * 
	 * @param title {@link String} title to be set to the Dialog
	 * @param message {@link String} message to be set to the confirmation Dialog.
	 * @param showDiscardButton if true then shows the discard button, else donot show the button
	 * @param icon {@link DialogIcon} Parameter which decides which type of icon should be displayed. If null QUESTION icon will be
	 *            displayed.
	 * @return {@link ConfirmationDialog} a confirmation dialog box.
	 */
	public static ConfirmationDialog showConfirmationDialog(final String title, final String message, boolean showDiscardButton,
			final DialogIcon icon) {

		confirmationDialog = new ConfirmationDialog(title, message, showDiscardButton, icon);
		confirmationDialog.setBlinkModal(false);
		confirmationDialog.setHeadingText(title);
		confirmationDialog.setMessage(message);
		confirmationDialog.setIcon(icon);
		if (showDiscardButton) {
			confirmationDialog.setCloseButtonText(PredefinedButton.CLOSE.name());
		}
		confirmationDialog.show();
		confirmationDialog.center();
		confirmationDialog.getButton(PredefinedButton.OK).focus();
		return confirmationDialog;
	}

	public static ConfirmationDialog showConfirmationDialog(final String title, final String message, final DialogIcon icon) {

		confirmationDialog = new ConfirmationDialog(title, message, icon);
		confirmationDialog.setBlinkModal(false);
		confirmationDialog.setHeadingText(title);
		confirmationDialog.setMessage(message);
		confirmationDialog.setIcon(icon);
		confirmationDialog.show();
		confirmationDialog.center();
		confirmationDialog.getButton(PredefinedButton.OK).focus();
		return confirmationDialog;
	}

	private DialogUtil() {
		// private constructor added to make the class non-instantiable.
	}

}
