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

/**
 * 
 * Defines parameters used to extract column parameters in a table.
 * 
 * @author Ephesoft
 * 
 *         created on Feb 11, 2014 <br/>
 * @version $LastChangedDate:$<br/>
 *          $LastChangedRevision:$<br/>
 */
public class TableExtractionRuleDTO implements IsSerializable {

	/**
	 * {@link String} start Pattern for the table.
	 */
	private String startPattern;

	/**
	 * {@link String} End-pattern for the Table
	 */
	private String endPattern;

	/**
	 * {@link String} TABLE_API that defines the extraction API to be used to extract the table parameters.
	 */
	private String tableAPI;

	/**
	 * {@link String} Unique name of the rule.
	 */
	private String ruleName;

	/**
	 * {@link TableInfoDTO} Reference to the table info DTO to which the rule belongs.
	 */
	private TableInfoDTO tableInfoDTO;

	/**
	 * boolean that indicates the rule is deleted.
	 */
	private boolean isDeleted;

	/**
	 * {@link String} Unique identifier for the Table Extraction Rule.
	 */
	private String identifier;

	/**
	 * boolean that indicates the rule is new or not. i.e It has been stored to DB or not.
	 */
	private boolean isNew;

	/**
	 * {@link List} of column Extraction Rules.
	 */
	private List<TableColumnExtractionRuleDTO> tableColumnExtractionRuleDTOs = new ArrayList<TableColumnExtractionRuleDTO>();

	/**
	 * @return the isNew
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * @param isNew the isNew to set
	 */
	public void setNew(final boolean isNew) {
		this.isNew = isNew;
	}

	public void setTableColumnExtractionRuleDTOs(final List<TableColumnExtractionRuleDTO> tableColumnExtractionRuleDTOs) {
		this.tableColumnExtractionRuleDTOs = tableColumnExtractionRuleDTOs == null ? this.tableColumnExtractionRuleDTOs
				: tableColumnExtractionRuleDTOs;
	}

	/**
	 * @return the startPattern
	 */
	public String getStartPattern() {
		return startPattern;
	}

	/**
	 * @param startPattern the startPattern to set
	 */
	public void setStartPattern(final String startPattern) {
		this.startPattern = startPattern;
	}

	/**
	 * @return the endPattern
	 */
	public String getEndPattern() {
		return endPattern;
	}

	/**
	 * @param endPattern the endPattern to set
	 */
	public void setEndPattern(final String endPattern) {
		this.endPattern = endPattern;
	}

	/**
	 * @return the tableAPI
	 */
	public String getTableAPI() {
		return tableAPI;
	}

	/**
	 * @param tableAPI the tableAPI to set
	 */
	public void setTableAPI(final String tableAPI) {
		this.tableAPI = tableAPI;
	}

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return ruleName;
	}

	/**
	 * @param ruleName the ruleName to set
	 */
	public void setRuleName(final String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * @return the tableInfoDTO
	 */
	public TableInfoDTO getTableInfoDTO() {
		return tableInfoDTO;
	}

	/**
	 * @param tableInfoDTO the tableInfoDTO to set
	 */
	public void setTableInfoDTO(final TableInfoDTO tableInfoDTO) {
		this.tableInfoDTO = tableInfoDTO;
	}

	/**
	 * @return the isDeleted
	 */
	public boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setDeleted(final boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(final String identifier) {
		this.identifier = identifier;
	}

	public List<TableColumnExtractionRuleDTO> getTableColumnExtractionRuleDTOs(final boolean includeDeleted) {
		List<TableColumnExtractionRuleDTO> tableColumnExtractionRuleDTOs = null;
		if (includeDeleted) {
			tableColumnExtractionRuleDTOs = this.tableColumnExtractionRuleDTOs;
		} else {
			tableColumnExtractionRuleDTOs = getTableColumnExtractionRuleDTOs();
		}
		return tableColumnExtractionRuleDTOs;
	}

	public List<TableColumnExtractionRuleDTO> getTableColumnExtractionRuleDTOs() {
		List<TableColumnExtractionRuleDTO> newTableColumnExtractionRuleDTOs = null;
		if (!CollectionUtil.isEmpty(tableColumnExtractionRuleDTOs)) {
			newTableColumnExtractionRuleDTOs = new LinkedList<TableColumnExtractionRuleDTO>();
			for (TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO : tableColumnExtractionRuleDTOs) {
				if (null != tableColumnExtractionRuleDTO && !(tableColumnExtractionRuleDTO.isDeleted())) {
					newTableColumnExtractionRuleDTOs.add(tableColumnExtractionRuleDTO);
				}
			}
		}
		return newTableColumnExtractionRuleDTOs;
	}

	public void addTableColumnExtractionRule(final TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO) {
		if (null != tableColumnExtractionRuleDTO) {
			tableColumnExtractionRuleDTOs.add(tableColumnExtractionRuleDTO);
		}
	}

	public TableColumnExtractionRuleDTO getTableColumnExtractionDTOByIdentifier(final String identifier) {
		TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO = null;
		if (null != identifier) {
			for (TableColumnExtractionRuleDTO tableExtractionRuleDTO2 : tableColumnExtractionRuleDTOs) {
				if (null != tableExtractionRuleDTO2 && identifier.equals(tableExtractionRuleDTO2.getIdentifier())) {
					tableColumnExtractionRuleDTO = tableExtractionRuleDTO2;
				}
			}
		}
		return tableColumnExtractionRuleDTO;
	}

	/**
	 * Gets the table Column extraction DTO by column name.
	 * 
	 * @param columnName {@link String} column name by which the table column extraction DTO is to be searched. 
	 * @return {@link TableColumnExtractionRuleDTO} referred by the column name passed as argument.
	 */
	public TableColumnExtractionRuleDTO getTableColumnExtractionRuleByColumnName(final String columnName) {
		TableColumnExtractionRuleDTO namedTableColumnExtractionDTO = null;
		if (!StringUtil.isNullOrEmpty(columnName) && !CollectionUtil.isEmpty(tableColumnExtractionRuleDTOs)) {
			for (TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO : tableColumnExtractionRuleDTOs) {
				if (null != tableColumnExtractionRuleDTO && columnName.equalsIgnoreCase(tableColumnExtractionRuleDTO.getColumnName())) {
					namedTableColumnExtractionDTO = tableColumnExtractionRuleDTO;
					break;
				}
			}
		}
		return namedTableColumnExtractionDTO;
	}
}
