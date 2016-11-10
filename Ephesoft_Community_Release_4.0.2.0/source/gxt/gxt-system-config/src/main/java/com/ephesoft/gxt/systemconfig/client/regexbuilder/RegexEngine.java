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

package com.ephesoft.gxt.systemconfig.client.regexbuilder;

import java.util.List;

import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.regexbuilder.DTO.RegexBuilderDetailsDTO;

/**
 * 
 * Generates the regex pattern based on the input provided. This class is having the methods which contains the logic for the
 * generation of the Regex Pattern.
 * 
 * <p>
 * This class also contains some utility methods.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see RegexBuilderDetailsDTO
 */
public final class RegexEngine {

	// Suppresses default constructor, ensuring non-instantiability.
	private RegexEngine() {
	}

	/**
	 * Generates the regex pattern based on the input provided and returns the regex pattern generated.
	 * 
	 * @param listDetailsDTOs {@link List <{@link RegexBuilderDetailsDTO} The list of RegexBuilderDetailsDTO.
	 * @return {@link String} The regex pattern generated.
	 */
	public static String generateRegexPattern(final List<RegexBuilderDetailsDTO> listDetailsDTOs) {
		StringBuffer finalResult = null;
		if (!CollectionUtil.isEmpty(listDetailsDTOs)) {
			finalResult = new StringBuffer();
			final StringBuffer regexString = new StringBuffer();
			for (RegexBuilderDetailsDTO regexBuilderDetailsDTO : listDetailsDTOs) {
				generateRegexForFieldAndGroupDetails(regexBuilderDetailsDTO, regexString);
			}
			RegexBuilderDetailsDTO regexBuilderDetailsDTO = listDetailsDTOs.get(0);
			if (regexBuilderDetailsDTO != null) {
				RegexFieldDetails regexFieldDetails = regexBuilderDetailsDTO.getRegexFieldDetails();
				if (regexFieldDetails.isStringStartsWith()) {
					finalResult.append(RegexBuilderConstants.STARTS_WITH_REGEX_STRING);
					finalResult.append(regexString);
				} else if (regexFieldDetails.isStringEndsIn()) {
					finalResult.append(regexString);
					finalResult.append(RegexBuilderConstants.ENDS_WITH_REGEX_STRING);
				} else if (regexFieldDetails.isStringStartsWithAndEndsIn()) {
					finalResult.append(RegexBuilderConstants.STARTS_WITH_REGEX_STRING);
					finalResult.append(regexString);
					finalResult.append(RegexBuilderConstants.ENDS_WITH_REGEX_STRING);
				} else {
					finalResult.append(regexString);
				}
			}
		}

		return finalResult.toString();
	}

