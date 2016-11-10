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

package com.ephesoft.gxt.admin.client.presenter.batchclass;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.BatchClassManagementMenuItemEnableEvent;
import com.ephesoft.gxt.admin.client.event.CopyBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.DeleteMultipleBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.ExportBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.GenerateKeyBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.OpenBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.SetSelectedBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.UnlockMultipleBatchClassEvent;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.batchclass.BatchClassMenuView;
import com.ephesoft.gxt.admin.client.view.batchclass.CopyBatchClassView;
import com.ephesoft.gxt.admin.client.view.batchclass.ExportBatchClassView;
import com.ephesoft.gxt.admin.client.view.batchclass.KeyGeneratorView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class BatchClassMenuPresenter extends BatchClassInlinePresenter<BatchClassMenuView> {

	interface CustomEventBinder extends EventBinder<BatchClassMenuPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public BatchClassMenuPresenter(BatchClassManagementController controller, BatchClassMenuView view) {
		super(controller, view);
		isSuperAdmin();
	}

	private DialogWindow keyGeneratorDialogWindow;

	private void isSuperAdmin() {
		rpcService.isSuperAdmin(new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Boolean isSuperAdmin) {
				if (!isSuperAdmin) {
					view.setButtonsEnableAttributeForSuperAdmin(isSuperAdmin);
				}
			}
		});

	}

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void exportBatchClass() {
		SetSelectedBatchClassEvent selectedBatchClassEvent = new SetSelectedBatchClassEvent();
		selectedBatchClassEvent.setExportBatchClass(true);
		BatchClassManagementEventBus.fireEvent(selectedBatchClassEvent);
	}

	/**
	 * Export batch class.
	 * 
	 * @param exportBatchClassEvent the export batch class event
	 */
	@EventHandler
	public void exportBatchClass(ExportBatchClassEvent exportBatchClassEvent) {
		final BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
		if (null != exportBatchClassEvent && null != batchClassDTO) {
			final DialogWindow dialogWindow = new DialogWindow(true, null);
			WidgetUtil.setID(dialogWindow, "exportBatchClass_view");
			final ExportBatchClassView exportBatchClassView = new ExportBatchClassView();
			final ExportBatchClassPresenter<ExportBatchClassView> exportBatchClassPresenter = new ExportBatchClassPresenter<ExportBatchClassView>(
					controller, exportBatchClassView);
			exportBatchClassView.setDialogBox(dialogWindow);
			exportBatchClassView.setExportBatchClassIdentifier(batchClassDTO.getIdentifier());
			dialogWindow.setFocusWidget(exportBatchClassView.getImportLearnedFilesRadio());
			exportBatchClassPresenter.bind();
			dialogWindow.center();
			dialogWindow.setDialogListener(new DialogAdapter() {

				@Override
				public void onOkClick() {
					final FormPanel exportPanel = new FormPanel();

					exportBatchClassView.getDialogBox().hide();
					view.setExportFormPanel(exportPanel);
					view.addFormPanelEvents(exportBatchClassView.getImportLearnedFilesRadio().getValue(),
							batchClassDTO.getIdentifier());
					exportPanel.submit();
				}

				@Override
				public void onCloseClick() {
					dialogWindow.hide();
				}

				@Override
				public void onCancelClick() {
					dialogWindow.hide();
				}
			});
			exportBatchClassPresenter.showBatchClassExportView();

		} else {
			Message.display(LocaleDictionary.getLocaleDictionary().getConstantValue(BatchClassConstants.BATCH_CLASS_EXPORT),
					LocaleDictionary.getLocaleDictionary().getMessageValue(BatchClassMessages.NO_BATCH_CLASS_SELECTED_FOR_PROCESSING));
		}
	}

	public void openBatchClass() {
		SetSelectedBatchClassEvent selectedBatchClassEvent = new SetSelectedBatchClassEvent();
		selectedBatchClassEvent.setOpenBatchClass(true);
		BatchClassManagementEventBus.fireEvent(selectedBatchClassEvent);
	}

	/**
	 * Opne batch class.
	 * 
	 * @param openBatchClassEvent the open batch class event
	 */
	@EventHandler
	public void openBatchClass(OpenBatchClassEvent openBatchClassEvent) {
		BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
		if (null != batchClassDTO) {
			BatchClassCompositePresenter<?, ?> currentBindedPresenter = controller.getCurrentBindedPresenter();
			if (null != currentBindedPresenter) {
				BatchClassCompositePresenter<?, ?> childPresenter = currentBindedPresenter.getChildPresenter();
				if (null != childPresenter) {
					childPresenter.layout(null);
				}
			}
		} else {
			DialogUtil.showMessageDialog(LocaleDictionary.getLocaleDictionary().getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getLocaleDictionary().getMessageValue(BatchClassMessages.BATCH_CLASS_OPEN_MESSAGE),
					DialogIcon.ERROR);
		}
	}

	public void deleteBatchClass() {
		controller.getEventBus().fireEvent(new DeleteMultipleBatchClassEvent());
	}

	public void copyBatchClass() {
		SetSelectedBatchClassEvent selectedBatchClassEvent = new SetSelectedBatchClassEvent();
		selectedBatchClassEvent.setCopyBatchClass(true);
		BatchClassManagementEventBus.fireEvent(selectedBatchClassEvent);
	}

	/**
	 * Copy batch class.
	 * 
	 * @param copyBatchClassEvent the copy batch class event
	 */
	@EventHandler
	public void copyBatchClass(CopyBatchClassEvent copyBatchClassEvent) {
		BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
		if (null != copyBatchClassEvent && null != batchClassDTO) {
			final DialogWindow dialogWindow = new DialogWindow(true, null);
			dialogWindow.setHideOnButtonClick(false);
			CopyBatchClassView copyBatchClassView = controller.getCopyBatchClassView();
			final CopyBatchClassPresenter<CopyBatchClassView> copyBatchClassPresenter = controller.getCopyBatchClassPresenter();
			copyBatchClassPresenter.setBatchClassDTO(batchClassDTO);
			copyBatchClassView.setDialogWindow(dialogWindow);
			copyBatchClassPresenter.bind();
			copyBatchClassView.clearInvalids();
			copyBatchClassPresenter.showBatchClassCopyView();
			WidgetUtil.setID(dialogWindow, "copyBatchClass_view");
			dialogWindow.setStyleName("copyBatchClassDialogWindow");
			dialogWindow.center();
			dialogWindow.setDialogListener(new DialogAdapter() {

				@Override
				public void onOkClick() {
					copyBatchClassPresenter.onOkClicked();
				}

				@Override
				public void onCloseClick() {
					dialogWindow.hide();
				}

				@Override
				public void onCancelClick() {
					dialogWindow.hide();
				}
			});
		} else {
			Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.COPY_BATCH_CLASS_LABEL),
					LocaleDictionary.getMessageValue(BatchClassMessages.NO_BATCH_CLASS_SELECTED_FOR_PROCESSING));
		}
	}

	public void unlockBatchClass() {
		controller.getEventBus().fireEvent(new UnlockMultipleBatchClassEvent());
	}

	public void keyGenerateBatchClass(){
		SetSelectedBatchClassEvent selectedBatchClassEvent = new SetSelectedBatchClassEvent();
		selectedBatchClassEvent.setGenerateKey(true);;
		BatchClassManagementEventBus.fireEvent(selectedBatchClassEvent);
	}
	
	@EventHandler
	public void keyGenerateBatchClass(GenerateKeyBatchClassEvent event) {
		final BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
		if (null != batchClassDTO) {

			controller.getRpcService().isBtachesInRunningOrErrorState(batchClassDTO.getIdentifier(), new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					// issue getting the list of batches for batch class.
				}

				@Override
				public void onSuccess(Boolean result) {
					if (Boolean.TRUE.equals(result)) {
						// error message if there are batches in running state...
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_IN_NON_FINISHED_STATE), DialogIcon.ERROR);
					} else {
						controller.getRpcService().getCurrentBatchClassEncryptionAlgo(batchClassDTO.getIdentifier(),
								new AsyncCallback<String>() {

									@Override
									public void onFailure(Throwable caught) {
									}

									@Override
									public void onSuccess(String result) {
										keyGeneratorDialogWindow = new DialogWindow(true, null);
										keyGeneratorDialogWindow.addStyleName("keyGeneratorDialogWindow");
										WidgetUtil.setID(keyGeneratorDialogWindow, "keyGeneratorBatchClass_view");
										final KeyGeneratorView keyGeneratorView = controller.getKeyGeneratorView();
										KeyGeneratorPresenter<KeyGeneratorView> keyGeneratorPresenter = controller
												.getKeyGeneratorPresenter();
										keyGeneratorView.clearBatchClassKeyText();
										keyGeneratorView.setDisabledKeyTextBox();
										keyGeneratorView.setDialogWindow(keyGeneratorDialogWindow);
										keyGeneratorView.setBatchClassIdentifier(batchClassDTO.getIdentifier());
										keyGeneratorDialogWindow.setFocusWidget(keyGeneratorView.getEncryptionAlgo());
										if (AdminConstants.ENCRYPTION_ALGO_AES_128.equals(result)) {
											keyGeneratorView.setSelectedEncryptionValue(1);
										} else if (AdminConstants.ENCRYPTION_ALGO_AES_256.equals(result)) {
											keyGeneratorView.setSelectedEncryptionValue(2);
										} else {
											keyGeneratorView.setSelectedEncryptionValue(0);
										}
										keyGeneratorPresenter.showKeyGeneratorView();
										keyGeneratorDialogWindow.center();
//										keyGeneratorView.getBatchClassKeyTextBox().focus();
										keyGeneratorDialogWindow.setDialogListener(new DialogAdapter() {

											@Override
											public void onOkClick() {
												keyGeneratorView.onGenerateButtonClick();
											}

											@Override
											public void onCloseClick() {
												keyGeneratorDialogWindow.hide();
											}

											@Override
											public void onCancelClick() {
												keyGeneratorDialogWindow.hide();
											}
										});
									}
								});
					}

				}
			});

		} else {
			Message.display(LocaleDictionary.getLocaleDictionary().getConstantValue(BatchClassConstants.KEY_GENERATOR_LABEL),
					LocaleDictionary.getLocaleDictionary().getMessageValue(BatchClassMessages.NO_BATCH_CLASS_SELECTED_FOR_PROCESSING));
		}
	}
	
	@EventHandler
	public void enableMenuItems(BatchClassManagementMenuItemEnableEvent event) {
			view.enableRequiredMenuItem(event.isEnable());
	}
}
