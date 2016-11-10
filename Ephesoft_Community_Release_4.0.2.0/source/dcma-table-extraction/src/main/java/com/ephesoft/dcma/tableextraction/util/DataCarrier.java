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

package com.ephesoft.dcma.tableextraction.util;

import java.util.List;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;

/**
 * This class is used to carry the span, confidence and value objects for extraction.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class DataCarrier implements Comparable<DataCarrier> {

	/**
	 * List of spans information.
	 */
	private List<Span> spans;

	/**
	 * Confidence information.
	 */
	private float confidence;

	/**
	 * Value information.
	 */
	private String value;

	/**
	 * Value coordinates tag information.
	 */
	private Coordinates coordinates;

	/**
	 * Constructor.
	 * 
	 * @param spans {@link List<{@link Span}>}
	 * @param confidence float
	 * @param value {@link String}
	 * @param coordinates {@link Coordinates}
	 */
	public DataCarrier(final List<Span> spans, final float confidence, final String value, final Coordinates coordinates) {
		super();
		this.spans = spans;
		this.confidence = confidence;
		this.value = value;
		this.coordinates = coordinates;
	}

	/**
	 * Compare a given DataCarrier with this object. If confidence of this object is greater than the received object, then this object
	 * is greater than the other. As we have to finder larger confidence score value we will return -1 for this case.
	 * 
	 * @param dataCarrier DataCarrier
	 * @return int
	 */
	public int compareTo(final DataCarrier dataCarrier) {

		int returnValue = 0;

		final float diffConfidence = this.getConfidence() - dataCarrier.getConfidence();

		if (diffConfidence > 0) {
			returnValue = -1;
		}
		if (diffConfidence < 0) {
			returnValue = 1;
		}

		return returnValue;
	}

	/**
	 * Gets list of spans.
	 * 
	 * @return {@link List<{@link Span}>}
	 */
	public List<Span> getSpans() {
		return spans;
	}

	/**
	 * Sets list of spans.
	 * 
	 * @param spans {@link List<{@link Span}>}
	 */
	public void setSpans(List<Span> spans) {
		this.spans = spans;
	}

	/**
	 * Gets the confidence of the data value.
	 * 
	 * @param confidence float
	 */
	public final float getConfidence() {
		return confidence;
	}

	/**
	 * Gets the data value.
	 * 
	 * @return {@link String}
	 */
	public final String getValue() {
		return value;
	}

	/**
	 * Sets data value coordinates.
	 * 
	 * @param spans {@link Coordinates}
	 */
	public Coordinates getCoordinates() {
		return coordinates;
	}

	/**
	 * Gets data value coordinates.
	 * 
	 * @return {@link Coordinates}
	 */
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * Sets the confidence of the data value.
	 * 
	 * @param confidence float
	 */
	public final void setConfidence(final float confidence) {
		this.confidence = confidence;
	}

	/**
	 * Sets the data value.
	 * 
	 * @param value {@link String}
	 */
	public final void setValue(final String value) {
		this.value = value;
	}

}
