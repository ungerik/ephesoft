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

package com.ephesoft.gxt.admin.client.view.document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.GetLearnFileInformationEvent;
import com.ephesoft.gxt.admin.client.event.NavigationEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.document.DocumentTypeGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.widget.BatchClassManagementGrid;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.GridRangeValidator;
import com.ephesoft.gxt.core.client.validator.UniqueDocumentTypeNameValidator;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.DocumentTypeProperties;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.Store.Change;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRecordChangeEvent.StoreRecordChangeHandler;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class DocumentTypeGridView extends BatchClassInlineView<DocumentTypeGridPresenter> implements HasResizableGrid {

	interface Binder extends UiBinder<Widget, DocumentTypeGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected VerticalPanel gridViewMainPanel;

	@UiField(provided = true)
	protected BatchClassManagementGrid<DocumentTypeDTO> documentTypeGrid;

	@UiField(provided = true)
	protected PagingToolbar pagingToolbar;

	@Override
	public void initialize() {
		documentTypeGrid = new BatchClassManagementGrid<DocumentTypeDTO>(PropertyAccessModel.DOCUMENT_TYPE) {

			@Override
			public void completeEditing(CompleteEditEvent<DocumentTypeDTO> completeEditEvent) {
				documentTypeGrid.getStore().commitChanges();
				presenter.setBatchClassDirtyOnChange();
				refreshBCMGrid(false);
				super.completeEditing(completeEditEvent);
			}
		};
		documentTypeGrid.setIdProvider(DocumentTypeProperties.INSTANCE.name());
		// removing empty validator as it is already handled by Unique Value Validator.
		// documentTypeGrid.addValidators(DocumentTypeProperties.INSTANCE.name(), new EmptyValueValidator<DocumentTypeDTO>());
		documentTypeGrid.addValidators(DocumentTypeProperties.INSTANCE.description(), new EmptyValueValidator<DocumentTypeDTO>());
		documentTypeGrid.addValidators(DocumentTypeProperties.INSTANCE.minConfidenceThreshold(),
				new GridRangeValidator<DocumentTypeDTO>(LocaleDictionary.getMessageValue(BatchClassMessages.MINIMUM_CONFIDENCE_THRESHOLD_SHOULD_BE_BETWEEN_0_AND_100), Float.class,
						0.00f, 100.00f));
		documentTypeGrid.addValidators(DocumentTypeProperties.INSTANCE.name(),
				new UniqueDocumentTypeNameValidator<DocumentTypeDTO, String>());
		documentTypeGrid.addSelectAllFunctionality(DocumentTypeProperties.INSTANCE.selected());

		documentTypeGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<DocumentTypeDTO>() {

			@Override
			public void onRecordChange(final StoreRecordChangeEvent<DocumentTypeDTO> event) {
				ValueProvider<? super DocumentTypeDTO, ?> property = event.getProperty();
				if (null != property) {
					if (property != DocumentTypeProperties.INSTANCE.selected()) {
						presenter.setBatchClassDirtyOnChange();
					}
					if (property == DocumentTypeProperties.INSTANCE.name()) {
						Change<DocumentTypeDTO, String> change = event.getRecord().getChange(DocumentTypeProperties.INSTANCE.name());
						if (null != change) {
							String newValue = change.getValue();
							presenter.setBatchClassDirtyOnChange();
							BatchClassManagementEventBus.fireEvent(new NavigationEvent.NavigationNodeRenameEvent(event.getRecord()
									.getModel(), newValue));
						}
					}
				}
				documentTypeGrid.getStore().commitChanges();
			}
		});
		List<ValueProvider<DocumentTypeDTO, ?>> valueProvidersList = new ArrayList<ValueProvider<DocumentTypeDTO, ?>>();
		valueProvidersList.add(DocumentTypeProperties.INSTANCE.name());
		documentTypeGrid.addNonEditableValueProviders(valueProvidersList);
		pagingToolbar = new PagingToolbar(15, documentTypeGrid);
		this.addDocumentTypeSelectionHandler(documentTypeGrid.getSelectionModel());
	}

	public DocumentTypeGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		documentTypeGrid.addStyleName("gridView");
		PagingLoader<FilterPagingLoadConfig, PagingLoadResult<DocumentTypeDTO>> loader = documentTypeGrid.getPaginationLoader();
		pagingToolbar.bind(loader);
	}

	public void reloadGrid() {
		Collection<DocumentTypeDTO> documentTypeCollection = presenter.getCurrentDocumentTypes();
		documentTypeGrid.setMemoryData(documentTypeCollection);
		WidgetUtil.reLoadGrid(documentTypeGrid);
	}

	@Override
	public void refresh() {
		documentTypeGrid.setMemoryData(presenter.getCurrentDocumentTypes());
		WidgetUtil.reLoadGrid(documentTypeGrid);
	}

	public BatchClassManagementGrid<DocumentTypeDTO> getDocumentTypeGrid() {
		return documentTypeGrid;
	}

	public List<DocumentTypeDTO> getSelectedDocumentTypes() {
		return documentTypeGrid.getSelectedModels();
	}

	public void addDocumentTypeSelectionHandler(GridSelectionModel<DocumentTypeDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<DocumentTypeDTO> cellSelectionModel = (CellSelectionModel<DocumentTypeDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<DocumentTypeDTO>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<DocumentTypeDTO> cellSelectionEvent) {
					presenter.setSelectedDocumentType(cellSelectionEvent);
					if (presenter.isDocumentTypeChanged())
						BatchClassManagementEventBus.fireEvent(new GetLearnFileInformationEvent());
				}
			});
		}
	}

	@Override
	public Grid getGrid() {
		return documentTypeGrid;
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public void setProjectFiles(List<String> rspFileNameList) {
		if (CollectionUtil.isEmpty(rspFileNameList)) {
			rspFileNameList = new ArrayList<String>();
		}
		final LabelProvider<String> firstPageLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox rspFirstPageCombo = new SimpleComboBox<String>(firstPageLabelProvider);
		rspFirstPageCombo.setTriggerAction(TriggerAction.ALL);
		rspFirstPageCombo.add(rspFileNameList);

		documentTypeGrid.addEditorWidget(DocumentTypeProperties.INSTANCE.firstPageProjectFileName(), rspFirstPageCombo);

		final LabelProvider<String> secondPageLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox rspSecondPageCombo = new SimpleComboBox<String>(secondPageLabelProvider);
		rspSecondPageCombo.setTriggerAction(TriggerAction.ALL);
		rspSecondPageCombo.add(rspFileNameList);

		documentTypeGrid.addEditorWidget(DocumentTypeProperties.INSTANCE.secondPageProjectFileName(), rspSecondPageCombo);

		final LabelProvider<String> thirdPageLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox rspThirdPageCombo = new SimpleComboBox<String>(thirdPageLabelProvider);
		rspThirdPageCombo.setTriggerAction(TriggerAction.ALL);
		rspThirdPageCombo.add(rspFileNameList);

		documentTypeGrid.addEditorWidget(DocumentTypeProperties.INSTANCE.thirdPageProjectFileName(), rspThirdPageCombo);

		final LabelProvider<String> fourthPageLabelProvider = new LabelProvider<String>() {

			@Override
			public String getLabel(final String item) {
				return item;
			}
		};

		final SimpleComboBox rspFourthPageCombo = new SimpleComboBox<String>(fourthPageLabelProvider);
		rspFourthPageCombo.setTriggerAction(TriggerAction.ALL);
		rspFourthPageCombo.add(rspFileNameList);

		documentTypeGrid.addEditorWidget(DocumentTypeProperties.INSTANCE.fourthPageProjectFileName(), rspFourthPageCombo);
	}

	public boolean isValid() {
		return documentTypeGrid.isGridValidated();
	}

	/**
	 * Adds the new item in grid.
	 * 
	 * @param docTypeDTO the document type dto {@link DocumentTypeDTO}
	 */
	public boolean addNewItemInGrid(final DocumentTypeDTO docTypeDTO) {
		return documentTypeGrid.addNewItemInGrid(docTypeDTO);
	}

	public void deSelectAllModels() {
		documentTypeGrid.deSelectModels();
	}
}
