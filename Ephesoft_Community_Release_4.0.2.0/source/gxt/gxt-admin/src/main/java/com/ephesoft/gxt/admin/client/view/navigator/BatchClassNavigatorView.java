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

package com.ephesoft.gxt.admin.client.view.navigator;

import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter;
import com.ephesoft.gxt.admin.client.presenter.navigator.BatchClassNavigatorPresenter;
import com.ephesoft.gxt.admin.client.util.NavigationNodeFactory.NavigationNode;
import com.ephesoft.gxt.admin.client.widget.BatchClassNavigationTree;
import com.ephesoft.gxt.admin.client.widget.property.BatchClassNavigationNode;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.ui.widget.property.Node;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassModuleDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.core.client.util.DelayedTask;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;

@SuppressWarnings({"rawtypes", "unchecked"})
public class BatchClassNavigatorView extends View<BatchClassNavigatorPresenter> {

	@Override
	public void initialize() {
	}

	@UiField
	protected BatchClassNavigationTree navigationTree;

	@UiField
	protected ContentPanel treePanel;

	private BatchClassCompositePresenter<?, ?> preLoadPresenter;

	interface Binder extends UiBinder<Component, BatchClassNavigatorView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public BatchClassNavigatorView() {
		initWidget(binder.createAndBindUi(this));
		navigationTree.addStyleName("tree");
		navigationTree.setVisible(false);
		treePanel.addStyleName("navigationPanel");
		treePanel.addStyleName("leftBottomPanel");

		this.navigationTree.getSelectionModel().addSelectionHandler(new SelectionHandler<BatchClassNavigationNode<Object>>() {

			@Override
			public void onSelection(SelectionEvent<BatchClassNavigationNode<Object>> event) {
			}
		});
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		DelayedTask task = new DelayedTask() {

			@Override
			public void onExecute() {
				navigationTree.setHeight(getOffsetHeight());
			}
		};
		task.delay(20);
	}

	/**
	 * @return the navigationTree
	 */
	public BatchClassNavigationTree getNavigationTree() {
		return navigationTree;
	}

	public void refreshTree(BatchClassDTO loadedBatchClass, BatchClassDTO newBatchClass) {
		Map<Object, List<BatchClassNavigationNode>> objectPresenterMap = navigationTree.getObjectPresenterMap();
		List<BatchClassNavigationNode> nodes = null;
		if (loadedBatchClass != null) {
			if (objectPresenterMap.containsKey(loadedBatchClass)) {
				nodes = objectPresenterMap.get(loadedBatchClass);
				objectPresenterMap.remove(loadedBatchClass);
				addBindedObjectInNodes(nodes, newBatchClass);
				objectPresenterMap.put(newBatchClass, nodes);
				refreshModules(loadedBatchClass, newBatchClass, objectPresenterMap);
				refreshDocuments(loadedBatchClass, newBatchClass, objectPresenterMap);
			}
			BatchClassNavigationNode currentNode = navigationTree.getCurrentNode();
			navigationTree.selectNode(currentNode);
		}
	}

	private void addBindedObjectInNodes(List<BatchClassNavigationNode> nodes, Object bindedObject) {
		if (!CollectionUtil.isEmpty(nodes)) {
			for (BatchClassNavigationNode node : nodes) {
				node.setBindedObject(bindedObject);
			}
		}
	}

	public void refreshModules(BatchClassDTO loadedBatchClass, BatchClassDTO newBatchClass,
			Map<Object, List<BatchClassNavigationNode>> map) {
		List<BatchClassNavigationNode> nodes = null;
		BatchClassModuleDTO moduleDTO = null;
		if (!CollectionUtil.isEmpty(loadedBatchClass.getModules(true))) {
			for (BatchClassModuleDTO bcModuleTypeDTO : loadedBatchClass.getModules(true)) {
				if (newBatchClass != null) {
					moduleDTO = newBatchClass.getModuleByWorkflowName(bcModuleTypeDTO.getWorkflowName());
				}
				if (map.containsKey(bcModuleTypeDTO)) {
					nodes = map.get(bcModuleTypeDTO);
					map.remove(bcModuleTypeDTO);
					addBindedObjectInNodes(nodes, moduleDTO);
					if (!CollectionUtil.isEmpty(nodes) && moduleDTO != null) {
						map.put(moduleDTO, nodes);
					}
					refreshBatchClassPlugins(bcModuleTypeDTO, moduleDTO, map);
				}
			}
		}
	}

