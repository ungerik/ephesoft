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

package com.ephesoft.gxt.systemconfig.client;

import com.ephesoft.gxt.core.client.DCMAEntryPoint;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.i18n.LocaleInfo;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.HTMLDomUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class SystemConfigEntryPoint extends DCMAEntryPoint<SystemConfigServiceAsync> {

	@Override
	public void onLoad() {
		checkForSuperAdmin();
	}

	/**
	 * Checks whether the user has super admin rights or not. System Config module is visible only to the super admin user.
	 */
	private void checkForSuperAdmin() {
		rpcService.isSuperAdmin(new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(final Boolean result) {
				if (result) {
					loadSystemConfigModule();
				} else {
					Window.Location.replace(LocaleCommonConstants.ACCESS_HTML_FILE_URL);
				}
			}

			@Override
			public void onFailure(final Throwable throwable) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(CoreCommonConstants.ERROR_FETCHING_DATA), DialogIcon.ERROR);
			}

		});

		// JIRA_ISSUE-8222
		HTMLDomUtil.setContentByTagName(CoreCommonConstants.HTML_TITLE_TAG_NAME,
				LocaleDictionary.getConstantValue(SystemConfigConstants.SYSTEM_CONFIG_TITLE));
	}

	/**
	 * Loads the System Config module.
	 */
	private void loadSystemConfigModule() {
		final SystemConfigController controller = new SystemConfigController(eventBus, rpcService);
		RootPanel.get().add(controller.createView());
	}

	@Override
	public SystemConfigServiceAsync createRpcService() {
		return GWT.create(SystemConfigService.class);
	}

	@Override
	public String getHomePage() {
		return null;
	}

	@Override
	public LocaleInfo createLocaleInfo(String locale) {
		return new LocaleInfo(locale, "systemConfigConstants", "systemConfigMessages");
	}

	@Override
	public com.ephesoft.gxt.core.client.DCMAEntryPoint.ScreenType getScreenType() {
		return ScreenType.ADMINISTRATOR;
	}

}
