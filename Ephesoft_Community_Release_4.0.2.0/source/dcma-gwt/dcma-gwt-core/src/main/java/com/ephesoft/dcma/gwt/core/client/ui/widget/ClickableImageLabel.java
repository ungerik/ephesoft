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

import com.ephesoft.dcma.gwt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.dcma.gwt.core.client.ui.widget.ImageLabel.ImagePosition;

/**
* Provides custom widget for adding click-able image label on UI. This is use for use case where label needs to be click-able
* and also needs icons.
* 
* The <code>ClickableImageLabel</code> extends the {@link ImageLabel} to support the use of icon with label.
* 
* 
* @author  Ephesoft
* 
* <b>created on</b>  21-Jun-2013 <br/>
* @version 1.0
* @see ImageLabel
* $LastChangedDate:$ <br/>
* $LastChangedRevision:$ <br/>
*/
public final class ClickableImageLabel extends ImageLabel {

	/**
	 * isClickable to indicate that label is click-able or not.
	 */
	private boolean isClickable = Boolean.TRUE;

	/**
	 * {@link BackgroundColor} to hold the background color of label, default sets to transparent.
	 */
	private BackgroundColor backgroundColor = null;

	/**
	 * Instantiates the object of {@link ClickableImageLabel} using the parameters passed. All parameters values are taken care by the getter
	 * method which uses the constructor to instantiates the object.
	 * 
	 * <p>
	 * It cannot be instantiated directly from outside.
	 * 
	 * @param labelText {@link String} the label text value to be shown on UI
	 * @param iconURLCSS {@link String} the CSS name for background image on empty label
	 * @param labelTitle {@link String} the title of label shown on mouse hover
	 * @param imagePosition {@link ImagePosition} the position of label icon with respect to label
	 * @param isClickable the value indicating whether label is click-able or not
	 * @param backgroundColor {@link BackgroundColor} the background color of label
	 */
	private ClickableImageLabel(final String labelText, final String iconURL, final String labelTitle, final ImagePosition imagePosition, final boolean isClickable,
			final BackgroundColor backgroundColor) {
		super(labelText, iconURL, labelTitle, imagePosition);
		this.isClickable = isClickable;
		this.backgroundColor = backgroundColor;
		createClickableImageLabel();
	}

	/**
	 * Gets the instance of <code>ClickableImageLabel</code> by handling all the parameters passed. All parameters passed can be NULL.
	 * 
	 * <p>
	 * It ensures the default positioning of image icon to the right of label horizontally. It also ensures label background color is 
	 * set to transparent if not specified.
	 * 
	 * @param labelText {@link String} the label text value to be shown on UI
	 * @param iconURLCSS {@link String} the CSS name for background image on empty label
	 * @param labelTitle {@link String} the title of label shown on mouse hover
	 * @param imagePosition {@link ImagePosition} the position of label icon with respect to label
	 * @param isClickable the value indicating whether label is click-able or not
	 * @param backgroundColor {@link BackgroundColor} the background color of label
	 * @return the instance of <code>ClickableImageLabel</code> with respect to parameters passed
	 */
	public static ClickableImageLabel getClickableImageLabel(final String labelText, final String iconURL, final String labelTitle,
			final ImagePosition imagePosition, final boolean isClickable, final BackgroundColor backgroundColor) {
		ClickableImageLabel clickableImageLabel = null;
		final String tempLabelText = labelText;
		final String tempIconURL = iconURL;
		final String tempLabelTitle = labelTitle;
		ImagePosition tempImagePosition = imagePosition;
		final boolean isClickableTemp = isClickable;
		BackgroundColor tempBackgroundColor = backgroundColor;
		
		// If background color is not specified
		if (null == tempBackgroundColor) {
			tempBackgroundColor = BackgroundColor.TRANSPARENT;
		}
		
		// If image position is not specified
		if (null == imagePosition) {
			tempImagePosition = ImagePosition.HORIZONTAL_RIGHT;
		}
		clickableImageLabel = new ClickableImageLabel(tempLabelText, tempIconURL, tempLabelTitle, tempImagePosition, isClickableTemp,
				tempBackgroundColor);
		return clickableImageLabel;
	}

	/**
	 * Creates the Click-able image Label view using the parameters passed and also adding the default CSS
	 * for showing it as a click-able or non-click able link.
	 */
	private void createClickableImageLabel() {
		if (isClickable) {
			addComponentStyle(ImageLabelComponent.LABEL, CoreCommonConstants.LABEL_LINK_CSS);
			addComponentStyle(ImageLabelComponent.LABEL, CoreCommonConstants.CURSOR_HAND);
		} else {
			addComponentStyle(ImageLabelComponent.LABEL, CoreCommonConstants.BOLD_TEXT_CSS);
		}
		addComponentStyle(ImageLabelComponent.CONTAINER, backgroundColor.getBackgroundCSSName());
	}

	/**
	* Provides constant for defining the background color of label.
	* 
	* @author  Ephesoft
	* 
	* <b>created on</b>  21-Jun-2013 <br/>
	* @version 1.0
	* $LastChangedDate:$ <br/>
	* $LastChangedRevision:$ <br/>
	*/
	public static enum BackgroundColor {

		/**
		 * To define yellow background color.
		 */
		YELLOW("backgroundYellow"),

		/**
		 * To define blue background color.
		 */
		BLUE("backgroundBlue"),

		/**
		 * To define green background color.
		 */
		GREEN("backgroundGreen"),

		/**
		 * To define red background color.
		 */
		RED("backgroundRed"),

		/**
		 * To define transparent background color.
		 */
		TRANSPARENT("backgroundTransparent");

		/**
		 * To hold the CSS class name for the background color.
		 */
		private String backgroundColorCSS;

		/**
		 * Instantiates background color constant with CSS name passed.
		 * 
		 * @param backgroundColorCSS {@link String} the name of CSS class name.
		 */
		private BackgroundColor(final String backgroundColorCSS) {
			this.backgroundColorCSS = backgroundColorCSS;
		}

		/**
		 * Gets the name of CSS class used for the constant.
		 * 
		 * @return {@link String} the CSS class name.
		 */
		private String getBackgroundCSSName() {
			return backgroundColorCSS;
		}
	}

}
