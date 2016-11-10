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

package com.ephesoft.dcma.gwt.core.client.ui.widget;

import com.ephesoft.dcma.gwt.core.shared.StringUtil;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

/**
 * Provides custom widget for adding label with image on UI. This widget provides supports for use case where label on UI needs icon
 * along with it.
 * 
 * <p>
 * It supports multiple icon position along the label defined by {@link ImagePosition}. By default, horizontal right is the image
 * position.
 * 
 * The <code>ImageLabel</code> class is also provides handling for adding click handlers.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Jun-2013 <br/>
 * @version 1.0
 * @see Composite
 * @see HasClickHandlers $LastChangedDate:$ <br/>
 *      $LastChangedRevision:$ <br/>
 */
public class ImageLabel extends Composite implements HasClickHandlers {

	/**
	 * labelIcon {@link Label} is used for holding the image as a background to empty label.
	 */
	private Label labelIcon = null;

	/**
	 * label {@link Label} is used for the label text to be shown.
	 */
	private Label label = null;

	/**
	 * labelTitle {@link String} is the title for the label shown on mouse hover.
	 */
	private String labelTitle = null;

	/**
	 * iconURLCSS {@link String} is for the CSS name of background image, so as to make it configurable through CSS.
	 */
	private String iconURLCSS = null;

	/**
	 * labelText {@link String} holds the text for label shown.
	 */
	private String labelText = null;

	/**
	 * imagePosition {@link ImagePosition} holds the image position constant, deciding the position of image.
	 */
	private ImagePosition imagePosition = null;

	/**
	 * horizontalPanel {@link HorizontalPanel} is the outermost panel used to hold the label and image horizontally.
	 */
	private HorizontalPanel horizontalPanel = null;

	/**
	 * verticalPanel {@link VerticalPanel} is the outermost panel used to hold the label and image vertically.
	 */
	private VerticalPanel verticalPanel = null;

	/**
	 * isImage is the boolean value that indicates if the image is available or not.
	 */
	private boolean isImage = Boolean.TRUE;

	/**
	 * isContainerHorizontal is the boolean value that indicates if the outermost panel is horizontal or not.
	 */
	private boolean isContainerHorizontal = Boolean.TRUE;

	/**
	 * Instantiates the object of {@link ImageLabel} using the parameters passed. All parameters values are taken care by the getter
	 * method which uses the constructor to instantiates the object.
	 * 
	 * <p>
	 * It cannot be instantiated directly from outside.
	 * 
	 * @param labelText {@link String} the label text value to be shown on UI
	 * @param iconURLCSS {@link String} the CSS name for background image on empty label
	 * @param labelTitle {@link String} the title of label shown on mouse hover
	 * @param imagePosition {@link ImagePosition} the position of label icon with respect to label
	 */
	protected ImageLabel(final String labelText, final String iconURLCSS, final String labelTitle, final ImagePosition imagePosition) {
		super();
		this.labelText = labelText;
		this.labelTitle = labelTitle;
		this.iconURLCSS = iconURLCSS;
		this.imagePosition = imagePosition;
		createImageLabelView();

		// Initialises widget depending upon the position of image icon.
		if (isContainerHorizontal) {
			initWidget(horizontalPanel);
		} else {
			initWidget(verticalPanel);
		}
	}

	/**
	 * Gets the instance of <code>ImageLabel</code> by handling all the parameters passed. All parameters passed can be NULL.
	 * 
	 * <p>
	 * It ensures the default positioning of image icon to the right of label horizontally. It also ensures label title is set to label
	 * text if not set explicitly.
	 * 
	 * @param labelText {@link String} the label text value to be shown on UI
	 * @param iconURLCSS {@link String} the CSS name for background image on empty label
	 * @param labelTitle {@link String} the title of label shown on mouse hover
	 * @param imagePosition {@link ImagePosition} the position of label icon with respect to label
	 * @return the instance of <code>ImageLabel</code> with respect to parameters passed
	 */
	public static ImageLabel getImageLabel(final String labelText, final String iconURL, final String labelTitle,
			final ImagePosition imagePosition) {
		ImageLabel imageLabel = null;
		final String tempLabelText = labelText;
		final String tempIconURL = iconURL;
		String tempLableTitle = labelTitle;
		ImagePosition tempImagePosition = imagePosition;

		// If title is empty or null but label text is given it sets the label title same as label text
		if (!StringUtil.isNullOrEmpty(tempLabelText) && StringUtil.isNullOrEmpty(tempLableTitle)) {
			tempLableTitle = tempLabelText;
		}

		// If no Image Position is specified it sets to default horizontally right
		if (null == tempImagePosition) {
			tempImagePosition = ImagePosition.HORIZONTAL_RIGHT;
		}
		imageLabel = new ImageLabel(tempLabelText, tempIconURL, tempLableTitle, tempImagePosition);
		return imageLabel;
	}

