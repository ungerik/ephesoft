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

package com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.gxt.core.client.ui.widget.RotatableImage;
import com.ephesoft.gxt.core.client.util.ClippedRectangleProperties;
import com.ephesoft.gxt.core.shared.dto.PointCoordinate;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.sencha.gxt.fx.client.DragEndEvent;
import com.sencha.gxt.fx.client.DragEndEvent.DragEndHandler;
import com.sencha.gxt.fx.client.DragStartEvent;
import com.sencha.gxt.fx.client.DragStartEvent.DragStartHandler;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.theme.gray.client.window.GrayWindowAppearance;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent;
import com.sencha.gxt.widget.core.client.event.ResizeStartEvent;

public class OverlayImage extends RotatableImage {

	private List<Coordinates> coordinatesList;
	private boolean isImageLoaded = false;
	private Coordinates lastDraggedCoordinate;
	private boolean overlayRoundingCounter;
	private boolean isDraggable = false;
	private boolean isResizable = false;
	private OverlayType type = OverlayType.DEFAULT;
	private List<String> codeList;
	private LinkedList<Coordinates> defaultCachedOverlays;
	private boolean isEdit = false;
	
	private List<Overlay> cachedOverlays;

	public static final String KEYCOLORCONSTANT = "#8A2908";

	
	
	public boolean isImageLoaded() {
		return isImageLoaded;
	}

	// Code to set Boundary Condition
	private ContentPanel imagePanel;

	public Coordinates getLastDraggedCoordinate() {
		return lastDraggedCoordinate;
	}

	public void setLastDraggedCoordinate(Coordinates lastDraggedCoordinate) {
		this.lastDraggedCoordinate = lastDraggedCoordinate;
	}

	public OverlayImage() {
		super();
		this.addHandlers();
	}

	public OverlayImage(final String uri) {
		super(uri);
		this.addHandlers();
	}

