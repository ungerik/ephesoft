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

package com.ephesoft.gxt.core.client.util;

/**
 * Utilities that call the native javascript functions defined in different js files within the application.
 * 
 * @author Ephesoft
 * @see NativeUtil
 * @version 1.0
 * 
 */
public class NativeUtil {

	/**
	 * Adds the jquery zoom functionality on the image container element whose id is passed as a parameter.
	 * <p>
	 * Parent element can be span, div or table element.
	 * 
	 * @param containerId {@link String} Id of the parent element of image element. Cannot be NULL or empty.
	 */
	public static native void addZoomProperty(final String containerId) /*-{
																		return $wnd.addZoomProperty(containerId);
																		}-*/;

	/**
	 * Removes the jquery zoom functionality on the image container element whose id is passed as a parameter.
	 * <p>
	 * Parent element can be span, div or table element.
	 * 
	 * @param containerId {@link String} Id of the parent element of image element. Cannot be NULL or empty.
	 */
	public static native void removeZoomProperty(final String containerId) /*-{
																			return $wnd.removeZoomProperty(containerId);
																			}-*/;

	/**
	 * Adds jquery resizable functionality to the child id passed along with the id passed as resizable.
	 * 
	 * <p>
	 * Width and height needs to be passed below which the element will not resize further.
	 * 
	 * @param resizableId {@link String} id of element which needs to be resizable along with child id.
	 * @param childId {@link String} id of element which needs to be resizable.
	 * @param width minimum width of child element.
	 * @param height maximum width of child element.
	 */
	public static native void resizeElements(final String resizableId, final String childId, final int width, final int height) /*-{
																																return $wnd.resizeElements(resizableId, childId, width, height);
																																}-*/;

}
