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

package com.ephesoft.gxt.admin.client.presenter.kvextraction;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter;
import com.ephesoft.gxt.admin.client.view.kvextraction.KVExtractionView;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.google.gwt.event.shared.EventBus;

public class KVExtractionPresenter extends BatchClassCompositePresenter<KVExtractionView, FieldTypeDTO> {

	KVExtractionMenuPresenter kvExtractionMenuPresenter;
	KVExtractionGridPresenter kvExtractionGridPresenter;

	public KVExtractionPresenter(BatchClassManagementController controller, KVExtractionView view) {
		super(controller, view);
		kvExtractionMenuPresenter = new KVExtractionMenuPresenter(controller, view.getMenuView());
		kvExtractionGridPresenter = new KVExtractionGridPresenter(controller, view.getGridView());
	}

	@Override
	public void bind() {
		controller.collapseBottomPanel();
	}

	@Override
	public void init(FieldTypeDTO selectionParameter) {
		if (null != selectionParameter) {
			controller.setSelectedFieldType(selectionParameter);
		}
	}

	@Override
	public BatchClassCompositePresenter<?, ?> getParentPresenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BatchClassCompositePresenter<?, ?> getChildPresenter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isValid() {
		return kvExtractionGridPresenter.isValid();
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

	public enum Results {

		/** The successful. */
		SUCCESSFUL,
		/** The failure. */
		FAILURE,
		/** The partial success. */
		PARTIAL_SUCCESS;
	}

}
