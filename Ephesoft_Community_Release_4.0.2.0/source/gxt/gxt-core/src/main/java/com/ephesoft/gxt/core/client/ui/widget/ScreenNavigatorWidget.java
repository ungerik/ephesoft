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
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

public class ScreenNavigatorWidget extends HorizontalPanel{
	private VerticalPanel innerVerticalPanel;
	private HorizontalPanel subHorizontalPanel ;
	private String height;
	private VerticalPanel toolBarMenuVerticalPanel;
	private VerticalPanel pageMenuVerticalPanel;
	private VerticalPanel userMenuVerticalPanel;
	private VerticalPanel bottomMenuVerticalPanel;
	private VerticalPanel topContainerPanel;
	private VerticalPanel bottomContainerPanel;
	private MenuBar previosMenuBar;
	private ScheduledCommand scheduledCommand;
	private Label userNameLabel;
	
	public ScreenNavigatorWidget() {
		initialize();
		height = "100%";
		this.setStyleName("promoBox");
		this.setHeight(height);
		pageMenuVerticalPanel.addStyleName("sidebarNavMenuDivider");
		userMenuVerticalPanel.addStyleName("sidebarNavMenuDivider");
		toolBarMenuVerticalPanel.addStyleName("sidebarNavMenuDivider");
		this.addStyleName("panelWidth");
		setUserPanelContents();
		logOutPanelContents();	
		innerVerticalPanel.setStyleName("panelStyle");
	}
	
	private void process(){
		innerVerticalPanel.add(topContainerPanel);
		innerVerticalPanel.add(bottomContainerPanel);
		subHorizontalPanel.add(innerVerticalPanel);
		subHorizontalPanel.addDomHandler(outerMouseOverHandler, MouseOverEvent.getType());
		subHorizontalPanel.addDomHandler(outerMouseOutHandler, MouseOutEvent.getType());
		this.add(subHorizontalPanel);
	}
	
	MouseOutHandler outerMouseOutHandler = new MouseOutHandler() {
		public void onMouseOut(MouseOutEvent event) {
			subHorizontalPanel.removeStyleName("setIndex");
			changeSecondWidgetVisibilty(Boolean.FALSE);
		}
	};
	
	MouseOverHandler outerMouseOverHandler = new MouseOverHandler() {
		public void onMouseOver(MouseOverEvent event) {
			subHorizontalPanel.addStyleName("setIndex");
			changeSecondWidgetVisibilty(Boolean.TRUE);
		}
	};

	private void changeSecondWidgetVisibilty(boolean shouldVisible)
	{
		for(int index = 0 ; index < innerVerticalPanel.getWidgetCount();index++)
		{
			VerticalPanel verticalPanel = (VerticalPanel) innerVerticalPanel.getWidget(index);
			for(int count = 0 ; count < verticalPanel.getWidgetCount(); count++)
			{
				VerticalPanel verticalPanel1 = (VerticalPanel) verticalPanel.getWidget(count);
				for(int widgetCount = 0; widgetCount < verticalPanel1.getWidgetCount() ; widgetCount++)
				{
					HorizontalPanel horizontalPanel = (HorizontalPanel) verticalPanel1.getWidget(widgetCount);
					Widget widget = horizontalPanel.getWidget(1);
					if(widget instanceof MenuBarWidget)
					{
						MenuBarWidget menuBar = (MenuBarWidget) horizontalPanel.getWidget(1);
						if(shouldVisible)
						{
							widget.setVisible(shouldVisible);
						}
						else
						{
							if(!menuBar.isSelectedMenu())
							{
								widget.setVisible(shouldVisible);
							}
							menuBar.setSelectedMenu(false);
						}
					}
					if(widget instanceof Label)
					{
						widget.setVisible(shouldVisible);
					}
				}
			}
		}
	}
	
