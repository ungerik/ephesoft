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

package com.ephesoft.gxt.rv.client.layout;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.BorderLayoutContainer;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.ui.widget.window.MessageDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.ReviewValidateConstants;
import com.ephesoft.gxt.rv.client.ReviewValidateServiceAsync;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocumentOpenEvent;
import com.ephesoft.gxt.rv.client.event.DocumentTreeCreationEvent;
import com.ephesoft.gxt.rv.client.event.FocusInitializationEvent;
import com.ephesoft.gxt.rv.client.event.NextBatchOpenEvent;
import com.ephesoft.gxt.rv.client.event.TableValidationEvent;
import com.ephesoft.gxt.rv.client.event.ToggleTableViewDisplayModeEvent;
import com.ephesoft.gxt.rv.client.presenter.ReviewDetailPresenter;
import com.ephesoft.gxt.rv.client.presenter.ReviewValidateBasePresenter;
import com.ephesoft.gxt.rv.client.presenter.TableExtractionPresenter;
import com.ephesoft.gxt.rv.client.presenter.ValidationDetailPresenter;
import com.ephesoft.gxt.rv.client.view.DocumentTreeView;
import com.ephesoft.gxt.rv.client.view.ImageOverlayView;
import com.ephesoft.gxt.rv.client.view.ReviewDetailView;
import com.ephesoft.gxt.rv.client.view.ReviewValidateBaseView;
import com.ephesoft.gxt.rv.client.view.TableExtractionView;
import com.ephesoft.gxt.rv.client.view.TableViewPanel;
import com.ephesoft.gxt.rv.client.view.ValidationDetailView;
import com.ephesoft.gxt.rv.client.view.menu.BatchDetailView;
import com.ephesoft.gxt.rv.client.view.menu.BatchOptionsView;
import com.ephesoft.gxt.rv.client.view.menu.DocumentOptionsView;
import com.ephesoft.gxt.rv.client.view.menu.UserInterfaceMenuView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.ThumbnailWidgetPanel;
import com.ephesoft.gxt.rv.shared.metadata.ReviewValidateMetaData;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.Window.ClosingHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;

public class ReviewValidateLayout extends Composite {

	interface Binder extends UiBinder<Component, ReviewValidateLayout> {
	}

	interface CustomEventBinder extends EventBinder<ReviewValidateLayout> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private static final Binder binder = GWT.create(Binder.class);

	private final ReviewValidateController controller;

	private final ReviewValidateServiceAsync rpcService;

	@UiField
	protected DocumentTreeView documentTreeView;

	@UiField
	protected ContentPanel treePanel;

	@UiField
	protected ContentPanel detailPanel;

	@UiField
	protected ContentPanel westPanel;

	@UiField
	protected BorderLayoutContainer mainPage;

	@UiField
	protected ContentPanel logoPanel;

	@UiField
	protected ContentPanel middlePanel;

	@UiField
	protected ContentPanel rightPanel;

	@UiField
	protected VerticalPanel buttonPanel;

	@UiField
	protected VerticalPanel buttonBottomPanel;

	@UiField
	protected ImageOverlayView imageOverlayPanel;

	@UiField
	protected BatchDetailView batchDetailView;

	@UiField
	protected BatchOptionsView batchOptionsView;

	protected UserInterfaceMenuView externalInterfaceMenuView = new UserInterfaceMenuView();

	@UiField
	protected DocumentOptionsView documentOptionsView;

	@UiField
	protected Label ephesoftPoweredLabel;

	private ReviewValidateBaseView<?> currentReviewValidateDetailView;

	private ReviewValidateBasePresenter<?> currentReviewValidateDetailPresenter;

	private final ReviewDetailView reviewDetailView;

	private final ReviewDetailPresenter reviewDetailPresenter;

	private final ValidationDetailPresenter validationDetailPresenter;

	private final ValidationDetailView validationDetailView;

	private TableViewPanel tableViewPanel;

	private TableExtractionView tableExtractionView;

