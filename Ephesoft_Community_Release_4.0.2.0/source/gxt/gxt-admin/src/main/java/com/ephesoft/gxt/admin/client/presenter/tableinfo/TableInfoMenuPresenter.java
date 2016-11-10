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

package com.ephesoft.gxt.admin.client.presenter.tableinfo;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AddTableInfoEvent;
import com.ephesoft.gxt.admin.client.event.ChangeTestTableToggleButtonStateEvent;
import com.ephesoft.gxt.admin.client.event.CloseTestTableDialogWindowEvent;
import com.ephesoft.gxt.admin.client.event.DeleteTableInfosEvent;
import com.ephesoft.gxt.admin.client.event.EnableTableMenuItemsEvent;
import com.ephesoft.gxt.admin.client.event.TestTableEvent;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.tableinfo.TableInfoMenuView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * This presenter deals with Table Info Menu.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.presenter.tableinfo.TableInfoMenuPresenter
 */
public class TableInfoMenuPresenter extends BatchClassInlinePresenter<TableInfoMenuView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<TableInfoMenuPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Instantiates a new table info menu presenter.
	 * 
	 * @param controller the controller {@link BatchClassManagementController}
	 * @param view the view {@link TableInfoMenuView}
	 */
	public TableInfoMenuPresenter(final BatchClassManagementController controller, final TableInfoMenuView view) {
		super(controller, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.presenter.BatchClassManagementMenuPresenter#bind()
	 */
	@Override
	public void bind() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ephesoft.gxt.admin.client.presenter.BatchClassManagementMenuPresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	/**
	 * Adds new table info configuration.
	 */
	public void addTableInfo() {
		controller.getEventBus().fireEvent(new AddTableInfoEvent());
	}

	/**
	 * Delete table info configuration.
	 */
	public void deleteTableInfos() {
		controller.getEventBus().fireEvent(new DeleteTableInfosEvent());
	}

	public void showTestTableView() {
		controller.getEventBus().fireEvent(new TestTableEvent());
	}

	public void closeDialogWindow() {
		controller.getEventBus().fireEvent(new CloseTestTableDialogWindowEvent());
	}

	@EventHandler
	public void enableAllOtherMenuItems(final EnableTableMenuItemsEvent event) {
		if (null != event) {
			view.enableAllOtherMenuItems(event.isMenuItemsEnabled());
		}
	}

	@EventHandler
	public void handleTestTableToggleButtonState(final ChangeTestTableToggleButtonStateEvent event) {
		if (null != event) {
			view.setTestTableButtonDown(false);
		}
	}
}
