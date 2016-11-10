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

package com.ephesoft.gxt.systemconfig.client.view.workflowmanagement;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.presenter.workflowmanagement.WorkflowManagmentMenuPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WorkflowManagmentMenuView extends SystemConfigInlineView<WorkflowManagmentMenuPresenter> {

	interface Binder extends UiBinder<Widget, WorkflowManagmentMenuView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected VerticalPanel menuBarPanel;

	@UiField
	protected CustomMenuBar batchClassMenuBar;

	protected CustomMenuItem addMenuItem;

	protected CustomMenuItem editMenuItem;

	protected CustomMenuItem deleteMenuItem;

	protected CustomMenuItem helpMenuItem;

	public WorkflowManagmentMenuView() {
		super();
		initWidget(binder.createAndBindUi(this));

		intializeMenuItems();

		addMenuItemActionEvents();

		batchClassMenuBar.setFocusOnHoverEnabled(false);

		WidgetUtil.setID(batchClassMenuBar, "batchClassMenuBar");
		setWidgetIDs();
	}

	private void setWidgetIDs() {
		WidgetUtil.setID(addMenuItem, "wm_add_menu_item");
		WidgetUtil.setID(deleteMenuItem, "wm_delete_menu_item");
		WidgetUtil.setID(editMenuItem, "wm_edit_menu_item");
		WidgetUtil.setID(helpMenuItem, "wm_help_menu_item");

	}

	private void addMenuItemActionEvents() {
		addMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.showAddPluginDependencyView();
			}
		});

		helpMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onHelpClicked();
			}
		});

		deleteMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.deleteSelectedPluginDependency();
			}
		});

		editMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.showEditPluginDependencyView();
			}
		});
	}

	/**
	 * 
	 */
	@SuppressWarnings("serial")
	private void intializeMenuItems() {
		addMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(SystemConfigConstants.ADD_CONSTANT);
			}
		});

		editMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(SystemConfigConstants.EDIT_CONSTANT);
			}
		});

		deleteMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(SystemConfigConstants.DELETE_CONSTANT);
			}
		});

		helpMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(SystemConfigConstants.HELP_BUTTON);
			}
		});

		batchClassMenuBar.addItem(addMenuItem);
		batchClassMenuBar.addItem(editMenuItem);
		batchClassMenuBar.addItem(deleteMenuItem);
		batchClassMenuBar.addItem(helpMenuItem);
		menuBarPanel.setWidth("100%");
	}

	@Override
	public void initialize() {
	}

}
