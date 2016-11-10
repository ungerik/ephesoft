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

package com.ephesoft.gxt.admin.client.view.navigator.draw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.view.navigator.BatchClassNavigatorView;
import com.ephesoft.gxt.admin.client.widget.BatchClassNavigationTree;
import com.ephesoft.gxt.admin.client.widget.property.BatchClassNavigationNode;
import com.ephesoft.gxt.admin.shared.constant.DTOPropertiesConstant;
import com.ephesoft.gxt.core.client.comparator.TreeNodeComparator;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassModuleDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;

@SuppressWarnings({"unchecked", "rawtypes"})
public class BatchClassNavigationCreator {

	private static BatchClassNavigationTree navigationTree;
	private static BatchClassManagementController controller;
	private static TreeNodeComparator<BatchClassNavigationNode<?>> nodeComparator;

	public static synchronized void createNavigator(final BatchClassDTO batchClassDTO, final BatchClassManagementController controller) {
		if (null != controller && null != batchClassDTO) {
			final BatchClassNavigationTree navigationTreeSet = getNavigationTree(controller);
			if (null != navigationTreeSet) {
				BatchClassNavigationCreator.controller = controller;
				BatchClassNavigationCreator.navigationTree = navigationTreeSet;
				nodeComparator = nodeComparator == null ? new TreeNodeComparator<BatchClassNavigationNode<?>>() : nodeComparator;
				navigationTree.getStore().clear();
				navigationTree.resetSelection();
				final String batchClassName = batchClassDTO.getName();
				final String batchClassIdentifier = batchClassDTO.getIdentifier();

				final String batchClassNodeDisplayName = StringUtil.concatenate(batchClassName, "-", batchClassIdentifier);
				final BatchClassNavigationNode<BatchClassDTO> navigationRootNode = new BatchClassNavigationNode<BatchClassDTO>(
						batchClassNodeDisplayName, null, batchClassDTO);
				navigationTreeSet.addRoot(navigationRootNode);
				addBatchClassLevelProperties(navigationRootNode);
			}
		}
	}

	private static BatchClassNavigationTree getNavigationTree(final BatchClassManagementController controller) {
		BatchClassNavigationTree navigationTree = null;
		final BatchClassNavigatorView navigatorView = controller.getNavigationView();
		if (null != navigatorView) {
			navigationTree = navigatorView.getNavigationTree();
		}
		return navigationTree;
	}

	private static void addBatchClassLevelProperties(final BatchClassNavigationNode<BatchClassDTO> batchClassNameNode) {
		final BatchClassDTO batchClassDTO = batchClassNameNode.getBindedObject();
		final BatchClassNavigationNode<BatchClassDTO> documentTypeHeader = new BatchClassNavigationNode<BatchClassDTO>(
				LocaleDictionary.getConstantValue(BatchClassConstants.DOCUMENT_TYPES), controller.getDocumentTypePresenter(),
				batchClassDTO);
		final BatchClassNavigationNode<BatchClassDTO> modulesHeader = new BatchClassNavigationNode<BatchClassDTO>(
				LocaleDictionary.getConstantValue(BatchClassConstants.MODULES), controller.getModulePresenter(), batchClassDTO);
		final BatchClassNavigationNode<BatchClassDTO> batchClassFieldHeader = new BatchClassNavigationNode<BatchClassDTO>(
				LocaleDictionary.getConstantValue(BatchClassConstants.BATCH_CLASS_FIELD), controller.getBatchClassFieldPresenter(),
				batchClassDTO);
		navigationTree.addChild(batchClassNameNode, documentTypeHeader);
		setDocumentTypeList(documentTypeHeader);
		navigationTree.addChild(batchClassNameNode, modulesHeader);
		setModulesList(modulesHeader);
		navigationTree.addChild(batchClassNameNode, batchClassFieldHeader);
		// navigationTree.selectNode(documentTypeHeader);
	}

