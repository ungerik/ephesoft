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
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.dao.xml.BatchSchemaDao;
import com.ephesoft.dcma.batch.dao.xml.HocrSchemaDao;
import com.ephesoft.dcma.batch.encryption.service.XMLSignature;
import com.ephesoft.dcma.batch.exception.XMLSignatureException;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.TesseractVersionProperty;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.OpenOfficeFileType;
import com.ephesoft.dcma.core.common.UserType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.AbstractRunnable;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassCloudConfig;
import com.ephesoft.dcma.da.domain.BatchClassDynamicPluginConfig;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassModuleConfig;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchClassPluginConfig;
import com.ephesoft.dcma.da.domain.KVPageProcess;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.da.service.BatchClassCloudConfigService;
import com.ephesoft.dcma.da.service.BatchClassModuleService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.BackUpFileService;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileFormatException;
import com.ephesoft.dcma.util.FileNameFormatter;
import com.ephesoft.dcma.util.IUtilCommonConstants;
import com.ephesoft.dcma.util.OCREngineUtil;
import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.util.constant.HOCRGenerationPlugin;

/**
 * This is a service to read and write data required by Batch Schema. Api's are present to move, rearrange, delete, create, swap the
 * document and pages from one to each other. On the basis of batch instance id and base folder location will read the batch.xml file
 * and rearrange the attribute of the xml file.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.batch.service.BatchSchemaService
 * @see com.ephesoft.dcma.batch.dao.xml.BatchSchemaDao
 */
public class BatchSchemaServiceImpl implements BatchSchemaService {

	/**
	 * LOGGER to print the logging information.
	 */
	private final Logger logger = LoggerFactory.getLogger(BatchSchemaServiceImpl.class);

	/**
	 * Name of TESSERACT HOCR Plugin
	 */
	private static final String TESSERACT_HOCR_PLUGIN = "TESSERACT_HOCR";

	/**
	 * Radix base.
	 */
	public static final int RADIX_BASE = 10;

	/**
	 * The USER_TYPE {@link String} is a constant for user type property key.
	 */
	private static final String USER_TYPE = "user_type";

	/**
	 * Provides a mechanism to generate the HOCR file signature generated.
	 */
	@Autowired
	private XMLSignature xmlSignatureService;
	/**
	 * Reference of BatchSchemaDao.
	 */
	@Autowired
	private BatchSchemaDao batchSchemaDao;

	/**
	 * Instance of Batch Instance Service {@link BatchInstanceService}
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * Reference of ScannerSchemaDao.
	 */
	@Autowired
	private HocrSchemaDao hocrSchemaDao;

	/**
	 * Instance of Batch Class Service {@link BatchClassService}
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * The batchClassCloudService {@link BatchClassCloudConfigService} is used for using batch class cloud service.
	 */
	@Autowired
	private BatchClassCloudConfigService batchClassCloudService;

	/**
	 * Instance of BatchClassPluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService batchClassPluginPropertiesService;

	/**
	 * Instance of BatchClassModuleService.
	 */
	@Autowired
	private BatchClassModuleService batchClassModuleService;

	/**
	 * An api to return the complete local folder location path.
	 * 
	 * @return String localFolderLocation
	 */
	@Override
	public boolean isZipSwitchOn() {
		boolean isZipSwitchOn = true;
		try {
			ApplicationConfigProperties prop = ApplicationConfigProperties.getApplicationConfigProperties();
			isZipSwitchOn = Boolean.parseBoolean(prop.getProperty(ICommonConstants.ZIP_SWITCH));
		} catch (IOException ioe) {
			logger.error("Unable to read the zip switch value. Taking default value as false.Exception thrown is:" + ioe.getMessage(),
					ioe);
		}
		return isZipSwitchOn;
	}

	/**
	 * An api to return the complete local folder location path.
	 * 
	 * @return String localFolderLocation
	 */
	@Override
	public String getLocalFolderLocation() {
		return batchSchemaDao.getJAXB2Template().getLocalFolderLocation();
	}

	@Override
	public void setLocalFolderLocation(String localFolderLocation) {
		if (null != localFolderLocation && !localFolderLocation.isEmpty()) {
			batchSchemaDao.getJAXB2Template().setLocalFolderLocation(localFolderLocation);
			hocrSchemaDao.getJAXB2Template().setLocalFolderLocation(localFolderLocation);
		}
	}

	@Override
	public String getBaseSampleFDLock() {
		return batchSchemaDao.getJAXB2Template().getBaseSampleFdLoc();
	}

	@Override
	public String getScriptFolderName() {
		return batchSchemaDao.getJAXB2Template().getScriptFolderName();
	}

	@Override
	public String getCmisPluginMappingFolderName() {
		return batchSchemaDao.getJAXB2Template().getCmisPluginMappingFolderName();
	}

	/**
	 * An api to return the sample path for search classification.
	 * 
	 * @param batchClassIdentifier String
	 * @param createDir boolean
	 * @return String searchClassSamplePath
	 */
	@Override
	public String getSearchClassSamplePath(String batchClassIdentifier, boolean createDir) {
		return getAbsolutePath(batchClassIdentifier, batchSchemaDao.getJAXB2Template().getSearchSampleName(), createDir);
	}

	/**
	 * An api to return the index folder for search classification.
	 * 
	 * @param batchClassIdentifier String
	 * @param createDir boolean
	 * @return String searchClassSamplePath
	 */
	@Override
	public String getSearchClassIndexFolder(String batchClassIdentifier, boolean createDir) {
		return getAbsolutePath(batchClassIdentifier, batchSchemaDao.getJAXB2Template().getSearchIndexFolderName(), createDir);
	}

	/**
	 * An api to return the base folder path for imagemagick.
	 * 
	 * @param batchClassIdentifier String
	 * @param createDir boolean
	 * @return String searchClassSamplePath
	 */
	@Override
	public String getImageMagickBaseFolderPath(String batchClassIdentifier, boolean createDir) {
		return getAbsolutePath(batchClassIdentifier, batchSchemaDao.getJAXB2Template().getImageMagickBaseFolderName(), createDir);
	}

	/**
	 * An api to return the Index folder for fuzzy db.
	 * 
	 * @param batchClassIdentifier String
	 * @param createDir boolean
	 * @return String searchClassSamplePath
	 */
	@Override
	public String getFuzzyDBIndexFolder(String batchClassIdentifier, boolean createDir) {
		return getAbsolutePath(batchClassIdentifier, batchSchemaDao.getJAXB2Template().getFuzzyDBIndexFolderName(), createDir);
	}

	/**
	 * An api to return the complete export folder location path.
	 * 
	 * @return String exportFolderLocation.
	 */
	@Override
	public String getExportFolderLocation() {
		return batchSchemaDao.getJAXB2Template().getExportFolderLocation();
	}

	/**
	 * An api to return the base http url path.
	 * 
	 * @return String base http url path.
	 */
	@Override
	public String getBaseHttpURL() {
		return batchSchemaDao.getJAXB2Template().getBaseHttpURL();
	}

	/**
	 * An api to get the absolute path of the directory. It will create the directory if it is not exits and createDir boolean is true
	 * otherwise not.
	 * 
	 * @param batchClassIdentifier String
	 * @param directoryName String
	 * @param createDir boolean
	 * @return absolute path of the directory.
	 */
	@Override
	public String getAbsolutePath(String batchClassIdentifier, String directoryName, boolean createDir) {

		String absolutePath = null;
		if (null == batchClassIdentifier || null == directoryName || "".equals(batchClassIdentifier) || "".equals(directoryName)) {
			logger.error("batchClassIdentifier/directoryName is null or empty.");
		} else {
			absolutePath = batchSchemaDao.getJAXB2Template().getBaseSampleFdLoc() + File.separator + batchClassIdentifier
					+ File.separator + directoryName;
			if (createDir) {
				try {
					// Create multiple directories
					boolean success = new File(absolutePath).mkdirs();
					if (success) {
						logger.info("Directories: " + absolutePath + " created.");
					} else {
						logger.info("Directories: " + absolutePath + " not created.");
					}
				} catch (Exception e) {
					logger.error("Directories: " + absolutePath + " not created.");
					logger.error(e.getMessage());
				}
			}
		}

		return absolutePath;
	}

	/**
	 * An api to create the Batch object.
	 * 
	 * @param batch Batch
	 */
	@Override
	public void createBatch(Batch batch) {
		if (null == batch) {
			logger.info("batch class is null.");
		} else {
			this.batchSchemaDao.create(batch, batch.getBatchInstanceIdentifier(), isZipSwitchOn());
		}
	}

	/**
	 * An api to create the hocrPages object.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param workflowName {@link String}
	 */
	@Override
	public void createBatchXml(final String batchInstanceIdentifier, final String workflowName) {
		logger.trace("Entering createBatchXml");
		if (null == batchInstanceIdentifier) {
			logger.info("batch instance is null.");
		} else {
			Batch batch = batchSchemaDao.getInMemoryBatch(batchInstanceIdentifier);
			if (null == batch) {
				logger.warn(EphesoftStringUtil.concatenate("No batch object exists in memory for batch instance ",
						batchInstanceIdentifier), ". Cannot create module backup for ", workflowName);
			} else {
				String systemFolderLocationPath = this.batchSchemaDao.getJAXB2Template().getLocalFolderLocation();
				File batchInstanceFolder = new File(EphesoftStringUtil.concatenate(systemFolderLocationPath, File.separator,
						batchInstanceIdentifier));
				if (batchInstanceFolder.exists()) {
					if (BackUpFileService.isBackupRequired()) {
						logger.info(EphesoftStringUtil.concatenate("Adding Batch XML to in-memory Map for ", batchInstanceIdentifier));
						this.batchSchemaDao.create(batch, batchInstanceIdentifier, workflowName,
								BackUpFileService.getOutputFileProperty(), isZipSwitchOn());
						BackUpFileService.copyBatchXML(batchInstanceIdentifier, workflowName);
					}
				}
			}
		}
	}

	/**
	 * An api to create the hocrPages object.
	 * 
	 * @param batch {@link Batch}
	 * @param batchInstanceIdentifier {@link String}
	 * @param pageId {@link String}
	 */
	@Override
	public void createHocr(HocrPages hocrPages, String batchInstanceIdentifier, String pageId) {
		if (null == hocrPages) {
			logger.info("hocrPages is null.");
		} else {
			this.hocrSchemaDao.create(hocrPages, batchInstanceIdentifier, pageId, FileType.HOCR_XML.getExtension(), Boolean.FALSE,
					true);
		}
	}

	/**
	 * An api to get the HocrPages object for an input of HOCR.XML.
	 * 
	 * @param batchInstanceIdentifier Serializable
	 * @param hocrFileName {@link String}
	 * @return {@link HocrPages}
	 */
	@Override
	public HocrPages getHocrPages(Serializable batchInstanceIdentifier, String hocrFileName) {
		HocrPages hocrPages = null;
		if (null == batchInstanceIdentifier) {
			logger.info("batchInstanceIdentifier is null.");
		} else {
			hocrPages = this.hocrSchemaDao.getObjectFromFilePath(getHOCRFilePath(batchInstanceIdentifier, hocrFileName));
		}
		return hocrPages;
	}

	/**
	 * An api to get the HocrPages object for an input of HOCR.XML.
	 * 
	 * @param batchInstanceIdentifier Serializable
	 * @param hocrFileName {@link String}
	 * @return {@link HocrPages}
	 */
	@Override
	public HocrPages getHocrPagesForTestContent(String pathToHOCRFile, String hocrFileName) {
		HocrPages hocrPages = null;
		if (null == pathToHOCRFile) {
			logger.info("Path provided is null.");
		} else {
			hocrPages = this.hocrSchemaDao.getObjectFromFilePath(EphesoftStringUtil.concatenate(pathToHOCRFile, File.separator,
					hocrFileName));
		}
		return hocrPages;
	}

	/**
	 * A method to get the absolute HOCR file path of a page Id of a batch inside
	 * 
	 * @param batchInstanceIdentifier The batch instance identifier of the page whose HOCR file path is to be fetched.
	 * @param pageId The identifier of the page whose HOCR file path is to be fetched.
	 * @return
	 */
	private String getHOCRFilePath(Serializable batchInstanceIdentifier, String hocrFileName) {
		return hocrSchemaDao.getJAXB2Template().getLocalFolderLocation() + File.separator + batchInstanceIdentifier + File.separator
				+ hocrFileName;
	}

	public void update(HocrPages hocrPages, Serializable batchInstanceIdentifier, String pageId) {
		if (null == batchInstanceIdentifier) {
			logger.info("batchInstanceIdentifier is null.");
		} else {
			this.hocrSchemaDao.update(hocrPages, batchInstanceIdentifier, FileType.HOCR_XML.getExtension(), pageId, isZipSwitchOn(),
					false);
		}
	}

	/**
	 * An api to get the Batch object for an input of batchInstanceIdentifier.
	 * 
	 * @param batchInstanceIdentifier Serializable
	 * @return
	 */
	@Override
	public Batch getBatch(Serializable batchInstanceIdentifier) {
		Batch batch = null;
		if (null == batchInstanceIdentifier) {
			logger.info("batch Identifier is null.");
		} else {
			batch = this.batchSchemaDao.getInMemoryBatch(batchInstanceIdentifier);
			if (null == batch) {
				throw new DCMABusinessException(EphesoftStringUtil.concatenate("No In-memory batch xml found for batch instance: ",
						batchInstanceIdentifier));
			}
		}
		return batch;
	}

	@Override
	public Batch getBatchFromXML(Serializable batchInstanceIdentifier, boolean throwException) {
		Batch batch = null;
		if (null == batchInstanceIdentifier) {
			logger.info("Batch instance Id is null.");
		} else {
			batch = this.batchSchemaDao.get(batchInstanceIdentifier, isZipSwitchOn(), throwException);
		}
		return batch;
	}

	@Override
	public Batch getBatchFromModuleBackup(Serializable batchInstanceIdentifier, String workflowName) {
		Batch batch = null;
		if (null == batchInstanceIdentifier) {
			logger.info("Batch instance Id is null.");
		} else {
			batch = this.batchSchemaDao.get(batchInstanceIdentifier, workflowName, null, BackUpFileService.getOutputFileProperty(),
					isZipSwitchOn());
		}
		return batch;
	}

	/**
	 * An api to update the Batch object.
	 * 
	 * @param batch Batch
	 */
	@Override
	public void updateBatch(Batch batch) {
		if (null == batch) {
			logger.info("Batch is null.");
		} else {
			batchSchemaDao.updateInMemoryBatch(batch, batch.getBatchInstanceIdentifier());
		}
	}

	@Override
	public void updateBatchXML(Batch batch) {
		if (null == batch) {
			logger.info("Batch is null.");
		} else {
			this.batchSchemaDao.update(batch, batch.getBatchInstanceIdentifier(), isZipSwitchOn(), true);
		}
	}

