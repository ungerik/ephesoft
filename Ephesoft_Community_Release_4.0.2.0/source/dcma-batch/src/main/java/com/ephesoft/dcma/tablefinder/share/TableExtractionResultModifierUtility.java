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
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.collections.CollectionUtils;

import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.core.common.TableColumnVO;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.tablefinder.constants.TableExtractionConstants;
import com.ephesoft.dcma.tablefinder.data.DataCarrier;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

public class TableExtractionResultModifierUtility {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(TableExtractionResultModifierUtility.class);

	/**
	 * This method merges the value and coordinates of the multiline rows.
	 * 
	 * @param previousRowColumnsList {@link List}<{@link Column}>
	 * @param currentRowColumnsList {@link List}<{@link Column}>
	 */
	public static void mergeMultilineRows(final List<Column> previousRowColumnsList, final List<Column> currentRowColumnsList) {
		LOGGER.trace("Entering method to Merge data of mutilple rows.");
		if (null != previousRowColumnsList && null != currentRowColumnsList) {
			Iterator<Column> previousRowColumnsListIterator = previousRowColumnsList.iterator();
			Iterator<Column> currentRowColumnsListIterator = currentRowColumnsList.iterator();
			while (previousRowColumnsListIterator.hasNext() && currentRowColumnsListIterator.hasNext()) {
				final Column previousRowColumn = previousRowColumnsListIterator.next();
				final Column currentRowColumn = currentRowColumnsListIterator.next();
				mergeColumns(previousRowColumn, currentRowColumn);
			}
		} else {
			LOGGER.error("previousRowColumnsList or currentRowColumnsList is null. Row data merging failed.");
		}
		LOGGER.trace("Exiting method to Merge data of mutilple rows.");
	}

	/**
	 * Merges column of two rows to first column.
	 * 
	 * @param prevColumnData {@link Column}
	 * @param newColumnData {@link Column}
	 */
	private static void mergeColumns(final Column prevColumnData, final Column newColumnData) {
		LOGGER.trace("Entering method to merge previous column data with new column data.");
		if (null != prevColumnData && null != newColumnData && validateColumnForMerging(prevColumnData, newColumnData)) {
			LOGGER.debug("Column name is : ", newColumnData.getName(),
					".\nPrevious data for column is: ", prevColumnData.getValue(), ", new data for column is: ",
					newColumnData.getValue());

			StringBuilder updatedColumnValue = new StringBuilder();

			// if previous row has some data for this column.
			if (null != prevColumnData.getValue() && !prevColumnData.getValue().isEmpty()) {
				updatedColumnValue.append(prevColumnData.getValue());
				updatedColumnValue.append(TableExtractionConstants.SPACE);
			}

			// if current row has some data for this column.
			if (null != newColumnData.getValue() && !newColumnData.getValue().isEmpty()) {
				updatedColumnValue.append(newColumnData.getValue());
			}
			prevColumnData.setValue(updatedColumnValue.toString());
			LOGGER.debug("Merged value is :", prevColumnData.getValue());
			if (prevColumnData.getCoordinatesList() == null) {
				prevColumnData.setCoordinatesList(new CoordinatesList());
			}
			LOGGER.info("Updating coordinates of the merged column.");
			if (!areColumnCoordinatesNull(newColumnData)) {
				for (Coordinates coordinates : newColumnData.getCoordinatesList().getCoordinates()) {
					if (!areColumnCoordinatesNull(prevColumnData)) {
						prevColumnData.getCoordinatesList().getCoordinates().add(coordinates);
					}
				}
			}
		}
		LOGGER.trace("Exiting method to merge previous column data with new column data.");
	}

	/**
	 * Checks if column coordinates are null.
	 * 
	 * @param column {@link Column}
	 * @return true if column coordinates are null.
	 */
	private static boolean areColumnCoordinatesNull(final Column column) {
		boolean areNull;
		CoordinatesList coordinateList = column.getCoordinatesList();
		if (null == coordinateList) {
			areNull = true;
		} else {
			List<Coordinates> currentCoordinates = coordinateList.getCoordinates();
			if (null == currentCoordinates) {
				areNull = true;
			} else {
				areNull = false;
			}
		}
		return areNull;
	}

