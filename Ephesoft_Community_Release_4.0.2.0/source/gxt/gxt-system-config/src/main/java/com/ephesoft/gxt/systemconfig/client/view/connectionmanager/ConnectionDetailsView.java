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

package com.ephesoft.gxt.systemconfig.client.view.connectionmanager;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.MandatoryLabel;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.RangeValidator;
import com.ephesoft.gxt.core.client.validator.form.EmptyValueValidator;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.connectionmanager.ConnectionDetailsPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.ephesoft.gxt.systemconfig.client.widget.property.ConnectionType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

public class ConnectionDetailsView extends SystemConfigInlineView<ConnectionDetailsPresenter> {

	interface Binder extends UiBinder<Widget, ConnectionDetailsView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected MandatoryLabel connectionNameLabel;

	@UiField
	protected TextField connectionName;

	@UiField
	protected MandatoryLabel hostLabel;

	@UiField
	protected TextField host;

	@UiField
	protected MandatoryLabel connectionDescriptionLabel;

	@UiField
	protected TextField connectionDescription;

	@UiField
	protected MandatoryLabel userNameLabel;

	@UiField
	protected TextField userName;

	@UiField
	protected MandatoryLabel connectionTypeLabel;

	@UiField
	protected ComboBox connectionType;

	@UiField
	protected MandatoryLabel databaseNameLabel;

	@UiField
	protected TextField databaseName;

	@UiField
	protected MandatoryLabel passwordLabel;

	@UiField
	protected PasswordField password;

	@UiField
	protected MandatoryLabel portLabel;

	@UiField
	protected TextField port;

	@UiField
	protected MandatoryLabel domainLabel;

	@UiField
	protected TextField domainTextField;

	public String getDomain() {
		return domainTextField.getText();
	}

	@UiField
	protected MandatoryLabel connectionURLLable;

	@UiField
	protected TextArea connectionURL;

	@UiField
	protected Button testConnectionButton;

	@UiField
	protected Button saveButton;

	@UiField
	protected Button cancelButton;

	@UiField
	protected VerticalLayoutContainer connectionManagerBottomPanel;

