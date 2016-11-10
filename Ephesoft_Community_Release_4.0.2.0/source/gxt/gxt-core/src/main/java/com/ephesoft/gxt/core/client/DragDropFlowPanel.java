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

package com.ephesoft.gxt.core.client;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.gxt.core.client.ui.widget.listener.ImageSelectionListener;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.dom.client.HasDragLeaveHandlers;
import com.google.gwt.event.dom.client.HasDragOverHandlers;
import com.google.gwt.event.dom.client.HasDropHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class DragDropFlowPanel extends FlowPanel implements HasDropHandlers, HasDragOverHandlers, HasDragLeaveHandlers {

	private static DragImage lastDraggedOverimage;

	private int lastDraggedOverimageIndex;

	private static DragImage draggedImage;

	private boolean dragEventScroll = false;

	private ImageSelectionListener imageSelectionListener;

	private String title;

	public DragDropFlowPanel() {
		this.attachHandlers();
	}

	/**
	 * @param imageSelectionListener the imageSelectionListener to set
	 */
	public void setImageSelectionListener(final ImageSelectionListener imageSelectionListener) {
		this.imageSelectionListener = imageSelectionListener;
	}

	@Override
	public HandlerRegistration addDropHandler(final DropHandler handler) {
		return addBitlessDomHandler(handler, DropEvent.getType());
	}

	@Override
	public HandlerRegistration addDragOverHandler(final DragOverHandler handler) {
		return addBitlessDomHandler(handler, DragOverEvent.getType());
	}

	@Override
	public HandlerRegistration addDragLeaveHandler(final DragLeaveHandler handler) {
		return addBitlessDomHandler(handler, DragLeaveEvent.getType());
	}

	protected DragImage getDraggableImage(final DropEvent event) {
		DragImage image = null;
		final String imageURI = event.getData(DragImage.DRAGGABLE_IMAGE_URI);
		if (!StringUtil.isNullOrEmpty(imageURI)) {
			image = new DragImage(imageURI);
		}
		return image;
	}

	private DragImage getImageByUrl(String url) {
		DragImage image = null;
		if (!StringUtil.isNullOrEmpty(url)) {
			int totalWidget = this.getWidgetCount();
			for (int index = 0; index < totalWidget; index++) {
				Widget widget = getWidget(index);
				if (widget instanceof DragImage && (url.equalsIgnoreCase(((DragImage) widget).getUrl()))) {
					image = (DragImage) widget;
				}
			}
		}
		return image;
	}

	@Override
	public void add(final Widget widget) {
		super.add(widget);
		if (widget instanceof DragImage) {
			final DragImage draggableImage = (DragImage) widget;
			draggableImage.addImageSelectionListener(imageSelectionListener);
		}
	}

	public void insert(final Widget widget, final int beforeIndex) {
		super.insert(widget, beforeIndex);
		if (widget instanceof DragImage) {
			final DragImage draggableImage = (DragImage) widget;
			draggableImage.addImageSelectionListener(imageSelectionListener);
		}
	}

	private void attachHandlers() {
		this.addDragOverHandler(new DragOverHandler() {

			@Override
			public void onDragOver(final DragOverEvent event) {
				dragEventScroll = true;
			}
		});

		this.addDragLeaveHandler(new DragLeaveHandler() {

			@Override
			public void onDragLeave(final DragLeaveEvent event) {
			}
		});

		this.addDropHandler(new DropHandler() {

			@Override
			public void onDrop(final DropEvent event) {
				event.preventDefault();
				DragDropFlowPanel.this.getElement().getStyle().clearBackgroundColor();
				final int widgetIndex = DragDropFlowPanel.this.getWidgetIndex(DragDropFlowPanel.lastDraggedOverimage);
				setLastDraggedOverimageIndex(widgetIndex);
				final DragImage dragImage = getDraggableImage(event);
				if (null != dragImage && null != DragDropFlowPanel.draggedImage) {
					DragImage image = getImageByUrl(draggedImage.getUrl());
					if (null != image) {
						title = DragDropFlowPanel.this.getWidget(widgetIndex).getTitle();
						DragDropFlowPanel.this.remove(image);
					}
					if (widgetIndex != -1) {
						DragDropFlowPanel.this.insert(dragImage, widgetIndex);
					} else {
						DragDropFlowPanel.this.add(dragImage);
					}
					if (null != imageSelectionListener) {
						imageSelectionListener.onDrop(dragImage, getImageURLs(), title);
					}
				}
			}
		});
	}

	public List<String> getImageURLs() {
		final int totalWidgets = this.getWidgetCount();
		final List<String> imageURLList = new ArrayList<String>(totalWidgets);
		Widget traversedWidget;
		for (int index = 0; index < totalWidgets; index++) {
			traversedWidget = this.getWidget(index);
			if (traversedWidget instanceof DragImage) {
				imageURLList.add(((DragImage) traversedWidget).getUrl());
			}
		}
		return imageURLList;
	}

	/**
	 * @return the lastDraggedOverimage
	 */
	public static DragImage getLastDraggedOverimage() {
		return lastDraggedOverimage;
	}

	public static class DragImage extends Image {

		public static final String DRAGGABLE_IMAGE_URI = "uri";
		public static final String BINDED_OBJECT = "bindedObject";
		private static final char URL_SEPERATOR = '/';
		private String title;

		public DragImage(final String safeUri) {
			super(safeUri);
			this.initDnD();
			this.addStyleName("draggableImage");
			this.addMouseDownHandler();
		}

		public void setUrl(final String url, final Direction direction) {
			if (null != direction && !StringUtil.isNullOrEmpty(url)) {
				final int lastIndex = url.lastIndexOf(URL_SEPERATOR);
				final String baseURL = url.substring(0, lastIndex + 1);
				final String imageName = url.substring(lastIndex + 1, url.length());
				final String newURL = StringUtil.concatenate(baseURL, direction, URL_SEPERATOR, imageName);
				setUrl(newURL);
			} else {
				setUrl(url);
			}
		}

		private void addMouseDownHandler() {
			this.addMouseDownHandler(new MouseDownHandler() {

				@Override
				public void onMouseDown(final MouseDownEvent event) {
					final Widget parent = DragImage.this.getParent();
					if (parent instanceof DragDropFlowPanel) {
						final int widgetCount = ((DragDropFlowPanel) parent).getWidgetCount();
						if (widgetCount <= 1) {
							event.preventDefault();
						}
					}
				}
			});
		}

		private void initDnD() {
			getElement().setDraggable(Element.DRAGGABLE_TRUE);

			addDragStartHandler(new DragStartHandler() {

				@Override
				public void onDragStart(final DragStartEvent event) {
					event.setData(DRAGGABLE_IMAGE_URI, getUrl());
					draggedImage = DragImage.this;
				}
			});

			this.addDragLeaveHandler(new DragLeaveHandler() {

				@Override
				public void onDragLeave(DragLeaveEvent event) {
					DragDropFlowPanel.lastDraggedOverimage = null;
				}
			});
			this.addDragEnterHandler(new DragEnterHandler() {

				@Override
				public void onDragEnter(final DragEnterEvent event) {
					DragDropFlowPanel.lastDraggedOverimage = DragImage.this;
					lastDraggedOverimage.getElement().scrollIntoView();
				}
			});
		}

		public void addImageSelectionListener(final ImageSelectionListener imageSelectionListener) {
			if (null != imageSelectionListener) {
				this.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(final ClickEvent event) {
						imageSelectionListener.onSelect(DragImage.this);
					}
				});
			}
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}
	}

	public int getLastDraggedOverimageIndex() {
		return lastDraggedOverimageIndex;
	}

	public void setLastDraggedOverimageIndex(int lastDraggedOverimageIndex) {
		this.lastDraggedOverimageIndex = lastDraggedOverimageIndex;
	}
}
