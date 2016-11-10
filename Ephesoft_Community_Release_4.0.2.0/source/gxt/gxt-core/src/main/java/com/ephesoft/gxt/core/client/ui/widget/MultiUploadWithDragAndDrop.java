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

package com.ephesoft.gxt.core.client.ui.widget;

import java.util.ArrayList;
import java.util.List;

import org.moxieapps.gwt.uploader.client.Uploader;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.FileDialogCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteEvent;
import org.moxieapps.gwt.uploader.client.events.UploadCompleteHandler;
import org.moxieapps.gwt.uploader.client.events.UploadErrorEvent;
import org.moxieapps.gwt.uploader.client.events.UploadErrorHandler;

import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;

/**
 * Uploader widget with ability to drag and drop file to upload.
 */
public class MultiUploadWithDragAndDrop extends Uploader {

	// private Map<String, Image> cancelButtons = new LinkedHashMap<String, Image>();

	private boolean isLoaded;
	
	private List<String> uploadErrorFiles;

	/**
	 * Constructor.
	 */
	public MultiUploadWithDragAndDrop() {
		super();
		setDefaultPropertiesOfUploadWidget();
	}

	@Override
	protected void onLoad() {
		/*
		 * 
		 * It is suspected to be a bug with Moxie-app implementation or its compatibility with Sencha. On load method is being called
		 * onAttach which adds a label in the DOM.
		 * 
		 * But in border layout container onAttach is called when the views expand or collapse.
		 * 
		 * So the labels were getting replicated....
		 */
		if (!isLoaded) {
			super.onLoad();
			isLoaded = true;
		}
	}

	/**
	 * Sets default properties of upload widget.
	 */
	private void setDefaultPropertiesOfUploadWidget() {
		this.setButtonText(LocaleConstants.CLICK_HERE_TO_UPLOAD);
		this.setButtonCursor(Uploader.Cursor.HAND);
		this.setButtonAction(Uploader.ButtonAction.SELECT_FILES);

		this.setUploadCompleteHandler(new UploadCompleteHandler() {

			public boolean onUploadComplete(final UploadCompleteEvent uploadCompleteEvent) {
				startUpload();
				return true;
			}
		});

		this.setFileDialogCompleteHandler(new FileDialogCompleteHandler() {

			public boolean onFileDialogComplete(final FileDialogCompleteEvent fileDialogCompleteEvent) {
				if (fileDialogCompleteEvent.getTotalFilesInQueue() > 0) {
					if (getStats().getUploadsInProgress() <= 0) {
						startUpload();
					}
				}
				return true;
			}
		});

		this.setUploadErrorHandler(new UploadErrorHandler() {

			public boolean onUploadError(final UploadErrorEvent uploadErrorEvent) {
				if (null == uploadErrorFiles){
					uploadErrorFiles = new ArrayList<String>();
				}
				final int noOfFilesQueued = getStats().getFilesQueued();
				if (noOfFilesQueued > 0) {
					final String fileName = uploadErrorEvent.getFile().getName();
					uploadErrorFiles.add(fileName);
				}
				return true;
			}
		});
	}
	
	@Override
	public void startUpload() {
		super.startUpload();
		if (getStats().getFilesQueued() == 0) {
			onFinishUpload();
		}
	}

	public void onFinishUpload () {
		if (!CollectionUtil.isEmpty(uploadErrorFiles)) {
			String concatenatedFileList = CoreCommonConstant.EMPTY_STRING;
			for (String fileName : uploadErrorFiles){
				concatenatedFileList = StringUtil.concatenate(concatenatedFileList, "<br>", fileName);
			}
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(LocaleCommonConstants.ERROR_TITLE),
					StringUtil.concatenate("Error occurred while uploading following files.", concatenatedFileList), DialogIcon.ERROR);
			uploadErrorFiles = null;
		}
	}
}
