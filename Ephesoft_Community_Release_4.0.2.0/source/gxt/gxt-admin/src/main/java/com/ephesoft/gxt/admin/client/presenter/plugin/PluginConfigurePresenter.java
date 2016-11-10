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

package com.ephesoft.gxt.admin.client.presenter.plugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.gxt.admin.client.DefaultPluginModuleMap;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.SubTreeRefreshEvent.PluginSubTreeRefreshEvent;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.plugin.PluginConfigureView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.listener.DualListButtonListener;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassModuleDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginDTO;
import com.ephesoft.gxt.core.shared.dto.DependencyDTO;
import com.ephesoft.gxt.core.shared.dto.ModuleDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.widget.core.client.ListView;

// TODO: Auto-generated Javadoc
/**
 * The Class PluginConfigurePresenter.
 */
public class PluginConfigurePresenter extends BatchClassInlinePresenter<PluginConfigureView> {

	/** The batch class module dto. */
	BatchClassModuleDTO batchClassModuleDTO;

	/** The selected plugin dependencies index set. */
	private Set<Integer> selectedPluginDependenciesIndexSet;

	/** The all plugin names. */
	private List<String> allPluginNames;

	/** The plugin name to dto map. */
	private Map<String, BatchClassPluginDTO> pluginNameToDtoMap;

	/** The is dragged from right. */
	private boolean isDraggedFromRight = false;
	/**
	 * Refers to all the indexes of dependencies to be added in different modules after the plugins of current module are configured.
	 */
	private Set<Integer> allDependenciesSet;

	/**
	 * Instantiates a new plugin configure presenter.
	 * 
	 * @param controller the controller
	 * @param view the view
	 */
	public PluginConfigurePresenter(BatchClassManagementController controller, PluginConfigureView view) {
		super(controller, view);
		pluginNameToDtoMap = new LinkedHashMap<String, BatchClassPluginDTO>();
		addDropHandler();
		addButtonListener();
	}

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<PluginConfigurePresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.event.BindHandler#bind()
	 */
	@Override
	public void bind() {
		allDependenciesSet = new TreeSet<Integer>();
		allPluginNames = new LinkedList<String>();
		removeHighlightedDependencies(view.getPluginList().getFromView(), selectedPluginDependenciesIndexSet);
		selectedPluginDependenciesIndexSet = new HashSet<Integer>(0);
		this.batchClassModuleDTO = controller.getSelectedBatchClassModule();
		if (null != pluginNameToDtoMap && pluginNameToDtoMap.size() == 0) {
			getPluginDTOs();
		} else {
			view.setSelectedPluginDTO(batchClassModuleDTO.getBatchClassPlugins());
		}
		view.fireSelectionEvent();
		applySortInfo();
	}

	/**
	 * Adds the button listener.
	 */
	private void addButtonListener() {
		DualListButtonListener<BatchClassPluginDTO> buttonListener = new DualListButtonListener<BatchClassPluginDTO>() {

			@Override
			public void onLeftClick(List<BatchClassPluginDTO> pluginDTOs) {
				onLeft(pluginDTOs);
			}

			@Override
			public void onRightClick(List<BatchClassPluginDTO> pluginDTOs) {
				onRight(pluginDTOs);
			}

			@Override
			public void onUpClick() {
				orderAllPlugins();
			}

			@Override
			public void onDownClick() {
				orderAllPlugins();
			}
		};
		view.getPluginList().setButtonListener(buttonListener);
	}

	/**
	 * On left.
	 * 
	 * @param pluginDTOs the plugin dt os
	 */
	private void onLeft(List<BatchClassPluginDTO> pluginDTOs) {
		if (null != pluginDTOs && pluginDTOs.size() > 0) {
			for (BatchClassPluginDTO pluginDTO : pluginDTOs) {
				if (pluginDTO.isNew()) {
					batchClassModuleDTO.removeBatchClassPlugin(pluginDTO);
				} else {
					pluginDTO.setDeleted(true);
					batchClassModuleDTO.addBatchClassPlugin(pluginDTO);
				}
			}
			orderAllPlugins();
		}
	}