	/**
	 * Validates a column for merging.
	 * 
	 * @param prevColumn {@link Column}
	 * @param column {@link Column}
	 * @return {@code boolean} True if column is valid for merging.
	 */
	private static boolean validateColumnForMerging(final Column prevColumn, final Column column) {
		boolean isValid;
		if (column != null && null != prevColumn && !EphesoftStringUtil.isNullOrEmpty(column.getValue())) {
			CoordinatesList columnCoordinatesList = column.getCoordinatesList();
			CoordinatesList prevColumnCoordinatesList = prevColumn.getCoordinatesList();
			if (null != columnCoordinatesList && !columnCoordinatesList.getCoordinates().isEmpty()) {
				if (null == prevColumnCoordinatesList || CollectionUtils.isEmpty(prevColumnCoordinatesList.getCoordinates())) {
					isValid = true;
				} else {
					Coordinates prevColumnCoordinates = HocrUtil.getRectangleCoordinates(prevColumnCoordinatesList.getCoordinates());
					if (null == prevColumnCoordinates) {
						isValid = true;
					} else {
						Coordinates columnCoordinates = HocrUtil.getRectangleCoordinates(column.getCoordinatesList().getCoordinates());
						isValid = isColumnValidWithPrevColumnCoordinate(prevColumnCoordinates.getX0(), prevColumnCoordinates.getX1(),
								columnCoordinates.getX0(), columnCoordinates.getX1());
					}
				}
			} else {
				isValid = false;
			}
		} else {
			isValid = false;
		}
		return isValid;
	}

	/**
	 * Checks if new column data is in same alignment with previous column data
	 * 
	 * @param prevColumnX0 {@link BigInteger}
	 * @param prevColumnX1 {@link BigInteger}
	 * @param columnX0 {@link BigInteger}
	 * @param columnX1 {@link BigInteger}
	 * @return {@code boolean} True if new column data value coordinates.
	 */
	private static boolean isColumnValidWithPrevColumnCoordinate(final BigInteger prevColumnX0, final BigInteger prevColumnX1,
			final BigInteger columnX0, final BigInteger columnX1) {
		LOGGER.trace("Entering method isColumnValidWithPrevColumnCoordinate.....");
		boolean isValid = false;
		LOGGER.debug("New Column Coordinates: X0=", columnX0, " ,X1=", columnX1);
		LOGGER.debug("Previous column Coordinates: X0=", prevColumnX0, " ,X1=", prevColumnX1);
		if (prevColumnX0 != null && prevColumnX1 != null && columnX0 != null && columnX0 != null) {
			if ((columnX0.compareTo(prevColumnX0) == 1 && columnX0.compareTo(prevColumnX1) == -1)
					|| (columnX1.compareTo(prevColumnX0) == 1 && columnX1.compareTo(prevColumnX1) == -1)
					|| (columnX0.compareTo(prevColumnX0) == -1 && columnX1.compareTo(prevColumnX1) == 1)
					|| columnX0.compareTo(prevColumnX0) == 0 || columnX1.compareTo(prevColumnX1) == 0
					|| TableRowFinderUtility.isApproxEqual(columnX1.intValue(), prevColumnX0.intValue())
					|| TableRowFinderUtility.isApproxEqual(columnX0.intValue(), prevColumnX1.intValue())) {
				isValid = true;
			}
		}
		LOGGER.info("Is value coordinates valid with column coordinates : ", isValid);
		LOGGER.trace("Exiting method isColumnValidWithColCoord.....");
		return isValid;
	}

