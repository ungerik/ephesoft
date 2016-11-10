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

package com.ephesoft.dcma.tablefinder.data;


/**
 * This class is the data carrier for the fields needed for searching table extraction data.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Jun 20, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class SearchTableConfigDataCarrier {

	/**
	 * Constructor of the class to set all necessary values for searching table rows.
	 * 
	 * @param startPattern
	 * @param endPattern
	 * @param fuzzyMatchThresholdValue
	 */
	public SearchTableConfigDataCarrier(final String startPattern, final String endPattern, final float fuzzyMatchThresholdValue) {
		this.startPattern = startPattern;
		this.endPattern = endPattern;
		this.fuzzyMatchThresholdValue = fuzzyMatchThresholdValue;
	}

	/**
	 * Pattern for start of the table.
	 */
	private String startPattern;

	/**
	 * Pattern for end of the table.
	 */
	private String endPattern;

	/**
	 * Threshold value for fuzziness for search of start pattern/end pattern.
	 */
	private float fuzzyMatchThresholdValue;

	/**
	 * Gets start pattern.
	 * 
	 * @return {@link String}
	 */
	public String getStartPattern() {
		return startPattern;
	}

	/**
	 * Sets start pattern.
	 * 
	 * @param startPattern {@link String}
	 */
	public void setStartPattern(final String startPattern) {
		this.startPattern = startPattern;
	}

	/**
	 * Gets end pattern.
	 * 
	 * @return {@link String}
	 */
	public String getEndPattern() {
		return endPattern;
	}

	/**
	 * Sets end pattern.
	 * 
	 * @param endPattern {@link String}
	 */
	public void setEndPattern(final String endPattern) {
		this.endPattern = endPattern;
	}

	/**
	 * Gets the threshold value of the fuzziness for search of start/end patterns.
	 * 
	 * @return float
	 */
	public float getFuzzyMatchThresholdValue() {
		return fuzzyMatchThresholdValue;
	}

	/**
	 * Sets the threshold value of the fuzziness for search of start/end patterns.
	 * 
	 * @param fuzzyMatchThresholdValue float
	 */
	public void setFuzzyMatchThresholdValue(final float fuzzyMatchThresholdValue) {
		this.fuzzyMatchThresholdValue = fuzzyMatchThresholdValue;
	}
}
