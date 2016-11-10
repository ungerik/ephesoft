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

package com.ephesoft.gxt.admin.client.widget;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassManagementConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox.ComboBoxCell;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.constant.ExpressionParameters;
import com.ephesoft.gxt.core.shared.dto.RuleInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.NativeEvent;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.core.client.dom.XElement;

/**
 * This View deals with Table Validation Rule Cell.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.widget.TableRuleCell
 */
public class TableRuleCell extends CompositeCell<RuleInfoDTO> {

	public TableRuleCell(final TableInfoDTO tableInfoDTO, final Grid<RuleInfoDTO> rulesGrid) {
		this(getCells(tableInfoDTO, rulesGrid));
	}

	private TableRuleCell(final List<HasCell<RuleInfoDTO, ?>> hasCells) {
		super(hasCells);
	}

	private static ComboBoxCell getColumnNameCell(final Grid<RuleInfoDTO> rulesGrid, final TableInfoDTO tableInfo) {
		final ComboBoxCell cell = new ComboBoxCell() {

			@Override
			protected void onTriggerClick(final com.google.gwt.cell.client.Cell.Context context, final XElement parent,
					final NativeEvent event, final String value, final ValueUpdater<String> updater) {
				if (null != context && null != rulesGrid) {
					final int rowIndex = context.getIndex();
					final RuleInfoDTO ruleInfoDTO = rulesGrid.getModel(rowIndex);
					if (null != ruleInfoDTO) {
						final String rule = ruleInfoDTO.getRule();
						if (StringUtil.isNullOrEmpty(rule) || ExpressionParameters.endsWithOperator(rule)) {
							super.onTriggerClick(context, parent, event, value, updater);
						}
					}
				}
			}

			@Override
			protected void onFocus(final com.google.gwt.cell.client.Cell.Context context, final XElement parent, final String value,
					final NativeEvent event, final ValueUpdater<String> valueUpdater) {
				super.onFocus(context, parent, value, event, valueUpdater);
				setEditable(parent, false);
			}

			@Override
			protected void onSelect(final String item) {
				super.onSelect(item);
				rulesGrid.reLoad();
			}
		};
		cell.getStore().addAll(getColumnNames(tableInfo));
		return cell;
	}

	private static ComboBoxCell getOperatorsCell(final Grid<RuleInfoDTO> rulesGrid, final TableInfoDTO tableInfo) {
		final ComboBoxCell cell = new ComboBoxCell() {

			@Override
			protected void onTriggerClick(final com.google.gwt.cell.client.Cell.Context context, final XElement parent,
					final NativeEvent event, final String value, final ValueUpdater<String> updater) {
				if (null != context && null != rulesGrid) {
					final int rowIndex = context.getIndex();
					final RuleInfoDTO ruleInfoDTO = rulesGrid.getModel(rowIndex);
					if (null != ruleInfoDTO) {
						final String rule = ruleInfoDTO.getRule();
						if (!StringUtil.isNullOrEmpty(rule) && !ExpressionParameters.endsWithOperator(rule)) {
							super.onTriggerClick(context, parent, event, value, updater);
						}
					}
				}
			}

			@Override
			protected void onFocus(final com.google.gwt.cell.client.Cell.Context context, final XElement parent, final String value,
					final NativeEvent event, final ValueUpdater<String> valueUpdater) {
				super.onFocus(context, parent, value, event, valueUpdater);
				setEditable(parent, false);
			}

			@Override
			protected void onSelect(final String item) {
				super.onSelect(item);
				rulesGrid.reLoad();
			}
		};
		cell.getStore().addAll(ExpressionParameters.getOperators());
		return cell;
	}

	private static List<String> getColumnNames(final TableInfoDTO tableInfo) {
		final List<String> columnNamesList = new LinkedList<String>();
		if (null != tableInfo) {
			final List<TableColumnInfoDTO> columnList = tableInfo.getColumnInfoDTOs();
			if (!CollectionUtil.isEmpty(columnList)) {
				for (final TableColumnInfoDTO tableColumn : columnList) {
					if (null != tableColumn) {
						columnNamesList.add(tableColumn.getColumnName());
					}
				}
			}
		}
		return columnNamesList;
	}