	/**
	 * On right.
	 * 
	 * @param pluginDTOs the plugin dt os
	 */
	private void onRight(List<BatchClassPluginDTO> pluginDTOs) {
		if (null != pluginDTOs && pluginDTOs.size() > 0) {
			view.getPluginList().getToStore().clearSortInfo();
			Set<Integer> dependenciesIndexList = getSelectedPluginDependenciesIndexSet();
			if (dependenciesIndexList != null) {
				List<String> workFlowNames = new ArrayList<String>();
				for (BatchClassPluginDTO pluginDTO : pluginDTOs) {
					String workFlowName = getModuleNameForPlugin(batchClassModuleDTO.getBatchClass(), pluginDTOs.get(0).getPlugin()
							.getPluginName());
					workFlowName = (workFlowName == null) ? searchSelectedPluginNameInRightHandedSelectBox(pluginDTOs.get(0)
							.getPlugin().getPluginName()) : workFlowName;
					if (workFlowName != null) {
						workFlowNames.add(workFlowName);
					}
				}
				if (workFlowNames.size() != 0) {
					confirmAldreadyExistingPluginAddition(workFlowNames, pluginDTOs);
				} else {
					confirmPluginAddition(pluginDTOs);
				}
			} else {
				addPlugin(pluginDTOs);
			}
		}
	}

	/**
	 * Adds the drop handler.
	 */
	private void addDropHandler() {
		view.getPluginList().getDragSourceFromField().addDropHandler(new DndDropHandler() {

			@Override
			public void onDrop(DndDropEvent event) {
				isDraggedFromRight = false;
				view.getPluginList().getToStore().clearSortInfo();
			}
		});

		view.getPluginList().getDragSourceToField().addDropHandler(new DndDropHandler() {

			@Override
			public void onDrop(DndDropEvent event) {
				if (event.getDropTarget() == view.getPluginList().getDropTargetFromField()) {
					List<BatchClassPluginDTO> pluginDTOs = (List<BatchClassPluginDTO>) event.getData();
					onLeft(pluginDTOs);
					applySortInfo();
				} else if (event.getDropTarget() == view.getPluginList().getDropTargetToField()) {
					view.getPluginList().getToStore().clearSortInfo();
					isDraggedFromRight = true;
				}
			}
		});
		view.getPluginList().getDropTargetToField().addDropHandler(new DndDropHandler() {

			@Override
			public void onDrop(DndDropEvent event) {

				if (!isDraggedFromRight) {
					List<BatchClassPluginDTO> pluginDTOs = (List<BatchClassPluginDTO>) event.getData();
					onRight(pluginDTOs);
				} else {
					orderAllPlugins();
					applySortInfo();
				}
			}
		});

	}

	/**
	 * Apply sort info.
	 */
	private void applySortInfo() {
		view.getPluginList().getToStore().addSortInfo(new StoreSortInfo<BatchClassPluginDTO>(getPluginComparator(), SortDir.ASC));
	}

