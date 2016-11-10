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

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.client.DragDropFlowPanel.DragImage;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.OverlayImage;
import com.ephesoft.gxt.core.client.ui.widget.OverlayImage.Overlay;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.listener.OverlayDrawnListener;
import com.ephesoft.gxt.core.client.ui.widget.property.Validatable;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.PointCoordinate;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.constant.ReviewValidateConstant;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleConstant;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleMessage;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.event.DocumentReRenderEvent;
import com.ephesoft.gxt.rv.client.event.DocumentReloadEvent;
import com.ephesoft.gxt.rv.client.event.DocumentRemovalEvent;
import com.ephesoft.gxt.rv.client.event.DocumentSplitStartEvent;
import com.ephesoft.gxt.rv.client.event.DocumentTraversalEvent;
import com.ephesoft.gxt.rv.client.event.FieldSelectionEvent;
import com.ephesoft.gxt.rv.client.event.ImageRotationEndEvent;
import com.ephesoft.gxt.rv.client.event.ThumbnailSelectionEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.CurrentPageDeletionEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.DuplicateCurrentPageStartEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ImageOverlayFitToPageEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ImageOverlayScrollAdjustEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ImageOverlayScrollAdjustEvent.ScrollDirection;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ImageOverlayZoomEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ImageRotationEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.SplitDocumentInitializationEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ZoomLockEvent;
import com.ephesoft.gxt.rv.client.util.RVDialogUtil;
import com.ephesoft.gxt.rv.client.view.ImageOverlayView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.DataTableGrid;
import com.ephesoft.gxt.rv.client.widget.RVConfirmationDialog;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;

public class ImageOverlayPresenter extends ReviewValidateBasePresenter<ImageOverlayView> {

	interface CustomEventBinder extends EventBinder<ImageOverlayPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private final int DEFAULT_ZOOM_FACTOR = 1;

	private String currentDisplayImageURL;

	private OverlayDrawnListener listener;

	private static final int DEFAULT_SPLIT_DIALOG_BOX_WIDTH = 450;

