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

package com.ephesoft.gxt.core.client.ui.widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.ui.widget.property.Node;
import com.ephesoft.gxt.core.client.ui.widget.property.Node.TreeNodeKeyProvider;
import com.ephesoft.gxt.core.client.ui.widget.property.Node.TreeNodeValueProvider;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;

@SuppressWarnings("rawtypes")
public abstract class AbstractTree<T extends Node> extends Tree<T, String> {

	protected T lastSelectedNode;

	protected T currentNode;

	private final Map<BasePresenter, T> presenterNodeMap;

	private boolean selectFirstNodeOnLoad = false;

	public AbstractTree() {
		super(new TreeStore<T>(new TreeNodeKeyProvider<T>()), new TreeNodeValueProvider<T>());
		this.setWidth(width);
		presenterNodeMap = new HashMap<BasePresenter, T>();
		this.addStoreAddHandler();
		this.getSelectionModel().addSelectionHandler(new SelectionHandler<T>() {

			@Override
			public void onSelection(SelectionEvent<T> event) {
				T selectedItem = event.getSelectedItem();
				if (null != selectedItem) {
					AbstractTree.this.lastSelectedNode = selectedItem;
				}
				onSelectionChange(lastSelectedNode);
			}
		});
	}

	private void addStoreAddHandler() {
		store.addStoreAddHandler(new StoreAddHandler<T>() {

			@Override
			public void onAdd(final StoreAddEvent<T> event) {
				final List<T> addedNodes = event.getItems();
				if (!CollectionUtil.isEmpty(addedNodes)) {
					for (final T node : addedNodes) {
						if (null != node) {
							presenterNodeMap.put(node.getPresenter(), node);
						}
					}
				}
			}
		});

		// Add handler to select first node.
		store.addStoreAddHandler(new StoreAddHandler<T>() {

			@Override
			public void onAdd(final StoreAddEvent<T> event) {
				final int storeSize = store.getAllItemsCount();
				if (storeSize == 1 && selectFirstNodeOnLoad) {
					if (!CollectionUtil.isEmpty(getRootItems())) {
						Scheduler.get().scheduleDeferred(new ScheduledCommand() {

							@Override
							public void execute() {
								selectNode(getRootItems().get(0));
							}
						});
					}
				}
			}
		});
	}

	public abstract void onSelectionChange(T newSelection);

	@SuppressWarnings("unchecked")
	public final void selectNode(final T treeNode) {
		if (null != treeNode) {
			this.getSelectionModel().select(false, treeNode);
			currentNode = treeNode;
		}
	}

	/**
	 * @return the selectedNode
	 */
	public final T getLastSelectedNode() {
		return lastSelectedNode;
	}

	public void addRoot(final T rootNode) {
		if (null != rootNode && null != store) {
			store.add(rootNode);
		}
	}

	public T getParent(final T childNode) {
		T parentNode = null;
		if (childNode != null && store != null) {
			parentNode = store.getParent(childNode);
		}
		return parentNode;
	}

	public void removeChildren(final T parentNode) {
		if (null != parentNode && store != null) {
			store.removeChildren(parentNode);
		}
	}

	public List<T> getChildren(final T parentNode) {
		List<T> childNodes = null;
		if (null != parentNode && null != store) {
			childNodes = store.getChildren(parentNode);
		}
		return childNodes;
	}

	public void addChild(final T parent, final T childNode) {
		if (null != parent && null != childNode && null != store) {
			store.add(parent, childNode);
		}
	}

	public void addChildren(final T parent, final Collection<T> childCollection) {
		if (!CollectionUtil.isEmpty(childCollection) && parent != null) {
			final List<T> childList = new ArrayList<T>(childCollection);
			this.addChildren(parent, childList);
		}
	}

	public void addChildren(final T parent, final List<T> childList) {
		if (!CollectionUtil.isEmpty(childList) && parent != null) {
			store.add(parent, childList);
		}
	}

	public T getCurrentNode() {
		return lastSelectedNode;
	}

	public void resetSelection() {
		lastSelectedNode = null;
		currentNode = null;
	}

	public void setCurrentNode(final T currentNode) {
		this.currentNode = currentNode;
	}

	public void select(final BasePresenter basePresenter) {
		if (null != basePresenter) {
			final T selectedNode = presenterNodeMap.get(basePresenter);
			if (null != selectedNode) {
				selectNode(selectedNode);
			}
		}
	}

	public List<T> getRootItems() {
		return store.getRootItems();
	}

	public List<T> getSiblings(final T navigationNode) {
		List<T> siblingsList = null;
		if (null != navigationNode) {
			T parent = store.getParent(navigationNode);
			siblingsList = parent == null ? getRootItems() : store.getChildren(parent);
		}
		return siblingsList;
	}

	public void remove(T node) {
		if (null != node) {
			store.remove(node);
		}
	}


	@Override
	protected void moveFocus(Element selectedElem) {
		// Focus was shifting in case of Firefox and IE while selecting node. moveFocus of the tree was responsible for the same.
		if (GXT.isChrome()) {
			super.moveFocus(selectedElem);
		}
	}

	/**
	 * Sets first node of the tree selected once the tree is loaded. Defaults to false.
	 */
	public void setSelectFirstNodeOnLoad(boolean selectFirstNodeOnLoad) {
		this.selectFirstNodeOnLoad = selectFirstNodeOnLoad;
	}
}
