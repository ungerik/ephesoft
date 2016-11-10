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

package com.ephesoft.gxt.core.shared.dto.propertyAccessors;

import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.TableColumnDBExportMappingDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.google.gwt.core.client.GWT;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

public interface TableColumnDBExportMappingProperties extends PropertyAccess<TableColumnDBExportMappingDTO> {

	public static TableColumnDBExportMappingProperties INSTANCE = GWT.create(TableColumnDBExportMappingProperties.class);

	ModelKeyProvider<TableColumnDBExportMappingDTO> id();

	ValueProvider<TableColumnDBExportMappingDTO, String> tableName();

	ValueProvider<TableColumnDBExportMappingDTO, String> columnName();

	ValueProvider<TableColumnDBExportMappingDTO, Boolean> selected();

	public static class TableNameValueProvider implements ValueProvider<TableColumnDBExportMappingDTO, String> {

		public static TableNameValueProvider INSTANCE = new TableNameValueProvider();

		private static final String TABLE_NAME_PATH = "tableNamePath";

		private TableNameValueProvider() {

		}

		@Override
		public String getValue(TableColumnDBExportMappingDTO object) {
			String name = CoreCommonConstant.EMPTY_STRING;
			if (null != object) {
				TableColumnInfoDTO bindedTableColumn = object.getBindedTableColumn();
				if (null != bindedTableColumn) {
					TableInfoDTO tableInfoDTO = bindedTableColumn.getTableInfoDTO();
					name = tableInfoDTO.getName();
				} else {
					name = object.getBindedTableName();
				}
			}
			return name == null ? CoreCommonConstant.EMPTY_STRING : name;
		}

		@Override
		public void setValue(TableColumnDBExportMappingDTO object, String value) {
		}

		@Override
		public String getPath() {
			return TABLE_NAME_PATH;
		}

	}

	public static class TableColumnNameValueProvider implements ValueProvider<TableColumnDBExportMappingDTO, String> {

		public static TableColumnNameValueProvider INSTANCE = new TableColumnNameValueProvider();

		private static final String TABLE_COLUMN_NAME_PATH = "tableColumnNamePath";

		private TableColumnNameValueProvider() {

		}

		@Override
		public String getValue(TableColumnDBExportMappingDTO object) {
			String name = CoreCommonConstant.EMPTY_STRING;
			if (null != object) {
				TableColumnInfoDTO bindedTableColumn = object.getBindedTableColumn();
				if (null != bindedTableColumn) {
					name = bindedTableColumn.getColumnName();
				}
			}
			return name;
		}

		@Override
		public void setValue(TableColumnDBExportMappingDTO object, String value) {
		}

		@Override
		public String getPath() {
			return TABLE_COLUMN_NAME_PATH;
		}

	}
}
