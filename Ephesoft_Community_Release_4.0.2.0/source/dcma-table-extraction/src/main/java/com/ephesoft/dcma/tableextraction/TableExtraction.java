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

package com.ephesoft.dcma.tableextraction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.script.ScriptException;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ephesoft.dcma.batch.schema.Batch;
import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.HeaderRow;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.Batch.Documents;
import com.ephesoft.dcma.batch.schema.Document.DataTables;
import com.ephesoft.dcma.batch.schema.Document.DocumentLevelFields;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.PluginPropertiesService;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.core.common.CategoryType;
import com.ephesoft.dcma.core.common.CurrencyCode;
import com.ephesoft.dcma.core.common.ExpressionEvaluator;
import com.ephesoft.dcma.core.common.TableColumnVO;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.BatchClass;
import com.ephesoft.dcma.da.domain.DocumentType;
import com.ephesoft.dcma.da.domain.TableColumnExtractionRule;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.da.domain.TableExtractionRule;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.domain.TableRuleInfo;
import com.ephesoft.dcma.da.service.BatchClassService;
import com.ephesoft.dcma.da.service.DocumentTypeService;
import com.ephesoft.dcma.da.service.TableInfoService;
import com.ephesoft.dcma.tablefinder.constants.TableExtractionConstants;
import com.ephesoft.dcma.tablefinder.data.DataCarrier;
import com.ephesoft.dcma.tablefinder.service.TableFinderService;
import com.ephesoft.dcma.tablefinder.share.ColumnCoordinates;
import com.ephesoft.dcma.tablefinder.share.ColumnHeaderExtractionDataCarrier;
import com.ephesoft.dcma.tablefinder.share.DataTableService;
import com.ephesoft.dcma.tablefinder.share.RegexValidationDataCarrier;
import com.ephesoft.dcma.tablefinder.share.TableExtractionAPI;
import com.ephesoft.dcma.tablefinder.share.TableExtractionAPIResult;
import com.ephesoft.dcma.tablefinder.share.TableExtractionResult;
import com.ephesoft.dcma.tablefinder.share.TableExtractionResultModifierUtility;
import com.ephesoft.dcma.tablefinder.share.TableRowFinderUtility;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.CurrencyUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.NumberUtil;

/**
 * This class is responsible to extract table grid data from the hOCR files(html files with HOCR text) from image files. Service will
 * read all the pages one by one and search some pattern corresponding to document type and update the batch xml file with data tables
 * values.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.tableextraction.service.TableExtractionService
 * 
 */
