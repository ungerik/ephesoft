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

package com.ephesoft.gxt.admin.client.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.CompositePresenterSelectionEvent;
import com.ephesoft.gxt.admin.client.event.ValidateColumnExtractionRuleEvent;
import com.ephesoft.gxt.admin.client.event.ValidateKVForAdvKV;
import com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter;
import com.ephesoft.gxt.core.client.Controller;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassModuleDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.EmailConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.KVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.RegexDTO;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.dto.RegexPatternDTO;
import com.ephesoft.gxt.core.shared.dto.RoleDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TestClassificationDataCarrierDTO;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.shared.EventBus;
import com.sencha.gxt.core.client.ValueProvider;

public abstract class BatchClassManagementSelectionController extends Controller {

	protected BatchClassDTO selectedBatchClass;

	protected DocumentTypeDTO selectedDocumentType;

	protected RegexDTO selectedRegexField;

	protected TableInfoDTO selectedTableInfo;

	protected TableInfoDTO currentTableInfoDTO;

	private DocumentLevelFields selectedDlf;

	private DataTable selectedDataTable;

	private Batch receivedBatch;

	protected FieldTypeDTO selectedFieldType;

	protected KVExtractionDTO selectedKVExtraction;

	private int selectedEmailGridCell = 0;

	private Map<String, String> pageIdMappingWithName;

	private BatchClassCompositePresenter<?, ?> currentBindedPresenter;

	protected BatchClassDTO newBatchClass;

	protected BatchClassDTO loadedBatchClass;

	protected TableColumnInfoDTO selectedTableColumnDTO;

	protected TableExtractionRuleDTO selectedTableExtrRule;

	protected TableColumnExtractionRuleDTO selectedColumnExtrRule;

	private List<TestClassificationDataCarrierDTO> testClassificationResultDtos;

	protected Object currentSelectedObject;

	protected ValueProvider currentValueProvider;

	protected BatchClassModuleDTO selectedBatchClassModule;

	protected BatchClassPluginDTO selectedBatchClassPlugin;

	private boolean isBatchClassSelectionChanged = false;

	private boolean isDocumentTypeSelectionChanged = false;

	protected TableColumnExtractionRuleDTO selectedTableColumnExtractionRuleDTO;

	protected TableExtractionRuleDTO currentTblExtrRule;

	protected EmailConfigurationDTO currentEmailConfigDTO;

	private RegexPatternDTO selectedRegexPatternDTO;

	private RegexGroupDTO selectedRegexGroupDTO;

	private Collection<RegexGroupDTO> regexGroupList;

	private List<RoleDTO> roleDTOs;

	public EmailConfigurationDTO getCurrentEmailConfigDTO() {
		return currentEmailConfigDTO;
	}

	public void setCurrentEmailConfigDTO(EmailConfigurationDTO currentEmailConfigDTO) {
		this.currentEmailConfigDTO = currentEmailConfigDTO;
	}

	public TableExtractionRuleDTO getCurrentTblExtrRule() {
		return currentTblExtrRule;
	}

	public void setCurrentTblExtrRule(TableExtractionRuleDTO currentTblExtrRule) {
		this.currentTblExtrRule = currentTblExtrRule;
	}

	public boolean isDocumentTypeSelectionChanged() {
		return isDocumentTypeSelectionChanged;
	}

	public boolean isBatchClassSelectionChanged() {
		return isBatchClassSelectionChanged;
	}

	public BatchClassManagementSelectionController(EventBus eventBus, DCMARemoteServiceAsync rpcService) {
		super(eventBus, rpcService);
	}

	/**
	 * @return the selectedBatchClass
	 */
	public BatchClassDTO getSelectedBatchClass() {
		return selectedBatchClass;
	}

	public DocumentTypeDTO getSelectedDocumentType() {
		return selectedDocumentType;

	}

	/**
	 * @param selectedBatchClass the selectedBatchClass to set
	 */
	public void setSelectedBatchClass(BatchClassDTO selectedBatchClass) {
		if (this.selectedBatchClass == null || this.selectedBatchClass != selectedBatchClass) {
			isBatchClassSelectionChanged = true;
		} else {
			isBatchClassSelectionChanged = false;
		}
		this.selectedBatchClass = selectedBatchClass;
	}

	public TableInfoDTO getCurrentTableInfo() {
		return currentTableInfoDTO;
	}

	public void setCurrentTableInfo(TableInfoDTO currentTableInfoDTO) {
		this.currentTableInfoDTO = currentTableInfoDTO;
	}

