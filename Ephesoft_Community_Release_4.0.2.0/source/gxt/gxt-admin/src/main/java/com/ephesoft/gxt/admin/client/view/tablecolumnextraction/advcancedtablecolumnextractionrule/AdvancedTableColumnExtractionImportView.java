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

package com.ephesoft.gxt.admin.client.view.tablecolumnextraction.advcancedtablecolumnextractionrule;

import org.moxieapps.gwt.uploader.client.File;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessEvent;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessHandler;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction.advancedtablecolumnextractionrule.AdvancedTableColumnExtractionImportPresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.MultiFileUploader;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class AdvancedTableColumnExtractionImportView.
 */
public class AdvancedTableColumnExtractionImportView extends
		AdvancedTableColumnExtractionInlineView<AdvancedTableColumnExtractionImportPresenter> {

	/**
	 * The Interface Binder.
	 */
	interface Binder extends UiBinder<Widget, AdvancedTableColumnExtractionImportView> {
	}

	/** The Constant BINDER. */
	private static final Binder BINDER = GWT.create(Binder.class);

	/** The file uploader. */
	@UiField
	protected MultiFileUploader fileUploader;

	/** The Constant UPLOAD_FORM_ACTION. */
	private static final String UPLOAD_FORM_ACTION = StringUtil.concatenate("/dcma/uploadImageFile?isAdvancedTableInfo=",
			String.valueOf(Boolean.TRUE));

	/** The is valid. */
	private boolean isValid = true;

	/**
	 * Instantiates a new advanced table column extraction import view.
	 */
	public AdvancedTableColumnExtractionImportView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		setWidgetIDs();
	}

	/**
	 * Initializes and set the properties of uploader.
	 */
	public void initializeUploader() {
		FileQueuedHandler fileQueueHandler = new FileQueuedHandler() {

			public boolean onFileQueued(final FileQueuedEvent fileQueuedEvent) {
				final File newDroppedFile = fileQueuedEvent.getFile();
				if (FileType.isValidFileName(newDroppedFile.getName().toLowerCase())) {
					return true;
				} else {
					if (isValid()) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_INPUT_FILE_FORMAT), DialogIcon.ERROR);
						setValid(false);
					}
					fileUploader.cancelUpload(fileQueuedEvent.getFile().getId(), false);
					return false;
				}
			}
		};
		fileUploader.addFileQueuedHandler(fileQueueHandler);

		UploadSuccessHandler uploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				String str1 = uploadSuccessEvent.getServerData();
				if (str1.toLowerCase().indexOf("multipage_error") > -1) {
					ScreenMaskUtility.unmaskScreen();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.ONLY_SINGLE_PAGE_ONE_FILE_SUPPORTED),
							DialogIcon.WARNING);
				} else if (str1.toLowerCase().indexOf(AdminConstants.ERROR_CODE_TEXT) > -1
						|| !str1.contains(AdminConstants.PATTERN_DELIMITER)) {
					ScreenMaskUtility.unmaskScreen();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_UPLOAD_IMAGE), DialogIcon.ERROR);
				} else {
					String[] array = str1.split(AdminConstants.PATTERN_DELIMITER);
					presenter.loadImage(array[0]);
				}
				return true;
			}
		};

		fileUploader.addUploadSuccessHandler(uploadSuccessHandler);

		FileDialogCompleteHandler fileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				if (fileDialogCompleteEvent.getNumberOfFilesSelected() > 1 && isValid()) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.MULTIPLE_FILE_UPLOAD_NOT_SUPPORTED),
							DialogIcon.WARNING);
				} else if (fileDialogCompleteEvent.getNumberOfFilesSelected() == 1 && isValid()) {
					TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO = presenter.getController()
							.getSelectedTableColumnExtractionRuleDTO();
					if (null != tableColumnExtractionRuleDTO) {
						DocumentTypeDTO docTypeDTO = tableColumnExtractionRuleDTO.getTableColumnInfoDTO().getTableInfoDTO()
								.getDocTypeDTO();
						if (null != docTypeDTO) {
							final String param = StringUtil.concatenate("&batchClassId=", docTypeDTO.getBatchClass().getIdentifier(),
									"&", "docName=", docTypeDTO.getName());
							fileUploader.setUploadURL(StringUtil.concatenate(UPLOAD_FORM_ACTION, param));
							fileUploader.startUpload();
							return true;
						}
					}
				}
				setValid(true);
				return false;

			}
		};

		fileUploader.addFileDialogCompleteHandler(fileDialogCompleteHandler);

	}

	/**
	 * Sets the widget ids.
	 */
	private void setWidgetIDs() {
		WidgetUtil.setID(fileUploader, "advancedTableColumnExtractionVerticalPanel");
	}

	/**
	 * Checks if files are valid.
	 * 
	 * @return true, if files are valid
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * Sets the valid.
	 * 
	 * @param isValid the new valid
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}
