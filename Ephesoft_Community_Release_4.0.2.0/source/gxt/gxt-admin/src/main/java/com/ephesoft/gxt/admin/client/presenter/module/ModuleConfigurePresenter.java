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

package com.ephesoft.gxt.admin.client.presenter.module;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.NavigationEvent.NavigationNodeDeletionEvent;
import com.ephesoft.gxt.admin.client.event.SubTreeRefreshEvent.ModuleSubTreeRefreshEvent;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.module.ModuleConfigureView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DualListButtonListener;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassModuleDTO;
import com.ephesoft.gxt.core.shared.dto.ModuleDTO;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;

// TODO: Auto-generated Javadoc
/**
 * The Class ModuleConfigurePresenter.
 */
public class ModuleConfigurePresenter extends BatchClassInlinePresenter<ModuleConfigureView> {

	/** The batch class dto. */
	BatchClassDTO batchClassDTO;
	
	/** The module names. */
	List<String> moduleNames;
	
	/** The description map. */
	Map<String, String> descriptionMap;
	
	/** The cached dto map. */
	Map<String, BatchClassModuleDTO> cachedDTOMap;
	
	/** The module name to dto map. */
	Map<String, BatchClassModuleDTO> moduleNameToDtoMap;
	
	/** The bc module name to dto map. */
	Map<String, BatchClassModuleDTO> bcModuleNameToDtoMap;
	
	/** The selected module name to dto map. */
	Map<String, BatchClassModuleDTO> selectedModuleNameToDtoMap;
	
	/** The selected module name to batch class dto map. */
	Map<String, BatchClassModuleDTO> selectedModuleNameToBatchClassDtoMap;
	
	/** The is dragged from right. */
	private boolean isDraggedFromRight = false;

	/**
	 * Instantiates a new module configure presenter.
	 *
	 * @param controller the controller
	 * @param view the view
	 */
	public ModuleConfigurePresenter(BatchClassManagementController controller, ModuleConfigureView view) {
		super(controller, view);
		addDropHandlers();
		applySortInfo();
		view.getModuleList().getAppearance().allLeft().setStyle("disableButton");
		moduleNameToDtoMap = new LinkedHashMap<String, BatchClassModuleDTO>();
		// loadModules();
		addButtonListener();
	}

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<ModuleConfigurePresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/* (non-Javadoc)
	 * @see com.ephesoft.gxt.core.client.event.BindHandler#bind()
	 */
	@Override
	public void bind() {
		this.batchClassDTO = controller.getSelectedBatchClass();
		boolean isReloadRequired = false;
		if (null != moduleNameToDtoMap && moduleNameToDtoMap.size() == 0) {
			isReloadRequired = true;
		}
		populateDualList(isReloadRequired);
		applySortInfo();
	}

	/**
	 * Adds the button listener.
	 */
	private void addButtonListener() {
		DualListButtonListener<BatchClassModuleDTO> buttonListener = new DualListButtonListener<BatchClassModuleDTO>() {

			@Override
			public void onLeftClick(List<BatchClassModuleDTO> moduleDTOs) {
				if (null != moduleDTOs && moduleDTOs.size() > 0) {
					onLeft(moduleDTOs);
					applySortInfo();
				}
			}

			@Override
			public void onRightClick(List<BatchClassModuleDTO> moduleDTOs) {
				if (null != moduleDTOs && moduleDTOs.size() > 0) {
					onRight(moduleDTOs);
				}
			}

			@Override
			public void onUpClick() {
				orderAllModules(true);
			}

			@Override
			public void onDownClick() {
				orderAllModules(true);
			}
		};
		view.getModuleList().setButtonListener(buttonListener);
	}

	/* (non-Javadoc)
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	/**
	 * On right movement of Modules.
	 *
	 * @param moduleDTOs the module dt os
	 * @param isDragged the is dragged
	 */
	private void onRight(List<BatchClassModuleDTO> moduleDTOs) {
		if (null != moduleDTOs && moduleDTOs.size() > 0) {
			view.getModuleList().getToView().getSelectionModel().deselectAll();
			for (BatchClassModuleDTO moduleDTO : moduleDTOs) {
				BatchClassModuleDTO newModuleDTO = createCopyOfModuleDTO(moduleDTO);
				batchClassDTO.addModule(newModuleDTO);
				int dtoIndex = view.getModuleList().getToStore().indexOf(moduleDTO);
				int order = AdminConstants.INITIAL_ORDER_NUMBER;
				order = (dtoIndex * AdminConstants.ORDER_NUMBER_OFFSET) + AdminConstants.INITIAL_ORDER_NUMBER;
				newModuleDTO.setOrderNumber(order);
				view.getModuleList().getToStore().remove(moduleDTO);
				view.getModuleList().getToStore().add(newModuleDTO);
//				view.getModuleList().getToView().getSelectionModel().select(newModuleDTO,false);
			}
			orderAllModules(true);
//			populateDualList(false);
		}
	}

