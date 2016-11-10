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

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

// TODO: Auto-generated Javadoc
/**
 * Class that stores a record's information on client side.
 * 
 * @author Ephesoft
 */
public class WebScannerConfigurationDTO implements IsSerializable, Selectable {

	/** The batch class. */
	private BatchClassDTO batchClass;

	/** The name. */
	private String name;

	/** The description. */
	private String description;

	/** The value. */
	private String value;

	/** The identifier. */
	private String identifier;

	/** The is deleted. */
	private boolean isDeleted;

	/** The is new. */
	private boolean isNew;

	/** The is mandatory. */
	private boolean isMandatory;

	/** The is multi value. */
	private boolean isMultiValue;

	/** The data type. */
	private String dataType;

	/** The sample value. */
	private List<String> sampleValue;

	/** The parent. */
	private WebScannerConfigurationDTO parent;

	/** The children. */
	private Collection<WebScannerConfigurationDTO> children;

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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Gets the parent.
	 *
	 * @return the parent
	 */
	public WebScannerConfigurationDTO getParent() {
		return parent;
	}

	/**
	 * Sets the parent.
	 *
	 * @param parent the new parent
	 */
	public void setParent(WebScannerConfigurationDTO parent) {
		this.parent = parent;
	}

	/**
	 * Gets the children.
	 *
	 * @return the children
	 */
	public Collection<WebScannerConfigurationDTO> getChildren() {
		return children;
	}

	/**
	 * Sets the children.
	 *
	 * @param children the new children
	 */
	public void setChildren(Collection<WebScannerConfigurationDTO> children) {
		this.children = children;
	}

	/**
	 * Sets the deleted.
	 *
	 * @param isDeleted the new deleted
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
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
	 * Sets the new.
	 *
	 * @param isNew the new new
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
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
	 * Gets the data type.
	 *
	 * @return the data type
	 */
	public String getDataType() {
		return dataType;
	}

	/**
	 * Sets the data type.
	 *
	 * @param dataType the new data type
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	/**
	 * Sets the mandatory.
	 *
	 * @param isMandatory the new mandatory
	 */
	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	/**
	 * Checks if is mandatory.
	 *
	 * @return true, if is mandatory
	 */
	public boolean isMandatory() {
		return isMandatory;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the sample value.
	 *
	 * @param sampleValue the new sample value
	 */
	public void setSampleValue(List<String> sampleValue) {
		this.sampleValue = sampleValue;
	}

	/**
	 * Gets the sample value.
	 *
	 * @return the sample value
	 */
	public List<String> getSampleValue() {
		return sampleValue;
	}

	/**
	 * Sets the multi value.
	 *
	 * @param isMultiValue the new multi value
	 */
	public void setMultiValue(boolean isMultiValue) {
		this.isMultiValue = isMultiValue;
	}

	/**
	 * Checks if is multi value.
	 *
	 * @return true, if is multi value
	 */
	public boolean isMultiValue() {
		return isMultiValue;
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
