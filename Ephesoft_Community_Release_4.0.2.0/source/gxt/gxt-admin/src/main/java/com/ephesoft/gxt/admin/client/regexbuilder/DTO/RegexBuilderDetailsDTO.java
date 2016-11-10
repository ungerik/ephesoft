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

package com.ephesoft.gxt.admin.client.regexbuilder.DTO;

import com.ephesoft.gxt.admin.client.regexbuilder.RegexFieldDetails;
import com.ephesoft.gxt.admin.client.regexbuilder.RegexGroupDetails;
import com.ephesoft.gxt.admin.client.regexbuilder.RegexQuantifierDetails;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents the DTO for the regex builder details used for the generation of regex pattern.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see RegexFieldDetails
 * @see RegexGroupDetails
 * @see RegexQuantifierDetails
 * @see IsSerializable
 */
public class RegexBuilderDetailsDTO implements IsSerializable {

	/**
	 * regexFieldDetailsDTO {@link RegexFieldDetails} The instance of regexFieldDetailsDTO.
	 */
	private RegexFieldDetails regexFieldDetails;

	/**
	 * regexGroupDetailsDTO {@link RegexGroupDetails} The instance of regexGroupDetailsDTO.
	 */
	private RegexGroupDetails regexGroupDetails;

	/**
	 * regexQuantifierDetailsDTO {@link RegexQuantifierDetails} The instance of regexQuantifierDetailsDTO.
	 */
	private RegexQuantifierDetails regexQuantifierDetails;

	/**
	 * @return the regexFieldDetailsDTO
	 */
	public RegexFieldDetails getRegexFieldDetails() {
		return regexFieldDetails;
	}

	/**
	 * @param regexFieldDetails the regexFieldDetailsDTO to set
	 */
	public void setRegexFieldDetails(final RegexFieldDetails regexFieldDetails) {
		this.regexFieldDetails = regexFieldDetails;
	}

	/**
	 * @return the regexGroupDetailsDTO
	 */
	public RegexGroupDetails getRegexGroupDetails() {
		return regexGroupDetails;
	}

	/**
	 * @param regexGroupDetails the regexGroupDetailsDTO to set
	 */
	public void setRegexGroupDetails(final RegexGroupDetails regexGroupDetails) {
		this.regexGroupDetails = regexGroupDetails;
	}

	/**
	 * @return the regexQuantifierDetailsDTO
	 */
	public RegexQuantifierDetails getRegexQuantifierDetails() {
		return regexQuantifierDetails;
	}

	/**
	 * @param regexQuantifierDetails the regexQuantifierDetailsDTO to set
	 */
	public void setRegexQuantifierDetails(final RegexQuantifierDetails regexQuantifierDetails) {
		this.regexQuantifierDetails = regexQuantifierDetails;
	}
}
