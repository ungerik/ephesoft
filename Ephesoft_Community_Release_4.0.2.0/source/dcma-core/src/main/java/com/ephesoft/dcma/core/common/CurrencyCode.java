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

package com.ephesoft.dcma.core.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to represent the currency codes of different countries. These currency codes are used to extract currency values, represent
 * {@link Double} / {@link Long} values in their corressponding currency representation.
 * 
 * @author Ephesoft
 * 
 */
public enum CurrencyCode {

	INDIAN_RUPEE("INR - Indian Rupee", "INR"), US_DOLLAR("USD - US Dollars", "USD"), EUROPEAN_EURO("EUR - European Euro", "EUR"),
	MALAYSIA("MYR - Malaysian ringgit", "MYR"), QATAR("QAR - Qatari riyal", "QAR"), ICELAND("ISK - Icelandic króna", "ISK"),
	SWITZERLAND("CHF - Swiss franc", "CHF"), SAUDI_ARABIA("SAR - Saudi riyal", "SAR"), IRAQ("IQD - Iraqi dinar", "IQD"), CHILE(
			"CLP - Chilean peso", "CLP"), UNITED_KINGDOM("GBP - UK Pound", "GBP"), PANAMA("PAB - Panamanian balboa", "PAB"), YEMEN(
			"YER - Yemeni rial", "YER"), MACEDONIA("MKD - Macedonian denar", "MKD"), CANADIAN_DOLLAR("CAD - Canadian dollar", "CAD"),
	VIETNAM("VND - Vietnamese dong", "VND"), CHINA("CNY - Chinese renminbi", "CNY"), HONDURAS("HNL - Honduran lempira", "HNL"),
	MOROCCO("MAD - Moroccan dirham", "MAD"), INDONESIA("IDR - Indonesian rupiah", "IDR"), SOUTH_AFRICA("ZAR - South African rand",
			"ZAR"), SOUTH_KOREA("KRW - South Korean won", "KRW"), TUNISIA("TND - Tunisian dinar", "TND"), SERBIA(
			"RSD - Serbian dinar", "RSD"), BELARUS("BYR - Belarusian ruble", "BYR"), NEW_TAIWAN("TWD - New Taiwan dollar", "TWD"),
	SUDAN("SDG - Sudanese pound", "SDG"), JAPAN("JPY - Japanese yen", "JPY"), BOLIVIA("BOB - Bolivian boliviano", "BOB"), ALGERIA(
			"DZD - Algerian dinar", "DZD"), ARGENTINA("ARS - Argentine peso", "ARS"), UAE("AED - UAE dirham", "AED"), LITHUANIA(
			"LTL - Lithuanian litas", "LTL"), SYRIA("SYP - Syrian pound", "SYP"), RUSSIA("RUB - Russian ruble", "RUB"), ISRAEL(
			"ILS - Israeli new sheqel", "ILS"), DENMARK("DKK - Danish krone", "DKK"), COSTARICA("CRC - Costa Rican colon", "CRC"),
	HONG_KONG("HKD - Hong Kong dollar", "HKD"), THAILAND("THB - Thai baht", "THB"), UKRAINE("UAH - Ukrainian hryvnia", "UAH"),
	DOMINICIAN_REPUBLIC("DOP - Dominican peso", "DOP"), VENEZUELAN("VEB - Venezuelan bolivar", "VEB"), POLAND("PLN - Polish zloty",
			"PLN"), LIBYA("LYD - Libyan dinar", "LYD"), JORDAN("JOD - Jordanian dinar", "JOD"), HUNGARY("HUF - Hungarian forint",
			"HUF"), GUATEMALA("GTQ - Guatemalan quetzal", "GTQ"), PARAGUAY("PYG - Paraguayan guarani", "PYG"), BULGARIA(
			"BGN - Bulgarian lev", "BGN"), CROATIA("HRK - Croatian kuna", "HRK"), BOSNIA("BAM - Bosnia konvertibilna marka", "BAM"),
	ROMANIA("RON - Romanian leu", "RON"), SINGAPORE("SGD - Singapore Dollar", "SGD"), NICARAGUA("NIO - Nicaraguan cordoba", "NIO"),
	BAHRAIN("BHD - Bahraini dinar", "BHD"), BRAZIL("BRL - Brazilian real", "BRL"), NORWAY("NOK - Norwegian krone", "NOK"), KUWAIT(
			"KWD - Kuwaiti dinar", "KWD"), EGYPT("EGP - Egyptian pound", "EGP"), PERU("PEN - Peruvian nuevo sol", "PEN"),
	CZECH_REPUBLIC("CZK - Czech koruna", "CZK"), TURKEY("TRY - Turkish new lira", "TRY"), URGUAY("UYU - Uruguayan peso", "UYU"), OMAN(
			"OMR - Omani rial", "OMR"), ALBANIA("ALL - Albanian lek", "ALL"), LATVIA("LVL - Latvian lats", "LVL"), SOLAVAKIA(
			"SKK - Slovak koruna", "SKK"), MEXICO("MXN - Mexican peso", "MXN"), AUSTRALIA("AUD - Australian dollar", "AUD"),
	NEW_ZEALAND("NZD - New Zealand dollar", "NZD"), SWEDEN("SEK - Swedish krona", "SEK"), LEBANON("LBP - Lebanese lira", "LBP"),
	COLOMBIA("COP - Colombian peso", "COP"), PHILIPPINES("PHP - Philippine peso", "PHP"), ESTONIA("EEK - Estonian kroon", "EEK");


