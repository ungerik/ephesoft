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

/**
 * Defines various zones for an image where KV Extraction can be performed. If in future some other zone needs to be added based on
 * which KV Extraction is to be performed, corresponding entry should be done here.
 * 
 * <p>
 * Currently there are 6 extract zones for which KV Extraction has been implemented:
 * <ul>
 * <li>ALL</li>
 * <li>TOP</li>
 * <li>MIDDLE</li>
 * <li>BOTTOM</li>
 * <li>LEFT</li>
 * <li>RIGHT</li>
 * </ul>
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 06-Jan-2013 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public enum KVExtractZone {

	/**
	 * Constant declared for ALL zones of the image. Used when extraction is to performed on all image.
	 */
	ALL,

	/**
	 * Constant for the TOP zone of the image.
	 */
	TOP,

	/**
	 * Constant for the LEFT zone of the image.
	 */
	LEFT,

	/**
	 * Constant for the ROGHT zone of the image.
	 */
	RIGHT,
	
	/**
	 * Constant for the MIDDLE zone of the image.
	 */
	MIDDLE,

	/**
	 * Constant for the BOTTOM zone of the image.
	 */
	BOTTOM;

	/**
	 * Gets the Enum value with same name as parameter, Otherwise null.
	 * 
	 * @param string {@link String} with same name of any enum value.
	 * @return {@link KVExtractZone} whose name matches the string, Otherwise null.
	 */
	public static KVExtractZone getValue(final String string) {
		KVExtractZone extractZone = null;
		KVExtractZone[] kvExtractZoneList = KVExtractZone.values();
		if (string != null) {
			for (KVExtractZone tempKVExtractZone : kvExtractZoneList) {
				String tempName = tempKVExtractZone.name();
				if (tempName != null && tempName.equals(string)) {
					extractZone = tempKVExtractZone;
					break;
				}
			}
		}
		return extractZone;
	}

}
