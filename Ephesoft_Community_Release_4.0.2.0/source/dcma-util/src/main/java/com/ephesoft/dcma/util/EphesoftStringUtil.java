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

package com.ephesoft.dcma.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.util.exception.HexDecoderException;

/**
 * Performs methods like concatenate, encode, split, null check, etc. on strings.
 * 
 * @author Ephesoft <b>created on</b> 18-Jun-2013 <br/>
 * @version 1.0 $LastChangedDate: 2014-04-21 09:56:23 +0530 (Mon, 21 Apr 2014) $ <br/>
 *          $LastChangedRevision: 10919 $ <br/>
 */
public final class EphesoftStringUtil {

	/**
	 * String constant for char encoder.
	 */
	private static final String CHAR_ENCODER = "UTF-8";
	/**
	 * String constant for plus character.
	 */
	private static final String CHARACTER_PLUS = "+";
	/**
	 * String constant for encoded value for Plus character.
	 */
	private static final String ENCODED_PLUS = "%20";
	/**
	 * Initialising logger {@link Logger}.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(EphesoftStringUtil.class);
	/**
	 * String constant for single comma delimiter.
	 */
	private static final String DELIMITER = ",";

	/**
	 * String constant for true
	 */
	private static final String TRUE = "true";

	/**
	 * String constant for false
	 */
	private static final String FALSE = "false";

	/**
	 * Constructor.
	 */
	private EphesoftStringUtil() {
		super();
	}

	/**
	 * Returns encoded value for a string.
	 * 
	 * @param toEncodeString {@link String} the string to be encoded.
	 * @return {@link String} Encoded String, null if toEncodeString is null.
	 */
	public static String getEncodedString(final String toEncodeString) {
		String encodedString = null;
		if (null != toEncodeString) {
			try {
				encodedString = URLEncoder.encode(toEncodeString, CHAR_ENCODER);
			} catch (final UnsupportedEncodingException encodingException) {
				LOGGER.error(concatenate(toEncodeString, "could not be encoded. ", CHAR_ENCODER, " encoding is not supported : ",
						encodingException));
			}
		}
		return encodedString;
	}

	/**
	 * Returns decoded value for a string.
	 * 
	 * @param toDecodeString {@link String} the string to be decoded.
	 * @return {@link String} Decoded String, null if toDecodeString is null.
	 */
	public static String getDecodedString(final String toDecodeString) {
		String decodedString = null;
		if (null != toDecodeString) {
			try {
				decodedString = URLDecoder.decode(toDecodeString, CHAR_ENCODER);
			} catch (final UnsupportedEncodingException encodingException) {
				LOGGER.error(concatenate(toDecodeString, "could not be decoded. ", CHAR_ENCODER, " decoding is not supported : ",
						encodingException));
			}
		}
		return decodedString;
	}

	/**
	 * Splits input string by comma delimiter.
	 * 
	 * @param inputString {@link String} string to split
	 * @return {@link List<{@link String}>} List of strings, null if inputString is null/empty.
	 */
	public static List<String> convertDelimitedStringToList(final String inputString) {
		List<String> listOfTokens = null;
		if (!isNullOrEmpty(inputString)) {
			final String[] stringArray = inputString.split(DELIMITER);
			listOfTokens = new ArrayList<String>(stringArray.length);
			if (stringArray.length > 0) {
				listOfTokens = Arrays.asList(stringArray);
			}
		}
		return listOfTokens;
	}

	/**
	 * Splits the string passed on the basis of sub String length and returns the array of strings computed by splitting this string.
	 * 
	 * @param inputString {@link String} The string to split.
	 * @param subStringLength {@link String} The length of the subString.
	 * @return {@link String[]} The array of strings computed by splitting this string, null if inputString is null/empty or
	 *         subStringLength is <=0.
	 */
	public static String[] splitString(final String inputString, final int subStringLength) {
		String[] chunks = null;
		if (!isNullOrEmpty(inputString) && subStringLength > 0) {
			final int stringLength = inputString.length();
			final int fullChunks = stringLength / subStringLength;
			boolean lastChunkEmpty = false;
			if (stringLength % subStringLength == 0) {
				lastChunkEmpty = true;
			}
			int size = fullChunks;
			if (!lastChunkEmpty) {
				size++;
			}
			chunks = new String[size];
			int num = 0;
			for (num = 0; num < fullChunks; num++) {
				chunks[num] = inputString.substring(num * subStringLength, num * subStringLength + subStringLength);
			}
			if (!lastChunkEmpty) {
				chunks[num] = inputString.substring(num * subStringLength, stringLength);
			}
		}
		return chunks;
	}

