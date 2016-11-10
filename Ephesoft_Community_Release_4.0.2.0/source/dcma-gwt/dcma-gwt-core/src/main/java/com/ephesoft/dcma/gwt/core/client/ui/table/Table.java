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

package com.ephesoft.dcma.gwt.core.client.ui.table;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.gwt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.DoubleClickListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.OrderingListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.PaginationListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.ListView.RowSelectionListner;
import com.ephesoft.dcma.gwt.core.client.ui.table.TableHeader.HeaderColumn;
import com.ephesoft.dcma.gwt.core.client.util.WindowUtil;
import com.ephesoft.dcma.gwt.core.shared.StringUtil;
import com.ephesoft.dcma.gwt.core.shared.util.CollectionUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasDoubleClickHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

public class Table extends ResizeComposite implements HasDoubleClickHandlers {

	interface Binder extends UiBinder<Widget, Table> {
	}

	public interface Images extends ClientBundle {

		ImageResource sortUp();
		ImageResource sortDown();
	}

	private static final Binder BINDER = GWT.create(Binder.class);
	public static int VISIBLE_RECORD_COUNT = 5;

	/**
	 * The CUSTOM_GRID_CSS {@link String} is a constant for table widget CSS named 'customGrid'.
	 */
	private static final String CUSTOM_GRID_CSS = "customGrid";

	/**
	 * The HEADER_CSS {@link String} is a constant for table header widget CSS named 'header'.
	 */
	private static final String HEADER_CSS = "customGridHeader";

	/**
	 * The ODD_CSS {@link String} is a constant for table widget odd row CSS named 'odd'.
	 */
	private static final String ODD_CSS = "odd";

	/**
	 * The EVEN_CSS {@link String} is a constant for table widget even row CSS named 'even'.
	 */
	private static final String EVEN_CSS = "even";

	/**
	 * The ROW_HIGHLIGHTED_CSS {@link String} is a constant for highlighted row widget CSS named 'rowHighlighted'.
	 */
	private static final String ROW_HIGHLIGHTED_CSS = "rowHighlighted";

	/**
	 * The GRID_HEADER_ALIGN_CLASS {@link String} is a constant for header row text alignment CSS named 'gridHeaderAlignClass'.
	 */
	private static final String GRID_HEADER_ALIGN_CLASS = "gridHeaderAlignClass";

	@UiField
	protected DockLayoutPanel outerPanel;
	
	@UiField
	FocusPanel focusPanel;

	@UiField
	FlexTable headerTable;

	@UiField
	FlexTable navBarTable;

	@UiField
	ScrollPanel scrollPanel;

	@UiField
	FlexTable table;

	private TableData tableData;

	private int totalCount;

	private NavBar navBar;

	/**
	 * requireRadioButton SelectionHandlers.
	 */
	private SelectionHandlers requireHandler;

	private boolean isOrderedEntity;

	private boolean fireEventForFirstRow;

	private String selectedRowId;

	private RowSelectionListner listner;

	private Order order;

	boolean mouseOn;

	private int selectedIndex;

	private static final int FIRST_COLUMN_INDEX = 1;

	/**
	 * checkBoxClick boolean.
	 */
	private boolean checkBoxClick;

	/**
	 * radioBtnTable boolean.
	 */
	private boolean radioBtnTable;

	/**
	 * no_selection_handler boolean.
	 */
	private boolean noSelectionHandler;

	/**
	 * selectionHandler Map<Integer, SelectionHandlerContainer>.
	 */
	private Map<Integer, SelectionHandlerContainer> selectionHandlerMap = new HashMap<Integer, SelectionHandlerContainer>();

	/**
	 * selectedRecordList List<String>.
	 */
	List<String> selectedRecordList = new LinkedList<String>();

	/**
	 * Constructor.
	 * 
	 * @param totalCount int
	 * @param header TableHeader
	 * @param requireRadioButton SelectionHandlers
	 * @param fireEventForFirstRow boolean
	 * @param doubleClickListner DoubleClickListner
	 */
	public Table(final int totalCount, final TableHeader header, final SelectionHandlers requireHandler,
			final boolean fireEventForFirstRow, final DoubleClickListner doubleClickListner) {
		initWidget(BINDER.createAndBindUi(this));
		this.totalCount = totalCount;
		this.fireEventForFirstRow = fireEventForFirstRow;
		tableData = new TableData();
		tableData.setHeader(header);
		this.requireHandler = requireHandler;
		navBar = new NavBar(this);
		mouseOn = false;
		table.addStyleName(CUSTOM_GRID_CSS);
		if (doubleClickListner != null && totalCount != 0) {
			focusPanel.addMouseOutHandler(new MouseOutHandler() {

				@Override
				public void onMouseOut(final MouseOutEvent arg0) {
					mouseOn = false;
				}
			});

			focusPanel.addMouseOverHandler(new MouseOverHandler() {

				@Override
				public void onMouseOver(final MouseOverEvent arg0) {
					mouseOn = true;
				}
			});

			addDoubleClickHandler(new DoubleClickHandler() {

				@Override
				public void onDoubleClick(final DoubleClickEvent arg0) {
					if (mouseOn) {
						doubleClickListner.onDoubleClickTable();
					}
				}
			});
		}
	}

