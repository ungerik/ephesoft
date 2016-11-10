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

package com.ephesoft.gxt.admin.client.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter;
import com.ephesoft.gxt.admin.client.util.NavigationNodeFactory.NavigationNode;
import com.ephesoft.gxt.admin.client.widget.property.BatchClassNavigationNode;
import com.ephesoft.gxt.core.client.ui.widget.AbstractTree;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;

public class BatchClassNavigationTree<T extends BatchClassNavigationNode> extends AbstractTree<BatchClassNavigationNode<?>> {

	private final Map<Object, List<BatchClassNavigationNode<?>>> objectPresenterMap;

	public BatchClassNavigationTree() {
		NavigationTreeIcons INSTANCE = GWT.create(NavigationTreeIcons.class);
		this.getStyle().setNodeCloseIcon(INSTANCE.close());
		this.getStyle().setNodeOpenIcon(INSTANCE.open());
		this.getStyle().setLeafIcon(INSTANCE.leaf());
		objectPresenterMap = new HashMap<Object, List<BatchClassNavigationNode<?>>>();
		this.addStoreAddHandler();
		this.addBeforeSelectionHandler();
	}

	@SuppressWarnings({"rawtypes"})
	@Override
	public void onSelectionChange(BatchClassNavigationNode<?> newSelection) {
		if (null != newSelection) {
			BatchClassNavigationTree.this.setCurrentNode(newSelection);
			BatchClassCompositePresenter compositePresenter = newSelection.getBindedPresenter();
			if (null == compositePresenter) {
				List<BatchClassNavigationNode<?>> navigationNodeList = this.store.getChildren(newSelection);
				BatchClassNavigationNode<?> navigationNode = CollectionUtil.isEmpty(navigationNodeList) ? null : navigationNodeList
						.get(0);
				if (null != navigationNode) {
					setExpanded(newSelection, true);
					selectNode(navigationNode);
				}
			} else {
				newSelection.bindView();
			}
		}
	}

	private void addStoreAddHandler() {
		store.addStoreAddHandler(new StoreAddHandler<BatchClassNavigationNode<?>>() {

			@Override
			public void onAdd(StoreAddEvent<BatchClassNavigationNode<?>> event) {
				final List<BatchClassNavigationNode<?>> addedNodes = event.getItems();
				if (!CollectionUtil.isEmpty(addedNodes)) {
					List<BatchClassNavigationNode<?>> nodeList;
					for (final BatchClassNavigationNode<?> node : addedNodes) {
						if (null != node) {
							if (objectPresenterMap.containsKey(node.getBindedObject())) {
								objectPresenterMap.get(node.getBindedObject()).add(node);
							} else {
								nodeList = new ArrayList<BatchClassNavigationNode<?>>();
								nodeList.add(node);
								objectPresenterMap.put(node.getBindedObject(), nodeList);
							}

						}
					}
				}
			}
		});
	}

	@Override
	public boolean isLeaf(BatchClassNavigationNode<?> model) {
		return super.isLeaf(model);
	}

	@Override
	public BatchClassNavigationNode<?> getCurrentNode() {
		return super.getCurrentNode();
	}

	public interface NavigationTreeIcons extends ClientBundleWithLookup {

		ImageResource open();

		ImageResource close();

		ImageResource leaf();
	}

	public Map<Object, List<BatchClassNavigationNode<?>>> getObjectPresenterMap() {
		return objectPresenterMap;
	}

	private void addBeforeSelectionHandler() {
		this.getSelectionModel().addBeforeSelectionHandler(new BeforeSelectionHandler<BatchClassNavigationNode<?>>() {

			@Override
			public void onBeforeSelection(BeforeSelectionEvent<BatchClassNavigationNode<?>> event) {
				BatchClassNavigationNode<?> currentNode = getCurrentNode();
				if (null != currentNode) {
					if (currentNode.getBindedPresenter() != null) {
						if (!currentNode.getBindedPresenter().isValid()) {
							event.cancel();
						}
					}
				}
			}
		});
	}

	public void renameNode(final BatchClassNavigationNode<?> navigationNode, final String newName) {
		if (null != navigationNode && !navigationNode.getDisplayName().equals(newName)) {
			List<BatchClassNavigationNode<?>> siblings = getSiblings(navigationNode);
			BatchClassNavigationNode<?> parent = getParent(navigationNode);
			boolean isParentExpanded = false;
			if (null != parent) {
				isParentExpanded = this.isExpanded(parent);
			}
			if (!CollectionUtil.isEmpty(siblings)) {
				com.sencha.gxt.data.shared.TreeStore.TreeNode<BatchClassNavigationNode<?>> node = store.getSubTree(navigationNode);
				if (null != node) {
					List<? extends com.sencha.gxt.data.shared.TreeStore.TreeNode<BatchClassNavigationNode<?>>> childSubtree = node
							.getChildren();
					boolean isExpanded = this.isExpanded(navigationNode);
					store.remove(navigationNode);
					navigationNode.setDisplayName(newName);
					if (null == parent) {
						store.add(navigationNode);
					} else {
						int index = DisplayNamesComparator.getInsertionIndex(siblings, navigationNode);
						store.insert(parent, index, navigationNode);
						this.setExpanded(parent, isParentExpanded);
					}
					store.addSubTree(navigationNode, 0, childSubtree);
					this.setExpanded(navigationNode, isExpanded);
				}
			}
		}
	}

	public void addNode(BatchClassNavigationNode<?> parentNode, final NavigationNode node, final int nodeIndex) {
		if (null != parentNode && null != node) {
			List<BatchClassNavigationNode<?>> nodesList = store.getChildren(parentNode);
			BatchClassNavigationNode<?> firstChild = node.getParent();
			int index=nodeIndex;
			if(nodeIndex==-1){
				index = DisplayNamesComparator.getInsertionIndex(nodesList, firstChild);
			}
			store.insert(parentNode, index, firstChild);
			List<BatchClassNavigationNode<?>> childNodes = node.getChildList();
			for (final BatchClassNavigationNode<?> child : childNodes) {
				if (null != child) {
					store.add(firstChild, child);
				}
			}
		}
	}

	public BatchClassNavigationNode<?> getNode(BatchClassNavigationNode<?> node, Object bindedObject) {
		BatchClassNavigationNode<?> foundedNode = null;
		if (null != bindedObject && null != node) {
			List<BatchClassNavigationNode<?>> childList = store.getChildren(node);
			if (!CollectionUtil.isEmpty(childList)) {
				for (BatchClassNavigationNode<?> childNode : childList) {
					if (null != childNode && childNode.getBindedObject() == bindedObject) {
						foundedNode = childNode;
						break;
					}
				}
			}
		}
		return foundedNode;
	}
	
	

	private static class DisplayNamesComparator {

		public static int getInsertionIndex(final List<BatchClassNavigationNode<?>> nodesList,
				final BatchClassNavigationNode<?> nodeToBeAdded) {
			int index = 0;
			if (!CollectionUtil.isEmpty(nodesList) && nodeToBeAdded != null) {
				String navigationNodeName = nodeToBeAdded.getDisplayName().toLowerCase();
				for (final BatchClassNavigationNode<?> navigationNode : nodesList) {
					if (null != navigationNode && navigationNode != nodeToBeAdded) {
						if (navigationNode.getDisplayName().toLowerCase().compareTo(navigationNodeName) > 0) {
							break;
						}
						index++;
					}
				}
			}
			return index;
		}
	}

}
