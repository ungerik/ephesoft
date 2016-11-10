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

package com.ephesoft.gxt.admin.client.presenter.fuzzymapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AddFuzzyDBMappingEvent;
import com.ephesoft.gxt.admin.client.event.DeleteFuzzyDBMappingEvent;
import com.ephesoft.gxt.admin.client.event.ResetFuzzyDBMappingEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.fuzzymapping.FuzzyDBMappingGridView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogListener;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FuzzyDBDTO;
import com.ephesoft.gxt.core.shared.dto.FuzzyDBMappingDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;

public class FuzzyDBGridPresenter extends BatchClassInlinePresenter<FuzzyDBMappingGridView> {

	public FuzzyDBGridPresenter(BatchClassManagementController controller, final FuzzyDBMappingGridView view) {
		super(controller, view);
	}

	interface CustomEventBinder extends EventBinder<FuzzyDBGridPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	Map<String, List<String>> tablesWithTypeMap;
	Map<String, List<String>> tablesWithPrimaryColumnMap;
	FuzzyDBDTO fuzzyDBDTO;

	@Override
	public void bind() {
		view.clear();
		view.actionForColumnCells();

		ScreenMaskUtility.maskScreen();
		rpcService.getAllConnectionsDTO(new AsyncCallback<List<ConnectionsDTO>>() {

			@Override
			public void onSuccess(List<ConnectionsDTO> result) {
				ScreenMaskUtility.unmaskScreen();
				view.setConnectionsList(result);
			}

			@Override
			public void onFailure(Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_FETCH_CONNECTIONS), DialogIcon.ERROR);
			}
		});
		tablesWithPrimaryColumnMap = new HashMap<String, List<String>>();

		DocumentTypeDTO selectedDocumentType = controller.getSelectedDocumentType();
		fuzzyDBDTO = selectedDocumentType.getFuzzyDBDTO();
		if (null != fuzzyDBDTO) {
			view.setSelectedFuzzyDBDTO(fuzzyDBDTO);
			List<FuzzyDBMappingDTO> allFuzzyMappings = fuzzyDBDTO.getListOfMappings();
			if (!CollectionUtil.isEmpty(allFuzzyMappings)) {
				view.addNonDeletedMappingsToGrid(allFuzzyMappings);
			}
			ConnectionsDTO connection = selectedDocumentType.getDocumentTypeMappingDTO().getFuzzyDBConnection(); // fuzzyDBDTO.getConnectionsDTO();
			view.setConnection(connection);
			if (null != fuzzyDBDTO) {
				view.setSelectedTable(fuzzyDBDTO.getTableName());
			}
		}

		if (null != selectedDocumentType) {
			List<String> indexFieldNames = getIndexFieldsNames(selectedDocumentType);
			view.setListOfIndexFieldNames(indexFieldNames);
			view.setFieldTypes();
		}
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
				selectedDocumentType.setFuzzyConnection(connection);
			}
		}
	}

	public ConnectionsDTO getCurrentConnection() {
		ConnectionsDTO connection = null;
		DocumentTypeDTO selectedDocumentType = controller.getSelectedDocumentType();
		if (null != selectedDocumentType) {
			FuzzyDBDTO fuzzyDBDTO = selectedDocumentType.getFuzzyDBDTO();
			if (null != fuzzyDBDTO) {
				connection = fuzzyDBDTO.getConnectionsDTO();
			}
		}
		return connection;
	}

	public void setFieldType(final FuzzyDBMappingDTO mappingDTO, final String fieldName) {
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

	public void setTables(final ConnectionsDTO connection) {

		if (null != connection) {
			ScreenMaskUtility.maskScreen();
			rpcService.getAllTables(connection, new AsyncCallback<Map<String, List<String>>>() {

				@Override
				public void onFailure(Throwable caught) {
					ScreenMaskUtility.unmaskScreen();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_CONNECT_TO_THE_DATABASE), DialogIcon.ERROR);
					view.clearComboStores(true, true, true, false, false);
				}

				@Override
				public void onSuccess(Map<String, List<String>> result) {
					ScreenMaskUtility.unmaskScreen();
					tablesWithTypeMap = result;
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
					if (null != fuzzyDBDTO) {
						view.setSelectedTable(fuzzyDBDTO.getTableName());
					}
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
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_FETCH_COLUMNS_FOR_TABLE, tableName),
							DialogIcon.ERROR);
					view.clearComboStores(true, false, true, false, false);
				}

				@Override
				public void onSuccess(Map<String, String> result) {
					ScreenMaskUtility.unmaskScreen();
					if (!CollectionUtil.isEmpty(result)) {
						view.addMappingForColumn(tableName, result);
					}
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
	public void handleIndexFieldMappingEvent(final AddFuzzyDBMappingEvent mappingAdditionEvent) {
		if (null != mappingAdditionEvent) {
			final ConnectionsDTO connection = view.getSelectedConnectionFromCombobox();
			final String tableName = view.getSelectedTableNamefromCombobox();
			final String rowID = view.getSelectedRowIDFromCombobox();

			if (null == connection) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SELECT_A_CONNECTION_FIRST), DialogIcon.ERROR);
			} else if (StringUtil.isNullOrEmpty(tableName)) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SELECT_A_TABLE_FIRST), DialogIcon.ERROR);
			} else if (StringUtil.isNullOrEmpty(rowID)) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SELECT_A_ROW_ID_FIRST), DialogIcon.ERROR);
			} else {
				view.setFieldTypes();
				view.addNewFuzzyMapping(createFuzzyDBMappingMappingDTO());
			}
		}
	}

	@EventHandler
	public void handleIndexFieldMappingEvent(final DeleteFuzzyDBMappingEvent mappingDeletionEvent) {
		if (CollectionUtil.isEmpty(view.getGrid().getStore().getAll())) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR),
					LocaleDictionary.getMessageValue(BatchClassMessages.NO_RECORDS_TO_DELETE), DialogIcon.ERROR);
		} else {
			if (null != mappingDeletionEvent) {
				if (CollectionUtil.isEmpty(view.getSelectedModelsFromGrid())) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR),
							LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SELECT_MAPPINGS_TO_DELETE_FIRST),
							DialogIcon.ERROR);
				} else {
					final ConfirmationDialog dialog = DialogUtil.showConfirmationDialog(
							LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
							LocaleDictionary.getMessageValue(BatchClassMessages.SURE_YOU_WANT_TO_DELETE_THE_SELECTED_MAPPINGS),false,
							DialogIcon.QUESTION_MARK);
	//				dialog.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
					dialog.setDialogListener(new DialogAdapter() {

						@Override
						public void onOkClick() {
							final List<FuzzyDBMappingDTO> selectedModels = view.getSelectedModelsFromGrid();
							if (!CollectionUtil.isEmpty(selectedModels)) {
								view.setFieldTypes();
								view.removeModelsFromGrid(selectedModels);
								if (null != fuzzyDBDTO) {
									for (final FuzzyDBMappingDTO selectedModel : selectedModels) {
										if (null != selectedModel) {
											List<FuzzyDBMappingDTO> allMappingDTO = fuzzyDBDTO.getListOfMappings();
											if (null != allMappingDTO) {
												if (allMappingDTO.contains(selectedModel)) {
													int index = allMappingDTO.indexOf(selectedModel);
													FuzzyDBMappingDTO mappingDTO = allMappingDTO.get(index);
													if (null != mappingDTO) {
														mappingDTO.setDeleted(true);
													}
												}
											}
										}
									}
									fuzzyDBDTO.setNew(true);
									controller.getSelectedBatchClass().setDirty(true);
									dialog.hide();
									Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.DELETE_FUZZY_MAPPING),
											LocaleDictionary.getMessageValue(BatchClassMessages.FUZZYDB_MAPPING_DELETED_SUCCESSFULLY));

								}
							} else {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_INDEX_LEVEL_FIELD_TO_DELETE));
							}
						}

						@Override
						public void onCloseClick() {
						}

						@Override
						public void onCancelClick() {
							// TODO Auto-generated method stub

						}
					});
				}
			}
		}
	}

	private FuzzyDBMappingDTO createFuzzyDBMappingMappingDTO() {
		FuzzyDBMappingDTO mapingDTO = new FuzzyDBMappingDTO();
		mapingDTO.setNew(true);
		//mapingDTO.setId(String.valueOf(RandomIdGenerator.getIdentifier()));
		mapingDTO.setSearchable(true);
		return mapingDTO;
	}

	@EventHandler
	public void handleDBExportResetEvent(ResetFuzzyDBMappingEvent resetEvent) {
		if (null != resetEvent) {
			final List<FuzzyDBMappingDTO> allItems = view.getGrid().getStore().getAll();
			if (CollectionUtil.isEmpty(allItems)) {
				final ConnectionsDTO selectedConnection = view.getSelectedConnectionFromCombobox();
				final String selectedRowID = view.getSelectedRowIDFromCombobox();
				final String selectedTableName = view.getSelectedTableNamefromCombobox();
				if (null != selectedConnection || !StringUtil.isNullOrEmpty(selectedRowID)
						|| !StringUtil.isNullOrEmpty(selectedTableName)) {
					view.clearComboStores(true, true, true, false, false);
					view.clearConnectionCombo();
				} else {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR),
							LocaleDictionary.getMessageValue(BatchClassMessages.NO_RECORDS_TO_RESET), DialogIcon.ERROR);
				}
			} else {
				ConfirmationDialog dialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchClassMessages.SURE_YOU_WANT_TO_RESET_FUZZY_DB_MAPPINGS),false,
						DialogIcon.QUESTION_MARK);
			//	dialog.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
				DialogListener dialogListner = new DialogAdapter() {

					@Override
					public void onOkClick() {
						deleteAllMappings();
						view.clearComboStores(true, true, true, false, false);
						view.clearConnectionCombo();
					}

					@Override
					public void onCloseClick() {

					}

					@Override
					public void onCancelClick() {

					}

					@Override
					public void onYesClick() {
						// TODO Auto-generated method stub

					}
				};
				dialog.setDialogListener(dialogListner);
			}
		}
	}

	public void deleteAllMappings() {
		final List<FuzzyDBMappingDTO> allModels = view.getGrid().getStore().getAll();
		if (null != fuzzyDBDTO) {
			for (FuzzyDBMappingDTO selectedModel : allModels) {
				if (null != selectedModel) {
					List<FuzzyDBMappingDTO> allMappingDTO = fuzzyDBDTO.getListOfMappings();
					if (allMappingDTO.contains(selectedModel)) {
						int index = allMappingDTO.indexOf(selectedModel);
						FuzzyDBMappingDTO mappingDTO = allMappingDTO.get(index);
						if (null != mappingDTO) {
							mappingDTO.setDeleted(true);
						}
					}
				}
			}
			fuzzyDBDTO.setNew(true);
			controller.getSelectedBatchClass().setDirty(true);
		}
		view.getGrid().getStore().clear();
	}

	public void setColumnsPrimaryKey(ConnectionsDTO connectionDTO, final String tableName) {
		final List<String> allPrimaryColumns = tablesWithPrimaryColumnMap.get(tableName);
		if (CollectionUtil.isEmpty(allPrimaryColumns)) {
			if (null != connectionDTO && !StringUtil.isNullOrEmpty(tableName)) {
				String tableType = null;
				for (String type : tablesWithTypeMap.keySet()) {
					List<String> allTypeTables = tablesWithTypeMap.get(type);
					if (allTypeTables.contains(tableName)) {
						tableType = type;
						break;
					}
				}
				if (!StringUtil.isNullOrEmpty(tableType)) {
					ScreenMaskUtility.maskScreen();
					rpcService.getAllPrimaryKeysForTable(connectionDTO, tableName, tableType, new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									LocaleDictionary
											.getMessageValue(BatchClassMessages.UNABLE_TO_FETCH_PRIMARY_KEYS_FOR_SELECTED_TABLE),
									DialogIcon.ERROR);
						}

						@Override
						public void onSuccess(List<String> result) {
							ScreenMaskUtility.unmaskScreen();
							tablesWithPrimaryColumnMap.put(tableName, result);
							view.setRowIDPrimaryKeyColumns(result);
							if (null != fuzzyDBDTO) {
								view.setSelectedRowIDColumn(fuzzyDBDTO.getRowID());
							}
						}
					});
				}
			}
		} else {
			view.setRowIDPrimaryKeyColumns(allPrimaryColumns);
			if (null != fuzzyDBDTO) {
				view.setSelectedRowIDColumn(fuzzyDBDTO.getRowID());
			}
		}
	}

	public void setSaveModelInDTO(FuzzyDBMappingDTO currentSelectedModel) {
		currentSelectedModel.setNew(true);
		FuzzyDBDTO fuzzyDTO = controller.getSelectedDocumentType().getFuzzyDBDTO();
		ConnectionsDTO connection = view.getSelectedConnectionFromCombobox();
		String tableName = view.getSelectedTableNamefromCombobox();
		String rowID = view.getSelectedRowIDFromCombobox();
		List<FuzzyDBMappingDTO> allMappings;
		if (null != connection && !StringUtil.isNullOrEmpty(tableName) && !StringUtil.isNullOrEmpty(rowID)) {
			if (null == fuzzyDTO) {
				fuzzyDTO = new FuzzyDBDTO();
				allMappings = new ArrayList<FuzzyDBMappingDTO>();
				allMappings.add(currentSelectedModel);
				fuzzyDTO.setListOfMappings(allMappings);
			} else {
				allMappings = fuzzyDTO.getListOfMappings();
				if (CollectionUtil.isEmpty(allMappings)) {
					allMappings = new ArrayList<FuzzyDBMappingDTO>();
					allMappings.add(currentSelectedModel);
					fuzzyDTO.setListOfMappings(allMappings);
				} else {
					if (allMappings.contains(currentSelectedModel)) {
						allMappings.remove(currentSelectedModel);
						allMappings.add(currentSelectedModel);
					} else {
						allMappings.add(currentSelectedModel);
					}
				}
			}
			fuzzyDTO.setConnectionsDTO(connection);
			fuzzyDTO.setRowID(rowID);
			fuzzyDTO.setTableName(tableName);
			fuzzyDTO.setNew(true);
			controller.getSelectedDocumentType().setFuzzyConnection(connection);
			controller.getSelectedDocumentType().setFuzzyDBDTO(fuzzyDTO);
			controller.getSelectedBatchClass().setDirty(true);
		}
	}

	public boolean isValid() {
		return view.isValid();
	}

	public boolean validateSearchableRows() {

		boolean isSearchableValid = true;
		int count=0;
		List<FuzzyDBMappingDTO> fuzzyDBMappingDTOLst = view.getGrid().getStore().getAll();
		if (!CollectionUtil.isEmpty(fuzzyDBMappingDTOLst)) {
			
			for (FuzzyDBMappingDTO fuzzyDBMappingDTO : fuzzyDBMappingDTOLst) {
				if (!fuzzyDBMappingDTO.isSearchable()) {
					count++;
				}
			}
			if (count==fuzzyDBMappingDTOLst.size()) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary
								.getMessageValue(BatchClassMessages.ATLEAST_ONE_CHKBOX_SELECTED),
						DialogIcon.WARNING);
				isSearchableValid=false;
			}
		}

		return isSearchableValid;
	}
}
