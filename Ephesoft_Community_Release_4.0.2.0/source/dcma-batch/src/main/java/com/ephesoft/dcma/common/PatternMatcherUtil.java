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

package com.ephesoft.dcma.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.core.common.constants.CommonConstants;
import com.ephesoft.dcma.core.exception.DCMAApplicationException;
import com.ephesoft.dcma.kvfinder.KVFinderConstants;
import com.ephesoft.dcma.kvfinder.data.InputDataCarrier;
import com.ephesoft.dcma.kvfinder.data.OutputDataCarrier;
import com.ephesoft.dcma.tablefinder.constants.TableExtractionConstants;
import com.ephesoft.dcma.tablefinder.data.DataCarrier;
import com.ephesoft.dcma.util.CollectionUtil;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

public class PatternMatcherUtil {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(PatternMatcherUtil.class);

	/**
	 * API is responsible for finding the patterns and returned the found data List.
	 * 
	 * @param lineDataCarrier
	 * @param patternStr
	 * @param confidenceScore
	 * @return
	 * @throws DCMAApplicationException
	 */
	public static final List<OutputDataCarrier> findPattern(final LineDataCarrier lineDataCarrier, final String patternStr,
			final String confidenceScore) throws DCMAApplicationException {

		List<OutputDataCarrier> dataCarrierList = new ArrayList<OutputDataCarrier>(0);
		if (lineDataCarrier != null) {
			final CharSequence inputStr = lineDataCarrier.getLineRowData();
			final List<Span> spanList = lineDataCarrier.getSpanList();
			if (null == inputStr || KVFinderConstants.EMPTY.equals(inputStr)) {
				LOGGER.info("No data found for pattern extraction, returning from method");
			} else {
				if (null == patternStr || KVFinderConstants.EMPTY.equals(patternStr)) {
					LOGGER.error("Invalid input pattern sequence.");
					// throw new DCMAApplicationException(errMsg);
				} else {
					int confidenceInt = CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE;
					try {
						confidenceInt = Integer.parseInt(confidenceScore);
					} catch (NumberFormatException nfe) {
						LOGGER.error(nfe, nfe.getMessage());
					}
					// Compile and use regular expression
					final Pattern pattern = Pattern.compile(patternStr);
					final Matcher matcher = pattern.matcher(inputStr);
					while (matcher.find()) {
						// Get all groups for this match
						for (int i = 0; i <= matcher.groupCount(); i++) {
							final String groupStr = matcher.group(i);
							final int startIndex = matcher.start();
							final Span matchedSpan = getMatchedSpan(spanList, startIndex);
							if (groupStr != null && matchedSpan != null) {
								final float confidence = (groupStr.length() * confidenceInt) / inputStr.length();
								OutputDataCarrier dataCarrier = new OutputDataCarrier(matchedSpan, confidence, groupStr);
								dataCarrierList.add(dataCarrier);
								LOGGER.info(groupStr);
							}
						}
					}
				}
			}
		}
		return dataCarrierList;
	}

	public static final List<OutputDataCarrier> findPattern(final LineDataCarrier lineDataCarrier, final String patternStr,
			final String confidenceScore, final double weightValue) throws DCMAApplicationException {

		List<OutputDataCarrier> dataCarrierList = new ArrayList<OutputDataCarrier>(0);
		if (lineDataCarrier != null) {
			final CharSequence inputStr = lineDataCarrier.getLineRowData();
			final List<Span> spanList = lineDataCarrier.getSpanList();
			if (null == inputStr || KVFinderConstants.EMPTY.equals(inputStr)) {
				LOGGER.info("No data found for pattern extraction, returning from method");
			} else {
				if (null == patternStr || KVFinderConstants.EMPTY.equals(patternStr)) {
					LOGGER.error("Invalid input pattern sequence.");
					// throw new DCMAApplicationException(errMsg);
				} else {
					int confidenceInt = CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE;
					try {
						confidenceInt = Integer.parseInt(confidenceScore);
					} catch (NumberFormatException nfe) {
						LOGGER.error(nfe, nfe.getMessage());
					}
					// Compile and use regular expression
					final Pattern pattern = Pattern.compile(patternStr);
					final Matcher matcher = pattern.matcher(inputStr);
					while (matcher.find()) {
						// Get all groups for this match
						for (int i = 0; i <= matcher.groupCount(); i++) {
							final String groupStr = matcher.group(i);
							final int startIndex = matcher.start();
							final Span matchedSpan = getMatchedSpan(spanList, startIndex);
							if (groupStr != null && matchedSpan != null) {
								final float confidence = (float) (((groupStr.length() * confidenceInt) / inputStr.length()) * weightValue);
								LOGGER.info("Confidence: " + confidence + " for weight value: " + weightValue);
								OutputDataCarrier dataCarrier = new OutputDataCarrier(matchedSpan, confidence, groupStr);
								dataCarrierList.add(dataCarrier);
								LOGGER.info(groupStr);
							}
						}
					}
				}
			}
		}
		return dataCarrierList;
	}

