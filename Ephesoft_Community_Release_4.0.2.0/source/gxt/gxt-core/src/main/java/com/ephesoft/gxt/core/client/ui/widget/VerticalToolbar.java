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

import com.ephesoft.gxt.core.client.i18n.LocaleConstants;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;

/**
 * Vertical Toolbar is a custom widget which provides tools to work around the images.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class VerticalToolbar extends Composite {

	/**
	 * Rotatable Image used to contain the image and perform various operations on it.
	 */
	private RotatableImage overlayImage;

	/**
	 * Border Layout Container contains all the widgets that are used to crate the toolbar.
	 */
	private final BorderLayoutContainer borderLayoutContainer;

	/**
	 * Toolbar Panel contains all the tool buttons.
	 */
	private VerticalPanel toolbarPanel;

	// Constant to specify the border size of the BorderLayoutContainer.
	private static final double BORDER_LAYOUT_DATA_SIZE = 0.022;

	// Constant for specifing the move button css style name.
	private static final String BUTTON_CSS = "move_toolbar_button";

	// Constant for specifing the toolbar container css style name.
	private static final String TOOLBAR_CONTAINER_CSS = "toolbarContainer";

	private VerticalPanel imagePanel;

	private final ScrollPanel imageScrollPanel;

	/**
	 * Constructor without the image.
	 */
	public VerticalToolbar() {
		this(null);
	}

	/**
	 * Constructor with the Rotatable Image.
	 * 
	 * @param image {@link RotatableImage}
	 */
	public VerticalToolbar(final RotatableImage image) {
		super();
		imagePanel = new VerticalPanel();
		imagePanel.addStyleName("overlayPanel");
		imagePanel.addStyleName("overlayPanelInitialView");
		toolbarPanel = new VerticalPanel();
		toolbarPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		// setting swap toolbar button.
		final Button moveToolbarButton = new Button();
		moveToolbarButton.setStyleName(BUTTON_CSS);
		moveToolbarButton.setTitle(LocaleConstants.MOVE_RIGHT);
		moveToolbarButton.addStyleName("btnfirst");
		toolbarPanel.add(moveToolbarButton);

		borderLayoutContainer = new BorderLayoutContainer();
		if (null == image) {
			overlayImage = new RotatableImage();
		} else {
			overlayImage = image;
		}
		imageScrollPanel = new ScrollPanel();
		imageScrollPanel.addStyleName("overlayImageScrollPanel");
		imagePanel.addStyleName("overlayImagePanel");
		imagePanel.add(overlayImage);
		imageScrollPanel.add(imagePanel);
		borderLayoutContainer.setCenterWidget(imageScrollPanel);

		final VerticalPanel toolbarPanelContainer = new VerticalPanel();
		toolbarPanelContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		toolbarPanelContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		toolbarPanelContainer.setStyleName(TOOLBAR_CONTAINER_CSS);
		toolbarPanelContainer.add(toolbarPanel);
		final BorderLayoutData regionLayout = new BorderLayoutData(BORDER_LAYOUT_DATA_SIZE);
		borderLayoutContainer.setWestWidget(toolbarPanelContainer, regionLayout);

		final ClickHandler switchToolbar = new ClickHandler() {

			public void onClick(final ClickEvent event) {
				final Widget toolbar = borderLayoutContainer.getEastWidget();
				imagePanel.removeStyleName("overlayPanelInitialView");
				if (null == toolbar) {
					toolbarPanelContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
					borderLayoutContainer.setWestWidget(null);
					borderLayoutContainer.setEastWidget(toolbarPanelContainer);
					toolbarPanelContainer.addStyleName("toolbarEast");
					moveToolbarButton.addStyleName("move_toolbar_button_east");
					moveToolbarButton.setTitle(LocaleConstants.MOVE_LEFT);
				} else {
					borderLayoutContainer.setEastWidget(null);
					toolbarPanelContainer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
					borderLayoutContainer.setWestWidget(toolbarPanelContainer);
					toolbarPanelContainer.removeStyleName("toolbarEast");
					moveToolbarButton.removeStyleName("move_toolbar_button_east");
					moveToolbarButton.setTitle(LocaleConstants.MOVE_RIGHT);
				}
				toolbarPanelContainer.add(getToolbaarPanel());
				borderLayoutContainer.forceLayout();
			}
		};
		moveToolbarButton.addClickHandler(switchToolbar);
		initWidget(borderLayoutContainer);
		this.addStyleName("imageOverlayContainer");
	}

	/**
	 * Gets the Border Layout Container.
	 * 
	 * @return {@link BorderLayoutContainer}
	 */
	public BorderLayoutContainer getBorderLayoutContainer() {
		return borderLayoutContainer;
	}

	/**
	 * Gets the Rotatable Image.
	 * 
	 * @return {@link RotatableImage}
	 */
	public RotatableImage getOverlayImage() {
		return overlayImage;
	}

	public void setOverlayImage(final OverlayImage overlayImage) {
		imagePanel.clear();
		if (null != overlayImage) {
			this.overlayImage = overlayImage;
			overlayImage.setRepositioningScroll(imageScrollPanel);
			imagePanel.add(overlayImage);
			WidgetUtil.setID(overlayImage, "overlay-image");
			overlayImage.addStyleName("overlayDisplayImage");
			this.disableDragDrop(overlayImage);
			borderLayoutContainer.forceLayout();
		}
	}

	public void setOverlayImage(final RotatableImage overlayImage) {
		imagePanel.clear();
		if (null != overlayImage) {
			this.overlayImage = overlayImage;
			imagePanel.add(overlayImage);
			WidgetUtil.setID(overlayImage, "overlay-image");
			overlayImage.addStyleName("overlayDisplayImage");
			this.disableDragDrop(overlayImage);
			borderLayoutContainer.forceLayout();
		}
	}

	public void clearImage() {
		imagePanel.clear();
	}

	private void disableDragDrop(final RotatableImage overlayImage) {
		overlayImage.addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(final MouseDownEvent event) {
				event.preventDefault();
			}
		});
	}

	/**
	 * Adds a new button to the toolbar.
	 * 
	 * @param button {@link Button}
	 */
	public void addButton(final Button button) {
		if (null != button) {
			toolbarPanel.add(button);
			// button.addStyleName("iconButton");
		}
	}

	/**
	 * Gets the Vertical Panel of toolbar.
	 * 
	 * @return {@link VerticalPanel}
	 */
	public VerticalPanel getToolbaarPanel() {
		return toolbarPanel;
	}

	/**
	 * Sets the vertical panel of the toolbar.
	 * 
	 * @param toolbaarPanel {@link VerticalPanel}
	 */
	public void setToolbaarPanel(final VerticalPanel toolbaarPanel) {
		this.toolbarPanel = toolbaarPanel;
	}

	public void adjustScroll(final int xOffset, final int yOffset) {
		final int newHorizontalScrollPosition = imageScrollPanel.getHorizontalScrollPosition() + xOffset;
		final int newVerticalScrollPosition = imageScrollPanel.getVerticalScrollPosition() + yOffset;
		imageScrollPanel.setHorizontalScrollPosition(newHorizontalScrollPosition);
		imageScrollPanel.setVerticalScrollPosition(newVerticalScrollPosition);
	}

	public VerticalPanel getImagePanel() {
		return imagePanel;
	}

	public void setImagePanel(VerticalPanel imagePanel) {
		this.imagePanel = imagePanel;
	}
}
