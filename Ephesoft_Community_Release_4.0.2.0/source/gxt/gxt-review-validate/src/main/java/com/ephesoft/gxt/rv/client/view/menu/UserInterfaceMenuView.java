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

package com.ephesoft.gxt.rv.client.view.menu;

import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.rv.client.presenter.menu.UserInterfaceMenuPresenter;
import com.ephesoft.gxt.rv.client.view.ReviewValidateBaseView;
import com.ephesoft.gxt.rv.client.view.ShortcutView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class UserInterfaceMenuView extends ReviewValidateBaseView<UserInterfaceMenuPresenter> {

	interface Binder extends UiBinder<Widget, UserInterfaceMenuView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected CustomMenuBar externalInterfaceMenuBar;

	private final CustomMenuBar shortcutsMenu;

	@Override
	public void initialize() {
	}

	public UserInterfaceMenuView() {
		initWidget(binder.createAndBindUi(this));
		externalInterfaceMenuBar.setAutoOpen(false);
		externalInterfaceMenuBar.setFocusOnHoverEnabled(false);
		externalInterfaceMenuBar.addStyleName("externalInterfaceMenu");
		shortcutsMenu = new CustomMenuBar(true);
		this.addShortcutMenu();
	}

	private void addShortcutMenu() {
		final CustomMenuItem shortcutsMenuItem = new CustomMenuItem("Shortcuts", shortcutsMenu);
		externalInterfaceMenuBar.addItem(shortcutsMenuItem);
		WidgetUtil.setID(shortcutsMenuItem, "rv-shortcuts-Button");
		final ScheduledCommand command = new ScheduledCommand() {

			@Override
			public void execute() {
				addShortcutMenuItem(new ShortcutView("Save", "Ctrl + E"));
				addShortcutMenuItem(new ShortcutView("Split", "Ctrl + 2"));
				addShortcutMenuItem(new ShortcutView("Zoom-in", "Ctrl + 1"));
				addShortcutMenuItem(new ShortcutView("Zoom-out", "Ctrl + Shift + 1"));
				addShortcutMenuItem(new ShortcutView("Move cursor to next field", "Ctrl + ]"));
				addShortcutMenuItem(new ShortcutView("Move cursor to next error field", "Ctrl + >"));
				addShortcutMenuItem(new ShortcutView("Duplicate Page", "Ctrl + D"));
				addShortcutMenuItem(new ShortcutView("Fit to Page", "F12"));
				addShortcutMenuItem(new ShortcutView("Rotate Page", "Ctrl + R"));
				addShortcutMenuItem(new ShortcutView("Remove Page", "Ctrl + Delete"));
				addShortcutMenuItem(new ShortcutView("For Review to Validate", "Ctrl + Q"));
				addShortcutMenuItem(new ShortcutView("Focus on document types", "Ctrl + \\"));
				addShortcutMenuItem(new ShortcutView("Merge document with previous one", "Ctrl + /"));
				addShortcutMenuItem(new ShortcutView("Next Batch", "Ctrl + Shift + >"));
				addShortcutMenuItem(new ShortcutView("Fuzzy DB Search", "Ctrl + Z"));
				addShortcutMenuItem(new ShortcutView("Insert a row below in table", "Ctrl + I"));
				addShortcutMenuItem(new ShortcutView("Insert a row above in table", "Ctrl + Shift + I"));
				addShortcutMenuItem(new ShortcutView("Delete a row in table", "Ctrl + J"));
				addShortcutMenuItem(new ShortcutView("Delete All rows in table", "Ctrl + U"));
				addShortcutMenuItem(new ShortcutView("Traverse Table", "Ctrl + K"));
				addShortcutMenuItem(new ShortcutView("Insert Data Manually", "Ctrl + Y"));
				addShortcutMenuItem(new ShortcutView("Move Image Left", "Shift + left arrow"));
				addShortcutMenuItem(new ShortcutView("Move Image Right", "Shift + right arrow"));
				addShortcutMenuItem(new ShortcutView("Move Image Up", "Shift + up arrow"));
				addShortcutMenuItem(new ShortcutView("Move Image Down", "Shift + down arrow"));
				addShortcutMenuItem(new ShortcutView("Next Document", "Ctrl + M"));
				addShortcutMenuItem(new ShortcutView("Previous Document", "Ctrl + Shift + M"));
				addShortcutMenuItem(new ShortcutView("Next Page", "Ctrl +  P"));
				addShortcutMenuItem(new ShortcutView("Previous Page", "Ctrl + Shift + P"));
				addShortcutMenuItem(new ShortcutView("Update Document", "Ctrl + Shift + C"));
				addShortcutMenuItem(new ShortcutView("Toggle Column Validations while Manual Extraction", "Ctrl + '"));
				addShortcutMenuItem(new ShortcutView("Disable / Enable Page Jumping on Field Change	", "Ctrl + Shift + \\"));
			}
		};
		final Scheduler scheduler = new SchedulerImpl();
		scheduler.scheduleDeferred(command);
	}

	private void addShortcutMenuItem(final ShortcutView shortcutView) {
		final String innerHTML = WidgetUtil.getInnerHTML(shortcutView);
		final SafeHtml shortcutMenuItem = SafeHtmlUtils.fromTrustedString(innerHTML);
		shortcutsMenu.addItem(new CustomMenuItem(shortcutMenuItem));
	}
}
