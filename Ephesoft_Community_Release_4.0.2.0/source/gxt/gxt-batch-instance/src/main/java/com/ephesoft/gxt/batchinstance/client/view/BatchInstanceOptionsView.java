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

package com.ephesoft.gxt.batchinstance.client.view;

import com.ephesoft.dcma.core.common.ModuleName;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceMessages;
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstanceOptionsPresenter;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

public class BatchInstanceOptionsView extends View<BatchInstanceOptionsPresenter> {

	interface Binder extends UiBinder<Widget, BatchInstanceOptionsView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected HorizontalPanel optionsContainer;

	@UiField
	protected HorizontalLayoutContainer buttonContainer;

	protected CustomMenuBar menuBar = new CustomMenuBar();

	protected CustomMenuItem openMenuItem;

	protected CustomMenuItem unlockMenuItem;

	protected CustomMenuItem deleteMenuItem;

	protected CustomMenuItem restartMenuItem;

	@SuppressWarnings("serial")
	public BatchInstanceOptionsView() {
		super();
		initWidget(binder.createAndBindUi(this));

		openMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {

				return LocaleDictionary.getConstantValue(BatchInstanceConstants.OPEN_MENU_ITEM_TITLE);
			}
		});

		unlockMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {

				return LocaleDictionary.getConstantValue(BatchInstanceConstants.UNLOCK_MENU_ITEM_TITLE);
			}
		});

		deleteMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {

				return LocaleDictionary.getConstantValue(BatchInstanceConstants.DELETE_MENU_ITEM_TITLE);
			}
		});

		CustomMenuBar restartMenuBar = new CustomMenuBar(true);
		restartMenuBar.setAutoOpen(true);
		CustomMenuItem restartCurrentMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {

				return LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_AT_CURRENT_MODULE_MENU_ITEM_TITLE);
			}
		});
		CustomMenuItem restartPreviousMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {

				return LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_AT_PREVIOUS_MODULE_MENU_ITEM_TITLE);
			}
		});
		CustomMenuItem restartFirstMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {

				return LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_AT_FIRST_MODULE_MENU_ITEM_TITLE);
			}
		});

		CustomMenuBar restartSelectedMenuItem = new CustomMenuBar(true);
		restartSelectedMenuItem.setAutoOpen(true);
		inistializeRestartSelectedMenu(restartSelectedMenuItem);

		restartCurrentMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {

				presenter.restartCurrentBatch();
			}
		});
		restartPreviousMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.restartPreviousBatch();
			}
		});
		restartFirstMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.restartFirstBatch();
			}
		});

		restartMenuBar.addItem(restartCurrentMenuItem);
		restartMenuBar.addItem(restartPreviousMenuItem);
		restartMenuBar.addItem(restartFirstMenuItem);
		final String restartAtSelectedModuleTitle = LocaleDictionary
				.getConstantValue(BatchInstanceConstants.RESTART_AT_SELECTED_MODULE_MENU_ITEM_TITLE);
		restartMenuBar.addItem(restartAtSelectedModuleTitle, restartSelectedMenuItem);
		restartMenuItem = new CustomMenuItem(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_MENU_ITEM_TITLE),
				restartMenuBar);
		menuBar.addItem(openMenuItem);
		menuBar.addItem(restartMenuItem);
		menuBar.addItem(unlockMenuItem);
		menuBar.addItem(deleteMenuItem);
		menuBar.setAutoOpen(true);
		menuBar.setFocusOnHoverEnabled(false);
		menuBar.addStyleName("menuBarBatchInstance");
		buttonContainer.addStyleName("buttonContainer");
		buttonContainer.add(menuBar);

		intializeSelectionHandlers();

		addWidgetIDs();
		WidgetUtil.setID(restartCurrentMenuItem, "bimRestartCurrentMenuItem");
		WidgetUtil.setID(restartPreviousMenuItem, "bimRestartPreviousMenuItem");
		WidgetUtil.setID(restartFirstMenuItem, "bimRestartFirstMenuItem");
		WidgetUtil.setID(restartSelectedMenuItem, "bimRestartSelectedMenuItem");

	}

	private void addWidgetIDs() {
		WidgetUtil.setID(openMenuItem, "bimOpenMenuItem");
		WidgetUtil.setID(restartMenuItem, "bimRestartMenuItem");
		WidgetUtil.setID(unlockMenuItem, "bimUnlockMenuItem");
		WidgetUtil.setID(deleteMenuItem, "bimDeleteMenuItem");
	}

	private void inistializeRestartSelectedMenu(CustomMenuBar restartSelectedMenuBar) {
		for (final String modulename : ModuleName.valuesAsStringList()) {
			CustomMenuItem restartSelectedMenuItem = new CustomMenuItem(new SafeHtml() {

				@Override
				public String asString() {
					return modulename;
				}
			});
			restartSelectedMenuItem.setScheduledCommand(new ScheduledCommand() {

				@Override
				public void execute() {
					presenter.restartSelectedBatch(modulename);
				}
			});
			restartSelectedMenuBar.addItem(restartSelectedMenuItem);
			
			WidgetUtil.setID(restartSelectedMenuItem, "bim"+modulename+"name");
		}
	}

	/**
	 * 
	 */
	private void intializeSelectionHandlers() {
		deleteMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {

				presenter.deleteMultipleBatches();
			}
		});

		unlockMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {

				presenter.unlockBatch();
			}
		});

		openMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {

				presenter.openSelectedBatchInstance();
			}
		});

	}

	@Override
	public void initialize() {
	}

	public void isBatchInstanceLocked(boolean isDelete, BatchInstanceDTO batchInstanceDTO) {
		if (batchInstanceDTO.getCurrentUser() != null && !batchInstanceDTO.getCurrentUser().isEmpty()) {

			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.INFO_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.OPERATION_FAILED_BATCH_IS_LOCKED_BY_USER), DialogIcon.INFO);
		} else if (isDelete) {
			showConfirmationDialog(isDelete, batchInstanceDTO.getBatchIdentifier());
		} else {

		}
	}

	private void showConfirmationDialog(final boolean isDelete, final String identifier) {
		// Confirmation Dialog
		// presenter.onRestartBatchButtonClicked(identifier, restartOptions.getItemText(restartOptions.getSelectedIndex()));
	}

}