	/**
	 * On left Movement Of Modules.
	 *
	 * @param moduleDTOs the module dtos
	 */
	private void onLeft(List<BatchClassModuleDTO> moduleDTOs) {
		if (null != moduleDTOs && moduleDTOs.size() > 0) {
			view.getModuleList().getToStore().clearSortInfo();
			for (BatchClassModuleDTO moduleDTO : moduleDTOs) {
				if (moduleDTO.isNew()) {
					batchClassDTO.removeModule(moduleDTO);
				} else {
					moduleDTO.setDeleted(true);
					batchClassDTO.addModule(moduleDTO);
				}
				BatchClassManagementEventBus.fireEvent(new NavigationNodeDeletionEvent(moduleDTO));
			}
			orderAllModules(false);
		}
	}

	
	/**
	 * Adds the drop handlers.
	 */
	private void addDropHandlers() {
		view.getModuleList().getDragSourceFromField().addDropHandler(new DndDropHandler() {

			@Override
			public void onDrop(DndDropEvent event) {
				isDraggedFromRight = false;
				view.getModuleList().getToStore().clearSortInfo();
			}
		});

		view.getModuleList().getDragSourceToField().addDropHandler(new DndDropHandler() {

			@Override
			public void onDrop(DndDropEvent event) {
				isDraggedFromRight = true;
				view.getModuleList().getToStore().clearSortInfo();
			}
		});

		view.getModuleList().getDropTargetFromField().addDropHandler(new DndDropHandler() {

			@Override
			public void onDrop(DndDropEvent event) {
				if (isDraggedFromRight) {
					isDraggedFromRight = false;
					@SuppressWarnings("unchecked")
					List<BatchClassModuleDTO> moduleDTOs = (List<BatchClassModuleDTO>) event.getData();
					onLeft(moduleDTOs);
				}
				applySortInfo();
			}
		});

		view.getModuleList().getDropTargetToField().addDropHandler(new DndDropHandler() {

			@Override
			public void onDrop(DndDropEvent event) {
				view.getModuleList().getToStore().clearSortInfo();

				if (!isDraggedFromRight) {
					isDraggedFromRight = true;
					@SuppressWarnings("unchecked")
					List<BatchClassModuleDTO> moduleDTOs = (List<BatchClassModuleDTO>) event.getData();
					onRight(moduleDTOs);
				} else {
					orderAllModules(true);
					isDraggedFromRight = false;
				}
				applySortInfo();
			}
		});

	}

