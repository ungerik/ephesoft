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

package com.ephesoft.gxt.admin.client.view.email;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.email.EmailGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.widget.BatchClassManagementGrid;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.PagingToolbar;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.PortNumberValidator;
import com.ephesoft.gxt.core.client.validator.RegexMatcherValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.EmailConfigurationDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.EmailImportConfigurationProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
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

/**
 * This View deals with Email Import Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.view.email.EmailGridView
 */
public class EmailGridView extends BatchClassInlineView<EmailGridPresenter> implements HasResizableGrid {

	/**
	 * The Interface Binder.
	 */
	interface Binder extends UiBinder<Widget, EmailGridView> {
	}

	/** The Constant binder. */
	private static final Binder binder = GWT.create(Binder.class);

	/** The grid view main panel. */
	@UiField
	protected VerticalPanel gridViewMainPanel;

	/** The email import grid. */
	@UiField(provided = true)
	protected BatchClassManagementGrid<EmailConfigurationDTO> emailGrid;

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
		emailGrid = new BatchClassManagementGrid<EmailConfigurationDTO>(PropertyAccessModel.EMAIL_LIST) {

			@Override
			public void completeEditing(final CompleteEditEvent<EmailConfigurationDTO> completeEditEvent) {
				EmailGridView.this.commitChanges();
				presenter.setBatchClassDirtyOnChange();
				refreshBCMGrid(false);
			}
		};
		emailGrid.setIdProvider(EmailImportConfigurationProperties.getEmailProperties().getUserName());

		emailGrid.addSelectAllFunctionality(EmailImportConfigurationProperties.getEmailProperties().getSelected());
		emailGrid.addValidators(EmailImportConfigurationProperties.getEmailProperties().getUserName(),
				new EmptyValueValidator<EmailConfigurationDTO>());
		emailGrid.addValidators(
				EmailImportConfigurationProperties.getEmailProperties().getUserName(),
				new RegexMatcherValidator<EmailConfigurationDTO>(CoreCommonConstant.EMAIL_REGEX_PATTERN, LocaleDictionary
						.getMessageValue(BatchClassMessages.INVALID_EMAIL_MSG)));
		emailGrid.addValidators(EmailImportConfigurationProperties.getEmailProperties().getUserName(),
				new UniqueValueValidator<EmailConfigurationDTO, String>());
		emailGrid.addValidators(EmailImportConfigurationProperties.getEmailProperties().getPassword(),
				new EmptyValueValidator<EmailConfigurationDTO>());
		emailGrid.addValidators(EmailImportConfigurationProperties.getEmailProperties().getServerName(),
				new EmptyValueValidator<EmailConfigurationDTO>());
		emailGrid.addValidators(EmailImportConfigurationProperties.getEmailProperties().getServerType(),
				new EmptyValueValidator<EmailConfigurationDTO>());
		emailGrid.addValidators(EmailImportConfigurationProperties.getEmailProperties().getPortNumber(),
				new PortNumberValidator<EmailConfigurationDTO, Integer>());

		emailGrid.getStore().addStoreRecordChangeHandler(new StoreRecordChangeHandler<EmailConfigurationDTO>() {

			@Override
			public void onRecordChange(final StoreRecordChangeEvent<EmailConfigurationDTO> event) {
				if (event.getProperty() != EmailImportConfigurationProperties.getEmailProperties().getSelected()) {
					presenter.setBatchClassDirtyOnChange();
				}
				EmailGridView.this.commitChanges();
			}
		});
		pagingToolbar = new PagingToolbar(15, emailGrid);
	}

	/**
	 * Instantiates a new email grid view.
	 */
	public EmailGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		emailGrid.addStyleName("gridView");
		this.addTableInfoSelectionHandler(emailGrid.getSelectionModel());
		final PagingLoader<FilterPagingLoadConfig, PagingLoadResult<EmailConfigurationDTO>> loader = emailGrid.getPaginationLoader();
		pagingToolbar.bind(loader);
	}

	/**
	 * Adds the email configuration selection handler.
	 * 
	 * @param selectionModel the selection model {@link GridSelectionModel< {@link EmailConfigurationDTO}>}
	 */
	public void addTableInfoSelectionHandler(final GridSelectionModel<EmailConfigurationDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			final CellSelectionModel<EmailConfigurationDTO> cellSelectionModel = (CellSelectionModel<EmailConfigurationDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<EmailConfigurationDTO>() {

				@Override
				public void onCellSelectionChanged(final CellSelectionChangedEvent<EmailConfigurationDTO> cellSelectionEvent) {
					presenter.setSelectedEmail(cellSelectionEvent);
				}
			});
		}
	}

	/**
	 * load grid with email configuration dtos.
	 */
	public void loadGrid() {
		final Collection<EmailConfigurationDTO> documentTypeCollection = presenter.getCurrentEmails();
		emailGrid.setMemoryData(documentTypeCollection);
		this.reLoadGrid();
	}

	/**
	 * Gets the selected email configurations.
	 * 
	 * @return the selected email configurations {@link List< {@link EmailConfigurationDTO}>}
	 */
	public List<EmailConfigurationDTO> getSelectedEmails() {
		return emailGrid.getSelectedModels();
	}

	/**
	 * Gets the email import grid.
	 * 
	 * @return the email import grid {@link BatchClassManagementGrid< {@link EmailConfigurationDTO}>}
	 */
	public BatchClassManagementGrid<EmailConfigurationDTO> getEmailGrid() {
		return emailGrid;
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
		// TODO Auto-generated method stub
		return emailGrid;
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isGridValidated() {
		return emailGrid.isGridValidated();
	}

	/**
	 * Commit changes in grid.
	 */
	public void commitChanges() {
		emailGrid.getStore().commitChanges();
	}

	/**
	 * Re load grid.
	 */
	public void reLoadGrid() {
		WidgetUtil.reLoadGrid(emailGrid);
	}

	/**
	 * Adds the new item in grid.
	 * 
	 * @param emailConfigDTO the email configuration dto {@link EmailConfigurationDTO}
	 */
	public boolean addNewItemInGrid(final EmailConfigurationDTO emailConfigDTO) {
		return emailGrid.addNewItemInGrid(emailConfigDTO);
	}

	/**
	 * Removes the items from grid.
	 * 
	 * @param emailConfDTOList the email configuration dto list {@link List< {@link EmailConfigurationDTO}>}
	 */
	public void removeItemsFromGrid(final List<EmailConfigurationDTO> emailConfDTOList) {
		emailGrid.removeItemsFromGrid(emailConfDTOList);
	}

	/**
	 * Gets the total record count.
	 * 
	 * @return int the total record count
	 */
	public int getTotalRecordCount() {
		return emailGrid.getPaginationLoader().getTotalCount();
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
}
