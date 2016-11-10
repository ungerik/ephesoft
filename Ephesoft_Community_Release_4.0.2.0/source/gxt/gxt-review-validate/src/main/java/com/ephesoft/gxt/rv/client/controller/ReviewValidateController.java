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

package com.ephesoft.gxt.rv.client.controller;

import java.util.HashMap;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.ReviewValidateServiceAsync;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleConstant;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleMessage;
import com.ephesoft.gxt.rv.client.event.DefaultDocumentOpenEvent;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.event.DocumentTreeCreationEvent;
import com.ephesoft.gxt.rv.client.event.ReviewBatchEndEvent;
import com.ephesoft.gxt.rv.client.event.ReviewValidateBatchStartEvent;
import com.ephesoft.gxt.rv.client.layout.ReviewValidateLayout;
import com.ephesoft.gxt.rv.client.presenter.DocumentTreePresenter;
import com.ephesoft.gxt.rv.client.presenter.ImageOverlayPresenter;
import com.ephesoft.gxt.rv.client.presenter.menu.BatchDetailPresenter;
import com.ephesoft.gxt.rv.client.presenter.menu.BatchOptionsPresenter;
import com.ephesoft.gxt.rv.client.presenter.menu.DocumentOptionsPresenter;
import com.ephesoft.gxt.rv.client.presenter.menu.UserInterfaceMenuPresenter;
import com.ephesoft.gxt.rv.client.util.RVDialogUtil;
import com.ephesoft.gxt.rv.client.view.DocumentTreeView;
import com.ephesoft.gxt.rv.client.view.ImageOverlayView;
import com.ephesoft.gxt.rv.client.view.ReviewDetailView;
import com.ephesoft.gxt.rv.client.view.menu.BatchDetailView;
import com.ephesoft.gxt.rv.client.view.menu.BatchOptionsView;
import com.ephesoft.gxt.rv.client.view.menu.DocumentOptionsView;
import com.ephesoft.gxt.rv.client.view.menu.UserInterfaceMenuView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.RVConfirmationDialog;
import com.ephesoft.gxt.rv.shared.metadata.ReviewValidateMetaData;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;

public class ReviewValidateController extends ReviewValidateSelectionController {

	private final Map<String, Document> documentMap;
	private final ReviewValidateLayout reviewValidateLayout;
	private DocumentTreeView documentTreeView;
	private DocumentTreePresenter documentTreePresenter;
	private ImageOverlayPresenter imageOverlayPresenter;
	private ImageOverlayView imageOverlayView;
	private BatchDetailPresenter batchDetailPresenter;
	private BatchDetailView batchDetailView;
	private BatchOptionsView batchOptionsView;
	private BatchOptionsPresenter batchOptionsPresenter;
	private UserInterfaceMenuView externalInterfaceMenuView;
	private UserInterfaceMenuPresenter externalInterfaceMenuPresenter;
	private DocumentOptionsView documentOptionsView;
	private DocumentOptionsPresenter documentOptionsPresenter;
	private boolean firstLoad = true;

	interface CustomEventBinder extends EventBinder<ReviewValidateController> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public ReviewValidateController(final EventBus eventBus, final ReviewValidateServiceAsync rpcService) {
		super(eventBus, rpcService);
		ReviewValidateEventBus.registerEventBus(eventBus);
		documentMap = new HashMap<String, Document>();
		reviewValidateLayout = new ReviewValidateLayout(this, rpcService);
		ScheduledCommand command = new ScheduledCommand() {

			@Override
			public void execute() {
				initializeView();
				initializePresenter();
			}
		};
		Scheduler scheduler = new SchedulerImpl();
		scheduler.scheduleDeferred(command);
	}

	private void initializeView() {
		documentTreeView = reviewValidateLayout.getDocumentTreeView();
		imageOverlayView = reviewValidateLayout.getImageOverlayPanel();
		batchDetailView = reviewValidateLayout.getBatchDetailView();
		batchOptionsView = reviewValidateLayout.getBatchOptionsView();
		// externalInterfaceMenuView = reviewValidateLayout.getUserInterfaceMenuView();
		documentOptionsView = reviewValidateLayout.getDocumentOptionsView();
	}

	private void initializePresenter() {
		documentTreePresenter = new DocumentTreePresenter(this, documentTreeView);
		imageOverlayPresenter = new ImageOverlayPresenter(this, imageOverlayView);
		batchDetailPresenter = new BatchDetailPresenter(this, batchDetailView);
		batchOptionsPresenter = new BatchOptionsPresenter(this, batchOptionsView);
		// externalInterfaceMenuPresenter = new UserInterfaceMenuPresenter(this, externalInterfaceMenuView);
		documentOptionsPresenter = new DocumentOptionsPresenter(this, documentOptionsView);
	}

	public void showTableView() {

	}

	@Override
	public Widget createView() {
		reviewValidateLayout.renderView();
		return reviewValidateLayout;
	}

	@Override
	public void refresh() {
	}

	public static class ReviewValidateEventBus {

		private static EventBus eventBus;

		private ReviewValidateEventBus() {
		}

		public static void fireEvent(final GwtEvent<?> event) {
			if (null != eventBus) {
				eventBus.fireEvent(event);
			}
		}

