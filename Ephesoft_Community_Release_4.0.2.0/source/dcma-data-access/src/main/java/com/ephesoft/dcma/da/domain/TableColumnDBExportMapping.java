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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.common.OptionalExportParameters;
import com.ephesoft.dcma.util.CollectionUtil;

@Entity
@Table(name = "table_column_db_export_mapping")
public class TableColumnDBExportMapping extends DatabaseMapping {

	/**
	 * Serialised version UID of the class.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link FieldType} to which DB Export Mapping is binded to.
	 */
	@JoinColumn(name = "table_column_id")
	@ManyToOne
	private TableColumnsInfo bindedColumn;

	/**
	 * {@link List}<{@link OptionalExportParameters}> optionalExportParametersList which are to be exported with field.
	 */
	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "table_column_mapping_id")
	private List<TableColumnOptionalParameters> optionalDBExportParameters = new ArrayList<TableColumnDBExportMapping.TableColumnOptionalParameters>();

	/**
	 * @return the bindedColumn
	 */
	public TableColumnsInfo getBindedColumn() {
		return bindedColumn;
	}

	/**
	 * @param bindedColumn the bindedColumn to set
	 */
	public void setBindedColumn(TableColumnsInfo bindedColumn) {
		this.bindedColumn = bindedColumn;
	}
	
	public void add(TableColumnOptionalParameters optionalParam) {
		if(null != optionalParam) {
			optionalDBExportParameters.add(optionalParam);
		}
	}

	public List<TableColumnOptionalParameters> getOptionalDBExportParameters() {
		return optionalDBExportParameters;
	}

	
	public void setOptionalDBExportParameters(List<TableColumnOptionalParameters> optionalDBExportParameters) {
		this.optionalDBExportParameters = optionalDBExportParameters;
	}
	
	public void removeOptionalParamById(final long id) {
		if (!CollectionUtil.isEmpty(optionalDBExportParameters)) {
			for (TableColumnOptionalParameters tableOptionalParam : optionalDBExportParameters) {
				if (null != tableOptionalParam && tableOptionalParam.getId() == id) {
					optionalDBExportParameters.remove(tableOptionalParam);
					break;
				}
			}
		}
	}
	
	public TableColumnOptionalParameters getOptionalParamById(final long id) {
		TableColumnOptionalParameters bindedMapping = null;
		if (!CollectionUtil.isEmpty(optionalDBExportParameters)) {
			for (TableColumnOptionalParameters tableOptionalParam : optionalDBExportParameters) {
				if (null != tableOptionalParam && tableOptionalParam.getId() == id) {
					bindedMapping = tableOptionalParam;
					break;
				}
			}
		}
		return bindedMapping;
	}



	@Entity
	@Table(name = "table_columns_optional_paramter")
	@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
	public static class TableColumnOptionalParameters extends DBExportOptionalParameters {

		private static final long serialVersionUID = 1L;
		
		@ManyToOne
		@JoinColumn(name = "table_column_mapping_id")
		private TableColumnDBExportMapping tableColumnMapping;
		
		/**
		 * @return the tableColumnMapping
		 */
		public TableColumnDBExportMapping getTableColumnMapping() {
			return tableColumnMapping;
		}
		
		/**
		 * @param tableColumnMapping the tableColumnMapping to set
		 */
		public void setTableColumnMapping(TableColumnDBExportMapping tableColumnMapping) {
			this.tableColumnMapping = tableColumnMapping;
		}
	}
}
