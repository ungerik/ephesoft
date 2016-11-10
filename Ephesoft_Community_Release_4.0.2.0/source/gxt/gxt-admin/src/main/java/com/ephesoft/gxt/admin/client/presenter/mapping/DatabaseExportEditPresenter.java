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

package com.ephesoft.gxt.admin.client.presenter.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AddDBExportMappingEvent;
import com.ephesoft.gxt.admin.client.event.DeleteDBExportMappingEvent;
import com.ephesoft.gxt.admin.client.event.ResetDBExportMappingEvent;
import com.ephesoft.gxt.admin.client.event.TableMappingOpenEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.mapping.DatabaseExportMappingEditView;
import com.ephesoft.gxt.admin.client.view.mapping.DatabaseExportMappingView;
import com.ephesoft.gxt.core.client.RandomIdGenerator;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.IndexFieldDBExportMappingDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnDBExportMappingDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;


public class DatabaseExportEditPresenter extends BatchClassInlinePresenter<DatabaseExportMappingEditView> {

	public DatabaseExportEditPresenter(BatchClassManagementController controller, DatabaseExportMappingEditView view) {
		super(controller, view);
	}

	interface CustomEventBinder extends EventBinder<DatabaseExportEditPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	@Override
	public void bind() {
		ScreenMaskUtility.maskScreen();
		view.reset();
		view.showTableView(false);
		rpcService.getAllConnectionsDTO(new AsyncCallback<List<ConnectionsDTO>>() {

			@Override
			public void onSuccess(List<ConnectionsDTO> result) {
				ScreenMaskUtility.unmaskScreen();
				view.setConnectionsList(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
			}
		});

		DocumentTypeDTO selectedDocumentType = controller.getSelectedDocumentType();

		if (null != selectedDocumentType) {
			List<IndexFieldDBExportMappingDTO> mergedMapping = getIndexFieldMapping(selectedDocumentType);
			List<String> indexFieldNames = getIndexFieldsNames(selectedDocumentType);
			view.add(mergedMapping);
			view.setFieldTypes(indexFieldNames);
			view.addTableMappings(getTableColumnMapping(selectedDocumentType));
			ConnectionsDTO connection = selectedDocumentType.getDBExportConnection();
			view.setConnection(connection);
			view.addTableInfos(getTableNames(selectedDocumentType));
			setTables(connection);
		}
	}

	private List<String> getTableNames(DocumentTypeDTO documentTypeDTO) {
		List<String> tableNames = new LinkedList<String>();
		Collection<TableInfoDTO> tableInfos = documentTypeDTO.getTableInfos();
		if (!CollectionUtil.isEmpty(tableInfos)) {
			for (TableInfoDTO tableInfo : tableInfos) {
				if (null != tableInfo) {
					tableNames.add(tableInfo.getName());
				}
			}
		}
		return tableNames;
	}

	private List<IndexFieldDBExportMappingDTO> getIndexFieldMapping(final DocumentTypeDTO documentType) {
		List<IndexFieldDBExportMappingDTO> mergedMappings = new LinkedList<IndexFieldDBExportMappingDTO>();
		if (null != documentType) {
			Collection<FieldTypeDTO> fieldsCollection = documentType.getFields(false);
			if (!CollectionUtil.isEmpty(fieldsCollection)) {
				for (final FieldTypeDTO fieldType : fieldsCollection) {
					if (null != fieldType) {
						List<IndexFieldDBExportMappingDTO> mappingList = fieldType.getIndexFieldMappingList();
						if (!CollectionUtil.isEmpty(mappingList)) {
							mergedMappings.addAll(mappingList);
						}
					}
				}
			}
		}
		return mergedMappings;
	}

	private List<TableColumnDBExportMappingDTO> getTableColumnMapping(final DocumentTypeDTO documentType) {
		List<TableColumnDBExportMappingDTO> mergedMappings = new LinkedList<TableColumnDBExportMappingDTO>();
		if (null != documentType) {
			Collection<TableInfoDTO> fieldsCollection = documentType.getTableInfos();
			if (!CollectionUtil.isEmpty(fieldsCollection)) {
				for (final TableInfoDTO table : fieldsCollection) {
					if (null != table) {
						List<TableColumnInfoDTO> columnInfoDTOs = table.getColumnInfoDTOs();
						if (!CollectionUtil.isEmpty(columnInfoDTOs)) {
							for (final TableColumnInfoDTO columnInfo : columnInfoDTOs) {
								if (null != columnInfo) {
									mergedMappings.addAll(columnInfo.getDbExportMappingList());
								}
							}
						}
					}
				}
			}
		}
		return mergedMappings;
	}

	private List<String> getIndexFieldsNames(final DocumentTypeDTO indexFields) {
		List<String> fieldTypeNames = null;
		if (null != indexFields) {
			fieldTypeNames = new ArrayList<String>();
			Collection<FieldTypeDTO> fieldsCollection = indexFields.getFields(false);
			for (FieldTypeDTO fieldType : fieldsCollection) {
				if (null != fieldType) {
					fieldTypeNames.add(fieldType.getName());
				}
			}
		}
		return fieldTypeNames;
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void setConnection(final ConnectionsDTO connection) {
		if (null != connection) {
			DocumentTypeDTO selectedDocumentType = controller.getSelectedDocumentType();
			if (null != selectedDocumentType) {
				selectedDocumentType.setDBExportConnection(connection);
			}
		}
	}

	public ConnectionsDTO getCurrentConnection() {
		ConnectionsDTO connection = null;
		DocumentTypeDTO selectedDocumentType = controller.getSelectedDocumentType();
		if (null != selectedDocumentType) {
			connection = selectedDocumentType.getDBExportConnection();
		}
		return connection;
	}

	public void setFieldType(final IndexFieldDBExportMappingDTO mappingDTO, final String fieldName) {
		if (null != mappingDTO && !StringUtil.isNullOrEmpty(fieldName)) {
			String mappedFieldName = fieldName.trim();
			DocumentTypeDTO currentDocumentType = controller.getSelectedDocumentType();
			if (null != currentDocumentType) {
				FieldTypeDTO fieldType = currentDocumentType.getFieldTypeByName(mappedFieldName);
				if (null != fieldType) {
					mappingDTO.setBindedField(fieldType);
				}
			}
		}
	}

	public void setTableColumn(final TableColumnDBExportMappingDTO mappingDTO, final String tableName, final String columnName) {
		if (null != mappingDTO && !StringUtil.isNullOrEmpty(tableName) && !StringUtil.isNullOrEmpty(columnName)) {
			DocumentTypeDTO currentDocumentType = controller.getSelectedDocumentType();
			if (null != currentDocumentType) {
				TableInfoDTO fieldType = currentDocumentType.getTableInfoByName(tableName);
				if (null != fieldType) {
					TableColumnInfoDTO tableColumnInfoDTOByColumnName = fieldType.getTableColumnInfoDTOByColumnName(columnName);
					mappingDTO.setBindedTableColumn(tableColumnInfoDTOByColumnName);
					mappingDTO.setNew(true);
					mappingDTO.setId(RandomIdGenerator.getIdentifier());
				}
			}
		}
	}

	public void setTables(final ConnectionsDTO connection) {

		if (null != connection) {
			ScreenMaskUtility.maskScreen();
			rpcService.getAllTables(connection, new AsyncCallback<Map<String, List<String>>>() {

				@Override
				public void onFailure(Throwable caught) {
					ScreenMaskUtility.unmaskScreen();
				}

				@Override
				public void onSuccess(Map<String, List<String>> result) {
					Collection<List<String>> valuesCollection = result.values();
					List<String> tablesNames = null;
					if (!CollectionUtil.isEmpty(valuesCollection)) {
						tablesNames = new ArrayList<String>();
						for (List<String> traversedNames : valuesCollection) {
							if (null != traversedNames) {
								tablesNames.addAll(traversedNames);
							}
						}
					}
					view.setTables(tablesNames);
					ScreenMaskUtility.unmaskScreen();
				}
			});
		}
	}

	public void setColumns(final ConnectionsDTO connection, final String tableName) {
		if (null != connection && !StringUtil.isNullOrEmpty(tableName)) {
			ScreenMaskUtility.maskScreen();
			rpcService.getAllColumnsForTable(connection, tableName, new AsyncCallback<Map<String, String>>() {

				@Override
				public void onFailure(Throwable caught) {
					ScreenMaskUtility.unmaskScreen();
				}

				@Override
				public void onSuccess(Map<String, String> result) {
					Collection<String> columnNames = null;
					if (!CollectionUtil.isEmpty(result)) {
						columnNames = result.keySet();
					}
					view.addMappingForColumn(tableName, columnNames);
					ScreenMaskUtility.unmaskScreen();
				}
			});
		}
	}

	public FieldTypeDTO getSelectedFieldTypeByName(final String fieldType) {
		FieldTypeDTO selectedFieldType = null;
		DocumentTypeDTO currentDocument = controller.getSelectedDocumentType();
		if (!StringUtil.isNullOrEmpty(fieldType)) {
			Collection<FieldTypeDTO> fieldsList = currentDocument.getFields(false);

			for (FieldTypeDTO field : fieldsList) {
				if (null != field && fieldType.equalsIgnoreCase(field.getName())) {
					selectedFieldType = field;
					break;
				}
			}
		}
		return selectedFieldType;
	}

	@EventHandler
	public void handleIndexFieldMappingEvent(final AddDBExportMappingEvent mappingAdditionEvent) {
		if (null != mappingAdditionEvent) {
			if (view.isIndexFieldMappingView()) {
				view.add(createIndexFieldMappingDTO());
			} else {
				view.add(createTableColumnMappingDTO());
			}
			controller.setBatchClassDirtyFlg(true);
		}
	}

	@EventHandler
	public void handleIndexFieldMappingEvent(final DeleteDBExportMappingEvent mappingDeletionEvent) {
		if (null != mappingDeletionEvent) {
			view.removeSelectedModels();
			controller.setBatchClassDirtyFlg(true);
		}
	}

	private IndexFieldDBExportMappingDTO createIndexFieldMappingDTO() {
		IndexFieldDBExportMappingDTO mapingDTO = new IndexFieldDBExportMappingDTO();
		mapingDTO.setNew(true);
		mapingDTO.setId(RandomIdGenerator.getIdentifier());
		return mapingDTO;
	}

	private TableColumnDBExportMappingDTO createTableColumnMappingDTO() {
		TableColumnDBExportMappingDTO mapingDTO = new TableColumnDBExportMappingDTO();
		mapingDTO.setNew(true);
		mapingDTO.setId(RandomIdGenerator.getIdentifier());
		return mapingDTO;
	}

	@EventHandler
	public void handleDBExportResetEvent(ResetDBExportMappingEvent resetEvent) {
		if (null != resetEvent) {
			ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(BatchClassConstants.RESET_MAPPING), LocaleDictionary.getMessageValue(BatchClassMessages.RESET_MAPPING_CONFIRMATION));
			confirmationDialog.setDialogListener(new DialogAdapter() {
				@Override
				public void onOkClick() {
					super.onOkClick();
				
					DocumentTypeDTO selectedDocumentType = controller.getSelectedDocumentType();
					if (null != selectedDocumentType) {
						selectedDocumentType.setDBExportConnection(null);
						Collection<FieldTypeDTO> fieldsList = selectedDocumentType.getFields(false);
						for (FieldTypeDTO fieldType : fieldsList) {
							if (null != fieldsList) {
								List<IndexFieldDBExportMappingDTO> indexFieldMappingList = fieldType.getIndexFieldMappingList();
								if (!CollectionUtil.isEmpty(indexFieldMappingList)) {
									for (final IndexFieldDBExportMappingDTO indexFieldMapping : indexFieldMappingList) {
										if (null != indexFieldMapping) {
											indexFieldMapping.setDeleted(true);
										}
									}
								}
							}
						}
					}
					List<TableColumnDBExportMappingDTO> allTableColumn = view.getAllTableColumnMapping();
					if(!CollectionUtil.isEmpty(allTableColumn)) {
						for(TableColumnDBExportMappingDTO columnMapping : allTableColumn) {
							if(null != columnMapping) {
								columnMapping.setDeleted(true);
							}
						}
					}
					controller.setBatchClassDirtyFlg(true);
					view.reset();
				}
			});
		}
	}

	public boolean isOptionalViewValid() {
		DatabaseExportMappingView databaseExportMappingView = controller.getDatabaseExportMappingView();
		return databaseExportMappingView.isOptionalParamsViewValid();
	}

	public void setBatchClassDirty() {
		controller.setBatchClassDirtyFlg(true);
	}

	public List<String> getColumnsForTable(String tableName) {
		List<String> tableColumnNameInfoList = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(tableName)) {
			DocumentTypeDTO selectedDocumentType = controller.getSelectedDocumentType();
			if (null != selectedDocumentType) {
				TableInfoDTO tableInfo = selectedDocumentType.getTableInfoByName(tableName);
				if (null != tableInfo) {
					List<TableColumnInfoDTO> columnInfoDTOs = tableInfo.getColumnInfoDTOs();
					if (!CollectionUtil.isEmpty(columnInfoDTOs)) {
						for (TableColumnInfoDTO columnInfo : columnInfoDTOs) {
							tableColumnNameInfoList.add(columnInfo.getColumnName());
						}
					}
				}
			}
		}
		return tableColumnNameInfoList;
	}

	@EventHandler
	public void handleTableMappingOpenEvent(final TableMappingOpenEvent mappingOpenEvent) {
		if (null != mappingOpenEvent) {
			boolean showTableView = mappingOpenEvent.isShowTableView();
			view.showTableView(showTableView);
		}
	}
}
