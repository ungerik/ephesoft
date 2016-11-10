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

package com.ephesoft.gxt.core.shared.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

public class BatchClassDTO implements IsSerializable, Selectable {

	private long id;
	private String identifier;
	private String name;
	private int priority;
	private String description;
	private String uncFolder;
	private String version;
	private boolean isDirty;
	private boolean isDeleted;
	private boolean isDeployed;
	private String encryptionAlgo;

	private boolean selected;
	/**
	 * Current user who has locked the batch class.
	 */
	private String currentUser;

	private Map<String, WebScannerConfigurationDTO> scannerMap = new LinkedHashMap<String, WebScannerConfigurationDTO>();;

	private Map<String, BatchClassModuleDTO> moduleMap = new LinkedHashMap<String, BatchClassModuleDTO>();

	private Map<String, DocumentTypeDTO> documentsMap = new LinkedHashMap<String, DocumentTypeDTO>();

	private Map<String, ScannerMasterDTO> scannerMasterMap = new LinkedHashMap<String, ScannerMasterDTO>();

	private Map<String, EmailConfigurationDTO> emailMap = new LinkedHashMap<String, EmailConfigurationDTO>();

	private Map<String, FieldTypeDTO> indexFieldsMap = new LinkedHashMap<String, FieldTypeDTO>();

	private Map<String, RegexDTO> regexValidationMap = new LinkedHashMap<String, RegexDTO>();

	private Map<String, CmisConfigurationDTO> cmisMap = new LinkedHashMap<String, CmisConfigurationDTO>();

	private Map<String, BatchClassFieldDTO> batchClassFieldMap = new LinkedHashMap<String, BatchClassFieldDTO>();

	private List<RoleDTO> assignedRole = new ArrayList<RoleDTO>();

	/**
	 * Map of group identifiers vs regex group Dtos.
	 */
	private Map<String, RegexGroupDTO> regexGroupMap;

	/**
	 * Boolean to indicate if regex pool configuration has been changed.
	 */
	private boolean isRegexPoolDirty;

	/**
	 * listTablePoolColumnInfoDTO {@link List} <{@link TableColumnInfoDTO}>.
	 */
	private List<TableColumnInfoDTO> tablePoolColumnsInfoDTOList;

	private List<RuleInfoDTO> ruleInfoDTOList;

