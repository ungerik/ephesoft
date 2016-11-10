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

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.gxt.core.client.Controller;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.client.constant.NativeKeyCodes;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.ReviewValidateServiceAsync;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocumentMergeEvent;
import com.ephesoft.gxt.rv.client.event.DocumentTraversalEvent;
import com.ephesoft.gxt.rv.client.event.PageSelectionEvent;
import com.ephesoft.gxt.rv.client.event.ReviewValidateBatchStartEvent;
import com.ephesoft.gxt.rv.client.event.ToggleTableViewDisplayModeEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.CurrentPageDeletionEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.DocumentTypeFieldFocusEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.DuplicateCurrentPageStartEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.FieldTraversalEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.FuzzyFieldFocusInitializationEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ImageOverlayFitToPageEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ImageOverlayScrollAdjustEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ImageOverlayScrollAdjustEvent.ScrollDirection;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ImageOverlayZoomEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ImageRotationEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.InitializeNextBatchOpenEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.SplitDocumentInitializationEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.TableTraversalEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.ZoomLockEvent;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.google.gwt.event.dom.client.DomEvent.Type;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class ReviewValidateSelectionController extends Controller {

	private String selectedDocumentIdentifier;

	public ReviewValidateSelectionController(final EventBus eventBus, final DCMARemoteServiceAsync rpcService) {
		super(eventBus, rpcService);
		RootPanel rootPanel = RootPanel.get();
		Type<KeyDownHandler> keyDownEventType = KeyDownEvent.getType();
		rootPanel.addDomHandler(getTraversalEventHandler(), keyDownEventType);
		rootPanel.addDomHandler(getImageOverlayPanelEventHandler(), keyDownEventType);
		rootPanel.addDomHandler(getOverlayImageTraversalHandler(), keyDownEventType);
		rootPanel.addDomHandler(getFocusInitializationEventHandler(), keyDownEventType);
		rootPanel.addDomHandler(getBatchOperationsHandler(), keyDownEventType);
		rootPanel.addDomHandler(getKeyUpPreventionHandler(), KeyUpEvent.getType());
		rootPanel.addDomHandler(getKeyPressHandler(), KeyPressEvent.getType());
	}

	private KeyUpHandler getKeyUpPreventionHandler() {
		return new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.isControlKeyDown()) {
					if (event.getNativeKeyCode() == KeyCodes.KEY_P) {
						event.preventDefault();
						event.stopPropagation();
					}
				}
			}
		};
	}

	private KeyPressHandler getKeyPressHandler() {
		return new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if (event.isControlKeyDown()) {
					if (event.getUnicodeCharCode() == KeyCodes.KEY_P || event.getNativeEvent().getKeyCode() == KeyCodes.KEY_P) {
						event.preventDefault();
						event.stopPropagation();
					}
				}
			}
		};
	}

	/**
	 * @return the selectedDocumentIdentifier
	 */
	public String getSelectedDocumentIdentifier() {
		return selectedDocumentIdentifier;
	}

	/**
	 * @param selectedDocumentIdentifier the selectedDocumentIdentifier to set
	 */
	public void setSelectedDocumentIdentifier(final String selectedDocumentIdentifier) {
		this.selectedDocumentIdentifier = selectedDocumentIdentifier;
	}

	public void saveBatch() {
		ScreenMaskUtility.maskScreen();
		this.getRpcService().saveBatch(ReviewValidateNavigator.getCurrentBatchInstanceIdentifier(),
				ReviewValidateNavigator.getAlteredDocumentMap(), ReviewValidateNavigator.getDocumentIdentifiersList(),
				new AsyncCallback<Void>() {

					@Override
					public void onSuccess(final Void result) {
						ReviewValidateNavigator.resetAlteredDocuments();
						ScreenMaskUtility.unmaskScreen();
					}

					@Override
					public void onFailure(final Throwable caught) {
					}
				});

	}

	public void signalWorkFlow() {
		String currentBatchInstanceIdentifier = ReviewValidateNavigator.getCurrentBatchInstanceIdentifier();
		this.getRpcService().signalWorkflow(currentBatchInstanceIdentifier, ReviewValidateNavigator.getAlteredDocumentMap(),
				ReviewValidateNavigator.getDocumentIdentifiersList(), new AsyncCallback<Void>() {

					@Override
					public void onFailure(final Throwable caught) {
					}

					@Override
					public void onSuccess(final Void result) {
						moveToBatchList();
					}
				});
	}

	public void moveToBatchList() {
		String url = Window.Location.getHref();
		if (!StringUtil.isNullOrEmpty(url)) {
			int baseURLEndingIndex = url.lastIndexOf(CoreCommonConstant.URL_SEPARATOR);
			if (baseURLEndingIndex != -1) {
				String baseURL = url.substring(0, baseURLEndingIndex);
				String urlTopOpen = StringUtil.concatenate(baseURL, CoreCommonConstant.URL_SEPARATOR, "BatchList.html");
				Window.Location.assign(urlTopOpen);
			}
		}
	}

	public ReviewValidateServiceAsync getRpcService() {
		return (ReviewValidateServiceAsync) super.getRpcService();
	}

	private KeyDownHandler getBatchOperationsHandler() {
		return new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.isControlKeyDown()) {
					int keyCode = event.getNativeKeyCode();
					switch (keyCode) {
						case KeyCodes.KEY_C:
							if (event.isShiftKeyDown()) {
								event.preventDefault();
								saveBatch();
							}
							break;
						case NativeKeyCodes.FORWARD_SLASH:
							event.preventDefault();
							Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
							String currentDocumentIdentfier = currentDocument == null ? null : currentDocument.getIdentifier();
							int documentIndex = ReviewValidateNavigator.getDocumentIndex(currentDocumentIdentfier);
							if (documentIndex > 0) {
								String previousDocumentIdentifier = ReviewValidateNavigator
										.getPreviousDocumentIdentifier(currentDocumentIdentfier);
								ReviewValidateEventBus.fireEvent(new DocumentMergeEvent(currentDocumentIdentfier,
										previousDocumentIdentifier));
							} else {
								Message.display("Invalid Merge Operation", "No Previous document for the current document");
							}
							break;
					}
				}
			}
		};
	}

	private KeyDownHandler getTraversalEventHandler() {
		return new KeyDownHandler() {

			@Override
			public void onKeyDown(final KeyDownEvent event) {
				if (event.isControlKeyDown()) {
					switch (event.getNativeKeyCode()) {
						case KeyCodes.KEY_M:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new DocumentTraversalEvent(event.isShiftKeyDown()));
							break;

						case KeyCodes.KEY_E:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new ReviewValidateBatchStartEvent(true));
							break;

						case KeyCodes.KEY_Q:
							event.preventDefault();
							final boolean doSave = ReviewValidateNavigator.doReviewValidateSave(true);
							ReviewValidateEventBus.fireEvent(new ReviewValidateBatchStartEvent(doSave));
							break;

						case KeyCodes.KEY_ZERO:
						case KeyCodes.KEY_P:
							event.preventDefault();
							event.stopPropagation();
							String pageIdentifierToOpen = null;
							if (event.isShiftKeyDown()) {
								pageIdentifierToOpen = ReviewValidateNavigator.getPreviousPageIdentifier(
										ReviewValidateNavigator.getCurrentDocument(), ReviewValidateNavigator.getCurrentPage());
							} else {
								pageIdentifierToOpen = ReviewValidateNavigator.getNextPageIdentifier(
										ReviewValidateNavigator.getCurrentDocument(), ReviewValidateNavigator.getCurrentPage());
							}
							ReviewValidateEventBus.fireEvent(new PageSelectionEvent(pageIdentifierToOpen));
							break;

						case KeyCodes.KEY_FIVE:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new ToggleTableViewDisplayModeEvent());
							break;

						case KeyCodes.KEY_K:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new TableTraversalEvent());
							break;
					}
				}
			}
		};
	}

	private KeyDownHandler getImageOverlayPanelEventHandler() {
		return new KeyDownHandler() {

			@Override
			public void onKeyDown(final KeyDownEvent event) {
				final int keyCode = event.getNativeKeyCode();
				if (event.isControlKeyDown()) {
					switch (keyCode) {
						case KeyCodes.KEY_ONE:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new ImageOverlayZoomEvent(!event.isShiftKeyDown()));
							break;

						case KeyCodes.KEY_D:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new DuplicateCurrentPageStartEvent());
							break;

						case KeyCodes.KEY_TWO:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new SplitDocumentInitializationEvent());
							break;

						case KeyCodes.KEY_DELETE:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new CurrentPageDeletionEvent());
							break;

						case KeyCodes.KEY_R:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new ImageRotationEvent());
							break;

						case KeyCodes.KEY_L:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new ZoomLockEvent());

					}
				} else {
					switch (keyCode) {
						case KeyCodes.KEY_F12:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new ImageOverlayFitToPageEvent());
							break;
					}
				}
			}
		};
	}

	private KeyDownHandler getOverlayImageTraversalHandler() {
		return new KeyDownHandler() {

			@Override
			public void onKeyDown(final KeyDownEvent event) {
				final int keyCode = event.getNativeKeyCode();
				if (event.isShiftKeyDown()) {
					switch (keyCode) {
						case KeyCodes.KEY_UP:
						case KeyCodes.KEY_NUM_EIGHT:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new ImageOverlayScrollAdjustEvent(ScrollDirection.NORTH));
							break;

						case KeyCodes.KEY_DOWN:
						case KeyCodes.KEY_NUM_TWO:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new ImageOverlayScrollAdjustEvent(ScrollDirection.SOUTH));
							break;
						case KeyCodes.KEY_NUM_FOUR:
						case KeyCodes.KEY_LEFT:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new ImageOverlayScrollAdjustEvent(ScrollDirection.WEST));
							break;
						case KeyCodes.KEY_NUM_SIX:
						case KeyCodes.KEY_RIGHT:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new ImageOverlayScrollAdjustEvent(ScrollDirection.EAST));
							break;
					}
				}
			}
		};
	}

	private KeyDownHandler getFocusInitializationEventHandler() {
		return new KeyDownHandler() {

			@Override
			public void onKeyDown(final KeyDownEvent event) {
				if (event.isControlKeyDown()) {
					final int keyCode = event.getNativeKeyCode();
					switch (keyCode) {
						case NativeKeyCodes.BACK_SLASH:
							event.preventDefault();
							if (!event.isShiftKeyDown()) {
								ReviewValidateEventBus.fireEvent(new DocumentTypeFieldFocusEvent());
							} else {
								handleImageJump();
							}
							break;

						case NativeKeyCodes.GREATER_THAN_KEY:
							event.preventDefault();
							if (!event.isShiftKeyDown()) {
								ReviewValidateEventBus.fireEvent(new FieldTraversalEvent(true, false));
							} else {
								ReviewValidateEventBus.fireEvent(new InitializeNextBatchOpenEvent());
							}
							break;

						case NativeKeyCodes.LESS_THAN_KEY:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new FieldTraversalEvent(true, true));
							break;
						case NativeKeyCodes.CLOSING_SQUARE_BRACE:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new FieldTraversalEvent(false, false));
							break;

						case NativeKeyCodes.KEY_TILT:
							event.preventDefault();
							ReviewValidateEventBus.fireEvent(new FuzzyFieldFocusInitializationEvent());
							break;
					}
				}
			}
		};
	}

	private void handleImageJump() {
		boolean isPageSelectionEnable = !(ReviewValidateNavigator.isPageSelectionEnable());
		ReviewValidateNavigator.setPageSelectionEnable(isPageSelectionEnable);
		if (isPageSelectionEnable) {
			Message.display("Page Jumping Enable", "Pages will change on field selection.");
		} else {
			Message.display("Page Jumping Disable", "Pages will not change on field selection.");
		}
	}
}
