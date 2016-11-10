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

package com.ephesoft.dcma.gwt.core.server;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.CurrencyCode;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.model.common.DomainEntity.EntityState;
import com.ephesoft.dcma.da.domain.AdvancedKVExtraction;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassCmisConfiguration;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.domain.BatchClassField;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassScannerConfiguration;
import com.ephesoft.dcma.da.domain.Dependency;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.FunctionKey;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.domain.KVPageProcess;
import com.ephesoft.dcma.da.domain.LockStatus;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.PageType;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;
import com.ephesoft.dcma.da.domain.RegexValidation;
import com.ephesoft.dcma.da.domain.ScannerMasterConfiguration;
import com.ephesoft.dcma.da.domain.TableColumnExtractionRule;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableExtractionRule;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.domain.TableRuleInfo;
import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.PluginConfigService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.encryption.util.EphesoftEncryptionUtil;
import com.ephesoft.dcma.gwt.core.client.RandomIdGenerator;
import com.ephesoft.dcma.gwt.core.shared.AdvancedKVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassDynamicPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassFieldDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginConfigDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchClassPluginDTO;
import com.ephesoft.dcma.gwt.core.shared.BatchConstants;
import com.ephesoft.dcma.gwt.core.shared.CmisConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.CoordinatesDTO;
import com.ephesoft.dcma.gwt.core.shared.DependencyDTO;
import com.ephesoft.dcma.gwt.core.shared.DocumentTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.EmailConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.FieldTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.FunctionKeyDTO;
import com.ephesoft.dcma.gwt.core.shared.KVExtractionDTO;
import com.ephesoft.dcma.gwt.core.shared.KVPageProcessDTO;
import com.ephesoft.dcma.gwt.core.shared.LockStatusDTO;
import com.ephesoft.dcma.gwt.core.shared.ModuleDTO;
import com.ephesoft.dcma.gwt.core.shared.OutputDataCarrierDTO;
import com.ephesoft.dcma.gwt.core.shared.PageTypeDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.PluginDetailsDTO;
import com.ephesoft.dcma.gwt.core.shared.RegexDTO;
import com.ephesoft.dcma.gwt.core.shared.RoleDTO;
import com.ephesoft.dcma.gwt.core.shared.RuleInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.SamplePatternDTO;
import com.ephesoft.dcma.gwt.core.shared.ScannerMasterDTO;
import com.ephesoft.dcma.gwt.core.shared.TableColumnExtractionRuleDTO;
import com.ephesoft.dcma.gwt.core.shared.TableColumnInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TableExtractionRuleDTO;
import com.ephesoft.dcma.gwt.core.shared.TableInfoDTO;
import com.ephesoft.dcma.gwt.core.shared.TestTableResultDTO;
import com.ephesoft.dcma.gwt.core.shared.WebScannerConfigurationDTO;
import com.ephesoft.dcma.gwt.core.shared.constants.CoreCommonConstants;
import com.ephesoft.dcma.gwt.core.shared.importTree.Label;
import com.ephesoft.dcma.gwt.core.shared.importTree.Node;
import com.ephesoft.dcma.gwt.core.shared.util.CollectionUtil;
import com.ephesoft.dcma.gwt.core.shared.util.ParseUtil;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This class is a utility for performing different operation on BatchClass like copy, merge, create.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.gwt.core.server
 */
