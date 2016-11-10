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

package com.ephesoft.gxt.systemconfig.client.view.workflowmanagement;

import org.moxieapps.gwt.uploader.client.File;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.FileQueuedEvent;
import org.moxieapps.gwt.uploader.client.events.FileQueuedHandler;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessEvent;
import org.moxieapps.gwt.uploader.client.events.UploadSuccessHandler;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.MultiFileUploader;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.workflowmanagement.ImportPluginPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

/**
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 08-Aug-2013 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class ImportPluginView extends SystemConfigInlineView<ImportPluginPresenter> {

	interface Binder extends UiBinder<Widget, ImportPluginView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected MultiFileUploader fileUploader;

	protected FileQueuedHandler fileQueuedHandler;

	protected UploadSuccessHandler uploadSuccessHandler;

	protected FileDialogCompleteHandler fileDialogCompleteHandler;

	/**
	 * Constructor.
	 */
	public ImportPluginView() {
		super();
		initWidget(binder.createAndBindUi(this));
		initializeUploader();
		fileUploader.setShowProgress(false);
		fileUploader.addFileQueuedHandler(fileQueuedHandler);
		fileUploader.addUploadSuccessHandler(uploadSuccessHandler);
		fileUploader.addFileDialogCompleteHandler(fileDialogCompleteHandler);
	}

	/**
	 * Initialises and sets properties of uploader.
	 */
	private void initializeUploader() {
		fileQueuedHandler = new FileQueuedHandler() {

			@Override
			public boolean onFileQueued(FileQueuedEvent fileQueuedEvent) {
				boolean validFile = false;
				File droppedFile = fileQueuedEvent.getFile();
				final String fileName = droppedFile.getName();
				final String fileExtension = fileName.substring(fileName.lastIndexOf(SystemConfigConstants.EXT_SEPARATOR) + 1);
				if (fileExtension.equalsIgnoreCase(SystemConfigConstants.ZIP)) {
					final String lastAttachedZipSourcePath = SystemConfigConstants.LAST_ATTACHED_ZIP_SOURCE_PATH + fileName;
					fileUploader.setUploadURL(StringUtil.concatenate(SystemConfigConstants.ATTACH_FORM_ACTION,
							lastAttachedZipSourcePath));
					validFile = true;
				} else {
					validFile = false;
					fileUploader.cancelUpload(droppedFile.getId(), false);
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.IMPORT_FILE_INVALID_TYPE), DialogIcon.ERROR);
				}
				return validFile;
			}
		};
		fileDialogCompleteHandler = new FileDialogCompleteHandler() {

			@Override
			public boolean onFileDialogComplete(FileDialogCompleteEvent fileDialogCompleteEvent) {
				if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
					if (fileUploader.getUploadsInProgress() <= 0) {
						fileUploader.startUpload();
					}
				}
				return true;
			}
		};
		uploadSuccessHandler = new UploadSuccessHandler() {

			@Override
			public boolean onUploadSuccess(UploadSuccessEvent uploadSuccessEvent) {
				String result = uploadSuccessEvent.getServerData();
				if (result.toLowerCase().indexOf(SystemConfigConstants.ERROR_CODE_TEXT) > -1) {
					DialogUtil.showConfirmationDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
							(result.substring(result.indexOf(SystemConfigConstants.CAUSE) + SystemConfigConstants.CAUSE.length())), false, DialogIcon.ERROR);
					return false;
				}
				String keyPluginName = SystemConfigConstants.PLUGIN_NAME;
				String keyXmlFolderPath = SystemConfigConstants.XML_FILE_PATH;
				String keyJarFolderPath = SystemConfigConstants.JAR_FILE_PATH;
				String pluginName = result.substring(result.indexOf(keyPluginName) + keyPluginName.length(),
						result.indexOf(SystemConfigConstants.SERVLET_RESULT_SEPERATOR, result.indexOf(keyPluginName)));
				String xmlSourcePath = result.substring(result.indexOf(keyXmlFolderPath) + keyXmlFolderPath.length(),
						result.indexOf(SystemConfigConstants.SERVLET_RESULT_SEPERATOR, result.indexOf(keyXmlFolderPath)));
				String jarSourcePath = result.substring(result.indexOf(keyJarFolderPath) + keyXmlFolderPath.length(),
						result.indexOf(SystemConfigConstants.SERVLET_RESULT_SEPERATOR, result.indexOf(keyJarFolderPath)));

				presenter.onSaveClicked(pluginName, xmlSourcePath, jarSourcePath);
				return true;
			}
		};

	}

	@Override
	public void initialize() {

	}
}
