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

package com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction.advancedtablecolumnextractionrule;

import java.util.HashMap;
import java.util.Map;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionChangeEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionInputPanelEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableExtractionFireColumnChangeEvent;
import com.ephesoft.gxt.admin.client.event.SetOverlayCoordinatesEvent;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.view.tablecolumnextraction.advcancedtablecolumnextractionrule.AdvancedTableColumnExtractionInputView;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.PointCoordinate;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class AdvancedTableColumnExtractionInputPresenter extends
		AdvancedTableColumnExtractionInlinePresenter<AdvancedTableColumnExtractionInputView> {

	interface CustomEventBinder extends EventBinder<AdvancedTableColumnExtractionInputPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private static final Object HEADING_SEPARATOR = " :: ";

	private String displayImageName;

	private Map<String, TableColumnExtractionRuleDTO> columnNameToDTOMap;

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public AdvancedTableColumnExtractionInputPresenter(BatchClassManagementController controller,
			AdvancedTableColumnExtractionInputView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
		columnNameToDTOMap = controller.getAdvTableColumnExtractionPresenter().getColumnNameToDTOMap();
		TableColumnExtractionRuleDTO selectedTableColumnExtractionRuleDTO = controller.getSelectedTableColumnExtractionRuleDTO();
		view.setImageName(AdminConstants.EMPTY_STRING);
		view.setHeadingText(getHeadingText());
		if (selectedTableColumnExtractionRuleDTO != null) {
			TableExtractionRuleDTO tableExtractionRuleDTO = selectedTableColumnExtractionRuleDTO.getTableExtractionRuleDTO();
			TableInfoDTO selectedTableInfoField = tableExtractionRuleDTO.getTableInfoDTO();
			this.displayImageName = selectedTableInfoField.getDisplayImage();
			view.setColStartCoord(selectedTableColumnExtractionRuleDTO.getColumnStartCoordinate());
			view.setColEndCoord(selectedTableColumnExtractionRuleDTO.getColumnEndCoordinate());
			view.setTableColumnExtractionRuleList(tableExtractionRuleDTO);
			view.setSelectedTableColumn(selectedTableColumnExtractionRuleDTO.getColumnName());
			if (null != displayImageName) {
				view.setImageName(displayImageName);
			}
		}
	}

	public void clearColumnDTOMap() {
		if (this.columnNameToDTOMap != null) {
			this.columnNameToDTOMap.clear();
		}
	}

	public void addToDtoMap(String columnName, TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO) {
		if (this.columnNameToDTOMap == null) {
			this.columnNameToDTOMap = new HashMap<String, TableColumnExtractionRuleDTO>();
		}
		TableColumnExtractionRuleDTO localTableColDTO = createLocalDTO(tableColumnExtractionRuleDTO);
		this.columnNameToDTOMap.put(columnName, localTableColDTO);
	}

	private TableColumnExtractionRuleDTO createLocalDTO(final TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO) {
		final TableColumnExtractionRuleDTO columnInfoDTO = new TableColumnExtractionRuleDTO();
		controller.getAdvTableColumnExtractionPresenter().mergeTableColumnsInfoDTOs(columnInfoDTO, tableColumnExtractionRuleDTO);
		return columnInfoDTO;
	}

	@EventHandler
	public void setStartEndCoordinate(SetOverlayCoordinatesEvent event) {
		PointCoordinate startCoord = event.getStartCoord();
		PointCoordinate endCoord = event.getEndCoord();
		view.setColStartCoord(String.valueOf(startCoord.getxCoordinate()));
		view.setColEndCoord(String.valueOf(endCoord.getxCoordinate()));

		// Setting coordinates value in temporary objects.
		TableColumnExtractionRuleDTO ruleDTO = columnNameToDTOMap.get(view.getSelectedTableColumn());
		if (null != ruleDTO) {
			ruleDTO.setColumnStartCoordinate(String.valueOf(startCoord.getxCoordinate()));
			ruleDTO.setColumnEndCoordinate(String.valueOf(endCoord.getxCoordinate()));
			ruleDTO.setColumnCoordY0(String.valueOf(startCoord.getyCoordinate()));
			ruleDTO.setColumnCoordY1(String.valueOf(endCoord.getyCoordinate()));
		}
	}

	@EventHandler
	public void updateInputPanel(AdvancedTableColumnExtractionInputPanelEvent event) {
		if (event.getColumnName() != null && !event.getColumnName().equals(AdminConstants.EMPTY_STRING)) {
			view.setSelectedTableColumn(event.getColumnName());
		}
		if (event.getDisplayImageName() != null && !event.getDisplayImageName().equals(AdminConstants.EMPTY_STRING)) {
			view.setImageName(event.getDisplayImageName());
		}
		if (event.getStartCoord() != null) {
			view.setColStartCoord(event.getStartCoord());
		}
		if (event.getEndCoord() != null) {
			view.setColEndCoord(event.getEndCoord());
		}
	}

	public void setSelectedTableColumnExtractionRuleDTO(final String selColumnIdentfier) {
		if (selColumnIdentfier != null && !selColumnIdentfier.isEmpty()) {
			TableColumnExtractionRuleDTO extractionRuleDTO = controller.getSelectedTableColumnExtractionRuleDTO();
			if (null != extractionRuleDTO) {
				TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO = extractionRuleDTO.getTableExtractionRuleDTO()
						.getTableColumnExtractionRuleByColumnName(selColumnIdentfier);
				controller.setSelectedTableColumnExtractionRuleDTO(tableColumnExtractionRuleDTO);
				BatchClassManagementEventBus.fireEvent(new AdvancedTableColumnExtractionChangeEvent());
			}
		}
	}

	@EventHandler
	public void fireSelectedColumnValueChangeEvent(AdvancedTableExtractionFireColumnChangeEvent event) {
		view.fireSelectedColumnValueChangeEvent();
	}

	private String getHeadingText() {
		TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO = controller.getSelectedColumnExtrRule();
		StringBuilder heading = new StringBuilder();
		if (null != tableColumnExtractionRuleDTO) {
			TableExtractionRuleDTO tableExtractionRuleDTO = tableColumnExtractionRuleDTO.getTableExtractionRuleDTO();
			if(null!=tableExtractionRuleDTO){
				TableInfoDTO tableInfoDTO = tableExtractionRuleDTO.getTableInfoDTO();
				if(null!=tableInfoDTO){
					DocumentTypeDTO docTypeDTO=tableInfoDTO.getDocTypeDTO();
					if(null!=docTypeDTO){
						BatchClassDTO batchClassDTO = docTypeDTO.getBatchClass();
						if(null!=batchClassDTO){
							heading.append(batchClassDTO.getIdentifier());
						}
						heading.append(HEADING_SEPARATOR);
						heading.append(docTypeDTO.getName());
					}
					heading.append(HEADING_SEPARATOR);
					heading.append(tableInfoDTO.getName());
				}
				heading.append(HEADING_SEPARATOR);
				heading.append(tableExtractionRuleDTO.getRuleName());
			}
		}
		return heading.toString();
	}
}