	private static List<HasCell<RuleInfoDTO, ?>> getCells(final TableInfoDTO tableInfoDTO, final Grid<RuleInfoDTO> rulesGrid) {
		final List<HasCell<RuleInfoDTO, ?>> list = new ArrayList<HasCell<RuleInfoDTO, ?>>();
		list.add(new ClearButtonCell(rulesGrid));
		list.add(new RuleComboBoxCell(getColumnNameCell(rulesGrid, tableInfoDTO), rulesGrid));
		list.add(new RuleComboBoxCell(getOperatorsCell(rulesGrid, tableInfoDTO), rulesGrid));
		return list;
	}

	private static class RuleComboBoxCell implements HasCell<RuleInfoDTO, String> {

		private final ComboBoxCell cell;
		private final Grid<RuleInfoDTO> rulesGrid;
		private static boolean doSelect = true;

		public RuleComboBoxCell(final ComboBoxCell comboBoxCell, final Grid<RuleInfoDTO> rulesGrid) {
			this.cell = comboBoxCell;
			this.rulesGrid = rulesGrid;
			cell.setForceSelection(true);
			cell.setFinishEditOnEnter(true);
		}

		@Override
		public ComboBoxCell getCell() {
			return cell;
		}

		@Override
		public FieldUpdater<RuleInfoDTO, String> getFieldUpdater() {
			return new FieldUpdater<RuleInfoDTO, String>() {

				@Override
				public void update(final int index, final RuleInfoDTO object, final String value) {
					/*
					 * doSelect is being added because the event is fired two times on refresh...
					 */
					if (null != object && doSelect) {
						if (!"Select".equalsIgnoreCase(value)) {
							String rule = object.getRule();
							if (StringUtil.isNullOrEmpty(rule)) {
								rule = CoreCommonConstant.EMPTY_STRING;
							}
							object.setRule(StringUtil.concatenate(rule, CoreCommonConstant.SPACE, CoreCommonConstant.AMPERSAND, value.toUpperCase()));
							rulesGrid.reLoad();
						}
					}
					doSelect = !doSelect;
				}
			};
		}

		@Override
		public String getValue(final RuleInfoDTO object) {
			return BatchClassManagementConstants.LABEL_SELECT;
		}

	}

	private static class ClearButtonCell implements HasCell<RuleInfoDTO, String> {

		private final ButtonCell<String> cell;

		private ClearButtonCell(final Grid<RuleInfoDTO> grid) {
			cell = getClearRuleCell(grid);
		}

		@Override
		public Cell<String> getCell() {
			return cell;
		}

		@Override
		public FieldUpdater<RuleInfoDTO, String> getFieldUpdater() {
			return null;
		}

		@Override
		public String getValue(final RuleInfoDTO object) {
			return LocaleDictionary.getConstantValue(BatchClassConstants.LABEL_CLEAR_BUTTON);
		}

		private static ButtonCell<String> getClearRuleCell(final Grid<RuleInfoDTO> rulesGrid) {
			final ButtonCell<String> cell = new ButtonCell<String>() {

				@Override
				protected void onClick(final com.google.gwt.cell.client.Cell.Context context, final XElement p, final String value,
						final NativeEvent event, final ValueUpdater<String> valueUpdater) {
					super.onClick(context, p, value, event, valueUpdater);
					if (null != context && null != rulesGrid) {
						final int rowIndex = context.getIndex();
						final RuleInfoDTO ruleInfoDTO = rulesGrid.getModel(rowIndex);
						if (ruleInfoDTO != null) {
							ruleInfoDTO.setRule(CoreCommonConstant.EMPTY_STRING);
							rulesGrid.reLoad();
						}
					}
				}

			};
			cell.setText(LocaleDictionary.getConstantValue(BatchClassConstants.LABEL_CLEAR_BUTTON));
			return cell;
		}

	}

}
