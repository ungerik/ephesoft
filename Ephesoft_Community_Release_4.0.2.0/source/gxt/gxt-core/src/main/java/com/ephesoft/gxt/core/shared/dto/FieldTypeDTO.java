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

package com.ephesoft.gxt.core.shared.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.core.common.DataType;
import com.ephesoft.dcma.core.common.LocationType;
import com.ephesoft.dcma.core.common.WidgetType;
import com.google.gwt.user.client.rpc.IsSerializable;

public class FieldTypeDTO implements IsSerializable, Selectable, Comparable<FieldTypeDTO> {

	private DocumentTypeDTO docTypeDTO;

	private DataType dataType;

	private String identifier;

	private String name;

	private String description;

	private String pattern;

	private boolean isDeleted;

	private boolean isNew;

	private int fieldOrderNumber;

	private String sampleValue;

	private String barcodeType;

	private String fieldOptionValueList;

	private List<String> regexPatternList;

	private WidgetType widgetType;

	private List<KVExtractionDTO> kvExtractionList = new ArrayList<KVExtractionDTO>();

	private List<RegexDTO> regexList = new ArrayList<RegexDTO>();

	private boolean hidden;

	/**
	 * Field is set to true if document level field is readonly.
	 */
	private boolean readOnly;

	private boolean fieldValueChangeScriptEnabled;

	private float ocrConfidenceThreshold;

	private boolean isSelected;

	private List<IndexFieldDBExportMappingDTO> indexFieldMappingList;

	private String category;

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * regexPatternSeparator {@link String} specifies the regex listing logical separtaor. For example OR, AND etc.
	 */
	private String regexListingSeparator;

	public DocumentTypeDTO getDocTypeDTO() {
		return docTypeDTO;
	}

