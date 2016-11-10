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
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.core.common.TableColumnVO;
import com.ephesoft.dcma.tablefinder.data.DataCarrier;

/**
 * Plain object class as data carrier for fields for column header based columns data extraction.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Apr 8, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class ColumnHeaderExtractionDataCarrier {

	/**
	 * {@link DataCarrier} Data carrier column header information.
	 */
	private DataCarrier colHeaderDataCarrier;

	/**
	 * {@link TableColumnVO} Data carrier column header information.
	 */
	private TableColumnVO tableColumn;

	/**
	 * {@link Column} Data carrier column header information.
	 */
	private Column column;

	/**
	 * {@link List}<{@link Span}> Data carrier column header information.
	 */
	private List<Span> spanList;

	/**
	 * {@link LineDataCarrier} Data carrier column header information.
	 */
	private LineDataCarrier lineDataCarrier;

	/**
	 * Gets column header data carrier.
	 * 
	 * @return {@link DataCarrier}
	 */
	public DataCarrier getColHeaderDataCarrier() {
		return colHeaderDataCarrier;
	}

	/**
	 * Sets column header data carrier.
	 * 
	 * @param colHeaderDataCarrier {@link DataCarrier}
	 */
	public void setColHeaderDataCarrier(DataCarrier colHeaderDataCarrier) {
		this.colHeaderDataCarrier = colHeaderDataCarrier;
	}

	/**
	 * Gets table column information data for extraction.
	 * 
	 * @return {@link TableColumnVO}
	 */
	public TableColumnVO getTableColumn() {
		return tableColumn;
	}

	/**
	 * Sets table column information data for extraction.
	 * 
	 * @param tableColumn {@link TableColumnVO}
	 */
	public void setTableColumn(TableColumnVO tableColumn) {
		this.tableColumn = tableColumn;
	}

	/**
	 * Gets row's column being extracted.
	 * 
	 * @return {@link TableColumnVO}
	 */
	public Column getColumn() {
		return column;
	}

	/**
	 * Sets row's column being extracted.
	 * 
	 * @param column {@link Column}
	 */
	public void setColumn(final Column column) {
		this.column = column;
	}

	/**
	 * Gets span list.
	 * 
	 * @return {@link List}<{@link Span}>
	 */
	public List<Span> getSpanList() {
		return spanList;
	}

	/**
	 * Sets span list.
	 * 
	 * @param spanList {@link List}<{@link Span}>
	 */
	public void setSpanList(final List<Span> spanList) {
		this.spanList = spanList;
	}

	/**
	 * Gets line data carrier.
	 * 
	 * @return {@link TableColumnVO}
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

}