	public void refreshBatchClassPlugins(BatchClassModuleDTO bcModuleTypeDTO, BatchClassModuleDTO moduleDTO,
			Map<Object, List<BatchClassNavigationNode>> map) {
		List<BatchClassNavigationNode> nodes = null;
		BatchClassPluginDTO pluginDTO = null;
		if (!CollectionUtil.isEmpty(bcModuleTypeDTO.getBatchClassPlugins())) {
			for (BatchClassPluginDTO bcPuginDTO : bcModuleTypeDTO.getBatchClassPlugins()) {
				if (null != moduleDTO) {
					pluginDTO = moduleDTO.getPluginByName(bcPuginDTO.getPlugin().getPluginName());
				}
				if (map.containsKey(bcPuginDTO)) {
					nodes = map.get(bcPuginDTO);
					map.remove(bcPuginDTO);
					addBindedObjectInNodes(nodes, pluginDTO);
					if (!CollectionUtil.isEmpty(nodes) && pluginDTO != null) {
						map.put(pluginDTO, nodes);
					}
				}
			}
		}
	}

	public void refreshDocuments(BatchClassDTO loadedBatchClass, BatchClassDTO newBatchClass,
			Map<Object, List<BatchClassNavigationNode>> map) {
		List<BatchClassNavigationNode> nodes = null;
		DocumentTypeDTO docTypeDTO = null;
		if (!CollectionUtil.isEmpty(loadedBatchClass.getDocuments(true))) {
			for (DocumentTypeDTO documentTypeDTO : loadedBatchClass.getDocuments(true)) {
				if (null != newBatchClass) {
					docTypeDTO = newBatchClass.getDocTypeByName(documentTypeDTO.getName());
				}
				if (map.containsKey(documentTypeDTO)) {
					nodes = map.get(documentTypeDTO);
					map.remove(documentTypeDTO);
					addBindedObjectInNodes(nodes, docTypeDTO);
					if (docTypeDTO != null && !CollectionUtil.isEmpty(nodes)) {
						map.put(docTypeDTO, nodes);
					}
					refreshDocumentFields(documentTypeDTO, docTypeDTO, map);
					refreshTables(documentTypeDTO, docTypeDTO, map);
				}
			}
		}
	}

	public void refreshDocumentFields(DocumentTypeDTO documentTypeDTO, DocumentTypeDTO newDocumentTypeDTO,
			Map<Object, List<BatchClassNavigationNode>> map) {
		List<BatchClassNavigationNode> nodes = null;
		FieldTypeDTO fieldDTO = null;
		if (!CollectionUtil.isEmpty(documentTypeDTO.getFields(true))) {
			for (FieldTypeDTO fieldTypeDTO : documentTypeDTO.getFields(true)) {
				if (null != newDocumentTypeDTO) {
					fieldDTO = newDocumentTypeDTO.getFieldTypeByName(fieldTypeDTO.getName());
				}
				if (map.containsKey(fieldTypeDTO)) {
					nodes = map.get(fieldTypeDTO);
					map.remove(fieldTypeDTO);
					addBindedObjectInNodes(nodes, fieldDTO);
					if (fieldDTO != null && !CollectionUtil.isEmpty(nodes)) {
						map.put(fieldDTO, nodes);
					}
				}
			}
		}
	}

	public void refreshTables(DocumentTypeDTO documentTypeDTO, DocumentTypeDTO newDocumentTypeDTO,
			Map<Object, List<BatchClassNavigationNode>> map) {
		List<BatchClassNavigationNode> nodes = null;
		TableInfoDTO tableDTO = null;
		if (!CollectionUtil.isEmpty(documentTypeDTO.getTableInfos(true))) {
			for (TableInfoDTO tableInfoDTO : documentTypeDTO.getTableInfos(true)) {
				if (null != newDocumentTypeDTO) {
					tableDTO = newDocumentTypeDTO.getTableInfoByName(tableInfoDTO.getName());
				}
				if (map.containsKey(tableInfoDTO)) {
					nodes = map.get(tableInfoDTO);
					map.remove(tableInfoDTO);
					addBindedObjectInNodes(nodes, tableDTO);
					if (tableDTO != null && !CollectionUtil.isEmpty(nodes)) {
						map.put(tableDTO, nodes);
					}
					refreshTableExtractionRules(tableInfoDTO, tableDTO, map);
				}
			}
		}
	}

	public void refreshTableExtractionRules(TableInfoDTO tableInfoDTO, TableInfoDTO newtableInfoDTO,
			Map<Object, List<BatchClassNavigationNode>> map) {
		List<BatchClassNavigationNode> nodes;
		TableExtractionRuleDTO extractionRuleDTO = null;
		if (!CollectionUtil.isEmpty(tableInfoDTO.getTableExtractionRuleDTOs(true))) {
			for (TableExtractionRuleDTO tableExtractionRuleDTO : tableInfoDTO.getTableExtractionRuleDTOs(true)) {
				if (null != newtableInfoDTO) {
					extractionRuleDTO = newtableInfoDTO.getTableExtractionRuleByName(tableExtractionRuleDTO.getRuleName());
				}
				if (map.containsKey(tableExtractionRuleDTO)) {
					nodes = map.get(tableExtractionRuleDTO);
					map.remove(tableExtractionRuleDTO);
					addBindedObjectInNodes(nodes, extractionRuleDTO);
					if (tableExtractionRuleDTO != null && !CollectionUtil.isEmpty(nodes)) {
						map.put(extractionRuleDTO, nodes);
					}
					refreshTableColumnExtraction(tableExtractionRuleDTO, extractionRuleDTO, map);
				}
			}
		}
	}

