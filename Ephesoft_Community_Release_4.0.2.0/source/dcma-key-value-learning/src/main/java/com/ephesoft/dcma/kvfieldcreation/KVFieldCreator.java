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

package com.ephesoft.dcma.kvfieldcreation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.Document.Pages;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.core.common.DCMABusinessException;
import com.ephesoft.dcma.core.common.KVExtractZone;
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.BatchInstanceService;
import com.ephesoft.dcma.da.service.KVExtractionService;
import com.ephesoft.dcma.kvfieldcreation.constant.KVFieldCreatorConstants;
import com.ephesoft.dcma.kvfieldcreation.io.PropertyMapReader;
import com.ephesoft.dcma.kvfinder.service.KVFinderService;
import com.ephesoft.dcma.util.EphesoftStringUtil;

/**
 * This class creates the key value field in batch class for every document level field of each document in the batch. New object is
 * needed for each request of method of this class.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvfieldcreation.service.KVFieldCreatorService
 */
@Component
@Scope("prototype")
public class KVFieldCreator implements ICommonConstants {

	private static final String MSG_KEY_CREATION = "Creating key at location : ";
	private static final Logger LOGGER = LoggerFactory.getLogger(KVFieldCreator.class);
	/**
	 * Location order to be followed.
	 */
	private String locationOrder;

	/**
	 * Maximum number of records per dlf.
	 */
	private String maxNumberRecordPerDlf;

	/**
	 * Tolerance threshold.
	 */
	private String toleranceThreshold;

	/**
	 * Array of location of type String.
	 */
	private String[] locationArr;

	/**
	 * Maximum number of records per dlf in integer.
	 */
	private int maxNumberRecordPerDlfInt;

	/**
	 * Tolerance threshold in integer.
	 */
	private int toleranceThresholdInt;

	/**
	 * To store the value of multiplier.
	 */
	private String multiplier;

	/**
	 * The fetch value.
	 */
	private String fetchValue;

	/**
	 * Minimum key character count.
	 */
	private String minKeyCharCount;

	/**
	 * The gap between the keys.
	 */
	private int gapBetweenKeys;

	/**
	 * A list to store the patterns or strings mentioned in key regex list.
	 */
	private List<String> keyRegexList = null;

	/**
	 * A list to store the patterns or strings mentioned in value regex list.
	 */
	private List<String> valueRegexList = null;

	/**
	 * A list to store the patterns or strings mentioned in exclusion list.
	 */
	private List<String> exclusionRegexList = null;

	/**
	 * An instance of {@link BatchSchemaService}.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * An instance of {@link BatchClassService}.
	 */
	@Autowired
	private BatchClassService batchClassService;

	/**
	 * An instance of {@link KVFinderService}.
	 */
	@Autowired
	private KVFinderService kvFinderService;

	/**
	 * An instance of {@link BatchInstanceService}.
	 */
	@Autowired
	private BatchInstanceService batchInstanceService;

	/**
	 * An instance of {@link KVExtractionService}.
	 */
	@Autowired
	private KVExtractionService kvExtractionService;

	/**
	 * Instance of {@link PluginPropertiesService}.
	 **/
	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;

	/**
	 * {@link KVExtractZone}
	 */
	private KVExtractZone kvExtractZone;

	/**
	 * {@link KVPageValue}
	 */
	private KVPageValue kvPageValue;

	/**
	 * weight for kv extraction.
	 */
	private float weight;

	/**
	 * getter for gapBetweenKeys.
	 * 
	 * @return int
	 */
	public int getGapBetweenKeys() {
		return gapBetweenKeys;
	}

	public void setGapBetweenKeys(int gapBetweenKeys) {
		this.gapBetweenKeys = gapBetweenKeys;
	}

	public String getMinKeyCharCount() {
		return minKeyCharCount;
	}

	public void setMinKeyCharCount(String minKeyCharCount) {
		this.minKeyCharCount = minKeyCharCount;
	}

	public String getFetchValue() {
		return fetchValue;
	}

	public void setFetchValue(final String fetchValue) {
		this.fetchValue = fetchValue;
	}

	public String getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(final String multiplier) {
		this.multiplier = multiplier;
	}

	public void setMaxNumberRecordPerDlfInt(final Integer maxNumberRecordPerDlfInt) {
		this.maxNumberRecordPerDlfInt = maxNumberRecordPerDlfInt;
	}

	public void setToleranceThresholdInt(final Integer toleranceThresholdInt) {
		this.toleranceThresholdInt = toleranceThresholdInt;
	}

	final public void setLocationOrder(final String locationOrder) {
		this.locationOrder = locationOrder;
		if (locationOrder == null || locationOrder.isEmpty()) {
			LOGGER.info("locationOrder is not defined in the specified property file ....");
			throw new DCMABusinessException("locationOrder is not definedin the specified property file ....");
		}
		this.locationArr = locationOrder.split(KVFieldCreatorConstants.SEMI_COLON);
	}

	final public String getMaxNumberRecordPerDlf() {
		return maxNumberRecordPerDlf;
	}

	final public void setMaxNumberRecordPerDlf(final String maxNumberRecordPerDlf) {
		this.maxNumberRecordPerDlf = maxNumberRecordPerDlf;
		Integer maxRecordsPerDlf = KVFieldCreatorConstants.DEFAULT_MAX_RECORD_PER_DLF;
		try {
			maxRecordsPerDlf = Integer.parseInt(maxNumberRecordPerDlf);
		} catch (NumberFormatException nfe) {
			LOGGER.info("cannot parse maxNumberRecordPerDlf from property file . maxNumberRecordPerDlf : " + maxNumberRecordPerDlf);
		}
		setMaxNumberRecordPerDlfInt(maxRecordsPerDlf);
	}

	final public String getToleranceThreshold() {
		return toleranceThreshold;
	}

	final public void setToleranceThreshold(final String toleranceThreshold) {
		this.toleranceThreshold = toleranceThreshold;
		Integer toleranceThresholdInt = KVFieldCreatorConstants.DEFAULT_TOLEANCE_THRESHOLD;
		try {
			toleranceThresholdInt = Integer.parseInt(toleranceThreshold);
		} catch (NumberFormatException nfe) {
			LOGGER.info("Cannot parse tolerance from properties field . ToleranceThershold : " + toleranceThreshold);
		}
		setToleranceThresholdInt(toleranceThresholdInt);
	}

