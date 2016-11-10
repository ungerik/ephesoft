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

package com.ephesoft.gxt.admin.client.view.plugin;

import java.util.Collection;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.plugin.PluginConfigurePresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.MultiSelectDragDropDualList;
import com.ephesoft.gxt.core.shared.dto.BatchClassPluginDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.form.DualListField.DualListFieldAppearance;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class PluginConfigureView extends BatchClassInlineView<PluginConfigurePresenter> {

	interface Binder extends UiBinder<Widget, PluginConfigureView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	@UiField
	Label availablePluginLabel;
	@UiField
	Label selectedPluginLabel;
	@UiField(provided = true)
	MultiSelectDragDropDualList<BatchClassPluginDTO, String> pluginList;

	@UiField
	FieldSet fieldSet;

	public PluginConfigureView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		applyCSSAndText();
		fieldSet.setHeadingText(LocaleDictionary.getConstantValue(BatchClassConstants.PLUGIN_CONFIGURATION));
		fieldSet.setCollapsible(false);
		fieldSet.addStyleName("fieldSet");
		pluginList.getFromView().getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<BatchClassPluginDTO>() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent<BatchClassPluginDTO> event) {
				presenter.onPluginSelect();
			}
		});
	}

	public void populatedualList(Collection<BatchClassPluginDTO> fromList, Collection<BatchClassPluginDTO> toList) {
		setAllPluginDTO(fromList);
		setSelectedPluginDTO(toList);
	}

	@Override
	public void initialize() {
		pluginList = new MultiSelectDragDropDualList<BatchClassPluginDTO, String>(null, PropertyAccessModel.PLUGIN_TYPE,
				new DualListFieldAppearance() {

					@Override
					public IconConfig allLeft() {
						return new IconConfig("allLeft");

					}

					@Override
					public IconConfig allRight() {
						return new IconConfig("allRight");
					}

					@Override
					public IconConfig down() {
						return new IconConfig("down");
					}

					@Override
					public IconConfig left() {
						return new IconConfig("left");
					}

					@Override
					public IconConfig right() {
						return new IconConfig("right");
					}

					@Override
					public IconConfig up() {
						return new IconConfig("up");
					}
				});
		// pluginList = new MultiSelectDragDropDualList<BatchClassPluginDTO, String>(null, PropertyAccessModel.PLUGIN_TYPE);
		pluginList.setWidth(500);
		pluginList.setEnableDnd(true);

	}

	private void setAllPluginDTO(Collection<BatchClassPluginDTO> fromList) {
		ListStore<BatchClassPluginDTO> fromDTOs = pluginList.getFromStore();
		fromDTOs.clear();
		if (null != fromList && fromList.size() != 0) {
			fromDTOs.addAll(fromList);
		}
	}

	public void setSelectedPluginDTO(Collection<BatchClassPluginDTO> toList) {
		ListStore<BatchClassPluginDTO> toDTOs = pluginList.getToStore();
		toDTOs.clear();
		if (null != toList && toList.size() != 0) {
			toDTOs.addAll(toList);
		}
	}

	public void addSelectedPluginDTO(Collection<BatchClassPluginDTO> pluginDTOs) {
		if (null != pluginDTOs && pluginDTOs.size() != 0) {
			pluginList.getToStore().addAll(pluginDTOs);
		}
	}

	public MultiSelectDragDropDualList<BatchClassPluginDTO, String> getPluginList() {
		return pluginList;
	}

	private void applyCSSAndText() {
		availablePluginLabel.setText(LocaleDictionary.getConstantValue(BatchClassConstants.AVAILABLE_PLUGINS));
		availablePluginLabel.addStyleName("availableConfLabel");
		selectedPluginLabel.setText(LocaleDictionary.getConstantValue(BatchClassConstants.SELECTED_PLUGINS));
		selectedPluginLabel.addStyleName("selectedConfLabel");
		pluginList.addStyleName("dualList");
		pluginList.getFromView().addStyleName("fromViewDualList");
		pluginList.getToView().addStyleName("toViewDualList");
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public void fireSelectionEvent() {
		if (null != getPluginList()) {
			if (null != pluginList.getFromView()
					&& !CollectionUtil.isEmpty(pluginList.getFromView().getSelectionModel().getSelection())) {
				getPluginList().getFromView().getSelectionModel().fireEvent(new SelectionChangedEvent<BatchClassPluginDTO>(pluginList.getFromView().getSelectionModel().getSelection()));
			}
		}
	}
}
