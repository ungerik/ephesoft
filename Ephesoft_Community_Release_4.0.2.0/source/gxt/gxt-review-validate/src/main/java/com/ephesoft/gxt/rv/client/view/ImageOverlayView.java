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

package com.ephesoft.gxt.rv.client.view;

import java.util.List;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.OverlayImage;
import com.ephesoft.gxt.core.client.ui.widget.OverlayImage.Overlay;
import com.ephesoft.gxt.core.client.ui.widget.RotatableImage;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleConstant;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleMessage;
import com.ephesoft.gxt.rv.client.presenter.ImageOverlayPresenter;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.ImageOverlayToolbar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;

public class ImageOverlayView extends ReviewValidateBaseView<ImageOverlayPresenter> {

	interface Binder extends UiBinder<Widget, ImageOverlayView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private int ZOOM_PIXEL = 40;
	private static String FIT_TO_PAGE_WIDTH = "95%";
	private static Integer FIT_TO_PAGE_HEIGHT = Window.getClientHeight() - 20;
	private boolean zoomLock = false;
	private int lockedHeight;
	private int lockedWidth;

	@UiField
	protected ImageOverlayToolbar imageOverlayToolbar;

	private OverlayImage displayedImage;

	private Button deleteButton = new Button();
	private Button splitButton = new Button();
	private Button zoomInButton = new Button();
	private Button duplicateButton = new Button();
	private Button zoomOutButton = new Button();
	private Button fitToPageButton = new Button();
	private Button rotateButton = new Button();

	private boolean pageFitted;
	private int heightBeforeFitToPage;
	private int widthBeforeFitToPage;

	@Override
	public void initialize() {
	}

	public ImageOverlayView() {
		initWidget(binder.createAndBindUi(this));
		splitButton.setStyleName("splitButton");
		splitButton.setTitle(LocaleConstant.SPLIT_IMAGE_TITLE);
		WidgetUtil.setID(splitButton, "split-document-button");
		deleteButton.setStyleName("deleteButton");
		deleteButton.setTitle(LocaleConstant.DELETE_IMAGE_TITLE);
		WidgetUtil.setID(deleteButton, "delete-document-button");
		imageOverlayToolbar.addButton(splitButton);
		imageOverlayToolbar.addButton(deleteButton);
		zoomInButton.setStyleName("zoomInButton");
		zoomInButton.setTitle(LocaleConstant.ZOOM_IN_TITLE);
		pageFitted = false;
		WidgetUtil.setID(zoomInButton, "zoom-in-button");
		duplicateButton.setStyleName("duplicateButton");
		duplicateButton.setTitle(LocaleConstant.DUPLICATE_IMAGE_TITLE);
		imageOverlayToolbar.addButton(duplicateButton);
		imageOverlayToolbar.addButton(zoomInButton);
		zoomOutButton.setStyleName("zoomOutButton");
		zoomOutButton.setTitle(LocaleConstant.ZOOM_OUT_TITLE);
		WidgetUtil.setID(zoomOutButton, "zoom-out-button");
		imageOverlayToolbar.addButton(zoomOutButton);
		fitToPageButton.setStyleName("fitToPageButton");
		fitToPageButton.setTitle(LocaleConstant.FIT_IMAGE_TITLE);
		WidgetUtil.setID(fitToPageButton, "fit-to-page-button");
		imageOverlayToolbar.addButton(fitToPageButton);
		rotateButton.setStyleName("rotateButton");
		WidgetUtil.setID(rotateButton, "rotate-button");
		rotateButton.setTitle(LocaleConstant.ROTATE_IMAGE_TITLE);
		imageOverlayToolbar.addButton(rotateButton);
		WidgetUtil.setID(duplicateButton, "rv-duplicate-button");
		addZoomInClickHandler();
		addZoomOutClickHandler();
		addFitToPageHandler();
		addRotateHandler();
		addSplitButtonHandler();
		addDuplicatePageButtonHandler();
		addDeleteButtonHandler();
	}

