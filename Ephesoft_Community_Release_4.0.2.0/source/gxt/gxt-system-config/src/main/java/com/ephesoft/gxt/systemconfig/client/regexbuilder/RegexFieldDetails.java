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
 * Represents the POJO for the regex field options used for the generation of regex pattern.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see IsSerializable
 */
public class RegexFieldDetails implements IsSerializable {

	/**
	 * stringStartsWith boolean.
	 */
	private boolean stringStartsWith;

	/**
	 * stringEndsIn boolean.
	 */
	private boolean stringEndsIn;

	/**
	 * stringStartsWithAndEndsIn boolean.
	 */
	private boolean stringStartsWithAndEndsIn;

	/**
	 * stringContains boolean.
	 */
	private boolean stringContains;

	/**
	 * followedBy boolean.
	 */
	private boolean followedBy;

	/**
	 * orOperator boolean.
	 */
	private boolean orOperator;

	/**
	 * endsIn boolean.
	 */
	private boolean endsIn;

	/**
	 * onlyIfFollowedBy boolean.
	 */
	private boolean onlyIfFollowedBy;

	/**
	 * onlyIfNotFollowedBy boolean.
	 */
	private boolean onlyIfNotFollowedBy;

	/**
	 * onlyIfProceedBy boolean.
	 */
	private boolean onlyIfProceedBy;

	/**
	 * onlyIfNotProceedBy boolean.
	 */
	private boolean onlyIfNotProceedBy;

	/**
	 * matchOnlyIfPresent boolean.
	 */
	private boolean matchOnlyIfPresent;

	/**
	 * matchOnlyIfAbsent boolean.
	 */
	private boolean matchOnlyIfAbsent;

	/**
	 * fieldValue boolean.
	 */
	private String fieldValue;

	/**
	 * startsWithWordBoundary boolean.
	 */
	private boolean startsWithWordBoundary;

	/**
	 * endsInWordBoundary boolean.
	 */
	private boolean endsInWordBoundary;

	/**
	 * startsWithEndsInWordBoundary boolean.
	 */
	private boolean startsWithEndsInWordBoundary;

	/**
	 * @return the stringStartsWith
	 */
	public boolean isStringStartsWith() {
		return stringStartsWith;
	}

	/**
	 * @param stringStartsWith the stringStartsWith to set
	 */
	public void setStringStartsWith(final boolean stringStartsWith) {
		this.stringStartsWith = stringStartsWith;
	}

	/**
	 * @return the stringEndsIn
	 */
	public boolean isStringEndsIn() {
		return stringEndsIn;
	}

	/**
	 * @param stringEndsIn the stringEndsIn to set
	 */
	public void setStringEndsIn(final boolean stringEndsIn) {
		this.stringEndsIn = stringEndsIn;
	}

	/**
	 * @return the stringStartsWithAndEndsIn
	 */
	public boolean isStringStartsWithAndEndsIn() {
		return stringStartsWithAndEndsIn;
	}

	/**
	 * @param stringStartsWithAndEndsIn the stringStartsWithAndEndsIn to set
	 */
	public void setStringStartsWithAndEndsIn(final boolean stringStartsWithAndEndsIn) {
		this.stringStartsWithAndEndsIn = stringStartsWithAndEndsIn;
	}

	/**
	 * @return the stringContains
	 */
	public boolean isStringContains() {
		return stringContains;
	}

	/**
	 * @param stringContains the stringContains to set
	 */
	public void setStringContains(final boolean stringContains) {
		this.stringContains = stringContains;
	}

	/**
	 * @return the followedBy
	 */
	public boolean isFollowedBy() {
		return followedBy;
	}

	/**
	 * @param followedBy the followedBy to set
	 */
	public void setFollowedBy(final boolean followedBy) {
		this.followedBy = followedBy;
	}

	/**
	 * @return the orOperator
	 */
	public boolean isOrOperator() {
		return orOperator;
	}

	/**
	 * @param orOperator the orOperator to set
	 */
	public void setOrOperator(final boolean orOperator) {
		this.orOperator = orOperator;
	}

	/**
	 * @return the endsIn
	 */
	public boolean isEndsIn() {
		return endsIn;
	}

