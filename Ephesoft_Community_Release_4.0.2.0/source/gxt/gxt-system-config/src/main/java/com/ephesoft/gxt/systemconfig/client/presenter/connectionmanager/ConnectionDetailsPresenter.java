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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.HideConnectionDetailViewEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigInlinePresenter;
import com.ephesoft.gxt.systemconfig.client.view.connectionmanager.ConnectionDetailsView;
import com.ephesoft.gxt.systemconfig.client.widget.property.ConnectionType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;

public class ConnectionDetailsPresenter extends SystemConfigInlinePresenter<ConnectionDetailsView> {

	interface CustomEventBinder extends EventBinder<ConnectionDetailsPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private ConnectionsDTO selectedConnectionsDTO;

	private Map<ConnectionType, Integer> connectionTypeMap;

	public ConnectionDetailsPresenter(final SystemConfigController controller, final ConnectionDetailsView view) {
		super(controller, view);
		// dependencyTypeList = new ArrayList<String>();

		initializeConnectionTypesMap();
		insertConnectionTypeInListBox();

	}

	private void initializeConnectionTypesMap() {
		connectionTypeMap = new HashMap<ConnectionType, Integer>();
		int index = 0;
		for (final ConnectionType connectionType : ConnectionType.values()) {
			connectionTypeMap.put(connectionType, index);
			index++;
		}
	}

	private void insertConnectionTypeInListBox() {
		for (final ConnectionType connectionType : connectionTypeMap.keySet()) {
			final int index = connectionTypeMap.get(connectionType);
			view.insertValuesInConnectionTypeListBox(connectionType.getName(), index);
		}
		view.setConnectionType();
	}

	@Override
	public void bind() {
		loadSelectedConnectionDetails();
	}

	public void loadSelectedConnectionDetails() {
		if (null != selectedConnectionsDTO) {
			view.setConnectionName(selectedConnectionsDTO.getConnectionName());
			view.setConnectionDescription(selectedConnectionsDTO.getConnectionDescription());
			view.setHost(selectedConnectionsDTO.getHostName());
			view.setUserName(selectedConnectionsDTO.getUserName());
			view.clearExistingDomain();
			final String connectionTypeName = selectedConnectionsDTO.getDatabaseType();
			final ConnectionType selectedConnectionType = getConnectionTypeForName(connectionTypeName);
			if (null == selectedConnectionType) {
				view.setSelectedConnectionType(0);
			} else {
				view.setSelectedConnectionType(connectionTypeMap.get(selectedConnectionType));
			}
			// Clear password in case of MSSQL Windows Authentication
			if (view.getConnectionType().equals("MSSQL Windows Authentication")) {
				view.setPassword("");
				view.setDomain(selectedConnectionsDTO.getDomain());
			} else {
				view.setPassword(selectedConnectionsDTO.getPassword());
			}
			if (selectedConnectionsDTO.getPort() == 0) {
				view.setPort("");
			} else {
				view.setPort(String.valueOf(selectedConnectionsDTO.getPort()));
			}
			view.setDatabaseName(selectedConnectionsDTO.getDatabaseName());
			view.setConnectionURL(selectedConnectionsDTO.getConnectionURL());

		}

	}