	private static void setModulesList(final BatchClassNavigationNode<BatchClassDTO> modulesHeader) {
		final BatchClassDTO batchClassDTO = modulesHeader.getBindedObject();
		final Collection<BatchClassModuleDTO> modulesCollection = batchClassDTO.getModules();
		BatchClassNavigationNode<BatchClassModuleDTO> moduleNavigationNode = null;
		final Collection<BatchClassNavigationNode<?>> navigationNodeSet = new TreeSet<BatchClassNavigationNode<?>>(
				new Comparator<BatchClassNavigationNode>() {

					@Override
					public int compare(BatchClassNavigationNode node1, BatchClassNavigationNode node2) {
						int comparisonValue = 0;
						if (null != node1 && null != node2) {
							BatchClassModuleDTO dto1 = (BatchClassModuleDTO) node1.getBindedObject();
							BatchClassModuleDTO dto2 = (BatchClassModuleDTO) node2.getBindedObject();
							if (dto1 != null && dto2 != null) {
								final Integer firstObjectOrderId = dto1.getOrderNumber();
								final Integer secondObjectOrderId = dto2.getOrderNumber();
								if (firstObjectOrderId != null && secondObjectOrderId != null) {
									comparisonValue = firstObjectOrderId.compareTo(secondObjectOrderId);
								}
							}
						}
						return comparisonValue;
					}
				});
		if (!CollectionUtil.isEmpty(modulesCollection)) {
			for (final BatchClassModuleDTO batchClassModule : modulesCollection) {
				if (null != batchClassModule) {
					final String moduleName = batchClassModule.getModule().getName();
					moduleNavigationNode = new BatchClassNavigationNode<BatchClassModuleDTO>(moduleName,
							controller.getPluginListPresenter(), batchClassModule);
					navigationNodeSet.add(moduleNavigationNode);
				}
			}
			navigationTree.addChildren(modulesHeader, navigationNodeSet);
			addModuleLevelPluginList(navigationNodeSet);
		}
	}

	private static void addModuleLevelPluginList(final Collection<BatchClassNavigationNode<?>> moduleTypeNodeCollection) {
		if (!CollectionUtil.isEmpty(moduleTypeNodeCollection)) {
			for (final BatchClassNavigationNode<?> moduleTypeNode : moduleTypeNodeCollection) {
				addModuleLevelPluginList(moduleTypeNode);
			}
		}
	}

	private static void addModuleLevelPluginList(final BatchClassNavigationNode<?> moduleTypeNode) {
		BatchClassModuleDTO bindedModule = (BatchClassModuleDTO) moduleTypeNode.getBindedObject();
		if (null != bindedModule) {
			final Collection<BatchClassPluginDTO> pluginCollection = bindedModule.getBatchClassPlugins();
			BatchClassNavigationNode<BatchClassPluginDTO> pluginNode;
			final Collection<BatchClassNavigationNode<?>> pluginNodeSet = new TreeSet<BatchClassNavigationNode<?>>(
					new Comparator<BatchClassNavigationNode>() {

						@Override
						public int compare(BatchClassNavigationNode node1, BatchClassNavigationNode node2) {
							int comparisonValue = 0;
							if (null != node1 && null != node2) {
								BatchClassPluginDTO dto1 = (BatchClassPluginDTO) node1.getBindedObject();
								BatchClassPluginDTO dto2 = (BatchClassPluginDTO) node2.getBindedObject();
								if (dto1 != null && dto2 != null) {
									final Integer firstObjectOrderId = dto1.getOrderNumber();
									final Integer secondObjectOrderId = dto2.getOrderNumber();
									if (firstObjectOrderId != null && secondObjectOrderId != null) {
										comparisonValue = firstObjectOrderId.compareTo(secondObjectOrderId);
									}
								}
							}
							return comparisonValue;
						}
					});
			String fieldNodeName = null;
			if (!CollectionUtil.isEmpty(pluginCollection)) {
				for (final BatchClassPluginDTO pluginDTO : pluginCollection) {
					if (pluginDTO != null) {
						fieldNodeName = pluginDTO.getPlugin().getPluginName();
						pluginNode = new BatchClassNavigationNode<BatchClassPluginDTO>(fieldNodeName, controller.getPluginPresenter(),
								pluginDTO);
						pluginNodeSet.add(pluginNode);
					}
				}
				navigationTree.addChildren(moduleTypeNode, pluginNodeSet);
			}
		}
	}

	private static void setDocumentTypeList(final BatchClassNavigationNode<BatchClassDTO> documentTypeHeader) {
		final BatchClassDTO batchClassDTO = documentTypeHeader.getBindedObject();
		final Collection<DocumentTypeDTO> documentTypesCollection = batchClassDTO.getDocuments(false);
		BatchClassNavigationNode<DocumentTypeDTO> documentTypeNavigationNode = null;
		final Collection<BatchClassNavigationNode<?>> navigationNodeSet = new TreeSet<BatchClassNavigationNode<?>>(nodeComparator);
		if (!CollectionUtil.isEmpty(documentTypesCollection)) {
			for (final DocumentTypeDTO documentType : documentTypesCollection) {
				if (null != documentType) {
					final String documentName = documentType.getName();
					if (!DTOPropertiesConstant.DOCUMENT_TYPE_UNKNOWN.equalsIgnoreCase(documentName)) {
						documentTypeNavigationNode = new BatchClassNavigationNode<DocumentTypeDTO>(documentName, null, documentType);
						navigationNodeSet.add(documentTypeNavigationNode);
					}
				}
			}
			navigationTree.addChildren(documentTypeHeader, navigationNodeSet);
			addDocumentLevelProperties(navigationNodeSet);
		}
	}

