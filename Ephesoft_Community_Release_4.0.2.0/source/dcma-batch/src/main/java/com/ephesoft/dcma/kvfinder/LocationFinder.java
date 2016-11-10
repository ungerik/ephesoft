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

package com.ephesoft.dcma.kvfinder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.common.HocrUtil;
import com.ephesoft.dcma.common.LineDataCarrier;
import com.ephesoft.dcma.common.PatternMatcherUtil;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.kvfinder.data.CustomList;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;

/**
 * This class is find the location of value extraction for a input of key extraction.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvextraction.KeyValueExtraction
 * @see com.ephesoft.dcma.kvextraction.util.OutputDataCarrier
 */
public class LocationFinder {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(LocationFinder.class);

	/**
	 * Confidence score.
	 */
	private String confidenceScore;

	/**
	 * Confidence score.
	 */
	private float confidenceScoreFloat = KVFinderConstants.DEFAULT_CONFIDENCE_SCORE;

	/**
	 * @return the confidenceScore
	 */
	public final String getConfidenceScore() {
		return confidenceScore;
	}

	/**
	 * @param confidenceScore the confidenceScore to set
	 */
	public final void setConfidenceScore(final String confidenceScore) {
		this.confidenceScore = confidenceScore;
		float confidenceFloat = KVFinderConstants.DEFAULT_CONFIDENCE_SCORE;
		try {
			confidenceFloat = Float.parseFloat(confidenceScore);
		} catch (NumberFormatException nfe) {
			LOGGER.error("Invalid value for confidence score specified. Setting it to its default value 100.");
		}
		this.confidenceScoreFloat = confidenceFloat;
	}