	/**
	 * @param selectedBatchClass the selectedBatchClass to set
	 */
	public void setSelectedDocumentType(DocumentTypeDTO selectedDocumentType) {
		if (this.selectedDocumentType == null || this.selectedDocumentType != selectedDocumentType) {
			isDocumentTypeSelectionChanged = true;
		} else {
			isDocumentTypeSelectionChanged = false;

		}
		this.selectedDocumentType = selectedDocumentType;
	}

	public void setSelectedDlfs(DocumentLevelFields dlf) {
		this.selectedDlf = dlf;
	}

	public DocumentLevelFields getSelectedDlfs() {
		return selectedDlf;
	}

	public DataTable getSelectedDataTable() {
		return selectedDataTable;
	}

	public void setSelectedDataTables(DataTable selectedDataTable) {
		this.selectedDataTable = selectedDataTable;
	}

	public TableInfoDTO getSelectedTableInfo() {
		return selectedTableInfo;
	}

	public void setSelectedTableInfo(TableInfoDTO selectedTableInfo) {
		this.selectedTableInfo = selectedTableInfo;
	}

	/**
	 * @return the currentBindedPresenter
	 */
	public BatchClassCompositePresenter<?, ?> getCurrentBindedPresenter() {
		return currentBindedPresenter;
	}

	/**
	 * @param currentBindedPresenter the currentBindedPresenter to set
	 */
	public void setCurrentBindedPresenter(BatchClassCompositePresenter<?, ?> currentBindedPresenter) {
		if (null != currentBindedPresenter) {
			this.currentBindedPresenter = currentBindedPresenter;
		}
	}

	protected void handleNonSiblingNavigationhandler(KeyDownEvent event) {
		if (null != currentBindedPresenter) {
			event.preventDefault();
			if (null != currentSelectedObject) {
				if (currentSelectedObject instanceof KVExtractionDTO) {
					BatchClassManagementEventBus.fireEvent(new ValidateKVForAdvKV(true, true));
				} else if (currentSelectedObject instanceof TableColumnExtractionRuleDTO) {
					BatchClassManagementEventBus.fireEvent(new ValidateColumnExtractionRuleEvent());
				} else {
					BatchClassCompositePresenter<?, ?> childPresenter = currentBindedPresenter.getChildPresenter();
					if (null != childPresenter) {
						CompositePresenterSelectionEvent selectionEvent = new CompositePresenterSelectionEvent(childPresenter);
						selectionEvent.setOnCtrlKey(true);
						BatchClassManagementEventBus.fireEvent(selectionEvent);
					}
				}
			}
		}
	}

	public Map<String, String> getPageIdMappingWithName() {
		return pageIdMappingWithName;
	}

	public void setPageIdMappingWithName(Map<String, String> pageIdMappingWithName) {
		this.pageIdMappingWithName = pageIdMappingWithName;
	}

	public Batch getReceivedBatch() {
		return receivedBatch;
	}

	public void setReceivedBatch(Batch receivedBatch) {
		this.receivedBatch = receivedBatch;
	}

	public FieldTypeDTO getSelectedFieldType() {
		return selectedFieldType;
	}

	public void setSelectedFieldType(FieldTypeDTO selectedFieldType) {
		this.selectedFieldType = selectedFieldType;
	}

	public KVExtractionDTO getSelectedKVExtraction() {
		return selectedKVExtraction;
	}

	public void setSelectedKVExtraction(KVExtractionDTO selectedKVExtraction) {
		this.selectedKVExtraction = selectedKVExtraction;
	}

	public TableColumnInfoDTO getSelectedTableColumnDTO() {
		return selectedTableColumnDTO;
	}

	public void setSelectedTableColumnDTO(TableColumnInfoDTO selectedTableColumnDTO) {
		this.selectedTableColumnDTO = selectedTableColumnDTO;
	}

	public TableExtractionRuleDTO getSelectedTableExtrRule() {
		return selectedTableExtrRule;
	}

	public void setSelectedTableExtrRule(TableExtractionRuleDTO selectedTableExtrRule) {
		this.selectedTableExtrRule = selectedTableExtrRule;
	}

	public TableColumnExtractionRuleDTO getSelectedColumnExtrRule() {
		return selectedColumnExtrRule;
	}

	public void setSelectedColumnExtrRule(TableColumnExtractionRuleDTO selectedColumnExtrRule) {
		this.selectedColumnExtrRule = selectedColumnExtrRule;
	}

	/**
	 * @param selectedRegexField the selectedRegexField to set
	 */
	public void setSelectedRegexField(RegexDTO selectedRegexField) {
		this.selectedRegexField = selectedRegexField;
	}

	/**
	 * @return the selectedRegexField
	 */
	public RegexDTO getSelectedRegexField() {
		return selectedRegexField;
	}

	public List<TestClassificationDataCarrierDTO> getTestClassificationResultDtos() {
		return testClassificationResultDtos;
	}