	/**
	 * Generates the regex for the field values and group options provided.
	 * 
	 * @param regexBuilderDetailsDTO {@link RegexQuantifierDetails} The instance of regexQuantifierDetailsDTO.
	 * @param regexString {@link StringBuffer} The regex pattern generated.
	 */
	private static void generateRegexForFieldAndGroupDetails(final RegexBuilderDetailsDTO regexBuilderDetailsDTO,
			final StringBuffer regexString) {
		String tempString = SystemConfigConstants.EMPTY_STRING;
		RegexGroupDetails regexGroupDetails = null;
		RegexFieldDetails regexFieldDetails = null;
		RegexQuantifierDetails regexQuantifierDetails = null;
		if (regexBuilderDetailsDTO != null) {
			regexGroupDetails = regexBuilderDetailsDTO.getRegexGroupDetails();
			regexFieldDetails = regexBuilderDetailsDTO.getRegexFieldDetails();
			if (regexGroupDetails != null && regexFieldDetails != null) {
				if ((regexFieldDetails.isOrOperator() || regexFieldDetails.isFollowedBy() || regexFieldDetails.isEndsIn()
						|| regexFieldDetails.isStartsWithWordBoundary() || regexFieldDetails.isStartsWithEndsInWordBoundary())) {
					regexString.append(RegexBuilderConstants.WORD_BOUNDARY_REGEX_STRING);
				}
				if (!(regexFieldDetails.isOnlyIfProceedBy() || regexFieldDetails.isOnlyIfNotProceedBy())
						&& (regexGroupDetails.isCaseInsensetive())) {
					regexString.append(RegexBuilderConstants.CASE_INSENSETIVE_REGEX_STRING);
				}
				if (!(regexFieldDetails.isOnlyIfProceedBy() || regexFieldDetails.isOnlyIfNotProceedBy())
						&& (regexGroupDetails.isStartGroup() || regexGroupDetails.isCaptureGroup())
						&& !regexGroupDetails.isNonCaptureGroup()) {
					regexString.append(RegexBuilderConstants.START_BRACKET_REGEX_STRING);
				}
				if (!(regexFieldDetails.isOnlyIfProceedBy() || regexFieldDetails.isOnlyIfNotProceedBy())
						&& (regexGroupDetails.isNonCaptureGroup())) {
					regexString.append(RegexBuilderConstants.NON_CAPTURE_GROUP_REGEX_STRING);
				}
				if (regexFieldDetails.isOrOperator()) {
					regexString.append(RegexBuilderConstants.OR_OPERATOR_REGEX_STRING);
				}
				if (regexFieldDetails.isOnlyIfFollowedBy()) {
					regexString.append(RegexBuilderConstants.IS_FOLLOWED_BY_REGEX_STRING);
				}
				if (regexFieldDetails.isOnlyIfNotFollowedBy()) {
					regexString.append(RegexBuilderConstants.IS_NOT_FOLLOWED_BY_REGEX_STRING);
				}
				if (regexFieldDetails.isOnlyIfProceedBy()) {
					tempString = regexString.toString();
					regexString.delete(0, regexString.length());
					if (regexGroupDetails.isStartGroup() || regexGroupDetails.isCaptureGroup()) {
						regexString.append(RegexBuilderConstants.START_BRACKET_REGEX_STRING);
					}
					if (regexGroupDetails.isNonCaptureGroup()) {
						regexString.append(RegexBuilderConstants.NON_CAPTURE_GROUP_REGEX_STRING);
					}
					if (regexGroupDetails.isCaseInsensetive()) {
						regexString.append(RegexBuilderConstants.CASE_INSENSETIVE_REGEX_STRING);
					}
					regexString.append(RegexBuilderConstants.IS_PRECEDED_BY_REGEX_STRING);
				}
				if (regexFieldDetails.isOnlyIfNotProceedBy()) {
					tempString = regexString.toString();
					regexString.delete(0, regexString.length());
					if (regexGroupDetails.isStartGroup() || regexGroupDetails.isCaptureGroup()) {
						regexString.append(RegexBuilderConstants.START_BRACKET_REGEX_STRING);
					}
					if (regexGroupDetails.isNonCaptureGroup()) {
						regexString.append(RegexBuilderConstants.NON_CAPTURE_GROUP_REGEX_STRING);
					}
					if (regexGroupDetails.isCaseInsensetive()) {
						regexString.append(RegexBuilderConstants.CASE_INSENSETIVE_REGEX_STRING);
					}
					regexString.append(RegexBuilderConstants.IS_NOT_PRECEDED_BY_REGEX_STRING);
				}
				if ((regexFieldDetails.isOnlyIfFollowedBy() || regexFieldDetails.isOnlyIfNotFollowedBy()
						|| regexFieldDetails.isOnlyIfProceedBy() || regexFieldDetails.isOnlyIfNotProceedBy())
						&& (regexFieldDetails.isStartsWithWordBoundary() || regexFieldDetails.isStartsWithEndsInWordBoundary())) {
					regexString.append(RegexBuilderConstants.WORD_BOUNDARY_REGEX_STRING);
				}
				if (!StringUtil.isNullOrEmpty(regexFieldDetails.getFieldValue())) {
					regexString.append(regexFieldDetails.getFieldValue());
				}

				regexQuantifierDetails = regexBuilderDetailsDTO.getRegexQuantifierDetails();
				if (!regexGroupDetails.isRegexQuantifierAppliedToEntireGroup()) {
					generateRegexForQuantifierDetails(regexQuantifierDetails, regexString);
				}

				if (!(regexFieldDetails.isOnlyIfFollowedBy() || regexFieldDetails.isOnlyIfNotFollowedBy()
						|| regexFieldDetails.isOnlyIfProceedBy() || regexFieldDetails.isOnlyIfNotProceedBy())
						&& (regexGroupDetails.isEndGroup() || regexGroupDetails.isCaptureGroup() || regexGroupDetails
								.isNonCaptureGroup())) {
					regexString.append(RegexBuilderConstants.END_BRACKET_REGEX_STRING);
				}

				if ((regexFieldDetails.isOnlyIfFollowedBy() || regexFieldDetails.isOnlyIfNotFollowedBy()
						|| regexFieldDetails.isOnlyIfProceedBy() || regexFieldDetails.isOnlyIfNotProceedBy())
						&& (regexGroupDetails.isEndGroup() || regexGroupDetails.isCaptureGroup() || regexGroupDetails
								.isNonCaptureGroup())) {
					regexString.append(RegexBuilderConstants.END_BRACKET_REGEX_STRING);
				}

				if (regexGroupDetails.isRegexQuantifierAppliedToEntireGroup()) {
					generateRegexForQuantifierDetails(regexQuantifierDetails, regexString);
				}

				if (regexFieldDetails.isOnlyIfFollowedBy() || regexFieldDetails.isOnlyIfNotFollowedBy()
						|| regexFieldDetails.isOnlyIfProceedBy() || regexFieldDetails.isOnlyIfNotProceedBy()) {
					if (regexFieldDetails.isEndsInWordBoundary() || regexFieldDetails.isStartsWithEndsInWordBoundary()) {
						regexString.append(RegexBuilderConstants.WORD_BOUNDARY_REGEX_STRING);
					}
					regexString.append(RegexBuilderConstants.END_BRACKET_REGEX_STRING);

					if (regexFieldDetails.isOnlyIfProceedBy() || regexFieldDetails.isOnlyIfNotProceedBy()) {
						regexString.append(tempString);
					}
				}
				if (regexFieldDetails.isEndsIn()) {
					regexString.append(RegexBuilderConstants.ENDS_WITH_REGEX_STRING);
				}
				if ((regexFieldDetails.isOrOperator() || regexFieldDetails.isFollowedBy() || regexFieldDetails.isEndsIn())
						&& (regexFieldDetails.isEndsInWordBoundary() || regexFieldDetails.isStartsWithEndsInWordBoundary())) {
					regexString.append(RegexBuilderConstants.WORD_BOUNDARY_REGEX_STRING);
				}
			}
		}
	}