	/**
	 * @return the height
	 */
	public String getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		 getElement().getStyle().setProperty("height", height);
	}
	
	public void addPageMenuWidget(Widget firstWidget, String  text, String url)
	{
		Label label = new Label(text);
		HorizontalPanel widgetHorizontalPanel = getPageMenuInnerHorizontalPanel(firstWidget, label, url);
		pageMenuVerticalPanel.add(widgetHorizontalPanel);
		topContainerPanel.add(pageMenuVerticalPanel);
		innerVerticalPanel.setCellVerticalAlignment(pageMenuVerticalPanel, VerticalPanel.ALIGN_TOP);
        process();		
	}
	
	
	public void addConfigMenu(Widget firstWidget, Widget secondWidget)
	{
		HorizontalPanel widgetHorizontalPanel = getInnerHorizontalPanel(firstWidget, secondWidget);
		toolBarMenuVerticalPanel.add(widgetHorizontalPanel);
		topContainerPanel.add(toolBarMenuVerticalPanel);
		innerVerticalPanel.setCellVerticalAlignment(toolBarMenuVerticalPanel, VerticalPanel.ALIGN_TOP);
        process();		
	}

	public void addUserMenu(Widget firstWidget, Widget secondWidget)
	{
		HorizontalPanel widgetHorizontalPanel = getPageMenuInnerHorizontalPanel(firstWidget, secondWidget,CoreCommonConstant.EMPTY_STRING);
		userMenuVerticalPanel.add(widgetHorizontalPanel);
		topContainerPanel.add(userMenuVerticalPanel);
		innerVerticalPanel.setCellVerticalAlignment(userMenuVerticalPanel, VerticalPanel.ALIGN_TOP);
        process();		
	}
	
	public void addLogOutMenu(Widget firstWidget, Widget secondWidget)
	{
		HorizontalPanel widgetHorizontalPanel = getPageMenuInnerHorizontalPanel(firstWidget, secondWidget,CoreCommonConstant.EMPTY_STRING);
		bottomMenuVerticalPanel.add(widgetHorizontalPanel);
		bottomContainerPanel.add(bottomMenuVerticalPanel);
		innerVerticalPanel.getWidget(1).setStyleName("logOutStyle");
        process();		
	}
	
	
	private HorizontalPanel getInnerHorizontalPanel(Widget firstWidget, final Widget secondWidget)
	{
		final HorizontalPanel widgetHorizontalPanel = new HorizontalPanel();
		widgetHorizontalPanel.add(firstWidget);
		widgetHorizontalPanel.getElement().setAttribute("cellPadding", "2");
		widgetHorizontalPanel.setSpacing(1);
		widgetHorizontalPanel.setWidth("100px");
		secondWidget.setVisible(true);
		secondWidget.addStyleName("panelLabelStyle");
		final MenuBar menuBar = (MenuBar) secondWidget;
		final MenuBarWidget menuBarWidget = new MenuBarWidget(true);
		menuBarWidget.addItem(new MenuItem(menuBar.getTitle(), menuBar));
		widgetHorizontalPanel.add(menuBarWidget);
		menuBarWidget.setVisible(false);
		widgetHorizontalPanel.addDomHandler(new MouseOverHandler() {
			
			public void onMouseOver(MouseOverEvent event) {
				widgetHorizontalPanel.addStyleName("horizontalPanelMouseHover");
				menuBarWidget.setAutoOpen(true);
				menuBarWidget.setSelectedMenu(false);
				if(null != previosMenuBar)
				{
					previosMenuBar.closeAllChildren(false);
				}
			}
		}, MouseOverEvent.getType());
		
		widgetHorizontalPanel.addDomHandler(new MouseOutHandler() {
			
			public void onMouseOut(MouseOutEvent event) {
				widgetHorizontalPanel.removeStyleName("horizontalPanelMouseHover");
				menuBarWidget.setSelectedMenu(true);
				previosMenuBar = menuBarWidget;
			}
		}, MouseOutEvent.getType());
		return widgetHorizontalPanel;
	}
	
	ClickHandler logOutClickHandler = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			if(null != scheduledCommand)
			{
				Scheduler.get().scheduleDeferred(scheduledCommand);
			}
		}
	};
	
	private void initialize()
	{
		subHorizontalPanel = new HorizontalPanel();
		innerVerticalPanel = new VerticalPanel();
		toolBarMenuVerticalPanel = new VerticalPanel();
		pageMenuVerticalPanel = new VerticalPanel();
		userMenuVerticalPanel = new VerticalPanel();
		bottomMenuVerticalPanel = new VerticalPanel();
		topContainerPanel = new VerticalPanel();
		bottomContainerPanel = new VerticalPanel();
		innerVerticalPanel.addStyleName("panelWidth");
		topContainerPanel.addStyleName("panelWidth");
		bottomContainerPanel.addStyleName("panelWidth");
		subHorizontalPanel.addStyleName("panelWidth");
		bottomMenuVerticalPanel.addStyleName("panelWidth");
		subHorizontalPanel.addStyleName("panelHeight");
		innerVerticalPanel.addStyleName("panelHeight");
		innerVerticalPanel.getElement().setId("innerVerticalPanel");
		bottomMenuVerticalPanel.getElement().setId("bottomMenuVerticalPanel");
		bottomContainerPanel.getElement().setId("bottomContainerPanel");
	}
	
	private void logOutPanelContents()
	{
		Label logOutLabel = new Label(LocaleConstants.LOG_OUT);
		Label logOutImageLabel = new Label();
		logOutImageLabel.addStyleName("logOutImage");
		addLogOutMenu(logOutImageLabel,logOutLabel);
		logOutImageLabel.addClickHandler(logOutClickHandler);
		logOutLabel.addClickHandler(logOutClickHandler);
	}
	 
	private HorizontalPanel getPageMenuInnerHorizontalPanel(Widget firstWidget, final Widget secondWidget,final String url)
	{
		final HorizontalPanel widgetHorizontalPanel = new HorizontalPanel();
		widgetHorizontalPanel.add(firstWidget);
		widgetHorizontalPanel.add(secondWidget);
		widgetHorizontalPanel.getElement().setAttribute("cellPadding", "2");
		widgetHorizontalPanel.setSpacing(1);
		widgetHorizontalPanel.addStyleName("panelWidth");
		secondWidget.setVisible(false);
		secondWidget.setWidth("100px");
		secondWidget.addStyleName("panelLabelStyle");
		if(!url.equalsIgnoreCase(CoreCommonConstant.EMPTY_STRING))
		{
			String currentURL = Window.Location.getHref();
			String baseUrl = currentURL.substring(0, currentURL.lastIndexOf('/'));
			StringBuffer newUrl = new StringBuffer();
			newUrl.append(baseUrl).append(url);
			if(currentURL.equalsIgnoreCase(newUrl.toString())){
				widgetHorizontalPanel.addStyleName("setSelectedPageStyle");
			}
		}
		widgetHorizontalPanel.addDomHandler(new MouseOverHandler() {
			
			public void onMouseOver(MouseOverEvent event) {
				widgetHorizontalPanel.addStyleName("horizontalPanelMouseHover");
				if(null != previosMenuBar)
				{
					previosMenuBar.closeAllChildren(false);
				}
			}
		}, MouseOverEvent.getType());
		
		widgetHorizontalPanel.addDomHandler(new MouseOutHandler() {
			
			public void onMouseOut(MouseOutEvent event) {
				widgetHorizontalPanel.removeStyleName("horizontalPanelMouseHover");
			}
		}, MouseOutEvent.getType());
		widgetHorizontalPanel.addDomHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				if(!url.equalsIgnoreCase(CoreCommonConstant.EMPTY_STRING))
				{
					String href = Window.Location.getHref();
					String baseUrl = href.substring(0, href.lastIndexOf('/'));
					StringBuffer newUrl = new StringBuffer();
					newUrl.append(baseUrl).append(url);
					Window.Location.assign(newUrl.toString());
				}
			} 
		}, ClickEvent.getType());
		return widgetHorizontalPanel;
	}

	/**
	 * @param scheduledCommand the scheduledCommand to set
	 */
	public void setScheduledCommand(ScheduledCommand scheduledCommand) {
		this.scheduledCommand = scheduledCommand;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		userNameLabel.setText(userName);
	}
	
	private void setUserPanelContents()
	{
		Label userLabel = new Label();
		userLabel.addStyleName("userImage");
		userNameLabel = new Label();
		addUserMenu(userLabel, userNameLabel);
	}
}