	/**
	 * This method gets the rectangle coordinates for the new merged row value.
	 * 
	 * @param coordinatesList {@link Row}
	 * @param currentRow {@link Row}
	 */
	public static void setMergedRowCoordinates(final Row previousRow, final Row currentRow) {
		LOGGER.trace("Entering method to merge coordinates of multiline column row.");
		if (null == previousRow || null == currentRow) {
			LOGGER.error("PreviousRow or currentRow is null. Coordinates of row could not be set.");
		} else {
			BigInteger minX0 = BigInteger.ZERO;
			BigInteger minY0 = BigInteger.ZERO;
			BigInteger maxX1 = BigInteger.ZERO;
			BigInteger maxY1 = BigInteger.ZERO;

			final Coordinates previousRowCoordinates = previousRow.getRowCoordinates();
			final Coordinates currentRowCoordinates = currentRow.getRowCoordinates();
			if (null == previousRowCoordinates || null == currentRowCoordinates) {
				LOGGER.error("previousRowCoordinates or currentRowCoordinates are null. Coordinates of row could not be set.");
			} else {
				final BigInteger previousRowX0 = previousRowCoordinates.getX0();
				final BigInteger previousRowY0 = previousRowCoordinates.getY0();
				final BigInteger previousRowX1 = previousRowCoordinates.getX1();
				final BigInteger previousRowY1 = previousRowCoordinates.getY1();

				final BigInteger currentRowX0 = currentRowCoordinates.getX0();
				final BigInteger currentRowY0 = currentRowCoordinates.getY0();
				final BigInteger currentRowX1 = currentRowCoordinates.getX1();
				final BigInteger currentRowY1 = currentRowCoordinates.getY1();

				if (previousRowX0.compareTo(currentRowX0) < 0) {
					minX0 = previousRowX0;
				} else {
					minX0 = currentRowX0;
				}
				if (previousRowY0.compareTo(currentRowY0) < 0) {
					minY0 = previousRowY0;
				} else {
					minY0 = currentRowY0;
				}
				if (previousRowX1.compareTo(currentRowX1) > 0) {
					maxX1 = previousRowX1;
				} else {
					maxX1 = currentRowX1;
				}
				if (previousRowY1.compareTo(currentRowY1) > 0) {
					maxY1 = previousRowY1;
				} else {
					maxY1 = currentRowY1;
				}

				final Coordinates coordinates = new Coordinates();
				coordinates.setX0(minX0);
				coordinates.setX1(maxX1);
				coordinates.setY0(minY0);
				coordinates.setY1(maxY1);
				previousRow.setRowCoordinates(coordinates);
				LOGGER.debug("Coordinates of row could are set: (X0, Y0, X1, Y1) are (", minX0, minY0,
						maxX1, maxY1, ").");
			}
		}
		LOGGER.trace("Exiting method to merge coordinates of multiline column row.");
	}

	/**
	 * Populates the extracted value from another column after applying regex pattern to that value.
	 * 
	 * @param lineDataCarrier {@link LineDataCarrier} The instance of LineDataCarrier.
	 * @param tableColumn {@link TableColumnVO} The table column info object.
	 * @param listColumns {@link List <{@link Column}> List of Column details.
	 * @throws DCMAApplicationException {@link DCMAApplicationException} The exception being thrown.
	 */
	public static void populateExtractedColumnValue(final LineDataCarrier lineDataCarrier, final TableColumnVO tableColumn,
			final List<Column> listColumns) throws DCMAApplicationException {
		final Column extractedColumn = getExtractedColumn(tableColumn, listColumns);
		if (extractedColumn != null) {
			applyRegexPatternOnExtractedValue(lineDataCarrier, tableColumn, listColumns, extractedColumn);
		}
	}

	/**
	 * Returns the Column details from which user needs to extract the data.
	 * 
	 * @param tableColumn {@link TableColumnVO} The table column info object.
	 * @param listColumns {@link List <{@link Column}> List of Column details.
	 * @return the column object from which user needs to extract the data.
	 */
	private static Column getExtractedColumn(final TableColumnVO tableColumn, final List<Column> listColumns) {
		Column extractedColumn = null;
		for (Column column : listColumns) {
			if (column != null && column.getName() != null && column.getName().equals(tableColumn.getExtractedDataColumnName())) {
				extractedColumn = column;
				break;
			}
		}
		return extractedColumn;
	}

