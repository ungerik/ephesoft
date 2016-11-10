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

package com.ephesoft.dcma.gwt.core.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Column.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.gwt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.dcma.gwt.core.client.util.ReverseIterable;
import com.ephesoft.dcma.gwt.core.shared.util.CollectionUtil;
import com.google.gwt.user.client.rpc.IsSerializable;

public class BatchDTO implements IsSerializable {

	private Batch batch;

	private BatchInstanceStatus batchInstanceStatus;

	private String baseHTTPUrl;

	private String isValidationScriptEnabled = "OFF";
	private String fieldValueChangeScriptSwitchState = "OFF";
	private String fuzzySearchSwitchState = "ON";
	private String suggestionBoxSwitchState = "OFF";
	private String externalApplicationSwitchState = "OFF";
	private Map<String, String> urlAndShortcutMap = null;
	private Map<String, String> dimensionsForPopUp = null;
	private Map<String, String> urlAndTitleMap = null;
	private boolean isCtrlQEnabled;

	/**
	 * to hold status of table extarction suggestion box switch
	 */
	private boolean tableTextSuggestionSwitchStatus = true;

	/**
	 * getter for tableExtractionSuggestionSwitchStatus
	 * 
	 * @return
	 */
	public boolean isTableTextSuggestionSwitchStatus() {
		return tableTextSuggestionSwitchStatus;
	}

	/**
	 * setter for tableExtractionSuggestionSwitchStatus
	 * 
	 * @param tableExtractionSuggestionSwitchStatus
	 */
	public void setTableTextSuggestionSwitchStatus(boolean tableTextSuggestionSwitchStatus) {
		this.tableTextSuggestionSwitchStatus = tableTextSuggestionSwitchStatus;
	}

	/**
	 * tableInfoMap {@link Map} map containing document and tableinfo dtos.
	 */
	private Map<String, List<TableInfoDTO>> tableInfoMap = null;
	private String fuzzySearchPopUpXDimension = "500";
	private String fuzzySearchPopUpYDimension = "350";
	private Integer realUpdateInterval = 5;
	private Integer preloadedImageCount = 3;
	// value '1' signifies document to be displayed in tree view
	private int docDisplayName = 1;
	private boolean defaultReviewPanelStateOpen = true;
	// Added for default check box for retain documents fields or not while split
	private boolean isDocFieldEnabled = true;
	// Added for default check box for retain table fields or not while split
	private boolean isTableFieldEnabled = true;

	/**
	 * Switch for sticky index fields feature.
	 */
	private boolean stickyIndexFieldSwitch;

	/**
	 * Instance of {@link String} that holds
	 */
	private String indexFieldValueSeparator;

	private static final String PIXELS = "px";

	/** Stores the status of insert table row script switch. */
	private String isInsertTableRowScriptEnabled = "OFF";

	/** Contains the arithematic operators that may occur in an expression */
	private String arithmeticOperatorString = null;

	/**
	 * Contains the invalid characters that may occur in a table cell value.
	 */
	private String invalidCharacters = null;

	public BatchDTO() {
	}

	public BatchDTO(Batch batch, String baseHTTPUrl) {
		this.batch = batch;
		this.baseHTTPUrl = baseHTTPUrl;
	}

