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
 * Defines the different type of periods for batch instance to be shown in bar
 * chart.
 * <p>
 * Periods are in hours.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public enum BatchInstancePeriod {

	/** The period one. */
	PERIOD_ONE(0, 1, "1 HOUR"),
	/** The period two. */
	PERIOD_TWO(1, 4, "4 HOUR"),
	/** The period three. */
	PERIOD_THREE(4, 24, "1 DAY"),
	/** The period four. */
	PERIOD_FOUR(24, Double.MAX_VALUE, "1+ DAY");

	/** The lower limit. */
	private double lowerLimit;

	/** The upper limit. */
	private double upperLimit;

	private String name;

	/**
	 * Instantiates a new batch instance period.
	 * 
	 * @param lowerLimit
	 *            the lower limit
	 * @param upperLimit
	 *            the upper limit
	 */
	private BatchInstancePeriod(final double lowerLimit,
			final double upperLimit, final String name) {
		this.lowerLimit = lowerLimit;
		this.upperLimit = upperLimit;
		this.name = name;
	}

	/**
	 * Gets the lower limit.
	 * 
	 * @return the lower limit
	 */
	public double getLowerLimit() {
		return lowerLimit;
	}

	/**
	 * Gets the upper limit.
	 * 
	 * @return the upper limit
	 */
	public double getUpperLimit() {
		return upperLimit;
	}

	@Override
	public String toString() {
		return name;
	}
}
