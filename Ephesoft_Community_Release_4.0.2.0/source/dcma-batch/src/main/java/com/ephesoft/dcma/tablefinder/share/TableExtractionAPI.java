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

package com.ephesoft.dcma.tablefinder.share;

/**
 * Class contains the table extraction algorithm API information.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Apr 8, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class TableExtractionAPI {

	/**
	 * {@link String} extraction API string from input.
	 */
	private String tableExtractionAPI;

	/**
	 * boolean True if regex validation is required.
	 */
	private boolean regexValidationRequired;

	/**
	 * boolean True if column header validation is required.
	 */
	private boolean colHeaderValidationRequired;

	/**
	 * boolean True if column coordinate validation is required.
	 */
	private boolean colCoordValidationRequired;

	/**
	 * Constructor.
	 */
	public TableExtractionAPI() {
		regexValidationRequired = false;
		colHeaderValidationRequired = false;
		colCoordValidationRequired = false;
	}

	/**
	 * Gets the table extraction API.
	 * 
	 * @return {@link String}
	 */
	public String getTableExtractionAPI() {
		return tableExtractionAPI;
	}

	/**
	 * Sets the table extraction API.
	 * 
	 * @param tableExtractionAPI {@link String}
	 */
	public void setTableExtractionAPI(String tableExtractionAPI) {
		this.tableExtractionAPI = tableExtractionAPI;
	}

	/**
	 * Tells if column coordinate validation is required or not.
	 * 
	 * @return boolean
	 */
	public boolean isColCoordValidationRequired() {
		return colCoordValidationRequired;
	}

	/**
	 * Sets column coordinate validation required or not.
	 * 
	 * @param colCoordValidationRequired
	 */
	public void setColCoordValidationRequired(boolean colCoordValidationRequired) {
		this.colCoordValidationRequired = colCoordValidationRequired;
	}

	/**
	 * Tells if regex validation is required or not.
	 * 
	 * @return boolean
	 */
	public boolean isRegexValidationRequired() {
		return regexValidationRequired;
	}

	/**
	 * Sets regex validation required or not.
	 * 
	 * @param regexValidationRequired boolean
	 */
	public void setRegexValidationRequired(boolean regexValidationRequired) {
		this.regexValidationRequired = regexValidationRequired;
	}

	/**
	 * Tells if column header validation is required or not.
	 * 
	 * @return boolean
	 */
	public boolean isColHeaderValidationRequired() {
		return colHeaderValidationRequired;
	}

	/**
	 * Sets column header validation required or not.
	 * 
	 * @param colHeaderValidationRequired
	 */
	public void setColHeaderValidationRequired(boolean colHeaderValidationRequired) {
		this.colHeaderValidationRequired = colHeaderValidationRequired;
	}

}
