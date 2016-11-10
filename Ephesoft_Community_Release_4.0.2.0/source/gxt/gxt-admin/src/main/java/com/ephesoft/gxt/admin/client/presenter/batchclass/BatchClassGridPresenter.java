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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ephesoft.dcma.core.common.DefaultBatchClasses;
import com.ephesoft.gxt.admin.client.BatchClassManagementServiceAsync;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.ApplyBatchClassChangesEvent;
import com.ephesoft.gxt.admin.client.event.BatchClassAdditionEvent;
import com.ephesoft.gxt.admin.client.event.BatchClassImportEndEvent;
import com.ephesoft.gxt.admin.client.event.BatchClassSelectionEvent;
import com.ephesoft.gxt.admin.client.event.CopyBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.DeleteMultipleBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.ExportBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.GenerateKeyBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.OpenBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.ReloadBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.SetSelectedBatchClassEvent;
import com.ephesoft.gxt.admin.client.event.UnlockMultipleBatchClassEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.presenter.BatchClassPresenter;
import com.ephesoft.gxt.admin.client.presenter.BatchClassPresenter.Results;
import com.ephesoft.gxt.admin.client.view.batchclass.BatchClassGridView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassListDTO;
import com.ephesoft.gxt.core.shared.dto.RoleDTO;
import com.ephesoft.gxt.core.shared.exception.UIArgumentException;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

public class BatchClassGridPresenter extends BatchClassInlinePresenter<BatchClassGridView> {

	private BatchClassManagementServiceAsync rpcService;

	interface CustomEventBinder extends EventBinder<BatchClassGridPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private BatchClassListDTO batchClassListdto = new BatchClassListDTO();

	public BatchClassGridPresenter(BatchClassManagementController controller, BatchClassGridView view) {
		super(controller, view);
		rpcService = controller.getRpcService();
		loadRoles();
	}