	/**
	 * Method is responsible for finding the patterns and returned the found data List.
	 * 
	 * @param span Span
	 * @param patternStr String
	 * @return OutputDataCarrier
	 * @throws DCMAApplicationException Check for all the input parameters and find the pattern.
	 */
	public final OutputDataCarrier findPattern(final Span span, final String patternStr, final String confidenceScore)
			throws DCMAApplicationException {

		String errMsg = null;
		OutputDataCarrier dataCarrier = null;
		final CharSequence inputStr = span.getValue();
		if (null == inputStr || KVFinderConstants.EMPTY.equals(inputStr)) {
			errMsg = "Invalid input character sequence.";
			// throw new DCMAApplicationException(errMsg);
			LOGGER.info(errMsg);
		} else {
			if (null == patternStr || KVFinderConstants.EMPTY.equals(patternStr)) {
				errMsg = "Invalid input pattern sequence.";
				// throw new DCMAApplicationException(errMsg);
			} else {
				int confidenceInt = CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE;
				try {
					confidenceInt = Integer.parseInt(confidenceScore);
				} catch (NumberFormatException nfe) {
					LOGGER.error(nfe, nfe.getMessage());
				}
				// Compile and use regular expression
				final Pattern pattern = Pattern.compile(patternStr);
				final Matcher matcher = pattern.matcher(inputStr);
				// boolean matchFound = matcher.find();
				while (matcher.find()) {
					// Get all groups for this match
					for (int i = 0; i <= matcher.groupCount(); i++) {
						final String groupStr = matcher.group(i);

						if (groupStr != null) {
							final float confidence = (groupStr.length() * confidenceInt) / inputStr.length();
							dataCarrier = new OutputDataCarrier(span, confidence, groupStr);
							LOGGER.info(groupStr);
						}
					}
				}
			}
		}

		return dataCarrier;
	}

	private static Span getMatchedSpan(final List<Span> spanList, final int startIndex) {
		int spanIndex = 0;
		boolean isFirstSpan = Boolean.TRUE;
		Span matchedSpan = null;
		for (Span span : spanList) {
			if (null != span && null != span.getValue()) {
				spanIndex = spanIndex + span.getValue().length();
				if (!isFirstSpan) {
					spanIndex = spanIndex + 1;
				}
				if (spanIndex > startIndex) {
					matchedSpan = span;
					break;
				}
			}
			isFirstSpan = Boolean.FALSE;
		}
		return matchedSpan;
	}

	/**
	 * Finds the fuzzy match for the input string based on the fuzzy match threshold value and returns the {@link List} of
	 * {@link OutputDataCarrier} . It returns {@code null} or empty list if no match is found.
	 * 
	 * @param lineDataCarrier represents the instance of data structure {@link LineDataCarrier}.
	 * @param patternStr represents the pattern {@link String} to which input string is to be matched.
	 * @param keyFuzzyness {@link Float} represents the key fuzzyness threshold value for the key pattern.
	 * @return the {@link List} of data structure {@link OutputDataCarrier}.
	 */
	public static final List<OutputDataCarrier> findFuzzyPattern(final LineDataCarrier lineDataCarrier, final String patternStr,
			final Float keyFuzzyness) {

		List<OutputDataCarrier> dataCarrierList = null;
		if (null != lineDataCarrier) {
			final String inputStr = lineDataCarrier.getLineRowData();
			final List<Span> spanList = lineDataCarrier.getSpanList();
			if (EphesoftStringUtil.isNullOrEmpty(patternStr)) {
				LOGGER.error("Invalid input pattern sequence.");
			} else if (EphesoftStringUtil.isNullOrEmpty(inputStr)) {
				LOGGER.info("No data found for pattern extraction, returning from method");
			} else {
				dataCarrierList = new ArrayList<OutputDataCarrier>();
				findFuzzyPattern(patternStr, dataCarrierList, inputStr, spanList, keyFuzzyness);
			}
		}
		return dataCarrierList;
	}

