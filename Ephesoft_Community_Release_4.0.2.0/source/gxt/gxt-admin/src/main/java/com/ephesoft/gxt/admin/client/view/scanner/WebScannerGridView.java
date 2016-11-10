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

package com.ephesoft.gxt.admin.client.view.scanner;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.scanner.WebScannerGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.widget.BatchClassManagementGrid;
import com.ephesoft.gxt.admin.shared.constant.AdminSharedConstants;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.NumericValueValidator;
import com.ephesoft.gxt.core.client.validator.RegexMatcherValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.WebScannerConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.WebScannerProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

/**
 * This View deals with Web Scanner Profile Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.view.scanner.WebScannerGridView
 */
public class WebScannerGridView extends BatchClassInlineView<WebScannerGridPresenter> implements HasResizableGrid {

	/**
	 * The Interface Binder.
	 */
	interface Binder extends UiBinder<Widget, WebScannerGridView> {
	}

	/** The Constant binder. */
	private static final Binder binder = GWT.create(Binder.class);

	/** The grid view main panel. */
	@UiField
	protected VerticalPanel gridViewMainPanel;

	/** The scanner grid. */
	@UiField(provided = true)
	protected BatchClassManagementGrid<WebScannerConfigurationDTO> scannerGrid;

	/** The paging toolbar. */
	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.view.BatchClassInlineView#initialize()
	 */
	@Override
	public void initialize() {
		scannerGrid = new BatchClassManagementGrid<WebScannerConfigurationDTO>(PropertyAccessModel.WEB_SCANNER) {

			@Override
			public void completeEditing(final CompleteEditEvent<WebScannerConfigurationDTO> completeEditEvent) {
				scannerGrid.getStore().commitChanges();
				presenter.setBatchClassDirtyOnChange();
				refreshBCMGrid(false);
			}
		};
		scannerGrid.setIdProvider(WebScannerProperties.getScannerProperties().getProfileValueProvider());

		scannerGrid.addSelectAllFunctionality(WebScannerProperties.getScannerProperties().selected());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getProfileValueProvider(),
				new EmptyValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getProfileValueProvider(),
				new UniqueValueValidator<WebScannerConfigurationDTO, String>());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getBitDepthValueProvider(),
				new EmptyValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.addValidators(
				WebScannerProperties.getScannerProperties().getBitDepthValueProvider(),
				new RegexMatcherValidator<WebScannerConfigurationDTO>(CoreCommonConstant.BIT_DEPTH_REGEX_PATTERN, LocaleDictionary
						.getMessageValue(BatchClassMessages.INVALID_BIT_DEPTH_MSG)));
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getPageModeValueProvider(),
				new EmptyValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getPageThresholdValueProvider(),
				new EmptyValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getPageThresholdValueProvider(),
				new RegexMatcherValidator<WebScannerConfigurationDTO>(CoreCommonConstant.BLANK_PAGE_THRESHOLD_REGEX_PATTERN,
						LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_BLANK_PAGE_THRESHOLD_MSG)));

		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getPixelValueProvider(),
				new EmptyValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getDpiValueProvider(),
				new EmptyValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getColorValueProvider(),
				new EmptyValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getPaperSizeValueProvider(),
				new EmptyValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getBackPageRotationValueProvider(),
				new EmptyValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getCacheCountValueProvider(),
				new EmptyValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getCacheCountValueProvider(),
				new NumericValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getCacheCountValueProvider(),
				new RegexMatcherValidator<WebScannerConfigurationDTO>(CoreCommonConstant.PAGE_CACHE_CLEAR_COUNT_REGEX_PATTERN,
						LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_PAGE_CACHE_CLEAR_COUNT_MSG)));
		scannerGrid.addValidators(WebScannerProperties.getScannerProperties().getFrontPageRotationValueProvider(),
				new EmptyValueValidator<WebScannerConfigurationDTO>());
		scannerGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<WebScannerConfigurationDTO>() {

			@Override
			public void onRecordChange(final StoreRecordChangeEvent<WebScannerConfigurationDTO> event) {
				if (event.getProperty() != WebScannerProperties.getScannerProperties().selected()) {
					presenter.setBatchClassDirtyOnChange();
				}
				scannerGrid.getStore().commitChanges();
			}
		});
		pagingToolbar = new PagingToolbar(15, scannerGrid);

	}

	/**
	 * Instantiates a new web scanner grid view.
	 */
	public WebScannerGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		scannerGrid.addStyleName("gridView");
		scannerGrid.addStyleName("WebScannergrid");
		final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<WebScannerConfigurationDTO>> loader = scannerGrid
				.getPaginationLoader();
		pagingToolbar.bind(loader);
	}

	/**
	 * load grid with scanner configuration dtos..
	 */
	public void loadGrid() {
		final Collection<WebScannerConfigurationDTO> scannerConfigCollection = presenter.getScannerConfigDTOs();
		scannerGrid.setMemoryData(scannerConfigCollection);
		this.reLoadGrid();
	}

	/**
	 * Gets the selected scanner configurations.
	 * 
	 * @return the selected scanner configurations {@link List< {@link WebScannerConfigurationDTO}>}.
	 */
	public List<WebScannerConfigurationDTO> getSelectedScannerConfigs() {
		return scannerGrid.getSelectedModels();
	}

	/**
	 * Gets the scanner configuration grid.
	 * 
	 * @return the scanner configuration grid {@link BatchClassManagementGrid< {@link WebScannerConfigurationDTO}>}
	 */
	public BatchClassManagementGrid<WebScannerConfigurationDTO> getScannerConfigGrid() {
		return scannerGrid;
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
	 * Adds the and populate cells.
	 * 
	 * @param scannerMasterMap the scanner master map {@link Map<{@link String},{@link List <{@link String}>}>}.
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void addAndPopulateCells(final Map<String, List<String>> scannerMasterMap) {

		final LabelProvider<String> pixelTypeLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox pixelTypeCombo = new SimpleComboBox<String>(pixelTypeLabelProvider);
		pixelTypeCombo.setTriggerAction(TriggerAction.ALL);
		pixelTypeCombo.add(scannerMasterMap.get(AdminSharedConstants.SCANNER_CURRENT_PIXEL_TYPE_KEY));

		scannerGrid.addEditorWidget(WebScannerProperties.getScannerProperties().getPixelValueProvider(), pixelTypeCombo);

		final LabelProvider<String> dpiTypeLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox dpiTypeCombo = new SimpleComboBox<String>(dpiTypeLabelProvider);
		dpiTypeCombo.setTriggerAction(TriggerAction.ALL);
		dpiTypeCombo.add(scannerMasterMap.get(AdminSharedConstants.SCANNER_DPI_KEY));
		scannerGrid.addEditorWidget(WebScannerProperties.getScannerProperties().getDpiValueProvider(), dpiTypeCombo);

		final LabelProvider<String> colorLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox colorCombo = new SimpleComboBox<String>(colorLabelProvider);
		colorCombo.setTriggerAction(TriggerAction.ALL);
		colorCombo.add(scannerMasterMap.get(AdminSharedConstants.SCANNER_COLOR_KEY));
		scannerGrid.addEditorWidget(WebScannerProperties.getScannerProperties().getColorValueProvider(), colorCombo);

		final LabelProvider<String> paperSizeLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox paperSizeCombo = new SimpleComboBox<String>(paperSizeLabelProvider);
		paperSizeCombo.setTriggerAction(TriggerAction.ALL);
		paperSizeCombo.add(scannerMasterMap.get(AdminSharedConstants.SCANNER_PAPER_SIZE_KEY));
		scannerGrid.addEditorWidget(WebScannerProperties.getScannerProperties().getPaperSizeValueProvider(), paperSizeCombo);

		final LabelProvider<String> backPageLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox backPageCombo = new SimpleComboBox<String>(backPageLabelProvider);
		backPageCombo.setTriggerAction(TriggerAction.ALL);
		backPageCombo.add(scannerMasterMap.get(AdminSharedConstants.SCANNER_BACK_PAGE_ROTATION_KEY));
		scannerGrid.addEditorWidget(WebScannerProperties.getScannerProperties().getBackPageRotationValueProvider(), backPageCombo);

		final LabelProvider<String> frontPageLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox frontPageCombo = new SimpleComboBox<String>(frontPageLabelProvider);
		frontPageCombo.setTriggerAction(TriggerAction.ALL);
		frontPageCombo.add(scannerMasterMap.get(AdminSharedConstants.SCANNER_FRONT_PAGE_ROTATION_KEY));
		scannerGrid.addEditorWidget(WebScannerProperties.getScannerProperties().getFrontPageRotationValueProvider(), backPageCombo);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid#getGrid()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Grid getGrid() {
		// TODO Auto-generated method stub
		return scannerGrid;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isGridValidated() {
		return scannerGrid.isGridValidated();
	}

	/**
	 * Commit changes in grid.
	 */
	public void commitChanges() {
		scannerGrid.getStore().commitChanges();
	}

	/**
	 * Re load grid.
	 */
	public void reLoadGrid() {
		WidgetUtil.reLoadGrid(scannerGrid);
	}

	/**
	 * Adds the new item in grid.
	 * 
	 * @param scannerDTO the scanner dto
	 */
	public boolean addNewItemInGrid(final WebScannerConfigurationDTO scannerDTO) {
		return scannerGrid.addNewItemInGrid(scannerDTO);
	}

	/**
	 * Removes the items from grid.
	 * 
	 * @param scannerDTOList the scanner dto list
	 */
	public void removeItemsFromGrid(final List<WebScannerConfigurationDTO> scannerDTOList) {
		scannerGrid.removeItemsFromGrid(scannerDTOList);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