	public static void addDocumentLevelProperties(final Collection<BatchClassNavigationNode<?>> documentTypeNodeCollection) {
		if (!CollectionUtil.isEmpty(documentTypeNodeCollection)) {
			Object bindedObject = null;
			DocumentTypeDTO bindedDocument = null;
			for (final BatchClassNavigationNode<?> documentTypeNode : documentTypeNodeCollection) {
				if (null != documentTypeNode) {
					bindedObject = documentTypeNode.getBindedObject();
					if (bindedObject instanceof DocumentTypeDTO) {
						bindedDocument = (DocumentTypeDTO) bindedObject;
						final BatchClassNavigationNode<DocumentTypeDTO> indexFieldHeaderNode = new BatchClassNavigationNode<DocumentTypeDTO>(
								LocaleDictionary.getConstantValue(BatchClassConstants.INDEX_FIELDS),
								controller.getIndexFieldPresenter(), bindedDocument);
						final BatchClassNavigationNode<DocumentTypeDTO> tablesHeaderNode = new BatchClassNavigationNode<DocumentTypeDTO>(
								LocaleDictionary.getConstantValue(BatchClassConstants.TABLES), controller.getTableInfoPresenter(),
								bindedDocument);
						navigationTree.addChild(documentTypeNode, indexFieldHeaderNode);
						navigationTree.addChild(documentTypeNode, tablesHeaderNode);
						addIndexFieldsList(indexFieldHeaderNode);
						addTablesList(tablesHeaderNode);
					}
				}
			}
		}
	}

	private static void addIndexFieldsList(final BatchClassNavigationNode<DocumentTypeDTO> indexFieldHeaderNode) {
		final ScheduledCommand indexFieldAdditionCommand = new ScheduledCommand() {

			@Override
			public void execute() {
				final DocumentTypeDTO bindedDocument = indexFieldHeaderNode.getBindedObject();
				if (null != bindedDocument) {
					final Collection<FieldTypeDTO> indexFieldCollection = bindedDocument.getFields();
					BatchClassNavigationNode<FieldTypeDTO> indexFieldNode;
					final Collection<BatchClassNavigationNode<?>> indexFieldNodeSet = new TreeSet<BatchClassNavigationNode<?>>(
							nodeComparator);
					String fieldNodeName = null;
					if (!CollectionUtil.isEmpty(indexFieldCollection)) {
						for (final FieldTypeDTO fieldTypeDTO : indexFieldCollection) {
							if (fieldTypeDTO != null) {
								fieldNodeName = fieldTypeDTO.getName();
								indexFieldNode = new BatchClassNavigationNode<FieldTypeDTO>(fieldNodeName, null, fieldTypeDTO);
								indexFieldNodeSet.add(indexFieldNode);
							}
						}
						navigationTree.addChildren(indexFieldHeaderNode, indexFieldNodeSet);
						addIndexFieldLevelProperties(indexFieldNodeSet);
					}
				}

			}
		};
		final Scheduler indexFieldAdditionScheduler = new SchedulerImpl();
		indexFieldAdditionScheduler.scheduleDeferred(indexFieldAdditionCommand);
	}

	public static void addIndexFieldLevelProperties(final Collection<BatchClassNavigationNode<?>> indexFieldNodeCollection) {
		if (!CollectionUtil.isEmpty(indexFieldNodeCollection)) {
			Object bindedObject = null;
			FieldTypeDTO bindedField = null;
			for (final BatchClassNavigationNode<?> indexFieldNode : indexFieldNodeCollection) {
				if (null != indexFieldNode) {
					bindedObject = indexFieldNode.getBindedObject();
					if (bindedObject instanceof FieldTypeDTO) {
						bindedField = (FieldTypeDTO) bindedObject;
						final BatchClassNavigationNode<FieldTypeDTO> keyValueFieldHeaderNode = new BatchClassNavigationNode<FieldTypeDTO>(
								LocaleDictionary.getConstantValue(BatchClassConstants.KV_EXTRACTION_RULE),
								controller.getKvExtractionPresenter(), bindedField);
						final BatchClassNavigationNode<FieldTypeDTO> validationRulesHeaderNode = new BatchClassNavigationNode<FieldTypeDTO>(
								LocaleDictionary.getConstantValue(BatchClassConstants.VALIDATION_RULES),
								controller.getRegexFieldPresenter(), bindedField);
						navigationTree.addChild(indexFieldNode, keyValueFieldHeaderNode);
						navigationTree.addChild(indexFieldNode, validationRulesHeaderNode);
					}
				}
			}
		}
	}

