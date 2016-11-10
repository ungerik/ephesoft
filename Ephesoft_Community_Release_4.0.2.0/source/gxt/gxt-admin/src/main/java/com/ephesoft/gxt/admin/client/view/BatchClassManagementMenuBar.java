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

package com.ephesoft.gxt.admin.client.view;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.CloseBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.OpenChildViewEvent;
import com.ephesoft.gxt.admin.client.event.UpdateBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.ValidateNDeployWorkFlow;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public class BatchClassManagementMenuBar extends CustomMenuBar {

	private CustomMenuItem applyMenuItem;

	private CustomMenuItem openMenuItem;

	private CustomMenuItem validateAndDeployAsNormalMenuItem;
	
	private CustomMenuItem closeMenuItem;

	public BatchClassManagementMenuBar() {
		super();
		this.addStyleName("defaultMenuBar");
		openMenuItem = new CustomMenuItem(LocaleDictionary.getConstantValue(BatchClassConstants.OPEN_BATCH_CLASS_MENU),
				new ScheduledCommand() {

					@Override
					public void execute() {
						ScreenMaskUtility.maskScreen();
						BatchClassManagementEventBus.fireEvent(new OpenChildViewEvent(true));
					}
				});
		this.addItem(openMenuItem);

		applyMenuItem = new CustomMenuItem(LocaleDictionary.getConstantValue(BatchClassConstants.LABEL_APPLY_BUTTON),
				new ScheduledCommand() {

					@Override
					public void execute() {
						ScreenMaskUtility.maskScreen();
						BatchClassManagementEventBus.fireEvent(new UpdateBatchClassEvent());
					}
				});
		this.addItem(applyMenuItem);
		closeMenuItem = new CustomMenuItem(LocaleDictionary.getConstantValue(BatchClassConstants.LABEL_CLOSE_BUTTON),
				new ScheduledCommand() {

					@Override
					public void execute() {
						ScreenMaskUtility.maskScreen();
						BatchClassManagementEventBus.fireEvent(new CloseBatchClassEvent());
					}
				});
		this.addItem(closeMenuItem);
		validateAndDeployAsNormalMenuItem = new CustomMenuItem(
				LocaleDictionary.getConstantValue(BatchClassConstants.VALIDATE_DEPLOY_AS_NORMAL), new ScheduledCommand() {

					@Override
					public void execute() {
						ScreenMaskUtility.maskScreen();
						BatchClassManagementEventBus.fireEvent(new ValidateNDeployWorkFlow(false));
					}
				});
		this.addItem(validateAndDeployAsNormalMenuItem);
		this.setFocusOnHoverEnabled(false);
		WidgetUtil.setID(applyMenuItem, "applyMenuItem");
		WidgetUtil.setID(openMenuItem, "openMenuItem");
		WidgetUtil.setID(closeMenuItem, "closeMenuItem");
		WidgetUtil.setID(validateAndDeployAsNormalMenuItem, "validateAndDeployAsNormalMenuItem");
		this.setFocusOnHoverEnabled(false);
	}

	public void removeOpenMenuItem() {
		this.removeItem(openMenuItem);
	}
	
	public void removeCloseMenuItem() {
		this.removeItem(closeMenuItem);
	}

	public void removeValidateDeployMenuItem() {
		this.removeItem(validateAndDeployAsNormalMenuItem);
	}

	public void removeApplyMenuItem() {
		this.removeItem(applyMenuItem);
	}

	public void enableOpenMenuItem(boolean isEnable) {
		this.openMenuItem.setEnabled(isEnable);
	}

	public void enableApplyMenuItem(boolean isEnable) {
		this.applyMenuItem.setEnabled(isEnable);
	}
	
	public void enableCloseMenuItem(boolean isEnable) {
		this.closeMenuItem.setEnabled(isEnable);
	}

	public void enableWorkflowDeployMenuItem(boolean isEnable) {
		this.validateAndDeployAsNormalMenuItem.setEnabled(isEnable);
	}
}
