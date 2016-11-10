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

package com.ephesoft.gxt.admin.client.presenter.scanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AddScannerConfigEvent;
import com.ephesoft.gxt.admin.client.event.DeleteScannerConfigEvent;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.scanner.WebScannerGridView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.ScannerMasterDTO;
import com.ephesoft.gxt.core.shared.dto.WebScannerConfigurationDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

/**
 * This presenter deals with Web Scanner Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.presenter.scanner.WebScannerGridPresenter
 */
public class WebScannerGridPresenter extends BatchClassInlinePresenter<WebScannerGridView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<WebScannerGridPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Instantiates a new web scanner grid presenter.
	 * 
	 * @param controller the controller
	 * @param view the view
	 */
	public WebScannerGridPresenter(final BatchClassManagementController controller, final WebScannerGridView view) {
		super(controller, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.event.BindHandler#bind()
	 */
	@Override
	public void bind() {
		if (null != controller.getSelectedBatchClass()) {
			rpcService.getScannerMasterConfigs(new AsyncCallback<Map<String, List<String>>>() {

				@Override
				public void onFailure(final Throwable caught) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSuccess(final Map<String, List<String>> result) {
					// TODO Auto-generated method stub
					view.addAndPopulateCells(result);
					view.loadGrid();
				}

			});
		}
	}

	/**
	 * Method to inject events in event bus.
	 * 
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	/**
	 * Method to get the scanner configuration dtos.
	 * 
	 * @return the scanner configuration dtos {@link Collection<{@link WebScannerConfigurationDTO}>}
	 */
	public Collection<WebScannerConfigurationDTO> getScannerConfigDTOs() {
		final BatchClassDTO selectedBatchClass = controller.getSelectedBatchClass();
		Collection<WebScannerConfigurationDTO> scannerConfigCollection = null;
		if (selectedBatchClass != null) {
			scannerConfigCollection = selectedBatchClass.getScannerConfiguration();
		}
		return scannerConfigCollection;
	}

	/**
	 * Handle multiple scanner configuration deletion.
	 * 
	 * @param deleteEvent {@link DeleteScannerConfigEvent}
	 */
	@EventHandler
	public void handleScannerConfigsDeletion(final DeleteScannerConfigEvent deleteEvent) {
		if (null != deleteEvent) {
			final List<WebScannerConfigurationDTO> scannerConfigs = view.getSelectedScannerConfigs();
			if (!CollectionUtil.isEmpty(scannerConfigs)) {

				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_SCANNNER_CONFIGS_MSG), false,
						DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
						deleteScannerConfigs(scannerConfigs);
					}
				});
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.NO_ROW_SELECTED), DialogIcon.WARNING);
			}
		}
	}

	/**
	 * Delete scanner configuration.
	 * 
	 * @param scannerConfigList the scanner configuration dtos list {@link List<{@link WebScannerConfigurationDTO}>}
	 */
	private void deleteScannerConfigs(final List<WebScannerConfigurationDTO> scannerConfigList) {

		final BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
		for (final WebScannerConfigurationDTO scannerConfig : scannerConfigList) {
			if (null != scannerConfig) {
				batchClassDTO.getWebScannerConfigurationById(scannerConfig.getIdentifier()).setDeleted(true);
			}
		}
		controller.setBatchClassDirtyFlg(true);
		view.removeItemsFromGrid(scannerConfigList);
		view.commitChanges();
		view.reLoadGrid();
		Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS),
				LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_SCANNER_PROFILES_SUCCESS_MSG));
	}

	/**
	 * Handle scanner configuration addition.
	 * 
	 * @param addEvent {@link AddScannerConfigEvent}
	 */
	@EventHandler
	public void handleScannerConfigAddition(final AddScannerConfigEvent addEvent) {
		view.commitChanges();
		if (null != addEvent) {
			final WebScannerConfigurationDTO sacnnerDTO = createScannerConfigurationDTOObject();
			if (view.addNewItemInGrid(sacnnerDTO)) {
				controller.getSelectedBatchClass().addScannerConfiguration(sacnnerDTO);
				controller.setBatchClassDirtyFlg(true);
			}
		}
	}

	/**
	 * Creates the scanner configuration dto object.
	 * 
	 * @return the web scanner configuration dto {@link WebScannerConfigurationDTO}
	 */
	private WebScannerConfigurationDTO createScannerConfigurationDTOObject() {
		final WebScannerConfigurationDTO sConfigurationDTO = new WebScannerConfigurationDTO();
		sConfigurationDTO.setNew(true);
		sConfigurationDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		// create the parent DTO
		sConfigurationDTO.setParent(null);

		// set the children

		final Map<String, ScannerMasterDTO> masterConfig = controller.getSelectedBatchClass().getScannerMasterMap();
		final Collection<WebScannerConfigurationDTO> children = new ArrayList<WebScannerConfigurationDTO>();
		for (final ScannerMasterDTO dto : masterConfig.values()) {
			if (dto.getName().equalsIgnoreCase(AdminConstants.WEB_SCANNER_PROFILE_TEXT_CONST)) {
				// set as parent
				sConfigurationDTO.setName(dto.getName());
				sConfigurationDTO.setDescription(dto.getDescription());
				sConfigurationDTO.setMandatory(dto.isMandatory());
				sConfigurationDTO.setMultiValue(dto.isMultiValue());
				sConfigurationDTO.setDataType(dto.getType());
				sConfigurationDTO.setBatchClass(controller.getSelectedBatchClass());

			} else {
				// set as child node
				final WebScannerConfigurationDTO sChildDTO = new WebScannerConfigurationDTO();
				sChildDTO.setName(dto.getName());
				sChildDTO.setDescription(dto.getDescription());
				if (dto.getSampleValue() != null && !dto.getSampleValue().isEmpty()) {
					sChildDTO.setValue(dto.getSampleValue().get(0));
				}
				sChildDTO.setMandatory(dto.isMandatory());
				sChildDTO.setMultiValue(dto.isMultiValue());
				sChildDTO.setSampleValue(dto.getSampleValue());
				sChildDTO.setDataType(dto.getType());
				sChildDTO.setBatchClass(controller.getSelectedBatchClass());
				sChildDTO.setParent(sConfigurationDTO);
				children.add(sChildDTO);
			}
		}
		sConfigurationDTO.setChildren(children);
		sConfigurationDTO.setBatchClass(controller.getSelectedBatchClass());
		return sConfigurationDTO;
	}

	/**
	 * Sets the batch class dirty on change.
	 * 
	 */
	public void setBatchClassDirtyOnChange() {
		controller.setBatchClassDirtyFlg(true);
	}

	/**
	 * Checks if is grid validated.
	 * 
	 * @return true, if is grid validated
	 */
	public boolean isValid() {
		return view.isGridValidated();
	}
}
