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

package com.ephesoft.gxt.uploadbatch.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SerializationUtils;

import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.EphesoftUser;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.UserType;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassCloudConfig;
import com.ephesoft.dcma.da.domain.BatchClassField;
import com.ephesoft.dcma.da.service.BatchClassCloudConfigService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.IUtilCommonConstants;
import com.ephesoft.dcma.util.PDFUtil;
import com.ephesoft.dcma.util.TIFFUtil;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.server.DCMARemoteServiceServlet;
import com.ephesoft.gxt.core.shared.dto.BatchClassCloudConfigDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassFieldDTO;
import com.ephesoft.gxt.core.shared.dto.UploadBatchDTO;
import com.ephesoft.gxt.core.shared.exception.UIException;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.uploadbatch.client.UploadBatchService;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchConstants;

public class UploadBatchServiceImpl extends DCMARemoteServiceServlet implements UploadBatchService {

	private static final long serialVersionUID = 1L;
	private static final String SERIALIZATION_EXT = FileType.SER.getExtensionWithDot();
	private static final String BCF_SER_FILE_NAME = "BCF_ASSO";
	private static final String BID_SER_FILE_NAME = "BID_ASSO";

	/**
	 * The UPLOAD_BATCH_LIMIT {@link String} is a constant for 'upload_batch_limit' defined in application-config properties file.
	 */
	private static final String UPLOAD_BATCH_LIMIT = "upload_batch_limit";
	
	/**
	 * The BATCH_NAME_DATE_FORMAT {@link String} is a constant for 'batch_name_date_format' defined in application-config properties file for defining date format appended in batch name.
	 */
	private static final String BATCH_NAME_DATE_FORMAT = "batch_name_date_format";
	
	/**
	 * The DEFAULT_FILE_SIZE_LIMIT is the default file size limit for upload batch for Freemium User type.
	 * 
	 * @return default file size limit
	 */
	private static final long DEFAULT_FILE_SIZE_LIMIT = 1024L;

	@Override
	public int getRowsCount() {
		final List<BatchInstanceStatus> statusList = new ArrayList<BatchInstanceStatus>();
		statusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		statusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);

