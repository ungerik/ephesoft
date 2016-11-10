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

package com.ephesoft.gxt.admin.client.presenter.indexFiled;

import java.util.List;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AddIndexFieldEvent;
import com.ephesoft.gxt.admin.client.event.CopyIndexFieldsEvent;
import com.ephesoft.gxt.admin.client.event.DeleteIndexFieldsEvent;
import com.ephesoft.gxt.admin.client.event.ExportSelectedIndexFieldEvent;
import com.ephesoft.gxt.admin.client.event.RetrieveSelectedIndexFieldListEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.indexFields.IndexFieldsMenuView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class IndexFieldMenuPresenter extends BatchClassInlinePresenter<IndexFieldsMenuView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<IndexFieldMenuPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public IndexFieldMenuPresenter(BatchClassManagementController controller, IndexFieldsMenuView view) {
		super(controller, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.presenter.BatchClassManagementMenuPresenter#bind()
	 */
	@Override
	public void bind() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ephesoft.gxt.admin.client.presenter.BatchClassManagementMenuPresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void addIndexField() {
		controller.getEventBus().fireEvent(new AddIndexFieldEvent());
	}

	public void deleteIndexField() {
		controller.getEventBus().fireEvent(new DeleteIndexFieldsEvent());
	}

	public void exportIndexField() {
		BatchClassManagementEventBus.fireEvent(new RetrieveSelectedIndexFieldListEvent());
	}

	@EventHandler
	public void handleExportIndexField(ExportSelectedIndexFieldEvent exportSelectedIndexFields) {
		if (null != exportSelectedIndexFields) {
			List<FieldTypeDTO> indexFieldList = exportSelectedIndexFields.getIndexFieldDTOList();
			if (null != indexFieldList) {
				if (!controller.getSelectedBatchClass().isDirty()) {
					StringBuilder identifierList = new StringBuilder();
					for (FieldTypeDTO fieldTypeDTO : indexFieldList) {
						identifierList.append(fieldTypeDTO.getIdentifier() + ";");
					}
					final DialogWindow dialogWindow = new DialogWindow();
					WidgetUtil.setID(dialogWindow, "exportIndexField_view");
					final FormPanel exportPanel = new FormPanel();
					view.setExportFormPanel(exportPanel);
					view.addFormPanelEvents(identifierList.toString());
					exportPanel.submit();
					controller.getIndexFieldView().getIndexFieldGridView().deSelectAllModels();
					controller.setSelectedFieldType(null);
					controller.getIndexFieldView().getIndexFieldGridView().refresh();
				} else {
					applyBatchClassBeforeOperation(LocaleDictionary.getConstantValue(BatchClassConstants.EXPORT_INDEX_FIELD));
				}
			} else {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SELECT_INDEX_FIELDS_TO_EXPORT), DialogIcon.WARNING);
			}
		}
	}

	public void applyBatchClassBeforeOperation(String operationName) {
		DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
				LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SAVE_PENDING_CHANGES_FIRST_TO_EXPORT_INDEX_FIELD), DialogIcon.INFO);
	}

	public void copyIndexField() {
		controller.getEventBus().fireEvent(new CopyIndexFieldsEvent());
	}
}
