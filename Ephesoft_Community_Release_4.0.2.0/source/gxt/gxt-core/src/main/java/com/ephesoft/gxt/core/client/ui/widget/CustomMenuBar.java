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
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.PopupPanel;

public class CustomMenuBar extends MenuBar {

	public CustomMenuBar() {
		this(false);
	}

	public CustomMenuBar(boolean vertical) {
		super(vertical);
		if (vertical) {

		}
	}

	/**
	 * Adds a menu item to the bar, that will open the specified menu when it is selected.
	 * 
	 * @param html the item's html text
	 * @param popup the menu to be cascaded from it
	 * @return the {@link MenuItem} object created
	 */
	public MenuItem addItem(SafeHtml html, MenuBar popup) {
		return this.addItem(html.asString(), true, popup);
	}

	/**
	 * Adds a menu item to the bar, that will open the specified menu when it is selected.
	 * 
	 * @param text the item's text
	 * @param popup the menu to be cascaded from it
	 * @return the {@link MenuItem} object created
	 */
	public MenuItem addItem(String text, MenuBar popup) {
		return this.addItem(SafeHtmlUtils.fromSafeConstant(text), popup);
	}

	/**
	 * Adds a menu item to the bar containing SafeHtml, that will fire the given command when it is selected.
	 * 
	 * @param html the item's html text
	 * @param cmd the command to be fired
	 * @return the {@link MenuItem} object created
	 */
	public MenuItem addItem(SafeHtml html, ScheduledCommand cmd) {
		return this.addItem(html.asString(), true, cmd);
	}

	/**
	 * Adds a menu item to the bar, that will fire the given command when it is selected.
	 * 
	 * @param text the item's text
	 * @param cmd the command to be fired
	 * @return the {@link MenuItem} object created
	 */
	public MenuItem addItem(String text, ScheduledCommand cmd) {
		return this.addItem(SafeHtmlUtils.fromSafeConstant(text), cmd);
	}

	/**
	 * Adds a menu item to the bar, that will fire the given command when it is selected.
	 * 
	 * @param text the item's text
	 * @param asHTML <code>true</code> to treat the specified text as html
	 * @param cmd the command to be fired
	 * @return the {@link MenuItem} object created
	 */
	public MenuItem addItem(String text, boolean asHTML, ScheduledCommand cmd) {
		final Element parentElement = Document.get().createDivElement();
		final Element element = Document.get().createDivElement();
		element.setInnerHTML(text);
		parentElement.appendChild(element);
		return super.addItem(parentElement.getInnerHTML(), asHTML, cmd);
	}

	/**
	 * Adds a menu item to the bar, that will open the specified menu when it is selected.
	 * 
	 * @param text the item's text
	 * @param asHTML <code>true</code> to treat the specified text as html
	 * @param popup the menu to be cascaded from it
	 * @return the {@link MenuItem} object created
	 */
	public MenuItem addItem(String text, boolean asHTML, MenuBar popup) {
		final Element parentElement = Document.get().createDivElement();
		final Element element = Document.get().createDivElement();
		element.setInnerHTML(text);
		parentElement.appendChild(element);
		final MenuItem addedItem = super.addItem(parentElement.getInnerHTML(), asHTML, popup);
		if (null != popup && null != addedItem) {
			popup.addCloseHandler(new CloseHandler<PopupPanel>() {

				@Override
				public void onClose(final CloseEvent<PopupPanel> event) {
					addedItem.removeStyleDependentName("selected");
				}
			});
		}
		return addedItem;
	}

}