	/**
	 * Load All Roles.
	 */
	public void loadRoles() {
		rpcService.getAllRoles(new AsyncCallback<List<RoleDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_LOAD_ROLES), DialogIcon.ERROR);
			}

			@Override
			public void onSuccess(List<RoleDTO> result) {
				controller.setRoleDTOs(result);
				view.setRoles(getAllRoles());
			}
		});
	}

	@Override
	public void bind() {
		view.reloadGrid();
		controller.setBatchClassRequiresReload(true);
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	/**
	 * @return the rpcService
	 */
	public BatchClassManagementServiceAsync getRpcService() {
		return rpcService;
	}

	public void selectBatchClass(CellSelectionChangedEvent<BatchClassDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			BatchClassDTO selectedBatchClass = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			if (selectedBatchClass != null) {
				controller.setSelectedBatchClass(selectedBatchClass);
				BatchClassManagementEventBus.fireEvent(new BatchClassSelectionEvent(selectedBatchClass));
			}
		}
	}

	@EventHandler
	public void handleMultipleBatchClassDeletion(DeleteMultipleBatchClassEvent deleteEvent) {
		if (null != deleteEvent) {
			final List<BatchClassDTO> selectedBatchClasses = view.getSelectedBatchClasses();
			if (!CollectionUtil.isEmpty(selectedBatchClasses)) {
				final List<BatchClassDTO> deletableBatchClasses = new ArrayList<BatchClassDTO>();
				final boolean isNonDeletableBCExist = isNonDeletableBatchClassesExist(selectedBatchClasses, deletableBatchClasses);
				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
						LocaleDictionary.getConstantValue(BatchClassConstants.DELETE_THE_SELECTED_BATCH_CLASS_MESSAGE), false,
						DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
						deleteSelectedBatchClasses(deletableBatchClasses, isNonDeletableBCExist);
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
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_BATCH_CLASSES_DELETE_MESSAGE), DialogIcon.WARNING);
			}
		}
	}

	private boolean isNonDeletableBatchClassesExist(List<BatchClassDTO> selectedBatchClasses, List<BatchClassDTO> deletableBatchClasses) {
		boolean isNonDeletable = false;
		String identifier = null;
		for (BatchClassDTO batchClassDTO : selectedBatchClasses) {
			identifier = batchClassDTO.getIdentifier();
			if (!StringUtil.isNullOrEmpty(identifier) && !DefaultBatchClasses.isDefaultBatchClass(identifier)) {
				deletableBatchClasses.add(batchClassDTO);
			} else {
				isNonDeletable = true;
			}
		}
		return isNonDeletable;
	}

	private void deleteSelectedBatchClasses(List<BatchClassDTO> selectedBatchClasses, final boolean isNonDeletableBCExist) {
		if (!CollectionUtil.isEmpty(selectedBatchClasses)) {
			rpcService.deleteBatchClasses(selectedBatchClasses, new AsyncCallback<BatchClassPresenter.Results>() {

				@Override
				public void onFailure(Throwable caught) {
					// failure is handled through result of the service.
				}

				@Override
				public void onSuccess(Results result) {
					if (null != result) {
						if (result == Results.FAILURE) {
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASSES_DELETION_FAILED_MESSAGE));
						} else {
							if (result == Results.PARTIAL_SUCCESS) {
								Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.BATCH_CLASS_DELETION),
										LocaleDictionary.getMessageValue(BatchClassMessages.PARTIAL_BATCH_CLASSES_DELETION_SUCCESS));
							} else {
								Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.BATCH_CLASS_DELETION),
										LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASSES_DELETION_SUCCESS));
							}
							if (isNonDeletableBCExist) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.PRIMARY_BATCH_CLASSES_CANNOT_BE_DELETED), DialogIcon.ERROR);
							}
							controller.getEventBus().fireEvent(new ReloadBatchClassEvent());
						}

					}
				}

			});
		} else {
			if (isNonDeletableBCExist) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.PRIMARY_BATCH_CLASSES_CANNOT_BE_DELETED), DialogIcon.ERROR);
			}
		}
	}

	@EventHandler
	public void handleMultipleBatchClassUnlock(UnlockMultipleBatchClassEvent unlockEvent) {
		if (null != unlockEvent) {
			final List<BatchClassDTO> selectedBatchClasses = view.getSelectedBatchClasses();
			if (!CollectionUtil.isEmpty(selectedBatchClasses)) {
				unlockSelectedBatchInstances(selectedBatchClasses);
			} else {
				List<BatchClassDTO> batchClassDTOs = view.getGrid().getSelectionModel().getSelection();
				if (!CollectionUtil.isEmpty(batchClassDTOs)) {
					BatchClassDTO batchClassDTO = (BatchClassDTO) view.getGrid().getSelectionModel().getSelection().get(0);
					unlockSelectedBatchInstances(Collections.singletonList(batchClassDTO));
				} else {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_BATCH_CLASSES_UNLOCK_MESSAGE),
							DialogIcon.WARNING);
				}

			}
		}
	}

	private void unlockSelectedBatchInstances(List<BatchClassDTO> selectedBatchClasses) {
		rpcService.clearCurrentUser(selectedBatchClasses, new AsyncCallback<BatchClassPresenter.Results>() {

			@Override
			public void onFailure(Throwable caught) {
				// Do nothing failure scenario is handled.
			}

			@Override
			public void onSuccess(Results result) {
				if (null != result) {
					if (result == Results.FAILURE) {
						DialogUtil.showMessageDialog(
								LocaleDictionary.getLocaleDictionary().getConstantValue(BatchClassConstants.INFO_TITLE),
								LocaleDictionary.getLocaleDictionary().getMessageValue(
										BatchClassMessages.BATCH_CLASSES_UNLOCK_FAILURE_MESSAGE));
					} else {
						if (result == Results.PARTIAL_SUCCESS) {
							Message.display(
									LocaleDictionary.getLocaleDictionary().getConstantValue(BatchClassConstants.UNLOCK_BATCH_CLASS),
									LocaleDictionary.getLocaleDictionary().getMessageValue(
											BatchClassMessages.PARTIAL_BATCH_CLASSES_UNLOCK_SUCCESS));
						} else {
							Message.display(
									LocaleDictionary.getLocaleDictionary().getConstantValue(BatchClassConstants.UNLOCK_BATCH_CLASS),
									LocaleDictionary.getLocaleDictionary().getMessageValue(
											BatchClassMessages.BATCH_CLASSES_UNLOCK_SUCCESS));
						}
						controller.getEventBus().fireEvent(new ReloadBatchClassEvent());
					}

				}
			}
		});
	}

	@EventHandler
	public void handleReloadBatchClass(ReloadBatchClassEvent reloadEvent) {
		if (null != reloadEvent) {
			view.reloadGrid();
		}

	}

	public void addBatchClassToUpdated(BatchClassDTO batchClassDTO) {
		batchClassListdto.addBatchClass(batchClassDTO);
	}

	@EventHandler
	public void handleBatchClassUpdateEvent(ApplyBatchClassChangesEvent applyBatchClassChanges) {
		if (view.isValid()) {
			try {
				rpcService.updateBatchClassList(batchClassListdto, new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						if(caught instanceof UIArgumentException){
							UIArgumentException exc = (UIArgumentException)caught;
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									StringUtil.concatenate(LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_UPDATE_BATCH_CLASS_INFO), LocaleDictionary.getMessageValue(exc.getMessage(), exc.getArgument())), DialogIcon.ERROR);
						
						}else{
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								StringUtil.concatenate(LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_UPDATE_BATCH_CLASS_INFO), LocaleDictionary.getMessageValue(caught.getMessage())), DialogIcon.ERROR);
						}
					}

					@Override
					public void onSuccess(Void result) {
						view.refresh();
						Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS), LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_UPDATED_SUCCESSFULLY));
						batchClassListdto.removeAllBatchClasses();
					}
				});
			} 
			catch(UIArgumentException argumentException){
				
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						StringUtil.concatenate(LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_UPDATE_BATCH_CLASS_INFO), LocaleDictionary.getMessageValue(argumentException.getMessage(), argumentException.getArgument())), DialogIcon.ERROR);
			}
			catch (UIException e) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						StringUtil.concatenate(LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_UPDATE_BATCH_CLASS_INFO), LocaleDictionary.getMessageValue(e.getMessage())), DialogIcon.ERROR);
			}
		} else {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_VALIDATE_BATCH_CLASS), DialogIcon.ERROR);
		}
	}

	@EventHandler
	public void handleBatchClassAdditionEVent(BatchClassAdditionEvent batchClassAdditionEvent) {
		if (null != batchClassAdditionEvent) {
			view.addBatchClass(batchClassAdditionEvent.getBatchClassDTO());
		}
	}

	public boolean isValid() {
		return view.isValid();
	}

	@EventHandler
	public void handleBatchClassImportEndEvent(BatchClassImportEndEvent batchClassImportEndEvent) {
		if (null != batchClassImportEndEvent) {
			view.reloadGrid();
		}
	}

	/**
	 * Sets the selected batch class.
	 * 
	 * @param setSelectedBatchClassEvent the new selected batch class
	 */
	@EventHandler
	public void setSelectedBatchClass(SetSelectedBatchClassEvent setSelectedBatchClassEvent) {
		if (null != setSelectedBatchClassEvent && isValid()) {
			view.getGrid().getStore().commitChanges();
			final List<BatchClassDTO> selectedBatchClasses = view.getSelectedBatchClasses();
			int totalBatchClasses = view.getGrid().getPaginationLoader().getTotalCount();
			if (CollectionUtil.isEmpty(selectedBatchClasses)) {
				if (totalBatchClasses == 0) {
					showMessage(setSelectedBatchClassEvent, BatchClassMessages.NO_RECORD_TO_EDIT);
				} else {
					List<BatchClassDTO> batchClassDTOs = view.getGrid().getSelectionModel().getSelection();
					if (!CollectionUtil.isEmpty(batchClassDTOs)) {
						BatchClassDTO batchClassDTO = (BatchClassDTO) view.getGrid().getSelectionModel().getSelection().get(0);
						fireEvent(setSelectedBatchClassEvent, batchClassDTO);
					} else {
						showMessage(setSelectedBatchClassEvent, BatchClassMessages.NO_RECORD_TO_EDIT);
					}
				}
			} else {
				if (selectedBatchClasses.size() > 1) {
					showMessage(setSelectedBatchClassEvent, BatchClassMessages.SELECT_ONE_ROW_ONLY_MSG);
				} else {
					fireEvent(setSelectedBatchClassEvent, selectedBatchClasses.get(0));
				}
			}
		} else {
			showMessage(setSelectedBatchClassEvent, BatchClassMessages.BATCH_CLASS_VIEW_INVALID);
		}

	}

	/**
	 * Show message.
	 * 
	 * @param setSelectedBatchClassEvent the set selected batch class event
	 * @param messageValue the message value
	 */
	private void showMessage(SetSelectedBatchClassEvent setSelectedBatchClassEvent, String messageValue) {
		messageValue = LocaleDictionary.getMessageValue(messageValue);
		if (setSelectedBatchClassEvent.isCopyBatchClass()) {
			messageValue = StringUtil.concatenate(messageValue, LocaleDictionary.getMessageValue(BatchClassMessages.COPY_BATCH_CLASS));
		} else if (setSelectedBatchClassEvent.isExportBatchClass()) {
			messageValue = StringUtil.concatenate(messageValue,
					LocaleDictionary.getMessageValue(BatchClassMessages.EXPORT_BATCH_CLASS));
		} else if(setSelectedBatchClassEvent.isGenerateKey()){
			messageValue = StringUtil.concatenate(messageValue,
					LocaleDictionary.getMessageValue(BatchClassMessages.ENCRYPT_BATCH_CLASS));
		}
		DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE), messageValue,
				DialogIcon.WARNING);
	}

	/**
	 * Fire event.
	 * 
	 * @param setSelectedBatchClassEvent the set selected batch class event
	 * @param batchClassDTO the batch class dto
	 */
	private void fireEvent(SetSelectedBatchClassEvent setSelectedBatchClassEvent, BatchClassDTO batchClassDTO) {
		controller.setSelectedBatchClass(batchClassDTO);
		if (setSelectedBatchClassEvent.isCopyBatchClass()) {
			BatchClassManagementEventBus.fireEvent(new CopyBatchClassEvent());
		} else if (setSelectedBatchClassEvent.isExportBatchClass()) {
			BatchClassManagementEventBus.fireEvent(new ExportBatchClassEvent());
		} else if (setSelectedBatchClassEvent.isOpenBatchClass()) {
			BatchClassManagementEventBus.fireEvent(new OpenBatchClassEvent());
		} else if(setSelectedBatchClassEvent.isGenerateKey()){
			BatchClassManagementEventBus.fireEvent(new GenerateKeyBatchClassEvent());
		}
	}

	public List<String> getAllRoles() {
		List<String> rolesString = new ArrayList<String>();
		List<RoleDTO> roles = controller.getRoleDTOs();
		if (!CollectionUtil.isEmpty(roles)) {
			for (RoleDTO role : roles) {
				rolesString.add(role.getName());
			}
		}
		return rolesString;
	}
	
	/**
	 * Sets the batch class list.
	 *
	 * @param batchClassList the new batch class list
	 */
	public void setBatchClassList(List<BatchClassDTO> batchClassList){
		controller.setBatchClassList(batchClassList);
	}
}
