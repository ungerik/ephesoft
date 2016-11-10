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

package com.ephesoft.gxt.systemconfig.client.presenter.workflowmanagement;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.ReloadPluginsTreeViewEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigInlinePresenter;
import com.ephesoft.gxt.systemconfig.client.view.workflowmanagement.ImportPluginView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;

public class ImportPluginPresenter extends SystemConfigInlinePresenter<ImportPluginView> {

	interface CustomEventBinder extends EventBinder<ImportPluginPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Constructor.
	 * 
	 * @param controller {@link UploadBatchController}
	 * @param view {@link UploadFilePanelView}
	 */
	public ImportPluginPresenter(final SystemConfigController controller, final ImportPluginView view) {
		super(controller, view);
	}

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);

	}

	public void onSaveClicked(final String pluginName, final String xmlSourcePath, final String jarSourcePath) {
		controller.getRpcService().addNewPlugin(pluginName, xmlSourcePath, jarSourcePath, new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean isPluginAdded) {
				Message.display(LocaleDictionary.getConstantValue(SystemConfigConstants.SUCCESS_TITLE), LocaleDictionary.getMessageValue(SystemConfigMessages.PLUGIN_ADDED_SUCCESSFULLY));
				controller.getEventBus().fireEvent(new ReloadPluginsTreeViewEvent());
			}

			@Override
			public void onFailure(Throwable message) {
				ScreenMaskUtility.unmaskScreen();
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE), StringUtil
						.concatenate(LocaleDictionary.getMessageValue(SystemConfigMessages.ERROR_ADDING_NEW_PLUGIN), " : ",
								message.getMessage()), DialogIcon.ERROR);
			}
		});

	}
}