	public ImageOverlayPresenter(final ReviewValidateController controller, final ImageOverlayView view) {
		super(controller, view);
		listener = new OverlayDrawnListener() {

			@Override
			public void onOverlayDraw(final PointCoordinate startCordinate, final PointCoordinate endCordinate,
					final boolean keepPreviousOverlay) {
				ScreenMaskUtility.maskScreen();
				rpcService.getHOCRContent(startCordinate, endCordinate, ReviewValidateNavigator.getCurrentBatchInstanceIdentifier(),
						ReviewValidateNavigator.getCurrentPage().getHocrFileName(), true, new AsyncCallback<List<Span>>() {

							@Override
							public void onFailure(final Throwable caught) {
								Message.display(LocaleConstant.ERROR_OCCURED_HEADER, LocaleMessage.HOCR_CONTENT_ERROR_MESSAGE);
								ScreenMaskUtility.unmaskScreen();
							}

							@Override
							public void onSuccess(final List<Span> result) {
								if (!CollectionUtil.isEmpty(result)) {
									final String valueToAppend = result.get(0).getValue();
									appendExtractedValue(valueToAppend, startCordinate, endCordinate, keepPreviousOverlay);
								}
								ScreenMaskUtility.unmaskScreen();
							}
						});
			}

			@Override
			public void onPointCordinateClick(final PointCoordinate clickedCoordinate, final boolean keepPreviousOverlay) {
				if (ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_VALIDATION) {
					appendDataForPoint(clickedCoordinate, keepPreviousOverlay);
				}
			}
		};
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void handleThumbnailSelectionEvent(final ThumbnailSelectionEvent thumbnailSelectionEvent) {
		if (thumbnailSelectionEvent != null) {
			Overlay.clearOverlays();
			final DragImage thumbnailImage = thumbnailSelectionEvent.getSelectedThumbnail();
			if (null != thumbnailImage) {
				ReviewValidateNavigator.setCurrentSelectedThumbnail(thumbnailImage);
				view.setPageFitted(false);
				final String thumbnailURL = thumbnailImage.getUrl();
				final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
				currentDisplayImageURL = ReviewValidateNavigator.getDisplayImageURL(currentDocument, thumbnailURL);
				final Page associatedPage = ReviewValidateNavigator.getPage(currentDocument, thumbnailURL);
				final Direction currentDirection = associatedPage == null ? null : associatedPage.getDirection();
				ReviewValidateNavigator.setCurrentPage(associatedPage);
				final OverlayImage displayImage = new OverlayImage();
				displayImage
						.setEnableOverlayDraw(ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_VALIDATION);
				if (null != associatedPage && associatedPage.isIsRotated()) {
					displayImage.setUrl(currentDisplayImageURL, currentDirection);
				} else {
					displayImage.setUrl(currentDisplayImageURL);
				}
				setOverlayDrawnListener(displayImage);
				view.setDisplayImage(displayImage);
			}
		}
	}

	private void setOverlayDrawnListener(final OverlayImage displayImage) {
		displayImage.setOverlayDrawnListener(listener);
	}

	private void appendDataForPoint(final PointCoordinate clickedCoordinate, final boolean keepPreviousOverlay) {
		if (null != clickedCoordinate) {
			final String currentBatchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
			final Page currentPage = ReviewValidateNavigator.getCurrentPage();
			final String hocrFileName = currentPage == null ? null : currentPage.getHocrFileName();
			ScreenMaskUtility.maskScreen();
			rpcService.getHOCRContent(clickedCoordinate, currentBatchInstanceIdentifier, hocrFileName,
					new AsyncCallback<HocrPages.HocrPage.Spans.Span>() {

						@Override
						public void onFailure(final Throwable caught) {
							Message.display(LocaleConstant.ERROR_OCCURED_HEADER, LocaleMessage.HOCR_CONTENT_ERROR_MESSAGE);
							ScreenMaskUtility.unmaskScreen();
						}

						@Override
						public void onSuccess(final Span result) {
							ScreenMaskUtility.unmaskScreen();
							if (null != result) {
								final Coordinates coordinates = result.getCoordinates();
								if (null != coordinates) {
									final BigInteger pointX0 = coordinates.getX0();
									final BigInteger pointX1 = coordinates.getX1();
									final BigInteger pointY0 = coordinates.getY0();
									final BigInteger pointY1 = coordinates.getY1();
									if (null != pointX0 && null != pointX1 && null != pointY0 && null != pointY1) {
										final PointCoordinate startCoordinate = new PointCoordinate(pointX0.intValue(), pointY0
												.intValue());
										final PointCoordinate endCoordinate = new PointCoordinate(pointX1.intValue(), pointY1
												.intValue());
										appendExtractedValue(result.getValue(), startCoordinate, endCoordinate, keepPreviousOverlay);
									}
								}
							}
						}
					});
		}
	}

	public void zoomInDisplayImage() {
		view.zoomInDisplayImage();
	}

	public void zoomOutDisplayImage() {
		view.zoomOutDisplayImage();
	}

	/**
	 * @return the dEFAULT_ZOOM_FACTOR
	 */
	public int getDefaultZoomFactor() {
		return DEFAULT_ZOOM_FACTOR;
	}

	@EventHandler
	public void handleImageRotationEvent(final ImageRotationEvent imageRotationEvent) {
		if (null != imageRotationEvent) {
			view.setZoomLock(false);
			rotateImage();
		}
	}

	@EventHandler
	public void handleZoomLockEvent(final ZoomLockEvent zoomLockEvent) {
		if (null != zoomLockEvent) {
			view.toggleZoomLock();
		}
	}

	public void rotateImage() {
		final String batchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
		final Map<String, Document> alteredDocumentsMap = ReviewValidateNavigator.getAlteredDocumentMap();
		final List<String> documentIdentifiersList = ReviewValidateNavigator.getDocumentIdentifiersList();
		final Page currentPage = ReviewValidateNavigator.getCurrentPage();
		final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
		if (null != currentDocument) {
			ScreenMaskUtility.maskScreen();
			alteredDocumentsMap.put(currentDocument.getIdentifier(), currentDocument);
			rpcService.rotateImage(batchInstanceIdentifier, alteredDocumentsMap, documentIdentifiersList, currentPage,
					currentDocument.getIdentifier(), new AsyncCallback<Page>() {

						@Override
						public void onFailure(final Throwable caught) {
							ScreenMaskUtility.unmaskScreen();
						}

						@Override
						public void onSuccess(final Page result) {
							if (null != result) {
								currentPage.setIsRotated(true);
								final Direction currentDirection = result.getDirection();
								currentPage.setDirection(currentDirection);
								final OverlayImage rotatableOverlayImage = new OverlayImage();
								rotatableOverlayImage.setEnableOverlayDraw(ReviewValidateNavigator.getBatchInstanceStatus() == BatchInstanceStatus.READY_FOR_VALIDATION);
								rotatableOverlayImage.setOverlayDrawnListener(listener);
								rotatableOverlayImage.setUrl(currentDisplayImageURL, currentDirection);
								view.setDisplayImage(rotatableOverlayImage);
								ScreenMaskUtility.unmaskScreen();
								ReviewValidateNavigator.resetAlteredDocuments();
								ReviewValidateEventBus.fireEvent(new ImageRotationEndEvent(currentDirection));
							}
						}
					});
		}
	}

	public void splitDocument() {
		final Page currentPage = ReviewValidateNavigator.getCurrentPage();
		if (null != currentPage) {
			final int pageIndex = BatchSchemaUtil.getPageTypeIndex(ReviewValidateNavigator.getCurrentDocument(),
					currentPage.getIdentifier());
			if (pageIndex < 1) {
				Message.display(LocaleConstant.INVALID_SPLIT_OPERATION_HEADER, LocaleMessage.SPLIT_FIRST_PAGE_ERROR);
			} else {
				startSplitDocumentOperation();
			}
		}
	}

	private void startSplitDocumentOperation() {
		final RVConfirmationDialog confirmationDialog = RVDialogUtil.showConfirmationDialog(LocaleConstant.SPLIT_DOCUMENT_HEADER,
				LocaleMessage.SPLIT_DOCUMENT_CONFIRMATION_MESSAGE);
		confirmationDialog.setRecordingCheckBox();
		final BatchInstanceStatus batchStatus = ReviewValidateNavigator.getBatchInstanceStatus();
		final boolean isStatusReadyForReview = (batchStatus == BatchInstanceStatus.READY_FOR_REVIEW);
		final String okButtonText = isStatusReadyForReview ? "OK" : "Fields";
		final String cancelButtonText = isStatusReadyForReview ? "Cancel" : "Tables";
		final String closeButtonText = isStatusReadyForReview ? "Close" : "Fields and Tables";
		final String yesButtonText = isStatusReadyForReview ? "Yes" : "None";
		final String recordingIdentifier = isStatusReadyForReview ? "review-split-dialog" : "validation-split-dialog";
		WidgetUtil.setID(confirmationDialog, recordingIdentifier);
		confirmationDialog.setOkButtonText(okButtonText);
		confirmationDialog.setCancelButtonText(cancelButtonText);
		confirmationDialog.setCloseButtonText(closeButtonText);
		confirmationDialog.setUserRecordingIdentfier(recordingIdentifier);
		setSplitDialogListener(confirmationDialog, isStatusReadyForReview);
		if (isStatusReadyForReview) {
			confirmationDialog.setResponseRecordingButtons(PredefinedButton.OK);
		} else {
			confirmationDialog.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL, PredefinedButton.CLOSE,
					PredefinedButton.YES);
			confirmationDialog.setResponseRecordingButtons(PredefinedButton.OK, PredefinedButton.CANCEL, PredefinedButton.CLOSE,
					PredefinedButton.YES);
			confirmationDialog.setWidth(DEFAULT_SPLIT_DIALOG_BOX_WIDTH);
			confirmationDialog.setRecordingCheckBox();
			confirmationDialog.setYesButtonText(yesButtonText);
		}
		confirmationDialog.setOkButtonText(okButtonText);
		confirmationDialog.setCancelButtonText(cancelButtonText);
		confirmationDialog.setCloseButtonText(closeButtonText);
	}

