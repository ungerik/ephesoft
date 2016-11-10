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

package com.ephesoft.dcma.core.common;

import java.io.Serializable;

/**
 * This class represents a virtual object that contains the combination of column information and column extraction rule information
 * that may be used by the algorithm of table extraction from pages of HOCR data.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Feb 11, 2014 <br/>
 * @version $LastChangedDate:$<br/>
 *          $LastChangedRevision:$<br/>
 */
public class TableColumnVO implements Serializable {

	/**
	 * Serial version UID of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Unique name of a column.
	 */
	private String columnName;

	/**
	 * Between left regex pattern.
	 */
	private String betweenLeft;

	/**
	 * Between right regex pattern.
	 */
	private String betweenRight;

	/**
	 * Column regex pattern.
	 */
	private String columnPattern;

	/**
	 * Tells if extracted row of table must contain valid value for this column.
	 */
	private boolean required;

	/**
	 * True if this column data is mandatory to be present once in a row in table.
	 */
	private boolean multilineAnchor;

	/**
	 * Column haeder regex pattern.
	 */
	private String columnHeaderPattern;

	/**
	 * Column start coordinate.
	 */
	private String columnStartCoordinate;

	/**
	 * Column end coordinate.
	 */
	private String columnEndCoordinate;

	/**
	 * Pattern taht validates extracted column value.
	 */
	private String validationPattern;

	/**
	 * Boolean that determines whether a column field is currency or not.
	 */
	private boolean isCurrency;

	/**
	 * {@link String} specifies the column name from which data needs to be extracted.
	 */
	private String extractedDataColumnName;

	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}

	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
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
	public void setBetweenLeft(String betweenLeft) {
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
	public void setBetweenRight(String betweenRight) {
		this.betweenRight = betweenRight;
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
	public void setColumnPattern(String columnPattern) {
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
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * @return true if this column is an multiline anchor
	 */
	public boolean isMultilineAnchor() {
		return multilineAnchor;
	}

	/**
	 * @param multilineAnchor boolean true/false to denote if this column is a multiline anchor.
	 */
	public void setMultilineAnchor(boolean multilineAnchor) {
		this.multilineAnchor = multilineAnchor;
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
	public void setColumnHeaderPattern(String columnHeaderPattern) {
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
	public void setColumnStartCoordinate(String columnStartCoordinate) {
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
	public void setColumnEndCoordinate(String columnEndCoordinate) {
		this.columnEndCoordinate = columnEndCoordinate;
	}

	/**
	 * @return the validationPattern
	 */
	public String getValidationPattern() {
		return validationPattern;
	}

	/**
	 * @param validationPattern the validationPattern to set
	 */
	public void setValidationPattern(String validationPattern) {
		this.validationPattern = validationPattern;
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
	public void setCurrency(boolean isCurrency) {
		this.isCurrency = isCurrency;
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
	public void setExtractedDataColumnName(String extractedDataColumnName) {
		this.extractedDataColumnName = extractedDataColumnName;
	}

	@Override
	public boolean equals(final Object object) {
		boolean isEqual;
		if (null != object && (object instanceof TableColumnVO)) {
			final TableColumnVO tableColumnVO = (TableColumnVO) object;
			if (null != tableColumnVO && null != columnName && columnName.equalsIgnoreCase(tableColumnVO.getColumnName())) {
				isEqual = true;
			} else {
				isEqual = false;
			}
		} else {
			isEqual = false;
		}
		return isEqual;
	}

}
