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

package com.ephesoft.gxt.admin.client.presenter.document.testextraction;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.ClearTestExtractionGridEvent;
import com.ephesoft.gxt.admin.client.event.ClearTestExtractionTreeEvent;
import com.ephesoft.gxt.admin.client.event.OnTestExtractionEvent;
import com.ephesoft.gxt.admin.client.event.PopulateExtractionModulesListEvent;
import com.ephesoft.gxt.admin.client.event.TestExtractionClearMenuItemEnableEvent;
import com.ephesoft.gxt.admin.client.event.TreeCreationEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.view.document.testextraction.TestExtractionClassificationTypeView;
import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class TestExtractionClassificationTypePresenter extends
		BasePresenter<BatchClassManagementController, TestExtractionClassificationTypeView> {

	interface CustomEventBinder extends EventBinder<TestExtractionClassificationTypePresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public TestExtractionClassificationTypePresenter(BatchClassManagementController controller,
			TestExtractionClassificationTypeView view) {
		super(controller, view);
	}

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void populateExtractionModules(PopulateExtractionModulesListEvent populateEvent) {
		view.clearExtractionModuleList();
		populateExtractionModulesList(view.getExtractionModulesList());
	}

	public void populateExtractionModulesList(ListBox extractionModulesList) {
		List<String> extractionModules = controller.getBatchClassExtractionModules();
		if (!CollectionUtil.isEmpty(extractionModules)) {
			for (String module : extractionModules) {
				extractionModulesList.addItem(module);
			}
			view.setExtractionModulesList(extractionModulesList);
		}
	}

	@EventHandler
	public void onTestExtraction(OnTestExtractionEvent testExtraction) {
		BatchClassManagementEventBus.fireEvent(new ClearTestExtractionGridEvent());
		BatchClassManagementEventBus.fireEvent(new ClearTestExtractionTreeEvent());
		String classificationType = view.getClassificationType();
		List<String> selectedExtractionModules = getSelectedExtractionModules(view.getExtractionModulesList());
		String batchClassID = controller.getSelectedBatchClass().getIdentifier();
		if (!CollectionUtil.isEmpty(selectedExtractionModules) && !StringUtil.isNullOrEmpty(classificationType)
				&& !StringUtil.isNullOrEmpty(batchClassID)) {
			getBatchXML(batchClassID, classificationType, selectedExtractionModules);
		} else if (CollectionUtil.isEmpty(selectedExtractionModules)) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.NO_EXTRACTION_MODULE_SELECTED), DialogIcon.ERROR);
		}
	}

	private void getBatchXML(String batchClassID, String classificationType, List<String> selectedExtractionModules) {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().testExtraction(batchClassID, classificationType, selectedExtractionModules,
				new AsyncCallback<Batch>() {

					@Override
					public void onFailure(Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						String errorMsg = caught.getMessage();
						if (!StringUtil.isNullOrEmpty(errorMsg)) {
							if (errorMsg.equals(BatchClassConstants.ERROR_BC_FOLDER_NOT_EXIST)) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassConstants.ERROR_BC_FOLDER_NOT_EXIST),
										DialogIcon.ERROR);
							} else if (errorMsg.equals(BatchClassConstants.ERROR_TEST_EXTRACTION_FOLDER_NOT_EXIST)) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										BatchClassConstants.ERROR_TEST_EXTRACTION_FOLDER_NOT_EXIST, DialogIcon.ERROR);
							} else if (errorMsg.equals(BatchClassConstants.ERROR_IMPROPER_INPUT_FILES)) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassConstants.ERROR_IMPROPER_INPUT_FILES),
										DialogIcon.ERROR);
							} else {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_WHILE_PERFORMING_EXTRACTION),
										DialogIcon.ERROR);
							}
						}
						BatchClassManagementEventBus.fireEvent(new TestExtractionClearMenuItemEnableEvent());
					}

					@Override
					public void onSuccess(Batch batch) {
						ScreenMaskUtility.unmaskScreen();
						controller.setReceivedBatch(batch);
						BatchClassManagementEventBus.fireEvent(new TestExtractionClearMenuItemEnableEvent());
						BatchClassManagementEventBus.fireEvent(new TreeCreationEvent());
					}
				});
	}

	private List<String> getSelectedExtractionModules(ListBox extractionModulesList) {
		List<String> selectedItems = new ArrayList<String>();
		int itemsCount = view.getModuleListItemsCount();
		for (int i = 0; i < itemsCount; i++) {
			if (view.isItemSelected(i)) {
				selectedItems.add(view.getSelectedItem(i));
			}
		}
		return selectedItems;
	}

}
