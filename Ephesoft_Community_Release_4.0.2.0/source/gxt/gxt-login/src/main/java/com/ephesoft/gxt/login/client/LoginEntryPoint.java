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

package com.ephesoft.gxt.login.client;

import java.util.Map;

import com.ephesoft.gxt.core.client.DCMAEntryPoint;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.i18n.LocaleInfo;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.login.client.i18n.LoginConstants;
import com.ephesoft.gxt.login.client.i18n.LoginMessages;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SubmitButton;

public class LoginEntryPoint extends DCMAEntryPoint<DCMARemoteServiceAsync> {

	/**
	 * The FONT_RED_CSS {@link String} is a CSS Class name for text color red.
	 */
	private static final String FONT_RED_CSS = "fontRed";

	/**
	 * The BOLD_TEXT_CSS {@link String} is a CSS Class name for bold text.
	 */
	private static final String BOLD_TEXT_CSS = "boldTxt";
	private static final String EXPIRY_MSG_LEBEL = "expiryMsgLabel";
	private static final String FAIL_OVER_LABEL = "failOverMsgLabel";

	String footerText;
	String footerLink;

	@Override
	public void onLoad() {
		Window.setTitle(LocaleDictionary.getConstantValue(LoginConstants.LOGIN_TITLE));

		Label userName = new Label(LocaleDictionary.getConstantValue(LoginConstants.login_username));
		Label password = new Label(LocaleDictionary.getConstantValue(LoginConstants.login_password));
		RootPanel.get("loginHeader").getElement().setInnerText(LocaleDictionary.getConstantValue(LoginConstants.login_button_text));
		RootPanel.get("j_username").getElement().setAttribute("placeholder", userName.getText());
		RootPanel.get("j_username").getElement().focus();
		RootPanel.get("j_password").getElement().setAttribute("placeholder", password.getText());
		final Label versionNumber = new Label();

		((LoginRemoteServiceAsync) createRpcService()).getProductVersion(new AsyncCallback<String>() {

			@Override
			public void onSuccess(String version) {
				versionNumber.setText(version);
				RootPanel.get("versionLabel").getElement().setInnerText(versionNumber.getText());
			}

			@Override
			public void onFailure(Throwable arg0) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(LocaleCommonConstants.ERROR_TITLE),
						LocaleDictionary.getConstantValue(LoginConstants.UNABLE_TO_RETRIVE_VERSION_INFO), DialogIcon.ERROR);

			}
		});

		final Label expiryMsg = new Label();
		final Label failOverMsg = new Label();
		((LoginRemoteServiceAsync) createRpcService()).getLicenseExpiryMsg(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				String alertMsg = arg0.getLocalizedMessage();
				int indexOfDelimiter = alertMsg.indexOf('@');
				String days = alertMsg.substring(0, indexOfDelimiter);
				int remainingDays = Integer.parseInt(days);
				String dateString = alertMsg.substring(indexOfDelimiter + 1);
				if (remainingDays != 0) {
					expiryMsg.setText(LocaleDictionary.getMessageValue(LoginMessages.LICENSE_EXPIRY_MSG, days, dateString));
				} else {
					expiryMsg.setText(LocaleDictionary.getMessageValue(LoginMessages.LICENSE_EXPIRY_MSG_TODAY));
				}
				expiryMsg.addStyleName(FONT_RED_CSS);
				expiryMsg.addStyleName(BOLD_TEXT_CSS);
				RootPanel.get(EXPIRY_MSG_LEBEL).getElement().setInnerText(expiryMsg.getText());
				RootPanel.get(EXPIRY_MSG_LEBEL).addStyleName(expiryMsg.getStyleName());
			}

			@Override
			public void onSuccess(Void arg0) {
			}
		});

		((LoginRemoteServiceAsync) createRpcService()).getFailOverMessage(new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if (true == result) {
					// failOverMsg.setText("");
				} else {
					failOverMsg.setText(LocaleDictionary.getMessageValue(LoginMessages.FAILOVER_MSG));
					failOverMsg.addStyleName(FONT_RED_CSS);
					failOverMsg.addStyleName(BOLD_TEXT_CSS);
					RootPanel.get(FAIL_OVER_LABEL).getElement().setInnerText(failOverMsg.getText());
					RootPanel.get(FAIL_OVER_LABEL).addStyleName(failOverMsg.getStyleName());
				}
			}

			@Override
			public void onFailure(Throwable arg0) {

			}

		});
		final HorizontalPanel horPanel = new HorizontalPanel();
		horPanel.addStyleName("horizontalPanel");
		RootPanel.get().add(horPanel);
		ScreenMaskUtility.maskScreen(LocaleDictionary.getMessageValue(LoginMessages.LOADING));
		rpcService.getFooterProperties(new AsyncCallback<Map<String, String>>() {

			@Override
			public void onSuccess(Map<String, String> footerProperties) {
				footerText = footerProperties.get(CoreCommonConstants.FOOTER_TEXT_KEY);
				footerLink = footerProperties.get(CoreCommonConstants.FOOTER_LINK_KEY);
				final Anchor footerInfo = new Anchor(footerText, footerLink);
				horPanel.addStyleName("loginFooter");
				horPanel.add(footerInfo);
				horPanel.setCellHorizontalAlignment(footerInfo, HasHorizontalAlignment.ALIGN_CENTER);
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onFailure(Throwable arg0) {
				ScreenMaskUtility.unmaskScreen();
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(LocaleCommonConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(LoginMessages.APPLICATION_NOT_AUTHORIZED), DialogIcon.ERROR);
			}
		});
		RootPanel.get("error").getElement().setInnerText(LocaleDictionary.getConstantValue(LoginConstants.INVALID_CREDENTIALS));
		RootPanel.get("error").addStyleName(FONT_RED_CSS);

		SubmitButton submitButton = new SubmitButton();
//				(SubmitButton) SubmitButton.wrap(RootPanel.get("button").getElement());
		// submitButton.setFocus(true);
		
		submitButton.getElement().setTitle(LocaleDictionary.getConstantValue(LoginConstants.login_button_text));
		submitButton.getElement().setInnerHTML("<i class=\"icon-arrow-right icon-large\"></i>");
		submitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				login();
			}
		});

		submitButton.setStyleName("gwt-Button");
		RootPanel.get("button").getElement().addClassName("submit");
		RootPanel.get("button").add(submitButton);
		
	}

	private native void login() /*-{
								return $wnd.loginSubmit();
								}-*/;

	@Override
	public DCMARemoteServiceAsync createRpcService() {
		return GWT.create(LoginRemoteService.class);
	}

	@Override
	public String getHomePage() {
		return LocaleCommonConstants.LOGIN_HTML;
	}

	@Override
	public LocaleInfo createLocaleInfo(String locale) {
		return new LocaleInfo(locale, "loginConstants", "loginMessages");
	}

	@Override
	public com.ephesoft.gxt.core.client.DCMAEntryPoint.ScreenType getScreenType() {
		return null;
	}
}
