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

package com.ephesoft.gxt.admin.client.view.mapping;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.ephesoft.gxt.core.client.RandomIdGenerator;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.OptionalParametersRenderEvent;
import com.ephesoft.gxt.admin.client.event.OptionalParametersVisibilityEvent;
import com.ephesoft.gxt.admin.client.event.TableOptionalParametersRenderEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.mapping.DatabaseExportEditPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.util.UniqueIDProvider;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.DatabaseMappingDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.IndexFieldDBExportMappingDTO;
import com.ephesoft.gxt.core.shared.dto.IndexFieldDBExportMappingDTO.IndexFieldOptionalParametersDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnDBExportMappingDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnDBExportMappingDTO.TableColumnOptionalParametersDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.IndexFieldMappingProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.IndexFieldMappingProperties.MappingPropertiesValueProvider.FieldNameValueProvider;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.TableColumnDBExportMappingProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.TableColumnDBExportMappingProperties.TableColumnNameValueProvider;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.TableColumnDBExportMappingProperties.TableNameValueProvider;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.StringComboBox;

public class DatabaseExportMappingEditView extends BatchClassInlineView<DatabaseExportEditPresenter> implements HasResizableGrid {

	@UiField(provided = true)
	protected ComboBox<ConnectionsDTO> connectionCombo;

	@UiField
	protected VerticalLayoutContainer gridContainer;

	protected Grid<IndexFieldDBExportMappingDTO> indexFieldMappingGrid;

	protected Grid<TableColumnDBExportMappingDTO> tableGrid;

	private final Map<String, Collection<String>> tableColumnsMap;

	private final StringComboBox tableNameCombo;

	private final StringComboBox tableInfoCombo;

	private final StringComboBox tableColInfoCombo;

	private final StringComboBox columnNameCombo;

	private Map<String, Boolean> columnsRetreivedMap = null;

	private IndexFieldDBExportMappingDTO indexFieldMap;

	private TableColumnDBExportMappingDTO tableColMapping;

	private final StringComboBox fieldNamesCombo;

	private String currentTableName;

	interface Binder extends UiBinder<Widget, DatabaseExportMappingEditView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public DatabaseExportMappingEditView() {
		initWidget(binder.createAndBindUi(this));
		tableColumnsMap = new HashMap<String, Collection<String>>();
		columnsRetreivedMap = new HashMap<String, Boolean>();
		addSelectionChangeHandler();
		tableInfoCombo = new StringComboBox();
		tableColInfoCombo = new StringComboBox();
		fieldNamesCombo = new StringComboBox();
		addFieldNamesSelectionHandler();
		indexFieldMappingGrid = getIndexFieldGrid();
		tableGrid = getTableColumnsExportMappingGrid();
		indexFieldMappingGrid.setHasPagination(false);
		tableGrid.setHasPagination(false);
		tableNameCombo = new StringComboBox();
		indexFieldMappingGrid.setHasPagination(false);
		columnNameCombo = new StringComboBox();
		this.addValueChangeHandler();
		columnNameCombo.setTriggerAction(TriggerAction.ALL);
		tableGrid.addSelectAllFunctionality(TableColumnDBExportMappingProperties.INSTANCE.selected());
		tableGrid.addEditor(TableColumnDBExportMappingProperties.INSTANCE.tableName(), tableNameCombo);
		tableGrid.addEditor(TableColumnDBExportMappingProperties.INSTANCE.columnName(), columnNameCombo);
		tableGrid.addEditor(TableNameValueProvider.INSTANCE, tableInfoCombo);
		tableGrid.addEditor(TableColumnNameValueProvider.INSTANCE, tableColInfoCombo);
		tableGrid.addValidators(TableColumnDBExportMappingProperties.INSTANCE.tableName(),
				new EmptyValueValidator<TableColumnDBExportMappingDTO>());
		tableGrid.addValidators(TableColumnDBExportMappingProperties.INSTANCE.columnName(),
				new EmptyValueValidator<TableColumnDBExportMappingDTO>());
		tableGrid.addValidators(TableNameValueProvider.INSTANCE, new EmptyValueValidator<TableColumnDBExportMappingDTO>());
		tableGrid.addValidators(TableColumnNameValueProvider.INSTANCE, new EmptyValueValidator<TableColumnDBExportMappingDTO>());