	public BatchDTO(Batch batch, String baseHTTPUrl, String isValidationScriptEnabled, String isFieldValueChangeScriptEnabled,
			String isFuzzySearchEnabled, String suggestionBoxSwitchState, String externalApplicationSwitchState,
			Map<String, String> urlAndShortcutMap, Map<String, String> dimensionsForPopUp, Map<String, String> urlAndTitleMap,
			String fuzzySearchPopUpXDimension, String fuzzySearchPopUpYDimension, String updateIntervalInStringForm,
			String preloadedImageCountString, BatchInstanceStatus batchInstanceStatus, int docDisplayName,
			String defaultReviewPanelState, Map<String, List<TableInfoDTO>> tableInfoMap, String isInsertTableRowScriptEnabled,
			String invalidCharacters, String arithmeticOperatorString) {
		this.batch = batch;
		this.baseHTTPUrl = baseHTTPUrl;
		this.setDocDisplayName(docDisplayName);
		this.batchInstanceStatus = batchInstanceStatus;
		if (null != isValidationScriptEnabled) {
			setIsValidationScriptEnabled(isValidationScriptEnabled);
		}
		if (null != isFuzzySearchEnabled) {
			setFuzzySearchSwitchState(isFuzzySearchEnabled);
		}
		if (null != isFieldValueChangeScriptEnabled) {
			setFieldValueChangeScriptSwitchState(isFieldValueChangeScriptEnabled);
		}
		if (null != suggestionBoxSwitchState) {
			setSuggestionBoxSwitchState(suggestionBoxSwitchState);
		}

		if (null != externalApplicationSwitchState) {
			setExternalApplicationSwitchState(externalApplicationSwitchState);
		}
		if (null != fuzzySearchPopUpXDimension) {
			try {
				if (Integer.parseInt(fuzzySearchPopUpXDimension) > 0) {
					setFuzzySearchPopUpXDimension(fuzzySearchPopUpXDimension + PIXELS);
				}
			} catch (Exception e) {
			}
		}
		if (null != fuzzySearchPopUpYDimension) {
			try {
				if (Integer.parseInt(fuzzySearchPopUpYDimension) > 0) {
					setFuzzySearchPopUpYDimension(fuzzySearchPopUpYDimension + PIXELS);
				}
			} catch (Exception e) {
			}
		}
		if (null != updateIntervalInStringForm) {
			try {
				int updateInterval = Integer.parseInt(updateIntervalInStringForm);
				if (updateInterval > 0) {
					setRealUpdateInterval(updateInterval);
				}
			} catch (Exception e) {
			}
		}
		if (null != preloadedImageCountString) {
			try {
				int preloadedImageCount = Integer.parseInt(preloadedImageCountString);
				if (preloadedImageCount > 0) {
					setPreloadedImageCount(preloadedImageCount);
				}
			} catch (Exception e) {
			}
		}
		if (null != defaultReviewPanelState) {
			if (defaultReviewPanelState.equalsIgnoreCase(BatchConstants.FALSE)) {
				defaultReviewPanelStateOpen = false;
			}
		}
		if (null != isInsertTableRowScriptEnabled) {
			this.isInsertTableRowScriptEnabled = isInsertTableRowScriptEnabled;
		}
		this.invalidCharacters = invalidCharacters;
		this.arithmeticOperatorString = arithmeticOperatorString;
		setUrlAndShortcutMap(urlAndShortcutMap);
		setDimensionsForPopUp(dimensionsForPopUp);
		setUrlAndTitleMap(urlAndTitleMap);
		setTableInfoMap(tableInfoMap);
	}

	public Map<String, String> getUrlAndTitleMap() {
		return urlAndTitleMap;
	}

	public void setUrlAndTitleMap(Map<String, String> urlAndTitleMap) {
		this.urlAndTitleMap = urlAndTitleMap;
	}

	public Batch getBatch() {
		return batch;
	}

	public String getAbsoluteURLFor(String fileName) {
		return this.baseHTTPUrl + "/" + fileName;
	}

	public String getAbsoluteURLForRotatedImage(String fileName, String direction) {
		return this.baseHTTPUrl + "/" + direction + "/" + fileName;
	}

	/**
	 * Gets the status for ctrl Q.
	 * 
	 * @return Returns isCtrlQEnabled
	 */
	public boolean isCtrlQEnabled() {
		return isCtrlQEnabled;
	}

	/**
	 * Sets the status for ctrl Q.
	 * 
	 * @param isCtrlQEnabled
	 */
	public void setCtrlQEnabled(final boolean isCtrlQEnabled) {
		this.isCtrlQEnabled = isCtrlQEnabled;
	}

	public String getDocDisplayNameByDocId(String id) {
		Document document = getDocumentById(id);
		return document.getType();
	}

	public Document getDocumentById(String id) {
		List<Document> docs = batch.getDocuments().getDocument();
		for (Document document : docs) {
			if (document.getIdentifier().equals(id)) {
				return document;
			}
		}
		return null;
	}

	public Page getUpdatedPageInDocument(Document document, Page page) {
		List<Page> pagesInDoc = document.getPages().getPage();
		for (Page pageType : pagesInDoc) {
			if (pageType.getIdentifier().equals(page.getIdentifier())) {
				return pageType;
			}
		}
		return null;
	}