	public void setDocTypeDTO(DocumentTypeDTO docTypeDTO) {
		this.docTypeDTO = docTypeDTO;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public String getSampleValue() {
		return sampleValue;
	}

	public void setSampleValue(String sampleValue) {
		this.sampleValue = sampleValue;
	}

	public List<String> getRegexPatternList() {
		return regexPatternList;
	}

	public void setRegexPatternList(List<String> regexPatternList) {
		this.regexPatternList = regexPatternList;
	}

	public void setRegexList(List<RegexDTO> regexList) {
		this.regexList = regexList;
	}

	public List<KVExtractionDTO> getKvExtractionList(boolean includeDeleted) {
		if (includeDeleted)
			return kvExtractionList;
		return getKvExtractionList();
	}

	public List<KVExtractionDTO> getKvExtractionList() {
		List<KVExtractionDTO> kvExtractionDTOs = new LinkedList<KVExtractionDTO>();
		for (KVExtractionDTO kvExtractionDTO : kvExtractionList) {
			if (!(kvExtractionDTO.isDeleted())) {
				kvExtractionDTOs.add(kvExtractionDTO);
			}
		}
		return kvExtractionDTOs;
	}

	public List<RegexDTO> getRegexList(boolean includeDeleted) {
		if (includeDeleted)
			return regexList;
		return getRegexList();
	}

	public List<RegexDTO> getRegexList() {
		List<RegexDTO> regexDTOs = new LinkedList<RegexDTO>();
		for (RegexDTO regexDTO : regexList) {
			if (!(regexDTO.isDeleted())) {
				regexDTOs.add(regexDTO);
			}
		}
		return regexDTOs;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public void addKvExtraction(KVExtractionDTO kvExtractionDTO) {
		kvExtractionList.add(kvExtractionDTO);
	}

	public void removeKvExtraction(KVExtractionDTO kvExtractionDTO) {
		this.kvExtractionList.remove(kvExtractionDTO);
	}

	public void setFieldOrderNumber(int fieldOrderNumber) {
		this.fieldOrderNumber = fieldOrderNumber;
	}

	public int getFieldOrderNumber() {
		return fieldOrderNumber;
	}

	public KVExtractionDTO getKVExtractionByKeyAndDataTypeAndLocation(String keyPattern, String valuePattern, LocationType locationType) {
		Collection<KVExtractionDTO> dtos = kvExtractionList;
		for (KVExtractionDTO kvExtractionDTO : dtos) {
			if (kvExtractionDTO.getKeyPattern() != null && kvExtractionDTO.getKeyPattern().equals(keyPattern)) {
				if (kvExtractionDTO.getValuePattern() != null && kvExtractionDTO.getValuePattern().equals(valuePattern))
					if (kvExtractionDTO.getLocationType() != null
							&& kvExtractionDTO.getLocationType().name().equals(locationType.name()))
						return kvExtractionDTO;
			}
		}
		return null;
	}

	public boolean checkKVExtractionDetails(String keyPattern, String valuePattern, LocationType locationType) {
		if (getKVExtractionByKeyAndDataTypeAndLocation(keyPattern, valuePattern, locationType) != null)
			return true;
		return false;
	}

	public KVExtractionDTO getKVExtractionDTOByIdentifier(String identifier) {
		for (KVExtractionDTO extractionDTO : kvExtractionList) {
			if (extractionDTO.getIdentifier().equals(identifier))
				return extractionDTO;
		}
		return null;
	}

	public boolean checkRegex(String pattern) {
		for (RegexDTO dto : regexList) {
			if (dto.getPattern().equals(pattern))
				return true;
		}
		return false;
	}

	public void addRegex(RegexDTO regexDTO) {
		regexList.add(regexDTO);
	}

	public RegexDTO getRegexDTOByIdentifier(String identifier) {
		for (RegexDTO dto : regexList) {
			if (dto.getIdentifier().equals(identifier))
				return dto;
		}
		return null;
	}

	public RegexDTO getRegexDTOByPattern(String pattern) {
		for (RegexDTO dto : regexList) {
			if (dto.getPattern().equals(pattern))
				return dto;
		}
		return null;
	}

	public String getBarcodeType() {
		return barcodeType;
	}

	public void setBarcodeType(String barcodeType) {
		this.barcodeType = barcodeType;
	}

	public String getFieldOptionValueList() {
		return fieldOptionValueList;
	}

	public void setFieldOptionValueList(String fieldOptionValueList) {
		this.fieldOptionValueList = fieldOptionValueList;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean isHidden) {
		this.hidden = isHidden;
	}

	/**
	 * API to set the 'isReadOnly' field
	 * 
	 * @param isReadOnly the isReadOnly to set
	 */
	public void setReadOnly(boolean isReadOnly) {
		this.readOnly = isReadOnly;
	}

	/**
	 * API to get the 'isReadOnly' field
	 * 
	 * @return the isReadOnly
	 */
	public boolean getReadOnly() {
		return readOnly;
	}

	/**
	 * Sets the kv extraction list.
	 * 
	 * @param kvExtractionList{{@link List} the kvExtractionList to set
	 */
	public void setKvExtractionList(List<KVExtractionDTO> kvExtractionList) {
		this.kvExtractionList = kvExtractionList;
	}

	public boolean isFieldValueChangeScriptEnabled() {
		return fieldValueChangeScriptEnabled;
	}

	public void setFieldValueChangeScriptEnabled(boolean isFieldValueChangeScriptEnabled) {
		this.fieldValueChangeScriptEnabled = isFieldValueChangeScriptEnabled;
	}

	/**
	 * @return the regexListingSeparator
	 */
	public String getRegexListingSeparator() {
		return regexListingSeparator;
	}

	/**
	 * @param regexListingSeparator the regexListingSeparator to set
	 */
	public void setRegexListingSeparator(final String regexListingSeparator) {
		this.regexListingSeparator = regexListingSeparator;
	}

	public float getOcrConfidenceThreshold() {
		return ocrConfidenceThreshold;
	}

	public void setOcrConfidenceThreshold(float ocrConfidenceThreshold) {
		this.ocrConfidenceThreshold = ocrConfidenceThreshold;
	}

	/**
	 * @param widgetType the widgetType to set
	 */
	public void setWidgetType(WidgetType widgetType) {
		this.widgetType = widgetType;
	}

	/**
	 * @return the categoryType
	 */
	public WidgetType getWidgetType() {
		return widgetType;
	}

	@Override
	public boolean isSelected() {
		return isSelected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.isSelected = selected;
	}

	/**
	 * @return the indexFieldMappingList
	 */
	public List<IndexFieldDBExportMappingDTO> getIndexFieldMappingList() {
		List<IndexFieldDBExportMappingDTO> mappingList = null;
		if (null != indexFieldMappingList) {
			mappingList = new ArrayList<IndexFieldDBExportMappingDTO>();
			for (IndexFieldDBExportMappingDTO mappingDTO : indexFieldMappingList) {
				if (null != mappingDTO) {
					if (!mappingDTO.isDeleted()) {
						mappingList.add(mappingDTO);
					}
				}
			}
		}
		return mappingList;
	}

	public List<IndexFieldDBExportMappingDTO> getIndexFieldMappingList(boolean includeDeleted) {
		return includeDeleted ? indexFieldMappingList : getIndexFieldMappingList();
	}

	/**
	 * @param indexFieldMappingList the indexFieldMappingList to set
	 */
	public void setIndexFieldMappingList(List<IndexFieldDBExportMappingDTO> indexFieldMappingList) {
		this.indexFieldMappingList = indexFieldMappingList;
	}

	public void add(IndexFieldDBExportMappingDTO mappingDTO) {
		if (null != mappingDTO) {
			if (null == indexFieldMappingList) {
				indexFieldMappingList = new LinkedList<IndexFieldDBExportMappingDTO>();
			}
			indexFieldMappingList.add(mappingDTO);
		}
	}

	public void remove(IndexFieldDBExportMappingDTO mappingDTO) {
		if (null != mappingDTO && null != indexFieldMappingList) {
			indexFieldMappingList.remove(mappingDTO);
			IndexFieldDBExportMappingDTO newMapping = new IndexFieldDBExportMappingDTO();
			newMapping.setDeleted(true);
			newMapping.setId(mappingDTO.getId());
			newMapping.setNew(mappingDTO.isNew());
			indexFieldMappingList.add(newMapping);
		}

	}

	@Override
	public int compareTo(FieldTypeDTO fieldType) {
		int comparisonResult = 0;
		if (null != fieldType) {
			if (name != null && fieldType.name != null) {
				comparisonResult = name.compareTo(fieldType.name);
			}
		}
		return comparisonResult;
	}

}