		indexFieldMappingGrid.addEditor(IndexFieldMappingProperties.INSTANCE.tableName(), tableNameCombo);
		indexFieldMappingGrid.addEditor(IndexFieldMappingProperties.INSTANCE.columnName(), columnNameCombo);
		indexFieldMappingGrid.addSelectAllFunctionality(IndexFieldMappingProperties.INSTANCE.selected());
		indexFieldMappingGrid.addEditor(FieldNameValueProvider.getInstance(), fieldNamesCombo);
		indexFieldMappingGrid.addValidators(FieldNameValueProvider.getInstance(),
				new EmptyValueValidator<IndexFieldDBExportMappingDTO>());
		indexFieldMappingGrid.addValidators(IndexFieldMappingProperties.INSTANCE.tableName(),
				new EmptyValueValidator<IndexFieldDBExportMappingDTO>());
		indexFieldMappingGrid.addValidators(IndexFieldMappingProperties.INSTANCE.columnName(),
				new EmptyValueValidator<IndexFieldDBExportMappingDTO>());
		// gridContainer.add(indexFieldMappingGrid);
		gridContainer.add(tableGrid);
		tableInfoSelectionHandler();
		fieldNamesCombo.setEditable(false);
		fieldNamesCombo.setTriggerAction(TriggerAction.ALL);
		tableInfoCombo.setTriggerAction(TriggerAction.ALL);
		tableColInfoCombo.setTriggerAction(TriggerAction.ALL);
		tableNameCombo.setTriggerAction(TriggerAction.ALL);
		indexFieldMappingGrid.addStyleName("gridView");
		tableGrid.addStyleName("gridView");
		connectionCombo.setTriggerAction(TriggerAction.ALL);
	}

