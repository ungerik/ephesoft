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

package com.ephesoft.dcma.da.dao.hibernate;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.ephesoft.dcma.core.dao.hibernate.HibernateDao;
import com.ephesoft.dcma.da.dao.TableRuleInfoDao;
import com.ephesoft.dcma.da.domain.TableInfo;
import com.ephesoft.dcma.da.domain.TableRuleInfo;

/**
 * Implementation of a Dao representing table_rule table in database.
 * 
 * @author Ephesoft
 * @version 1.0
 */
@Repository
public class TableRuleInfoDaoImpl extends HibernateDao<TableRuleInfo> implements TableRuleInfoDao{

private static final Logger LOGGER = LoggerFactory.getLogger(TableColumnsInfoDaoImpl.class);
	
	/**
	 * TABLE_INFO {@link} specifies the table_info_id column in table_rule table.
	 */
	private static final String TABLE_INFO = "tableInfo";
	
	/**
	 * API to fetch TableRulesInfo by TableInfo
	 * 
	 * @param tableInfo TableInfo
	 * @return List<TableRuleInfo>
	 */
	@Override
	public List<TableRuleInfo> getTableRulesInfoByTableInfo(TableInfo tableInfo) {
	
		LOGGER.info("TableInfo : " + tableInfo);
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.eq(TABLE_INFO, tableInfo));

		return find(criteria);

	}

	/**
	 * API to fetch all TableRuleInfo by document type name and table name for a batch class.
	 * 
	 * @param batchClassIdentifer {@link String} batch class identifier
	 * @param docTypeName {@link String} document type name
	 * @param tableName {@link String} table name
	 * @return List<TableRuleInfo> table rule info list
	 */
	@Override
	public List<TableRuleInfo> getTableRulesInfo(final String batchClassIdentifer,final String docTypeName,final String tableName) {
		
		LOGGER.info("docTypeName  : " + docTypeName);
		DetachedCriteria criteria = criteria();
		criteria.createAlias("tableInfo", "tableInfo", JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("tableInfo.name", tableName));
		criteria.createAlias("tableInfo.docType", "docType", JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("docType.name", docTypeName));
		criteria.createAlias("docType.batchClass", "batchClass", JoinFragment.INNER_JOIN);
		criteria.add(Restrictions.eq("batchClass.identifier", batchClassIdentifer));
		return find(criteria);
		
	}

	/**
	 * API to insert the TableRuleInfo object.
	 * 
	 * @param tableRuleInfo
	 */
	@Override
	public void insertTableRulesInfo(TableRuleInfo tableRuleInfo) {
		create(tableRuleInfo);
		
	}

	/**
	 * API to update the TableRuleInfo object.
	 * 
	 * @param tableRuleInfo
	 */
	@Override
	public void updateTableRulesInfo(TableRuleInfo tableRuleInfo) {
		saveOrUpdate(tableRuleInfo);
		
	}

	/**
	 * API to remove TableRuleInfo object.
	 * 
	 * @param tableRuleInfo
	 */
	@Override
	public void removeTableRulesInfo(TableRuleInfo tableRuleInfo) {
		remove(tableRuleInfo);
		
	}

	/**
	 * API to update or save the list of table rules.
	 * 
	 * @param listTableRuleInfos {@link List}< {@link TableRuleInfo}> The list of table rules which need to save or update.
	 * @param listTableRuleToBeDeleted {@link List}< {@link TableRuleInfo}> The list of table rules which need to be deleted.
	 */
	@Override
	public void updateTableColumnsInfo(List<TableRuleInfo> listTableRuleInfos, List<TableRuleInfo> listTableRuleToBeDeleted) {
		LOGGER.info("listTableRulesInfo Object is " + listTableRuleInfos);
		LOGGER.info("listTableRulesToBeDeleted Object is " + listTableRuleToBeDeleted);
		if (listTableRuleToBeDeleted != null && !listTableRuleToBeDeleted.isEmpty()) {
			for (TableRuleInfo tableRulesInfo : listTableRuleToBeDeleted) {
				remove(tableRulesInfo);
			}
		}

		if (listTableRuleInfos != null && !listTableRuleInfos.isEmpty()) {
			for (TableRuleInfo tableRulesInfo : listTableRuleInfos) {
				saveOrUpdate(tableRulesInfo);
			}
		}
		
	}

	/**
	 * API to retrieve list of all the table rules.
	 * 
	 * @return {@link List}< {@link TableRuleInfo}> The list of table rules.
	 */
	@Override
	public List<TableRuleInfo> getAllTableRules() {
		DetachedCriteria criteria = criteria();
		criteria.add(Restrictions.isNull(TABLE_INFO));
		return find(criteria);
	}

}