	private void setSplitDialogListener(final RVConfirmationDialog confirmationDialog, final boolean isStatusReadyForReview) {
		confirmationDialog.setDialogListener(new DialogAdapter() {

			@Override
			public void onOkClick() {
				boolean stickyFields = isStatusReadyForReview ? false : true;
				ReviewValidateEventBus.fireEvent(new DocumentSplitStartEvent(ReviewValidateNavigator.getCurrentDocument(),
						ReviewValidateNavigator.getCurrentPage(), stickyFields, false));
				confirmationDialog.hide();
			}

			@Override
			public void onCancelClick() {
				if (!isStatusReadyForReview) {
					ReviewValidateEventBus.fireEvent(new DocumentSplitStartEvent(ReviewValidateNavigator.getCurrentDocument(),
							ReviewValidateNavigator.getCurrentPage(), false, true));
					confirmationDialog.hide();
				}
			}

			@Override
			public void onCloseClick() {
				if (!isStatusReadyForReview) {
					ReviewValidateEventBus.fireEvent(new DocumentSplitStartEvent(ReviewValidateNavigator.getCurrentDocument(),
							ReviewValidateNavigator.getCurrentPage(), true, true));
					confirmationDialog.hide();
				}
			}

			@Override
			public void onYesClick() {
				if (!isStatusReadyForReview) {
					ReviewValidateEventBus.fireEvent(new DocumentSplitStartEvent(ReviewValidateNavigator.getCurrentDocument(),
							ReviewValidateNavigator.getCurrentPage(), false, false));
					confirmationDialog.hide();
				}
			}
		});
	}

