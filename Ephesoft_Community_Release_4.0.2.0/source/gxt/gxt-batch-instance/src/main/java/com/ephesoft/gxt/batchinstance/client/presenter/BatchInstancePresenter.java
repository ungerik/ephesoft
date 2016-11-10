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

package com.ephesoft.gxt.batchinstance.client.presenter;

import com.ephesoft.gxt.batchinstance.client.controller.BatchInstanceController;
import com.ephesoft.gxt.batchinstance.client.event.GridResizeEvent;
import com.ephesoft.gxt.batchinstance.client.view.BatchInstanceView;
import com.ephesoft.gxt.core.client.BasePresenter;
import com.google.gwt.event.shared.EventBus;

public class BatchInstancePresenter extends BasePresenter<BatchInstanceController, BatchInstanceView> {

	protected BatchInstanceGridPresenter batchInstanceGridPresenter;

	protected BatchInstanceOptionsPresenter batchInstanceOptionsPresenter;

	protected BatchInstanceDetailPresenter batchInstanceDetailPresenter;

	protected BatchInstanceLeftPanelPresenter batchInstanceLeftPanelPresenter;

	public enum Results {
		SUCCESSFUL, FAILURE;
	}

	public enum ExecutionStates {
		FOLDER_IMPORT("Folder Import"), PAGE_PROCESS("Page Process"), DOCUMENT_ASSEMBLY("Docuemnt Assembly"), READY_FOR_REVIEW(
				"Ready For Review"), EXTRACTION("Extraction"), AUTOMATIC_VALIDATION("Automatic Validation"), READY_FOR_VALIDATION("Ready For Validation"), EXPORT("Export");

		private String moduleName;

		ExecutionStates(String moduleName) {
			this.setModuleName(moduleName);
		}

		/**
		 * @param moduleName the moduleName to set
		 */
		public void setModuleName(String moduleName) {
			this.moduleName = moduleName;
		}

		/**
		 * @return the moduleName
		 */
		public String getModuleName() {
			return moduleName;
		}

	}

	public BatchInstancePresenter(BatchInstanceController controller, BatchInstanceView view) {
		super(controller, view);
		if (view != null) {
			batchInstanceOptionsPresenter = new BatchInstanceOptionsPresenter(controller, view.getBatchInstanceOptionsView());
			batchInstanceDetailPresenter = new BatchInstanceDetailPresenter(controller, view.getBatchInstanceDetailView());
			batchInstanceLeftPanelPresenter = new BatchInstanceLeftPanelPresenter(controller, view.getBatchInstanceLeftPanelView());
			batchInstanceGridPresenter = new BatchInstanceGridPresenter(controller, view.getGridView());
		}
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {

	}

	public BatchInstanceDetailPresenter getBatchInstanceDetailPresenter() {
		return batchInstanceDetailPresenter;
	}
	
	public void fireResizeEvent(){
		controller.getEventBus().fireEvent(new GridResizeEvent());
	}
}
