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

package com.ephesoft.dcma.da.service;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.common.DeleteBatchClassEvent;
import com.ephesoft.dcma.da.common.NewBatchClassEvent;
import com.ephesoft.dcma.da.dao.BatchClassDao;
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
import com.ephesoft.dcma.da.domain.ModuleConfig;
import com.ephesoft.dcma.da.domain.PageType;
import com.ephesoft.dcma.da.domain.RegexValidation;
import com.ephesoft.dcma.da.domain.TableColumnExtractionRule;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableExtractionRule;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.property.BatchClassProperty;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is a database service to read data required by Batch Class Service.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.BatchClassService
 */
@Service
public class BatchClassServiceImpl implements BatchClassService, ApplicationContextAware {

	/**
	 * LOGGER to print the LOGGERging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchClassServiceImpl.class);

	private static final int DEFAULT_PRIORITY = 99;

	private static final String FINAL_DROP_FOLDER = "Final-drop-folder";

	private static final String BATCH_EXPORT_TO_FOLDER = "batch.export_to_folder";

	private static final String COPY_BATCH_XML = "COPY_BATCH_XML";

	private static final String EXPORT = "Export";

	private static final String DCMA_BATCH = "dcma-batch" + File.separator + "dcma-batch";

	private static final String BATCH_BASE_FOLDER = "batch.base_folder";

	@Autowired
	private BatchClassDao batchClassDao;

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	/**
	 * An api to fetch BatchClass by id.
	 * 
	 * @param identifier Serializable
	 * @return BatchClass
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchClass get(final Serializable identifier) {
		return batchClassDao.getBatchClassByIdentifier((String) identifier);
	}

	/**
	 * An api to update the batch class.
	 * 
	 * @param batchClass BatchClass
	 */
	@Transactional
	@Override
	public void saveOrUpdate(final BatchClass batchClass) {
		if (null == batchClass) {
			LOGGER.info("batch class is null.");
		} else {
			batchClassDao.saveOrUpdate(batchClass);
		}
	}

	/**
	 * An api to get batch class by unc folder name.
	 * 
	 * @param folderName String
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchClass getBatchClassbyUncFolder(final String folderName) {
		BatchClass batchClass = null;
		if (null == folderName || "".equals(folderName)) {
			LOGGER.info("folderName is null or empty.");
		} else {
			batchClass = batchClassDao.getBatchClassbyUncFolder(folderName);
		}
		return batchClass;
	}

	/**
	 * An api to fetch BatchClass by batch Class name.
	 * 
	 * @param batchClassName String
	 * @return BatchClass
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchClass getBatchClassbyName(final String batchClassName) {
		BatchClass batchClass = null;
		if (null == batchClassName || "".equals(batchClassName)) {
			LOGGER.info("batchClassName is null or empty.");
		} else {
			batchClass = batchClassDao.getBatchClassbyName(batchClassName);
		}
		return batchClass;
	}

	/**
	 * An api to fetch BatchClass by batch Class processName.
	 * 
	 * @param processName String
	 * @return BatchClass
	 */
	@Transactional(readOnly = true)
	@Override
	public BatchClass getBatchClassbyProcessName(final String processName) {
		BatchClass batchClass = null;
		if (null == processName || "".equals(processName)) {
			LOGGER.info("processName is null or empty.");
		} else {
			batchClass = batchClassDao.getBatchClassbyName(processName);
		}
		return batchClass;
	}

	@Override
	public List<BatchClass> getAllUnlockedBatchClasses() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllUnlockedBatchClasses();
		return batchClassList;
	}

	@Override
	public List<BatchClass> getAllBatchClasses() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllBatchClasses();
		return batchClassList;
	}

	@Override
	@Transactional(readOnly = true)
	public BatchClass getBatchClassByIdentifier(final String batchClassIdentifier) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByIdentifier(batchClassIdentifier);
		return batchClass;
	}

	@Override
	@Transactional(readOnly = true)
	public BatchClass getLoadedBatchClassByIdentifier(final String batchClassIdentifier) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByIdentifier(batchClassIdentifier);
		if (null != batchClass) {
			loadBatchClass(batchClass);
		}
		return batchClass;
	}

	@Override
	@Transactional(readOnly = true)
	public BatchClass getLoadedBatchClassByUNC(final String folderName) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassbyUncFolder(folderName);
		if (null != batchClass) {
			loadBatchClass(batchClass);
		}
		return batchClass;
	}

	@Override
	@Transactional(readOnly = false)
	public synchronized BatchClass acquireBatchClass(final String batchClassIdentifier, final String currentUser)
			throws BatchAlreadyLockedException {
		BatchClass batchClass = null;
		if (null != batchClassIdentifier) {
			batchClass = getBatchClassByIdentifier(batchClassIdentifier);
			if (!currentUser.equalsIgnoreCase(batchClass.getCurrentUser()) && batchClass.getCurrentUser() != null) {
				throw new BatchAlreadyLockedException("Batch Class " + batchClass + " is already locked by "
						+ batchClass.getCurrentUser());
			} else {
				// Updating the state of the batch to locked and saving in the
				// database.
				LOGGER.info(currentUser + " is getting lock on batch " + batchClassIdentifier);
				if (!currentUser.trim().isEmpty()) {
					batchClass.setCurrentUser(currentUser);
				}
				batchClassDao.updateBatchClass(batchClass);
			}
		} else {
			LOGGER.warn("batchClassID id is null.");
		}
		return batchClass;
	}

	@Transactional
	@Override
	public void unlockAllBatchClassesForCurrentUser(final String currentUser) {
		if (currentUser != null && !currentUser.isEmpty()) {
			final List<BatchClass> batchClassList = batchClassDao.getAllBatchClassesForCurrentUser(currentUser);
			if (batchClassList != null && !batchClassList.isEmpty()) {
				for (final BatchClass batchClass : batchClassList) {
					LOGGER.info("Unlocking batches for " + currentUser);
					batchClass.setCurrentUser(null);
					batchClassDao.updateBatchClass(batchClass);
				}
			} else {
				LOGGER.warn("No batches exist for the username " + currentUser);
			}
		} else {
			LOGGER.warn("Username not specified or is Null. Returning.");
		}
	}

	@Transactional
	@Override
	public void unlockCurrentBatchClass(final String batchClassIdentifier) {
		BatchClass batchClass = null;
		if (null == batchClassIdentifier) {
			LOGGER.info("batchClassIdentifier is null.");
			return;
		}
		batchClass = getBatchClassByIdentifier(batchClassIdentifier);
		batchClass.setCurrentUser(null);
		batchClassDao.updateBatchClass(batchClass);
	}

	@Transactional
	@Override
	public BatchClass merge(final BatchClass batchClass, final boolean isBatchClassDeleted) {
		if (isBatchClassDeleted) {
			applicationContext
					.publishEvent(new DeleteBatchClassEvent(applicationContext, new BatchClassID(batchClass.getIdentifier())));
		}
		return batchClassDao.merge(batchClass);
	}

	@Transactional
	@Override
	public void evict(final BatchClass batchClass) {
		batchClassDao.evict(batchClass);
	}

