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
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import com.ephesoft.dcma.tablefinder.data.SearchTableConfigDataCarrier;
import com.ephesoft.dcma.tablefinder.data.TableBoundaryDataCarrier;
import com.ephesoft.dcma.batch.schema.Column;
import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.dcma.batch.schema.HocrPages;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.dcma.batch.service.BatchSchemaService;
import com.ephesoft.dcma.batch.service.EphesoftContext;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.common.PatternMatcherUtil;
import com.ephesoft.dcma.core.common.ExpressionEvaluator;
import com.ephesoft.dcma.core.common.TableColumnVO;
import com.ephesoft.dcma.core.common.TableExtractionTechnique;
import com.ephesoft.dcma.core.common.constants.CommonConstants;
import com.ephesoft.dcma.core.component.ICommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.tablefinder.constants.TableExtractionConstants;
import com.ephesoft.dcma.tablefinder.data.DataCarrier;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * This class is the utility class to find and extract rows of the table data.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Apr 8, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class TableRowFinderUtility {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(TableRowFinderUtility.class);
	
	private static final String pageIdentifier = "PG";

	/**
	 * This method extracts all the rows, i.e, all rows between start and end pattern.
	 * 
	 * @param hocrPage {@link HocrPage}
	 * @param startPattern {@link String}
	 * @param endPattern {@link String}
	 * @param fuzzyMatchThresholdValue float
	 * @return {@link List}<{@link LineDataCarrier}>
	 * @throws DCMAApplicationException
	 */
	public static List<LineDataCarrier> searchAllRowOfTables(final HocrPage hocrPage, final String startPattern,
			final String endPattern, final float fuzzyMatchThresholdValue) throws DCMAApplicationException {
		List<LineDataCarrier> lineDataCarrierList = new LinkedList<LineDataCarrier>();
		LineDataCarrier lineDataCarrier = new LineDataCarrier(hocrPage.getPageID());
		SearchTableConfigDataCarrier searchTableConfigDataCarrier = new SearchTableConfigDataCarrier(startPattern, endPattern,
				fuzzyMatchThresholdValue);
		TableBoundaryDataCarrier tableBoundaryDataCarrier = new TableBoundaryDataCarrier();
		getTableLines(searchTableConfigDataCarrier, lineDataCarrierList, hocrPage, lineDataCarrier, tableBoundaryDataCarrier);
		return lineDataCarrierList;
	}

	/**
	 * Get lines of the table area in a page.
	 * 
	 * @param searchTableConfigDataCarrier {@link SearchTableConfigDataCarrier}
	 * @param lineDataCarrierList {@link List}<{@link LineDataCarrier}>
	 * @param hocrPage {@link HocrPage}
	 * @param lineDataCarrier {@link LineDataCarrier} Line data carrier that will have new selected spans.
	 * @param tableBoundaryDataCarrier
	 * @throws DCMAApplicationException
	 */
	private static void getTableLines(final SearchTableConfigDataCarrier searchTableConfigDataCarrier,
			List<LineDataCarrier> lineDataCarrierList, final HocrPage hocrPage, LineDataCarrier lineDataCarrier,
			TableBoundaryDataCarrier tableBoundaryDataCarrier) throws DCMAApplicationException {
		final String pageID = hocrPage.getPageID();
		LOGGER.debug("HocrPage page ID : ", pageID);
		final Spans spans = hocrPage.getSpans();
		final List<Span> linkedList = getSortedSpanList(spans);
		if (null == linkedList || linkedList.isEmpty()) {
			LOGGER.debug("Return linked list is null for the page id = ", pageID);
		} else {
			final ListIterator<Span> listItr = linkedList.listIterator();
			if (null == listItr) {
				LOGGER.debug("Return list iterator is null for the page id = ", pageID);
			} else {
				final String startPattern = searchTableConfigDataCarrier.getStartPattern();
				final float fuzzyMatchThresholdValue = searchTableConfigDataCarrier.getFuzzyMatchThresholdValue();
				List<DataCarrier> startDataCarrier = tableBoundaryDataCarrier.getStartDataCarrier();
				if (null == startDataCarrier) {
					while (listItr.hasNext()) {
						final Span span = listItr.next();
						try {
							List<Span> spanList = lineDataCarrier.getSpanList();
							if (spanList.isEmpty()) {
								spanList.add(span);
							} else {
								if (null != span) {
									if (isSameLineSpan(spanList, span)) {
										spanList.add(span);
									} else {
										lineDataCarrier = new LineDataCarrier(pageID);
										spanList = lineDataCarrier.getSpanList();
										spanList.add(span);
									}
								}
							}
							final DataCarrier finalDataCarrier = PatternMatcherUtil.findFuzzyPattern(lineDataCarrier.getLineRowData(),
									startPattern, fuzzyMatchThresholdValue, spanList);
							if (null == finalDataCarrier) {
								startDataCarrier = findPattern(lineDataCarrier.getLineRowData(), startPattern, spanList);
							} else {
								startDataCarrier = new ArrayList<DataCarrier>(0);
							}
							if (null != startDataCarrier) {
								LOGGER.debug("Start pattern found for table where start pattern : ", startPattern);
								addLine(lineDataCarrierList, lineDataCarrier);
								break;
							}
						} catch (final Exception exception) {
							LOGGER.error(exception.getMessage(), exception);
						}
					}
				}
				if (startDataCarrier != null) {
					tableBoundaryDataCarrier.setStartDataCarrier(startDataCarrier);
					LOGGER.info("Finding end pattern.");
					while (listItr.hasNext()) {
						final Span span = listItr.next();
						List<Span> spanList = lineDataCarrier.getSpanList();
						if (spanList.isEmpty()) {
							spanList.add(span);
						} else {
							if (isSameLineSpan(spanList, span)) {
								spanList.add(span);
							} else {
								lineDataCarrier = new LineDataCarrier(pageID);
								addLine(lineDataCarrierList, lineDataCarrier);
								spanList = lineDataCarrier.getSpanList();
								spanList.add(span);
							}
						}
						List<DataCarrier> endDataCarrier = null;
						final String endPattern = searchTableConfigDataCarrier.getEndPattern();
						final DataCarrier finalDataCarrier = PatternMatcherUtil.findFuzzyPattern(lineDataCarrier.getLineRowData(),
								endPattern, fuzzyMatchThresholdValue, spanList);
						if (null == finalDataCarrier) {
							endDataCarrier = findPattern(lineDataCarrier.getLineRowData(), endPattern, lineDataCarrier.getSpanList());
						} else {
							endDataCarrier = new ArrayList<DataCarrier>(0);
						}
						if (null != endDataCarrier) {
							LOGGER.debug("End pattern found for table where end pattern : ", endPattern);
							tableBoundaryDataCarrier.setEndDataCarrier(endDataCarrier);
							break;
						}
					}
				} else {
					LOGGER.debug("No start pattern found for table where start pattern : ", startPattern);
				}
			}
		}
	}

	/**
	 * This method extracts all the rows, i.e, all rows between start and end pattern.
	 * 
	 * @param pageList {@link List}<{@link Page}>
	 * @param startPattern {@link String}
	 * @param endPattern {@link String}
	 * @param batchInstanceIdentifier {@link String}
	 * @param fuzzyMatchThresholdValue float
	 * @return {@link List}<{@link LineDataCarrier}>
	 * @throws DCMAApplicationException
	 */
	public static List<LineDataCarrier> searchAllRowOfTables(final List<Page> pageList, final String startPattern,
			final String endPattern, final String batchInstanceIdentifier, final float fuzzyMatchThresholdValue)
			throws DCMAApplicationException {
		List<LineDataCarrier> lineDataCarrierList = new LinkedList<LineDataCarrier>();
		boolean isFirstPage = true;
		final BatchSchemaService batchSchemaService = EphesoftContext.get(BatchSchemaService.class);
		SearchTableConfigDataCarrier searchTableConfigDataCarrier = new SearchTableConfigDataCarrier(startPattern, endPattern,
				fuzzyMatchThresholdValue);
		TableBoundaryDataCarrier tableBoundaryDataCarrier = new TableBoundaryDataCarrier();
		for (final Page pageType : pageList) {
			final String pageID = pageType.getIdentifier();
			final HocrPages hocrPages = batchSchemaService.getHocrPages(batchInstanceIdentifier, pageType.getHocrFileName());
			if (null == hocrPages) {
				throw new DCMAApplicationException(EphesoftStringUtil.concatenate("No Hocr files found for page id : ", pageID));
			}
			final List<HocrPage> hocrPageList = hocrPages.getHocrPage();
			final HocrPage hocrPage = hocrPageList.get(0);

			LOGGER.debug("HocrPage page ID : ", pageID);
			LineDataCarrier lineDataCarrier = new LineDataCarrier(pageID);
			if (!isFirstPage) {
				addLine(lineDataCarrierList, lineDataCarrier);
			}
			isFirstPage = false;
			getTableLines(searchTableConfigDataCarrier, lineDataCarrierList, hocrPage, lineDataCarrier, tableBoundaryDataCarrier);
			if (null != tableBoundaryDataCarrier.getEndDataCarrier()) {
				break;
			}
		}
		return lineDataCarrierList;
	}

	/**
	 * Checks if two coordinates are nearly equal.
	 * 
	 * @param first int
	 * @param second int
	 * @return boolean
	 */
	public static boolean isApproxEqual(final int first, final int second) {
		boolean result;
		int compare = first - second;
		if (compare < 0) {
			compare = -compare;
		}
		if (compare <= 5) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * Algorithm for comparison: if old span's y coordinate's mid lies within range of new span's y coordinates or not.
	 * 
	 * @param spanList {@link List}<{@link Span}>
	 * @param span {@link Span}
	 * @return boolean
	 */
	private static boolean isSameLineSpan(final List<Span> spanList, final Span span) {
		final Span lastSpan = spanList.get(spanList.size() - 1);
		final BigInteger s1Y0 = span.getCoordinates().getY0();
		final BigInteger s1Y1 = span.getCoordinates().getY1();
		final BigInteger s2Y0 = lastSpan.getCoordinates().getY0();
		final BigInteger s2Y1 = lastSpan.getCoordinates().getY1();
		final BigInteger s2Y = s2Y1.add(s2Y0);
		final int oldSpanMid = s2Y.intValue() / 2;
		boolean isSameLineSpan = oldSpanMid >= s1Y0.intValue() && oldSpanMid <= s1Y1.intValue();
		return isSameLineSpan;
	}

	/**
	 * Adds a new new line to list of data carriers for lines.
	 * 
	 * @param listOfLines {@link List}<{@link LineDataCarrier}>
	 * @param newLine {@link LineDataCarrier}
	 */
	private static void addLine(List<LineDataCarrier> listOfLines, LineDataCarrier newLineDataCarrier) {
		if (null == listOfLines) {
			listOfLines = new LinkedList<LineDataCarrier>();
		}
		listOfLines.add(newLineDataCarrier);
	}

	/**
	 * Gets sorted list of spans of the page.
	 * 
	 * @param spans {@link Spans}
	 * @return {@link List}<{@link Span}>
	 */
	private static List<Span> getSortedSpanList(final Spans spans) {
		final List<Span> spanList = spans.getSpan();
		final Set<Span> set = new TreeSet<Span>(new Comparator<Span>() {

			public int compare(final Span firstSpan, final Span secSpan) {
				BigInteger s1Y0 = firstSpan.getCoordinates().getY0();
				BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
				final BigInteger s2Y0 = secSpan.getCoordinates().getY0();
				final BigInteger s2Y1 = secSpan.getCoordinates().getY1();
				int halfOfSecSpan = (s2Y1.intValue() - s2Y0.intValue()) / 2;
				int y1 = s2Y1.intValue() + halfOfSecSpan;
				int y0 = s2Y0.intValue() - halfOfSecSpan;

				// following if else code is to handle abnormal(out of synch) value y0 or y1 coordinate of new span.
				if (isApproxEqual(s1Y0.intValue(), s2Y0.intValue()) && s1Y1.intValue() > y1) {
					s1Y1 = BigInteger.valueOf(y1);
					firstSpan.getCoordinates().setY1(s1Y1);
				} else if (isApproxEqual(s1Y1.intValue(), s2Y1.intValue()) && s1Y0.intValue() < y0) {
					s1Y0 = BigInteger.valueOf(y0);
					firstSpan.getCoordinates().setY0(s1Y0);
				}
				final BigInteger s1Y = s1Y1.add(s1Y0);
				final BigInteger s2Y = s2Y1.add(s2Y0);

				// calculating middle of old span.
				final int oldSpanMid = s2Y.intValue() / 2;
				int returnValue = 0;

				// if old span's y coordinate's middle lies within range of new span's y coordinates or not. if true, the two spans
				// belong to same line compare them further on their x coordinates, else they belong to two different lines.
				if (oldSpanMid >= s1Y0.intValue() && oldSpanMid <= s1Y1.intValue()) {
					final BigInteger s1X1 = firstSpan.getCoordinates().getX1();
					final BigInteger s2X1 = secSpan.getCoordinates().getX1();
					returnValue = s1X1.compareTo(s2X1);
				} else {
					returnValue = s1Y.compareTo(s2Y);
				}
				return returnValue;
			}

		});

		set.addAll(spanList);

		final List<Span> linkedList = new LinkedList<Span>();
		linkedList.addAll(set);

		// TODO add the clear method to remove all elements of set since it not required after adding it to linked list.
		// set.clear();

		return linkedList;
	}

	/**
	 * Method is responsible for finding the patterns and returned the found data List.
	 * 
	 * @param value
	 * @param patternStr
	 * @param spanList
	 * @return
	 * @throws DCMAApplicationException {@link DCMAApplicationException} Check for all the input parameters and find the pattern.
	 */
	public static final List<DataCarrier> findPattern(final String value, final String patternStr, final List<Span> spanList)
			throws DCMAApplicationException {

		String errMsg = null;
		List<DataCarrier> dataCarrierList = null;
		if (EphesoftStringUtil.isNullOrEmpty(value)) {
			errMsg = "Invalid input character sequence.";
			LOGGER.info(errMsg);
		} else if (null == patternStr || TableExtractionConstants.EMPTY.equals(patternStr)) {
			errMsg = "Invalid input pattern sequence.";
			throw new DCMAApplicationException(errMsg);
		} else {
			// Compile and use regular expression
			final CharSequence inputStr = value;
			final Pattern pattern = Pattern.compile(patternStr);
			final Matcher matcher = pattern.matcher(inputStr);
			List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
			while (matcher.find()) {

				// Get all groups for this match
				for (int i = 0; i <= matcher.groupCount(); i++) {
					final String groupStr = matcher.group(i);
					if (groupStr != null) {
						final int startIndex = matcher.start();
						final int endIndex = startIndex + groupStr.trim().length();
						List<Span> matchedSpans = null;
						matchedSpans = PatternMatcherUtil.getMatchedSpans(spanList, startIndex, endIndex);
						String matchedString = PatternMatcherUtil.getMatchedSpansValue(matchedSpans);
						LOGGER.debug("Matched String is ", matchedString);
						if (!EphesoftStringUtil.isNullOrEmpty(matchedString)) {
							final float confidence = (groupStr.length() * CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE)
									/ matchedString.length();
							coordinatesList.clear();
							for (Span span : matchedSpans) {
								coordinatesList.add(span.getCoordinates());
							}
							Coordinates matchedCoordinates = HocrUtil.getRectangleCoordinates(coordinatesList);
							final DataCarrier dataCarrier = new DataCarrier(matchedSpans, confidence, matchedString,
									matchedCoordinates);
							if (dataCarrierList == null) {
								dataCarrierList = new ArrayList<DataCarrier>();
							}
							dataCarrierList.add(dataCarrier);
							LOGGER.info(groupStr);
						}
					}
				}
			}
		}

		return dataCarrierList;
	}

	/**
	 * Runs column header validation for the column.
	 * 
	 * @param colHeaderExtractionDataCarrier {@link ColumnHeaderExtractionDataCarrier}
	 * @return {@link TableExtractionAPIResult}
	 */
	public static TableExtractionAPIResult runColumnHeaderExtraction(
			final ColumnHeaderExtractionDataCarrier colHeaderExtractionDataCarrier, final int gapBetweenColumnWords) {
		LOGGER.info("Applying Column Header Validation for table extraction....");
		boolean isColHeaderValidationPassed = getColumnDataByHeaderValidation(colHeaderExtractionDataCarrier, gapBetweenColumnWords,
				true);

		// Calculating confidence for column header extraction results on the basis of the value's match with
		// validation pattern.
		TableExtractionAPIResult tableExtractionAPIResult = getMostConfidentHeaderExtraction(colHeaderExtractionDataCarrier,
				isColHeaderValidationPassed);
		return tableExtractionAPIResult;
	}

	/**
	 * Runs column coordinate extraction for the column.
	 * 
	 * @param pageID {@link String}
	 * @param tableColumn {@link TableColumnVO}
	 * @param column {@link Column}
	 * @param spanList {@link List}<{@link Span}>
	 * @return
	 */
	public static TableExtractionAPIResult runColumnCoordinateExtraction(final String pageID, final TableColumnVO tableColumn,
			final Column column, final List<Span> spanList) {
		Column columnCoordinateExtractionColumn = new Column();
		columnCoordinateExtractionColumn.setValid(false);
		columnCoordinateExtractionColumn.setValidationRequired(false);
		columnCoordinateExtractionColumn.setValid(false);
		columnCoordinateExtractionColumn.setValidationRequired(false);
		columnCoordinateExtractionColumn.setConfidence(0.0f);
		columnCoordinateExtractionColumn.setForceReview(false);
		columnCoordinateExtractionColumn.setOcrConfidence(0.0f);
		columnCoordinateExtractionColumn.setOcrConfidenceThreshold(0.0f);
		setColumnProperties(pageID, columnCoordinateExtractionColumn, null, 0);
		columnCoordinateExtractionColumn.setName(tableColumn.getColumnName());
		ColumnCoordinates columnCoordinates = getXColumncoordinates(tableColumn);
		boolean isColCoordValidationPassed = getColumnDataByColCoordValidation(columnCoordinates.getX0Coordinate(), columnCoordinates
				.getX1coordinate(), columnCoordinateExtractionColumn, spanList);
		boolean isColumnCoordinateExtractionConfidenceMaximum = false;
		if (isColCoordValidationPassed) {
			isColumnCoordinateExtractionConfidenceMaximum = isConfidenceMaximum(tableColumn, columnCoordinateExtractionColumn);

			// Fix for Bug #12563: Data is not coming in a column on which validation pattern is applied.
			if (EphesoftStringUtil.isNullOrEmpty(column.getValue()) && column.getConfidence() == 0
					|| columnCoordinateExtractionColumn.getConfidence() > column.getConfidence()) {
				swapColumnContents(columnCoordinateExtractionColumn, column);
			}

			// Add columnCoordinateColumn to alternate values of column.
			if (null != columnCoordinateExtractionColumn.getValue()) {
				column.getAlternateValues().getAlternateValue().add(columnCoordinateExtractionColumn);
			}
		}
		TableExtractionAPIResult tableExtractionAPIResult = new TableExtractionAPIResult();
		tableExtractionAPIResult.setConfidenceMaximum(isColumnCoordinateExtractionConfidenceMaximum);
		tableExtractionAPIResult.setValidationPassed(isColCoordValidationPassed);
		LOGGER.info("Getting rectangle coordinates for the value. ");
		tableExtractionAPIResult.setValueCoordinates(HocrUtil.getRectangleCoordinates(column.getCoordinatesList().getCoordinates()));
		return tableExtractionAPIResult;
	}

	/**
	 * Runs column header validation for the column.
	 * 
	 * @param colHeaderDataCarrier {@link ColumnHeaderExtractionDataCarrier}
	 * @param gapBetweenColumnWords int
	 * @param isFirstPage boolean
	 * @return {@link TableExtractionAPIResult}
	 */
	public static TableExtractionAPIResult runColumnHeaderExtraction(
			final ColumnHeaderExtractionDataCarrier colHeaderExtractionDataCarrier, final int gapBetweenColumnWords,
			final boolean isFirstPage) {
		LOGGER.info("Applying Column Header Validation for table extraction....");
		boolean isColHeaderValidationPassed = getColumnDataByHeaderValidation(colHeaderExtractionDataCarrier, gapBetweenColumnWords,
				(Boolean) isFirstPage);

		// Calculating confidence for column header extraction results on the basis of the value's match with
		// validation pattern.
		TableExtractionAPIResult tableExtractionAPIResult = getMostConfidentHeaderExtraction(colHeaderExtractionDataCarrier,
				isColHeaderValidationPassed);
		return tableExtractionAPIResult;
	}

	/**
	 * Calculating confidence for column header extraction results on the basis of the value's match with validation pattern.
	 * 
	 * @param colHeaderExtractionDataCarrier {@link ColumnHeaderExtractionDataCarrier}
	 * @param isColHeaderValidationPassed boolean
	 * @return {@link TableExtractionAPIResult}
	 */
	private static TableExtractionAPIResult getMostConfidentHeaderExtraction(
			final ColumnHeaderExtractionDataCarrier colHeaderExtractionDataCarrier, boolean isColHeaderValidationPassed) {
		boolean isColumnConfidenceMaximum = false;
		final Column column = colHeaderExtractionDataCarrier.getColumn();
		if (isColHeaderValidationPassed) {
			isColumnConfidenceMaximum = isConfidenceMaximum(colHeaderExtractionDataCarrier.getTableColumn(), column);
		}
		LOGGER.info("Getting rectangle coordinates for the value. ");
		TableExtractionAPIResult tableExtractionAPIResult = new TableExtractionAPIResult();
		tableExtractionAPIResult.setValueCoordinates(HocrUtil.getRectangleCoordinates(column.getCoordinatesList().getCoordinates()));
		tableExtractionAPIResult.setValidationPassed(isColHeaderValidationPassed);
		tableExtractionAPIResult.setConfidenceMaximum(isColumnConfidenceMaximum);
		return tableExtractionAPIResult;
	}

	/**
	 * Checks if current column value's confidence is greater than of last maximum column value's confidence.
	 * 
	 * @param tableColumn {@link TableColumnVO}
	 * @param column {@link Column}
	 * @return boolean
	 */
	private static boolean isConfidenceMaximum(final TableColumnVO tableColumn, final Column column) {
		boolean isConfidenceMaximum;
		setColumnConfidence(tableColumn, column);
		float maxConfidence = (float) CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE;
		if (column.getConfidence() < maxConfidence) {
			isConfidenceMaximum = false;
		} else {
			isConfidenceMaximum = true;
		}
		return isConfidenceMaximum;
	}

	/**
	 * Sets column confidence.
	 * 
	 * @param tableColumn {@link TableColumnVO}
	 * @param column {@link Column}
	 */
	public static void setColumnConfidence(final TableColumnVO tableColumn, final Column column) {
		float confidence = calculateColumnConfidence(tableColumn, column.getValue());
		column.setConfidence(confidence);
	}

	/**
	 * Calculates confidence for a column value with respect to its pattern.
	 * 
	 * @param patternString {@link String}
	 * @param columnValue {@link String}
	 * @return float
	 */
	private static float calculateColumnConfidence(final TableColumnVO tableColumn, final String columnValue) {
		float confidence;
		if (null == tableColumn) {
			confidence = TableExtractionConstants.MINIMUM_CONFIDENCE;
		} else {
			String patternString = tableColumn.getValidationPattern();
			LOGGER.debug("Calculating confidence for ", columnValue, " for regex pattern : ", patternString);
			if (EphesoftStringUtil.isNullOrEmpty(patternString) || EphesoftStringUtil.isNullOrEmpty(columnValue)) {
				confidence = (float) CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE;
			} else {
				confidence = 0;
				final Pattern pattern = Pattern.compile(patternString);
				final Matcher matcher = pattern.matcher(columnValue);
				int previousConfidence = 0;
				while (matcher.find()) {

					// Entire matching string.
					final String groupStr = matcher.group(0);
					if (groupStr != null) {
						confidence = (groupStr.length() * CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE) / columnValue.length();
						if (confidence > previousConfidence) {
							LOGGER.info(groupStr);
						}
					}
				}
			}
		}
		LOGGER.debug("Confidence is : ", confidence);
		return confidence;
	}

	/**
	 * Swaps contents of two columns.
	 * 
	 * @param column1 {@link Column}
	 * @param column2 {@link Column}
	 */
	public static void swapColumnContents(final Column column1, final Column column2) {
		swapValue(column1, column2);
		swapValidity(column1, column2);
		swapCoordinates(column1, column2);
		swapConfidence(column1, column2);
	}

	/**
	 * Swaps values of two columns.
	 * 
	 * @param column1 {@link Column}
	 * @param column2 {@link Column}
	 */
	private static void swapValue(final Column column1, final Column column2) {
		String valueColumn1 = column1.getValue();
		String valueColumn2 = column2.getValue();
		column1.setValue(valueColumn2);
		column2.setValue(valueColumn1);
	}

	/**
	 * Swaps validity of two columns.
	 * 
	 * @param column1 {@link Column}
	 * @param column2 {@link Column}
	 */
	private static void swapValidity(final Column column1, final Column column2) {
		boolean isColumn1Valid = column1.isValid();
		boolean isColumn2Valid = column2.isValid();
		column1.setValid(isColumn2Valid);
		column2.setValid(isColumn1Valid);
	}

	/**
	 * Swaps confidence of two columns.
	 * 
	 * @param column1 {@link Column}
	 * @param column2 {@link Column}
	 */
	private static void swapCoordinates(final Column column1, final Column column2) {
		CoordinatesList coordinateList1 = column1.getCoordinatesList();
		CoordinatesList coordinateList2 = column2.getCoordinatesList();
		column1.setCoordinatesList(coordinateList2);
		column2.setCoordinatesList(coordinateList1);
	}

	/**
	 * Swaps confidence of two columns.
	 * 
	 * @param column1 {@link Column}
	 * @param column2 {@link Column}
	 */
	private static void swapConfidence(final Column column1, final Column column2) {
		float confidence1 = column1.getConfidence();
		float confidence2 = column2.getConfidence();
		column1.setConfidence(confidence2);
		column2.setConfidence(confidence1);
	}

	/**
	 * Sets properties for a column.
	 * 
	 * @param pageID {@link String}
	 * @param column {@link Column}
	 * @param value {@link String}
	 * @param confidence float
	 */
	public static void setColumnProperties(final String pageID, final Column column, final String value, final float confidence) {
		final Column.AlternateValues alternateValues = new Column.AlternateValues();
		column.setName(null);
		column.setConfidence(confidence);
		column.setPage(pageID);
		column.setValid(true);
		column.setCoordinatesList(new CoordinatesList());
		column.setAlternateValues(alternateValues);
		column.setValue(value);
	}

	/**
	 * Adds alternate value to a column.
	 * 
	 * @param column {@link Column}
	 * @param outputValue {@link String}
	 * @param outputConfidence {@link String}
	 * @param coordinateList {@link List}<{@link Coordinates}>
	 */
	public static void addAlternateValues(final Column column, final String outputValue, final float outputConfidence,
			final List<Coordinates> coordinateList) {

		// Changed input parameters and logic to make alternate value coordinates similar to as a column coordinate, so that if user
		// selects any alternate value as a column value, the new overlays drawn shall behave similarly (not get turned into rectangle
		// coordinates).
		if (null != outputValue && null != coordinateList) {
			final Field alternateValue = new Field();
			alternateValue.setForceReview(Boolean.FALSE);
			alternateValue.setValue(outputValue);
			alternateValue.setName(column.getName());
			alternateValue.setConfidence(outputConfidence);
			alternateValue.setPage(column.getPage());
			final CoordinatesList coordinatesList = new CoordinatesList();
			alternateValue.setCoordinatesList(coordinatesList);
			for (Coordinates hocrCoordinates : coordinateList) {
				final Coordinates coordinates = new Coordinates();
				coordinates.setX0(hocrCoordinates.getX0());
				coordinates.setX1(hocrCoordinates.getX1());
				coordinates.setY0(hocrCoordinates.getY0());
				coordinates.setY1(hocrCoordinates.getY1());
				alternateValue.getCoordinatesList().getCoordinates().add(coordinates);
			}
			LOGGER.info("Add alternate value = ", alternateValue.getValue(), " to column = ", column.getName());
			column.getAlternateValues().getAlternateValue().add(alternateValue);
		}
	}

	/**
	 * Adds alternate value to a column.
	 * 
	 * @param column {@link Column}
	 * @param alternateColumn {@link Column}
	 */
	public static void addAlternateValues(final Column column, final Column alternateColumn) {

		// Changed input parameters and logic to make alternate value coordinates similar to as a column coordinate, so that if user
		// selects any alternate value as a column value, the new overlays drawn shall behave similarly (not get turned into rectangle
		// coordinates).
		if (null != alternateColumn && null != column) {
			final Field alternateValue = new Field();
			alternateValue.setForceReview(Boolean.FALSE);
			alternateValue.setValue(alternateColumn.getValue());
			alternateValue.setName(column.getName());
			alternateValue.setConfidence(alternateColumn.getConfidence());
			alternateValue.setPage(column.getPage());
			final CoordinatesList coordinatesList = new CoordinatesList();
			alternateValue.setCoordinatesList(coordinatesList);
			for (Coordinates hocrCoordinates : alternateColumn.getCoordinatesList().getCoordinates()) {
				final Coordinates coordinates = new Coordinates();
				coordinates.setX0(hocrCoordinates.getX0());
				coordinates.setX1(hocrCoordinates.getX1());
				coordinates.setY0(hocrCoordinates.getY0());
				coordinates.setY1(hocrCoordinates.getY1());
				alternateValue.getCoordinatesList().getCoordinates().add(coordinates);
			}
			LOGGER.info("Add alternate value = ", alternateValue.getValue(), " to column = ", column.getName());
			column.getAlternateValues().getAlternateValue().add(alternateValue);
		}
	}

	/**
	 * This method extracts the column data based on the column coordinates.
	 * 
	 * @param columnCoordX0 {@link Integer}
	 * @param columnCoordX1 {@link Integer}
	 * @param column {@link Column} Column whose data is to be extracted.
	 * @param spanList {@link List<Span>}
	 * @return boolean
	 */
	private static boolean getColumnDataByColCoordValidation(final Integer columnCoordX0, final Integer columnCoordX1,
			final Column column, final List<Span> spanList) {
		LOGGER.info("Applying Column Coordinates Validation for table extraction ..... ");
		final StringBuffer outputValue = new StringBuffer();
		final CoordinatesList coordinatesList = new CoordinatesList();
		boolean isColCoordValidationPassed = false;
		for (final Span span : spanList) {
			if (span != null && span.getValue() != null && !span.getValue().isEmpty() && span.getCoordinates() != null) {
				final Coordinates coordinates = span.getCoordinates();
				LOGGER.info("Checking if the value: ", span.getValue(), " is valid with respect to column coordinates: X0=",
						columnCoordX0, " ,X1=", columnCoordX1);
				if (isColumnValidWithColCoord(coordinates.getX0().intValue(), coordinates.getX1().intValue(), columnCoordX0,
						columnCoordX1)) {
					LOGGER.info(span.getValue() + " valid with column coordinates .");
					outputValue.append(span.getValue());
					outputValue.append(TableExtractionConstants.SPACE);
					coordinatesList.getCoordinates().add(span.getCoordinates());
				}
			}
		}
		if (outputValue.length() > 0 && coordinatesList != null) {
			LOGGER.debug("Column value = ", outputValue);
			column.setValue(outputValue.toString().trim());
			column.setCoordinatesList(coordinatesList);
			column.setValid(true);
			isColCoordValidationPassed = true;
		}
		LOGGER.info("Column coordinate validation passed = ", isColCoordValidationPassed);
		return isColCoordValidationPassed;
	}

	/**
	 * Method to check if value coordinates passed are valid with respect to column coordinates.
	 * 
	 * @param valueX0 {@link Integer}
	 * @param valueX1 {@link Integer}
	 * @param columnX0 {@link Integer}
	 * @param columnX1 {@link Integer}
	 * @return boolean
	 */
	public static boolean isColumnValidWithColCoord(final Integer valueX0, final Integer valueX1, final Integer columnX0,
			final Integer columnX1) {
		LOGGER.info("Entering method isColumnValidWithColCoord.....");
		boolean isValid = false;
		LOGGER.debug("Column Coordinates: X0=", valueX0, " ,X1=", valueX1);
		LOGGER.debug("Value Coordinates: X0=", columnX0, " ,X1=", columnX1);
		if (valueX0 != null && valueX1 != null && columnX0 != null && columnX0 != null && (columnX0 != 0 || columnX1 != 0)) {
			if ((valueX0.compareTo(columnX0) == 1 && valueX0.compareTo(columnX1) == -1)
					|| (valueX1.compareTo(columnX0) == 1 && valueX1.compareTo(columnX1) == -1)
					|| (valueX0.compareTo(columnX0) == -1 && valueX1.compareTo(columnX1) == 1) || valueX0.compareTo(columnX0) == 0
					|| valueX1.compareTo(columnX1) == 0) {
				isValid = true;
			}
		}
		LOGGER.info("Is value coordinates valid with column coordinates : ", isValid);
		LOGGER.info("Exiting method isColumnValidWithColCoord.....");
		return isValid;
	}

	/**
	 * This method checks if span is valid with respect to the column header specified by admin and if is valid, then adds the
	 * coordinates of current span to the coordinates list of a column.
	 * 
	 * @param coordinatesList
	 * @param column
	 * @param colHeaderInfoMap
	 */
	public static void setColumnCoordinates(final List<Coordinates> coordinatesList, final Column column) {
		if (null != column && null != coordinatesList && !coordinatesList.isEmpty()) {
			column.getCoordinatesList().getCoordinates().clear();
			for (final Coordinates coordinates : coordinatesList) {
				column.getCoordinatesList().getCoordinates().add(coordinates);
			}
		}
	}

	/**
	 * Gets X0 and X1 coordinates for a tableColumn.
	 * 
	 * @param tableColumn {@link TableColumnVO}
	 * @return {@link ColumnCoordinates}
	 */
	public static ColumnCoordinates getXColumncoordinates(final TableColumnVO tableColumn) {
		String startCoordinate = tableColumn.getColumnStartCoordinate();
		String endCoordinate = tableColumn.getColumnEndCoordinate();
		Integer columnCoordX0 = null;
		Integer columnCoordX1 = null;
		if (NumberUtils.isDigits(startCoordinate) && NumberUtils.isDigits(endCoordinate)) {
			columnCoordX0 = Integer.parseInt(startCoordinate);
			columnCoordX1 = Integer.parseInt(endCoordinate);
		}
		ColumnCoordinates coordinates = new ColumnCoordinates();
		coordinates.setX0Coordinate(columnCoordX0);
		coordinates.setX1coordinate(columnCoordX1);
		return coordinates;
	}

	/**
	 * Gets extraction results and validation status for regex based extraction on a column.
	 * 
	 * @param columnRowList {@link List}<{@link Column}> List of original table row columns for extraction of column data.
	 * @param columnHeadStartCoordinate int
	 * @param columnHeadEndCoordinate int
	 * @param regexValidationDataCarrier {@link RegexValidationDataCarrier}
	 * @param dataCarrierList {@link List}<{@link DataCarrier}>
	 * @return boolean True if regex validation is passed.
	 * @throws DCMAApplicationException
	 */
	public static boolean getRegexBasedExtraction(final List<Column> columnRowList, final int columnHeadStartCoordinate,
			final int columnHeadEndCoordinate, final RegexValidationDataCarrier regexValidationDataCarrier,
			final List<DataCarrier> dataCarrierList) throws DCMAApplicationException {
		boolean isRegexValidationPassed = false;
		if (CollectionUtils.isNotEmpty(columnRowList) && CollectionUtils.isNotEmpty(dataCarrierList)
				&& null != regexValidationDataCarrier) {
			final Column regexExtractionColumn = regexValidationDataCarrier.getColumn();
			if (null != regexExtractionColumn) {
				final int indexOfTableColumn = regexValidationDataCarrier.getIndexOfTableColumn();
				final Coordinates columnCoordinates = regexValidationDataCarrier.getColumnCoordinates();
				final LineDataCarrier lineDataCarrier = regexValidationDataCarrier.getLineDataCarrier();
				final TableColumnVO tableColumn = regexValidationDataCarrier.getTableColumn();
				String betweenLeft;
				String betweenRight;
				if (null == tableColumn) {
					betweenLeft = null;
					betweenRight = null;
				} else {
					betweenLeft = tableColumn.getBetweenLeft();
					betweenRight = tableColumn.getBetweenRight();
				}
				LOGGER.debug("Between Left Pattern : ", betweenLeft, ". Between Right Pattern : ", betweenRight);
				List<Field> selectionColumnOptionsList = null;
				int indexOfDataCarrier = TableExtractionConstants.SINGLE_SIZE_VALUE;
				for (final DataCarrier outputDataCarrier : dataCarrierList) {
					if (outputDataCarrier == null || null == outputDataCarrier.getValue() || null == outputDataCarrier.getSpans()) {
						continue;
					}

					// Fix for trimming outputdataCarrier with trailing spaces to get correct number of spans for a pattern.
					String outputValue = outputDataCarrier.getValue().trim();
					float outputValueConfidence = outputDataCarrier.getConfidence();
					LOGGER.debug("Result data carrier value is: ", outputValue);
					final List<Span> outputSpans = outputDataCarrier.getSpans();
					final List<Coordinates> coordinatesList = new ArrayList<Coordinates>(outputSpans.size());
					boolean isValid = true;
					String finalValue = ICommonConstants.EMPTY_STRING;
					List<Span> finalSpanList = new ArrayList<Span>();
					for (Span span : outputSpans) {
						if (null == columnCoordinates
								|| isColumnValidWithCoordinateVaildation(span.getCoordinates(), columnCoordinates)) {
							finalValue = EphesoftStringUtil.concatenate(finalValue, span.getValue());
							coordinatesList.add(span.getCoordinates());
							finalSpanList.add(span);
						}
					}
					outputDataCarrier.setValue(finalValue);
					outputDataCarrier.setSpans(finalSpanList);
					Coordinates resultValueCoordinates = outputDataCarrier.getCoordinates();
					if (EphesoftStringUtil.isNullOrEmpty(finalValue)
							|| (resultValueCoordinates != null && null != columnCoordinates && !isColumnValidWithColCoord(
									resultValueCoordinates.getX0().intValue(), resultValueCoordinates.getX1().intValue(),
									columnCoordinates.getX0().intValue(), columnCoordinates.getX1().intValue()))) {
						isValid = false;
					}
					if (isValid) {
						LOGGER.info("Data output value :", outputValue, " is valid with respect to rectangle coordinates.");
						isRegexValidationPassed = true;
						regexExtractionColumn.setValid(true);
						regexExtractionColumn.setValue(finalValue);
						setColumnCoordinates(coordinatesList, regexExtractionColumn);
						regexExtractionColumn.setConfidence(outputValueConfidence);

						// Validate the field with left right pattern specified by admin.
						boolean isFound = isValidWithBetweenLeftBetweenRight(betweenLeft, betweenRight, lineDataCarrier,
								outputDataCarrier.getSpans());
						if (isFound) {
							selectionColumnOptionsList = addToSelectedColumnOptionsList(selectionColumnOptionsList, outputValue,
									outputValueConfidence, coordinatesList);
						} else if (indexOfDataCarrier != dataCarrierList.size() || null != selectionColumnOptionsList) {

							// Ensured that final selected data for the column wont be added to alternate values.
							LOGGER.info("Adding output value :", outputValue, " to the list of alternate values.");
							addAlternateValues(columnRowList.get(indexOfTableColumn), regexExtractionColumn);
						}
					}
					indexOfDataCarrier++;
				}

				// For selecting from multiple valid and found values from regex validation.
				SelectionValueDataCarrier selectionValueDataCarrier = new SelectionValueDataCarrier();
				selectionValueDataCarrier.setColumn(regexExtractionColumn);
				selectionValueDataCarrier.setColumnHeadEndCoordinate(columnHeadEndCoordinate);
				selectionValueDataCarrier.setColumnHeadStartCoordinate(columnHeadStartCoordinate);
				selectionValueDataCarrier.setColumnRowList(columnRowList);
				selectionValueDataCarrier.setIndexOfTableColumn(indexOfTableColumn);
				selectionValueDataCarrier.setSelectionColumnOptionsList(selectionColumnOptionsList);
				selectBestMatchedValue(selectionValueDataCarrier);
			}
		}
		return isRegexValidationPassed;
	}

	/**
	 * Adds a column data field to selected options for the column final value list.
	 * 
	 * @param selectionColumnOptionsList {@link List}<{@link Field}>
	 * @param outputValue {@link String}
	 * @param outputValueConfidence float
	 * @param coordinatesList {@link List}<{@link Coordinates}>
	 * @return {@link List}<{@link Field}>
	 */
	private static List<Field> addToSelectedColumnOptionsList(List<Field> selectionColumnOptionsList, final String outputValue,
			final float outputValueConfidence, final List<Coordinates> coordinatesList) {
		LOGGER.info("Data output value :", outputValue, " is valid with respect to between left and between right regex.");
		if (null == selectionColumnOptionsList) {
			selectionColumnOptionsList = new ArrayList<Field>(TableExtractionConstants.SINGLE_SIZE_VALUE);
		}
		Field foundColumn = createColumnSelectionOption(outputValue, outputValueConfidence, coordinatesList);
		selectionColumnOptionsList.add(foundColumn);
		return selectionColumnOptionsList;
	}

	/**
	 * Selects best matched value for column extraction data amongst list of multiple candidates available.
	 * 
	 * @param selectionValueDataCarrier {@link SelectionValueDataCarrier} Object carrying data for selecting data for column.
	 */
	private static void selectBestMatchedValue(final SelectionValueDataCarrier selectionValueDataCarrier) {
		LOGGER
				.trace("In method to select best matched column data out of multiple values matched with column pattern, between left and between right.");
		if (null != selectionValueDataCarrier) {
			final List<Field> selectionColumnOptionsList = selectionValueDataCarrier.getSelectionColumnOptionsList();
			final int columnHeadStartCoordinate = selectionValueDataCarrier.getColumnHeadStartCoordinate();
			final int columnHeadEndCoordinate = selectionValueDataCarrier.getColumnHeadEndCoordinate();
			LOGGER.debug("Column start coordinate is: ", columnHeadStartCoordinate, ". Column End coordinate is: ",
					columnHeadEndCoordinate);
			final Column column = selectionValueDataCarrier.getColumn();
			if (null != selectionColumnOptionsList) {
				int sizeOfColumnOptionsList = selectionColumnOptionsList.size();
				LOGGER.debug("Number of selection options for column data is: ", sizeOfColumnOptionsList);
				if (sizeOfColumnOptionsList > TableExtractionConstants.SINGLE_SIZE_VALUE) {
					int selectedDataIndex;
					sortFieldsOnDescendingConfidence(selectionColumnOptionsList);
					if (columnHeadStartCoordinate > TableExtractionConstants.MINIMUM_POSITIVE_COORDINATE_VALUE
							&& columnHeadEndCoordinate > TableExtractionConstants.MINIMUM_POSITIVE_COORDINATE_VALUE) {

						// Either of column coordinate/column header information is available for selecting column data.
						selectedDataIndex = selectValueUsingColumnOrderByCoordinates(columnHeadStartCoordinate,
								columnHeadEndCoordinate, column, selectionColumnOptionsList);
					} else {

						// Make use of default order of table columns for selecting column data.
						selectedDataIndex = selectValueUsingColumnDefaultOrder(selectionValueDataCarrier.getColumnRowList(),
								selectionValueDataCarrier.getIndexOfTableColumn(), column, selectionColumnOptionsList);
					}
					addNonSelectedToAlternateValues(selectionValueDataCarrier.getColumnRowList().get(
							selectionValueDataCarrier.getIndexOfTableColumn()), column, selectionColumnOptionsList, selectedDataIndex);
				} else if (sizeOfColumnOptionsList == TableExtractionConstants.SINGLE_SIZE_VALUE) {
					Field field = selectionColumnOptionsList.get(TableExtractionConstants.START_INDEX);
					if (null != field) {
						column.setValue(field.getValue());
						setColumnCoordinates(field.getCoordinatesList().getCoordinates(), column);
					}
				}
			}
		}
	}

	/**
	 * This method verifies if coordinate validation is used with regex validation, column extracted by regex validation is valid.
	 * 
	 * @param spanCoordinates {@link Coordinates} coordinates of the current span during regex validation.
	 * @param columnCoordinates {@link Coordinates} coordinates of the extracted column from coordinate validation
	 * @return boolean True if valid.
	 */
	private static boolean isColumnValidWithCoordinateVaildation(Coordinates spanCoordinates, Coordinates columnCoordinates) {
		boolean isValid;
		if (spanCoordinates != null
				&& columnCoordinates != null
				&& isColumnValidWithColCoord(spanCoordinates.getX0().intValue(), spanCoordinates.getX1().intValue(), columnCoordinates
						.getX0().intValue(), columnCoordinates.getX1().intValue())) {
			isValid = true;
		} else {
			isValid = false;
		}
		return isValid;
	}

	/**
	 * Sorts list of fields on the basis on confidence in descending order.
	 * 
	 * @param fieldList {@link List}<{@link Field}>
	 */
	private static void sortFieldsOnDescendingConfidence(final List<Field> fieldList) {
		if (CollectionUtils.isNotEmpty(fieldList)) {
			Collections.sort(fieldList, new Comparator<Field>() {

				@Override
				public int compare(final Field object1, final Field object2) {

					// This comparator sorts fields in descending order.
					int comparison;
					if (null == object1 || null == object2) {
						comparison = TableExtractionConstants.EQUAL_COMPARISON;
					} else {
						float field1Confidence = object1.getConfidence();
						float field2Confidence = object2.getConfidence();

						// Comparing start coordinates of column coordinates of two input table columns.
						if (field1Confidence > field2Confidence) {
							comparison = TableExtractionConstants.LESS_COMPARISON;
						} else if (field1Confidence < field2Confidence) {
							comparison = TableExtractionConstants.GREATER_COMPARISON;
						} else {
							comparison = TableExtractionConstants.EQUAL_COMPARISON;
						}
					}
					return comparison;
				}
			});

		}
	}

	/**
	 * Selects value among the options available for column data in the selectionColumnOptionsList, on the basis default order of the
	 * table columns.
	 * 
	 * @param columnRowList {@link List}<{@link Column}> List of extracted columns of the row of the table.
	 * @param indexOfTableColumn int Index of the column in a row of the table.
	 * @param column {@link Column} Column being extracted.
	 * @param selectionColumnOptionsList {@link List}<{@link Field}> List of valid column data value extraction candidates.
	 * @return int Index of the selected value for the column in the selection Column Options List.
	 */
	private static int selectValueUsingColumnDefaultOrder(final List<Column> columnRowList, final int indexOfTableColumn,
			final Column column, final List<Field> selectionColumnOptionsList) {
		boolean isValid = false;
		int prevColumnIndex = TableExtractionConstants.START_INDEX;
		int selectedDataIndex = TableExtractionConstants.INVALID_INDEX_VALUE;
		int indexOfColumnOptionsList = TableExtractionConstants.START_INDEX;
		Column prevColumn = null;

		if (CollectionUtils.isNotEmpty(selectionColumnOptionsList)) {

			// Pure UI order dependency in regex validation.
			for (Field field : selectionColumnOptionsList) {
				if (null != field) {
					String fieldValue = field.getValue();

					// If last value among the column values option list.
					if (indexOfColumnOptionsList + TableExtractionConstants.SINGLE_SIZE_VALUE == selectionColumnOptionsList.size()) {
						LOGGER.info("Selection of column value is done. Selected column value is: ", fieldValue);
						column.setValue(fieldValue);
						setColumnCoordinates(field.getCoordinatesList().getCoordinates(), column);
						selectedDataIndex = indexOfColumnOptionsList;
						isValid = true;
						break;
					}

					// searching if current candidate value is extracted as value for some previous column.
					boolean matchFound = false;
					if (CollectionUtils.isNotEmpty(columnRowList)) {
						while (prevColumnIndex < indexOfTableColumn) {
							prevColumn = columnRowList.get(prevColumnIndex);
							if (null != prevColumn) {
								String prevValue = prevColumn.getValue();
								if (null != prevValue && prevValue.equals(fieldValue)) {

									// if value is matched, we break loop to go to next column candidate value if it is last in the
									// list or not used by some previous column.
									matchFound = true;
									break;
								}
							}
							prevColumnIndex++;
						}
					}

					// if none of the previous columns had used current candidate value for column, we assign this value for the column
					// as final extracted value.
					if (!matchFound) {
						LOGGER.info("Selection of column value is done. Selected column value is: ", fieldValue);
						column.setValue(fieldValue);
						setColumnCoordinates(field.getCoordinatesList().getCoordinates(), column);
						selectedDataIndex = indexOfColumnOptionsList;
						isValid = true;
						break;
					}
					indexOfColumnOptionsList++;
				}
			}
		}

		// if value not found, move all elements in option list to alternate values, so make selectedDataIndex as an invalid index.
		if (!isValid) {
			selectedDataIndex = TableExtractionConstants.INVALID_INDEX_VALUE;
		}
		return selectedDataIndex;
	}

	/**
	 * Adds non selected values from regex validation results to alternate values for the column.
	 * 
	 * @param pageID {@link Column} Original extraction column for table row.
	 * @param column {@link Column} Column whose data is being extracted currently.
	 * @param selectionColumnOptionsList {@link List}<{@link Field}> List of fields which are valid candidates for column data
	 *            selection.
	 * @param selectedDataIndex int Index of the data selected for the table column from selection options list.
	 */
	private static void addNonSelectedToAlternateValues(final Column column, final Column alternateColumn,
			final List<Field> selectionColumnOptionsList, final int selectedDataIndex) {
		int index = TableExtractionConstants.START_INDEX;
		if (CollectionUtils.isNotEmpty(selectionColumnOptionsList)) {

			// adding non selected values in the alternate value list for the current column.
			for (Field field : selectionColumnOptionsList) {
				if (index != selectedDataIndex) {
					String outputValue = field.getValue();
					LOGGER.info("Adding output value :", outputValue, " to the list of alternate values.");
					addAlternateValues(column, alternateColumn);
				}
				index++;
			}
		}
	}

	/**
	 * Selects value among the options available for column data in the selectionColumnOptionsList, on the basis of nearest value to
	 * coordinates of the column (either known by column coordinates or by column header).
	 * 
	 * @param columnHeadStartCoordinate int Table column head's start coordinate.
	 * @param columnHeadEndCoordinate int Table column head's end coordinate.
	 * @param column {@link Column} column being extracted.
	 * @param selectionColumnOptionsList {@link List}<{@link Field}> List of valid column data value extraction candidates.
	 * @return int Index of the selected value for the column in the selection Column Options List.
	 */
	private static int selectValueUsingColumnOrderByCoordinates(final int columnHeadStartCoordinate,
			final int columnHeadEndCoordinate, Column column, List<Field> selectionColumnOptionsList) {
		LOGGER.trace("In method to select value column data based on learned column coordinates/ column header coordinates.");
		int selectedDataIndex = TableExtractionConstants.INVALID_INDEX_VALUE;
		Field selectedColumn = null;
		boolean isValid = false;
		int minDistance = Integer.MAX_VALUE;
		int indexOfColumnOptionsList = TableExtractionConstants.START_INDEX;
		for (Field field : selectionColumnOptionsList) {
			if (null != field) {
				String fieldValue = field.getValue();
				final Coordinates valCoordinates = HocrUtil.getRectangleCoordinates(field.getCoordinatesList().getCoordinates());
				if (null != valCoordinates) {
					int valueX0 = valCoordinates.getX0().intValue();
					int valueX1 = valCoordinates.getX1().intValue();
					isValid = isColumnValidWithColCoord(valueX0, valueX1, columnHeadStartCoordinate, columnHeadEndCoordinate);

					// value in valid as per column coordinates itself, chose this as final column data. Mostly this wont occur.
					if (isValid) {
						LOGGER.info("Selection of column value is done. Selected column value is: ", fieldValue);
						column.setValue(fieldValue);
						setColumnCoordinates(field.getCoordinatesList().getCoordinates(), column);
						selectedDataIndex = indexOfColumnOptionsList;
						break;
					} else {

						// choosing nearest value among the list of select column values.

						// Comparing value's start x coordinate with head's end coordinate;
						int compareX0 = Math.abs(valueX0 - columnHeadEndCoordinate);

						// Comparing value's end x coordinate with head's start coordinate;
						int compareX1 = Math.abs(columnHeadStartCoordinate - valueX1);
						if (compareX0 < minDistance) {
							minDistance = compareX0;
							selectedColumn = field;
							selectedDataIndex = indexOfColumnOptionsList;
						}
						if (compareX1 < minDistance) {
							minDistance = compareX1;
							selectedColumn = field;
							selectedDataIndex = indexOfColumnOptionsList;
						}
					}
				}
			}
			indexOfColumnOptionsList++;
		}
		if (!isValid && null != selectedColumn) {

			// choosing final column data value with the nearest location with respect to the column header/ column coordinates.
			String selectedValue = selectedColumn.getValue();
			LOGGER.info("Selection of column value is done. Selected column value is: ", selectedValue);
			column.setValue(selectedValue);
			setColumnCoordinates(selectedColumn.getCoordinatesList().getCoordinates(), column);
			isValid = true;
		}

		// if value not found, move all elements in option list to alternate values, so make selectedDataIndex as an invalid index.
		if (!isValid) {
			selectedDataIndex = TableExtractionConstants.INVALID_INDEX_VALUE;
		}
		return selectedDataIndex;
	}

	/**
	 * Creates a field that is a valid candidate for the selection of value and coordinates for a table column.
	 * 
	 * @param outputValue {@link String}
	 * @param confidence float
	 * @param coordinatesList {@link List}<{@link Coordinates}>
	 * @return {@link Field}
	 */
	private static Field createColumnSelectionOption(final String outputValue, final float confidence,
			final List<Coordinates> coordinatesList) {
		LOGGER.trace("Creating field object for :", outputValue);

		// Saving string output value and list of coordinates for the current data in a field object.
		Field foundColumn = new Field();
		foundColumn.setForceReview(true);
		foundColumn.setValue(outputValue);
		foundColumn.setConfidence(confidence);
		final CoordinatesList foundCoordinatesList = new CoordinatesList();
		foundColumn.setCoordinatesList(foundCoordinatesList);
		List<Coordinates> foundCoordinates = foundColumn.getCoordinatesList().getCoordinates();
		if (null != foundCoordinates && CollectionUtils.isNotEmpty(coordinatesList)) {
			for (Coordinates coordinate : coordinatesList) {
				final Coordinates newCoordinates = new Coordinates();
				newCoordinates.setX0(coordinate.getX0());
				newCoordinates.setX1(coordinate.getX1());
				newCoordinates.setY0(coordinate.getY0());
				newCoordinates.setY1(coordinate.getY1());
				foundCoordinates.add(newCoordinates);
			}
		}
		return foundColumn;
	}

	/**
	 * Tests if extracted data based on column pattern is also valid according to between left and between right pattern for the
	 * column.
	 * 
	 * @param betweenLeft {@link String}
	 * @param betweenRight {@link String}
	 * @param lineDataCarrier {@link String}
	 * @param spans {@link List}<{@link Span}>
	 * @return boolean True if data is valid with respect to between left and between right pattern also.
	 * @throws DCMAApplicationException {@link DCMAApplicationException}
	 */
	private static boolean isValidWithBetweenLeftBetweenRight(final String betweenLeft, final String betweenRight,
			final LineDataCarrier lineDataCarrier, List<Span> spans) throws DCMAApplicationException {
		boolean isValid = false;
		if (!CollectionUtil.isEmpty(spans)) {
			int sizeOfSpans = spans.size();
			Span leftmostSpan = spans.get(TableExtractionConstants.START_INDEX);
			Span rightmostSpan = null;
			if (sizeOfSpans > 0) {
				rightmostSpan = spans.get(sizeOfSpans - 1);
			}
			if (!EphesoftStringUtil.isNullOrEmpty(betweenLeft)) {
				if (!EphesoftStringUtil.isNullOrEmpty(betweenRight)) {
					isValid = isValidBetweenLeftAndRight(betweenLeft, betweenRight, lineDataCarrier, leftmostSpan, rightmostSpan);
				} else {
					isValid = isValidBetweenLeft(betweenLeft, lineDataCarrier, leftmostSpan);
				}
			} else {
				isValid = isValidBetweenRight(betweenRight, lineDataCarrier, rightmostSpan);
			}
		}
		return isValid;
	}

	/**
	 * Checks if the value is valid with respect to right regex pattern.
	 * 
	 * @param betweenRight {@link String}
	 * @param lineDataCarrier {@link LineDataCarrier}
	 * @param rightmostSpan {@link Span}
	 * @return boolean True if the value is valid with respect to right regex pattern.
	 * @throws DCMAApplicationException
	 */
	private static boolean isValidBetweenRight(final String betweenRight, final LineDataCarrier lineDataCarrier,
			final Span rightmostSpan) throws DCMAApplicationException {
		boolean isValid;
		if (!EphesoftStringUtil.isNullOrEmpty(betweenRight)) {
			final Span rightSpan = lineDataCarrier.getRightSpan(rightmostSpan);
			;
			if (null == rightSpan) {
				LOGGER.debug("Right Span is null. Between Right = ", betweenRight);
				isValid = false;
			} else {
				final DataCarrier rightDataCarrier = findPattern(rightSpan, betweenRight);
				if (null == rightDataCarrier) {
					LOGGER.debug("Between right pattern match failed. Between right is: ", betweenRight);
					isValid = false;
				} else {
					LOGGER.debug("Between right found = ", rightDataCarrier.getValue());
					isValid = true;
				}
			}
		} else {
			isValid = true;
		}
		return isValid;
	}

	/**
	 * Checks if the value is valid with respect to left regex pattern.
	 * 
	 * @param betweenLeft {@link String}
	 * @param lineDataCarrier {@link LineDataCarrier}
	 * @param leftmostSpan {@link Span}
	 * @return boolean True if the value is valid with respect to left regex pattern.
	 * @throws DCMAApplicationException
	 */
	private static boolean isValidBetweenLeft(final String betweenLeft, final LineDataCarrier lineDataCarrier, final Span leftmostSpan)
			throws DCMAApplicationException {
		boolean isValid;
		final Span leftSpan = lineDataCarrier.getLeftSpan(leftmostSpan);
		if (null == leftSpan) {
			LOGGER.debug("Left Span is null. Between Left = ", betweenLeft);
			isValid = false;
		} else {
			final DataCarrier leftDataCarrier = findPattern(leftSpan, betweenLeft);
			if (null != leftDataCarrier) {
				LOGGER.debug("Between left found = ", leftDataCarrier.getValue());
				isValid = true;
			} else {
				LOGGER.debug("Between left pattern match failed. Between left is: ", betweenLeft);
				isValid = false;
			}
		}
		return isValid;
	}

	/**
	 * Checks if the value is valid with respect to both left and right regex patterns.
	 * 
	 * @param betweenLeft {@link String}
	 * @param betweenRight {@link String}
	 * @param lineDataCarrier {@link LineDataCarrier}
	 * @param leftmostSpan {@link Span}
	 * @param rightmostSpan {@link Span}
	 * @return boolean True if value is valid w.r.t both between left and between right regex patterns.
	 * @throws DCMAApplicationException
	 */
	private static boolean isValidBetweenLeftAndRight(final String betweenLeft, final String betweenRight,
			final LineDataCarrier lineDataCarrier, final Span leftmostSpan, final Span rightmostSpan) throws DCMAApplicationException {
		boolean isValid;
		final Span leftSpan = lineDataCarrier.getLeftSpan(leftmostSpan);
		if (null == leftSpan) {
			LOGGER.debug("Left Span is null. betweenLeft = ", betweenLeft);
			isValid = false;
		} else {
			final Span rightSpan = lineDataCarrier.getRightSpan(rightmostSpan);
			if (null == rightSpan) {
				LOGGER.debug("Right Span is null. betweenRight = ", betweenRight);
				isValid = false;
			} else {
				final DataCarrier leftDataCarrier = findPattern(leftSpan, betweenLeft);
				final DataCarrier rightDataCarrier = findPattern(rightSpan, betweenRight);
				if (null == leftDataCarrier || null == rightDataCarrier) {
					LOGGER.debug("Between left/between right pattern match failed. Between left is: ", betweenLeft,
							". Between right is: ", betweenRight, ". Left span value is: ", leftSpan.getValue(),
							". Right span value is: ", rightSpan.getValue());
					isValid = false;
				} else {
					LOGGER.debug("Between left found = ", leftDataCarrier.getValue());
					LOGGER.debug("Between right found = ", rightDataCarrier.getValue());
					isValid = true;

				}
			}
		}
		return isValid;
	}

	/**
	 * Method is responsible for finding the patterns and returned the found data List.
	 * 
	 * @param span Span
	 * @param patternStr String
	 * @return DataCarrier
	 * @throws DCMAApplicationException Check for all the input parameters and find the pattern.
	 */
	private static final DataCarrier findPattern(final Span span, final String patternStr) throws DCMAApplicationException {

		String errMsg = null;
		DataCarrier dataCarrier = null;
		final CharSequence inputStr = span.getValue();
		if (null == inputStr || TableExtractionConstants.EMPTY.equals(inputStr)) {

			errMsg = "Invalid input character sequence.";
			LOGGER.info(errMsg);

		} else {

			if (null == patternStr || TableExtractionConstants.EMPTY.equals(patternStr)) {
				errMsg = "Invalid input pattern sequence.";
				throw new DCMAApplicationException(errMsg);
			}

			// Compile and use regular expression
			final Pattern pattern = Pattern.compile(patternStr);
			final Matcher matcher = pattern.matcher(inputStr);
			while (matcher.find()) {

				// Get all groups for this match
				for (int i = 0; i <= matcher.groupCount(); i++) {
					final String groupStr = matcher.group(i);
					List<Span> matchedSpans = null;
					if (groupStr != null) {
						matchedSpans = new ArrayList<Span>();
						matchedSpans.add(span);
						String matchedString = PatternMatcherUtil.getMatchedSpansValue(matchedSpans);
						if (!EphesoftStringUtil.isNullOrEmpty(matchedString)) {
							final float confidence = (groupStr.length() * CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE)
									/ matchedString.length();
							dataCarrier = new DataCarrier(matchedSpans, confidence, matchedString, null);
							LOGGER.info(groupStr);
						}
					}
				}
			}
		}

		return dataCarrier;
	}

/**
	 * Runs regex validation for the column extraction.
	 *
	 * @param expressionEvaluator {@list ExpressionEvaluator}<{@list Boolean}>
	 * @param columnHeaderValidationRequired boolean
	 * @param columnCoordinateValidationRequired boolean
	 * @param columnRowList {@link List}<{@link Column>
	 * @param colHeaderDataCarrier {@link DataCarrier}
	 * @param regexValidationDataCarrier {@link RegexValidationDataCarrier}
	 * @return boolean True if regex validation is passed.
	 * @throws DCMAApplicationException
	 */
	public static boolean runRegexValidation(final ExpressionEvaluator<Boolean> expressionEvaluator,
			final boolean columnHeaderValidationRequired, final boolean columnCoordinateValidationRequired,
			final List<Column> columnRowList, final DataCarrier colHeaderDataCarrier,
			final RegexValidationDataCarrier regexValidationDataCarrier) throws DCMAApplicationException {
		boolean isRegexValidationPassed = false;
		LOGGER.info("Applying Regex Validation for table extraction ..... ");
		TableColumnVO tableColumn = regexValidationDataCarrier.getTableColumn();
		if (EphesoftStringUtil.isNullOrEmpty(tableColumn.getExtractedDataColumnName())) {

			// columnHeadStartCoordinate and columnHeadEndCoordinate are used to take help of column data position
			// knowledge from column header/column coordinate, for Regex Validation based extraction.
			int columnHeadStartCoordinate = TableExtractionConstants.MINIMUM_POSITIVE_COORDINATE_VALUE;
			int columnHeadEndCoordinate = TableExtractionConstants.MINIMUM_POSITIVE_COORDINATE_VALUE;

			// Preparing a new column object.
			final Column column = regexValidationDataCarrier.getColumn();
			Column regexExtractionColumn = new Column();
			regexExtractionColumn.setValid(false);
			regexExtractionColumn.setValidationRequired(false);
			regexExtractionColumn.setConfidence(0.0f);
			regexExtractionColumn.setForceReview(false);
			regexExtractionColumn.setOcrConfidence(0.0f);
			regexExtractionColumn.setOcrConfidenceThreshold(0.0f);
			regexExtractionColumn.setValid(false);
			regexExtractionColumn.setValidationRequired(false);
			TableRowFinderUtility.setColumnProperties(regexValidationDataCarrier.getPageID(), regexExtractionColumn, null, 0);
			regexExtractionColumn.setName(tableColumn.getColumnName());
			if (isDataValid(expressionEvaluator, true, false, false)) {
				if (columnHeaderValidationRequired && null != colHeaderDataCarrier) {
					LOGGER.info("Fetching column coordinate start and end headers for Regex Validation.");
					Coordinates spanCoordinates = colHeaderDataCarrier.getCoordinates();
					if (null != spanCoordinates) {
						BigInteger X0 = spanCoordinates.getX0();
						BigInteger X1 = spanCoordinates.getX1();
						if (null != X0 && null != X1) {
							columnHeadStartCoordinate = X0.intValue();
							columnHeadEndCoordinate = X1.intValue();
						}
					}
				} else if (columnCoordinateValidationRequired) {
					ColumnCoordinates columnCoordinates = TableRowFinderUtility.getXColumncoordinates(tableColumn);
					Integer columnCoordX0 = columnCoordinates.getX0Coordinate();
					Integer columnCoordX1 = columnCoordinates.getX1coordinate();
					if (null != columnCoordX0 && null != columnCoordX1) {
						LOGGER.info("Fetching column coordinate start and end for Regex Validation.");
						columnHeadStartCoordinate = columnCoordX0.intValue();
						columnHeadEndCoordinate = columnCoordX1.intValue();
					}
				}
				LOGGER.debug("Column start coordinate is: ", columnHeadStartCoordinate, ". Column End Coordinate is: ",
						columnHeadEndCoordinate);
				regexValidationDataCarrier.setColumnCoordinates(null);
			}
			regexValidationDataCarrier.setColumn(regexExtractionColumn);
			isRegexValidationPassed = applyRegexValidation(columnRowList, columnHeadStartCoordinate, columnHeadEndCoordinate,
					regexValidationDataCarrier);
			if (isRegexValidationPassed) {
				TableRowFinderUtility.setColumnConfidence(tableColumn, regexExtractionColumn);
				if (EphesoftStringUtil.isNullOrEmpty(column.getValue()) && column.getConfidence() == 0
						|| regexExtractionColumn.getConfidence() > column.getConfidence()) {
					TableRowFinderUtility.swapColumnContents(regexExtractionColumn, column);
				}

				// Add columnCoordinateColumn to alternate values of column.
				if (null != regexExtractionColumn.getValue()) {
					column.getAlternateValues().getAlternateValue().add(regexExtractionColumn);
				}
			}
		}
		return isRegexValidationPassed;
	}

	/**
	 * Applies regex validation for column data extraction.
	 * 
	 * @param columnRowList {@link List}<{@link Column}> List of original table row columns for extraction of column data.
	 * @param columnHeadStartCoordinate int
	 * @param columnHeadEndCoordinate int
	 * @param regexValidationDataCarrier {@link RegexValidationDataCarrier}
	 * @return boolean True if regex validation is passed.
	 * @throws DCMAApplicationException
	 */
	private static boolean applyRegexValidation(final List<Column> columnRowList, final int columnHeadStartCoordinate,
			final int columnHeadEndCoordinate, final RegexValidationDataCarrier regexValidationDataCarrier)
			throws DCMAApplicationException {
		LOGGER.trace("Applying Regex Validation for table extraction ..... ");
		boolean isRegexValidationPassed = false;
		if (null != regexValidationDataCarrier) {
			TableColumnVO tableColumn = regexValidationDataCarrier.getTableColumn();
			final String rowData = regexValidationDataCarrier.getRowData();
			final List<Span> spanList = regexValidationDataCarrier.getSpanList();
			if (null != tableColumn && !CollectionUtil.isEmpty(columnRowList)) {
				final String columnName = tableColumn.getColumnName();
				final String patternOfColumnData = tableColumn.getColumnPattern();
				LOGGER.debug("Extracting column data for column = ", columnName, ". Column Pattern : ", patternOfColumnData);
				if (!EphesoftStringUtil.isNullOrEmpty(patternOfColumnData)) {
					final List<DataCarrier> dataCarrierList = TableRowFinderUtility
							.findPattern(rowData, patternOfColumnData, spanList);
					if (null == dataCarrierList || dataCarrierList.isEmpty()) {
						LOGGER.info("No data found for table column = ", columnName);
					} else {
						isRegexValidationPassed = TableRowFinderUtility.getRegexBasedExtraction(columnRowList,
								columnHeadStartCoordinate, columnHeadEndCoordinate, regexValidationDataCarrier, dataCarrierList);
					}
				}
			}
		}
		return isRegexValidationPassed;
	}

	/**
	 * This method checks if extracted column data is valid with validation methods.
	 * 
	 * @param expressionEvaluator
	 * @param isRegexValidationPassed
	 * @param isColumnHeaderValidationPassed
	 * @param isColumnCoordValidationPassed
	 * @return boolean
	 */
	public static boolean isDataValid(final ExpressionEvaluator<Boolean> expressionEvaluator, final boolean isRegexValidationPassed,
			final boolean isColumnHeaderValidationPassed, final boolean isColumnCoordValidationPassed) {
		boolean isDataValid = false;
		try {
			expressionEvaluator.putValue(TableExtractionTechnique.REGEX_VALIDATION.name(), Boolean.valueOf(isRegexValidationPassed));
			expressionEvaluator.putValue(TableExtractionTechnique.COLUMN_HEADER_VALIDATION.name(), Boolean
					.valueOf(isColumnHeaderValidationPassed));
			expressionEvaluator.putValue(TableExtractionTechnique.COLUMN_COORDINATES_VALIDATION.name(), Boolean
					.valueOf(isColumnCoordValidationPassed));
			isDataValid = expressionEvaluator.eval();
		} catch (final ScriptException se) {
			LOGGER.error("Exception occurred while ", se);
		}
		return isDataValid;
	}

	/**
	 * This method will extract the column data by applying column header validation and column coordinates validation, if needed.
	 * Column coordinates validation will be applied based on the value of colCoordalidationRequired.
	 * 
	 * @param columnHeaderExtractionDataCarrier {@link ColumnHeaderExtractionDataCarrier} Data carrier for header span
	 * @param gapBetweenColumnWords int
	 * @param isFirstPage {@link Boolean}
	 * @return boolean True if column header validation is passed.
	 */
	private static boolean getColumnDataByHeaderValidation(final ColumnHeaderExtractionDataCarrier columnHeaderExtractionDataCarrier,
			final int gapBetweenColumnWords, final Boolean isFirstPage) {
		LOGGER.info("Applying Column Header Validation for table extraction ..... ");
		List<Span> spanList = columnHeaderExtractionDataCarrier.getSpanList();
		StringBuilder outputValue = null;
		final CoordinatesList coordinatesList = new CoordinatesList();
		boolean isColHeaderValidationPassed;
		boolean isFirstSpan = true;
		Span lastSpan = null;
		DataCarrier colHeaderDataCarrier = columnHeaderExtractionDataCarrier.getColHeaderDataCarrier();
		LineDataCarrier lineDataCarrier = columnHeaderExtractionDataCarrier.getLineDataCarrier();
		if (CollectionUtils.isNotEmpty(spanList)) {
			outputValue = new StringBuilder();
			for (final Span span : spanList) {
				if (span != null && span.getValue() != null && !span.getValue().isEmpty() && span.getCoordinates() != null
						&& null != colHeaderDataCarrier) {
					final Coordinates coordinates = span.getCoordinates();
					LOGGER.info("Checking if the value: ", span.getValue(), " is valid with respect to column header: ",
							colHeaderDataCarrier.getValue());
					if (isValidCoordinatesWithColumnHeader(coordinates, colHeaderDataCarrier.getCoordinates(), isFirstPage)) {
						LOGGER.info(span.getValue(), " valid with column header.");
						lastSpan = span;
						if (isFirstSpan) {
							isFirstSpan = false;
							List<Span> leftSpanList = lineDataCarrier.appendSpansLeft(span, gapBetweenColumnWords);
							if (leftSpanList != null && !leftSpanList.isEmpty()) {
								for (final Span leftSpan : leftSpanList) {
									if (leftSpan.getValue() != null) {
										outputValue.append(leftSpan.getValue());
										outputValue.append(TableExtractionConstants.SPACE);
										coordinatesList.getCoordinates().add(leftSpan.getCoordinates());
									}
								}
							}

							// Fix for left span data getting appended on right side of first span.
							outputValue.append(span.getValue());
							outputValue.append(TableExtractionConstants.SPACE);
							coordinatesList.getCoordinates().add(span.getCoordinates());
						} else {
							outputValue.append(span.getValue());
							outputValue.append(TableExtractionConstants.SPACE);
							coordinatesList.getCoordinates().add(span.getCoordinates());
						}
					}
				}
			}
		}
		if (null != colHeaderDataCarrier && null != outputValue && !outputValue.toString().isEmpty()) {
			List<Span> rightSpanList = lineDataCarrier.appendSpansRight(lastSpan, gapBetweenColumnWords);
			if (rightSpanList != null && !rightSpanList.isEmpty()) {
				for (final Span rightSpan : rightSpanList) {
					if (rightSpan.getValue() != null) {
						outputValue.append(rightSpan.getValue());
						outputValue.append(TableExtractionConstants.SPACE);
						coordinatesList.getCoordinates().add(rightSpan.getCoordinates());
					}
				}
			}
			LOGGER.info(outputValue, " added to column value.");
			Column column = columnHeaderExtractionDataCarrier.getColumn();
			column.setValue(outputValue.toString().trim());
			column.setValid(Boolean.TRUE);
			column.setCoordinatesList(coordinatesList);
			isColHeaderValidationPassed = true;
		} else {
			isColHeaderValidationPassed = false;
		}
		LOGGER.info("Column header validation passed = ", isColHeaderValidationPassed);
		return isColHeaderValidationPassed;
	}

	/**
	 * Method to check if span is valid with respect to the column header specified by admin.
	 * 
	 * @param valueCoordinates {@link Coordinates}
	 * @param headerSpan {@link Coordinates}
	 * @param isFirstPage {@link Boolean}
	 * @return boolean
	 */
	private static boolean isValidCoordinatesWithColumnHeader(final Coordinates valueCoordinates,
			final Coordinates headerSpanHocrCoordinates, final Boolean isFirstPage) {
		LOGGER.info("Entering method isValidCoordinatesWithColumnHeader.");
		boolean isValid = false;
		if (null != headerSpanHocrCoordinates && null != valueCoordinates) {
			final BigInteger outputSpanX0 = valueCoordinates.getX0();
			final BigInteger outputSpanX1 = valueCoordinates.getX1();
			final BigInteger outputSpanY0 = valueCoordinates.getY0();
			final BigInteger headerSpanX0 = headerSpanHocrCoordinates.getX0();
			final BigInteger headerSpanX1 = headerSpanHocrCoordinates.getX1();
			final BigInteger headerSpanY1 = headerSpanHocrCoordinates.getY1();
			LOGGER.info("Value Coordinates: X0=", outputSpanX0, ", X1=", outputSpanX1, ", Y0=", outputSpanY0);
			LOGGER.info("Column Header Coordinates: X0=", headerSpanX0, ", X1=", headerSpanX1, ", Y1=", headerSpanY1);
			if (outputSpanX0 != null && outputSpanX0 != null && outputSpanY0 != null && headerSpanX0 != null && headerSpanX1 != null
					&& headerSpanY1 != null) {
				if (((outputSpanX0.compareTo(headerSpanX0) == 1 && outputSpanX0.compareTo(headerSpanX1) == -1)
						|| (outputSpanX1.compareTo(headerSpanX0) == 1 && outputSpanX1.compareTo(headerSpanX1) == -1)
						|| (outputSpanX0.compareTo(headerSpanX0) == -1 && outputSpanX1.compareTo(headerSpanX1) == 1)
						|| outputSpanX0.compareTo(headerSpanX0) == 0 || outputSpanX1.compareTo(headerSpanX1) == 0)) {
					if (null != isFirstPage && isFirstPage) {
						if (outputSpanY0.compareTo(headerSpanY1) == 1) {
							isValid = true;
						}
					} else {
						isValid = true;
					}
				}
			}
			LOGGER.info("Is span valid with column header : ", isValid);
		}
		LOGGER.info("Exiting method isValidCoordinatesWithColumnHeader.");
		return isValid;
	}

	/**
	 * Gets columns for the current row.
	 * 
	 * @param lineDataCarrier {@link LineDataCarrier}
	 * @param row {@link Row}
	 * @return {@link Row.Columns}
	 */
	public static Row.Columns getRowColumns(final LineDataCarrier lineDataCarrier, final Row row) {
		Row.Columns columnsRow = null;
		if (null != row && null != lineDataCarrier) {
			columnsRow = row.getColumns();

			if (null == columnsRow) {
				columnsRow = new Row.Columns();
				row.setColumns(columnsRow);
			}
			row.setRowCoordinates(lineDataCarrier.getRowCoordinates());
		}
		return columnsRow;
	}

	/**
	 * This method extracts all the rows, all rows between start and end pattern. 
	 * 
	 * @param pageList
	 * @param startPattern
	 * @param endPattern
	 * @param fuzzyMatchThresholdValue
	 * @return
	 * @throws DCMAApplicationException
	 */
	public static List<LineDataCarrier> searchAllRowOfTablesForTableExtractionWebServvice(final List<HocrPages> pageList,
			final String startPattern, final String endPattern, final float fuzzyMatchThresholdValue) throws DCMAApplicationException {
		List<LineDataCarrier> lineDataCarrierList = new LinkedList<LineDataCarrier>();
		boolean isFirstPage = true;
		SearchTableConfigDataCarrier searchTableConfigDataCarrier = new SearchTableConfigDataCarrier(startPattern, endPattern,
				fuzzyMatchThresholdValue);
		TableBoundaryDataCarrier tableBoundaryDataCarrier = new TableBoundaryDataCarrier();
		for (final HocrPages hocrPages : pageList) {
			if (null == hocrPages) {
				throw new DCMAApplicationException(EphesoftStringUtil.concatenate("No Hocr files found "));
			}
			final List<HocrPage> hocrPageList = hocrPages.getHocrPage();
			HocrPage hocrPage = hocrPageList.get(0);
			final String pageID = hocrPage.getPageID();
			LineDataCarrier lineDataCarrier = new LineDataCarrier(pageID);
			if (!isFirstPage) {
				addLine(lineDataCarrierList, lineDataCarrier);
			}
			isFirstPage = false;
			getTableLines(searchTableConfigDataCarrier, lineDataCarrierList, hocrPage, lineDataCarrier, tableBoundaryDataCarrier);
			if (null != tableBoundaryDataCarrier.getEndDataCarrier()) {
				break;
			}
		}
		return lineDataCarrierList;
	}
}
