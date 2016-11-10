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

package com.ephesoft.gxt.core.shared.util;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.client.RandomIdGenerator;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.shared.dto.AdvancedKVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FunctionKeyDTO;
import com.ephesoft.gxt.core.shared.dto.IndexFieldDBExportMappingDTO;
import com.ephesoft.gxt.core.shared.dto.IndexFieldDBExportMappingDTO.IndexFieldOptionalParametersDTO;
import com.ephesoft.gxt.core.shared.dto.KVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.RegexDTO;
import com.ephesoft.gxt.core.shared.dto.RuleInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnInfoDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionAPIModel;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;

/**
 * Provides utility methods for creating the replicas of DTOs and list of DTOs.
 * <p>
 * The <code>DTOUtil</code> class defines methods for making copies of KVExtractionDTO, RegexDTO and list of such DTOs.
 * 
 * @author Ephesoft
 * 
 *         <b> created on </b> Jul 1, 2013 <br/>
 * @version 1.0
 * 
 *          $LastChanged:Jul 1, 2013<br/>
 *          $LastChangedRevision:Jul 1, 2013<br/>
 */
public class DTOUtil {

	/**
	 * Creates the copy of a KVExtractionDTO and returns it.
	 * 
	 * @param fieldTypeDTO {@link FieldTypeDTO} the fieldType DTO of given kvExtration DTO
	 * @param kvExtractionDTO {@link KVExtractionDTO} the kv Extraction to be copied
	 * @return {@link KVExtractionDTO} new kv extraction DTO
	 */
	private static KVExtractionDTO createKVExtractionDTO(final FieldTypeDTO fieldTypeDTO, final KVExtractionDTO kvExtractionDTO) {
		KVExtractionDTO newKVExtractionDTO = null;
		if (kvExtractionDTO != null) {
			newKVExtractionDTO = new KVExtractionDTO();
			newKVExtractionDTO.setFieldTypeDTO(fieldTypeDTO);
			newKVExtractionDTO.setIdentifier(kvExtractionDTO.getIdentifier());
			newKVExtractionDTO.setKeyPattern(kvExtractionDTO.getKeyPattern());
			newKVExtractionDTO.setLocationType(kvExtractionDTO.getLocationType());
			newKVExtractionDTO.setValuePattern(kvExtractionDTO.getValuePattern());
			newKVExtractionDTO.setNoOfWords(kvExtractionDTO.getNoOfWords());
			newKVExtractionDTO.setLength(kvExtractionDTO.getLength());
			newKVExtractionDTO.setWidth(kvExtractionDTO.getWidth());
			newKVExtractionDTO.setXoffset(kvExtractionDTO.getXoffset());
			newKVExtractionDTO.setYoffset(kvExtractionDTO.getYoffset());
			newKVExtractionDTO.setFetchValue(kvExtractionDTO.getFetchValue());
			newKVExtractionDTO.setKvPageValue(kvExtractionDTO.getKvPageValue());
			newKVExtractionDTO.setUseExistingKey(kvExtractionDTO.isUseExistingKey());
			newKVExtractionDTO.setWeight(kvExtractionDTO.getWeight());

			// Setting the fuzzy match threshold value for key pattern.
			newKVExtractionDTO.setKeyFuzziness(kvExtractionDTO.getKeyFuzziness());

			newKVExtractionDTO.setOrderNumber(kvExtractionDTO.getOrderNumber());
			newKVExtractionDTO.setNew(true);
			AdvancedKVExtractionDTO advancedKVExtractionDTO = kvExtractionDTO.getAdvancedKVExtractionDTO();
			if (advancedKVExtractionDTO != null) {
				newKVExtractionDTO.setAdvancedKVExtractionDTO(createAdvancedKVExtractionDTO(newKVExtractionDTO,
						advancedKVExtractionDTO));
			}
		}
		return newKVExtractionDTO;
	}

