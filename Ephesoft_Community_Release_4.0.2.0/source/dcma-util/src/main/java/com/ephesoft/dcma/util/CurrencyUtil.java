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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Performs the operation on currency values. This Utility bridges the gaps between the currency and the mathematical values of
 * different locales.
 * <p>
 * Currencies can be converted from currency values to the Double value on the basis of their
 * <p>
 * <li>
 * country codes eg USD 12.5 should be extracted to 12.5 .</li>
 * <li>
 * Currency symbols eg $ 12.5 should be 12.5 .</li>
 * <li>
 * Locale eg LOCALE.US would extract currency on the basis of its own symbol . LOCALE.CANADA on its own.</li>
 * </p>
 * Currently it supports 112 currencies including EUROS, USD, INR,etc.
 * 
 * @author Ephesoft
 * 
 */
public final class CurrencyUtil {

	/**
	 * Constant used to extract a
	 */
	private static final char SPACE = ' ';

	/**
	 * Country code extensively used for PARAGUAY as it was showing an exceptional behaviour from java currency locales support.
	 */
	private static final String PARAGUAY_CODE = "PY";

	/**
	 * Country code extensively used for BELARUS as it was showing an exceptional behaviour from java currency locales support.
	 */
	private static final String BELARUS_CODE = "BY";

	/**
	 * Country code extensively used for CHILE as it was showing an exceptional behaviour from java currency locales support.
	 */
	private static final String CHILE_CODE = "CL";

	/**
	 * Country code extensively used for ICELAND as it was showing an exceptional behaviour from java currency locales support.
	 */
	private static final String ICELAND_CODE = "IS";

	/**
	 * Fractional Separator of CHILE,BELARUS,ICELAND,PARAGUAY etc.
	 */
	private static final char COMMA = ',';

	/**
	 * Default fractional separator used in case fractional separator is not fetched.
	 */
	private static final char FULL_STOP = '.';

	/**
	 * Constant used to extract fractional separator corresponding to the locale. We extract the currency symbol just before this
	 * charaacter.
	 */
	private static final String AFTER_FRACTIONAL_SEPERATOR = "2";

	/**
	 * Maps the currency symbol with the Locales.
	 */
	private static Map<String, List<Locale>> localeCurrencySymbolMap;

	/**
	 * Maps the currency codes with the corresponding locales.
	 */
	private static Map<String, List<Locale>> localeCurrencyCodeMap;

	/**
	 * Maps the currency locale with its fractional seperator.
	 */
	private static Map<Locale, Character> localeFractionalSeperatorMap;

	/**
	 * Initialize the locales and the currencies
	 */
	static {
		setLocaleCurrencyMaps();
	}

	/**
	 * Constructor declared so that the object cannot be initialized from outside.
	 */
	private CurrencyUtil() {
		// Do nothing
	}

	public static Double getDoubleValue(final String currencyValue) {
		Double doubleValue = null;
		if (!EphesoftStringUtil.isNullOrEmpty(currencyValue)) {
			final String symbol = getCurrencySymbolOrCode(currencyValue);
			if (!EphesoftStringUtil.isNullOrEmpty(symbol)) {
				doubleValue = getDoubleValue(currencyValue, symbol);
			}
		}
		return doubleValue;
	}

	public static Double getDoubleValue(final String currencyValue, final String symbolOrCode) {
		Double doubleValue = null;
		if (!EphesoftStringUtil.isNullOrEmpty(currencyValue) && !EphesoftStringUtil.isNullOrEmpty(symbolOrCode)) {
			final Locale locale = getLocaleFromCurrency(symbolOrCode);
			if (null != locale) {
				doubleValue = getDoubleValue(currencyValue, locale);
			}
		}
		return doubleValue;
	}

	public static Double getDoubleValue(final String currencyValue, final Locale locale) {
		Double doubleValue = null;
		if (!EphesoftStringUtil.isNullOrEmpty(currencyValue) && null != locale) {
			final Character fractionalSeperator = localeFractionalSeperatorMap.get(locale);
			if (fractionalSeperator != null && 0 != fractionalSeperator) {
				doubleValue = getDoubleValue(currencyValue, fractionalSeperator);
			}
		}
		return doubleValue;
	}

	public static Long getLongValue(final String currencyValue) {
		Long longValue = null;
		if (!EphesoftStringUtil.isNullOrEmpty(currencyValue)) {
			final String symbol = getCurrencySymbolOrCode(currencyValue);
			if (!EphesoftStringUtil.isNullOrEmpty(symbol)) {
				longValue = getLongValue(currencyValue, symbol);
			}
		}
		return longValue;
	}

