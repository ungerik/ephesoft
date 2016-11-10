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
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.gxt.core.shared.dto.IndexFieldDBExportMappingDTO.IndexFieldOptionalParametersDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnDBExportMappingDTO.TableColumnOptionalParametersDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.dcma.core.common.OptionalExportParameters;

public class TableColumnDBExportMappingDTO extends DatabaseMappingDTO implements Selectable {

	private TableColumnInfoDTO bindedTableColumn;

	private List<TableColumnOptionalParametersDTO> optionalDBExportParameters;

	private boolean selected;

	private boolean isNew;

	private boolean deleted;

	private String bindedTableName;

	private long id;

	public TableColumnInfoDTO getBindedTableColumn() {
		return bindedTableColumn;
	}

	public void setBindedTableColumn(TableColumnInfoDTO bindedTableColumn) {
		if (null != this.bindedTableColumn) {
			this.bindedTableColumn.remove(this);
		}
		if (null != bindedTableColumn) {
			bindedTableColumn.add(this);
		}
		this.bindedTableColumn = bindedTableColumn;

	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<TableColumnOptionalParametersDTO> getOptionalDBExportParameters() {
		List<TableColumnOptionalParametersDTO> optionalParamsList = new ArrayList<TableColumnOptionalParametersDTO>();
		if (!CollectionUtil.isEmpty(optionalDBExportParameters)) {
			for (final TableColumnOptionalParametersDTO mappingDTO : optionalDBExportParameters) {
				if (null != mappingDTO && !mappingDTO.isDeleted()) {
					optionalParamsList.add(mappingDTO);
				}
			}
		}
		return optionalParamsList;
	}

	public void add(TableColumnOptionalParametersDTO optionalDTO) {
		if (null == optionalDBExportParameters) {
			optionalDBExportParameters = new LinkedList<TableColumnDBExportMappingDTO.TableColumnOptionalParametersDTO>();
		}
		optionalDBExportParameters.add(optionalDTO);
	}

	public List<TableColumnOptionalParametersDTO> getOptionalDBExportParameters(boolean includeDeleted) {
		return includeDeleted ? optionalDBExportParameters : getOptionalDBExportParameters();
	}

	public void setOptionalDBExportParameters(List<TableColumnOptionalParametersDTO> optionalDBExportParameters) {
		this.optionalDBExportParameters = optionalDBExportParameters;
	}

	public void removeOptionalParamById(final long id) {
		if (!CollectionUtil.isEmpty(optionalDBExportParameters)) {
			for (TableColumnOptionalParametersDTO tableOptionalParam : optionalDBExportParameters) {
				if (null != tableOptionalParam && tableOptionalParam.getId() == id) {
					optionalDBExportParameters.remove(tableOptionalParam);
					break;
				}
			}
		}
	}

	public TableColumnOptionalParametersDTO getOtionalParamById(final long id) {
		TableColumnOptionalParametersDTO bindedMapping = null;
		if (!CollectionUtil.isEmpty(optionalDBExportParameters)) {
			for (TableColumnOptionalParametersDTO tableOptionalParam : optionalDBExportParameters) {
				if (null != tableOptionalParam && tableOptionalParam.getId() == id) {
					bindedMapping = tableOptionalParam;
					break;
				}
			}
		}
		return bindedMapping;
	}

	public TableColumnOptionalParametersDTO getMapping(OptionalExportParameters optionalParam) {
		TableColumnOptionalParametersDTO bindedOptionalParam = null;
		if (null != optionalParam && !CollectionUtil.isEmpty(optionalDBExportParameters)) {
			for (TableColumnOptionalParametersDTO traversedParam : optionalDBExportParameters) {
				if (null != traversedParam && !traversedParam.isDeleted()
						&& optionalParam == traversedParam.getOptionalExportParamter()) {
					bindedOptionalParam = traversedParam;
					break;
				}
			}
		}
		return bindedOptionalParam;
	}

	public String getBindedTableName() {
		return bindedTableName;
	}

	public void setBindedTableName(String tableName) {
		this.bindedTableName = tableName;
	}

	public void clearOptionalParams() {
		if (!CollectionUtil.isEmpty(optionalDBExportParameters)) {
			for (final TableColumnOptionalParametersDTO exportParam : optionalDBExportParameters) {
				if (null != exportParam) {
					exportParam.setDeleted(true);
				}
			}
		}
	}

	public static class TableColumnOptionalParametersDTO extends DBExportOptionalParametersDTO {

		private TableColumnDBExportMappingDTO tableColumnMapping;

		public TableColumnDBExportMappingDTO getTableColumnMapping() {
			return tableColumnMapping;
		}

		public void setTableColumnMapping(TableColumnDBExportMappingDTO tableColumnMapping) {
			this.tableColumnMapping = tableColumnMapping;
		}

	}

}
