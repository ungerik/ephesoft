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

package com.ephesoft.gxt.batchinstance.client.presenter;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.batchinstance.client.controller.BatchInstanceController;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceDeletedEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceOpenEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceOpenedEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceRestartedEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceSelectionEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceUnlockEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceUnlockedEvent;
import com.ephesoft.gxt.batchinstance.client.event.GetSelectedBatchInstanceEvent;
import com.ephesoft.gxt.batchinstance.client.event.RestartCurrentBatchesEvent;
import com.ephesoft.gxt.batchinstance.client.event.RestartFirstBatchesEvent;
import com.ephesoft.gxt.batchinstance.client.event.RestartPreviousBatchesEvent;
import com.ephesoft.gxt.batchinstance.client.event.RestartSelectedBatchesEvent;
import com.ephesoft.gxt.batchinstance.client.event.StartMultipleBatchDeletionEvent;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceMessages;
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstancePresenter.Results;
import com.ephesoft.gxt.batchinstance.client.view.BatchInstanceOptionsView;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.ui.widget.window.MessageDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceRetriesDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;

public class BatchInstanceOptionsPresenter extends BatchInstanceBasePresenter<BatchInstanceOptionsView> {

	interface CustomEventBinder extends EventBinder<BatchInstanceOptionsPresenter> {
	}

	public static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public BatchInstanceOptionsPresenter(final BatchInstanceController controller, final BatchInstanceOptionsView view) {
		super(controller, view);
	}

	private static final String REVIEW_VALIDATE_HTML = "/ReviewValidate.html";
	private static final String BATCH_ID_URL = "?batch_id=";

	int unlockCounter = 0;

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void deleteSelectedbatchInstance() {
		BatchInstanceDTO selectedBatchInstance = controller.getSelectedBatchInstance();
		this.deleteBatchInstance(selectedBatchInstance);
	}

	public void openSelectedBatchInstance() {
		GetSelectedBatchInstanceEvent batchInstanceEvent = new GetSelectedBatchInstanceEvent();
		batchInstanceEvent.setOpen(true);
		controller.getEventBus().fireEvent(batchInstanceEvent);
	}

	@EventHandler
	public void openSelectedBatchInstance(BatchInstanceOpenEvent batchInstanceOpenEvent) {
		if (null != batchInstanceOpenEvent) {
			BatchInstanceDTO selectedBatchInstanceDTO = batchInstanceOpenEvent.getBatchInstanceDTO();
			if (null != selectedBatchInstanceDTO) {
				this.openBatchInstance(selectedBatchInstanceDTO);
			}
		}
	}

	public void unlockBatch() {
		GetSelectedBatchInstanceEvent batchInstanceEvent = new GetSelectedBatchInstanceEvent();
		batchInstanceEvent.setUnlock(true);
		controller.getEventBus().fireEvent(batchInstanceEvent);
	}

