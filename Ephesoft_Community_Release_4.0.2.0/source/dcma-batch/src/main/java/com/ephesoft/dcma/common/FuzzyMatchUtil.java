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

import java.util.HashMap;
import java.util.Map;

import com.ephesoft.dcma.core.common.constants.CommonConstants;
import com.ephesoft.dcma.kvfinder.KVFinderConstants;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * This class contains the fuzzy matching algorithm for pattern matching. This class contains method which takes text to search, a
 * pattern to search for and an expected location in the text near which to find the pattern, return the location which matches
 * closest. The function will search for the best match based on both the number of character errors between the pattern and the
 * potential match, as well as the distance between the expected location and the potential match.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 17-Jan-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see HashMap
 */
public final class FuzzyMatchUtil {

	/**
	 * LOGGER to print the logging information.
	 */
	private static final EphesoftLogger LOGGER = EphesoftLoggerFactory.getLogger(FuzzyMatchUtil.class);

	// Suppresses default constructor, ensuring non-instantiability.
	private FuzzyMatchUtil() {
	}

	/**
	 * How far to search for a match (0 = exact location, 1000+ = broad match). A match this many characters away from the expected
	 * location will add 1.0 to the score (0.0 is a perfect match).
	 */
	private static final int MATCH_DISTANCE = 1000;

	/**
	 * Represents the maximum no of bits allow for fuzzy match algorithm.
	 */
	private static final int MATCH_MAX_BITS = 32;

	/**
	 * Loacte the best match index of {@code pattern} in the input string near {@link location}. This method returns -1 if no match is
	 * found .
	 * 
	 * @param inputStr {@link String} represents the input string to be matched.
	 * @param pattern {@link String} represents the pattern to which input string to be matched.
	 * @param location represents the location from which string needs to be searched.
	 * @return Best match index or -1.
	 */
	public static int matchInputString(final String inputStr, final String pattern, final int location, final float matchThershold) {
		LOGGER.info("Inside matchInputString method.");
		int matchedLocation = CommonConstants.NO_FUZZY_MATCH_ERROR_CODE;

		// Checking for null inputs.
		if (inputStr == null || pattern == null) {
			LOGGER.error("Either input String or pattern is null.");
		} else if (!validateRange(matchThershold, KVFinderConstants.MINIMUM_FUZZY_THRESHOLD, KVFinderConstants.MAXIMUM_FUZZY_THRESHOLD)) {
			LOGGER.error("Invalid Thershold value.");
		} else {
			int patternLength = pattern.length();
			int inputStrLength = inputStr.length();
			if (patternLength >= MATCH_MAX_BITS) {
				matchedLocation = CommonConstants.PATTERN_LENGTH_FUZZY_MATCH_ERROR_CODE;
			} else {
				matchedLocation = Math.max(0, Math.min(location, inputStrLength));
				
				// Check if there is exact match.
				if (patternLength >= MATCH_MAX_BITS) {
					LOGGER.error("Pattern too long for applying Fuzzy matching.");
				} else if (inputStr.equals(pattern)) {
					matchedLocation = 0;
				} else if (inputStrLength == 0) {
					matchedLocation = -1;
				} else if (!(matchedLocation + patternLength <= inputStrLength && inputStr.substring(matchedLocation,
						matchedLocation + patternLength).equals(pattern))) {
					matchedLocation = matchBitmap(inputStr, pattern, location, matchThershold);
				}
				LOGGER.info("Matched location is :", matchedLocation);
			}
		}
		return matchedLocation;
	}

