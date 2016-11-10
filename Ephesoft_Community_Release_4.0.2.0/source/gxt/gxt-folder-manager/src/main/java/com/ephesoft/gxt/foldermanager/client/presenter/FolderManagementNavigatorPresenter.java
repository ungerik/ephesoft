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

package com.ephesoft.gxt.foldermanager.client.presenter;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.Tree;
import com.ephesoft.gxt.core.client.ui.widget.property.TreeNode;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.FolderManagerDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.foldermanager.client.controller.FolderManagementController;
import com.ephesoft.gxt.foldermanager.client.event.BatchClassChangeEvent;
import com.ephesoft.gxt.foldermanager.client.event.CurrentPathRefreshEvent;
import com.ephesoft.gxt.foldermanager.client.event.FolderCutEvent;
import com.ephesoft.gxt.foldermanager.client.event.FolderGridRefreshEvent;
import com.ephesoft.gxt.foldermanager.client.event.FolderTreeRefreshEvent;
import com.ephesoft.gxt.foldermanager.client.event.FolderUpEvent;
import com.ephesoft.gxt.foldermanager.client.event.TreeExpandEvent;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementConstants;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementMessages;
import com.ephesoft.gxt.foldermanager.client.view.FolderManagementNavigatorView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.TreeStore;

public class FolderManagementNavigatorPresenter extends FolderManagementBasePresenter<FolderManagementNavigatorView> {

	private String parentFolderPath;

	private String parentFolderName;

	private TreeNode currentTreeNode;

	private String selectedBatchClassID;

	private Label footer;

	private FolderManagerDTO selectedGridDTO;

	private Map<String, String> batchClassesNameMap;

	private String folderName;

	// This is to show error dialog only once to user
	private boolean isErrorDialogShown = false;

	interface CustomEventBinder extends EventBinder<FolderManagementNavigatorPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public FolderManagementNavigatorPresenter(FolderManagementController controller, FolderManagementNavigatorView view,
			String parentFolderPath, Map<String, String> batchClassesMap, Label footer) {
		super(controller, view);
		this.setParentFolderPath(parentFolderPath);
		this.setParentFolderName(getBaseFolderName(parentFolderPath));
		this.batchClassesNameMap = batchClassesMap;
		this.footer = footer;
	}

	@Override
	public void bind() {

	}

	private String getBaseFolderName(String path) {
		String baseFolderName = null;
		int lastIndex = path.lastIndexOf("\\");
		baseFolderName = path.substring(lastIndex + 1);
		return baseFolderName;
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void onTreeExpand(TreeExpandEvent TreeExpandEvent) {
		TreeNode targetNode = TreeExpandEvent.getTargetNode();
		String path = findNodePath(targetNode, view.getNavigationTree());
		fetchTreeItems(targetNode, path, view.getNavigationTree());
	}

	@EventHandler
	public void onBatchClassChange(BatchClassChangeEvent batchClassChangeEvent) {
		selectedBatchClassID = batchClassChangeEvent.getBatchClassID();
		this.parentFolderName = batchClassChangeEvent.getBatchClassName();
		TreeStore<TreeNode> ts = view.getNavigationTree().getStore();
		ts.clear();
		controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
	}

	@EventHandler
	public void onFolderUp(FolderUpEvent folderUpEvent) {
		TreeNode parentNode = view.getNavigationTree().getParent(currentTreeNode);
		if (parentNode != null) {
			currentTreeNode = parentNode;
			controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
		} else {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(FolderManagementMessages.NO_PARENT_FOLDER), DialogIcon.ERROR);
		}
	}

	@EventHandler
	public void onFolderCut(FolderCutEvent folderCutEvent) {
		List<TreeNode> cutTreeNodesList = folderCutEvent.getSelectedTreeNodes();
		TreeStore<TreeNode> ts = view.getNavigationTree().getStore();
		for (TreeNode node : cutTreeNodesList) {
			TreeNode parentNode = view.getNavigationTree().getParent(node);
			if (null != parentNode) {
				ts.remove(node);
			}
		}

	}

	@EventHandler
	public void refreshTree(FolderTreeRefreshEvent treeRefreshEvent) {
		List<TreeNode> rootList = view.getNavigationTree().getRootItems();
		if (CollectionUtil.isEmpty(rootList)) {
			createTree();
		}
		String newFolderName = treeRefreshEvent.getNewFolderName();
		setFolderName(newFolderName);
		FolderManagerDTO selectedDTO = treeRefreshEvent.getSelectedDTO();
		if (null != selectedDTO) {
			Boolean isExpanded = view.getNavigationTree().isExpanded(currentTreeNode);
			if (!isExpanded) {
				selectedGridDTO = selectedDTO;
				view.getNavigationTree().setExpanded(currentTreeNode, true);
			} else {
				int size = view.getNavigationTree().getChildren(currentTreeNode).size();
				for (int i = 0; i < size; i++) {
					TreeNode childNode = view.getNavigationTree().getChildren(currentTreeNode).get(i);
					String childName = childNode.getDisplayName();
					if (childName != null && !childName.isEmpty() && childName.equals(selectedDTO.getFileName())) {
						currentTreeNode = childNode;
						break;
					}
				}
				view.getNavigationTree().selectNode(currentTreeNode);
			}
		} else {
			view.getNavigationTree().selectNode(currentTreeNode);
		}
	}

