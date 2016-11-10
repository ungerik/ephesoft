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

import java.util.List;
import java.util.regex.Pattern;

import com.ephesoft.gxt.core.shared.constant.ExpressionParameters;

/**
 * Utility class required during validation
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jan 13, 2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public final class ValidationUtil {

	// Suppresses default constructor, ensuring non-instantiability.
	private ValidationUtil() {
	}

	/**
	 * Validates the input so as to fit within the prescribed range. For Eg. confidence should lie between 0 to 100%
	 * 
	 * @param value {@link Float} entered by the user.
	 * @param minimumValue {@link Float} prescribed starting value for variable.
	 * @param maximumValue {@link Float} prescribed ending value for variable.
	 * @return <code>TRUE</code> if is in Range and <code>FALSE</code> if not.
	 */
	public static boolean isValueInRange(final Float value, final Float minimumValue, final Float maximumValue) {
		boolean isValueInRange = false;
		if (value != null && minimumValue != null && maximumValue != null) {
			isValueInRange = (value >= minimumValue && value <= maximumValue) ? true : false;
		}
		return isValueInRange;
	}

	/**
	 * Validates the input so as to fit within the prescribed range. For Eg. confidence should lie between 0 to 100%
	 * 
	 * @param value {@link Double} entered by the user.
	 * @param minimumValue {@link Double} prescribed starting value for variable.
	 * @param maximumValue {@link Double} prescribed ending value for variable.
	 * @return <code>TRUE</code> if is in Range and <code>FALSE</code> if not.
	 */
	public static boolean isValueInRange(final Double value, final Double minimumValue, final Double maximumValue) {
		boolean isValueInRange = false;
		if (value != null && minimumValue != null && maximumValue != null) {
			isValueInRange = (value >= minimumValue && value <= maximumValue) ? true : false;
		}
		return isValueInRange;
	}

	/**
	 * Checks if the provided String input can be parsed into a valid number.
	 * 
	 * @param value {@link String} numeric input provided from user in form of String.
	 * @param isClass {@link Class} is the class against which values is to be verified.
	 * @return <code>TRUE</code> if valid numerical value and <code>FALSE</code> if not.
	 */
	public static boolean isValidNumericalValue(final String value, final Class<?> isClass) {
		boolean isValidNumber = false;
		if (!StringUtil.isNullOrEmpty(value)) {
			try {
				if (isClass == Float.class) {
					Float.valueOf(value);
				}
				if (isClass == Double.class) {
					Double.valueOf(value);
				}
				if (isClass == Integer.class) {
					Integer.valueOf(value);
				}
				if(isClass == Long.class){
					Long.valueOf(value);
				}
				isValidNumber = true;
			} catch (final NumberFormatException exception) {
				isValidNumber = false;
			}
		}
		return isValidNumber;
	}

	/**
	 * Checks if the input is a valid positive numerical value.
	 * 
	 * @param value {@link String} numeric input provided from user in form of String.
	 * @param isClass {@link Class} is the class against which values is to be verified.
	 * @return <code>TRUE</code> if valid positive numerical value and <code>FALSE</code> if not.
	 */
	public static boolean isValidPositiveValue(final String value, final Class<?> isClass) {
		boolean isPositiveNumber = false;
		// Check if it is a valid number.
		if (isValidNumericalValue(value, isClass)) {
			if (Integer.valueOf(value) > 0) {
				isPositiveNumber = true;
			}
		}
		return isPositiveNumber;
	}

	/**
	 * Checks if the input is a valid non negative numerical value.
	 * 
	 * @param value {@link String} numeric input provided from user in form of String.
	 * @param isClass {@link Class} is the class against which values is to be verified.
	 * @return <code>TRUE</code> if valid non negative numerical value and <code>FALSE</code> if not.
	 */
	public static boolean isValidNonNegativeNumericalValue(final String value, final Class<?> isClass) {
		boolean isValidNumber = false;
		if (!StringUtil.isNullOrEmpty(value)) {
			try {
				if ((isClass == Float.class && Float.compare(Float.parseFloat(value), 0.00f) >= 0)
						|| (isClass == Double.class && Double.compare(Double.parseDouble(value), 0.00) >= 0)
						|| (isClass == Integer.class)) {
					isValidNumber = true;
				}
			} catch (final NumberFormatException exception) {
				isValidNumber = false;
			}
		}
		return isValidNumber;
	}

	/**
	 * Checks if the input is a valid positive numerical value in the given range.
	 * 
	 * @param value {@link String} numeric input provided from user in form of String.
	 * @param isClass {@link Class} is the class against which values is to be verified.
	 * @return <code>TRUE</code> if valid positive numerical value and <code>FALSE</code> if not.
	 */
	public static boolean isValidPositiveRangeValue(String value, final Class<?> isClass, int minValue, int maxValue) {
		// Check if it is a valid number.
		boolean isPositiveNumber = false;
		if (isValidNumericalValue(value, isClass)) {
			Integer number = Integer.valueOf(value);
			if (number > 0 && number >= minValue && number <= maxValue) {
				isPositiveNumber = true;
			}
		}
		return isPositiveNumber;
	}

	public static boolean isRegexPatternValid(String regexPattern) {
		return true;
	}

	public static boolean isValidRule(final String ruleDefinedForTable) {
		boolean isValid = false;
		String selectedOperator = null;
		int index = 0;
		List<String> relationalOperators = ExpressionParameters.getConditionalOperators();
		try {
			if (!CollectionUtil.isEmpty(relationalOperators) && !StringUtil.isNullOrEmpty(ruleDefinedForTable)) {
				for (String operator : relationalOperators) {
					if (ruleDefinedForTable.indexOf(operator) != -1) {
						selectedOperator = operator;
						isValid = true;
						relationalOperators.remove(index);
						break;
					}
					index++;
				}

				if (selectedOperator != null) {
					final int indexOfRuleDefined = ruleDefinedForTable.indexOf(selectedOperator);
					for (String operator : relationalOperators) {
						if (ruleDefinedForTable.indexOf(operator) != -1) {
							isValid = false;
							break;
						}
					}
					if (isValid) {
						final String ruleDefinedRHS = ruleDefinedForTable.substring(indexOfRuleDefined + selectedOperator.length(),
								ruleDefinedForTable.length());
						final String ruleDefinedLHS = ruleDefinedForTable.substring(0, indexOfRuleDefined - 1);
						if (StringUtil.isNullOrEmpty(ruleDefinedLHS) || StringUtil.isNullOrEmpty(ruleDefinedRHS)
								|| (ruleDefinedRHS.indexOf(selectedOperator) != -1)) {
							isValid = false;
						} else if (!StringUtil.isNullOrEmpty(ruleDefinedLHS) && !StringUtil.isNullOrEmpty(ruleDefinedRHS)) {
							final String[] columnsRHS = ruleDefinedForTable.split(ExpressionParameters.OPERAND_SEPERATOR
									.getRuleExpressionValue());
							final String[] columnsLHS = ruleDefinedForTable.split(ExpressionParameters.OPERAND_SEPERATOR
									.getRuleExpressionValue());
							if (columnsRHS != null && columnsRHS.length % 2 == 0 && columnsLHS != null && columnsLHS.length % 2 == 0) {
								isValid = true;
							}
						}
					}

				}
			}
		} catch (Exception e) {
			isValid = false;
		}
		return isValid;
	}
	public static boolean isValueInRange(final Integer value, final Integer minimumValue, final Integer maximumValue) {
		boolean isValueInRange = false;
		if (value != null && minimumValue != null && maximumValue != null) {
			isValueInRange = (value >= minimumValue && value <= maximumValue) ? true : false;
		}
		return isValueInRange;
	}

}
