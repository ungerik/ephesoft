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

package com.ephesoft.gxt.core.shared.dto.propertyAccessors;

import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.BatchConstants;
import com.ephesoft.gxt.core.shared.dto.CustomValueProvider;
import com.ephesoft.gxt.core.shared.dto.EmailConfigurationDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * This interface provides Property Accessors for {@link EmailConfigurationDTO}
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */

public class EmailImportConfigurationProperties {

	private static EmailImportConfigurationProperties emailProperties;

	private ModelKeyProvider<EmailConfigurationDTO> identifier;

	private ValueProvider<EmailConfigurationDTO, String> userName;

	private CustomValueProvider<EmailConfigurationDTO, String> password;

	private ValueProvider<EmailConfigurationDTO, String> serverName;

	private ValueProvider<EmailConfigurationDTO, String> serverType;

	private ValueProvider<EmailConfigurationDTO, String> folderName;

	private ValueProvider<EmailConfigurationDTO, Integer> portNumber;

	private ValueProvider<EmailConfigurationDTO, Boolean> isSSL;

	private ValueProvider<EmailConfigurationDTO, Boolean> isDeleted;

	private ValueProvider<EmailConfigurationDTO, Boolean> isNew;

	private ValueProvider<EmailConfigurationDTO, Boolean> isEnabled;

	private ValueProvider<EmailConfigurationDTO, Boolean> selected;

	public static EmailImportConfigurationProperties getEmailProperties() {
		return emailProperties;
	}

	public ModelKeyProvider<EmailConfigurationDTO> getIdentifier() {
		return identifier;
	}

	public ValueProvider<EmailConfigurationDTO, String> getUserName() {
		return userName;
	}

	public CustomValueProvider<EmailConfigurationDTO, String> getPassword() {
		return password;
	}

	public ValueProvider<EmailConfigurationDTO, String> getServerName() {
		return serverName;
	}

	public ValueProvider<EmailConfigurationDTO, String> getServerType() {
		return serverType;
	}

	public ValueProvider<EmailConfigurationDTO, String> getFolderName() {
		return folderName;
	}

	public ValueProvider<EmailConfigurationDTO, Integer> getPortNumber() {
		return portNumber;
	}

	public ValueProvider<EmailConfigurationDTO, Boolean> getIsSSL() {
		return isSSL;
	}

	public ValueProvider<EmailConfigurationDTO, Boolean> getIsDeleted() {
		return isDeleted;
	}

	public ValueProvider<EmailConfigurationDTO, Boolean> getIsNew() {
		return isNew;
	}

	public ValueProvider<EmailConfigurationDTO, Boolean> getIsEnabled() {
		return isEnabled;
	}

	public ValueProvider<EmailConfigurationDTO, Boolean> getSelected() {
		return selected;
	}

	static {
		emailProperties = new EmailImportConfigurationProperties();
	}

	private EmailImportConfigurationProperties() {
		identifier = EmailConfigurationProperties.properties.identifier();
		selected = EmailConfigurationProperties.properties.selected();
		userName = EmailConfigurationProperties.properties.userName();
		password = new CustomValueProvider<EmailConfigurationDTO, String>(EmailConfigurationProperties.properties.password()) {

			@Override
			public String getValue(EmailConfigurationDTO object) {
				String passwordString = CoreCommonConstant.EMPTY_STRING;
				if (!StringUtil.isNullOrEmpty(object.getPassword())) {
					passwordString = BatchConstants.PASSWORD_DISPLAY_STRING;
				}
				return passwordString;
			}
		};
		serverName = EmailConfigurationProperties.properties.serverName();
		serverType = EmailConfigurationProperties.properties.serverType();
		folderName = EmailConfigurationProperties.properties.folderName();
		portNumber = EmailConfigurationProperties.properties.portNumber();
		isSSL = EmailConfigurationProperties.properties.isSSL();
		isDeleted = EmailConfigurationProperties.properties.isDeleted();
		isNew = EmailConfigurationProperties.properties.isNew();
		isEnabled = EmailConfigurationProperties.properties.isEnabled();
	}

	interface EmailConfigurationProperties extends PropertyAccess<EmailConfigurationDTO> {

		public static EmailConfigurationProperties properties = GWT.create(EmailConfigurationProperties.class);

		public ModelKeyProvider<EmailConfigurationDTO> identifier();

		public ValueProvider<EmailConfigurationDTO, String> userName();

		public ValueProvider<EmailConfigurationDTO, String> password();

		public ValueProvider<EmailConfigurationDTO, String> serverName();

		public ValueProvider<EmailConfigurationDTO, String> serverType();

		public ValueProvider<EmailConfigurationDTO, String> folderName();

		public ValueProvider<EmailConfigurationDTO, Integer> portNumber();

		public ValueProvider<EmailConfigurationDTO, Boolean> isSSL();

		public ValueProvider<EmailConfigurationDTO, Boolean> isDeleted();

		public ValueProvider<EmailConfigurationDTO, Boolean> isNew();

		public ValueProvider<EmailConfigurationDTO, Boolean> selected();

		public ValueProvider<EmailConfigurationDTO, Boolean> isEnabled();
	}
}
