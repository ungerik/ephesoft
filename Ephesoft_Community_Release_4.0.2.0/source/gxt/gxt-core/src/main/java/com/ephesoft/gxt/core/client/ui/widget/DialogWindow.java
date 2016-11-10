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

package com.ephesoft.gxt.core.client.ui.widget;

import com.ephesoft.gxt.core.client.ui.widget.listener.DialogListener;
import com.ephesoft.gxt.core.client.ui.widget.util.CookieUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.event.MoveEvent;
import com.sencha.gxt.widget.core.client.event.MoveEvent.MoveHandler;

public class DialogWindow extends Dialog {

	private DialogListener dialogListener;

	private boolean preferCookieDimension;

	private Widget lastFocusableWidget;

	private boolean focusSupport = false;

	private boolean focusproviderAdded = false;

	public DialogWindow() {
		super();
		initializeDefaultProperties();
	}

	public DialogWindow(boolean provideFocusSupport, Widget lastFocusableWidget) {
		super();
		this.focusSupport = provideFocusSupport;
		this.lastFocusableWidget = lastFocusableWidget;
		initializeDefaultProperties();
	}

	private void initializeDefaultProperties() {
		this.addResizeHandler();
		this.addMoveHandler();
		this.setModal(true);
		this.setOnEsc(true);
		this.setButtonAlign(BoxLayoutPack.CENTER);
		WidgetUtil.setID(this, "dialogWindow");
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
//		this.focus();
		preferCookieDimension = true;
		this.addStyleName("dialog");
	}

	public DialogWindow(WindowAppearance appearance) {
		super(appearance);
		initializeDefaultProperties();
	}

	public DialogWindow(WindowAppearance appearance, boolean provideFocusSupport, Widget lastFocusableWidget) {
		super(appearance);
		this.focusSupport = provideFocusSupport;
		this.lastFocusableWidget = lastFocusableWidget;
		initializeDefaultProperties();
	}

	public DialogWindow(boolean wantOKCANCEL, boolean provideFocusSupport, Widget lastFocusableWidget) {
		this.addResizeHandler();
		this.addMoveHandler();
		this.setModal(true);
		this.setOnEsc(true);
		this.setButtonAlign(BoxLayoutPack.CENTER);
		if (wantOKCANCEL) {
			// this.setPredefinedButtons(PredefinedButton.OK);
		} else {
			setPredefinedButtons();
		}
//		this.focus();
		this.addStyleName("dialog");
		this.focusSupport = provideFocusSupport;
		this.lastFocusableWidget = lastFocusableWidget;
	}
	
	public DialogWindow(boolean wantOKCANCEL) {
		this.addResizeHandler();
		this.addMoveHandler();
		this.setModal(true);
		this.setOnEsc(true);
		this.setButtonAlign(BoxLayoutPack.CENTER);
		if (wantOKCANCEL) {
			// this.setPredefinedButtons(PredefinedButton.OK);
		} else {
			setPredefinedButtons();
		}

//		this.focus();
		this.addStyleName("dialog");
	}

	public void setPreferCookieDimension(boolean value) {
		this.preferCookieDimension = value;
	}

	private void addResizeHandler() {
		this.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				storeLayoutDimensionInCookies();
			}
		});
	}

	private void addMoveHandler() {
		this.addMoveHandler(new MoveHandler() {

			@Override
			public void onMove(MoveEvent event) {
				storeLayoutDimensionInCookies();
			}
		});
	}

	private void storeLayoutDimensionInCookies() {
		String elementId = this.getElement().getId();
		if (!StringUtil.isNullOrEmpty(elementId) && preferCookieDimension) {
			String absoluteLeftCookieName = CookieUtil.getCookieNameForAbsoluteLeft(elementId);
			String absoluteTopCookieName = CookieUtil.getCookieNameForAbsoluteTop(elementId);
			String widthCookieName = CookieUtil.getCookieNameForWidth(elementId);
			String heightCookieName = CookieUtil.getCookieNameForHeight(elementId);
			Integer absoluteLeft = this.getElement().getAbsoluteLeft();
			Integer absoluteTop = this.getElement().getAbsoluteTop();
			Integer width = this.getOffsetWidth();
			Integer height = this.getOffsetHeight();
			CookieUtil.storeCookie(widthCookieName, width.toString());
			CookieUtil.storeCookie(absoluteTopCookieName, absoluteTop.toString());
			CookieUtil.storeCookie(absoluteLeftCookieName, absoluteLeft.toString());
			CookieUtil.storeCookie(heightCookieName, height.toString());
		}
	}

	public void show() {
		String elementId = this.getElement().getId();
		if (!StringUtil.isNullOrEmpty(elementId) && preferCookieDimension) {
			String absoluteLeftCookieName = CookieUtil.getCookieNameForAbsoluteLeft(elementId);
			String absoluteTopCookieName = CookieUtil.getCookieNameForAbsoluteTop(elementId);
			String widthCookieName = CookieUtil.getCookieNameForWidth(elementId);
			String heightCookieName = CookieUtil.getCookieNameForHeight(elementId);
			String heightCookieValue = CookieUtil.getCookieValue(heightCookieName);
			String widthCookieValue = CookieUtil.getCookieValue(widthCookieName);
			String absoluteTopCookieValue = CookieUtil.getCookieValue(absoluteTopCookieName);
			String absoluteLeftCookieValue = CookieUtil.getCookieValue(absoluteLeftCookieName);
			if (!StringUtil.isNullOrEmpty(heightCookieValue)) {
				this.setSize(widthCookieValue, heightCookieValue);
				this.setPosition(Integer.parseInt(absoluteLeftCookieValue), Integer.parseInt(absoluteTopCookieValue));
			}
		}
		super.show();
		this.focus();
	}

	@Override
	protected void onButtonPressed(final TextButton textButton) {
		super.onButtonPressed(textButton);
		final PredefinedButton predefinedButton = this.getPredefinedButton(textButton);
		if (null != predefinedButton && dialogListener != null) {
			switch (predefinedButton) {
				case OK:
					dialogListener.onOkClick();
					break;
				case CLOSE:
					dialogListener.onCloseClick();
					break;
				case CANCEL:
					dialogListener.onCancelClick();
					break;
				default:
					break;
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

	protected void createButtons() {
		super.createButtons();
		if (!focusproviderAdded) {
			if (focusSupport) {
				if (null == lastFocusableWidget) {
					// add Hidden Button to move to first widget on TAB press.
					TextButton hiddenButton = new TextButton("");
					hiddenButton.setBorders(false);
					hiddenButton.setHeight(0);
					hiddenButton.setWidth(0);
					hiddenButton.getElement().setOpacity(0.001);
					addButton(hiddenButton);
					hiddenButton.addFocusHandler(new FocusHandler() {

						@Override
						public void onFocus(FocusEvent event) {
							focusOnFirstElement();
						}
					});
				} else {
					// Set focus back to first element on TAB press on last widget
					lastFocusableWidget.addDomHandler(new KeyDownHandler() {

						@Override
						public void onKeyDown(KeyDownEvent event) {
							if (event.getNativeKeyCode() == KeyCodes.KEY_TAB) {
								focusOnFirstElement();
							}
						}
					}, KeyDownEvent.getType());
				}
				focusproviderAdded = true;
			}
		}
	}

	private void focusOnFirstElement() {
		this.focus();
	}
}