	/**
	 * Concatenates a number of objects together in the form of a String.
	 * 
	 * @param objects {@link Object} Objects for concatenation to form a String.
	 * @return {@link String} concatenated string, null if objects is null.
	 */
	public static String concatenate(final Object... objects) {
		String concatenatedString = null;
		if (null != objects) {
			StringBuilder concatenatedStringBuilder = null;
			for (final Object object : objects) {
				if (null == concatenatedStringBuilder) {
					concatenatedStringBuilder = new StringBuilder();
				}
				if (null != object) {
					concatenatedStringBuilder.append(object);
				}
			}
			concatenatedString = concatenatedStringBuilder.toString();
		}
		return concatenatedString;
	}

	/**
	 * Concatenates a number of Strings together in the form of a String.
	 * 
	 * @param strings {@link String} Strings for concatenation.
	 * @return {@link String} concatenated string, null if strings is null.
	 */
	public static String concatenate(final String... strings) {
		String concatenatedString = null;
		if (null != strings) {
			StringBuilder concatenatedStringBuilder = null;
			for (final String string : strings) {
				if (null == concatenatedStringBuilder) {
					concatenatedStringBuilder = new StringBuilder();
				}
				if (null != string) {
					concatenatedStringBuilder.append(string);
				}
			}
			concatenatedString = concatenatedStringBuilder.toString();
		}
		return concatenatedString;
	}

	/**
	 * Encodes a string using URLEncoder.
	 * 
	 * @param inputString {@link String} the string for encoding.
	 * @return {@link String} Encoded String, null if encoding fails due to inputString being empty/null or encoder not able to encode.
	 */
	public static String encode(final String inputString) {
		String encodedString = getEncodedString(inputString);
		if (null == encodedString) {
			encodedString = inputString;
		} else {
			encodedString = encodedString.replace(CHARACTER_PLUS, ENCODED_PLUS);
		}
		return encodedString;
	}

	/**
	 * Checks if a string is null or empty.
	 * 
	 * @param paramToCheck {@link String} String to check for empty or null
	 * @return boolean, true if paramToCheck is null or empty.
	 */
	public static boolean isNullOrEmpty(final String paramToCheck) {
		return (null == paramToCheck || paramToCheck.isEmpty());
	}

	/**
	 * Returns array of string separated by the given split pattern. In case the split pattern is empty, then the input string will be
	 * split character-wise.
	 * 
	 * @param inputString {@link String} the input string that is to be split.
	 * @param splitPattern {@link String} the parameter on which input string should be split.
	 * @return {@link String[]} The array of strings computed by splitting this string, null if inputString being empty/null or
	 *         splitPattern is null.
	 */
	public static String[] splitString(final String inputString, final String splitPattern) {
		String[] tokens = null;
		if (!isNullOrEmpty(inputString) && null != splitPattern) {
			final Pattern pattern = Pattern.compile(splitPattern);
			tokens = pattern.split(inputString);
		}
		return tokens;
	}

	/**
	 * Checks if the input string is valid boolean value i.e. is <b>TRUE</b> or <b>FALSE</b>
	 * 
	 * @param value {@link String} is the string provided
	 * @return <b>TRUE</b> if is a valid boolean value and <b>FALSE</b> if not
	 */
	public static boolean isValidBooleanValue(final String value) {
		boolean isValidBooleanValue = false;
		if ((!isNullOrEmpty(value)) && (value.equalsIgnoreCase(TRUE) || value.equalsIgnoreCase(FALSE))) {
			isValidBooleanValue = true;
		}
		return isValidBooleanValue;
	}