@Override
	public List<BatchClass> getBatchClassList(final int firstResult, final int maxResults, final Order order,
			final Set<String> userRoles) {
		List<BatchClass> batches = null;
		Order batchClassOrder = order;
		final List<Order> orderList = new ArrayList<Order>();
		if (order == null) {
			batchClassOrder = new Order(BatchClassProperty.ID, true);
		}
		orderList.add(batchClassOrder);
		batches = batchClassDao.getBatchClassList(firstResult, maxResults, orderList, userRoles);
		return batches;
	}
	

	@Override
	public int countAllBatchClassesExcludeDeleted(final Set<String> userRoles) {
		return batchClassDao.getAllBatchClassCountExcludeDeleted(userRoles);
	}

	@Override
	@Transactional
	public void delete(final Serializable identifier) {
		final BatchClass object = batchClassDao.get(identifier);
		batchClassDao.remove(object);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public BatchClass createBatchClass(final BatchClass batchClass) {
		BatchClass newBatchClass = null;
		newBatchClass = batchClassDao.merge(batchClass);
		applicationContext.publishEvent(new NewBatchClassEvent(applicationContext, new BatchClassID(newBatchClass.getIdentifier())));
		return newBatchClass;
	}

	@Override
	public List<BatchClass> getAllBatchClassesByUserRoles(final Set<String> userRoles) {
		List<BatchClass> batchClassList = new ArrayList<BatchClass>();
		batchClassList = batchClassDao.getAllBatchClassesByUserRoles(userRoles);
		return batchClassList;
	}

	@Override
	public List<BatchClass> getAllBatchClassesExcludeDeleted() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllBatchClassExcludeDeleted();
		return batchClassList;
	}

	@Override
	@Transactional
	public List<BatchClass> getAllLoadedBatchClassExcludeDeleted() {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllLoadedBatchClassExcludeDeleted();
		return batchClassList;
	}

	@Override
	@Transactional
	public BatchClass merge(final BatchClass batchClass) {
		return batchClassDao.merge(batchClass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.dcma.da.service.BatchClassService#getAssociatedUNCList(java.lang.String)
	 */
	@Override
	@Transactional
	public List<String> getAssociatedUNCList(final String workflowName) {
		List<String> uncFolderList = null;
		uncFolderList = batchClassDao.getAllAssociatedUNCFolders(workflowName);
		return uncFolderList;
	}

	@Override
	@Transactional(readOnly = true)
	public BatchClass getLoadedBatchClassForImport(final String batchClassIdentifier) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByIdentifier(batchClassIdentifier);
		if (batchClass != null) {
			for (final BatchClassModule mod : batchClass.getBatchClassModules()) {
				for (final BatchClassModuleConfig importConfig : mod.getBatchClassModuleConfig()) {
					if (LOGGER.isDebugEnabled() && importConfig != null) {
						LOGGER.debug(importConfig.toString());
					}
				}
			}
		}
		return batchClass;
	}

	private void loadBatchClass(final BatchClass batchClass) {
		final List<BatchClassCmisConfiguration> cmisConfigurations = batchClass.getCmisConfigurations();
		if (cmisConfigurations != null && !cmisConfigurations.isEmpty()) {
			for (final BatchClassCmisConfiguration batchClassCmisConfiguration : cmisConfigurations) {
				if (batchClassCmisConfiguration != null) {
					LOGGER.debug(batchClassCmisConfiguration.getServerURL());
				}
			}
		}
		final List<BatchClassEmailConfiguration> emailConfigurations = batchClass.getEmailConfigurations();
		if (emailConfigurations != null && !emailConfigurations.isEmpty()) {
			for (final BatchClassEmailConfiguration emails : emailConfigurations) {
				if (emails != null) {
					LOGGER.debug(emails.getFolderName());
				}
			}
		}

		final List<BatchClassScannerConfiguration> scannerProfiles = batchClass.getBatchClassScannerConfiguration();
		if (scannerProfiles != null && !scannerProfiles.isEmpty()) {
			for (final BatchClassScannerConfiguration scannerProfile : scannerProfiles) {
				if (scannerProfile != null) {
					LOGGER.debug(scannerProfile.getScannerMasterConfig().getName());
					LOGGER.debug(scannerProfile.getValue());
					final List<BatchClassScannerConfiguration> childScannerProfiles = scannerProfile.getChildren();
					if (childScannerProfiles != null && !childScannerProfiles.isEmpty()) {
						for (final BatchClassScannerConfiguration childScannerProfile : childScannerProfiles) {
							LOGGER.debug(childScannerProfile.getScannerMasterConfig().getName());
							LOGGER.debug(childScannerProfile.getValue());

						}
					}
				}
			}
		}

		final List<BatchClassField> batchClassFields = batchClass.getBatchClassField();
		if (batchClassFields != null && !batchClassFields.isEmpty()) {
			for (final BatchClassField batchClassField : batchClassFields) {
				if (batchClassField != null) {
					LOGGER.debug(batchClassField.getName());
				}
			}
		}

		final List<DocumentType> documentTypes = batchClass.getDocumentTypes();
		if (documentTypes != null && !documentTypes.isEmpty()) {
			for (final DocumentType documentType : batchClass.getDocumentTypes()) {
				if (documentType != null) {
					LOGGER.debug(documentType.getName());
					for (final PageType pageType : documentType.getPages()) {
						if (pageType != null) {
							LOGGER.debug(pageType.getName());
						}
					}

					final List<FieldType> fieldTypes = documentType.getFieldTypes();
					if (fieldTypes != null && !fieldTypes.isEmpty()) {
						for (final FieldType fieldType : fieldTypes) {
							if (fieldType != null) {
								LOGGER.debug(fieldType.getName());
								for (final KVExtraction kvExtraction : fieldType.getKvExtraction()) {
									if (kvExtraction != null) {
										LOGGER.debug(kvExtraction.getKeyPattern());
									}
								}
								for (final RegexValidation regexValidation : fieldType.getRegexValidation()) {
									if (regexValidation != null) {
										LOGGER.debug(regexValidation.getPattern());
									}
								}
							}
						}
					}
					final List<TableInfo> tableInfos = documentType.getTableInfos();
					if (tableInfos != null && !tableInfos.isEmpty()) {
						for (final TableInfo tableInfo : tableInfos) {
							if (tableInfo != null) {
								LOGGER.debug(tableInfo.getName());
								for (final TableColumnsInfo tableColumnsInfo : tableInfo.getTableColumnsInfo()) {
									if (tableColumnsInfo != null) {
										LOGGER.debug(tableColumnsInfo.getColumnName());
									}
								}
								loadTableExtractionRules(tableInfo);
							}
						}

					}
					final List<FunctionKey> functionKeys = documentType.getFunctionKeys();
					if (functionKeys != null && !functionKeys.isEmpty()) {
						for (final FunctionKey functionKey : functionKeys) {
							if (functionKey != null) {
								LOGGER.debug(functionKey.getShortcutKeyname());
							}
						}
					}
				}
			}
		}

		final List<BatchClassGroups> assignedGroups = batchClass.getAssignedGroups();
		if (assignedGroups != null && !assignedGroups.isEmpty()) {
			for (final BatchClassGroups roles : assignedGroups) {
				if (roles != null) {
					LOGGER.debug(roles.getGroupName());
				}
			}
		}

		for (final BatchClassModule mod : batchClass.getBatchClassModules()) {
			final List<BatchClassModuleConfig> modConfigs = mod.getBatchClassModuleConfig();
			if (modConfigs != null && !modConfigs.isEmpty()) {
				for (final BatchClassModuleConfig BCModConfig : modConfigs) {
					final ModuleConfig moduleConfig = BCModConfig.getModuleConfig();
					if (moduleConfig != null) {
						LOGGER.debug(moduleConfig.getChildDisplayName());
					}
				}
			}

			final List<BatchClassPlugin> plugins = mod.getBatchClassPlugins();
			if (plugins != null && !plugins.isEmpty()) {
				for (final BatchClassPlugin plugin : plugins) {
					final List<BatchClassPluginConfig> pluginConfigs = plugin.getBatchClassPluginConfigs();
					final List<BatchClassDynamicPluginConfig> dynamicPluginConfigs = plugin.getBatchClassDynamicPluginConfigs();
					if (pluginConfigs != null && !pluginConfigs.isEmpty()) {
						for (final BatchClassPluginConfig conf : pluginConfigs) {
							final List<KVPageProcess> kvs = conf.getKvPageProcesses();
							for (final KVPageProcess kv : kvs) {
								if (kv != null) {
									LOGGER.debug(kv.getKeyPattern());
								}
							}
						}
					}
					if (dynamicPluginConfigs != null && !dynamicPluginConfigs.isEmpty()) {
						for (final BatchClassDynamicPluginConfig dyPluginConfig : dynamicPluginConfigs) {
							if (dyPluginConfig != null) {
								LOGGER.debug(dyPluginConfig.getName());
								final List<BatchClassDynamicPluginConfig> children = dyPluginConfig.getChildren();
								if (children != null && !children.isEmpty()) {
									for (final BatchClassDynamicPluginConfig child : children) {
										if (child != null) {
											LOGGER.debug(child.getName());
											LOGGER.debug(child.getValue());
										}
									}
								}
							}
						}
					}
					for (final Dependency dependency : plugin.getPlugin().getDependencies()) {
						if (dependency != null) {
							LOGGER.debug(dependency.getDependencies());
						}
					}
				}
			}
		}

	}

	private void loadTableExtractionRules(final TableInfo tableInfo) {
		List<TableExtractionRule> tableExtractionRulesList = tableInfo.getTableExtractionRules();
		List<TableColumnExtractionRule> tableColumnExtractionRulesList = null;
		if (null != tableExtractionRulesList) {
			for (final TableExtractionRule tableExtractionRule : tableExtractionRulesList) {
				if (tableExtractionRule != null) {
					LOGGER.debug(tableExtractionRule.getRuleName());
					tableColumnExtractionRulesList = tableExtractionRule.getTableColumnExtractionRules();
					for (final TableColumnExtractionRule tableColumnExtractionRule : tableColumnExtractionRulesList) {
						if (tableColumnExtractionRule != null) {
							LOGGER.debug(tableColumnExtractionRule.getColumnName());
						}
					}
				}
			}
		}
	}

	@Override
	public BatchClass getBatchClassByNameIncludingDeleted(final String batchClassName) {
		final BatchClass batchClass = batchClassDao.getBatchClassByNameIncludingDeleted(batchClassName);

		if (batchClass != null) {
			for (final BatchClassModule mod : batchClass.getBatchClassModules()) {
				for (final BatchClassModuleConfig importConfig : mod.getBatchClassModuleConfig()) {
					if (LOGGER.isDebugEnabled() && importConfig != null) {
						LOGGER.debug(importConfig.toString());
					}
				}
				final List<BatchClassPlugin> plugins = mod.getBatchClassPlugins();
				for (final BatchClassPlugin plugin : plugins) {
					if (LOGGER.isDebugEnabled() && plugin != null) {
						LOGGER.debug(plugin.toString());
					}

				}
			}
		}
		return batchClass;
	}

	@Override
	@Transactional
	public BatchClass createBatchClassWithoutWatch(final BatchClass batchClass) {
		BatchClass batchClass1 = null;
		batchClass1 = batchClassDao.merge(batchClass);
		return batchClass1;
	}

	@Override
	@Transactional
	public BatchClass getLoadedBatchClassByNameIncludingDeleted(final String batchClassName) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByNameIncludingDeleted(batchClassName);
		if (null != batchClass) {
			loadBatchClass(batchClass);
		}
		return batchClass;
	}

	@Override
	public String getBatchClassIdentifierByUNCfolder(final String uncFolder) {
		String identifier = null;
		identifier = batchClassDao.getBatchClassIdentifierByUNCfolder(uncFolder);
		return identifier;
	}

	/**
	 * This API returns list of all batch class identifiers.
	 * 
	 * @return List<String>
	 */
	@Override
	public List<String> getAllBatchClassIdentifier() {
		List<String> allBatchClassIdentifiers = null;
		allBatchClassIdentifiers = batchClassDao.getAllBatchClassIdentifiers();
		return allBatchClassIdentifiers;
	}

	@Override
	public List<BatchClass> getAllBatchClasses(final boolean isExcludeDeleted, final boolean isAsc, final String propertyName) {
		List<BatchClass> batchClassList = null;
		batchClassList = batchClassDao.getAllBatchClasses(isExcludeDeleted, isAsc, propertyName);
		return batchClassList;
	}

	@Override
	public List<String> getAllUNCList(final boolean isExcludeDeleted) {
		return batchClassDao.getAllUNCList(isExcludeDeleted);
	}

	@Override
	@Transactional
	public BatchClass copyBatchClass(final String identifier, final String batchClassName, final String batchClassDesc,
			final String batchClassGroup, final String batchClassPriority, final String uncFolderName,
			final boolean isConfigureExportFolder) throws DCMAApplicationException {
		LOGGER.info("Inside Copy of batch class: " + identifier);
		final boolean configureExportFolder = isConfigureExportFolder;
		String sharedFolderPath = null;
		// empty and null checks
		isStringEmptyOrNull("batch class name", batchClassName);
		isStringEmptyOrNull("batch class identifier", identifier);
		isStringEmptyOrNull("batch class group", batchClassGroup);
		isStringEmptyOrNull("batch class priority", batchClassPriority);
		isStringEmptyOrNull("UNC folder", uncFolderName);
		// checking batch class and unc folder existence
		LOGGER.info("Checking batch class and unc folder existence");
		checkBatchClassOrUncFolderExistence(batchClassName, uncFolderName);
		LOGGER.info("Getting the loaded batch class from database");
		BatchClass batchClass = getLoadedBatchClassByIdentifier(identifier);
		if (batchClass == null) {
			LOGGER.error("Batch class with id:" + identifier + " does not exists.");
			throw new DCMAApplicationException("Batch class with id:" + identifier + " does not exist.");
		} else {
			evict(batchClass);
			// getting the shared folder path from batch properties file
			try {
				sharedFolderPath = ApplicationConfigProperties.getApplicationConfigProperties().getAllProperties(DCMA_BATCH)
						.getProperty(BATCH_BASE_FOLDER);
			} catch (final IOException ioException) {
				LOGGER.error("Unable to get shared folder path from properties file.", ioException);
				throw new DCMAApplicationException("Unable to get shared folder path from properties file.", ioException);
			}
			isStringEmptyOrNull("Shared folder path ", sharedFolderPath);
			// JIRA-Bug-11125
			String uncFolderPath;
			if (uncFolderName.contains(File.separator)) {
				uncFolderPath = uncFolderName;
			} else {
				uncFolderPath = sharedFolderPath + File.separator + uncFolderName;
			}
			// creating unc folder
			final File uncFolder = new File(uncFolderPath);
			uncFolder.mkdirs();
			// setting batch class info
			LOGGER.info("Setting batch class info.");
			setBatchClassInfo(batchClass, batchClassName, batchClassDesc, uncFolderPath, batchClassGroup, batchClassPriority);
			LOGGER.info("Start copy batch class modules.");
			copyModules(batchClass);
			// Email configurations were not getting copied
			copyEmailConfiguration(batchClass);
			LOGGER.info("Start copy batch class document types.");
			copyDocumentTypes(batchClass);
			LOGGER.info("Start copy batch class scanner configs.");
			copyScannerConfig(batchClass);
			LOGGER.info("Start copy batch class fields.");
			copyBatchClassField(batchClass);
			LOGGER.info("Creating batch class");
			batchClass = createBatchClass(batchClass);
			// copy batch class folder
			evict(batchClass);

			batchClass = getLoadedBatchClassByIdentifier(batchClass.getIdentifier());
			// evict(batchClass);
			// rename module names
			renameBatchClassModules(batchClass);
			batchClass = merge(batchClass);
			final File originalFolder = new File(sharedFolderPath + File.separator + identifier);
			final File copiedFolder = new File(sharedFolderPath + File.separator + batchClass.getIdentifier());
			try {
				FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);
			} catch (final IOException ioException) {
				LOGGER.error("Error in copy of the batch class folder.", ioException);
				throw new DCMAApplicationException("Error in copy of the batch class folder.", ioException);
			}
			LOGGER.info("Batch class folder has been created successfully");
			if (configureExportFolder) {
				// configure the export folder of newly created batch class
				LOGGER.info("Inside configure export folder");
				final String newExportFolder = sharedFolderPath + File.separator + batchClass.getBatchClassID() + File.separator
						+ FINAL_DROP_FOLDER;
				final boolean isValueUpdated = setPluginConfigValue(batchClass, EXPORT, COPY_BATCH_XML, BATCH_EXPORT_TO_FOLDER,
						newExportFolder);
				if (isValueUpdated) {
					LOGGER.info("Updating the batch class after configuring the export folder.");
					// updating the batch class
					batchClassDao.updateBatchClass(batchClass);
					// creating the final drop folder in the batch class folder for the new batch class
					LOGGER.info("Creating the export folder for the batch class");
					final File exportFolder = new File(newExportFolder);
					exportFolder.mkdirs();
					LOGGER.info("Final drop folder created successfully");
				} else {
					LOGGER.error("Error in configuring the export folder");
					throw new DCMAApplicationException("Error in configuring the export folder");
				}
			}
		}
		return batchClass;
	}

	/**
	 * API to check for null or empty strings
	 * 
	 * @param property{@link String} : the name of the string
	 * @param data{@link String} : value of the string
	 * @throws DCMAApplicationException
	 */
	private void isStringEmptyOrNull(final String property, final String data) throws DCMAApplicationException {
		if (data == null) {
			LOGGER.error(property + " is null.");
			throw new DCMAApplicationException(property + " is null.");
		}
		if (data.isEmpty()) {
			LOGGER.error(property + " is empty.");
			throw new DCMAApplicationException(property + " is empty.");
		}
	}

	/**
	 * API that checks whether batch class with name or unc folder exists. Returns true if batch class with name or unc folder exists
	 * else returns false.
	 * 
	 * @param batchClassName{@link String}
	 * @return
	 * @throws CopyBatchClassException
	 */
	private void checkBatchClassOrUncFolderExistence(final String batchClassName, final String uncFolder)
			throws DCMAApplicationException {
		LOGGER.info("Checking batch class existance");
		final List<BatchClass> batchClasses = getAllBatchClasses();
		final List<String> uncFolders = getAllUNCList(false);
		if (batchClasses != null && (!batchClasses.isEmpty())) {
			for (final BatchClass batchClass : batchClasses) {
				if (batchClass.getName().equalsIgnoreCase(batchClassName)) {
					LOGGER.error("Batch class with name : " + batchClassName + " already exists");
					throw new DCMAApplicationException("Batch class with name : " + batchClassName + " already exists.");

				}
			}
		}
		if (uncFolders != null && !uncFolders.isEmpty()) {
			for (final String uncFolderName : uncFolders) {
				if (uncFolderName.equalsIgnoreCase(uncFolder)) {
					LOGGER.error("UNC folder with name : " + uncFolder + " already exists");
					throw new DCMAApplicationException("UNC folder with name : " + uncFolder + " already exists");
				}

			}
		}
	}

	/**
	 * API sets the value of the given batch class plugin config with the new value value. Returns true if value is updated
	 * successfully.
	 * 
	 * @param batchClass{@link BatchClass}
	 * @param moduleName{@link String}
	 * @param pluginName{@link String}
	 * @param pluginConfigName{@link String}
	 * @param newpluginConfigValue{@link String}
	 * @return
	 */
	private boolean setPluginConfigValue(final BatchClass batchClass, final String moduleName, final String pluginName,
			final String pluginConfigName, final String newpluginConfigValue) {
		boolean isValueUpdated = false;
		BatchClassPlugin copyBatchXMLPlugin = null;
		final BatchClassModule exportModule = batchClass.getBatchClassModuleByName(moduleName);
		List<BatchClassPlugin> batchClassPlugins = null;
		if (exportModule != null) {
			batchClassPlugins = exportModule.getBatchClassPlugins();
		}
		if (batchClassPlugins != null) {
			for (final BatchClassPlugin batchClassPlugin : batchClassPlugins) {
				if (batchClassPlugin.getPlugin().getPluginName().equals(pluginName)) {
					copyBatchXMLPlugin = batchClassPlugin;
					break;
				}
			}
			isValueUpdated = updatePluginInformation(copyBatchXMLPlugin, pluginConfigName, newpluginConfigValue);

		}
		return isValueUpdated;
	}

	/**
	 * API updates the value of the plugin config with the new value given as argument.
	 * 
	 * @param plugin{@link BatchClassPlugin}
	 * @param pluginConfigName{@link String}
	 * @param newPluginConfigValue{@link String}
	 * @return
	 */
	private boolean updatePluginInformation(final BatchClassPlugin plugin, final String pluginConfigName,
			final String newPluginConfigValue) {
		LOGGER.info("Updating the plugin config value.");
		boolean isPluginConfigValueUpdated = false;
		if (plugin != null) {
			final List<BatchClassPluginConfig> batchClassPluginConfigs = plugin.getBatchClassPluginConfigs();
			if (batchClassPluginConfigs != null && (!batchClassPluginConfigs.isEmpty())) {
				for (final BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
					if (batchClassPluginConfig.getName().equals(pluginConfigName)) {
						batchClassPluginConfig.setValue(newPluginConfigValue);
						isPluginConfigValueUpdated = true;
						break;
					}
				}

			}

		}
		return isPluginConfigValueUpdated;
	}

	/**
	 * API sets the batch class info with the given arguments.
	 * 
	 * @param batchClass{@link String}
	 * @param batchClassName{@link String}
	 * @param batchClassDesc{@link String}
	 * @param uncFolder{@link String}
	 * @param batchClassGroup{@link String}
	 * @param batchClassPriority{@link String}
	 */
	private void setBatchClassInfo(final BatchClass batchClass, final String batchClassName, final String batchClassDesc,
			final String uncFolder, final String batchClassGroup, final String batchClassPriority) {
		LOGGER.info("Setting batch class info.");
		batchClass.setDescription(batchClassDesc);
		int priority = DEFAULT_PRIORITY;
		if (batchClassPriority != null) {
			try {
				final int newpriority = Integer.valueOf(batchClassPriority);
				if (0 <= newpriority && newpriority <= 100) {
					priority = newpriority;
				}
			} catch (final NumberFormatException e) {
				LOGGER.error("Error converting priority: " + batchClassPriority + "for batch class:" + " ." + e.getMessage(), e);
			}
		}
		batchClass.setPriority(priority);
		batchClass.setUncFolder(uncFolder);
		batchClass.setName(batchClassName);
		batchClass.setId(0);
		batchClass.setIdentifier(null);
		batchClass.setCurrentUser(null);
		batchClass.setVersion(ICommonConstants.VERSION);
		batchClass.setEmailConfigurations(null);
		final List<BatchClassGroups> batchClassGroupsList = batchClass.getAssignedGroups();
		if (batchClassGroup == null) {
			// assign the old batch class groups to the new batch class
			for (final BatchClassGroups batchClassGroups : batchClassGroupsList) {
				batchClassGroups.setId(0);
			}

		} else {// create a new user group and assign that group to new batch class
			batchClassGroupsList.clear();
			final BatchClassGroups batchClassGroupName = new BatchClassGroups();
			batchClassGroupName.setBatchClass(batchClass);
			batchClassGroupName.setGroupName(batchClassGroup);
			batchClassGroupsList.add(batchClassGroupName);
		}

		batchClass.setAssignedGroups(batchClassGroupsList);
		batchClass.setDeleted(false);
	}

	/**
	 * API to copy modules of batch class.
	 * 
	 * @param batchClass{@link BatchClass}
	 */
	private void copyModules(final BatchClass batchClass) {
		final List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
		final List<BatchClassModule> newBatchClassModulesList = new ArrayList<BatchClassModule>();
		for (final BatchClassModule batchClassModule : batchClassModules) {
			newBatchClassModulesList.add(batchClassModule);
			batchClassModule.setId(0);
			batchClassModule.setBatchClass(null);
			copyPlugins(batchClassModule);
			copyModuleConfig(batchClassModule);
		}
		batchClass.setBatchClassModules(newBatchClassModulesList);
	}

	/**
	 * Creates a copy of email configuration assigning id 0 to the new objects. So as to create new objects using hibernate.
	 * 
	 * @param batchClass {@link BatchClass} whose email configurations are to be eagerly loaded for creating a copy.
	 */
	private void copyEmailConfiguration(final BatchClass batchClass) {
		final List<BatchClassEmailConfiguration> emailList = batchClass.getEmailConfigurations();
		final List<BatchClassEmailConfiguration> newBatchClassEmailList = new ArrayList<BatchClassEmailConfiguration>();
		if (!CollectionUtil.isEmpty(emailList)) {
			for (final BatchClassEmailConfiguration batchClassEmailConfig : emailList) {
				newBatchClassEmailList.add(batchClassEmailConfig);
				batchClassEmailConfig.setId(0);
				batchClassEmailConfig.setBatchClass(null);
			}
		}
		batchClass.setEmailConfigurations(newBatchClassEmailList);
	}

	/**
	 * API to copy Document Types of batch class.
	 * 
	 * @param batchClass{@link BatchClass}
	 */
	private void copyDocumentTypes(final BatchClass batchClass) {
		final List<DocumentType> documentTypes = batchClass.getDocumentTypes();
		final List<DocumentType> newDocumentType = new ArrayList<DocumentType>();
		for (final DocumentType documentType : documentTypes) {
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

	/**
	 * API to copy ScannerConfig of batch class.
	 * 
	 * @param batchClass{@link BatchClass}
	 */
	private void copyScannerConfig(final BatchClass batchClass) {
		final List<BatchClassScannerConfiguration> configs = batchClass.getBatchClassScannerConfiguration();
		final List<BatchClassScannerConfiguration> newConfigsList = new ArrayList<BatchClassScannerConfiguration>();
		for (final BatchClassScannerConfiguration config : configs) {
			if (config.getParent() == null) {
				newConfigsList.add(config);
				config.setId(0);
				config.setBatchClass(null);

				final List<BatchClassScannerConfiguration> children = config.getChildren();
				for (final BatchClassScannerConfiguration child : children) {
					child.setId(0);
					newConfigsList.add(child);
					child.setBatchClass(null);
					child.setParent(config);
				}
			}
		}
		batchClass.setBatchClassScannerConfiguration(newConfigsList);
	}

	/**
	 * API to copy Batch Class Fields of batch class.
	 * 
	 * @param batchClass{@link BatchClass}
	 */
	private void copyBatchClassField(final BatchClass batchClass) {
		final List<BatchClassField> batchClassField = batchClass.getBatchClassField();
		final List<BatchClassField> newBatchClassField = new ArrayList<BatchClassField>();
		for (final BatchClassField batchClassFieldTemp : batchClassField) {
			newBatchClassField.add(batchClassFieldTemp);
			batchClassFieldTemp.setId(0);
			batchClassFieldTemp.setBatchClass(null);
			batchClassFieldTemp.setIdentifier(null);
		}
		batchClass.setBatchClassField(newBatchClassField);
	}

	/**
	 * API to copy Page Types of batch class.
	 * 
	 * @param documentType{@link DocumentType}
	 */
	private void copyPageTypes(final DocumentType documentType) {
		final List<PageType> pages = documentType.getPages();
		final List<PageType> newPageTypes = new ArrayList<PageType>();
		for (final PageType pageType : pages) {
			newPageTypes.add(pageType);
			pageType.setId(0);
			pageType.setDocType(null);
			pageType.setIdentifier(null);
		}
		documentType.setPages(newPageTypes);
	}

	/**
	 * API to copy Field Types of batch class.
	 * 
	 * @param documentType{@link DocumentType}
	 */
	private void copyFieldTypes(final DocumentType documentType) {
		final List<FieldType> fieldTypes = documentType.getFieldTypes();
		final List<FieldType> newFieldType = new ArrayList<FieldType>();
		for (final FieldType fieldType : fieldTypes) {
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
	 * API to copy Table Info of batch class.
	 * 
	 * @param documentType{@link DocumentType}
	 */
	private void copyTableInfo(final DocumentType documentType) {
		final List<TableInfo> tableInfos = documentType.getTableInfos();
		final List<TableInfo> newTableInfo = new ArrayList<TableInfo>();
		for (final TableInfo tableInfo : tableInfos) {
			newTableInfo.add(tableInfo);
			tableInfo.setId(0);
			tableInfo.setDocType(null);
			copyTableColumnsInfo(tableInfo);
			copyTableExtractionRule(tableInfo);
		}
		documentType.setTableInfos(newTableInfo);
	}

	/**
	 * API to copy Function Keys of batch class.
	 * 
	 * @param documentType{@link DocumentType}
	 */
	public void copyFunctionKeys(final DocumentType documentType) {
		final List<FunctionKey> functionKeys = documentType.getFunctionKeys();
		final List<FunctionKey> newFunctionKeys = new ArrayList<FunctionKey>();
		for (final FunctionKey functionKey : functionKeys) {
			newFunctionKeys.add(functionKey);
			functionKey.setId(0);
			functionKey.setDocType(null);
			functionKey.setIdentifier(null);
		}
		documentType.setFunctionKeys(newFunctionKeys);

	}

	/**
	 * API to copy Table Columns Info of batch class.
	 * 
	 * @param tableInfo{@link TableInfo}
	 */
	public void copyTableColumnsInfo(final TableInfo tableInfo) {
		final List<TableColumnsInfo> tableColumnsInfos = tableInfo.getTableColumnsInfo();
		final List<TableColumnsInfo> newTableColumnsInfo = new ArrayList<TableColumnsInfo>();
		for (final TableColumnsInfo tableColumnsInfo : tableColumnsInfos) {
			newTableColumnsInfo.add(tableColumnsInfo);
			tableColumnsInfo.setId(0);
		}
		tableInfo.setTableColumnsInfo(newTableColumnsInfo);
	}

	/**
	 * API to copy Table Columns Extraction Rules of table extraction rule.
	 * 
	 * @param tableExtractionRule{@link TableExtractionRule} the table extraction rule whose column extraction rules are to be copied.
	 */
	private void copyTableColumnsExtractionRule(final TableExtractionRule tableExtractionRule) {
		final List<TableColumnExtractionRule> tableExtractionRuleList = tableExtractionRule.getTableColumnExtractionRules();
		final List<TableColumnExtractionRule> newTableColumnExtractionRules = new ArrayList<TableColumnExtractionRule>();
		for (final TableColumnExtractionRule tableColumnExtractionRule : tableExtractionRuleList) {
			newTableColumnExtractionRules.add(tableColumnExtractionRule);
			tableColumnExtractionRule.setId(0);
		}
		tableExtractionRule.setTableColumnExtractionRules(newTableColumnExtractionRules);
	}

	/**
	 * API to copy Table Extraction Rules of Table info field.
	 * 
	 * @param tableInfo{@link TableInfo} the table whose table extraction rules are to be copied.
	 */
	private void copyTableExtractionRule(final TableInfo tableInfo) {
		final List<TableExtractionRule> tableColumnsExtractionRuleList = tableInfo.getTableExtractionRules();
		final List<TableExtractionRule> newTableExtractionRules = new ArrayList<TableExtractionRule>();
		for (final TableExtractionRule tableColumnExtractionRule : tableColumnsExtractionRuleList) {
			newTableExtractionRules.add(tableColumnExtractionRule);
			tableColumnExtractionRule.setId(0);
			copyTableColumnsExtractionRule(tableColumnExtractionRule);
		}
		tableInfo.setTableExtractionRules(newTableExtractionRules);
	}

	/**
	 * API to copy KVExtractionFields of batch class.
	 * 
	 * @param fieldType{@link FieldType}
	 */
	private void copyKVExtractionFields(final FieldType fieldType) {
		final List<KVExtraction> kvExtraction2 = fieldType.getKvExtraction();
		final List<KVExtraction> newKvExtraction = new ArrayList<KVExtraction>();
		for (final KVExtraction kvExtraction : kvExtraction2) {
			newKvExtraction.add(kvExtraction);
			kvExtraction.setId(0);
			kvExtraction.setFieldType(null);
		}
		fieldType.setKvExtraction(newKvExtraction);
	}

	/**
	 * API to copy Regex of batch class.
	 * 
	 * @param fieldType{@link FieldType}
	 */
	private void copyRegex(final FieldType fieldType) {
		final List<RegexValidation> regexValidations = fieldType.getRegexValidation();
		final List<RegexValidation> regexValidations2 = new ArrayList<RegexValidation>();
		for (final RegexValidation regexValidation : regexValidations) {
			regexValidations2.add(regexValidation);
			regexValidation.setId(0);
			regexValidation.setFieldType(null);
		}
		fieldType.setRegexValidation(regexValidations2);
	}

	/**
	 * API to copy Plugins of batch class.
	 * 
	 * @param batchClassModule{@link BatchClassModule}
	 */
	private void copyPlugins(final BatchClassModule batchClassModule) {
		final List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
		final List<BatchClassPlugin> newBatchClassPluginsList = new ArrayList<BatchClassPlugin>();
		for (final BatchClassPlugin batchClassPlugin : batchClassPlugins) {
			newBatchClassPluginsList.add(batchClassPlugin);
			batchClassPlugin.setId(0);
			batchClassPlugin.setBatchClassModule(null);
			copyPluginConfigs(batchClassPlugin);
			copyDynamicPluginConfigs(batchClassPlugin);
		}
		batchClassModule.setBatchClassPlugins(newBatchClassPluginsList);
	}

	/**
	 * API to copy PluginConfigs of batch class.
	 * 
	 * @param batchClassPlugin{@link BatchClassPlugin}
	 */
	private void copyPluginConfigs(final BatchClassPlugin batchClassPlugin) {
		final List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();
		final List<BatchClassPluginConfig> newBatchClassPluginConfigsList = new ArrayList<BatchClassPluginConfig>();
		for (final BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
			newBatchClassPluginConfigsList.add(batchClassPluginConfig);
			batchClassPluginConfig.setId(0);
			batchClassPluginConfig.setBatchClassPlugin(null);
			// copyBatchClassPluginConfigsChild(batchClassPluginConfig);
			copyKVPageProcess(batchClassPluginConfig);
		}
		batchClassPlugin.setBatchClassPluginConfigs(newBatchClassPluginConfigsList);
	}

	/**
	 * API to copy DynamicPluginConfigs of batch class.
	 * 
	 * @param batchClassPlugin{@link BatchClassPlugin}
	 */
	private void copyDynamicPluginConfigs(final BatchClassPlugin batchClassPlugin) {
		final List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassPlugin
				.getBatchClassDynamicPluginConfigs();
		final List<BatchClassDynamicPluginConfig> newBatchClassDynamicPluginConfigsList = new ArrayList<BatchClassDynamicPluginConfig>();
		for (final BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
			newBatchClassDynamicPluginConfigsList.add(batchClassDynamicPluginConfig);
			batchClassDynamicPluginConfig.setId(0);
			batchClassDynamicPluginConfig.setBatchClassPlugin(null);
			copyBatchClassDynamicPluginConfigsChild(batchClassDynamicPluginConfig);
		}
		batchClassPlugin.setBatchClassDynamicPluginConfigs(newBatchClassDynamicPluginConfigsList);
	}

	/**
	 * API to copy KVPageProcess of batch class.
	 * 
	 * @param batchClassPluginConfig{@link BatchClassPluginConfig}
	 */
	private void copyKVPageProcess(final BatchClassPluginConfig batchClassPluginConfig) {
		final List<KVPageProcess> kvPageProcess = batchClassPluginConfig.getKvPageProcesses();
		final List<KVPageProcess> newKvPageProcessList = new ArrayList<KVPageProcess>();
		for (final KVPageProcess kVPageProcessChild : kvPageProcess) {
			kVPageProcessChild.setId(0);
			newKvPageProcessList.add(kVPageProcessChild);
			kVPageProcessChild.setBatchClassPluginConfig(null);
		}
		batchClassPluginConfig.setKvPageProcesses(newKvPageProcessList);
	}

	/**
	 * API to copy DynamicPluginConfigsChild.
	 * 
	 * @param batchClassDynamicPluginConfig {@link BatchClassDynamicPluginConfig}
	 */
	private void copyBatchClassDynamicPluginConfigsChild(final BatchClassDynamicPluginConfig batchClassDynamicPluginConfig) {
		final List<BatchClassDynamicPluginConfig> children = batchClassDynamicPluginConfig.getChildren();
		final List<BatchClassDynamicPluginConfig> newChildrenList = new ArrayList<BatchClassDynamicPluginConfig>();
		if (children != null && !children.isEmpty()) {
			for (final BatchClassDynamicPluginConfig child : children) {
				child.setId(0);
				newChildrenList.add(child);
				child.setBatchClassPlugin(null);
				child.setParent(null);
			}
			batchClassDynamicPluginConfig.setChildren(newChildrenList);
		}
	}

	/**
	 * API to copy ModuleConfig.
	 * 
	 * @param batchClassModule{@link BatchClassModule}
	 */
	private void copyModuleConfig(final BatchClassModule batchClassModule) {
		final List<BatchClassModuleConfig> batchClassModConfigs = batchClassModule.getBatchClassModuleConfig();
		final List<BatchClassModuleConfig> newBatchClassModuleConfigList = new ArrayList<BatchClassModuleConfig>();
		for (final BatchClassModuleConfig batchClassModConfig : batchClassModConfigs) {
			newBatchClassModuleConfigList.add(batchClassModConfig);
			batchClassModConfig.setId(0);
			batchClassModConfig.setBatchClassModule(null);
		}
		batchClassModule.setBatchClassModuleConfig(newBatchClassModuleConfigList);
	}

	/**
	 * API to rename the modules of given batch class (appending module names with batch class identifier)
	 * 
	 * @param batchClass
	 */
	private void renameBatchClassModules(final BatchClass batchClass) {
		final String existingBatchClassIdentifier = batchClass.getIdentifier();
		final List<BatchClassModule> batchClassModuleList = batchClass.getBatchClassModules();
		if (!CollectionUtil.isEmpty(batchClassModuleList)) {
			String existingModuleName = null;
			String newModuleName = null;
			for (final BatchClassModule batchClassModule : batchClassModuleList) {
				if (null != batchClassModule) {
					existingModuleName = batchClassModule.getModule().getName();
					newModuleName = EphesoftStringUtil.concatenate(
							existingModuleName.replaceAll(ICommonConstants.SPACE, ICommonConstants.UNDERSCORE),
							ICommonConstants.UNDERSCORE, existingBatchClassIdentifier);
					batchClassModule.setWorkflowName(newModuleName);
				}
			}
		}
	}

	@Override
	public BatchClass getBatchClassByUserRoles(final Set<String> userRoles, final String batchClassID) {
		return batchClassDao.getBatchClassByUserRoles(userRoles, batchClassID);
	}

	@Override
	public BatchClass getBatchClassByIdentifierIncludingDeleted(final String batchClassIdentifier) {
		BatchClass batchClass = null;
		batchClass = batchClassDao.getBatchClassByIdentifierIncludingDeleted(batchClassIdentifier);
		return batchClass;
	}

	@Transactional
	@Override
	public void clearCurrentUser(final String batchClassIdentifier) {
		batchClassDao.clearCurrentUser(batchClassIdentifier);
	}

	/**
	 * Returns all the batch classes on user role, sorted on basis of batch class description.
	 * 
	 * @param userRoles Set<String>- set of user roles
	 * @return List<BatchClass>- list of batch classes sorted on basis of batch class description
	 */
	@Override
	public List<BatchClass> getBatchClassesSortedOnDescription(final Set<String> userRoles) {
		List<BatchClass> batchClassList = null;
		if (null != userRoles) {
			batchClassList = batchClassDao.getBatchClassesSortedOnDescription(userRoles);
		}
		return batchClassList;
	}

	@Override
	public List<BatchClass> getAllDeletedBatchClass() {
		return batchClassDao.getAllDeletedBatchClass();
	}

	@Override
	public List<String> getAllUNCListExcludingDefaultBatchClasses(final boolean isExcludeDeleted) {
		return batchClassDao.getAllUNCListExcludingDefaultBatchClasses(isExcludeDeleted);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<BatchClass> getDeletedBatchClassesbyUncFolder(final String uncFolderName) {
		return batchClassDao.getDeletedBatchClassesbyUncFolder(uncFolderName);
	}
}
