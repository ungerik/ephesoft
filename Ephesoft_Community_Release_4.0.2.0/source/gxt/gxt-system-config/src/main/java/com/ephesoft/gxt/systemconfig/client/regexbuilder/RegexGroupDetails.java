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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents the POJO for the regex group options used for the generation of regex pattern.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see IsSerializable
 */
public class RegexGroupDetails implements IsSerializable {

	/**
	 * <code>caseInsensetive</code> true value indicates that the particular checkbox is being checked whereas false value indicates
	 * that the checkbox is being unchecked.
	 */
	private boolean caseInsensetive;

	/**
	 * <code>startGroup</code> true value indicates that the particular checkbox is being checked whereas false value indicates that
	 * the checkbox is being unchecked.
	 */
	private boolean startGroup;

	/**
	 * <code>endGroup</code> true value indicates that the particular checkbox is being checked whereas false value indicates that the
	 * checkbox is being unchecked.
	 */
	private boolean endGroup;

	/**
	 * <code>captureGroup</code> true value indicates that the particular checkbox is being checked whereas false value indicates that
	 * the checkbox is being unchecked.
	 */
	private boolean captureGroup;

	/**
	 * <code>nonCaptureGroup</code> true value indicates that the particular checkbox is being checked whereas false value indicates
	 * that the checkbox is being unchecked.
	 */
	private boolean nonCaptureGroup;

	/**
	 * <code>regexQuantifierAppliedToEntireGroup</code> true value indicates that the particular checkbox is being checked whereas
	 * false value indicates that the checkbox is being unchecked.
	 */
	private boolean regexQuantifierAppliedToEntireGroup;

	/**
	 * @return the caseInsensetive
	 */
	public boolean isCaseInsensetive() {
		return caseInsensetive;
	}

	/**
	 * @param caseInsensetive the caseInsensetive to set
	 */
	public void setCaseInsensetive(final boolean caseInsensetive) {
		this.caseInsensetive = caseInsensetive;
	}

	/**
	 * @return the startGroup
	 */
	public boolean isStartGroup() {
		return startGroup;
	}

	/**
	 * @param startGroup the startGroup to set
	 */
	public void setStartGroup(final boolean startGroup) {
		this.startGroup = startGroup;
	}

	/**
	 * @return the endGroup
	 */
	public boolean isEndGroup() {
		return endGroup;
	}

	/**
	 * @param endGroup the endGroup to set
	 */
	public void setEndGroup(final boolean endGroup) {
		this.endGroup = endGroup;
	}

	/**
	 * @return the captureGroup
	 */
	public boolean isCaptureGroup() {
		return captureGroup;
	}

	/**
	 * @param captureGroup the captureGroup to set
	 */
	public void setCaptureGroup(final boolean captureGroup) {
		this.captureGroup = captureGroup;
	}

	/**
	 * @return the nonCaptureGroup
	 */
	public boolean isNonCaptureGroup() {
		return nonCaptureGroup;
	}

	/**
	 * @param nonCaptureGroup the nonCaptureGroup to set
	 */
	public void setNonCaptureGroup(final boolean nonCaptureGroup) {
		this.nonCaptureGroup = nonCaptureGroup;
	}

	/**
	 * @return the regexQuantifierAppliedToEntireGroup
	 */
	public boolean isRegexQuantifierAppliedToEntireGroup() {
		return regexQuantifierAppliedToEntireGroup;
	}

	/**
	 * @param regexQuantifierAppliedToEntireGroup the regexQuantifierAppliedToEntireGroup to set
	 */
	public void setRegexQuantifierAppliedToEntireGroup(final boolean regexQuantifierAppliedToEntireGroup) {
		this.regexQuantifierAppliedToEntireGroup = regexQuantifierAppliedToEntireGroup;
	}

}
