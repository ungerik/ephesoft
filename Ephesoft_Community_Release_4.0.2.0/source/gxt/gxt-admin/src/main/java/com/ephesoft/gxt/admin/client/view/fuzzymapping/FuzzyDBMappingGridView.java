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

package com.ephesoft.gxt.admin.client.view.fuzzymapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.fuzzymapping.FuzzyDBGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.util.UniqueIDProvider;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FuzzyDBDTO;
import com.ephesoft.gxt.core.shared.dto.FuzzyDBMappingDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.FuzzyDBMappingProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.FuzzyDBMappingProperties.MappingPropertiesValueProvider.FieldNameValueProvider;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.CheckBoxCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.StringComboBox;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

public class FuzzyDBMappingGridView extends BatchClassInlineView<FuzzyDBGridPresenter> implements HasResizableGrid {

	@UiField(provided = true)
	protected ComboBox<ConnectionsDTO> connectionCombo;

	@UiField(provided = true)
	protected ComboBox<String> tableNameCombo;

	@UiField(provided = true)
	protected ComboBox<String> rowIDCombo;

	@UiField
	protected VerticalLayoutContainer gridContainer;

	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	protected Grid<FuzzyDBMappingDTO> indexFieldMappingGrid;

	private final Map<String, Map<String, String>> tableWithAllColumnsMap;

	private final StringComboBox columnNameCombo;

	private Map<String, Boolean> columnsRetreivedMap = null;

	private final StringComboBox fieldNamesCombo;

	private String currentTableName;

	private FuzzyDBDTO selectedFuzzyDBDTO;

	private FuzzyDBMappingDTO selectedMappingDTO;

	private List<String> listOfIndexFieldNames;

	interface Binder extends UiBinder<Widget, FuzzyDBMappingGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public FuzzyDBMappingGridView() {
		initWidget(binder.createAndBindUi(this));
		tableWithAllColumnsMap = new HashMap<String, Map<String, String>>();
		columnsRetreivedMap = new HashMap<String, Boolean>();
		fieldNamesCombo = new StringComboBox();
		fieldNamesCombo.setTriggerAction(TriggerAction.ALL);
		fieldNamesCombo.setEditable(false);
		addFieldNamesSelectionHandler();
		indexFieldMappingGrid = getIndexFieldGrid();
		indexFieldMappingGrid.getStore().commitChanges();
		indexFieldMappingGrid.finishEditing();
		indexFieldMappingGrid.setHasPagination(false);
		columnNameCombo = new StringComboBox();
		columnNameCombo.setTriggerAction(TriggerAction.ALL);
		indexFieldMappingGrid.addEditor(FuzzyDBMappingProperties.INSTANCE.columnName(), columnNameCombo);
		indexFieldMappingGrid.addSelectAllFunctionality(FuzzyDBMappingProperties.INSTANCE.selected());
		indexFieldMappingGrid.addEditor(FieldNameValueProvider.getInstance(), fieldNamesCombo);
		indexFieldMappingGrid.addValidators(FieldNameValueProvider.getInstance(), new EmptyValueValidator<FuzzyDBMappingDTO>());
		indexFieldMappingGrid.addValidators(FuzzyDBMappingProperties.INSTANCE.columnName(),
				new EmptyValueValidator<FuzzyDBMappingDTO>());
		gridContainer.add(indexFieldMappingGrid);
		indexFieldMappingGrid.getStore().setAutoCommit(true);
		gridContainer.addStyleName("gridViewMainPanel");
		indexFieldMappingGrid.addStyleName("gridView");
		PagingLoader<FilterPagingLoadConfig, PagingLoadResult<FuzzyDBMappingDTO>> loader = indexFieldMappingGrid.getPaginationLoader();
		pagingToolbar.bind(loader);
		addSelectionChangeHandler();
	}

