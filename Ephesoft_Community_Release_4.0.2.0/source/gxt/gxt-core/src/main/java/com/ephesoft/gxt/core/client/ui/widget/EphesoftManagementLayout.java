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

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.Component;

public class EphesoftManagementLayout extends Composite {

	interface Binder extends
			UiBinder<Component, EphesoftManagementLayout> {
	}

	@UiField
	protected HorizontalLayoutContainer horizontalLayoutContainer;

	@UiField
	protected ScreenNavigatorWidget screenNavigatorWidget;

	private static final Binder uiBinder = GWT.create(Binder.class);

	public EphesoftManagementLayout() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	private void addPageMenu(Widget firstWidget, String text, String url) {
		screenNavigatorWidget.addPageMenuWidget(firstWidget, text, url);
	}

	private void addConfigMenu(Widget firstWidget, Widget secondWidget) {
		screenNavigatorWidget.addConfigMenu(firstWidget, secondWidget);
	}
	
	private void addView(Widget widget)
	{
		horizontalLayoutContainer.add(widget);
	}
	
	private void removeView(Widget widget)
	{
		horizontalLayoutContainer.remove(widget);
	}
	
	private void setScheduledCommand(ScheduledCommand scheduledCommand)
	{
		screenNavigatorWidget.setScheduledCommand(scheduledCommand);
	}

	private void setUserName(String userName)
	{
		screenNavigatorWidget.setUserName(userName);
	}
	public static final class EphesoftLayoutManager {
		private static EphesoftManagementLayout ephesoftManagementLayout;

		private static Widget addedView = null;

		private EphesoftLayoutManager() {

		}

		public static EphesoftManagementLayout getLayoutManager() {
			if (null == ephesoftManagementLayout) {
				ephesoftManagementLayout = new EphesoftManagementLayout();
			}
			return ephesoftManagementLayout;
		}

		public static void addURLForNavigation(Widget firstWidget, String text,
				String url) {
			ephesoftManagementLayout.addPageMenu(firstWidget, text, url);
		}

		public static void setViewToDisplay(Widget widgetToBeAdded) {
			
			if (null != addedView) {
				ephesoftManagementLayout.removeView(addedView);
			}
			if(null != widgetToBeAdded)
			{
				ephesoftManagementLayout.addView(widgetToBeAdded);
				addedView = widgetToBeAdded;
			}
		}

		public static void addConfigMenu(Widget firstWidget, Widget secondWidget) {
			ephesoftManagementLayout.addConfigMenu(firstWidget, secondWidget);
		}
		
		public static void setScheduledCommand(ScheduledCommand scheduledCommand)
		{
			ephesoftManagementLayout.setScheduledCommand(scheduledCommand);
		}
		
		public static void setUserName(String userName)
		{
			ephesoftManagementLayout.setUserName(userName);
		}
	}
}