	/**
	 * Sets the inner component like label or image or panel alignment as all are rendered as
	 * <table>
	 * elements.
	 * <p>
	 * It sets the alignment horizontally inside the horizontal or vertical panel.
	 * 
	 * @param imageLabelComponent {@link ImageLabelComponent} the image label inner component for which alignment needs to be set
	 * @param horizontalAlignmentConstant {@link HasHorizontalAlignment} the type of horizontal alignment
	 */
	public void setCellHorizontalAlignment(final ImageLabelComponent imageLabelComponent,
			final HorizontalAlignmentConstant horizontalAlignmentConstant) {

		// Gets the component instance using the constant
		final Widget widget = getComponent(imageLabelComponent);

		// Select the outermost panel
		if (isContainerHorizontal) {
			horizontalPanel.setCellHorizontalAlignment(widget, horizontalAlignmentConstant);
		} else {
			verticalPanel.setCellHorizontalAlignment(widget, horizontalAlignmentConstant);
		}
	}

	/**
	 * Sets the inner component like label or image or panel alignment as all are rendered as
	 * <table>
	 * elements.
	 * <p>
	 * It sets the alignment vertically inside the horizontal or vertical panel.
	 * 
	 * @param imageLabelComponent {@link ImageLabelComponent} the image label inner component for which alignment needs to be set
	 * @param verticalAlignmentConstant {@link HasHorizontalAlignment} the type of vertical alignment
	 */
	public void setCellVerticalAlignment(final ImageLabelComponent imageLabelComponent,
			final VerticalAlignmentConstant verticalAlignmentConstant) {
		final Widget widget = getComponent(imageLabelComponent);
		if (isContainerHorizontal) {
			horizontalPanel.setCellVerticalAlignment(widget, verticalAlignmentConstant);
		} else {
			verticalPanel.setCellVerticalAlignment(widget, verticalAlignmentConstant);
		}
	}

	/**
	 * Adds the style class to the inner component using the <code>ImageLabelComponent</code> to define on which component style needs
	 * to be added.
	 * 
	 * @param imageLabelComponent {@link ImageLabelComponent} the image label inner component for which style needs to be added
	 * @param style {@link String} name of style class
	 */
	public void addComponentStyle(final ImageLabelComponent imageLabelComponent, final String style) {
		final Widget widget = getComponent(imageLabelComponent);
		if (null != widget && !StringUtil.isNullOrEmpty(style)) {
			widget.addStyleName(style);
		}
	}

	/**
	 * Sets the label text value. It can be a empty string.
	 * 
	 * @param labelText {@link String} the value of label text
	 */
	public void setLabelText(final String labelText) {
		if (null != labelText) {
			label.setText(labelText);
		}
	}

	/**
	 * Sets the label title value. It can be a empty string.
	 * 
	 * @param labelTitle {@link String} the value of label title
	 */
	public void setLabelTitle(final String labelTitle) {
		if (null != labelTitle) {
			label.setTitle(labelTitle);
		}
	}

	/**
	 * Creates the image label view using the parameters set during the instantiation of <code>ImageLabel</code>.
	 */
	private void createImageLabelView() {
		label = new Label(labelText);
		label.setTitle(labelTitle);

		// If no CSS name is given means this is label without an image
		if (null == iconURLCSS || iconURLCSS.isEmpty()) {
			isImage = Boolean.FALSE;
		} else {
			labelIcon = new Label();
			labelIcon.addStyleName(iconURLCSS);
		}
		setImageLabelContainer();
	}

