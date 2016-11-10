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

package com.ephesoft.gxt.admin.client.presenter.tablecolumninfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AddTableColumnEvent;
import com.ephesoft.gxt.admin.client.event.DeleteTableColumnsEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.tablecolumninfo.TableColumnInfoGridView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.RuleInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * This presenter deals with Table Column Info Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.presenter.tablecolumninfo.TableColumnInfoGridPresenter
 */
public class TableColumnInfoGridPresenter extends BatchClassInlinePresenter<TableColumnInfoGridView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<TableColumnInfoGridPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Instantiates a new table column info grid presenter.
	 * 
	 * @param controller the controller {@link BatchClassManagementController}
	 * @param view the view {@link TableColumnInfoGridView}
	 */
	public TableColumnInfoGridPresenter(final BatchClassManagementController controller, final TableColumnInfoGridView view) {
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
	 * Method to get the table column info dtos.
	 * 
	 * @return the table column info dtos {@link Collection< {@link TableColumnInfoDTO}>}
	 */
	public Collection<TableColumnInfoDTO> getCurrentTableColumnDTOs() {
		final TableInfoDTO selectedTableInfo = controller.getSelectedTableInfo();
		Collection<TableColumnInfoDTO> columnDTOs = null;
		if (selectedTableInfo != null && !selectedTableInfo.isDeleted()) {
			columnDTOs = selectedTableInfo.getTableColumnInfoList();
		}
		return columnDTOs;
	}

	/**
	 * Handle deletion of multiple table column info dtos.
	 * 
	 * @param deleteEvent the delete event {@link DeleteTableColumnsEvent}
	 */
	@EventHandler
	public void handleTableColumnsDeletion(final DeleteTableColumnsEvent deleteEvent) {
		if (null != deleteEvent) {
			final List<TableColumnInfoDTO> selectedTableColumns = view.getSelectedTableColumns();
			if (!CollectionUtil.isEmpty(selectedTableColumns)) {
				final ConfirmationDialog confirmationDialog;
				if (controller.getSelectedTableInfo() != null
						&& CollectionUtil.isEmpty(controller.getSelectedTableInfo().getTableExtractionRuleDTOs(false))) {
					confirmationDialog = DialogUtil.showConfirmationDialog(
							LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
							LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_TABLE_COLUMNS_MSG), false,
							DialogIcon.QUESTION_MARK);
				} else {
					confirmationDialog = DialogUtil.showConfirmationDialog(
							LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
							LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_TABLE_COLUMNS_WITH_MAPPING_MSG), false,
							DialogIcon.QUESTION_MARK);
				}
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
						deleteSelectedTableColumns(selectedTableColumns);
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
	 * Delete selected table column info dtos.
	 * 
	 * @param selectedTableColumns the selected table column info dtos {@link List< {@link TableColumnInfoDTO}>}
	 */
	private void deleteSelectedTableColumns(final List<TableColumnInfoDTO> selectedTableColumns) {
		final TableInfoDTO tableInfoDTO = controller.getSelectedTableInfo();
		final List<TableColumnInfoDTO> deletablecolumnsList = new ArrayList<TableColumnInfoDTO>();
		boolean isColValidRuleExist = false;
		String nonDeletableColumns = CoreCommonConstant.EMPTY_STRING;
		final List<RuleInfoDTO> tableValidationRules = tableInfoDTO.getRuleInfoDTOs();
		final List<TableExtractionRuleDTO> tableExtractionRuleDTOList = tableInfoDTO.getTableExtractionRuleDTOs(false);
		for (final TableColumnInfoDTO tableColumn : selectedTableColumns) {
			isColValidRuleExist = false;
			if (null != tableColumn) {
				if (!CollectionUtil.isEmpty(tableValidationRules)) {
					for (final RuleInfoDTO tableValidationRule : tableValidationRules) {
						final String ruleString = tableValidationRule.getRule();
						if (!StringUtil.isNullOrEmpty(tableColumn.getColumnName())
								&& ruleString.toLowerCase().contains(tableColumn.getColumnName().toLowerCase())) {
							isColValidRuleExist = true;
							break;
						}
					}
					if (isColValidRuleExist) {
						if (!StringUtil.isNullOrEmpty(nonDeletableColumns)) {
							nonDeletableColumns = StringUtil.concatenate(nonDeletableColumns, CoreCommonConstant.COMMA,
									CoreCommonConstant.SPACE, tableColumn.getColumnName());
						} else {
							nonDeletableColumns = StringUtil.concatenate(nonDeletableColumns, tableColumn.getColumnName());
						}
					} else {
						tableInfoDTO.getTableColumnInfoDTOByIdentifier(tableColumn.getIdentifier()).setDeleted(true);
						deleteTableColumnExtractionDTO(tableExtractionRuleDTOList, tableColumn);
						deletablecolumnsList.add(tableColumn);
					}
				} else {
					tableInfoDTO.getTableColumnInfoDTOByIdentifier(tableColumn.getIdentifier()).setDeleted(true);
					deleteTableColumnExtractionDTO(tableExtractionRuleDTOList, tableColumn);
					deletablecolumnsList.add(tableColumn);
				}
			}
		}

		if (!StringUtil.isNullOrEmpty(nonDeletableColumns)) {
			DialogUtil.showMessageDialog(
					LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					nonDeletableColumns + CoreCommonConstant.SPACE
							+ LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_VALIDATION_RULES_EXIST_ERR_MSG),
					DialogIcon.ERROR);
		}
		if (!CollectionUtil.isEmpty(deletablecolumnsList)) {
			controller.setBatchClassDirtyFlg(true);
			view.removeItemsFromGrid(deletablecolumnsList);
			view.commitChanges();
			view.reLoadGrid();
			Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS),
					LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_TABLE_COLUMNS_SUCCESS_MSG));
		}
	}

	public void deleteTableColumnExtractionDTO(List<TableExtractionRuleDTO> listOfExtractionRules,
			TableColumnInfoDTO tableColumnInfoDTO) {
		String extractedDataColumn = null;
		if (!CollectionUtil.isEmpty(listOfExtractionRules)) {
			for (TableExtractionRuleDTO tableExtractionRuleDTO : listOfExtractionRules) {
				if (tableExtractionRuleDTO != null) {
					List<TableColumnExtractionRuleDTO> tableColumnExtractionRuleDTOs = tableExtractionRuleDTO
							.getTableColumnExtractionRuleDTOs(false);
					if (!CollectionUtil.isEmpty(tableColumnExtractionRuleDTOs)) {
						for (TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO : tableColumnExtractionRuleDTOs) {
							if (tableColumnExtractionRuleDTO != null) {
								// && tableColumnExtractionRuleDTO.getTableColumnInfoDTO() == tableColumnInfoDTO) {
								extractedDataColumn = tableColumnExtractionRuleDTO.getExtractedDataColumnName();
								if (!StringUtil.isNullOrEmpty(extractedDataColumn)
										&& extractedDataColumn.trim().equalsIgnoreCase(tableColumnInfoDTO.getColumnName())) {
									tableColumnExtractionRuleDTO.setExtractedDataColumnName(null);
								}
								if (tableColumnExtractionRuleDTO.getTableColumnInfoDTO() == tableColumnInfoDTO) {
									tableColumnExtractionRuleDTO.setDeleted(true);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Handle new table column addition.
	 * 
	 * @param addEvent the add event {@link AddTableColumnEvent}
	 */
	@EventHandler
	public void handleTableColumnAddition(final AddTableColumnEvent addEvent) {
		view.commitChanges();
		if (null != addEvent) {
			final TableColumnInfoDTO tableColumnDTO = createTableColumnInfoDTO();
			if (view.addNewItemInGrid(tableColumnDTO)) {
				List<TableExtractionRuleDTO> extractionRuleDTOList = controller.getSelectedTableInfo().getTableExtractionRuleDTOs(
						false);
				addNewTableColumnRuleInfo(extractionRuleDTOList, tableColumnDTO);
				controller.getSelectedTableInfo().addColumnInfo(tableColumnDTO);
				controller.setBatchClassDirtyFlg(true);
			}
		}
	}

	/**
	 * Method to create the new table column info dto.
	 * 
	 * @return the table column info dto {@link TableColumnInfoDTO}
	 */
	private TableColumnInfoDTO createTableColumnInfoDTO() {
		final TableColumnInfoDTO tcColumnInfoDTO = new TableColumnInfoDTO();
		tcColumnInfoDTO.setNew(true);
		tcColumnInfoDTO.setTableInfoDTO(controller.getSelectedTableInfo());
		tcColumnInfoDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		tcColumnInfoDTO.setColumnName(CoreCommonConstant.EMPTY_STRING);
		return tcColumnInfoDTO;
	}

	/**
	 * Method to add a new column extraction rule in all existing table extraction rules when a new column with <code>columnName</code>
	 * is created.
	 */
	private void addNewTableColumnRuleInfo(List<TableExtractionRuleDTO> tableExtractionRuleDTOList,
			final TableColumnInfoDTO tableColumnInfoDTO) {
		if (tableExtractionRuleDTOList != null && tableColumnInfoDTO != null) {
			for (TableExtractionRuleDTO tableExtractionDTO : tableExtractionRuleDTOList) {
				TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO = new TableColumnExtractionRuleDTO();
				tableColumnExtractionRuleDTO.setNew(true);
				tableColumnExtractionRuleDTO.setColumnName(CoreCommonConstant.EMPTY_STRING);
				tableColumnExtractionRuleDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
				tableColumnExtractionRuleDTO.setTableColumnInfoDTO(tableColumnInfoDTO);
				if (tableExtractionDTO != null) {
					tableColumnExtractionRuleDTO.setTableExtractionRuleDTO(tableExtractionDTO);
					tableExtractionDTO.addTableColumnExtractionRule(tableColumnExtractionRuleDTO);
				}
			}
		}
	}

	public void actionOnColumnnValueChange(String value, TableColumnInfoDTO tableColumnDTO) {
		if (tableColumnDTO != null) {
			if (StringUtil.isNullOrEmpty(value)) {
				value = CoreCommonConstant.EMPTY_STRING;
			}
			changeTableColumnExtractionRule(controller.getSelectedTableInfo(), tableColumnDTO, value);
		}
	}

	private void changeTableColumnExtractionRule(TableInfoDTO tableInfo, TableColumnInfoDTO tableColumnDTO, String newColumnName) {
		List<TableExtractionRuleDTO> tableExtractionRuleList = tableInfo.getTableExtractionRuleDTOs(false);
		if (!CollectionUtil.isEmpty(tableExtractionRuleList)) {
			changeTableColumnExtractionRule(tableExtractionRuleList, tableColumnDTO, newColumnName);
		}
	}

	private void changeTableColumnExtractionRule(List<TableExtractionRuleDTO> tableExtractionRuleList,
			TableColumnInfoDTO tableColumnDTO, String newColumnName) {
		if (tableExtractionRuleList != null) {
			for (TableExtractionRuleDTO columnExtractionRule : tableExtractionRuleList) {
				changeTableColumnExtractionRule(columnExtractionRule, tableColumnDTO, newColumnName);
			}
		}
	}

	private void changeTableColumnExtractionRule(TableExtractionRuleDTO columnExtractionRule, TableColumnInfoDTO tableColumnDTO,
			String newColumnName) {
		if (columnExtractionRule != null) {
			List<TableColumnExtractionRuleDTO> tableColumnExtractionRuleDTOs = columnExtractionRule
					.getTableColumnExtractionRuleDTOs(false);
			if (tableColumnExtractionRuleDTOs != null) {
				for (TableColumnExtractionRuleDTO columnExtractionRuleDTO : tableColumnExtractionRuleDTOs) {
					if (columnExtractionRuleDTO != null && columnExtractionRuleDTO.getTableColumnInfoDTO() == tableColumnDTO) {
						columnExtractionRuleDTO.setColumnName(newColumnName);
					}
				}
			}
		}
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

}
