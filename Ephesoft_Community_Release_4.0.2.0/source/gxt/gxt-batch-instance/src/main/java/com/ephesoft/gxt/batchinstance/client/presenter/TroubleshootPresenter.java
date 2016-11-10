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

package com.ephesoft.gxt.batchinstance.client.presenter;

import java.util.List;

import com.ephesoft.gxt.batchinstance.client.controller.BatchInstanceController;
import com.ephesoft.gxt.batchinstance.client.event.BatchInstanceSelectionEvent;
import com.ephesoft.gxt.batchinstance.client.event.LoadTroubleshootArtifactsEvent;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceConstants;
import com.ephesoft.gxt.batchinstance.client.i18n.BatchInstanceMessages;
import com.ephesoft.gxt.batchinstance.client.shared.constants.BatchInfoConstants;
import com.ephesoft.gxt.batchinstance.client.view.TroubleshootView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.TroubleshootDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Hidden;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class TroubleshootPresenter extends BatchInstanceBasePresenter<TroubleshootView> {

	interface CustomEventBinder extends EventBinder<TroubleshootPresenter> {
	}

	public final static CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private static Boolean isUnix = null;

	private VerticalLayoutContainer hiddenFieldsContainer;

	private Hidden selectedRadioButton;

	public TroubleshootPresenter(BatchInstanceController controller, TroubleshootView view) {
		super(controller, view);

		if (null == isUnix) {
			controller.getRpcService().isUnix(new AsyncCallback<Boolean>() {

				@Override
				public void onSuccess(Boolean param) {
					isUnix = param;
				}

				@Override
				public void onFailure(Throwable caught) {
					isUnix = false;
					
				}
			});
		}
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void onBatchInstanceSelection(BatchInstanceSelectionEvent batchInstanceSelectionEvent) {
		if (null != batchInstanceSelectionEvent) {
			BatchInstanceDTO selectedBatchInstance = batchInstanceSelectionEvent.getBatchInstanceDTO();
			if (null != selectedBatchInstance) {
				final String batchInstanceIdentifier = selectedBatchInstance.getBatchIdentifier();
				setBatchInstanceIdentifier(batchInstanceIdentifier);
				view.setDefaultSelection();
				// view.initialize();
			}
		}
	}
	
	@EventHandler
	public void onLoadTroubleShootArtifacts(final LoadTroubleshootArtifactsEvent loadTroubleshootArtifactsEvent) {
		boolean isBatchPresent = loadTroubleshootArtifactsEvent.isBatchPresent();
		if (isBatchPresent) {
			view.loadAllArtifacts();
		} else {
			view.loadGeneralArtifacts();
			setBatchInstanceIdentifier(null);
		}
	}

	private void setBatchInstanceIdentifier(String batchInstanceIdentifier) {
		view.setSelectedBatchInstanceIdentifier(batchInstanceIdentifier);

	}

	public void onDownloadClicked() {
		boolean atLeastOneChecked = view.checkSelectedOptions();

		if (!atLeastOneChecked) {
			// ConfirmationDialogUtil.showConfirmationDialogError(localeDictionary
			// .getMessageValue(BatchInstanceMessages.NO_OPTION_SELECTED_ERROR_MESSAGE));
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.NO_ARTIFACTS_SELECTED_FOR_TROUBLESHOOTING), DialogIcon.ERROR);
		} else {
			hiddenFieldsContainer = new VerticalLayoutContainer();
			addSelectedBatchIDToContainer();
			addSelectedCheckboxToContainer();
			if (null != hiddenFieldsContainer) {
				selectedRadioButton = new Hidden("selectedRadioButton");
				selectedRadioButton.setValue(BatchInfoConstants.DOWNLOAD_RADIO);
				hiddenFieldsContainer.add(selectedRadioButton);
			}
			view.addAndSubmitForm(hiddenFieldsContainer);
		}
	}

	/**
	 * Handler for download button.
	 * 
	 * @param clickEvent {@link ClickEvent}.
	 */
	public void onDownloadToClicked() {
		boolean atLeastOneChecked = view.checkSelectedOptions();

		String downloadToPath = view.getDownloadToValue();
		if (!atLeastOneChecked) {
			// ConfirmationDialogUtil.showConfirmationDialogError(localeDictionary
			// .getMessageValue(BatchInstanceMessages.NO_OPTION_SELECTED_ERROR_MESSAGE));
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.NO_ARTIFACTS_SELECTED_FOR_TROUBLESHOOTING), DialogIcon.ERROR);
		} else if (StringUtil.isNullOrEmpty(downloadToPath)) {
			// ConfirmationDialogUtil.showConfirmationDialogError(localeDictionary
			// .getMessageValue(BatchInstanceMessages.BLANK_PATH_ERROR_MESSAGE));
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.NO_NETWORK_PATH_ENTERED_FOR_TROUBLESHOOTING), DialogIcon.ERROR);
		} else if (!downloadToPath.startsWith(BatchInstanceConstants.DOUBLE_SLASH) && !isUnix) {
			// ConfirmationDialogUtil.showConfirmationDialogError(localeDictionary
			// .getMessageValue(BatchInstanceMessages.PATH_ENTERED_WITHOUT_SLASH_ERROR_MESSAGE));
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.PATH_ENTERED_WITHOUT_SLASH_ERROR_MESSAGE), DialogIcon.ERROR);
		} else {
			hiddenFieldsContainer = new VerticalLayoutContainer();
			addSelectedBatchIDToContainer();
			addSelectedCheckboxToContainer();
			addDowloadToParametersToConainer();
			if (null != hiddenFieldsContainer) {
				selectedRadioButton = new Hidden("selectedRadioButton");
				selectedRadioButton.setValue(BatchInfoConstants.DOWNLOAD_TO_RADIO);
				hiddenFieldsContainer.add(selectedRadioButton);
			}
			view.addAndSubmitForm(hiddenFieldsContainer);
		}
	}

	private void addDowloadToParametersToConainer() {
		if (null != hiddenFieldsContainer) {
			Hidden downloadToPathField = new Hidden(BatchInfoConstants.PATH_TEXT_BOX);
			final String downloadToPath = view.getDownloadToValue();
			if (!StringUtil.isNullOrEmpty(downloadToPath)) {
				downloadToPathField.setValue(downloadToPath);
			}
			hiddenFieldsContainer.add(downloadToPathField);
		}
	}

	private void addSelectedBatchIDToContainer() {
		if (null != hiddenFieldsContainer) {
			final Hidden batchInstanceIdentifierField = new Hidden(BatchInfoConstants.BATCH_INSTANCE_IDENTIFIER);
			final String selectedIdentifier = view.getSelectedBatchInstanceIdentifier();
			batchInstanceIdentifierField.setValue(selectedIdentifier);
			hiddenFieldsContainer.add(batchInstanceIdentifierField);
		}
	}

	public void addSelectedCheckboxToContainer() {
		if (null != hiddenFieldsContainer) {
			List<TroubleshootDTO> selectedCheckboxList = view.getSelectedArtifacts();
			if (!CollectionUtil.isEmpty(selectedCheckboxList)) {
				Hidden selectedCheckbox = null;
				for (TroubleshootDTO checkbox : selectedCheckboxList) {
					selectedCheckbox = new Hidden(checkbox.getName());
					selectedCheckbox.setName(checkbox.getName());
					selectedCheckbox.setValue("true");
					hiddenFieldsContainer.add(selectedCheckbox);
				}
			}
		}
	}

	public void onUploadClicked() {
		boolean atLeastOneChecked = view.checkSelectedOptions();

		if (!atLeastOneChecked) {
			// ConfirmationDialogUtil.showConfirmationDialogError(localeDictionary
			// .getMessageValue(BatchInstanceMessages.NO_OPTION_SELECTED_ERROR_MESSAGE));
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.NO_ARTIFACTS_SELECTED_FOR_TROUBLESHOOTING), DialogIcon.ERROR);
		} else if (StringUtil.isNullOrEmpty(view.getUsernameValue())) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.USERNAME_NOT_DEFINED), DialogIcon.ERROR);
		} else if (StringUtil.isNullOrEmpty(view.getPasswordValue())) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.PASSWORD_NOT_DEFINED), DialogIcon.ERROR);
		} else if (StringUtil.isNullOrEmpty(view.getServerURLValue())) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.SERVER_NOT_DEFINED), DialogIcon.ERROR);
		} else if (StringUtil.isNullOrEmpty(view.getTicketNoValue())) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchInstanceConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchInstanceMessages.TICKET_ID_NOT_DEFINED), DialogIcon.ERROR);
		} else {
			hiddenFieldsContainer = new VerticalLayoutContainer();
			addSelectedBatchIDToContainer();
			addSelectedCheckboxToContainer();
			addUploadParametersToConainer();
			if (null != hiddenFieldsContainer) {
				selectedRadioButton = new Hidden("selectedRadioButton");
				selectedRadioButton.setValue(BatchInfoConstants.UPLOAD_RADIO);
				hiddenFieldsContainer.add(selectedRadioButton);
			}
			view.addAndSubmitForm(hiddenFieldsContainer);
		}
	}

	private void addUploadParametersToConainer() {
		if (null != hiddenFieldsContainer) {
			final String userName = view.getUsernameValue();
			final String password = view.getPasswordValue();
			final String ftpPath = view.getServerURLValue();
			final String ticketID = view.getTicketNoValue();
			if (!StringUtil.isNullOrEmpty(userName)) {
				final Hidden userNameField = new Hidden(BatchInfoConstants.USERNAME_TEXT_BOX);
				userNameField.setValue(userName);
				hiddenFieldsContainer.add(userNameField);
			}
			if (!StringUtil.isNullOrEmpty(password)) {
				final Hidden passwordField = new Hidden(BatchInfoConstants.PASSWORD_TEXT_BOX);
				passwordField.setValue(password);
				hiddenFieldsContainer.add(passwordField);
			}
			if (!StringUtil.isNullOrEmpty(ftpPath)) {
				final Hidden ftpPathField = new Hidden(BatchInfoConstants.FTP_PATH_TEXT_BOX);
				ftpPathField.setValue(ftpPath);
				hiddenFieldsContainer.add(ftpPathField);
			}
			if (!StringUtil.isNullOrEmpty(ticketID)) {
				final Hidden ticketIDField = new Hidden(BatchInfoConstants.TICKET_TEXT_BOX);
				ticketIDField.setValue(ticketID);
				hiddenFieldsContainer.add(ticketIDField);
			}
		}
	}
}