	/**
	 * Checks case insensitively if toBeChecked parameter is contained in original string or not.
	 * 
	 * @param original {@link String}
	 * @param toBeChecked {@link String}
	 * @return boolean True if toBeChecked string is contained in original string when checked with case insensitivity.
	 */
	public static boolean containsIgnoreCase(final String original, final String toBeChecked) {
		boolean doesContain;
		if (null != original && null != toBeChecked) {
			doesContain = StringUtils.containsIgnoreCase(original, toBeChecked);
		} else {
			doesContain = false;
		}
		return doesContain;
	}

	/**
	 * Joins the collection values passed as a parameter separated by the delimiter passed as a parameter.
	 * 
	 * @param collection {@link Collection} collection whose values needs to be joined.
	 * @param separator delimiter to be used as a separator between values.
	 * @return {@link String} joined string.
	 */
	public static <E> String join(final Collection<E> collection, final char separator) {
		String joinString = null;
		if (!CollectionUtil.isEmpty(collection) && 0 != separator) {
			joinString = StringUtils.join(collection.toArray(), separator);
		}
		return joinString;
	}

	/**
	 * Encodes the String value to its corresponding Hex representation.
	 * 
	 * @param originalData byte[] data to be encoded.
	 * @return {@link String} Hex representation of the String.
	 */
	public static String toHexString(final byte[] originalData) {
		String encodedString = null;
		if (originalData != null) {
			char[] encodedCharacters = Hex.encodeHex(originalData);
			encodedString = new String(encodedCharacters);
		}
		return encodedString;
	}

	/**
	 * Decodes the hex String to its original String representation.
	 * 
	 * @param hexString {@link String} encoded hex string representation of a String value.
	 * @return {@link String} decoded String which is the UTF-8 representation of the Hex value.
	 * @throws HexDecoderException when hex string is invalid and cannot be decoded to original String.
	 */
	public static byte[] decodeHexString(final String hexString) throws HexDecoderException {
		byte[] decodedValue = null;
		try {
			if (!isNullOrEmpty(hexString)) {
				decodedValue = Hex.decodeHex(hexString.toCharArray());
			}
		} catch (DecoderException decoderException) {
			throw new HexDecoderException(concatenate("Could not decode the hexvalue ", hexString), decoderException);
		}
		return decodedValue;
	}

	/**
	 * Concatenates the {@link List} of {@link String} using the separator. Final result is the concatenated list of Strings in each
	 * separated by a <code>separator</code>
	 * 
	 * @param objectsToConcatenate []{@link Object} of Strings to concatenate.
	 * @param separator {@link String} separator to separate the two {@link String} in the final String.
	 * @return {@link String} concatenated {@link String} each separated by the <code>separator</code>.
	 */
	public static String concatenateUsingSeperator(String separator, Object... objectsToConcatenate) {
		String concatenatedString = null;
		if (objectsToConcatenate != null && !isNullOrEmpty(separator)) {
			StringBuilder concatenationBuilder = new StringBuilder();
			int listSize = objectsToConcatenate.length;
			for (int listIndex = 0; listIndex < listSize; listIndex++) {
				if (objectsToConcatenate[listIndex] != null) {
					concatenationBuilder.append(objectsToConcatenate[listIndex].toString());
					if (listIndex != listSize - 1) {
						concatenationBuilder.append(separator);
					}
				}
			}
			concatenatedString = concatenationBuilder.toString();
		}
		return concatenatedString;
	}
	
	/**
	 * Replace if contains oldString with newString in String Builder object.
	 *
	 * @param builder String on which replacement needs to be performed.
	 * @param oldString the old string pattern
	 * @param newString the new string pattern
	 */
	public static void replaceIfContains(StringBuilder builder, final String oldString, final String newString) {
		String tempString = builder.toString();
		if (tempString.contains(oldString)) {
			tempString = tempString.replaceAll(oldString, newString);
			builder = builder.replace(0, builder.length(), tempString);
		}
	}
}
