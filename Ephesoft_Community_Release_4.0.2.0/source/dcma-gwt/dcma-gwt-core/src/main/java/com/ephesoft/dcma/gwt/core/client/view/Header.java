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

package com.ephesoft.dcma.gwt.core.client.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.gwt.core.client.event.HelpClickEvent;
import com.ephesoft.dcma.gwt.core.client.event.SignoutEvent;
import com.ephesoft.dcma.gwt.core.client.event.TabSelectionEvent;
import com.ephesoft.dcma.gwt.core.client.event.ThemeChangeEvent;
import com.ephesoft.dcma.gwt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialog;
import com.ephesoft.dcma.gwt.core.shared.ConfirmationDialogUtil;
import com.ephesoft.dcma.gwt.core.shared.listener.ThirdButtonListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabBar.Tab;

public class Header extends DCMAComplexPanel {

	/**
	 * Constant for height of the logo.
	 */
	private static final String LOGO_HEIGHT = "36px";

	/**
	 * Constant for width of the logo.
	 */
	private static final String LOGO_WIDTH = "235px";
	private static final String DISCARD_BUTTON = LocaleDictionary.get().getConstantValue(
			LocaleCommonConstants.title_confirmation_discard);
	private static final String SAVE_BUTTON = LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_confirmation_save);
	private static final String CANCEL_BUTTON = LocaleDictionary.get().getConstantValue(
			LocaleCommonConstants.title_confirmation_cancel);

	/**
	 * The LOGO_HEADER_PANEL_CSS {@link String} is a constant for logo header panel CSS with class name 'logoHeaderPanel'.
	 */
	private static final String LOGO_HEADER_PANEL_CSS = "logoHeaderPanel";

	interface Binder extends UiBinder<DockLayoutPanel, Header> {
	}

	/*
	 * public interface Images extends ClientBundle {
	 * 
	 * ImageResource logo();
	 * 
	 * ImageResource logo_tk(); }
	 */

	@UiField
	Label userName;

	/**
	 * themeIcon {@link Label}.
	 */
	@UiField
	Label themeIcon;

	@UiField
	Anchor signOutLink;
	@UiField
	TabBar tabBar;

	@UiField
	Label logo;

	@UiField
	Anchor helpLink;

	@UiField
	HorizontalPanel signoutPanel;

	@UiField
	FocusPanel logoFocusPanel;

	@UiField
	HTMLPanel htmlPanel;

	private boolean dialogBoxOnTabClick;

	private String dialogMessage;

	private String saveButtonText = null;

	private String discardButtonText = null;

	private ConfirmationDialog confirmationDialog = null;

	private Map<Integer, String> tabs = new HashMap<Integer, String>();

	private static final Binder binder = GWT.create(Binder.class);

	public Header() {
		// Images images = GWT.create(Images.class);
		initWidget(binder.createAndBindUi(this));
		sinkEvents(Event.ONCONTEXTMENU);
		signOutLink.setText(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.header_label_signOut));
		helpLink.setText(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.header_label_help));
		String locale = LocaleDictionary.get().getLocaleInfo().getLocale();
		if (locale != null && locale.equalsIgnoreCase(CoreCommonConstants.TURKISH_POSTFIX)) {
			logo.setStyleName(CoreCommonConstants.IMAGE_LOGO_TK);
		} else {
			logo.setStyleName(CoreCommonConstants.IMAGE_LOGO);
		}
		logo.setSize(LOGO_WIDTH, LOGO_HEIGHT);
		tabBar.addStyleName(CoreCommonConstants.HEADER_TABS);
		signoutPanel.addStyleName(CoreCommonConstants.LOGO_PANEL);
		logoFocusPanel.addStyleName(LOGO_HEADER_PANEL_CSS);
		// Changes for theme icon in the header section
		themeIcon.setStyleName(CoreCommonConstants.SET_ICON_FOR_THEME_CSS);
		themeIcon.addStyleName(CoreCommonConstants.BUTTON_MARGIN_RIGHT_CSS);
		themeIcon.setTitle(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.CHANGE_THEME));
	}

	public void addNonClickableTab(String display, final String htmlPattern) {
		Label htmlUrl = new Label(display);
		htmlUrl.setWordWrap(false);
		this.tabBar.addTab(htmlUrl);
		tabs.put(this.tabBar.getTabCount() - 1, htmlPattern);
		selectTab();
	}

	public void addTab(String display, final String htmlPattern, final boolean isFromBatchList) {
		Label htmlUrl = new Label(display);
		htmlUrl.setWordWrap(false);
		this.tabBar.addTab(htmlUrl);
		final int tabIndex = this.tabBar.getTabCount() - 1;
		Tab tab = this.tabBar.getTab(tabIndex);

		// Fix for client issue Tabs Highlight but do not activate.
		tab.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				if (showDialogBoxOnTabClick()) {
					if (isFromBatchList) {
						confirmationDialog = new ConfirmationDialog(true, true);
						confirmationDialog.setMessage(dialogMessage);
						confirmationDialog.setDialogTitle(LocaleDictionary.get().getConstantValue(
								LocaleCommonConstants.dialog_box_title));
						confirmationDialog.okButton.setText(LocaleDictionary.get().getConstantValue(
								LocaleCommonConstants.title_confirmation_save));

						// JIRA 2434: Screen not sync
						confirmationDialog.cancelButton.setText(CANCEL_BUTTON);
						confirmationDialog.thirdButton.setText(DISCARD_BUTTON);
						confirmationDialog.show();
						confirmationDialog.center();
					} else {
						confirmationDialog = ConfirmationDialogUtil.showConfirmationDialog(dialogMessage, LocaleDictionary.get()
								.getConstantValue(LocaleCommonConstants.dialog_box_title), Boolean.FALSE);

					}
					if (saveButtonText != null) {
						confirmationDialog.okButton.setText(saveButtonText);
					} else {
						confirmationDialog.okButton.setText(SAVE_BUTTON);
					}

					// JIRA 2434: Screen not sync
					if (discardButtonText != null) {
						confirmationDialog.thirdButton.setText(discardButtonText);
					} else {
						confirmationDialog.thirdButton.setText(DISCARD_BUTTON);
					}
					confirmationDialog.setPerformCancelOnEscape(true);
					confirmationDialog.addDialogListener(new ThirdButtonListener() {

						// JIRA 2434: Screen not sync
						@Override
						public void onOkClick() {
							confirmationDialog.hide();
							if (isFromBatchList) {
								eventBus.fireEvent(new TabSelectionEvent(htmlPattern, Boolean.TRUE));
							} else
								moveToTab(htmlPattern);
						}

						@Override
						public void onCancelClick() {
							confirmationDialog.hide();
							selectTab();
						}

						@Override
						public void onThirdButtonClick() {
							confirmationDialog.hide();
							if (isFromBatchList) {
								eventBus.fireEvent(new TabSelectionEvent(htmlPattern, Boolean.FALSE));
							} else {
								selectTab();
							}
						}
					});
					confirmationDialog.okButton.setFocus(true);
				} else {
					moveToTab(htmlPattern);
				}

			}
		});
		htmlUrl.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent mouseDown) {
				if (mouseDown.getNativeButton() == Event.BUTTON_RIGHT) {

					final ContextMenuPanel contextMenu = new ContextMenuPanel();
					contextMenu.show();
					MenuBar menuBar = new MenuBar(true);
					MenuItem openMenuItem = new MenuItem("Open in New Tab", new Command() {

						@Override
						public void execute() {
							contextMenu.hide();
							openUrlInNewTab(htmlPattern);
						}

					});
					MenuItem reloadMenuItem = new MenuItem("Reload", new Command() {

						@Override
						public void execute() {
							contextMenu.hide();
							reloadCurrentTab();
						}
					});
					menuBar.addItem(openMenuItem);
					menuBar.addItem(reloadMenuItem);
					contextMenu.setWidget(menuBar);
					contextMenu.setPopupPosition(mouseDown.getNativeEvent().getClientX(), mouseDown.getNativeEvent().getClientY());
					contextMenu.show();
				}
			}
		});

		tabs.put(tabIndex, htmlPattern);
		selectTab();
	}

	public TabBar getTabBar() {
		return this.tabBar;
	}

	public void selectTab() {
		String href = Window.Location.getHref();

		String url = href.substring(href.lastIndexOf('/') + 1);
		if (url.contains("?")) {
			url = url.substring(0, url.indexOf("?"));
		}
		Set<Map.Entry<Integer, String>> tabSet = tabs.entrySet();
		for (Map.Entry<Integer, String> entry : tabSet) {
			if (entry.getValue().equals(url) && this.tabBar.getSelectedTab() != entry.getKey()) {
				this.tabBar.selectTab(entry.getKey());
				break;
			}
		}
	}

	public void setUserName(String name) {
		this.userName.setText(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.header_label_hi) + " " + name);
	}

	@UiHandler("signOutLink")
	void onSignOutClicked(ClickEvent event) {

		// To mask screen while signing out, to stop further user interaction
		ScreenMaskUtility.maskScreen();
		this.eventBus.fireEvent(new SignoutEvent());
	}

	@UiHandler("helpLink")
	void onhelpClicked(ClickEvent event) {
		this.eventBus.fireEvent(new HelpClickEvent());
	}

	/**
	 * Handling to be done on change theme icon click.
	 * 
	 * @param event {@link ClickEvent}
	 */
	@UiHandler("themeIcon")
	void onThemeIconClicked(ClickEvent event) {
		this.eventBus.fireEvent(new ThemeChangeEvent());
	}

	private void moveToTab(String htmlPattern) {
		String href = Window.Location.getHref();
		String baseUrl = href.substring(0, href.lastIndexOf("/"));
		Window.Location.assign(baseUrl + "/" + htmlPattern);
	}

	private void openUrlInNewTab(String htmlPattern) {
		String href = Window.Location.getHref();
		String baseUrl = href.substring(0, href.lastIndexOf("/"));
		Window.open(baseUrl + "/" + htmlPattern, "_blank", null);
	}

	private void reloadCurrentTab() {
		Window.Location.reload();
	}

	public boolean showDialogBoxOnTabClick() {
		return dialogBoxOnTabClick;
	}

	public void setShowDialogBoxOnTabClick(boolean show) {
		dialogBoxOnTabClick = show;
	}

	public void setDialogMessage(String dialogMessage) {
		this.dialogMessage = dialogMessage;
	}

	public void setButtonText(String saveButtonText, String discardButtonText) {
		this.saveButtonText = saveButtonText;
		this.discardButtonText = discardButtonText;
	}

	@Override
	public void onBrowserEvent(Event event) {
		switch (DOM.eventGetType(event)) {
			case Event.ONCONTEXTMENU:
				event.cancelBubble(true);
				event.preventDefault();
				break;
		}
	}
}
