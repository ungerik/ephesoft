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

package com.ephesoft.dcma.webservice.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;
import org.jdom.output.DOMOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.oxm.XmlMappingException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.w3c.dom.Document;

import com.ephesoft.dcma.batch.constant.BatchConstants;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.dao.xml.BatchSchemaDao;
import com.ephesoft.dcma.batch.encryption.service.EncryptionKeyService;
import com.ephesoft.dcma.batch.encryption.util.CryptoMarshaller;
import com.ephesoft.dcma.batch.encryption.util.DOMEncryptionUtil;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.BatchClasses;
import com.ephesoft.dcma.batch.schema.BatchInstances;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.CopyDocumentType;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.ExtractKV;
import com.ephesoft.dcma.batch.schema.ExtractKVParams;
import com.ephesoft.dcma.batch.schema.ExtractKVParams.Params;
import com.ephesoft.dcma.batch.schema.ExtractTableParam;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.HOCRFile;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.ImportBatchClassOptions;
import com.ephesoft.dcma.batch.schema.KVExtractionDocType;
import com.ephesoft.dcma.batch.schema.ListValues;
import com.ephesoft.dcma.batch.schema.Modules;
import com.ephesoft.dcma.batch.schema.Modules.Module;
import com.ephesoft.dcma.batch.schema.UploadLearningFiles;
import com.ephesoft.dcma.batch.schema.UploadLearningFiles.DocType;
import com.ephesoft.dcma.batch.schema.UploadLearningFiles.DocType.PageTypeFirst;
import com.ephesoft.dcma.batch.schema.UploadLearningFiles.DocType.PageTypeFirst.FilesToBeUploaded;
import com.ephesoft.dcma.batch.schema.UploadLearningFiles.DocType.PageTypeLast;
import com.ephesoft.dcma.batch.schema.UploadLearningFiles.DocType.PageTypeMiddle;
import com.ephesoft.dcma.batch.schema.WebServiceParams;
import com.ephesoft.dcma.batch.schema.WebServiceParams.Params.Param;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.ImportBatchService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.batch.util.BatchEncryptionUtil;
import com.ephesoft.dcma.common.ImportBatchClassResultCarrier;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.common.CategoryType;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.PluginProperty;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.core.threadpool.ThreadPool;
import com.ephesoft.dcma.da.domain.AdvancedKVExtraction;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.BatchClassGroups;
import com.ephesoft.dcma.da.domain.BatchClassModule;
import com.ephesoft.dcma.da.domain.BatchClassPlugin;
import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.PageType;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.service.BatchClassGroupsService;
import com.ephesoft.dcma.da.service.BatchClassModuleService;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceGroupsService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.imagemagick.ImageMagicProperties;
import com.ephesoft.dcma.imagemagick.imageClassifier.SampleThumbnailGenerator;
import com.ephesoft.dcma.imagemagick.service.ImageProcessService;
import com.ephesoft.dcma.kvextraction.service.KVExtractionService;
import com.ephesoft.dcma.lucene.service.SearchClassificationService;
import com.ephesoft.dcma.tableextraction.TableExtraction;
import com.ephesoft.dcma.tableextraction.TableExtractionProperties;
import com.ephesoft.dcma.tablefinder.service.TableFinderService;
import com.ephesoft.dcma.tesseract.TesseractProperties;
import com.ephesoft.dcma.tesseract.service.TesseractService;
import com.ephesoft.dcma.user.service.UserConnectivityService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.IUtilCommonConstants;
import com.ephesoft.dcma.util.OSUtil;
import com.ephesoft.dcma.util.TIFFUtil;
import com.ephesoft.dcma.util.WebServiceUtil;
import com.ephesoft.dcma.util.XMLUtil;
import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;
import com.ephesoft.dcma.util.exception.HexDecoderException;
import com.ephesoft.dcma.util.exception.KeyGenerationException;
import com.ephesoft.dcma.util.exception.KeyNotFoundException;
import com.ephesoft.dcma.webservice.constants.WebServiceConstants;
import com.ephesoft.dcma.webservice.customexceptions.BatchNameAlreadyExistException;
import com.ephesoft.dcma.webservice.customexceptions.ConfigurationException;
import com.ephesoft.dcma.webservice.customexceptions.InternalServerException;
import com.ephesoft.dcma.webservice.customexceptions.NoUserRoleException;
import com.ephesoft.dcma.webservice.customexceptions.UnAuthorisedAccessException;
import com.ephesoft.dcma.webservice.customexceptions.UnSupportedFileTypeException;
import com.ephesoft.dcma.webservice.customexceptions.ValidationException;
import com.ephesoft.dcma.webservice.exceptionhandler.RestError;
import com.ephesoft.dcma.webservice.responseXML.ErrorElement;
import com.ephesoft.dcma.webservice.responseXML.ResponseCodeElement;
import com.ephesoft.dcma.webservice.responseXML.RootElement;
import com.ephesoft.dcma.workflows.constant.WorkflowConstants;
import com.ephesoft.dcma.workflows.service.DeploymentService;
import com.ephesoft.dcma.workflows.service.WorkflowService;
import com.ephesoft.dcma.workflows.service.engine.EngineService;

/**
 * Web service Helper class is used for providing all the functionality required for processing of web service.
 * 
 * @author Ephesoft
 * @version 3.0
 * @see EphesoftWebServiceAPI
 * 
 */
public class WebServiceHelper {

	/**
	 * {@link RootElement} for xml format of the element.
	 */
	private RootElement rootElement;

	/**
	 * {@link ErrorElement} for xml formatting of the xml element.
	 */
	private ErrorElement errorElement;

	/**
	 * {@link ResponseCodeElement} for response code inclusion in root element.
	 */
	private ResponseCodeElement responseCodeElement;

	/**
	 * Initializing batchSchemaService {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/** {@link WorkflowService} The workflow service. */
	@Autowired
	private WorkflowService workflowService;

	/**
	 * Initializing searchClassificationService {@link SearchClassificationService}.
	 */
	@Autowired
	private SearchClassificationService searchClassificationService;

	/**
	 * Initializing batchClassService {@link BatchClassService}.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * Initializing documentTypeService {@link DocumentTypeService}.
	 */
	@Autowired
	private DocumentTypeService docTypeService;

	/** The user connectivity service. */
	@Autowired
	private UserConnectivityService userConnectivityService;

	@Autowired
	private ImageProcessService imService;

	@Autowired
	private TesseractService tesseractService;
	
	@Autowired
	private EncryptionKeyService encryptionService;

	/** The batch class groups service. */
	@Autowired
	private BatchClassGroupsService batchClassGroupsService;

	/** The batch instance service. */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/** The batch instance group service. */
	@Autowired
	private BatchInstanceGroupsService batchInstanceGroupsService;

	/** The batch class module service. */
	@Autowired
	private BatchClassModuleService batchClassModuleService;

	/** The plugin properties service. */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/** The bs service. */
	@Autowired
	private BatchSchemaService bsService;

	@Autowired
	private TableFinderService tableFinderService;

	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService batchClassPPService;

	@Autowired
	private TableExtraction tableExtraction;

	/** The bc service. */
	@Autowired
	private BatchClassService bcService;

	// TODO
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService classPluginPropertiesService;

	@Autowired
	private EngineService engineService;

	/** The import batch service. */
	@Autowired
	private ImportBatchService importBatchService;

	@Autowired
	private DeploymentService deploymentService;

	/**
	 * The kv service.
	 */
	@Autowired
	private KVExtractionService kvService;

	/**
	 * Initializing batchSchemaDao {@link BatchSchemaDao}.
	 */
	@Autowired
	private BatchSchemaDao batchSchemaDao;

	/**
	 * Initializing batchClassPluginConfigService {@link BatchClassPluginConfigService}.
	 */
	@Autowired
	private BatchClassPluginConfigService batchClassPluginConfigService;

	@Autowired
	private EncryptionKeyService encryptionKeyService;

	/**
	 * Initializing logger {@link Logger}.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WebServiceHelper.class);

	/**
	 * The constant value for minimum confidence threshold value.
	 */
	private static final float MIN_CONFIDENCE_THRESHOLD_VALUE = 0;

	/**
	 * The constant value for maximum confidence threshold value.
	 */
	private static final float MAX_CONFIDENCE_THRESHOLD_VALUE = 100;

	/**
	 * Constant for string "true".
	 */
	private static final String TRUE = "true";

	/**
	 * Constant for string "false".
	 */
	private static final String FALSE = "false";

	/**
	 * Constant for the type of processing file used for document type creation: "FPR.rsp".
	 */
	private static final String DEFAULT_RSP_FILE_NAME = "FPR.rsp";

	/**
	 * Constant for the type of processing file used for document type creation: "FPR_Barcode.rsp".
	 */
	private static final String DEFAULT_RSP_BARCODE_FILE_NAME = "FPR_Barcode.rsp";

	/**
	 * Constant for the maximum limit of value input for name.
	 */
	private static final int MAX_CHARACTER_LIMIT = 255;
	/**
	 * Constant for minimum limit of priority.
	 */
	private static final int MIN_PRIORITY_VALUE = 1;
	/**
	 * Constant for maximum limit of priority.
	 */
	private static final int MAX_PRIORITY_VALUE = 100;
	/**
	 * Constant Defined for Lucene Search Classification type
	 */
	private static final String LUCENE_SEARCH_CLASSIFICATION_TYPE = "Lucene";
	/**
	 * Constant defined Image-Classification-sample type
	 */
	private static final String IMAGE_CLASSIFICATION_SAMPLE_TYPE = "Image";

	@Autowired
	private CryptoMarshaller cryptoMarshaller;

