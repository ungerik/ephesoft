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

package com.ephesoft.dcma.kvfinder.data;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.core.common.KVExtractZone;
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.LocationType;

/**
 * This data structure is used to carry input data key, value and location type.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.kvfinder.service.KVFinderServiceImpl
 * @see com.ephesoft.dcma.kvfinder.LocationFinder
 */
public class InputDataCarrier {

	/**
	 * Default value to be assigned to weight property required for key value field.
	 */
	private static final float DEFAULT_KV_FIELD_WEIGHT = 1.0f;

	/**
	 * Location type.
	 */
	private LocationType locationType;

	/**
	 * Key pattern.
	 */
	private String keyPattern;

	/**
	 * Value pattern.
	 */
	private String valuePattern;

	/**
	 * noOfWords.
	 */
	private Integer noOfWords;

	/**
	 * distance.
	 */
	private String distance;

	/**
	 * fetchValue.
	 */
	private KVFetchValue fetchValue;

	/**
	 * length.
	 */
	private Integer length;

	/**
	 * width.
	 */
	private Integer width;

	/**
	 * x-offset.
	 */
	private Integer xoffset;

	/**
	 * y-offset.
	 */
	private Integer yoffset;

	/**
	 * <code>weightValue</code> of KV Extraction Patter on the basis of which confidence will be build.
	 */
	private Float weightValue;

	/**
	 * use existing key.
	 */
	private boolean useExistingField;

	/**
	 * Coordinates of key rectangle drawn by user at UI.
	 */
	private Coordinates keyRectangleCoordinates;

	/**
	 * Coordinates of value rectangle drawn by user at UI.
	 */
	private Coordinates valueRectangleCoordinates;

	/**
	 * Extract zone for which this KV extraction field is to be executed.
	 */
	private KVExtractZone extractZone;

	/**
	 * {@code keyFuzziness} {@link Float} is used to define the threshold value for fuzzy matching for the key pattern.
	 */
	private Float keyFuzziness;

	/**
	 * @param locationType
	 * @param keyPattern
	 * @param valuePattern
	 */
	public InputDataCarrier(LocationType locationType, String keyPattern, String valuePattern, Integer noOfWords) {
		super();
		this.locationType = locationType;
		this.keyPattern = keyPattern;
		this.valuePattern = valuePattern;
		this.noOfWords = noOfWords;
		this.weightValue = DEFAULT_KV_FIELD_WEIGHT;
	}

	public InputDataCarrier(LocationType locationType, String keyPattern, String valuePattern, Integer noOfWords,
			KVFetchValue fetchValue, Integer length, Integer width, Integer xoffset, Integer yoffset, boolean useExistingField,
			Coordinates keyRectangleCoordinates, Coordinates valueRectangleCoordinates, Float weightValue, final Float keyFuzziness) {
		super();
		this.locationType = locationType;
		this.keyPattern = keyPattern;
		this.valuePattern = valuePattern;
		this.noOfWords = noOfWords;

		this.fetchValue = fetchValue;
		this.length = length;
		this.width = width;
		this.xoffset = xoffset;
		this.yoffset = yoffset;
		this.useExistingField = useExistingField;

		this.keyRectangleCoordinates = keyRectangleCoordinates;
		this.valueRectangleCoordinates = valueRectangleCoordinates;

		if (weightValue == null) {
			this.weightValue = DEFAULT_KV_FIELD_WEIGHT;
		} else {
			this.weightValue = weightValue;
		}

		this.keyFuzziness = keyFuzziness;

	}

	/**
	 * @return the locationType
	 */
	public LocationType getLocationType() {
		return locationType;
	}

	/**
	 * @param locationType the locationType to set
	 */
	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	/**
	 * @return the keyPattern
	 */
	public String getKeyPattern() {
		return keyPattern;
	}

	/**
	 * @param keyPattern the keyPattern to set
	 */
	public void setKeyPattern(String keyPattern) {
		this.keyPattern = keyPattern;
	}

	/**
	 * @return the valuePattern
	 */
	public String getValuePattern() {
		return valuePattern;
	}

	/**
	 * @param valuePattern the valuePattern to set
	 */
	public void setValuePattern(String valuePattern) {
		this.valuePattern = valuePattern;
	}

	/**
	 * @return the noOfWords
	 */
	public Integer getNoOfWords() {
		return noOfWords;
	}

	/**
	 * @param noOfWords the noOfWords to set
	 */
	public void setNoOfWords(final Integer noOfWords) {
		this.noOfWords = noOfWords;
	}

	/**
	 * @return the distance
	 */
	public String getDistance() {
		return distance;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(final String distance) {
		this.distance = distance;
	}

	public KVFetchValue getFetchValue() {
		return fetchValue;
	}

	public void setFetchValue(final KVFetchValue fetchValue) {
		this.fetchValue = fetchValue;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(final Integer width) {
		this.width = width;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(final Integer length) {
		this.length = length;
	}

	public Integer getXoffset() {
		return xoffset;
	}

	public void setXoffset(final Integer xoffset) {
		this.xoffset = xoffset;
	}

	public Integer getYoffset() {
		return yoffset;
	}

	public void setYoffset(final Integer yoffset) {
		this.yoffset = yoffset;
	}

	public boolean isUseExistingField() {
		return useExistingField;
	}

	public void setUseExistingField(final boolean useExistingField) {
		this.useExistingField = useExistingField;
	}

	public Coordinates getKeyRectangleCoordinates() {
		return keyRectangleCoordinates;
	}

	public void setKeyRectangleCoordinates(final Coordinates keyRectangleCoordinates) {
		this.keyRectangleCoordinates = keyRectangleCoordinates;
	}

	public Coordinates getValueRectangleCoordinates() {
		return valueRectangleCoordinates;
	}

	public void setValueRectangleCoordinates(final Coordinates valueRectangleCoordinates) {
		this.valueRectangleCoordinates = valueRectangleCoordinates;
	}

	/**
	 * Sets the weight value of KV Extraction Pattern.
	 * 
	 * @param weightValue the weight value of KV Extraction Pattern
	 */
	public void setWeightValue(Float weightValue) {
		this.weightValue = weightValue;
	}

	/**
	 * Gets the weight value of KV Extraction Pattern.
	 * 
	 * @return the weight value of KV Extraction Pattern
	 */
	public Float getWeightValue() {
		return weightValue;
	}

	/**
	 * Gets the extract zone for this KV Extraction field.
	 * 
	 * @return Returns the extract zone.
	 */
	public KVExtractZone getExtractZone() {
		return extractZone;
	}

	/**
	 * Sets the extract zone for KV Extraction field.
	 * 
	 * @param extractZone {@link KVExtractZone} extract zone to be set
	 */
	public void setExtractZone(KVExtractZone extractZone) {
		this.extractZone = extractZone;
	}

	/**
	 * @return the keyFuzziness
	 */
	public Float getKeyFuzziness() {
		return keyFuzziness;
	}

	/**
	 * @param keyFuzziness the keyFuzziness to set
	 */
	public void setKeyFuzziness(final Float keyFuzziness) {
		this.keyFuzziness = keyFuzziness;
	}

}
