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

package com.ephesoft.gxt.foldermanager.client.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.Tree;
import com.ephesoft.gxt.core.client.ui.widget.property.TreeNode;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.FileType;
import com.ephesoft.gxt.core.shared.dto.FolderManagerDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.foldermanager.client.event.FolderTreeRefreshEvent;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementNavigatorPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.resources.client.ClientBundleWithLookup;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreAddEvent.StoreAddHandler;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BeforeCollapseItemEvent;
import com.sencha.gxt.widget.core.client.event.BeforeCollapseItemEvent.BeforeCollapseItemHandler;
import com.sencha.gxt.widget.core.client.event.BeforeExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.BeforeExpandItemEvent.BeforeExpandItemHandler;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent;
import com.sencha.gxt.widget.core.client.event.ExpandItemEvent.ExpandItemHandler;

public class FolderManagementNavigatorView extends View<FolderManagementNavigatorPresenter> {

	@UiField
	protected ComboBox batchClassComboBox;

	@UiField(provided = true)
	protected Tree navigationTree;

	@UiField
	protected VerticalLayoutContainer treeContainer;

	final NavigationTreeIcons INSTANCE = GWT.create(NavigationTreeIcons.class);

	public static Map<String, TreeNode> treeData = new HashMap<String, TreeNode>();

	interface Binder extends UiBinder<Widget, FolderManagementNavigatorView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public FolderManagementNavigatorView() {
		super();
		initWidget(binder.createAndBindUi(this));
		this.batchClassComboBox.setEditable(false);
		batchClassComboBox.addStyleName("folderOptionsBox");
		navigationTree.addStyleName("folderNavigationTree");
		treeContainer.addStyleName("treeContainer");
		WidgetUtil.setID(navigationTree, "FMNV_navigationTree");
		WidgetUtil.setID(batchClassComboBox, "FMNV_bcComboBox");
		WidgetUtil.setID(treeContainer, "FMNV_treeContainer");
		this.navigationTree.getStyle().setNodeCloseIcon(INSTANCE.close());
		this.navigationTree.getStyle().setNodeOpenIcon(INSTANCE.open());
		this.batchClassComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				presenter.comboboxSelection();
				batchClassComboBox.setToolTip(batchClassComboBox.getValue());
			}
		});
	}

	@Override
	public void initialize() {

		navigationTree = new Tree() {

			@Override
			public boolean isLeaf(com.ephesoft.gxt.core.client.ui.widget.property.TreeNode model) {
				return false;
			}
		};

		navigationTree.addBeforeExpandHandler(new BeforeExpandItemHandler<TreeNode>() {

			@Override
			public void onBeforeExpand(BeforeExpandItemEvent<TreeNode> event) {
				TreeNode father = event.getItem();
				if (!CollectionUtil.isEmpty(navigationTree.getChildren(father))) {
					navigationTree.removeChildren(father);
				}
			}

		});
		navigationTree.getStore().addStoreAddHandler(new StoreAddHandler<TreeNode>() {

			@Override
			public void onAdd(StoreAddEvent<TreeNode> event) {
				presenter.selectTreeNode(event.getItems());
			}

		});
		navigationTree.addExpandHandler(new ExpandItemHandler<TreeNode>() {

			@Override
			public void onExpand(ExpandItemEvent<TreeNode> expandEvent) {
				presenter.fireExpandEvent(expandEvent.getItem());
			}
		});

		navigationTree.getSelectionModel().addSelectionHandler(new SelectionHandler<TreeNode>() {

			@Override
			public void onSelection(SelectionEvent<TreeNode> event) {
				TreeNode selectedNode = event.getSelectedItem();
				navigationTree.scrollIntoView(selectedNode);
				presenter.setCurrentTreeNode(selectedNode);
				String path = findNodePath(selectedNode, getNavigationTree());
				presenter.setFooterPath(path);
				presenter.refreshGrid(path, selectedNode);
			}
		});

		navigationTree.addBeforeCollapseHandler(new BeforeCollapseItemHandler<TreeNode>() {

			@Override
			public void onBeforeCollapse(BeforeCollapseItemEvent<TreeNode> event) {
				TreeNode collapsibleNode = event.getItem();
				TreeNode currentNode = presenter.getCurrentTreeNode();
				TreeNode parentNode = navigationTree.getParent(currentNode);
				while (null != parentNode) {
					if (collapsibleNode.getDisplayName().equals(parentNode.getDisplayName())) {
						presenter.setCurrentTreeNode(collapsibleNode);
						presenter.getController().getEventBus().fireEvent(new FolderTreeRefreshEvent());
					}
					parentNode = navigationTree.getParent(parentNode);
				}
			}

		});

	}

	public void createFolderSystemTree(final String parentFolderName) {
		TreeNode rootNode = new TreeNode(parentFolderName, null);
		navigationTree.addRoot(rootNode);
		presenter.setCurrentTreeNode(rootNode);
	}

	public void openTreeNode(TreeNode father, List<FolderManagerDTO> folderManagerDTOsList, Tree navigationTree) {
		for (FolderManagerDTO file : folderManagerDTOsList) {
			if (file.getKind() == FileType.DIR) {
				TreeNode newNode = new TreeNode(file.getFileName(), null);
				treeData.put(file.getPath(), newNode);
				navigationTree.addChild(father, newNode);
			}
		}
	}

	public ComboBox getComboBox() {
		return this.batchClassComboBox;
	}

	public String findNodePath(TreeNode node, Tree navigationTree) {
		return presenter.findNodePath(node, navigationTree);
	}

	public void fetchTreeItems(final TreeNode father, String path, Tree navigationTree) {
		presenter.fetchTreeItems(father, path, navigationTree);
	}

	public Tree getNavigationTree() {
		return this.navigationTree;
	}

	public interface NavigationTreeIcons extends ClientBundleWithLookup {

		ImageResource open();

		ImageResource close();

		ImageResource leaf();
	}
}
