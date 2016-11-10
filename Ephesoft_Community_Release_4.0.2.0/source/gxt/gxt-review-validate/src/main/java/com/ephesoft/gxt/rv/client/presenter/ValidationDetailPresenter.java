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

package com.ephesoft.gxt.rv.client.presenter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.StickyDocumentDataDTO;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleConstant;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleMessage;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DefaultDocumentOpenEvent;
import com.ephesoft.gxt.rv.client.event.DocFieldValidationChangeEvent;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.event.DocumentOpenEvent;
import com.ephesoft.gxt.rv.client.event.DocumentSelectionEvent;
import com.ephesoft.gxt.rv.client.event.DocumentTypeChangeEvent;
import com.ephesoft.gxt.rv.client.event.FieldSelectionChangeEvent;
import com.ephesoft.gxt.rv.client.event.FocusInitializationEvent;
import com.ephesoft.gxt.rv.client.event.ReviewBatchEndEvent;
import com.ephesoft.gxt.rv.client.event.ValidateBatchEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.FieldTraversalEvent;
import com.ephesoft.gxt.rv.client.util.RVDialogUtil;
import com.ephesoft.gxt.rv.client.view.ValidationDetailView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.RVConfirmationDialog;
import com.ephesoft.gxt.rv.client.widget.ThumbnailWidgetPanel;
import com.ephesoft.gxt.rv.shared.metadata.ReviewValidateMetaData;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class ValidationDetailPresenter extends ReviewValidateBasePresenter<ValidationDetailView> {

	interface CustomEventBinder extends EventBinder<ValidationDetailPresenter> {
	}

	private boolean setFocusOnField = true;

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public ValidationDetailPresenter(final ReviewValidateController controller, final ValidationDetailView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void handleDocumentOpenEvent(final DocumentOpenEvent documentOpenEvent) {
		if (null != documentOpenEvent && ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_VALIDATION) {
			final ThumbnailWidgetPanel thumbnailWidgetPanel = documentOpenEvent.getDocumentThumbnail();
			if (null != thumbnailWidgetPanel) {
				final Document openedDocument = thumbnailWidgetPanel.getDocument();
				if (null != openedDocument) {
					this.bindDocumentOnView(openedDocument);
				}
			}
		}
	}

	public void bindDocumentOnView(final Document document) {
		view.clear();
		if (null != document) {
			final Map<String, List<DocField>> categoryMap = BatchSchemaUtil.getDLFCategoryMap(document);
			if (null != categoryMap) {
				final Set<Entry<String, List<DocField>>> entrySet = categoryMap.entrySet();
				for (final Entry<String, List<DocField>> entry : entrySet) {
					List<DocField> docFieldsList = entry.getValue();
					int nonHiddenFieldsCount = BatchSchemaUtil.getNonHiddenFieldsCount(docFieldsList);
					if (nonHiddenFieldsCount > 0) {
						view.addCategory(entry.getKey(), docFieldsList);
					}
				}
				ReviewValidateEventBus.fireEvent(new FocusInitializationEvent());
			}
		}
	}

	@EventHandler
	public void handleDocFieldValidationChangeEvent(final DocFieldValidationChangeEvent validationChangeEvent) {
		if (null != validationChangeEvent) {
			final DocField bindedDocField = validationChangeEvent.getDocField();
			if (null != bindedDocField) {
				view.validateListPanel(bindedDocField);
			}
		}
	}

	@EventHandler
	public void handleReviewBatchEndEvent(final ReviewBatchEndEvent reviewBatchEndEvent) {
		if (null != reviewBatchEndEvent
				&& ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_VALIDATION) {
			final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			String currentBatchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
			String batchClassIdentifier = ReviewValidateNavigator.getBatchClassIdentifier();

			boolean isValid = true;

			try {
				isValid = validate(currentBatchInstanceIdentifier, currentDocument.getType(), batchClassIdentifier);
			} catch (final Throwable exception) {
				isValid = false;
			}
			if (null != currentDocument && isValid) {
				Field currentField = ReviewValidateNavigator.getCurrentField();
				if (null != currentField) {
					float ocrConfidence = currentField.getOcrConfidence();
					float ocrConfidenceThreshold = currentField.getOcrConfidenceThreshold();
					if (ocrConfidence < ocrConfidenceThreshold) {
						currentField.setOcrConfidence(100.0f);
					}
				}
				currentDocument.setValid(view.isValid());
				final boolean isBatchValid = ReviewValidateNavigator.validateBatch(currentDocument);
				ReviewValidateEventBus.fireEvent(new ValidateBatchEvent(currentDocument.getIdentifier()));
				final DocField lastSelectedDocField = ReviewValidateNavigator.getCurrentSelectedDocField();
				ReviewValidateNavigator.setCurrentSelectedDocField(null, false);
				ReviewValidateNavigator.addAlteredDocument(currentDocument);

				afterValidationOperation(isBatchValid);
				if (reviewBatchEndEvent.isSaveBatch()) {
					controller.saveBatch();
				}
			}
		}
	}

	private void performReValidationOperation(final ReviewValidateMetaData reviewValidateMetadata) {
		controller.renderView(reviewValidateMetadata);
		ReviewValidateNavigator.resetAlteredDocuments();
		final boolean isBatchValid = ReviewValidateNavigator.validateBatch(null);
		afterValidationOperation(isBatchValid);
	}

	private void afterValidationOperation(final boolean isValid) {
		if (isValid) {
			RVConfirmationDialog confirmationDialog = RVDialogUtil.showConfirmationDialog(LocaleConstant.VALIDATION_DONE_HEADER,
					LocaleMessage.VALIDATION_CONFIRMATION_MESSAGE);
			confirmationDialog.setUserRecordingIdentfier("rv-validation-confirmationDialog");
			WidgetUtil.setID(confirmationDialog, "rv-validation-confirmationDialog");
			confirmationDialog.setResponseRecordingButtons(PredefinedButton.OK);
			setFocusOnField = false;
			confirmationDialog.setDialogListener(new DialogAdapter() {

				@Override
				public void onOkClick() {
					controller.signalWorkFlow();
				}
			});

			confirmationDialog.addHideHandler(new HideHandler() {

				@Override
				public void onHide(HideEvent event) {
					setFocusOnField = true;
				}
			});
		} else {
			ReviewValidateEventBus.fireEvent(new DefaultDocumentOpenEvent());
		}
	}

	@EventHandler
	public void handleFocusInitializationEvent(final FocusInitializationEvent focusInitializationEvent) {
		if (null != focusInitializationEvent
				&& ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_VALIDATION
				&& !controller.isTableViewVisible()) {
			final ScheduledCommand command = new ScheduledCommand() {

				@Override
				public void execute() {
					if (setFocusOnField) {
						view.setDefaultFocus();
					}
				}
			};
			final Scheduler scheduler = new SchedulerImpl();
			scheduler.scheduleDeferred(command);
		}
	}

	@EventHandler
	public void reValidateDocumentOnOpen(final DocumentOpenEvent documentOpenEvent) {
		if (ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_VALIDATION) {
			final ScheduledCommand command = new ScheduledCommand() {

				@Override
				public void execute() {
					final ThumbnailWidgetPanel thumbnailPanel = documentOpenEvent.getDocumentThumbnail();
					if (null != thumbnailPanel) {
						final Document bindedDocument = thumbnailPanel.getDocument();
						if (null != bindedDocument && bindedDocument.isValid()) {
							bindedDocument.setValid(view.isValid());
							ReviewValidateNavigator.validateBatch(bindedDocument);
							ReviewValidateEventBus.fireEvent(new ValidateBatchEvent(bindedDocument.getIdentifier()));
						}
					}
				}
			};
			final Scheduler scheduler = new SchedulerImpl();
			scheduler.scheduleDeferred(command);
		}
	}

	@EventHandler
	public void handleFieldSelectionChangeEvent(final FieldSelectionChangeEvent fieldSelectionChangeEvent) {
		if (null != fieldSelectionChangeEvent) {
			final DocField lastSelectedDocField = fieldSelectionChangeEvent.getLastSelectedDocField();
		}
	}

	@EventHandler
	public void handleFieldTraversalEvent(final FieldTraversalEvent fieldTraversalEvent) {
		if (null != fieldTraversalEvent) {
			final boolean isErrorField = fieldTraversalEvent.isErrorField();
			view.locateSubSequentErrorField(ReviewValidateNavigator.getCurrentSelectedDocField(), fieldTraversalEvent.isPrevious(),
					isErrorField);
		}
	}

	public boolean isTableViewValid() {
		return controller.isTableViewValid();
	}

	@EventHandler
	public void handleDocumentTypeChangeEvent(final DocumentTypeChangeEvent documentTypeChangeEvent) {
		if (null != documentTypeChangeEvent
				&& BatchInstanceStatus.READY_FOR_VALIDATION == ReviewValidateNavigator.getBatchInstanceStatus()) {
			final String batchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
			final Document changedDocument = documentTypeChangeEvent.getDocumentChanged();
			final String documentName = changedDocument == null ? CoreCommonConstant.EMPTY_STRING : changedDocument.getType();
			rpcService.getFdTypeByDocTypeName(batchInstanceIdentifier, documentName, new AsyncCallback<Document>() {

				@Override
				public void onFailure(final Throwable caught) {
					ScreenMaskUtility.unmaskScreen();
				}

				@Override
				public void onSuccess(final Document result) {
					if (null != result && null != changedDocument) {
						DocumentLevelFields documentLevelFields = result.getDocumentLevelFields();
						updateStickyFields(changedDocument, documentLevelFields);
						changedDocument.setDocumentLevelFields(documentLevelFields);
						changedDocument.setDataTables(result.getDataTables());
						changedDocument.setConfidence(result.getConfidence());
						changedDocument.setConfidenceThreshold(result.getConfidenceThreshold());
						changedDocument.setDescription(result.getDescription());
						final DocumentTypeDTO documentTypeDTO = ReviewValidateNavigator.getDocumentTypeByName(documentName);
						if (null != documentTypeDTO) {
							reRenderDocument(changedDocument);
						} else {
							loadChangedDocument(documentName, changedDocument);
						}
					}
				}
			});
		}
	}

	private void updateStickyFields(final Document changedDocument, final DocumentLevelFields documentLevelFields) {
		if (ReviewValidateNavigator.isStickyIndexFieldEnable() && changedDocument != null && documentLevelFields != null) {
			StickyDocumentDataDTO stickyDocumentDataDTO = ReviewValidateNavigator.getStickyDocumentData(
					changedDocument.getIdentifier(), changedDocument);
			BatchSchemaUtil.updateIndexFields(documentLevelFields, stickyDocumentDataDTO);
		}
	}

	private void loadChangedDocument(final String documentTypeName, final Document changedDocument) {
		final String batchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
		rpcService.getDocumentType(batchInstanceIdentifier, documentTypeName, new AsyncCallback<DocumentTypeDTO>() {

			@Override
			public void onFailure(final Throwable caught) {

			}

			@Override
			public void onSuccess(final DocumentTypeDTO result) {
				if (null != result) {
					ReviewValidateNavigator.addDocumentType(result);
					reRenderDocument(changedDocument);
				}
			}
		});
	}

	private void reRenderDocument(final Document changedDocument) {
		// this.bindDocumentOnView(changedDocument);
		ReviewValidateEventBus.fireEvent(new DocumentSelectionEvent(changedDocument.getIdentifier()));
		ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(changedDocument));
	}

	private static native boolean validate(final String batchInstanceIdentifier, final String documentName,
			final String batchClassIdentifer) /*-{
												return $wnd.documentvalidate(batchInstanceIdentifier,documentName, batchClassIdentifer);
												}-*/;
}