	public void refreshTableColumnExtraction(TableExtractionRuleDTO tableExtractionRuleDTO, TableExtractionRuleDTO newExtRuleDTO,
			Map<Object, List<BatchClassNavigationNode>> map) {
		List<BatchClassNavigationNode> nodes;
		TableColumnExtractionRuleDTO colExtrRuleDTO = null;
		if (!CollectionUtil.isEmpty(tableExtractionRuleDTO.getTableColumnExtractionRuleDTOs(true))) {
			for (TableColumnExtractionRuleDTO tabColExtRuleDTO : tableExtractionRuleDTO.getTableColumnExtractionRuleDTOs(false)) {
				if (null != newExtRuleDTO) {
					colExtrRuleDTO = newExtRuleDTO.getTableColumnExtractionRuleByColumnName(tabColExtRuleDTO.getColumnName());
				}
				if (map.containsKey(tabColExtRuleDTO)) {
					nodes = map.get(tabColExtRuleDTO);
					map.remove(tabColExtRuleDTO);
					addBindedObjectInNodes(nodes, colExtrRuleDTO);
					if (colExtrRuleDTO != null && !CollectionUtil.isEmpty(nodes)) {
						map.put(colExtrRuleDTO, nodes);
					}
				}
			}
		}
	}

	public void bindChildView(final BatchClassCompositePresenter compositePresenter, final Object bindedObject) {
		if (navigationTree.isVisible()) {
			openFirstChild(bindedObject);
		} else {
			if(bindedObject instanceof BatchClassDTO) {
				presenter.getController().setSelectedBatchClass((BatchClassDTO)bindedObject);
			}
			presenter.acquireLock(compositePresenter);
		}
	}

	private void openFirstChild(final Object currentObject) {
		Node currentNode = navigationTree.getCurrentNode();
		if (null != currentNode) {
			List<BatchClassNavigationNode<?>> childList = navigationTree.getChildren(currentNode);
			if (!CollectionUtil.isEmpty(childList)) {
				BatchClassNavigationNode<?> navigationNode = childList.get(0);
				for (BatchClassNavigationNode<?> iteratedNode : childList) {
					if (null != iteratedNode && iteratedNode.getBindedObject() == currentObject) {
						navigationNode = iteratedNode;
						break;
					}
				}
				navigationTree.selectNode(navigationNode);
			}
		}
	}

	public void selectPreNavigationLoadPresenter() {
		if (this.preLoadPresenter != null) {
			Timer timer = new Timer() {

				@Override
				public void run() {
					navigationTree.select(preLoadPresenter);
				}
			};
			timer.schedule(50);
		}
	}

	public void maskBCMTree(boolean isMaskingEnable) {
		if (isMaskingEnable) {
			this.treePanel.mask();
		} else {
			this.treePanel.unmask();
		}
	}

	public BatchClassNavigationNode<?> getCurrentNode() {
		return navigationTree.getCurrentNode();
	}

	public BatchClassNavigationNode<?> getChild(BatchClassNavigationNode<?> parent, final Object bindedChildObject) {
		BatchClassNavigationNode<?> child = null;
		if (null != bindedChildObject && null != parent) {
			List<BatchClassNavigationNode<?>> nodesList = navigationTree.getChildren(parent);
			if (!CollectionUtil.isEmpty(nodesList)) {
				for (final BatchClassNavigationNode<?> node : nodesList) {
					if (null != node && node.getBindedObject() == bindedChildObject) {
						child = node;
						break;
					}
				}
			}
		}
		return child;
	}

	public void removeChildren(final BatchClassNavigationNode<?> node) {
		if (null != node) {
			navigationTree.removeChildren(node);
		}
	}

	public void rename(final BatchClassNavigationNode<?> node, final String newName) {
		navigationTree.renameNode(node, newName);
	}

	public void addNode(BatchClassNavigationNode<?> parent, NavigationNode childNode, int index) {
		navigationTree.addNode(parent, childNode, index);
	}

	public BatchClassNavigationNode<?> getNode(BatchClassNavigationNode<?> parentNode, Object bindedObject) {
		return navigationTree.getNode(parentNode, bindedObject);
	}

	public void removeNode(BatchClassNavigationNode<?> nodeToRemove) {
		navigationTree.remove(nodeToRemove);
	}

	public BatchClassCompositePresenter<?, ?> getPreLoadPresenter() {
		return preLoadPresenter;
	}

	public void setPreLoadPresenter(BatchClassCompositePresenter<?, ?> preLoadPresenter) {
		this.preLoadPresenter = preLoadPresenter;
	}

	public void setNavigationTreeVisible(boolean setVisible) {
		this.navigationTree.setVisible(setVisible);
	}
}