	/**
	 * Finds the fuzzy match for the input string based on the fuzzy match threshold value and append the matched value to the
	 * {@link List} of {@link OutputDataCarrier}.
	 * 
	 * @param inputDataCarrier represents the instance of data structure {@link InputDataCarrier}.
	 * @param patternStr represents the pattern {@link String} to which input string is to be matched.
	 * @param dataCarrierList represents the {@link List} of data structure {@link LineDataCarrier}.
	 * @param inputStr represents the input string {@link String} to be matched.
	 * @param spanList represents the {@link List} of {@link Span}.
	 * @param keyFuzzyness {@link Float} represents the key fuzzyness threshold value for the key pattern.
	 */
	private static void findFuzzyPattern(final String patternStr, final List<OutputDataCarrier> dataCarrierList,
			final String inputStr, final List<Span> spanList, final Float keyFuzzyness) {
		int location = KVFinderConstants.DEFAULT_FUZZY_SEARCH_LOCATION;
		final float matchThershold = getMatchThersholdValue(keyFuzzyness);
		final int patternStrLength = patternStr.length();
		String strTobeMatched = inputStr;
		String strTobeMatchedUpperCase = inputStr.toUpperCase(Locale.ENGLISH);
		String patterStrUpperCase = patternStr.toUpperCase(Locale.ENGLISH);
		while (true) {
			final int result = FuzzyMatchUtil.matchInputString(strTobeMatchedUpperCase, patterStrUpperCase, location, matchThershold);
			if (result == CommonConstants.NO_FUZZY_MATCH_ERROR_CODE) {
				break;
			} else if (result == CommonConstants.PATTERN_LENGTH_FUZZY_MATCH_ERROR_CODE) {
				LOGGER.error("Pattern too long for applying Fuzzy matching.");
				break;
			} else {
				final int strMatchedLength = strTobeMatchedUpperCase.length();
				if ((result + patternStrLength) > strMatchedLength) {
					final String resultStr = strTobeMatched.substring(result, strMatchedLength);
					final Span matchedSpan = getMatchedSpan(spanList, result);
					addOutputDataCareer(dataCarrierList, resultStr, matchedSpan);
					break;

				} else {
					final String resultStr = strTobeMatched.substring(result, result + patternStrLength);
					final Span matchedSpan = getMatchedSpan(spanList, result);
					addOutputDataCareer(dataCarrierList, resultStr, matchedSpan);
					strTobeMatched = strTobeMatched.substring(result + patternStrLength);
					strTobeMatchedUpperCase = strTobeMatchedUpperCase.substring(result + patternStrLength);
					location = location + result + patternStrLength;
				}
			}
		}
	}

