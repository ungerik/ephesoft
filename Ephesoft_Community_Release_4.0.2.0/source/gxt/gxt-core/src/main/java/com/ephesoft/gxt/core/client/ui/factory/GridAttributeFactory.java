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

package com.ephesoft.gxt.core.client.ui.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.ColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.BatchClassColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.BatchClassFieldColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.BatchInstanceColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.BatchListColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.CmisImportColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.ColumnExtractionRuleColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.ConnectionsListColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.DocumentTypeColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.EmailListColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.ExtractedDlfColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.FolderManagerColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.FolderUploadedFilesColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.FunctionKeyColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.FuzzyDBMappingColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.IndexFieldColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.IndexFieldMappingColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.KVExtractionColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.LicenseDetailsColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.OutputDataColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.PluginDependenciesColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.RegerGroupButtonColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.RegerGroupColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.RegerPatternButtonColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.RegerPatternColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.RegexValidationColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.TableColumnExportMappingColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.TableColumnInfoColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.TableExtRuleColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.TableInfoColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.TableRuleColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.TestClassificationColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.UploadBatchColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.ViewLearnFileColumnConfigService;
import com.ephesoft.gxt.core.client.ui.service.columnConfig.impl.WebScannerColumnConfigService;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.filters.Filter;

public class GridAttributeFactory {

	private static HashMap<PropertyAccessModel, ColumnConfigService<?>> columnConfigFactory;

	static {
		columnConfigFactory = new HashMap<PropertyAccessModel, ColumnConfigService<?>>();
	}

	public static <T> List<ColumnConfig<T, ?>> getColumnConfig(final PropertyAccessModel propertyAccessor) {
		final ColumnConfigService<T> configService = GridAttributeFactory.<T> getColumnConfigService(propertyAccessor);
		List<ColumnConfig<T, ?>> columnConfigList = null;
		if (configService != null) {
			columnConfigList = configService == null ? null : configService.getColumnConfig();
		}
		return columnConfigList;
	}

	public static <T> ModelKeyProvider<T> getKeyProvider(final PropertyAccessModel propertyAccessor) {
		final ColumnConfigService<T> configService = GridAttributeFactory.<T> getColumnConfigService(propertyAccessor);
		ModelKeyProvider<T> modelKeyProvider = null;
		if (configService != null) {
			modelKeyProvider = configService == null ? null : configService.getKeyProvider();
		}
		return modelKeyProvider;
	}

