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

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DefaultDocumentOpenEvent;
import com.ephesoft.gxt.rv.client.event.DocumentMergeEvent;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.event.DocumentOpenEvent;
import com.ephesoft.gxt.rv.client.event.DocumentReRenderEvent;
import com.ephesoft.gxt.rv.client.event.DocumentReloadEvent;
import com.ephesoft.gxt.rv.client.event.DocumentRemovalEvent;
import com.ephesoft.gxt.rv.client.event.DocumentSelectionEvent;
import com.ephesoft.gxt.rv.client.event.DocumentSplitEndHandler;
import com.ephesoft.gxt.rv.client.event.DocumentTraversalEvent;
import com.ephesoft.gxt.rv.client.event.DocumentTreeCreationEvent;
import com.ephesoft.gxt.rv.client.event.DocumentTypeChangeEvent;
import com.ephesoft.gxt.rv.client.event.DragImageEvent;
import com.ephesoft.gxt.rv.client.event.FieldSelectionEvent;
import com.ephesoft.gxt.rv.client.event.NextBatchOpenEvent;
import com.ephesoft.gxt.rv.client.event.ReviewBatchEndEvent;
import com.ephesoft.gxt.rv.client.event.TreeItemSelectionEvent;
import com.ephesoft.gxt.rv.client.event.ValidateBatchEvent;
import com.ephesoft.gxt.rv.client.view.DocumentTreeView;
import com.ephesoft.gxt.rv.client.view.ReviewDetailView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.ThumbnailWidgetPanel;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class DocumentTreePresenter extends ReviewValidateBasePresenter<DocumentTreeView> {

	interface CustomEventBinder extends EventBinder<DocumentTreePresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public DocumentTreePresenter(final ReviewValidateController controller, final DocumentTreeView view) {
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
	public void handleDocumentTreeCreationEvent(final DocumentTreeCreationEvent documentTreeCreationEvent) {
		if (null != documentTreeCreationEvent) {
			final List<String> documentIdentifiersList = documentTreeCreationEvent.getDocumentIdentifiersList();
			view.createDocumentTree(documentIdentifiersList);
			view.setEnableDragImageSwitch(ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_VALIDATION);
		}
	}

	@EventHandler
	public void handleNextBatchOpenEvent(NextBatchOpenEvent nextBatchOpenEvent) {
		if (null != nextBatchOpenEvent) {
			view.resetView();
		}
	}

	@EventHandler
	public void handleTreeItemSelectionEvent(final TreeItemSelectionEvent selectionEvent) {
		if (null != selectionEvent) {
			final ThumbnailWidgetPanel associatedPanel = selectionEvent.getBindedThumbnailPanel();
			if (associatedPanel.getDocument() == null) {
				this.loadThumbnail(associatedPanel, true);
			} else {
				validateAndLoadDocumentTypeForDocument(associatedPanel);
			}
		}
	}

	public void loadThumbnail(final ThumbnailWidgetPanel thumbnailPanel) {
		this.loadThumbnail(thumbnailPanel, false);
	}

	@EventHandler
	public void handleDocumentSelectionEvent(DocumentSelectionEvent documentSelectionEvent) {
		if (null != documentSelectionEvent) {
			String documentIdentifier = documentSelectionEvent.getDocumentIdentifier();
			view.openDocument(documentIdentifier);
		}
	}

	public void loadThumbnail(final ThumbnailWidgetPanel thumbnailPanel, final boolean openOnLoad) {
		if (null != thumbnailPanel) {
			final String documentIdentifier = thumbnailPanel.getDocIdentifier();
			rpcService.getDocument(ReviewValidateNavigator.getCurrentBatchInstanceIdentifier(), documentIdentifier,
					new AsyncCallback<Document>() {

						@Override
						public void onSuccess(final Document result) {
							if (result != null) {
								thumbnailPanel.setUnloadedDocument(result);
								ReviewValidateNavigator.updateDocumentOnLoad(result);
								thumbnailPanel.setVisible(true);
								if (openOnLoad) {
									validateAndLoadDocumentTypeForDocument(thumbnailPanel);
								}
							}
						}

						@Override
						public void onFailure(final Throwable caught) {
						}
					});
		}
	}

	@EventHandler
	public void handleDefaultDocumentOpenEvent(final DefaultDocumentOpenEvent defaultDocumentOpenEvent) {
		if (defaultDocumentOpenEvent != null) {
			final String documentIdentifierToOpen = ReviewValidateNavigator.getDefaultDocumentIdentifierToOpen();
			view.openDocument(documentIdentifierToOpen);
		}
	}

	@EventHandler
	public void handleDocumentSplitEndEventHandler(final DocumentSplitEndHandler documentSplitEndEvent) {
		if (documentSplitEndEvent != null) {
			final Document splittedDocument = documentSplitEndEvent.getSplittedDocument();
			final Document addedDocument = documentSplitEndEvent.getNewDocumentAdded();
			if (splittedDocument != null && addedDocument != null) {
				final ThumbnailWidgetPanel widgetPanel = new ThumbnailWidgetPanel(addedDocument.getIdentifier());
				widgetPanel.setDocument(addedDocument);
				int index = ReviewValidateNavigator.getDocumentIndex(splittedDocument.getIdentifier());
				view.addThumbnail(widgetPanel, ++index);
				widgetPanel.setVisible(true);
				view.openDocument(addedDocument.getIdentifier());
				view.refreshDocument(splittedDocument.getIdentifier());
			}
		}
	}

	@EventHandler
	public void handleDocumentReloadEvent(final DocumentReloadEvent documentUpdationEvent) {
		if (documentUpdationEvent != null) {
			rpcService.getDocument(ReviewValidateNavigator.getCurrentBatchInstanceIdentifier(),
					documentUpdationEvent.getDocumentIdentifer(), new AsyncCallback<Document>() {

						@Override
						public void onFailure(final Throwable caught) {
						}

						@Override
						public void onSuccess(final Document document) {
							ReviewValidateNavigator.setCurrentDocument(document);
							ReviewValidateNavigator.updateDocumentOnLoad(document,true);
							ReviewValidateEventBus.fireEvent(new DocumentReRenderEvent(document, documentUpdationEvent
									.getPageIndexToSelect()));
						}
					});
		}
	}

	@EventHandler
	public void handleDocumentReRenderEvent(final DocumentReRenderEvent documentReRenderEvent) {
		if (documentReRenderEvent != null) {
			final Document updatedDocument = documentReRenderEvent.getDocumentUpdated();
			view.updateDocument(updatedDocument);
		}
	}

	@EventHandler
	public void handleDocumentMergeEvent(final DocumentMergeEvent documentMergeEvent) {
		if (null != documentMergeEvent) {
			final String documentToMergeIdentifier = documentMergeEvent.getDocumentToMergeIdentifier();
			final String documentToMergeWithIdentifier = documentMergeEvent.getDocumentToMergeWithIdentifier();
			this.loadAndMergeDocument(documentToMergeWithIdentifier, documentToMergeIdentifier);
		}
	}

	public void loadAndMergeDocument(final String documentIDToMergeWith, final String documentToMerge) {
		if (!StringUtil.isNullOrEmpty(documentToMerge) && !StringUtil.isNullOrEmpty(documentIDToMergeWith)) {
			final ThumbnailWidgetPanel mergeWithDocumentPanel = view.getThumbnailStackPanel(documentIDToMergeWith);
			final Document bindedMergeWithDocument = mergeWithDocumentPanel.getDocument();
			if (null == bindedMergeWithDocument) {
				rpcService.getDocument(ReviewValidateNavigator.getCurrentBatchInstanceIdentifier(), documentIDToMergeWith,
						new AsyncCallback<Document>() {

							@Override
							public void onFailure(final Throwable caught) {
							}

							@Override
							public void onSuccess(final Document result) {
								mergeWithDocumentPanel.setDocument(result);
								ReviewValidateNavigator.updateDocumentOnLoad(result);
								mergeLoadedDocuments(documentIDToMergeWith, documentToMerge);
							}
						});
			} else {
				mergeLoadedDocuments(documentIDToMergeWith, documentToMerge);
			}
		}
	}

	private void mergeLoadedDocuments(final String documentIDToMergeWith, final String documentIDToMerge) {
		final Document documentToMerge = ReviewValidateNavigator.getLoadedDocumentByIdentifier(documentIDToMerge);
		final Document documentToMergeWith = ReviewValidateNavigator.getLoadedDocumentByIdentifier(documentIDToMergeWith);
		final List<Page> documentToMergePageList = BatchSchemaUtil.getDocumentPageList(documentToMerge);
		final List<Page> documentToMergeWithPageList = BatchSchemaUtil.getDocumentPageList(documentToMergeWith);
		if (null != documentToMergePageList && null != documentToMergeWithPageList) {
			documentToMergeWithPageList.addAll(documentToMergePageList);
			ReviewValidateEventBus.fireEvent(new DocumentRemovalEvent(documentIDToMerge));
			ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(documentToMergeWith));
			view.openDocument(documentIDToMergeWith);
			view.refreshDocument(documentIDToMergeWith);
		}
	}

	@EventHandler
	public void handleDocumentRemovalEvent(final DocumentRemovalEvent documentRemovalEvent) {
		if (null != documentRemovalEvent) {
			final String documentIdentifier = documentRemovalEvent.getDocumentIdentifierToRemove();
			view.removeDocument(documentIdentifier);
			ReviewValidateNavigator.removeDocument(documentIdentifier);
		}
	}

	@EventHandler
	public void handleDocumentTypeChangeEvent(final DocumentTypeChangeEvent documentTypeChangeEvent) {
		if (null != documentTypeChangeEvent) {
			final Document changedDocument = documentTypeChangeEvent.getDocumentChanged();
			if (null != changedDocument) {
				view.setValidDocumentType(changedDocument.getIdentifier(), false);
				view.setDocumentType(changedDocument.getIdentifier(), changedDocument.getType());
			}
		}
	}

	@EventHandler
	public void handleReviewBatchEndEvent(final ReviewBatchEndEvent reviewBatchEndEvent) {
		if (null != reviewBatchEndEvent) {
			final String currentDocumentIdentifier = reviewBatchEndEvent.getReviewedDocumentIdentifier();
			view.setValidDocumentType(currentDocumentIdentifier, ReviewValidateNavigator.isDocumentValid(currentDocumentIdentifier));
		}
	}

	@EventHandler
	public void handleValidateBatchEndEvent(final ValidateBatchEvent validateBatchEndEvent) {
		if (null != validateBatchEndEvent) {
			final String validatedDocumentIdentifier = validateBatchEndEvent.getValidatedDocumentIdentifier();
			view.setValidDocumentType(validatedDocumentIdentifier,
					ReviewValidateNavigator.isDocumentValid(validatedDocumentIdentifier));
		}
	}

	@EventHandler
	public void handleDragImageEvent(final DragImageEvent dragImageEvent) {
		if (null != dragImageEvent) {
			view.refreshDocument(dragImageEvent.getDocumentIdentifier());
		}
	}

	public ReviewDetailView getReviewDetailView() {
		return controller.getReviewDetailView();
	}

	private void validateAndLoadDocumentTypeForDocument(final ThumbnailWidgetPanel associatedPanel) {
		final Document loadedDoc = associatedPanel.getDocument();
		if (null != loadedDoc) {
			final String docTypeName = loadedDoc.getType();
			if (!StringUtil.isNullOrEmpty(docTypeName)) {
				final DocumentTypeDTO docType = ReviewValidateNavigator.getDocumentTypeByName(docTypeName);
				if (null == docType) {
					loadDocumentTypeByName(docTypeName, associatedPanel);
				} else {
					ReviewValidateEventBus.fireEvent(new DocumentOpenEvent(associatedPanel));
				}
			}
		}
	}

	private void loadDocumentTypeByName(final String docTypeName, final ThumbnailWidgetPanel associatedPanel) {
		ScreenMaskUtility.maskScreen();
		rpcService.getDocumentType(ReviewValidateNavigator.getCurrentBatchInstanceIdentifier(), docTypeName,
				new AsyncCallback<DocumentTypeDTO>() {

					@Override
					public void onFailure(final Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
					}

					@Override
					public void onSuccess(final DocumentTypeDTO result) {
						ScreenMaskUtility.unmaskScreen();
						ReviewValidateNavigator.addDocumentType(result);
						ReviewValidateEventBus.fireEvent(new DocumentOpenEvent(associatedPanel));
					}
				});
	}

	@EventHandler
	public void adjustScrollOnDocumentOpenEvent(final DocumentOpenEvent documentOpenEvent) {
		if (null != documentOpenEvent) {
			final ScheduledCommand command = new ScheduledCommand() {

				@Override
				public void execute() {
					view.fireScrollEventToLoadDocument();
				}
			};
			final Scheduler scheduler = new SchedulerImpl();
			scheduler.scheduleDeferred(command);
		}
	}

	@EventHandler
	public void handleDocumentTraversalEvent(final DocumentTraversalEvent documentTraversalEvent) {
		ScreenMaskUtility.maskScreen();
		if (null != documentTraversalEvent) {
			Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			String documentIdentifierToOpen = null;
			String currentDocumentIdentifier = currentDocument.getIdentifier();
			if (null != currentDocument) {
				if (documentTraversalEvent.isPrevious()) {
					documentIdentifierToOpen = ReviewValidateNavigator.getPreviousDocumentIdentifier(currentDocumentIdentifier);
				} else {
					documentIdentifierToOpen = ReviewValidateNavigator.getNextDocumentIdentifier(currentDocumentIdentifier);
				}
				view.openDocument(documentIdentifierToOpen);
			}
		}
		ScreenMaskUtility.unmaskScreen();
	}

	@EventHandler
	public void handleFieldSelectionEvent(final FieldSelectionEvent fieldSelectionEvent) {
		if (null != fieldSelectionEvent) {
			Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			view.scrollInToView(currentDocument);
		}
	}
}
