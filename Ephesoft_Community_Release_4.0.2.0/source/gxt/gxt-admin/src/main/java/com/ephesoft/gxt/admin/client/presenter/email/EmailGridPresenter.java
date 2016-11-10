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

package com.ephesoft.gxt.admin.client.presenter.email;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AddEmailEvent;
import com.ephesoft.gxt.admin.client.event.DeleteMultipleEmailsEvent;
import com.ephesoft.gxt.admin.client.event.TestEmailEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.email.EmailGridView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.BatchConstants;
import com.ephesoft.gxt.core.shared.dto.EmailConfigurationDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

/**
 * This presenter deals with Email Import Grid.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.presenter.email.EmailGridPresenter
 */
public class EmailGridPresenter extends BatchClassInlinePresenter<EmailGridView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<EmailGridPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Instantiates a new email grid presenter.
	 * 
	 * @param controller the controller {@link BatchClassManagementController}
	 * @param view the view {@link EmailGridView}
	 */
	public EmailGridPresenter(final BatchClassManagementController controller, final EmailGridView view) {
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
			controller.setCurrentEmailConfigDTO(null);
			view.loadGrid();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	/**
	 * Method to get the email configuration dtos.
	 * 
	 * @return the email configuration dtos {@link Collection<{@link EmailConfigurationDTO}>}
	 */
	public Collection<EmailConfigurationDTO> getCurrentEmails() {
		final BatchClassDTO selectedBatchClass = controller.getSelectedBatchClass();
		Collection<EmailConfigurationDTO> emailCollection = null;
		if (selectedBatchClass != null) {
			emailCollection = selectedBatchClass.getEmailConfiguration(false);
		}
		return emailCollection;
	}
	
	/**
	 * Sets the selected email configuration dto in controller.
	 * 
	 * @param cellSelectionChangeEvent the new select table info {@link CellSelectionChangedEvent<{@link EmailConfigurationDTO}>}
	 */
	public void setSelectedEmail(final CellSelectionChangedEvent<EmailConfigurationDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			final EmailConfigurationDTO selectedEmail = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			if (selectedEmail != null) {
				controller.setCurrentEmailConfigDTO(selectedEmail);
			}
		}
	}

	/**
	 * Handle multiple email configurations deletion.
	 * 
	 * @param deleteEvent the delete event {@link DeleteMultipleEmailsEvent}
	 */
	@EventHandler
	public void handleEmailsDeletion(final DeleteMultipleEmailsEvent deleteEvent) {
		if (null != deleteEvent) {
			final List<EmailConfigurationDTO> selectedEmails = view.getSelectedEmails();
			if (!CollectionUtil.isEmpty(selectedEmails)) {
				final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
						LocaleDictionary.getConstantValue(BatchClassConstants.CONFIRMATION),
						LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_THE_SELECTED_EMAILS_MSG), false,
						DialogIcon.QUESTION_MARK);
				confirmationDialog.setDialogListener(new DialogAdapter() {

					@Override
					public void onOkClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
						deleteSelectedEmails(selectedEmails);
					}

					@Override
					public void onCancelClick() {
						// TODO Auto-generated method stub
						confirmationDialog.hide();
					}
				});
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.NO_ROW_SELECTED), DialogIcon.WARNING);
			}
		}
	}

	/**
	 * Delete selected email configurations.
	 * 
	 * @param selectedEmails the selected email configurations {@link List<{@link EmailConfigurationDTO}>}
	 */
	private void deleteSelectedEmails(final List<EmailConfigurationDTO> selectedEmails) {

		final BatchClassDTO batchClassDTO = controller.getSelectedBatchClass();
		for (final EmailConfigurationDTO email : selectedEmails) {
			if (null != email) {
				batchClassDTO.getEmailConfigurationDTOByIdentifier(email.getIdentifier()).setDeleted(true);
			}
		}
		controller.setCurrentEmailConfigDTO(null);
		controller.setBatchClassDirtyFlg(true);
		view.removeItemsFromGrid(selectedEmails);
		view.commitChanges();
		view.reLoadGrid();
		Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS),
				LocaleDictionary.getMessageValue(BatchClassMessages.DELETE_EMAILS_SUCCESS_MSG));
	}

	/**
	 * Handle email addition.
	 * 
	 * @param addEvent the add event
	 */
	@EventHandler
	public void handleEmailAddition(final AddEmailEvent addEvent) {
		view.commitChanges();
		if (null != addEvent) {
			final EmailConfigurationDTO emailDTO = createNewEmail();
			if (view.addNewItemInGrid(emailDTO)) {
				controller.getSelectedBatchClass().addEmailConfiguration(emailDTO);
				controller.setBatchClassDirtyFlg(true);
			}
		}
	}

	/**
	 * Method to create the new email.
	 * 
	 * @return the email configuration dto {@link EmailConfigurationDTO}
	 */
	private EmailConfigurationDTO createNewEmail() {
		final EmailConfigurationDTO emailDTO = new EmailConfigurationDTO();
		emailDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
		emailDTO.setUserName(null);
		emailDTO.setPassword(BatchClassConstants.EMPTY_STRING);
		emailDTO.setFolderName(BatchClassConstants.INBOX);
		emailDTO.setServerName(BatchClassConstants.EMPTY_STRING);
		emailDTO.setServerType(BatchConstants.IMAP_SERVER_TYPE);
		emailDTO.setIsSSL(false);
		emailDTO.setPortNumber(null);
		emailDTO.setNew(true);
		emailDTO.setIsEnabled(true);
		return emailDTO;

	}

	/**
	 * Handle event to test the email.
	 * 
	 * @param testEmailEvent the test email event {@link TestEmailEvent}
	 */
	@EventHandler
	public void handleEmailTesting(final TestEmailEvent testEmailEvent) {
		view.commitChanges();
		ScreenMaskUtility.maskScreen();
		final List<EmailConfigurationDTO> selectedEmails = view.getSelectedEmails();
		final int totalMailAccountsCount = view.getTotalRecordCount();
		if (CollectionUtil.isEmpty(selectedEmails)) {
			if (totalMailAccountsCount == 0) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.NO_RECORD_TO_TEST_MSG), DialogIcon.WARNING);
				ScreenMaskUtility.unmaskScreen();
			} else {
				final EmailConfigurationDTO emailDTO = controller.getCurrentEmailConfigDTO();
				if (emailDTO != null) {
					verifyEmailConfig(emailDTO);
				} else {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.NO_ROW_SELECTED), DialogIcon.WARNING);
					ScreenMaskUtility.unmaskScreen();
				}
			}

		} else {
			if (selectedEmails.size() > 1) {
				String message = LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_ONE_ROW_ONLY_MSG);
				message = StringUtil.concatenate(message, LocaleDictionary.getMessageValue(BatchClassMessages.TEST_AN_EMAIL));
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE), message,
						DialogIcon.WARNING);
				ScreenMaskUtility.unmaskScreen();
			} else {
				verifyEmailConfig(selectedEmails.get(0));
			}
		}
	}

	/**
	 * Verify whether email configuration is valid.
	 * 
	 * @param emailConfigDTO the email configuration dto {@link EmailConfigurationDTO}
	 */
	public void verifyEmailConfig(final EmailConfigurationDTO emailConfigDTO) {
		controller.getRpcService().verifyEmailConfig(emailConfigDTO, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(final Throwable arg0) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_CONNECT_EMAIL_ERR_MSG), DialogIcon.ERROR);
				ScreenMaskUtility.unmaskScreen();
				// view.reLoadGrid();
			}

			@Override
			public void onSuccess(final Boolean isValid) {
				if (isValid != null && isValid) {
					Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS),
							LocaleDictionary.getMessageValue(BatchClassMessages.EMAIL_VALIDATED_SUCCESSFULLY));
					ScreenMaskUtility.unmaskScreen();
					// view.reLoadGrid();
				} else {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_CONNECT_EMAIL_ERR_MSG), DialogIcon.ERROR);
					ScreenMaskUtility.unmaskScreen();
					// view.reLoadGrid();

				}
			}
		});
	}

	/**
	 * Method to set the batch class dirty on change.
	 * 
	 */
	public void setBatchClassDirtyOnChange() {
		controller.setBatchClassDirtyFlg(true);
		;
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