	private void addHandlers() {
		coordinatesList = new LinkedList<Coordinates>();
		codeList = new LinkedList<String>();
		addAttachHandler();
		this.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
				isImageLoaded = true;
			}
		});

		// Redraw Overlays on Resize of Panel
		if (null != imagePanel) {
			imagePanel.addResizeHandler(new ResizeHandler() {

				@Override
				public void onResize(ResizeEvent event) {
					if (!type.equals(OverlayType.LABEL)) {
						Overlay.redrawAll(false);
					} else {
						ImageLabel.redrawAll();
					}
				}
			});
		}
	}

	private void addAttachHandler() {
		this.addAttachHandler(new Handler() {

			@Override
			public void onAttachOrDetach(final AttachEvent event) {
				// isImageLoaded = true;
			}
		});
	}

	public OverlayImage(final double actualImageWidth, final double actualImageHeight) {
		coordinatesList = new LinkedList<Coordinates>();
		codeList = new LinkedList<String>();
	}

	public void clearOverlays() {
		Overlay.clearOverlays();
		coordinatesList.clear();
		codeList.clear();
	}

	public void clearLabels() {
		ImageLabel.clearLabels();
		coordinatesList.clear();
		codeList.clear();
	}

	public void drawOverlay(final Coordinates cordinatePosition, final boolean retainPrevious) {
		Overlay.clearOverlays();
		if (!retainPrevious) {
			coordinatesList.clear();
		}
		coordinatesList.add(cordinatePosition);
		drawOverlays();
	}

	// Draw Colored Overlays
	public void drawColoredOverlays(final Coordinates cordinatePosition, final boolean retainPrevious, final String colorCode,
			final boolean drawOverlay) {
		drawColoredOverlays(cordinatePosition, retainPrevious, colorCode, false, drawOverlay);
	}

	public void drawColoredOverlays(final Coordinates cordinatePosition, final boolean retainPrevious, final String colorCode,
			final boolean isDefaultOverlay, final boolean drawOverlay) {
		drawColoredOverlays(cordinatePosition, retainPrevious, colorCode, isDefaultOverlay, drawOverlay, false);
	}

	public void drawColoredOverlays(final Coordinates cordinatePosition, final boolean retainPrevious, final String colorCode,
			final boolean isDefaultOverlay, final boolean drawOverlay, final boolean isEdit) {
		this.isEdit = isEdit;
		if (isDefaultOverlay) {
			this.isDraggable = true;
			this.isResizable = true;
		}
		if (!retainPrevious) {
			this.codeList.clear();
			coordinatesList.clear();
		}
		if (!StringUtil.isNullOrEmpty(colorCode)) {
			this.codeList.add(colorCode);
		}
		this.coordinatesList.add(cordinatePosition);
		if (drawOverlay) {
			Overlay.clearOverlays();
			if (isDefaultOverlay)
				drawdefaultOverlays();
			else
				drawOverlays();
		}

	}

	/**
	 * Draw overlay.
	 * 
	 * @param cordinatePosition the cordinate position
	 * @param retainPrevious the retain previous
	 * @param isDraggable the is draggable
	 * @param isResizable the is resizable
	 * @param isHeaderVisible the is header visible
	 * @param colorCode the color code
	 */
	public void drawOverlay(final Coordinates cordinatePosition, final boolean retainPrevious, final boolean isDraggable,
			final boolean isResizable, final boolean isHeaderVisible, final String colorCode) {
		this.isDraggable = isDraggable;
		this.isResizable = isResizable;
		Overlay.clearOverlays();
		if (!retainPrevious) {
			codeList.clear();
			coordinatesList.clear();
		}
		codeList.add(colorCode);
		coordinatesList.add(cordinatePosition);
		drawdefaultOverlays();
	}

	private void drawOverlays() {
		if (isImageLoaded) {
			drawOverlayOnLoadedImage();
		}
	}

	private void drawdefaultOverlays() {
		if (!CollectionUtil.isEmpty(defaultCachedOverlays) && isImageLoaded /* && !isEdit */) {
			coordinatesList.clear();
			coordinatesList.addAll(defaultCachedOverlays);
			drawOverlayOnLoadedImage(true);
		} else if (isImageLoaded) {
			drawOverlayOnLoadedImage(true);
		}
	}

	/**
	 * Draw labels.
	 * 
	 * @param cordinatePosition the cordinate position
	 * @param retainPrevious the retain previous
	 * @param colorCode the color code
	 * @param drawLabel the draw label
	 */
	public void drawLabels(final Coordinates cordinatePosition, final boolean retainPrevious, final String colorCode,
			final boolean drawLabel) {
		if (!retainPrevious) {
			this.codeList.clear();
			coordinatesList.clear();
		}
		if (!StringUtil.isNullOrEmpty(colorCode)) {
			this.codeList.add(colorCode);
		}

		this.coordinatesList.add(cordinatePosition);
		if (drawLabel) {
			Overlay.clearOverlays();
			ImageLabel.clearLabels();
			drawLabels();
		}
	}

	private void drawLabels() {
		if (isImageLoaded) {
			drawLabelsOnLoadedImage();
		}
	}

	private void drawOverlayOnLoadedImage() {
		drawOverlayOnLoadedImage(false);
	}

	private void drawOverlayOnLoadedImage(boolean isDefault) {
		for (int i = 0; i < coordinatesList.size(); i++) {

			if (i == 2 && isDefault) {
				break;
			}
			final Coordinates coordinate = coordinatesList.get(i);
			final BigInteger x_start_cordinate = coordinate.getX0();
			final BigInteger x_end_cordinate = coordinate.getX1();
			final BigInteger y_start_cordinate = coordinate.getY0();
			final BigInteger y_end_cordinate = coordinate.getY1();
			if (null != x_start_cordinate && null != y_start_cordinate && null != x_end_cordinate && null != y_end_cordinate) {
				final int imageLeft = this.getAbsoluteLeft();
				final int imageTop = this.getAbsoluteTop();
				final int imageWidth = this.getWidth();
				final int imageHeight = this.getHeight();
				final int naturalWidth = this.getNaturalWidth();
				final int naturalHeight = this.getNaturalHeight();
				if (naturalHeight != 0 && naturalWidth != 0 && imageWidth != 0 && imageHeight != 0) {
					float heightCompressionFactor = (imageHeight * 1.0f) / naturalHeight;
					float widthCompressionFactor = (imageWidth * 1.0f) / naturalWidth;
					final Overlay overlay = new Overlay();
					Coordinates coordinates = null;
					if (isDefault) {
						if (CollectionUtil.isEmpty(defaultCachedOverlays)) {
							if (!isEdit) {
								heightCompressionFactor = 1;
								widthCompressionFactor = 1;
							}
							// overlay.getElement().getStyle().setBorderStyle(BorderStyle.DOTTED);
							// overlay.getElement().getStyle().setBorderWidth(3, Unit.PX);
							overlay.addStyleName("defaultKVOverlay");
						}
						Draggable draggable = new Draggable(overlay);
						overlay.setResizable(this.isResizable);
						overlay.getElement().getStyle().setCursor(Cursor.POINTER);

						if (this.codeList.get(i) == KEYCOLORCONSTANT) {
							overlay.addStyleName("keyAdvKVOverlay");
							overlay.setToolTip("Key");
							coordinates = getDefaultKeyCoordinates();
						} else {
							overlay.addStyleName("valueAdvKVOverlay");
							overlay.setToolTip("Value");
							coordinates = getDefaultValueCoordinates();
						}
						final Coordinates kvCoordinates = coordinates;
						draggable.addDragEndHandler(new DragEndHandler() {

							@Override
							public void onDragEnd(DragEndEvent event) {
								overlay.removeStyleName("defaultKVOverlay");
								if (getLastDraggedCoordinate() != null) {
									
									// Fix for setting boundary Condition on Overlays.
									if(overlay.cutDownDimension.getHeight() == 0){
										redrawIfBoundaryConditionViolated(overlay, getLastDraggedCoordinate());
										overlay.freeze(overlay.overlayImage);
									}else{
										overlay.redraw(false);
										overlay.freeze(overlay.overlayImage);
									}
								}
								overlay.overlayImage.setCachedOverlays();
							}
						});
						draggable.addDragStartHandler(new DragStartHandler() {

							@Override
							public void onDragStart(DragStartEvent event) {
								if (null != imagePanel) {
									int left = overlay.getAbsoluteLeft() - imagePanel.getAbsoluteLeft();
									int top = overlay.getAbsoluteTop() - imagePanel.getAbsoluteTop();
									setLastDraggedCoordinate(getCoordinate(left, top, left + overlay.getOffsetWidth(),
											top + overlay.getOffsetHeight()));
								}
							}
						});
					} else {
						overlay.addStyleName("advKVOverlay");
						if (codeList != null && !StringUtil.isNullOrEmpty(this.codeList.get(i))) {
							overlay.getElement().getStyle().setBackgroundColor(this.codeList.get(i));
						}
					}
					int overlayLeft = Math.round((imageLeft) + (x_start_cordinate.intValue() * widthCompressionFactor));
					int overlayTop = Math.round((imageTop) + (y_start_cordinate.intValue() * heightCompressionFactor));
					int overlayWidth = Math
							.round((x_end_cordinate.intValue() - x_start_cordinate.intValue()) * widthCompressionFactor);
					int overlayHeight = Math.round((y_end_cordinate.intValue() - y_start_cordinate.intValue())
							* heightCompressionFactor);
					overlay.setPosition(overlayLeft, overlayTop);
					overlay.setWidth(overlayWidth);
					overlay.setHeight(overlayHeight);
					Overlay.addOverlay(overlay);
					overlay.redraw(false);
					overlay.freeze(this);
					// Retain Overlays Functionality.

					if (isDefault && null != coordinates) {
						if (checkImageBoundaryOnOverlay(getCoordinate(overlay.getAbsoluteLeft(), overlay.getAbsoluteTop(),
								overlay.getAbsoluteLeft() + overlay.getOffsetWidth(),
								overlay.getAbsoluteTop() + overlay.getOffsetHeight()))) {
							redrawOverlay(overlay, coordinates);
						}
						// redrawIfBoundaryConditionViolated(overlay, coordinates);
						overlay.freeze(overlay.overlayImage);
						if (i == 1) {
							Overlay.redrawAll(false);
							overlay.overlayImage.setCachedOverlays();
						}
					}
				}
			}
		}
	}

	private void redrawIfBoundaryConditionViolated(Overlay overlay, Coordinates coordinates) {
		if (checkImageBoundaryOnOverlay(getCoordinate(overlay.getAbsoluteLeft(), overlay.getAbsoluteTop(), overlay.getAbsoluteLeft()
				+ overlay.getOffsetWidth(), overlay.getAbsoluteTop() + overlay.getOffsetHeight()))) {
			overlay.redraw(false);
		} else if (checkBoundaryConditionOnOverlay(getCoordinate(overlay.getAbsoluteLeft(), overlay.getAbsoluteTop(),
				overlay.getAbsoluteLeft() + overlay.getOffsetWidth(), overlay.getAbsoluteTop() + overlay.getOffsetHeight()))) {
			overlay.redraw(false);
		}
	}

	private void redrawOverlay(Overlay overlay, Coordinates coordinates) {
		overlay.setPosition(this.getAbsoluteLeft() + coordinates.getX0().intValue(), this.getAbsoluteTop()
				+ coordinates.getY0().intValue());
		int height = coordinates.getY1().intValue() - coordinates.getY0().intValue();
		int width = coordinates.getX1().intValue() - coordinates.getX0().intValue();

		overlay.setWidth(width);
		overlay.setHeight(height);
	}

	boolean areOverlaysOverlapping() {
		boolean isInside = false;
		Overlay overlay0 = Overlay.getOverlayList().get(0);
		Overlay overlay1 = Overlay.getOverlayList().get(1);
		int o0x0 = overlay0.getAbsoluteLeft();
		int o0x1 = overlay0.getAbsoluteLeft() + overlay0.getOffsetWidth();
		int o0y0 = overlay0.getAbsoluteTop();
		int o0y1 = overlay0.getAbsoluteTop() + overlay0.getOffsetHeight();
		int o1x0 = overlay1.getAbsoluteLeft();
		int o1x1 = overlay1.getAbsoluteLeft() + overlay1.getOffsetWidth();
		int o1y0 = overlay1.getAbsoluteTop();
		int o1y1 = overlay1.getAbsoluteTop() + overlay1.getOffsetHeight();
		PointCoordinate topLeft1 = new PointCoordinate(o0x0, o0y0);
		PointCoordinate bottomRight1 = new PointCoordinate(o0x1, o0y1);
		PointCoordinate topLeft2 = new PointCoordinate(o1x0, o1y0);
		PointCoordinate bottomRight2 = new PointCoordinate(o1x1, o1y1);

		isInside = areOverlaysColliding(topLeft1, bottomRight1, topLeft2, bottomRight2);
		if (!isInside) {
			isInside = areOverlaysColliding(topLeft2, bottomRight2, topLeft1, bottomRight1);
		}
		return isInside;
	}

	public boolean areOverlaysColliding(PointCoordinate topLeft1, PointCoordinate bottomRight1, PointCoordinate topLeft2,
			PointCoordinate bottomRight2) {
		boolean isInside = false;
		isInside = isPointInside(topLeft1, bottomRight1, topLeft2);
		if (!isInside) {
			isInside = isPointInside(topLeft1, bottomRight1,
					new PointCoordinate(topLeft2.getxCoordinate(), bottomRight2.getyCoordinate()));
			if (!isInside) {
				isInside = isPointInside(topLeft1, bottomRight1,
						new PointCoordinate(bottomRight2.getxCoordinate(), topLeft2.getyCoordinate()));
				if (!isInside) {
					isInside = isPointInside(topLeft1, bottomRight1, bottomRight2);
				}
			}
		}
		return isInside;

	}

	public boolean isPointInside(PointCoordinate topLeft1, PointCoordinate bottomRight1, PointCoordinate coord) {
		if (topLeft1.getxCoordinate() <= coord.getxCoordinate() && coord.getxCoordinate() <= bottomRight1.getxCoordinate()) {
			if (topLeft1.getyCoordinate() <= coord.getyCoordinate() && coord.getyCoordinate() <= bottomRight1.getyCoordinate()) {
				return true;
			}
		}
		return false;
	}

	private boolean checkBoundaryConditionOnOverlay(Coordinates coordinates) {
		boolean isViolated = false;
		if (null != imagePanel) {
			if (coordinates.getX0().intValue() < imagePanel.getAbsoluteLeft()
					|| coordinates.getX1().intValue() > (imagePanel.getAbsoluteLeft() + imagePanel.getOffsetWidth())) {
				isViolated = true;
			}
			if (coordinates.getY0().intValue() < imagePanel.getAbsoluteTop()
					|| coordinates.getY1().intValue() > (imagePanel.getAbsoluteTop() + imagePanel.getOffsetHeight())) {
				isViolated = true;
			}
		}
		return isViolated;
	}

	private boolean checkImageBoundaryOnOverlay(Coordinates coordinates) {
		boolean isViolated = false;
		if (isImageLoaded) {
			if (coordinates.getX0().intValue() < this.getAbsoluteLeft()
					|| coordinates.getX1().intValue() > (this.getAbsoluteLeft() + this.getOffsetWidth())) {
				isViolated = true;
			}
			if (coordinates.getY0().intValue() < this.getAbsoluteTop()
					|| coordinates.getY1().intValue() > (this.getAbsoluteTop() + this.getOffsetHeight())) {
				isViolated = true;
			}
		}
		return isViolated;
	}

	private void drawLabelsOnLoadedImage() {
		for (int i = 0; i < coordinatesList.size(); i++) {

			final Coordinates coordinate = coordinatesList.get(i);
			final BigInteger x_start_cordinate = coordinate.getX0();
			final BigInteger x_end_cordinate = coordinate.getX1();
			final BigInteger y_start_cordinate = coordinate.getY0();
			final BigInteger y_end_cordinate = coordinate.getY1();
			if (null != x_start_cordinate && null != y_start_cordinate && null != x_end_cordinate && null != y_end_cordinate) {
				final int imageLeft = this.getAbsoluteLeft();
				final int imageTop = this.getAbsoluteTop();
				final int imageWidth = this.getWidth();
				final int imageHeight = this.getHeight();
				final int naturalWidth = this.getNaturalWidth();
				final int naturalHeight = this.getNaturalHeight();
				if (naturalHeight != 0 && naturalWidth != 0 && imageWidth != 0 && imageHeight != 0) {
					float heightCompressionFactor = (imageHeight * 1.0f) / naturalHeight;
					float widthCompressionFactor = (imageWidth * 1.0f) / naturalWidth;
					final ImageLabel imageLabel = new ImageLabel();
					int labelLeft = Math.round(x_start_cordinate.intValue() * widthCompressionFactor);
					int labelTop = Math.round(y_start_cordinate.intValue() * heightCompressionFactor);
					int labelWidth = Math.round((x_end_cordinate.intValue() - x_start_cordinate.intValue()) * widthCompressionFactor);
					int labelHeight = Math
							.round((y_end_cordinate.intValue() - y_start_cordinate.intValue()) * heightCompressionFactor);

					imageLabel.getElement().getStyle().setLeft(labelLeft, Unit.PX);
					imageLabel.getElement().getStyle().setTop(labelTop, Unit.PX);
					imageLabel.getElement().getStyle().setWidth(labelWidth, Unit.PX);
					imageLabel.getElement().getStyle().setHeight(labelHeight, Unit.PX);
					imageLabel.getElement().getStyle().setFontSize(labelHeight, Unit.PX);
					imageLabel.setText(this.codeList.get(i));
					ImageLabel.addLabel(imageLabel);
					imageLabel.freeze(this);

				}
			}
		}
	}

	public LinkedList<Coordinates> getOverlayCoordinates() {
		LinkedList<Coordinates> coordinates = new LinkedList<Coordinates>();
		Coordinates coordinate = null;
		if (!CollectionUtil.isEmpty(Overlay.overlayList)) {
			for (Overlay overlay : Overlay.overlayList) {
				coordinate = convert(getOriginalCoordinates(overlay));

				coordinates.add(coordinate);
			}
		}
		return coordinates;
	}
	
	public LinkedList<Coordinates> getCachedCoordinates(){
		LinkedList<Coordinates> coordinates = new LinkedList<Coordinates>();
		Coordinates coordinate = null;
		if (!CollectionUtil.isEmpty(cachedOverlays)) {
			for (Overlay overlay : cachedOverlays) {
				coordinate = convert(getOriginalCoordinates(overlay));

				coordinates.add(coordinate);
			}
		}
		return coordinates;
	}

	private LinkedList<PointCoordinate> getOriginalCoordinates(Overlay overlay) {
		LinkedList<PointCoordinate> coordinates = new LinkedList<PointCoordinate>();
		int imageWidth = this.getOffsetWidth();
		int imageHeight = this.getOffsetHeight();
		if (overlay != null && imageWidth > 0 && imageHeight > 0) {
			//Fix for JIRA ISSUE # 15976: overlays only get bigger in size when overlays are drawn in bottom half of
			//image in which scroll appears
			final int overlayTop = overlay.getAbsoluteTop() - overlay.imageTop - overlay.cutDownDimension.getTop();
			final int overlayLeft = overlay.original_X_Cordinate - overlay.imageLeft;
			final int overlayWidth = overlay.originalWidth;
			final int overlayHeight = overlay.getOffsetHeight() + overlay.cutDownDimension.getHeight();
			final float heightAdjustmentFactor = (this.getNaturalHeight() * 1.0f) / imageHeight;
			final float widthAdjustmenetFactor = (this.getNaturalWidth() * 1.0f) / imageWidth;
			final int actualOverlayTop = (int) Math.round(overlayTop * heightAdjustmentFactor);
			final int actualOverlayLeft = (int) Math.round(overlayLeft * widthAdjustmenetFactor);
			final int actualSpanWidth = (int) Math.round(overlayWidth * widthAdjustmenetFactor);
			final int actualSpanHeight = (int) Math.round(overlayHeight * heightAdjustmentFactor);
			PointCoordinate startCordinate = new PointCoordinate(actualOverlayLeft, actualOverlayTop);
			PointCoordinate endCordinate = new PointCoordinate(actualOverlayLeft + actualSpanWidth, actualOverlayTop
					+ actualSpanHeight);
			coordinates.add(startCordinate);
			coordinates.add(endCordinate);
		}
		return coordinates;
	}

	public void setRepositioningScroll(final ScrollPanel scrollPanel) {
		Overlay.setRepositioningScroll(scrollPanel);
	}

	public void setWidth(final String width) {
		super.setWidth(width);
		if (isImageLoaded) {
			onSizeChange(getHeight(), getWidth());
		}
	}

	public void setHeight(final String height) {
		super.setHeight(height);
		if (isImageLoaded) {
			onSizeChange(getHeight(), getWidth());
		}
	}

	public void onSizeChange(final int newHeight, final int newWidth) {
		final List<Overlay> imageOverlayList = Overlay.getOverlayList();
		if (!CollectionUtil.isEmpty(imageOverlayList) && newHeight > 0 && newWidth > 0) {
			for (final Overlay overlay : imageOverlayList) {
				overlay.redraw(false);
			}
		}
	}

	public int calculateOverlayValue(final double factor, final int valueToChange) {
		final double exactValue = factor * valueToChange;
		return (int) (overlayRoundingCounter ? Math.floor(exactValue) : (Math.round(exactValue)));
	}

	public void setImageLoaded(boolean isImageLoaded) {
		this.isImageLoaded = isImageLoaded;
	}

	public Coordinates convert(LinkedList<PointCoordinate> pointCoordinates) {
		Coordinates coordinate = new Coordinates();
		coordinate.setX0(BigInteger.valueOf(pointCoordinates.get(0).getxCoordinate()));
		coordinate.setY0(BigInteger.valueOf(pointCoordinates.get(0).getyCoordinate()));
		coordinate.setX1(BigInteger.valueOf(pointCoordinates.get(1).getxCoordinate()));
		coordinate.setY1(BigInteger.valueOf(pointCoordinates.get(1).getyCoordinate()));
		return coordinate;
	}

	public ContentPanel getImagePanel() {
		return imagePanel;
	}

	public void setImagePanel(ContentPanel imagePanel) {
		this.imagePanel = imagePanel;
		if (null != imagePanel) {
			imagePanel.addResizeHandler(new ResizeHandler() {

				@Override
				public void onResize(ResizeEvent event) {

					Timer timer = new Timer() {

						@Override
						public void run() {
							if (!type.equals(OverlayType.LABEL)) {
								Overlay.redrawAll(false);
							} else {
								ImageLabel.redrawAll();
							}
						}
					};
					timer.schedule(20);
				}
			});
		}
	}

	public static class Overlay extends Window {

		private static List<Overlay> overlayList;

		private int original_X_Cordinate;

		private int original_Y_Cordinate;

		private int last_X_Cordinate;

		private int last_Y_Cordinate;

		private int imageLeft;

		private int imageTop;

		private int imageWidth;

		private int imageHeight;

		private int originalWidth;

		private int originalHeight;

		private int tempImageLeft;

		private int tempImageTop;

		private OverlayImage overlayImage;

		private ClippedRectangleProperties cutDownDimension;
		private static ScrollPanel lastAddedScrollPanel;

		public Overlay() {
			super(((WindowAppearance) GWT.create(GrayWindowAppearance.class)));
			this.setClosable(false);
			this.setMinWidth(0);
			this.setMinHeight(0);
			this.setResizable(false);
			this.setDraggable(false);
			this.setHeaderVisible(false);
			this.setBodyBorder(false);
			this.setBorders(false);
			this.setShadow(false);
			this.setResizable(false);
			cutDownDimension = new ClippedRectangleProperties();
			Resizable resizable = new Resizable(this);
			resizable.setEnabled(true);
		}

		public static void addOverlay(final Overlay overlayToAdd) {
			if (null == overlayList) {
				overlayList = new LinkedList<OverlayImage.Overlay>();
			}
			if (null != overlayToAdd) {
				overlayList.add(overlayToAdd);
				RootPanel.get().add(overlayToAdd);
			}
		}

		public static void clearOverlays() {
			if (!CollectionUtil.isEmpty(overlayList)) {
				for (final Overlay overlay : overlayList) {
					RootPanel.get().remove(overlay);
				}
				overlayList = new LinkedList<OverlayImage.Overlay>();
			}
		}

		private void freeze(final OverlayImage image) {
			if (null != image) {
				originalWidth = this.getOffsetWidth();
				originalHeight = this.getOffsetHeight() + cutDownDimension.getHeight();
				original_X_Cordinate = getAbsoluteLeft();
				original_Y_Cordinate = getAbsoluteTop() - cutDownDimension.getTop();
				last_X_Cordinate = original_X_Cordinate;
				last_Y_Cordinate = original_Y_Cordinate;
				imageLeft = image.getAbsoluteLeft();
				imageTop = image.getAbsoluteTop();
				imageHeight = image.getHeight();
				imageWidth = image.getWidth();
				tempImageLeft = imageLeft;
				tempImageTop = imageTop;
				this.overlayImage = image;
			}
		}

		public static List<Overlay> getOverlayList() {
			return overlayList;
		}

		public void freezePosition() {
			last_X_Cordinate = this.getAbsoluteLeft();
			last_Y_Cordinate = this.getAbsoluteTop();
		}

		public static void setRepositioningScroll(final ScrollPanel scrollPanel) {
			if (null != scrollPanel) {

				lastAddedScrollPanel = scrollPanel;
				scrollPanel.addScrollHandler(new ScrollHandler() {

					@Override
					public void onScroll(final ScrollEvent event) {
						final List<Overlay> overlayList = Overlay.getOverlayList();
						if (!CollectionUtil.isEmpty(overlayList)) {
							for (final Overlay drawnOverlay : overlayList) {
								if (null != drawnOverlay) {
									drawnOverlay.redraw(false);
								}
							}
						}
					}
				});
			}
		}

		private void redraw(boolean isResize) {
			final List<Overlay> imageOverlayList = Overlay.getOverlayList();
			if (!CollectionUtil.isEmpty(imageOverlayList) && this.overlayImage != null && this.overlayImage.isAttached()
					&& this.overlayImage.isVisible()) {
				final double widthFactor = ((this.overlayImage.getWidth() * 1.0) / this.imageWidth);
				int left = (int) Math.round((this.original_X_Cordinate - this.imageLeft) * widthFactor);
				int overlayWidth = (int) Math.round(this.originalWidth * widthFactor);
				double heightFactor = ((this.overlayImage.getHeight() * 1.0) / this.imageHeight);
				int top = (int) Math.round((this.original_Y_Cordinate - this.imageTop) * heightFactor);
				int height = (int) Math.round(this.originalHeight * heightFactor);

				if (null == this.overlayImage.getImagePanel()) {
					this.setPosition(this.overlayImage.getAbsoluteLeft() + left, top + this.overlayImage.getAbsoluteTop());
					this.setWidth(overlayWidth);
					this.setHeight(height);
					this.freezePosition();
				} else {
					ContentPanel imagePanel = this.overlayImage.getImagePanel();
					int boundaryY0 = imagePanel.getAbsoluteTop();
					int overlayy0 = (top + this.overlayImage.getAbsoluteTop());
					int topCutDownDimension = boundaryY0 - overlayy0;
					int y0 = (height - topCutDownDimension);
					int boundaryY1 = imagePanel.getAbsoluteTop() + imagePanel.getOffsetHeight();
					int overlayY1 = overlayy0 + height;
					int y1 = (height - (overlayY1 - boundaryY1));

					int previousLeft = this.overlayImage.getAbsoluteLeft() + left;
					int previousTop = top + this.overlayImage.getAbsoluteTop();
					if (isResize) {
						this.setVisible(false);
					} else {
						this.setVisible(true);
					}
					// Boundary Condition for X-axis

					this.setWidth(overlayWidth);

					boolean fromTop = false;
					boolean dimensionChanged = false;
					// Boundary Condition for Y-axis
					if (overlayy0 < boundaryY0) {
						previousTop = boundaryY0;
						fromTop = true;
						cutDownDimension.setTop(boundaryY0 - overlayy0);
						this.setPosition(this.overlayImage.getAbsoluteLeft() + left, boundaryY0);
						int newHeight = y0 > 0 ? y0 : 0;
						cutDownDimension.setHeight(height - newHeight);
						dimensionChanged = true;
						height = newHeight;
						if (y0 < 15 || height < 15)
							this.setVisible(false);
					} else {
						cutDownDimension.setTop(0);
					}

					if (overlayY1 > boundaryY1) {
						int newHeight = y1 > 0 ? y1 : 0;
						dimensionChanged = true;
						int previousHeight = fromTop ? cutDownDimension.getHeight() : 0;
						cutDownDimension.setHeight(previousHeight + height - newHeight);
						int finalHeight = Math.max(0, newHeight - previousHeight);
						this.setHeight(finalHeight);
						height = finalHeight;
						if (y1 < 15 || finalHeight < 15)
							this.setVisible(false);
					} else {
						this.setHeight(height);
					}

					if (!dimensionChanged) {
						cutDownDimension.setHeight(0);
					}

					//Reducing minimum threshold height of overlays.
					if (height > 10) {
						this.setVisible(true);
					} else {
						this.setVisible(false);
					}
					this.setPosition(previousLeft, previousTop);
					this.freezePosition();
				}
			}
		}

		public static int getOverlayCount() {
			return CollectionUtil.isEmpty(overlayList) ? 0 : overlayList.size();
		}

		protected void onStartResize(ResizeStartEvent re) {
			if (null != overlayImage.imagePanel) {
				int left = this.getAbsoluteLeft() - overlayImage.imagePanel.getAbsoluteLeft();
				int top = this.getAbsoluteTop() - overlayImage.imagePanel.getAbsoluteTop();
				overlayImage.setLastDraggedCoordinate(overlayImage.getCoordinate(left, top, left + this.getOffsetWidth(),
						top + this.getOffsetHeight()));

			}
			super.onStartResize(re);
		}

		protected void onEndResize(ResizeEndEvent re) {
			if (overlayImage.getLastDraggedCoordinate() != null) {
				overlayImage.redrawIfBoundaryConditionViolated(this, overlayImage.getLastDraggedCoordinate());
			}

			// Check for overlay, if width and height is less than minimum width i.e 12
			if (this.getOffsetWidth() <= 12) {
				this.setWidth(13);
			}
			if (this.getOffsetHeight() <= 12) {
				this.setHeight(13);
			}

			this.freeze(overlayImage);
			overlayImage.setCachedOverlays();
			this.removeStyleName("defaultKVOverlay");
			super.onEndResize(re);
		}

		/**
		 * Method to redraw all overlays.
		 */
		public static void redrawAll(boolean flag) {
			final List<Overlay> overlayList = Overlay.getOverlayList();
			if (!CollectionUtil.isEmpty(overlayList)) {
				for (final Overlay drawnOverlay : overlayList) {
					if (null != drawnOverlay) {
						drawnOverlay.redraw(flag);
					}
				}
			}

		}
	}

	public static class ImageLabel extends Label {

		private static List<ImageLabel> labelList;

		private int original_X_Cordinate;

		private int original_Y_Cordinate;

		private int last_X_Cordinate;

		private int last_Y_Cordinate;

		private int imageLeft;

		private int imageTop;

		private int imageWidth;

		private int imageHeight;

		private int originalWidth;

		private int originalHeight;

		private int tempImageLeft;

		private int tempImageTop;

		private OverlayImage overlayImage;

		private static ScrollPanel lastAddedScrollPanel;

		public ImageLabel() {
			this.addStyleName("imageLabel");
			this.getElement().getStyle().setBorderColor("black");
		}

		public static void addLabel(final ImageLabel labelToAdd) {
			if (null == labelList) {
				labelList = new LinkedList<OverlayImage.ImageLabel>();
			}
			if (null != labelToAdd) {
				labelList.add(labelToAdd);
				RootPanel.get("imageScroll").add(labelToAdd);
			}
		}

		public static void clearLabels() {
			if (!CollectionUtil.isEmpty(labelList)) {
				for (final ImageLabel label : labelList) {
					label.removeFromParent();
					// Overlay.lastAddedScrollPanel.remove(label);
				}
				labelList.clear();
			}
		}

		private void freeze(final OverlayImage image) {
			if (null != image) {
				originalWidth = StringUtil.getIntegerValue(getElement().getStyle().getWidth());
				originalHeight = StringUtil.getIntegerValue(getElement().getStyle().getHeight());
				original_X_Cordinate = getAbsoluteLeft();
				original_Y_Cordinate = getAbsoluteTop();
				last_X_Cordinate = original_X_Cordinate;
				last_Y_Cordinate = original_Y_Cordinate;
				imageLeft = image.getAbsoluteLeft();
				imageTop = image.getAbsoluteTop();
				imageHeight = image.getHeight();
				imageWidth = image.getWidth();
				tempImageLeft = imageLeft;
				tempImageTop = imageTop;
				this.overlayImage = image;
			}
		}

		public static List<ImageLabel> getlabelList() {
			return labelList;
		}

		public static int getLabelCount() {
			return CollectionUtil.isEmpty(labelList) ? 0 : labelList.size();
		}

		/**
		 * Redraw Label.
		 */
		private void redraw() {
			final List<ImageLabel> imageLabelList = ImageLabel.getlabelList();
			if (!CollectionUtil.isEmpty(imageLabelList) && this.overlayImage != null && this.overlayImage.isAttached()
					&& this.overlayImage.isVisible()) {
				final double widthFactor = ((this.overlayImage.getWidth() * 1.0) / this.imageWidth);
				final int left = (int) Math.round((this.original_X_Cordinate - this.imageLeft) * widthFactor);
				final int labelWidth = (int) Math.round(this.originalWidth * widthFactor);
				final double heightFactor = ((this.overlayImage.getHeight() * 1.0) / this.imageHeight);
				final int top = (int) Math.round((this.original_Y_Cordinate - this.imageTop) * heightFactor);
				final int labelheight = (int) Math.round(this.originalHeight * heightFactor);

				if (null != this.overlayImage.getImagePanel()) {
					this.getElement().getStyle().setLeft(/* this.overlayImage.getAbsoluteLeft() + */left, Unit.PX);
					this.getElement().getStyle().setTop(top /* + this.overlayImage.getAbsoluteTop() */, Unit.PX);
					this.getElement().getStyle().setWidth(labelWidth, Unit.PX);
					this.getElement().getStyle().setHeight(labelheight, Unit.PX);
					this.getElement().getStyle().setFontSize(labelheight, Unit.PX);
					this.freezePosition();
				}
			}
		}

		/**
		 * Redraw all Labels.
		 */
		public static void redrawAll() {
			final List<ImageLabel> labelList = ImageLabel.getlabelList();
			Timer timer = new Timer() {

				@Override
				public void run() {
					if (!CollectionUtil.isEmpty(labelList)) {
						for (final ImageLabel drawnLabel : labelList) {
							if (null != drawnLabel) {
								drawnLabel.redraw();
							}
						}
					}
				}
			};
			timer.schedule(20);
		}

		public void freezePosition() {
			last_X_Cordinate = this.getAbsoluteLeft();
			last_Y_Cordinate = this.getAbsoluteTop();
		}
	}

	public void setCachedOverlays() {
		this.defaultCachedOverlays = getOverlayCoordinates();
		this.cachedOverlays = Overlay.getOverlayList();
	}

	public void setCachedOverlaysToNull() {
		this.defaultCachedOverlays = null;
	}

	public void setType(OverlayType type) {
		this.type = type;
	}

	public List<Coordinates> getDefaultCoordinates() {
		List<Coordinates> coordinates = new ArrayList<Coordinates>();
		final int imageLeft = this.getAbsoluteLeft();
		final int imageTop = this.getAbsoluteTop();
		final int imageWidth = this.getWidth();
		final int imageHeight = this.getHeight();
		int keyOverlayx0 = Math.round(imageLeft + imageWidth * 0.2f);
		int keyOverlayy0 = Math.round(imageTop + imageHeight * 0.2f);
		int keyOverlayx1 = Math.round(imageLeft + imageWidth * 0.4f);
		int keyOverlayy1 = Math.round(imageTop + imageHeight * 0.27f);
		coordinates.add(getCoordinate(keyOverlayx0, keyOverlayy0, keyOverlayx1, keyOverlayy1));

		int valueOverlayx0 = Math.round(imageLeft + imageWidth * 0.5f);
		int valueOverlayy0 = Math.round(imageTop + imageHeight * 0.3f);
		int valueOverlayx1 = Math.round(imageLeft + imageWidth * 0.7f);
		int valueOverlayy1 = Math.round(imageTop + imageHeight * 0.37f);

		coordinates.add(getCoordinate(valueOverlayx0, valueOverlayy0, valueOverlayx1, valueOverlayy1));
		return coordinates;
	}

	public Coordinates getDefaultKeyCoordinates() {
		// final int imageLeft = this.getAbsoluteLeft();
		// final int imageTop = this.getAbsoluteTop();
		final int imageWidth = this.getOffsetWidth();
		final int imageHeight = this.getOffsetHeight();
		int keyOverlayx0 = Math.round(imageWidth * 0.2f);
		int keyOverlayy0 = Math.round(imageHeight * 0.2f);
		int keyOverlayx1 = Math.round(imageWidth * 0.4f);
		int keyOverlayy1 = Math.round(imageHeight * 0.27f);

		// Fix for overlay height less than 6.
		int height = keyOverlayy1 - keyOverlayy0;
		int width = keyOverlayx1 - keyOverlayx0;
		if (height <= 6) {
			keyOverlayy0 = 2;
			keyOverlayy1 = imageHeight - 3;
		}
		if (width <= 6) {
			keyOverlayx0 = 2;
			keyOverlayx1 = imageWidth - 3;
		}

		return getCoordinate(keyOverlayx0, keyOverlayy0, keyOverlayx1, keyOverlayy1);
	}

	public Coordinates getDefaultValueCoordinates() {
		// final int imageLeft = this.getAbsoluteLeft();
		// final int imageTop = this.getAbsoluteTop();
		final int imageWidth = this.getOffsetWidth();
		final int imageHeight = this.getOffsetHeight();
		int valueOverlayx0 = Math.round(imageWidth * 0.5f);
		int valueOverlayy0 = Math.round(imageHeight * 0.3f);
		int valueOverlayx1 = Math.round(imageWidth * 0.7f);
		int valueOverlayy1 = Math.round(imageHeight * 0.37f);

		// Fix for overlay height less than 6.
		int height = valueOverlayy1 - valueOverlayy0;
		int width = valueOverlayx1 - valueOverlayx0;
		if (height <= 6) {
			valueOverlayy0 = 2;
			valueOverlayy1 = imageHeight - 3;
		}
		if (width <= 6) {
			valueOverlayx0 = 2;
			valueOverlayx1 = imageWidth - 3;
		}

		return getCoordinate(valueOverlayx0, valueOverlayy0, valueOverlayx1, valueOverlayy1);
	}

	public Coordinates getCoordinate(Integer x0, Integer y0, Integer x1, Integer y1) {
		Coordinates coordinates = new Coordinates();
		coordinates.setX0(BigInteger.valueOf(x0));
		coordinates.setY0(BigInteger.valueOf(y0));
		coordinates.setX1(BigInteger.valueOf(x1));
		coordinates.setY1(BigInteger.valueOf(y1));
		return coordinates;
	}
}
