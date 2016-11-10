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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.dcma.core.common.OptionalExportParameters;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.mapping.DatabaseExportOptionalParamsPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.ui.widget.DetailGrid;
import com.ephesoft.gxt.core.client.ui.widget.util.UniqueIDProvider;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.DBExportOptionalParametersDTO;
import com.ephesoft.gxt.core.shared.dto.DetailsDTO;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.ephesoft.gxt.core.shared.dto.IndexFieldDBExportMappingDTO;
import com.ephesoft.gxt.core.shared.dto.IndexFieldDBExportMappingDTO.IndexFieldOptionalParametersDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnDBExportMappingDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnDBExportMappingDTO.TableColumnOptionalParametersDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ToStringValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent.StoreRemoveHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.form.StringComboBox;

public class DatabaseExportOptionalParamsView extends BatchClassInlineView<DatabaseExportOptionalParamsPresenter> {

	interface Binder extends UiBinder<Widget, DatabaseExportOptionalParamsView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField(provided = true)
	protected DetailGrid singleRowGrid;

	@UiField(provided = true)
	protected ListView<String, String> systemLevelFieldsTarget;

	@UiField(provided = true)
	protected ListView<String, String> systemLevelFieldsSource;

	protected StringComboBox detailsGridColumnEditor;

	private IndexFieldDBExportMappingDTO currentMappingDTO;

	private TableColumnDBExportMappingDTO currentTableColumnMappingDTO;

	private List<IndexFieldDBExportMappingDTO> mappingList;

	private List<TableColumnDBExportMappingDTO> tableColumnMappingList;

	private static final String OPTIONAL_PARAMS_GROUP = "optionalParamsGroup";

	public DatabaseExportOptionalParamsView() {
		initWidget(binder.createAndBindUi(this));
		singleRowGrid.setHeaders(LocaleDictionary.getConstantValue(BatchClassConstants.EXPORT_PARAMETER), LocaleDictionary.getConstantValue(BatchClassConstants.DATABASE_COLUMN));
		detailsGridColumnEditor = new StringComboBox();
		detailsGridColumnEditor.setTriggerAction(TriggerAction.ALL);
		this.addStyleName("optionalParamsView");
		singleRowGrid.addEditor(DetailGrid.DETAIL_VALUE_PROVIDER, detailsGridColumnEditor);
		singleRowGrid.getStore().setAutoCommit(true);
		singleRowGrid.addValidators(DetailGrid.TITLE_PROVIDER, new UniqueValueValidator<DetailsDTO, String>());
		singleRowGrid.addValidators(DetailGrid.DETAIL_VALUE_PROVIDER, new UniqueValueValidator<DetailsDTO, String>());
		singleRowGrid.addValidators(DetailGrid.DETAIL_VALUE_PROVIDER, new EmptyValueValidator<DetailsDTO>());
		this.addDragDropHandler();
		singleRowGrid.addStyleName("gridView");
	}

