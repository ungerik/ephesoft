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
 * Represents the POJO for the regex quantifier options used for the generation of regex pattern.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see IsSerializable
 */
public class RegexQuantifierDetails implements IsSerializable {

	/**
	 * asFewAsPossible boolean.
	 */
	private boolean asFewAsPossible;

	/**
	 * minimumNumberOfTimes {@link String}.
	 */
	private String minimumNumberOfTimes;

	/**
	 * maximumNumberOfTimes {@link String}.
	 */
	private String maximumNumberOfTimes;

	/**
	 * noOfTimes {@link String}.
	 */
	private String noOfTimes;
	/**
	 * oneOrMoreTimes boolean.
	 */
	private boolean oneOrMoreTimes;

	/**
	 * betweenZeroAndOneTimes boolean.
	 */
	private boolean betweenZeroAndOneTimes;

	/**
	 * anyNumberOfTimes boolean.
	 */
	private boolean anyNumberOfTimes;

	/**
	 * @return the asFewAsPossible
	 */
	public boolean isAsFewAsPossible() {
		return asFewAsPossible;
	}

	/**
	 * @param asFewAsPossible the asFewAsPossible to set
	 */
	public void setAsFewAsPossible(final boolean asFewAsPossible) {
		this.asFewAsPossible = asFewAsPossible;
	}

	/**
	 * @return the minimumNumberOfTimes
	 */
	public String getMinimumNumberOfTimes() {
		return minimumNumberOfTimes;
	}

	/**
	 * @param minimumNumberOfTimes the minimumNumberOfTimes to set
	 */
	public void setMinimumNumberOfTimes(final String minimumNumberOfTimes) {
		this.minimumNumberOfTimes = minimumNumberOfTimes;
	}

	/**
	 * @return the maximumNumberOfTimes
	 */
	public String getMaximumNumberOfTimes() {
		return maximumNumberOfTimes;
	}

	/**
	 * @param maximumNumberOfTimes the maximumNumberOfTimes to set
	 */
	public void setMaximumNumberOfTimes(final String maximumNumberOfTimes) {
		this.maximumNumberOfTimes = maximumNumberOfTimes;
	}

	/**
	 * @return the noOfTimes
	 */
	public String getNoOfTimes() {
		return noOfTimes;
	}

	/**
	 * @param noOfTimes the noOfTimes to set
	 */
	public void setNoOfTimes(final String noOfTimes) {
		this.noOfTimes = noOfTimes;
	}

	/**
	 * @return the oneOrMoreTimes
	 */
	public boolean isOneOrMoreTimes() {
		return oneOrMoreTimes;
	}

	/**
	 * @param oneOrMoreTimes the oneOrMoreTimes to set
	 */
	public void setOneOrMoreTimes(final boolean oneOrMoreTimes) {
		this.oneOrMoreTimes = oneOrMoreTimes;
	}

	/**
	 * @return the betweenZeroAndOneTimes
	 */
	public boolean isBetweenZeroAndOneTimes() {
		return betweenZeroAndOneTimes;
	}

	/**
	 * @param betweenZeroAndOneTimes the betweenZeroAndOneTimes to set
	 */
	public void setBetweenZeroAndOneTimes(final boolean betweenZeroAndOneTimes) {
		this.betweenZeroAndOneTimes = betweenZeroAndOneTimes;
	}

	/**
	 * @return the anyNumberOfTimes
	 */
	public boolean isAnyNumberOfTimes() {
		return anyNumberOfTimes;
	}

	/**
	 * @param anyNumberOfTimes the anyNumberOfTimes to set
	 */
	public void setAnyNumberOfTimes(final boolean anyNumberOfTimes) {
		this.anyNumberOfTimes = anyNumberOfTimes;
	}

}