	private void addFieldNamesSelectionHandler() {
		fieldNamesCombo.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(final SelectionEvent<String> event) {
				final String selectedItem = event.getSelectedItem();
				final FieldTypeDTO selectedFieldTypeDTO = presenter.getSelectedFieldTypeByName(selectedItem);

				if (null != selectedFieldTypeDTO && selectedMappingDTO != null) {
					selectedMappingDTO.setBindedField(selectedFieldTypeDTO);
					selectedMappingDTO.setColumnName("");
					selectedMappingDTO.setNew(true);
				}
				setFieldTypes();
				setCompatibileDBColumns(selectedFieldTypeDTO);
			}

			private void setCompatibileDBColumns(final FieldTypeDTO selectedFieldTypeDTO) {
				// String columnDataType = controller.getColumnsMap().get(fields.getItemText(fields.getSelectedIndex()));
				String fieldDataType = convertFieldType(selectedFieldTypeDTO.getDataType().toString());

				Map<String, String> allColumnsWithType = tableWithAllColumnsMap.get(tableNameCombo.getValue());
				Collection<String> allColumns = allColumnsWithType.keySet();
				List<String> filteredColumns = new ArrayList<String>();
				for (String columnName : allColumns) {
					if (!StringUtil.isNullOrEmpty(columnName)) {
						String columnType = allColumnsWithType.get(columnName);
						if (columnType.contains(fieldDataType)) {
							filteredColumns.add(columnName);
						}
					}
				}
				setColumsForField(filteredColumns);
			}
		});
	}

	private String convertFieldType(String inputFieldType) {
		String fieldType = inputFieldType;
		if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_DATE)) {
			fieldType = AdminConstants.JAVA_TYPE_DATE;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_STRING)) {
			fieldType = AdminConstants.JAVA_TYPE_STRING;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_LONG)) {
			fieldType = AdminConstants.JAVA_TYPE_LONG;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_DOUBLE)) {
			fieldType = AdminConstants.JAVA_TYPE_DOUBLE;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_INTEGER)) {
			fieldType = AdminConstants.JAVA_TYPE_INTEGER;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_FLOAT)) {
			fieldType = AdminConstants.JAVA_TYPE_FLOAT;
		} else if (fieldType.equalsIgnoreCase(AdminConstants.DATABASE_TYPE_BIGDECIMAL)) {
			fieldType = AdminConstants.JAVA_TYPE_BIGDECIMAL;
		}
		return fieldType;
	}

	private Grid<FuzzyDBMappingDTO> getIndexFieldGrid() {
		return new Grid<FuzzyDBMappingDTO>(PropertyAccessModel.FUZZY_DB_MAPPING) {

			@SuppressWarnings("rawtypes")
			@Override
			public void completeEditing(final CompleteEditEvent<FuzzyDBMappingDTO> completeEditEvent) {
				super.completeEditing(completeEditEvent);
				final GridCell editCell = completeEditEvent.getEditCell();
				if (null != editCell) {
					indexFieldMappingGrid.getStore().commitChanges();
					presenter.getController().getSelectedBatchClass().setDirty(true);
					final FuzzyDBMappingDTO currentSelectedModel = getCurrentSelectedModel();
					if (indexFieldMappingGrid.isGridValidated()) {
						presenter.setSaveModelInDTO(currentSelectedModel);
						currentSelectedModel.setNew(true);

					}
					super.completeEditing(completeEditEvent);

				}
			}

			@Override
			protected void onCellSelectionChange(final FuzzyDBMappingDTO selectedModel) {
				super.onCellSelectionChange(selectedModel);
				selectedMappingDTO = getCurrentSelectedModel();
			}

		};
	}

	@Override
	protected void onLoad() {
		super.onLoad();
	}

	private void getColumns(final String tableName) {
		final ConnectionsDTO connection = connectionCombo.getValue();
		final Map<String, String> columnWithDataTypeMap = tableWithAllColumnsMap.get(tableName);
		final Collection<String> columnNames = columnWithDataTypeMap.values();
		if (CollectionUtil.isEmpty(columnNames)) {
			presenter.setColumns(connection, tableName);
		}
		presenter.setColumnsPrimaryKey(connection, tableName);
	}

	@Override
	public void initialize() {
		super.initialize();

		connectionCombo = new ComboBox<ConnectionsDTO>(CollectionUtil.createListStore(new UniqueIDProvider<ConnectionsDTO>()),
				new LabelProvider<ConnectionsDTO>() {

					@Override
					public String getLabel(final ConnectionsDTO item) {
						String label = CoreCommonConstant.EMPTY_STRING;
						if (null != item) {
							label = item.getConnectionName();
						}
						return label;
					}
				});
		connectionCombo.setEditable(false);
		connectionCombo.setTriggerAction(TriggerAction.ALL);
		tableNameCombo = new ComboBox<String>(CollectionUtil.createListStore(new UniqueIDProvider<String>()),
				new LabelProvider<String>() {

					@Override
					public String getLabel(final String item) {
						String label = CoreCommonConstant.EMPTY_STRING;
						if (null != item) {
							label = item;
						}
						return label;
					}
				});
		tableNameCombo.setEditable(false);
		tableNameCombo.setTriggerAction(TriggerAction.ALL);
		rowIDCombo = new ComboBox<String>(CollectionUtil.createListStore(new UniqueIDProvider<String>()), new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				String label = CoreCommonConstant.EMPTY_STRING;
				if (null != item) {
					label = item;
				}
				return label;
			}
		});
		rowIDCombo.setEditable(false);
		rowIDCombo.setTriggerAction(TriggerAction.ALL);
		pagingToolbar = new PagingToolbar(15, indexFieldMappingGrid);

	}

	public void setConnectionsList(final Collection<ConnectionsDTO> dtoConnectionList) {
		connectionCombo.getStore().clear();
		connectionCombo.getStore().addAll(dtoConnectionList);
	}

	private void addSelectionChangeHandler() {
		connectionCombo.addBeforeSelectionHandler(new BeforeSelectionHandler<ConnectionsDTO>() {

			@Override
			public void onBeforeSelection(final BeforeSelectionEvent<ConnectionsDTO> event) {
				final ConnectionsDTO connection = event.getItem();
				if (presenter.getCurrentConnection() == connection) {
					event.cancel();
				} else {
					if (indexFieldMappingGrid.getStore().size() > 0) {
						event.cancel();
						ConfirmationDialog dialog = DialogUtil.showConfirmationDialog(
								LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
								LocaleDictionary.getMessageValue(BatchClassMessages.SURE_YOU_WANT_TO_RESET_FUZZY_DB_MAPPINGS));
						dialog.setDialogListener(new DialogAdapter() {

							@Override
							public void onOkClick() {
								presenter.deleteAllMappings();
								clearComboStores(true, true, false, false, false);
								setConnection(connection);
							}

							@Override
							public void onCloseClick() {
								event.cancel();
							}

							@Override
							public void onCancelClick() {
								event.cancel();
							}
						});
					}
				}
			}
		});
		connectionCombo.addSelectionHandler(new SelectionHandler<ConnectionsDTO>() {

			@Override
			public void onSelection(final SelectionEvent<ConnectionsDTO> event) {
				final ConnectionsDTO connection = event.getSelectedItem();
				connectionCombo.setValue(connection, true, true);
			}
		});
		connectionCombo.addValueChangeHandler(new ValueChangeHandler<ConnectionsDTO>() {

			@Override
			public void onValueChange(ValueChangeEvent<ConnectionsDTO> event) {
				// TODO Auto-generated method stub
				final ConnectionsDTO connection = event.getValue();
				presenter.setConnection(connection);
				presenter.setTables(connection);
			}
		});

		tableNameCombo.addBeforeSelectionHandler(new BeforeSelectionHandler<String>() {

			@Override
			public void onBeforeSelection(final BeforeSelectionEvent<String> event) {
				final String value = event.getItem();
				final String previouslySelectedTableName = tableNameCombo.getValue();
				if (!StringUtil.isNullOrEmpty(previouslySelectedTableName) && !previouslySelectedTableName.equals(value)) {
					if (indexFieldMappingGrid.getStore().size() > 0) {
						event.cancel();
						ConfirmationDialog dialog = DialogUtil.showConfirmationDialog(
								LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
								LocaleDictionary.getMessageValue(BatchClassMessages.SURE_YOU_WANT_TO_RESET_FUZZY_DB_MAPPINGS));
						dialog.setDialogListener(new DialogAdapter() {

							@Override
							public void onOkClick() {
								presenter.deleteAllMappings();
								clearComboStores(true, false, false, false, false);
								setSelectedTable(value);
							}

							@Override
							public void onCloseClick() {
								event.cancel();
							}

							@Override
							public void onCancelClick() {
								event.cancel();
							}
						});
					}
				}
			}
		});

		tableNameCombo.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(SelectionEvent<String> event) {
				String value = event.getSelectedItem();
				setSelectedTable(value);
			}
		});
		tableNameCombo.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				final ListStore<String> store = tableNameCombo.getStore();
				if (store != null) {
					final String value = event.getValue();
					final int totalItems = store.size();
					for (int index = 0; index < totalItems; index++) {
						final String item = store.get(index);
						if (null != item && item.equalsIgnoreCase(value)) {
							getColumns(item);
							tableNameCombo.setValue(item);
							break;
						}
					}
				}
			}
		});

		columnNameCombo.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(SelectionEvent<String> event) {
				columnNameCombo.setValue(event.getSelectedItem(), true, true);
				// columnNameCombo.focus();
				columnNameCombo.finishEditing();
			}
		});

		rowIDCombo.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(SelectionEvent<String> event) {
				String value = event.getSelectedItem();
				rowIDCombo.setValue(value, true, true);
			}
		});
	}

	public void addNonDeletedMappingsToGrid(final List<FuzzyDBMappingDTO> mapping) {
		for (FuzzyDBMappingDTO mappingDTO : mapping) {
			if (!mappingDTO.isDeleted()) {
				indexFieldMappingGrid.getStore().add(mappingDTO);
			}
		}
	}

	public void setFieldTypes() {
		fieldNamesCombo.getStore().clear();
		List<FuzzyDBMappingDTO> mappingsInGrid = indexFieldMappingGrid.getStore().getAll();
		if (CollectionUtil.isEmpty(mappingsInGrid)) {
			fieldNamesCombo.getStore().addAll(listOfIndexFieldNames);
		} else {
			List<String> filteredIndexFields = new ArrayList<String>();
			filteredIndexFields.addAll(listOfIndexFieldNames);
			for (FuzzyDBMappingDTO mappings : mappingsInGrid) {
				FieldTypeDTO selectedMappingIndexField = mappings.getBindedField();
				if (null != selectedMappingIndexField) {
					String existingIndexFieldName = selectedMappingIndexField.getName();
					if (filteredIndexFields.contains(existingIndexFieldName)) {
						filteredIndexFields.remove(existingIndexFieldName);
					}
				}
			}
			fieldNamesCombo.getStore().addAll(filteredIndexFields);
		}
	}

	public void setConnection(final ConnectionsDTO connection) {
		if (null != connection) {
			connectionCombo.setText(connection.getConnectionName());
			connectionCombo.setValue(connection, true, true);
			connectionCombo.finishEditing();
		}
	}

	public void setTables(final List<String> tableNames) {
		tableNameCombo.getStore().clear();
		if (!CollectionUtil.isEmpty(tableNames)) {
			tableNameCombo.getStore().addAll(tableNames);
			for (final String tableName : tableNames) {
				tableWithAllColumnsMap.put(tableName, new HashMap<String, String>());
			}
		}
	}

	public void addMappingForColumn(final String tableName, final Map<String, String> result) {
		if (!StringUtil.isNullOrEmpty(tableName) && !CollectionUtil.isEmpty(result)) {
			tableWithAllColumnsMap.put(tableName, result);
		}
		if (!StringUtil.isNullOrEmpty(tableName)) {
			columnsRetreivedMap.put(tableName, true);
		}
		if (!StringUtil.isNullOrEmpty(tableName) && tableName.equalsIgnoreCase(currentTableName)) {
			currentTableName = tableName;
		}
	}

	public void setRowIDPrimaryKeyColumns(final Collection<String> columnsList) {
		rowIDCombo.getStore().clear();
		if (!CollectionUtil.isEmpty(columnsList)) {
			rowIDCombo.getStore().addAll(columnsList);
		}
	}

	public void setColumsForField(final Collection<String> columnsList) {
		columnNameCombo.clear();
		columnNameCombo.getStore().clear();
		if (!CollectionUtil.isEmpty(columnsList)) {
			columnNameCombo.getStore().addAll(columnsList);
		}
	}

	public void addNewFuzzyMapping(final FuzzyDBMappingDTO mappingDTO) {
		if (null != mappingDTO) {
			indexFieldMappingGrid.addNewItemInGrid(mappingDTO);
		}
	}

	public List<FuzzyDBMappingDTO> getSelectedModelsFromGrid() {
		return indexFieldMappingGrid.getSelectedModels();
	}

	public void removeModelsFromGrid(List<FuzzyDBMappingDTO> selectedModels) {
		if (!CollectionUtil.isEmpty(selectedModels)) {
			for (FuzzyDBMappingDTO mappingDto : selectedModels) {
				indexFieldMappingGrid.getStore().remove(mappingDto);
			}
		}
	}

	public void clearComboStores(boolean clearRowIDCombo, boolean clearTableNameCombo, boolean clearColumnNameCombo,
			boolean clearFields, boolean clearConnections) {
		if (clearRowIDCombo) {
			rowIDCombo.getStore().clear();
			rowIDCombo.setValue("", true, true);
		}
		if (clearTableNameCombo) {
			tableNameCombo.getStore().clear();
			tableNameCombo.setValue("", true, true);
		}
		if (clearColumnNameCombo) {
			columnNameCombo.getStore().clear();
			columnNameCombo.setValue("", true, true);
		}
		if (clearFields) {
			fieldNamesCombo.getStore().clear();
			fieldNamesCombo.setValue("", true, true);
		}
		if (clearConnections) {
			connectionCombo.getStore().clear();
			connectionCombo.setValue(null, true, true);
		}
	}

	public void setSelectedTable(final String tableName) {
		if (!StringUtil.isNullOrEmpty(tableName)) {
			List<String> allTables = tableNameCombo.getStore().getAll();
			if (!CollectionUtil.isEmpty(allTables) && allTables.contains(tableName)) {
				tableNameCombo.setText(tableName);
				tableNameCombo.setValue(tableName, true, true);
				tableNameCombo.finishEditing();
			}
		}
	}

	public void setSelectedRowIDColumn(final String columnName) {
		if (!StringUtil.isNullOrEmpty(columnName)) {
			List<String> allColumns = rowIDCombo.getStore().getAll();
			if (!CollectionUtil.isEmpty(allColumns) && allColumns.contains(columnName)) {
				rowIDCombo.setText(columnName);
				rowIDCombo.setValue(columnName, true, true);
				rowIDCombo.finishEditing();
			}
		}
	}

	public FuzzyDBDTO getSelectedFuzzyDBDTO() {
		return selectedFuzzyDBDTO;
	}

	public void setSelectedFuzzyDBDTO(FuzzyDBDTO selectedFuzzyDBDTO) {
		this.selectedFuzzyDBDTO = selectedFuzzyDBDTO;
	}

	public ConnectionsDTO getSelectedConnectionFromCombobox() {
		return connectionCombo.getValue();
	}

	public String getSelectedTableNamefromCombobox() {
		return tableNameCombo.getValue();
	}

	public String getSelectedRowIDFromCombobox() {
		return rowIDCombo.getValue();
	}

	@Override
	public Grid getGrid() {
		return indexFieldMappingGrid;
	}

	public void clear() {
		indexFieldMappingGrid.getStore().clear();
		tableNameCombo.getStore().clear();
		rowIDCombo.getStore().clear();
		columnNameCombo.getStore().clear();

		columnNameCombo.clear();
		fieldNamesCombo.clear();
		connectionCombo.clear();
		tableNameCombo.clear();
		rowIDCombo.clear();
	}

	public void clearConnectionCombo() {
		connectionCombo.clear();
	}

	public boolean isValid() {
		return indexFieldMappingGrid.isGridValidated();
	}

	/**
	 * Gets the paging tool bar.
	 * 
	 * @return the paging tool bar {@link PagingToolbar}
	 */
	public PagingToolbar getPagingToolbar() {
		return pagingToolbar;
	}

	public List<String> getListOfIndexFieldNames() {
		return listOfIndexFieldNames;
	}

	public void setListOfIndexFieldNames(List<String> indexFieldNames) {
		this.listOfIndexFieldNames = indexFieldNames;
	}

	public void actionForColumnCells() {
		CheckBoxCell multilineAnchorCell = new CheckBoxCell() {

			@Override
			public void onBrowserEvent(Context context, Element parent, Boolean value, NativeEvent event,
					ValueUpdater<Boolean> valueUpdater) {

				super.onBrowserEvent(context, parent, value, event, valueUpdater);
				final InputElement input = getInputElement(parent);
				if (null != event.getType() && "click".equals(event.getType())) {
					FuzzyDBMappingDTO currentSelectedModel = indexFieldMappingGrid.getCurrentSelectedModel();
					if (indexFieldMappingGrid.isGridValidated()) {
						presenter.setSaveModelInDTO(currentSelectedModel);
						presenter.getController().getSelectedBatchClass().setDirty(true);
						currentSelectedModel.setNew(true);
					}
					presenter.getController().getSelectedBatchClass().setDirty(true);
					if (!presenter.validateSearchableRows()) {
						input.setChecked(true);
						if(null != currentSelectedModel) {
							currentSelectedModel.setSearchable(true);
							indexFieldMappingGrid.reLoad();
						}
					}
				} else {
					super.onBrowserEvent(context, parent, value, event, valueUpdater);
				}
			}
		};
		indexFieldMappingGrid.setCell(FuzzyDBMappingProperties.INSTANCE.searchable(), multilineAnchorCell);
	}
}