	private void startDuplicateCurrentPage() {
		final Page currentPage = ReviewValidateNavigator.getCurrentPage();
		final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
		if (currentPage != null && currentDocument != null) {
			final String currentPageIdentifier = currentPage.getIdentifier();
			final String currentDocIdentifer = currentDocument.getIdentifier();
			final String currentBatchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
			ScreenMaskUtility.maskScreen();
			rpcService.duplicatePage(currentBatchInstanceIdentifier, ReviewValidateNavigator.getAlteredDocumentMap(),
					ReviewValidateNavigator.getDocumentIdentifiersList(), currentDocIdentifer, currentPageIdentifier,
					new AsyncCallback<Void>() {

						@Override
						public void onSuccess(final Void result) {
							ReviewValidateNavigator.resetAlteredDocuments();
							Message.display(LocaleConstant.SUCCESS_HEADER, LocaleMessage.PAGE_DUPLIACTE_SUCCESS);
							ReviewValidateEventBus.fireEvent(new DocumentReloadEvent(currentDocIdentifer,
									ReviewValidateConstant.LAST_PAGE_SELECTION_INDEX));
							ScreenMaskUtility.unmaskScreen();
						}

						@Override
						public void onFailure(final Throwable caught) {
							ReviewValidateEventBus.fireEvent(new DocumentReloadEvent(currentDocIdentifer,
									ReviewValidateConstant.LAST_PAGE_SELECTION_INDEX));
							ScreenMaskUtility.unmaskScreen();
						}
					});
		}
	}

