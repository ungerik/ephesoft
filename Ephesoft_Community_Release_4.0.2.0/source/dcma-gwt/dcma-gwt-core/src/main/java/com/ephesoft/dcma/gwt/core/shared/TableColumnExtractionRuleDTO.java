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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * 
 * Defines parameters used to extract column parameters in a table.
 * 
 * @author Ephesoft
 * 
 *         created on Feb 11, 2014 <br/>
 * @version $LastChangedDate:$<br/>
 *          $LastChangedRevision:$<br/>
 */

public class TableColumnExtractionRuleDTO implements IsSerializable {

	/**
	 * {@link String} Regex parameter which would occur in the LHS of the column.
	 */
	private String betweenLeft;

	/**
	 * {@link String} Regex parameter which would occur in the RHS of the column.
	 */
	private String betweenRight;

	/**
	 * {@link String} Identity of the column extraction DTO
	 */
	private String identifier;

	/**
	 * Regex pattern {@link String} of the column value.
	 */
	private String columnPattern;

	/**
	 * boolean that defines that the column in required to be mandatory or not.
	 */
	private boolean required;

	/**
	 * boolean that defines the search needs to be done on multiple lines.
	 */
	private boolean multilineAnchor;

	/**
	 * boolean that defines the DTO is new i.e. it has been saved to DB or not.
	 */
	private boolean isNew;

	/**
	 * boolean that decides that DTO is deleted.
	 */
	private boolean isDeleted;

	/**
	 * {@link String} Regex Pattern for the column header
	 */
	private String columnHeaderPattern;

	/**
	 * {@link String} start coordinates for the column
	 */
	private String columnStartCoordinate;

	/**
	 * {@link String} end coordinates for the column
	 */
	private String columnEndCoordinate;

	/**
	 * {@link String} starting Y coordinate
	 */
	private String columnCoordY0;

	/**
	 * {@link String} starting X coordinate
	 */
	private String columnCoordY1;

	/**
	 * {@link String} Name of the column from which the extracted data can be imported.
	 */
	private String extractedDataColumnName;

	/**
	 * Order number for the DTO
	 */
	private String orderNumber;

	/**
	 * boolean if the column is an currency field
	 */
	private boolean isCurrency;

	/**
	 * Reference {@link TableExtractionRuleDTO} to the table extraction rules.
	 */
	private TableExtractionRuleDTO tableExtractionRuleDTO;

	/**
	 * {@link String} name of the column.
	 */
	private String columnName;

	/**
	 * @return the betweenLeft
	 */
	public String getBetweenLeft() {
		return betweenLeft;
	}

	/**
	 * @param betweenLeft the betweenLeft to set
	 */
	public void setBetweenLeft(final String betweenLeft) {
		this.betweenLeft = betweenLeft;
	}

	/**
	 * @return the betweenRight
	 */
	public String getBetweenRight() {
		return betweenRight;
	}

	/**
	 * @param betweenRight the betweenRight to set
	 */
	public void setBetweenRight(final String betweenRight) {
		this.betweenRight = betweenRight;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the columnPattern
	 */
	public String getColumnPattern() {
		return columnPattern;
	}

	/**
	 * @param columnPattern the columnPattern to set
	 */
	public void setColumnPattern(final String columnPattern) {
		this.columnPattern = columnPattern;
	}

	/**
	 * @return the required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(final boolean required) {
		this.required = required;
	}

	/**
	 * @return the mandatory
	 */
	public boolean isMandatory() {
		return multilineAnchor;
	}

	/**
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(final boolean mandatory) {
		this.multilineAnchor = mandatory;
	}

	/**
	 * @return the isNew
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * @param isNew the isNew to set
	 */
	public void setNew(final boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * @return the isDeleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(final boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the columnHeaderPattern
	 */
	public String getColumnHeaderPattern() {
		return columnHeaderPattern;
	}

	/**
	 * @param columnHeaderPattern the columnHeaderPattern to set
	 */
	public void setColumnHeaderPattern(final String columnHeaderPattern) {
		this.columnHeaderPattern = columnHeaderPattern;
	}

	/**
	 * @return the columnStartCoordinate
	 */
	public String getColumnStartCoordinate() {
		return columnStartCoordinate;
	}

	/**
	 * @param columnStartCoordinate the columnStartCoordinate to set
	 */
	public void setColumnStartCoordinate(final String columnStartCoordinate) {
		this.columnStartCoordinate = columnStartCoordinate;
	}

	/**
	 * @return the columnEndCoordinate
	 */
	public String getColumnEndCoordinate() {
		return columnEndCoordinate;
	}

	/**
	 * @param columnEndCoordinate the columnEndCoordinate to set
	 */
	public void setColumnEndCoordinate(final String columnEndCoordinate) {
		this.columnEndCoordinate = columnEndCoordinate;
	}

	/**
	 * @return the columnCoordY0
	 */
	public String getColumnCoordY0() {
		return columnCoordY0;
	}

	/**
	 * @param columnCoordY0 the columnCoordY0 to set
	 */
	public void setColumnCoordY0(final String columnCoordY0) {
		this.columnCoordY0 = columnCoordY0;
	}

	/**
	 * @return the columnCoordY1
	 */
	public String getColumnCoordY1() {
		return columnCoordY1;
	}

	/**
	 * @param columnCoordY1 the columnCoordY1 to set
	 */
	public void setColumnCoordY1(final String columnCoordY1) {
		this.columnCoordY1 = columnCoordY1;
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
	 * @return the orderNumber
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber the orderNumber to set
	 */
	public void setOrderNumber(final String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * @return the isCurrency
	 */
	public boolean isCurrency() {
		return isCurrency;
	}

	/**
	 * @param isCurrency the isCurrency to set
	 */
	public void setCurrency(final boolean isCurrency) {
		this.isCurrency = isCurrency;
	}

	/**
	 * @return the tableExtractionRuleDTO
	 */
	public TableExtractionRuleDTO getTableExtractionRuleDTO() {
		return tableExtractionRuleDTO;
	}

	/**
	 * @param tableExtractionRuleDTO the tableExtractionRuleDTO to set
	 */
	public void setTableExtractionRuleDTO(final TableExtractionRuleDTO tableExtractionRuleDTO) {
		this.tableExtractionRuleDTO = tableExtractionRuleDTO;
	}

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(final String columnName) {
		this.columnName = columnName;
	}

}
