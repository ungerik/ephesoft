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

package com.ephesoft.gxt.admin.server;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.Store;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.IOUtils;
import org.springframework.dao.DataAccessException;

import com.ephesoft.dcma.barcode.BarcodeProperties;
import com.ephesoft.dcma.barcode.service.BarcodeService;
import com.ephesoft.dcma.barcodeextraction.service.BarcodeExtractionService;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.encryption.service.EncryptionKeyService;
import com.ephesoft.dcma.batch.encryption.util.CryptoMarshaller;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Batch.DocumentClassificationTypes;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.Direction;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Documents;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Folders;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.Scripts;
import com.ephesoft.dcma.batch.schema.ObjectFactory;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.batch.service.BatchClassPluginPropertiesService;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.ImportBatchService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.common.ImportBatchClassResultCarrier;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.ConnectionType;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.ImportExportDocument;
import com.ephesoft.dcma.core.common.ImportExportIndexField;
import com.ephesoft.dcma.core.common.Order;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.BatchAlreadyLockedException;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao;
import com.ephesoft.dcma.core.hibernate.DynamicHibernateDao.ColumnDefinition;
import com.ephesoft.dcma.core.hibernate.util.HibernateDaoUtil;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassEmailConfiguration;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.Connections;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.LockStatus;
import com.ephesoft.dcma.da.domain.Module;
import com.ephesoft.dcma.da.domain.Plugin;
import com.ephesoft.dcma.da.domain.RegexGroup;
import com.ephesoft.dcma.da.domain.ScannerMasterConfiguration;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.domain.TableRuleInfo;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.service.BatchClassEmailConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassPluginService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.ConnectionsService;
import com.ephesoft.dcma.da.service.LockStatusService;
import com.ephesoft.dcma.da.service.MasterScannerService;
import com.ephesoft.dcma.da.service.ModuleService;
import com.ephesoft.dcma.da.service.PageTypeService;
import com.ephesoft.dcma.da.service.PluginConfigService;
import com.ephesoft.dcma.da.service.PluginService;
import com.ephesoft.dcma.da.service.RegexGroupService;
import com.ephesoft.dcma.da.service.TableColumnsInfoService;
import com.ephesoft.dcma.da.service.TableRuleInfoService;
import com.ephesoft.dcma.docassembler.DocumentAssembler;
import com.ephesoft.dcma.docassembler.DocumentAssemblerProperties;
import com.ephesoft.dcma.docassembler.constant.DocumentAssemblerConstants;
import com.ephesoft.dcma.docassembler.factory.DocumentClassificationFactory;
import com.ephesoft.dcma.encryption.constant.CryptographyConstant;
import com.ephesoft.dcma.encryption.util.EphesoftEncryptionUtil;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.constant.ImageMagicKConstants;
import com.ephesoft.dcma.imagemagick.imageClassifier.SampleThumbnailGenerator;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.imp.FolderImporter;
import com.ephesoft.dcma.kvextraction.service.KVExtractionService;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.KeyValueFieldCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.kvfinder.service.KVFinderService;
import com.ephesoft.dcma.lucene.LuceneProperties;
import com.ephesoft.dcma.lucene.service.SearchClassificationService;
import com.ephesoft.dcma.ocr.OCRUtil;
import com.ephesoft.dcma.regex.service.ExtractionService;
import com.ephesoft.dcma.regexpp.service.RegexService;
import com.ephesoft.dcma.tableextraction.service.TableExtractionService;
import com.ephesoft.dcma.tablefinder.service.TableFinderService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileStreamResult;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.MailUtil;
import com.ephesoft.dcma.util.PDFUtil;
import com.ephesoft.dcma.util.TIFFUtil;
import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;
import com.ephesoft.dcma.util.exception.KeyGenerationException;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;
import com.ephesoft.dcma.workflows.service.DeploymentService;
import com.ephesoft.gxt.admin.client.BatchClassManagementService;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.i18n.PluginNameConstants;
import com.ephesoft.gxt.admin.client.presenter.BatchClassPresenter.Results;
import com.ephesoft.gxt.admin.server.util.RegexUtil;
import com.ephesoft.gxt.core.server.BatchClassUtil;
import com.ephesoft.gxt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.gxt.core.shared.CategorisedData;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.constant.BatchConstants;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassListDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassModuleDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginConfigDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginDTO;
import com.ephesoft.gxt.core.shared.dto.BatchFolderListDTO;
import com.ephesoft.gxt.core.shared.dto.ConnectionsDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.EmailConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.ImportBatchClassUserOptionDTO;
import com.ephesoft.gxt.core.shared.dto.KVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.LockStatusDTO;
import com.ephesoft.gxt.core.shared.dto.ModuleDTO;
import com.ephesoft.gxt.core.shared.dto.OutputDataCarrierDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.dto.RoleDTO;
import com.ephesoft.gxt.core.shared.dto.RuleInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TestClassificationDataCarrierDTO;
import com.ephesoft.gxt.core.shared.dto.TestTableResultDTO;
import com.ephesoft.gxt.core.shared.dto.ViewLearnFileDTO;
import com.ephesoft.gxt.core.shared.exception.UIArgumentException;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.ephesoft.gxt.core.shared.importTree.Node;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoadResultBean;

public class BatchClassManagementServiceImpl extends DCMARemoteServiceServlet implements BatchClassManagementService {

	/**
	 * Serialized version UID of the class;
	 */
	private static final long serialVersionUID = 1L;

	private static int DEFAULT_BATCH_CLASS_ROWS = 15;

	private static EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(BatchClassManagementServiceImpl.class);

	private static final String ROW_COUNT = "row_count";
	private static final String BATCH_CLASS_PLUGIN_PROPERTIES_SERVICE = "batchClassPluginPropertiesService";
	private static final String BARCODE_1 = "BARCODE_1";
	private static final String BARCODE_CLASSIFICATION_TAG = "Barcode Classification";
	private static final String IMAGE_CLASSIFICATION_TAG = "Image Classification";
	private static final String IMAGE_COMPARE_CLASSIFICATION = "IMAGE_COMPARE_CLASSIFICATION";
	private static final String AUTOMATIC_CLASSIFICATION_TAG = "Automatic Classification";
	private static final String SEARCH_CLASSIFICATION_TAG = "Search Classification";
	private static final String AUTOMATIC_CLASSIFICATION = "AUTOMATIC_CLASSIFICATION";
	private static final String SEARCH_ENGINE_CLASSIFICATION = "SEARCH_ENGINE_CLASSIFICATION";

	private static final String DOC_ASSEMBLER = "docAssembler";
	private static final String DELETED_UNC_KEYWORD = "--deleted";
	private static final String URL_SEPARATOR = "/";
	private static final String FIRST_PAGE = "_First_Page";
	private static final String MIDDLE_PAGE = "_Middle_Page";
	private static final String LAST_PAGE = "_Last_Page";

	@Override
	public PagingLoadResult<BatchClassDTO> getBatchClasses(int offset, final int totalVisibleRows, Order order) {
		Set<String> userRoles = getUserRoles();
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		// int maxResults = this.getTotalVisibleBatchClasses();
		int maxResults = totalVisibleRows;
		List<BatchClass> batchList = batchClassService.getBatchClassList(offset, maxResults, order, userRoles);
		List<BatchClassDTO> batchClassDTOs = convertToBatchClassDTOs(batchList);
		int totalBatchClasses = this.countAllBatchClassesExcludeDeleted();
		return new PagingLoadResultBean<BatchClassDTO>(batchClassDTOs, totalBatchClasses, offset);
	}

	/*
	 * The following line has been removed from the method to decrease the loading time of batch classes.
	 * batchClassDTO.setDeployed(isBatchClassDeployed(batchClassDTO));
	 */
	private List<BatchClassDTO> convertToBatchClassDTOs(List<BatchClass> batchList) {
		List<BatchClassDTO> batchClassDTOs = new ArrayList<BatchClassDTO>();
		for (BatchClass batchClass : batchList) {
			BatchClassDTO batchClassDTO = new BatchClassDTO();
			batchClassDTO.setId(batchClass.getId());
			batchClassDTO.setIdentifier(batchClass.getIdentifier());
			batchClassDTO.setDescription(batchClass.getDescription());
			batchClassDTO.setName(batchClass.getName());
			batchClassDTO.setPriority(batchClass.getPriority());
			batchClassDTO.setUncFolder(batchClass.getUncFolder());
			batchClassDTO.setVersion(batchClass.getVersion());
			batchClassDTO.setCurrentUser(batchClass.getCurrentUser() != null ? batchClass.getCurrentUser() : "");
			final PrivateKeyEncryptionAlgorithm encryptionAlgorithm = batchClass.getEncryptionAlgorithm();
			final String algorithmName = encryptionAlgorithm == null ? null : encryptionAlgorithm.toString();
			batchClassDTO.setEncryptionAlgo(algorithmName);

			// Adding Roles Assigned to Batches in DTO
			List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
			if (!CollectionUtil.isEmpty(batchClass.getAssignedGroups())) {
				RoleDTO roleDTO;
				for (BatchClassGroups batchClassGroup : batchClass.getAssignedGroups()) {
					roleDTO = new RoleDTO();
					roleDTO.setName(batchClassGroup.getGroupName());
					roleDTOs.add(roleDTO);
				}
			}

			batchClassDTO.setAssignedRole(roleDTOs);

			batchClassDTOs.add(batchClassDTO);
		}
		return batchClassDTOs;
	}

	public int countAllBatchClassesExcludeDeleted() {
		Set<String> userRoles = null;
		// Bug fix for JIRA ID - 8953 the check for super admin before getting user roles is removed.
		userRoles = getUserRoles();
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		return batchClassService.countAllBatchClassesExcludeDeleted(userRoles);
	}

	public int getTotalVisibleBatchClasses() {
		int defaultRows = DEFAULT_BATCH_CLASS_ROWS;
		try {
			ApplicationConfigProperties properties = ApplicationConfigProperties.getApplicationConfigProperties();
			String propertyValue = properties.getProperty(ROW_COUNT);
			if (!EphesoftStringUtil.isNullOrEmpty(propertyValue)) {
				defaultRows = Integer.parseInt(propertyValue);
			}
		} catch (final IOException ioException) {
			LOGGER.error(" Cannot execute read batch class rows count ", ioException);
		}
		return defaultRows;
	}

	@Override
	public BatchClassDTO getBatchClass(String batchClassIdentifier) {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		MasterScannerService masterScannerService = this.getSingleBeanOfType(MasterScannerService.class);
		List<ScannerMasterConfiguration> masterScannerConfig = masterScannerService.getMasterConfigurations();

		BatchClassDTO batchClassDTO = BatchClassUtil.createBatchClassDTO(masterScannerConfig, batchClass, pluginService);
		Boolean isBatchClassDeployed = isBatchClassDeployed(batchClassDTO);
		batchClassDTO.setDeployed(isBatchClassDeployed);

		// Setting the list of all the table columns from pool to the batchClassDTO object.
		// batchClassDTO.setTablePoolColumnsInfoDTOList(getAllTableColumnsFromPool());
		batchClassDTO.setListLockStatusDTOs(getAllFeaturesLockStatus());
		if (!isBatchClassDeployed) {
			batchClassDTO.setDirty(true);
		}
		// Following is added for regex pool.
		batchClassDTO.setRegexPoolDirty(false);
		Map<String, RegexGroupDTO> regexGroupDTOMap = getRegexGroupMap();
		batchClassDTO.setRegexGroupMap(regexGroupDTOMap);
		return batchClassDTO;
	}

	private Boolean isBatchClassDeployed(BatchClassDTO batchClassDTO) {
		Boolean isBatchClassDeployed = false;
		DeploymentService deploymentService = this.getSingleBeanOfType(DeploymentService.class);
		LOGGER.info("Checking if the batch class with identifier as :" + batchClassDTO.getIdentifier() + " is deployed or not");
		isBatchClassDeployed = deploymentService.isDeployed(batchClassDTO.getName());
		LOGGER.info("Batch class with identifier  is deployed :" + batchClassDTO.getIdentifier());
		return isBatchClassDeployed;

	}

	// public Map<String, RegexGroupDTO> getRegexGroupMap() {
	// Map<String, RegexGroupDTO> regexGroupDTOMap = new LinkedHashMap<String, RegexGroupDTO>();
	// List<RegexGroupDTO> listOfGroupDTOs = getRegexGroupDTOList();
	// for (RegexGroupDTO groupDTO : listOfGroupDTOs) {
	// if (null != groupDTO) {
	// regexGroupDTOMap.put(groupDTO.getIdentifier(), groupDTO);
	// }
	// }
	// return regexGroupDTOMap;
	// }

	private List<LockStatusDTO> getAllFeaturesLockStatus() {
		List<LockStatusDTO> listLockStatusDTOs = null;
		final LockStatusService lockStatusService = this.getSingleBeanOfType(LockStatusService.class);
		if (this.getThreadLocalRequest().getUserPrincipal() != null) {
			listLockStatusDTOs = convertLockStatusToLockStatusDTO(lockStatusService.getAllFeaturesLockedByUser(this
					.getThreadLocalRequest().getUserPrincipal().getName()));
		}
		return listLockStatusDTOs;
	}

	private List<LockStatusDTO> convertLockStatusToLockStatusDTO(final List<LockStatus> listLockStatus) {
		List<LockStatusDTO> listLockStatusDTO = null;
		if (listLockStatus != null && !listLockStatus.isEmpty()) {
			listLockStatusDTO = new ArrayList<LockStatusDTO>(listLockStatus.size());
			for (LockStatus lockStatus : listLockStatus) {
				if (lockStatus != null) {
					listLockStatusDTO.add(BatchClassUtil.mapLockStatusToLockStatusDTO(lockStatus));
				}
			}
		}
		return listLockStatusDTO;
	}

	@Override
	public Results deleteBatchClasses(List<BatchClassDTO> selectedBatchClasses) {
		Results returnValue = Results.FAILURE;
		int totalClasses = selectedBatchClasses.size();
		int failedDeletions = 0;
		for (BatchClassDTO batchClass : selectedBatchClasses) {
			if (null != batchClass) {
				if (deleteBatchClass(batchClass) == Results.FAILURE) {
					failedDeletions++;
				}
			} else {
				failedDeletions++;
				LOGGER.error("Batch class DTO is null. Batch class cannot be deleted.");
			}
		}
		if (failedDeletions == totalClasses) {
			returnValue = Results.FAILURE;
		} else if (failedDeletions == 0) {
			returnValue = Results.SUCCESSFUL;
		} else {
			returnValue = Results.PARTIAL_SUCCESS;
		}
		return returnValue;
	}

	private Results deleteBatchClass(BatchClassDTO batchClassDTO) {
		Results returnVal = Results.SUCCESSFUL;
		final String batchClassIdentifier = batchClassDTO.getIdentifier();
		final BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.get(batchClassIdentifier);

		if (checkIfBatchClassIsUnlocked(batchClass, true)) {

			final String originalUncFolderPath = batchClassDTO.getUncFolder();
			final String deletedUncFolderPath = EphesoftStringUtil.concatenate(originalUncFolderPath, ICommonConstants.HYPHEN,
					batchClassDTO.getIdentifier(), DELETED_UNC_KEYWORD);
			LOGGER.debug(EphesoftStringUtil.concatenate("Original unc folder path is: ", originalUncFolderPath,
					". Deleted unc folder path is: ", deletedUncFolderPath));

			boolean uncDeleted = FileUtils.renameFolder(originalUncFolderPath, deletedUncFolderPath);
			if (uncDeleted) {
				LOGGER.info("Moving of Directory and its contents of uncFolder is successful");
			} else {
				LOGGER.error("Moving of Directory and its contents of uncFolder is failed");
			}
			batchClass.setDeleted(true);
			batchClass.setUncFolder(deletedUncFolderPath);
			deleteEmailConfigForBatchClass(batchClass);
			batchClass = batchClassService.merge(batchClass, batchClassDTO.isDeleted());
			final MasterScannerService masterScannerService = this.getSingleBeanOfType(MasterScannerService.class);
			final List<ScannerMasterConfiguration> masterScannerConfig = masterScannerService.getMasterConfigurations();
			final PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
			batchClassDTO = BatchClassUtil.createBatchClassDTO(masterScannerConfig, batchClass, pluginService);
			final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
			batchInstanceService.unlockAllBatchInstancesForCurrentUser(getUserName());
		} else {
			returnVal = Results.FAILURE;
		}
		return returnVal;
	}

	/**
	 * It checks whether a batch class is locked by any user or not.
	 * 
	 * @param batchClassIdentifier {@link String } value containing identifier of batch class.
	 * @param batchClass an object of {@link BatchClass} on which locking is to be checked.
	 * @param isDelete
	 * @throws GWTException This Exception is thrown in case Batch class is already deleted or lock is already acquired on batch class
	 *             by some other user.
	 */
	private boolean checkIfBatchClassIsUnlocked(BatchClass batchClass, boolean isDelete) {
		boolean returnValue = true;
		final String batchClassIdentifier = batchClass.getIdentifier();
		LOGGER.debug(EphesoftStringUtil.concatenate("Cheking if batch class with identifier: ", batchClassIdentifier,
				" is locked or not"));
		if (batchClass == null || batchClass.isDeleted()) {
			LOGGER.info(EphesoftStringUtil.concatenate("Error occured while getting batch class :", batchClassIdentifier,
					". Batch class is null or is deleted by some other user."));
			returnValue = false;
		} else {
			final String batchClassCurrentUser = batchClass.getCurrentUser();
			if (batchClassCurrentUser != null) {
				if (isDelete) {
					final String currentUser = getUserName();
					LOGGER.error(EphesoftStringUtil.concatenate("Batch class's current user is: ", batchClassCurrentUser,
							". User name from authentication is: ", currentUser));
					if (!batchClassCurrentUser.equalsIgnoreCase(currentUser) && !isSuperAdmin()) {
						LOGGER.error(EphesoftStringUtil.concatenate("Batch class is already unlocked :", batchClassIdentifier));
						returnValue = false;
					}
				} else {
					returnValue = false;
				}
			}
		}
		return returnValue;
	}

	private void deleteEmailConfigForBatchClass(final BatchClass batchClass) {
		BatchClassEmailConfigService batchClassEmailConfigService = this.getSingleBeanOfType(BatchClassEmailConfigService.class);
		List<BatchClassEmailConfiguration> emailConfigList = batchClassEmailConfigService
				.getEmailConfigByBatchClassIdentifier(batchClass.getIdentifier());
		for (BatchClassEmailConfiguration batchClassEmailConfiguration : emailConfigList) {
			batchClassEmailConfigService.removeEmailConfiguration(batchClassEmailConfiguration);
		}
	}

	@Override
	public Results clearCurrentUser(List<BatchClassDTO> selectedBatchClasses) {
		Results returnValue = Results.FAILURE;
		int totalClasses = selectedBatchClasses.size();
		int failedUnlocks = 0;
		final BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		for (BatchClassDTO batchClassDTO : selectedBatchClasses) {
			BatchClass batchClass = batchClassService.get(batchClassDTO.getIdentifier());
			if (null != batchClass) {
				if (!batchClass.isDeleted()) {
					if (!checkIfBatchClassIsUnlocked(batchClass, false)) {
						String currentUser = getUserName();
						if (currentUser.equalsIgnoreCase(batchClass.getCurrentUser()) || isSuperAdmin()) {
							batchClassService.clearCurrentUser(batchClass.getIdentifier());
						} else {
							failedUnlocks++;
							LOGGER.error("A non-super admin cannot unlock batch class locked by super-admin.");
						}
					}
				} else {
					failedUnlocks++;
					LOGGER.error("Batch class is deleted by some other user.It Cannot be unlocked.");
				}
			} else {
				failedUnlocks++;
				LOGGER.error("Batch class DTO is null. Batch class cannot be unlocked.");
			}
		}
		if (failedUnlocks == totalClasses) {
			returnValue = Results.FAILURE;
		} else if (failedUnlocks == 0) {
			returnValue = Results.SUCCESSFUL;
		} else {
			returnValue = Results.PARTIAL_SUCCESS;
		}
		return returnValue;
	}

	@Override
	public Boolean isBtachesInRunningOrErrorState(String batchClassIdentifier) throws Exception {
		Boolean returnValue = Boolean.FALSE;
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		if (null != batchClass) {
			List<BatchInstance> listOfBatches = batchInstanceService.getAllUnFinishedBatchInstances(batchClass);
			if (null != listOfBatches && listOfBatches.size() > 0) {
				returnValue = Boolean.TRUE;
			}
		}
		return returnValue;
	}

	@Override
	public String getCurrentBatchClassEncryptionAlgo(String batchClassIdentifier) {
		String returnValue = AdminConstants.EMPTY_STRING;
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		if (null != batchClass && null != batchClass.getEncryptionAlgorithm()) {
			returnValue = batchClass.getEncryptionAlgorithm().name();
		}
		return returnValue;
	}

	@Override
	public int generateBatchClassLevelKey(String batchClassKey, String selectedEncryptionAlgo, String batchClassIdentifier)
			throws Exception {
		int returnValue = 0;
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		PrivateKeyEncryptionAlgorithm batchClassEncryptionAlgo = batchClass.getEncryptionAlgorithm();
		if (null != batchClass) {
			try {
				EncryptionKeyService encryptionService = this.getSingleBeanOfType(EncryptionKeyService.class);
				// remove previous key present if any.
				encryptionService.removeBatchClassKey(batchClassIdentifier);
				batchClass.setEncryptionAlgorithm(encryptionService.getPrivateKeyAlgorithm(selectedEncryptionAlgo));
				batchClassService.saveOrUpdate(batchClass);
				// generate new key.
				encryptionService.generateBatchClassKey(batchClassIdentifier, batchClassKey.getBytes());
				if ((null == batchClass.getEncryptionAlgorithm() && null != batchClassEncryptionAlgo)
						|| batchClassEncryptionAlgo != batchClass.getEncryptionAlgorithm()) {
					// re learn files.
					returnValue = reLearnFiles(batchClassIdentifier);
				}
			} catch (KeyGenerationException keyGenException) {
				LOGGER.error(keyGenException.getMessage(), keyGenException);
				throw new Exception("Problem generating the key : " + keyGenException.getMessage());
			} catch (Exception exception) {
				LOGGER.error(exception.getMessage(), exception);
				throw new Exception("Problem generating the key : " + exception.getMessage());
			}
		}
		return returnValue;
	}