	public Document getDocumentForPage(Page page) {
		Batch batch = this.getBatch();
		List<Document> documents = batch.getDocuments().getDocument();
		List<Page> pageTypes = null;
		for (Document document : documents) {
			pageTypes = document.getPages().getPage();
			for (Page pageType : pageTypes) {
				if (pageType.getIdentifier().equals(page.getIdentifier())) {
					return document;
				}
			}
		}
		return null;
	}

	public Document getNextDocumentTo(Document document, boolean isError) {
		List<Document> documents = batch.getDocuments().getDocument();
		if (documents != null && !documents.isEmpty()) {
			Boolean reachedToInput = Boolean.FALSE;
			for (Document documentType : documents) {
				if (documentType.getIdentifier().equals(document.getIdentifier())) {
					reachedToInput = Boolean.TRUE;
					continue;
				}
				if (reachedToInput && (!isError || isErrorContained(documentType))) {
					return documentType;
				}
			}
			for (Document documentType : documents) {
				if (isErrorContained(documentType)) {
					return documentType;
				}
			}

			return document;
		}
		return null;
	}

	public Document getPreviousDocumentTo(Document document, boolean isError) {
		List<Document> documents = batch.getDocuments().getDocument();

		Boolean reachedToInput = Boolean.FALSE;
		ReverseIterable<Document> iter = new ReverseIterable<Document>(documents);
		int count = 0;
		for (Document documentType : iter) {
			count++;
			if (documentType.getIdentifier().equals(document.getIdentifier())) {
				reachedToInput = Boolean.TRUE;
				if (iter.iterator().hasNext()) {
					continue;
				}
			}
			if (reachedToInput && (!isError || isErrorContained(documentType))
					&& !documentType.getIdentifier().equalsIgnoreCase(document.getIdentifier())) {
				return documentType;
			}
		}
		iter = new ReverseIterable<Document>(documents);
		for (Document documentType : iter) {
			if (reachedToInput && (!isError || isErrorContained(documentType))) {
				return documentType;
			}
		}
		return null;
	}

	public boolean isErrorContained(Document document) {
		boolean reviewed = document.isReviewed();
		boolean validate = document.isValid();
		switch (batchInstanceStatus) {
			case READY_FOR_REVIEW:
				return !reviewed;
			case READY_FOR_VALIDATION:
				return !validate || !reviewed;
		}
		return true;
	}

	public String getIsValidationScriptEnabled() {
		return isValidationScriptEnabled;
	}

	public void setIsValidationScriptEnabled(String isValidationScriptEnabled) {
		this.isValidationScriptEnabled = isValidationScriptEnabled;
	}

	public boolean isBatchValidated() {
		boolean valid = true;
		List<Document> documents = batch.getDocuments().getDocument();
		if (batchInstanceStatus.equals(BatchInstanceStatus.READY_FOR_REVIEW)) {
			for (Document document : documents) {
				if (!document.isReviewed()) {
					valid = false;
					break;
				}
			}
		} else {
			for (Document document : documents) {
				if (!document.isReviewed() || !document.isValid()) {
					valid = false;
					break;
				}
			}
		}
		return valid;
	}

	public BatchInstanceStatus getBatchInstanceStatus() {
		return batchInstanceStatus;
	}

	public void setBatchInstanceStatus(BatchInstanceStatus batchInstanceStatus) {
		this.batchInstanceStatus = batchInstanceStatus;
	}

	public void setFuzzySearchSwitchState(String fuzzySearchSwitchState) {
		this.fuzzySearchSwitchState = fuzzySearchSwitchState;
	}

	public String getFuzzySearchSwitchState() {
		return fuzzySearchSwitchState;
	}

	public void setFieldValueChangeScriptSwitchState(String fieldValueChangeScriptSwitchState) {
		this.fieldValueChangeScriptSwitchState = fieldValueChangeScriptSwitchState;
	}

	public String getFieldValueChangeScriptSwitchState() {
		return fieldValueChangeScriptSwitchState;
	}

	public String getSuggestionBoxSwitchState() {
		return this.suggestionBoxSwitchState;
	}

