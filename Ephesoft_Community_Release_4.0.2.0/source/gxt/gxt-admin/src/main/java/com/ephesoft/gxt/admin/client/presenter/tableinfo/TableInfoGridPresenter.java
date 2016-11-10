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

package com.ephesoft.gxt.admin.client.presenter.tableinfo;

import java.util.Collection;
import java.util.List;

import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AddTableInfoEvent;
import com.ephesoft.gxt.admin.client.event.BCMTreeMaskEvent;
import com.ephesoft.gxt.admin.client.event.ChangeTestTableToggleButtonStateEvent;
import com.ephesoft.gxt.admin.client.event.CloseTestTableDialogWindowEvent;
import com.ephesoft.gxt.admin.client.event.DeleteTableInfosEvent;
import com.ephesoft.gxt.admin.client.event.DialogWindowResizeEvent;
import com.ephesoft.gxt.admin.client.event.EnableTableMenuItemsEvent;
import com.ephesoft.gxt.admin.client.event.TestTableEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.tableinfo.TableInfoGridView;
import com.ephesoft.gxt.admin.client.view.testtableresult.TestTableResultView;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.view.ListPanel;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TestTableResultDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

/**
 * This presenter deals with Table Info Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.presenter.tableinfo.TableInfoGridPresenter
 */
