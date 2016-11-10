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

package com.ephesoft.gxt.systemconfig.client.widget;

import java.util.HashMap;
import java.util.List;

import com.ephesoft.gxt.core.client.ui.widget.AbstractTree;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigCompositePresenter;
import com.ephesoft.gxt.systemconfig.client.widget.property.SystemConfigNavigationNode;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.client.Style.SelectionMode;

@SuppressWarnings("rawtypes")
public class SystemConfigNavigationTree<T extends SystemConfigNavigationNode> extends AbstractTree<SystemConfigNavigationNode<?>> {

	private HashMap<Object, SystemConfigNavigationNode> navigationTreeNodes = new HashMap<Object, SystemConfigNavigationNode>();

	public HashMap<Object, SystemConfigNavigationNode> getNavigationTreeNodes() {
		return navigationTreeNodes;
	}

	public SystemConfigNavigationTree() {
		NavigationTreeIcons INSTANCE = GWT.create(NavigationTreeIcons.class);
		this.getStyle().setNodeCloseIcon(INSTANCE.close());
		this.getStyle().setNodeOpenIcon(INSTANCE.open());
		this.getStyle().setLeafIcon(INSTANCE.leaf());
		this.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

		this.addBeforeSelectionHandler();
		this.setSelectFirstNodeOnLoad(true);
	}

	public void addTreeNodeInMap(Object key, SystemConfigNavigationNode value) {
		if (null != key && null != value) {
			navigationTreeNodes.put(key, value);
		}
	}

	public interface NavigationTreeIcons extends ClientBundleWithLookup {

		// @Source("")
		ImageResource open();

		// @Source("")
		ImageResource close();

		// @Source("")
		ImageResource leaf();
	}

	public SystemConfigNavigationNode getParentNode(String moduleParameter) {
		SystemConfigNavigationNode parentNode = null;
		if (navigationTreeNodes.containsKey(moduleParameter)) {
			parentNode = navigationTreeNodes.get(moduleParameter);
		}
		return parentNode;
	}

	@Override
	public void onSelectionChange(SystemConfigNavigationNode<?> newSelection) {
		if (null != newSelection) {
			final SystemConfigCompositePresenter<?, ?> bindedPresenter = newSelection.getBindedPresenter();
			if (null == bindedPresenter) {
				final List<SystemConfigNavigationNode<?>> childrenOfSelectedNode = getChildren(newSelection);
				if (CollectionUtil.isEmpty(childrenOfSelectedNode)) {

					// do noting if node does not have binded presenter nor any child.
				} else {
					final SystemConfigNavigationNode<?> firstChild = childrenOfSelectedNode.get(0);
					setExpanded(newSelection, true);
					selectNode(firstChild);
				}
			} else {
				newSelection.bindView();
			}
		}
	}

	public void removeTreeNodeInMap(Object key) {
		if (null != key) {
			navigationTreeNodes.remove(key);
		}
	}

	private void addBeforeSelectionHandler() {
		this.getSelectionModel().addBeforeSelectionHandler(new BeforeSelectionHandler<SystemConfigNavigationNode<?>>() {

			@Override
			public void onBeforeSelection(BeforeSelectionEvent<SystemConfigNavigationNode<?>> event) {
				SystemConfigNavigationNode<?> currentNode = SystemConfigNavigationTree.this.getCurrentNode();
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
}