	@Override
	public void initialize() {
		super.initialize();
		systemLevelFieldsTarget = new ListView<String, String>(new ListStore<String>(new UniqueIDProvider<String>()),
				new ToStringValueProvider<String>());
		systemLevelFieldsTarget.setWidth(220);
		systemLevelFieldsTarget.setHeight(180);
		systemLevelFieldsTarget.addStyleName("dbExportAdditionalParamBox");
		systemLevelFieldsTarget.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		systemLevelFieldsSource = new ListView<String, String>(new ListStore<String>(new UniqueIDProvider<String>()),
				new ToStringValueProvider<String>());
		systemLevelFieldsSource.setWidth(220);
		systemLevelFieldsSource.setHeight(180);
		systemLevelFieldsSource.addStyleName("dbExportAdditionalParamBox");
		systemLevelFieldsSource.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		singleRowGrid = new DetailGrid(true) {

			@Override
			protected void onCompleteEdit(CompleteEditEvent<DetailsDTO> event) {
				super.onCompleteEdit(event);
				int row = event.getEditCell().getRow();
				store.commitChanges();
				DetailsDTO detailsDTO = store.get(row);
				if (null != detailsDTO) {
					changeColumns(detailsDTO);
				}
				refreshGrid();
			}

			@Override
			protected void onBeforeeEdit(BeforeStartEditEvent<DetailsDTO> event) {
				super.onBeforeeEdit(event);
				if (null != event) {
					com.sencha.gxt.widget.core.client.grid.Grid.GridCell editCell = event.getEditCell();
					if (null != editCell) {
						int row = editCell.getRow();
						DetailsDTO currentModel = store.get(row);
						String title = currentModel.getTitle();
						if (StringUtil.isNullOrEmpty(title) || !title.startsWith("$$")) {
							event.setCancelled(true);
						}
					}
				}
			}
			
			@Override
			protected void onLoad() {
				super.onLoad();
				Timer timer = new Timer() {
					@Override
					public void run() {
						setPixelSize(350, 180);
					}
				};
				timer.schedule(100);
			}
		};

		ListViewDragSource<String> optionParamsSourceDragSource = new ListViewDragSource<String>(systemLevelFieldsSource);
		optionParamsSourceDragSource.setGroup(OPTIONAL_PARAMS_GROUP);
		ListViewDragSource<String> optionParamsTargetDragSource = new ListViewDragSource<String>(systemLevelFieldsTarget);
		optionParamsTargetDragSource.setGroup(OPTIONAL_PARAMS_GROUP);
		ListViewDropTarget<String> optionParamsSourceDropTarget = new ListViewDropTarget<String>(systemLevelFieldsSource);
		optionParamsSourceDropTarget.setGroup(OPTIONAL_PARAMS_GROUP);
		ListViewDropTarget<String> optionParamsTargetDropTarget = new ListViewDropTarget<String>(systemLevelFieldsTarget);
		optionParamsTargetDropTarget.setGroup(OPTIONAL_PARAMS_GROUP);

		optionParamsSourceDropTarget.addDropHandler(new DndDropHandler() {

			@SuppressWarnings("unchecked")
			@Override
			public void onDrop(DndDropEvent event) {
				Object data = event.getData();
				if (data instanceof List) {
					List<String> list = ((List<String>) data);
					if (!CollectionUtil.isEmpty(mappingList) && null != currentMappingDTO) {
						removeIndexFieldMapping(list);
					} else {
						if (!CollectionUtil.isEmpty(tableColumnMappingList) && null != currentTableColumnMappingDTO) {
							removeTableColumnMapping(list);
						}
					}
				}
				presenter.getController().setBatchClassDirtyFlg(true);
			}
		});
		singleRowGrid.addStyleName("exportSingleRowGrid");
	}

	private void removeIndexFieldMapping(List<String> list) {
		List<IndexFieldDBExportMappingDTO> tableMappingList = getMappingsWithTableName(mappingList, currentMappingDTO.getTableName());
		if (!CollectionUtil.isEmpty(tableMappingList)) {
			for (String value : list) {
				IndexFieldOptionalParametersDTO mappingDTO = initializeOptionalParam(value);
				OptionalExportParameters optionalExportParamter = mappingDTO.getOptionalExportParamter();
				String mappingValue = mappingDTO.getValue();
				if (null != mappingDTO) {
					for (IndexFieldDBExportMappingDTO fieldMapping : tableMappingList) {
						fieldMapping.remove(optionalExportParamter, mappingValue);
					}
				}
			}
		}
	}

	private void removeTableColumnMapping(List<String> list) {
		List<TableColumnDBExportMappingDTO> tableMappingList = getMappingsWithTableName(tableColumnMappingList,
				currentTableColumnMappingDTO.getTableName(), currentTableColumnMappingDTO);
		if (!CollectionUtil.isEmpty(tableMappingList)) {
			for (String value : list) {
				IndexFieldOptionalParametersDTO mappingDTO = initializeOptionalParam(value);
				OptionalExportParameters optionalExportParamter = mappingDTO.getOptionalExportParamter();
				if (null != mappingDTO) {
					for (TableColumnDBExportMappingDTO fieldMapping : tableMappingList) {
						TableColumnOptionalParametersDTO mapping = fieldMapping.getMapping(optionalExportParamter);
						if (null != mapping) {
							mapping.setDeleted(true);
						}
					}
				}
			}
		}
	}

	public void addOptionalParameters(List<? extends DBExportOptionalParametersDTO> optionalExportParameters, BatchClassDTO batchClass) {
		systemLevelFieldsTarget.getStore().clear();
		systemLevelFieldsSource.getStore().clear();
		for (OptionalExportParameters exportParameter : OptionalExportParameters.values()) {
			if (null != exportParameter) {
				if (isOptionalParameterExist(optionalExportParameters, exportParameter)) {
					systemLevelFieldsTarget.getStore().add("$$ " + exportParameter);
				} else {
					systemLevelFieldsSource.getStore().add("$$ " + exportParameter);
				}
			}
		}
	}

