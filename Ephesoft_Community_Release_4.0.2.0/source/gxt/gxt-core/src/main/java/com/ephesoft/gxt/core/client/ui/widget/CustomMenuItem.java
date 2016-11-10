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

import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

public class CustomMenuItem extends MenuItem {

	/**
	 * Constructs a new menu item that fires a command when it is selected.
	 * 
	 * @param html the item's html text
	 */
	public CustomMenuItem(SafeHtml html) {
		super(SafeHtmlUtils.fromSafeConstant(addTextInsideDiv(html.asString())));
	}

	/**
	 * Constructs a new menu item that fires a command when it is selected.
	 * 
	 * @param html the item's text
	 * @param cmd the command to be fired when it is selected
	 */
	public CustomMenuItem(SafeHtml html, ScheduledCommand cmd) {
		super(SafeHtmlUtils.fromSafeConstant(addTextInsideDiv(html.asString())), cmd);
	}

	/**
	 * Constructs a new menu item that cascades to a sub-menu when it is selected.
	 * 
	 * @param html the item's text
	 * @param subMenu the sub-menu to be displayed when it is selected
	 */
	public CustomMenuItem(SafeHtml html, MenuBar subMenu) {
		super(SafeHtmlUtils.fromSafeConstant(addTextInsideDiv(html.asString())), subMenu);
		this.addStyleName("gwt-MenuItem-dropdown");
	}

	/**
	 * Constructs a new menu item that fires a command when it is selected.
	 * 
	 * @param text the item's text
	 * @param cmd the command to be fired when it is selected
	 */
	public CustomMenuItem(String text, ScheduledCommand cmd) {
		this(SafeHtmlUtils.fromSafeConstant(addTextInsideDiv(text)), cmd);
	}

	/**
	 * Constructs a new menu item that cascades to a sub-menu when it is selected.
	 * 
	 * @param text the item's text
	 * @param subMenu the sub-menu to be displayed when it is selected
	 */
	public CustomMenuItem(String text, MenuBar subMenu) {
		this(SafeHtmlUtils.fromSafeConstant(addTextInsideDiv(text)), subMenu);
		this.addStyleName("gwt-MenuItem-dropdown");
	}

	private static String addTextInsideDiv(String text) {
		final Element parentElement = Document.get().createDivElement();
		final Element element = Document.get().createDivElement();
		element.setInnerHTML(text);
		parentElement.appendChild(element);
		return parentElement.getInnerHTML();
	}

	@Override
	protected void setSelectionStyle(boolean selected) {
		MenuBar subMenu = this.getSubMenu();
		if(subMenu != null) {
			super.setSelectionStyle(selected);
		}
	}

	@Override
	public void setText(String text) {
		super.setHTML(addTextInsideDiv(text));
	}
}
