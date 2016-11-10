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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.gxt.batchinstance.client.controller.BatchInstanceController;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceDeletedEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceOpenEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceOpenedEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceRestartedEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceSelectionEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceUnlockEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceUnlockedEvent;
import com.ephesoft.gxt.batchinstance.client.event.DeleteAllBatchesEvent;
import com.ephesoft.gxt.batchinstance.client.event.GetSelectedBatchInstanceEvent;
import com.ephesoft.gxt.batchinstance.client.event.LoadBatchInstanceChartsEvent;
import com.ephesoft.gxt.batchinstance.client.event.LoadTroubleshootArtifactsEvent;
import com.ephesoft.gxt.batchinstance.client.event.RestartAllBatchesEvent;
import com.ephesoft.gxt.batchinstance.client.event.RestartCurrentBatchesEvent;
import com.ephesoft.gxt.batchinstance.client.event.RestartFirstBatchesEvent;
import com.ephesoft.gxt.batchinstance.client.event.RestartPreviousBatchesEvent;
import com.ephesoft.gxt.batchinstance.client.event.RestartSelectedBatchesEvent;
import com.ephesoft.gxt.batchinstance.client.event.StartMultipleBatchDeletionEvent;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceMessages;
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstancePresenter.Results;
import com.ephesoft.gxt.batchinstance.client.view.BatchInstanceGridView;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

public class BatchInstanceGridPresenter extends BatchInstanceBasePresenter<BatchInstanceGridView> {