	/**
	 * Finds the fuzzy match for the input string based on the fuzzy match threshold value and return the {@link DataCarrier} having
	 * the best confidence.
	 * 
	 * @param patternString {@link String} represents the pattern {@link String} to which input string is to be matched.
	 * @param inputString {@link String} represents the input string {@link String} to be matched.
	 * @param matchThersholdValue represents the fuzzy match threshold value for the pattern
	 * @param spanList {@link List}<{@link Span}> represents the {@link List} of {@link Span}.
	 * @return {@link DataCarrier} based on fuzzy match threshold value having high confidence value.
	 */
	public static DataCarrier findFuzzyPattern(final String inputString, final String patternString, final float matchThersholdValue,
			final List<Span> spanList) {
		DataCarrier dataCarrier = null;
		int location = KVFinderConstants.DEFAULT_FUZZY_SEARCH_LOCATION;
		if (EphesoftStringUtil.isNullOrEmpty(patternString)) {
			LOGGER.error("Invalid regex pattern sequence.");
		} else if (EphesoftStringUtil.isNullOrEmpty(inputString)) {
			LOGGER.info("No data found for pattern extraction, returning from finding fuzzy pattern method.");
		} else {
			String stringTobeMatched = inputString;
			String stringTobeMatchedInUpperCase = inputString.toUpperCase(Locale.ENGLISH);
			String patterStrUpperCase = patternString.toUpperCase(Locale.ENGLISH);
			final int patternStrLength = patternString.length();
			while (true) {
				final int startIndex = FuzzyMatchUtil.matchInputString(stringTobeMatchedInUpperCase, patterStrUpperCase, location,
						matchThersholdValue);
				if (startIndex == CommonConstants.NO_FUZZY_MATCH_ERROR_CODE
						|| startIndex == CommonConstants.PATTERN_LENGTH_FUZZY_MATCH_ERROR_CODE) {
					break;
				} else {
					final int strToBeMatchedLength = stringTobeMatchedInUpperCase.length();
					int endIndex = 0;
					if ((startIndex + patternStrLength) > strToBeMatchedLength) {
						endIndex = strToBeMatchedLength;
						final String resultStr = stringTobeMatched.substring(startIndex, endIndex);
						dataCarrier = getDataCarrier(spanList, dataCarrier, resultStr, getMatchedSpans(spanList, startIndex, endIndex));
						break;
					} else {
						endIndex = startIndex + patternStrLength;
						final String resultStr = stringTobeMatched.substring(startIndex, endIndex);
						dataCarrier = getDataCarrier(spanList, dataCarrier, resultStr, getMatchedSpans(spanList, startIndex, endIndex));
						stringTobeMatched = stringTobeMatched.substring(endIndex);
						stringTobeMatchedInUpperCase = stringTobeMatchedInUpperCase.substring(endIndex);
						location = location + endIndex;
					}
				}
			}
		}
		return dataCarrier;
	}

	/**
	 * Returns the fuzzy match threshold value based on the value passed. If passed value is {@code null} then it retruns 0.0f.
	 * 
	 * @param keyFuzzyness {@link Float} represents the key fuzzyness threshold value for the key pattern.
	 * @return the fuzzy match threshold value for the key pattern.
	 */
	private static float getMatchThersholdValue(final Float keyFuzzyness) {
		float thresholdValue = 0.0f;
		if (null != keyFuzzyness) {
			thresholdValue = keyFuzzyness.floatValue();
		}
		return thresholdValue;
	}

	/**
	 * Adds the {@link OutputDataCarrier} to the {@link List} of {@link OutputDataCarrier} and sets its {@code confidence} to 100 if
	 * {@code matchedSpan} and {@code resultStr} is not {@code null}.
	 * 
	 * @param dataCarrierList represents the {@link List} of data structure {@link LineDataCarrier}.
	 * @param resultStr represents the matched string.
	 * @param matchedSpan represents the instance of matched {@link Span}.
	 */
	private static void addOutputDataCareer(final List<OutputDataCarrier> dataCarrierList, final String resultStr,
			final Span matchedSpan) {
		if (resultStr != null && matchedSpan != null) {
			final float confidence = CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE;
			final OutputDataCarrier dataCarrier = new OutputDataCarrier(matchedSpan, confidence, resultStr);
			dataCarrierList.add(dataCarrier);
			LOGGER.info(resultStr);
		}
	}

	/**
	 * Gets data carrier object for a set of spans.
	 * 
	 * @param spanList {@link List}<{@link Span}>
	 * @param dataCarrier {@link DataCarrier}
	 * @param resultStr {@link String}
	 * @param matchedSpans {@link List}<{@link Span}>
	 * @return {@link DataCarrier}
	 */
	private static DataCarrier getDataCarrier(final List<Span> spanList, final DataCarrier dataCarrier, final String resultStr,
			final List<Span> matchedSpans) {
		DataCarrier finalDataCarrier = null;
		String matchedSpanString = getMatchedSpansValue(spanList);
		if (!EphesoftStringUtil.isNullOrEmpty(matchedSpanString) && !EphesoftStringUtil.isNullOrEmpty(resultStr)) {
			LOGGER.info(resultStr);
			final float confidence = getConfidence(matchedSpanString, resultStr);
			if ((dataCarrier == null || dataCarrier.getConfidence() > confidence)) {
				final List<Coordinates> matchedSpanCoordinates = getCoordinatesOfSpans(matchedSpans);
				if (null != matchedSpanCoordinates) {
					finalDataCarrier = new DataCarrier(matchedSpans, confidence, resultStr,
							HocrUtil.getRectangleCoordinates(matchedSpanCoordinates));
				}
			}
		}
		return finalDataCarrier;
	}

