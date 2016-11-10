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

package com.ephesoft.gxt.systemconfig.client.view.workflowmanagement;

import java.util.List;

import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.PluginDetailsDTO;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.presenter.workflowmanagement.PluginDependenciesPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.ephesoft.gxt.systemconfig.client.widget.DependencyDragDropDualList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class PluginDependenciesView extends SystemConfigInlineView<PluginDependenciesPresenter> {

	interface Binder extends UiBinder<Widget, PluginDependenciesView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected Label pluginNameLabel;

	@UiField
	protected Label pluginName;

	// @UiField
	// protected Label headerLabel;

	@UiField
	protected Label pluginDescriptionLabel;

	@UiField
	protected Label pluginDescription;

	@UiField
	protected Label dependencyTypeLabel;

	@UiField
	protected ListBox dependencyType;

	@UiField
	protected Label pluginDependencyLabel;

	@UiField
	protected TextButton saveButton;

	@UiField
	protected TextButton resetButton;

	@UiField
	protected TextButton cancelButton;

	@UiField(provided = true)
	DependencyDragDropDualList<String> pluginDependency;

	public PluginDependenciesView() {
		super();
		initWidget(binder.createAndBindUi(this));

		// Sets text and other important properties for all widgets.
		setWidgetProperties();

		// Sets ids for each widget.
		setWidgetIDs();

		addValueChangeHandlerToListBox();

	}

	private void setWidgetIDs() {
		WidgetUtil.setID(pluginNameLabel, "PDV_pluginName_Label");
		WidgetUtil.setID(pluginName, "PDV_pluginName");
		WidgetUtil.setID(pluginDescriptionLabel, "PDV_pluginDescription_Label");
		WidgetUtil.setID(pluginDescription, "PDV_pluginDescription");
		WidgetUtil.setID(dependencyTypeLabel, "PDV_dependencyType_Label");
		WidgetUtil.setID(dependencyType, "PDV_dependencyType");
		WidgetUtil.setID(pluginDependencyLabel, "PDV_pluginDependency_Label");
		WidgetUtil.setID(pluginDependencyLabel, "PDV_pluginDependency");
		WidgetUtil.setID(saveButton, "PDV_saveButton");
		WidgetUtil.setID(resetButton, "PDV_resetButton");
		WidgetUtil.setID(cancelButton, "PDV_cancelButton");
	}

	private void setWidgetProperties() {
		pluginNameLabel.setText(LocaleDictionary.getConstantValue(SystemConfigConstants.PLUGIN_NAME_LABEl));
		pluginDescriptionLabel.setText(LocaleDictionary.getConstantValue(SystemConfigConstants.PLUGIN_DESCRIPTION_LABEl));
		dependencyTypeLabel.setText(LocaleDictionary.getConstantValue(SystemConfigConstants.DEPENDENCY_TYPE_LABEL));
		pluginDependencyLabel.setText(LocaleDictionary.getConstantValue(SystemConfigConstants.DEPENDENCIES_LABEL));

		pluginNameLabel.addStyleName("boldTxt");
		pluginDescriptionLabel.addStyleName("boldTxt");
		dependencyTypeLabel.addStyleName("boldTxt");
		pluginDependencyLabel.addStyleName("boldTxt");

		// headerLabel.setText(LocaleDictionary.getConstantValue(SystemConfigConstants.CONNECTION_HEADER));
		// headerLabel.addStyleName("labelCss");

		saveButton.setText(LocaleDictionary.getConstantValue(SystemConfigConstants.SAVE_BUTTON));
		resetButton.setText(LocaleDictionary.getConstantValue(SystemConfigConstants.RESET_BUTTON));
		cancelButton.setText(LocaleDictionary.getConstantValue(SystemConfigConstants.CANCEL_BUTTON));
		saveButton.addStyleName("system_config_button_css");
		resetButton.addStyleName("system_config_button_css");
		cancelButton.addStyleName("system_config_button_css");
	}

	private void addValueChangeHandlerToListBox() {
		dependencyType.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				final String selectedItem = dependencyType.getItemText(dependencyType.getSelectedIndex());
				presenter.checkAndDisableDualDependencySelector(selectedItem);
			}

		});

		saveButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				presenter.onSaveClicked();
			}

		});

		resetButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				presenter.onResetClicked();
			}

		});

		cancelButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				presenter.onCancelClicked();
			}

		});
	}

	@Override
	public void initialize() {
		pluginDependency = new DependencyDragDropDualList<String>(null, PropertyAccessModel.PLUGIN_DEPENDENCIES);
		pluginDependency.setWidth(500);
		pluginDependency.setEnableDnd(true);
		
		pluginDependency.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				final int keyPressed = event.getNativeKeyCode();
				if (keyPressed == KeyCodes.KEY_CTRL) {
					if (!pluginDependency.isCtrlPressed()) {
						pluginDependency.setCtrlPressed(true);
					}
				}
			}

		}, KeyDownEvent.getType());
		pluginDependency.addDomHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				final int keyReleased = event.getNativeKeyCode();
				if (keyReleased == KeyCodes.KEY_CTRL) {
					if (pluginDependency.isCtrlPressed()) {
						pluginDependency.setCtrlPressed(false);
					}
				}
			}

		}, KeyUpEvent.getType());
		
		//Added Style name to remove overflow on x.
		pluginDependency.getFromView().addStyleName("fromViewDependencyList");
		pluginDependency.getToView().addStyleName("toViewDependencyList");
	}

	public String getPluginName() {
		return pluginName.getText();
	}

	public void setPluginName(String pluginName) {
		this.pluginName.setText(pluginName);
	}

	public String getPluginDescription() {
		return pluginDescription.getText();
	}

	public void setPluginDescription(String pluginDescription) {
		this.pluginDescription.setText(pluginDescription);
	}

	public void insertValuesInDependencyListBox(final String value, final int index) {
		dependencyType.insertItem(value, index);
	}

	public void setValuesInDualList(List<PluginDetailsDTO> pluginList) {
		ListStore<PluginDetailsDTO> allPlugins = pluginDependency.getFromStore();
		// allPlugins.
		allPlugins.clear();
		allPlugins.addAll(pluginList);
	}

	public void setSelectedDependencyType(final int index) {
		dependencyType.setSelectedIndex(index);
	}

	public int getSelectedDependencyType() {
		return dependencyType.getSelectedIndex();
	}

	public List<PluginDetailsDTO> getSelectedDependencies() {
		return pluginDependency.getToStore().getAll();
	}

	public void clearSelectedDependencies() {
		pluginDependency.getToStore().clear();
	}

	public void setSelectedDependencies(List<PluginDetailsDTO> selectedDependenciesInDTO) {
		pluginDependency.getToStore().addAll(selectedDependenciesInDTO);

	}

	public void disableDualDependencySelector() {
		pluginDependency.disable();

	}

	public boolean isDualDependencySelectorEnabled() {
		return pluginDependency.isEnabled();
	}

	public void enableDualDependencySelector() {
		pluginDependency.enable();
	}

	// /**
	// * Sets header text of the details panel.
	// *
	// * @param header {@link String}
	// */
	// public void setHeaderText(String header) {
	// headerLabel.setText(header);
	// }
}
