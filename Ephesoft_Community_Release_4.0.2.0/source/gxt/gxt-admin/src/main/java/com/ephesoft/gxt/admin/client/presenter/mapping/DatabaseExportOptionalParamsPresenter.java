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
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.OptionalParametersRenderEvent;
import com.ephesoft.gxt.admin.client.event.OptionalParametersVisibilityEvent;
import com.ephesoft.gxt.admin.client.event.ResetDBExportMappingEvent;
import com.ephesoft.gxt.admin.client.event.TableMappingOpenEvent;
import com.ephesoft.gxt.admin.client.event.TableOptionalParametersRenderEvent;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.mapping.DatabaseExportOptionalParamsView;
import com.ephesoft.gxt.core.shared.dto.DetailsDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.IndexFieldDBExportMappingDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.IndexFieldDBExportMappingDTO.IndexFieldOptionalParametersDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnDBExportMappingDTO.TableColumnOptionalParametersDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnDBExportMappingDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class DatabaseExportOptionalParamsPresenter extends BatchClassInlinePresenter<DatabaseExportOptionalParamsView> {

	interface CustomEventBinder extends EventBinder<DatabaseExportOptionalParamsPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public DatabaseExportOptionalParamsPresenter(BatchClassManagementController controller, DatabaseExportOptionalParamsView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void handleOptionalParamsVisibilityEvent(OptionalParametersVisibilityEvent visibilityEvent) {
		if (null != visibilityEvent) {
			view.setVisible(visibilityEvent.isVisible());
		}
	}

	@EventHandler
	public void handleOptionalParametersRenderEvent(final OptionalParametersRenderEvent renderEvent) {
		if (null != renderEvent) {
			IndexFieldDBExportMappingDTO mappingToShow = renderEvent.getCurrentIndexFieldMapping();
			if (null != mappingToShow) {
				List<IndexFieldDBExportMappingDTO> allBindedFields = renderEvent.getAllBindedFields();
				view.setCurrentIndexFieldMapping(mappingToShow);
				view.setMappingList(allBindedFields);
				view.setCurrentTableColumnDTO(null);
				view.setTableColumnMappingList(null);
				String tableName = mappingToShow.getTableName();
				List<DetailsDTO> detailsList = getDetailsDTOByTableName(tableName, allBindedFields);
				view.setSingleRowDetail(detailsList);
				List<IndexFieldOptionalParametersDTO> optionalDBExportParameters = mappingToShow.getOptionalDBExportParameters();
				view.addOptionalParameters(optionalDBExportParameters, controller.getSelectedBatchClass());
				view.setColumns(renderEvent.getColumnsList());
			}
		}
	}

	@EventHandler
	public void handleDBExportResetEvent(final ResetDBExportMappingEvent event) {
		if (null != event) {
			view.setVisible(false);
		}
	}

	private List<DetailsDTO> getDetailsDTOByTableName(String tableName, final List<IndexFieldDBExportMappingDTO> mappingList) {
		List<DetailsDTO> list = null;
		if (!StringUtil.isNullOrEmpty(tableName) && !CollectionUtil.isEmpty(mappingList)) {
			list = new ArrayList<DetailsDTO>();
			for (IndexFieldDBExportMappingDTO mappingDTO : mappingList) {
				FieldTypeDTO bindedField = mappingDTO.getBindedField();
				if (null != mappingDTO && null != bindedField && tableName.equalsIgnoreCase(mappingDTO.getTableName())) {
					list.add(new DetailsDTO(bindedField.getName(), mappingDTO.getColumnName()));
				}
			}
		}
		return list;
	}

	private List<DetailsDTO> getDetailsDTOByTableName(String tableName, final List<TableColumnDBExportMappingDTO> mappingList,
			final TableColumnDBExportMappingDTO columnMapping) {
		List<DetailsDTO> list = null;
		if (!StringUtil.isNullOrEmpty(tableName) && !CollectionUtil.isEmpty(mappingList) && null != columnMapping) {
			list = new ArrayList<DetailsDTO>();
			TableColumnInfoDTO columnInfo = columnMapping.getBindedTableColumn();
			TableInfoDTO tableInfo = columnInfo == null ? null : columnInfo.getTableInfoDTO();
			if (null != tableInfo) {
				for (TableColumnDBExportMappingDTO mappingDTO : mappingList) {
					TableColumnInfoDTO bindedField = mappingDTO.getBindedTableColumn();
					if (null != mappingDTO && null != bindedField && tableName.equalsIgnoreCase(mappingDTO.getTableName())) {
						TableColumnInfoDTO newColumnInfo = mappingDTO.getBindedTableColumn();
						TableInfoDTO newTableInfo = newColumnInfo == null ? null : newColumnInfo.getTableInfoDTO();
						if (newTableInfo == tableInfo) {
							list.add(new DetailsDTO(bindedField.getColumnName(), mappingDTO.getColumnName()));
						}
					}
				}
			}
		}
		return list;
	}

	@EventHandler
	public void handleTableOptionalParamsRenderEvent(final TableOptionalParametersRenderEvent tableOptionalParametersRenderEvent) {
		if (null != tableOptionalParametersRenderEvent) {
			TableColumnDBExportMappingDTO currentTableColumnMapping = tableOptionalParametersRenderEvent
					.getCurrentTableColumnMapping();
			if (null != currentTableColumnMapping) {
				List<TableColumnDBExportMappingDTO> allBindedColumnMapping = tableOptionalParametersRenderEvent
						.getAllBindedColumnMapping();
				if (!CollectionUtil.isEmpty(allBindedColumnMapping)) {
					view.setCurrentTableColumnDTO(currentTableColumnMapping);
					view.setTableColumnMappingList(allBindedColumnMapping);
					view.setCurrentIndexFieldMapping(null);
					view.setMappingList(null);
					String tableName = currentTableColumnMapping.getTableName();
					List<DetailsDTO> detailsDTOList = getDetailsDTOByTableName(tableName, allBindedColumnMapping,
							currentTableColumnMapping);
					view.setSingleRowDetail(detailsDTOList);
					List<TableColumnOptionalParametersDTO> optionalDBExportParameters = currentTableColumnMapping
							.getOptionalDBExportParameters();
					view.addOptionalParameters(optionalDBExportParameters, controller.getSelectedBatchClass());
					view.setColumns(tableOptionalParametersRenderEvent.getColumnsList());
				}
			}
		}
	}
	
	@EventHandler
	public void handleTableMappingOpenEvent(final TableMappingOpenEvent mappingOpenEvent) {
		if (null != mappingOpenEvent) {
			view.setVisible(false);
		}
	}
}
