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

package com.ephesoft.gxt.rv.client.presenter;

import java.util.List;

import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DataTables;
import com.ephesoft.gxt.core.client.LayoutData;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocumentMergeEvent;
import com.ephesoft.gxt.rv.client.event.DocumentOpenEvent;
import com.ephesoft.gxt.rv.client.event.shortcut.initializer.TableTraversalEvent;
import com.ephesoft.gxt.rv.client.presenter.batch.DataTablePresenter;
import com.ephesoft.gxt.rv.client.view.TableExtractionView;
import com.ephesoft.gxt.rv.client.view.batch.DataTableView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.ephesoft.gxt.rv.client.widget.ThumbnailWidgetPanel;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class TableExtractionPresenter extends ReviewValidateBasePresenter<TableExtractionView> {

	interface CustomEventBinder extends EventBinder<TableExtractionPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public TableExtractionPresenter(final ReviewValidateController controller, final TableExtractionView view) {
		super(controller, view);
	}

	private static int THRESHOLD_HEIGHT_MARGIN = 75;

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void setDataTablePresenter(final DataTableView tableView) {
		if (null != tableView) {
			new DataTablePresenter(controller, tableView);
		}
	}

	@EventHandler
	public void handleDocumentTypeOpenEvent(final DocumentOpenEvent documentOpenEvent) {
		if (null != documentOpenEvent) {
			view.clear();
			final ThumbnailWidgetPanel thumbnailPanel = documentOpenEvent.getDocumentThumbnail();
			if (null != thumbnailPanel) {
				final Document bindedDocument = thumbnailPanel.getDocument();
				if (null != bindedDocument) {
					this.addDocument(bindedDocument);
				}
			}
			scheduleMergeMenuOperation(documentOpenEvent);
		}
	}

	private void addDocument(final Document bindedDocument) {
		final DataTables bindedDataTables = bindedDocument.getDataTables();
		if (null != bindedDataTables) {
			final List<DataTable> dataTablesList = bindedDataTables.getDataTable();
			if (!CollectionUtil.isEmpty(dataTablesList)) {
				final int totalTables = dataTablesList.size();
				final int clientHeight = Window.getClientHeight();
				final int tableViewWidth = controller.calculateTableViewWidth();
				final LayoutData tableData = new LayoutData();
				tableData.setWidth(tableViewWidth - 30);
				tableData.setHeight(totalTables == 1 ? clientHeight - THRESHOLD_HEIGHT_MARGIN - 25 : ((clientHeight) / 2)
						- THRESHOLD_HEIGHT_MARGIN);
				for (final DataTable dataTable : dataTablesList) {
					view.add(dataTable, tableData);
				}
			}
		}
	}

	public void scheduleMergeMenuOperation(final DocumentOpenEvent documentOpenEvent) {
		final ScheduledCommand command = new ScheduledCommand() {

			@Override
			public void execute() {
				initializeMergeMenu(documentOpenEvent);
			}
		};
		final Scheduler scheduler = new SchedulerImpl();
		scheduler.scheduleDeferred(command);
	}

	public void initializeMergeMenu(final DocumentOpenEvent documentOpenEvent) {
		boolean elementAdded = false;
		if (null != documentOpenEvent) {
			final ThumbnailWidgetPanel documentThumbnail = documentOpenEvent.getDocumentThumbnail();
			if (documentThumbnail != null) {
				final Document bindedDocument = documentThumbnail.getDocument();
				if (null != bindedDocument) {
					view.clearMergeMenuItem();
					final List<String> documentIdentifierList = ReviewValidateNavigator.getDocumentIdentifiersList();
					if (!CollectionUtil.isEmpty(documentIdentifierList)) {
						final String currentDocumentIdentifier = bindedDocument.getIdentifier();
						String documentName;
						String menuItemtext;
						ScheduledCommand mergeDocumentCommand;
						for (final String documentIdentifier : documentIdentifierList) {
							if (!currentDocumentIdentifier.equals(documentIdentifier)) {
								documentName = ReviewValidateNavigator.getDocumentName(documentIdentifier);
								menuItemtext = StringUtil.concatenate(documentIdentifier, "-", documentName);
								mergeDocumentCommand = getMergeDocumentScheduledCommand(documentIdentifier, currentDocumentIdentifier);
								elementAdded = true;
								view.addMergeMenu(menuItemtext, mergeDocumentCommand);
							}
						}
					}
				}
			}
		}
		view.setEnableMergeOptions(elementAdded);
	}

	private ScheduledCommand getMergeDocumentScheduledCommand(final String documentToMergeWithIdentifier,
			final String currentDocumentIdentifier) {
		return new ScheduledCommand() {

			@Override
			public void execute() {
				if (!StringUtil.isNullOrEmpty(documentToMergeWithIdentifier)
						&& !StringUtil.isNullOrEmpty(documentToMergeWithIdentifier)) {
					ReviewValidateEventBus.fireEvent(new DocumentMergeEvent(currentDocumentIdentifier, documentToMergeWithIdentifier));
				}
			}
		};
	}

	@EventHandler
	public void prepareFunctionKeyMenu(final DocumentOpenEvent documentOpenEvent) {
	}

	@EventHandler
	public void handleTableTraversalEvent(final TableTraversalEvent tableTraversalEvent) {
		if (null != tableTraversalEvent) {
			view.traverseToNextGrid();
		}
	}
}
