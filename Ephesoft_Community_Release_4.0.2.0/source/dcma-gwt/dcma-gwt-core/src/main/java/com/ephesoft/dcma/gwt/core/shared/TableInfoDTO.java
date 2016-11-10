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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.gwt.core.shared.util.CollectionUtil;
import com.google.gwt.user.client.rpc.IsSerializable;

public class TableInfoDTO implements IsSerializable {

	private DocumentTypeDTO docTypeDTO;

	private String name;

	private String identifier;

	private String startPattern;

	private String endPattern;

	private boolean isNew;

	private boolean isDeleted;

	/**
	 * Instance of rule {@link String}.
	 */
	private String ruleOperator;

	private List<TableColumnInfoDTO> columnInfoDTOs = new ArrayList<TableColumnInfoDTO>();

	private List<RuleInfoDTO> ruleDTOs = new ArrayList<RuleInfoDTO>();

	private String displayImage;

	/**
	 * Represents the currency code corresponding to the table column.
	 */
	private String currencyCode;

	/**
	 * the number of rows in the table.
	 */
	private String numberOfRows;

	private boolean isRemoveInvalidRows;

	/***
	 * {@link List} of extraction rules which belong to a table.
	 */
	private List<TableExtractionRuleDTO> tableExtractionRuleDTOs = new ArrayList<TableExtractionRuleDTO>();

	public String getDisplayImage() {
		return displayImage;
	}

	public void setDisplayImage(String displayImage) {
		this.displayImage = displayImage;
	}

	public DocumentTypeDTO getDocTypeDTO() {
		return docTypeDTO;
	}

