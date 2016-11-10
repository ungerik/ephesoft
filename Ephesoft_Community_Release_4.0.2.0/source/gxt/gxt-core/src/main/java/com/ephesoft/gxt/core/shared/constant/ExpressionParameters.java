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

package com.ephesoft.gxt.core.shared.constant;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.shared.util.NumberUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;

public enum ExpressionParameters {

	OPERAND_SEPERATOR("&"), ARITHMETIC_OPERATOR_PLUS("&+"), ARITHMETIC_OPERATOR_MULTIPLY("&*"), ARITHMETIC_OPERATOR_MINUS("&-"),
	ARITHMETIC_OPERATOR_DIVIDE("&/"), ARITHMETIC_OPERATOR_REMAINDER("&%"), CONDITIONAL_OPERATOR_LESS_THAN_EQUAL("&<="),
	CONDITIONAL_OPERATOR_GREATER_THAN_EQUAL("&>="), CONDITIONAL_OPERATOR_NOT_EQUAL_TO("&!="), CONDITIONAL_OPERATOR_EQUALS_TO("&==");

	private String ruleExpressionValue;

	private ExpressionParameters(final String value) {
		this.ruleExpressionValue = value;
	}

	public String getRuleExpressionValue() {
		return ruleExpressionValue;
	}

	public static ExpressionParameters getConditionalOperatorInRule(final String rule) {
		ExpressionParameters conditionalOperator = null;
		if (!StringUtil.isNullOrEmpty(rule)) {
			if (rule.contains(ExpressionParameters.CONDITIONAL_OPERATOR_EQUALS_TO.ruleExpressionValue)) {
				conditionalOperator = ExpressionParameters.CONDITIONAL_OPERATOR_EQUALS_TO;
			} else if (rule.contains(ExpressionParameters.CONDITIONAL_OPERATOR_NOT_EQUAL_TO.ruleExpressionValue)) {
				conditionalOperator = ExpressionParameters.CONDITIONAL_OPERATOR_NOT_EQUAL_TO;
			} else if (rule.contains(ExpressionParameters.CONDITIONAL_OPERATOR_GREATER_THAN_EQUAL.ruleExpressionValue)) {
				conditionalOperator = ExpressionParameters.CONDITIONAL_OPERATOR_GREATER_THAN_EQUAL;
			} else {
				conditionalOperator = ExpressionParameters.CONDITIONAL_OPERATOR_LESS_THAN_EQUAL;
			}
		}
		return conditionalOperator;
	}

	public static boolean isValid(final double leftValue, final double rightValue, final ExpressionParameters conditionalOperator) {
		boolean isValid = false;
		double precisedLeftValue = NumberUtil.getRoundedValue(leftValue);
		double precisedRightValue = NumberUtil.getRoundedValue(rightValue);
		if (null != conditionalOperator) {
			switch (conditionalOperator) {
				case CONDITIONAL_OPERATOR_EQUALS_TO:
					isValid = precisedLeftValue == precisedRightValue;
					break;
				case CONDITIONAL_OPERATOR_NOT_EQUAL_TO:
					isValid = precisedLeftValue != precisedRightValue;
					break;

				case CONDITIONAL_OPERATOR_GREATER_THAN_EQUAL:
					isValid = precisedLeftValue >= precisedRightValue;
					break;
				case CONDITIONAL_OPERATOR_LESS_THAN_EQUAL:
					isValid = precisedLeftValue <= precisedRightValue;
					break;
				default:
					break;
			}
		}
		return isValid;
	}

	public static boolean endsWithOperator(String stringToCheck) {
		boolean endsWithOperator = false;
		if (!StringUtil.isNullOrEmpty(stringToCheck)) {
			String trimmedString = stringToCheck.trim();
			ExpressionParameters[] parametersArray = ExpressionParameters.values();
			for (ExpressionParameters paramter : parametersArray) {
				if (null != paramter && paramter != ExpressionParameters.OPERAND_SEPERATOR) {
					if (trimmedString.endsWith(paramter.ruleExpressionValue)) {
						endsWithOperator = true;
						break;
					}
				}
			}
		}
		return endsWithOperator;
	}

	public static List<String> getOperators() {
		List<String> list = new ArrayList<String>();
		ExpressionParameters[] parametersArray = ExpressionParameters.values();
		for (ExpressionParameters parameter : parametersArray) {
			if (parameter != ExpressionParameters.OPERAND_SEPERATOR) {
				list.add(parameter.ruleExpressionValue.substring(1));
			}
		}
		return list;
	}
	
	public static List<String> getConditionalOperators()
	{
		List<String> list = new ArrayList<String>();
		list.add(CONDITIONAL_OPERATOR_EQUALS_TO.ruleExpressionValue);
		list.add(CONDITIONAL_OPERATOR_GREATER_THAN_EQUAL.ruleExpressionValue);
		list.add(CONDITIONAL_OPERATOR_LESS_THAN_EQUAL.ruleExpressionValue);
		list.add(CONDITIONAL_OPERATOR_NOT_EQUAL_TO.ruleExpressionValue);
		return list;
	}
}
