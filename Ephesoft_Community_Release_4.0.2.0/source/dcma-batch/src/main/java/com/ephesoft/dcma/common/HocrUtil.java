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
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;

/**
 * Util class to perform any operation on HOCR content.
 * 
 * @author Ephesoft
 * 
 */
public class HocrUtil {

	private static final int DEFAULT_CHAR_SPACE = 5;
	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(HocrUtil.class);

	/**
	 * Gets the line data carrier.
	 * 
	 * @param spans {@link Spans}
	 * @param pageID {@link String}
	 * @return {@link List<{@link LineDataCarrier}>}
	 */
	public static List<LineDataCarrier> getLineDataCarrierList(final Spans spans, final String pageID) {
		return getLineDataCarrierList(spans, pageID, false);
	}

	/**
	 * API to calculate the average space taken by a char within a span.
	 * 
	 * @param span
	 * @return
	 */
	private static int getSpanCharSpace(final Span span) {
		int allowedCharSpace = DEFAULT_CHAR_SPACE;
		if (span != null) {
			String spanValue = span.getValue();
			if (spanValue != null && !spanValue.isEmpty()) {
				int spanCharLength = spanValue.length();
				Coordinates spanCoordinates = span.getCoordinates();
				if (spanCoordinates != null) {
					int spanX1Coordinate = spanCoordinates.getX1().intValue();
					int spanX0Coordinate = spanCoordinates.getX0().intValue();
					int spanWidth = spanX0Coordinate - spanX1Coordinate;
					allowedCharSpace = Math.abs(spanWidth / spanCharLength);
				}
			}
		}
		return allowedCharSpace;
	}

	/**
	 * API to get valid rows from rows list with respect to the rectangular zone coordinates.
	 * 
	 * @param lineDataCarrierList List of rows
	 * @param zoneCoordinates Zone Coordinates
	 * @return
	 */
	public static List<LineDataCarrier> getValidRowsForZone(final List<LineDataCarrier> lineDataCarrierList,
			final Coordinates zoneCoordinates) {
		final List<LineDataCarrier> validLineDataCarrierList = new ArrayList<LineDataCarrier>();
		if (zoneCoordinates != null && lineDataCarrierList != null && !lineDataCarrierList.isEmpty()) {
			for (final LineDataCarrier lineDataCarrier : lineDataCarrierList) {
				final Coordinates rowCoordinates = lineDataCarrier.getRowCoordinates();
				if (rowCoordinates != null && isValidRowForZone(rowCoordinates, zoneCoordinates)) {
					validLineDataCarrierList.add(lineDataCarrier);
				}
			}
		}
		return validLineDataCarrierList;
	}

	/**
	 * API to check whether row is valid with respect to the rectangular zone coordinates.
	 * 
	 * @param rowCoordinate
	 * @param zoneCoordinates
	 * @return
	 */
	public static boolean isValidRowForZone(final Coordinates rowCoordinate, final Coordinates zoneCoordinate) {
		boolean isValidRow = false;
		if (rowCoordinate != null && zoneCoordinate != null) {
			final int rowY0 = rowCoordinate.getY0().intValue();
			final int zoneY0 = zoneCoordinate.getY0().intValue();
			final int rowY1 = rowCoordinate.getY1().intValue();
			final int zoneY1 = zoneCoordinate.getY1().intValue();
			if ((rowY0 >= zoneY0 && rowY0 <= zoneY1) || (rowY1 >= zoneY0 && rowY1 <= zoneY1) || (rowY0 <= zoneY0 && rowY1 >= zoneY1)) {
				isValidRow = true;
			}
		}
		return isValidRow;
	}

