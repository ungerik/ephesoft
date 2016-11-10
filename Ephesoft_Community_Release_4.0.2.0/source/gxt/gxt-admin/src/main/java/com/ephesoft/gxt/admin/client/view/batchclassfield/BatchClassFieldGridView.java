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

package com.ephesoft.gxt.admin.client.view.batchclassfield;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.presenter.batchclassfield.BatchClassFieldGridPresenter;
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
import com.ephesoft.gxt.core.client.validator.NumericValueValidator;
import com.ephesoft.gxt.core.client.validator.RegexPatternValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.dto.BatchClassFieldDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.BatchClassFieldProperties;
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

/**
 * This View deals with Batch Class Field Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.view.batchclassfield.BatchClassFieldGridView
 */
public class BatchClassFieldGridView extends BatchClassInlineView<BatchClassFieldGridPresenter> implements HasResizableGrid {

	/**
	 * The Interface Binder.
	 */
	interface Binder extends UiBinder<Widget, BatchClassFieldGridView> {
	}

	/** The Constant binder. */
	private static final Binder binder = GWT.create(Binder.class);

	/** The grid view main panel. */
	@UiField
	protected VerticalPanel gridViewMainPanel;

	/** The batch class field grid. */
	@UiField(provided = true)
	protected BatchClassManagementGrid<BatchClassFieldDTO> bcFieldGrid;

	/** The paging toolbar. */
	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	private RegexComboBox validationPatternComboBox;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.view.BatchClassInlineView#initialize()
	 */
	@Override
	public void initialize() {

		validationPatternComboBox = new RegexComboBox();
		bcFieldGrid = new BatchClassManagementGrid<BatchClassFieldDTO>(PropertyAccessModel.BATCH_CLASS_FIELD) {

			@Override
			public void completeEditing(final CompleteEditEvent<BatchClassFieldDTO> completeEditEvent) {
				super.completeEditing(completeEditEvent);
				bcFieldGrid.getStore().commitChanges();
				presenter.setBatchClassDirtyOnChange();
				refreshBCMGrid(false);
			}
		};

		bcFieldGrid.addEditor(BatchClassFieldProperties.properties.validationPattern(), validationPatternComboBox);
		bcFieldGrid.setIdProvider(BatchClassFieldProperties.properties.name());

		bcFieldGrid.addSelectAllFunctionality(BatchClassFieldProperties.properties.selected());
		bcFieldGrid.addValidators(BatchClassFieldProperties.properties.name(), new EmptyValueValidator<BatchClassFieldDTO>());
		bcFieldGrid.addValidators(BatchClassFieldProperties.properties.name(), new UniqueValueValidator<BatchClassFieldDTO, String>());
		bcFieldGrid.addValidators(BatchClassFieldProperties.properties.description(), new EmptyValueValidator<BatchClassFieldDTO>());
		bcFieldGrid.addValidators(BatchClassFieldProperties.properties.dataType(), new EmptyValueValidator<BatchClassFieldDTO>());
		bcFieldGrid.addValidators(BatchClassFieldProperties.properties.fieldOrderNumber(),
				new EmptyValueValidator<BatchClassFieldDTO>());
		bcFieldGrid.addValidators(BatchClassFieldProperties.properties.fieldOrderNumber(),
				new NumericValueValidator<BatchClassFieldDTO>());
		bcFieldGrid.addValidators(BatchClassFieldProperties.properties.fieldOrderNumber(),
				new UniqueValueValidator<BatchClassFieldDTO, Integer>());
		bcFieldGrid.addValidators(BatchClassFieldProperties.properties.validationPattern(),
				new RegexPatternValidator<BatchClassFieldDTO>(false));
		bcFieldGrid.addDirtyCellValueProvider(BatchClassFieldProperties.properties.validationPattern());
		bcFieldGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<BatchClassFieldDTO>() {

			@Override
			public void onRecordChange(final StoreRecordChangeEvent<BatchClassFieldDTO> event) {
				// TODO Auto-generated method stub
				if (event.getProperty() != BatchClassFieldProperties.properties.selected()) {
					presenter.setBatchClassDirtyOnChange();
				}
				bcFieldGrid.getStore().commitChanges();
			}
		});
		pagingToolbar = new PagingToolbar(15, bcFieldGrid);
	}

	/**
	 * Instantiates a new batch class field grid view.
	 */
	public BatchClassFieldGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		bcFieldGrid.addStyleName("gridView");
		final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<BatchClassFieldDTO>> loader = bcFieldGrid.getPaginationLoader();
		pagingToolbar.bind(loader);
	}

	/**
	 * load grid with batch class field dtos.
	 */
	public void loadGrid() {
		final Collection<BatchClassFieldDTO> bcFieldCollection = presenter.getBatchClassFieldDTOs();
		bcFieldGrid.setMemoryData(bcFieldCollection);
		this.reLoadGrid();
	}

	/**
	 * Gets the selected batch class field dtos.
	 * 
	 * @return the selected batch class field dtos {@link List< {@link BatchClassFieldDTO}>}
	 */
	public List<BatchClassFieldDTO> getSelectedBatchClassField() {
		return bcFieldGrid.getSelectedModels();
	}

	/**
	 * Gets the batch class field grid.
	 * 
	 * @return the batch class field grid {@link BatchClassManagementGrid< {@link BatchClassFieldDTO}>}
	 */
	public BatchClassManagementGrid<BatchClassFieldDTO> getBatchClassFieldGrid() {
		return bcFieldGrid;
	}

	/**
	 * Gets the paging tool bar.
	 * 
	 * @return the paging tool bar {@link PagingToolbar}
	 */
	public PagingToolbar getPagingToolbar() {
		return pagingToolbar;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isGridValidated() {
		return bcFieldGrid.isGridValidated();
	}

	/**
	 * Commit changes in grid.
	 */
	public void commitChanges() {
		bcFieldGrid.getStore().commitChanges();
	}

	/**
	 * Re load grid.
	 */
	public void reLoadGrid() {
		WidgetUtil.reLoadGrid(bcFieldGrid);
	}

	/**
	 * Adds the new item in grid.
	 * 
	 * @param fieldDTO the batch class field dto {@link BatchClassFieldDTO}
	 */
	public boolean addNewItemInGrid(final BatchClassFieldDTO fieldDTO) {
		return bcFieldGrid.addNewItemInGrid(fieldDTO);
	}

	/**
	 * Removes the items from grid.
	 * 
	 * @param fieldDTOList the batch class field dto list {@link List< {@link BatchClassFieldDTO}>}
	 */
	public void removeItemsFromGrid(final List<BatchClassFieldDTO> fieldDTOList) {
		bcFieldGrid.removeItemsFromGrid(fieldDTOList);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Grid getGrid() {
		// TODO Auto-generated method stub
		return bcFieldGrid;
	}

	public void setComponents(final RegexBuilderView regexBuilderView, final RegexGroupSelectionGridView regexGroupSelectionGridView) {
		validationPatternComboBox.setComponents(regexBuilderView, regexGroupSelectionGridView);
		validationPatternComboBox.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				currentValueProvider.setValue(currentSelectedObject, newPattern);
				validationPatternComboBox.setText(newPattern);
				bcFieldGrid.reLoad();

			}
		});

	}
}
