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

package com.ephesoft.dcma.da.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.UserType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.service.DBScriptExecuter;
import com.ephesoft.dcma.da.constant.DataAccessConstant;
import com.ephesoft.dcma.da.dao.ModuleConfigDao;
import com.ephesoft.dcma.da.dao.ModuleDao;
import com.ephesoft.dcma.da.dao.PluginConfigDao;
import com.ephesoft.dcma.da.dao.PluginDao;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassCloudConfig;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassScannerConfiguration;
import com.ephesoft.dcma.da.domain.Dependency;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.ModuleConfig;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;
import com.ephesoft.dcma.da.domain.ScannerMasterConfiguration;
import com.ephesoft.dcma.da.service.BatchClassCloudConfigService;
import com.ephesoft.dcma.da.service.BatchClassCmisConfigService;
import com.ephesoft.dcma.da.service.BatchClassDynamicPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassEmailConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.MasterScannerService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.encryption.util.EphesoftEncryptionUtil;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;

public class ExecuteUpdatePatch {

	private static final Logger LOG = LoggerFactory.getLogger(ExecuteUpdatePatch.class);

	@Autowired
	private BatchClassService batchClassService;

	@Autowired
	private PluginService pluginService;

	@Autowired
	private MasterScannerService masterScannerService;

	@Autowired
	private PluginConfigDao dao;

	@Autowired
	private ModuleConfigDao moduleConfigDao;

	@Autowired
	private PluginDao pluginDao;

	@Autowired
	private ModuleDao moduleDao;

	@Autowired
	private DBScriptExecuter executer;

	@Autowired
	private BatchClassDynamicPluginConfigService batchClassDynamicPluginConfigService;

	@Autowired
	private DocumentTypeService documentTypeService;

	/**
	 * The batchClassCloudService {@link BatchClassCloudConfigService} is for accessing service available for batch class cloud
	 * configuration.
	 */
	@Autowired
	private BatchClassCloudConfigService batchClassCloudService;

	/**
	 * The batchClassPluginConfigService {@link BatchClassPluginConfigService} is for accessing service available for batch class
	 * plugin configuration.
	 */
	@Autowired
	private BatchClassPluginConfigService batchClassPluginConfigService;

	/**
	 * The batchClassEmailConfigService {@link BatchClassEmailConfigService} is for accessing service available for batch class email
	 * configuration.
	 */
	@Autowired
	private BatchClassEmailConfigService batchClassEmailConfigService;

	/**
	 * The batchClassCmisConfigService {@link BatchClassCmisConfigService} is for accessing service available for batch class cmis
	 * configuration.
	 */
	@Autowired
	private BatchClassCmisConfigService batchClassCmisConfigService;

	private static final String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();

	private String dbPatchFolderLocation;

	private boolean patchEnabled;

	private Map<String, List<BatchClassModule>> batchClassNameVsModulesMap = new HashMap<String, List<BatchClassModule>>();

	final private Map<String, List<BatchClass>> nameVsBatchClassListMap = new HashMap<String, List<BatchClass>>();

	private Map<String, List<BatchClassPlugin>> batchClassNameVsBatchClassPluginMap = new HashMap<String, List<BatchClassPlugin>>();

	private Map<String, List<Dependency>> pluginNameVsDependencyMap = new HashMap<String, List<Dependency>>();

	private Map<String, BatchClass> batchClassNameVsBatchClassMap = new HashMap<String, BatchClass>();

	private static final String USER_SUPER_ADMIN = "user.super_admin";

	private static final String DOUBLE_SEMICOLON_DELIMITER = ";;";

	private static final String INITIAL_VERSION = "1.0.0.0";

	/**
	 * The USER_TYPE {@link String} is a constant for Ephesoft Cloud user type key.
	 */
	private static final String USER_TYPE = "user_type";

	private boolean superAdminGroupUpdated;

	private boolean passwordsEncrypted;

	public void init() {
		if (patchEnabled) {

			try {

				LOG.info("==============Running the Upgrade Patch=======================");

				executer.execute(new ClassPathResource("META-INF/dcma-data-access/pre-schema.sql"));

				populateMap();

				LOG.info("Database is  not already prepared. Will execute the DB Scripts.");

				LOG.info("Upgrade Patch for Module Configs executed successfully.");

				LOG.info("==========Running Upgrade Patch for Module.==========");

				// Code change for removal of execution of Module serialized files.
				// updateModules();

				LOG.info("Upgrade Patch for Modules executed successfully.");

				LOG.info("==========Running Upgrade Patch for Plugins.==========");

				updatePlugin();

				LOG.info("Upgrade Patch for Plugins executed successfully.");

				LOG.info("==========Running Upgrade Patch for Plugin Configs.==========");

				updatePluginConfig();

				LOG.info("Upgrade Patch for Plugin Configs executed successfully.");

				LOG.info("==========Running Upgrade Patch for Batch Class.==========");

				updateBatchClasses();

				LOG.info("Upgrade Patch for Batch Class executed successfully.");

				LOG.info("==========Running Upgrade Patch for Scanner Configs.==========");

				// code for updating the email configs.
				updateEmailConfig();

				updateScannerConfig();

				LOG.info("Upgrade Patch for Scanner Configs executed successfully.");

				LOG.info("==========Running Upgrade Patch for Plugin Dependencies.==========");

				updatePluginDependencies();

				LOG.info("Upgrade Patch for Plugin Dependencies executed successfully.");

				executer.execute(new ClassPathResource("META-INF/dcma-data-access/post-schema.sql"));

				LOG.info("Assigning default roles to all batch classes.==========");

				assignDefaultRolesToBatchClasses();

				LOG.info("Assigning default scanner configs for all batch classes.==========");

				updateProperty(DataAccessConstant.DATA_ACCESS_DCMA_DB_PROPERTIES, DataAccessConstant.UPGRADE_PATCH_ENABLE,
						Boolean.FALSE.toString(), "Patch disabled.");

				LOG.info("Assigning default batch class cloud configs for all batch classes.==========");

				updateBatchClassLimit();

				LOG.info("Cleaning the database for invalid content");

				cleanUpDatabaseFromInvalidData();

				updateCopyBatchXMLsToken();

				LOG.info("==========Upgrade Patch finished successfully.==========");
			} catch (Exception e) {
				LOG.error("An Exception occurred while executing the patch." + e.getMessage(), e);
			}
		} else {
			LOG.info("No Upgrade Patch Configured. Continuing with the start up.");
		}

		// Checking the update_super_admin_group property.If enabled, assign super admin roles to all batch classes.
		if (isSuperAdminGroupUpdated()) {
			assignSuperAdminGroups();

			// Disable update_super_admin_group property
			updateProperty(DataAccessConstant.APPLICATION_PROPERTIES, DataAccessConstant.UPDATE_SUPER_ADMIN_GROUP,
					Boolean.FALSE.toString(), "Super admin group update disabled.");
		}

		// Check if the application passwords are encrypted or not. Following code will encrypt the passwords.

		if (isPasswordEncrypted()) {
			encryptPasswords();

			// Disable password.encrypt property
			updateProperty(DataAccessConstant.ENCRYPTION_PROPERTIES_FILE_META_INF, DataAccessConstant.PASSWORD_ENCRYPTED_PROPERTY,
					Boolean.FALSE.toString(), "Password encryted property disabled.");
		}
	}

	private void updateEmailConfig() {
		LOG.info(DataAccessConstant.UPDATING_BATCH_CLASSES);
		List<BatchClass> batchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();
		if (batchClasses != null && !batchClasses.isEmpty()) {
			for (BatchClass batchClass : batchClasses) {
				// Checking if the profiles are already present, if present then not adding the default values.
				List<BatchClassEmailConfiguration> emailConfigs = batchClass.getEmailConfigurations();
				if (null != emailConfigs && !emailConfigs.isEmpty()) {
					batchClassService.merge(batchClass);
				}
			}
		}
	}

	private void encryptPasswords() {
		// Encrypt plugin passwords
		encryptPluginPasswords();
		// Encrypt Email import passwords
		encryptMailPasswords();
	}