	/**
	 * Order module.
	 *
	 * @param moduleDTO the module dto
	 */
	private void orderModule(BatchClassModuleDTO moduleDTO) {
		int order = AdminConstants.INITIAL_ORDER_NUMBER;
		int index = view.getModuleList().getToStore().indexOf(moduleDTO);
		order = (index * AdminConstants.ORDER_NUMBER_OFFSET) + AdminConstants.INITIAL_ORDER_NUMBER;
		moduleDTO.setOrderNumber(order);
	}

	
	/**
	 * Order all modules.
	 *
	 * @param isRefreshTree the is refresh tree
	 */
	private void orderAllModules(boolean isRefreshTree) {
		List<BatchClassModuleDTO> moduleDTOs = view.getModuleList().getToStore().getAll();
		controller.getSelectedBatchClass().setDirty(true);
		for (BatchClassModuleDTO moduleDTO : moduleDTOs) {
			orderModule(moduleDTO);
		}
		Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.WORKFLOW_UPDATE),
				LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_DEPLOY_WORKFLOW));
		if(isRefreshTree){
			BatchClassManagementEventBus.fireEvent(new ModuleSubTreeRefreshEvent(batchClassDTO));
		}
	}

	/**
	 * Sets the batch class dto.
	 *
	 * @param selectionParameter the new batch class dto
	 */
	public void setBatchClassDTO(BatchClassDTO selectionParameter) {
		this.batchClassDTO = selectionParameter;
	}

	/**
	 * Gets the modules dto from current selected batch class.
	 *
	 * @return the modules dto
	 */
	public Collection<BatchClassModuleDTO> getModulesDTO() {
		return batchClassDTO.getModules();
	}

	/**
	 * Load modules.
	 */
	private void loadModules() {

		controller.getRpcService().getAllModules(new AsyncCallback<List<ModuleDTO>>() {

			@Override
			public void onFailure(final Throwable arg0) {

			}

			@Override
			public void onSuccess(final List<ModuleDTO> moduleDTOs) {
				final List<String> moduleNames = new ArrayList<String>();
				bcModuleNameToDtoMap = new HashMap<String, BatchClassModuleDTO>();
				moduleNameToDtoMap = new HashMap<String, BatchClassModuleDTO>();
				ModuleDTO dto;
				BatchClassModuleDTO batchClassModuleDTO;
				for (final ModuleDTO moduleDTO : moduleDTOs) {
					final String moduleName = moduleDTO.getName();
					moduleNames.add(moduleName);
					dto = new ModuleDTO();
					dto.setName(moduleDTO.getDescription());
					dto.setDescription(moduleDTO.getName());
					dto.setIdentifier(moduleDTO.getIdentifier());
					dto.setVersion(moduleDTO.getVersion());

					batchClassModuleDTO = new BatchClassModuleDTO();
					batchClassModuleDTO.setIdentifier(dto.getIdentifier());
					batchClassModuleDTO.setWorkflowName(dto.getName());
					batchClassModuleDTO.setNew(true);
					batchClassModuleDTO.setModule(dto);
					moduleNameToDtoMap.put(moduleName, batchClassModuleDTO);
				}
				populateDualList(false);
				controller.getModulePresenter().setAvailableModules(moduleNameToDtoMap.keySet());
			}
		});
	}

	/**
	 * Populating Multiple Select dual list with available and selected modules.
	 *
	 * @param reloadAvailableModules the reload available modules
	 */
	public void populateDualList(boolean reloadAvailableModules) {
		if (reloadAvailableModules) {
			loadModules();
		}
		view.setAllModuleDTO(moduleNameToDtoMap.values());
		view.setSelectedModuleDTO(batchClassDTO.getModules());
	}

	/**
	 * Creates the copy of module dto.
	 *
	 * @param batchClassModuleDTO the batch class module dto
	 * @return the batch class module dto
	 */
	private BatchClassModuleDTO createCopyOfModuleDTO(BatchClassModuleDTO batchClassModuleDTO) {
		BatchClassModuleDTO copyOfBCModuleDTO = new BatchClassModuleDTO();
		copyOfBCModuleDTO.setDeleted(false);
		copyOfBCModuleDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		copyOfBCModuleDTO.setNew(true);
		copyOfBCModuleDTO.setOrderNumber(batchClassModuleDTO.getOrderNumber());
		copyOfBCModuleDTO.setRemoteBatchClassIdentifier(batchClassModuleDTO.getRemoteBatchClassIdentifier());
		copyOfBCModuleDTO.setRemoteURL(batchClassModuleDTO.getRemoteURL());
		String newWorkflowName = generateWorkFlowNameOfModule(batchClassModuleDTO.getWorkflowName());
		copyOfBCModuleDTO.setWorkflowName(newWorkflowName);
		copyOfBCModuleDTO.setBatchClass(batchClassDTO);
		ModuleDTO copyOfmoduleDTO = new ModuleDTO();
		copyOfmoduleDTO.setDescription(batchClassModuleDTO.getModule().getName());
		copyOfmoduleDTO.setIdentifier(batchClassModuleDTO.getModule().getIdentifier());
		copyOfmoduleDTO.setName(batchClassModuleDTO.getModule().getDescription());
		copyOfmoduleDTO.setVersion(batchClassModuleDTO.getModule().getVersion());
		copyOfBCModuleDTO.setModule(copyOfmoduleDTO);

		return copyOfBCModuleDTO;
	}

	/**
	 * Generate work flow name of module.
	 *
	 * @param moduleName the module name
	 * @return the string
	 */
	public String generateWorkFlowNameOfModule(String moduleName) {
		String batchClassIdentifier = batchClassDTO.getIdentifier();
		StringBuffer workflowNameStringBuffer = new StringBuffer();
		workflowNameStringBuffer.append(moduleName.replaceAll(String.valueOf(BatchClassConstants.SPACE),
				BatchClassConstants.UNDERSCORE));
		workflowNameStringBuffer.append(BatchClassConstants.UNDERSCORE);
		workflowNameStringBuffer.append(batchClassIdentifier);
		int instance = 0;
		String newWorkflowName = workflowNameStringBuffer.toString();
		if (batchClassDTO.getModuleByWorkflowName(newWorkflowName) != null) {
			do {
				instance++;
			} while (batchClassDTO.getModuleByWorkflowName(newWorkflowName + BatchClassConstants.UNDERSCORE + instance) != null);
			workflowNameStringBuffer.append(BatchClassConstants.UNDERSCORE);
			workflowNameStringBuffer.append(instance);
		}
		newWorkflowName = workflowNameStringBuffer.toString();
		return newWorkflowName;
	}

	/**
	 * Adds the new module to module map.
	 *
	 * @param moduleName the module name
	 * @param moduleDTO the module dto
	 */
	public void addNewModuleToModuleMap(String moduleName, ModuleDTO moduleDTO) {
		BatchClassModuleDTO bcModuleDTO = new BatchClassModuleDTO();
		bcModuleDTO.setModule(moduleDTO);
		bcModuleDTO.setWorkflowName(moduleName);
		bcModuleNameToDtoMap.put(moduleName, bcModuleDTO);
	}

	/**
	 * Apply sort info.
	 */
	private void applySortInfo() {
		view.getModuleList().getToStore().addSortInfo(new StoreSortInfo<BatchClassModuleDTO>(getModuleComparator(), SortDir.ASC));
	}

	/**
	 * Gets the module comparator.
	 *
	 * @return the module comparator
	 */
	private Comparator<BatchClassModuleDTO> getModuleComparator() {
		return new Comparator<BatchClassModuleDTO>() {

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
		};
	}
}