	/**
	 * Creates the copy of a AdvancedKVExtractionDTO and returns it.
	 * 
	 * @param newKVExtractionDTO {@link KVExtractionDTO} the kv Extraction DTO of advanced kv extraction DTO
	 * @param advancedKVExtractionDTO {@link AdvancedKVExtractionDTO} the advance kv extraction DTO to copy
	 * @return {@link KVExtractionDTO} new advance kv extraction DTO
	 */
	public static AdvancedKVExtractionDTO createAdvancedKVExtractionDTO(final KVExtractionDTO newKVExtractionDTO,
			final AdvancedKVExtractionDTO advancedKVExtractionDTO) {
		AdvancedKVExtractionDTO newAdvancedKVExtractionDTO = null;
		if (advancedKVExtractionDTO != null) {
			newAdvancedKVExtractionDTO = new AdvancedKVExtractionDTO();
			// newAdvancedKVExtractionDTO.setImageName(advancedKVExtractionDTO.getImageName());
			// newAdvancedKVExtractionDTO.setDisplayImageName(advancedKVExtractionDTO.getDisplayImageName());
			newAdvancedKVExtractionDTO.setKeyX0Coord(advancedKVExtractionDTO.getKeyX0Coord());
			newAdvancedKVExtractionDTO.setKeyY0Coord(advancedKVExtractionDTO.getKeyY0Coord());
			newAdvancedKVExtractionDTO.setKeyX1Coord(advancedKVExtractionDTO.getKeyX1Coord());
			newAdvancedKVExtractionDTO.setKeyY1Coord(advancedKVExtractionDTO.getKeyY1Coord());
			newAdvancedKVExtractionDTO.setValueX0Coord(advancedKVExtractionDTO.getValueX0Coord());
			newAdvancedKVExtractionDTO.setValueY0Coord(advancedKVExtractionDTO.getValueY0Coord());
			newAdvancedKVExtractionDTO.setValueX1Coord(advancedKVExtractionDTO.getValueX1Coord());
			newAdvancedKVExtractionDTO.setValueY1Coord(advancedKVExtractionDTO.getValueY1Coord());
		}
		return newAdvancedKVExtractionDTO;
	}

	/**
	 * Copies the list of kv extraction DTOs and returns it.
	 * 
	 * @param kvExtractionList {@link List} list of kv extraction DTOs to be copied
	 * @return {@link List} list of new kv extraction DTOs
	 */
	public static List<KVExtractionDTO> createKVExtractionDTOList(final List<KVExtractionDTO> kvExtractionList) {
		List<KVExtractionDTO> newKvExtractionList = new ArrayList<KVExtractionDTO>();
		if (!CollectionUtil.isEmpty(kvExtractionList)) {
//			newKvExtractionList = new ArrayList<KVExtractionDTO>();
			for (KVExtractionDTO kvExtractionDTO : kvExtractionList) {
				KVExtractionDTO newKvExtractionDTO = createKVExtractionDTO(kvExtractionDTO.getFieldTypeDTO(), kvExtractionDTO);
				newKvExtractionList.add(newKvExtractionDTO);
			}
		}
		return newKvExtractionList;
	}

	/**
	 * Creates the copy of a RegexDTO and returns it.
	 * 
	 * @param fieldTypeDTO {@link FieldTypeDTO} the fieldType DTO of given regular expression DTO
	 * @param regexDTO {@link RegexDTO} the regular expression to be copied
	 * @return {@link RegexDTO} new regex DTO
	 */
	private static RegexDTO createRegexDTO(final FieldTypeDTO fieldTypeDTO, final RegexDTO regexDTO) {
		RegexDTO newRegexDTO = null;
		if (regexDTO != null) {
			newRegexDTO = new RegexDTO();
			newRegexDTO.setPattern(regexDTO.getPattern());
			newRegexDTO.setIdentifier(String.valueOf(regexDTO.getIdentifier()));
			newRegexDTO.setFieldTypeDTO(fieldTypeDTO);
			newRegexDTO.setNew(true);
		}
		return newRegexDTO;
	}

