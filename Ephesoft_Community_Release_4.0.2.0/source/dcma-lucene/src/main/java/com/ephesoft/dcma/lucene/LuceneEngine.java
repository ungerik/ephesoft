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

package com.ephesoft.dcma.lucene;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.demo.IndexHTML;
import org.apache.lucene.demo.SearchFiles;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similar.MoreLikeThis;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPlugin;
import com.ephesoft.dcma.batch.dao.impl.BatchPluginPropertyContainer.BatchPluginConfiguration;
import com.ephesoft.dcma.batch.encryption.service.EncryptionKeyService;
import com.ephesoft.dcma.batch.encryption.util.XMLKeyFinder;
import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Page.PageLevelFields;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.batch.util.BatchEncryptionUtil;
import com.ephesoft.dcma.core.DCMAException;
import com.ephesoft.dcma.core.TesseractVersionProperty;
import com.ephesoft.dcma.core.common.CategoryType;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.common.WidgetType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.core.threadpool.BatchInstanceThread;
import com.ephesoft.dcma.da.dao.BatchInstanceDao;
import com.ephesoft.dcma.da.id.BatchClassID;
import com.ephesoft.dcma.da.service.BatchClassPluginConfigService;
import com.ephesoft.dcma.da.service.PageTypeService;
import com.ephesoft.dcma.ocr.OCRFactoryClass;
import com.ephesoft.dcma.tesseract.TesseractProperties;
import com.ephesoft.dcma.tesseract.service.TesseractService;
import com.ephesoft.dcma.util.ApplicationConfigProperties;
import com.ephesoft.dcma.util.CustomFileFilter;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.FileUtils;
import com.ephesoft.dcma.util.ImageDetails;
import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;
import com.ephesoft.dcma.util.exception.KeyGenerationException;
import com.ephesoft.dcma.util.exception.KeyNotFoundException;

/**
 * This class generate the confidence score by using Lucene search engine.It first creates the indexes for a well defined set of input
 * HOCR files and then finds first a maximum of 5 similar documents in the standard indexed set based on confidence score. It stores
 * the first in page level field and others as alternate values inside batch xml.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.core.component.ICommonConstants
 * 
 */
@Component
public class LuceneEngine implements ICommonConstants {

	private static final String EXCEPTION_GENERATING_HOCR_FOR_IMAGE = "Exception while generating HOCR for image";
	private static final String TESSERACT_HOCR_PLUGIN = "TESSERACT_HOCR";
	private static final String TESSERACT_BASE_PATH_NOT_CONFIGURED = "Tesseract Base path not configured.";
	private static final String HOCR_XML = "_HOCR.xml";
	/**
	 * Constant for file type name.
	 */
	public static final String FIELD_TYPE_NAME = "Search_Engine_Classification";
	/**
	 * Constant for switch
	 */
	private static final String ON_VALUE = "ON";
	/**
	 * Constant for empty string.
	 */
	private static final String EMPTY_STRING = "";

	/**
	 * Instance of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Instance of TesseractService.
	 */
	@Autowired
	private TesseractService tesseractService;

	/**
	 * Instance of BatchInstanceDao.
	 */
	@Autowired
	private BatchInstanceDao batchInstanceDao;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * Instance of BatchClassPluginPropertiesService.
	 */
	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService batchClassPluginPropertiesService;

	/**
	 * Instance of PluginPropertiesService.
	 */
	@Autowired
	private BatchClassPluginConfigService batchClassPluginConfigService;

	/**
	 * Instance of PageTypeService.
	 */
	@Autowired
	private PageTypeService pageTypeService;

	/**
	 * Service to get the appropriate encryption/decryption key for Batch Class and the Batch Instance.
	 */
	@Autowired
	private EncryptionKeyService encryptionKeyService;

	/**
	 * Service that provides access to the appropriate key for the Learn folder.
	 */
	@Autowired
	private XMLKeyFinder xmlKeyFinder;
	@Autowired
	private OCRFactoryClass ocrFactoryClass;

	/**
	 * Logger instance for logging using slf4j for logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LuceneEngine.class);

	/**
	 * First page.
	 */
	private transient String firstPage;
	/**
	 * Middle page.
	 */
	private transient String middlePage;
	/**
	 * Last page.
	 */
	private transient String lastPage;

	/**
	 * @param firstPage the firstPage to set
	 */
	public void setFirstPage(final String firstPage) {
		this.firstPage = firstPage;
	}

	/**
	 * @param middlePage the middlePage to set
	 */
	public void setMiddlePage(final String middlePage) {
		this.middlePage = middlePage;
	}

	/**
	 * @param lastPage the lastPage to set
	 */
	public void setLastPage(final String lastPage) {
		this.lastPage = lastPage;
	}

	/**
	 * @return the batchSchemaService
	 */
	public BatchSchemaService getBatchSchemaService() {
		return batchSchemaService;
	}

	/**
	 * @param batchSchemaService the batchSchemaService to set
	 */
	public void setBatchSchemaService(final BatchSchemaService batchSchemaService) {
		this.batchSchemaService = batchSchemaService;
	}

	/**
	 * @return the pageTypeService
	 */
	public PageTypeService getPageTypeService() {
		return pageTypeService;
	}

	/**
	 * @param pageTypeService the pageTypeService to set
	 */
	public void setPageTypeService(final PageTypeService pageTypeService) {
		this.pageTypeService = pageTypeService;
	}

	/**
	 * @return the batchClassPluginConfigService
	 */
	public BatchClassPluginConfigService getBatchClassPluginConfigService() {
		return batchClassPluginConfigService;
	}

	/**
	 * @param batchClassPluginConfigService the batchClassPluginConfigService to set
	 */
	public void setBatchClassPluginConfigService(final BatchClassPluginConfigService batchClassPluginConfigService) {
		this.batchClassPluginConfigService = batchClassPluginConfigService;
	}

	/**
	 * @return the batchInstanceDao
	 */
	public BatchInstanceDao getBatchInstanceDao() {
		return batchInstanceDao;
	}

	/**
	 * @param batchInstanceDao the batchInstanceDao to set
	 */
	public void setBatchInstanceDao(final BatchInstanceDao batchInstanceDao) {
		this.batchInstanceDao = batchInstanceDao;
	}

