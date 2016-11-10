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

package com.ephesoft.gxt.core.client;

import java.util.Set;

import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.i18n.LocaleInfo;
import com.ephesoft.gxt.core.client.ui.widget.ScreenNavigator;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WindowUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.InitializeMetaData;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class DCMAEntryPoint<R extends DCMARemoteServiceAsync> implements EntryPoint {

	interface GlobalResources extends ClientBundle {

		@NotStrict
		@Source("global.css")
		CssResource css();
	}

	protected R rpcService;

	protected EventBus eventBus;

	private ScreenNavigator screenNavigator;

	public static String BASE_URL;

	@Override
	public final void onModuleLoad() {
		eventBus = new SimpleEventBus();
		rpcService = this.createRpcService();
		String urlPath = Window.Location.getPath();
		int urlSeparatorIndex = urlPath.lastIndexOf(CoreCommonConstant.URL_SEPARATOR);
		if (urlSeparatorIndex != -1) {
			BASE_URL = urlPath.substring(0, urlSeparatorIndex);
		}
		screenNavigator = new ScreenNavigator();
		if (getScreenType() != null) {
			RootPanel.get().add(screenNavigator);
			screenNavigator.setBaseURL(BASE_URL);
		}
		// initialize("");
		preprocess();
	}

	private void addURLs() {
		screenNavigator.addUserLabel(StringUtil.concatenate("Hi ", EphesoftUIContext.getUserName()));
		screenNavigator.addHeader("Administrator");
		screenNavigator.addURL(CoreCommonConstant.BATCH_CLASS_MANAGEMENT_RESOURCE_NAME, "Batch Class Management",
				"batchClassManagement", true);
		screenNavigator.addURL(CoreCommonConstant.BATCH_INSTANCE_MANAGEMENT_RESOURCE_NAME, "Batch Instance Management",
				"batchInstanceManagement", true);
		screenNavigator.addURL(CoreCommonConstant.FOLDER_MANAGER_RESOURCE_NAME, "Folder Management", "folderManagement", true);

		if (EphesoftUIContext.isSuperAdmin()) {
			screenNavigator.addURL(CoreCommonConstant.SYSTEM_CONFIG_RESOURCE_NAME, "System Config", "systemConfig", true);
		}

		screenNavigator.addHeader("Operator");
		screenNavigator.addURL(CoreCommonConstant.BATCH_LIST_RESOURCE_NAME, "Batch List", "batchList", true);
		screenNavigator.addURL(CoreCommonConstant.REVIEW_VALIDATE_RESOURCE_NAME, "Review Validate", "reviewValidate", true);
		screenNavigator.addURL(CoreCommonConstant.UPLOAD_BATCH_RESOURCE_NAME, "Upload Batch", "uploadBatch", true);
		ScheduledCommand command = new ScheduledCommand() {

			@Override
			public void execute() {
				rpcService.logout(Window.Location.getPath(), new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						Window.Location.reload();
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.Location.reload();
					}
				});
			}
		};
		screenNavigator.addLogoutURL(command, "Logout", "signout", true);
	}

	private void initialize(final String locale) {
		LocaleDictionary.create(createLocaleInfo(locale));
		this.rpcService.initRemoteService(new AsyncCallback<Void>() {

			@Override
			public void onFailure(final Throwable throwable) {

				// Common service added for authorization and authentication
				rpcService.getAuthenticationType(new AsyncCallback<Integer>() {

					@Override
					public void onFailure(Throwable throwable) {
						// Do nothing
					}

					@Override
					public void onSuccess(final Integer authenticationType) {
						if (null == authenticationType || authenticationType.intValue() == 0) {
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(LocaleCommonConstants.ERROR_TITLE),
									LocaleDictionary.getConstantValue(throwable.getMessage().replace('.', '_')), DialogIcon.ERROR);
						} else {
							WindowUtil.redirectToResourse(CoreCommonConstants.LICENSE_ERROR_PAGE_PATH);
						}
					}
				});
			}

			@Override
			public void onSuccess(final Void arg0) {
		// defineBridgeMethod();
		rpcService.initializeMetaData(new AsyncCallback<InitializeMetaData>() {

			@Override
			public void onFailure(Throwable caught) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(LocaleCommonConstants.ERROR_TITLE),
						"Error occured while retrieving  metadata.", DialogIcon.ERROR);
			}

			@Override
			public void onSuccess(InitializeMetaData result) {
				EphesoftUIContext.setUnix(result.isOperatingSystemLinux());
				EphesoftUIContext.setWindows(result.isOperatingSystemWindows());
				EphesoftUIContext.setSuperAdmin(result.isSuperAdmin());
				EphesoftUIContext.setUserName(result.getCurrentUser());
				EphesoftUIContext.setSuperAdminGroup(result.getSuperAdminGroups());
				EphesoftUIContext.setAllGroups(result.getAllGroups());
				EphesoftUIContext.footerText = result.getFooterLabel();
				EphesoftUIContext.footerLink = result.getEphesoftURL();
				EphesoftUIContext.documentDisplayProperty = result.getDocumentDisplayProperty();
				addURLs();
				onLoad();
			}

		});
	}
		});
	}

	private void preprocess() {
		rpcService.getLocale(new AsyncCallback<String>() {

			@Override
			public void onFailure(final Throwable throwable) {
				initialize("");
			}

			@Override
			public void onSuccess(final String locale) {
				initialize(locale);
			}
		});
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public R getRpcService() {
		return rpcService;
	}

	public void onCloseWindow() {

	}

	public native void defineBridgeMethod() /*-{
											var _this = this;
											$wnd.onCloseWindow = function() {
											return _this.@com.ephesoft.gxt.core.client.DCMAEntryPoint::onCloseWindow()();
											}
											}-*/;

	public abstract void onLoad();

	public abstract ScreenType getScreenType();

	public abstract R createRpcService();

	public abstract String getHomePage();

	public abstract LocaleInfo createLocaleInfo(String locale);

	/**
	 * To change the theme color according to the user's selected theme.
	 * 
	 * @param url String
	 */
	public static native void loadJs(String url) /*-{  
													return $wnd.loadJs(url);
													}-*/;

	public static enum ScreenType {
		ADMINISTRATOR, OPERATOR;
	}

	interface ScreenNavigationIcon extends ClientBundleWithLookup {

		ScreenNavigationIcon INSTANCE = GWT.create(ScreenNavigationIcon.class);

		@Source("BCMIcon.png")
		ImageResource batchClassManagementIcon();

		@Source("BIMIcon.png")
		ImageResource batchInstanceManagementIcon();

		@Source("SC_icon.png")
		ImageResource systemConfigIcon();

		@Source("FM_Icon.png")
		ImageResource folderManagerIcon();

		@Source("SC_icon.png")
		ImageResource batchListIcon();

		@Source("ReviewValidate_icon.png")
		ImageResource reviewValidateIcon();

		@Source("Scanner.png")
		ImageResource webScannerIcon();

		@Source("FM_Icon.png")
		ImageResource uploadBatchIcon();

		@Source("sign_out.png")
		ImageResource signoutIcon();
	}

	public static class EphesoftUIContext {

		/** True if OS is unix. */
		private static boolean isUnix;

		/** True if OS is windows. */
		private static boolean isWindows;

		private static boolean isSuperAdmin;

		/** Logged in user name. */
		private static String userName;

		/** The super admin group. */
		private static Set<String> superAdminGroup;

		/** The all groups. */
		private static Set<String> allGroups;

		private static String footerText;

		private static String footerLink;
		private static int documentDisplayProperty;

		public static String getFooterLink() {
			return footerLink;
		}

		/**
		 * Checks if OS is unix.
		 * 
		 * @return true, if OS is unix
		 */
		public static boolean isUnix() {
			return isUnix;
		}
		
		/**
		 * @return the documentDisplayProperty
		 */
		public static int getDocumentDisplayProperty() {
			return documentDisplayProperty;
		}

		/**
		 * Sets the unix boolean.
		 * 
		 * @param isUnix the boolean
		 */
		private static void setUnix(boolean isUnix) {
			EphesoftUIContext.isUnix = isUnix;
		}

		/**
		 * Checks if OS is windows.
		 * 
		 * @return true, if OS is windows
		 */
		public static boolean isWindows() {
			return isWindows;
		}

		/**
		 * Sets the windows boolean.
		 * 
		 * @param isWindows the boolean
		 */
		private static void setWindows(boolean isWindows) {
			EphesoftUIContext.isWindows = isWindows;
		}

		/**
		 * @return the isSuperAdmin
		 */
		public static boolean isSuperAdmin() {
			return isSuperAdmin;
		}

		/**
		 * @param isSuperAdmin the isSuperAdmin to set
		 */
		private static void setSuperAdmin(boolean isSuperAdmin) {
			EphesoftUIContext.isSuperAdmin = isSuperAdmin;
		}

		/**
		 * Gets the user name.
		 * 
		 * @return the user name
		 */
		public static String getUserName() {
			return userName;
		}

		/**
		 * Sets the user name.
		 * 
		 * @param userName the new user name
		 */
		public static void setUserName(String userName) {
			EphesoftUIContext.userName = userName;
		}

		/**
		 * Gets the super admin group.
		 * 
		 * @return the super admin group
		 */
		public static Set<String> getSuperAdminGroup() {
			return superAdminGroup;
		}

		/**
		 * Sets the super admin group.
		 * 
		 * @param superAdminGroup the new super admin group
		 */
		public static void setSuperAdminGroup(Set<String> superAdminGroup) {
			EphesoftUIContext.superAdminGroup = superAdminGroup;
		}

		public static String getFooterText() {
			return footerText;
		}

		/**
		 * Gets the all groups.
		 *
		 * @return the all groups
		 */
		public static Set<String> getAllGroups() {
			return allGroups;
		}

		/**
		 * Sets the all groups.
		 *
		 * @param allGroups the new all groups
		 */
		public static void setAllGroups(Set<String> allGroups) {
			EphesoftUIContext.allGroups = allGroups;
		}

	}

}
