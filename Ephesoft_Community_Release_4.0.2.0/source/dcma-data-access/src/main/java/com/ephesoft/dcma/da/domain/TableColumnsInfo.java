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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.constant.DataAccessConstant;
import com.ephesoft.dcma.util.CollectionUtil;

@Entity
@Table(name = "table_columns_info")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class TableColumnsInfo extends AbstractChangeableEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "table_info_id")
	private TableInfo tableInfo;

	@Column(name = "column_name")
	private String columnName;

	@Column(name = "column_header_pattern", length = 700)
	private String columnHeaderPattern;

	@Column(name = "column_start_coordinate")
	private Integer columnStartCoordinate;

	@Column(name = "column_end_coordinate")
	private Integer columnEndCoordinate;

	@Column(name = "column_coordinate_y0")
	private Integer columnCoordinateY0;

	@Column(name = "column_coordinate_y1")
	private Integer columnCoordinateY1;

	@Column(name = "column_pattern", length = 700)
	private String columnPattern;

	/**
	 * validationPattern {@link String} specifies validation_pattern column in table_columns_info table.
	 */
	@Column(name = "validation_pattern", length = 700)
	private String validationPattern;

	/**
	 * extractedDataColumnName {@link String} specifies the column name from which data needs to be extracted.
	 */
	@Column(name = "extracted_column_name")
	private String extractedDataColumnName;

	@Column(name = "between_left", length = 700)
	private String betweenLeft;

	@Column(name = "between_right", length = 700)
	private String betweenRight;

	@Column(name = "is_required", columnDefinition = "bit default 0")
	private boolean isRequired;

	/**
	 * True if this column data is mandatory to be present once in a row in table.
	 */
	@Column(name = "is_mandatory", columnDefinition = "bit default 0")
	private boolean mandatory;

	/**
	 * sampleValues {@link String} specifies sample_values column in table_columns_info table.
	 */
	@Column(name = "sample_values")
	@Lob
	private String sampleValues;

	/**
	 * orderNo {@link String} specifies the order_number at the UI
	 */
	@Column(name = "order_number", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderNo;

	/**
	 * Represents whether the column is currency or not.
	 */
	@Column(name = "is_currency", columnDefinition = "bit default 0")
	private boolean isCurrency;

	/**
	 * Represents the column for defining column description for a particular table column.
	 */
	@Column(name = DataAccessConstant.COLUMN_DESCRIPTION)
	private String columnDescription;

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "table_column_id")
	private List<TableColumnDBExportMapping> dbExportMappings = new ArrayList<TableColumnDBExportMapping>();

	public TableInfo getTableInfo() {
		return tableInfo;
	}

	/**
	 * Gets mandatory set or not.
	 * 
	 * @return boolean
	 */
	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * Sets mandatory or not.
	 * 
	 * @param mandatory boolean
	 */
	public void setMandatory(final boolean mandatory) {
		this.mandatory = mandatory;
	}

	/**
	 * To set Table Info.
	 * 
	 * @param tableInfo TableInfo
	 */
	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
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

	public boolean isRequired() {
		return isRequired;
	}

	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	public String getColumnHeaderPattern() {
		return columnHeaderPattern;
	}

	public void setColumnHeaderPattern(String columnHeaderPattern) {
		this.columnHeaderPattern = columnHeaderPattern;
	}

	public Integer getColumnStartCoordinate() {
		return columnStartCoordinate;
	}

	public void setColumnStartCoordinate(Integer columnStartCoordinate) {
		this.columnStartCoordinate = columnStartCoordinate;
	}

	public Integer getColumnEndCoordinate() {
		return columnEndCoordinate;
	}

	public void setColumnEndCoordinate(Integer columnEndCoordinate) {
		this.columnEndCoordinate = columnEndCoordinate;
	}

	public Integer getColumnCoordinateY0() {
		return columnCoordinateY0;
	}

	public void setColumnCoordinateY0(Integer columnCoordinateY0) {
		this.columnCoordinateY0 = columnCoordinateY0;
	}

	public Integer getColumnCoordinateY1() {
		return columnCoordinateY1;
	}

	public void setColumnCoordinateY1(Integer columnCoordinateY1) {
		this.columnCoordinateY1 = columnCoordinateY1;
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
	 * @return {@link String} The validation pattern.
	 */
	public String getValidationPattern() {
		return validationPattern;
	}

	/**
	 * Gets the alternateValues.
	 * 
	 * @return {@link String} The alternate values.
	 */
	public String getAlternateValues() {
		return sampleValues;
	}

	/**
	 * Sets the alternateValues.
	 * 
	 * @param alternateValues {@link String} The alternate values.
	 */
	public void setAlternateValues(String alternateValues) {
		this.sampleValues = alternateValues;
	}

	/**
	 * @param extractedDataColumnName the extractedDataColumnName to set
	 */
	public void setExtractedDataColumnName(final String extractedDataColumnName) {
		this.extractedDataColumnName = extractedDataColumnName;
	}

	/**
	 * @return the extractedDataColumnName
	 */
	public String getExtractedDataColumnName() {
		return extractedDataColumnName;
	}

	/**
	 * Gets the order_number at the UI
	 * 
	 * @return the orderNo int
	 */
	public int getOrderNo() {
		return orderNo;
	}

	/**
	 * Sets the order_number at the UI
	 * 
	 * @param orderNo int the orderNo to set
	 */
	public void setOrderNo(final int orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * @return boolean the isCurrency
	 */
	public boolean isCurrency() {
		return isCurrency;
	}

	/**
	 * @param boolean isCurrency the isCurrency to set
	 */
	public void setCurrency(boolean isCurrency) {
		this.isCurrency = isCurrency;
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

	@Override
	public void postPersist() {
		super.postPersist();
	}

	/**
	 * @return the dbExportMappings
	 */
	public List<TableColumnDBExportMapping> getDbExportMappings() {
		return dbExportMappings;
	}

	public TableColumnDBExportMapping getDBExportMappingById(long id) {
		TableColumnDBExportMapping bindedMapping = null;
		if (!CollectionUtil.isEmpty(dbExportMappings)) {
			for (TableColumnDBExportMapping mapping : dbExportMappings) {
				if (null != mapping && mapping.getId() == id) {
					bindedMapping = mapping;
					break;
				}
			}
		}
		return bindedMapping;
	}

	public void add(TableColumnDBExportMapping mapping) {
		if(null != mapping) {
			dbExportMappings.add(mapping);
		}
	}
	/**
	 * @param dbExportMappings the dbExportMappings to set
	 */
	public void setDbExportMappings(List<TableColumnDBExportMapping> dbExportMappings) {
		this.dbExportMappings = dbExportMappings;
	}

	public void removeDbExportMappingById(long id) {
		if (!CollectionUtil.isEmpty(dbExportMappings)) {
			for (TableColumnDBExportMapping mapping : dbExportMappings) {
				if (null != mapping && mapping.getId() == id) {
					dbExportMappings.remove(mapping);
					break;
				}
			}
		}
	}

}
