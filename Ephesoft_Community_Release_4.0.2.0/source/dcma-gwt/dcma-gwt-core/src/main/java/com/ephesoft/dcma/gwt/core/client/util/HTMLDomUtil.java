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

package com.ephesoft.dcma.gwt.core.client.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dom.client.Element;

/**
 * A Utility Class which provides the functionality of making changes in the DOM structure of HTML Page. It uses native javascripts to
 * make those changes.
 * <p>
 * It provides functionality to modify content under the tag by its name,id etc.
 * 
 * @author Ephesoft
 * 
 */
public final class HTMLDomUtil {

	private HTMLDomUtil() {
		// Do Nothing.
	}

	/**
	 * Sets the tag content under the first tag found by <code> tagName </code> to the value passed as an argument.
	 * 
	 * @param tagName {@link String} tagName under which the value is to be altered. If <code>NULL</code>, would be discarded.
	 * @param value {@link String} value to be set for the tag.
	 */
	public static void setContentByTagName(final String tagName, final String value) {
		if (tagName != null && tagName.trim().length() > 0) {
			Map<String, String> tagMap = new HashMap<String, String>(1);
			tagMap.put(tagName, value);
		}
	}

	/**
	 * Sets the values under the tag with the name as Map Key to the values in the map.
	 * 
	 * @param tagMap : {@link Map} Key Value pairs where key will be the tag Name and value will be the content to be set. If
	 *            <code>NULL</code> or empty the request would be discarded.
	 */
	public static void setContentByTagName(final Map<String, String> tagMap) {
		if (null != tagMap && tagMap.size() > 0) {
			Set<String> keySet = tagMap.keySet();
			for (String tagName : keySet) {
				if (tagName != null && tagName.trim().length() > 0) {
					setElementContentByName(tagName, tagMap.get(tagName));
				}
			}
		}
	}

	/**
	 * Sets the values under the tag with the ID as Map Key to the values in the map.
	 * 
	 * @param tagMap : {@link Map} Key Value pairs where key will be the tag ID and value will be the content to be set. If null or
	 *            empty the request would be discarded.
	 */
	public static void setContentByTagID(final Map<String, String> tagMap) {
		if (null != tagMap && tagMap.size() > 0) {
			Set<String> keySet = tagMap.keySet();
			for (String tagID : keySet) {
				setElementContentByID(tagID, tagMap.get(tagID));
			}
		}
	}

	/**
	 * Sets the tag content under the first tag found by <code>tagName</code> to the value passed as an argument.
	 * 
	 * @param tagName {@link String} tagName under which the value is to be altered. If null, would be discarded.
	 * @param value {@link String} value to be set for the tag.
	 */
	public static void setContentByTagID(final String tagID, final String value) {
		if (null != tagID) {
			Map<String, String> tagMap = new HashMap<String, String>(1);
			tagMap.put(tagID, value);
			setContentByTagID(tagMap);
		}
	}

	/**
	 * Calls the native javascript function to set the value under the tag with <code>tagName</code>
	 * 
	 * @param tagName {@link String} tagName under which the value is to be altered.
	 * @param value {@link String} value to be set for the tag.
	 */
	private static native void setElementContentByName(String tagName, String value)/*-{
																					return $wnd.setTagValue(tagName,value);
																					}-*/;

	/**
	 * Calls the native javascript function to set the value with <code>tagID</code>
	 * 
	 * @param tagName {@link String} tagID under which the value is to be altered.
	 * @param value {@link String} value to be set for the tag.
	 */
	private static native void setElementContentByID(String tagID, String value) /*-{
																					return $wnd.setIDValue(tagID,value);
																					}-*/;

	/**
	 * Return the DOM element currently in focus.
	 * 
	 * @return {@link Element} focused element in the DOM Script.
	 */
	public static native Element getFocusedElement() /*-{
														return $wnd.document.activeElement;
														}-*/;

	public static native Element elementInFocus(Element element) /*-{
																	return element.ownerDocument.activeElement;
																	}-*/;

}