	/**
	 * Confirms for addition of Plugin that already exist in a module for the batchClass.
	 * 
	 * @param moduleNames the module names
	 * @param pluginDTOs the plugin dt os
	 */
	private void confirmAldreadyExistingPluginAddition(final List<String> moduleNames, final List<BatchClassPluginDTO> pluginDTOs) {
		String messageToDisplayForConfirmation;
		if (moduleNames.size() == 1) {
			messageToDisplayForConfirmation = StringUtil.concatenate(
					LocaleDictionary.getMessageValue(BatchClassMessages.PLUGIN_ALREADY_EXIST), moduleNames.get(0),
					LocaleDictionary.getMessageValue(BatchClassMessages.ADD_ALREADY_EXIST_MODULE));
		} else {
			messageToDisplayForConfirmation = StringUtil.concatenate(LocaleDictionary
					.getMessageValue(BatchClassMessages.PLUGINS_ALREADY_EXIST));
		}
		ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
				LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION), messageToDisplayForConfirmation, false);
		confirmationDialog.setDialogListener(new DialogAdapter() {

			@Override
			public void onOkClick() {
				confirmPluginAddition(pluginDTOs);
			}

			@Override
			public void onCancelClick() {
				for (BatchClassPluginDTO pluginDTO : pluginDTOs)
					view.getPluginList().getToStore().remove(pluginDTO);
			}

			@Override
			public void onCloseClick() {
				for (BatchClassPluginDTO pluginDTO : pluginDTOs)
					view.getPluginList().getToStore().remove(pluginDTO);
			}
		});
	}

	/**
	 * Confirm plugin addition.
	 * 
	 * @param pluginDTOs the plugin dt os
	 */
	private void confirmPluginAddition(final List<BatchClassPluginDTO> pluginDTOs) {
		if (selectedPluginDependenciesIndexSet.size() > 0) {
			final ConfirmationDialog confirmationDialog = new ConfirmationDialog(
					LocaleDictionary.getMessageValue(BatchClassMessages.PLUGIN_ADDITION),
					LocaleDictionary.getMessageValue(BatchClassMessages.HIGHLIGHT_DEPENDENCY_ADD_MESSAGE), true);
			// confirmationDialog.center();
			confirmationDialog.setOkButtonText(LocaleDictionary.getConstantValue(BatchClassConstants.YES));
			confirmationDialog.setCancelButtonText(LocaleDictionary.getConstantValue(BatchClassConstants.NO));
			confirmationDialog.setCloseButtonText(LocaleDictionary.getConstantValue(BatchClassConstants.CANCEL));
			confirmationDialog.setDialogListener(new DialogAdapter() {

				@Override
				public void onOkClick() {
					addPlugin(pluginDTOs);
					moveDependenciesAlongWithplugin();

					removeHighlightedDependencies(view.getPluginList().getFromView(), selectedPluginDependenciesIndexSet);
					addDependancyInCorrespondingModule();

				}

				@Override
				public void onCancelClick() {
					confirmationDialog.hide();
					addPlugin(pluginDTOs);
				}

				@Override
				public void onCloseClick() {
					confirmationDialog.hide();
					for (BatchClassPluginDTO pluginDTO : pluginDTOs) {
						view.getPluginList().getToStore().remove(pluginDTO);
					}
				}
			});

			confirmationDialog.show();

		} else {
			addPlugin(pluginDTOs);
		}
	}

	/**
	 * Adds the plugin.
	 * 
	 * @param pluginDTOs the plugin dt os
	 */
	private void addPlugin(List<BatchClassPluginDTO> pluginDTOs) {
		for (BatchClassPluginDTO pluginDTO : pluginDTOs) {
			addPlugin(pluginDTO);
		}
		orderAllPlugins();
		// applySortInfo();
	}

	/**
	 * Adds the plugin.
	 * 
	 * @param pluginDTO the plugin dto
	 */
	private void addPlugin(BatchClassPluginDTO pluginDTO) {

		pluginDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		pluginDTO.setNew(true);
		pluginDTO.setDeleted(false);
		batchClassModuleDTO.addBatchClassPlugin(pluginDTO);

	}

	/**
	 * Order plugin.
	 * 
	 * @param pluginDTO the plugin dto
	 */
	private void orderPlugin(BatchClassPluginDTO pluginDTO) {
		int order = AdminConstants.INITIAL_ORDER_NUMBER;
		int index = view.getPluginList().getToStore().indexOf(pluginDTO);
		order = (index * AdminConstants.ORDER_NUMBER_OFFSET) + AdminConstants.INITIAL_ORDER_NUMBER;
		pluginDTO.setOrderNumber(order);

	}

	/**
	 * Order all plugins.
	 */
	private void orderAllPlugins() {
		List<BatchClassPluginDTO> pluginDTOs = view.getPluginList().getToStore().getAll();
		controller.getSelectedBatchClassModule().getBatchClass().setDirty(true);
		for (BatchClassPluginDTO pluginDTO : pluginDTOs) {
			orderPlugin(pluginDTO);
		}
		// applySortInfo();
		Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.WORKFLOW_UPDATE),
				LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_DEPLOY_WORKFLOW));
		BatchClassManagementEventBus.fireEvent(new PluginSubTreeRefreshEvent(batchClassModuleDTO));

	}

	/**
	 * Move dependencies along withplugin.
	 */
	private void moveDependenciesAlongWithplugin() {
		getDependenciesForCurrentModule();
		addDependancyInCorrespondingModule();
	}

	/**
	 * Gets the dependencies for current module.
	 * 
	 * @return the dependencies for current module
	 */
	private Set<Integer> getDependenciesForCurrentModule() {
		Set<Integer> selectedDependencies = getSelectedPluginDependenciesIndexSet();
		String selectedModuleName = batchClassModuleDTO.getModule().getName();
		String pluginName = null;
		String pluginModuleName = null;
		Set<Integer> currentModuleDependencies = new TreeSet<Integer>();
		ListStore<BatchClassPluginDTO> leftHandListStore = view.getPluginList().getFromStore();
		int selectedIndex = leftHandListStore.indexOf(view.getPluginList().getFromView().getSelectionModel().getSelectedItem());
		if (selectedDependencies != null && leftHandListStore != null) {
			for (Integer dependencyIndex : selectedDependencies) {
				if (null != dependencyIndex && leftHandListStore != null) {
					pluginName = leftHandListStore.get(dependencyIndex).getPlugin().getPluginName();
					pluginModuleName = DefaultPluginModuleMap.getModuleNameForPlugin(pluginName);
					allDependenciesSet.add(dependencyIndex);
				}
			}
		}
		return currentModuleDependencies;
	}

	/**
	 * Adds the dependancy in corresponding module.
	 */
	private void addDependancyInCorrespondingModule() {
		Set<Integer> pluginIndexSet = getAllDependenciesSet();
		String pluginName = null;
		String moduleName = null;
		PluginDetailsDTO pluginDetails = null;
		BatchClassModuleDTO batchClassModule = null;
		if (!CollectionUtil.isEmpty(pluginIndexSet)) {
			for (Integer dependencyIndex : pluginIndexSet) {
				if (dependencyIndex != null) {
					pluginName = view.getPluginList().getFromStore().get(dependencyIndex).getPlugin().getPluginName();// getLeftHandedListBox().getItemText(dependencyIndex);
					moduleName = DefaultPluginModuleMap.getModuleNameForPlugin(pluginName);
					batchClassModule = batchClassModuleDTO.getBatchClass().getModuleByName(moduleName);
					if (null != batchClassModule) {
						pluginDetails = view.getPluginList().getFromStore().get(dependencyIndex).getPlugin();
						int identifer = getPluginMaximumOrderNumber(batchClassModule) + 10;
						insertPlugin(batchClassModule, pluginName, pluginDetails, identifer);
					}
				}
			}
		}
	}

	/**
	 * Insert plugin.
	 * 
	 * @param batchClassModuleDTO the batch class module dto
	 * @param pluginName the plugin name
	 * @param pluginDetails the plugin details
	 * @param identifier the identifier
	 */
	private void insertPlugin(final BatchClassModuleDTO batchClassModuleDTO, String pluginName, PluginDetailsDTO pluginDetails,
			int identifier) {
		if (batchClassModuleDTO != null && batchClassModuleDTO.getNonDeletedPluginByName(pluginName) == null) {
			// Message.display("Plugin Inserted ", " " + pluginName);
			BatchClassPluginDTO batchClassPluginDTO = new BatchClassPluginDTO();
			batchClassPluginDTO.setPlugin(pluginDetails);
			batchClassPluginDTO.setBatchClassModule(batchClassModuleDTO);
			batchClassPluginDTO.setNew(true);
			batchClassPluginDTO.setOrderNumber(identifier);
			batchClassPluginDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
			batchClassModuleDTO.addBatchClassPlugin(batchClassPluginDTO);
			if (batchClassModuleDTO == controller.getSelectedBatchClassModule()) {
				view.addSelectedPluginDTO(Collections.singletonList(batchClassPluginDTO));
			}
			Timer timer = new Timer() {

				@Override
				public void run() {
					BatchClassManagementEventBus.fireEvent(new PluginSubTreeRefreshEvent(batchClassModuleDTO));
				}
			};
			timer.schedule(10);
		}
	}

	/**
	 * Gets the selected plugin dependencies index set.
	 * 
	 * @return the selected plugin dependencies index set
	 */
	public Set<Integer> getSelectedPluginDependenciesIndexSet() {
		return selectedPluginDependenciesIndexSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	/**
	 * Sets the batch class module dto.
	 * 
	 * @param selectionParameter the new batch class module dto
	 */
	public void setBatchClassModuleDTO(BatchClassModuleDTO selectionParameter) {
		this.batchClassModuleDTO = selectionParameter;
	}

	/**
	 * Search selected plugin name in right handed select box.
	 * 
	 * @param pluginName the plugin name
	 * @return the string
	 */
	private String searchSelectedPluginNameInRightHandedSelectBox(String pluginName) {
		String workFlowName = null;
		ListStore<BatchClassPluginDTO> pluginDTOs = view.getPluginList().getToStore();
		if (pluginDTOs != null) {
			int total_items = pluginDTOs.size();
			int count = 0;
			for (int i = 0; i < total_items; i++) {
				if (pluginDTOs.get(i).getPlugin().getPluginName().equalsIgnoreCase(pluginName)) {
					count++;
					if (count == 2) {
						workFlowName = getModuleNameForBatchClassModule(batchClassModuleDTO);
						break;
					}

				}
			}
		}
		return workFlowName;
	}

	/**
	 * Gets the plugin dt os.
	 * 
	 * @return the plugin dt os
	 */
	public void getPluginDTOs() {
		controller.getRpcService().getAllPluginDetailDTOs(new AsyncCallback<List<PluginDetailsDTO>>() {

			@Override
			public void onFailure(Throwable arg0) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_GET_PLUGINs_LIST), DialogIcon.ERROR);
			}

			@Override
			public void onSuccess(List<PluginDetailsDTO> pluginDetailsDTOs) {
				sortPluginDetailsDTOList(pluginDetailsDTOs, true);
				populateAndShowPluginView(pluginDetailsDTOs);
			}
		});
	}

	/**
	 * Populate and show plugin view.
	 * 
	 * @param pluginDTOs the plugin dt os
	 */
	private void populateAndShowPluginView(List<PluginDetailsDTO> pluginDTOs) {
		List<BatchClassPluginDTO> bcPluginDTOs = new ArrayList<BatchClassPluginDTO>();
		BatchClassPluginDTO bcPluginDTO;
		allPluginNames = new LinkedList<String>();
		for (PluginDetailsDTO pluginDetailDTO : pluginDTOs) {
			bcPluginDTO = new BatchClassPluginDTO();
			bcPluginDTO.setPlugin(pluginDetailDTO);
			bcPluginDTO.setIdentifier(pluginDetailDTO.getIdentifier());
			bcPluginDTOs.add(bcPluginDTO);
			allPluginNames.add(pluginDetailDTO.getPluginName());
			pluginNameToDtoMap.put(pluginDetailDTO.getPluginName(), bcPluginDTO);
		}
		view.populatedualList(bcPluginDTOs, batchClassModuleDTO.getBatchClassPlugins());
	}

	/**
	 * Gets the selected plugin dependencies index.
	 * 
	 * @return the selected plugin dependencies index
	 */
	private Set<Integer> getSelectedPluginDependenciesIndex() {
		Set<Integer> dependenciesIndexList = null;
		// remove already highlighted dependencies
		removeHighlightedDependencies(view.getPluginList().getFromView(), selectedPluginDependenciesIndexSet);

		Map<String, String> orderedPluginNames = getPluginIndexToNameMap();

		BatchClassPluginDTO pluginDTO = view.getPluginList().getFromView().getSelectionModel().getSelectedItem();
		if (pluginDTO != null) {
			String selectedPluginName = "";
			// Get plugin name
			selectedPluginName = pluginDTO.getPlugin().getPluginName();
			Set<String> dependenciesNameList = new HashSet<String>(0);
			// Get list of dependencies
			if (pluginDTO.getPlugin().getDependencies() != null) {
				for (DependencyDTO dependencyDTO : pluginDTO.getPlugin().getDependencies()) {
					if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.ORDER_BEFORE.getProperty())) {
						dependenciesNameList.addAll(getDependenciesSet(dependencyDTO.getDependencies()));
					}
				}
			}
			// Get indexes of Dependencies if not in the above
			dependenciesIndexList = new HashSet<Integer>(0);
			for (String dependencyName : dependenciesNameList) {
				if (!orderedPluginNames.values().contains(dependencyName)) {
					int valueIndex = getIndexForValueFromList(view.getPluginList().getFromView(), dependencyName);
					dependenciesIndexList.add(valueIndex);
				}
			}
		}
		return dependenciesIndexList;
	}

	/**
	 * Gets the dependencies set.
	 * 
	 * @param dependencies the dependencies
	 * @return the dependencies set
	 */
	private Set<String> getDependenciesSet(String dependencies) {
		Set<String> dependenciesSet = new HashSet<String>(0);
		String[] andDependencies = dependencies.split(AdminConstants.AND);
		for (String andDependency : andDependencies) {
			String[] orDependencies = andDependency.split(AdminConstants.OR);
			for (String dependencyName : orDependencies) {
				dependenciesSet.add(dependencyName);
			}
		}
		return dependenciesSet;
	}

	/**
	 * Gets the plugin index to name map.
	 * 
	 * @return the plugin index to name map
	 */
	private Map<String, String> getPluginIndexToNameMap() {
		List<BatchClassPluginDTO> selectedModulePluginNames = view.getPluginList().getToStore().getAll();
		Map<String, String> orderedPluginNames = new HashMap<String, String>(0);
		int index = 0;
		for (BatchClassPluginDTO pluginName : selectedModulePluginNames) {
			orderedPluginNames.put(String.valueOf(index++), pluginName.getPlugin().getPluginName());
		}
		return orderedPluginNames;
	}

	/**
	 * Sets the selected plugin dependencies index set.
	 * 
	 * @param selectedPluginDependenciesIndexSet the new selected plugin dependencies index set
	 */
	public void setSelectedPluginDependenciesIndexSet(Set<Integer> selectedPluginDependenciesIndexSet) {
		this.selectedPluginDependenciesIndexSet = selectedPluginDependenciesIndexSet;
	}

	/**
	 * Gets the module name for plugin.
	 * 
	 * @param batchClassDTO the batch class dto
	 * @param pluginName the plugin name
	 * @return the module name for plugin
	 */
	private String getModuleNameForPlugin(final BatchClassDTO batchClassDTO, final String pluginName) {
		String moduleName = null;
		if (pluginName != null && batchClassDTO != null) {
			Collection<BatchClassModuleDTO> modulesCollection = batchClassDTO.getModules();
			if (modulesCollection != null) {
				Iterator<BatchClassModuleDTO> moduleIterator = modulesCollection.iterator();
				while (moduleIterator.hasNext() && moduleName == null) {
					BatchClassModuleDTO moduleDTO = moduleIterator.next();
					if (moduleDTO != batchClassModuleDTO) {
						if (isPluginExistInBatchClassModule(moduleDTO, pluginName)) {
							moduleName = getModuleNameForBatchClassModule(moduleDTO);
						}
					}
				}
			}
		}
		return moduleName;
	}

	/**
	 * Checks if is plugin exist in batch class module.
	 * 
	 * @param moduleDTO the module dto
	 * @param pluginName the plugin name
	 * @return true, if is plugin exist in batch class module
	 */
	private boolean isPluginExistInBatchClassModule(final BatchClassModuleDTO moduleDTO, final String pluginName) {
		boolean pluginExist = false;
		if (moduleDTO != null) {
			pluginExist = moduleDTO.getPluginByName(pluginName) != null;
		}
		return pluginExist;
	}

	/**
	 * Gets the module name for batch class module.
	 * 
	 * @param batchClassModuleDTO the batch class module dto
	 * @return the module name for batch class module
	 */
	private String getModuleNameForBatchClassModule(final BatchClassModuleDTO batchClassModuleDTO) {
		String moduleName = null;
		if (batchClassModuleDTO != null) {
			ModuleDTO module = batchClassModuleDTO.getModule();
			if (null != module) {
				moduleName = module.getName();
			}
		}
		return moduleName;
	}

	/**
	 * On plugin select.
	 */
	public void onPluginSelect() {
		// Find new Dependencies
		Set<Integer> dependenciesIndexList = getSelectedPluginDependenciesIndex();
		setSelectedPluginDependenciesIndexSet(dependenciesIndexList);
	}

	/**
	 * Removes the highlighted dependencies.
	 * 
	 * @param listView the list view
	 * @param selectedPluginDependenciesIndex the selected plugin dependencies index
	 */
	private void removeHighlightedDependencies(ListView<BatchClassPluginDTO, String> listView,
			Set<Integer> selectedPluginDependenciesIndex) {
		if (null != listView && selectedPluginDependenciesIndex != null) {
			for (Integer index : selectedPluginDependenciesIndex) {
				if (null != listView.getElement(index)) {
					listView.getElement(index).removeClassName("dependencyColor");
				}
			}
		}
	}

	/**
	 * Gets the index for value from list.
	 * 
	 * @param listView the list view
	 * @param dependencyName the dependency name
	 * @return the index for value from list
	 */
	private Integer getIndexForValueFromList(ListView<BatchClassPluginDTO, String> listView, String dependencyName) {
		int valueIndex = -1;
		ListStore<BatchClassPluginDTO> pluginDTOs = listView.getStore();
		for (int index = 0; index < pluginDTOs.size(); index++) {
			if (pluginDTOs.get(index).getPlugin().getPluginName().equals(dependencyName)) {
				valueIndex = index;
				listView.getElement(index).addClassName("dependencyColor");
				break;
			}
		}
		return valueIndex;
	}

	/**
	 * Sort plugin details dto list.
	 * 
	 * @param pluginsList the plugins list
	 * @param ascending the ascending
	 */
	public void sortPluginDetailsDTOList(final List<PluginDetailsDTO> pluginsList, final boolean ascending) {
		Collections.sort(pluginsList, new Comparator<PluginDetailsDTO>() {

			@Override
			public int compare(final PluginDetailsDTO PluginDTO1, final PluginDetailsDTO PluginDTO2) {
				int result;
				final String orderNumberOne = PluginDTO1.getPluginName();
				final String orderNumberTwo = PluginDTO2.getPluginName();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
	}

	/**
	 * Sets the batch class dto plugins list.
	 */
	public void setBatchClassDTOPluginsList() {
		addDependancyInCorrespondingModule();
		allDependenciesSet.clear();
	}

	/**
	 * Gets the plugin maximum order number.
	 * 
	 * @param batchClassModule the batch class module
	 * @return the plugin maximum order number
	 */
	private int getPluginMaximumOrderNumber(final BatchClassModuleDTO batchClassModule) {
		int maxOrderNumber = 0;
		if (null != batchClassModule) {
			Collection<BatchClassPluginDTO> pluginCollection = batchClassModule.getBatchClassPlugins(true);
			if (!CollectionUtil.isEmpty(pluginCollection)) {
				for (BatchClassPluginDTO pluginDTO : pluginCollection) {
					maxOrderNumber = Math.max(maxOrderNumber, pluginDTO.getOrderNumber());
				}
			}
		}
		return maxOrderNumber;
	}

	/**
	 * Gets the all dependencies set.
	 * 
	 * @return the all dependencies set
	 */
	public Set<Integer> getAllDependenciesSet() {
		return allDependenciesSet;
	}

	/**
	 * Sort modules list.
	 * 
	 * @param modulesList the modules list
	 */
	public void sortModulesList(final List<BatchClassModuleDTO> modulesList) {
		Collections.sort(modulesList, new Comparator<BatchClassModuleDTO>() {

			@Override
			public int compare(final BatchClassModuleDTO batchClassModuleDTO1, final BatchClassModuleDTO batchClassModuleDTO2) {
				int result;
				final Integer orderNumberOne = batchClassModuleDTO1.getOrderNumber();
				final Integer orderNumberTwo = batchClassModuleDTO2.getOrderNumber();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		});
	}

	/**
	 * Gets the plugin comparator.
	 * 
	 * @return the plugin comparator
	 */
	private Comparator<BatchClassPluginDTO> getPluginComparator() {
		return new Comparator<BatchClassPluginDTO>() {

			@Override
			public int compare(final BatchClassPluginDTO batchClassPluginDTO1, final BatchClassPluginDTO batchClassPluginDTO2) {
				int result;
				final Integer orderNumberOne = batchClassPluginDTO1.getOrderNumber();
				final Integer orderNumberTwo = batchClassPluginDTO2.getOrderNumber();
				if (orderNumberOne != null && orderNumberTwo != null) {
					result = orderNumberOne.compareTo(orderNumberTwo);
				} else if (orderNumberOne == null && orderNumberTwo == null) {
					result = 0;
				} else if (orderNumberOne == null) {
					result = -1;
				} else {
					result = 1;
				}
				return result;
			}
		};
	}

}