	/**
	 * API to get the rectangular coordinates for the coordinates list passed.
	 * 
	 * @param coordinatesList
	 * @return
	 */
	public static Coordinates getRectangleCoordinates(final List<Coordinates> coordinatesList) {
		final Coordinates rectCoordinate = new Coordinates();
		BigInteger minX0 = BigInteger.ZERO;
		BigInteger minY0 = BigInteger.ZERO;
		BigInteger maxX1 = BigInteger.ZERO;
		BigInteger maxY1 = BigInteger.ZERO;
		boolean isFirst = true;

		for (final Coordinates coordinates : coordinatesList) {
			final BigInteger hocrX0 = coordinates.getX0();
			final BigInteger hocrY0 = coordinates.getY0();
			final BigInteger hocrX1 = coordinates.getX1();
			final BigInteger hocrY1 = coordinates.getY1();
			if (isFirst) {
				minX0 = hocrX0;
				minY0 = hocrY0;
				maxX1 = hocrX1;
				maxY1 = hocrY1;
				isFirst = false;
			} else {
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

		rectCoordinate.setX0(minX0);
		rectCoordinate.setX1(maxX1);
		rectCoordinate.setY0(minY0);
		rectCoordinate.setY1(maxY1);
		return rectCoordinate;
	}

	/**
	 * @param coordinate
	 * @param zoneCoordinates
	 * @return distance between the two coordinates, -ve if coordinates lie inside the rectangle, +ve otherwise
	 */
	public static double calculateDistanceFromZone(final Coordinates coordinate, final Coordinates zoneCoordinates) {
		double distance = 0.0;
		LOGGER.info("Entering method calculateDistanceFromZone........");
		if (coordinate != null && zoneCoordinates != null) {
			BigInteger coordX0 = coordinate.getX0();
			BigInteger coordY0 = coordinate.getY0();
			BigInteger zoneX0 = zoneCoordinates.getX0();
			BigInteger zoneY0 = zoneCoordinates.getY0();
			if (coordX0 != null && coordY0 != null && zoneX0 != null && zoneY0 != null) {
				double xDistance = coordX0.subtract(zoneX0).doubleValue();
				double yDistance = coordY0.subtract(zoneY0).doubleValue();
				distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
			}
			if (isInsideZone(coordinate, zoneCoordinates)) {
				distance = -distance;
			}
		}
		LOGGER.info("Exiting method calculateDistanceFromZone........");
		return distance;
	}

	public static boolean isInsideZone(final Coordinates coordinate, final Coordinates zoneCoordinates) {
		boolean isInsideZone = false;
		if (coordinate != null && zoneCoordinates != null) {
			long coordX0 = coordinate.getX0().longValue();
			long coordY0 = coordinate.getY0().longValue();
			long coordX1 = coordinate.getX1().longValue();
			long coordY1 = coordinate.getY1().longValue();
			long zoneX0 = zoneCoordinates.getX0().longValue();
			long zoneY0 = zoneCoordinates.getY0().longValue();
			long zoneX1 = zoneCoordinates.getX1().longValue();
			long zoneY1 = zoneCoordinates.getY1().longValue();
			if (((coordX1 >= zoneX0 && coordX1 <= zoneX1) || (coordX0 >= zoneX0 && coordX0 <= zoneX1))
					&& ((coordY1 <= zoneY1 && coordY1 >= zoneY0) || (coordY0 <= zoneY1 && coordY0 >= zoneY0))) {
				isInsideZone = true;
			} else if (((zoneX0 <= coordX0 && zoneX1 >= coordX0) || (zoneX0 >= coordX1 && zoneX1 <= coordX1))
					&& ((zoneY0 >= coordY0 && zoneY0 <= coordY1) || (zoneY1 >= coordY0 && zoneY1 <= coordY1))
					|| ((zoneY0 <= coordY0 && zoneY1 >= coordY0) || (zoneY0 >= coordY1 && zoneY1 <= coordY1))
					&& ((zoneX0 >= coordX0 && zoneX0 <= coordX1) || (zoneX1 >= coordX0 && zoneX1 <= coordX1))) {
				isInsideZone = true;
			} else if (((zoneX0 > coordX0 && zoneX0 < coordX1) || (zoneX1 > coordX0 && zoneX1 < coordX1))
					&& ((zoneY0 > coordY0 && zoneY0 < coordY1) || (zoneY1 > coordY0 && zoneY1 < coordY1))) {
				isInsideZone = true;
			}
		}
		return isInsideZone;
	}

	/**
	 * API to get the line data carrier.
	 * 
	 * @param spans {@link Spans}
	 * @param pageID {@link String}
	 * @param checkCharacterSeperateWidth boolean
	 * @return {@link List<{@link LineDataCarrier}>}
	 */
	public static List<LineDataCarrier> getLineDataCarrierList(final Spans spans, final String pageID,
			final boolean checkCharacterSeperateWidth) {
		LOGGER.info("Entering method getLineDataCarrierList...");
		List<Span> mainSpanList = null;
		final List<LineDataCarrier> lineDataCarriers = new ArrayList<LineDataCarrier>();
		if (spans != null) {
			mainSpanList = spans.getSpan();
			if (null != mainSpanList && !mainSpanList.isEmpty()) {
				final Set<Span> set = sortSpansByCoordinates();
				set.addAll(mainSpanList);
				LineDataCarrier lineDataCarrier = new LineDataCarrier(pageID);
				lineDataCarriers.add(lineDataCarrier);
				List<Span> spanList = lineDataCarrier.getSpanList();
				int allowedCharSpace = DEFAULT_CHAR_SPACE;
				for (final Span span : set) {
					if (spanList.isEmpty()) {
						spanList.add(span);
						if (checkCharacterSeperateWidth) {
							allowedCharSpace = getSpanCharSpace(span);
						}
					} else {
						final Span lastSpan = spanList.get(spanList.size() - 1);
						final int charSpaceCompare = lastSpan.getCoordinates().getX1().intValue()
								- span.getCoordinates().getX0().intValue();
						boolean checkCharSpace = true;
						if (checkCharacterSeperateWidth) {
							checkCharSpace = charSpaceCompare >= -allowedCharSpace && charSpaceCompare <= allowedCharSpace;
						}

						// Algorithm for comparison: if old span's y coordinate's mid lies within range of new span's y coordinates or
						// not.
						final BigInteger s1Y0 = span.getCoordinates().getY0();
						final BigInteger s1Y1 = span.getCoordinates().getY1();
						final BigInteger s2Y0 = lastSpan.getCoordinates().getY0();
						final BigInteger s2Y1 = lastSpan.getCoordinates().getY1();
						final BigInteger s2Y = s2Y1.add(s2Y0);
						final int oldSpanMid = s2Y.intValue() / 2;
						if (oldSpanMid >= s1Y0.intValue() && oldSpanMid <= s1Y1.intValue() && checkCharSpace) {
							spanList.add(span);
						} else {
							lineDataCarrier = new LineDataCarrier(pageID);
							lineDataCarriers.add(lineDataCarrier);
							spanList = lineDataCarrier.getSpanList();
							spanList.add(span);
						}
					}
				}
			}
		}
		LOGGER.info("Exiting method getLineDataCarrierList...");
		return lineDataCarriers;
	}

	/**
	 * Sorts span on the basis of their coordinates.
	 * 
	 * @return {@link Set<{@link Span}>}
	 */
	private static Set<Span> sortSpansByCoordinates() {
		final Set<Span> set = new TreeSet<Span>(new Comparator<Span>() {

			public int compare(final Span firstSpan, final Span secSpan) {
				BigInteger s1Y0 = firstSpan.getCoordinates().getY0();
				BigInteger s1Y1 = firstSpan.getCoordinates().getY1();
				final BigInteger s2Y0 = secSpan.getCoordinates().getY0();
				final BigInteger s2Y1 = secSpan.getCoordinates().getY1();
				int halfOfSecSpan = (s2Y1.intValue() - s2Y0.intValue()) / 2;
				int y1 = s2Y1.intValue() + halfOfSecSpan;
				int y0 = s2Y0.intValue() - halfOfSecSpan;

				// following if else code is to handle abnormal(out of synch) value y0 or y1 coordinate of new span.
				if (isApproxEqual(s1Y0.intValue(), s2Y0.intValue()) && s1Y1.intValue() > y1) {
					s1Y1 = BigInteger.valueOf(y1);
					firstSpan.getCoordinates().setY1(s1Y1);
				} else if (isApproxEqual(s1Y1.intValue(), s2Y1.intValue()) && s1Y0.intValue() < y0) {
					s1Y0 = BigInteger.valueOf(y0);
					firstSpan.getCoordinates().setY0(s1Y0);
				}
				final BigInteger s1Y = s1Y1.add(s1Y0);
				final BigInteger s2Y = s2Y1.add(s2Y0);

				// calculating middle of old span.
				final int oldSpanMid = s2Y.intValue() / 2;
				int returnValue = 0;

				// if old span's y coordinate's middle lies within range of new span's y coordinates or not. if true, the two spans
				// belong to same line compare them further on their x coordinates, else they belong to two different lines.
				if (oldSpanMid >= s1Y0.intValue() && oldSpanMid <= s1Y1.intValue()) {
					final BigInteger s1X1 = firstSpan.getCoordinates().getX1();
					final BigInteger s2X1 = secSpan.getCoordinates().getX1();
					returnValue = s1X1.compareTo(s2X1);
				} else {
					returnValue = s1Y.compareTo(s2Y);
				}
				return returnValue;
			}
		});
		return set;
	}

	/**
	 * Checks if two coordinates are nearly equal.
	 * 
	 * @param first int
	 * @param second int
	 * @return boolean, true if parameter coordinates are equal.
	 */
	private static boolean isApproxEqual(final int first, final int second) {
		boolean result;
		int compare = first - second;
		if (compare < 0) {
			compare = -compare;
		}
		if (compare <= 5) {
			result = true;
		} else {
			result = false;
		}
		return result;
	}
}