@Component
public class TableExtraction {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TableExtraction.class);

	private static final String TABLE_EXTRACTION_PLUGIN = "TABLE_EXTRACTION";
	/**
	 * Constant for document identifier
	 */
	private final String documentIdetifier = "DOC";

	/**
	 * Reference of BatchSchemaService.
	 */
	@Autowired
	private BatchSchemaService batchSchemaService;

	/**
	 * Reference of TableInfoService.
	 */
	@Autowired
	private TableInfoService tableInfoService;

	/**
	 * Reference of TableFinderService.
	 */
	@Autowired
	private TableFinderService tableFinderService;

	@Autowired
	@Qualifier("batchInstancePluginPropertiesService")
	private PluginPropertiesService pluginPropertiesService;
	
	@Autowired
	private DocumentTypeService documentTypeService;

	@Autowired
	@Qualifier("batchClassPluginPropertiesService")
	private PluginPropertiesService batchClassPluginPropertiesService;

	@Autowired
	private BatchClassService batchClassService;

	Locale defaultLocale = Locale.getDefault();

	/**
	 * Invalid charactes which needs to remove before applying table rule for the extracted column value.
	 */
	private String invalidRuleCharacters;

	/**
	 * This method is used to extract the document level fields using key value based extraction. Update the extracted data to the
	 * batch.xml file.
	 * 
	 * @param batchInstanceIdentifier
	 * @return
	 * @throws DCMAApplicationException
	 */
	public final boolean extractFields(final String batchInstanceIdentifier) throws DCMAApplicationException {
		final String switchValue = pluginPropertiesService.getPropertyValue(batchInstanceIdentifier, TABLE_EXTRACTION_PLUGIN,
				TableExtractionProperties.TABLE_EXTRACTION_SWITCH);
		boolean isSuccessful = true;
		if (("ON").equalsIgnoreCase(switchValue)) {
			String errMsg = null;
			setInvalidRuleCharacters(tableFinderService.getInvalidRuleCharacters());
			final int gapBetweenColumnWords = tableFinderService.getGapBetweenColumnWords();

			if (null == batchInstanceIdentifier) {
				errMsg = "Invalid batchInstanceId.";
				LOGGER.error(errMsg);
				throw new DCMAApplicationException(errMsg);
			}

			LOGGER.info("batchInstanceIdentifier : " + batchInstanceIdentifier);

			final Batch batch = batchSchemaService.getBatch(batchInstanceIdentifier);

			try {
				final List<Document> docTypeList = batch.getDocuments().getDocument();

				if (null == docTypeList) {
					LOGGER.info("In valid batch documents.");
				} else {
					processDocPage(docTypeList, batchInstanceIdentifier, batch, gapBetweenColumnWords);
				}
			} catch (final DCMAApplicationException e) {
				isSuccessful = false;
				LOGGER.error(e.getMessage());
				throw new DCMAApplicationException(e.getMessage(), e);
			} catch (final Exception e) {
				isSuccessful = false;
				LOGGER.error(e.getMessage());
				throw new DCMAApplicationException(e.getMessage(), e);
			}

			batchSchemaService.updateBatch(batch);
		} else {
			LOGGER.info("Skipping Table extraction. Switch set as off.");
		}
		return isSuccessful;
	}

	/**
	 * This method will process for each page for each document.
	 * 
	 * @param xmlDocuments List<DocumentType>
	 * @param batchInstanceIdentifier String
	 * @param batch Batch
	 * @param gapBetweenColumnWords
	 * @return isSuccessful
	 * @throws DCMAApplicationException Check for all the input parameters.
	 */
	private boolean processDocPage(final List<Document> xmlDocuments, final String batchInstanceIdentifier, final Batch batch,
			final int gapBetweenColumnWords) throws DCMAApplicationException {
		boolean isSuccessful = false;
		if (null == xmlDocuments || xmlDocuments.isEmpty()) {
			throw new DCMAApplicationException("In valid parameters.");
		}

		for (final Document document : xmlDocuments) {
			// Create doc level fields for document.
			createDocLevelFields(document, batchInstanceIdentifier);

			if (null == document) {
				continue;
			}

			final String docTypeNameBatch = document.getType();
			if (null == docTypeNameBatch || docTypeNameBatch.isEmpty()) {
				continue;
			}

			final List<Page> pageList = document.getPages().getPage();
			if (null == pageList || pageList.isEmpty()) {
				continue;
			}

			final String batchClassIdentifier = batch.getBatchClassIdentifier();

			if (null == batchClassIdentifier || batchClassIdentifier.isEmpty()) {
				throw new DCMAApplicationException("Batch class identifier is null or empty...");
			}

			final List<TableInfo> tableInfoList = tableInfoService.getTableInfoByDocTypeName(docTypeNameBatch, batchClassIdentifier);

			if (null == tableInfoList || tableInfoList.isEmpty()) {
				LOGGER.info("Table info list is null or empty.");
				continue;
			}

			DataTables dataTables = document.getDataTables();
			if (null == dataTables) {
				dataTables = new DataTables();
				document.setDataTables(dataTables);
			}

			final List<DataTable> dataTableList = dataTables.getDataTable();

			isSuccessful = readAllDataTables(tableInfoList, dataTableList, pageList, batchInstanceIdentifier, gapBetweenColumnWords);

		}

		return isSuccessful;
	}

	/**
	 * This method reads all table info and extracts table accordingly.
	 * 
	 * @param tableInfoList
	 * @param startDataCarrier
	 * @param lineDataCarrierList
	 * @param dataTableList
	 * @param pageList
	 * @param gapBetweenColumnWords
	 * @return boolean
	 * @throws DCMAApplicationException
	 */
	private boolean readAllDataTables(final List<TableInfo> tableInfoList, final List<DataTable> dataTableList,
			final List<Page> pageList, final String batchInstanceIdentifier, final int gapBetweenColumnWords)
			throws DCMAApplicationException {
		DataTableService tableExtractionUtility = new DataTableService();
		for (final TableInfo tableInfo : tableInfoList) {
			if (null == tableInfo) {
				LOGGER.info("Table info is null.");
				continue;
			}
			final String tableName = tableInfo.getName();
			LOGGER.info(EphesoftStringUtil.concatenate("Extracting data for table : ", tableName));
			if (EphesoftStringUtil.isNullOrEmpty(tableName)) {
				throw new DCMAApplicationException("Table name is null or empty.");
			}
			final DataTable dataTable = new DataTable();
			dataTable.setName(tableName);
			dataTableList.add(dataTable);
			tableExtractionUtility.initializeHeaderRow(dataTable);
			tableExtractionUtility.initializeDataTableRows(dataTable);
			final List<TableColumnsInfo> tableColumnsInfoList = tableInfo.getTableColumnsInfo();
			if (CollectionUtils.isEmpty(tableColumnsInfoList)) {
				LOGGER.error("Table Columns Info list is null or empty.");
				continue;
			}

			// Changed for bug #12556: Table ExtractionIf NO �Table Extraction Rule� is defined then on Validation screen Table
			// icon is
			// displayed but no Table on clicking that icon.
			HeaderRow headerRow = dataTable.getHeaderRow();
			HeaderRow.Columns columnsHeader = headerRow.getColumns();
			final List<Column> columnHeaderList = columnsHeader.getColumn();
			tableExtractionUtility.createColumnHeaders(columnHeaderList, tableColumnsInfoList);
			final List<TableExtractionRule> tableExtractionRuleList = tableInfo.getTableExtractionRules();

			// Getting attributes for extracting table data.
			final CurrencyCode tableCurrencyCode = tableInfo.getCurrencyCode();
			final String ruleOperatorDefinedForTable = tableInfo.getRuleOperator();
			final boolean isRemoveInvalidRows = tableInfo.isRemoveInvalidRows();
			final List<TableRuleInfo> tableValidationRulesList = tableInfo.getTableRuleInfo();
			LOGGER.debug(EphesoftStringUtil.concatenate("Currency for the table is: ", tableCurrencyCode,
					"\n Rule Operator for table is: ", ruleOperatorDefinedForTable, "\n Do we need to remove invalid rows?: ",
					isRemoveInvalidRows));
			List<LineDataCarrier> lineDataCarrierList = null;

			// Map to store the column header info against column name.
			// changed to avoid memory wastage.
			Map<String, DataCarrier> colHeaderInfoMap = null;

			// Maps whether a column is currency or not. If column field is currency then its currency code is stored.
			// Currently we are storing one locale per table. so value would be same for all keys. But this representation would be
			// helpful
			// when in any scenario currency support would be at column level.
			Map<String, CurrencyCode> columnCurrencyMap = null;
			List<TableColumnVO> tableColumnVOList = null;
			int noOfTableColumns = tableColumnsInfoList.size();
			List<TableExtractionResult> allTableRowsList = new ArrayList<TableExtractionResult>(tableExtractionRuleList.size());
			if (!CollectionUtils.isEmpty(tableExtractionRuleList)) {

				for (TableExtractionRule tableExtractionRule : tableExtractionRuleList) {
					if (null != tableExtractionRule) {
						final String startPattern = tableExtractionRule.getStartPattern();
						if (EphesoftStringUtil.isNullOrEmpty(startPattern)) {
							LOGGER.error("Rule: ", tableExtractionRule.getRuleName(), "'s start pattern is null or empty.");
							continue;
						}
						final String endPattern = tableExtractionRule.getEndPattern();
						TableExtractionAPI tableExtractionAPI = tableExtractionUtility.getTableExtractionAPI(tableExtractionRule
								.getTableAPI());
						final List<TableColumnExtractionRule> tableColumnExtractionRuleList = tableExtractionRule
								.getTableColumnExtractionRules();

						// getting all table column info for extraction.
						tableColumnVOList = tableExtractionUtility.getTableColumnData(tableColumnsInfoList, tableColumnVOList,
								noOfTableColumns, tableColumnExtractionRuleList);

						// Searching all rows of the page.
						lineDataCarrierList = TableRowFinderUtility.searchAllRowOfTables(pageList, startPattern, endPattern,
								batchInstanceIdentifier, tableFinderService.getFuzzyMatchThresholdValue());

						if (CollectionUtils.isNotEmpty(lineDataCarrierList)) {
							boolean colHeaderValidationRequired = tableExtractionAPI.isColHeaderValidationRequired();
							boolean colCoordValidationRequired = tableExtractionAPI.isColCoordValidationRequired();

							// Get extracted column header value mapped with column headers.
							if (colHeaderValidationRequired) {
								colHeaderInfoMap = tableExtractionUtility.getColumnHeaderMap(colHeaderInfoMap, tableColumnVOList);

								// call method to populate the columnHeaderInfoMap.
								tableExtractionUtility.setColumnHeaderInfo(lineDataCarrierList, colHeaderInfoMap, tableColumnVOList,
										tableFinderService.getFuzzyMatchThresholdValue());
							}
							boolean isTableColumnListDirty = tableExtractionUtility.sortTableColumnsInOrderOfOccurance(
									colHeaderInfoMap, tableColumnVOList, colHeaderValidationRequired, colCoordValidationRequired);
							final List<Row> rowList = addDataTablesValues(lineDataCarrierList, tableColumnVOList, colHeaderInfoMap,
									tableExtractionAPI, gapBetweenColumnWords);
							if (CollectionUtils.isNotEmpty(rowList)) {
								columnCurrencyMap = tableExtractionUtility.getColumnCurrencyMap(tableCurrencyCode, columnCurrencyMap,
										noOfTableColumns, tableColumnExtractionRuleList);

								// Multiple rule validation implementation.
								applyTableValidationRulesOnRows(ruleOperatorDefinedForTable, tableValidationRulesList,
										isRemoveInvalidRows, columnCurrencyMap, rowList);
								if (null != columnCurrencyMap) {
									columnCurrencyMap.clear();
								}
								allTableRowsList.add(new TableExtractionResult(rowList, tableExtractionRule.getRuleName()));
							}

							// Data extracted for columns in rows is different order from ui displayed table columns. Assign values to
							// correct column.
							if (isTableColumnListDirty) {
								for (TableExtractionResult list : allTableRowsList) {
									if (null != list) {
										tableExtractionUtility.sortTableRowsInUiOrder(tableColumnsInfoList, list.getRowList());
									}
								}
							}
							if (null != colHeaderInfoMap) {
								colHeaderInfoMap.clear();
							}
							lineDataCarrierList.clear();
						}
						if (null != tableColumnVOList) {
							tableColumnVOList.clear();
						}
					}
				}
			}
			// Find the table with maximum valid data.
			tableExtractionUtility.addBestRuleRows(dataTable, allTableRowsList);
			List<Row> dataTableRowList = dataTable.getRows().getRow();
			if (CollectionUtil.isEmpty(dataTableRowList)) {

				// Adding by default one empty row in table results if no data was extracted from table extraction rules.
				// This row has application of validation patterns on empty columns and table validation rules on the row.
				insertEmptyRow(dataTableRowList, tableColumnsInfoList);
				applyTableValidationRulesOnRows(ruleOperatorDefinedForTable, tableValidationRulesList, isRemoveInvalidRows,
						columnCurrencyMap, dataTableRowList);
			}
		}
		return true;
	}

	/**
	 * Inserts an empty row to a list of table rows.
	 * 
	 * @param dataTableRowList {@link List}<{@link Row}>
	 * @param tableColumnsInfoList {@link List}<{@link TableColumnsInfo}>
	 */
	private void insertEmptyRow(List<Row> dataTableRowList, final List<TableColumnsInfo> tableColumnsInfoList) {
		Row row = new Row();
		row.setMannualExtraction(false);
		row.setIsRuleValid(true);
		Row.Columns columnsRow = row.getColumns();
		if (null == columnsRow) {
			columnsRow = new Row.Columns();
			row.setColumns(columnsRow);
		}
		final List<Column> columnRowList = columnsRow.getColumn();
		for (final TableColumnsInfo tableColumnsInfo : tableColumnsInfoList) {
			Column column = new Column();
			column.setValid(false);
			column.setValidationRequired(false);
			column.setConfidence(0.0f);
			column.setForceReview(false);
			column.setOcrConfidence(0.0f);
			column.setOcrConfidenceThreshold(0.0f);
			column.setValid(false);
			column.setValidationRequired(false);
			column.setName(tableColumnsInfo.getColumnName());
			column.setValue(TableExtractionConstants.EMPTY);
			column.setValid(TableExtractionResultModifierUtility.isValidWithPattern(column.getValue(), tableColumnsInfo
					.getValidationPattern()));
			columnRowList.add(column);
		}
		dataTableRowList.add(row);
	}

	/**
	 * Extracts table data.
	 * 
	 * @param lineDataCarrierList {@link List}<{@link LineDataCarrier}>
	 * @param tableColumnList {@link List}<{@link TableColumnVO}>
	 * @param colHeaderInfoMap {@link Map}<{@link String}, {@link DataCarrier}>
	 * @param tableExtractionAPI {@link TableExtractionAPI}
	 * @return {@link List}<{@link Row}>
	 * @throws DCMAApplicationException
	 */
	private List<Row> addDataTablesValues(final List<LineDataCarrier> lineDataCarrierList, final List<TableColumnVO> tableColumnList,
			final Map<String, DataCarrier> colHeaderInfoMap, final TableExtractionAPI tableExtractionAPI,
			final int gapBetweenColumnWords) throws DCMAApplicationException {
		List<Row> rowList = null;
		if (CollectionUtils.isNotEmpty(lineDataCarrierList) && CollectionUtils.isNotEmpty(tableColumnList)
				&& null != tableExtractionAPI) {

			/** isRowValidForAllMandatoryColumns is a boolean variable used in context of multiline spanning column data in table. */
			boolean isRowValidForAllMandatoryColumns;

			/** previous row to merge with next row if mandatory is set for the column. */
			Row previousRow = new Row();
			previousRow.setIsRuleValid(false);
			previousRow.setMannualExtraction(false);
			boolean isRowAvaliable = false;
			boolean isRowValidForAllRequiredColumns = true;
			boolean allRowFinishTasksDone = true;
			final String tableExtractionAPIString = tableExtractionAPI.getTableExtractionAPI();
			final ExpressionEvaluator<Boolean> expressionEvaluator = new ExpressionEvaluator<Boolean>(tableExtractionAPIString
					.toUpperCase());
			int lineIndex = TableExtractionConstants.START_INDEX;
			boolean isFirstPage = true;
			String firstPageId = null;
			if (lineDataCarrierList != null && !lineDataCarrierList.isEmpty()) {
				firstPageId = lineDataCarrierList.get(0).getPageID();
			}
			LineDataCarrier lineDataCarrier = null;
			boolean columnHeaderValidationRequired = tableExtractionAPI.isColHeaderValidationRequired();
			boolean columnCoordinateValidationRequired = tableExtractionAPI.isColCoordValidationRequired();
			boolean regexValidationRequired = tableExtractionAPI.isRegexValidationRequired();
			ColumnHeaderExtractionDataCarrier columnHeaderExtractionDataCarrier = null;
			while (lineIndex < lineDataCarrierList.size()) {
				lineDataCarrier = lineDataCarrierList.get(lineIndex);
				final String pageID = lineDataCarrier.getPageID();
				if (!pageID.equals(firstPageId)) {
					isFirstPage = false;
				}
				final String rowData = lineDataCarrier.getLineRowData();
				final List<Span> spanList = lineDataCarrier.getSpanList();
				LOGGER.info(EphesoftStringUtil.concatenate("Row Data : ", rowData));
				isRowValidForAllRequiredColumns = true;
				isRowValidForAllMandatoryColumns = true;
				final Row row = new Row();
				row.setIsRuleValid(false);
				row.setMannualExtraction(false);

				// By default setting rule validation as true.
				row.setIsRuleValid(true);
				Row.Columns columnsRow = TableRowFinderUtility.getRowColumns(lineDataCarrier, row);
				final List<Column> columnRowList = columnsRow.getColumn();
				isRowAvaliable = false;
				for (final TableColumnVO tableColumn : tableColumnList) {
					LOGGER.info(EphesoftStringUtil.concatenate("Extracting column data for column = ", tableColumn.getColumnName()));

					// Search for all the table row data one by one.
					final Column column = new Column();
					column.setValid(false);
					column.setValidationRequired(false);
					column.setConfidence(0.0f);
					column.setForceReview(false);
					column.setOcrConfidence(0.0f);
					column.setOcrConfidenceThreshold(0.0f);
					column.setValid(false);
					column.setValidationRequired(false);
					TableRowFinderUtility.setColumnProperties(pageID, column, null, 0);
					column.setName(tableColumn.getColumnName());
					columnRowList.add(column);
					DataCarrier colHeaderDataCarrier = null;
					final Integer indexOfTableColumn = tableColumnList.indexOf(tableColumn);
					boolean isRegexValidationPassed = false;
					boolean isColHeaderValidationPassed = false;
					boolean isColCoordValidationPassed = false;
					boolean furtherValidationRequired = true;
					Coordinates valueCoordinates = null;

					// This confidence parameter for column header extraction mechanism indicates whether result obtained from this
					// extraction matches 100% to the validation pattern. If it doesn't match, we search to look for extraction results
					// by further algorithms (if applicable).
					boolean isColumnHeaderExtractionConfidenceMaximum = false;

					// This confidence parameter for column coordinate extraction mechanism indicates whether result obtained from this
					// extraction matches 100% to the validation pattern. If it doesn't match, we search to look for extraction results
					// by further algorithms (if applicable).
					boolean isColumnCoordinateExtractionConfidenceMaximum = false;
					if (columnHeaderValidationRequired) {
						if (null != colHeaderInfoMap && !colHeaderInfoMap.isEmpty()) {
							colHeaderDataCarrier = colHeaderInfoMap.get(tableColumn.getColumnName());
						}
						if (null == columnHeaderExtractionDataCarrier) {
							columnHeaderExtractionDataCarrier = new ColumnHeaderExtractionDataCarrier();
						}
						columnHeaderExtractionDataCarrier.setColHeaderDataCarrier(colHeaderDataCarrier);
						columnHeaderExtractionDataCarrier.setColumn(column);
						columnHeaderExtractionDataCarrier.setLineDataCarrier(lineDataCarrier);
						columnHeaderExtractionDataCarrier.setSpanList(spanList);
						columnHeaderExtractionDataCarrier.setTableColumn(tableColumn);
						TableExtractionAPIResult tableExtractionAPIResult = TableRowFinderUtility.runColumnHeaderExtraction(
								columnHeaderExtractionDataCarrier, gapBetweenColumnWords, isFirstPage);
						valueCoordinates = tableExtractionAPIResult.getValueCoordinates();
						isColumnHeaderExtractionConfidenceMaximum = tableExtractionAPIResult.isConfidenceMaximum();
						isColHeaderValidationPassed = tableExtractionAPIResult.isValidationPassed();
					}
					if (TableRowFinderUtility
							.isDataValid(expressionEvaluator, false, isColumnHeaderExtractionConfidenceMaximum, false)
							|| !TableRowFinderUtility.isDataValid(expressionEvaluator, true, isColHeaderValidationPassed, true)) {
						LOGGER.info("No further validation required.....");
						furtherValidationRequired = false;
					}
					if (furtherValidationRequired && columnCoordinateValidationRequired) {
						LOGGER.info("Applying Column Coordinates Validation for table extraction....");
						if (TableRowFinderUtility.isDataValid(expressionEvaluator, false, false, true)) {
							TableExtractionAPIResult tableExtractionAPIResult = TableRowFinderUtility.runColumnCoordinateExtraction(
									pageID, tableColumn, column, spanList);
							valueCoordinates = tableExtractionAPIResult.getValueCoordinates();
							isColumnCoordinateExtractionConfidenceMaximum = tableExtractionAPIResult.isConfidenceMaximum();
							isColCoordValidationPassed = tableExtractionAPIResult.isValidationPassed();
						} else if (isColumnHeaderExtractionConfidenceMaximum && valueCoordinates != null) {
							ColumnCoordinates columnCoordinates = TableRowFinderUtility.getXColumncoordinates(tableColumn);
							if (TableRowFinderUtility.isColumnValidWithColCoord(valueCoordinates.getX0().intValue(), valueCoordinates
									.getX1().intValue(), columnCoordinates.getX0Coordinate(), columnCoordinates.getX1coordinate())) {
								isColCoordValidationPassed = true;
								isColumnCoordinateExtractionConfidenceMaximum = true;
								column.setValid(Boolean.TRUE);
								LOGGER.info("Getting rectangle coordinates for the value. ");
								valueCoordinates = HocrUtil.getRectangleCoordinates(column.getCoordinatesList().getCoordinates());
							}
						} else {
							TableExtractionAPIResult tableExtractionAPIResult = TableRowFinderUtility.runColumnCoordinateExtraction(
									pageID, tableColumn, column, spanList);
							valueCoordinates = tableExtractionAPIResult.getValueCoordinates();
							isColumnCoordinateExtractionConfidenceMaximum = tableExtractionAPIResult.isConfidenceMaximum();
							isColCoordValidationPassed = tableExtractionAPIResult.isValidationPassed();
						}

						// Checking if column header validation and/or column coordinate validation results are confident enough with
						// respect to table extraction API needed.
						if ((TableRowFinderUtility.isDataValid(expressionEvaluator, false, isColumnHeaderExtractionConfidenceMaximum,
								isColumnCoordinateExtractionConfidenceMaximum) || !TableRowFinderUtility.isDataValid(
								expressionEvaluator, true, isColHeaderValidationPassed, isColCoordValidationPassed))) {
							LOGGER.info("No further validation required....");
							furtherValidationRequired = false;
						}
					}
					if (furtherValidationRequired && regexValidationRequired) {
						RegexValidationDataCarrier regexvalidationDataCarrier = new RegexValidationDataCarrier();
						regexvalidationDataCarrier.setColumn(column);
						regexvalidationDataCarrier.setColumnCoordinates(valueCoordinates);
						regexvalidationDataCarrier.setIndexOfTableColumn(indexOfTableColumn);
						regexvalidationDataCarrier.setLineDataCarrier(lineDataCarrier);
						regexvalidationDataCarrier.setPageID(pageID);
						regexvalidationDataCarrier.setRowData(rowData);
						regexvalidationDataCarrier.setSpanList(spanList);
						regexvalidationDataCarrier.setTableColumn(tableColumn);
						isRegexValidationPassed = TableRowFinderUtility.runRegexValidation(expressionEvaluator,
								columnHeaderValidationRequired, columnCoordinateValidationRequired, columnRowList,
								colHeaderDataCarrier, regexvalidationDataCarrier);
					}
					if (TableRowFinderUtility.isDataValid(expressionEvaluator, isRegexValidationPassed, isColHeaderValidationPassed,
							isColCoordValidationPassed)) {
						isRowAvaliable = true;
						LOGGER.info("Data valid with respect to all the validations..");
					} else {
						TableRowFinderUtility.setColumnProperties(pageID, column, TableExtractionConstants.EMPTY, 0);
					}
					column.setName(tableColumn.getColumnName());
					LOGGER.info("Checking if row contains valid data for mandatory columns.....");
					if (tableColumn.isMultilineAnchor() && (column.getValue() == null || column.getValue().isEmpty())) {
						isRowValidForAllMandatoryColumns = false;
					}
				}

				if (isRowAvaliable) {
					LOGGER.info("Row is available.");
					if (null == rowList) {
						rowList = new ArrayList<Row>();
					}

					/**
					 * if executes if current row has data to be merged with last added row to rowList, because of missing content in
					 * atleast one mandatory column.
					 */
					if (!isRowValidForAllMandatoryColumns && null != previousRow && previousRow.getColumns() != null) {
						final List<Column> previousRowColumnsList = previousRow.getColumns().getColumn();
						LOGGER.info("Merging rows of multiline data...");
						TableExtractionResultModifierUtility.mergeMultilineRows(previousRowColumnsList, columnRowList);
						TableExtractionResultModifierUtility.setMergedRowCoordinates(previousRow, row);
					} else {

						// Note: previousRow.getColumns() == null if for first row
						isRowValidForAllRequiredColumns = TableExtractionResultModifierUtility.finishTasksForPreviousRow(
								tableColumnList, rowList, isRowValidForAllRequiredColumns, previousRow, lineDataCarrier);
						if (!isRowValidForAllRequiredColumns) {
							LOGGER.debug("Removed previous row from rowList as required column data was missing from it.");
						}
						allRowFinishTasksDone = false;
						LOGGER.info("Adding a new row to the row list...");
						rowList.add(row);
						previousRow = row;
					}
				}
				lineIndex++;
			}

			// following executes for last row in extracted table data.
			if (!allRowFinishTasksDone) {
				isRowValidForAllRequiredColumns = TableExtractionResultModifierUtility.finishTasksForPreviousRow(tableColumnList,
						rowList, isRowValidForAllRequiredColumns, previousRow, lineDataCarrier);
				if (!isRowValidForAllRequiredColumns) {
					LOGGER.debug("Removed previous row from rowList as required column data was missing from it.");
				}
			}
		}
		return rowList;
	}

	/**
	 * Apply table row validation of each row of list.
	 * 
	 * @param ruleOperatorDefinedForTable {@link String}
	 * @param listofTableRules {@link List}<{@link TableRuleInfo}>
	 * @param isRemoveInvalidRows boolean
	 * @param columnCurrencyMap {@link Map<{@link String}, {@link CurrencyCode}>
	 * @param rowList {@link List}<{@link Row}>
	 */
	private void applyTableValidationRulesOnRows(final String ruleOperatorDefinedForTable, final List<TableRuleInfo> listofTableRules,
			final boolean isRemoveInvalidRows, final Map<String, CurrencyCode> columnCurrencyMap, List<Row> rowList) {
		if (rowList != null && !rowList.isEmpty()) {

			// Added to delete rows with invalid row as per rule when Remove invalid rows is true.
			final List<Row> listToBeDeleted = new ArrayList<Row>(rowList.size());
			StringBuilder invalidCharactesString = null;
			if (!EphesoftStringUtil.isNullOrEmpty(invalidRuleCharacters)) {
				final String[] inValidCharcters = invalidRuleCharacters.split(TableExtractionConstants.SEMI_COLON);
				if (inValidCharcters != null && inValidCharcters.length > 0) {
					invalidCharactesString = new StringBuilder();
					for (int index = 0; index < inValidCharcters.length; index++) {
						invalidCharactesString.append(inValidCharcters[index]);
					}
				}
			}

			for (Row tableRow : rowList) {

				// if multiple rule validation operator is "and".
				if (TableExtractionConstants.AND_OPERATOR.equalsIgnoreCase(ruleOperatorDefinedForTable)) {
					for (int index = 0; index < listofTableRules.size(); index++) {
						if (!isRuleValid(listofTableRules.get(index).getRule(), tableRow, invalidCharactesString, columnCurrencyMap)) {
							setRowForInvalidRule(tableRow, listToBeDeleted);
							break;
						} else {
							setRowForInvalidRule(tableRow, listToBeDeleted);
						}
					}
				} else {
					for (int index = 0; index < listofTableRules.size(); index++) {
						if (!isRuleValid(listofTableRules.get(index).getRule(), tableRow, invalidCharactesString, columnCurrencyMap)) {
							setRowForInvalidRule(tableRow, listToBeDeleted);
						} else {
							setRowForValidRule(tableRow, listToBeDeleted);
							tableRow.setRuleViolated(null);
							break;
						}
					}
				}
			}

			// Removes invalid rows based on rule defined if this property has been set as checked.
			if (isRemoveInvalidRows) {
				rowList.removeAll(listToBeDeleted);
			}
		}
	}

	/**
	 * This method creates new document level fields for document if they haven't been created by previous plugins.
	 * 
	 * @param eachDocType
	 * @param batchInstanceIdentifier
	 */
	private void createDocLevelFields(final Document eachDocType, final String batchInstanceIdentifier) {
		DocumentLevelFields documentLevelFields = eachDocType.getDocumentLevelFields();
		if (documentLevelFields == null) {
			documentLevelFields = new DocumentLevelFields();
			eachDocType.setDocumentLevelFields(documentLevelFields);
		}
		final List<DocField> docLevelFields = documentLevelFields.getDocumentLevelField();
		if (docLevelFields == null || docLevelFields.isEmpty()) {
			LOGGER.info("Getting document level fields for document type : " + eachDocType.getType());
			final List<com.ephesoft.dcma.da.domain.FieldType> allFdTypes = pluginPropertiesService.getFieldTypes(
					batchInstanceIdentifier, eachDocType.getType());
			if (allFdTypes != null) {
				for (final com.ephesoft.dcma.da.domain.FieldType fdType : allFdTypes) {
					// Create new document level field
					LOGGER.info("Creating new document level field");
					final DocField docLevelField = new DocField();
					docLevelField.setName(fdType.getName());
					docLevelField.setFieldOrderNumber(fdType.getFieldOrderNumber());
					docLevelField.setType(fdType.getDataType().name());
					docLevelField.setFieldValueChangeScript(fdType.isFieldValueChangeScriptEnabled());
					if (null != fdType.getCategoryName()) {
						docLevelField.setCategory(fdType.getCategoryName());
					} else {
//						docLevelField.setCategory("Uncategorised");
						docLevelField.setCategory(CategoryType.GROUP_1.getCategoryName());
					}
					docLevelField.setHidden(fdType.isHidden());
					// Object newValue = getValueForDocField(fdType.getName(), allColumnNames, extractedData);
					// docLevelField.setValue(newValue.toString());

					// Add new document level field to document.
					docLevelFields.add(docLevelField);
					LOGGER.info("New doc level field added : " + docLevelField.getName());
				}
			} else {
				LOGGER.info("No field types could be found for document type :" + eachDocType.getType());
			}
		}
	}

	/**
	 * Returns true if rule defined for table is valid otherwise returns false.
	 * 
	 * @param ruleDefinedForTable {@link String} The rule defined for a table.
	 * @param row {@link Row} The row of a table.
	 * @param columnCurrencyMap {@link Map}<{@link String}, {@link CurrencyCode}> is used to map the currency codes corresponding to
	 *            the different column names. If null currency extraction is not performed.
	 * 
	 * @return true if rule defined for table is valid otherwise returns false.
	 * @throws Exception {@link Exception}
	 */
	private boolean isRuleValid(final String ruleDefinedForTable, final Row row, final StringBuilder invalidCharactesString,
			final Map<String, CurrencyCode> columnCurrencyMap) {
		boolean isValid = false;
		try {
			if (!EphesoftStringUtil.isNullOrEmpty(ruleDefinedForTable)) {
				Map<String, Integer> relationalOperators = createRelationalOperatorsMap();
				String selectedOperator = getSelectedRelationalOperator(ruleDefinedForTable, relationalOperators);
				final int indexOfRuleDefined = ruleDefinedForTable.indexOf(selectedOperator);
				if (indexOfRuleDefined != -1) {
					final String ruleDefinedRHS = ruleDefinedForTable.substring(indexOfRuleDefined + selectedOperator.length(),
							ruleDefinedForTable.length());
					final String ruleDefinedLHS = ruleDefinedForTable.substring(0, indexOfRuleDefined - 1);
					if (!EphesoftStringUtil.isNullOrEmpty(ruleDefinedLHS) && !EphesoftStringUtil.isNullOrEmpty(ruleDefinedRHS)) {
						final String[] columnsRHS = EphesoftStringUtil.splitString(ruleDefinedRHS.trim(),
								TableExtractionConstants.AMPERSAND_STRING);
						final String[] columnsLHS = EphesoftStringUtil.splitString(ruleDefinedLHS.trim(),
								TableExtractionConstants.AMPERSAND_STRING);
						if (columnsLHS != null && columnsLHS.length > 0 && columnsRHS != null && columnsRHS.length > 0) {
							final StringBuilder ruleModifiedForExpressionEvaluatorLHS = new StringBuilder();
							final StringBuilder ruleModifiedForExpressionEvaluatorRHS = new StringBuilder();
							final Map<String, Object> columnValuesMapLHS = getColumnValuesMap(row, getColumnNamesMap(
									ruleModifiedForExpressionEvaluatorLHS, columnsLHS), invalidCharactesString, columnCurrencyMap);
							if (columnValuesMapLHS != null && columnValuesMapLHS.get(TableExtractionConstants.TABLE_RULE_KEY) == null) {
								final Map<String, Object> columnValuesMapRHS = getColumnValuesMap(row, getColumnNamesMap(
										ruleModifiedForExpressionEvaluatorRHS, columnsRHS), invalidCharactesString, columnCurrencyMap);
								if (columnValuesMapRHS != null
										&& columnValuesMapRHS.get(TableExtractionConstants.TABLE_RULE_KEY) == null) {
									final ExpressionEvaluator<Double> expressionEvaluatorLHS = getColumnsExpressionEvaluator(
											ruleModifiedForExpressionEvaluatorLHS.toString(), columnValuesMapLHS);
									final ExpressionEvaluator<Double> expressionEvaluatorRHS = getColumnsExpressionEvaluator(
											ruleModifiedForExpressionEvaluatorRHS.toString(), columnValuesMapRHS);
									if (expressionEvaluatorLHS != null && expressionEvaluatorRHS != null) {
										isValid = checkExpressionsForSelectedInequality(relationalOperators, selectedOperator,
												expressionEvaluatorLHS, expressionEvaluatorRHS);
									}
									if (!isValid) {
										row.setRuleViolated(ruleDefinedForTable);
									}
								}

							}
						}
					}
				}
			}
		} catch (final Exception exception) {
			LOGGER.error("Exception occurred while validating rule for a table row" + exception.getMessage());
			row.setRuleViolated(TableExtractionConstants.INVALID_DATA_ERROR_MSG);
		}
		return isValid;
	}

	/**
	 * Checks the passed expressions for relational inequalities like '==','<=','>=','!='.
	 * 
	 * @param relationalOperators {@link Map} map of relational operators.
	 * @param selectedOperator {@link String} selected operator.
	 * @param expressionEvaluatorLHS {@link String} expression on the right hand side of equality.
	 * @param expressionEvaluatorLHS {@link String} expression on the left hand side of equality.
	 * @return {@link boolean} true if the inequality is satisfied, false otherwise.
	 */
	private boolean checkExpressionsForSelectedInequality(Map<String, Integer> relationalOperators, String selectedOperator,
			final ExpressionEvaluator<Double> expressionEvaluatorLHS, final ExpressionEvaluator<Double> expressionEvaluatorRHS)
			throws ScriptException, ClassCastException {
		boolean isValid = false;
		final Double expressionResultLHS = expressionEvaluatorLHS.eval();
		final Double expressionResultRHS = expressionEvaluatorRHS.eval();
		if (selectedOperator != null && expressionResultLHS != null && expressionResultRHS != null) {
			// Formatting required because of results provided by the engine were not formatted.
			final double expressionLHSValue = NumberUtil.getRoundedValue(expressionResultLHS);
			final double expressionRHSValue = NumberUtil.getRoundedValue(expressionResultRHS);
			switch (relationalOperators.get(selectedOperator)) {
				case 0:
					isValid = (expressionLHSValue == expressionRHSValue);
					break;
				case 1:
					isValid = (expressionLHSValue != expressionRHSValue);
					break;
				case 2:
					isValid = (expressionLHSValue >= expressionRHSValue);
					break;
				case 3:
					isValid = (expressionLHSValue <= expressionRHSValue);
					break;
				default:
			}
		}
		return isValid;
	}

	/**
	 * Returns the relational operator used in the rule.
	 * 
	 * @param ruleDefinedForTable {@link String} the table rule.
	 * @param relationalOperators {@link Map} map of all the relational operators.
	 * @return {@link String} the relational operator used in the rule.
	 */
	private String getSelectedRelationalOperator(final String ruleDefinedForTable, Map<String, Integer> relationalOperators) {
		String selectedOperator = null;
		Set<String> operators = relationalOperators.keySet();
		for (String operator : operators) {
			if (ruleDefinedForTable.indexOf(operator) != -1) {
				selectedOperator = operator;
			}
		}
		return selectedOperator;
	}

	/**
	 * Creates a map of relational operators that may occur in an expression.
	 * 
	 * @return {@link Map} map of relational operators.
	 */
	private Map<String, Integer> createRelationalOperatorsMap() {
		Map<String, Integer> relationalOperators = new HashMap<String, Integer>();
		relationalOperators.put(TableExtractionConstants.CHAR_EQUAL, 0);
		relationalOperators.put(TableExtractionConstants.NOT_EQUAL, 1);
		relationalOperators.put(TableExtractionConstants.GREATER_OR_EQUAL, 2);
		relationalOperators.put(TableExtractionConstants.LESS_OR_EQUAL, 3);
		return relationalOperators;
	}

	/**
	 * Returns the instance of {@link ExpressionEvaluator} for the rule defined for a particular table and table columns.
	 * 
	 * @param ruleDefined {@link String} Rule defined for the table.
	 * @param columnsValuesMap {@link Map} contains the map for the table column value.
	 * @return The instance of {@link ExpressionEvaluator}.
	 */
	private ExpressionEvaluator<Double> getColumnsExpressionEvaluator(final String ruleDefined,
			final Map<String, Object> columnsValuesMap) {
		ExpressionEvaluator<Double> expressionEvaluator = null;
		if (columnsValuesMap != null && !columnsValuesMap.isEmpty()) {
			expressionEvaluator = new ExpressionEvaluator<Double>(ruleDefined);
			final Set<String> columnValuesKeySet = columnsValuesMap.keySet();
			final Iterator<String> columnValuesKeyItr = columnValuesKeySet.iterator();
			while (columnValuesKeyItr.hasNext()) {
				final String key = columnValuesKeyItr.next();
				expressionEvaluator.putValue(key, columnsValuesMap.get(key));
			}
		}
		return expressionEvaluator;
	}

	/**
	 * Modifies the column name of the table and creates the modified rule used by expression evaluator and returns the table columns
	 * name map.
	 * 
	 * @param ruleModifiedForExpressionEvaluator {@link String} The modified rule used by expression evaluator.
	 * @param columns {@link String} The array of table columns.
	 * @return {@link String} The table columns name map.
	 */
	private Map<String, String> getColumnNamesMap(final StringBuilder ruleModifiedForExpressionEvaluator, final String[] columns) {
		Map<String, String> columnNamesMap = null;
		if (columns != null) {
			columnNamesMap = new LinkedHashMap<String, String>();
			for (int index = 0; index < columns.length; index++) {
				String tempString = columns[index];
				if (tempString != null) {
					if (index % 2 != 0) {
						final String columnNamesKey = EphesoftStringUtil.concatenate("ID", index);
						columnNamesMap.put(columnNamesKey, tempString.trim());
						tempString = columnNamesKey;
					}
					ruleModifiedForExpressionEvaluator.append(tempString);
				}
			}
		}
		return columnNamesMap;
	}

	/**
	 * Returns the column values map.
	 * 
	 * @param row {@link Row} The row of table.
	 * @param columnNamesMap {@link Map} The columns name map,
	 * @param columnCurrencyMap {@link Map} is used to map the currency codes corresponding to the different column names.
	 * @return {@link Map} The column values map.
	 */
	private Map<String, Object> getColumnValuesMap(final Row row, final Map<String, String> columnNamesMap,
			final StringBuilder invalidCharactesString, final Map<String, CurrencyCode> columnCurrencyMap) {
		Map<String, Object> columnValuesMap = null;
		if (row != null && columnNamesMap != null) {
			columnValuesMap = new LinkedHashMap<String, Object>();
			Set<String> columnsNameset = columnNamesMap.keySet();
			Iterator<String> columnsNameItr = columnsNameset.iterator();
			CurrencyCode columnCurrency = null;
			while (columnsNameItr.hasNext()) {
				String columnNameKey = columnsNameItr.next();
				String columnName = columnNamesMap.get(columnNameKey);
				if (columnCurrencyMap != null) {
					columnCurrency = columnCurrencyMap.get(columnName);
				}
				String columnValue = getColumnValue(row, columnName, columnCurrency);
				if (invalidCharactesString != null && !EphesoftStringUtil.isNullOrEmpty(invalidCharactesString.toString())) {
					columnValue = removeCharacters(columnValue, invalidCharactesString.toString().toCharArray());
				}
				if (!EphesoftStringUtil.isNullOrEmpty(columnValue) && columnValue.matches(TableExtractionConstants.TABLE_RULE_REGEX)) {
					columnValuesMap.put(columnNameKey, Double.valueOf(columnValue));
				} else {
					columnValuesMap.put(TableExtractionConstants.TABLE_RULE_KEY, false);
					break;
				}
			}
		}
		return columnValuesMap;
	}

	private String removeCharacters(final String strToBeModified, final char[] charsToBeReplaced) {
		String resultString = null;
		if (!EphesoftStringUtil.isNullOrEmpty(strToBeModified) && charsToBeReplaced != null) {
			StringBuilder resultStringBuilder = new StringBuilder(strToBeModified.length());
			char[] words = strToBeModified.toCharArray();
			for (int index = 0; index < words.length; index++) {
				boolean matchFailed = true;
				for (int charIndex = 0; charIndex < charsToBeReplaced.length; charIndex++) {
					if (words[index] == charsToBeReplaced[charIndex]) {
						resultStringBuilder.append(TableExtractionConstants.EMPTY);
						matchFailed = false;
						break;
					}
				}
				if (matchFailed) {
					resultStringBuilder.append(words[index]);
				}
			}
			resultString = resultStringBuilder.toString();
		}

		return resultString;
	}

	/**
	 * Returns the value of the columnName passed for the table row. If <code>cuurencyCode</code> is not <code>NULL</code>, then
	 * currencyExtraction rules are applied to the extracted value corresponding to the code.
	 * 
	 * @param row {@link Row} row from which the values are to be extracted.
	 * @param columnPassed {@link String} name of the column whose value is to be extracted.
	 * @param currencyCode {@link CurrencyCode} implies the rules for currency extraction. If null currency extraction is not
	 *            performed.
	 * @return {@link String} Value extracted corresponding to the column.
	 */
	private String getColumnValue(final Row row, final String columnPassed, final CurrencyCode currencyCode) {
		String extractedValue = getColumnValue(row, columnPassed);

		if (currencyCode != null) {
			Double currencyValue = CurrencyUtil.getDoubleValue(extractedValue, currencyCode.getRepresentationValue());
			if (currencyValue != null) {
				extractedValue = currencyValue.toString();
			}
		}
		LOGGER.info(EphesoftStringUtil.concatenate("Extracted value for ", columnPassed, " is ", extractedValue));
		return extractedValue;
	}

	/**
	 * Returns the value of the columnName passed for the table row.
	 * 
	 * @param row {@link Row} The row of the table.
	 * @param columnPassed {@link String} The column name passed.
	 * @return {@link String} The value of column name passed.
	 */
	private String getColumnValue(final Row row, final String columnPassed) {
		String columnValue = null;
		if (row != null || columnPassed != null) {
			Row.Columns columns = row.getColumns();
			if (columns != null) {
				List<Column> columnList = columns.getColumn();
				if (columnList != null && !columnList.isEmpty()) {
					for (Column column : columnList) {
						if (column != null && columnPassed.trim().equalsIgnoreCase(column.getName())) {
							columnValue = column.getValue();
							break;
						}
					}
				}
			}
		}
		return columnValue;
	}

	/**
	 * Sets all the row of a table to invalid.
	 * 
	 * @param row{{@link Row} The row of a table.
	 */
	private void setAllRowInvalid(final Row row) {
		if (row != null) {
			row.setIsRuleValid(false);
		}
	}

	/**
	 * Sets all the row of a table to valid.
	 * 
	 * @param row{{@link Row} The row of a table.
	 */
	private void setAllRowValid(final Row row) {
		if (row != null) {
			row.setIsRuleValid(true);
		}
	}

	/**
	 * API to set row property isRuleValid true when rule validation passes and remove it from the list to be deleted if
	 * isRemoveInvalidRows property is unchecked.
	 * 
	 * @param tableRow
	 * @param listToBeDeleted
	 */
	private void setRowForValidRule(Row tableRow, List<Row> listToBeDeleted) {
		setAllRowValid(tableRow);
		listToBeDeleted.remove(tableRow);
	}

	/**
	 * API to set row property isRuleValid false when rule validation fails and add it to the list of rows to be deleted if
	 * isRemoveInvalidRows property is checked
	 * 
	 * @param tableRow
	 * @param listToBeDeleted
	 */
	private void setRowForInvalidRule(Row tableRow, List<Row> listToBeDeleted) {
		setAllRowInvalid(tableRow);
		listToBeDeleted.add(tableRow);
	}

	/**
	 * @return the invalidRuleCharacters
	 */
	public String getInvalidRuleCharacters() {
		return invalidRuleCharacters;
	}

	/**
	 * @param invalidRuleCharacters the invalidRuleCharacters to set
	 */
	public void setInvalidRuleCharacters(final String invalidRuleCharacters) {
		this.invalidRuleCharacters = invalidRuleCharacters;
	}

	/**
	 * returns documents object after adding data tables
	 * 
	 * @param gapBetweenColumnWords
	 * @param documentHOCRMap
	 * @param docTypeList
	 * @param docTypesName
	 * @return
	 * @throws DCMAApplicationException
	 */
	public Documents processDocPageForTableExtractionWebService(final int gapBetweenColumnWords,
			final Map<DocumentType, List<HocrPages>> documentHOCRMap, List<DocumentType> docTypeList, List<String> docTypesName) throws DCMAApplicationException {
		Documents documents = new Batch.Documents();
		int identifierIndex = 0;
		int pageOrder = 0;
		for (Map.Entry<DocumentType, List<HocrPages>> entry : documentHOCRMap.entrySet()) {
			++identifierIndex;
			DocumentType documentType = entry.getKey();
			// int index = docTypesName.indexOf(entry.getKey().getName());
			final List<TableInfo> tableInfoList = tableInfoService.getTableInfoByDocumentType(documentType);
			if (null == tableInfoList || tableInfoList.isEmpty()) {
				LOGGER.info("Table info list is null or empty.");
				continue;
			}
			Document document = new Document();
			document.setType(documentType.getName());
			document.setIdentifier(EphesoftStringUtil.concatenate(documentIdetifier, identifierIndex));
			documents.getDocument().add(document);
			final List<HocrPages> pageList = entry.getValue();
			if (null == pageList || pageList.isEmpty()) {
				continue;
			}
			for (HocrPages hocrPages : pageList) {
				for (HocrPage hocrPage : hocrPages.getHocrPage()) {
					hocrPage.setPageID(EphesoftStringUtil.concatenate(TableExtractionConstants.pageIdentifier, pageOrder));
					++pageOrder;
				}
			}
			DataTables dataTables = document.getDataTables();
			if (null == dataTables) {
				dataTables = new DataTables();
				document.setDataTables(dataTables);
			}
			final List<DataTable> dataTableList = dataTables.getDataTable();
			readAllDataTablesForTableExtractionWebService(tableInfoList, dataTableList, pageList, gapBetweenColumnWords);
		}
		return documents;
	}

	public Documents processDocPageForTestExtraction(final int gapBetweenColumnWords,
			final Map<Document, List<HocrPages>> documentHOCRMap, final String batchClassIdentifier) throws DCMAApplicationException {
		Documents documents = new Batch.Documents();
		int identifierIndex = 0;
		for (Map.Entry<Document, List<HocrPages>> entry : documentHOCRMap.entrySet()) {
			++identifierIndex;
			Document document = entry.getKey();
			final List<TableInfo> tableInfoList = tableInfoService.getTableInfoByDocTypeName(document.getType(), batchClassIdentifier);
			if (null == tableInfoList || tableInfoList.isEmpty()) {
				LOGGER.info("Table info list is null or empty.");
				continue;
			}
			documents.getDocument().add(document);
			final List<HocrPages> pageList = entry.getValue();
			if (null == pageList || pageList.isEmpty()) {
				continue;
			}
			DataTables dataTables = document.getDataTables();
			if (null == dataTables) {
				dataTables = new DataTables();
				document.setDataTables(dataTables);
			}
			final List<DataTable> dataTableList = dataTables.getDataTable();
			readAllDataTablesForTableExtractionWebService(tableInfoList, dataTableList, pageList, gapBetweenColumnWords);
		}
		return documents;
	}

	/**
	 * Returns documents object after adding data tables to the exisiting document
	 * 
	 * @param gapBetweenColumnWords
	 * @param documentHOCRMap
	 * @param docTypeList
	 * @param docTypesName
	 * @return
	 * @throws DCMAApplicationException
	 */
	public void processDocPageForTableExtraction(final int gapBetweenColumnWords,
			final Map<Document, List<HocrPages>> documentHOCRMap, List<DocumentType> docTypeList, List<String> docTypesName,
			final boolean isTestExtractionFlow) throws DCMAApplicationException {
		int identifierIndex = 0;
		int pageOrder = 0;
		for (Map.Entry<Document, List<HocrPages>> entry : documentHOCRMap.entrySet()) {
			++identifierIndex;
			Document document = entry.getKey();
			int index = docTypesName.indexOf(entry.getKey().getType());
			final List<TableInfo> tableInfoList = tableInfoService.getTableInfoByDocumentType(docTypeList.get(index));
			if (null == tableInfoList || tableInfoList.isEmpty()) {
				LOGGER.info("Table info list is null or empty.");
				continue;
			}
			final List<HocrPages> pageList = entry.getValue();
			if (null == pageList || pageList.isEmpty()) {
				continue;
			}
			for (HocrPages hocrPages : pageList) {
				for (HocrPage hocrPage : hocrPages.getHocrPage()) {
					hocrPage.setPageID(EphesoftStringUtil.concatenate(TableExtractionConstants.pageIdentifier, pageOrder));
					++pageOrder;
				}
			}
			DataTables dataTables = document.getDataTables();
			if (null == dataTables) {
				dataTables = new DataTables();
				document.setDataTables(dataTables);
			}
			final List<DataTable> dataTableList = dataTables.getDataTable();
			readAllDataTablesForTableExtractionWebService(tableInfoList, dataTableList, pageList, gapBetweenColumnWords);
		}
	}

	/**
	 * returns true after extracting table values from the HOCR after applying extraction rules
	 * 
	 * @param tableInfoList
	 * @param dataTableList
	 * @param pageList
	 * @param gapBetweenColumnWords
	 * @return
	 * @throws DCMAApplicationException
	 */
	private boolean readAllDataTablesForTableExtractionWebService(final List<TableInfo> tableInfoList,
			final List<DataTable> dataTableList, final List<HocrPages> pageList, final int gapBetweenColumnWords)
			throws DCMAApplicationException {
		DataTableService tableExtractionUtility = new DataTableService();
		for (final TableInfo tableInfo : tableInfoList) {
			if (null == tableInfo) {
				LOGGER.info("Table info is null.");
				continue;
			}
			final String tableName = tableInfo.getName();
			LOGGER.info(EphesoftStringUtil.concatenate("Extracting data for table : ", tableName));
			if (EphesoftStringUtil.isNullOrEmpty(tableName)) {
				throw new DCMAApplicationException("Table name is null or empty.");
			}
			final DataTable dataTable = new DataTable();
			dataTable.setName(tableName);
			dataTableList.add(dataTable);
			tableExtractionUtility.initializeHeaderRow(dataTable);
			tableExtractionUtility.initializeDataTableRows(dataTable);
			final List<TableColumnsInfo> tableColumnsInfoList = tableInfo.getTableColumnsInfo();
			if (CollectionUtils.isEmpty(tableColumnsInfoList)) {
				LOGGER.error("Table Columns Info list is null or empty.");
				continue;
			}
			HeaderRow headerRow = dataTable.getHeaderRow();
			HeaderRow.Columns columnsHeader = headerRow.getColumns();
			final List<Column> columnHeaderList = columnsHeader.getColumn();
			tableExtractionUtility.createColumnHeaders(columnHeaderList, tableColumnsInfoList);
			final List<TableExtractionRule> tableExtractionRuleList = tableInfo.getTableExtractionRules();
			final CurrencyCode tableCurrencyCode = tableInfo.getCurrencyCode();
			final String ruleOperatorDefinedForTable = tableInfo.getRuleOperator();
			final boolean isRemoveInvalidRows = tableInfo.isRemoveInvalidRows();
			final List<TableRuleInfo> tableValidationRulesList = tableInfo.getTableRuleInfo();
			LOGGER.debug(EphesoftStringUtil.concatenate("Currency for the table is: ", tableCurrencyCode,
					"\n Rule Operator for table is: ", ruleOperatorDefinedForTable, "\n Do we need to remove invalid rows?: ",
					isRemoveInvalidRows));
			List<LineDataCarrier> lineDataCarrierList = null;
			Map<String, DataCarrier> colHeaderInfoMap = null;
			Map<String, CurrencyCode> columnCurrencyMap = null;
			List<TableColumnVO> tableColumnVOList = null;
			int noOfTableColumns = tableColumnsInfoList.size();
			List<TableExtractionResult> allTableRowsList = new ArrayList<TableExtractionResult>(tableExtractionRuleList.size());
			if (!CollectionUtils.isEmpty(tableExtractionRuleList)) {

				for (TableExtractionRule tableExtractionRule : tableExtractionRuleList) {
					if (null != tableExtractionRule) {
						final String startPattern = tableExtractionRule.getStartPattern();
						if (EphesoftStringUtil.isNullOrEmpty(startPattern)) {
							LOGGER.error("Rule: ", tableExtractionRule.getRuleName(), "'s start pattern is null or empty.");
							continue;
						}
						final String endPattern = tableExtractionRule.getEndPattern();
						TableExtractionAPI tableExtractionAPI = tableExtractionUtility.getTableExtractionAPI(tableExtractionRule
								.getTableAPI());
						final List<TableColumnExtractionRule> tableColumnExtractionRuleList = tableExtractionRule
								.getTableColumnExtractionRules();
						tableColumnVOList = tableExtractionUtility.getTableColumnData(tableColumnsInfoList, tableColumnVOList,
								noOfTableColumns, tableColumnExtractionRuleList);
						lineDataCarrierList = TableRowFinderUtility.searchAllRowOfTablesForTableExtractionWebServvice(pageList,
								startPattern, endPattern, tableFinderService.getFuzzyMatchThresholdValue());
						if (CollectionUtils.isNotEmpty(lineDataCarrierList)) {
							boolean colHeaderValidationRequired = tableExtractionAPI.isColHeaderValidationRequired();
							boolean colCoordValidationRequired = tableExtractionAPI.isColCoordValidationRequired();
							if (colHeaderValidationRequired) {
								colHeaderInfoMap = tableExtractionUtility.getColumnHeaderMap(colHeaderInfoMap, tableColumnVOList);
								tableExtractionUtility.setColumnHeaderInfo(lineDataCarrierList, colHeaderInfoMap, tableColumnVOList,
										tableFinderService.getFuzzyMatchThresholdValue());
							}
							boolean isTableColumnListDirty = tableExtractionUtility.sortTableColumnsInOrderOfOccurance(
									colHeaderInfoMap, tableColumnVOList, colHeaderValidationRequired, colCoordValidationRequired);
							final List<Row> rowList = addDataTablesValues(lineDataCarrierList, tableColumnVOList, colHeaderInfoMap,
									tableExtractionAPI, gapBetweenColumnWords);
							if (CollectionUtils.isNotEmpty(rowList)) {
								columnCurrencyMap = tableExtractionUtility.getColumnCurrencyMap(tableCurrencyCode, columnCurrencyMap,
										noOfTableColumns, tableColumnExtractionRuleList);
								applyTableValidationRulesOnRows(ruleOperatorDefinedForTable, tableValidationRulesList,
										isRemoveInvalidRows, columnCurrencyMap, rowList);
								if (null != columnCurrencyMap) {
									columnCurrencyMap.clear();
								}
								allTableRowsList.add(new TableExtractionResult(rowList, tableExtractionRule.getRuleName()));
							}
							if (isTableColumnListDirty) {
								for (TableExtractionResult list : allTableRowsList) {
									if (null != list) {
										tableExtractionUtility.sortTableRowsInUiOrder(tableColumnsInfoList, list.getRowList());
									}
								}
							}
							if (null != colHeaderInfoMap) {
								colHeaderInfoMap.clear();
							}
							lineDataCarrierList.clear();
						}
						if (null != tableColumnVOList) {
							tableColumnVOList.clear();
						}
					}
				}
			}
			tableExtractionUtility.addBestRuleRows(dataTable, allTableRowsList);
			List<Row> dataTableRowList = dataTable.getRows().getRow();
			if (CollectionUtil.isEmpty(dataTableRowList)) {
				insertEmptyRow(dataTableRowList, tableColumnsInfoList);
				applyTableValidationRulesOnRows(ruleOperatorDefinedForTable, tableValidationRulesList, isRemoveInvalidRows,
						columnCurrencyMap, dataTableRowList);
			}
		}
		return true;
	}

	public void extractFieldsForTestExtraction(String folderLocation, String batchClassIdentifier, Batch batch)
			throws DCMAApplicationException {
		final String switchValue = batchClassPluginPropertiesService.getPropertyValue(batchClassIdentifier, TABLE_EXTRACTION_PLUGIN,
				TableExtractionProperties.TABLE_EXTRACTION_SWITCH);
		if (null != switchValue) {
			setInvalidRuleCharacters(tableFinderService.getInvalidRuleCharacters());
			final int gapBetweenColumnWords = tableFinderService.getGapBetweenColumnWords();

			LOGGER.info("batchClassIdentifier : " + batchClassIdentifier);

			try {
				final List<Document> docTypeList = batch.getDocuments().getDocument();

				if (null == docTypeList) {
					LOGGER.info("In valid batch documents.");
				} else {
					performTableExtraction(folderLocation, batchClassIdentifier, batch, gapBetweenColumnWords, docTypeList);
				}
			} catch (final DCMAApplicationException e) {
				LOGGER.error(e.getMessage());
				throw new DCMAApplicationException(e.getMessage(), e);
			} catch (final Exception e) {
				LOGGER.error(e.getMessage());
				throw new DCMAApplicationException(e.getMessage(), e);
			}

			batchSchemaService.updateBatch(batch);
		} else {
			LOGGER.info("Skipping Table extraction. Switch set as off.");
		}
	}

	/**
	 * Performs table extraction
	 * 
	 * @param folderLocation
	 * @param batchClassIdentifier
	 * @param batch
	 * @param gapBetweenColumnWords
	 * @param docTypeList
	 * @throws DCMAApplicationException
	 */
	private void performTableExtraction(String folderLocation, String batchClassIdentifier, Batch batch, int gapBetweenColumnWords,
			List<Document> docTypeList) throws DCMAApplicationException {
		if (null != batch && null != docTypeList) {
			//BatchClass batchClass = batchClassService.getBatchClassByIdentifier(batchClassIdentifier);
			Map<Document, List<HocrPages>> documentToHOCRMapping = new LinkedHashMap<Document, List<HocrPages>>();
			List<DocumentType> documentTypeList = documentTypeService.getDocTypeByBatchClassIdentifier(batchClassIdentifier);
			
			
			List<String> documentTypeName = new ArrayList<String>();
			if (null != documentTypeList) {
				for (DocumentType docType : documentTypeList) {
					documentTypeName.add(docType.getName());
				}
			}
			if (null != documentTypeList) {
				for (Document document : docTypeList) {
					if (null != document) {
						List<Page> pages = document.getPages().getPage();
						if (null != pages) {
							List<HocrPages> hocrPageList = new ArrayList<HocrPages>();
							for (Page page : pages) {
								HocrPages hocrPage = batchSchemaService.getHocrPagesForTestContent(folderLocation, page
										.getHocrFileName());
								if (null != hocrPage) {
									hocrPageList.add(hocrPage);
								}
							}
							documentToHOCRMapping.put(document, hocrPageList);
						}
					}
				}
				if (null != documentToHOCRMapping) {
					Batch.Documents documents = processDocPageForTestExtraction(gapBetweenColumnWords, documentToHOCRMapping,
							batchClassIdentifier);
					if (null != documents) {
						mergeDocuments(documents, batch);
					}
				}
			}
		}
	}

	private void mergeDocuments(Documents documents, Batch batch) {
		if (null != documents.getDocument()) {
			for (Document document : documents.getDocument()) {
				if (null != document) {
					for (Document batchDocument : batch.getDocuments().getDocument()) {
						if (batchDocument.getIdentifier().equals(document.getIdentifier())) {
							if (null != document.getDataTables()) {
								batchDocument.setDataTables(document.getDataTables());
								break;
							}
						}
					}
				}
			}
		}
	}

}
