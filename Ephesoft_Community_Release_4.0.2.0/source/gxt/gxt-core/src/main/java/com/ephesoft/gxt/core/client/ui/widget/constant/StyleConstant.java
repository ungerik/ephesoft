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

package com.ephesoft.gxt.core.client.ui.widget.constant;

/**
 * 
 * Constants that are used for styles and Class Names which are used to make Styling to the Widgets.
 * 
 * <p>
 * Basically, these are constants which are used to provide styles to the widgets.
 * </p>
 * 
 * @author Ephesoft.
 * @version 1.0.
 * 
 */
public final class StyleConstant {

	/**
	 * Private constructor declared so that the object of the class cannot be initialized.
	 */
	private StyleConstant() {
		throw new UnsupportedOperationException("The Object of the class cannot be initialized.");
	}

	/**
	 * Constant {@link String} class name of the selected item in a GWT Suggest Box.
	 */
	public static final String SUGGEST_BOX_SELECTED_ITEM_CLASS_APPENDER = "selected";

	/**
	 * Constant {@link String} for specifying field separator.
	 */
	public static final String FIELD_SEPERATOR = ":";

	/**
	 * Constant {@link String} containing HTML code for specifying color of the mandatory field indicator.
	 */
	public static final String HTML_FONT_COLOR_TAG = "<font color='red'>";

	/**
	 * Constant {@link String} for specifying mandatory field indicator.
	 */
	public static final String MANDATORY_STAR = "*";

	/**
	 * Constant {@link String} specifying HTML font end tag.
	 */
	public static final String HTML_END_FONT_TAG = "</font>";

	/**
	 * Css for improving the padding of buttons.
	 */
	public static final String GWT_SMALL_BUTTON_CSS = "gwt-small-button";
	
	
	/** 
	 * Css for specifying batch class key text box in key generator window.
	 */
	public static final String BATCH_CLASS_KEY_TEXT_BOXX_STYLE = "batchClassKeyTextBoxStyle";
}