	/**
	 * Applies the regex pattern on the value of the column from which data need to be extracted and set the value to that column after
	 * regex pattern is being applied.
	 * 
	 * @param lineDataCarrier {@link LineDataCarrier} The instance of LineDataCarrier.
	 * @param tableColumn {@link TableColumnVO} The table column info object.
	 * @param listColumns {@link List <{@link Column}> List of Column details.
	 * @param extractedColumn {@link Column} The extracted column Object.
	 * @throws DCMAApplicationException The exception being thrown.
	 */
	private static void applyRegexPatternOnExtractedValue(final LineDataCarrier lineDataCarrier, final TableColumnVO tableColumn,
			final List<Column> listColumns, final Column extractedColumn) throws DCMAApplicationException {
		if (extractedColumn != null) {
			final String extractedColumnValue = extractedColumn.getValue();
			final List<Coordinates> listCoordinates = extractedColumn.getCoordinatesList().getCoordinates();
			List<Span> listSpans = null;
			Span span = null;
			String pageID = null;
			String patternOfColumnData = null;
			List<DataCarrier> dataCarrierList = null;

			for (Column column : listColumns) {
				if (column != null && column.getName().equals(tableColumn.getColumnName())) {
					listSpans = new ArrayList<Span>();
					span = new Span();
					listSpans.add(span);
					span.setValue(extractedColumnValue);
					span.setCoordinates(HocrUtil.getRectangleCoordinates(listCoordinates));
					pageID = lineDataCarrier.getPageID();
					LOGGER.debug("Extracting column data for column = ", tableColumn.getColumnName());
					patternOfColumnData = tableColumn.getColumnPattern();
					LOGGER.info("Column Pattern : ", patternOfColumnData);
					if (!EphesoftStringUtil.isNullOrEmpty(patternOfColumnData)) {
						dataCarrierList = TableRowFinderUtility.findPattern(extractedColumnValue, patternOfColumnData, listSpans);
						if (null == dataCarrierList || dataCarrierList.isEmpty()) {
							LOGGER.info("No data found for table column = ",
									tableColumn.getColumnName());
						} else {
							setExtractedColumnValue(column, dataCarrierList, pageID, listCoordinates);
						}
					}
					break;
				}
			}
		}
	}

	/**
	 * Set extracted value for column.
	 * 
	 * @param column {@link Column}
	 * @param dataCarrierList {@link List}<{@link DataCarrier}>
	 * @param pageID {@link String}
	 * @param listCoordinates {@link List}<{@link Coordinates}>
	 */
	private static void setExtractedColumnValue(final Column column, final List<DataCarrier> dataCarrierList, final String pageID,
			final List<Coordinates> listCoordinates) {
		if (null != column && CollectionUtils.isNotEmpty(dataCarrierList)) {
			List<Coordinates> coordinatesList = null;
			float maxConfidence = 0;
			int indexOfMostConfidenceDataCarrier = TableExtractionConstants.START_INDEX;
			int dataListSize = dataCarrierList.size();
			int dataCarrierListIndex = 0;
			while (dataCarrierListIndex < dataListSize) {
				DataCarrier outputDataCarrier = dataCarrierList.get(dataCarrierListIndex);
				if (outputDataCarrier == null) {
					continue;
				}
				float dataCarrierConfidence = outputDataCarrier.getConfidence();
				if (dataCarrierConfidence > maxConfidence) {
					maxConfidence = dataCarrierConfidence;
					indexOfMostConfidenceDataCarrier = dataCarrierListIndex;
				}
				dataCarrierListIndex++;
			}
			dataCarrierListIndex = 0;
			while (dataCarrierListIndex < dataListSize) {
				DataCarrier outputDataCarrier = dataCarrierList.get(dataCarrierListIndex);
				if (outputDataCarrier == null) {
					continue;
				}
				if (dataCarrierListIndex == indexOfMostConfidenceDataCarrier) {
					column.setValue(outputDataCarrier.getValue());
					column.setConfidence(outputDataCarrier.getConfidence());
					List<Span> spans = outputDataCarrier.getSpans();
					if (null != spans) {
						coordinatesList = new ArrayList<Coordinates>(spans.size());
						for (Span dataCarrierSpan : spans) {
							coordinatesList.add(dataCarrierSpan.getCoordinates());
						}
					}
					TableRowFinderUtility.setColumnCoordinates(coordinatesList, column);
				} else {
					TableRowFinderUtility.addAlternateValues(column, outputDataCarrier.getValue(), outputDataCarrier.getConfidence(),
							listCoordinates);
				}
				dataCarrierListIndex++;
			}
		}
	}