	/**
	 * @param endsIn the endsIn to set
	 */
	public void setEndsIn(final boolean endsIn) {
		this.endsIn = endsIn;
	}

	/**
	 * @return the onlyIfFollowedBy
	 */
	public boolean isOnlyIfFollowedBy() {
		return onlyIfFollowedBy;
	}

	/**
	 * @param onlyIfFollowedBy the onlyIfFollowedBy to set
	 */
	public void setOnlyIfFollowedBy(final boolean onlyIfFollowedBy) {
		this.onlyIfFollowedBy = onlyIfFollowedBy;
	}

	/**
	 * @return the onlyIfNotFollowedBy
	 */
	public boolean isOnlyIfNotFollowedBy() {
		return onlyIfNotFollowedBy;
	}

	/**
	 * @param onlyIfNotFollowedBy the onlyIfNotFollowedBy to set
	 */
	public void setOnlyIfNotFollowedBy(final boolean onlyIfNotFollowedBy) {
		this.onlyIfNotFollowedBy = onlyIfNotFollowedBy;
	}

	/**
	 * @return the onlyIfProceedBy
	 */
	public boolean isOnlyIfProceedBy() {
		return onlyIfProceedBy;
	}

	/**
	 * @param onlyIfProceedBy the onlyIfProceedBy to set
	 */
	public void setOnlyIfProceedBy(final boolean onlyIfProceedBy) {
		this.onlyIfProceedBy = onlyIfProceedBy;
	}

	/**
	 * @return the onlyIfNotProceedBy
	 */
	public boolean isOnlyIfNotProceedBy() {
		return onlyIfNotProceedBy;
	}

	/**
	 * @param onlyIfNotProceedBy the onlyIfNotProceedBy to set
	 */
	public void setOnlyIfNotProceedBy(final boolean onlyIfNotProceedBy) {
		this.onlyIfNotProceedBy = onlyIfNotProceedBy;
	}

	/**
	 * @return the matchOnlyIfPresent
	 */
	public boolean isMatchOnlyIfPresent() {
		return matchOnlyIfPresent;
	}

	/**
	 * @param matchOnlyIfPresent the matchOnlyIfPresent to set
	 */
	public void setMatchOnlyIfPresent(final boolean matchOnlyIfPresent) {
		this.matchOnlyIfPresent = matchOnlyIfPresent;
	}

	/**
	 * @return the matchOnlyIfAbsent
	 */
	public boolean isMatchOnlyIfAbsent() {
		return matchOnlyIfAbsent;
	}

	/**
	 * @param matchOnlyIfAbsent the matchOnlyIfAbsent to set
	 */
	public void setMatchOnlyIfAbsent(final boolean matchOnlyIfAbsent) {
		this.matchOnlyIfAbsent = matchOnlyIfAbsent;
	}

	/**
	 * @return the fieldValue
	 */
	public String getFieldValue() {
		return fieldValue;
	}

	/**
	 * @param fieldValue the fieldValue to set
	 */
	public void setFieldValue(final String fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 * @return the startsWithWordBoundary
	 */
	public boolean isStartsWithWordBoundary() {
		return startsWithWordBoundary;
	}

	/**
	 * @param startsWithWordBoundary the startsWithWordBoundary to set
	 */
	public void setStartsWithWordBoundary(final boolean startsWithWordBoundary) {
		this.startsWithWordBoundary = startsWithWordBoundary;
	}

	/**
	 * @return the endsInWordBoundary
	 */
	public boolean isEndsInWordBoundary() {
		return endsInWordBoundary;
	}

	/**
	 * @param endsInWordBoundary the endsInWordBoundary to set
	 */
	public void setEndsInWordBoundary(final boolean endsInWordBoundary) {
		this.endsInWordBoundary = endsInWordBoundary;
	}

	/**
	 * @return the startsWithEndsInWordBoundary
	 */
	public boolean isStartsWithEndsInWordBoundary() {
		return startsWithEndsInWordBoundary;
	}

	/**
	 * @param startsWithEndsInWordBoundary the startsWithEndsInWordBoundary to set
	 */
	public void setStartsWithEndsInWordBoundary(final boolean startsWithEndsInWordBoundary) {
		this.startsWithEndsInWordBoundary = startsWithEndsInWordBoundary;
	}

}