	/**
	 * Copies the list of regex DTOs and returns it.
	 * 
	 * @param regexList {@link List} list of regular expression DTOs to be copied
	 * @return {@link List} list of new regular expression DTOs
	 */
	public static List<RegexDTO> createRegExDTOList(final List<RegexDTO> regexList) {
		List<RegexDTO> newRegexDTOList = new ArrayList<RegexDTO>();
		if (!CollectionUtil.isEmpty(regexList)) {
//			newRegexDTOList = new ArrayList<RegexDTO>();
			for (RegexDTO regexDTO : regexList) {
				RegexDTO newRegexDTO = createRegexDTO(regexDTO.getFieldTypeDTO(), regexDTO);
				newRegexDTOList.add(newRegexDTO);
			}
		}
		return newRegexDTOList;
	}

	/**
	 * Creates the copy of a FieldTypeDTO and returns it.
	 * 
	 * @param documentTypeDTO {@link DocumentTypeDTO} the document type DTO of given field type DTO
	 * @param fieldTypeDTO {@link FieldTypeDTO} the field type to copy.
	 * @return {@link FieldTypeDTO} new field type DTO
	 */
	public static FieldTypeDTO createFieldTypeDTO(final DocumentTypeDTO documentTypeDTO, final FieldTypeDTO fieldTypeDTO) {
		FieldTypeDTO newFieldTypeDTO = null;
		if (fieldTypeDTO != null) {
			newFieldTypeDTO = new FieldTypeDTO();
			newFieldTypeDTO.setNew(true);
			newFieldTypeDTO.setBarcodeType(fieldTypeDTO.getBarcodeType());
			newFieldTypeDTO.setDataType(fieldTypeDTO.getDataType());
			newFieldTypeDTO.setDescription(fieldTypeDTO.getDescription());
			newFieldTypeDTO.setDocTypeDTO(documentTypeDTO);
			newFieldTypeDTO.setFieldOptionValueList(fieldTypeDTO.getFieldOptionValueList());
			newFieldTypeDTO.setFieldOrderNumber(fieldTypeDTO.getFieldOrderNumber());
			newFieldTypeDTO.setFieldValueChangeScriptEnabled(fieldTypeDTO.isFieldValueChangeScriptEnabled());
			newFieldTypeDTO.setHidden(fieldTypeDTO.isHidden());
			newFieldTypeDTO.setIdentifier(fieldTypeDTO.getIdentifier());
			newFieldTypeDTO.setKvExtractionList(createKVExtractionDTOList(fieldTypeDTO.getKvExtractionList()));
			newFieldTypeDTO.setCategory(fieldTypeDTO.getCategory());
			newFieldTypeDTO.setWidgetType(fieldTypeDTO.getWidgetType());
			newFieldTypeDTO.setName(fieldTypeDTO.getName());
			newFieldTypeDTO.setPattern(fieldTypeDTO.getPattern());
			newFieldTypeDTO.setReadOnly(fieldTypeDTO.getReadOnly());
			newFieldTypeDTO.setRegexList(createRegExDTOList(fieldTypeDTO.getRegexList()));
			newFieldTypeDTO.setRegexListingSeparator(fieldTypeDTO.getRegexListingSeparator());
			newFieldTypeDTO.setRegexPatternList(fieldTypeDTO.getRegexPatternList());
			newFieldTypeDTO.setSampleValue(fieldTypeDTO.getSampleValue());
			newFieldTypeDTO.setOcrConfidenceThreshold(fieldTypeDTO.getOcrConfidenceThreshold());
			List<IndexFieldDBExportMappingDTO> indexFieldMappingList = fieldTypeDTO.getIndexFieldMappingList();
			if (!CollectionUtil.isEmpty(indexFieldMappingList)) {
				for (IndexFieldDBExportMappingDTO mapping : indexFieldMappingList) {
					if (null != mapping) {
						IndexFieldDBExportMappingDTO newMapping = copyIndexFieldMappingDTO(newFieldTypeDTO, mapping);
						newFieldTypeDTO.add(newMapping);
					}
				}
			}
		}
		return newFieldTypeDTO;
	}

