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

package com.ephesoft.dcma.gwt.core.shared;

import com.ephesoft.dcma.gwt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.util.HTMLDomUtil;
import com.ephesoft.dcma.gwt.core.shared.listener.ThirdButtonListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ConfirmationDialog extends DialogBox {

	interface Binder extends UiBinder<Widget, ConfirmationDialog> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	public interface DialogListener {

		void onOkClick();

		void onCancelClick();
	}

	/**
	 * OK Button to be viewed on the confirmation dialog.
	 */
	public Button okButton = new Button();

	/**
	 * Cancel Button to be viewed on Confirmation Dialog
	 */
	public Button cancelButton = new Button();

	/**
	 * Third Button to be viewed on Confirmation dialog
	 */
	public Button thirdButton = new Button();

	@UiField
	protected HTML centerWidget;

	/**
	 * Panel in which the three buttons of the confirmation dialog will be added
	 */
	@UiField
	HorizontalPanel buttonPanel;

	@UiField
	protected HTMLPanel panel;

	@UiField
	public CheckBox docTypeFieldChkBox;

	@UiField
	public CheckBox tableFieldChkBox;

	@UiField
	protected VerticalPanel verticalPanel;

	private DialogListener listener;

	private ClickHandler clickHandler;

	private boolean performCancelOnEscape;

	/**
	 * Reference to the focused element before the confirmation dialog appeared.
	 */
	private static Element focusedElement;

	public boolean isPerformCancelOnEscape() {
		return performCancelOnEscape;
	}

	public void setPerformCancelOnEscape(boolean performCancelOnEscape) {
		this.performCancelOnEscape = performCancelOnEscape;
	}

	/**
	 * Confirmation Dialog constructor with option to create a dialog with third button visibility option.
	 * 
	 * @param thirdButtonVisibility
	 */
	public ConfirmationDialog(final boolean thirdButtonVisibility) {
		super(false, false);
		setConfirmationDialogView(thirdButtonVisibility);
		addButtons(false);
	}

	/**
	 * Sets the view of Confirmation Dialog. The view can contain two or three buttons decided by the flag thirdButtonVisibility
	 * 
	 * @param thirdButtonVisibility boolean flag that decides whether or not third button should be visible or not.
	 */
	public void setConfirmationDialogView(final boolean thirdButtonVisibility) {
		setWidget(BINDER.createAndBindUi(this));
		focusedElement = HTMLDomUtil.getFocusedElement();
		okButton.setText(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_confirmation_ok));
		cancelButton.setText(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_confirmation_cancel));
		thirdButton.setText(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_confirmation_discard));
		okButton.setVisible(true);
		cancelButton.setVisible(true);
		thirdButton.setVisible(thirdButtonVisibility);
		setAnimationEnabled(true);
		setGlassEnabled(true);
		setWidth(CoreCommonConstants._100_PERCENTAGE);
		setDefaultListener(thirdButtonVisibility);
		assignClickHandlers();
	}

	/**
	 * Adds the buttons to the button panel decided by the flag toggleSwitch
	 * 
	 * @param toggleSwitch boolean flag which decides whether or not toggle the cancel and third button
	 */
	private void addButtons(final boolean toggleSwitch) {
		buttonPanel.addStyleName(CoreCommonConstants.CSS_ALIGN_RIGHT);
		buttonPanel.add(okButton);
		if (toggleSwitch) {
			buttonPanel.add(thirdButton);
			buttonPanel.add(cancelButton);
		} else {
			buttonPanel.add(cancelButton);
			buttonPanel.add(thirdButton);
		}
	}

	/**
	 * Creates a dialog with an option to toggle the cancel button and the third button. The buttons toggle only and only if the third
	 * button visibility is set to true
	 * 
	 * @param thirdButtonVisibility boolean flag which decides whether third button is visible
	 * @param toggleSwtich a switch which toggles the second and the third button if third button flag is on.
	 */
	public ConfirmationDialog(final boolean thirdButtonVisibility, final boolean toggleSwitch) {
		// JIRA-Bug-ID-10483
		// We need to set Modal true because modal dialogs hangs the browser. If two dialogs are opened at same time.
		super(false, false);
		setConfirmationDialogView(thirdButtonVisibility);
		if (thirdButtonVisibility) {
			addButtons(toggleSwitch);
		} else {
			addButtons(false);
		}
	}

	/**
	 * This API sets the default listener
	 * 
	 * @param thirdButtonVisibility
	 */
	public final void setDefaultListener(final boolean thirdButtonVisibility) {
		if (thirdButtonVisibility) {
			addDialogListener(new ThirdButtonListener() {

				@Override
				public void onOkClick() {
					hideConfirmationDialog();
				}

				@Override
				public void onCancelClick() {
					hideConfirmationDialog();
				}

				@Override
				public void onThirdButtonClick() {
					hideConfirmationDialog();
				}
			});
		} else {
			addDialogListener(new DialogListener() {

				@Override
				public void onOkClick() {
					hideConfirmationDialog();
				}

				@Override
				public void onCancelClick() {
					hideConfirmationDialog();
				}
			});
		}
	}

	public ConfirmationDialog() {
		this(false);
	}

	private void hideConfirmationDialog() {
		this.hide();
	}

	public void setMessage(String message) {
		centerWidget.setHTML(message);
	}

	public void setDialogTitle(String text) {
		setText(text);
	}

	@Override
	protected void onPreviewNativeEvent(NativePreviewEvent preview) {
		super.onPreviewNativeEvent(preview);

		NativeEvent evt = preview.getNativeEvent();
		if (evt.getType().equals("keydown")) {
			// Use the popup's key preview hooks to close the dialog when either
			// enter or escape is pressed.
			switch (evt.getKeyCode()) {
				case KeyCodes.KEY_ESCAPE:
					if (performCancelOnEscape && listener != null) {
						listener.onCancelClick();
					}
					hide();
					break;
				case 's':
				case 'S':
					if (evt.getCtrlKey()) {
						evt.preventDefault();
					}
					break;

			}
		}
	}

	/**
	 * Assigns the Handlers to all the three {@link Button} (named Ok ,Cancel and Third). These Buttons will call the corresponding
	 * onClick of their {@link DialogListener}
	 */
	private void assignClickHandlers() {

		// Handler of OK Button
		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (null != listener) {
					listener.onOkClick();
					hide();
				}
			}
		});

		// Handler of Cancel Button
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (null != listener) {
					listener.onCancelClick();
					hide();
				}
			}
		});

		// Handler of third button
		thirdButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (listener instanceof ThirdButtonListener) {
					((ThirdButtonListener) listener).onThirdButtonClick();
					hide();
				}
			}
		});
	}

	public final void addDialogListener(DialogListener dialogListener) {
		this.listener = dialogListener;
	}

	public final void addClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	public HTMLPanel getPanel() {
		return panel;
	}

	@Override
	public void hide() {
		super.hide();
		// On hide set focus to the previously selected element.
		if (null != focusedElement) {
			focusedElement.focus();
		}
	}
}