	/**
	 * Sets the image label outermost panel depending upon the ImagePosition.
	 * 
	 * <p>
	 * By default image position is set to horizontally right
	 */
	private void setImageLabelContainer() {
		switch (imagePosition) {
			case HORIZONTAL_LEFT:
				setHorizontalContainer(Boolean.FALSE);
				break;
			case HORIZONTAL_RIGHT:
				setHorizontalContainer(Boolean.TRUE);
				break;
			case VERTICAL_TOP:
				setVerticalContainer(Boolean.TRUE);
				break;
			case VERTICAL_BOTTOM:
				setVerticalContainer(Boolean.FALSE);
				break;
			default:
				setHorizontalContainer(Boolean.TRUE);
				break;
		}
	}

	/**
	 * Sets the outermost panel as horizontal panel. It add the image to the left of label or to the right depending upon the parameter
	 * passed.
	 * 
	 * @param isImageRight the true value means image is added to the right of label and false means to the left
	 */
	private void setHorizontalContainer(final boolean isImageRight) {
		horizontalPanel = new HorizontalPanel();
		if (isImage) {
			if (isImageRight) {
				horizontalPanel.add(label);
				horizontalPanel.add(labelIcon);
			} else {
				horizontalPanel.add(labelIcon);
				horizontalPanel.add(label);
			}
		} else {
			horizontalPanel.add(label);
		}
		isContainerHorizontal = Boolean.TRUE;
	}

	/**
	 * Sets the outermost panel as vertical panel. It add the image to the top of label or to the bottom depending upon the parameter
	 * passed.
	 * 
	 * @param isImageTop the true value means image is added to the top of label and false means to the bottom
	 */
	private void setVerticalContainer(final boolean isImageTop) {
		verticalPanel = new VerticalPanel();
		if (isImage) {
			if (isImageTop) {
				verticalPanel.add(labelIcon);
				verticalPanel.add(label);
			} else {
				verticalPanel.add(label);
				verticalPanel.add(labelIcon);
			}
		} else {
			verticalPanel.add(label);
		}
		isContainerHorizontal = Boolean.FALSE;
	}

	/**
	 * Gets the instance of inner components depending upon the parameter passed.
	 * 
	 * <p>
	 * It returns null if parameter passed is wrong.
	 * 
	 * @param imageLabelComponent {@link ImageLabelComponent} the inner component constant
	 * @return the instance of inner component
	 */
	private Widget getComponent(final ImageLabelComponent imageLabelComponent) {
		Widget widget = null;
		if (null != imageLabelComponent) {
			switch (imageLabelComponent) {
				case LABEL:
					widget = label;
					break;
				case IMAGE:
					widget = labelIcon;
					break;
				case CONTAINER:
					widget = (isContainerHorizontal) ? horizontalPanel : verticalPanel;
					break;
				default:
					widget = null;
					break;
			}
		}
		return widget;
	}

	/**
	 * Provides constants for defining image positions with respect to label.
	 * 
	 * @author Ephesoft
	 * 
	 *         <b>created on</b> 21-Jun-2013 <br/>
	 * @version 1.0 $LastChangedDate:$ <br/>
	 *          $LastChangedRevision:$ <br/>
	 */
	public static enum ImagePosition {
		/**
		 * To the left of label.
		 */
		HORIZONTAL_LEFT,

		/**
		 * To the right of label.
		 */
		HORIZONTAL_RIGHT,

		/**
		 * To the top of label.
		 */
		VERTICAL_TOP,

		/**
		 * To the bottom of label.
		 */
		VERTICAL_BOTTOM;
	}

	/**
	 * Provides constant for defining inner components of <code>ImageLabel</code>
	 * 
	 * @author Ephesoft
	 * 
	 *         <b>created on</b> 21-Jun-2013 <br/>
	 * @version 1.0 $LastChangedDate:$ <br/>
	 *          $LastChangedRevision:$ <br/>
	 */
	public static enum ImageLabelComponent {
		/**
		 * To refer label used.
		 */
		LABEL,

		/**
		 * To refer empty label used as Image.
		 */
		IMAGE,

		/**
		 * To refer outermost panel containing ImageLabel.
		 */
		CONTAINER
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler clickHandler) {
		return addDomHandler(clickHandler, ClickEvent.getType());
	}

}
