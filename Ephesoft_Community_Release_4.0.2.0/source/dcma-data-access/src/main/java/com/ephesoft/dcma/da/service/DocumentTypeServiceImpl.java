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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ephesoft.dcma.core.common.WidgetType;
import com.ephesoft.dcma.core.service.DataAccessService;
import com.ephesoft.dcma.da.dao.DocumentTypeDao;
import com.ephesoft.dcma.da.domain.AdvancedKVExtraction;
import com.ephesoft.dcma.da.domain.AdvancedKVExtractionDetail;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.FieldType;
import com.ephesoft.dcma.da.domain.FunctionKey;
import com.ephesoft.dcma.da.domain.KVExtraction;
import com.ephesoft.dcma.da.domain.PageType;
import com.ephesoft.dcma.da.domain.RegexValidation;
import com.ephesoft.dcma.da.domain.TableColumnExtractionRule;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableExtractionRule;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.domain.TableRuleInfo;
import com.ephesoft.dcma.util.CollectionUtil;

/**
 * This is a database service to read data required by Document Type Service .
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.DocumentTypeService
 */
@Service
public class DocumentTypeServiceImpl extends DataAccessService implements DocumentTypeService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DocumentTypeServiceImpl.class);

	@Autowired
	private DocumentTypeDao documentTypeDao;

	/**
	 * An api to fetch all DocumentType by document type name.
	 * 
	 * @param docTypeName String
	 * @return List<DocumentType>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<DocumentType> getDocTypeByDocTypeName(String docTypeName) {
		List<DocumentType> documentType = null;
		if (null == docTypeName || "".equals(docTypeName)) {
			LOGGER.info("Input docTypeName is null or empty.");
		} else {
			documentType = documentTypeDao.getDocTypeByDocTypeName(docTypeName);
		}
		return documentType;
	}

	/**
	 * An api to insert the documentType object.
	 * 
	 * @param documentType DocumentType
	 */
	@Transactional
	@Override
	public void insertDocumentType(DocumentType documentType) {
		if (null == documentType) {
			LOGGER.info("Document object is null.");
		} else {
			documentTypeDao.insertDocumentType(documentType);
		}
	}

	/**
	 * An api to update the documentType object.
	 * 
	 * @param documentType DocumentType
	 */
	@Transactional
	@Override
	public void updateDocumentType(DocumentType documentType) {
		if (null == documentType) {
			LOGGER.info("Document object is null.");
		} else {
			documentTypeDao.updateDocumentType(documentType);
		}
	}

	/**
	 * An api to remove the documentType object.
	 * 
	 * @param documentType DocumentType
	 */
	@Transactional
	@Override
	public void removeDocumentType(DocumentType documentType) {
		if (null == documentType) {
			LOGGER.info("Document object is null.");
		} else {
			documentTypeDao.removeDocumentType(documentType);
		}
	}

	/**
	 * An api to fetch all DocumentType by batch instance id.
	 * 
	 * @param batchInstanceIdentifier String
	 * @return List<DocumentType>
	 */
	@Transactional
	@Override
	public List<DocumentType> getDocTypeByBatchInstanceIdentifier(String batchInstanceIdentifier) {
		List<DocumentType> documentType = null;
		if (null == batchInstanceIdentifier) {
			LOGGER.info("Input batchInstanceIdentifier is null.");
		} else {
			documentType = documentTypeDao.getDocTypeByBatchInstanceIdentifier(batchInstanceIdentifier);
		}
		return documentType;
	}

	/**
	 * An api to fetch all DocumentType by batch class id.
	 * 
	 * @param batchClassID Long
	 * @param firstIndex int
	 * @param maxResults int
	 * @return List<DocumentType>
	 */
	@Transactional
	@Override
	public List<DocumentType> getDocTypeByBatchClassIdentifier(final String batchClassIdentifier, final int firstIndex,
			final int maxResults) {
		List<DocumentType> documentType = null;
		if (null == batchClassIdentifier) {
			LOGGER.info("Input batchClassID is null.");
		} else {
			documentType = documentTypeDao.getDocTypeByBatchClassIdentifier(batchClassIdentifier, firstIndex, maxResults);
		}
		return documentType;
	}

	@Override
	public DocumentType getDocTypeByIdentifier(String identifier) {
		DocumentType returnVal = null;
		if (identifier == null || identifier.isEmpty()) {
			LOGGER.warn("Empty identifier for document type");
		} else {
			DocumentType documentType = null;
			documentType = documentTypeDao.getDocTypeByIdentifier(identifier);
			returnVal = documentType;
		}
		return returnVal;
	}

	@Override
	public void evict(DocumentType documentType) {
		documentTypeDao.evict(documentType);
	}

	@Transactional
	@Override
	public List<DocumentType> getDocTypeByBatchClassIdentifier(final String batchClassIdentifier) {
		List<DocumentType> documentType = null;
		if (null == batchClassIdentifier) {
			LOGGER.info("Input batchClassID is null.");
		} else {
			documentType = documentTypeDao.getDocTypeByBatchClassIdentifier(batchClassIdentifier);
		}
		return documentType;
	}

	@Transactional
	@Override
	public DocumentType getDocTypeByBatchClassAndDocTypeName(String batchClassIdentifier, String docTypeName) {
		DocumentType documentType = null;
		if (null == batchClassIdentifier) {
			LOGGER.info("Input batchClassID is null.");
		} else {
			documentType = documentTypeDao.getDocTypeByBCIdandDocTypeName(batchClassIdentifier, docTypeName);
			if (documentType != null) {
				List<FieldType> fieldTypes = documentType.getFieldTypes();
				for (FieldType fieldType : fieldTypes) {
					LOGGER.debug(fieldType.getDescription());
				}
			}
		}
		return documentType;
	}

	/**
	 * Copies following entries of existing {@link DocumentType} to the new {@link DocumentType}:-
	 * <ul>
	 * <li>Copies field type to existing {@link DocumentType} to the new {@link DocumentType}.
	 * <li>Copies page type of existing {@link DocumentType} to the new {@link DocumentType}.
	 * <li>Copies table info of existing {@link DocumentType} to the new {@link DocumentType}
	 * <li>Copies function keys of existing {@link DocumentType} to the new {@link DocumentType}
	 * </ul>
	 * 
	 * @param existingDocType {@link DocumentType} existing document from which function keys are to be copied.
	 * @param newDocumentType {@link DocumentType} new document to which function keys are to be copied.
	 * @return {@link DocumentType}copy of existing document type with all parameters set or newDocumentType input parameter is
	 *         returned if existing document type is null.
	 */
	public DocumentType copyDocumentType(final DocumentType existingDocType, final DocumentType newDocumentType) {
		if (null != existingDocType) {
			copyFieldTypes(existingDocType, newDocumentType);
			copyPageTypes(existingDocType, newDocumentType);
			copyTableInfo(existingDocType, newDocumentType);
			copyFunctionKeys(existingDocType, newDocumentType);
		}
		return newDocumentType;
	}

	/**
	 * Copies the List of function keys of existing {@link DocumentType} to the new {@link DocumentType}
	 * 
	 * @param existingDocType {@link DocumentType} existing document from which function keys are to be copied.
	 * @param newDocumentType {@link DocumentType} new document to which function keys are to be copied
	 */
	private void copyFunctionKeys(final DocumentType existingDocType, final DocumentType newDocumentType) {
		final List<FunctionKey> existingList = existingDocType.getFunctionKeys();
		final List<FunctionKey> newList = new ArrayList<FunctionKey>();
		FunctionKey newKey;
		for (final FunctionKey existingKey : existingList) {
			newKey = new FunctionKey();
			newKey.setDocType(newDocumentType);
			newKey.setEntityState(existingKey.getEntityState());
			newKey.setIdentifier(existingKey.getIdentifier());
			newKey.setMethodName(existingKey.getMethodName());
			newKey.setShortcutKeyname(existingKey.getShortcutKeyname());
			newKey.setUiLabel(existingKey.getUiLabel());
			newList.add(newKey);
		}
		newDocumentType.setFunctionKeys(newList);
	}

	/**
	 * Copies the List of page types of existing {@link DocumentType} to the new {@link DocumentType}.
	 * 
	 * @param existingDocumentType {@link DocumentType} existing document from which function keys are to be copied.
	 * @param newDocumentType {@link DocumentType} new document to which function keys are to be copied
	 */
	private void copyPageTypes(final DocumentType existingDocumentType, final DocumentType newDocumentType) {
		final List<PageType> pages = existingDocumentType.getPages();
		final List<PageType> newPageTypes = new ArrayList<PageType>();
		PageType newPageType;
		for (PageType pageType : pages) {
			newPageType = new PageType();
			newPageType.setDocType(newDocumentType);
			newPageType.setDescription(pageType.getDescription());
			newPageType.setEntityState(pageType.getEntityState());
			newPageType.setName(pageType.getName());
			newPageType.setIdentifier(pageType.getIdentifier());
			newPageTypes.add(pageType);
		}
		newDocumentType.setPages(newPageTypes);
	}

	/**
	 * Copies the List of field types of existing {@link DocumentType} to the new {@link DocumentType}.
	 * 
	 * @param existingDocType {@link DocumentType} existing document from which field types are to be copied.
	 * @param newDocumentType {@link DocumentType} new document to which field types are copied.
	 */
	private void copyFieldTypes(final DocumentType existingDocType, final DocumentType newDocumentType) {
		final List<FieldType> fieldTypes = existingDocType.getFieldTypes();
		final List<FieldType> newFieldTypeLst = new ArrayList<FieldType>();
		FieldType newFieldType;
		for (final FieldType fieldType : fieldTypes) {
			newFieldType = new FieldType();
			newFieldType.setName(fieldType.getName());
			newFieldType.setDescription(fieldType.getDescription());
			newFieldType.setDataType(fieldType.getDataType());
			newFieldType.setFieldOrderNumber(fieldType.getFieldOrderNumber());
			newFieldType.setSampleValue(fieldType.getSampleValue());
			newFieldType.setBarcodeType(fieldType.getBarcodeType());
			newFieldType.setHidden(fieldType.isHidden());
			newFieldType.setReadOnly(fieldType.getIsReadOnly());
			newFieldType.setFieldValueChangeScriptEnabled(fieldType.isFieldValueChangeScriptEnabled());
			newFieldType.setDocType(newDocumentType);
			newFieldType.setRegexListingSeparator(fieldType.getRegexListingSeparator());
			newFieldType.setEntityState(fieldType.getEntityState());
			newFieldType.setFieldOptionValueList(fieldType.getFieldOptionValueList());
			newFieldType.setIdentifier(fieldType.getIdentifier());
			newFieldType.setPattern(fieldType.getPattern());
			newFieldType.setOcrConfidenceThreshold(fieldType.getOcrConfidenceThreshold());
			if (null != fieldType.getWidgetType()) {
				fieldType.setWidgetType(fieldType.getWidgetType());
			} else {
				fieldType.setWidgetType(WidgetType.TEXT);
			}
			newFieldType.setCategoryName(fieldType.getCategoryName());
			copyKVExtractionFields(fieldType, newFieldType);
			copyRegex(fieldType, newFieldType);
			newFieldTypeLst.add(newFieldType);
		}
		newDocumentType.setFieldTypes(newFieldTypeLst);
	}

	/**
	 * Copies the List of KV Extraction fields of existing {@link FieldType} to the new {@link FieldType}.
	 * 
	 * @param existingDocKVfields {@link FieldType} existing document from which KV extraction fields are to be copied.
	 * @param newDocKVfields {@link FieldType} new document to which KV extraction fields are copied.
	 */
	private void copyKVExtractionFields(final FieldType existingDocKVfields, final FieldType newDocKVfields) {

		final List<KVExtraction> kvExtractionFields = existingDocKVfields.getKvExtraction();
		final List<KVExtraction> newKvExtractionLst = new ArrayList<KVExtraction>();
		KVExtraction newKVExtract;
		for (final KVExtraction kvExtraction : kvExtractionFields) {
			newKVExtract = new KVExtraction();
			newKVExtract.setKeyPattern(kvExtraction.getKeyPattern());
			newKVExtract.setWeight(kvExtraction.getWeight());

			// Setting the fuzzy match threshold value for key pattern.
			newKVExtract.setKeyFuzziness(kvExtraction.getKeyFuzziness());

			newKVExtract.setValuePattern(kvExtraction.getValuePattern());
			newKVExtract.setLocationType(kvExtraction.getLocationType());
			newKVExtract.setNoOfWords(kvExtraction.getNoOfWords());
			newKVExtract.setFetchValue(kvExtraction.getFetchValue());
			newKVExtract.setPageValue(kvExtraction.getPageValue());
			newKVExtract.setMultiplier(kvExtraction.getMultiplier());
			newKVExtract.setYoffset(kvExtraction.getYoffset());
			newKVExtract.setXoffset(kvExtraction.getXoffset());
			newKVExtract.setFieldType(newDocKVfields);
			newKVExtract.setDistance(kvExtraction.getDistance());
			newKVExtract.setKeyPattern(kvExtraction.getKeyPattern());
			newKVExtract.setUseExistingKey(kvExtraction.isUseExistingKey());
			newKVExtract.setWidth(kvExtraction.getWidth());
			newKVExtract.setLength(kvExtraction.getLength());
			newKVExtract.setEntityState(kvExtraction.getEntityState());
			copyAdvancedKVExtraction(kvExtraction, newKVExtract);
			newKvExtractionLst.add(newKVExtract);
		}
		newDocKVfields.setKvExtraction(newKvExtractionLst);
	}

	/**
	 * Copies the List of Advanced KV Extraction fields of existing {@link KVExtraction} to the new {@link KVExtraction}.
	 * 
	 * @param existingkvExtract {@link KVExtraction} existing document from which Advanced KV extraction fields are to be copied.
	 * @param newKVExtract {@link KVExtraction} new document to which Advanced KV extraction fields are copied.
	 */
	private void copyAdvancedKVExtraction(final KVExtraction existingkvExtract, final KVExtraction newKVExtract) {
		final AdvancedKVExtraction advancedKVExtraction = existingkvExtract.getAdvancedKVExtraction();
		AdvancedKVExtraction newAdvKVExtract;
		if (advancedKVExtraction != null) {
			newAdvKVExtract = new AdvancedKVExtraction();
			newAdvKVExtract.setKeyX0Coord(advancedKVExtraction.getKeyX0Coord());
			newAdvKVExtract.setKeyX1Coord(advancedKVExtraction.getKeyX1Coord());
			newAdvKVExtract.setKeyY0Coord(advancedKVExtraction.getKeyY0Coord());
			newAdvKVExtract.setKeyY1Coord(advancedKVExtraction.getKeyY1Coord());
			newAdvKVExtract.setValueX0Coord(advancedKVExtraction.getValueX0Coord());
			newAdvKVExtract.setValueX1Coord(advancedKVExtraction.getValueX1Coord());
			newAdvKVExtract.setValueY0Coord(advancedKVExtraction.getValueY0Coord());
			newAdvKVExtract.setValueY1Coord(advancedKVExtraction.getValueY1Coord());
			newAdvKVExtract.setSelectedImageDisplayName(advancedKVExtraction.getSelectedImageDisplayName());
			newAdvKVExtract.setSelectedImageName(advancedKVExtraction.getSelectedImageName());
			newAdvKVExtract.setEntityState(advancedKVExtraction.getEntityState());
			newAdvKVExtract.setKvExtraction(newKVExtract);
			newKVExtract.setAdvancedKVExtraction(newAdvKVExtract);
			// Change for ADV KV Detailing.
			copyAdvancedKVExtractionDetail(advancedKVExtraction, newAdvKVExtract);
		}

	}

	// Method for ADV KV Detailing.
	public static void copyAdvancedKVExtractionDetail(AdvancedKVExtraction existingAdvancedKVExtraction,
			AdvancedKVExtraction newAdvancedKVExtraction) {
		final List<AdvancedKVExtractionDetail> advancedKVExtractionDetails = existingAdvancedKVExtraction
				.getAdvancedKVExtractionDetail();
		AdvancedKVExtractionDetail newDetail;
		List<AdvancedKVExtractionDetail> newDetails = new ArrayList<AdvancedKVExtractionDetail>();
		if (!CollectionUtil.isEmpty(advancedKVExtractionDetails)) {
			for (AdvancedKVExtractionDetail detail : advancedKVExtractionDetails) {
				newDetail = new AdvancedKVExtractionDetail();
				newDetail.setCreationDate(detail.getCreationDate());
				newDetail.setEntityState(detail.getEntityState());
				newDetail.setFileName(detail.getFileName());
				newDetail.setMultiPage(detail.isMultiPage());
				newDetail.setPageCount(detail.getPageCount());
				newDetail.setAdvancedKVExtraction(newAdvancedKVExtraction);
				newDetails.add(newDetail);
			}
			newAdvancedKVExtraction.setAdvancedKVExtractionDetail(newDetails);
		}

	}

	/**
	 * Copies the List of Regex patterns existing {@link FieldType} to the new {@link FieldType}.
	 * 
	 * @param existingFieldType {@link FieldType} existing document from which regex patterns are to be copied.
	 * @param newFieldType {@link FieldType} new document to which regex patterns are copied.
	 */
	private void copyRegex(final FieldType existingFieldType, final FieldType newFieldType) {
		final List<RegexValidation> regexValidations = existingFieldType.getRegexValidation();
		final List<RegexValidation> regexValidationsList = new ArrayList<RegexValidation>();
		RegexValidation newRegex;
		for (final RegexValidation regexValidation : regexValidations) {
			newRegex = new RegexValidation();
			newRegex.setPattern(regexValidation.getPattern());
			regexValidationsList.add(newRegex);
		}
		newFieldType.setRegexValidation(regexValidationsList);
	}

	/**
	 * Copies the List of Table information existing {@link DocumentType} to the new {@link DocumentType}.
	 * 
	 * @param existingDocType {@link DocumentType} existing document from which Table information are to be copied.
	 * @param newDocumentType {@link DocumentType} new document to which Table information are copied.
	 */
	private void copyTableInfo(final DocumentType existingDocType, final DocumentType newDocType) {
		final List<TableInfo> tableInfos = existingDocType.getTableInfos();
		final List<TableInfo> newTableInfoLst = new ArrayList<TableInfo>();
		TableInfo newTableInfo;
		for (final TableInfo existingtableInfo : tableInfos) {
			newTableInfo = new TableInfo();
			newTableInfo.setName(existingtableInfo.getName());
			newTableInfo.setNumberOfRows(existingtableInfo.getNumberOfRows());
			copyTableColumnsInfo(existingtableInfo, newTableInfo);
			newTableInfo.setDisplayImage(existingtableInfo.getDisplayImage());
			newTableInfo.setDocType(newDocType);
			newTableInfo.setRuleOperator(existingtableInfo.getRuleOperator());
			// JIRA-11360
			newTableInfo.setCurrencyCode(existingtableInfo.getCurrencyCode());
			newTableInfo.setEntityState(existingtableInfo.getEntityState());
			newTableInfo.setRuleOperator(existingtableInfo.getRuleOperator());
			// Copies the table extraction rule details.
			copyTableExtractionRules(existingtableInfo, newTableInfo);
			// Copies the table validation rule details.
			copyTableValidationRules(existingtableInfo, newTableInfo);

			newTableInfoLst.add(newTableInfo);
		}
		newDocType.setTableInfos(newTableInfoLst);
	}

	/**
	 * Copies the List of Table column information existing {@link TableInfo} to the new {@link TableInfo}.
	 * 
	 * @param existingtableInfo {@link TableInfo} existing document from which Table information is to be copied.
	 * @param newTableInfo {@link TableInfo} new document to which Table column information is copied.
	 */
	private void copyTableColumnsInfo(final TableInfo existingtableInfo, final TableInfo newTableInfo) {

		final List<TableColumnsInfo> tableColInfos = existingtableInfo.getTableColumnsInfo();
		final List<TableColumnsInfo> tableColInfoList = new ArrayList<TableColumnsInfo>();
		TableColumnsInfo newColInfo;
		for (final TableColumnsInfo tableColInfo : tableColInfos) {
			newColInfo = new TableColumnsInfo();
			newColInfo.setColumnName(tableColInfo.getColumnName());
			newColInfo.setColumnDescription(tableColInfo.getColumnDescription());
			newColInfo.setAlternateValues(tableColInfo.getAlternateValues());
			newColInfo.setValidationPattern(tableColInfo.getValidationPattern());
			newColInfo.setTableInfo(newTableInfo);
			// newColInfo.setOrderNo(tableColInfo.getOrderNo());
			tableColInfoList.add(newColInfo);
		}
		newTableInfo.setTableColumnsInfo(tableColInfoList);
	}

	/**
	 * Copies the table extraction rule details from existing table to the new table.
	 * 
	 * @param existingtableInfo {@link TableInfo} represents the Object from which table details need to be copied.
	 * @param newTableInfo {@link TableInfo} represents the Object to which table details need to be copied.
	 */
	private void copyTableExtractionRules(final TableInfo existingtableInfo, final TableInfo newTableInfo) {

		final List<TableExtractionRule> tableExtractionRules = existingtableInfo.getTableExtractionRules();
		if (!CollectionUtil.isEmpty(tableExtractionRules)) {
			final List<TableExtractionRule> newTableExtractionRules = new ArrayList<TableExtractionRule>(tableExtractionRules.size());
			for (final TableExtractionRule tableExtractionRule : tableExtractionRules) {
				if (null != tableExtractionRule) {
					final TableExtractionRule newTableExtractionRule = new TableExtractionRule();
					newTableExtractionRule.setStartPattern(tableExtractionRule.getStartPattern());
					newTableExtractionRule.setEndPattern(tableExtractionRule.getEndPattern());
					newTableExtractionRule.setTableAPI(tableExtractionRule.getTableAPI());
					newTableExtractionRule.setRuleName(tableExtractionRule.getRuleName());
					newTableExtractionRule.setTableInfo(newTableInfo);
					newTableExtractionRule.setTableColumnExtractionRules(copyTableColumnExtractionRule(tableExtractionRule,
							newTableExtractionRule));
					newTableExtractionRules.add(newTableExtractionRule);
				}
			}
			newTableInfo.setTableExtractionRules(newTableExtractionRules);
		}
	}

	/**
	 * Copies the table column extraction rule details from the table extraction rule object passed.
	 * 
	 * @param tableExtractionRule {@link TableExtractionRule} represnts the table extraction rule object from which table column
	 *            extraction rule details needs to be copied.
	 * @return the copied list of table column extraction rules.
	 */
	private List<TableColumnExtractionRule> copyTableColumnExtractionRule(final TableExtractionRule tableExtractionRule,
			final TableExtractionRule newTableExtractionRule) {
		List<TableColumnExtractionRule> newListColumnExtractionRules = null;
		if (null != tableExtractionRule) {
			List<TableColumnExtractionRule> tableColumnExtractionRules = tableExtractionRule.getTableColumnExtractionRules();
			if (!CollectionUtil.isEmpty(tableColumnExtractionRules)) {
				newListColumnExtractionRules = new ArrayList<TableColumnExtractionRule>(tableColumnExtractionRules.size());
				for (TableColumnExtractionRule tableColumnExtractionRule : tableColumnExtractionRules) {
					if (null != tableColumnExtractionRule) {
						final TableColumnExtractionRule newTableColumnExtractionRule = new TableColumnExtractionRule();
						newTableColumnExtractionRule.setBetweenLeft(tableColumnExtractionRule.getBetweenLeft());
						newTableColumnExtractionRule.setBetweenRight(tableColumnExtractionRule.getBetweenRight());
						newTableColumnExtractionRule.setColumnPattern(tableColumnExtractionRule.getColumnPattern());
						newTableColumnExtractionRule
								.setExtractedDataColumnName(tableColumnExtractionRule.getExtractedDataColumnName());
						newTableColumnExtractionRule.setRequired(tableColumnExtractionRule.isRequired());
						newTableColumnExtractionRule.setMandatory(tableColumnExtractionRule.isMandatory());
						newTableColumnExtractionRule.setTableExtractionRuleInfo(newTableExtractionRule);
						newTableColumnExtractionRule.setCurrency(tableColumnExtractionRule.isCurrency());
						newTableColumnExtractionRule.setColumnHeaderPattern(tableColumnExtractionRule.getColumnHeaderPattern());
						newTableColumnExtractionRule.setOrderNo(tableColumnExtractionRule.getOrderNo());
						newTableColumnExtractionRule.setColumnStartCoordinate(tableColumnExtractionRule.getColumnStartCoordinate());
						newTableColumnExtractionRule.setColumnEndCoordinate(tableColumnExtractionRule.getColumnEndCoordinate());
						newTableColumnExtractionRule.setColumnCoordinateY0(tableColumnExtractionRule.getColumnCoordinateY0());
						newTableColumnExtractionRule.setColumnName(tableColumnExtractionRule.getColumnName());
						newTableColumnExtractionRule.setColumnCoordinateY1(tableColumnExtractionRule.getColumnCoordinateY1());
						newTableColumnExtractionRule.setColumnName(tableColumnExtractionRule.getColumnName());
						newListColumnExtractionRules.add(newTableColumnExtractionRule);
					}
				}
			}
		}
		return newListColumnExtractionRules;
	}

	/**
	 * Copies the List of Table Validation rule information existing {@link TableInfo} to the new {@link TableInfo}.
	 * 
	 * @param existingtableValidationInfo {@link TableInfo} existing document from which Table information is to be copied.
	 * @param newTableInfo {@link TableInfo} new document to which Table column information is copied.
	 */
	private void copyTableValidationRules(final TableInfo existingTableInfo, final TableInfo newTableInfo) {

		final List<TableRuleInfo> tableValidationInfos = existingTableInfo.getTableRuleInfo();
		final List<TableRuleInfo> tableValidationInfoList = new ArrayList<TableRuleInfo>();
		TableRuleInfo newValRuleInfo;
		for (final TableRuleInfo tableValInfo : tableValidationInfos) {
			newValRuleInfo = new TableRuleInfo();
			newValRuleInfo.setRule(tableValInfo.getRule());
			newValRuleInfo.setDescription(tableValInfo.getDescription());
			tableValidationInfoList.add(newValRuleInfo);
		}
		newTableInfo.setTableRuleInfo(tableValidationInfoList);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public DocumentType mergeDocumentType(DocumentType documentType) {
		DocumentType newDocumentType = null;
		if (null == documentType) {
			LOGGER.info("Document object is null.");
		} else {
			newDocumentType = documentTypeDao.merge(documentType);
		}
		return newDocumentType;
	}
}