	private void tableInfoSelectionHandler() {
		tableInfoCombo.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(SelectionEvent<String> event) {
				String selectedItem = event.getSelectedItem();
				if (!StringUtil.isNullOrEmpty(selectedItem)) {
					TableColumnDBExportMappingDTO currentSelectedModel = tableGrid.getCurrentSelectedModel();
					if (null != currentSelectedModel) {
						currentSelectedModel.setBindedTableName(selectedItem);
						currentSelectedModel.setBindedTableColumn(null);
						tableGrid.getStore().commitChanges();
					}
				}
			}
		});
	}

	private void addFieldNamesSelectionHandler() {
		fieldNamesCombo.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(final SelectionEvent<String> event) {
				final String selectedItem = event.getSelectedItem();
				final FieldTypeDTO selectedFieldTypeDTO = presenter.getSelectedFieldTypeByName(selectedItem);
				if (null != indexFieldMap) {
					final FieldTypeDTO bindedField = indexFieldMap.getBindedField();
					if (null != bindedField) {
						bindedField.remove(indexFieldMap);
					}
				}
				if (null != selectedFieldTypeDTO && indexFieldMap != null) {
					indexFieldMap.setBindedField(selectedFieldTypeDTO);
					indexFieldMap.setNew(true);
					indexFieldMap.setId(RandomIdGenerator.getIdentifier());
					selectedFieldTypeDTO.add(indexFieldMap);
				}
			}
		});
	}

	private Grid<IndexFieldDBExportMappingDTO> getIndexFieldGrid() {
		return new Grid<IndexFieldDBExportMappingDTO>(PropertyAccessModel.INDEX_FIELD_MAPPING) {

			@SuppressWarnings("rawtypes")
			@Override
			public void completeEditing(final CompleteEditEvent<IndexFieldDBExportMappingDTO> completeEditEvent) {
				super.completeEditing(completeEditEvent);
				final com.sencha.gxt.widget.core.client.grid.Grid.GridCell editCell = completeEditEvent.getEditCell();
				presenter.setBatchClassDirty();
				if (null != editCell) {
					final int editCol = editCell.getCol();
					final ValueProvider valueProvider = cm.getValueProvider(editCol);
					final IndexFieldDBExportMappingDTO currentSelectedModel = store.get(editCell.getRow());
					if (valueProvider == FieldNameValueProvider.getInstance()) {
						presenter.setFieldType(currentSelectedModel, fieldNamesCombo.getText());
					}
					String tableName;
					if (null != currentSelectedModel && valueProvider == IndexFieldMappingProperties.INSTANCE.tableName()) {
						tableName = currentSelectedModel.getTableName();
						indexFieldMappingGrid.getStore().commitChanges();
						String newTableName = currentSelectedModel.getTableName();
						tableName = tableName == null ? CoreCommonConstant.EMPTY_STRING : tableName;
						if (!tableName.equalsIgnoreCase(newTableName)) {
							List<IndexFieldOptionalParametersDTO> optionalParamsList = getNotEqualOptionalParams(currentSelectedModel);
							setOptionalParams(currentSelectedModel, optionalParamsList);
						}
					} else {
						Timer timer = new Timer() {

							@Override
							public void run() {
								indexFieldMappingGrid.getStore().commitChanges();
							}
						};
						timer.schedule(100);
					}
					BatchClassManagementEventBus.fireEvent(new OptionalParametersVisibilityEvent(indexFieldMappingGrid
							.isValid(currentSelectedModel)));
					BatchClassManagementEventBus.fireEvent(new OptionalParametersRenderEvent(currentSelectedModel, store.getAll(),
							columnNameCombo.getStore().getAll()));
				}
			}

			@Override
			protected void onBeforeCellSelectionChange(BeforeSelectionEvent<IndexFieldDBExportMappingDTO> event,
					IndexFieldDBExportMappingDTO model) {
				super.onBeforeCellSelectionChange(event, model);
				IndexFieldDBExportMappingDTO newModel = event.getItem();
				if (!presenter.isOptionalViewValid() && newModel != model) {
					event.cancel();
				}
			}

			public void setOptionalParams(final IndexFieldDBExportMappingDTO model,
					final List<IndexFieldOptionalParametersDTO> optionalParamsList) {
				model.clearOptionalParams();
				if (!CollectionUtil.isEmpty(optionalParamsList)) {
					for (IndexFieldOptionalParametersDTO optionalParam : optionalParamsList) {
						model.addOptionalParamter(DatabaseExportMappingEditView.this.clone(optionalParam));
					}
				}
			}

			@Override
			protected void onCellSelectionChange(final IndexFieldDBExportMappingDTO selectedModel) {
				super.onCellSelectionChange(selectedModel);
				onCellSelection(selectedModel);
				indexFieldMap = getCurrentSelectedModel();
				setView();
			}

			private void setView() {
				final Timer timer = new Timer() {

					@Override
					public void run() {
						final IndexFieldDBExportMappingDTO currentSelectedModel = getCurrentSelectedModel();
						if (null != currentSelectedModel) {
							BatchClassManagementEventBus.fireEvent(new OptionalParametersVisibilityEvent(indexFieldMappingGrid
									.isValid(currentSelectedModel)));
							BatchClassManagementEventBus.fireEvent(new OptionalParametersRenderEvent(currentSelectedModel, store
									.getAll(), columnNameCombo.getStore().getAll()));
						}
					}
				};
				timer.schedule(200);
			}
		};
	}

	private Grid<TableColumnDBExportMappingDTO> getTableColumnsExportMappingGrid() {
		return new Grid<TableColumnDBExportMappingDTO>(PropertyAccessModel.TABLE_COLUMN_DB_MAPPING) {

			@Override
			public void completeEditing(CompleteEditEvent<TableColumnDBExportMappingDTO> completeEditEvent) {
				super.completeEditing(completeEditEvent);
				final com.sencha.gxt.widget.core.client.grid.Grid.GridCell editCell = completeEditEvent.getEditCell();
				presenter.setBatchClassDirty();
				if (null != editCell) {
					final int editCol = editCell.getCol();
					final ValueProvider valueProvider = cm.getValueProvider(editCol);
					final TableColumnDBExportMappingDTO currentSelectedModel = store.get(editCell.getRow());
					if (valueProvider == TableColumnNameValueProvider.INSTANCE) {
						TableColumnInfoDTO bindedTableColumn = currentSelectedModel.getBindedTableColumn();
						String bindedTable = currentSelectedModel.getBindedTableName();
						if (null != bindedTableColumn) {
							TableInfoDTO tableInfoDTO = bindedTableColumn.getTableInfoDTO();
							if (null != tableInfoDTO) {
								bindedTable = tableInfoDTO.getName();
							}
						}
						presenter.setTableColumn(currentSelectedModel, bindedTable, tableColInfoCombo.getText());
					}
					String tableName;
					final List<String> columnNamesListing = columnNameCombo.getStore().getAll();
					final List<TableColumnDBExportMappingDTO> allModels = store.getAll();
					if (null != currentSelectedModel) {
						if (valueProvider == TableColumnDBExportMappingProperties.INSTANCE.tableName()) {
							tableName = currentSelectedModel.getTableName();
							tableGrid.getStore().commitChanges();
							String newTableName = currentSelectedModel.getTableName();
							tableName = tableName == null ? CoreCommonConstant.EMPTY_STRING : tableName;
							if (!tableName.equalsIgnoreCase(newTableName)) {
								List<TableColumnOptionalParametersDTO> optionalParamsList = getNotEqualOptionalParams(currentSelectedModel);
								setOptionalParams(currentSelectedModel, optionalParamsList);
							}
							BatchClassManagementEventBus.fireEvent(new OptionalParametersVisibilityEvent(tableGrid
									.isValid(currentSelectedModel)));
							BatchClassManagementEventBus.fireEvent(new TableOptionalParametersRenderEvent(currentSelectedModel,
									allModels, columnNamesListing));
						} else {
							Timer timer = new Timer() {

								@Override
								public void run() {
									tableGrid.getStore().commitChanges();
									BatchClassManagementEventBus.fireEvent(new OptionalParametersVisibilityEvent(tableGrid
											.isValid(currentSelectedModel)));
									BatchClassManagementEventBus.fireEvent(new TableOptionalParametersRenderEvent(
											currentSelectedModel, allModels, columnNamesListing));
								}
							};
							timer.schedule(200);
						}
					}
				}
			}

			public void setOptionalParams(final TableColumnDBExportMappingDTO model,
					final List<TableColumnOptionalParametersDTO> optionalParamsList) {
				model.clearOptionalParams();
				if (!CollectionUtil.isEmpty(optionalParamsList)) {
					for (TableColumnOptionalParametersDTO optionalParam : optionalParamsList) {
						model.add(DatabaseExportMappingEditView.this.clone(optionalParam));
					}
				}
			}

			@Override
			protected void onCellSelectionChange(final TableColumnDBExportMappingDTO model) {
				super.onCellSelectionChange(model);
				String tableName = model.getBindedTableName();
				if (StringUtil.isNullOrEmpty(tableName)) {
					TableColumnInfoDTO bindedTableColumn = model.getBindedTableColumn();
					if (null != bindedTableColumn) {
						TableInfoDTO tableInfoDTO = bindedTableColumn.getTableInfoDTO();
						if (null != tableInfoDTO) {
							tableName = tableInfoDTO.getName();
						}
					}
				}
				List<String> columnNamesList = presenter.getColumnsForTable(tableName);
				setColumnInfo(columnNamesList);

				onCellSelection(model);
				tableColMapping = model;
				final List<String> allColumns = columnNameCombo.getStore().getAll();
				final TableColumnDBExportMappingDTO currentSelectedModel = getCurrentSelectedModel();
				Timer timer = new Timer() {

					@Override
					public void run() {
						BatchClassManagementEventBus.fireEvent(new OptionalParametersVisibilityEvent(tableGrid.isValid(model)));
						BatchClassManagementEventBus.fireEvent(new TableOptionalParametersRenderEvent(currentSelectedModel, store
								.getAll(), allColumns));
					}
				};
				timer.schedule(200);

			}
		};
	}

	private void setColumnInfo(final List<String> columnInfoList) {
		tableColInfoCombo.getStore().clear();
		if (!CollectionUtil.isEmpty(columnInfoList)) {
			tableColInfoCombo.getStore().addAll(columnInfoList);
		}
	}

	public void addTableInfos(final List<String> tableInfoList) {
		tableInfoCombo.getStore().clear();
		if (!CollectionUtil.isEmpty(tableInfoList)) {
			tableInfoCombo.getStore().addAll(tableInfoList);
		}
	}

	private void addValueChangeHandler() {
		tableNameCombo.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				final ListStore<String> store = tableNameCombo.getStore();
				if (store != null) {
					final String value = event.getValue();
					final int totalItems = store.size();
					for (int index = 0; index < totalItems; index++) {
						final String item = store.get(index);
						if (null != item && item.equalsIgnoreCase(value)) {
							getColumns(item);
							break;
						}
					}
				}
			}
		});
	}

	private List<IndexFieldOptionalParametersDTO> getNotEqualOptionalParams(IndexFieldDBExportMappingDTO indexFieldOptionalParams) {
		IndexFieldDBExportMappingDTO mappingDTO = null;
		List<IndexFieldDBExportMappingDTO> mappingList = indexFieldMappingGrid.getStore().getAll();
		if (!CollectionUtil.isEmpty(mappingList) && null != indexFieldOptionalParams) {
			for (IndexFieldDBExportMappingDTO mapping : mappingList) {
				if (null != mapping && mapping != indexFieldOptionalParams
						&& mapping.getTableName().equalsIgnoreCase(indexFieldOptionalParams.getTableName())) {
					mappingDTO = mapping;
					break;
				}
			}
		}
		return mappingDTO == null ? null : mappingDTO.getOptionalDBExportParameters();
	}

	private List<TableColumnOptionalParametersDTO> getNotEqualOptionalParams(TableColumnDBExportMappingDTO indexFieldOptionalParams) {
		TableColumnDBExportMappingDTO mappingDTO = null;
		List<TableColumnDBExportMappingDTO> mappingList = tableGrid.getStore().getAll();
		TableColumnInfoDTO bindedTableColumnDTO = indexFieldOptionalParams.getBindedTableColumn();
		TableInfoDTO tableInfo = bindedTableColumnDTO == null ? null : bindedTableColumnDTO.getTableInfoDTO();
		if (!CollectionUtil.isEmpty(mappingList) && null != indexFieldOptionalParams) {
			for (TableColumnDBExportMappingDTO mapping : mappingList) {
				if (null != mapping && mapping != indexFieldOptionalParams
						&& mapping.getTableName().equalsIgnoreCase(indexFieldOptionalParams.getTableName())) {
					TableColumnInfoDTO bindedTableColumn = mapping.getBindedTableColumn();
					TableInfoDTO tableInfoDTO = bindedTableColumn == null ? null : bindedTableColumn.getTableInfoDTO();
					if (tableInfo == tableInfoDTO) {
						mappingDTO = mapping;
						break;
					}
				}
			}
		}
		return mappingDTO == null ? null : mappingDTO.getOptionalDBExportParameters();
	}

	private IndexFieldOptionalParametersDTO clone(final IndexFieldOptionalParametersDTO mappingDTO) {
		final IndexFieldOptionalParametersDTO newDTO = new IndexFieldOptionalParametersDTO();
		newDTO.setColumnName(mappingDTO.getColumnName());
		newDTO.setNew(true);
		newDTO.setOptionalExportParamter(mappingDTO.getOptionalExportParamter());
		newDTO.setValue(mappingDTO.getValue());
		return newDTO;
	}

	private TableColumnOptionalParametersDTO clone(final TableColumnOptionalParametersDTO mappingDTO) {
		final TableColumnOptionalParametersDTO newDTO = new TableColumnOptionalParametersDTO();
		newDTO.setColumnName(mappingDTO.getColumnName());
		newDTO.setNew(mappingDTO.isNew());
		newDTO.setOptionalExportParamter(mappingDTO.getOptionalExportParamter());
		newDTO.setValue(mappingDTO.getValue());
		return newDTO;
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		BatchClassManagementEventBus.fireEvent(new OptionalParametersVisibilityEvent(false));
	}

	private void getColumns(final String tableName) {
		final ConnectionsDTO connection = connectionCombo.getValue();
		final Collection<String> columnNames = tableColumnsMap.get(tableName);
		currentTableName = tableName;
		if (!CollectionUtil.isEmpty(columnNames)) {
			setColumns(columnNames);
		} else {
			if (columnsRetreivedMap.get(tableName) != Boolean.TRUE) {
				presenter.setColumns(connection, tableName);
			} else {
				setColumns(columnNames);
			}
		}
	}

	private void onCellSelection(final DatabaseMappingDTO currentMappingDTO) {
		if (null != currentMappingDTO) {
			final String tableName = currentMappingDTO.getTableName();
			if (StringUtil.isNullOrEmpty(tableName)) {
				currentTableName = tableName;
				columnNameCombo.clear();
				columnNameCombo.getStore().clear();
			} else {
				getColumns(tableName);
			}
		}
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
		connectionCombo.setTriggerAction(TriggerAction.ALL);
		connectionCombo.setEditable(false);
	}

	public void setConnectionsList(final Collection<ConnectionsDTO> dtoConnectionList) {
		if (!CollectionUtil.isEmpty(dtoConnectionList)) {
			List<ConnectionsDTO> connectionList = new LinkedList<ConnectionsDTO>();
			for (ConnectionsDTO connection : dtoConnectionList) {
				if (null != connection && !"ORACLE".equalsIgnoreCase(connection.getDatabaseType())) {
					connectionList.add(connection);
				}
			}
			connectionCombo.getStore().addAll(connectionList);
		}
	}

	private void addSelectionChangeHandler() {
		connectionCombo.addBeforeSelectionHandler(new BeforeSelectionHandler<ConnectionsDTO>() {

			@Override
			public void onBeforeSelection(final BeforeSelectionEvent<ConnectionsDTO> event) {
				final ConnectionsDTO connection = event.getItem();
				ConnectionsDTO currentConnection = presenter.getCurrentConnection();
				if (currentConnection != null) {
					if (currentConnection == connection) {
						event.cancel();
					}
				}
			}
		});
		connectionCombo.addSelectionHandler(new SelectionHandler<ConnectionsDTO>() {

			@Override
			public void onSelection(final SelectionEvent<ConnectionsDTO> event) {
				final ConnectionsDTO connection = event.getSelectedItem();
				presenter.setConnection(connection);
				presenter.setTables(connection);
				presenter.setBatchClassDirty();
			}
		});
	}

	public void add(final List<IndexFieldDBExportMappingDTO> mapping) {
		indexFieldMappingGrid.setMemoryData(mapping);
	}

	public void addTableMappings(final List<TableColumnDBExportMappingDTO> mapping) {
		tableGrid.setMemoryData(mapping);
	}

	public void setFieldTypes(final List<String> fieldTypeNames) {
		fieldNamesCombo.getStore().clear();
		if (!CollectionUtil.isEmpty(fieldTypeNames)) {
			fieldNamesCombo.getStore().addAll(fieldTypeNames);
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
			tableNameCombo.add(tableNames);
			for (final String tableName : tableNames) {
				tableColumnsMap.put(tableName, new LinkedList<String>());
			}
		}
	}

	public void addMappingForColumn(final String tableName, final Collection<String> columnsList) {
		if (!StringUtil.isNullOrEmpty(tableName) && !CollectionUtil.isEmpty(columnsList)) {
			tableColumnsMap.put(tableName, columnsList);
		}
		if (!StringUtil.isNullOrEmpty(tableName)) {
			columnsRetreivedMap.put(tableName, true);
		}
		if (!StringUtil.isNullOrEmpty(tableName) && tableName.equalsIgnoreCase(currentTableName)) {
			currentTableName = tableName;
			setColumns(columnsList);
		}
	}

	private void setColumns(final String tableName) {
		if (!StringUtil.isNullOrEmpty(tableName)) {
			if (!tableName.equalsIgnoreCase(currentTableName)) {
				currentTableName = tableName;
				getColumns(tableName);
			}
		}
	}

	private void setColumns(final Collection<String> columnsList) {
		columnNameCombo.getStore().clear();
		if (!CollectionUtil.isEmpty(columnsList)) {
			Set<String> columnNamesList = new TreeSet<String>(columnsList);
			columnNameCombo.getStore().addAll(columnNamesList);
		}
	}

	public void reset() {
		currentTableName = CoreCommonConstant.EMPTY_STRING;
		connectionCombo.setText(CoreCommonConstant.EMPTY_STRING);
		indexFieldMap = null;
		if (null != columnsRetreivedMap) {
			columnsRetreivedMap.clear();
		}
		if (null != tableColumnsMap) {
			tableColumnsMap.clear();
		}
		connectionCombo.getStore().clear();
		indexFieldMappingGrid.getStore().clear();
		ListStore<TableColumnDBExportMappingDTO> store = tableGrid.getStore();
		store.clear();
		tableNameCombo.getStore().clear();
		columnNameCombo.getStore().clear();
	}

	public List<TableColumnDBExportMappingDTO> getAllTableColumnMapping() {
		ListStore<TableColumnDBExportMappingDTO> store = tableGrid.getStore();
		List<TableColumnDBExportMappingDTO> allTableColumn = store.getAll();
		return allTableColumn;
	}

	public void add(final IndexFieldDBExportMappingDTO mappingDTO) {
		if (null != mappingDTO) {
			indexFieldMappingGrid.addNewItemInGrid(mappingDTO);
		}
	}

	public void add(final TableColumnDBExportMappingDTO mappingDTO) {
		if (null != mappingDTO) {
			tableGrid.addNewItemInGrid(mappingDTO);
		}
	}

	public boolean isIndexFieldMappingView() {
		return indexFieldMappingGrid.isAttached() && indexFieldMappingGrid.isVisible();
	}

	public void removeSelectedModels() {
		if (isIndexFieldMappingView()) {
			final List<IndexFieldDBExportMappingDTO> selectedModels = indexFieldMappingGrid.getSelectedModels();
			deleteIndexFieldsMapping(selectedModels);
		} else {
			final List<TableColumnDBExportMappingDTO> selectedTableMapping = tableGrid.getSelectedModels();
			deleteTableColumnMapping(selectedTableMapping);
		}
	}

	private void deleteTableColumnMapping(final List<TableColumnDBExportMappingDTO> selectedTableMapping) {
		if (!CollectionUtil.isEmpty(selectedTableMapping)) {
			ConfirmationDialog dialog = DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(BatchClassConstants.DELETE_MAPPING),
					LocaleDictionary.getMessageValue(BatchClassMessages.DB_EXPORT_RECORD_DELETE_CONFIRMATION));
			dialog.setDialogListener(new DialogAdapter() {

				@Override
				public void onOkClick() {
					super.onOkClick();
					for (final TableColumnDBExportMappingDTO selectedModel : selectedTableMapping) {
						if (null != selectedModel) {
							selectedModel.setDeleted(true);
						}
					}
					tableGrid.removeItemsFromGrid(selectedTableMapping);
				}
			});
		} else {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INVALID_OPERATION), LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_ROW_TO_DELETE));
		}
	}

	private void deleteIndexFieldsMapping(final List<IndexFieldDBExportMappingDTO> selectedModels) {
		if (!CollectionUtil.isEmpty(selectedModels)) {
			ConfirmationDialog dialog = DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(BatchClassConstants.DELETE_MAPPING),
					LocaleDictionary.getMessageValue(BatchClassMessages.DB_EXPORT_RECORD_DELETE_CONFIRMATION));
			dialog.setDialogListener(new DialogAdapter() {

				@Override
				public void onOkClick() {
					super.onOkClick();
					for (final IndexFieldDBExportMappingDTO selectedModel : selectedModels) {
						if (null != selectedModel) {
							selectedModel.setDeleted(true);
						}
					}
					indexFieldMappingGrid.removeItemsFromGrid(selectedModels);
				}
			});
		} else {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INVALID_OPERATION), LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_ROW_TO_DELETE));
		}
	}

	@Override
	public Grid<?> getGrid() {
		return indexFieldMappingGrid;
	}

	public void showTableView(boolean showTableView) {
		if (showTableView && !tableGrid.isAttached()) {
			tableGrid.setPixelSize(indexFieldMappingGrid.getOffsetWidth(), indexFieldMappingGrid.getOffsetHeight());
			gridContainer.remove(indexFieldMappingGrid);
			gridContainer.add(tableGrid);
		} else {
			gridContainer.remove(tableGrid);
			gridContainer.add(indexFieldMappingGrid);
		}
	}
}
