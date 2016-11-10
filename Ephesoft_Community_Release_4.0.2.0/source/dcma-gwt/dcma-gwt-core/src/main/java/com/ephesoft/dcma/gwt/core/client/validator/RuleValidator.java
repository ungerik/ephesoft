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

package com.ephesoft.dcma.gwt.core.client.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.dcma.gwt.core.shared.StringUtil;
import com.ephesoft.dcma.gwt.core.shared.common.ExpressionEvaluator;
import com.ephesoft.dcma.gwt.core.shared.constants.CoreCommonConstants;
import com.google.gwt.user.client.ui.HasValue;

/**
 * Defines validator for validating a value with given rules defined for a table.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Nov-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RuleValidator extends TableExtractionValidator {

	/**
	 * Values to validate by applying the rules.
	 */
	private Map<String, ? extends HasValue<String>> _values;

	/**
	 * List of rules to apply on a value.
	 */
	private List<String> _rules;

	/**
	 * Rule operator defined for the table.
	 */
	private String ruleOperatorDefinedForTable;

	/**
	 * Contains all the invalid characters that may occur in a cell value.
	 */
	private String invalidCharacters;

	/**
	 * Contains the operators that may occur in a rule.
	 */
	private String arithmeticOperators;

	/**
	 * Contains the rule violated.
	 */
	private String ruleNotSatisfied = null;

	/**
	 * Constructs the validator for given rules and value.
	 * 
	 * @param tableRules the value to validate
	 * @param suggestBoxes {@link List} list of rules defined for the table
	 * @param ruleOperatorDefinedForTable2
	 */
	public RuleValidator(List<String> tableRules, Map<String, ? extends HasValue<String>> suggestBoxes,
			String ruleOperatorDefinedForTable, String invalidCharacters, String arithmeticOperators) {
		super();
		this._values = suggestBoxes;
		this._rules = tableRules;
		this.ruleOperatorDefinedForTable = ruleOperatorDefinedForTable;
		this.invalidCharacters = invalidCharacters;
		this.arithmeticOperators = arithmeticOperators;
	}

	@Override
	public boolean validate() {

		boolean valid = true;
		// if rule operator is 'and' all the rule must be true to for marking the row valid.
		if (CoreCommonConstants.AND_OPERATOR.equalsIgnoreCase(ruleOperatorDefinedForTable)) {
			for (String rule : _rules) {
				if (!isRuleValid(rule, _values, invalidCharacters)) {
					ruleNotSatisfied = rule;
					valid = false;
					break;
				}
			}

			// if rule operator is 'or' one or more rules must be true to for marking the row valid.
		} else {
			valid = false;
			for (String rule : _rules) {
				if (isRuleValid(rule, _values, invalidCharacters)) {
					valid = true;
					break;
				} else {
					ruleNotSatisfied = rule;
				}
			}
		}
		if (valid) {
			ruleNotSatisfied = null;
		}
		return valid;
	}

	/**
	 * Returns the rule name which is not satisfied.
	 * 
	 * @return {@link String} the rule name.
	 */
	@Override
	public String getRuleOrPatternNotSatisfied() {
		return ruleNotSatisfied;
	}

	/**
	 * Checks if the values in columns are valid with respect to the rules defined for them.
	 * 
	 * @param rule {@link String} the rule to be verified.
	 * @param _values {@link Map} map of cell widgets of the selected row.
	 * @param invalidCharacters {@link String} string containing invalid characters to be removed from cell values.
	 * @return {@link boolean} true if the cell values satisfy the rule or false otherwise.
	 */
	private boolean isRuleValid(String rule, Map<String, ? extends HasValue<String>> _values, String invalidCharacters) {
		boolean isValid = false;
		if (!StringUtil.isNullOrEmpty(rule)) {
			Map<String, Integer> relationalOperators = createRelationalOperatorsMap();
			String selectedOperator = getSelectedRelationalOperator(rule, relationalOperators);

			final int indexOfEqual = rule.indexOf(selectedOperator);
			if (indexOfEqual != -1) {
				final String ruleDefinedForRHS = rule.substring(indexOfEqual + selectedOperator.length(), rule.length());
				final String ruleDefinedForLHS = rule.substring(0, indexOfEqual - 1);
				if (!StringUtil.isNullOrEmpty(ruleDefinedForLHS) && !StringUtil.isNullOrEmpty(ruleDefinedForRHS)) {
					isValid = evaluateExpression(_values, invalidCharacters, relationalOperators, selectedOperator, ruleDefinedForRHS,
							ruleDefinedForLHS);
				}
			}
		}
		return isValid;
	}

	/**
	 * Creates the expressions for left and right hand side as per the rules defined evaluates them and return the result of the
	 * inequality.
	 * 
	 * @param _values {@link Map} Map of column and its corresponding widget in table.
	 * @param invalidCharacters {@link String} invalid characters that may occur in a column cell value.
	 * @param relationalOperators {@link Map} Map of relational inequalities that may occur in a rule.
	 * @param selectedOperator {@link String} inequality used in the rule.
	 * @param ruleDefinedForRHS {@link String} expression on the right hand side of inequality in the rule.
	 * @param ruleDefinedForLHS {@link String} expression on the left hand side of inequality in the rule.
	 * @return {@link boolean} returns true if the right hand side and left hand side value of the rule satisfies the inequality.
	 */
	private boolean evaluateExpression(Map<String, ? extends HasValue<String>> _values, String invalidCharacters,
			Map<String, Integer> relationalOperators, String selectedOperator, final String ruleDefinedForRHS,
			final String ruleDefinedForLHS) {
		boolean invalidValueExists = false;
		boolean isValid = false;
		final String[] columnsInLHS = ruleDefinedForLHS.trim().split(CoreCommonConstants.AMPERSAND);
		final String[] columnsInRHS = ruleDefinedForRHS.trim().split(CoreCommonConstants.AMPERSAND);
		if (columnsInLHS != null && columnsInLHS.length > 0 && columnsInRHS != null && columnsInRHS.length > 0) {
			char[] invalidChars = invalidCharacters.toCharArray();
			String valueLHS = null;
			String valueRHS = null;
			valueLHS = StringUtil.removeCharacters(_values.get(columnsInLHS[1].trim()).getValue(), invalidChars);
			valueRHS = StringUtil.removeCharacters(_values.get(columnsInRHS[1].trim()).getValue(), invalidChars);
			StringBuilder expressionRHS = null;
			StringBuilder expressionLHS = null;
			if (StringUtil.isNullOrEmpty(valueRHS) || StringUtil.isNullOrEmpty(valueLHS) || !isNumeric(valueRHS)
					|| !isNumeric(valueLHS)) {
				invalidValueExists = true;
			} else {
				expressionRHS = new StringBuilder(CoreCommonConstants.ZERO);
				expressionLHS = new StringBuilder(CoreCommonConstants.ZERO);
				expressionRHS.append(valueRHS.trim());
				expressionLHS.append(valueLHS.trim());
			}
			boolean isOperator = false;
			for (int i = 2; i < columnsInRHS.length && !invalidValueExists; i++) {
				if (arithmeticOperators.contains(columnsInRHS[i].trim())) {
					valueRHS = columnsInRHS[i].trim();
					isOperator = true;
				} else {
					valueRHS = StringUtil.removeCharacters(_values.get(columnsInRHS[i].trim()).getValue(), invalidChars);
					invalidValueExists = !isNumeric(valueRHS);
				}
				if (!StringUtil.isNullOrEmpty(valueRHS)) {
					if (isOperator) {
						expressionRHS.append(valueRHS.trim());
					} else {
						expressionRHS.append(StringUtil.concatenate(CoreCommonConstants.ZERO, valueRHS.trim()));
					}
				} else {
					invalidValueExists = true;
					break;
				}
				isOperator = false;
			}
			for (int i = 2; i < columnsInLHS.length && !invalidValueExists; i++) {
				isOperator = false;
				if (arithmeticOperators.contains(columnsInLHS[i].trim())) {
					valueLHS = columnsInLHS[i];
					isOperator = true;
				} else {
					valueLHS = StringUtil.removeCharacters(_values.get(columnsInLHS[i].trim()).getValue(), invalidChars);
					invalidValueExists = !isNumeric(valueLHS);
				}
				if (!StringUtil.isNullOrEmpty(valueLHS)) {
					if (isOperator) {
						expressionLHS.append(valueLHS.trim());
					} else {
						expressionLHS.append(StringUtil.concatenate(CoreCommonConstants.ZERO, valueLHS.trim()));
					}
				} else {
					invalidValueExists = true;
					break;
				}

			}
			if (expressionRHS != null && expressionLHS != null && !StringUtil.isNullOrEmpty(expressionLHS.toString())
					&& !StringUtil.isNullOrEmpty(expressionRHS.toString()) && !invalidValueExists) {
				isValid = checkExpressionsForSelectedInequality(relationalOperators, selectedOperator, expressionRHS, expressionLHS);
			}
		}
		return isValid;
	}

	/**
	 * Checks the passed expressions for relational inequalities like '==','<=','>=','!='.
	 * 
	 * @param relationalOperators {@link Map} map of relational operators.
	 * @param selectedOperator {@link String} selected operator.
	 * @param expressionRHS {@link String} expression on the right hand side of equality.
	 * @param expressionLHS {@link String} expression on the left hand side of equality.
	 * @return {@link boolean} true if the inequality is satisfied, false otherwise.
	 */
	private boolean checkExpressionsForSelectedInequality(Map<String, Integer> relationalOperators, String selectedOperator,
			StringBuilder expressionRHS, StringBuilder expressionLHS) {
		boolean isValid = false;
		ExpressionEvaluator expressionEvaluatorLHS = new ExpressionEvaluator(expressionLHS);
		ExpressionEvaluator expressionEvaluatorRHS = new ExpressionEvaluator(expressionRHS);
		Double resultLHS = expressionEvaluatorLHS.evaluateExpression();
		Double resultRHS = expressionEvaluatorRHS.evaluateExpression();
		if (selectedOperator != null) {
			switch (relationalOperators.get(selectedOperator)) {
				case 0:
					isValid = (resultLHS.doubleValue() == resultRHS.doubleValue());
					break;
				case 1:
					isValid = (resultLHS.doubleValue() != resultRHS.doubleValue());
					break;
				case 2:
					isValid = (resultLHS.doubleValue() >= resultRHS.doubleValue());
					break;
				case 3:
					isValid = (resultLHS.doubleValue() <= resultRHS.doubleValue());
					break;
				default:
					isValid = false;
			}
		}
		return isValid;
	}

	/**
	 * Returns the relational operator used in the rule.
	 * 
	 * @param rule {@link String} the table rule.
	 * @param relationalOperators {@link Map} map of all the relational operators.
	 * @return {@link String} the relational operator used in the rule.
	 */
	private String getSelectedRelationalOperator(String rule, Map<String, Integer> relationalOperators) {
		String selectedOperator = null;
		Set<String> operators = relationalOperators.keySet();
		for (String operator : operators) {
			if (rule.indexOf(operator) != -1) {
				selectedOperator = operator;
			}
		}
		return selectedOperator;
	}

	/**
	 * Creates a map of relational operators that may occur in an expression.
	 * 
	 * @return {@link Map} map of relational operators.
	 */
	private Map<String, Integer> createRelationalOperatorsMap() {
		Map<String, Integer> relationalOperators = new HashMap<String, Integer>();
		relationalOperators.put(CoreCommonConstants.CHAR_EQUAL, 0);
		relationalOperators.put(CoreCommonConstants.NOT_EQUAL, 1);
		relationalOperators.put(CoreCommonConstants.GREATER_OR_EQUAL, 2);
		relationalOperators.put(CoreCommonConstants.LESS_OR_EQUAL, 3);
		return relationalOperators;
	}

	/**
	 * Checks the given string for a numeric value.
	 * 
	 * @param value {@link String} the string to be checked.
	 * @return {@link boolean} true if the value is numeric, false otherwise.
	 */
	private boolean isNumeric(final String value) {
		boolean isValid = true;
		try {
			float floatValue = Float.parseFloat(value);
		} catch (NumberFormatException exception) {
			isValid = false;
		}
		return isValid;
	}
}
