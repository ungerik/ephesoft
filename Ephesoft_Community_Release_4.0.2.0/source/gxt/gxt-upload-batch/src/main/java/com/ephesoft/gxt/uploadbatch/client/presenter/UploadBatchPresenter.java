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

package com.ephesoft.gxt.uploadbatch.client.presenter;

import java.util.List;

import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.BatchClassFieldDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.uploadbatch.client.controller.UploadBatchController;
import com.ephesoft.gxt.uploadbatch.client.event.ShowAssociatedBCFViewEvent;
import com.ephesoft.gxt.uploadbatch.client.event.StartBatchEvent;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchMessages;
import com.ephesoft.gxt.uploadbatch.client.view.UploadBatchView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;

public class UploadBatchPresenter extends BasePresenter<UploadBatchController, UploadBatchView> {

	interface CustomEventBinder extends EventBinder<UploadBatchPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	UploadBatchButtonPanelPresenter uploadBatchButtonPanelPresenter;

	UploadFilePanelPresenter uploadFilePanelPresenter;

	UploadBatchGridPresenter uploadBatchGridPresenter;

	AssociateBCFPresenter associateBCFPresenter;
	
	/**
	 * Constructor.
	 * 
	 * @param controller {@link UploadBatchController}
	 * @param view {@link UploadBatchView}
	 */
	public UploadBatchPresenter(final UploadBatchController controller, final UploadBatchView view) {
		super(controller, view);
		if (view != null) {
			uploadBatchButtonPanelPresenter = new UploadBatchButtonPanelPresenter(controller, view.getInputPanel());
			uploadFilePanelPresenter = new UploadFilePanelPresenter(controller, view.getUploadPanel());
			uploadBatchGridPresenter = new UploadBatchGridPresenter(controller, view.getUploadedFileGridView());
			associateBCFPresenter = new AssociateBCFPresenter(controller, view.getAssociateBCFView());
		}
	}

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void showAssociateBCFView(final ShowAssociatedBCFViewEvent showBCFViewEvent) {
		if (!controller.getBatchClassFieldDTOs().isEmpty()) {
			final DialogWindow dialogWindow = new DialogWindow(true, null);
			associateBCFPresenter.setAssociatedBCFVeiwDialogBox(dialogWindow);
			associateBCFPresenter.setBatchClassFieldDTOs(controller.getBatchClassFieldDTOs());
			associateBCFPresenter.bind();
			associateBCFPresenter.showAssociateBCFView();
			dialogWindow.setWidth("398px");
			dialogWindow.setHeight(view.getAssociateBCFView().getOffsetHeight());
			dialogWindow.center();
		}

	}

	public boolean isBCFieldsSerilized() {
		return associateBCFPresenter.isBatchClassFeildsSerialized();
	}
	
	public void setBCFieldsSerialized(boolean batchClassFeildsSerialized) {
		associateBCFPresenter.setBatchClassFeildsSerialized(batchClassFeildsSerialized);
	}
	
	@EventHandler
	public void startBatchEventFired(final StartBatchEvent startBatchEvent) {
		List<BatchClassFieldDTO> batchClassFieldDTOs = controller.getBatchClassFieldDTOs();
		if (!CollectionUtil.isEmpty(batchClassFieldDTOs)) {
			serializeBatchClassField();
		} else {
			finishBatch();
		}
	}
	
	private void serializeBatchClassField() {
		List<BatchClassFieldDTO> batchClassFieldDTOs = controller.getBatchClassFieldDTOs();
		if (!CollectionUtil.isEmpty(batchClassFieldDTOs)) {
			final String currentUploadFolder = controller.getCurrentBatchUploadFolder();
			ScreenMaskUtility.maskScreen(LocaleDictionary.getConstantValue(CoreCommonConstants.PLEASE_WAIT));
			controller.getRpcService().serializeBatchClassField(currentUploadFolder, batchClassFieldDTOs, new AsyncCallback<Void>() {

				@Override
				public void onSuccess(final Void arg0) {
					ScreenMaskUtility.unmaskScreen();
					finishBatch();
				}

				@Override
				public void onFailure(final Throwable arg0) {
					ScreenMaskUtility.unmaskScreen();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(UploadBatchMessages.UNABLE_TO_SERIALISE_BATCH_CLASS_FIELDS), DialogIcon.ERROR);

				}
			});
		}
	}
	
	private void finishBatch() {
		final String batchDescription = view.getInputPanel().getBatchDescriptionFromTextBox();
		final String selectedBatchClassId = view.getInputPanel().getSelectedBatchClassComboBoxValue();
		if (!StringUtil.isNullOrEmpty(batchDescription) && !StringUtil.isNullOrEmpty(selectedBatchClassId)) {
			final String currentBatchUploadFolder = controller.getCurrentBatchUploadFolder();
			final String batchName = view.getInputPanel().getBatchName();
			if (!StringUtil.isNullOrEmpty(batchName)) {
				ScreenMaskUtility.maskScreen(LocaleDictionary.getConstantValue(CoreCommonConstants.PLEASE_WAIT));
				controller.getRpcService().finishBatch(currentBatchUploadFolder, selectedBatchClassId, batchName, batchDescription,
						new AsyncCallback<String>() {

							@Override
							public void onFailure(final Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(UploadBatchMessages.UNABLE_TO_UPLOAD_BATCH_TRY_AGAIN), DialogIcon.ERROR);

							}

							@Override
							public void onSuccess(final String result) {
								ScreenMaskUtility.unmaskScreen();
								final String message = StringUtil.concatenate(LocaleDictionary.getConstantValue(UploadBatchConstants.BATCH), batchDescription,
										LocaleDictionary.getConstantValue(UploadBatchConstants.QUEUED_FOR_PROCESSING));
								Message.display(LocaleDictionary.getMessageValue(UploadBatchMessages.SUCCESS), message);
								uploadBatchButtonPanelPresenter.clearAllFields();
								associateBCFPresenter.setOnStartBatchClicked(false);
								associateBCFPresenter.setBatchClassFeildsSerialized(false);
							}

						});
			}
		}
	}
}
