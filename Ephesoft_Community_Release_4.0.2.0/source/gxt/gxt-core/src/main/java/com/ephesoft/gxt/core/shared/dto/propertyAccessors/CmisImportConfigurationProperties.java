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
import com.ephesoft.gxt.core.shared.dto.CmisConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.CustomValueProvider;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

/**
 * This interface provides Property Accessors for {@link CmisConfigurationDTO}
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */

public class CmisImportConfigurationProperties {

	private static CmisImportConfigurationProperties cmisProperties;

	private ModelKeyProvider<CmisConfigurationDTO> identifier;

	private ValueProvider<CmisConfigurationDTO, String> serverURL;

	private ValueProvider<CmisConfigurationDTO, String> userName;

	private CustomValueProvider<CmisConfigurationDTO, String> password;

	private ValueProvider<CmisConfigurationDTO, String> repositoryID;

	private ValueProvider<CmisConfigurationDTO, String> fileExtension;

	private ValueProvider<CmisConfigurationDTO, String> folderName;

	private ValueProvider<CmisConfigurationDTO, String> cmisProperty;

	private ValueProvider<CmisConfigurationDTO, String> value;

	private ValueProvider<CmisConfigurationDTO, String> valueToUpdate;

	private ValueProvider<CmisConfigurationDTO, Boolean> isDeleted;

	private ValueProvider<CmisConfigurationDTO, Boolean> isNew;

	private ValueProvider<CmisConfigurationDTO, Boolean> selected;

	public static CmisImportConfigurationProperties getCmisProperties() {
		return cmisProperties;
	}

	public ModelKeyProvider<CmisConfigurationDTO> getIdentifier() {
		return identifier;
	}

	public ValueProvider<CmisConfigurationDTO, String> getServerURL() {
		return serverURL;
	}

	public ValueProvider<CmisConfigurationDTO, String> getUserName() {
		return userName;
	}

	public CustomValueProvider<CmisConfigurationDTO, String> getPassword() {
		return password;
	}

	public ValueProvider<CmisConfigurationDTO, String> getRepositoryID() {
		return repositoryID;
	}

	public ValueProvider<CmisConfigurationDTO, String> getFileExtension() {
		return fileExtension;
	}

	public ValueProvider<CmisConfigurationDTO, String> getFolderName() {
		return folderName;
	}

	public ValueProvider<CmisConfigurationDTO, String> getCmisProperty() {
		return cmisProperty;
	}

	public ValueProvider<CmisConfigurationDTO, String> getValue() {
		return value;
	}

	public ValueProvider<CmisConfigurationDTO, String> getValueToUpdate() {
		return valueToUpdate;
	}

	public ValueProvider<CmisConfigurationDTO, Boolean> getIsDeleted() {
		return isDeleted;
	}

	public ValueProvider<CmisConfigurationDTO, Boolean> getIsNew() {
		return isNew;
	}

	public ValueProvider<CmisConfigurationDTO, Boolean> getSelected() {
		return selected;
	}

	static {
		cmisProperties = new CmisImportConfigurationProperties();
	}

	private CmisImportConfigurationProperties() {
		identifier = CmisConfigurationProperties.properties.identifier();
		selected = CmisConfigurationProperties.properties.selected();
		serverURL = CmisConfigurationProperties.properties.serverURL();
		userName = CmisConfigurationProperties.properties.userName();
		password = new CustomValueProvider<CmisConfigurationDTO, String>(CmisConfigurationProperties.properties.password()) {

			@Override
			public String getValue(CmisConfigurationDTO object) {
				String passwordString = CoreCommonConstant.EMPTY_STRING;
				if (!StringUtil.isNullOrEmpty(object.getPassword())) {
					passwordString = BatchConstants.PASSWORD_DISPLAY_STRING;
				}
				return passwordString;
			}
		};
		repositoryID = CmisConfigurationProperties.properties.repositoryID();
		fileExtension = CmisConfigurationProperties.properties.fileExtension();
		folderName = CmisConfigurationProperties.properties.folderName();
		cmisProperty = CmisConfigurationProperties.properties.cmisProperty();
		value = CmisConfigurationProperties.properties.value();
		valueToUpdate = CmisConfigurationProperties.properties.valueToUpdate();
		isDeleted = CmisConfigurationProperties.properties.isDeleted();
		isNew = CmisConfigurationProperties.properties.isNew();
	}

	interface CmisConfigurationProperties extends PropertyAccess<CmisConfigurationDTO> {

		public static CmisConfigurationProperties properties = GWT.create(CmisConfigurationProperties.class);

		ModelKeyProvider<CmisConfigurationDTO> identifier();

		ValueProvider<CmisConfigurationDTO, String> serverURL();

		ValueProvider<CmisConfigurationDTO, String> userName();

		ValueProvider<CmisConfigurationDTO, String> password();

		ValueProvider<CmisConfigurationDTO, String> repositoryID();

		ValueProvider<CmisConfigurationDTO, String> fileExtension();

		ValueProvider<CmisConfigurationDTO, String> folderName();

		ValueProvider<CmisConfigurationDTO, String> cmisProperty();

		ValueProvider<CmisConfigurationDTO, String> value();

		ValueProvider<CmisConfigurationDTO, String> valueToUpdate();

		ValueProvider<CmisConfigurationDTO, Boolean> isDeleted();

		ValueProvider<CmisConfigurationDTO, Boolean> isNew();

		ValueProvider<CmisConfigurationDTO, Boolean> selected();
	}

}