	private static IndexFieldDBExportMappingDTO copyIndexFieldMappingDTO(final FieldTypeDTO fieldTypeDTO,
			final IndexFieldDBExportMappingDTO mappingDTO) {
		IndexFieldDBExportMappingDTO newMappingDTO = new IndexFieldDBExportMappingDTO();
		newMappingDTO.setBindedField(fieldTypeDTO);
		newMappingDTO.setColumnName(mappingDTO.getColumnName());
		newMappingDTO.setDeleted(false);
		newMappingDTO.setId(RandomIdGenerator.getIdentifier());
		newMappingDTO.setTableName(mappingDTO.getTableName());
		newMappingDTO.setNew(true);
		List<IndexFieldOptionalParametersDTO> optionalParamsList = mappingDTO.getOptionalDBExportParameters(false);
		if (!CollectionUtil.isEmpty(optionalParamsList)) {
			for (IndexFieldOptionalParametersDTO optionalParam : optionalParamsList) {
				if (null != optionalParam) {
					newMappingDTO.addOptionalParamter(createIndexFieldOptionalParams(newMappingDTO, optionalParam));
				}
			}
		}
		return newMappingDTO;
	}

	private static IndexFieldOptionalParametersDTO createIndexFieldOptionalParams(final IndexFieldDBExportMappingDTO mappingDTO,
			final IndexFieldOptionalParametersDTO optionalParamsDTO) {
		IndexFieldOptionalParametersDTO newOptionalParams = new IndexFieldOptionalParametersDTO();
		newOptionalParams.setIndexFieldMapping(mappingDTO);
		newOptionalParams.setColumnName(optionalParamsDTO.getColumnName());
		newOptionalParams.setOptionalExportParamter(optionalParamsDTO.getOptionalExportParamter());
		newOptionalParams.setNew(true);
		newOptionalParams.setId(RandomIdGenerator.getIdentifier());
		newOptionalParams.setDeleted(false);
		return newOptionalParams;
	}

	/**
	 * Creates copy of given function key dto and returns it.
	 * 
	 * @param documentTypeDTO {@link DocumentTypeDTO} the document dto of new function key dto.
	 * @param functionKeyDTO {@link FunctionKeyDTO} the function key dto to be copied.
	 * @return {@link FunctionKeyDTO} function key dto created after copying the given function key dto.
	 */
	public static FunctionKeyDTO createFunctionKeyDTO(final DocumentTypeDTO documentTypeDTO, final FunctionKeyDTO functionKeyDTO) {
		FunctionKeyDTO newFunctionKeyDTO = null;
		if (functionKeyDTO != null) {
			newFunctionKeyDTO = new FunctionKeyDTO();
			newFunctionKeyDTO.setNew(true);
			newFunctionKeyDTO.setDocTypeDTO(documentTypeDTO);
			newFunctionKeyDTO.setIdentifier(functionKeyDTO.getIdentifier());
			newFunctionKeyDTO.setMethodDescription(functionKeyDTO.getMethodDescription());
			newFunctionKeyDTO.setMethodName(functionKeyDTO.getMethodName());
			newFunctionKeyDTO.setShortcutKeyName(functionKeyDTO.getShortcutKeyName());
		}
		return newFunctionKeyDTO;
	}