	/**
	 * This method validates the table column value with the table column validation pattern.
	 * 
	 * @param tableColumn {@link TableColumnVO} The table column virtual object.
	 * @param column {@link Column} The column.
	 */
	public static void validateColumnByValidationPattern(final TableColumnVO tableColumn, final Column column) {
		if (null != column && null != tableColumn) {
			try {
				final String validationPattern = tableColumn.getValidationPattern();
				final String columnValue = column.getValue();
				column.setValid(isValidWithPattern(columnValue, validationPattern));
			} catch (final PatternSyntaxException patternSyntaxException) {
				LOGGER.info("Exception occured in Pattern Matching. ", patternSyntaxException);
				column.setValid(false);
			}
		}
	}

	/**
	 * Checks if a string matches with regex pattern.
	 * 
	 * @param value {@link String}
	 * @param pattern {@link String}
	 * @return boolean True if value matches pattern else false.
	 */
	public static boolean isValidWithPattern(final String value, final String pattern) {
		boolean isValid = true;
		if (!EphesoftStringUtil.isNullOrEmpty(pattern) && value != null) {
			LOGGER.info("Validation Pattern for the Column is : ", pattern);
			isValid = Pattern.matches(pattern, value.trim());
		}
		return isValid;
	}

	/**
	 * Performs a) Extraction of column data from other column data. b) Check if row is valid for all required columns, if not removes
	 * that row from rowList. c) validates column is valid w.r.t validation pattern.
	 * 
	 * @param tableColumnList {@link List}<{@link TableColumnVO}>
	 * @param rowList {@link List}<{@link Row}>
	 * @param isRowValidForAllRequiredColumns boolean
	 * @param previousRow {@link Row}
	 * @param lineDataCarrier {@link LineDataCarrier}
	 * @return boolean true if currently last row in rowList is valid for all required columns.
	 * @throws{@link DCMAApplicationException}
	 */
	public static boolean finishTasksForPreviousRow(final List<TableColumnVO> tableColumnList, final List<Row> rowList,
			boolean isRowValidForAllRequiredColumns, Row previousRow, final LineDataCarrier lineDataCarrier)
			throws DCMAApplicationException {
		if (previousRow.getColumns() != null) {
			final List<Column> previousRowColumnsList = previousRow.getColumns().getColumn();
			for (int index = 0; index < tableColumnList.size(); index++) {
				TableColumnVO tableColumn = tableColumnList.get(index);
				Column column = previousRowColumnsList.get(index);
				if (tableColumn != null && !EphesoftStringUtil.isNullOrEmpty(tableColumn.getExtractedDataColumnName())) {
					populateExtractedColumnValue(lineDataCarrier, tableColumn, previousRowColumnsList);
				}
				validateColumnByValidationPattern(tableColumn, column);
				if (null != column && tableColumn.isRequired() && !column.isValid()) {
					LOGGER.info("Data not valid for required column: ", column.getName());
					isRowValidForAllRequiredColumns = false;
					break;
				}
			}
		}
		if (null != rowList && !isRowValidForAllRequiredColumns) {
			rowList.remove(previousRow);
		}
		return isRowValidForAllRequiredColumns;
	}
}