	public void setDocTypeDTO(DocumentTypeDTO docTypeDTO) {
		this.docTypeDTO = docTypeDTO;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartPattern() {
		return startPattern;
	}

	public void setStartPattern(String startPattern) {
		this.startPattern = startPattern;
	}

	public String getEndPattern() {
		return endPattern;
	}

	public void setEndPattern(String endPattern) {
		this.endPattern = endPattern;
	}

	public List<TableColumnInfoDTO> getColumnInfoDTOs(boolean includeDeleted) {
		List<TableColumnInfoDTO> columnInfoDTOs = null;
		if (includeDeleted) {

			columnInfoDTOs = this.columnInfoDTOs;
		} else {
			columnInfoDTOs = getColumnInfoDTOs();
		}
		return columnInfoDTOs;
	}

	public List<RuleInfoDTO> getRuleInfoDTOs(boolean includeDeleted) {
		List<RuleInfoDTO> ruleInfoDTOs = null;
		if (includeDeleted) {
			ruleInfoDTOs = this.ruleDTOs;
		} else {
			ruleInfoDTOs = getRuleInfoDTOs();
		}
		return ruleInfoDTOs;
	}

	public List<TableColumnInfoDTO> getColumnInfoDTOs() {
		List<TableColumnInfoDTO> tableColumnInfoDTOs = new LinkedList<TableColumnInfoDTO>();
		for (TableColumnInfoDTO columnInfoDTO : columnInfoDTOs) {
			if (!(columnInfoDTO.isDeleted())) {
				tableColumnInfoDTOs.add(columnInfoDTO);
			}
		}
		return tableColumnInfoDTOs;
	}

	/**
	 * Returns the list of rule info dtos excluding the deleted rule dtos.
	 * 
	 * @return {@link List{@link RuleInfoDTO} list of rule info dtos.
	 */
	public List<RuleInfoDTO> getRuleInfoDTOs() {
		List<RuleInfoDTO> ruleInfoDTOs = new LinkedList<RuleInfoDTO>();
		if (null != ruleDTOs) {
			for (RuleInfoDTO ruleDTO : ruleDTOs) {
				if (!(ruleDTO.isDeleted())) {
					ruleInfoDTOs.add(ruleDTO);
				}
			}
		}
		return ruleInfoDTOs;
	}

	public void setColumnInfoDTOs(List<TableColumnInfoDTO> columnInfoDTOs) {
		this.columnInfoDTOs = columnInfoDTOs;
	}

	public void addColumnInfo(TableColumnInfoDTO columnInfoDTO) {
		columnInfoDTOs.add(columnInfoDTO);
	}

	public void addRuleInfo(RuleInfoDTO ruleInfoDTO) {
		ruleDTOs.add(ruleInfoDTO);
	}

	public List<TableColumnInfoDTO> getTableColumnInfoList(boolean includeDeleted) {
		List<TableColumnInfoDTO> tableColumnInfoDTOs = null;
		if (includeDeleted) {
			tableColumnInfoDTOs = columnInfoDTOs;
		} else {
			tableColumnInfoDTOs = getTableColumnInfoList();
		}
		return tableColumnInfoDTOs;
	}

	public List<TableColumnInfoDTO> getTableColumnInfoList() {
		List<TableColumnInfoDTO> tableColumnInfoDTOs = new LinkedList<TableColumnInfoDTO>();
		for (TableColumnInfoDTO columnInfoDTO : columnInfoDTOs) {
			if (!(columnInfoDTO.isDeleted())) {
				tableColumnInfoDTOs.add(columnInfoDTO);
			}
		}
		return tableColumnInfoDTOs;
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

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public TableColumnInfoDTO getTCInfoDTOByIdentifier(String identifier) {
		TableColumnInfoDTO tableColumnInfoDTO = null;
		for (TableColumnInfoDTO columnInfoDTO : columnInfoDTOs) {
			if (columnInfoDTO.getIdentifier().equals(identifier)) {
				tableColumnInfoDTO = columnInfoDTO;
			}
		}
		return tableColumnInfoDTO;
	}

	public RuleInfoDTO getRuleInfoDTOByIdentifier(String identifier) {
		RuleInfoDTO ruleInfoDTO = null;
		for (RuleInfoDTO ruleDTO : ruleDTOs) {
			if (ruleDTO.getIdentifier().equals(identifier)) {
				ruleInfoDTO = ruleDTO;
			}
		}
		return ruleInfoDTO;
	}

	public TableColumnInfoDTO getTCInfoDTOByName(final String name) {
		TableColumnInfoDTO tableColumnInfoDTO = null;
		for (TableColumnInfoDTO columnInfoDTO : columnInfoDTOs) {
			if (columnInfoDTO.getColumnName().equalsIgnoreCase(name)) {
				tableColumnInfoDTO = columnInfoDTO;
			}
		}
		return tableColumnInfoDTO;
	}

	public RuleInfoDTO getRuleInfoDTOByRule(String rule) {
		RuleInfoDTO ruleInfoDTO = null;
		for (RuleInfoDTO ruleDTO : ruleDTOs) {
			if (ruleDTO.getRule().equals(rule)) {
				ruleInfoDTO = ruleDTO;
			}
		}
		return ruleInfoDTO;
	}

	/**
	 * Gets the <code>TableColumnInfoDTO</code> object for table column in this table which is not deleted and has the given name.
	 * Performs a case insensitive search for table column name.
	 * 
	 * @param name {@link String} the name of the table column to be searched.
	 * @return {@link TableColumnInfoDTO} table column info DTO for the table column with the given in this table, returns
	 *         <code>null</code> if no such table column is found.
	 */
	public TableColumnInfoDTO getTableColumnInfoDTOByName(String name) {
		TableColumnInfoDTO tableColumnInfoDTO = null;
		for (TableColumnInfoDTO columnInfoDTO : columnInfoDTOs) {
			if (!columnInfoDTO.isDeleted() && columnInfoDTO.getColumnName().equalsIgnoreCase(name)) {
				tableColumnInfoDTO = columnInfoDTO;
				break;
			}
		}
		return tableColumnInfoDTO;
	}

	/**
	 * Gets the <code>TableColumnInfoDTO</code> object for table column in this table which is not deleted and has the given name
	 * except for the table column with given identifier. Performs a case insensitive search for table column name.
	 * 
	 * @param name {@link String} the name of the table column to be searched.
	 * @return {@link TableColumnInfoDTO} table column info DTO for the table column with the given in this table, returns
	 *         <code>null</code> if no such table column is found.
	 */
	public TableColumnInfoDTO getTableColumnInfoDTOByName(String name, String identifier) {
		TableColumnInfoDTO tableColumnInfoDTO = null;
		for (TableColumnInfoDTO columnInfoDTO : columnInfoDTOs) {
			if (!columnInfoDTO.getIdentifier().equalsIgnoreCase(identifier) && !columnInfoDTO.isDeleted()
					&& columnInfoDTO.getColumnName().equalsIgnoreCase(name)) {
				tableColumnInfoDTO = columnInfoDTO;
				break;
			}
		}
		return tableColumnInfoDTO;
	}

	/**
	 * Checks if the table column name provided is already in use by some other table column in this table or not. Performs a case
	 * insensitive search on the table column name.
	 * 
	 * @param tableColumnName {@link String} the name of the table column to checked for availability.
	 * @return boolean, <code>true</code> if the provided name is not taken by any other existing table column, <code>false</code>
	 *         otherwise.
	 */
	public boolean checkTableColumnName(String tableColumnName) {
		boolean isTableNameInUse = false;
		if (getTableColumnInfoDTOByName(tableColumnName) != null) {
			isTableNameInUse = true;
		}
		return isTableNameInUse;
	}

	/**
	 * Checks if the table column name provided is already in use by some other table column in this table or not except for the table
	 * column with given identifier. Performs a case insensitive search on the table column name.
	 * 
	 * @param tableColumnName {@link String} the name of the table column to checked for availability.
	 * @return boolean, <code>true</code> if the provided name is not taken by any other existing table column, <code>false</code>
	 *         otherwise.
	 */
	public boolean checkTableColumnName(String tableColumnName, String identifier) {
		boolean isTableNameInUse = false;
		if (getTableColumnInfoDTOByName(tableColumnName, identifier) != null) {
			isTableNameInUse = true;
		}
		return isTableNameInUse;
	}

	/**
	 * Setter for the rule defined for a table.
	 * 
	 * @param rule {@link String} The rule defined for a table.
	 */
	public void setRuleOperator(final String ruleOperator) {
		this.ruleOperator = ruleOperator;
	}

	/**
	 * Getter for the rule defined for a table.
	 * 
	 * @return rule {@link String} The rule defined for a table.
	 */
	public String getRuleOperator() {
		return ruleOperator;
	}

	/**
	 * Returns the number of rows.
	 * 
	 * @return {@link String} the number of rows in the table
	 */
	public String getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * Sets the number of rows.
	 * 
	 * @param numberOfRows {@link String} the number of rows to set
	 */
	public void setNumberOfRows(String numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isRemoveInvalidRows() {
		return isRemoveInvalidRows;
	}

	public void setRemoveInvalidRows(boolean isRemoveInvalidRows) {
		this.isRemoveInvalidRows = isRemoveInvalidRows;
	}

	/**
	 * Checks for the duplicacy of the rule defined.
	 * 
	 * @param rule {@link String} rule defined for a table
	 */
	public boolean checkRule(final String rule, final String identifier) {
		for (RuleInfoDTO ruleInfoDTO : ruleDTOs) {
			if (ruleInfoDTO.getRule().equals(rule) && !ruleInfoDTO.getIdentifier().equalsIgnoreCase(identifier))
				return true;
		}
		return false;
	}

	/**
	 * Sets the rule info dtos.
	 * 
	 * @param ruleDTOs {@link RuleInfoDTO} the list to set in the dto list.
	 */
	public void setRuleDTOs(List<RuleInfoDTO> ruleDTOs) {
		this.ruleDTOs = ruleDTOs;
	}

	/**
	 * @return {@link String} the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode {@link String} the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	// New
	public List<TableExtractionRuleDTO> getTableExtractionRuleDTOs(final boolean includeDeleted) {
		List<TableExtractionRuleDTO> tableExtractionRuleDTOs = null;
		if (includeDeleted) {
			tableExtractionRuleDTOs = this.tableExtractionRuleDTOs;
		} else {
			tableExtractionRuleDTOs = getTableExtractionRuleDTOs();
		}
		return tableExtractionRuleDTOs;
	}

	public List<TableExtractionRuleDTO> getTableExtractionRuleDTOs() {
		List<TableExtractionRuleDTO> tableRuleDTOs = null;
		if (!CollectionUtil.isEmpty(tableExtractionRuleDTOs)) {
			tableRuleDTOs = new LinkedList<TableExtractionRuleDTO>();
			for (TableExtractionRuleDTO tableExtractionRuleDTO : tableExtractionRuleDTOs) {
				if (null != tableExtractionRuleDTO && !(tableExtractionRuleDTO.isDeleted())) {
					tableRuleDTOs.add(tableExtractionRuleDTO);
				}
			}
		}
		return tableRuleDTOs;
	}

	public void addTableExtractionRule(final TableExtractionRuleDTO tableExtractionRuleDTO) {
		if (null != tableExtractionRuleDTO) {
			tableExtractionRuleDTOs.add(tableExtractionRuleDTO);
		}
	}

	public TableExtractionRuleDTO getTableExtractionDTOByIdentifier(final String identifier) {
		TableExtractionRuleDTO newTableExtractionRuleDTO = null;
		if (null != identifier) {
			for (TableExtractionRuleDTO tableExtractionRuleDTO : tableExtractionRuleDTOs) {
				if (null != tableExtractionRuleDTO && identifier.equals(tableExtractionRuleDTO.getIdentifier())) {
					newTableExtractionRuleDTO = tableExtractionRuleDTO;
					break;
				}
			}
		}
		return newTableExtractionRuleDTO;
	}

	public void setTableExtractionRuleDTOs(final List<TableExtractionRuleDTO> tableExtractionRuleDTOs) {
		this.tableExtractionRuleDTOs = tableExtractionRuleDTOs == null ? this.tableExtractionRuleDTOs : tableExtractionRuleDTOs;
	}

	/**
	 * Gets the table extraction DTO by name.
	 * 
	 * @param name {@link String} name by which the table extraction DTO is to be searched.
	 * @return {@link TableExtractionRuleDTO} referred by the name passed as argument.
	 */
	public TableExtractionRuleDTO getTableExtractionRuleByName(final String name) {
		TableExtractionRuleDTO namedTableExtractionDTO = null;
		if (!StringUtil.isNullOrEmpty(name) && !CollectionUtil.isEmpty(tableExtractionRuleDTOs)) {
			for (TableExtractionRuleDTO tableExtractionRuleDTO : tableExtractionRuleDTOs) {
				if (null != tableExtractionRuleDTO && name.equalsIgnoreCase(tableExtractionRuleDTO.getRuleName())) {
					namedTableExtractionDTO = tableExtractionRuleDTO;
					break;
				}
			}
		}
		return namedTableExtractionDTO;
	}
}