	public boolean isBatchClassFieldOptionalParamter(List<? extends DBExportOptionalParametersDTO> optionalParameters,
			final String name) {
		boolean isFound = false;
		if (!CollectionUtil.isEmpty(optionalParameters) && !StringUtil.isNullOrEmpty(name)) {
			for (DBExportOptionalParametersDTO mappingDTO : optionalParameters) {
				if (null != mappingDTO) {
					OptionalExportParameters optionalParamter = mappingDTO.getOptionalExportParamter();
					if (null != optionalParamter && optionalParamter == OptionalExportParameters.BATCH_LEVEL_FIELDS
							&& name.equalsIgnoreCase(mappingDTO.getValue())) {
						isFound = true;
						break;
					}
				}
			}
		}
		return isFound;
	}

	private void createIndexFieldsOptionalParams(String item) {
		IndexFieldOptionalParametersDTO optionalParam = initializeOptionalParam(item);
		if (null != optionalParam && !CollectionUtil.isEmpty(mappingList) && null != currentMappingDTO) {
			IndexFieldOptionalParametersDTO tempBinding = currentMappingDTO.getMapping(optionalParam.getOptionalExportParamter(),
					optionalParam.getValue());
			optionalParam = tempBinding == null ? optionalParam : tempBinding;
			if (null != optionalParam) {
				singleRowGrid.getStore().add(new DetailsDTO(item, optionalParam.getColumnName()));
				String tableName = currentMappingDTO.getTableName();
				List<IndexFieldDBExportMappingDTO> mappingDTOList = getMappingsWithTableName(mappingList, tableName);
				if (!CollectionUtil.isEmpty(mappingDTOList)) {
					for (IndexFieldDBExportMappingDTO mappingDTO : mappingDTOList) {
						if (!mappingDTO.containsOptionalParameter(optionalParam.getOptionalExportParamter(), optionalParam.getValue())) {
							mappingDTO.addOptionalParamter(DatabaseExportOptionalParamsView.this.clone(optionalParam));
						}
					}
				}
			}
		}
	}

	private void createTableColumnOptionalParams(String item) {
		TableColumnOptionalParametersDTO optionalParam = initializeTableColOptionalParam(item);
		if (null != optionalParam && !CollectionUtil.isEmpty(tableColumnMappingList) && null != currentTableColumnMappingDTO) {
			TableColumnOptionalParametersDTO tempBinding = currentTableColumnMappingDTO.getMapping(optionalParam
					.getOptionalExportParamter());
			optionalParam = tempBinding == null ? optionalParam : tempBinding;
			if (null != optionalParam) {
				singleRowGrid.getStore().add(new DetailsDTO(item, optionalParam.getColumnName()));
				String tableName = currentTableColumnMappingDTO.getTableName();
				List<TableColumnDBExportMappingDTO> mappingDTOList = getMappingsWithTableName(tableColumnMappingList, tableName,
						currentTableColumnMappingDTO);
				if (!CollectionUtil.isEmpty(mappingDTOList)) {
					for (TableColumnDBExportMappingDTO mappingDTO : mappingDTOList) {
						if (null != mappingDTO && mappingDTO.getMapping(optionalParam.getOptionalExportParamter()) == null) {
							mappingDTO.add(DatabaseExportOptionalParamsView.this.clone(optionalParam));
						}
					}
				}
			}
		}
	}

	private void addDragDropHandler() {
		systemLevelFieldsTarget.getStore().addStoreAddHandler(new StoreAddHandler<String>() {

			@Override
			public void onAdd(StoreAddEvent<String> event) {
				List<String> addedItems = event.getItems();
				presenter.getController().setBatchClassDirtyFlg(true);
				if (!CollectionUtil.isEmpty(addedItems)) {
					for (String item : addedItems) {
						if (null != currentMappingDTO) {
							createIndexFieldsOptionalParams(item);
						} else {
							if (null != currentTableColumnMappingDTO) {
								createTableColumnOptionalParams(item);
							}
						}
					}
				}
			}

		});

		systemLevelFieldsTarget.getStore().addStoreRemoveHandler(new StoreRemoveHandler<String>() {

			@Override
			public void onRemove(StoreRemoveEvent<String> event) {
				String removedItem = event.getItem();
				ListStore<DetailsDTO> store = singleRowGrid.getStore();
				List<DetailsDTO> list = store.getAll();
				if (!CollectionUtil.isEmpty(list) && !StringUtil.isNullOrEmpty(removedItem)) {
					for (DetailsDTO detail : list) {
						if (null != detail && removedItem.equalsIgnoreCase(detail.getTitle())) {
							store.remove(detail);
							break;
						}
					}
				}
			}
		});
	}

