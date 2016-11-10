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

package com.ephesoft.gxt.foldermanager.client.presenter;

import java.util.Map;

import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.foldermanager.client.controller.FolderManagementController;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementConstants;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementMessages;
import com.ephesoft.gxt.foldermanager.client.view.FolderManagementView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FolderManagementPresenter extends BasePresenter<FolderManagementController, FolderManagementView> {

	public FolderManagementPresenter(final FolderManagementController controller, final FolderManagementView view) {
		super(controller, view);
	}

	public void init() {
		controller.getRpcService().getParentFolder(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(FolderManagementMessages.UNABLE_TO_FETCH_BASE_FOLDER_PATH),
						DialogIcon.ERROR);
			}

			@Override
			public void onSuccess(final String parentFolderPath) {
				controller.getRpcService().getBatchClassNames(new AsyncCallback<Map<String, String>>() {

					@Override
					public void onFailure(Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						DialogUtil.showMessageDialog(
								LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
								LocaleDictionary
										.getMessageValue(FolderManagementMessages.UNABLE_TO_FETCH_BATCH_CLASSES_BY_ROLES_ASSIGNED_TO_USER),
								DialogIcon.ERROR);
					}

					@Override
					public void onSuccess(final Map<String, String> batchClassesMap) {
						controller.getRpcService().getBaseHttpURL(new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(FolderManagementMessages.UNABLE_TO_FETCH_BASE_HTTP_URL),
										DialogIcon.ERROR);
							}

							@Override
							public void onSuccess(final String baseFolderUrl) {
								ScreenMaskUtility.unmaskScreen();
								view.setInitialFolderManagementView(controller, parentFolderPath, batchClassesMap, baseFolderUrl);
							}

						});
					}
				});
			}
		});
	}

	@Override
	public void bind() {
		init();
	}

	@Override
	public void injectEvents(EventBus eventBus) {

	}

}