	private static void addTablesList(final BatchClassNavigationNode<DocumentTypeDTO> tableHeaderNode) {
		final ScheduledCommand tablesAdditionCommand = new ScheduledCommand() {

			@Override
			public void execute() {
				final DocumentTypeDTO bindedDocument = tableHeaderNode.getBindedObject();
				if (null != bindedDocument) {
					final Collection<TableInfoDTO> tableInfoCollection = bindedDocument.getTableInfos();
					BatchClassNavigationNode<TableInfoDTO> tableInfoNode;
					final Collection<BatchClassNavigationNode<?>> tablesNodeSet = new TreeSet<BatchClassNavigationNode<?>>(
							nodeComparator);
					String tableNodeName = null;
					if (!CollectionUtil.isEmpty(tableInfoCollection)) {
						for (final TableInfoDTO tableDTO : tableInfoCollection) {
							if (tableDTO != null) {
								tableNodeName = tableDTO.getName();
								tableInfoNode = new BatchClassNavigationNode<TableInfoDTO>(tableNodeName, null, tableDTO);
								tablesNodeSet.add(tableInfoNode);
							}
						}
						navigationTree.addChildren(tableHeaderNode, tablesNodeSet);
						addTableLevelProperties(tablesNodeSet);
					}
				}

			}
		};
		final Scheduler tablesAdditionScheduler = new SchedulerImpl();
		tablesAdditionScheduler.scheduleDeferred(tablesAdditionCommand);
	}

