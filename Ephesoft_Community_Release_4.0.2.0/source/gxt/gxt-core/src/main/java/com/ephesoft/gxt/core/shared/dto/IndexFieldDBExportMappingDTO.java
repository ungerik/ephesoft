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
import java.util.List;

import com.ephesoft.dcma.core.common.OptionalExportParameters;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;

public class IndexFieldDBExportMappingDTO extends DatabaseMappingDTO implements Selectable {

	/**
	 * {@link FieldTypeDTO} to which DB Export Mapping is binded to.
	 */
	private FieldTypeDTO bindedField;

	/**
	 * {@link List}<{@link OptionalExportParameters}> optionalExportParametersList which are to be exported with field.
	 */
	private List<IndexFieldOptionalParametersDTO> optionalDBExportParameters;

	private boolean selected;

	private boolean isNew;

	private boolean deleted;

	private long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the bindedField
	 */
	public FieldTypeDTO getBindedField() {
		return bindedField;
	}

	/**
	 * @param bindedField the bindedField to set
	 */
	public void setBindedField(FieldTypeDTO bindedField) {
		this.bindedField = bindedField;
	}

	public void addOptionalParamter(final IndexFieldOptionalParametersDTO optionalParamter) {
		if (null != optionalParamter) {
			if (null == optionalDBExportParameters) {
				optionalDBExportParameters = new ArrayList<IndexFieldDBExportMappingDTO.IndexFieldOptionalParametersDTO>();
			}
			optionalParamter.setIndexFieldMapping(this);
			optionalDBExportParameters.add(optionalParamter);
		}
	}

	public boolean containsOptionalParameter(final OptionalExportParameters optionalParameter, String value) {
		IndexFieldOptionalParametersDTO param = getIndexFieldOptionalParameter(optionalParameter, value);
		return param != null;
	}

	public void remove(final OptionalExportParameters optionalParameter, String value) {
		IndexFieldOptionalParametersDTO param = getIndexFieldOptionalParameter(optionalParameter, value);
		if (!CollectionUtil.isEmpty(optionalDBExportParameters) && null != param) {
			param.setDeleted(true);
		}
	}

	private IndexFieldOptionalParametersDTO getIndexFieldOptionalParameter(final OptionalExportParameters optionalParameter,
			String value) {
		IndexFieldOptionalParametersDTO param = null;
		if (!CollectionUtil.isEmpty(optionalDBExportParameters) && null != optionalParameter) {
			for (IndexFieldOptionalParametersDTO exportParam : optionalDBExportParameters) {
				if (null != exportParam && !exportParam.isDeleted()) {
					if (exportParam.getOptionalExportParamter() == optionalParameter) {
						param = exportParam;
						break;
					}
				}
			}
		}
		return param;
	}

	/**
	 * @return the optionalDBExportParameters
	 */
	public List<IndexFieldOptionalParametersDTO> getOptionalDBExportParameters(boolean includeDeleted) {
		return includeDeleted ? optionalDBExportParameters : getOptionalDBExportParameters();
	}

	public List<IndexFieldOptionalParametersDTO> getOptionalDBExportParameters() {
		List<IndexFieldOptionalParametersDTO> mappingList = null;
		if (null != optionalDBExportParameters) {
			mappingList = new ArrayList<IndexFieldOptionalParametersDTO>();
			for (IndexFieldOptionalParametersDTO mappingDTO : optionalDBExportParameters) {
				if (null != mappingDTO) {
					if (!mappingDTO.isDeleted()) {
						mappingList.add(mappingDTO);
					}
				}
			}
		}
		return mappingList;
	}

	/**
	 * @param optionalDBExportParameters the optionalDBExportParameters to set
	 */
	public void setOptionalDBExportParameters(List<IndexFieldOptionalParametersDTO> optionalDBExportParameters) {
		this.optionalDBExportParameters = optionalDBExportParameters;
	}

	public static class IndexFieldOptionalParametersDTO extends DBExportOptionalParametersDTO {

		private IndexFieldDBExportMappingDTO indexFieldMapping;

		/**
		 * @return the tableColumnMapping
		 */
		public IndexFieldDBExportMappingDTO getIndexFieldMapping() {
			return indexFieldMapping;
		}

		/**
		 * @param indexFieldMapping the tableColumnMapping to set
		 */
		public void setIndexFieldMapping(IndexFieldDBExportMappingDTO indexFieldMapping) {
			this.indexFieldMapping = indexFieldMapping;
		}
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * @return the isDeleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the isDeleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public IndexFieldOptionalParametersDTO getMapping(final OptionalExportParameters exportParameter, final String value) {
		IndexFieldOptionalParametersDTO newParam = null;
		if (!CollectionUtil.isEmpty(optionalDBExportParameters)) {
				for (IndexFieldOptionalParametersDTO param : optionalDBExportParameters) {
					if (param != null && !param.isDeleted()) {
						OptionalExportParameters optionalExportParamter = param.getOptionalExportParamter();
						if (optionalExportParamter == exportParameter) {
							newParam = param;
							break;
						}
					}
				}
		}
		return newParam;
	}

	public void clearOptionalParams() {
		if (!CollectionUtil.isEmpty(optionalDBExportParameters)) {
			for (final IndexFieldOptionalParametersDTO exportParam : optionalDBExportParameters) {
				if (null != exportParam) {
					exportParam.setDeleted(true);
				}
			}
		}
	}
}