public class BatchClassUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassUtil.class);

	/**
	 * 
	 * @param batchClass the batch class that is to be merged
	 * @param batchClassDTO the batchClassDTO which has to be replicated on the batch class
	 * @param batchClassPluginConfigService
	 * @param batchClassPluginService
	 * @param superAdminGroupsSet
	 * @return a list of document type names that have been deleted.
	 */

	public static List<String> mergeBatchClassFromDTO(BatchClass batchClass, BatchClassDTO batchClassDTO, Set<String> groupNameSet,
			BatchClassPluginConfigService batchClassPluginConfigService, BatchClassPluginService batchClassPluginService,
			PluginConfigService pluginConfigService, List<ScannerMasterConfiguration> scannerMasterConfigs,
			Set<String> superAdminGroupsSet) {
		List<String> docTypeNameList = null;
		int priority = Integer.parseInt(batchClassDTO.getPriority());
		batchClass.setPriority(priority);
		batchClass.setDescription(batchClassDTO.getDescription());
		batchClass.setName(batchClassDTO.getName());
		batchClass.setLastModifiedBy(batchClass.getCurrentUser());
		updateRevisionNumber(batchClass);

		batchClassDTO.setVersion(batchClass.getVersion());
		batchClass.setDeleted(batchClassDTO.isDeleted());

		List<BatchClassModuleDTO> allBatchClassModuleDTOs = new ArrayList<BatchClassModuleDTO>(batchClassDTO.getModules(true));
		List<BatchClassModuleDTO> removedBatchClassModuleDTOs = new ArrayList<BatchClassModuleDTO>();

		for (BatchClassModuleDTO moduleDTO : allBatchClassModuleDTOs) {

			if (moduleDTO.isDeleted() && !moduleDTO.isNew()) {
				batchClass.removeModuleByIdentifier(moduleDTO.getIdentifier());
			} else if (moduleDTO.isNew() && !moduleDTO.isDeleted()) {
				BatchClassModule batchClassModule = new BatchClassModule();
				batchClassModule.setBatchClass(batchClass);
				mergeBatchClassModuleFromDTO(batchClassModule, moduleDTO, batchClassPluginConfigService, batchClassPluginService,
						pluginConfigService);
				moduleDTO.setNew(false);
				batchClass.addModule(batchClassModule);

			} else if (!moduleDTO.isNew() && !moduleDTO.isDeleted()) {
				BatchClassModule batchClassModule = null;
				try {
					long moduleIdentifier = Long.valueOf(moduleDTO.getIdentifier());
					batchClassModule = batchClass.getBatchClassModuleById(moduleIdentifier);
					mergeBatchClassModuleFromDTO(batchClassModule, moduleDTO, batchClassPluginConfigService, batchClassPluginService,
							pluginConfigService);
				} catch (NumberFormatException e) {
					LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
				}

			} else {
				removedBatchClassModuleDTOs.add(moduleDTO);
			}

		}
		allBatchClassModuleDTOs.removeAll(removedBatchClassModuleDTOs);
		for (DocumentTypeDTO documentTypeDTO : batchClassDTO.getDocuments(true)) {
			if (documentTypeDTO.isDeleted()) {
				batchClass.removeDocumentTypeByIdentifier(documentTypeDTO.getIdentifier());
				if (null == docTypeNameList) {
					docTypeNameList = new ArrayList<String>();
					docTypeNameList.add(documentTypeDTO.getName());
				} else {
					docTypeNameList.add(documentTypeDTO.getName());
				}
				removeFuzzyDynamicConfigs(documentTypeDTO.getDescription(), batchClass, CoreCommonConstants.DOCUMENT_TYPE);
			} else if (documentTypeDTO.isNew()) {
				DocumentType documentType = new DocumentType();
				documentTypeDTO.setNew(false);
				mergeBatchClassDocumentFromDTO(documentType, documentTypeDTO);
				addAutoGeneratedPageType(documentType);
				batchClass.addDocumentType(documentType);
				documentType.setBatchClass(batchClass);
			} else {
				DocumentType documentType = batchClass.getDocumentTypeByIdentifier(documentTypeDTO.getIdentifier());
				mergeBatchClassDocumentFromDTO(documentType, documentTypeDTO);
			}
		}
		for (EmailConfigurationDTO emailConfigurationDTO : batchClassDTO.getEmailConfiguration(true)) {
			if (emailConfigurationDTO.isDeleted()) {
				batchClass.removeEmailConfigurationByIdentifier(emailConfigurationDTO.getIdentifier());
			} else if (emailConfigurationDTO.isNew()) {
				BatchClassEmailConfiguration emailConfiguration = new BatchClassEmailConfiguration();
				emailConfigurationDTO.setNew(false);
				mergeBatchClassEmailFromDTO(emailConfiguration, emailConfigurationDTO);
				batchClass.addEmailConfiguration(emailConfiguration);
			} else {
				BatchClassEmailConfiguration batchClassEmailConfiguration = batchClass
						.getEmailConfigurationByIdentifier(emailConfigurationDTO.getIdentifier());
				mergeBatchClassEmailFromDTO(batchClassEmailConfiguration, emailConfigurationDTO);
			}
		}

		for (CmisConfigurationDTO cmisConfigurationDTO : batchClassDTO.getCmisConfiguration(true)) {
			if (cmisConfigurationDTO.isDeleted()) {
				batchClass.removeCmisConfigurationByIdentifier(cmisConfigurationDTO.getIdentifier());
			} else if (cmisConfigurationDTO.isNew()) {
				BatchClassCmisConfiguration cmisConfiguration = new BatchClassCmisConfiguration();
				cmisConfigurationDTO.setNew(false);
				mergeBatchClassCmisFromDTO(cmisConfiguration, cmisConfigurationDTO);
			} else {
				BatchClassCmisConfiguration batchClassCmisConfiguration = batchClass
						.getCmisConfigurationByIdentifier(cmisConfigurationDTO.getIdentifier());
				mergeBatchClassCmisFromDTO(batchClassCmisConfiguration, cmisConfigurationDTO);
			}
		}

		for (WebScannerConfigurationDTO configurationDTO : batchClassDTO.getScannerConfiguration(true)) {
			if (configurationDTO.getParent() == null) {
				// update only the parent configs here
				if (configurationDTO.isDeleted()) {
					batchClass.removeScannerConfigIdentifier(configurationDTO.getIdentifier());
				} else if (configurationDTO.isNew()) {
					BatchClassScannerConfiguration config = new BatchClassScannerConfiguration();
					config.setBatchClass(batchClass);
					config.setParent(null);

					configurationDTO.setNew(false);
					mergeBatchClassScannerFromDTO(config, configurationDTO, batchClass, scannerMasterConfigs);
				} else {
					BatchClassScannerConfiguration config = batchClass.getScannerConfigByIdentifier(configurationDTO.getIdentifier());
					mergeBatchClassScannerFromDTO(config, configurationDTO, batchClass, scannerMasterConfigs);
				}
			}
		}

		for (BatchClassFieldDTO batchClassFieldDTO : batchClassDTO.getBatchClassField(true)) {
			if (batchClassFieldDTO.isDeleted()) {
				batchClass.removeBatchClassFieldByIdentifier(batchClassFieldDTO.getIdentifier());
			} else if (batchClassFieldDTO.isNew()) {
				BatchClassField batchClassField = new BatchClassField();
				batchClassFieldDTO.setNew(false);
				mergeBatchClassFieldFromDTO(batchClassField, batchClassFieldDTO);
				batchClass.addBatchClassField(batchClassField);
			} else {
				BatchClassField batchClassField = batchClass.getBatchClassFieldByIdentifier(batchClassFieldDTO.getIdentifier());
				mergeBatchClassFieldFromDTO(batchClassField, batchClassFieldDTO);
			}
		}

		List<BatchClassGroups> assignedRoles = batchClass.getAssignedGroups();
		if (assignedRoles == null) {
			assignedRoles = new ArrayList<BatchClassGroups>();
		}
		assignedRoles.clear();
		if (null != groupNameSet && !groupNameSet.isEmpty()) {
			List<String> addedRoles = new ArrayList<String>();
			for (RoleDTO roleDTO : batchClassDTO.getAssignedRole()) {
				for (String group : groupNameSet) {
					if (!addedRoles.contains(group) && (superAdminGroupsSet.contains(group) || roleDTO.getName().equals(group))) {
						BatchClassGroups batchClassGroups = new BatchClassGroups();
						batchClassGroups.setGroupName(group);
						addedRoles.add(group);
						batchClassGroups.setBatchClass(batchClass);
						assignedRoles.add(batchClassGroups);
					}
				}
			}
		}

		// This is when super-admin has unmapped all roles from batch class but super-admin role needs to persist
		if (assignedRoles.isEmpty() && batchClassDTO.getAssignedRole().isEmpty()) {
			for (String group : groupNameSet) {
				if (superAdminGroupsSet.contains(group)) {
					BatchClassGroups batchClassGroups = new BatchClassGroups();
					batchClassGroups.setGroupName(group);
					batchClassGroups.setBatchClass(batchClass);
					assignedRoles.add(batchClassGroups);
				}
			}
		}
		batchClass.setAssignedGroups(assignedRoles);

		return docTypeNameList;
	}

	private static void removeFuzzyDynamicConfigs(final String configDescription, final BatchClass batchClass, final String configType) {
		LOGGER.info("Removing Dynamic plugin config for deleted config type: " + configDescription);
		if (batchClass != null) {
			final List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
			if (batchClassModules != null) {
				for (final BatchClassModule batchClassModule : batchClassModules) {
					final List<BatchClassPlugin> batchClassPlugins = batchClassModule
							.getBatchClassPluginsByPluginName(CoreCommonConstants.FUZZY_DB_PLUGIN_NAME);
					if (batchClassPlugins != null) {
						for (final BatchClassPlugin batchClassPlugin : batchClassPlugins) {
							final List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassPlugin
									.getBatchClassDynamicPluginConfigs();
							if (batchClassDynamicPluginConfigs != null) {
								final List<BatchClassDynamicPluginConfig> invalidBatchClassDynamicPluginConfigs = new ArrayList<BatchClassDynamicPluginConfig>();
								for (final BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
									final String pluginConfigName = batchClassDynamicPluginConfig.getName();
									final String pluginConfigDescription = batchClassDynamicPluginConfig.getDescription();
									if (pluginConfigName != null && pluginConfigName.equals(configType)
											&& pluginConfigDescription != null && pluginConfigDescription.equals(configDescription)) {
										LOGGER.info("Invalid Dynamic config: " + pluginConfigDescription
												+ " . So Removing it and its refernces(if any).");
										invalidBatchClassDynamicPluginConfigs.add(batchClassDynamicPluginConfig);
										final List<BatchClassDynamicPluginConfig> pluginConfigChildren = batchClassDynamicPluginConfig
												.getChildren();
										final BatchClassDynamicPluginConfig pluginConfigParent = batchClassDynamicPluginConfig
												.getParent();
										if (pluginConfigChildren != null && !pluginConfigChildren.isEmpty()) {
											// Removing Its Children
											invalidBatchClassDynamicPluginConfigs.addAll(pluginConfigChildren);
										}

										if (pluginConfigParent != null) {
											// Removing it from parents reference
											final List<BatchClassDynamicPluginConfig> pluginConfigParentChildList = pluginConfigParent
													.getChildren();
											if (pluginConfigParentChildList != null && !pluginConfigParentChildList.isEmpty()) {
												pluginConfigParentChildList.remove(batchClassDynamicPluginConfig);
											}
										}
									}

								}
								batchClassDynamicPluginConfigs.removeAll(invalidBatchClassDynamicPluginConfigs);
							}
						}
					}
				}
			}
		}
	}

	private static ScannerMasterConfiguration getScannerMasterConfigByName(List<ScannerMasterConfiguration> scannerMasterConfigs,
			final String name) {
		ScannerMasterConfiguration scannerMasterConfiguration = null;
		for (ScannerMasterConfiguration eachMasterConfiguration : scannerMasterConfigs) {
			if (eachMasterConfiguration.getName().equalsIgnoreCase(name)) {
				scannerMasterConfiguration = eachMasterConfiguration;
				break;
			}
		}
		return scannerMasterConfiguration;
	}

	private static void mergeBatchClassScannerFromDTO(BatchClassScannerConfiguration config,
			WebScannerConfigurationDTO configurationDTO, BatchClass batchClass, List<ScannerMasterConfiguration> scannerMasterConfigs) {

		// update the parent config DTO name value.
		config.setValue(configurationDTO.getValue());
		config.setScannerMasterConfig(getScannerMasterConfigByName(scannerMasterConfigs, configurationDTO.getName()));

		if (config.getChildren() == null || config.getChildren().isEmpty()) {
			for (WebScannerConfigurationDTO childDTO : configurationDTO.getChildren()) {
				BatchClassScannerConfiguration childConfig = new BatchClassScannerConfiguration();
				childConfig.setBatchClass(batchClass);
				childConfig.setParent(config);
				childConfig.setValue(childDTO.getValue());
				childConfig.setScannerMasterConfig(getScannerMasterConfigByName(scannerMasterConfigs, childDTO.getName()));
				config.addChild(childConfig);
			}
		} else {
			// update the existing config of child with values
			for (WebScannerConfigurationDTO childDTO : configurationDTO.getChildren()) {
				for (BatchClassScannerConfiguration childConfig : config.getChildren()) {
					if (childConfig.getScannerMasterConfig().getName().equalsIgnoreCase(childDTO.getName())) {
						childConfig.setValue(childDTO.getValue());
						break;
					}
				}
			}
		}
	}

	public static void mergeBatchClassScannerConfigDTO(BatchClassScannerConfiguration sConfiguration, WebScannerConfigurationDTO sDTO) {

		// Setting the parent

		if (sDTO.getParent() != null) {
			WebScannerConfigurationDTO parentConfig = sDTO.getParent();
			BatchClassScannerConfiguration parent = mergeScannerConfig(sConfiguration, parentConfig);
			parent.setParent(null);
			sConfiguration.setParent(parent);
		}

		// setting the children

		if (sDTO.getChildren() != null) {
			List<BatchClassScannerConfiguration> children = new ArrayList<BatchClassScannerConfiguration>();
			for (WebScannerConfigurationDTO child : sDTO.getChildren()) {
				BatchClassScannerConfiguration childConfig = new BatchClassScannerConfiguration();
				mergeScannerConfig(childConfig, child);
				childConfig.setParent(sConfiguration);
				childConfig.setChildren(null);
				children.add(childConfig);
			}

			sConfiguration.setChildren(children);
		}
	}

	public static void mergeBatchClassEmailFromDTO(BatchClassEmailConfiguration batchClassEmailConfiguration,
			EmailConfigurationDTO emailConfigurationDTO) {
		batchClassEmailConfiguration.setUserName(emailConfigurationDTO.getUserName());
		String emailPassword = emailConfigurationDTO.getPassword();
		String encryptedPasswordString = EphesoftEncryptionUtil.getEncryptedPasswordString(emailPassword);
		batchClassEmailConfiguration.setPassword(encryptedPasswordString);
		batchClassEmailConfiguration.setServerName(emailConfigurationDTO.getServerName());
		batchClassEmailConfiguration.setServerType(emailConfigurationDTO.getServerType());
		batchClassEmailConfiguration.setFolderName(emailConfigurationDTO.getFolderName());
		batchClassEmailConfiguration.setPortNumber(emailConfigurationDTO.getPortNumber());
		batchClassEmailConfiguration.setIsSSL(emailConfigurationDTO.getIsSSL());
	}

	public static void mergeBatchClassCmisFromDTO(BatchClassCmisConfiguration batchClassCmisConfiguration,
			CmisConfigurationDTO cmisConfigurationDTO) {
		batchClassCmisConfiguration.setCmisProperty(cmisConfigurationDTO.getCmisProperty());
		batchClassCmisConfiguration.setFileExtension(cmisConfigurationDTO.getFileExtension());
		batchClassCmisConfiguration.setFolderName(cmisConfigurationDTO.getFolderName());
		String cmisPassword = cmisConfigurationDTO.getPassword();
		String encryptedPasswordString = EphesoftEncryptionUtil.getEncryptedPasswordString(cmisPassword);
		batchClassCmisConfiguration.setPassword(encryptedPasswordString);
		batchClassCmisConfiguration.setRepositoryID(cmisConfigurationDTO.getRepositoryID());
		batchClassCmisConfiguration.setServerURL(cmisConfigurationDTO.getServerURL());
		batchClassCmisConfiguration.setUserName(cmisConfigurationDTO.getUserName());
		batchClassCmisConfiguration.setValue(cmisConfigurationDTO.getValue());
		batchClassCmisConfiguration.setValueToUpdate(cmisConfigurationDTO.getValueToUpdate());
	}

	public static void mergeBatchClassFieldFromDTO(BatchClassField batchClassField, BatchClassFieldDTO batchClassFieldDTO) {
		batchClassField.setDataType(batchClassFieldDTO.getDataType());
		batchClassField.setIdentifier(batchClassFieldDTO.getIdentifier());
		batchClassField.setName(batchClassFieldDTO.getName());

		try {
			int fieldOrderNumber = Integer.parseInt(batchClassFieldDTO.getFieldOrderNumber());
			batchClassField.setFieldOrderNumber(fieldOrderNumber);
		} catch (NumberFormatException e) {
			LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
		}
		batchClassField.setDescription(batchClassFieldDTO.getDescription());
		batchClassField.setValidationPattern(batchClassFieldDTO.getValidationPattern());
		batchClassField.setSampleValue(batchClassFieldDTO.getSampleValue());
		batchClassField.setFieldOptionValueList(batchClassFieldDTO.getFieldOptionValueList());
	}

	public static void addAutoGeneratedPageType(DocumentType documentType) {
		PageType pageType = new PageType();
		pageType.setName(documentType.getName() + BatchConstants.FIRST_PAGE);
		pageType.setDescription(documentType.getName() + "FIRST_PAGE");
		documentType.addPageType(pageType);
		pageType = new PageType();
		pageType.setName(documentType.getName() + BatchConstants.MIDDLE_PAGE);
		pageType.setDescription(documentType.getName() + BatchConstants.MIDDLE_PAGE);
		documentType.addPageType(pageType);
		pageType = new PageType();
		pageType.setName(documentType.getName() + BatchConstants.LAST_PAGE);
		pageType.setDescription(documentType.getName() + BatchConstants.LAST_PAGE);
		documentType.addPageType(pageType);
	}

	public static void mergeBatchClassDocumentFromDTO(DocumentType documentType, DocumentTypeDTO documentTypeDTO) {
		// merge the document fields edited by user.
		documentType.setName(documentTypeDTO.getName());
		documentType.setDescription(documentTypeDTO.getDescription());

		documentType.setFirstPageProjectFileName(documentTypeDTO.getFirstPageProjectFileName());
		documentType.setSecondPageProjectFileName(documentTypeDTO.getSecondPageProjectFileName());
		documentType.setThirdPageProjectFileName(documentTypeDTO.getThirdPageProjectFileName());
		documentType.setFourthPageProjectFileName(documentTypeDTO.getFourthPageProjectFileName());
		documentType.setMinConfidenceThreshold(documentTypeDTO.getMinConfidenceThreshold());
		documentType.setPriority(documentTypeDTO.getPriority());
		documentType.setHidden(documentTypeDTO.isHidden());
		if (documentTypeDTO.getFields(true) != null && documentTypeDTO.getFields(true).size() != 0) {
			for (FieldTypeDTO fieldTypeDTO : documentTypeDTO.getFields(true)) {
				if (fieldTypeDTO.isDeleted()) {
					documentType.removeFieldTypeByIdentifier(fieldTypeDTO.getIdentifier());
					removeFuzzyDynamicConfigs(fieldTypeDTO.getName(), documentType.getBatchClass(), CoreCommonConstants.FIELD_TYPE);
				} else if (fieldTypeDTO.isNew()) {
					FieldType fieldType = new FieldType();
					fieldTypeDTO.setNew(false);
					mergeDocumentTypeFieldFromDTO(fieldType, fieldTypeDTO);
					documentType.addFieldType(fieldType);
				} else {
					FieldType fieldType = documentType.getFieldByIdentifier(fieldTypeDTO.getIdentifier());
					mergeDocumentTypeFieldFromDTO(fieldType, fieldTypeDTO);
				}
			}
		}
		if (documentTypeDTO.getFunctionKeys(true) != null && documentTypeDTO.getFunctionKeys(true).size() != 0) {
			for (FunctionKeyDTO functionKeyDTO : documentTypeDTO.getFunctionKeys(true)) {
				if (functionKeyDTO.isDeleted()) {
					documentType.removeFunctionKeyByIdentifier(functionKeyDTO.getIdentifier());
				} else if (functionKeyDTO.isNew()) {
					FunctionKey functionKey = new FunctionKey();
					functionKeyDTO.setNew(false);
					mergeDocumentTypeFieldFromDTO(functionKey, functionKeyDTO);
					documentType.addFunctionKey(functionKey);
				} else {
					FunctionKey functionKey = documentType.getFunctionKeyByIdentifier(functionKeyDTO.getIdentifier());
					mergeDocumentTypeFieldFromDTO(functionKey, functionKeyDTO);
				}
			}
		}

		if (documentTypeDTO.getTableInfos(true) != null && documentTypeDTO.getTableInfos(true).size() != 0) {
			for (TableInfoDTO tableInfoDTO : documentTypeDTO.getTableInfos(true)) {
				if (tableInfoDTO.isDeleted()) {
					documentType.removeTableInfoByIdentifier(tableInfoDTO.getIdentifier());
				} else if (tableInfoDTO.isNew()) {
					TableInfo tableInfo = new TableInfo();
					tableInfoDTO.setNew(false);

					mergeDocumentTypeFieldFromDTO(tableInfo, tableInfoDTO);
					documentType.addTableInfo(tableInfo);
				} else {
					TableInfo tableInfo = documentType.getTableInfoByIdentifier(tableInfoDTO.getIdentifier());
					mergeDocumentTypeFieldFromDTO(tableInfo, tableInfoDTO);
				}
			}
		}

		if (documentTypeDTO.getPages(true) != null && documentTypeDTO.getPages(true).size() != 0) {
			for (PageTypeDTO pageTypeDTO : documentTypeDTO.getPages(true)) {
				if (pageTypeDTO.isDeleted()) {
					documentType.removePageTypeByIdentifier(pageTypeDTO.getIdentifier());
				} else {
					PageType pageType = documentType.getPageByIdentifier(pageTypeDTO.getIdentifier());
					mergeDocumentTypePageFromDTO(pageType, pageTypeDTO);
				}
			}
		}
	}

	private static void mergeDocumentTypeFieldFromDTO(FunctionKey functionKey, FunctionKeyDTO functionKeyDTO) {
		functionKey.setIdentifier(functionKeyDTO.getIdentifier());
		functionKey.setMethodName(functionKeyDTO.getMethodName());
		functionKey.setUiLabel(functionKeyDTO.getMethodDescription());
		functionKey.setShortcutKeyname(functionKeyDTO.getShortcutKeyName());
	}

	public static void mergeDocumentTypeFieldFromDTO(TableInfo tableInfo, TableInfoDTO tableInfoDTO) {
		// merge the field types edited by user.
		if (tableInfoDTO.getRuleOperator() != null) {
			tableInfo.setRuleOperator(tableInfoDTO.getRuleOperator());
		}
		tableInfo.setRemoveInvalidRows(tableInfoDTO.isRemoveInvalidRows());
		tableInfo.setName(tableInfoDTO.getName());
		tableInfo.setDisplayImage(tableInfoDTO.getDisplayImage());
		tableInfo.setCurrencyCode(CurrencyCode.getCurrencyCodeForRepresentationValue(tableInfoDTO.getCurrencyCode()));

		if (tableInfoDTO.getNumberOfRows() != null && !tableInfoDTO.getNumberOfRows().isEmpty()) {
			tableInfo.setNumberOfRows(Integer.parseInt(tableInfoDTO.getNumberOfRows()));
		} else {
			tableInfo.setNumberOfRows(CoreCommonConstants.DEFAULT_NO_OF_ROWS);
		}

		if (tableInfoDTO.getTableColumnInfoList(true) != null) {
			for (TableColumnInfoDTO tableColumnInfoDTO : tableInfoDTO.getTableColumnInfoList(true)) {
				if (tableColumnInfoDTO.isDeleted()) {

					try {
						long tableColumnInfoId = Long.parseLong(tableColumnInfoDTO.getIdentifier());
						tableInfo.removeTableColumnsInfoById(tableColumnInfoId);
					} catch (NumberFormatException e) {
						LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
					}
				} else if (tableColumnInfoDTO.isNew()) {
					TableColumnsInfo tabColumnsInfo = new TableColumnsInfo();
					tableColumnInfoDTO.setNew(false);
					mergeTableColumnInfoFromDTO(tabColumnsInfo, tableColumnInfoDTO);
					tableInfo.addTableColumnInfo(tabColumnsInfo);
				} else {
					TableColumnsInfo tabColumnsInfo = tableInfo.getTableColumnInfobyIdentifier(tableColumnInfoDTO.getIdentifier());
					mergeTableColumnInfoFromDTO(tabColumnsInfo, tableColumnInfoDTO);
				}
			}
		}
		if (tableInfoDTO.getRuleInfoDTOs(true) != null) {
			for (RuleInfoDTO ruleInfoDTO : tableInfoDTO.getRuleInfoDTOs(true)) {
				if (ruleInfoDTO.isDeleted()) {

					try {
						long ruleInfoId = Long.parseLong(ruleInfoDTO.getIdentifier());
						tableInfo.removeTableRulesInfoById(ruleInfoId);
					} catch (NumberFormatException e) {
						LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
					}
				} else if (ruleInfoDTO.isNew()) {
					TableRuleInfo tabRuleInfo = new TableRuleInfo();
					ruleInfoDTO.setNew(false);
					mergeRuleInfoFromDTO(tabRuleInfo, ruleInfoDTO);
					tableInfo.addTableRuleInfo(tabRuleInfo);
				} else {
					TableRuleInfo tabRuleInfo = tableInfo.getTableRuleInfobyIdentifier(ruleInfoDTO.getIdentifier());
					mergeRuleInfoFromDTO(tabRuleInfo, ruleInfoDTO);
				}
			}
		}

		// Merging table extraction rules and table column extraction rules from table info DTO to table info object.
		final List<TableExtractionRuleDTO> tableExtractionRuleDTOs = tableInfoDTO.getTableExtractionRuleDTOs(true);
		if (!CollectionUtil.isEmpty(tableExtractionRuleDTOs)) {
			for (TableExtractionRuleDTO tableExtractionRuleDTO : tableExtractionRuleDTOs) {
				if (null != tableExtractionRuleDTO) {
					if (tableExtractionRuleDTO.isDeleted()) {
						try {
							long tableExtractionRuleId = Long.parseLong(tableExtractionRuleDTO.getIdentifier());
							tableInfo.removeTableExtractionRuleById(tableExtractionRuleId);
						} catch (final NumberFormatException numberFormatException) {
							LOGGER.error(EphesoftStringUtil.concatenate(BatchConstants.ERROR_CONVERT_NUMBER,
									numberFormatException.getMessage()));
						}
					} else if (tableExtractionRuleDTO.isNew()) {
						final TableExtractionRule tableExtractionRule = new TableExtractionRule();
						tableExtractionRuleDTO.setNew(false);
						tableExtractionRule.setTableInfo(tableInfo);
						mergeTableExtractionRuleFromDTO(tableExtractionRule, tableExtractionRuleDTO);
						tableInfo.addTableExtractionRule(tableExtractionRule);
					} else {
						final TableExtractionRule tableExtractionRule = tableInfo
								.getTableExtractionRuleByIdentifier(tableExtractionRuleDTO.getIdentifier());
						mergeTableExtractionRuleFromDTO(tableExtractionRule, tableExtractionRuleDTO);
					}
				}
			}
		}

	}

	public static void mergeDocumentTypeFieldFromDTO(FieldType fieldType, FieldTypeDTO fieldTypeDTO) {
		// merge the field types edited by user.
		fieldType.setName(fieldTypeDTO.getName());
		fieldType.setDescription(fieldTypeDTO.getDescription());
		fieldType.setDataType(fieldTypeDTO.getDataType());
		fieldType.setPattern(fieldTypeDTO.getPattern());
		fieldType.setRegexListingSeparator(fieldTypeDTO.getRegexListingSeparator());
		fieldType.setSampleValue(fieldTypeDTO.getSampleValue());
		fieldType.setFieldOrderNumber(Integer.parseInt(fieldTypeDTO.getFieldOrderNumber()));
		fieldType.setBarcodeType(fieldTypeDTO.getBarcodeType());
		fieldType.setHidden(fieldTypeDTO.isHidden());
		// fieldType.setMultiLine(fieldTypeDTO.isMultiLine());
		fieldType.setReadOnly(fieldTypeDTO.getIsReadOnly());
		fieldType.setOcrConfidenceThreshold(fieldTypeDTO.getOcrConfidenceThreshold());
		fieldType.setFieldValueChangeScriptEnabled(fieldTypeDTO.isFieldValueChangeScriptEnabled());
		fieldType.setFieldOptionValueList(fieldTypeDTO.getFieldOptionValueList());
		if (fieldTypeDTO.getKvExtractionList(true) != null) {
			for (KVExtractionDTO kvExtractionDTO : fieldTypeDTO.getKvExtractionList(true)) {
				if (kvExtractionDTO.isDeleted()) {

					try {
						Long kvExtractionId = Long.parseLong(kvExtractionDTO.getIdentifier());
						fieldType.removeKvExtractionById(kvExtractionId);
					} catch (NumberFormatException e) {
						LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
					}
				} else if (kvExtractionDTO.isNew()) {
					KVExtraction kvExtraction = new KVExtraction();
					kvExtractionDTO.setNew(false);
					mergeFieldTypeKVExtractionFromDTO(kvExtraction, kvExtractionDTO);
					fieldType.addKVExtraction(kvExtraction);
				} else {
					KVExtraction kvExtraction = fieldType.getKVExtractionbyIdentifier(kvExtractionDTO.getIdentifier());
					mergeFieldTypeKVExtractionFromDTO(kvExtraction, kvExtractionDTO);
				}
			}
		}
		if (fieldTypeDTO.getRegexList(true) != null) {
			for (RegexDTO regexDTO : fieldTypeDTO.getRegexList(true)) {
				if (regexDTO.isDeleted()) {

					try {
						Long regexValidationId = Long.parseLong(regexDTO.getIdentifier());
						fieldType.removeRegexValidationById(regexValidationId);
					} catch (NumberFormatException e) {
						LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
					}
				} else if (regexDTO.isNew()) {
					RegexValidation regexValidation = new RegexValidation();
					regexDTO.setNew(false);
					regexValidation.setPattern(regexDTO.getPattern());
					fieldType.addRegexValidation(regexValidation);
				} else {
					RegexValidation regexValidation = fieldType.getRegexValidationbyIdentifier(regexDTO.getIdentifier());
					regexValidation.setPattern(regexDTO.getPattern());
				}
			}
		}
	}

	public static void mergeFieldTypeKVExtractionFromDTO(KVExtraction kvExtraction, KVExtractionDTO kvExtractionDTO) {
		kvExtraction.setLocationType(kvExtractionDTO.getLocationType());
		kvExtraction.setKeyPattern(kvExtractionDTO.getKeyPattern());
		kvExtraction.setValuePattern(kvExtractionDTO.getValuePattern());
		kvExtraction.setNoOfWords(kvExtractionDTO.getNoOfWords());
		kvExtraction.setLength(kvExtractionDTO.getLength());
		kvExtraction.setWidth(kvExtractionDTO.getWidth());
		kvExtraction.setXoffset(kvExtractionDTO.getXoffset());
		kvExtraction.setYoffset(kvExtractionDTO.getYoffset());
		kvExtraction.setFetchValue(kvExtractionDTO.getFetchValue());
		kvExtraction.setPageValue(kvExtractionDTO.getKvPageValue());
		kvExtraction.setUseExistingKey(kvExtractionDTO.isUseExistingKey());
		kvExtraction.setOrderNo(Integer.parseInt(kvExtractionDTO.getOrderNumber()));

		// Sets extract zone of KVExtraction from its DTO.
		kvExtraction.setExtractZone(kvExtractionDTO.getExtractZone());
		try {
			int orderNumber = Integer.parseInt(kvExtractionDTO.getOrderNumber());
			kvExtraction.setOrderNo(orderNumber);
		} catch (NumberFormatException numberFormatException) {
			LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + numberFormatException.getMessage());
		}

		// Adding weight value of KV extraction
		Float weight = kvExtractionDTO.getWeight();
		if (null != weight) {
			kvExtraction.setWeight(weight);
		} else {
			kvExtraction.setWeight(CoreCommonConstants.DEFAULT_WEIGHT);
		}

		// Setting the fuzzy match threshold value for key pattern.
		kvExtraction.setKeyFuzziness(kvExtractionDTO.getKeyFuzziness());

		AdvancedKVExtractionDTO advancedKVExtractionDTO = kvExtractionDTO.getAdvancedKVExtractionDTO();
		if (null != advancedKVExtractionDTO // && advancedKVExtractionDTO.getDisplayImageName() != null
		/* && !advancedKVExtractionDTO.getDisplayImageName().isEmpty() */) {
			AdvancedKVExtraction advancedKVExtraction = kvExtraction.getAdvancedKVExtraction();
			if (null == advancedKVExtraction) {
				advancedKVExtraction = new AdvancedKVExtraction();
				kvExtraction.setAdvancedKVExtraction(advancedKVExtraction);
			}
			mergeAdvancedKVExtractionFromDTO(advancedKVExtraction, advancedKVExtractionDTO);
		}
	}

	public static void mergeAdvancedKVExtractionFromDTO(final AdvancedKVExtraction advancedKVExtraction,
			final AdvancedKVExtractionDTO advancedKVExtractionDTO) {
		// advancedKVExtraction.setImageName(advancedKVExtractionDTO.getImageName());
		// advancedKVExtraction.setDisplayImageName(advancedKVExtractionDTO.getDisplayImageName());
		advancedKVExtraction.setKeyX0Coord(advancedKVExtractionDTO.getKeyX0Coord());
		advancedKVExtraction.setKeyY0Coord(advancedKVExtractionDTO.getKeyY0Coord());
		advancedKVExtraction.setKeyX1Coord(advancedKVExtractionDTO.getKeyX1Coord());
		advancedKVExtraction.setKeyY1Coord(advancedKVExtractionDTO.getKeyY1Coord());
		advancedKVExtraction.setValueX0Coord(advancedKVExtractionDTO.getValueX0Coord());
		advancedKVExtraction.setValueY0Coord(advancedKVExtractionDTO.getValueY0Coord());
		advancedKVExtraction.setValueX1Coord(advancedKVExtractionDTO.getValueX1Coord());
		advancedKVExtraction.setValueY1Coord(advancedKVExtractionDTO.getValueY1Coord());
	}

	public static void mergeRuleInfoFromDTO(TableRuleInfo tabRuleInfo, RuleInfoDTO ruleInfoDTO) {
		if (ruleInfoDTO.getRule() != null && !ruleInfoDTO.getRule().isEmpty()) {
			tabRuleInfo.setRule(ruleInfoDTO.getRule());
		}
		tabRuleInfo.setDescription(ruleInfoDTO.getDescription());
	}

	public static void mergeTableColumnInfoFromDTO(TableColumnsInfo tabColumnsInfo, TableColumnInfoDTO tableColumnInfoDTO) {

		// Column extarction information mopved to table column extraction rule and column ddescription is added to table column
		// information.
		tabColumnsInfo.setColumnName(tableColumnInfoDTO.getColumnName());
		tabColumnsInfo.setValidationPattern(tableColumnInfoDTO.getValidationPattern());
		tabColumnsInfo.setColumnDescription(tableColumnInfoDTO.getColumnDescription());
		tabColumnsInfo.setAlternateValues(tableColumnInfoDTO.getAlternateValues());
	}

	public static void mergeDocumentTypePageFromDTO(PageType pageType, PageTypeDTO pageTypeDTO) {
		// will be used in edit case.
	}

	public static void mergeBatchClassModuleFromDTO(BatchClassModule batchClassModule, BatchClassModuleDTO batchClassModuleDTO,
			BatchClassPluginConfigService batchClassPluginConfigService, BatchClassPluginService batchClassPluginService,
			PluginConfigService pluginConfigService) {

		batchClassModule.setRemoteURL(batchClassModuleDTO.getRemoteURL());
		batchClassModule.setRemoteBatchClassIdentifier(batchClassModuleDTO.getRemoteBatchClassIdentifier());
		batchClassModule.setOrderNumber(batchClassModuleDTO.getOrderNumber());

		// New BCMDTO
		if (batchClassModuleDTO.isNew()) {
			batchClassModule.setId(0);
			batchClassModule.setWorkflowName(batchClassModuleDTO.getWorkflowName());
			Module module = new Module();

			try {
				Long moduleID = Long.valueOf(batchClassModuleDTO.getModule().getIdentifier());
				module.setId(moduleID);
			} catch (NumberFormatException e) {
				LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
			}
			module.setName(batchClassModuleDTO.getModule().getName());
			module.setDescription(batchClassModuleDTO.getModule().getDescription());
			module.setVersion(batchClassModuleDTO.getModule().getVersion());
			batchClassModule.setModule(module);
			// if (!batchClassModuleDTO.getWorkflowName().contains(batchClassModuleDTO.getBatchClass().getIdentifier())) {
			// batchClassModule.setWorkflowName(batchClassModuleDTO.getWorkflowName() + UNDERSCORE
			// + batchClassModuleDTO.getBatchClass().getIdentifier());
			// }
		} else {

			try {
				long batchClassModuleId = Long.valueOf(batchClassModuleDTO.getIdentifier());
				batchClassModule.setId(batchClassModuleId);
			} catch (NumberFormatException e) {
				LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
			}
			batchClassModule.setWorkflowName(batchClassModuleDTO.getWorkflowName());
		}

		List<BatchClassPluginDTO> allPluginDTOs = new ArrayList<BatchClassPluginDTO>(batchClassModuleDTO.getBatchClassPlugins(true));
		List<BatchClassPluginDTO> removedPluginDTOs = new ArrayList<BatchClassPluginDTO>();

		for (BatchClassPluginDTO pluginDTO : allPluginDTOs) {

			if (batchClassModuleDTO.isNew()) {
				pluginDTO.setNew(true);
			}

			if (pluginDTO.isDeleted() && !pluginDTO.isNew()) {
				try {
					long batchClassPluginId = Long.valueOf(pluginDTO.getIdentifier());
					batchClassModule.removeBatchClassPluginById(batchClassPluginId);
				} catch (NumberFormatException e) {
					LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
				}
			} else if (pluginDTO.isNew() && !pluginDTO.isDeleted()) {
				BatchClassPlugin batchClassPlugin = new BatchClassPlugin();
				batchClassPlugin.setBatchClassModule(batchClassModule);
				batchClassPlugin.setId(0);
				batchClassPlugin.setOrderNumber(pluginDTO.getOrderNumber());

				Plugin plugin = new Plugin();

				try {

					Long pluginId = Long.valueOf(pluginDTO.getPlugin().getIdentifier());
					plugin.setId(pluginId);
				} catch (NumberFormatException e) {
					LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
				}
				plugin.setPluginName(pluginDTO.getPlugin().getPluginName());

				batchClassPlugin.setPlugin(plugin);

				mergeBatchClassPluginFromDTO(batchClassPlugin, pluginDTO, batchClassPluginConfigService, batchClassPluginService,
						pluginConfigService);
				if (batchClassModule.getBatchClassPlugins() == null) {
					batchClassModule.setBatchClassPlugins(new ArrayList<BatchClassPlugin>(0));
					batchClassModule.getBatchClassPlugins().add(batchClassPlugin);
				} else {
					batchClassModule.getBatchClassPlugins().add(batchClassPlugin);
				}
			} else if (!pluginDTO.isDeleted() && !pluginDTO.isNew()) {

				try {
					long batchClassPluginId = Long.valueOf(pluginDTO.getIdentifier());

					BatchClassPlugin batchClassPlugin = batchClassModule.getBatchClassPluginById(batchClassPluginId);
					mergeBatchClassPluginFromDTO(batchClassPlugin, pluginDTO, batchClassPluginConfigService, batchClassPluginService,
							pluginConfigService);
				} catch (NumberFormatException e) {
					LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
				}
			} else {
				removedPluginDTOs.add(pluginDTO);
			}

		}

		allPluginDTOs.removeAll(removedPluginDTOs);

	}

	public static void mergeBatchClassPluginFromDTO(BatchClassPlugin batchClassPlugin, BatchClassPluginDTO pluginDTO,
			BatchClassPluginConfigService batchClassPluginConfigService, BatchClassPluginService batchClassPluginService,
			PluginConfigService pluginConfigService) {
		Map<String, BatchClassPluginConfig> batchClassPluginConfigMap = new HashMap<String, BatchClassPluginConfig>();
		Map<String, BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigMap = new HashMap<String, BatchClassDynamicPluginConfig>();

		Collection<BatchClassPluginConfigDTO> pluginConfigDTOs = pluginDTO.getBatchClassPluginConfigs();

		Collection<BatchClassDynamicPluginConfigDTO> dynamicPluginConfigDTOs = pluginDTO.getBatchClassDynamicPluginConfigs();

		batchClassPlugin.setOrderNumber(pluginDTO.getOrderNumber());
		// New module is added
		if (batchClassPlugin.getBatchClassPluginConfigs() == null) {
			batchClassPlugin.setBatchClassPluginConfigs(new ArrayList<BatchClassPluginConfig>(0));
			batchClassPlugin.setBatchClassDynamicPluginConfigs(new ArrayList<BatchClassDynamicPluginConfig>());

		}
		if (batchClassPlugin.getBatchClassDynamicPluginConfigs() == null) {
			batchClassPlugin.setBatchClassDynamicPluginConfigs(new ArrayList<BatchClassDynamicPluginConfig>());
		}

		if (pluginDTO.isNew()) {
			setBatchClassModuleDTOPluginConfigList(batchClassPlugin, pluginDTO, batchClassPluginConfigService,
					batchClassPluginService, pluginConfigService);
			for (BatchClassPluginConfigDTO batchClassPluginConfigDTO : pluginDTO.getBatchClassPluginConfigs()) {
				BatchClassPluginConfig batchClassPluginConfig = new BatchClassPluginConfig();
				PluginConfig pluginConfig = new PluginConfig();
				pluginConfig = mergePluginConfiguration(batchClassPluginConfigDTO);
				batchClassPluginConfig.setPluginConfig(pluginConfig);
				refreshBatchClassPluginConfigFromDTO(batchClassPluginConfig, batchClassPluginConfigDTO);
				batchClassPlugin.addBatchClassPluginConfig(batchClassPluginConfig);
			}

		}
		if (!pluginDTO.isNew()) {
			for (BatchClassPluginConfigDTO batchClassPluginConfigDTO : pluginConfigDTOs) {

				BatchClassPluginConfig batchClassPluginConfig = null;

				try {
					long batchClassPluginConfigId = Long.valueOf(batchClassPluginConfigDTO.getIdentifier());
					batchClassPluginConfig = batchClassPlugin.getBatchClassPluginConfigById(batchClassPluginConfigId);
				} catch (NumberFormatException e) {
					LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
				}
				if (batchClassPluginConfig == null) {
					batchClassPluginConfig = new BatchClassPluginConfig();
					PluginConfig pluginConfig = new PluginConfig();
					try {
						long pluginConfigId = Long.valueOf(batchClassPluginConfigDTO.getPluginConfig().getIdentifier());
						pluginConfig.setId(pluginConfigId);
					} catch (NumberFormatException e) {
						LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
					}
					pluginConfig.setMultiValue(batchClassPluginConfigDTO.isMultivalue());
					pluginConfig.setMandatory(batchClassPluginConfigDTO.isMandatory());
					// pluginConfig.setDataType(batchClassPluginConfigDTO.getDataType());

					batchClassPluginConfig.setBatchClassPlugin(batchClassPlugin);
					batchClassPluginConfig.setPluginConfig(pluginConfig);
				}
				refreshBatchClassPluginConfigFromDTO(batchClassPluginConfig, batchClassPluginConfigDTO);
				batchClassPlugin.addBatchClassPluginConfig(batchClassPluginConfig);

				if (batchClassPluginConfigDTO.getKvPageProcessDTOs(true) != null) {
					for (KVPageProcessDTO kvPageProcessDTO : batchClassPluginConfigDTO.getKvPageProcessDTOs(true)) {
						if (kvPageProcessDTO.getIsDeleted()) {
							batchClassPluginConfig.removeKvPageProcessById(kvPageProcessDTO.getIdentifier());
						} else if (kvPageProcessDTO.getIsNew()) {
							KVPageProcess kvPageProcess = new KVPageProcess();
							kvPageProcessDTO.setIsNew(false);
							kvPageProcessDTO.setIdentifier(0L);
							kvPageProcess.setBatchClassPluginConfig(batchClassPluginConfig);
							mergeKvPageProcessFromDTO(kvPageProcess, kvPageProcessDTO);
							batchClassPluginConfig.addKVPageProcess(kvPageProcess);
						} else {
							KVPageProcess kvPageProcess = batchClassPluginConfig.getKVPageProcessbyIdentifier(String
									.valueOf(kvPageProcessDTO.getIdentifier()));
							mergeKvPageProcessFromDTO(kvPageProcess, kvPageProcessDTO);
						}
					}
				}

				batchClassPluginConfigMap.put(batchClassPluginConfigDTO.getIdentifier(), batchClassPluginConfig);
			}

			for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : dynamicPluginConfigDTOs) {

				BatchClassDynamicPluginConfig batchClassDynamicPluginConfig = null;
				try {
					long batchClassDynamicPluginConfigId = Long.valueOf(batchClassDynamicPluginConfigDTO.getIdentifier());
					batchClassDynamicPluginConfig = batchClassPlugin
							.getBatchClassDynamicPluginConfigById(batchClassDynamicPluginConfigId);
				} catch (NumberFormatException e) {
					LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
				}
				if (batchClassDynamicPluginConfig == null) {
					batchClassDynamicPluginConfig = new BatchClassDynamicPluginConfig();
					batchClassDynamicPluginConfig.setId(0);
				}
				refreshBatchClassDynamicPluginConfigFromDTO(batchClassDynamicPluginConfig, batchClassDynamicPluginConfigDTO);
				batchClassPlugin.addBatchClassDynamicPluginConfig(batchClassDynamicPluginConfig);
				batchClassDynamicPluginConfigMap.put(batchClassDynamicPluginConfigDTO.getIdentifier(), batchClassDynamicPluginConfig);

			}

			if (batchClassPlugin.getBatchClassDynamicPluginConfigs() == null) {
				batchClassPlugin.setBatchClassDynamicPluginConfigs(new ArrayList<BatchClassDynamicPluginConfig>(0));
			}
			for (BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO : dynamicPluginConfigDTOs) {
				BatchClassDynamicPluginConfig config = batchClassDynamicPluginConfigMap.get(batchClassDynamicPluginConfigDTO
						.getIdentifier());
				if (batchClassDynamicPluginConfigDTO.getChildren() != null
						&& !batchClassDynamicPluginConfigDTO.getChildren().isEmpty()) {
					Collection<BatchClassDynamicPluginConfigDTO> children = batchClassDynamicPluginConfigDTO.getChildren();
					for (BatchClassDynamicPluginConfigDTO child : children) {
						BatchClassDynamicPluginConfig childConfig = batchClassDynamicPluginConfigMap.get(child.getIdentifier());
						if (childConfig != null) {
							config.addChild(childConfig);
						}
					}
				}
			}

			for (BatchClassDynamicPluginConfigDTO parent : dynamicPluginConfigDTOs) {

				if (parent != null && parent.getChildren() != null) {
					BatchClassDynamicPluginConfig dynamicParentConfig = batchClassDynamicPluginConfigMap.get(parent.getIdentifier());
					mergeDeletedDynamicConfigs(dynamicParentConfig, pluginDTO);
				}
			}
			mergeDeletedDynamicConfigs(batchClassPlugin, pluginDTO);
		} else {
			batchClassPlugin.setBatchClassDynamicPluginConfigs(new ArrayList<BatchClassDynamicPluginConfig>(0));

		}
	}

	private static void mergeDeletedDynamicConfigs(BatchClassPlugin batchClassPlugin, BatchClassPluginDTO pluginDTO) {
		if (batchClassPlugin.getPlugin().getPluginName().equalsIgnoreCase(CoreCommonConstants.FUZZY_DB_PLUGIN_NAME)) {
			Collection<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassPlugin
					.getBatchClassDynamicPluginConfigs();

			if (batchClassDynamicPluginConfigs == null) {
				return;
			}
			List<BatchClassDynamicPluginConfig> removedConfigs = new ArrayList<BatchClassDynamicPluginConfig>();
			for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
				if (batchClassDynamicPluginConfig.getId() == 0) {
					continue;
				}
				if (pluginDTO.getBatchClassDynamicPluginConfigDTOById(String.valueOf(batchClassDynamicPluginConfig.getId())) == null) {
					removedConfigs.add(batchClassDynamicPluginConfig);
				}
			}

			for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : removedConfigs) {
				batchClassPlugin.removeBatchClassDynamicPluginConfig(batchClassDynamicPluginConfig);
			}
		}
	}

	private static void mergeDeletedDynamicConfigs(BatchClassDynamicPluginConfig config, BatchClassPluginDTO pluginDTO) {
		if (config != null) {
			Collection<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = config.getChildren();

			if (batchClassDynamicPluginConfigs == null) {
				return;
			}

			List<BatchClassDynamicPluginConfig> removedConfigs = new ArrayList<BatchClassDynamicPluginConfig>();
			for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
				if (batchClassDynamicPluginConfig.getId() == 0) {
					continue;
				}
				if (pluginDTO.getBatchClassDynamicPluginConfigDTOById(String.valueOf(batchClassDynamicPluginConfig.getId())) == null) {
					removedConfigs.add(batchClassDynamicPluginConfig);
				}
			}

			for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : removedConfigs) {
				config.removeChild(batchClassDynamicPluginConfig);
			}
		}
	}

	public static void refreshBatchClassPluginConfigFromDTO(BatchClassPluginConfig batchClassPluginConfig,
			BatchClassPluginConfigDTO batchClassPluginConfigDTO) {
		batchClassPluginConfig.setDescription(batchClassPluginConfigDTO.getDescription());
		batchClassPluginConfig.setEntityState(EntityState.NEW);
		batchClassPluginConfig.setName(batchClassPluginConfigDTO.getName());
		batchClassPluginConfig.setQualifier(batchClassPluginConfigDTO.getQualifier());
		DataType configDataType = batchClassPluginConfigDTO.getDataType();
		String configValue = batchClassPluginConfigDTO.getValue();
		if (configDataType == DataType.PASSWORD) {
			configValue = EphesoftEncryptionUtil.getEncryptedPasswordString(configValue);
		}
		batchClassPluginConfig.setValue(configValue);
	}

	public static void refreshBatchClassDynamicPluginConfigFromDTO(BatchClassDynamicPluginConfig batchClassDynamicPluginConfig,
			BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO) {
		batchClassDynamicPluginConfig.setDescription(batchClassDynamicPluginConfigDTO.getDescription());
		batchClassDynamicPluginConfig.setEntityState(EntityState.NEW);
		batchClassDynamicPluginConfig.setName(batchClassDynamicPluginConfigDTO.getName());
		batchClassDynamicPluginConfig.setValue(batchClassDynamicPluginConfigDTO.getValue());
	}

	public static BatchClassDTO createBatchClassDTO(List<ScannerMasterConfiguration> masterScannerConfigs, BatchClass batchClass,
			PluginService pluginService) {
		BatchClassDTO batchClassDTO = new BatchClassDTO();
		batchClassDTO.setIdentifier(batchClass.getIdentifier());
		batchClassDTO.setDescription(batchClass.getDescription());
		batchClassDTO.setName(batchClass.getName());
		batchClassDTO.setPriority(BatchConstants.EMPTY_STRING + batchClass.getPriority());
		batchClassDTO.setUncFolder(batchClass.getUncFolder());
		batchClassDTO.setVersion(batchClass.getVersion());
		batchClassDTO.setDeleted(batchClass.isDeleted());
		batchClassDTO.setCurrentUser(batchClass.getCurrentUser());
		List<DocumentType> documentTypes = batchClass.getDocumentTypes();

		for (DocumentType documentType : documentTypes) {
			DocumentTypeDTO documentTypeDTO = createDocumentTypeDTO(batchClassDTO, documentType);
			batchClassDTO.addDocumentType(documentTypeDTO);
		}

		for (ScannerMasterConfiguration masterScannerConfig : masterScannerConfigs) {
			ScannerMasterDTO dto = new ScannerMasterDTO();
			dto.setBatchClass(batchClassDTO);
			dto.setName(masterScannerConfig.getName());
			dto.setType(masterScannerConfig.getType());
			dto.setMultiValue(masterScannerConfig.isMultiValue());
			dto.setSampleValue(masterScannerConfig.getSampleValue());
			dto.setDescription(masterScannerConfig.getDescription());
			dto.setMandatory(masterScannerConfig.isMandatory());
			batchClassDTO.addScannerMaster(dto);
		}

		for (BatchClassModule batchClassModule : batchClass.getBatchClassModules()) {
			BatchClassModuleDTO batchClassModuleDTO = createBatchClassModuleDTO(batchClassDTO, batchClassModule, pluginService);
			batchClassDTO.addModule(batchClassModuleDTO);
		}

		for (BatchClassEmailConfiguration batchClassEmailConfiguration : batchClass.getEmailConfigurations()) {
			EmailConfigurationDTO emailConfigurationDTO = createEmailDTO(batchClassDTO, batchClassEmailConfiguration);
			batchClassDTO.addEmailConfiguration(emailConfigurationDTO);
		}

		for (BatchClassCmisConfiguration batchClassCmisConfiguration : batchClass.getCmisConfigurations()) {
			CmisConfigurationDTO cmisConfigurationDTO = createCmisDTO(batchClassDTO, batchClassCmisConfiguration);
			batchClassDTO.addCmisConfiguration(cmisConfigurationDTO);
		}

		for (BatchClassScannerConfiguration configuration : batchClass.getBatchClassScannerConfiguration()) {
			if (configuration.getParent() == null) {
				// set only the parent node
				WebScannerConfigurationDTO configurationDTO = createScannerDTO(batchClassDTO, configuration);
				batchClassDTO.addScannerConfiguration(configurationDTO);
			}
		}

		for (BatchClassField batchClassField : batchClass.getBatchClassField()) {
			BatchClassFieldDTO batchClassFieldDTO = createBatchClassFieldDTO(batchClassDTO, batchClassField);
			batchClassDTO.addBatchClassField(batchClassFieldDTO);
		}

		for (BatchClassGroups role : batchClass.getAssignedGroups()) {
			RoleDTO roleDTO = createRoleDTO(role);
			batchClassDTO.addAssignedRole(roleDTO);
		}
		batchClassDTO.setDirty(false);
		return batchClassDTO;
	}

	public static EmailConfigurationDTO createEmailDTO(BatchClassDTO batchClassDTO,
			BatchClassEmailConfiguration batchClassEmailConfiguration) {
		EmailConfigurationDTO emailConfigurationDTO = new EmailConfigurationDTO();
		emailConfigurationDTO.setBatchClass(batchClassDTO);
		emailConfigurationDTO.setUserName(batchClassEmailConfiguration.getUserName());
		emailConfigurationDTO.setPassword(batchClassEmailConfiguration.getPassword());
		emailConfigurationDTO.setServerName(batchClassEmailConfiguration.getServerName());
		emailConfigurationDTO.setServerType(batchClassEmailConfiguration.getServerType());
		emailConfigurationDTO.setFolderName(batchClassEmailConfiguration.getFolderName());
		emailConfigurationDTO.setIdentifier(String.valueOf(batchClassEmailConfiguration.getId()));
		emailConfigurationDTO.setPortNumber(batchClassEmailConfiguration.getPortNumber());
		emailConfigurationDTO.setIsSSL(batchClassEmailConfiguration.isSSL());
		return emailConfigurationDTO;
	}

	public static CmisConfigurationDTO createCmisDTO(BatchClassDTO batchClassDTO,
			BatchClassCmisConfiguration batchClassCmisConfiguration) {
		CmisConfigurationDTO cmisConfigurationDTO = new CmisConfigurationDTO();
		cmisConfigurationDTO.setBatchClass(batchClassDTO);
		cmisConfigurationDTO.setCmisProperty(batchClassCmisConfiguration.getCmisProperty());
		cmisConfigurationDTO.setFileExtension(batchClassCmisConfiguration.getFileExtension());
		cmisConfigurationDTO.setFolderName(batchClassCmisConfiguration.getFolderName());
		cmisConfigurationDTO.setPassword(batchClassCmisConfiguration.getPassword());
		cmisConfigurationDTO.setRepositoryID(batchClassCmisConfiguration.getRepositoryID());
		cmisConfigurationDTO.setServerURL(batchClassCmisConfiguration.getServerURL());
		cmisConfigurationDTO.setUserName(batchClassCmisConfiguration.getUserName());
		cmisConfigurationDTO.setIdentifier(String.valueOf(batchClassCmisConfiguration.getId()));
		cmisConfigurationDTO.setValue(batchClassCmisConfiguration.getValue());
		cmisConfigurationDTO.setValueToUpdate(batchClassCmisConfiguration.getValueToUpdate());
		return cmisConfigurationDTO;
	}

	public static WebScannerConfigurationDTO createScannerDTO(BatchClassDTO batchClassDTO,
			BatchClassScannerConfiguration batchClassScannerConfiguration) {

		WebScannerConfigurationDTO configurationDTO = new WebScannerConfigurationDTO();
		configurationDTO.setBatchClass(batchClassDTO);
		configurationDTO.setIdentifier(BatchConstants.EMPTY_STRING + batchClassScannerConfiguration.getId());
		configurationDTO.setName(batchClassScannerConfiguration.getScannerMasterConfig().getName());
		configurationDTO.setValue(batchClassScannerConfiguration.getValue());
		configurationDTO.setDescription(batchClassScannerConfiguration.getScannerMasterConfig().getDescription());
		configurationDTO.setMandatory(batchClassScannerConfiguration.getScannerMasterConfig().isMandatory());
		configurationDTO.setDataType(batchClassScannerConfiguration.getScannerMasterConfig().getType());
		configurationDTO.setSampleValue(batchClassScannerConfiguration.getScannerMasterConfig().getSampleValue());
		configurationDTO.setMultiValue(batchClassScannerConfiguration.getScannerMasterConfig().isMultiValue());

		// Setting the parent

		if (batchClassScannerConfiguration.getParent() != null) {
			BatchClassScannerConfiguration parentConfig = batchClassScannerConfiguration.getParent();
			WebScannerConfigurationDTO parent = setScannerConfigDTO(batchClassDTO, parentConfig);
			parent.setParent(null);
			configurationDTO.setParent(parent);
		}

		// setting the children
		if (batchClassScannerConfiguration.getChildren() != null) {
			List<WebScannerConfigurationDTO> children = new ArrayList<WebScannerConfigurationDTO>();
			for (BatchClassScannerConfiguration child : batchClassScannerConfiguration.getChildren()) {
				WebScannerConfigurationDTO childConfigDTO = setScannerConfigDTO(batchClassDTO, child);
				childConfigDTO.setParent(configurationDTO);
				childConfigDTO.setChildren(null);
				children.add(childConfigDTO);
			}
			configurationDTO.setChildren(children);
		}
		return configurationDTO;
	}

	public static BatchClassFieldDTO createBatchClassFieldDTO(BatchClassDTO batchClassDTO, BatchClassField batchClassField) {
		BatchClassFieldDTO batchClassFieldDTO = new BatchClassFieldDTO();
		batchClassFieldDTO.setBatchClass(batchClassDTO);
		batchClassFieldDTO.setDataType(batchClassField.getDataType());
		batchClassFieldDTO.setIdentifier(batchClassField.getIdentifier());
		batchClassFieldDTO.setName(batchClassField.getName());
		batchClassFieldDTO.setFieldOrderNumber(String.valueOf(batchClassField.getFieldOrderNumber()));
		batchClassFieldDTO.setDescription(batchClassField.getDescription());
		batchClassFieldDTO.setValidationPattern(batchClassField.getValidationPattern());
		batchClassFieldDTO.setSampleValue(batchClassField.getSampleValue());
		batchClassFieldDTO.setFieldOptionValueList(batchClassField.getFieldOptionValueList());
		return batchClassFieldDTO;
	}

	public static DocumentTypeDTO createDocumentTypeDTO(BatchClassDTO batchClassDTO, DocumentType documentType) {
		DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
		documentTypeDTO.setBatchClass(batchClassDTO);
		documentTypeDTO.setDescription(documentType.getDescription());

		documentTypeDTO.setFirstPageProjectFileName(documentType.getFirstPageProjectFileName());
		documentTypeDTO.setSecondPageProjectFileName(documentType.getSecondPageProjectFileName());
		documentTypeDTO.setThirdPageProjectFileName(documentType.getThirdPageProjectFileName());
		documentTypeDTO.setFourthPageProjectFileName(documentType.getFourthPageProjectFileName());
		documentTypeDTO.setMinConfidenceThreshold(documentType.getMinConfidenceThreshold());
		documentTypeDTO.setName(documentType.getName());
		documentTypeDTO.setPriority(documentType.getPriority());
		documentTypeDTO.setIdentifier(documentType.getIdentifier());
		documentTypeDTO.setHidden(documentType.isHidden());
		List<PageType> pageTypesList = documentType.getPages();
		List<FieldType> fieldTypesList = documentType.getFieldTypes();
		List<FunctionKey> functionKeyList = documentType.getFunctionKeys();
		for (PageType pageType : pageTypesList) {
			PageTypeDTO pageTypeDTO = createPageTypeDTO(documentTypeDTO, pageType);
			documentTypeDTO.addPageType(pageTypeDTO);
		}
		for (FieldType fieldType : fieldTypesList) {
			FieldTypeDTO fieldTypeDTO = createFieldTypeDTO(documentTypeDTO, fieldType);
			documentTypeDTO.addFieldTypeDTO(fieldTypeDTO);
		}

		// Create TableInfoDTO and TableColumsInfoDto to be used for table UI.
		// Use the following code for above.
		if (documentType.getTableInfos() != null && !documentType.getTableInfos().isEmpty()) {
			for (TableInfo tableInfo : documentType.getTableInfos()) {
				TableInfoDTO tableInfoDTO = createTableInfoDTO(documentTypeDTO, tableInfo);
				documentTypeDTO.addTableInfoDTO(tableInfoDTO);
			}
		}
		for (FunctionKey functionKey : functionKeyList) {
			FunctionKeyDTO functionKeyDTO = createFunctionKeyDTO(documentTypeDTO, functionKey);
			documentTypeDTO.addFunctionKey(functionKeyDTO);
		}
		return documentTypeDTO;
	}

	public static TableInfoDTO createTableInfoDTO(DocumentTypeDTO documentTypeDTO, TableInfo tableInfo) {
		TableInfoDTO tableInfoDTO = new TableInfoDTO();

		tableInfoDTO.setDocTypeDTO(documentTypeDTO);
		if (tableInfo.getRuleOperator() != null) {
			tableInfoDTO.setRuleOperator(tableInfo.getRuleOperator());
		}
		tableInfoDTO.setName(tableInfo.getName());
		tableInfoDTO.setIdentifier(String.valueOf(tableInfo.getId()));
		tableInfoDTO.setRemoveInvalidRows(tableInfo.isRemoveInvalidRows());
		tableInfoDTO.setDisplayImage(tableInfo.getDisplayImage());
		tableInfoDTO.setNumberOfRows(String.valueOf(tableInfo.getNumberOfRows()));
		CurrencyCode currencyCode = tableInfo.getCurrencyCode();
		String representationValue = null;
		if (currencyCode != null) {
			representationValue = currencyCode.getRepresentationValue();
		}
		tableInfoDTO.setCurrencyCode(representationValue);
		for (TableColumnsInfo tableColumnsInfo : tableInfo.getTableColumnsInfo()) {
			TableColumnInfoDTO columnInfoDTO = createTableColumnInfoDTO(tableInfoDTO, tableColumnsInfo);
			tableInfoDTO.addColumnInfo(columnInfoDTO);
		}
		if (tableInfo.getTableRuleInfo() != null) {
			for (TableRuleInfo tableRuleInfo : tableInfo.getTableRuleInfo()) {
				if (tableRuleInfo != null) {
					RuleInfoDTO ruleInfoDTO = createRuleInfoDTO(tableInfoDTO, tableRuleInfo);
					tableInfoDTO.addRuleInfo(ruleInfoDTO);
				}

			}
		}

		// Iterating over the list of table extraction rules and adding their DTOs to table info DTO object.
		final List<TableExtractionRule> listTableExtractionRules = tableInfo.getTableExtractionRules();
		if (!CollectionUtil.isEmpty(listTableExtractionRules)) {
			for (TableExtractionRule tableExtractionRule : listTableExtractionRules) {
				if (tableExtractionRule != null) {
					TableExtractionRuleDTO tableExtractionRuleDTO = createTableExtractionRuleDTO(tableInfoDTO, tableExtractionRule);
					if (null != tableExtractionRuleDTO) {
						tableInfoDTO.addTableExtractionRule(tableExtractionRuleDTO);
					}
				}

			}
		}

		return tableInfoDTO;
	}

	public static PageTypeDTO createPageTypeDTO(DocumentTypeDTO documentTypeDTO, PageType pageType) {
		PageTypeDTO pageTypeDTO = new PageTypeDTO();
		pageTypeDTO.setDescription(pageType.getDescription());
		pageTypeDTO.setDocumentTypeDTO(documentTypeDTO);
		pageTypeDTO.setName(pageType.getName());
		pageTypeDTO.setIdentifier(pageType.getIdentifier());
		return pageTypeDTO;
	}

	public static FieldTypeDTO createFieldTypeDTO(DocumentTypeDTO documentTypeDTO, FieldType fieldType) {
		FieldTypeDTO fieldTypeDTO = new FieldTypeDTO();
		fieldTypeDTO.setIdentifier(fieldType.getIdentifier());
		fieldTypeDTO.setDataType(fieldType.getDataType());
		fieldTypeDTO.setDescription(fieldType.getDescription());
		fieldTypeDTO.setDocTypeDTO(documentTypeDTO);
		fieldTypeDTO.setName(fieldType.getName());
		fieldTypeDTO.setPattern(fieldType.getPattern());
		fieldTypeDTO.setRegexListingSeparator(fieldType.getRegexListingSeparator());
		fieldTypeDTO.setSampleValue(fieldType.getSampleValue());
		fieldTypeDTO.setFieldOptionValueList(fieldType.getFieldOptionValueList());
		fieldTypeDTO.setFieldOrderNumber(String.valueOf(fieldType.getFieldOrderNumber()));
		fieldTypeDTO.setBarcodeType(fieldType.getBarcodeType());
		fieldTypeDTO.setHidden(Boolean.valueOf(fieldType.isHidden()));
		// fieldTypeDTO.setMultiLine(fieldType.isMultiLine());
		fieldTypeDTO.setReadOnly(fieldType.getIsReadOnly());
		fieldTypeDTO.setFieldValueChangeScriptEnabled(fieldType.isFieldValueChangeScriptEnabled());
		fieldTypeDTO.setOcrConfidenceThreshold(fieldType.getOcrConfidenceThreshold());
		for (KVExtraction kvExtraction : fieldType.getKvExtraction()) {
			KVExtractionDTO kvExtractionDTO = createKVExtractionDTO(fieldTypeDTO, kvExtraction);
			fieldTypeDTO.addKvExtraction(kvExtractionDTO);
		}
		for (RegexValidation regexValidation : fieldType.getRegexValidation()) {
			RegexDTO regexDTO = createRegexDTO(fieldTypeDTO, regexValidation);
			fieldTypeDTO.addRegex(regexDTO);
		}
		return fieldTypeDTO;
	}

	public static KVExtractionDTO createKVExtractionDTO(FieldTypeDTO fieldTypeDTO, KVExtraction kvExtraction) {
		KVExtractionDTO kvExtractionDTO = new KVExtractionDTO();
		kvExtractionDTO.setFieldTypeDTO(fieldTypeDTO);
		kvExtractionDTO.setIdentifier(String.valueOf(kvExtraction.getId()));
		kvExtractionDTO.setKeyPattern(kvExtraction.getKeyPattern());
		kvExtractionDTO.setLocationType(kvExtraction.getLocationType());
		kvExtractionDTO.setValuePattern(kvExtraction.getValuePattern());
		kvExtractionDTO.setNoOfWords(kvExtraction.getNoOfWords());
		kvExtractionDTO.setLength(kvExtraction.getLength());
		kvExtractionDTO.setWidth(kvExtraction.getWidth());
		kvExtractionDTO.setXoffset(kvExtraction.getXoffset());
		kvExtractionDTO.setYoffset(kvExtraction.getYoffset());
		kvExtractionDTO.setFetchValue(kvExtraction.getFetchValue());
		kvExtractionDTO.setKvPageValue(kvExtraction.getPageValue());
		kvExtractionDTO.setUseExistingKey(kvExtraction.isUseExistingKey());
		kvExtractionDTO.setOrderNumber(String.valueOf(kvExtraction.getOrderNo()));
		// Sets the KV extraction pattern weight value in it's respective DTO
		kvExtractionDTO.setWeight(kvExtraction.getWeight());

		// Setting the fuzzy match threshold value for key pattern.
		kvExtractionDTO.setKeyFuzziness(kvExtraction.getKeyFuzziness());

		// Sets the Extract Zone in it's respective DTO.
		kvExtractionDTO.setExtractZone(kvExtraction.getExtractZone());
		LOGGER.info(EphesoftStringUtil.concatenate("Value for KV Extraction Added successfully", kvExtractionDTO.getWeight()));
		AdvancedKVExtraction advancedKVExtraction = kvExtraction.getAdvancedKVExtraction();
		if (advancedKVExtraction != null) {
			kvExtractionDTO.setAdvancedKVExtractionDTO(createAdvancedKVExtractionDTO(kvExtractionDTO, advancedKVExtraction));
		}
		return kvExtractionDTO;
	}

	public static AdvancedKVExtractionDTO createAdvancedKVExtractionDTO(KVExtractionDTO kvExtractionDTO,
			AdvancedKVExtraction advancedKVExtraction) {
		AdvancedKVExtractionDTO advancedKVExtractionDTO = new AdvancedKVExtractionDTO();
		// advancedKVExtractionDTO.setImageName(advancedKVExtraction.getImageName());
		// advancedKVExtractionDTO.setDisplayImageName(advancedKVExtraction.getDisplayImageName());
		advancedKVExtractionDTO.setKeyX0Coord(advancedKVExtraction.getKeyX0Coord());
		advancedKVExtractionDTO.setKeyY0Coord(advancedKVExtraction.getKeyY0Coord());
		advancedKVExtractionDTO.setKeyX1Coord(advancedKVExtraction.getKeyX1Coord());
		advancedKVExtractionDTO.setKeyY1Coord(advancedKVExtraction.getKeyY1Coord());
		advancedKVExtractionDTO.setValueX0Coord(advancedKVExtraction.getValueX0Coord());
		advancedKVExtractionDTO.setValueY0Coord(advancedKVExtraction.getValueY0Coord());
		advancedKVExtractionDTO.setValueX1Coord(advancedKVExtraction.getValueX1Coord());
		advancedKVExtractionDTO.setValueY1Coord(advancedKVExtraction.getValueY1Coord());
		return advancedKVExtractionDTO;
	}

	public static FunctionKeyDTO createFunctionKeyDTO(DocumentTypeDTO documentTypeDTO, FunctionKey functionKey) {
		FunctionKeyDTO functionKeyDTO = new FunctionKeyDTO();
		functionKeyDTO.setDocTypeDTO(documentTypeDTO);
		functionKeyDTO.setMethodName(functionKey.getMethodName());
		functionKeyDTO.setMethodDescription(functionKey.getUiLabel());
		functionKeyDTO.setShortcutKeyName(functionKey.getShortcutKeyname());
		functionKeyDTO.setIdentifier(functionKey.getIdentifier());
		return functionKeyDTO;
	}

	public static RuleInfoDTO createRuleInfoDTO(TableInfoDTO tableInfoDTO, TableRuleInfo tableRuleInfo) {
		RuleInfoDTO ruleInfoDTO = new RuleInfoDTO();
		if (tableRuleInfo != null) {
			ruleInfoDTO.setIdentifier(String.valueOf(tableRuleInfo.getId()));
			ruleInfoDTO.setRule(tableRuleInfo.getRule());
			ruleInfoDTO.setTableInfoDTO(tableInfoDTO);
			ruleInfoDTO.setDescription(tableRuleInfo.getDescription());
		}
		return ruleInfoDTO;
	}

	public static TableColumnInfoDTO createTableColumnInfoDTO(TableInfoDTO tableInfoDTO, TableColumnsInfo tableColumnsInfo) {
		TableColumnInfoDTO columnInfoDTO = new TableColumnInfoDTO();

		// Column extraction information mopved to table column extraction rule and column ddescription is added to table column
		// information.
		columnInfoDTO.setColumnName(tableColumnsInfo.getColumnName());
		columnInfoDTO.setValidationPattern(tableColumnsInfo.getValidationPattern());
		columnInfoDTO.setIdentifier(String.valueOf(tableColumnsInfo.getId()));
		columnInfoDTO.setColumnDescription(tableColumnsInfo.getColumnDescription());
		columnInfoDTO.setAlternateValues(tableColumnsInfo.getAlternateValues());
		columnInfoDTO.setTableInfoDTO(tableInfoDTO);
		return columnInfoDTO;
	}

	public static RegexDTO createRegexDTO(FieldTypeDTO fieldTypeDTO, RegexValidation regexValidation) {
		RegexDTO regexDTO = new RegexDTO();
		regexDTO.setPattern(regexValidation.getPattern());
		regexDTO.setIdentifier(String.valueOf(regexValidation.getId()));
		regexDTO.setFieldTypeDTO(fieldTypeDTO);
		return regexDTO;

	}

	public static BatchClassModuleDTO createBatchClassModuleDTO(BatchClassDTO batchClassDTO, BatchClassModule batchClassModule,
			PluginService pluginService) {

		BatchClassModuleDTO batchClassModuleDTO = new BatchClassModuleDTO();
		// if (!batchClassDTO.isDeployed()) {
		// batchClassModuleDTO.setNew(true);
		// }

		batchClassModuleDTO.setBatchClass(batchClassDTO);
		batchClassModuleDTO.setIdentifier(BatchConstants.EMPTY_STRING + batchClassModule.getId());
		batchClassModuleDTO.setOrderNumber(batchClassModule.getOrderNumber());
		batchClassModuleDTO.setRemoteURL(batchClassModule.getRemoteURL());
		batchClassModuleDTO.setRemoteBatchClassIdentifier(batchClassModule.getRemoteBatchClassIdentifier());
		batchClassModuleDTO.setWorkflowName(batchClassModule.getWorkflowName());

		batchClassModuleDTO.setModule(createModuleDTO(batchClassModule.getModule()));

		List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
		if (batchClassPlugins == null) {
			batchClassPlugins = new ArrayList<BatchClassPlugin>(0);
		}
		for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
			BatchClassPluginDTO batchClassPluginDTO = createBatchClassPluginDTO(batchClassModuleDTO, batchClassPlugin, pluginService);
			batchClassModuleDTO.addBatchClassPlugin(batchClassPluginDTO);
		}
		return batchClassModuleDTO;
	}

	public static ModuleDTO createModuleDTO(Module module) {
		ModuleDTO moduleDTO = new ModuleDTO();
		moduleDTO.setDescription(module.getDescription());
		moduleDTO.setIdentifier(BatchConstants.EMPTY_STRING + module.getId());
		moduleDTO.setName(module.getName());
		moduleDTO.setVersion(module.getVersion());
		return moduleDTO;
	}

	public static BatchClassPluginDTO createBatchClassPluginDTO(BatchClassModuleDTO batchClassModuleDTO,
			BatchClassPlugin batchClassPlugin, PluginService pluginService) {
		BatchClassPluginDTO batchClassPluginDTO = new BatchClassPluginDTO();
		batchClassPluginDTO.setBatchClassModule(batchClassModuleDTO);
		batchClassPluginDTO.setIdentifier(BatchConstants.EMPTY_STRING + batchClassPlugin.getId());
		batchClassPluginDTO.setOrderNumber(batchClassPlugin.getOrderNumber());

		batchClassPluginDTO.setPlugin(createPluginDetailsDTO(batchClassPlugin.getPlugin(), pluginService));

		List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();
		if (batchClassPluginConfigs == null) {
			batchClassPluginConfigs = new ArrayList<BatchClassPluginConfig>(0);
		}
		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			BatchClassPluginConfigDTO batchClassPluginConfigDTO = createBatchClassPluginConfigDTO(batchClassPluginDTO,
					batchClassPluginConfig);
			batchClassPluginDTO.addBatchClassPluginConfig(batchClassPluginConfigDTO);

		}

		batchClassPluginDTO.sortBatchClassPluginConfigList();
		List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassPlugin.getBatchClassDynamicPluginConfigs();
		if (batchClassDynamicPluginConfigs == null) {
			batchClassDynamicPluginConfigs = new ArrayList<BatchClassDynamicPluginConfig>(0);
		}
		for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
			BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO = createBatchClassDynamicPluginConfigDTO(
					batchClassPluginDTO, batchClassDynamicPluginConfig);
			batchClassPluginDTO.addBatchClassDynamicPluginConfig(batchClassDynamicPluginConfigDTO);

		}

		return batchClassPluginDTO;
	}

	public static PluginDetailsDTO createPluginDetailsDTO(Plugin plugin, PluginService pluginService) {
		PluginDetailsDTO pluginDetailsDTO = new PluginDetailsDTO();
		pluginDetailsDTO.setIdentifier(BatchConstants.EMPTY_STRING + plugin.getId());
		pluginDetailsDTO.setPluginDescription(plugin.getDescription());
		pluginDetailsDTO.setPluginName(plugin.getPluginName());
		pluginDetailsDTO.setPluginWorkflowName(plugin.getWorkflowName());
		pluginDetailsDTO.setScriptName(plugin.getScriptName());
		pluginDetailsDTO.setPluginInformation(plugin.getInformation());

		List<Dependency> dependenciesList = plugin.getDependencies();
		pluginDetailsDTO.setDependencies(new ArrayList<DependencyDTO>(0));
		if (dependenciesList != null) {
			for (Dependency dependency : dependenciesList) {
				DependencyDTO dependencyDTO = createDependencyDTO(dependency, pluginService);
				if (dependencyDTO != null) {
					dependencyDTO.setPluginDTO(pluginDetailsDTO);
					pluginDetailsDTO.getDependencies().add(dependencyDTO);
				}
			}
		}
		return pluginDetailsDTO;
	}

	public static BatchClassPluginConfigDTO createBatchClassPluginConfigDTO(BatchClassPluginDTO batchClassPluginDTO,
			BatchClassPluginConfig batchClassPluginConfig) {

		BatchClassPluginConfigDTO batchClassPluginConfigDTO = new BatchClassPluginConfigDTO();
		batchClassPluginConfigDTO.setBatchClassPlugin(batchClassPluginDTO);
		batchClassPluginConfigDTO.setDescription(batchClassPluginConfig.getDescription());
		batchClassPluginConfigDTO.setIdentifier(BatchConstants.EMPTY_STRING + batchClassPluginConfig.getId());
		batchClassPluginConfigDTO.setName(batchClassPluginConfig.getName());
		batchClassPluginConfigDTO.setSampleValue(batchClassPluginConfig.getSampleValue());
		batchClassPluginConfigDTO.setQualifier(batchClassPluginConfig.getQualifier());
		PluginConfig pluginConfig = batchClassPluginConfig.getPluginConfig();
		batchClassPluginConfigDTO.setOrderNumber(pluginConfig.getOrderNumber());
		if (batchClassPluginConfig.getKvPageProcesses() != null && !batchClassPluginConfig.getKvPageProcesses().isEmpty()) {
			List<KVPageProcess> kvPageProcesses = batchClassPluginConfig.getKvPageProcesses();
			List<KVPageProcessDTO> kvPageProcessDTOs = new ArrayList<KVPageProcessDTO>();
			for (KVPageProcess kvPageProcess : kvPageProcesses) {
				KVPageProcessDTO kvPageProcessDTO = new KVPageProcessDTO();
				kvPageProcessDTO.setKeyPattern(kvPageProcess.getKeyPattern());
				kvPageProcessDTO.setLocationType(kvPageProcess.getLocationType());
				kvPageProcessDTO.setValuePattern(kvPageProcess.getValuePattern());
				kvPageProcessDTO.setNoOfWords(kvPageProcess.getNoOfWords());
				kvPageProcessDTO.setIdentifier(kvPageProcess.getId());
				kvPageProcessDTO.setPageLevelFieldName(kvPageProcess.getPageLevelFieldName());
				kvPageProcessDTOs.add(kvPageProcessDTO);
			}
			batchClassPluginConfigDTO.setKvPageProcessDTOs(kvPageProcessDTOs);
		} else {
			batchClassPluginConfigDTO.setKvPageProcessDTOs(null);
		}

		PluginConfigurationDTO pluginConfigurationDTO = new PluginConfigurationDTO();

		if (pluginConfig != null) {
			batchClassPluginConfigDTO.setMultivalue(pluginConfig.isMultiValue());
			batchClassPluginConfigDTO.setMandatory(pluginConfig.isMandatory());
			DataType pluginCOnfigDataType = pluginConfig.getDataType();
			batchClassPluginConfigDTO.setDataType(pluginCOnfigDataType);
			if (pluginCOnfigDataType == DataType.PASSWORD) {
				batchClassPluginConfigDTO.setPassword(true);
			}
			pluginConfigurationDTO.setFieldName(pluginConfig.getName());
			pluginConfigurationDTO.setFieldValue(batchClassPluginConfig.getValue());
			pluginConfigurationDTO.setIdentifier(BatchConstants.EMPTY_STRING + pluginConfig.getId());
			pluginConfigurationDTO.setSampleValue(pluginConfig.getSampleValue());
		}
		batchClassPluginConfigDTO.setPluginConfig(pluginConfigurationDTO);
		batchClassPluginConfigDTO.setValue(batchClassPluginConfig.getValue());
		pluginConfigurationDTO.setDirty(false);

		return batchClassPluginConfigDTO;
	}

	public static BatchClassDynamicPluginConfigDTO createBatchClassDynamicPluginConfigDTO(BatchClassPluginDTO batchClassPluginDTO,
			BatchClassDynamicPluginConfig batchClassDynamicPluginConfig) {

		BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO = new BatchClassDynamicPluginConfigDTO();
		batchClassDynamicPluginConfigDTO.setBatchClassPlugin(batchClassPluginDTO);
		batchClassDynamicPluginConfigDTO.setDescription(batchClassDynamicPluginConfig.getDescription());
		batchClassDynamicPluginConfigDTO.setIdentifier(BatchConstants.EMPTY_STRING + batchClassDynamicPluginConfig.getId());
		batchClassDynamicPluginConfigDTO.setName(batchClassDynamicPluginConfig.getName());
		batchClassDynamicPluginConfigDTO.setValue(batchClassDynamicPluginConfig.getValue());

		// Setting the parent

		if (batchClassDynamicPluginConfig.getParent() != null) {
			BatchClassDynamicPluginConfig parentPluginConfig = batchClassDynamicPluginConfig.getParent();
			BatchClassDynamicPluginConfigDTO parent = setBatchClassDynamicPluginConfigDTO(batchClassPluginDTO, parentPluginConfig);
			parent.setParent(null);
			batchClassDynamicPluginConfigDTO.setParent(parent);
		}

		// setting the children

		if (batchClassDynamicPluginConfig.getChildren() != null) {
			List<BatchClassDynamicPluginConfigDTO> children = new ArrayList<BatchClassDynamicPluginConfigDTO>();
			for (BatchClassDynamicPluginConfig child : batchClassDynamicPluginConfig.getChildren()) {
				BatchClassDynamicPluginConfigDTO childBatchClassDynamicPluginConfigDTO = setBatchClassDynamicPluginConfigDTO(
						batchClassPluginDTO, child);
				childBatchClassDynamicPluginConfigDTO.setParent(batchClassDynamicPluginConfigDTO);
				childBatchClassDynamicPluginConfigDTO.setChildren(null);
				children.add(childBatchClassDynamicPluginConfigDTO);
			}

			batchClassDynamicPluginConfigDTO.setChildren(children);
		}

		return batchClassDynamicPluginConfigDTO;
	}

	public static BatchClassPluginConfigDTO setBatchClassPluginConfigDTO(BatchClassPluginDTO batchClassPluginDTO,
			BatchClassPluginConfig batchClassPluginConfig) {
		BatchClassPluginConfigDTO batchClassPluginConfigDTO = new BatchClassPluginConfigDTO();
		batchClassPluginConfigDTO.setBatchClassPlugin(batchClassPluginDTO);
		batchClassPluginConfigDTO.setDescription(batchClassPluginConfig.getDescription());
		batchClassPluginConfigDTO.setIdentifier(BatchConstants.EMPTY_STRING + batchClassPluginConfig.getId());
		batchClassPluginConfigDTO.setName(batchClassPluginConfig.getName());
		batchClassPluginConfigDTO.setSampleValue(batchClassPluginConfig.getSampleValue());
		batchClassPluginConfigDTO.setQualifier(batchClassPluginConfig.getQualifier());
		DataType pluginCOnfigDataType = batchClassPluginConfig.getPluginConfig().getDataType();
		batchClassPluginConfigDTO.setDataType(pluginCOnfigDataType);
		if (pluginCOnfigDataType == DataType.PASSWORD) {
			batchClassPluginConfigDTO.setPassword(true);
		}
		batchClassPluginConfigDTO.setOrderNumber(batchClassPluginConfig.getPluginConfig().getOrderNumber());
		PluginConfigurationDTO pluginConfigurationDTO = refreshPluginConfigurationDTO(batchClassPluginConfig);

		batchClassPluginConfigDTO.setPluginConfig(pluginConfigurationDTO);
		batchClassPluginConfigDTO.setValue(batchClassPluginConfig.getValue());
		return batchClassPluginConfigDTO;
	}

	/**
	 * @param batchClassPluginConfig
	 * @return
	 */
	private static PluginConfigurationDTO refreshPluginConfigurationDTO(BatchClassPluginConfig batchClassPluginConfig) {
		PluginConfigurationDTO pluginConfigurationDTO = new PluginConfigurationDTO();

		if (batchClassPluginConfig.getPluginConfig() != null) {
			pluginConfigurationDTO.setFieldName(batchClassPluginConfig.getName());
			pluginConfigurationDTO.setFieldValue(batchClassPluginConfig.getValue());
			pluginConfigurationDTO.setIdentifier(BatchConstants.EMPTY_STRING + batchClassPluginConfig.getId());
			pluginConfigurationDTO.setSampleValue(batchClassPluginConfig.getSampleValue());
		}
		return pluginConfigurationDTO;
	}

	public static BatchClassDynamicPluginConfigDTO setBatchClassDynamicPluginConfigDTO(BatchClassPluginDTO batchClassPluginDTO,
			BatchClassDynamicPluginConfig batchClassDynamicPluginConfig) {
		BatchClassDynamicPluginConfigDTO batchClassDynamicPluginConfigDTO = new BatchClassDynamicPluginConfigDTO();
		batchClassDynamicPluginConfigDTO.setBatchClassPlugin(batchClassPluginDTO);
		batchClassDynamicPluginConfigDTO.setDescription(batchClassDynamicPluginConfig.getDescription());
		batchClassDynamicPluginConfigDTO.setIdentifier(BatchConstants.EMPTY_STRING + batchClassDynamicPluginConfig.getId());
		batchClassDynamicPluginConfigDTO.setName(batchClassDynamicPluginConfig.getName());
		batchClassDynamicPluginConfigDTO.setValue(batchClassDynamicPluginConfig.getValue());
		return batchClassDynamicPluginConfigDTO;
	}

	public static WebScannerConfigurationDTO setScannerConfigDTO(BatchClassDTO batchClassDTO,
			BatchClassScannerConfiguration batchClassScannerConfig) {
		WebScannerConfigurationDTO configDTO = new WebScannerConfigurationDTO();
		configDTO.setBatchClass(batchClassDTO);
		configDTO.setIdentifier(BatchConstants.EMPTY_STRING + batchClassScannerConfig.getId());
		configDTO.setName(batchClassScannerConfig.getScannerMasterConfig().getName());
		configDTO.setValue(batchClassScannerConfig.getValue());

		configDTO.setSampleValue(batchClassScannerConfig.getScannerMasterConfig().getSampleValue());
		configDTO.setMultiValue(batchClassScannerConfig.getScannerMasterConfig().isMultiValue());
		configDTO.setDescription(batchClassScannerConfig.getScannerMasterConfig().getDescription());
		configDTO.setMandatory(batchClassScannerConfig.getScannerMasterConfig().isMandatory());
		configDTO.setDataType(batchClassScannerConfig.getScannerMasterConfig().getType());
		return configDTO;
	}

	public static BatchClassScannerConfiguration mergeScannerConfig(BatchClassScannerConfiguration config,
			WebScannerConfigurationDTO configDTO) {
		config.setId(Long.valueOf(configDTO.getIdentifier()));
		config.getScannerMasterConfig().setName(configDTO.getName());
		config.setValue(configDTO.getValue());
		return config;
	}

	public static void updateRevisionNumber(BatchClass batchClass) {
		if (null == batchClass) {
			return;
		}
		String version = batchClass.getVersion();
		if (null == version) {
			return;
		}
		String newVersion = version;
		if (version.contains(".")) {
			int lastIndex = version.lastIndexOf('.');
			String preFix = version.substring(0, lastIndex);
			String postFix = version.substring(lastIndex + 1);
			int revNumber = Integer.parseInt(postFix);
			revNumber++;
			newVersion = preFix + "." + revNumber;
		} else {
			int revNumber = Integer.parseInt(version);
			revNumber++;
			newVersion = String.valueOf(revNumber);
		}
		batchClass.setVersion(newVersion);
	}

	public static void copyModules(BatchClass batchClass) {
		List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
		List<BatchClassModule> newBatchClassModulesList = new ArrayList<BatchClassModule>();
		for (BatchClassModule batchClassModule : batchClassModules) {
			newBatchClassModulesList.add(batchClassModule);
			batchClassModule.setId(0);
			batchClassModule.setBatchClass(null);
			copyPlugins(batchClassModule);
			copyModuleConfig(batchClassModule);
		}
		batchClass.setBatchClassModules(newBatchClassModulesList);
	}

	public static void copyModuleConfig(BatchClassModule batchClassModule) {
		List<BatchClassModuleConfig> batchClassModConfigs = batchClassModule.getBatchClassModuleConfig();
		List<BatchClassModuleConfig> newBatchClassModuleConfigList = new ArrayList<BatchClassModuleConfig>();
		for (BatchClassModuleConfig batchClassModConfig : batchClassModConfigs) {
			newBatchClassModuleConfigList.add(batchClassModConfig);
			batchClassModConfig.setId(0);
			batchClassModConfig.setBatchClassModule(null);
		}
		batchClassModule.setBatchClassModuleConfig(newBatchClassModuleConfigList);
	}

	public static void copyPlugins(BatchClassModule batchClassModule) {
		List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
		List<BatchClassPlugin> newBatchClassPluginsList = new ArrayList<BatchClassPlugin>();
		for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
			newBatchClassPluginsList.add(batchClassPlugin);
			batchClassPlugin.setId(0);
			batchClassPlugin.setBatchClassModule(null);
			copyPluginConfigs(batchClassPlugin);
			copyDynamicPluginConfigs(batchClassPlugin);
		}
		batchClassModule.setBatchClassPlugins(newBatchClassPluginsList);
	}

	public static void copyPluginConfigs(BatchClassPlugin batchClassPlugin) {
		List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();
		List<BatchClassPluginConfig> newBatchClassPluginConfigsList = new ArrayList<BatchClassPluginConfig>();
		for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			newBatchClassPluginConfigsList.add(batchClassPluginConfig);
			batchClassPluginConfig.setId(0);
			batchClassPluginConfig.setBatchClassPlugin(null);
			// copyBatchClassPluginConfigsChild(batchClassPluginConfig);
			copyKVPageProcess(batchClassPluginConfig);
		}
		batchClassPlugin.setBatchClassPluginConfigs(newBatchClassPluginConfigsList);
	}

	public static void copyDynamicPluginConfigs(BatchClassPlugin batchClassPlugin) {
		List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassPlugin.getBatchClassDynamicPluginConfigs();
		List<BatchClassDynamicPluginConfig> newBatchClassDynamicPluginConfigsList = new ArrayList<BatchClassDynamicPluginConfig>();
		for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
			newBatchClassDynamicPluginConfigsList.add(batchClassDynamicPluginConfig);
			batchClassDynamicPluginConfig.setId(0);
			batchClassDynamicPluginConfig.setBatchClassPlugin(null);
			copyBatchClassDynamicPluginConfigsChild(batchClassDynamicPluginConfig);
		}
		batchClassPlugin.setBatchClassDynamicPluginConfigs(newBatchClassDynamicPluginConfigsList);
	}

	private static void copyKVPageProcess(BatchClassPluginConfig batchClassPluginConfig) {
		List<KVPageProcess> kvPageProcess = batchClassPluginConfig.getKvPageProcesses();
		List<KVPageProcess> newKvPageProcessList = new ArrayList<KVPageProcess>();
		for (KVPageProcess kVPageProcessChild : kvPageProcess) {
			kVPageProcessChild.setId(0);
			newKvPageProcessList.add(kVPageProcessChild);
			kVPageProcessChild.setBatchClassPluginConfig(null);
		}
		batchClassPluginConfig.setKvPageProcesses(newKvPageProcessList);
	}

	private static void copyBatchClassDynamicPluginConfigsChild(BatchClassDynamicPluginConfig batchClassDynamicPluginConfig) {
		List<BatchClassDynamicPluginConfig> children = batchClassDynamicPluginConfig.getChildren();
		List<BatchClassDynamicPluginConfig> newChildrenList = new ArrayList<BatchClassDynamicPluginConfig>();
		for (BatchClassDynamicPluginConfig child : children) {
			child.setId(0);
			newChildrenList.add(child);
			child.setBatchClassPlugin(null);
			child.setParent(null);
		}
		batchClassDynamicPluginConfig.setChildren(newChildrenList);
	}

	public static void copyScannerConfig(BatchClass batchClass) {
		List<BatchClassScannerConfiguration> configs = batchClass.getBatchClassScannerConfiguration();
		List<BatchClassScannerConfiguration> newConfigsList = new ArrayList<BatchClassScannerConfiguration>();
		for (BatchClassScannerConfiguration config : configs) {
			if (config.getParent() == null) {
				newConfigsList.add(config);
				config.setId(0);
				config.setBatchClass(null);

				List<BatchClassScannerConfiguration> children = config.getChildren();
				for (BatchClassScannerConfiguration child : children) {
					child.setId(0);
					newConfigsList.add(child);
					child.setBatchClass(null);
					child.setParent(config);
				}
			}
		}
		batchClass.setBatchClassScannerConfiguration(newConfigsList);
	}

	public static void copyDocumentTypes(BatchClass batchClass) {
		List<DocumentType> documentTypes = batchClass.getDocumentTypes();
		List<DocumentType> newDocumentType = new ArrayList<DocumentType>();
		for (DocumentType documentType : documentTypes) {
			newDocumentType.add(documentType);
			documentType.setId(0);
			documentType.setBatchClass(null);
			documentType.setIdentifier(null);
			copyPageTypes(documentType);
			copyFieldTypes(documentType);
			copyTableInfo(documentType);
			copyFunctionKeys(documentType);
		}
		batchClass.setDocumentTypes(newDocumentType);
	}

	public static void copyFunctionKeys(DocumentType documentType) {
		List<FunctionKey> functionKeys = documentType.getFunctionKeys();
		List<FunctionKey> newFunctionKeys = new ArrayList<FunctionKey>();
		for (FunctionKey functionKey : functionKeys) {
			newFunctionKeys.add(functionKey);
			functionKey.setId(0);
			functionKey.setDocType(null);
			functionKey.setIdentifier(null);
		}
		documentType.setFunctionKeys(newFunctionKeys);

	}

	/**
	 * This method copies function keys for given document type.
	 * 
	 * @param documentType to be copied document type.
	 */
	public static void copyFunctionKeysForCopyDocument(DocumentType documentType) {
		LOGGER.info("Copying function key for:" + documentType.getIdentifier());
		List<FunctionKey> functionKeys = documentType.getFunctionKeys();
		List<FunctionKey> newFunctionKeys = new ArrayList<FunctionKey>();
		for (FunctionKey functionKey : functionKeys) {
			newFunctionKeys.add(functionKey);
			functionKey.setId(0);
			functionKey.setDocType(null);
			functionKey.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		}
		documentType.setFunctionKeys(newFunctionKeys);
		LOGGER.info("Function keys copied for:" + documentType.getIdentifier());
	}

	public static void copyBatchClassField(BatchClass batchClass) {
		List<BatchClassField> batchClassField = batchClass.getBatchClassField();
		List<BatchClassField> newBatchClassField = new ArrayList<BatchClassField>();
		for (BatchClassField batchClassFieldTemp : batchClassField) {
			newBatchClassField.add(batchClassFieldTemp);
			batchClassFieldTemp.setId(0);
			batchClassFieldTemp.setBatchClass(null);
			batchClassFieldTemp.setIdentifier(null);
		}
		batchClass.setBatchClassField(newBatchClassField);
	}

	public static void copyTableInfo(DocumentType documentType) {
		List<TableInfo> tableInfos = documentType.getTableInfos();
		List<TableInfo> newTableInfo = new ArrayList<TableInfo>();
		for (TableInfo tableInfo : tableInfos) {
			newTableInfo.add(tableInfo);
			tableInfo.setId(0);
			tableInfo.setDocType(null);
			copyTableColumnsInfo(tableInfo);
			copyTableRuleInfo(tableInfo);

			// Copying table extraction rules for a table.
			copyTableExtractionRule(tableInfo);
		}
		documentType.setTableInfos(newTableInfo);
	}

	/**
	 * Copies table information of the copied document to the new document. If document type is null, batch class of document type is
	 * null or table info data is empty the method performs no action.
	 * 
	 * @param documentType {@link DocumentType} Copied document being updated for new document data.
	 */
	public static void copyTableInfoForCopyDocument(final DocumentType documentType) {
		if (documentType != null) {
			final BatchClass batchClass = documentType.getBatchClass();
			if (batchClass != null) {
				String batchClassIdentifier = batchClass.getIdentifier();
				String documentTypeIdentifier = documentType.getIdentifier();
				LOGGER.debug(EphesoftStringUtil.concatenate("Copying table info for document: ", documentTypeIdentifier,
						" of batch class: ", batchClassIdentifier));
				final List<TableInfo> tableInfos = documentType.getTableInfos();
				for (final TableInfo tableInfo : tableInfos) {

					/* The id is set to a non-zero value as this the id is used for table information map key. */
					if (tableInfo != null) {
						tableInfo.setId(RandomIdGenerator.getIdentifier());
						tableInfo.setDocType(null);
						copyTableColumnsInfo(tableInfo);
						copyTableRuleInfo(tableInfo);

						// Copying table extraction rules for a table.
						copyTableExtractionRule(tableInfo);
					}
				}
				LOGGER.debug(EphesoftStringUtil.concatenate("Table info copied for document: ", documentTypeIdentifier,
						" of batch class:", batchClassIdentifier));
			}
		}
	}

	public static void copyTableColumnsInfo(TableInfo tableInfo) {
		List<TableColumnsInfo> tableColumnsInfos = tableInfo.getTableColumnsInfo();
		List<TableColumnsInfo> newTableColumnsInfo = new ArrayList<TableColumnsInfo>();
		for (TableColumnsInfo tableColumnsInfo : tableColumnsInfos) {
			newTableColumnsInfo.add(tableColumnsInfo);
			tableColumnsInfo.setId(0);
		}
		tableInfo.setTableColumnsInfo(newTableColumnsInfo);
	}

	public static void copyFieldTypes(DocumentType documentType) {
		List<FieldType> fieldTypes = documentType.getFieldTypes();
		List<FieldType> newFieldType = new ArrayList<FieldType>();
		for (FieldType fieldType : fieldTypes) {
			newFieldType.add(fieldType);
			fieldType.setId(0);
			fieldType.setDocType(null);
			fieldType.setIdentifier(null);
			copyKVExtractionFields(fieldType);
			copyRegex(fieldType);
		}
		documentType.setFieldTypes(newFieldType);
	}

	/**
	 * This method copies field types for given document type.
	 * 
	 * @param documentType to be copied document type.
	 */
	public static void copyFieldTypesForCopyDocument(DocumentType documentType) {
		LOGGER.info("Copying field type for:" + documentType.getIdentifier());
		List<FieldType> fieldTypes = documentType.getFieldTypes();
		List<FieldType> newFieldType = new ArrayList<FieldType>();
		for (FieldType fieldType : fieldTypes) {
			newFieldType.add(fieldType);
			fieldType.setId(0);
			fieldType.setDocType(null);
			fieldType.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
			copyKVExtractionFields(fieldType);
			copyRegex(fieldType);
		}
		documentType.setFieldTypes(newFieldType);
		LOGGER.info("Field types copied for:" + documentType.getIdentifier());
	}

	public static void copyKVExtractionFields(FieldType fieldType) {
		List<KVExtraction> kvExtraction2 = fieldType.getKvExtraction();
		List<KVExtraction> newKvExtraction = new ArrayList<KVExtraction>();
		for (KVExtraction kvExtraction : kvExtraction2) {
			newKvExtraction.add(kvExtraction);
			kvExtraction.setId(0);
			kvExtraction.setFieldType(null);
			copyAdvancedKVExtraction(kvExtraction);
		}
		fieldType.setKvExtraction(newKvExtraction);
	}

	public static void copyAdvancedKVExtraction(final KVExtraction kvExtraction) {
		if (kvExtraction != null) {
			AdvancedKVExtraction advancedKVExtraction = kvExtraction.getAdvancedKVExtraction();
			if (advancedKVExtraction != null) {
				advancedKVExtraction.setId(0);
				advancedKVExtraction.setKvExtraction(null);
			}
			// advancedKVExtraction
		}
	}

	public static void copyRegex(FieldType fieldType) {
		List<RegexValidation> regexValidations = fieldType.getRegexValidation();
		List<RegexValidation> regexValidations2 = new ArrayList<RegexValidation>();
		for (RegexValidation regexValidation : regexValidations) {
			regexValidations2.add(regexValidation);
			regexValidation.setId(0);
			regexValidation.setFieldType(null);
		}
		fieldType.setRegexValidation(regexValidations2);
	}

	public static void copyPageTypes(DocumentType documentType) {
		List<PageType> pages = documentType.getPages();
		List<PageType> newPageTypes = new ArrayList<PageType>();
		for (PageType pageType : pages) {
			newPageTypes.add(pageType);
			pageType.setId(0);
			pageType.setDocType(null);
			pageType.setIdentifier(null);
		}
		documentType.setPages(newPageTypes);
	}

	/**
	 * This method create Page Type for new document type.
	 * 
	 * @param documentType document type for which page type is to be created
	 */
	public static void changePageTypeForNewDocument(DocumentType documentType) {
		LOGGER.info("Creating page type for:" + documentType.getIdentifier());
		List<PageType> pages = documentType.getPages();
		List<PageType> newPageTypes = new ArrayList<PageType>();
		int iVar = 0;
		for (PageType pageType : pages) {
			pageType.setId(0);
			pageType.setDocType(null);
			pageType.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
			newPageTypes.add(pageType);
			switch (iVar) {
				case 0:
					pageType.setName(documentType.getName() + BatchConstants.FIRST_PAGE);
					pageType.setDescription(documentType.getName() + BatchConstants.FIRST_PAGE);
					break;
				case 1:
					pageType.setName(documentType.getName() + BatchConstants.MIDDLE_PAGE);
					pageType.setDescription(documentType.getName() + BatchConstants.MIDDLE_PAGE);
					break;
				case 2:
					pageType.setName(documentType.getName() + BatchConstants.LAST_PAGE);
					pageType.setDescription(documentType.getName() + BatchConstants.LAST_PAGE);
					break;
				default:
					break;
			}
			iVar++;
		}
		documentType.setPages(newPageTypes);
		LOGGER.info("Page type created for:" + documentType.getIdentifier());
	}

	public static void mergeKvPageProcessFromDTO(KVPageProcess kvPageProcess, KVPageProcessDTO kvPageProcessDTO) {
		kvPageProcess.setKeyPattern(kvPageProcessDTO.getKeyPattern());
		kvPageProcess.setLocationType(kvPageProcessDTO.getLocationType());
		kvPageProcess.setValuePattern(kvPageProcessDTO.getValuePattern());
		kvPageProcess.setNoOfWords(kvPageProcessDTO.getNoOfWords());
		kvPageProcess.setPageLevelFieldName(kvPageProcessDTO.getPageLevelFieldName());
	}

	public static InputDataCarrier createInputDataCarrierFromKVExtDTO(KVExtractionDTO kvExtractionDTO) {
		AdvancedKVExtractionDTO advancedKVExtraction = kvExtractionDTO.getAdvancedKVExtractionDTO();
		Coordinates keyRectangleCoordinates = null;
		Coordinates valueRectangleCoordinates = null;
		if (advancedKVExtraction != null) {
			keyRectangleCoordinates = new Coordinates();
			valueRectangleCoordinates = new Coordinates();

			// EPHESOFT-11278 In case of copy of normal KV field ,Test KV is throwing null pointer exception.
			keyRectangleCoordinates.setX0(ParseUtil.getBigIntegerValue(advancedKVExtraction.getKeyX0Coord()));
			keyRectangleCoordinates.setY0(ParseUtil.getBigIntegerValue(advancedKVExtraction.getKeyY0Coord()));
			keyRectangleCoordinates.setX1(ParseUtil.getBigIntegerValue(advancedKVExtraction.getKeyX1Coord()));
			keyRectangleCoordinates.setY1(ParseUtil.getBigIntegerValue(advancedKVExtraction.getKeyY1Coord()));
			valueRectangleCoordinates.setX0(ParseUtil.getBigIntegerValue(advancedKVExtraction.getValueX0Coord()));
			valueRectangleCoordinates.setY0(ParseUtil.getBigIntegerValue(advancedKVExtraction.getValueY0Coord()));
			valueRectangleCoordinates.setX1(ParseUtil.getBigIntegerValue(advancedKVExtraction.getValueX1Coord()));
			valueRectangleCoordinates.setY1(ParseUtil.getBigIntegerValue(advancedKVExtraction.getValueY1Coord()));
		}
		InputDataCarrier inputDataCarrier = new InputDataCarrier(kvExtractionDTO.getLocationType(), kvExtractionDTO.getKeyPattern(),
				kvExtractionDTO.getValuePattern(), kvExtractionDTO.getNoOfWords(), kvExtractionDTO.getFetchValue(),
				kvExtractionDTO.getLength(), kvExtractionDTO.getWidth(), kvExtractionDTO.getXoffset(), kvExtractionDTO.getYoffset(),
				kvExtractionDTO.isUseExistingKey(), keyRectangleCoordinates, valueRectangleCoordinates, kvExtractionDTO.getWeight(),
				kvExtractionDTO.getKeyFuzziness());

		inputDataCarrier.setExtractZone(kvExtractionDTO.getExtractZone());

		return inputDataCarrier;
	}

	public static List<OutputDataCarrierDTO> createOutputDataDTOFromOutputDataCarrier(List<OutputDataCarrier> outputDataCarriers,
			List<OutputDataCarrierDTO> carrierDtos, String inputFileName) {
		if (outputDataCarriers != null && !outputDataCarriers.isEmpty()) {
			for (OutputDataCarrier outputDataCarrier : outputDataCarriers) {
				Coordinates hocr = outputDataCarrier.getSpan().getCoordinates();
				CoordinatesDTO coordinates = null;
				if (hocr != null) {
					coordinates = new CoordinatesDTO();
					coordinates.setX0(hocr.getX0());
					coordinates.setX1(hocr.getX1());
					coordinates.setY0(hocr.getY0());
					coordinates.setY1(hocr.getY1());
				}
				OutputDataCarrierDTO dto = new OutputDataCarrierDTO();
				dto.setConfidence(outputDataCarrier.getConfidence());
				dto.setCoordinates(coordinates);
				dto.setValue(outputDataCarrier.getValue());
				dto.setInputFileName(inputFileName);
				carrierDtos.add(dto);
			}
		}
		return carrierDtos;
	}

	public static RoleDTO createRoleDTO(BatchClassGroups role) {
		RoleDTO roleDTO = new RoleDTO();
		roleDTO.setName(role.getGroupName());
		return roleDTO;
	}

	public static void exportEmailConfiguration(BatchClass batchClass) {
		List<BatchClassEmailConfiguration> batchClassEmailConfigurations = batchClass.getEmailConfigurations();
		List<BatchClassEmailConfiguration> newBatchClassEmailConfigurationList = new ArrayList<BatchClassEmailConfiguration>();
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : batchClassEmailConfigurations) {
			newBatchClassEmailConfigurationList.add(batchClassEmailConfiguration);
			batchClassEmailConfiguration.setId(0);
			batchClassEmailConfiguration.setBatchClass(null);
		}
		batchClass.setEmailConfigurations(newBatchClassEmailConfigurationList);
	}

	public static void exportBatchClassField(BatchClass batchClass) {
		List<BatchClassField> batchClassField = batchClass.getBatchClassField();
		List<BatchClassField> newBatchClassFieldList = new ArrayList<BatchClassField>();
		for (BatchClassField batchClassFields : batchClassField) {
			newBatchClassFieldList.add(batchClassFields);
			batchClassFields.setId(0);
			batchClassFields.setBatchClass(null);
		}
		batchClass.setBatchClassField(newBatchClassFieldList);
	}

	public static void exportUserGroups(BatchClass batchClass) {
		List<BatchClassGroups> batchClassGroups = batchClass.getAssignedGroups();
		List<BatchClassGroups> newBatchClassGroupsList = new ArrayList<BatchClassGroups>();
		for (BatchClassGroups batchClassGroup : batchClassGroups) {
			newBatchClassGroupsList.add(batchClassGroup);
			batchClassGroup.setId(0);
			batchClassGroup.setBatchClass(null);
		}
		batchClass.setAssignedGroups(newBatchClassGroupsList);
		batchClass.setCurrentUser(null);
	}

	public static boolean matchBaseFolder(String uncFolderPath, String baseFolderPath) {
		boolean isUncPathStartsWithBaseFolder = false;

		// Following code commented as its no longer used in our code and it may cause ArrayOutOfBoundException
		// at line 1338 in some specific scenario while importing batch class.

		/*
		 * uncFolderPath = uncFolderPath.replace("\\", "/"); uncFolderPath = uncFolderPath.replace("\\\\", "/"); uncFolderPath =
		 * uncFolderPath.replace("//", "/");
		 * 
		 * baseFolderPath = baseFolderPath.replace("\\", "/"); baseFolderPath = baseFolderPath.replace("\\\\", "/"); baseFolderPath =
		 * baseFolderPath.replace("//", "/");
		 * 
		 * String[] uncFolderPathToken = uncFolderPath.split("\\/"); String[] baseFolderPathToken = baseFolderPath.split("\\/");
		 * 
		 * for (int index = 0; index < baseFolderPathToken.length; index++) { String userVal = uncFolderPathToken[index]; String
		 * baseVal = baseFolderPathToken[index]; if (!userVal.equals(baseVal)) { isUncPathStartsWithBaseFolder = false; break; } }
		 */
		return isUncPathStartsWithBaseFolder;
	}

	public static List<TableInfo> mapTableInputFromDTO(TableInfoDTO tableInfoDTO, String ruleIndex) {
		LOGGER.trace("Executing mapTableFromDTO method");
		List<TableInfo> tableInfoList = new ArrayList<TableInfo>();

		TableInfo tableInfo = new TableInfo();

		// Removed start pattern, end apttern and table extraction API from table info and moved it to table extraction rules.
		tableInfo.setRuleOperator(tableInfoDTO.getRuleOperator());
		tableInfo.setRemoveInvalidRows(tableInfoDTO.isRemoveInvalidRows());
		tableInfo.setName(tableInfoDTO.getName());
		tableInfo.setDisplayImage(tableInfoDTO.getDisplayImage());
		tableInfo.setNumberOfRows(Integer.parseInt(tableInfoDTO.getNumberOfRows()));
		// JIRA-11286
		tableInfo.setCurrencyCode(CurrencyCode.getCurrencyCodeForRepresentationValue(tableInfoDTO.getCurrencyCode()));

		for (TableColumnInfoDTO tableColumnsInfo : tableInfoDTO.getColumnInfoDTOs()) {
			TableColumnsInfo columnInfo = mapTableColumnFromDTO(tableInfoDTO, tableColumnsInfo);
			tableInfo.addTableColumnInfo(columnInfo);
		}

		for (RuleInfoDTO tableRuleInfo : tableInfoDTO.getRuleInfoDTOs()) {
			TableRuleInfo ruleInfo = mapRuleInputFromDTO(tableInfoDTO, tableRuleInfo);
			tableInfo.addTableRuleInfo(ruleInfo);
		}

		// Iterating over the list of table extraction rules DTOs and adding their objects to table info object.
		List<TableExtractionRuleDTO> listExtractionRuleDTOs = tableInfoDTO.getTableExtractionRuleDTOs();
		TableExtractionRuleDTO selectedTableExtractionRuleDTO = tableInfoDTO.getTableExtractionDTOByIdentifier(ruleIndex);
		if (null == selectedTableExtractionRuleDTO) {
			if (!CollectionUtil.isEmpty(listExtractionRuleDTOs)) {
				for (final TableExtractionRuleDTO tableExtractionRuleDTO : listExtractionRuleDTOs) {
					final TableExtractionRule tableExtractionRule = mapTableExtractionRuleInputFromDTO(tableExtractionRuleDTO);
					if (null != tableExtractionRule) {
						tableInfo.addTableExtractionRule(tableExtractionRule);
					}
				}
			}
		} else {
			final TableExtractionRule selectedtableExtractionRule = mapTableExtractionRuleInputFromDTO(selectedTableExtractionRuleDTO);
			if (null != selectedtableExtractionRule) {
				tableInfo.addTableExtractionRule(selectedtableExtractionRule);
			}
		}
		tableInfoList.add(tableInfo);
		LOGGER.trace("Executing mapTableFromDTO method, Returning Table Info List.");
		return tableInfoList;

	}

	public static TableRuleInfo mapRuleInputFromDTO(TableInfoDTO tableInfoDTO, RuleInfoDTO ruleInfoDTO) {
		// List<RuleInfoDTO> ruleInfoList = new ArrayList<RuleInfoDTO>();
		TableRuleInfo ruleInfo = new TableRuleInfo();
		ruleInfo.setRule(ruleInfoDTO.getRule());
		ruleInfo.setDescription(ruleInfoDTO.getDescription());
		return ruleInfo;

	}

	public static TableColumnsInfo mapTableColumnFromDTO(TableInfoDTO tableInfoDTO, TableColumnInfoDTO tableColumnsInfo) {
		TableColumnsInfo columnInfo = new TableColumnsInfo();

		// Column extarction information mopved to table column extraction rule and column ddescription is added to table column
		// information.
		columnInfo.setColumnName(tableColumnsInfo.getColumnName());
		columnInfo.setValidationPattern(tableColumnsInfo.getValidationPattern());
		columnInfo.setColumnDescription(tableColumnsInfo.getColumnDescription());
		columnInfo.setAlternateValues(tableColumnsInfo.getAlternateValues());
		return columnInfo;
	}

	public static void mapTestTableResultsToDTO(List<TestTableResultDTO> results, DataTable dataTable, String inputFileName) {
		if (results != null && dataTable != null) {
			TestTableResultDTO dto = new TestTableResultDTO();
			dto.setDataTable(dataTable);
			dto.setInputFileName(inputFileName);
			results.add(dto);
		}
	}

	public static void populateUICOnfig(BatchClass importedBatchClass, Node uiConfig, BatchClassService batchClassService,
			String workflowName, String zipSourcePath, BatchSchemaService batchSchemaService) {
		Label rootLabel = new Label(BatchConstants.ROOT, BatchConstants.ROOT, false);
		uiConfig.setLabel(rootLabel);
		List<BatchClassModule> moduleList = importedBatchClass.getBatchClassModules();
		if (!(moduleList == null || moduleList.isEmpty())) {
			final List<Node> rootUiConfigChildren = uiConfig.getChildren();
			Node node = new Node();
			Label lRole = node.getLabel();
			lRole.setDisplayName(BatchConstants.ROLES_DISPLAY_NAME);
			lRole.setKey(BatchConstants.ROLES);
			lRole.setMandatory(true);
			rootUiConfigChildren.add(node);
			node.setParent(uiConfig);

			Node nodeEmail = new Node();
			Label lEmail = nodeEmail.getLabel();
			lEmail.setDisplayName(BatchConstants.EMAIL_ACCOUNT_DISPLAY_NAME);
			lEmail.setKey(BatchConstants.EMAIL_ACCOUNTS);
			lEmail.setMandatory(true);
			rootUiConfigChildren.add(nodeEmail);
			nodeEmail.setParent(uiConfig);

			Node cmisNode = new Node();
			Label cmisLabel = cmisNode.getLabel();
			cmisLabel.setDisplayName(BatchConstants.CMIS_PLUGIN_DISPLAY_NAME);
			cmisLabel.setKey(BatchConstants.CMIS_PLUGIN);
			cmisLabel.setMandatory(true);
			rootUiConfigChildren.add(cmisNode);
			cmisNode.setParent(uiConfig);

			// Method for creating CMIS Export Tree.
			createCMISTree(cmisNode);

			Node nodeBatchClassDef = new Node();
			Label lDef = nodeBatchClassDef.getLabel();
			lDef.setDisplayName(BatchConstants.BATCH_CLASS_DEFI_DISPLAY_NAME);
			lDef.setKey(BatchConstants.BATCH_CLASS_DEF);
			lDef.setMandatory(true);
			rootUiConfigChildren.add(nodeBatchClassDef);
			nodeBatchClassDef.setParent(uiConfig);

			String scriptFolderPath = FileUtils.getFileNameOfTypeFromFolder(zipSourcePath, batchSchemaService.getScriptFolderName());
			String[] scriptsFileList = new File(scriptFolderPath).list(new CustomFileFilter(false,
					FileType.JAVA.getExtensionWithDot(), FileType.JAVA.getExtensionWithDot()));

			if (scriptsFileList != null && scriptsFileList.length > 0) {
				Node branch = new Node();
				Label labelBranch = branch.getLabel();
				labelBranch.setDisplayName(BatchConstants.SCRIPT_DISPLAY_NAME);
				labelBranch.setKey(batchSchemaService.getScriptFolderName());
				labelBranch.setMandatory(false);
				branch.setLabel(labelBranch);
				for (String scriptFileName : scriptsFileList) {
					Node leaf = new Node();
					Label leafLabel = leaf.getLabel();
					leafLabel.setDisplayName(scriptFileName);
					leafLabel.setKey(scriptFileName);
					leafLabel.setMandatory(false);
					leaf.setLabel(leafLabel);
					leaf.setParent(branch);
					branch.getChildren().add(leaf);
				}
				branch.setParent(nodeBatchClassDef);
				nodeBatchClassDef.getChildren().add(branch);
			}

			Node branchFolder = new Node();
			Label labelBranchFolder = branchFolder.getLabel();
			labelBranchFolder.setDisplayName(BatchConstants.FOLDER_LIST_DISPLAY_NAME);
			labelBranchFolder.setKey(BatchConstants.FOLDER_LIST);
			labelBranchFolder.setMandatory(true);
			branchFolder.setLabel(labelBranchFolder);
			branchFolder.setParent(nodeBatchClassDef);
			nodeBatchClassDef.getChildren().add(branchFolder);

			File[] listFiles = new File(zipSourcePath).listFiles();
			for (File file : listFiles) {
				if (file.isDirectory() && !file.getName().equalsIgnoreCase(batchSchemaService.getScriptFolderName())) {
					Node leaf = new Node();
					Label leafLabel = leaf.getLabel();
					if (file.getName().equals(batchSchemaService.getSearchSampleName())
							|| file.getName().equalsIgnoreCase(batchSchemaService.getImagemagickBaseFolderName())) {
						leafLabel.setMandatory(true);
					} else {
						leafLabel.setMandatory(false);
					}
					leafLabel.setDisplayName(file.getName());
					leafLabel.setKey(file.getName());
					leaf.setLabel(leafLabel);
					leaf.setParent(branchFolder);
					branchFolder.getChildren().add(leaf);
				}
			}

			Node branchBCM = new Node();
			Label labelBCM = branchBCM.getLabel();
			labelBCM.setDisplayName(BatchConstants.BATCH_CLASS_MODULES_DISPLAY_NAME);
			labelBCM.setKey(BatchConstants.BATCH_CLASS_MODULES);
			labelBCM.setMandatory(true);
			branchBCM.setLabel(labelBCM);
			branchBCM.setParent(nodeBatchClassDef);
			nodeBatchClassDef.getChildren().add(branchBCM);

			for (BatchClassModule module : moduleList) {
				Node leaf = new Node();
				Label leafLabel = leaf.getLabel();
				leafLabel.setDisplayName(module.getWorkflowName());
				leafLabel.setKey(module.getWorkflowName());
				leafLabel.setMandatory(true);
				leaf.setLabel(leafLabel);
				leaf.setParent(branchBCM);
				branchBCM.getChildren().add(leaf);
			}
		}
	}

	public static void mapTestTableResultsToDTO(List<TestTableResultDTO> results, List<TestTableResultDTO> noResultsDTOs,
			DataTable dataTable, String inputFileName) {
		if (results != null && dataTable != null) {
			TestTableResultDTO dto = new TestTableResultDTO();
			dto.setDataTable(dataTable);
			dto.setInputFileName(inputFileName);
			results.add(dto);
		} else {
			TestTableResultDTO dto = new TestTableResultDTO();
			dto.setInputFileName(inputFileName);
			noResultsDTOs.add(dto);
		}
	}

	public static List<OutputDataCarrierDTO> createOutputDataDTOFromOutputDataCarrier(List<OutputDataCarrier> outputDataCarriers,
			List<OutputDataCarrierDTO> carrierDtos, List<OutputDataCarrierDTO> carrierDTOsNoResult, String inputFileName) {
		if (outputDataCarriers != null && !outputDataCarriers.isEmpty()) {
			for (OutputDataCarrier outputDataCarrier : outputDataCarriers) {
				Coordinates hocr = outputDataCarrier.getSpan().getCoordinates();
				CoordinatesDTO coordinates = null;
				if (hocr != null) {
					coordinates = new CoordinatesDTO();
					coordinates.setX0(hocr.getX0());
					coordinates.setX1(hocr.getX1());
					coordinates.setY0(hocr.getY0());
					coordinates.setY1(hocr.getY1());
				}
				OutputDataCarrierDTO dto = new OutputDataCarrierDTO();
				dto.setConfidence(outputDataCarrier.getConfidence());
				dto.setCoordinates(coordinates);
				dto.setValue(outputDataCarrier.getValue());
				dto.setInputFileName(inputFileName);
				carrierDtos.add(dto);
			}
		} else {
			OutputDataCarrierDTO dto = new OutputDataCarrierDTO();
			dto.setInputFileName(inputFileName);
			carrierDTOsNoResult.add(dto);
		}
		return carrierDtos;
	}

	public static boolean isChecked(Node node) {
		boolean checked = false;
		if (node.getLabel().isMandatory()) {
			checked = true;
		}
		return checked;
	}

	public static void createSamplePatternDTO(SamplePatternDTO samplePatterns, Properties properties) {
		Set<Object> keys = properties.keySet();
		Map<String, String> patternValueVPatternDesc = new HashMap<String, String>();

		for (Iterator<Object> iterator = keys.iterator(); iterator.hasNext();) {
			String patternValue = (String) iterator.next();
			String patternDescription = properties.getProperty(patternValue);
			patternValueVPatternDesc.put(patternValue, patternDescription);
		}
		samplePatterns.setPatternValueMap(patternValueVPatternDesc);
	}

	public static void setBatchClassModuleDTOPluginConfigList(BatchClassPlugin batchClassPlugin,
			BatchClassPluginDTO batchClassPluginDTO, BatchClassPluginConfigService batchClassPluginConfigService,
			BatchClassPluginService batchClassPluginService, PluginConfigService pluginConfigService) {
		long batchClassPluginId = -1;
		LOGGER.info("Getting list of batch class plugins in the batch class module DTO");

		if (batchClassPluginDTO.isNew()) {

			String pluginIdentifier = batchClassPluginDTO.getPlugin().getIdentifier();
			List<BatchClassPluginConfig> batchClassPluginConfigsList = new ArrayList<BatchClassPluginConfig>(0);
			try {
				Long pluginId = Long.valueOf(pluginIdentifier);
				List<BatchClassPlugin> batchClassPlugins = batchClassPluginService.getBatchClassPluginForPluginId(pluginId);
				for (BatchClassPlugin batchClassPluginTemp : batchClassPlugins) {
					if (batchClassPluginTemp.getPlugin().getPluginName().equals(batchClassPluginDTO.getPlugin().getPluginName())) {
						batchClassPluginId = batchClassPluginTemp.getId();
						break;
					}

				}
			} catch (NumberFormatException e) {
				LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
			}
			if (batchClassPluginId != -1) {
				batchClassPluginConfigsList = batchClassPluginConfigService.getPluginConfigurationForPluginId(batchClassPluginId);

				if (batchClassPluginConfigsList != null) {
					for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigsList) {
						if (batchClassPlugin.getBatchClassPluginConfigByName(batchClassPluginConfig.getName()) == null) {
							batchClassPluginConfigService.evict(batchClassPluginConfig);
							BatchClassPluginConfig newBatchClassPluginConfig = getNewBatchClassPluginConfig(batchClassPluginConfig);
							batchClassPlugin.addBatchClassPluginConfig(newBatchClassPluginConfig);
						}
					}
				}

			}
			List<PluginConfig> pluginConfigs = pluginConfigService.getPluginConfigForPluginId(pluginIdentifier);

			if (pluginConfigs != null && !pluginConfigs.isEmpty()) {
				for (PluginConfig pluginConfig : pluginConfigs) {
					if (batchClassPlugin.getBatchClassPluginConfigByName(pluginConfig.getName()) == null) {
						BatchClassPluginConfig batchClassPluginConfig = new BatchClassPluginConfig();
						batchClassPluginConfig.setPluginConfig(pluginConfig);
						batchClassPluginConfig.setId(0);
						setDefaultValueForNewConfig(batchClassPluginConfig);
						batchClassPluginConfig.setDescription(pluginConfig.getDescription());
						batchClassPluginConfig.setName(pluginConfig.getName());
						batchClassPlugin.addBatchClassPluginConfig(batchClassPluginConfig);
					}
				}
			}
		}
	}

	/**
	 * Returns a new plugin configuration.
	 * 
	 * @param batchClassPluginConfig {@link BatchClassPluginConfig} whose configuration values is to be used in new plugin config
	 *            created by this API.
	 * @return newly created {@link BatchClassPluginConfig}.
	 */
	private static BatchClassPluginConfig getNewBatchClassPluginConfig(final BatchClassPluginConfig batchClassPluginConfig) {
		BatchClassPluginConfig newBatchClassPluginConfig = new BatchClassPluginConfig();
		PluginConfig pluginConfig = batchClassPluginConfig.getPluginConfig();
		newBatchClassPluginConfig.setPluginConfig(pluginConfig);
		newBatchClassPluginConfig.setId(0);
		newBatchClassPluginConfig.getSampleValue().addAll(batchClassPluginConfig.getSampleValue());
		newBatchClassPluginConfig.setDescription(pluginConfig.getDescription());
		newBatchClassPluginConfig.setName(pluginConfig.getName());
		newBatchClassPluginConfig.setValue(batchClassPluginConfig.getValue());
		return newBatchClassPluginConfig;
	}

	private static void setDefaultValueForNewConfig(BatchClassPluginConfig batchClassPluginConfig) {
		DataType pluginConfigDataType = batchClassPluginConfig.getPluginConfig().getDataType();
		if (pluginConfigDataType == DataType.BOOLEAN) {
			batchClassPluginConfig.setValue(BatchConstants.YES);
		} else {
			boolean isMandatory = batchClassPluginConfig.getPluginConfig().isMandatory();
			if (pluginConfigDataType == DataType.STRING && isMandatory) {
				List<String> sampleValues = batchClassPluginConfig.getSampleValue();
				if (sampleValues == null || sampleValues.isEmpty()) {
					batchClassPluginConfig.setValue(BatchConstants.DEFAULT);
				} else if (sampleValues.size() > 0) {
					batchClassPluginConfig.setValue(sampleValues.get(0));
				}
			} else if (pluginConfigDataType == DataType.INTEGER && isMandatory) {
				batchClassPluginConfig.setValue(BatchConstants.ZERO);
			}
		}
	}

	public static DependencyDTO createDependencyDTO(Dependency dependency, PluginService pluginService) {
		DependencyDTO dependencyDTO = null;

		if (dependency.getDependencyType() == DependencyTypeProperty.ORDER_BEFORE) {
			final String dependenciesInName = changeDependenciesIdentifierToName(dependency.getDependencies(), pluginService);
			if (!dependenciesInName.isEmpty()) {
				dependencyDTO = new DependencyDTO();
				dependencyDTO.setDependencies(dependenciesInName);
			}
		} else {
			dependencyDTO = new DependencyDTO();
			dependencyDTO.setDependencies(BatchConstants.EMPTY_STRING);

		}
		if (dependencyDTO != null) {
			dependencyDTO.setIdentifier(String.valueOf(dependency.getId()));
			dependencyDTO.setDependencyType(dependency.getDependencyType().getProperty());
		}
		return dependencyDTO;

	}

	private static String changeDependenciesIdentifierToName(String dependencyNames, PluginService pluginService) {

		String[] andDependencies = dependencyNames.split(BatchConstants.CONDITION_AND);
		StringBuffer andDependenciesNameAsString = new StringBuffer();

		for (String andDependency : andDependencies) {

			if (!andDependenciesNameAsString.toString().isEmpty()) {
				andDependenciesNameAsString.append(BatchConstants.CONDITION_AND);
			}

			String[] orDependencies = andDependency.split(BatchConstants.CONDITION_OR);
			StringBuffer orDependenciesNameAsString = new StringBuffer();

			for (String dependencyIdentifier : orDependencies) {
				try {
					long dependencyId = Long.valueOf(dependencyIdentifier);
					final Plugin plugin = pluginService.getPluginPropertiesForPluginId(dependencyId);
					if (plugin != null) {
						if (!orDependenciesNameAsString.toString().isEmpty()) {
							orDependenciesNameAsString.append(BatchConstants.CONDITION_OR);
						}
						orDependenciesNameAsString.append(plugin.getPluginName());
					}
				} catch (NumberFormatException e) {
					LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
				}
			}

			andDependenciesNameAsString.append(orDependenciesNameAsString);
			orDependenciesNameAsString = new StringBuffer();
		}
		return andDependenciesNameAsString.toString();
	}

	private static String changeDependenciesNameToIdentifier(String dependencyNames, PluginService pluginService) {

		String[] andDependencies = dependencyNames.split(BatchConstants.CONDITION_AND);
		StringBuffer andDependenciesNameAsString = new StringBuffer();

		for (String andDependency : andDependencies) {

			if (!andDependenciesNameAsString.toString().isEmpty()) {
				andDependenciesNameAsString.append(BatchConstants.CONDITION_AND);
			}

			String[] orDependencies = andDependency.split(BatchConstants.CONDITION_OR);
			StringBuffer orDependenciesNameAsString = new StringBuffer();

			for (String dependencyName : orDependencies) {
				final Plugin plugin = pluginService.getPluginPropertiesForPluginName(dependencyName);

				if (plugin != null) {
					if (!orDependenciesNameAsString.toString().isEmpty()) {
						orDependenciesNameAsString.append(BatchConstants.CONDITION_OR);
					}
					orDependenciesNameAsString.append(plugin.getId());
				}

			}

			andDependenciesNameAsString.append(orDependenciesNameAsString);
			orDependenciesNameAsString = new StringBuffer();
		}
		return andDependenciesNameAsString.toString();
	}

	private static void mergeDependencyFromDTO(Dependency dependency, DependencyDTO dependencyDTO, PluginService pluginService) {
		if (dependency != null) {

			try {
				long dependencyId = Long.valueOf(dependencyDTO.getIdentifier());
				dependency.setId(dependencyId);
			} catch (NumberFormatException e) {
				LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
			}
			dependency.setDependencyType(getDependencyTypePropertyFromValue(dependencyDTO.getDependencyType()));
			if (!dependencyDTO.getDependencyType().equals(DependencyTypeProperty.UNIQUE.getProperty())) {
				dependency.setDependencies(changeDependenciesNameToIdentifier(dependencyDTO.getDependencies(), pluginService));
			} else {
				dependency.setDependencies(BatchConstants.EMPTY_STRING);
			}
		}
	}

	public static DependencyTypeProperty getDependencyTypePropertyFromValue(String dependencyTypeProperty) {
		DependencyTypeProperty dependencyType = null;
		for (DependencyTypeProperty property : DependencyTypeProperty.valuesAsList()) {

			if (property.getProperty().equals(dependencyTypeProperty)) {
				dependencyType = property;
				break;
			}
		}
		return dependencyType;
	}

	public static void mergePluginFromDTO(Plugin plugin, PluginDetailsDTO pluginDetailsDTO, PluginService pluginService) {
		// plugin.setDescription(pluginDetailsDTO.getPluginDescription());

		List<Dependency> dependenciesList = plugin.getDependencies();

		if (dependenciesList == null) {
			dependenciesList = new ArrayList<Dependency>();
		}
		List<DependencyDTO> removedDependencyDTOs = null;
		for (DependencyDTO dependencyDTO : pluginDetailsDTO.getDependencies()) {
			if (dependencyDTO.isNew() && !dependencyDTO.isDeleted()) {
				Dependency dependency = new Dependency();
				mergeDependencyFromDTO(dependency, dependencyDTO, pluginService);
				dependency.setPlugin(plugin);
				dependenciesList.add(dependency);
			} else {

				if (dependencyDTO.isDeleted()) {
					if (dependencyDTO.isNew()) {
						if (removedDependencyDTOs == null) {
							removedDependencyDTOs = new ArrayList<DependencyDTO>();
						}
						removedDependencyDTOs.add(dependencyDTO);
					} else {
						long dependencyIdentifier = Long.valueOf(dependencyDTO.getIdentifier());
						Dependency dependency = plugin.getDependencyById(dependencyIdentifier);
						plugin.getDependencies().remove(dependency);
						dependency.setPlugin(null);
					}
				} else if (dependencyDTO.isDirty()) {

					try {
						long dependencyId = Long.valueOf(dependencyDTO.getIdentifier());
						Dependency dependency = plugin.getDependencyById(dependencyId);
						mergeDependencyFromDTO(dependency, dependencyDTO, pluginService);
					} catch (NumberFormatException e) {
						LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
					}
				}
			}
		}
		if (removedDependencyDTOs != null) {
			pluginDetailsDTO.getDependencies().removeAll(removedDependencyDTOs);
		}
		plugin.setDependencies(dependenciesList);
	}

	/**
	 * @param batchClassPluginConfig
	 * @return
	 */
	private static PluginConfig mergePluginConfiguration(BatchClassPluginConfigDTO batchClassPluginConfigDTO) {
		PluginConfig pluginConfig = new PluginConfig();

		PluginConfigurationDTO pluginConfigDTO = batchClassPluginConfigDTO.getPluginConfig();
		if (pluginConfigDTO != null) {
			pluginConfig.setName(pluginConfigDTO.getFieldName());

			try {
				long pluginConfigDtoId = Long.valueOf(pluginConfigDTO.getIdentifier());
				pluginConfig.setId(pluginConfigDtoId);
			} catch (NumberFormatException e) {
				LOGGER.error(BatchConstants.ERROR_CONVERT_NUMBER + e.getMessage());
			}
		}

		return pluginConfig;
	}

	public static void mergeModuleFromDTO(Module module, ModuleDTO moduleDTO) {
		module.setDescription(moduleDTO.getDescription());
		module.setName(moduleDTO.getName());
		module.setVersion(BatchConstants.VERSION);
	}

	public static BatchClassDTO converToDTO(final BatchClass batchClass) {
		BatchClassDTO batchClassDTO = new BatchClassDTO();
		batchClassDTO.setIdentifier(batchClass.getIdentifier());
		batchClassDTO.setDescription(batchClass.getDescription());
		batchClassDTO.setName(batchClass.getName());
		batchClassDTO.setPriority(String.valueOf(batchClass.getPriority()));
		batchClassDTO.setUncFolder(batchClass.getUncFolder());
		batchClassDTO.setVersion(batchClass.getVersion());
		return batchClassDTO;
	}

	public static void decryptAllPasswords(BatchClass batchClass) {

		// Decrypt plugin passwords
		decryptPluginPasswords(batchClass.getBatchClassPluginsByDataType(DataType.PASSWORD));
		// Decrypt CMIS import repositories password
		decryptCMISPasswords(batchClass.getCmisConfigurations());
		// Decrypt Email import passwords
		decryptMailPasswords(batchClass.getEmailConfigurations());

	}

	private static void decryptMailPasswords(List<BatchClassEmailConfiguration> allEmailConfigs) {
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : allEmailConfigs) {
			String mailPassword = batchClassEmailConfiguration.getPassword();
			mailPassword = EphesoftEncryptionUtil.getDecryptedPasswordString(mailPassword);
			batchClassEmailConfiguration.setPassword(mailPassword);
		}
	}

	private static void decryptCMISPasswords(List<BatchClassCmisConfiguration> allCmisConfigs) {
		for (BatchClassCmisConfiguration batchClassCmisConfiguration : allCmisConfigs) {
			String cmisPassword = batchClassCmisConfiguration.getPassword();
			cmisPassword = EphesoftEncryptionUtil.getDecryptedPasswordString(cmisPassword);
			batchClassCmisConfiguration.setPassword(cmisPassword);
		}
	}

	private static void decryptPluginPasswords(List<BatchClassPluginConfig> passwordTypePluginConfigs) {
		for (BatchClassPluginConfig batchClassPluginConfig : passwordTypePluginConfigs) {
			String pluginPassword = batchClassPluginConfig.getValue();
			pluginPassword = EphesoftEncryptionUtil.getDecryptedPasswordString(pluginPassword);
			batchClassPluginConfig.setValue(pluginPassword);
		}
	}

	public static void exportCMISConfiguration(BatchClass batchClass) {
		List<BatchClassCmisConfiguration> batchClassCMISConfigurations = batchClass.getCmisConfigurations();
		List<BatchClassCmisConfiguration> exportedBatchClassCMISConfigurations = new ArrayList<BatchClassCmisConfiguration>();

		for (BatchClassCmisConfiguration batchClassCmisConfiguration : batchClassCMISConfigurations) {
			if (batchClassCmisConfiguration != null) {
				exportedBatchClassCMISConfigurations.add(batchClassCmisConfiguration);
				batchClassCmisConfiguration.setId(0);
				batchClassCmisConfiguration.setBatchClass(null);
			}
		}
	}

	/**
	 * Converts the LockStatus object to LockStatusDTO object.
	 * 
	 * @param lockStatus {@link LockStatus} The instance of lockStatus.
	 * @return {@link LockStatusDTO} The instance of lock status DTO Object.
	 */
	public static LockStatusDTO mapLockStatusToLockStatusDTO(final LockStatus lockStatus) {
		LockStatusDTO lockStatusDTO = null;
		if (lockStatus != null) {
			lockStatusDTO = new LockStatusDTO();
			lockStatusDTO.setFeatureName(lockStatus.getFeatureName());
			lockStatusDTO.setLocked(lockStatus.isLocked());
			lockStatusDTO.setUserName(lockStatus.getUserName());
			lockStatusDTO.setId(lockStatus.getId());
		}
		return lockStatusDTO;
	}

	/**
	 * Copies the table rule information to a given table.
	 * 
	 * @param tableInfo {@link TableInfo} the table to which rule info is added
	 */
	private static void copyTableRuleInfo(TableInfo tableInfo) {
		List<TableRuleInfo> tableRuleInfos = tableInfo.getTableRuleInfo();
		List<TableRuleInfo> newTableRuleInfo = new ArrayList<TableRuleInfo>();
		for (TableRuleInfo tableRuleInfo : tableRuleInfos) {
			newTableRuleInfo.add(tableRuleInfo);
			tableRuleInfo.setId(0);
		}
		tableInfo.setTableRuleInfo(newTableRuleInfo);
	}

	/**
	 * Creates table information DTOs for the given document type and table info object.
	 * 
	 * @param documentTypeDTO {@link DocumentTypeDTO} the document type dto.
	 * @param tableInfo {@link TableInfo} the table information object.
	 * @param includeAlternateValues {@link boolean} states whether alternate values will be included or not.
	 * @return {@link TableInfoDTO} a dto containing the table information.
	 */
	public static TableInfoDTO createTableInfoDTO(final DocumentTypeDTO documentTypeDTO, final TableInfo tableInfo,
			final boolean includeAlternateValues) {
		TableInfoDTO tableInfoDTO = new TableInfoDTO();

		tableInfoDTO.setDocTypeDTO(documentTypeDTO);

		// Removed start pattern, end apttern and table extraction API from table info and moved it to table extraction rules.
		if (tableInfo.getRuleOperator() != null) {
			tableInfoDTO.setRuleOperator(tableInfo.getRuleOperator());
		}
		tableInfoDTO.setName(tableInfo.getName());
		tableInfoDTO.setIdentifier(String.valueOf(tableInfo.getId()));
		tableInfoDTO.setRemoveInvalidRows(tableInfo.isRemoveInvalidRows());
		tableInfoDTO.setDisplayImage(tableInfo.getDisplayImage());
		tableInfoDTO.setNumberOfRows(String.valueOf(tableInfo.getNumberOfRows()));

		for (TableColumnsInfo tableColumnsInfo : tableInfo.getTableColumnsInfo()) {
			TableColumnInfoDTO columnInfoDTO = createTableColumnInfoDTO(tableInfoDTO, tableColumnsInfo, includeAlternateValues);
			tableInfoDTO.addColumnInfo(columnInfoDTO);
		}
		if (tableInfo.getTableRuleInfo() != null) {
			for (TableRuleInfo tableRuleInfo : tableInfo.getTableRuleInfo()) {
				if (tableRuleInfo != null) {
					RuleInfoDTO ruleInfoDTO = createRuleInfoDTO(tableInfoDTO, tableRuleInfo);
					tableInfoDTO.addRuleInfo(ruleInfoDTO);
				}

			}
		}

		// Iterating over the list of table extraction rules and adding their DTOs to table info DTO.
		final List<TableExtractionRule> listTableExtractionRules = tableInfo.getTableExtractionRules();
		if (!CollectionUtil.isEmpty(listTableExtractionRules)) {
			for (TableExtractionRule tableExtractionRule : listTableExtractionRules) {
				if (tableExtractionRule != null) {
					TableExtractionRuleDTO tableExtractionRuleDTO = createTableExtractionRuleDTO(tableInfoDTO, tableExtractionRule);
					if (null != tableExtractionRuleDTO) {
						tableInfoDTO.addTableExtractionRule(tableExtractionRuleDTO);
					}
				}

			}
		}

		return tableInfoDTO;
	}

	/**
	 * Creates table column information DTO for the given table info dto and table column info object.
	 * 
	 * @param tableInfoDTO {@link TableInfoDTO} the table info dto for which the column dto is created.
	 * @param tableColumnsInfo {@link TableColumnsInfo} the table column info domain object whose dto will be created.
	 * @param includeAlternateValues {@link boolean} states whether alternate values will be included or not.
	 * @return {@link TableColumnInfoDTO} a dto containing the table column information.
	 */
	public static TableColumnInfoDTO createTableColumnInfoDTO(final TableInfoDTO tableInfoDTO,
			final TableColumnsInfo tableColumnsInfo, final boolean includeAlternateValues) {
		TableColumnInfoDTO columnInfoDTO = new TableColumnInfoDTO();

		// Column extraction information mopved to table column extraction rule and column ddescription is added to table column
		// information.
		columnInfoDTO.setColumnName(tableColumnsInfo.getColumnName());
		columnInfoDTO.setValidationPattern(tableColumnsInfo.getValidationPattern());
		columnInfoDTO.setIdentifier(String.valueOf(tableColumnsInfo.getId()));
		columnInfoDTO.setAlternateValues(tableColumnsInfo.getAlternateValues());
		columnInfoDTO.setColumnDescription(tableColumnsInfo.getColumnDescription());
		columnInfoDTO.setTableInfoDTO(tableInfoDTO);
		return columnInfoDTO;
	}

	/**
	 * Copies table extraction rule from table info to a new table info object.
	 * 
	 * @param tableInfo {@link TableInfoDTO}
	 */
	private static void copyTableExtractionRule(final TableInfo tableInfo) {
		List<TableExtractionRule> newTableExtractionRules = null;
		final List<TableExtractionRule> tableExtractionRules = tableInfo.getTableExtractionRules();
		if (!CollectionUtil.isEmpty(tableExtractionRules)) {
			newTableExtractionRules = new ArrayList<TableExtractionRule>(tableExtractionRules.size());
			for (TableExtractionRule tableExtractionRule : tableExtractionRules) {
				if (null != tableExtractionRule) {
					newTableExtractionRules.add(tableExtractionRule);
					tableExtractionRule.setId(0);
					copyTableColumnExtractionRule(tableExtractionRule);
				}
			}
		}
		tableInfo.setTableExtractionRules(newTableExtractionRules);
	}

	/**
	 * Copies table extraction rule from table info to a new table info object.
	 * 
	 * @param tableInfo {@link TableInfoDTO}
	 */
	private static void copyTableColumnExtractionRule(final TableExtractionRule tableExtractionRule) {
		List<TableColumnExtractionRule> newTableColumnExtractionRules = null;
		final List<TableColumnExtractionRule> tableColumnExtractionRules = tableExtractionRule.getTableColumnExtractionRules();
		if (!CollectionUtil.isEmpty(tableColumnExtractionRules)) {
			newTableColumnExtractionRules = new ArrayList<TableColumnExtractionRule>(tableColumnExtractionRules.size());
			for (TableColumnExtractionRule tableColumnExtractionRule : tableColumnExtractionRules) {
				if (null != tableColumnExtractionRule) {
					newTableColumnExtractionRules.add(tableColumnExtractionRule);
					tableColumnExtractionRule.setId(0);
				}
			}
		}
		tableExtractionRule.setTableColumnExtractionRules(newTableColumnExtractionRules);
	}

	/**
	 * Merges table extraction rules from their DTOs.
	 * 
	 * @param tableExtractionRule {@link TableExtractionRule}
	 * @param tableExtractionRuleDTO {@link TableExtractionRuleDTO}
	 */
	public static void mergeTableExtractionRuleFromDTO(final TableExtractionRule tableExtractionRule,
			final TableExtractionRuleDTO tableExtractionRuleDTO) {
		if (null != tableExtractionRule && null != tableExtractionRuleDTO) {
			tableExtractionRule.setTableAPI(tableExtractionRuleDTO.getTableAPI());
			tableExtractionRule.setStartPattern(tableExtractionRuleDTO.getStartPattern());
			tableExtractionRule.setEndPattern(tableExtractionRuleDTO.getEndPattern());
			tableExtractionRule.setRuleName(tableExtractionRuleDTO.getRuleName());
			addTableColumnExtractionRuleToTable(tableExtractionRuleDTO.getTableColumnExtractionRuleDTOs(true), tableExtractionRule);
		}
	}

	/**
	 * Populates table column extraction rules information taken from their DTOs and added to table extraction rule.
	 * 
	 * @param tableExtractionRule {@link List{@link TableColumnExtractionRuleDTO}
	 * @param tableExtractionRuleDTO {@link TableExtractionRule}
	 */
	private static void addTableColumnExtractionRuleToTable(final List<TableColumnExtractionRuleDTO> listColumnExtractionRuleDTOs,
			final TableExtractionRule tableExtractionRule) {
		if (tableExtractionRule != null) {
			List<TableColumnExtractionRule> listColumnExtractionRules = tableExtractionRule.getTableColumnExtractionRules();
			TableColumnExtractionRule tableColumnExtractionRule = null;
			if (listColumnExtractionRules != null) {
				for (TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO : listColumnExtractionRuleDTOs) {
					if (null != tableColumnExtractionRuleDTO) {
						String identifier = tableColumnExtractionRuleDTO.getIdentifier();
						tableColumnExtractionRule = tableExtractionRule.getTableExtractionRuleByIdentifier(identifier);
						if (tableColumnExtractionRuleDTO.isDeleted()) {
							if (tableColumnExtractionRule != null) {
								listColumnExtractionRules.remove(tableColumnExtractionRule);
							}
						} else {
							if (tableColumnExtractionRuleDTO.isNew()) {
								tableColumnExtractionRule = new TableColumnExtractionRule();
								tableColumnExtractionRuleDTO.setNew(false);
								listColumnExtractionRules.add(tableColumnExtractionRule);
							}
							mergeTableColumnExtractionRuleFromDTO(tableColumnExtractionRuleDTO, tableColumnExtractionRule);
						}
					}
				}
			}
		}
	}

	/**
	 * Merges table column extraction rule information from their DTOs.
	 * 
	 * @param tableColumnExtractionRuleDTO {@link TableColumnExtractionRuleDTO}
	 * @param tableColumnExtractionRule {@link TableColumnExtractionRule}
	 */
	private static void mergeTableColumnExtractionRuleFromDTO(TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO,
			TableColumnExtractionRule tableColumnExtractionRule) {
		if (null != tableColumnExtractionRuleDTO && null != tableColumnExtractionRule) {
			tableColumnExtractionRule.setBetweenLeft(tableColumnExtractionRuleDTO.getBetweenLeft());
			tableColumnExtractionRule.setBetweenRight(tableColumnExtractionRuleDTO.getBetweenRight());
			tableColumnExtractionRule.setColumnPattern(tableColumnExtractionRuleDTO.getColumnPattern());
			tableColumnExtractionRule.setExtractedDataColumnName(tableColumnExtractionRuleDTO.getExtractedDataColumnName());
			tableColumnExtractionRule.setRequired(tableColumnExtractionRuleDTO.isRequired());
			tableColumnExtractionRule.setMandatory(tableColumnExtractionRuleDTO.isMandatory());
			tableColumnExtractionRule.setCurrency(tableColumnExtractionRuleDTO.isCurrency());
			tableColumnExtractionRule.setColumnHeaderPattern(tableColumnExtractionRuleDTO.getColumnHeaderPattern());
			tableColumnExtractionRule.setColumnName(tableColumnExtractionRuleDTO.getColumnName());
			if (tableColumnExtractionRuleDTO.getColumnStartCoordinate() != null
					&& !tableColumnExtractionRuleDTO.getColumnStartCoordinate().isEmpty()) {
				tableColumnExtractionRule.setColumnStartCoordinate(Integer.parseInt(tableColumnExtractionRuleDTO
						.getColumnStartCoordinate()));
			} else {
				tableColumnExtractionRule.setColumnStartCoordinate(null);
			}
			if (tableColumnExtractionRuleDTO.getColumnEndCoordinate() != null
					&& !tableColumnExtractionRuleDTO.getColumnEndCoordinate().isEmpty()) {
				tableColumnExtractionRule.setColumnEndCoordinate(Integer.parseInt(tableColumnExtractionRuleDTO
						.getColumnEndCoordinate()));
			} else {
				tableColumnExtractionRule.setColumnEndCoordinate(null);
			}
			if (tableColumnExtractionRuleDTO.getColumnCoordY0() != null && !tableColumnExtractionRuleDTO.getColumnCoordY0().isEmpty()) {
				tableColumnExtractionRule.setColumnCoordinateY0(Integer.parseInt(tableColumnExtractionRuleDTO.getColumnCoordY0()));
			} else {
				tableColumnExtractionRule.setColumnCoordinateY0(null);
			}
			if (tableColumnExtractionRuleDTO.getColumnCoordY1() != null && !tableColumnExtractionRuleDTO.getColumnCoordY1().isEmpty()) {
				tableColumnExtractionRule.setColumnCoordinateY1(Integer.parseInt(tableColumnExtractionRuleDTO.getColumnCoordY1()));
			} else {
				tableColumnExtractionRule.setColumnCoordinateY1(null);
			}
		}
	}

	/**
	 * Maps a table extarction rule DTO to table extraction rule object.
	 * 
	 * @param tableExtractionRuleDTO {@link TableExtractionRuleDTO}
	 * @return {@link TableExtractionRule}
	 */
	private static TableExtractionRule mapTableExtractionRuleInputFromDTO(final TableExtractionRuleDTO tableExtractionRuleDTO) {
		TableExtractionRule tableExtractionRule = new TableExtractionRule();
		tableExtractionRule.setTableAPI(tableExtractionRuleDTO.getTableAPI());
		tableExtractionRule.setStartPattern(tableExtractionRuleDTO.getStartPattern());
		tableExtractionRule.setEndPattern(tableExtractionRuleDTO.getEndPattern());
		tableExtractionRule.setRuleName(tableExtractionRuleDTO.getRuleName());
		List<TableColumnExtractionRuleDTO> tableColumnExtractionRuleDTOs = tableExtractionRuleDTO
				.getTableColumnExtractionRuleDTOs(false);
		if (null != tableColumnExtractionRuleDTOs) {
			for (TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO : tableColumnExtractionRuleDTOs) {
				TableColumnExtractionRule columnExtractionRule = mapTableColumnExtractionRuleFromDTO(tableColumnExtractionRuleDTO);
				if (null != columnExtractionRule) {
					tableExtractionRule.addTableColumnExtractionRule(columnExtractionRule);
				}
			}
		}
		return tableExtractionRule;
	}

	/**
	 * Maps a table column extarction rule DTO to table column extraction rule object.
	 * 
	 * @param tableExtractionRuleDTO {@link TableColumnExtractionRuleDTO}
	 * @return {@link TableColumnExtractionRule}
	 */
	private static TableColumnExtractionRule mapTableColumnExtractionRuleFromDTO(
			TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO) {
		TableColumnExtractionRule tableColumnExtractionRule = null;
		if (null != tableColumnExtractionRuleDTO) {
			tableColumnExtractionRule = new TableColumnExtractionRule();
			tableColumnExtractionRule.setColumnName(tableColumnExtractionRuleDTO.getColumnName());
			tableColumnExtractionRule.setBetweenLeft(tableColumnExtractionRuleDTO.getBetweenLeft());
			tableColumnExtractionRule.setBetweenRight(tableColumnExtractionRuleDTO.getBetweenRight());
			tableColumnExtractionRule.setColumnPattern(tableColumnExtractionRuleDTO.getColumnPattern());
			tableColumnExtractionRule.setColumnHeaderPattern(tableColumnExtractionRuleDTO.getColumnHeaderPattern());
			tableColumnExtractionRule.setExtractedDataColumnName(tableColumnExtractionRuleDTO.getExtractedDataColumnName());
			tableColumnExtractionRule.setRequired(tableColumnExtractionRuleDTO.isRequired());
			tableColumnExtractionRule.setMandatory(tableColumnExtractionRuleDTO.isMandatory());
			tableColumnExtractionRule.setCurrency(tableColumnExtractionRuleDTO.isCurrency());
			String columnStartCoordinate = tableColumnExtractionRuleDTO.getColumnStartCoordinate();
			if (null != columnStartCoordinate && NumberUtils.isDigits(columnStartCoordinate)) {
				tableColumnExtractionRule.setColumnStartCoordinate(Integer.parseInt(columnStartCoordinate));
			} else {
				tableColumnExtractionRule.setColumnStartCoordinate(null);
			}
			String columnEndCoordinate = tableColumnExtractionRuleDTO.getColumnEndCoordinate();
			if (null != columnEndCoordinate && NumberUtils.isDigits(columnEndCoordinate)) {
				tableColumnExtractionRule.setColumnEndCoordinate(Integer.parseInt(columnEndCoordinate));
			} else {
				tableColumnExtractionRule.setColumnEndCoordinate(null);
			}
			String columnCoordinateY0 = tableColumnExtractionRuleDTO.getColumnCoordY0();
			if (null != columnCoordinateY0 && NumberUtils.isDigits(columnCoordinateY0)) {
				tableColumnExtractionRule.setColumnCoordinateY0(Integer.parseInt(columnCoordinateY0));
			} else {
				tableColumnExtractionRule.setColumnCoordinateY0(null);
			}
			String columnCoordinateY1 = tableColumnExtractionRuleDTO.getColumnCoordY1();
			if (null != columnCoordinateY1 && NumberUtils.isDigits(columnCoordinateY1)) {
				tableColumnExtractionRule.setColumnCoordinateY1(Integer.parseInt(columnCoordinateY1));
			} else {
				tableColumnExtractionRule.setColumnCoordinateY1(null);
			}
		}
		return tableColumnExtractionRule;
	}

	/**
	 * Creates table column extraction rule DTO from table's column extraction rule.
	 * 
	 * @param tableInfoDTO {@link TableInfoDTO}
	 * @param tableExtractionRule {@link TableExtractionRule}
	 * @return {@link TableExtractionRuleDTO}
	 */
	private static TableExtractionRuleDTO createTableExtractionRuleDTO(final TableInfoDTO tableInfoDTO,
			final TableExtractionRule tableExtractionRule) {
		TableExtractionRuleDTO tableExtractionRuleDTO = null;
		if (tableExtractionRule != null) {
			tableExtractionRuleDTO = new TableExtractionRuleDTO();
			tableExtractionRuleDTO.setIdentifier(String.valueOf(tableExtractionRule.getId()));
			tableExtractionRuleDTO.setTableAPI(tableExtractionRule.getTableAPI());
			tableExtractionRuleDTO.setStartPattern(tableExtractionRule.getStartPattern());
			tableExtractionRuleDTO.setEndPattern(tableExtractionRule.getEndPattern());
			tableExtractionRuleDTO.setRuleName(tableExtractionRule.getRuleName());
			tableExtractionRuleDTO.setTableInfoDTO(tableInfoDTO);
			tableExtractionRuleDTO.setTableColumnExtractionRuleDTOs(createTableColumnExtractionRuleDTOList(
					tableExtractionRule.getTableColumnExtractionRules(), tableExtractionRuleDTO));
		}
		return tableExtractionRuleDTO;
	}

	/**
	 * Creates table column extraction rule DTOs from table's column extraction rules and attaches to its parent table extraction rule.
	 * 
	 * @param listTableColumnExtractionRule {@link List<{@link TableColumnExtractionRule}>}
	 * @param tableExtractionRuleDTO {@link TableExtractionRuleDTO}
	 * @return {@link List<{@link TableColumnExtractionRuleDTO}>}
	 */
	private static List<TableColumnExtractionRuleDTO> createTableColumnExtractionRuleDTOList(
			final List<TableColumnExtractionRule> listTableColumnExtractionRule, TableExtractionRuleDTO tableExtractionRuleDTO) {
		List<TableColumnExtractionRuleDTO> tableColumnExtractionRuleDTOs = null;
		if (!CollectionUtil.isEmpty(listTableColumnExtractionRule)) {
			tableColumnExtractionRuleDTOs = new ArrayList<TableColumnExtractionRuleDTO>(listTableColumnExtractionRule.size());
			for (TableColumnExtractionRule tableColumnExtractionRule : listTableColumnExtractionRule) {
				TableColumnExtractionRuleDTO newTableColumnExtractionRuleDTO = createTableColumnExtractionRuleDTO(
						tableColumnExtractionRule, tableExtractionRuleDTO);
				if (null != newTableColumnExtractionRuleDTO) {
					tableColumnExtractionRuleDTOs.add(newTableColumnExtractionRuleDTO);
				}
			}
		}
		return tableColumnExtractionRuleDTOs;
	}

	/**
	 * Creates table column extraction rules DTO from table's column extraction rule.
	 * 
	 * @param tableColumnExtractionRule {@link TableColumnExtractionRule}
	 * @param tableExtractionRuleDTO {@link TableExtractionRuleDTO}
	 * @return {@link TableColumnExtractionRuleDTO}
	 */
	private static TableColumnExtractionRuleDTO createTableColumnExtractionRuleDTO(
			final TableColumnExtractionRule tableColumnExtractionRule, TableExtractionRuleDTO tableExtractionRuleDTO) {
		TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO = null;
		if (null != tableColumnExtractionRule) {
			tableColumnExtractionRuleDTO = new TableColumnExtractionRuleDTO();
			tableColumnExtractionRuleDTO.setBetweenLeft(tableColumnExtractionRule.getBetweenLeft());
			tableColumnExtractionRuleDTO.setBetweenRight(tableColumnExtractionRule.getBetweenRight());
			tableColumnExtractionRuleDTO.setColumnPattern(tableColumnExtractionRule.getColumnPattern());
			tableColumnExtractionRuleDTO.setExtractedDataColumnName(tableColumnExtractionRule.getExtractedDataColumnName());
			tableColumnExtractionRuleDTO.setRequired(tableColumnExtractionRule.isRequired());
			tableColumnExtractionRuleDTO.setMandatory(tableColumnExtractionRule.isMandatory());
			tableColumnExtractionRuleDTO.setIdentifier(String.valueOf(tableColumnExtractionRule.getId()));
			tableColumnExtractionRuleDTO.setColumnHeaderPattern(tableColumnExtractionRule.getColumnHeaderPattern());
			tableColumnExtractionRuleDTO.setColumnName(tableColumnExtractionRule.getColumnName());
			tableColumnExtractionRuleDTO.setCurrency(tableColumnExtractionRule.isCurrency());
			tableColumnExtractionRuleDTO.setTableExtractionRuleDTO(tableExtractionRuleDTO);
			if (tableColumnExtractionRule.getColumnStartCoordinate() != null) {
				tableColumnExtractionRuleDTO.setColumnStartCoordinate(String.valueOf(tableColumnExtractionRule
						.getColumnStartCoordinate()));
			} else {
				tableColumnExtractionRuleDTO.setColumnStartCoordinate(CoreCommonConstants.EMPTY_STRING);
			}
			if (tableColumnExtractionRule.getColumnEndCoordinate() != null) {
				tableColumnExtractionRuleDTO
						.setColumnEndCoordinate(String.valueOf(tableColumnExtractionRule.getColumnEndCoordinate()));
			} else {
				tableColumnExtractionRuleDTO.setColumnEndCoordinate(CoreCommonConstants.EMPTY_STRING);
			}
			if (tableColumnExtractionRule.getColumnCoordinateY0() != null) {
				tableColumnExtractionRuleDTO.setColumnCoordY0(String.valueOf(tableColumnExtractionRule.getColumnCoordinateY0()));
			} else {
				tableColumnExtractionRuleDTO.setColumnCoordY0(CoreCommonConstants.EMPTY_STRING);
			}
			if (tableColumnExtractionRule.getColumnCoordinateY1() != null) {
				tableColumnExtractionRuleDTO.setColumnCoordY1(String.valueOf(tableColumnExtractionRule.getColumnCoordinateY1()));
			} else {
				tableColumnExtractionRuleDTO.setColumnCoordY1(CoreCommonConstants.EMPTY_STRING);
			}
		}
		return tableColumnExtractionRuleDTO;
	}

	/**
	 * Generates tree structure for CMIS Export checkbox while importing a batch class.
	 * 
	 * @param cmisNode{@link Node} CMIS Export Plugin Parent Node.
	 */
	private static void createCMISTree(final Node cmisNode) {
		if (null != cmisNode) {
			// Added for CMIS Properties Checkbox Under CMIS Export Tree.
			Node cmisPropertiesChild = new Node();
			Label cmisPropertiesLabel = cmisPropertiesChild.getLabel();
			cmisPropertiesLabel.setDisplayName(BatchConstants.CMIS_PROPERTIES_DISPLAY_NAME);
			cmisPropertiesLabel.setKey(BatchConstants.CMIS_PLUGIN_PROPERTIES);
			cmisPropertiesLabel.setMandatory(Boolean.TRUE);
			cmisPropertiesChild.setLabel(cmisPropertiesLabel);
			cmisPropertiesChild.setParent(cmisNode);
			cmisNode.getChildren().add(cmisPropertiesChild);

			// Added for CMIS Mapping Checkbox Under CMIS Export Tree.
			Node cmisMappingChild = new Node();
			Label cmisMappingLabel = cmisMappingChild.getLabel();
			cmisMappingLabel.setDisplayName(BatchConstants.CMIS_MAPPING_DISPLAY_NAME);
			cmisMappingLabel.setKey(BatchConstants.CMIS_MAPPING);
			cmisMappingLabel.setMandatory(Boolean.TRUE);
			cmisMappingChild.setLabel(cmisMappingLabel);
			cmisMappingChild.setParent(cmisNode);
			cmisNode.getChildren().add(cmisMappingChild);
		}
	}
}
