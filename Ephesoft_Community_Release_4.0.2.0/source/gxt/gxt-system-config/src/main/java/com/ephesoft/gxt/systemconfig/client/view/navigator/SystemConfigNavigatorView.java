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

package com.ephesoft.gxt.systemconfig.client.view.navigator;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigCompositePresenter;
import com.ephesoft.gxt.systemconfig.client.presenter.navigator.SystemConfigNavigatorPresenter;
import com.ephesoft.gxt.systemconfig.client.widget.SystemConfigNavigationTree;
import com.ephesoft.gxt.systemconfig.client.widget.property.SystemConfigModules;
import com.ephesoft.gxt.systemconfig.client.widget.property.SystemConfigNavigationNode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class SystemConfigNavigatorView extends View<SystemConfigNavigatorPresenter> {

	@SuppressWarnings("rawtypes")
	@UiField
	protected SystemConfigNavigationTree navigationTree;

	@UiField
	protected VerticalLayoutContainer treeContainer;

	interface Binder extends UiBinder<Component, SystemConfigNavigatorView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public SystemConfigNavigatorView() {
		super();
		initWidget(binder.createAndBindUi(this));
		treeContainer.addStyleName("systemConfigTreeContainer");
		navigationTree.addStyleName("systemConfigNavigationTree");
		setWidgetIds();
	}

	@Override
	public void initialize() {
	}

	public void createTree() {
		for (SystemConfigModules module : SystemConfigModules.values()) {
			SystemConfigCompositePresenter<?, String> compositePresenter = presenter.getCompositePresenter(module);
			if (module.getParentModule() == null) {
				addNewTreeRoot(module.getModuleParameter(), compositePresenter);
			} else {
				SystemConfigNavigationNode parentNode = navigationTree.getParentNode(module.getParentModule().getModuleParameter());
				if (null != parentNode) {
					String displayParameter = LocaleDictionary.getConstantValue(module.getModuleParameter());
					SystemConfigNavigationNode<String> childNode = new SystemConfigNavigationNode<String>(displayParameter,
							compositePresenter, module.getModuleParameter());
					navigationTree.addChild(parentNode, childNode);
				}
			}

		}
		navigationTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}

	public void addNewTreeRoot(String comparisionParameter, SystemConfigCompositePresenter<?, String> compositePresenter) {
		String displayParameter = LocaleDictionary.getConstantValue(comparisionParameter);
		SystemConfigNavigationNode<String> newNode = new SystemConfigNavigationNode<String>(displayParameter, compositePresenter,
				comparisionParameter);
		addRoot(newNode);
		navigationTree.addTreeNodeInMap(comparisionParameter, newNode);
	}

	private void addRoot(SystemConfigNavigationNode<String> treeNode) {
		navigationTree.addRoot(treeNode);
	}

	private void setWidgetIds() {
		WidgetUtil.setID(treeContainer, "SCNV_treeContainer");
		WidgetUtil.setID(navigationTree, "SCNV_navigationTree");
	}

	public SystemConfigNavigationNode getParentNodeFromTree(String moduleParameter) {
		return navigationTree.getParentNode(moduleParameter);
	}

	public void addChildrenInTree(SystemConfigNavigationNode parentNode, SystemConfigNavigationNode<?> childNode) {
		if (null != parentNode && null != childNode) {
			navigationTree.addTreeNodeInMap(childNode.getBindedObject(), childNode);
			navigationTree.addChild(parentNode, childNode);
		}
	}

	public void removeAllChildernFromTree(SystemConfigNavigationNode workflowManagmentNode) {
		if (null != workflowManagmentNode) {
			navigationTree.removeChildren(workflowManagmentNode);

		}
	}

	public void selectFirstChildOfTree() {
		if (null != navigationTree) {
			TreeStore<SystemConfigNavigationNode> store = navigationTree.getStore();
			if (null != store) {
				SystemConfigNavigationNode<?> firstChild = store.getAll().get(0);
				navigationTree.selectNode(firstChild);
			}
		}
	}

	public SystemConfigNavigationTree getNavigationTree() {
		return navigationTree;
	}

	public void insertNodeAtSpecifiedIndex(Object parentNode, int index, Object childNode) {

		navigationTree.getStore().insert(parentNode, index, childNode);
	}
}
