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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.common.CurrencyCode;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.constant.DataAccessConstant;

@Entity
@Table(name = "table_info")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class TableInfo extends AbstractChangeableEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "document_type_id")
	private DocumentType docType;

	@Column(name = "table_name")
	private String name;

	@Column(name = "start_pattern", length = 700)
	private String startPattern;

	@Column(name = "end_pattern", length = 700)
	private String endPattern;

	@Column(name = "table_extraction_api")
	private String tableExtractionAPI;

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "table_info_id")
	private List<TableColumnsInfo> tableColumnsInfo = new ArrayList<TableColumnsInfo>();

	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "table_info_id")
	private List<TableRuleInfo> tableRuleInfo = new ArrayList<TableRuleInfo>();

	/**
	 * Represents the list of table extraction rules defined for a particular table.
	 */
	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = DataAccessConstant.TABLE_INFO_ID)
	private List<TableExtractionRule> tableExtractionRules = new ArrayList<TableExtractionRule>();

	@Column(name = "display_image")
	private String displayImage;

	/**
	 * rule {@link String} specifies the table_rule column in table_info table.
	 */
	@Column(name = "table_rule_operator")
	private String ruleOperator;

	/**
	 * numberOfRows {@link int} specifies no_of_rows column in table_info table.
	 */
	@Column(name = "no_of_rows")
	private int numberOfRows;

	@Column(name = "is_remove_invalid_rows", columnDefinition = "bit default 0")
	private boolean isRemoveInvalidRows;

	/**
	 * Represents the currency code that is to be used for extracting currency values from table..
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "currency_value")
	private CurrencyCode currencyCode;

	public String getDisplayImage() {
		return displayImage;
	}

	public void setDisplayImage(String displayImage) {
		this.displayImage = displayImage;
	}

	public String getTableExtractionAPI() {
		return tableExtractionAPI;
	}

	public void setTableExtractionAPI(String tableExtractionAPI) {
		this.tableExtractionAPI = tableExtractionAPI;
	}

	public DocumentType getDocType() {
		return docType;
	}

	public void setDocType(DocumentType docType) {
		this.docType = docType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setStartPattern(String startPattern) {
		this.startPattern = startPattern;
	}

	public String getStartPattern() {
		return startPattern;
	}

	public void setEndPattern(String endPattern) {
		this.endPattern = endPattern;
	}

	public String getEndPattern() {
		return endPattern;
	}

	public void setTableColumnsInfo(List<TableColumnsInfo> tableColumnsInfo) {
		this.tableColumnsInfo = tableColumnsInfo;
	}

	public List<TableColumnsInfo> getTableColumnsInfo() {
		return tableColumnsInfo;
	}

	public void setTableRuleInfo(List<TableRuleInfo> tableRuleInfo) {
		this.tableRuleInfo = tableRuleInfo;
	}

	public List<TableRuleInfo> getTableRuleInfo() {
		return tableRuleInfo;
	}

	public boolean removeTableColumnsInfoById(long identifier) {
		boolean isRemoved = false;
		if (null != this.tableColumnsInfo) {
			int index = 0;
			TableColumnsInfo removalElement = null;
			for (TableColumnsInfo actualElement : this.tableColumnsInfo) {
				if (identifier == actualElement.getId()) {
					removalElement = this.tableColumnsInfo.get(index);
					isRemoved = this.tableColumnsInfo.remove(removalElement);
					break;
				}
				index++;
			}
		}

		return isRemoved;
	}

	public boolean removeTableRulesInfoById(long identifier) {
		boolean isRemoved = false;
		if (null != this.tableRuleInfo) {
			int index = 0;
			TableRuleInfo removalElement = null;
			for (TableRuleInfo actualElement : this.tableRuleInfo) {
				if (identifier == actualElement.getId()) {
					removalElement = this.tableRuleInfo.get(index);
					isRemoved = this.tableRuleInfo.remove(removalElement);
					break;
				}
				index++;
			}
		}

		return isRemoved;
	}

	public void addTableColumnsInfo(TableColumnsInfo tableColumnsInfo) {

		if (null == tableColumnsInfo) {
			return;
		}

		this.tableColumnsInfo.add(tableColumnsInfo);
	}

	public void addTableRuleColumnsInfo(TableRuleInfo tableRuleInfo) {
		if (null == tableRuleInfo) {
			return;
		}

		this.tableRuleInfo.add(tableRuleInfo);
	}

	public TableColumnsInfo getTableColumnsInfobyIdOfColumn(Long tableColumnInfoId) {
		TableColumnsInfo tableColumnsInfo1 = null;
		if (null != tableColumnInfoId && this.tableColumnsInfo != null && !this.tableColumnsInfo.isEmpty()) {
			for (TableColumnsInfo tableColumn : this.tableColumnsInfo) {
				if (tableColumn.getId() == tableColumnInfoId) {
					tableColumnsInfo1 = tableColumn;
					break;
				}
			}
		}
		return tableColumnsInfo1;
	}

	public TableRuleInfo getTableRulesInfo(Long tableRuleInfoId) {
		TableRuleInfo tableRuleInfo1 = null;
		if (null != tableRuleInfoId && this.tableRuleInfo != null && !this.tableRuleInfo.isEmpty()) {
			for (TableRuleInfo tableRule : this.tableRuleInfo) {
				if (tableRule.getId() == tableRuleInfoId) {
					tableRuleInfo1 = tableRule;
					break;
				}
			}
		}
		return tableRuleInfo1;
	}

	/**
	 * Adds a Table Column Info to this table
	 * 
	 * @param kvExtraction the KV Extraction to be added
	 */
	public void addTableColumnInfo(TableColumnsInfo tableColumnsInfo) {

		if (null == tableColumnsInfo) {
			return;
		}

		this.tableColumnsInfo.add(tableColumnsInfo);
	}

	public void addTableRuleInfo(TableRuleInfo tableRulesInfo) {
		if (null == tableRulesInfo) {
			return;
		}

		this.tableRuleInfo.add(tableRulesInfo);
	}

	/**
	 * Returns a Table Columns Info based on identifier
	 * 
	 * @param identifier the identifier corresponding to the Table Columns Info
	 * @return Table Columns Info if found. null otherwise
	 */
	public TableColumnsInfo getTableColumnInfobyIdentifier(String identifier) {
		TableColumnsInfo tableColumnsInfo = null;
		if (null != identifier && this.tableColumnsInfo != null && !this.tableColumnsInfo.isEmpty()) {
			for (TableColumnsInfo columnsInfo : this.tableColumnsInfo) {
				if (String.valueOf(columnsInfo.getId()).equals(identifier)) {
					tableColumnsInfo = columnsInfo;
					break;
				}
			}
		}
		return tableColumnsInfo;
	}

	public TableRuleInfo getTableRuleInfobyIdentifier(String identifier) {
		TableRuleInfo tableRulesInfo = null;
		if (null != identifier && this.tableRuleInfo != null && !this.tableRuleInfo.isEmpty()) {
			for (TableRuleInfo ruleInfo : this.tableRuleInfo) {
				if (String.valueOf(ruleInfo.getId()).equals(identifier)) {
					tableRulesInfo = ruleInfo;
					break;
				}
			}
		}

		return tableRulesInfo;
	}

	/**
	 * The <code>setRule</code> is used to set the rule defined for a table.
	 * 
	 * @param rule {@link String} The rule defined for a table.
	 */
	public void setRuleOperator(final String ruleOperator) {
		this.ruleOperator = ruleOperator;
	}

	/**
	 * The <code>getRule</code> is used to set the rule defined for a table.
	 * 
	 * @return rule {@link String} The rule defined for a table.
	 */
	public String getRuleOperator() {
		return ruleOperator;
	}

	/**
	 * The <code>getNumberOfRows</code> is used to get the number of rows to be displayed in table.
	 * 
	 * @return {@link int} The number of rows for a table.
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * The <code>setNumberOfRows</code> is used to set the number of rows to be displayed in table.
	 * 
	 * @param numberOfRows {@link int} The number of rows for a table.
	 */
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public boolean isRemoveInvalidRows() {
		return isRemoveInvalidRows;
	}

	public void setRemoveInvalidRows(boolean isRemoveInvalidRows) {
		this.isRemoveInvalidRows = isRemoveInvalidRows;
	}

	/**
	 * @return the currencyCode
	 */
	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(CurrencyCode currencyCode) {
		this.currencyCode = currencyCode;
	}

	/**
	 * Removes the table extraction rule for the given identifier. Returns {@code true} if it is removed successfully otherwise returns
	 * {@code false}.
	 * 
	 * @param identifier represents the table extraction rule identifier.
	 * @return {@code true} if the table extraction rule is removed successfully, {@code false} otherwise.
	 */
	public boolean removeTableExtractionRuleById(final long identifier) {
		boolean isRemoved = false;
		if (null != this.tableExtractionRules) {
			int index = 0;
			TableExtractionRule removalElement = null;
			for (TableExtractionRule actualElement : this.tableExtractionRules) {
				if (null != actualElement && identifier == actualElement.getId()) {
					removalElement = this.tableExtractionRules.get(index);
					isRemoved = this.tableExtractionRules.remove(removalElement);
					break;
				}
				index++;
			}
		}
		return isRemoved;
	}

	/**
	 * Adds the table extraction rule object in the list of table extraction rules if it is not {@code null}. If table extraction rule
	 * is {@code null} then it is not added in the list.
	 * 
	 * @param {@link TableExtractionRule} Object to be added in the list of table extraction rules.
	 */
	public void addTableExtractionRule(final TableExtractionRule tableExtractionRule) {
		if (null != tableExtractionRule) {
			this.tableExtractionRules.add(tableExtractionRule);
		}
	}

	/**
	 * Returns the table extraction rule object based on the identifer passed. This method returns {@code null} if no table extraction
	 * rule object exists for the given identifier.
	 * 
	 * @param identifier {@link String} represents the table extraction rule identifier.
	 * @return {@link TableExtractionRule} object based on the identifier passed.
	 */
	public TableExtractionRule getTableExtractionRuleByIdentifier(final String identifier) {
		TableExtractionRule newTableExtractionRule = null;
		if (null != identifier && !this.tableExtractionRules.isEmpty()) {
			for (TableExtractionRule tableExtractionRule : this.tableExtractionRules) {
				if (null != tableExtractionRule && identifier.equals(String.valueOf(tableExtractionRule.getId()))) {
					newTableExtractionRule = tableExtractionRule;
					break;
				}
			}
		}
		return newTableExtractionRule;
	}

	/**
	 * @return the tableExtractionRules
	 */
	public List<TableExtractionRule> getTableExtractionRules() {
		return tableExtractionRules;
	}

	/**
	 * @param tableExtractionRules the tableExtractionRules to set
	 */
	public void setTableExtractionRules(final List<TableExtractionRule> tableExtractionRules) {
		this.tableExtractionRules = tableExtractionRules== null ? this.tableExtractionRules : tableExtractionRules;
	}

	@Override
	public void postPersist() {
		super.postPersist();
	}
}
