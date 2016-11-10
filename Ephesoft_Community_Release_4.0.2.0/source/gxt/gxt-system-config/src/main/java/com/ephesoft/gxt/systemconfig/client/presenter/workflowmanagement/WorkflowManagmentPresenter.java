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

package com.ephesoft.gxt.systemconfig.client.presenter.workflowmanagement;

import java.util.List;

import com.ephesoft.dcma.da.property.DependencyTypeProperty;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.DependencyDTO;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.DeleteSelectedDependenciesEvent;
import com.ephesoft.gxt.systemconfig.client.event.HidePluginDependenciesViewEvent;
import com.ephesoft.gxt.systemconfig.client.event.PluginsGridSelectionChangeEvent;
import com.ephesoft.gxt.systemconfig.client.event.ShowAddPluginDependenciesViewEvent;
import com.ephesoft.gxt.systemconfig.client.event.ShowEditPluginDependenciesViewEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigCompositePresenter;
import com.ephesoft.gxt.systemconfig.client.view.workflowmanagement.WorkflowManagmentView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class WorkflowManagmentPresenter extends SystemConfigCompositePresenter<WorkflowManagmentView, PluginDetailsDTO> {

	interface CustomEventBinder extends EventBinder<WorkflowManagmentPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private WorkflowManagmentMenuPresenter workflowManagmentMenuPresenter;
	private PluginDependenciesPresenter pluginDependenciesPresenter;
	private ImportPluginPresenter importPluginPresenter;
	private PluginDependencyGridPresenter pluginDependencyGridPresenter;
	private PluginDetailsDTO selectedPluginDTO;
	private DependencyDTO selectedDependencyDTO;
	private List<DependencyDTO> listOfSelectedPluginDependencyDTO;

	public WorkflowManagmentPresenter(SystemConfigController controller, WorkflowManagmentView view) {
		super(controller, view);

		workflowManagmentMenuPresenter = new WorkflowManagmentMenuPresenter(controller, view.getWorkflowManagmentMenuView());
		pluginDependenciesPresenter = new PluginDependenciesPresenter(controller, view.getPluginDependenciesView());
		importPluginPresenter = new ImportPluginPresenter(controller, view.getImportPluginView());
		pluginDependencyGridPresenter = new PluginDependencyGridPresenter(controller, view.getPluginDependencyGridView());
	}

	@Override
	public void bind() {
		controller.setBottomPanelHeader(LocaleDictionary.getConstantValue(SystemConfigConstants.IMPORT_PLUGIN_HEADER));
		// controller.getBatchClassLayout().getBottomPanel().collapse();
		pluginDependencyGridPresenter.loadGrid(this.selectedPluginDTO);
		selectedDependencyDTO = null;
	}

	@Override
	public void init(PluginDetailsDTO selectedPluginID) {
//		PluginDetailsDTO selectedPlugin = controller.getPluginDTOForID(selectedPluginID);
		if (null != selectedPluginID) {
			//this.selectedPluginDTO = selectedPluginID;
			this.selectedPluginDTO = controller.getPluginDTOForID(selectedPluginID.getIdentifier());
		}
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void showAddPluginDependenciesEvent(final ShowAddPluginDependenciesViewEvent showAddPluginDependenciesViewEvent) {
		pluginDependenciesPresenter.loadDependencies(this.selectedPluginDTO);
		pluginDependenciesPresenter.setCurrentDisplayedDependency(null);
		controller.setBottomPanelHeader(LocaleDictionary.getConstantValue(SystemConfigConstants.DEPENDENCY_ADD_HEADER));
		pluginDependenciesPresenter.bind();
		controller.setBottomPanelView(view.getPluginDependenciesView());
	}

	@EventHandler
	public void showEditPluginDependenciesEvent(final ShowEditPluginDependenciesViewEvent showEditPluginDependenciesViewEvent) {
		if (null != selectedPluginDTO) {
			if (CollectionUtil.isEmpty(selectedPluginDTO.getDependencies())) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(SystemConfigMessages.NO_RECORD_TO_EDIT), DialogIcon.ERROR);
			} else if (null == selectedDependencyDTO) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(SystemConfigMessages.NO_DEPENDENCY_SELECTED), DialogIcon.ERROR);
			} else {
				pluginDependenciesPresenter.loadDependencies(this.selectedPluginDTO);
				pluginDependenciesPresenter.setCurrentDisplayedDependency(selectedDependencyDTO);
				controller.setBottomPanelHeader(LocaleDictionary.getConstantValue(SystemConfigConstants.DEPENDENCY_EDIT_HEADER));
				pluginDependenciesPresenter.bind();
				controller.setBottomPanelView(view.getPluginDependenciesView());
			}
		}
	}

	@EventHandler
	public void hidePluginDependenciesEvent(final HidePluginDependenciesViewEvent hidePluginDependenciesViewEvent) {
		this.selectedPluginDTO.setDirty(false);
		if (null != selectedDependencyDTO) {
			selectedDependencyDTO.setNew(false);
			selectedDependencyDTO.setDirty(false);
		}
		controller.setBottomPanelView(view.getImportPluginView());
		controller.setBottomPanelHeader(LocaleDictionary.getConstantValue(SystemConfigConstants.IMPORT_PLUGIN_HEADER));
	}

	@EventHandler
	public void onGridSelectionChanged(final PluginsGridSelectionChangeEvent dependencyChangeEvent) {
		if (null != dependencyChangeEvent) {
			this.selectedDependencyDTO = dependencyChangeEvent.getDto();
			pluginDependenciesPresenter.setCurrentDisplayedDependency(selectedDependencyDTO);
			hidePluginDependenciesEvent(null);

		}
	}

	@EventHandler
	public void onDeleteSelectedPluginDependency(final DeleteSelectedDependenciesEvent deletedSelectedPluginEvent) {
		listOfSelectedPluginDependencyDTO = pluginDependencyGridPresenter.getSelectedPluginsDependencies();
		
		if (null != selectedPluginDTO) {
			if (CollectionUtil.isEmpty(selectedPluginDTO.getDependencies())) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(SystemConfigMessages.NO_RECORD_TO_DELETE), DialogIcon.ERROR);
			} else if (CollectionUtil.isEmpty(listOfSelectedPluginDependencyDTO)) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(SystemConfigMessages.NO_DEPENDENCY_SELECTED), DialogIcon.ERROR);
			} else {
				ConfirmationDialog dialog = new ConfirmationDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.WARNING),
						LocaleDictionary.getMessageValue(SystemConfigMessages.DELETE_DEPENDENCY));
				TextButton okButton = dialog.getButton(PredefinedButton.OK);
				okButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						deleteSelectedDependency();
					}
				});
				dialog.show();
				dialog.center();
			}
		}
	}

	private void deleteSelectedDependency() {
		final String idOfSelectedPlugin = selectedPluginDTO.getIdentifier();
		final List<DependencyDTO> selectedPluginDependencyList = selectedPluginDTO.getDependencies();
		boolean updateDependencies = false;
		for (final DependencyDTO selectedDependencyDTO : listOfSelectedPluginDependencyDTO) {
			if (selectedPluginDependencyList.contains(selectedDependencyDTO)) {
				selectedPluginDTO.setDirty(true);
				selectedDependencyDTO.setDeleted(true);
				updateDependencies = true;
			}
		}
		if (updateDependencies) {
			ScreenMaskUtility.maskScreen();
			controller.getRpcService().updateAllPluginDetailsDTOs(controller.getAllPluginsList(),
					new AsyncCallback<List<PluginDetailsDTO>>() {

						@Override
						public void onSuccess(final List<PluginDetailsDTO> result) {
							ScreenMaskUtility.unmaskScreen();
							controller.setAllPluginsList(result);
							selectedPluginDTO = controller.getPluginDTOForID(idOfSelectedPlugin);
							selectedDependencyDTO = null;
							bind();
							Message.display(LocaleDictionary.getConstantValue(SystemConfigConstants.SUCCESS_TITLE),
									LocaleDictionary.getMessageValue(SystemConfigMessages.DEPENDENCY_DELETED));
						}

						@Override
						public void onFailure(final Throwable caught) {
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(SystemConfigMessages.ERROR_UPDATING_PLUGINS), DialogIcon.ERROR);

						}

					});
		}
	}

	// Unused method to delete the plugin.
	@SuppressWarnings("static-access")
	public void deletePlugin() {

		final PluginDetailsDTO selectedPluginDTO = this.selectedPluginDTO;

		if (selectedPluginDTO != null) {
			ScreenMaskUtility.maskScreen(LocaleDictionary.getLocaleDictionary().getMessageValue(
					SystemConfigMessages.DELETING_PLUGIN_MESSAGE));
			controller.getRpcService().deletePlugin(selectedPluginDTO, new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean isPluginDeleted) {
					if (isPluginDeleted) {
						updateAllPluginDependencies(controller.getAllPluginsList(), selectedPluginDTO.getPluginName());
						controller.getRpcService().updateAllPluginDetailsDTOs(controller.getAllPluginsList(),
								new AsyncCallback<List<PluginDetailsDTO>>() {

									@Override
									public void onSuccess(List<PluginDetailsDTO> allPluginDetailsDTOs) {
										ScreenMaskUtility.unmaskScreen();
										controller.setAllPluginsList(allPluginDetailsDTOs);
										Message.display(
												LocaleDictionary.getConstantValue(SystemConfigConstants.SUCCESS_TITLE),
												LocaleDictionary.getLocaleDictionary().getMessageValue(
														SystemConfigMessages.PLUGIN_DELETED_SUCCESSFULLY));
									}

									@Override
									public void onFailure(Throwable arg0) {
										ScreenMaskUtility.unmaskScreen();
									}
								});

					}
				}

				@Override
				public void onFailure(Throwable failureMessage) {
					ScreenMaskUtility.unmaskScreen();
					final String message = failureMessage.getLocalizedMessage();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
							LocaleDictionary.getLocaleDictionary().getMessageValue(message), DialogIcon.ERROR);
				}
			});
		}
	}

	private void updateAllPluginDependencies(List<PluginDetailsDTO> allPlugins, String pluginName) {

		StringBuilder orRegexBuilder = new StringBuilder();

		orRegexBuilder.append(SystemConfigConstants.BOUNDARY);
		orRegexBuilder.append(pluginName);
		orRegexBuilder.append(SystemConfigConstants.OR_REPLACE_REGEX);
		orRegexBuilder.append(pluginName);
		orRegexBuilder.append(SystemConfigConstants.BOUNDARY);

		String orRegex = orRegexBuilder.toString();

		StringBuilder andRegexBuilder = new StringBuilder();

		andRegexBuilder.append(SystemConfigConstants.BOUNDARY);
		andRegexBuilder.append(pluginName);
		andRegexBuilder.append(SystemConfigConstants.AND_REPLACE_REGEX);
		andRegexBuilder.append(pluginName);
		andRegexBuilder.append(SystemConfigConstants.BOUNDARY);

		String andRegex = andRegexBuilder.toString();

		for (PluginDetailsDTO pluginDetailsDTO : allPlugins) {

			for (DependencyDTO dependencyDTO : pluginDetailsDTO.getDependencies()) {
				String originalDependenciesString = dependencyDTO.getDependencies();

				if (dependencyDTO.getDependencyType().equals(DependencyTypeProperty.ORDER_BEFORE.getProperty())) {

					if (!originalDependenciesString.equals(pluginName)) {
						String newDependenciesString = originalDependenciesString.replaceAll(orRegex,
								SystemConfigConstants.EMPTY_STRING).replaceAll(andRegex, SystemConfigConstants.EMPTY_STRING);
						dependencyDTO.setDependencies(newDependenciesString);
						if (newDependenciesString.isEmpty()) {
							pluginDetailsDTO.setDirty(true);
							dependencyDTO.setDeleted(true);
						}
						if (!originalDependenciesString.equals(newDependenciesString)) {
							pluginDetailsDTO.setDirty(true);
							dependencyDTO.setDirty(true);
						}
					} else {
						pluginDetailsDTO.setDirty(true);
						dependencyDTO.setDependencies(SystemConfigConstants.EMPTY_STRING);
						dependencyDTO.setDeleted(true);
					}
				}
			}
		}
	}

	public WorkflowManagmentMenuPresenter getWorkflowManagmentMenuPresenter() {
		return workflowManagmentMenuPresenter;
	}

	public PluginDependenciesPresenter getPluginDependenciesPresenter() {
		return pluginDependenciesPresenter;
	}

	public ImportPluginPresenter getImportPluginPresenter() {
		return importPluginPresenter;
	}

	public PluginDependencyGridPresenter getPluginDependencyGridPresenter() {
		return pluginDependencyGridPresenter;
	}

	public DependencyDTO getSelectedDependencyDTO() {
		return selectedDependencyDTO;
	}

	public void setSelectedDependencyDTO(DependencyDTO selectedDependencyDTO) {
		this.selectedDependencyDTO = selectedDependencyDTO;
	}

	public PluginDetailsDTO getSelectedPluginDTO() {
		return selectedPluginDTO;
	}
}