	interface CustomEventBinder extends EventBinder<BatchInstanceGridPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);
	
	private List<BatchInstanceDTO> invalidBatchSelectionList;

	public BatchInstanceGridPresenter(BatchInstanceController controller, BatchInstanceGridView view) {
		super(controller, view);
		view.loadGrid();
		invalidBatchSelectionList = new ArrayList<BatchInstanceDTO>();

	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void selectBatchInstance(CellSelectionChangedEvent<BatchInstanceDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			BatchInstanceDTO selectedBatchInstance = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			if (selectedBatchInstance != null) {
				controller.setSelectedBatchInstance(selectedBatchInstance);
				controller.getEventBus().fireEvent(new BatchInstanceSelectionEvent(selectedBatchInstance));
			}
		}
	}

	@EventHandler
	public void handleBatchInstanceDeletionEvent(BatchInstanceDeletedEvent batchInstanceDeletionEvent) {
		if (null != batchInstanceDeletionEvent) {
			controller.setSelectedBatchInstance(null);
			view.reloadGrid();
		}
	}

	@EventHandler
	public void handleBatchInstanceOpenEvent(BatchInstanceOpenedEvent openBatchInstanceEvent) {
		if (null != openBatchInstanceEvent) {
			view.reloadGrid();
		}
	}

	@EventHandler
	public void handleBatchInstanceUnlockEvent(BatchInstanceUnlockedEvent unlockBatchEvent) {
		if (null != unlockBatchEvent) {
			view.reloadGrid();
		}
	}

	@EventHandler
	public void handleBatchInstanceRestartedEvent(BatchInstanceRestartedEvent restartedEvent) {
		if (null != restartedEvent) {
			view.reloadGrid();
		}
	}

	@EventHandler
	public void handleMultipleDeletionStartEvent(StartMultipleBatchDeletionEvent multipleDeletionStartEvent) {
		if (null != multipleDeletionStartEvent) {
			final List<BatchInstanceDTO> selectedBatchInstances = view.getSelectedBatchInstances();
			if (CollectionUtil.isEmpty(selectedBatchInstances)) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECT_BATCH_TO_DELETE), DialogIcon.ERROR);
			} else {
				invalidBatchSelectionList.clear();
				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchInstanceConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.DELETE_THE_SELECTED_BATCH_INSTANCE_MESSAGE), false, DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
						List<String> selectedBatchInstanceIdentifiers = new LinkedList<String>();
						for (BatchInstanceDTO selectedBatchInstance : selectedBatchInstances) {
							if (canProcess(selectedBatchInstance)) {
								selectedBatchInstanceIdentifiers.add(selectedBatchInstance.getBatchIdentifier());
							} else {
								invalidBatchSelectionList.add(selectedBatchInstance);
							}
						}
						if (!CollectionUtil.isEmpty(selectedBatchInstanceIdentifiers)){
							deleteSelectedBatchInstances(selectedBatchInstanceIdentifiers);
						}
						if (!CollectionUtil.isEmpty(invalidBatchSelectionList)){
							String invalidBatchIdentifier = BatchInstanceConstants.EMPTY_STRING;
							for (BatchInstanceDTO invalidSelectedBatchInstance : invalidBatchSelectionList){
								if (!StringUtil.isNullOrEmpty(invalidBatchIdentifier)){
									invalidBatchIdentifier = StringUtil.concatenate(invalidBatchIdentifier, BatchInstanceConstants.COMMA);
								}
								invalidBatchIdentifier = StringUtil.concatenate(invalidBatchIdentifier, invalidSelectedBatchInstance.getBatchIdentifier());
							}
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
									StringUtil.concatenate(LocaleDictionary.getMessageValue(BatchInstanceMessages.CONNOT_DELETE_BATCH_WITH_ID), invalidBatchIdentifier,
											LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCHES_NOT_IN_DELETABLE_STATE)), DialogIcon.ERROR);
						}
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
			}
		}
	}

	@EventHandler
	public void handleMultipleCurrentRestartEvent(RestartCurrentBatchesEvent restartCurrentEvent) {
		if (null != restartCurrentEvent) {
			final List<BatchInstanceDTO> selectedBatchInstances = getSelectedBatches();
			if (!CollectionUtil.isEmpty(selectedBatchInstances)) {
				invalidBatchSelectionList.clear();
				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchInstanceConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.RESTART_BATCH_FROM_CURRENT_MODULE));
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						List<String> selectedBatchInstanceIdentifiers = new LinkedList<String>();
						for (BatchInstanceDTO selectedBatchInstance : selectedBatchInstances) {
							if (canProcess(selectedBatchInstance) && !isBatchLocked(selectedBatchInstance)) {
								selectedBatchInstanceIdentifiers.add(selectedBatchInstance.getBatchIdentifier());
							} else {
								invalidBatchSelectionList.add(selectedBatchInstance);
							}
						}
						if (!CollectionUtil.isEmpty(selectedBatchInstanceIdentifiers)) {
							restartSelectedBatchInstancesCurrent(selectedBatchInstanceIdentifiers);
						}
						if (!CollectionUtil.isEmpty(invalidBatchSelectionList)){
							String invalidBatchIdentifier = BatchInstanceConstants.EMPTY_STRING;
							for (BatchInstanceDTO invalidSelectedBatchInstance : invalidBatchSelectionList){
								if (!StringUtil.isNullOrEmpty(invalidBatchIdentifier)){
									invalidBatchIdentifier = StringUtil.concatenate(invalidBatchIdentifier, BatchInstanceConstants.COMMA);
								}
								invalidBatchIdentifier = StringUtil.concatenate(invalidBatchIdentifier, invalidSelectedBatchInstance.getBatchIdentifier());
							}
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
									StringUtil.concatenate(LocaleDictionary.getMessageValue(BatchInstanceMessages.CONNOT_RESTART_BATCH_WITH_ID), invalidBatchIdentifier, LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCHES_NOT_IN_RESTARTABLE_STATE)), DialogIcon.ERROR);
						}
					}

					@Override
					public void onCancelClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
					}

					@Override
					public void onCloseClick() {
						// TODO Auto-generated method stub

					}
				});
			} else {
				Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES), LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECT_BATCHES_FOR_RESTARTING));
			}
		}
	}

	@EventHandler
	public void handleMultipleSelectedRestartEvent(final RestartSelectedBatchesEvent restartSelectedEvent) {
		if (null != restartSelectedEvent) {
			final List<BatchInstanceDTO> selectedBatchInstances = getSelectedBatches();
			if (!CollectionUtil.isEmpty(selectedBatchInstances)) {
				invalidBatchSelectionList.clear();
				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.CONFIRMATION),
						StringUtil.concatenate(LocaleDictionary.getMessageValue(BatchInstanceMessages.RESTART_THE_BATCH_FROM_MODULE), restartSelectedEvent.getModuleName(), BatchInstanceConstants.QUESTION_MARK));
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						List<String> selectedBatchInstanceIdentifiers = new LinkedList<String>();
						for (BatchInstanceDTO selectedBatchInstance : selectedBatchInstances) {
							if (canProcess(selectedBatchInstance) && !isBatchLocked(selectedBatchInstance)) {
								selectedBatchInstanceIdentifiers.add(selectedBatchInstance.getBatchIdentifier());
							} else {
								invalidBatchSelectionList.add(selectedBatchInstance);
							}
						}
						if (!CollectionUtil.isEmpty(selectedBatchInstanceIdentifiers)) {
							restartSelectedBatchInstancesModule(selectedBatchInstanceIdentifiers, restartSelectedEvent.getModuleName());
						}
						if (!CollectionUtil.isEmpty(invalidBatchSelectionList)){
							String invalidBatchIdentifier = BatchInstanceConstants.EMPTY_STRING;
							for (BatchInstanceDTO invalidSelectedBatchInstance : invalidBatchSelectionList){
								if (!StringUtil.isNullOrEmpty(invalidBatchIdentifier)){
									invalidBatchIdentifier = StringUtil.concatenate(invalidBatchIdentifier, BatchInstanceConstants.COMMA);
								}
								invalidBatchIdentifier = StringUtil.concatenate(invalidBatchIdentifier, invalidSelectedBatchInstance.getBatchIdentifier());
							}
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
									StringUtil.concatenate(LocaleDictionary.getMessageValue(BatchInstanceMessages.CONNOT_RESTART_BATCH_WITH_ID), invalidBatchIdentifier, LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCHES_NOT_IN_RESTARTABLE_STATE)), DialogIcon.ERROR);
						}
					}

					@Override
					public void onCancelClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
					}

					@Override
					public void onCloseClick() {
						// TODO Auto-generated method stub

					}
				});
			} else {
				Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES), LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECT_BATCHES_FOR_RESTARTING));
			}
		}
	}

	@EventHandler
	public void handleMultiplePreviousRestartEvent(RestartPreviousBatchesEvent restartPreviousEvent) {
		if (null != restartPreviousEvent) {
			final List<BatchInstanceDTO> selectedBatchInstances = getSelectedBatches();
			if (!CollectionUtil.isEmpty(selectedBatchInstances)) {
				invalidBatchSelectionList.clear();
				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchInstanceConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.RESTART_BATCH_FROM_PREVIOUS_MODULE));
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						List<String> selectedBatchInstanceIdentifiers = new LinkedList<String>();
						for (BatchInstanceDTO selectedBatchInstance : selectedBatchInstances) {
							if (canProcess(selectedBatchInstance) && !isBatchLocked(selectedBatchInstance)) {
								selectedBatchInstanceIdentifiers.add(selectedBatchInstance.getBatchIdentifier());
							} else {
								invalidBatchSelectionList.add(selectedBatchInstance);
							}
						}
						if (!CollectionUtil.isEmpty(selectedBatchInstanceIdentifiers)) {
							restartSelectedBatchInstancesPrevious(selectedBatchInstanceIdentifiers);
						}
						if (!CollectionUtil.isEmpty(invalidBatchSelectionList)){
							String invalidBatchIdentifier = BatchInstanceConstants.EMPTY_STRING;
							for (BatchInstanceDTO invalidSelectedBatchInstance : invalidBatchSelectionList){
								if (!StringUtil.isNullOrEmpty(invalidBatchIdentifier)){
									invalidBatchIdentifier = StringUtil.concatenate(invalidBatchIdentifier, BatchInstanceConstants.COMMA);
								}
								invalidBatchIdentifier = StringUtil.concatenate(invalidBatchIdentifier, invalidSelectedBatchInstance.getBatchIdentifier());
							}
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
									StringUtil.concatenate(LocaleDictionary.getMessageValue(BatchInstanceMessages.CONNOT_RESTART_BATCH_WITH_ID), invalidBatchIdentifier, LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCHES_NOT_IN_RESTARTABLE_STATE)), DialogIcon.ERROR);
						}
					}

					@Override
					public void onCancelClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
					}

					@Override
					public void onCloseClick() {
						// TODO Auto-generated method stub

					}
				});
			} else {
				Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES), LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECT_BATCHES_FOR_RESTARTING));
			}
		}
	}

	@EventHandler
	public void handleMultipleFirstRestartEvent(RestartFirstBatchesEvent restartFirstEvent) {
		if (null != restartFirstEvent) {
			final List<BatchInstanceDTO> selectedBatchInstances = getSelectedBatches();
			if (!CollectionUtil.isEmpty(selectedBatchInstances)) {
				invalidBatchSelectionList.clear();
				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchInstanceConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.RESTART_BATCH_FROM_FIRST_MODULE));
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						List<String> selectedBatchInstanceIdentifiers = new LinkedList<String>();
						for (BatchInstanceDTO selectedBatchInstance : selectedBatchInstances) {
							if (canProcess(selectedBatchInstance) && !isBatchLocked(selectedBatchInstance)) {
								selectedBatchInstanceIdentifiers.add(selectedBatchInstance.getBatchIdentifier());
							} else {
								invalidBatchSelectionList.add(selectedBatchInstance);
							}
						}
						if (!CollectionUtil.isEmpty(selectedBatchInstanceIdentifiers)) {
							restartSelectedBatchInstancesFirst(selectedBatchInstanceIdentifiers);
						}
						if (!CollectionUtil.isEmpty(invalidBatchSelectionList)){
							String invalidBatchIdentifier = BatchInstanceConstants.EMPTY_STRING;
							for (BatchInstanceDTO invalidSelectedBatchInstance : invalidBatchSelectionList){
								if (!StringUtil.isNullOrEmpty(invalidBatchIdentifier)){
									invalidBatchIdentifier = StringUtil.concatenate(invalidBatchIdentifier, BatchInstanceConstants.COMMA);
								}
								invalidBatchIdentifier = StringUtil.concatenate(invalidBatchIdentifier, invalidSelectedBatchInstance.getBatchIdentifier());
							}
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
									StringUtil.concatenate(LocaleDictionary.getMessageValue(BatchInstanceMessages.CONNOT_RESTART_BATCH_WITH_ID), invalidBatchIdentifier, LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCHES_NOT_IN_RESTARTABLE_STATE)), DialogIcon.ERROR);
						}
					}

					@Override
					public void onCancelClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
					}

					@Override
					public void onCloseClick() {
						// TODO Auto-generated method stub

					}
				});
			} else {
				Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES), LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECT_BATCHES_FOR_RESTARTING));
			}
		}
	}

	private void restartSelectedBatchInstancesCurrent(List<String> selectedBatchInstanceIdentifiers) {
		rpcService.restartBatchInstanceCurrent(selectedBatchInstanceIdentifiers, new AsyncCallback<BatchInstancePresenter.Results>() {

			@Override
			public void onSuccess(Results result) {
				if (result == Results.SUCCESSFUL) {
					Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES),
							LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECTED_BATCHES_RESTARTED_SUCCESSFULLY_FROM_CURRENT_MODULE));
				} else {
					Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES),
							LocaleDictionary.getMessageValue(BatchInstanceMessages.SOME_BATCHES_RESTARTED_SUCCESSFULLY_FROM_CURRENT_MODULE));
				}
				controller.setSelectedBatchInstance(null);
				view.reloadGrid();
			}

			@Override
			public void onFailure(Throwable caught) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECTED_BATCH_COULD_NOT_BE_RESTARTED), DialogIcon.ERROR);
			}
		});
	}

	private void restartSelectedBatchInstancesModule(List<String> selectedBatchInstanceIdentifiers, String moduleName) {
		rpcService.restartBatchInstanceSelectedModule(selectedBatchInstanceIdentifiers, moduleName,
				new AsyncCallback<BatchInstancePresenter.Results>() {

					@Override
					public void onSuccess(Results result) {
						if (result == Results.SUCCESSFUL) {
							Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES),
									LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECTED_BATCHES_RESTARTED_SUCCESSFULLY_FROM_MODULE));
						} else {
							Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES),
									LocaleDictionary.getMessageValue(BatchInstanceMessages.SOME_BATCHES_RESTARTED_SUCCESSFULLY_FROM_MODULE));
						}
						controller.setSelectedBatchInstance(null);
						view.reloadGrid();
					}

					@Override
					public void onFailure(Throwable caught) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECTED_BATCH_COULD_NOT_BE_RESTARTED), DialogIcon.ERROR);
					}
				});
	}

	private void restartSelectedBatchInstancesPrevious(List<String> selectedBatchInstanceIdentifiers) {
		rpcService.restartBatchInstancePrevious(selectedBatchInstanceIdentifiers, new AsyncCallback<BatchInstancePresenter.Results>() {

			@Override
			public void onSuccess(Results result) {
				if (result == Results.SUCCESSFUL) {
					Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES),
							LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECTED_BATCHES_RESTARTED_SUCCESSFULLY_FROM_PREVIOUS_MODULE));
				} else {
					Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES),
							LocaleDictionary.getMessageValue(BatchInstanceMessages.SOME_BATCHES_RESTARTED_SUCCESSFULLY_FROM_PREVIOUS_MODULE));
				}
				controller.setSelectedBatchInstance(null);
				view.reloadGrid();
			}

			@Override
			public void onFailure(Throwable caught) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECTED_BATCH_COULD_NOT_BE_RESTARTED), DialogIcon.ERROR);
			}
		});
	}

	private void restartSelectedBatchInstancesFirst(List<String> selectedBatchInstanceIdentifiers) {
		rpcService.restartBatchInstanceFirst(selectedBatchInstanceIdentifiers, new AsyncCallback<BatchInstancePresenter.Results>() {

			@Override
			public void onSuccess(Results result) {
				if (result == Results.SUCCESSFUL) {
					Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES),
							LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECTED_BATCHES_RESTARTED_SUCCESSFULLY_FROM_FIRST_MODULE));
				} else {
					Message.display(LocaleDictionary.getConstantValue(BatchInstanceConstants.RESTART_BATCHES),
							LocaleDictionary.getMessageValue(BatchInstanceMessages.SOME_BATCHES_RESTARTED_SUCCESSFULLY_FROM_FIRST_MODULE));
				}
				controller.setSelectedBatchInstance(null);
				view.reloadGrid();
			}

			@Override
			public void onFailure(Throwable caught) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECTED_BATCH_COULD_NOT_BE_RESTARTED), DialogIcon.ERROR);
			}
		});
	}

	private void deleteSelectedBatchInstances(List<String> selectedBatchInstanceIdentifiers) {
		rpcService.deleteBatchInstance(selectedBatchInstanceIdentifiers, new AsyncCallback<BatchInstancePresenter.Results>() {

			@Override
			public void onSuccess(Results result) {
				if (result == Results.SUCCESSFUL) {
					Message.display(LocaleDictionary.getConstantValue(LocaleCommonConstants.SUCCESS_TITLE),
							LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECTED_BATCHES_DELETED_SUCCESSFULLY));
				} else {
					Message.display(LocaleDictionary.getConstantValue(LocaleCommonConstants.SUCCESS_TITLE),
							LocaleDictionary.getMessageValue(BatchInstanceMessages.SOME_BATCHES_DELETED_SUCCESSFULLY));
				}
				controller.setSelectedBatchInstance(null);
				view.reloadGrid();
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

	@EventHandler
	public void handleRestartAllBatchesEvent(RestartAllBatchesEvent restartAllBatchesEvent) {
		if (null != restartAllBatchesEvent) {
			view.reloadGrid();
		}
	}

	@EventHandler
	public void handleDeleteAllBatchesEvent(DeleteAllBatchesEvent deleteAllBatchesEvent) {
		if (null != deleteAllBatchesEvent) {
			view.reloadGrid();
		}
	}

	public void savePriority(BatchInstanceDTO editedBatchInstance) {
		if (null != editedBatchInstance) {
			rpcService.updateBatchInstancePriority(editedBatchInstance, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onSuccess(Boolean result) {
					// TODO Auto-generated method stub
					if (result) {
						view.reloadGrid();
					} else {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchInstanceMessages.BATCH_INSTANCE_PRIORITY_UPDATE_FAILED), DialogIcon.ERROR);
					}
				}
			});
		}
	}

	/**
	 * Gets list of checked Batch Instances, if none is selected returns the current selected Batch Instance.
	 * 
	 * @return {@link List}<{@link BatchInstanceDTO}>
	 */
	public List<BatchInstanceDTO> getSelectedBatches() {
		List<BatchInstanceDTO> selectedBatches = view.getSelectedBatchInstances();
		if (CollectionUtil.isEmpty(selectedBatches)) {
			selectedBatches = new ArrayList<BatchInstanceDTO>();
			final BatchInstanceDTO selectedBatch = controller.getSelectedBatchInstance();
			if (null != selectedBatch) {
				selectedBatches.add(selectedBatch);
			}
		}
		return selectedBatches;
	}

	/**
	 * Load batch instance charts.
	 */
	public void loadBatchInstanceCharts(final FilterPagingLoadConfig loadConfig) {
		// Fires the event to create charts.
		controller.setLoadConfig(loadConfig);
		controller.getEventBus().fireEvent(new LoadBatchInstanceChartsEvent());
	}

	public void setTroubleshootArtifacts(final ListStore<BatchInstanceDTO> store) {
		if (store == null || store.size() == 0) {
			controller.getEventBus().fireEvent(new LoadTroubleshootArtifactsEvent(false));
		} else {
			controller.getEventBus().fireEvent(new LoadTroubleshootArtifactsEvent(true));
		}
	}
	
	private boolean isBatchLocked(final BatchInstanceDTO selectedBatchInstance) {
		boolean batchLocked = false;
		if (null != selectedBatchInstance){
			final String currentUser = selectedBatchInstance.getCurrentUser();
			if (!StringUtil.isNullOrEmpty(currentUser)){
				batchLocked = true;
			}
		}
		return batchLocked;
	}

	public void clearDetailsOfPreviouslySelectedBatch() {
		controller.getBatchInstancePresenter().getBatchInstanceDetailPresenter().loadEmptyBatchDetailsGrid();
		controller.getBatchInstancePresenter().getBatchInstanceDetailPresenter().clearProgressBarsContainer();
	}

	@EventHandler
	public void getSelectedBatches(GetSelectedBatchInstanceEvent getSelectedBatchInstanceEvent) {
		List<BatchInstanceDTO> selectedBatches = getSelectedBatches();

		if (getSelectedBatchInstanceEvent.isUnlock()) {
			controller.getEventBus().fireEvent(new BatchInstanceUnlockEvent(selectedBatches));
		}
		if (getSelectedBatchInstanceEvent.isOpen()) {
			if (CollectionUtil.isEmpty(selectedBatches)) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.NO_RECORD_SELECTED_TO_OPEN), DialogIcon.WARNING);
			}
			else if(selectedBatches.size()>1){
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.SELECT_ONLY_ONE_ROW_TO_OPEN), DialogIcon.WARNING);
			}
			else{
				controller.getEventBus().fireEvent(new BatchInstanceOpenEvent(selectedBatches.get(0)));
			}
		}

	}
}
