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

package com.ephesoft.gxt.systemconfig.client.presenter.navigator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.property.Node;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.AddRegexGroupInTreeEvent;
import com.ephesoft.gxt.systemconfig.client.event.EditRegexGroupInTreeEvent;
import com.ephesoft.gxt.systemconfig.client.event.ImportRegexGroupEvent;
import com.ephesoft.gxt.systemconfig.client.event.RegexGroupSelectionEvent;
import com.ephesoft.gxt.systemconfig.client.event.ReloadPluginsTreeViewEvent;
import com.ephesoft.gxt.systemconfig.client.event.StartMultipleRegexGroupDeletionInTreeEvent;
import com.ephesoft.gxt.systemconfig.client.event.TreeCreationEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigCompositePresenter;
import com.ephesoft.gxt.systemconfig.client.presenter.workflowmanagement.WorkflowManagmentPresenter;
import com.ephesoft.gxt.systemconfig.client.view.navigator.SystemConfigNavigatorView;
import com.ephesoft.gxt.systemconfig.client.widget.SystemConfigTreeNodesFactory;
import com.ephesoft.gxt.systemconfig.client.widget.property.PluginDTOComparator;
import com.ephesoft.gxt.systemconfig.client.widget.property.RegexGroupDTOComparator;
import com.ephesoft.gxt.systemconfig.client.widget.property.SystemConfigModules;
import com.ephesoft.gxt.systemconfig.client.widget.property.SystemConfigNavigationNode;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class SystemConfigNavigatorPresenter extends BasePresenter<SystemConfigController, SystemConfigNavigatorView> {

	private List<RegexGroupDTO> regexGroupList;

	public SystemConfigNavigatorPresenter(SystemConfigController controller, SystemConfigNavigatorView view) {
		super(controller, view);
		// setPluginListInTree();
	}

	interface CustomEventBinder extends EventBinder<SystemConfigNavigatorPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void createTree(TreeCreationEvent treeCreationEvent) {

		for (SystemConfigModules module : SystemConfigModules.values()) {
			SystemConfigCompositePresenter<?, String> compositePresenter = getCompositePresenter(module);
			
			// Encryption check for unix/linux.
			if (module.getModuleParameter().equals(SystemConfigConstants.GENERATION_KEY) && EphesoftUIContext.isUnix()) {
				continue;
			}
			if (module.getParentModule() == null) {
				view.addNewTreeRoot(module.getModuleParameter(), compositePresenter);
			} else {
				SystemConfigNavigationNode parentNode = view.getParentNodeFromTree(module.getParentModule().getModuleParameter());
				String displayParameter = LocaleDictionary.getConstantValue(module.getModuleParameter());
				SystemConfigNavigationNode<String> childNode = new SystemConfigNavigationNode<String>(displayParameter,
						compositePresenter, module.getModuleParameter());
				if (null != parentNode) {
					view.addChildrenInTree(parentNode, childNode);
				}
			}

		}
		getRegexGroupsList();
		getAllPluginList();
		selectFirstChildOfTree();
	}

	private void selectFirstChildOfTree() {
		view.selectFirstChildOfTree();
	}

	public void getAllPluginList() {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().getAllPluginDetailDTOs(new AsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void onSuccess(List<PluginDetailsDTO> pluginList) {
				ScreenMaskUtility.unmaskScreen();
				if (CollectionUtil.isEmpty(pluginList)) {
					controller.setAllPluginsList(new ArrayList<PluginDetailsDTO>());
				} else {
					Collections.sort(pluginList, new PluginDTOComparator());
					controller.setAllPluginsList(pluginList);
					populatePluginsInTree();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_GET_ALL_PLUGINS_LIST), DialogIcon.ERROR);
			}
		});
	}

	/**
	 * Populates all the plugins under Workflow Managment node.
	 */
	private void populatePluginsInTree() {
		List<PluginDetailsDTO> allPluginList = controller.getAllPluginsList();
		SystemConfigNavigationNode workflowManagmentNode = view.getParentNodeFromTree(SystemConfigModules.WORKFLOW_MANAGEMENT
				.getModuleParameter());
		if (!CollectionUtil.isEmpty(allPluginList)) {
			if (null != workflowManagmentNode) {
				view.removeAllChildernFromTree(workflowManagmentNode);
				for (PluginDetailsDTO plugin : allPluginList) {
					WorkflowManagmentPresenter workflowManagmentPresenter = controller.getWorkflowManagmentPresenter();
					SystemConfigNavigationNode<PluginDetailsDTO> pluginNode = new SystemConfigNavigationNode<PluginDetailsDTO>(
							plugin.getPluginName(), workflowManagmentPresenter, plugin);
					view.addChildrenInTree(workflowManagmentNode, pluginNode);
				}
			}
		}
	}

	@EventHandler
	public void reloadPluginListInTree(final ReloadPluginsTreeViewEvent reloadPluginTreeEvent) {
		getAllPluginList();
	}

	public SystemConfigCompositePresenter<?, String> getCompositePresenter(final SystemConfigModules module) {
		return SystemConfigTreeNodesFactory.getModuleSpecificPresenter(module, controller);
	}

	public void createRegexGroupList(Collection<RegexGroupDTO> regexGroupList, String newRegexGroupName) {

		for (RegexGroupDTO regexGroup : regexGroupList) {

			createRegexGroupNode(regexGroup);
		}
	}

	public void getRegexGroupsList() {
		controller.getRpcService().getRegexGroupMap(new AsyncCallback<Map<String, RegexGroupDTO>>() {

			@Override
			public void onSuccess(final Map<String, RegexGroupDTO> regexGroupListMap) {
				if (regexGroupListMap == null) {
					DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.UNABLE_TO_READ_REGEX_GROUPS), false,
							DialogIcon.ERROR);

				} else {
					if (regexGroupListMap.isEmpty()) {
						DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
								LocaleDictionary.getMessageValue(SystemConfigMessages.NO_REGEX_GROUPS), false, DialogIcon.ERROR);

					} else {
						Collection<RegexGroupDTO> regexGroupList = getRegexGroups(regexGroupListMap);
						final Map<String, RegexGroupDTO> regexGroupDTOMap = new HashMap<String, RegexGroupDTO>(regexGroupList.size());
						for (RegexGroupDTO groupDTO : regexGroupList) {
							if (null != groupDTO) {
								regexGroupDTOMap.put(groupDTO.getIdentifier(), groupDTO);
							}
						}

						controller.setRegexGroupList(regexGroupList);
						setRegexGroupList((List<RegexGroupDTO>) regexGroupList);
						Collections.sort(getRegexGroupList(), new RegexGroupDTOComparator());
						createRegexGroupList(getRegexGroupList(), null);

					}
				}
			}

			@Override
			public void onFailure(final Throwable caught) {
			}
		});

	}

	/**
	 * getRegexGroups method use for getting the list of Regex group list from regex group map
	 * 
	 * @param regexGroupMap {@link Map<String, RegexGroupDTO>}
	 * @return list of regex groups {@link Collection<RegexGroupDTO>}
	 */
	public Collection<RegexGroupDTO> getRegexGroups(final Map<String, RegexGroupDTO> regexGroupMap) {
		Collection<RegexGroupDTO> regexGroupDTOList = null;
		if (null != regexGroupMap) {
			regexGroupDTOList = new ArrayList<RegexGroupDTO>(regexGroupMap.size());
			for (RegexGroupDTO regexGroupDTO : regexGroupMap.values()) {
				if (null != regexGroupDTO && !regexGroupDTO.isDeleted()) {
					regexGroupDTOList.add(regexGroupDTO);
				}
			}
		}
		return regexGroupDTOList;
	}

	@EventHandler
	public void regexGrouproupSelectionEvent(RegexGroupSelectionEvent regexGroupSelectionEvent) {
		RegexGroupDTO regexGroupDTO = regexGroupSelectionEvent.getRegexGroupDTO();

		Node currentlySelectedNode = view.getNavigationTree().getCurrentNode();

		Collection<SystemConfigNavigationNode> currentlySelectedNodeChilds = view.getNavigationTree().getChildren(
				currentlySelectedNode);

		for (SystemConfigNavigationNode regexGroupNode : currentlySelectedNodeChilds) {

			String temp = ((RegexGroupDTO) regexGroupNode.getBindedObject()).getIdentifier();
			if (temp.equals(regexGroupDTO.getIdentifier())) {

				view.getNavigationTree().selectNode(regexGroupNode);
				break;
			}
		}
	}

	@EventHandler
	public void handleMultipleRegexGroupDeletionStartEvent(StartMultipleRegexGroupDeletionInTreeEvent multipleDeletionStartEvent) {

		List<RegexGroupDTO> selectedRegexGroupsList = multipleDeletionStartEvent.getSelectedRegexGroups();

		Node currentlySelectedNode = view.getNavigationTree().getCurrentNode();
		Collection<SystemConfigNavigationNode> currentlySelectedNodeChilds = view.getNavigationTree().getChildren(
				currentlySelectedNode);

		for (RegexGroupDTO regexGroupDTO : selectedRegexGroupsList) {
			for (SystemConfigNavigationNode regexGroupNode : currentlySelectedNodeChilds) {

				String temp = ((RegexGroupDTO) regexGroupNode.getBindedObject()).getIdentifier();
				if (temp.equals(regexGroupDTO.getIdentifier())) {

					view.getNavigationTree().removeTreeNodeInMap(regexGroupNode);

					// view.getNavigationTree().removeChildren(regexGroupNode);
					view.getNavigationTree().remove(regexGroupNode);
					getRegexGroupList().remove(regexGroupDTO);
					break;
				}
			}
		}
	}

	@EventHandler
	public void handleRegexGroupAdditionEvent(AddRegexGroupInTreeEvent addEvent) {
		createRegexGroupNode(addEvent.getRegexGroupDTO());
	}

	public SystemConfigNavigationNode createRegexGroupNode(RegexGroupDTO regexGroup) {

		SystemConfigCompositePresenter<?, RegexGroupDTO> compositePresenter = controller.getRegexPatternViewPresenter();
		SystemConfigNavigationNode parentNode = view.getParentNodeFromTree(SystemConfigModules.REGEX_GROUP.getModuleParameter());

		SystemConfigNavigationNode<RegexGroupDTO> childNode = new SystemConfigNavigationNode<RegexGroupDTO>(regexGroup.getName(),
				null, regexGroup);

		if (null != parentNode) {
			view.addChildrenInTree(parentNode, childNode);
		}

		parentNode = childNode;
		childNode = new SystemConfigNavigationNode<RegexGroupDTO>(SystemConfigConstants.REGEX_PATTERN, compositePresenter, regexGroup);
		if (null != parentNode) {
			view.addChildrenInTree(parentNode, childNode);
		}

		return parentNode;
	}

	@EventHandler
	public void editRegexGroupEvent(EditRegexGroupInTreeEvent editEvent) {
		if (null != editEvent) {
			int indexToPlaceNewRegex = 0;

			Node currentlySelectedNode = view.getNavigationTree().getCurrentNode();
			Collection<SystemConfigNavigationNode> currentlySelectedNodeChilds = view.getNavigationTree().getChildren(
					currentlySelectedNode);

			SystemConfigNavigationNode editedNode = null;
			List<? extends com.sencha.gxt.data.shared.TreeStore.TreeNode<SystemConfigNavigationNode<?>>> childSubtree = null;

			for (SystemConfigNavigationNode regexGroupNode : currentlySelectedNodeChilds) {
				String temp = ((RegexGroupDTO) regexGroupNode.getBindedObject()).getIdentifier();
				if (temp.equals(editEvent.getRegexGroupDTO().getIdentifier())) {
					editedNode = regexGroupNode;
					com.sencha.gxt.data.shared.TreeStore.TreeNode<SystemConfigNavigationNode<?>> node = view.getNavigationTree()
							.getStore().getSubTree(regexGroupNode);

					if (null != node) {
						childSubtree = node.getChildren();

					}

					view.getNavigationTree().remove(regexGroupNode);

					break;
				}
			}

			RegexGroupDTOComparator comparator = new RegexGroupDTOComparator();
			for (RegexGroupDTO group : getRegexGroupList()) {

				if (comparator.compare(editEvent.getRegexGroupDTO(), group) < 0) {
					break;
				} else {
					indexToPlaceNewRegex++;
				}
			}
			editedNode.setDisplayName(editEvent.getRegexGroupDTO().getName());
			view.insertNodeAtSpecifiedIndex(currentlySelectedNode, indexToPlaceNewRegex, editedNode);
			view.getNavigationTree().getStore().addSubTree(editedNode, 0, (List) childSubtree);

		}
	}

	public List<RegexGroupDTO> getRegexGroupList() {
		return regexGroupList;
	}

	public void setRegexGroupList(List<RegexGroupDTO> regexGroupList) {
		this.regexGroupList = regexGroupList;
	}

	@EventHandler
	public void handleImportRegexGroupevent(ImportRegexGroupEvent importEvent) {
		if (null != importEvent) {
			boolean groupExist = false;
			int indexToPlaceNewRegex = 0;
			Node currentlySelectedNode = view.getNavigationTree().getCurrentNode();
			Collection<SystemConfigNavigationNode> currentlySelectedNodeChilds = view.getNavigationTree().getChildren(
					currentlySelectedNode);

			for (RegexGroupDTO importedRegexGroup : importEvent.getSelectedRegexGroups()) {
				groupExist = false;
				indexToPlaceNewRegex = 0;
				currentlySelectedNodeChilds = view.getNavigationTree().getChildren(currentlySelectedNode);
				for (SystemConfigNavigationNode regexGroupNode : currentlySelectedNodeChilds) {
					if (importedRegexGroup.getIdentifier().equals(((RegexGroupDTO) regexGroupNode.getBindedObject()).getIdentifier())) {
						groupExist = true;
						Collection<SystemConfigNavigationNode> regexGroupChilds = view.getNavigationTree().getChildren(regexGroupNode);
						for (SystemConfigNavigationNode regexGroupchild : regexGroupChilds) {
							if (regexGroupchild.getDisplayName().equals(SystemConfigConstants.REGEX_PATTERN)) {
								regexGroupchild.setBindedObject(importedRegexGroup);
							}
						}

						break;

					}
				}
				if (!groupExist) {
					RegexGroupDTOComparator comparator = new RegexGroupDTOComparator();
					// for (RegexGroupDTO group : getRegexGroupList()) {
					for (SystemConfigNavigationNode regexGroupNode : currentlySelectedNodeChilds) {
						RegexGroupDTO group = ((RegexGroupDTO) regexGroupNode.getBindedObject());
						if (comparator.compare(importedRegexGroup, group) < 0) {
							break;
						} else {
							indexToPlaceNewRegex++;
							if (currentlySelectedNodeChilds.size() == indexToPlaceNewRegex) {
								// indexToPlaceNewRegex--;
								break;
							}
						}
					}
					getRegexGroupList().add(importedRegexGroup);
					SystemConfigNavigationNode<RegexGroupDTO> parentNode = null;
					SystemConfigNavigationNode<RegexGroupDTO> childNode = null;
					SystemConfigCompositePresenter<?, RegexGroupDTO> compositePresenter = controller.getRegexPatternViewPresenter();

					childNode = new SystemConfigNavigationNode<RegexGroupDTO>(importedRegexGroup.getName(), null, importedRegexGroup);
					view.insertNodeAtSpecifiedIndex(currentlySelectedNode, indexToPlaceNewRegex, childNode);
					view.getNavigationTree().addTreeNodeInMap(childNode.getBindedObject(), childNode);
					parentNode = childNode;
					childNode = new SystemConfigNavigationNode<RegexGroupDTO>(SystemConfigConstants.REGEX_PATTERN, compositePresenter,
							importedRegexGroup);
					view.insertNodeAtSpecifiedIndex(parentNode, 0, childNode);
					view.getNavigationTree().addTreeNodeInMap(childNode.getBindedObject(), childNode);

				}
			}

		}
	}
}
