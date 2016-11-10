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

package com.ephesoft.gxt.core.shared.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.ephesoft.gxt.core.client.i18n.LocaleConstants;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;

/**
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Jul-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class ParseUtil {

	/**
	 * @param value
	 * @param defaultVal
	 * @return
	 */
	public static int parseInt(final String value, final int defaultVal) {
		int parsedVal = defaultVal;
		try {
			parsedVal = Integer.parseInt(value);
		} catch (NumberFormatException numberFormatException) {
			//
		}
		return parsedVal;
	}

	/**
	 * @param value
	 * @return
	 * 
	 * @throws {@link NumberFormatException} if passed String is not a valid Integer.
	 */
	public static int parseInt(final String value) {
		return Integer.parseInt(value);
	}

	/**
	 * Returns the rounded value of given value without removing trailing zeroes.
	 * 
	 * @param value the value to round off.
	 * @return the rounded off value.
	 */
	public static String getRoundedValue(final String value, final int decimalPlaces) {
		String roundValue = null;

		// fix for JIRA bug - 11816. For handling long strings not containing numeric data.
		try {
			if (!StringUtil.isNullOrEmpty(value) && decimalPlaces >= 1) {

				// code changed for rounding off to two decimal places correctly without removing trailing zeroes.
				BigDecimal doubleValue = new BigDecimal(value.trim());
				doubleValue = doubleValue.setScale(decimalPlaces, RoundingMode.HALF_UP);
				roundValue = doubleValue.toString();
			} else {
				roundValue = value;
			}
		} catch (NumberFormatException numberFormatException) {
			roundValue = value;
		}
		return roundValue;
	}

	/**
	 * Returns the rounded value of given value.
	 * 
	 * @param value the value to round off.
	 * @return the rounded off value.
	 */
	public static String getRoundOffValue(final String value, final int decimalPlaces) {
		String roundValue = null;
		if (!StringUtil.isNullOrEmpty(value) && decimalPlaces >= 1) {
			try {
				double num = Math.pow(10, decimalPlaces);
				Double doubleValue = Double.parseDouble(value);
				doubleValue = (Math.round(doubleValue * num)) / num;
				roundValue = String.valueOf(doubleValue);
			} catch (NumberFormatException numberFormatException) {
				roundValue = value;
			}
		} else {
			roundValue = value;
		}
		return roundValue;
	}

	/**
	 * Parses the passed string and returns the corresponding float value.
	 * 
	 * @param value {@link String} value to be parsed.
	 * @param defaultVal default value for the float if input string is null, empty or not a float value.
	 * @return returns the corresponding float value for the string passed.
	 */
	public static float parseFloat(final String value, final float defaultVal) {
		float parsedVal = defaultVal;
		try {
			parsedVal = Float.parseFloat(value);
		} catch (NumberFormatException numberFormatException) {
			parsedVal = defaultVal;
		}
		return parsedVal;
	}

	/**
	 * Returns the {@link BigInteger} value of the {@link Integer} {@code value} passed if the {@code value} passed is not null,
	 * otherwise returns null.
	 * 
	 * @param value {@link Integer} represents the integer value which is to be converted into {@link BigInteger}.
	 * @return the {@link BigInteger} value for the {@code value} passed if the value is not null.
	 */
	public static BigInteger getBigIntegerValue(final Integer value) {
		BigInteger result = null;
		if (value != null) {
			result = BigInteger.valueOf(value);
		}
		return result;
	}

	/**
	 * Covert from bit to readable form in TB, GB, MB, KB.
	 * 
	 * @param value the value in bits
	 * @return the value with units
	 */
	public static String covertFromBit(double value) {
		String data = CoreCommonConstant.EMPTY_STRING;
		double byteUnit = value / 8;
		double kiloByte = byteUnit / 1024.0;
		double megaByte = kiloByte / 1024.0;
		double gigaByte = megaByte / 1024.0;
		double teraByte = gigaByte / 1024.0;
		if (teraByte > 1) {
			value = teraByte;
			data = LocaleConstants.TERABYTE;
		} else if (gigaByte > 1) {
			value = gigaByte;
			data = LocaleConstants.GIGABYTE;
		} else if (megaByte > 1) {
			value = megaByte;
			data = LocaleConstants.MEGABYTE;
		} else if (kiloByte > 1) {
			value = kiloByte;
			data = LocaleConstants.KILOBYTE;
		} else {
			value = byteUnit;
			data = LocaleConstants.BYTE;
		}
		// Negative check for value.
		if (value < 0) {
			value = 0;
		}
		DecimalFormat df = new DecimalFormat("#.##");
		data = df.format(value) + data;
		return data;
	}
}