	public ConnectionDetailsView() {
		super();
		initWidget(binder.createAndBindUi(this));

		// Sets text and other important properties for all widgets.
		setWidgetProperties();

		// Sets ids for each widget.
		setWidgetIDs();

		// Add value change handlers to widgets where required.
		addValueChangeHandlers();

		// Add value change handlers to widgets where required.
		addValidators();
		connectionType.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				final ConnectionType selectedConnectionType = presenter.getConnectionTypeForName(event.getValue());
				connectionType.setToolTip(connectionType.getValue());
				if (ConnectionType.MSSQL_ALWAYSON.equals(selectedConnectionType)) {
					domainLabel.setVisible(true);
					domainTextField.getElement().setVisibility(true);
					userName.setEnabled(false);
					password.setEnabled(false);
					userName.clear();
					password.clear();
				} else {
					domainLabel.setVisible(false);
					domainTextField.getElement().setVisibility(false);
					userName.setEnabled(true);
					password.setEnabled(true);
				}
			}
		});

		addStyle();

		this.passwordValueChangeHandler();
	}

	private void addValidators() {

		final String emptyMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.VALUE_NOT_EMPTY);
		connectionName.addValidator(new EmptyValueValidator(emptyMessage));
		host.addValidator(new EmptyValueValidator(emptyMessage));
		userName.addValidator(new EmptyValueValidator(emptyMessage));
		password.addValidator(new EmptyValueValidator(emptyMessage));
		connectionURL.addValidator(new EmptyValueValidator(emptyMessage));
		connectionDescription.addValidator(new EmptyValueValidator(emptyMessage));
		databaseName.addValidator(new EmptyValueValidator(emptyMessage));
		port.addValidator(new EmptyValueValidator(emptyMessage));
		domainTextField.addValidator(new EmptyValueValidator(LocaleDictionary
				.getMessageValue(SystemConfigMessages.INVALID_DOMAIN_NAME)));
		port.addValidator(new RangeValidator(Integer.class, LocaleDictionary.getMessageValue(SystemConfigMessages.PORT_NUMBER_RANGE),
				SystemConfigConstants.MIN_PORT_NUMBER, SystemConfigConstants.MAX_PORT_NUMBER));
	}

	private void setWidgetProperties() {
		connectionURL.setWidth("300px");
		connectionNameLabel.setLabelText(LocaleDictionary.getConstantValue(SystemConfigConstants.CONNECTION_NAME_LABEL));
		connectionDescriptionLabel.setLabelText(LocaleDictionary.getConstantValue(SystemConfigConstants.CONNECTION_DESCRIPTION_LABEL));
		hostLabel.setLabelText(LocaleDictionary.getConstantValue(SystemConfigConstants.HOST_LABEL));
		connectionTypeLabel.setLabelText(LocaleDictionary.getConstantValue(SystemConfigConstants.CONNECTION_TYPE_LABEL));
		userNameLabel.setLabelText(LocaleDictionary.getConstantValue(SystemConfigConstants.USER_NAME_LABEL));
		databaseNameLabel.setLabelText(LocaleDictionary.getConstantValue(SystemConfigConstants.DATABASE_NAME_LABEL));
		passwordLabel.setLabelText(LocaleDictionary.getConstantValue(SystemConfigConstants.PASSWORD_LABEL));
		portLabel.setLabelText(LocaleDictionary.getConstantValue(SystemConfigConstants.PORT_LABEL));
		domainLabel.setLabelText(LocaleDictionary.getConstantValue(SystemConfigConstants.DOMAIN_LABEL));
		connectionURLLable.setLabelText(LocaleDictionary.getConstantValue(SystemConfigConstants.CONNECTION_URL_LABEL));
		testConnectionButton.setText(LocaleDictionary.getConstantValue(SystemConfigConstants.TEST_CONNECTION_BUTTON));
		saveButton.setText(LocaleDictionary.getConstantValue(SystemConfigConstants.SAVE_BUTTON));
		cancelButton.setText(LocaleDictionary.getConstantValue(SystemConfigConstants.CANCEL_BUTTON));
		domainLabel.setVisible(false);
		domainTextField.getElement().setVisibility(false);
		connectionType.setEditable(false);

	}

	public boolean validateFields() {
		boolean isValidFields = false;
		if (getConnectionType().equals(ConnectionType.MSSQL_ALWAYSON) && domainTextField.isValid()) {
			isValidFields = true;
		}
		if (connectionName.isValid() && host.isValid() && userName.isValid() && password.isValid() && connectionURL.isValid()
				&& connectionDescription.isValid() && databaseName.isValid() && port.isValid()) {
			isValidFields = true;
		}
		return isValidFields;
	}

	private void setWidgetIDs() {
		WidgetUtil.setID(connectionNameLabel, "CDV_connectionName_Label");
		WidgetUtil.setID(connectionDescriptionLabel, "CDV_connectionDescription_Label");
		WidgetUtil.setID(hostLabel, "CDV_host_Label");
		WidgetUtil.setID(connectionTypeLabel, "CDV_connectionType_Label");
		WidgetUtil.setID(userNameLabel, "CDV_userName_Label");
		WidgetUtil.setID(databaseNameLabel, "CDV_databaseName_Label");
		WidgetUtil.setID(passwordLabel, "CDV_password_Label");
		WidgetUtil.setID(portLabel, "CDV_port_Label");
		WidgetUtil.setID(connectionURLLable, "CDV_connectionURL_Label");

		WidgetUtil.setID(connectionName, "CDV_connectionName_Textbox");
		WidgetUtil.setID(connectionDescription, "CDV_connectionDescription_Textbox");
		WidgetUtil.setID(host, "CDV_host_Textbox");
		WidgetUtil.setID(connectionType, "CDV_connectionType_Textbox");
		WidgetUtil.setID(userName, "CDV_userName_Textbox");
		WidgetUtil.setID(databaseName, "CDV_databaseName_Textbox");
		WidgetUtil.setID(password, "CDV_password_Textbox");
		WidgetUtil.setID(port, "CDV_port_Textbox");
		WidgetUtil.setID(connectionURL, "CDV_connectionURL_Textbox");

		WidgetUtil.setID(testConnectionButton, "CDV_testConnection_Button");
		WidgetUtil.setID(saveButton, "CDV_save_Button");
		WidgetUtil.setID(cancelButton, "CDV_cancel_Button");
	}

	private void addValueChangeHandlers() {
		final ValueChangeHandler<String> valueChangedHandler = new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				presenter.genrateConnectionString();
			}

		};

		final KeyUpHandler keyReleasedHandler = new KeyUpHandler() {

			@Override
			public void onKeyUp(final KeyUpEvent event) {
				presenter.genrateConnectionString();
			}
		};

		final KeyDownHandler keyPressedHandler = new KeyDownHandler() {

			@Override
			public void onKeyDown(final KeyDownEvent event) {
				if (event.getNativeKeyCode() >= KeyCodes.KEY_A || event.getNativeKeyCode() >= KeyCodes.KEY_Z) {
					event.preventDefault();
				}
			}
		};

		connectionType.addValueChangeHandler(valueChangedHandler);
		domainTextField.addKeyUpHandler(keyReleasedHandler);
		host.addKeyUpHandler(keyReleasedHandler);
		databaseName.addKeyUpHandler(keyReleasedHandler);
		port.addKeyUpHandler(keyReleasedHandler);
		// port.addKeyDownHandler(keyPressedHandler);
	}

	@Override
	public void initialize() {
	}

	@UiHandler("cancelButton")
	public void onCancelClicked(final ClickEvent event) {
		presenter.onCancelClicked();
	}

	@UiHandler("saveButton")
	public void onSaveClicked(final ClickEvent event) {
		presenter.onSaveClicked();
	}

	@UiHandler("testConnectionButton")
	public void onTestConnectionClicked(final ClickEvent event) {
		presenter.onTestConnectionClicked();
	}

	public void insertValuesInConnectionTypeListBox(final String value, final int index) {
		connectionType.getStore().add(index, value);
	}

	public void setHost(final String host) {
		this.host.setValue(host, true);
		if (!StringUtil.isNullOrEmpty(host)) {
			this.host.validate();
		}
	}

	public void setDatabaseName(final String databaseName) {
		this.databaseName.setValue(databaseName, true);
		if (!StringUtil.isNullOrEmpty(databaseName)) {
			this.databaseName.validate();
		}
	}

	public int getSelectedConnectionType() {
		return connectionType.getSelectedIndex();
	}

	public void setConnectionName(final String connectionName) {
		this.connectionName.setValue(connectionName, true);
		if (!StringUtil.isNullOrEmpty(connectionName)) {
			this.connectionName.validate();
		}
	}

	public void setConnectionDescription(final String connectionDescription) {
		this.connectionDescription.setValue(connectionDescription, true);
		if (!StringUtil.isNullOrEmpty(connectionDescription)) {
			this.connectionDescription.validate();
		}
	}

	public void setUserName(final String userName) {
		this.userName.setValue(userName, true);
		if (!StringUtil.isNullOrEmpty(userName)) {
			this.userName.validate();
		}
	}

	public void setPassword(final String password) {
		// this.password.setValue(password, true);
		this.password.setValue(password);
		if (!StringUtil.isNullOrEmpty(password)) {
			this.password.validate();
		}
	}

	public void setPort(final String port) {
		this.port.setValue(port, true);
		if (!StringUtil.isNullOrEmpty(port)) {
			this.port.validate();
		}
	}

	public void setConnectionURL(final String connectionURL) {
		this.connectionURL.setValue(connectionURL, true);
		if (!StringUtil.isNullOrEmpty(connectionURL)) {
			this.connectionURL.validate();
		}
	}
	
	public void setDomain(final String domain) {
		this.domainTextField.setValue(domain);
		if (!StringUtil.isNullOrEmpty(domain)) {
			this.domainTextField.validate();
		}
	}

	public String getConnectionName() {
		return connectionName.getText();
	}

	public String getHost() {
		return host.getText();
	}

	public String getConnectionDescription() {
		return connectionDescription.getText();
	}

	public String getUserName() {
		return userName.getText();
	}

	public String getConnectionType() {
		return connectionType.getValue();
	}

	public String getDatabaseName() {
		return databaseName.getText();
	}

	public String getPassword() {
		return password.getText();
	}

	public String getPort() {
		return port.getText();
	}

	public String getConnectionURL() {
		return connectionURL.getText();
	}

	public void setFocusOnConnectionName() {
		final DelayedTask task = new DelayedTask() {

			@Override
			public void onExecute() {
				connectionName.focus();
			}
		};
		task.delay(100);
	}

	public void addStyle() {
		connectionManagerBottomPanel.addStyleName("connectionManagerBottomPanel");
		connectionNameLabel.addStyleName(SystemConfigConstants.BOLD_TEXT_CSS);
		connectionDescriptionLabel.addStyleName(SystemConfigConstants.BOLD_TEXT_CSS);
		hostLabel.addStyleName(SystemConfigConstants.BOLD_TEXT_CSS);
		connectionTypeLabel.addStyleName(SystemConfigConstants.BOLD_TEXT_CSS);
		userNameLabel.addStyleName(SystemConfigConstants.BOLD_TEXT_CSS);
		databaseNameLabel.addStyleName(SystemConfigConstants.BOLD_TEXT_CSS);
		passwordLabel.addStyleName(SystemConfigConstants.BOLD_TEXT_CSS);
		portLabel.addStyleName(SystemConfigConstants.BOLD_TEXT_CSS);
		domainLabel.addStyleName(SystemConfigConstants.BOLD_TEXT_CSS);
		connectionURLLable.addStyleName(SystemConfigConstants.BOLD_TEXT_CSS);
	}

	/**
	 * Sets the selected value in connection type combobox.
	 * 
	 * @param index
	 */
	public void setSelectedConnectionType(final int index) {
		if (null != connectionType && connectionType.getStore().size() != 0) {
			final ListStore<String> batchClassComboBoxValues = connectionType.getStore();
			connectionType.setValue(batchClassComboBoxValues.get(index), true);
		}

	}

	public void setConnectionType() {
		connectionType.setValue(connectionType.getStore().get(0), true);
		connectionType.setToolTip(connectionType.getValue());
	}

	public void clearExistingDomain() {
		domainTextField.clear();
	}

	public void passwordValueChangeHandler() {

		password.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				presenter.passwordValueChangeHandler(password.getValue());

			}
		});

	}
}
