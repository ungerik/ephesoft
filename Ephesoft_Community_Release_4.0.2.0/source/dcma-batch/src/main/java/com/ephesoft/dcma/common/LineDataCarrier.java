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

package com.ephesoft.dcma.common;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;

public class LineDataCarrier {

	private List<Span> spanList;

	private String pageID;

	/**
	 * Constructor.
	 * 
	 */
	public LineDataCarrier(final List<Span> spanList, final String pageID) {
		super();
		this.spanList = spanList;
		this.pageID = pageID;
	}

	/**
	 * Constructor.
	 * 
	 */
	public LineDataCarrier(final String pageID) {
		super();
		this.spanList = new ArrayList<Span>();
		this.pageID = pageID;
	}

	public List<Span> getSpanList() {
		return spanList;
	}

	public void setSpanList(final List<Span> spanList) {
		this.spanList = spanList;
	}

	public String getPageID() {
		return pageID;
	}

	public void setPageID(final String pageID) {
		this.pageID = pageID;
	}

	/**
	 * API to get the row data.
	 * 
	 * @return
	 */
	public String getLineRowData() {
		final StringBuilder rowData = new StringBuilder();
		if (this.spanList != null && !this.spanList.isEmpty()) {
			for (final Span span : this.spanList) {
				if (null != span && span.getValue() != null && !span.getValue().isEmpty()) {
					final String value = span.getValue();
					rowData.append(value);
					rowData.append(' ');
				}
			}
		}

		return rowData.toString();
	}

	/**
	 * API to the index list of all searchValues found in the current row.
	 * 
	 * @param searchValue
	 * @return
	 */
	public List<Integer> getIndexOfSpan(final String searchValue) {
		final List<Integer> indexList = new ArrayList<Integer>(0);
		if (searchValue != null && this.spanList != null && !this.spanList.isEmpty()) {
			int local = 0;
			for (final Span span : this.spanList) {
				if (null == span) {
					local++;
					continue;
				}
				final String value = span.getValue();
				if (null != value && !value.isEmpty() && value.contains(searchValue)) {
					indexList.add(local);
				}
				local++;
			}
		}

		return indexList;
	}

	/**
	 * API to get the span at passed index in the current row.
	 * 
	 * @param currentIndex
	 * @return
	 */
	public Span getCurrentSpan(final int currentIndex) {
		Span span = null;
		if (null != this.spanList && !this.spanList.isEmpty()) {
			final int length = this.spanList.size();
			if (currentIndex >= 0 && currentIndex < length) {
				span = this.spanList.get(currentIndex);
			}
			// place a logger that current index is invalid
		}
		return span;
	}

	/**
	 * API to get the span at left of passed index in the current row.
	 * 
	 * @param currentIndex
	 * @return
	 */
	public Span getLeftSpan(final int currentIndex) {
		Span span = null;
		if (null != this.spanList && !this.spanList.isEmpty()) {
			final int length = this.spanList.size();
			if (currentIndex - 1 >= 0 && currentIndex - 1 < length) {
				span = this.spanList.get(currentIndex - 1);
			}
		}
		return span;
	}

	/**
	 * API to get the span at right of passed index in the current row.
	 * 
	 * @param currentIndex
	 * @return
	 */
	public Span getRightSpan(final int currentIndex) {
		Span span = null;
		if (null != this.spanList && !this.spanList.isEmpty()) {
			final int length = this.spanList.size();
			if (currentIndex + 1 >= 0 && currentIndex + 1 < length) {
				span = this.spanList.get(currentIndex + 1);
			}
		}
		return span;
	}

	/**
	 * API to get the index of passed {@link Span} in the current row.
	 * 
	 * @param span
	 * @return
	 */
	public Integer getIndexOfSpan(final Span span) {
		Integer indexOf = null;
		if (this.spanList != null && span != null) {
			for (final Span currentSpan : this.spanList) {
				if (currentSpan != null && compareSpans(currentSpan, span)) {
					indexOf = spanList.indexOf(currentSpan);
					break;
				}
			}
		}
		return indexOf;
	}

	/**
	 * API to return the span at right of the passed span. Returns null if span doesn't exists at right.
	 * 
	 * @param span
	 * @return
	 */
	public Span getRightSpan(final Span span) {
		Span rightSpan = null;
		if (this.spanList != null && span != null) {
			for (final Span currentSpan : this.spanList) {
				if (currentSpan == null) {
					continue;
				}
				final int indexOf = spanList.indexOf(currentSpan);
				if (indexOf < spanList.size() - 1 && compareSpans(currentSpan, span)) {
					rightSpan = spanList.get(indexOf + 1);
					break;
				}
			}
		}
		return rightSpan;
	}

	/**
	 * API to return the span at left of the passed span. Returns null if span doesn't exists at left.
	 * 
	 * @param span
	 * @return
	 */
	public Span getLeftSpan(final Span span) {
		Span leftSpan = null;
		if (this.spanList != null && span != null) {
			for (final Span currentSpan : this.spanList) {
				if (currentSpan == null) {
					continue;
				}
				final int indexOf = spanList.indexOf(currentSpan);
				if (indexOf > 0 && compareSpans(currentSpan, span)) {
					leftSpan = spanList.get(indexOf - 1);
					break;
				}
			}
		}
		return leftSpan;
	}