	public void setTestClassificationResultDtos(List<TestClassificationDataCarrierDTO> testClassificationResultDtos) {
		this.testClassificationResultDtos = testClassificationResultDtos;
	}

	public Object getCurrentSelectedObject() {
		return currentSelectedObject;
	}

	public void setCurrentSelectedObject(Object currentSelectedObject) {
		this.currentSelectedObject = currentSelectedObject;
	}

	public BatchClassModuleDTO getSelectedBatchClassModule() {
		return selectedBatchClassModule;
	}

	public void setSelectedBatchClassModule(BatchClassModuleDTO selectedBatchClassModule) {
		this.selectedBatchClassModule = selectedBatchClassModule;
	}

	public BatchClassPluginDTO getSelectedBatchClassPlugin() {
		return selectedBatchClassPlugin;
	}

	public void setSelectedBatchClassPlugin(BatchClassPluginDTO selectedBatchClassPlugin) {
		this.selectedBatchClassPlugin = selectedBatchClassPlugin;
	}

	public TableColumnExtractionRuleDTO getSelectedTableColumnExtractionRuleDTO() {
		return selectedTableColumnExtractionRuleDTO;
	}

	public void setSelectedTableColumnExtractionRuleDTO(TableColumnExtractionRuleDTO selectedTableColumnExtractionRuleDTO) {
		this.selectedTableColumnExtractionRuleDTO = selectedTableColumnExtractionRuleDTO;
	}

	/**
	 * Nullify current object.
	 */
	public void nullifyExcluding(Object objectToExclude) {

		if (objectToExclude != null) {
			if (objectToExclude != this.selectedDocumentType) {
				this.selectedDocumentType = null;
			}
			if (objectToExclude != this.selectedRegexField) {
				this.selectedRegexField = null;
			}
			if (objectToExclude != this.selectedTableInfo) {
				this.selectedTableInfo = null;
			}
			if (objectToExclude != this.currentTableInfoDTO) {
				this.currentTableInfoDTO = null;
			}
			if (objectToExclude != this.selectedDlf) {
				this.selectedDlf = null;
			}
			if (objectToExclude != this.selectedDataTable) {
				this.selectedDataTable = null;
			}
			if (objectToExclude != this.selectedFieldType) {
				this.selectedFieldType = null;
			}
			if (objectToExclude != this.selectedKVExtraction) {
				this.selectedKVExtraction = null;
			}
			if (objectToExclude != this.selectedTableColumnDTO) {
				this.selectedTableColumnDTO = null;
			}
			if (objectToExclude != this.selectedTableExtrRule) {
				this.selectedTableExtrRule = null;
			}
			if (objectToExclude != this.selectedColumnExtrRule) {
				this.selectedColumnExtrRule = null;
			}
			if (objectToExclude != this.testClassificationResultDtos) {
				this.testClassificationResultDtos = null;
			}
			if (objectToExclude != this.selectedBatchClassModule) {
				this.selectedBatchClassModule = null;
			}
			if (objectToExclude != this.selectedBatchClassPlugin) {
				this.selectedBatchClassPlugin = null;
			}
			if (objectToExclude != this.selectedTableColumnExtractionRuleDTO) {
				this.selectedTableColumnExtractionRuleDTO = null;
			}
			if (objectToExclude != this.currentTblExtrRule) {
				this.currentTblExtrRule = null;
			}
			if (objectToExclude != this.currentEmailConfigDTO) {
				this.currentEmailConfigDTO = null;
			}
		}
	}

	public RegexPatternDTO getSelectedRegexPatternDTO() {
		return selectedRegexPatternDTO;
	}

	public void setSelectedRegexPatternDTO(RegexPatternDTO selectedRegexPatternDTO) {
		this.selectedRegexPatternDTO = selectedRegexPatternDTO;
	}

	public RegexGroupDTO getSelectedRegexGroupDTO() {
		return selectedRegexGroupDTO;
	}

	public void setSelectedRegexGroupDTO(RegexGroupDTO selectedRegexGroupDTO) {
		this.selectedRegexGroupDTO = selectedRegexGroupDTO;
	}

	public Collection<RegexGroupDTO> getRegexGroupList() {
		return regexGroupList;
	}

	public void setRegexGroupList(Collection<RegexGroupDTO> regexGroupList) {
		this.regexGroupList = regexGroupList;
	}

	public ValueProvider getCurrentValueProvider() {
		return currentValueProvider;
	}

	public void setCurrentValueProvider(ValueProvider currentValueProvider) {
		this.currentValueProvider = currentValueProvider;
	}

	public List<RoleDTO> getRoleDTOs() {
		return roleDTOs;
	}

	public void setRoleDTOs(List<RoleDTO> roleDTOs) {
		this.roleDTOs = roleDTOs;
	}

}