		private static void registerEventBus(final EventBus eventBus) {
			ReviewValidateEventBus.eventBus = eventBus;
		}
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void addDocument(final Document document) {
		if (null != document) {
			documentMap.put(document.getIdentifier(), document);
		}
	}

	public Document getDocument(final String docIdentifier) {
		Document requiredDocument = null;
		if (!StringUtil.isNullOrEmpty(docIdentifier)) {
			requiredDocument = documentMap.get(docIdentifier);
		}
		return requiredDocument;
	}

	/**
	 * @return the documentTreePresenter
	 */
	public DocumentTreePresenter getDocumentTreePresenter() {
		return documentTreePresenter;
	}

	/**
	 * @return the imageOverlayPresenter
	 */
	public ImageOverlayPresenter getImageOverlayPresenter() {
		return imageOverlayPresenter;
	}

	/**
	 * @return the batchDetailPresenter
	 */
	public BatchDetailPresenter getBatchDetailPresenter() {
		return batchDetailPresenter;
	}

	/**
	 * @return the batchOptionsPresenter
	 */
	public BatchOptionsPresenter getBatchOptionsPresenter() {
		return batchOptionsPresenter;
	}

	/**
	 * @return the externalInterfaceMenuPresenter
	 */
	public UserInterfaceMenuPresenter getExternalInterfaceMenuPresenter() {
		return externalInterfaceMenuPresenter;
	}

	/**
	 * @return the documentOptionsPresenter
	 */
	public DocumentOptionsPresenter getDocumentOptionsPresenter() {
		return documentOptionsPresenter;
	}

	@EventHandler
	public void handleDocumentModificationEvent(final DocumentModificationEvent documentModificationEvent) {
		if (null != documentModificationEvent) {
			final Document modifiedDocument = documentModificationEvent.getModifiedDocument();
			ReviewValidateNavigator.addAlteredDocument(modifiedDocument);
		}
	}

	@EventHandler
	public void handleReviewBatchEvent(final ReviewValidateBatchStartEvent reviewBatchStartEvent) {
		if (null != reviewBatchStartEvent && ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_REVIEW) {
			final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			final boolean isValidBatch = this.reviewBatch(currentDocument, true);
			if (!isValidBatch) {
				ReviewValidateEventBus.fireEvent(new DefaultDocumentOpenEvent());
			} else {
				RVConfirmationDialog confirmationDialog = RVDialogUtil.showConfirmationDialog(LocaleConstant.REVIEW_DONE,
						LocaleMessage.REVIEW_BATCH_COMPLETED);
				WidgetUtil.setID(confirmationDialog, "rv-validation-confirmationDialog");
				confirmationDialog.setUserRecordingIdentfier("rv-validation-confirmationDialog");
				confirmationDialog.setResponseRecordingButtons(PredefinedButton.OK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						super.onOkClick();
						signalWorkFlow();
					}
				});
			}
			ReviewValidateEventBus.fireEvent(new ReviewBatchEndEvent(reviewBatchStartEvent.isSaveAfterCompletion(), currentDocument
					.getIdentifier()));
		}
	}

	@EventHandler
	public void handleValidationBatchEvent(final ReviewValidateBatchStartEvent validateBatchStartEvent) {
		if (null != validateBatchStartEvent
				&& ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_VALIDATION) {
			final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			this.reviewBatch(currentDocument, false);
			ReviewValidateEventBus.fireEvent(new ReviewBatchEndEvent(validateBatchStartEvent.isSaveAfterCompletion(), currentDocument
					.getIdentifier()));
		}
	}

	private boolean reviewBatch(final Document updatedDocument, final boolean applyValidationOnBatch) {
		boolean isValid = false;
		if (null != updatedDocument) {
			final String documentType = updatedDocument.getType();
			final boolean reviewedDocument = ReviewValidateNavigator.isValidDocumentType(documentType);
			updatedDocument.setReviewed(reviewedDocument);
			ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(updatedDocument));
			isValid = applyValidationOnBatch ? ReviewValidateNavigator.validateBatch(updatedDocument) : reviewedDocument;
			if ("Unknown".equalsIgnoreCase(updatedDocument.getType())) {
				Message.display("Invalid Document Type", "Document cannot have Unknown Document Type.");
			}
		}
		return isValid;
	}

	public ReviewDetailView getReviewDetailView() {
		return reviewValidateLayout.getReviewDetailView();
	}

	public void renderView(final ReviewValidateMetaData reviewValidateMetadata) {
		if (null != reviewValidateMetadata) {
			ReviewValidateNavigator.create(reviewValidateMetadata);
			Timer timer = new Timer() {

				@Override
				public void run() {
					fireTreeCreationEvent();
				}
			};
			timer.schedule(firstLoad ? 15 : 0);
		}
	}

	private void fireTreeCreationEvent() {
		firstLoad = false;
		final DocumentTreeCreationEvent treeCreationEvent = new DocumentTreeCreationEvent(
				ReviewValidateNavigator.getDocumentIdentifiersList());
		ReviewValidateEventBus.fireEvent(treeCreationEvent);
	}

	public boolean isTableViewValid() {
		return reviewValidateLayout.isTableViewValid();
	}

	public boolean isTableViewVisible() {
		return reviewValidateLayout.isTableViewVisible();
	}
	
	public int calculateTableViewWidth() {
		return reviewValidateLayout.calculateTableViewWidth();
	}

}