	private void createTree() {
		if (selectedBatchClassID == null) {
			if (getParentFolderName() != null) {
				view.createFolderSystemTree(getParentFolderName());
			}
		} else {
			view.createFolderSystemTree(selectedBatchClassID);
		}
	}

	public String findNodePath(TreeNode node, Tree navigationTree) {
		TreeNode parentNode = navigationTree.getParent(node);
		String path = null;
		if (parentNode == null) {
			path = parentFolderPath;
			if (selectedBatchClassID != null) {
				path += FolderManagementConstants.URL_SEPARATOR + selectedBatchClassID;
			}
		} else {
			path = findNodePath(parentNode, navigationTree) + FolderManagementConstants.URL_SEPARATOR + node.getDisplayName();
		}
		return path;
	}

	public void loadComboBox() {
		if (null != batchClassesNameMap && !batchClassesNameMap.isEmpty()) {
			Set<Entry<String, String>> entrySet = batchClassesNameMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				view.getComboBox().add(entry.getKey());
			}
			ListStore<String> listStore = view.getComboBox().getStore();
			String firstItem = listStore.get(0);
			view.getComboBox().setValue(firstItem, true);
			view.getComboBox().setToolTip(firstItem);
		}
	}

	public void comboboxSelection() {
		ComboBox batchClassComboBox = view.getComboBox();
		EventBus eventBus = controller.getEventBus();
		String bcName = batchClassComboBox.getValue();
		if (eventBus != null) {
			eventBus.fireEvent(new BatchClassChangeEvent(bcName, batchClassesNameMap.get(bcName)));
		}
	}

	public void fetchTreeItems(final TreeNode father, String path, final Tree navigationTree) {
		controller.getRpcService().getContents(path, new AsyncCallback<List<FolderManagerDTO>>() {

			@Override
			public void onFailure(Throwable throwable) {
				String errorMessage = throwable.getLocalizedMessage();
				if (!isErrorDialogShown) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(FolderManagementMessages.UNABLE_TO_LOAD_FOLDER_ITEMS), DialogIcon.ERROR);
					isErrorDialogShown = true;
				}
				if (null != father && errorMessage.contains(FolderManagementConstants.ERROR_TYPE_1)) {
					view.getNavigationTree().getStore().remove(father);
				}

			}

			@Override
			public void onSuccess(List<FolderManagerDTO> results) {
				isErrorDialogShown = false;
				view.openTreeNode(father, results, navigationTree);
			}
		});
	}

	public void setCurrentTreeNode(TreeNode selectedNode) {
		this.currentTreeNode = selectedNode;
	}

	public TreeNode getCurrentTreeNode() {
		return currentTreeNode;
	}

	public String getParentFolderPath() {
		return parentFolderPath;
	}

	public void setParentFolderPath(String parentFolderPath) {
		this.parentFolderPath = parentFolderPath;
	}

	public String getParentFolderName() {
		return parentFolderName;
	}

	public void setParentFolderName(String parentFolderName) {
		this.parentFolderName = parentFolderName;
	}

	public String getSelectedBatchClassID() {
		return selectedBatchClassID;
	}

	public void setSelectedBatchClassID(String selectedBatchClassID) {
		this.selectedBatchClassID = selectedBatchClassID;
	}

	public void setFooterPath(String path) {
		if (path != null) {
			controller.getEventBus().fireEvent(new CurrentPathRefreshEvent(path));
			String pathString = LocaleDictionary.getMessageValue(FolderManagementMessages.CURRENT_PATH) + getRelativePath(path);
			footer.setText(pathString);
			footer.setTitle(pathString);
		}
	}

	public String getRelativePath(String path) {
		String relativePath = null;
		if (selectedBatchClassID != null) {
			relativePath = FolderManagementConstants.DOT + path.substring(parentFolderPath.length());
		} else {
			relativePath = FolderManagementConstants.DOT + path.substring(parentFolderPath.length() - parentFolderName.length() - 1);
		}
		return relativePath;
	}

	public void refreshGrid(String path, TreeNode selectedNode) {
		EventBus eventBus = controller.getEventBus();
		eventBus.fireEvent(new FolderGridRefreshEvent(path, selectedNode));
	}

	public void fireExpandEvent(TreeNode treeNode) {
		controller.getEventBus().fireEvent(new TreeExpandEvent(treeNode));
	}

	public void selectTreeNode(List<TreeNode> list) {
		if (null != selectedGridDTO && null != list) {
			boolean found = false;
			for (TreeNode node : list) {
				if (node.getDisplayName().equals(selectedGridDTO.getFileName())) {
					currentTreeNode = node;
					found = true;
					break;
				}
			}
			if (found) {
				selectedGridDTO = null;
				view.getNavigationTree().selectNode(currentTreeNode);
			}
		}
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
}
