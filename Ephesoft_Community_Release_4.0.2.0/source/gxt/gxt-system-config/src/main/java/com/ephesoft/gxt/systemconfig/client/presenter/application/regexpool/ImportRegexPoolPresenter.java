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

package com.ephesoft.gxt.systemconfig.client.presenter.application.regexpool;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.ImportPoolDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.RefreshRegexGroupGridEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigInlinePresenter;
import com.ephesoft.gxt.systemconfig.client.view.application.regexpool.ImportRegexPoolView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ImportRegexPoolPresenter extends SystemConfigInlinePresenter<ImportRegexPoolView> {

	public ImportRegexPoolPresenter(SystemConfigController controller, ImportRegexPoolView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
		controller.setBottomPanelHeader(LocaleDictionary.getConstantValue(SystemConfigConstants.IMPORT_REGEX_BOTTOM_PANEL_HEADER));
	}

	@Override
	public void injectEvents(EventBus eventBus) {

	}

	public void onRegexUploadComplete(List<ImportPoolDTO> filesToBeUploaded) {
		ScreenMaskUtility.maskScreen();
		if (!CollectionUtil.isEmpty(filesToBeUploaded)) {
			controller.getRpcService().importRegexGroups(filesToBeUploaded, new AsyncCallback<Map<String, Boolean>>() {

				@Override
				public void onFailure(Throwable caught) {
					ScreenMaskUtility.unmaskScreen();
					String localizedMessage = caught.getLocalizedMessage();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
							localizedMessage, DialogIcon.ERROR);
				}

				@Override
				public void onSuccess(Map<String, Boolean> result) {
					ScreenMaskUtility.unmaskScreen();
					List<String> filesNotUploadedList = updateNotUploadedFilesList(result);
					view.filesNotSupportedAction(filesNotUploadedList);
					view.clearUploadedFilesList();
					controller.getEventBus().fireEvent(new RefreshRegexGroupGridEvent());
				}
			});

		}
	}

	private List<String> updateNotUploadedFilesList(Map<String, Boolean> result) {
		List<String> filesNotUploadedList = view.getFilesNotSupportedList();
		List<ImportPoolDTO> uploadedFiles = view.getFilesToBeUploaded();
		for (ImportPoolDTO importPoolDto : uploadedFiles) {
			boolean isFileUploaded = isFileUploaded(importPoolDto, result);
			if (!isFileUploaded) {
				filesNotUploadedList.add(importPoolDto.getZipFileName());
			}
		}
		return filesNotUploadedList;
	}

	private boolean isFileUploaded(ImportPoolDTO importPoolDto, Map<String, Boolean> result) {
		boolean isFileUploaded = false;
		Iterator<Entry<String, Boolean>> iterator = result.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Boolean> entrySet = iterator.next();
			if (entrySet.getKey().equals(importPoolDto.getZipFileName())) {
				isFileUploaded = entrySet.getValue();
				break;
			}
		}
		return isFileUploaded;
	}
}
