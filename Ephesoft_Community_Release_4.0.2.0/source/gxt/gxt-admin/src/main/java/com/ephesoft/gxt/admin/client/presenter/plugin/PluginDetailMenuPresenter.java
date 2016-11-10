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

package com.ephesoft.gxt.admin.client.presenter.plugin;

import java.util.Map;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.i18n.PluginNameConstants;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.plugin.PluginDetailMenuView;
import com.ephesoft.gxt.core.client.EphesoftAsyncCallback;
import com.ephesoft.gxt.core.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ExternalDialogApp;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginConfigDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginDTO;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

// TODO: Auto-generated Javadoc
/**
 * The Class PluginDetailMenuPresenter.
 */
public class PluginDetailMenuPresenter extends BatchClassInlinePresenter<PluginDetailMenuView> {

	/**
	 * Instantiates a new plugin detail menu presenter.
	 *
	 * @param controller the controller
	 * @param view the view
	 */
	public PluginDetailMenuPresenter(BatchClassManagementController controller, PluginDetailMenuView view) {
		super(controller, view);
	}

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<PluginDetailMenuPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.event.BindHandler#bind()
	 */
	@Override
	public void bind() {

	}

	/**
	 * Generate token.
	 */
	public void generateToken() {
		BatchClassPluginDTO batchClassPluginDTO = controller.getSelectedBatchClassPlugin();
		if (null != batchClassPluginDTO) {
			if (batchClassPluginDTO.getPlugin().getPluginName().equals(PluginNameConstants.CMIS_EXPORT_PLUGIN)) {
				controller.getRpcService().getCmisConfiguration(batchClassPluginDTO.getBatchClassPluginConfigs(),
						new EphesoftAsyncCallback<Map<String, String>>() {

							@Override
							public void customFailure(final Throwable throwable) {
							}

							@Override
							public void onSuccess(final Map<String, String> tokenMap) {
								if (tokenMap != null && !tokenMap.isEmpty()) {
									BatchClassPluginDTO batchClassPluginDTO = controller.getSelectedBatchClassPlugin();
									for (BatchClassPluginConfigDTO batchClassPluginConfigDTO : batchClassPluginDTO
											.getBatchClassPluginConfigs()) {
										if (tokenMap.containsKey(batchClassPluginConfigDTO.getName())) {
											batchClassPluginConfigDTO.setValue(tokenMap.get(batchClassPluginConfigDTO.getName()));
										}
									}
									controller.setSelectedBatchClassPlugin(batchClassPluginDTO);
									bind();
								}
							}
						});

				controller.getRpcService().getAuthenticationURL(batchClassPluginDTO.getBatchClassPluginConfigs(),
						new EphesoftAsyncCallback<String>() {

							@Override
							public void customFailure(final Throwable throwable) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										throwable.getMessage(), DialogIcon.ERROR);
							}

							@Override
							public void onSuccess(final String authenticationURL) {
								if (null != authenticationURL && !authenticationURL.isEmpty()) {
									openExternalAppDialog(authenticationURL);
								}
							}
						});
			}
		}
	}

	/**
	 * Open external app dialog.
	 *
	 * @param authenticationURL the authentication url
	 */
	private void openExternalAppDialog(String authenticationURL) {
		ExternalDialogApp externalAppDialog = new ExternalDialogApp(authenticationURL);
		externalAppDialog.show();
	}

}
