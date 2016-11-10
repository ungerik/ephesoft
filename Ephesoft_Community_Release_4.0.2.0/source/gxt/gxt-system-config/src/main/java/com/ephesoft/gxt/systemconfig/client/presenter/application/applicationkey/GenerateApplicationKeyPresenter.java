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

package com.ephesoft.gxt.systemconfig.client.presenter.application.applicationkey;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.DisableGenerateKeyEvent;
import com.ephesoft.gxt.systemconfig.client.event.KeyGenerationEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigInlinePresenter;
import com.ephesoft.gxt.systemconfig.client.view.application.applicationkey.GenerateApplicationKeyView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class GenerateApplicationKeyPresenter extends SystemConfigInlinePresenter<GenerateApplicationKeyView> {

	interface CustomEventBinder extends EventBinder<GenerateApplicationKeyPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public GenerateApplicationKeyPresenter(SystemConfigController controller, GenerateApplicationKeyView view) {
		super(controller, view);
		initializeApplicationKeyGeneratorView();
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void onBeforeKeyGeneration(KeyGenerationEvent event) {
		if (event.generateKey()) {
			String userEnteredKey = view.getUserEnteredKey();
			if (StringUtil.isNullOrEmpty(userEnteredKey)) {
				final String applicationKeyBlank = LocaleDictionary.getMessageValue(SystemConfigMessages.APPLICATION_KEY_BLANK);
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
						applicationKeyBlank, DialogIcon.ERROR);
			} else {
				generateApplicationKey(userEnteredKey);
			}
		}
	}

	private void generateApplicationKey(final String userEnteredKey) {
		final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
				LocaleDictionary.getConstantValue(SystemConfigConstants.WARNING),
				LocaleDictionary.getMessageValue(SystemConfigMessages.KEY_GENERATION_CONFIRMATION), false, DialogIcon.WARNING);
		confirmationDialog.setDialogListener(new DialogAdapter() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				ScreenMaskUtility.maskScreen();
				controller.getRpcService().generateApplicationLevelKey(userEnteredKey, new AsyncCallback<Boolean>() {

					@Override
					public void onSuccess(Boolean result) {
						final String applicationKeySuccessfullyGenerated = LocaleDictionary
								.getMessageValue(SystemConfigMessages.APPLICATION_KEY_SUCCESSFULLY_GENERATED);
						Message.display(LocaleDictionary.getConstantValue(SystemConfigConstants.SUCCESS_TITLE),
								applicationKeySuccessfullyGenerated);
						view.disableKeyGeneratorView();
						ScreenMaskUtility.unmaskScreen();
					}

					@Override
					public void onFailure(Throwable caught) {
						final String applicationKeyGenerationError = LocaleDictionary
								.getMessageValue(SystemConfigMessages.KEY_GENERATION_ERROR_MESSAGE);
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
								applicationKeyGenerationError, DialogIcon.ERROR);
						ScreenMaskUtility.unmaskScreen();
					}
				});
			}

			@Override
			public void onCloseClick() {
				confirmationDialog.hide();
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});

	}

	public void disableGenerateKeyMenuItem(boolean disable) {
		controller.getEventBus().fireEvent(new DisableGenerateKeyEvent(disable));
	}

	private void initializeApplicationKeyGeneratorView() {
		this.controller.getRpcService().isApplicationKeyGenerated(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// view.disableKeyGeneratorView();
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result == Boolean.TRUE) {
					view.disableKeyGeneratorView();
				}
			}

		});
	}
}