	@SuppressWarnings("unchecked")
	private static <T> ColumnConfigService<T> getColumnConfigService(final PropertyAccessModel propertyAccessor) {
		ColumnConfigService<?> columnConfigService = null;
		if (propertyAccessor != null) {
			columnConfigService = columnConfigFactory.get(propertyAccessor);
			if (columnConfigService == null) {
				switch (propertyAccessor) {
					case BATCH_INSTANCE:
						columnConfigService = new BatchInstanceColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.BATCH_INSTANCE, columnConfigService);
						break;
					case BATCH_CLASS:
						columnConfigService = new BatchClassColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.BATCH_CLASS, columnConfigService);
						break;
					case DOCUMENT_TYPE:
						columnConfigService = new DocumentTypeColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.DOCUMENT_TYPE, columnConfigService);
						break;
					case UPLOAD_BATCH:
						columnConfigService = new UploadBatchColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.UPLOAD_BATCH, columnConfigService);
						break;
					case BATCH_LIST:
						columnConfigService = new BatchListColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.BATCH_LIST, columnConfigService);
						break;
					case FOLDER_MANAGER:
						columnConfigService = new FolderManagerColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.FOLDER_MANAGER, columnConfigService);
						break;
					case FOLDER_UPLOADED_FILES:
						columnConfigService = new FolderUploadedFilesColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.FOLDER_UPLOADED_FILES, columnConfigService);
						break;
					case LICENSE_DETAILS:
						columnConfigService = new LicenseDetailsColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.LICENSE_DETAILS, columnConfigService);
						break;
					case EMAIL_LIST:
						columnConfigService = new EmailListColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.EMAIL_LIST, columnConfigService);
						break;
					case CMIS_IMPORT:
						columnConfigService = new CmisImportColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.CMIS_IMPORT, columnConfigService);
						break;
					case WEB_SCANNER:
						columnConfigService = new WebScannerColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.WEB_SCANNER, columnConfigService);
						break;
					case BATCH_CLASS_FIELD:
						columnConfigService = new BatchClassFieldColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.BATCH_CLASS_FIELD, columnConfigService);
						break;
					case FUNCTION_KEY:
						columnConfigService = new FunctionKeyColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.FUNCTION_KEY, columnConfigService);
						break;
					case TABLE_INFO:
						columnConfigService = new TableInfoColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.TABLE_INFO, columnConfigService);
						break;
					case TABLE_COLUMN:
						columnConfigService = new TableColumnInfoColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.TABLE_COLUMN, columnConfigService);
						break;
					case EXTRACTED_DLF:
						columnConfigService = new ExtractedDlfColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.EXTRACTED_DLF, columnConfigService);
						break;
					case PLUGIN_DEPENDENCIES:
						columnConfigService = new PluginDependenciesColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.PLUGIN_DEPENDENCIES, columnConfigService);
						break;
					case CONNECTIONS_LIST:
						columnConfigService = new ConnectionsListColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.CONNECTIONS_LIST, columnConfigService);
						break;
					case TABLE_EXTRACTION_RULE:
						columnConfigService = new TableExtRuleColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.TABLE_EXTRACTION_RULE, columnConfigService);
						break;
					case OUTPUT_DATA_CARRIER:
						columnConfigService = new OutputDataColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.OUTPUT_DATA_CARRIER, columnConfigService);
						break;
					case COLUMN_EXTRACTION_RULE:
						columnConfigService = new ColumnExtractionRuleColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.COLUMN_EXTRACTION_RULE, columnConfigService);
						break;
					case TABLE_RULE:
						columnConfigService = new TableRuleColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.TABLE_RULE, columnConfigService);
						break;
					case KV_EXTRACTION:
						columnConfigService = new KVExtractionColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.KV_EXTRACTION, columnConfigService);
						break;
					case INDEX_FIELDS:
						columnConfigService = new IndexFieldColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.INDEX_FIELDS, columnConfigService);
						break;
					case REGEX_VALIDATION:
						columnConfigService = new RegexValidationColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.REGEX_VALIDATION, columnConfigService);
						break;                                                                                                
					case LEARN_FILES:
						columnConfigService = new ViewLearnFileColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.LEARN_FILES, columnConfigService);
						break;
						
					case REGEX_GROUP:
						columnConfigService = new RegerGroupColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.REGEX_GROUP, columnConfigService);
						break;

					case REGEX_PATTERN:
						columnConfigService = new RegerPatternColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.REGEX_PATTERN, columnConfigService);
						break;

					case TEST_CLASSIFICATION:
						columnConfigService = new TestClassificationColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.TEST_CLASSIFICATION, columnConfigService);
						break;

					case INDEX_FIELD_MAPPING:
						columnConfigService = new IndexFieldMappingColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.INDEX_FIELD_MAPPING, columnConfigService);
						break;

					case REGEX_GROUP_BUTTON:
						columnConfigService = new RegerGroupButtonColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.REGEX_GROUP_BUTTON, columnConfigService);
						break;

					case REGEX_PATTERN_BUTTON:
						columnConfigService = new RegerPatternButtonColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.REGEX_PATTERN_BUTTON, columnConfigService);
						break;

					case FUZZY_DB_MAPPING:
						columnConfigService = new FuzzyDBMappingColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.FUZZY_DB_MAPPING, columnConfigService);
						break;

						
					case TABLE_COLUMN_DB_MAPPING:
						columnConfigService = new TableColumnExportMappingColumnConfigService();
						columnConfigFactory.put(PropertyAccessModel.TABLE_COLUMN_DB_MAPPING, columnConfigService);
						break;
					default:
						columnConfigService = null;
				}
			}
		}
		return columnConfigService == null ? null : (ColumnConfigService<T>) columnConfigService;
	}

	/*
	 * private static <T> ColumnConfigService<T> getColumnConfigService(final PropertyAccessModel propertyAccessor) {
	 * ColumnConfigService<?> columnConfigService = null; if (propertyAccessor != null) { columnConfigService =
	 * columnConfigFactory.get(propertyAccessor); } return columnConfigService == null ? null : (ColumnConfigService<T>)
	 * columnConfigService; }
	 */
	public static <T> List<Filter<T, ?>> getFilters(final PropertyAccessModel propertyAccessor) {
		final ColumnConfigService<T> configService = GridAttributeFactory.<T> getColumnConfigService(propertyAccessor);
		List<Filter<T, ?>> filtersList = null;
		if (configService != null) {
			filtersList = configService == null ? null : configService.getFilters();
		}
		return filtersList;
	}

	@SuppressWarnings("rawtypes")
	public static <T> Map<ColumnConfig<T, Comparable>, IsField<Comparable>> getEditors(final PropertyAccessModel propertyAccessor) {
		Map<ColumnConfig<T, Comparable>, IsField<Comparable>> editorsMap = null;
		final ColumnConfigService<T> configService = GridAttributeFactory.<T> getColumnConfigService(propertyAccessor);
		if (configService != null) {
			editorsMap = configService == null ? null : configService.getEditorsMap();
		}
		return editorsMap;
	}
}
