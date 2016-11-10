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

package com.ephesoft.gxt.core.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Class that stores a record's information on client side.
 * 
 * @author Ephesoft
 */
public class CmisConfigurationDTO implements IsSerializable, Selectable {

	/** The batch class. */
	private BatchClassDTO batchClass;

	/** The server url. */
	private String serverURL;

	/** The user name. */
	private String userName;

	/** The password. */
	private String password;

	/** The repository id. */
	private String repositoryID;

	/** The file extension. */
	private String fileExtension;

	/** The folder name. */
	private String folderName;

	/** The cmis property. */
	private String cmisProperty;

	/** The value. */
	private String value;

	/** The value to update. */
	private String valueToUpdate;

	/** The identifier. */
	private String identifier;

	/** The is deleted. */
	private boolean isDeleted;

	/** The is new. */
	private boolean isNew;

	/** The selected. */
	private boolean selected;

	/**
	 * Gets the batch class.
	 *
	 * @return the batch class
	 */
	public BatchClassDTO getBatchClass() {
		return batchClass;
	}

	/**
	 * Sets the batch class.
	 *
	 * @param batchClass the new batch class
	 */
	public void setBatchClass(BatchClassDTO batchClass) {
		this.batchClass = batchClass;
	}

	/**
	 * Gets the server url.
	 *
	 * @return the server url
	 */
	public String getServerURL() {
		return serverURL;
	}

	/**
	 * Sets the server url.
	 *
	 * @param serverURL the new server url
	 */
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	/**
	 * Gets the user name.
	 *
	 * @return the user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName the new user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Gets the password.
	 *
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 *
	 * @param password the new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the repository id.
	 *
	 * @return the repository id
	 */
	public String getRepositoryID() {
		return repositoryID;
	}

	/**
	 * Sets the repository id.
	 *
	 * @param repositoryID the new repository id
	 */
	public void setRepositoryID(String repositoryID) {
		this.repositoryID = repositoryID;
	}

	/**
	 * Gets the file extension.
	 *
	 * @return the file extension
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * Sets the file extension.
	 *
	 * @param fileExtension the new file extension
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	/**
	 * Gets the folder name.
	 *
	 * @return the folder name
	 */
	public String getFolderName() {
		return folderName;
	}

	/**
	 * Sets the folder name.
	 *
	 * @param folderName the new folder name
	 */
	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	/**
	 * Gets the cmis property.
	 *
	 * @return the cmis property
	 */
	public String getCmisProperty() {
		return cmisProperty;
	}

	/**
	 * Sets the cmis property.
	 *
	 * @param cmisProperty the new cmis property
	 */
	public void setCmisProperty(String cmisProperty) {
		this.cmisProperty = cmisProperty;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the value to update.
	 *
	 * @return the value to update
	 */
	public String getValueToUpdate() {
		return valueToUpdate;
	}

	/**
	 * Sets the value to update.
	 *
	 * @param valueToUpdate the new value to update
	 */
	public void setValueToUpdate(String valueToUpdate) {
		this.valueToUpdate = valueToUpdate;
	}

	/**
	 * Gets the identifier.
	 *
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets the identifier.
	 *
	 * @param identifier the new identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Checks if is new.
	 *
	 * @return true, if is new
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * Sets the new.
	 *
	 * @param isNew the new new
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * Checks if is deleted.
	 *
	 * @return true, if is deleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * Sets the deleted.
	 *
	 * @param isDeleted the new deleted
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/* (non-Javadoc)
	 * @see com.ephesoft.gxt.core.shared.dto.Selectable#isSelected()
	 */
	@Override
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Sets the selected.
	 *
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

}