	/**
	 * An api to update the Batch object.
	 * 
	 * @param batch Batch
	 * @param isFirstTimeUpdate
	 * 
	 */
	@Override
	public void updateBatch(Batch batch, boolean isFirstTimeUpdate) {
		if (null == batch) {
			logger.info("Batch is null.");
		} else {
			this.batchSchemaDao.updateInMemoryBatch(batch, batch.getBatchInstanceIdentifier());
		}
	}

	/**
	 * An api to store all files to base folder location.
	 * 
	 * @param id Serializable
	 * @param files File[]
	 */
	public void storeFiles(Serializable id, File[] files) {

		String errMsg = null;

		if (null == id || null == files) {
			errMsg = "Input parameters id/files are null.";
			logger.error(errMsg);
			throw new DCMABusinessException(errMsg);
		}

		boolean preserveFileDate = false;
		String newPath = null;
		String localFolderLocation = batchSchemaDao.getJAXB2Template().getLocalFolderLocation();

		for (File srcFile : files) {

			String path = srcFile.getPath();

			newPath = localFolderLocation + File.separator + id + BatchConstants.UNDER_SCORE + path;
			// The target file name to which the source file will be copied.
			File destFile = new File(newPath);

			try {
				FileUtils.copyFile(srcFile, destFile, preserveFileDate);
				errMsg = "Successfully copy of the file for the batch Instance Id : " + id;
				logger.info(errMsg);
			} catch (IOException e) {
				errMsg = "Unable to copy the file for the batch Instance Id : " + id;
				logger.error(errMsg);
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * An api to create backup of all files to input folder location.
	 * 
	 * @param id Serializable
	 * @param files File[]
	 */
	public void backUpFiles(Serializable id, File[] files) {

		String errMsg = null;

		if (null == id || null == files) {
			errMsg = "Input parameters id/files are null.";
			logger.error(errMsg);
			throw new DCMABusinessException(errMsg);
		}

		boolean preserveFileDate = false;
		String newAbsPath = null;

		for (File srcFile : files) {

			String absPath = srcFile.getAbsolutePath();
			String path = srcFile.getPath();

			String[] arr = absPath.split(path);
			if (null != arr && arr.length >= 0) {
				newAbsPath = arr[0] + BatchConstants.UNDER_SCORE + id + "_backUp_" + path;
			}

			if (null == newAbsPath) {
				continue;
			}

			// The target file name to which the source file will be copied.
			File destFile = new File(newAbsPath);

			try {
				FileUtils.copyFile(srcFile, destFile, preserveFileDate);
				errMsg = "Successfully copy of the file for the batch Instance Id : " + id;
				logger.info(errMsg);
			} catch (IOException e) {
				errMsg = "Unable to copy the file for the batch Instance Id : " + id;
				logger.error(errMsg);
				logger.error(e.getMessage());
			}
		}
	}

	/**
	 * An api to get the URL object for the specified file name.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param fileName String
	 * @return URL object.
	 */
	@Override
	public URL getURL(String batchInstanceIdentifier, String fileName) {

		String errMsg = null;

		if (null == fileName || "".equals(fileName)) {
			errMsg = "File name is null or empty.";
			logger.error(errMsg);
			throw new DCMABusinessException(errMsg);
		}

		URL url = null;
		String pathName = getBatchFolderURL() + "/" + batchInstanceIdentifier + "/" + fileName;

		try {
			url = new URL(pathName);
		} catch (MalformedURLException e) {
			errMsg = "File name does not exists at the specified path location.";
			logger.error(errMsg);
			throw new DCMABusinessException(errMsg, e);
		}

		return url;
	}

	/**
	 * An api to get the File object for the specified file name.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param fileName String
	 * @return File object.
	 */
	@Override
	public File getFile(String batchInstanceIdentifier, String fileName) {

		String errMsg = null;

		if (null == fileName || "".equals(fileName)) {
			errMsg = "File name is null or empty.";
			logger.error(errMsg);
			throw new DCMABusinessException(errMsg);
		}

		File file = null;
		String pathName = getLocalFolderLocation() + File.separator + batchInstanceIdentifier + File.separator + fileName;
		file = new File(pathName);
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			errMsg = "File name does not exists at the specified path location. file : " + file.getName() + " , filepath : "
					+ file.getAbsolutePath();
			logger.error(errMsg);
			throw new DCMABusinessException(errMsg + e.getMessage(), e);
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					logger.error("could not cloose the fileInputStream . file + " + file.getAbsolutePath() + e.getMessage(), e);
				}
			}
		}

		/*
		 * if (!file.exists()) { errMsg = "File name does not exists at the specified path location."; logger.error(errMsg); throw new
		 * DCMABusinessException(errMsg); }
		 */

		return file;
	}

	/**
	 * An api to get the input stream object for the specified file name.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param fileName String
	 * @return InputStream object.
	 */
	@Override
	public InputStream getInputStream(String batchInstanceIdentifier, String fileName) {

		String errMsg = null;

		InputStream inputStream = null;
		String pathName = getLocalFolderLocation() + File.separator + batchInstanceIdentifier + File.separator + fileName;

		// Open the file that is the first
		// command line parameter
		try {
			inputStream = new FileInputStream(pathName);
		} catch (FileNotFoundException e) {
			errMsg = e.getMessage();
			logger.error(errMsg);
			throw new DCMABusinessException(errMsg, e);
		} catch (Exception e) {
			errMsg = e.getMessage();
			logger.error(errMsg);
			throw new DCMABusinessException(errMsg, e);
		}

		return inputStream;
	}

