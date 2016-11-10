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

package com.ephesoft.gxt.systemconfig.client.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.core.client.Controller;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.LicenseDetailsDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.dto.RegexPatternDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.systemconfig.client.SystemConfigServiceAsync;
import com.ephesoft.gxt.systemconfig.client.event.RegexGroupSelectionEvent;
import com.ephesoft.gxt.systemconfig.client.event.TreeCreationEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigCompositePresenter;
import com.ephesoft.gxt.systemconfig.client.presenter.application.applicationkey.ApplicationKeyPresenter;
import com.ephesoft.gxt.systemconfig.client.presenter.application.regexbuilder.RegexBuilderPresenter;
import com.ephesoft.gxt.systemconfig.client.presenter.application.regexpool.RegexPatternViewPresenter;
import com.ephesoft.gxt.systemconfig.client.presenter.application.regexpool.RegexPoolPresenter;
import com.ephesoft.gxt.systemconfig.client.presenter.connectionmanager.ConnectionsCompositePresenter;
import com.ephesoft.gxt.systemconfig.client.presenter.licensedetails.LicenseDetailsPresenter;
import com.ephesoft.gxt.systemconfig.client.presenter.navigator.SystemConfigNavigatorPresenter;
import com.ephesoft.gxt.systemconfig.client.presenter.workflowmanagement.WorkflowManagmentPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.ephesoft.gxt.systemconfig.client.view.application.applicationkey.ApplicationKeyView;
import com.ephesoft.gxt.systemconfig.client.view.application.regexbuilder.RegexBuilderView;
import com.ephesoft.gxt.systemconfig.client.view.application.regexpool.RegexPatternView;
import com.ephesoft.gxt.systemconfig.client.view.application.regexpool.RegexPoolView;
import com.ephesoft.gxt.systemconfig.client.view.connectionmanager.ConnectionsView;
import com.ephesoft.gxt.systemconfig.client.view.layout.SystemConfigLayout;
import com.ephesoft.gxt.systemconfig.client.view.licensedetails.LicenseDetailsView;
import com.ephesoft.gxt.systemconfig.client.view.navigator.SystemConfigNavigatorView;
import com.ephesoft.gxt.systemconfig.client.view.workflowmanagement.WorkflowManagmentView;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class SystemConfigController extends Controller {

	private SystemConfigLayout systemConfigLayout;
	private SystemConfigNavigatorPresenter systemConfigNavigatorPresenter;
	private WorkflowManagmentView workflowManagmentView;
	private ConnectionsView connectionsView;
	private ConnectionsCompositePresenter connectionsCompositePresenter;
	private ApplicationKeyView applicationKeyView;
	private RegexPoolView regexPoolView;
	private ApplicationKeyPresenter applicationKeyPresenter;
	private RegexPoolPresenter regexPoolPresenter;
	private WorkflowManagmentPresenter workflowManagmentPresenter;
	private List<PluginDetailsDTO> allPluginsList;
	private LicenseDetailsView licenseDetailsView;
	private LicenseDetailsPresenter licenseDetailsPresenter;
	private List<LicenseDetailsDTO> licenseDetailsDTOList;
	private List<ConnectionsDTO> allConnections;

	private RegexPatternDTO selectedRegexPatternDTO;

	private RegexGroupDTO selectedRegexGroupDTO;

	private RegexBuilderView regexBuilderView;

	private RegexBuilderPresenter regexBuilderPresenter;

	private RegexPatternView regexPatternView;

	private RegexPatternViewPresenter regexPatternViewPresenter;

	private Collection<RegexGroupDTO> regexGroupList;

	private SystemConfigCompositePresenter<?, ?> currentBindedPresenter;

	public SystemConfigController(EventBus eventBus, DCMARemoteServiceAsync rpcService) {
		super(eventBus, rpcService);
		systemConfigLayout = new SystemConfigLayout();
		setLicenseDetailsDTOList(new ArrayList<LicenseDetailsDTO>());

		SystemConfigEventBus.registerEventBus(eventBus);

		this.initializeView();
		this.initializePresenter();
		this.addNavigationHandler();
	}

	private void initializeView() {
		workflowManagmentView = new WorkflowManagmentView();
		applicationKeyView = new ApplicationKeyView();
		regexPoolView = new RegexPoolView();
		licenseDetailsView = new LicenseDetailsView();
		connectionsView = new ConnectionsView();
		regexBuilderView = new RegexBuilderView();
		regexPatternView = new RegexPatternView();
	}

	private void initializePresenter() {
		systemConfigNavigatorPresenter = new SystemConfigNavigatorPresenter(this, getNavigationView());
		workflowManagmentPresenter = new WorkflowManagmentPresenter(this, this.workflowManagmentView);
		applicationKeyPresenter = new ApplicationKeyPresenter(this, this.applicationKeyView);
		regexPoolPresenter = new RegexPoolPresenter(this, this.regexPoolView);
		licenseDetailsPresenter = new LicenseDetailsPresenter(this, this.licenseDetailsView);
		connectionsCompositePresenter = new ConnectionsCompositePresenter(this, this.connectionsView);
		regexBuilderPresenter = new RegexBuilderPresenter(this, this.regexBuilderView);

		regexPatternViewPresenter = new RegexPatternViewPresenter(this, this.regexPatternView);
		this.getEventBus().fireEvent(new TreeCreationEvent());
	}

	public void setTopPanelView(SystemConfigInlineView<?> inlineView) {
		systemConfigLayout.setTopPanelView(inlineView);
	}

	public void setCenterPanelView(SystemConfigInlineView<?> inlineView) {
		systemConfigLayout.setCenterPanelView(inlineView);
	}

	public void setBottomPanelView(SystemConfigInlineView<?> inlineView) {
		systemConfigLayout.setBottomPanelView(inlineView);
	}

	public void setBottomPanelHeader(String text) {
		systemConfigLayout.setBottomPanelHeader(text);
	}

	@Override
	public void injectEvents(EventBus eventBus) {

	}

	@Override
	public Widget createView() {
		// applicationKeyPresenter.layout(null);
		return systemConfigLayout;
	}

	/**
	 * @return the navigationView
	 */
	public SystemConfigNavigatorView getNavigationView() {
		return systemConfigLayout.getNavigationView();
	}

	@Override
	public SystemConfigServiceAsync getRpcService() {
		DCMARemoteServiceAsync remoteService = super.getRpcService();
		if (remoteService instanceof SystemConfigServiceAsync) {
			return (SystemConfigServiceAsync) remoteService;
		} else {
			throw new UnsupportedOperationException("RPC Service of System Config is not valid");
		}
	}

	@Override
	public void refresh() {

	}

	public SystemConfigNavigatorPresenter getSystemConfigNavigatorPresenter() {
		return systemConfigNavigatorPresenter;
	}

	public ApplicationKeyPresenter getApplicationKeyPresenter() {
		return applicationKeyPresenter;
	}

	public RegexPoolPresenter getRegexPoolPresenter() {
		return regexPoolPresenter;
	}

	public WorkflowManagmentPresenter getWorkflowManagmentPresenter() {
		return workflowManagmentPresenter;
	}

	public LicenseDetailsPresenter getLicenseDetailsPresenter() {
		return licenseDetailsPresenter;
	}

	public List<PluginDetailsDTO> getAllPluginsList() {
		return allPluginsList;
	}

	public void setAllPluginsList(List<PluginDetailsDTO> allPluginsList) {
		this.allPluginsList = allPluginsList;
	}

	public List<LicenseDetailsDTO> getLicenseDetailsDTOList() {
		return licenseDetailsDTOList;
	}

	public void setLicenseDetailsDTOList(List<LicenseDetailsDTO> licenseDetailsDTOList) {
		this.licenseDetailsDTOList = licenseDetailsDTOList;
	}

	public PluginDetailsDTO getSelectedPluginByName(String selectedNodeDisplayName) {
		PluginDetailsDTO selectedPluginDTO = null;
		for (PluginDetailsDTO pluginDTO : getAllPluginsList()) {
			if (selectedNodeDisplayName.equalsIgnoreCase(pluginDTO.getPluginName())) {
				selectedPluginDTO = pluginDTO;
				break;
			}
		}
		return selectedPluginDTO;
	}

	public PluginDetailsDTO getPluginDTOForID(String selectedPluginID) {
		PluginDetailsDTO plugin = null;
		if (!CollectionUtil.isEmpty(allPluginsList)) {
			for (PluginDetailsDTO pluginDTO : allPluginsList) {
				if (pluginDTO.getIdentifier().equals(selectedPluginID)) {
					plugin = pluginDTO;
					break;
				}
			}
		}
		return plugin;
	}

	public List<ConnectionsDTO> getAllConnections() {
		return allConnections;
	}

	public void setAllConnections(List<ConnectionsDTO> allConnections) {
		this.allConnections = allConnections;
	}

	public ConnectionsCompositePresenter getConnectionsCompositePresenter() {
		return connectionsCompositePresenter;
	}

	public void setConnectionsCompositePresenter(ConnectionsCompositePresenter connectionsCompositePresenter) {
		this.connectionsCompositePresenter = connectionsCompositePresenter;
	}

	public RegexGroupDTO getSelectedRegexGroupDTO() {
		return selectedRegexGroupDTO;
	}

	public void setSelectedRegexGroupDTO(RegexGroupDTO selectedRegexGroupDTO) {
		this.selectedRegexGroupDTO = selectedRegexGroupDTO;
	}

	public RegexPatternDTO getSelectedRegexPatternDTO() {
		return selectedRegexPatternDTO;
	}

	public void setSelectedRegexPatternDTO(RegexPatternDTO selectedRegexPatternDTO) {
		this.selectedRegexPatternDTO = selectedRegexPatternDTO;
	}

	public static class SystemConfigEventBus {

		private static EventBus eventBus;

		private SystemConfigEventBus() {
		}

		public static void fireEvent(final GwtEvent<?> event) {
			if (null != eventBus) {
				eventBus.fireEvent(event);
			}
		}

		private static void registerEventBus(final EventBus eventBus) {
			SystemConfigEventBus.eventBus = eventBus;
		}
	}

	public RegexBuilderView getRegexBuilderView() {
		return regexBuilderView;
	}

	public void setRegexBuilderView(RegexBuilderView regexBuilderView) {
		this.regexBuilderView = regexBuilderView;
	}

	public RegexBuilderPresenter getRegexBuilderPresenter() {
		return regexBuilderPresenter;
	}

	public void setRegexBuilderPresenter(RegexBuilderPresenter regexBuilderPresenter) {
		this.regexBuilderPresenter = regexBuilderPresenter;
	}

	public RegexPoolView getRegexPoolView() {
		return regexPoolView;
	}

	public RegexPatternView getRegexPatternView() {
		return regexPatternView;
	}

	public RegexPatternViewPresenter getRegexPatternViewPresenter() {
		return regexPatternViewPresenter;
	}

	public Collection<RegexGroupDTO> getRegexGroupList() {
		return regexGroupList;
	}

	public void setRegexGroupList(Collection<RegexGroupDTO> regexGroupList) {
		this.regexGroupList = regexGroupList;
	}

	public void addNavigationHandler() {
		RootPanel.get().addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.isControlKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_K) {
					handleNonSiblingNavigationhandler(event);
				} else if (event.isControlKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_I) {
				}
			}
		}, KeyDownEvent.getType());
	}

	protected void handleNonSiblingNavigationhandler(KeyDownEvent event) {
		event.preventDefault();
		if (getNavigationView().getNavigationTree().getCurrentNode().getDisplayName().equals(SystemConfigConstants.REGEX_GROUP)) {
			if (regexPoolView.getRegexGroupView().getRegexGroupGrid().isValid()) {
				if (null != getSelectedRegexGroupDTO()) {
					this.getEventBus().fireEvent(new RegexGroupSelectionEvent(getSelectedRegexGroupDTO()));
				} else {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.PLEASE_SOME_REGEX_GROUP_TO_OPEN), DialogIcon.ERROR);
				}
			}
		}
	}

	/**
	 * @return the currentBindedPresenter
	 */
	public SystemConfigCompositePresenter<?, ?> getCurrentBindedPresenter() {
		return currentBindedPresenter;
	}

	/**
	 * @param currentBindedPresenter the currentBindedPresenter to set
	 */
	public void setCurrentBindedPresenter(SystemConfigCompositePresenter<?, ?> currentBindedPresenter) {
		if (null != currentBindedPresenter) {
			this.currentBindedPresenter = currentBindedPresenter;
		}
	}
}