	/**
	 * Loacte the best match index of {@code pattern} in the input string near {@link location} using the Bitap algorithm. This method
	 * returns -1 if no match is found .
	 * 
	 * @param inputStr {@link String} represents the input string to be matched.
	 * @param pattern {@link String} represents the pattern to which input string to be matched.
	 * @param location represents the location from which string needs to be searched.
	 * @return Best match index or -1.
	 */
	private static int matchBitmap(final String inputStr, final String pattern, final int location, final float matchThershold) {

		// Initialise the alphabet.
		final Map<Character, Integer> characterMap = matchAlphabet(pattern);

		double scoreThreshold = matchThershold;

		int bestLocation = inputStr.indexOf(pattern, location);
		if (bestLocation != -1) {
			scoreThreshold = Math.min(matchBitapScore(0, bestLocation, location, pattern), scoreThreshold);
			bestLocation = inputStr.lastIndexOf(pattern, location + pattern.length());
			if (bestLocation != -1) {
				scoreThreshold = Math.min(matchBitapScore(0, bestLocation, location, pattern), scoreThreshold);
			}
		}

		// Initialise the bit arrays.
		final int matchmask = 1 << (pattern.length() - 1);
		bestLocation = -1;

		int binMin, binMid;
		int binMax = pattern.length() + inputStr.length();
		int[] lastRdArray = new int[0];
		for (int d = 0; d < pattern.length(); d++) {
			binMin = 0;
			binMid = binMax;
			while (binMin < binMid) {
				if (matchBitapScore(d, location + binMid, location, pattern) <= scoreThreshold) {
					binMin = binMid;
				} else {
					binMax = binMid;
				}
				binMid = (binMax - binMin) / 2 + binMin;
			}
			binMax = binMid;
			int start = Math.max(1, location - binMid + 1);
			final int finish = Math.min(location + binMid, inputStr.length()) + pattern.length();

			int[] rdArray = new int[finish + 2];
			rdArray[finish + 1] = (1 << d) - 1;
			for (int index = finish; index >= start; index--) {
				int charMatch;
				if (inputStr.length() <= index - 1 || !characterMap.containsKey(inputStr.charAt(index - 1))) {
					charMatch = 0;
				} else {
					charMatch = characterMap.get(inputStr.charAt(index - 1));
				}
				if (d == 0) {
					rdArray[index] = ((rdArray[index + 1] << 1) | 1) & charMatch;
				} else {
					rdArray[index] = (((rdArray[index + 1] << 1) | 1) & charMatch)
							| (((lastRdArray[index + 1] | lastRdArray[index]) << 1) | 1) | lastRdArray[index + 1];
				}
				if ((rdArray[index] & matchmask) != 0) {
					final double score = matchBitapScore(d, index - 1, location, pattern);
					if (score <= scoreThreshold) {
						scoreThreshold = score;
						bestLocation = index - 1;
						if (bestLocation > location) {
							start = Math.max(1, 2 * location - bestLocation);
						} else {
							break;
						}
					}
				}
			}
			if (matchBitapScore(d + 1, location, location, pattern) > scoreThreshold) {
				break;
			}
			lastRdArray = rdArray;
		}
		return bestLocation;
	}

	/**
	 * Initialises the alphabet for the Bitap algorithm returns the {@link Map} which contains the hash of character locations.
	 * 
	 * @param pattern {@link String} represents the pattern to which input string to be matched.
	 * @return Hash of character locations.
	 */
	private static Map<Character, Integer> matchAlphabet(final String pattern) {
		final Map<Character, Integer> matchCharacterMap = new HashMap<Character, Integer>();
		final char[] charPattern = pattern.toCharArray();
		for (char character : charPattern) {
			matchCharacterMap.put(character, 0);
		}
		int index = 0;
		for (char character : charPattern) {
			matchCharacterMap.put(character, matchCharacterMap.get(character) | (1 << (pattern.length() - index - 1)));
			index++;
		}
		return matchCharacterMap;
	}

	/**
	 * Compute and return the score for a match with e errors and x location.
	 * 
	 * @param noOfErrors Number of errors in match.
	 * @param locationOfMatch Location of match.
	 * @param location Expected location of match.
	 * @param pattern Pattern being sought.
	 * @return Overall score for match (0.0 = good, 1.0 = bad).
	 */
	private static double matchBitapScore(final int noOfErrors, final int locationOfMatch, final int location, final String pattern) {
		final float accuracy = (float) noOfErrors / pattern.length();
		final int proximity = Math.abs(location - locationOfMatch);
		return accuracy + (proximity / (float) MATCH_DISTANCE);
	}

	/**
	 * Validates the {@code value} with the given range and returns {@code true} if value lies with in the range otherwise returns
	 * {@code false}.
	 * 
	 * @param value represents the value to be validated.
	 * @param startRange start range for the value.
	 * @param endRange end range for the value.
	 * @return {@code true} if value lies with in the range otherwise returns {@code false}.
	 */
	public static boolean validateRange(final float value, final float startRange, final float endRange) {
		boolean isValid = false;
		if (value >= startRange && value <= endRange) {
			isValid = true;
		}
		return isValid;
	}
}
