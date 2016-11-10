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

package com.ephesoft.gxt.admin.client.view.module;

import java.util.Collection;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.module.ModuleConfigurePresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.MultiSelectDragDropDualList;
import com.ephesoft.gxt.core.shared.dto.BatchClassModuleDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.DualListField.DualListFieldAppearance;

public class ModuleConfigureView extends BatchClassInlineView<ModuleConfigurePresenter> {

	interface Binder extends UiBinder<Widget, ModuleConfigureView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);
	@UiField
	FieldSet fieldSet;
	@UiField
	Label availableModuleLabel;
	@UiField
	Label selectedModuleLabel;
	@UiField(provided = true)
	MultiSelectDragDropDualList<BatchClassModuleDTO, String> moduleList;

	public ModuleConfigureView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		applyCSSAndText();
	}

	private void applyCSSAndText() {
		fieldSet.setHeadingText(LocaleDictionary.getConstantValue(BatchClassConstants.MODULES_CONFIGURATION));
		fieldSet.setCollapsible(false);
		fieldSet.addStyleName("fieldSet");
		availableModuleLabel.setText(LocaleDictionary.getConstantValue(BatchClassConstants.AVAILABLE_MODULES));
		availableModuleLabel.addStyleName("availableConfLabel");
		selectedModuleLabel.setText(LocaleDictionary.getConstantValue(BatchClassConstants.SELECTED_MODULES));
		selectedModuleLabel.addStyleName("selectedConfLabel");
		moduleList.addStyleName("dualList");
		moduleList.getFromView().addStyleName("fromViewDualList");
		moduleList.getToView().addStyleName("toViewDualList");
	}

	public void populateDualList(Collection<BatchClassModuleDTO> fromList, Collection<BatchClassModuleDTO> toList) {
		setAllModuleDTO(fromList);
		setSelectedModuleDTO(toList);
	}

	@Override
	public void initialize() {
		moduleList = new MultiSelectDragDropDualList<BatchClassModuleDTO, String>(null, PropertyAccessModel.MODULE_TYPE, new DualListFieldAppearance() {

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
		moduleList.setWidth(500);
		moduleList.setEnableDnd(true);
		moduleList.getToStore().applySort(true);
				
	}

	public void setAllModuleDTO(Collection<BatchClassModuleDTO> fromList) {
		ListStore<BatchClassModuleDTO> fromDTOs = moduleList.getFromStore();
		fromDTOs.clear();
		fromDTOs.addAll(fromList);
	}

	public void setSelectedModuleDTO(Collection<BatchClassModuleDTO> toList) {
		ListStore<BatchClassModuleDTO> toDTOs = moduleList.getToStore();
		toDTOs.clear();
		toDTOs.addAll(toList);
	}

	
	public MultiSelectDragDropDualList<BatchClassModuleDTO, String> getModuleList() {
		return moduleList;
	}
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
	
}
