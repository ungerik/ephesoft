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

package com.ephesoft.dcma.tablefinder.share;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.DataTable;
import com.ephesoft.dcma.batch.schema.DataTable.Rows;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.HeaderRow;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.Row.Columns;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.common.PatternMatcherUtil;
import com.ephesoft.dcma.core.common.CurrencyCode;
import com.ephesoft.dcma.core.common.TableColumnVO;
import com.ephesoft.dcma.core.common.TableExtractionTechnique;
import com.ephesoft.dcma.core.common.constants.CommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.da.domain.TableColumnExtractionRule;
import com.ephesoft.dcma.da.domain.TableColumnsInfo;
import com.ephesoft.dcma.tablefinder.constants.TableExtractionConstants;
import com.ephesoft.dcma.tablefinder.data.DataCarrier;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * This class serves for basic functions of data table extraction algorithm.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Apr 8, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class DataTableService {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(DataTableService.class);

	/**
	 * Sorts extracted row columns as per UI display or table columns and reverting back the original order of table column infos if
	 * were changed by sorting of column header or column coordinate.
	 * 
	 * @param tableColumnsInfoList {@link List}<{@link TableColumnsInfo}>
	 * @param rowList {@link List}<{@link Row}>
	 */
	public void sortTableRowsInUiOrder(final List<TableColumnsInfo> tableColumnsInfoList, final List<Row> rowList) {
		if (CollectionUtils.isNotEmpty(tableColumnsInfoList) && CollectionUtils.isNotEmpty(rowList)) {
			for (Row row : rowList) {
				if (null != row) {
					List<Column> listOfColumns = row.getColumns().getColumn();
					if (null != listOfColumns) {
						List<Column> newColumnList = setRowColumnData(tableColumnsInfoList, listOfColumns);
						if (CollectionUtils.isNotEmpty(newColumnList)) {
							listOfColumns.clear();
							listOfColumns.addAll(newColumnList);
							newColumnList.clear();
						}
					}
				}
			}
		}
	}

	/**
	 * Sets UI display based ordered column data for a row.
	 * 
	 * @param tableColumnsInfoList {@link List}<{@link TableColumnsInfo}>
	 * @param listOfColumns {@link List}<{@link Column}>
	 * @return {@link List}<{@link Column}>
	 */
	private List<Column> setRowColumnData(final List<TableColumnsInfo> tableColumnsInfoList, final List<Column> listOfColumns) {
		List<Column> newListOfColumns = null;
		if (!CollectionUtil.isEmpty(tableColumnsInfoList) && !CollectionUtil.isEmpty(listOfColumns)) {
			newListOfColumns = new ArrayList<Column>(tableColumnsInfoList.size());
			for (final TableColumnsInfo tableColumnInfo : tableColumnsInfoList) {
				if (null != tableColumnInfo) {
					String tableColumnName = tableColumnInfo.getColumnName();
					if (null != tableColumnName) {
						for (Column column : listOfColumns) {
							if (null != column && tableColumnName.equals(column.getName())) {
								newListOfColumns.add(column);
							}
						}
					}
				}
			}
		}
		return newListOfColumns;
	}

	/**
	 * Initializes header row of the data table.
	 * 
	 * @param dataTable {@link DataTable}
	 */
	public void initializeHeaderRow(final DataTable dataTable) {
		if (null != dataTable) {
			HeaderRow headerRow = dataTable.getHeaderRow();
			if (null == headerRow) {
				headerRow = new HeaderRow();
				dataTable.setHeaderRow(headerRow);
			}
			HeaderRow.Columns columnsHeader = headerRow.getColumns();
			if (null == columnsHeader) {
				columnsHeader = new HeaderRow.Columns();
				headerRow.setColumns(columnsHeader);
			}
		}
	}

	/**
	 * Initializes data rows of the data table.
	 * 
	 * @param dataTable {@link DataTable}
	 */
	public void initializeDataTableRows(final DataTable dataTable) {
		if (null != dataTable) {
			Rows rows = dataTable.getRows();
			if (null == rows) {
				rows = new Rows();
				dataTable.setRows(rows);
			}
		}
	}

	/**
	 * Adds best table extraction rule's resultant row sin the data table rows.
	 * 
	 * @param dataTable {@link DataTable}
	 * @param allTableRowsList {@link List}<{@link TableExtractionResult}>
	 */
	public void addBestRuleRows(final DataTable dataTable, final List<TableExtractionResult> allTableRowsList) {
		if (null != dataTable) {
			TableExtractionResult bestTableExtractionResult = findTableWithMaxConfidence(allTableRowsList);
			if (bestTableExtractionResult != null && CollectionUtils.isNotEmpty(bestTableExtractionResult.getRowList())) {
				Rows rows = dataTable.getRows();
				List<Row> dataTableRowList = rows.getRow();
				dataTableRowList.addAll(bestTableExtractionResult.getRowList());
				dataTable.setRuleName(bestTableExtractionResult.getRuleName());
			}
		}
	}

	/**
	 * Selects a table with maximum valid extracted column values out of list of all tables extraction via multiple table extraction
	 * APIs.
	 * 
	 * @param allTableAPIResultRows {@link List}<{@link TableExtractionResult}>
	 * @return {@link TableExtractionResult}
	 */
	private TableExtractionResult findTableWithMaxConfidence(final List<TableExtractionResult> allTableAPIResultRows) {
		TableExtractionResult mostConfidentTableExtractionRuleRows = null;
		List<Row> extractedRow = null;
		if (CollectionUtils.isNotEmpty(allTableAPIResultRows)) {

			// Bug id #11927: Added to handle boundary case where all table extraction API results have all columns invalid.
			mostConfidentTableExtractionRuleRows = allTableAPIResultRows.get(TableExtractionConstants.START_INDEX);
			float maxResultTableConfidence = TableExtractionConstants.MINIMUM_VALID_VALUES_COUNT;
			for (TableExtractionResult extractionResult : allTableAPIResultRows) {

				// calculator loop
				if (extractionResult != null && CollectionUtils.isNotEmpty(extractionResult.getRowList())) {
					extractedRow = extractionResult.getRowList();
					float confidence = getTableResultConfidence(extractedRow, extractionResult);
					if (confidence > maxResultTableConfidence) {
						maxResultTableConfidence = confidence;
						mostConfidentTableExtractionRuleRows = extractionResult;
					}
				}
			}
			LOGGER.info("Most confident table extraction rule is: ", mostConfidentTableExtractionRuleRows.getRuleName());
		}
		return mostConfidentTableExtractionRuleRows;
	}

	/**
	 * Gets the confidence for the extracted table result rows.
	 * 
	 * @param extractedRow {@link List<{@link Row}>
	 * @param extractionResult {@link TableExtractionResult}
	 * @return float Confidence of the table extraction results.
	 */
	private float getTableResultConfidence(final List<Row> extractedRow, final TableExtractionResult extractionResult) {
		float confidence;
		if (null == extractionResult) {
			confidence = 0;
		} else {
			int resultTableValidColumnCount = TableExtractionConstants.MINIMUM_VALID_VALUES_COUNT;
			int resultTableNumberOfColumns = TableExtractionConstants.MINIMUM_VALID_VALUES_COUNT;
			if (CollectionUtils.isNotEmpty(extractedRow)) {
				for (Row row : extractedRow) {
					if (null != row) {
						Columns rowColumns = row.getColumns();
						if (null != rowColumns) {
							List<Column> columnList = rowColumns.getColumn();
							if (row.isIsRuleValid()) {
								for (Column column : columnList) {
									if (column.isValid()) {
										resultTableValidColumnCount++;
									}
									resultTableNumberOfColumns++;
								}
							} else {
								resultTableNumberOfColumns += columnList.size();
							}
						}

					}
				}
			}
			confidence = resultTableNumberOfColumns == 0 ? 0 : (resultTableValidColumnCount
					* CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE / resultTableNumberOfColumns);
			LOGGER.debug("Confidence of rule: ", extractionResult.getRuleName(), "is: ", confidence);
		}
		return confidence;
	}

	/**
	 * Gets currency map for all table columns.
	 * 
	 * @param tableCurrencyCode {@link CurrencyCode}
	 * @param columnCurrencyMap {@link Map}<{@link String}, {@link CurrencyCode}>
	 * @param noOfTableColumns int
	 * @param tableColumnExtractionRuleList {@link List}<{@link TableColumnExtractionRule}>
	 * @return {@link Map}<{@link String}, {@link CurrencyCode}>
	 */
	public Map<String, CurrencyCode> getColumnCurrencyMap(final CurrencyCode tableCurrencyCode,
			Map<String, CurrencyCode> columnCurrencyMap, final int noOfTableColumns,
			final List<TableColumnExtractionRule> tableColumnExtractionRuleList) {
		if (CollectionUtils.isNotEmpty(tableColumnExtractionRuleList)) {

			// Moved Table Validation rules out of addDataTableValues method.
			if (null == columnCurrencyMap) {
				columnCurrencyMap = new HashMap<String, CurrencyCode>(noOfTableColumns);
			}

			// JIRA-11725
			String tableColumnName = null;
			for (TableColumnExtractionRule tableColumnExtractionRule : tableColumnExtractionRuleList) {
				if (null != tableColumnExtractionRule && tableColumnExtractionRule.isCurrency()) {
					tableColumnName = tableColumnExtractionRule.getColumnName();
					columnCurrencyMap.put(tableColumnName, tableCurrencyCode);
				}
			}
		}
		return columnCurrencyMap;
	}

	/**
	 * Sorts table columns in order of occurrence in the page in processing.
	 * 
	 * @param colHeaderInfoMap {@link Map}<{@link String}, {@link DataCarrier}>
	 * @param tableColumnVOList {@link List}<{@link TableColumnVO}>
	 * @param colHeaderValidationRequired boolean
	 * @param colCoordValidationRequired boolean
	 * @return boolean True if table column list is dirty, else false.
	 * @throws DCMAApplicationException
	 */
	public boolean sortTableColumnsInOrderOfOccurance(final Map<String, DataCarrier> colHeaderInfoMap,
			final List<TableColumnVO> tableColumnVOList, final boolean colHeaderValidationRequired,
			final boolean colCoordValidationRequired) throws DCMAApplicationException {
		boolean isTableColumnListDirty;
		if (colHeaderValidationRequired) {
			sortTableColumns(tableColumnVOList, colHeaderInfoMap);
			LOGGER.info("Table columns are sorted by the order of column headers.");
			isTableColumnListDirty = true;
		} else if (colCoordValidationRequired) {
			sortTableColumns(tableColumnVOList);
			LOGGER.info("Table columns are sorted by the order of column coordinates.");
			isTableColumnListDirty = true;
		} else {
			LOGGER.info("Table columns are in their default User display order.");
			isTableColumnListDirty = false;
		}
		return isTableColumnListDirty;
	}

	/**
	 * Gets map of table columns with their column header regex patterns.
	 * 
	 * @param colHeaderInfoMap {@link Map}<{@link String}, {@link DataCarrier}>
	 * @param tableColumnVOList {@link List}<{@link TableColumnVO}>
	 * @return {@link Map}<{@link String}, {@link DataCarrier}>
	 */
	public Map<String, DataCarrier> getColumnHeaderMap(Map<String, DataCarrier> colHeaderInfoMap,
			final List<TableColumnVO> tableColumnVOList) {
		if (CollectionUtils.isNotEmpty(tableColumnVOList)) {

			// Fill map at first encounter with a rule using column header validation.
			if (null == colHeaderInfoMap) {
				colHeaderInfoMap = new HashMap<String, DataCarrier>(tableColumnVOList.size());
			}
			for (final TableColumnVO tableColumnVO : tableColumnVOList) {
				if (null != tableColumnVO) {
					final String colHeaderRegex = tableColumnVO.getColumnHeaderPattern();
					colHeaderInfoMap.put(tableColumnVO.getColumnName(), new DataCarrier(null, 0, colHeaderRegex, null));
				}
			}
		}
		return colHeaderInfoMap;
	}

	/**
	 * Sorts table columns list on the basis of their column header start coordinates.
	 * 
	 * @param tableColumnList {@link List}<{@link TableColumnVO}>
	 * @param colHeaderInfoMap {@link List}<{@link String}, {@link DataCarrier}>
	 */
	private void sortTableColumns(final List<TableColumnVO> tableColumnList, final Map<String, DataCarrier> colHeaderInfoMap) {
		LOGGER.info("Sorting table columns based on their table headers.");
		if (CollectionUtils.isNotEmpty(tableColumnList) && null != colHeaderInfoMap && !colHeaderInfoMap.isEmpty()) {
			Collections.sort(tableColumnList, new Comparator<TableColumnVO>() {

				@Override
				public int compare(final TableColumnVO object1, final TableColumnVO object2) {
					int comparison;
					if (null == object1 || null == object2) {
						comparison = TableExtractionConstants.EQUAL_COMPARISON;
					} else {
						final String headerMapColumnNameForObject1 = object1.getColumnName();
						final String headerMapColumnNameForObject2 = object2.getColumnName();
						final DataCarrier colHeaderInfoObject1 = colHeaderInfoMap.get(headerMapColumnNameForObject1);
						final DataCarrier colHeaderInfoObject2 = colHeaderInfoMap.get(headerMapColumnNameForObject2);
						if (null == colHeaderInfoObject1 || null == colHeaderInfoObject2) {
							comparison = TableExtractionConstants.EQUAL_COMPARISON;
						} else {

							// Getting coordinates of spans of two table columns.
							final Coordinates object1SpanHeaderCoordinates = colHeaderInfoObject1.getCoordinates();
							final Coordinates object2SpanHeaderCoordinates = colHeaderInfoObject2.getCoordinates();
							if (null == object1SpanHeaderCoordinates || null == object2SpanHeaderCoordinates) {
								comparison = TableExtractionConstants.EQUAL_COMPARISON;
							} else {

								// Comparing start coordinates of headers of two input table columns.
								final BigInteger startCoordinateHeader1 = object1SpanHeaderCoordinates.getX0();
								final BigInteger startCoordinateHeader2 = object2SpanHeaderCoordinates.getX0();
								if (null == startCoordinateHeader1 && null == startCoordinateHeader2) {
									comparison = TableExtractionConstants.EQUAL_COMPARISON;
								} else if (null == startCoordinateHeader1) {
									comparison = TableExtractionConstants.LESS_COMPARISON;
								} else if (null == startCoordinateHeader2) {
									comparison = TableExtractionConstants.GREATER_COMPARISON;
								} else {
									comparison = startCoordinateHeader1.compareTo(startCoordinateHeader2);
								}
							}
						}
					}
					return comparison;
				}
			});
		}
	}

	/**
	 * Sorts table columns list on the basis of their column start coordinates.
	 * 
	 * @param tableColumnList {@link List}<{@link TableColumnVO}>
	 */
	private void sortTableColumns(final List<TableColumnVO> tableColumnList) {
		LOGGER.info("Sorting table columns based on their start end coordinates.");
		if (CollectionUtils.isNotEmpty(tableColumnList)) {
			Collections.sort(tableColumnList, new Comparator<TableColumnVO>() {

				@Override
				public int compare(final TableColumnVO object1, final TableColumnVO object2) {
					int comparison;
					if (null == object1 || null == object2) {
						comparison = TableExtractionConstants.EQUAL_COMPARISON;
					} else {
						String startCoordinate = object1.getColumnStartCoordinate();
						String endCoordinate = object2.getColumnEndCoordinate();
						Integer startCoordinateObject1 = null;
						Integer startCoordinateObject2 = null;
						if (NumberUtils.isDigits(startCoordinate) && NumberUtils.isDigits(endCoordinate)) {
							startCoordinateObject1 = Integer.parseInt(startCoordinate);
							startCoordinateObject2 = Integer.parseInt(endCoordinate);
						}

						// Comparing start coordinates of column coordinates of two input table columns.
						if (null == startCoordinateObject1 && null == startCoordinateObject2) {
							comparison = TableExtractionConstants.EQUAL_COMPARISON;
						} else if (null == startCoordinateObject1) {
							comparison = TableExtractionConstants.LESS_COMPARISON;
						} else if (null == startCoordinateObject2) {
							comparison = TableExtractionConstants.GREATER_COMPARISON;
						} else {
							comparison = startCoordinateObject1.compareTo(startCoordinateObject2);
						}
					}
					return comparison;
				}
			});
		}
	}

	/**
	 * Gets table extraction API object for the selected list of APIs for the table.
	 * 
	 * @param tableExtractionAPIString {@link String}
	 * @return {@link TableExtractionAPI}
	 */
	public TableExtractionAPI getTableExtractionAPI(final String tableExtractionAPIString) {
		LOGGER.trace("Entering method to read the table extraction aPI for the table");
		TableExtractionAPI tableExtractionAPI = new TableExtractionAPI();
		if (EphesoftStringUtil.isNullOrEmpty(tableExtractionAPIString)) {
			tableExtractionAPI.setTableExtractionAPI(TableExtractionTechnique.REGEX_VALIDATION.name());
			LOGGER.info("No validation method specified by admin. Using regex validation for table extraction. Table columns are in their default User display order.");
			tableExtractionAPI.setRegexValidationRequired(true);
		} else {
			tableExtractionAPI.setTableExtractionAPI(tableExtractionAPIString);
			String tableExtractionAPIInUpperCase = tableExtractionAPIString.toUpperCase();
			boolean regexValidationRequired = tableExtractionAPIInUpperCase.contains(TableExtractionTechnique.REGEX_VALIDATION.name());
			boolean colHeaderValidationRequired = tableExtractionAPIInUpperCase
					.contains(TableExtractionTechnique.COLUMN_HEADER_VALIDATION.name());
			boolean colCoordValidationRequired = tableExtractionAPIInUpperCase
					.contains(TableExtractionTechnique.COLUMN_COORDINATES_VALIDATION.name());
			LOGGER.debug("Regex validation used?: ", regexValidationRequired, "Column coordinate validation used?: ",
					colCoordValidationRequired, "Column header validation used?: ", colHeaderValidationRequired);
			tableExtractionAPI.setColCoordValidationRequired(colCoordValidationRequired);
			tableExtractionAPI.setColHeaderValidationRequired(colHeaderValidationRequired);
			tableExtractionAPI.setRegexValidationRequired(regexValidationRequired);
		}
		return tableExtractionAPI;
	}

	/**
	 * Gets consolidated form of table column for extraction.
	 * 
	 * @param tableColumnsInfoList {@link List}<{TableColumnsInfo}>
	 * @param tableColumnVOList {@link List}<{TableColumnVO}>
	 * @param noOfTableColumns int
	 * @param tableColumnExtractionRuleList {@link List}<{TableColumnExtractionRule}>
	 * @return {@link List}<{TableColumnVO}>
	 */
	public List<TableColumnVO> getTableColumnData(final List<TableColumnsInfo> tableColumnsInfoList,
			List<TableColumnVO> tableColumnVOList, final int noOfTableColumns,
			final List<TableColumnExtractionRule> tableColumnExtractionRuleList) {
		for (int columnIndex = 0; columnIndex < noOfTableColumns; columnIndex++) {
			if (null == tableColumnVOList) {
				tableColumnVOList = new ArrayList<TableColumnVO>(noOfTableColumns);
			}

			// create list of extraction and info details of all table columns in a common object.
			final TableColumnVO tableColumnVO = createTableColumnVO(tableColumnsInfoList.get(columnIndex),
					tableColumnExtractionRuleList.get(columnIndex));
			if (null != tableColumnVO) {
				tableColumnVOList.add(tableColumnVO);
			}
		}
		return tableColumnVOList;
	}

	/**
	 * Creates headers for columns of table data.
	 * 
	 * @param columnHeaderList {@link List}<{@link Column}>
	 * @param tableColumnsInfoList {@link List}<{@link TableColumnsInfo}>
	 */
	public void createColumnHeaders(final List<Column> columnHeaderList, final List<TableColumnsInfo> tableColumnsInfoList) {
		if (CollectionUtil.isEmpty(tableColumnsInfoList)) {
			LOGGER.warn("Table column info is null/empty. Column header list would be empty.");
		} else {
			for (final TableColumnsInfo tableColumnsInfo : tableColumnsInfoList) {
				if (null != tableColumnsInfo) {
					final String nameOfColumn = tableColumnsInfo.getColumnName();

					// Create the table header first and then all the columns for the header.
					final Column column = new Column();
					column.setValid(false);
					column.setValidationRequired(false);
					column.setName(nameOfColumn);
					columnHeaderList.add(column);
				}
			}
		}
	}

	/**
	 * Creates an object of table column virtual class that contains all extraction relevant data.
	 * 
	 * @param tableColumnsInfo {@link TableColumnsInfo}
	 * @param tableColumnExtractionRule {@link TableColumnExtractionRule}
	 * @return {@link TableColumnVO}
	 */
	private TableColumnVO createTableColumnVO(final TableColumnsInfo tableColumnsInfo,
			final TableColumnExtractionRule tableColumnExtractionRule) {
		LOGGER.info("Combining data from table column and table column extraction rule into a virtual object for processing of table extraction.");
		TableColumnVO tableColumnVO = null;
		if (null != tableColumnExtractionRule && null != tableColumnsInfo) {
			tableColumnVO = new TableColumnVO();
			tableColumnVO.setColumnName(tableColumnsInfo.getColumnName());
			tableColumnVO.setValidationPattern(tableColumnsInfo.getValidationPattern());
			tableColumnVO.setColumnPattern(tableColumnExtractionRule.getColumnPattern());
			tableColumnVO.setColumnHeaderPattern(tableColumnExtractionRule.getColumnHeaderPattern());
			tableColumnVO.setBetweenLeft(tableColumnExtractionRule.getBetweenLeft());
			tableColumnVO.setBetweenRight(tableColumnExtractionRule.getBetweenRight());
			tableColumnVO.setRequired(tableColumnExtractionRule.isRequired());
			tableColumnVO.setMultilineAnchor(tableColumnExtractionRule.isMandatory());
			Integer endCoordinate = tableColumnExtractionRule.getColumnEndCoordinate();
			if (null == endCoordinate) {
				tableColumnVO.setColumnEndCoordinate(TableExtractionConstants.EMPTY);
			} else {
				tableColumnVO.setColumnEndCoordinate(endCoordinate.toString());
			}
			Integer startCoordinate = tableColumnExtractionRule.getColumnStartCoordinate();
			if (null == startCoordinate) {
				tableColumnVO.setColumnStartCoordinate(TableExtractionConstants.EMPTY);
			} else {
				tableColumnVO.setColumnStartCoordinate(startCoordinate.toString());
			}
			tableColumnVO.setCurrency(tableColumnExtractionRule.isCurrency());
			tableColumnVO.setExtractedDataColumnName(tableColumnExtractionRule.getExtractedDataColumnName());
		}
		return tableColumnVO;
	}

	/**
	 * Extracts and stores the column header information in map.
	 * 
	 * @param lineDataCarrierList {@link List}<{@link LineDataCarrier}>
	 * @param colHeaderInfoMap {@link Map}<{@link String}, {@link DataCarrier}>
	 * @param tableColumnList {@link List}<{@link TableColumnVO}>
	 * @param colHeaderValidationRequired boolean
	 * @param fuzzyMatchThresholdValue float
	 * @throws DCMAApplicationException {@link DCMAApplicationException}
	 */
	public void setColumnHeaderInfo(final List<LineDataCarrier> lineDataCarrierList, final Map<String, DataCarrier> colHeaderInfoMap,
			final List<TableColumnVO> tableColumnList, final float fuzzyMatchThresholdValue) throws DCMAApplicationException {
		if (!CollectionUtils.isEmpty(lineDataCarrierList) && !CollectionUtils.isEmpty(tableColumnList)) {
			for (final TableColumnVO tableColumn : tableColumnList) {
				if (null != tableColumn && null != colHeaderInfoMap) {
					String columnName = tableColumn.getColumnName();
					final DataCarrier colHeaderInfo = colHeaderInfoMap.get(columnName);

					// At this point colHeaderInfo.getValue() contains the regex pattern for column header.
					if (null != colHeaderInfo) {
						String columnHeaderPattern = colHeaderInfo.getValue();
						if (null != columnHeaderPattern) {
							for (final LineDataCarrier lineDataCarrier : lineDataCarrierList) {
								if (null != lineDataCarrier) {
									String rowData = lineDataCarrier.getLineRowData();
									LOGGER.debug("Line Row Data = ", rowData);
									List<Span> spans = lineDataCarrier.getSpanList();
									DataCarrier headerDataCarrier = PatternMatcherUtil.findFuzzyPattern(rowData, columnHeaderPattern,
											fuzzyMatchThresholdValue, spans);
									if (null == headerDataCarrier) {
										headerDataCarrier = setColumnHeaderSpan(rowData, columnHeaderPattern, spans);
									}
									colHeaderInfo.setSpans(headerDataCarrier.getSpans());
									colHeaderInfo.setValue(headerDataCarrier.getValue());
									colHeaderInfo.setConfidence(headerDataCarrier.getConfidence());
									colHeaderInfo.setCoordinates(headerDataCarrier.getCoordinates());

									// At this point colHeaderInfo.getValue() contains the matched value for column header.
									// Added check with respect to ticket #5019: Failure in table extraction using column header validation. 
									if (null != colHeaderInfo.getSpans()) {
										LOGGER.debug("Header span found for column : ", columnName, "; Span : ",
												headerDataCarrier.getValue());
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Sets column header span for a column.
	 * 
	 * @param inputData {@link String}
	 * @param columnHeaderPattern {@link String}
	 * @param spanList {@link List}<{@link Span}>
	 * @throws DCMAApplicationException {@link DCMAApplicationException}
	 */
	private final DataCarrier setColumnHeaderSpan(final String inputData, final String columnHeaderPattern, final List<Span> spanList)
			throws DCMAApplicationException {
		List<Span> bestMatchSpans = null;
		String value = null;
		float bestConfidence = 0;
		Coordinates bestMatchSpanCoordinates = null;
		if (EphesoftStringUtil.isNullOrEmpty(inputData)) {
			LOGGER.warn("Invalid input character sequence of line.");
		} else {
			if (EphesoftStringUtil.isNullOrEmpty(columnHeaderPattern)) {
				LOGGER.warn("Invalid input pattern sequence.");
			} else {

				// Compile and use regular expression
				final CharSequence inputStr = inputData;
				final Pattern pattern = Pattern.compile(columnHeaderPattern);
				final Matcher matcher = pattern.matcher(inputStr);
				float previousConfidence = 0;
				final List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
				while (matcher.find()) {

					// Get all groups for this match
					for (int i = 0; i <= matcher.groupCount(); i++) {
						final String groupString = matcher.group(i);
						if (groupString != null) {
							final int startIndex = matcher.start();
							final int endIndex = startIndex + groupString.trim().length();
							List<Span> matchedSpans = PatternMatcherUtil.getMatchedSpans(spanList, startIndex, endIndex);
							String headerValue = PatternMatcherUtil.getMatchedSpansValue(matchedSpans);
							if (!EphesoftStringUtil.isNullOrEmpty(headerValue)) {
								final float confidence = (groupString.length() * CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE)
										/ headerValue.length();
								LOGGER.info("Matched Value : ", groupString, " ,Confidence : ", confidence);
								if (confidence > previousConfidence) {
									bestMatchSpans = matchedSpans;
									bestConfidence = confidence;
									value = headerValue;
									previousConfidence = confidence;
									coordinatesList.clear();
									for (Span span : matchedSpans) {
										coordinatesList.add(span.getCoordinates());
									}
									bestMatchSpanCoordinates = HocrUtil.getRectangleCoordinates(coordinatesList);
								}
								LOGGER.info(groupString);
							}
						}
					}
				}
			}
		}
		DataCarrier dataCarrier = new DataCarrier(bestMatchSpans, bestConfidence, value, bestMatchSpanCoordinates);
		return dataCarrier;
	}
}