	public void deleteCurrentPage() {
		final Page currentPage = ReviewValidateNavigator.getCurrentPage();
		final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
		boolean canDelete = BatchSchemaUtil.getPageCount(currentDocument) > 1 || ReviewValidateNavigator.getTotalDocument() > 1;
		if (null != currentDocument && null != currentPage && canDelete) {
			RVConfirmationDialog confirmationDialog = RVDialogUtil.showConfirmationDialog(LocaleConstant.CONFIRM_DELETION,
					LocaleMessage.CONFIRM_PAGE_DELETION_MESSAGE);
			WidgetUtil.setID(confirmationDialog, "rv-deletePageDialog");
			confirmationDialog.setUserRecordingIdentfier("rv-deletePageDialog");
			confirmationDialog.setResponseRecordingButtons(PredefinedButton.OK);
			confirmationDialog.setDialogListener(new DialogAdapter() {

				@Override
				public void onOkClick() {
					removePage(currentDocument, currentPage);
					int totalPages = BatchSchemaUtil.getPageCount(currentDocument);
					if (totalPages == 0) {
						ReviewValidateEventBus.fireEvent(new DocumentTraversalEvent(false));
						ReviewValidateEventBus.fireEvent(new DocumentRemovalEvent(currentDocument.getIdentifier()));
					}
				}
			});
		} else {
			Message.display(LocaleConstant.INVALID_DELETE_OPERATION_HEADER, LocaleMessage.CANNOT_DELETE_CURRENT_PAGE);
		}
	}

	private void removePage(Document currentDocument, Page currentPage) {
		String identifier = currentPage.getIdentifier();
		int pageIndex = BatchSchemaUtil.getPageTypeIndex(currentDocument, identifier);
		if (BatchSchemaUtil.removePage(currentDocument, identifier)) {
			Message.display(LocaleConstant.SUCCESS_HEADER, LocaleMessage.PAGE_DELETE_SUCCESS);
			int totalPages = BatchSchemaUtil.getPageCount(currentDocument);
			int pageNumberToFocus = pageIndex >= totalPages ? totalPages - 1 : pageIndex;
			ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
			ReviewValidateEventBus.fireEvent(new DocumentReRenderEvent(currentDocument, pageNumberToFocus));
		}
	}

	@EventHandler
	public void handleImageOverlayZoomEvent(final ImageOverlayZoomEvent imageOverlayZoomEvent) {
		if (null != imageOverlayZoomEvent) {
			final boolean zoomIn = imageOverlayZoomEvent.isZoomIn();
			if (zoomIn) {
				zoomInDisplayImage();
			} else {
				zoomOutDisplayImage();
			}
		}
	}

	@EventHandler
	public void handleFitToPageEvent(final ImageOverlayFitToPageEvent imageOverlayFitToPageEvent) {
		if (null != imageOverlayFitToPageEvent) {
			view.fitToPage();
		}
	}

	@EventHandler
	public void handleOverlayImageScrollAdjustEvent(final ImageOverlayScrollAdjustEvent scrollAdjustEvent) {
		if (null != scrollAdjustEvent) {
			final ScrollDirection scrollDirection = scrollAdjustEvent.getScrollDirection();
			if (null != scrollDirection) {
				final int offSet = scrollDirection.getOffset();
				switch (scrollDirection) {
					case NORTH:
					case SOUTH:
						view.adjustOverlayImageScroll(0, offSet);
						break;
					case EAST:
					case WEST:
						view.adjustOverlayImageScroll(offSet, 0);
						break;
				}
			}
		}
	}

	@EventHandler
	public void handleDuplicateCurrentPageEvent(final DuplicateCurrentPageStartEvent duplicateCurrentPageStartEvent) {
		if (null != duplicateCurrentPageStartEvent) {
			duplicateCurrentPage();
		}
	}

	public void duplicateCurrentPage() {
		RVConfirmationDialog confirmationDialog = RVDialogUtil.showConfirmationDialog(LocaleConstant.DUPLICATE_PAGE_HEADER,
				LocaleMessage.DUPLICATE_PAGE_CONFIRMATION_MESSAGE);
		confirmationDialog.setUserRecordingIdentfier("rv-duplicate-dialog");
		WidgetUtil.setID(confirmationDialog, "rv-duplicate-dialog");
		confirmationDialog.setResponseRecordingButtons(PredefinedButton.OK);
		confirmationDialog.setDialogListener(new DialogAdapter() {

			@Override
			public void onOkClick() {
				startDuplicateCurrentPage();
			}
		});

	}