public class TableInfoGridPresenter extends BatchClassInlinePresenter<TableInfoGridView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<TableInfoGridPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private DialogWindow testTableDialogWindow;

	private ListPanel<DataTable, TestTableResultView> listPanel;

	/**
	 * Instantiates a new table info grid presenter.
	 * 
	 * @param controller the controller {@link BatchClassManagementController}
	 * @param view the view {@link TableInfoGridView}
	 */
	public TableInfoGridPresenter(final BatchClassManagementController controller, final TableInfoGridView view) {
		super(controller, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.event.BindHandler#bind()
	 */
	@Override
	public void bind() {
		if (null != controller.getSelectedBatchClass()) {
			controller.setCurrentTableInfo(null);
			view.loadGrid();
		}
	}

	/**
	 * Sets the select table info dto in controller.
	 * 
	 * @param cellSelectionChangeEvent the new select table info {@link CellSelectionChangedEvent<{@link TableInfoDTO}>}
	 */
	public void setSelectTableInfo(final CellSelectionChangedEvent<TableInfoDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			final TableInfoDTO selectedTableInfo = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			if (selectedTableInfo != null) {
				controller.setSelectedTableInfo(selectedTableInfo);
				controller.setCurrentTableInfo(selectedTableInfo);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	/**
	 * Method to get the table info dtos.
	 * 
	 * @return the table info dtos {@link Collection<{@link TableInfoDTO}>}
	 */
	public Collection<TableInfoDTO> getCurrentTableInfoDTOs() {
		final DocumentTypeDTO selectedDocType = controller.getSelectedDocumentType();
		Collection<TableInfoDTO> tableInfoCollection = null;
		if (selectedDocType != null && !selectedDocType.isDeleted()) {
			tableInfoCollection = selectedDocType.getTableInfos();
		}
		return tableInfoCollection;
	}

	/**
	 * Handle deletion of multiple table info dtos.
	 * 
	 * @param deleteEvent the delete event {@link DeleteTableInfosEvent}
	 */
	@EventHandler
	public void handleTablesDeletion(final DeleteTableInfosEvent deleteEvent) {
		if (null != deleteEvent) {
			final List<TableInfoDTO> selectedTableInfos = view.getSelectedTableInfos();
			if (!CollectionUtil.isEmpty(selectedTableInfos)) {
				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_TABLE_INFO_MSG), false, DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
						deleteSelectedTables(selectedTableInfos);
					}

					@Override
					public void onCancelClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
					}
				});
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.NO_ROW_SELECTED), DialogIcon.WARNING);
			}
		}
	}

	/**
	 * Delete selected table info dtos.
	 * 
	 * @param selectedTableInfos the selected table info dtos {@link List<{@link TableInfoDTO}>}
	 */
	private void deleteSelectedTables(final List<TableInfoDTO> selectedTableInfos) {

		final DocumentTypeDTO docTypeDTO = controller.getSelectedDocumentType();
		for (final TableInfoDTO tableInfo : selectedTableInfos) {
			if (null != tableInfo) {
				docTypeDTO.getTableInfoByIdentifier(tableInfo.getIdentifier()).setDeleted(true);
			}
		}
		controller.setBatchClassDirtyFlg(true);
		view.removeItemsFromGrid(selectedTableInfos);
		view.commitChanges();
		view.reLoadGrid();
		Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS),
				LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_TABLES_SUCCESS_MSG));
	}

	/**
	 * Handle new table info addition.
	 * 
	 * @param addEvent the add event {link AddTableInfoEvent}
	 */
	@EventHandler
	public void handleTableAddition(final AddTableInfoEvent addEvent) {
		view.commitChanges();
		if (null != addEvent) {
			final TableInfoDTO tableInfoDTO = createTableInfoDTO();
			if (view.addNewItemInGrid(tableInfoDTO)) {
				controller.getSelectedDocumentType().addTableInfoDTO(tableInfoDTO);
				controller.setBatchClassDirtyFlg(true);
			}

		}
	}

	/**
	 * Method to create the new table info dto.
	 * 
	 * @return the table info dto {@link TableInfoDTO}
	 */
	private TableInfoDTO createTableInfoDTO() {
		final TableInfoDTO tableInfoDTO = new TableInfoDTO();
		tableInfoDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		tableInfoDTO.setNew(true);
		tableInfoDTO.setDocTypeDTO(controller.getSelectedDocumentType());
		tableInfoDTO.setNumberOfRows(BatchClassConstants.DEFAULT_NUMBER_OF_ROWS);
		tableInfoDTO.setRuleOperator(BatchClassConstants.AND_OPERATOR);
		return tableInfoDTO;

	}

	/**
	 * Method to set the batch class dirty on change.
	 * 
	 */
	public void setBatchClassDirtyOnChange() {
		controller.setBatchClassDirtyFlg(true);
		;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isValid() {
		return view.isGridValidated();
	}

	@EventHandler
	public void handleCloseTestTableView(final CloseTestTableDialogWindowEvent event) {
		if (null != testTableDialogWindow) {
			testTableDialogWindow.hide();
		}
	}

	@EventHandler
	public void handleTestTableEvent(final TestTableEvent event) {
		if (null != event) {
			ScreenMaskUtility.maskScreen();
			testTableDialogWindow = null;
			listPanel = null;
			if (isValid()) {
				final List<TableInfoDTO> tableDTOList = view.getSelectedTableInfos();
				if (CollectionUtil.isEmpty(view.getTableDTOs())) {
					BatchClassManagementEventBus.fireEvent(new ChangeTestTableToggleButtonStateEvent());
					ScreenMaskUtility.unmaskScreen();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.NO_RECORD_EXIST_MSG), DialogIcon.WARNING);
				} else if (!CollectionUtil.isEmpty(tableDTOList)) {
					if (tableDTOList.size() == 1) {
						TableInfoDTO tableInfoDTO = tableDTOList.get(0);
						if (!CollectionUtil.isEmpty(tableInfoDTO.getColumnInfoDTOs())) {
							testTable(tableInfoDTO);
						} else {
							BatchClassManagementEventBus.fireEvent(new ChangeTestTableToggleButtonStateEvent());
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.NO_COLUMN_EXIST_IN_TABLE_MSG),
									DialogIcon.WARNING);
						}
					} else {
						BatchClassManagementEventBus.fireEvent(new ChangeTestTableToggleButtonStateEvent());
						ScreenMaskUtility.unmaskScreen();
						String message = LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_ONE_ROW_ONLY_MSG);
						message = StringUtil.concatenate(message, LocaleDictionary.getMessageValue(BatchClassMessages.TEST_TABLE));
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE), message,
								DialogIcon.WARNING);
					}
				} else {
					TableInfoDTO tableInfoDTO = controller.getCurrentTableInfo();
					if (null != tableInfoDTO) {
						if (!CollectionUtil.isEmpty(tableInfoDTO.getColumnInfoDTOs())) {
							testTable(tableInfoDTO);
						} else {
							BatchClassManagementEventBus.fireEvent(new ChangeTestTableToggleButtonStateEvent());
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.NO_COLUMN_EXIST_IN_TABLE_MSG),
									DialogIcon.WARNING);
						}
					} else {
						BatchClassManagementEventBus.fireEvent(new ChangeTestTableToggleButtonStateEvent());
						ScreenMaskUtility.unmaskScreen();
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.NO_ROW_SELECTED), DialogIcon.WARNING);
					}
				}
			} else {
				BatchClassManagementEventBus.fireEvent(new ChangeTestTableToggleButtonStateEvent());
				ScreenMaskUtility.unmaskScreen();
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.TEST_TABLE_VALIDATE_GRID_ERR_MSG), DialogIcon.WARNING);
			}
		}
	}

	private void testTable(TableInfoDTO tableInfoDTO) {
		controller.getRpcService().testTablePattern(controller.getSelectedBatchClass(), tableInfoDTO, null,
				new AsyncCallback<List<TestTableResultDTO>>() {

					@Override
					public void onFailure(Throwable throwable) {
						BatchClassManagementEventBus.fireEvent(new ChangeTestTableToggleButtonStateEvent());
						ScreenMaskUtility.unmaskScreen();
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.TEST_TABLE_FAILURE_MSG), DialogIcon.ERROR);
					}

					@Override
					public void onSuccess(List<TestTableResultDTO> outputDtos) {
						// boolean isRemove = tableDTOList.get(0).isRemoveInvalidRows();
						if (!CollectionUtil.isEmpty(outputDtos)) {
							BatchClassManagementEventBus.fireEvent(new EnableTableMenuItemsEvent(false));
							BatchClassManagementEventBus.fireEvent(new BCMTreeMaskEvent(true));
							createTestTableView(outputDtos);
							ScreenMaskUtility.unmaskScreen();
							testTableDialogWindow.show();

						} else {
							BatchClassManagementEventBus.fireEvent(new ChangeTestTableToggleButtonStateEvent());
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
									LocaleDictionary.getConstantValue(CoreCommonConstants.NO_RECORDS_FOUND), DialogIcon.INFO);
						}

					}
				});
	}

	private void createTestTableView(List<TestTableResultDTO> testTableResultDTOs) {
		TestTableResultView testTableResultView = null;
		listPanel = new ListPanel<DataTable, TestTableResultView>();
		listPanel.addStyleName("testTable");
		createDialogWindow();
		Integer height = null;
		Integer width = null;
		int offsetHeight = getOffsetHeight();
		int offsetWidth = getOffsetWidth();
		if (testTableResultDTOs.size() == 1) {
			height = offsetHeight - 50;
			width = offsetWidth - 10;
		} else if (testTableResultDTOs.size() == 2) {
			height = (offsetHeight / 2) - 45;
			width = offsetWidth - 10;
		} else {
			height = (offsetHeight / 2) - 45;
			width = offsetWidth - 40;
		}
		for (TestTableResultDTO testTableResultDTO : testTableResultDTOs) {
			testTableResultView = new TestTableResultView(testTableResultDTO.getDataTable(), width, height);
			// set Input file name and rule name
			testTableResultView.setInputFileName(testTableResultDTO.getInputFileName());
			if (testTableResultDTO.getDataTable() != null) {
				testTableResultView.setRuleName(testTableResultDTO.getDataTable().getRuleName());
			}
			listPanel.add(testTableResultView);
		}
		listPanel.setWidth(offsetWidth - 10);
		listPanel.setHeight(offsetHeight);
		listPanel.setScrollMode(ScrollMode.AUTO);
		listPanel.setBorders(false);
		testTableDialogWindow.setBorders(false);
		testTableDialogWindow.setWidget(listPanel);
	}

	private void createDialogWindow() {
		testTableDialogWindow = new DialogWindow();
		testTableDialogWindow.setPreferCookieDimension(false);
		testTableDialogWindow.setModal(false);
		testTableDialogWindow.setHeight(getOffsetHeight() + 10);
		testTableDialogWindow.setWidth(getOffsetWidth());
		testTableDialogWindow.setPosition(getAbsoluteLeft(), getAbsoluteTop());
		testTableDialogWindow.setOnEsc(false);
		testTableDialogWindow.setResizable(false);
		testTableDialogWindow.setClosable(false);
		testTableDialogWindow.setDraggable(false);
		testTableDialogWindow.setHeaderVisible(false);
		testTableDialogWindow.setPredefinedButtons();
	}

	@EventHandler
	public void handleDialogWindowResizing(DialogWindowResizeEvent beforeResize) {
		if (null != testTableDialogWindow) {
			int offsetWidth = getOffsetWidth();
			int offsetHeight = getOffsetHeight();
			testTableDialogWindow.setResizable(true);
			testTableDialogWindow.setHeight(offsetHeight + 10);
			testTableDialogWindow.setWidth(offsetWidth);
			testTableDialogWindow.setPosition(getAbsoluteLeft(), getAbsoluteTop());
			setTestTableViewsSize(offsetWidth, offsetHeight);
			listPanel.setWidth(offsetWidth - 10);
			listPanel.setScrollMode(ScrollMode.AUTO);
			testTableDialogWindow.setResizable(false);
		}
	}

	private void setTestTableViewsSize(int offsetWidth, int offsetHeight) {
		Integer height = null;
		Integer width = null;
		if (listPanel != null) {
			List<TestTableResultView> testTableResultViews = listPanel.getDomainViewList();
			if (!CollectionUtil.isEmpty(testTableResultViews)) {
				if (testTableResultViews.size() == 1) {
					height = offsetHeight - 50;
					width = offsetWidth - 20;
				} else if (testTableResultViews.size() == 2) {
					height = (offsetHeight / 2) - 45;
					width = offsetWidth - 20;
				} else {
					height = (offsetHeight / 2) - 45;
					width = offsetWidth - 40;
				}
				for (TestTableResultView testTableResultView : testTableResultViews) {
					testTableResultView.setGridWidth(width);
					testTableResultView.setGridHeight(height);
					testTableResultView.setHPanelWidth(width);
					testTableResultView.refreshGrid();
				}
			}
		}
	}

	public DialogWindow getDialogWindow() {
		return testTableDialogWindow;
	}

	public void setDialogWindow(DialogWindow dialogWindow) {
		this.testTableDialogWindow = dialogWindow;
	}

	private native int getViewPortWidth() /*-{
											return $wnd.getViewPortWidth();
											}-*/;

	private int getOffsetHeight() {
		int listViewHeight = controller.getListPanelOffsetHeight();
		int bottomPanelHeight = controller.getBottomPanelAbsoluteHeight();
		int dialogWindowHeight = listViewHeight + bottomPanelHeight;
		return dialogWindowHeight;
	}

	private int getOffsetWidth() {
		return controller.getListPanelAbsoluteWidth();
	}

	private int getAbsoluteLeft() {
		return controller.getListPanelAbsoluteLeft();
	}

	private int getAbsoluteTop() {
		return controller.getListPanelAbsoluteTop();
	}
}
