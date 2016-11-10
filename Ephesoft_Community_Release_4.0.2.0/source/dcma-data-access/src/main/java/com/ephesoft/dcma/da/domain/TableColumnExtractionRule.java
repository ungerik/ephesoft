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

package com.ephesoft.dcma.da.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.constant.DataAccessConstant;

/**
 * Entity class for Table column extraction rule used for storing all the table extraction details like column start coordinate, column
 * end coordinate, column pattern etc. defined for a particular table.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 09-Jan-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see AbstractChangeableEntity
 * @see TableExtractionRule
 * @see DataAccessConstant
 */
@Entity
@Table(name = DataAccessConstant.TABLE_COLUMN_EXTRACTION_RULE)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class TableColumnExtractionRule extends AbstractChangeableEntity implements Serializable {

	/**
	 * Represents the generated serial version id.
	 */
	private static final long serialVersionUID = -1104804266069278082L;

	/**
	 * Represents the column for defining the column header pattern for a particular table.
	 */
	@Column(name = DataAccessConstant.COLUMN_HEADER_PATTERN, length = 700)
	private String columnHeaderPattern;

	/**
	 * Represents the column for defining the column start coordinate for a particular table.
	 */
	@Column(name = DataAccessConstant.COLUMN_START_CORDINATE)
	private Integer columnStartCoordinate;

	/**
	 * Represents the column for defining the column end coordinate for a particular table.
	 */
	@Column(name = DataAccessConstant.COLUMN_END_CORDINATE)
	private Integer columnEndCoordinate;

	/**
	 * Represents the column for defining the column y0 coordinate for a particular table.
	 */
	@Column(name = DataAccessConstant.COLUMN_CORDINATE_Y0)
	private Integer columnCoordinateY0;

	/**
	 * Represents the column for defining the column y1 coordinate for a particular table.
	 */
	@Column(name = DataAccessConstant.COLUMN_CORDINATE_Y1)
	private Integer columnCoordinateY1;

	/**
	 * Represents the column for defining the column pattern for a particular table.
	 */
	@Column(name = DataAccessConstant.COLUMN_PATTERN, length = 700)
	private String columnPattern;

	/**
	 * Represents the column for defining the extracted column data name for a particular table.
	 */
	@Column(name = DataAccessConstant.EXTRACTED_COLUMN_NAME)
	private String extractedDataColumnName;

	/**
	 * Represents the column for defining the between left for a particular table.
	 */
	@Column(name = DataAccessConstant.BETWEEN_LEFT, length = 700)
	private String betweenLeft;

	/**
	 * Represents the column for defining the between right for a particular table.
	 */
	@Column(name = DataAccessConstant.BETWEEN_RIGHT, length = 700)
	private String betweenRight;

	/**
	 * Represents the column which defines the column is required or not for a particular table.
	 */
	@Column(name = DataAccessConstant.IS_REQUIRED, columnDefinition = "bit default 0")
	private boolean isRequired;

	/**
	 * Represents the column for defining whether the column is mandatory or not for a particular table.
	 */
	@Column(name = DataAccessConstant.IS_MANDATORY, columnDefinition = "bit default 0")
	private boolean mandatory;

	/**
	 * Represents the column for defining order number for a particular table.
	 */
	@Column(name = DataAccessConstant.ORDER_NUMBER, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderNo;

	/**
	 * Represents the column for defining whether the column is defined for a currency or not for a particular table.
	 */
	@Column(name = DataAccessConstant.IS_CURRENCY, columnDefinition = "bit default 0")
	private boolean isCurrency;

	/**
	 * Represents the column for defining column name for a particular table.
	 */
	@Column(name = DataAccessConstant.COLUMN_NAME, length = 700)
	private String columnName;

	/**
	 * Represents the {@link TableExtractionRule} object defined for a particular table column extraction rule.
	 */
	@ManyToOne
	@JoinColumn(name = DataAccessConstant.TABLE_EXTRACTION_RULE_ID)
	private TableExtractionRule tableExtractionRuleInfo;
	
	@ManyToOne
	@JoinColumn(name = DataAccessConstant.TABLE_COLUMNS_INFO_ID)
	private TableColumnsInfo tableColumnInfo;

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
	public Integer getColumnStartCoordinate() {
		return columnStartCoordinate;
	}

	/**
	 * @param columnStartCoordinate the columnStartCoordinate to set
	 */
	public void setColumnStartCoordinate(final Integer columnStartCoordinate) {
		this.columnStartCoordinate = columnStartCoordinate;
	}

	/**
	 * @return the columnEndCoordinate
	 */
	public Integer getColumnEndCoordinate() {
		return columnEndCoordinate;
	}

	/**
	 * @param columnEndCoordinate the columnEndCoordinate to set
	 */
	public void setColumnEndCoordinate(final Integer columnEndCoordinate) {
		this.columnEndCoordinate = columnEndCoordinate;
	}

	/**
	 * @return the columnCoordinateY0
	 */
	public Integer getColumnCoordinateY0() {
		return columnCoordinateY0;
	}

	/**
	 * @param columnCoordinateY0 the columnCoordinateY0 to set
	 */
	public void setColumnCoordinateY0(final Integer columnCoordinateY0) {
		this.columnCoordinateY0 = columnCoordinateY0;
	}

	/**
	 * @return the columnCoordinateY1
	 */
	public Integer getColumnCoordinateY1() {
		return columnCoordinateY1;
	}

	/**
	 * @param columnCoordinateY1 the columnCoordinateY1 to set
	 */
	public void setColumnCoordinateY1(final Integer columnCoordinateY1) {
		this.columnCoordinateY1 = columnCoordinateY1;
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
	 * @return the isRequired
	 */
	public boolean isRequired() {
		return isRequired;
	}

	/**
	 * @param isRequired the isRequired to set
	 */
	public void setRequired(final boolean isRequired) {
		this.isRequired = isRequired;
	}

	/**
	 * @return the mandatory
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * @param mandatory the mandatory to set
	 */
	public void setMandatory(final boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * @return the orderNo
	 */
	public int getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(final int orderNo) {
		this.orderNo = orderNo;
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
	 * @return the tableExtractionRuleInfo
	 */
	public TableExtractionRule getTableExtractionRuleInfo() {
		return tableExtractionRuleInfo;
	}

	/**
	 * @param tableExtractionRuleInfo the tableExtractionRuleInfo to set
	 */
	public void setTableExtractionRuleInfo(TableExtractionRule tableExtractionRuleInfo) {
		this.tableExtractionRuleInfo = tableExtractionRuleInfo;
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

	
	public TableColumnsInfo getTableColumnInfo() {
		return tableColumnInfo;
	}

	
	public void setTableColumnInfo(TableColumnsInfo tableColumnInfo) {
		this.tableColumnInfo = tableColumnInfo;
	}

	@Override
	public void postPersist() {
		super.postPersist();
	}
}