	/**
	 * Right location finder. Method will search to the right of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void rightLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			isValid = false;
		}
		LineDataCarrier lineOutputDataCarrier = lineOutputDataCarrierList.get(currentLineIndex);
		if (isValid && null == lineOutputDataCarrier) {
			isValid = false;
		}

		List<Span> spanList = lineOutputDataCarrier.getSpanList();
		if (isValid && null == spanList) {
			isValid = false;
		}
		if (isValid) {
			final String valuePattern = kVExtraction.getValuePattern();
			final List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(lineOutputDataCarrier,
					valuePattern, getConfidenceScore(), getWeightValue(kVExtraction.getWeightValue()));
			final BigInteger keyX1 = keyCoordinate.getX1();
			BigInteger minValue = null;
			BigInteger tempValue = null;
			OutputDataCarrier valueOutputDataCarrier = null;
			for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
				final Span span = dataCarrier.getSpan();
				final Coordinates valueCoordinates = span.getCoordinates();
				final BigInteger valueX0 = valueCoordinates.getX0();
				if (keyX1.longValue() >= valueX0.longValue()) {
					continue;
				}
				tempValue = valueX0.subtract(keyX1);
				if (tempValue.longValue() < 0) {
					tempValue = tempValue.negate();
				}
				if (null == minValue) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				} else if (tempValue.subtract(minValue).longValue() < 0) {
					minValue = tempValue;
					valueOutputDataCarrier = dataCarrier;
				}
			}
			// Append values to the right according to the number of words specified by admin.
			appendValues(kVExtraction, lineOutputDataCarrier, valueOutputDataCarrier);
			valueFoundData.add(valueOutputDataCarrier);
		}
	}

	private void addValues(LineDataCarrier lineDataCarrier, OutputDataCarrier valueOutputDataCarrier, int numberOfWords,
			Coordinates finalValueCoord) {
		StringBuffer finalValue = new StringBuffer(valueOutputDataCarrier.getValue());
		Span valueSpan = valueOutputDataCarrier.getSpan();

		if (null != valueSpan) {
			Integer spanIndex = lineDataCarrier.getIndexOfSpan(valueSpan);
			if (spanIndex != null) {
				Span rightSpan = null;
				for (int index = 0; index < numberOfWords; index++) {
					rightSpan = lineDataCarrier.getRightSpan(spanIndex);
					if (null != rightSpan && rightSpan.getValue() != null) {
						finalValue.append(KVFinderConstants.SPACE);
						finalValue.append(rightSpan.getValue());
						setValueCoordinates(finalValueCoord, rightSpan);
					}
					spanIndex = spanIndex + 1;
				}
			}
			valueOutputDataCarrier.setValue(finalValue.toString());
		}
	}

	private void setValueCoordinates(final Coordinates coordinates, final Span span) {
		BigInteger coordX0 = coordinates.getX0();
		BigInteger coordY0 = coordinates.getY0();
		BigInteger coordX1 = coordinates.getX1();
		BigInteger coordY1 = coordinates.getY1();
		Coordinates spanCoordinates = span.getCoordinates();
		if (null != spanCoordinates) {
			BigInteger spanX0 = span.getCoordinates().getX0();
			BigInteger spanY0 = span.getCoordinates().getY0();
			BigInteger spanX1 = span.getCoordinates().getX1();
			BigInteger spanY1 = span.getCoordinates().getY1();
			if (spanX0.compareTo(coordX0) == -1) {
				coordinates.setX0(spanX0);
			}
			if (spanY0.compareTo(coordY0) == -1) {
				coordinates.setY0(spanY0);
			}
			if (spanX1.compareTo(coordX1) == 1) {
				coordinates.setX1(spanX1);
			}
			if (spanY1.compareTo(coordY1) == 1) {
				coordinates.setY1(spanY1);
			}
		}
	}

	/**
	 * Left location finder. Method will search to the left of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void leftLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()) {
			isValid = false;
		}
		LineDataCarrier lineOutputDataCarrier = lineOutputDataCarrierList.get(currentLineIndex);
		if (isValid && null == lineOutputDataCarrier) {
			isValid = false;
		}
		List<Span> spanList = lineOutputDataCarrier.getSpanList();
		if (isValid && null == spanList) {
			isValid = false;
		}
		if (isValid) {
			final String valuePattern = kVExtraction.getValuePattern();
			final List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(lineOutputDataCarrier,
					valuePattern, getConfidenceScore(), getWeightValue(kVExtraction.getWeightValue()));
			final BigInteger keyX0 = keyCoordinate.getX0();
			BigInteger minValue = null;
			BigInteger tempValue = null;
			OutputDataCarrier valueOutputDataCarrier = null;
			for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
				final Span span = dataCarrier.getSpan();
				final Coordinates valueCoordinates = span.getCoordinates();
				final BigInteger valueX1 = valueCoordinates.getX1();
				if (valueX1.longValue() < keyX0.longValue()) {
					tempValue = keyX0.subtract(valueX1);
					if (tempValue.longValue() < 0) {
						tempValue = tempValue.negate();
					}
					if (null == minValue) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					} else if (tempValue.subtract(minValue).longValue() < 0) {
						minValue = tempValue;
						valueOutputDataCarrier = dataCarrier;
					}
				}
			}
			// Append values to the right according to the number of words specified by admin.
			appendValues(kVExtraction, lineOutputDataCarrier, valueOutputDataCarrier);
			valueFoundData.add(valueOutputDataCarrier);
		}
	}

	/**
	 * Bottom Right location finder. Method will search to the bottom right of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void bottomRightLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {

		boolean isValid = true;
		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()
				|| currentLineIndex + 1 >= lineOutputDataCarrierList.size()) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex + 1);
			if (isValid && null == topLineDataCarrier) {
				isValid = false;
			}

			List<Span> spanList = topLineDataCarrier.getSpanList();
			if (isValid && null == spanList) {
				isValid = false;
			}
			if (isValid) {
				Coordinates keyCoordinates = keyCoordinate;
				final String valuePattern = kVExtraction.getValuePattern();

				List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier, valuePattern,
						getConfidenceScore(), getWeightValue(kVExtraction.getWeightValue()));

				BigInteger keyX1 = keyCoordinates.getX1();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;
				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					BigInteger valueX1 = valueCoordinates.getX1();
					if (valueX1.longValue() > keyX1.longValue()) {
						tempValue = valueX0.subtract(keyX1);
						if (tempValue.longValue() < 0) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < 0) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Bottom left location finder. Method will search to the bottom left of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void bottomLeftLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()
				|| currentLineIndex + 1 >= lineOutputDataCarrierList.size()) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex + 1);
			if (isValid && null == topLineDataCarrier) {
				isValid = false;
			}
			List<Span> spanList = topLineDataCarrier.getSpanList();
			if (isValid && null == spanList) {
				isValid = false;
			}
			if (isValid) {
				Coordinates keyCoordinates = keyCoordinate;
				String valuePattern = kVExtraction.getValuePattern();

				final List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier,
						valuePattern, getConfidenceScore(), getWeightValue(kVExtraction.getWeightValue()));

				BigInteger keyX0 = keyCoordinates.getX0();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;
				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					if (valueX0.longValue() < keyX0.longValue()) {
						tempValue = valueX0.subtract(keyX0);
						if (tempValue.longValue() < 0) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < 0) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Bottom location finder. Method will search to the bottom of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void bottomLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size()
				|| currentLineIndex + 1 >= lineOutputDataCarrierList.size()) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex + 1);
			if (isValid && null == topLineDataCarrier) {
				isValid = false;
			}
			if (isValid) {
				List<Span> spanList = topLineDataCarrier.getSpanList();
				if (!isValid && null == spanList) {
					isValid = false;
				}
				Coordinates keyCoordinates = keyCoordinate;
				String valuePattern = kVExtraction.getValuePattern();

				final List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier,
						valuePattern, getConfidenceScore(), getWeightValue(kVExtraction.getWeightValue()));

				BigInteger keyX0 = keyCoordinates.getX0();
				BigInteger keyX1 = keyCoordinates.getX1();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;

				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					BigInteger valueX1 = valueCoordinates.getX1();
					if ((valueX0.longValue() < keyX1.longValue()) && (valueX1.longValue() >= keyX0.longValue())) {

						tempValue = valueX0.subtract(keyX0);
						if (tempValue.longValue() < 0) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < 0) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Top right location finder. Method will search to the top right of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void topRightLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size() || currentLineIndex - 1 < 0) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex - 1);
			if (null == topLineDataCarrier) {
				isValid = false;
			}
			if (isValid) {
				Coordinates keyCoordinates = keyCoordinate;
				String valuePattern = kVExtraction.getValuePattern();

				final List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier,
						valuePattern, getConfidenceScore(), getWeightValue(kVExtraction.getWeightValue()));

				BigInteger keyX1 = keyCoordinates.getX1();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;
				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					BigInteger valueX1 = valueCoordinates.getX1();

					// EPHESOFT-11277 KV extraction is not working correctly in case of Top and Top right postition.
					if (valueX1.longValue() > keyX1.longValue()) {
						tempValue = valueX0.subtract(keyX1);
						if (tempValue.longValue() < 0) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < 0) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Top left location finder. Method will search to the top left of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void topLeftLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size() || currentLineIndex - 1 < 0) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex - 1);
			if (null == topLineDataCarrier) {
				isValid = false;
			}
			if (isValid) {
				Coordinates keyCoordinates = keyCoordinate;
				String valuePattern = kVExtraction.getValuePattern();

				List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier, valuePattern,
						getConfidenceScore(), getWeightValue(kVExtraction.getWeightValue()));

				BigInteger keyX0 = keyCoordinates.getX0();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;
				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					if (valueX0.longValue() < keyX0.longValue()) {
						tempValue = valueX0.subtract(keyX0);
						if (tempValue.longValue() < 0) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < 0) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Top location finder. Method will search to the top of the key word.
	 * 
	 * @param kVExtraction {@link InputDataCarrier}
	 * @param valueFoundData List<LineDataCarrier>
	 * @param lineOutputDataCarrierList List<Span>
	 * @param currentLineIndex int
	 * @param keyCoordinate Coordinates
	 * @throws DCMAApplicationException If any exception occur.
	 */
	public final void topLocation(final InputDataCarrier kVExtraction, final CustomList valueFoundData,
			final List<LineDataCarrier> lineOutputDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		boolean isValid = true;
		if (currentLineIndex < 0 || currentLineIndex >= lineOutputDataCarrierList.size() || currentLineIndex - 1 < 0) {
			isValid = false;
		}
		if (isValid) {
			LineDataCarrier topLineDataCarrier = lineOutputDataCarrierList.get(currentLineIndex - 1);
			if (null == topLineDataCarrier) {
				isValid = false;
			}

			List<Span> spanList = topLineDataCarrier.getSpanList();
			String rowData = topLineDataCarrier.getLineRowData();
			if (null == spanList || null == rowData || rowData.isEmpty()) {
				isValid = false;
			}
			if (isValid) {
				Coordinates keyCoordinates = keyCoordinate;
				String valuePattern = kVExtraction.getValuePattern();

				List<OutputDataCarrier> afterValueExtractionList = PatternMatcherUtil.findPattern(topLineDataCarrier, valuePattern,
						getConfidenceScore(), getWeightValue(kVExtraction.getWeightValue()));

				BigInteger keyX0 = keyCoordinates.getX0();
				BigInteger keyX1 = keyCoordinates.getX1();
				BigInteger minValue = null;
				BigInteger tempValue = null;
				OutputDataCarrier valueOutputDataCarrier = null;

				for (OutputDataCarrier dataCarrier : afterValueExtractionList) {
					Span span = dataCarrier.getSpan();
					Coordinates valueCoordinates = span.getCoordinates();
					BigInteger valueX0 = valueCoordinates.getX0();
					BigInteger valueX1 = valueCoordinates.getX1();
					if ((valueX0.longValue() < keyX1.longValue()) && (valueX1.longValue() >= keyX0.longValue())) {
						tempValue = valueX0.subtract(keyX0);
						if (tempValue.longValue() < 0) {
							tempValue = tempValue.negate();
						}
						if (null == minValue) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						} else if (tempValue.subtract(minValue).longValue() < 0) {
							minValue = tempValue;
							valueOutputDataCarrier = dataCarrier;
						}
					}
				}
				// Append values to the right according to the number of words specified by admin.
				appendValues(kVExtraction, topLineDataCarrier, valueOutputDataCarrier);
				valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	private void appendValues(final InputDataCarrier kVExtraction, final LineDataCarrier lineDataCarrier,
			OutputDataCarrier valueOutputDataCarrier) {
		if (null != valueOutputDataCarrier) {
			String foundValue = valueOutputDataCarrier.getValue();
			Span span = valueOutputDataCarrier.getSpan();
			if (null != foundValue && !foundValue.isEmpty() && null != span) {
				float confidence = 0;
				List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
				coordinatesList.add(span.getCoordinates());
				String[] foundValArr = foundValue.split(KVFinderConstants.SPACE);
				Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
				Span rightSpan = null;
				Span lastSpan = span;
				int foundDataLength = foundValArr.length;
				if (foundDataLength > 0 && span.getValue() != null && !span.getValue().isEmpty()) {
					confidence = confidence + foundValArr[0].length() * this.confidenceScoreFloat / span.getValue().length();
				}
				if (null != spanIndex && null != foundValArr && foundDataLength > 1) {
					for (int count = 1; count < foundDataLength; count++) {
						rightSpan = lineDataCarrier.getRightSpan(spanIndex);
						if (null != rightSpan) {
							String currSpanValue = rightSpan.getValue();
							if (currSpanValue != null && !currSpanValue.isEmpty()) {
								confidence = confidence + foundValArr[count].length() * this.confidenceScoreFloat
										/ currSpanValue.length();
							}
							coordinatesList.add(rightSpan.getCoordinates());
							lastSpan = rightSpan;
						}
						spanIndex = spanIndex + 1;
					}
				}
				Coordinates valueCoordinates = HocrUtil.getRectangleCoordinates(coordinatesList);
				Integer noOfWords = kVExtraction.getNoOfWords();
				int numberOfWords = 0;
				Span valueSpan = new Span();
				valueSpan.setCoordinates(valueCoordinates);
				if (null != noOfWords && noOfWords > 0) {
					numberOfWords = noOfWords;
					valueOutputDataCarrier.setSpan(lastSpan);
					addValues(lineDataCarrier, valueOutputDataCarrier, numberOfWords, valueCoordinates);
				}
				valueOutputDataCarrier.setSpan(valueSpan);
				if (foundDataLength > 0) {
					valueOutputDataCarrier.setConfidence((float) ((confidence / foundDataLength) * kVExtraction.getWeightValue()));
				}
				// valueFoundData.add(valueOutputDataCarrier);
			}
		}
	}

	/**
	 * Method to locate the values in a zone on the basis of fetch Value
	 * 
	 * @param zonalLineList
	 * @param kvExtraction
	 * @param valueFoundData
	 * @param zoneCoordinates
	 */
	private void createOutputDataCarrierList(List<LineDataCarrier> zonalLineList, final InputDataCarrier kvExtraction,
			CustomList valueFoundData, final Coordinates zoneCoordinates) throws DCMAApplicationException {
		List<OutputDataCarrier> dataCarrierList = new ArrayList<OutputDataCarrier>();
		for (Iterator<LineDataCarrier> iterator = zonalLineList.iterator(); iterator.hasNext();) {
			LineDataCarrier lineDataCarrier = (LineDataCarrier) iterator.next();
			dataCarrierList.addAll(applyValuePattern(kvExtraction, zoneCoordinates, lineDataCarrier));
		}
		if (dataCarrierList != null && !dataCarrierList.isEmpty()) {
			// valueFoundData.addAll(dataCarrierList);
			// Basis of fetchValue : ALL , FIRST , LAST
			switch (kvExtraction.getFetchValue()) {
				case ALL:
					valueFoundData.addAll(dataCarrierList);
					break;
				case LAST:
					valueFoundData.add(dataCarrierList.get(dataCarrierList.size() - 1));
					break;
				case FIRST:
					valueFoundData.add(dataCarrierList.get(0));
					break;
				default:
					break;
			}
		}
		if (valueFoundData != null && !valueFoundData.isEmpty() && valueFoundData.size() > 1) {
			List<OutputDataCarrier> sortedList = getSortedList(valueFoundData);
			concatenateList(valueFoundData, sortedList);
		}
	}

	private void concatenateList(final CustomList valueFoundData, final List<OutputDataCarrier> outputDataCarriers) {
		StringBuffer valueList = new StringBuffer();
		Coordinates coordinates = new Coordinates();
		float confidence = 0;
		for (int index = 0; index < outputDataCarriers.size(); index++) {
			OutputDataCarrier outputDataCarrier = outputDataCarriers.get(index);
			Span span = outputDataCarrier.getSpan();
			if (null != span && span.getCoordinates() != null) {
				if (index == 0) {
					coordinates.setX0(span.getCoordinates().getX0());
					coordinates.setY0(span.getCoordinates().getY0());
					coordinates.setX1(span.getCoordinates().getX1());
					coordinates.setY1(span.getCoordinates().getY1());
					confidence = outputDataCarrier.getConfidence();
				} else {
					setValueCoordinates(coordinates, span);
				}
			}
			valueList.append(KVFinderConstants.SPACE).append(outputDataCarrier.getValue());
		}

		Span span = new Span();
		span.setValue(valueList.toString());
		span.setCoordinates(coordinates);
		OutputDataCarrier finalConcatenatedoutput = new OutputDataCarrier(span, confidence, valueList.toString());
		valueFoundData.clear();
		valueFoundData.add(finalConcatenatedoutput);
	}

	/**
	 * Method to determine whether the zone is valid with correct co-ordinates
	 * 
	 * @param zoneCoordinates
	 * @return
	 */
	private boolean isValidZone(final Coordinates zoneCoordinates) {
		boolean isValidZone = true;
		if (zoneCoordinates.getX0().longValue() <= 0 && zoneCoordinates.getX1().longValue() <= 0
				&& zoneCoordinates.getY0().longValue() <= 0 && zoneCoordinates.getY1().longValue() <= 0) {
			isValidZone = false;
		}
		return isValidZone;
	}

	/**
	 * @param kvExtraction
	 * @param zoneCoordinates
	 * @param lineDataCarrier
	 * @return
	 */
	private List<OutputDataCarrier> applyValuePattern(final InputDataCarrier kvExtraction, final Coordinates zoneCoordinates,
			final LineDataCarrier lineDataCarrier) throws DCMAApplicationException {
		final List<OutputDataCarrier> dataCarrierList = PatternMatcherUtil.findPattern(lineDataCarrier,
				kvExtraction.getValuePattern(), getConfidenceScore(), getWeightValue(kvExtraction.getWeightValue()));
		float confidenceInt = KVFinderConstants.DEFAULT_CONFIDENCE_SCORE;
		try {
			confidenceInt = Float.parseFloat(getConfidenceScore());
		} catch (NumberFormatException nfe) {
			LOGGER.error("Invalid value for confidence score specified. Setting it to its default value 100.");
		}
		final List<OutputDataCarrier> finalDataCarrierList = new ArrayList<OutputDataCarrier>();
		finalDataCarrierList.addAll(dataCarrierList);
		boolean isValidData = true;
		List<Coordinates> coordinatesList = new ArrayList<Coordinates>();
		if (dataCarrierList != null && !dataCarrierList.isEmpty()) {
			for (OutputDataCarrier dataCarrier : dataCarrierList) {
				float confidence = 0;
				isValidData = true;
				// Add coordinates for multiple word capture support.
				Span span = dataCarrier.getSpan();
				coordinatesList.add(span.getCoordinates());
				String foundValue = dataCarrier.getValue();
				String[] foundValArr = foundValue.split(KVFinderConstants.SPACE);
				Integer spanIndex = lineDataCarrier.getIndexOfSpan(span);
				Span rightSpan = null;
				int wordsCount = foundValArr.length;
				if (wordsCount > 0 && span.getValue() != null && !span.getValue().isEmpty()) {
					confidence = confidence + foundValArr[0].length() * confidenceInt / span.getValue().length();
				}
				
				// Removed isValidCoordinatesForZone method as no longer needed. Used HocrUtil.isInsideZone() method instead.
				if (!HocrUtil.isInsideZone(span.getCoordinates(), zoneCoordinates)) {
					finalDataCarrierList.remove(dataCarrier);
					isValidData = false;
				} else if (null != spanIndex && null != foundValArr && foundValArr.length > 1) {
					for (int count = 1; count < wordsCount; count++) {
						rightSpan = lineDataCarrier.getRightSpan(spanIndex);
						if (null != rightSpan && !HocrUtil.isInsideZone(rightSpan.getCoordinates(), zoneCoordinates)) {
							finalDataCarrierList.remove(dataCarrier);
							isValidData = false;
							break;
						} else if (null != rightSpan) {
							String currSpanValue = rightSpan.getValue();
							if (currSpanValue != null && !currSpanValue.isEmpty()) {
								confidence = confidence + foundValArr[count].length() * confidenceInt / currSpanValue.length();
							}
							coordinatesList.add(rightSpan.getCoordinates());
						}
						spanIndex = spanIndex + 1;
					}
				}
				if (isValidData) {
					Span valueSpan = new Span();
					if (wordsCount > 1) {
						confidence = confidence / wordsCount;
					}
					dataCarrier.setConfidence((float) (confidence * kvExtraction.getWeightValue()));
					valueSpan.setCoordinates(HocrUtil.getRectangleCoordinates(coordinatesList));
					dataCarrier.setSpan(valueSpan);
				}
				coordinatesList.clear();
			}
		}
		return finalDataCarrierList;
	}

	private List<OutputDataCarrier> getSortedList(final CustomList valueFoundData) {

		// Changing from toList to toAscendingList as it creates the output list in wrong order.
		List<OutputDataCarrier> list = valueFoundData.getAscendingList();
		Collections.sort(list, new Comparator<OutputDataCarrier>() {

			public int compare(final OutputDataCarrier outputCarrier1, final OutputDataCarrier outputCarrier2) {
				final Span firstSpan = outputCarrier1.getSpan();
				final Span secSpan = outputCarrier2.getSpan();
				BigInteger s1Y0 = firstSpan.getCoordinates().getY0();
				BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
				final BigInteger s2Y0 = secSpan.getCoordinates().getY0();
				final BigInteger s2Y1 = secSpan.getCoordinates().getY1();
				int y1 = s2Y1.intValue() + ((s2Y1.intValue() - s2Y0.intValue()) / 2);
				int y0 = s2Y0.intValue() - ((s2Y1.intValue() - s2Y0.intValue()) / 2);
				if (isApproxEqual(s1Y0.intValue(), s2Y0.intValue(), KVFinderConstants.MAX_SPAN_DIFFERENCE) && s1Y1.intValue() > y1) {
					s1Y1 = BigInteger.valueOf(y1);
					firstSpan.getCoordinates().setY1(s1Y1);
				} else if (isApproxEqual(s1Y1.intValue(), s2Y1.intValue(), KVFinderConstants.MAX_SPAN_DIFFERENCE)
						&& s1Y0.intValue() < y0) {
					s1Y0 = BigInteger.valueOf(y0);
					firstSpan.getCoordinates().setY0(s1Y0);
				}
				final BigInteger s1Y = s1Y1.add(s1Y0);
				final BigInteger s2Y = s2Y1.add(s2Y0);
				final int oldSpanMid = s2Y.intValue() / 2;
				int returnValue = 0;
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
		return list;
	}

	/**
	 * Checks if two coordinates are nearly equal.
	 * 
	 * @param first int
	 * @param second int
	 * @return boolean, true if parameter coordinates are equal.
	 */
	private boolean isApproxEqual(final int first, final int second, final int difference) {
		boolean result;
		int compare = first - second;
		if (compare < 0) {
			compare = -compare;
		}
		if (compare <= difference) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}

	/**
	 * @param inputDataCarrier
	 * @param outputDataCarrierList
	 * @param lineDataCarrierList
	 * @param currentLineIndex
	 * @param keyCoordinate
	 * @throws DCMAApplicationException
	 */
	public void extractValueFromZone(final InputDataCarrier inputDataCarrier, final CustomList outputDataCarrierList,
			final List<LineDataCarrier> lineDataCarrierList, final int currentLineIndex, final Coordinates keyCoordinate)
			throws DCMAApplicationException {
		Coordinates valueZone = createValueZone(keyCoordinate, inputDataCarrier);
		if (isValidZone(valueZone)) {
			List<LineDataCarrier> zonalLineList = getZonalLineList(lineDataCarrierList, currentLineIndex, valueZone);
			createOutputDataCarrierList(zonalLineList, inputDataCarrier, outputDataCarrierList, valueZone);
		}
	}

	/**
	 * @param lineDataCarrierList
	 * @param currentLineIndex
	 * @param valueZone
	 * @return
	 */
	private List<LineDataCarrier> getZonalLineList(final List<LineDataCarrier> lineDataCarrierList, final int currentLineIndex,
			final Coordinates valueZone) {
		LOGGER.info("Entering method getZonalLineList....");
		// LineDataCarrier topLineDataCarrier = null;
		LineDataCarrier lineDataCarrier = null;
		List<LineDataCarrier> zonalLineList = new ArrayList<LineDataCarrier>();
		boolean firstValidRowFound = false;
		LineDataCarrier validLineDataCarrier = null;
		for (int index = currentLineIndex; index >= 0; index--) {
			lineDataCarrier = lineDataCarrierList.get(index);
			if (null == lineDataCarrier) {
				continue;
			}
			Coordinates rowCoordinates = lineDataCarrier.getRowCoordinates();
			if (HocrUtil.isValidRowForZone(rowCoordinates, valueZone)) {
				getValidZonalData(valueZone, lineDataCarrier, zonalLineList);
				zonalLineList.add(0, validLineDataCarrier);
				firstValidRowFound = true;
			} else if (firstValidRowFound) {
				break;
			}
		}
		firstValidRowFound = false;
		for (int index = currentLineIndex + 1; index < lineDataCarrierList.size(); index++) {
			lineDataCarrier = lineDataCarrierList.get(index);
			if (null == lineDataCarrier) {
				continue;
			}
			Coordinates rowCoordinates = lineDataCarrier.getRowCoordinates();
			if (HocrUtil.isValidRowForZone(rowCoordinates, valueZone)) {
				getValidZonalData(valueZone, lineDataCarrier, zonalLineList);
				firstValidRowFound = true;
			} else if (firstValidRowFound) {
				break;
			}
		}
		LOGGER.info("Exiting method getZonalLineList....");
		return zonalLineList;
	}

	private void getValidZonalData(final Coordinates valueZone, LineDataCarrier lineDataCarrier, List<LineDataCarrier> zonalLineList) {
		List<Span> spanList;
		LineDataCarrier validLineDataCarrier;
		validLineDataCarrier = new LineDataCarrier(lineDataCarrier.getPageID());
		spanList = lineDataCarrier.getSpanList();
		if (spanList != null) {
			for (Span span : spanList) {
				if (span != null && HocrUtil.isInsideZone(span.getCoordinates(), valueZone)) {
					validLineDataCarrier.getSpanList().add(span);
				}
			}
		}
		zonalLineList.add(validLineDataCarrier);
	}

	/**
	 * This method creates a value zone relative to the key coordinates.
	 * 
	 * @param keyCoordinate {@link Coordinates}
	 * @param inputDataCarrier {@link InputDataCarrier}
	 * @return {@link Coordinates}
	 */
	private Coordinates createValueZone(final Coordinates keyCoordinate, final InputDataCarrier inputDataCarrier) {
		Coordinates valueZone = new Coordinates();
		if (keyCoordinate != null) {
			final int xOffset = inputDataCarrier.getXoffset();
			final int yOffset = inputDataCarrier.getYoffset();
			boolean isKeyMidPointAlgoUsed = checkKeyMidPointAlgoUsed(inputDataCarrier);
			if (isKeyMidPointAlgoUsed) {

				// Modified logic for KV Extraction from bottom right to Mid Point of the Key Overlay.
				valueZone.setX0(BigInteger.valueOf(((keyCoordinate.getX0().intValue() + keyCoordinate.getX1().intValue()) / 2)
						+ xOffset));
				valueZone.setY0(BigInteger.valueOf(((keyCoordinate.getY0().intValue() + keyCoordinate.getY1().intValue()) / 2)
						+ yOffset));
			} else {
				valueZone.setX0(BigInteger.valueOf(keyCoordinate.getX1().intValue() + xOffset));
				valueZone.setY0(BigInteger.valueOf(keyCoordinate.getY1().intValue() + yOffset));
			}
			final int length = inputDataCarrier.getLength();
			final int width = inputDataCarrier.getWidth();
			valueZone.setX1(valueZone.getX0().add(BigInteger.valueOf(length)));
			valueZone.setY1(valueZone.getY0().add(BigInteger.valueOf(width)));
		}
		return valueZone;
	}

	private double getWeightValue(Float weightValue) {
		double tempWeightValue = 1.0;
		if (null != weightValue && 0.0 < weightValue) {
			tempWeightValue = weightValue;
		}
		return tempWeightValue;
	}

	/**
	 * This method checks whether or not the key mid point kv extraction algo is used or not.
	 * 
	 * @param inputDataCarrier {@link InputDataCarrier}
	 * @return boolean
	 */
	private boolean checkKeyMidPointAlgoUsed(final InputDataCarrier inputDataCarrier) {
		boolean isKeyMidPointAlgoUsed;
		final Coordinates keyCoordinate = inputDataCarrier.getKeyRectangleCoordinates();
		final Coordinates valueCoordinate = inputDataCarrier.getValueRectangleCoordinates();
		final int xOffset = inputDataCarrier.getXoffset();
		final int yOffset = inputDataCarrier.getYoffset();

		if (null == keyCoordinate || null == valueCoordinate) {
			isKeyMidPointAlgoUsed = false;
		} else {
			final int calculatedXOffset = (int) Math.round(valueCoordinate.getX0().intValue()
					- ((keyCoordinate.getX0().intValue() + keyCoordinate.getX1().intValue()) / 2.0));
			final int calculatedYOffset = (int) Math.round(valueCoordinate.getY0().intValue()
					- ((keyCoordinate.getY0().intValue() + keyCoordinate.getY1().intValue()) / 2.0));

			if (isApproxEqual(calculatedXOffset, xOffset, KVFinderConstants.MAX_OFFSET_DIFFERENCE)
					&& isApproxEqual(calculatedYOffset, yOffset, KVFinderConstants.MAX_OFFSET_DIFFERENCE)) {
				isKeyMidPointAlgoUsed = true;
			} else {
				isKeyMidPointAlgoUsed = false;
			}
		}
		return isKeyMidPointAlgoUsed;
	}

}
