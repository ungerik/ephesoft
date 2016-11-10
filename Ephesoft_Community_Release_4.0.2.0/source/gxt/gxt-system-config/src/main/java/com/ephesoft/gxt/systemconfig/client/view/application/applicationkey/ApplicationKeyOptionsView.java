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

package com.ephesoft.gxt.systemconfig.client.view.application.applicationkey;

import com.ephesoft.gxt.core.client.constant.CssConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.presenter.application.applicationkey.ApplicationKeyOptionsPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationKeyOptionsView extends SystemConfigInlineView<ApplicationKeyOptionsPresenter> {

	interface Binder extends UiBinder<Widget, ApplicationKeyOptionsView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected VerticalPanel applicationKeyMenuBarPanel;

	@UiField
	protected CustomMenuBar applicationKeyMenuBar;

	protected CustomMenuItem generateKeyMenuItem;

	public ApplicationKeyOptionsView() {
		super();
		initWidget(binder.createAndBindUi(this));
		intializeMenuItems();
		applicationKeyMenuBar.addItem(generateKeyMenuItem);
		intializeSelectionHandlers();
		applicationKeyMenuBarPanel.addStyleName(CssConstants.MENUBAR_CSS);
		setWidgetIds();
		applicationKeyMenuBar.setFocusOnHoverEnabled(false);
		applicationKeyMenuBarPanel.addStyleName("menuBarPanelApplicationKey");
		applicationKeyMenuBar.addStyleName("menuBarApplicationKey");
	}

	private void intializeMenuItems() {
		generateKeyMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(SystemConfigConstants.GENERATION_KEY);
			}
		});

	}

	private void intializeSelectionHandlers() {
		generateKeyMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.generateApplicationLevelKey();
			}
		});

	}

	private void setWidgetIds() {
		WidgetUtil.setID(applicationKeyMenuBarPanel, "AKOV_menuBarVerticalPanel");
		WidgetUtil.setID(applicationKeyMenuBar, "AKOV_menuBar");
		WidgetUtil.setID(generateKeyMenuItem, "AKOV_generateKey");
	}

	public void disableGenerateKey(boolean disable) {
		this.generateKeyMenuItem.setEnabled(disable);
	}
}