	private final TableExtractionPresenter tableExtractionPresenter;

	public ReviewValidateLayout(final ReviewValidateController controller, final ReviewValidateServiceAsync rpcService) {
		initWidget(binder.createAndBindUi(this));
		mainPage.sync(false);
		this.controller = controller;
		this.rpcService = rpcService;
		ephesoftPoweredLabel.setText(EphesoftUIContext.getFooterText());
		treePanel.addStyleName("documentTreeContentPanel");
		eventBinder.bindEventHandlers(this, controller.getEventBus());
		reviewDetailView = new ReviewDetailView();
		reviewDetailPresenter = new ReviewDetailPresenter(controller, reviewDetailView);
		validationDetailView = new ValidationDetailView();
		validationDetailView.addStyleName("viewPort");
		reviewDetailView.addStyleName("viewPort");
		treePanel.setAnimCollapse(true);
		validationDetailPresenter = new ValidationDetailPresenter(controller, validationDetailView);
		tableExtractionView = new TableExtractionView();
		tableExtractionPresenter = new TableExtractionPresenter(controller, tableExtractionView);
		this.addWindowClosingHandler();
		WidgetUtil.setID(reviewDetailView, "review-Panel");
		WidgetUtil.setID(validationDetailView, "validation-Detail-Panel");
		addStyleNameforContentPanel();
		initializeTableViewPanel();

		Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				if (null != tableViewPanel && tableViewPanel.isVisible() && tableViewPanel.isAttached()) {
					Timer timer = new Timer() {

						@Override
						public void run() {
							showTableView(false);
						}
					};
					timer.schedule(100);
				}
			}
		});

		ephesoftPoweredLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open(CoreCommonConstants.EPHESOFT_LINK, "", "");
			}

		});
	}

	private void initializeTableViewPanel() {
		tableViewPanel = new TableViewPanel(tableExtractionView);
		tableViewPanel.setPreferCookieDimension(false);
		tableViewPanel.setPredefinedButtons();
		tableViewPanel.setClosable(false);
		tableViewPanel.addStyleName("tableViewContainer");
		tableViewPanel.addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent event) {
				ReviewValidateEventBus.fireEvent(new TableValidationEvent(isTableViewValid()));
			}
		});
	}

	private void addStyleNameforContentPanel() {
		mainPage.addStyleName("mainPage");
		westPanel.addStyleName("leftPanel");
		logoPanel.addStyleName("logoPanel");
		treePanel.addStyleName("leftBottomPanel");
		middlePanel.addStyleName("middlePanel");
		rightPanel.addStyleName("rightPanel");
		buttonPanel.addStyleName("buttonPanel");
		buttonBottomPanel.addStyleName("buttonBottomPanel");
		ephesoftPoweredLabel.addStyleName("poweredByEphesoftLabel");
	}

	private void addWindowClosingHandler() {
		Window.addWindowClosingHandler(new ClosingHandler() {

			@Override
			public void onWindowClosing(final ClosingEvent event) {
				final String batchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
				if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
					onWindowClose();
				}
			}
		});
	}

	public void onWindowClose() {

		final String batchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
		if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier)) {

			// following code was inserted to call a webservice to do cleanup and updateTime on RV screen close.
			RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, StringUtil.concatenate(
					CoreCommonConstant.WINDOW_CLOSE_SERVICE_URL, batchInstanceIdentifier));
			final String postData = CoreCommonConstants.EMPTY_STRING;

			builder.setHeader(CoreCommonConstant.CONTENT_TYPE, CoreCommonConstant.MIME_TYPE);
			try {
				builder.sendRequest(postData, new RequestCallback() {

					public void onError(Request request, Throwable exception) {
						// code will never reach here, before that window will be closed.
					}

					public void onResponseReceived(Request request, Response response) {
						// code will never reach here, before that window will be closed.
					}
				});
			} catch (com.google.gwt.http.client.RequestException e) {
				// UNReachable Code..
			}

		}

	}

	/**
	 * @return the controller
	 */
	public ReviewValidateController getController() {
		return controller;
	}

	public void renderView() {
		this.renderView(true);
	}

	public void renderView(final boolean preferFromURL) {
		String batchInstanceId = null;
		if (preferFromURL) {
			batchInstanceId = Window.Location.getParameter(ReviewValidateConstants.BATCH_ID);
		}
		releaseCurrentBatchLock();
		rpcService.getReviewValidateMetaData(batchInstanceId, new AsyncCallback<ReviewValidateMetaData>() {

			@Override
			public void onSuccess(final ReviewValidateMetaData documentMetaData) {
				MessageDialog dialog = null;
				if (null != documentMetaData) {
					final BatchInstanceStatus status = documentMetaData.getBatchInstanceStatus();
					if (status == BatchInstanceStatus.READY_FOR_REVIEW || status == BatchInstanceStatus.READY_FOR_VALIDATION) {
						controller.renderView(documentMetaData);
					} else {
						dialog = DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(ReviewValidateConstants.ERROR_TITLE),
								"Batch Instance is not of the status Ready For Review or Validation.", DialogIcon.ERROR);
					}
				} else {
					dialog = DialogUtil.showMessageDialog("Invalid Operation", "Could not find a Batch Instance.");
				}
				addDialogHideHandler(dialog);
				
			}

			@Override
			public void onFailure(final Throwable caught) {
				MessageDialog dialog = DialogUtil.showMessageDialog(
						LocaleDictionary.getConstantValue(ReviewValidateConstants.ERROR_TITLE),
						"Error occured while fetching Batch details.", DialogIcon.ERROR);
				addDialogHideHandler(dialog);
			}
		});
	}

	private void releaseCurrentBatchLock() {
		final String batchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
		if (!StringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			rpcService.cleanUpCurrentBatch(batchInstanceIdentifier, new AsyncCallback<Void>() {

				@Override
				public void onFailure(final Throwable caught) {
				}

				@Override
				public void onSuccess(final Void result) {
				}
			});
		}
	}

	/**
	 * @return the documentTreeView
	 */
	public DocumentTreeView getDocumentTreeView() {
		return documentTreeView;
	}

	/**
	 * @return the tableExtractionPresenter
	 */
	public TableExtractionPresenter getTableExtractionPresenter() {
		return tableExtractionPresenter;
	}

	@EventHandler
	public void handleDocumentTreeCreationEvent(final DocumentTreeCreationEvent treeCreationEvent) {
		if (treeCreationEvent != null) {
			detailPanel.clear();
			switch (ReviewValidateNavigator.getBatchInstanceStatus()) {
				case READY_FOR_REVIEW:
					if (!(currentReviewValidateDetailView instanceof ReviewDetailView)) {
						currentReviewValidateDetailView = reviewDetailView;
						currentReviewValidateDetailPresenter = reviewDetailPresenter;
					}
					break;
				case READY_FOR_VALIDATION:
					if (!(currentReviewValidateDetailView instanceof ValidationDetailView)) {
						currentReviewValidateDetailView = validationDetailView;
						currentReviewValidateDetailPresenter = validationDetailPresenter;
					}
					break;
				default:
					break;
			}
			if (currentReviewValidateDetailPresenter != null) {
				currentReviewValidateDetailPresenter.bind();
				detailPanel.add(currentReviewValidateDetailView);
				detailPanel.addStyleName("detailPanel");
			}
		}
	}

	private void addDialogHideHandler(MessageDialog dialog) {
		if (null != dialog) {
			dialog.addHideHandler(new HideHandler() {

				@Override
				public void onHide(HideEvent event) {
					controller.moveToBatchList();
				}
			});
		}
	}

	/**
	 * @return the imageOverlayPanel
	 */
	public ImageOverlayView getImageOverlayPanel() {
		return imageOverlayPanel;
	}

	/**
	 * @return the batchDetailView
	 */
	public BatchDetailView getBatchDetailView() {
		return batchDetailView;
	}

	/**
	 * @return the batchOptionsView
	 */
	public BatchOptionsView getBatchOptionsView() {
		return batchOptionsView;
	}

	/**
	 * @return the externalInterfaceMenuView
	 */
	public UserInterfaceMenuView getUserInterfaceMenuView() {
		return externalInterfaceMenuView;
	}

	/**
	 * @return the documentOptionsView
	 */
	public DocumentOptionsView getDocumentOptionsView() {
		return documentOptionsView;
	}

	public ReviewDetailView getReviewDetailView() {
		return reviewDetailView;
	}

	public int getWestPanelAbsoluteLeft() {
		return westPanel.getAbsoluteLeft();
	}

	public int getWestPanelAbsoluteTop() {
		return westPanel.getAbsoluteTop();
	}

	public int getWestPanelWidth() {
		return westPanel.getOffsetWidth();
	}

	public int getMiddlePanelWidth() {
		return middlePanel.getOffsetWidth();
	}

	public int getMiddlePanelHeight() {
		return middlePanel.getOffsetHeight();
	}

	@EventHandler
	public void handleDocumentOpenEvent(final DocumentOpenEvent documentOpenEvent) {
		if (null != documentOpenEvent) {
			ThumbnailWidgetPanel widgetPanel = documentOpenEvent.getDocumentThumbnail();
			Document bindedDocument = widgetPanel == null ? null : widgetPanel.getDocument();
			ReviewValidateNavigator.setCurrentDocument(bindedDocument);
			showTableView(true);
		}
	}

	public void showTableView(final boolean checksHidden) {
		final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
		if (null != currentDocument) {
			final int totalTables = BatchSchemaUtil.getDataTableCount(currentDocument);
			final boolean doShow = checksHidden ? !tableViewPanel.isHidden() : true;
			if (totalTables > 0 && doShow) {
				tableViewPanel.hide();
				initializeTableViewPanel();
				tableViewPanel.setHeight(getMiddlePanelHeight());
				final int tablePanelWidth = calculateTableViewWidth();
				tableViewPanel.setPosition(getWestPanelAbsoluteLeft(), getWestPanelAbsoluteTop());
				tableViewPanel.setWidth(tablePanelWidth);
				tableViewPanel.show();
			} else {
				tableViewPanel.hide();
			}
		}
	}

	public int calculateTableViewWidth() {
		return getMiddlePanelWidth() + getWestPanelWidth();
	}
	


	@Override
	protected void onLoad() {
		super.onLoad();
		detailPanel.setWidth(middlePanel.getOffsetWidth());
	}

	@EventHandler
	public void handleTableViewOpenEvent(final ToggleTableViewDisplayModeEvent tableViewOpenEvent) {
		if (null != tableViewOpenEvent) {
			final boolean isDisplay = tableViewPanel.isHidden();
			if (isDisplay) {
				showTableView(false);
			} else {
				tableViewPanel.hide();
			}
		}
	}

	@EventHandler
	public void handleNextBatchOpenEvent(final NextBatchOpenEvent nextBatchOpenEvent) {
		if (null != nextBatchOpenEvent) {
			ReviewValidateNavigator.setCurrentDocument(null);
			ReviewValidateNavigator.setCurrentPage(null);
			renderView(false);
		}
	}

	@EventHandler
	public void handleFocusInitializationEvent(final FocusInitializationEvent focusInitializationEvent) {
		DelayedTask task = new DelayedTask() {

			@Override
			public void onExecute() {
				if (null != focusInitializationEvent
						&& BatchInstanceStatus.READY_FOR_VALIDATION == ReviewValidateNavigator.getBatchInstanceStatus()
						&& isTableViewVisible()) {
					tableViewPanel.selectDefaultCell();
				}
			}
		};
		task.delay(1000);
	}

	public boolean isTableViewValid() {
		return tableViewPanel.isValid();
	}

	public boolean isTableViewVisible() {
		return !tableViewPanel.isHidden();
	}
	
}
