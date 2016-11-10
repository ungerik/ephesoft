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

package com.ephesoft.gxt.admin.client.view.batchclass;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.ApplyBatchClassChangesEvent;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.batchclass.BatchClassMenuPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.view.BatchClassManagementMenuBar;
import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.constant.CssConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class BatchClassMenuView extends BatchClassInlineView<BatchClassMenuPresenter> {

	private static final String IDENTIFIER = "identifier";

	private static final String GXT_ADMIN_EXPORT_BATCH_CLASS_DOWNLOAD = "gxt-admin/exportBatchClassDownload?";

	private static final String EXPORT_LEARNING = "exportLearning=";

	interface Binder extends UiBinder<Widget, BatchClassMenuView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected VerticalPanel gridViewMainPanel;

	@UiField
	protected BatchClassManagementMenuBar batchClassMenuBar;

	protected CustomMenuItem exportMenuItem;

	protected CustomMenuItem copyMenuItem;

	protected CustomMenuItem deleteMenuItem;

	protected CustomMenuItem unlockMenuItem;

	protected CustomMenuItem keyGenMenuItem;

	protected CustomMenuItem applyMenuItem;

	protected FormPanel exportFormPanel;

	/**
	 * @return the exportPresenter
	 */
	public FormPanel getExportFormPanel() {
		return exportFormPanel;
	}

	/**
	 * @param exportPresenter the exportPresenter to set
	 */
	public void setExportFormPanel(FormPanel exportFormPanel) {
		this.exportFormPanel = exportFormPanel;
	}

	public BatchClassMenuView() {
		super();
		initWidget(binder.createAndBindUi(this));
		intializeMenuItems();

		batchClassMenuBar.removeApplyMenuItem();
		batchClassMenuBar.removeValidateDeployMenuItem();
		batchClassMenuBar.removeCloseMenuItem();
		batchClassMenuBar.addItem(applyMenuItem);
		batchClassMenuBar.addItem(exportMenuItem);
		batchClassMenuBar.addItem(copyMenuItem);
		batchClassMenuBar.addItem(deleteMenuItem);

		batchClassMenuBar.addItem(unlockMenuItem);
		if (EphesoftUIContext.isWindows()) {
			batchClassMenuBar.addItem(keyGenMenuItem);
		}
		addMenuItemActionEvents();
		gridViewMainPanel.addStyleName(CssConstants.CSS_GRID_VIEW_MAIN_PANEL);
		WidgetUtil.setID(batchClassMenuBar, "batchClassMenuBar");
	}

	public void addFormPanelEvents(final boolean importLearned, final String identifier) {
		if (null != exportFormPanel) {
			gridViewMainPanel.add(exportFormPanel);
		}
		exportFormPanel.setMethod(FormPanel.METHOD_POST);

		exportFormPanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {
				StringBuilder action = new StringBuilder(GXT_ADMIN_EXPORT_BATCH_CLASS_DOWNLOAD);
				action.append(EXPORT_LEARNING);
				action.append(importLearned);
				exportFormPanel.setAction(action.toString());
				final Hidden exportIdentifierHidden = new Hidden(IDENTIFIER);
				exportIdentifierHidden.setValue(identifier);
				exportFormPanel.add(exportIdentifierHidden);
			}
		});

		exportFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				if (event.getResults().toLowerCase().indexOf(AdminConstants.ERROR_CODE_TEXT) > -1) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.EXPORT_UNSUCCESSFUL), DialogIcon.ERROR);
					return;
				}
			}
		});
	}

	private void addMenuItemActionEvents() {
		exportMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.exportBatchClass();
			}
		});

		applyMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				BatchClassManagementEventBus.fireEvent(new ApplyBatchClassChangesEvent());
			}
		});

		deleteMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.deleteBatchClass();
			}
		});

		copyMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.copyBatchClass();
			}
		});

		unlockMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.unlockBatchClass();
			}
		});

		keyGenMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.keyGenerateBatchClass();
			}
		});
	}

	/**
	 * 
	 */
	@SuppressWarnings("serial")
	private void intializeMenuItems() {
		exportMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.EXPORT_BATCH_CLASS);
			}
		});

		deleteMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.DELETE_BATCH_CLASS);
			}
		});

		copyMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.COPY_BATCH_CLASS);
			}
		});

		unlockMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.UNLOCK_BATCH_CLASS_MENU);
			}
		});
		applyMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.APPLY);
			}
		});

		keyGenMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.KEY_GENERATOR);
			}
		});
	}

	/**
	 * This method sets buttons that are disable for non super-admin.
	 * 
	 * @param disable {link: Boolean} true/false.
	 */
	public void setButtonsEnableAttributeForSuperAdmin(Boolean disable) {
		if (null != disable) {
			copyMenuItem.setEnabled(disable);
			keyGenMenuItem.setEnabled(disable);

			// Disabled these buttons for non-admin users.
			unlockMenuItem.setEnabled(disable);
			deleteMenuItem.setEnabled(disable);
		}
	}

	@Override
	public void initialize() {
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public void enableRequiredMenuItem(boolean enable) {
		exportMenuItem.setEnabled(enable);
		batchClassMenuBar.enableOpenMenuItem(enable);
		batchClassMenuBar.enableApplyMenuItem(enable);
	}

}