	/**
	 * Creates copy of given table info dto and returns it.
	 * 
	 * @param documentTypeDTO {@link DocumentTypeDTO} the document dto of new function key dto.
	 * @param tableInfoDTO {@link TableInfoDTO} the table info dto to copy.
	 * @return {@link TableInfoDTO} table info dto created after copying the given table info dto.
	 */
	public static TableInfoDTO createTableInfoDTO(final DocumentTypeDTO documentTypeDTO, final TableInfoDTO tableInfoDTO) {
		TableInfoDTO newTableInfoDTO = null;
		if (tableInfoDTO != null) {
			newTableInfoDTO = new TableInfoDTO();
			newTableInfoDTO.setDisplayImage(tableInfoDTO.getDisplayImage());
			newTableInfoDTO.setDocTypeDTO(documentTypeDTO);
			newTableInfoDTO.setIdentifier(tableInfoDTO.getIdentifier());
			newTableInfoDTO.setName(tableInfoDTO.getName());
			newTableInfoDTO.setNew(true);
			newTableInfoDTO.setNumberOfRows(tableInfoDTO.getNumberOfRows());
			newTableInfoDTO.setRemoveInvalidRows(tableInfoDTO.isRemoveInvalidRows());
			newTableInfoDTO.setRuleOperator(tableInfoDTO.getRuleOperator());
			newTableInfoDTO.setColumnInfoDTOs(copyTableColumnDTOList(newTableInfoDTO, tableInfoDTO.getTableColumnInfoList()));
			newTableInfoDTO.setRuleDTOs(createTableRuleDTOList(newTableInfoDTO, tableInfoDTO.getRuleInfoDTOs()));
			newTableInfoDTO.setCurrencyCode(tableInfoDTO.getCurrencyCode());

			// Setting Table Extraction rule DTOs to the new Table Info DTO.
			newTableInfoDTO.setTableExtractionRuleDTOs(copyTableExtractionRuleDTOList(newTableInfoDTO,
					tableInfoDTO.getTableExtractionRuleDTOs()));
		}
		return newTableInfoDTO;
	}

	/**
	 * Copies the list of table rule DTOs and returns it.
	 * 
	 * @param ruleInfoDTOs {@link List} list of field type DTOs to be copied.
	 * @param newTableInfoDTO {@link TableInfoDTO} the table info dto in which the new list of table rule dtos will be copied.
	 * @return {@link List} list of new table rule DTOs.
	 */
	private static List<RuleInfoDTO> createTableRuleDTOList(final TableInfoDTO newTableInfoDTO, final List<RuleInfoDTO> ruleInfoDTOs) {
		List<RuleInfoDTO> tableRuleInfoDTOs = null;
		if (!CollectionUtil.isEmpty(ruleInfoDTOs)) {
			tableRuleInfoDTOs = new ArrayList<RuleInfoDTO>();
			for (RuleInfoDTO ruleInfoDTO : ruleInfoDTOs) {
				RuleInfoDTO newRuleInfoDTO = createRuleInfoDTO(newTableInfoDTO, ruleInfoDTO);
				tableRuleInfoDTOs.add(newRuleInfoDTO);
			}
		}
		return tableRuleInfoDTOs;
	}

	/**
	 * Creates copy of given table rule dto, adds it to the given table info dto and returns it.
	 * 
	 * @param newTableInfoDTO {@link TableInfoDTO} the table info dto of new table rule dto.
	 * @param ruleInfoDTO {@link RuleInfoDTO} the table rule dto to be copied.
	 * @return {@link RuleInfoDTO} table rule dto created after copying the given table rule dto.
	 */
	private static RuleInfoDTO createRuleInfoDTO(final TableInfoDTO newTableInfoDTO, final RuleInfoDTO ruleInfoDTO) {
		RuleInfoDTO newRuleInfoDTO = null;
		if (ruleInfoDTO != null) {
			newRuleInfoDTO = new RuleInfoDTO();
			newRuleInfoDTO.setDescription(ruleInfoDTO.getDescription());
			newRuleInfoDTO.setIdentifier(ruleInfoDTO.getIdentifier());
			newRuleInfoDTO.setNew(true);
			newRuleInfoDTO.setRule(ruleInfoDTO.getRule());
			newRuleInfoDTO.setTableInfoDTO(newTableInfoDTO);
		}
		return newRuleInfoDTO;
	}