	private void addDuplicatePageButtonHandler() {
		duplicateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.duplicateCurrentPage();
			}
		});
	}

	private void addDeleteButtonHandler() {
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.deleteCurrentPage();
			}
		});
	}

	private void addSplitButtonHandler() {
		splitButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.splitDocument();
			}
		});
	}

	public void addFitToPageHandler() {
		fitToPageButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fitToPage();
			}
		});
	}

	private void addRotateHandler() {
		rotateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.rotateImage();
			}
		});
	}

	private void addZoomInClickHandler() {
		zoomInButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.zoomInDisplayImage();
			}
		});
	}

	private void addZoomOutClickHandler() {

		zoomOutButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.zoomOutDisplayImage();
			}
		});
	}

	public void setDisplayImage(final OverlayImage rotatableImage) {
		if (null != rotatableImage) {
			this.displayedImage = rotatableImage;
			imageOverlayToolbar.setOverlayImage(rotatableImage);
			rotatableImage.addLoadHandler(new LoadHandler() {

				@Override
				public void onLoad(LoadEvent event) {
					if (zoomLock) {
						rotatableImage.setWidth(String.valueOf(lockedWidth) + Unit.PX.getType());
						rotatableImage.setHeight(String.valueOf(lockedHeight) + Unit.PX.getType());
					} else {
						if(GXT.isIE()) {
							rotatableImage.setHeight(Window.getClientHeight() + Unit.PX.getType());
						} else {
							rotatableImage.setHeight(StringUtil.concatenate(rotatableImage.getHeight(), Unit.PX.getType()));							
						}
						rotatableImage.setWidth(rotatableImage.getParent().getOffsetWidth() + Unit.PX.getType());
						int zoomCount = ReviewValidateNavigator.getZoomCount();
						if (zoomCount > 1) {
							zoomInDisplayImage(zoomCount - 1);
						}
					}
				}
			});
		}
	}

	public void zoomInDisplayImage(final int factor) {
		if (factor > 0) {
			RotatableImage displayImage = imageOverlayToolbar.getOverlayImage();
			if (null != displayImage) {
				int newWidth = displayImage.getWidth() + ((ZOOM_PIXEL) * factor);
				displayImage.setWidth(StringUtil.concatenate(newWidth, Unit.PX.getType()));
				int newHeight = displayImage.getHeight() + ((ZOOM_PIXEL) * factor);
				displayImage.setHeight(StringUtil.concatenate(newHeight, Unit.PX.getType()));
			}
		}
	}

	public void zoomOutDisplayImage(int factor) {
		if (factor > 0) {
			RotatableImage displayImage = imageOverlayToolbar.getOverlayImage();
			if (null != displayImage) {
				displayImage.setWidth(StringUtil.concatenate((displayImage.getWidth() - ZOOM_PIXEL), Unit.PX.getType()));
				displayImage.setHeight(StringUtil.concatenate((displayImage.getHeight() - ZOOM_PIXEL), Unit.PX.getType()));
			}
		}
	}

	public void zoomInDisplayImage() {
		int defaultZoomFactor = presenter.getDefaultZoomFactor();
		this.zoomInDisplayImage(defaultZoomFactor);
		pageFitted = false;
	}

	public void zoomOutDisplayImage() {
		int defaultZoomFactor = presenter.getDefaultZoomFactor();
		this.zoomOutDisplayImage(defaultZoomFactor);
		pageFitted = false;
	}

	public void fitToPage() {
		RotatableImage rotatableImage = imageOverlayToolbar.getOverlayImage();
		if (null != rotatableImage) {
			if (!pageFitted) {
				heightBeforeFitToPage = rotatableImage.getHeight();
				widthBeforeFitToPage = rotatableImage.getWidth();
				rotatableImage.setHeight(FIT_TO_PAGE_HEIGHT.toString());
				rotatableImage.setWidth(FIT_TO_PAGE_WIDTH);
				pageFitted = true;
			} else {
				pageFitted = false;
				rotatableImage.setHeight(String.valueOf(heightBeforeFitToPage));
				rotatableImage.setWidth(String.valueOf(widthBeforeFitToPage));
			}
		}
	}

	public void adjustOverlayImageScroll(int x_offset, int y_offset) {
		imageOverlayToolbar.adjustScroll(x_offset, y_offset);
	}

	public void drawOverlays(CoordinatesList coordinateList) {
		if (null != coordinateList) {
			this.drawOverlays(coordinateList.getCoordinates());
		}
	}

	public void drawOverlays(List<Coordinates> listOfCoordinates) {
		if (!CollectionUtil.isEmpty(listOfCoordinates) && null != displayedImage) {
			boolean retainPrevious = false;
			for (Coordinates fieldCoordinate : listOfCoordinates) {
				displayedImage.drawOverlay(fieldCoordinate, retainPrevious);
				retainPrevious = true;
			}
		}
	}

	public void removeDisplayImage() {
		imageOverlayToolbar.clearImage();
	}

	public void clearOverlays() {
		Overlay.clearOverlays();
	}

	public int getOverlayImageHeight() {
		int height = 0;
		if (null != displayedImage) {
			height = displayedImage.getHeight();
		}
		return height;
	}

	public int getOverlayImageWidth() {
		int width = 0;
		if (null != displayedImage) {
			width = displayedImage.getWidth();
		}
		return width;
	}

	public void toggleZoomLock() {
		zoomLock = !zoomLock;
		if (zoomLock) {
			lockedHeight = getOverlayImageHeight();
			lockedWidth = getOverlayImageWidth();
			Message.display(LocaleConstant.ZOOM_LOCK_HEADER, LocaleMessage.ZOOM_LOCK_SUCCESS);
		} else {
			Message.display(LocaleConstant.ZOOM_UNLOCK_HEADER, LocaleMessage.ZOOM_UNLOCK_SUCCESS);
		}
	}

	public void setZoomLock(boolean lock) {
		zoomLock = lock;
	}

	public void setPageFitted(boolean pageFitted) {
		this.pageFitted = pageFitted;
	}
}
