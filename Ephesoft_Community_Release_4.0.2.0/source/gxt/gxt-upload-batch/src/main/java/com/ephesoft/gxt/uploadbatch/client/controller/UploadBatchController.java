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

package com.ephesoft.gxt.uploadbatch.client.controller;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.client.Controller;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.shared.dto.BatchClassFieldDTO;
import com.ephesoft.gxt.core.shared.dto.UploadBatchDTO;
import com.ephesoft.gxt.uploadbatch.client.UploadBatchServiceAsync;
import com.ephesoft.gxt.uploadbatch.client.presenter.UploadBatchLeftPanelPresenter;
import com.ephesoft.gxt.uploadbatch.client.presenter.UploadBatchPresenter;
import com.ephesoft.gxt.uploadbatch.client.view.UploadBatchLeftPanelView;
import com.ephesoft.gxt.uploadbatch.client.view.UploadBatchView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Composite;

/**
 * Controller for Upload Batch Screen.
 * 
 * @author Ephesoft
 * 
 */
public class UploadBatchController extends Controller {

	private UploadBatchPresenter uploadBatchPresenter;
	private UploadBatchLeftPanelPresenter uploadBatchLeftPanelPresenter;
	private UploadBatchLeftPanelView uploadBatchLeftPanelView;
	private UploadBatchView uploadBatchView;
	private String currentBatchUploadFolder;
	private List<UploadBatchDTO> uploadedFileList;
	private List<BatchClassFieldDTO> batchClassFieldDTOs;

	// private UploadBatchDTO selectedFile;
	// private BatchClassFieldDTO selectedBatchClassField;

	/**
	 * Constructor.
	 * 
	 * @param eventBus {@link EventBus}
	 * @param rpcService {@link DCMARemoteServiceAsync}
	 */
	public UploadBatchController(final EventBus eventBus, final DCMARemoteServiceAsync rpcService) {
		super(eventBus, rpcService);
		uploadedFileList = new ArrayList<UploadBatchDTO>();
		setBatchClassFieldDTOs(new ArrayList<BatchClassFieldDTO>());
	}

	@Override
	public Composite createView() {
		this.uploadBatchView = new UploadBatchView();
		uploadBatchLeftPanelView = getUploadBatchView().getUploadBatchLeftPanelView();
		this.setUploadBatchLeftPanelPresenter(new UploadBatchLeftPanelPresenter(this, uploadBatchLeftPanelView));
		this.uploadBatchPresenter = new UploadBatchPresenter(this, uploadBatchView);
		return this.uploadBatchView;
	}

	@Override
	public void injectEvents(final EventBus eventBus) {

	}

	@Override
	public UploadBatchServiceAsync getRpcService() {
		return (UploadBatchServiceAsync) super.getRpcService();
	}

	@Override
	public void refresh() {

	}

	/**
	 * Gets Upload Batch View.
	 * 
	 * @return {@link UploadBatchView}
	 */
	public UploadBatchView getUploadBatchView() {
		return uploadBatchView;
	}

	/**
	 * Gets Upload Batch Presenter.
	 * 
	 * @return {@link UploadBatchPresenter}
	 */
	public UploadBatchPresenter getMainPresenter() {
		return uploadBatchPresenter;
	}

	/**
	 * Gets Current upload folder name.
	 * 
	 * @return {@link String}
	 */
	public String getCurrentBatchUploadFolder() {
		return this.currentBatchUploadFolder;
	}

	/**
	 * Sets Current upload folder name.
	 * 
	 * @param currentBatchUploadFolder {@link String}
	 */
	public void setCurrentBatchUploadFolder(final String currentBatchUploadFolder) {
		this.currentBatchUploadFolder = currentBatchUploadFolder;
	}

	/**
	 * Gets Uploaded Files List.
	 * 
	 * @return {@link List}<{@link UploadBatchDTO}>
	 */
	public List<UploadBatchDTO> getUploadedFileList() {
		return uploadedFileList;
	}

	/**
	 * Sets Uploaded files list.
	 * 
	 * @param uploadedFileList {@link List}<{@link UploadBatchDTO}>
	 */
	public void setUploadedFileList(final List<UploadBatchDTO> uploadedFileList) {
		this.uploadedFileList = uploadedFileList;
	}

	/**
	 * Gets Batch Class Fields DTOs.
	 * 
	 * @return {@link List}<{@link BatchClassFieldDTO}>
	 */
	public List<BatchClassFieldDTO> getBatchClassFieldDTOs() {
		return batchClassFieldDTOs;
	}

	/**
	 * Sets Batch Class Field DTOs.
	 * 
	 * @param batchClassFieldDTOs {@link List}<{@link BatchClassFieldDTO}>
	 */
	public void setBatchClassFieldDTOs(final List<BatchClassFieldDTO> batchClassFieldDTOs) {
		this.batchClassFieldDTOs = batchClassFieldDTOs;
	}

	/**
	 * Gets selected files from Grid.
	 * 
	 * @return {@link List}<{@link UploadBatchDTO}>
	 */
	public List<UploadBatchDTO> getSelectedFilesFromGrid() {
		return uploadBatchView.getSelectedFilesFromGridView();
	}

	public UploadBatchLeftPanelPresenter getUploadBatchLeftPanelPresenter() {
		return uploadBatchLeftPanelPresenter;
	}

	public void setUploadBatchLeftPanelPresenter(UploadBatchLeftPanelPresenter uploadBatchLeftPanelPresenter) {
		this.uploadBatchLeftPanelPresenter = uploadBatchLeftPanelPresenter;
	}

	// public UploadBatchDTO getSelectedFile() {
	// return selectedFile;
	// }
	//
	// public void setSelectedFile(UploadBatchDTO selectedFile) {
	// this.selectedFile = selectedFile;
	// }

	// public BatchClassFieldDTO getSelectedBatchClassField() {
	// return selectedBatchClassField;
	// }
	//
	// public void setSelectedBatchClassField(BatchClassFieldDTO selectedBatchClassField) {
	// this.selectedBatchClassField = selectedBatchClassField;
	// }

}
