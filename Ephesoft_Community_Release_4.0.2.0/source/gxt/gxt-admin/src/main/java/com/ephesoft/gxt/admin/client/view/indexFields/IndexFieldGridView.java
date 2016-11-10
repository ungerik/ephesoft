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

package com.ephesoft.gxt.admin.client.view.indexFields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.NavigationEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.indexFiled.IndexFieldGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.view.regexPool.RegexGroupSelectionGridView;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexBuilderView;
import com.ephesoft.gxt.admin.client.widget.BatchClassManagementGrid;
import com.ephesoft.gxt.admin.client.widget.RegexComboBox;
import com.ephesoft.gxt.admin.client.widget.RegexComboBoxHandler;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.FieldTypeValidator;
import com.ephesoft.gxt.core.client.validator.GridRangeValidator;
import com.ephesoft.gxt.core.client.validator.NumericValueValidator;
import com.ephesoft.gxt.core.client.validator.RegexPatternValidator;
import com.ephesoft.gxt.core.client.validator.StringRangeValidator;
import com.ephesoft.gxt.core.client.validator.UniqueIndexFieldsValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.IndexFieldProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
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

public class IndexFieldGridView extends BatchClassInlineView<IndexFieldGridPresenter> implements HasResizableGrid {

	interface Binder extends UiBinder<Widget, IndexFieldGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected VerticalPanel gridViewMainPanel;

	@UiField(provided = true)
	protected BatchClassManagementGrid<FieldTypeDTO> indexFieldGrid;

	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	private RegexComboBox patternRegexComboBox;

	@Override
	public void initialize() {
		patternRegexComboBox = new RegexComboBox();

		indexFieldGrid = new BatchClassManagementGrid<FieldTypeDTO>(PropertyAccessModel.INDEX_FIELDS) {

			@Override
			public void completeEditing(CompleteEditEvent<FieldTypeDTO> completeEditEvent) {
				super.completeEditing(completeEditEvent);
				indexFieldGrid.getStore().commitChanges();
				presenter.setBatchClassDirtyOnChange();
				refreshBCMGrid(false);
			}
		};

		indexFieldGrid.addEditor(IndexFieldProperties.properties.pattern(), patternRegexComboBox);
		indexFieldGrid.setIdProvider(IndexFieldProperties.properties.name());
		indexFieldGrid.addDirtyCellValueProvider(IndexFieldProperties.properties.pattern());

		this.addIndexFieldSelectionHandler(indexFieldGrid.getSelectionModel());
		pagingToolbar = new PagingToolbar(15, indexFieldGrid);
		indexFieldGrid.addValidators(IndexFieldProperties.properties.name(), new EmptyValueValidator<FieldTypeDTO>());
		indexFieldGrid.addValidators(IndexFieldProperties.properties.description(), new EmptyValueValidator<FieldTypeDTO>());
		indexFieldGrid.addValidators(IndexFieldProperties.properties.pattern(), new RegexPatternValidator<FieldTypeDTO>(false));
		indexFieldGrid.addValidators(IndexFieldProperties.properties.ocrConfidenceThreshold(), new GridRangeValidator<FieldTypeDTO>(
				LocaleDictionary.getMessageValue(BatchClassMessages.OCR_CONFIDENCE_THRESHOLD_SHOULD_BE_BETWEEN_0_AND_100), Float.class, 0.00f, 100.00f));
		indexFieldGrid.addValidators(IndexFieldProperties.properties.fieldOrderNumber(), new EmptyValueValidator<FieldTypeDTO>());
		indexFieldGrid.addValidators(IndexFieldProperties.properties.fieldOrderNumber(), new NumericValueValidator<FieldTypeDTO>());
		indexFieldGrid.addValidators(IndexFieldProperties.properties.fieldOrderNumber(),
				new UniqueValueValidator<FieldTypeDTO, Integer>());
		indexFieldGrid.addValidators(IndexFieldProperties.properties.name(), new UniqueIndexFieldsValidator<FieldTypeDTO>());
		indexFieldGrid.addSelectAllFunctionality(IndexFieldProperties.properties.selected());
		indexFieldGrid.addValidators(IndexFieldProperties.properties.category(), new StringRangeValidator<FieldTypeDTO>(
				LocaleDictionary.getMessageValue(BatchClassMessages.CATEGORY_VALUE_SHOULD_NOT_BE_GREATER_THAN_256_CHARACTERS), 0, 256,true));
		indexFieldGrid.addValidators(IndexFieldProperties.properties.widgetType(), new FieldTypeValidator<FieldTypeDTO>());
		indexFieldGrid.addNonEditableValueProviders(IndexFieldProperties.properties.name());

		indexFieldGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<FieldTypeDTO>() {

			@Override
			public void onRecordChange(final StoreRecordChangeEvent<FieldTypeDTO> event) {
				ValueProvider<? super FieldTypeDTO, ?> property = event.getProperty();
				if (null != property) {
					if (property != IndexFieldProperties.properties.selected()) {
						presenter.setBatchClassDirtyOnChange();
					}
					if (property == IndexFieldProperties.properties.name()) {
						String newValue = event.getRecord().getChange(IndexFieldProperties.properties.name()).getValue();
						presenter.setBatchClassDirtyOnChange();
						BatchClassManagementEventBus.fireEvent(new NavigationEvent.NavigationNodeRenameEvent(event.getRecord()
								.getModel(), newValue));
					}
					indexFieldGrid.getStore().commitChanges();
				}
			}
		});