	/**
	 * API to match two spans. Returns true if they match otherwise false.
	 * 
	 * @param currentSpan {@link Span}
	 * @param span {@link Span}
	 * @return
	 */
	public boolean compareSpans(final Span currentSpan, final Span span) {
		boolean spanMatched = false;
		if (currentSpan != null && span != null && currentSpan.getCoordinates() != null && span.getCoordinates() != null) {
			final BigInteger currSpanX0 = currentSpan.getCoordinates().getX0();
			final BigInteger currSpanX1 = currentSpan.getCoordinates().getX1();
			final BigInteger currSpanY0 = currentSpan.getCoordinates().getY0();
			final BigInteger currSpanY1 = currentSpan.getCoordinates().getY0();

			final BigInteger spanX0 = span.getCoordinates().getX0();
			final BigInteger spanX1 = span.getCoordinates().getX1();
			final BigInteger spanY0 = span.getCoordinates().getY0();
			final BigInteger spanY1 = span.getCoordinates().getY0();

			if (spanX0.compareTo(currSpanX0) == 0 && spanX1.compareTo(currSpanX1) == 0 && spanY0.compareTo(currSpanY0) == 0
					&& spanY1.compareTo(currSpanY1) == 0) {
				spanMatched = true;
			}
		}
		return spanMatched;
	}

	/**
	 * API to get the row coordinates of current row.
	 * 
	 * @return
	 */
	public Coordinates getRowCoordinates() {

		final Coordinates coordinates = new Coordinates();
		BigInteger minX0 = BigInteger.ZERO;
		BigInteger minY0 = BigInteger.ZERO;
		BigInteger maxX1 = BigInteger.ZERO;
		BigInteger maxY1 = BigInteger.ZERO;

		boolean isFirst = true;

		if (this.spanList != null && !this.spanList.isEmpty()) {
			for (final Span span : this.spanList) {
				if (null == span || span.getCoordinates() == null) {
					continue;
				}
				final Coordinates hocrCoordinates = span.getCoordinates();
				final BigInteger hocrX0 = hocrCoordinates.getX0();
				final BigInteger hocrY0 = hocrCoordinates.getY0();
				final BigInteger hocrX1 = hocrCoordinates.getX1();
				final BigInteger hocrY1 = hocrCoordinates.getY1();
				if (isFirst) {
					minX0 = hocrX0;
					minY0 = hocrY0;
					maxX1 = hocrX1;
					maxY1 = hocrY1;
					isFirst = false;
					continue;
				}
				if (hocrX0.compareTo(minX0) < 0) {
					minX0 = hocrX0;
				}
				if (hocrY0.compareTo(minY0) < 0) {
					minY0 = hocrY0;
				}
				if (hocrX1.compareTo(maxX1) > 0) {
					maxX1 = hocrX1;
				}
				if (hocrY1.compareTo(maxY1) > 0) {
					maxY1 = hocrY1;
				}
			}
		}

		coordinates.setX0(minX0);
		coordinates.setX1(maxX1);
		coordinates.setY0(minY0);
		coordinates.setY1(maxY1);

		return coordinates;
	}

	/**
	 * API to add spans to the left of passed span index based on the distance between between adjacent words in a line.
	 * 
	 * @param span Current span
	 * @param gapBetweenWords Distance between words of a line
	 * @return
	 */
	public List<Span> appendSpansLeft(final Span span, final int gapBetweenWords) {
		final List<Span> finalSpanList = new ArrayList<Span>();
		Span nextSpan = null;
		Span prevSpan = span;
		if (span != null) {
			Integer currSpanIndex = getIndexOfSpan(span);
			if (currSpanIndex != null && this.spanList != null && !this.spanList.isEmpty()) {
				nextSpan = getLeftSpan(currSpanIndex++);
				while (nextSpan != null && currSpanIndex < this.spanList.size()) {
					if (Math.abs(nextSpan.getCoordinates().getX1().subtract(prevSpan.getCoordinates().getX0()).intValue()) <= gapBetweenWords) {
						finalSpanList.add(nextSpan);
					} else {
						break;
					}
					prevSpan = nextSpan;
					nextSpan = getLeftSpan(currSpanIndex++);
				}
			}
			Collections.reverse(finalSpanList);
		}
		return finalSpanList;
	}

	/**
	 * API to add spans to the right of passed span index based on the distance between between words in a column.
	 * 
	 * @param span current span
	 * @param gapBetweenWords Distance between words of a single column
	 * @return
	 */
	public List<Span> appendSpansRight(final Span span, final int gapBetweenWords) {
		final List<Span> finalSpanList = new ArrayList<Span>();
		Span nextSpan = null;
		Span prevSpan = span;
		if (span != null) {
			Integer currSpanIndex = getIndexOfSpan(span);
			if (currSpanIndex != null && this.spanList != null && !this.spanList.isEmpty()) {
				nextSpan = getRightSpan(currSpanIndex++);
				while (nextSpan != null && currSpanIndex < this.spanList.size()) {
					if (Math.abs(nextSpan.getCoordinates().getX0().subtract(prevSpan.getCoordinates().getX1()).intValue()) <= gapBetweenWords) {
						finalSpanList.add(nextSpan);
					} else {
						break;
					}
					prevSpan = nextSpan;
					nextSpan = getRightSpan(currSpanIndex++);
				}
			}
		}
		return finalSpanList;
	}

}
