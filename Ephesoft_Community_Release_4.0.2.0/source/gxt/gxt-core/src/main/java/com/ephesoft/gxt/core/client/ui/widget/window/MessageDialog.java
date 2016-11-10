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
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.widget.core.client.Dialog.DialogMessages;
import com.sencha.gxt.widget.core.client.box.MessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;

/**
 * Used to show the message Dialog. This Dialog Box will be used to show the Message with an OK Button By Default, which will be used
 * to hide the Dialog Box.
 * 
 * <p>
 * Use this Dialog to show the message at UI side.
 * </p>
 * 
 * @author Ephesoft.
 * @version 1.0.
 * 
 */
public class MessageDialog extends MessageBox {

	/**
	 * Initializes the Non-Modal <code> Message Dialog Box</code> with the title <code>htmlHeading</code>.
	 * 
	 * @param htmlHeading {@link String} Heading to be set. Accepts HTML {@link String} for the heading
	 */
	public MessageDialog(final String htmlHeading) {
		this(htmlHeading, null);
	}

	/**
	 * Initializes the Non-Modal <code> Message Dialog Box</code> with the title <code>htmlHeading</code> and the
	 * <code>htmlMessage</code>.
	 * 
	 * @param htmlHeading {@link String} Heading to be set. Accepts HTML {@link String} for the heading
	 * @param htmlMessage {@link String} Message to be displayed in the MessageBox. Accepts HTML {@link String} as a Message.
	 */
	public MessageDialog(final String htmlHeading, final String htmlMessage) {
		this(htmlHeading, htmlHeading, DialogIcon.ERROR);
	}

	/**
	 * Initializes the Non-Modal <code> Message Dialog Box</code> with the title <code>htmlHeading</code> and the
	 * <code>htmlMessage</code>.
	 * 
	 * @param htmlHeading {@link String} Heading to be set. Accepts HTML {@link String} for the heading
	 * @param htmlMessage {@link String} Message to be displayed in the MessageBox. Accepts HTML {@link String} as a Message.
	 * @param icon {@link DialogIcon} Parameter which decides which type of icon should be displayed. If null INFO icon will be
	 *            displayed.
	 */
	public MessageDialog(final String htmlHeading, final String htmlMessage, final DialogIcon icon) {
		super(htmlHeading, htmlMessage);
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
		this.setIcon(icon);
		this.setClosable(true);
		this.setShadow(true);
		this.setOnEsc(true);
		this.getButton(PredefinedButton.OK).focus();
		this.addStyleName("dialog");
	}

	/**
	 * Sets the text of the OK Button. Ignores the request, if the OK Button doesn't exist, i.e. explicitly removed by the User.
	 * 
	 * @param text {@link String} text to be set. If <code>null</code> then ignores the request.
	 */
	public void setOKButtonText(final String text) {
		final TextButton okButton = this.getButton(PredefinedButton.OK);
		if (okButton != null && text != null) {
			okButton.setText(text);
		}
	}

	/**
	 * Sets an icon to the Dialog Box on the basis of the <code> icon </code> value.
	 * 
	 * @param icon {@link DialogIcon} Parameter which decides which type of icon should be displayed. If null INFO icon will be
	 *            displayed.
	 */
	public void setIcon(final DialogIcon icon) {
		final MessageBoxIcons availableIcons = MessageBox.ICONS;
		ImageResource dialogIcon = availableIcons.info();
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

	@Override
	protected void onKeyPress(final Event event) {
		final int keyCode = event.getKeyCode();
		switch (keyCode) {
			case KeyCodes.KEY_TAB:
				final TextButton okButton = getButton(PredefinedButton.OK);
				okButton.focus();
				break;

			default:
				super.onKeyPress(event);
				break;
		}
		event.stopPropagation();
		event.preventDefault();
	}
	
	@Override
	public void show() {
		super.show();
		focusOnButton(PredefinedButton.OK);
	}
	
	private void focusOnButton(PredefinedButton ok) {
		final DelayedTask task = new DelayedTask() {

			@Override
			public void onExecute() {
				final TextButton okButton = getButton(PredefinedButton.OK);
				okButton.focus();
			}
		};
		task.delay(200);
	}
}
