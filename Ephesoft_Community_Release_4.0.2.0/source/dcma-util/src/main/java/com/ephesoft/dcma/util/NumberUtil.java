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

import java.text.DecimalFormat;
import java.util.Random;

import com.ephesoft.dcma.util.constant.CommonConstants;

/**
 * A Utility for performing number operations. This class provides functionality for performing operations on numbers like round up
 * values etc.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
public final class NumberUtil {

	private static String ROUND_UPTO_TWO_DECIMAL_FORMAT = "#.##";

	/**
	 * Rounds off the double value up to 2 decimal digits.
	 * 
	 * example : 12.456 would be rounded off to 12.45, 12.534 would be rounded off to 12.53.
	 * 
	 * @param doubleValue {@link Double} value to be rounded off. If null, returns null.
	 * @return {@link Double} rounded double value.
	 */
	public static final double getRoundedValue(double doubleValue) {
		DecimalFormat decimalFormatter = new DecimalFormat(ROUND_UPTO_TWO_DECIMAL_FORMAT);
		double roundedValue = Double.parseDouble(decimalFormatter.format(doubleValue));
		return roundedValue;
	}

	/**
	 * Compares two integers.
	 * 
	 * @param value1 int
	 * @param value2 int
	 * @return int comparison result
	 */
	public static final int compare(int value1, int value2) {
		int result;
		if (value1 > value2) {
			result = 1;
		} else if (value2 > value1) {
			result = -1;
		} else {
			result = 0;
		}
		return result;
	}

	/**
	 * Checks if a string is a valid long value.
	 * 
	 * @param value {@link String} String for check.
	 * @return True if input is a valid long value.
	 */
	public static final boolean isValidLong(final String value) {
		boolean result = value.matches(CommonConstants.LONG_REGEX);
		return result;
	}

	/**
	 * Checks if a string is a valid priority range group string.
	 * 
	 * @param value {@link String} String for check.
	 * @return True if input is a valid long value.
	 */
	public static final boolean isValidPriorityRange(final String value) {
		boolean result = value.matches(CommonConstants.PRIORITY_RANGE_GROUPS_REGEX);
		return result;
	}

	/**
	 * Checks if a string is a valid positive integer value.
	 * 
	 * @param value {@link String} String for check.
	 * @return True if input is a valid long value.
	 */
	public static final boolean isValidPositiveInteger(final String value) {
		boolean result = value.matches(CommonConstants.POSITIVE_INT_REGEX);
		return result;
	}

	public static final int parseInt(String value) {
		int intValue = 0;
		try {
			intValue = Integer.parseInt(value);
		} catch (NumberFormatException numberFormatException) {

		}
		return intValue;
	}

	public static final Integer getRandomNumber(final int maxInt, final int minInt) {
		Random rand = new Random();
		return rand.nextInt((maxInt - minInt) + 1) + minInt;
	}
}