	@EventHandler
	public void unlockBatches(BatchInstanceUnlockEvent unlockEvent) {
		if (null != unlockEvent) {
			List<BatchInstanceDTO> selectedBatches = unlockEvent.getSelectedBatches();
			if (!CollectionUtil.isEmpty(selectedBatches)) {
				// Modified Code to Reload Grid only once.
				ListIterator<BatchInstanceDTO> selectedBatch = selectedBatches.listIterator();
				boolean isReloadGrid = false;
				int totalBatches=selectedBatches.size();
				setUnlockCounter(totalBatches);
				while (selectedBatch.hasNext()) {
					BatchInstanceDTO selectedInstance=(BatchInstanceDTO) selectedBatch.next();
					if (!selectedBatch.hasNext()) {
						isReloadGrid = true;
					}
					this.unlockBatch(selectedInstance, isReloadGrid);
				}
				
				//Single Message display for all batches unlocked
				if(getUnlockCounter() == totalBatches){
					Message.display(LocaleDictionary.getConstantValue(LocaleCommonConstants.SUCCESS_TITLE), LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_UNLOCKED_SUCCESSFULLY));
				}else if(getUnlockCounter() == 0){
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE), LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_UNLOCKED_FAILED), DialogIcon.ERROR);
				}else if(getUnlockCounter()>0){
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE), LocaleDictionary.getMessageValue(BatchInstanceMessages.SOME_BATCH_UNLOCKED_FAILED), DialogIcon.ERROR);
				}
				
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.NO_RECORD_SELECTED_TO_UNLOCK), DialogIcon.WARNING);
			}
		}
	}

	public void unlockBatch(final BatchInstanceDTO batchInstanceDTO, final boolean isReloadGrid) {
		if (batchInstanceDTO != null) {
			final String batchInstanceIdentifier = batchInstanceDTO.getBatchIdentifier();
			rpcService.clearCurrentUser(batchInstanceIdentifier, new AsyncCallback<Void>() {

				@Override
				public void onFailure(Throwable caught) {
					setUnlockCounter(getUnlockCounter()-1);
				}

				@Override
				public void onSuccess(Void result) {
					if (isReloadGrid) {
						controller.getEventBus().fireEvent(new BatchInstanceUnlockedEvent(null));
					}
				}
			});
		}
	}

	public void openBatchInstance(final BatchInstanceDTO batchInstanceDTO) {
		if (batchInstanceDTO.getStatus().equals(BatchInstanceStatus.READY_FOR_REVIEW.name())
				|| batchInstanceDTO.getStatus().equals(BatchInstanceStatus.READY_FOR_VALIDATION.name())) {
			final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
					LocaleDictionary.getConstantValue(BatchInstanceConstants.CONFIRMATION),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.MESSAGE_NAVIGATE_TO_BATCH_DETAIL_SCREEN));
			confirmationDialog.setDialogListener(new DialogAdapter() {

				@Override
				public void onOkClick() {
					// TODO Auto-generated method stub
					super.onOkClick();
					if (batchInstanceDTO != null) {
						final String batchInstanceIdentifier = batchInstanceDTO.getBatchIdentifier();
						rpcService.acquireLock(batchInstanceIdentifier, new AsyncCallback<Void>() {

							@Override
							public void onFailure(final Throwable caught) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(LocaleCommonConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_IS_LOCKED));
							}

							@Override
							public void onSuccess(final Void result) {
								redirectToBatchDetailPage(batchInstanceIdentifier);
								controller.getEventBus().fireEvent(new BatchInstanceOpenedEvent(batchInstanceDTO));
							}

						});
					}
				}

				@Override
				public void onCancelClick() {
					// TODO Auto-generated method stub
					super.onCancelClick();
					confirmationDialog.collapse();
				}
			});
		} else {
			final MessageDialog messageDialog = new MessageDialog(LocaleDictionary.getConstantValue(LocaleCommonConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_IS_NOT_READY_FOR_REVIEW_VALIDATION), DialogIcon.ERROR);
			messageDialog.setPredefinedButtons(PredefinedButton.OK);
			messageDialog.show();
			messageDialog.center();
		}
	}

	public void redirectToBatchDetailPage(String batchInstanceIdentifier) {
		if (batchInstanceIdentifier != null && !batchInstanceIdentifier.isEmpty()) {
			String href = Window.Location.getHref();
			String baseUrl = href.substring(0, href.lastIndexOf('/'));
			StringBuffer newUrl = new StringBuffer();
			newUrl.append(baseUrl).append(REVIEW_VALIDATE_HTML);
			newUrl.append(BATCH_ID_URL).append(batchInstanceIdentifier);
			Window.open(newUrl.toString(), "_blank", BatchInstanceConstants.EMPTY_STRING);
		}
	}

	public void deleteBatchInstance(final BatchInstanceDTO batchInstance) {
		if (null != batchInstance && this.canProcess(batchInstance)) {
			final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.CONFIRMATION),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.DELETE_SELECTED_BATCHES));
			confirmationDialog.setDialogListener(new DialogAdapter() {

				@Override
				public void onOkClick() {
					// TODO Auto-generated method stub
					final String batchInstanceIdentifier = batchInstance.getBatchIdentifier();
					rpcService.deleteBatchInstance(batchInstanceIdentifier, new AsyncCallback<BatchInstancePresenter.Results>() {

						@Override
						public void onSuccess(final Results result) {
							if (result == Results.SUCCESSFUL) {
								Message.display(LocaleDictionary.getConstantValue(LocaleCommonConstants.SUCCESS_TITLE), LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_DELETED_SUCCESSFULLY));
								controller.getEventBus().fireEvent(new BatchInstanceDeletedEvent(batchInstance));
							} else {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE), LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_COULD_NOT_BE_DELETED),
										DialogIcon.ERROR);
							}
						}

						@Override
						public void onFailure(final Throwable caught) {
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE), LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_COULD_NOT_BE_DELETED),
									DialogIcon.ERROR);
						}
					});
				}

				@Override
				public void onCloseClick() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onCancelClick() {
					// TODO Auto-generated method stub
					confirmationDialog.hide();
				}
			});
		} else {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCHES_NOT_IN_DELETABLE_STATE),
					DialogIcon.ERROR);
		}
	}

	@EventHandler
	public void onBatchInstanceSelection(BatchInstanceSelectionEvent batchInstanceSelectionEvent) {
		if (null != batchInstanceSelectionEvent) {
			BatchInstanceDTO selectedBatchInstance = batchInstanceSelectionEvent.getBatchInstanceDTO();
			if (null != selectedBatchInstance) {
				BatchInstanceStatus selectedBatchInstanceStatus = BatchInstanceStatus.valueOf(selectedBatchInstance.getStatus());
				String batchInstanceIdentifier = selectedBatchInstance.getBatchIdentifier();
				String currentUser = selectedBatchInstance.getCurrentUser();
				this.handleCurrentUserOptions(currentUser);
				this.handleBatchInstanceOptions(selectedBatchInstanceStatus);
				this.setRestartOptions(batchInstanceIdentifier);
			}
		}
	}

	private void handleCurrentUserOptions(String currentUser) {
		if (StringUtil.isNullOrEmpty(currentUser)) {
			// view.setEnableUnlockBatchButton(false);
			// view.setEnableRestart(true);
			// view.setEnableDeleteBatchButton(true);
		} else {
			// view.setEnableUnlockBatchButton(true);
			// view.setEnableRestart(false);
			// view.setEnableDeleteBatchButton(false);
		}
	}

	private void handleBatchInstanceOptions(BatchInstanceStatus instanceStatus) {
		if (null != instanceStatus) {
			switch (instanceStatus) {
				case READY:
				case NEW:
				case LOCKED:
				case DELETED:
				case FINISHED:
					// view.setEnableOpenBatchButton(false);
					// view.setEnableDeleteBatchButton(false);
					// view.setEnableRestart(false);
					break;
				case READY_FOR_REVIEW:
				case READY_FOR_VALIDATION:
					// view.setEnableOpenBatchButton(true);
					break;
				case RUNNING:
				case ERROR:
					// view.setEnableOpenBatchButton(false);
					// view.setEnableDeleteBatchButton(true);
					// view.setEnableRestart(true);
					break;
				default:
					break;
			}
		}
	}

	public void setRestartOptions(final String batchInstanceIdentifier) {
		rpcService.getRestartOptions(batchInstanceIdentifier, new AsyncCallback<Map<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(Map<String, String> result) {
				// view.setRestartOptions(result);
			}
		});
	}

	public void restartSelectedBatch(final String modulename) {
		controller.getEventBus().fireEvent(new RestartSelectedBatchesEvent(modulename));
	}

	public void getBatchInstanceRetriesDTO(final String batchIdentifier) {
		controller.getRpcService().getBatchInstanceRetriesDTO(batchIdentifier, new AsyncCallback<BatchInstanceRetriesDTO>() {

			@Override
			public void onFailure(Throwable paramThrowable) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(LocaleCommonConstants.ERROR_TITLE), LocaleDictionary.getMessageValue(
						BatchInstanceMessages.MSG_RESTART_FAILURE, batchIdentifier), DialogIcon.ERROR);
			}

			@Override
			public void onSuccess(BatchInstanceRetriesDTO batchInstanceRetriesDTO) {
				if (batchInstanceRetriesDTO != null && batchInstanceRetriesDTO.getRestartAllowableFlag() == 1) {
					// TODO Dialog Box
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE), LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_CANNOT_BE_RESTARTED), DialogIcon.ERROR);
				} else {
					getBatchInstanceDTO(false, batchIdentifier);
				}
			}
		});
	}

	public void getBatchInstanceDTO(final boolean isDelete, final String batchIdentifier) {
		controller.getRpcService().getBatchInstanceDTO(batchIdentifier, new AsyncCallback<BatchInstanceDTO>() {

			@Override
			public void onSuccess(BatchInstanceDTO batchInstanceDTO) {
				view.isBatchInstanceLocked(isDelete, batchInstanceDTO);
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Confirmtation Dialog
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE), LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_CANNOT_BE_RESTARTED), DialogIcon.ERROR);
			}

		});
	}

	public void onRestartBatchButtonClicked(final String identifier, final String module) {
		controller.getRpcService().updateBatchInstanceStatus(identifier, BatchInstanceStatus.RESTART_IN_PROGRESS,
				new AsyncCallback<Results>() {

					@Override
					public void onSuccess(Results arg0) {

						// TODO Restart
						controller.getRpcService().restartBatchInstance(identifier, module, new AsyncCallback<Results>() {

							@Override
							public void onSuccess(Results arg0) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.INFO_TITLE), StringUtil.concatenate(identifier
										, LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_SENT_FOR_RESTART)), DialogIcon.INFO);
								final BatchInstanceDTO batchInstanceDTO = controller.getSelectedBatchInstance();
								controller.getEventBus().fireEvent(new BatchInstanceRestartedEvent(batchInstanceDTO));
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Dialog Box
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE), LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_CANNOT_BE_RESTARTED), DialogIcon.ERROR);
							}

						});

					}

					@Override
					public void onFailure(Throwable caught) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE), LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_CANNOT_BE_RESTARTED), DialogIcon.ERROR);
					}

				});
	}

	public void deleteMultipleBatches() {
		controller.getEventBus().fireEvent(new StartMultipleBatchDeletionEvent());
	}

	public void restartCurrentBatch() {
		controller.getEventBus().fireEvent(new RestartCurrentBatchesEvent());
	}

	public void restartFirstBatch() {
		controller.getEventBus().fireEvent(new RestartFirstBatchesEvent());
	}

	public void restartPreviousBatch() {
		controller.getEventBus().fireEvent(new RestartPreviousBatchesEvent());

	}

	public int getUnlockCounter() {
		return unlockCounter;
	}

	public void setUnlockCounter(int unlockCounter) {
		this.unlockCounter = unlockCounter;
	}

}