	/**
	 * Copies the list of table column DTOs and returns it.
	 * 
	 * @param tableColumnInfoList {@link List} list of table column DTOs to be copied.
	 * @param tableInfoDTO {@link TableInfoDTO} the table info dto in which the new list of table column dtos will be copied.
	 * @return {@link List} list of new table column DTOs.
	 */
	private static List<TableColumnInfoDTO> copyTableColumnDTOList(final TableInfoDTO tableInfoDTO,
			final List<TableColumnInfoDTO> tableColumnInfoList) {
		List<TableColumnInfoDTO> tableColumnInfoDTOs = null;
		if (!CollectionUtil.isEmpty(tableColumnInfoList)) {
			tableColumnInfoDTOs = new ArrayList<TableColumnInfoDTO>();
			for (TableColumnInfoDTO columnInfoDTO : tableColumnInfoList) {
				TableColumnInfoDTO newColumnInfoDTO = copyColumnInfoDTO(tableInfoDTO, columnInfoDTO);
				tableColumnInfoDTOs.add(newColumnInfoDTO);
			}
		}
		return tableColumnInfoDTOs;
	}

	/**
	 * Creates copy of given table column dto, adds it to the given table info dto and returns it.
	 * 
	 * @param tableInfoDTO {@link TableInfoDTO} the table info dto of new table column dto.
	 * @param columnInfoDTO {@link TableColumnInfoDTO} the table column dto to copy.
	 * @return {@link TableColumnInfoDTO} table column dto created after copying the given table column dto.
	 */
	private static TableColumnInfoDTO copyColumnInfoDTO(final TableInfoDTO tableInfoDTO, final TableColumnInfoDTO columnInfoDTO) {
		TableColumnInfoDTO newTableColumnInfoDTO = null;
		if (columnInfoDTO != null) {
			newTableColumnInfoDTO = new TableColumnInfoDTO();

			// Removed all column extraction deatils from column info and added desciption.
			newTableColumnInfoDTO.setColumnName(columnInfoDTO.getColumnName());
			newTableColumnInfoDTO.setIdentifier(columnInfoDTO.getIdentifier());
			newTableColumnInfoDTO.setColumnDescription(columnInfoDTO.getColumnDescription());
			newTableColumnInfoDTO.setValidationPattern(columnInfoDTO.getValidationPattern());
			newTableColumnInfoDTO.setAlternateValues(columnInfoDTO.getAlternateValues());
			newTableColumnInfoDTO.setNew(true);
			newTableColumnInfoDTO.setTableInfoDTO(tableInfoDTO);
		}
		return newTableColumnInfoDTO;
	}

	/**
	 * Copies list of table extraction rules to a new list of table extraction rules for a new parent of those table extraction rules.
	 * 
	 * @param newTableInfoDTO {@link TableInfoDTO}
	 * @param listTableExtractionRuleDTOs {@link List<{@link TableExtractionRuleDTO}>}
	 * @return {@link List<{@link TableExtractionRuleDTO}>}
	 */
	private static List<TableExtractionRuleDTO> copyTableExtractionRuleDTOList(final TableInfoDTO newTableInfoDTO,
			final List<TableExtractionRuleDTO> listTableExtractionRuleDTOs) {
		List<TableExtractionRuleDTO> tableExtractionRuleDTOs = null;
		if (!CollectionUtil.isEmpty(listTableExtractionRuleDTOs)) {
			tableExtractionRuleDTOs = new ArrayList<TableExtractionRuleDTO>(listTableExtractionRuleDTOs.size());
			for (TableExtractionRuleDTO tableExtractionRuleDTO : listTableExtractionRuleDTOs) {
				TableExtractionRuleDTO newTableExtractionRuleDTO = copyTableExtractionRuleDTO(newTableInfoDTO, tableExtractionRuleDTO);
				if (null != newTableExtractionRuleDTO) {
					tableExtractionRuleDTOs.add(newTableExtractionRuleDTO);
				}
			}
		}
		return tableExtractionRuleDTOs;
	}

