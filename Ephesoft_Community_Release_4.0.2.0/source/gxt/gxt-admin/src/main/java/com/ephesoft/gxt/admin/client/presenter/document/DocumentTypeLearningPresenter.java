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

package com.ephesoft.gxt.admin.client.presenter.document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ephesoft.dcma.core.common.ImportExportDocument;
import com.ephesoft.gxt.admin.client.BatchClassManagementServiceAsync;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.BatchClassManagementMenuItemEnableEvent;
import com.ephesoft.gxt.admin.client.event.LearnFilesStartEvent;
import com.ephesoft.gxt.admin.client.event.LoadImportedDocumentEvent;
import com.ephesoft.gxt.admin.client.event.UpdateLearnFileInformation;
import com.ephesoft.gxt.admin.client.event.ViewLearnFilesEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.document.DocumentTypeLearningView;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.ui.widget.window.MessageDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class DocumentTypeLearningPresenter extends BatchClassInlinePresenter<DocumentTypeLearningView> {

	protected ViewDocLearnFileGridPresenter viewDocLearnPresenter;

	public DocumentTypeLearningPresenter(BatchClassManagementController controller, DocumentTypeLearningView view) {
		super(controller, view);
		viewDocLearnPresenter = new ViewDocLearnFileGridPresenter(controller, view.getViewLearnFileView());

	}

	interface CustomEventBinder extends EventBinder<DocumentTypeLearningPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

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

	@EventHandler
	public void handleLearnFilesEvent(LearnFilesStartEvent learnFileStartEvent) {
		if (isBatchClassDirty()) {
			view.applyBatchClassBeforeOperation(LocaleDictionary.getConstantValue(BatchClassConstants.LEARN_FILES));
		} else {
			final boolean isAnyInvalidFile = learnFileStartEvent.isAnyInvalidFile();
			if (null != learnFileStartEvent) {
				BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();

				if (batchClassDTO.hasDocumentType(true)) {
					final String batchClassID = controller.getSelectedBatchClass().getIdentifier();
					if (!StringUtil.isNullOrEmpty(batchClassID)) {
						ScreenMaskUtility.maskScreen(LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_WAIT_LEARNING_FILES));
						controller.getRpcService().learnFileForBatchClass(batchClassID, new AsyncCallback<Void>() {

							@Override
							public void onFailure(final Throwable arg0) {
								ScreenMaskUtility.unmaskScreen();
								final MessageDialog dialog = DialogUtil.showMessageDialog(
										LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary
												.getMessageValue(BatchClassMessages.LEARNING_FAILED_FOR_FEW_SAMPLE_FILES_PLEASE_REFER_APPLICATION_LOGS),
										DialogIcon.ERROR);
								dialog.setVisible(true);
							}

							@Override
							public void onSuccess(final Void arg0) {
								ScreenMaskUtility.unmaskScreen();
								if (isAnyInvalidFile) {
									view.filesNotSupportedAction(view.getFilesNotSupportedList());
								}
								Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.FILES_LEARNED_SUCCESSFULLY));
							}
						});
					} else {
						final MessageDialog dialog = DialogUtil.showMessageDialog(
								LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_GET_BATCH_CLASS_INFORMATION),
								DialogIcon.ERROR);
						dialog.setVisible(true);
					}
				} else {
					final MessageDialog dialog = DialogUtil.showMessageDialog(
							LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.NO_DOCUMENT_TYPE_EXIST), DialogIcon.ERROR);
					dialog.setVisible(true);
				}
			}
		}
	}

	@EventHandler
	public void handleViewLearnFilesGrid(ViewLearnFilesEvent viewLearnFilesEvent) {
		if (null != viewLearnFilesEvent) {
			if (viewLearnFilesEvent.isViewValid()) {
				boolean isViewLearnFile = viewLearnFilesEvent.isLearnFileViewShow();
				if (isViewLearnFile) {
					view.setViewLearnFilesView(viewLearnFilesEvent.getSelectedDocumentTypeDTO());
				} else {
					view.setDragDropView();
				}
			}

		}

	}

	public DocumentTypeDTO getDocumentType() {
		DocumentTypeDTO documentTypeDTO = controller.getSelectedDocumentType();
		return documentTypeDTO;
	}

	public String getBatchClassIdentifer() {
		String batchClassIdentifier = null;
		BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
		if (null != batchClassDTO) {
			batchClassIdentifier = batchClassDTO.getIdentifier();
		}
		return batchClassIdentifier;
	}

	public void importDocumentType(String tempZipFileLocation, boolean isLuceneLearningToImport) {
		controller.getRpcService().importDocumentType(tempZipFileLocation, true, controller.getSelectedBatchClass().getIdentifier(),
				new AsyncCallback<Map<ImportExportDocument, DocumentTypeDTO>>() {

					@Override
					public void onFailure(Throwable updateException) {
						ScreenMaskUtility.unmaskScreen();
						if (updateException.getMessage().equals(BatchClassMessages.BATCH_CLASS_UNLOCKED_BY_SUPER_USER)
								|| updateException.getMessage().equals(BatchClassMessages.BATCH_CLASS_ALREADY_DELETED)) {
							StringBuilder message = new StringBuilder();
							message.append(LocaleDictionary.getMessageValue(updateException.getMessage()));
							message.append(BatchClassConstants.SPACE);
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									message.toString(), DialogIcon.ERROR);

						} else {
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_WHILE_IMPORTING_DOCUMENT_TYPES),
									DialogIcon.ERROR);
						}
					}

					@Override
					public void onSuccess(Map<ImportExportDocument, DocumentTypeDTO> result) {
						ScreenMaskUtility.unmaskScreen();
						if (null != result) {
							final List<DocumentTypeDTO> addedDocList = new ArrayList<DocumentTypeDTO>();
							for (Entry<ImportExportDocument, DocumentTypeDTO> entry : result.entrySet()) {
								if (null != entry) {
									if (null != entry.getValue()) {
										addedDocList.add(entry.getValue());
									}
								}
							}
							if (result.size() == 1 && addedDocList.size() != 1) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_WHILE_IMPORTING_DOCUMENT_TYPES),
										DialogIcon.ERROR);
							} else if (addedDocList.size() != result.size()) {
								DialogUtil.showMessageDialog(
										LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_WHILE_IMPORTING_SOME_DOCUMENT_TYPES),
										DialogIcon.ERROR);
							}
							if (null != addedDocList) {
								controller.getRpcService().isDocTypeLimitExceed(new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										DialogUtil.showMessageDialog(LocaleDictionary
												.getConstantValue(BatchClassConstants.ERROR_TITLE), LocaleDictionary
												.getMessageValue(BatchClassMessages.ERROR_WHILE_IMPORTING_DOCUMENT_TYPES),
												DialogIcon.ERROR);
									}

									@Override
									public void onSuccess(Boolean result) {
										if (result) {
											DialogUtil.showMessageDialog(
													LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
													LocaleDictionary.getConstantValue(LocaleCommonConstants.DOC_LIMIT_MESSAGE),
													DialogIcon.INFO);
										}
										BatchClassManagementEventBus.fireEvent(new LoadImportedDocumentEvent(addedDocList));
									}

								});
							}
						}
					}
				});
	}

	public void getLearnFileInformation(DocumentTypeDTO documentTypeDTO) {
		BatchClassManagementEventBus.fireEvent(new UpdateLearnFileInformation(documentTypeDTO));
	}

	@EventHandler
	public void enableMenuItems(BatchClassManagementMenuItemEnableEvent event) {
		if (event.getPropertyAccessModel().equals(PropertyAccessModel.DOCUMENT_TYPE)) {
			view.enableLearnDropView(event.isEnable());
		}
	}

	public boolean isBatchClassDirty() {
		if (null != controller.getSelectedBatchClass()) {
			return controller.getSelectedBatchClass().isDirty();
		}
		return false;
	}

}
