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

import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.client.DragDropFlowPanel.DragImage;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.constant.ReviewValidateConstant;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleConstant;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleMessage;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.event.DocumentOpenEvent;
import com.ephesoft.gxt.rv.client.event.DocumentReRenderEvent;
import com.ephesoft.gxt.rv.client.event.DocumentSplitEndHandler;
import com.ephesoft.gxt.rv.client.event.DocumentSplitStartEvent;
import com.ephesoft.gxt.rv.client.event.DocumentTypeChangeEvent;
import com.ephesoft.gxt.rv.client.event.ImageRotationEndEvent;
import com.ephesoft.gxt.rv.client.event.PageSelectionEvent;
import com.ephesoft.gxt.rv.client.event.ReviewBatchEndEvent;
import com.ephesoft.gxt.rv.client.listener.ReviewDragImageSelectionHandler;
import com.ephesoft.gxt.rv.client.view.ReviewDetailView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.ThumbnailWidgetPanel;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class ReviewDetailPresenter extends ReviewValidateBasePresenter<ReviewDetailView> {

	interface CustomEventBinder extends EventBinder<ReviewDetailPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public ReviewDetailPresenter(final ReviewValidateController controller, final ReviewDetailView view) {
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
		if (null != documentOpenEvent) {
			view.clearThumbnailView();
			final ThumbnailWidgetPanel widgetPanel = documentOpenEvent.getDocumentThumbnail();
			final Document selectedDocument = widgetPanel == null ? null : widgetPanel.getDocument();
			if (null != selectedDocument) {
				ReviewValidateNavigator.setCurrentDocument(selectedDocument);
				openDocument(selectedDocument);
			}
		}
	}

	@EventHandler
	public void handleDocumentReRenderEvent(final DocumentReRenderEvent documentUpdationEndEvent) {
		if (null != documentUpdationEndEvent) {
			final Document updatedDocument = documentUpdationEndEvent.getDocumentUpdated();
			if (null != updatedDocument && updatedDocument == ReviewValidateNavigator.getCurrentDocument()) {
				int pageIndexToOpen = documentUpdationEndEvent.getPageIndexToOpen();
				if (pageIndexToOpen == ReviewValidateConstant.LAST_PAGE_SELECTION_INDEX) {
					pageIndexToOpen = BatchSchemaUtil.getPageCount(updatedDocument) - 1;
				}
				this.openDocument(updatedDocument, pageIndexToOpen);
			}
		}
	}

	private void openDocument(final Document document) {
		this.openDocument(document, 0);
	}

	private void openDocument(final Document document, final int pageIndexToOpen) {
		view.clearThumbnailView();
		if (null != document) {
			final List<String> thumbnailNamesList = BatchSchemaUtil.getThumbnails(document);
			if (!CollectionUtil.isEmpty(thumbnailNamesList)) {
				String imageURL = null;
				DragImage thumbnailImage = null;
				int pageIndex = 0;
				for (final String thumbnailName : thumbnailNamesList) {
					if (!StringUtil.isNullOrEmpty(thumbnailName)) {
						imageURL = ReviewValidateNavigator.getAbsoluteURL(thumbnailName);
						thumbnailImage = new DragImage(imageURL);
						if (pageIndex == pageIndexToOpen) {
							ReviewDragImageSelectionHandler.selectImage(thumbnailImage);
						}
						pageIndex++;
						view.addImage(thumbnailImage);
					}
				}
			}
		}
	}

	@EventHandler
	public void handleSplitDocumentStartEvent(final DocumentSplitStartEvent documentSplitEvent) {
		if (documentSplitEvent != null) {
			final Document bindedDocument = documentSplitEvent.getBindedDocument();
			final Page fromPage = documentSplitEvent.getFromPage();

			final String pageIdentifier = fromPage == null ? null : fromPage.getIdentifier();
			if (!StringUtil.isNullOrEmpty(pageIdentifier)) {
				final Document newDocument = BatchSchemaUtil.splitDocument(bindedDocument, pageIdentifier,
						documentSplitEvent.isStickFields(), documentSplitEvent.isStickTables(),
						ReviewValidateNavigator.getDocumentIdentifiersList());
				if (null != newDocument) {
					if (ReviewValidateNavigator.addDocument(newDocument, bindedDocument)) {
						Message.display(LocaleConstant.SUCCESS_HEADER, LocaleMessage.SPLIT_OPERATION_DONE);
						ReviewValidateEventBus.fireEvent(new DocumentSplitEndHandler(bindedDocument, newDocument));
						ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(bindedDocument));
						ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(newDocument));
					}
				}
			}
		}
	}

	@EventHandler
	public void handleReviewEndEvent(final ReviewBatchEndEvent reviewBatchEndEvent) {
		if (null != reviewBatchEndEvent && BatchInstanceStatus.READY_FOR_REVIEW == ReviewValidateNavigator.getBatchInstanceStatus()) {
			if (reviewBatchEndEvent.isSaveBatch()) {
				controller.saveBatch();
			}
		}
	}

	@EventHandler
	public void handlePageSelectionEvent(final PageSelectionEvent pageSelectionEvent) {
		if (null != pageSelectionEvent) {
			final Page currentSelectedPage = ReviewValidateNavigator.getCurrentPage();
			final String currentPageIdentifier = currentSelectedPage == null ? null : currentSelectedPage.getIdentifier();
			final String pageIdentifier = pageSelectionEvent.getPageIdentifier();
			if (null == currentPageIdentifier || !currentPageIdentifier.equalsIgnoreCase(pageIdentifier)) {
				view.selectPage(pageIdentifier);
			}
		}
	}

	@EventHandler
	public void handleImageRotationEvent(final ImageRotationEndEvent imageRotationEndEvent) {
		if (null != imageRotationEndEvent) {
			final Direction rotationDirection = imageRotationEndEvent.getRotationDirection();
			final DragImage dragImage = ReviewValidateNavigator.getCurrentSelectedThumbnail();
			final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			if (null != rotationDirection && null != dragImage) {
				final String thumbnailURL = dragImage.getUrl();
				final Page currentPage = ReviewValidateNavigator.getPage(currentDocument, thumbnailURL);
				if (null != currentPage) {
					final String thumbnailName = currentPage.getThumbnailFileName();
					final String absoluteURL = ReviewValidateNavigator.getAbsoluteURL(thumbnailName);
					dragImage.setUrl(absoluteURL, rotationDirection);
				}
			}
		}
	}

	@EventHandler
	public void handleDocumentTypeChangeEvent(DocumentTypeChangeEvent changeEvent) {
		if (null != changeEvent && BatchInstanceStatus.READY_FOR_REVIEW == ReviewValidateNavigator.getBatchInstanceStatus()) {
			final Document documentChanged = changeEvent.getDocumentChanged();
			if (null != documentChanged) {
				String type = documentChanged.getType();
				ScreenMaskUtility.maskScreen();
				rpcService.getDocumentType(ReviewValidateNavigator.getCurrentBatchInstanceIdentifier(), type,
						new AsyncCallback<DocumentTypeDTO>() {

							@Override
							public void onSuccess(DocumentTypeDTO result) {
								ScreenMaskUtility.unmaskScreen();
								if (null != result) {
									documentChanged.setDescription(result.getDescription());
									documentChanged.setConfidenceThreshold(result.getMinConfidenceThreshold());
								}
							}

							@Override
							public void onFailure(Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
							}
						});
			}
		}
	}
}