	/**
	 * This method creates key value field for each document level field of every document.
	 * 
	 * @param batchInstanceIdentifier {@link String}
	 * @param pluginWorkflow {@link String}
	 * @throws DCMAApplicationException
	 */
	public void createKeyValueFields(String batchInstanceIdentifier, String pluginWorkflow) throws DCMAApplicationException {
		String kvFieldCreatorSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				KVFieldCreatorConstants.KV_FIELD_LEARNING_PLUGIN, KVFieldCreatorProperties.LEARNING_KEY_VALUE_SWTICH);
		String numericKeyLearningSwitch = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier,
				KVFieldCreatorConstants.KV_FIELD_LEARNING_PLUGIN, KVFieldCreatorProperties.LEARNING_NUMERIC_KEY_SWITCH);
		LOGGER.info("KV creation plugin switch value  : " + kvFieldCreatorSwitch);
		if (KVFieldCreatorConstants.SWITCH_ON.equalsIgnoreCase(kvFieldCreatorSwitch)) {
			String batchClassIdentifier = batchInstanceService.getBatchClassIdentifier(batchInstanceIdentifier);
			String kvLearningFolderPath = getKVLearningFolderLocation(batchClassIdentifier);
			setFieldsFromKeyValueRegexLocationFile(kvLearningFolderPath);
			if (null == keyRegexList) {
				keyRegexList = new ArrayList<String>();
				readRegexPropertyFile(
						EphesoftStringUtil.concatenate(kvLearningFolderPath, KVFieldCreatorConstants.KEY_REGEX_FILE_NAME),
						keyRegexList);
			}
			if (null == valueRegexList) {
				valueRegexList = new ArrayList<String>();
				readRegexPropertyFile(
						EphesoftStringUtil.concatenate(kvLearningFolderPath, KVFieldCreatorConstants.VALUE_REGEX_FILE_NAME),
						valueRegexList);
			}

			// get the exclusion list.
			if (null == exclusionRegexList) {
				exclusionRegexList = new ArrayList<String>();
				readRegexPropertyFile(
						EphesoftStringUtil.concatenate(kvLearningFolderPath, KVFieldCreatorConstants.EXCLUSION_REGEX_FILE_NAME),
						exclusionRegexList);
			}
			Float multiplierFloat = getMultiplierFloat();
			KVFetchValue kvFetchValue = getKVFetchValue();
			int minKeyCharsInt = getminKeyCharsInt();
			Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);
			BatchClass batchClass = batchClassService.getLoadedBatchClassByIdentifier(batchInstanceService
					.getBatchClassIdentifier(batchInstanceIdentifier));

			List<Document> documentList = batch.getDocuments().getDocument();
			Map<String, List<LineDataCarrier>> pageIdToLineDataCarrier = new HashMap<String, List<LineDataCarrier>>();
			boolean isSuccess = false;
			for (Document document : documentList) {
				DocumentLevelFields documentLevelFields = document.getDocumentLevelFields();
				if (null == documentLevelFields) {
					LOGGER.info("documentLevelFields is null for document : " + document.getType());
					continue;
				}
				List<DocField> docLevelFields = documentLevelFields.getDocumentLevelField();
				if (null != docLevelFields) {
					for (DocField docLevelField : docLevelFields) {
						KVExtraction kvExtractionField = new KVExtraction();

						// Null check added to avoid null Pointer exception for page id missing in hidden DLFs.
						Page page = getPage(document, docLevelField.getPage());
						if (null != page) {
							String docLevelFieldValue = docLevelField.getValue();
							List<LineDataCarrier> lineDataCarrierList = null;
							if (page == null || docLevelFieldValue == null || docLevelFieldValue.trim().isEmpty()) {
								continue;
							}
							String pageID = page.getIdentifier();
							if (pageID == null) {
								continue;
							}
							if (pageIdToLineDataCarrier != null && !pageIdToLineDataCarrier.containsKey(pageID)) {
								final HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier,
										page.getHocrFileName());
								if (null == hocrPages) {
									LOGGER.info("hocrPages is null. pageID = " + pageID + ". batchInstanceIdentifier = "
											+ batchInstanceIdentifier);
									continue;
								}
								HocrPage hocrPage = hocrPages.getHocrPage().get(0);
								Spans spans = hocrPage.getSpans();
								if (hocrPage != null && spans != null) {
									lineDataCarrierList = HocrUtil.getLineDataCarrierList(spans, hocrPage.getPageID());
								}
								if (lineDataCarrierList != null) {
									pageIdToLineDataCarrier.put(pageID, lineDataCarrierList);
								}
							} else {
								lineDataCarrierList = pageIdToLineDataCarrier.get(pageID);
							}
							isSuccess = createKeyValuePattern(kvExtractionField, lineDataCarrierList, docLevelField, minKeyCharsInt,
									numericKeyLearningSwitch);
							if (!isSuccess) {
								LOGGER.info("Key value field not created for DLF " + docLevelField.getName() + " for document "
										+ document.getType());
								continue;
							} else {
								kvExtractionField.setFetchValue(kvFetchValue);

								// Fix for JIRA ISSUE #11150 added page value, extract zone, weight value from property file to learned
								// kv extraction.
								kvExtractionField.setPageValue(kvPageValue);
								kvExtractionField.setExtractZone(kvExtractZone);
								kvExtractionField.setWeight(weight);
								addKVField(batchClass, document, docLevelField, kvExtractionField);
							}
						}
					}
				}
			}
			batchClassService.merge(batchClass);
		}
	}

	private List<KVExtraction> createKeyValuePatternList(KVFetchValue kvFetchValue, Float multiplierFloat, final String value,
			final List<LineDataCarrier> lineDataCarrierList, final int minKeyCharsInt, final String numericKeyLearningSwitch)
			throws DCMAApplicationException {
		List<KVExtraction> kvExtractionList = new ArrayList<KVExtraction>();
		boolean valueFound = false;
		boolean keyFound = false;
		if (null != lineDataCarrierList) {
			for (LineDataCarrier lineDataCarrier : lineDataCarrierList) {
				if (null != lineDataCarrier) {
					LOGGER.info("Searching for value: " + value + " in row == " + lineDataCarrier.getLineRowData());
					List<Span> spanList = lineDataCarrier.getSpanList();
					if (null == spanList) {
						continue;
					}
					for (Span span : spanList) {
						if (null != span) {
							String valueSpan = span.getValue();
							if (valueSpan != null && value.contains(valueSpan)) {
								KVExtraction kvExtractionField = new KVExtraction();
								String valuePattern = getRegexPattern(valueSpan, valueRegexList);
								kvExtractionField.setValuePattern(valuePattern);
								keyFound = searchKey(kvExtractionField, span.getCoordinates(), span, lineDataCarrier,
										lineDataCarrierList, minKeyCharsInt, numericKeyLearningSwitch);
								kvExtractionField.setFetchValue(kvFetchValue);

								// Fix for JIRA ISSUE #11150 added page value, extract zone, weight value from property file to learned
								// kv extraction.
								kvExtractionField.setWeight(weight);
								kvExtractionField.setExtractZone(kvExtractZone);
								kvExtractionField.setPageValue(kvPageValue);

								// Fix for JIRA ISSUE #11151 added order no. for each kv extraction field added.
								kvExtractionField.setOrderNo(getNextKVFieldOrderNo(kvExtractionList));
								kvExtractionList.add(kvExtractionField);
								LOGGER.info("Value Found=" + valueFound);
								LOGGER.info("Key Found=" + keyFound);
							}
						}
					}
				}
			}
		}
		return kvExtractionList;
	}

	/**
	 * This api is used to create the key value fields.
	 * 
	 * @param batchClassIdentifier {@link String} value containing identifier of batch class.
	 * @param value {@link String}
	 * @param hocrPage {@link com.ephesoft.dcma.batch.schema.HocrPages.HocrPage}
	 * @param numericKeyLearningSwitch {@link String}
	 * @return {@link List<KVExtraction>}
	 * @throws DCMAApplicationException
	 */
	public List<KVExtraction> createKeyValueFieldsAPI(String batchClassIdentifier, final String value, final HocrPage hocrPage,
			final String numericKeyLearningSwitch) throws DCMAApplicationException {
		String kvLearningFolderPath = getKVLearningFolderLocation(batchClassIdentifier);

		// Setting class fields from key value location property file.
		setFieldsFromKeyValueRegexLocationFile(kvLearningFolderPath);
		if (null == keyRegexList) {
			keyRegexList = new ArrayList<String>();
			readRegexPropertyFile(EphesoftStringUtil.concatenate(kvLearningFolderPath, KVFieldCreatorConstants.KEY_REGEX_FILE_NAME),
					keyRegexList);
		}
		if (null == valueRegexList) {
			valueRegexList = new ArrayList<String>();
			readRegexPropertyFile(EphesoftStringUtil.concatenate(kvLearningFolderPath, KVFieldCreatorConstants.VALUE_REGEX_FILE_NAME),
					valueRegexList);
		}
		if (null == exclusionRegexList) {
			exclusionRegexList = new ArrayList<String>();
			readRegexPropertyFile(
					EphesoftStringUtil.concatenate(kvLearningFolderPath, KVFieldCreatorConstants.EXCLUSION_REGEX_FILE_NAME),
					exclusionRegexList);
		}
		Float multiplierFloat = getMultiplierFloat();
		KVFetchValue kvFetchValue = getKVFetchValue();
		int minKeyCharsInt = getminKeyCharsInt();
		List<KVExtraction> kvExtractionList = new ArrayList<KVExtraction>();
		List<LineDataCarrier> lineDataCarrierList = HocrUtil.getLineDataCarrierList(hocrPage.getSpans(), hocrPage.getPageID());
		kvExtractionList = createKeyValuePatternList(kvFetchValue, multiplierFloat, value, lineDataCarrierList, minKeyCharsInt,
				numericKeyLearningSwitch);
		return kvExtractionList;
	}

	private int getminKeyCharsInt() {
		int minKeyCharsInt = KVFieldCreatorConstants.MIN_KEY_CHAR_COUNT;
		try {
			if (minKeyCharCount != null) {
				minKeyCharsInt = Integer.parseInt(minKeyCharCount);
			} else {
				LOGGER.error("No min_key_char_count specified, setting it to its default value '3'...");
			}
		} catch (NumberFormatException e) {
			LOGGER.error("Invalid value of min_key_characters value specified in properties file..., setting it to its default value 3..");
			minKeyCharsInt = KVFieldCreatorConstants.MIN_KEY_CHAR_COUNT;
		}
		return minKeyCharsInt;
	}

	private Float getMultiplierFloat() {
		float multilierFloat = KVFieldCreatorConstants.DEFAULT_MULTIPLIER;
		try {
			multilierFloat = Float.parseFloat(multiplier);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Couldn't parse multiplier value, setting it to its default value 1.", nfe);
		}
		return multilierFloat;
	}

	private KVFetchValue getKVFetchValue() {
		KVFetchValue kvFetchValue = KVFetchValue.ALL;
		try {
			if (fetchValue == null) {
				LOGGER.error("No fetch_value specified, setting it to its default value ALL.");
			} else {
				kvFetchValue = KVFetchValue.valueOf(fetchValue);
			}
		} catch (IllegalArgumentException illArgExcep) {
			LOGGER.error("Cannot parse value for fetch_value specified in properties file, setting its default value to ALL.",
					illArgExcep);
		}
		return kvFetchValue;
	}

	/**
	 * This method adds the key value field to the batch class.
	 * 
	 * @param batchClass
	 * @param document
	 * @param docLevelField
	 * @param kvExtractionField
	 */
	private void addKVField(final BatchClass batchClass, final Document document, final DocField docLevelField,
			final KVExtraction kvExtractionField) {
		boolean fieldAdded = false;
		boolean isDuplicateField = false;
		String docFieldName = docLevelField.getName();
		for (DocumentType documentType : batchClass.getDocumentTypes()) {
			if (documentType.getName().equals(document.getType())) {
				List<FieldType> fieldTypes = documentType.getFieldTypes();
				if (fieldTypes == null) {
					LOGGER.info("Field type is null for document type : " + documentType.getName());
					continue;
				}
				for (FieldType fieldType : fieldTypes) {
					String fieldName = fieldType.getName();
					if (fieldName != null && docFieldName != null && fieldName.equals(docFieldName)) {
						if (isDuplicateKVField(fieldType, kvExtractionField)) {
							LOGGER.info("KV field duplicate for field type : " + fieldName);
							isDuplicateField = true;
						} else {
							List<KVExtraction> kvExtractionList = fieldType.getKvExtraction();
							if (kvExtractionList == null) {
								fieldType.setKvExtraction(new ArrayList<KVExtraction>());
							}
							if (kvExtractionList.size() < this.maxNumberRecordPerDlfInt) {
								LOGGER.info("Field added to field type " + fieldName + " of document type " + documentType.getName());

								// Fix for JIRA ISSUE #11151 added order no. for each kv extraction field added.
								kvExtractionField.setOrderNo(getNextKVFieldOrderNo(kvExtractionList));
								fieldType.addKVExtraction(kvExtractionField);
								fieldAdded = true;
								break;
							}
						}
					}
				}
				if (fieldAdded || isDuplicateField) {
					break;
				}
			}
		}
	}

	/**
	 * This method checks if the new KV field already exists for the field type.
	 * 
	 * @param fieldType
	 * @param kvExtractionField
	 * @return
	 */
	private boolean isDuplicateKVField(final FieldType fieldType, final KVExtraction kvExtractionField) {
		LOGGER.info("checking for duplicate key value field ..");
		boolean isDuplicateField = false;
		List<KVExtraction> kvExtractionList = kvExtractionService.getDuplicateKVFields(fieldType, kvExtractionField);
		if (kvExtractionList != null && kvExtractionList.size() > 0) {
			LOGGER.info("KV field is duplicate.");
			isDuplicateField = true;
		} else {
			LOGGER.info("KV field is not duplicate.");
		}
		return isDuplicateField;
	}

	/**
	 * This method is used to read regex property file and put data in {@link List}
	 * 
	 * @param filePath {@link String} path of file which is to be read.
	 * @param regexList {@link List} which can store list of {@link String} .
	 */
	private void readRegexPropertyFile(final String filePath, final List<String> regexList) {
		LOGGER.debug(EphesoftStringUtil.concatenate("Reading property file having path : ", filePath));
		if (regexList != null) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(filePath));
				String pattern = reader.readLine();
				LOGGER.debug(EphesoftStringUtil.concatenate("Pattern read from file is : ", pattern));
				while (pattern != null) {
					regexList.add(pattern);
					pattern = reader.readLine();
					LOGGER.debug(EphesoftStringUtil.concatenate("Pattern read from file is : ", pattern));
				}
			} catch (FileNotFoundException fileNotFoundException) {
				LOGGER.info("File not found at path speified.");
			} catch (IOException ioException) {
				LOGGER.info("Error reading from property file.");
			} finally {
				IOUtils.closeQuietly(reader);
			}
		}
	}

	/**
	 * This method creates key value pattern for doc level field.
	 * 
	 * @param kvExtractionField
	 * @param lineDataCarrierList
	 * @param docLevelField
	 * @param minKeyCharsInt
	 * @param numericKeyLearningSwitch {@link String}
	 * @return keyFound
	 * @throws DCMAApplicationException
	 */
	private boolean createKeyValuePattern(final KVExtraction kvExtractionField, final List<LineDataCarrier> lineDataCarrierList,
			final DocField docLevelField, final int minKeyCharsInt, final String numericKeyLearningSwitch)
			throws DCMAApplicationException {
		CoordinatesList coordinates = docLevelField.getCoordinatesList();
		boolean valueFound = false;
		boolean keyFound = false;
		if (coordinates != null) {
			List<Coordinates> coordinatesList = coordinates.getCoordinates();
			Coordinates recCoordinates = HocrUtil.getRectangleCoordinates(coordinatesList);
			String docLevelFieldValue = docLevelField.getValue();
			if (null != lineDataCarrierList) {
				for (LineDataCarrier lineDataCarrier : lineDataCarrierList) {
					if (null != lineDataCarrier) {
						LOGGER.info("Searching for value: " + docLevelFieldValue + " in row == " + lineDataCarrier.getLineRowData());
						List<Span> spanList = lineDataCarrier.getSpanList();
						if (null != spanList) {
							for (Span span : spanList) {
								if (null != span) {
									String value = span.getValue();
									if (value != null && docLevelFieldValue.contains(value) && matchCoordinates(span, recCoordinates)) {
										String valuePattern = getRegexPattern(value, valueRegexList);
										kvExtractionField.setValuePattern(valuePattern);
										keyFound = searchKey(kvExtractionField, recCoordinates, span, lineDataCarrier,
												lineDataCarrierList, minKeyCharsInt, numericKeyLearningSwitch);
										valueFound = true;
										break;
									}
								}
							}
						}
						if (valueFound) {
							break;
						}
					}
				}
			}
		}
		LOGGER.info("Value Found=" + valueFound);
		LOGGER.info("Key Found=" + keyFound);
		return keyFound && valueFound;
	}

	/**
	 * This method look for key in directions as ordered in locationArr array.
	 * 
	 * @param kvExtractionField
	 * @param recCoordinates
	 * @param span
	 * @param lineDataCarrier
	 * @param lineDataCarrierList
	 * @param minKeyCharsInt
	 * @param numericKeyLearningSwitch {@link String}
	 * @return {@link Boolean}
	 * @throws DCMAApplicationException
	 */
	private boolean searchKey(final KVExtraction kvExtractionField, final Coordinates recCoordinates, final Span span,
			LineDataCarrier lineDataCarrier, final List<LineDataCarrier> lineDataCarrierList, final int minKeyCharsInt,
			final String numericKeyLearningSwitch) throws DCMAApplicationException {
		boolean keyFound = false;
		try {
			Coordinates keyCoordinates = new Coordinates();
			if (locationOrder == null || locationOrder.isEmpty()) {
				LOGGER.error("locationOrder is not defined in the specified property file ....");
				throw new DCMAApplicationException("locationOrder is not defined in the specified property file ....");
			}

			for (String location : this.locationArr) {
				LocationType locationType = LocationType.valueOf(location);
				LOGGER.info("Searching key at location : " + locationType);
				switch (locationType) {
					case TOP:
					case TOP_LEFT:
					case TOP_RIGHT:
						keyFound = createKeyTop(kvExtractionField, recCoordinates, lineDataCarrier, lineDataCarrierList, locationType,
								keyCoordinates, minKeyCharsInt, numericKeyLearningSwitch);
						break;

					case RIGHT:
						keyFound = createKeyRight(kvExtractionField, recCoordinates, span, lineDataCarrier, locationType,
								keyCoordinates, minKeyCharsInt, numericKeyLearningSwitch);
						break;

					case LEFT:
						keyFound = createKeyLeft(kvExtractionField, recCoordinates, span, lineDataCarrier, locationType,
								keyCoordinates, minKeyCharsInt, numericKeyLearningSwitch);
						break;

					case BOTTOM:
					case BOTTOM_LEFT:
					case BOTTOM_RIGHT:
						keyFound = createKeyBottom(kvExtractionField, recCoordinates, lineDataCarrier, lineDataCarrierList,
								locationType, keyCoordinates, minKeyCharsInt, numericKeyLearningSwitch);
						break;

					default:
						LOGGER.info("***********  Default case found. In valid case.*************");
						break;
				}
				LOGGER.info("Key found = " + keyFound);
				if (keyFound) {
					setValues(kvExtractionField, keyCoordinates, recCoordinates);
					break;
				}
			}
		} catch (IllegalArgumentException illArgExcep) {
			LOGGER.error("Invalid location specified in the properties file....", illArgExcep);
			throw new DCMAApplicationException("Invalid location specified in the properties file....", illArgExcep);
		}
		return keyFound;
	}

	/**
	 * This method sets the length, width, x-offset and y-offset values of the new KV Field.
	 * 
	 * @param kvExtractionField {@link KVExtraction}
	 * @param keyCoordinates {@link Coordinates}
	 * @param valueCoordinates {@link Coordinates}
	 */
	private void setValues(final KVExtraction kvExtractionField, final Coordinates keyCoordinates, final Coordinates valueCoordinates) {
		if (isValidCoordinate(keyCoordinates) && isValidCoordinate(valueCoordinates)) {
			long keyX1 = keyCoordinates.getX1().longValue();
			long keyY1 = keyCoordinates.getY1().longValue();

			long valueX0 = valueCoordinates.getX0().longValue();
			long valueY0 = valueCoordinates.getY0().longValue();
			long valueX1 = valueCoordinates.getX1().longValue();
			long valueY1 = valueCoordinates.getY1().longValue();

			int xOffsetInInt = (int) Math.round(valueX0 - keyX1);
			int yOffsetInInt = (int) Math.round(valueY0 - keyY1);
			LOGGER.debug("X offset : " + xOffsetInInt);
			LOGGER.debug("Y offset : " + yOffsetInInt);

			int lengthOfBoxInInt = (int) Math.round(valueX1 - valueX0);
			int widthOfBoxInInt = (int) Math.round(valueY1 - valueY0);
			lengthOfBoxInInt = lengthOfBoxInInt + (this.toleranceThresholdInt * lengthOfBoxInInt) / 100;
			widthOfBoxInInt = widthOfBoxInInt + (this.toleranceThresholdInt * widthOfBoxInInt) / 100;
			LOGGER.debug("value zone length : " + lengthOfBoxInInt);
			LOGGER.debug("value zone width : " + widthOfBoxInInt);

			kvExtractionField.setXoffset(xOffsetInInt);
			kvExtractionField.setYoffset(yOffsetInInt);
			kvExtractionField.setLength(lengthOfBoxInInt);
			kvExtractionField.setWidth(widthOfBoxInInt);
			// kvExtractionField.setLocationType(locationType);
		}
	}

	/**
	 * This method creates key at TOP,TOP_LEFT and TOP_RIGHT of value.
	 * 
	 * @param kvExtractionField {@link KVExtraction}
	 * @param recCoordinates
	 * @param lineDataCarrier
	 * @param lineDataCarrierList
	 * @param location
	 * @param keyCoordinates
	 * @param minKeyCharsInt
	 * @param numericKeyLearningSwitch {@link String}
	 * @return {@link Boolean}
	 */
	private boolean createKeyTop(final KVExtraction kvExtractionField, final Coordinates recCoordinates,
			final LineDataCarrier lineDataCarrier, final List<LineDataCarrier> lineDataCarrierList, final LocationType location,
			final Coordinates keyCoordinates, int minKeyCharsInt, final String numericKeyLearningSwitch) {
		boolean keyFound = false;
		LOGGER.info(MSG_KEY_CREATION + location.toString());
		int index = lineDataCarrierList.indexOf(lineDataCarrier);
		Coordinates spanCoordinates = null;
		LineDataCarrier topLineDataCarrier = null;
		while (index > 0) {
			topLineDataCarrier = lineDataCarrierList.get(--index);
			List<Span> spanList = new ArrayList<Span>();
			spanList.addAll(topLineDataCarrier.getSpanList());
			if (LocationType.TOP_LEFT.equals(location)) {
				Collections.reverse(spanList);
			}
			for (Span span : spanList) {
				if (span == null) {
					continue;
				}
				spanCoordinates = span.getCoordinates();
				String key = span.getValue();
				if (key != null && !key.trim().isEmpty() && !KVFieldCreatorConstants.KEY_VALUE_SEPARATORS.contains(key)
						&& validateKey(key, recCoordinates, spanCoordinates, location, numericKeyLearningSwitch)) {
					if (LocationType.TOP_RIGHT.equals(location)) {
						keyFound = concatenateKeysRight(kvExtractionField, topLineDataCarrier, span, minKeyCharsInt, keyCoordinates);
					} else {
						keyFound = concatenateKeysLeft(kvExtractionField, topLineDataCarrier, span, minKeyCharsInt, keyCoordinates);
					}
					LOGGER.info("Key found at location: " + keyFound);
					break;
				}
			}
			if (keyFound) {
				break;
			}
		}
		return keyFound;
	}

	/**
	 * This method creates key at BOTTOM,BOTTOM_LEFT and BOTTOM_RIGHT of value.
	 * 
	 * @param kvExtractionField {@link KVExtraction}
	 * @param recCoordinates {@link Coordinates}
	 * @param lineDataCarrier {@link LineDataCarrier}
	 * @param lineDataCarrierList {@link List<LineDataCarrier>}
	 * @param location {@link LocationType}
	 * @param keyCoordinates
	 * @param keyCoordinates {@link Coordinates}
	 * @param minKeyCharsInt
	 * @param numericKeyLearningSwitch {@link String}
	 * @return {@link Boolean}
	 */
	private boolean createKeyBottom(final KVExtraction kvExtractionField, final Coordinates recCoordinates,
			final LineDataCarrier lineDataCarrier, final List<LineDataCarrier> lineDataCarrierList, final LocationType location,
			final Coordinates keyCoordinates, final int minKeyCharsInt, final String numericKeyLearningSwitch) {
		boolean keyFound = false;
		LOGGER.info(MSG_KEY_CREATION + location.toString());
		int index = lineDataCarrierList.indexOf(lineDataCarrier);
		int length = lineDataCarrierList.size();
		Coordinates spanCoordinates = null;
		LineDataCarrier bottomLineDataCarrier = null;
		while (index < length - 1) {
			bottomLineDataCarrier = lineDataCarrierList.get(++index);
			List<Span> spanList = new ArrayList<Span>();
			spanList.addAll(bottomLineDataCarrier.getSpanList());
			if (LocationType.BOTTOM_LEFT.equals(location)) {
				Collections.reverse(spanList);
			}
			for (Span span : spanList) {
				if (span == null) {
					continue;
				}
				spanCoordinates = span.getCoordinates();
				String key = span.getValue();
				if (key != null && !key.trim().isEmpty() && !KVFieldCreatorConstants.KEY_VALUE_SEPARATORS.contains(key)
						&& validateKey(key, recCoordinates, spanCoordinates, location, numericKeyLearningSwitch)) {
					if (LocationType.BOTTOM_RIGHT.equals(location)) {
						keyFound = concatenateKeysRight(kvExtractionField, bottomLineDataCarrier, span, minKeyCharsInt, keyCoordinates);
					} else {
						keyFound = concatenateKeysLeft(kvExtractionField, bottomLineDataCarrier, span, minKeyCharsInt, keyCoordinates);
					}
					LOGGER.info("Key found at location: " + keyFound);
					break;
				}
			}
			if (keyFound) {
				break;
			}
		}
		return keyFound;
	}

	/**
	 * This method searches key to left of value.
	 * 
	 * @param kvExtractionField {@link KVExtraction}
	 * @param recCoordinates
	 * @param span
	 * @param lineDataCarrier
	 * @param location
	 * @param keyCoordinates
	 * @param minKeyCharsInt
	 * @param numericKeyLearningSwitch {@link String}
	 * @return {@link Boolean}
	 */
	private boolean createKeyLeft(KVExtraction kvExtractionField, Coordinates recCoordinates, Span span,
			LineDataCarrier lineDataCarrier, LocationType location, Coordinates keyCoordinates, int minKeyCharsInt,
			final String numericKeyLearningSwitch) {
		LOGGER.info(MSG_KEY_CREATION + location.toString());
		int prevKeyX0 = 0;
		boolean keyFound = false;
		int gapBetweenWords = 0;
		StringBuffer keyString = new StringBuffer();
		List<Coordinates> keyCoordinatesList = new ArrayList<Coordinates>();
		Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
		if (spanIndex != null) {
			Span leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
			while (leftSpan != null) {
				String key = leftSpan.getValue();
				if (keyFound && null != key && !key.trim().isEmpty()) {
					gapBetweenWords = prevKeyX0 - leftSpan.getCoordinates().getX1().intValue();
					if (Math.abs(gapBetweenWords) < gapBetweenKeys) {
						keyCoordinatesList.add(leftSpan.getCoordinates());
						keyString.insert(0, key.trim() + KVFieldCreatorConstants.SPACE);
						prevKeyX0 = leftSpan.getCoordinates().getX0().intValue();
						LOGGER.info("Key == " + keyString);
					} else {
						break;
					}
				} else if (key != null && !key.trim().isEmpty() && !KVFieldCreatorConstants.KEY_VALUE_SEPARATORS.contains(key)
						&& validateKey(key, recCoordinates, leftSpan.getCoordinates(), location, numericKeyLearningSwitch)) {
					keyCoordinatesList.add(leftSpan.getCoordinates());
					prevKeyX0 = leftSpan.getCoordinates().getX0().intValue();
					keyString.append(key.trim());
					LOGGER.info("Key == " + keyString);
					keyFound = true;
				}
				spanIndex = spanIndex - 1;
				leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
			}
			if (keyFound) {
				keyFound = setKeyPattern(kvExtractionField, keyCoordinates, minKeyCharsInt, keyString, keyCoordinatesList);
			}
		}
		return keyFound;
	}

	/**
	 * checks if exclusion string is valid, i.e. present in exclusion list
	 * 
	 * @param keyString is the string provided
	 * @return true if not present in exclusion
	 */
	private boolean checkExclusionList(String keyString) {
		boolean isValidkey = true;
		if (null != exclusionRegexList && !exclusionRegexList.isEmpty()) {
			for (String exclusionString : exclusionRegexList) {
				if (keyString.matches(exclusionString)) {
					isValidkey = false;
					break;

				}
			}
		}
		return isValidkey;
	}

	/**
	 * 
	 * @param kvExtractionField
	 * @param recCoordinates
	 * @param span
	 * @param lineDataCarrier
	 * @param location
	 * @param keyCoordinates
	 * @param minKeyCharsInt
	 * @param numericKeyLearningSwitch {@link String}
	 * @return
	 */
	private boolean createKeyRight(KVExtraction kvExtractionField, Coordinates recCoordinates, Span span,
			LineDataCarrier lineDataCarrier, LocationType location, Coordinates keyCoordinates, int minKeyCharsInt,
			final String numericKeyLearningSwitch) {
		LOGGER.info(MSG_KEY_CREATION + location.toString());
		boolean keyFound = false;
		Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
		if (spanIndex != null) {
			Span rightSpan = lineDataCarrier.getRightSpan(spanIndex);
			spanIndex = spanIndex + 1;
			int prevKeyX1 = 0;
			int gapBetweenWords = 0;
			StringBuffer keyString = new StringBuffer();
			List<Coordinates> keyCoordinatesList = new ArrayList<Coordinates>();
			while (rightSpan != null) {
				String key = rightSpan.getValue();
				if (keyFound && null != key && !key.trim().isEmpty()) {
					gapBetweenWords = prevKeyX1 - rightSpan.getCoordinates().getX0().intValue();
					if (Math.abs(gapBetweenWords) < gapBetweenKeys) {
						keyCoordinatesList.add(rightSpan.getCoordinates());
						keyString.append(KVFieldCreatorConstants.SPACE);
						keyString.append(key.trim());
						prevKeyX1 = rightSpan.getCoordinates().getX1().intValue();
					} else {
						break;
					}
				} else if (key != null && !key.trim().isEmpty() && !KVFieldCreatorConstants.KEY_VALUE_SEPARATORS.contains(key)
						&& validateKey(key, recCoordinates, rightSpan.getCoordinates(), location, numericKeyLearningSwitch)) {
					keyCoordinatesList.add(rightSpan.getCoordinates());
					prevKeyX1 = rightSpan.getCoordinates().getX1().intValue();
					keyString.append(key.trim());
					keyFound = true;
				}
				spanIndex = spanIndex + 1;
				rightSpan = lineDataCarrier.getRightSpan(spanIndex);
			}
			if (keyFound) {
				keyFound = setKeyPattern(kvExtractionField, keyCoordinates, minKeyCharsInt, keyString, keyCoordinatesList);
			}
		}
		return keyFound;
	}

	private boolean setKeyPattern(KVExtraction kvExtractionField, Coordinates keyCoordinates, int minKeyCharsInt,
			StringBuffer keyString, List<Coordinates> keyCoordinatesList) {
		boolean keyFound;
		final String finalKeyString = keyString.toString().trim();

		// check if the found key is present in exclusion list or not.
		if (finalKeyString.length() >= minKeyCharsInt && checkExclusionList(finalKeyString)) {
			LOGGER.info("Key found: " + finalKeyString);
			final String keyPattern = getRegexPattern(finalKeyString, keyRegexList);

			kvExtractionField.setKeyPattern(keyPattern);
			setKeyCoordinates(keyCoordinates, keyCoordinatesList);
			LOGGER.info("Key Pattern Created : " + keyPattern);
			keyFound = true;
		} else {
			LOGGER.info("Required no of characters in key= " + minKeyCharsInt + " , Chars found= " + finalKeyString.length());
			keyFound = false;
		}
		return keyFound;
	}

	/**
	 * Validates a proposed key span with valid key pattern syntax and with respect to value coordinates.
	 * 
	 * @param keyString {@link String}
	 * @param recCoordinates {@link Coordinates}
	 * @param spanCoordinates {@link Coordinates}
	 * @param location {@link LocationType}
	 * @param numericKeyLearningSwitch {@link String}
	 * @return {@link Boolean}
	 */
	private boolean validateKey(final String keyString, final Coordinates recCoordinates, final Coordinates spanCoordinates,
			final LocationType location, final String numericKeyLearningSwitch) {
		boolean isKeyValid = false;

		// bug id #7154: Invalid Key pattern is extracted through automatic key value learning.
		boolean isKeyPatternSyntaxValid = true;
		try {
			Pattern.compile(keyString);
		} catch (PatternSyntaxException patternInvalidException) {
			isKeyPatternSyntaxValid = false;
		}

		if (isKeyPatternSyntaxValid) {

			// Auto KV learning algo enhancement to ignore Keys with numeric data (having exactly a digit/ having two or more digits in
			// entire key string) if numericKeyLearningSwitch switch is off.
			boolean isKeyNumericTestRequired = KVFieldCreatorConstants.SWITCH_OFF.equalsIgnoreCase(numericKeyLearningSwitch);
			boolean isKeyNonNumeric = true;
			if (isKeyNumericTestRequired) {
				try {
					Pattern pattern = Pattern.compile(EphesoftStringUtil.concatenate(
							KVFieldCreatorConstants.ATLEAST_TWO_DIGITS_PATTERN, KVFieldCreatorConstants.OR_OPERATOR,
							KVFieldCreatorConstants.ONLY_ONE_DIGIT_PATTERN));
					Matcher matcher = pattern.matcher(keyString);
					while (matcher.find()) {
						for (int i = 0; i <= matcher.groupCount(); i++) {
							if (isKeyNonNumeric) {
								isKeyNonNumeric = false;
							}
						}
					}
				} catch (PatternSyntaxException patternInvalidException) {
					LOGGER.error("The Numeric check pattern used has invalid syntax.");
				}
			}
			if (!isKeyNumericTestRequired || (isKeyNumericTestRequired && isKeyNonNumeric)) {
				LOGGER.info(MSG_KEY_CREATION + location.toString());
				long rectangleX0 = recCoordinates.getX0().longValue();
				long rectangleY0 = recCoordinates.getY0().longValue();
				long rectangleX1 = recCoordinates.getX1().longValue();
				long rectangleY1 = recCoordinates.getY1().longValue();

				long spanX0 = spanCoordinates.getX0().longValue();
				long spanY0 = spanCoordinates.getY0().longValue();
				long spanX1 = spanCoordinates.getX1().longValue();
				long spanY1 = spanCoordinates.getY1().longValue();

				long diffX0 = Math.abs(spanX0 - rectangleX0);
				long diffX1 = Math.abs(spanX1 - rectangleX1);

				if ((spanX0 <= rectangleX0 && spanX1 <= rectangleX0) || (spanX0 >= rectangleX1 && spanX1 >= rectangleX1)
						|| (spanY0 <= rectangleY0 && spanY1 <= rectangleY0) || (spanY0 >= rectangleY1 && spanY1 >= rectangleY1)) {
					isKeyValid = true;
				}

				switch (location) {
					case BOTTOM:
					case TOP:
						if (isKeyValid
								&& ((spanX0 <= rectangleX0 && spanX1 >= rectangleX1)
										|| (spanX0 >= rectangleX0 && spanX1 <= rectangleX1)
										|| ((spanX0 >= rectangleX0 && spanX0 <= rectangleX1) && (diffX0 >= diffX1)) || ((spanX1 >= rectangleX0 && spanX1 <= rectangleX1) && (diffX0 <= diffX1)))) {
							isKeyValid = true;
						} else {
							isKeyValid = false;
						}
						break;

					case BOTTOM_LEFT:
					case TOP_LEFT:
						if (isKeyValid && (spanX1 <= rectangleX0 || (spanX0 <= rectangleX0) && (diffX0 >= diffX1))) {
							isKeyValid = true;
						} else {
							isKeyValid = false;
						}
						break;

					case BOTTOM_RIGHT:
					case TOP_RIGHT:
						if (isKeyValid && (spanX0 >= rectangleX1 || (spanX1 >= rectangleX1) && (diffX0 <= diffX1))) {
							isKeyValid = true;
						} else {
							isKeyValid = false;
						}
						break;

					default:
						break;
				}
			}
		}
		return isKeyValid;
	}

	/**
	 * This method checks if span exists inside the value rectangle zone.
	 * 
	 * @param span {@link Span}
	 * @param recCoordinates {@link List<Coordinates}
	 * @return {@link Boolean}
	 */
	private boolean matchCoordinates(final Span span, final Coordinates recCoordinates) {
		boolean isValidCoor = false;
		Coordinates spanCoordinates = span.getCoordinates();
		long spanX0 = spanCoordinates.getX0().longValue();
		long spanY0 = spanCoordinates.getY0().longValue();
		long spanX1 = spanCoordinates.getX1().longValue();
		long spanY1 = spanCoordinates.getY1().longValue();
		long valueX0 = recCoordinates.getX0().longValue();
		long valueY0 = recCoordinates.getY0().longValue();
		long valueX1 = recCoordinates.getX1().longValue();
		long valueY1 = recCoordinates.getY1().longValue();
		// Verify whether span lie inside value rectangle.
		if ((spanX0 >= valueX0 && spanX0 <= valueX1) && (spanX1 >= valueX0 && spanX1 <= valueX1)
				&& (spanY0 >= valueY0 && spanY0 <= valueY1) && (spanY1 >= valueY0 && spanY1 <= valueY1)) {
			isValidCoor = true;
		}
		return isValidCoor;
	}

	/**
	 * Gets the Page with respect to passed apge identifier.
	 * 
	 * @param document {@link Document}
	 * @param pageIdentifier {@link String}
	 * @return {@link Page}
	 */
	private Page getPage(final Document document, final String pageIdentifier) {
		Page page = null;
		if (null != document) {
			Pages pages = document.getPages();
			if (null != pages && !EphesoftStringUtil.isNullOrEmpty(pageIdentifier)) {
				for (Page eachPage : pages.getPage()) {
					if (pageIdentifier.equals(eachPage.getIdentifier())) {
						page = eachPage;
						break;
					}
				}
			}
		}
		return page;
	}

	/**
	 * This method gets the matched regex pattern for input string.
	 * 
	 * @param inputString
	 * @param regexList
	 * @return String
	 */
	private String getRegexPattern(String inputString, List<String> regexList) {
		String matchedPattern = null;
		int confidenceInt = 100;
		Pattern pattern = null;
		Matcher matcher = null;
		float previousMatchedConfidence = 0;
		if (null == inputString || inputString.isEmpty()) {
			LOGGER.info("Input string is null or empty.");
		} else {
			String dlfValue = inputString.split(KVFieldCreatorConstants.SPACE)[0];
			for (String regex : regexList) {
				pattern = Pattern.compile(regex);
				matcher = pattern.matcher(dlfValue);
				while (matcher.find()) {
					for (int i = 0; i <= matcher.groupCount(); i++) {
						final String groupStr = matcher.group(i);
						if (null != groupStr) {
							final float confidence = (groupStr.length() * confidenceInt) / inputString.length();
							if (confidence > previousMatchedConfidence) {
								previousMatchedConfidence = confidence;
								matchedPattern = regex;
							}
						}
					}
				}
			}
			if (matchedPattern == null) {
				LOGGER.info("No regex pattern found, setting input string as the regex itself. Pattern == " + inputString);
				matchedPattern = inputString;
			}
		}
		return matchedPattern;
	}

	/**
	 * Concatenate keys found at left location.
	 * 
	 * @param kvExtractionField
	 * @param lineDataCarrier
	 * @param span
	 * @param minKeyCharsInt
	 * @param keyCoordinates
	 * @return
	 */
	private boolean concatenateKeysLeft(final KVExtraction kvExtractionField, final LineDataCarrier lineDataCarrier, final Span span,
			final int minKeyCharsInt, final Coordinates keyCoordinates) {
		LOGGER.info("Concatenating keys on left....");
		boolean keyFound = false;
		String key = span.getValue();
		StringBuffer keyString = null;
		List<Coordinates> keyCoordinatesList = new ArrayList<Coordinates>();
		if (key != null) {
			keyString = new StringBuffer(key);
			keyCoordinatesList.add(span.getCoordinates());
			int prevKeyX0 = span.getCoordinates().getX0().intValue();
			int gapBetweenWords = 0;
			keyString = new StringBuffer(key);
			Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
			if (spanIndex != null) {
				Span leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
				while (leftSpan != null) {
					key = leftSpan.getValue();
					if (null != key && !key.trim().isEmpty()) {
						gapBetweenWords = prevKeyX0 - leftSpan.getCoordinates().getX1().intValue();
						if (Math.abs(gapBetweenWords) < gapBetweenKeys) {
							LOGGER.info(key.trim() + " Concatenated on left.");
							keyCoordinatesList.add(leftSpan.getCoordinates());
							keyString.insert(0, key.trim() + KVFieldCreatorConstants.SPACE);
							prevKeyX0 = leftSpan.getCoordinates().getX0().intValue();
						} else {
							LOGGER.info("gap between words > " + gapBetweenKeys);
							break;
						}
					}
					spanIndex = spanIndex - 1;
					leftSpan = lineDataCarrier.getLeftSpan(spanIndex);
				}
			}
		}
		keyFound = setKeyPattern(kvExtractionField, keyCoordinates, minKeyCharsInt, keyString, keyCoordinatesList);
		return keyFound;
	}

	/**
	 * Concatenate keys found at right location.
	 * 
	 * @param kvExtractionField
	 * @param lineDataCarrier
	 * @param span
	 * @param minKeyCharsInt
	 * @param keyCoordinates
	 * @return
	 */
	private boolean concatenateKeysRight(final KVExtraction kvExtractionField, final LineDataCarrier lineDataCarrier, final Span span,
			final int minKeyCharsInt, final Coordinates keyCoordinates) {
		LOGGER.info("Concatenating keys on right....");
		boolean keyFound = false;
		String key = span.getValue();
		StringBuffer keyString = null;
		List<Coordinates> keyCoordinatesList = new ArrayList<Coordinates>();
		if (key != null) {
			keyString = new StringBuffer(key);
			keyCoordinatesList.add(span.getCoordinates());
			int prevKeyX1 = span.getCoordinates().getX1().intValue();
			int gapBetweenWords = 0;
			keyString = new StringBuffer(key);
			Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
			if (spanIndex != null) {
				Span rightSpan = lineDataCarrier.getLeftSpan(spanIndex);
				while (rightSpan != null) {
					key = rightSpan.getValue();
					if (null != key && !key.trim().isEmpty()) {
						gapBetweenWords = prevKeyX1 - rightSpan.getCoordinates().getX0().intValue();
						if (Math.abs(gapBetweenWords) < gapBetweenKeys) {
							LOGGER.info(key.trim() + " Concatenated on right.");
							keyCoordinatesList.add(rightSpan.getCoordinates());
							keyString.insert(0, key.trim() + KVFieldCreatorConstants.SPACE);
							prevKeyX1 = rightSpan.getCoordinates().getX0().intValue();
						} else {
							LOGGER.info("gap between words >= " + gapBetweenKeys);
							break;
						}
					}
					spanIndex = spanIndex - 1;
					rightSpan = lineDataCarrier.getRightSpan(spanIndex);
				}
			}
		}
		keyFound = setKeyPattern(kvExtractionField, keyCoordinates, minKeyCharsInt, keyString, keyCoordinatesList);
		return keyFound;
	}

	private void setKeyCoordinates(final Coordinates keyCoordinates, final List<Coordinates> keyCoordinatesList) {
		Coordinates recCoordinates = HocrUtil.getRectangleCoordinates(keyCoordinatesList);
		if (null != recCoordinates) {
			keyCoordinates.setX0(recCoordinates.getX0());
			keyCoordinates.setY0(recCoordinates.getY0());
			keyCoordinates.setX1(recCoordinates.getX1());
			keyCoordinates.setY1(recCoordinates.getY1());
		}
	}

	private boolean isValidCoordinate(final Coordinates coordinate) {
		boolean validCoordinate = false;
		BigInteger x0Coord = coordinate.getX0();
		BigInteger y0Coord = coordinate.getY0();
		BigInteger x1Coord = coordinate.getX1();
		BigInteger y1Coord = coordinate.getY1();
		LOGGER.debug("X0:\t" + x0Coord);
		LOGGER.debug("Y0:\t" + y0Coord);
		LOGGER.debug("X1:\t" + x1Coord);
		LOGGER.debug("Y1:\t" + y1Coord);
		if (x0Coord != null || y0Coord != null || x1Coord != null || y1Coord != null) {
			validCoordinate = true;
		}
		LOGGER.debug("Is valid coordinates = " + validCoordinate);
		return validCoordinate;
	}

	/**
	 * Returns the batch class folder path to which batch instance identifier belongs.
	 * 
	 * @param batchClassIdentifier {@link String} value having identifier of batch class.
	 * @return returns {@link String} having absolute path of batch class
	 */
	private String getKVLearningFolderLocation(final String batchClassIdentifier) {
		String baseFolderPath = batchSchemaService.getBaseFolderLocation();
		String kvLearningFolderPath = EphesoftStringUtil.concatenate(baseFolderPath, File.separator, batchClassIdentifier,
				File.separator, batchSchemaService.getKeyValueLearningFolderName(), File.separator);
		return kvLearningFolderPath;
	}

	/**
	 * Set the key value learning plugin properties for a batch class from key value location properties file.
	 * 
	 * @param kvLearningFolderPath {@link String} Path of kv learning folder.
	 */
	private void setFieldsFromKeyValueRegexLocationFile(final String kvLearningFolderPath) throws DCMAApplicationException {
		LOGGER.debug("Path of key value learning folder is : " + kvLearningFolderPath);
		PropertyMapReader propertyMapReader = new PropertyMapReader();
		String propertyFilePath = EphesoftStringUtil.concatenate(kvLearningFolderPath,
				KVFieldCreatorConstants.KEY_VALUE_LOCATION_FILE_NAME);
		Map<String, String> propertyMap = propertyMapReader.getPropertyMap(propertyFilePath);
		if (propertyMap == null) {
			LOGGER.error("Either property file is empty or cound not be read.");
			throw new DCMAApplicationException("Either property file is empty or cound not be read.");
		} else {
			setLocationOrder(propertyMap.get(KVFieldCreatorConstants.KEY_VALUE_LOCATION_ORDER));
			setMaxNumberRecordPerDlf(propertyMap.get(KVFieldCreatorConstants.KEY_VALUE_MAX_NUMBER_RECORD));
			setToleranceThreshold(propertyMap.get(KVFieldCreatorConstants.KEY_VALUE_TOLERANCE_THRESHOLD));
			setMultiplier(propertyMap.get(KVFieldCreatorConstants.KEY_VALUE_MULTIPLIER));
			setFetchValue(propertyMap.get(KVFieldCreatorConstants.KEY_VALUE_FETCH_VALUE));
			setMinKeyCharCount(propertyMap.get(KVFieldCreatorConstants.KEY_VALUE_MIN_KEY_CHAR_COUNT));

			// Fix for JIRA ISSUE #11150 added page value, extract zone, weight value from property file to learned kv extraction.
			setWeight(propertyMap.get(KVFieldCreatorConstants.KEY_VALUE_WEIGHT));
			setKvPageValue(propertyMap.get(KVFieldCreatorConstants.KEY_VALUE_PAGE_VALUE));
			setKVExtractZone(propertyMap.get(KVFieldCreatorConstants.KEY_VALUE_EXTRACT_ZONE));
			int gapBetweenKeys = KVFieldCreatorConstants.DEFAULT_GAP_BETWEEN_KEYS;
			try {
				gapBetweenKeys = Integer.parseInt(propertyMap.get(KVFieldCreatorConstants.KEY_VALUE_GAP_BETWEEN_KEYS));
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error("Value for kv gap between keys is not Integer type");
			}
			setGapBetweenKeys(gapBetweenKeys);
		}
	}

	/**
	 * Sets the Extract zone.
	 * 
	 * @param extractZone {@link String} which is converted to its respective enum.
	 */
	private void setKVExtractZone(final String extractZone) {
		this.kvExtractZone = KVExtractZone.ALL;
		if (extractZone == null) {
			LOGGER.error("No extract_zone specified, setting it to its default value ALL.");
		} else {
			KVExtractZone tempKVExtractZone = KVExtractZone.getValue(extractZone);
			if (tempKVExtractZone != null) {
				this.kvExtractZone = tempKVExtractZone;
			} else {
				LOGGER.error("Invalid value for extract_zone specified in properties file, setting its default value to ALL.");
			}
		}
	}

	/**
	 * Gets the kv extract zone.
	 * 
	 * @return {@link KVExtractZone}
	 */
	public KVExtractZone getKvExtractZone() {
		return kvExtractZone;
	}

	/**
	 * Sets the kv Extract zone.
	 * 
	 * @param kvExtractZone {@link KVExtractZone}
	 */
	public void setKvExtractZone(final KVExtractZone kvExtractZone) {
		this.kvExtractZone = kvExtractZone;
	}

	/**
	 * Sets KV page value.
	 * 
	 * @param pageValue{@link String} value corresponding to which page value to be set.
	 */
	private void setKvPageValue(final String pageValue) {
		this.kvPageValue = KVPageValue.ALL;
		if (pageValue == null) {
			LOGGER.error("No page_value specified, setting it to its default value ALL.");
		} else {
			KVPageValue tempKVPageValue = KVPageValue.getValue(pageValue);
			if (tempKVPageValue != null) {
				this.kvPageValue = tempKVPageValue;
			} else {
				LOGGER.error("Invalid value for page_value specified in properties file, setting its default value to ALL.");
			}
		}
	}

	/**
	 * Gets the PageValue.
	 * 
	 * @return {@link KVPageValue}
	 */
	public KVPageValue getKvPageValue() {
		return kvPageValue;
	}

	/**
	 * Sets the Page value.
	 * 
	 * @param kvPageValue {@link KVPageValue}
	 */
	public void setKvPageValue(final KVPageValue kvPageValue) {
		this.kvPageValue = kvPageValue;
	}

	/**
	 * Sets the Weight.
	 * 
	 * @param weightData {@link String} containing value for weight which will be converted to float and set for weight.
	 */
	private void setWeight(final String weightData) {
		this.weight = KVFieldCreatorConstants.DEFAULT_WEIGHT;
		if (weightData != null) {
			try {
				this.weight = Float.parseFloat(weightData);
			} catch (NumberFormatException numberFormatException) {
				LOGGER.error("Couldn't parse weight value, setting it to its default value 1.", numberFormatException);
			}
		}
	}

	/**
	 * Gets the kv Weight.
	 * 
	 * @return float value.
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 * 
	 * @param weight float value.
	 */
	public void setWeight(final float weight) {
		this.weight = weight;
	}

	/**
	 * Finds the max value of order currently present and sets new order as incremented by one.
	 * 
	 * @param kvList {@link KVExtraction}>} list of currently present kv extraction.
	 * @return returns {@link Integer} containing needed.
	 */
	private Integer getNextKVFieldOrderNo(final List<KVExtraction> kvList) {
		Integer kvFieldOrderNo = null;
		if (CollectionUtils.isNotEmpty(kvList)) {
			Integer maxKVOrder = Integer.MIN_VALUE;
			for (KVExtraction kvType : kvList) {
				Integer tempOrderNo = kvType.getOrderNo();
				if (null != tempOrderNo && tempOrderNo > maxKVOrder) {
					maxKVOrder = tempOrderNo;
				}
			}
			if (maxKVOrder > (Integer.MAX_VALUE - KVFieldCreatorConstants.FIELD_ORDER_DIFFERENCE)) {

				// setting the field order to be empty if generated field order is not in integer range
				kvFieldOrderNo = null;
			} else {
				Integer newKVOrder = maxKVOrder + KVFieldCreatorConstants.FIELD_ORDER_DIFFERENCE;
				kvFieldOrderNo = newKVOrder;
			}
		} else {
			kvFieldOrderNo = KVFieldCreatorConstants.INITIAL_FIELD_ORDER_NUMBER;
		}
		return kvFieldOrderNo;
	}

}