	public void setSuggestionBoxSwitchState(String suggestionBoxSwitchState) {
		this.suggestionBoxSwitchState = suggestionBoxSwitchState;
	}

	public void setExternalApplicationSwitchState(String externalApplicationSwitchState) {
		this.externalApplicationSwitchState = externalApplicationSwitchState;
	}

	public String getExternalApplicationSwitchState() {
		return externalApplicationSwitchState;
	}

	public void setUrlAndShortcutMap(Map<String, String> urlAndShortcutMap) {
		this.urlAndShortcutMap = urlAndShortcutMap;
	}

	public Map<String, String> getUrlAndShortcutMap() {
		return urlAndShortcutMap;
	}

	public void setDimensionsForPopUp(Map<String, String> dimensionsForPopUp) {
		this.dimensionsForPopUp = dimensionsForPopUp;
	}

	public Map<String, String> getDimensionsForPopUp() {
		return dimensionsForPopUp;
	}

	public String getFuzzySearchPopUpXDimension() {
		return fuzzySearchPopUpXDimension;
	}

	public void setFuzzySearchPopUpXDimension(String fuzzySearchPopUpXDimension) {
		this.fuzzySearchPopUpXDimension = fuzzySearchPopUpXDimension;
	}

	public String getFuzzySearchPopUpYDimension() {
		return fuzzySearchPopUpYDimension;
	}

	public void setFuzzySearchPopUpYDimension(String fuzzySearchPopUpYDimension) {
		this.fuzzySearchPopUpYDimension = fuzzySearchPopUpYDimension;
	}

	public void setRealUpdateInterval(Integer realUpdateInterval) {
		this.realUpdateInterval = realUpdateInterval;
	}

	public Integer getRealUpdateInterval() {
		return realUpdateInterval;
	}

	public Integer getPreloadedImageCount() {
		return preloadedImageCount;
	}

	public void setPreloadedImageCount(Integer preloadedImageCount) {
		this.preloadedImageCount = preloadedImageCount;
	}

	public void setDocDisplayName(int docDisplayName) {
		this.docDisplayName = docDisplayName;
	}

	public int getDocDisplayName() {
		return docDisplayName;
	}

	public boolean isDefaultReviewPanelStateOpen() {
		return defaultReviewPanelStateOpen;
	}

	public void setDefaultReviewPanelStateOpen(boolean defaultReviewPanelStateOpen) {
		this.defaultReviewPanelStateOpen = defaultReviewPanelStateOpen;
	}

	/**
	 * Gets the separator to be used for index field value.
	 * 
	 * @return the indexFieldValueSeparator
	 */
	public String getIndexFieldValueSeparator() {
		if (StringUtil.isNullOrEmpty(indexFieldValueSeparator)) {
			indexFieldValueSeparator = BatchConstants.SPACE;
		}
		return indexFieldValueSeparator;
	}

	/**
	 * Sets the value of indexFieldValueSeparator.
	 * 
	 * @param indexFieldValueSeparator the indexFieldValueSeparator to set
	 */
	public void setIndexFieldValueSeparator(String indexFieldValueSeparator) {
		this.indexFieldValueSeparator = indexFieldValueSeparator;
	}

	/**
	 * Sets the table info map.
	 * 
	 * @param tableInfoMap {@link Map} the map to set
	 */
	public void setTableInfoMap(Map<String, List<TableInfoDTO>> tableInfoMap) {
		this.tableInfoMap = tableInfoMap;
	}

	public boolean isDocFieldEnabled() {
		return isDocFieldEnabled;
	}

	public void setDocFieldEnabled(boolean isDocFieldEnabled) {
		this.isDocFieldEnabled = isDocFieldEnabled;
	}

	public boolean isTableFieldEnabled() {
		return isTableFieldEnabled;
	}

	public void setTableFieldEnabled(boolean isTableFieldEnabled) {
		this.isTableFieldEnabled = isTableFieldEnabled;
	}

	/**
	 * Gets the sticky index field switch status.
	 * 
	 * @return Returns the sticky index field switch status.
	 */
	public boolean isStickyIndexFieldsEnabled() {
		return stickyIndexFieldSwitch;
	}