	/**
	 * Copies Table extraction rule DTO to a new DTO and attaches it to its new parent.
	 * 
	 * @param newTableInfoDTO {@link TableInfoDTO}
	 * @param tableExtractionRuleDTO {@link TableExtractionRuleDTO}
	 * @return {@link TableExtractionRuleDTO}
	 */
	private static TableExtractionRuleDTO copyTableExtractionRuleDTO(final TableInfoDTO newTableInfoDTO,
			final TableExtractionRuleDTO tableExtractionRuleDTO) {
		TableExtractionRuleDTO newTableExtractionRuleDTO = null;
		if (tableExtractionRuleDTO != null) {
			newTableExtractionRuleDTO = new TableExtractionRuleDTO();
			newTableExtractionRuleDTO.setIdentifier(tableExtractionRuleDTO.getIdentifier());
			newTableExtractionRuleDTO.setNew(true);
			newTableExtractionRuleDTO.setTableAPI(tableExtractionRuleDTO.getTableAPI());
			newTableExtractionRuleDTO.setStartPattern(tableExtractionRuleDTO.getStartPattern());
			newTableExtractionRuleDTO.setEndPattern(tableExtractionRuleDTO.getEndPattern());
			newTableExtractionRuleDTO.setRuleName(tableExtractionRuleDTO.getRuleName());
			setTableExtractionAPIModel(newTableExtractionRuleDTO, tableExtractionRuleDTO);
			newTableExtractionRuleDTO.setTableColumnExtractionRuleDTOs(copyTableColumnExtractionRuleDTO(
					tableExtractionRuleDTO.getTableColumnExtractionRuleDTOs(), tableExtractionRuleDTO, newTableInfoDTO));
			newTableExtractionRuleDTO.setTableInfoDTO(newTableInfoDTO);
		}
		return newTableExtractionRuleDTO;
	}

	private static void setTableExtractionAPIModel(TableExtractionRuleDTO newTableExtractionRuleDTO,
			TableExtractionRuleDTO tableExtractionRuleDTO) {
		TableExtractionAPIModel newExtAPIModel = new TableExtractionAPIModel();
		if (null != tableExtractionRuleDTO) {
			TableExtractionAPIModel extAPIModel = tableExtractionRuleDTO.getExtAPIModel();
			if (null != extAPIModel) {
				newExtAPIModel.setColumnCoordinates(extAPIModel.isColumnCoordinates());
				newExtAPIModel.setColumnHeader(extAPIModel.isColumnHeader());
				newExtAPIModel.setRegexExtraction(extAPIModel.isRegexExtraction());
				newExtAPIModel.setCoordinateHeaderComb(extAPIModel.getCoordinateHeaderComb());
				newExtAPIModel.setHeaderRegexComb(extAPIModel.getHeaderRegexComb());
			}
		}
		if (null != newTableExtractionRuleDTO) {
			newTableExtractionRuleDTO.setExtAPIModel(newExtAPIModel);
		}
	}

