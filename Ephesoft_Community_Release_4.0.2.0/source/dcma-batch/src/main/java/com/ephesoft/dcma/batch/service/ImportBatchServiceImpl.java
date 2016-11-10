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

package com.ephesoft.dcma.batch.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Folders;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Folders.Folder;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Scripts.Script;
import com.ephesoft.dcma.common.ImportBatchClassResultCarrier;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.ImportExportDocument;
import com.ephesoft.dcma.core.common.ImportExportIndexField;
import com.ephesoft.dcma.core.common.KVExtractZone;
import com.ephesoft.dcma.core.common.WidgetType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.AdvancedKVExtraction;
import com.ephesoft.dcma.da.domain.AdvancedKVExtractionDetail;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassScannerConfiguration;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.Connections;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.PluginConfig;
import com.ephesoft.dcma.da.domain.ScannerMasterConfiguration;
import com.ephesoft.dcma.da.domain.TableColumnExtractionRule;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableExtractionRule;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.service.BatchClassEmailConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ConnectionsService;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.FieldTypeService;
import com.ephesoft.dcma.da.service.MasterScannerService;
import com.ephesoft.dcma.da.service.ModuleService;
import com.ephesoft.dcma.da.service.PluginConfigService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.encryption.util.EphesoftEncryptionUtil;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.OSUtil;

/**
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.ImportBatchServiceImpl
 */
public class ImportBatchServiceImpl implements ImportBatchService, ICommonConstants {

	private static final String EMPTY_STRING = "";

	private static final String SEMI_COLON = ";";

	/**
	 * String constant for db-export-mapping.properties filename.
	 */
	private static final String DB_EXPORT_MAPPING_PROPERTIES_FILE = "db-export-mapping.properties";

	/**
	 * String constant for db-export-plugin-mapping directory.
	 */
	private static final String DB_EXPORT_PLUGIN_MAPPING_DIR = "db-export-plugin-mapping";

	/**
	 * String constant for template batch class identifier.
	 */
	private static final String BASE_CLASS_IDENTIFIER = "BC1";

	/**
	 * String constant for unknown document type.
	 */
	private static final String UNKNOWN_DOC_TYPE = "Unknown";

	/**
	 * Integer constant for maximum allowable document types.
	 */
	private static final int MAX_DOC_TYPE = 100;