	public ConnectionType getConnectionTypeForName(final String connectionTypeName) {
		ConnectionType selectedConnectionType = null;
		if (!StringUtil.isNullOrEmpty(connectionTypeName))
			for (final ConnectionType connectionType : connectionTypeMap.keySet()) {
				if (connectionType.getName().equalsIgnoreCase(connectionTypeName)) {
					selectedConnectionType = connectionType;
				}
			}
		return selectedConnectionType;
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void onCancelClicked() {
		// TODO Auto-generated method stub
		hideConnectionDetails();
	}

	public void hideConnectionDetails() {
		controller.getEventBus().fireEvent(new HideConnectionDetailViewEvent());
	}

	public void onSaveClicked() {
		if (!view.validateFields()) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.MANDATORY_FIELD_CANNOT_BE_BLANK), DialogIcon.ERROR);
		} else if (!validateUniqueConnection()) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.CONNECTION_WITH_SAME_NAME_ALREADY_EXISTS), DialogIcon.ERROR);
		} else {
			if (null != selectedConnectionsDTO) {
				if (selectedConnectionsDTO.isNew()) {
					setNewDataInDTO(selectedConnectionsDTO);

					controller.getAllConnections().add(selectedConnectionsDTO);
				} else if (selectedConnectionsDTO.isDirty()) {
					setNewDataInDTO(selectedConnectionsDTO);
				}
				ScreenMaskUtility.maskScreen();
				controller.getRpcService().updateAllConnectionsDTOs(controller.getAllConnections(),
						new AsyncCallback<List<ConnectionsDTO>>() {

							@Override
							public void onFailure(final Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(SystemConfigMessages.ERROR_UPDATING_CONNECTIONS),
										DialogIcon.ERROR);
							}

							@Override
							public void onSuccess(final List<ConnectionsDTO> result) {
								ScreenMaskUtility.unmaskScreen();
								controller.setAllConnections(result);
								controller.getConnectionsCompositePresenter().loadDataInGrid(result);
								controller.getEventBus().fireEvent(new HideConnectionDetailViewEvent());
								Message.display(LocaleDictionary.getConstantValue(SystemConfigConstants.SUCCESS_TITLE),
										LocaleDictionary.getMessageValue(SystemConfigMessages.CONNECTION_SAVED));
							}
						});
			}
		}
	}

	private boolean validateUniqueConnection() {
		boolean isConnectionUnique = true;
		final String connectionNameValue = view.getConnectionName();
		final List<ConnectionsDTO> allConnections = controller.getAllConnections();
		for (final ConnectionsDTO connection : allConnections) {
			if (connectionNameValue.equalsIgnoreCase(connection.getConnectionName())) {
				if (selectedConnectionsDTO.isNew()) {
					isConnectionUnique = false;
					break;
				} else if (selectedConnectionsDTO.isDirty()) {
					if (!selectedConnectionsDTO.getIdentifier().equalsIgnoreCase(connection.getIdentifier())) {
						isConnectionUnique = false;
						break;
					}
				}
			}
		}

		return isConnectionUnique;
	}

	private void setNewDataInDTO(final ConnectionsDTO newConnection) {
		if (null != newConnection) {
			newConnection.setConnectionName(view.getConnectionName());
			newConnection.setConnectionDescription(view.getConnectionDescription());
			newConnection.setHostName(view.getHost());
			newConnection.setDatabaseType(view.getConnectionType());
			newConnection.setUserName(view.getUserName());
			newConnection.setDatabaseName(view.getDatabaseName());
			newConnection.setPassword(view.getPassword());
			newConnection.setPort(Integer.valueOf(view.getPort()));
			newConnection.setConnectionURL(view.getConnectionURL());

			newConnection.setDecryptedPassword(view.getPassword());
			newConnection.setDomain(view.getDomain());
		}
	}

	public void genrateConnectionString() {
		String connectionString = SystemConfigConstants.EMPTY_STRING;
		final String hostName = view.getHost();
		final String connectionType = view.getConnectionType();
		final String dbName = view.getDatabaseName();
		final String portNo = view.getPort();
		final String domainName = view.getDomain();
		final ConnectionType selectedConnectionType = getConnectionTypeForName(connectionType);
		if (ConnectionType.MYSQL.equals(selectedConnectionType) || ConnectionType.MARIADB.equals(selectedConnectionType)) {
			final String connectionTypeAnnotation = selectedConnectionType.getDriverURLAnnotation();
			connectionString = StringUtil.concatenate(connectionTypeAnnotation, SystemConfigConstants.SLASH,
					SystemConfigConstants.SLASH, hostName, SystemConfigConstants.COLON, portNo, SystemConfigConstants.SLASH, dbName);
		} else if (ConnectionType.MSSQL.equals(selectedConnectionType)) {
			final String connectionTypeAnnotation = selectedConnectionType.getDriverURLAnnotation();
			connectionString = StringUtil.concatenate(connectionTypeAnnotation, SystemConfigConstants.SLASH,
					SystemConfigConstants.SLASH, hostName, SystemConfigConstants.COLON, portNo, SystemConfigConstants.SEMI_COLON,
					SystemConfigConstants.DATABASE_NAME, SystemConfigConstants.EQUAL, dbName);
		} else if (ConnectionType.ORACLE.equals(selectedConnectionType)) {
			final String connectionTypeAnnotation = selectedConnectionType.getDriverURLAnnotation();
			connectionString = StringUtil.concatenate(connectionTypeAnnotation, SystemConfigConstants.AT_SIGN, hostName,
					SystemConfigConstants.COLON, portNo, SystemConfigConstants.SEMI_COLON, dbName);
		} else if (ConnectionType.MSSQL_ALWAYSON.equals(selectedConnectionType)) {
			final String connectionTypeAnnotation = selectedConnectionType.getDriverURLAnnotation();
			connectionString = StringUtil.concatenate(connectionTypeAnnotation, SystemConfigConstants.SLASH,
					SystemConfigConstants.SLASH, hostName, SystemConfigConstants.COLON, portNo, SystemConfigConstants.SEMI_COLON,
					SystemConfigConstants.DATABASE_NAME, SystemConfigConstants.EQUAL, dbName, SystemConfigConstants.SEMI_COLON,
					SystemConfigConstants.DOMAIN, SystemConfigConstants.EQUAL, domainName);
		}
		view.setConnectionURL(connectionString);
	}

	public ConnectionsDTO getCurrentSelectedConnectionDetails() {
		return this.selectedConnectionsDTO;
	}

	public void setCurrentSelectedConnectionDetails(final ConnectionsDTO currentDisplayedDependency) {
		this.selectedConnectionsDTO = currentDisplayedDependency;
	}

	public void onTestConnectionClicked() {
		final String userName = view.getUserName();
		// final String password = view.getPassword();
		final String password = this.selectedConnectionsDTO.getDecryptedPassword();
		final String connectionType = view.getConnectionType();
		final ConnectionType selectedConnectionType = getConnectionTypeForName(connectionType);
		final String driverClass = selectedConnectionType.getDriver();
		final String connectionURL = view.getConnectionURL();
		int portNumber = 0;
		try {
			portNumber = Integer.valueOf(view.getPort());

		} catch (NumberFormatException numberformatException) {
			String errorMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.PORT_MUST_BE_AN_INTEGER);
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE), errorMessage,
					DialogIcon.ERROR);
			return;
		}
		if (portNumber < SystemConfigConstants.MIN_PORT_NUMBER || portNumber > SystemConfigConstants.MAX_PORT_NUMBER) {
			String portNumberRange = LocaleDictionary.getMessageValue(SystemConfigMessages.PORT_NUMBER_RANGE);
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE), portNumberRange,
					DialogIcon.ERROR);
		} else if (StringUtil.isNullOrEmpty(connectionURL)) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.EMPTY_CONNECTION_URL), DialogIcon.ERROR);
		} else if (StringUtil.isNullOrEmpty(userName) && !ConnectionType.MSSQL_ALWAYSON.equals(selectedConnectionType)) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.EMPTY_USER_NAME), DialogIcon.ERROR);
		} else if (StringUtil.isNullOrEmpty(password) && !ConnectionType.MSSQL_ALWAYSON.equals(selectedConnectionType)) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.EMPTY_PASSWORD), DialogIcon.ERROR);
		} else if (StringUtil.isNullOrEmpty(view.getHost())) {
			String invalidHostName = LocaleDictionary.getMessageValue(SystemConfigMessages.INVALID_HOST_NAME);
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE), invalidHostName,
					DialogIcon.ERROR);
		} else if (StringUtil.isNullOrEmpty(driverClass)) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.INVALID_DB_TYPE), DialogIcon.ERROR);
		} else if (ConnectionType.MSSQL_ALWAYSON.equals(selectedConnectionType) && StringUtil.isNullOrEmpty(view.getDomain())) {
			String invalidDomain = LocaleDictionary.getMessageValue(SystemConfigMessages.INVALID_DOMAIN_NAME);
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE), invalidDomain,
					DialogIcon.ERROR);
		} else if (ConnectionType.MSSQL_ALWAYSON.equals(selectedConnectionType) && StringUtil.isNullOrEmpty(view.getDatabaseName())) {
			String invalidDbName = LocaleDictionary.getMessageValue(SystemConfigMessages.INVALID_DB_NAME);
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE), invalidDbName,
					DialogIcon.ERROR);
		} else {
			ScreenMaskUtility.maskScreen();
			if (!ConnectionType.MSSQL_ALWAYSON.equals(selectedConnectionType)) {
				controller.getRpcService().testConnection(connectionURL, userName, password, driverClass,
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(final Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(SystemConfigMessages.CONNECTION_UNSUCCESSFUL),
										DialogIcon.ERROR);
							}

							@Override
							public void onSuccess(final Boolean result) {
								ScreenMaskUtility.unmaskScreen();
								if (result) {
									Message.display(LocaleDictionary.getConstantValue(SystemConfigConstants.SUCCESS_TITLE),
											LocaleDictionary.getMessageValue(SystemConfigMessages.CONNECTION_SUCCESSFUL));
								} else {
									DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
											LocaleDictionary.getMessageValue(SystemConfigMessages.CONNECTION_UNSUCCESSFUL),
											DialogIcon.ERROR);
								}
							}
						});
			} else {
				controller.getRpcService().testMSSQLAlwaysONConnection(view.getDatabaseName(), view.getConnectionURL(),
						new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(final Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(SystemConfigMessages.CONNECTION_UNSUCCESSFUL),
										DialogIcon.ERROR);
							}

							@Override
							public void onSuccess(final Boolean result) {
								ScreenMaskUtility.unmaskScreen();
								if (result) {
									Message.display(LocaleDictionary.getConstantValue(SystemConfigConstants.SUCCESS_TITLE),
											LocaleDictionary.getMessageValue(SystemConfigMessages.CONNECTION_SUCCESSFUL));
								} else {
									DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
											LocaleDictionary.getMessageValue(SystemConfigMessages.CONNECTION_UNSUCCESSFUL),
											DialogIcon.ERROR);
								}
							}
						});
			}
		}

	}

	public void setFocusOnConnectionName() {
		view.setFocusOnConnectionName();
	}

	public void passwordValueChangeHandler(final String password) {
		this.selectedConnectionsDTO.setPassword(password);
		this.selectedConnectionsDTO.setDecryptedPassword(password);
	}
}