	/**
	 * Copies table column extraction rule DTOs from table's column extraction rules and attaches to its new parent table extraction
	 * rule DTO.
	 * 
	 * @param tableColumnExtractionRuleDTOs {@link List<{@link TableColumnExtractionRuleDTO}>}
	 * @param tableExtractionRuleDTO {@link TableExtractionRuleDTO}
	 * @return {@link List<{@link TableColumnExtractionRuleDTO}>}
	 */
	private static List<TableColumnExtractionRuleDTO> copyTableColumnExtractionRuleDTO(
			final List<TableColumnExtractionRuleDTO> tableColumnExtractionRuleDTOs,
			final TableExtractionRuleDTO tableExtractionRuleDTO, TableInfoDTO newTableInfoDTO) {
		List<TableColumnExtractionRuleDTO> newListColumnExtractionRuleDTOs = null;
		if (!CollectionUtil.isEmpty(tableColumnExtractionRuleDTOs)) {
			newListColumnExtractionRuleDTOs = new ArrayList<TableColumnExtractionRuleDTO>(tableColumnExtractionRuleDTOs.size());
			for (TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO : tableColumnExtractionRuleDTOs) {
				TableColumnExtractionRuleDTO newTableColumnExtractionRuleDTO = null;
				if (null != tableColumnExtractionRuleDTO) {
					newTableColumnExtractionRuleDTO = new TableColumnExtractionRuleDTO();
					newTableColumnExtractionRuleDTO.setIdentifier(tableColumnExtractionRuleDTO.getIdentifier());
					newTableColumnExtractionRuleDTO.setBetweenLeft(tableColumnExtractionRuleDTO.getBetweenLeft());
					newTableColumnExtractionRuleDTO.setBetweenRight(tableColumnExtractionRuleDTO.getBetweenRight());
					newTableColumnExtractionRuleDTO.setColumnPattern(tableColumnExtractionRuleDTO.getColumnPattern());
					newTableColumnExtractionRuleDTO.setExtractedDataColumnName(tableColumnExtractionRuleDTO
							.getExtractedDataColumnName());
					if (newTableInfoDTO != null) {
						newTableColumnExtractionRuleDTO.setTableColumnInfoDTO(newTableInfoDTO
								.getTableColumnInfoDTOByColumnName(tableColumnExtractionRuleDTO.getColumnName()));
					} else {
						newTableColumnExtractionRuleDTO.setTableColumnInfoDTO(null);
					}
					// Currency field not getting copied, Bug
					newTableColumnExtractionRuleDTO.setCurrency(tableColumnExtractionRuleDTO.isCurrency());
					newTableColumnExtractionRuleDTO.setNew(true);
					newTableColumnExtractionRuleDTO.setTableExtractionRuleDTO(tableExtractionRuleDTO);
					newTableColumnExtractionRuleDTO.setRequired(tableColumnExtractionRuleDTO.isRequired());
					newTableColumnExtractionRuleDTO.setMandatory(tableColumnExtractionRuleDTO.isMandatory());
					newTableColumnExtractionRuleDTO.setIdentifier(tableColumnExtractionRuleDTO.getIdentifier());
					newTableColumnExtractionRuleDTO.setColumnHeaderPattern(tableColumnExtractionRuleDTO.getColumnHeaderPattern());
					newTableColumnExtractionRuleDTO.setColumnName(tableColumnExtractionRuleDTO.getColumnName());
					if (tableColumnExtractionRuleDTO.getColumnStartCoordinate() != null) {
						newTableColumnExtractionRuleDTO.setColumnStartCoordinate(String.valueOf(tableColumnExtractionRuleDTO
								.getColumnStartCoordinate()));
					} else {
						newTableColumnExtractionRuleDTO.setColumnStartCoordinate(CoreCommonConstants.EMPTY_STRING);
					}
					if (tableColumnExtractionRuleDTO.getColumnEndCoordinate() != null) {
						newTableColumnExtractionRuleDTO.setColumnEndCoordinate(String.valueOf(tableColumnExtractionRuleDTO
								.getColumnEndCoordinate()));
					} else {
						newTableColumnExtractionRuleDTO.setColumnEndCoordinate(CoreCommonConstants.EMPTY_STRING);
					}
					if (tableColumnExtractionRuleDTO.getColumnCoordY0() != null) {
						newTableColumnExtractionRuleDTO.setColumnCoordY0(String.valueOf(tableColumnExtractionRuleDTO
								.getColumnCoordY0()));
					} else {
						newTableColumnExtractionRuleDTO.setColumnCoordY0(CoreCommonConstants.EMPTY_STRING);
					}
					if (tableColumnExtractionRuleDTO.getColumnCoordY1() != null) {
						newTableColumnExtractionRuleDTO.setColumnCoordY1(String.valueOf(tableColumnExtractionRuleDTO
								.getColumnCoordY1()));
					} else {
						newTableColumnExtractionRuleDTO.setColumnCoordY1(CoreCommonConstants.EMPTY_STRING);
					}
					newListColumnExtractionRuleDTOs.add(newTableColumnExtractionRuleDTO);
				}
			}
		}

		return newListColumnExtractionRuleDTOs;
	}
}
