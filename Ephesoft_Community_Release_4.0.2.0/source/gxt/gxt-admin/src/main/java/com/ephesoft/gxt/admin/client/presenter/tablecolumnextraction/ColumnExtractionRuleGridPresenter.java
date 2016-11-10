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

package com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionHideEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionShowEvent;
import com.ephesoft.gxt.admin.client.event.ValidateColumnExtractionRuleEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.tablecolumnextraction.ColumnExtractionRuleGridView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

/**
 * This presenter deals with Table Column Extraction Rule Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction.ColumnExtractionRuleGridPresenter
 */
public class ColumnExtractionRuleGridPresenter extends BatchClassInlinePresenter<ColumnExtractionRuleGridView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<ColumnExtractionRuleGridPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Instantiates a new column extraction rule grid presenter.
	 * 
	 * @param controller the controller {@link BatchClassManagementController}
	 * @param view the view {@link ColumnExtractionRuleGridView}
	 */
	public ColumnExtractionRuleGridPresenter(final BatchClassManagementController controller, final ColumnExtractionRuleGridView view) {
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
			view.actionForColumnCells();
			view.loadGrid();
			view.setComponents(controller.getRegexBuilderView(), controller.getRegexGroupSelectionGridView());
		}
	}

	/**
	 * Sets the select table column extraction rule dto in controller.
	 * 
	 * @param cellSelectionChangeEvent the new select table column extraction rule {@link CellSelectionChangedEvent<
	 *            {@link TableColumnExtractionRuleDTO}>}
	 */
	public void setSelectColumnExtrRule(final CellSelectionChangedEvent<TableColumnExtractionRuleDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			final TableColumnExtractionRuleDTO selectedColumnExtrRule = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			if (selectedColumnExtrRule != null) {
				controller.setSelectedColumnExtrRule(selectedColumnExtrRule);
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
	 * Method to get the table column extraction rules.
	 * 
	 * @return the table column extraction rule dtos {@link Collection<{@link TableColumnExtractionRuleDTO}>}
	 */
	public Collection<TableColumnExtractionRuleDTO> getTableColumnRules() {
		final TableExtractionRuleDTO selectedTableExtrRule = controller.getSelectedTableExtrRule();
		Collection<TableColumnExtractionRuleDTO> columnRuleCollection = null;
		if (selectedTableExtrRule != null && !selectedTableExtrRule.isDeleted()) {
			columnRuleCollection = selectedTableExtrRule.getTableColumnExtractionRuleDTOs();
		}
		return columnRuleCollection;
	}

	/**
	 * Sets the values in column drop down.
	 * 
	 * @param combo to set the column values {@link SimpleComboBox<{@link String}>}
	 */
	public void setColumnValues(SimpleComboBox<String> combo) {
		List<String> columnList = new ArrayList<String>();
		TableColumnExtractionRuleDTO selectedColExtrRuleDTO = controller.getSelectedColumnExtrRule();
		if (selectedColExtrRuleDTO != null && selectedColExtrRuleDTO.getTableExtractionRuleDTO() != null
				&& selectedColExtrRuleDTO.getTableExtractionRuleDTO().getTableInfoDTO() != null) {
			getColumnsListForSelectedColumn(selectedColExtrRuleDTO, columnList);
			combo.getStore().addAll(columnList);
		}
	}

	/**
	 * Gets the columns list for selected table column.
	 * 
	 * @param selectedColExtrRuleDTO the selected table column extraction rule dto {@link TableColumnExtractionRuleDTO}
	 * @param columnList the column list {@link List<{@link String}>}
	 * @return the columns list for selected column {@link List<{@link String}>}
	 */
	public List<String> getColumnsListForSelectedColumn(TableColumnExtractionRuleDTO selectedColExtrRuleDTO, List<String> columnList) {
		String selectedColName = selectedColExtrRuleDTO.getColumnName();
		TableExtractionRuleDTO tableExtrRuleDTO = selectedColExtrRuleDTO.getTableExtractionRuleDTO();
		if (tableExtrRuleDTO != null) {
			TableInfoDTO tableInfoDTO = tableExtrRuleDTO.getTableInfoDTO();
			if (tableInfoDTO != null && !CollectionUtil.isEmpty(tableInfoDTO.getColumnInfoDTOs())) {
				List<TableColumnInfoDTO> columnRules = tableInfoDTO.getColumnInfoDTOs();
				for (TableColumnInfoDTO columnInfo : columnRules) {
					if (!selectedColName.equalsIgnoreCase(columnInfo.getColumnName())) {
						columnList.add(columnInfo.getColumnName());
					}
				}

			}
		}
		return columnList;
	}

	/**
	 * Action on column combo selection.
	 */
	public void actionOnColumnSelection() {
		boolean isMandatory = false;
		TableColumnExtractionRuleDTO selectedColExtrRuleDTO = controller.getSelectedColumnExtrRule();
		if (null != selectedColExtrRuleDTO) {
			isMandatory = selectedColExtrRuleDTO.isMandatory();
			if (isMandatory) {
				selectedColExtrRuleDTO.setMandatory(false);
				Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_COLUMN_EXTRACTION_RULE),
						LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_EXTRACT_COLUMN_MANDATORY_OPTION_SET));
			}
		}
	}

	/**
	 * Action on mandatory field selection.
	 */
	public void actionOnMandatorySelection() {
		boolean isExtractDataFromOtherColumn = false;
		boolean isRequired = false;
		TableColumnExtractionRuleDTO selectedColExtrRuleDTO = controller.getSelectedColumnExtrRule();
		if (null != selectedColExtrRuleDTO) {
			isExtractDataFromOtherColumn = !StringUtil.isNullOrEmpty(selectedColExtrRuleDTO.getExtractedDataColumnName());
			isRequired = selectedColExtrRuleDTO.isRequired();
			if (isRequired && isExtractDataFromOtherColumn) {
				selectedColExtrRuleDTO.setRequired(false);
				selectedColExtrRuleDTO.setExtractedDataColumnName(null);
				Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_COLUMN_EXTRACTION_RULE),
						LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_MANDATORY_OPTION_EXTRACT_COLUMN_AND_REQUIRED_SET));
			} else if (isRequired) {
				selectedColExtrRuleDTO.setRequired(false);
				Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_COLUMN_EXTRACTION_RULE),
						LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_MANDATORY_OPTION_REQUIRED_SET));
			} else if (isExtractDataFromOtherColumn) {
				selectedColExtrRuleDTO.setExtractedDataColumnName(null);
				Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_COLUMN_EXTRACTION_RULE),
						LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_MANDATORY_OPTION_EXTRACT_COLUMN_SET));
			}
		}
	}

	/**
	 * Action on required field selection.
	 */
	public void actionOnRequiredSelection() {
		boolean isMandatory = false;
		TableColumnExtractionRuleDTO selectedColExtrRuleDTO = controller.getSelectedColumnExtrRule();
		if (null != selectedColExtrRuleDTO) {
			isMandatory = selectedColExtrRuleDTO.isMandatory();
			if (isMandatory) {
				selectedColExtrRuleDTO.setMandatory(false);
				Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_COLUMN_EXTRACTION_RULE),
						LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_REQUIRED_OPTION_MANDATORY_SET));
			}
		}
	}

	/**
	 * Method to set the batch class dirty on change.
	 * 
	 */
	public void setBatchClassDirtyOnChange() {
		controller.getSelectedBatchClass().setDirty(true);
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
	public void validateViewAndOpenSetCoordinates(ValidateColumnExtractionRuleEvent event) {
		if (isValid()) {
			view.getColExtrGrid().getStore().commitChanges();
			TableColumnExtractionRuleDTO columnExtractionDTO = controller.getSelectedTableColumnExtractionRuleDTO();
			if(null!=columnExtractionDTO){
			openColumnExtractionSetCoordinateView(columnExtractionDTO);
			}else{
				showMessage(BatchClassMessages.NO_ROW_SELECTED_SET_COORDINATE);
			}
		} else {
			showMessage(BatchClassMessages.INVALID_VIEW);
		}

	}

	private void showMessage(String messageValue) {
		String message = LocaleDictionary.getMessageValue(messageValue);
//		if (!messageValue.equals(BatchClassMessages.INVALID_VIEW)) {
//			message = StringUtil.concatenate(message, LocaleDictionary.getMessageValue(BatchClassMessages.EDIT_SET_COORDINATES));
//		}
		DialogUtil
				.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE), message, DialogIcon.WARNING);
	}

	private void openColumnExtractionSetCoordinateView(TableColumnExtractionRuleDTO extractionRule) {
		controller.getEventBus().fireEvent(new AdvancedTableColumnExtractionShowEvent());
	}

	@EventHandler
	public void reloadGridOnSetCoordinateApply(AdvancedTableColumnExtractionHideEvent event) {
		if (event.isWindowHide() && event.isClearOverlays()) {
			view.reLoadGrid();
		}
	}
	

}
