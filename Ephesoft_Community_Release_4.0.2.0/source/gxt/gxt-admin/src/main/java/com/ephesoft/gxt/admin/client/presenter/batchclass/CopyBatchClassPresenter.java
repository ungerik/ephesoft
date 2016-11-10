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

package com.ephesoft.gxt.admin.client.presenter.batchclass;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.ReloadBatchClassEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.view.batchclass.CopyBatchClassView;
import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author Ephesoft
 *
 */
/**
 * Presenter for copying a batch class.
 */
public class CopyBatchClassPresenter<V extends CopyBatchClassView> extends BasePresenter<BatchClassManagementController, V> {

	/**
	 * The batch class that is to being copied.
	 */
	private BatchClassDTO batchClassDTO;

	public CopyBatchClassPresenter(final BatchClassManagementController controller, final V view) {
		super(controller, view);
	}

	@Override
	public void bind() {
		if (batchClassDTO != null) {
			view.setName(batchClassDTO.getName());
			view.setUncFolder(batchClassDTO.getUncFolder());
			view.setPrioritySlider(batchClassDTO.getPriority());
			view.setDescription(batchClassDTO.getDescription());
			view.setprioritySliderValue(view.getPrioritySliderString());

			// Changes made to solve: (EPHESOFT-12858) : Validation check
			// missing for batch class name.
			// This rpc fetches characters that are not allowed in batch class
			// name from property file.
			if (null == controller.getNotAllowedBatchClassCharacters()) {
				controller.getRpcService().getNotAllowedCharactersInBCName(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable arg0) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
								LocaleDictionary.getMessageValue(BatchClassConstants.BC_NAME_UNALLOWED_CHARS_ERROR_MESSAGE));
					}

					@Override
					public void onSuccess(String notAllowedBatchClassCharacters) {
						controller.setNotAllowedBatchClassCharacters(notAllowedBatchClassCharacters);
						view.setBcNameSpecialCharacterValidator(notAllowedBatchClassCharacters);
						view.setInvalidCharacterMessage(notAllowedBatchClassCharacters);
					}
				});
			} else {
				view.setBcNameSpecialCharacterValidator(controller.getNotAllowedBatchClassCharacters());
				view.setInvalidCharacterMessage(controller.getNotAllowedBatchClassCharacters());
			}
			controller.getRpcService().getAllBatchClasses(new AsyncCallback<List<BatchClassDTO>>() {

				@Override
				public void onFailure(Throwable caught) {

				}

				@Override
				public void onSuccess(List<BatchClassDTO> batchClasses) {
					if (!CollectionUtil.isEmpty(batchClasses)) {
						List<String> uncNames = new ArrayList<String>();
						List<String> bcNames = new ArrayList<String>();
						for (BatchClassDTO dto : batchClasses) {
							if (null != dto.getName()) {
								bcNames.add(dto.getName());
							}
							if (null != dto.getUncFolder()) {
								uncNames.add(dto.getUncFolder());
							}
						}
						view.setBcNameUniqueValidator(bcNames);
						view.setUncFolderUniqueValidator(uncNames);
					}
				}
			});
		}
	}

	public void showBatchClassCopyView() {
		view.getDialogWindow().add(view);
		view.getDialogWindow().setFocusWidget(view.getNameTextField());
		view.getDialogWindow().show();
		view.getDialogWindow().setWidth("400px");
		view.getDialogWindow().setHeadingText(LocaleDictionary.getConstantValue(BatchClassConstants.COPY_BATCH_CLASS_LABEL));
	}

	public BatchClassDTO getBatchClassDTO() {
		return batchClassDTO;
	}

	public void setBatchClassDTO(final BatchClassDTO batchClassDTO) {
		this.batchClassDTO = batchClassDTO;
	}

	public void onOkClicked() {
		view.fireValidations();
		boolean validatedError = view.validateInputs();
		if (validatedError) {
			batchClassDTO.setName(view.getName());
			batchClassDTO.setDescription(view.getDescription());
			batchClassDTO.setPriority(view.getPrioritySlider());
			batchClassDTO.setUncFolder(view.getUncFolder());
			controller.getRpcService().createUncFolder(batchClassDTO.getUncFolder(), new AsyncCallback<Void>() {

				@Override
				public void onFailure(final Throwable arg0) {
					Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.COPY_BATCH_CLASS_LABEL),
							LocaleDictionary.getMessageValue(BatchClassMessages.UNC_PATH_NOT_EMPTY));
				}

				@Override
				public void onSuccess(final Void arg0) {
					view.getDialogWindow().hide();
					ScreenMaskUtility.maskScreen();
					controller.getRpcService().copyBatchClass(batchClassDTO, view.getGridCheckBoxValue(), new AsyncCallback<String>() {

						@Override
						public void onFailure(final Throwable arg0) {
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.UNABLE_TO_CREATE_COPY), DialogIcon.ERROR);
						}

						@Override
						public void onSuccess(String result) {
							ScreenMaskUtility.unmaskScreen();
							Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.COPY_BATCH_CLASS_LABEL), LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_COPIED_SUCCESSFULLY, view.getName()));
							// refresh batch class management
							// screen.
							controller.getEventBus().fireEvent(new ReloadBatchClassEvent());
						}
					});
				}
			});

		}
	}

	// /**
	// * Checks if a batch class name or description contains invalid characters
	// or not.
	// *
	// * @param textBoxData
	// * @return boolean isValidBCname
	// */
	// public boolean validateBCNameForSpecialCharacters(String textBoxData) {
	// boolean isValidBCname = true;
	// String invalidSpecialChars =
	// controller.getNotAllowedBatchClassCharacters();
	// char[] invalidChars = invalidSpecialChars.toCharArray();
	// if (textBoxData != null && !textBoxData.isEmpty() && invalidChars != null
	// && invalidChars.length > 0) {
	// for (char invalidChar : invalidChars) {
	// if (textBoxData.indexOf(invalidChar) != -1) {
	// isValidBCname = false;
	// break;
	// }
	// }
	// }
	// return isValidBCname;
	// }

	// if
	// (AdminConstants.EMPTY_STRING.equalsIgnoreCase(validationFailResponseName))
	// {
	// nameTextBox.removeStyleName("changeStyleTextBox");
	// nameTextBox.setTitle(AdminConstants.EMPTY_STRING);
	// } else {
	// nameTextBox.addStyleName("changeStyleTextBox");
	// nameTextBox.setTitle(validationFailResponseName);
	// }
	// if
	// (AdminConstants.EMPTY_STRING.equalsIgnoreCase(validationFailResponseDesc))
	// {
	// descTextBox.removeStyleName("changeStyleTextBox");
	// descTextBox.setTitle(AdminConstants.EMPTY_STRING);
	// } else {
	// descTextBox.addStyleName("changeStyleTextBox");
	// descTextBox.setTitle(validationFailResponseDesc);
	// }
	// if
	// (AdminConstants.EMPTY_STRING.equalsIgnoreCase(validationFailResponseUNC))
	// {
	// uncTextBox.removeStyleName("changeStyleTextBox");
	// uncTextBox.setTitle(AdminConstants.EMPTY_STRING);
	// } else {
	// uncTextBox.addStyleName("changeStyleTextBox");
	// uncTextBox.setTitle(validationFailResponseUNC);
	// }

	@Override
	public void injectEvents(EventBus eventBus) {
		// TODO Auto-generated method stub

	}
}
