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

package com.ephesoft.dcma.gwt.core.client.view;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.gwt.core.client.i18n.CoreCommonConstants;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ScrollPanel;

/**
 * This class gives a table with radio buttons.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 22-Jun-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RadioButtonTable {

	/**
	 * Instance of flow panel with header and table contents.
	 */
	private final FlowPanel flowPanel = new FlowPanel();

	/**
	 * Instance of scroll panel for scroll in table data.
	 */
	private final ScrollPanel scrollPanel = new ScrollPanel();

	/**
	 * Instance of header of the table.
	 */
	private final FlexTable headerTable = new FlexTable();

	/**
	 * Gets header of the table.
	 * 
	 * @return {@link FlexTable}
	 */
	public FlexTable getHeaderTable() {
		return headerTable;
	}

	/**
	 * Gets content of the table.
	 * 
	 * @return {@link FlexTable}
	 */
	public FlexTable getContentTable() {
		return contentTable;
	}

	/**
	 * Instance of table with data.
	 */
	private final FlexTable contentTable = new FlexTable();

	/**
	 * Object id of selected Row in table.
	 */
	private String selectedRowId;

	/**
	 * Label text when table is empty.
	 */
	private static final String MSG_NO_RESULTS_FOUND = "No Results Found.";

	/**
	 * Constant for width in table header for radio button.
	 */
	private static final int HEADER_RADIO_WIDTH = 6;

	/**
	 * Constant for width in table header for row data.
	 */
	private static final int HEADER_DATA_WIDTH = 94;

	/**
	 * Constructor.
	 */
	public RadioButtonTable() {
		flowPanel.add(headerTable);
		scrollPanel.add(contentTable);
		flowPanel.add(scrollPanel);
		setTableStyle(headerTable);
		setTableStyle(contentTable);
	}

	/**
	 * Sets style for table.
	 * 
	 * @param flexTable {@link FlexTable}
	 */
	private void setTableStyle(final FlexTable flexTable) {
		flexTable.setCellSpacing(0);
		flexTable.addStyleName(CoreCommonConstants.PADDING_5_CSS);
		flexTable.addStyleName(CoreCommonConstants.BORDER_TABLE_CSS);
		flexTable.addStyleName(CoreCommonConstants.CUSTOM_GRID_CSS);
	}

	/**
	 * Creates table with header and data rows.
	 * 
	 * @param headerList {@link List<{@link String}>}
	 * @param tableRowsData {@link List<{@link List<{@link String}>}>}
	 */
	public void createList(final List<String> headerList, final List<List<String>> tableRowsData) {
		contentTable.clear();
		contentTable.removeAllRows();
		boolean success = createHeaderColumns(headerList);
		if (success && null != tableRowsData) {
			int rowIndex = 0;
			int rowColumnWidth = HEADER_DATA_WIDTH / headerList.size();
			/* Instance of map of object's id and radio button in table. */
			final Map<RadioButton, String> radioVsIdMap = new HashMap<RadioButton, String>();
			final Map<RadioButton, Integer> radioVsRowIdMap = new HashMap<RadioButton, Integer>();
			for (List<String> rowData : tableRowsData) {
				if (null != rowData) {
					final RadioButton radioButton = new RadioButton(new Date().toString());
					radioButton.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(final ClickEvent clickEvent) {
							int rowIndex = 0;
							for (RadioButton radio : radioVsIdMap.keySet()) {
								radio.setValue(false);
								if (rowIndex % 2 == 0) {
									contentTable.getRowFormatter().setStyleName(rowIndex, CoreCommonConstants.ODD_CSS);
								} else {
									contentTable.getRowFormatter().setStyleName(rowIndex, CoreCommonConstants.EVEN_CSS);
								}
								rowIndex++;
							}
							radioButton.setValue(true);
							selectedRowId = radioVsIdMap.get(radioButton);
							contentTable.getRowFormatter().addStyleName(radioVsRowIdMap.get(radioButton),
									CoreCommonConstants.ROW_HIGHLIGHTED_CSS);
						}
					});
					radioVsIdMap.put(radioButton, rowData.get(0));
					radioVsRowIdMap.put(radioButton, rowIndex);
					contentTable.getCellFormatter().setWidth(rowIndex, 0, String.valueOf(HEADER_RADIO_WIDTH));
					contentTable.setWidget(rowIndex, 0, radioButton);
					int columnIndex = 0;
					for (String columnData : rowData) {
						if (columnIndex > 0) {
							contentTable.getCellFormatter().setWidth(rowIndex, columnIndex, String.valueOf(rowColumnWidth));
							contentTable.setWidget(rowIndex, columnIndex, new Label(columnData));
							contentTable.getCellFormatter().setHorizontalAlignment(rowIndex, columnIndex,
									HasHorizontalAlignment.ALIGN_LEFT);
						}
						columnIndex++;
					}
					if (rowIndex % 2 == 0) {
						contentTable.getRowFormatter().setStyleName(rowIndex, CoreCommonConstants.ODD_CSS);
					} else {
						contentTable.getRowFormatter().setStyleName(rowIndex, CoreCommonConstants.EVEN_CSS);
					}
					if (rowIndex == 0) {
						radioButton.setValue(true);
						selectedRowId = radioVsIdMap.get(radioButton);
						contentTable.getRowFormatter().addStyleName(0, CoreCommonConstants.ROW_HIGHLIGHTED_CSS);
					}
					rowIndex++;
				}
			}
		} else {
			contentTable.getFlexCellFormatter().setColSpan(1, 0, headerList.size() + 1);
			contentTable.setWidget(1, 0, new Label(MSG_NO_RESULTS_FOUND));
		}
	}

	/**
	 * Creates table header row.
	 * 
	 * @param headerList {@link List<{@link String}>}
	 * @return boolean is true if header row is added to table.
	 */
	private boolean createHeaderColumns(final List<String> headerList) {
		boolean success = true;
		if (headerList == null || headerList.isEmpty() || headerTable == null) {
			success = false;
		} else {
			int headerColumnWidth = HEADER_DATA_WIDTH / headerList.size();
			headerTable.getCellFormatter().setWidth(0, 0, String.valueOf(HEADER_RADIO_WIDTH));
			headerTable.setWidget(0, 0, new Label(CoreCommonConstants.EMPTY));
			int index = 1;
			for (String headerName : headerList) {
				headerTable.getCellFormatter().setWidth(0, index, String.valueOf(headerColumnWidth));
				headerTable.setWidget(0, index, new Label(headerName));
				index++;
			}
			headerTable.getRowFormatter().setStyleName(0, CoreCommonConstants.CUSTOM_GRID_HEADER_CSS);
		}
		return success;
	}

	/**
	 * Gets the Object id of selected Row in table.
	 * 
	 * @return {@link String}
	 */
	public String getSelectedRowId() {
		return selectedRowId;
	}

	/**
	 * Gets table's flow panel.
	 * 
	 * @return {@link FlowPanel}
	 */
	public FlowPanel getFlowPanel() {
		return flowPanel;
	}

	/**
	 * Gets scrollPanel of table content.
	 * 
	 * @return {@link ScrollPanel}
	 */
	public ScrollPanel getScrollPanel() {
		return scrollPanel;
	}

}
