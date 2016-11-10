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

package com.ephesoft.dcma.da.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.common.KVExtractZone;
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.constant.DataAccessConstant;

@Entity
@Table(name = "kv_extraction")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class KVExtraction extends AbstractChangeableEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "field_type_id")
	private FieldType fieldType;

	@Column(name = "use_existing_key", columnDefinition = "bit default 0")
	private boolean useExistingKey;

	@Column(name = "key_pattern", nullable = false, length = 700)
	private String keyPattern;

	@Column(name = "value_pattern", length = 700)
	private String valuePattern;

	@Column(name = "location")
	@Enumerated(EnumType.STRING)
	private LocationType locationType;

	@Column(name = "no_of_words")
	private Integer noOfWords;

	@Column(name = "distance")
	private String distance;

	@Transient
	private Float multiplier;

	/**
	 * weight is used to define the weightage of field's kv pattern on the basis of which particular value extracted from patterns
	 * value is decided to be the field's value.
	 */
	@Column(name = "multiplier")
	private Float weight;

	@Column(name = "fetch_value")
	@Enumerated(EnumType.STRING)
	private KVFetchValue fetchValue;

	@Column(name = "page_value")
	@Enumerated(EnumType.STRING)
	private KVPageValue pageValue;

	/**
	 * Extract Zone for advanced kv extraction.
	 */
	@Column(name = "extract_zone")
	@Enumerated(EnumType.STRING)
	private KVExtractZone extractZone;

	@Column(name = "order_number", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int orderNo;

	/**
	 * length.
	 */
	@Column(name = "length")
	private Integer length;

	/**
	 * width.
	 */
	@Column(name = "width")
	private Integer width;

	/**
	 * x-offset.
	 */
	@Column(name = "x_offset")
	private Integer xoffset;

	/**
	 * y-offset.
	 */
	@Column(name = "y_offset")
	private Integer yoffset;

	@OneToOne
	@Cascade( {CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "advanced_kv_extraction_id")
	private AdvancedKVExtraction advancedKVExtraction;

	/**
	 * {@code keyFuzziness} {@link Float} is used to define the threshold value for fuzzy matching for the key pattern. Thershold value
	 * can vary from 0.0 to 1.0, but we have restricted user to enter the value only up to 0.3 from UI as per the requirement.
	 */
	@Column(name = DataAccessConstant.KEY_FUZZINESS)
	private Float keyFuzziness;

	/**
	 * @return the fieldType
	 */
	public FieldType getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldType the fieldType to set
	 */
	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
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
	 * @return the noOfWords
	 */
	public Integer getNoOfWords() {
		return noOfWords;
	}

	/**
	 * @param noOfWords the noOfWords to set
	 */
	public void setNoOfWords(Integer noOfWords) {
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
	public void setDistance(String distance) {
		this.distance = distance;
	}

	/**
	 * @return the multiplier
	 */
	public Float getMultiplier() {
		return multiplier;
	}

	/**
	 * @param multiplier the multiplier to set
	 */
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
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getXoffset() {
		return xoffset;
	}

	public void setXoffset(Integer xoffset) {
		this.xoffset = xoffset;
	}

	public Integer getYoffset() {
		return yoffset;
	}

	public KVPageValue getPageValue() {
		return pageValue;
	}

	public void setPageValue(KVPageValue pageValue) {
		this.pageValue = pageValue;
	}

	public void setYoffset(Integer yoffset) {
		this.yoffset = yoffset;
	}

	public AdvancedKVExtraction getAdvancedKVExtraction() {
		return advancedKVExtraction;
	}

	public void setAdvancedKVExtraction(AdvancedKVExtraction advancedKVExtraction) {
		this.advancedKVExtraction = advancedKVExtraction;
	}

	public boolean isUseExistingKey() {
		return useExistingKey;
	}

	public void setUseExistingKey(boolean useExistingKey) {
		this.useExistingKey = useExistingKey;
	}

	/**
	 * @return the orderNo
	 */
	public int getOrderNo() {
		return orderNo;
	}

	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}

	/**
	 * Sets the weight for the field's kv pattern value.
	 * 
	 * @param weight the weight of field's kv patten value.
	 */
	public void setWeight(Float weight) {
		this.weight = weight;
	}

	/**
	 * Gets the weight for the field's kv pattern value.
	 * 
	 * @return the weight of field's kv patten value.
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