	private void encryptMailPasswords() {
		List<BatchClassEmailConfiguration> allEmailConfigs = batchClassEmailConfigService.getAllEmailConfigs();
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : allEmailConfigs) {
			String mailPassword = batchClassEmailConfiguration.getPassword();
			boolean isPasswordStringEncrypted = EphesoftEncryptionUtil.isPasswordStringEncrypted(mailPassword);
			if (!isPasswordStringEncrypted) {
				mailPassword = EphesoftEncryptionUtil.getEncryptedPasswordString(mailPassword);
				batchClassEmailConfiguration.setPassword(mailPassword);
				batchClassEmailConfigService.updateEmailConfiguration(batchClassEmailConfiguration);
			}

		}
	}

	private void encryptPluginPasswords() {
		List<BatchClassPluginConfig> passwordTypePluginConfigs = batchClassPluginConfigService
				.getBatchClassPluginConfigByDataType(DataType.PASSWORD);
		for (BatchClassPluginConfig batchClassPluginConfig : passwordTypePluginConfigs) {
			String pluginPassword = batchClassPluginConfig.getValue();
			boolean isPasswordStringEncrypted = EphesoftEncryptionUtil.isPasswordStringEncrypted(pluginPassword);
			if (!isPasswordStringEncrypted) {
				pluginPassword = EphesoftEncryptionUtil.getEncryptedPasswordString(pluginPassword);
				batchClassPluginConfig.setValue(pluginPassword);
				batchClassPluginConfigService.updateSinglePluginConfiguration(batchClassPluginConfig);
			}
		}
	}

	private void cleanUpDatabaseFromInvalidData() {
		cleanInvalidFuzzyDBMappings();
	}

	private void cleanInvalidFuzzyDBMappings() {
		List<BatchClassDynamicPluginConfig> allDynamicPluginProperties = batchClassDynamicPluginConfigService
				.getAllDynamicPluginProperties();

		List<BatchClassDynamicPluginConfig> invalidDynamicPluginProperties = getInvalidDocTypeMappings(allDynamicPluginProperties);

		if (invalidDynamicPluginProperties != null && !invalidDynamicPluginProperties.isEmpty()) {
			removeInvalidMappings(invalidDynamicPluginProperties);
		}
	}

	/**
	 * @param allDynamicPluginProperties
	 * @return
	 */
	private List<BatchClassDynamicPluginConfig> getInvalidDocTypeMappings(
			List<BatchClassDynamicPluginConfig> allDynamicPluginProperties) {
		List<BatchClassDynamicPluginConfig> invalidDynamicPluginProperties = null;
		for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : allDynamicPluginProperties) {
			if (batchClassDynamicPluginConfig.getName().equals("document.type")) {
				String mappedDocumentName = batchClassDynamicPluginConfig.getDescription();
				BatchClassPlugin batchClassPlugin = batchClassDynamicPluginConfig.getBatchClassPlugin();
				if (batchClassPlugin != null) {
					BatchClassModule batchClassModule = batchClassPlugin.getBatchClassModule();
					if (batchClassModule != null) {
						BatchClass batchClass = batchClassModule.getBatchClass();
						if (batchClass != null) {
							String batchClassIdentifier = batchClass.getIdentifier();
							DocumentType availableDocType = documentTypeService.getDocTypeByBatchClassAndDocTypeName(
									batchClassIdentifier, mappedDocumentName);
							if (availableDocType != null) {

								List<BatchClassDynamicPluginConfig> invalidChildPluginProperties = getInvalidFieldMappings(
										batchClassDynamicPluginConfig, availableDocType);
								if (invalidChildPluginProperties != null && !invalidChildPluginProperties.isEmpty()) {
									if (invalidDynamicPluginProperties == null) {
										invalidDynamicPluginProperties = new ArrayList<BatchClassDynamicPluginConfig>();
									}
									invalidDynamicPluginProperties.addAll(invalidChildPluginProperties);
								}
							} else {
								if (invalidDynamicPluginProperties == null) {
									invalidDynamicPluginProperties = new ArrayList<BatchClassDynamicPluginConfig>();
								}
								invalidDynamicPluginProperties.add(batchClassDynamicPluginConfig);
							}
						}
					}
				}

			}
		}
		return invalidDynamicPluginProperties;
	}

	private void removeInvalidMappings(List<BatchClassDynamicPluginConfig> invalidDynamicPluginProperties) {
		batchClassDynamicPluginConfigService.removeAllDynamicConfigs(invalidDynamicPluginProperties);
	}

	/**
	 * @param batchClassDynamicPluginConfig
	 * @param availableFieldTypes2
	 * @return
	 */
	private List<BatchClassDynamicPluginConfig> getInvalidFieldMappings(BatchClassDynamicPluginConfig batchClassDynamicPluginConfig,
			DocumentType availableFieldTypes) {
		List<BatchClassDynamicPluginConfig> mappingChildren = batchClassDynamicPluginConfig.getChildren();
		List<BatchClassDynamicPluginConfig> invalidDynamicPluginProperties = null;
		for (BatchClassDynamicPluginConfig childrenDynamicPluginConfig : mappingChildren) {
			if (childrenDynamicPluginConfig.getName().equals("field.type")) {
				boolean isMappedFieldValid = false;
				for (FieldType fieldType : availableFieldTypes.getFieldTypes()) {
					isMappedFieldValid = fieldType.getName().equals(childrenDynamicPluginConfig.getDescription());
					if (isMappedFieldValid) {
						break;
					}
				}
				if (!isMappedFieldValid) {
					if (invalidDynamicPluginProperties == null) {
						invalidDynamicPluginProperties = new ArrayList<BatchClassDynamicPluginConfig>();
					}
					invalidDynamicPluginProperties.add(childrenDynamicPluginConfig);
				}
			}

		}
		return invalidDynamicPluginProperties;
	}

	/**
	 * Assigning default roles to all batch classes
	 * 
	 */
	private void assignDefaultRolesToBatchClasses() {
		List<BatchClass> batchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();
		String defaultRoles = fetchPropertyFromPropertiesFile(DataAccessConstant.UPGRADE_PATCH_DEFAULT_BATCH_CLASS_ROLES,
				DataAccessConstant.DATA_ACCESS_DCMA_DB_PROPERTIES);
		if (defaultRoles != null) {
			String[] defaultRolesArray = defaultRoles.split(DataAccessConstant.PROPERTY_FILE_DELIMITER);

			Set<String> defaultRolesSet = new HashSet<String>(Arrays.asList(defaultRolesArray));

			for (BatchClass userBatchClasses : batchClasses) {
				List<BatchClassGroups> batchClassGroups = userBatchClasses.getAssignedGroups();
				if (batchClassGroups == null) {
					batchClassGroups = new ArrayList<BatchClassGroups>();

				}
				for (String role : defaultRolesSet) {
					boolean isDefaultRoleExists = false;
					if (!role.trim().isEmpty()) {
						for (BatchClassGroups batchClassGroup : batchClassGroups) {
							if (role.equals(batchClassGroup.getGroupName())) {
								isDefaultRoleExists = true;
								break;
							}
						}
						if (!isDefaultRoleExists) {
							BatchClassGroups defaultBatchClassGroup = new BatchClassGroups();
							defaultBatchClassGroup.setGroupName(role);
							defaultBatchClassGroup.setBatchClass(userBatchClasses);
							batchClassGroups.add(defaultBatchClassGroup);
							userBatchClasses.setAssignedGroups(batchClassGroups);
						}
					}
				}
			}
			LOG.info(DataAccessConstant.UPDATING_BATCH_CLASSES);
			for (BatchClass batchClass : batchClasses) {
				batchClassService.merge(batchClass);
			}
		}
	}

	/**
	 * This method fetches the specified property from the given property file
	 * 
	 * @param propertyName: property to be fetched
	 * @param propertyFileName: property file name
	 * @return property
	 */
	private String fetchPropertyFromPropertiesFile(String propertyName, String propertyFileName) {
		ClassPathResource classPathResource = new ClassPathResource(propertyFileName);

		FileInputStream fileInputStream = null;
		File propertyFile = null;
		String property = null;
		try {
			propertyFile = classPathResource.getFile();
			Properties properties = new Properties();
			fileInputStream = new FileInputStream(propertyFile);
			properties.load(fileInputStream);
			property = properties.getProperty(propertyName);
		} catch (IOException e) {
			LOG.error("An Exception occurred while executing the patch." + e.getMessage(), e);
		}

		return property;

	}

	/**
	 * API to set update the value of a property in property file.
	 * 
	 * @param propertyFilePath{@link String} path of property file
	 * @param propertyName{@link String} property to be updated
	 * @param updatedValue{@link String} updated value of property
	 * @param updateComment{@link String} comment to add on value update
	 */
	private void updateProperty(String propertyFilePath, String propertyName, String updatedValue, String updateComment) {
		LOG.info("Updating property:" + propertyName + " to value:" + updatedValue);
		try {
			ClassPathResource classPathResource = new ClassPathResource(propertyFilePath);
			File propertyFile = classPathResource.getFile();
			Map<String, String> propertyMap = new HashMap<String, String>();
			propertyMap.put(propertyName, updatedValue);
			FileUtils.updateProperty(propertyFile, propertyMap, updateComment);
		} catch (IOException ioException) {
			LOG.error("An Exception occurred updating the property:" + propertyName + ioException.getMessage(), ioException);
		}
	}

	public void updateModules() {

		batchClassNameVsModulesMap = readModuleSerializeFile();
		if (batchClassNameVsModulesMap == null || batchClassNameVsModulesMap.isEmpty()) {
			LOG.info("No data recovered from serialized file. Returning..");
			return;
		}

		updatePluginConfigsForNewModules();

		for (String batchClassName : batchClassNameVsModulesMap.keySet()) {

			List<BatchClass> batchClasses = nameVsBatchClassListMap.get(batchClassName);
			List<BatchClassModule> newModulesToBeAdded = batchClassNameVsModulesMap.get(batchClassName);
			if (newModulesToBeAdded != null && !newModulesToBeAdded.isEmpty()) {
				for (BatchClass batchClass : batchClasses) {
					List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
					for (BatchClassModule bcmNew : newModulesToBeAdded) {
						boolean isModulePresent = false;
						for (BatchClassModule bcm : batchClassModules) {
							if (bcmNew.getModule().getName().equalsIgnoreCase(bcm.getModule().getName())) {
								isModulePresent = true;
								break;
							}
						}

						if (!isModulePresent) {
							Module module = moduleDao.getModuleByName(bcmNew.getModule().getName());
							bcmNew.setModule(module);
							batchClass.getBatchClassModules().add(bcmNew);
						}

					}
				}
			}
			LOG.info(DataAccessConstant.UPDATING_BATCH_CLASSES);
			for (BatchClass batchClass : batchClasses) {
				batchClassService.merge(batchClass);
			}

		}

	}

	public void updatePlugin() {

		readPluginSerializeFile();

		nameVsBatchClassListMap.clear();
		populateMap();

		if (batchClassNameVsBatchClassPluginMap == null || batchClassNameVsBatchClassPluginMap.isEmpty()) {
			LOG.info("No data recovered from serialized file.");
			return;
		}

		for (String key : batchClassNameVsBatchClassPluginMap.keySet()) {
			List<BatchClassPlugin> newPlugins = batchClassNameVsBatchClassPluginMap.get(key);
			updatePluginConfigsForNewConfigs(newPlugins);
		}

		for (String key : batchClassNameVsBatchClassPluginMap.keySet()) {
			StringTokenizer pluginConfigTokens = new StringTokenizer(key, DataAccessConstant.COMMA);
			String batchClassName = null;
			String moduleName = null;
			try {
				batchClassName = pluginConfigTokens.nextToken();
				moduleName = pluginConfigTokens.nextToken();

				List<BatchClass> batchClasses = nameVsBatchClassListMap.get(batchClassName);
				List<BatchClassPlugin> newPlugins = batchClassNameVsBatchClassPluginMap.get(key);
				if (batchClasses != null) {
					for (BatchClass batchClass : batchClasses) {
						List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
						for (BatchClassModule bcm : batchClassModules) {
							if (bcm.getModule().getName().equals(moduleName)) {
								for (BatchClassPlugin newPlugin : newPlugins) {
									boolean isPluginPresent = isPluginAlreadyPresent(bcm, newPlugin.getPlugin().getPluginName());
									if (!isPluginPresent) {
										Plugin plugin = pluginDao.getPluginByName(newPlugin.getPlugin().getPluginName());
										newPlugin.setPlugin(plugin);
										bcm.getBatchClassPlugins().add(newPlugin);
									}
								}
							}
						}
					}
					LOG.info(DataAccessConstant.UPDATING_BATCH_CLASSES);
					for (BatchClass batchClass : batchClasses) {
						batchClass = batchClassService.merge(batchClass);
					}
				}
				nameVsBatchClassListMap.clear();
				populateMap();

			} catch (NoSuchElementException e) {
				LOG.error("Incomplete data specified in properties file.", e);
				batchClassNameVsBatchClassPluginMap.clear();
			} catch (NumberFormatException e) {
				LOG.error("Module Id or Plugin id is not numeric.", e);
				batchClassNameVsBatchClassPluginMap.clear();
			}
		}

		nameVsBatchClassListMap.clear();
		populateMap();
	}

	public void updateBatchClasses() {
		batchClassNameVsBatchClassMap = readBatchClassSerializeFile();
		if (batchClassNameVsBatchClassMap == null || batchClassNameVsBatchClassMap.isEmpty()) {
			LOG.info("No data recovered from serialized file. Returning..");
			return;
		}
		Map<String, BatchClass> newBatchClassNameVsBatchClassMap = new HashMap<String, BatchClass>();
		newBatchClassNameVsBatchClassMap.putAll(batchClassNameVsBatchClassMap);
		for (String batchClassName : newBatchClassNameVsBatchClassMap.keySet()) {
			List<BatchClass> batchClasses = batchClassService.getAllBatchClassesExcludeDeleted();
			for (BatchClass batchClass : batchClasses) {
				if (batchClassName.equalsIgnoreCase(batchClass.getName())) {
					batchClassNameVsBatchClassMap.remove(batchClassName);
					break;
				}
			}
		}
		if (batchClassNameVsBatchClassMap.size() > 0) {

			updatePluginConfigsForBatchClass();

			updateModuleConfigsForBatchClass();

			for (String batchClassName : batchClassNameVsBatchClassMap.keySet()) {

				BatchClass newBatchClassToBeAdded = batchClassNameVsBatchClassMap.get(batchClassName);
				ClassPathResource classPathResource = new ClassPathResource(DataAccessConstant.DCMA_BATCH_PROPERTIES);
				StringBuffer uncFolderLocation = new StringBuffer();
				FileInputStream fileInputStream = null;
				File propertyFile = null;
				try {
					propertyFile = classPathResource.getFile();
					Properties properties = new Properties();
					fileInputStream = new FileInputStream(propertyFile);
					properties.load(fileInputStream);
					uncFolderLocation.append(properties.getProperty(DataAccessConstant.BASE_FOLDER_LOCATION));
				} catch (IOException e) {
					LOG.error("Unable to retriving property file :" + e.getMessage(), e);
				} finally {
					try {
						if (fileInputStream != null) {
							fileInputStream.close();
						}
					} catch (IOException ioe) {
						if (propertyFile != null) {
							LOG.error(DataAccessConstant.PROBLEM_CLOSING_STREAM_FOR_FILE + propertyFile.getName());
						}
					}
				}

				if (newBatchClassToBeAdded != null) {
					if (createUNCFolder(uncFolderLocation, newBatchClassToBeAdded)) {
						newBatchClassToBeAdded.setVersion(INITIAL_VERSION);
						newBatchClassToBeAdded.setLastModifiedBy(null);

						// Assign super admin roles to newly added batch class.
						assignSuperAdminRoleToBatchClass(newBatchClassToBeAdded, getAllSuperAdminRoles());
						batchClassService.createBatchClassWithoutWatch(newBatchClassToBeAdded);
					}
				}
				LOG.info(DataAccessConstant.UPDATING_BATCH_CLASSES);
			}
		}
	}

	/**
	 * Returns true/false according to the value present UNC value in the database.
	 * 
	 * @param uncFolderLocation {@link StringBuffer} the default UNC folder.
	 * @param batchClass {@link BatchClass} the new batch class to be added.
	 * @return true if new batch class to be added and false otherwise.
	 */
	private boolean createUNCFolder(StringBuffer uncFolderLocation, BatchClass batchClass) {
		uncFolderLocation.append(File.separator);
		String OSIndependentUncFolderPath = FileUtils.createOSIndependentPath(batchClass.getUncFolder());
		uncFolderLocation.append((new File(OSIndependentUncFolderPath)).getName());
		boolean createBatchClass = true;
		if (batchClassService.getBatchClassbyUncFolder(uncFolderLocation.toString()) == null) {
			batchClass.setUncFolder(uncFolderLocation.toString());
			File file = new File(uncFolderLocation.toString());
			if (!file.exists()) {
				file.mkdir();
			}
		} else {
			createBatchClass = false;
		}
		return createBatchClass;
	}

	private Map<String, List<BatchClassModule>> readModuleSerializeFile() {

		FileInputStream fileInputStream = null;
		Map<String, List<BatchClassModule>> newModulesMap = null;
		File serializedFile = null;
		try {
			String moduleFilePath = dbPatchFolderLocation + File.separator + DataAccessConstant.MODULE_UPDATE + SERIALIZATION_EXT;
			serializedFile = new File(moduleFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			newModulesMap = (Map<String, List<BatchClassModule>>) SerializationUtils.deserialize(fileInputStream);
			updateFile(serializedFile, moduleFilePath);
		} catch (IOException e) {
			LOG.info(DataAccessConstant.ERROR_DURING_READING_THE_SERIALIZED_FILE + e.getMessage(), e);
		} catch (Exception e) {
			LOG.error(DataAccessConstant.ERROR_DURING_DE_SERIALIZING_THE_PROPERTIES_FOR_DATABASE_UPGRADE + e.getMessage(), e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null) {
					LOG.error(DataAccessConstant.PROBLEM_CLOSING_STREAM_FOR_FILE + serializedFile.getName());
				}
			}
		}

		return newModulesMap;
	}

	private Map<String, BatchClass> readBatchClassSerializeFile() {

		FileInputStream fileInputStream = null;
		Map<String, BatchClass> newBatchClassMap = null;
		File serializedFile = null;
		try {
			String batchClassFilePath = dbPatchFolderLocation + File.separator + DataAccessConstant.BATCH_CLASS_UPDATE
					+ SERIALIZATION_EXT;
			serializedFile = new File(batchClassFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			newBatchClassMap = (Map<String, BatchClass>) SerializationUtils.deserialize(fileInputStream);
			updateFile(serializedFile, batchClassFilePath);
		} catch (IOException e) {
			LOG.info(DataAccessConstant.ERROR_DURING_READING_THE_SERIALIZED_FILE);
		} catch (Exception e) {
			LOG.error(DataAccessConstant.ERROR_DURING_DE_SERIALIZING_THE_PROPERTIES_FOR_DATABASE_UPGRADE, e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null) {
					LOG.error(DataAccessConstant.PROBLEM_CLOSING_STREAM_FOR_FILE + serializedFile.getName());
				}
			}
		}

		return newBatchClassMap;
	}

	private void readPluginSerializeFile() {
		FileInputStream fileInputStream = null;
		File serializedFile = null;
		try {
			String pluginFilePath = dbPatchFolderLocation + File.separator + DataAccessConstant.PLUGIN_UPDATE + SERIALIZATION_EXT;
			serializedFile = new File(pluginFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			batchClassNameVsBatchClassPluginMap = (Map<String, List<BatchClassPlugin>>) SerializationUtils
					.deserialize(fileInputStream);
			updateFile(serializedFile, pluginFilePath);
		} catch (IOException e) {
			LOG.info(DataAccessConstant.ERROR_DURING_READING_THE_SERIALIZED_FILE);
		} catch (Exception e) {
			LOG.error(DataAccessConstant.ERROR_DURING_DE_SERIALIZING_THE_PROPERTIES_FOR_DATABASE_UPGRADE, e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null) {
					LOG.error(DataAccessConstant.PROBLEM_CLOSING_STREAM_FOR_FILE + serializedFile.getName());
				}
			}
		}
	}

	private List<BatchClassScannerConfiguration> readScannerConfigSerializeFile() {
		FileInputStream fileInputStream = null;
		List<BatchClassScannerConfiguration> newScannerConfigsList = null;
		File serializedFile = null;
		try {
			String scannerConfigFilePath = dbPatchFolderLocation + File.separator + DataAccessConstant.SCANNER_CONFIG_UPDATE
					+ SERIALIZATION_EXT;
			serializedFile = new File(scannerConfigFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			newScannerConfigsList = (List<BatchClassScannerConfiguration>) SerializationUtils.deserialize(fileInputStream);
			updateFile(serializedFile, scannerConfigFilePath);
		} catch (IOException e) {
			LOG.info(DataAccessConstant.ERROR_DURING_READING_THE_SERIALIZED_FILE);
		} catch (Exception e) {
			LOG.error(DataAccessConstant.ERROR_DURING_DE_SERIALIZING_THE_PROPERTIES_FOR_DATABASE_UPGRADE, e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null) {
					LOG.error(DataAccessConstant.PROBLEM_CLOSING_STREAM_FOR_FILE + serializedFile.getName());
				}
			}
		}
		return newScannerConfigsList;

	}

	private Map<String, List<BatchClassPluginConfig>> readPluginConfigSerializeFile() {
		FileInputStream fileInputStream = null;
		Map<String, List<BatchClassPluginConfig>> newPluginConfigsMap = null;
		File serializedFile = null;
		try {
			String pluginConfigFilePath = dbPatchFolderLocation + File.separator + DataAccessConstant.PLUGIN_CONFIG_UPDATE
					+ SERIALIZATION_EXT;
			serializedFile = new File(pluginConfigFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			newPluginConfigsMap = (Map<String, List<BatchClassPluginConfig>>) SerializationUtils.deserialize(fileInputStream);
			updateFile(serializedFile, pluginConfigFilePath);
		} catch (IOException e) {
			LOG.info(DataAccessConstant.ERROR_DURING_READING_THE_SERIALIZED_FILE);
		} catch (Exception e) {
			LOG.error(DataAccessConstant.ERROR_DURING_DE_SERIALIZING_THE_PROPERTIES_FOR_DATABASE_UPGRADE, e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null) {
					LOG.error(DataAccessConstant.PROBLEM_CLOSING_STREAM_FOR_FILE + serializedFile.getName());
				}
			}
		}
		return newPluginConfigsMap;
	}

	private Map<String, List<BatchClassModuleConfig>> readModuleConfigSerializeFile() {
		FileInputStream fileInputStream = null;
		Map<String, List<BatchClassModuleConfig>> newModuleConfigsMap = null;
		File serializedFile = null;
		try {
			String moduleConfigFilePath = dbPatchFolderLocation + File.separator + DataAccessConstant.MODULE_CONFIG_UPDATE
					+ SERIALIZATION_EXT;
			serializedFile = new File(moduleConfigFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			newModuleConfigsMap = (Map<String, List<BatchClassModuleConfig>>) SerializationUtils.deserialize(fileInputStream);
			updateFile(serializedFile, moduleConfigFilePath);
		} catch (IOException e) {
			LOG.info(DataAccessConstant.ERROR_DURING_READING_THE_SERIALIZED_FILE);
		} catch (Exception e) {
			LOG.error(DataAccessConstant.ERROR_DURING_DE_SERIALIZING_THE_PROPERTIES_FOR_DATABASE_UPGRADE, e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null) {
					LOG.error(DataAccessConstant.PROBLEM_CLOSING_STREAM_FOR_FILE + serializedFile.getName());
				}
			}
		}
		return newModuleConfigsMap;
	}

	public void updatePluginConfig() throws CloneNotSupportedException {
		Map<String, List<BatchClassPluginConfig>> newPluginConfigMap = new HashMap<String, List<BatchClassPluginConfig>>();
		newPluginConfigMap = readPluginConfigSerializeFile();
		if (newPluginConfigMap == null || newPluginConfigMap.isEmpty()) {
			LOG.info("No data recovered from serialized file.");
			return;
		}
		for (String pluginName : newPluginConfigMap.keySet()) {
			List<BatchClassPluginConfig> pluginConfigs = newPluginConfigMap.get(pluginName);
			if (pluginConfigs != null && !pluginConfigs.isEmpty()) {
				setUpdatedPluginConfigs(pluginConfigs);
			}
		}

		List<BatchClass> batchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();
		for (String plgName : newPluginConfigMap.keySet()) {
			for (BatchClass batchClass : batchClasses) {
				List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
				for (BatchClassModule bcm : batchClassModules) {
					List<BatchClassPlugin> batchClassPlugins = bcm.getBatchClassPlugins();
					for (BatchClassPlugin bcp : batchClassPlugins) {
						if (bcp.getPlugin().getPluginName().equals(plgName)) {
							List<BatchClassPluginConfig> toBeAddedConfigs = newPluginConfigMap.get(plgName);
							for (BatchClassPluginConfig bcpc : toBeAddedConfigs) {
								boolean isPresnt = isBatchClassPluginConfigPresent(bcpc, bcp.getBatchClassPluginConfigs());
								if (!isPresnt) {
									bcp.getBatchClassPluginConfigs().add((BatchClassPluginConfig) bcpc.clone());
								}
							}

						}
					}
				}
			}

		}

		for (BatchClass batchClass : batchClasses) {
			batchClassService.merge(batchClass);
		}
	}

	public void updateScannerConfig() {
		List<BatchClassScannerConfiguration> batchClassScannerConfigs = new ArrayList<BatchClassScannerConfiguration>();
		batchClassScannerConfigs = readScannerConfigSerializeFile();
		if (batchClassScannerConfigs == null || batchClassScannerConfigs.isEmpty()) {
			LOG.info("No Serialize file present for Scanner Configs... Returning..");
			return;
		}
		LOG.info(DataAccessConstant.UPDATING_BATCH_CLASSES);
		List<BatchClass> batchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();
		if (batchClasses != null) {
			List<BatchClassScannerConfiguration> scannerConfigForBatchClass = getScannerConfigsForBatchClasses(batchClassScannerConfigs);
			for (BatchClass batchClass : batchClasses) {
				// Checking if the profiles are already present, if present then not adding the default values.
				if (null != batchClass.getBatchClassScannerConfiguration()
						&& !batchClass.getBatchClassScannerConfiguration().isEmpty()) {
					// if profiles already present then check for updated property.
					List<BatchClassScannerConfiguration> updatedScannerConfigs = new ArrayList<BatchClassScannerConfiguration>(
							scannerConfigForBatchClass);
					List<BatchClassScannerConfiguration> scannerConfigs = batchClass.getBatchClassScannerConfiguration();
					List<BatchClassScannerConfiguration> parentProfiles = getAllParentProfilesForBatchClass(scannerConfigs);
					if (parentProfiles != null && !parentProfiles.isEmpty()) {
						for (BatchClassScannerConfiguration batchClassScannerConfig : scannerConfigs) {
							removeExistingScannerConfigs(batchClassScannerConfig, updatedScannerConfigs);
						}
						List<BatchClassScannerConfiguration> newScannerConfigs = new ArrayList<BatchClassScannerConfiguration>();
						// The remaining list of new properties to be added.
						for (BatchClassScannerConfiguration parentProfile : parentProfiles) {
							for (BatchClassScannerConfiguration updatedScannerConfig : updatedScannerConfigs) {
								BatchClassScannerConfiguration config = new BatchClassScannerConfiguration();
								config.setScannerMasterConfig(updatedScannerConfig.getScannerMasterConfig());
								config.setValue(updatedScannerConfig.getValue());
								config.setParent(parentProfile);
								newScannerConfigs.add(config);
							}
							scannerConfigs.addAll(newScannerConfigs);
							newScannerConfigs.clear();
						}
						batchClass.setBatchClassScannerConfiguration(scannerConfigs);
						batchClassService.merge(batchClass);
					}
				} else {
					batchClass.setBatchClassScannerConfiguration(scannerConfigForBatchClass);
					batchClassService.merge(batchClass);
				}
			}
		}
	}

	/**
	 * @param batchClassScannerConfigs
	 * @return
	 */
	private List<BatchClassScannerConfiguration> getScannerConfigsForBatchClasses(
			List<BatchClassScannerConfiguration> batchClassScannerConfigs) {
		List<BatchClassScannerConfiguration> updatedBatchClassScannerConfigs = new ArrayList<BatchClassScannerConfiguration>();
		for (BatchClassScannerConfiguration batchClassScannerConfig : batchClassScannerConfigs) {
			String masterScannerConfigName = batchClassScannerConfig.getScannerMasterConfig().getName();
			ScannerMasterConfiguration scannerMasterConfig = masterScannerService
					.getScannerMasterConfigForProfile(masterScannerConfigName);
			if (scannerMasterConfig != null) {
				batchClassScannerConfig.setScannerMasterConfig(scannerMasterConfig);
				updatedBatchClassScannerConfigs.add(batchClassScannerConfig);
			} else {
				LOG.error("Scanner config with name:" + masterScannerConfigName
						+ " is not present in the scanner_master_configuration table.So skipping this sacnner config.");
			}
		}
		return updatedBatchClassScannerConfigs;
	}

	private List<BatchClassScannerConfiguration> getAllParentProfilesForBatchClass(List<BatchClassScannerConfiguration> scannerConfigs) {
		List<BatchClassScannerConfiguration> scannerProfileConfigs = new ArrayList<BatchClassScannerConfiguration>();
		for (BatchClassScannerConfiguration scannerConfig : scannerConfigs) {
			if (scannerProfileConfigs == null || scannerProfileConfigs.isEmpty()) {
				if (scannerConfig.getParent() == null) {
					scannerProfileConfigs.add(scannerConfig);
				}
			}
			if (scannerConfig.getParent() != null && !scannerProfileConfigs.contains(scannerConfig.getParent())) {
				scannerProfileConfigs.add(scannerConfig.getParent());
			}
		}
		return scannerProfileConfigs;
	}

	/**
	 * To remove the property from updated list if this property is already present in existing batch class scanner configs.
	 * 
	 * @param batchClassScannerConfig {@link BatchClassScannerConfiguration} the scanner configuration in existing scanner profiles in
	 *            batch class.
	 * @param updatedScannerConfigs {@link List <BatchClassScannerConfiguration>} the list of updated scanner configurations.
	 */
	private void removeExistingScannerConfigs(BatchClassScannerConfiguration batchClassScannerConfig,
			List<BatchClassScannerConfiguration> updatedScannerConfigs) {
		if (updatedScannerConfigs != null && batchClassScannerConfig != null) {
			for (BatchClassScannerConfiguration updatedConfigs : updatedScannerConfigs) {
				if (updatedConfigs.getScannerMasterConfig().getName()
						.equalsIgnoreCase(batchClassScannerConfig.getScannerMasterConfig().getName())) {
					updatedScannerConfigs.remove(updatedConfigs);
					break;
				}
			}
		}
	}

	public void updateModuleConfigs() {
		Map<String, List<BatchClassModuleConfig>> moduleNameVsBatchClassModuleConfigs = new HashMap<String, List<BatchClassModuleConfig>>();
		moduleNameVsBatchClassModuleConfigs = readModuleConfigSerializeFile();
		if (moduleNameVsBatchClassModuleConfigs == null || moduleNameVsBatchClassModuleConfigs.isEmpty()) {
			LOG.info("No Serialize file present for Module Configs... Returning..");
			return;
		}
		for (String pluginName : moduleNameVsBatchClassModuleConfigs.keySet()) {
			List<BatchClassModuleConfig> moduleConfigs = moduleNameVsBatchClassModuleConfigs.get(pluginName);
			if (moduleConfigs != null && !moduleConfigs.isEmpty()) {
				setUpdatedModuleConfigs(moduleConfigs);
			}
		}
		List<BatchClass> batchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();
		for (String modName : moduleNameVsBatchClassModuleConfigs.keySet()) {
			for (BatchClass batchClass : batchClasses) {
				List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
				for (BatchClassModule bcm : batchClassModules) {
					if (bcm.getModule().getName().equals(modName)) {
						List<BatchClassModuleConfig> toBeAddedConfigs = moduleNameVsBatchClassModuleConfigs.get(modName);
						for (BatchClassModuleConfig bcmc : toBeAddedConfigs) {
							boolean isPresnt = isBatchClassModuleConfigPresent(bcmc, bcm.getBatchClassModuleConfig());
							if (!isPresnt) {
								if (bcm.getBatchClassModuleConfig() != null) {
									bcm.getBatchClassModuleConfig().add((BatchClassModuleConfig) bcmc);
								} else {
									List<BatchClassModuleConfig> batchClassModuleConfigs = new ArrayList<BatchClassModuleConfig>();
									batchClassModuleConfigs.add((BatchClassModuleConfig) bcmc);
									bcm.setBatchClassModuleConfig(batchClassModuleConfigs);
								}
							}
						}

					}
				}
			}
		}
		LOG.info(DataAccessConstant.UPDATING_BATCH_CLASSES);
		for (BatchClass batchClass : batchClasses) {
			batchClass = batchClassService.merge(batchClass);
		}
	}

	public void setDbPatchFolderLocation(String dbPatchFolderLocation) {
		this.dbPatchFolderLocation = dbPatchFolderLocation;
	}

	public void setPatchEnabled(boolean isPatchEnabled) {
		this.patchEnabled = isPatchEnabled;
	}

	private void populateMap() {
		List<BatchClass> batchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();
		for (BatchClass batchClass : batchClasses) {
			if (nameVsBatchClassListMap.get(batchClass.getName()) == null) {
				List<BatchClass> batchList = new ArrayList<BatchClass>();
				batchList.add(batchClass);
				nameVsBatchClassListMap.put(batchClass.getName(), batchList);
			} else {
				List<BatchClass> batchList = nameVsBatchClassListMap.get(batchClass.getName());
				batchList.add(batchClass);
			}
		}
	}

	private boolean isBatchClassPluginConfigPresent(BatchClassPluginConfig config, List<BatchClassPluginConfig> configs) {
		boolean isPresent = false;
		for (BatchClassPluginConfig batchClassPluginConfig : configs) {
			if (batchClassPluginConfig.getPluginConfig().getName().equalsIgnoreCase(config.getPluginConfig().getName())) {
				isPresent = true;
			}
		}
		return isPresent;
	}

	private void updatePluginConfigsForNewConfigs(List<BatchClassPlugin> newPlugins) {
		for (BatchClassPlugin bcp : newPlugins) {
			List<BatchClassPluginConfig> pluginConfigs = bcp.getBatchClassPluginConfigs();
			setUpdatedPluginConfigs(pluginConfigs);
			Plugin plugin = pluginDao.getPluginByName(bcp.getPlugin().getPluginName());
			if (plugin != null) {
				bcp.setPlugin(plugin);
			}
		}
	}

	private void updateModuleForNewConfigs(List<BatchClassModule> newModules) {
		for (BatchClassModule bcm : newModules) {
			List<BatchClassModuleConfig> moduleConfigs = bcm.getBatchClassModuleConfig();
			setUpdatedModuleConfigs(moduleConfigs);
			Module module = moduleDao.getModuleByName(bcm.getModule().getName());
			if (module != null) {
				bcm.setModule(module);
			}
		}
	}

	private void setUpdatedPluginConfigs(List<BatchClassPluginConfig> pluginConfigs) {
		for (BatchClassPluginConfig bcpc : pluginConfigs) {
			PluginConfig pluginConfig = dao.getPluginConfigByName(bcpc.getPluginConfig().getName());
			if (pluginConfig != null) {
				bcpc.setPluginConfig(pluginConfig);
			}
		}
	}

	private void setUpdatedModuleConfigs(List<BatchClassModuleConfig> moduleConfigs) {
		for (BatchClassModuleConfig bcmc : moduleConfigs) {
			String childKey = bcmc.getModuleConfig().getChildKey();
			if (childKey != null) {
				List<ModuleConfig> moduleConfigsList = moduleConfigDao.getModuleByChildName(childKey);
				for (ModuleConfig moduleConfig : moduleConfigsList) {
					if (moduleConfig != null) {
						bcmc.setModuleConfig(moduleConfig);
					}
				}
			} else {
				List<ModuleConfig> moduleConfigsList = moduleConfigDao.getModuleByChildName(childKey);
				for (ModuleConfig moduleConfig : moduleConfigsList) {
					if (moduleConfig != null && bcmc.getModuleConfig().isMandatory() == moduleConfig.isMandatory()) {
						bcmc.setModuleConfig(moduleConfig);
					}
				}
			}
		}
	}

	private boolean isPluginAlreadyPresent(BatchClassModule bcm, String pluginName) {
		boolean isPluginPresent = false;
		List<BatchClassPlugin> batchClassPlugins = bcm.getBatchClassPlugins();
		for (BatchClassPlugin bcp : batchClassPlugins) {
			if (bcp.getPlugin().getPluginName().equalsIgnoreCase(pluginName)) {
				isPluginPresent = true;
				break;
			}
		}
		return isPluginPresent;
	}

	private void updatePluginConfigsForNewModules() {
		for (String batchClassName : batchClassNameVsModulesMap.keySet()) {
			List<BatchClassModule> newModules = batchClassNameVsModulesMap.get(batchClassName);
			for (BatchClassModule batchClassModule : newModules) {
				List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
				for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
					Plugin plugin = pluginDao.getPluginByName(batchClassPlugin.getPlugin().getPluginName());
					if (plugin != null) {
						batchClassPlugin.setPlugin(plugin);
					}
				}
				updatePluginConfigsForNewConfigs(batchClassPlugins);
			}
		}
	}

	private void updatePluginConfigsForBatchClass() {
		for (String batchClassName : batchClassNameVsBatchClassMap.keySet()) {
			BatchClass batchClass = batchClassNameVsBatchClassMap.get(batchClassName);
			List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
			for (BatchClassModule batchClassModule : batchClassModules) {
				List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
				for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
					Plugin plugin = pluginDao.getPluginByName(batchClassPlugin.getPlugin().getPluginName());
					if (plugin != null) {
						batchClassPlugin.setPlugin(plugin);
					}
				}
				Module module = moduleDao.getModuleByName(batchClassModule.getModule().getName());
				batchClassModule.setModule(module);
				updatePluginConfigsForNewConfigs(batchClassPlugins);
			}
		}
	}

	private void updateModuleConfigsForBatchClass() {
		for (String batchClassName : batchClassNameVsBatchClassMap.keySet()) {
			BatchClass batchClass = batchClassNameVsBatchClassMap.get(batchClassName);
			List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
			for (BatchClassModule batchClassModule : batchClassModules) {
				List<BatchClassModuleConfig> batchClassModuleConfigs = batchClassModule.getBatchClassModuleConfig();
				for (BatchClassModuleConfig batchClassModuleConfig : batchClassModuleConfigs) {
					ModuleConfig moduleConfig = moduleConfigDao.get(batchClassModuleConfig.getModuleConfig().getId());
					if (moduleConfig != null) {
						batchClassModuleConfig.setModuleConfig(moduleConfig);
						batchClassModuleConfig.setId(0);
					}
				}
			}
			updateModuleForNewConfigs(batchClassModules);
		}
	}

	private void updateFile(File serialFile, String filePath) {
		try {
			File dest = new File(filePath + DataAccessConstant.EXECUTED);
			boolean renameSuccess = serialFile.renameTo(dest);
			if (renameSuccess) {
				LOG.info(serialFile.getName() + " renamed successfully to " + dest.getName());
			} else {
				LOG.debug("Unable to rename the serialize file " + serialFile.getName() + " to " + dest.getName());
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

	}

	private boolean isBatchClassModuleConfigPresent(BatchClassModuleConfig config, List<BatchClassModuleConfig> configs) {
		boolean isPresent = false;
		if (configs != null) {
			for (BatchClassModuleConfig batchClassModuleConfig : configs) {
				if (batchClassModuleConfig != null && batchClassModuleConfig.getModuleConfig() != null) {
					String childKey = batchClassModuleConfig.getModuleConfig().getChildKey();
					if (childKey != null && childKey.equalsIgnoreCase(config.getModuleConfig().getChildKey())
							&& batchClassModuleConfig.getModuleConfig().isMandatory() == config.getModuleConfig().isMandatory()) {
						isPresent = true;
						break;
					} else if (childKey == null && config.getModuleConfig() != null && null == config.getModuleConfig().getChildKey()
							&& batchClassModuleConfig.getModuleConfig().isMandatory() == config.getModuleConfig().isMandatory()) {
						isPresent = true;
						break;
					}
				}
			}
		}
		return isPresent;
	}

	private Map<String, List<Dependency>> readPluginDependenciesSerializeFile() {
		FileInputStream fileInputStream = null;
		Map<String, List<Dependency>> newPluginNameVsDependencyMap = null;
		File serializedFile = null;
		try {
			String dependenciesConfigFilePath = dbPatchFolderLocation + File.separator + DataAccessConstant.DEPENDENCY_UPDATE
					+ SERIALIZATION_EXT;
			serializedFile = new File(dependenciesConfigFilePath);
			fileInputStream = new FileInputStream(serializedFile);
			newPluginNameVsDependencyMap = (Map<String, List<Dependency>>) SerializationUtils.deserialize(fileInputStream);
			updateFile(serializedFile, dependenciesConfigFilePath);
		} catch (IOException e) {
			LOG.error(DataAccessConstant.ERROR_DURING_READING_THE_SERIALIZED_FILE);
		} catch (Exception e) {
			LOG.error(DataAccessConstant.ERROR_DURING_DE_SERIALIZING_THE_PROPERTIES_FOR_DATABASE_UPGRADE, e);
		} finally {
			try {
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				if (serializedFile != null) {
					LOG.error(DataAccessConstant.PROBLEM_CLOSING_STREAM_FOR_FILE + serializedFile.getName());
				}
			}
		}
		return newPluginNameVsDependencyMap;
	}

	public void updatePluginDependencies() {
		pluginNameVsDependencyMap = readPluginDependenciesSerializeFile();

		if (pluginNameVsDependencyMap == null || pluginNameVsDependencyMap.isEmpty()) {
			LOG.info("No data recovered from " + DataAccessConstant.DEPENDENCY_UPDATE + " Serialized file. Returning");
			return;
		}

		for (String pluginName : pluginNameVsDependencyMap.keySet()) {
			LOG.info("Checking if plugin with name: " + pluginName + " exists in db or not.");
			Plugin plugin = pluginService.getPluginPropertiesForPluginName(pluginName);
			if (plugin != null) {
				// Plugin exist in database
				LOG.info("Checking if plugin with name: " + pluginName + " exists in db.");
				List<Dependency> pluginDependenciesList = pluginNameVsDependencyMap.get(plugin.getPluginName());
				changePluginNameToId(pluginDependenciesList);
				plugin.setDependencies(pluginDependenciesList);
				LOG.info("Plugin Name: " + plugin.getPluginName() + " \t ID:" + plugin.getId());
				// pluginDao.merge(plugin);
				pluginService.mergePlugin(plugin);
			} else {
				LOG.info("Checking if plugin with name: " + pluginName + " does not exist in db.");
			}
		}

	}

	private void changePluginNameToId(List<Dependency> dependencies) {
		for (Dependency dependency : dependencies) {
			String dependenciesString = dependency.getDependencies();
			LOG.info("Dependencies name list for plugin: " + dependency.getPlugin().getPluginName() + " is: " + dependenciesString);
			dependenciesString = changeDependenciesNameToIdentifier(dependenciesString);
			dependency.setDependencies(dependenciesString);
			dependency.setPlugin(null);
		}
	}

	private String changeDependenciesNameToIdentifier(String dependencyNames) {
		LOG.info("Changing Dependency names to their identifiers");
		String[] andDependencies = dependencyNames.split(DataAccessConstant.AND);
		StringBuffer andDependenciesNameAsString = new StringBuffer();

		for (String andDependency : andDependencies) {

			if (!andDependenciesNameAsString.toString().isEmpty()) {
				andDependenciesNameAsString.append(DataAccessConstant.AND);
			}

			String[] orDependencies = andDependency.split(DataAccessConstant.OR);
			StringBuffer orDependenciesNameAsString = new StringBuffer();

			for (String dependencyName : orDependencies) {
				if (!orDependenciesNameAsString.toString().isEmpty()) {
					orDependenciesNameAsString.append(DataAccessConstant.OR);
				}
				orDependenciesNameAsString.append(pluginService.getPluginPropertiesForPluginName(dependencyName).getId());
			}

			andDependenciesNameAsString.append(orDependenciesNameAsString);
			orDependenciesNameAsString = new StringBuffer();
		}
		return andDependenciesNameAsString.toString();
	}

	/**
	 * API <code>assignSuperAdminGroups</code> to assign super admin roles to all batch classes.
	 */
	private void assignSuperAdminGroups() {

		// Getting all super admin roles.
		List<String> allSuperAdminRoles = getAllSuperAdminRoles();
		if (allSuperAdminRoles != null && !allSuperAdminRoles.isEmpty()) {

			// Getting all batch classes exclude deleted.
			List<BatchClass> allBatchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();

			if (allBatchClasses != null) {
				for (BatchClass batchClass : allBatchClasses) {

					// Assign super admin roles to each batch class
					assignSuperAdminRoleToBatchClass(batchClass, allSuperAdminRoles);

					// Updating the batch class with newly assigned groups.
					batchClassService.saveOrUpdate(batchClass);
				}
			} else {
				LOG.info("No batch classes excluding deleted found");
			}
		} else {
			LOG.info("No super-admin roles found.");
		}
	}

	/**
	 * API <code>assignSuperAdminRoleToBatchClass</code> to assign super admin roles to given batch class
	 * 
	 * @param batchClass{@link BatchClass}
	 * @param allSuperAdminRoles{@link List<String>}
	 */
	private void assignSuperAdminRoleToBatchClass(BatchClass batchClass, List<String> allSuperAdminRoles) {
		if (batchClass != null && allSuperAdminRoles != null && !allSuperAdminRoles.isEmpty()) {
			List<BatchClassGroups> assignedGroups = new ArrayList<BatchClassGroups>();
			List<BatchClassGroups> assignedGroupsToBatchClass = batchClass.getAssignedGroups();
			if (assignedGroupsToBatchClass != null && !assignedGroupsToBatchClass.isEmpty()) {
				assignedGroups.addAll(batchClass.getAssignedGroups());
			}
			String batchClassIdentifier = batchClass.getIdentifier();
			LOG.info("Adding super-admin roles to batch class: " + batchClassIdentifier);
			for (String adminRoles : allSuperAdminRoles) {
				boolean adminRoleAssigned = false;
				if (assignedGroups != null && !assignedGroups.isEmpty()) {
					for (BatchClassGroups batchClassGroups : assignedGroups) {

						// Checking if the super admin groups already assigned to the batch class.
						if (batchClassGroups.getGroupName().equals(adminRoles)) {
							adminRoleAssigned = true;
							break;
						}
					}
				}

				// If the super admin groups are not assigned to batch class, then assign those groups to batch class
				if (!adminRoleAssigned) {
					LOG.info("Adding role: " + adminRoles + " to batch class: " + batchClassIdentifier);
					BatchClassGroups batchClassGroup = new BatchClassGroups();
					batchClassGroup.setGroupName(adminRoles);
					batchClassGroup.setId(0);
					batchClassGroup.setBatchClass(null);
					assignedGroups.add(batchClassGroup);
				}
			}
			batchClass.setAssignedGroups(assignedGroups);
		}
	}

	private List<String> getAllSuperAdminRoles() {
		List<String> allSuperAdminRoles = null;
		try {
			ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			String superAdminGroups = applicationConfigProperties.getProperty(USER_SUPER_ADMIN);
			allSuperAdminRoles = getAllSuperAdminRoles(superAdminGroups);
		} catch (IOException e) {
			LOG.error("Error in fetching roles for super admin");
		}
		return allSuperAdminRoles;
	}

	/**
	 * The <code>updateBatchClassLimit</code> method is used for updating already existing batch class with null values for batch class
	 * limit.
	 */
	private void updateBatchClassLimit() {
		List<BatchClass> batchClassesList = batchClassService.getAllBatchClassesExcludeDeleted();

		// Make default entries for all the batch classes
		for (BatchClass batchClass : batchClassesList) {

			// If entry is missing form batch class cloud config table
			if (null == batchClassCloudService.getBatchClassCloudConfigByBatchClassIdentifier(batchClass.getIdentifier())) {
				BatchClassCloudConfig batchClassCloudConfig = new BatchClassCloudConfig();
				Integer userType = getUserType();

				// If user type is limited/metered then add default limits.
				if (userType.intValue() == UserType.LIMITED.getUserType()) {
					batchClassCloudConfig.setBatchInstanceLimit(ICommonConstants.DEFAULT_BATCH_INSTANCE_LIMIT);
					batchClassCloudConfig.setPageCount(ICommonConstants.DEFAULT_PAGE_COUNT_LIMIT);
					batchClassCloudConfig.setNoOfDays(ICommonConstants.DEFAULT_NO_OF_DAYS_LIMIT);
					batchClassCloudConfig.setCurrentCounter(0);
					batchClassCloudConfig.setLastReset(new Date());
				}
				batchClassCloudConfig.setBatchClass(batchClass);
				batchClassCloudService.createBatchClassCloudConfig(batchClassCloudConfig);
			}
		}

	}

	public List<String> getAllSuperAdminRoles(String superAdminGroups) {
		List<String> superAdminGroupSet = new ArrayList<String>();
		if (superAdminGroups != null && !superAdminGroups.isEmpty()) {
			String delimiter = DOUBLE_SEMICOLON_DELIMITER;
			String[] superAdmins = superAdminGroups.split(delimiter);
			if (superAdmins != null) {
				for (String superAdmin : superAdmins) {
					if (superAdmin != null && !superAdmin.isEmpty())
						superAdminGroupSet.add(superAdmin);
				}
			}
		}
		return superAdminGroupSet;
	}

	/**
	 * The <code>getUserType</code> method is used to get the user type from the properties file.
	 * 
	 * @return {@link Integer} user type
	 */
	private Integer getUserType() {
		Integer userType = null;
		try {
			userType = Integer.parseInt(ApplicationConfigProperties.getApplicationConfigProperties().getProperty(USER_TYPE).trim());
		} catch (NumberFormatException numberFormatException) {
			userType = UserType.OTHERS.getUserType();
			LOG.error("user type property is in wrong format in property file");
		} catch (IOException ioException) {
			userType = UserType.OTHERS.getUserType();
			LOG.error("user type property is missing from property file");
		}
		return userType;
	}

	/**
	 * API <code>isSuperAdminGroupUpdated</code>to get value of property 'update_super_admin_group' from application properties file
	 * 
	 * @return value of property(true or false)
	 */
	private boolean isSuperAdminGroupUpdated() {
		boolean isSuperAdminUpdated = false;
		try {
			ApplicationConfigProperties applicationConfigProperties = ApplicationConfigProperties.getApplicationConfigProperties();
			String isSuperAdminGrpUpdated = applicationConfigProperties.getProperty(DataAccessConstant.UPDATE_SUPER_ADMIN_GROUP);
			LOG.info("value of property:update_super_admin_group=" + isSuperAdminGrpUpdated);

			// Checking if the update_super_admin_group property set to true
			if (isSuperAdminGrpUpdated != null && isSuperAdminGrpUpdated.equalsIgnoreCase(Boolean.TRUE.toString())) {
				isSuperAdminUpdated = true;
			}
		} catch (IOException e) {
			LOG.error("Error in fetching super admin updation switch.");
		}

		return isSuperAdminUpdated;
	}

	/**
	 * API <code>isPasswordEncrypted</code>to get value of property 'password.encrypt' from dcma-encryption.properties file. This will
	 * define whether or not the passwords are encrypted or not.
	 * 
	 * @return value of property(true or false)
	 */
	private boolean isPasswordEncrypted() {
		boolean isPasswordEncrypted = false;
		try {
			Properties applicationConfigProperties = ApplicationConfigProperties.getApplicationConfigProperties().getAllProperties(
					DataAccessConstant.ENCRYPTION_PROPERTIES_FILE);
			String isPasswordEncryptedProperty = applicationConfigProperties
					.getProperty(DataAccessConstant.PASSWORD_ENCRYPTED_PROPERTY);
			LOG.info("value of property: password.is_encrypted=" + isPasswordEncryptedProperty);

			// Checking if the update_super_admin_group property set to true
			if (isPasswordEncryptedProperty != null && isPasswordEncryptedProperty.equalsIgnoreCase(Boolean.TRUE.toString())) {
				isPasswordEncrypted = true;
			}
		} catch (IOException e) {
			LOG.error("Error in fetching password encrytion switch.");
		}
		return isPasswordEncrypted;
	}

	/**
	 * API <code>updateCopyBatchXMLsToken</code> to upgrade COPY_BATCH_XML plugin configurations. Updated tokens are
	 * multipage_export_folder_path and multipage_export_file_name.
	 */
	private void updateCopyBatchXMLsToken() {
		LOG.info(DataAccessConstant.UPDATING_BATCH_CLASSES);
		final List<BatchClass> batchClasses = batchClassService.getAllLoadedBatchClassExcludeDeleted();
		if (batchClasses != null && !batchClasses.isEmpty()) {
			for (final BatchClass batchClass : batchClasses) {
				boolean doPersist = false;
				// Checking if the plugin is present in Batch Class, if present then only performing operations.
				final List<BatchClassPlugin> copyBatchXmlPlugins = batchClass
						.getBatchClassPluginsByPluginName(DataAccessConstant.COPY_BATCH_XML);
				if (!CollectionUtil.isEmpty(copyBatchXmlPlugins)) {
					for (final BatchClassPlugin batchClassPlugin : copyBatchXmlPlugins) {

						doPersist = updateExportFolderPath(batchClassPlugin);
						doPersist = updateExportFileName(batchClassPlugin) || doPersist;

					}
				}
				if (doPersist) {
					LOG.info(DataAccessConstant.UPDATING_BATCH_CLASSES + batchClass.getName()
							+ DataAccessConstant.WITH_COPY_BATCH_XML_PLUGIN);
					batchClassService.merge(batchClass);
				}
			}
		}
	}

	/**
	 * Update copy batch XML plugin configuration multipage_export_folder_path with 'export_to_folder/folder_name'. Old identifiers for
	 * Batch Class and Document ID are replaced with new ones.
	 * 
	 * @param the batch class plugin for which configurations are done.
	 * @return true, if successful and value needs to be merged.
	 */
	private boolean updateExportFolderPath(final BatchClassPlugin batchClassPlugin) {
		boolean doPersist = false;
		final BatchClassPluginConfig exportFolderNameConfig = batchClassPlugin
				.getBatchClassPluginConfigByName(DataAccessConstant.BATCH_FOLDER_NAME);
		final BatchClassPluginConfig finalExportFolderConfig = batchClassPlugin
				.getBatchClassPluginConfigByName(DataAccessConstant.BATCH_EXPORT_TO_FOLDER);
		final BatchClassPluginConfig exportDocFolderLocationConfig = batchClassPlugin
				.getBatchClassPluginConfigByName(DataAccessConstant.BATCH_MULTIPAGE_EXPORT_FOLDER_PATH);

		if (null != exportFolderNameConfig && null != finalExportFolderConfig && null != exportDocFolderLocationConfig) {
			String exportFolderNameConfigValue = exportFolderNameConfig.getValue();
			final String finalExportFolderValue = finalExportFolderConfig.getValue();
			String exportDocFolderLocationValue = exportDocFolderLocationConfig.getValue();
			StringBuilder builder;
			if (EphesoftStringUtil.isNullOrEmpty(exportDocFolderLocationValue)
					|| exportDocFolderLocationValue.equals(DataAccessConstant.DUMMY)) {
				if (!EphesoftStringUtil.isNullOrEmpty(exportFolderNameConfigValue)
						&& !EphesoftStringUtil.isNullOrEmpty(finalExportFolderValue)) {
					builder = new StringBuilder(exportFolderNameConfigValue);
					EphesoftStringUtil.replaceIfContains(builder, DataAccessConstant.OLD_BATCH_IDENTIFIER,
							DataAccessConstant.NEW_BATCH_IDENTIFIER);
					EphesoftStringUtil.replaceIfContains(builder, DataAccessConstant.OLD_DOC_IDENTIFIER,
							DataAccessConstant.NEW_DOC_IDENTIFIER);
					exportFolderNameConfigValue = builder.toString();
					exportDocFolderLocationValue = EphesoftStringUtil.concatenate(finalExportFolderValue, File.separator,
							exportFolderNameConfigValue);
				}
			} else {
				builder = new StringBuilder(exportDocFolderLocationValue);
				EphesoftStringUtil.replaceIfContains(builder, DataAccessConstant.OLD_BATCH_IDENTIFIER,
						DataAccessConstant.NEW_BATCH_IDENTIFIER);
				EphesoftStringUtil.replaceIfContains(builder, DataAccessConstant.OLD_DOC_IDENTIFIER,
						DataAccessConstant.NEW_DOC_IDENTIFIER);
				exportDocFolderLocationValue = builder.toString();

			}

			exportDocFolderLocationConfig.setValue(exportDocFolderLocationValue);
			doPersist = true;
		}

		return doPersist;
	}

	/**
	 * Update copy batch xml plugin configuration multipage_export_file_name with file_name. Old identifiers for Batch Class and
	 * Document ID are replaced with new ones.
	 * 
	 * @param batch class plugin for which configurations are done
	 * @return true, if successful and value needs to be merged.
	 */
	private boolean updateExportFileName(final BatchClassPlugin batchClassPlugin) {
		boolean doPersist = false;
		final BatchClassPluginConfig exportFileName = batchClassPlugin
				.getBatchClassPluginConfigByName(DataAccessConstant.BATCH_FILE_NAME);
		final BatchClassPluginConfig exportDocFileNameConfig = batchClassPlugin
				.getBatchClassPluginConfigByName(DataAccessConstant.BATCH_MULTIPAGE_EXPORT_FILE_NAME);
		if (null != exportDocFileNameConfig && null != exportFileName) {
			String exportDocFileNameConfigValue = exportDocFileNameConfig.getValue();
			StringBuilder builder = null;
			String configuredValue = null;
			String exportFileNameConfigValue = exportFileName.getValue();
			boolean isDummy = false;
			if ((EphesoftStringUtil.isNullOrEmpty(exportDocFileNameConfigValue) || exportDocFileNameConfigValue
					.equals(DataAccessConstant.DUMMY)) && !EphesoftStringUtil.isNullOrEmpty(exportFileNameConfigValue)) {
				builder = new StringBuilder(exportFileNameConfigValue);
				configuredValue = exportFileNameConfigValue;
				isDummy = true;
			} else if (!EphesoftStringUtil.isNullOrEmpty(exportDocFileNameConfigValue)) {
				builder = new StringBuilder(exportDocFileNameConfigValue);
				configuredValue = exportDocFileNameConfigValue;
			}
			if (null != builder && null != configuredValue) {
				EphesoftStringUtil.replaceIfContains(builder, DataAccessConstant.DOUBLE_AMPERSAND, DataAccessConstant.AMPERSAND);
				EphesoftStringUtil.replaceIfContains(builder, DataAccessConstant.OLD_BATCH_IDENTIFIER,
						DataAccessConstant.NEW_BATCH_IDENTIFIER);
				EphesoftStringUtil.replaceIfContains(builder, DataAccessConstant.OLD_DOC_IDENTIFIER,
						DataAccessConstant.NEW_DOC_IDENTIFIER);
				if (!builder.toString().equals(configuredValue) || isDummy) {
					exportDocFileNameConfig.setValue(builder.toString());
					doPersist = true;
				}
			}
		}
		return doPersist;
	}

}
