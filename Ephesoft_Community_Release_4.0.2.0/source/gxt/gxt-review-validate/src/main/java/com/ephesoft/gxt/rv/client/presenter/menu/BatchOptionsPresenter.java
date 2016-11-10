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

package com.ephesoft.gxt.rv.client.presenter.menu;

import java.util.List;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleConstant;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleMessage;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocumentMergeEvent;
import com.ephesoft.gxt.rv.client.event.DocumentOpenEvent;
import com.ephesoft.gxt.rv.client.event.DocumentTypeChangeEvent;
import com.ephesoft.gxt.rv.client.event.NextBatchOpenEvent;
import com.ephesoft.gxt.rv.client.event.PluginPropertiesLoadEvent;
import com.ephesoft.gxt.rv.client.event.ReviewValidateBatchStartEvent;
import com.ephesoft.gxt.rv.client.event.TableValidationEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.InitializeNextBatchOpenEvent;
import com.ephesoft.gxt.rv.client.presenter.ReviewValidateBasePresenter;
import com.ephesoft.gxt.rv.client.util.RVDialogUtil;
import com.ephesoft.gxt.rv.client.view.menu.BatchOptionsView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.RVConfirmationDialog;
import com.ephesoft.gxt.rv.client.widget.ThumbnailWidgetPanel;
import com.ephesoft.gxt.rv.shared.metadata.PluginPropertiesMetaData;
import com.ephesoft.gxt.rv.shared.metadata.ReviewValidateMetaData;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;

public class BatchOptionsPresenter extends ReviewValidateBasePresenter<BatchOptionsView> {

	interface CustomEventBinder extends EventBinder<BatchOptionsPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public BatchOptionsPresenter(final ReviewValidateController controller, final BatchOptionsView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
	}

