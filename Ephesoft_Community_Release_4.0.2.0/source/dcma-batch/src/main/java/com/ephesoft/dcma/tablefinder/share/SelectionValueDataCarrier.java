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

package com.ephesoft.dcma.tablefinder.share;

import java.util.List;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Field;

/**
 * This class is a data carrier for column information for value selection from multiple valid values.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 21-Jan-2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class SelectionValueDataCarrier {

	/**
	 * {@link List}<{@link Column}> List of columns of a row of table data.
	 */
	private List<Column> columnRowList;

	/**
	 * Index of the current table column.
	 */
	private int indexOfTableColumn;

	/**
	 * Table column start coordinate.
	 */
	private int columnHeadStartCoordinate;

	/**
	 * Table column end coordinate.
	 */
	private int columnHeadEndCoordinate;

	/**
	 * {@link Column} column of the row of table carrying extracted data.
	 */
	private Column column;

	/**
	 * {@link List}<{@link Field}> List of fields which are valid candidates for column data selection.
	 */
	private List<Field> selectionColumnOptionsList;

	/**
	 * Gets columnRowList.
	 * 
	 * @return {@link List}<{@link Column}>
	 */
	public List<Column> getColumnRowList() {
		return columnRowList;
	}

	/**
	 * Sets columnRowList.
	 * 
	 * @param columnRowList {@link List}<{@link Column}>
	 */
	public void setColumnRowList(final List<Column> columnRowList) {
		this.columnRowList = columnRowList;
	}

	/**
	 * Gets index of the table column in the list of columns for a row of table data.
	 * 
	 * @return int
	 */
	public int getIndexOfTableColumn() {
		return indexOfTableColumn;
	}

	/**
	 * Sets index of the table column in the list of columns for a row of table data.
	 * 
	 * @param indexOfTableColumn int
	 */
	public void setIndexOfTableColumn(final int indexOfTableColumn) {
		this.indexOfTableColumn = indexOfTableColumn;
	}

	/**
	 * Gets column header/coordinate start.
	 * 
	 * @return int
	 */
	public int getColumnHeadStartCoordinate() {
		return columnHeadStartCoordinate;
	}

	/**
	 * Sets column header/coordinate start.
	 * 
	 * @param columnHeadStartCoordinate int
	 */
	public void setColumnHeadStartCoordinate(final int columnHeadStartCoordinate) {
		this.columnHeadStartCoordinate = columnHeadStartCoordinate;
	}

	/**
	 * Gets column header/coordinate end.
	 * 
	 * @return int
	 */
	public int getColumnHeadEndCoordinate() {
		return columnHeadEndCoordinate;
	}

	/**
	 * Sets column header/coordinate end.
	 * 
	 * @param columnHeadEndCoordinate int
	 */
	public void setColumnHeadEndCoordinate(final int columnHeadEndCoordinate) {
		this.columnHeadEndCoordinate = columnHeadEndCoordinate;
	}

	/**
	 * Gets column.
	 * 
	 * @return {@link Column}
	 */
	public Column getColumn() {
		return column;
	}

	/**
	 * Sets column.
	 * 
	 * @param column {@link Column}
	 */
	public void setColumn(final Column column) {
		this.column = column;
	}

	/**
	 * Gets selectionColumnOptionsList.
	 * 
	 * @return {@link List}<{@link Field}>
	 */
	public List<Field> getSelectionColumnOptionsList() {
		return selectionColumnOptionsList;
	}

	/**
	 * Sets selectionColumnOptionsList.
	 * 
	 * @param selectionColumnOptionsList {@link List}<{@link Field}>
	 */
	public void setSelectionColumnOptionsList(final List<Field> selectionColumnOptionsList) {
		this.selectionColumnOptionsList = selectionColumnOptionsList;
	}
}