		List<ValueProvider<FieldTypeDTO, ?>> valueProvidersList = new ArrayList<ValueProvider<FieldTypeDTO, ?>>();
		valueProvidersList.add(IndexFieldProperties.properties.name());
		indexFieldGrid.addNonEditableValueProviders(valueProvidersList);

	}

	public void addIndexFieldSelectionHandler(GridSelectionModel<FieldTypeDTO> selectionModel) {
		indexFieldGrid.getStore().commitChanges();
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<FieldTypeDTO> cellSelectionModel = (CellSelectionModel<FieldTypeDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<FieldTypeDTO>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<FieldTypeDTO> event) {
					indexFieldGrid.getStore().commitChanges();
					presenter.selectIndexField(event);
				}
			});
		}
	}

	public IndexFieldGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		indexFieldGrid.addStyleName("gridView");
		PagingLoader<FilterPagingLoadConfig, PagingLoadResult<FieldTypeDTO>> loader = indexFieldGrid.getPaginationLoader();
		pagingToolbar.bind(loader);
	}

	public void reloadGrid() {
		Collection<FieldTypeDTO> indexFieldCollection = presenter.getCurrentFieldTypes();
		indexFieldGrid.setMemoryData(indexFieldCollection);
		WidgetUtil.reLoadGrid(indexFieldGrid);
		if (getSizeOfGrid() < 1) {
			presenter.toggleExportButton(false);
		}
	}

	/**
	 * @return the indexFieldGrid
	 */
	public BatchClassManagementGrid<FieldTypeDTO> getIndexFieldGrid() {
		return indexFieldGrid;
	}

	public List<FieldTypeDTO> getSelectedIndexFields() {
		return indexFieldGrid.getSelectedModels();
	}

	public boolean isValid() {
		return indexFieldGrid.isGridValidated();
	}

	@Override
	public Grid getGrid() {
		return indexFieldGrid;
	}

	@Override
	public void refresh() {
		indexFieldGrid.setMemoryData(presenter.getCurrentFieldTypes());
		WidgetUtil.reLoadGrid(indexFieldGrid);
		if (getSizeOfGrid() < 1) {
			presenter.toggleExportButton(false);
		}
	}

	/**
	 * Adds the new item in grid.
	 * 
	 * @param fieldTypeDTO the field type dto {@link FieldTypeDTO}
	 */
	public boolean addNewItemInGrid(final FieldTypeDTO fieldTypeDTO) {
		return indexFieldGrid.addNewItemInGrid(fieldTypeDTO);
	}

	public void setComponents(final RegexBuilderView regexBuilderView, final RegexGroupSelectionGridView regexGroupSelectionGridView) {
		patternRegexComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		patternRegexComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				patternRegexComboBox.setText(newPattern);
				indexFieldGrid.reLoad();

			}
		});

	}

	public int getSizeOfGrid() {

		return indexFieldGrid.getStore().getAll().size();

	}

	public void deSelectAllModels() {
		indexFieldGrid.deSelectModels();
	}
}