	public void performNextBatchOpenOperation() {

		final int count = ReviewValidateNavigator.getAlteredDocumentCount();
		if (count > 0) {
			final RVConfirmationDialog confirmationDialog = RVDialogUtil.showConfirmationDialog(LocaleConstant.OPEN_NEXT_BATCH_HEADER,
					LocaleMessage.UNSAVED_CHANGES_MESSAGE);
			confirmationDialog.setOkButtonText("Save");
			confirmationDialog.setCancelButtonText("Discard");
			WidgetUtil.setID(confirmationDialog, "rv-next-batch-dialog");
			confirmationDialog.setUserRecordingIdentfier("rv-next-batch-dialog");
			confirmationDialog.setResponseRecordingButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
			confirmationDialog.setDialogListener(new DialogAdapter() {

				@Override
				public void onOkClick() {
					controller.saveBatch();
					ReviewValidateEventBus.fireEvent(new NextBatchOpenEvent());
				}

				@Override
				public void onCancelClick() {
					ReviewValidateEventBus.fireEvent(new NextBatchOpenEvent());
				}
			});
		} else {
			ReviewValidateEventBus.fireEvent(new NextBatchOpenEvent());
		}
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void handleBatchReviewValidateOperation() {
		final BatchInstanceStatus currentBatchInstanceStatus = ReviewValidateNavigator.getBatchInstanceStatus();
		if (currentBatchInstanceStatus == BatchInstanceStatus.READY_FOR_REVIEW
				|| currentBatchInstanceStatus == BatchInstanceStatus.READY_FOR_VALIDATION) {
			ReviewValidateEventBus.fireEvent(new ReviewValidateBatchStartEvent(ReviewValidateNavigator.doReviewValidateSave(false)));
		}
	}

	@EventHandler
	public void handleNextBatchInitializationEvent(final InitializeNextBatchOpenEvent initializeNextBatchOpenEvent) {
		if (null != initializeNextBatchOpenEvent) {
			performNextBatchOpenOperation();
		}
	}

	@EventHandler
	public void handleDocumentOpenEvent(final DocumentOpenEvent documentOpenEvent) {
		final String header = ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_REVIEW
				? LocaleConstant.REVIEW_MENU_ITEM_TEXT : LocaleConstant.VALIDATE_MENU_ITEM_TEXT;
		view.setReviewValidateMenuHeader(header);
		final ScheduledCommand command = new ScheduledCommand() {

			@Override
			public void execute() {
				scheduleDocumentOpenEventOperation(documentOpenEvent);
			}
		};
		final Scheduler scheduler = new SchedulerImpl();
		scheduler.scheduleDeferred(command);
		if (BatchInstanceStatus.READY_FOR_VALIDATION == ReviewValidateNavigator.getBatchInstanceStatus()) {
			view.setTableView(controller.isTableViewValid());
		}
	}

	public void scheduleDocumentOpenEventOperation(final DocumentOpenEvent documentOpenEvent) {
		boolean elementAdded = false;
		if (null != documentOpenEvent) {
			final ThumbnailWidgetPanel documentThumbnail = documentOpenEvent.getDocumentThumbnail();
			if (documentThumbnail != null) {
				final Document bindedDocument = documentThumbnail.getDocument();
				if (null != bindedDocument) {
					view.clearMergeMenuItem();
					final List<String> documentIdentifierList = ReviewValidateNavigator.getDocumentIdentifiersList();
					if (!CollectionUtil.isEmpty(documentIdentifierList)) {
						final String currentDocumentIdentifier = bindedDocument.getIdentifier();
						String documentName;
						String menuItemtext;
						ScheduledCommand mergeDocumentCommand;
						for (final String documentIdentifier : documentIdentifierList) {
							if (!currentDocumentIdentifier.equals(documentIdentifier)) {
								documentName = ReviewValidateNavigator.getDocumentName(documentIdentifier);
								menuItemtext = StringUtil.concatenate(documentIdentifier, "-", documentName);
								mergeDocumentCommand = getMergeDocumentScheduledCommand(documentIdentifier, currentDocumentIdentifier);
								elementAdded = true;
								view.addMergeMenu(menuItemtext, mergeDocumentCommand);
							}
						}
					}
				}
			}
		}
		view.setEnableMergeOptions(elementAdded);
	}

	private ScheduledCommand getMergeDocumentScheduledCommand(final String documentToMergeWithIdentifier,
			final String currentDocumentIdentifier) {
		return new ScheduledCommand() {

			@Override
			public void execute() {
				if (!StringUtil.isNullOrEmpty(documentToMergeWithIdentifier)
						&& !StringUtil.isNullOrEmpty(documentToMergeWithIdentifier)) {
					ReviewValidateEventBus.fireEvent(new DocumentMergeEvent(currentDocumentIdentifier, documentToMergeWithIdentifier));
				}
			}
		};
	}

	@EventHandler
	public void enableTablesOnDocumentOpen(final DocumentOpenEvent documentOpenEvent) {
		if (null != documentOpenEvent) {
			final ThumbnailWidgetPanel documentPanel = documentOpenEvent.getDocumentThumbnail();
			if (null != documentPanel) {
				final Document document = documentPanel.getDocument();
				final int totalTables = BatchSchemaUtil.getDataTableCount(document);
				if (totalTables > 0) {
					view.setEnableTableViewSwitch(true);
				} else {
					view.setEnableTableViewSwitch(false);
				}
			}
		}
	}

	@EventHandler
	public void handleDocumentTypeChangeEvent(final DocumentTypeChangeEvent documentTypeChangeEvent) {
		if (null != documentTypeChangeEvent) {
			// final Document changedDocument = documentTypeChangeEvent.getDocumentChanged();
			// final String documentName = changedDocument == null ? null : changedDocument.getType();
			// if (!StringUtil.isNullOrEmpty(documentName)) {
			// final String batchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
			// final DocumentTypeDTO documentType = ReviewValidateNavigator.getDocumentTypeByName(documentName);
			// if (null == documentType) {
			// addFunctionKeyForUnloadedDocument(batchInstanceIdentifier, documentName);
			// } else {
			// addFunctionKeys(documentType);
			// }
			//
			// }
			if (BatchInstanceStatus.READY_FOR_VALIDATION == ReviewValidateNavigator.getBatchInstanceStatus()) {
				view.setTableView(controller.isTableViewValid());
			}
		}
	}

	public void reRenderView() {
		rpcService.getReviewValidateMetaData(ReviewValidateNavigator.getCurrentBatchInstanceIdentifier(),
				new AsyncCallback<ReviewValidateMetaData>() {

					@Override
					public void onFailure(final Throwable caught) {

					}

					@Override
					public void onSuccess(final ReviewValidateMetaData result) {
						controller.renderView(result);
					}
				});
	}

	@EventHandler
	public void handlePluginPropertiesLoadEvent(final PluginPropertiesLoadEvent pluginPropertiesLoadEvent) {
		final ScheduledCommand command = new ScheduledCommand() {

			@Override
			public void execute() {
				if (null != pluginPropertiesLoadEvent) {
					final String batchInstanceIdentifier = pluginPropertiesLoadEvent.getBatchInstanceIdentfier();
					final BatchInstanceStatus batchStatus = pluginPropertiesLoadEvent.getBatchInstanceStatus();
					fectchPluginConfigurations(batchInstanceIdentifier, batchStatus);
				}
			}
		};
		final Scheduler scheduler = new SchedulerImpl();
		scheduler.scheduleDeferred(command);
	}

	private void fectchPluginConfigurations(final String batchInstanceIdentifier, final BatchInstanceStatus batchStatus) {
		rpcService.getPluginConfigurations(batchInstanceIdentifier, batchStatus, new AsyncCallback<PluginPropertiesMetaData>() {

			@Override
			public void onFailure(final Throwable caught) {

			}

			@Override
			public void onSuccess(final PluginPropertiesMetaData result) {
				if (null != result) {
					ReviewValidateNavigator.merge(result);
				}
			}
		});
	}

	public void invalidateBatchAndOpenURL(final String urlToOpen) {
		rpcService.invalidate(ReviewValidateNavigator.getCurrentBatchInstanceIdentifier(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(final Throwable caught) {
			}

			@Override
			public void onSuccess(final Void result) {
				Window.Location.assign(urlToOpen);
			}
		});
	}

	@EventHandler
	public void handleTableValidationChangeEvent(TableValidationEvent tableValidationEvent) {
		if (null != tableValidationEvent) {
			view.setTableView(tableValidationEvent.isValid());
		}
	}

}
