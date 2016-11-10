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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.constant.DataAccessConstant;
import com.ephesoft.dcma.util.CollectionUtil;

/**
 * Entity class for Table extraction rule used for storing the table extraction API, table start pattern, table end pattern defined for
 * a particular table.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 09-Jan-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see AbstractChangeableEntity
 * @see TableInfo
 * @see DataAccessConstant
 */
@Entity
@Table(name = DataAccessConstant.TABLE_EXTRACTION_RULE)
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class TableExtractionRule extends AbstractChangeableEntity implements Serializable {

	/**
	 * Represents the generated serial version id.
	 */
	private static final long serialVersionUID = 2137872827959487772L;

	/**
	 * Represents the column for defining the start pattern for a particular table.
	 */
	@Column(name = DataAccessConstant.TABLE_EXTRACTION_START_PATTERN, length = 700)
	private String startPattern;

	/**
	 * Represents the column for defining the end pattern for a particular table.
	 */
	@Column(name = DataAccessConstant.TABLE_EXTRACTION_END_PATTERN, length = 700)
	private String endPattern;

	/**
	 * Represents the column for defining the table extraction API for a particular table.
	 */
	@Column(name = DataAccessConstant.TABLE_API)
	private String tableAPI;

	/**
	 * Represents the column for defining the name of the table extraction rule for a particular table.
	 */
	@Column(name = DataAccessConstant.TABLE_EXTRACTION_RULE_NAME)
	private String ruleName;

	/**
	 * Represents the object for the table_info detail table.
	 */
	@ManyToOne
	@JoinColumn(name = DataAccessConstant.TABLE_INFO_ID)
	private TableInfo tableInfo;

	/**
	 * Represents the list of column extraction rules defined for a particular table extraction rule.
	 */
	@OneToMany
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = DataAccessConstant.TABLE_EXTRACTION_RULE_ID)
	private List<TableColumnExtractionRule> tableColumnExtractionRules = new ArrayList<TableColumnExtractionRule>();

	/**
	 * @return the tableColumnExtractionRules
	 */
	public List<TableColumnExtractionRule> getTableColumnExtractionRules() {
		return tableColumnExtractionRules;
	}

	/**
	 * @param tableColumnExtractionRules the tableColumnExtractionRules to set
	 */
	public void setTableColumnExtractionRules(final List<TableColumnExtractionRule> tableColumnExtractionRules) {
		this.tableColumnExtractionRules = tableColumnExtractionRules == null ? this.tableColumnExtractionRules
				: tableColumnExtractionRules;
	}

	/**
	 * @return the tableInfo
	 */
	public TableInfo getTableInfo() {
		return tableInfo;
	}

	/**
	 * @param tableInfo the tableInfo to set
	 */
	public void setTableInfo(final TableInfo tableInfo) {
		this.tableInfo = tableInfo;
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

	public void addTableColumnExtractionRule(final TableColumnExtractionRule tableColumnExtractionRule) {
		if (null != tableColumnExtractionRule) {
			this.tableColumnExtractionRules.add(tableColumnExtractionRule);
		}
	}

	public TableColumnExtractionRule getTableExtractionRuleByIdentifier(final String identifier) {
		TableColumnExtractionRule tableColumnExtractionRule = null;
		if (null != identifier && !this.tableColumnExtractionRules.isEmpty()) {
			for (TableColumnExtractionRule tableColumnExtractionRule2 : this.tableColumnExtractionRules) {
				if (null != tableColumnExtractionRule2 && identifier.equals(String.valueOf(tableColumnExtractionRule2.getId()))) {
					tableColumnExtractionRule = tableColumnExtractionRule2;
					break;
				}
			}
		}
		return tableColumnExtractionRule;
	}

	@Override
	public void postPersist() {
		super.postPersist();
		if (!CollectionUtil.isEmpty(tableColumnExtractionRules)) {
			for (TableColumnExtractionRule tableColumnExtractionRule : tableColumnExtractionRules) {
				tableColumnExtractionRule.postPersist();
			}
		}
	}
}
