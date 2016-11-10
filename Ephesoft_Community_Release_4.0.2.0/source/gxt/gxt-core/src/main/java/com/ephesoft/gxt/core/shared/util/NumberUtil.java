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

import com.google.gwt.i18n.client.NumberFormat;

/**
 * A Utility for performing number operations. This class provides functionality for performing operations on numbers like round up
 * values etc.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
public final class NumberUtil {

	private static String ROUND_UPTO_TWO_DECIMAL_FORMAT = "0.00";

	/**
	 * Rounds off the double value up to 2 decimal digits.
	 * 
	 * example : 12.456 would be rounded off to 12.45, 12.534 would be rounded off to 12.53.
	 * 
	 * @param doubleValue {@link Double} value to be rounded off. If null, returns null.
	 * @return {@link Double} rounded double value.
	 */
	public static final double getRoundedValue(double doubleValue) {
		return Double.parseDouble(NumberFormat.getFormat(ROUND_UPTO_TWO_DECIMAL_FORMAT).format(doubleValue));
	}

	/**
	 * Rounds off the float value up to 2 decimal digits.
	 * 
	 * example : 12.456 would be rounded off to 12.45, 12.534 would be rounded off to 12.53.
	 * 
	 * @param floatValue {@link Float} value to be rounded off. If null, returns null.
	 * @return {@link Float} rounded float value.
	 */
	public static final float getRoundedValue(float floatValue) {
		return Float.parseFloat(NumberFormat.getFormat(ROUND_UPTO_TWO_DECIMAL_FORMAT).format(floatValue));
	}

	/**
	 * Round up number by using multiple value.
	 * 
	 * @param number the number
	 * @param multiple the multiple
	 * @return the result
	 */
	public static final double roundUpNumberByUsingMultipleValue(double number, double multiple) {
		double result = multiple;
		if (number % multiple == 0) {
			result = number;
		} else if (number % multiple != 0) {
			// If not already multiple of given number
			if (number >= 0) {
				// For positive number
				int division = (int) ((number / multiple) + 1);
				result = division * multiple;
			} else {
				// For negative number
				int division = (int) ((number / multiple));
				result = division * multiple;
			}
		}
		return result;
	}
}
