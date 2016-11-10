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

package com.ephesoft.gxt.admin.client.util;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.widget.property.BatchClassNavigationNode;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.shared.dto.BatchClassModuleDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;

@SuppressWarnings({"unchecked", "rawtypes"})
public final class NavigationNodeFactory {

	public static NavigationNode getNode(final Object bindedObject, final BatchClassManagementController controller,
			final Object parentObject) {
		NavigationNode node = null;
		if (null != bindedObject && null != controller && null != parentObject) {
			if (bindedObject instanceof DocumentTypeDTO) {
				DocumentTypeDTO documentType = (DocumentTypeDTO) bindedObject;
				BatchClassNavigationNode batchClassNavigationNode = new BatchClassNavigationNode(documentType.getName(), null,
						documentType);
				node = new NavigationNode(batchClassNavigationNode);
				node.add(new BatchClassNavigationNode(LocaleDictionary.getConstantValue(BatchClassConstants.INDEX_FIELDS), controller
						.getIndexFieldPresenter(), bindedObject));
				node.add(new BatchClassNavigationNode(LocaleDictionary.getConstantValue(BatchClassConstants.TABLES), controller
						.getTableInfoPresenter(), bindedObject));
			} else if (bindedObject instanceof FieldTypeDTO) {
				FieldTypeDTO fieldType = (FieldTypeDTO) bindedObject;
				BatchClassNavigationNode batchClassNavigationNode = new BatchClassNavigationNode(fieldType.getName(), null, fieldType);
				node = new NavigationNode(batchClassNavigationNode);
				node.add(new BatchClassNavigationNode(LocaleDictionary.getConstantValue(BatchClassConstants.KV_EXTRACTION_RULE),
						controller.getKvExtractionPresenter(), bindedObject));
				node.add(new BatchClassNavigationNode(LocaleDictionary.getConstantValue(BatchClassConstants.VALIDATION_RULES),
						controller.getRegexFieldPresenter(), bindedObject));
			} else if (bindedObject instanceof TableInfoDTO) {
				TableInfoDTO tableInfo = (TableInfoDTO) bindedObject;
				BatchClassNavigationNode batchClassNavigationNode = new BatchClassNavigationNode(tableInfo.getName(), null, tableInfo);
				node = new NavigationNode(batchClassNavigationNode);
				node.add(new BatchClassNavigationNode(LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_COLUMNS), controller
						.getTableColumnPresenter(), bindedObject));
				node.add(new BatchClassNavigationNode(LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_EXTRACTION_RULES),
						controller.getTabExtRulePresenter(), bindedObject));
				node.add(new BatchClassNavigationNode(LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_VALIDATION_RULES),
						controller.getTableValidationRulePresenter(), bindedObject));
			} else if (bindedObject instanceof TableExtractionRuleDTO) {
				TableExtractionRuleDTO tableExtractionRule = (TableExtractionRuleDTO) bindedObject;
				BatchClassNavigationNode batchClassNavigationNode = new BatchClassNavigationNode(tableExtractionRule.getRuleName(),
						null, tableExtractionRule);
				node = new NavigationNode(batchClassNavigationNode);
				node.add(new BatchClassNavigationNode(LocaleDictionary
						.getConstantValue(BatchClassConstants.TABLE_COLUMN_EXTRACTION_RULES), controller.getColExtrRulePresenter(),
						bindedObject));
			} else if (bindedObject instanceof BatchClassModuleDTO) {
				BatchClassModuleDTO batchClassModule = (BatchClassModuleDTO) bindedObject;
				BatchClassNavigationNode batchClassNavigationNode = new BatchClassNavigationNode(batchClassModule.getModule()
						.getName(), controller.getPluginListPresenter(), batchClassModule);
				node = new NavigationNode(batchClassNavigationNode);
			} else if (bindedObject instanceof BatchClassPluginDTO) {
				BatchClassPluginDTO batchClassPlugin = (BatchClassPluginDTO) bindedObject;
				BatchClassNavigationNode batchClassNavigationNode = new BatchClassNavigationNode(batchClassPlugin.getPlugin()
						.getPluginName(), controller.getPluginPresenter(), batchClassPlugin);
				node = new NavigationNode(batchClassNavigationNode);
			}
		}
		return node;
	}

	public static final class NavigationNode {

		private BatchClassNavigationNode<?> parent;
		private List<BatchClassNavigationNode<?>> childList;

		private NavigationNode(final BatchClassNavigationNode<?> parent) {
			this.parent = parent;
			childList = new ArrayList<BatchClassNavigationNode<?>>();
		}

		private void add(final BatchClassNavigationNode<?> child) {
			childList.add(child);
		}

		public BatchClassNavigationNode<?> getParent() {
			return parent;
		}

		public List<BatchClassNavigationNode<?>> getChildList() {
			return childList;
		}
	}
}
