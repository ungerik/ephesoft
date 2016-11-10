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

package com.ephesoft.gxt.systemconfig.client.presenter.connectionmanager;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.ConnectionsResult;
import com.ephesoft.gxt.core.shared.dto.Results;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.ConnectionsGridSelectionChangeEvent;
import com.ephesoft.gxt.systemconfig.client.event.DeleteSelectedConnectionDetailsViewEvent;
import com.ephesoft.gxt.systemconfig.client.event.HideConnectionDetailViewEvent;
import com.ephesoft.gxt.systemconfig.client.event.ShowAddConnectionDetailsViewEvent;
import com.ephesoft.gxt.systemconfig.client.event.ShowEditConnectionDetailsViewEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigCompositePresenter;
import com.ephesoft.gxt.systemconfig.client.view.connectionmanager.ConnectionsView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class ConnectionsCompositePresenter extends SystemConfigCompositePresenter<ConnectionsView, String> {

	interface CustomEventBinder extends EventBinder<ConnectionsCompositePresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	List<ConnectionsDTO> allConnections;

	ConnectionsDTO selectedConnection;

	ConnectionDetailsPresenter connectionDetailPresenter;

	ConnectionGridPresenter connectionGridPresenter;

	ConnectionMenuPresenter connectionMenuPresenter;

	private List<ConnectionsDTO> listOfSelectedConnectionDTO;

	public ConnectionsCompositePresenter(SystemConfigController controller, ConnectionsView view) {
		super(controller, view);
		connectionDetailPresenter = new ConnectionDetailsPresenter(controller, view.getConnectionDetailsView());
		connectionGridPresenter = new ConnectionGridPresenter(controller, view.getConnectionGridView());
		connectionMenuPresenter = new ConnectionMenuPresenter(controller, view.getConnectionMenuView());

		getAllConnectionList();
	}

	private void getAllConnectionList() {

		controller.getRpcService().getAllConnectionsDTO(new AsyncCallback<List<ConnectionsDTO>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSuccess(List<ConnectionsDTO> result) {
				if (!CollectionUtil.isEmpty(result)) {
					controller.setAllConnections(result);
				} else {
					controller.setAllConnections(new ArrayList<ConnectionsDTO>());
				}

			}
		});

	}

	@Override
	public void bind() {
		controller.setBottomPanelHeader(null);
		selectedConnection = null;
	}

	@Override
	public void init(String selectedPluginID) {
		loadDataInGrid(controller.getAllConnections());
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void loadDataInGrid(List<ConnectionsDTO> allConnection) {
		connectionGridPresenter.loadGrid(allConnection);
	}

	@EventHandler
	public void onGridSelectionChanged(final ConnectionsGridSelectionChangeEvent dependencyChangeEvent) {
		if (null != dependencyChangeEvent) {
			selectedConnection = dependencyChangeEvent.getDto();
			connectionDetailPresenter.setCurrentSelectedConnectionDetails(dependencyChangeEvent.getDto());
			// connectionDetailPresenter.bind();
			hideConnectionDetails(null);

		}
	}

	@EventHandler
	public void onAddConnectionsClicked(final ShowAddConnectionDetailsViewEvent addNewConnectionClickedEvent) {
		ConnectionsDTO currentDisplayedConnection = new ConnectionsDTO();
		currentDisplayedConnection.setIdentifier(String.valueOf(0));
		currentDisplayedConnection.setNew(true);
		connectionDetailPresenter.setCurrentSelectedConnectionDetails(currentDisplayedConnection);
		controller.setBottomPanelHeader(LocaleDictionary.getConstantValue(SystemConfigConstants.CONNECTION_ADD_HEADER));
		connectionDetailPresenter.bind();
		controller.setBottomPanelView(view.getConnectionDetailsView());
		connectionDetailPresenter.setFocusOnConnectionName();
	}

	@EventHandler
	public void onEditConnectionsClicked(final ShowEditConnectionDetailsViewEvent addNewConnectionClickedEvent) {
		List<ConnectionsDTO> allConnections = controller.getAllConnections();
		if (CollectionUtil.isEmpty(allConnections)) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.NO_RECORD_TO_EDIT), DialogIcon.ERROR);
		} else if (null == selectedConnection) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.NO_CONNECTION_SELECTED), DialogIcon.ERROR);
		} else {
			connectionDetailPresenter.setCurrentSelectedConnectionDetails(selectedConnection);
			controller.setBottomPanelHeader(LocaleDictionary.getConstantValue(SystemConfigConstants.CONNECTION_EDIT_HEADER));
			selectedConnection.setDirty(true);
			connectionDetailPresenter.bind();
			controller.setBottomPanelView(view.getConnectionDetailsView());
			connectionDetailPresenter.setFocusOnConnectionName();
		}
	}

	@EventHandler
	public void onDeleteConnectionsClicked(final DeleteSelectedConnectionDetailsViewEvent addNewConnectionClickedEvent) {
		listOfSelectedConnectionDTO = connectionGridPresenter.getSelectedConnectionsDTO();
		List<ConnectionsDTO> allConnections = controller.getAllConnections();
		if (CollectionUtil.isEmpty(allConnections)) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.NO_RECORD_TO_DELETE), DialogIcon.ERROR);
		} else if (CollectionUtil.isEmpty(listOfSelectedConnectionDTO)) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.NO_CONNECTION_SELECTED), DialogIcon.ERROR);
		} else {
			ConfirmationDialog dialog = new ConfirmationDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.WARNING),
					LocaleDictionary.getMessageValue(SystemConfigMessages.DELETE_CONNECTIONS));
			TextButton yesButton = dialog.getButton(PredefinedButton.OK);
			yesButton.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					deleteSelectedConnection();
				}

			});
			dialog.show();
			dialog.center();
		}
	}

	private void deleteSelectedConnection() {
		final List<ConnectionsDTO> allConnections = controller.getAllConnections();
		boolean updateConnections = false;
		for (final ConnectionsDTO selectedConnectionDTO : listOfSelectedConnectionDTO) {
			if (allConnections.contains(selectedConnectionDTO)) {
				selectedConnectionDTO.setDeleted(true);
				updateConnections = true;
			}
		}
		if (updateConnections) {
			ScreenMaskUtility.maskScreen();
			controller.getRpcService().removeSelectedConnection(listOfSelectedConnectionDTO, new AsyncCallback<ConnectionsResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ScreenMaskUtility.unmaskScreen();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_DELETE_RECORD), DialogIcon.ERROR);
				}

				@Override
				public void onSuccess(ConnectionsResult connectionsResult) {
					ScreenMaskUtility.unmaskScreen();
					if (null == connectionsResult) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_DELETE_RECORD), DialogIcon.ERROR);
					} else {
						List<ConnectionsDTO> allConnections = connectionsResult.getAllConnections();
						Results result = connectionsResult.getConnectionResult();
						if (null != allConnections && null != result) {
							controller.setAllConnections(allConnections);
							connectionGridPresenter.loadGrid(allConnections);
							selectedConnection = null;
							hideConnectionDetails(null);
							if (result == Results.FAILURE) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_DELETE_ALL_RECORDS), DialogIcon.ERROR);
							} else if (result == Results.PARTIAL_SUCCESS) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_DELETE_SOME_RECORDS), DialogIcon.ERROR);
							} else {
								Message.display(LocaleDictionary.getConstantValue(SystemConfigConstants.SUCCESS_TITLE),
										LocaleDictionary.getMessageValue(SystemConfigMessages.CONNECTION_DELETED));
							}
						} else {
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_DELETE_RECORD), DialogIcon.ERROR);
						}
					}
				}
			});
		}
	}

	@EventHandler
	public void hideConnectionDetails(final HideConnectionDetailViewEvent hideConnecttionDetailsEvent) {
		if (null != selectedConnection) {
			selectedConnection.setDirty(false);
		}
		controller.setBottomPanelView(null);
	}

	public ConnectionsDTO getSelectedConnection() {
		return selectedConnection;
	}

	public void setSelectedConnection(ConnectionsDTO selectedConnection) {
		this.selectedConnection = selectedConnection;
	}
}
