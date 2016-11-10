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

package com.ephesoft.gxt.rv.client.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.matheclipse.parser.client.eval.DoubleEvaluator;

import com.ephesoft.dcma.batch.schema.Row;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.constant.ExpressionParameters;
import com.ephesoft.gxt.core.shared.dto.RuleInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;

public final class RowValidator {

	private static final int EXPRESSION_TO_EVALUATE = 2;

	public static boolean isValid(final Row row, final TableInfoDTO tableInfo) {

		boolean isRowValid = true;
		if (null != tableInfo && null != row) {
			final Map<String, String> columnValues = BatchSchemaUtil.getColumnValues(row);
			final String tableRuleOperator = tableInfo.getRuleOperator();
			try {
				if (!StringUtil.isNullOrEmpty(tableRuleOperator)) {
					List<RuleInfoDTO> ruleList = tableInfo.getRuleInfoDTOs();
					ruleList = new ArrayList<RuleInfoDTO>();
					// ruleList.add(new RuleInfoDTO());
					boolean allRules = "AND".equalsIgnoreCase(tableRuleOperator);
					if (!CollectionUtil.isEmpty(ruleList)) {
						boolean ruleValid = false;
						for (RuleInfoDTO tableRule : ruleList) {
							ruleValid = isRuleValid(columnValues, tableRule);
							isRowValid = isRowValid && ruleValid;
							if (allRules) {
								if (!isRowValid) {
									break;
								}
							} else {
								if (ruleValid) {
									isRowValid = true;
									break;
								}
							}
						}
					}
				}
			} catch (Exception exception) {
				isRowValid = false;
			}
			row.setIsRuleValid(!isRowValid);
		}
		return isRowValid;
	}

	private static boolean isRuleValid(final Map<String, String> columnValues, final RuleInfoDTO ruleToValidate) {
		boolean isRuleValid = true;
		if (null != columnValues && null != ruleToValidate) {
			String rule = ruleToValidate.getRule();
			// rule = "&ITEM NO. &+ &ORDERED &<= &PRICE";
			final ExpressionParameters conditionalOperator = ExpressionParameters.getConditionalOperatorInRule(rule);
			final String operandSeperator = ExpressionParameters.OPERAND_SEPERATOR.getRuleExpressionValue();
			if (null != conditionalOperator) {
				String separator = conditionalOperator.getRuleExpressionValue();
				rule = rule.toUpperCase();
				String[] expression = rule.split(separator);
				if (expression.length == EXPRESSION_TO_EVALUATE) {
					String leftExpressionToEvaluate = expression[0];
					String rightExpressionToEvaluate = expression[1];
					String leftExpression = leftExpressionToEvaluate.replaceAll(operandSeperator, CoreCommonConstant.EMPTY_STRING);
					String rightExpression = rightExpressionToEvaluate.replaceAll(operandSeperator, CoreCommonConstant.EMPTY_STRING);
					DoubleEvaluator evaluator = new DoubleEvaluator();
					String leftExpressionForEvaluator = getExpressionToEvaluate(columnValues, leftExpression);
					String rightExpressionForEvaluator = getExpressionToEvaluate(columnValues, rightExpression);
					double leftValue = evaluator.evaluate(leftExpressionForEvaluator);
					double rightValue = evaluator.evaluate(rightExpressionForEvaluator);
					isRuleValid = ExpressionParameters.isValid(leftValue, rightValue, conditionalOperator);
				}
			}
		}
		return isRuleValid;
	}

	private static String getExpressionToEvaluate(final Map<String, String> columnValues, final String expression) {
		Set<Entry<String, String>> columnEntries = columnValues.entrySet();
		String actualExpression = expression;
		String value;
		for (Entry<String, String> entry : columnEntries) {
			if (null != entry) {
				value = entry.getValue();
				actualExpression = actualExpression
						.replaceAll(entry.getKey(), value == null ? CoreCommonConstant.EMPTY_STRING : value);
			}
		}
		return actualExpression;
	}
}