	/**
	 * Sets the sticky index field switch status.
	 * 
	 * @param stickyIndexFieldSwitch {@link String} sticky index field status to set.
	 */
	public void setStickyIndexFieldSwitch(final boolean stickyIndexFieldSwitch) {
		this.stickyIndexFieldSwitch = stickyIndexFieldSwitch;
	}

	/**
	 * Returns the height of table configured by user.
	 * 
	 * @param tableName {@link String} the name of table whose height is required
	 * @param documentName {@link String} the document name
	 * @return {@link int} the table height
	 */
	public int getTableHeight(String tableName, String documentName) {
		int tableHeight = CoreCommonConstants.DEFAULT_HEIGHT * CoreCommonConstants.CELL_HEIGHT;
		if (tableName != null && documentName != null && tableInfoMap != null) {
			List<TableInfoDTO> tableInfoList = this.tableInfoMap.get(documentName);
			if (tableInfoList != null) {
				for (TableInfoDTO currentTable : tableInfoList) {
					if (tableName.equalsIgnoreCase(currentTable.getName())) {
						tableHeight = Integer.parseInt(currentTable.getNumberOfRows()) * CoreCommonConstants.CELL_HEIGHT;
						break;
					}
				}
			}
		}
		return tableHeight;
	}

	/**
	 * Creates a map containing table columns and the alternate values specified.
	 * 
	 * @param selectedDocument {@link String} the document selected
	 * @return {@link Map} Map containing table columns and their alternate values.
	 */
	public Map<String, Map<String, AlternateValues>> getTableColumnVsAlternateValuesMap(String selectedDocument) {
		Map<String, Map<String, AlternateValues>> tableColumnMap = new HashMap<String, Map<String, AlternateValues>>();
		List<TableInfoDTO> tableInfoDTOs = null;
		if (tableInfoMap != null) {
			tableInfoDTOs = this.tableInfoMap.get(selectedDocument);
			if (tableInfoDTOs != null && !tableInfoDTOs.isEmpty()) {
				for (TableInfoDTO currentTableInfoDTO : tableInfoDTOs) {
					List<TableColumnInfoDTO> tableColumnInfoDTOs = currentTableInfoDTO.getColumnInfoDTOs();
					if (tableColumnInfoDTOs != null && !tableColumnInfoDTOs.isEmpty()) {
						Map<String, AlternateValues> columnAlternateValuesMap = new HashMap<String, AlternateValues>();
						for (TableColumnInfoDTO currentColumnInfoDTO : tableColumnInfoDTOs) {
							String columnName = currentColumnInfoDTO.getColumnName();
							AlternateValues alternateValues = new AlternateValues();
							List<Field> alternateValueList = alternateValues.getAlternateValue();
							String values = currentColumnInfoDTO.getAlternateValues();
							if (values != null && !values.isEmpty()) {
								CharSequence semiColon = ";";
								if (values.contains(semiColon)) {
									String[] valuesArray = values.split(semiColon.toString());
									for (int i = 0; i < valuesArray.length; i++) {
										Field field = new Field();
										field.setForceReview(Boolean.FALSE);
										field.setValue(valuesArray[i]);
										alternateValueList.add(field);
									}
								} else {
									Field field = new Field();
									field.setForceReview(Boolean.FALSE);
									field.setValue(values);
									alternateValueList.add(field);
								}
							}
							columnAlternateValuesMap.put(columnName, alternateValues);
						}
						tableColumnMap.put(currentTableInfoDTO.getName(), columnAlternateValuesMap);
					}
				}
			}
		}
		return tableColumnMap;
	}

	/**
	 * Returns validation patterns defined for each column of a given table of a document.
	 * 
	 * @param documentName {@link String} document name
	 * @param tableName {@link String} table name
	 * @return returns the map containing validation pattern against column name
	 */
	public Map<String, String> getColumnValidationPatterns(final String documentName, final String tableName) {
		Map<String, String> validationPatternMap = null;
		if (documentName != null && tableName != null) {
			validationPatternMap = new HashMap<String, String>();
			List<TableInfoDTO> tableInfoDTOs = this.tableInfoMap.get(documentName);
			if (tableInfoDTOs != null && !tableInfoDTOs.isEmpty()) {
				for (TableInfoDTO currentTableInfoDTO : tableInfoDTOs) {
					if (tableName.equalsIgnoreCase(currentTableInfoDTO.getName())) {
						processTableColumns(validationPatternMap, currentTableInfoDTO);
						break;
					}
				}
			}
		}
		return validationPatternMap;
	}

