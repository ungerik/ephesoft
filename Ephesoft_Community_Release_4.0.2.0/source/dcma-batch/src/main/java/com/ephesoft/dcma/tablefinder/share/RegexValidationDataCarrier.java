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
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.core.common.TableColumnVO;

/**
 * This class is the data carrier for the fields needed for regex based column extraction.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Apr 8, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RegexValidationDataCarrier {

	/**
	 * {@link LineDataCarrier} Line data carrier of the page lines.
	 */
	private LineDataCarrier lineDataCarrier;

	/**
	 * {@link TableColumnVO} table column information carrier for extraction.
	 */
	private TableColumnVO tableColumn;

	/**
	 * {@link Column} column of a row being extracted.
	 */
	private Column column;

	/**
	 * {@link Integer} index of the table column in the list of table columns.
	 */
	private Integer indexOfTableColumn;

	/**
	 * {@link List}<{@link Span}> list of spans the line.
	 */
	private List<Span> spanList;

	/**
	 * {@link String} row data of the line.
	 */
	private String rowData;

	/**
	 * {@link String} Id of the page.
	 */
	private String pageID;

	/**
	 * {@link Coordinates} Coordiantes of the column.
	 */
	private Coordinates columnCoordinates;

	/**
	 * Gets line data carrier.
	 * 
	 * @return {@link LineDataCarrier}
	 */
	public LineDataCarrier getLineDataCarrier() {
		return lineDataCarrier;
	}

	/**
	 * Sets line data carrier.
	 * 
	 * @param lineDataCarrier {@link LineDataCarrier}
	 */
	public void setLineDataCarrier(final LineDataCarrier lineDataCarrier) {
		this.lineDataCarrier = lineDataCarrier;
	}

	/**
	 * Gets table column information object.
	 * 
	 * @return {@link TableColumnVO}
	 */
	public TableColumnVO getTableColumn() {
		return tableColumn;
	}

	/**
	 * Sets table column information object.
	 * 
	 * @param tableColumn {@link TableColumnVO}
	 */
	public void setTableColumn(final TableColumnVO tableColumn) {
		this.tableColumn = tableColumn;
	}

	/**
	 * Gets the table column.
	 * 
	 * @return {@link Column}
	 */
	public Column getColumn() {
		return column;
	}

	/**
	 * Sets the table column.
	 * 
	 * @param column {@link Column}
	 */
	public void setColumn(final Column column) {
		this.column = column;
	}

	/**
	 * Gets the index of the current table column in the list of table columns.
	 * 
	 * @return {@link Integer}
	 */
	public Integer getIndexOfTableColumn() {
		return indexOfTableColumn;
	}

	/**
	 * Sets the index of the current table column in the list of table columns.
	 * 
	 * @param indexOfTableColumn {@link Integer}
	 */
	public void setIndexOfTableColumn(final Integer indexOfTableColumn) {
		this.indexOfTableColumn = indexOfTableColumn;
	}

	/**
	 * Gets the spans list.
	 * 
	 * @return {@link List}<{@link Span}>
	 */
	public List<Span> getSpanList() {
		return spanList;
	}

	/**
	 * Sets the spans list.
	 * 
	 * @param spanList {@link List}<{@link Span}>
	 */
	public void setSpanList(final List<Span> spanList) {
		this.spanList = spanList;
	}

	/**
	 * Gets row data.
	 * 
	 * @return {@link String}
	 */
	public String getRowData() {
		return rowData;
	}

	/**
	 * Sets row data.
	 * 
	 * @param rowData {@link String}
	 */
	public void setRowData(final String rowData) {
		this.rowData = rowData;
	}

	/**
	 * Gets page id.
	 * 
	 * @return {@link String}
	 */
	public String getPageID() {
		return pageID;
	}

	/**
	 * Sets page id.
	 * 
	 * @param pageID {@link String}
	 */
	public void setPageID(final String pageID) {
		this.pageID = pageID;
	}

	/**
	 * Gets column coordinates.
	 * 
	 * @return {@link Coordinates}
	 */
	public Coordinates getColumnCoordinates() {
		return columnCoordinates;
	}

	/**
	 * Sets column coordinates.
	 * 
	 * @param columnCoordinates {@link Coordinates}
	 */
	public void setColumnCoordinates(final Coordinates columnCoordinates) {
		this.columnCoordinates = columnCoordinates;
	}
}