	/**
	 * @param batchClassIdentifier
	 * @throws Exception
	 * @throws DCMAException
	 * @throws GWTException
	 */
	private int reLearnFiles(String batchClassIdentifier) throws DCMAException, Exception {
		int returnValue = 0;
		return returnValue;
	}

	@Override
	public void sampleGeneration(List<String> batchClassIDList) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		PageTypeService pageTypeService = this.getSingleBeanOfType(PageTypeService.class);

		List<List<String>> batchIdDocPgNameList = pageTypeService.getDocTypeNameAndPgTypeName(batchClassIDList);
		try {
			batchSchemaService.sampleGeneration(batchIdDocPgNameList);
		} catch (DCMAApplicationException exception) {

		}
	}

	@Override
	public void learnFileForBatchClass(String batchClassID) throws Exception {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		File batchClassFolder = new File(EphesoftStringUtil.concatenate(batchSchemaService.getBaseFolderLocation(), File.separator,
				batchClassID));
		if (!batchClassFolder.exists()) {
			return;
		}
		SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);
		searchClassificationService.learnSampleHOCRFilesUsingSelectedPlugin(batchClassID, true);
		sampleThumbnailGenerator(batchClassID);
	}

	private void sampleThumbnailGenerator(String batchClassIdentifier) throws DCMAApplicationException {

		String sampleBaseFolderPath = null;
		String thumbnailType = null;
		String thumbnailH = null;
		String thumbnailW = null;

		if (batchClassIdentifier == null || batchClassIdentifier.equals(BatchClassConstants.ZERO)) {
			throw new DCMAApplicationException("In valid Batch Class ID");
		} else {
			BatchClassPluginConfigService batchClassPluginConfigService = this
					.getSingleBeanOfType(BatchClassPluginConfigService.class);
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			Map<String, String> properties = batchClassPluginConfigService.getPluginPropertiesForBatchClass(batchClassIdentifier,
					PluginNameConstants.CREATE_THUMBNAILS_PLUGIN, null);
			if (properties != null && properties.size() > 0) {
				sampleBaseFolderPath = batchSchemaService.getImageMagickBaseFolderPath(batchClassIdentifier, false);
				if (!(sampleBaseFolderPath != null && sampleBaseFolderPath.length() > 0)) {
					LOGGER.error("Error retreiving sampleBaseFolderPath");
					throw new DCMAApplicationException("Error retreiving sampleBaseFolderPath");
				}
				thumbnailH = properties.get(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_HEIGHT.getPropertyKey());
				if (!(thumbnailH != null && thumbnailH.length() > 0)) {
					LOGGER.error("Error retreiving thumbnailH");
					throw new DCMAApplicationException("Error retreiving thumbnailH");
				}
				thumbnailType = properties.get(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_TYPE.getPropertyKey());
				if (!(thumbnailType != null && thumbnailType.length() > 0)) {
					LOGGER.error("Error retreiving thumbnailType");
					throw new DCMAApplicationException("Error retreiving thumbnailType");
				}
				thumbnailW = properties.get(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_WIDTH.getPropertyKey());
				if (!(thumbnailW != null && thumbnailW.length() > 0)) {
					LOGGER.error("Error retreiving thumbnailW");
					throw new DCMAApplicationException("Error retreiving thumbnailW");
				}
			} else {
				LOGGER.error("No Image Magic Properties found in DB. Could not generate sample Thumbnails.");
				return;
			}
		}

		SampleThumbnailGenerator sampleThumbnailGenerator = new SampleThumbnailGenerator(sampleBaseFolderPath, thumbnailType,
				thumbnailH, thumbnailW);
		sampleThumbnailGenerator.generateAllThumbnails(batchClassIdentifier);

	}

	/**
	 * @param batchClassID
	 * @param inputFilesPath
	 * @param searchClassificationService
	 * @return
	 * @throws DCMAException
	 */
	private CustomFileFilter ocrInputFiles(String batchClassID, String inputFilesPath,
			SearchClassificationService searchClassificationService, boolean isAdvKVExtracton) throws DCMAException {
		CustomFileFilter filter = null;
		if (null != inputFilesPath) {
			File inputFolder = new File(inputFilesPath);
			File[] listOfimages = inputFolder.listFiles(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
					.getExtensionWithDot()));
			String[] listOfHOCRFiles = inputFolder.list(new CustomFileFilter(false, FileType.XML.getExtensionWithDot()));
			// perform ocr-ing for the pages.
			filter = new CustomFileFilter(false, FileType.XML.getExtension());
			if (listOfimages.length != listOfHOCRFiles.length) {
				String ocrEngineName = getOCRPluginNameForBatchClass(batchClassID);

				if (!inputFolder.exists()) {
					inputFolder.mkdirs();
				}
				if (null != inputFolder) {
					// remove redundant thumbnail files.
					deleteThumbnailFiles(inputFilesPath);
					// The underlying API will check which files are not OCR-ed in the folder.
					searchClassificationService.generateHOCRForKVExtractionTest(inputFilesPath, ocrEngineName, batchClassID,
							inputFolder, isAdvKVExtracton);
				}
			}
		}
		return filter;
	}

	private String getOCRPluginNameForBatchClass(String batchClassIdentifier) {
		LOGGER.info("Fetching the ocr engine to be used for learning.");
		String defaultOcrEngineName = getDefaultHOCRPlugin(batchClassIdentifier);
		LOGGER.info(EphesoftStringUtil.concatenate("OCR Engine used is = ", defaultOcrEngineName));
		return defaultOcrEngineName;
	}

	/**
	 * Deletion of thumbnail files present inside provided folder path.
	 * 
	 * @param inputFilesPath {@link String} the path of existing folder already checked for deleting the thumbnail files present
	 *            inside.
	 */
	private void deleteThumbnailFiles(String inputFilesPath) {
		File imageFolderPath = new File(inputFilesPath);
		if (null != imageFolderPath) {
			File[] listOfimages = imageFolderPath.listFiles(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
					FileType.TIFF.getExtensionWithDot()));
			if (null != listOfimages) {
				for (File tiffFile : listOfimages) {
					if (tiffFile.getName().contains(BatchClassConstants.THUMBNAILS_CONSTANT)
							|| tiffFile.getName().contains(BatchClassConstants.THUMBNAILS_HYPHEN_CONSTANT)) {
						tiffFile.delete();
					}
				}
			}
		}
	}

	private String getDefaultHOCRPlugin(final String batchClassIdentifier) {
		OCRUtil ocrUtil = this.getSingleBeanOfType(OCRUtil.class);
		return ocrUtil.getFirstOnOCRPlugin(batchClassIdentifier);
	}

	@Override
	public List<BatchClassDTO> getAllBatchClasses() {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		List<BatchClass> batchList = batchClassService.getAllBatchClassesExcludeDeleted();
		List<BatchClassDTO> batchClassDTOs = convertToBatchClassDTOs(batchList);
		return batchClassDTOs;
	}

	@Override
	public void createUncFolder(String path) throws Exception {
		File file = new File(path);
		// if (!file.exists()) {
		if (!file.exists()) {
			boolean success = file.mkdirs();
			if (!success) {
				throw new Exception("Unable to create directory.");
			}
		}
		// Added functionality to check whether UNC folder path is empty or not
		else {
			File[] listOfFiles = file.listFiles();
			if ((listOfFiles.length > 0)) {
				throw new Exception("UNC folder path is not empty");
			}
		}
	}

	@Override
	public String copyBatchClass(BatchClassDTO batchClassDTO, Boolean isGridWorkflow) throws Exception {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		MasterScannerService masterScannerService = this.getSingleBeanOfType(MasterScannerService.class);
		BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchClassDTO.getIdentifier());
		List<ScannerMasterConfiguration> masterScannerConfig = masterScannerService.getMasterConfigurations();
		BatchClassUtil.createBatchClassDTO(masterScannerConfig, batchClass, pluginService);
		batchClassService.evict(batchClass);
		String identifier = copyAndSaveBatchClass(batchClassDTO, batchClassService, batchClass, isGridWorkflow);
		return identifier;
	}

	private String copyAndSaveBatchClass(BatchClassDTO batchClassDTO, BatchClassService batchClassService, BatchClass batchClass,
			boolean isGridWorkflow) throws Exception {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String identifier = batchClass.getIdentifier();
		setBatchClassInfo(batchClassDTO, batchClass);
		BatchClassUtil.copyModules(batchClass);
		BatchClassUtil.copyDocumentTypes(batchClass);
		BatchClassUtil.copyScannerConfig(batchClass);
		BatchClassUtil.copyBatchClassField(batchClass);
		BatchClassUtil.copyCMISConfigurations(batchClass);

		File originalFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator + identifier);
		batchClass = batchClassService.createBatchClass(batchClass);
		batchClassService.evict(batchClass);
		File copiedFolder = new File(batchSchemaService.getBaseSampleFDLock() + File.separator + batchClass.getIdentifier());
		try {
			FileUtils.copyDirectoryWithContents(originalFolder, copiedFolder);
			FileUtils.deleteFileOfType(copiedFolder.getAbsolutePath(), FileType.SER.getExtensionWithDot());
		} catch (IOException e) {
			throw new Exception("Unable to create learning folders");
		}

		// Deploy Workflow
		try {
			deployNewBatchClass(batchClass.getIdentifier(), isGridWorkflow);
		} catch (DCMAException e) {
			throw new UIException("Unable to deploy workflow");
		}
		return batchClass.getIdentifier();
	}

	private void deployNewBatchClass(String identifier, boolean isGridWorkflow) throws DCMAException {
		LOGGER.info("Deploying the newly copied batch class");
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		DeploymentService deploymentService = this.getSingleBeanOfType(DeploymentService.class);
		BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(identifier);
		renameBatchClassModules(batchClass);
		batchClass = batchClassService.merge(batchClass);
		deploymentService.createAndDeployProcessDefinition(batchClass, isGridWorkflow);
	}

	/**
	 * API to rename the modules of given batch class (appending module names with batch class identifier)
	 * 
	 * @param batchClass
	 */
	private void renameBatchClassModules(BatchClass batchClass) {
		String existingBatchClassIdentifier = batchClass.getIdentifier();
		for (BatchClassModule batchClassModule : batchClass.getBatchClassModules()) {
			String existingModuleName = batchClassModule.getModule().getName();
			StringBuffer newWorkflowNameStringBuffer = new StringBuffer();
			newWorkflowNameStringBuffer.append(existingModuleName.replaceAll(AdminConstants.SPACE, AdminConstants.UNDERSCORE));
			newWorkflowNameStringBuffer.append(AdminConstants.UNDERSCORE);
			newWorkflowNameStringBuffer.append(existingBatchClassIdentifier);
			batchClassModule.setWorkflowName(newWorkflowNameStringBuffer.toString());
		}
	}

	private void setBatchClassInfo(BatchClassDTO batchClassDTO, BatchClass batchClass) {
		EncryptionKeyService encryptionService = this.getSingleBeanOfType(EncryptionKeyService.class);
		batchClass.setDescription(batchClassDTO.getDescription());
		batchClass.setPriority(batchClassDTO.getPriority());
		batchClass.setUncFolder(batchClassDTO.getUncFolder());
		batchClass.setName(batchClassDTO.getName());

		// Setting batch class creation date as current time.
		Date currentDate = new Date(System.currentTimeMillis());
		batchClass.setCreationDate(currentDate);
		batchClass.setId(0);
		batchClass.setIdentifier(null);
		batchClass.setCurrentUser(null);
		batchClass.setVersion(AdminConstants.VERSION);
		batchClass.setEmailConfigurations(null);
		List<BatchClassGroups> batchClassGroupsList = batchClass.getAssignedGroups();
		for (BatchClassGroups batchClassGroups : batchClassGroupsList) {
			batchClassGroups.setId(0);
		}
		batchClass.setAssignedGroups(batchClassGroupsList);
		batchClass.setDeleted(false);
		// add algorithm previous set if any.
		batchClass.setEncryptionAlgorithm(encryptionService.getPrivateKeyAlgorithm(batchClassDTO.getEncryptionAlgo()));
	}

	/**
	 * It checks whether a batch class is unlocked by super user or not.
	 * 
	 * @param batchClassIdentifier {@link String } identifier of the batch class.
	 * @param batchClass an object of {@link BatchClass} on which locking is to be checked.
	 * @throws {@link GWTException} thrown in case Batch class is already deleted or batch class is unlocked by super user or lock is
	 *         already acquired on batch class by some other user.
	 */
	private void checkIfBatchClassUnLockedBySuperUser(final String batchClassIdentifier, final BatchClass batchClass)
			throws UIException {
		LOGGER.debug(EphesoftStringUtil.concatenate("Cheking if batch class with identifier: ", batchClassIdentifier,
				" is unlocked by super user or not"));

		if (batchClass == null || batchClass.isDeleted()) {
			LOGGER.error(EphesoftStringUtil.concatenate("Error occured while getting batch class :", batchClassIdentifier,
					". Batch class is null or is deleted by some other user."));
			throw new UIException(BatchClassMessages.BATCH_CLASS_ALREADY_DELETED);
		} else {
			final String batchClassCurrentUser = batchClass.getCurrentUser();
			if (batchClassCurrentUser == null) {
				// commenting below 2 lines to fix current user lock issue raised in 3.1 rc1 installer
				// LOGGER.error(EphesoftStringUtil.concatenate("Batch class is already unlocked :", batchClassIdentifier));
				// throw new GWTException(BatchClassManagementMessages.BATCH_CLASS_UNLOCKED_BY_SUPER_USER);
			} else {
				final String currentUser = getUserName();
				LOGGER.debug(EphesoftStringUtil.concatenate("Batch class's current user is: ", batchClassCurrentUser,
						". User name from authentication is: ", currentUser));
				if (!batchClassCurrentUser.equalsIgnoreCase(currentUser) && !isSuperAdmin()) {
					LOGGER.error(EphesoftStringUtil.concatenate("Batch class is already unlocked :", batchClassIdentifier));

					throw new UIException(BatchClassMessages.BATCH_CLASS_UNLOCKED_BY_SUPER_USER);
				}
			}
		}
	}

	@Override
	public List<TableColumnInfoDTO> getAllTableColumnsFromPool() {
		TableColumnsInfoService tableColumnsInfoService = this.getSingleBeanOfType(TableColumnsInfoService.class);
		List<TableColumnsInfo> lisColumnsInfos = tableColumnsInfoService.getAllTableColumnsFromPool();
		List<TableColumnInfoDTO> lisColumnInfoDTOs = null;
		if (lisColumnsInfos != null) {
			lisColumnInfoDTOs = convertTableColumnInfoToTableColumnInfoDTO(lisColumnsInfos);
		}
		return lisColumnInfoDTOs;

	}

	@Override
	public List<RuleInfoDTO> getAllTableRules() {
		TableRuleInfoService tableRuleInfoService = this.getSingleBeanOfType(TableRuleInfoService.class);
		List<TableRuleInfo> lisRuleInfos = tableRuleInfoService.getAllTableRules();
		List<RuleInfoDTO> lisRuleInfoDTOs = null;
		if (lisRuleInfos != null) {
			lisRuleInfoDTOs = convertTableRuleInfoToTableRuleDTO(lisRuleInfos);
		}
		return lisRuleInfoDTOs;
	}

	/**
	 * Converts the list of TableColumnsInfo to list of TableColumnInfoDTO.
	 * 
	 * @param listColumnsInfos {@link List} <{@link TableColumnsInfo}> The List of tableColumnsInfo.
	 * @return {@link List} <{@link TableColumnInfoDTO}> The List of tableColumnInfoDTO.
	 */
	private List<TableColumnInfoDTO> convertTableColumnInfoToTableColumnInfoDTO(final List<TableColumnsInfo> listColumnsInfos) {
		List<TableColumnInfoDTO> listColumnInfoDTOs = null;
		if (listColumnsInfos != null) {
			listColumnInfoDTOs = new ArrayList<TableColumnInfoDTO>();
			for (TableColumnsInfo tableColumnsInfo : listColumnsInfos) {
				TableColumnInfoDTO tableColumnInfoDTO = BatchClassUtil.createTableColumnInfoDTO(null, tableColumnsInfo);
				listColumnInfoDTOs.add(tableColumnInfoDTO);
			}
		}
		return listColumnInfoDTOs;
	}

	private List<RuleInfoDTO> convertTableRuleInfoToTableRuleDTO(final List<TableRuleInfo> listRuleInfos) {
		List<RuleInfoDTO> listRuleInfoDTOs = null;
		if (listRuleInfos != null) {
			listRuleInfoDTOs = new ArrayList<RuleInfoDTO>();
			for (TableRuleInfo tableRuleInfo : listRuleInfos) {
				RuleInfoDTO ruleInfoDTO = BatchClassUtil.createRuleInfoDTO(null, tableRuleInfo);
				listRuleInfoDTOs.add(ruleInfoDTO);
			}
		}
		return listRuleInfoDTOs;
	}

	@Override
	public void releaseAllLocks() {
		final LockStatusService lockStatusService = this.getSingleBeanOfType(LockStatusService.class);
		if (this.getThreadLocalRequest().getUserPrincipal() != null) {
			lockStatusService.releaseAllLocks(this.getThreadLocalRequest().getUserPrincipal().getName());
		}
	}

	@Override
	public BatchClassDTO updateBatchClass(BatchClassDTO batchClassDTO) throws UIException {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		String batchClassIdentifier = batchClassDTO.getIdentifier();
		BatchClass batchClass = batchClassService.get(batchClassIdentifier);

		// Fix for JIRA ISSUE #10927 Call to check if batch class is already deleted or unlocked (by super admin)
		// while current user save its changes.
		checkIfBatchClassUnLockedBySuperUser(batchClassIdentifier, batchClass);
		BatchClassPluginService batchClassPluginService = this.getSingleBeanOfType(BatchClassPluginService.class);
		BatchClassPluginConfigService batchClassPluginConfigService = this.getSingleBeanOfType(BatchClassPluginConfigService.class);
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		PluginConfigService pluginConfigService = this.getSingleBeanOfType(PluginConfigService.class);
		MasterScannerService scannerMasterService = this.getSingleBeanOfType(MasterScannerService.class);

		boolean isBatchClassDirty = false;
		if (batchClassDTO.isDirty()) {
			isBatchClassDirty = true;
		}
		List<TableColumnInfoDTO> lisColumnInfoDTOs = batchClassDTO.getNonDeletedTableColumnsPoolDTOList();
		List<RuleInfoDTO> lisRuleInfoDTOs = batchClassDTO.getNonDeletedRuleInfoDTOList();
		if (!isBatchClassDirty) {
			Collection<BatchClassModuleDTO> batchClassModules = batchClassDTO.getModules();
			for (BatchClassModuleDTO batchClassModule : batchClassModules) {
				Collection<BatchClassPluginDTO> batchClassPluginDTOs = batchClassModule.getBatchClassPlugins();
				for (BatchClassPluginDTO batchClassPlugin : batchClassPluginDTOs) {
					Collection<BatchClassPluginConfigDTO> batchClassPluginConfigDTOs = batchClassPlugin.getBatchClassPluginConfigs();
					for (BatchClassPluginConfigDTO batchClassPluginConfig : batchClassPluginConfigDTOs) {
						if (batchClassPluginConfig.getPluginConfig() != null && batchClassPluginConfig.getPluginConfig().isDirty()) {
							isBatchClassDirty = true;
							break;
						}
					}
				}
			}
		}

		TableColumnsInfoService tableColumnsInfoService = this.getSingleBeanOfType(TableColumnsInfoService.class);
		TableRuleInfoService tableRuleInfoServie = this.getSingleBeanOfType(TableRuleInfoService.class);
		if (batchClassDTO.getListLockStatusDTOs() != null) {
			List<TableColumnsInfo> listTableColumnsInfos = null;
			List<TableColumnsInfo> listTableColumnsToBeDeleted = null;

			List<TableRuleInfo> listTableRuleInfos = null;
			List<TableRuleInfo> listTableRuleToBeDeleted = null;
			if (batchClassDTO.getTablePoolColumnsInfoDTOList() != null) {
				listTableColumnsInfos = new ArrayList<TableColumnsInfo>();
				listTableColumnsToBeDeleted = new ArrayList<TableColumnsInfo>();
				for (TableColumnInfoDTO tableColumnInfoDTO : batchClassDTO.getTablePoolColumnsInfoDTOList()) {
					TableColumnsInfo columnInfo = BatchClassUtil.mapTableColumnFromDTO(null, tableColumnInfoDTO);
					if (tableColumnInfoDTO.getIdentifier() != null && !tableColumnInfoDTO.isNew()) {
						columnInfo.setId(Long.parseLong(tableColumnInfoDTO.getIdentifier()));
					}
					if (tableColumnInfoDTO.isDeleted()) {
						if (!tableColumnInfoDTO.isNew()) {
							listTableColumnsToBeDeleted.add(columnInfo);
						}
					} else {
						listTableColumnsInfos.add(columnInfo);
					}
				}
				if (listTableColumnsInfos != null) {
					tableColumnsInfoService.updateTableColumnsInfo(listTableColumnsInfos, listTableColumnsToBeDeleted);
				}
			}
			if (batchClassDTO.getRuleInfoDTOList() != null) {
				listTableRuleInfos = new ArrayList<TableRuleInfo>();
				listTableRuleToBeDeleted = new ArrayList<TableRuleInfo>();
				for (RuleInfoDTO ruleInfoDTO : batchClassDTO.getRuleInfoDTOList()) {
					TableRuleInfo ruleInfo = BatchClassUtil.mapRuleInputFromDTO(null, ruleInfoDTO);
					if (ruleInfoDTO.getIdentifier() != null && !ruleInfoDTO.isNew()) {
						ruleInfo.setId(Long.parseLong(ruleInfoDTO.getIdentifier()));
					}
					if (ruleInfoDTO.isDeleted()) {
						if (!ruleInfoDTO.isNew()) {
							listTableRuleToBeDeleted.add(ruleInfo);
						}
					} else {
						listTableRuleInfos.add(ruleInfo);
					}
				}
				if (listTableRuleInfos != null) {
					tableRuleInfoServie.updateTableRulesInfo(listTableRuleInfos, listTableRuleToBeDeleted);
				}

			}
		}
		lisColumnInfoDTOs = getAllTableColumnsFromPool();
		lisRuleInfoDTOs = getAllTableRules();

		if (batchClassDTO.getListLockStatusDTOs() != null) {
			releaseAllLocks();
			batchClassDTO.setListLockStatusDTOs(null);
		}

		Map<String, RegexGroupDTO> regexGroupDTOMap = batchClassDTO.getRegexGroupMap();
		if (batchClassDTO.isRegexPoolDirty()) {
			boolean successfulUpdate = false;
			try {
				successfulUpdate = RegexUtil.updateRegexPool(batchClassDTO.getRegexGroups(true));
			} catch (DataAccessException dataAccessException) {
				LOGGER.error("Regex Pool could not be updated successfully.", dataAccessException);
				throw new UIException("Regex Pool could not be updated successfully.");
			}
			if (successfulUpdate) {
				batchClassDTO.setRegexPoolDirty(false);
				regexGroupDTOMap = getRegexGroupMap();
				batchClassDTO.setRegexGroupMap(regexGroupDTOMap);
			} else {
				LOGGER.error("Regex Pool could not be updated successfully.");
			}
		}

		if (!isBatchClassDirty) {
			return batchClassDTO;
		}

		Set<String> groupNameSet = getAllGroups();
		Set<String> superAdminGroupsSet = getAllSuperAdminGroup();
		List<ScannerMasterConfiguration> scannerMasterConfigs = scannerMasterService.getMasterConfigurations();
		List<String> docTypeNameList = BatchClassUtil
				.mergeBatchClassFromDTO(batchClass, batchClassDTO, groupNameSet, batchClassPluginConfigService,
						batchClassPluginService, pluginConfigService, scannerMasterConfigs, superAdminGroupsSet);

		if (null != docTypeNameList) {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			try {
				batchSchemaService.deleteDocTypeFolder(docTypeNameList, batchClassIdentifier);
			} catch (DCMAApplicationException e) {
			}
		}
		batchClass = batchClassService.merge(batchClass, batchClass.isDeleted());

		// Serialize Batch Class
		PluginPropertiesService batchClassPluginPropertiesService = this.getBeanByName("batchClassPluginPropertiesService",
				PluginPropertiesService.class);
		batchClassPluginPropertiesService.createBatchPropertiesFile(batchClassIdentifier);
		// End serialize Batch Class

		MasterScannerService masterScannerService = this.getSingleBeanOfType(MasterScannerService.class);
		List<ScannerMasterConfiguration> masterScannerConfig = masterScannerService.getMasterConfigurations();
		batchClassDTO = BatchClassUtil.createBatchClassDTO(masterScannerConfig, batchClass, pluginService);
		batchClassDTO.setDeployed(isBatchClassDeployed(batchClassDTO));
		batchClassDTO.setDirty(false);
		batchClassDTO.setTablePoolColumnsInfoDTOList(lisColumnInfoDTOs);
		batchClassDTO.setRegexGroupMap(regexGroupDTOMap);
		batchClassDTO.setRuleInfoDTOList(lisRuleInfoDTOs);
		return batchClassDTO;
	}

	@Override
	public Boolean verifyEmailConfig(final EmailConfigurationDTO emailConfigDTO) {
		Boolean isMailConfigCorrect = false;
		LOGGER.debug("Entering the Email config verification method");
		try {
			String password = emailConfigDTO.getPassword();
			String mailPassword = EphesoftEncryptionUtil.getDecryptedPasswordString(password);
			// Create a store object with user credentials and server properties
			final Store mailStore = MailUtil.getMailStore(emailConfigDTO.getServerType(), emailConfigDTO.getServerName(),
					emailConfigDTO.getUserName(), mailPassword, emailConfigDTO.getPortNumber(), emailConfigDTO.getIsSSL());
			// Try to connect to the given folder in the store. If the return object is null, then error.
			if (null != mailStore && null != MailUtil.getFolderFromMailStore(mailStore, emailConfigDTO.getFolderName())) {
				isMailConfigCorrect = true;
			}
		} catch (MessagingException e) {
			LOGGER.error(EphesoftStringUtil.concatenate("Email configuration is incorrect.", e.getMessage()), e);
		}
		LOGGER.debug(EphesoftStringUtil.concatenate("Is Email config correct : \t", isMailConfigCorrect));
		return isMailConfigCorrect;
	}

	@Override
	public Map<String, List<String>> getScannerMasterConfigs() {
		Map<String, List<String>> scannerMasterMap = new HashMap<String, List<String>>();
		final MasterScannerService masterScannerService = this.getSingleBeanOfType(MasterScannerService.class);
		List<ScannerMasterConfiguration> masterScannerConfigs = masterScannerService.getMasterConfigurations();
		for (ScannerMasterConfiguration masterScannerConfig : masterScannerConfigs) {

			scannerMasterMap.put(masterScannerConfig.getName(), masterScannerConfig.getSampleValue());
		}

		return scannerMasterMap;
	}

	@Override
	public String getEncryptedPasswordString(EmailConfigurationDTO emailConfigDTO) {
		String encryptedPassword = null;
		encryptedPassword = EphesoftEncryptionUtil.getEncryptedPasswordString(emailConfigDTO.getPassword());
		emailConfigDTO.setPassword(encryptedPassword);
		return encryptedPassword;
	}

	@Override
	public List<TestClassificationDataCarrierDTO> testContentClassification(String batchClassID, String classificationType)
			throws UIException {

		// get the properties for this class and according to da settings perform classification.
		boolean ppPluginFlag = true;
		boolean daPluginFlag = true;
		Batch batch = null;
		List<TestClassificationDataCarrierDTO> outputDTOs = new ArrayList<TestClassificationDataCarrierDTO>();
		Documents docs = new Documents();
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		StringBuilder folderPathBuilder = new StringBuilder(batchSchemaService.getBaseFolderLocation());
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(batchClassID);
		File batchClassFolder = new File(folderPathBuilder.toString());
		if (!batchClassFolder.exists()) {
			LOGGER.info("Batch Class Folder does not exist.");
			throw new UIException(BatchClassConstants.ERROR_BC_FOLDER_NOT_EXIST);
		}

		// The path for test classification folder is found.......
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(BatchClassConstants.TEST_CLASSIFICATION_FOLDER_NAME);
		String inputFilesPath = folderPathBuilder.toString();

		// Checking if test classification folder exists....
		File testClassificationFolder = new File(inputFilesPath);
		if (!testClassificationFolder.exists()) {
			testClassificationFolder.mkdir();
			LOGGER.error("Test classification folder does not exist.");
			throw new UIException(BatchClassConstants.ERROR_TEST_CLASSIFICATION_FOLDER_NOT_EXIST);
		}

		// checking if there are files present in folder.
		File[] listOfimages = testClassificationFolder.listFiles(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
				FileType.TIFF.getExtensionWithDot(), FileType.PDF.getExtensionWithDot()));
		if (null == listOfimages || listOfimages.length < 1) {
			LOGGER.error("There are no proper input files present. Please upload tif, tiff or pdf files.");
			throw new UIException(BatchClassConstants.ERROR_IMPROPER_INPUT_FILES);
		}
		// createBackupFolder(inputFilesPath);
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(BatchClassConstants.TEST_CLASSIFICATION_RESULT_FOLDER_NAME);
		File resultFolder = new File(folderPathBuilder.toString());
		if (resultFolder.exists()) {
			FileUtils.deleteDirectoryAndContents(folderPathBuilder.toString());
		}

		// break multi page pdf & tiff
		FolderImporter folderImporter = this.getSingleBeanOfType(FolderImporter.class);
		try {
			folderImporter.breakPDFandTifForExtraction(batchClassID, inputFilesPath);
		} catch (DCMAApplicationException e) {
			throw new UIException(BatchClassConstants.ERROR_WHILE_BREAKING_TIFF_OR_PDF);
			// throw new UIException("Error occur while breaking multipage tiff or pdf.");
		}

		PluginPropertiesService batchClassPPService = this.getBeanByName(BATCH_CLASS_PLUGIN_PROPERTIES_SERVICE,
				BatchClassPluginPropertiesService.class);
		ImageProcessService imService = this.getSingleBeanOfType(ImageProcessService.class);
		BarcodeService barcodeService = this.getSingleBeanOfType(BarcodeService.class);
		RegexService regexService = this.getSingleBeanOfType(RegexService.class);
		DocumentAssembler docAssembler = this.getBeanByName(DOC_ASSEMBLER, DocumentAssembler.class);
		SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);
		addTimestampToFileName(inputFilesPath);

		// perform OCR-ing for all images as it is required for extraction.

		if (classificationType == null) {
			classificationType = batchClassPPService.getPropertyValue(batchClassID,
					DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN, DocumentAssemblerProperties.DA_FACTORY_CLASS);
		}
		List<Page> listOfPages = null;
		if (AdminConstants.SEARCH_CLASSIFICATION.equalsIgnoreCase(classificationType)) {

			CustomFileFilter filter = null;
			try {
				filter = ocrInputFiles(batchClassID, inputFilesPath, searchClassificationService, false);
			} catch (DCMAException e) {
				e.printStackTrace();
				throw new UIException(BatchClassConstants.ERROR_WHILE_OCRING);
				// throw new UIException("Error occur whilw ocring.");
			}
			// search classification
			try {
				listOfPages = searchClassificationAPI(batchClassID, inputFilesPath, batchClassPPService, listOfPages,
						searchClassificationService, filter);
			} catch (Exception exception) {
				LOGGER.error("Error response at server:" + exception.getMessage());
				exception.printStackTrace();
				throw new UIException(exception.getMessage());
			}
			try {
				if (ppPluginFlag) {
					try {
						batch = createBatch(batchClass, classificationType, listOfPages, null);
						// batch = scriptSvc.executeScript(batch, null, AdminSharedConstants.PAGE_PROCESSING_SCRIPTING_PLUGIN_NAME,
						// null,
						// null);
						if (null != batch.getDocuments() && !CollectionUtil.isEmpty(batch.getDocuments().getDocument())) {
							List<Document> documentsList = batch.getDocuments().getDocument();
							if (null != documentsList.get(0).getPages()
									&& !CollectionUtil.isEmpty(documentsList.get(0).getPages().getPage())) {
								listOfPages = documentsList.get(0).getPages().getPage();
							}
						}
					} catch (Exception exception) {
						// throw new DCMAException("The result of execution for Page Processing ScriptsService was unsuccessful.",
						// exception);
						LOGGER.error("Error response at server:" + exception.getMessage());
						throw new UIException(BatchClassConstants.PP_SERVICE_UNSUCCESSFUL);
					}
				}
				docs = performDocCreation(batchClassID, docAssembler, docs, listOfPages, AdminConstants.SEARCH_CLASSIFICATION);
				if (daPluginFlag) {
					try {
						batch.setDocuments(null);
						batch = createBatch(batchClass, classificationType, null, docs);
						// batch = scriptSvc.executeScript(batch, null, AdminSharedConstants.DOCUMENT_ASSEMBLER_SCRIPTING_PLUGIN_NAME,
						// null, null);
						docs = new Documents();
						if (null != batch.getDocuments() && !CollectionUtil.isEmpty(batch.getDocuments().getDocument())) {
							docs.getDocument().addAll(batch.getDocuments().getDocument());
						}
					} catch (Exception exception) {
						// exception.printStackTrace();
						LOGGER.error("Error response at server:" + exception.getMessage());
						// throw new DCMAException("The result of execution for Document Assembler ScriptsService was unsuccessful.",
						// exception);
						throw new UIException(BatchClassConstants.DA_SERVICE_UNSUCCESSFULL);
					}
				}
			} catch (DCMAApplicationException dcmaAppException) {
				LOGGER.error("Error response at server:" + dcmaAppException.getMessage());
				LOGGER.error(dcmaAppException.getMessage(), dcmaAppException);
				throw new UIException(dcmaAppException.getMessage());
			}
			if (null != docs) {
				writeToXML(docs, inputFilesPath, batchClass, AdminConstants.SEARCH_CLASSIFICATION, "test-classification-result");
				outputDTOs = populateOutputDTOsForResult(docs, SEARCH_ENGINE_CLASSIFICATION, SEARCH_CLASSIFICATION_TAG);
			}
		} else if (AdminConstants.BARCODE_CLASSIFICATION.equalsIgnoreCase(classificationType)) {
			// barcode classification

			try {
				ocrInputFiles(batchClassID, inputFilesPath, searchClassificationService, false);
				listOfPages = barcodeClassificationAPI(batchClassID, inputFilesPath, batchClassPPService, barcodeService, listOfPages,
						regexService);
			} catch (Exception exception) {
				LOGGER.error("Error response at server:" + exception.getMessage());
				exception.printStackTrace();
				throw new UIException(exception.getMessage());
			}
			try {
				if (ppPluginFlag) {
					try {
						batch = createBatch(batchClass, classificationType, listOfPages, null);
						// batch = scriptSvc.executeScript(batch, null, AdminSharedConstants.PAGE_PROCESSING_SCRIPTING_PLUGIN_NAME,
						// null,
						// null);
						if (null != batch.getDocuments() && !CollectionUtil.isEmpty(batch.getDocuments().getDocument())) {
							List<Document> documentsList = batch.getDocuments().getDocument();
							if (null != documentsList.get(0).getPages()
									&& !CollectionUtil.isEmpty(documentsList.get(0).getPages().getPage())) {
								listOfPages = batch.getDocuments().getDocument().get(0).getPages().getPage();
							}
						}

					} catch (Exception exception) {
						LOGGER.error("Error response at server:" + exception.getMessage());
						throw new UIException(BatchClassConstants.PP_SERVICE_UNSUCCESSFUL);
						// throw new DCMAException("The result of execution for Page Processing ScriptsService was unsuccessful.",
						// exception);
					}

				}
				docs = performDocCreation(batchClassID, docAssembler, docs, listOfPages, AdminConstants.BARCODE_CLASSIFICATION);
				if (daPluginFlag) {
					try {
						batch.setDocuments(null);
						batch = createBatch(batchClass, classificationType, null, docs);
						// batch = scriptSvc.executeScript(batch, null, AdminSharedConstants.DOCUMENT_ASSEMBLER_SCRIPTING_PLUGIN_NAME,
						// null, null);
						docs = new Documents();
						if (null != batch.getDocuments() && !CollectionUtil.isEmpty(batch.getDocuments().getDocument())) {
							docs.getDocument().addAll(batch.getDocuments().getDocument());
						}
					} catch (Exception exception) {
						LOGGER.error("Error response at server:" + exception.getMessage());
						throw new UIException(BatchClassConstants.DA_SERVICE_UNSUCCESSFULL);
						// throw new DCMAException("The result of execution for Document Assembler ScriptsService was unsuccessful.",
						// exception);
					}
				}
			} catch (DCMAApplicationException dcmaAppException) {
				LOGGER.error("Error response at server:" + dcmaAppException.getMessage());
				throw new UIException(dcmaAppException.getMessage());
			}
			if (null != docs) {
				writeToXML(docs, inputFilesPath, batchClass, AdminConstants.BARCODE_CLASSIFICATION, "test-classification-result");
				outputDTOs = populateOutputDTOsForResult(docs, BARCODE_1, BARCODE_CLASSIFICATION_TAG);
			}

		} else if (AdminConstants.IMAGE_CLASSIFICATION.equalsIgnoreCase(classificationType)) {
			// Image Classification
			try {
				listOfPages = imageClassificationAPI(batchClassID, inputFilesPath, testClassificationFolder, batchClassPPService,
						imService, listOfPages);
			} catch (Exception exception) {
				LOGGER.error("Error response at server:" + exception.getMessage());
				exception.printStackTrace();
				throw new UIException(exception.getMessage());
			}
			try {
				if (ppPluginFlag) {
					try {
						batch = createBatch(batchClass, classificationType, listOfPages, null);
						// batch = scriptSvc.executeScript(batch, null, AdminSharedConstants.PAGE_PROCESSING_SCRIPTING_PLUGIN_NAME,
						// null,
						// null);
						if (null != batch.getDocuments() && !CollectionUtil.isEmpty(batch.getDocuments().getDocument())) {
							List<Document> documentsList = batch.getDocuments().getDocument();
							if (null != documentsList.get(0).getPages()
									&& !CollectionUtil.isEmpty(documentsList.get(0).getPages().getPage())) {
								listOfPages = batch.getDocuments().getDocument().get(0).getPages().getPage();
							}
						}
					} catch (Exception exception) {
						// throw new DCMAException("The result of execution for Page Processing ScriptsService was unsuccessful.",
						// exception);
						LOGGER.error("Error response at server:" + exception.getMessage());
						throw new UIException(BatchClassConstants.PP_SERVICE_UNSUCCESSFUL);
					}
				}
				docs = performDocCreation(batchClassID, docAssembler, docs, listOfPages, AdminConstants.IMAGE_CLASSIFICATION);
				if (daPluginFlag) {
					try {
						batch.setDocuments(null);
						batch = createBatch(batchClass, classificationType, null, docs);
						// batch = scriptSvc.executeScript(batch, null, AdminSharedConstants.DOCUMENT_ASSEMBLER_SCRIPTING_PLUGIN_NAME,
						// null, null);
						docs = new Documents();
						if (null != batch.getDocuments() && !CollectionUtil.isEmpty(batch.getDocuments().getDocument())) {
							docs.getDocument().addAll(batch.getDocuments().getDocument());
						}
					} catch (Exception exception) {
						// throw new DCMAException("The result of execution for Document Assembler ScriptsService was unsuccessful.",
						// exception);
						LOGGER.error("Error response at server:" + exception.getMessage());
						throw new UIException(BatchClassConstants.DA_SERVICE_UNSUCCESSFULL);
					}
				}
			} catch (DCMAApplicationException dcmaAppException) {
				LOGGER.error("Error response at server:" + dcmaAppException.getMessage());
				throw new UIException(dcmaAppException.getMessage());
			}
			if (null != docs) {
				writeToXML(docs, inputFilesPath, batchClass, AdminConstants.IMAGE_CLASSIFICATION, "test-classification-result");
				outputDTOs = populateOutputDTOsForResult(docs, IMAGE_COMPARE_CLASSIFICATION, IMAGE_CLASSIFICATION_TAG);
			}
		} else {
			// Automatic classification
			CustomFileFilter filter = null;
			try {
				filter = ocrInputFiles(batchClassID, inputFilesPath, searchClassificationService, false);
			} catch (DCMAException e) {
				e.printStackTrace();
				throw new UIException(BatchClassConstants.ERROR_WHILE_OCRING);
				// throw new UIException("Error occur whilw ocring.");
			}
			try {
				listOfPages = searchClassificationAPI(batchClassID, inputFilesPath, batchClassPPService, listOfPages,
						searchClassificationService, filter);
			} catch (Exception exception) {
				LOGGER.error("Unable to execute search classification for Automatic classification. Error response at server:"
						+ exception.getMessage());
				exception.printStackTrace();
			}
			try {

				listOfPages = barcodeClassificationAPI(batchClassID, inputFilesPath, batchClassPPService, barcodeService, listOfPages,
						regexService);
			} catch (Exception exception) {
				LOGGER.error("Unable to execute barcode classification for Automatic classification. Error response at server:"
						+ exception.getMessage());
				exception.printStackTrace();
			}
			try {
				listOfPages = imageClassificationAPI(batchClassID, inputFilesPath, testClassificationFolder, batchClassPPService,
						imService, listOfPages);

			} catch (Exception exception) {
				LOGGER.error("Unable to execute barcode classification for Automatic classification. Error response at server:"
						+ exception.getMessage());
				exception.printStackTrace();
			}
			if (null == listOfPages || listOfPages.isEmpty()) {
				LOGGER.error("Unable to perform Automatic Classification. Please verify plugins in batch class for Search, Barcode and Image Classification.");
				throw new UIException(
						"Unable to perform Automatic Classification. Please verify plugins in batch class for Search, Barcode and Image Classification.");
			} else {
				try {
					if (ppPluginFlag) {
						try {
							batch = createBatch(batchClass, classificationType, listOfPages, null);
							// batch = scriptSvc.executeScript(batch, null, AdminSharedConstants.PAGE_PROCESSING_SCRIPTING_PLUGIN_NAME,
							// null, null);
							if (null != batch.getDocuments() && !CollectionUtil.isEmpty(batch.getDocuments().getDocument())) {
								List<Document> documentsList = batch.getDocuments().getDocument();
								if (null != documentsList.get(0).getPages()
										&& !CollectionUtil.isEmpty(documentsList.get(0).getPages().getPage())) {
									listOfPages = batch.getDocuments().getDocument().get(0).getPages().getPage();
								}
							}
						} catch (Exception exception) {
							LOGGER.error("Error response at server:" + exception.getMessage());
							throw new UIException(BatchClassConstants.PP_SERVICE_UNSUCCESSFUL);
						}
					}
					docs = performDocCreation(batchClassID, docAssembler, docs, listOfPages, AdminConstants.AUTOMATIC_CLASSIFICATION);
					if (daPluginFlag) {
						try {
							batch.setDocuments(null);
							batch = createBatch(batchClass, classificationType, null, docs);
							// batch = scriptSvc.executeScript(batch, null,
							// AdminSharedConstants.DOCUMENT_ASSEMBLER_SCRIPTING_PLUGIN_NAME, null, null);
							docs = new Documents();
							if (null != batch.getDocuments() && !CollectionUtil.isEmpty(batch.getDocuments().getDocument())) {
								docs.getDocument().addAll(batch.getDocuments().getDocument());
							}
						} catch (Exception exception) {
							LOGGER.error("Error response at server:" + exception.getMessage());
							throw new UIException(BatchClassConstants.DA_SERVICE_UNSUCCESSFULL);
						}
					}
				} catch (DCMAApplicationException dcmaAppException) {
					LOGGER.error("Unable to execute Automatic classification. Error response at server:"
							+ dcmaAppException.getMessage());
					throw new UIException(dcmaAppException.getMessage());
				}
			}
			if (null != docs) {
				writeToXML(docs, inputFilesPath, batchClass, AdminConstants.AUTOMATIC_CLASSIFICATION, "test-classification-result");
				outputDTOs = populateOutputDTOsForResult(docs, AUTOMATIC_CLASSIFICATION, AUTOMATIC_CLASSIFICATION_TAG);
			}
		}
		return outputDTOs;
	}

	/**
	 * To populate the DTO's for the result documents created.
	 * 
	 * @param docs {@link Documents} the documents created after the processing.
	 * @param classificationType {@link String} the type of classification to be done.
	 * @param classificationName {@link String} the classification name to be shown on UI.
	 * @return {@link List <TestClassificationDataCarrierDTO>}
	 */
	private List<TestClassificationDataCarrierDTO> populateOutputDTOsForResult(Documents docs, String classificationType,
			String classificationName) {
		List<TestClassificationDataCarrierDTO> outputDTOs = new ArrayList<TestClassificationDataCarrierDTO>();
		List<Document> documentList = docs.getDocument();
		if (null != documentList && !documentList.isEmpty()) {
			for (Document documentElement : documentList) {
				Pages pages = documentElement.getPages();
				if (null != pages) {
					List<Page> pageList = pages.getPage();
					if (null != pageList && !pageList.isEmpty()) {
						for (Page pageElement : pageList) {
							PageLevelFields pageLevelFields = pageElement.getPageLevelFields();
							if (null != pageLevelFields) {
								List<DocField> pageLevelFieldList = pageLevelFields.getPageLevelField();
								if (null != pageLevelFieldList && !pageLevelFieldList.isEmpty()) {
									DocField pageLevelField = null;
									if (classificationType.equalsIgnoreCase(AUTOMATIC_CLASSIFICATION)) {
										pageLevelField = getDocFieldForAutomaticClassification(pageLevelFieldList);
									} else {
										pageLevelField = getDocFieldForGivenClassification(pageLevelFieldList, classificationType);
									}
									if (null != pageLevelField && null != pageLevelField.getValue()) {

										// Create a new DTO and populate it with all the data.
										TestClassificationDataCarrierDTO classificationDTO = new TestClassificationDataCarrierDTO();
										classificationDTO.setClassificationType(classificationName);
										classificationDTO.setPageConfidence(pageLevelField.getConfidence());
										classificationDTO.setDocIdentifier(documentElement.getIdentifier());
										classificationDTO.setDocType(documentElement.getType());
										classificationDTO.setPageClassificationValue(pageLevelField.getValue());
										classificationDTO.setPageID(pageElement.getIdentifier());
										classificationDTO.setPageName(pageElement.getOldFileName());
										classificationDTO.setDocumentConfidence(documentElement.getConfidence());
										if (EphesoftStringUtil.isNullOrEmpty(pageLevelField.getLearnedFileName())) {
											classificationDTO.setLearnedFileName("NA");
										} else {
											classificationDTO.setLearnedFileName(pageLevelField.getLearnedFileName());
										}
										if (!BatchClassConstants.UNKNOWN.equalsIgnoreCase(documentElement.getType())
												&& documentElement.getConfidence() > documentElement.getConfidenceThreshold()) {
											classificationDTO.setValidated(true);
										} else {
											classificationDTO.setValidated(false);
										}
										outputDTOs.add(classificationDTO);
									}
								}
							}
						}
					}
				}
			}
		}
		return outputDTOs;
	}

	/**
	 * Getting the maximum doc level field for automatic classification.
	 * 
	 * @param pageLevelFieldList {@link List <DocField>} the field list for a page.
	 * @return {@link DocField} the maximum doc field for the a page.
	 */
	private DocField getDocFieldForAutomaticClassification(List<DocField> pageLevelFieldList) {
		DocField maxDocField = null;
		float maxConfidence = -1l;
		for (DocField pageLevelField : pageLevelFieldList) {
			if (pageLevelField != null) {
				float confidence = pageLevelField.getConfidence();
				if (confidence > maxConfidence) {
					maxConfidence = confidence;
					maxDocField = pageLevelField;
				}
			}
		}
		return maxDocField;
	}

	/**
	 * Getting the doc level fields for the provided classification name.
	 * 
	 * @param pageLevelFieldList {@link List <DocField>} the field list for a page.
	 * @param classificationType {@link String} the classification name.
	 * @return {@link DocField} the doc field for the provided classification.
	 */
	private DocField getDocFieldForGivenClassification(List<DocField> pageLevelFieldList, String classificationType) {
		DocField maxDocField = null;
		for (DocField pageLevelField : pageLevelFieldList) {
			if (pageLevelField != null) {
				if (classificationType.equalsIgnoreCase(pageLevelField.getName())) {
					maxDocField = pageLevelField;
					break;
				}
			}
		}
		return maxDocField;
	}

	@Override
	public Batch testExtraction(String batchClassID, String classificationType, List<String> extractionPluginNames) throws Exception {
		// get the properties for this class and according to da settings perform classification.

		Batch batch = new Batch();
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		PluginPropertiesService batchClassPPService = this.getBeanByName(BATCH_CLASS_PLUGIN_PROPERTIES_SERVICE,
				BatchClassPluginPropertiesService.class);
		ImageProcessService imService = this.getSingleBeanOfType(ImageProcessService.class);
		BarcodeService barcodeService = this.getSingleBeanOfType(BarcodeService.class);
		RegexService regexService = this.getSingleBeanOfType(RegexService.class);
		DocumentAssembler docAssembler = this.getBeanByName("docAssembler", DocumentAssembler.class);
		SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);

		String inputFilesPath = checkAndCreateRequiredFolders(batchClassID, batchSchemaService);

		FolderImporter folderImporter = this.getSingleBeanOfType(FolderImporter.class);
		folderImporter.breakPDFandTifForExtraction(batchClassID, inputFilesPath);

		// breakMultiPagePDFandTiff(batchClassID, inputFilesPath);
		if (classificationType == null) {
			classificationType = AdminConstants.SEARCH_CLASSIFICATION;
		}
		addTimestampToFileName(inputFilesPath);
		// perform OCR-ing for all images as it is required for extraction.
		CustomFileFilter filter = ocrInputFiles(batchClassID, inputFilesPath, searchClassificationService, false);

		if (AdminConstants.SEARCH_CLASSIFICATION.equalsIgnoreCase(classificationType)) {
			batch = searchClassificationExecutionFlow(batchClass, batchClassPPService, docAssembler, searchClassificationService,
					inputFilesPath, filter);
		} else if (AdminConstants.BARCODE_CLASSIFICATION.equalsIgnoreCase(classificationType)) {
			batch = barcodeClassificationExecutionFlow(batchClass, batchClassPPService, barcodeService, regexService, docAssembler,
					inputFilesPath);
		} else if (AdminConstants.IMAGE_CLASSIFICATION.equalsIgnoreCase(classificationType)) {
			batch = imageClassificationExecutionFlow(batchClass, batchClassPPService, imService, docAssembler, inputFilesPath);
		}
		performExtractionOnImages(batch, extractionPluginNames, batchSchemaService, inputFilesPath);
		return writeBatchToXML(inputFilesPath, batch, "test-extraction-result");
		// performExtractionOnImages(batch, extractionPluginNames, batchSchemaService, inputFilesPath);
		// try {
		// batch = scriptSvc.executeScript(batch, null, AdminSharedConstants.EXTRACTION_SCRIPTING_PLUGIN_NAME, null, null);
		// } catch (Exception exception) {
		// throw new DCMAException("The result of execution for Extraction ScriptsService was unsuccessful.", exception);
		// }
		// return writeBatchToXML(inputFilesPath, batch, "test-extraction-result");
	}

	private void addTimestampToFileName(String inputFilesPath) {
		File[] listOfimages = new File(inputFilesPath).listFiles(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
				FileType.TIFF.getExtensionWithDot()));
		if (null != listOfimages && listOfimages.length > 0) {
			for (File imageFile : listOfimages) {
				String fileName = imageFile.getAbsolutePath();
				if (null != fileName) {
					int index = fileName.indexOf("-0");
					if (index == -1 && (fileName.lastIndexOf('.') - fileName.lastIndexOf('-') != 14)) {
						String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "-" + System.currentTimeMillis()
								+ FileType.TIF.getExtensionWithDot();
						File renameFile = new File(fileName);
						renameFile.renameTo(new File(newFileName));
					} else if (index < fileName.length() && !fileName.substring(index + 1, fileName.length()).contains("-")) {
						String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + "-" + System.currentTimeMillis()
								+ FileType.TIF.getExtensionWithDot();
						File renameFile = new File(fileName);
						renameFile.renameTo(new File(newFileName));
					}
				}
			}
		}

	}

	@Override
	public List<String> getBatchClassExtractionPlugins(String batchClassID) throws UIException {
		List<String> batchClassPlugins = new ArrayList<String>();
		List<BatchClassPlugin> batchClassExtractionPlugins = getBatchClassExtractionModules(batchClassID);
		if (!CollectionUtil.isEmpty(batchClassExtractionPlugins)) {
			for (BatchClassPlugin plugin : batchClassExtractionPlugins) {
				if (null != plugin && null != plugin.getPlugin()) {
					String pluginName = TestExtractionModule.getDisplayValue(plugin.getPlugin().getPluginName());
					if (!StringUtil.isNullOrEmpty(pluginName)) {
						batchClassPlugins.add(pluginName);
					}
				}
			}
		}
		return batchClassPlugins;
	}

	private enum TestExtractionModule {
		BARCODE_EXTRACTION("Barcode Extraction"), RECOSTAR_EXTRACTION("Recostar Extraction"), REGULAR_REGEX_EXTRACTION(
				"Regular Regex Extraction"), KEY_VALUE_EXTRACTION("Key Value Extraction"), FUZZYDB("Fuzzy DB"), TABLE_EXTRACTION(
				"Table Extraction"), EXTRACTION_SCRIPTING_PLUGIN("Extraction Scripting plugin"), NUANCE_EXTRACTION_PLUGIN(
				"Nuance Extraction");

		private String displayParameter;

		private TestExtractionModule(String displayParameter) {
			this.displayParameter = displayParameter;
		}

		public String getDisplayParameter() {
			return displayParameter;
		}

		public static String getDisplayValue(String moduleType) {
			String moduleName = null;
			for (TestExtractionModule module : TestExtractionModule.values()) {
				if (module.name().equals(moduleType)) {
					moduleName = module.getDisplayParameter();
					break;
				}
			}
			return moduleName;
		}
	}

	private List<BatchClassPlugin> getBatchClassExtractionModules(String batchClassID) throws UIException {
		List<BatchClassPlugin> batchClassPlugins = new ArrayList<BatchClassPlugin>();
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		if (null != batchClass) {
			if (null != batchClass.getBatchClassModuleByName("Extraction")) {
				batchClassPlugins = batchClass.getBatchClassModuleByName("Extraction").getBatchClassPluginInOrder();
			} else {
				LOGGER.info("Unable to get Batch Class Plugins as batch class modules were not retreived.");
				throw new UIException("Unable to get Batch Class Plugins.");
			}
		} else {
			LOGGER.info("Unable to get Batch Class Plugins as Batch class has not been retreived.");
			throw new UIException("Unable to get Batch Class Plugins.");
		}
		return batchClassPlugins;
	}

	/**
	 * @param batchClassID
	 * @param classificationType
	 * @param batch
	 * @param docs
	 * @param batchClass
	 * @param batchClassPPService
	 * @param imService
	 * @param docAssembler
	 * @param scriptSvc
	 * @param inputFilesPath
	 * @param listOfPages
	 * @return
	 * @throws Exception
	 * @throws DCMAException
	 */
	private Batch imageClassificationExecutionFlow(BatchClass batchClass, PluginPropertiesService batchClassPPService,
			ImageProcessService imService, DocumentAssembler docAssembler, String inputFilesPath) throws Exception, DCMAException {
		// Image Classification
		List<Page> listOfPages = null;
		Documents docs = new Documents();
		Batch batch = new Batch();
		String batchClassID = batchClass.getIdentifier();
		try {
			listOfPages = imageClassificationAPI(batchClassID, inputFilesPath, new File(inputFilesPath), batchClassPPService,
					imService, listOfPages);
		} catch (Exception exception) {
			LOGGER.error("Error response at server:" + exception.getMessage());
			exception.printStackTrace();
			throw new Exception(exception.getMessage());
		}
		// executePPScript(AdminConstants.IMAGE_CLASSIFICATION, batch, batchClass, listOfPages, scriptSvc);
		// executing document assembler for image classification.
		try {
			docs = performDocCreation(batchClassID, docAssembler, docs, listOfPages, AdminConstants.IMAGE_CLASSIFICATION);
		} catch (DCMAApplicationException dcmaAppException) {
			LOGGER.error("Error response at server:" + dcmaAppException.getMessage());
			throw new Exception(dcmaAppException.getMessage());
		}
		// executing document assembler script if any.
		// executeDAScript(AdminConstants.IMAGE_CLASSIFICATION, batch, batchClass, docs, scriptSvc);
		if (null != docs) {
			batch = writeToXML(docs, inputFilesPath, batchClass, AdminConstants.IMAGE_CLASSIFICATION, "test-extraction-result");
			// outputDTOs = populateOutputDTOsForResult(docs, IMAGE_COMPARE_CLASSIFICATION, IMAGE_CLASSIFICATION_TAG);
		}
		return batch;
	}

	/**
	 * @param batchClassID
	 * @param classificationType
	 * @param batch
	 * @param docs
	 * @param batchClass
	 * @param batchClassPPService
	 * @param barcodeService
	 * @param regexService
	 * @param docAssembler
	 * @param scriptSvc
	 * @param inputFilesPath
	 * @param listOfPages
	 * @return
	 * @throws Exception
	 * @throws DCMAException
	 */
	private Batch barcodeClassificationExecutionFlow(BatchClass batchClass, PluginPropertiesService batchClassPPService,
			BarcodeService barcodeService, RegexService regexService, DocumentAssembler docAssembler, String inputFilesPath)
			throws Exception, DCMAException {
		// barcode classification
		List<Page> listOfPages = null;
		Documents docs = new Documents();
		Batch batch = new Batch();
		String batchClassID = batchClass.getIdentifier();
		try {
			listOfPages = barcodeClassificationAPI(batchClassID, inputFilesPath, batchClassPPService, barcodeService, listOfPages,
					regexService);
		} catch (Exception exception) {
			LOGGER.error("Error response at server:" + exception.getMessage());
			exception.printStackTrace();
			throw new Exception(exception.getMessage());
		}
		// executing page process script if any.
		// executePPScript(AdminConstants.BARCODE_CLASSIFICATION, batch, batchClass, listOfPages, scriptSvc);
		// executing document assembler for barcode classification.
		try {
			docs = performDocCreation(batchClassID, docAssembler, docs, listOfPages, AdminConstants.BARCODE_CLASSIFICATION);
		} catch (DCMAApplicationException dcmaAppException) {
			LOGGER.error("Error response at server:" + dcmaAppException.getMessage());
			throw new Exception(dcmaAppException.getMessage());
		}
		// executing document assembler script if any.
		// executeDAScript(AdminConstants.BARCODE_CLASSIFICATION, batch, batchClass, docs, scriptSvc);
		if (null != docs) {
			batch = writeToXML(docs, inputFilesPath, batchClass, AdminConstants.BARCODE_CLASSIFICATION, "test-extraction-result");
			// outputDTOs = populateOutputDTOsForResult(docs, BARCODE_1, BARCODE_CLASSIFICATION_TAG);
		}
		return batch;
	}

	/**
	 * @param batchClassID
	 * @param classificationType
	 * @param batch
	 * @param docs
	 * @param batchClass
	 * @param batchClassPPService
	 * @param docAssembler
	 * @param scriptSvc
	 * @param searchClassificationService
	 * @param inputFilesPath
	 * @param filter
	 * @param listOfPages
	 * @return
	 * @throws Exception
	 * @throws DCMAException
	 */
	private Batch searchClassificationExecutionFlow(BatchClass batchClass, PluginPropertiesService batchClassPPService,
			DocumentAssembler docAssembler, SearchClassificationService searchClassificationService, String inputFilesPath,
			CustomFileFilter filter) throws Exception, DCMAException {
		List<Page> listOfPages = null;
		// search classification
		Documents docs = new Documents();
		Batch batch = new Batch();
		String batchClassID = batchClass.getIdentifier();
		try {
			listOfPages = searchClassificationAPI(batchClassID, inputFilesPath, batchClassPPService, listOfPages,
					searchClassificationService, filter);
		} catch (Exception exception) {
			LOGGER.error("Error response at server:" + exception.getMessage());
			exception.printStackTrace();
			throw new Exception(exception.getMessage());
		}
		// execute PP script if any.
		// executePPScript(AdminConstants.SEARCH_CLASSIFICATION, batch, batchClass, listOfPages, scriptSvc);
		// executing document assembler plugin for search classification.
		try {
			docs = performDocCreation(batchClassID, docAssembler, docs, listOfPages, AdminConstants.SEARCH_CLASSIFICATION);
		} catch (DCMAApplicationException dcmaAppException) {
			LOGGER.error("Error response at server:" + dcmaAppException.getMessage());
			throw new Exception(dcmaAppException.getMessage());
		}
		// executing document assembler script if any.
		// executeDAScript(AdminConstants.SEARCH_CLASSIFICATION, batch, batchClass, docs, scriptSvc);
		if (null != docs) {
			batch = writeToXML(docs, inputFilesPath, batchClass, AdminConstants.SEARCH_CLASSIFICATION, "test-extraction-result");
			// outputDTOs = populateOutputDTOsForResult(docs, SEARCH_ENGINE_CLASSIFICATION, SEARCH_CLASSIFICATION_TAG);
		}
		return batch;
	}

	/**
	 * 
	 * @param batchClass {@link BatchClass} batch class whose details is used to create batch.
	 * @param classificationTypeName classification type.
	 * @param pagesList list of pages to create batch.
	 * @param documents documents to create batch.
	 * @return {@link Batch} batch created using batch class,list of pages and documents.
	 */
	private Batch createBatch(BatchClass batchClass, String classificationTypeName, List<Page> pagesList, Documents documents) {
		Batch batch = new Batch();
		DocumentClassificationTypes classificationType = new DocumentClassificationTypes();
		classificationType.getDocumentClassificationType().add(classificationTypeName);
		batch.setBatchInstanceIdentifier(FileUtils.EMPTY_STRING);
		batch.setBatchName(FileUtils.EMPTY_STRING);
		batch.setBatchDescription(FileUtils.EMPTY_STRING);
		batch.setBatchPriority(FileUtils.EMPTY_STRING);
		batch.setBatchLocalPath(FileUtils.EMPTY_STRING);
		batch.setUNCFolderPath(batchClass.getUncFolder());
		batch.setBatchClassDescription(batchClass.getDescription());
		batch.setBatchClassIdentifier(batchClass.getIdentifier());
		batch.setBatchClassName(batchClass.getName());
		batch.setBatchClassVersion(batchClass.getVersion());

		com.ephesoft.dcma.batch.schema.Batch.Documents batchDocuments = new Batch.Documents();
		if (pagesList != null && !pagesList.isEmpty()) {
			Document doc = new Document();
			doc.setType(AdminConstants.DOCUMENT_TYPE_UNKNOWN);
			doc.setDescription(AdminConstants.DOCUMENT_TYPE_UNKNOWN);
			Document.Pages docPages = new Document.Pages();
			docPages.getPage().addAll(pagesList);
			doc.setPages(docPages);
			batchDocuments.getDocument().add(doc);
		}
		if (documents != null) {
			batchDocuments.getDocument().addAll(documents.getDocument());
		}
		batch.setDocumentClassificationTypes(classificationType);
		batch.setDocuments(batchDocuments);
		return batch;
	}

	private void performExtractionOnImages(Batch batch, List<String> extractionPluginNames, BatchSchemaService batchSchemaService,
			String folderLocation) throws DCMAException, DCMAApplicationException {
		if (null != batch && null != extractionPluginNames && !extractionPluginNames.isEmpty()) {
			BarcodeExtractionService barcodeExtractionService = this.getSingleBeanOfType(BarcodeExtractionService.class);
			ExtractionService regexExtractionService = this.getSingleBeanOfType(ExtractionService.class);
			KVExtractionService kvService = this.getSingleBeanOfType(KVExtractionService.class);
			TableExtractionService tableExtractionService = this.getSingleBeanOfType(TableExtractionService.class);

			for (String extractionPluginName : extractionPluginNames) {
				String batchClassIdentifier = batch.getBatchClassIdentifier();
				if ("Barcode Extraction".equalsIgnoreCase(extractionPluginName)) {
					barcodeExtractionService.extractPageBarCodeForExtraction(batchClassIdentifier, folderLocation, batch);
				} else if ("Key Value Extraction".equalsIgnoreCase(extractionPluginName)) {
					kvService.extractKVForExtraction(folderLocation, batchClassIdentifier, batch);
				} else if ("Table Extraction".equalsIgnoreCase(extractionPluginName)) {
					tableExtractionService.extractTableForExtraction(folderLocation, batchClassIdentifier, batch);
				} else if ("Regular Regex Extraction".equalsIgnoreCase(extractionPluginName)) {
					regexExtractionService.extractDocumentFieldsForExtraction(batchClassIdentifier, folderLocation, batch);
				}
			}
		}
	}

	/**
	 * Search classification API for performing the classification of lucene for input images..
	 * 
	 * @param batchClassID {@link String} the batch class identifier.
	 * @param inputFilesPath {@link String} the path for input files to be processed.
	 * @param batchClassPPService {@link PluginPropertiesService} the service to extract properties of plugin.
	 * @param listOfPages {@link List<Page>} the list of pages to be processed.
	 * @param searchClassificationService
	 * @param filter
	 * @return {@link List<Page>} the updated list of pages.
	 * @throws DCMAException
	 * @throws DCMAApplicationException
	 */
	private List<Page> searchClassificationAPI(String batchClassID, String inputFilesPath,
			PluginPropertiesService batchClassPPService, List<Page> listOfPages,
			SearchClassificationService searchClassificationService, CustomFileFilter filter) throws DCMAException,
			DCMAApplicationException {
		String respStr = FileUtils.EMPTY_STRING;
		BatchSchemaService bSService = this.getSingleBeanOfType(BatchSchemaService.class);
		Map<LuceneProperties, String> batchClassConfigMap = new HashMap<LuceneProperties, String>();
		BatchPlugin searchClassPlugin = batchClassPPService.getPluginProperties(batchClassID,
				ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN);
		BatchPlugin docAssemblyPlugin = batchClassPPService.getPluginProperties(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN);
		respStr = validateSearchPluginProperties(batchClassID, respStr, searchClassPlugin, docAssemblyPlugin);
		if (respStr.isEmpty()) {
			try {
				batchClassConfigMap.put(LuceneProperties.LUCENE_VALID_EXTNS, batchClassPPService.getPropertyValue(batchClassID,
						ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_VALID_EXTNS));
				batchClassConfigMap.put(LuceneProperties.LUCENE_INDEX_FIELDS, batchClassPPService.getPropertyValue(batchClassID,
						ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_INDEX_FIELDS));
				batchClassConfigMap.put(LuceneProperties.LUCENE_STOP_WORDS, batchClassPPService.getPropertyValue(batchClassID,
						ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_STOP_WORDS));
				batchClassConfigMap.put(LuceneProperties.LUCENE_MIN_TERM_FREQ, batchClassPPService.getPropertyValue(batchClassID,
						ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MIN_TERM_FREQ));
				batchClassConfigMap.put(LuceneProperties.LUCENE_MIN_DOC_FREQ, batchClassPPService.getPropertyValue(batchClassID,
						ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MIN_DOC_FREQ));
				batchClassConfigMap.put(LuceneProperties.LUCENE_MIN_WORD_LENGTH, batchClassPPService.getPropertyValue(batchClassID,
						ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MIN_WORD_LENGTH));
				batchClassConfigMap.put(LuceneProperties.LUCENE_MAX_QUERY_TERMS, batchClassPPService.getPropertyValue(batchClassID,
						ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MAX_QUERY_TERMS));
				batchClassConfigMap.put(LuceneProperties.LUCENE_TOP_LEVEL_FIELD, batchClassPPService.getPropertyValue(batchClassID,
						ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_TOP_LEVEL_FIELD));
				batchClassConfigMap.put(LuceneProperties.LUCENE_NO_OF_PAGES, batchClassPPService.getPropertyValue(batchClassID,
						ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_NO_OF_PAGES));
				batchClassConfigMap.put(LuceneProperties.LUCENE_MAX_RESULT_COUNT, batchClassPPService.getPropertyValue(batchClassID,
						ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_MAX_RESULT_COUNT));
				batchClassConfigMap.put(LuceneProperties.LUCENE_FIRST_PAGE_CONF_WEIGHTAGE, batchClassPPService
						.getPropertyValue(batchClassID, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN,
								LuceneProperties.LUCENE_FIRST_PAGE_CONF_WEIGHTAGE));
				batchClassConfigMap.put(LuceneProperties.LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE, batchClassPPService.getPropertyValue(
						batchClassID, ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN,
						LuceneProperties.LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE));
				batchClassConfigMap
						.put(LuceneProperties.LUCENE_LAST_PAGE_CONF_WEIGHTAGE, batchClassPPService.getPropertyValue(batchClassID,
								ICommonConstants.SEARCH_CLASSIFICATION_PLUGIN, LuceneProperties.LUCENE_LAST_PAGE_CONF_WEIGHTAGE));
			} catch (NullPointerException nullPointerException) {
				LOGGER.error("Problem in fetching search classification properties : " + nullPointerException);
				throw new DCMAApplicationException("Problem in fetching search classification properties.");
			}
			String pageName;
			String thumbFileName;
			String xmlFileList[] = new File(inputFilesPath).list(filter);
			if (xmlFileList != null && xmlFileList.length > 0) {
				ObjectFactory objectFactory = new ObjectFactory();
				List<Document> xmlDocuments = new ArrayList<Document>();
				HocrPages hocrPages = new HocrPages();
				Pages pages = new Pages();
				listOfPages = pages.getPage();
				Document doc = objectFactory.createDocument();
				xmlDocuments.add(doc);
				doc.setPages(pages);

				for (int index = 0; index < xmlFileList.length; index++) {
					// generate hocr file from html file.
					String xmlFile = xmlFileList[index];
					String hocrFilePath = inputFilesPath + File.separator + xmlFile;
					HocrPages hocrpages = bSService.getHOCR(hocrFilePath);
					HocrPage hocrPage = hocrpages.getHocrPage().get(0);
					List<HocrPage> hocrPageList = hocrPages.getHocrPage();
					String pageID = "PG" + index;
					hocrPage.setPageID(pageID);
					hocrPageList.add(hocrPage);

					Page pageType = objectFactory.createPage();
					pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + index);
					pageType.setHocrFileName(xmlFile);
					pageName = EphesoftStringUtil.concatenate(xmlFile.substring(0, xmlFile.indexOf("_HOCR.xml")),
							FileType.TIF.getExtensionWithDot());
					thumbFileName = EphesoftStringUtil.concatenate(xmlFile.substring(0, xmlFile.indexOf("_HOCR.xml")), "_th",
							FileType.TIF.getExtensionWithDot());
					pageType.setNewFileName(pageName);
					pageType.setOldFileName(EphesoftStringUtil.concatenate(xmlFile.substring(0, xmlFile.indexOf("_HOCR.xml")),
							FileType.TIF.getExtensionWithDot()));
					pageType.setDirection(Direction.NORTH);
					pageType.setIsRotated(false);
					pageType.setComparisonThumbnailFileName(thumbFileName);
					listOfPages.add(pageType);
				}
				try {
					searchClassificationService.generateConfidenceScoreAPI(xmlDocuments, hocrPages, inputFilesPath,
							batchClassConfigMap, batchClassID);
				} catch (DCMAException dcmaException) {
					LOGGER.error("Exception occured during generating confidence." + dcmaException);
					throw new DCMAException(
							"Could not create confidence scores for search classification. Please check if learning is properly done for batch class.");
				}
			} else {
				respStr = "Improper input for classification.";
				LOGGER.error("Error response at server:" + respStr);
				throw new DCMAApplicationException(respStr);
			}
		} else {
			LOGGER.error("Error response at server:" + respStr);
			throw new DCMAApplicationException(respStr);
		}
		// returning the final result
		return listOfPages;
	}

	/**
	 * @param batchClassID
	 * @param respStr
	 * @param searchClassPlugin
	 * @param docAssemblyPlugin
	 * @return
	 */
	private String validateSearchPluginProperties(String batchClassID, String respStr, BatchPlugin searchClassPlugin,
			BatchPlugin docAssemblyPlugin) {
		if (searchClassPlugin == null || docAssemblyPlugin == null) {
			respStr = "Either Search Classification plugin or document assembly plugin does not exist for the specified batch class id: "
					+ batchClassID;
		} else if (docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP_LP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_LP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_LP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP_LP) == null) {
			respStr = "Incomplete properties of the Document assembler plugin for the specified batch class id :" + batchClassID;
		} else if (searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_VALID_EXTNS) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_INDEX_FIELDS) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_STOP_WORDS) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIN_TERM_FREQ) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIN_DOC_FREQ) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIN_WORD_LENGTH) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MAX_QUERY_TERMS) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_TOP_LEVEL_FIELD) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_NO_OF_PAGES) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MAX_RESULT_COUNT) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_FIRST_PAGE_CONF_WEIGHTAGE) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE) == null
				|| searchClassPlugin.getPluginConfigurations(LuceneProperties.LUCENE_LAST_PAGE_CONF_WEIGHTAGE) == null) {
			respStr = "Incomplete properties of the Search Classification plugin for the specified batch class id :" + batchClassID;
		}
		return respStr;
	}

	/**
	 * Doc creation method for calling the required classification module for classification.
	 * 
	 * @param batchClassID {@link String} the batch class identifier.
	 * @param docAssembler {@link DocumentAssembler} to get the fatory for classification.
	 * @param docs {@link Documents} the documents for classification.
	 * @param listOfPages {@link List<Page>} the list of all the pages.
	 * @param classification {@link String} the classification name.
	 * @return {@link Documents} the resulting documents.
	 * @throws DCMAApplicationException
	 */
	private Documents performDocCreation(String batchClassID, DocumentAssembler docAssembler, Documents docs, List<Page> listOfPages,
			String classification) throws DCMAApplicationException {
		List<Document> xmlDocuments = null;
		try {
			if (classification.equals(DocumentClassificationFactory.SEARCHCLASSIFICATION.getNameClassification())) {
				xmlDocuments = docAssembler.createDocumentAPI(DocumentClassificationFactory.SEARCHCLASSIFICATION, batchClassID,
						listOfPages);
			} else if (classification.equals(DocumentClassificationFactory.IMAGE.getNameClassification())) {
				xmlDocuments = docAssembler.createDocumentAPI(DocumentClassificationFactory.IMAGE, batchClassID, listOfPages);
			} else if (classification.equals(DocumentClassificationFactory.BARCODE.getNameClassification())) {
				xmlDocuments = docAssembler.createDocumentAPI(DocumentClassificationFactory.BARCODE, batchClassID, listOfPages);
			} else if (classification.equals(DocumentClassificationFactory.AUTOMATIC.getNameClassification())) {
				xmlDocuments = docAssembler.createDocumentAPI(DocumentClassificationFactory.AUTOMATIC, batchClassID, listOfPages);
			} else {
				return null;
			}
		} catch (DCMAApplicationException dcmaAppException) {
			LOGGER.error("Exception during creating documents : " + dcmaAppException);
			dcmaAppException.printStackTrace();
			throw new DCMAApplicationException("Could not create documents for classification : " + classification);
		}
		docs.getDocument().addAll(xmlDocuments);
		return docs;
	}

	/**
	 * Writing the result of classification into an XML.
	 * 
	 * @param document {@link Documents} the resulting documents tag after classification.
	 * @param filePath {@link String} the path and name of file to be created.
	 * @param batchClass {@link BatchClass} the object of batch class for which classification is being performed.
	 * @param classificationTypeName {@link String} the name of the classification.
	 */
	private Batch writeToXML(Documents documents, String filePath, BatchClass batchClass, String classificationTypeName,
			String resultFolder) {
		Batch batch = new Batch();
		DocumentClassificationTypes classificationType = new DocumentClassificationTypes();
		classificationType.getDocumentClassificationType().add(classificationTypeName);
		batch.setBatchInstanceIdentifier(FileUtils.EMPTY_STRING);
		batch.setBatchName(FileUtils.EMPTY_STRING);
		batch.setBatchDescription(FileUtils.EMPTY_STRING);
		batch.setBatchPriority(FileUtils.EMPTY_STRING);
		batch.setBatchLocalPath(FileUtils.EMPTY_STRING);
		batch.setUNCFolderPath(batchClass.getUncFolder());
		batch.setBatchClassDescription(batchClass.getDescription());
		batch.setBatchClassIdentifier(batchClass.getIdentifier());
		batch.setBatchClassName(batchClass.getName());
		batch.setBatchClassVersion(batchClass.getVersion());
		com.ephesoft.dcma.batch.schema.Batch.Documents batchDocuments = new Batch.Documents();
		batchDocuments.getDocument().addAll(documents.getDocument());
		batch.setDocumentClassificationTypes(classificationType);
		batch.setDocuments(batchDocuments);
		return writeBatchToXML(filePath, batch, resultFolder);
	}

	/**
	 * @param filePath
	 * @param batch
	 * @return
	 */
	private Batch writeBatchToXML(String filePath, Batch batch, String resultFolder) {
		String folderPath = EphesoftStringUtil.concatenate(filePath, File.separator, resultFolder);
		File testContentResultFolder = new File(folderPath);
		if (!testContentResultFolder.exists()) {
			if (!testContentResultFolder.mkdir()) {
				LOGGER.error("Test classification result folder could not be created. Therefore download of resulting xml will not be done.");
			}
		}
		String outputFilePath = EphesoftStringUtil.concatenate(folderPath, File.separator, resultFolder,
				FileType.XML.getExtensionWithDot());
		File file = new File(outputFilePath);
		try {
			CryptoMarshaller marshaller = this.getSingleBeanOfType(CryptoMarshaller.class);
			FileOutputStream outputStream = new FileOutputStream(file);
			StreamResult result = new FileStreamResult(file, outputStream);
			marshaller.marshal(batch, result);
			IOUtils.closeQuietly(outputStream);
		} catch (Exception e) {
			LOGGER.error("Unable to write the content to xml file.");
		}
		return batch;
	}

	/**
	 * Barcode classification API for performing the classification of barcode for input images..
	 * 
	 * @param batchClassID {@link String} the batch class identifier.
	 * @param inputFilesPath {@link String} the path for input files to be processed.
	 * @param batchClassPPService {@link PluginPropertiesService} the service to extract properties of plugin.
	 * @param barcodeService {@link BarcodeService} the service for performing bar code classifications.
	 * @param listOfPages {@link List<Page>} the list of pages to be processed.
	 * @return {@link List<Page>} the updated list of pages.
	 * @throws DCMAException
	 * @throws DCMAApplicationException
	 */
	private List<Page> barcodeClassificationAPI(String batchClassID, String inputFilesPath,
			PluginPropertiesService batchClassPPService, BarcodeService barcodeService, List<Page> listOfPages,
			RegexService regexService) throws DCMAException, DCMAApplicationException {
		Map<BarcodeProperties, String> batchClassConfigMap = new HashMap<BarcodeProperties, String>();
		try {
			batchClassConfigMap.put(BarcodeProperties.BARCODE_VALID_EXTNS, batchClassPPService.getPropertyValue(batchClassID,
					ICommonConstants.BARCODE_READER_PLUGIN, BarcodeProperties.BARCODE_VALID_EXTNS));
			batchClassConfigMap.put(BarcodeProperties.BARCODE_READER_TYPES, batchClassPPService.getPropertyValue(batchClassID,
					ICommonConstants.BARCODE_READER_PLUGIN, BarcodeProperties.BARCODE_READER_TYPES));
			batchClassConfigMap.put(BarcodeProperties.MAX_CONFIDENCE, batchClassPPService.getPropertyValue(batchClassID,
					ICommonConstants.BARCODE_READER_PLUGIN, BarcodeProperties.MAX_CONFIDENCE));
			batchClassConfigMap.put(BarcodeProperties.MIN_CONFIDENCE, batchClassPPService.getPropertyValue(batchClassID,
					ICommonConstants.BARCODE_READER_PLUGIN, BarcodeProperties.MIN_CONFIDENCE));
		} catch (NullPointerException nullPointerException) {
			LOGGER.error("Exception in fetching the properties for Barcode plugin. " + nullPointerException);
			nullPointerException.printStackTrace();
			throw new DCMAException(
					"Problem fetching the properties of Barcode plugin. Please verify the plugin in batch class with identifier: "
							+ batchClassID);
		}
		// SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);
		// ocrInputFiles(batchClassID, inputFilesPath, searchClassificationService, false);
		ObjectFactory objectFactory = new ObjectFactory();
		CustomFileFilter filter = new CustomFileFilter(false, FileType.TIF.getExtension());
		String listOfFiles[] = new File(inputFilesPath).list(filter);

		List<Document> xmlDocuments = new ArrayList<Document>();
		Document doc = objectFactory.createDocument();
		Pages pages = new Pages();
		List<Page> pageList = pages.getPage();
		if (null != listOfPages) {
			// if still value is null then we have to check for pages part.
			pageList.addAll(listOfPages);
		} else {
			int pageID = 0;
			for (String page : listOfFiles) {
				if (new File(inputFilesPath + File.separator + page).isFile() && !page.contains("_th.")) {
					Page pageType = objectFactory.createPage();
					pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + pageID);
					// String pageName = EphesoftStringUtil.concatenate(page.substring(0, page.indexOf("-0")), FileType.TIF
					// .getExtensionWithDot());
					pageType.setNewFileName(page);
					pageType.setHocrFileName(page.substring(0, page.indexOf(".tif")) + "_HOCR.xml");
					pageType.setOldFileName(page);
					pageID++;
					pageList.add(pageType);
				}
			}

		}
		xmlDocuments.add(doc);
		doc.setPages(pages);
		String batchInstanceIdentifier = new File(inputFilesPath).getName() + Math.random();
		try {
			barcodeService.extractPageBarCodeAPI(xmlDocuments, batchInstanceIdentifier, inputFilesPath, batchClassConfigMap);
		} catch (DCMAException dcmaException) {
			LOGGER.error("Exception in fetching the properties for Barcode plugin. " + dcmaException);
			dcmaException.printStackTrace();
			throw new DCMAApplicationException("Could not generate the confidence for barcode extraction.");
		}
		// addition of KV_PP_plugin results in barcode.
		try {
			regexService.regexPPReaderAPI(batchClassID, xmlDocuments, inputFilesPath);
		} catch (DCMAException dcmaException) {
			LOGGER.error("Exception in executing KV Page Process plugin. " + dcmaException);
			dcmaException.printStackTrace();
		}
		return pageList;
	}

	/**
	 * To perform image compare classification for the inputs provided.
	 * 
	 * @param batchClassID {@link String} the batch class id from which properties are to be picked up.
	 * @param inputFilesPath {@link String} the input files folder path.
	 * @param testClassificationFolder {@link File} the file folder for input files.
	 * @param batchClassPPService {@link BatchClassPluginPropertiesService} the service to fetch batch class plugin properties details.
	 * @param imService {@link ImageProcessService} service for image processing.
	 * @param listOfPages {@link List<Page>} the list of all pages.
	 * @return {@link List<Page>} the updated list of all pages.
	 * @throws DCMAException
	 * @throws DCMAApplicationException
	 */
	private List<Page> imageClassificationAPI(String batchClassID, String inputFilesPath, File testClassificationFolder,
			PluginPropertiesService batchClassPPService, ImageProcessService imService, List<Page> listOfPages) throws DCMAException,
			DCMAApplicationException {
		String respStr = FileUtils.EMPTY_STRING;
		BatchPlugin createThumbPlugin = batchClassPPService.getPluginProperties(batchClassID,
				ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN);
		BatchPlugin classifyImgPlugin = batchClassPPService.getPluginProperties(batchClassID,
				ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN);
		BatchPlugin docAssemblyPlugin = batchClassPPService.getPluginProperties(batchClassID,
				DocumentAssemblerConstants.DOCUMENT_ASSEMBLER_PLUGIN);
		respStr = validatePluginPresence(batchClassID, respStr, createThumbPlugin, classifyImgPlugin, docAssemblyPlugin);
		if (respStr.isEmpty()) {
			String[] listOfFiles = testClassificationFolder.list();
			String[][] sListOfTiffFiles = new String[listOfFiles.length][3];
			if (respStr.isEmpty()) {
				final String outputParams = batchClassPPService.getPropertyValue(batchClassID,
						ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_OUTPUT_IMAGE_PARAMETERS);
				String compareThumbnailH = batchClassPPService.getPropertyValue(batchClassID,
						ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_HEIGHT);
				String compareThumbnailW = batchClassPPService.getPropertyValue(batchClassID,
						ImageMagicKConstants.CREATE_THUMBNAILS_PLUGIN, ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_WIDTH);
				String batchId = new File(inputFilesPath).getName() + Math.random();
				ObjectFactory objectFactory = new ObjectFactory();
				Pages pages = new Pages();
				// Delete redundant thumbnail files.
				deleteThumbnailFiles(inputFilesPath);
				String[] imageFiles = new File(inputFilesPath).list(new CustomFileFilter(false, FileType.TIFF.getExtensionWithDot(),
						FileType.TIF.getExtensionWithDot()));
				if (null != listOfPages) {
					for (int i = 0; i < imageFiles.length; i++) {
						if (!imageFiles[i].contains("_th")) {
							String fileName = inputFilesPath + File.separator + imageFiles[i];
							String thumbFileName = imageFiles[i].substring(0,
									imageFiles[i].indexOf(FileType.TIF.getExtensionWithDot()))
									+ "_th" + FileType.TIF.getExtensionWithDot();
							String fileTiffPath = inputFilesPath + File.separator + thumbFileName;
							sListOfTiffFiles[i][0] = fileName;
							sListOfTiffFiles[i][1] = fileTiffPath;
							sListOfTiffFiles[i][2] = Integer.toString(i);
						}
					}
				} else {
					listOfPages = pages.getPage();
					for (int i = 0; i < imageFiles.length; i++) {
						if (!imageFiles[i].contains("_th")) {
							String fileName = inputFilesPath + File.separator + imageFiles[i];
							String thumbFileName = imageFiles[i].substring(0,
									imageFiles[i].indexOf(FileType.TIF.getExtensionWithDot()))
									+ "_th" + FileType.TIF.getExtensionWithDot();
							String hocrFileName = EphesoftStringUtil
									.concatenate(
											imageFiles[i].substring(0, imageFiles[i].indexOf(FileType.TIF.getExtensionWithDot())),
											"_HOCR.xml");
							String fileTiffPath = inputFilesPath + File.separator + thumbFileName;
							sListOfTiffFiles[i][0] = fileName;
							sListOfTiffFiles[i][1] = fileTiffPath;
							sListOfTiffFiles[i][2] = Integer.toString(i);

							Page pageType = objectFactory.createPage();
							pageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + i);
							String pageName = "";
							if (imageFiles[i].indexOf("-0") != -1) {
								pageName = EphesoftStringUtil.concatenate(imageFiles[i].substring(0, imageFiles[i].indexOf("-0")),
										FileType.TIF.getExtensionWithDot());
							} else {
								pageName = imageFiles[i];
							}
							pageType.setNewFileName(pageName);
							// if (pageName.contains("-0000")) {
							// pageType.setOldFileName(pageName.substring(0, pageName.indexOf("-0000"))
							// + FileType.TIF.getExtensionWithDot());
							// } else {
							pageType.setOldFileName(imageFiles[i]);
							// }
							pageType.setDirection(Direction.NORTH);
							pageType.setIsRotated(false);
							pageType.setHocrFileName(hocrFileName);
							pageType.setComparisonThumbnailFileName(thumbFileName);
							listOfPages.add(pageType);
						}
					}

				}
				boolean createThumbnailFlag = true;
				if (null != sListOfTiffFiles && sListOfTiffFiles.length > 0) {
					for (int i = 0; i < sListOfTiffFiles.length; i++) {
						String filePath = sListOfTiffFiles[i][1];
						if (null != filePath) {
							File thumbnailFile = new File(filePath);
							if (filePath.contains("_th") && null != thumbnailFile && thumbnailFile.exists()) {
								createThumbnailFlag = false;
								break;
							}
						}
					}
				}
				if (createThumbnailFlag) {
					BatchInstanceThread threadList = null;
					threadList = imService.createCompThumbForImage(batchId, inputFilesPath, sListOfTiffFiles, outputParams,
							compareThumbnailH, compareThumbnailW);
					threadList.execute();
				}
				// invoke the Classification Image plugin.
				String imMetric = batchClassPPService.getPropertyValue(batchClassID, ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN,
						ImageMagicProperties.CLASSIFY_IMAGES_COMP_METRIC);
				String imFuzz = batchClassPPService.getPropertyValue(batchClassID, ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN,
						ImageMagicProperties.CLASSIFY_IMAGES_FUZZ_PERCNT);
				String maxVal = batchClassPPService.getPropertyValue(batchClassID, ImageMagicKConstants.CLASSIFY_IMAGES_PLUGIN,
						ImageMagicProperties.CLASSIFY_IMAGES_MAX_RESULTS);
				// Classifying the images for image classification.
				try {
					imService.classifyImagesAPI(maxVal, imMetric, imFuzz, batchId, batchClassID, inputFilesPath, listOfPages);
				} catch (DCMAException dcmaException) {
					LOGGER.error("Problem occured in generating the confidence for image classification. Exception is : "
							+ dcmaException);
					throw new DCMAException(
							"Problem occured in generating the confidence for image classification. Please check if learning is properly done for batch class.");
				}
			} else {
				LOGGER.error("Error response at server:" + respStr);
				throw new DCMAException(respStr);
			}
		} else {
			LOGGER.error("Error response at server:" + respStr);
			throw new DCMAException(respStr);
		}
		return listOfPages;
	}

	/**
	 * @param batchClassID
	 * @param respStr
	 * @param createThumbPlugin
	 * @param classifyImgPlugin
	 * @param docAssemblyPlugin
	 * @return
	 */
	private String validatePluginPresence(String batchClassID, String respStr, BatchPlugin createThumbPlugin,
			BatchPlugin classifyImgPlugin, BatchPlugin docAssemblyPlugin) {
		if (createThumbPlugin == null || classifyImgPlugin == null || docAssemblyPlugin == null) {
			respStr = "Either create Thumbnails plugin or Classify Image plugin or document assembly plugin does not exist for the specified batch class id: "
					+ batchClassID;
			LOGGER.error("Error response at server:" + respStr);
		} else if (createThumbPlugin.getPluginConfigurations(ImageMagicProperties.CREATE_THUMBNAILS_OUTPUT_IMAGE_PARAMETERS) == null
				|| createThumbPlugin.getPluginConfigurations(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_WIDTH) == null
				|| createThumbPlugin.getPluginConfigurations(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_HEIGHT) == null) {
			respStr = "Please check Create Thumbnails plugin in Page Process module for its properties in batch class id: "
					+ batchClassID;
			LOGGER.error("Error response at server:" + respStr);
		} else if (classifyImgPlugin.getPluginConfigurations(ImageMagicProperties.CLASSIFY_IMAGES_COMP_METRIC) == null
				|| classifyImgPlugin.getPluginConfigurations(ImageMagicProperties.CLASSIFY_IMAGES_MAX_RESULTS) == null
				|| classifyImgPlugin.getPluginConfigurations(ImageMagicProperties.CLASSIFY_IMAGES_FUZZ_PERCNT) == null) {
			respStr = "Please check Classify Images plugin in Page Process module for its properties in batch class id: "
					+ batchClassID;
			LOGGER.error("Error response at server:" + respStr);
		} else if (docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP_LP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_LP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_LP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_FP_MP) == null
				|| docAssemblyPlugin.getPluginConfigurations(DocumentAssemblerProperties.DA_RULE_MP_LP) == null) {
			respStr = "Incomplete properties of the Document assembler plugin for the specified batch class id: " + batchClassID;
			LOGGER.error("Error response at server:" + respStr);
		}
		return respStr;
	}

	private String checkAndCreateRequiredFolders(String batchClassID, BatchSchemaService batchSchemaService) throws Exception {
		StringBuilder folderPathBuilder = new StringBuilder(batchSchemaService.getBaseFolderLocation());
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(batchClassID);
		File batchClassFolder = new File(folderPathBuilder.toString());
		if (!batchClassFolder.exists()) {
			LOGGER.info("Batch Class Folder does not exist.");
			throw new UIException(BatchClassConstants.ERROR_BC_FOLDER_NOT_EXIST);
		}

		// The path for test classification folder is found.......
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append("test-extraction");
		String inputFilesPath = folderPathBuilder.toString();
		// checking if there are files present in folder.
		File testExtractionFolder = new File(inputFilesPath);
		if (!testExtractionFolder.exists()) {
			testExtractionFolder.mkdir();
			LOGGER.error("Test Extraction folder does not exist.");
			throw new UIException(BatchClassConstants.ERROR_TEST_EXTRACTION_FOLDER_NOT_EXIST);
		}
		// checking if there are files present in folder.
		File[] listOfimages = testExtractionFolder.listFiles(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
				FileType.TIFF.getExtensionWithDot(), FileType.PDF.getExtensionWithDot()));
		if (null == listOfimages || listOfimages.length < 1) {
			LOGGER.error("There are no proper input files present. Please upload tif, tiff or pdf files.");
			throw new UIException(BatchClassConstants.ERROR_IMPROPER_INPUT_FILES);
		}

		folderPathBuilder.append(File.separator);
		folderPathBuilder.append("test-extraction-result");
		File resultFolder = new File(folderPathBuilder.toString());
		if (resultFolder.exists()) {
			FileUtils.deleteDirectoryAndContents(folderPathBuilder.toString());
		}
		return inputFilesPath;
	}

	@Override
	public Boolean isExtractionFolderEmpty(String batchClassID) throws UIException {
		boolean isEmpty = checkIfFolderEmpty(batchClassID, BatchClassConstants.TEST_EXTRACTION_FOLDER_NAME);
		return isEmpty;
	}

	@Override
	public Boolean isClassificationFolderEmpty(String batchClassID) throws UIException {
		boolean isEmpty = checkIfFolderEmpty(batchClassID, BatchClassConstants.TEST_CLASSIFICATION_FOLDER_NAME);
		return isEmpty;
	}

	private boolean checkIfFolderEmpty(String batchClassID, String folderName) throws UIException {
		boolean isEmpty = true;
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		StringBuilder folderPathBuilder = new StringBuilder(batchSchemaService.getBaseFolderLocation());
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(batchClassID);
		File batchClassFolder = new File(folderPathBuilder.toString());
		if (!batchClassFolder.exists()) {
			LOGGER.info("Batch Class Folder does not exist.");
			throw new UIException("Unable to check folder empty or not as batch Class Folder does not exist.");
		}
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(folderName);
		String inputFilesPath = folderPathBuilder.toString();
		File testClassificationFolder = new File(inputFilesPath);
		if (testClassificationFolder.exists()) {
			isEmpty = FileUtils.checkForEmptyDirectory(inputFilesPath);
		}
		return isEmpty;
	}

	@Override
	public void clearExtractionFolder(String batchClassID) throws Exception {
		clearFolderContents(batchClassID, BatchClassConstants.TEST_EXTRACTION_FOLDER_NAME);
	}

	@Override
	public void clearClassificationFolder(String batchClassID) throws Exception {
		clearFolderContents(batchClassID, BatchClassConstants.TEST_CLASSIFICATION_FOLDER_NAME);
	}

	/**
	 * @param batchClassID
	 * @throws Exception
	 */
	private void clearFolderContents(String batchClassID, String folderName) throws Exception {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		StringBuilder folderPathBuilder = new StringBuilder(batchSchemaService.getBaseFolderLocation());
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(batchClassID);
		File batchClassFolder = new File(folderPathBuilder.toString());
		if (!batchClassFolder.exists()) {
			LOGGER.info("Batch Class Folder does not exist.");
			throw new Exception("Unable to clear folder. Batch Class Folder does not exist.");
		}

		// The path for test classification folder is found.......
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(folderName);
		String inputFilesPath = folderPathBuilder.toString();

		// checking if there are files present in folder.
		File testExtractionFolder = new File(inputFilesPath);
		if (testExtractionFolder.exists()) {

			// String backUpFolderPath = EphesoftStringUtil.concatenate(inputFilesPath, File.separator, folderName,
			// ICommonConstants.BACK_UP_FOLDER_NAME);

			String resultXMLFolderPath = EphesoftStringUtil.concatenate(inputFilesPath, File.separator, folderName, "-result");

			// if (!FileUtils.deleteDirectoryAndContents(new File(backUpFolderPath))) {
			// LOGGER.error("Could not delete the back up folder.");
			// }

			File resultXmlFolder = new File(resultXMLFolderPath);
			if (resultXmlFolder.exists()) {
				if (!FileUtils.deleteDirectoryAndContents(resultXmlFolder)) {
					LOGGER.error("Could not delete the result XML folder.");
				}
			}
			String keyFile = EphesoftStringUtil.concatenate(inputFilesPath, File.separator, CryptographyConstant.KEYSTORE_FILE_NAME);
			new File(keyFile).delete();
			if (!FileUtils.deleteContentsOnly(inputFilesPath)) {
				throw new Exception("Unable to clear folder.");
			}
		}
	}

	@Override
	public BatchClassDTO createAndDeployWorkflowJPDL(String workflowName, BatchClassDTO batchClassDTO, boolean isGridWorkflow)
			throws UIException {
		LOGGER.info("Saving Batch Class before deploying");
		batchClassDTO = updateBatchClass(batchClassDTO);

		DeploymentService deploymentService = this.getSingleBeanOfType(DeploymentService.class);
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);

		BatchClass batchClass = new BatchClass();
		String batchClassIdentifier = batchClassDTO.getIdentifier();
		batchClass = batchClassService.get(batchClassIdentifier);

		try {
			deploymentService.createAndDeployProcessDefinition(batchClass, isGridWorkflow);
		} catch (DCMAException e) {
			throw new UIException("Unable to deploy workflow");
		}
		batchClassDTO.setDirty(false);

		return batchClassDTO;
	}

	@Override
	public BatchFolderListDTO getBatchFolderList() {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		BatchFolderListDTO batchFolderListDTO = new BatchFolderListDTO();

		batchFolderListDTO.setCmisPluginMapping(batchSchemaService.getCmisPluginMappingFolderName());
		batchFolderListDTO.setExportToImageImportPluginMapping(batchSchemaService.getExportToImageImportPluginMappingFolderName());
		batchFolderListDTO.setFuzzyDBIndexFolderName(batchSchemaService.getFuzzyDBIndexFolderName());
		batchFolderListDTO.setImageMagickBaseFolderName(batchSchemaService.getImagemagickBaseFolderName());
		batchFolderListDTO.setProjectFilesBaseFolder(batchSchemaService.getProjectFileBaseFolder());
		batchFolderListDTO.setScripts(batchSchemaService.getScriptFolderName());
		batchFolderListDTO.setSearchIndexFolderName(batchSchemaService.getSearchIndexFolderName());
		batchFolderListDTO.setSearchSampleName(batchSchemaService.getSearchSampleName());
		batchFolderListDTO.setBatchClassSerializableFile(batchSchemaService.getBatchClassSerializableFile());
		batchFolderListDTO.setFileboundPluginMappingFolderName(batchSchemaService.getFileboundPluginMappingFolderName());
		batchFolderListDTO.initFolderList();

		return batchFolderListDTO;
	}

	@Override
	public ModuleDTO createNewModule(ModuleDTO moduleDTO) throws UIException {
		LOGGER.info("Creating new Module with name: " + moduleDTO);
		ModuleService moduleService = this.getSingleBeanOfType(ModuleService.class);
		Module module = new Module();
		String moduleName = moduleDTO.getName();
		Module checkModule = moduleService.getModuleByName(moduleName);
		if (checkModule == null) {
			BatchClassUtil.mergeModuleFromDTO(module, moduleDTO);
			moduleService.createNewModule(module);
		} else {
			throw new UIException("A module with same name already exists. Please try another name.");
		}
		return BatchClassUtil.createModuleDTO(module);
	}

	@Override
	public List<String> getAdvancedKVImageUploadPath(String batchClassId, String docName, String imageName, boolean getPageCount) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Getting image url for image = ", imageName));
		LOGGER.debug(EphesoftStringUtil.concatenate("batchClassId : ", batchClassId, ", docName : ", docName, ", imageName : ",
				imageName));
		String imageUrl = null;
		List<String> imageUrlNPageCount = new ArrayList<String>();
		if (batchClassId != null && docName != null && imageName != null) {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			String testExtractionFolderPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassId, true);
			docName = docName.trim();
			File imageFile = new File(EphesoftStringUtil.concatenate(testExtractionFolderPath, File.separator, docName,
					File.separator, imageName));
			if (imageFile.exists()) {
				imageUrl = EphesoftStringUtil.concatenate(batchSchemaService.getBaseHttpURL(), URL_SEPARATOR, batchClassId,
						URL_SEPARATOR, batchSchemaService.getTestAdvancedKVExtractionFolderName(), URL_SEPARATOR,
						EphesoftStringUtil.encode(docName), URL_SEPARATOR, EphesoftStringUtil.encode(imageName));
			}
		}
		LOGGER.debug(EphesoftStringUtil.concatenate("Image url path : ", imageUrl));
		imageUrlNPageCount.add(imageUrl);
		if (getPageCount) {
			int pageCount = getPageCount(imageUrl);
			imageUrlNPageCount.add(String.valueOf(pageCount));
		}
		return imageUrlNPageCount;
	}

	public int getPageCount(String imageUrl) {
		int count = -1;
		try {
			if (!StringUtil.isNullOrEmpty(imageUrl)) {
				String fileExtension = imageUrl.substring(imageUrl.lastIndexOf("."), imageUrl.length());
				LOGGER.debug("image url" + imageUrl + " extension " + fileExtension);
				if (fileExtension.equalsIgnoreCase(FileType.PDF.getExtensionWithDot())) {
					count = PDFUtil.getPDFPageCount(imageUrl);
				} else if (fileExtension.equalsIgnoreCase(FileType.TIF.getExtensionWithDot())
						|| fileExtension.equalsIgnoreCase(FileType.TIFF.getExtensionWithDot())) {
					count = TIFFUtil.getTIFFPageCount(imageUrl);
				} else {
					LOGGER.error("Unsupported File extensions. Valid extensions are pdf, tif and tiff");
				}
			}
		} catch (Exception exception) {
			LOGGER.error("Error while getting page Count of file " + imageUrl);
		}
		return count;
	}

	@Override
	public List<OutputDataCarrierDTO> testAdvancedKVExtraction(final BatchClassDTO batchClassDTO,
			final KVExtractionDTO kvExtractionDTO, final String docName, final String imageName) throws Exception {
		LOGGER.debug(EphesoftStringUtil.concatenate("Test Advanced KV Extraction for image : ", imageName));
		List<OutputDataCarrierDTO> outputDataCarrierDTOs = null;
		if (imageName == null) {
			LOGGER.debug("Image name is null.");
			throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
		}

		String uploadedImageName = imageName;
		if (imageName.contains(FileType.PDF.getExtensionWithDot())) {
			uploadedImageName = FileUtils.changeFileExtension(imageName, FileType.TIF.getExtension());
		}
		try {
			if (batchClassDTO == null) {
				LOGGER.debug("Batch Class DTO is null.");
				throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
			}
			if (docName == null) {
				LOGGER.debug("Document Name is null.");
				throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
			}
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			// Get path upto test-advanced-extraction folder.
			String testKVFolderPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassDTO.getIdentifier(), true);
			if (testKVFolderPath == null) {
				LOGGER.info("Test KV Folder path is null");
				throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
			}
			String testKVFileName = EphesoftStringUtil.concatenate(testKVFolderPath, File.separator, docName, File.separator,
					uploadedImageName);

			File destinationImageFile = new File(testKVFileName);
			if (destinationImageFile.exists()) {
				// hOCR file and get the list of html file names.
				outputDataCarrierDTOs = testKVExtraction(batchClassDTO, kvExtractionDTO, destinationImageFile.getAbsolutePath(), true);
			} else {
				LOGGER.error(EphesoftStringUtil.concatenate("Image doesn't exist = ", destinationImageFile.getAbsolutePath(),
						". Cannot continue ocring..."));
				throw new Exception(EphesoftStringUtil.concatenate("Image Not Found : ", destinationImageFile.getAbsolutePath()));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
		}
		return outputDataCarrierDTOs;
	}

	public List<OutputDataCarrierDTO> testKVExtraction(final BatchClassDTO batchClassDTO, final KVExtractionDTO kvExtractionDTO,
			final String testImageFilePath, final boolean isTestAdvancedKV) throws Exception {
		if (batchClassDTO == null) {
			LOGGER.error("Batch Class DTO is null.");
			throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
		}
		SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		KVFinderService kVFinderService = this.getSingleBeanOfType(KVFinderService.class);
		ImageProcessService imageProcessService = this.getSingleBeanOfType(ImageProcessService.class);
		BatchClassID batchClassID = new BatchClassID(batchClassDTO.getIdentifier());

		Map<String, String> imageToOCRFilePath = null;
		List<InputDataCarrier> inputDataCarrierList = new ArrayList<InputDataCarrier>();
		List<OutputDataCarrierDTO> carrierDTOs = new ArrayList<OutputDataCarrierDTO>();
		List<OutputDataCarrierDTO> carrierDTOsNoResult = new ArrayList<OutputDataCarrierDTO>();
		InputDataCarrier inputDataCarrier = BatchClassUtil.createInputDataCarrierFromKVExtDTO(kvExtractionDTO);
		inputDataCarrierList.add(inputDataCarrier);
		try {
			String testExtractionFolderPath = batchSchemaService.getTestKVExtractionFolderPath(batchClassID, true);
			String ocrEngineName = getOCRPluginNameForBatchClass(batchClassDTO.getIdentifier());
			BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
			List<File> allImageFiles = null;
			File testImageFile = null;
			if (isTestAdvancedKV) {
				if (testImageFilePath == null || testImageFilePath.isEmpty()) {
					LOGGER.error("File Image Path is either null or empty");
					throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
				}
				testImageFile = new File(testImageFilePath);
				testExtractionFolderPath = testImageFile.getParent();
			}
			allImageFiles = imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClassID, testExtractionFolderPath, testImageFile,
					isTestAdvancedKV, batchInstanceThread);
			batchInstanceThread.execute();

			if (!isTestAdvancedKV && allImageFiles != null) {
				// delete the original files.
				for (File imageFile : allImageFiles) {
					imageFile.delete();
				}
			}

			imageToOCRFilePath = searchClassificationService.generateHOCRForKVExtractionTest(testExtractionFolderPath, ocrEngineName,
					batchClassDTO.getIdentifier(), testImageFile, isTestAdvancedKV);
			File file = new File(testExtractionFolderPath + File.separator + "tempfile");
			if (imageToOCRFilePath != null && !imageToOCRFilePath.isEmpty()) {
				Map<String, KeyValueFieldCarrier> fieldTypeKVMap = new HashMap<String, KeyValueFieldCarrier>();
				extractKeyValueRecursively(batchClassDTO, kvExtractionDTO, batchSchemaService, kVFinderService, imageToOCRFilePath,
						inputDataCarrierList, carrierDTOs, carrierDTOsNoResult, ocrEngineName, file, fieldTypeKVMap);
			}
			carrierDTOs.addAll(carrierDTOsNoResult);
			boolean isTempFileDeleted = file.delete();
			if (!isTempFileDeleted) {
				file.delete();
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
		}

		return carrierDTOs;
	}

	private void extractKeyValueRecursively(BatchClassDTO batchClassDTO, KVExtractionDTO kvExtractionDTO,
			BatchSchemaService batchSchemaService, KVFinderService kVFinderService, Map<String, String> imageToOCRFilePath,
			List<InputDataCarrier> inputDataCarrierList, List<OutputDataCarrierDTO> carrierDTOs,
			List<OutputDataCarrierDTO> carrierDTOsNoResult, String ocrEngineName, File file,
			Map<String, KeyValueFieldCarrier> fieldTypeKVMap) throws DCMAException {
		LOGGER.info("Entering method extractKeyValueRecursively....");
		String fieldType = kvExtractionDTO.getFieldTypeDTO().getName();
		KeyValueFieldCarrier keyValueFieldCarrier = fieldTypeKVMap.get(fieldType);
		if (keyValueFieldCarrier == null) {
			keyValueFieldCarrier = new KeyValueFieldCarrier();
			fieldTypeKVMap.put(fieldType, keyValueFieldCarrier);
		}
		boolean useExisitingKey = kvExtractionDTO.isUseExistingKey();
		LOGGER.info("Use existing key : " + useExisitingKey);
		if (useExisitingKey) {
			FieldTypeDTO fieldTypeDTO = kvExtractionDTO.getFieldTypeDTO().getDocTypeDTO()
					.getFieldTypeByName(kvExtractionDTO.getKeyPattern());
			List<KVExtractionDTO> kvExtractionDTOList = fieldTypeDTO.getKvExtractionList();
			for (KVExtractionDTO keyValueExtractionDTO : kvExtractionDTOList) {
				List<InputDataCarrier> localInputDataCarrierList = new ArrayList<InputDataCarrier>();
				InputDataCarrier inputDataCarrier = BatchClassUtil.createInputDataCarrierFromKVExtDTO(keyValueExtractionDTO);
				localInputDataCarrierList.add(inputDataCarrier);
				extractKeyValueRecursively(batchClassDTO, keyValueExtractionDTO, batchSchemaService, kVFinderService,
						imageToOCRFilePath, localInputDataCarrierList, carrierDTOs, carrierDTOsNoResult, ocrEngineName, file,
						fieldTypeKVMap);
			}
		}
		carrierDTOsNoResult.clear();
		carrierDTOs.clear();

		for (String imagePath : imageToOCRFilePath.keySet()) {

			String hocrFilePath = imageToOCRFilePath.get(imagePath);
			File ocrFile = new File(hocrFilePath);

			HocrPages hocrPages = batchSchemaService.getHOCR(hocrFilePath);

			if (hocrPages != null) {
				HocrPage hocrPage = hocrPages.getHocrPage().get(0);
				List<OutputDataCarrier> outputDataCarrierList = kVFinderService.findKeyValue(inputDataCarrierList, hocrPage,
						fieldTypeKVMap, keyValueFieldCarrier, Integer.MAX_VALUE, imagePath);
				BatchClassUtil.createOutputDataDTOFromOutputDataCarrier(outputDataCarrierList, carrierDTOs, carrierDTOsNoResult,
						ocrFile.getName());
			}
		}
		LOGGER.info("Exiting method extractKeyValueRecursively....");
	}

	// New Method to get span value at coordinate clicked by User.
	public List<Span> getSpanValues(final BatchClassDTO batchClassDTO, String imageName, String docName, LinkedList<Integer> pageProp)
			throws Exception {
		LOGGER.debug(EphesoftStringUtil.concatenate("Getting Span Values for image : ", imageName));
		List<Span> values = null;
		String uploadedImageName = imageName;

		if (imageName.contains(FileType.PDF.getExtensionWithDot())) {
			uploadedImageName = FileUtils.changeFileExtension(imageName, FileType.TIF.getExtension());
		}
		try {
			if (batchClassDTO == null) {
				LOGGER.debug("Batch Class DTO is null.");
				throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
			}
			if (docName == null) {
				LOGGER.debug("Document Name is null.");
				throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
			}
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			// Get path upto test-advanced-extraction folder.
			String testKVFolderPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassDTO.getIdentifier(), true);
			if (testKVFolderPath == null) {
				LOGGER.info("Test KV Folder path is null");
				throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
			}
			testKVFolderPath = EphesoftStringUtil.concatenate(testKVFolderPath, File.separator, docName);
			String testKVFileName = EphesoftStringUtil.concatenate(testKVFolderPath, File.separator, uploadedImageName);
			File destinationImageFile = new File(testKVFileName);
			// Creating an Empty Image of same size.
			String emptyImageName = EphesoftStringUtil.concatenate(
					testKVFileName.substring(0, testKVFileName.lastIndexOf(FileType.TIF.getExtensionWithDot())), "empty",
					FileType.PNG.getExtensionWithDot());

			BufferedImage image = new BufferedImage(pageProp.get(0), pageProp.get(1), BufferedImage.TYPE_INT_RGB);
			image.getGraphics().setColor(Color.WHITE);
			image.getGraphics().fillRect(0, 0, image.getWidth(), image.getHeight());
			File file = new File(emptyImageName);
			ImageIO.write(image, "PNG", file);
			if (destinationImageFile.exists()) {
				SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);
				String ocrEngineName = getOCRPluginNameForBatchClass(batchClassDTO.getIdentifier());
				Map<String, String> imageToOCRFilePath = searchClassificationService.generateHOCRForKVExtractionTest(testKVFolderPath,
						ocrEngineName, batchClassDTO.getIdentifier(), new File(destinationImageFile.getAbsolutePath()), true);
				for (String imagePath : imageToOCRFilePath.keySet()) {
					String hocrFilePath = imageToOCRFilePath.get(imagePath);
					HocrPages hocrPages = batchSchemaService.getHOCR(hocrFilePath);
					if (hocrPages != null) {
						HocrPage hocrPage = hocrPages.getHocrPage().get(0);
						final Spans spans = hocrPage.getSpans();
						values = spans.getSpan();
						LOGGER.debug("Span Size" + values.size());
					}
				}
			} else {
				LOGGER.error(EphesoftStringUtil.concatenate("Image doesn't exist = ", destinationImageFile.getAbsolutePath(),
						". Cannot continue ocring..."));
				throw new Exception(EphesoftStringUtil.concatenate("Image Not Found : ", destinationImageFile.getAbsolutePath()));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
		}

		return values;
	}

	public String generateEmptyFileName(final BatchClassDTO batchClassDTO, String imageName, String docName) throws Exception {
		String uploadedImageName = imageName;
		if (imageName.contains(FileType.PDF.getExtensionWithDot())) {
			uploadedImageName = FileUtils.changeFileExtension(imageName, FileType.TIF.getExtension());
		}
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String testKVFolderPath = batchSchemaService.getTestAdvancedKvExtractionFolderPath(batchClassDTO.getIdentifier(), true);
		if (testKVFolderPath == null) {
			LOGGER.info("Test KV Folder path is null");
			throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
		}
		String testKVFileName = EphesoftStringUtil.concatenate(testKVFolderPath, File.separator, docName, File.separator,
				uploadedImageName);
		String emptyImageName = EphesoftStringUtil.concatenate(
				testKVFileName.substring(0, testKVFileName.lastIndexOf(FileType.TIF.getExtensionWithDot())), "empty",
				FileType.PNG.getExtensionWithDot());
		return emptyImageName;
	}

	public void deleteEmptyFile(final BatchClassDTO batchClassDTO, String imageName, String docName) throws Exception {
		try {
			String emptyFileName = generateEmptyFileName(batchClassDTO, imageName, docName);
			File emptyFile = new File(emptyFileName);
			if (emptyFile.exists()) {
				emptyFile.delete();
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new Exception(BatchClassMessages.TEST_KV_FAILURE);
		}
	}

	@Override
	public List<ModuleDTO> getAllModules() {
		LOGGER.info("Getting list of modules.");
		ModuleService moduleService = this.getSingleBeanOfType(ModuleService.class);
		List<Module> modulesList = moduleService.getAllModules();
		List<ModuleDTO> moduleDTOsList = new ArrayList<ModuleDTO>();
		for (Module module : modulesList) {
			LOGGER.info("Adding " + module.getName() + " module to the list.");
			moduleDTOsList.add(BatchClassUtil.createModuleDTO(module));
		}
		return moduleDTOsList;
	}

	@Override
	public List<PluginDetailsDTO> getAllPluginDetailDTOs() {
		PluginService pluginService = this.getSingleBeanOfType(PluginService.class);
		List<PluginDetailsDTO> allPluginDetailsDTO = new ArrayList<PluginDetailsDTO>();
		for (Plugin plugin : pluginService.getAllPlugins()) {
			PluginDetailsDTO pluginDetailsDTO = BatchClassUtil.createPluginDetailsDTO(plugin, pluginService);
			allPluginDetailsDTO.add(pluginDetailsDTO);
		}
		return allPluginDetailsDTO;
	}

	@Override
	public boolean importBatchClass(ImportBatchClassUserOptionDTO userOptions, boolean isGridWorkflow) throws UIException {
		String path = userOptions.getUncFolder();
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}

		// Edited to support: if an existing UNC folder is used as UNC folder for the imported batch class then empty check is not
		// applicable.
		if (!userOptions.isImportExisting()) {

			checkUncFolderEmpty(path);
		}
		boolean isSuccess = true;
		Map<Boolean, String> importResults = new HashMap<Boolean, String>();
		Map<String, Connections> dbExportConnections = null;
		Map<String, Connections> fuzzyConnections = null;
		try {
			ImportBatchService importService = this.getSingleBeanOfType(ImportBatchService.class);
			ImportBatchClassOptions optionXML = new ImportBatchClassOptions();
			Set<String> groupsToAssign = null;
			convertDTOtoXMLForImport(userOptions, optionXML);
			String firstRoleOfUser = null;
			groupsToAssign = getAllSuperAdminGroup();
			if (!isSuperAdmin()) {
				Set<String> userRoles = getUserRoles();
				if (userRoles != null) {
					for (String userRole : userRoles) {
						firstRoleOfUser = userRole;
						groupsToAssign.add(firstRoleOfUser);
						break;
					}
				}
			}
			ImportBatchClassResultCarrier importBatchClassResultCarrier = importService.importBatchClass(optionXML,
					userOptions.isWorkflowDeployed(), false, groupsToAssign);
			if (null != importBatchClassResultCarrier) {
				importResults = importBatchClassResultCarrier.getImportResults();
				dbExportConnections = importBatchClassResultCarrier.getDbExportConnections();
				fuzzyConnections = importBatchClassResultCarrier.getFuzzyConnections();
			}
		} catch (Exception e) {
			String errorMessg = "Error while importing." + e.getMessage();
			importResults.put(false, errorMessg);
			LOGGER.error(errorMessg, e);
		}
		isSuccess = !importResults.containsKey(false);
		if (isSuccess) {
			final String batchClassId = importResults.get(Boolean.TRUE);
			BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
			batchClassService.evict(batchClassService.getLoadedBatchClassByIdentifier(batchClassId));
			setDocumentConnections(dbExportConnections, fuzzyConnections, batchClassId);
			try {
				deployNewBatchClass(batchClassId, isGridWorkflow);
			} catch (DCMAException e) {
				throw new UIException(BatchClassMessages.AN_ERROR_OCCURED_WHILE_DEPLOYING_THE_WORKFLOW);
			}
		}
		return isSuccess;
	}

	private void setDocumentConnections(Map<String, Connections> dbExportConnections, Map<String, Connections> fuzzyConnections,
			String batchClassId) {
		if (!CollectionUtil.isEmpty(dbExportConnections) || !CollectionUtil.isEmpty(fuzzyConnections)) {
			LOGGER.info("Mapping Connections to documents.");
			BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
			BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchClassId);
			mapConnections(dbExportConnections, fuzzyConnections, batchClass);
			batchClassService.merge(batchClass);
		}
	}

	private void mapConnections(Map<String, Connections> dbExportConnections, Map<String, Connections> fuzzyConnections,
			BatchClass batchClass) {
		List<DocumentType> documentTypes = batchClass.getDocumentTypes();
		if (!CollectionUtil.isEmpty(documentTypes)) {
			ConnectionsService connectionsService = this.getSingleBeanOfType(ConnectionsService.class);
			for (DocumentType documentType : documentTypes) {
				if (!CollectionUtil.isEmpty(dbExportConnections)) {
					if (dbExportConnections.containsKey(documentType.getName())) {
						Connections dbExportConnection = dbExportConnections.get(documentType.getName());
						if (null != dbExportConnection) {
							Connections connectionToMap = getConnection(connectionsService, dbExportConnection);
							if (null != connectionToMap) {
								documentType.setDbExportConnection(connectionToMap);
							}
						}
					}
				}
				if (!CollectionUtil.isEmpty(fuzzyConnections)) {
					if (fuzzyConnections.containsKey(documentType.getName())) {
						Connections fuzzyConnection = fuzzyConnections.get(documentType.getName());
						if (null != fuzzyConnection) {
							Connections connectionToMap = getConnection(connectionsService, fuzzyConnection);
							if (null != connectionToMap) {
								documentType.setFuzzyConnection(connectionToMap);
							}
						}
					}
				}
			}
			// if (documentConnections.containsKey(documentType.getName())) {
			// DocumentTypeMappingConfig documentTypeMappingConfig = documentConnections.get(documentType.getName());
			// if (null != documentTypeMappingConfig) {
			// Connections dbExportConnection = documentTypeMappingConfig.getDatabaseExportConnections();
			// Connections fuzzyConnection = documentTypeMappingConfig.getFuzzyConnections();
			// if (null != dbExportConnection) {
			// Connections connectionToMap = getConnection(connectionsService, dbExportConnection);
			// if (null != connectionToMap) {
			// documentType.setDbExportConnection(connectionToMap);
			// }
			// }
			// if (null != fuzzyConnection) {
			// Connections connectionToMap = getConnection(connectionsService, fuzzyConnection);
			// if (null != connectionToMap) {
			// documentType.setFuzzyConnection(connectionToMap);
			// }
			// }
			// }
			// }

		}
	}

	private Connections getConnection(ConnectionsService connectionsService, Connections connection) {
		List<Connections> existingConnections = connectionsService.getAllConnectionsExcludingDeleted();
		Connections connectionToMap = null;
		for (Connections existingConnection : existingConnections) {
			if (null != connection) {
				if (existingConnection.getConnectionName().equalsIgnoreCase(connection.getConnectionName())) {
					connectionToMap = existingConnection;
					break;
				}
			}
		}
		return connectionToMap;
	}

	private void convertDTOtoXMLForImport(ImportBatchClassUserOptionDTO userOptions, ImportBatchClassOptions optionXML) {

		optionXML.setZipFilePath(userOptions.getZipFileName());
		optionXML.setUncFolder(userOptions.getUncFolder());
		optionXML.setUseExisting(userOptions.isImportExisting());
		optionXML.setUseSource(false);
		optionXML.setName(userOptions.getName());
		optionXML.setDescription(userOptions.getDescription());
		optionXML.setPriority(userOptions.getPriority());
		optionXML.setImportConnections(userOptions.isImportConnections());
		// update the configurations of imported batch class from DB / Zip
		if (userOptions.getUiConfigRoot() != null) {
			List<Node> leafNodes = new ArrayList<Node>();
			leafNodes = userOptions.getUiConfigRoot().getLeafNodes(leafNodes);
			List<ImportBatchClassOptions.BatchClassDefinition> bcdList = optionXML.getBatchClassDefinition();
			BatchClassDefinition bcd = new BatchClassDefinition();
			bcdList.add(bcd);

			Scripts scripts = new Scripts();
			bcd.setScripts(scripts);

			Folders folders = new Folders();
			bcd.setFolders(folders);

			com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules modules = new com.ephesoft.dcma.batch.schema.ImportBatchClassOptions.BatchClassDefinition.BatchClassModules();
			bcd.setBatchClassModules(modules);

			for (Node node : leafNodes) {
				// iterate over leaf nodes.
				if (node.getLabel() != null && node.getLabel().getKey().equalsIgnoreCase(BatchConstants.ROLES)) {
					optionXML.setRolesImported(BatchClassUtil.isChecked(node));
					continue;
				} else if (node.getLabel() != null && node.getLabel().getKey().equalsIgnoreCase(BatchConstants.EMAIL_ACCOUNTS)) {
					optionXML.setEmailAccounts(BatchClassUtil.isChecked(node));
					continue;
				} else if (node.getLabel() != null && node.getLabel().getKey().equalsIgnoreCase(BatchConstants.CMIS_PLUGIN)) {
					optionXML.setCmisPlugin(BatchClassUtil.isChecked(node));
					continue;
				} else if (node.getLabel() != null && node.getLabel().getKey().equalsIgnoreCase(BatchConstants.CMIS_PLUGIN_PROPERTIES)) {
					// check for CMIS Plugin Properties check box selected.
					optionXML.setCmisPluginProperties(BatchClassUtil.isChecked(node));
					continue;
				} else if (node.getLabel() != null && node.getLabel().getKey().equalsIgnoreCase(BatchConstants.CMIS_MAPPING)) {
					// check for CMIS Mapping check box selected.
					optionXML.setCmisMappings(BatchClassUtil.isChecked(node));
					continue;
				}
			}
		}

	}

	@Override
	public void updateBatchClassList(BatchClassListDTO batchClassLstDTO) throws UIException {
		Collection<BatchClassDTO> batchClassDTO = batchClassLstDTO.getBatchClasses();
		final BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		for (BatchClassDTO batchClassdto : batchClassDTO) {

			final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassdto.getIdentifier());
			if (null != batchClass) {
				try {
					if (!batchClass.getUncFolder().equalsIgnoreCase(batchClassdto.getUncFolder())) {
						checkUncFolderEmpty(batchClass.getUncFolder());
						boolean isUNCFolderUpdated = updateUNCFolderForBatchClass(batchClassdto.getUncFolder(),
								batchClassdto.getIdentifier());
						checkUncFolderEmpty(batchClassdto.getUncFolder());
						if (isUNCFolderUpdated) {
							batchClass.setUncFolder(batchClassdto.getUncFolder());
						}
					}
					batchClass.setPriority(batchClassdto.getPriority());
					batchClass.setDescription(batchClassdto.getDescription());
					BatchClassUtil.updateRevisionNumber(batchClass);
					batchClassdto.setVersion(batchClass.getVersion());

					// Update Roles in BatchClass
					if (batchClassdto.isDirty()) {
						BatchClassUtil.mergeRolesInBatchClass(batchClass, batchClassdto, getAllGroups(), getAllSuperAdminGroup());
					}

					batchClassService.saveOrUpdate(batchClass);
				} catch (UIArgumentException exc) {
					log.error(EphesoftStringUtil.concatenate("Exception while updating batch Classes information", exc.getMessage(),
							exc));
					throw exc;
				} catch (Exception ex) {
					log.error(EphesoftStringUtil.concatenate("Exception while updating batch Classes information", ex.getMessage(), ex));
					throw new UIException(ex.getMessage());
				}
			}
		}
	}

	private boolean updateUNCFolderForBatchClass(final String uncFldrPath, final String batchClassIdentifier)
			throws UIArgumentException {
		boolean isUpdated = false;
		final BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		if (null != batchClass) {
			final List<BatchInstance> listOfBatches = batchInstanceService.getAllUnFinishedBatchInstances(batchClass);
			if (null != listOfBatches && listOfBatches.size() > 0) {
				isUpdated = false;
			} else {
				for (BatchClass tempBatchClass : batchClassService.getAllBatchClasses()) {
					if (tempBatchClass.getUncFolder().equalsIgnoreCase(uncFldrPath)) {
						throw new UIArgumentException(BatchClassMessages.ERROR_CODE_2, batchClassIdentifier);
					}
				}
				final File uncFldr = new File(uncFldrPath);
				if (!uncFldr.exists()) {
					uncFldr.mkdirs();
				}
				isUpdated = true;
			}
		}

		return isUpdated;
	}

	@Override
	public List<String> getProjectFilesForDocumentType(String batchClassIdentifier, String documentTypeName) throws UIException {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		List<String> rspFileName = null;
		try {
			rspFileName = batchSchemaService.getProjectFilesForDocumentType(batchClassIdentifier, documentTypeName);
		} catch (Exception e) {
			throw new UIException(e.getMessage());
		}
		return rspFileName;
	}

	@Override
	public Map<ImportExportDocument, DocumentTypeDTO> importDocumentType(String tempImportDocLocation, boolean isLuceneImport,
			String batchClassIdentifier) throws UIException {

		// Fix for ISSUE Call to check if batch class is already deleted or unlocked (by super admin)
		// while current user save its changes.
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		BatchClass batchClass = batchClassService.get(batchClassIdentifier);
		checkIfBatchClassUnLockedBySuperUser(batchClassIdentifier, batchClass);

		ImportBatchService importBatchService = this.getSingleBeanOfType(ImportBatchService.class);
		Map<ImportExportDocument, DocumentType> documentTypeMap = importBatchService.importDocumentTypes(tempImportDocLocation,
				isLuceneImport, batchClassIdentifier);
		Map<ImportExportDocument, DocumentTypeDTO> documentDTOMap = new HashMap<ImportExportDocument, DocumentTypeDTO>();
		for (Entry<ImportExportDocument, DocumentType> entry : documentTypeMap.entrySet()) {
			if (null != entry) {
				if (null != entry.getValue()) {
					documentDTOMap.put(entry.getKey(), BatchClassUtil.createDocumentTypeDTO(null, entry.getValue()));
				} else {
					documentDTOMap.put(entry.getKey(), null);
				}
			}
		}
		return documentDTOMap;
	}

	@Override
	public boolean isDocTypeLimitExceed() {
		ImportBatchService importBatchService = this.getSingleBeanOfType(ImportBatchService.class);
		return importBatchService.isDocTypeLimitExceed();
	}

	@Override
	public Map<ImportExportIndexField, FieldTypeDTO> importIndexField(final String tempFolderPath, final DocumentTypeDTO documentType)
			throws UIException {
		List<FieldTypeDTO> indexFieldList = new ArrayList<FieldTypeDTO>();
		ImportBatchService importBatchService = this.getSingleBeanOfType(ImportBatchService.class);
		// DocumentType documentType = documentTypeService.getDocTypeByBatchClassAndDocTypeName(batchClassIdentifier,
		// documentTypeName);
		final Map<ImportExportIndexField, FieldTypeDTO> fieldTypeList = new HashMap<ImportExportIndexField, FieldTypeDTO>();
		int fieldOrderNumber = 1;
		for (FieldTypeDTO indexField : documentType.getFields()) {
			if (null != indexField && fieldOrderNumber < indexField.getFieldOrderNumber()) {
				fieldOrderNumber = indexField.getFieldOrderNumber();
			}
		}
		try {
			indexFieldList = new ArrayList<FieldTypeDTO>(documentType.getFields());
			// indexFieldList = fieldTypeService.getFdTypeByDocTypeNameForBatchClass(documentTypeName, batchClassIdentifier);
			final File tempUnZipDir = new File(tempFolderPath);
			String[] unZipDocDirList = null;
			if (tempUnZipDir.exists()) {
				unZipDocDirList = tempUnZipDir.list();
				for (int k = 0; k < unZipDocDirList.length; k++) {
					FieldTypeDTO deleteIndexField = null;
					final FieldTypeDTO importIndexField = BatchClassUtil.createFieldTypeDTO(documentType,
							importBatchService.deserializeIndexFieldObj(tempFolderPath + File.separator + unZipDocDirList[k]));
					BatchClassUtil.setFieldTypeAsNew(importIndexField);
					if (importIndexField != null) {
						deleteIndexField = checkFieldTypeExistance(importIndexField, indexFieldList);
						fieldOrderNumber = removeInsertIndexFieldImportDoc(deleteIndexField, importIndexField, documentType,
								fieldTypeList, fieldOrderNumber);

					} else {
						final ImportExportIndexField impExpDoc = new ImportExportIndexField("ERROR", false, false, true);
						fieldTypeList.put(impExpDoc, null);
						LOGGER.error("Error while deserializing the Index Field object in Dir: ");

					}
				}
			} else {
				LOGGER.error("UnZip Directory not exist while importing the Index Fields");
			}
		} catch (final Exception exception) {
			final String errorMessg = EphesoftStringUtil.concatenate("Error while importing. ", exception.getMessage());
			LOGGER.error(errorMessg, exception);
		}
		return fieldTypeList;
	}

	private FieldTypeDTO checkFieldTypeExistance(final FieldTypeDTO indexField, final List<FieldTypeDTO> fieldTypeList) {
		FieldTypeDTO deleteFieldType = null;
		for (int i = 0; i < fieldTypeList.size(); i++) {
			if (indexField.getName().equalsIgnoreCase(fieldTypeList.get(i).getName())) {
				deleteFieldType = fieldTypeList.get(i);
				break;
			}
		}
		return deleteFieldType;
	}

	private int removeInsertIndexFieldImportDoc(final FieldTypeDTO deleteFieldType, FieldTypeDTO importFieldType,
			final DocumentTypeDTO documentType, Map<ImportExportIndexField, FieldTypeDTO> fieldTypeList, int fieldOrderNumber) {
		ImportExportIndexField impExpDoc = null;
		if (deleteFieldType == null) {
			try {
				// setTableColumnInfosToColumnExtractionRules(importDocType.getTableInfos());
				fieldOrderNumber++;
				FieldTypeDTO newDocumentType = mergeFieldType(importFieldType, documentType, fieldOrderNumber);
				impExpDoc = new ImportExportIndexField(importFieldType.getName(), false, false, false);
				fieldTypeList.put(impExpDoc, newDocumentType);
			} catch (final Exception exception) {
				fieldOrderNumber--;
				final String errorMessg = EphesoftStringUtil.concatenate("Error while creating field type. ", exception.getMessage());
				impExpDoc = new ImportExportIndexField(importFieldType.getName(), false, true, false);
				fieldTypeList.put(impExpDoc, null);
				LOGGER.error(errorMessg, exception);
			}
		} else {
			impExpDoc = new ImportExportIndexField(importFieldType.getName(), true, false, false);
			fieldTypeList.put(impExpDoc, null);
		}
		return fieldOrderNumber;
	}

	private FieldTypeDTO mergeFieldType(FieldTypeDTO fieldType, final DocumentTypeDTO documentType, int fieldOrderNumber) {
		fieldType.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		fieldType.setFieldOrderNumber(fieldOrderNumber);
		fieldType.setDocTypeDTO(documentType);
		return fieldType;
	}

	@Override
	public List<ViewLearnFileDTO> getLearnFileDetails(String batchClassIdentifier, String documentType) throws UIException {
		String learnFilelocationFirstPage = getFileLocationPageType(getLearnFileLocation(batchClassIdentifier, documentType),
				documentType, FIRST_PAGE);
		String imageClassificationFirstPage = getFileLocationPageType(getImageFileLocation(batchClassIdentifier, documentType),
				documentType, FIRST_PAGE);

		List<ViewLearnFileDTO> fileWrapperList = getLearnedFiles(learnFilelocationFirstPage, imageClassificationFirstPage, "FIRST");
		String imageClassificationSecondPage = getFileLocationPageType(getImageFileLocation(batchClassIdentifier, documentType),
				documentType, MIDDLE_PAGE);
		String learnFilelocationSecondPage = getFileLocationPageType(getLearnFileLocation(batchClassIdentifier, documentType),
				documentType, MIDDLE_PAGE);
		List<ViewLearnFileDTO> fileWrapperMiddlePage = getLearnedFiles(learnFilelocationSecondPage, imageClassificationSecondPage,
				"MIDDLE");
		if (fileWrapperMiddlePage != null) {
			fileWrapperList.addAll(fileWrapperMiddlePage);
		}
		String learnFilelocationLastPage = getFileLocationPageType(getLearnFileLocation(batchClassIdentifier, documentType),
				documentType, LAST_PAGE);
		String imageClassificationLastPage = getFileLocationPageType(getImageFileLocation(batchClassIdentifier, documentType),
				documentType, LAST_PAGE);
		List<ViewLearnFileDTO> fileWrapperLastPage = getLearnedFiles(learnFilelocationLastPage, imageClassificationLastPage, "LAST");
		if (fileWrapperLastPage != null) {
			fileWrapperList.addAll(fileWrapperLastPage);
		}

		return fileWrapperList;

	}

	private List<ViewLearnFileDTO> getLearnedFiles(String learnFilelocation, String imageClassificationLocation, String pageType) {
		List<ViewLearnFileDTO> fileWrapperList = null;

		File learnFile = new File(learnFilelocation);
		File imageClassifyFile = new File(imageClassificationLocation);
		if (!learnFile.exists() && imageClassifyFile.exists()) {
			fileWrapperList = this.buildFilesList(null, imageClassificationLocation, imageClassifyFile.listFiles(), pageType);
		}
		if (learnFile.exists() && !imageClassifyFile.exists()) {
			fileWrapperList = this.buildFilesList(learnFile.listFiles(), imageClassificationLocation, null, pageType);
		}
		if (learnFile.exists() && imageClassifyFile.exists()) {
			fileWrapperList = this.buildFilesList(learnFile.listFiles(), imageClassificationLocation, imageClassifyFile.listFiles(),
					pageType);
		}

		return fileWrapperList;

	}

	private String getFileLocationPageType(String learnFileLocation, String documentType, String pageType) {

		learnFileLocation = EphesoftStringUtil.concatenate(learnFileLocation, File.separator, documentType + pageType);
		return learnFileLocation;
	}

	private String getLearnFileLocation(String batchClassIdentifier, String documentType) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String learnFileLocation = null;

		learnFileLocation = EphesoftStringUtil.concatenate(batchSchemaService.getBaseFolderLocation(), File.separator,
				batchClassIdentifier, File.separator, batchSchemaService.getSearchSampleName(), File.separator, documentType);

		return learnFileLocation;
	}

	private String getImageFileLocation(String batchClassIdentifier, String documentType) {
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		String imageClassifyLocation = null;

		imageClassifyLocation = EphesoftStringUtil
				.concatenate(batchSchemaService.getBaseFolderLocation(), File.separator, batchClassIdentifier, File.separator,
						batchSchemaService.getImagemagickBaseFolderName() + File.separator, documentType);

		return imageClassifyLocation;
	}

	private List<ViewLearnFileDTO> buildFilesList(File[] learnFiles, String imageClassifyLocation, File[] thumbFiles, String pageType) {
		List<ViewLearnFileDTO> result = new ArrayList<ViewLearnFileDTO>();
		String thumbFileLocation = imageClassifyLocation + File.separator + "thumbs";

		File thumbFile = new File(thumbFileLocation);
		File[] listthumbFiles = null;
		if (thumbFile.exists()) {
			listthumbFiles = thumbFile.listFiles();

		}
		ViewLearnFileDTO learnFileWrapper = new ViewLearnFileDTO();
		if ((learnFiles != null && learnFiles.length > 0) && (thumbFiles == null || thumbFiles.length == 0)) {
			for (File file : learnFiles) {
				if (file.getName().endsWith(".tif")) {
					learnFileWrapper = new ViewLearnFileDTO();

					String fileName = file.getName();
					boolean isLuceneLearned = isHOCRXMLExist(getHOCRXMLFileName(fileName), learnFiles);
					learnFileWrapper.setLuceneLearned(isLuceneLearned);
					learnFileWrapper.setImageClassified(false);
					learnFileWrapper.setFileName(fileName);
					if (fileName.indexOf(".") > 0) {
						learnFileWrapper.setPageNumber("000");
						learnFileWrapper.setPageType(pageType);
					}
					result.add(learnFileWrapper);
				}

			}
		} else if (learnFiles == null && thumbFiles != null) {
			learnFileWrapper = new ViewLearnFileDTO();
			boolean isImageClassification = false;
			for (File file : thumbFiles) {
				String fileName = file.getName();
				if (file.getName().endsWith(".tif")) {
					learnFileWrapper.setLuceneLearned(false);
					if (listthumbFiles != null) {
						isImageClassification = isImageClassificationThumbExist(fileName, listthumbFiles);
					}
					learnFileWrapper.setFileName(fileName);
					if (fileName.indexOf(".") > 0) {
						learnFileWrapper.setPageNumber("000");
						learnFileWrapper.setPageType(pageType);
					}
					result.add(learnFileWrapper);
				}
			}

		} else if (learnFiles != null && thumbFiles != null && thumbFiles.length > 1) {
			for (File file : learnFiles) {
				if (file.getName().endsWith(".tif")) {
					learnFileWrapper = new ViewLearnFileDTO();

					String fileName = file.getName();
					boolean isLuceneLearned = isHOCRXMLExist(getHOCRXMLFileName(fileName), learnFiles);
					learnFileWrapper.setLuceneLearned(isLuceneLearned);
					boolean isImageClassification = isImageClassificationThumbExist(fileName, listthumbFiles);
					learnFileWrapper.setImageClassified(isImageClassification);
					learnFileWrapper.setFileName(fileName);
					if (fileName.indexOf(".") > 0) {
						learnFileWrapper.setPageNumber("000");
						learnFileWrapper.setPageType(pageType);
					}
					result.add(learnFileWrapper);
				}
			}

		}
		return result;
	}

	private boolean isImageClassificationThumbExist(String name, File[] thumbFiles) {
		boolean isThumbFileExist = false;
		for (File thumbFile : thumbFiles) {
			if (name.concat("_thumb.tif").equalsIgnoreCase(thumbFile.getName())) {
				isThumbFileExist = true;
			}
		}
		return isThumbFileExist;
	}

	private String getHOCRXMLFileName(String fileName) {
		String hocrXMLFileName = null;
		if (null != fileName) {
			if (fileName.indexOf(".") > 0) {
				fileName = fileName.substring(0, fileName.lastIndexOf("."));
			}
			hocrXMLFileName = fileName.concat("_HOCR.xml");
		}
		return hocrXMLFileName;
	}

	private boolean isHOCRXMLExist(String hocrFileName, File[] hocrXMLList) {
		boolean isHOCRPresent = false;
		for (File hocrXML : hocrXMLList) {

			String hocrxmlFileName = hocrXML.getName();
			if (hocrxmlFileName.equalsIgnoreCase(hocrFileName)) {
				isHOCRPresent = true;
				break;
			}
		}
		return isHOCRPresent;
	}

	public void checkUncFolderEmpty(String path) throws UIException {

		if (!FileUtils.checkForEmptyDirectory(path)) {
			throw new UIException(BatchClassMessages.ERROR_CODE_1);
		}
	}

	public String getBatchClassIdentifierForBatchClassName(String batchClassName) {
		String returnValue = AdminConstants.EMPTY_STRING;
		if (!EphesoftStringUtil.isNullOrEmpty(batchClassName)) {
			BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
			BatchClass batchClass = batchClassService.getBatchClassbyName(batchClassName);
			if (null != batchClass) {
				returnValue = batchClass.getIdentifier();
			}
		}
		return returnValue;
	}

	@Override
	public void acquireLock(String batchClassIdentifier) throws UIException {
		BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		try {
			batchClassService.acquireBatchClass(batchClassIdentifier, getUserName());
		} catch (BatchAlreadyLockedException e) {
			throw new UIException(e.getMessage());
		}
	}

	@Override
	public List<CategorisedData> getBatchClassManagementPPMData(List<String> batchIdentifierList) {
		final List<CategorisedData> categorisedDataList = new ArrayList<CategorisedData>();
		final CategorisedData data1 = new CategorisedData();
		data1.setCategory("BC1");
		data1.setData(40);
		final CategorisedData data2 = new CategorisedData();
		data2.setCategory("BC2");
		data2.setData(20);
		final CategorisedData data3 = new CategorisedData();
		data3.setCategory("BC3");
		data3.setData(50);
		categorisedDataList.add(data1);
		categorisedDataList.add(data2);
		categorisedDataList.add(data3);
		return categorisedDataList;
	}

	@Override
	public String getAdvancedTEImageUploadPath(final String batchClassId, final String docName, final String imageName) {
		LOGGER.debug("Entering method getAdvancedTEImageUploadPath....");
		LOGGER.debug(EphesoftStringUtil.concatenate("batchClassId : ", batchClassId, ", docName : ", docName, ", imageName : ",
				imageName));
		String imageUrl = null;
		if (batchClassId != null && docName != null && imageName != null) {
			BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			imageUrl = EphesoftStringUtil.concatenate(batchSchemaService.getBaseHttpURL(), URL_SEPARATOR, batchClassId, URL_SEPARATOR,
					batchSchemaService.getAdvancedTestTableFolderName(), URL_SEPARATOR, EphesoftStringUtil.encode(docName),
					URL_SEPARATOR, EphesoftStringUtil.encode(imageName));
		}
		LOGGER.debug(EphesoftStringUtil.concatenate("Image url path : ", imageUrl));
		LOGGER.debug("Exiting method getAdvancedTEImageUploadPath....");
		return imageUrl;
	}

	@Override
	public List<TestTableResultDTO> testTablePattern(final BatchClassDTO batchClassDTO, final TableInfoDTO tableInfoDTO,
			final String ruleIndex) throws UIException {
		if (batchClassDTO == null) {
			LOGGER.info("Batch Class DTO is null.");
			throw new UIException("Test Table failure");
		}
		SearchClassificationService searchClassificationService = this.getSingleBeanOfType(SearchClassificationService.class);
		BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		TableFinderService tableFinderService = this.getSingleBeanOfType(TableFinderService.class);
		ImageProcessService imageProcessService = this.getSingleBeanOfType(ImageProcessService.class);
		BatchClassID batchClassID = new BatchClassID(batchClassDTO.getIdentifier());
		final List<TableInfo> inputDataCarrier = BatchClassUtil.mapTableInputFromDTO(tableInfoDTO, ruleIndex);
		// List<DataTable> dataTableDTOs = new ArrayList<DataTable>();
		List<TestTableResultDTO> results = new ArrayList<TestTableResultDTO>();
		List<TestTableResultDTO> noResultsDTOs = new ArrayList<TestTableResultDTO>();
		try {
			String testTableFolderPath = batchSchemaService.getTestTableFolderPath(batchClassID, true);
			String ocrEngineName = getOCRPluginNameForBatchClass(batchClassDTO.getIdentifier());
			BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
			final List<File> allImageFiles = imageProcessService.convertPdfOrMultiPageTiffToTiff(batchClassID, testTableFolderPath,
					null, false, batchInstanceThread);
			batchInstanceThread.execute();

			// delete the original files.
			if (allImageFiles != null) {
				for (File imageFile : allImageFiles) {
					imageFile.delete();
				}
			}
			searchClassificationService.generateHOCRForKVExtractionTest(testTableFolderPath, ocrEngineName,
					batchClassDTO.getIdentifier(), null, false);
			Map<String, String> imageToOCRFilePath = new HashMap<String, String>();
			File inputFolder = new File(testTableFolderPath);
			File[] listOfimages = inputFolder.listFiles(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(), FileType.TIFF
					.getExtensionWithDot(), FileType.PNG.getExtensionWithDot()));
			for (File file : listOfimages) {
				if (file.exists()) {
					String filePath = file.getPath();
					String ocrFilePath = EphesoftStringUtil.concatenate(filePath.substring(0, filePath.lastIndexOf(".")), "_HOCR.xml");
					if (new File(ocrFilePath).exists()) {
						imageToOCRFilePath.put(filePath, ocrFilePath);
					}
				}
			}
			File file = new File(testTableFolderPath + File.separator + "tempfile");
			if (!CollectionUtil.isEmpty(imageToOCRFilePath)) {
				for (String imagePath : imageToOCRFilePath.keySet()) {
					File imageFile = new File(imagePath);
					String ocrFilePath = imageToOCRFilePath.get(imagePath);
					HocrPages hocrPages = batchSchemaService.getHOCR(ocrFilePath);
					if (hocrPages != null) {
						HocrPage hocrPage = hocrPages.getHocrPage().get(0);
						DataTable dataTable = tableFinderService.findTableData(inputDataCarrier, hocrPage, Integer.MAX_VALUE);
						BatchClassUtil.mapTestTableResultsToDTO(results, noResultsDTOs, dataTable, imageFile.getName());
					}
				}
			}
			results.addAll(noResultsDTOs);
			boolean isTempFileDeleted = file.delete();
			if (!isTempFileDeleted) {
				file.delete();
			}

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new UIException(e.getMessage());

		}
		return results;
	}

	@Override
	public Map<String, List<String>> getAllTables(final ConnectionsDTO connection) throws Exception {
		Map<String, List<String>> tableNames = null;
		if (null != connection) {
			tableNames = getAllTables(ConnectionType.getDriver(connection.getDatabaseType()), connection.getConnectionURL(),
					connection.getUserName(), connection.getDecryptedPassword());
		}
		return tableNames;
	}

	private Map<String, List<String>> getAllTables(String driverName, String dbConnectionURL, String userName, String password)
			throws Exception {
		Map<String, List<String>> tableNames = null;
		String dbPassword = EphesoftEncryptionUtil.getDecryptedPasswordString(password);
		DynamicHibernateDao dao = HibernateDaoUtil.createHibernateDaoConnection(driverName, dbConnectionURL, userName, dbPassword);
		tableNames = dao.getAllTableNames();
		dao.closeSession();
		return tableNames;
	}

	@Override
	public Map<String, String> getAllColumnsForTable(final ConnectionsDTO connection, final String tableName) throws Exception {
		Map<String, String> tablesMap = null;
		if (null != connection) {
			tablesMap = getAllColumnsForTable(ConnectionType.getDriver(connection.getDatabaseType()), connection.getConnectionURL(),
					connection.getUserName(), connection.getDecryptedPassword(), tableName);
		}
		return tablesMap;
	}

	private Map<String, String> getAllColumnsForTable(String driverName, String dbConnectionURL, String userName, String password,
			String tableName) throws Exception {
		String dbPassword = EphesoftEncryptionUtil.getDecryptedPasswordString(password);
		DynamicHibernateDao dao = HibernateDaoUtil.createHibernateDaoConnection(driverName, dbConnectionURL, userName, dbPassword);
		List<ColumnDefinition> columnNames = dao.getAllColumnsForTable(tableName);
		Map<String, String> map = new HashMap<String, String>();
		for (ColumnDefinition columnDefinition : columnNames) {
			map.put(columnDefinition.getColumnName(), columnDefinition.getType().getName());
		}
		dao.closeSession();
		return map;
	}

	/**
	 * Finds all the matches inside the string for the given regex pattern and returns
	 * 
	 * the list of matched indexes.
	 * 
	 * @param regex {@link String} The regex pattern generated.
	 * @param strToBeMatched {@link String} The string which is to be matched.
	 * @return {@link List<{@link String}> The list of matched indexes.
	 * @throws Exception if any exception or error occur.
	 */
	@Override
	public List<String> findMatchedIndexesList(final String regex, final String

	strToBeMatched) throws UIException {
		List<String> matchedIndexList = null;
		try {
			if (!StringUtil.isNullOrEmpty(regex) && strToBeMatched != null) {
				matchedIndexList = new ArrayList<String>();
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(strToBeMatched);
				while (matcher.find()) {
					matchedIndexList.add(String.valueOf(matcher.start

					()));
					matchedIndexList.add(String.valueOf(matcher.end

					()));
				}
			}
		} catch (final PatternSyntaxException patternSyntaxException) {
			throw new UIException("Invalid Regex Pattern.");
		}
		return matchedIndexList;
	}

	/**
	 * used for getting the map of avaliable REgex groups from DB
	 * 
	 * @return {@link Map<String, RegexGroupDTO>}
	 */
	@Override
	public Map<String, RegexGroupDTO> getRegexGroupMap() {
		Map<String, RegexGroupDTO> regexGroupDTOMap = new LinkedHashMap<String,

		RegexGroupDTO>();
		List<RegexGroupDTO> listOfGroupDTOs = getRegexGroupList();
		for (RegexGroupDTO groupDTO : listOfGroupDTOs) {
			if (null != groupDTO) {
				regexGroupDTOMap.put(groupDTO.getIdentifier(), groupDTO);
			}
		}
		return regexGroupDTOMap;

	}

	/**
	 * used for getting the list of avaliable REgex groups from DB
	 * 
	 * @return {@link list<RegexGroupDTO>}
	 */
	private List<RegexGroupDTO> getRegexGroupList() {
		List<RegexGroupDTO> regexGroupDTOList = null;
		RegexGroupService regexGroupService = this.getSingleBeanOfType

		(RegexGroupService.class);
		final List<RegexGroup> regexGroupList = regexGroupService.getRegexGroups

		();
		if (regexGroupList == null) {
			LOGGER.error("Regex group List selection failed.");
		} else {
			regexGroupDTOList = new ArrayList<RegexGroupDTO>

			(regexGroupList.size());
			for (RegexGroup regexGroup : regexGroupList) {
				if (regexGroup != null) {
					final RegexGroupDTO regexGroupDTO =

					RegexUtil.convertRegexGroupToRegexGroupDTO(regexGroup);
					if (regexGroupDTO == null) {
						continue;
					}
					regexGroupDTOList.add(regexGroupDTO);
				}
			}
		}
		return regexGroupDTOList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.BatchClassManagementService#getCmisConfiguration(java.util.Collection)
	 */
	@Override
	public Map<String, String> getCmisConfiguration(final Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues)
			throws UIException {
		Map<String, String> tokenMap = null;
		return tokenMap;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.BatchClassManagementService#getAuthenticationURL(java.util.Collection)
	 */
	@Override
	public String getAuthenticationURL(final Collection<BatchClassPluginConfigDTO> pluginConfigDTOValues) throws UIException {
		String authenticationURL = null;
		return authenticationURL;
	}

	@Override
	public List<String> getAllPrimaryKeysForTable(String driverName, String dbConnectionURL, String userName, String password,
			String table, String tableType) throws UIException {
		List<String> keyList = null;
		LOGGER.debug("Getting primary keys for the table : " + table + " of type " + tableType);
		LOGGER.debug("Connecting to the database using the following settings: ");
		LOGGER.debug("Driver name: " + driverName);
		LOGGER.debug("URL: " + dbConnectionURL);
		LOGGER.debug("User Name: " + userName);

		String dbPassword = EphesoftEncryptionUtil.getDecryptedPasswordString(password);
		DynamicHibernateDao dao = null;
		try {
			dao = HibernateDaoUtil.createHibernateDaoConnection(driverName, dbConnectionURL, userName, dbPassword);
			keyList = dao.getPrimaryKeysForTable(table, tableType);
		} catch (DCMAException dcmaException) {
			LOGGER.error(
					EphesoftStringUtil.concatenate("Error occurred while getting primary keys for table : ", table,
							dcmaException.getMessage()), dcmaException);
			throw new UIException(dcmaException.getMessage());
		} catch (SQLException sqlException) {
			LOGGER.error(
					EphesoftStringUtil.concatenate("Error occurred while getting primary keys for table : ", table,
							sqlException.getMessage()), sqlException);
			throw new UIException(sqlException.getMessage());
		} finally {
			if (null != dao) {
				dao.closeSession();
			}
		}
		LOGGER.info("Primary keys found are: " + keyList.toString());
		return keyList;
	}

	@Override
	public List<String> getAllPrimaryKeysForTable(ConnectionsDTO connectionDTO, String table, String tableType) throws UIException {
		List<String> allPrimaryKeys = null;
		if (!(null == connectionDTO && EphesoftStringUtil.isNullOrEmpty(table) && EphesoftStringUtil.isNullOrEmpty(tableType))) {
			String driverName = ConnectionType.getDriver(connectionDTO.getDatabaseType());
			allPrimaryKeys = getAllPrimaryKeysForTable(driverName, connectionDTO.getConnectionURL(), connectionDTO.getUserName(),
					connectionDTO.getDecryptedPassword(), table, tableType);
		}
		return allPrimaryKeys;
	}

	@Override
	public List<RoleDTO> getAllRoles() {
		Set<String> allGroups = getAllGroups();
		Set<String> superAdminGroups = getAllSuperAdminGroup();
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();

		// NULL check added after SSO changes
		if (null != allGroups && null != superAdminGroups) {
			for (String group : allGroups) {
				if (!superAdminGroups.contains(group)) {
					RoleDTO roleDTO = new RoleDTO();
					roleDTO.setName(group);
					roleDTOs.add(roleDTO);
				}
			}
		}
		return roleDTOs;
	}

}
