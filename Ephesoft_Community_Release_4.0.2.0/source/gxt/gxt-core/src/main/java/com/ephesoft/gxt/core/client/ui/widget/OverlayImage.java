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

package com.ephesoft.gxt.core.client.ui.widget;

import java.math.BigInteger;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.theme.gray.client.window.GrayWindowAppearance;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.gxt.core.client.ui.widget.listener.OverlayDrawnListener;
import com.ephesoft.gxt.core.shared.dto.PointCoordinate;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;

public class OverlayImage extends RotatableImage {

	private List<Coordinates> coordinatesList;
	private boolean isImageLoaded = false;
	private static boolean drawOverlayOnMouseMove = false;
	private static Overlay lastAddedOverlay;
	private int lastAddedOverlay_X;
	private int lastAddedOverlay_Y;
	private boolean overlayRoundingCounter;
	private OverlayDrawnListener overlayDrawnListener;
	private boolean retainPreviousValues;
	private static final char URL_SEPERATOR = '/';
	private boolean enableOverlayDraw;

	// Code to set Boundary Condition
	private ContentPanel imagePanel;

	private boolean scrollIntoView = true;

	public OverlayImage() {
		super();
		this.addHandlers();
		enableOverlayDraw = true;
	}

	public OverlayImage(final String uri) {
		super(uri);
		this.addHandlers();
		enableOverlayDraw = true;
	}

	public void setEnableOverlayDraw(boolean enable) {
		this.enableOverlayDraw = enable;
	}

	public boolean isScrollIntoView() {
		return scrollIntoView;
	}

	public void setScrollIntoView(boolean scrollIntoView) {
		this.scrollIntoView = scrollIntoView;
	}

