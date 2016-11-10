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

package com.ephesoft.gxt.admin.client.presenter.indexFields;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ephesoft.dcma.core.common.ImportExportIndexField;
import com.ephesoft.gxt.admin.client.BatchClassManagementServiceAsync;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.LoadImportedIndexFieldEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.indexFields.IndexFieldImportView;
import com.ephesoft.gxt.core.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;

public class ImportIndexFieldPresenter extends BatchClassInlinePresenter<IndexFieldImportView> {

	private BatchClassManagementServiceAsync rpcService;

	interface CustomEventBinder extends EventBinder<ImportIndexFieldPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public ImportIndexFieldPresenter(BatchClassManagementController controller, IndexFieldImportView view) {
		super(controller, view);
		rpcService = controller.getRpcService();
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	/**
	 * @return the rpcService
	 */
	public BatchClassManagementServiceAsync getRpcService() {
		return rpcService;
	}

	public void importIndexField(String tempZipFileLocation) {
		DocumentTypeDTO documentTypeDTO = controller.getSelectedDocumentType();
		if (StringUtil.isNullOrEmpty(tempZipFileLocation)) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_ZIP_FILE), DialogIcon.ERROR);
		} else {
			if (null != documentTypeDTO) {
				controller.getRpcService().importIndexField(tempZipFileLocation, documentTypeDTO,
						new AsyncCallback<Map<ImportExportIndexField, FieldTypeDTO>>() {

							@Override
							public void onSuccess(Map<ImportExportIndexField, FieldTypeDTO> result) {
								ScreenMaskUtility.unmaskScreen();
								if (null != result) {
									List<FieldTypeDTO> addedIndexFieldList = new ArrayList<FieldTypeDTO>();
									for (Entry<ImportExportIndexField, FieldTypeDTO> entry : result.entrySet()) {
										if (null != entry) {
											if (null != entry.getValue()) {
												addedIndexFieldList.add(entry.getValue());
											}
										}
									}
									String message = CoreCommonConstants.EMPTY_STRING;
									if (result.size() == 1 && addedIndexFieldList.size() != 1) {
										message = LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_WHILE_IMPORTING_INDEX_FIELDS);
									} else if (addedIndexFieldList.size() != result.size()) {
										message = LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_WHILE_IMPORTING_SOME_INDEX_FIELDS);
									}
									if (null != addedIndexFieldList) {
										BatchClassManagementEventBus.fireEvent(new LoadImportedIndexFieldEvent(addedIndexFieldList, message));
									}

								}
							}

							@Override
							public void onFailure(Throwable caught) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.FAILED_TO_IMPORT_INDEX_FIELD),
										DialogIcon.ERROR);
							}
						});
			}
		}
	}
}
