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

package com.ephesoft.gxt.admin.client.view.tablecolumnextraction.advcancedtablecolumnextractionrule;

import com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction.advancedtablecolumnextractionrule.AdvancedTableColumnExtractionMenuPresenter;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AdvancedTableColumnExtractionMenuView extends
		AdvancedTableColumnExtractionInlineView<AdvancedTableColumnExtractionMenuPresenter> {

	interface Binder extends UiBinder<Widget, AdvancedTableColumnExtractionMenuView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);
	@UiField
	protected HorizontalPanel advTableColumnExtractionMenuPanel;
	@UiField
	protected CustomMenuBar advTableColumnExtractionMenuBar;

	private CustomMenuItem clearMenuItem;

	private CustomMenuItem clearAllMenuItem;

	private CustomMenuItem cancelMenuItem;

	private CustomMenuItem applyMenuItem;

	AdvancedTableColumnExtractionMenuView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		setWidgetIDs();
		initializeMenuItems();
		addMenuItemActions();
		initializeMenuItems();
		addMenuItemActions();
		advTableColumnExtractionMenuBar.addItem(applyMenuItem);
		advTableColumnExtractionMenuBar.addItem(clearMenuItem);
		advTableColumnExtractionMenuBar.addItem(clearAllMenuItem);
		advTableColumnExtractionMenuBar.addItem(cancelMenuItem);
	}

	private void initializeMenuItems() {
		applyMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return "Apply";
			}
		});

		clearAllMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return "Clear All";
			}
		});
		clearMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return "Clear";
			}
		});

		cancelMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return "Cancel";
			}
		});
	}

	private void addMenuItemActions() {
		applyMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onApply();
			}
		});
		clearAllMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onClearAll();
			}
		});
		clearMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onClear();
			}
		});
		cancelMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onCancel();
			}
		});
	}

	private void setWidgetIDs() {
		WidgetUtil.setID(advTableColumnExtractionMenuPanel, "advTableColumnExtractionMenuPanel");
		WidgetUtil.setID(advTableColumnExtractionMenuBar, "advTableColumnExtractionMenuBar");
		WidgetUtil.setID(applyMenuItem, "applyMenuItem");
		WidgetUtil.setID(clearAllMenuItem, "clearAllMenuItem");
		WidgetUtil.setID(clearMenuItem, "clearMenuItem");
		WidgetUtil.setID(cancelMenuItem, "cancelMenuItem");
	}

	public void enableClearButtons(boolean enable) {
		clearMenuItem.setEnabled(enable);
		clearAllMenuItem.setEnabled(enable);
	}

	public void enableApplyButton(boolean enabled) {
		applyMenuItem.setEnabled(enabled);
	}
}
