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

package com.ephesoft.gxt.admin.client.presenter.document.testclassification;

import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.ClearTestClassificationGridEvent;
import com.ephesoft.gxt.admin.client.event.OnTestClassificationEvent;
import com.ephesoft.gxt.admin.client.event.ReloadTestClassificationResultGrid;
import com.ephesoft.gxt.admin.client.event.TestClassificationClearMenuItemEnableEvent;
import com.ephesoft.gxt.admin.client.event.TestClassificationDownloadEnableEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.document.testclassification.TestClassifyTypeView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.TestClassificationDataCarrierDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class TestClassifyTypePresenter extends BatchClassInlinePresenter<TestClassifyTypeView> {

	public TestClassifyTypePresenter(BatchClassManagementController controller, TestClassifyTypeView view) {
		super(controller, view);
	}

	interface CustomEventBinder extends EventBinder<TestClassifyTypePresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void onTestClassification(OnTestClassificationEvent classifyEvent) {
		String classificationType = view.getClassificationType();
		String batchClassID = controller.getSelectedBatchClass().getIdentifier();
		BatchClassManagementEventBus.fireEvent(new ClearTestClassificationGridEvent());
		if (!StringUtil.isNullOrEmpty(batchClassID) && !StringUtil.isNullOrEmpty(classificationType)) {
			getOutputDtos(batchClassID, classificationType);
		}
	}

	private void getOutputDtos(String batchClassID, String classificationType) {
		ScreenMaskUtility.maskScreen();
		rpcService.testContentClassification(batchClassID, classificationType,
				new AsyncCallback<List<TestClassificationDataCarrierDTO>>() {

					@Override
					public void onFailure(Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						String errorMsg = caught.getMessage();
						if (!StringUtil.isNullOrEmpty(errorMsg)) {
							if (errorMsg.equals(BatchClassConstants.ERROR_BC_FOLDER_NOT_EXIST)) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassConstants.ERROR_BC_FOLDER_NOT_EXIST),
										DialogIcon.ERROR);
							} else if (errorMsg.equals(BatchClassConstants.ERROR_TEST_CLASSIFICATION_FOLDER_NOT_EXIST)) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary
												.getMessageValue(BatchClassConstants.ERROR_TEST_CLASSIFICATION_FOLDER_NOT_EXIST),
										DialogIcon.ERROR);
							} else if (errorMsg.equals(BatchClassConstants.ERROR_IMPROPER_INPUT_FILES)) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassConstants.ERROR_IMPROPER_INPUT_FILES),
										DialogIcon.ERROR);
							} else if (errorMsg.equals(BatchClassConstants.ERROR_WHILE_BREAKING_TIFF_OR_PDF)) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassConstants.ERROR_WHILE_BREAKING_TIFF_OR_PDF),
										DialogIcon.ERROR);
							} else if (errorMsg.equals(BatchClassConstants.ERROR_WHILE_OCRING)) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassConstants.ERROR_WHILE_OCRING), DialogIcon.ERROR);
							} else if (errorMsg.equals(BatchClassConstants.PP_SERVICE_UNSUCCESSFUL)) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassConstants.PP_SERVICE_UNSUCCESSFUL),
										DialogIcon.ERROR);
							} else if (errorMsg.equals(BatchClassConstants.DA_SERVICE_UNSUCCESSFULL)) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassConstants.DA_SERVICE_UNSUCCESSFULL),
										DialogIcon.ERROR);
							} else {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										errorMsg, DialogIcon.ERROR);
							}
						}
						BatchClassManagementEventBus.fireEvent(new TestClassificationClearMenuItemEnableEvent());
					}

					@Override
					public void onSuccess(List<TestClassificationDataCarrierDTO> outputDtos) {
						ScreenMaskUtility.unmaskScreen();
						if (!CollectionUtil.isEmpty(outputDtos)) {
							controller.setTestClassificationResultDtos(outputDtos);
							BatchClassManagementEventBus.fireEvent(new ReloadTestClassificationResultGrid());
						}
						BatchClassManagementEventBus.fireEvent(new TestClassificationDownloadEnableEvent(true));
						BatchClassManagementEventBus.fireEvent(new TestClassificationClearMenuItemEnableEvent());
					}
				});

	}

	@Override
	public void bind() {

	}
}