	@EventHandler
	public void handleSplitDocumentInitializationEvent(final SplitDocumentInitializationEvent splitDocumentInitializationEvent) {
		if (null != splitDocumentInitializationEvent) {
			splitDocument();
		}
	}

	@EventHandler
	public void handleCurrentPageDeletionEvent(final CurrentPageDeletionEvent pageDeletionEvent) {
		if (null != pageDeletionEvent) {
			this.deleteCurrentPage();
		}
	}

	@EventHandler
	public void handleFieldSelectionEvent(final FieldSelectionEvent fieldSelectionEvent) {
		final ScheduledCommand command = new ScheduledCommand() {

			@Override
			public void execute() {
				if (null != fieldSelectionEvent) {
					view.clearOverlays();
					final Field selectedField = fieldSelectionEvent.getSelectedField();
					if (null != selectedField) {
						final String selectedFieldPageIdentifier = selectedField.getPage();
						if (!StringUtil.isNullOrEmpty(selectedFieldPageIdentifier)) {
							drawOverlays(selectedField);
						}
					}
				}
			}
		};
		final Scheduler scheduler = new SchedulerImpl();
		scheduler.scheduleDeferred(command);
	}

	@EventHandler
	public void drawOverlaysOnThumbnailSelection(final ThumbnailSelectionEvent thumbnailSelectionEvent) {
		final ScheduledCommand command = new ScheduledCommand() {

			@Override
			public void execute() {
				drawOverlays(ReviewValidateNavigator.getCurrentField());
			}
		};
		final Scheduler scheduler = new SchedulerImpl();
		scheduler.scheduleDeferred(command);
	}

	private void drawOverlays(final Field selectedField) {
		if (null != selectedField) {
			final ScheduledCommand command = new ScheduledCommand() {

				@Override
				public void execute() {
					final Page currentPage = ReviewValidateNavigator.getCurrentPage();
					final String selectedFieldPage = selectedField.getPage();
					if (currentPage != null && !StringUtil.isNullOrEmpty(selectedFieldPage)
							&& selectedFieldPage.equalsIgnoreCase(currentPage.getIdentifier())) {
						Overlay.clearOverlays();
						view.drawOverlays(selectedField.getCoordinatesList());
					}
				}
			};
			final Scheduler scheduler = new SchedulerImpl();
			scheduler.scheduleDeferred(command);
		}
	}

	private void appendExtractedValue(final String valueToAppend, final PointCoordinate startCordinate,
			final PointCoordinate endCordinate, final boolean keepPreviousOverlay) {

		ReviewValidateNavigator.addCoordinateToCurrentField(startCordinate, endCordinate, !keepPreviousOverlay);
		final Validatable lastSelectedWidget = ReviewValidateNavigator.getLastSelectedWidget();
		if (null != lastSelectedWidget) {
			lastSelectedWidget.appendValue(valueToAppend, keepPreviousOverlay);
			lastSelectedWidget.focus();
		} else {
			final Column selectedColumn = ReviewValidateNavigator.getSelectedColumn();
			final DataTableGrid grid = ReviewValidateNavigator.getCurrentGrid();
			if (selectedColumn != null && grid != null) {
				final String value = selectedColumn.getValue();
				String newValue = CoreCommonConstant.EMPTY_STRING;
				if (keepPreviousOverlay && !StringUtil.isNullOrEmpty(value)) {
					newValue = StringUtil.concatenate(value, CoreCommonConstant.SPACE);
				}
				newValue = StringUtil.concatenate(newValue, valueToAppend);
				selectedColumn.setValue(newValue);
				grid.refresh();
			}
		}
		final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
		ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
	}
}