	public Table(final int totalCount, final TableHeader header, final SelectionHandlers requireHandler,
			final boolean fireEventForFirstRow) {
		this(totalCount, header, requireHandler, fireEventForFirstRow, null);
	}

	public void setPaginationListner(final PaginationListner paginationListner) {
		this.navBar.setPaginationListner(paginationListner);
	}

	public void setOrderingListner(final OrderingListner orderingListner) {
		this.navBar.setOrderingListner(orderingListner);
	}

	public void setRowSelectionListener(final RowSelectionListner rowSelectionListner) {
		this.listner = rowSelectionListner;
	}

	@Override
	protected void onLoad() {
	}

	public void pushData(final List<Record> recordList, final int startIndex) {
		this.tableData.setRecordList(recordList);
		update(requireHandler, startIndex);
	}

	public void pushData(final List<Record> recordList, final int startIndex, final int count) {
		this.tableData.setRecordList(recordList);
		this.totalCount = count;
		update(requireHandler, startIndex);
	}

	public void pushData(final List<Record> recordList, final int startIndex, final int count, final int selectedIndex) {
		this.tableData.setRecordList(recordList);
		this.totalCount = count;
		update(requireHandler, startIndex, selectedIndex);
	}

	/**
	 * Push the data in the table.
	 * 
	 * @param recordList {@link List}<{@link Record}>.The list of records.
	 * @param startIndex int. The start index.
	 * @param count int. The total count of record list.
	 * @param selectedIndex int. The selcted index.
	 * @param isSelectAllCheckBoxes boolean.
	 */
	public void pushData(final List<Record> recordList, final int startIndex, final int count, final boolean isSelectAllCheckBoxes) {
		this.tableData.setRecordList(recordList);
		this.totalCount = count;
		update(requireHandler, startIndex, isSelectAllCheckBoxes);
	}

	/**
	 * To create table headers.
	 * 
	 * @param requireHandler SelectionHandlers
	 */
	private void createTableHeader(final SelectionHandlers requireHandler, final int startIndex, final int selectedIndexlocal,
			final boolean isSelectAllCheckBoxes) {
		Images images = GWT.create(Images.class);
		final TableHeader header = tableData.getHeader();
		final LinkedList<HeaderColumn> columns = header.getHeaderColumns(requireHandler);
		String width = null;
		int j = 0;
		CheckBox selectAllCheckBox = null;
		for (final HeaderColumn column : columns) {
			width = String.valueOf(column.getWidth()) + "%";
			headerTable.getCellFormatter().setWidth(0, j, width);
			headerTable.getCellFormatter().addStyleName(0, j, "wordWrap");

			// Alignment Issue: JIRA 10917
			headerTable.addStyleName(CoreCommonConstants.TABLE_LAYOUT_FIXED);
			String currentURL = WindowUtil.getCurrentURL();
			if (!StringUtil.isNullOrEmpty(currentURL) && currentURL.contains(CoreCommonConstants.BATCH_CLASS_MANAGEMENT_HTML))  {
				headerTable.addStyleName(CoreCommonConstants.BATCH_CLASS_HEADER);
			}
			
			HorizontalPanel headerPanel = new HorizontalPanel();
			headerPanel.addStyleName(GRID_HEADER_ALIGN_CLASS);
			Label name = new Label(column.name);
			headerPanel.add(name);
			if (requireHandler == SelectionHandlers.CHECKBOX && j == 0) {
				selectAllCheckBox = new CheckBox();
			}
			final Label sortImage = new Label();
			sortImage.setStyleName("alignMiddle");
			if (order != null && column.getDomainProperty() != null
					&& order.getSortProperty().getProperty().equals(column.getDomainProperty().getProperty())) {
				if (column.isPrimaryAsc()) {
					DOM.setInnerHTML(sortImage.getElement(), AbstractImagePrototype.create(images.sortUp()).getHTML());
				} else {
					DOM.setInnerHTML(sortImage.getElement(), AbstractImagePrototype.create(images.sortDown()).getHTML());
				}
			}
			headerPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			headerPanel.add(sortImage);

			if (j == 0 && requireHandler != SelectionHandlers.NO_HANDLER) {
				name.setText("");

			}

			// Fixed for JIRA EPHESOFT-10076 - Making checkbox at correct position.
			if (requireHandler == SelectionHandlers.CHECKBOX && j == 0) {
				headerTable.setWidget(0, j, selectAllCheckBox);
			} else {
				headerTable.setWidget(0, j, headerPanel);
			}
			if (column.isSortable()) {
				name.addStyleName("cursorHand");
				name.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(final ClickEvent arg0) {
						order = new Order(column.getDomainProperty(), !column.isPrimaryAsc());
						navBar.setOrder(order);
						column.setPrimaryAsc(!column.isPrimaryAsc());
						navBar.getListner().onPagination(navBar.getStartIndex(), VISIBLE_RECORD_COUNT, order);
					}
				});
			}
			
