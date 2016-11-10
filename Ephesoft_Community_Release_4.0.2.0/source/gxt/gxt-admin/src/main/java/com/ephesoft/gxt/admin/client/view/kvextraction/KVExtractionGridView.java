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

package com.ephesoft.gxt.admin.client.view.kvextraction;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.kvextraction.KVExtractionGridPresenter;
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
import com.ephesoft.gxt.core.client.validator.GridRangeValidator;
import com.ephesoft.gxt.core.client.validator.RegexPatternValidator;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.KVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.KVExtractionProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.NumberFormat;
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

public class KVExtractionGridView extends BatchClassInlineView<KVExtractionGridPresenter> implements HasResizableGrid {

	interface Binder extends UiBinder<Widget, KVExtractionGridView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	/** The grid view main panel. */
	@UiField
	protected VerticalPanel gridViewMainPanel;

	/** The KV Extraction configuration grid. */
	@UiField(provided = true)
	protected BatchClassManagementGrid<KVExtractionDTO> kvExtractionGrid;

	/** The paging toolbar. */
	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	private RegexComboBox keyPatternRegexComboBox;

	private RegexComboBox valuePatternRegexComboBox;

	public KVExtractionGridView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		kvExtractionGrid.addStyleName("gridView");
		PagingLoader<FilterPagingLoadConfig, PagingLoadResult<KVExtractionDTO>> loader = kvExtractionGrid.getPaginationLoader();
		pagingToolbar.bind(loader);
		WidgetUtil.setID(kvExtractionGrid, "kvExtractionGrid");
		WidgetUtil.setID(pagingToolbar, "kvExtractionPagingToolbar");
	}

	@Override
	public void initialize() {

		keyPatternRegexComboBox = new RegexComboBox();
		valuePatternRegexComboBox = new RegexComboBox();
		kvExtractionGrid = new BatchClassManagementGrid<KVExtractionDTO>(PropertyAccessModel.KV_EXTRACTION) {

			@Override
			public void completeEditing(CompleteEditEvent<KVExtractionDTO> completeEditEvent) {
				super.completeEditing(completeEditEvent);
				kvExtractionGrid.getStore().commitChanges();
				presenter.setBatchClassDirtyOnChange();
				WidgetUtil.reLoadGrid(kvExtractionGrid);
			}
		};

		kvExtractionGrid.addEditor(KVExtractionProperties.properties.keyPattern(), keyPatternRegexComboBox);
		kvExtractionGrid.addEditor(KVExtractionProperties.properties.valuePattern(), valuePatternRegexComboBox);
		kvExtractionGrid.setIdProvider(KVExtractionProperties.properties.keyPattern());

		kvExtractionGrid.addValidators(KVExtractionProperties.properties.keyPattern(), new EmptyValueValidator<KVExtractionDTO>());
		kvExtractionGrid.addValidators(KVExtractionProperties.properties.keyPattern(), new RegexPatternValidator<KVExtractionDTO>());
		kvExtractionGrid.addValidators(KVExtractionProperties.properties.valuePattern(), new EmptyValueValidator<KVExtractionDTO>());
		kvExtractionGrid.addValidators(KVExtractionProperties.properties.valuePattern(), new RegexPatternValidator<KVExtractionDTO>());
		kvExtractionGrid.addValidators(KVExtractionProperties.properties.weight(), new EmptyValueValidator<KVExtractionDTO>());
		kvExtractionGrid.addValidators(KVExtractionProperties.properties.weight(), new GridRangeValidator<KVExtractionDTO>(
				LocaleDictionary.getMessageValue(BatchClassMessages.WEIGHT_ERROR), Float.class, getRange(BatchClassConstants.MIN_WEIGHT_RANGE),
				getRange(BatchClassConstants.MAX_WEIGHT_RANGE)));
		kvExtractionGrid.addValidators(KVExtractionProperties.properties.noOfWords(), new EmptyValueValidator<KVExtractionDTO>());
		kvExtractionGrid.addSelectAllFunctionality(KVExtractionProperties.properties.selected());
		kvExtractionGrid.addDirtyCellValueProvider(KVExtractionProperties.properties.keyPattern());
		kvExtractionGrid.addDirtyCellValueProvider(KVExtractionProperties.properties.valuePattern());
		kvExtractionGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<KVExtractionDTO>() {

			@Override
			public void onRecordChange(StoreRecordChangeEvent<KVExtractionDTO> event) {
				if (event.getProperty() != KVExtractionProperties.properties.selected()) {
					presenter.setBatchClassDirtyOnChange();
				}
				kvExtractionGrid.getStore().commitChanges();
			}
		});
		this.addKVExtractionSelectionHandler(kvExtractionGrid.getSelectionModel());
		kvExtractionGrid.setFirstRowSelectedOnLoad(true);
		pagingToolbar = new PagingToolbar(15, kvExtractionGrid);
	}

	
	public void addKVExtractionSelectionHandler(GridSelectionModel<KVExtractionDTO> selectionModel) {
		kvExtractionGrid.getStore().commitChanges();
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<KVExtractionDTO> cellSelectionModel = (CellSelectionModel<KVExtractionDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<KVExtractionDTO>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<KVExtractionDTO> event) {
					kvExtractionGrid.getStore().commitChanges();
					presenter.selectKVExtraction(event);
				}
			});
		}
	}
	
	/*
	 * Reload grid with Key Value Extraction dtos.
	 */
	public void reloadGrid() {
		Collection<KVExtractionDTO> kvExtractionList = presenter.getKVFromSelectedDTO();
		kvExtractionGrid.setMemoryData(kvExtractionList);
		WidgetUtil.reLoadGrid(kvExtractionGrid);
		presenter.enableDisableEditButton(kvExtractionGrid.getStore().getAll());
	}

	public void clearSelectionModel() {
		List<KVExtractionDTO> extractionDTOs = getSelectedKVs();
		if (null != kvExtractionGrid && null != kvExtractionGrid.getSelectionModel()) {
			kvExtractionGrid.getSelectionModel().deselectAll();
		}
		if (null != extractionDTOs && extractionDTOs.size() != 0) {
			for (KVExtractionDTO extractionDTO : extractionDTOs) {
				extractionDTO.setSelected(false);
			}
		}
		kvExtractionGrid.getStore().clear();
	}

	public Float getRange(Float val) {
		NumberFormat format = NumberFormat.getFormat(".##");
		Float range = val;
		String str = format.format(range);
		range = Float.valueOf(str);
		return range;
	}

	/**
	 * Gets the selected function key dtos.
	 * 
	 * @return the selected Key Value dtos {@link List<{@link KVExtractionDTO}>}
	 */
	public List<KVExtractionDTO> getSelectedKVs() {
		return kvExtractionGrid.getSelectedModels();
	}

	/**
	 * Gets the paging tool bar.
	 * 
	 * @return the paging tool bar {@link PagingToolbar}
	 */
	public PagingToolbar getPagingToolbar() {
		return pagingToolbar;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid#getGrid()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Grid getGrid() {
		return this.kvExtractionGrid;
	}

	/**
	 * Gets the key Value Extraction grid.
	 * 
	 * @return the key Value Extraction grid
	 */
	public BatchClassManagementGrid<KVExtractionDTO> getKVExtractionGrid() {
		return this.kvExtractionGrid;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isGridValidated() {
		return kvExtractionGrid.isGridValidated();
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public void setComponents(final RegexBuilderView regexBuilderView, final RegexGroupSelectionGridView regexGroupSelectionGridView) {
		keyPatternRegexComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		keyPatternRegexComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				keyPatternRegexComboBox.setText(newPattern);
				kvExtractionGrid.reLoad();

			}
		});
		valuePatternRegexComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		valuePatternRegexComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				valuePatternRegexComboBox.setText(newPattern);
				kvExtractionGrid.reLoad();

			}
		});

	}
}
