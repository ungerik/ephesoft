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

package com.ephesoft.gxt.admin.client.view.batchclass;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.batchclass.ExportBatchClassPresenter;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ExportBatchClassView extends View<ExportBatchClassPresenter> {

	interface Binder extends UiBinder<VerticalPanel, ExportBatchClassView> {
	}

	@UiField
	protected RadioButton importLearnedFilesRadio;
	@UiField
	protected RadioButton doNotImportLearnedFilesRadio;

	private DialogWindow dialogWindow;

	private String exportBatchClassIdentifier;

	private static final Binder BINDER = GWT.create(Binder.class);

	public ExportBatchClassView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

		importLearnedFilesRadio.setText(LocaleDictionary.getConstantValue(
				BatchClassConstants.IMPORT_LEARNED_FILES));
		doNotImportLearnedFilesRadio.setText(LocaleDictionary.getConstantValue(
				BatchClassConstants.DO_NOT_IMPORT_LEARNED_FILES));
	}

	public DialogWindow getDialogBox() {
		return dialogWindow;
	}

	public void setDialogBox(DialogWindow dialogBox) {
		this.dialogWindow = dialogBox;
	}

	public void setExportBatchClassIdentifier(String exportBatchClassIdentifier) {
		this.exportBatchClassIdentifier = exportBatchClassIdentifier;
	}

	public String getExportBatchClassIdentifier() {
		return exportBatchClassIdentifier;
	}

	/**
	 * @return the importLearnedFilesRadio
	 */
	public RadioButton getImportLearnedFilesRadio() {
		return importLearnedFilesRadio;
	}

	/**
	 * @param importLearnedFilesRadio the importLearnedFilesRadio to set
	 */
	public void setImportLearnedFilesRadio(RadioButton importLearnedFilesRadio) {
		this.importLearnedFilesRadio = importLearnedFilesRadio;
	}

	/**
	 * @return the doNotImportLearnedFilesRadio
	 */
	public RadioButton getDoNotImportLearnedFilesRadio() {
		return doNotImportLearnedFilesRadio;
	}

	/**
	 * @param doNotImportLearnedFilesRadio the doNotImportLearnedFilesRadio to set
	 */
	public void setDoNotImportLearnedFilesRadio(RadioButton doNotImportLearnedFilesRadio) {
		this.doNotImportLearnedFilesRadio = doNotImportLearnedFilesRadio;
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}
}
