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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.batchinstance.client.controller.BatchInstanceController;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceSelectionEvent;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceViewAdditionEvent;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceMessages;
import com.ephesoft.gxt.batchinstance.client.shared.constants.BatchInfoConstants;
import com.ephesoft.gxt.batchinstance.client.view.BatchInstanceDetailView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.DetailsDTO;
import com.ephesoft.gxt.core.shared.dto.ModuleDTO;
import com.ephesoft.gxt.core.shared.dto.WorkflowDetailDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class BatchInstanceDetailPresenter extends BatchInstanceBasePresenter<BatchInstanceDetailView> {

	TroubleshootPresenter troubleshootPresenter;
	
	private Map<String, String> moduleDetailMap;
	
	private BatchInstanceDTO selectedBatchInstance;

	interface CustomEventBinder extends EventBinder<BatchInstanceDetailPresenter> {
	}

	public final static CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public BatchInstanceDetailPresenter(BatchInstanceController controller, BatchInstanceDetailView view) {
		super(controller, view);
		troubleshootPresenter = new TroubleshootPresenter(controller, view.getTroubleshootPanel());
		loadEmptyBatchDetailsGrid();

		controller.getRpcService().getEnhancedLoggingSwitch(new AsyncCallback<String>() {

			@Override
			public void onSuccess(final String enhancedLoggingSwitch) {
				enableBatchInstanceLogging(enhancedLoggingSwitch);
			}

			@Override
			public void onFailure(final Throwable caught) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchInstanceMessages.UNABLE_TO_FETCH_BATCH_INSTANCE_LOGGING_SWITCH), DialogIcon.ERROR);
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

	@EventHandler
	public void onBatchInstanceSelection(BatchInstanceSelectionEvent batchInstanceSelectionEvent) {
		if (null != batchInstanceSelectionEvent) {
			selectedBatchInstance = batchInstanceSelectionEvent.getBatchInstanceDTO();
			if (null == selectedBatchInstance) {
				loadEmptyBatchDetailsGrid();
			} else {
				view.setSelectedBatchInstance(selectedBatchInstance);
				setErrorCauseForBatchInstance(selectedBatchInstance);
				if (BatchInstanceStatus.FINISHED.getName().equalsIgnoreCase(selectedBatchInstance.getStatus())) {
					if((moduleDetailMap == null) || (moduleDetailMap.size() < 1) ) {
						moduleDetailMap = new LinkedHashMap<String, String>();
						controller.getRpcService().getAllModules(new AsyncCallback<List<ModuleDTO>>() {

							@Override
							public void onSuccess(List<ModuleDTO> moduleList) {
								for (ModuleDTO module : moduleList) {
									moduleDetailMap.put(module.getIdentifier(), module.getDescription());
								}
								progressBarForFinishedState(selectedBatchInstance,moduleDetailMap);
							}

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}
						});
					} else {
						progressBarForFinishedState(selectedBatchInstance,moduleDetailMap);
					}
					
					
				} else {
					getPluginAndModuleProgress(selectedBatchInstance);
				}
			}
		}
	}

	private void clearBatchDetailView() {
		view.clearPane();
	}

	private void setErrorCauseForBatchInstance(final BatchInstanceDTO selectedBatchInstance) {
		if (selectedBatchInstance.getStatus().equalsIgnoreCase(BatchInstanceConstants.ERROR)) {
			controller.getRpcService().getBatchInstanceLogErrorDetails(selectedBatchInstance.getBatchIdentifier(),
					new AsyncCallback<String>() {

						@Override
						public void onSuccess(final String batchInstanceError) {
							if (batchInstanceError != null
									&& !batchInstanceError.equalsIgnoreCase(BatchInstanceConstants.EMPTY_STRING)) {
								selectedBatchInstance.setErrorCause(batchInstanceError);
							} else {
								selectedBatchInstance.setErrorCause(BatchInstanceConstants.EMPTY_STRING);
							}

						}

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							selectedBatchInstance.setErrorCause(BatchInstanceConstants.EMPTY_STRING);
						}
					});
		}
	}

	private void getPluginAndModuleProgress(final BatchInstanceDTO selectedBatchInstance) {
		String batchInstanceIdentifier = selectedBatchInstance.getBatchIdentifier();
		rpcService.getBatchInstanceWorkflowProgress(batchInstanceIdentifier, new AsyncCallback<WorkflowDetailDTO>() {

			@Override
			public void onFailure(Throwable caught) {
				// MessageDialog dialog = new MessageDialog(BatchInstanceConstants.BATCH_DETAIL_FAILURE,
				// BatchInstanceConstants.FAILED_DETAILS_OF_BATCH_INSTANCE_IDENTIFIER, DialogIcon.ERROR);
				// dialog.show();
			}

			@Override
			public void onSuccess(WorkflowDetailDTO result) {
				if (null != result) {
					view.initializeProgressBars(result);
				}
				getDetailsOfBatch(selectedBatchInstance);
			}
		});

	}

	@EventHandler
	public void handleBatchInstanceViewAdditionEvent(BatchInstanceViewAdditionEvent viewAdditionEvent) {
		Timer timer = new Timer() {

			@Override
			public void run() {
				view.forceLayout();
			}
		};
		timer.schedule(300);
	}

	/**
	 * Sets blank details in Batch Details Grid for the first time.
	 */
	public void loadEmptyBatchDetailsGrid() {
		final List<DetailsDTO> detailsList = new ArrayList<DetailsDTO>();
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.BATCH_CLASS_NAME_LABEL), BatchInstanceConstants.EMPTY_STRING));
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.BATCH_IDENTIFIER), BatchInstanceConstants.EMPTY_STRING));
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.BATCH_NAME), BatchInstanceConstants.EMPTY_STRING));
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.BATCH_PRIORITY), BatchInstanceConstants.EMPTY_STRING));
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.CURRENT_STATUS), BatchInstanceConstants.EMPTY_STRING));
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.UNC_FOLDER_PATH), BatchInstanceConstants.EMPTY_STRING));
		view.initializeBatchInstanceDetails(detailsList);
	}

	/**
	 * This method shows the log message error dialog.
	 * 
	 * @param batchInstanceIdentifier {@link String} The batch Instance Identifier which is in ERROR state and logs information needs
	 *            to be fetched.
	 */
	public void showErrorLogMessageDialog(final String batchInstanceIdentifier) {

		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getOldBatchInstanceLogErrorDetails(batchInstanceIdentifier,
				new AsyncCallback<Map<String, Object>>() {

					@Override
					public void onSuccess(final Map<String, Object> batchInstanceErrorDetailsMap) {
						ScreenMaskUtility.unmaskScreen();
						String errorMessage = (String) batchInstanceErrorDetailsMap.get(BatchInfoConstants.BI_ERROR_MESSAGE);
						final String batchInstanceLogFilePath = (String) batchInstanceErrorDetailsMap
								.get(BatchInfoConstants.BI_LOG_FILE_PATH);
						if (StringUtil.isNullOrEmpty(errorMessage)) {
							errorMessage = StringUtil.concatenate(/*"Download logs for batch "*/
									LocaleDictionary.getMessageValue(BatchInstanceMessages.NO_ERROR_LOG_FOUND) ,
									batchInstanceIdentifier);
						}
						ConfirmationDialog confirmationDialog = null;
						if (!StringUtil.isNullOrEmpty(batchInstanceLogFilePath)) {
							confirmationDialog = DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.CONFIRMATION), errorMessage);
							confirmationDialog.getButton(PredefinedButton.OK).setText(LocaleDictionary.getConstantValue(
									 BatchInstanceConstants.DOWNLOAD_LOG_FILE));
							confirmationDialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {

								@Override
								public void onSelect(final SelectEvent event) {
									view.setBatchInstanceLogFilePath(batchInstanceLogFilePath);
									view.setBatchInstanceIdentifier(batchInstanceIdentifier);
									view.getLogFileDownloadPanel().submit();

								}
							});
							confirmationDialog.show();
							confirmationDialog.center();
							// confirmationDialog.okButton.setFocus(true);
						} else {
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(BatchInstanceMessages.ERROR_OCCURED_IN_GETTING_LOG_MESSAGE),
									DialogIcon.ERROR);
							// confirmationDialog = ConfirmationDialogUtil.showConfirmationDialogError(errorMessage, Boolean.TRUE);
						}

						// Added to break text because of text alignment issue. JIRA-11192
						// confirmationDialog.addStyleName(CoreCommonConstants.BREAK_ALL_TEXT_CSS );

					}

					@Override
					public void onFailure(final Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchInstanceMessages.ERROR_OCCURED_IN_GETTING_LOG_MESSAGE),
								DialogIcon.ERROR);
					}
				});
	}

	private void enableBatchInstanceLogging(final String switchValue) {
		view.setEnhancedLoggingSwitch(switchValue);
		view.addErrorLogsDownloadHandler();
	}

	public void getDetailsOfBatch(final BatchInstanceDTO selectedBatchInstance) {
		List<DetailsDTO> detailsList = new ArrayList<DetailsDTO>();
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.BATCH_CLASS_NAME_LABEL), selectedBatchInstance.getBatchClassName()));
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.BATCH_IDENTIFIER), selectedBatchInstance.getBatchIdentifier()));
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.BATCH_NAME), selectedBatchInstance.getBatchName()));
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.BATCH_DESCRIPTION), selectedBatchInstance.getBatchDescription()));
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.BATCH_PRIORITY), selectedBatchInstance.getPriority()
				+ BatchInstanceConstants.EMPTY_STRING));
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.CURRENT_STATUS), selectedBatchInstance.getStatus()));
		detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.UNC_FOLDER_PATH), selectedBatchInstance.getUncSubFolderPath()));
		if (BatchInstanceConstants.ERROR.equalsIgnoreCase(selectedBatchInstance.getStatus())) {
			detailsList.add(new DetailsDTO(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_CAUSE), selectedBatchInstance.getErrorCause()));
		}
		view.initializeBatchInstanceDetails(detailsList);
	}

	/**
	 * Clears the progress bar conatiner.
	 */
	public void clearProgressBarsContainer() {
		view.clearProgressBarsContainer();
	}
	
	public void progressBarForFinishedState(final BatchInstanceDTO selectedBatchInstance,final Map<String, String> moduleDetailMap ) {
		List<String> moduleDescriptionList = new ArrayList<String>();
		String executedModulesId = selectedBatchInstance.getExecutedModules();
		int temp = executedModulesId.length();
		for (int i = 0; i < temp;) {
			String executedModuleId = Character.toString(executedModulesId.charAt(i));
			if (moduleDetailMap.containsKey(executedModuleId)) {
				
				// Change display of module/plugin keywords to camelcase in batch progress bar.
				String moduleDescription = moduleDetailMap.get(executedModuleId);
				if (null != moduleDescription) {
					moduleDescription = moduleDescription.replace(CoreCommonConstant.MODULE_KEYWORD_LOWER_CASE,
							CoreCommonConstant.MODULE_KEYWORD_CAMEL_CASE);
				}
				moduleDescriptionList.add(moduleDescription);
			}
			i += 2;
		}
		view.initializeProgressBars(moduleDescriptionList);
		getDetailsOfBatch(selectedBatchInstance);
	}
}
