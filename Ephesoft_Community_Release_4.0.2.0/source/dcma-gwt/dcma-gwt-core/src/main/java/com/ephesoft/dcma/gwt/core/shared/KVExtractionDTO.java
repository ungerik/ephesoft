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

package com.ephesoft.dcma.gwt.core.shared;

import com.ephesoft.dcma.core.common.KVExtractZone;
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.dcma.core.common.LocationType;
import com.google.gwt.user.client.rpc.IsSerializable;

public class KVExtractionDTO implements IsSerializable {

	private FieldTypeDTO fieldTypeDTO;

	private boolean useExistingKey;

	private String keyPattern;

	private String valuePattern;

	private LocationType locationType;

	private String identifier;

	private boolean isDeleted;

	private boolean isNew;

	private Integer noOfWords;

	/**
	 * <code>weight</code> is the priority value of a kv extraction pattern for a filed type which will help in calculating confidence
	 * on the basis of which field type value will be populated.
	 */
	private Float weight;

	private KVPageValue kvPageValue;

	/**
	 * multiplier.
	 */
	private Float multiplier;

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
	 * Order Number will set the order of KV fields in the KV Field List
	 */
	private String orderNumber;

	/**
	 * Advanced Key Value extraction.
	 */
	private AdvancedKVExtractionDTO advancedKVExtractionDTO;

	/**
	 * {@link KVExtractZone} for the kv extraction.
	 */
	private KVExtractZone extractZone;
	
	/**
	 * {@code keyFuzziness} {@link Float} is used to define the threshold value for fuzzy matching for the key pattern. Thershold value
	 * can vary from 0.0 to 1.0, but we have restricted user to enter the value only up to 0.3 from UI as per the requirement.
	 */
	private Float keyFuzziness;

	public FieldTypeDTO getFieldTypeDTO() {
		return fieldTypeDTO;
	}

	public void setFieldTypeDTO(FieldTypeDTO fieldTypeDTO) {
		this.fieldTypeDTO = fieldTypeDTO;
	}

	public String getKeyPattern() {
		return keyPattern;
	}

	public void setKeyPattern(String keyPattern) {
		this.keyPattern = keyPattern;
	}

	public String getValuePattern() {
		return valuePattern;
	}

	public void setValuePattern(String valuePattern) {
		this.valuePattern = valuePattern;
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * @return the noOfWords
	 */
	public Integer getNoOfWords() {
		if (null == noOfWords) {
			noOfWords = 0;
		}
		return noOfWords;
	}

	/**
	 * @param noOfWords the noOfWords to set
	 */
	public void setNoOfWords(Integer noOfWords) {
		if (null == noOfWords) {
			noOfWords = 0;
		}
		this.noOfWords = noOfWords;
	}

	public Float getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(Float multiplier) {
		this.multiplier = multiplier;
	}

	public KVFetchValue getFetchValue() {
		return fetchValue;
	}

	public void setFetchValue(KVFetchValue fetchValue) {
		this.fetchValue = fetchValue;
	}

	public Integer getLength() {
		if (null == length) {
			length = 0;
		}
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getWidth() {
		if (null == width) {
			width = 0;
		}
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getXoffset() {
		if (null == xoffset) {
			xoffset = 0;
		}
		return xoffset;
	}

	public void setXoffset(Integer xoffset) {
		this.xoffset = xoffset;
	}

	public Integer getYoffset() {
		if (null == yoffset) {
			yoffset = 0;
		}
		return yoffset;
	}

	public void setYoffset(Integer yoffset) {
		this.yoffset = yoffset;
	}

	public boolean isSimpleKVExtraction() {
		boolean returnVal = true;
		if (width != null && width != 0 && length != null && length != 0) {
			returnVal = false;
		}
		return returnVal;
	}

	public AdvancedKVExtractionDTO getAdvancedKVExtractionDTO() {
		return advancedKVExtractionDTO;
	}

	public void setAdvancedKVExtractionDTO(AdvancedKVExtractionDTO advancedKVExtractionDTO) {
		this.advancedKVExtractionDTO = advancedKVExtractionDTO;
	}

	public KVPageValue getKvPageValue() {
		return kvPageValue;
	}

	public void setKvPageValue(KVPageValue kvPageValue) {
		this.kvPageValue = kvPageValue;
	}

	public boolean isUseExistingKey() {
		return useExistingKey;
	}

	public void setUseExistingKey(boolean useExistingKey) {
		this.useExistingKey = useExistingKey;
	}

	/**
	 * @return OrderNumber.
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param sets OrderNumber for KV Field.
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	/**
	 * Sets the weight value of KV extraction pattern.
	 * 
	 * @param weight the weight value of KV extraction pattern.
	 */
	public void setWeight(Float weight) {
		this.weight = weight;
	}

	/**
	 * Gets the weight value of KV extraction pattern.
	 * 
	 * @return the weight value of KV extraction pattern.
	 */
	public Float getWeight() {
		return weight;
	}

	/**
	 * Gets the Extract Zone of KV extraction pattern.
	 * 
	 * @return {@link KVExtractZone}
	 */
	public KVExtractZone getExtractZone() {
		return extractZone;
	}

	/**
	 * Sets the KV Extract Zone.
	 * 
	 * @param extractZone {@link KVExtractZone} to be set.
	 */
	public void setExtractZone(final KVExtractZone extractZone) {
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
