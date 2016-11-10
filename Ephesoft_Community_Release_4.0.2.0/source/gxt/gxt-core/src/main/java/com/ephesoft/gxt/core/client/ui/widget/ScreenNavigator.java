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

import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class ScreenNavigator extends VerticalLayoutContainer {

	private String baseURL;

	public ScreenNavigator() {
		this.addStyleName("screenNavigatorView");
		this.addStyleName("screenNavigatorDefaultPosition");
		this.addMouseOverHandler();
		this.addMouseOutHandler();
		this.getElement().getStyle().setPosition(Position.ABSOLUTE);
	}

	private void addMouseOverHandler() {
		this.addDomHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(final MouseOverEvent event) {
				addStyleName("screenNavigatorCompleteView");
				removeStyleName("screenNavigatorDefaultPosition");
			}

		}, MouseOverEvent.getType());
	}

	public void addHeader(final String header) {
		if (!StringUtil.isNullOrEmpty(header)) {
			Label headerLabel = new Label(header);
			headerLabel.addStyleName("navigationView");
			headerLabel.addStyleName("navigationTitle");
			headerLabel.addStyleName("header");
			this.add(headerLabel);
		}
	}

	public void addUserLabel(final String label) {
		if (!StringUtil.isNullOrEmpty(label)) {
			UserInfoView userInfoView = new UserInfoView(label, "userLabel");
			this.add(userInfoView);
		}
	}

	public void addURL(final String url, final String title, final String styleName, final boolean isRelative) {
		final String urlToOpen = isRelative ? StringUtil.concatenate(baseURL, CoreCommonConstant.URL_SEPARATOR, url) : url;
		NavigationView navigationView = new NavigationView(urlToOpen, title, styleName);
		this.add(navigationView);
		navigationView.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				if (!StringUtil.isNullOrEmpty(urlToOpen)) {
					Window.Location.assign(urlToOpen);
				}
			}
		}, ClickEvent.getType());
	}

	public void addLogoutURL(final ScheduledCommand commandToExecute, final String title, final String styleName,
			final boolean isRelativePath) {
		if (commandToExecute != null) {
			final NavigationView navigationView = new NavigationView(null, title, styleName);
			navigationView.addStyleName("logoutView");
			this.add(navigationView);
			navigationView.addDomHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					commandToExecute.execute();
				}
			}, ClickEvent.getType());
		}
	}

	public void setBaseURL(final String baseURL) {
		this.baseURL = baseURL;
	}

	private void addMouseOutHandler() {
		this.addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(final MouseOutEvent event) {
				removeStyleName("screenNavigatorCompleteView");
				addStyleName("screenNavigatorDefaultPosition");
			}
		}, MouseOutEvent.getType());
	}

	private static final class NavigationView extends HorizontalLayoutContainer {

		public NavigationView(final String absoluteURL, final String title, final String styleName) {
			Label icon = new Label();
			icon.addStyleName(styleName);
			this.add(icon);
			final Widget titleLabel;
			if (null != absoluteURL) {
				titleLabel = new Anchor(title);
				((Anchor) titleLabel).setHref(absoluteURL);
			} else {
				titleLabel = new Label(title);
			}
			this.addStyleName("navigationView");
			titleLabel.addStyleName("navigationTitle");
			this.add(titleLabel);
		}

	}

	private static final class UserInfoView extends HorizontalLayoutContainer {

		public UserInfoView(final String title, final String styleName) {
			Label icon = new Label();
			icon.addStyleName(styleName);
			this.add(icon);
			final Label titleLabel = new Label(title);
			this.addStyleName("navigationView");
			titleLabel.addStyleName("navigationTitle");
			this.add(titleLabel);
		}

	}
}
