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

package com.ephesoft.gxt.admin.client.view.regexValidation;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.NavigationEvent;
import com.ephesoft.gxt.admin.client.presenter.regexValidation.RegexValidationGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.view.regexPool.RegexGroupSelectionGridView;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexBuilderView;
import com.ephesoft.gxt.admin.client.widget.BatchClassManagementGrid;
import com.ephesoft.gxt.admin.client.widget.RegexComboBox;
import com.ephesoft.gxt.admin.client.widget.RegexComboBoxHandler;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.RegexPatternValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.dto.RegexDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.RegexProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class RegexValidationGridView extends BatchClassInlineView<RegexValidationGridPresenter> implements HasResizableGrid {

	interface Binder extends UiBinder<Widget, RegexValidationGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected VerticalPanel gridViewMainPanel;

	@UiField(provided = true)
	protected BatchClassManagementGrid<RegexDTO> regexValidationGrid;

	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	private RegexComboBox patternRegexComboBox;

	@Override
	public void initialize() {

		patternRegexComboBox = new RegexComboBox();

		regexValidationGrid = new BatchClassManagementGrid<RegexDTO>(PropertyAccessModel.REGEX_VALIDATION) {
			@Override
			public void completeEditing(CompleteEditEvent<RegexDTO> completeEditEvent) {
				super.completeEditing(completeEditEvent);
			}
		};
		this.addIndexFieldSelectionHandler(regexValidationGrid.getSelectionModel());
		pagingToolbar = new PagingToolbar(15, regexValidationGrid);

		regexValidationGrid.addEditor(RegexProperties.properties.pattern(), patternRegexComboBox);
		regexValidationGrid.addValidators(RegexProperties.properties.pattern(), new EmptyValueValidator<RegexDTO>());
		regexValidationGrid.addValidators(RegexProperties.properties.pattern(), new RegexPatternValidator<RegexDTO>());
		regexValidationGrid.addValidators(RegexProperties.properties.pattern(), new UniqueValueValidator<RegexDTO, String>());
		regexValidationGrid.addDirtyCellValueProvider(RegexProperties.properties.pattern());
		regexValidationGrid.addSelectAllFunctionality(RegexProperties.properties.selected());
		regexValidationGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<RegexDTO>() {

			@Override
			public void onRecordChange(final StoreRecordChangeEvent<RegexDTO> event) {
				if (event.getProperty() != RegexProperties.properties.selected()) {
					presenter.setBatchClassDirtyOnChange();
				}
				Store<RegexDTO>.Record record = event.getRecord();
				if (event.getProperty() == RegexProperties.properties.pattern() && null != record) {
					String newValue = record.getChange(RegexProperties.properties.pattern()).getValue();
					presenter.setBatchClassDirtyOnChange();
					BatchClassManagementEventBus.fireEvent(new NavigationEvent.NavigationNodeRenameEvent(record.getModel(),
							newValue));
				}
				regexValidationGrid.getStore().commitChanges();
			}
		});
	}

	public void addIndexFieldSelectionHandler(GridSelectionModel<RegexDTO> selectionModel) {
		regexValidationGrid.getStore().commitChanges();
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<RegexDTO> cellSelectionModel = (CellSelectionModel<RegexDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<RegexDTO>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<RegexDTO> event) {
					regexValidationGrid.getStore().commitChanges();
					presenter.selectRegexValidationField(event);
				}
			});
		}
	}

	public RegexValidationGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		regexValidationGrid.addStyleName("gridView");
		PagingLoader<FilterPagingLoadConfig, PagingLoadResult<RegexDTO>> loader = regexValidationGrid.getPaginationLoader();
		pagingToolbar.bind(loader);
	}

	public void reloadGrid() {
		Collection<RegexDTO> regexValidationFieldsCollection = presenter.getCurrentRegexFields();
		regexValidationGrid.setMemoryData(regexValidationFieldsCollection);
		WidgetUtil.reLoadGrid(regexValidationGrid);
	}

	public BatchClassManagementGrid<RegexDTO> getRegexValidationFieldGrid() {
		return regexValidationGrid;
	}

	public List<RegexDTO> getSelectedRegexFields() {
		return regexValidationGrid.getSelectedModels();
	}

	public boolean isValid() {
		return regexValidationGrid.isGridValidated();
	}

	@Override
	public Grid getGrid() {
		return regexValidationGrid;
	}

	@Override
	public void refresh() {
		regexValidationGrid.setMemoryData(presenter.getCurrentRegexFields());
		WidgetUtil.reLoadGrid(regexValidationGrid);
	}

	public void setComponents(final RegexBuilderView regexBuilderView, final RegexGroupSelectionGridView regexGroupSelectionGridView) {
		patternRegexComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		patternRegexComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				patternRegexComboBox.setText(newPattern);
				regexValidationGrid.reLoad();

			}
		});

	}
}