	/**
	 * This method generates the confidence score for each HOCR file read from batch xml and stores maximum of 5 best fir pages inbatch
	 * xml. For comparison it refers to the already created indexes of standard HOCR pages stored in hierarchy : batch class > document
	 * type > page type.
	 * 
	 * @param batchInstanceID String
	 * @throws DCMAApplicationException
	 * @throws DCMABusinessException
	 */
	public void generateConfidence(final String batchInstanceID) throws DCMAApplicationException {
		// Initialize properties
		BatchPlugin batchPlugin = pluginPropertiesService.getPluginProperties(batchInstanceID, SEARCH_CLASSIFICATION_PLUGIN);
		String switchValue = pluginPropertiesService.getPropertyValue(batchInstanceID, SEARCH_CLASSIFICATION_PLUGIN,
				LuceneProperties.LUCENE_SWITCH);
		if (switchValue.equalsIgnoreCase(ON_VALUE)) {
			LOGGER.info("Initializing properties...");
			String validExt = batchPlugin.getPluginConfigurationValue(LuceneProperties.LUCENE_VALID_EXTNS);
			validExt = validExt + ";xml";
			String indexFields = batchPlugin.getPluginConfigurationValue(LuceneProperties.LUCENE_INDEX_FIELDS);
			String stopWords = batchPlugin.getPluginConfigurationValue(LuceneProperties.LUCENE_STOP_WORDS);
			String minTermFreq = batchPlugin.getPluginConfigurationValue(LuceneProperties.LUCENE_MIN_TERM_FREQ);
			String minDocFreq = batchPlugin.getPluginConfigurationValue(LuceneProperties.LUCENE_MIN_DOC_FREQ);
			String minWordLength = batchPlugin.getPluginConfigurationValue(LuceneProperties.LUCENE_MIN_WORD_LENGTH);
			String maxQueryTerms = batchPlugin.getPluginConfigurationValue(LuceneProperties.LUCENE_MAX_QUERY_TERMS);
			String topLevelField = batchPlugin.getPluginConfigurationValue(LuceneProperties.LUCENE_TOP_LEVEL_FIELD);
			String numOfPages = batchPlugin.getPluginConfigurationValue(LuceneProperties.LUCENE_NO_OF_PAGES);
			String max_results = batchPlugin.getPluginConfigurationValue(LuceneProperties.LUCENE_MAX_RESULT_COUNT);

			String first_page_conf_weightage = batchPlugin
					.getPluginConfigurationValue(LuceneProperties.LUCENE_FIRST_PAGE_CONF_WEIGHTAGE);

			String middle_page_conf_weightage = batchPlugin
					.getPluginConfigurationValue(LuceneProperties.LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE);

			String last_page_conf_weightage = batchPlugin
					.getPluginConfigurationValue(LuceneProperties.LUCENE_LAST_PAGE_CONF_WEIGHTAGE);

			int max_result = 10;
			try {
				max_result = Integer.valueOf(max_results);
			} catch (NumberFormatException e) {
				LOGGER.info("Could not set max result value. Using default value of 10.");
			}
			Float f_100 = 100.0f;
			float first_page_conf_weightage_float = 1f;
			try {
				first_page_conf_weightage_float = Integer.valueOf(first_page_conf_weightage) / f_100;
			} catch (NumberFormatException e) {
				LOGGER.info("Could not set max first page confidence weightage value. Using default value of 100.");
			}

			float middle_page_conf_weightage_float = 0.90f;
			try {
				middle_page_conf_weightage_float = Integer.valueOf(middle_page_conf_weightage) / f_100;
			} catch (NumberFormatException e) {
				LOGGER.info("Could not set max middle page confidence weightage value. Using default value of 90.");
			}

			float last_page_conf_weightage_float = 0.80f;
			try {
				last_page_conf_weightage_float = Integer.valueOf(last_page_conf_weightage) / f_100;
			} catch (NumberFormatException e) {
				LOGGER.info("Could not set max last page confidence weightage value. Using default value of 80.");
			}

			LOGGER.info("Properties Initialized Successfully");

			Batch batch = batchSchemaService.getBatch(batchInstanceID);
			String batchClassIdentifier = batch.getBatchClassIdentifier();
			String indexFolder = batchSchemaService.getSearchClassIndexFolder(batchClassIdentifier, false);

			String[] validExtensions = validExt.split(";");
			String[] allIndexFields = indexFields.split(";");
			String[] allStopWords = stopWords.split(";");
			if (allIndexFields == null || allIndexFields.length <= 0) {
				LOGGER.error("Cannot read Index level fields from resources");
				throw new DCMAApplicationException("Cannot read Index level fields from resources");
			}
			String actualFolderLocation = batchSchemaService.getLocalFolderLocation() + File.separator + batchInstanceID;
			IndexReader reader = null;
			List<Page> allPages = null;
			Query query = null;
			Map<String, ImageDetails> returnMap = new HashMap<String, ImageDetails>();
			List<Document> xmlDocuments = batch.getDocuments().getDocument();

			try {
				allPages = findAllHocrFromXML(xmlDocuments);
			} catch (DCMAApplicationException e1) {
				LOGGER.error("Exception while reading from XML" + e1.getMessage());
				throw new DCMAApplicationException(e1.getMessage(), e1);
			}
			// List<com.ephesoft.dcma.da.domain.PageType> allPageTypes =
			// pageTypeService.getPageTypesByBatchInstanceID(batchInstanceID);
			List<com.ephesoft.dcma.da.domain.PageType> allPageTypes = pluginPropertiesService.getPageTypes(batchInstanceID);
			if (!(allPageTypes != null && !allPageTypes.isEmpty())) {
				LOGGER.error("Page Types not configured in Database");
				throw new DCMAApplicationException("Page Types not configured in Database");
			}

			String[] indexFiles = new File(indexFolder).list();
			if (indexFiles == null || indexFiles.length <= 0) {
				LOGGER.info("No index files exist inside folder : " + indexFolder);
				throw new DCMAApplicationException("No index files exist inside folder : " + indexFolder);
			}

			try {
				reader = IndexReader.open(FSDirectory.open(new File(indexFolder)), true);
			} catch (CorruptIndexException e) {
				LOGGER.error("CorruptIndexException while reading Index" + e.getMessage(), e);
				throw new DCMAApplicationException("CorruptIndexException while reading Index" + e.getMessage(), e);
			} catch (IOException e) {
				LOGGER.error("IOException while reading Index" + e.getMessage(), e);
				throw new DCMAApplicationException("IOException while reading Index" + e.getMessage(), e);
			}
			MoreLikeThis moreLikeThis = new MoreLikeThis(reader);
			moreLikeThis.setFieldNames(allIndexFields);
			// moreLikeThis.setBoost(true);
			// moreLikeThis.setBoostFactor(10.0f);
			moreLikeThis.setMinTermFreq(Integer.valueOf(minTermFreq));
			moreLikeThis.setMinDocFreq(Integer.valueOf(minDocFreq));
			moreLikeThis.setMinWordLen(Integer.valueOf(minWordLength));
			moreLikeThis.setMaxQueryTerms(Integer.valueOf(maxQueryTerms));
			if (allStopWords != null && allStopWords.length > 0) {
				Set<String> stopWordsTemp = new HashSet<String>();
				for (String allStopWord : allStopWords) {
					stopWordsTemp.add(allStopWord);
				}
				moreLikeThis.setStopWords(stopWordsTemp);
			}

			if (null != allPages) {
				for (Page page : allPages) {
					returnMap.clear();
					String eachPage = page.getHocrFileName();

					eachPage = eachPage.trim();
					boolean isFileValid = false;
					if (validExtensions != null && validExtensions.length > 0) {
						for (String validExtension : validExtensions) {
							if (eachPage.toLowerCase().endsWith(validExtension)) {
								isFileValid = true;
								break;
							}
						}
					} else {
						LOGGER.error("No valid extensions are specified in resources");
						cleanUpResource(reader);
						throw new DCMAApplicationException("No valid extensions are specified in resources");
					}
					if (isFileValid) {
						try {
							LOGGER.info("Generating query for : " + eachPage);
							String hocrContent = null;

							HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceID, page.getHocrFileName());
							List<HocrPage> hocrPageList = hocrPages.getHocrPage();
							HocrPage hocrPage = hocrPageList.get(0);
							hocrContent = hocrPage.getHocrContent();
							PrivateKeyEncryptionAlgorithm encryptionAlgorithm = encryptionKeyService
									.getBatchClassEncryptionAlgorithm(batchClassIdentifier);
							byte[] luceneKey = encryptionKeyService.getLuceneIndexKey(batchClassIdentifier);
							hocrContent = BatchEncryptionUtil.encryptXMLStringContent(hocrContent, encryptionAlgorithm, luceneKey);
							if (null != hocrContent) {
								try {
									InputStream inputStream = new ByteArrayInputStream(hocrContent.getBytes("UTF-8"));
									query = moreLikeThis.like(inputStream);
								} catch (UnsupportedEncodingException e) {
									LOGGER.error(e.getMessage(), e);
									query = moreLikeThis.like(new File(actualFolderLocation + File.separator + eachPage));
								}
							} else {
								query = moreLikeThis.like(new File(actualFolderLocation + File.separator + eachPage));
							}

							if (query != null && query.toString() != null && query.toString().length() > 0) {
								LOGGER.info("Generating confidence score for : " + eachPage);
								returnMap = SearchFiles.generateConfidence(indexFolder, query.toString(), topLevelField,
										Integer.valueOf(numOfPages), max_result);
							} else {
								LOGGER.info("Empty query generated for : " + eachPage);
							}

						} catch (IOException e) {
							LOGGER.error("Exception while generating query for :" + eachPage + e.getMessage(), e);
							cleanUpResource(reader);
							throw new DCMAApplicationException("Exception while generating query for :" + eachPage + e.getMessage(), e);
						} catch (Exception e) {
							cleanUpResource(reader);
							LOGGER.error("Exception while generating index or updating xml" + e.getMessage(), e);
						}
						LOGGER.info("Started updating batch xml for : " + eachPage);
						updateBatchXML(returnMap, xmlDocuments, eachPage, allPageTypes, first_page_conf_weightage_float,
								middle_page_conf_weightage_float, last_page_conf_weightage_float);
						LOGGER.info("Successfully ended updating batch xml for : " + eachPage);
					} else {
						LOGGER.error("File " + eachPage + " has invalid extension.");
						throw new DCMABusinessException("File " + eachPage + " has invalid extension.");
					}
				}
			} else {
				LOGGER.error("No pages found in batch XML.");
				throw new DCMAApplicationException("No pages found in batch XML.");
			}
			batchSchemaService.updateBatch(batch);
			LOGGER.info("Batch XML updated.");
			cleanUpResource(reader);
		} else {
			LOGGER.info("Skipping search classification. Switch mode set as OFF.");
		}
	}

	/**
	 * This method generates the confidence score for each HOCR file read from batch xml and stores maximum of 5 best fir pages inbatch
	 * xml. For comparison it refers to the already created indexes of standard HOCR pages stored in hierarchy : batch class > document
	 * type > page type.
	 * 
	 * @param batchInstanceID String
	 * @throws DCMAApplicationException
	 * @throws DCMABusinessException
	 */
	public void generateConfidenceAPI(final List<Document> xmlDocuments, final HocrPages hocrPages, final String workingDir,
			final Map<LuceneProperties, String> propertyMap, final String batchClassIdentifier) throws DCMAApplicationException {
		// Initialize properties
		LOGGER.info("Initializing properties...");
		String validExt = propertyMap.get(LuceneProperties.LUCENE_VALID_EXTNS);
		String indexFields = propertyMap.get(LuceneProperties.LUCENE_INDEX_FIELDS);
		String stopWords = propertyMap.get(LuceneProperties.LUCENE_STOP_WORDS);
		String minTermFreq = propertyMap.get(LuceneProperties.LUCENE_MIN_TERM_FREQ);
		String minDocFreq = propertyMap.get(LuceneProperties.LUCENE_MIN_DOC_FREQ);
		String minWordLength = propertyMap.get(LuceneProperties.LUCENE_MIN_WORD_LENGTH);
		String maxQueryTerms = propertyMap.get(LuceneProperties.LUCENE_MAX_QUERY_TERMS);
		String topLevelField = propertyMap.get(LuceneProperties.LUCENE_TOP_LEVEL_FIELD);
		String numOfPages = propertyMap.get(LuceneProperties.LUCENE_NO_OF_PAGES);
		String max_results = propertyMap.get(LuceneProperties.LUCENE_MAX_RESULT_COUNT);
		String first_page_conf_weightage = propertyMap.get(LuceneProperties.LUCENE_FIRST_PAGE_CONF_WEIGHTAGE);
		String middle_page_conf_weightage = propertyMap.get(LuceneProperties.LUCENE_MIDDLE_PAGE_CONF_WEIGHTAGE);
		String last_page_conf_weightage = propertyMap.get(LuceneProperties.LUCENE_LAST_PAGE_CONF_WEIGHTAGE);

		int max_result = 10;
		try {
			max_result = Integer.valueOf(max_results);
		} catch (NumberFormatException e) {
			LOGGER.info("Could not set max result value. Using default value of 10.");
		}
		Float f_100 = 100.0f;
		float first_page_conf_weightage_float = 1f;
		try {
			first_page_conf_weightage_float = Integer.valueOf(first_page_conf_weightage) / f_100;
		} catch (NumberFormatException e) {
			LOGGER.info("Could not set max first page confidence weightage value. Using default value of 100.");
		}

		float middle_page_conf_weightage_float = 0.90f;
		try {
			middle_page_conf_weightage_float = Integer.valueOf(middle_page_conf_weightage) / f_100;
		} catch (NumberFormatException e) {
			LOGGER.info("Could not set max middle page confidence weightage value. Using default value of 90.");
		}

		float last_page_conf_weightage_float = 0.80f;
		try {
			last_page_conf_weightage_float = Integer.valueOf(last_page_conf_weightage) / f_100;
		} catch (NumberFormatException e) {
			LOGGER.info("Could not set max last page confidence weightage value. Using default value of 80.");
		}

		LOGGER.info("Properties Initialized Successfully");
		String indexFolder = batchSchemaService.getSearchClassIndexFolder(batchClassIdentifier, false);
		if (indexFolder == null) {
			LOGGER.info("No learn folder exist: " + indexFolder);
			throw new DCMAApplicationException("No learn folder exist : " + indexFolder);
		}
		String[] validExtensions = validExt.split(";");
		String[] allIndexFields = indexFields.split(";");
		String[] allStopWords = stopWords.split(";");
		if (allIndexFields == null || allIndexFields.length <= 0) {
			LOGGER.error("Cannot read Index level fields from resources");
			throw new DCMAApplicationException("Cannot read Index level fields from resources");
		}
		IndexReader reader = null;
		Query query = null;
		Map<String, ImageDetails> returnMap = new HashMap<String, ImageDetails>();

		List<com.ephesoft.dcma.da.domain.PageType> allPageTypes = pageTypeService.getPageTypesByBatchClassID(batchClassIdentifier);
		if (!(allPageTypes != null && !allPageTypes.isEmpty())) {
			LOGGER.error("Page Types not configured in Database");
			throw new DCMAApplicationException("Page Types not configured in Database");
		}

		String[] indexFiles = new File(indexFolder).list();
		if (indexFiles == null || indexFiles.length <= 0) {
			LOGGER.info("No index files exist inside folder : " + indexFolder);
			throw new DCMAApplicationException("No index files exist inside folder : " + indexFolder);
		}

		try {
			reader = IndexReader.open(FSDirectory.open(new File(indexFolder)), true);
		} catch (CorruptIndexException e) {
			LOGGER.error("CorruptIndexException while reading Index" + e.getMessage(), e);
			throw new DCMAApplicationException("CorruptIndexException while reading Index" + e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("IOException while reading Index" + e.getMessage(), e);
			throw new DCMAApplicationException("IOException while reading Index" + e.getMessage(), e);
		}
		MoreLikeThis moreLikeThis = new MoreLikeThis(reader);
		moreLikeThis.setFieldNames(allIndexFields);
		// moreLikeThis.setBoost(true);
		// moreLikeThis.setBoostFactor(10.0f);
		moreLikeThis.setMinTermFreq(Integer.valueOf(minTermFreq));
		moreLikeThis.setMinDocFreq(Integer.valueOf(minDocFreq));
		moreLikeThis.setMinWordLen(Integer.valueOf(minWordLength));
		moreLikeThis.setMaxQueryTerms(Integer.valueOf(maxQueryTerms));
		if (allStopWords != null && allStopWords.length > 0) {
			Set<String> stopWordsTemp = new HashSet<String>();
			for (String allStopWord : allStopWords) {
				stopWordsTemp.add(allStopWord);
			}
			moreLikeThis.setStopWords(stopWordsTemp);
		}

		List<Page> allPages = null;
		try {
			allPages = findAllHocrFromXML(xmlDocuments);
		} catch (DCMAApplicationException e1) {
			LOGGER.error("Exception while reading from XML" + e1.getMessage());
			throw new DCMAApplicationException(e1.getMessage(), e1);
		}

		if (null != allPages) {
			int index = 0;
			for (Page page : allPages) {
				returnMap.clear();
				String eachPage = page.getHocrFileName();

				eachPage = eachPage.trim();
				boolean isFileValid = false;
				if (validExtensions != null && validExtensions.length > 0) {
					for (String validExtension : validExtensions) {
						if (eachPage.endsWith(validExtension)) {
							isFileValid = true;
							break;
						}
					}
				} else {
					LOGGER.error("No valid extensions are specified in resources");
					cleanUpResource(reader);
					throw new DCMAApplicationException("No valid extensions are specified in resources");
				}
				if (isFileValid) {
					try {
						LOGGER.info("Generating query for : " + eachPage);
						String hocrContent = null;

						List<HocrPage> hocrPageList = hocrPages.getHocrPage();
						HocrPage hocrPage = hocrPageList.get(index);
						hocrContent = hocrPage.getHocrContent();
						byte[] luceneKey = encryptionKeyService.getLuceneIndexKey(batchClassIdentifier);
						PrivateKeyEncryptionAlgorithm encryptionAlgorithm = encryptionKeyService
								.getBatchClassEncryptionAlgorithm(batchClassIdentifier);
						hocrContent = BatchEncryptionUtil.encryptXMLStringContent(hocrContent, encryptionAlgorithm, luceneKey);

						if (null != hocrContent) {
							try {
								InputStream inputStream = new ByteArrayInputStream(hocrContent.getBytes("UTF-8"));
								query = moreLikeThis.like(inputStream);
							} catch (UnsupportedEncodingException e) {
								LOGGER.error(e.getMessage(), e);
								query = moreLikeThis.like(new File(workingDir + File.separator + eachPage));
							}
						} else {
							query = moreLikeThis.like(new File(workingDir + File.separator + eachPage));
						}

						if (query != null && query.toString() != null && query.toString().length() > 0) {
							LOGGER.info("Generating confidence score for : " + eachPage);
							returnMap = SearchFiles.generateConfidence(indexFolder, query.toString(), topLevelField,
									Integer.valueOf(numOfPages), max_result);
						} else {
							LOGGER.info("Empty query generated for : " + eachPage);
						}

					} catch (IOException e) {
						LOGGER.error("Exception while generating query for :" + eachPage + e.getMessage(), e);
						cleanUpResource(reader);
						throw new DCMAApplicationException("Exception while generating query for :" + eachPage + e.getMessage(), e);
					} catch (Exception e) {
						cleanUpResource(reader);
						LOGGER.error("Exception while generating index or updating xml" + e.getMessage(), e);
					}
					LOGGER.info("Started updating batch xml for : " + eachPage);
					updateBatchXML(returnMap, xmlDocuments, eachPage, allPageTypes, first_page_conf_weightage_float,
							middle_page_conf_weightage_float, last_page_conf_weightage_float);
					LOGGER.info("Successfully ended updating batch xml for : " + eachPage);
				} else {
					LOGGER.error("File " + eachPage + " has invalid extension.");
					throw new DCMABusinessException("File " + eachPage + " has invalid extension.");
				}
				index++;
			}
		} else {
			LOGGER.error("No pages found in batch XML.");
			throw new DCMAApplicationException("No pages found in batch XML.");
		}
		cleanUpResource(reader);
	}

	public void cleanUpResource(IndexReader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				LOGGER.info("There was a problem in closing the Index reader.");
			}
		}
	}

	/**
	 * This method finds the name of all HOCR files from batch xml which are generated by tesseract/ocropus plugin .
	 * 
	 * @param batch Batch
	 * @return List<PageType>
	 * @throws DCMAApplicationException
	 */
	public List<Page> findAllHocrFromXML(final List<Document> xmlDocuments) throws DCMAApplicationException {
		List<Page> allPages = new ArrayList<Page>();
		for (int i = 0; i < xmlDocuments.size(); i++) {
			Document document = xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			allPages.addAll(listOfPages);
		}
		return allPages;
	}

	/**
	 * This method updates the batch xml for each HOCR file and stores the confidence and value at page level field. The maximum value
	 * is stored at page level field and values for all the page types including maximum value are stored as alternate values.
	 * 
	 * @param batchDocNameScore Map<String, Float>
	 * @param batch Batch
	 * @param eachHtmlPage String
	 * @param allPageTypes List<com.ephesoft.dcma.da.domain.PageType>
	 * @param lastPageConfWeightage confidence score weightage of last page
	 * @param middlePageConfWeightage confidence score weightage of middle page
	 * @param firstPageConfWeightage confidence score weightage of first page
	 * @throws DCMAApplicationException
	 */
	public void updateBatchXML(final Map<String, ImageDetails> batchDocNameScore, final List<Document> xmlDocuments,
			final String eachHtmlPage, final List<com.ephesoft.dcma.da.domain.PageType> allPageTypes, float firstPageConfWeightage,
			float middlePageConfWeightage, float lastPageConfWeightage) throws DCMAApplicationException {
		for (int i = 0; i < xmlDocuments.size(); i++) {
			Document document = xmlDocuments.get(i);
			List<Page> listOfPages = document.getPages().getPage();
			for (int j = 0; j < listOfPages.size(); j++) {
				Page page = listOfPages.get(j);
				if (page.getHocrFileName().equalsIgnoreCase(eachHtmlPage)) {
					PageLevelFields pageLevelFields = page.getPageLevelFields();
					if (pageLevelFields == null) {
						pageLevelFields = new PageLevelFields();
					}
					List<DocField> allPageLevelFields = pageLevelFields.getPageLevelField();
					DocField docFieldType = new DocField();
					docFieldType.setConfidence(0.0f);
					// docFieldType.setCategory("Uncategorised");
					docFieldType.setCategory(CategoryType.GROUP_1.getCategoryName());
					docFieldType.setWidgetType(WidgetType.TEXT.name());
					docFieldType.setFieldOrderNumber(0);
					docFieldType.setForceReview(false);
					docFieldType.setOcrConfidence(0.0f);
					docFieldType.setOcrConfidenceThreshold(0.0f);
					AlternateValues alternateValues = docFieldType.getAlternateValues();
					if (!allPageTypes.isEmpty()) {
						docFieldType.setName(FIELD_TYPE_NAME);
						docFieldType.setCoordinatesList(null);
						docFieldType.setType(EMPTY_STRING);
						String pageType = EMPTY_STRING;
						float maxConfidenceValue = 0.0f;
						for (int k = 0; k < allPageTypes.size(); k++) {
							com.ephesoft.dcma.da.domain.PageType eachPageType = allPageTypes.get(k);
							if (eachPageType != null && batchDocNameScore.containsKey(eachPageType.getName())) {
								if (alternateValues == null) {
									alternateValues = new AlternateValues();
								}
								float eachScore = 0f;
								ImageDetails obj = batchDocNameScore.get(eachPageType.getName());
								eachScore = obj.getConfidenceValue();
								Field fieldType = new Field();
								fieldType.setForceReview(Boolean.FALSE);
								String alternateValuePageType = eachPageType.getName();
								fieldType.setValue(alternateValuePageType);

								float alternateValueConfidenceScore = calculateConfidenceScore(eachScore);
								alternateValueConfidenceScore = applyConfWeightage(alternateValueConfidenceScore,
										alternateValuePageType, firstPageConfWeightage, middlePageConfWeightage, lastPageConfWeightage);
								fieldType.setConfidence(alternateValueConfidenceScore);

								fieldType.setName(FIELD_TYPE_NAME);
								fieldType.setType(EMPTY_STRING);
								fieldType.setCoordinatesList(null);
								alternateValues.getAlternateValue().add(fieldType);
								if (maxConfidenceValue < alternateValueConfidenceScore) {
									maxConfidenceValue = alternateValueConfidenceScore;
									if (!EphesoftStringUtil.isNullOrEmpty(obj.getFileName())
											&& obj.getFileName().indexOf(HOCR_XML) != -1) {
										docFieldType.setLearnedFileName(EphesoftStringUtil.concatenate(
												obj.getFileName().substring(0, obj.getFileName().indexOf(HOCR_XML)),
												FileType.TIF.getExtensionWithDot()));
									}
									pageType = alternateValuePageType;
								}
							}
						}
						// To set the value of page type as empty if the confidence of the pages being classified is 0.
						if (maxConfidenceValue == 0) {
							docFieldType.setValue(EMPTY_STRING);
						} else {
							docFieldType.setValue(pageType);
						}
						docFieldType.setConfidence(maxConfidenceValue);
						docFieldType.setAlternateValues(alternateValues);
					}
					allPageLevelFields.add(docFieldType);
					page.setPageLevelFields(pageLevelFields);
				}
			}
		}
	}

	/**
	 * Method to apply the confidence score weightage to page level fields
	 * 
	 * @param confidenceScore
	 * 
	 * @param pageType page value that contains the string first_page, middle_page, last_page
	 * @param firstPageConfWeightage confidence score weightage of first page
	 * @param middlePageConfWeightage confidence score weightage of middle page
	 * @param lastPageConfWeightage confidence score weightage of last page
	 * @return
	 */
	private float applyConfWeightage(float confidenceScore, String pageType, float firstPageConfWeightage,
			float middlePageConfWeightage, float lastPageConfWeightage) {
		float updatedConfScore = confidenceScore;
		if (pageType != null) {
			if (pageType.contains(firstPage)) {
				updatedConfScore = confidenceScore * firstPageConfWeightage;
			} else if (pageType.contains(lastPage)) {
				updatedConfScore = confidenceScore * lastPageConfWeightage;
			} else {
				updatedConfScore = confidenceScore * middlePageConfWeightage;
			}
		}
		return updatedConfScore;
	}

	/**
	 * This method finds the highest score among all the scores inside batchDocNameScore.
	 * 
	 * @param batchDocNameScore Map<String, Float>
	 * @param allPageTypes List<com.ephesoft.dcma.da.domain.PageType>
	 * @return float
	 */
	public float fetchHighestScoreValue(final Map<String, Float> batchDocNameScore,
			final List<com.ephesoft.dcma.da.domain.PageType> allPageTypes) {
		float highestValue = 0f;
		Set<String> set = batchDocNameScore.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String learnName = iterator.next();
			float eachValue = batchDocNameScore.get(learnName);
			if (eachValue > highestValue && checkPageTypeExistence(allPageTypes, learnName)) {
				highestValue = eachValue;
			}
		}
		return highestValue;
	}

	/**
	 * This method calculates the actual confidence score to be updated in batch xml from the score returned by Lucene plugin.
	 * 
	 * @param score float
	 * @param bestScore float
	 * @return int
	 */
	public float calculateConfidenceScore(final float score) {
		float returnScore = 0;
		try {
			returnScore = 100.0f * (1.0f - 1.0f / (1.0f + score));
			DecimalFormat twoDForm = new DecimalFormat("#0.00");
			returnScore = Float.valueOf(twoDForm.format(returnScore));
		} catch (Exception e) {
			LOGGER.error("Error while converting score" + score + " to Decimal Format", e);
		}
		return returnScore;
	}

	/**
	 * This method extracts the value to be stored at page level field inside batch xml from the confidence score.
	 * 
	 * @param batchDocNameScore Map<String, Float>
	 * @param confidenceScore float
	 * @param allPageType List<com.ephesoft.dcma.da.domain.PageType>
	 * @return String
	 */
	public String fetchPageValueByConfidence(final Map<String, Float> batchDocNameScore, final float confidenceScore,
			final List<com.ephesoft.dcma.da.domain.PageType> allPageType) {
		String returnValue = EMPTY_STRING;
		if (confidenceScore != 0) {
			float learnScore = 0f;
			List<String> sameScoreDocs = new ArrayList<String>();
			Set<String> set = batchDocNameScore.keySet();
			Iterator<String> iterator = set.iterator();
			while (iterator.hasNext()) {
				String learnName = iterator.next();
				learnScore = batchDocNameScore.get(learnName);
				if (learnScore == confidenceScore && checkPageTypeExistence(allPageType, learnName)) {
					sameScoreDocs.add(learnName);
				}
			}
			if (!sameScoreDocs.isEmpty()) {
				if (sameScoreDocs.size() == 1) {
					returnValue = sameScoreDocs.get(0);
				} else if (sameScoreDocs.size() >= 1) {
					int firstIndex = -1;
					int middleIndex = -1;
					int lastIndex = -1;
					for (int i = 0; i < sameScoreDocs.size(); i++) {
						if (sameScoreDocs.get(i).contains(firstPage)) {
							firstIndex = i;
						} else if (sameScoreDocs.get(i).contains(middlePage)) {
							middleIndex = i;
						} else if (sameScoreDocs.get(i).contains(lastPage)) {
							lastIndex = i;
						}
					}
					if (firstIndex != -1) {
						returnValue = sameScoreDocs.get(firstIndex);
					} else if (lastIndex != -1) {
						returnValue = sameScoreDocs.get(lastIndex);
					} else if (middleIndex != -1) {
						returnValue = sameScoreDocs.get(middleIndex);
					}
				}
			}
		}
		return returnValue;
	}

	/**
	 * This method is used to generate index of a well defined set of HOCR files placed in a hierarchy : batch class > document type >
	 * page type.
	 * 
	 * @param batchClassIdentifier String
	 * @param createIndex boolean
	 * @throws IOException
	 */
	public void learnSampleHocrFiles(final String batchClassIdentifier, final boolean createIndex) throws DCMAApplicationException,
			IOException {
		String admLearnFolder = batchSchemaService.getSearchClassSamplePath(batchClassIdentifier, false);
		String admIndexFolder = batchSchemaService.getSearchClassIndexFolder(batchClassIdentifier, false);
		if (!(admLearnFolder.length() > 0 && admIndexFolder.length() > 0)) {
			LOGGER.error("Problem in initializing Lucene properties");
			throw new DCMAApplicationException("Problem initializing Lucene properties");
		}
		deleteIndexes(admIndexFolder);
		generateHOCRFiles(admLearnFolder, batchClassIdentifier);
		try {
			if (!(admLearnFolder.length() > 0 && admIndexFolder.length() > 0)) {
				LOGGER.error("Problem in initializing Lucene properties");
				throw new DCMAApplicationException("Problem initializing Lucene properties");
			}
			deleteIndexes(admIndexFolder);
			generateHOCRFiles(admLearnFolder, batchClassIdentifier);
			IndexHTML.generateIndex(admIndexFolder, admLearnFolder, createIndex);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
	}

	/**
	 * This method is used to generate index of a well defined set of HOCR files placed in a hierarchy : batch class > document type >
	 * page type.
	 * 
	 * @param batchClassIdentifier String
	 * @param createIndex boolean
	 */
	public void learnSampleHocrFilesForTesseract(final String batchClassIdentifier, final boolean createIndex)
			throws DCMAApplicationException {
		String admLearnFolder = batchSchemaService.getSearchClassSamplePath(batchClassIdentifier, false);
		String admIndexFolder = batchSchemaService.getSearchClassIndexFolder(batchClassIdentifier, false);
		if (!(admLearnFolder.length() > 0 && admIndexFolder.length() > 0)) {
			LOGGER.error("Problem initializing Lucene properties");
			throw new DCMAApplicationException("Problem initializing Lucene properties");
		}
		deleteIndexes(admIndexFolder);
		generateHOCRFilesFromTesseract(admLearnFolder, batchClassIdentifier);
		String batchFolderPath = admLearnFolder;
		try {
			IndexHTML.generateIndex(admIndexFolder, batchFolderPath, createIndex);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new DCMAApplicationException(e.getMessage(), e);
		}
		cleanUpIntrmediatePngs(batchFolderPath);
	}

	/**
	 * This method cleans up all the intermediate PNG files which are formed by tesseract plugin in process of creation of HOCR files.
	 * 
	 * @param batchFolderPath String
	 */
	public void cleanUpIntrmediatePngs(final String batchFolderPath) {

		cleanUpIntrmediatePngs(batchFolderPath, true);
	}

	/**
	 * 
	 */
	public void cleanUpIntrmediatePngs(final String batchFolderPath, boolean isCleanPngs) {

		if (null != batchFolderPath) {
			File batchFile = new File(batchFolderPath);
			if (batchFile.isDirectory()) {
				File[] allDocFiles = batchFile.listFiles();
				if (null != allDocFiles) {
					for (File documentFile : allDocFiles) {
						if (documentFile.isDirectory()) {
							File[] allPageFiles = documentFile.listFiles();
							if (null == allPageFiles) {
								continue;
							}
							for (File pageFile : allPageFiles) {
								if (pageFile.isDirectory()) {
									File[] allFiles = pageFile.listFiles();
									if (null == allFiles) {
										continue;
									}
									for (File file : allFiles) {
										if (file.isDirectory()) {
											continue;
										}
										if (file.getName().toLowerCase(Locale.getDefault())
												.contains(FileType.TIF.getExtensionWithDot())
												&& file.getName().toLowerCase(Locale.getDefault())
														.contains(FileType.PNG.getExtensionWithDot())) {
											file.delete();
										}
									}
								}
							}
						} else {
							if (isCleanPngs
									&& documentFile.getName().toLowerCase(Locale.getDefault())
											.contains(FileType.TIF.getExtensionWithDot())
									&& documentFile.getName().toLowerCase(Locale.getDefault())
											.contains(FileType.PNG.getExtensionWithDot())) {
								documentFile.delete();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * This method deletes the previous indexes before creating new one.
	 * 
	 * @param admIndexFolder
	 */
	public void deleteIndexes(final String admIndexFolder) {
		FileUtils.deleteContents(admIndexFolder, false);
	}

	/**
	 * This method generates the HOCR files for a certain folder, specific to a batch class
	 * 
	 * @param learnFolder String
	 * @param batchClassIdentifier String
	 * @throws DCMAApplicationException
	 * @throws IOException
	 */
	public void generateHOCRFiles(final String learnFolder, final String batchClassIdentifier) throws DCMAApplicationException,
			IOException {
		ocrFactoryClass.generateHOCRFilesForFolder(learnFolder, batchClassIdentifier);
	}

	private boolean checkHocrExists(final String hocrName, final String[] listOfFiles) {
		boolean returnValue = false;
		if (listOfFiles != null && listOfFiles.length > 0) {
			for (String eachFile : listOfFiles) {
				if (eachFile.equalsIgnoreCase(hocrName)) {
					returnValue = true;
					break;
				}
			}
		}
		return returnValue;
	}

	/**
	 * This method generates the hocr files for all the images present inside supplied folder for a paticular batch class.
	 * 
	 * @param imageFolder
	 * @param ocrEngineName
	 * @return path of output file
	 * @throws DCMAApplicationException
	 * @throws IOException
	 */
	public Map<String, String> generateHOCRForKVExtractionTest(final String imageFolder, final String ocrEngineName,
			final String batchClassIdentifer, final File testImageFile, final boolean isAdvancedKvExtraction)
			throws DCMAApplicationException, IOException {
		LOGGER.info("Inside generateHOCRForKVExtractionTest");
		Map<String, String> imageToOCRFilePathMap = new HashMap<String, String>();
		boolean isValid = true;
		LOGGER.info("Is hOCRing for advanced KV Extraction = " + isAdvancedKvExtraction);
		if (isAdvancedKvExtraction) {
			if (null == testImageFile || !testImageFile.exists()) {
				LOGGER.error("No file name specified for advanced KV Extraction");
				isValid = false;
			}
		}

		if (isValid) {
			File imageFolderPath = new File(imageFolder);
			try {
				xmlKeyFinder.generateKey(imageFolderPath);
			} catch (final KeyNotFoundException keyNotFoundException) {
				throw new DCMAApplicationException("Key's required to generate the key for learn folder is not found",
						keyNotFoundException);
			} catch (final KeyGenerationException keyGenerationException) {
				throw new DCMAApplicationException("Key Could not be generated for the learn folder", keyGenerationException);
			}
			File[] listOfimages = imageFolderPath.listFiles(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
					FileType.TIFF.getExtensionWithDot()));
			String[] listOfHOCRFiles = imageFolderPath.list(new CustomFileFilter(false, FileType.XML.getExtensionWithDot()));
			if (ocrEngineName != null && !ocrEngineName.isEmpty()) {
				String tesseractBasePath = EMPTY_STRING;
				String tesseractVersion = EMPTY_STRING;
				if (ocrEngineName.equalsIgnoreCase(TESSERACT_HOCR_PLUGIN)) {
					BatchPluginConfiguration[] pluginConfiguration = batchClassPluginPropertiesService.getPluginProperties(
							batchClassIdentifer, TESSERACT_HOCR_PLUGIN, TesseractVersionProperty.TESSERACT_VERSIONS);
					if (pluginConfiguration != null && pluginConfiguration.length > 0 && pluginConfiguration[0].getValue() != null
							&& pluginConfiguration[0].getValue().length() > 0) {
						tesseractVersion = pluginConfiguration[0].getValue();
					}
					try {
						ApplicationConfigProperties app = ApplicationConfigProperties.getApplicationConfigProperties();
						tesseractBasePath = app.getProperty(tesseractVersion);
					} catch (IOException ioe) {
						LOGGER.error(TESSERACT_BASE_PATH_NOT_CONFIGURED + ioe, ioe);
						throw new DCMAApplicationException(TESSERACT_BASE_PATH_NOT_CONFIGURED, ioe);
					}
					if (tesseractBasePath == null) {
						LOGGER.error(TESSERACT_BASE_PATH_NOT_CONFIGURED);
						throw new DCMAApplicationException(TESSERACT_BASE_PATH_NOT_CONFIGURED);
					}
				}
				if (listOfimages != null && listOfimages.length > 0) {
					if (isAdvancedKvExtraction) {
						LOGGER.info("hOCRing file: " + testImageFile.getAbsolutePath());
						performHOCRingOfImage(testImageFile, imageToOCRFilePathMap, ocrEngineName, listOfHOCRFiles, tesseractVersion,
								tesseractBasePath, batchClassIdentifer);
					} else {
						// instead of calling each image, call the api for creating hocr for whole folder.
						generateHOCRFiles(imageFolder, batchClassIdentifer);
					}
				} else {
					LOGGER.info("No images found for learning inside :" + imageFolder);
				}
			} else {
				LOGGER.info("OCR Engine not configured.");
			}
		}
		return imageToOCRFilePathMap;
	}

	private void performHOCRingOfImage(final File testImage, Map<String, String> imageToOCRFilePathMap, String ocrEngineName,
			String[] listOfHOCRFiles, final String tesseractVersion, final String tesseractBasePath, final String batchClassIdentifier)
			throws DCMAApplicationException {
		StringBuffer targetHTMlAbsolutePath = new StringBuffer();
		// boolean isTesseractVersion3Batch = false;
		if (testImage.exists()) {
			String imageAbsolutePath = testImage.getAbsolutePath();
			String imagePath = imageAbsolutePath.toLowerCase(Locale.getDefault());
			targetHTMlAbsolutePath.append(imageAbsolutePath.substring(0, imagePath.lastIndexOf(FileType.TIF.getExtensionWithDot())));
			targetHTMlAbsolutePath.append(FileType.HOCR_XML.getExtension());
			String imageName = testImage.getName().toLowerCase(Locale.getDefault());
			String hocrFileName = testImage.getName().substring(0, imageName.lastIndexOf(FileType.TIF.getExtensionWithDot()))
					+ FileType.HOCR_XML.getExtension();
			boolean hocrExists = checkHocrExists(hocrFileName, listOfHOCRFiles);
			if (!hocrExists) {
				try {
					ocrFactoryClass.createOCRForFile(batchClassIdentifier, testImage.getParent(), testImage.getName(),
							testImage.getParent());
					LOGGER.info(EphesoftStringUtil.concatenate("Generated HOCR file : ", targetHTMlAbsolutePath));
				} catch (Exception exception) {
					LOGGER.error(EphesoftStringUtil.concatenate(EXCEPTION_GENERATING_HOCR_FOR_IMAGE, imageAbsolutePath,
							exception.getMessage()));
					throw new DCMAApplicationException(EphesoftStringUtil.concatenate(EXCEPTION_GENERATING_HOCR_FOR_IMAGE,
							imageAbsolutePath, exception.getMessage()), exception);
				}
			}
			imageToOCRFilePathMap.put(testImage.getPath(), targetHTMlAbsolutePath.toString());
		}
	}

	/**
	 * This method generates the HOCR file when "Learn Files" button is pressed from Admin UI.
	 * 
	 * @param learnFolder String
	 * @param batchClass String
	 * @throws DCMAApplicationException
	 */
	public void generateHOCRFilesFromTesseract(final String learnFolder, final String batchClass) throws DCMAApplicationException {
		LOGGER.info("Inside Tesseract hocr generation");
		BatchPluginConfiguration[] pluginConfiguration = batchClassPluginPropertiesService.getPluginProperties(batchClass,
				TESSERACT_HOCR_PLUGIN, TesseractVersionProperty.TESSERACT_VERSIONS);
		String tesseractVersion = EMPTY_STRING;
		if (pluginConfiguration != null && pluginConfiguration.length > 0 && pluginConfiguration[0].getValue() != null
				&& pluginConfiguration[0].getValue().length() > 0) {
			tesseractVersion = pluginConfiguration[0].getValue();
		}
		final BatchPluginConfiguration[] pluginConfigurations = batchClassPluginPropertiesService.getPluginProperties(batchClass,
				TESSERACT_HOCR_PLUGIN, TesseractProperties.TESSERACT_LANGUAGE);
		String cmdLanguage = CMD_LANGUAGE_ENG;
		if (null != pluginConfigurations && pluginConfigurations.length > 0) {
			final String pluginConfig = pluginConfigurations[0].getValue();
			if (null != pluginConfig && pluginConfig.length() > 0) {
				cmdLanguage = pluginConfig;
			}
		}
		String tesseractBasePath = EMPTY_STRING;
		try {
			ApplicationConfigProperties app = ApplicationConfigProperties.getApplicationConfigProperties();
			tesseractBasePath = app.getProperty(tesseractVersion);
		} catch (IOException ioe) {
			LOGGER.error(TESSERACT_BASE_PATH_NOT_CONFIGURED + ioe, ioe);
			throw new DCMAApplicationException(TESSERACT_BASE_PATH_NOT_CONFIGURED, ioe);
		}
		if (tesseractBasePath == null) {
			LOGGER.error(TESSERACT_BASE_PATH_NOT_CONFIGURED);
			throw new DCMAApplicationException(TESSERACT_BASE_PATH_NOT_CONFIGURED);
		}
		File fBatchClass = new File(learnFolder);
		String[] listOfDocumetFolders = fBatchClass.list();
		BatchInstanceThread batchInstanceThread = new BatchInstanceThread();
		if (listOfDocumetFolders != null && listOfDocumetFolders.length > 0) {
			for (String documentFolder : listOfDocumetFolders) {
				File fDocumentFolder = new File(fBatchClass.getAbsolutePath() + File.separator + documentFolder);
				String[] listOfPageFolders = fDocumentFolder.list();
				if (listOfPageFolders != null && listOfPageFolders.length > 0) {
					for (String pageFolderName : listOfPageFolders) {
						String pageFolderPath = fBatchClass.getAbsolutePath() + File.separator + documentFolder + File.separator
								+ pageFolderName;
						File fPageFolder = new File(pageFolderPath);
						String[] listOfHtmlFiles = fPageFolder.list(new CustomFileFilter(false, FileType.HOCR_XML.getExtension()));
						String[] listOfImageFiles = fPageFolder.list(new CustomFileFilter(false, FileType.TIF.getExtensionWithDot(),
								FileType.TIFF.getExtensionWithDot()));
						if (listOfImageFiles != null && listOfImageFiles.length > 0) {
							for (String eachImageFile : listOfImageFiles) {
								String imageFile = eachImageFile.toLowerCase(Locale.getDefault());
								String hocrFileName = eachImageFile.substring(0,
										imageFile.lastIndexOf(FileType.TIF.getExtensionWithDot()))
										+ "_HOCR.xml";
								boolean hocrExists = checkHocrExists(hocrFileName, listOfHtmlFiles);
								if (!hocrExists) {
									try {
										tesseractService.createOCR(pageFolderPath, "OFF", eachImageFile, pageFolderPath, cmdLanguage,
												tesseractVersion);
									} catch (DCMAException e1) {
										e1.printStackTrace();
									}
								} else {
									LOGGER.info("HOCR already present for image : " + eachImageFile);
								}
							}
						} else {
							LOGGER.info("No Image file found");
						}
					}
				} else {
					LOGGER.info("No Page types present");
				}
			}
		} else {
			LOGGER.info("No Document types present");
		}
		LOGGER.info("Executing commands in multi thread environment");
		batchInstanceThread.execute();
		LOGGER.info("Learning completed successfully");
	}

	private boolean checkPageTypeExistence(List<com.ephesoft.dcma.da.domain.PageType> allPageTypes, String pageType) {
		boolean isDocumentExists = false;
		for (com.ephesoft.dcma.da.domain.PageType docField : allPageTypes) {
			if (docField.getName().equals(pageType)) {
				isDocumentExists = true;
				break;
			}
		}
		return isDocumentExists;
	}

	/**
	 * Performs learning for a batch class using the plugin selected for ocr'ing in the batch class configuration
	 * 
	 * @param batchClassID {@link BatchClassID} the batch class id
	 * @param createIndex specifies wheteher to create lucene indexes or not
	 * @throws DCMAApplicationException if an error occurs while creating the hocr file
	 * @throws IOException if an error occurs while creating the hocr file
	 */
	public void learnSampleHOCRFilesUsingSelectedPlugin(final String batchClassIdentifier, final boolean createIndex)
			throws DCMAApplicationException, IOException {
		LOGGER.info("Entering learn sample hocr files using selected plugin...");
		String admLearnFolder = batchSchemaService.getSearchClassSamplePath(batchClassIdentifier, false);
		String admIndexFolder = batchSchemaService.getSearchClassIndexFolder(batchClassIdentifier, false);
		if (!(!EphesoftStringUtil.isNullOrEmpty(admLearnFolder) && !EphesoftStringUtil.isNullOrEmpty(admIndexFolder))) {
			LOGGER.error("Problem initializing Lucene properties");
			throw new DCMAApplicationException("Problem initializing Lucene properties");
		}
		deleteIndexes(admIndexFolder);
		ocrFactoryClass.generateHOCRFilesForFolder(admLearnFolder, batchClassIdentifier);
		String batchFolderPath = admLearnFolder;
		try {
			IndexHTML.generateIndex(admIndexFolder, batchFolderPath, createIndex);
		} catch (Exception exception) {
			LOGGER.error(exception.getMessage(), exception);
			throw new DCMAApplicationException(exception.getMessage(), exception);
		}
		cleanUpIntrmediatePngs(batchFolderPath);
		final int tiffFileCount = FileUtils.getFileCountOfTypeFromDirectory(admLearnFolder, new String[] {
				com.ephesoft.dcma.util.FileType.TIF.getExtension(), com.ephesoft.dcma.util.FileType.TIFF.getExtension(),
				com.ephesoft.dcma.util.FileType.TIF.getExtension().toUpperCase(),
				com.ephesoft.dcma.util.FileType.TIFF.getExtension().toUpperCase()}, true);
		final int xmlFileCount = FileUtils.getFileCountOfTypeFromDirectory(admLearnFolder, new String[] {
				com.ephesoft.dcma.util.FileType.XML.getExtension(), com.ephesoft.dcma.util.FileType.XML.getExtension().toUpperCase()},
				true);
		if (xmlFileCount < tiffFileCount) {
			LOGGER.error("Learning failed for some sample tiff files.");
			throw new DCMAApplicationException(EphesoftStringUtil.concatenate(
					"Learning failed for some sample tiff files present inside batch class : ", batchClassIdentifier));
		} else {
			LOGGER.info(EphesoftStringUtil.concatenate("Learning performed successfully for batch class : ", batchClassIdentifier));
		}
		LOGGER.info("Exiting learn sample hocr files using selected plugin...");
	}
}
