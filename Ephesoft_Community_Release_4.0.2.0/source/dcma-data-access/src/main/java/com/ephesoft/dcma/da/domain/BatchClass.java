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

package com.ephesoft.dcma.da.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NaturalId;

import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.encryption.util.EphesoftEncryptionUtil;
import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;

@Entity
@Table(name = "batch_class")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class BatchClass extends AbstractChangeableEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String BATCH_CLASS_ID = "batch_class_id";

	private transient BatchClassID batchClassID;

	@Column(name = "batch_class_name")
	private String name;

	@Column(name = "identifier")
	private String identifier;

	@Column(name = "priority")
	private int priority;

	@Column(name = "batch_class_description")
	private String description;

	@Column(name = "batch_class_version")
	private String version;

	@Column(name = "unc_folder")
	@NaturalId(mutable = true)
	private String uncFolder;

	@Column(name = "process_name")
	private String processName;

	@Column(name = "curr_user")
	private String currentUser;

	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "advanced_batch_class", nullable = true)
	private Boolean advancedBatchClass;

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	@javax.persistence.OrderBy("orderNumber")
	private List<BatchClassModule> batchClassModules;

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<DocumentType> documentTypes;

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<BatchClassEmailConfiguration> emailConfigurations;

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<BatchClassField> batchClassField;

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<BatchClassScannerConfiguration> batchClassScannerConfiguration;

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<BatchClassGroups> assignedGroups;

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = BATCH_CLASS_ID)
	private List<BatchClassCmisConfiguration> cmisConfigurations;

	/**
	 * Encryption algorithm associated with the batch class. If null then batch instances associated with the batch will be
	 * Un-encrypted.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "encryption_algorithm")
	private PrivateKeyEncryptionAlgorithm encryptionAlgorithm;

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
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUncFolder() {
		return uncFolder;
	}

	public void setUncFolder(String uncFolder) {
		this.uncFolder = uncFolder;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public List<BatchClassModule> getBatchClassModules() {
		return batchClassModules;
	}

	public void setBatchClassModules(List<BatchClassModule> batchClassModules) {
		this.batchClassModules = batchClassModules;
	}

	public List<DocumentType> getDocumentTypes() {
		if (documentTypes.size() > 100) {
			return documentTypes.subList(0, 100);
		}
		return documentTypes;
	}

	public void setDocumentTypes(List<DocumentType> documentTypes) {
		this.documentTypes = documentTypes;
	}

	public List<BatchClassScannerConfiguration> getBatchClassScannerConfiguration() {
		return batchClassScannerConfiguration;
	}

	public void setBatchClassScannerConfiguration(List<BatchClassScannerConfiguration> batchClassScannerConfiguration) {
		this.batchClassScannerConfiguration = batchClassScannerConfiguration;
	}

	/**
	 * @return the currentUser
	 */
	public String getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser the currentUser to set
	 */
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	/**
	 * @return the lastModifiedBy
	 */
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	/**
	 * @param lastModifiedBy the lastModifiedBy to set
	 */
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public List<BatchClassGroups> getAssignedGroups() {
		return assignedGroups;
	}

	public void setAssignedGroups(List<BatchClassGroups> assignedRoles) {
		this.assignedGroups = assignedRoles;
	}

	@Transient
	public BatchClassID getBatchClassID() {
		if (getId() != 0 && batchClassID == null) {
			batchClassID = new BatchClassID(identifier);
		}
		return batchClassID;
	}

	/**
	 * Returns the Module based on ID
	 * 
	 * @param identifier the id of the module required
	 * @return the module corresponding to the supplied id
	 */
	public BatchClassModule getBatchClassModuleById(long identifier) {
		BatchClassModule batchClassModule1 = null;
		if (!(this.batchClassModules == null || this.batchClassModules.isEmpty())) {
			for (BatchClassModule batchClassModule : this.batchClassModules) {
				if (batchClassModule.getId() == identifier) {
					batchClassModule1 = batchClassModule;
					break;
				}
			}
		}
		return batchClassModule1;
	}

	/**
	 * API to return the batch class module by name.
	 * 
	 * @param moduleName {@link String} the name of the module required
	 * @return {@link BatchClassModule} the module corresponding to the supplied name
	 */
	public BatchClassModule getBatchClassModuleByName(String moduleName) {
		BatchClassModule batchClassModule1 = null;
		if (!(this.batchClassModules == null || this.batchClassModules.isEmpty())) {
			for (BatchClassModule batchClassModule : this.batchClassModules) {
				if (batchClassModule.getModule().getName().equalsIgnoreCase(moduleName)) {
					batchClassModule1 = batchClassModule;
					break;
				}
			}
		}
		return batchClassModule1;
	}

	/**
	 * API to return the batch class module by name.
	 * 
	 * @param moduleName {@link String} the name of the module required
	 * @return {@link BatchClassModule} the module corresponding to the supplied name
	 */
	public BatchClassModule getBatchClassModuleByDesc(String moduleName) {
		BatchClassModule batchClassModule1 = null;
		if (!(this.batchClassModules == null || this.batchClassModules.isEmpty())) {
			for (BatchClassModule batchClassModule : this.batchClassModules) {
				if (batchClassModule.getModule().getDescription().equalsIgnoreCase(moduleName)) {
					batchClassModule1 = batchClassModule;
					break;
				}
			}
		}
		return batchClassModule1;
	}

	/**
	 * API to return the batch class module by workflow name.
	 * 
	 * @param workflowName {@link String} the workflowName of the module required
	 * @return {@link BatchClassModule} the module corresponding to the supplied name
	 */
	public BatchClassModule getBatchClassModuleByWorkflowName(String workflowName) {
		BatchClassModule batchClassModule1 = null;
		if (!(this.batchClassModules == null || this.batchClassModules.isEmpty())) {
			for (BatchClassModule batchClassModule : this.batchClassModules) {
				if (batchClassModule.getWorkflowName().equalsIgnoreCase(workflowName)) {
					batchClassModule1 = batchClassModule;
					break;
				}
			}
		}
		return batchClassModule1;
	}

	/**
	 * Removes the document type from this batch class based on the identifier Used to delete a document type
	 * 
	 * @param identifier the identifier of the document type that is to be removed
	 * @return true if the document was found and removed else false.
	 */
	public boolean removeDocumentTypeByIdentifier(String identifier) {
		boolean isRemoved = false;
		List<DocumentType> documentTypesList = new ArrayList<DocumentType>();
		documentTypesList.addAll(this.documentTypes);
		for (DocumentType documentType : documentTypesList) {
			if (identifier.equals(documentType.getIdentifier())) {
				isRemoved = documentTypes.remove(documentType);
				break;
			}
		}
		return isRemoved;
	}

	public boolean removeScannerConfigIdentifier(String identifier) {
		boolean isRemoved = false;
		List<BatchClassScannerConfiguration> scannerList = new ArrayList<BatchClassScannerConfiguration>();
		scannerList.addAll(this.batchClassScannerConfiguration);
		for (BatchClassScannerConfiguration config : scannerList) {
			if (identifier.equals(String.valueOf(config.getId()))) {
				// remove the child config and parent config
				isRemoved = batchClassScannerConfiguration.remove(config);
				if (isRemoved) {
					for (BatchClassScannerConfiguration childConfig : config.getChildren()) {
						isRemoved = batchClassScannerConfiguration.remove(childConfig);
					}
					break;
				}
			}
		}
		return isRemoved;
	}

	/**
	 * Removes the email configuration from this batch class based on the identifier Used to delete a email configuration
	 * 
	 * @param identifier the identifier of the email configuration that is to be removed
	 * @return true if the email configuration was found and removed else false.
	 */
	public boolean removeEmailConfigurationByIdentifier(String identifier) {
		boolean isRemoved = false;
		List<BatchClassEmailConfiguration> emailBatchClassEmailConfigurationsList = new ArrayList<BatchClassEmailConfiguration>();
		emailBatchClassEmailConfigurationsList.addAll(this.emailConfigurations);
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : emailBatchClassEmailConfigurationsList) {
			if (identifier.equals(String.valueOf(batchClassEmailConfiguration.getId()))) {
				isRemoved = this.emailConfigurations.remove(batchClassEmailConfiguration);
				break;
			}
		}
		return isRemoved;
	}

	/**
	 * Removes the cmis configuration from this batch class based on the identifier Used to delete a cmis configuration
	 * 
	 * @param identifier the identifier of the cmis configuration that is to be removed
	 * @return true if the cmis configuration was found and removed else false.
	 */
	public boolean removeCmisConfigurationByIdentifier(String identifier) {
		boolean isRemoved = false;
		List<BatchClassCmisConfiguration> batchClassCmisConfigurationsList = new ArrayList<BatchClassCmisConfiguration>();
		batchClassCmisConfigurationsList.addAll(this.cmisConfigurations);
		for (BatchClassCmisConfiguration batchClassCmisConfiguration : batchClassCmisConfigurationsList) {
			if (identifier.equals(String.valueOf(batchClassCmisConfiguration.getId()))) {
				isRemoved = this.cmisConfigurations.remove(batchClassCmisConfiguration);
				break;
			}
		}
		return isRemoved;
	}

	/**
	 * Removes the batch class field from this batch class based on the identifier Used to delete a batch class field
	 * 
	 * @param identifier the identifier of the batch class field that is to be removed
	 * @return true if the batch class field was found and removed else false.
	 */
	public boolean removeBatchClassFieldByIdentifier(String identifier) {
		boolean isRemoved = false;
		List<BatchClassField> batchClassFieldList = new ArrayList<BatchClassField>();
		batchClassFieldList.addAll(this.batchClassField);
		for (BatchClassField batchClassField : batchClassFieldList) {
			if (identifier.equals(batchClassField.getIdentifier())) {
				isRemoved = this.batchClassField.remove(batchClassField);
				break;
			}
		}
		return isRemoved;
	}

	/**
	 * Returns the document type corresponding to the identifier passed
	 * 
	 * @param identifier the identifier of the document that is needed
	 * @return document type if found else null
	 */
	public DocumentType getDocumentTypeByIdentifier(String identifier) {
		DocumentType documentType = null;
		if (!(this.documentTypes == null || this.documentTypes.isEmpty())) {
			for (DocumentType documentType2 : this.documentTypes) {
				if (documentType2.getIdentifier().equals(identifier)) {
					documentType = documentType2;
					break;
				}
			}
		}
		return documentType;
	}

	/**
	 * Returns the document type corresponding to the identifier passed
	 * 
	 * @param docTypeName the identifier of the document that is needed
	 * @return document type if found else null
	 */
	public DocumentType getDocumentTypeByName(String docTypeName) {
		DocumentType documentType = null;
		if (!(this.documentTypes == null || this.documentTypes.isEmpty())) {
			for (DocumentType iteratedDocType : this.documentTypes) {
				if (iteratedDocType.getName().equalsIgnoreCase(docTypeName)) {
					documentType = iteratedDocType;
					break;
				}
			}
		}
		return documentType;
	}

	/**
	 * Returns the email configuration corresponding to the identifier passed
	 * 
	 * @param identifier the identifier of the email configuration that is needed
	 * @return email configuration if found else null
	 */
	public BatchClassEmailConfiguration getEmailConfigurationByIdentifier(String identifier) {
		BatchClassEmailConfiguration batchClassEmailConfiguration1 = null;
		if (!(this.emailConfigurations == null || this.emailConfigurations.isEmpty())) {
			for (BatchClassEmailConfiguration batchClassEmailConfiguration : this.emailConfigurations) {
				if (identifier.equals(String.valueOf(batchClassEmailConfiguration.getId()))) {
					batchClassEmailConfiguration1 = batchClassEmailConfiguration;
					break;
				}
			}
		}
		return batchClassEmailConfiguration1;
	}

	/**
	 * Returns the cmis configuration corresponding to the identifier passed
	 * 
	 * @param identifier the identifier of the cmis configuration that is needed
	 * @return email configuration if found else null
	 */
	public BatchClassCmisConfiguration getCmisConfigurationByIdentifier(String identifier) {
		BatchClassCmisConfiguration batchClassCmisConfiguration1 = null;
		if (!(this.cmisConfigurations == null || this.cmisConfigurations.isEmpty())) {
			for (BatchClassCmisConfiguration batchClassCmisConfiguration : this.cmisConfigurations) {
				if (identifier.equals(String.valueOf(batchClassCmisConfiguration.getId()))) {
					batchClassCmisConfiguration1 = batchClassCmisConfiguration;
					break;
				}
			}
		}
		return batchClassCmisConfiguration1;
	}

	/**
	 * Returns the batch class field corresponding to the identifier passed
	 * 
	 * @param identifier the identifier of the batch class field that is needed
	 * @return batch class field if found else null
	 */
	public BatchClassField getBatchClassFieldByIdentifier(String identifier) {
		BatchClassField batchClassField1 = null;
		if (!(this.batchClassField == null || this.batchClassField.isEmpty())) {
			for (BatchClassField batchClassField : this.batchClassField) {
				if (identifier.equals(batchClassField.getIdentifier())) {
					batchClassField1 = batchClassField;
					break;
				}
			}
		}
		return batchClassField1;
	}

	/**
	 * Adds a document type to this batch class
	 * 
	 * @param documentType the document type that is to be added in this batch class
	 */
	public void addDocumentType(DocumentType documentType) {
		this.documentTypes.add(documentType);
	}

	/**
	 * Adds a email configuration to this batch class
	 * 
	 * @param batchClassEmailConfiguration the email configuration that is to be added in this batch class
	 */
	public void addEmailConfiguration(BatchClassEmailConfiguration batchClassEmailConfiguration) {
		this.emailConfigurations.add(batchClassEmailConfiguration);
	}

	/**
	 * Adds a cmis configuration to this batch class
	 * 
	 * @param batchClassCmisConfiguration the cmis configuration that is to be added in this batch class
	 */
	public void addCmisConfiguration(BatchClassCmisConfiguration batchClassCmisConfiguration) {
		this.cmisConfigurations.add(batchClassCmisConfiguration);
	}

	public void addScannerConfiguration(BatchClassScannerConfiguration batchClassscannerConfiguration) {
		this.batchClassScannerConfiguration.add(batchClassscannerConfiguration);
	}

	/**
	 * Adds a batch class field to this batch class
	 * 
	 * @param batchClassField the batch class field that is to be added in this batch class
	 */
	public void addBatchClassField(BatchClassField batchClassField) {
		this.batchClassField.add(batchClassField);
	}

	public void postPersist() {
		super.postPersist();
		this.identifier = EphesoftProperty.BATCH_CLASS.getProperty() + Long.toHexString(this.getId()).toUpperCase();
		if (this.documentTypes != null && !this.documentTypes.isEmpty()) {
			for (DocumentType documentType : documentTypes) {
				documentType.postPersist();
			}
		}
		if (this.batchClassField != null && !this.batchClassField.isEmpty()) {
			for (BatchClassField batchClassField1 : batchClassField) {
				batchClassField1.postPersist();
			}
		}
	}

	public List<BatchClassField> getBatchClassField() {
		return batchClassField;
	}

	public void setBatchClassField(List<BatchClassField> batchClassField) {
		this.batchClassField = batchClassField;
	}

	public List<BatchClassEmailConfiguration> getEmailConfigurations() {
		return emailConfigurations;
	}

	public void setEmailConfigurations(List<BatchClassEmailConfiguration> emailConfigurations) {
		this.emailConfigurations = emailConfigurations;
	}

	public List<BatchClassCmisConfiguration> getCmisConfigurations() {
		return cmisConfigurations;
	}

	public void setCmisConfigurations(List<BatchClassCmisConfiguration> cmisConfigurations) {
		this.cmisConfigurations = cmisConfigurations;
	}

	/**
	 * @return the deleted
	 */
	public Boolean isDeleted() {
		if (this.isDeleted == null) {
			this.isDeleted = Boolean.FALSE;
		}
		return isDeleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean isDeleted) {
		if (isDeleted == null) {
			isDeleted = Boolean.FALSE;
		}
		this.isDeleted = isDeleted;
	}

	public void removeModuleByIdentifier(String moduleIdentifier) {
		List<BatchClassModule> batchClassModules = new ArrayList<BatchClassModule>(this.batchClassModules);
		for (BatchClassModule batchClassModule : batchClassModules) {
			if (moduleIdentifier.equals(String.valueOf(batchClassModule.getId()))) {
				this.batchClassModules.remove(batchClassModule);
				break;
			}
		}
	}

	public void addModule(BatchClassModule batchClassModule) {
		this.batchClassModules.add(batchClassModule);
	}

	public BatchClassScannerConfiguration getScannerConfigByIdentifier(String identifier) {
		BatchClassScannerConfiguration config1 = null;
		if (!(this.batchClassScannerConfiguration == null || this.batchClassScannerConfiguration.isEmpty())) {
			for (BatchClassScannerConfiguration config : this.batchClassScannerConfiguration) {
				if (identifier.equals(String.valueOf(config.getId()))) {
					config1 = config;
					break;
				}
			}
		}
		return config1;
	}

	/**
	 * Fetches the list of batch class plugins with the given plugin name. All the modules are checked for any plugin with the given
	 * name and an accumulated list is returned.
	 * 
	 * @param pluginName {@link Strict}, the name of the plugin to be searched in the batch class.
	 * @return {@link List} < {@link BatchClassPlugin}>, returns null if no such plugin exists in the batch class.
	 */
	public List<BatchClassPlugin> getBatchClassPluginsByPluginName(String pluginName) {
		List<BatchClassPlugin> batchClassPlugins = null;
		for (BatchClassModule batchClassModule : this.batchClassModules) {
			List<BatchClassPlugin> batchClassPluginsModule = batchClassModule.getBatchClassPluginsByPluginName(pluginName);
			if (batchClassPluginsModule != null && !batchClassPluginsModule.isEmpty()) {
				if (batchClassPlugins == null) {
					batchClassPlugins = new ArrayList<BatchClassPlugin>(batchClassPluginsModule.size());
				}
				batchClassPlugins.addAll(batchClassPluginsModule);
			}
		}
		return batchClassPlugins;
	}

	/**
	 * Fetches the list of batch class plugins with the given plugin name. All the modules are checked for any plugin with the given
	 * name and an accumulated list is returned.
	 * 
	 * @param pluginName {@link Strict}, the name of the plugin to be searched in the batch class.
	 * @return {@link List} < {@link BatchClassPlugin}>, returns null if no such plugin exists in the batch class.
	 */
	public List<BatchClassPluginConfig> getBatchClassPluginsByDataType(DataType dataType) {
		List<BatchClassPluginConfig> batchClassPluginConfigs = null;
		if (this.batchClassModules != null) {
			for (BatchClassModule batchClassModule : this.batchClassModules) {
				List<BatchClassPluginConfig> batchClassPluginConfigsByDataType = batchClassModule
						.getBatchClassPluginConfigsByDataType(dataType);
				if (batchClassPluginConfigs == null) {
					batchClassPluginConfigs = new ArrayList<BatchClassPluginConfig>();
				}
				// Batch Class modules with No Plugin Configs should be added. It should not throw NULL pointer exception.
				if (batchClassPluginConfigsByDataType != null) {
					batchClassPluginConfigs.addAll(batchClassPluginConfigsByDataType);
				}
			}
		}
		return batchClassPluginConfigs;
	}

	public void encryptAllPasswords() {
		// Encrypt plugin passwords
		encryptPluginPasswords();
		// Encrypt CMIS import repositories password
		encryptCMISPasswords();
		// Encrypt Email import passwords
		encryptMailPasswords();
	}

	public void encryptMailPasswords() {
		if (this.emailConfigurations != null) {
			for (BatchClassEmailConfiguration batchClassEmailConfiguration : this.emailConfigurations) {
				String mailPassword = batchClassEmailConfiguration.getPassword();
				mailPassword = EphesoftEncryptionUtil.getEncryptedPasswordString(mailPassword);
				batchClassEmailConfiguration.setPassword(mailPassword);
			}
		}
	}

	public void encryptCMISPasswords() {
		if (Hibernate.isInitialized(this.cmisConfigurations) && this.cmisConfigurations != null) {
			for (BatchClassCmisConfiguration batchClassCmisConfiguration : this.cmisConfigurations) {
				String cmisPassword = batchClassCmisConfiguration.getPassword();
				cmisPassword = EphesoftEncryptionUtil.getEncryptedPasswordString(cmisPassword);
				batchClassCmisConfiguration.setPassword(cmisPassword);
			}
		}
	}

	public void encryptPluginPasswords() {
		List<BatchClassPluginConfig> passwordTypePluginConfigs = getBatchClassPluginsByDataType(DataType.PASSWORD);
		if (passwordTypePluginConfigs != null) {
			for (BatchClassPluginConfig batchClassPluginConfig : passwordTypePluginConfigs) {
				String pluginPassword = batchClassPluginConfig.getValue();
				pluginPassword = EphesoftEncryptionUtil.getEncryptedPasswordString(pluginPassword);
				batchClassPluginConfig.setValue(pluginPassword);
			}
		}
	}

	/**
	 * @param advancedBatchClass the advancedBatchClass to set
	 */
	public void setAdvancedBatchClass(Boolean advancedBatchClass) {
		if (advancedBatchClass == null) {
			this.advancedBatchClass = Boolean.FALSE;
		} else {
			this.advancedBatchClass = advancedBatchClass;
		}
	}

	/**
	 * @return the advancedBatchClass
	 */
	public Boolean isAdvancedBatchClass() {
		return advancedBatchClass;
	}

	/**
	 * Gets the algorithm with which the batches associated with the class is encrypted. If null then batch instance should be
	 * un-encrypted.
	 * 
	 * @return {@link PrivateKeyEncryptionAlgorithm} encryptionAlgorithm associated with the batch instance.
	 */
	public PrivateKeyEncryptionAlgorithm getEncryptionAlgorithm() {
		return encryptionAlgorithm;
	}

	/**
	 * Set the encryption algorithm for the batch class.
	 * 
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm} The encryptionAlgorithm to set.
	 */
	public void setEncryptionAlgorithm(PrivateKeyEncryptionAlgorithm encryptionAlgorithm) {
		this.encryptionAlgorithm = encryptionAlgorithm;
	}

	/**
	 * Gets modules in increasing order of their execution for the bath class workflow.
	 * 
	 * @return {@link List}<{@link BatchClassModule}>
	 */
	public List<BatchClassModule> getBatchClassModuleInOrder() {
		List<BatchClassModule> modules = getBatchClassModules();
		Collections.sort(modules);
		return modules;
	}
}