	public static Long getLongValue(final String currencyValue, final String symbolOrCode) {
		Long longValue = null;
		if (!EphesoftStringUtil.isNullOrEmpty(currencyValue) && !EphesoftStringUtil.isNullOrEmpty(symbolOrCode)) {
			final Locale locale = getLocaleFromCurrency(symbolOrCode);
			if (null != locale) {
				longValue = getLongValue(currencyValue, locale);
			}
		}
		return longValue;
	}

	public static Long getLongValue(final String currencyValue, final Locale locale) {
		Long longValue = null;
		if (!EphesoftStringUtil.isNullOrEmpty(currencyValue) && null != locale) {
			final char fractionalSeperator = localeFractionalSeperatorMap.get(locale);
			if (0 != fractionalSeperator) {
				longValue = getLongValue(currencyValue, fractionalSeperator);
			}
		}
		return longValue;
	}

	public static String formatCurrency(final Double currencyValue, final Locale locale, final boolean isGroup) {
		String formattedText = null;
		if (null != currencyValue && null != locale) {
			final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
			numberFormat.setGroupingUsed(isGroup);
			formattedText = numberFormat.format(currencyValue);
		}
		return formattedText;
	}

	public static String formatCurrency(final Long currencyValue, final Locale locale, final boolean isGroup) {
		String formattedText = null;
		if (null != currencyValue && null != locale) {
			final NumberFormat numberFormat = NumberFormat.getIntegerInstance(locale);
			numberFormat.setGroupingUsed(isGroup);
			formattedText = numberFormat.format(currencyValue);
		}
		return formattedText;
	}

	private static Double getDoubleValue(final String currencyValue, final char fractionalSeperator) {
		Double doubleValue = null;
		String tempCurrencyValue = currencyValue;
		if (!EphesoftStringUtil.isNullOrEmpty(tempCurrencyValue) && fractionalSeperator != 0) {
			tempCurrencyValue = getDefaultCurrencyValue(tempCurrencyValue, fractionalSeperator);
			if (!EphesoftStringUtil.isNullOrEmpty(tempCurrencyValue)) {
				try {
					doubleValue = Double.parseDouble(tempCurrencyValue);
				} catch (final NumberFormatException numberFormatException) {
					doubleValue = null;
				}
			}
		}
		return doubleValue;
	}

	private static Long getLongValue(final String currencyValue, final char fractionalSeperator) {
		Long longValue = null;
		String tempCurrencyValue = currencyValue;
		if (!EphesoftStringUtil.isNullOrEmpty(tempCurrencyValue) && fractionalSeperator != 0) {
			if (!tempCurrencyValue.contains(String.valueOf(fractionalSeperator))) {
				try {
					longValue = Long.parseLong(removeGroupingCharacters(tempCurrencyValue));
				} catch (NumberFormatException numberFormatException) {
					longValue = null;
				}
			}
		}
		return longValue;
	}

	private static String getDefaultCurrencyValue(final String currencyValue, final char fractionalSeperator) {
		String tempCurrencyValue = currencyValue;
		if (!EphesoftStringUtil.isNullOrEmpty(tempCurrencyValue) && fractionalSeperator != 0) {
			tempCurrencyValue = removeNonFractionalCharacters(tempCurrencyValue, fractionalSeperator);
			if (!EphesoftStringUtil.isNullOrEmpty(tempCurrencyValue)) {
				tempCurrencyValue = convertToDecimalFormat(tempCurrencyValue, fractionalSeperator);
			}
		}
		return tempCurrencyValue;
	}

	private static void setLocaleCurrencyMaps() {
		Currency currency = null;
		NumberFormat numberFormat = null;
		final Locale[] availableLocales = NumberFormat.getAvailableLocales();
		if (null != availableLocales && availableLocales.length > 0) {
			localeCurrencyCodeMap = new HashMap<String, List<Locale>>(availableLocales.length);
			localeCurrencySymbolMap = new HashMap<String, List<Locale>>(availableLocales.length);
			localeFractionalSeperatorMap = new HashMap<Locale, Character>(availableLocales.length);
			String currencyCode = null;
			String currencySymbol = null;
			List<Locale> localeCodeList = null;
			List<Locale> localeSymbolList = null;
			String countryCode = null;
			for (final Locale locale : availableLocales) {
				if (null != locale) {
					numberFormat = NumberFormat.getInstance(locale);
					currency = numberFormat.getCurrency();
					countryCode = locale.getCountry();
					if (!EphesoftStringUtil.isNullOrEmpty(countryCode)) {
						currencyCode = currency.getCurrencyCode();
						currencySymbol = currency.getSymbol(locale);
						localeCodeList = localeCurrencyCodeMap.containsKey(currencyCode) ? localeCurrencyCodeMap.get(currencyCode)
								: new ArrayList<Locale>(1);
						localeSymbolList = localeCurrencySymbolMap.containsKey(currencySymbol) ? localeCurrencySymbolMap
								.get(currencySymbol) : new ArrayList<Locale>(1);
						localeCodeList.add(locale);
						localeSymbolList.add(locale);
						localeCurrencyCodeMap.put(currencyCode, localeCodeList);
						localeCurrencySymbolMap.put(currencySymbol, localeSymbolList);
						localeFractionalSeperatorMap.put(locale, getLocaleFractionalSeperator(locale));
					}
				}
			}
		}
	}