	/**
	 * Generates the regex for the field quantitative options provided.
	 * 
	 * @param regexQuantifierDetails {@link RegexQuantifierDetails} The instance of regexQuantifierDetails.
	 * @param regexString {@link StringBuffer}.
	 */
	private static void generateRegexForQuantifierDetails(final RegexQuantifierDetails regexQuantifierDetails,
			final StringBuffer regexString) {
		if (regexQuantifierDetails != null) {
			if (regexQuantifierDetails.isAnyNumberOfTimes()) {
				regexString.append(RegexBuilderConstants.ZERO_OR_MORE_TIME_REGEX_STRING);
			}
			if (regexQuantifierDetails.isBetweenZeroAndOneTimes()) {
				regexString.append(RegexBuilderConstants.ZERO_OR_ONE_TIME_REGEX_STRING);
			}
			if (regexQuantifierDetails.isOneOrMoreTimes()) {
				regexString.append(RegexBuilderConstants.ONE_OR_MORE_TIME_REGEX_STRING);
			}

			if (!StringUtil.isNullOrEmpty(regexQuantifierDetails.getNoOfTimes())) {
				regexString.append(RegexBuilderConstants.START_CURLY_BRACES_REGEX_STRING);
				regexString.append(regexQuantifierDetails.getNoOfTimes());
				regexString.append(RegexBuilderConstants.END_CURLY_BRACES_REGEX_STRING);
			}
			if (regexQuantifierDetails.getMinimumNumberOfTimes() != null
					&& !StringUtil.isNullOrEmpty(regexQuantifierDetails.getMaximumNumberOfTimes())
					&& !StringUtil.isNullOrEmpty(regexQuantifierDetails.getMinimumNumberOfTimes())) {
				regexString.append(RegexBuilderConstants.START_CURLY_BRACES_REGEX_STRING);
				regexString.append(regexQuantifierDetails.getMinimumNumberOfTimes());
				regexString.append(SystemConfigConstants.COMMA);
				regexString.append(regexQuantifierDetails.getMaximumNumberOfTimes());
				regexString.append(RegexBuilderConstants.END_CURLY_BRACES_REGEX_STRING);
			}
			if (regexQuantifierDetails.isAsFewAsPossible()) {
				regexString.append(RegexBuilderConstants.LAZY_QUANTIFIER_REGEX_STRING);
			}
		}
	}

