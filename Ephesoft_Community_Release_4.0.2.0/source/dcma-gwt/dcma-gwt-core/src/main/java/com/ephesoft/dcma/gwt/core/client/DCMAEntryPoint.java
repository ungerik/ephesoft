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

package com.ephesoft.dcma.gwt.core.client;

import com.ephesoft.dcma.gwt.core.client.event.HelpClickEvent;
import com.ephesoft.dcma.gwt.core.client.event.HelpClickEventHandler;
import com.ephesoft.dcma.gwt.core.client.event.SignoutEvent;
import com.ephesoft.dcma.gwt.core.client.event.SignoutEventHandler;
import com.ephesoft.dcma.gwt.core.client.event.ThemeChangeEvent;
import com.ephesoft.dcma.gwt.core.client.event.ThemeChangeEventHandler;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleInfo;
import com.ephesoft.dcma.gwt.core.client.util.WindowUtil;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.StringUtil;
import com.ephesoft.dcma.gwt.core.shared.constants.CoreCommonConstants;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class DCMAEntryPoint<R extends DCMARemoteServiceAsync> implements EntryPoint {

	interface GlobalResources extends ClientBundle {

		@NotStrict
		@Source("global.css")
		CssResource css();
	}

	protected R rpcService;

	protected HandlerManager eventBus;

	@Override
	public void onModuleLoad() {

		GWT.<GlobalResources> create(GlobalResources.class).css().ensureInjected();

		this.eventBus = new HandlerManager(null);
		this.rpcService = createRpcService();

		eventBus.addHandler(SignoutEvent.TYPE, new SignoutEventHandler() {

			@Override
			public void onSignout(final SignoutEvent event) {
				rpcService.logout(Window.getTitle(), new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable throwable) {
						// Do nothing
					}

					@Override
					public void onSuccess(final String logoutURL) {

						// Common service added for authorisation and
						// authentication
						String tempLogoutURL = logoutURL;
						if (StringUtil.isNullOrEmpty(tempLogoutURL)) {
							tempLogoutURL = StringUtil.concatenate(GWT.getHostPageBaseURL(), getHomePage());
						}
						Window.Location.assign(tempLogoutURL);
					}
				});
			}
		});

		eventBus.addHandler(HelpClickEvent.TYPE, new HelpClickEventHandler() {

			@Override
			public void onHelpClicked(final HelpClickEvent event) {
				rpcService.getHelpUrl(new AsyncCallback<String>() {

					@Override
					public void onSuccess(final String url) {
						
						// opening the help url in new tab
						// Fix for JIRA ISSUE #11388 added features to new window to be opened.
						String features = "resizable=1,scrollbars=1";
						Window.open(url, "_blank", features);

					}

					@Override
					public void onFailure(final Throwable arg0) {
						ConfirmationDialogUtil.showConfirmationDialog(
								LocaleDictionary.get().getConstantValue(LocaleCommonConstants.help_url_error_msg), LocaleDictionary
										.get().getConstantValue(LocaleCommonConstants.ERROR_TITLE), true);

					}
				});
			}
		});

		// Changes done for theme handler
		eventBus.addHandler(ThemeChangeEvent.TYPE, new ThemeChangeEventHandler() {

			@Override
			public void onThemeChange(final ThemeChangeEvent event) {
				String previousUrl = Window.Location.getHref();
				Window.Location.assign(GWT.getHostPageBaseURL() + LocaleCommonConstants.THEME_HTML);

				rpcService.setAttributeInSession(LocaleCommonConstants.PREVIOUS_URL, previousUrl, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(Void result) {
						// Do nothing.
					}

					@Override
					public void onFailure(final Throwable throwable) {
						ConfirmationDialogUtil.showConfirmationDialog(
								LocaleDictionary.get().getConstantValue(LocaleCommonConstants.ERROR_STORING_URL_IN_SESSION),
								LocaleDictionary.get().getConstantValue(LocaleCommonConstants.ERROR_TITLE), true);

					}
				});
			}
		});

		preprocess();
	}

	private void initialize(final String locale) {
		LocaleDictionary.create(createLocaleInfo(locale));
		this.rpcService.initRemoteService(new AsyncCallback<Void>() {

			@Override
			public void onFailure(final Throwable throwable) {

				// Common service added for authorisation and authentication
				rpcService.getAuthenticationType(new AsyncCallback<Integer>() {

					@Override
					public void onFailure(Throwable throwable) {
						// Do nothing
					}

					@Override
					public void onSuccess(final Integer authenticationType) {
						if (null == authenticationType || authenticationType.intValue() == 0) {
							ConfirmationDialogUtil.showConfirmationDialogError(LocaleDictionary.get().getConstantValue(
									throwable.getMessage().replace('.', '_')));
						} else {
							WindowUtil.redirectToResourse(CoreCommonConstants.LICENSE_ERROR_PAGE_PATH);
						}
					}
				});
			}

			@Override
			public void onSuccess(final Void arg0) {
				// defineBridgeMethod();

				onLoad();

			}
		});
	}

	private void preprocess() {
		this.rpcService.getUserName(new AsyncCallback<String>() {

			@Override
			public void onFailure(final Throwable arg0) {
				// Do nothing.
			}

			@Override
			public void onSuccess(final String userName) {

				// To get the selected theme for the user.
				rpcService.getSelectedTheme(new EphesoftAsyncCallback<String>() {

					@Override
					public void customFailure(final Throwable throwable) {
						// Do nothing.
					}

					@Override
					public void onSuccess(final String theme) {
						if (theme != null) {
							final StringBuilder url = new StringBuilder();
							url.append(LocaleCommonConstants.THEMES).append(LocaleCommonConstants.URL_SEPARATOR).append(theme)
									.append(LocaleCommonConstants.JS_EXTENSION);

							loadJs(url.toString());
						}

						rpcService.getLocale(new AsyncCallback<String>() {

							@Override
							public void onFailure(final Throwable throwable) {
								initialize("");
							}

							@Override
							public void onSuccess(final String locale) {
								initialize(locale);
							}
						});
					}

				});

			}
		});
	}

	public HandlerManager getEventBus() {
		return eventBus;
	}

	public R getRpcService() {
		return rpcService;
	}

	public void onCloseWindow() {
		/*
		 * rpcService.cleanup(new AsyncCallback<Void>() {
		 * 
		 * @Override public void onFailure(Throwable caught) { }
		 * 
		 * @Override public void onSuccess(Void arg0) { } });
		 */
	}

	public native void defineBridgeMethod() /*-{
											var _this = this;
											$wnd.onCloseWindow = function() {
											return _this.@com.ephesoft.dcma.gwt.core.client.DCMAEntryPoint::onCloseWindow()();
											}
											}-*/;

	public abstract void onLoad();

	public abstract R createRpcService();

	public abstract String getHomePage();

	public abstract LocaleInfo createLocaleInfo(String locale);

	/**
	 * To change the theme color according to the user's selected theme.
	 * 
	 * @param url String
	 */
	public static native void loadJs(String url) /*-{  
													return $wnd.loadJs(url);
													}-*/;

}