	public boolean isOptionalParameterExist(final List<? extends DBExportOptionalParametersDTO> optionalParametersList,
			final OptionalExportParameters optionalParameter) {
		boolean isFound = false;
		if (null != optionalParameter) {
			if (!CollectionUtil.isEmpty(optionalParametersList) && null != optionalParameter) {
				for (DBExportOptionalParametersDTO mappingDTO : optionalParametersList) {
					if (null != mappingDTO) {
						OptionalExportParameters optionalParamter = mappingDTO.getOptionalExportParamter();
						if (null != optionalParamter && optionalParamter == optionalParameter) {
							isFound = true;
							break;
						}
					}
				}
			}
		}
		return isFound;
	}

	public void setColumns(List<String> columnsist) {
		detailsGridColumnEditor.clear();
		detailsGridColumnEditor.getStore().clear();
		if (!CollectionUtil.isEmpty(columnsist)) {
			detailsGridColumnEditor.add(columnsist);
		}
	}

	public void setSingleRowDetail(List<DetailsDTO> detailsList) {
		singleRowGrid.getStore().clear();
		if (!CollectionUtil.isEmpty(detailsList)) {
			singleRowGrid.getStore().addAll(detailsList);
		}
	}

	public void setCurrentIndexFieldMapping(IndexFieldDBExportMappingDTO mappingDTO) {
		this.currentMappingDTO = mappingDTO;
	}

	public void setMappingList(List<IndexFieldDBExportMappingDTO> mappingList) {
		this.mappingList = mappingList;
	}

	public void setCurrentTableColumnDTO(TableColumnDBExportMappingDTO currentTableColumnDTO) {
		this.currentTableColumnMappingDTO = currentTableColumnDTO;
	}

	public void setTableColumnMappingList(List<TableColumnDBExportMappingDTO> tableColumnMappingList) {
		this.tableColumnMappingList = tableColumnMappingList;
	}

	private IndexFieldOptionalParametersDTO initializeOptionalParam(String item) {
		IndexFieldOptionalParametersDTO optionalParametersDTO = null;
		if (!StringUtil.isNullOrEmpty(item)) {
			if (item.startsWith("$$")) {
				String optionalParamName = item.substring(3);
				OptionalExportParameters exportParams = OptionalExportParameters.valueOf(optionalParamName);
				if (null != exportParams) {
					optionalParametersDTO = new IndexFieldOptionalParametersDTO();
					optionalParametersDTO.setColumnName(CoreCommonConstant.EMPTY_STRING);
					optionalParametersDTO.setOptionalExportParamter(exportParams);
					optionalParametersDTO.setValue(null);
					optionalParametersDTO.setNew(true);
				}
			}
		}
		return optionalParametersDTO;
	}

	private TableColumnOptionalParametersDTO initializeTableColOptionalParam(String item) {
		TableColumnOptionalParametersDTO optionalParam = null;
		if (!StringUtil.isNullOrEmpty(item)) {
			if (item.startsWith("$$ ")) {
				String optionalParamName = item.substring(3);
				OptionalExportParameters exportParams = OptionalExportParameters.valueOf(optionalParamName);
				if (null != exportParams) {
					optionalParam = new TableColumnOptionalParametersDTO();
					optionalParam.setColumnName(CoreCommonConstant.EMPTY_STRING);
					optionalParam.setOptionalExportParamter(exportParams);
					optionalParam.setValue(null);
					optionalParam.setNew(true);
				}
			}
		}
		return optionalParam;
	}

	private List<IndexFieldDBExportMappingDTO> getMappingsWithTableName(
			final List<IndexFieldDBExportMappingDTO> indexFieldMappingList, String tableName) {
		List<IndexFieldDBExportMappingDTO> mappingList = null;
		if (!CollectionUtil.isEmpty(indexFieldMappingList) && !StringUtil.isNullOrEmpty(tableName)) {
			mappingList = new LinkedList<IndexFieldDBExportMappingDTO>();
			for (IndexFieldDBExportMappingDTO indexFieldMapping : indexFieldMappingList) {
				if (null != indexFieldMapping && tableName.equalsIgnoreCase(indexFieldMapping.getTableName())) {
					mappingList.add(indexFieldMapping);
				}
			}
		}
		return mappingList;
	}