			// Fixed for JIRA EPHESOFT-10076 - Making checkbox at correct position.
			if (requireHandler == SelectionHandlers.CHECKBOX && j == 0) {
				headerTable.getFlexCellFormatter().setVerticalAlignment(0, j, HasVerticalAlignment.ALIGN_MIDDLE);
			} else {
				headerTable.getFlexCellFormatter().setVerticalAlignment(0, j, HasVerticalAlignment.ALIGN_TOP);
			}
			headerTable.getCellFormatter().setHorizontalAlignment(0, j, HasHorizontalAlignment.ALIGN_LEFT);
			j++;
		}
		headerTable.getRowFormatter().setStyleName(0, HEADER_CSS);

		if (selectAllCheckBox != null) {
			selectAllCheckBox.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent clickEvent) {
					selectedRecordList = new LinkedList<String>();
					updateRecordsList(startIndex, -1, ((CheckBox) clickEvent.getSource()).getValue());
				}
			});
		}
	}

	private native void scrollIntoView(final Element element) /*-{
																element.scrollIntoView(true);
																}-*/;

	private void update(final SelectionHandlers requireHandler, final int startIndex, final int selectedIndexlocal) {
		processUpgradation(requireHandler, startIndex, selectedIndexlocal, false);

	}

	private void processUpgradation(final SelectionHandlers requireHandler, final int startIndex, final int selectedIndexlocal,
			final boolean isSelectAllCheckBoxes) {
		createTableHeader(requireHandler, startIndex, selectedIndexlocal, isSelectAllCheckBoxes);
		updateRecordsList(startIndex, selectedIndexlocal, isSelectAllCheckBoxes);
	}

	private void updateRecordsList(final int startIndex, final int selectedIndexlocal, final boolean isSelectAllCheckBoxes) {
		selectedRowId = null;
		table.removeAllRows();
		navBarTable.removeAllRows();

		int count = totalCount;
		int max = startIndex + VISIBLE_RECORD_COUNT;
		if (max > count) {
			max = count;
		}
		navBar.update(startIndex, count, max);
		setNavigationBar();

		TableHeader header = tableData.getHeader();
		HeaderColumn[] columns = header.getHeaderColumns();
		String width = null;
		int row = 0;

		final List<Record> recordList = tableData.getRecordList();

		if (recordList != null) {
			if (!recordList.isEmpty()) {
				for (final Record record : recordList) {
					for (int col = 0; col < columns.length; col++) {
						width = String.valueOf(columns[col].getWidth()) + "%";
						table.getCellFormatter().setWidth(row, col, width);
						table.setWidget(row, col, record.getWidget(columns[col]));
						table.getCellFormatter().setHorizontalAlignment(row, col, HasHorizontalAlignment.ALIGN_LEFT);
						table.getCellFormatter().setWordWrap(row, col, true);
						table.getCellFormatter().addStyleName(row, col, "wordWrap");
					}
					if (requireHandler == SelectionHandlers.RADIOBUTTONS) {
						addRadioBtnTable(row, selectedIndexlocal, record, recordList);

					} else if (requireHandler == SelectionHandlers.CHECKBOX) {
						addCheckBoxTable(row, -1, record, recordList, isSelectAllCheckBoxes);

					} else {
						noSelectionHandler = true;
						SelectionHandlerContainer selectionHandlerContainer = new SelectionHandlerContainer(null, record
								.getIdentifier());
						selectionHandlerMap.put(new Integer(row), selectionHandlerContainer);

					}

					if (row % 2 == 0) {
						table.getRowFormatter().setStyleName(row, ODD_CSS);
					} else {
						table.getRowFormatter().setStyleName(row, EVEN_CSS);
					}
					if (isSelectAllCheckBoxes) {
						table.getRowFormatter().addStyleName(row, ROW_HIGHLIGHTED_CSS);
					}
					row++;
				}
				if (requireHandler == SelectionHandlers.RADIOBUTTONS) {
					table.getRowFormatter().addStyleName(selectedIndexlocal, ROW_HIGHLIGHTED_CSS);
				}
			} else {
				Label label = new Label();
				label.setWidth("100%");
				label.setText(LocaleDictionary.get().getMessageValue(LocaleCommonConstants.NO_RECORD_FOUND));
				table.getCellFormatter().setWidth(1, 0, "100%");
				table.getFlexCellFormatter().setColSpan(1, 0, 9);
				table.setWidget(1, 0, label);
			}

		}
	}

	/**
	 * Updates the table.
	 * 
	 * @param requireHandler {@link SelectionHandlers}.
	 * @param startIndex int. The start index.
	 * @param selectedIndexlocal int. The selcted index.
	 * @param isSelectAllCheckBoxes boolean.
	 */
	private void update(final SelectionHandlers requireHandler, final int startIndex, final boolean isSelectAllCheckBoxes) {
		processUpgradation(requireHandler, startIndex, 0, isSelectAllCheckBoxes);
	}

	/**
	 * To add a radio button table.
	 * 
	 * @param index int
	 * @param selectedIndexlocal int
	 * @param record Record
	 * @param recordList List<Record>
	 */
	private void addRadioBtnTable(final int index, final int selectedIndexlocal, final Record record, final List<Record> recordList) {

		String radioName = String.valueOf(new Date().getTime());
		radioBtnTable = true;
		final RadioButton radioButton = new RadioButton(radioName);

		if (index == selectedIndexlocal) {
			radioButton.setValue(true);
			selectedRowId = record.getIdentifier();
			selectedIndex = index;
			if (null != listner && fireEventForFirstRow) {
				listner.onRowSelected(selectedRowId);
			}

			scrollIntoView(table.getWidget(selectedIndexlocal, FIRST_COLUMN_INDEX).getElement());
		}

		radioButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent arg0) {
				clearRadioButtons();
				radioButton.setValue(true);
				selectedRowId = record.getIdentifier();
				selectedIndex = recordList.indexOf(record);
			}
		});

		table.setWidget(index, 0, radioButton);
		SelectionHandlerContainer selectionHandlerContainer = new SelectionHandlerContainer(radioButton, record.getIdentifier());
		selectionHandlerMap.put(new Integer(index), selectionHandlerContainer);
		table.getCellFormatter().setHorizontalAlignment(index, 0, HasHorizontalAlignment.ALIGN_CENTER);

		radioButton.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(final FocusEvent arg0) {
				removeSelectedRowStyleFromTable();
				for (Integer rowId : selectionHandlerMap.keySet()) {
					if (selectionHandlerMap.get(rowId).getRadioButton().equals(radioButton)) {
						selectedIndex = recordList.indexOf(record);
						table.getRowFormatter().addStyleName(rowId, ROW_HIGHLIGHTED_CSS);
					}
				}
			}
		});

	}

	/**
	 * To add a checkbox table.
	 * 
	 * @param index int
	 * @param selectedIndexlocal int
	 * @param record Record
	 * @param recordList List<Record>
	 */
	private void addCheckBoxTable(final int index, final int selectedIndexlocal, final Record record, final List<Record> recordList,
			final boolean isSelectAllCheckBoxes) {
		radioBtnTable = false;
		final CheckBox checkBox = new CheckBox();

		if (index == selectedIndexlocal) {
			checkBox.setValue(true);
			selectedRowId = record.getIdentifier();
			selectedRecordList.add(selectedRowId);

			selectedIndex = index;
			if (null != listner && fireEventForFirstRow) {
				listner.onRowSelected(selectedRowId);
			}

			scrollIntoView(table.getWidget(selectedIndexlocal, FIRST_COLUMN_INDEX).getElement());
		}
		if (isSelectAllCheckBoxes) {
			checkBox.setValue(true);
			selectedRowId = record.getIdentifier();
			selectedRecordList.add(selectedRowId);
			if (null != listner && fireEventForFirstRow) {
				listner.onRowSelected(selectedRowId);
			}
		}

		checkBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent arg0) {
				checkBoxClick = true;

				selectedRowId = record.getIdentifier();
				selectedIndex = recordList.indexOf(record);

				if (checkBox.getValue()) {
					selectedRecordList.add(selectedRowId);
				} else {
					selectedRecordList.remove(selectedRowId);
				}

			}
		});

		table.setWidget(index, 0, checkBox);
		SelectionHandlerContainer selectionHandlerContainer = new SelectionHandlerContainer(checkBox, record.getIdentifier());
		selectionHandlerMap.put(new Integer(index), selectionHandlerContainer);
		table.getCellFormatter().setHorizontalAlignment(index, 0, HasHorizontalAlignment.ALIGN_CENTER);
	}

	private void update(final SelectionHandlers isRadioButton, final int startIndex) {
		update(isRadioButton, startIndex, 0);
	}

	private void setNavigationBar() {
		navBarTable.getCellFormatter().setWidth(0, 1, "30%");
		navBarTable.getCellFormatter().setWidth(0, 2, "10%");
		navBarTable.getCellFormatter().setWidth(0, 3, "10%");
		navBarTable.getCellFormatter().setWidth(0, 4, "22%");
		navBarTable.getCellFormatter().setWidth(0, 5, "23%");
		Label displayText = new Label(navBar.getCountString());
		Label searchPageText = new Label(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_go_to_page));
		Label totalPageCountText = new Label("  /" + navBar.getTotalPageCount());
		displayText.addStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		searchPageText.addStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		totalPageCountText.addStyleName(CoreCommonConstants.BOLD_TEXT_CSS);
		HorizontalPanel searchPagePanel = new HorizontalPanel();
		searchPagePanel.add(searchPageText);
		searchPagePanel.add(navBar.getSearchPageTextBox());
		searchPagePanel.add(totalPageCountText);
		searchPagePanel.setCellHorizontalAlignment(searchPageText, HasHorizontalAlignment.ALIGN_RIGHT);
		searchPagePanel.setCellHorizontalAlignment(navBar.getSearchPageTextBox(), HasHorizontalAlignment.ALIGN_RIGHT);
		searchPagePanel.setCellHorizontalAlignment(totalPageCountText, HasHorizontalAlignment.ALIGN_RIGHT);
		if (this.navBar.getOrderingListner() != null) {
			navBarTable.setWidget(0, 2, navBar.getOrderingPanel());
			navBarTable.getCellFormatter().setHorizontalAlignment(0, 2, HasHorizontalAlignment.ALIGN_RIGHT);
		}
		if (this.navBar.getListner() != null) {
			navBarTable.setWidget(0, 3, navBar.getPaginationPanel());
			navBarTable.getCellFormatter().setHorizontalAlignment(0, 3, HasHorizontalAlignment.ALIGN_RIGHT);
			navBarTable.setWidget(0, 4, searchPagePanel);
		}
		navBarTable.setWidget(0, 5, displayText);
		navBarTable.getCellFormatter().setHorizontalAlignment(0, 4, HasHorizontalAlignment.ALIGN_RIGHT);
		navBarTable.getCellFormatter().setHorizontalAlignment(0, 5, HasHorizontalAlignment.ALIGN_RIGHT);
	}

	/**
	 * To get the selected row id.
	 * 
	 * @return String
	 */
	public String getSelectedRowId() {
		return selectedRowId;
	}

	public Order getOrder() {
		return order;
	}

	public List<String> getSelectedRowIds() {
		return selectedRecordList;
	}

	@UiHandler("table")
	void onTableClicked(final ClickEvent event) {
		removeSelectedRowStyleFromTable();
		Cell cell = table.getCellForEvent(event);
		if (cell != null && totalCount != 0) {
			int row = cell.getRowIndex();
			selectedIndex = row;
			table.getRowFormatter().addStyleName(row, ROW_HIGHLIGHTED_CSS);
			SelectionHandlerContainer selectionHandlerContainer = selectionHandlerMap.get(row);
			if (selectionHandlerContainer != null
					&& ((selectionHandlerContainer.getRadioButton() != null) || (selectionHandlerContainer.getCheckBox() != null))) {

				selectedRowId = selectionHandlerContainer.getIdentifier();

				if (radioBtnTable) {
					clearRadioButtons();
					selectionHandlerContainer.getRadioButton().setValue(true);
				} else if (!checkBoxClick) {
					selectionHandlerContainer.getCheckBox().setValue(!selectionHandlerContainer.getCheckBox().getValue());
					if (selectionHandlerContainer.getCheckBox().getValue()) {
						selectedRecordList.add(selectedRowId);
					} else {
						selectedRecordList.remove(selectedRowId);
					}
				}

				if (listner != null) {
					listner.onRowSelected(selectedRowId);
				}
			}
			if (selectionHandlerContainer != null && selectionHandlerContainer.getIdentifier() != null
					&& (selectionHandlerContainer.getRadioButton() == null || selectionHandlerContainer.getCheckBox() == null)) {
				selectedRowId = selectionHandlerContainer.getIdentifier();
			}

			if (!radioBtnTable && !noSelectionHandler) {
				for (Integer rowId : selectionHandlerMap.keySet()) {
					if (selectionHandlerMap.get(rowId).getCheckBox().getValue()) {
						table.getRowFormatter().setStyleName(rowId, ROW_HIGHLIGHTED_CSS);
					} else {
						table.getRowFormatter().removeStyleName(rowId, ROW_HIGHLIGHTED_CSS);
						if (rowId % 2 == 0) {
							table.getRowFormatter().setStyleName(rowId, ODD_CSS);
						} else {
							table.getRowFormatter().setStyleName(rowId, EVEN_CSS);
						}
					}
				}
			}
			checkBoxClick = false;

		}
	}

	private void clearRadioButtons() {
		for (SelectionHandlerContainer radioBtnContainer : selectionHandlerMap.values()) {
			radioBtnContainer.getRadioButton().setValue(false);
		}
	}

	private void removeSelectedRowStyleFromTable() {
		for (int rowId = 0; rowId < table.getRowCount(); rowId++) {
			table.getRowFormatter().removeStyleName(rowId, ROW_HIGHLIGHTED_CSS);
		}
	}

	/**
	 * Class to handle rows selection.
	 * 
	 * @author Ephesoft
	 * @version 1.0
	 */
	private class SelectionHandlerContainer {

		/**
		 * radioButton RadioButton.
		 */
		private RadioButton radioButton;

		/**
		 * checkBox CheckBox.
		 */
		private CheckBox checkBox;

		/**
		 * identifier String.
		 */
		private String identifier;

		/**
		 * Constructor.
		 * 
		 * @param radioBtn RadioButton
		 * @param identifier String
		 */
		public SelectionHandlerContainer(final RadioButton radioBtn, final String identifier) {
			this.radioButton = radioBtn;
			this.identifier = identifier;
		}

		/**
		 * Constructor.
		 * 
		 * @param checkBox CheckBox
		 * @param identifier String
		 */
		public SelectionHandlerContainer(final CheckBox checkBox, final String identifier) {
			this.checkBox = checkBox;
			this.identifier = identifier;
		}

		/**
		 * To get Identifier.
		 * 
		 * @return String
		 */
		public String getIdentifier() {
			return identifier;
		}

		/**
		 * To get Radio Button.
		 * 
		 * @return RadioButton
		 */
		public RadioButton getRadioButton() {
			return radioButton;
		}

		/**
		 * To get CheckBox.
		 * 
		 * @return CheckBox
		 */
		public CheckBox getCheckBox() {
			return checkBox;
		}
	}

	public int getTableRecordCount() {
		return tableData.getRecordList().size();
	}

	@Override
	public HandlerRegistration addDoubleClickHandler(final DoubleClickHandler handler) {
		return addDomHandler(handler, DoubleClickEvent.getType());
	}

	/**
	 * @return the isOrderedEntity
	 */
	public boolean isOrderedEntity() {
		return isOrderedEntity;
	}

	/**
	 * @param isOrderedEntity the isOrderedEntity to set
	 */
	public void setOrderedEntity(final boolean isOrderedEntity) {
		this.isOrderedEntity = isOrderedEntity;
	}

	public int getStartIndex() {
		return navBar.getStartIndex();
	}

	/**
	 * @return the selectedIndex
	 */
	public int getSelectedIndex() {
		return selectedIndex;
	}

	/**
	 * @param selectedIndex the selectedIndex to set
	 */
	public void setSelectedIndex(final int selectedIndex) {
		this.selectedIndex = selectedIndex;
	}
	
	/**
	 * Gets outmost layout panel of the table.
	 * 
	 * @return {@link DockLayoutPanel}
	 */
	public DockLayoutPanel getOuterPanel() {
		return outerPanel;
	}


}