	private static char getLocaleFractionalSeperator(final Locale locale) {
		char fractionalSeperator = 0;
		if (null != locale) {
			final String country = locale.getCountry();
			if (country.equals(ICELAND_CODE) || country.equals(CHILE_CODE) || country.equals(BELARUS_CODE)
					|| country.equals(PARAGUAY_CODE)) {
				fractionalSeperator = COMMA;
			} else {
				final double temp = 1.24;
				final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
				final String result = numberFormat.format(temp);
				final int index = result.indexOf(AFTER_FRACTIONAL_SEPERATOR) - 1;
				fractionalSeperator = (index > 0) ? result.charAt(index) : FULL_STOP;
			}
		}
		return fractionalSeperator;
	}

	private static String removeNonFractionalCharacters(final String currency, final char fractionalSeperator) {
		String currencyValue = null;
		if (!EphesoftStringUtil.isNullOrEmpty(currency) && fractionalSeperator != 0) {
			final StringBuilder currencyBuilder = new StringBuilder();
			final char[] currencyArray = currency.toCharArray();
			for (final char c : currencyArray) {
				if (Character.isDigit(c) || c == fractionalSeperator) {
					currencyBuilder.append(c);
				}
			}
			currencyValue = currencyBuilder.toString();
		}
		return currencyValue;
	}

	private static String removeGroupingCharacters(final String currency) {
		String currencyValue = null;
		if (!EphesoftStringUtil.isNullOrEmpty(currency)) {
			final StringBuilder currencyBuilder = new StringBuilder();
			final char[] currencyArray = currency.toCharArray();
			for (final char c : currencyArray) {
				if (Character.isDigit(c)) {
					currencyBuilder.append(c);
				}
			}
			currencyValue = currencyBuilder.toString();
		}
		return currencyValue;
	}

	private static String getCurrencySymbolOrCode(final String currency) {
		String currencySymbol = null;
		if (!EphesoftStringUtil.isNullOrEmpty(currency)) {
			final char[] currencyArray = currency.trim().toCharArray();
			final int length = currencyArray.length;
			final StringBuilder symbolBuilder = new StringBuilder();
			if (Character.isDigit(currencyArray[0])) {
				for (int i = length - 1; i >= 0; i--) {
					if (!appendSymbol(currencyArray[i], symbolBuilder)) {
						break;
					}
				}
				currencySymbol = symbolBuilder.reverse().toString();
			} else {
				for (int i = 0; i < length; i++) {
					if (!appendSymbol(currencyArray[i], symbolBuilder)) {
						break;
					}
				}
				currencySymbol = symbolBuilder.toString();
			}
		}
		return currencySymbol;
	}

	private static boolean appendSymbol(final char symbolCharacter, final StringBuilder stringBuilder) {
		boolean isSuccess = true;
		if (Character.isDigit(symbolCharacter)) {
			isSuccess = false;
		} else {
			if (symbolCharacter != SPACE) {
				stringBuilder.append(symbolCharacter);
			}
		}
		return isSuccess;
	}

	private static String convertToDecimalFormat(final String currency, final char fractionSeperator) {
		String decimalString = currency;
		if (!EphesoftStringUtil.isNullOrEmpty(currency) && fractionSeperator != FULL_STOP) {
			decimalString = currency.replace(fractionSeperator, FULL_STOP);
		}
		return decimalString;
	}

	private static Locale getLocaleFromCurrency(final String symbolOrCode) {
		Locale locale = null;
		if (!EphesoftStringUtil.isNullOrEmpty(symbolOrCode)) {
			locale = localeCurrencySymbolMap.containsKey(symbolOrCode) ? localeCurrencySymbolMap.get(symbolOrCode).get(0) : null;
			if (null == locale) {
				locale = localeCurrencyCodeMap.containsKey(symbolOrCode) ? localeCurrencyCodeMap.get(symbolOrCode).get(0) : null;
			}
		}
		return locale;
	}
}
