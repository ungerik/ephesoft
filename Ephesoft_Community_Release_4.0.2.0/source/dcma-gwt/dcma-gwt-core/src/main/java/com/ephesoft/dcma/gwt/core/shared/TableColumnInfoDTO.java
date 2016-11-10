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

package com.ephesoft.dcma.gwt.core.shared;

import com.ephesoft.dcma.gwt.core.shared.comparator.SortingProperty;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TableColumnInfoDTO implements IsSerializable, SortingProperty {

	private TableInfoDTO tableInfoDTO;

	private String betweenLeft;

	private String betweenRight;

	private String identifier;

	private String columnName;

	private String columnPattern;

	private boolean required;

	/**
	 * True if this column data is mandatory to be present once in a row in table.
	 */
	private boolean mandatory;

	private boolean isNew;

	private boolean isDeleted;

	private String columnHeaderPattern;

	private String columnStartCoordinate;

	private String columnEndCoordinate;

	private String columnCoordY0;

	private String columnCoordY1;

	private String alternateValues;

	/**
	 * Instance of validationPattern.
	 */
	private String validationPattern;

	/**
	 * extractedDataColumnName {@link String} specifies the column name from which data needs to be extracted.
	 */
	private String extractedDataColumnName;

	/**
	 * {@link String} required to store the order number at UI
	 */
	private String orderNumber;

	/**
	 * Boolean that determines whether a column field is currency or not.
	 */
	private boolean isCurrency;

	/**
	 * {@link String} that indicates the descriptive information about the column.
	 */
	private String columnDescription;

	/**
	 * @return the isCurrency
	 */
	public boolean isCurrency() {
		return isCurrency;
	}

	/**
	 * @param isCurrency the isCurrency to set
	 */
	public void setCurrency(boolean isCurrency) {
		this.isCurrency = isCurrency;
	}

	public TableInfoDTO getTableInfoDTO() {
		return tableInfoDTO;
	}

	public void setTableInfoDTO(TableInfoDTO tableInfoDTO) {
		this.tableInfoDTO = tableInfoDTO;
	}

	public String getBetweenLeft() {
		return betweenLeft;
	}

	public void setBetweenLeft(String betweenLeft) {
		this.betweenLeft = betweenLeft;
	}

	public String getBetweenRight() {
		return betweenRight;
	}

	public void setBetweenRight(String betweenRight) {
		this.betweenRight = betweenRight;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnPattern() {
		return columnPattern;
	}

	public void setColumnPattern(String columnPattern) {
		this.columnPattern = columnPattern;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public String getColumnHeaderPattern() {
		return columnHeaderPattern;
	}

	public void setColumnHeaderPattern(String columnHeaderPattern) {
		this.columnHeaderPattern = columnHeaderPattern;
	}

	public String getColumnStartCoordinate() {
		return columnStartCoordinate;
	}

	public void setColumnStartCoordinate(String columnStartCoordinate) {
		this.columnStartCoordinate = columnStartCoordinate;
	}

	public String getColumnEndCoordinate() {
		return columnEndCoordinate;
	}

	public void setColumnEndCoordinate(String columnEndCoordinate) {
		this.columnEndCoordinate = columnEndCoordinate;
	}

	public String getColumnCoordY0() {
		return columnCoordY0;
	}

	public void setColumnCoordY0(String columnCoordY0) {
		this.columnCoordY0 = columnCoordY0;
	}

	public String getColumnCoordY1() {
		return columnCoordY1;
	}

	public void setColumnCoordY1(String columnCoordY1) {
		this.columnCoordY1 = columnCoordY1;
	}

	public String getAlternateValues() {
		return alternateValues;
	}

	public void setAlternateValues(String alternateValues) {
		this.alternateValues = alternateValues;
	}

	/**
	 * Setter for the validationPattern.
	 * 
	 * @param validationPattern {@link String} The validation pattern.
	 */
	public void setValidationPattern(final String validationPattern) {
		this.validationPattern = validationPattern;
	}

	/**
	 * Getter for the validationPattern.
	 * 
	 * @return validationPattern {@link String} The validation pattern.
	 */
	public String getValidationPattern() {
		return validationPattern;
	}

	/**
	 * @return the extractedDataColumnName
	 */
	public String getExtractedDataColumnName() {
		return extractedDataColumnName;
	}

	/**
	 * @param extractedDataColumnName the extractedDataColumnName to set
	 */
	public void setExtractedDataColumnName(final String extractedDataColumnName) {
		this.extractedDataColumnName = extractedDataColumnName;
	}

	/**
	 * Gets mandatory option set or not.
	 * 
	 * @return boolean
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * Sets mandatory option set or not.
	 * 
	 * @param boolean
	 */
	public void setMandatory(final boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * Gets the <code>orderNumber</code>
	 * 
	 * @return {@link String} current ordering number
	 */

	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * Sets the <code>orderNumber</code>
	 * 
	 * @param orderNumber {@link String}
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the columnDescription
	 */
	public String getColumnDescription() {
		return columnDescription;
	}

	/**
	 * @param columnDescription the columnDescription to set
	 */
	public void setColumnDescription(final String columnDescription) {
		this.columnDescription = columnDescription;
	}

}