	/**
	 * Returns the status of insert table row script switch.
	 * 
	 * @return {@link String} OFF if the switch is off and ON if the switch is on
	 */
	public String getIsInsertTableRowScriptEnabled() {
		return isInsertTableRowScriptEnabled;
	}

	private void processTableColumns(final Map<String, String> validationPatternMap, final TableInfoDTO currentTableInfoDTO) {
		List<TableColumnInfoDTO> tableColumnInfoDTOs = currentTableInfoDTO.getColumnInfoDTOs();
		if (tableColumnInfoDTOs != null && !tableColumnInfoDTOs.isEmpty()) {
			for (TableColumnInfoDTO currentColumnInfoDTO : tableColumnInfoDTOs) {
				validationPatternMap.put(currentColumnInfoDTO.getColumnName(), currentColumnInfoDTO.getValidationPattern());
			}
		}
	}

	/**
	 * Returns the invalid characters that may occur in an expression.
	 * 
	 * @return {@link String} string containing invalid Characters.
	 */
	public String getInvalidCharacters() {
		return invalidCharacters;
	}

	/**
	 * Returns the arithmetic operators that may occur in a rule.
	 * 
	 * @return {@link String} string containing arithmetic operators.
	 */
	public String getArithmeticOperatorString() {
		return arithmeticOperatorString;
	}

	/**
	 * Returns List of Rules defined for the given table of a document.
	 * 
	 * @param documentName
	 * @param tableName
	 * @return
	 */
	public Map<String, Object> getRuleListForTable(final String documentName, final String tableName) {
		Map<String, Object> ruleTableMap = null;
		if (documentName != null && tableName != null) {
			ruleTableMap = new HashMap<String, Object>();
			List<TableInfoDTO> tableInfoDTOs = this.tableInfoMap.get(documentName);
			if (tableInfoDTOs != null && !tableInfoDTOs.isEmpty()) {
				for (TableInfoDTO currentTableInfoDTO : tableInfoDTOs) {
					if (tableName.equalsIgnoreCase(currentTableInfoDTO.getName())) {
						processTableRules(ruleTableMap, currentTableInfoDTO);
						break;
					}
				}
			}
		}
		return ruleTableMap;
	}

	/**
	 * Creates a map containing table and table rules defined for it for a given document.
	 * 
	 * @param selectedDocument {@link String} the document whose table rule information is required
	 * @return {@link Map} map containing table rules of the document
	 */
	public Map<String, List<String>> getTableVsRuleMap(String selectedDocument) {
		Map<String, List<String>> tableVsRuleMap = new HashMap<String, List<String>>();
		if (tableInfoMap != null && !tableInfoMap.isEmpty()) {
			List<TableInfoDTO> tableInfoDTOs = tableInfoMap.get(selectedDocument);
			if (!CollectionUtil.isEmpty(tableInfoDTOs)) {
				for (TableInfoDTO tableInfoDTO : tableInfoDTOs) {
					List<RuleInfoDTO> ruleInfoDTOs = tableInfoDTO.getRuleInfoDTOs();
					if (!CollectionUtil.isEmpty(ruleInfoDTOs)) {
						List<String> ruleList = new ArrayList<String>();
						for (RuleInfoDTO ruleInfoDTO : ruleInfoDTOs) {
							ruleList.add(ruleInfoDTO.getRule());
						}
						tableVsRuleMap.put(tableInfoDTO.getName(), ruleList);
					}
				}
			}
		}
		return tableVsRuleMap;
	}

