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

package com.ephesoft.dcma.gwt.core.shared;

import com.ephesoft.dcma.gwt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.dcma.gwt.core.client.i18n.LocaleDictionary;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class ConfirmationDialogUtil {

	private static ConfirmationDialog confirmationDialog;
	//Adding Confirmation dialog type for Split with checkbox
	private static ConfirmationDialog confirmationDialogSplit;

	public static ConfirmationDialog showConfirmationDialog(final String message, final String title, final Boolean removeCancelButton) {
		return showConfirmationDialog(message, title, removeCancelButton, false);
	}

	public static ConfirmationDialog showConfirmationDialog(final String message, final String title,
			final Boolean removeCancelButton, final boolean createNewDialogInstance) {
		return showConfirmationDialog(message, title, removeCancelButton, createNewDialogInstance, false, "");

	}

	/**
	 * This API is used to create an instance of confirmation dialog with option to handle the third button.
	 * 
	 * @param message {@link String}
	 * @param title {@link String}
	 * @param removeCancelButton {@link Boolean}
	 * @param createNewDialogInstance boolean
	 * @param showThirdButton boolean
	 * @param thirdButtontext {@link String}
	 * @return {@link ConfirmationDialog}
	 */
	public static ConfirmationDialog showConfirmationDialog(final String message, final String title,
			final Boolean removeCancelButton, final boolean createNewDialogInstance, final boolean showThirdButton,
			final String thirdButtontext) {
		if (createNewDialogInstance || confirmationDialog == null) {
			confirmationDialog = new ConfirmationDialog(true);
		} else {
			confirmationDialog.setDefaultListener(showThirdButton);
		}
		confirmationDialog.hide();
		confirmationDialog.okButton.setText(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_confirmation_ok));
		confirmationDialog.cancelButton.setText(LocaleDictionary.get().getConstantValue(
				LocaleCommonConstants.title_confirmation_cancel));
		confirmationDialog.setMessage(message);
		confirmationDialog.setDialogTitle(title);
		if (removeCancelButton) {
			confirmationDialog.cancelButton.setVisible(false);
		} else {
			confirmationDialog.cancelButton.setVisible(true);
		}
		if (showThirdButton) {
			confirmationDialog.thirdButton.setText(thirdButtontext);
			confirmationDialog.thirdButton.setVisible(true);
		} else {
			confirmationDialog.thirdButton.setVisible(false);
		}
		confirmationDialog.show();
		confirmationDialog.center();
		confirmationDialog.okButton.setFocus(true);
		return confirmationDialog;
	}

	public static ConfirmationDialog showConfirmationDialogSuccess(final String message) {
		return showConfirmationDialogSuccess(message, false);
	}

	public static ConfirmationDialog showConfirmationDialogSuccess(final String message, final boolean createNewConfirmationDialog) {
		return showConfirmationDialog(message, LocaleDictionary.get().getConstantValue(LocaleCommonConstants.SUCCESS_TITLE), true,
				createNewConfirmationDialog);
	}

	public static ConfirmationDialog showConfirmationDialogError(final String message) {
		return showConfirmationDialogError(message, false);
	}

	public static ConfirmationDialog showConfirmationDialogError(final String message, final boolean createNewConfirmationDialog) {
		return showConfirmationDialog(message, LocaleDictionary.get().getConstantValue(LocaleCommonConstants.ERROR_TITLE), true,
				createNewConfirmationDialog);
	}

	public static ConfirmationDialog getConfirmationDialog() {
		return confirmationDialog;
	}
	/**
	 * This API is used to create an instance of confirmation dialog with option to handle CheckBox in Confirmation Dialogue
	 * 
	 * @param message {@link String}
	 * @param title {@link String}
	 * @param removeCancelButton {@link Boolean}
	 * @param createNewDialogInstance boolean
	 * @param isChkBoxEnabled boolean
	 * @param isDocumentFieldEnabled default value specified set in Application.properties
	 * @param isTableFieldEnabled as per value specified in application.properties
	 * @return {@link ConfirmationDialog}
	 */
	public static ConfirmationDialog showConfirmationDialogOnSplit(final String message, final String title,
			final Boolean removeCancelButton, final Boolean isChkBoxEnabled, final Boolean isDocumentFieldEnabled,
			final Boolean isTableFieldEnabled,boolean isTableExists) {
		if (confirmationDialogSplit == null) {
			confirmationDialogSplit = new ConfirmationDialog(true);
		}
		confirmationDialogSplit.hide();
		if (isChkBoxEnabled) {
			confirmationDialogSplit.docTypeFieldChkBox.setText(LocaleDictionary.get().getConstantValue(
					LocaleCommonConstants.RETAIN_DOCUMENT_LEVEL_FIELD));
			confirmationDialogSplit.tableFieldChkBox.setText(LocaleDictionary.get().getConstantValue(
					LocaleCommonConstants.TABLE_LEVEL_FIELD));

			confirmationDialogSplit.docTypeFieldChkBox.setValue(isDocumentFieldEnabled);
			if(isTableExists){
			confirmationDialogSplit.tableFieldChkBox.setValue(isTableFieldEnabled);
			confirmationDialogSplit.tableFieldChkBox.setEnabled(true);
			}else{
			confirmationDialogSplit.tableFieldChkBox.setValue(false);
			confirmationDialogSplit.tableFieldChkBox.setEnabled(false);
			}
			confirmationDialogSplit.docTypeFieldChkBox.setVisible(true);
			confirmationDialogSplit.tableFieldChkBox.setVisible(true);

			confirmationDialogSplit.verticalPanel.add(confirmationDialogSplit.docTypeFieldChkBox);
			confirmationDialogSplit.verticalPanel.add(confirmationDialogSplit.tableFieldChkBox);

			confirmationDialogSplit.verticalPanel.setCellHorizontalAlignment(confirmationDialogSplit.docTypeFieldChkBox,
					HasHorizontalAlignment.ALIGN_LEFT);
			confirmationDialogSplit.verticalPanel.setVisible(true);
		}

		confirmationDialogSplit.okButton.setText(LocaleDictionary.get().getConstantValue(LocaleCommonConstants.title_confirmation_ok));
		confirmationDialogSplit.cancelButton.setText(LocaleDictionary.get().getConstantValue(
				LocaleCommonConstants.title_confirmation_cancel));
		confirmationDialogSplit.setMessage(message);
		confirmationDialogSplit.setDialogTitle(title);
		confirmationDialogSplit.thirdButton.setVisible(false);

		if (removeCancelButton) {
			confirmationDialogSplit.cancelButton.setVisible(false);
		} else {
			confirmationDialogSplit.cancelButton.setVisible(true);
		}

		confirmationDialogSplit.show();
		confirmationDialogSplit.center();
		confirmationDialogSplit.okButton.setFocus(true);
		return confirmationDialogSplit;
	}
}
