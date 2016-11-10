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

package com.ephesoft.gxt.uploadbatch.client.presenter;

import java.util.List;

import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.shared.dto.UploadBatchDTO;
import com.ephesoft.gxt.uploadbatch.client.controller.UploadBatchController;
import com.ephesoft.gxt.uploadbatch.client.event.DeleteFilesFromUploadedFilesGridEvent;
import com.ephesoft.gxt.uploadbatch.client.event.FileUploadProgressEvent;
import com.ephesoft.gxt.uploadbatch.client.event.RefreshUploadedFilesGridEvent;
import com.ephesoft.gxt.uploadbatch.client.event.ReloadUploadedFilesGridEvent;
import com.ephesoft.gxt.uploadbatch.client.view.UploadBatchGridView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class UploadBatchGridPresenter extends BasePresenter<UploadBatchController, UploadBatchGridView> {

	interface CustomEventBinder extends EventBinder<UploadBatchGridPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Constructor.
	 * 
	 * @param controller {@link UploadBatchController}
	 * @param view {@link UploadBatchGridView}
	 */
	public UploadBatchGridPresenter(final UploadBatchController controller, final UploadBatchGridView view) {
		super(controller, view);
		view.setDataAndReloadGrid(null);
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);

	}

	@EventHandler
	public void relodGrid(final ReloadUploadedFilesGridEvent reloadGridEvent) {
		view.setDataAndReloadGrid(controller.getUploadedFileList());
	}

	@EventHandler
	public void refreshGrid(final RefreshUploadedFilesGridEvent reloadGridEvent) {
		final UploadBatchDTO row = reloadGridEvent.getRow();
		view.refreshRecord(row);
	}

	@EventHandler
	public void deleteFilesFromGrid(final DeleteFilesFromUploadedFilesGridEvent deleteFileFromGridEvent) {
		final List<UploadBatchDTO> selectedModels = deleteFileFromGridEvent.getSelectedModels();
		view.deleteModels(selectedModels);
	}

	public void loadCharts() {
		controller.getEventBus().fireEvent(new FileUploadProgressEvent(null));
	}
}