	/**
	 * Gets collection of coordinates of all input spans.
	 * 
	 * @param spans {@link List}<{@link Span}>
	 * @return {@link List}<{@link Coordinates}>
	 */
	private static List<Coordinates> getCoordinatesOfSpans(final List<Span> spans) {
		List<Coordinates> spansCoordinates = null;
		if (!CollectionUtil.isEmpty(spans)) {
			spansCoordinates = new ArrayList<Coordinates>(spans.size());
			for (Span span : spans) {
				if (span != null) {
					final Coordinates spanCoordinates = span.getCoordinates();
					if (null != spanCoordinates) {
						spansCoordinates.add(spanCoordinates);
					}
				}
			}
		}
		return spansCoordinates;
	}

	/**
	 * Gets confidence of the result string with respect to input string.
	 * 
	 * @param inputString {@link String}
	 * @param resultString {@link String}
	 * @return float Confidence of result string.
	 */
	private static float getConfidence(final String inputString, final String resultString) {
		int confidenceInt = CommonConstants.DEFAULT_MAXIMUM_CONFIDENCE;
		float confidence = 0.0f;
		if (!EphesoftStringUtil.isNullOrEmpty(inputString) && !EphesoftStringUtil.isNullOrEmpty(resultString)) {
			final int inputStrLength = inputString.length();
			final int resultStrLength = resultString.length();
			if (inputStrLength > resultStrLength) {
				confidence = (resultString.length() * confidenceInt) / inputString.length();
			} else {
				confidence = (inputString.length() * confidenceInt) / resultString.length();
			}
		}
		return confidence;

	}

	/**
	 * Gets list of all spans whose values are fall within the start and end index of matched pattern string.
	 * 
	 * @param spanList {@link List}<{@link Span}>
	 * @param startIndex int
	 * @param endIndex int
	 * @return
	 */
	public static List<Span> getMatchedSpans(final List<Span> spanList, final int startIndex, final int endIndex) {
		int spanIndex = 0;
		boolean isFirstSpan = true;
		List<Span> matchedSpans = null;
		LOGGER.debug(EphesoftStringUtil.concatenate("Start index is ", startIndex, " End index is ", endIndex));
		if (null != spanList) {
			for (final Span span : spanList) {
				if (null != span && null != span.getValue()) {
					if (spanIndex >= endIndex) {
						break;
					}
					spanIndex = spanIndex + span.getValue().length();

					// For space.
					if (!isFirstSpan) {
						spanIndex = spanIndex + 1;
					}
					if (spanIndex > startIndex) {
						if (null == matchedSpans) {
							matchedSpans = new ArrayList<Span>();
						}
						matchedSpans.add(span);
					}
				}
				isFirstSpan = false;
			}
		}
		return matchedSpans;
	}

	/**
	 * Returns concatenated value string of spans in the list passed to it.
	 * 
	 * @param spanList {@link List}<{@link Span}>
	 * @return {@link String}
	 */
	public static String getMatchedSpansValue(final List<Span> spanList) {
		int spanIndex = 0;
		boolean isFirstSpan = true;
		StringBuilder matchedSpansValue = new StringBuilder();
		if (null != spanList) {
			for (final Span span : spanList) {
				if (null != span && null != span.getValue()) {
					spanIndex = spanIndex + span.getValue().length();
					if (!isFirstSpan) {
						spanIndex = spanIndex + 1;
						matchedSpansValue.append(TableExtractionConstants.SPACE);
					}
					matchedSpansValue.append(span.getValue());
				}
				isFirstSpan = false;
			}
		}
		return matchedSpansValue.toString();
	}
}