	private List<TableColumnDBExportMappingDTO> getMappingsWithTableName(
			final List<TableColumnDBExportMappingDTO> tableColumnMappingList, final String tableName,
			final TableColumnDBExportMappingDTO currentMapping) {
		List<TableColumnDBExportMappingDTO> mappingList = null;
		if (null != currentMapping && !CollectionUtil.isEmpty(tableColumnMappingList) && !StringUtil.isNullOrEmpty(tableName)) {
			mappingList = new ArrayList<TableColumnDBExportMappingDTO>();
			TableColumnInfoDTO bindedTableColumn = currentMapping.getBindedTableColumn();
			if (null != bindedTableColumn) {
				TableInfoDTO currentTableInfoDTO = bindedTableColumn.getTableInfoDTO();
				if (null != currentTableInfoDTO) {
					for (TableColumnDBExportMappingDTO tableColumnMapping : tableColumnMappingList) {
						if (null != tableColumnMapping && tableName.equalsIgnoreCase(tableColumnMapping.getTableName())) {
							TableColumnInfoDTO tableColumn = tableColumnMapping.getBindedTableColumn();
							if (null != tableColumn) {
								TableInfoDTO tableInfo = tableColumn.getTableInfoDTO();
								if (tableInfo == currentTableInfoDTO) {
									mappingList.add(tableColumnMapping);
								}
							}
						}
					}
				}
			}
		}
		return mappingList;
	}

	private IndexFieldOptionalParametersDTO clone(final IndexFieldOptionalParametersDTO mappingDTO) {
		final IndexFieldOptionalParametersDTO newDTO = new IndexFieldOptionalParametersDTO();
		newDTO.setColumnName(mappingDTO.getColumnName());
		newDTO.setNew(mappingDTO.isNew());
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

	private void changeColumns(final DetailsDTO detailDTO) {
		if (null != detailDTO) {
			if (null != currentMappingDTO) {
				changeColumnsForIndexFields(detailDTO);
			} else {
				if (null != currentTableColumnMappingDTO) {
					changeColumnsForTableColumns(detailDTO);
				}
			}
		}
	}

	private void changeColumnsForIndexFields(final DetailsDTO detailDTO) {
		String title = detailDTO.getTitle();
		String tableName = currentMappingDTO.getTableName();
		IndexFieldOptionalParametersDTO initalParam = initializeOptionalParam(title);
		if (null != initalParam) {
			List<IndexFieldDBExportMappingDTO> indexFieldsList = getMappingsWithTableName(mappingList, tableName);
			if (!CollectionUtil.isEmpty(indexFieldsList)) {
				for (final IndexFieldDBExportMappingDTO mapping : indexFieldsList) {
					if (null != mapping) {
						IndexFieldOptionalParametersDTO optionalMapping = mapping.getMapping(initalParam.getOptionalExportParamter(),
								initalParam.getColumnName());
						if (null != optionalMapping) {
							optionalMapping.setColumnName(detailDTO.getValue());
						}
					}
				}
			}
		}
	}

	private void changeColumnsForTableColumns(final DetailsDTO detailDTO) {
		String title = detailDTO.getTitle();
		String tableName = currentTableColumnMappingDTO.getTableName();
		TableColumnOptionalParametersDTO initialParam = initializeTableColOptionalParam(title);
		if (null != initialParam) {
			List<TableColumnDBExportMappingDTO> indexFieldsList = getMappingsWithTableName(tableColumnMappingList, tableName,
					currentTableColumnMappingDTO);
			if (!CollectionUtil.isEmpty(indexFieldsList)) {
				for (final TableColumnDBExportMappingDTO mapping : indexFieldsList) {
					if (null != mapping) {
						TableColumnOptionalParametersDTO optionalMapping = mapping
								.getMapping(initialParam.getOptionalExportParamter());
						if (null != optionalMapping) {
							optionalMapping.setColumnName(detailDTO.getValue());
						}
					}
				}
			}
		}
	}

	public boolean isValid() {
		return singleRowGrid.isValid();
	}

}
