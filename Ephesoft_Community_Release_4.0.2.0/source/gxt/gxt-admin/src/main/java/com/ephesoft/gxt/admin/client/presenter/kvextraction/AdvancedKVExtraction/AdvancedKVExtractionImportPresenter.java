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

package com.ephesoft.gxt.admin.client.presenter.kvextraction.AdvancedKVExtraction;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.moxieapps.gwt.uploader.client.File;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.ReloadUploadedFilesEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionImportView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.RandomIdGenerator;
import com.ephesoft.gxt.core.shared.dto.AdvancedKVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.AdvancedKVExtractionDetailDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

public class AdvancedKVExtractionImportPresenter extends AdvancedKVExtractionInlinePresenter<AdvancedKVExtractionImportView> {

	private static final char EXTENSION_CHAR = '.';
	private static final String EXTENSION_PNG = "png";
	private LinkedHashSet<String> tempImages;

	interface CustomEventBinder extends EventBinder<AdvancedKVExtractionImportPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public AdvancedKVExtractionImportPresenter(BatchClassManagementController controller, AdvancedKVExtractionImportView view) {
		super(controller, view);
		view.initializeUploader();
	}

	final Map<String, AdvancedKVExtractionDTO> uploadingFilesMap = new LinkedHashMap<String, AdvancedKVExtractionDTO>();

	@Override
	public void bind() {
		tempImages = new LinkedHashSet<String>();
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		// TODO Auto-generated method stub
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public boolean addNewFileIntoList(final String newDroppedFileName) {

		boolean fileAdded = true;
		if (null != newDroppedFileName && !StringUtil.isNullOrEmpty(newDroppedFileName)) {
			AdvancedKVExtractionDTO advancedKVExtractionDTO = controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO();
			String fileName = newDroppedFileName;
			String fileExtension = fileName.substring(fileName.lastIndexOf(EXTENSION_CHAR));
			fileName = fileName.substring(0, fileName.lastIndexOf(EXTENSION_CHAR)).concat(fileExtension.toLowerCase());
			if (advancedKVExtractionDTO!=null){
				if(advancedKVExtractionDTO.getAdvKVExtractionDetailByFileName(fileName) == null) {
					final AdvancedKVExtractionDetailDTO newAdvKVEDetailDTO = new AdvancedKVExtractionDetailDTO();
					newAdvKVEDetailDTO.setIdentifier(String.valueOf(RandomIdGenerator.getIdentifier()));
					newAdvKVEDetailDTO.setFileName(fileName);
					newAdvKVEDetailDTO.setNew(true);
					newAdvKVEDetailDTO.setDeleted(false);
                    newAdvKVEDetailDTO.setMultiPage(false);
                    newAdvKVEDetailDTO.setPageCount(1);					
					advancedKVExtractionDTO.addAdvKVDetail(newAdvKVEDetailDTO);
				}
				tempImages.add(fileName);
			}

		} else {
			fileAdded = false;
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_WHILE_UPLOADING), DialogIcon.ERROR);
		}
		return fileAdded;
	}

	public void loadNewImages() {
		if (tempImages!=null && tempImages.size() > 0) {
			controller.getEventBus().fireEvent(new ReloadUploadedFilesEvent(tempImages));
			tempImages = new LinkedHashSet<String>();
		}
	}

	public void setImageNameInDTO(String fileName, String pngFileName) {
		controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO().setSelectedImageName(fileName);
		controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO().setSelectedImageDisplayName(pngFileName);
	}

	public boolean isFilePresent(File newDroppedFile) {
		if (uploadingFilesMap.containsKey(newDroppedFile.getName())) {
			return true;
		} else
			return false;
	}
}
