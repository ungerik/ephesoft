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

package com.ephesoft.gxt.admin.client.presenter.tableextractionrule;

import java.util.Collection;
import java.util.List;

import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AddTableExtractionRuleEvent;
import com.ephesoft.gxt.admin.client.event.BCMTreeMaskEvent;
import com.ephesoft.gxt.admin.client.event.ChangeTestTblExtrRuleTogglerStateEvent;
import com.ephesoft.gxt.admin.client.event.CloseTestTblExtrRuleWindowEvent;
import com.ephesoft.gxt.admin.client.event.DeleteTableExtractionRulesEvent;
import com.ephesoft.gxt.admin.client.event.DialogWindowResizeEvent;
import com.ephesoft.gxt.admin.client.event.EnableTblExtrRuleMenuItemsEvent;
import com.ephesoft.gxt.admin.client.event.TestTableExtractionRuleEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.tableextractionrule.TableExtractionRuleGridView;
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
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionAPIModel;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
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
 * This presenter deals with Table Extraction Rule Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.presenter.tableextractionrule.TableExtractionRuleGridPresenter
 */
public class TableExtractionRuleGridPresenter extends BatchClassInlinePresenter<TableExtractionRuleGridView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<TableExtractionRuleGridPresenter> {
	}

	private DialogWindow testTblExtrRuleWindow;

	private ListPanel<DataTable, TestTableResultView> listPanel;

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Instantiates a new table extraction rule grid presenter.
	 * 
	 * @param controller the controller {@link BatchClassManagementController}
	 * @param view the view {@link TableExtractionRuleGridView}
	 */
	public TableExtractionRuleGridPresenter(final BatchClassManagementController controller, final TableExtractionRuleGridView view) {
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
			controller.setCurrentTblExtrRule(null);
			view.setExtractionRuleCell();
			view.loadGrid();
			view.setComponents(controller.getRegexBuilderView(), controller.getRegexGroupSelectionGridView());

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt .event.shared.EventBus)
	 */
	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	/**
	 * Method to get the table extraction rule dtos.
	 * 
	 * @return the table extraction rule dtos {@link Collection< {@link TableExtractionRuleDTO}>}
	 */
	public Collection<TableExtractionRuleDTO> getTableExtractionRules() {
		final TableInfoDTO selectedTableInfo = controller.getSelectedTableInfo();
		Collection<TableExtractionRuleDTO> tableExtRuleDTOs = null;
		if (selectedTableInfo != null && !selectedTableInfo.isDeleted()) {
			tableExtRuleDTOs = selectedTableInfo.getTableExtractionRuleDTOs();
		}
		return tableExtRuleDTOs;
	}

	/**
	 * Sets the select table extraction rule dto in controller.
	 * 
	 * @param cellSelectionChangeEvent {@link CellSelectionChangedEvent< {@link TableExtractionRuleDTO}>}
	 */
	public void setSelectTableExtractionRule(final CellSelectionChangedEvent<TableExtractionRuleDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			final TableExtractionRuleDTO selectedTblExtRule = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			if (selectedTblExtRule != null) {
				controller.setSelectedTableExtrRule(selectedTblExtRule);
				controller.setCurrentTblExtrRule(selectedTblExtRule);
			}
		}
	}

	/**
	 * Handle deletion of multiple table extraction rules.
	 * 
	 * @param deleteEvent the delete event {@link DeleteTableExtractionRulesEvent}
	 */
	@EventHandler
	public void handleTableExtRulesDeletion(final DeleteTableExtractionRulesEvent deleteEvent) {
		if (null != deleteEvent) {
			final List<TableExtractionRuleDTO> tableExtRuleList = view.getSelectedTableExtractionRules();
			if (!CollectionUtil.isEmpty(tableExtRuleList)) {
				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_TABLE_EXTR_RULES_MSG), false,
						DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
						deleteTableExtractionRules(tableExtRuleList);
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
	 * Delete the selected table extraction rule dtos.
	 * 
	 * @param tableExtRules the selected table extraction rule dtos {@link List< {@link TableExtractionRuleDTO}>}
	 */
	private void deleteTableExtractionRules(final List<TableExtractionRuleDTO> tableExtRules) {

		final TableInfoDTO tableInfoDTO = controller.getSelectedTableInfo();
		for (final TableExtractionRuleDTO tableExtRule : tableExtRules) {
			if (null != tableExtRule) {
				tableInfoDTO.getTableExtractionDTOByIdentifier(tableExtRule.getIdentifier()).setDeleted(true);
			}
		}
		controller.setBatchClassDirtyFlg(true);
		view.removeItemsFromGrid(tableExtRules);
		view.commitChanges();
		view.reLoadGrid();
		Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS),
				LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_TABLE_EXTR_RULES_SUCCESS_MSG));
	}

	/**
	 * Handle new table extraction rule addition.
	 * 
	 * @param addEvent the add event {@link AddTableExtractionRuleEvent}
	 */
	@EventHandler
	public void handleTableExtRuleAddition(final AddTableExtractionRuleEvent addEvent) {
		view.commitChanges();
		if (null != addEvent) {
			final TableExtractionRuleDTO tableExtRuleDTO = createTableExtractionRule();
			if (view.addNewItemInGrid(tableExtRuleDTO)) {
				controller.getSelectedTableInfo().addTableExtractionRule(tableExtRuleDTO);
				controller.setBatchClassDirtyFlg(true);
			}
		}
	}

	/**
	 * Method to create the new table extraction rule dto.
	 * 
	 * @return the table extraction rule dto {@link TableExtractionRuleDTO}
	 */
	private TableExtractionRuleDTO createTableExtractionRule() {
		final TableExtractionRuleDTO tableExtractionRuleDTO = new TableExtractionRuleDTO();
		final TableExtractionAPIModel extAPIModel = new TableExtractionAPIModel();
		tableExtractionRuleDTO.setNew(true);
		tableExtractionRuleDTO.setTableInfoDTO(controller.getSelectedTableInfo());
		tableExtractionRuleDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		tableExtractionRuleDTO.setExtAPIModel(extAPIModel);
		initializeTableColumnExtractionRule(tableExtractionRuleDTO);
		return tableExtractionRuleDTO;
	}

	public void initializeTableColumnExtractionRule(TableExtractionRuleDTO tableExtractionRuleDTO) {
		if (null != tableExtractionRuleDTO) {
			List<TableColumnInfoDTO> tableColumnInfoList = getTableColumnInfoList(tableExtractionRuleDTO);
			if (tableColumnInfoList != null) {
				for (TableColumnInfoDTO tableColumnInfoDTO : tableColumnInfoList) {
					if (tableColumnInfoDTO != null) {
						addTableColumnExtractionRule(tableExtractionRuleDTO, tableColumnInfoDTO);
					}
				}
			}
		}
	}

	private void addTableColumnExtractionRule(TableExtractionRuleDTO tableExtractionRuleDTO, TableColumnInfoDTO tableColumnInfoDTO) {
		if (tableColumnInfoDTO != null && tableExtractionRuleDTO != null) {
			TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO = new TableColumnExtractionRuleDTO();
			tableColumnExtractionRuleDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
			tableColumnExtractionRuleDTO.setColumnName(tableColumnInfoDTO.getColumnName());
			tableColumnExtractionRuleDTO.setNew(true);
			tableColumnExtractionRuleDTO.setDeleted(false);
			tableColumnExtractionRuleDTO.setTableExtractionRuleDTO(tableExtractionRuleDTO);
			tableColumnExtractionRuleDTO.setTableColumnInfoDTO(tableColumnInfoDTO);
			tableExtractionRuleDTO.addTableColumnExtractionRule(tableColumnExtractionRuleDTO);
		}
	}

	private TableInfoDTO getTableInfoDTO(TableExtractionRuleDTO tableExtractionRuleDTO) {
		TableInfoDTO tableInfoDTO = null;
		if (tableExtractionRuleDTO != null) {
			tableInfoDTO = tableExtractionRuleDTO.getTableInfoDTO();
		}
		return tableInfoDTO;
	}

	private List<TableColumnInfoDTO> getTableColumnInfoList(TableExtractionRuleDTO tableExtractionRuleDTO) {
		TableInfoDTO tableInfoDTO = getTableInfoDTO(tableExtractionRuleDTO);
		List<TableColumnInfoDTO> tableColumnsInfoList = null;
		if (null != tableInfoDTO) {
			tableColumnsInfoList = tableInfoDTO.getTableColumnInfoList();
		}
		return tableColumnsInfoList;
	}

	/**
	 * Method to set the batch class dirty on change.
	 * 
	 */
	public void setBatchClassDirtyOnChange() {
		controller.setBatchClassDirtyFlg(true);
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
	public void handleCloseTestTblExtrRuleView(final CloseTestTblExtrRuleWindowEvent event) {
		if (null != testTblExtrRuleWindow) {
			testTblExtrRuleWindow.hide();
		}
	}

	@EventHandler
	public void handleTestTableExtrRuleEvent(final TestTableExtractionRuleEvent event) {
		if (null != event) {
			ScreenMaskUtility.maskScreen();
			testTblExtrRuleWindow = null;
			listPanel = null;
			if (isValid()) {
				final List<TableExtractionRuleDTO> tableExtrRules = view.getSelectedTableExtractionRules();
				if (CollectionUtil.isEmpty(view.getTableExtrRuleDTOs())) {
					BatchClassManagementEventBus.fireEvent(new ChangeTestTblExtrRuleTogglerStateEvent());
					ScreenMaskUtility.unmaskScreen();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.NO_RECORD_EXIST_MSG), DialogIcon.WARNING);
				} else if (!CollectionUtil.isEmpty(tableExtrRules)) {
					if (tableExtrRules.size() == 1) {
						TableExtractionRuleDTO tblExtrRuleDTO = tableExtrRules.get(0);
						if (tblExtrRuleDTO != null && !CollectionUtil.isEmpty(tblExtrRuleDTO.getTableInfoDTO().getColumnInfoDTOs())) {
							testTableExtractionRule(tblExtrRuleDTO);
						} else {
							BatchClassManagementEventBus.fireEvent(new ChangeTestTblExtrRuleTogglerStateEvent());
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.NO_COLUMN_EXIST_IN_TABLE_MSG),
									DialogIcon.WARNING);
						}
					} else {
						BatchClassManagementEventBus.fireEvent(new ChangeTestTblExtrRuleTogglerStateEvent());
						ScreenMaskUtility.unmaskScreen();
						String message = LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_ONE_ROW_ONLY_MSG);
						message = StringUtil.concatenate(message,
								LocaleDictionary.getMessageValue(BatchClassMessages.TEST_TABLE_COLUMN_EXTR_RULE));
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE), message,
								DialogIcon.WARNING);
					}
				} else {
					TableExtractionRuleDTO tblExtrRuleDTO = controller.getCurrentTblExtrRule();
					if (null != tblExtrRuleDTO) {
						if (!CollectionUtil.isEmpty(tblExtrRuleDTO.getTableInfoDTO().getColumnInfoDTOs())) {
							testTableExtractionRule(tblExtrRuleDTO);
						} else {
							BatchClassManagementEventBus.fireEvent(new ChangeTestTblExtrRuleTogglerStateEvent());
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.NO_COLUMN_EXIST_IN_TABLE_MSG),
									DialogIcon.WARNING);
						}
					} else {
						BatchClassManagementEventBus.fireEvent(new ChangeTestTblExtrRuleTogglerStateEvent());
						ScreenMaskUtility.unmaskScreen();
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.NO_ROW_SELECTED), DialogIcon.WARNING);
					}
				}
			} else {
				BatchClassManagementEventBus.fireEvent(new ChangeTestTblExtrRuleTogglerStateEvent());
				ScreenMaskUtility.unmaskScreen();
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.TEST_TABLE_VALIDATE_GRID_ERR_MSG), DialogIcon.WARNING);
			}
		}
	}

	private void testTableExtractionRule(final TableExtractionRuleDTO tableExtrRuleDTO) {
		controller.getRpcService().testTablePattern(controller.getSelectedBatchClass(), tableExtrRuleDTO.getTableInfoDTO(),
				tableExtrRuleDTO.getIdentifier(), new AsyncCallback<List<TestTableResultDTO>>() {

					@Override
					public void onFailure(Throwable throwable) {
						BatchClassManagementEventBus.fireEvent(new ChangeTestTblExtrRuleTogglerStateEvent());
						ScreenMaskUtility.unmaskScreen();
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.TEST_TABLE_EXTR_RULE_FAILURE_MSG),
								DialogIcon.ERROR);
					}

					@Override
					public void onSuccess(List<TestTableResultDTO> outputDtos) {
						if (!CollectionUtil.isEmpty(outputDtos)) {
							BatchClassManagementEventBus.fireEvent(new EnableTblExtrRuleMenuItemsEvent(false));
							BatchClassManagementEventBus.fireEvent(new BCMTreeMaskEvent(true));
							createTestTblExtrRuleView(outputDtos, tableExtrRuleDTO);
							ScreenMaskUtility.unmaskScreen();
							testTblExtrRuleWindow.show();

						} else {
							BatchClassManagementEventBus.fireEvent(new ChangeTestTblExtrRuleTogglerStateEvent());
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
									LocaleDictionary.getConstantValue(CoreCommonConstants.NO_RECORDS_FOUND), DialogIcon.INFO);
						}

					}
				});
	}

	private void createTestTblExtrRuleView(List<TestTableResultDTO> testTableResultDTOs, TableExtractionRuleDTO tableExtrRuleDTO) {
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
			// set input file name and rule name
			testTableResultView.setInputFileName(testTableResultDTO.getInputFileName());
			if (tableExtrRuleDTO != null) {
				testTableResultView.setRuleName(tableExtrRuleDTO.getRuleName());
			}
			listPanel.add(testTableResultView);
		}
		listPanel.setWidth(offsetWidth - 10);
		listPanel.setHeight(offsetHeight);
		listPanel.setScrollMode(ScrollMode.AUTO);
		listPanel.setBorders(false);
		testTblExtrRuleWindow.setBorders(false);
		testTblExtrRuleWindow.setWidget(listPanel);
	}

	private void createDialogWindow() {
		testTblExtrRuleWindow = new DialogWindow();
		testTblExtrRuleWindow.setPreferCookieDimension(false);
		testTblExtrRuleWindow.setModal(false);
		testTblExtrRuleWindow.setHeight(getOffsetHeight() + 10);
		testTblExtrRuleWindow.setWidth(getOffsetWidth());
		testTblExtrRuleWindow.setPosition(getAbsoluteLeft(), getAbsoluteTop());
		testTblExtrRuleWindow.setOnEsc(false);
		testTblExtrRuleWindow.setResizable(false);
		testTblExtrRuleWindow.setClosable(false);
		testTblExtrRuleWindow.setDraggable(false);
		testTblExtrRuleWindow.setHeaderVisible(false);
		testTblExtrRuleWindow.setPredefinedButtons();
	}

	@EventHandler
	public void handleDialogWindowResizing(DialogWindowResizeEvent beforeResize) {
		if (null != testTblExtrRuleWindow) {
			int offsetWidth = getOffsetWidth();
			int offsetHeight = getOffsetHeight();
			testTblExtrRuleWindow.setResizable(true);
			testTblExtrRuleWindow.setHeight(getOffsetHeight() + 10);
			testTblExtrRuleWindow.setWidth(offsetWidth);
			testTblExtrRuleWindow.setPosition(getAbsoluteLeft(), getAbsoluteTop());
			listPanel.setWidth(offsetWidth - 10);
			setTestTblExtrRuleViewsSize(offsetWidth, offsetHeight);
			listPanel.setScrollMode(ScrollMode.AUTO);
			testTblExtrRuleWindow.setResizable(false);
		}
	}

	private void setTestTblExtrRuleViewsSize(int offsetWidth, int offsetHeight) {
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
		return testTblExtrRuleWindow;
	}

	public void setDialogWindow(DialogWindow dialogWindow) {
		this.testTblExtrRuleWindow = dialogWindow;
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