	/**
	 * Matches the text with the regex range and if it doesn't matches then it returns false otherwise returns true.
	 * 
	 * @param textValue {@link String} The text which is to validated.
	 * @param rangeRegex {@link String} The regex range. for example a-z,0-9 etc.
	 * @return false if the text value doesn't matches with in the regex range otherwise returns false.
	 */
	public static boolean isValidValueInRange(final String textValue, final String rangeRegex) {
		boolean isValid = true;
		if (!StringUtil.isNullOrEmpty(textValue) && !StringUtil.isNullOrEmpty(rangeRegex) && !textValue.matches(rangeRegex)) {
			isValid = false;
		}
		return isValid;
	}

	/**
	 * Returns false if digit2 is greater than digit1 otherwise returns true.
	 * 
	 * @param digit1 {@link String} The first digit.
	 * @param digit2 {@link String} The second digit.
	 * @return false if digit2 is greater than digit1 otherwise returns true.
	 */
	public static boolean isDigitGreaterThan(final String digit1, final String digit2) {
		boolean isValid = true;
		if (!StringUtil.isNullOrEmpty(digit1) && !StringUtil.isNullOrEmpty(digit2)) {
			try {
				if (Integer.parseInt(digit2) > Integer.parseInt(digit1)) {
					isValid = false;
				}
			} catch (final NumberFormatException numberFormatException) {
				isValid = true;
			}
		}
		return isValid;
	}

	/**
	 * Returns false if str2 is greater than str1 otherwise returns true.
	 * 
	 * @param str1 {@link String} The first string value.
	 * @param str2 {@link String} The second string value.
	 * @param regexRange {@link String} The regex range. for example a-z,0-9 etc.
	 * @return false if str2 is greater than str1 otherwise returns true.
	 */
	public static boolean isCharacterGreaterThan(final String str1, final String str2, final String regexRange) {
		boolean isValid = true;
		if (!StringUtil.isNullOrEmpty(str1) && !StringUtil.isNullOrEmpty(str2) && RegexEngine.isValidValueInRange(str1, regexRange)
				&& RegexEngine.isValidValueInRange(str2, regexRange) && str1.charAt(0) < str2.charAt(0)) {
			isValid = false;
		}
		return isValid;
	}

	/**
	 * Returns false if digit2 is greater than digit1 otherwise returns true.
	 * 
	 * @param digit1 {@link String} The first digit.
	 * @param digit2 {@link String} The second digit.
	 * @param regexRange {@link String} The regex range. for example a-z,0-9 etc.
	 * @return false if digit2 is greater than digit1 otherwise returns true.
	 */
	public static boolean isDigitGreaterThan(final String digit1, final String digit2, final String regexRange) {
		boolean isValid = true;
		try {
			if (!StringUtil.isNullOrEmpty(digit1) && !StringUtil.isNullOrEmpty(digit2)
					&& RegexEngine.isValidValueInRange(digit1, regexRange) && RegexEngine.isValidValueInRange(digit2, regexRange)
					&& (Integer.parseInt(digit2) > Integer.parseInt(digit1))) {
				isValid = false;
			}
		} catch (final NumberFormatException numberFormatException) {
			isValid = false;
		}
		return isValid;
	}

}
