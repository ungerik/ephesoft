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

package com.ephesoft.dcma.tablefinder.constants;

/**
 * This is a common constants file for Table Extraction plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public interface TableExtractionConstants {

	/**
	 * String constant for space.
	 */
	String SPACE = " ";
	/**
	 * String constant for single quotes.
	 */
	String QUOTES = "\"";
	/**
	 * String constant for empty string.
	 */
	String EMPTY = "";
	/**
	 * String constant for semi colon.
	 */
	String SEMI_COLON = ";";
	/**
	 * String constant for not space.
	 */
	String NOT_SPACE = "\\S";
	/**
	 * String constant for dot.
	 */
	String FULL_STOP = ".";

	/**
	 * String constant for not valid.
	 */
	String NOT_VALID = "!";

	/**
	 * String constant for span tag.
	 */
	String SPAN_TAG = "<span";

	/**
	 * CHAR_EQUAL char defined for Equal.
	 */
	public static final String CHAR_EQUAL = "==";

	/**
	 * AMPERSAND_STRING {@link String}.
	 */
	public static final String AMPERSAND_STRING = "&";

	/**
	 * TABLE_RULE_KEY {@link String} is a constant used as a key for hashmap.
	 */
	public static final String TABLE_RULE_KEY = "ruleValid";

	/**
	 * TABLE_RULE_REGEX {@link String} is the regex pattern for the validating the value in the table before applying table rule.
	 */
	public static final String TABLE_RULE_REGEX = "[0-9.]*";

	/**
	 * AND_OPERATOR {@link String} is a constant for operations using "and" operator.
	 */
	String AND_OPERATOR = "and";

	/**
	 * OR_OPERATOR for operations using "or" operator.
	 */
	String OR_OPERATOR = "or";

	/**
	 * String constant for '!='.
	 */
	public static final String NOT_EQUAL = "!=";

	/**
	 * String constant for '>='.
	 */
	public static final String GREATER_OR_EQUAL = ">=";

	/**
	 * String constant for '<='.
	 */
	public static final String LESS_OR_EQUAL = "<=";

	/**
	 * Constant for start index for a list/array.
	 */
	public static final int START_INDEX = 0;

	/**
	 * Constant for invalid index for a list/array.
	 */
	public static final int INVALID_INDEX_VALUE = -1;

	/**
	 * Constant for minimum positive value for a coordinate.
	 */
	public static final int MINIMUM_POSITIVE_COORDINATE_VALUE = 0;

	/**
	 * Constant for single size.
	 */
	public static final int SINGLE_SIZE_VALUE = 1;

	/**
	 * Int constant for comparison result when objects in comparison are equal.
	 */
	public static final int EQUAL_COMPARISON = 0;

	/**
	 * Int constant for comparison result when object being compared is less than object being compared to.
	 */
	public static final int LESS_COMPARISON = -1;

	/**
	 * Int constant for comparison result when object being compared is greater than object being compared to.
	 */
	public static final int GREATER_COMPARISON = 1;

	/**
	 * Int constant for the minimum value of valid values for confidence calculation.
	 */
	public static final int MINIMUM_VALID_VALUES_COUNT = 0;

	/**
	 * Minimum confidence value.
	 */
	public static final float MINIMUM_CONFIDENCE = 0;

	/** Constant for displaying error message for invalid data for a table column, i.e data which is not numeric in nature. */
	public static final String INVALID_DATA_ERROR_MSG = "data_ocrd_not_numeric_error";
	
	public static final String pageIdentifier = "PG";

}