	/**
	 * An api to move the page of one document to to another document. Position of the page in new document will depend on the to page
	 * ID. If the isAfterToPageID boolean is true then from page ID is added after the to page ID other wise before the to page ID.
	 * After the movement api will delete the from page ID from the from doc ID.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param fromDocID String
	 * @param fromPageID String
	 * @param toDocID String
	 * @param toPageID String from page ID position relative to this page ID.
	 * @param isAfterToPageID boolean true means add after the to page ID. false means add before the to page ID.
	 * @throws DCMAApplicationException If not able to find fromDocID, fromPageID or toDocID. If not able to find fromPageID within
	 *             fromDocID.
	 */
	@Override
	public void movePageOfDocument(String batchInstanceIdentifier, String fromDocID, String fromPageID, String toDocID,
			String toPageID, boolean isAfterToPageID) throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == fromDocID || null == fromPageID || null == toDocID || null == toPageID) {
			errMsg = "Input parameters batchInstanceIdentifier/fromDocID/fromPageID/toDocID/toPageID are null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = batchSchemaDao.get(batchInstanceIdentifier);

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		int fromDocIndex = getDocumentTypeIndex(docTypesList, fromDocID);

		int fromPageIndex = getPageTypeIndex(docTypesList, fromDocIndex, fromPageID);

		Document fromDocType = docTypesList.get(fromDocIndex);
		Pages fromPages = fromDocType.getPages();
		List<Page> fromPageTypeList = fromPages.getPage();
		Page fromPgType = fromPageTypeList.get(fromPageIndex);

		int toDocIndex = getDocumentTypeIndex(docTypesList, toDocID);
		int toPageIndex = getPageTypeIndex(docTypesList, toDocIndex, toPageID);
		int newPageIndex = -1;
		if (isAfterToPageID) {
			newPageIndex = toPageIndex + 1;
		} else {
			newPageIndex = toPageIndex;
		}
		Document toDocType = docTypesList.get(toDocIndex);
		Pages toPages = toDocType.getPages();
		List<Page> toPageTypeList = toPages.getPage();

		if (newPageIndex < 0) {
			errMsg = "newPageIndex is not valid for toDocID : " + toDocID + " and toPageID" + toPageID;
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		// remove the page from document type.
		fromPageTypeList.remove(fromPageIndex);

		if (newPageIndex > toPageTypeList.size()) {
			newPageIndex = toPageTypeList.size();
		}

		toPageTypeList.add(newPageIndex, fromPgType);

		// Add a check for single page document. If a document have single page
		// and we are moving that page means delete the whole document.

		if (fromPageTypeList.isEmpty()) {
			docTypesList.remove(fromDocIndex);
		}

		batchSchemaDao.update(batch, batchInstanceIdentifier, isZipSwitchOn());

	}

	/**
	 * An api to remove the page of document. This will remove all the information available to page. This will delete all the files
	 * present at page level fields. If a document have single page and we are deleting that page means delete the whole document.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docID String
	 * @param removePageID String
	 * @throws DCMAApplicationException If not able to find docID, removePageID. If not able to find removePageID within docID.
	 */
	@Override
	public void removePageOfDocument(String batchInstanceIdentifier, String docID, String removePageID)
			throws DCMAApplicationException {
		logger.info("inside removePageOfDocument method.");
		String errMsg = null;

		if (null == batchInstanceIdentifier || null == docID || null == removePageID) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/removePageID are null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = batchSchemaDao.get(batchInstanceIdentifier);

		DocPageCarrier docPageCarrier = getDocPageCarrier(batch, docID, removePageID);

		int pageIndex = docPageCarrier.getPageIndex();
		int docIndex = docPageCarrier.getDocIndex();
		List<Document> docTypesList = docPageCarrier.getDocTypesList();

		List<Page> pageTypeList = docTypesList.get(docIndex).getPages().getPage();

		// Remove the page from the document.
		pageTypeList.remove(pageIndex);

		// Add a check for single page document. If a document have single page
		// and we are deleting that page means delete the whole document.

		if (pageTypeList.isEmpty()) {
			docTypesList.remove(docIndex);
		}
		logger.info("updating batch xml.");
		batchSchemaDao.update(batch, batchInstanceIdentifier, isZipSwitchOn());
		logger.info("Exiting method removePageOfDocument.");
	}

	/**
	 * An API to create duplicate page of document. This will create duplicate for all the information available to page. This will
	 * create duplicate for all the files present at page level fields.
	 * 
	 * @param batchInstanceId String
	 * @param docID String
	 * @param duplicatePageID String
	 * @throws DCMAApplicationException If not able to find docID, duplicatePageID. If not able to find removePageID within
	 *             duplicatePageID.
	 */
	@Override
	public void duplicatePageOfDocument(final String batchInstanceId, final String docID, final String duplicatePageID)
			throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceId || null == docID || null == duplicatePageID) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/duplicatePageID are null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		final Batch batch = batchSchemaDao.get(batchInstanceId);

		final DocPageCarrier docPageCarrier = getDocPageCarrier(batch, docID, duplicatePageID);

		final String batchInstanceFolderPath = getLocalFolderLocation() + File.separator + batchInstanceId;

		final int pageIndex = docPageCarrier.getPageIndex();
		logger.info("pageIndex : " + pageIndex);
		final int docIndex = docPageCarrier.getDocIndex();
		final List<Document> docTypesList = docPageCarrier.getDocTypesList();

		checkDocTypeList(docTypesList);

		final Page duplicatePageType = docTypesList.get(docIndex).getPages().getPage().get(pageIndex);

		final String newPageTypeID = getNewPageTypeID(docTypesList);

		final Page duplicateCopyPage = duplicatePageFiles(duplicatePageType, batchInstanceId, newPageTypeID, batchInstanceFolderPath);

		// Add the duplicate page type to the end of the document.
		docTypesList.get(docIndex).getPages().getPage().add(duplicateCopyPage);

		batchSchemaDao.update(batch, batchInstanceId, isZipSwitchOn());

	}

	private void checkDocTypeList(final List<Document> docTypesList) throws DCMAApplicationException {
		String errMsg;
		if (null == docTypesList) {
			errMsg = BatchConstants.DOC_TYPE_LIST_NULL;
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}
	}

	/**
	 * An api to re order pages of document. This will re order the pages for the input document id.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docID String
	 * @param reOrderOfPageIDs String
	 * @throws DCMAApplicationException If not able to find docID, reOrderOfPageIDs. If not able to find all reOrderOfPageIDs within
	 *             duplicatePageID.
	 */
	@Override
	public void reOrderPagesOfDocument(String batchInstanceIdentifier, String docID, List<String> reOrderOfPageIDs)
			throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == docID || null == reOrderOfPageIDs) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/reOrderOfPageIDs are null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = batchSchemaDao.get(batchInstanceIdentifier);

		List<Document> docTypesList = validateParams(batch, docID, reOrderOfPageIDs);

		int docIndex = getDocumentTypeIndex(docTypesList, docID);
		logger.info("Document type index : " + docIndex);

		Document docType = docTypesList.get(docIndex);
		Pages pages = docType.getPages();
		if (null == pages) {
			errMsg = "There are zero pages for document ID : " + docID;
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		List<Page> pageTypeList = pages.getPage();

		if (reOrderOfPageIDs.size() != pageTypeList.size()) {
			errMsg = "ReOrderOfPageID's list are not valid for document ID : " + docID;
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Pages reOrdPages = new Pages();
		List<Page> reOrderPageTypeList = reOrdPages.getPage();

		for (String reOrderPgID : reOrderOfPageIDs) {
			for (Page pgLt : pageTypeList) {
				String pgID = pgLt.getIdentifier();
				if (null != pgID && null != reOrderPgID && pgID.equals(reOrderPgID)) {
					reOrderPageTypeList.add(pgLt);
					break;
				}
			}
		}

		docType.setPages(reOrdPages);

		batchSchemaDao.update(batch, batchInstanceIdentifier, isZipSwitchOn());
	}

	/**
	 * An api to swap pages of document. This will swap the pages for the input document id.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docID String
	 * @param swapPageIDOne String
	 * @param swapPageIDTwo String
	 * @throws DCMAApplicationException If not able to find docID, swapPageIDOne or swapPageIDTwo. If not able to find all
	 *             swapPageIDOne and swapPageIDTwo within docID.
	 */
	@Override
	public void swapPageOfDocument(String batchInstanceIdentifier, String docID, String swapPageIDOne, String swapPageIDTwo)
			throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == docID || null == swapPageIDOne || null == swapPageIDTwo) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/pageID are null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = batchSchemaDao.get(batchInstanceIdentifier);

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		int docIndex = getDocumentTypeIndex(docTypesList, docID);

		int swapPageIndexOne = getPageTypeIndex(docTypesList, docIndex, swapPageIDOne);

		int swapPageIndexTwo = getPageTypeIndex(docTypesList, docIndex, swapPageIDTwo);

		Document docType = docTypesList.get(docIndex);
		Pages pages = docType.getPages();
		List<Page> pageTypeList = pages.getPage();
		Page swapPageTypeOne = pageTypeList.get(swapPageIndexOne);
		Page swapPageTypeTwo = pageTypeList.get(swapPageIndexTwo);

		pageTypeList.set(swapPageIndexOne, swapPageTypeTwo);
		pageTypeList.set(swapPageIndexTwo, swapPageTypeOne);

		batchSchemaDao.update(batch, batchInstanceIdentifier, isZipSwitchOn());
	}

	/**
	 * An api to swap one page of one document to second page of other document.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param swapDocIDOne String
	 * @param swapPageIDOne String
	 * @param swapDocIDTwo String
	 * @param swapPageIDTwo String
	 * @throws DCMAApplicationException If not able to find swapDocIDOne, swapPageIDOne, swapDocIDTwo or swapPageIDTwo. If not able to
	 *             find all swapPageIDOne and swapPageIDTwo within swapDocIDOne and swapDocIDTwo respectively.
	 */
	@Override
	public void swapPageOfDocuments(String batchInstanceIdentifier, String swapDocIDOne, String swapPageIDOne, String swapDocIDTwo,
			String swapPageIDTwo) throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == swapDocIDOne || null == swapPageIDOne || null == swapDocIDTwo
				|| null == swapPageIDTwo) {
			errMsg = "Input parameters batchInstanceIdentifier/swapDocIDOne/swapPageIDOne/" + "swapDocIDTwo/swapPageIDTwo are null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = batchSchemaDao.get(batchInstanceIdentifier);

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		int swapDocIndexOne = getDocumentTypeIndex(docTypesList, swapDocIDOne);

		int swapPageIndexOne = getPageTypeIndex(docTypesList, swapDocIndexOne, swapPageIDOne);

		int swapDocIndexTwo = getDocumentTypeIndex(docTypesList, swapDocIDTwo);

		int swapPageIndexTwo = getPageTypeIndex(docTypesList, swapDocIndexTwo, swapPageIDTwo);

		Document docTypeOne = docTypesList.get(swapDocIndexOne);
		Pages pagesOne = docTypeOne.getPages();
		List<Page> pageTypeListOne = pagesOne.getPage();
		Page swapPageTypeOne = pageTypeListOne.get(swapPageIndexOne);

		Document docTypeTwo = docTypesList.get(swapDocIndexTwo);
		Pages pagesTwo = docTypeTwo.getPages();
		List<Page> pageTypeListTwo = pagesTwo.getPage();
		Page swapPageTypeTwo = pageTypeListTwo.get(swapPageIndexTwo);

		pageTypeListOne.set(swapPageIndexOne, swapPageTypeTwo);
		pageTypeListTwo.set(swapPageIndexTwo, swapPageTypeOne);

		batchSchemaDao.update(batch, batchInstanceIdentifier, isZipSwitchOn());
	}

	/**
	 * An api to merge two different documents to a single document. This will merge all the pages of second document to first
	 * document.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docIDOne String
	 * @param mergeDocID String
	 * @throws DCMAApplicationException If any of the input parameter is null.
	 */
	@Override
	public Batch mergeDocuments(String batchInstanceIdentifier, String docIDOne, String mergeDocID) throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == docIDOne || null == mergeDocID) {
			errMsg = "Input parameters batchInstanceIdentifier/docIDOne/mergeDocID" + " are null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = batchSchemaDao.get(batchInstanceIdentifier);

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		int mergeDocIndexOne = getDocumentTypeIndex(docTypesList, docIDOne);

		int mergeDocIndexTwo = getDocumentTypeIndex(docTypesList, mergeDocID);

		Document mergeDocTypeOne = docTypesList.get(mergeDocIndexOne);
		Pages mergePagesOne = mergeDocTypeOne.getPages();
		List<Page> mergePageTypeListOne = mergePagesOne.getPage();

		Document mergeDocTypeTwo = docTypesList.get(mergeDocIndexTwo);
		Pages mergePagesTwo = mergeDocTypeTwo.getPages();
		List<Page> pageTypeListTwo = mergePagesTwo.getPage();

		mergePageTypeListOne.addAll(pageTypeListTwo);
		docTypesList.remove(mergeDocTypeTwo);

		batchSchemaDao.update(batch, batchInstanceIdentifier, isZipSwitchOn());
		return batchSchemaDao.get(batchInstanceIdentifier);
	}

	/**
	 * An api to split the document for given input page id. This will create a new document starting from input page id to the last
	 * page id.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docID String
	 * @param pageID String
	 * @throws DCMAApplicationException If any one of the parameter is null or pageID is not present in docID.
	 */
	@Override
	public void splitDocument(String batchInstanceIdentifier, String docID, String pageID) throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == docID || null == pageID) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/pageID are null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = batchSchemaDao.get(batchInstanceIdentifier);

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		int docIndex = getDocumentTypeIndex(docTypesList, docID);

		int pageIndex = getPageTypeIndex(docTypesList, docIndex, pageID);

		Document docType = docTypesList.get(docIndex);
		Pages pages = docType.getPages();
		List<Page> pageTypeList = pages.getPage();

		// create new document.
		String newDocID = getNewDocumentTypeID(docTypesList);

		Document newDocType = new Document();
		newDocType.setIdentifier(EphesoftProperty.DOCUMENT.getProperty() + newDocID);
		newDocType.setType(docType.getType());
		newDocType.setDescription(docType.getDescription());
		newDocType.setConfidence(docType.getConfidence());
		newDocType.setMultiPagePdfFile(docType.getMultiPagePdfFile());
		newDocType.setMultiPageTiffFile(docType.getMultiPageTiffFile());
		newDocType.setValid(docType.isValid());
		newDocType.setReviewed(docType.isReviewed());
		newDocType.setErrorMessage(BatchConstants.EMPTY);
		newDocType.setDocumentDisplayInfo(BatchConstants.EMPTY);

		Pages newPages = new Pages();
		List<Page> listOfNewPages = newPages.getPage();
		List<String> listOfNewPageIdentifiers = new ArrayList<String>();
		// create the new document for split pages.
		for (int index = pageIndex; index < pageTypeList.size(); index++) {
			Page pgType = pageTypeList.get(index);
			listOfNewPages.add(pgType);
			listOfNewPageIdentifiers.add(pgType.getIdentifier());
		}

		newDocType.setPages(newPages);

		// remove the pages from the older document.
		for (int index = pageTypeList.size() - 1; index >= pageIndex; index--) {
			pageTypeList.remove(index);
		}

		DocumentLevelFields documentLevelFields = docType.getDocumentLevelFields();
		if (documentLevelFields != null) {
			List<DocField> docFields = documentLevelFields.getDocumentLevelField();
			DocumentLevelFields documentLevelFieldsForNewDoc = new DocumentLevelFields();
			List<DocField> docFieldsForNewDoc = documentLevelFieldsForNewDoc.getDocumentLevelField();
			if (docFields != null) {
				for (DocField docField : docFields) {
					DocField docFieldForNewDoc = new DocField();
					docFieldForNewDoc.setConfidence(0.0f);
					docFieldForNewDoc.setFieldOrderNumber(0);
					docFieldForNewDoc.setForceReview(false);
					docFieldForNewDoc.setOcrConfidence(0.0f);
					docFieldForNewDoc.setOcrConfidenceThreshold(0.0f);
					docFieldForNewDoc.setConfidence(docField.getConfidence());
					docFieldForNewDoc.setFieldOrderNumber(docField.getFieldOrderNumber());
					docFieldForNewDoc.setFieldValueOptionList(docField.getFieldValueOptionList());
					docFieldForNewDoc.setName(docField.getName());
					docFieldForNewDoc.setType(docField.getType());
					docFieldForNewDoc.setValue(docField.getValue());
					docFieldForNewDoc.setExtractionName(docField.getExtractionName());
					docFieldForNewDoc.setCategory(docField.getCategory());
					docFieldForNewDoc.setWidgetType(docField.getWidgetType());
					if (listOfNewPageIdentifiers.contains(docField.getPage())) {
						docFieldForNewDoc.setCoordinatesList(docField.getCoordinatesList());
						docFieldForNewDoc.setOverlayedImageFileName(docField.getOverlayedImageFileName());
						docFieldForNewDoc.setPage(docField.getPage());
						docField.setPage(pageTypeList.get(0).getIdentifier());
						docField.setCoordinatesList(null);
						docField.setOverlayedImageFileName(null);
					} else {
						docFieldForNewDoc.setCoordinatesList(null);
						docFieldForNewDoc.setOverlayedImageFileName(null);
						if (docField.getPage() != null && !docField.getPage().isEmpty()) {
							docFieldForNewDoc.setPage(listOfNewPages.get(0).getIdentifier());
						} else {
							docFieldForNewDoc.setPage(docField.getPage());
						}
					}
					AlternateValues alternateValuesForNewDoc = new AlternateValues();
					List<Field> fieldsForNewDoc = alternateValuesForNewDoc.getAlternateValue();
					AlternateValues alternateValues = docField.getAlternateValues();
					if (alternateValues != null) {
						List<Field> alternateValueFields = alternateValues.getAlternateValue();
						if (alternateValueFields != null) {
							for (Field field : alternateValueFields) {
								Field fieldForNewDoc = new Field();
								fieldForNewDoc.setForceReview(Boolean.FALSE);
								fieldForNewDoc.setConfidence(field.getConfidence());
								fieldForNewDoc.setFieldOrderNumber(field.getFieldOrderNumber());
								fieldForNewDoc.setFieldValueOptionList(field.getFieldValueOptionList());
								fieldForNewDoc.setName(field.getName());
								fieldForNewDoc.setType(field.getType());
								fieldForNewDoc.setValue(field.getValue());
								fieldForNewDoc.setExtractionName(field.getExtractionName());
								if (listOfNewPageIdentifiers.contains(field.getPage())) {
									fieldForNewDoc.setCoordinatesList(field.getCoordinatesList());
									fieldForNewDoc.setOverlayedImageFileName(field.getOverlayedImageFileName());
									fieldForNewDoc.setPage(field.getPage());
									field.setCoordinatesList(null);
									field.setOverlayedImageFileName(null);
									field.setPage(pageTypeList.get(0).getIdentifier());
								} else {
									fieldForNewDoc.setCoordinatesList(null);
									fieldForNewDoc.setOverlayedImageFileName(null);
									if (field.getPage() != null && !field.getPage().isEmpty()) {
										fieldForNewDoc.setPage(listOfNewPages.get(0).getIdentifier());
									} else {
										fieldForNewDoc.setPage(field.getPage());
									}
								}
								fieldsForNewDoc.add(fieldForNewDoc);
							}
						}
					}
					docFieldForNewDoc.setAlternateValues(alternateValuesForNewDoc);
					docFieldsForNewDoc.add(docFieldForNewDoc);
				}
			}

			newDocType.setDocumentLevelFields(documentLevelFieldsForNewDoc);
		} else {
			newDocType.setDocumentLevelFields(null);
		}
		docTypesList.add(docIndex + 1, newDocType);

		batchSchemaDao.update(batch, batchInstanceIdentifier, isZipSwitchOn());
	}

	/**
	 * An api to update the document type name and document level fields for the input doc type ID.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param docID String
	 * @param docTypeName String
	 * @param documentLevelFields DocumentLevelFields
	 * @throws DCMAApplicationException If any one of the parameter is null or docID is not present in batch.
	 */
	@Override
	public void updateDocTypeName(String batchInstanceIdentifier, String docID, String docTypeName,
			DocumentLevelFields documentLevelFields) throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == docID || null == docTypeName || null == documentLevelFields) {
			errMsg = "Input parameters batchInstanceIdentifier/docID/docTypeName/" + "documentLevelFields are null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = batchSchemaDao.get(batchInstanceIdentifier);

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		int docIndex = getDocumentTypeIndex(docTypesList, docID);

		Document docType = docTypesList.get(docIndex);
		docType.setType(docTypeName);
		docType.setDocumentLevelFields(documentLevelFields);

		batchSchemaDao.update(batch, batchInstanceIdentifier, isZipSwitchOn());
	}

	/**
	 * @param batch Batch
	 * @param docID String
	 * @param reOrderOfPageIDs List<String>
	 * @return List<DocumentType>
	 * @throws DCMAApplicationException If any one of the parameter is null or empty.
	 */
	private List<Document> validateParams(Batch batch, String docID, List<String> reOrderOfPageIDs) throws DCMAApplicationException {

		String errMsg = null;

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		return docTypesList;
	}

	/**
	 * @param batch Batch
	 * @param docID String
	 * @param pageID String
	 * @param xmlUtil XMLUtil
	 * @return DocPageCarrier
	 * @throws DCMAApplicationException If any one of the parameter is null or empty.
	 */
	private DocPageCarrier getDocPageCarrier(Batch batch, String docID, String pageID) throws DCMAApplicationException {
		String errMsg = null;

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		int docIndex = getDocumentTypeIndex(docTypesList, docID);

		int pageIndex = getPageTypeIndex(docTypesList, docIndex, pageID);

		DocPageCarrier docPageCarrier = new DocPageCarrier();
		docPageCarrier.setDocIndex(docIndex);
		docPageCarrier.setPageIndex(pageIndex);
		docPageCarrier.setDocTypesList(docTypesList);

		return docPageCarrier;
	}

	/**
	 * @param docTypesList List<DocumentType>
	 * @param docIndex int
	 * @param pageID String
	 * @return int page type index.
	 * @throws DCMAApplicationException If any one of the parameter is null or empty.
	 */
	private int getPageTypeIndex(List<Document> docTypesList, int docIndex, String pageID) throws DCMAApplicationException {

		String errMsg = null;
		Pages pages = docTypesList.get(docIndex).getPages();

		if (null == pages) {
			errMsg = "Page id not found for the input document id.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		List<Page> pageTypeList = pages.getPage();

		int pageIndex = 0;
		// PageType removePageType = null;
		for (Page pageType : pageTypeList) {
			String curPageID = pageType.getIdentifier();
			if (null == curPageID) {
				errMsg = "Page ID is null.";
				logger.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}
			if (curPageID.equals(pageID)) {
				// removePageType = pageType;
				break;
			}
			pageIndex++;
		}

		if (pageIndex >= pageTypeList.size()) {
			errMsg = "Page id is not found for the input page id : " + pageID;
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		return pageIndex;
	}

	/**
	 * @param docTypesList List<DocumentType>
	 * @param docID String
	 * @return int document type index.
	 * @throws DCMAApplicationException If any one of the parameter is null or empty.
	 */
	private int getDocumentTypeIndex(List<Document> docTypesList, String docID) throws DCMAApplicationException {
		String errMsg = null;
		int docIndex = 0;
		for (Document docType : docTypesList) {
			String curDocID = docType.getIdentifier();
			if (null == curDocID) {
				errMsg = "Document ID is null.";
				logger.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}
			if (curDocID.equals(docID)) {
				break;
			}
			docIndex++;
		}

		if (docIndex >= docTypesList.size()) {
			errMsg = "Document id is not found for the input document id : " + docID;
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		return docIndex;
	}

	/**
	 * An internal method to get unique page id.
	 * 
	 * @param batchInstanceFolderPath String
	 * @return String next page type id.
	 * @throws DCMAApplicationException
	 */
	@Override
	public String getNewPageTypeID(List<Document> docTypesList) throws DCMAApplicationException {
		String errMsg = null;

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Long maxPageTypeID = 0L;

		for (Document docType : docTypesList) {
			Pages pages = docType.getPages();
			if (null != pages) {
				List<Page> pageTypeList = pages.getPage();
				for (Page pageType : pageTypeList) {
					String curPageID = pageType.getIdentifier();
					if (null == curPageID) {
						errMsg = "Page ID is null.";
						logger.error(errMsg);
						throw new DCMAApplicationException(errMsg);
					}
					curPageID = curPageID.replaceAll(EphesoftProperty.PAGE.getProperty(), "");
					Long curPageIDLg = Long.parseLong(curPageID, RADIX_BASE);
					if (curPageIDLg > maxPageTypeID) {
						maxPageTypeID = curPageIDLg;
					}
				}
			}
		}

		String maxPgStr = Long.toString(maxPageTypeID + 1L);

		return maxPgStr;
	}

	/**
	 * @param docTypesList List<DocumentType>
	 * @return String next document type id.
	 * @throws DCMAApplicationException If any one of the parameter is null or empty.
	 */
	private String getNewDocumentTypeID(List<Document> docTypesList) throws DCMAApplicationException {

		String errMsg = null;

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Long maxDocumentTypeID = 1L;

		for (Document docType : docTypesList) {
			String curDocID = docType.getIdentifier();
			if (null == curDocID) {
				errMsg = "Document ID is null.";
				logger.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}

			curDocID = curDocID.replaceAll(EphesoftProperty.DOCUMENT.getProperty(), "");
			Long curPageIDLg = Long.parseLong(curDocID, RADIX_BASE);

			if (curPageIDLg > maxDocumentTypeID) {
				maxDocumentTypeID = curPageIDLg;
			}
		}

		String maxDocStr = Long.toString(maxDocumentTypeID + 1L);

		return maxDocStr;
	}

	/**
	 * An internal method to create duplicate pages and its physical contents.
	 * 
	 * @param duplicatePageType PageType
	 * @param batchInstanceIdentifier String
	 * @param newPageTypeID String
	 * @param batchInstanceFolderPath String
	 * @return PageType
	 * @throws DCMAApplicationException
	 */
	private Page duplicatePageFiles(final Page duplicatePageType, final String batchInstanceIdentifier, String newPageTypeID,
			final String batchInstanceFolderPath) throws DCMAApplicationException {
		// Create duplicatePageType for all the images present at this page
		// type.
		String errMsg = null;
		final Page duplicateCopyPageType = new Page();

		int fileFormatIndex = duplicatePageType.getDisplayFileName().lastIndexOf(BatchConstants.DOT);
		String fileFormat = duplicatePageType.getDisplayFileName().substring(fileFormatIndex);

		duplicateCopyPageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + newPageTypeID);

		File fileName = new File(EphesoftStringUtil.concatenate(batchInstanceFolderPath, File.separator, batchInstanceIdentifier,
				BatchConstants.UNDER_SCORE, duplicateCopyPageType.getIdentifier(), fileFormat));

		while (fileName.exists()) {
			Long pageIDLg = Long.parseLong(newPageTypeID, RADIX_BASE);
			newPageTypeID = Long.toString(pageIDLg + 1L);
			duplicateCopyPageType.setIdentifier(EphesoftProperty.PAGE.getProperty() + newPageTypeID);
			fileName = new File(EphesoftStringUtil.concatenate(batchInstanceFolderPath, File.separator, batchInstanceIdentifier,
					BatchConstants.UNDER_SCORE, duplicateCopyPageType.getIdentifier(), fileFormat));
		}

		duplicateCopyPageType.setOldFileName(BatchConstants.EMPTY);
		duplicateCopyPageType.setDirection(duplicatePageType.getDirection());
		duplicateCopyPageType.setIsRotated(duplicatePageType.isIsRotated());
		final PageLevelFields pPageLevelFields = duplicatePageType.getPageLevelFields();
		if (null != pPageLevelFields) {
			final PageLevelFields copyPageLevelFields = new PageLevelFields();
			final List<DocField> pageLevelField = pPageLevelFields.getPageLevelField();
			final List<DocField> copyPageLevelField = copyPageLevelFields.getPageLevelField();
			copyPageLevelField.addAll(pageLevelField);
			duplicateCopyPageType.setPageLevelFields(copyPageLevelFields);
		}

		final String newFileName = duplicatePageType.getNewFileName();
		String displayFileName = duplicatePageType.getDisplayFileName();
		final String hocrFileName = duplicatePageType.getHocrFileName();
		final String thumbnailFileName = duplicatePageType.getThumbnailFileName();
		final String oCRInputFileName = duplicatePageType.getOCRInputFileName();
		String originalXmlFile = batchInstanceFolderPath + File.separator + hocrFileName;

		FileNameFormatter fileNameFormatter = null;
		try {
			fileNameFormatter = new FileNameFormatter();
		} catch (IOException e) {
			errMsg = BatchConstants.FILE_NAME_FORMATTER_NOT_CREATED;
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg, e);
		}
		setNewFileName(batchInstanceFolderPath, duplicateCopyPageType, newFileName, fileNameFormatter, batchInstanceIdentifier,
				newPageTypeID);

		String newPageName = BatchConstants.PAGE_ID_PREFIX + newPageTypeID;
		setDisplayFileName(batchInstanceFolderPath, duplicateCopyPageType, displayFileName, fileNameFormatter,
				batchInstanceIdentifier, newPageName);

		setThumbnailFileName(batchInstanceFolderPath, duplicateCopyPageType, thumbnailFileName, fileNameFormatter,
				batchInstanceIdentifier, newPageName);

		// setOCRFileName(batchInstanceFolderPath, duplicateCopyPageType, oCRInputFileName, fileNameFormatter, batchInstanceIdentifier,
		// newPageName);

		if (null != originalXmlFile) {
			setHocrFileName(batchInstanceIdentifier, batchInstanceFolderPath, duplicateCopyPageType, newFileName, hocrFileName,
					originalXmlFile, newPageName);
		}

		return duplicateCopyPageType;
	}

	/**
	 * API to set HOCR file name.
	 * 
	 * @param batchInstanceIdentifier String
	 * @param batchInstanceFolderPath String
	 * @param duplicateCopyPageType Page
	 * @param newFileName String
	 * @param hocrFileName String
	 * @param originalXmlFile String
	 * @param newPageName String
	 */
	private void setHocrFileName(final String batchInstanceIdentifier, final String batchInstanceFolderPath,
			final Page duplicateCopyPageType, final String newFileName, final String hocrFileName, String originalXmlFile,
			String newPageName) {
		String errMsg;
		String hocrFilePath = null;
		FileOutputStream xmlOutputStream = null;
		try {

			int index = newFileName.lastIndexOf(BatchConstants.DOT);
			String newFileNameWithoutExt = newFileName.substring(0, index);
			String str = new StringBuilder(newFileNameWithoutExt + FileType.HOCR_XML.getExtension()).toString();

			if (str.equals(hocrFileName)) {
				newPageName = newPageName.replaceAll(EphesoftProperty.PAGE.getProperty(), "");
			}

			String duplicateHocrFileName = EphesoftStringUtil.concatenate(batchInstanceIdentifier, BatchConstants.UNDER_SCORE,
					newPageName, FileType.HOCR_XML.getExtension());
			String xmlFileName = EphesoftStringUtil.concatenate(batchInstanceFolderPath, File.separator, duplicateHocrFileName);

			duplicateCopyPageType.setHocrFileName(duplicateHocrFileName);

			hocrFilePath = xmlFileName.toString();

			final String zipFileName = originalXmlFile + FileType.ZIP.getExtensionWithDot();
			File srcFileHocrFileName = new File(zipFileName);
			if (srcFileHocrFileName.exists()) {
				originalXmlFile = zipFileName;
				EphesoftStringUtil.concatenate(xmlFileName, FileType.ZIP.getExtensionWithDot());
			} else {
				srcFileHocrFileName = new File(originalXmlFile);
			}
			final File destFileHocrFileName = new File(xmlFileName.toString());
			logger.info("srcFileHocrFileName : " + srcFileHocrFileName);
			logger.info("destFileHocrFileName : " + destFileHocrFileName);
			FileUtils.copyFile(srcFileHocrFileName, destFileHocrFileName, false);
			SAXBuilder documentBuilder = new SAXBuilder();

			org.jdom.Document xmlDocument = documentBuilder.build(destFileHocrFileName);
			String xmlSignature = xmlSignatureService.generateHOCRSignature(HOCRGenerationPlugin.TESSERACT_HOCR.toString(),
					destFileHocrFileName.getName());
			final Element signatureElement = xmlDocument.getRootElement().getChild(BatchConstants.HOCR_SIGNATURE_ELEMENT);
			if (signatureElement != null) {
				signatureElement.setText(xmlSignature);
				XMLOutputter outputter = new XMLOutputter();
				xmlOutputStream = new FileOutputStream(destFileHocrFileName);
				outputter.output(xmlDocument, xmlOutputStream);
			}
		} catch (final IOException e) {
			errMsg = "Not able to create duplicate the xml file  " + originalXmlFile + " " + e.getMessage();
			logger.error(errMsg);
		} catch (final Exception exception) {
			logger.error("could not create the duplicate file ", exception);
		} finally {
			IOUtils.closeQuietly(xmlOutputStream);
		}
		try {
			final HocrPages hocrPages = getHOCR(hocrFilePath);
			if (null != hocrPages) {
				final HocrPage pages = hocrPages.getHocrPage().get(0);
				if (pages != null) {
					pages.setPageID(duplicateCopyPageType.getIdentifier());
					update(hocrPages, batchInstanceIdentifier, newPageName);
				}
			}
		} catch (final Exception e) {
			logger.error("Error in updating the xml file for new page type " + e.getMessage());
		}
	}

	/**
	 * API to set OCR file name.
	 * 
	 * @param batchInstanceFolderPath {@link String}
	 * @param duplicateCopyPage {@link Page}
	 * @param oCRInputFileName {@link String}
	 * @param fileNameFormatter {@link FileNameFormatter}
	 * @param batchInstanceId {@link String}
	 * @param newPageTypeID {@link String}
	 */
	private void setOCRFileName(final String batchInstanceFolderPath, final Page duplicateCopyPage, final String oCRInputFileName,
			final FileNameFormatter fileNameFormatter, final String batchInstanceId, final String newPageTypeID) {
		String errMsg;
		if (null != oCRInputFileName) {
			final String localOCRInputFileName = oCRInputFileName.trim();

			try {
				final String oCRInputFileNameFormatter = fileNameFormatter.getOCRInputFileName(String.valueOf(batchInstanceId), null,
						null, BatchConstants.PNG_EXTENSION, String.valueOf(newPageTypeID));
				duplicateCopyPage.setOCRInputFileName(oCRInputFileNameFormatter);
			} catch (final FileFormatException e) {
				logger.error(e.getMessage());
				duplicateCopyPage.setOCRInputFileName(newPageTypeID + BatchConstants.UNDER_SCORE + localOCRInputFileName);
			}
			try {
				final File srcFileOCRInputFileName = new File(batchInstanceFolderPath + File.separator + localOCRInputFileName);
				final File destFileOCRInputFileName = new File(batchInstanceFolderPath + File.separator
						+ duplicateCopyPage.getOCRInputFileName());
				logger.info("srcFileOCRInputFileName : " + srcFileOCRInputFileName);
				logger.info("destFileOCRInputFileName : " + destFileOCRInputFileName);
				FileUtils.copyFile(srcFileOCRInputFileName, destFileOCRInputFileName, false);
			} catch (final IOException e) {
				errMsg = "Not able to create duplicate the file oCRInputFileName: " + localOCRInputFileName + " " + e.getMessage();
				logger.error(errMsg);
			} catch (final Exception exception) {
				logger.error("Error occured while duplicating the pages ", exception);
			}
		}
	}

	/**
	 * API to set Thumbnail file name.
	 * 
	 * @param batchInstanceFolderPath {@link String}
	 * @param duplicateCopyPageType {@link Page}
	 * @param thumbnailFileName {@link String}
	 * @param fileNameFormatter {@link FileNameFormatter}
	 * @param batchInstanceIdentifier {@link String}
	 * @param newPageTypeID {@link String}
	 */
	private void setThumbnailFileName(final String batchInstanceFolderPath, final Page duplicateCopyPageType,
			final String thumbnailFileName, final FileNameFormatter fileNameFormatter, final String batchInstanceIdentifier,
			final String newPageTypeID) {
		String errMsg;
		if (null != thumbnailFileName) {
			final String localThumbnailFileName = thumbnailFileName.trim();
			try {
				final String thumbnailFileNameFormatter = fileNameFormatter.getDisplayThumbnailFileName(
						String.valueOf(batchInstanceIdentifier), null, null, null, BatchConstants.PNG_EXTENSION,
						String.valueOf(newPageTypeID));
				duplicateCopyPageType.setThumbnailFileName(thumbnailFileNameFormatter);
			} catch (final FileFormatException e) {
				logger.error(e.getMessage());
				duplicateCopyPageType.setThumbnailFileName(newPageTypeID + BatchConstants.UNDER_SCORE + localThumbnailFileName);
			}
			try {
				final File srcFileThumbnailFileName = new File(batchInstanceFolderPath + File.separator + localThumbnailFileName);
				final File destFileThumbnailFileName = new File(batchInstanceFolderPath + File.separator
						+ duplicateCopyPageType.getThumbnailFileName());
				logger.info("srcFileThumbnailFileName : " + srcFileThumbnailFileName);
				logger.info("destFileThumbnailFileName : " + destFileThumbnailFileName);
				FileUtils.copyFile(srcFileThumbnailFileName, destFileThumbnailFileName, false);
				if (duplicateCopyPageType.isIsRotated()) {
					final File destFileThumbnailDirectionFileName = new File(batchInstanceFolderPath + File.separator
							+ duplicateCopyPageType.getDirection().toString() + File.separator
							+ duplicateCopyPageType.getThumbnailFileName());
					FileUtils.copyFile(srcFileThumbnailFileName, destFileThumbnailDirectionFileName, false);
				}

			} catch (final IOException e) {
				errMsg = "Not able to create duplicate the file thumbnailFileName: " + localThumbnailFileName + " " + e.getMessage();
				logger.error(errMsg);
			}
		}
	}

	/**
	 * API to set Display file name.
	 * 
	 * @param batchInstanceFolderPath {@link String}
	 * @param duplicateCopyPageType {@link Page}
	 * @param displayFileName {@link String}
	 * @param fileNameFormatter {@link FileNameFormatter}
	 * @param batchInstanceIdentifier {@link String}
	 * @param newPageTypeID {@link String}
	 */
	private void setDisplayFileName(final String batchInstanceFolderPath, final Page duplicateCopyPageType,
			final String displayFileName, final FileNameFormatter fileNameFormatter, final String batchInstanceIdentifier,
			final String newPageTypeID) {
		String errMsg;
		if (null != displayFileName) {
			final String localDisplayFileName = displayFileName.trim();
			try {
				final String displayFileNameFormatter = fileNameFormatter.getOCRInputFileName(String.valueOf(batchInstanceIdentifier),
						null, null, BatchConstants.PNG_EXTENSION, String.valueOf(newPageTypeID));
				duplicateCopyPageType.setDisplayFileName(displayFileNameFormatter);
			} catch (final FileFormatException e) {
				logger.error(e.getMessage());
				duplicateCopyPageType.setDisplayFileName(newPageTypeID + BatchConstants.UNDER_SCORE + localDisplayFileName);
			}
			// create the copy of each file.
			try {
				final File srcFileDisplayFileName = new File(batchInstanceFolderPath + File.separator + localDisplayFileName);
				final File destFileDisplayFileName = new File(batchInstanceFolderPath + File.separator
						+ duplicateCopyPageType.getDisplayFileName());
				logger.info("srcFileDisplayFileName : " + srcFileDisplayFileName);
				logger.info("destFileDisplayFileName : " + destFileDisplayFileName);
				FileUtils.copyFile(srcFileDisplayFileName, destFileDisplayFileName, false);
				if (duplicateCopyPageType.isIsRotated()) {
					final File destFileDisplayDirectionFileName = new File(batchInstanceFolderPath + File.separator
							+ duplicateCopyPageType.getDirection().toString() + File.separator
							+ duplicateCopyPageType.getDisplayFileName());
					FileUtils.copyFile(srcFileDisplayFileName, destFileDisplayDirectionFileName, false);
				}
			} catch (final IOException e) {
				errMsg = "Not able to create duplicate the file displayFileName: " + localDisplayFileName + " " + e.getMessage();
				logger.error(errMsg);
			}
		}
	}

	/**
	 * API to set new file name.
	 * 
	 * @param batchInstanceFolderPath {@link String}
	 * @param duplicateCopyPageType {@link Page}
	 * @param newFileName {@link String}
	 * @param fileNameFormatter {@link FileNameFormatter}
	 * @param batchInstanceIdentifier {@link String}
	 * @param newPageTypeID {@link String}
	 */
	private void setNewFileName(final String batchInstanceFolderPath, final Page duplicateCopyPageType, final String newFileName,
			final FileNameFormatter fileNameFormatter, final String batchInstanceIdentifier, final String newPageTypeID) {
		String errMsg;
		if (null != newFileName) {
			final String localNewFileName = newFileName.trim();
			try {
				final String newFileNameFormatter = fileNameFormatter.getNewFileName(String.valueOf(batchInstanceIdentifier), null,
						String.valueOf(newPageTypeID), BatchConstants.TIF_EXTENSION);
				duplicateCopyPageType.setNewFileName(newFileNameFormatter);
			} catch (final FileFormatException e) {
				logger.error(e.getMessage());
				duplicateCopyPageType.setNewFileName(newPageTypeID + BatchConstants.UNDER_SCORE + localNewFileName);
			}
			// create the copy of each file.
			try {
				final File srcFileNewFileName = new File(batchInstanceFolderPath + File.separator + localNewFileName);
				final File destFileNewFileName = new File(batchInstanceFolderPath + File.separator
						+ duplicateCopyPageType.getNewFileName());
				logger.info("srcFileNewFileName : " + srcFileNewFileName);
				logger.info("destFileNewFileName : " + destFileNewFileName);
				FileUtils.copyFile(srcFileNewFileName, destFileNewFileName, false);
			} catch (final IOException e) {
				errMsg = "Not able to create duplicate the file newFileName: " + localNewFileName + " " + e.getMessage();
				logger.error(errMsg);
			}
		}
	}

	/**
	 * Data carrier.
	 * 
	 * @authorEphesoftft.
	 * 
	 */
	private class DocPageCarrier {

		private int pageIndex;
		private int docIndex;
		private List<Document> docTypesList;

		/**
		 * @return the pageIndex
		 */
		public int getPageIndex() {
			return pageIndex;
		}

		/**
		 * @param pageIndex the pageIndex to set
		 */
		public void setPageIndex(int pageIndex) {
			this.pageIndex = pageIndex;
		}

		/**
		 * @return the docIndex
		 */
		public int getDocIndex() {
			return docIndex;
		}

		/**
		 * @param docIndex the docIndex to set
		 */
		public void setDocIndex(int docIndex) {
			this.docIndex = docIndex;
		}

		/**
		 * @return the docTypesList
		 */
		public List<Document> getDocTypesList() {
			return docTypesList;
		}

		/**
		 * @param docTypesList the docTypesList to set
		 */
		public void setDocTypesList(List<Document> docTypesList) {
			this.docTypesList = docTypesList;
		}

	}

	/**
	 * An api to get the URL object for the batch ID.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return URL object.
	 */
	@Override
	public URL getBatchContextURL(String batchInstanceIdentifier) {
		try {
			return new URL(this.getBatchFolderURL() + "/" + batchInstanceIdentifier);
		} catch (MalformedURLException e) {
			throw new DCMABusinessException(e.getMessage(), e);
		}
	}

	/**
	 * An api which will return true if the review is required other wise false.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return boolean true if isReviewRequired other wise false.
	 * @throws DCMAApplicationException If any of the parameter is not valid.
	 */
	@Override
	public boolean isReviewRequired(final String batchInstanceIdentifier, boolean checkReviewFlag) throws DCMAApplicationException {
		return isReviewRequired(batchInstanceIdentifier, checkReviewFlag, false);

	}

	private boolean isReviewRequired(String batchInstanceIdentifier, boolean checkReviewFlag, boolean setDocumentReviewStatus)
			throws DCMAApplicationException {

		boolean isReviewRequired = false;

		String errMsg = null;

		if (null == batchInstanceIdentifier) {
			errMsg = "Input parameter batchInstanceIdentifier is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = getBatch(batchInstanceIdentifier);

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}
		boolean isBatchReviewed = true;
		for (Document document : docTypesList) {
			if (!document.isReviewed()) {
				isBatchReviewed = false;
			}
		}

		if (!isBatchReviewed) {
			if (!setDocumentReviewStatus) {
				for (Document document : docTypesList) {
					String docType = document.getType();
					if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
						isReviewRequired = true;
						break;
					}
					if (checkReviewFlag) {
						isReviewRequired = !document.isReviewed();
						if (isReviewRequired) {
							break;
						}
					} else {
						float confidence = document.getConfidence();
						float confidenceThreshold = document.getConfidenceThreshold();
						if (confidenceThreshold >= confidence) {
							isReviewRequired = true;
							break;
						}
					}
				}
			} else {
				for (Document document : docTypesList) {
					String docType = document.getType();
					if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
						document.setReviewed(false);
						isReviewRequired = true;
					} else {
						float confidence = document.getConfidence();
						float confidenceThreshold = document.getConfidenceThreshold();
						if (confidenceThreshold >= confidence) {
							document.setReviewed(false);
							isReviewRequired = true;
						} else {
							document.setReviewed(true);
						}
					}
				}
				updateBatch(batch);
			}
		}
		return isReviewRequired;
	}

	/**
	 * An api which will return true if the review is required other wise false.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return boolean true if isReviewRequired other wise false.
	 * @throws DCMAApplicationException If any of the parameter is not valid.
	 */
	@Override
	public boolean isReviewRequired(final String batchInstanceIdentifier) throws DCMAApplicationException {
		return isReviewRequired(batchInstanceIdentifier, false, true);
	}

	/**
	 * An api which will return true if the validation is required for the document level fields other wise false.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return boolean true if isValidationRequired other wise false.
	 * @throws DCMAApplicationException If any of the parameter is not valid.
	 */
	@Override
	public boolean isValidationRequired(final String batchInstanceIdentifier) throws DCMAApplicationException {

		boolean isValidationRequired = false;

		String errMsg = null;

		if (null == batchInstanceIdentifier) {
			errMsg = "Input parameter batchInstanceIdentifier is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = getBatch(batchInstanceIdentifier);

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		for (Document document : docTypesList) {
			// Add a check to insure that no document have document type "Unknown"
			// We can remove this check if isReviewRequired is handled properly.
			String docType = document.getType();
			if (null != docType && docType.equals(EphesoftProperty.UNKNOWN.getProperty())) {
				isValidationRequired = true;
				break;
			}
			// End of check.
			boolean b = document.isValid();
			if (!b) {
				isValidationRequired = true;
				break;
			}
		}

		return isValidationRequired;
	}

	/**
	 * An api to generate all the folder structure for samples.
	 * 
	 * @param batchClassIDList List<List<String>>
	 * @throws DCMAApplicationException If any of the parameter is not valid.
	 */
	@Override
	public void sampleGeneration(final List<List<String>> batchIdDocPgNameList) throws DCMAApplicationException {
		String errMsg = null;
		String baseSampleFdLoc = batchSchemaDao.getJAXB2Template().getBaseSampleFdLoc();
		String sampleFolders = batchSchemaDao.getJAXB2Template().getSampleFolders();

		if (null == batchIdDocPgNameList || batchIdDocPgNameList.isEmpty() || null == baseSampleFdLoc || null == sampleFolders) {
			errMsg = "Input parameter batchClassIDList are null or empty.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		String[] nameOfFoldersArr = sampleFolders.split(BatchConstants.SEMI_COLON);
		boolean isFodlerNameAdded = true;
		for (String fdName : nameOfFoldersArr) {
			String dirPath = baseSampleFdLoc;
			for (List<String> batchIdDocPgName : batchIdDocPgNameList) {
				StringBuilder subDirPath = new StringBuilder();
				subDirPath.append(dirPath);
				isFodlerNameAdded = true;
				for (String dirName : batchIdDocPgName) {
					if (null == dirName) {
						break;
					}
					subDirPath.append(File.separator);
					subDirPath.append(dirName);
					if (isFodlerNameAdded) {
						subDirPath.append(File.separator);
						subDirPath.append(fdName);
						isFodlerNameAdded = false;
					}
				}
				String strManyDirectories = subDirPath.toString();
				try {
					// Create multiple directories
					boolean success = new File(strManyDirectories).mkdirs();
					if (success) {
						logger.info("Directories: " + strManyDirectories + " created.");
					} else {
						logger.info("Directories: " + strManyDirectories + " not created.");
					}
				} catch (Exception e) {
					logger.info("Directories: " + strManyDirectories + " not created.");
					logger.error(e.getMessage());
				}
			}
		}
	}

	@Override
	public void deleteDocTypeFolder(List<String> docTypeNameList, String batchClassIdentifier) throws DCMAApplicationException {
		String errMsg = null;
		String baseSampleFdLoc = batchSchemaDao.getJAXB2Template().getBaseSampleFdLoc();
		String sampleFolders = batchSchemaDao.getJAXB2Template().getSampleFolders();
		// JIRA bug - folder of document type is not deleted from "test-advanced-extraction" if document type is deleted and generate
		// folder button is clicked.
		String testAdvanceExtractionFolder = batchSchemaDao.getJAXB2Template().getTestAdvancedKvExtractionFolderName();
		sampleFolders = EphesoftStringUtil.concatenate(sampleFolders, BatchConstants.SEMI_COLON, testAdvanceExtractionFolder);

		if (null == docTypeNameList || docTypeNameList.isEmpty() || null == baseSampleFdLoc || null == sampleFolders) {
			errMsg = "Input parameter docTypeNameList are null or empty.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		String[] nameOfFoldersArr = sampleFolders.split(BatchConstants.SEMI_COLON);
		StringBuilder docTypeDirPath = new StringBuilder(baseSampleFdLoc);
		docTypeDirPath.append(File.separator);
		docTypeDirPath.append(batchClassIdentifier);

		String batchClassFolderPath = docTypeDirPath.toString();
		String docTypePath = null;
		String baseFolPath = null;
		for (String fdName : nameOfFoldersArr) {
			baseFolPath = batchClassFolderPath + File.separator + fdName;
			for (String docTypeName : docTypeNameList) {
				docTypePath = baseFolPath + File.separator + docTypeName;
				docTypePath = docTypePath.trim();
				try {
					File docTypeFileName = new File(docTypePath);
					logger.info("docTypeFileName : " + docTypeFileName);
					FileUtils.forceDelete(docTypeFileName);
				} catch (IOException e) {
					errMsg = "Not able to delete the file docTypeFilePath: " + docTypePath + " " + e.getMessage();
					logger.info(errMsg);
				}
			}

		}

	}

	@Override
	public String getThumbnailFilePath(String batchInstanceIdentifier, String documentId, String pageId)
			throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == documentId || null == pageId) {
			errMsg = "Input parameters batchInstanceIdentifier/documentId/pageId are null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = batchSchemaDao.get(batchInstanceIdentifier);

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		int docIndex = getDocumentTypeIndex(docTypesList, documentId);

		int pageIndex = getPageTypeIndex(docTypesList, docIndex, pageId);

		Document docType = docTypesList.get(docIndex);
		Pages pages = docType.getPages();
		List<Page> pageTypeList = pages.getPage();
		Page page = pageTypeList.get(pageIndex);
		String thumbnailFileName = page.getThumbnailFileName();

		String thumbnailPath = getLocalFolderLocation() + File.separator + batchInstanceIdentifier + File.separator
				+ thumbnailFileName;

		return thumbnailPath;
	}

	@Override
	public String getDisplayImageFilePath(String batchInstanceIdentifier, String documentId, String pageId)
			throws DCMAApplicationException {

		String errMsg = null;

		if (null == batchInstanceIdentifier || null == documentId || null == pageId) {
			errMsg = "Input parameters batchInstanceIdentifier/documentId/pageId are null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		Batch batch = batchSchemaDao.get(batchInstanceIdentifier);

		List<Document> docTypesList = batch.getDocuments().getDocument();

		if (null == docTypesList) {
			errMsg = "docTypesList is null.";
			logger.error(errMsg);
			throw new DCMAApplicationException(errMsg);
		}

		int docIndex = getDocumentTypeIndex(docTypesList, documentId);

		int pageIndex = getPageTypeIndex(docTypesList, docIndex, pageId);

		Document docType = docTypesList.get(docIndex);
		Pages pages = docType.getPages();
		List<Page> pageTypeList = pages.getPage();
		Page page = pageTypeList.get(pageIndex);
		String displayFileName = page.getDisplayFileName();

		String displayFilePath = getLocalFolderLocation() + File.separator + batchInstanceIdentifier + File.separator
				+ displayFileName;

		return displayFilePath;
	}

	/**
	 * An api to generate the xml file for all the htmls generated by image process plug-ins.
	 * 
	 * @return batchInstanceIdentifier String
	 * @throws DCMAException If any error occurs.
	 */
	public void htmlToXmlGeneration(final String batchInstanceIdentifier) throws DCMAException {
		try {
			htmlOutputGeneration(batchInstanceIdentifier);
		} catch (DCMABusinessException e) {
			logger.error(e.getMessage());
			throw new DCMAException(e.getMessage(), e);
		} catch (DCMAApplicationException e) {
			logger.error(e.getMessage());
			throw new DCMAException(e.getMessage(), e);
		}
	}

	/**
	 * @param batchInstanceIdentifier String
	 * @throws DCMAApplicationException
	 * @throws DCMABusinessException
	 */
	private void htmlOutputGeneration(final String batchInstanceIdentifier) throws DCMAApplicationException, DCMABusinessException {
		String actualFolderLocation = getLocalFolderLocation() + File.separator + batchInstanceIdentifier + File.separator;

		Batch batch = getBatch(batchInstanceIdentifier);
		String outputFileName = "tempFile";
		String outputFilePath = null;

		List<Document> xmlDocuments = batch.getDocuments().getDocument();
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread(batchInstanceIdentifier);
		outputFilePath = actualFolderLocation + outputFileName;
		String outputFilePathLocal = null;
		if (null != xmlDocuments) {
			for (Document document : xmlDocuments) {
				List<Page> listOfPages = document.getPages().getPage();
				if (null != listOfPages) {
					for (Page page : listOfPages) {
						if (page.getHocrFileName() == null) {
							outputFilePathLocal = outputFilePath + page.getIdentifier();
							hOCRGenerationUsingThreadpool(batchInstanceThread, batchInstanceIdentifier, actualFolderLocation,
									outputFilePathLocal, page);
						} else {
							String fileName = page.getHocrFileName();
							outputFilePathLocal = actualFolderLocation + fileName;
							if (!new File(outputFilePathLocal).exists() || !fileName.endsWith(FileType.HOCR_XML.getExtension())) {
								outputFilePathLocal = outputFilePath + page.getIdentifier();
								hOCRGenerationUsingThreadpool(batchInstanceThread, batchInstanceIdentifier, actualFolderLocation,
										outputFilePathLocal, page);
							}
						}
					}
				}
			}
			try {
				logger.info("Creating HOCR for all pages using threadpool.");
				batchInstanceThread.execute();
				logger.info("HOCRgenerated succussfully for all pages.");
			} catch (DCMAApplicationException dcmae) {
				logger.error("Error while genmerating hOCR from html using threadpool");
				batchInstanceThread.remove();
				throw new DCMAApplicationException("Error while genmerating hOCR from html using threadpool ." + dcmae.getMessage(),
						dcmae);
			}
		}

	}

	private void hOCRGenerationUsingThreadpool(final BatchInstanceThread batchInstanceThread, final String batchInstanceIdentifier,
			final String actualFolderLocation, final String outputFilePath, final Page page) {
		batchInstanceThread.add(new AbstractRunnable() {

			@Override
			public void run() {
				String pageID;
				String hocrFileName;
				String pathOfHOCRFile;
				FileInputStream inputStream = null;
				HocrPages hocrPages = new HocrPages();
				List<HocrPage> hocrPageList = hocrPages.getHocrPage();

				HocrPage hocrPage = new HocrPage();
				pageID = page.getIdentifier();
				hocrPage.setPageID(pageID);
				hocrPageList.add(hocrPage);
				hocrFileName = page.getHocrFileName();
				pathOfHOCRFile = actualFolderLocation + hocrFileName;
				String generatedHOCRFileName = null;
				if (null == pageID) {
					generatedHOCRFileName = EphesoftStringUtil.concatenate(batchInstanceIdentifier, FileType.HOCR_XML.getExtension());
				} else {
					generatedHOCRFileName = EphesoftStringUtil.concatenate(batchInstanceIdentifier, "_", pageID,
							FileType.HOCR_XML.getExtension());
				}
				logger.info("Creating hOCR for page : " + pageID);
				try {
					inputStream = hocrGenerationInternal(actualFolderLocation, outputFilePath, pageID, pathOfHOCRFile, hocrPage);
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				} finally {
					try {
						if (inputStream != null) {
							inputStream.close();
						}
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
				try {
					String xmlSignature = xmlSignatureService.generateHOCRSignature(HOCRGenerationPlugin.TESSERACT_HOCR.toString(),
							generatedHOCRFileName);
					hocrPages.setSignature(xmlSignature);
				} catch (final XMLSignatureException xmlSignatureException) {
					logger.error("Could not generate the signature for the XML File");
				}
				createHocr(hocrPages, batchInstanceIdentifier, pageID);
				try {
					logger.info("Deleting temp file : " + outputFilePath);
					FileUtils.forceDelete(new File(outputFilePath));
				} catch (IOException e) {
					logger.info("Deleting the temp file." + e.getMessage());
				}
			}

		});
	}

	@Override
	public void hocrGenerationAPI(final String workingDir, String pageID, String pathOfHOCRFile, HocrPage hocrPage)
			throws FileNotFoundException, IOException, Exception {
		String outputFilePath = workingDir + File.separator + "tempFile" + pageID;
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = hocrGenerationInternal(workingDir, outputFilePath, pageID, pathOfHOCRFile, hocrPage);
		} finally {
			if (fileInputStream != null) {
				fileInputStream.close();
			}
		}
	}

	/**
	 * Method extracted to be reused for Ephesoft Web Services.
	 * 
	 * @param actualFolderLocation
	 * @param outputFilePath
	 * @param pageID
	 * @param pathOfHOCRFile
	 * @param hocrPage
	 * @return
	 * @throws Exception
	 */
	public FileInputStream hocrGenerationInternal(final String actualFolderLocation, final String outputFilePath, String pageID,
			String pathOfHOCRFile, HocrPage hocrPage) throws Exception {

		// if (isTesseractBatch
		// && tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
		XMLUtil.htmlOutputStream(pathOfHOCRFile, outputFilePath);
		OCREngineUtil.formatHOCRForTesseract(outputFilePath, actualFolderLocation, pageID);
		// } else {
		// XMLUtil.htmlOutputStream(pathOfHOCRFile, outputFilePath);
		// }
		FileInputStream inputStream = new FileInputStream(outputFilePath);
		org.w3c.dom.Document doc = XMLUtil.createDocumentFrom(inputStream);
		NodeList titleNodeList = doc.getElementsByTagName("title");
		if (null != titleNodeList) {
			for (int index = 0; index < titleNodeList.getLength(); index++) {
				Node node = titleNodeList.item(index);
				NodeList childNodeList = node.getChildNodes();
				if (childNodeList.getLength() > 0) {
					Node n = childNodeList.item(0);
					if (null != n) {
						String value = n.getNodeValue();
						if (value != null) {
							hocrPage.setTitle(value);
							break;
						}
					}
				} else {
					hocrPage.setTitle("");
				}
			}
		}

		NodeList spanNodeList = doc.getElementsByTagName("span");
		Spans spans = new Spans();
		hocrPage.setSpans(spans);
		List<Span> spanList = spans.getSpan();
		if (null != spanNodeList) {
			StringBuilder hocrContent = new StringBuilder();
			for (int index = 0; index < spanNodeList.getLength(); index++) {
				Node node = spanNodeList.item(index);
				NodeList childNodeList = node.getChildNodes();
				Node n = childNodeList.item(0);
				Span span = new Span();
				if (null != n) {
					String value = n.getNodeValue();
					span.setValue(value);
					hocrContent.append(value);
					hocrContent.append(" ");
				}
				spanList.add(span);
				NamedNodeMap map = node.getAttributes();
				Node nMap = map.getNamedItem("title");
				Coordinates hocrCoordinates = null;
				try {
					String coordinates = nMap.getNodeValue();
					String[] arr = coordinates.split(" ");
					if (null != arr && arr.length >= 4) {
						hocrCoordinates = new Coordinates();
						hocrCoordinates.setX0(new BigInteger(arr[1]));
						hocrCoordinates.setX1(new BigInteger(arr[3]));
						hocrCoordinates.setY0(new BigInteger(arr[2]));
						hocrCoordinates.setY1(new BigInteger(arr[4]));
					}
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				if (null == hocrCoordinates) {
					hocrCoordinates = new Coordinates();
					hocrCoordinates.setX0(BigInteger.ZERO);
					hocrCoordinates.setX1(BigInteger.ZERO);
					hocrCoordinates.setY0(BigInteger.ZERO);
					hocrCoordinates.setY1(BigInteger.ZERO);
				}
				span.setCoordinates(hocrCoordinates);
			}
			hocrPage.setHocrContent(hocrContent.toString());
		}
		return inputStream;
	}

	/**
	 * This api is used to generate the HocrPage object for input hocr file.
	 * 
	 * @param pageName {@link String}
	 * @param pathOfHOCRFile {@link String}
	 * @param outputFilePath {@link String}
	 * @return {@link HocrPage}
	 */
	@Override
	public HocrPage generateHocrPage(String pageName, String pathOfHOCRFile, String outputFilePath, String batchClassIdentifier,
			String ocrEngineName) {

		if (null == pathOfHOCRFile || null == outputFilePath) {
			return null;
		}
		BatchPluginConfiguration[] pluginConfiguration = batchClassPluginPropertiesService.getPluginProperties(batchClassIdentifier,
				TESSERACT_HOCR_PLUGIN, TesseractVersionProperty.TESSERACT_VERSIONS);
		String tesseractVersion = "";
		if (pluginConfiguration != null && pluginConfiguration.length > 0 && pluginConfiguration[0].getValue() != null
				&& pluginConfiguration[0].getValue().length() > 0) {
			tesseractVersion = pluginConfiguration[0].getValue();
		}
		HocrPage hocrPage = new HocrPage();
		hocrPage.setPageID(pageName);
		FileInputStream inputStream = null;
		try {
			if (ocrEngineName.equalsIgnoreCase(IUtilCommonConstants.TESSERACT_HOCR_PLUGIN)
					&& tesseractVersion.equalsIgnoreCase(TesseractVersionProperty.TESSERACT_VERSION_3.getPropertyKey())) {
				XMLUtil.htmlOutputStream(pathOfHOCRFile, outputFilePath);
				String actualFolderLocation = new File(outputFilePath).getParent();
				OCREngineUtil.formatHOCRForTesseract(outputFilePath, actualFolderLocation, pageName);
			} else {
				XMLUtil.htmlOutputStream(pathOfHOCRFile, outputFilePath);
			}
			inputStream = new FileInputStream(outputFilePath);
			org.w3c.dom.Document doc = XMLUtil.createDocumentFrom(inputStream);
			NodeList titleNodeList = doc.getElementsByTagName("title");
			if (null != titleNodeList) {
				for (int index = 0; index < titleNodeList.getLength(); index++) {
					Node node = titleNodeList.item(index);
					NodeList childNodeList = node.getChildNodes();
					Node n = childNodeList.item(0);
					if (null != n) {
						String value = n.getNodeValue();
						if (value != null) {
							hocrPage.setTitle(value);
							break;
						}
					}
				}
			}

			NodeList spanNodeList = doc.getElementsByTagName("span");
			Spans spans = new Spans();
			hocrPage.setSpans(spans);
			List<Span> spanList = spans.getSpan();
			if (null != spanNodeList) {
				StringBuilder hocrContent = new StringBuilder();
				for (int index = 0; index < spanNodeList.getLength(); index++) {
					Node node = spanNodeList.item(index);
					NodeList childNodeList = node.getChildNodes();
					Node n = childNodeList.item(0);
					Span span = new Span();
					if (null != n) {
						String value = n.getNodeValue();
						span.setValue(value);
						hocrContent.append(value);
						hocrContent.append(" ");
					}
					spanList.add(span);
					NamedNodeMap map = node.getAttributes();
					Node nMap = map.getNamedItem("title");
					Coordinates hocrCoordinates = null;
					try {
						String coordinates = nMap.getNodeValue();
						String[] arr = coordinates.split(" ");
						if (null != arr && arr.length >= 4) {
							hocrCoordinates = new Coordinates();
							hocrCoordinates.setX0(new BigInteger(arr[1]));
							hocrCoordinates.setX1(new BigInteger(arr[3]));
							hocrCoordinates.setY0(new BigInteger(arr[2]));
							hocrCoordinates.setY1(new BigInteger(arr[4]));
						}
					} catch (Exception e) {
						logger.error(e.getMessage(), e);
					}
					if (null == hocrCoordinates) {
						hocrCoordinates = new Coordinates();
						hocrCoordinates.setX0(BigInteger.ZERO);
						hocrCoordinates.setX1(BigInteger.ZERO);
						hocrCoordinates.setY0(BigInteger.ZERO);
						hocrCoordinates.setY1(BigInteger.ZERO);
					}
					span.setCoordinates(hocrCoordinates);
				}
				hocrPage.setHocrContent(hocrContent.toString());
			}
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return hocrPage;
	}

	@Override
	public String getWebScannerScannedImagesURL() {
		return getBaseHttpURL() + "/" + batchSchemaDao.getJAXB2Template().getWebScannerScannedImagesFolderPath();
	}

	@Override
	public String getWebScannerScannedImagesFolderPath() {
		return getBaseFolderLocation() + File.separator + batchSchemaDao.getJAXB2Template().getWebScannerScannedImagesFolderPath();
	}

	@Override
	public void createWebScannerFolder(String folderName) {
		String errMsg = null;
		File newDirectory = new File(getWebScannerScannedImagesFolderPath() + File.separator + folderName);
		try {
			FileUtils.forceMkdir(newDirectory);
		} catch (IOException e) {
			errMsg = "Not able to create the file " + folderName + " " + e.getMessage();
			logger.error(errMsg);
		}
		return;
	}

	@Override
	public void copyFolder(String sourcePath, String folderName, String batchClassID) throws DCMAApplicationException {
		String scannedImagesPath = sourcePath + File.separator + folderName;
		File scannedImagesDirectory = new File(scannedImagesPath);
		CustomFileFilter validFilesFilter = getValidFilesFilter();
		List<File> validFilesToBeCopied = null;
		try {
			validFilesToBeCopied = getFiles(scannedImagesPath, validFilesFilter);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		if (validFilesToBeCopied == null || validFilesToBeCopied.isEmpty()) {
			throw new DCMAApplicationException("No files scanned/uploaded.");
		}

		CustomFileFilter filesToBeDeletedFilter = new CustomFileFilter(true, FileType.TIF.getExtensionWithDot(),
				FileType.TIFF.getExtensionWithDot(), FileType.SER.getExtensionWithDot(), FileType.PDF.getExtensionWithDot());
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		File uncFolder = new File(batchClass.getUncFolder() + File.separator + folderName);
		try {
			for (File file : getFiles(scannedImagesPath, filesToBeDeletedFilter)) {
				try {
					FileUtils.forceDelete(file);
					logger.info("Deleting File : " + file.getAbsolutePath());
				} catch (Exception e) {
					logger.error("Unable to delete file : " + file.getAbsolutePath());
				}
			}

			// Create a file filter which accepts only valid files for a web scanner batch. It will include TIF, TIFF and SER files.
			FileFilter batchValidFilesfilter = new FileFilter() {

				@Override
				public boolean accept(File pathname) {
					final String fileName = pathname.getName().toLowerCase();
					return com.ephesoft.dcma.util.FileUtils.isFileExtensionValid(fileName, FileType.TIF.getExtension(),
							FileType.TIFF.getExtension(), FileType.SER.getExtension(), FileType.PDF.getExtension());
				}
			};

			/*
			 * Copy only valid files. i.e. TIF, TIFF and SER extension files. FIX for the issue where system tries to copy PNG files
			 * under creation or use to other directory. Whereas we were expecting that file to be deleted by now.
			 */
			logger.info("Copying directory " + scannedImagesDirectory.getAbsolutePath() + "to " + uncFolder.getAbsolutePath());
			FileUtils.copyDirectory(scannedImagesDirectory, uncFolder, batchValidFilesfilter);
			String uncSubFolder = uncFolder.getAbsolutePath();
			batchInstanceService.createBatchInstance(batchClass, uncSubFolder, getLocalFolderLocation(), batchClass.getPriority());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
		try {
			/*
			 * Try to delete the scanned images folder. Try mechanism used to delete it twice. If still cannot delete then just log the
			 * error and move on.
			 */
			logger.info("Deleting directory :" + scannedImagesDirectory.getAbsolutePath());
			FileUtils.forceDelete(scannedImagesDirectory);
		} catch (IOException ioExcpetion1) {
			logger.error(ioExcpetion1.getMessage(), ioExcpetion1);

			logger.info("Trying to delete the scanned images folder " + scannedImagesDirectory.getAbsolutePath() + " 2nd time.");
			logger.info("Deleting directory :" + scannedImagesDirectory.getAbsolutePath());
			try {
				FileUtils.forceDelete(scannedImagesDirectory);
			} catch (IOException ioException2) {
				// Now leave this folder as it is.
				logger.error(ioException2.getMessage(), ioException2);
			}

		}
	}

	/**
	 * Gets a custom file filter for all the supported file extensions. Includes all the Office files, tifs and pdf.
	 * 
	 * @return {@link CustomFileFilter}
	 */
	private CustomFileFilter getValidFilesFilter() {
		CustomFileFilter validFileFilter = new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
				FileType.TIFF.getExtensionWithDot(), FileType.PDF.getExtensionWithDot(), OpenOfficeFileType.BMP.getExtensionWithDot(),
				OpenOfficeFileType.DOC.getExtensionWithDot(), OpenOfficeFileType.DOCX.getExtensionWithDot(),
				OpenOfficeFileType.GIF.getExtensionWithDot(), OpenOfficeFileType.HTM.getExtensionWithDot(),
				OpenOfficeFileType.HTML.getExtensionWithDot(), OpenOfficeFileType.JPEG.getExtensionWithDot(),
				OpenOfficeFileType.JPG.getExtensionWithDot(), OpenOfficeFileType.ODF.getExtensionWithDot(),
				OpenOfficeFileType.ODG.getExtensionWithDot(), OpenOfficeFileType.ODP.getExtensionWithDot(),
				OpenOfficeFileType.ODS.getExtensionWithDot(), OpenOfficeFileType.ODT.getExtensionWithDot(),
				OpenOfficeFileType.PNG.getExtensionWithDot(), OpenOfficeFileType.PPT.getExtensionWithDot(),
				OpenOfficeFileType.PPTX.getExtensionWithDot(), OpenOfficeFileType.PSD.getExtensionWithDot(),
				OpenOfficeFileType.RTF.getExtensionWithDot(), OpenOfficeFileType.SXC.getExtensionWithDot(),
				OpenOfficeFileType.SXD.getExtensionWithDot(), OpenOfficeFileType.SXI.getExtensionWithDot(),
				OpenOfficeFileType.SXW.getExtensionWithDot(), OpenOfficeFileType.TXT.getExtensionWithDot(),
				OpenOfficeFileType.WPD.getExtensionWithDot(), OpenOfficeFileType.XLS.getExtensionWithDot(),
				OpenOfficeFileType.XLSX.getExtensionWithDot());
		return validFileFilter;
	}

	@Override
	public void copyFolderWithFileFilter(String sourcePath, String folderName, String batchClassID, CustomFileFilter fileFilter)
			throws DCMAApplicationException {
		String scannedImagesPath = sourcePath + File.separator + folderName;
		File scannedImagesDirectory = new File(scannedImagesPath);
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		File uncFolder = new File(batchClass.getUncFolder() + File.separator + folderName);
		try {
			List<File> files = getFiles(scannedImagesPath, fileFilter);
			if (files != null && !files.isEmpty()) {
				for (File file : files) {
					try {
						FileUtils.forceDelete(file);
						logger.info("Deleting File : " + file.getAbsolutePath());
					} catch (Exception e) {
						logger.error("Unable to delete file : " + file.getAbsolutePath());
					}
				}
			}
			FileUtils.copyDirectory(scannedImagesDirectory, uncFolder);
			logger.info("Copying directory " + scannedImagesDirectory.getAbsolutePath() + "to " + uncFolder.getAbsolutePath());
			FileUtils.forceDelete(scannedImagesDirectory);
			logger.info("Deleting directory :" + scannedImagesDirectory.getAbsolutePath());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

	private List<File> getFiles(String srcPath, CustomFileFilter fileFilter) throws IOException {
		File srcDir = new File(srcPath);
		List<File> fileList = new ArrayList<File>();
		if (srcDir != null && srcDir.list(fileFilter) != null) {
			for (String imagename : srcDir.list(fileFilter)) {
				fileList.add(new File(srcPath + File.separator + imagename));
			}
		}
		return fileList;
	}

	/*
	 * class FileFilter implements FilenameFilter {
	 * 
	 * public FileFilter() { super(); }
	 * 
	 * public boolean accept(File dir, String name) { return (!(name.endsWith(".tif")|| name.endsWith(".tiff"))); } }
	 */

	@Override
	public String getBaseFolderLocation() {
		return batchSchemaDao.getJAXB2Template().getBaseFolderLocation();
	}

	@Override
	public void htmlToXmlGeneration(BatchInstanceID batchInstanceID, final String pluginWorkflow) throws DCMAException {
		try {
			this.htmlOutputGeneration(batchInstanceID.getID());
		} catch (Exception e) {
			throw new DCMAException(e.getMessage(), e);
		}
		logger.info("Copying File : " + batchInstanceID);
		BackUpFileService.copyBatchXML(batchInstanceID.getID(), pluginWorkflow);
	}

	@Override
	public String getProjectFileBaseFolder() {
		return batchSchemaDao.getJAXB2Template().getProjectFilesBaseFolder();
	}

	@Override
	public List<String> getProjectFilesForDocumentType(String batchClassIdentifier, String documentType) throws DCMAException {
		List<String> returnList = null;
		if (!EphesoftStringUtil.isNullOrEmpty(batchClassIdentifier)) {
			String projectFileBaseFolder = getProjectFileBaseFolder();
			File projectFilesFolder = new File(EphesoftStringUtil.concatenate(getBaseFolderLocation(), File.separator,
					batchClassIdentifier, File.separator, projectFileBaseFolder));
			if (projectFilesFolder.exists()) {
				String[] allProjectFiles = projectFilesFolder.list();
				returnList = new ArrayList<String>(allProjectFiles.length);
			}
		}
		return returnList;
	}

	@Override
	public String getHttpEmailFolderPath() {
		return getBaseHttpURL() + "/" + batchSchemaDao.getJAXB2Template().getEmailFolderName();
	}

	@Override
	public String getBatchFolderURL() {
		File file = new File(getLocalFolderLocation());
		return getBaseHttpURL() + "/" + file.getName();
	}

	@Override
	public String getEmailFolderPath() {
		return getBaseSampleFDLock() + File.separator + batchSchemaDao.getJAXB2Template().getEmailFolderName();
	}

	@Override
	public String getTestKVExtractionFolderPath(BatchClassID batchClassID, boolean createDirectory) {
		return getAbsolutePath(batchClassID.getID(), batchSchemaDao.getJAXB2Template().getTestKVExtractionFolderName(),
				createDirectory);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.dcma.batch.service.BatchSchemaService#getBatchExportFolderLocation()
	 */
	@Override
	public String getBatchExportFolderLocation() {
		return batchSchemaDao.getJAXB2Template().getBatchExportFolder();
	}

	@Override
	public String getFuzzyDBIndexFolderName() {
		return batchSchemaDao.getJAXB2Template().getFuzzyDBIndexFolderName();
	}

	@Override
	public String getImagemagickBaseFolderName() {
		return batchSchemaDao.getJAXB2Template().getImageMagickBaseFolderName();
	}

	@Override
	public String getSearchIndexFolderName() {
		return batchSchemaDao.getJAXB2Template().getSearchIndexFolderName();
	}

	@Override
	public String getSearchSampleName() {
		return batchSchemaDao.getJAXB2Template().getSearchSampleName();
	}

	@Override
	public String getTestKVExtractionFolderName() {
		return batchSchemaDao.getJAXB2Template().getTestKVExtractionFolderName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.dcma.batch.service.BatchSchemaService#getBatchClassSerializableFile()
	 */
	@Override
	public String getBatchClassSerializableFile() {
		return batchSchemaDao.getJAXB2Template().getBatchClassSerializableFile();
	}

	@Override
	public String getFileboundPluginMappingFolderName() {
		return batchSchemaDao.getJAXB2Template().getFileboundPluginMappingFolderName();
	}

	@Override
	public String getTempFolderName() {
		return batchSchemaDao.getJAXB2Template().getTempFolder();
	}

	@Override
	public void copyEmailFolderToUNC(String sourcePath, String folderName, String batchClassID, int maxMailProcessingCapacity,
			boolean mailImportConsolidation) throws DCMAApplicationException {
		if (mailImportConsolidation) {
			copyEmailFolderToUNCClubbingFeature(sourcePath, folderName, batchClassID, maxMailProcessingCapacity);
		} else {
			copyEmailFolderToUNCOldFeature(sourcePath, folderName, batchClassID);
		}
	}

	private void copyEmailFolderToUNCOldFeature(String sourcePath, String folderName, String batchClassID)
			throws DCMAApplicationException {

		// Getting Ephesoft cloud user type
		Integer userType = getUserType();
		String scannedImagesPath = sourcePath + File.separator + folderName;
		File scannedImagesDirectory = new File(scannedImagesPath);

		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		File uncFolder = new File(batchClass.getUncFolder() + File.separator + folderName);
		try {
			logger.info("Copying directory " + scannedImagesDirectory.getAbsolutePath() + "to " + uncFolder.getAbsolutePath());
			FileUtils.copyDirectory(scannedImagesDirectory, uncFolder);
			logger.info("Deleting directory :" + scannedImagesDirectory.getAbsolutePath());
			com.ephesoft.dcma.util.FileUtils.forceDelete(scannedImagesDirectory);
			updateBatchCounter(batchClassID, userType);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

	private void copyEmailFolderToUNCClubbingFeature(String sourcePath, String folderName, String batchClassID,
			int maxMailProcessingCapacity) throws DCMAApplicationException {

		// Getting Ephesoft cloud user type
		Integer userType = getUserType();
		String scannedImagesPath = sourcePath + File.separator + folderName;
		File scannedImagesDirectory = new File(scannedImagesPath);
		int noOfIterations = getTotalBatchesToBeMade(scannedImagesDirectory, maxMailProcessingCapacity);
		int uploadedMailCounter = 0;
		for (int i = 0; i < noOfIterations; i++) {
			String transferFolderName = folderName + "-" + String.valueOf(System.nanoTime());
			String uploadableFolderPath = EphesoftStringUtil.concatenate(scannedImagesPath, File.separator, transferFolderName);
			File uploadFolder = new File(uploadableFolderPath);
			if (!uploadFolder.exists()) {
				uploadFolder.mkdirs();
			}
			File[] listOfFolders = scannedImagesDirectory.listFiles();
			for (File copyFile : listOfFolders) {
				if (uploadedMailCounter < maxMailProcessingCapacity) {
					if (copyFile != null && copyFile.getName().contains("mailDropFolder-") && !copyFile.getName().contains("-Issue")) {
						uploadedMailCounter++;
						try {
							if (!checkFolderCopyPossible(copyFile)) {
								if (checkNumberOfFilesCompatibility(copyFile)) {
									copyBatchFiles(copyFile, uploadFolder, String.format("%04d", uploadedMailCounter - 1));
									com.ephesoft.dcma.util.FileUtils.copyFolder(copyFile, uploadFolder);
									logger.info("Deleting directory :" + copyFile.getAbsolutePath());
									com.ephesoft.dcma.util.FileUtils.forceDelete(copyFile);
								} else {
									logger.error("Problem in the email downloaded folder. The conversion of the files could not process properly. Skipping the folder : "
											+ copyFile.getName());
									copyFile.renameTo(new File(copyFile.getAbsolutePath() + "-Issue"));
									uploadedMailCounter--;
								}
							} else {
								logger.error("Problem in the email downloaded folder. Any file is in use in the folder. Skipping the folder : "
										+ copyFile.getName());
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							logger.error("Unable to perform Upload of email files. Issue is : " + e.getMessage());
						}
					}
				} else {
					break;
				}
			}
			uploadedMailCounter = 0;
			BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
			File uncFolder = new File(batchClass.getUncFolder() + File.separator + transferFolderName);
			try {
				if (uploadFolder.list().length > 0) {
					logger.info("Copying directory " + uploadFolder.getAbsolutePath() + "to " + uncFolder.getAbsolutePath());
					FileUtils.copyDirectory(uploadFolder, uncFolder);
					updateBatchCounter(batchClassID, userType);
				}
				logger.info("Deleting directory :" + uploadFolder.getAbsolutePath());
				com.ephesoft.dcma.util.FileUtils.forceDelete(uploadFolder);

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new DCMAApplicationException(e.getMessage(), e);
			}
		}

	}

	private boolean checkFolderCopyPossible(File copyFile) {
		boolean isFileLocked = false;
		try {
			org.apache.commons.io.FileUtils.touch(copyFile);
			isFileLocked = false;
		} catch (final IOException ioException) {
			isFileLocked = true;
			logger.debug(EphesoftStringUtil.concatenate("Status of file: ", copyFile.getAbsolutePath(), " locked is: ", isFileLocked));
			return isFileLocked;
		}
		String filePath = null;
		if (null != copyFile && copyFile.exists()) {
			File[] fileList = copyFile.listFiles();
			if (null != fileList) {
				for (File fileItem : fileList) {
					filePath = fileItem.getAbsolutePath();
					logger.debug(EphesoftStringUtil.concatenate("Checking for file lock on: ", filePath));
					if (fileItem.isDirectory()) {
						isFileLocked = checkFolderCopyPossible(fileItem);
					} else {
						try {
							org.apache.commons.io.FileUtils.touch(fileItem);
							isFileLocked = false;
						} catch (final IOException ioException) {
							isFileLocked = true;
							logger.debug(EphesoftStringUtil.concatenate("Status of file: ", filePath, " locked is: ", isFileLocked));
						}
					}
					if (isFileLocked) {
						break;
					}
				}
			}
		}
		return isFileLocked;
	}

	private boolean checkNumberOfFilesCompatibility(File copyFile) {
		boolean returnValue = false;
		int processedFileCount = getFileCountInFolder(copyFile);
		File downloadedFilesFolder = new File(EphesoftStringUtil.concatenate(copyFile.getAbsolutePath(), File.separator,
				"downloaded-email-attachments"));
		int downloadedFileCount = getFileCountInFolder(downloadedFilesFolder);
		if (processedFileCount != 0 && downloadedFileCount != 0 && processedFileCount == downloadedFileCount) {
			returnValue = true;
		} else {
			returnValue = false;
		}
		return returnValue;
	}

	private int getFileCountInFolder(File folderCheckForFiles) {
		int returnValue = 0;
		if (folderCheckForFiles.exists()) {
			File[] filesList = folderCheckForFiles.listFiles();
			for (File fileInstance : filesList) {
				if (fileInstance.exists() && fileInstance.isFile()
						&& !fileInstance.getName().contains(com.ephesoft.dcma.util.FileUtils.ZIP_FILE_EXT)) {
					returnValue++;
				}
			}
		}
		return returnValue;
	}

	private void copyBatchFiles(File copyFile, File uploadFolder, String prefixValue) throws IOException {
		renameFiles(copyFile, prefixValue);
		File[] listOfFiles = copyFile.listFiles();
		for (File fileElement : listOfFiles) {
			if (fileElement != null) {
				if (fileElement.isDirectory() && fileElement.getAbsolutePath().contains("downloaded-email-attachments")) {
					File downloadEmailsFolder = new File(uploadFolder.getPath() + File.separator + "downloaded-email-attachments");
					if (downloadEmailsFolder.exists()) {
						copyBatchFiles(fileElement, downloadEmailsFolder, prefixValue);
					} else {
						renameFiles(fileElement, prefixValue);
						FileUtils.copyDirectory(fileElement, downloadEmailsFolder);
					}
				} else {
					if (!fileElement.getName().contains("_null")) {
						String updatedPathName = com.ephesoft.dcma.util.FileUtils.getUpdatedFileNameForDuplicateFile(
								fileElement.getName(), uploadFolder);
						final StringBuilder finalFilePathBuilder = new StringBuilder();
						finalFilePathBuilder.append(uploadFolder.getPath());
						finalFilePathBuilder.append(File.separator);
						finalFilePathBuilder.append(updatedPathName);
						final String finalFilePath = finalFilePathBuilder.toString();
						FileUtils.copyFile(fileElement, new File(finalFilePath));
					}
				}
			}
		}

	}

	private void renameFiles(File copyFile, String prefixValue) {
		File[] listOfFiles = copyFile.listFiles();
		if (null != listOfFiles) {
			for (File fileElement : listOfFiles) {
				if (fileElement != null) {
					if (!fileElement.isDirectory()) {
						File destFile = new File(EphesoftStringUtil.concatenate(copyFile.getAbsolutePath(), File.separator,
								prefixValue, "_", fileElement.getName()));
						fileElement.renameTo(destFile);
						// } else {
						// renameFiles(fileElement, prefixValue);
					}
				}
			}
		}

	}

	private int getTotalBatchesToBeMade(File scannedImagesDirectory, int maxMailProcessingCapacity) {
		String[] listOfFolders = scannedImagesDirectory.list();
		int counter = 0;
		for (String fileName : listOfFolders) {
			if (fileName.contains("mailDropFolder-") && !fileName.contains("-Issue")) {
				counter++;
			}
		}
		int retValue = 1;
		if (counter == 0) {
			retValue = 0;
		} else if (counter >= maxMailProcessingCapacity) {
			retValue = counter / maxMailProcessingCapacity;
		}
		return retValue;
	}

	@Override
	public String getTestTableFolderPath(BatchClassID batchClassID, boolean createDirectory) {
		return getAbsolutePath(batchClassID.getID(), batchSchemaDao.getJAXB2Template().getTestTableFolderName(), createDirectory);
	}

	@Override
	public void backUpBatchXML(final BatchInstanceID batchInstanceID, final String pluginWorkflow) {
		BackUpFileService.copyBatchXML(batchInstanceID.getID(), pluginWorkflow);
	}

	@Override
	public String getTestTableFolderName() {
		return batchSchemaDao.getJAXB2Template().getTestTableFolderName();
	}

	@Override
	public String getValidationScriptName() {
		return batchSchemaDao.getJAXB2Template().getValidationScriptName();
	}

	@Override
	public String getThreadpoolLockFolderName() {
		return batchSchemaDao.getJAXB2Template().getThreadpoolLockFolder();
	}

	@Override
	public String getAddNewTableScriptName() {
		return batchSchemaDao.getJAXB2Template().getAddNewTableScriptName();
	}

	@Override
	public void setTestFolderLocation(String testFolderLocation) {
		if (null != testFolderLocation && !testFolderLocation.isEmpty()) {
			batchSchemaDao.getJAXB2Template().setTestFolderLocation(testFolderLocation);
		}
	}

	@Override
	public String getTestFolderLocation() {
		return batchSchemaDao.getJAXB2Template().getTestFolderLocation();
	}

	@Override
	public String getScriptConfigFolderName() {
		return batchSchemaDao.getJAXB2Template().getScriptConfigFolderName();
	}

	@Override
	public void setBaseFolderLocation(String baseFolderLocation) {
		if (null != baseFolderLocation && !baseFolderLocation.isEmpty()) {
			batchSchemaDao.getJAXB2Template().setBaseFolderLocation(baseFolderLocation);
			batchSchemaDao.getJAXB2Template().setBaseSampleFdLoc(baseFolderLocation);
		}

	}

	@Override
	public BatchClassModule getDetachedBatchClassModuleByName(String batchClassIdentifier, String workflowName) {
		BatchClassModule batchClassModule = batchClassModuleService.getBatchClassModuleByWorkflowName(batchClassIdentifier,
				workflowName);
		BatchClassModule detachedBatchClassModule = null;
		if (batchClassModule == null) {
			logger.info("No module found with workflowName:" + workflowName + " in batch class id:" + batchClassIdentifier
					+ ". Skipping the updates for this module.");
		} else {
			batchClassModuleService.evict(batchClassModule);
			detachedBatchClassModule = new BatchClassModule();

			detachedBatchClassModule.setId(0);
			detachedBatchClassModule.setBatchClass(null);

			detachedBatchClassModule.setModule(batchClassModule.getModule());
			detachedBatchClassModule.setWorkflowName(batchClassModule.getWorkflowName());
			detachedBatchClassModule.setOrderNumber(batchClassModule.getOrderNumber());
			detachedBatchClassModule.setRemoteBatchClassIdentifier(batchClassModule.getRemoteBatchClassIdentifier());
			detachedBatchClassModule.setRemoteURL(batchClassModule.getRemoteURL());

			List<BatchClassModuleConfig> moduleConfigs = batchClassModule.getBatchClassModuleConfig();
			List<BatchClassModuleConfig> newModuleConfigList = new ArrayList<BatchClassModuleConfig>();
			for (BatchClassModuleConfig moduleConfig : moduleConfigs) {
				newModuleConfigList.add(moduleConfig);
				moduleConfig.setId(0);
				moduleConfig.setBatchClassModule(null);
			}
			detachedBatchClassModule.setBatchClassModuleConfig(newModuleConfigList);

			List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
			List<BatchClassPlugin> newBatchClassPluginsList = new ArrayList<BatchClassPlugin>();
			for (BatchClassPlugin batchClassPlugin : batchClassPlugins) {
				newBatchClassPluginsList.add(batchClassPlugin);
				batchClassPlugin.setId(0);
				batchClassPlugin.setBatchClassModule(null);

				List<BatchClassPluginConfig> batchClassPluginConfigs = batchClassPlugin.getBatchClassPluginConfigs();
				List<BatchClassPluginConfig> newBatchClassPluginConfigsList = new ArrayList<BatchClassPluginConfig>();
				for (BatchClassPluginConfig batchClassPluginConfig : batchClassPluginConfigs) {
					newBatchClassPluginConfigsList.add(batchClassPluginConfig);
					batchClassPluginConfig.setId(0);
					batchClassPluginConfig.setBatchClassPlugin(null);

					List<KVPageProcess> kvPageProcess = batchClassPluginConfig.getKvPageProcesses();
					List<KVPageProcess> newKvPageProcessList = new ArrayList<KVPageProcess>();
					for (KVPageProcess kVPageProcessChild : kvPageProcess) {
						kVPageProcessChild.setId(0);
						newKvPageProcessList.add(kVPageProcessChild);
						kVPageProcessChild.setBatchClassPluginConfig(null);
					}
					batchClassPluginConfig.setKvPageProcesses(newKvPageProcessList);
				}
				batchClassPlugin.setBatchClassPluginConfigs(newBatchClassPluginConfigsList);

				List<BatchClassDynamicPluginConfig> batchClassDynamicPluginConfigs = batchClassPlugin
						.getBatchClassDynamicPluginConfigs();
				List<BatchClassDynamicPluginConfig> newBatchClassDynamicPluginConfigsList = new ArrayList<BatchClassDynamicPluginConfig>();
				for (BatchClassDynamicPluginConfig batchClassDynamicPluginConfig : batchClassDynamicPluginConfigs) {
					newBatchClassDynamicPluginConfigsList.add(batchClassDynamicPluginConfig);
					batchClassDynamicPluginConfig.setId(0);
					batchClassDynamicPluginConfig.setBatchClassPlugin(null);

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
				batchClassPlugin.setBatchClassDynamicPluginConfigs(newBatchClassDynamicPluginConfigsList);
			}
			detachedBatchClassModule.setBatchClassPlugins(newBatchClassPluginsList);

		}
		return detachedBatchClassModule;
	}

	@Override
	public String getUploadBatchFolder() {
		return getBaseFolderLocation() + File.separator + batchSchemaDao.getJAXB2Template().getUploadBatchFolder();
	}

	@Override
	public String getWebServicesFolderPath() {
		return getBaseFolderLocation() + File.separator + batchSchemaDao.getJAXB2Template().getWebServicesFolderPath();
	}

	@Override
	public String getTestAdvancedKvExtractionFolderPath(String batchClassIdentifier, boolean createDirectory) {
		return getAbsolutePath(batchClassIdentifier, batchSchemaDao.getJAXB2Template().getTestAdvancedKvExtractionFolderName(),
				createDirectory);
	}

	@Override
	public String getTestAdvancedKVExtractionFolderName() {
		return batchSchemaDao.getJAXB2Template().getTestAdvancedKvExtractionFolderName();
	}

	@Override
	public void update(final Batch batch, final String filePath) {
		if (null == batch) {
			logger.info("batch class is null.");
		} else if (null == filePath || filePath.isEmpty()) {
			logger.info("File path is either null or empty.");
		} else {
			this.batchSchemaDao.update(batch, filePath);
		}
	}

	@Override
	public HocrPages getHOCR(String xmlFilePath) {
		HocrPages hocrPages = null;
		hocrPages = this.hocrSchemaDao.getObjectFromFilePath(xmlFilePath);
		return hocrPages;
	}

	@Override
	public String getAdvancedTestTableFolderPath(final String batchClassId, final boolean createDir) {
		return getAbsolutePath(batchClassId, batchSchemaDao.getJAXB2Template().getAdvancedTestTableFolder(), createDir);
	}

	@Override
	public String getAdvancedTestTableFolderName() {
		return this.batchSchemaDao.getJAXB2Template().getAdvancedTestTableFolder();
	}

	@Override
	public String getDbExportMappingFolderName() {
		return batchSchemaDao.getJAXB2Template().getDbExportPluginMappingFolderName();
	}

	/**
	 * The <code>getUserType</code> method is used to retrieve user type from properties file.
	 * 
	 * @return {@link Integer} Ephesoft Cloud user type
	 */
	private Integer getUserType() {
		Integer userType = null;
		try {
			userType = Integer.parseInt(ApplicationConfigProperties.getApplicationConfigProperties().getProperty(USER_TYPE));
		} catch (NumberFormatException numberFormatException) {
			userType = UserType.OTHERS.getUserType();
			logger.error("user type property is in wrong format in property file");
		} catch (IOException ioException) {
			userType = UserType.OTHERS.getUserType();
			logger.error("user type property is missing from property file");
		}
		return userType;
	}

	/**
	 * The <code>getFileType</code> method is used to get the file type.
	 * 
	 * @param file {@link File} file
	 * @return {@link String} file type
	 */
	private String getFileType(File file) {
		String fileType = null;
		if (null != file) {
			String fileName = file.getName().toLowerCase(Locale.getDefault());
			if (fileName.endsWith(FileType.PDF.getExtensionWithDot())) {
				fileType = FileType.PDF.getExtension();
			} else if (fileName.endsWith(FileType.TIF.getExtensionWithDot())) {
				fileType = FileType.TIF.getExtension();
			} else if (fileName.endsWith(FileType.TIFF.getExtensionWithDot())) {
				fileType = FileType.TIFF.getExtension();
			}
		}
		return fileType;
	}

	/**
	 * The <code>updateBatchCounter</code> method is used to update batch count for Freemium user type.
	 * 
	 * @param batchClassID {@link String} batch class identifier
	 * @param userType {@link Integer} Ephesoft Cloud user type
	 */
	private void updateBatchCounter(String batchClassID, Integer userType) {
		if (userType.intValue() == UserType.LIMITED.getUserType()) {
			BatchClassCloudConfig batchClassCloudConfig = batchClassCloudService
					.getBatchClassCloudConfigByBatchClassIdentifier(batchClassID);
			if (null != batchClassCloudConfig) {
				Integer batchInstanceCount = batchClassCloudConfig.getBatchInstanceLimit();
				Integer currentCounter = batchClassCloudConfig.getCurrentCounter();
				if (null != currentCounter && null != batchInstanceCount && ++currentCounter <= batchInstanceCount) {
					batchClassCloudConfig.setCurrentCounter(currentCounter);
					batchClassCloudService.updateBatchClassCloudConfig(batchClassCloudConfig);
				}
			}
		}
	}

	@Override
	public void updateHOCR(HocrPages hocrPages, String batchInstanceID, String hocrFileName) {
		hocrSchemaDao.update(hocrPages, getHOCRFilePath(batchInstanceID, hocrFileName));
	}

	/**
	 * Returns the name of script to execute on inserting a new table row.
	 * 
	 * @return {@link String} the script name
	 */
	@Override
	public String getInsertNewRowScriptName() {
		return batchSchemaDao.getJAXB2Template().getInsertNewRowScriptName();
	}

	@Override
	public String getExportToImageImportPluginMappingFolderName() {
		return batchSchemaDao.getJAXB2Template().getExportToImageImportPluginMappingFolderName();
	}

	@Override
	public String getKeyValueLearningFolderName() {
		return batchSchemaDao.getJAXB2Template().getKeyValueLearningFolderName();
	}

	@Override
	public void removeBatch(String batchInstanceIdentifier) {
		batchSchemaDao.removeInMemoryBatch(batchInstanceIdentifier);
	}

	@Override
	public void createBatchXMLAtPath(String batchInstanceID, String destinationFilePath) {
		if (null == batchInstanceID) {
			logger.info("batch instance is null.");
		} else {
			Batch batch = batchSchemaDao.getInMemoryBatch(batchInstanceID);
			if (null == batch) {
				logger.error(EphesoftStringUtil.concatenate("No batch xml object exists in memory for batch instance ",
						batchInstanceID));
			} else {
				this.batchSchemaDao.update(batch, destinationFilePath);
			}
		}
	}

	@Override
	public Set<String> getInMemoryBatchKeys() {
		return batchSchemaDao.getKeys();
	}

	@Override
	public boolean isBatchInMemory(String batchInstanceIdentifier) {
		boolean isBatchInMemory = true;
		Batch batch = batchSchemaDao.getInMemoryBatch(batchInstanceIdentifier);
		if (null == batch) {
			isBatchInMemory = false;
		}
		return isBatchInMemory;
	}

}