	private void addHandlers() {
		coordinatesList = new LinkedList<Coordinates>();
		handleOverlayGeneration();
		addAttachHandler();
		addClickHandler();
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
					Overlay.redrawAll();
				}
			});
		}
	}

	private void addClickHandler() {
		this.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				event.preventDefault();
				retainPreviousValues = event.getNativeEvent().getCtrlKey();
				if (null != overlayDrawnListener) {
					onPointCoordinateClick(event.getClientX() - OverlayImage.this.getAbsoluteLeft(), event.getClientY()
							- OverlayImage.this.getAbsoluteTop());
				}
			}
		});
	}

	protected void onPointCoordinateClick(int clientX, int clientY) {
		int imageWidth = this.getWidth();
		int imageHeight = this.getHeight();
		if (null != overlayDrawnListener) {
			final float heightAdjustmentFactor = (this.getNaturalHeight() * 1.0f) / imageHeight;
			final float widthAdjustmenetFactor = (this.getNaturalWidth() * 1.0f) / imageWidth;
			final int actualYCoordinate = Math.round(clientY * heightAdjustmentFactor);
			final int actualXCoordinate = Math.round(clientX * widthAdjustmenetFactor);
			overlayDrawnListener
					.onPointCordinateClick(new PointCoordinate(actualXCoordinate, actualYCoordinate), retainPreviousValues);
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
	}

	public void drawOverlay(final Coordinates cordinatePosition, final boolean retainPrevious) {
		Overlay.clearOverlays();
		if (!retainPrevious) {
			coordinatesList.clear();
		}
		coordinatesList.add(cordinatePosition);
		drawOverlays();
	}

	private void drawOverlays() {
		if (isImageLoaded) {
			drawImageOnLoadedImage();
		} else {
			this.addLoadHandler(new LoadHandler() {

				@Override
				public void onLoad(final LoadEvent event) {
					isImageLoaded = true;
					drawImageOnLoadedImage();
				}
			});
		}
	}

	@Override
	public void setUrl(String url, Direction direction) {

		if (null != direction && !StringUtil.isNullOrEmpty(url)) {
			int lastIndex = url.lastIndexOf(URL_SEPERATOR);
			String baseURL = url.substring(0, lastIndex + 1);
			String imageName = url.substring(lastIndex + 1, url.length());
			String newURL = StringUtil.concatenate(baseURL, direction, URL_SEPERATOR, imageName);
			setUrl(newURL);
		} else {
			setUrl(url);
		}
	}

	private void drawImageOnLoadedImage() {
		for (final Coordinates coordinate : coordinatesList) {
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
					
					overlay.isScrollView = isScrollIntoView();
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
					overlay.freeze(this);
				}
			}
		}
	}

	protected void handleOverlayGeneration() {
		this.addDomHandler(new ContextMenuHandler() {

			@Override
			public void onContextMenu(final ContextMenuEvent event) {
				event.preventDefault();
				if (enableOverlayDraw) {
					final NativeEvent nativeEvent = event.getNativeEvent();
					if (!nativeEvent.getCtrlKey() && !drawOverlayOnMouseMove) {
						Overlay.clearOverlays();
					}
					if (!drawOverlayOnMouseMove) {
						drawOverlayOnMouseMove = true;
						lastAddedOverlay = new Overlay();
						lastAddedOverlay.isScrollView = isScrollIntoView();
						addMouseHandlerOnOverlay();
						lastAddedOverlay_X = nativeEvent.getClientX();
						lastAddedOverlay_Y = nativeEvent.getClientY();
						retainPreviousValues = nativeEvent.getCtrlKey();
						Overlay.addOverlay(lastAddedOverlay);
					} else {
						if (null != lastAddedOverlay) {
							lastAddedOverlay.freeze(OverlayImage.this);
						}
						drawOverlayOnMouseMove = false;
						onOverlayDraw();
					}
				}
			}
		}, ContextMenuEvent.getType());

		this.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(final MouseMoveEvent event) {
				if (drawOverlayOnMouseMove) {
					handleMouseMoveEvent(event.getClientX(), event.getClientY());
				}
			}
		});
	}

	protected void onOverlayDraw() {
		int imageWidth = this.getWidth();
		int imageHeight = this.getHeight();
		if (null != overlayDrawnListener && null != lastAddedOverlay && imageWidth > 0 && imageHeight > 0) {
			final int overlayTop = lastAddedOverlay.getAbsoluteTop() - this.getAbsoluteTop();
			final int overlayLeft = lastAddedOverlay.getAbsoluteLeft() - this.getAbsoluteLeft();
			final int overlayWidth = lastAddedOverlay.getOffsetWidth();
			final int overlayHeight = lastAddedOverlay.getOffsetHeight();
			final float heightAdjustmentFactor = (this.getNaturalHeight() * 1.0f) / imageHeight;
			final float widthAdjustmenetFactor = (this.getNaturalWidth() * 1.0f) / imageWidth;
			final int actualOverlayTop = (int) Math.round(overlayTop * heightAdjustmentFactor);
			final int actualOverlayLeft = (int) Math.round(overlayLeft * widthAdjustmenetFactor);
			final int actualSpanWidth = (int) Math.round(overlayWidth * widthAdjustmenetFactor);
			final int actualSpanHeight = (int) Math.round(overlayHeight * heightAdjustmentFactor);
			PointCoordinate startCordinate = new PointCoordinate(actualOverlayLeft, actualOverlayTop);
			PointCoordinate endCordinate = new PointCoordinate(actualOverlayLeft + actualSpanWidth, actualOverlayTop
					+ actualSpanHeight);
			overlayDrawnListener.onOverlayDraw(startCordinate, endCordinate, retainPreviousValues);
		}
	}

	private void addMouseHandlerOnOverlay() {
		lastAddedOverlay.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(final MouseMoveEvent event) {
				handleMouseMoveEvent(event.getClientX(), event.getClientY());
			}
		}, MouseMoveEvent.getType());

		lastAddedOverlay.addDomHandler(new ContextMenuHandler() {

			@Override
			public void onContextMenu(final ContextMenuEvent event) {
				event.preventDefault();
				if (drawOverlayOnMouseMove) {
					if (null != lastAddedOverlay) {
						lastAddedOverlay.freeze(OverlayImage.this);
					}
					drawOverlayOnMouseMove = false;
					onOverlayDraw();
				}
			}
		}, ContextMenuEvent.getType());
	}

	protected void handleMouseMoveEvent(final int x_CordinateMousePosition, final int y_CordinateMousePosition) {
		if (null != lastAddedOverlay && drawOverlayOnMouseMove) {
			if (x_CordinateMousePosition > lastAddedOverlay_X && y_CordinateMousePosition > lastAddedOverlay_Y) {
				lastAddedOverlay.setPosition(lastAddedOverlay_X, lastAddedOverlay_Y);
			} else if (x_CordinateMousePosition < lastAddedOverlay_X && y_CordinateMousePosition < lastAddedOverlay_Y) {
				lastAddedOverlay.setPosition(x_CordinateMousePosition, y_CordinateMousePosition);
			} else if (x_CordinateMousePosition < lastAddedOverlay_X && y_CordinateMousePosition > lastAddedOverlay_Y) {
				lastAddedOverlay.setPosition(x_CordinateMousePosition, lastAddedOverlay_Y);
			} else {
				lastAddedOverlay.setPosition(lastAddedOverlay_X, y_CordinateMousePosition);
			}
			lastAddedOverlay.setWidth(Math.abs(x_CordinateMousePosition - lastAddedOverlay_X));
			lastAddedOverlay.setHeight(Math.abs(y_CordinateMousePosition - lastAddedOverlay_Y));
		}
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
				overlay.redraw();
			}
		}
	}

	public int calculateOverlayValue(final double factor, final int valueToChange) {
		final double exactValue = factor * valueToChange;
		return (int) (overlayRoundingCounter ? Math.floor(exactValue) : (Math.round(exactValue)));
	}

	public void setOverlayDrawnListener(final OverlayDrawnListener overlayDrawnListener) {
		this.overlayDrawnListener = overlayDrawnListener;
	}

	public void setImageLoaded(boolean isImageLoaded) {
		this.isImageLoaded = isImageLoaded;
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
					Overlay.redrawAll();
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
		
		private boolean isScrollView =true;

		private OverlayImage overlayImage;

		private static ScrollPanel lastAddedScrollPanel;

		public Overlay() {
			super(((WindowAppearance) GWT.create(GrayWindowAppearance.class)));
			this.addStyleName("overlay");
			this.setClosable(false);
			this.setMinWidth(0);
			this.setMinHeight(0);
			this.setResizable(false);
			this.setDraggable(false);
			this.setHeaderVisible(false);
			this.setBodyBorder(false);
			this.setShadow(false);
		}

		public void scrollInToView() {
			if (null != lastAddedScrollPanel) {
				int relativeXCorrdinate = (getAbsoluteLeft() - lastAddedScrollPanel.getAbsoluteLeft());
				int scrollWidth = lastAddedScrollPanel.getOffsetWidth();
				if (relativeXCorrdinate < 0 || relativeXCorrdinate > scrollWidth) {
					lastAddedScrollPanel.setHorizontalScrollPosition(relativeXCorrdinate);
				}

				int relativeYCoordinate = (getAbsoluteTop() - lastAddedScrollPanel.getAbsoluteTop());
				int scrollHeight = lastAddedScrollPanel.getOffsetHeight();
				if (relativeYCoordinate < 0 || relativeYCoordinate > scrollHeight) {
					lastAddedScrollPanel.setVerticalScrollPosition(relativeYCoordinate);
				}
				// lastAddedScrollPanel.setVerticalScrollPosition( last_Y_Cordinate + getOffsetHeight()-
				// overlayImage.getAbsoluteTop());
				// lastAddedScrollPanel.setHorizontalScrollPosition(last_X_Cordinate + getOffsetWidth()-
				// overlayImage.getAbsoluteLeft());
			}
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
				overlayList.clear();
			}
		}

		private void freeze(final OverlayImage image) {
			if (null != image) {
				originalWidth = getElement().getWidth(true);
				originalHeight = getElement().getHeight(true);
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

		@Override
		protected void onLoad() {
			super.onLoad();

			if (isScrollView) {
				Timer timer = new Timer() {

					public void run() {
						scrollInToView();
					};
				};
				timer.schedule(100);
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
			if (null != scrollPanel && scrollPanel != lastAddedScrollPanel) {
				lastAddedScrollPanel = scrollPanel;
				scrollPanel.addScrollHandler(new ScrollHandler() {

					@Override
					public void onScroll(final ScrollEvent event) {
						final List<Overlay> overlayList = Overlay.getOverlayList();
						if (!CollectionUtil.isEmpty(overlayList)) {
							for (final Overlay drawnOverlay : overlayList) {
								if (null != drawnOverlay) {
									drawnOverlay.redraw();
								}
							}
						}
					}
				});
			}
		}

		private void redraw() {
			final List<Overlay> imageOverlayList = Overlay.getOverlayList();
			if (!CollectionUtil.isEmpty(imageOverlayList) && this.overlayImage != null && this.overlayImage.isAttached()
					&& this.overlayImage.isVisible()) {
				final double widthFactor = ((this.overlayImage.getWidth() * 1.0) / this.imageWidth);
				final int left = (int) Math.round((this.original_X_Cordinate - this.imageLeft) * widthFactor);
				final int overlayWidth = (int) Math.round(this.originalWidth * widthFactor);
				final double heightFactor = ((this.overlayImage.getHeight() * 1.0) / this.imageHeight);
				final int top = (int) Math.round((this.original_Y_Cordinate - this.imageTop) * heightFactor);
				final int height = (int) Math.round(this.originalHeight * heightFactor);

				if (null == this.overlayImage.getImagePanel()) {
					this.setPosition(this.overlayImage.getAbsoluteLeft() + left, top + this.overlayImage.getAbsoluteTop());
					this.setWidth(overlayWidth);
					this.setHeight(height);
					this.freezePosition();
				} else {
					ContentPanel imagePanel = this.overlayImage.getImagePanel();

					int boundaryY0 = imagePanel.getAbsoluteTop();
					int overlayy0 = (top + this.overlayImage.getAbsoluteTop());
					int y0 = (height - (boundaryY0 - overlayy0));

					int boundaryY1 = imagePanel.getAbsoluteTop() + imagePanel.getOffsetHeight();
					int overlayY1 = overlayy0 + height;
					int y1 = (height - (overlayY1 - boundaryY1));

					int boundaryX0 = imagePanel.getAbsoluteLeft();
					int overlayx0 = (left + this.overlayImage.getAbsoluteLeft());
					int x0 = (overlayWidth - (boundaryX0 - overlayx0));

					int boundaryX1 = imagePanel.getAbsoluteLeft() + imagePanel.getOffsetWidth();
					int overlayX1 = overlayx0 + overlayWidth;
					int x1 = (overlayWidth - (overlayX1 - boundaryX1));

					int previousLeft = this.overlayImage.getAbsoluteLeft() + left;
					int previousTop = top + this.overlayImage.getAbsoluteTop();

					// Boundary Condition for X-axis
					if (overlayx0 < boundaryX0) {
						previousLeft = boundaryX0;
						this.setWidth(x0 > 0 ? x0 : 0);
						if (x0 < 15)
							this.setVisible(false);
					} else if (overlayX1 > boundaryX1) {
						this.setWidth(x1 > 0 ? x1 : 0);
						if (x1 < 15)
							this.setVisible(false);
					} else {
						this.setWidth(overlayWidth);
					}

					// Boundary Condition for Y-axis
					if (overlayy0 < boundaryY0) {
						previousTop = boundaryY0;
						this.setPosition(this.overlayImage.getAbsoluteLeft() + left, boundaryY0);
						this.setHeight(y0 > 0 ? y0 : 0);
						if (y0 < 15)
							this.setVisible(false);
					} else if (overlayY1 > boundaryY1) {
						this.setHeight(y1 > 0 ? y1 : 0);
						if (y1 < 15)
							this.setVisible(false);
					} else {
						this.setHeight(height);
						this.setVisible(true);
					}
					this.setPosition(previousLeft, previousTop);
					this.freezePosition();
				}

			}
		}

		public static int getOverlayCount() {
			return CollectionUtil.isEmpty(overlayList) ? 0 : overlayList.size();
		}

		// Method to redraw all overlays
		public static void redrawAll() {
			Timer timer = new Timer() {

				@Override
				public void run() {

					final List<Overlay> overlayList = Overlay.getOverlayList();
					if (!CollectionUtil.isEmpty(overlayList)) {
						for (final Overlay drawnOverlay : overlayList) {
							if (null != drawnOverlay) {
								drawnOverlay.redraw();
							}
						}
					}
				}
			};
			timer.schedule(50);
		}
	}

}