	/**
	 * listLockStatusDTOs {@link List<{@link LockStatusDTO>} The list of all the locked feature.
	 */
	private List<LockStatusDTO> listLockStatusDTOs;

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
		setDirty(true);
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		setDirty(true);
	}

	public String getUncFolder() {
		return uncFolder;
	}

	public void setUncFolder(String uncFolder) {
		this.uncFolder = uncFolder;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Collection<BatchClassModuleDTO> getModules() {
		return getModules(false);
	}

	public Collection<BatchClassModuleDTO> getModules(boolean includeDeleted) {
		Collection<BatchClassModuleDTO> values = moduleMap.values();
		if (!includeDeleted) {
			Map<String, BatchClassModuleDTO> batchClassModulesMap = new LinkedHashMap<String, BatchClassModuleDTO>();

			for (BatchClassModuleDTO batchClassModuleDTO : values) {
				if (!(batchClassModuleDTO.isDeleted())) {
					batchClassModulesMap.put(batchClassModuleDTO.getIdentifier(), batchClassModuleDTO);
				}
			}
			values = batchClassModulesMap.values();
		}
		return values;

	}

	public void addModule(BatchClassModuleDTO batchClassModuleDTO) {
		this.moduleMap.put(batchClassModuleDTO.getIdentifier(), batchClassModuleDTO);
	}

	public void removeModule(BatchClassModuleDTO batchClassModuleDTO) {
		this.moduleMap.remove(batchClassModuleDTO.getIdentifier());
	}

	/**
	 * 
	 * @return a collection of the documents that are present in the batchClass and have not been soft deleted.
	 */
	public Collection<DocumentTypeDTO> getDocuments() {
		Map<String, DocumentTypeDTO> dMap = new LinkedHashMap<String, DocumentTypeDTO>();
		for (DocumentTypeDTO documentTypeDTO : documentsMap.values())
			if (!(documentTypeDTO.isDeleted()))
				dMap.put(documentTypeDTO.getIdentifier(), documentTypeDTO);
		return dMap.values();
	}

	/**
	 * 
	 * @param includeDeleted : includes the soft deleted documents if set true
	 * @return a collection of documents present in the batch class
	 */
	public Collection<DocumentTypeDTO> getDocuments(boolean includeDeleted) {
		if (includeDeleted)
			return documentsMap.values();
		return getDocuments();
	}

	/**
	 * Adds a new document type to this batch
	 * 
	 * @param documentTypeDTO the documentTypeDTO to be added to this batch class.
	 */
	public void addDocumentType(DocumentTypeDTO documentTypeDTO) {
		documentsMap.put(documentTypeDTO.getIdentifier(), documentTypeDTO);
	}

	/**
	 * Remove a document type
	 * 
	 * @param documentTypeDTO the documentTypeDTO to be removed
	 */
	public void removeDocumentType(DocumentTypeDTO documentTypeDTO) {
		documentsMap.remove(documentTypeDTO.getIdentifier());
	}

	public boolean isDirty() {
		return isDirty;
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
	}

	/**
	 * Returns the document type based on name
	 * 
	 * @param name the name of the document
	 * @return document type DTO based on provided name
	 */
	public DocumentTypeDTO getDocTypeByName(String name) {
		Collection<DocumentTypeDTO> dtos = documentsMap.values();
		if (dtos != null)
			for (DocumentTypeDTO documentTypeDTO : dtos) {
				if (documentTypeDTO.getName().equalsIgnoreCase(name)) {
					return documentTypeDTO;
				}
			}
		return null;
	}

	/**
	 * Api to check if the name of document is same
	 * 
	 * @param name the name to check
	 * @return true if a document by the name exists. False otherwise
	 */
	public boolean checkDocumentTypeName(String name) {
		if (getDocTypeByName(name) != null)
			return true;
		return false;
	}

	/**
	 * Api to return the module based on provided identifier
	 * 
	 * @param identifier the identifier of the module required
	 * @return BathcClassModuleDTO based on identifier
	 */
	public BatchClassModuleDTO getModuleByIdentifier(String identifier) {
		if (this.moduleMap != null && !this.moduleMap.isEmpty()) {
			for (BatchClassModuleDTO batchClassModuleDTO : this.moduleMap.values()) {
				if (batchClassModuleDTO.getIdentifier().equals(identifier))
					return batchClassModuleDTO;
			}
		}
		return null;
	}

	public DocumentTypeDTO getDocTypeByIdentifier(String identifier) {
		if (this.documentsMap != null && !this.documentsMap.isEmpty()) {
			for (DocumentTypeDTO documentTypeDTO : this.documentsMap.values()) {
				if (documentTypeDTO.getIdentifier().equals(identifier))
					return documentTypeDTO;
			}
		}
		return null;
	}

	public BatchClassModuleDTO getModuleByName(String name) {
		if (this.moduleMap != null && !this.moduleMap.isEmpty()) {
			for (BatchClassModuleDTO batchClassModuleDTO : this.moduleMap.values()) {
				if (!batchClassModuleDTO.isDeleted()) {
					if (batchClassModuleDTO.getModule().getName().equals(name))
						return batchClassModuleDTO;
				}
			}
		}
		return null;
	}

	public BatchClassModuleDTO getModuleByWorkflowName(String name) {
		BatchClassModuleDTO batchClassModuleDTO = null;
		if (this.moduleMap != null && !this.moduleMap.isEmpty()) {
			for (BatchClassModuleDTO batchClassModuleDTOObject : this.moduleMap.values()) {
				if (batchClassModuleDTOObject.getWorkflowName().equals(name)) {
					batchClassModuleDTO = batchClassModuleDTOObject;
					break;
				}
			}
		}
		return batchClassModuleDTO;
	}

	public void addEmailConfiguration(EmailConfigurationDTO emailConfigurationDTO) {
		this.emailMap.put(emailConfigurationDTO.getIdentifier(), emailConfigurationDTO);
	}

	public void addIndexFieldConfiguration(FieldTypeDTO indexFieldConfigurationDTO) {
		this.indexFieldsMap.put(indexFieldConfigurationDTO.getIdentifier(), indexFieldConfigurationDTO);
	}

	public void addRegexValidationConfiguration(RegexDTO regexConfigurationDTO) {
		this.regexValidationMap.put(regexConfigurationDTO.getIdentifier(), regexConfigurationDTO);
	}

	public void addCmisConfiguration(CmisConfigurationDTO cmisConfigurationDTO) {
		this.cmisMap.put(cmisConfigurationDTO.getIdentifier(), cmisConfigurationDTO);
	}

	public WebScannerConfigurationDTO getWebScannerConfigurationById(String identifier) {
		if (this.scannerMap != null && !this.scannerMap.isEmpty()) {
			for (WebScannerConfigurationDTO scannerConfigurationDTO : this.scannerMap.values()) {
				if (scannerConfigurationDTO.getIdentifier().equals(identifier))
					return scannerConfigurationDTO;
			}
		}
		return null;
	}

	public WebScannerConfigurationDTO getScannerConfigurationByIdentifier(String identifier) {
		if (this.scannerMap != null && !this.scannerMap.isEmpty()) {
			for (WebScannerConfigurationDTO dto : this.scannerMap.values()) {
				if (dto.getIdentifier().equals(identifier))
					return dto;
			}
		}
		return null;
	}

	public WebScannerConfigurationDTO getScannerConfigurationByProfileName(String profileName) {
		Collection<WebScannerConfigurationDTO> dtos = scannerMap.values();
		if (dtos != null)
			for (WebScannerConfigurationDTO dto : dtos) {
				if (dto.getValue().equals(profileName)) {
					return dto;
				}
			}
		return null;
	}

	public EmailConfigurationDTO getEmailConfigurationDTOByIdentifier(String identifier) {
		if (this.emailMap != null && !this.emailMap.isEmpty()) {
			for (EmailConfigurationDTO emailConfigurationDTO : this.emailMap.values()) {
				if (emailConfigurationDTO.getIdentifier().equals(identifier))
					return emailConfigurationDTO;
			}
		}
		return null;
	}

	public void removeEmailConfiguration(EmailConfigurationDTO emailConfigurationDTO) {
		this.emailMap.remove(emailConfigurationDTO.getIdentifier());
	}

	public CmisConfigurationDTO getCmisConfigurationDTOByIdentifier(String identifier) {
		if (this.cmisMap != null && !this.cmisMap.isEmpty()) {
			for (CmisConfigurationDTO cmisConfigurationDTO : this.cmisMap.values()) {
				if (cmisConfigurationDTO.getIdentifier().equals(identifier))
					return cmisConfigurationDTO;
			}
		}
		return null;
	}

	public void removeCmisConfiguration(CmisConfigurationDTO cmisConfigurationDTO) {
		this.cmisMap.remove(cmisConfigurationDTO.getIdentifier());
	}

	/**
	 * 
	 * @return a collection of the documents that are present in the batchClass and have not been soft deleted.
	 */
	public Collection<EmailConfigurationDTO> getEmailConfiguration() {
		Map<String, EmailConfigurationDTO> eMap = new LinkedHashMap<String, EmailConfigurationDTO>();
		for (EmailConfigurationDTO emailConfigurationDTO : emailMap.values())
			if (!(emailConfigurationDTO.isDeleted()))
				eMap.put(emailConfigurationDTO.getIdentifier(), emailConfigurationDTO);
		return eMap.values();
	}

	/**
	 * 
	 * @return a collection of the documents that are present in the batchClass and have not been soft deleted.
	 */
	public Collection<CmisConfigurationDTO> getCmisConfiguration() {
		Map<String, CmisConfigurationDTO> eMap = new LinkedHashMap<String, CmisConfigurationDTO>();
		for (CmisConfigurationDTO cmisConfigurationDTO : cmisMap.values())
			if (!(cmisConfigurationDTO.isDeleted()))
				eMap.put(cmisConfigurationDTO.getIdentifier(), cmisConfigurationDTO);
		return eMap.values();
	}

	/**
	 * 
	 * @return a collection of the documents that are present in the batchClass and have not been soft deleted.
	 */
	public Collection<WebScannerConfigurationDTO> getScannerConfiguration() {
		Map<String, WebScannerConfigurationDTO> sMap = new LinkedHashMap<String, WebScannerConfigurationDTO>();
		for (WebScannerConfigurationDTO dto : scannerMap.values())
			if (!(dto.isDeleted()))
				sMap.put(dto.getIdentifier(), dto);
		return sMap.values();
	}

	public Collection<WebScannerConfigurationDTO> getScannerConfiguration(boolean includeDeleted) {
		if (includeDeleted) {
			return scannerMap.values();
		}
		return getScannerConfiguration();
	}

	/**
	 * 
	 * @param includeDeleted : includes the soft deleted documents if set true
	 * @return a collection of documents present in the batch class
	 */
	public Collection<CmisConfigurationDTO> getCmisConfiguration(boolean includeDeleted) {
		if (includeDeleted) {
			return cmisMap.values();
		}
		return getCmisConfiguration();
	}

	public CmisConfigurationDTO getCmisConfigurationByFields(String serverURL, String userName, String password, String repositoryID,
			String fileExtension, String folderName, String cmisProperty, String value, String valueToUpdate) {
		Collection<CmisConfigurationDTO> dtos = cmisMap.values();
		if (dtos != null)
			for (CmisConfigurationDTO cmisConfigurationDTO : dtos) {
				if (cmisConfigurationDTO.getUserName().equals(userName) && cmisConfigurationDTO.getPassword().equals(password)
						&& cmisConfigurationDTO.getServerURL().equals(serverURL)
						&& cmisConfigurationDTO.getRepositoryID().equals(repositoryID)
						&& cmisConfigurationDTO.getFolderName().equals(folderName)
						&& cmisConfigurationDTO.getFileExtension().equals(fileExtension)
						&& cmisConfigurationDTO.getValue().equals(value)
						&& cmisConfigurationDTO.getValueToUpdate().equals(valueToUpdate)) {
					return cmisConfigurationDTO;
				}
			}
		return null;
	}

	/**
	 * 
	 * @param includeDeleted : includes the soft deleted documents if set true
	 * @return a collection of documents present in the batch class
	 */
	public Collection<EmailConfigurationDTO> getEmailConfiguration(boolean includeDeleted) {
		if (includeDeleted) {
			return emailMap.values();
		}
		return getEmailConfiguration();
	}

	public EmailConfigurationDTO getEmailConfigurationByFields(String userName, String password, String serverName, String folderName) {
		Collection<EmailConfigurationDTO> dtos = emailMap.values();
		if (dtos != null)
			for (EmailConfigurationDTO emailConfigurationDTO : dtos) {
				if (emailConfigurationDTO.getUserName().equals(userName) && emailConfigurationDTO.getServerName().equals(serverName)
						&& emailConfigurationDTO.getFolderName().equals(folderName)) {
					return emailConfigurationDTO;
				}
			}
		return null;
	}

	public boolean checkEmailConfiguration(String username, String password, String serverName, String folderName) {
		boolean returnValue = false;
		if (getEmailConfigurationByFields(username, password, serverName, folderName) != null) {
			returnValue = true;
		}
		return returnValue;
	}

	public boolean checkCmisConfiguration(String serverURL, String userName, String password, String repositoryID,
			String fileExtension, String folderName, String cmisProperty, String value, String valueToUpdate) {
		boolean returnValue = false;
		if (getCmisConfigurationByFields(serverURL, userName, password, repositoryID, fileExtension, folderName, cmisProperty, value,
				valueToUpdate) != null) {
			returnValue = true;
		}
		return returnValue;
	}

	public void addScannerConfiguration(WebScannerConfigurationDTO dto) {
		this.scannerMap.put(dto.getIdentifier(), dto);
	}

	public void addBatchClassField(BatchClassFieldDTO batchClassFieldDTO) {
		this.batchClassFieldMap.put(batchClassFieldDTO.getIdentifier(), batchClassFieldDTO);
	}

	public BatchClassFieldDTO getBatchClassFieldDTOByIdentifier(String identifier) {
		BatchClassFieldDTO batchClassFieldDTO = null;
		if (this.batchClassFieldMap != null && !this.batchClassFieldMap.isEmpty()) {
			batchClassFieldDTO = batchClassFieldMap.get(identifier);
		}
		return batchClassFieldDTO;
	}

	public void removeBatchClassField(BatchClassFieldDTO batchClassFieldDTO) {
		this.batchClassFieldMap.remove(batchClassFieldDTO.getIdentifier());
	}

	/**
	 * 
	 * @return a collection of the batch class field that are present in the batch class and have not been soft deleted.
	 */
	public Collection<BatchClassFieldDTO> getBatchClassField() {
		Map<String, BatchClassFieldDTO> bCFMap = new LinkedHashMap<String, BatchClassFieldDTO>();
		for (BatchClassFieldDTO batchClassFieldDTO : batchClassFieldMap.values()) {
			if (!(batchClassFieldDTO.isDeleted()))
				bCFMap.put(batchClassFieldDTO.getIdentifier(), batchClassFieldDTO);
		}

		return bCFMap.values();

	}

	/**
	 * Returns the batch class field based on name excluding the given batchClassFieldId
	 * 
	 * @param name the name of the document
	 * @return batch class field DTO based on provided name
	 */
	public BatchClassFieldDTO getBatchClassFieldByName(String name, String batchClassFieldId) {
		Collection<BatchClassFieldDTO> batchClassFields = batchClassFieldMap.values();
		BatchClassFieldDTO batchClassFieldDto = null;
		if (batchClassFields != null && !batchClassFields.isEmpty() && batchClassFieldId != null) {
			for (BatchClassFieldDTO batchClassFieldDTO : batchClassFields) {
				if (!batchClassFieldDTO.getIdentifier().equals(batchClassFieldId)
						&& batchClassFieldDTO.getName().equalsIgnoreCase(name)) {
					batchClassFieldDto = batchClassFieldDTO;
					break;
				}
			}
		}
		return batchClassFieldDto;
	}

	/**
	 * Returns the batch class field based on name
	 * 
	 * @param name the name of the document
	 * @return batch class field DTO based on provided name
	 */
	public BatchClassFieldDTO getBatchClassFieldByName(String name) {
		Collection<BatchClassFieldDTO> batchClassFields = batchClassFieldMap.values();
		BatchClassFieldDTO batchClassFieldDto = null;
		if (batchClassFields != null) {
			for (BatchClassFieldDTO batchClassFieldDTO : batchClassFields) {
				if (batchClassFieldDTO.getName().equalsIgnoreCase(name)) {
					batchClassFieldDto = batchClassFieldDTO;
					break;
				}
			}
		}
		return batchClassFieldDto;
	}

	/**
	 * Api to check if the batch class field with given name exists excluding the given batchClassFieldId
	 * 
	 * @param name the name to check
	 * @return true if a batch class field by the name exists. False otherwise
	 */
	public boolean checkBatchClassFieldName(String name, String batchClassFieldId) {
		BatchClassFieldDTO batchClassFieldDTO = null;
		boolean isExists = false;
		if (batchClassFieldId == null) {
			batchClassFieldDTO = getBatchClassFieldByName(name);
		} else {
			batchClassFieldDTO = getBatchClassFieldByName(name, batchClassFieldId);
		}
		if (batchClassFieldDTO != null) {
			isExists = true;
		}
		return isExists;
	}

	/**
	 * 
	 * @param includeDeleted : includes the soft deleted batch class field if set true
	 * @return a collection of batch class field present in the batch class
	 */
	public Collection<BatchClassFieldDTO> getBatchClassField(boolean includeDeleted) {
		if (includeDeleted) {
			return batchClassFieldMap.values();
		}
		return getBatchClassField();
	}

	public List<RoleDTO> getAssignedRole() {
		return assignedRole;
	}

	public void addAssignedRole(RoleDTO roleDTO) {
		this.assignedRole.add(roleDTO);
	}

	public void setAssignedRole(List<RoleDTO> assignedRole) {
		this.assignedRole = assignedRole;
	}

	/**
	 * @return the isDeleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the isDeployed
	 */
	public boolean isDeployed() {
		return isDeployed;
	}

	/**
	 * @param isDeployed the isDeployed to set
	 */
	public void setDeployed(boolean isDeployed) {
		this.isDeployed = isDeployed;
	}

	public void addScannerMaster(ScannerMasterDTO dto) {
		scannerMasterMap.put(dto.getName(), dto);
	}

	public Map<String, ScannerMasterDTO> getScannerMasterMap() {
		return scannerMasterMap;
	}

	/**
	 * Setter for the listTablePoolColumnInfoDTO.
	 * 
	 * @param listTablePoolColumnInfoDTO {@link List} <{@link TableColumnInfoDTO}>
	 */
	public void setTablePoolColumnsInfoDTOList(final List<TableColumnInfoDTO> listTablePoolColumnInfoDTO) {
		this.tablePoolColumnsInfoDTOList = listTablePoolColumnInfoDTO;
	}

	/**
	 * Getter for the listTablePoolColumnInfoDTO.
	 * 
	 * @return listTablePoolColumnInfoDTO {@link List} <{@link TableColumnInfoDTO}>
	 */
	public List<TableColumnInfoDTO> getTablePoolColumnsInfoDTOList() {
		if (tablePoolColumnsInfoDTOList == null) {
			tablePoolColumnsInfoDTOList = new ArrayList<TableColumnInfoDTO>(10);
		}
		return tablePoolColumnsInfoDTOList;
	}

	/**
	 * Setter for the listRuleInfoDTO.
	 * 
	 * @param listRuleInfoDTOList
	 */

	public void setRuleInfoDTOList(final List<RuleInfoDTO> listRuleInfoDTOList) {
		this.ruleInfoDTOList = listRuleInfoDTOList;
	}

	/**
	 * Getter for the listRuleInfoDTO.
	 * 
	 * @return listRuleInfoDTO {@link List} <{@link RuleInfoDTO}>
	 */
	public List<RuleInfoDTO> getRuleInfoDTOList() {
		if (ruleInfoDTOList == null) {
			ruleInfoDTOList = new ArrayList<RuleInfoDTO>(10);
		}
		return ruleInfoDTOList;
	}

	/**
	 * Getter for the list of all the undeleted table columns present in the pool.
	 * 
	 * @return {@link List} <{@link TableColumnInfoDTO}>
	 */
	public List<TableColumnInfoDTO> getNonDeletedTableColumnsPoolDTOList() {
		final List<TableColumnInfoDTO> listColumnInfoDTOs = new ArrayList<TableColumnInfoDTO>();
		if (tablePoolColumnsInfoDTOList != null && !tablePoolColumnsInfoDTOList.isEmpty()) {
			for (TableColumnInfoDTO tableColumnInfoDTO : tablePoolColumnsInfoDTOList) {
				if (!tableColumnInfoDTO.isDeleted()) {
					listColumnInfoDTOs.add(tableColumnInfoDTO);
				}
			}
		}
		return listColumnInfoDTOs;
	}

	public List<RuleInfoDTO> getNonDeletedRuleInfoDTOList() {
		final List<RuleInfoDTO> listRuleInfoDTOs = new ArrayList<RuleInfoDTO>();
		if (ruleInfoDTOList != null && !ruleInfoDTOList.isEmpty()) {
			for (RuleInfoDTO ruleInfoDTO : ruleInfoDTOList) {
				if (!ruleInfoDTO.isDeleted()) {
					listRuleInfoDTOs.add(ruleInfoDTO);
				}
			}
		}
		return listRuleInfoDTOs;
	}

	/**
	 * Adds regex group.
	 * 
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @return boolean true if regex group added successfully.
	 */
	public boolean addRegexGroupDTO(final RegexGroupDTO regexGroupDTO) {
		boolean success = false;
		if (null != regexGroupDTO && null != regexGroupMap) {
			regexGroupMap.put(regexGroupDTO.getIdentifier(), regexGroupDTO);
			success = true;
		}
		return success;
	}

	/**
	 * Gets regex group by identifier.
	 * 
	 * @param identifier {@link String}
	 * @return {@link RegexGroupDTO} null if DTO was not fetched.
	 */
	public RegexGroupDTO getRegexGroupDTOById(final String identifier) {
		RegexGroupDTO selectedRegexGroupDTO = null;
		if (null != identifier && null != regexGroupMap && !regexGroupMap.isEmpty()) {
			for (RegexGroupDTO regexGroupDTO : regexGroupMap.values()) {
				if (null != regexGroupDTO && identifier.equals(regexGroupDTO.getIdentifier())) {
					selectedRegexGroupDTO = regexGroupDTO;
				}
			}
		}
		return selectedRegexGroupDTO;
	}

	/**
	 * Removes regex group.
	 * 
	 * @param regexGroupDTO {@link RegexGroupDTO}
	 * @return boolean true if regex group removed successfully from set of regex group dtos.
	 */
	public boolean removeRegexGroupDTO(final RegexGroupDTO regexGroupDTO) {
		boolean success = false;
		if (null != regexGroupDTO) {
			regexGroupMap.remove(regexGroupDTO.getIdentifier());
			success = true;
		}
		return success;
	}

	/**
	 * Gets all non deleted regex groups <code>RegexGroupDTO</code>.
	 * 
	 * @return {@link Collection<{@link RegexGroupDTO}>} a collection of all regex groups present in application.
	 */
	public Collection<RegexGroupDTO> getRegexGroups() {
		Collection<RegexGroupDTO> regexGroupDTOList = null;
		if (null != regexGroupMap) {
			regexGroupDTOList = new ArrayList<RegexGroupDTO>(regexGroupMap.size());
			for (RegexGroupDTO regexGroupDTO : regexGroupMap.values()) {
				if (null != regexGroupDTO && !regexGroupDTO.isDeleted()) {
					regexGroupDTOList.add(regexGroupDTO);
				}
			}
		}
		return regexGroupDTOList;
	}

	/**
	 * Gets non deleted regex groups <code>RegexGroupDTO</code> of a type.
	 * 
	 * @param type {@link String}
	 * @return {@link Collection<{@link RegexGroupDTO}>} a collection of regex groups of particular type present in application.
	 */
	public Collection<RegexGroupDTO> getRegexGroups(final String type) {
		Collection<RegexGroupDTO> regexGroupDTOList = null;
		if (null != regexGroupMap && null != type) {
			regexGroupDTOList = new ArrayList<RegexGroupDTO>(regexGroupMap.size());
			for (RegexGroupDTO regexGroupDTO : regexGroupMap.values()) {
				if (null != regexGroupDTO && !(regexGroupDTO.isDeleted()) && type.equalsIgnoreCase(regexGroupDTO.getType())) {
					regexGroupDTOList.add(regexGroupDTO);
				}
			}
		}
		return regexGroupDTOList;
	}

	/**
	 * Gets all or non deleted regex groups <code>RegexGroupDTO</code> of a regex type depending on value of includeDeleted parameter
	 * passed.
	 * 
	 * @param type {@link String} regex group type
	 * @param includeDeleted boolean includes the soft deleted regex groups if set true.
	 * @return {@link Collection<{@link RegexGroupDTO}>} a collection of regex groups of particular type present in application.
	 */
	public Collection<RegexGroupDTO> getRegexGroups(final String type, final boolean includeDeleted) {
		Collection<RegexGroupDTO> regexGroupDTOList = null;
		if (null != regexGroupMap && null != type) {
			if (includeDeleted) {
				regexGroupDTOList = new ArrayList<RegexGroupDTO>(regexGroupMap.size());
				Collection<RegexGroupDTO> regexGroupsAllTypes = regexGroupMap.values();
				if (null != regexGroupsAllTypes) {
					for (RegexGroupDTO regexGroupDTO : regexGroupsAllTypes) {
						if (null != regexGroupDTO && type.equalsIgnoreCase(regexGroupDTO.getType())) {
							regexGroupDTOList.add(regexGroupDTO);
						}
					}
				}
			} else {
				regexGroupDTOList = getRegexGroups(type);
			}
		}
		return regexGroupDTOList;
	}

	/**
	 * Gets all or non deleted regex groups <code>RegexGroupDTO</code> depending on value of includeDeleted parameter passed.
	 * 
	 * @param includeDeleted boolean includes the soft deleted regex groups if set true.
	 * @return {@link Collection<{@link RegexGroupDTO}>} a collection of regex groups present in application.
	 */
	public Collection<RegexGroupDTO> getRegexGroups(final boolean includeDeleted) {
		Collection<RegexGroupDTO> regexGroupDTOList = null;
		if (null != regexGroupMap) {
			if (includeDeleted) {
				regexGroupDTOList = regexGroupMap.values();
			} else {
				regexGroupDTOList = getRegexGroups();
			}
		}
		return regexGroupDTOList;
	}

	/**
	 * Sets regex groups map.
	 * 
	 * @param regexGroupMap {@link Map<{@link String}, {@link RegexGroupDTO}>}
	 */
	public void setRegexGroupMap(final Map<String, RegexGroupDTO> regexGroupMap) {
		this.regexGroupMap = regexGroupMap;
	}

	/**
	 * Gets regex groups map.
	 * 
	 * @return {@link Map<{@link String}, {@link RegexGroupDTO}>}
	 */
	public Map<String, RegexGroupDTO> getRegexGroupMap() {
		return regexGroupMap;
	}

	/**
	 * Checks if the name provided for a new regex group is already in use by some other regex group. Performs a case insensitive
	 * search on the regex group name.
	 * 
	 * @param groupName {@link String} the name of the regex group to check for availability.
	 * @return boolean, <code>true</code> if the provided name is not taken by any other existing group, <code>false</code> otherwise.
	 */
	public boolean checkGroupName(final String groupName) {
		boolean isGroupNameInUse = false;
		if (null != groupName) {
			if (null != getRegexGroupDTOByName(groupName)) {
				isGroupNameInUse = true;
			}
		}
		return isGroupNameInUse;
	}

	/**
	 * Checks if the name edited for a new regex group is already in use by some other regex group. Performs a case insensitive search
	 * on the regex group name.
	 * 
	 * @param groupName {@link String} edited name of the regex group to check for availability.
	 * @param identifier {@link String} identifier of the editing regex group.
	 * @return boolean, <code>true</code> if the provided name is not taken by any other existing group, <code>false</code> otherwise.
	 */
	public boolean checkGroupName(final String groupName, final String identifier) {
		boolean isGroupNameInUse = false;
		if (null != groupName && null != identifier) {
			RegexGroupDTO searchedRegexGroupDTO = getRegexGroupDTOByName(groupName);
			if (null != searchedRegexGroupDTO && !identifier.equals(searchedRegexGroupDTO.getIdentifier())) {
				isGroupNameInUse = true;
			}
		}
		return isGroupNameInUse;
	}

	/**
	 * Gets regex group by name.
	 * 
	 * @param name {@link String}
	 * @return {@link RegexGroupDTO} null if DTO was not fetched.
	 */
	public RegexGroupDTO getRegexGroupDTOByName(final String name) {
		RegexGroupDTO selectedRegexGroupDTO = null;
		if (null != name && null != regexGroupMap && !regexGroupMap.isEmpty()) {
			for (RegexGroupDTO regexGroupDTO : regexGroupMap.values()) {
				if (null != regexGroupDTO && !regexGroupDTO.isDeleted() && name.equalsIgnoreCase(regexGroupDTO.getName())) {
					selectedRegexGroupDTO = regexGroupDTO;
					break;
				}
			}
		}
		return selectedRegexGroupDTO;
	}

	/**
	 * Gets if regex pool is dirty.
	 * 
	 * @return boolean true if regex pool configuration has been changed.
	 */
	public boolean isRegexPoolDirty() {
		return isRegexPoolDirty;
	}

	/**
	 * Sets if regex pool is dirty.
	 * 
	 * @param isRegexPoolDirty boolean.
	 */
	public void setRegexPoolDirty(final boolean isRegexPoolDirty) {
		this.isRegexPoolDirty = isRegexPoolDirty;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * Checks if the table column pattern provided is already in use by some other table column in the table column pool or not.
	 * Performs a case insensitive search on the table column pattern.
	 * 
	 * @param tableColumnPattern {@link String} the name of the table column pattern to checked for availability.
	 * @param identifier {@link String} represents the table identifier or table id.
	 * @return boolean, <code>true</code> if the provided name is not taken by any other existing table column, <code>false</code>
	 *         otherwise.
	 */
	public boolean checkTableColumnPattern(final String tableColumnPattern, final String identifier) {
		boolean isTableColumnNameInUse = false;
		if (getTableColumnInfoDTOByColumnPattern(tableColumnPattern, identifier) != null) {
			isTableColumnNameInUse = true;
		}
		return isTableColumnNameInUse;
	}

	/**
	 * Gets the <code>TableColumnInfoDTO</code> object for table column in the table column pool which is not deleted and has the given
	 * name.
	 * 
	 * @param name {@link String} the name of the table column pattern to be searched.
	 * @param identifier {@link String} represents the table identifier or table id.
	 * @return {@link TableColumnInfoDTO} table column info DTO for the table column with the given in this table, returns
	 *         <code>null</code> if no such table column is found.
	 */
	public TableColumnInfoDTO getTableColumnInfoDTOByColumnPattern(final String columnPattern, final String identifier) {
		TableColumnInfoDTO tableColumnInfoDTO = null;
		for (TableColumnInfoDTO columnInfoDTO : tablePoolColumnsInfoDTOList) {
			if (columnInfoDTO != null && !identifier.equalsIgnoreCase(columnInfoDTO.getIdentifier()) && !columnInfoDTO.isDeleted()
					&& columnPattern.equals(columnInfoDTO.getColumnPattern())) {
				tableColumnInfoDTO = columnInfoDTO;
				break;
			}
		}
		return tableColumnInfoDTO;
	}

	/**
	 * Checks if the table column name provided is already in use by some other table column in the table column pool or not. Performs
	 * a case insensitive search on the table column name.
	 * 
	 * @param tableColumnName {@link String} the name of the table column to checked for availability.
	 * @param identifier {@link String} represents the table identifier or table id.
	 * @return boolean, <code>true</code> if the provided name is not taken by any other existing table column, <code>false</code>
	 *         otherwise.
	 */
	public boolean checkTableColumnName(final String tableColumnName, final String identifier) {
		boolean isTableColumnNameInUse = false;
		if (getTableColumnInfoDTOByName(tableColumnName, identifier) != null) {
			isTableColumnNameInUse = true;
		}
		return isTableColumnNameInUse;
	}

	/**
	 * Gets the <code>TableColumnInfoDTO</code> object for table column in the table column pool which is not deleted and has the given
	 * name. Performs a case insensitive search for table column name.
	 * 
	 * @param name {@link String} the name of the table column to be searched.
	 * @param identifier {@link String} represents the table identifier or table id.
	 * @return {@link TableColumnInfoDTO} table column info DTO for the table column with the given in the table column pool, returns
	 *         <code>null</code> if no such table column is found.
	 */
	public TableColumnInfoDTO getTableColumnInfoDTOByName(final String name, final String identifier) {
		TableColumnInfoDTO tableColumnInfoDTO = null;
		for (TableColumnInfoDTO columnInfoDTO : tablePoolColumnsInfoDTOList) {
			if (columnInfoDTO != null && !identifier.equalsIgnoreCase(columnInfoDTO.getIdentifier()) && !columnInfoDTO.isDeleted()
					&& name.equalsIgnoreCase(columnInfoDTO.getColumnName())) {
				tableColumnInfoDTO = columnInfoDTO;
				break;
			}
		}
		return tableColumnInfoDTO;
	}

	/**
	 * @return the listLockStatusDTOs
	 */
	public List<LockStatusDTO> getListLockStatusDTOs() {
		return listLockStatusDTOs;
	}

	/**
	 * @param listLockStatusDTOs the listLockStatusDTOs to set
	 */
	public void setListLockStatusDTOs(final List<LockStatusDTO> listLockStatusDTOs) {
		this.listLockStatusDTOs = listLockStatusDTOs;
	}

	/**
	 * Checks if document types list contains any document type other than Unknown or not.
	 * 
	 * @param includeDeleted boolean value to decide whether to include deleted Document type or not.
	 * @return returns a boolean value, returns false in case DocumentList of {@link BatchClassDTO} is empty or<br>
	 *         contains one element with name as Unknown.Otherwise returns true.
	 */
	public boolean hasDocumentType(final boolean includeDeleted) {
		boolean isDocumentTypePresent = true;
		final Collection<DocumentTypeDTO> documents = getDocuments(includeDeleted);
		if (null == documents) {
			isDocumentTypePresent = false;
		} else {
			if (documents.isEmpty()) {
				isDocumentTypePresent = false;
			} else {
				if (documents.size() == 1) {
					final String documentName = documents.iterator().next().getName();
					if (null != documentName && documentName.equalsIgnoreCase(BatchConstants.DOCUMENT_TYPE_UNKNOWN)) {
						isDocumentTypePresent = false;
					}
				}
			}
		}
		return isDocumentTypePresent;
	}

	/**
	 * @param encryptionAlgo the encryptionAlgo to set
	 */
	public void setEncryptionAlgo(String encryptionAlgo) {
		this.encryptionAlgo = encryptionAlgo;
	}

	/**
	 * @return the encryptionAlgo
	 */
	public String getEncryptionAlgo() {
		return encryptionAlgo;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public boolean isNew() {
		return false;
	}

}