	/**
	 * Represents the currency Representator which is used to perform currency operations.
	 */
	private String representationValue;

	/**
	 * Represents the abbreviation of the currency.
	 */
	private String abbreviation;
	/**
	 * Represents the map containing all the key-values of currency codes where key is currency code and value is the abbreviation for
	 * the currency..
	 */
	private static Map<String, String> currencyCodeMap = null;

	/**
	 * Initialize the currency with the currency name and the code with which the currency would be processed.
	 * 
	 * @param abbreviation {@link String} Represents the abbreviation of the currency.
	 * @param representationValue {@link String} Represents the currency representator which is used to perform currency operations.
	 */
	private CurrencyCode(final String representationValue,final String abbreviation) {
		this.abbreviation = abbreviation;
		this.representationValue = representationValue;
	}

	/**
	 * Gets the abbreviation corresponding to the currency. eg US Dollars for USD, INR for Indian Rupee.
	 * 
	 * @return {@link String} Abbreviation of the currency.
	 */
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * Gets the currency code which can be used to perform currency operations.
	 * 
	 * @return {@link String} currency code for the corresponding currency.
	 */
	public String getRepresentationValue() {
		return representationValue;
	}

	/**
	 * Returns all the values of Currency code as a map, where <code>mapKey</code> indicates the currencyCode and value indicates
	 * <code>abbreviation</code> to be used for the currency.
	 * 
	 * @return {@link Map} map containing all the values in currency codes list. If there is no value . It returns and empty map.
	 */
	public static Map<String, String> getCurrencyMap() {
		if (currencyCodeMap == null) {
			currencyCodeMap = new HashMap<String, String>();
			CurrencyCode[] codes = CurrencyCode.values();
			if (codes != null) {
				for (CurrencyCode currencyCode : codes) {
					currencyCodeMap.put(currencyCode.getRepresentationValue(), currencyCode.getAbbreviation());
				}
			}
		}
		return currencyCodeMap;
	}

	/**
	 * Returns the CurrencyCode corresponding to the representation of currency.
	 * 
	 * @param representationValue {@link String} representation of currencies eg <br>
	 *            INR for INDIA for Indian Rupee ,USD for US Dollar.
	 * @return {@link CurrencyCode} corresponding to the representation value
	 */
	public static CurrencyCode getCurrencyCodeForRepresentationValue(final String representationValue) {
		CurrencyCode value = null;
		CurrencyCode[] codes = CurrencyCode.values();
		if (representationValue != null && codes != null) {

			for (CurrencyCode currencyCode : codes) {
				if (representationValue.equals(currencyCode.getRepresentationValue())) {
					value = currencyCode;
					break;
				}
			}
		}
		return value;
	}

	/**
	 * Returns the abbreviation corresponding to the representation of currency.
	 * 
	 * @param representationValue {@link String} representation of currencies eg <br>
	 *            INR for INDIA for Indian Rupee ,USD for US Dollar.
	 * @return {@link String} abbreviation corresponding to the representation value.
	 */
	public static String getAbbreviationForRepresentationValue(final String representationValue) {
		String currencyName = null;
		Map<String, String> currencyMap = getCurrencyMap();
		if (null != currencyMap) {
			currencyName = currencyMap.get(representationValue);
		}
		return currencyName;
	}

	@Override
	public String toString() {
		return this.getAbbreviation();
	}

}