	private static void addTableLevelProperties(final Collection<BatchClassNavigationNode<?>> tableNodesCollection) {
		if (!CollectionUtil.isEmpty(tableNodesCollection)) {
			Object bindedObject = null;
			TableInfoDTO bindedTable = null;
			for (final BatchClassNavigationNode<?> indexFieldNode : tableNodesCollection) {
				if (null != indexFieldNode) {
					bindedObject = indexFieldNode.getBindedObject();
					if (bindedObject instanceof TableInfoDTO) {
						bindedTable = (TableInfoDTO) bindedObject;
						final BatchClassNavigationNode<TableInfoDTO> tableColumnHeaderNode = new BatchClassNavigationNode<TableInfoDTO>(
								LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_COLUMNS),
								controller.getTableColumnPresenter(), bindedTable);
						final BatchClassNavigationNode<TableInfoDTO> tableExtractionRulesHeaderNode = new BatchClassNavigationNode<TableInfoDTO>(
								LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_EXTRACTION_RULES),
								controller.getTabExtRulePresenter(), bindedTable);
						final BatchClassNavigationNode<TableInfoDTO> tableValidationRulesHeaderNode = new BatchClassNavigationNode<TableInfoDTO>(
								LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_VALIDATION_RULES),
								controller.getTableValidationRulePresenter(), bindedTable);
						navigationTree.addChild(indexFieldNode, tableColumnHeaderNode);
						navigationTree.addChild(indexFieldNode, tableExtractionRulesHeaderNode);
						navigationTree.addChild(indexFieldNode, tableValidationRulesHeaderNode);
						addTablesExtractionRulesList(tableExtractionRulesHeaderNode);
					}
				}
			}
		}
	}

	private static void addTablesExtractionRulesList(final BatchClassNavigationNode<TableInfoDTO> tableExtractionRuleHeaderNode) {
		final ScheduledCommand tablesAdditionCommand = new ScheduledCommand() {

			@Override
			public void execute() {
				final TableInfoDTO bindedDocument = tableExtractionRuleHeaderNode.getBindedObject();
				if (null != bindedDocument) {
					final Collection<TableExtractionRuleDTO> tableInfoCollection = bindedDocument.getTableExtractionRuleDTOs();
					BatchClassNavigationNode<TableExtractionRuleDTO> tableExtractionNode;
					final Collection<BatchClassNavigationNode<?>> tableExtractionRulesSet = new TreeSet<BatchClassNavigationNode<?>>(
							nodeComparator);
					String tableNodeName = null;
					if (!CollectionUtil.isEmpty(tableInfoCollection)) {
						for (final TableExtractionRuleDTO tableExtractionRuleDTO : tableInfoCollection) {
							if (tableExtractionRuleDTO != null) {
								tableNodeName = tableExtractionRuleDTO.getRuleName();
								tableExtractionNode = new BatchClassNavigationNode<TableExtractionRuleDTO>(tableNodeName, null,
										tableExtractionRuleDTO);
								tableExtractionRulesSet.add(tableExtractionNode);
							}
						}
						navigationTree.addChildren(tableExtractionRuleHeaderNode, tableExtractionRulesSet);
						addTableExtractionRuleLevelProperties(tableExtractionRulesSet);
					}
				}

			}
		};
		final Scheduler tablesExtractionAdditionScheduler = new SchedulerImpl();
		tablesExtractionAdditionScheduler.scheduleDeferred(tablesAdditionCommand);
	}

	private static void addTableExtractionRuleLevelProperties(
			final Collection<BatchClassNavigationNode<?>> tableExtractionRuleCollection) {
		if (!CollectionUtil.isEmpty(tableExtractionRuleCollection)) {
			Object bindedObject = null;
			TableExtractionRuleDTO bindedRule = null;
			for (final BatchClassNavigationNode<?> tableExtractionRuleNode : tableExtractionRuleCollection) {
				if (null != tableExtractionRuleNode) {
					bindedObject = tableExtractionRuleNode.getBindedObject();
					if (bindedObject instanceof TableExtractionRuleDTO) {
						bindedRule = (TableExtractionRuleDTO) bindedObject;
						final BatchClassNavigationNode<TableExtractionRuleDTO> tableColumnExtractionHeaderNode = new BatchClassNavigationNode<TableExtractionRuleDTO>(
								LocaleDictionary.getConstantValue(BatchClassConstants.TABLE_COLUMN_EXTRACTION_RULES),
								controller.getColExtrRulePresenter(), bindedRule);
						navigationTree.addChild(tableExtractionRuleNode, tableColumnExtractionHeaderNode);
					}
				}
			}
		}
	}

	/**
	 * Refresh module tree.
	 * 
	 * @param batchClassDTO the batch class dto
	 * @return the batch class navigation node
	 */
	public static BatchClassNavigationNode refreshModuleTree(BatchClassDTO batchClassDTO) {
		final BatchClassNavigationNode<BatchClassDTO> modulesHeader = navigationTree.getCurrentNode();
		final List<String> expandedNodes = new ArrayList<String>();
		final boolean previousState = navigationTree.isExpanded(modulesHeader);

		// Maintaining State of previous expanded Nodes.
		List<BatchClassNavigationNode> nodes = navigationTree.getChildren(modulesHeader);
		for (BatchClassNavigationNode node : nodes) {
			if (navigationTree.isExpanded(node)) {
				expandedNodes.add(node.getDisplayName());
			}
		}
		navigationTree.removeChildren(modulesHeader);
		modulesHeader.setBindedObject(batchClassDTO);
		setModulesList(modulesHeader);
		navigationTree.setExpanded(modulesHeader, previousState);
		nodes = navigationTree.getChildren(modulesHeader);
		for (BatchClassNavigationNode node : nodes) {
			if (expandedNodes.contains(node.getDisplayName())) {
				navigationTree.setExpanded(node, true);
			}
		}
		return modulesHeader;
	}

	/**
	 * Refresh plugin tree.
	 * 
	 * @param batchClassModuleDTO the batch class module dto
	 * @return the batch class navigation node
	 */
	public static BatchClassNavigationNode refreshPluginTree(BatchClassModuleDTO batchClassModuleDTO) {
		BatchClassNavigationNode<BatchClassModuleDTO> pluginHeader = navigationTree.getCurrentNode();

		// Added to Refresh Dependent Plugin Node.
		if (!batchClassModuleDTO.getModule().getName().equals(pluginHeader.getDisplayName())) {
			final BatchClassNavigationNode<BatchClassDTO> modulesHeader = (BatchClassNavigationNode<BatchClassDTO>) navigationTree
					.getParent(navigationTree.getCurrentNode());
			List<BatchClassNavigationNode> nodes = navigationTree.getChildren(modulesHeader);
			for (BatchClassNavigationNode node : nodes) {
				if (batchClassModuleDTO.getModule().getName().equals(node.getDisplayName())) {
					pluginHeader = node;
					break;
				}
			}
		}
		boolean previousState = navigationTree.isExpanded(pluginHeader);
		navigationTree.removeChildren(pluginHeader);
		pluginHeader.setBindedObject(batchClassModuleDTO);
		addModuleLevelPluginList(pluginHeader);
		navigationTree.setExpanded(pluginHeader, previousState);
		return pluginHeader;
	}
}