	/**
	 * Boolean to check if number of document types to be imported exceeds limit or not.
	 */
	boolean docTypeLimitExceed = false;

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ImportBatchServiceImpl.class);

	/** DEFAULT_RULE_OPERATOR is constant for default rule operator. */
	private static final String DEFAULT_OPERATOR = "OR";

	/**
	 * Instance of Batch Schema Service {@link BatchSchemaService}
	 */
	@Autowired
	BatchSchemaService batchSchemaService;

	/**
	 * Instance of Batch Class Service {@link BatchClassService}
	 */
	@Autowired
	BatchClassService batchClassService;

	/**
	 * Instance of Batch Instance Service {@link BatchInstanceService}
	 */
	@Autowired
	BatchInstanceService batchInstanceService;

	/**
	 * Instance of Module Service {@link ModuleService}
	 */
	@Autowired
	private ModuleService moduleService;

	/**
	 * Instance of Plugin Service {@link PluginService}
	 */
	@Autowired
	private PluginService pluginService;

	/**
	 * Instance of PluginConfig Service {@link PluginConfigService}
	 */
	@Autowired
	private PluginConfigService pluginConfigService;

	/**
	 * Instance of Batch Class Email Config Service {@link BatchClassEmailConfigService}
	 */
	@Autowired
	private BatchClassEmailConfigService bcEmailConfigService;

	@Autowired
	DocumentTypeService documentTypeService;

	@Autowired
	FieldTypeService fieldTypeService;

	@Autowired
	private ConnectionsService connectionsService;

	@Autowired
	BatchClassPluginConfigService batchClassPluginConfigService;

	/*
	 * @Autowired private BatchClassPluginService batchClassPluginService;
	 * 
	 * @Autowired private BatchClassPluginConfigService batchClassPluginConfigService;
	 */

	/**
	 * Instance of Master Scanner Service {@link MasterScannerService}
	 */
	@Autowired
	private MasterScannerService masterScannerService;

	private BatchClassPlugin getLastPluginFor(BatchClassModule previousBatchClassModule) {
		List<BatchClassPlugin> batchClassPlugins = previousBatchClassModule.getBatchClassPlugins();
		BatchClassPlugin lastBatchClassPlugin = null;
		for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
			if (lastBatchClassPlugin == null) {
				lastBatchClassPlugin = batchClassPlugin;
			} else {
				if (batchClassPlugin.getOrderNumber() > lastBatchClassPlugin.getOrderNumber()) {
					lastBatchClassPlugin = batchClassPlugin;
				}
			}
		}
		return lastBatchClassPlugin;
	}

	@Override
	public void updateBatchFolders(Properties properties, BatchInstance batchInstance, String moduleName, boolean isZipSwitchOn)
			throws Exception {

		updateBatchFolders(properties, batchInstance, moduleName, isZipSwitchOn, batchInstance.getBatchClass().getBatchClassModules());
	}

	// @Override
	// public void updateBatchFolders(Properties properties, BatchInstance
	// batchInstance, String moduleName, boolean isZipSwitchOn,
	// List<BatchClassModule> batchClassModules)
	// throws Exception {
	// // removing .SER files of deleted batch instances
	// boolean isRemoveSuccessful = removeFolders(batchInstance);
	// if (!isRemoveSuccessful) {
	// LOGGER.error("Exception in removing .SER files of batch Instance:" +
	// batchInstance.getIdentifier()
	// + ". Continuing further in deleting folders.");
	// }
	// String batchXmlExtension = ICommonConstants.UNDERSCORE_BATCH_XML;
	// if (properties != null) {
	// batchXmlExtension = properties.getProperty(INPUT_BATCH_XML);
	// }
	// File batchInstanceFolder = new File(batchInstance.getLocalFolder() +
	// File.separator + batchInstance.getIdentifier());
	// String batchXmlPath = batchInstanceFolder.getAbsolutePath() +
	// File.separator + batchInstance.getIdentifier()
	// + batchXmlExtension;
	//
	// File batchZipFile = null;
	// File batchXmlFile = null;
	// boolean isZip = false;
	// if (isZipSwitchOn) {
	// if (FileUtils.isZipFileExists(batchXmlPath)) {
	// isZip = true;
	// }
	// } else {
	// batchXmlFile = new File(batchXmlPath);
	// if (batchXmlFile.exists()) {
	// isZip = false;
	//
	// } else {
	// isZip = true;
	// }
	// }
	//
	// LOGGER.info("isZip in restarting batch is : " + isZip);
	// if (isZip) {
	// batchXmlExtension = ICommonConstants.UNDERSCORE_BATCH_XML_ZIP;
	// if (properties != null) {
	// batchXmlExtension = properties.getProperty(INPUT_BATCH_XML_ZIP);
	// }
	// String batchZipPath = batchInstanceFolder.getAbsolutePath() +
	// File.separator + batchInstance.getIdentifier()
	// + batchXmlExtension;
	// batchZipFile = new File(batchZipPath);
	// File backupXmlZipFile = new File(batchInstanceFolder.getAbsolutePath() +
	// File.separator + batchInstance.getIdentifier()
	// + ICommonConstants.UNDERSCORE_BAK_BATCH_XML_ZIP);
	// try {
	// FileUtils.copyFile(batchZipFile, backupXmlZipFile);
	// } catch (Exception e) {
	// LOGGER.error("Unable to create backup copy of batch file for batch instance : "
	// + batchInstance.getIdentifier());
	// throw new
	// Exception("Unable to create backup copy of batch file for batch instance : "
	// + batchInstance.getIdentifier());
	// }
	// } else {
	// batchXmlFile = new File(batchXmlPath);
	// File backupXmlFile = new File(batchInstanceFolder.getAbsolutePath() +
	// File.separator + batchInstance.getIdentifier()
	// + ICommonConstants.UNDERSCORE_BAK_BATCH_XML);
	// try {
	// if (batchXmlFile.exists()) {
	// FileUtils.copyFile(batchXmlFile, backupXmlFile);
	// }
	// } catch (Exception e) {
	// LOGGER.error("Unable to create backup copy of batch file for batch instance : "
	// + batchInstance.getIdentifier());
	// throw new
	// Exception("Unable to create backup copy of batch file for batch instance : "
	// + batchInstance.getIdentifier());
	// }
	//
	// }
	//
	// List<BatchClassModule> batchClassModuleList = batchClassModules;
	// BatchClassModule currentBatchClassModule = null;
	// for (BatchClassModule batchClassModule : batchClassModuleList) {
	// if (moduleName.equalsIgnoreCase(batchClassModule.getWorkflowName())) {
	// currentBatchClassModule = batchClassModule;
	// break;
	// }
	// }
	// if (currentBatchClassModule != null) {
	// BatchClassModule previousBatchClassModule = null;
	// for (BatchClassModule batchClassModule : batchClassModuleList) {
	// if (previousBatchClassModule == null && batchClassModule.getOrderNumber()
	// < currentBatchClassModule.getOrderNumber()) {
	// previousBatchClassModule = batchClassModule;
	// } else {
	// if (previousBatchClassModule != null
	// && batchClassModule.getOrderNumber() >
	// previousBatchClassModule.getOrderNumber()
	// && batchClassModule.getOrderNumber() <
	// currentBatchClassModule.getOrderNumber()) {
	// previousBatchClassModule = batchClassModule;
	// }
	// }
	// }
	//
	// String batchBakXml = ICommonConstants.UNDERSCORE_BATCH_BAK_XML;
	// if (properties != null) {
	// batchBakXml = properties.getProperty(OUTPUT_BATCH_XML);
	// }
	//
	// if (previousBatchClassModule != null) {
	// BatchClassPlugin prevBatchClassPlugin =
	// getLastPluginFor(previousBatchClassModule);
	// String prevPluginFilePath = batchInstanceFolder.getAbsolutePath() +
	// File.separator + batchInstance.getIdentifier()
	// + "_" + prevBatchClassPlugin.getPlugin().getWorkflowName() + batchBakXml;
	// File prevPluginBatchXml = new File(prevPluginFilePath);
	//
	// if (!prevPluginBatchXml.exists() &&
	// !FileUtils.isZipFileExists(prevPluginFilePath)) {
	// String prevPath = prevPluginBatchXml.getAbsolutePath();
	// prevPluginFilePath = batchInstanceFolder.getAbsolutePath() +
	// File.separator + batchInstance.getIdentifier() + "_"
	// + previousBatchClassModule.getWorkflowName() + "_" + SCRIPTING_PLUGIN +
	// batchBakXml;
	// prevPluginBatchXml = new File(prevPluginFilePath);
	// if (!prevPluginBatchXml.exists() &&
	// !FileUtils.isZipFileExists(prevPluginFilePath)) {
	// LOGGER.error("Unable to find backup batch xml for batch instance : " +
	// batchInstance.getIdentifier()
	// + "with file : " + prevPluginBatchXml.getAbsolutePath() + "or " +
	// prevPath);
	// throw new Exception("Unable to update batch xml for batch instance : " +
	// batchInstance.getIdentifier()
	// + "with file : " + prevPluginBatchXml.getAbsolutePath() + "or " +
	// prevPath);
	// }
	// }
	//
	// try {
	// if (batchZipFile != null && batchZipFile.exists()) {
	// prevPluginFilePath = prevPluginBatchXml +
	// FileType.ZIP.getExtensionWithDot();
	// FileUtils.copyFile(new File(prevPluginFilePath), batchZipFile);
	// } else {
	// FileUtils.copyFile(prevPluginBatchXml, batchXmlFile);
	// }
	// } catch (Exception e) {
	// LOGGER.error("Unable to update batch xml for batch instance : " +
	// batchInstance.getIdentifier() + "with file : "
	// + prevPluginBatchXml.getAbsolutePath());
	// throw new Exception("Unable to update batch xml for batch instance : " +
	// batchInstance.getIdentifier()
	// + "with file : " + prevPluginBatchXml.getAbsolutePath());
	// }
	// }
	// } else {
	// LOGGER.error("Could not find restart option for batch instance : " +
	// batchInstance.getIdentifier() + "restart option "
	// + moduleName);
	// throw new Exception("Could not find restart option for batch instance : "
	// + batchInstance.getIdentifier()
	// + "restart option " + moduleName);
	// }
	// }

	@Override
	public void updateBatchFolders(Properties properties, BatchInstance batchInstance, String moduleName, boolean isZipSwitchOn,
			List<BatchClassModule> batchClassModules) throws Exception {
		LOGGER.debug(EphesoftStringUtil.concatenate("Update batch folders for module: ", moduleName));

		// removing .SER files of deleted batch instances
		boolean isRemoveSuccessful = removeFolders(batchInstance);
		if (!isRemoveSuccessful) {
			LOGGER.warn("Exception in removing .SER files of batch Instance:" + batchInstance.getIdentifier()
					+ ". Continuing further in deleting folders.");
		}

		List<BatchClassModule> batchClassModuleList = batchClassModules;
		BatchClassModule currentBatchClassModule = null;
		for (BatchClassModule batchClassModule : batchClassModuleList) {
			if (moduleName.equalsIgnoreCase(batchClassModule.getWorkflowName())) {
				currentBatchClassModule = batchClassModule;
				break;
			}
		}

		// for Finding the previous executed module
		if (currentBatchClassModule != null) {
			BatchClassModule previousBatchClassModule = null;
			for (BatchClassModule batchClassModule : batchClassModuleList) {
				if (previousBatchClassModule == null && batchClassModule.getOrderNumber() < currentBatchClassModule.getOrderNumber()) {
					previousBatchClassModule = batchClassModule;
				} else {
					if (previousBatchClassModule != null
							&& batchClassModule.getOrderNumber() > previousBatchClassModule.getOrderNumber()
							&& batchClassModule.getOrderNumber() < currentBatchClassModule.getOrderNumber()) {
						previousBatchClassModule = batchClassModule;
					}
				}
			}

			if (previousBatchClassModule != null) {
				LOGGER.debug(EphesoftStringUtil.concatenate("Inside Update batch folders for module, previous batch class module: ",
						previousBatchClassModule.getWorkflowName()));
				Batch batch = batchSchemaService.getBatchFromModuleBackup(batchInstance.getIdentifier(),
						previousBatchClassModule.getWorkflowName());
				if (null == batch) {
					String errorMsg = EphesoftStringUtil.concatenate("No backup XML found for batch instance ",
							batchInstance.getIdentifier(), " for module ", previousBatchClassModule.getWorkflowName());
					LOGGER.error(errorMsg);
					throw new DCMAApplicationException(errorMsg);
				}
				batchSchemaService.updateBatchXML(batch);
			}
		} else {
			LOGGER.error("Could not find restart option for batch instance : " + batchInstance.getIdentifier() + "restart option "
					+ moduleName);
			throw new Exception("Could not find restart option for batch instance : " + batchInstance.getIdentifier()
					+ "restart option " + moduleName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.dcma.batch.service.ImportBatchService#importBatchClass(com
	 * .ephesoft.dcma.batch.schema.ImportBatchClassOptions, boolean, boolean)
	 */
	@Override
	public ImportBatchClassResultCarrier importBatchClass(ImportBatchClassOptions optionXML, boolean isDeployed,
			boolean isFromWebService, Set<String> userRolesToAssign) {
		ImportBatchClassResultCarrier importBatchClassResultCarrier = new ImportBatchClassResultCarrier();
		Map<Boolean, String> resultsMap = new HashMap<Boolean, String>();
		importBatchClassResultCarrier.setImportResults(resultsMap);
		Map<String, Connections> fuzzyConnections;
		Map<String, Connections> dbExportConnections;
		Set<Connections> uniqueConnections;
		List<Connections> connectionsToImport = null;
		String tempOutputUnZipDir = optionXML.getZipFilePath();
		File originalFolder = new File(tempOutputUnZipDir);
		String serializableFilePath = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir, FileType.SER.getExtensionWithDot());

		InputStream serializableFileStream = null;
		try {
			serializableFileStream = new FileInputStream(serializableFilePath);
			// Import Into Database from the serialized file
			BatchClass importBatchClass = (BatchClass) SerializationUtils.deserialize(serializableFileStream);

			List<DocumentType> documentTypes = importBatchClass.getDocumentTypes();
			if (documentTypes != null && !documentTypes.isEmpty()) {
				uniqueConnections = new HashSet<Connections>();
				fuzzyConnections = new HashMap<String, Connections>();
				dbExportConnections = new HashMap<String, Connections>();
				importBatchClassResultCarrier.setDbExportConnections(dbExportConnections);
				importBatchClassResultCarrier.setFuzzyConnections(fuzzyConnections);
				int orderNumber;
				List<TableColumnsInfo> tableColumnsInfos = null;
				for (DocumentType currentDocumentType : documentTypes) {
					if (null != currentDocumentType) {
						Connections dbExportConnection = currentDocumentType.getDbExportConnection();
						Connections fuzzyConnection = currentDocumentType.getFuzzyConnection();
						if (null != dbExportConnection) {
							uniqueConnections.add(dbExportConnection);
							dbExportConnections.put(currentDocumentType.getName(), dbExportConnection);
							currentDocumentType.setDbExportConnection(null);
						}
						if (null != fuzzyConnection) {
							uniqueConnections.add(fuzzyConnection);
							fuzzyConnections.put(currentDocumentType.getName(), fuzzyConnection);
							currentDocumentType.setFuzzyConnection(null);
						}
					}
					List<TableInfo> tableInfos = currentDocumentType.getTableInfos();
					List<FieldType> fieldTypes = currentDocumentType.getFieldTypes();
					if (tableInfos != null && !tableInfos.isEmpty()) {
						for (TableInfo currentTableInfo : tableInfos) {
							if (currentTableInfo.getNumberOfRows() == 0) {
								currentTableInfo.setNumberOfRows(DEFAULT_NUMBER_OF_ROWS);
							}

							// default rule operator added on importing a batch
							// class if null.
							if (currentTableInfo.getRuleOperator() == null) {
								currentTableInfo.setRuleOperator(DEFAULT_OPERATOR);
							}

							// Adds incremental order
							orderNumber = BatchConstants.IMPORT_BATCH_STARTING_ORDER;
							tableColumnsInfos = currentTableInfo.getTableColumnsInfo();
							if (!(CollectionUtil.isEmpty(tableColumnsInfos))) {
								for (final TableColumnsInfo tableColumnsInfo : tableColumnsInfos) {
									tableColumnsInfo.setOrderNo(orderNumber++);
								}
							}
						}
					}
					setTableExtractionRules(tableInfos);
					setTableColumnExtractionRules(tableInfos);
					setTableColumnInfosToColumnExtractionRules(tableInfos);

					// changes made for copying the value of multiplier into
					// weight for old batch classes.
					if (null != fieldTypes && !fieldTypes.isEmpty()) {
						// Order for KV's was always 0. Needs to be auto
						// incremented.
						int order;
						for (FieldType fieldType : fieldTypes) {

							// changes made for adding default validation
							// operator on importing a batch class.
							if (fieldType.getWidgetType() == null && fieldType.getFieldOptionValueList() != null
									&& !fieldType.getFieldOptionValueList().trim().equals(EMPTY_STRING)) {
								fieldType.setWidgetType(WidgetType.COMBO);
							}
							if (fieldType.getRegexListingSeparator() == null) {
								fieldType.setRegexListingSeparator(DEFAULT_OPERATOR);
							}
							order = BatchConstants.IMPORT_BATCH_KV_STARTING_ORDER;
							List<KVExtraction> kvExtractions = fieldType.getKvExtraction();
							if (null != kvExtractions && !kvExtractions.isEmpty()) {
								for (KVExtraction kvExtraction : kvExtractions) {
									Float multiplier = kvExtraction.getMultiplier();
									if (null != multiplier) {
										kvExtraction.setWeight(multiplier);
									}
									// Adding advancedKVExtractionDetail if it
									// does not exist in advancedKVExtraction.
									AdvancedKVExtraction advancedKVExtraction = kvExtraction.getAdvancedKVExtraction();
									if (null != advancedKVExtraction) {
										List<AdvancedKVExtractionDetail> details = advancedKVExtraction
												.getAdvancedKVExtractionDetail();
										if (null == details || details.isEmpty()) {
											AdvancedKVExtractionDetail advancedKVExtractionDetail = new AdvancedKVExtractionDetail();
											advancedKVExtractionDetail.setMultiPage(false);
											advancedKVExtractionDetail.setPageCount(1);
											advancedKVExtractionDetail.setFileName(advancedKVExtraction.getSelectedImageName());
											advancedKVExtractionDetail.setAdvancedKVExtraction(advancedKVExtraction);
											advancedKVExtraction.addModule(advancedKVExtractionDetail);
										}
									}
									setDefaultValueKVExtraction(kvExtraction);
									kvExtraction.setOrderNo(order++);
								}
							}
						}
					}
				}
				connectionsToImport = importConnections(uniqueConnections, optionXML.isImportConnections());
				if (!CollectionUtil.isEmpty(connectionsToImport)) {
					connectionsService.mergeConnections(connectionsToImport);
				}
			}
			// delete serialization file from folder and create test-extraction
			// folder
			new File(serializableFilePath).delete();
			new File(tempOutputUnZipDir + File.separator + batchSchemaService.getTestKVExtractionFolderName()).mkdir();
			new File(tempOutputUnZipDir + File.separator + batchSchemaService.getTestTableFolderName()).mkdir();

			// if from web service, implement workflow content equality check
			if (isFromWebService) {
			}

			final Boolean isAdvanceBatchClass = importBatchClass.isAdvancedBatchClass();
			if (optionXML.getName() == null || optionXML.getName().isEmpty()) {
				optionXML.setName(importBatchClass.getName());
			}
			// Setting batch class creation date as current time.
			Date currentDate = new Date(System.currentTimeMillis());
			importBatchClass.setCreationDate(currentDate);

			// bug fix for description not being overridden during importing on
			// existing batch class.
			if (EphesoftStringUtil.isNullOrEmpty(optionXML.getDescription())) {
				optionXML.setDescription(importBatchClass.getDescription());
			}
			if (optionXML.isUseExisting()) {
				importBatchClass = overrideExistingBatchClass(resultsMap, optionXML, tempOutputUnZipDir, originalFolder,
						importBatchClass, userRolesToAssign);
			} else {
				importBatchClass = importNewBatchClass(resultsMap, optionXML, tempOutputUnZipDir, originalFolder,
						serializableFileStream, importBatchClass, userRolesToAssign);
			}
			String batchClassId = null;
			if (null != importBatchClass && !resultsMap.containsKey(false)) {
				batchClassId = importBatchClass.getIdentifier();

				if (isAdvanceBatchClass == null || !isAdvanceBatchClass) {
					LOGGER.debug("Imported batch class is of older version. Copying new files inside batch class folder.");
					final boolean copyKVLearningFolderSuccessful = copyKVLearningFolder(batchClassId);
					LOGGER.debug(EphesoftStringUtil.concatenate("Copy of kv learning folder operation status : ",
							copyKVLearningFolderSuccessful));
					if (!copyKVLearningFolderSuccessful) {
						LOGGER.error("Unable to copy kv learning folder. Some problem occured.");
					}

				}
			}

			final String baseFolderLocation = batchSchemaService.getBaseFolderLocation();

			// Changed to make deletion of only serialised file containing batch
			// class data.
			String batchClassSerializedFileName = EphesoftStringUtil.concatenate(baseFolderLocation, File.separator, batchClassId,
					File.separator, batchClassId, FileType.SER.getExtensionWithDot());
			File serializedFile = new File(batchClassSerializedFileName);
			if (serializedFile.exists()) {
				serializedFile.delete();
			}

		} catch (final Exception exception) {
			final String errorMessg = EphesoftStringUtil.concatenate("Error while importing. ", exception.getMessage());
			resultsMap.put(false, errorMessg);
			LOGGER.error(errorMessg, exception);
		}
		return importBatchClassResultCarrier;
	}

	private List<Connections> importConnections(Set<Connections> uniqueConnections, Boolean importConnections) {
		List<Connections> connectionsToImport = null;
		if (!CollectionUtil.isEmpty(uniqueConnections)) {
			connectionsToImport = new ArrayList<Connections>();
			List<Connections> existingConnections = connectionsService.getAllConnectionsExcludingDeleted();
			for (Connections connection : uniqueConnections) {
				if (CollectionUtil.isEmpty(existingConnections)) {
					connectionsToImport.add(connection);
				} else {
					boolean alreadyPresent = isConnectionAlreadyPresent(connection, existingConnections);
					if (alreadyPresent && importConnections) {
						addTimeStampToConnectionName(connection);
						connectionsToImport.add(connection);
					} else if (!alreadyPresent) {
						connectionsToImport.add(connection);
					}
				}
			}
		}
		return connectionsToImport;
	}

	private void addTimeStampToConnectionName(Connections connection) {
		connection.setConnectionName(connection.getConnectionName() + System.currentTimeMillis());
	}

	private boolean isConnectionAlreadyPresent(Connections connection, List<Connections> existingConnections) {
		boolean alreadyPresent = false;
		for (Connections existingConnection : existingConnections) {
			if (existingConnection.getConnectionName().equalsIgnoreCase(connection.getConnectionName())) {
				alreadyPresent = true;
				break;
			}
		}
		return alreadyPresent;
	}

	private void setTableColumnInfosToColumnExtractionRules(List<TableInfo> tableInfos) {
		List<TableColumnsInfo> tableColumnInfos = null;
		List<TableExtractionRule> tableExtractionRules = null;
		List<TableColumnExtractionRule> colExtrRules = null;
		if (!CollectionUtil.isEmpty(tableInfos)) {
			for (TableInfo tableInfo : tableInfos) {
				tableColumnInfos = tableInfo.getTableColumnsInfo();
				tableExtractionRules = tableInfo.getTableExtractionRules();
				if (!CollectionUtil.isEmpty(tableExtractionRules) && !CollectionUtil.isEmpty(tableColumnInfos)) {
					for (TableExtractionRule tableExtractionRule : tableExtractionRules) {
						colExtrRules = tableExtractionRule.getTableColumnExtractionRules();
						if (!CollectionUtil.isEmpty(colExtrRules)) {
							for (TableColumnExtractionRule colExtrRule : colExtrRules) {
								setTableColumnInfoToColumnExtractionRule(colExtrRule, tableColumnInfos);
							}

						}
					}
				}
			}
		}
	}

	private void setTableColumnInfoToColumnExtractionRule(TableColumnExtractionRule colExtrRule,
			List<TableColumnsInfo> tableColumnInfos) {
		String columnName = null;
		String tableColumnName = null;
		if (colExtrRule != null && !CollectionUtil.isEmpty(tableColumnInfos)) {
			columnName = colExtrRule.getColumnName();
			for (TableColumnsInfo tableColumnInfo : tableColumnInfos) {
				tableColumnName = tableColumnInfo.getColumnName();
				if (!EphesoftStringUtil.isNullOrEmpty(columnName) && !EphesoftStringUtil.isNullOrEmpty(tableColumnName)
						&& columnName.trim().equalsIgnoreCase(tableColumnName.trim())) {
					colExtrRule.setTableColumnInfo(tableColumnInfo);
					break;
				}
			}
		}
	}

	/**
	 * Sets table column extraction rules for all tables.
	 * 
	 * @param tableInfos {@link List}<{@link TableInfo}>
	 */
	private void setTableColumnExtractionRules(final List<TableInfo> tableInfos) {
		LOGGER.trace("Setting table column extraction rules for importing batch class.");
		if (!CollectionUtil.isEmpty(tableInfos)) {
			for (TableInfo tableInfo : tableInfos) {
				if (tableInfo != null && !EphesoftStringUtil.isNullOrEmpty(tableInfo.getTableExtractionAPI())) {
					populateTableColumnExtractionDetails(tableInfo);
					tableInfo.setTableExtractionAPI(null);
				}
			}
		}
	}

	/**
	 * Sets table column extraction rules for a table.
	 * 
	 * @param tableInfo {@link TableInfo}
	 */
	private void populateTableColumnExtractionDetails(final TableInfo tableInfo) {
		if (null != tableInfo) {
			LOGGER.info("Populating table column extraction rules for table info: ", tableInfo.getName());
			final List<TableColumnsInfo> tableColumnsInfos = tableInfo.getTableColumnsInfo();
			List<TableExtractionRule> tableExtractionRules = tableInfo.getTableExtractionRules();
			if (tableExtractionRules == null) {
				tableExtractionRules = new ArrayList<TableExtractionRule>();
				tableInfo.setTableExtractionRules(tableExtractionRules);
			}
			setTableColumnExtractionRulesToExtractionRules(tableColumnsInfos, tableExtractionRules);
		}
	}

	/**
	 * Sets a table column extraction rule for all table info's column into all table extraction rules.
	 * 
	 * @param tableColumnsInfos {@link List}<{@link TableColumnsInfo}>
	 * @param tableExtractionRules {@link List}<{@link TableExtractionRule}>
	 */
	private void setTableColumnExtractionRulesToExtractionRules(final List<TableColumnsInfo> tableColumnsInfos,
			final List<TableExtractionRule> tableExtractionRules) {
		if (!CollectionUtil.isEmpty(tableColumnsInfos) && !CollectionUtil.isEmpty(tableExtractionRules)) {
			for (TableColumnsInfo tableColumnsInfo : tableColumnsInfos) {
				if (null != tableColumnsInfo && null != tableColumnsInfo.getColumnName()) {
					for (TableExtractionRule tableExtractionRule : tableExtractionRules) {
						LOGGER.info("Populating table column extraction rules for table extraction rules: ",
								tableExtractionRule.getRuleName());
						setTableColumnExtractionRule(tableColumnsInfo, tableExtractionRule);
					}
				}
			}
		}
	}

	/**
	 * Sets a table column extraction rule for a table info's column into a table extraction rule.
	 * 
	 * @param tableColumnsInfo {@link TableColumnsInfo}
	 * @param tableExtractionRule {@link TableExtractionRule}
	 */
	private void setTableColumnExtractionRule(final TableColumnsInfo tableColumnsInfo, final TableExtractionRule tableExtractionRule) {
		if (null != tableExtractionRule && null != tableColumnsInfo) {
			TableInfo tableColumnTableInfo = tableColumnsInfo.getTableInfo();
			TableInfo tableExtractionRuleTableInfo = tableExtractionRule.getTableInfo();
			if (null != tableExtractionRuleTableInfo && null != tableColumnTableInfo
					&& tableExtractionRuleTableInfo.getId() == tableColumnTableInfo.getId()) {
				List<TableColumnExtractionRule> listColumnExtractionRules = tableExtractionRule.getTableColumnExtractionRules();
				if (null == listColumnExtractionRules) {
					listColumnExtractionRules = new ArrayList<TableColumnExtractionRule>();
					tableExtractionRule.setTableColumnExtractionRules(listColumnExtractionRules);
				}
				final TableColumnExtractionRule tableColumnExtractionRule = createTableColumnExtractionRuleFromOldTableColumn(tableColumnsInfo);
				listColumnExtractionRules.add(tableColumnExtractionRule);

				// Setting null/false to old fields of table columns.
				tableColumnsInfo.setBetweenLeft(null);
				tableColumnsInfo.setBetweenRight(null);
				tableColumnsInfo.setColumnPattern(null);
				tableColumnsInfo.setExtractedDataColumnName(null);
				tableColumnsInfo.setRequired(false);
				tableColumnsInfo.setMandatory(false);
				tableColumnsInfo.setCurrency(false);
				tableColumnsInfo.setColumnHeaderPattern(null);
				tableColumnsInfo.setColumnStartCoordinate(null);
				tableColumnsInfo.setColumnEndCoordinate(null);
				tableColumnsInfo.setColumnCoordinateY0(null);
				tableColumnsInfo.setColumnCoordinateY1(null);
			}
		}
	}

	/**
	 * Extract old table column extraction rule details from table column info to a table column extraction rule.
	 * 
	 * @param tableInfo {@link TableColumnsInfo}
	 * @return {@link TableColumnExtractionRule}
	 */
	private TableColumnExtractionRule createTableColumnExtractionRuleFromOldTableColumn(final TableColumnsInfo tableColumnsInfo) {
		TableColumnExtractionRule tableColumnExtractionRule = null;
		if (null != tableColumnsInfo) {
			LOGGER.trace("Creating table column extraction rules from old table columns for importing batch class.");
			tableColumnExtractionRule = new TableColumnExtractionRule();
			tableColumnExtractionRule.setBetweenLeft(tableColumnsInfo.getBetweenLeft());
			tableColumnExtractionRule.setBetweenRight(tableColumnsInfo.getBetweenRight());
			tableColumnExtractionRule.setColumnPattern(tableColumnsInfo.getColumnPattern());
			tableColumnExtractionRule.setExtractedDataColumnName(tableColumnsInfo.getExtractedDataColumnName());
			tableColumnExtractionRule.setRequired(tableColumnsInfo.isRequired());
			tableColumnExtractionRule.setMandatory(tableColumnsInfo.isMandatory());
			tableColumnExtractionRule.setCurrency(tableColumnsInfo.isCurrency());
			tableColumnExtractionRule.setColumnHeaderPattern(tableColumnsInfo.getColumnHeaderPattern());
			tableColumnExtractionRule.setColumnStartCoordinate(tableColumnsInfo.getColumnStartCoordinate());
			tableColumnExtractionRule.setColumnEndCoordinate(tableColumnsInfo.getColumnEndCoordinate());
			tableColumnExtractionRule.setColumnCoordinateY0(tableColumnsInfo.getColumnCoordinateY0());
			tableColumnExtractionRule.setColumnCoordinateY1(tableColumnsInfo.getColumnCoordinateY1());
			tableColumnExtractionRule.setCreationDate(tableColumnsInfo.getCreationDate());
			tableColumnExtractionRule.setEntityState(tableColumnsInfo.getEntityState());
			tableColumnExtractionRule.setLastModified(tableColumnsInfo.getLastModified());
			tableColumnExtractionRule.setColumnName(tableColumnsInfo.getColumnName());
			tableColumnExtractionRule.setTableColumnInfo(tableColumnsInfo);
		}
		LOGGER.info("Table column extraction rule for column : ", tableColumnExtractionRule.getColumnName(),
				" is successfully created.");
		return tableColumnExtractionRule;
	}

	/**
	 * Sets table extraction rules for a table info.
	 * 
	 * @param tableInfos {@link List}<{@link TableInfo}>
	 */
	private void setTableExtractionRules(final List<TableInfo> tableInfos) {
		if (!CollectionUtil.isEmpty(tableInfos)) {
			LOGGER.trace("Setting table extraction rules for importing batch class.");
			for (final TableInfo tableInfo : tableInfos) {
				if (null != tableInfo && null != tableInfo.getTableExtractionAPI()) {
					List<TableExtractionRule> tableExtractionRuleInfos = tableInfo.getTableExtractionRules();
					if (null == tableExtractionRuleInfos) {
						tableExtractionRuleInfos = new ArrayList<TableExtractionRule>();
						tableInfo.setTableExtractionRules(tableExtractionRuleInfos);
					}
					final TableExtractionRule tableExtractionRule = getTableExtractionRuleFromOldTableInfo(tableInfo);
					tableInfo.setStartPattern(null);
					tableInfo.setEndPattern(null);
					tableExtractionRuleInfos.add(tableExtractionRule);
				}
			}
		}
	}

	/**
	 * Extract old table extraction rule details from table info to a table extraction rule.
	 * 
	 * @param tableInfo {@link TableInfo}
	 * @return {@link TableExtractionRule}
	 */
	private TableExtractionRule getTableExtractionRuleFromOldTableInfo(final TableInfo tableInfo) {
		TableExtractionRule tableExtractionRule = null;
		if (null != tableInfo) {
			LOGGER.trace("Creating table extraction rules from old table data for importing batch class.");
			tableExtractionRule = new TableExtractionRule();
			tableExtractionRule.setCreationDate(tableInfo.getCreationDate());
			tableExtractionRule.setEntityState(tableInfo.getEntityState());
			tableExtractionRule.setLastModified(tableInfo.getLastModified());
			tableExtractionRule.setEndPattern(tableInfo.getEndPattern());
			tableExtractionRule.setStartPattern(tableInfo.getStartPattern());
			tableExtractionRule.setTableAPI(tableInfo.getTableExtractionAPI());
			tableExtractionRule.setRuleName(BatchConstants.IMPORT_BATCH_TABLE_EXTRACTION_RULE_NAME);
			tableExtractionRule.setTableInfo(tableInfo);
		}
		LOGGER.info("Table extraction rule with name : ", BatchConstants.IMPORT_BATCH_TABLE_EXTRACTION_RULE_NAME,
				" is successfully created.");
		return tableExtractionRule;
	}

	/**
	 * Method to import a new batch class.
	 * 
	 * @param resultsImport
	 * @param optionXML
	 * @param tempOutputUnZipDir
	 * @param originalFolder
	 * @param serializableFileStream
	 * @param importBatchClass
	 * @param userRolesToAssign
	 * @return BatchClass object reference or null
	 * @throws IOException
	 */
	private BatchClass importNewBatchClass(Map<Boolean, String> resultsImport, ImportBatchClassOptions optionXML,
			String tempOutputUnZipDir, File originalFolder, InputStream serializableFileStream, BatchClass importBatchClass,
			Set<String> userRolesToAssign) throws IOException {
		// create a new batch class from zip file
		String errorMessg = "";
		boolean isSuccess = true;
		resultsImport.put(isSuccess, errorMessg);
		try {
			importBatchClass.setId(0L);
			List<ImportBatchClassOptions.BatchClassDefinition> bcdList = optionXML.getBatchClassDefinition();
			BatchClassDefinition bcd = bcdList.get(0);
			if (!optionXML.isRolesImported()) {
				// do not import the roles
				importBatchClass.getAssignedGroups().clear();
			}

			// checking if Email Accounts Properties check box is checked or
			// not.
			if (!optionXML.isEmailAccounts()) {
				// do not import the Email Configurations.
				importBatchClass.getEmailConfigurations().clear();
			}

			for (Folder folder : bcd.getFolders().getFolder()) {
				if (batchSchemaService.getImagemagickBaseFolderName().equalsIgnoreCase(folder.getFileName())) {
					if (!folder.isSelected()) {
						// do not import the image samples
						String imagemagickBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir,
								batchSchemaService.getImagemagickBaseFolderName());
						FileUtils.deleteDirectoryAndContentsRecursive(new File(imagemagickBaseFolder));
					}
				}
				if (batchSchemaService.getSearchSampleName().equalsIgnoreCase(folder.getFileName())) {
					if (!folder.isSelected()) {
						// do not import the lucene samples
						String searchSampleFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir,
								batchSchemaService.getSearchSampleName());
						FileUtils.deleteDirectoryAndContentsRecursive(new File(searchSampleFolder));
					}
				}

			}
			setCurrentUserRoleToBatchClass(importBatchClass, userRolesToAssign);
			if (!optionXML.isUseSource()) {
				importBatchClass.setDescription(optionXML.getDescription());
				importBatchClass.setName(optionXML.getName());
				importBatchClass.setPriority(optionXML.getPriority());
			}
			new File(optionXML.getUncFolder()).mkdirs();
			importBatchClass.setUncFolder(optionXML.getUncFolder());
			importBatchClass.setAdvancedBatchClass(true);
			try {
				BatchClass dbBatchClass = batchClassService.getLoadedBatchClassByNameIncludingDeleted(optionXML.getName());
				if (dbBatchClass != null) {
					batchClassService.evict(dbBatchClass);
				}
				updateSerializableBatchClass(dbBatchClass, importBatchClass);
				batchClassService.evict(importBatchClass);
				importBatchClass = batchClassService.createBatchClass(importBatchClass);
				resultsImport.put(Boolean.TRUE, importBatchClass.getIdentifier());
			} catch (Exception exception) {
				errorMessg = "Unable to create/override batch Class." + exception.getMessage();
				LOGGER.error(errorMessg, exception);
				isSuccess = false;
				resultsImport.put(isSuccess, errorMessg);
				new File(optionXML.getUncFolder()).delete();
			}

			File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator + importBatchClass.getIdentifier());

			FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);

			// To create export to image import plugin mapping folder in a batch
			// class not containing it at the time of export.
			String exportToImageImportMappingFolderPath = EphesoftStringUtil.concatenate(copiedFolder, File.separator,
					batchSchemaService.getExportToImageImportPluginMappingFolderName());
			if (!EphesoftStringUtil.isNullOrEmpty(exportToImageImportMappingFolderPath)) {
				File exportToImageImportMappingFolder = new File(exportToImageImportMappingFolderPath);
				if (!exportToImageImportMappingFolder.exists()) {
					exportToImageImportMappingFolder.mkdirs();
					LOGGER.info("Created exportToImageExport Mapping folder successfully for importing batch class with id: ",
							importBatchClass.getIdentifier());
				}
			}

		} catch (FileNotFoundException e) {
			errorMessg = "Serializable file not found." + e.getMessage();
			LOGGER.error(errorMessg, e);
			isSuccess = false;
			resultsImport.put(isSuccess, errorMessg);
			new File(optionXML.getUncFolder()).delete();
		} catch (IOException e) {
			errorMessg = "Unable to copy the learning folders." + e.getMessage();
			LOGGER.error(errorMessg, e);
			isSuccess = false;
			resultsImport.put(isSuccess, errorMessg);
			new File(optionXML.getUncFolder()).delete();
		} finally {
			if (serializableFileStream != null) {
				serializableFileStream.close();
			}
			FileUtils.deleteDirectoryAndContentsRecursive(originalFolder);
			new File(optionXML.getZipFilePath()).delete();
		}
		return importBatchClass;
	}

	/**
	 * This API sets first role of current user to this import batch class.
	 * 
	 * @param importBatchClass
	 * @param userRolesToAssign
	 */
	private void setCurrentUserRoleToBatchClass(BatchClass importBatchClass, Set<String> userRolesToAssign) {
		if (userRolesToAssign != null) {
			List<BatchClassGroups> batchClassGroupsList = importBatchClass.getAssignedGroups();
			for (String roleToAssign : userRolesToAssign) {
				boolean validRoleToAdd = true;
				for (BatchClassGroups batchClassGroup : batchClassGroupsList) {
					if (batchClassGroup.getGroupName().equals(roleToAssign)) {
						validRoleToAdd = false;
						break;
					}

				}
				if (validRoleToAdd) {
					BatchClassGroups batchClassGroup = new BatchClassGroups();
					batchClassGroup.setGroupName(roleToAssign);
					batchClassGroup.setId(0);
					batchClassGroup.setBatchClass(null);
					batchClassGroupsList.add(batchClassGroup);
				}
			}
			importBatchClass.setAssignedGroups(batchClassGroupsList);
		}
	}

	/**
	 * Method to override the existing batch class
	 * 
	 * @param resultsImport
	 * @param optionXML
	 * @param tempOutputUnZipDir
	 * @param originalFolder
	 * @param importBatchClass
	 * @param userRolesToAssign
	 * @return BatchClass object reference or null
	 * @throws Exception
	 */
	private BatchClass overrideExistingBatchClass(Map<Boolean, String> resultsImport, ImportBatchClassOptions optionXML,
			final String tempOutputUnZipDir, File originalFolder, BatchClass importBatchClass, Set<String> userRolesToAssign)
			throws Exception {

		// overriding a batch class
		BatchClass existingBatchClass = batchClassService.getLoadedBatchClassByUNC(optionXML.getUncFolder());
		batchClassService.evict(existingBatchClass);
		List<String> scriptsList = new ArrayList<String>();
		if (existingBatchClass != null) {
			// check the workflow name with UNC folder
			/*
			 * if (!existingBatchClass.getName().equalsIgnoreCase(importBatchClass .getName())) { logger.error(
			 * "Returning from import batch service with error. Mismatching workflow type from zip and from UNC folder specified. Zip workflow name:"
			 * + importBatchClass.getName() + ". UNC workflow name:" + existingBatchClass.getName()); isSuccess = false; return
			 * isSuccess; }
			 */

			String existingUncFolder = existingBatchClass.getUncFolder();
			setCurrentUserRoleToBatchClass(importBatchClass, userRolesToAssign);
			// update the configurations of imported batch class from DB / Zip
			if (!optionXML.isRolesImported()) {
				// import the roles from database
				updateRoles(importBatchClass, existingBatchClass, Boolean.TRUE);
			}
			if (!optionXML.isEmailAccounts()) {
				updateEmailConfigurations(importBatchClass, existingBatchClass, Boolean.TRUE);
			}

			List<ImportBatchClassOptions.BatchClassDefinition> bcdList = optionXML.getBatchClassDefinition();
			BatchClassDefinition batchClassDefinition = bcdList.get(0);
			if (batchClassDefinition.getScripts() != null && !batchClassDefinition.getScripts().getScript().isEmpty()) {
				scriptsList = updateScriptsFiles(tempOutputUnZipDir, batchClassDefinition.getScripts().getScript(),
						existingBatchClass.getIdentifier());
			}

			if (batchClassDefinition.getFolders() != null && !batchClassDefinition.getFolders().getFolder().isEmpty()) {
				for (Folder folder : batchClassDefinition.getFolders().getFolder()) {
					if (!folder.isSelected()) {
						// import from database
						String zipBaseFolder = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir, folder.getFileName());
						String existingBaseFolder = batchSchemaService.getAbsolutePath(existingBatchClass.getIdentifier(),
								folder.getFileName(), false);
						overrideFromDB(resultsImport, zipBaseFolder, existingBaseFolder, tempOutputUnZipDir);
						String errorMessgImport = resultsImport.get(Boolean.FALSE);
						if (errorMessgImport != null && !errorMessgImport.isEmpty()) {
							return null;
						}
					}
				}
			}
			if (batchClassDefinition.getBatchClassModules() != null
					&& !batchClassDefinition.getBatchClassModules().getBatchClassModule().isEmpty()) {
				for (com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule bcm : batchClassDefinition
						.getBatchClassModules().getBatchClassModule()) {
					updatePlgConfig(resultsImport, bcm, importBatchClass, existingBatchClass);
					String errorMessgImport = resultsImport.get(Boolean.FALSE);
					if (errorMessgImport != null && !errorMessgImport.isEmpty()) {
						return null;
					}
				}
			}

			boolean isSuccess = !resultsImport.containsKey(false);
			String errorMessg = "";
			if (isSuccess) {
				// override the existing batch class
				importBatchClass.setId(0L);
				importBatchClass.setUncFolder("dummyUncFolder" + System.currentTimeMillis());
				importBatchClass.setAdvancedBatchClass(true);
				if (!optionXML.isUseSource()) {
					importBatchClass.setDescription(optionXML.getDescription());
					importBatchClass.setName(optionXML.getName());
					importBatchClass.setPriority(optionXML.getPriority());
				}
			}
			try {
				updateSerializableBatchClass(existingBatchClass, importBatchClass);
				importBatchClass = batchClassService.createBatchClassWithoutWatch(importBatchClass);
				batchClassService.evict(importBatchClass);
				importBatchClass = batchClassService.getLoadedBatchClassByIdentifier(importBatchClass.getIdentifier());

				String dummyUncFolder = existingUncFolder + "-" + existingBatchClass.getIdentifier() + "-deleted";
				existingBatchClass = batchClassService.getBatchClassByIdentifier(existingBatchClass.getIdentifier());
				existingBatchClass.setUncFolder(dummyUncFolder);
				deleteEmailConfigForBatchClass(existingBatchClass);
				existingBatchClass = batchClassService.merge(existingBatchClass);
				try {
					importBatchClass.setUncFolder(existingUncFolder);
					importBatchClass = batchClassService.createBatchClass(importBatchClass);
					updateBatchForCopyXML(importBatchClass);

					try {
						FileUtils.deleteSelectedFilesFromDirectory(
								tempOutputUnZipDir + File.separator + batchSchemaService.getScriptFolderName(), scriptsList);
						File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator
								+ importBatchClass.getIdentifier());

						FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);

						List<BatchInstance> newBatchInstances = batchInstanceService.getBatchInstByBatchClass(existingBatchClass);
						for (BatchInstance batchInstance : newBatchInstances) {
							if (batchInstance.getStatus().equals(BatchInstanceStatus.NEW)) {
								batchInstance.setBatchClass(importBatchClass);
								batchInstance.setPriority(importBatchClass.getPriority());
								batchInstanceService.updateBatchInstance(batchInstance);
							}
						}
						updateBatchForCMIS(importBatchClass);
						resultsImport.put(Boolean.TRUE, importBatchClass.getIdentifier());
					} catch (Exception e) {
						errorMessg = "Error while overriding, reverting the changes:" + e.getMessage();
						LOGGER.info(errorMessg, e);
						isSuccess = false;
						resultsImport.put(isSuccess, errorMessg);
						importBatchClass.setDeleted(true);
						importBatchClass = batchClassService.merge(importBatchClass, true);

						existingBatchClass.setUncFolder(existingUncFolder);
						existingBatchClass = batchClassService.merge(existingBatchClass);
					}
					existingBatchClass.setDeleted(true);
					existingBatchClass = batchClassService.merge(existingBatchClass, true);
					new File(dummyUncFolder).mkdirs();

				} catch (Exception e) {
					errorMessg = "Error while overriding, reverting the changes:" + e.getMessage();
					isSuccess = false;
					resultsImport.put(isSuccess, errorMessg);
					LOGGER.info(errorMessg, e);
					existingBatchClass.setUncFolder(existingUncFolder);
					existingBatchClass = batchClassService.merge(existingBatchClass);
				}

			} catch (Exception e) {
				errorMessg = "Error while overriding, reverting the changes:" + e.getMessage();
				isSuccess = false;
				resultsImport.put(isSuccess, errorMessg);
				LOGGER.info(errorMessg, e);
				importBatchClass.setDeleted(true);
				importBatchClass = batchClassService.merge(importBatchClass, true);
			}

		}
		return importBatchClass;
	}

	/**
	 * Sets the selection status of a folder with given name contained within the imported batch class zip as the flag boolean value.
	 * 
	 * @param batchClassDefinition {@link BatchClassDefinition}
	 * @param folderName {@link String}, the name of the folder whose selection status needs to be updated.
	 * @param flag , boolean value to which will be set as the selection status of the folder.
	 */
	private void setFolderSelected(BatchClassDefinition batchClassDefinition, String folderName, boolean flag) {
		if (batchClassDefinition != null) {
			final Folders definitionFolders = batchClassDefinition.getFolders();
			if (definitionFolders != null) {
				final List<Folder> definitionFolder = definitionFolders.getFolder();
				if (definitionFolder != null && !definitionFolder.isEmpty()) {
					for (Folder folder : definitionFolder) {
						if (folder.getFileName().equalsIgnoreCase(folderName)) {
							folder.setSelected(flag);
						}
					}
				}
			}
		}
	}

	/**
	 * Method to delete the email configurations from the specified batch class.
	 * 
	 * @param batchClass
	 */
	private void deleteEmailConfigForBatchClass(final BatchClass batchClass) {
		List<BatchClassEmailConfiguration> emailConfigList = bcEmailConfigService.getEmailConfigByBatchClassIdentifier(batchClass
				.getIdentifier());
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : emailConfigList) {
			bcEmailConfigService.removeEmailConfiguration(batchClassEmailConfiguration);
		}
	}

	/**
	 * Utility Method to synchronize the serializable file Batch class with the current System Batch Class. In case the exported batch
	 * class contains any additional plugins,modules,plugin Config or plugin config sample values, they are imported as well.
	 * 
	 * @param dbBatchClass
	 * @param moduleConfigService
	 * @param moduleService
	 * @param pluginService
	 * @param pluginConfigService
	 * @param batchClassService
	 * @param batchSchemaService
	 * @param serializedBatchClass
	 */
	public void updateSerializableBatchClass(BatchClass dbBatchClass, BatchClass serializedBatchClass) throws DCMAApplicationException {
		List<DocumentType> documentTypes = serializedBatchClass.getDocumentTypes();
		if (documentTypes != null) {
			for (DocumentType documentType : documentTypes) {

				List<FieldType> fieldTypeList = documentType.getFieldTypes();
				for (FieldType fieldType : fieldTypeList) {
					if (fieldType.getOcrConfidenceThreshold() == null) {
						fieldType.setOcrConfidenceThreshold(DEFAULT_OCR_CONFIDENCE_THRESHOLD);
					}
				}
			}
		}

		List<BatchClassModule> batchClassModules = serializedBatchClass.getBatchClassModules();
		for (BatchClassModule batchClassModule : batchClassModules) {
			Module currentModule = moduleService.getModuleByName(batchClassModule.getModule().getName());
			if (currentModule == null) {
				continue;
			} else {
				batchClassModule.setModule(currentModule);
			}
			// Code commented to always delete batch class module configs while
			// importing batch class.
			// Always delete batch class module configs while importing batch
			// class.
			batchClassModule.getBatchClassModuleConfig().clear();
			batchClassModule.setBatchClassModuleConfig(new ArrayList<BatchClassModuleConfig>());

		}

		updateScannerConfig(serializedBatchClass.getBatchClassScannerConfiguration());
		updateBatchClassModules(batchClassModules);
		serializedBatchClass.encryptAllPasswords();
	}

	public void updateScannerConfig(List<BatchClassScannerConfiguration> batchClassScannerConfigs) {
		if (batchClassScannerConfigs == null || batchClassScannerConfigs.isEmpty()) {
			LOGGER.info("No Scanner Configs present in the imported batch class.");
			return;
		}
		final List<ScannerMasterConfiguration> scannerMasterConfigurations = masterScannerService.getMasterConfigurations();
		for (BatchClassScannerConfiguration batchClassScannerConfig : batchClassScannerConfigs) {
			ScannerMasterConfiguration importedScannerMasterConfig = batchClassScannerConfig.getScannerMasterConfig();
			if (null != importedScannerMasterConfig) {
				String masterScannerConfigName = importedScannerMasterConfig.getName();
				ScannerMasterConfiguration scannerMasterConfig = getScannerMasterConfigByName(scannerMasterConfigurations,
						masterScannerConfigName);
				if (scannerMasterConfig != null) {
					batchClassScannerConfig.setScannerMasterConfig(scannerMasterConfig);
				} else {
					LOGGER.error("Scanner config with name:" + masterScannerConfigName
							+ " is not present in the scanner_master_configuration table. So skipping this scanner config.");
				}
			}
		}
		// Adding new scanner properties to imported batch class assuming all
		// imported scanner profiles will have the same scanner
		// properties
		List<BatchClassScannerConfiguration> updatedScannerConfigs = getUpdatedScannerConfigs(scannerMasterConfigurations);
		List<BatchClassScannerConfiguration> parentProfiles = getAllParentProfilesForBatchClass(batchClassScannerConfigs);
		if (!CollectionUtil.isEmpty(parentProfiles)) {
			for (BatchClassScannerConfiguration batchClassScannerConfig : batchClassScannerConfigs) {
				removeExistingScannerConfigs(batchClassScannerConfig, updatedScannerConfigs);
			}
			List<BatchClassScannerConfiguration> newScannerConfigs = new ArrayList<BatchClassScannerConfiguration>();
			// The remaining list of new properties to be added.
			for (BatchClassScannerConfiguration parentProfile : parentProfiles) {
				for (BatchClassScannerConfiguration updatedScannerConfig : updatedScannerConfigs) {
					BatchClassScannerConfiguration config = new BatchClassScannerConfiguration();
					config.setScannerMasterConfig(updatedScannerConfig.getScannerMasterConfig());
					if (updatedScannerConfig.getScannerMasterConfig() != null
							&& !CollectionUtil.isEmpty(updatedScannerConfig.getScannerMasterConfig().getSampleValue())) {
						config.setValue(updatedScannerConfig.getScannerMasterConfig().getSampleValue().get(0));
					}
					config.setParent(parentProfile);
					config.setBatchClass(parentProfile.getBatchClass());
					newScannerConfigs.add(config);
				}
				batchClassScannerConfigs.addAll(newScannerConfigs);
				newScannerConfigs.clear();
			}
		}
	}

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

	private List<BatchClassScannerConfiguration> getUpdatedScannerConfigs(List<ScannerMasterConfiguration> scannerMasterConfigurations) {
		List<BatchClassScannerConfiguration> updatedBatchClassScannerConfigs = new ArrayList<BatchClassScannerConfiguration>();
		for (ScannerMasterConfiguration scannerMasterConfig : scannerMasterConfigurations) {
			if (scannerMasterConfig != null) {
				BatchClassScannerConfiguration batchClassScannerConfig = new BatchClassScannerConfiguration();
				batchClassScannerConfig.setScannerMasterConfig(scannerMasterConfig);
				updatedBatchClassScannerConfigs.add(batchClassScannerConfig);
			}
		}
		return updatedBatchClassScannerConfigs;
	}

	private ScannerMasterConfiguration getScannerMasterConfigByName(List<ScannerMasterConfiguration> scannerMasterConfigurations,
			String masterScannerConfigName) {
		ScannerMasterConfiguration matchedScannerMasterConfiguration = null;
		if (scannerMasterConfigurations != null && masterScannerConfigName != null) {
			for (ScannerMasterConfiguration scannerMasterConfiguration : scannerMasterConfigurations) {
				if (scannerMasterConfiguration.getName().equalsIgnoreCase(masterScannerConfigName)) {
					matchedScannerMasterConfiguration = scannerMasterConfiguration;
				}
			}
		}
		return matchedScannerMasterConfiguration;
	}

	private List<BatchClassScannerConfiguration> getAllParentProfilesForBatchClass(List<BatchClassScannerConfiguration> scannerConfigs) {
		List<BatchClassScannerConfiguration> scannerProfileConfigs = new ArrayList<BatchClassScannerConfiguration>();
		for (BatchClassScannerConfiguration scannerConfig : scannerConfigs) {
			if (CollectionUtil.isEmpty(scannerProfileConfigs)) {
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
	 * This method synchronizes the zip batch class modules with the system batch class modules.
	 * 
	 * @param pluginConfigService
	 * @param batchSchemaService
	 * @param dbBatchClass
	 * @param zipBatchClassModules
	 */
	private void updateBatchClassModules(List<BatchClassModule> zipBatchClassModules) throws DCMAApplicationException {
		List<BatchClassModule> validBatchClassModules = new ArrayList<BatchClassModule>();
		for (BatchClassModule zipBatchClassModule : zipBatchClassModules) {
			final Module moduleByName = moduleService.getModuleByName(zipBatchClassModule.getModule().getName());
			updateBatchClassPlugins(zipBatchClassModule.getBatchClassPlugins());
			zipBatchClassModule.setModule(moduleByName);
			validBatchClassModules.add(zipBatchClassModule);
		}
		if (validBatchClassModules != null) {
			zipBatchClassModules.clear();
			zipBatchClassModules.addAll(validBatchClassModules);
		}
	}

	/**
	 * This method synchronizes the zip batch class plugins of specific batch class module with batch class plugins of corresponding
	 * batch class module of system batch class.
	 * 
	 * @param pluginConfigService
	 * @param dbBatchClassModule
	 * @param zipBatchClassPlugins
	 */
	private void updateBatchClassPlugins(List<BatchClassPlugin> zipBatchClassPlugins) throws DCMAApplicationException {
		List<BatchClassPlugin> validBatchClassPlugins = new ArrayList<BatchClassPlugin>();
		for (BatchClassPlugin zipBatchClassPlugin : zipBatchClassPlugins) {
			final String pluginName = zipBatchClassPlugin.getPlugin().getPluginName();
			final Plugin pluginPropertiesForPluginName = pluginService.getPluginPropertiesForPluginName(pluginName);
			if (pluginPropertiesForPluginName != null) {
				final List<PluginConfig> newPluginConfigs = pluginConfigService.getPluginConfigForPluginId(""
						+ pluginPropertiesForPluginName.getId());
				zipBatchClassPlugin.setPlugin(pluginPropertiesForPluginName);
				getBatchClassPluginConfigs(zipBatchClassPlugin, newPluginConfigs);
				validBatchClassPlugins.add(zipBatchClassPlugin);
			} else {
				String errorMsg = "Plugin name " + pluginName + "is invalid or the plugin doesn't exist.";
				LOGGER.error(errorMsg);
				throw new DCMAApplicationException(errorMsg);
			}
		}
		if (validBatchClassPlugins != null) {
			zipBatchClassPlugins.clear();
			zipBatchClassPlugins.addAll(validBatchClassPlugins);
		}
	}

	/**
	 * Sets the batch class plugin configs for the given batch class plugin after verifying if the plugin config exists in the database
	 * or not. Also adds any new plugin configs which is actually not a part of the original batch class plugin config.
	 * 
	 * @param zipBatchClassPlugin {@link BatchClassPlugin}, The batch class plugin object retrieved from the zipped/input batch class.
	 * @param newPluginConfigs {@link List} <{@link BatchClassPluginConfig}>, list of newly added plugin configs for the concerned
	 *            plugin.
	 * @throws DCMAApplicationException
	 */
	private void getBatchClassPluginConfigs(final BatchClassPlugin zipBatchClassPlugin, final List<PluginConfig> newPluginConfigs)
			throws DCMAApplicationException {

		List<BatchClassPluginConfig> newBatchClassClassPluginConfigs = null;
		newBatchClassClassPluginConfigs = addBatchClassPluginConfigsForExitingPlugin(zipBatchClassPlugin, newPluginConfigs);
		zipBatchClassPlugin.getBatchClassPluginConfigs().clear();
		if (newBatchClassClassPluginConfigs != null) {
			zipBatchClassPlugin.getBatchClassPluginConfigs().addAll(newBatchClassClassPluginConfigs);
		}

	}

	/**
	 * This method synchronizes the zip batch class plugin configs of specific batch class plugin with the corresponding batch class
	 * plugin config in system batch class.
	 * 
	 * @param zipBatchClassPlugin {@link BatchClassPlugin}, The batch class plugin object retrieved from the zipped/input batch class.
	 * @param newPluginConfigs {@link List} <{@link BatchClassPluginConfig}>, list of newly added plugin configs for the concerned
	 *            plugin.
	 * @return {@link List} <{@link BatchClassPluginConfig}>, the list of valid batch class plugin configs.
	 */
	private List<BatchClassPluginConfig> addBatchClassPluginConfigsForExitingPlugin(final BatchClassPlugin zipBatchClassPlugin,
			final List<PluginConfig> newPluginConfigs) {
		final List<BatchClassPluginConfig> zipBatchClassPluginConfigs = zipBatchClassPlugin.getBatchClassPluginConfigs();

		List<BatchClassPluginConfig> validBCPluginConfigs = null;
		for (BatchClassPluginConfig batchClassPluginConfig : zipBatchClassPluginConfigs) {

			final String pluginConfigName = batchClassPluginConfig.getName();
			final PluginConfig pluginConfigByName = pluginConfigService.getPluginConfigByName(pluginConfigName);

			if (pluginConfigByName != null) {

				updateBatchClassPluginConfigValue(batchClassPluginConfig, pluginConfigByName);
				batchClassPluginConfig.setPluginConfig(pluginConfigByName);
				if (validBCPluginConfigs == null) {
					validBCPluginConfigs = new ArrayList<BatchClassPluginConfig>();
				}
				validBCPluginConfigs.add(batchClassPluginConfig);

			} else {
				LOGGER.error("No Plugin config with name " + pluginConfigName + " exists.");
			}

		}

		// Below code is to cater any new plugin config not present in the batch
		// class exported from the previous versions.
		validBCPluginConfigs = addNewPluginConfigs(zipBatchClassPlugin, newPluginConfigs, validBCPluginConfigs);

		return validBCPluginConfigs;
	}

	/**
	 * Adds the newly added plugin configs to the batch class plugin if it is not a part of it already. This is done to cater the
	 * situation where a new plugin config has already been added w.r.t the plugin but the batch class being imported was actually
	 * exported from an older version application.
	 * 
	 * @param zipBatchClassPlugin {@link BatchClassPlugin}, The batch class plugin object retrieved from the zipped/input batch class.
	 * @param newPluginConfigs {@link List} <{@link BatchClassPluginConfig}>, list of newly added plugin configs for the concerned
	 *            plugin.
	 * @param validBCPluginConfigs , {@link List} <{@link BatchClassPluginConfig}>, the list of already validated batch class plugin
	 *            configs.
	 * @return {@link List} <{@link BatchClassPluginConfig}>, the updated list of valid batch class plugin configs which now includes
	 *         the new plugin configs.
	 */
	private List<BatchClassPluginConfig> addNewPluginConfigs(final BatchClassPlugin zipBatchClassPlugin,
			final List<PluginConfig> newPluginConfigs, List<BatchClassPluginConfig> validBCPluginConfigs) {
		BatchClassPluginConfig validBatchClassPluginConfig;
		if (newPluginConfigs != null && !newPluginConfigs.isEmpty()) {
			LOGGER.info(newPluginConfigs.size() + " new plugin configs found for " + zipBatchClassPlugin.getPlugin().getPluginName()
					+ " plugin.");
			for (PluginConfig newPluginConfig : newPluginConfigs) {
				validBatchClassPluginConfig = getBatchClassPluginConfigByName(validBCPluginConfigs, newPluginConfig);
				if (validBatchClassPluginConfig == null) {
					LOGGER.info("Adding " + newPluginConfig.getDescription() + " to the list of valid batch class plugin configs.");
					/*
					 * Some new plugin config has been added and is not a part of existing set of configs present in the batch class
					 * plugin instance. Needs to be added to valid set of configs.
					 */
					validBatchClassPluginConfig = new BatchClassPluginConfig();
					// updateBatchClassPluginConfigValue(validBatchClassPluginConfig,
					// newPluginConfig);
					validBatchClassPluginConfig.setBatchClassPlugin(zipBatchClassPlugin);
					validBatchClassPluginConfig.setPluginConfig(newPluginConfig);

					// For setting new plugin config value to value existed in
					// pre existing batch class plugin value.
					final long id = validBatchClassPluginConfig.getPluginConfig().getId();
					final List<BatchClassPluginConfig> existingBatchClassPluginConfigs = batchClassPluginConfigService
							.getBatchClassPluginConfigForPluginConfigId(id);
					String existingValueForNewConfig = null;
					for (BatchClassPluginConfig batchClassPluginConfig : existingBatchClassPluginConfigs) {
						existingValueForNewConfig = batchClassPluginConfig.getValue();
						if (existingBatchClassPluginConfigs != null) {
							break;
						}
					}
					if (existingValueForNewConfig != null) {
						validBatchClassPluginConfig.setValue(existingValueForNewConfig);
					} else {
						setDefaultValueForNewConfig(validBatchClassPluginConfig);
					}
					if (validBCPluginConfigs == null) {
						validBCPluginConfigs = new ArrayList<BatchClassPluginConfig>();
					}
					validBCPluginConfigs.add(validBatchClassPluginConfig);
				} else {
					LOGGER.info(newPluginConfig.getDescription()
							+ " plugin config already present in the list of valid batch class plugin configs. Skipping it.");
				}
			}
		}
		return validBCPluginConfigs;
	}

	/**
	 * Retrieves the batch class plugin config object contained within the provided list of batch class plugin configs with its plugin
	 * config name as of the provided plugin config object .
	 * 
	 * @param validBCPluginConfigs {@link List} <{@link BatchClassPluginConfig}>, the list of already present batch class plugin
	 *            configs.
	 * @param newPluginConfig {@link PluginConfig}, the plugin config to be searched.
	 * @return {@link BatchClassPluginConfig}, the batch class plugin config object with it its parent plugin config as the one
	 *         provided. <code>null</code> in case no such value is found.
	 */
	private BatchClassPluginConfig getBatchClassPluginConfigByName(final List<BatchClassPluginConfig> validBCPluginConfigs,
			final PluginConfig newPluginConfig) {
		BatchClassPluginConfig validBatchClassPluginConfig = null;
		// if there is no plugin config already present in that Plugin then we
		// will recieve null as validBCPluginConfigs.
		if (validBCPluginConfigs != null) {
			for (BatchClassPluginConfig batchClassPluginConfig : validBCPluginConfigs) {
				if (null != batchClassPluginConfig && batchClassPluginConfig.getName().equals(newPluginConfig.getName())) {
					validBatchClassPluginConfig = batchClassPluginConfig;
					break;
				}
			}
		}
		return validBatchClassPluginConfig;
	}

	/*
	 * private List<BatchClassPluginConfig> addBatchClassPluginConfigsForNewPlugin(List<PluginConfig> existingPluginConfigs,
	 * List<BatchClassPluginConfig> zipBatchClassPluginConfigs) { List<BatchClassPluginConfig> validBCPluginConfigs = new
	 * ArrayList<BatchClassPluginConfig>(); BatchClassPluginConfig bcPluginConfig = null; for (PluginConfig pluginConfig :
	 * existingPluginConfigs) { boolean pluginConfigExits = false;
	 * 
	 * String pluginConfigName = pluginConfig.getName(); for (BatchClassPluginConfig batchClassPluginConfig :
	 * zipBatchClassPluginConfigs) { String existingPluginConfigName = batchClassPluginConfig.getName(); if
	 * (existingPluginConfigName.equals(pluginConfigName)) { updateBatchClassPluginConfigValue(batchClassPluginConfig, pluginConfig);
	 * pluginConfigExits = true; validBCPluginConfigs.add(batchClassPluginConfig); break; } } if (!pluginConfigExits) {
	 * BatchClassPluginConfig batchClassPluginConfig = new BatchClassPluginConfig();
	 * batchClassPluginConfig.setPluginConfig(pluginConfig); setDefaultValueForNewConfig(batchClassPluginConfig);
	 * validBCPluginConfigs.add(batchClassPluginConfig); } } return validBCPluginConfigs; }
	 */

	/**
	 * @param pluginConfig
	 * @param batchClassPluginConfig
	 */
	private static void setDefaultValueForNewConfig(final BatchClassPluginConfig batchClassPluginConfig) {
		LOGGER.trace("Setting Values for new Plugin..");
		DataType pluginConfigDataType = batchClassPluginConfig.getPluginConfig().getDataType();
		if (pluginConfigDataType == DataType.BOOLEAN) {
			batchClassPluginConfig.setValue("No");
		} else if (pluginConfigDataType == DataType.PASSWORD) {
			batchClassPluginConfig.setValue(DataType.PASSWORD.toString());
		} else {
			boolean isMandatory = batchClassPluginConfig.getPluginConfig().isMandatory();
			if (pluginConfigDataType == DataType.STRING && isMandatory) {
				List<String> sampleValues = batchClassPluginConfig.getSampleValue();
				if (sampleValues == null || sampleValues.isEmpty()) {
					batchClassPluginConfig.setValue("Default");
				} else if (sampleValues.size() > 0) {
					batchClassPluginConfig.setValue(sampleValues.get(0));
				}
			} else if (pluginConfigDataType == DataType.INTEGER && isMandatory) {
				batchClassPluginConfig.setValue("0");
			}

		}
	}

	/**
	 * This method sychronizes the zip batch class plugin config value with the value in plugin config sample value.
	 * 
	 * @param pluginConfigService
	 * @param zipBatchClassPluginConfig
	 * @param dbBatchClassPluginConfig
	 */
	private void updateBatchClassPluginConfigValue(final BatchClassPluginConfig zipBatchClassPluginConfig,
			final PluginConfig pluginConfig) {
		if (pluginConfig != null) {

			String bcpcValue = zipBatchClassPluginConfig.getValue();
			if (zipBatchClassPluginConfig.getPluginConfig().getDataType() == DataType.PASSWORD) {
				zipBatchClassPluginConfig.setValue(EphesoftEncryptionUtil.getEncryptedPasswordString(bcpcValue));
			} else {
				// default setting the value of valid extensions as xml;html if
				// only html is set.
				if (zipBatchClassPluginConfig.getPluginConfig().getName().equalsIgnoreCase(BatchConstants.LUCENE_VALID_EXTENSIONS)
						&& zipBatchClassPluginConfig.getValue().equalsIgnoreCase(BatchConstants.HTML_SETTING)) {
					zipBatchClassPluginConfig.setValue(BatchConstants.DEFAULT_VALUE_SETTING);
				} else {
					// get sample values for that plugin config.
					List<String> pcSampleValue = pluginConfig.getSampleValue();
					// get batch class plugin config value in zip.
					// Check if batch class plugin config value is valid or not.

					// Rename SearchablePdfClassification to
					// OneDocumentClassification.
					if (BatchConstants.DA_SEARCHABLE_PDF_CLASSIFICATION.equals(bcpcValue)) {
						bcpcValue = BatchConstants.DA_ONE_DOCUMENT_CLASSIFICATION;
						zipBatchClassPluginConfig.setValue(bcpcValue);
					}

					if (pcSampleValue != null && !pcSampleValue.isEmpty() && !isBcpcValueExistsInDB(pcSampleValue, bcpcValue)) {
						// update imported batch class plugin value with plugin
						// config
						// value for system batch class.
						zipBatchClassPluginConfig.setValue(pcSampleValue.get(0));
					}
				}
			}
		}
	}

	/**
	 * This method returns true or false depending on whether zip batch class plugin config value exists in its sample values in
	 * database.
	 * 
	 * @param dbBatchClassPluginConfig
	 * @param zipBcpcValue
	 * @return
	 */
	private boolean isBcpcValueExistsInDB(List<String> dbBatchClassPluginConfig, String zipBcpcValue) {
		boolean isPresent = false;
		if (zipBcpcValue != null) {
			String[] valueArray = zipBcpcValue.split(SEMI_COLON);
			for (int index = 0; index < valueArray.length; index++) {
				String eachValue = valueArray[index];
				for (String sampleValue : dbBatchClassPluginConfig) {
					// Check if BCPC value exists in database.
					if (sampleValue.equalsIgnoreCase(eachValue)) {
						isPresent = true;
						break;
					}
				}
				if (!isPresent) {
					// Value not present in sample values. Return false.
					break;
				} else if (index == valueArray.length - 1) {
					// Reached end of array. Each BCPC value present. Return
					// true.
					break;
				} else {
					// BCPC value present. Continue with next value in array.
					isPresent = false;
				}
			}
		}
		return isPresent;
	}

	/**
	 * Method to update the plugin configurations of the batch class as per the options specified.
	 * 
	 * @param resultsImport
	 * @param bcm
	 * @param importBatchClass
	 * @param existingBatchClass
	 */
	public void updatePlgConfig(Map<Boolean, String> resultsImport,
			com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules.BatchClassModule bcm,
			BatchClass importBatchClass, BatchClass existingBatchClass) {
		// To-Do:

		String errorMessg = "";
		boolean results = true;
		BatchClassModule importBatchClassModule = importBatchClass.getBatchClassModuleByWorkflowName(bcm.getModuleName());
		BatchClassModule existingBatchClassModule = batchSchemaService.getDetachedBatchClassModuleByName(
				existingBatchClass.getIdentifier(), bcm.getModuleName());
		if (!bcm.isPluginConfiguration()) {
			// import the plug-in configuration for the module from database
			if (existingBatchClassModule == null || importBatchClassModule == null) {
				errorMessg = "Either no module found with workflowName:\"" + bcm.getModuleName() + "\" in batch class id:"
						+ existingBatchClass.getIdentifier()
						+ " or no module found in zip batch class. Returning with error while batch class import.";
				LOGGER.error(errorMessg);
				results = false;
			} else {
				List<BatchClassModule> importedBatchClassMod = importBatchClass.getBatchClassModules();
				importedBatchClassMod.set(importedBatchClassMod.indexOf(importBatchClassModule), existingBatchClassModule);

			}
			resultsImport.put(results, errorMessg);

		} else {
			if (importBatchClassModule == null) {
				// not found in zip throw in error
				errorMessg = "No module found with workflowName:\"" + bcm.getModuleName()
						+ "\" in zip batch class. Returning with error while batch class import.";
				LOGGER.error(errorMessg);
				results = false;
				resultsImport.put(results, errorMessg);
			}
		}
	}

	/**
	 * Method to update the roles of the specified batch class.
	 * 
	 * @param importBatchClass
	 * @param existingBatchClass
	 * @param isFromDB
	 */
	public void updateRoles(BatchClass importBatchClass, BatchClass existingBatchClass, Boolean isFromDB) {
		List<BatchClassGroups> batchClassGroups = new ArrayList<BatchClassGroups>();
		if (isFromDB) {
			batchClassGroups = existingBatchClass.getAssignedGroups();
		} else {
			batchClassGroups = importBatchClass.getAssignedGroups();
		}
		List<BatchClassGroups> newBatchClassGroupsList = new ArrayList<BatchClassGroups>();
		for (BatchClassGroups batchClassGroup : batchClassGroups) {
			newBatchClassGroupsList.add(batchClassGroup);
			batchClassGroup.setId(0);
			batchClassGroup.setBatchClass(null);
		}
		if (isFromDB) {
			importBatchClass.getAssignedGroups().clear();
			importBatchClass.setAssignedGroups(newBatchClassGroupsList);
		} else {
			existingBatchClass.getAssignedGroups().clear();
			existingBatchClass.setAssignedGroups(newBatchClassGroupsList);
		}
	}

	/**
	 * @param importBatchClass
	 * @param existingBatchClass
	 * @param isFromDB
	 */
	public void updateEmailConfigurations(BatchClass importBatchClass, BatchClass existingBatchClass, Boolean isFromDB) {
		List<BatchClassEmailConfiguration> batchClassEmailConfigurations = new ArrayList<BatchClassEmailConfiguration>();
		if (isFromDB) {
			batchClassEmailConfigurations = existingBatchClass.getEmailConfigurations();
		} else {
			batchClassEmailConfigurations = importBatchClass.getEmailConfigurations();
		}

		List<BatchClassEmailConfiguration> newBatchClassEmailConfigurationList = new ArrayList<BatchClassEmailConfiguration>();
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : batchClassEmailConfigurations) {
			newBatchClassEmailConfigurationList.add(batchClassEmailConfiguration);
			String emailPassword = batchClassEmailConfiguration.getPassword();
			emailPassword = EphesoftEncryptionUtil.getEncryptedPasswordString(emailPassword);
			batchClassEmailConfiguration.setPassword(emailPassword);
			batchClassEmailConfiguration.setId(0);
			batchClassEmailConfiguration.setBatchClass(null);
		}
		if (isFromDB) {
			importBatchClass.getEmailConfigurations().clear();
			importBatchClass.setEmailConfigurations(newBatchClassEmailConfigurationList);
		} else {
			existingBatchClass.getEmailConfigurations().clear();
			existingBatchClass.setEmailConfigurations(newBatchClassEmailConfigurationList);
		}
	}

	/**
	 * @param resultsImport
	 * @param existingFolderName
	 * @param zipFolderName
	 * @param zipDir
	 */
	public void overrideFromDB(Map<Boolean, String> resultsImport, String zipFolderName, String existingFolderName, String zipDir) {
		String errorMessg = "";
		boolean isSuccess = true;
		// import the samples from database
		if (zipFolderName.isEmpty()) {
			// create a folder in the directory of zip file
			zipFolderName = new File(zipFolderName).getAbsolutePath();
		} else {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(zipFolderName), false);
		}
		if (zipFolderName != null && !zipFolderName.isEmpty()) {
			if (existingFolderName != null && !existingFolderName.isEmpty()) {
				boolean copiedSuccessfully = true;
				try {
					FileUtils.copyDirectoryWithContents(existingFolderName, zipFolderName);
				} catch (IOException ioe) {
					errorMessg = "Unable to override folder" + existingFolderName + " for batch class.";
					LOGGER.info(errorMessg + ioe, ioe);
					copiedSuccessfully = false;
				}
				if (!copiedSuccessfully) {
					isSuccess = false;
					resultsImport.put(isSuccess, errorMessg);
				}
			}
		}
	}

	/**
	 * @param tempOutputUnZipDir
	 * @param allScriptsList
	 * @param existingBCID
	 * @return
	 * @throws Exception
	 */
	public List<String> updateScriptsFiles(String tempOutputUnZipDir,
			List<ImportBatchClassOptions.BatchClassDefinition.Scripts.Script> allScriptsList, String existingBCID) throws Exception {
		List<String> scriptsList = new ArrayList<String>();
		if (!allScriptsList.isEmpty()) {
			String existingScriptFilePath = EMPTY_STRING;
			String zipScriptFilePath = EMPTY_STRING;
			String scriptsFolderName = batchSchemaService.getScriptFolderName();
			for (Script script : allScriptsList) {
				String scriptFileName = script.getFileName();
				boolean isSelected = script.isSelected();

				existingScriptFilePath = FileUtils.getFileNameOfTypeFromFolder(
						batchSchemaService.getAbsolutePath(existingBCID, scriptsFolderName, false), scriptFileName);
				zipScriptFilePath = FileUtils.getFileNameOfTypeFromFolder(tempOutputUnZipDir + File.separator + scriptsFolderName,
						scriptFileName);

				File zipScriptFile = null;
				File existingScriptFile = null;
				if (zipScriptFilePath != null && !zipScriptFilePath.isEmpty()) {
					zipScriptFile = new File(zipScriptFilePath);
				}
				if (!isSelected) {
					// import the script for the module from database

					if (zipScriptFile != null && zipScriptFile.exists()) {
						zipScriptFile.delete();
					}
					if (existingScriptFilePath != null && !existingScriptFilePath.isEmpty()) {
						existingScriptFile = new File(existingScriptFilePath);
					}
					if (existingScriptFile != null && existingScriptFile.exists()) {
						if (zipScriptFile == null) {
							zipScriptFile = new File(tempOutputUnZipDir + File.separator + scriptsFolderName + File.separator
									+ existingScriptFile.getName());
						}

						FileUtils.copyFile(existingScriptFile, zipScriptFile);
						scriptsList.add(zipScriptFile.getName());
					}
				} else {
					if (zipScriptFile != null) {
						scriptsList.add(zipScriptFile.getName());
					}
				}
			}
		}
		return scriptsList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.dcma.batch.service.ImportBatchService#validateInputXML(com
	 * .ephesoft.dcma.batch.schema.ImportBatchClassOptions)
	 */
	@Override
	public Map<Boolean, String> validateInputXML(ImportBatchClassOptions optionXML) {
		boolean isValid = true;
		String errorMessg = "";
		if (!optionXML.isUseSource()) {
			// check for the description and priority from the XML file
			if (optionXML.getName() == null || optionXML.getDescription() == null || optionXML.getName().isEmpty()
					|| optionXML.getDescription().isEmpty() || optionXML.getPriority() <= 0 || optionXML.getPriority() > 100) {
				errorMessg = "Either name, description is empty or priority is not between 1 and 100 inclusive.";
				isValid = false;
			}
		}
		if (isValid) {
			String batchClassKey = optionXML.getBatchClassKey();
			String encryptionAlgo = optionXML.getEncryptionAlgorithm();

			// Encryption is not supported for Linux. Hecnce putting check for
			// the skipping the validation of input XML for encryption
			// on Unix operating system.
			if (OSUtil.isWindows()) {
				if (!optionXML.isUseKey()) {
					if (null == encryptionAlgo || encryptionAlgo.isEmpty()) {
						errorMessg = "Encryption Algorithm cannot be empty when use existing is set to false. Please provide a value from among None/AES-128/AES-256";
						isValid = false;
					} else if (!(BatchConstants.ENCRYPTION_ALGO_NONE.equalsIgnoreCase(encryptionAlgo)
							|| BatchConstants.ENCRYPTION_ALGO_AES_128.equalsIgnoreCase(encryptionAlgo) || BatchConstants.ENCRYPTION_ALGO_AES_256
								.equalsIgnoreCase(encryptionAlgo))) {
						errorMessg = "Incorrect value provided for encryption algorithm. Please provide a value from among None/AES-128/AES-256";
						isValid = false;
					} else if ((null == batchClassKey || batchClassKey.isEmpty())
							&& !BatchConstants.ENCRYPTION_ALGO_NONE.equalsIgnoreCase(encryptionAlgo)) {
						errorMessg = "Batch class key cannot be empty for the requested algorithm : " + encryptionAlgo;
						isValid = false;
					}
				}
			}
		}
		if (isValid) {
			String name = optionXML.getName();
			if (name.indexOf(" ") > -1 || name.indexOf("-") > -1) {
				errorMessg = "Workflow name should not contain space or hyphen character.";
				isValid = false;
			}
		}
		if (isValid) {
			if (optionXML.getUncFolder() == null || optionXML.getUncFolder().isEmpty()) {
				errorMessg = "UNC folder is not specified.";
				isValid = false;
			} else {
				File unc = new File(optionXML.getUncFolder());
				if (optionXML.isUseExisting()) {
					// check for the existence of UNC folder in case it is
					// override batch class
					isValid = unc.exists();
					if (!isValid) {
						errorMessg = "UNC folder does not exist. Can not override an existing batch class.";
					}
				} else {
					// check for NOT existence of UNC folder in case it is new
					// batch class
					isValid = (!unc.exists()) || (unc.list() != null && unc.list().length == 0);
					if (!isValid) {
						errorMessg = "UNC folder exists already and is not empty. Can not import a new batch class with the existing UNC folder.";
					}
				}
			}
		}
		Map<Boolean, String> results = new HashMap<Boolean, String>();
		results.put(isValid, errorMessg);
		return results;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.ephesoft.dcma.batch.service.ImportBatchService# isImportWorkflowEqualDeployedWorkflow
	 * (com.ephesoft.dcma.da.domain.BatchClass, java.lang.String)
	 */
	@Override
	public boolean isImportWorkflowEqualDeployedWorkflow(final BatchClass importBatchClass, final String userInputWorkflowName) {
		BatchClass dbBatchClass = batchClassService.getBatchClassByNameIncludingDeleted(userInputWorkflowName);
		boolean isEqual = true;
		// check for the number of modules
		if (importBatchClass != null && dbBatchClass != null) {
			List<BatchClassModule> zipBCMs = importBatchClass.getBatchClassModules();
			List<BatchClassModule> dbBCMs = dbBatchClass.getBatchClassModules();
			if (dbBCMs.size() == zipBCMs.size()) {
				bcmLoop: for (int index = 0; index < dbBCMs.size(); index++) {
					BatchClassModule zipBCM = zipBCMs.get(index);
					BatchClassModule dbBCM = dbBCMs.get(index);
					if (!dbBCM.getWorkflowName().equals(zipBCM.getWorkflowName())) {
						isEqual = false;
						break;
					}

					List<BatchClassPlugin> zipBCPs = zipBCM.getBatchClassPlugins();
					List<BatchClassPlugin> dbBCPs = dbBCM.getBatchClassPlugins();
					if (dbBCPs.size() == zipBCPs.size()) {
						for (int indexPlg = 0; indexPlg < dbBCPs.size(); indexPlg++) {
							BatchClassPlugin zipBCP = zipBCPs.get(indexPlg);
							BatchClassPlugin dbBCP = dbBCPs.get(indexPlg);
							if (!dbBCP.getPlugin().getWorkflowName().equals(zipBCP.getPlugin().getWorkflowName())) {
								isEqual = false;
								break bcmLoop;
							}
						}
					} else {
						isEqual = false;
						break bcmLoop;
					}

				}
			} else {
				isEqual = false;
			}
		} else {
			isEqual = false;
		}
		return isEqual;
	}

	@Override
	public boolean removeFolders(BatchInstance batchInstance) {
		final String localFolderLocation = batchSchemaService.getLocalFolderLocation();

		// Added code to delete serialized file for batch instance workflows.
		String batchInstanceIdentifier = batchInstance.getIdentifier();
		String commonPathToBatchPropertyFiles = EphesoftStringUtil.concatenate(localFolderLocation, File.separator,
				BatchConstants.PROPERTIES_DIRECTORY, File.separator, batchInstanceIdentifier);
		String workflowSerializedFileSuffix = EphesoftStringUtil.concatenate(ICommonConstants.UNDERSCORE,
				BatchConstants.WORKFLOWS_CONSTANT, FileType.SER.getExtensionWithDot());
		String batchPropertyFilePath = EphesoftStringUtil.concatenate(commonPathToBatchPropertyFiles,
				FileType.SER.getExtensionWithDot());
		LOGGER.info("For batch instance: " + batchInstanceIdentifier + ", deleting batch data.");
		LOGGER.debug("Batch property file path is: " + batchPropertyFilePath);
		File batchPropertyfile = new File(batchPropertyFilePath);
		boolean isDeleted = true;
		if (batchPropertyfile.exists()) {
			isDeleted &= batchPropertyfile.delete();
		}
		if (isDeleted) {
			LOGGER.debug("Batch instance data at path: " + batchPropertyFilePath + " is deleted successfully.");
		} else {
			LOGGER.debug("Batch instance data at path: " + batchPropertyFilePath + " is is not deleted completely.");
		}
		String workflowBatchFilePath = EphesoftStringUtil.concatenate(commonPathToBatchPropertyFiles, workflowSerializedFileSuffix);
		File batchWorkflowsPropertyFile = new File(workflowBatchFilePath);
		if (batchWorkflowsPropertyFile.exists()) {
			isDeleted &= batchWorkflowsPropertyFile.delete();
		}
		if (isDeleted) {
			LOGGER.debug("Batch instance workflow data at path: " + workflowBatchFilePath + " is deleted successfully.");
		} else {
			LOGGER.debug("Batch instance workflow data at file path: " + workflowBatchFilePath + " is is not deleted completely.");
		}
		return isDeleted;
	}

	/**
	 * Creates a copy of kv learning folder from BC1 batch class and pastes it in the imported batchClass folder.
	 * 
	 * @param batchClassId {@link String } Identifier of batch class being imported.
	 * @return boolean value as status of operation. True if copy was successful.
	 */
	private boolean copyKVLearningFolder(final String batchClassId) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Getting parameter Batch class idenifier : ", batchClassId));
		boolean isOperationSuccessful = false;
		if (EphesoftStringUtil.isNullOrEmpty(batchClassId)) {
			LOGGER.error("Getting parameter Batch class idenifier as null.");
		} else {
			final String baseFolderLocation = batchSchemaService.getBaseFolderLocation();
			final String kvLearningFolderName = batchSchemaService.getKeyValueLearningFolderName();
			final String sourceFolderPath = EphesoftStringUtil.concatenate(baseFolderLocation, File.separator,
					BatchConstants.BC1_BATCH_CLASS_NAME, File.separator, kvLearningFolderName);
			final String targetFolderPath = EphesoftStringUtil.concatenate(baseFolderLocation, File.separator, batchClassId,
					File.separator, kvLearningFolderName);
			try {
				FileUtils.copyDirectoryWithContents(sourceFolderPath, targetFolderPath);
				isOperationSuccessful = true;
			} catch (IOException ioException) {
				LOGGER.error("Unable to copying kv learning folder ", ioException);
			}
		}
		return isOperationSuccessful;
	}

	/**
	 * Sets default value to attributes in {@link KVExtraction}
	 * 
	 * @param kvExtraction {@link KVExtraction} instance
	 */
	private void setDefaultValueKVExtraction(final KVExtraction kvExtraction) {

		// Setting default weight value to 1
		if (null == kvExtraction.getWeight()) {
			kvExtraction.setWeight(DEFAULT_WEIGHT);
		}

		// Setting default value of extractZone to ALL
		if ((null == kvExtraction.getExtractZone()) && (null != kvExtraction.getFetchValue())) {
			kvExtraction.setExtractZone(KVExtractZone.ALL);
		}
	}

	public Map<ImportExportDocument, DocumentType> importDocumentTypes(final String tempFolderPath, final boolean isSampleFdSelected,
			final String bCIdentifier) {
		boolean dbException = false;
		List<DocumentType> docTypeList = new ArrayList<DocumentType>();
		final Map<ImportExportDocument, DocumentType> documentsList = new HashMap<ImportExportDocument, DocumentType>();

		try {
			docTypeList = documentTypeService.getDocTypeByBatchClassIdentifier(bCIdentifier);
			final List<DocumentType> validDocTypeList = new ArrayList<DocumentType>(docTypeList.size());

			// maintaining a list of document types without 'unknown' document types
			for (int i = 0; i < docTypeList.size(); i++) {
				if (!(UNKNOWN_DOC_TYPE.equalsIgnoreCase(docTypeList.get(i).getName()))) {
					validDocTypeList.add(docTypeList.get(i));
				}
			}
			final int allowedNoOfDocType = MAX_DOC_TYPE - validDocTypeList.size();
			docTypeLimitExceed = false;

			final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(bCIdentifier);
			final File tempUnZipDir = new File(tempFolderPath);
			String[] unZipDocDirList = null;
			if (tempUnZipDir.exists()) {
				unZipDocDirList = tempUnZipDir.list();
				final int noOfImportDocType = unZipDocDirList.length;
				for (int k = 0; k < noOfImportDocType && k < allowedNoOfDocType; k++) {
					DocumentType deleteDocType = null;
					dbException = false;
					final String documentDir = FilenameUtils.getName(unZipDocDirList[k]);
					final String tempDocDirPath = tempFolderPath + File.separator + documentDir;
					final DocumentType importDocType = deserializeObj(tempDocDirPath);
					if (importDocType != null) {
						deleteDocType = checkDocumentExistence(importDocType, docTypeList);
						final ImportExportDocument doc = removeInsertDocInImportDoc(deleteDocType, importDocType, batchClass,
								documentsList);
						if (doc != null) {
							dbException = doc.isError();
						}
						delCopyFdsInImportDoc(isSampleFdSelected, dbException, deleteDocType, tempDocDirPath, bCIdentifier);
					} else {
						final ImportExportDocument impExpDoc = new ImportExportDocument(documentDir, false, false, true);
						documentsList.put(impExpDoc, null);
						LOGGER.error("Error while deserializing the document type object in Dir: " + tempDocDirPath);

					}
				}

				if (noOfImportDocType > allowedNoOfDocType) {
					docTypeLimitExceed = true;
				}
			} else {
				LOGGER.error("UnZip Directory not exist while importing the documents");
			}
		} catch (final Exception exception) {
			final String errorMessg = EphesoftStringUtil.concatenate("Error while importing. ", exception.getMessage());
			LOGGER.error(errorMessg, exception);
		}
		return documentsList;
	}

	public boolean isDocTypeLimitExceed() {
		return docTypeLimitExceed;
	}

	public Map<ImportExportIndexField, FieldType> importIndexField(final String tempFolderPath, final String documentTypeName,
			String batchClassIdentifier) {
		boolean dbException = false;
		List<FieldType> indexFieldList = new ArrayList<FieldType>();
		DocumentType documentType = documentTypeService.getDocTypeByBatchClassAndDocTypeName(batchClassIdentifier, documentTypeName);
		final Map<ImportExportIndexField, FieldType> fieldTypeList = new HashMap<ImportExportIndexField, FieldType>();
		try {
			indexFieldList = fieldTypeService.getFdTypeByDocTypeNameForBatchClass(documentTypeName, batchClassIdentifier);
			final File tempUnZipDir = new File(tempFolderPath);
			String[] unZipDocDirList = null;
			if (tempUnZipDir.exists()) {
				unZipDocDirList = tempUnZipDir.list();
				for (int k = 0; k < unZipDocDirList.length; k++) {
					FieldType deleteIndexField = null;
					dbException = false;
					final FieldType importIndexField = deserializeIndexFieldObj(tempFolderPath + File.separator + unZipDocDirList[k]);
					if (importIndexField != null) {
						deleteIndexField = checkFieldTypeExistance(importIndexField, indexFieldList);
						final ImportExportIndexField doc = removeInsertIndexFieldImportDoc(deleteIndexField, importIndexField,
								documentType, fieldTypeList);

					} else {
						final ImportExportIndexField impExpDoc = new ImportExportIndexField("ERROR", false, false, true);
						fieldTypeList.put(impExpDoc, null);
						LOGGER.error("Error while deserializing the document type object in Dir: ");

					}
				}
			} else {
				LOGGER.error("UnZip Directory not exist while importing the documents");
			}
		} catch (final Exception exception) {
			final String errorMessg = EphesoftStringUtil.concatenate("Error while importing. ", exception.getMessage());
			LOGGER.error(errorMessg, exception);
		}
		return fieldTypeList;
	}

	/**
	 * This API is used to deserialize DocumentType object.
	 * 
	 * @param tempDocDirPath {@link String} Source path where serialize file exist.
	 * @return {@link DocumentType} deserialize document type object.
	 */
	private DocumentType deserializeObj(final String tempDocDirPath) {
		DocumentType importDocType = null;
		try {
			String serFilePath = "";
			InputStream serFileStream = null;
			serFilePath = FileUtils.getFileNameOfTypeFromFolder(tempDocDirPath, FileType.SER.getExtensionWithDot());
			serFileStream = new FileInputStream(serFilePath);
			importDocType = (DocumentType) SerializationUtils.deserialize(serFileStream);
		} catch (final Exception exception) {
			final String errorMessg = EphesoftStringUtil.concatenate("Error while deserializing the Document Type in . "
					+ tempDocDirPath, exception.getMessage());
			LOGGER.error(errorMessg, exception);
		}
		return importDocType;
	}

	/**
	 * This API is used to deserialize DocumentType object.
	 * 
	 * @param tempDocDirPath {@link String} Source path where serialize file exist.
	 * @return {@link DocumentType} deserialize document type object.
	 */
	public FieldType deserializeIndexFieldObj(final String tempDocDirPath) {
		FieldType indexFieldType = null;
		try {
			InputStream serFileStream = null;
			serFileStream = new FileInputStream(tempDocDirPath);
			// Import Into Database from the serialized file
			indexFieldType = (FieldType) SerializationUtils.deserialize(serFileStream);
		} catch (final Exception exception) {
			final String errorMessg = EphesoftStringUtil.concatenate(
					"Error while deserializing the Index Type in . " + tempDocDirPath, exception.getMessage());
			LOGGER.error(errorMessg, exception);
		}
		return indexFieldType;
	}

	/**
	 * This API is used to delete and copy document type classification samples in batch class classification samples.
	 * 
	 * @param isIMFdSelected boolean true if image classification samples selected to import.
	 * @param isSSFdSelected boolean true if search classification samples selected to import.
	 * @param dbException boolean.
	 * @param deleteDocType {@link DocumentType} document type to delete.
	 * @param tempDocDirPath {@link String} temporary document directory path.
	 * @param bCIdentifier {@link String} batch class identifier.
	 */
	private void delCopyFdsInImportDoc(final boolean isSampleFdSelected, final boolean dbException, final DocumentType deleteDocType,
			final String tempDocDirPath, final String bCIdentifier) {
		final String imageMagickFdPath = batchSchemaService.getBaseSampleFDLock() + File.separator + bCIdentifier + File.separator
				+ batchSchemaService.getImagemagickBaseFolderName();
		final String searchSampleFdPath = batchSchemaService.getBaseSampleFDLock() + File.separator + bCIdentifier + File.separator
				+ batchSchemaService.getSearchSampleName();
		final String tempIMFdPath = tempDocDirPath + File.separator + batchSchemaService.getImagemagickBaseFolderName();
		final String tempSSFdPath = tempDocDirPath + File.separator + batchSchemaService.getSearchSampleName();

		if (isSampleFdSelected && !dbException && deleteDocType == null) {
			final File tempImageMagickFd = new File(tempDocDirPath + File.separator
					+ batchSchemaService.getImagemagickBaseFolderName());
			copyDocumentInBaseFolder(tempImageMagickFd, new File(imageMagickFdPath));
			final File tempSearchSampleFd = new File(tempDocDirPath + File.separator + batchSchemaService.getSearchSampleName());
			copyDocumentInBaseFolder(tempSearchSampleFd, new File(searchSampleFdPath));
		} else if (isSampleFdSelected && !dbException && deleteDocType != null) {
			if (deleteDocType != null) {
				deleteAndCopyDocumentFolder(imageMagickFdPath, deleteDocType, tempIMFdPath);
				deleteAndCopyDocumentFolder(searchSampleFdPath, deleteDocType, tempSSFdPath);
			}
		}

		/*
		 * if (isSSFdSelected && !dbException && deleteDocType == null) { final File tempSearchSampleFd = new File(tempDocDirPath +
		 * File.separator + batchSchemaService.getSearchSampleName()); copyDocumentInBaseFolder(tempSearchSampleFd, new
		 * File(searchSampleFdPath)); } else if (isSSFdSelected && !dbException && deleteDocType != null) { if (deleteDocType != null)
		 * { deleteAndCopyDocumentFolder(searchSampleFdPath, deleteDocType, tempSSFdPath); } }
		 */
	}

	/**
	 * This API is used to delete and copy document type classification samples from/to batch class classification samples.
	 * 
	 * @param imageMagickFdPath {@link String} classificatio samples path of a batch class
	 * @param deleteDocType {@link DocumentType} document whose classification samples to delete from batch class classification
	 *            samples.
	 * @param tempDocDirPath {@link String} temporary document type classification samples path.
	 * @param batchClass {@link BatchClass} batch class for which document type to insert.
	 * @return {@link List<{@link ImportExportDocument}>} if document type inserted then add inserted document type in list.
	 */
	private void deleteAndCopyDocumentFolder(final String classificationFdPath, final DocumentType deleteDocType,
			final String tempDocDirPath) {
		{
			// remove document type folder from batch image magick folder
			final File classificationFd = new File(classificationFdPath);
			FileUtils.deleteDirectoryAndContentsRecursive(new File(classificationFdPath + File.separator + deleteDocType.getName()));
			final File tempImageMagickFd = new File(tempDocDirPath);
			copyDocumentInBaseFolder(tempImageMagickFd, classificationFd);
		}
	}

	/**
	 * Method to insert document type in database.
	 * 
	 * @param docType {@link DocumentType} document type to insert.
	 * @param batchClass {@link BatchClass} batch class for which document type to insert.
	 * @return {@link List<{@link ImportExportDocument}>} if document type inserted then add inserted document type in list.
	 */
	private void insertDocumentType(DocumentType docType, final BatchClass batchClass) {
		docType.setId(0);
		docType.setBatchClass(batchClass);
		docType.setIdentifier(null);
		documentTypeService.insertDocumentType(docType);
	}

	/**
	 * Method to copy document type directory with contents.
	 * 
	 * @param srcFolder {@link File} source directory.
	 * @param destFolder {@link File} destination directory.
	 * @return
	 */
	private void copyDocumentInBaseFolder(final File srcFolder, final File destFolder) {
		String[] tempDirList = null;
		if (srcFolder.exists()) {
			tempDirList = srcFolder.list();
			if (tempDirList.length > 0) {
				try {
					FileUtils.copyDirectoryWithContents(new File(srcFolder, tempDirList[0]), new File(destFolder, tempDirList[0]));
				} catch (final IOException exception) {
					final String errorMessg = EphesoftStringUtil.concatenate(
							"Error while copying document type in image or search Classification Directory. ", exception.getMessage());
					LOGGER.error(errorMessg, exception);
				}

			}
		}
	}

	/**
	 * Method to check document type existence against document types of a batch class.
	 * 
	 * @param docType {@link DocumentType} document type whom existence to check.
	 * @param docTypeList {@link List<{@link DocumentType}>} Document type List for a batch class.
	 * @return boolean true if document type already exist then true else false.
	 */
	private DocumentType checkDocumentExistence(final DocumentType docType, final List<DocumentType> docTypeList) {
		DocumentType deleteDocType = null;
		for (int i = 0; i < docTypeList.size(); i++) {
			if (docType.getName().equalsIgnoreCase(docTypeList.get(i).getName())) {
				deleteDocType = docTypeList.get(i);
				break;
			}
		}
		return deleteDocType;
	}

	/**
	 * Method to check document type existence against document types of a batch class.
	 * 
	 * @param indexField {@link FieldType} document type whom existence to check.
	 * @param docTypeList {@link List<{@link FieldType}>} Document type List for a batch class.
	 * @return boolean true if document type already exist then true else false.
	 */
	private FieldType checkFieldTypeExistance(final FieldType indexField, final List<FieldType> fieldTypeList) {
		FieldType deleteFieldType = null;
		for (int i = 0; i < fieldTypeList.size(); i++) {
			if (indexField.getName().equalsIgnoreCase(fieldTypeList.get(i).getName())) {
				deleteFieldType = fieldTypeList.get(i);
				break;
			}
		}
		return deleteFieldType;
	}

	/**
	 * Method to insert document type in database.
	 * 
	 * @param docType {@link DocumentType} document type to insert.
	 * @param batchClass {@link BatchClass} batch class for which document type to insert.
	 */
	private DocumentType mergeDocumentType(DocumentType docType, final BatchClass batchClass) {
		docType.setId(0);
		docType.setBatchClass(batchClass);
		docType.setIdentifier(null);
		return documentTypeService.mergeDocumentType(docType);
	}

	/**
	 * Method to insert field type in database.
	 * 
	 * @param docType {@link DocumentType} document type to insert.
	 * @param batchClass {@link BatchClass} batch class for which document type to insert.
	 */
	private FieldType mergeFieldType(FieldType fieldType, final DocumentType documentType) {
		fieldType.setId(0);
		fieldType.setDocType(documentType);
		fieldType.setIdentifier(null);
		return fieldTypeService.mergeFieldType(fieldType);
	}

	/**
	 * This API is used to delete and insert document type in batch class classification samples.
	 * 
	 * @param deleteDocType {@link DocumentType} document type to delete.
	 * @param importDocType {@link DocumentType} document type to insert.
	 * @param batchClass {@link BatchClass} batch class inwhich to import document..
	 * @return {@link ImportExportDocument} return details of copied, already exist or not inserted document type.
	 */
	private ImportExportDocument removeInsertDocInImportDoc(final DocumentType deleteDocType, DocumentType importDocType,
			final BatchClass batchClass, Map<ImportExportDocument, DocumentType> documentsList) {
		ImportExportDocument impExpDoc = null;
		if (deleteDocType == null) {
			try {
				setTableColumnInfosToColumnExtractionRules(importDocType.getTableInfos());
				DocumentType newDocumentType = mergeDocumentType(importDocType, batchClass);
				impExpDoc = new ImportExportDocument(importDocType.getName(), false, false, false);
				documentsList.put(impExpDoc, newDocumentType);
			} catch (final Exception exception) {
				final String errorMessg = EphesoftStringUtil.concatenate("Error while creating document type. ",
						exception.getMessage());
				impExpDoc = new ImportExportDocument(importDocType.getName(), false, true, false);
				documentsList.put(impExpDoc, null);
				LOGGER.error(errorMessg, exception);
			}
		} else {
			impExpDoc = new ImportExportDocument(importDocType.getName(), true, false, false);
			documentsList.put(impExpDoc, null);
		}
		return impExpDoc;
	}

	/**
	 * This API is used to delete and insert document type in batch class classification samples.
	 * 
	 * @param deleteDocType {@link DocumentType} document type to delete.
	 * @param importDocType {@link DocumentType} document type to insert.
	 * @param batchClass {@link BatchClass} batch class inwhich to import document..
	 * @return {@link ImportExportDocument} return details of copied, already exist or not inserted document type.
	 */
	private ImportExportIndexField removeInsertIndexFieldImportDoc(final FieldType deleteFieldType, FieldType importFieldType,
			final DocumentType documentType, Map<ImportExportIndexField, FieldType> fieldTypeList) {
		ImportExportIndexField impExpDoc = null;
		if (deleteFieldType == null) {
			try {
				// setTableColumnInfosToColumnExtractionRules(importDocType.getTableInfos());
				FieldType newDocumentType = mergeFieldType(importFieldType, documentType);
				impExpDoc = new ImportExportIndexField(importFieldType.getName(), false, false, false);
				fieldTypeList.put(impExpDoc, newDocumentType);
			} catch (final Exception exception) {
				final String errorMessg = EphesoftStringUtil.concatenate("Error while creating field type. ", exception.getMessage());
				impExpDoc = new ImportExportIndexField(importFieldType.getName(), false, true, false);
				fieldTypeList.put(impExpDoc, null);
				LOGGER.error(errorMessg, exception);
			}
		} else {
			impExpDoc = new ImportExportIndexField(importFieldType.getName(), true, false, false);
			fieldTypeList.put(impExpDoc, null);
		}
		return impExpDoc;
	}
	
	/**
	 * updates the batch class COPY_XML plugin confiurations while upgradation from 3.X to 4.X
	 * 
	 * @param importBatchClass
	 */
	private void updateBatchForCopyXML(BatchClass importBatchClass)
	{
		if (null != importBatchClass.getBatchClassPluginsByPluginName(BatchConstants.COPY_BATCH_XML)) {
			String exportFolderPath = EMPTY_STRING;
			String exportFolderName = EMPTY_STRING;
			String exportFileName = EMPTY_STRING;
			int indexOfMultPageExportFolderPath = 0;
			int indexOfMultiPageFileName = 0;
			List<BatchClassPluginConfig> batchClassPluginConfigs = importBatchClass
					.getBatchClassPluginsByPluginName(BatchConstants.COPY_BATCH_XML).get(0).getBatchClassPluginConfigs();
			int pluginPositionIndex = 0;
			for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
				if (BatchConstants.BATCH_FOLDER_NAME.equals(batchClassPluginConfig.getName())) {
					exportFolderName = batchClassPluginConfig.getValue();
				}
				else if (BatchConstants.BATCH_FILE_NAME.equals(batchClassPluginConfig.getName())) {
					exportFileName = batchClassPluginConfig.getValue();
				}
				else if (BatchConstants.BATCH_EXPORT_FOLDER.equals(batchClassPluginConfig.getName())) {
					exportFolderPath = batchClassPluginConfig.getValue();
				}
				else if (BatchConstants.BATCH_EXPORT_FOLDER_PATH.equals(batchClassPluginConfig.getName())) {
					indexOfMultPageExportFolderPath = pluginPositionIndex;
				}
				else if (BatchConstants.BATCH_EXPORT_FILE_NAME.equals(batchClassPluginConfig.getName())) {
					indexOfMultiPageFileName = pluginPositionIndex;
				}
				pluginPositionIndex++;
			}
			if(!EphesoftStringUtil.isNullOrEmpty(exportFileName))
			{
				BatchClassPluginConfig multiPageExportFileName = batchClassPluginConfigs.get(indexOfMultiPageFileName);
				exportFileName = exportFileName
						.replaceAll(BatchConstants.OLD_FILENAME_DELIMETER, BatchConstants.NEW_FILENAME_DELIMETER)
						.replaceAll(BatchConstants.EPHESOFT_BATCH_ID, BatchConstants.BATCH_IDENTIFIER)
						.replaceAll(BatchConstants.EPHESOFT_DOC_ID, BatchConstants.DOCUMENT_IDENTIFIER);
				multiPageExportFileName.setValue(exportFileName);
				batchClassPluginConfigService.updateSinglePluginConfiguration(multiPageExportFileName);
			}
			if(!EphesoftStringUtil.isNullOrEmpty(exportFolderPath))
			{
				String resultantFolderPath = exportFolderPath;
				if(!EphesoftStringUtil.isNullOrEmpty(exportFolderName))
				{
					resultantFolderPath = EphesoftStringUtil.concatenate(resultantFolderPath,File.separator,exportFolderName);
				}
				BatchClassPluginConfig multiPageExportFolder =batchClassPluginConfigs.get(indexOfMultPageExportFolderPath);
				resultantFolderPath = resultantFolderPath.replaceAll(BatchConstants.OLD_FILENAME_DELIMETER, BatchConstants.NEW_FILENAME_DELIMETER)
						.replaceAll(BatchConstants.EPHESOFT_BATCH_ID, BatchConstants.BATCH_IDENTIFIER)
						.replaceAll(BatchConstants.EPHESOFT_DOC_ID, BatchConstants.DOCUMENT_IDENTIFIER);
				multiPageExportFolder.setValue(resultantFolderPath);
				batchClassPluginConfigService.updateSinglePluginConfiguration(multiPageExportFolder);
			}
		}
	}
	
	/**
	 *  updates the batch class CMIS_EXPORT plugin confiurations while upgradation from 3.X to 4.X
	 * 
	 * @param importBatchClass
	 */
	
	private void updateBatchForCMIS(BatchClass importBatchClass)
	{
		if (null != importBatchClass.getBatchClassPluginsByPluginName(BatchConstants.CMIS_EXPORT)) {
			BatchClassPlugin batchClassPlugin= importBatchClass.getBatchClassPluginsByPluginName(BatchConstants.CMIS_EXPORT).get(0);
			String cmisFileName = EMPTY_STRING;
			String cmisFilePath = EMPTY_STRING;
			int CMISExportFileIndex = 0 ;
			int CMISExportFolderPathIndex = 0 ; 
			List<BatchClassPluginConfig> batchClassPluginConfigs = importBatchClass
					.getBatchClassPluginsByPluginName(BatchConstants.CMIS_EXPORT).get(0).getBatchClassPluginConfigs();
			int pluginPositionIndex = 0;
			for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
				if (BatchConstants.CMIS_FILE_NAME.equals(batchClassPluginConfig.getName())) {
					
					cmisFileName = batchClassPluginConfig.getValue();
					if(EphesoftStringUtil.isNullOrEmpty(cmisFileName)){
						cmisFileName=EphesoftStringUtil.concatenate(BatchConstants.CMIS_EXPORT_FILE_NAME_SUFFIX,BatchConstants.BATCH_IDENTIFIER);
					}
					cmisFileName = cmisFileName
							.replaceAll(BatchConstants.OLD_FILENAME_DELIMETER, BatchConstants.NEW_FILENAME_DELIMETER)
							.replaceAll(BatchConstants.EPHESOFT_BATCH_ID, BatchConstants.BATCH_IDENTIFIER)
							.replaceAll(BatchConstants.EPHESOFT_DOC_ID, BatchConstants.DOCUMENT_IDENTIFIER);
					CMISExportFileIndex = pluginPositionIndex;
				}
				else if(BatchConstants.CMIS_ROOT_FOLDER.equals(batchClassPluginConfig.getName())) {
					cmisFilePath = batchClassPluginConfig.getValue();
					cmisFilePath = cmisFilePath
							.replaceAll(BatchConstants.OLD_FILENAME_DELIMETER, BatchConstants.NEW_FILENAME_DELIMETER)
							.replaceAll(BatchConstants.EPHESOFT_BATCH_ID, BatchConstants.BATCH_IDENTIFIER)
							.replaceAll(BatchConstants.EPHESOFT_DOC_ID, BatchConstants.DOCUMENT_IDENTIFIER);
					CMISExportFolderPathIndex = pluginPositionIndex;
				}
				pluginPositionIndex++;
			}
			List<BatchClassPluginConfig> updatedBatchClassPluginList = new ArrayList<BatchClassPluginConfig>(2);
			if(!EphesoftStringUtil.isNullOrEmpty(cmisFileName))
			{
				BatchClassPluginConfig cmisFileNamePluginConfig = batchClassPluginConfigs.get(CMISExportFileIndex);
				cmisFileNamePluginConfig.setBatchClassPlugin(batchClassPlugin);
				cmisFileNamePluginConfig.setValue(cmisFileName);
				updatedBatchClassPluginList.add(cmisFileNamePluginConfig);
			}
			if(!EphesoftStringUtil.isNullOrEmpty(cmisFilePath))
			{
				BatchClassPluginConfig cmisFilePathPluginConfig = batchClassPluginConfigs.get(CMISExportFolderPathIndex);
				cmisFilePathPluginConfig.setBatchClassPlugin(batchClassPlugin);
				cmisFilePathPluginConfig.setValue(cmisFilePath);
				updatedBatchClassPluginList.add(cmisFilePathPluginConfig);
			}
			batchClassPluginConfigService.updatePluginConfiguration(updatedBatchClassPluginList);
		}
	}
}