	/**
	 * Creates a map containing table name and its corresponding rule descriptions for a given document.
	 * 
	 * @param selectedDocument the document whose table rule description map will be created.
	 * @return {@link Map} Map containing tables and their rule descriptions.
	 */
	public Map<String, Map<String, String>> getTableRuleVsDescriptionMap(final String selectedDocument) {
		Map<String, Map<String, String>> tableRuleVsDescriptionMap = new HashMap<String, Map<String, String>>();
		if (tableInfoMap != null && !tableInfoMap.isEmpty()) {
			List<TableInfoDTO> tableInfoDTOs = tableInfoMap.get(selectedDocument);
			if (!CollectionUtil.isEmpty(tableInfoDTOs)) {
				for (TableInfoDTO tableInfoDTO : tableInfoDTOs) {
					String tableName = tableInfoDTO.getName();
					List<RuleInfoDTO> ruleInfoDTOs = tableInfoDTO.getRuleInfoDTOs();
					if (!CollectionUtil.isEmpty(ruleInfoDTOs)) {
						Map<String, String> ruleVsDescriptionMap = new HashMap<String, String>();
						for (RuleInfoDTO ruleInfoDTO : ruleInfoDTOs) {
							ruleVsDescriptionMap.put(ruleInfoDTO.getRule(), ruleInfoDTO.getDescription());
						}
						tableRuleVsDescriptionMap.put(tableName, ruleVsDescriptionMap);
					}
				}
			}
		}
		return tableRuleVsDescriptionMap;
	}

	/**
	 * Creates a map containing table name and rule operator defined for it for a given document.
	 * 
	 * @param selectedDocument {@link String} the document whose table rule operator information is required
	 * @return {@link Map} map containing table rule operator information of the document
	 */
	public Map<String, String> getTableVsRuleOperatorMap(final String selectedDocument) {
		Map<String, String> tableVsRuleOperatorMap = new HashMap<String, String>();
		if (null != tableInfoMap && !tableInfoMap.isEmpty()) {
			List<TableInfoDTO> tableInfoDTOs = tableInfoMap.get(selectedDocument);
			if (!CollectionUtil.isEmpty(tableInfoDTOs)) {
				for (TableInfoDTO tableInfoDTO : tableInfoDTOs) {
					tableVsRuleOperatorMap.put(tableInfoDTO.getName(), tableInfoDTO.getRuleOperator());
				}
			}
		}
		return tableVsRuleOperatorMap;
	}

	private void processTableRules(final Map<String, Object> ruleTableMap, final TableInfoDTO currentTableInfoDTO) {
		List<TableColumnInfoDTO> tableColumnInfoDTOs = currentTableInfoDTO.getColumnInfoDTOs();
		if (tableColumnInfoDTOs != null && !tableColumnInfoDTOs.isEmpty()) {
			for (TableColumnInfoDTO currentColumnInfoDTO : tableColumnInfoDTOs) {
				ruleTableMap.put("tableRuleList", currentColumnInfoDTO.getTableInfoDTO().getRuleInfoDTOs());
			}
		}
	}

	/**
	 * Creates a map containing containing table column isRequird values for a given document.
	 * 
	 * @param selectedDocument {@link String} the document for which table column isRequired map will be created.
	 * @return {@link Map} Map containing table column isRequired Field value
	 */
	public Map<String, Map<String, Boolean>> getTableColumnVsRequiredMap(final String selectedDocument) {
		Map<String, Map<String, Boolean>> tableColumnVsRequiredMap = null;
		if (null != tableInfoMap && !tableInfoMap.isEmpty()) {
			List<TableInfoDTO> tableInfoDTOs = tableInfoMap.get(selectedDocument);
			if (!CollectionUtil.isEmpty(tableInfoDTOs)) {
				tableColumnVsRequiredMap = new HashMap<String, Map<String, Boolean>>();
				for (TableInfoDTO tableInfoDTO : tableInfoDTOs) {
					List<TableColumnInfoDTO> tableColumnInfoDTOs = tableInfoDTO.getColumnInfoDTOs();
					if (!CollectionUtil.isEmpty(tableColumnInfoDTOs)) {
						Map<String, Boolean> columnVsRequiredMap = new HashMap<String, Boolean>();
						for (TableColumnInfoDTO columnInfoDTO : tableColumnInfoDTOs) {
							columnVsRequiredMap.put(columnInfoDTO.getColumnName(), columnInfoDTO.isRequired());
						}
						tableColumnVsRequiredMap.put(tableInfoDTO.getName(), columnVsRequiredMap);
					}
				}
			}
		}
		return tableColumnVsRequiredMap;
	}

}