		final BatchInstanceService batchInstanceService = this.getSingleBeanOfType(BatchInstanceService.class);
		return batchInstanceService.getCount(statusList, null, getUserRoles(), getUserName(), EphesoftUser.NORMAL_USER);
	}

	@Override
	public String finishBatch(final String currentBatchUploadFolder, final String batchClassID, final String batchName,
			final String batchDescription) throws UIException {
		if (currentBatchUploadFolder.equals(batchDescription)) {
			log.info("Batch Name and Batch Description are the same. Batch Description serialized file will not be created.");
		} else {
			serializeBatchInstanceDescription(currentBatchUploadFolder, batchDescription);
		}
		final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		boolean folderRenamed = renameUploadFolder(batchSchemaService, currentBatchUploadFolder, batchName);
		final Integer userType = getUserType();
		String errorMessage = null;
		try {

			// Check for batch class limit for the current user group
			errorMessage = checkForBatchClassLimit(userType, batchClassID, currentBatchUploadFolder,
					batchSchemaService.getUploadBatchFolder());
			if (EphesoftStringUtil.isNullOrEmpty(errorMessage) && folderRenamed) {

				// CustomFileFilter fileFilter = new CustomFileFilter(true, FileType.TIF.getExtensionWithDot(),
				// FileType.TIFF.getExtensionWithDot(), FileType.SER.getExtensionWithDot(), FileType.PDF.getExtensionWithDot());
				batchSchemaService.copyFolder(batchSchemaService.getUploadBatchFolder(), batchName, batchClassID);

				updateBatchClassCounter(userType, batchClassID);
			} else {

				// Throw batch class limit exception
				throw new UIException(errorMessage);
			}
		} catch (final Exception e) {
			throw new UIException(e.getMessage());
		}
		return currentBatchUploadFolder;
	}

	private boolean renameUploadFolder(final BatchSchemaService batchSchemaService, final String currentBatchUploadFolder,
			final String batchName) {
		boolean isRenameSuccess = false;
		if (null != batchSchemaService){
			if (!StringUtil.isNullOrEmpty(currentBatchUploadFolder) && !StringUtil.isNullOrEmpty(batchName)) {
				final String uploadFolderBasePath = batchSchemaService.getUploadBatchFolder();
				File uploadFolder = new File(EphesoftStringUtil.concatenate(uploadFolderBasePath, File.separator,
						currentBatchUploadFolder));
				if (uploadFolder.exists()) {
					File batchFolder = new File(EphesoftStringUtil.concatenate(uploadFolderBasePath, File.separator, batchName));
					try {
						uploadFolder.renameTo(batchFolder);
						isRenameSuccess = true;
					} catch (SecurityException securityException){
						isRenameSuccess = false;
						log.error("Unable to rename upload folder name.", securityException);
					}
				}
			}
		}
		return isRenameSuccess;
	}
	/**
	 * Returns BatchClass id with it's description.
	 * 
	 * @param onBasisOfSorting {@link Boolean}- if <code>true</code>, sorting is done on batch class description else on batch class id
	 * @return {@link Map}< {@link String}, {@link String}>
	 */
	@Override
	public Map<String, String> getBatchClassName(final boolean onBasisOfSorting) {
		Map<String, String> batchClassMap = null;
		final BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		final Set<String> allGroups = getUserRoles();
		List<BatchClass> batchClassList = null;
		if (null != allGroups) {
			if (onBasisOfSorting) {
				batchClassList = batchClassService.getBatchClassesSortedOnDescription(allGroups);
			} else {
				batchClassList = batchClassService.getAllBatchClassesByUserRoles(allGroups);
			}
			if (null != batchClassList) {
				String identifier = null;
				String description = null;
				batchClassMap = new LinkedHashMap<String, String>(batchClassList.size());
				for (final BatchClass batchClass : batchClassList) {
					identifier = batchClass.getIdentifier();
					description = batchClass.getDescription();
					if (description.length() > 30) {
						description = description.substring(0, 30);
					}
					batchClassMap.put(EphesoftStringUtil.concatenate(identifier, " - ", description), identifier);
				}
			}
		}
		return batchClassMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.dcma.gwt.uploadbatch.client.UploadBatchService#getBatchClassImageLimit()
	 */
	@Override
	public Map<String, BatchClassCloudConfigDTO> getBatchClassImageLimit() {
		final Map<String, BatchClassCloudConfigDTO> map = new LinkedHashMap<String, BatchClassCloudConfigDTO>();
		final BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
		final BatchClassCloudConfigService batchClassCloudService = this.getSingleBeanOfType(BatchClassCloudConfigService.class);
		final Set<String> allGroups = getUserRoles();
		final Integer userType = getUserType();
		if (null != allGroups) {
			final List<BatchClass> batchClassList = batchClassService.getAllBatchClassesByUserRoles(allGroups);

			if (null != batchClassList) {
				for (final BatchClass batchClass : batchClassList) {
					final String identifier = batchClass.getIdentifier();
					BatchClassCloudConfigDTO batchClassConfigDTO = null;
					final BatchClassCloudConfig batchClassCloudConfig = batchClassCloudService
							.getBatchClassCloudConfigByBatchClassIdentifier(identifier);
					if (null != batchClassCloudConfig && userType.intValue() == UserType.LIMITED.getUserType()) {
						batchClassConfigDTO = new BatchClassCloudConfigDTO();
						batchClassConfigDTO.setBatchInstanceCounter(batchClassCloudConfig.getCurrentCounter());
						batchClassConfigDTO.setBatchInstanceImageLimit(batchClassCloudConfig.getPageCount());
						batchClassConfigDTO.setBatchInstanceLimit(batchClassCloudConfig.getBatchInstanceLimit());
					}
					map.put(identifier, batchClassConfigDTO);
				}
			}
		}
		return map;
	}

	/**
	 * This method is used to fetch the BatchClassFieldDTO on the basis of BatchClassIdentifier
	 * 
	 * @param identifier
	 * @return ArrayList<BatchClassFieldDTO>
	 */

	@Override
	public List<BatchClassFieldDTO> getBatchClassFieldDTOByBatchClassIdentifier(final String identifier) {
		BatchClass batchClass = null;
		final ArrayList<BatchClassFieldDTO> arrayList = new ArrayList<BatchClassFieldDTO>();
		if (identifier != null) {
			final BatchClassService batchClassService = this.getSingleBeanOfType(BatchClassService.class);
			batchClass = batchClassService.getBatchClassByIdentifier(identifier);
			final BatchClassDTO batchClassDTO = new BatchClassDTO();
			batchClassDTO.setIdentifier(batchClass.getIdentifier());
			batchClassDTO.setDescription(batchClass.getDescription());
			batchClassDTO.setName(batchClass.getName());
			batchClassDTO.setPriority(batchClass.getPriority());
			batchClassDTO.setUncFolder(batchClass.getUncFolder());
			batchClassDTO.setVersion(batchClass.getVersion());
			batchClassDTO.setDeleted(batchClass.isDeleted());
			for (final BatchClassField batchClassField : batchClass.getBatchClassField()) {
				final BatchClassFieldDTO batchClassFieldDTO = createBatchClassFieldDTO(batchClassDTO, batchClassField);
				batchClassDTO.addBatchClassField(batchClassFieldDTO);
			}
			for (final BatchClassFieldDTO batchClassFieldDTO : batchClassDTO.getBatchClassField()) {
				arrayList.add(batchClassFieldDTO);
			}
		}
		return arrayList;
	}

	/**
	 * This method is used to create the BatchClassFieldDTO on the basis of BatchClassDTO and BatchClassField
	 * 
	 * @param batchClassDTO
	 * @param batchClassField
	 * @return BatchClassFieldDTO
	 */
	private BatchClassFieldDTO createBatchClassFieldDTO(final BatchClassDTO batchClassDTO, final BatchClassField batchClassField) {
		final BatchClassFieldDTO batchClassFieldDTO = new BatchClassFieldDTO();
		batchClassFieldDTO.setBatchClass(batchClassDTO);
		batchClassFieldDTO.setDataType(batchClassField.getDataType());
		batchClassFieldDTO.setIdentifier(batchClassField.getIdentifier());
		batchClassFieldDTO.setName(batchClassField.getName());
		batchClassFieldDTO.setFieldOrderNumber(batchClassField.getFieldOrderNumber());
		batchClassFieldDTO.setDescription(batchClassField.getDescription());
		batchClassFieldDTO.setValidationPattern(batchClassField.getValidationPattern());
		batchClassFieldDTO.setSampleValue(batchClassField.getSampleValue());
		batchClassFieldDTO.setFieldOptionValueList(batchClassField.getFieldOptionValueList());
		batchClassFieldDTO.setValue(batchClassField.getValue());
		return batchClassFieldDTO;
	}

	/**
	 * This method is used to serialize the BatchClassField that is to be used in Web scanner module
	 * 
	 * @param folderName
	 * @param values
	 */

	@Override
	public void serializeBatchClassField(final String folderName, final List<BatchClassFieldDTO> values) throws UIException {
		FileOutputStream fileOutputStream = null;
		File serializedExportFile = null;
		final ArrayList<BatchClassField> batchClassFieldList = new ArrayList<BatchClassField>();
		for (final BatchClassFieldDTO batchClassFieldDTO : values) {
			final BatchClassField batchClassField = new BatchClassField();
			batchClassField.setBatchClass(null);
			batchClassField.setDataType(batchClassFieldDTO.getDataType());
			batchClassField.setIdentifier(batchClassFieldDTO.getIdentifier());
			batchClassField.setName(batchClassFieldDTO.getName());
			batchClassField.setFieldOrderNumber(batchClassFieldDTO.getFieldOrderNumber());
			batchClassField.setDescription(batchClassFieldDTO.getDescription());
			batchClassField.setValidationPattern(batchClassFieldDTO.getValidationPattern());
			batchClassField.setSampleValue(batchClassFieldDTO.getSampleValue());
			batchClassField.setFieldOptionValueList(batchClassFieldDTO.getFieldOptionValueList());
			batchClassField.setValue(batchClassFieldDTO.getValue());
			batchClassFieldList.add(batchClassField);
		}
		try {
			final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			final String folderPath = batchSchemaService.getUploadBatchFolder() + File.separator + folderName;
			final File currentBatchUploadFolder = new File(folderPath);
			if (!currentBatchUploadFolder.exists()) {
				currentBatchUploadFolder.mkdirs();
			}
			serializedExportFile = new File(folderPath + File.separator + BCF_SER_FILE_NAME + SERIALIZATION_EXT);
			fileOutputStream = new FileOutputStream(serializedExportFile);
			SerializationUtils.serialize(batchClassFieldList, fileOutputStream);
		} catch (final FileNotFoundException e) {
			// Unable to read serializable file
			log.info("Error occurred while creating the serializable file." + e, e);
			throw new UIException(e.getMessage());
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}

			} catch (final Exception e) {
				if (serializedExportFile != null)
					log.error("Problem closing stream for file :" + serializedExportFile.getName());
			}
		}

	}

	@Override
	public void resetCurrentBatchUploadFolder(final String folderName) {
		final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		final String folderPath = batchSchemaService.getUploadBatchFolder() + File.separator + folderName;
		final File currentBatchUploadFolder = new File(folderPath);
		if (currentBatchUploadFolder.exists()) {
			try {
				FileUtils.deleteDirectory(currentBatchUploadFolder);
				if (currentBatchUploadFolder.exists()) {
					FileUtils.deleteDirectory(currentBatchUploadFolder);
				}
			} catch (final IOException e) {
				log.info("Error while cleaning up last upload batch folder. Folder name:" + folderPath);
			}
		}
	}

	@Override
	public List<String> deleteFilesByName(final String folderName, final List<String> fileNames) {
		final List<String> filesNotDeleted = new ArrayList<String>();
		final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
		final String folderPath = batchSchemaService.getUploadBatchFolder() + File.separator + folderName;
		for (final String fileName : fileNames) {
			final File currentFile = new File(folderPath + File.separator + fileName);
			if (currentFile.exists()) {
				try {
					currentFile.delete();
					if (currentFile.exists()) {
						currentFile.delete();
					}
				} catch (final Exception e) {
					filesNotDeleted.add(fileName);
					log.info("Error while deleting " + fileName + "file from the Folder:" + folderPath);
				}
			}
			if (currentFile.exists()) {
				// add to the list if file not deleted
				filesNotDeleted.add(fileName);
			}
		}
		return filesNotDeleted;
	}

	@Override
	public String getCurrentBatchFolderName() {
		String dateFormat;
		try {
			dateFormat = ApplicationConfigProperties
					.getApplicationConfigProperties().getProperty(
							BATCH_NAME_DATE_FORMAT);
		} catch (IOException iOException) {
			dateFormat = UploadBatchConstants.DATE_FORMAT;
			log.error("Property batch_name_date_format is missing from property file.");
		} catch (Exception exception) {
			dateFormat = UploadBatchConstants.DATE_FORMAT;
		}
		SimpleDateFormat simpleDateFormat = null;
		String formattedDate;
		try {
			simpleDateFormat = new SimpleDateFormat(dateFormat.trim());
		} catch (IllegalArgumentException illegalArgumentException) {
			simpleDateFormat = new SimpleDateFormat(
					UploadBatchConstants.DATE_FORMAT);
		} catch (Exception exception) {
			simpleDateFormat = new SimpleDateFormat(
					UploadBatchConstants.DATE_FORMAT);
		} finally {
			formattedDate = simpleDateFormat.format(new Date());
		}
		formattedDate = formattedDate.replaceAll(UploadBatchConstants.INVALID_CHARACTER_LIST,
				UploadBatchConstants.MODIFIED_FILE_NAME_SEPERATOR);
		final String folderName = EphesoftStringUtil.concatenate(getUserName(),CoreCommonConstants.UNDERSCORE,formattedDate);
		return folderName ; 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.dcma.gwt.uploadbatch.client.UploadBatchService#getFileSizeLimit()
	 */
	public Long getFileSizeLimit() {
		Long fileSizeLimit = null;
		try {
			fileSizeLimit = Long.parseLong(ApplicationConfigProperties.getApplicationConfigProperties()
					.getProperty(UPLOAD_BATCH_LIMIT));
		} catch (final NumberFormatException e) {
			fileSizeLimit = DEFAULT_FILE_SIZE_LIMIT;
			log.error("Format of property file_size_limit is wrong.");
		} catch (final IOException e) {
			fileSizeLimit = DEFAULT_FILE_SIZE_LIMIT;
			log.error("Property file_size_limit is missing from property file.");
		}
		return fileSizeLimit;
	}

	/**
	 * The <code>updateBatchClassCounter</code> method is used for updating the batch class instance counter.
	 * 
	 * @param userType {@link Integer} Ephesoft Cloud user type
	 * @param batchClassID {@link String} current selected batch class identifier
	 */
	private void updateBatchClassCounter(final Integer userType, final String batchClassID) {

		// Check for FREEMIUM user type
		if (userType.intValue() == UserType.LIMITED.getUserType()) {
			final BatchClassCloudConfigService batchClassCloudService = this.getSingleBeanOfType(BatchClassCloudConfigService.class);
			final BatchClassCloudConfig batchClassCloudConfig = batchClassCloudService
					.getBatchClassCloudConfigByBatchClassIdentifier(batchClassID);
			if (null != batchClassCloudConfig) {
				final Integer batchInstanceCount = batchClassCloudConfig.getBatchInstanceLimit();
				Integer currentCounter = batchClassCloudConfig.getCurrentCounter();

				// Check for batch class limit and update the counter
				if (null != currentCounter && null != batchInstanceCount && ++currentCounter <= batchInstanceCount) {
					batchClassCloudConfig.setCurrentCounter(currentCounter);
					batchClassCloudService.updateBatchClassCloudConfig(batchClassCloudConfig);
				}
			}
		}
	}

	/**
	 * The <code>checkForBatchClassLimit</code> method is used for checking batch class instance limit for a given user.
	 * 
	 * @param userType {@link Integer} Ephesoft Cloud user type
	 * @param batchClassID {@link String} current selected batch class identifier
	 * @param currentBatchUploadFolder {@link String} temporary upload folder path
	 * @param sourcePath {@link String} batch class source path
	 * @return {@link String} error message
	 */
	private String checkForBatchClassLimit(final Integer userType, final String batchClassID, final String currentBatchUploadFolder,
			final String sourcePath) {
		String errorMessage = null;

		// Check for FREEMIUM user type
		if (userType.intValue() == UserType.LIMITED.getUserType()) {
			final String finalPath = sourcePath + File.separator + currentBatchUploadFolder;
			final BatchClassCloudConfigService batchClassCloudService = this.getSingleBeanOfType(BatchClassCloudConfigService.class);
			final BatchClassCloudConfig batchClassCloudConfig = batchClassCloudService
					.getBatchClassCloudConfigByBatchClassIdentifier(batchClassID);
			if (null != batchClassCloudConfig) {
				final Integer pageCount = batchClassCloudConfig.getPageCount();
				final Integer batchInstanceLimit = batchClassCloudConfig.getBatchInstanceLimit();
				Integer currentCounter = batchClassCloudConfig.getCurrentCounter();

				// Check for batch class instance limit
				final boolean isBatchLimit = (null != batchInstanceLimit && null != currentCounter && ++currentCounter <= batchInstanceLimit);

				// If batch instance limit is not reached
				if (isBatchLimit) {

					// Check for batch instance page count limit
					final boolean isPageLimit = checkForPageLimit(finalPath, pageCount);
					if (isPageLimit) {
						errorMessage = UploadBatchConstants.IMAGE_ERROR;
					}
				} else {
					errorMessage = UploadBatchConstants.INSTANCE_ERROR;
					com.ephesoft.dcma.util.FileUtils.deleteDirectoryAndContentsRecursive(new File(finalPath), true);

				}
			}
		}
		return errorMessage;
	}

	/**
	 * The <code>checkForPageLimit</code> method is used to check the page count limit for a batch class instance.
	 * 
	 * @param currentBatchUploadFolder {@link String} current folder name of upload batch
	 * @param pageCount {@link Integer} current page count limit
	 * @return true/false indicating page limit crossed or not
	 */
	private boolean checkForPageLimit(final String currentBatchUploadFolder, final Integer pageCount) {
		boolean isPageError = false;
		if (null != pageCount && null != currentBatchUploadFolder) {
			final File uploadDir = new File(currentBatchUploadFolder);
			int currentCount = 0;

			// Iterate through upload files stored in temporary upload folder
			for (final File file : uploadDir.listFiles()) {
				final String fileName = file.getName();
				final String filePath = file.getAbsolutePath();

				// Get page count for multi-page tiff or pdf file.
				final int pageCountTemp = fileName.endsWith(IUtilCommonConstants.EXTENSION_PDF) ? PDFUtil.getPDFPageCount(filePath)
						: TIFFUtil.getTIFFPageCount(filePath);
				currentCount += pageCountTemp;

				// If page count limit is crossed
				if (currentCount > pageCount) {
					isPageError = true;
					break;
				}
			}
		}
		return isPageError;
	}

	@Override
	public void deleteSelectedFiles(final String currentBatchUploadFolder, final List<UploadBatchDTO> selectedFiles) throws UIException {
		if (!CollectionUtil.isEmpty(selectedFiles) && !EphesoftStringUtil.isNullOrEmpty(currentBatchUploadFolder)) {
			final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			final String uploadedFilesParentFolderPath = batchSchemaService.getUploadBatchFolder();
			final File tempUploadedFolder = new File(EphesoftStringUtil.concatenate(uploadedFilesParentFolderPath, File.separator,
					currentBatchUploadFolder));
			if (tempUploadedFolder.exists()) {
				for (final File currentFile : tempUploadedFolder.listFiles()) {
					final String currentFileName = currentFile.getName();
					for (final UploadBatchDTO selectedFile : selectedFiles) {
						if (selectedFile.getFileName().equalsIgnoreCase(currentFileName)) {
							try {
								currentFile.delete();
							} catch (final SecurityException securityException) {
								log.error(EphesoftStringUtil.concatenate("Unable to delete file ", currentFileName));
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void deleteAllFiles(final String currentBatchUploadFolder) throws UIException {
		if (!EphesoftStringUtil.isNullOrEmpty(currentBatchUploadFolder)) {
			final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
			final String uploadedFilesParentFolderPath = batchSchemaService.getUploadBatchFolder();
			final File tempUploadedFolder = new File(EphesoftStringUtil.concatenate(uploadedFilesParentFolderPath, File.separator,
					currentBatchUploadFolder));
			if (tempUploadedFolder.exists()) {
				try {
					FileUtils.deleteDirectory(tempUploadedFolder);
				} catch (final IOException ioException) {
					log.error("Unable to delete all Files from temp_uploaded folder.");
				}
			}
		}
	}

	/**
	 * This method is used to serialize the BatchClassDescription that is to be used in Web scanner module
	 * 
	 * @param folderName
	 * @param values
	 */

	@Override
	public void serializeBatchInstanceDescription(final String folderName, final String batchDescription) throws UIException {
		if (EphesoftStringUtil.isNullOrEmpty(folderName)) {
			log.error("Cannot serialize Batch Class Description. Folder name is Null or Empty.");
		} else if (EphesoftStringUtil.isNullOrEmpty(batchDescription)) {
			log.error("Cannot serialize Batch Class Description. Description is Null or Empty.");
		} else {
			FileOutputStream fileOutputStream = null;
			File serializedExportFile = null;
			try {
				final BatchSchemaService batchSchemaService = this.getSingleBeanOfType(BatchSchemaService.class);
				final String folderPath = batchSchemaService.getUploadBatchFolder() + File.separator + folderName;
				final File currentBatchUploadFolder = new File(folderPath);
				if (!currentBatchUploadFolder.exists()) {
					currentBatchUploadFolder.mkdirs();
				}
				serializedExportFile = new File(StringUtil.concatenate(folderPath, File.separator, BID_SER_FILE_NAME,
						SERIALIZATION_EXT));
				fileOutputStream = new FileOutputStream(serializedExportFile);
				SerializationUtils.serialize(batchDescription, fileOutputStream);
			} catch (final FileNotFoundException fileNotFoundException) {
				// Unable to read serializable file
				log.info(StringUtil.concatenate("Error occurred while creating the serializable file.", fileNotFoundException));
				throw new UIException(fileNotFoundException.getMessage());
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}

				} catch (final IOException ioException) {
					if (serializedExportFile != null)
						log.error(StringUtil.concatenate("Problem closing stream for file : ", serializedExportFile.getName(),
								ioException.getMessage()));
				}
			}
		}
	}

	// Remove this before committing.
	public static void main(final String[] args) {
		final double a = 100;
		final long b = 1000;
		final double c = a / b;
		System.out.println(c);
	}
}
