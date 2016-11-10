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

package com.ephesoft.gxt.admin.client.presenter.navigator;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.BCMTreeMaskEvent;
import com.ephesoft.gxt.admin.client.event.BatchClassLoadEvent;
import com.ephesoft.gxt.admin.client.event.CompositePresenterSelectionEvent;
import com.ephesoft.gxt.admin.client.event.NavigationTreeRefreshEvent;
import com.ephesoft.gxt.admin.client.event.RemoveTreeViewEvent;
import com.ephesoft.gxt.admin.client.event.NavigationEvent.NavigationNodeAdditionEvent;
import com.ephesoft.gxt.admin.client.event.NavigationEvent.NavigationNodeDeletionEvent;
import com.ephesoft.gxt.admin.client.event.NavigationEvent.NavigationNodeRenameEvent;
import com.ephesoft.gxt.admin.client.event.SubTreeAdditionEvent.DocumentSubTreeAdditionEvent;
import com.ephesoft.gxt.admin.client.event.SubTreeAdditionEvent.IndexFieldSubTreeAdditionEvent;
import com.ephesoft.gxt.admin.client.event.SubTreeRefreshEvent.ModuleSubTreeRefreshEvent;
import com.ephesoft.gxt.admin.client.event.SubTreeRefreshEvent.PluginSubTreeRefreshEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter;
import com.ephesoft.gxt.admin.client.util.NavigationNodeFactory;
import com.ephesoft.gxt.admin.client.util.NavigationNodeFactory.NavigationNode;
import com.ephesoft.gxt.admin.client.view.BatchClassCompositeView;
import com.ephesoft.gxt.admin.client.view.navigator.BatchClassNavigatorView;
import com.ephesoft.gxt.admin.client.view.navigator.draw.BatchClassNavigationCreator;
import com.ephesoft.gxt.admin.client.widget.property.BatchClassNavigationNode;
import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassModuleDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class BatchClassNavigatorPresenter extends BasePresenter<BatchClassManagementController, BatchClassNavigatorView> {

	interface CustomEventBinder extends EventBinder<BatchClassNavigatorPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public BatchClassNavigatorPresenter(BatchClassManagementController controller, BatchClassNavigatorView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	@EventHandler
	public void handleBatchClassLoadEvent(BatchClassLoadEvent loadEvent) {
		if (null != loadEvent) {
			BatchClassDTO loadedBatchClass = loadEvent.getLoadedBatchClass();
			if (null != loadedBatchClass) {
				BatchClassNavigationCreator.createNavigator(loadedBatchClass, controller);
				view.selectPreNavigationLoadPresenter();
				ScreenMaskUtility.unmaskScreen();
			}
		}
	}

	@EventHandler
	public void maskTree(BCMTreeMaskEvent maskEvent) {
		view.maskBCMTree(maskEvent.isTreeMasked());
	}

	@SuppressWarnings( {"rawtypes", "unchecked"})
	@EventHandler
	public void onCompositeViewSelectionEvent(final CompositePresenterSelectionEvent compositePresenterSelectionEvent) {
		if (null != compositePresenterSelectionEvent) {
			final BatchClassCompositePresenter compositePresenter = compositePresenterSelectionEvent.getCompositePresenter();
			final BatchClassCompositePresenter currentPresenter = controller.getCurrentBindedPresenter();
			boolean isValid = true;

			// Modified Code To Open elements on the basis of Check Box selected, in case of Open Button Clicked.
			if (!compositePresenterSelectionEvent.isOnCtrlKey() && compositePresenterSelectionEvent.isOnOpenButton()) {
				BatchClassCompositeView compositeView = (BatchClassCompositeView) currentPresenter.getView();
				if (null != compositeView && compositeView.getListPanelView() instanceof HasResizableGrid) {
					Grid grid = ((HasResizableGrid) compositeView.getListPanelView()).getGrid();
					if (null != grid) {
						List selectedModels = grid.getSelectedModels();
						if (!CollectionUtil.isEmpty(selectedModels)) {
							if (selectedModels.size() > 1) {
								ScreenMaskUtility.unmaskScreen();
								isValid = false;
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.ONLY_ONE_RECORD_CAN_BE_OPENED_AT_TIME),
										DialogIcon.WARNING);
							} else {
								controller.setCurrentSelectedObject(selectedModels.get(0));
							}
						}
					}
				}
			}

			if (isValid) {
				if (null != compositePresenter && (currentPresenter == null || currentPresenter.isValid())) {
					view.bindChildView(compositePresenter, controller.getCurrentSelectedObject());
				}
			}
		}
	}

	@EventHandler
	public void handleNavigationTreeRefreshEvent(NavigationTreeRefreshEvent refreshEvent) {
		if (null != refreshEvent) {
			BatchClassDTO loadedBatchClass = refreshEvent.getLoadedBatchClass();
			BatchClassDTO newBatchClassDTO = refreshEvent.getNewBatchClass();
			if (null != loadedBatchClass && null != newBatchClassDTO) {
				view.refreshTree(refreshEvent.getLoadedBatchClass(), refreshEvent.getNewBatchClass());
				// BatchClassNavigationCreator.createNavigator(loadedBatchClass,
				// controller);
			}
		}
		ScreenMaskUtility.unmaskScreen();
		Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS), LocaleDictionary
				.getMessageValue(BatchClassMessages.BATCH_CLASS_UPDATED_SUCCESSFULLY));
	}

	@EventHandler
	public void handleNodeRenameEvent(final NavigationNodeRenameEvent nodeRenameEvent) {
		if (null != nodeRenameEvent) {
			BatchClassNavigationNode<?> currentNode = view.getCurrentNode();
			if (null != currentNode) {
				String nameToAssign = nodeRenameEvent.getNameToAssign();
				Object bindedChildObject = nodeRenameEvent.getBindedObject();
				BatchClassNavigationNode<?> nodeToRename = view.getChild(currentNode, bindedChildObject);
				view.rename(nodeToRename, nameToAssign);
			}
		}
	}

	@EventHandler
	public void handleNavigationNodeAdditionEvent(final NavigationNodeAdditionEvent additionEvent) {
		if (null != additionEvent) {
			final Object bindedObject = additionEvent.getBindedObject();
			final BatchClassNavigationNode<?> currentNode = view.getCurrentNode();
			if (null != currentNode) {
				final Object parentObject = currentNode.getBindedObject();
				final NavigationNode nodeHierarchy = NavigationNodeFactory.getNode(bindedObject, controller, parentObject);
				if (null != nodeHierarchy) {
					view.addNode(currentNode, nodeHierarchy, additionEvent.getIndex());
				}
			}
		}
	}

	@EventHandler
	public void handleNodeDeletionEvent(NavigationNodeDeletionEvent nodeDeletionEvent) {
		if (null != nodeDeletionEvent) {
			Object bindedObject = nodeDeletionEvent.getBindedObject();
			BatchClassNavigationNode<?> nodeToRemove = view.getNode(view.getCurrentNode(), bindedObject);
			if (null != nodeToRemove) {
				view.removeNode(nodeToRemove);
			}
		}
	}

	public void acquireLock(final BatchClassCompositePresenter<?, ?> compositePresenter) {
		final BatchClassDTO selectedBatchClass = controller.getSelectedBatchClass();
		if (null != selectedBatchClass) {
			controller.getRpcService().acquireLock(selectedBatchClass.getIdentifier(), new AsyncCallback<Void>() {

				@Override
				public void onSuccess(Void result) {
					view.setNavigationTreeVisible(true);
					BatchClassCompositePresenter<?, ?> preLoadPresenter = view.getPreLoadPresenter();
					if (null != compositePresenter && preLoadPresenter == null) {
						view.setPreLoadPresenter(compositePresenter);
						compositePresenter.layout(null);
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					String errorMsg = StringUtil.concatenate(LocaleDictionary.getConstantValue(BatchClassConstants.BATCH_CLASS), " ", selectedBatchClass.getIdentifier(), " ",
							LocaleDictionary.getMessageValue(BatchClassMessages.LOCKED_BY_SOMEONE));
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE), errorMsg,
							DialogIcon.ERROR);
				}
			});

		}
	}

	@EventHandler
	public void handleDocumentTypeSubTreeAdditionEvent(final DocumentSubTreeAdditionEvent documentSubTreeAdditionEvent) {
		if (null != documentSubTreeAdditionEvent) {
			DocumentTypeDTO document = documentSubTreeAdditionEvent.getDocumentType();
			if (null != document) {
				List<BatchClassNavigationNode<?>> nodeList = new ArrayList<BatchClassNavigationNode<?>>();
				BatchClassNavigationNode<?> documentTypeNode = view.getChild(view.getCurrentNode(), document);
				nodeList.add(documentTypeNode);
				if (null != documentTypeNode) {
					// Navigation create requires node to have no child, it will add the child.
					view.removeChildren(documentTypeNode);
					BatchClassNavigationCreator.addDocumentLevelProperties(nodeList);
				}
			}
		}
	}
	@EventHandler
	public void handleIndexFieldSubTreeAdditionEvent(final IndexFieldSubTreeAdditionEvent indexFieldSubTreeAdditionEvent) {
		if (null != indexFieldSubTreeAdditionEvent) {
			FieldTypeDTO indexField = indexFieldSubTreeAdditionEvent.getIndexField();
			if (null != indexField) {
				List<BatchClassNavigationNode<?>> nodeList = new ArrayList<BatchClassNavigationNode<?>>();
				BatchClassNavigationNode<?> indexFieldNode = view.getChild(view.getCurrentNode(), indexField);
				nodeList.add(indexFieldNode);
				if (null != indexFieldNode) {
					// Navigation create requires node to have no child, it will add the child.
					view.removeChildren(indexFieldNode);
					BatchClassNavigationCreator.addIndexFieldLevelProperties(nodeList);
				}
			}
		}
	}


	public BatchClassCompositePresenter<?, ?> getCurrentPresenter() {
		return controller.getCurrentBindedPresenter();
	}

	/**
	 * Handle module sub tree refresh event.
	 * 
	 * @param moduleSubTreeRefreshEvent the module sub tree refresh event
	 */
	@EventHandler
	public void handleModuleSubTreeRefreshEvent(final ModuleSubTreeRefreshEvent moduleSubTreeRefreshEvent) {
		if (null != moduleSubTreeRefreshEvent) {
			BatchClassDTO batchClass = moduleSubTreeRefreshEvent.getBatchClass();
			if (null != batchClass) {
				BatchClassNavigationCreator.refreshModuleTree(batchClass);
			}
		}
	}

	/**
	 * Handle plugin sub tree refresh event.
	 * 
	 * @param pluginSubTreeRefreshEvent the plugin sub tree refresh event
	 */
	@EventHandler
	public void handlePluginSubTreeRefreshEvent(final PluginSubTreeRefreshEvent pluginSubTreeRefreshEvent) {
		if (null != pluginSubTreeRefreshEvent) {
			BatchClassModuleDTO batchClassModule = pluginSubTreeRefreshEvent.getBatchClassModule();
			if (null != batchClassModule) {
				BatchClassNavigationCreator.refreshPluginTree(batchClassModule);
			}
		}
	}

	@EventHandler
	public void handleRemoveTreeEvent(final RemoveTreeViewEvent moduleSubTreeRefreshEvent) {
		if (null != moduleSubTreeRefreshEvent) {
			view.setNavigationTreeVisible(false);
			view.setPreLoadPresenter(null);
		}
	}
}