	/**
	 * Learning for batch class is performed in this api.
	 * 
	 * @param batchClassID The batch class identifier for which learning has to be done.
	 * @throws InternalServerException the internal server exception
	 */
	public void learnFileForBatchClass(final String batchClassID) throws InternalServerException {
		final File batchClassFolder = new File(batchSchemaService.getBaseFolderLocation() + File.separator + batchClassID);
		if (!batchClassFolder.exists()) {
			return;
		}
		final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassID);
		final String ocrPluginName = getOCRPluginNameForBatchClass(batchClassID);
		if (null != ocrPluginName) {
			try {
				if (ocrPluginName.equalsIgnoreCase(WebServiceConstants.TESSERACT_HOCR_PLUGIN)) {
					searchClassificationService.learnSampleHOCRForTesseract(batchClass.getBatchClassID(), true);
				}
			} catch (final DCMAException exception) {
				LOGGER.error(exception.getMessage());
				final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
				final RestError restError = new RestError(status, WebServiceConstants.ERROR_RETRIEVING_FOLDER_PATH_CODE,
						WebServiceConstants.ERROR_RETRIEVING_FOLDER_PATH_MESSAGE,
						WebServiceConstants.ERROR_RETRIEVING_FOLDER_PATH_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
						WebServiceConstants.DEFAULT_URL);
				throw new InternalServerException(WebServiceConstants.ERROR_RETRIEVING_FOLDER_PATH_MESSAGE, restError);
			}
		}
		sampleThumbnailGenerator(batchClassID);
	}

	/**
	 * The OCR engine to be used for learning in this batch class is given by this api.
	 * 
	 * @param batchClassIdentifier {@link String} the identifier for batch class for which learning has to be done.
	 * @return {@link String} the ocr engine name.
	 */
	private String getOCRPluginNameForBatchClass(final String batchClassIdentifier) {
		LOGGER.info("Fetching the ocr engine to be used for learning.");
		final String defaultOcrEngineName = getDefaultHOCRPlugin(batchClassIdentifier);
		String ocrEngineName = defaultOcrEngineName;
		PluginProperty ocrEngineSwitch = null;
		String ocrEngineSwitchValue = null;

		ocrEngineSwitch = TesseractProperties.TESSERACT_SWITCH;
		ocrEngineSwitchValue = getOcrEngineSwitchValue(batchClassIdentifier, batchClassPPService, ocrEngineName, ocrEngineSwitch);

		if (!WebServiceConstants.SWITCH_ON.equalsIgnoreCase(ocrEngineSwitchValue)) {
			ocrEngineSwitch = TesseractProperties.TESSERACT_SWITCH;
			ocrEngineName = WebServiceConstants.TESSERACT_HOCR_PLUGIN;
			ocrEngineSwitchValue = getOcrEngineSwitchValue(batchClassIdentifier, pluginPropertiesService, ocrEngineName,
					ocrEngineSwitch);
			if (!WebServiceConstants.SWITCH_ON.equalsIgnoreCase(ocrEngineSwitchValue)) {
				ocrEngineName = defaultOcrEngineName;
			}
		}
		LOGGER.info("OCR Engine used for learning = " + ocrEngineName);
		return ocrEngineName;
	}

	/**
	 * To generate the thumb-nails for the images being learned.
	 * 
	 * @param batchClassIdentifier the batch class identifier for which learning has to be done
	 * @throws InternalServerException the internal server exception
	 */
	private void sampleThumbnailGenerator(final String batchClassIdentifier) throws InternalServerException {
		final HttpStatus status;
		final RestError restError;
		String sampleBaseFolderPath = null;
		String thumbnailType = null;
		String thumbnailH = null;
		String thumbnailW = null;
		final Map<String, String> properties = batchClassPluginConfigService.getPluginPropertiesForBatchClass(batchClassIdentifier,
				WebServiceConstants.CREATE_THUMBNAILS_PLUGIN, null);
		if (properties != null && !properties.isEmpty()) {
			sampleBaseFolderPath = batchSchemaService.getImageMagickBaseFolderPath(batchClassIdentifier, false);
			if (!(sampleBaseFolderPath != null && sampleBaseFolderPath.length() > 0)) {
				LOGGER.error(WebServiceConstants.ERROR_RETRIEVING_FOLDER_PATH_MESSAGE);
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				restError = new RestError(status, WebServiceConstants.ERROR_RETRIEVING_FOLDER_PATH_CODE,
						WebServiceConstants.ERROR_RETRIEVING_FOLDER_PATH_MESSAGE,
						WebServiceConstants.ERROR_RETRIEVING_FOLDER_PATH_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
						WebServiceConstants.DEFAULT_URL);
				throw new InternalServerException(WebServiceConstants.ERROR_RETRIEVING_FOLDER_PATH_MESSAGE, restError);
			}
			thumbnailH = properties.get(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_HEIGHT.getPropertyKey());
			if (!(thumbnailH != null && thumbnailH.length() > 0)) {
				LOGGER.error(WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_DIMENSION_MESSAGE);
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				restError = new RestError(status, WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_DIMENSION_CODE,
						WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_DIMENSION_MESSAGE,
						WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_DIMENSION_MESSAGE
								+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
				throw new InternalServerException(WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_DIMENSION_MESSAGE, restError);
			}
			thumbnailType = properties.get(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_TYPE.getPropertyKey());
			if (!(thumbnailType != null && thumbnailType.length() > 0)) {
				LOGGER.error(WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_TYPE_MESSAGE);
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				restError = new RestError(status, WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_TYPE_CODE,
						WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_TYPE_MESSAGE,
						WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_TYPE_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
						WebServiceConstants.DEFAULT_URL);
				throw new InternalServerException(WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_TYPE_MESSAGE, restError);
			}
			thumbnailW = properties.get(ImageMagicProperties.CREATE_THUMBNAILS_COMP_THUMB_WIDTH.getPropertyKey());
			if (!(thumbnailW != null && thumbnailW.length() > 0)) {
				LOGGER.error(WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_DIMENSION_MESSAGE);
				status = HttpStatus.INTERNAL_SERVER_ERROR;
				restError = new RestError(status, WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_DIMENSION_CODE,
						WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_DIMENSION_MESSAGE,
						WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_DIMENSION_MESSAGE
								+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
				throw new InternalServerException(WebServiceConstants.ERROR_RETRIEVING_THUMBNAIL_DIMENSION_MESSAGE, restError);
			}
		} else {
			LOGGER.error(WebServiceConstants.NO_IMAGE_MAGIC_PROPERTIES_FOUND_MESSAGE);
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			restError = new RestError(status, WebServiceConstants.NO_IMAGE_MAGIC_PROPERTIES_FOUND_CODE,
					WebServiceConstants.NO_IMAGE_MAGIC_PROPERTIES_FOUND_MESSAGE,
					WebServiceConstants.NO_IMAGE_MAGIC_PROPERTIES_FOUND_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
					WebServiceConstants.DEFAULT_URL);
			throw new InternalServerException(WebServiceConstants.NO_IMAGE_MAGIC_PROPERTIES_FOUND_MESSAGE, restError);
		}
		final SampleThumbnailGenerator sampleThumbnailGenerator = new SampleThumbnailGenerator(sampleBaseFolderPath, thumbnailType,
				thumbnailH, thumbnailW);
		try {
			sampleThumbnailGenerator.generateAllThumbnails(batchClassIdentifier);
		} catch (final DCMAApplicationException e) {
			LOGGER.error(e.getMessage());
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			restError = new RestError(status, WebServiceConstants.INTERNAL_SERVER_ERROR_CODE,
					WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
							+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
			throw new InternalServerException(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
		}

	}

	/**
	 * The default HOCR plugin to be used.
	 * 
	 * @param batchClassIdentifier {@link String} the identifier for batch class for which learning has to be done.
	 * @return {@link String} the name of default HOCR plugin name.
	 */
	private String getDefaultHOCRPlugin(final String batchClassIdentifier) {
		String ocrEngine = WebServiceConstants.TESSERACT_HOCR_PLUGIN;
		if (batchClassIdentifier != null && !batchClassIdentifier.isEmpty()) {
			final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
			if (null != batchClass) {
				final List<BatchClassPlugin> batchClassPlugins = getOrderedListOfPlugins(batchClass);
				if (batchClassPlugins != null) {
					String pluginName = null;
					for (final BatchClassPlugin batchClassPlugin : batchClassPlugins) {
						if (batchClassPlugin != null) {
							pluginName = batchClassPlugin.getPlugin().getPluginName();
						}
						if (IUtilCommonConstants.TESSERACT_HOCR_PLUGIN.equals(pluginName)) {
							ocrEngine = WebServiceConstants.TESSERACT_HOCR_PLUGIN;
							break;
						}
					}
				}
			} else {
				LOGGER.info("Batch Class is non existent.");
			}
		}
		LOGGER.info("Default ocr plugin to be used in case both ");
		return ocrEngine;
	}

	/**
	 * The switch value for the OCR engine configured in the batch class.
	 * 
	 * @param batchClassIdentifier {@link String} batch class identifier.
	 * @param service {@link PluginPropertiesService} plugin property service to get the plugin for getting properties.
	 * @param ocrEngineName {@link String} the name of the ocr engine.
	 * @param ocrEngineSwitch {@link PluginProperty} The plugin switch property for the ocr engine.
	 * @return {@link String} the value for the switch.
	 */
	private String getOcrEngineSwitchValue(final String batchClassIdentifier, final PluginPropertiesService service,
			final String ocrEngineName, final PluginProperty ocrEngineSwitch) {
		String ocrEngineSwitchValue = null;
		final BatchPlugin plugin = service.getPluginProperties(batchClassIdentifier, ocrEngineName);
		if (plugin != null && plugin.getPropertiesSize() > 0) {
			final List<BatchPluginConfiguration> pluginConfigurations = plugin.getPluginConfigurations(ocrEngineSwitch);
			if (pluginConfigurations != null && !pluginConfigurations.isEmpty()) {
				ocrEngineSwitchValue = pluginConfigurations.get(0).getValue();
				LOGGER.info(ocrEngineName + " switch = " + ocrEngineSwitchValue);
			}
		}
		return ocrEngineSwitchValue;
	}

	/**
	 * The list of all the batch class plugins.
	 * 
	 * @param batchClass {@link BatchClass} the batch class for which all the plugins has to be extracted.
	 * @return {@link List} list of batch class plugins.
	 */
	private List<BatchClassPlugin> getOrderedListOfPlugins(final BatchClass batchClass) {
		List<BatchClassPlugin> allBatchClassPlugins = null;
		if (batchClass != null) {
			final List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
			if (batchClassModules != null) {

				if (allBatchClassPlugins == null) {
					allBatchClassPlugins = new ArrayList<BatchClassPlugin>();
				}
				for (final BatchClassModule batchClassModule : batchClassModules) {
					final List<BatchClassPlugin> batchClassPlugins = batchClassModule.getBatchClassPlugins();
					allBatchClassPlugins.addAll(batchClassPlugins);
				}
			}
		}
		return allBatchClassPlugins;
	}

	/**
	 * This Web service helper will create the new document type for the specified Batch Class. If it cant create the Document type it
	 * raises an Exception
	 * 
	 * @param req the {@link HttpServletRequest} The request made by the client encapsulated as an Object
	 * @throws InternalServerException When an uncaught Exception occurs
	 * @throws ValidationException When the Input Parameters cannot be validated
	 */
	public void documentTypeCreation(final HttpServletRequest req) throws InternalServerException, ValidationException {
		String respStr = WebServiceConstants.EMPTY_STRING;
		String workingDir = WebServiceConstants.EMPTY_STRING;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = batchSchemaService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				LOGGER.info("Retreiving xml file.");
				String xmlFileName = WebServiceConstants.EMPTY_STRING;
				xmlFileName = getXMLFile(workingDir, multiPartRequest, fileMap);
				WebServiceParams webServiceParams = null;
				final File xmlFile = new File(workingDir + File.separator + xmlFileName);
				if (xmlFile.exists()) {
					LOGGER.info("Input xml file found, retrieving the parameters from it.");
					final FileInputStream inputStream = new FileInputStream(xmlFile);
					final Source source = XMLUtil.createSourceFromStream(inputStream);
					webServiceParams = (WebServiceParams) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
				} else {
					respStr = WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE;
					final RestError restError = createUnprocessableEntityRestError(respStr,
							WebServiceConstants.INVALID_PARAMETERS_CODE);
					LOGGER.error(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE + WebServiceConstants.HTTP_STATUS
							+ HttpStatus.UNPROCESSABLE_ENTITY);
					throw new ValidationException(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE, restError);
				}
				if (null != webServiceParams.getParams()) {
					final List<Param> paramList = webServiceParams.getParams().getParam();
					if (paramList == null || paramList.isEmpty()) {
						respStr = WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE;
						final RestError restError = createUnprocessableEntityRestError(respStr,
								WebServiceConstants.INVALID_PARAMETERS_CODE);
						LOGGER.error(WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE + WebServiceConstants.HTTP_STATUS
								+ HttpStatus.UNPROCESSABLE_ENTITY);
						throw new InternalServerException(WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE, restError);
					} else {
						LOGGER.info("Initializing input parameters.");
						String documentTypeName = WebServiceConstants.EMPTY_STRING;
						String documentTypeDescription = WebServiceConstants.EMPTY_STRING;
						String minConfidenceThreshold = WebServiceConstants.EMPTY_STRING;
						String formProcessingFile = WebServiceConstants.EMPTY_STRING;
						String secondPageProjectFile = WebServiceConstants.EMPTY_STRING;
						String thirdPageProjectFile = WebServiceConstants.EMPTY_STRING;
						String lastPageProjectFile = WebServiceConstants.EMPTY_STRING;
						String batchClassIdentifier = WebServiceConstants.EMPTY_STRING;
						String hidden = WebServiceConstants.EMPTY_STRING;
						for (final Param param : paramList) {
							if (WebServiceConstants.DOCUMENT_TYPE_NAME.equalsIgnoreCase(param.getName())) {
								documentTypeName = param.getValue().trim();
								LOGGER.info("Document type name entered is :" + documentTypeName);
								continue;
							}
							if (WebServiceConstants.DOCUMENT_TYPE_DESCRIPTION.equalsIgnoreCase(param.getName())) {
								documentTypeDescription = param.getValue().trim();
								LOGGER.info("Document type description given is :" + documentTypeDescription);
								continue;
							}
							if (WebServiceConstants.MIN_CONFIDENCE_THRESHOLD.equalsIgnoreCase(param.getName())) {
								minConfidenceThreshold = param.getValue().trim();
								LOGGER.info("Document type minimum confidence threshold entered is :" + minConfidenceThreshold);
								continue;
							}
							if (WebServiceConstants.FIRST_PAGE_PROJECT_FILE.equalsIgnoreCase(param.getName())) {
								formProcessingFile = param.getValue().trim();
								LOGGER.info(EphesoftStringUtil.concatenate("Document type form processing file entered is : ",
										formProcessingFile));
								continue;
							}

							if (WebServiceConstants.SECOND_PAGE_PROJECT_FILE.equalsIgnoreCase(param.getName())) {
								secondPageProjectFile = param.getValue().trim();
								LOGGER.info(EphesoftStringUtil.concatenate(
										"Document type second page form processing file entered is : ", secondPageProjectFile));
								continue;
							}

							if (WebServiceConstants.THIRD_PAGE_PROJECT_FILE.equalsIgnoreCase(param.getName())) {
								thirdPageProjectFile = param.getValue().trim();
								LOGGER.info(EphesoftStringUtil.concatenate(
										"Document type third page form processing file entered is : ", thirdPageProjectFile));
								continue;
							}

							if (WebServiceConstants.LAST_PAGE_PROJECT_FILE.equalsIgnoreCase(param.getName())) {
								lastPageProjectFile = param.getValue().trim();
								LOGGER.info(EphesoftStringUtil.concatenate(
										"Document type last page form processing file entered is : ", lastPageProjectFile));
								continue;
							}
							if (WebServiceUtil.BATCH_CLASS_IDENTIFIER.equalsIgnoreCase(param.getName())) {
								batchClassIdentifier = param.getValue().trim();
								LOGGER.info("Batch class identifier used for copying batch class is :" + batchClassIdentifier);
								continue;
							}
							if (WebServiceConstants.HIDDEN.equalsIgnoreCase(param.getName())) {
								hidden = param.getValue().trim();
								LOGGER.info("Document type hidden field parameter value entered is :" + hidden);
								continue;
							}
						}

						final List<String> formProcessingFileList = new ArrayList<String>();
						formProcessingFileList.add(formProcessingFile);
						formProcessingFileList.add(secondPageProjectFile);
						formProcessingFileList.add(thirdPageProjectFile);
						formProcessingFileList.add(lastPageProjectFile);
						// Validation for document type creation parameters
						// provided.
						LOGGER.info("Validating parameters.");
						respStr = validateDocumentTypeCreationParameters(documentTypeName, documentTypeDescription,
								minConfidenceThreshold, formProcessingFileList, batchClassIdentifier, hidden);
						// verify for unique entries for batch class and unc
						// folders.
						if (respStr.isEmpty()) {
							// creating a new document type.
							final DocumentType newDocumentType = new DocumentType();
							newDocumentType.setName(documentTypeName);
							newDocumentType.setDescription(documentTypeDescription);
							newDocumentType.setMinConfidenceThreshold(Float.parseFloat(minConfidenceThreshold));
							newDocumentType.setFirstPageProjectFileName(formProcessingFile);
							newDocumentType.setSecondPageProjectFileName(secondPageProjectFile);
							newDocumentType.setThirdPageProjectFileName(thirdPageProjectFile);
							newDocumentType.setFourthPageProjectFileName(lastPageProjectFile);

							final List<PageType> pages = getListOfPageTypes(documentTypeName);
							newDocumentType.setPages(pages);
							if (hidden.equalsIgnoreCase(TRUE)) {
								newDocumentType.setHidden(true);
							} else {
								newDocumentType.setHidden(false);
							}
							final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
							// adding the document type to the requested batch
							// class.
							batchClass.addDocumentType(newDocumentType);
							// merging the batch class for persisting the
							// database state for new document type entry.
							batchClassService.merge(batchClass);
						} else {
							// if the parameters provided didn't validate.
							final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
							final RestError restError = createUnprocessableEntityRestError(respStr,
									WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_CODE);
							LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
							throw new ValidationException(respStr, restError);
						}
					}
				} else {
					respStr = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_MESSAGE;
					throw new ValidationException(respStr, createUnprocessableEntityRestError(respStr,
							WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_CODE));
				}
			} catch (final ValidationException validationException) {
				throw validationException;
			} catch (final InternalServerException internalServerError) {
				throw internalServerError;
			} catch (final Exception exception) {
				// to handle any other kind of exception that has occurred
				// during creation.
				final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
				respStr = WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + exception;
				final RestError restError = createUnprocessableEntityRestError(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
						+ exception.getMessage(), WebServiceConstants.INTERNAL_SERVER_ERROR_CODE);
				LOGGER.error("Error response at server:" + respStr);
				final InternalServerException internalServerExcpetion = new InternalServerException(
						WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
				LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
				throw internalServerExcpetion;
			} finally {
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			respStr = WebServiceConstants.INVALID_MULTIPART_REQUEST;
			final RestError restError = createUnprocessableEntityRestError(respStr, WebServiceConstants.INVALID_PARAMETERS_CODE);
			throw new InternalServerException(respStr, restError);
		}

	}

	/**
	 * Make the list of page types for the given document type name.
	 * 
	 * @param documentTypeName {@link String} the name of the document being created.
	 * @return {@link List <PageType>}
	 */
	private List<PageType> getListOfPageTypes(final String documentTypeName) {
		final List<PageType> pages = new ArrayList<PageType>();
		// Add a first page.
		final PageType firstPage = new PageType();
		firstPage.setDescription(documentTypeName + WebServiceConstants.FIRST_PAGE);
		firstPage.setName(documentTypeName + WebServiceConstants.FIRST_PAGE);
		// Add a middle page.
		final PageType middlePage = new PageType();
		middlePage.setDescription(documentTypeName + WebServiceConstants.MIDDLE_PAGE);
		middlePage.setName(documentTypeName + WebServiceConstants.MIDDLE_PAGE);
		// Add a last page.
		final PageType lastPage = new PageType();
		lastPage.setDescription(documentTypeName + WebServiceConstants.LAST_PAGE);
		lastPage.setName(documentTypeName + WebServiceConstants.LAST_PAGE);
		pages.add(firstPage);
		pages.add(middlePage);
		pages.add(lastPage);
		return pages;
	}

	/**
	 * Validation for the document name required for document type creation web service.
	 * 
	 * @param documentTypeName {@link String} the name of document type which has to be created.
	 * @param batchClassIdentifier {@link String} the batch class identifier in which document type has to be created.
	 * @return {@link String} the resulting string for validation. If empty then all fields are validated otherwise the error statement
	 *         is given in the returned string.
	 */
	private boolean validateDocumentName(final String documentTypeName, final String batchClassIdentifier) {
		boolean docTypeNameUnique = true;
		final String respStr = WebServiceConstants.EMPTY_STRING;
		if (respStr.isEmpty()) {
			final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
			if (batchClass != null) {
				final List<DocumentType> documentList = batchClass.getDocumentTypes();
				if (documentList != null) {
					for (final DocumentType docType : documentList) {
						if (documentTypeName.equalsIgnoreCase(docType.getName())) {
							docTypeNameUnique = false;
							break;
						}

					}
				}
			}
		}
		return docTypeNameUnique;
	}

	/**
	 * Method to validate the input fields for create document type web service.
	 * 
	 * @param documentTypeName {@link String} the name for new document to be created.
	 * @param documentTypeDescription {@link String} the description for the new document type to be created.
	 * @param minConfidenceThreshold {@link String} the minimum threshold value for this document type.
	 * @param formProcessingFileList {@link String[]} list of the type of rsp processing file required.
	 * @param batchClassIdentifier {@link String} the batch class identifier for which this document type has to be created.
	 * @param hidden {@link String} value to state if this document type has to be of type hidden.
	 * @return {@link String} the resulting string for validation. If empty then all fields are validated otherwise the error statement
	 *         is given in the returned string.
	 */
	private String validateDocumentTypeCreationParameters(final String documentTypeName, final String documentTypeDescription,
			final String minConfidenceThreshold, final List<String> formProcessingFileList, final String batchClassIdentifier,
			final String hidden) {
		String errorMessage = WebServiceConstants.EMPTY_STRING;
		boolean isValid = true;
		if (documentTypeName == null || documentTypeName.isEmpty()) {
			errorMessage = "The document type name is empty. Please enter unique name for document type.";
			isValid = false;
		}
		if (isValid && documentTypeName.contains("*")) {
			errorMessage = "The name of document type cannot contain character '*' in it."
					+ " Please enter a unique name without this character.";
			isValid = false;
		}
		if (isValid && (documentTypeDescription == null || documentTypeDescription.isEmpty())) {
			errorMessage = "The batch class description is empty. Please enter description for batch class.";
			isValid = false;
		}
		if (isValid && (minConfidenceThreshold == null || minConfidenceThreshold.isEmpty())) {
			errorMessage = "The minimum confidence threshold cannot be null"
					+ " and should be between 0 and 100 inclusive of both. Please enter correct value.";
			isValid = false;
		}
		if (isValid && (batchClassIdentifier == null || batchClassIdentifier.isEmpty())) {
			errorMessage = "The batch class identifier is empty. Please enter proper batch class identifier.";
			isValid = false;
		} else {
			if (isValid) {
				errorMessage = validateBatchClassIdentifier(batchClassIdentifier);
				if (!errorMessage.isEmpty()) {
					isValid = false;
				}
			}
		}
		if (isValid && (documentTypeName.length() > MAX_CHARACTER_LIMIT)) {
			errorMessage = "Batch class name must not be more than 255 characters.";
			isValid = false;
		}
		if (isValid && !(TRUE.equalsIgnoreCase(hidden) || FALSE.equalsIgnoreCase(hidden))) {
			errorMessage = "The hidden property value entered is incorrect."
					+ " Please give true/false for this property in xml file.";
			isValid = false;
		}

		if (isValid && !validateDocumentName(documentTypeName, batchClassIdentifier)) {
			errorMessage = "Document type name is not unique. Please enter a unique name for document type.";
			isValid = false;
		}

		for (String formProcessingFile : formProcessingFileList) {
			if (!(EphesoftStringUtil.isNullOrEmpty(formProcessingFile))) {
				errorMessage = validateProcessingFile(formProcessingFile, batchClassIdentifier);
				if (EphesoftStringUtil.isNullOrEmpty(errorMessage)) {
					isValid = false;
				}
			}
		}

		if (isValid) {
			try {
				final float confidenceThreshold = Float.parseFloat(minConfidenceThreshold);
				if (confidenceThreshold < MIN_CONFIDENCE_THRESHOLD_VALUE || confidenceThreshold > MAX_CONFIDENCE_THRESHOLD_VALUE) {
					errorMessage = "The minimum threshold for confidence should be a numeric value"
							+ " between 0 and 100 inclusive of both.";
					isValid = false;
				}
			} catch (final NumberFormatException numberFormatException) {
				errorMessage = "The value entered of minimum threshold for confidence should be a numeric value.";
				isValid = false;
			}
		}

		return errorMessage;
	}

	/**
	 * This method is used to get the input xml file.
	 * 
	 * @param workingDir {@link String} the working directory created for web service execution.
	 * @param multiPartRequest {@link DefaultMultipartHttpServletRequest} request to get the multipart from request.
	 * @param fileMap {@link MultiValueMap} this map contains the input files.
	 * @return string
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static String getXMLFile(final String workingDir, final DefaultMultipartHttpServletRequest multiPartRequest,
			final MultiValueMap<String, MultipartFile> fileMap) throws IOException {
		InputStream instream = null;
		OutputStream outStream = null;
		String xmlFileName = WebServiceConstants.EMPTY_STRING;
		LOGGER.info("Checking for input file.");
		for (final String fileName : fileMap.keySet()) {
			try {
				if (fileName.endsWith(FileType.XML.getExtensionWithDot())) {
					xmlFileName = fileName;
				}
				final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
				instream = multiPartFile.getInputStream();
				final File file = new File(workingDir + File.separator + fileName);
				outStream = new FileOutputStream(file);
				final byte[] buf = new byte[WebServiceUtil.bufferSize];
				int len;
				while ((len = instream.read(buf)) > 0) {
					outStream.write(buf, 0, len);
				}
			} finally {
				IOUtils.closeQuietly(instream);
				IOUtils.closeQuietly(outStream);
			}
		}
		return xmlFileName;
	}

	/**
	 * Validations for batch class identifier.
	 * 
	 * @param batchClassIdentifier {@link String} the batch class identifier.
	 * @return {@link String} null if input is validated otherwise the result string of validation.
	 */
	private String validateBatchClassIdentifier(final String batchClassIdentifier) {
		String respStr = WebServiceConstants.EMPTY_STRING;
		LOGGER.info("Validating unique value for batch class identifier.");
		if (batchClassIdentifier == null || batchClassIdentifier.isEmpty()) {
			respStr = "The batch class identifier is empty. Please enter proper batch class identifier.";
		}
		if (respStr.isEmpty() && batchClassService.getBatchClassByIdentifier(batchClassIdentifier) == null) {
			respStr = "Batch class identifier provided does not exist. Please enter a valid value.";
		}
		return respStr;
	}

	/**
	 * Method to extract all required fields and process request.
	 * 
	 * @param req {@link HttpServletRequest} the request header for web service hit.
	 * @throws InternalServerException
	 * @throws ValidationException
	 */
	public void processCopyBatchClass(final HttpServletRequest req) throws InternalServerException, ValidationException {
		String respStr = WebServiceConstants.EMPTY_STRING;
		String workingDir = WebServiceConstants.EMPTY_STRING;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = batchSchemaService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				LOGGER.info("Retreiving xml file.");
				if (respStr.isEmpty()) {
					String xmlFileName = WebServiceConstants.EMPTY_STRING;
					xmlFileName = getXMLFile(workingDir, multiPartRequest, fileMap);
					WebServiceParams webServiceParams = null;
					final File xmlFile = new File(workingDir + File.separator + xmlFileName);
					if (xmlFile.exists()) {
						LOGGER.info("Input xml file found, retrieving the parameters from it.");
						final FileInputStream inputStream = new FileInputStream(xmlFile);
						final Source source = XMLUtil.createSourceFromStream(inputStream);
						webServiceParams = (WebServiceParams) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
					} else {
						createAndThrowMissingXmlException(WebServiceConstants.INPUT_XML_NOT_FOUND_CODE,
								WebServiceConstants.INPUT_XML_NOT_FOUND_MESSAGE);
					}
					final List<Param> paramList = webServiceParams.getParams().getParam();
					if (paramList == null || paramList.isEmpty()) {
						final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
						final RestError restError = new RestError(status, WebServiceConstants.PARAMETER_XML_INCORRECT_CODE,
								WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE,
								WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
								WebServiceConstants.DEFAULT_URL);
						LOGGER.error(status + WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE);
						throw new ValidationException(WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE, restError);
					} else {
						LOGGER.info("Initializing input parameters.");
						String batchClassName = WebServiceConstants.EMPTY_STRING;
						String batchClassDescription = WebServiceConstants.EMPTY_STRING;
						String batchClassPriority = WebServiceConstants.EMPTY_STRING;
						String uncFolderName = WebServiceConstants.EMPTY_STRING;
						String batchClassIdentifier = WebServiceConstants.EMPTY_STRING;
						String gridWorkflow = WebServiceConstants.EMPTY_STRING;
						for (final Param param : paramList) {
							if ((WebServiceConstants.BATCH_CLASS_NAME).equalsIgnoreCase(param.getName())) {
								batchClassName = param.getValue().trim();
								LOGGER.info("Batch class name entered is :" + batchClassName);
								continue;
							}
							if ((WebServiceConstants.BATCH_CLASS_DESCRIPTION).equalsIgnoreCase(param.getName())) {
								batchClassDescription = param.getValue().trim();
								LOGGER.info("Batch class description given is :" + batchClassDescription);
								continue;
							}
							if ((WebServiceConstants.BATCH_CLASS_PRIORITY).equalsIgnoreCase(param.getName())) {
								batchClassPriority = param.getValue().trim();
								LOGGER.info("Batch class priority entered is :" + batchClassPriority);
								continue;
							}
							if ((WebServiceConstants.BATCH_UNC_FOLDER_NAME).equalsIgnoreCase(param.getName())) {
								uncFolderName = param.getValue().trim();
								LOGGER.info("UNC folder path entered is : " + uncFolderName);
								continue;
							}
							if ((WebServiceConstants.COPY_BATCH_CLASS_IDENTIFIER).equalsIgnoreCase(param.getName())) {
								batchClassIdentifier = param.getValue().trim();
								LOGGER.info("Batch class identifier used for copying batch class is :" + batchClassIdentifier);
								continue;
							}
							if ((WebServiceConstants.IS_GRIDWORKFLOW).equalsIgnoreCase(param.getName())) {
								gridWorkflow = param.getValue().trim();
								LOGGER.info("Grid workflow is :" + gridWorkflow);
								continue;
							}
						}
						// Validation for batch class creation parameters
						// provided.
						LOGGER.info("Validating parameters.");
						respStr = validateCreateBatchClassParameters(batchClassName, batchClassDescription, batchClassPriority,
								uncFolderName, batchClassIdentifier, gridWorkflow);
						// verify for unique entries for batch class and unc
						// folders.
						if (respStr.isEmpty()) {
							respStr = validateBatchClassAndUNC(batchClassName, uncFolderName, batchClassIdentifier);
							if (respStr.isEmpty()) {
								// After validation creating batch class.
								Set<String> superAdminGroups = userConnectivityService.getAllSuperAdminGroups();
								if (superAdminGroups != null && !superAdminGroups.isEmpty()) {
									performBatchClassCreation(batchClassName, batchClassDescription, batchClassPriority,
											uncFolderName, batchClassIdentifier, superAdminGroups.toArray()[0].toString());
									BatchClass batchClass = batchClassService.getBatchClassbyUncFolder(uncFolderName);
									List<BatchClassGroups> assignedRoles = new ArrayList<BatchClassGroups>();
									BatchClassGroups bcGrup = new BatchClassGroups();
									bcGrup.setBatchClass(batchClass);
									for (int itr = 1; itr < superAdminGroups.size(); itr++) {
										bcGrup.setGroupName(superAdminGroups.toArray()[itr].toString());
									}
									assignedRoles.add(bcGrup);
									// batchClass.setAssignedGroups(assignedRoles);
									batchClassService.saveOrUpdate(batchClass);
									// JIRA-BUG-ID-11125
									deploymentService.createAndDeployProcessDefinition(batchClass, false);
								} else {
									respStr = "Error in retreving admin groups.Please check user-connectivity settings.";
									final HttpStatus status = HttpStatus.FORBIDDEN;
									final RestError restError = new RestError(status, WebServiceConstants.ADMIN_ROLES_NOT_FOUND,
											respStr, WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
									LOGGER.error("Error in retreving admin groups.Please check user-connectivity settings" + status);
									throw new ValidationException(respStr, restError);
								}
							} else {
								final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
								final RestError restError = new RestError(status, WebServiceConstants.VALIDATION_EXCEPTION_CODE,
										respStr, WebServiceConstants.VALIDATION_EXCEPTION_MESSAGE
												+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
								LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
								throw new ValidationException(respStr, restError);
							}
						} else {
							final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
							final RestError restError = new RestError(status, WebServiceConstants.VALIDATION_EXCEPTION_CODE, respStr,
									WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
							LOGGER.error("Error in Validating Input XML " + respStr + status);
							throw new ValidationException(respStr, restError);
						}
					}
				}
			} catch (org.xml.sax.SAXParseException Ex) {
				respStr = "Error in Parsing Input XML.Please try again";
				final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
						WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_CODE, respStr,
						WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
						WebServiceConstants.DEFAULT_URL);
				LOGGER.error("Error response at server:" + respStr);
				throw new ValidationException(respStr, restError);
			} catch (final FileNotFoundException fe) {
				respStr = "Input XMl file is not found.Please try again";
				final RestError restError = new RestError(HttpStatus.NOT_FOUND, WebServiceConstants.INPUT_XML_NOT_FOUND_CODE, respStr,
						WebServiceConstants.INPUT_XML_NOT_FOUND_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
						WebServiceConstants.DEFAULT_URL);
				LOGGER.error("Error response at server:" + respStr + fe);
				throw new ValidationException(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
			} catch (ValidationException validationException) {
				throw validationException;
			} catch (InternalServerException validationException) {
				throw validationException;
			} catch (final Exception exception) {
				final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
				respStr = WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + exception;
				final RestError restError = new RestError(status, WebServiceConstants.INTERNAL_SERVER_ERROR_CODE, respStr, respStr
						+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
				LOGGER.error("Error response at server:" + respStr + exception);
				throw new InternalServerException(respStr, restError);
			} finally {
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
			final RestError restError = new RestError(status, WebServiceConstants.VALIDATION_EXCEPTION_CODE,
					WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE, respStr + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
					WebServiceConstants.DEFAULT_URL);
			LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
			throw new ValidationException(respStr, restError);
		}
	}

	/**
	 * This method is used to check for batch class name and unc folder path uniqueness.
	 * 
	 * @param batchClassName {@link String} this is the name of batch class to be created.
	 * @param uncFolderPath {@link String} this is the unc folder path for batch class to be created.
	 * @param batchClassIdentifier {@link String} this is the identifier for thebatch class from which new batch class has to be
	 *            copied.
	 * @return {@link String} the resulting string for batch class parameter validation. If string is not empty then it means some
	 *         error has occurred.
	 */
	private String validateBatchClassAndUNC(final String batchClassName, final String uncFolderPath, final String batchClassIdentifier) {
		boolean isValid = true;
		String respStr = WebServiceConstants.EMPTY_STRING;
		LOGGER.info("Validating unique values for batch class name and unc folder path inputs.");
		// Checking for batch class identifier present or not.
		if (batchClassService.getBatchClassByIdentifier(batchClassIdentifier) == null) {
			respStr = "Batch class identifier provided to copy batch class does not exist. Please enter a valid value.";
			isValid = false;
		}
		// Checking for unique batch class name.
		if (isValid && batchClassService.getBatchClassbyName(batchClassName) != null) {
			respStr = "Batch class name is not unique. Please enter a unique name for batch class.";
			isValid = false;
		}
		// Checking for unique batch class unc folder.
		if (isValid) {
			final List<BatchClass> batchClassList = batchClassService.getAllBatchClasses();
			for (final BatchClass batchClass : batchClassList) {
				if (batchClass.getUncFolder().endsWith(uncFolderPath)) {
					respStr = "Batch class UNC folder is not unique." + " Please enter a unique UNC folder path for batch class.";
					break;
				}
			}
		}
		return respStr;
	}

	/**
	 * This method is used to get the call for batch class creation.
	 * 
	 * @param batchClassName {@link String} batch class name for creating new batch class.
	 * @param batchClassDescription {@link String} batch class description for creating new batch class.
	 * @param batchClassPriority {@link String} batch class priority for creating new batch class.
	 * @param uncFolderPath {@link String} batch class unc path for creating new batch class.
	 * @param batchClassIdentifier {@link String} batch class identifier to be used for copying batch class.
	 * @param batchClassGroup {@link String} group to which this batch class has to be assigned.
	 * @param configureExportFolder {@link String} property used for configuring the export folder for new batch class.
	 * @throws InternalServerException
	 */
	private void performBatchClassCreation(final String batchClassName, final String batchClassDescription,
			final String batchClassPriority, final String uncFolderPath, final String batchClassIdentifier,
			final String batchClassGroup) throws InternalServerException {
		LOGGER.info("Input parameters validated, creating batch class:");
		try {
			// Calling the batch class copy method for batch class service.
			final BatchClass copiedBatchClass = batchClassService.copyBatchClass(batchClassIdentifier, batchClassName,
					batchClassDescription, batchClassGroup, batchClassPriority, uncFolderPath, true);
			// Copied Batch Class should get deployed.
			if (null != copiedBatchClass) {
				deploymentService.createAndDeployProcessDefinition(copiedBatchClass, false);
			}
		} catch (final Exception exception) {
			final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			final RestError restError = new RestError(status, WebServiceConstants.INTERNAL_SERVER_ERROR_CODE,
					WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
							+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
			LOGGER.error(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + exception.getMessage());
			throw new InternalServerException("Could not create batch class due to internal error", restError);
		}
	}

	/**
	 * Perform the learning for provided batch class identifier.
	 * 
	 * @param batchClassIdentifier {@link String} the identifier for the batch class for which learning has to be done.
	 * @throws InternalServerException
	 * @throws ValidationException
	 */
	public void performLearningForBatchClass(final String batchClassIdentifier) throws InternalServerException, ValidationException {
		String respStr = WebServiceConstants.EMPTY_STRING;
		// validating the input for batch class identifier.
		respStr = validateBatchClassIdentifier(batchClassIdentifier);
		if (respStr.isEmpty()) {
			try {
				learnFileForBatchClass(batchClassIdentifier);
			} catch (final Exception e) {
				final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
				respStr = WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE;
				final RestError restError = new RestError(status, WebServiceConstants.IMPROPER_INPUT_TO_SERVER_CODE,
						WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE, WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE
								+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
				LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
				throw new InternalServerException(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE, restError);
			}
		} else {
			final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
			final RestError restError = new RestError(status, WebServiceConstants.VALIDATION_EXCEPTION_CODE, respStr, respStr
					+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
			LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
			throw new ValidationException(respStr, restError);
		}
	}

	/**
	 * This method is used to validate the input parameters for create batch class parameters.
	 * 
	 * @param batchClassName {@link String} batch class name to be created.
	 * @param batchClassDescription {@link String} batch class description to be created.
	 * @param batchClassPriority {@link String} batch class priority to be created.
	 * @param batchUNCFolder {@link String} batch class unc folder path to be created.
	 * @param batchClassID {@link String} batch class identifier from which it has to be copied.
	 * @param gridWorkflow {@link String} batch class grid workflow deployment variable.
	 * @return {@link String} return string carries the result of validation. If string is empty then it means that parameters are
	 *         validated, otherwise the error statement is returned.
	 */
	public static String validateCreateBatchClassParameters(final String batchClassName, final String batchClassDescription,
			final String batchClassPriority, final String batchUNCFolder, final String batchClassID, final String gridWorkflow) {
		String respStr = WebServiceConstants.EMPTY_STRING;
		boolean isValid = true;
		if (batchClassName == null || batchClassName.isEmpty()) {
			respStr = "The batch class name is empty. Please enter unique name for batch class.";
			isValid = false;
		}
		if (isValid && (batchClassDescription == null || batchClassDescription.isEmpty())) {
			respStr = "The batch class description is empty. Please enter description for batch class.";
			isValid = false;
		}
		if (isValid && (batchClassPriority == null || batchClassPriority.isEmpty())) {
			respStr = "The batch class priority is empty. Please enter priority for batch class.";
			isValid = false;
		}
		if (isValid && (batchUNCFolder == null || batchUNCFolder.isEmpty())) {
			respStr = "The batch class UNC folder is empty. Please enter proper UNC folder path for batch class.";
			isValid = false;
		}
		if (isValid && (batchClassID == null || batchClassID.isEmpty())) {
			respStr = "The batch class identifier is empty. Please enter proper batch class identifier.";
			isValid = false;
		}
		if (isValid && (batchClassName.contains(" ") || batchClassName.contains("-"))) {
			respStr = "Batch class name must not contain a space or a hyphen.";
			isValid = false;
		}
		if (isValid && (gridWorkflow == null || gridWorkflow.isEmpty())) {
			respStr = "Grid workflow must not be empty. Please enter a valid value.";
			isValid = false;
		}
		if (isValid && !(TRUE.equalsIgnoreCase(gridWorkflow) || FALSE.equalsIgnoreCase(gridWorkflow))) {
			respStr = "Grid workflow must be either true or false. Please enter a valid value.";
			isValid = false;
		}
		if (isValid && (batchClassName.length() > MAX_CHARACTER_LIMIT)) {
			respStr = "Batch class name must not be more than 255 characters.";
			isValid = false;
		}
		if (isValid) {
			try {
				final int priority = Integer.parseInt(batchClassPriority);
				if (priority < MIN_PRIORITY_VALUE || priority > MAX_PRIORITY_VALUE) {
					respStr = "The batch class priority should be a numeric value between 0 and 100 inclusive of both.";
					isValid = false;
				}
			} catch (final NumberFormatException numberFormatException) {
				respStr = "The value entered for priority should be a numeric value.";
				isValid = false;
			}
		}
		return respStr;
	}

	/**
	 * Extracting the xml structure for exception received.
	 * 
	 * @param statusCode {@link HttpStatus} the status code for received exception.
	 * @param cause {@link String} the cause description to the error.
	 * @param customCode int the custom code maintained for this specific error cause.
	 * @return {@link RootElement} the root object of xml structure returned.
	 */
	public RootElement getXMLStructureObjectForError(final HttpStatus statusCode, final String cause, final int customCode) {
		rootElement = new RootElement();
		responseCodeElement = new ResponseCodeElement();
		errorElement = new ErrorElement();
		responseCodeElement.setHttpCode(statusCode.value());
		responseCodeElement.setResult(WebServiceConstants.ERROR);
		rootElement.setResponseCode(responseCodeElement);
		errorElement.setCause(cause);
		errorElement.setCustomCode(customCode);
		errorElement.setInfoUrl(WebServiceConstants.DEFAULT_URL);
		rootElement.setError(errorElement);
		return rootElement;
	}

	/**
	 * This is an helper API for the request of getting Batch Class Identifiers by role to the user. It returns the {@link ListValues}
	 * corresponding to the Object
	 * 
	 * @param role {@link String} The Role Corresponding to which the Batch Class instances are required
	 * @return {@link ListValues} of the Batch Class identifiers corresponding to the role
	 * @throws ValidationException When the input cannot be validated to the request
	 */
	public ListValues getBatchClassIdentifiersForRole(final String role) throws ValidationException {
		final Set<String> batchClassIdentifiers = new TreeSet<String>();
		if (!StringUtils.isEmpty(role) && userConnectivityService.getAllGroups().contains(role)) {
			LOGGER.info("Given role: " + role + " is valid.");
			if (userConnectivityService.getAllSuperAdminGroups().contains(role)) {
				LOGGER.info("Given role:" + role + " is super admin.");
				final List<String> results = batchClassService.getAllBatchClassIdentifier();
				if (CollectionUtils.isNotEmpty(results)) {
					batchClassIdentifiers.addAll(results);
				}
			} else {
				final Set<String> userRoles = new HashSet<String>();
				userRoles.add(role);
				LOGGER.info("Fetching batch classes for user role" + role);
				final Set<String> results = batchClassGroupsService.getBatchClassIdentifierForUserRoles(userRoles, false);
				if (CollectionUtils.isNotEmpty(results)) {
					batchClassIdentifiers.addAll(results);
				}
			}
		} else {
			throw new ValidationException(WebServiceConstants.INVALID_ROLE_ERROR_MESSAGE, createUnprocessableEntityRestError(
					WebServiceConstants.INVALID_ROLE_ERROR_MESSAGE, WebServiceConstants.INVALID_ROLE_ERROR_CODE));
		}
		final ListValues list = new ListValues();
		list.getValue().addAll(batchClassIdentifiers);
		return list;
	}

	/**
	 * Gets the batch instance identifiers for role corresponding to the role passed as an argument. It process the request and returns
	 * the {@link ListValues} corresponding to the role. and throws an {@link ValidationException} when could not validate the input
	 * role
	 * 
	 * @param role {@link String} role corresponding to which the Batch instances are required
	 * @return {@link ListValues} of the Batch Instances
	 * @throws ValidationException when could not validate the Input role.
	 */
	public ListValues getBatchInstanceIdentifiersForRole(final String role) throws ValidationException {
		final Set<String> batchInstanceIdentifiers = new TreeSet<String>();
		if (!StringUtils.isEmpty(role) && userConnectivityService.getAllGroups().contains(role)) {
			LOGGER.info("Given role:" + role + " is valid.");
			List<BatchClass> batchClassList;
			if (userConnectivityService.getAllSuperAdminGroups().contains(role)) {
				LOGGER.info("Given role:" + role + " is super admin.");
				batchClassList = batchClassService.getAllBatchClasses();
			} else {
				final Set<String> userRoles = new HashSet<String>();
				userRoles.add(role);
				batchClassList = batchClassService.getAllBatchClassesByUserRoles(userRoles);
			}
			if (CollectionUtils.isNotEmpty(batchClassList)) {
				List<BatchInstance> eachBatchInstance;
				for (final BatchClass batchClass : batchClassList) {
					// TODO : service hit should not be inside the loop.
					eachBatchInstance = batchInstanceService.getBatchInstByBatchClass(batchClass);
					for (final BatchInstance batchInstance : eachBatchInstance) {
						batchInstanceIdentifiers.add(batchInstance.getIdentifier());
					}
				}
			}
		} else {
			throw new ValidationException(WebServiceConstants.INVALID_ROLE_ERROR_MESSAGE, createUnprocessableEntityRestError(
					WebServiceConstants.INVALID_ROLE_ERROR_MESSAGE, WebServiceConstants.INVALID_ROLE_ERROR_CODE));
		}
		final ListValues list = new ListValues();
		list.getValue().addAll(batchInstanceIdentifiers);
		return list;
	}

	/**
	 * Gets the batch instance list on the basis of passed status, roles of the logged in user and flag whether the user is super admin
	 * or not.
	 * 
	 * @param statusParam the batch instance status
	 * @param loggedInUserRole the logged in user role
	 * @param isSuperAdmin the flag if the user is super admin
	 * @return the batch instance list
	 * @throws ValidationException
	 * @throws ValidationException the validation exception
	 */
	public BatchInstances getBatchInstanceList(final String statusParam, final Set<String> loggedInUserRole, final boolean isSuperAdmin)
			throws ValidationException {
		final BatchInstances batchInstances = new BatchInstances();
		boolean isStatusValid = false;
		String status = null;
		final List<String> statusList = BatchInstanceStatus.valuesAsStringList();
		for (final String statusItem : statusList) {
			if (statusItem.equalsIgnoreCase(statusParam)) {
				status = statusItem;
				isStatusValid = true;
				break;
			}
		}
		if (!isStatusValid) {
			throw new ValidationException(WebServiceConstants.INVALID_BATCH_INSTANCE_STATUS_MESSAGE, new RestError(
					HttpStatus.UNPROCESSABLE_ENTITY, WebServiceConstants.INVALID_BATCH_INSTANCE_STATUS_CODE,
					WebServiceConstants.INVALID_BATCH_INSTANCE_STATUS_MESSAGE,
					WebServiceConstants.INVALID_BATCH_INSTANCE_STATUS_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
					WebServiceConstants.DEFAULT_URL));
		} else {
			final Set<String> batchInstancesId = new TreeSet<String>();
			final BatchInstanceStatus batchInstanceStatus = BatchInstanceStatus.valueOf(status);
			LOGGER.info("Batch instance status is " + status);
			LOGGER.info("Fetching batch instance list from the database");
			final List<BatchInstance> batchInstance = batchInstanceService.getBatchInstByStatus(batchInstanceStatus);
			if (CollectionUtils.isNotEmpty(batchInstance)) {
				if (!isSuperAdmin) {
					// fetch the batch instances from batch instance groups
					final Set<String> batchInstancesIdentifiers = batchInstanceGroupsService
							.getBatchInstanceIdentifierForUserRoles(loggedInUserRole);

					if (CollectionUtils.isNotEmpty(batchInstancesIdentifiers)) {
						batchInstancesId.addAll(batchInstancesIdentifiers);
					}

					// fetch the list of batch instances from the batch instance
					// table for batch classes having the given role.
					final List<BatchClass> batchClasses = batchClassService.getAllBatchClassesByUserRoles(loggedInUserRole);
					List<BatchInstance> eachBatchInstance;
					for (final BatchClass batchClass : batchClasses) {
						// TODO : service hit should not be inside a loop.
						// Modify the API to return list of Identifiers
						// only, rather than complete objects.
						eachBatchInstance = batchInstanceService.getBatchInstByBatchClass(batchClass);
						for (final BatchInstance bi : eachBatchInstance) {
							batchInstancesId.add(bi.getIdentifier());
						}
					}
				} else {
					for (final BatchInstance bi : batchInstance) {
						batchInstancesId.add(bi.getIdentifier());
					}
				}
				LOGGER.info("Fetched list of batch instances from the batch instance table"
						+ " for batch classes having the given role:");
				LOGGER.info(batchInstancesId.toString());
				final List<com.ephesoft.dcma.batch.schema.BatchInstances.BatchInstance> batchInstanceList = batchInstances
						.getBatchInstance();

				com.ephesoft.dcma.batch.schema.BatchInstances.BatchInstance batchLocal;
				for (final BatchInstance eachBatchInstance : batchInstance) {
					if (batchInstancesId.contains(eachBatchInstance.getIdentifier())) {
						batchLocal = new com.ephesoft.dcma.batch.schema.BatchInstances.BatchInstance();
						batchLocal.setIdentifier(eachBatchInstance.getIdentifier());
						batchLocal.setBatchName(eachBatchInstance.getBatchName());
						batchLocal.setCurrentUser(eachBatchInstance.getCurrentUser());
						batchLocal.setExecutedModules(eachBatchInstance.getExecutedModules());
						batchLocal.setLocalFolder(eachBatchInstance.getLocalFolder());
						batchLocal.setRemoteBatchInstanceId(eachBatchInstance.getRemoteBatchInstance() != null ? eachBatchInstance
								.getRemoteBatchInstance().getRemoteBatchInstanceIdentifier() : null);
						batchLocal.setReviewOperatorName(eachBatchInstance.getReviewUserName());
						batchLocal.setServerIP(eachBatchInstance.getServerIP());
						batchLocal.setUncSubFolder(eachBatchInstance.getUncSubfolder());
						batchLocal.setValidateOperatorName(eachBatchInstance.getValidationUserName());
						batchInstanceList.add(batchLocal);
					}
				}
			}
		}
		return batchInstances;
	}

	/**
	 * Gets the all WorkFlow name and the module name of the Batch Class Identifier as requested by the Client in the Request
	 * 
	 * @param loggedInUserRole {@link Set} Roles specifed for the user
	 * @param identifier {@link String} Batch Class identifier which is the Name of the Batch Class Identifier
	 * @param isBatchClassViewable boolean It is t5he flag which determines that the Batch Class is Visible to the user or not
	 * @return {@link Modules} All the modules and the work Flow names
	 * @throws ValidationException The validation exception - in case some parameter is not valid.
	 * @throws UnAuthorisedAccessException the un authorised access exception - in case user does not have access to the passed batch
	 *             class.
	 */
	public Modules getAllModulesWorkflowNameByBatchClass(final Set<String> loggedInUserRole, final String identifier,
			final boolean isBatchClassViewable) throws ValidationException, UnAuthorisedAccessException {
		final Modules modulesSchema = new Modules();
		if (CollectionUtils.isEmpty(loggedInUserRole)) {
			throw new UnAuthorisedAccessException();
		} else {
			final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(identifier);
			if (batchClass != null) {
				if (isBatchClassViewable) {
					final List<BatchClassModule> modules = batchClass.getBatchClassModules();
					for (final BatchClassModule bcm : modules) {
						final Module module = new Module();
						module.setModuleName(bcm.getModule().getName());
						module.setWorkflowName(bcm.getWorkflowName());
						modulesSchema.getModule().add(module);
					}
				} else {
					throw new UnAuthorisedAccessException();
				}
			} else {
				throw new ValidationException(WebServiceConstants.INVALID_BATCH_CLASS_ID_MESSAGE + identifier,
						createUnprocessableEntityRestError(WebServiceConstants.INVALID_BATCH_CLASS_ID_MESSAGE,
								WebServiceConstants.INVALID_BATCH_CLASS_ID_CODE));
			}
		}
		return modulesSchema;
	}

	/**
	 * This Web Service Helper restarts the batch Instance whose identifier is passed as an argument. This whill throw an exception if
	 * it could not restart the Batch else would process the request and return a success/failure Message
	 * 
	 * @param identifier {@link String} The identity of the Batch instance Batch Instance Identifier
	 * @param moduleName {@link String} name of the Module from which the batch is supposed to be restarted
	 * @param loggedInUserRole {@link Set} The roles assigned to the logged in user
	 * @param isSuperAdmin {@link Boolean} The flag which determines whether the user is a super admin or not
	 * @return Success or error message after processing the request. Which is the information regarding the process
	 * @throws UnAuthorisedAccessException When the user is not authenticated to make the request
	 * @throws ValidationException When the input parameters are not validated
	 * @throws InternalServerException When the Server could not process the request due to some reason or some lock acquired on the
	 *             batch class
	 */
	public String restartBatchInstance(final String identifier, final String moduleName, final Set<String> loggedInUserRole,
			final boolean isSuperAdmin) throws UnAuthorisedAccessException, ValidationException, InternalServerException {
		boolean isRestarted = false;
		String moduleWorkflowNameToRestart = null;
		if (!EphesoftStringUtil.isNullOrEmpty(moduleName)) {
			if (StringUtils.isNotBlank(identifier)) {
				boolean isSuccess = false;
				LOGGER.info("Start processing of restarting batch for batch instance:" + identifier);
				final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(identifier);
				// only batch instance with these status can be restarted
				if (batchInstance == null) {
					throw new ValidationException(WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_MESSAGE
							+ getAdditionalInfo(identifier), createUnprocessableEntityRestError(
							WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_MESSAGE + getAdditionalInfo(identifier),
							WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_CODE));
				} else if (BatchInstanceStatus.DELETED.equals(batchInstance.getStatus())) {
					LOGGER.error("Error response at server: " + WebServiceConstants.BATCH_INSTANCE_ALREADY_DELETED_MESSAGE
							+ getAdditionalInfo(identifier));
					throw new ValidationException(WebServiceConstants.BATCH_INSTANCE_ALREADY_DELETED_MESSAGE
							+ getAdditionalInfo(identifier), createUnprocessableEntityRestError(
							WebServiceConstants.BATCH_INSTANCE_ALREADY_DELETED_MESSAGE + getAdditionalInfo(identifier),
							WebServiceConstants.BATCH_INSTANCE_ALREADY_DELETED_CODE));

				} else if (!BatchInstanceStatus.restartableStatusList().contains(batchInstance.getStatus())) {
					throw new ValidationException(WebServiceConstants.BATCH_INSTANCE_CANNOT_BE_RESTARTED_MESSAGE
							+ getAdditionalInfo(identifier + " " + batchInstance.getStatus()), createUnprocessableEntityRestError(
							WebServiceConstants.BATCH_INSTANCE_CANNOT_BE_RESTARTED_MESSAGE
									+ getAdditionalInfo(identifier + " " + batchInstance.getStatus()),
							WebServiceConstants.BATCH_INSTANCE_CANNOT_BE_DELETED_CODE));
				} else if (StringUtils.isNotBlank(batchInstance.getCurrentUser())) {
					// the batch is locked by some user, cannot be
					// restarted/deleted
					LOGGER.error("Error response at server: " + WebServiceConstants.BATCH_INSTANCE_LOCKED_MESSAGE
							+ getAdditionalInfo(identifier));
					throw new InternalServerException(WebServiceConstants.BATCH_INSTANCE_LOCKED_MESSAGE
							+ getAdditionalInfo(identifier), createUnprocessableEntityRestError(
							WebServiceConstants.BATCH_INSTANCE_LOCKED_MESSAGE + getAdditionalInfo(identifier),
							WebServiceConstants.BATCH_INSTANCE_LOCKED_CODE));
				} else {
					LOGGER.info("Batch is in the valid state to restart.Restarting batch instance:" + batchInstance);
					final Set<String> batchInstanceRoles = batchInstanceService.getRolesForBatchInstance(batchInstance);
					if (isSuperAdmin || batchInstanceRoles.removeAll(loggedInUserRole)) {
						LOGGER.info("User is authorized to perform operation on the batch instance:" + identifier);
						final String batchClassIdentifier = batchInstanceService.getBatchClassIdentifier(identifier);
						final String executedBatchInstanceModules = batchInstance.getExecutedModules();
						// if some of the modules are already executed
						if (StringUtils.isNotBlank(executedBatchInstanceModules)) {
							final String[] executedModulesArray = executedBatchInstanceModules.split(";");
							if (batchClassIdentifier != null) {
								LOGGER.info("Restarting the batch instance for the  batch class:" + batchClassIdentifier);
								final BatchClassModule batchClassModuleItem = batchClassModuleService.getBatchClassModuleByName(
										batchClassIdentifier, moduleName);
								if (batchClassModuleItem != null) {
									for (final String executedModule : executedModulesArray) {
										if (executedModule.equalsIgnoreCase(String.valueOf(batchClassModuleItem.getModule().getId()))) {
											moduleWorkflowNameToRestart = batchClassModuleItem.getWorkflowName();
											isSuccess = true;
											break;
										}
									}
								}
							}
						} else {
							// if none of the modules has executed
							isSuccess = true;
							final List<BatchClassModule> batchClassModuleList = batchInstance.getBatchClass().getBatchClassModules();
							moduleWorkflowNameToRestart = batchClassModuleList.get(0).getWorkflowName();
							LOGGER.info("Restarting the batch from first module." + moduleWorkflowNameToRestart
									+ " as the executed module list is empty.");
						}
						final boolean isZipSwitchOn = batchSchemaService.isZipSwitchOn();
						LOGGER.info("Zipped Batch XML switch is: " + isZipSwitchOn);

						if (isSuccess) {
							LOGGER.info("All parameters for restarting the batch are valid.");
							try {
								isRestarted = processRestartingBatchInternal(identifier, moduleWorkflowNameToRestart, batchInstance,
										batchClassIdentifier, isZipSwitchOn);
							} catch (final Exception exception) {
								LOGGER.error("Error in restarting batch instance " + identifier + exception, exception);

								// update the batch instance to ERROR state and
								// get Exception
								if (null != workflowService) {
									workflowService.handleErrorBatch(batchInstance, exception, exception.getMessage());
								}
							}
						} else {
							final List<BatchClassModule> batchClassModules = batchClassModuleService
									.getAllBatchClassModulesByIdentifier(batchClassIdentifier);
							final String[] executedModulesArray = executedBatchInstanceModules.split(";");
							final Set<String> executedWorkflows = new HashSet<String>();
							String batchInstanceModuleName = "";
							for (final String executedModuleId : executedModulesArray) {
								for (final BatchClassModule batchClassModule : batchClassModules) {
									if (batchClassModule != null
											&& executedModuleId.equalsIgnoreCase(String.valueOf(batchClassModule.getModule().getId()))) {
										batchInstanceModuleName = batchClassModule.getModule().getName();
										executedWorkflows.add(batchClassModule.getWorkflowName());
										break;
									}
								}
							}
							final String errorMessage = WebServiceConstants.INVALID_MODULE_NAME_TO_RESTART_MESSAGE
									+ (StringUtils.isNotBlank(batchInstanceModuleName) ? "Batch instance is currently in "
											+ batchInstanceModuleName + " state. It cannot be restarted from " + moduleName : "");
							LOGGER.error("Error response at server:" + errorMessage);
							throw new ValidationException(errorMessage, createUnprocessableEntityRestError(errorMessage,
									WebServiceConstants.INVALID_MODULE_NAME_TO_RESTART_CODE));
						}
					} else {
						throw new UnAuthorisedAccessException();
					}
				}
			} else {
				throw new ValidationException(WebServiceConstants.NO_INSTANCE_NAME, createUnprocessableEntityRestError(
						WebServiceConstants.NO_INSTANCE_NAME, WebServiceConstants.NO_INSTANCE_NAME_CODE));
			}
			if (!isRestarted) {
				throw new InternalServerException(WebServiceConstants.MODULE_CANNOT_BE_RESTARTED_MESSAGE,
						createUnprocessableEntityRestError(WebServiceConstants.MODULE_CANNOT_BE_RESTARTED_MESSAGE,
								WebServiceConstants.MODULE_CANNOT_BE_RESTARTED_CODE));
			}
		} else {
			throw new ValidationException(WebServiceConstants.NO_MODULE_NAME, createUnprocessableEntityRestError(
					WebServiceConstants.NO_MODULE_NAME, WebServiceConstants.NO_MODULE_NAME_CODE));
		}
		return WebServiceConstants.BATCH_RESTARTED_SUCCESS_MESSAGE + getAdditionalInfo(identifier);
	}

	private String getAdditionalInfo(final String info) {
		return " (" + info + "). ";
	}

	/**
	 * WebService Helper which will restart all the Batched in the REVIEW and the VALIDATION phase
	 * 
	 * @param loggedInUserRole {@link Set} Roles assigned to the User
	 * @param isSuperAdmin {@link Boolean} Flag that determines that the user is a Super Admin or not
	 * @return the success/failure message defining whether the Request is processed or not and raise an exception when culdnot
	 *         validate the request
	 * 
	 * @throws InternalServerException When could not process the Request
	 */
	public String restartAllBatchInstance(final Set<String> loggedInUserRole, final boolean isSuperAdmin)
			throws InternalServerException {
		final StringBuffer successMessage = new StringBuffer(WebServiceConstants.BATCH_RESTARTED_SUCCESS_MESSAGE);
		StringBuffer failureMessage;
		boolean isRestarted = false;
		final List<BatchInstanceStatus> batchStatusList = new ArrayList<BatchInstanceStatus>();
		batchStatusList.add(BatchInstanceStatus.READY_FOR_REVIEW);
		batchStatusList.add(BatchInstanceStatus.READY_FOR_VALIDATION);
		List<BatchInstance> batchInstanceList = null;
		if (isSuperAdmin) {
			batchInstanceList = batchInstanceService.getBatchInstanceByStatusList(batchStatusList);
		} else {
			batchInstanceList = batchInstanceService.getBatchInstancesForStatusPriority(batchStatusList, null, loggedInUserRole);
		}
		final boolean isZipSwitchOn = batchSchemaService.isZipSwitchOn();
		if (CollectionUtils.isNotEmpty(batchInstanceList)) {
			failureMessage = new StringBuffer(WebServiceConstants.BATCH_RESTARTED_FAIL_MESSAGE);
			for (final BatchInstance batchInstance : batchInstanceList) {
				final String batchInstanceIdentifier = batchInstance.getIdentifier();
				LOGGER.info("Restarting batch instance : " + batchInstanceIdentifier);
				final String moduleName = workflowService.getActiveModuleName(batchInstanceIdentifier);
				if (moduleName != null) {
					try {
						final String batchClassIdentifier = batchInstanceService.getBatchClassIdentifier(batchInstanceIdentifier);
						isRestarted = processRestartingBatchInternal(batchInstanceIdentifier, moduleName, batchInstance,
								batchClassIdentifier, isZipSwitchOn);

						successMessage.append(EphesoftStringUtil.concatenate(WebServiceConstants.OPENING_SQUARE_BRACES,
								batchInstanceIdentifier, WebServiceConstants.CLOSING_SQUARE_BRACES));
					} catch (final Exception e) {
						failureMessage.append(batchInstanceIdentifier);
						final RestError restError = createUnprocessableEntityRestError(
								WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, WebServiceConstants.INTERNAL_SERVER_ERROR_CODE);
						LOGGER.error("Error while restarting batch instance: " + batchInstanceIdentifier);
						throw new InternalServerException(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);

					}
				} else {
					failureMessage.append(batchInstanceIdentifier);
				}
			}
		} else {
			failureMessage = new StringBuffer(WebServiceConstants.NO_BATCH_INSTANCE_FOUND);
		}
		return isRestarted ? successMessage.toString() : failureMessage.toString();
	}

	/**
	 * Reads from the property file that can all the batch instances restart.
	 * 
	 * @return true if the restart all property is enabled else return false.
	 */
	private boolean canRestartAllBatchInstances() {
		boolean canRestart = false;
		LOGGER.info("Reading restart_all batch instance property.");
		try {
			ApplicationConfigProperties properties = ApplicationConfigProperties.getApplicationConfigProperties();
			if (properties != null) {
				String property = properties.getProperty(WebServiceConstants.RESTART_ALL_BATCH_INSTANCE_PROPERTY);
				if (EphesoftStringUtil.isValidBooleanValue(property)) {
					canRestart = Boolean.parseBoolean(property);
				}
			}
		} catch (IOException ioException) {
			LOGGER.error("Could not read from application-config-properties to restart");
		}
		return canRestart;
	}

	/**
	 * Process restarting batch.
	 * 
	 * @param identifier the identifier
	 * @param moduleWorkflowNameToRestart the module name
	 * @param newBatchInstance the batch instance
	 * @param batchClassIdentifier the batch class identifier
	 * @param isZipSwitchOn the is zip switch on
	 * @param activeModule the active module
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	private boolean processRestartingBatchInternal(final String identifier, final String moduleWorkflowNameToRestart,
			final BatchInstance batchInstance, final String batchClassIdentifier, final boolean isZipSwitchOn) throws Exception {
		boolean isRestarted = true;
		BatchInstance newBatchInstance = batchInstance;
		LOGGER.info("Inside processRestartingBatchInternal method.");
		engineService.deleteProcessInstanceByBatchInstance(batchInstance, false);
		final BatchInstanceThread batchInstanceThread = ThreadPool.getBatchInstanceThreadList(identifier);
		if (batchInstanceThread != null) {
			batchInstanceThread.remove();
			LOGGER.info("Removing the batch instance thread done successsfully.");
		}
		String executedModules = WebServiceUtil.EMPTY_STRING;
		if (moduleWorkflowNameToRestart != null) {
			LOGGER.info("Restarting the batch instance from the module:" + moduleWorkflowNameToRestart);
			final Properties properties = WebServiceUtil.fetchConfig();

			importBatchService.updateBatchFolders(properties, newBatchInstance, moduleWorkflowNameToRestart, isZipSwitchOn);

			LOGGER.info("Batch folders have been updated successfully.");
			executedModules = newBatchInstance.getExecutedModules();
			if (executedModules != null && !executedModules.isEmpty()) {
				LOGGER.info("List of executed modules:" + executedModules);
				if (batchClassIdentifier != null) {
					final BatchClassModule batchClassModuleItem = batchClassModuleService.getBatchClassModuleByWorkflowName(
							batchClassIdentifier, moduleWorkflowNameToRestart);
					final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
					final List<BatchClassModule> batchClassModules = batchClass.getBatchClassModules();
					if (null != batchClassModules) {
						for (final BatchClassModule batchClassModule : batchClassModules) {
							if (batchClassModule != null && batchClassModule.getModule() != null) {
								if (batchClassModule.getOrderNumber() >= batchClassModuleItem.getOrderNumber()) {
									final String replaceText = batchClassModule.getModule().getId() + ";";
									executedModules = executedModules.replace(replaceText, WebServiceUtil.EMPTY_STRING);
								}
							}
						}
					}
				}
			}
			LOGGER.info("Resetted the executed modules list to :" + executedModules);
		} else {
			LOGGER.info("Restarting the batch instance from the begining.");
			importBatchService.removeFolders(newBatchInstance);
			executedModules = WebServiceUtil.EMPTY_STRING;
			LOGGER.info("Resetted the executed modules list to :" + executedModules);
		}

		newBatchInstance = batchInstanceService.getBatchInstanceByIdentifier(identifier);
		newBatchInstance.setExecutedModules(executedModules);
		newBatchInstance = batchInstanceService.merge(newBatchInstance);
		LOGGER.info("BatchInstance object updated in the database successfully.");

		if (!EphesoftStringUtil.isNullOrEmpty(moduleWorkflowNameToRestart)
				&& moduleWorkflowNameToRestart.equalsIgnoreCase(WorkflowConstants.WORKFLOW_CONTINUE_CHECK)) {
			isRestarted = false;
			LOGGER.error(EphesoftStringUtil.concatenate("Error in restarting batch instance : ", identifier));
		}
		workflowService.startWorkflow(newBatchInstance.getBatchInstanceID(), moduleWorkflowNameToRestart);
		return isRestarted;
	}

	/**
	 * Delete batch instance and return the Success or the error Message when it tries to process the request and raise an exception
	 * when unable to process the request
	 * 
	 * @param identifier {@link PathVariable} The path variable which is the identifier of the batch instance to be deleted
	 * @param loggedInUserRole {@link Set} Set of the roles the user is assigned to.
	 * @param isSuperAdmin {@link Boolean} The flag which determines the user is SuperAdmin or not
	 * @return the success/error message when process the request
	 * @throws ValidationException When could not validate the input provided
	 * @throws UnAuthorisedAccessException When the user is not authorized to make the request.
	 * @throws InternalServerException When the request could not be processed by the server or any run time exception being generated
	 * @throws DCMAApplicationException
	 */
	public String deleteBatchInstance(final String identifier, final Set<String> loggedInUserRole, final boolean isSuperAdmin)
			throws ValidationException, UnAuthorisedAccessException, InternalServerException {
		boolean isDeleted = false;
		if (StringUtils.isNotBlank(identifier)) {
			final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(identifier);
			// Status for which a batch can be deleted:
			if (batchInstance == null) {
				LOGGER.error("Error response at server: " + WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_MESSAGE
						+ getAdditionalInfo(identifier));
				throw new ValidationException(WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_MESSAGE
						+ getAdditionalInfo(identifier), createUnprocessableEntityRestError(
						WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_MESSAGE + getAdditionalInfo(identifier),
						WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_CODE));
			} else if (BatchInstanceStatus.DELETED.equals(batchInstance.getStatus())) {
				LOGGER.error("Error response at server: " + WebServiceConstants.BATCH_INSTANCE_ALREADY_DELETED_MESSAGE
						+ getAdditionalInfo(identifier));
				throw new ValidationException(WebServiceConstants.BATCH_INSTANCE_ALREADY_DELETED_MESSAGE
						+ getAdditionalInfo(identifier), createUnprocessableEntityRestError(
						WebServiceConstants.BATCH_INSTANCE_ALREADY_DELETED_MESSAGE + getAdditionalInfo(identifier),
						WebServiceConstants.BATCH_INSTANCE_ALREADY_DELETED_CODE));
			} else if (!BatchInstanceStatus.restartableStatusList().contains(batchInstance.getStatus())) {
				LOGGER.error("Error response at server: " + WebServiceConstants.BATCH_INSTANCE_CANNOT_BE_DELETED_MESSAGE);
				throw new ValidationException(WebServiceConstants.BATCH_INSTANCE_CANNOT_BE_DELETED_MESSAGE
						+ getAdditionalInfo(identifier + " " + batchInstance.getStatus()), createUnprocessableEntityRestError(
						WebServiceConstants.BATCH_INSTANCE_CANNOT_BE_DELETED_MESSAGE
								+ getAdditionalInfo(identifier + " " + batchInstance.getStatus()),
						WebServiceConstants.BATCH_INSTANCE_CANNOT_BE_DELETED_CODE));
			} else if (StringUtils.isNotBlank(batchInstance.getCurrentUser())) {
				// the batch is locked by some user, cannot be restarted/deleted
				LOGGER.error("Error response at server: " + WebServiceConstants.BATCH_INSTANCE_LOCKED_MESSAGE
						+ getAdditionalInfo(identifier));
				throw new InternalServerException(WebServiceConstants.BATCH_INSTANCE_LOCKED_MESSAGE + getAdditionalInfo(identifier),
						createUnprocessableEntityRestError(WebServiceConstants.BATCH_INSTANCE_LOCKED_MESSAGE
								+ getAdditionalInfo(identifier), WebServiceConstants.BATCH_INSTANCE_LOCKED_CODE));
			} else {
				final Set<String> batchInstanceRoles = batchInstanceService.getRolesForBatchInstance(batchInstance);
				if (isSuperAdmin || batchInstanceRoles.removeAll(loggedInUserRole)) {
					LOGGER.info("Deleting the batch instance:" + identifier);

					BatchInstanceThread batchInstanceThread = ThreadPool.getBatchInstanceThreadList(identifier);
					if (batchInstanceThread != null) {
						batchInstanceThread.remove();
						try {
							Thread.sleep(90000);
						} catch (InterruptedException e) {
							LOGGER.info("Unable to sleep for 90000 mili seconds.");
						}
					}
					pluginPropertiesService.clearCache(identifier);
					engineService.deleteProcessInstanceByBatchInstance(batchInstance, true);
					batchInstance.setStatus(BatchInstanceStatus.DELETED);
					batchInstanceService.updateBatchInstance(batchInstance);
					batchInstanceGroupsService.deleteBatchInstanceFromGrps(identifier);
					final File uncFile = new File(batchInstance.getUncSubfolder());
					LOGGER.info("uncFile for the batch instance:" + uncFile);
					if (null != uncFile) {
						FileUtils.deleteDirectoryAndContentsRecursive(uncFile);
						LOGGER.info("Deleted the unc folders of batch instance:" + identifier + " successfully.");
					}
					deleteBatchFolder(batchInstance);
					deleteSerFile(batchInstance);
					isDeleted = true;
				} else {
					throw new UnAuthorisedAccessException();
				}
			}
		}
		return isDeleted ? WebServiceConstants.BATCH_DELETED_SUCCESS_MESSAGE + getAdditionalInfo(identifier)
				: WebServiceConstants.BATCH_DELETED_FAILURE_MESSAGE;
	}

	/**
	 * Adds the user roles to batch instance.
	 * 
	 * @param identifier the batch class identifier
	 * @param userRole the user role to add
	 * @param isSuperAdmin the flag if user is super admin
	 * @return the success or failure message.
	 * @throws ValidationException the validation exception
	 * @throws UnAuthorisedAccessException the un authorised access exception
	 */
	public String addUserRolesToBatchInstance(final String identifier, final String userRole, final Boolean isSuperAdmin)
			throws ValidationException, UnAuthorisedAccessException {
		boolean isRoleAdded = false;
		final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(identifier);
		final Set<String> allRoles = userConnectivityService.getAllGroups();
		if (CollectionUtils.isNotEmpty(allRoles) && allRoles.contains(userRole)) {
			if (batchInstance != null) {
				if (isSuperAdmin) {
					batchInstanceGroupsService.addUserRolesToBatchInstanceIdentifier(identifier, userRole);
					isRoleAdded = true;
				} else {
					throw new UnAuthorisedAccessException();
				}
			} else {
				createAndThrowValidationException(identifier, WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_CODE,
						WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_MESSAGE);
			}
		} else {
			throw new ValidationException(WebServiceConstants.INVALID_ROLE_ERROR_MESSAGE, new RestError(
					HttpStatus.UNPROCESSABLE_ENTITY, WebServiceConstants.INVALID_ROLE_ERROR_CODE,
					WebServiceConstants.INVALID_ROLE_ERROR_MESSAGE, WebServiceConstants.INVALID_ROLE_ERROR_MESSAGE
							+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL));
		}
		return isRoleAdded ? WebServiceConstants.USER_ROLE_ADDED_SUCCESS_MESSAGE
				+ getAdditionalInfo(userRole + " added to " + identifier) : WebServiceConstants.USER_ROLE_ADDED_FAILURE_MESSAGE
				+ identifier;
	}

	/**
	 * Gets the batch class list defined for the User. These Batch class roles would be different for the Super Admin which would not
	 * account for the deleted ones
	 * 
	 * @param loggedInUserRole {@link Set} nSet of roles assigned to the Logged in user
	 * @param isSuperAdmin {@link Boolean} Flag that determines that the user is a super Admin or not
	 * @return {@link BatchClasses} That is List representation of the {@link BatchClass}
	 * @throws UnAuthorisedAccessException When the user is not authorised to make the req
	 */
	public BatchClasses getBatchClassList(final Set<String> loggedInUserRole, final boolean isSuperAdmin)
			throws UnAuthorisedAccessException {
		final BatchClasses batchClasses = new BatchClasses();
		if (CollectionUtils.isNotEmpty(loggedInUserRole)) {
			List<BatchClass> batchClass = null;
			if (isSuperAdmin) {
				batchClass = batchClassService.getAllBatchClassesExcludeDeleted();
			} else {
				batchClass = batchClassService.getAllBatchClassesByUserRoles(loggedInUserRole);
			}
			final List<com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass> batchClassList = batchClasses.getBatchClass();
			LOGGER.info("Total batch class found from the database is : " + batchClass.size());
			com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass batchClassLocal;
			for (final BatchClass eachBatchClass : batchClass) {
				batchClassLocal = createBatchClass(eachBatchClass);
				batchClassList.add(batchClassLocal);
			}
		} else {
			throw new UnAuthorisedAccessException();
		}
		return batchClasses;
	}

	/**
	 * Method to perform conversion of batch class from database to xsd for web service.
	 * 
	 * @param eachBatchClass the each batch class
	 * @return the com.ephesoft.dcma.batch.schema. batch classes. batch class
	 */
	private com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass createBatchClass(final BatchClass eachBatchClass) {
		final com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass batchClassLocal = new com.ephesoft.dcma.batch.schema.BatchClasses.BatchClass();
		batchClassLocal.setCurrentUser(eachBatchClass.getCurrentUser());
		batchClassLocal.setDescription(eachBatchClass.getDescription());
		batchClassLocal.setIdentifier(eachBatchClass.getIdentifier());
		batchClassLocal.setName(eachBatchClass.getName());
		batchClassLocal.setPriority(eachBatchClass.getPriority());
		batchClassLocal.setUncFolder(eachBatchClass.getUncFolder());
		batchClassLocal.setVersion(eachBatchClass.getVersion());
		return batchClassLocal;
	}

	/**
	 * The Helper method for the Web Service API which defines the working of fetching the roles on the Batch Class. It returns the
	 * values of roles
	 * 
	 * @param identifier {@link String} The Batch Class Identifier received from the request
	 * @param loggedInUserRole {@link Set} The Set of Roles defined for the User that has logged in
	 * @param isSuperAdmin {@link Boolean} defines whether the user is Super-Admin or not
	 * @param isBatchClassViewableToUser {@link Boolean} Defines whether the Batch is Visible to User or not
	 * @return {@link ListValues} The list Values that defines the Roles as on the Batch Class Idenitifer
	 * @throws ValidationException {@link ValidationException} When there is some Validation issue this Exception would be raised
	 * @throws UnAuthorisedAccessException {@link UnAuthorisedAccessException} When the user is not allowed to make the request
	 */
	public ListValues getRoles(final String identifier, final Set<String> loggedInUserRole, final boolean isSuperAdmin,
			final boolean isBatchClassViewableToUser) throws ValidationException, UnAuthorisedAccessException {
		Set<String> userGroups = null;
		if (isBatchClassViewableToUser) {
			final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(identifier);
			if (batchClass != null) {
				LOGGER.info("Fetching user roles for batch class identifier" + identifier);
				userGroups = batchClassGroupsService.getRolesForBatchClass(identifier);
			} else {
				throw new ValidationException(WebServiceConstants.INVALID_BATCH_INSTANCE_STATUS_MESSAGE,
						createUnprocessableEntityRestError(WebServiceConstants.INVALID_BATCH_INSTANCE_STATUS_MESSAGE,
								WebServiceConstants.INVALID_BATCH_INSTANCE_STATUS_CODE));
			}
		} else {
			throw new UnAuthorisedAccessException();
		}
		final ListValues list = new ListValues();
		list.getValue().addAll(userGroups);
		return list;
	}

	/**
	 * This Web Service implements the functionality of uploading the Batch and creating the new BatchInstances with the name provided
	 * by the user making sure that the there is no Other Batch Instance with the same name under that Batch class and as well as the
	 * user has the access to that Batch Class
	 * 
	 * @param req {@link HttpServletRequest} The request Object encapsulated for the clients Request
	 * @param batchClassIdentifier {@link String} Batch Class Identifier which uniquely Identifies the Object
	 * @param batchInstanceName {@link String} Batch Instance Name that is the unique name which identifies the batch Instance
	 * @param roles {@link Set} Set of the roles assigned to the user
	 * @return {@link String} the Result to be displaying When the Request is executed successfully withoput generating an Exception
	 * @throws ValidationException When the Input Parameters cannot be validated
	 * @throws UnAuthorisedAccessException When User tries to make an access to a batch which is not under his role
	 * @throws NoUserRoleException : When the user is not assigned to any role
	 * @throws UnSupportedFileTypeException : When the uploaded file is not from any of the supprted file type by plugin pdf/tif/tiff
	 * @throws BatchNameAlreadyExistException : The Batch With same Name aslready exist
	 * @throws InternalServerException : When Some IOException Occurs at Server
	 * @throws Exception When unable to process the Multi-Part Request
	 */
	public String uploadBatch(final HttpServletRequest req, final String batchClassIdentifier, final String batchInstanceName,
			final Set<String> roles) throws ValidationException, UnAuthorisedAccessException, NoUserRoleException,
			UnSupportedFileTypeException, BatchNameAlreadyExistException, InternalServerException, Exception {
		String workingDir = WebServiceUtil.EMPTY_STRING;
		boolean isSuccess = false;
		try {
			InputStream inStream = null;
			OutputStream outStream = null;
			if (StringUtils.isBlank(batchClassIdentifier)) {
				throw new ValidationException(WebServiceConstants.INVALID_BATCH_CLASS_ID_MESSAGE + batchClassIdentifier,
						createUnprocessableEntityRestError(WebServiceConstants.INVALID_BATCH_CLASS_ID_MESSAGE,
								WebServiceConstants.INVALID_BATCH_CLASS_ID_CODE));
			} else if (StringUtils.isBlank(batchInstanceName)) {
				throw new ValidationException(WebServiceConstants.INVALID_BATCH_INSTANCE_NAME_MESSAGE + batchClassIdentifier,
						createUnprocessableEntityRestError(WebServiceConstants.INVALID_BATCH_INSTANCE_NAME_MESSAGE,
								WebServiceConstants.INVALID_BATCH_INSTANCE_NAME_CODE));
			} else if (req instanceof DefaultMultipartHttpServletRequest) {
				final String webServiceFolderPath = batchSchemaService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				String uncFolderPath = WebServiceUtil.EMPTY_STRING;
				final List<File> fileList = new ArrayList<File>();
				if (CollectionUtils.isNotEmpty(roles)) {
					final BatchClass batchClass = batchClassService.getBatchClassByUserRoles(roles, batchClassIdentifier);
					if (batchClass == null) {
						LOGGER.error("The user does not have the authentication to run the batch in the requested batch class with id: "
								+ batchClassIdentifier);
						throw new UnAuthorisedAccessException();
					} else {
						uncFolderPath = batchClass.getUncFolder();
					}
				} else {
					LOGGER.error(WebServiceConstants.NO_USER_ROLE_EXCEPTION_MESSAGE);
					throw new NoUserRoleException();
				}
				if (fileMap.size() == 0) {
					throw new ValidationException(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE,
							createUnprocessableEntityRestError(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE,
									WebServiceConstants.IMPROPER_INPUT_TO_SERVER_CODE));
				}
				for (final String srcFileName : fileMap.keySet()) {
					if (FileType.isValidFileName(srcFileName)) {
						final File file = new File(workingDir + File.separator + srcFileName);
						fileList.add(file);
						final MultipartFile multiPartFile = multiPartRequest.getFile(srcFileName);
						inStream = multiPartFile.getInputStream();
						outStream = new FileOutputStream(file);
						final byte[] buf = new byte[WebServiceUtil.bufferSize];
						int len = inStream.read(buf);
						while (len > 0) {
							outStream.write(buf, 0, len);
							len = inStream.read(buf);
						}
						IOUtils.closeQuietly(inStream);
						IOUtils.closeQuietly(outStream);
					} else {
						LOGGER.error(WebServiceConstants.UNSUPPORTED_FILE_TYPE_EXCEPTION_MESSAGE);
						throw new UnSupportedFileTypeException();
					}
				}
				final String filePath = workingDir + File.separator + batchInstanceName;
				final File file = new File(filePath);
				if (file.mkdir()) {
					for (final File srcFileName : fileList) {
						final File destFile = new File(filePath + File.separator + srcFileName.getName());
						FileUtils.copyFile(srcFileName, destFile);
					}
					final File destFile = new File(uncFolderPath + File.separator + batchInstanceName);
					if (!destFile.exists()) {
						FileUtils.copyDirectoryWithContents(file, destFile);
					} else {
						LOGGER.error(WebServiceConstants.BATCH_NAME_ALREADY_EXIST_EXCEPTION_MESSAGE);
						throw new BatchNameAlreadyExistException();
					}
					isSuccess = true;
				} else {
					LOGGER.error(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE);
					throw new InternalServerException(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE,
							createUnprocessableEntityRestError(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE,
									WebServiceConstants.IMPROPER_INPUT_TO_SERVER_CODE));
				}
			} else {
				throw new ValidationException(WebServiceConstants.INVALID_INPUT_FOR_UPLOAD_BATCH, createUnprocessableEntityRestError(
						WebServiceConstants.INVALID_INPUT_FOR_UPLOAD_BATCH, WebServiceConstants.INVALID_PARAMETERS_CODE));
			}
		} finally {
			if (!workingDir.isEmpty()) {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
			}
		}
		return isSuccess ? WebServiceConstants.BATCH_UPLOADED_SUCCESS_MESSAGE : WebServiceConstants.BATCH_UPLOADED_FAILURE_MESSAGE;
	}

	/**
	 * Function of the Helper Class which helps the web services to handle the Request of importing the Batch Class. It process the
	 * request but raises an Exception when could not process the Request
	 * 
	 * @param httpRequest {@link HttpServletRequest} The request made by the client encapsulated as an object
	 * @return success or error Message When an exception is not raised after processing the request
	 * @throws ValidationException : When could not validate the input or input condition required for the request
	 * @throws InternalServerException : When input is valid but server could not process the request
	 * @throws Exception : In case of any un-expected Failure
	 */
	public String importBatchClass(final HttpServletRequest httpRequest) throws ValidationException, InternalServerException,
			Exception {
		String workingDir = WebServiceUtil.EMPTY_STRING;
		boolean isImported = false;
		LOGGER.info("Reached Here");
		if (httpRequest instanceof DefaultMultipartHttpServletRequest) {
			InputStream instream = null;
			OutputStream outStream = null;
			final String webServiceFolderPath = batchSchemaService.getWebServicesFolderPath();
			LOGGER.info("web Service Folder Path" + webServiceFolderPath);
			final DefaultMultipartHttpServletRequest mPartReq = (DefaultMultipartHttpServletRequest) httpRequest;
			final MultiValueMap<String, MultipartFile> fileMap = mPartReq.getMultiFileMap();
			if (fileMap.size() == WebServiceConstants.IMPORT_BATCH_CLASS_FILES) {
				try {
					workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
					LOGGER.info("Created the web service working directory successfully  :" + workingDir);
					ImportBatchClassOptions option = null;
					String zipFilePath = WebServiceUtil.EMPTY_STRING;
					for (final String fileName : fileMap.keySet()) {
						try {
							final MultipartFile f = mPartReq.getFile(fileName);
							instream = f.getInputStream();
							if (fileName.toLowerCase().indexOf(FileType.XML.getExtension().toLowerCase()) > -1) {
								final Source source = XMLUtil.createSourceFromStream(instream);
								option = (ImportBatchClassOptions) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller()
										.unmarshal(source);
								continue;
							} else if (fileName.toLowerCase().indexOf(FileType.ZIP.getExtension().toLowerCase()) > -1) {
								zipFilePath = workingDir + File.separator + fileName;
								LOGGER.info("Zip file is using for importing batch class is " + zipFilePath);
								final File file = new File(zipFilePath);
								outStream = new FileOutputStream(file);
								final byte[] buf = new byte[WebServiceUtil.bufferSize];
								int len;
								while ((len = instream.read(buf)) > 0) {
									outStream.write(buf, 0, len);
								}
								continue;
							} else {
								throw new ValidationException(WebServiceConstants.INVALID_FILES_SEND,
										createUnprocessableEntityRestError(WebServiceConstants.INVALID_FILES_SEND,
												WebServiceConstants.INVALID_PARAMETERS_CODE));
							}
						} finally {
							IOUtils.closeQuietly(instream);
							IOUtils.closeQuietly(outStream);
						}
					}
					importBatchClassInternal(workingDir, option, zipFilePath);
					isImported = true;
				} catch (final InternalServerException ise) {
					throw ise;
				} catch (final ValidationException validationException) {
					throw validationException;
				} catch (final Exception exception) {
					exception.printStackTrace();
					throw new InternalServerException(
							WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE.concat(exception.getMessage()),
							createUnprocessableEntityRestError(
									WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE.concat(exception.getMessage()),
									WebServiceConstants.INTERNAL_SERVER_ERROR_CODE));
				} finally {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			} else {
				final String errorMessage = "Improper input to server. Expected two files: zip and xml file. Returning without processing the results.";
				LOGGER.error("Error response at server:" + errorMessage);
				final RestError restError = createUnprocessableEntityRestError(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE,
						WebServiceConstants.IMPROPER_INPUT_TO_SERVER_CODE);
				LOGGER.error(errorMessage + WebServiceConstants.HTTP_STATUS + HttpStatus.INTERNAL_SERVER_ERROR);
				throw new ValidationException(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE + getAdditionalInfo(errorMessage),
						restError);
			}
		} else {
			final String errorMessage = "Improper input to server. Expected two files: zip and xml file. Returning without processing the results.";
			LOGGER.error("Error response at server:" + errorMessage);
			final RestError restError = createUnprocessableEntityRestError(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE,
					WebServiceConstants.IMPROPER_INPUT_TO_SERVER_CODE);
			LOGGER.error(errorMessage + WebServiceConstants.HTTP_STATUS + HttpStatus.INTERNAL_SERVER_ERROR);
			throw new ValidationException(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE + getAdditionalInfo(errorMessage),
					restError);
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		return isImported ? WebServiceConstants.BATCH_IMPORTED_SUCCESS_MESSAGE : WebServiceConstants.BATCH_IMPORTED_FAILURE_MESSAGE;
	}

	/**
	 * Import batch class internal. Process the request fetched from the Import Function and import the batch class using various DAO
	 * Services
	 * 
	 * @param workingDir {@link String} The working directory of the user
	 * @param option {@link ImportBatchClassOptions} The import batch class options
	 * @param zipFilePath {@link String} Path to the zipped file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ValidationException : When could not validate the input or input condition required for the request
	 * @throws InternalServerException : When input is valid but server could not process the request
	 */
	private void importBatchClassInternal(final String workingDir, final ImportBatchClassOptions option, final String zipFilePath)
			throws IOException, InternalServerException, ValidationException {
		LOGGER.info("Reached Batch Class Internal");
		Set<String> assignedGroups = null;
		if (option != null && !zipFilePath.isEmpty()) {
			final Map<Boolean, String> results = importBatchService.validateInputXML(option);
			final String errorMsg = results.get(Boolean.FALSE);
			if (StringUtils.isNotBlank(errorMsg)) {
				throw new InternalServerException(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + getAdditionalInfo(errorMsg),
						createUnprocessableEntityRestError(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
								+ getAdditionalInfo(errorMsg), WebServiceConstants.INTERNAL_SERVER_ERROR_CODE));
			} else {
				LOGGER.info("zip file path:" + zipFilePath);
				final File tempZipFile = new File(zipFilePath);
				final String tempOutputUnZipDir = tempZipFile.getParent() + File.separator
						+ tempZipFile.getName().substring(0, tempZipFile.getName().indexOf(WebServiceUtil.DOT));
				LOGGER.info("temporary Output UnZip Directory:" + tempOutputUnZipDir);
				try {
					FileUtils.unzip(tempZipFile, tempOutputUnZipDir);
				} catch (final Exception e) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
					tempZipFile.delete();
					final String errorMessage = "Unable to unzip file. Returning without processing the results.";
					LOGGER.error(errorMessage);
					throw new InternalServerException(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
							+ getAdditionalInfo(errorMessage), createUnprocessableEntityRestError(
							WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + getAdditionalInfo(errorMessage),
							WebServiceConstants.INTERNAL_SERVER_ERROR_CODE));
				}

				option.setZipFilePath(tempOutputUnZipDir);
				LOGGER.info("Importing batch class");

				// JIRA-Bug-11092
				if (!option.isRolesImported()) {
					assignedGroups = userConnectivityService.getAllSuperAdminGroups();
				}
				final boolean isDeployed = deploymentService.isDeployed(option.getName());
				// final Map<Boolean, String> resultsImport =
				// importBatchService.importBatchClass(option, isDeployed, true,
				// assignedGroups);
				ImportBatchClassResultCarrier importBatchClassResultCarrier = importBatchService.importBatchClass(option, isDeployed,
						true, assignedGroups);
				if (null != importBatchClassResultCarrier && null != importBatchClassResultCarrier.getImportResults()) {
					final Map<Boolean, String> resultsImport = importBatchClassResultCarrier.getImportResults();
					final String errorMessgImport = resultsImport.get(Boolean.FALSE);
					if (StringUtils.isNotBlank(errorMessgImport)) {
						throw new InternalServerException(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
								+ getAdditionalInfo(errorMessgImport), createUnprocessableEntityRestError(
								WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + getAdditionalInfo(errorMessgImport),
								WebServiceConstants.INTERNAL_SERVER_ERROR_CODE));
					} else {
						final String batchClassId = resultsImport.get(Boolean.TRUE);
						final BatchClass batchClass = batchClassService.get(batchClassId);
						batchClassService.evict(batchClassService.getLoadedBatchClassByIdentifier(batchClassId));
						deployBatchClass(batchClassId);
						// if success in importing batch class and generating
						// key,
						// clear the HOCR files from diff folders.
						// Adding check for use case as it has to be checked in
						// case
						// of Windows only.
						if (OSUtil.isWindows() && !option.isUseKey()) {
							try {
								generateBatchClassLevelKey(option.getBatchClassKey(), option.getEncryptionAlgorithm(), batchClassId);
							} catch (Exception e) {
								RestError restError = new RestError(HttpStatus.INTERNAL_SERVER_ERROR,
										WebServiceConstants.INTERNAL_SERVER_ERROR_CODE, e.getMessage(), e.getMessage()
												+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
								LOGGER.error(WebServiceConstants.HTTP_STATUS + HttpStatus.UNPROCESSABLE_ENTITY);
								InternalServerException internalServerExcpetion = new InternalServerException(
										WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
								throw internalServerExcpetion;
							}
							clearFoldersAfterKeyGeneration(option);
						}
						LOGGER.info("Batch class with identifier:" + batchClass.getIdentifier() + " imported successfully.");
					}
				}
			}
		} else {
			LOGGER.error(WebServiceConstants.HTTP_STATUS + HttpStatus.UNPROCESSABLE_ENTITY);
			throw new ValidationException(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE, createUnprocessableEntityRestError(
					WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE, WebServiceConstants.IMPROPER_INPUT_TO_SERVER_CODE));
		}
	}

	private void deployBatchClass(final String batchClassId) throws InternalServerException {
		try {
			deployNewBatchClass(batchClassId, false);
		} catch (DCMAException dcmaException) {

			RestError restError = new RestError(HttpStatus.INTERNAL_SERVER_ERROR, WebServiceConstants.INTERNAL_SERVER_ERROR_CODE,
					dcmaException.getMessage(), dcmaException.getMessage() + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
					WebServiceConstants.DEFAULT_URL);
			LOGGER.error(WebServiceConstants.HTTP_STATUS + HttpStatus.UNPROCESSABLE_ENTITY);
			InternalServerException internalServerExcpetion = new InternalServerException(
					WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
			throw internalServerExcpetion;
		}
	}

	public void generateBatchClassLevelKey(String batchClassKey, String selectedEncryptionAlgo, String batchClassIdentifier)
			throws Exception {
		BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
		if (null != batchClass) {
			try {
				// remove previous key present if any.
				encryptionService.removeBatchClassKey(batchClassIdentifier);
				batchClass.setEncryptionAlgorithm(encryptionService.getPrivateKeyAlgorithm(selectedEncryptionAlgo));
				batchClassService.saveOrUpdate(batchClass);
				// generate new key.
				encryptionService.generateBatchClassKey(batchClassIdentifier, batchClassKey.getBytes());
			} catch (KeyGenerationException keyGenException) {
				LOGGER.error(keyGenException.getMessage(), keyGenException);
				throw new Exception("Problem generating the key : " + keyGenException.getMessage());
			} catch (Exception exception) {
				LOGGER.error(exception.getMessage(), exception);
				throw new Exception("Problem generating the key : " + exception.getMessage());
			}
		}
	}

	private String getBatchClassLearningFolderPath(String batchClassIdentifier) {
		String learnSampleFolderPath = WebServiceConstants.EMPTY_STRING;
		learnSampleFolderPath = batchSchemaService.getSearchClassSamplePath(batchClassIdentifier, false);
		return learnSampleFolderPath;
	}

	private String getTestClassificationPath(String batchClassIdentifier) {
		StringBuilder folderPathBuilder = new StringBuilder(batchSchemaService.getBaseFolderLocation());
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(batchClassIdentifier);
		File batchClassFolder = new File(folderPathBuilder.toString());
		// The path for test classification folder is found.......
		folderPathBuilder.append(File.separator);
		folderPathBuilder.append(WebServiceConstants.TEST_CLASSIFICATION_FOLDER_NAME);
		clearFolderForHOCRFiles(folderPathBuilder.toString());
		return folderPathBuilder.toString();
	}

	private void clearFolderForHOCRFiles(String inputFilesPath) {
		if (null != inputFilesPath && !WebServiceConstants.EMPTY_STRING.equals(inputFilesPath)) {
			// remove redundant thumbnail files.
			File imageFolderPath = new File(inputFilesPath);
			if (null != imageFolderPath) {
				if (!imageFolderPath.exists()) {
					imageFolderPath.mkdirs();
				}
				File[] listOfimages = imageFolderPath.listFiles(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
						FileType.TIFF.getExtensionWithDot(), FileType.PNG.getExtensionWithDot(), FileType.PDF.getExtensionWithDot()));
				if (null != listOfimages) {
					for (File tiffFile : listOfimages) {
						if (null != tiffFile && tiffFile.getName().contains("_th")) {
							tiffFile.delete();
						}
					}
				}
				File[] listOfAllFiles = imageFolderPath.listFiles();
				if (null != listOfAllFiles) {
					for (File file : listOfAllFiles) {
						if (null != file) {
							if (file.isDirectory()) {
								clearFolderForHOCRFiles(file.getAbsolutePath());
							} else {
								boolean notPresent = true;
								for (File tiffFiles : listOfimages) {
									if (null != tiffFiles && null != file && tiffFiles.getName().equals(file.getName())) {
										notPresent = false;
										break;
									}
								}
								if (notPresent) {
									file.delete();
								}
							}
						}
					}
				}
			}
		} else {
			LOGGER.info("The provided folder path is null or empty, no files for deleting.");
		}
	}

	private void clearFoldersAfterKeyGeneration(ImportBatchClassOptions option) {
		BatchClass batchClass = batchClassService.getBatchClassbyName(option.getName());
		if (null != batchClass) {
			String batchClassIdentifier = batchClass.getIdentifier();
			clearFolderForHOCRFiles(getBatchClassLearningFolderPath(batchClassIdentifier));
			// get and clear the test classification folder.
			getTestClassificationPath(batchClassIdentifier);
			// for ocring all the files present in test classification folder
			BatchClassID batchClassID = new BatchClassID(batchClassIdentifier);
			String testTableFolderPath = batchSchemaService.getTestTableFolderPath(batchClassID, true);
			clearFolderForHOCRFiles(testTableFolderPath);
			// recreating the indexes for fuzzy search
			String fuzzyDBFolderPath = batchSchemaService.getFuzzyDBIndexFolder(batchClassIdentifier, true);
			clearFolderForHOCRFiles(fuzzyDBFolderPath);
			// test extraction folder clearance and re-learning.
			String testKVExtractionFolderPath = batchSchemaService.getTestKVExtractionFolderPath(batchClassID, true);
			clearFolderForHOCRFiles(testKVExtractionFolderPath);
			// test advanced extraction folder clearance and re-learning
			String testAdvKVExtractionFolderPath = batchSchemaService
					.getTestAdvancedKvExtractionFolderPath(batchClassIdentifier, true);
			clearFolderForHOCRFiles(testAdvKVExtractionFolderPath);
		}

	}

	private void deployNewBatchClass(String identifier, boolean isGridWorkflow) throws DCMAException {
		LOGGER.info("Deploying the newly copied batch class");

		BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(identifier);
		renameBatchClassModules(batchClass);
		batchClass = batchClassService.merge(batchClass);
		deploymentService.createAndDeployProcessDefinition(batchClass, isGridWorkflow);
	}

	/**
	 * Rename batch class modules.
	 * 
	 * @param batchClass the batch class
	 */
	private void renameBatchClassModules(final BatchClass batchClass) {
		final String existingBatchClassIdentifier = batchClass.getIdentifier();
		for (final BatchClassModule batchClassModule : batchClass.getBatchClassModules()) {
			final String existingModuleName = batchClassModule.getModule().getName();
			final StringBuffer newWorkflowNameStringBuffer = new StringBuffer();
			newWorkflowNameStringBuffer.append(existingModuleName.replaceAll(" ", "_"));
			newWorkflowNameStringBuffer.append("_");
			newWorkflowNameStringBuffer.append(existingBatchClassIdentifier);
			batchClassModule.setWorkflowName(newWorkflowNameStringBuffer.toString());
		}
	}

	/**
	 * Validates the total number of files for learning from the <code>docTypes</code>.
	 * 
	 * @param docTypes {@link List} of {@link DocType} which is to be validated. If null returns false.
	 * @param size int total expected number of files.
	 * @return true if the files count for learning in <code> doctypes </code> matches the size else false.
	 */
	private boolean isValidFileCountForLearning(final List<DocType> docTypes, final int size) {
		boolean isValid = false;
		int totalFiles = 0;
		LOGGER.info(EphesoftStringUtil.concatenate("Validating the total documents with count =", size));
		if (docTypes != null) {
			for (DocType documentType : docTypes) {
				if (documentType != null) {
					totalFiles += getFirstPageFileCount(documentType.getPageTypeFirst());
					totalFiles += getMiddlePageFileCount(documentType.getPageTypeMiddle());
					totalFiles += getLastPageFileCount(documentType.getPageTypeLast());
					LOGGER.info(EphesoftStringUtil.concatenate("total file count after ", documentType.getDocTypeName(), " is ",
							totalFiles));
				}
			}
		}
		isValid = totalFiles == size;
		return isValid;
	}

	/**
	 * Returns the count of number of files in the <code>middlePage<code>
	 * 
	 * @param middlePage {@link PageTypeMiddle} page in which files to be counted. If NULL, returns 0.
	 * @return number of files to upload in the page.
	 */
	private int getMiddlePageFileCount(final PageTypeMiddle middlePage) {
		int totalFiles = 0;
		LOGGER.info(EphesoftStringUtil.concatenate("Counting files in middle page ", middlePage));
		if (middlePage != null) {
			com.ephesoft.dcma.batch.schema.UploadLearningFiles.DocType.PageTypeMiddle.FilesToBeUploaded uploadFiles = middlePage
					.getFilesToBeUploaded();
			if (uploadFiles != null) {
				List<String> filesToUpload = uploadFiles.getFileName();
				if (filesToUpload != null) {
					totalFiles = totalFiles + filesToUpload.size();
				}
			}
		}
		return totalFiles;
	}

	/**
	 * Returns the count of number of files in the <code>lastPage<code>
	 * 
	 * @param middlePage {@link PageTypeLast} page in which files to be counted. If NULL, returns 0.
	 * @return number of files to upload in the page.
	 */
	private int getLastPageFileCount(final PageTypeLast lastPage) {
		int totalFiles = 0;
		LOGGER.info(EphesoftStringUtil.concatenate("Counting files in last page ", lastPage));
		if (lastPage != null) {
			com.ephesoft.dcma.batch.schema.UploadLearningFiles.DocType.PageTypeLast.FilesToBeUploaded uploadFiles = lastPage
					.getFilesToBeUploaded();
			if (uploadFiles != null) {
				List<String> filesToUpload = uploadFiles.getFileName();
				if (filesToUpload != null) {
					totalFiles = totalFiles + filesToUpload.size();
				}
			}
		}
		return totalFiles;
	}

	/**
	 * Returns the count of number of files in the <code>firstPage<code>
	 * 
	 * @param middlePage {@link PageTypeFirst} page in which files to be counted. If NULL, returns 0.
	 * @return number of files to upload in the page.
	 */
	private int getFirstPageFileCount(final PageTypeFirst firstPage) {
		int totalFiles = 0;
		LOGGER.info(EphesoftStringUtil.concatenate("Counting files in first page ", firstPage));
		if (firstPage != null) {
			FilesToBeUploaded uploadFiles = firstPage.getFilesToBeUploaded();
			if (uploadFiles != null) {
				List<String> filesToUpload = uploadFiles.getFileName();
				if (filesToUpload != null) {
					totalFiles = totalFiles + filesToUpload.size();
				}
			}
		}
		return totalFiles;
	}

	/**
	 * This Method is Added to process the input provided for WebService to upload files for Learning
	 * 
	 * @param req the {@link HttpServletRequest} the request header for this web service hit
	 * @throws InternalServerException the internal server exception
	 * @throws ValidationException the validation exception
	 */
	public void getUploadFilesforLearning(final HttpServletRequest req) throws InternalServerException, ValidationException {
		LOGGER.info("Inside getUploadFilesforLearning method");
		String respStr = WebServiceConstants.EMPTY_STRING;
		String workingDir = WebServiceConstants.EMPTY_STRING;
		String docTypeName = WebServiceConstants.EMPTY_STRING;
		String learningType = WebServiceConstants.EMPTY_STRING;
		List<String> fileNamesFirst = null;
		List<String> fileNamesMiddle = null;
		List<String> fileNamesLast = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = batchSchemaService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				final int fileCountValue = checkFileCountForExtentionType("xml", fileMap);
				if (fileCountValue >= 2) {
					final HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
					respStr = "There are more than 1 xml file uploaded with the request. Only 1 xml is expected";
					final RestError restError = new RestError(status, WebServiceConstants.INTERNAL_SERVER_ERROR_CODE, respStr, respStr
							+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
					LOGGER.error("Error response at server:" + respStr);

					final InternalServerException internalServerExcpetion = new InternalServerException(
							WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
					LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
					throw internalServerExcpetion;
				}

				String xmlFileName = WebServiceConstants.EMPTY_STRING;
				xmlFileName = getXMLFile(workingDir, multiPartRequest, fileMap);
				LOGGER.info("XML file name is" + xmlFileName);
				UploadLearningFiles uploadLearningFileXML = null;
				final File xmlFile = new File(workingDir + File.separator + xmlFileName);

				final FileInputStream inputStream = new FileInputStream(xmlFile);
				final Source source = XMLUtil.createSourceFromStream(inputStream);
				uploadLearningFileXML = (UploadLearningFiles) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
				final String inputXMLValidationRes = validateInputXMLForLearning(uploadLearningFileXML);
				if (inputXMLValidationRes.isEmpty()) {
					String searchPathName = WebServiceConstants.EMPTY_STRING;
					final List<DocType> docTypes = uploadLearningFileXML.getDocType();
					if (isValidFileCountForLearning(docTypes, fileMap.size() - 1)) {
						for (final DocType docType : docTypes) {
							docTypeName = docType.getDocTypeName();
							learningType = docType.getLearningType();
							if (docType.getPageTypeFirst() != null
									&& docType.getPageTypeFirst().getFilesToBeUploaded().getFileName() != null
									&& !docType.getPageTypeFirst().getFilesToBeUploaded().getFileName().isEmpty()) {
								fileNamesFirst = docType.getPageTypeFirst().getFilesToBeUploaded().getFileName();
								if (LUCENE_SEARCH_CLASSIFICATION_TYPE.equalsIgnoreCase(learningType)) {
									searchPathName = EphesoftStringUtil
											.concatenate(
													batchSchemaService.getSearchClassSamplePath(
															uploadLearningFileXML.getBatchClassId(), true), File.separator,
													docType.getDocTypeName(), File.separator, docTypeName,
													WebServiceConstants.FIRST_PAGE);

									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesFirst);
									LOGGER.info("Sucessfully Uploaded Images for lucene-search-classification-sample");
								} else if ("Image".equalsIgnoreCase(learningType)) {

									searchPathName = EphesoftStringUtil.concatenate(batchSchemaService.getImageMagickBaseFolderPath(
											uploadLearningFileXML.getBatchClassId(), true), File.separator, docType.getDocTypeName(),
											File.separator, docTypeName, WebServiceConstants.FIRST_PAGE);
									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesFirst);
								} else {

									searchPathName = EphesoftStringUtil
											.concatenate(
													batchSchemaService.getSearchClassSamplePath(
															uploadLearningFileXML.getBatchClassId(), true), File.separator,
													docType.getDocTypeName(), File.separator, docTypeName,
													WebServiceConstants.FIRST_PAGE);

									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesFirst);
									LOGGER.info("Sucessfully Uploaded Images for lucene-search-classification-sample");

									searchPathName = EphesoftStringUtil.concatenate(batchSchemaService.getImageMagickBaseFolderPath(
											uploadLearningFileXML.getBatchClassId(), true), File.separator, docType.getDocTypeName(),
											File.separator, docTypeName, WebServiceConstants.FIRST_PAGE);

									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesFirst);
									LOGGER.info("Sucessfully Uploaded Images for image-classification-sample");
								}
							}

							if (docType.getPageTypeMiddle() != null
									&& docType.getPageTypeMiddle().getFilesToBeUploaded().getFileName() != null
									&& !docType.getPageTypeMiddle().getFilesToBeUploaded().getFileName().isEmpty()) {
								fileNamesMiddle = docType.getPageTypeMiddle().getFilesToBeUploaded().getFileName();
								if (LUCENE_SEARCH_CLASSIFICATION_TYPE.equalsIgnoreCase(learningType)) {

									searchPathName = EphesoftStringUtil
											.concatenate(
													batchSchemaService.getSearchClassSamplePath(
															uploadLearningFileXML.getBatchClassId(), true), File.separator,
													docType.getDocTypeName(), File.separator, docTypeName,
													WebServiceConstants.MIDDLE_PAGE);

									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesMiddle);
									LOGGER.info("Sucessfully Uploaded Images for lucene-search-classification-sample");
								} else if ("Image".equalsIgnoreCase(learningType)) {
									searchPathName = EphesoftStringUtil.concatenate(batchSchemaService.getImageMagickBaseFolderPath(
											uploadLearningFileXML.getBatchClassId(), true), File.separator, docType.getDocTypeName(),
											File.separator, docTypeName, WebServiceConstants.MIDDLE_PAGE);

									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesMiddle);
									LOGGER.info("Sucessfully Uploaded Images for image-classification-sample");
								} else {

									searchPathName = EphesoftStringUtil
											.concatenate(
													batchSchemaService.getSearchClassSamplePath(
															uploadLearningFileXML.getBatchClassId(), true), File.separator,
													docType.getDocTypeName(), File.separator, docTypeName,
													WebServiceConstants.MIDDLE_PAGE);

									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesMiddle);
									LOGGER.info("Sucessfully Uploaded Images for lucene-search-classification-sample");

									searchPathName = EphesoftStringUtil.concatenate(batchSchemaService.getImageMagickBaseFolderPath(
											uploadLearningFileXML.getBatchClassId(), true), File.separator, docType.getDocTypeName(),
											File.separator, docTypeName, WebServiceConstants.MIDDLE_PAGE);

									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesMiddle);
									LOGGER.info("Sucessfully Uploaded Images for image-classification-sample");
								}
							}

							if (docType.getPageTypeLast() != null
									&& docType.getPageTypeLast().getFilesToBeUploaded().getFileName() != null
									&& !docType.getPageTypeLast().getFilesToBeUploaded().getFileName().isEmpty()) {
								fileNamesLast = docType.getPageTypeLast().getFilesToBeUploaded().getFileName();
								if (LUCENE_SEARCH_CLASSIFICATION_TYPE.equalsIgnoreCase(learningType)) {
									searchPathName = EphesoftStringUtil
											.concatenate(
													batchSchemaService.getSearchClassSamplePath(
															uploadLearningFileXML.getBatchClassId(), true), File.separator,
													docType.getDocTypeName(), File.separator, docTypeName,
													WebServiceConstants.LAST_PAGE);

									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesLast);
									LOGGER.info("Sucessfully Uploaded Images for lucene-search-classification-sample");
								} else if ("Image".equalsIgnoreCase(learningType)) {

									searchPathName = EphesoftStringUtil.concatenate(batchSchemaService.getImageMagickBaseFolderPath(
											uploadLearningFileXML.getBatchClassId(), true), File.separator, docType.getDocTypeName(),
											File.separator, docTypeName, WebServiceConstants.LAST_PAGE);

									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesLast);
									LOGGER.info("Sucessfully Uploaded Images for image-classification-sample");
								} else {

									searchPathName = EphesoftStringUtil
											.concatenate(
													batchSchemaService.getSearchClassSamplePath(
															uploadLearningFileXML.getBatchClassId(), true), File.separator,
													docType.getDocTypeName(), File.separator, docTypeName,
													WebServiceConstants.LAST_PAGE);

									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesLast);
									LOGGER.info("Sucessfully Uploaded Images for lucene-search-classification-sample");

									searchPathName = EphesoftStringUtil.concatenate(batchSchemaService.getImageMagickBaseFolderPath(
											uploadLearningFileXML.getBatchClassId(), true), File.separator, docType.getDocTypeName(),
											File.separator, docTypeName, WebServiceConstants.LAST_PAGE);

									uploadInputImagesToLearningFolder(searchPathName, workingDir, fileNamesLast);
									LOGGER.info("Sucessfully Uploaded Images for image-classification-sample");
								}
							}
						}
					} else {
						final RestError restError = createUnprocessableEntityRestError(
								WebServiceConstants.INVALID_NUMBER_OF_FILES_FOR_UPLOAD_LEARNING,
								WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_CODE);
						LOGGER.error("Mismatch in the XML input and files sent.");
						throw new ValidationException(WebServiceConstants.INVALID_NUMBER_OF_FILES_FOR_UPLOAD_LEARNING, restError);
					}
				} else {
					final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
							WebServiceConstants.PARAMETER_XML_INCORRECT_CODE, inputXMLValidationRes, inputXMLValidationRes
									+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
					LOGGER.error(WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE + WebServiceConstants.HTTP_STATUS
							+ HttpStatus.UNPROCESSABLE_ENTITY);
					throw new InternalServerException(inputXMLValidationRes, restError);
				}

			} catch (final FileNotFoundException fe) {

				respStr = WebServiceConstants.INPUT_XML_NOT_FOUND_MESSAGE;
				final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
						WebServiceConstants.INPUT_XML_NOT_FOUND_CODE, respStr, WebServiceConstants.INPUT_XML_NOT_FOUND_MESSAGE
								+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
				LOGGER.error("Error response at server:" + respStr);

				final InternalServerException internalServerExcpetion = new InternalServerException(
						WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
				LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS);
				throw internalServerExcpetion;
			} catch (final org.xml.sax.SAXParseException ex) {

				respStr = "Error in Parsing Input XML.Please try again" + ex.getMessage();
				final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
						WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_CODE, respStr,
						WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
						WebServiceConstants.DEFAULT_URL);
				LOGGER.error("Error response at server:" + respStr);

				final InternalServerException internalServerExcpetion = new InternalServerException(
						WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
				LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS);
				throw internalServerExcpetion;
				// JIRA-Bug-11130
			} catch (InternalServerException internalServerException) {
				throw internalServerException;
			} catch (ValidationException validationException) {
				throw validationException;
			} catch (final Exception exception) {
				final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
				respStr = WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + exception;
				final RestError restError = new RestError(status, WebServiceConstants.INTERNAL_SERVER_ERROR_CODE,
						WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
								+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
				LOGGER.error("Error response at server:" + respStr);

				final InternalServerException internalServerExcpetion = new InternalServerException(
						WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
				LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
				throw internalServerExcpetion;
			}

			finally {
				try {
					if (!workingDir.isEmpty()) {
						FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
					}
				} catch (final Exception ex) {
					final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
					respStr = WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + ex.getMessage();
					final RestError restError = new RestError(status, WebServiceConstants.INTERNAL_SERVER_ERROR_CODE,
							WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
									+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
					LOGGER.error("Error response at server:" + respStr);
					final InternalServerException internalServerExcpetion = new InternalServerException(
							WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
					LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
					throw internalServerExcpetion;
				}
			}
		} else {
			final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			respStr = WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE;
			final RestError restError = new RestError(status, WebServiceConstants.IMPROPER_INPUT_TO_SERVER_CODE,
					WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE, WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE
							+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
			LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
			throw new InternalServerException(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE, restError);
		}

	}

	/**
	 * This Method is Used to Check Input XML for the set of Validations based on evaluation of which Learning files are uploaded
	 * 
	 * @param uploadLearningInputXML
	 * @return
	 */
	private String validateInputXMLForLearning(final UploadLearningFiles uploadLearningInputXML) {
		String errorMsg = WebServiceConstants.EMPTY_STRING;
		try {
			if (uploadLearningInputXML.getBatchClassId() != null && !uploadLearningInputXML.getBatchClassId().isEmpty()) {
				final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(uploadLearningInputXML.getBatchClassId());
				if (batchClass == null) {
					errorMsg = "Input batch class doesn't exists.Please enter a valid batch class";
					return errorMsg;
				}
				final List<DocType> docTypes = uploadLearningInputXML.getDocType();
				if (docTypes != null || !docTypes.isEmpty()) {
					for (final DocType docType : docTypes) {
						if (docType.getDocTypeName() == null || docType.getDocTypeName().isEmpty()) {
							errorMsg = "Input document type name is null.Please enter a document name of batch class  "
									+ uploadLearningInputXML.getBatchClassId();
							return errorMsg;
						} else {
							final boolean isValidDocName = validateDocumentName(docType.getDocTypeName(),
									uploadLearningInputXML.getBatchClassId());
							if (isValidDocName) {
								errorMsg = "Input document type name doesn't exist.Please enter a valid document name of batch class identifier  "
										+ uploadLearningInputXML.getBatchClassId();
								return errorMsg;
							}
						}
						if (docType.getLearningType() != null && !docType.getLearningType().isEmpty()) {
							if (!LUCENE_SEARCH_CLASSIFICATION_TYPE.equalsIgnoreCase(docType.getLearningType())
									&& !IMAGE_CLASSIFICATION_SAMPLE_TYPE.equalsIgnoreCase(docType.getLearningType())
									&& !"Both".equalsIgnoreCase(docType.getLearningType())) {
								errorMsg = "Input learning type name is invalid.Please enter learning type as lucene/image/both for batch class "
										+ uploadLearningInputXML.getBatchClassId()
										+ " and Document name is "
										+ docType.getDocTypeName();
								return errorMsg;
							} else {
								if (docType.getPageTypeFirst() == null && docType.getPageTypeMiddle() == null
										&& docType.getPageTypeLast() == null) {
									errorMsg = "Input page type name is null.Please enter at least one page type with files for batch class identifier "
											+ uploadLearningInputXML.getBatchClassId()
											+ " and Document Nanme is  "
											+ docType.getDocTypeName();
								} else {
									if ((docType.getPageTypeFirst().getFilesToBeUploaded().getFileName() == null || docType
											.getPageTypeFirst().getFilesToBeUploaded().getFileName().size() < 0)
											&& (docType.getPageTypeMiddle().getFilesToBeUploaded().getFileName() == null || docType
													.getPageTypeMiddle().getFilesToBeUploaded().getFileName().size() < 0)
											&& (docType.getPageTypeLast().getFilesToBeUploaded().getFileName() == null || docType
													.getPageTypeFirst().getFilesToBeUploaded().getFileName().size() < 0)) {
										errorMsg = "No Images available for Learning for Batch Class "
												+ uploadLearningInputXML.getBatchClassId() + " and Document Name is"
												+ docType.getDocTypeName();
										return errorMsg;
									} else {
										boolean checkPageTypeExtFirst = false;
										boolean checkPageTypeExtMid = false;
										boolean checkPageTypeExtLast = false;
										if (docType.getPageTypeFirst() != null
												&& docType.getPageTypeFirst().getFilesToBeUploaded().getFileName() != null
												&& docType.getPageTypeFirst().getFilesToBeUploaded().getFileName().size() > 0) {

											checkPageTypeExtFirst = checkZipOrTiffExtensions(docType.getPageTypeFirst()
													.getFilesToBeUploaded().getFileName());
										}
										if (docType.getPageTypeMiddle() != null
												&& docType.getPageTypeMiddle().getFilesToBeUploaded().getFileName() != null
												&& docType.getPageTypeMiddle().getFilesToBeUploaded().getFileName().size() > 0) {
											checkZipOrTiffExtensions(docType.getPageTypeMiddle().getFilesToBeUploaded().getFileName());
										}
										if (docType.getPageTypeLast() != null
												&& docType.getPageTypeLast().getFilesToBeUploaded().getFileName() != null
												&& docType.getPageTypeLast().getFilesToBeUploaded().getFileName().size() > 0) {
											checkPageTypeExtLast = checkZipOrTiffExtensions(docType.getPageTypeLast()
													.getFilesToBeUploaded().getFileName());
										}
										if (!checkPageTypeExtFirst && !checkPageTypeExtLast && !checkPageTypeExtLast) {
											errorMsg = "Input file type should be either tiff/tiff or ZIP folder containig tiff/tif images"
													+ uploadLearningInputXML.getBatchClassId()
													+ "and Document Name is"
													+ docType.getDocTypeName();
											return errorMsg;
										}
									}
								}
							}
						} else {
							errorMsg = "Input Learning Type Name is either  Null or empty. Please Enter a Learning Type for Batch Class "
									+ uploadLearningInputXML.getBatchClassId() + " and Document Name is " + docType.getDocTypeName();
							return errorMsg;
						}
					}
				} else {
					errorMsg = "Input Document Type is Null.Please Enter Atleast one Documument Type for Batch Class  "
							+ uploadLearningInputXML.getBatchClassId();
					return errorMsg;
				}
			} else {
				errorMsg = "Input batch class identifier is null.Please enter a valid indentifier";
			}
		} catch (final Exception e) {
			LOGGER.error("Exception Caught in Validating input XML for upload Files for Learning" + e);
			errorMsg = "Exception Caught in Validating input XML for upload Files for Learning.Please check Logs for Details";
		}
		return errorMsg;
	}

	/**
	 * This Method is Used to Unzip uploaded file to a temporary putput Location
	 * 
	 * @param zipFilePath
	 * @param workingDir
	 * @return
	 * @throws InternalServerException
	 */
	private String unZipToTemporaryOutputLocation(final String zipFilePath, final String workingDir) throws InternalServerException {
		LOGGER.info("zip file path:" + zipFilePath);
		final File tempZipFile = new File(workingDir + File.separator + zipFilePath);
		final String tempOutputUnZipDir = tempZipFile.getParent() + File.separator
				+ tempZipFile.getName().substring(0, tempZipFile.getName().indexOf(WebServiceUtil.DOT));
		LOGGER.info("temporary Output UnZip Directory:" + tempOutputUnZipDir);
		try {
			FileUtils.unzip(tempZipFile, tempOutputUnZipDir);
		} catch (final Exception e) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
			tempZipFile.delete();
			final String errorMessage = "Unable to unzip file. Returning without processing the results.";
			LOGGER.error(errorMessage);
			throw new InternalServerException(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + getAdditionalInfo(errorMessage),
					new RestError(HttpStatus.INTERNAL_SERVER_ERROR, WebServiceConstants.INTERNAL_SERVER_ERROR_CODE,
							WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
									+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL));
		}
		return tempOutputUnZipDir;
	}

	/**
	 * This Method Takes Input List of File Names Check for their Extensions if its Zip or Tiff this Method will return True else false
	 * will be returned
	 * 
	 * @param fileNames
	 * @return boolean
	 */
	private boolean checkZipOrTiffExtensions(final List<String> fileNames) {
		boolean isZippOrTiffFile = false;
		if (!fileNames.isEmpty() || fileNames != null) {
			for (final String fileName : fileNames) {
				if (fileName.toLowerCase().indexOf(FileType.TIF.getExtension().toLowerCase()) > -1
						|| fileName.toLowerCase().indexOf(FileType.ZIP.getExtension().toLowerCase()) > -1
						|| fileName.toLowerCase().indexOf(FileType.TIFF.getExtension().toLowerCase()) > -1) {
					isZippOrTiffFile = true;
				}
			}
		}
		return isZippOrTiffFile;
	}

	/**
	 * This Method takes all the File Names as Input,checks whether they are .tif/.tiff , if ZIP folder is specified all the files
	 * inside Zip folder are checked for .tif/.tiff extension are are copied to searchPath Name specified
	 * 
	 * @param searchPathName
	 * @param workingDir
	 * @param fileNames
	 * @throws InternalServerException
	 * @throws FileNotFoundException
	 */
	private void uploadInputImagesToLearningFolder(final String searchPathName, final String workingDir, final List<String> fileNames)
			throws InternalServerException {
		String respStr = "";
		try {
			for (final String fileName : fileNames) {
				if (fileName.toLowerCase().indexOf(FileType.TIFF.getExtension()) > -1
						|| fileName.toLowerCase().indexOf(FileType.TIF.getExtension()) > -1) {
					final File saveDirectory = new File(searchPathName);
					saveDirectory.mkdirs();
					FileUtils.copyFile(new File(workingDir + File.separator + fileName), new File(searchPathName + File.separator
							+ fileName));
				}
				if (fileName.toLowerCase().indexOf(FileType.ZIP.getExtension()) > -1) {
					final String unZipFileLocation = unZipToTemporaryOutputLocation(fileName, workingDir);
					final File fPageFolder = new File(unZipFileLocation + File.separator
							+ fileName.substring(0, fileName.indexOf(".")));
					final String[] listOfImageFiles = fPageFolder.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
							FileType.TIFF.getExtensionWithDot()));
					if (listOfImageFiles != null && listOfImageFiles.length > 0) {
						for (final String imageFile : listOfImageFiles) {
							final File saveDirectory = new File(searchPathName);
							saveDirectory.mkdirs();
							FileUtils.copyFile(
									new File(unZipFileLocation + File.separator + fileName.substring(0, fileName.indexOf("."))
											+ File.separator + imageFile), new File(searchPathName + File.separator + imageFile));
						}
					} else {
						LOGGER.error("No Image File Exists at Location Specified " + fileName);
						final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
						respStr = "Input file" + fileName + " is not found.Please try again ";
						final RestError restError = new RestError(status, WebServiceConstants.INPUT_FILES_NOT_FOUND_CODE, respStr,
								WebServiceConstants.INPUT_IMAGE_NOT_FOUND_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
								WebServiceConstants.DEFAULT_URL);
						LOGGER.error("Error response at server:" + respStr);
						;
						if (!workingDir.isEmpty()) {
							FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
						}
						final InternalServerException internalServerExcpetion = new InternalServerException(
								WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
						LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
						throw internalServerExcpetion;
					}
				}
			}
		} catch (final FileNotFoundException e) {
			final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			respStr = "Input file is not found.Please try again";
			final RestError restError = new RestError(status, WebServiceConstants.INPUT_FILES_NOT_FOUND_CODE, respStr,
					WebServiceConstants.INPUT_IMAGE_NOT_FOUND_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
					WebServiceConstants.DEFAULT_URL);
			LOGGER.error("Error response at server:" + respStr);
			if (!workingDir.isEmpty()) {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
			}
			final InternalServerException internalServerExcpetion = new InternalServerException(
					WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
			LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
			throw internalServerExcpetion;

		} catch (final Exception Ex) {
			final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			respStr = "Error while uploading images.Please try again";
			final RestError restError = new RestError(status, WebServiceConstants.INPUT_IMAGE_NOT_ABLE_TO_UPLOAD, respStr,
					WebServiceConstants.INPUT_IMAGE_NOT_ABLE_TO_UPLOAD_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
					WebServiceConstants.DEFAULT_URL);
			LOGGER.error("Error response at server:" + respStr);
			;
			if (!workingDir.isEmpty()) {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
			}
			final InternalServerException internalServerExcpetion = new InternalServerException(
					WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
			LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
			throw internalServerExcpetion;
		}
	}

	/**
	 * This method is Added to process input XML to implement auto Learning for KV feature
	 * 
	 * @param req the {@link HttpServletRequest} the request header for this web service hit
	 * @throws InternalServerException the internal server exception
	 * @throws ValidationException the validation exception
	 */

	public ExtractKV processKVLearningDocType(final HttpServletRequest req) throws InternalServerException, ValidationException {
		final com.ephesoft.dcma.batch.schema.ExtractKV extractKV = new com.ephesoft.dcma.batch.schema.ExtractKV();

		LOGGER.info("Inside autoLearningKV method for WebService");
		String respStr = WebServiceConstants.EMPTY_STRING;
		String workingDir = WebServiceConstants.EMPTY_STRING;
		Coordinates keyRectangleCoordinates = null;
		Coordinates valueRectangleCoordinates = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = batchSchemaService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				if (fileMap.size() != 2) {
					final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
					respStr = "Improper input to server. Expected two files: hocrFileZip and xml parameter file.";
					final RestError restError = new RestError(status, WebServiceConstants.IMPROPER_INPUT_TO_SERVER_CODE, respStr,
							WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
							WebServiceConstants.DEFAULT_URL);
					LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
					throw new InternalServerException(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE, restError);
				}
				String xmlFileName = WebServiceConstants.EMPTY_STRING;
				xmlFileName = getXMLFile(workingDir, multiPartRequest, fileMap);
				LOGGER.info("XML file name is" + xmlFileName);
				KVExtractionDocType kvExtract = null;
				final File xmlFile = new File(workingDir + File.separator + xmlFileName);
				if (xmlFile.exists()) {
					final FileInputStream inputStream = new FileInputStream(xmlFile);
					final Source source = XMLUtil.createSourceFromStream(inputStream);
					kvExtract = (KVExtractionDocType) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
					if (kvExtract != null) {
						final String inputXMLValidationRes = validateInputXMLForAutoKVExtraction(kvExtract);
						if (inputXMLValidationRes.isEmpty()) {
							final String documentName = kvExtract.getDocumentType();

							final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(kvExtract.getBatchClass());
							DocumentType documentType = new DocumentType();
							final List<DocumentType> docTypes = batchClass.getDocumentTypes();
							if (docTypes != null && !docTypes.isEmpty()) {
								for (final DocumentType docType : docTypes) {
									if (documentName.equalsIgnoreCase(docType.getName())) {
										documentType = batchClass.getDocumentTypeByIdentifier(docType.getIdentifier());
									}
								}

							} else {
								respStr = "Document Type doesn't Exist.Please enter a valid document type of batch class" + batchClass;
								final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
										WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_CODE, respStr,
										WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_MESSAGE
												+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
								LOGGER.error("Error response at server:" + respStr);
								throw new InternalServerException(respStr, restError);
							}
							final List<com.ephesoft.dcma.batch.schema.HOCRFile> hocrFileList = new ArrayList<com.ephesoft.dcma.batch.schema.HOCRFile>();
							final String hocrFileZipFileName = kvExtract.getHOCRFile();

							final String unZipFileLocation = unZipToTemporaryOutputLocation(hocrFileZipFileName, workingDir);

							final File fPageFolder = new File(unZipFileLocation);
							final String[] listOfHOCRFiles = fPageFolder.list(new CustomFileFilter(false, FileType.XML
									.getExtensionWithDot()));
							for (final String hocrFileName : listOfHOCRFiles) {
								final HOCRFile hocrFile1 = new HOCRFile();
								final HOCRFile.DocumentLevelFields dlfs = new HOCRFile.DocumentLevelFields();

								LOGGER.info("Number of HOCR files" + listOfHOCRFiles.length);
								List<com.ephesoft.dcma.da.domain.FieldType> fieldTypes = null;
								if (documentType != null) {
									fieldTypes = documentType.getFieldTypes();
								} else {
									respStr = "Document Type doesn't Exist.Please enter a valid document type of batch class"
											+ batchClass;
									final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
											WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_CODE, respStr,
											WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_MESSAGE
													+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
									LOGGER.error("Error response at server:" + respStr);
									throw new InternalServerException(respStr, restError);
								}
								if (fieldTypes != null && !fieldTypes.isEmpty()) {
									for (final com.ephesoft.dcma.da.domain.FieldType fieldType : fieldTypes) {
										final List<com.ephesoft.dcma.da.domain.KVExtraction> kvExtracttion = fieldType
												.getKvExtraction();
										final List<DocField> updtDocList = new ArrayList<DocField>(5);
										if (kvExtracttion != null && !kvExtracttion.isEmpty()) {
											for (final com.ephesoft.dcma.da.domain.KVExtraction extractKVFields : kvExtracttion) {

												final ExtractKVParams params = new ExtractKVParams();

												final OutputStream outStream = null;
												final InputStream instream = null;

												final Params paramList = new Params();
												if (extractKVFields.getLocationType() != null) {
													paramList.setLocationType(extractKVFields.getLocationType().toString());
												}
												if (extractKVFields.getNoOfWords() != null) {
													paramList.setNoOfWords(extractKVFields.getNoOfWords());
												}
												if (extractKVFields.getKeyPattern() != null) {
													paramList.setKeyPattern(extractKVFields.getKeyPattern());
												}
												if (extractKVFields.getValuePattern() != null) {
													paramList.setValuePattern(extractKVFields.getValuePattern());
												}
												if (extractKVFields.getFetchValue() != null) {
													paramList.setKVFetchValue(extractKVFields.getFetchValue().toString());
												}

												// Null values are allowed
												paramList.setWeight(extractKVFields.getWeight());
												paramList.setKeyFuzziness(extractKVFields.getKeyFuzziness());

												if (extractKVFields.getAdvancedKVExtraction() != null) {
													paramList.setLength(extractKVFields.getLength());
													paramList.setWidth(extractKVFields.getWidth());
													paramList.setXoffset(extractKVFields.getXoffset());
													paramList.setYoffset(extractKVFields.getYoffset());
													paramList.setMultiplier(Float.valueOf(1));
													paramList.setAdvancedKV(true);

													final AdvancedKVExtraction advancedKVExtraction = extractKVFields
															.getAdvancedKVExtraction();
													if (advancedKVExtraction != null) {
														keyRectangleCoordinates = new Coordinates();
														valueRectangleCoordinates = new Coordinates();
														keyRectangleCoordinates.setX0(BigInteger.valueOf(advancedKVExtraction
																.getKeyX0Coord()));
														keyRectangleCoordinates.setY0(BigInteger.valueOf(advancedKVExtraction
																.getKeyY0Coord()));
														keyRectangleCoordinates.setX1(BigInteger.valueOf(advancedKVExtraction
																.getKeyX1Coord()));
														keyRectangleCoordinates.setY1(BigInteger.valueOf(advancedKVExtraction
																.getKeyY1Coord()));
														valueRectangleCoordinates.setX0(BigInteger.valueOf(advancedKVExtraction
																.getValueX0Coord()));
														valueRectangleCoordinates.setY0(BigInteger.valueOf(advancedKVExtraction
																.getValueY0Coord()));
														valueRectangleCoordinates.setX1(BigInteger.valueOf(advancedKVExtraction
																.getValueX1Coord()));
														valueRectangleCoordinates.setY1(BigInteger.valueOf(advancedKVExtraction
																.getValueY1Coord()));
													}

												}
												params.getParams().add(paramList);
												final String filePath = unZipFileLocation + File.separator + hocrFileName;
												final HocrPages hocrPages = batchSchemaService.getHOCR(filePath);

												final boolean isSuccess = kvService.extractKVDocumentFieldsFromHOCR(updtDocList,
														hocrPages, params, keyRectangleCoordinates, valueRectangleCoordinates);
												// final boolean
												// isSuccess=kvService.extractKVFromHOCRForBatchClass(updtDocList,hocrPages,kvDocTypeLearning.getBatchClass(),kvDocTypeLearning.getDocumentType());

												if (!isSuccess) {
													respStr = "Unable to perform KV Extraction for HOCR file" + hocrFileName;
													final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
															WebServiceConstants.INTERNAL_SERVER_ERROR_CODE,
															WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE,
															WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
																	+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
															WebServiceConstants.DEFAULT_URL);
													LOGGER.error("Error response at server:" + respStr);
													throw new InternalServerException(respStr, restError);
												}
												hocrFile1.setFileName(hocrFileName);

											}
										}

										final DocField docField = getDocLevelField(fieldType, updtDocList);
										dlfs.getDocumentLevelField().add(docField);
									}
								} else {
									respStr = "No Field Type is Defined for the document.Please try again.";
									final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
											WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_CODE, respStr,
											WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_MESSAGE
													+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
									LOGGER.error("Error response at server:" + respStr);
									throw new InternalServerException(respStr, restError);

								}
								hocrFile1.setDocumentLevelFields(dlfs);
								extractKV.getHOCRFile().add(hocrFile1);
							}
						} else {
							FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
							final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
									WebServiceConstants.PARAMETER_XML_INCORRECT_CODE, inputXMLValidationRes,
									WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE
											+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
							LOGGER.error(WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE + WebServiceConstants.HTTP_STATUS
									+ HttpStatus.UNPROCESSABLE_ENTITY);
							throw new InternalServerException(inputXMLValidationRes, restError);
						}
					} else {
						respStr = WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE;
						final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
								WebServiceConstants.INPUT_FILES_NOT_FOUND_CODE, WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE,
								WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
								WebServiceConstants.DEFAULT_URL);
						LOGGER.error(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE + WebServiceConstants.HTTP_STATUS
								+ HttpStatus.UNPROCESSABLE_ENTITY);
						throw new ValidationException(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE, restError);
					}
				} else {
					respStr = WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE;
					final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
							WebServiceConstants.INPUT_FILES_NOT_FOUND_CODE, WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE,
							WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
							WebServiceConstants.DEFAULT_URL);
					LOGGER.error(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE + WebServiceConstants.HTTP_STATUS
							+ HttpStatus.UNPROCESSABLE_ENTITY);
					throw new ValidationException(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE, restError);
				}

			} catch (final ValidationException validationException) {
				throw validationException;
			} catch (final InternalServerException internalServerError) {
				throw internalServerError;
			} catch (final java.io.FileNotFoundException ex) {
				respStr = "Input File not found.Please check logs for Exception";
				final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
						WebServiceConstants.INPUT_FILES_NOT_FOUND_CODE, WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE,
						WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
						WebServiceConstants.DEFAULT_URL);
				LOGGER.error(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE + WebServiceConstants.HTTP_STATUS
						+ HttpStatus.UNPROCESSABLE_ENTITY + ex);
				throw new ValidationException(respStr, restError);
			} catch (final org.xml.sax.SAXParseException Ex) {
				respStr = "Error in Parsing Input XML.Please try again";
				final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
						WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_CODE, respStr,
						WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
						WebServiceConstants.DEFAULT_URL);
				LOGGER.error("Error response at server:" + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
				final InternalServerException internalServerExcpetion = new InternalServerException(
						WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
				LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS);
				throw internalServerExcpetion;

			} catch (final Exception exception) {
				final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
				respStr = WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + exception;
				final RestError restError = new RestError(status, WebServiceConstants.INTERNAL_SERVER_ERROR_CODE,
						WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
								+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
				LOGGER.error("Error response at server:" + respStr);
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
				final InternalServerException internalServerExcpetion = new InternalServerException(
						WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
				LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
				throw internalServerExcpetion;
			}
		}

		else {
			final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
			respStr = WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE;
			final RestError restError = new RestError(status, WebServiceConstants.IMPROPER_INPUT_TO_SERVER_CODE,
					WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE, WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE
							+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
			LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
			throw new InternalServerException(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE, restError);
		}
		if (!workingDir.isEmpty()) {
			FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
		}
		return extractKV;
	}

	private DocField getDocLevelField(final com.ephesoft.dcma.da.domain.FieldType fieldType, final List<DocField> updtDocList) {
		LOGGER.info("Getting DLF according to the defined KV rules");
		DocField finalDocField = null;

		final String fieldTypeName = fieldType.getName();
		final int fieldorderNumber = fieldType.getFieldOrderNumber();
		final String fieldDataType = fieldType.getDataType().toString();
		if (null != fieldType && !CollectionUtil.isEmpty(updtDocList)) {
			final AlternateValues finalAlternateValues = new AlternateValues();
			final List<Field> alternateValue = finalAlternateValues.getAlternateValue();
			float maxConf = 0;
			Field maxField = null;
			float confidence = 0;
			Field field = null;
			AlternateValues alternateValues = null;
			List<Field> alternateSubList = null;

			// to add other extracted DLF in the alternate tag
			for (DocField docField : updtDocList) {
				alternateValues = docField.getAlternateValues();
				if (null != alternateValues) {
					alternateSubList = alternateValues.getAlternateValue();
					for (Field alternateField : alternateSubList) {
						alternateField.setName(fieldTypeName);
						alternateField.setType(fieldTypeName);
						alternateField.setFieldOrderNumber(fieldorderNumber);
					}
					alternateValue.addAll(alternateSubList);
					docField.setAlternateValues(null);
				}

				docField.setName(fieldTypeName);
				docField.setFieldOrderNumber(fieldorderNumber);
				docField.setType(fieldDataType);
				field = convertDocFieldToField(docField, fieldorderNumber, fieldDataType);

				alternateValue.add(field);
				confidence = docField.getConfidence();
				if (maxConf < confidence) {
					maxConf = confidence;
					maxField = field;
					finalDocField = docField;
				}
			}

			alternateValue.remove(maxField);
			if (null != finalDocField) {
				finalDocField.setAlternateValues(finalAlternateValues);
			}
		}

		if (null == finalDocField) {
			finalDocField = new DocField();
			finalDocField.setConfidence(0f);
			finalDocField.setOcrConfidence(0f);
			finalDocField.setOcrConfidenceThreshold(0f);
			finalDocField.setValue(WebServiceUtil.EMPTY_STRING);
			finalDocField.setPage(WebServiceUtil.EMPTY_STRING);
			finalDocField.setFieldOrderNumber(fieldorderNumber);
			finalDocField.setType(fieldDataType);
			finalDocField.setForceReview(Boolean.FALSE);
			// finalDocField.setCategory("Uncategorised");
			finalDocField.setCategory(CategoryType.GROUP_1.getCategoryName());
			finalDocField.setOverlayedImageFileName(WebServiceUtil.EMPTY_STRING);
		}

		finalDocField.setName(fieldTypeName);
		return finalDocField;
	}

	private Field convertDocFieldToField(final DocField docField, int fieldorderNumber, String fieldDataType) {
		final Field field = new Field();
		field.setName(docField.getName());
		field.setPage(docField.getPage());
		field.setValue(docField.getValue());
		field.setConfidence(docField.getConfidence());
		field.setCoordinatesList(docField.getCoordinatesList());
		field.setFieldOrderNumber(docField.getFieldOrderNumber());
		field.setForceReview(Boolean.FALSE);
		field.setFieldOrderNumber(fieldorderNumber);
		field.setType(fieldDataType);
		return field;
	}

	/**
	 * This Method is used to check input XML for for set to validations based on evaluation of which AutoLearning is performed
	 * 
	 * @param Input XML on which Validation is performed
	 * @return If input XML violates any constraint then Error message is returned else empty string is returned
	 */

	private String validateInputXMLForAutoKVExtraction(final KVExtractionDocType autoLearningKV) {
		String errorMsg = WebServiceConstants.EMPTY_STRING;
		try {
			if (autoLearningKV.getBatchClass() != null && !autoLearningKV.getBatchClass().isEmpty()) {
				final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(autoLearningKV.getBatchClass());
				if (batchClass == null) {
					return errorMsg;
				}
				if (autoLearningKV.getDocumentType() != null && !autoLearningKV.getDocumentType().isEmpty()) {
					final boolean isValidDocName = validateDocumentName(autoLearningKV.getDocumentType(),
							autoLearningKV.getBatchClass());
					if (!isValidDocName) {
						final String hocrFileNameZip = autoLearningKV.getHOCRFile();
						if (hocrFileNameZip == null || hocrFileNameZip.isEmpty()) {
							errorMsg = "Input HOCR Files is null.Please enter a ZIP file containing HOCR Files and try again";
							return errorMsg;
						}
					} else {
						errorMsg = "Input document type name doesn't exist.Please enter a valid document name of batch class identifier "
								+ autoLearningKV.getBatchClass();
						return errorMsg;
					}
				} else {
					errorMsg = "Input Document Type is null.Please enter a valid Document Type";
					return errorMsg;
				}
			} else {
				errorMsg = "Input batch class identifier is null.Please enter a valid identifier";
			}
		} catch (final Exception ex) {
			LOGGER.error("Error in Validating Input XML, Exception Caught is " + ex.getMessage() + "'\n'" + "Reason" + ex);
			errorMsg = "Exception Caught in Validating input XML for upload Files for Learning.Please check Logs for Details";
		}
		return errorMsg;
	}

	/**
	 * This Method contains functionality to copy document type of batch Class passed as an input
	 * 
	 * @param req {@link HttpServletRequest} the request header for this web service hit
	 * @param resp {@link HttpServletResponse} the response header for this web service.
	 */
	public void processCopyDocumentType(final HttpServletRequest req) throws InternalServerException, ValidationException {
		{
			LOGGER.info("Processing Copy Document Type for Batch Class Web Service.");
			String respStr = WebServiceUtil.EMPTY_STRING;
			String workingDir = WebServiceUtil.EMPTY_STRING;
			DocumentType existingDoctype = new DocumentType();
			if (req instanceof DefaultMultipartHttpServletRequest) {
				try {
					final String webServiceFolderPath = batchSchemaService.getWebServicesFolderPath();
					workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);

					final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
					final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
					String xmlFileName = WebServiceConstants.EMPTY_STRING;
					LOGGER.info("Retreiving xml file.");
					CopyDocumentType copyDocType = null;
					xmlFileName = getXMLFile(workingDir, multiPartRequest, fileMap);
					LOGGER.info("XML file name is" + xmlFileName);
					final File xmlFile = new File(workingDir + File.separator + xmlFileName);
					if (xmlFile.exists()) {
						final FileInputStream inputStream = new FileInputStream(xmlFile);
						final Source source = XMLUtil.createSourceFromStream(inputStream);
						copyDocType = (CopyDocumentType) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
						if (copyDocType != null && respStr.isEmpty()) {
							// Create Method to Validate Input XML
							respStr = validateCopyDocTypeInputXML(copyDocType);

							if (respStr.isEmpty()) {
								final DocumentType newDocumentType = new DocumentType();
								final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(copyDocType
										.getBatchClassId());
								final List<DocumentType> documentList = batchClass.getDocumentTypes();
								if (documentList != null) {
									for (final DocumentType docType : documentList) {
										if (docType.getName().equals(copyDocType.getExistingDocTypeName())) {
											existingDoctype = batchClass.getDocumentTypeByIdentifier(docType.getIdentifier());
										}
									}
								}
								newDocumentType.setName(copyDocType.getNewDocumentType().getName());
								newDocumentType.setDescription(copyDocType.getNewDocumentType().getDescription());
								newDocumentType.setMinConfidenceThreshold(Float.parseFloat(copyDocType.getNewDocumentType()
										.getConfidenceThreshold()));
								if (existingDoctype.getFirstPageProjectFileName() != null) {
									newDocumentType.setFirstPageProjectFileName(existingDoctype.getFirstPageProjectFileName());
								}
								if (existingDoctype.getSecondPageProjectFileName() != null) {
									newDocumentType.setFirstPageProjectFileName(existingDoctype.getSecondPageProjectFileName());
								}
								if (existingDoctype.getThirdPageProjectFileName() != null) {
									newDocumentType.setFirstPageProjectFileName(existingDoctype.getThirdPageProjectFileName());
								}
								if (existingDoctype.getFourthPageProjectFileName() != null) {
									newDocumentType.setFirstPageProjectFileName(existingDoctype.getFourthPageProjectFileName());
								}
								docTypeService.copyDocumentType(existingDoctype, newDocumentType);
								batchClass.addDocumentType(newDocumentType);
								batchClassService.merge(batchClass);
							} else {
								final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
										WebServiceConstants.PARAMETER_XML_INCORRECT_CODE, respStr,
										WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE
												+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
								LOGGER.error(WebServiceConstants.PARAMETER_XML_INCORRECT_MESSAGE + WebServiceConstants.HTTP_STATUS
										+ HttpStatus.UNPROCESSABLE_ENTITY);
								throw new InternalServerException(respStr, restError);
							}
						} else {
							respStr = WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE;
							final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
									WebServiceConstants.INPUT_FILES_NOT_FOUND_CODE, WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE,
									WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
									WebServiceConstants.DEFAULT_URL);
							LOGGER.error(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE + WebServiceConstants.HTTP_STATUS
									+ HttpStatus.UNPROCESSABLE_ENTITY);
							throw new ValidationException(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE, restError);
						}
					} else {
						respStr = WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE;
						final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
								WebServiceConstants.INPUT_FILES_NOT_FOUND_CODE, WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE,
								WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
								WebServiceConstants.DEFAULT_URL);
						LOGGER.error(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE + WebServiceConstants.HTTP_STATUS
								+ HttpStatus.UNPROCESSABLE_ENTITY);
						throw new ValidationException(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE, restError);

					}
				} catch (final XmlMappingException xmle) {
					respStr = "Error in mapping input XML. Please send it in the specified format.";
					final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY,
							WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_CODE, respStr,
							WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_MESSAGE + WebServiceConstants.CLASS_WEB_SERVICE_UTILITY,
							WebServiceConstants.DEFAULT_URL);
					LOGGER.error(WebServiceConstants.INPUT_XML_NOT_ABLE_TO_PARSE_MESSAGE + WebServiceConstants.HTTP_STATUS
							+ HttpStatus.UNPROCESSABLE_ENTITY);
					throw new ValidationException(WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE, restError);

				} catch (final ValidationException validationException) {
					throw validationException;
				} catch (final InternalServerException internalServerError) {
					throw internalServerError;
				} catch (final Exception exception) {
					final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
					respStr = WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + exception;
					final RestError restError = new RestError(status, WebServiceConstants.INTERNAL_SERVER_ERROR_CODE,
							WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE
									+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
					LOGGER.error("Error response at server:" + respStr);

					final InternalServerException internalServerExcpetion = new InternalServerException(
							WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, restError);
					LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
					throw internalServerExcpetion;
				} finally {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			} else {
				final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
				respStr = WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE;
				final RestError restError = new RestError(status, WebServiceConstants.IMPROPER_INPUT_TO_SERVER_CODE,
						WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE, WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE
								+ WebServiceConstants.CLASS_WEB_SERVICE_UTILITY, WebServiceConstants.DEFAULT_URL);
				LOGGER.error(respStr + WebServiceConstants.HTTP_STATUS + status);
				throw new InternalServerException(WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE, restError);
			}
		}
	}

	/**
	 * This Method is added to validate input XML
	 * 
	 * @param copyDocType contaiOns input values for copying a document type
	 * @return string containing result of validation of input XML
	 */
	private String validateCopyDocTypeInputXML(final CopyDocumentType copyDocType) {
		String respStr = WebServiceUtil.EMPTY_STRING;
		final String batchClassId = copyDocType.getBatchClassId();
		if (batchClassId != null & !batchClassId.isEmpty()) {
			final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
			if (batchClass != null) {
				// Validate new document type name already doesn't exist for
				// batchClass
				final List<DocumentType> documentList = batchClass.getDocumentTypes();
				boolean isNewDocTypeNameUnique = true;
				boolean isNewDocTypeDescUnique = true;
				boolean isDocTypeExist = false;
				final String newDocTypeName = copyDocType.getNewDocumentType().getName();
				final String existingdocTypeName = copyDocType.getExistingDocTypeName();
				if (documentList != null) {
					for (final DocumentType docType : documentList) {
						if (existingdocTypeName != null && existingdocTypeName.equals(docType.getName())) {
							isDocTypeExist = true;
							break;
						}
					}
					for (final DocumentType docTypeList : documentList) {
						if (newDocTypeName != null && newDocTypeName.equals(docTypeList.getName())) {
							isNewDocTypeNameUnique = false;
							break;
						}
					}
				}
				if (!isDocTypeExist) {
					respStr = "Input document type to be copied doesn't exist.Please enter Valid Document type name for batch class"
							+ batchClassId;
				}
				if (respStr.isEmpty() && !isNewDocTypeNameUnique) {
					respStr = "Name entered for new Doc Type already exist.Please enter a unique document name";
				}
				if (respStr.isEmpty()
						&& (copyDocType.getNewDocumentType().getDescription().isEmpty() || copyDocType.getNewDocumentType()
								.getDescription() == null)) {
					respStr = "Description for new Doc Type is Empty.Please enter description for new Document type";
				}
				if (respStr.isEmpty() && !isNewDocTypeDescUnique) {
					respStr = "Description entered for new Doc Type already exist.Please enter a unique document type description.";
				}
				try {
					final float confidenceThreshold = Float.parseFloat(copyDocType.getNewDocumentType().getConfidenceThreshold());
					if (confidenceThreshold < 0.0 || confidenceThreshold > 100.0) {
						respStr = "Please enter confidence threshold value between 0.0 to 100.0 for new document type";
					}
				} catch (final Exception ex) {
					respStr = "Confidence threshold should be a number for new doc type between 0.0 to 100.0";
				}
			} else {
				respStr = "Batch Class Identifer doesn't exist.Please enter a valid batchClassId";
			}

		} else {
			respStr = "Batch Class Identifer is empty.Please enter a valid batchClassId";
		}
		return respStr;
	}

	public void processCreateHOCRXML(final HttpServletRequest req, final HttpServletResponse resp) throws InternalServerException,
			ValidationException {
		LOGGER.info("Start processing web service for create HOCR-XML for Batch Class");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		File zipInFolder = null;
		File zipOutFolder = null;
		int responseCode = 0;
		WebServiceParams webServiceParams = null;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			try {
				final String webServiceFolderPath = batchSchemaService.getWebServicesFolderPath();
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final String outputDir = WebServiceUtil.createWebServiceOutputDir(workingDir);
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				if (fileMap.size() != 2) {
					LOGGER.error("Invalid Number of files sent to the server");
					respStr = WebServiceConstants.INVALID_ARGUMENTS_FOR_CREATE_HOCR;
					responseCode = WebServiceConstants.INVALID_PARAMETERS_CODE;
				} else {
					String xmlFileName = WebServiceUtil.EMPTY_STRING;
					String zipOutputLocation = WebServiceUtil.EMPTY_STRING;
					String zipFileNameWithOutExt = WebServiceUtil.EMPTY_STRING;
					for (final String fileName : fileMap.keySet()) {
						InputStream instream = null;
						OutputStream outStream = null;
						ZipInputStream zipstream = null;
						try {

							if (!(fileName.endsWith(FileType.ZIP.getExtensionWithDot())
									|| fileName.endsWith(FileType.XML.getExtensionWithDot())
									|| fileName.endsWith(FileType.TIF.getExtensionWithDot()) || fileName.endsWith(FileType.TIFF
									.getExtensionWithDot()))) {
								respStr = WebServiceConstants.INVALID_ARGUMENTS_FOR_CREATE_OCR_BATCH_CLASS;
								responseCode = WebServiceConstants.INVALID_PARAMETERS_CODE;
							}
							final MultipartFile multiPartFile = multiPartRequest.getFile(fileName);
							instream = multiPartFile.getInputStream();

							if (fileName.endsWith(FileType.XML.getExtensionWithDot())) {
								xmlFileName = fileName;
								final File file = new File(workingDir + File.separator + fileName);
								outStream = new FileOutputStream(file);
								final byte[] buf = new byte[WebServiceUtil.bufferSize];
								int len;
								while ((len = instream.read(buf)) > 0) {
									outStream.write(buf, 0, len);
								}
							}
							if (fileName.endsWith(FileType.ZIP.getExtensionWithDot())) {
								zipFileNameWithOutExt = FilenameUtils.removeExtension(fileName);
								zipInFolder = new File(workingDir + File.separator + zipFileNameWithOutExt);
								if (zipInFolder != null) {
									zipInFolder.mkdirs();
								}
								zipstream = new ZipInputStream(instream);
								ZipEntry ze = zipstream.getNextEntry();
								if (ze == null) {
									respStr = WebServiceConstants.NO_FILES_IN_ZIP_DIR;
									responseCode = WebServiceConstants.NO_FILES_IN_ZIP_DIR_CODE;
									LOGGER.error(respStr + " No files in the zip directory ");
								}
								while (ze != null) {
									String upzipFileName = ze.getName();
									LOGGER.info("Unzipping " + upzipFileName);

									if (!(upzipFileName.endsWith(FileType.TIF.getExtensionWithDot())
											|| upzipFileName.endsWith(FileType.TIFF.getExtensionWithDot()) || upzipFileName
												.endsWith(FileType.PDF.getExtensionWithDot()))) {
										respStr = WebServiceConstants.UNSUPPORTED_FILE_TYPE_EXCEPTION_MESSAGE;
										responseCode = WebServiceConstants.UNSUPPORTED_FILE_TYPE_EXCEPTION_CODE;
										LOGGER.error("File name should be a valid tif, tiff or pdf file name only");
									}
									final File filePath = new File(zipInFolder + File.separator + upzipFileName);
									outStream = new FileOutputStream(filePath);
									final byte[] buf = new byte[WebServiceUtil.bufferSize];
									int len;
									while ((len = zipstream.read(buf)) > 0) {
										outStream.write(buf, 0, len);
									}
									final int pageCount = TIFFUtil.getTIFFPageCount(zipInFolder + File.separator + upzipFileName);
									if (pageCount > 1 || upzipFileName.endsWith(FileType.PDF.getExtensionWithDot())) {
										final BatchInstanceThread threadList = new BatchInstanceThread(
												new File(zipInFolder.toString()).getName() + Math.random());
										LOGGER.info("Start spliting multipage tiff/pdf file into tiffs using image magick");
										imService.convertPdfOrMultiPageTiffToTiffUsingIM(
												"",
												filePath,
												"",
												new File(EphesoftStringUtil.concatenate(zipInFolder.toString(), File.separator,
														upzipFileName)), threadList);
										threadList.execute();
									}
									ze = zipstream.getNextEntry();
								}
							}
							if (fileName.endsWith(FileType.TIFF.getExtensionWithDot())
									|| fileName.endsWith(FileType.TIF.getExtensionWithDot())) {
								zipFileNameWithOutExt = WebServiceUtil.EMPTY_STRING;
								final File file = new File(workingDir + File.separator + fileName);
								zipInFolder = new File(workingDir);
								outStream = new FileOutputStream(file);
								final byte[] buf = new byte[WebServiceUtil.bufferSize];
								int len;
								while ((len = instream.read(buf)) > 0) {
									outStream.write(buf, 0, len);
								}
								final int pageCount = TIFFUtil.getTIFFPageCount(workingDir + File.separator + fileName);
								if (pageCount > 1) {
									final BatchInstanceThread threadList = new BatchInstanceThread(zipInFolder.getName()
											+ Math.random());
									LOGGER.info("Start spliting multipage tiff/pdf file into tiffs using image magick");
									imService.convertPdfOrMultiPageTiffToTiffUsingIM(WebServiceUtil.EMPTY_STRING, file,
											WebServiceUtil.EMPTY_STRING,
											new File(EphesoftStringUtil.concatenate(workingDir.toString(), File.separator, fileName)),
											threadList);
									threadList.execute();
								}
							}
						} finally {
							IOUtils.closeQuietly(instream);
							IOUtils.closeQuietly(outStream);
							IOUtils.closeQuietly(zipstream);
						}
					}
					final File xmlFile = new File(workingDir + File.separator + xmlFileName);
					if (StringUtils.isNotEmpty(xmlFileName) && xmlFile.exists()) {
						final FileInputStream inputStream = new FileInputStream(xmlFile);
						final Source source = XMLUtil.createSourceFromStream(inputStream);
						final Object unmarshelledObject = batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(source);
						if (!(unmarshelledObject instanceof WebServiceParams)) {
							respStr = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_MESSAGE;
							responseCode = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_CODE;
						} else {
							webServiceParams = (WebServiceParams) unmarshelledObject;
							if (null != webServiceParams.getParams()) {
								final List<Param> paramList = webServiceParams.getParams().getParam();
								if (paramList == null || paramList.isEmpty()) {
									final HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
									respStr = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_MESSAGE;
									responseCode = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_CODE;
									LOGGER.error(respStr + "\n No Parameters in the request" + status);
								} else {
									String batchClassId = WebServiceUtil.EMPTY_STRING;
									for (final Param param : paramList) {
										if (WebServiceConstants.BATCH_CLASS_IDENTIFIER.equalsIgnoreCase(param.getName())) {
											batchClassId = param.getValue();
											continue;
										}
										if (WebServiceUtil.ZIP_OUTPUT_LOCATION.equalsIgnoreCase(param.getName())) {
											zipOutputLocation = param.getValue();
											if (StringUtils.isBlank(zipOutputLocation) || !(new File(zipOutputLocation).isDirectory())) {
												respStr = WebServiceConstants.ZIP_OUTPUT_LOCATION_INVALID_MESSAGE;
												responseCode = WebServiceConstants.ZIP_OUTPUT_LOCATION_INVALID_CODE;
												LOGGER.error("Zip output location is blank or invalid in xml input file");
											}
											continue;
										}
										if (WebServiceUtil.ZIP_NAME.equalsIgnoreCase(param.getName())) {
											if (!((zipFileNameWithOutExt + FileType.ZIP.getExtensionWithDot())
													.equals(param.getValue()))) {
												respStr = WebServiceConstants.INPUT_ZIP_NOT_FOUND_MESSAGE;
												responseCode = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_CODE;
												LOGGER.error("Zip file name doesn't match zip file name in xml input file");
											} else
												continue;
										}
									}
									if (respStr == null || respStr.isEmpty()) {
										if (batchClassId != null && !batchClassId.isEmpty()) {
											final BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassId);
											if (batchClass != null) {

												final String ocrEngine = getDefaultHOCRPlugin(batchClassId);
												String colorSwitch = WebServiceUtil.EMPTY_STRING;
												String cmdLanguage = WebServiceUtil.EMPTY_STRING;
												LOGGER.info(EphesoftStringUtil.concatenate("Ocr engine used is : ", ocrEngine));

												if (WebServiceConstants.TESSERACT_HOCR_PLUGIN.equalsIgnoreCase(ocrEngine)) {
													final File dir = new File(zipInFolder + "");
													File[] directoryListing = dir.listFiles();
													if (directoryListing != null) {
														zipOutFolder = new File(outputDir + File.separator + zipFileNameWithOutExt);
														if (zipOutFolder != null) {
															zipOutFolder.mkdirs();
														}
														for (File inputFile : directoryListing) {
															String inputFileName = inputFile.getName();
															if (inputFileName != null && !inputFileName.isEmpty()) {
																if (inputFileName.endsWith(FileType.TIFF.getExtensionWithDot())
																		|| inputFileName.endsWith(FileType.TIF.getExtensionWithDot())) {
																	final int pageCountTiff = TIFFUtil.getTIFFPageCount(inputFile
																			.toString());
																	if (pageCountTiff > 1) {
																		continue;
																	}

																	if (WebServiceConstants.TESSERACT_HOCR_PLUGIN
																			.equalsIgnoreCase(ocrEngine)) {
																		final BatchPlugin pluginProperties = classPluginPropertiesService
																				.getPluginProperties(batchClassId,
																						WebServiceConstants.TESSERACT_HOCR_PLUGIN);
																		if (pluginProperties != null) {
																			if (pluginProperties
																					.getPluginConfigurations(TesseractProperties.TESSERACT_COLOR_SWITCH) != null) {
																				colorSwitch = classPluginPropertiesService
																						.getPropertyValue(
																								batchClassId,
																								WebServiceConstants.TESSERACT_HOCR_PLUGIN,
																								TesseractProperties.TESSERACT_COLOR_SWITCH);
																				if (pluginProperties
																						.getPluginConfigurations(TesseractProperties.TESSERACT_LANGUAGE) != null) {
																					cmdLanguage = classPluginPropertiesService
																							.getPropertyValue(
																									batchClassId,
																									WebServiceConstants.TESSERACT_HOCR_PLUGIN,
																									TesseractProperties.TESSERACT_LANGUAGE);
																					tesseractService
																							.createOCR(
																									zipInFolder.toString(),
																									colorSwitch,
																									inputFileName,
																									zipOutFolder.toString(),
																									cmdLanguage,
																									WebServiceConstants.TESSERACT_CURRENT_VERSION);
																				} else {
																					respStr = WebServiceConstants.NO_TESSERACT_LANGUAGE_SUPPORT;
																					responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
																					LOGGER.error("No Language Support");
																				}

																			} else {
																				respStr = WebServiceConstants.NO_TESSERACT_COLOR_SWITCH;
																				responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
																				LOGGER.error("Colour Switch Not Found");
																			}
																		} else {
																			respStr = WebServiceConstants.NO_PROPERTY_FOR_TESSERACT_HOCR;
																			responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
																		}
																	}

																}
															} else {
																respStr = WebServiceConstants.INVALID_FILE_NAME;
																responseCode = WebServiceConstants.INVALID_PARAMETERS_CODE;
																LOGGER.error("File Name should not be NULL or empty ");
															}
														}// End of for loop

													}

													else {
														respStr = WebServiceConstants.NO_FILES_IN_ZIP_DIR;
														responseCode = WebServiceConstants.NO_FILES_IN_ZIP_DIR_CODE;
														LOGGER.error(respStr + " No files in the zip directory ");
													}

												}
												if (respStr.isEmpty()) {
													ServletOutputStream out = null;
													ZipOutputStream zout = null;
													final String zipFileName = WebServiceUtil.serverOutputFolderName;
													resp.setContentType(WebServiceUtil.APPLICATION_X_ZIP);
													resp.setHeader(
															WebServiceUtil.CONTENT_DISPOSITION,
															WebServiceUtil.ATTACHMENT_FILENAME + zipFileName
																	+ FileType.ZIP.getExtensionWithDot() + "\"\r\n");
													resp.setStatus(HttpServletResponse.SC_OK);
													try {
														out = resp.getOutputStream();
														zout = new ZipOutputStream(out);
														FileUtils.zipDirectory(zipOutFolder.toString(), zout, zipFileName);
													} catch (final FileNotFoundException fileNotFoundException) {
														String messageString = fileNotFoundException.getMessage();
														messageString = messageString.substring(messageString
																.lastIndexOf(File.separator));
														respStr = WebServiceConstants.FILE_NOT_FOUND + messageString;
														responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
														LOGGER.error("Could Not Copy the File " + fileNotFoundException.getMessage());
													} catch (final IOException ioExcpetion) {
														respStr = WebServiceConstants.ERROR_WHILE_CREATING_ZIPPED_FILE + ioExcpetion;
														responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
														LOGGER.error(respStr);
													} finally {
														IOUtils.closeQuietly(zout);
														IOUtils.closeQuietly(out);
													}
												}
												if (respStr.isEmpty()) {
													FileOutputStream fos = null;
													ZipOutputStream zos = null;
													final File out = new File(zipOutputLocation + File.separator
															+ zipFileNameWithOutExt + FileType.ZIP.getExtensionWithDot());
													try {
														fos = new FileOutputStream(out);
														zos = new ZipOutputStream(fos);
														FileUtils.zipDirectory(zipOutFolder.toString(), zos, zipFileNameWithOutExt);

													} catch (final FileNotFoundException fileNotFoundException) {
														String messageString = fileNotFoundException.getMessage();
														messageString = messageString.substring(messageString
																.lastIndexOf(File.separator));
														respStr = WebServiceConstants.FILE_NOT_FOUND + messageString;
														responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
														LOGGER.error("Could Not Copy the File " + fileNotFoundException.getMessage());
													} catch (final IOException ioExcpetion) {
														respStr = WebServiceConstants.ERROR_WHILE_CREATING_ZIPPED_FILE + ioExcpetion;
														responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
														LOGGER.error(respStr);
													} finally {
														IOUtils.closeQuietly(zos);
														IOUtils.closeQuietly(fos);
													}
												}

											} else {
												respStr = WebServiceConstants.INVALID_BATCH_CLASS_ID_MESSAGE;
												responseCode = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_CODE;
												LOGGER.error(respStr + " Batch Class ID doesnot exist ");
											}
										} else {
											respStr = WebServiceConstants.INVALID_BATCH_CLASS_ID_MESSAGE;
											responseCode = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_CODE;
											LOGGER.error(respStr + " No input of Batch Class ID ");
										}
									}
								}
							}
						}
					} else {
						respStr = WebServiceConstants.INPUT_FILES_NOT_FOUND_MESSAGE;
						responseCode = WebServiceConstants.INVALID_PARAMETERS_CODE;
					}
				}
			} catch (final FileNotFoundException fileNotFoundException) {
				String message = fileNotFoundException.getMessage();
				message = message.substring(message.lastIndexOf(File.separator));
				respStr = WebServiceConstants.FILE_NOT_FOUND + message;
			} catch (final ValidationException validationException) {
				throw validationException;
			} catch (final InternalServerException internalServerError) {
				throw internalServerError;
			} catch (final Exception exception) {
				respStr = WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE + exception;
				responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
				exception.printStackTrace();
			} finally {
				FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir));
			}
		} else {
			respStr = WebServiceConstants.INVALID_MULTIPART_REQUEST;
			responseCode = WebServiceConstants.INVALID_PARAMETERS_CODE;
		}
		validateResponse(responseCode, respStr);
	}

	/**
	 * Processes a batch instance further its workflow from READY_FOR_REVIEW or READY_FOR_VALIDATION state.
	 * 
	 * @param loggedInUserRole the logged in user role
	 * @param identifier the batch instance identifier
	 * @throws ValidationException the validation exception - in case some parameter is not valid.
	 * @throws UnAuthorisedAccessException the unauthorised access exception - in case user does not have access to the passed batch
	 *             class.
	 */
	public void runBatchInstance(final Set<String> loggedInUserRole, final String identifier) throws ValidationException,
			UnAuthorisedAccessException, InternalServerException {
		String responseString = WebServiceConstants.EMPTY_STRING;
		int responseCode = 0;
		try {
			if (StringUtils.isNotBlank(identifier)) {
				final BatchInstance batchInstance = batchInstanceService.getBatchInstanceByIdentifier(identifier);
				if (CollectionUtils.isEmpty(loggedInUserRole)) {
					responseString = WebServiceConstants.UNAUTHORISED_ACCESS_EXCEPTION_MESSAGE;
					responseCode = WebServiceConstants.UNAUTHORISED_ACCESS_EXCEPTION_CODE;
				} else {
					if (batchInstance != null) {
						final String batchInstanceStatus = batchInstance.getStatus().name();
						if (batchInstanceStatus.equalsIgnoreCase(BatchInstanceStatus.READY_FOR_REVIEW.toString())
								|| batchInstanceStatus.equalsIgnoreCase(BatchInstanceStatus.READY_FOR_VALIDATION.toString())) {
							workflowService.signalWorkflow(identifier);
						} else {
							responseString = EphesoftStringUtil.concatenate(WebServiceConstants.BATCH_INSTANCE_STATUS_INVALID_MESSAGE,
									identifier);
							responseCode = WebServiceConstants.BATCH_INSTANCE_STATUS_INVALID_CODE;
						}
					} else {
						responseString = EphesoftStringUtil.concatenate(WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_MESSAGE,
								identifier);
						responseCode = WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_CODE;
					}
				}
			}
		} catch (final Exception exception) {
			responseString = EphesoftStringUtil.concatenate(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE, exception.getMessage());
			responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
		}
		validateResponse(responseCode, responseString);
	}

	/**
	 * Creates the UNPROCESSABLE Entity Rest Error with the Response Code and the response Message.
	 * 
	 * @param responseString {@link String}
	 * @param responseCode int
	 */
	public static RestError createUnprocessableEntityRestError(final String responseString, final int responseCode) {
		final RestError restError = new RestError(HttpStatus.UNPROCESSABLE_ENTITY, responseCode, responseString, responseString,
				WebServiceConstants.DEFAULT_URL);
		return restError;
	}

	/**
	 * A utility function which check that the request is an MultiPart HTTP Request or not. If its a multiPart request then returns the
	 * multipartHTTPServletRequest else throws a validation Exception
	 * 
	 * @param request {@link HttpServletRequest}
	 * @return {@link DefaultMultipartHttpServletRequest}
	 * @throws ValidationException {@link ValidationException}
	 */
	public static DefaultMultipartHttpServletRequest getDefaultMultipartHttpServletRequest(final HttpServletRequest request)
			throws ValidationException {
		DefaultMultipartHttpServletRequest defaultRequest = null;
		if (null != request && request instanceof DefaultMultipartHttpServletRequest) {
			defaultRequest = (DefaultMultipartHttpServletRequest) request;
		} else {
			final String errorMessage = WebServiceConstants.IMPROPER_INPUT_TO_SERVER_MESSAGE;
			final int errorCode = WebServiceConstants.IMPROPER_INPUT_TO_SERVER_CODE;
			final RestError validationRestError = WebServiceHelper.createUnprocessableEntityRestError(errorMessage, errorCode);
			throw new ValidationException(errorMessage, validationRestError);
		}
		return defaultRequest;
	}

	/**
	 * Checks whether or not the RSP File actually Exist under the BatchClass.
	 * <p>
	 * It accepts the name of the filename and the Batch Class ID. and returns false when any of them is invalid or null.
	 * </p>
	 * 
	 * @param fileName : name of the File to be searched should be a valid fileName
	 * @param batchClassID : the identifier of the Batch Class.
	 * @return : true if the file exist , else false.
	 */
	public boolean isExistingFixedFormExtractionFile(final String fileName, final String batchClassID) {
		boolean containsFile = false;
		if (StringUtils.isNotBlank(fileName) && StringUtils.isNotEmpty(batchClassID)) {
			final String fixedFormFolderLocation = EphesoftStringUtil.concatenate(batchSchemaService.getBaseFolderLocation(),
					File.separator, batchClassID, File.separator, WebServiceConstants.FIXED_FORM_EXTRACTION_FOLDER);
			final File fixedFormFolder = new File(fixedFormFolderLocation);
			containsFile = FileUtils.isFileExists(fileName, fixedFormFolder);
		}
		return containsFile;
	}

	private void validateResponse(final int responseCode, final String respStr) throws ValidationException, InternalServerException {
		if (!respStr.isEmpty()) {
			final RestError error = new RestError(HttpStatus.UNPROCESSABLE_ENTITY, responseCode, respStr, respStr,
					WebServiceConstants.DEFAULT_URL);
			if (responseCode == WebServiceConstants.INTERNAL_SERVER_ERROR_CODE) {
				throw new ValidationException(respStr, error);
			} else {
				throw new InternalServerException(respStr, error);
			}
		}
	}

	/**
	 * Helper method for generating error message.
	 * 
	 * @param errorMessage is the error message
	 * @param errorDeveloperMessage is more detailed message
	 * @param errorCode is the error code
	 * @return RestError which encapsulates all the above
	 */
	public RestError generateRestError(final String errorMessage, final String errorDeveloperMessage, final int errorCode) {

		final int statusCode = errorCode / WebServiceConstants.DIVISOR_ERROR_CODE;

		final RestError restError = new RestError(HttpStatus.valueOf(statusCode), errorCode, errorMessage, errorDeveloperMessage,
				WebServiceConstants.DEFAULT_URL);
		return restError;

	}

	/**
	 * This method will return the number of files available in MultiValueMap for a particular extention
	 * 
	 * @param fileExtension {@link String} the extension of the file whose count is required
	 * @param fileMap {@link MultiValueMap} the MultiValuemapMultiValueMap<String, MultipartFile>
	 * @return count {@link int } the number of files present in the MultiValueMap
	 */

	private int checkFileCountForExtentionType(String fileExtension, MultiValueMap<String, MultipartFile> fileMap) {
		// TODO Auto-generated method stub
		LOGGER.info("Getting number of files present with speified file type in multi part request");
		int count = 0;
		if (fileExtension != null && !fileExtension.isEmpty() && !fileExtension.trim().equalsIgnoreCase("") && fileMap != null) {
			for (final String fileName : fileMap.keySet()) {
				if (fileName.toLowerCase().indexOf(FileType.XML.getExtension()) > -1) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Validates the input string to be a rsp file.
	 * 
	 * @param processingFile {@link String} is the name of rsp file
	 * @return <code> errorMessage </code> with the cause of error
	 */
	private String validateProcessingFile(final String processingFile, final String batchClassIdentifier) {
		boolean hasError = false;
		String errorMessage = WebServiceConstants.EMPTY_STRING;

		if (!(hasError)
				&& !((processingFile.toLowerCase().indexOf(FileType.RSP.getExtension().toLowerCase()) > -1) || (processingFile
						.toLowerCase().indexOf(FileType.ZON.getExtension().toLowerCase()) > -1))) {
			errorMessage = "The form processing project file should be either .rsp file or .zon file";
			hasError = true;
		}

		if (!(hasError) && !isExistingFixedFormExtractionFile(processingFile.toLowerCase(), batchClassIdentifier)) {
			errorMessage = "This RSP file does not exist";
			hasError = true;
		}
		return errorMessage;
	}

	/**
	 * Validates keyFuzziness for extractKV webservice
	 * 
	 * @param keyFuzziness {@link Float} parameter required during KV extraction
	 */
	public boolean isValidKeyFuzziness(final Float keyFuzziness) {
		boolean isValid = true;
		if (keyFuzziness != null
				&& (!(keyFuzziness == WebServiceConstants.KEY_FUZZINESS_FIRST_VALUE
						|| keyFuzziness == WebServiceConstants.KEY_FUZZINESS_SECOND_VALUE || keyFuzziness == WebServiceConstants.KEY_FUZZINESS_THIRD_VALUE))) {
			isValid = false;
		}
		return isValid;
	}

	public Batch batchXMLDecryption(HttpServletRequest httpRequest) throws InternalServerException, NoUserRoleException,
			ValidationException {
		final String webServiceFolderPath = batchSchemaService.getWebServicesFolderPath();
		org.jdom.Document document = null;
		Batch unmarshalledObject = null;
		try {
			String workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
			final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) httpRequest;
			final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
			LOGGER.info("Retreiving xml file.");
			final byte[] applicationLevelKey = encryptionKeyService.getApplicationKey();
			String xmlFileName = WebServiceConstants.EMPTY_STRING;
			xmlFileName = WebServiceHelper.getXMLFile(workingDir, multiPartRequest, fileMap);
			final File xmlFile = new File(workingDir + File.separator + xmlFileName);
			if (xmlFile.exists()) {
				LOGGER.info("Input xml file found, decrypting it.");
				final FileInputStream inputStream = new FileInputStream(xmlFile);
				// final Source source =
				// XMLUtil.createSourceFromStream(inputStream);
				if (null != applicationLevelKey) {
					document = XMLUtil.createJDOMDocumentFromInputStream(inputStream);
					unmarshalledObject = unmarshallXmlDocument(applicationLevelKey, document, httpRequest);
				} else {
					createAndThrowValidationException(null, WebServiceConstants.PARAMETER_APPLICATION_KEY_INCORRECT_CODE,
							WebServiceConstants.WRONG_APP_KEY_EXCEPTION_MESSAGE);
				}
			} else {
				createAndThrowMissingXmlException(WebServiceConstants.INPUT_XML_NOT_FOUND_CODE,
						WebServiceConstants.INPUT_XML_NOT_FOUND_MESSAGE);
			}
		} catch (NoUserRoleException noUserRoleException) {
			throw noUserRoleException;
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new InternalServerException(WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE.concat(exception.getMessage()),
					createUnprocessableEntityRestError(
							WebServiceConstants.INTERNAL_SERVER_ERROR_MESSAGE.concat(exception.getMessage()),
							WebServiceConstants.INTERNAL_SERVER_ERROR_CODE));
		}

		return unmarshalledObject;
	}

	/**
	 * Gets the user roles.
	 * 
	 * @param req the req
	 * @return the user roles
	 */
	public Set<String> getUserRoles(final HttpServletRequest req) {
		LOGGER.info("========Getting the user roles=========");

		Set<String> allGroups = userConnectivityService.getAllGroups();
		if (null == allGroups || allGroups.isEmpty()) {
			LOGGER.error("No groups fetched from Authenticated User.....All groups is empty.Returning null");
			return null;
		}

		Set<String> userGroups = new HashSet<String>();
		for (String group : allGroups) {
			if (null != group && !group.isEmpty()) {
				if (req.isUserInRole(group)) {
					LOGGER.info("Added group is: " + group);
					userGroups.add(group);
				}
			}
		}

		LOGGER.info("List of fetched user roles:");
		for (String userRole : userGroups) {
			LOGGER.info(userRole + ",");
		}

		if (userGroups.isEmpty()) {
			String userName = WebServiceUtil.EMPTY_STRING;
			if (req.getUserPrincipal() != null) {
				userName = req.getUserPrincipal().getName();
			}
			LOGGER.error("No roles found in Authenticated User for " + userName);
			userGroups = null;
		}

		return userGroups;
	}

	/**
	 * Decrypts plugin batch xml file of a batch instance folder located in report-data backup.
	 * 
	 * @param batchInstanceIdentifier {@link String} The batch instance identifier.
	 * @param moduleName {@link String} The name of plugin.
	 * @param httpRequest {@link HttpServletRequest} The request Object from the client.
	 * 
	 * @return {@link Batch} object for plugin xml file.
	 * @throws InternalServerException if xml file is not found.
	 * @throws NoUserRoleException if logged in user has not enough permissions to perform the operations.
	 * @throws ValidationException if batchInstanceIdentifier and pluginName is invalid.
	 * @throws IOException if report-data folder path is not resolved.
	 * @throws KeyNotFoundException if encryption key is not found.
	 * @throws Exception if any other error condition occurs.
	 */
	public Batch decryptReportingBatchXML(final String batchInstanceIdentifier, final String moduleName,
			final HttpServletRequest httpRequest) throws Exception, InternalServerException, NoUserRoleException, ValidationException,
			IOException, KeyNotFoundException {
		Batch unmarshalledObject = null;
		if (!EphesoftStringUtil.isNullOrEmpty(batchInstanceIdentifier)) {
			if (!EphesoftStringUtil.isNullOrEmpty(moduleName)) {
				final Properties backUpFileConfig = WebServiceUtil.fetchConfig();
				final String reportDataFolderPath = backUpFileConfig.getProperty(WebServiceConstants.REPORT_BASE_FOL_LOC);
				if (!EphesoftStringUtil.isNullOrEmpty(reportDataFolderPath)) {
					final String batchInstanceFolderPath = EphesoftStringUtil.concatenate(reportDataFolderPath, File.separator,
							batchInstanceIdentifier);
					try {
						final File batchInstanceFolder = new File(batchInstanceFolderPath);
						if (batchInstanceFolder.exists()) {
							File moduleXmlFile = null;
							final File[] fileList = batchInstanceFolder.listFiles();
							for (final File xmlFile : fileList) {
								final String fileName = xmlFile.getName();
								// Change to accomodate new naming convention of xml files created per Module in Report Data Folder
								if (xmlFile.isFile() && (fileName.toUpperCase().contains(moduleName.toUpperCase()))) {
									moduleXmlFile = xmlFile;
									break;
								}
							}
							if (null != moduleXmlFile) {
								final byte[] applicationLevelKey = this.encryptionKeyService.getApplicationKey();
								if (null != applicationLevelKey) {
									org.jdom.Document document = null;
									final String moduleFilePath = moduleXmlFile.getAbsolutePath();
									if (moduleFilePath.endsWith(com.ephesoft.dcma.util.FileType.ZIP.getExtension())) {
										final String xmlFilePath = moduleFilePath.replaceAll(
												com.ephesoft.dcma.util.FileType.ZIP.getExtensionWithDot(),
												WebServiceConstants.EMPTY_STRING);
										final InputStream inputStream = FileUtils.getInputStreamFromZip(xmlFilePath,
												EphesoftStringUtil.concatenate(batchInstanceIdentifier.toUpperCase(),
														ICommonConstants.UNDERSCORE_BATCH_XML));
										document = XMLUtil.createJDOMDocumentFromInputStream(inputStream);
									} else if (moduleFilePath.endsWith(com.ephesoft.dcma.util.FileType.XML.getExtension())) {
										document = XMLUtil.createJDOMDocumentFromFile(new File(moduleFilePath));
									} else {
										this.createAndThrowMissingXmlException(WebServiceConstants.INPUT_XML_NOT_FOUND_CODE,
												WebServiceConstants.INPUT_XML_NOT_FOUND_MESSAGE);
									}
									LOGGER.info("Plugin xml file found, decrypting it.");
									unmarshalledObject = this.unmarshallXmlDocument(applicationLevelKey, document, httpRequest);
								} else {
									this.createAndThrowValidationException(null,
											WebServiceConstants.PARAMETER_APPLICATION_KEY_INCORRECT_CODE,
											WebServiceConstants.WRONG_APP_KEY_EXCEPTION_MESSAGE);
								}
							} else {
								this.createAndThrowMissingXmlException(WebServiceConstants.INPUT_XML_NOT_FOUND_CODE,
										WebServiceConstants.INPUT_XML_NOT_FOUND_MESSAGE);
							}
						} else {
							this.createAndThrowMissingXmlException(WebServiceConstants.INPUT_XML_NOT_FOUND_CODE,
									WebServiceConstants.INPUT_XML_NOT_FOUND_MESSAGE);
						}
					} catch (final NoUserRoleException noUserRoleException) {
						throw noUserRoleException;
					}
				} else {
					this.createAndThrowValidationException(batchInstanceIdentifier, WebServiceConstants.INVALID_REPORT_DATA_PATH_CODE,
							WebServiceConstants.INVALID_REPORT_DATA_PATH_MESSAGE);
				}
			} else {
				this.createAndThrowValidationException(batchInstanceIdentifier,
						WebServiceConstants.INVALID_PLUGIN_NAME_PARAM_FOR_DECRYPTION_CODE,
						WebServiceConstants.INVALID_PLUGIN_NAME_PARAM_FOR_DECRYPTION_MESSAGE);
			}
		} else {
			this.createAndThrowValidationException(batchInstanceIdentifier,
					WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_CODE,
					WebServiceConstants.INVALID_BATCH_INSTANCE_IDENTIFIER_MESSAGE);
		}
		return unmarshalledObject;
	}

	/**
	 * Creates and throws {@link InternalServerException} with specified error code and error message.
	 * 
	 * @param code for error.
	 * @param message for error.
	 * @throws InternalServerException with specified message and error.
	 */
	private void createAndThrowMissingXmlException(final int code, final String message) throws InternalServerException {
		final RestError restError = new RestError(HttpStatus.NOT_FOUND, code, message, EphesoftStringUtil.concatenate(message,
				WebServiceConstants.CLASS_WEB_SERVICE_UTILITY), WebServiceConstants.DEFAULT_URL);
		LOGGER.error(EphesoftStringUtil.concatenate(message, code, WebServiceConstants.HTTP_STATUS, HttpStatus.NOT_FOUND));
		throw new InternalServerException(message, restError);
	}

	/**
	 * Creates and throws {@link ValidationException} with specified batch instance, error code and error message.
	 * 
	 * @param batchClassIdentifier to be logged in error message.
	 * @param code for error.
	 * @param message for error.
	 * @throws ValidationException with specified message and error.
	 */
	private void createAndThrowValidationException(final String batchClassIdentifier, final int code, final String message)
			throws ValidationException {
		if (null == batchClassIdentifier) {
			LOGGER.error(EphesoftStringUtil.concatenate(message, WebServiceConstants.ERROR_CODE_MESSAGE, code));
			throw new ValidationException(message, new RestError(HttpStatus.INTERNAL_SERVER_ERROR, code, message,
					EphesoftStringUtil.concatenate(message, WebServiceConstants.CLASS_WEB_SERVICE_UTILITY),
					WebServiceConstants.DEFAULT_URL));
		} else {
			LOGGER.error(EphesoftStringUtil.concatenate(message, code, batchClassIdentifier));
			throw new ValidationException(EphesoftStringUtil.concatenate(message, getAdditionalInfo(batchClassIdentifier)),
					new RestError(HttpStatus.UNPROCESSABLE_ENTITY, code, EphesoftStringUtil.concatenate(message,
							getAdditionalInfo(batchClassIdentifier)), EphesoftStringUtil.concatenate(message,
							getAdditionalInfo(batchClassIdentifier), WebServiceConstants.CLASS_WEB_SERVICE_UTILITY),
							WebServiceConstants.DEFAULT_URL));
		}
	}

	/**
	 * Returns unmarshalled {@link Batch} object from specified {@link InputStream} using application level key.
	 * 
	 * @param applicationLevelKey {@link String}.
	 * @param jdom document corrosponding to Plugin XML file.
	 * @param httpRequest {@link HttpServletRequest} The request Object from the client.
	 * 
	 * @return unmarshalled {@link Batch} object.
	 * @throws Exception in case of errornous condition occurs.
	 * @throws HexDecoderException in case some error occures while decoding.
	 * @throws UnsupportedEncodingException if the named charset is not supported
	 * @throws JDOMException if some error occures while parsing xml file.
	 * @throws XmlMappingException if some error occurs while unmarshalling the batch object.
	 * @throws ValidationException if input validations fails.
	 * @throws NoUserRoleException if logged in user has not enough permissions to perform the operations.
	 */
	private Batch unmarshallXmlDocument(final byte[] applicationLevelKey, final org.jdom.Document document,
			final HttpServletRequest httpRequest) throws Exception, HexDecoderException, UnsupportedEncodingException, JDOMException,
			XmlMappingException, ValidationException, NoUserRoleException {
		Batch unmarshalledBatchObject = null;
		final org.jdom.Element signature = document.getRootElement().getChild(WebServiceConstants.SIGNATURE);
		if (null != signature && !EphesoftStringUtil.isNullOrEmpty(signature.getText())) {
			final String decodedSignature = BatchEncryptionUtil.decryptXMLStringContent(signature.getText(),
					PrivateKeyEncryptionAlgorithm.AES_128, applicationLevelKey);
			// decoded signature: 1.encoded application key 2. encoded batch key
			// 3. encoded groups(hex form) 4. encryption
			// algorithm(not encoded)
			final String[] signatureparts = decodedSignature.split("#");
			final byte[] extractedAppKey = EphesoftStringUtil.decodeHexString(signatureparts[0]);
			final byte[] extractedBatchInstanceKey = EphesoftStringUtil.decodeHexString(signatureparts[1]);
			final byte[] extractedGroups = EphesoftStringUtil.decodeHexString(signatureparts[2]);
			final String groups = new String(extractedGroups, BatchConstants.UTF_8_ENCODER);
			// check if both app key equal or not.
			if (Arrays.equals(applicationLevelKey, extractedAppKey)) {
				document.getRootElement().removeChild(WebServiceConstants.SIGNATURE);
				final Set<String> rolesOfRequest = getUserRoles(httpRequest);
				final String[] listOfGropus = groups.split(",");
				boolean isPresent = false;
				if (null != listOfGropus && rolesOfRequest != null) {
					for (final String group : listOfGropus) {
						if (rolesOfRequest.contains(group)) {
							isPresent = true;
							break;
						}
					}
				}
				if (isPresent) {
					DOMEncryptionUtil.decryptDocument(document, PrivateKeyEncryptionAlgorithm.AES_128, extractedBatchInstanceKey);
					final DOMOutputter xmlOutputter = new DOMOutputter();
					final Document w3cDocument = xmlOutputter.output(document);
					final DOMSource domSource = new DOMSource(w3cDocument);
					unmarshalledBatchObject = (Batch) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller().unmarshal(domSource);
				} else {
					LOGGER.error(WebServiceConstants.NO_USER_ROLE_EXCEPTION_MESSAGE);
					throw new NoUserRoleException();
				}
			} else {
				createAndThrowValidationException(null, WebServiceConstants.PARAMETER_APPLICATION_KEY_INCORRECT_CODE,
						WebServiceConstants.WRONG_APP_KEY_EXCEPTION_MESSAGE);
			}
		} else {
			createAndThrowValidationException(null, WebServiceConstants.SIGNATURE_NOT_FOUND_ERROR_CODE,
					WebServiceConstants.SIGNATURE_NOT_FOUND_ERROR_MESSAGE);
		}
		return unmarshalledBatchObject;
	}

	/**
	 * Returns unmarshalled {@link HocrPages} object after decrypting specified HOCR xml file.
	 * 
	 * @param xmlFilePath {@link String} HOCR xml file's path.
	 * 
	 * @return unmarshalled {@link HocrPages} object.
	 * @throws Exception in case of errornous condition occurs.
	 */
	public HocrPages decryptHocrXML(final String xmlFilePath) throws Exception {
		Source source = null;
		if (EphesoftStringUtil.isNullOrEmpty(xmlFilePath)) {
			this.createAndThrowMissingXmlException(WebServiceConstants.INPUT_XML_NOT_FOUND_CODE,
					WebServiceConstants.INPUT_XML_NOT_FOUND_MESSAGE);
		} else {
			final File xmlFile = new File(xmlFilePath);
			if (xmlFile.exists() && xmlFile.isFile()) {
				LOGGER.info(EphesoftStringUtil.concatenate("Path of HOCR file to decrypt is : ", xmlFilePath));
				final InputStream inputStream = new FileInputStream(xmlFile);
				source = XMLUtil.createSourceFromStream(inputStream, xmlFile);
			} else {
				this.createAndThrowMissingXmlException(WebServiceConstants.INPUT_XML_NOT_FOUND_CODE,
						WebServiceConstants.INPUT_XML_NOT_FOUND_MESSAGE);
			}
		}
		return (HocrPages) cryptoMarshaller.unmarshal(source);
	}

	/**
	 * Validates user for specified batchInstanceIdentifier.
	 * 
	 * @param batchInstanceIdentifier
	 * @param httpRequest
	 * @return true if user has permissions to perform operations on specified batchInstanceIdentifier.
	 */
	public boolean validateUserForBatchInstance(final String batchInstanceIdentifier, final HttpServletRequest httpRequest) {
		boolean isValidUser = false;
		if (!EphesoftStringUtil.isNullOrEmpty(batchInstanceIdentifier) && null != httpRequest) {
			final Set<String> rolesOfRequest = getUserRoles(httpRequest);
			Set<String> batchInstanceIdentifiers = batchInstanceGroupsService.getBatchInstanceIdentifierForUserRoles(rolesOfRequest);
			if (batchInstanceIdentifiers.contains(batchInstanceIdentifier)) {
				isValidUser = true;
			}
		}
		return isValidUser;
	}

	/**
	 * Validates user for specified batchClassIdentifier.
	 * 
	 * @param batchClassIdentifier
	 * @param httpRequest
	 * @return true if user has permissions to perform operations on specified batchClassIdentifier.
	 */
	public boolean validateUserForBatchClass(final String batchClassIdentifier, final HttpServletRequest httpRequest) {
		boolean isPresent = false;
		if (!EphesoftStringUtil.isNullOrEmpty(batchClassIdentifier) && null != httpRequest) {
			final Set<String> userRoles = batchClassGroupsService.getRolesForBatchClass(batchClassIdentifier);
			final Set<String> rolesOfRequest = getUserRoles(httpRequest);
			if (null != userRoles && rolesOfRequest != null) {
				for (final String group : userRoles) {
					if (rolesOfRequest.contains(group)) {
						isPresent = true;
						break;
					}
				}
			}
		}
		return isPresent;
	}

	/**
	 * performs table extraction on the HOCR file provided ,accepts input xml with HOCR page and document type classification along
	 * with HOCR pages on whuich extraction is to be performed
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws ValidationException
	 * @throws InternalServerException
	 */
	public Batch.Documents processtableExtractionHOCR(final HttpServletRequest req, final HttpServletResponse resp)
			throws ValidationException, InternalServerException {
		LOGGER.info("Start processing web service for table extraction....");
		String respStr = WebServiceUtil.EMPTY_STRING;
		String xmlFileName = WebServiceUtil.EMPTY_STRING;
		String workingDir = WebServiceUtil.EMPTY_STRING;
		Batch.Documents documents = null;
		InputStream instream = null;
		int responseCode = 0;
		if (req instanceof DefaultMultipartHttpServletRequest) {
			final String webServiceFolderPath = bsService.getWebServicesFolderPath();
			try {
				workingDir = WebServiceUtil.createWebServiceWorkingDir(webServiceFolderPath);
				final DefaultMultipartHttpServletRequest multiPartRequest = (DefaultMultipartHttpServletRequest) req;
				final MultiValueMap<String, MultipartFile> fileMap = multiPartRequest.getMultiFileMap();
				boolean isFileHOCR = false;
				boolean isFilePlainXML = false;
				if (fileMap.size() >= WebServiceConstants.MINIMUM_FILE_COUNT_FOR_TABLE_EXTRACTION) {
					for (final String inputFileName : fileMap.keySet()) {
						try {
							isFileHOCR = inputFileName.trim().endsWith(WebServiceConstants.HOCR_EXTENSION);
							isFilePlainXML = inputFileName.toLowerCase().endsWith(FileType.XML.getExtensionWithDot());
							if (!(isFileHOCR || isFilePlainXML)) {
								respStr = WebServiceConstants.INVALID_FILES_TABLE_EXTRACTION;
								responseCode = WebServiceConstants.INVALID_PARAMETERS_CODE;
								createAndThrowValidationException(null, responseCode, respStr);
							}
							if (!isFileHOCR) {
								xmlFileName = inputFileName;
							}
							final MultipartFile multiPartFile = multiPartRequest.getFile(inputFileName);
							instream = multiPartFile.getInputStream();
							WebServiceUtil.copyFile(workingDir, inputFileName, instream);
						} catch (FileNotFoundException fileNotFoundException) {
							respStr = WebServiceConstants.ERROR;
							responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
						}
					}
					LOGGER.info("XML file name is" + xmlFileName);
					final File xmlFile = new File(EphesoftStringUtil.concatenate(workingDir, File.separator, xmlFileName));
					final FileInputStream inputStream = new FileInputStream(xmlFile);
					final Source source = XMLUtil.createSourceFromStream(inputStream);
					String response = WebServiceUtil.EMPTY_STRING;
					ExtractTableParam extractTableParams = (ExtractTableParam) batchSchemaDao.getJAXB2Template().getJaxb2Marshaller()
							.unmarshal(source);
					List<com.ephesoft.dcma.batch.schema.ExtractTableParam.Documents.Document> docList = extractTableParams
							.getDocuments().getDocument();
					final String batchClassIdentifier = extractTableParams.getBatchClassId();
					Map<DocumentType, List<HocrPages>> documentHOCRMap = new HashMap<DocumentType, List<HocrPages>>();
					if (EphesoftStringUtil.isNullOrEmpty(batchClassIdentifier)) {
						responseCode = WebServiceConstants.INVALID_PARAMETERS_CODE;
						respStr = WebServiceConstants.UNDEFINED_BATCH_IDENTIFIER;
						createAndThrowValidationException(null, responseCode, respStr);
					}
					final BatchClass batchClass = bcService.getBatchClassByIdentifier(batchClassIdentifier);
					if (null == batchClass) {
						response = WebServiceUtil.BATCH_NOT_EXISTS;
					}
					if (EphesoftStringUtil.isNullOrEmpty(response)) {
						String tableExtractionSwitch = WebServiceConstants.EMPTY_STRING;
						try {
							tableExtractionSwitch = batchClassPPService.getPropertyValue(batchClassIdentifier,
									WebServiceConstants.TABLE_EXTRACTION_PLUGIN, TableExtractionProperties.TABLE_EXTRACTION_SWITCH);
						} catch (NullPointerException nullPointerException) {
							responseCode = WebServiceConstants.INVALID_PARAMETERS_CODE;
							respStr = EphesoftStringUtil.concatenate(WebServiceConstants.UNDEFINED_TABLE_EXTRACTION_SWITCH,
									batchClassIdentifier);
							createAndThrowConfigurationException(responseCode, respStr);
						}
						if (WebServiceUtil.OFF_STRING.equals(tableExtractionSwitch)) {
							respStr = EphesoftStringUtil.concatenate(WebServiceConstants.TABLE_EXTRACCTION_SWITCH_OFF_MESSAGE,
									batchClassIdentifier);
							responseCode = WebServiceConstants.TABLE_EXTRACTION_SWITCH_OFF_CODE;
							createAndThrowConfigurationException(responseCode, respStr);
						}
						final List<DocumentType> docTypeList = batchClass.getDocumentTypes();
						List<String> docTypesName = obtainDocumentNameList(docTypeList);
						documentHOCRMap = generateDocumentMapHOCR(docList, workingDir, docTypesName, batchClassIdentifier);
						if (documentHOCRMap.isEmpty()) {
							respStr = WebServiceConstants.INVALID_MAPPING_DOCUMENT_HOCR_PAGES;
							responseCode = WebServiceConstants.INVALID_PARAMETERS_CODE;
							createAndThrowValidationException(null, responseCode, respStr);
						}
						final int gapBetweenColumnWords = tableFinderService.getGapBetweenColumnWords();
						documents = tableExtraction.processDocPageForTableExtractionWebService(gapBetweenColumnWords, documentHOCRMap,
								docTypeList, docTypesName);
					} else {
						respStr = response;
						responseCode = WebServiceConstants.INVALID_PARAMETERS_CODE;
						createAndThrowValidationException(null, responseCode, respStr);
					}

				} else {
					respStr = WebServiceConstants.TABLE_EXTRACTION_MINIMUM_PARAMETERS_REQUIRED_ERROR_MESSAGE;
					responseCode = WebServiceConstants.INVALID_PARAMETERS_CODE;
					createAndThrowValidationException(null, responseCode, respStr);
				}
			} catch (ClassCastException classCastException) {
				LOGGER.error(EphesoftStringUtil.concatenate("Not an Object of extract table Params", classCastException.getMessage()));
				respStr = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_MESSAGE;
				responseCode = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_CODE;
			} catch (DCMABusinessException dcmaBusinessException) {
				LOGGER.error(EphesoftStringUtil.concatenate("Invalid HOCR xml file uploaded", dcmaBusinessException.getMessage()));
				respStr = WebServiceConstants.INVALID_HOCR_FILE_UPLOAD_MESSAGE;
				responseCode = WebServiceConstants.INVALID_HOCR_FILE_UPLOADED_CODE;
			} catch (org.springframework.oxm.UnmarshallingFailureException unmarshallingFailureException) {
				LOGGER.error(EphesoftStringUtil.concatenate("Not an Object of extract table Params",
						unmarshallingFailureException.getMessage()));
				respStr = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_MESSAGE;
				responseCode = WebServiceConstants.INVALID_ARGUMENTS_IN_XML_INPUT_CODE;
			} catch (Exception exception) {
				LOGGER.error(EphesoftStringUtil.concatenate("Error generated is ", exception.getMessage()));
				if (EphesoftStringUtil.isNullOrEmpty(respStr)) {
					respStr = WebServiceConstants.INVALID_MULTIPART_REQUEST;
					responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
				}
			} finally {
				if (!workingDir.isEmpty()) {
					FileUtils.deleteDirectoryAndContentsRecursive(new File(workingDir).getParentFile());
				}
			}
		} else {
			if (EphesoftStringUtil.isNullOrEmpty(respStr)) {
				respStr = WebServiceConstants.INVALID_MULTIPART_REQUEST;
				responseCode = WebServiceConstants.INTERNAL_SERVER_ERROR_CODE;
			}
		}
		validateResponse(responseCode, respStr);
		return documents;
	}

	/**
	 * returns a map with document type as key and list of HOCR pages as value
	 * 
	 * @param inputDocList list of documents in the input xml
	 * @param workingDir working directory
	 * @return map with document type and a list of HOCR belonging to that document
	 */
	private Map<DocumentType, List<HocrPages>> generateDocumentMapHOCR(
			List<com.ephesoft.dcma.batch.schema.ExtractTableParam.Documents.Document> inputDocList, String workingDir,
			List<String> docTypesName, String batchClassIdentifier) {
		Map<DocumentType, List<HocrPages>> documentHOCRMap = new HashMap<DocumentType, List<HocrPages>>();
		for (com.ephesoft.dcma.batch.schema.ExtractTableParam.Documents.Document inputDocument : inputDocList) {
			if (null != inputDocument.getName()) {
				String inputDocumentName = inputDocument.getName().trim();
				if (!EphesoftStringUtil.isNullOrEmpty(inputDocumentName)) {
					if (docTypesName.contains(inputDocumentName)) {
						List<HocrPages> documentHOCRPages = new ArrayList<HocrPages>();
						List<String> pageList = inputDocument.getPage();
						if (null == pageList || pageList.size() == 0) {
							continue;
						} else {
							for (String page : pageList) {
								if (!EphesoftStringUtil.isNullOrEmpty(page)) {
									String filePath = EphesoftStringUtil.concatenate(workingDir, File.separator, page);
									File file = new File(filePath);
									if (!file.exists()) {
										continue;
									}
									final HocrPages hocrPages = bsService.getHOCR(filePath);
									if (null != hocrPages) {
										documentHOCRPages.add(hocrPages);
									}
								}
							}
						}
						if (!documentHOCRPages.isEmpty()) {
							DocumentType documentType = docTypeService.getDocTypeByBatchClassAndDocTypeName(batchClassIdentifier,
									inputDocumentName);
							documentHOCRMap.put(documentType, documentHOCRPages);
						}
					}
				}
			}
		}
		return documentHOCRMap;
	}

	/**
	 * returns a list of document names present in the batch class
	 * 
	 * @param docTypeList list of document type
	 * @return list of document names
	 */
	private List<String> obtainDocumentNameList(List<DocumentType> docTypeList) {
		List<String> docTypesName = new ArrayList<String>();
		for (DocumentType doctype : docTypeList) {
			docTypesName.add(doctype.getName());
		}
		return docTypesName;
	}

	/**
	 * throws a configuration exception
	 * 
	 * @param code error code
	 * @param message error message
	 * @throws ConfigurationException
	 */
	private void createAndThrowConfigurationException(final int code, final String message) throws ConfigurationException {
		throw new ConfigurationException(message, new RestError(HttpStatus.INTERNAL_SERVER_ERROR, code, message,
				EphesoftStringUtil.concatenate(message, WebServiceConstants.CLASS_WEB_SERVICE_UTILITY),
				WebServiceConstants.DEFAULT_URL));
	}

	/**
	 * Method to delete batch instance folder from ephesoft-system-folder
	 * 
	 * @param {@link BatchInstance}
	 */
	private void deleteBatchFolder(final BatchInstance batchInstance) {
		LOGGER.info("Deleting batch instance folder from ephesoft system folder");
		final File batchInstanceFolder = new File(EphesoftStringUtil.concatenate(batchInstance.getLocalFolder(), File.separator,
				batchInstance.getIdentifier()));
		if (batchInstanceFolder.exists()) {
			FileUtils.deleteDirectoryAndContentsRecursive(batchInstanceFolder);
		}
	}

	/**
	 * Method to delete .ser file from properties folder of ephesoft-system-folder
	 * 
	 * @param {@link BatchInstance}
	 */
	private void deleteSerFile(final BatchInstance batchInstance) {
		LOGGER.info("Deleting .ser file from properties folder");
		final File batchInstanceFolder = new File(EphesoftStringUtil.concatenate(batchInstance.getLocalFolder(), File.separator,
				WebServiceConstants.PROPERTIES_FOLDER, File.separator, batchInstance.getIdentifier(), WebServiceConstants.SER_FILE));
		if (batchInstanceFolder.exists()) {
			batchInstanceFolder.delete();
		}

	}

}
