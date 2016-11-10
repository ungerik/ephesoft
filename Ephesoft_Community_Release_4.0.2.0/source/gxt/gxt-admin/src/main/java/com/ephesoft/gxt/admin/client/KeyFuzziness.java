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

package com.ephesoft.gxt.admin.client;

/**
 * Represnts the enumm for the fuzzy match threshold value. This specifies the constant for the defining the fuzzy match threshold
 * value, fuzziness percentage value and index at which fuzzy match threshold option need to be added .
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 29-Jan-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public enum KeyFuzziness {

	/**
	 * Constant used for defining 10 percent fuzziness value.
	 */
	_10_PERCENT_FUZZINESS(1, "10%", 0.1f),

	/**
	 * Constant used for defining 20 percent fuzziness value.
	 */
	_20_PERCENT_FUZZINESS(2, "20%", 0.2f),

	/**
	 * Constant used for defining 30 percent fuzziness value.
	 */
	_30_PERCENT_FUZZINESS(3, "30%", 0.3f);

	/**
	 * percentageFuzzinessValue {@link String} represents the constant for the fuzziness percentage value.
	 */
	private String percentageFuzzinessValue;

	/**
	 * fuzzinessValue represents the fuzzy match threshold value.
	 */
	private float fuzzinessValue;

	/**
	 * index represnts the list index where the fuzzy match threshold value is added in fuzzy match threshold values list.
	 */
	private int index;

	/**
	 * Constructor for the {@code KeyFuzziness} enum.
	 * 
	 * @param index represnts the list index where the fuzzy match threshold value is added in fuzzy match threshold values list.
	 * @param percentageFuzzinessValue {@link String} represents the constant for the fuzziness percentage value.
	 * @param fuzzinessValue represents the fuzzy match threshold value.
	 */
	KeyFuzziness(final int index, final String percentageFuzzinessValue, final float fuzzinessValue) {
		this.percentageFuzzinessValue = percentageFuzzinessValue;
		this.fuzzinessValue = fuzzinessValue;
		this.index = index;
	}

	/**
	 * @return the percentageFuzzinessValue
	 */
	public String getPercentageFuzzinessValue() {
		return percentageFuzzinessValue;
	}

	/**
	 * @return the fuzzineeValue
	 */
	public float getFuzzinessValue() {
		return fuzzinessValue;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

}
