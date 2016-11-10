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

package com.ephesoft.gxt.rv.client.view;

import java.util.List;

import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.gxt.core.client.LayoutData;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.view.ListPanel;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.ReviewValidateBatchStartEvent;
import com.ephesoft.gxt.rv.client.event.ToggleTableViewDisplayModeEvent;
import com.ephesoft.gxt.rv.client.presenter.TableExtractionPresenter;
import com.ephesoft.gxt.rv.client.view.batch.DataTableView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class TableExtractionView extends ReviewValidateBaseView<TableExtractionPresenter> {

	interface Binder extends UiBinder<Widget, TableExtractionView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected ListPanel<DataTable, DataTableView> dataTableListPanel;

	@UiField
	protected CustomMenuBar tableExtractionMenuBar;

	protected CustomMenuBar mergeMenuBar;

	private final CustomMenuBar functionKeyMenuBar;

	private final CustomMenuItem validateBatchMenuItem;

	private final CustomMenuItem functionKeyMenuItem;

	private final CustomMenuItem mergeDocumentMenuItem;

	private final CustomMenuItem dlfViewMenuItem;

	@Override
	public void initialize() {
	}

	public TableExtractionView() {
		initWidget(binder.createAndBindUi(this));
		validateBatchMenuItem = new CustomMenuItem("Validate Batch", this.getValidateBatchScheduledCommand());
		WidgetUtil.setID(validateBatchMenuItem, "rv-table-validateBatchButton");
		functionKeyMenuBar = new CustomMenuBar(true);
		functionKeyMenuItem = new CustomMenuItem("Function Keys", functionKeyMenuBar);
		WidgetUtil.setID(functionKeyMenuItem, "rv-table-functionKeyButton");
		tableExtractionMenuBar.addStyleName("tableMenuBar");
		tableExtractionMenuBar.setFocusOnHoverEnabled(false);
		mergeMenuBar = new CustomMenuBar(true);
		mergeDocumentMenuItem = new CustomMenuItem("Merge", mergeMenuBar);
		mergeMenuBar.addStyleName("tableMenuPopup");
		dlfViewMenuItem = new CustomMenuItem("Field View", getDLFViewOpenScheduledCommand());
		WidgetUtil.setID(functionKeyMenuItem, "rv-dlfViewButton");
		tableExtractionMenuBar.addItem(validateBatchMenuItem);
		WidgetUtil.setID(mergeDocumentMenuItem, "rv-table-mergeDocumentButton");
		tableExtractionMenuBar.addItem(functionKeyMenuItem);
		tableExtractionMenuBar.addItem(mergeDocumentMenuItem);
		tableExtractionMenuBar.addItem(dlfViewMenuItem);
	}

	private ScheduledCommand getDLFViewOpenScheduledCommand() {
		return new ScheduledCommand() {

			@Override
			public void execute() {
				ReviewValidateEventBus.fireEvent(new ToggleTableViewDisplayModeEvent());
			}
		};
	}

	public void add(final DataTable dataTableToAdd, final LayoutData gridLayoutData) {
		final DataTableView viewToAdd = new DataTableView(dataTableToAdd);
		if (null != gridLayoutData) {
			viewToAdd.setGridHeight(gridLayoutData.getHeight());
			viewToAdd.setGridWidth(gridLayoutData.getWidth());
		}
		dataTableListPanel.add(viewToAdd);
		presenter.setDataTablePresenter(viewToAdd);
	}

	private ScheduledCommand getValidateBatchScheduledCommand() {
		return new ScheduledCommand() {

			@Override
			public void execute() {
				ReviewValidateEventBus
						.fireEvent(new ReviewValidateBatchStartEvent(ReviewValidateNavigator.doReviewValidateSave(false)));
			}
		};
	}

	public void clear() {
		dataTableListPanel.clear();
	}

	public void clearMergeMenuItem() {
		mergeMenuBar.clearItems();
	}

	public void addMergeMenu(final String textToDisplay, final ScheduledCommand commandToExecute) {
		if (!StringUtil.isNullOrEmpty(textToDisplay) && commandToExecute != null) {
			addMergeMenuItem(new CustomMenuItem(textToDisplay, commandToExecute));
		}
	}

	public void addMergeMenuItem(final CustomMenuItem menuItemToAdd) {
		if (menuItemToAdd != null) {
			mergeMenuBar.addItem(menuItemToAdd);
		}
	}

	public void setEnableMergeOptions(final boolean enable) {
		mergeDocumentMenuItem.setEnabled(enable);
	}

	public boolean isValid() {
		boolean isValid = true;
		Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
		int totalTables = BatchSchemaUtil.getDataTableCount(currentDocument);
		if (totalTables > 0) {
			final List<DataTableView> dataTableList = dataTableListPanel.getDomainViewList();
			if (!CollectionUtil.isEmpty(dataTableList)) {
				for (final DataTableView tableView : dataTableList) {
					if (null != tableView && !tableView.isValid()) {
						isValid = false;
						break;
					}
				}
			}
		}
		return isValid;
	}

	public void selectDefaultCell() {
		executeDefaultCellSelection();
	}

	private void executeDefaultCellSelection() {
		if (!isValid()) {
			final List<DataTableView> dataTableList = dataTableListPanel.getDomainViewList();
			if (!CollectionUtil.isEmpty(dataTableList)) {
				for (final DataTableView tableView : dataTableList) {
					if (null != tableView && !tableView.isValid()) {
						tableView.selectFirstInvalidCell();
						break;
					}
				}
			}
		} else {
			DataTableView tableView = getTableViewAtIndex(0);
			if (null != tableView) {
				tableView.focus();
				tableView.select(0);
			}
		}
	}

	public DataTableView getTableViewAtIndex(int index) {
		final List<DataTableView> dataTableList = dataTableListPanel.getDomainViewList();
		DataTableView tableView = null;
		if (index >= 0 && !CollectionUtil.isEmpty(dataTableList) && dataTableList.size() > index) {
			tableView = dataTableList.get(index);
		}
		return tableView;
	}

	public void traverseToNextGrid() {
		DataTable dataTable = ReviewValidateNavigator.getCurrentTable();
		DataTableView tableView = dataTableListPanel.getNextView(dataTable, false, false);
		tableView = tableView == null ? dataTableListPanel.getView(0) : tableView;
		if (null != tableView) {
			tableView.focus();
			tableView.select(0);
		}
	}

}
