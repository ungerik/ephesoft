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

package com.ephesoft.gxt.admin.client.presenter.document;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.BatchClassManagementServiceAsync;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.BCMTreeMaskEvent;
import com.ephesoft.gxt.admin.client.event.BatchClassLoadEvent;
import com.ephesoft.gxt.admin.client.event.BatchClassManagementMenuItemEnableEvent;
import com.ephesoft.gxt.admin.client.event.ClearTestClassificationGridEvent;
import com.ephesoft.gxt.admin.client.event.ClearTestExtractionGridEvent;
import com.ephesoft.gxt.admin.client.event.ClearTestExtractionTreeEvent;
import com.ephesoft.gxt.admin.client.event.DialogWindowResizeEvent;
import com.ephesoft.gxt.admin.client.event.ExportSelectedDocumentsListEvent;
import com.ephesoft.gxt.admin.client.event.OpenMenuItemEnableEvent;
import com.ephesoft.gxt.admin.client.event.PopulateExtractionModulesListEvent;
import com.ephesoft.gxt.admin.client.event.RetrieveSelectedDocumentsListEvent;
import com.ephesoft.gxt.admin.client.event.TestClassificationClearMenuItemEnableEvent;
import com.ephesoft.gxt.admin.client.event.TestClassificationDownloadEnableEvent;
import com.ephesoft.gxt.admin.client.event.TestExtractionClearMenuItemEnableEvent;
import com.ephesoft.gxt.admin.client.event.ViewLearnFilesEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.document.DocumentTypeMenuView;
import com.ephesoft.gxt.admin.client.view.document.testclassification.TestClassificationLayout;
import com.ephesoft.gxt.admin.client.view.document.testextraction.TestExtractionlayout;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.theme.gray.client.window.GrayWindowAppearance;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;

public class DocumentTypeMenuPresenter extends BatchClassInlinePresenter<DocumentTypeMenuView> {

	public DocumentTypeMenuPresenter(BatchClassManagementController controller, DocumentTypeMenuView view) {
		super(controller, view);
	}

	interface CustomEventBinder extends EventBinder<DocumentTypeMenuPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	@Override
	public void bind() {
	}

	@EventHandler
	public void handleBatchClassLoadEvent(BatchClassLoadEvent loadEvent) {
		if (null != loadEvent) {
			BatchClassDTO loadedBatchClass = loadEvent.getLoadedBatchClass();
			if (null != loadedBatchClass) {
				Collection<DocumentTypeDTO> documents = loadedBatchClass.getDocuments();
				if (CollectionUtil.isEmpty(documents)) {
					view.enaableTestExtractionButton(false);
					view.enableTestClassificationButton(false);
				} else {
					if (documents.size() == 1) {
						for (DocumentTypeDTO dto : documents) {
							if (dto.getName().equalsIgnoreCase(BatchClassConstants.UNKNOWN_DOCUMENT_TYPE)) {
								view.enaableTestExtractionButton(false);
								view.enableTestClassificationButton(false);
							}
						}
					} else {
						view.enaableTestExtractionButton(true);
						view.enableTestClassificationButton(true);
					}
				}
				ScreenMaskUtility.unmaskScreen();
			}
		}
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	private DialogWindow testExtractionDialogWindow;

	private DialogWindow testClassificationDialogWindow;

	/**
	 * @return the rpcService
	 */
	public BatchClassManagementServiceAsync getRpcService() {
		return rpcService;
	}

	public void exportDocumentType() {
		RetrieveSelectedDocumentsListEvent documentsListEvent = new RetrieveSelectedDocumentsListEvent();
		documentsListEvent.setExport(true);
		BatchClassManagementEventBus.fireEvent(documentsListEvent);
	}

	@EventHandler
	public void handleExportDocumentType(ExportSelectedDocumentsListEvent exportSelectedDocuments) {
		if (null != exportSelectedDocuments && !CollectionUtil.isEmpty(exportSelectedDocuments.getDocumentTypeDTOList())) {
			if (!controller.getSelectedBatchClass().isDirty()) {
				List<DocumentTypeDTO> documentList = exportSelectedDocuments.getDocumentTypeDTOList();
				StringBuilder identifierList = new StringBuilder();
				for (DocumentTypeDTO docTypeDTO : documentList) {
					identifierList.append(docTypeDTO.getIdentifier() + ";");
				}
				final DialogWindow dialogWindow = new DialogWindow();
				WidgetUtil.setID(dialogWindow, "exportDocumentType_view");

				final FormPanel exportPanel = new FormPanel();

				view.setExportFormPanel(exportPanel);
				view.addFormPanelEvents(identifierList.toString());
				exportPanel.submit();
				controller.getDocumentTypeView().getDocumentTypeGridView().deSelectAllModels();
				controller.setSelectedDocumentType(null);
				exportSelectedDocuments.setDocumentTypeDTOList(null);
				controller.getDocumentTypeView().getDocumentTypeGridView().refresh();
			} else {
				applyBatchClassBeforeOperation(LocaleDictionary.getConstantValue(BatchClassConstants.EXPORT_DOCUMENT_TYPE));
			}
		} else {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.SELECT_AT_LEAST_ONE_DOCUMENT_TYPE_TO_EXPORT), DialogIcon.WARNING);
		}
	}

	public void applyBatchClassBeforeOperation(String operationName) {
		DialogUtil.showMessageDialog(operationName, LocaleDictionary.getMessageValue(BatchClassMessages.PLEASE_SAVE_PENDING_CHANGES_FIRST), DialogIcon.INFO);
	}

	public void showTestClassificationView() {
		if (controller.getSelectedBatchClass().isDirty()) {
			applyBatchClassBeforeOperation(LocaleDictionary.getConstantValue(BatchClassConstants.TEST_CLASSIFICATION));
			view.enableAllOtherMenuItems(true);
			view.setTestClassificationButtonDown(false);
		} else {
			testClassificationDialogWindow = new DialogWindow(new GrayWindowAppearance(), true, controller.getTestClassifyTypeView().getClassificationTypesComboBox());
			testClassificationDialogWindow.addStyleName("testClassificationDialogWindow");
			testClassificationDialogWindow.addStyleName("testExtractionDialogWindow");
			BatchClassManagementEventBus.fireEvent(new ClearTestClassificationGridEvent());
			BatchClassManagementEventBus.fireEvent(new TestClassificationDownloadEnableEvent(false));
			BatchClassManagementEventBus.fireEvent(new TestClassificationClearMenuItemEnableEvent());
			TestClassificationLayout testClassificationLayout = controller.getTestClassificationLayout();
			
			testClassificationDialogWindow.setFocusWidget(controller.getTestClassifyTypeView().getClassificationTypesComboBox());
			
			testClassificationDialogWindow.add(testClassificationLayout);
			testClassificationDialogWindow.setBorders(false);
			testClassificationDialogWindow.setModal(false);
			testClassificationDialogWindow.setWidth(getOffsetWidth());
			final Element element = testClassificationDialogWindow.getElement();
			final Style style = element.getStyle();
			style.setHeight(getOffsetHeight(), Unit.PX);
			// testClassificationDialogWindow.setHeight(getOffsetHeight());
			testClassificationDialogWindow.setPosition(getAbsoluteLeft(), getAbsoluteTop());
			testClassificationDialogWindow.setOnEsc(false);
			testClassificationDialogWindow.setResizable(false);
			testClassificationDialogWindow.setClosable(false);
			testClassificationDialogWindow.setDraggable(false);
			testClassificationDialogWindow.setHeaderVisible(false);
			testClassificationDialogWindow.setPredefinedButtons();
			controller.resizeGrid(getOffsetHeight(), getOffsetWidth());
			BatchClassManagementEventBus.fireEvent(new BCMTreeMaskEvent(true));
			testClassificationDialogWindow.show();
		}
	}

	public void showTestExtractionView() {
		if (controller.getSelectedBatchClass().isDirty()) {
			applyBatchClassBeforeOperation(LocaleDictionary.getConstantValue(BatchClassConstants.TEST_EXTRACTION));
			view.enableAllOtherMenuItems(true);
			view.setTestExtractionButtonDown(false);
		} else {
			BatchClassManagementEventBus.fireEvent(new TestExtractionClearMenuItemEnableEvent());
			BatchClassManagementEventBus.fireEvent(new ClearTestExtractionGridEvent());
			BatchClassManagementEventBus.fireEvent(new ClearTestExtractionTreeEvent());
			getBatchClassExtractionModules();
		}
	}

	private void getBatchClassExtractionModules() {
		String batchClassID = controller.getSelectedBatchClass().getIdentifier();
		controller.getRpcService().getBatchClassExtractionPlugins(batchClassID, new AsyncCallback<List<String>>() {

			@Override
			public void onSuccess(List<String> batchClassExtractionModules) {
				if (!CollectionUtil.isEmpty(batchClassExtractionModules)) {
					controller.setBatchClassExtractionModules(batchClassExtractionModules);
					testExtractionDialogWindow = new DialogWindow(new GrayWindowAppearance(), true, controller.getTestExtractionClassificationTypesView().getExtractionModulesList() );
					testExtractionDialogWindow.addStyleName("testExtractionDialogWindow");
					testExtractionDialogWindow.setFocusWidget(controller.getTestExtractionClassificationTypesView().getClassificationTypesComboBox());
					testExtractionDialogWindow.setBorders(false);
					TestExtractionlayout testExtractionlayout = controller.getTestExtractionLayout();
					BatchClassManagementEventBus.fireEvent(new PopulateExtractionModulesListEvent());
					testExtractionDialogWindow.add(testExtractionlayout);
					testExtractionDialogWindow.setModal(false);
					// final Element element = testExtractionDialogWindow.getElement();
					// final Style style = element.getStyle();
					// style.setHeight(getOffsetHeight(), Unit.PX);
					testExtractionDialogWindow.setHeight(getOffsetHeight());
					testExtractionDialogWindow.setWidth(getOffsetWidth());
					testExtractionDialogWindow.setPosition(getAbsoluteLeft(), getAbsoluteTop());
					testExtractionDialogWindow.setOnEsc(false);
					testExtractionDialogWindow.setResizable(false);
					testExtractionDialogWindow.setClosable(false);
					testExtractionDialogWindow.setDraggable(false);
					testExtractionDialogWindow.setHeaderVisible(false);
					testExtractionDialogWindow.setPredefinedButtons();
					testExtractionDialogWindow.show();
					controller.setBorderLayoutContainer(getOffsetHeight(), getOffsetWidth());
				} else {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.NO_EXTRACTION_MODULE_EXIST), DialogIcon.ERROR);
					view.enableAllOtherMenuItems(true);
					view.setTestExtractionButtonDown(false);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.NO_EXTRACTION_MODULE_EXIST), DialogIcon.ERROR);
				view.enableAllOtherMenuItems(true);
				view.setTestExtractionButtonDown(false);
			}
		});
	}

	@EventHandler
	public void beforeDialogWindowResize(DialogWindowResizeEvent beforeResize) {
		if (null != testExtractionDialogWindow && testExtractionDialogWindow.isVisible()) {
			testExtractionDialogWindow.setResizable(true);
			// final Element element = testExtractionDialogWindow.getElement();
			// final Style style = element.getStyle();
			// style.setHeight(getOffsetHeight(), Unit.PX);
			testExtractionDialogWindow.setHeight(getOffsetHeight());
			testExtractionDialogWindow.setWidth(getOffsetWidth());
			testExtractionDialogWindow.setPosition(getAbsoluteLeft(), getAbsoluteTop());
			controller.setBorderLayoutContainer(getOffsetHeight(), getOffsetWidth());
			testExtractionDialogWindow.setResizable(false);
		}
		if (null != testClassificationDialogWindow && testClassificationDialogWindow.isVisible()) {
			testClassificationDialogWindow.setResizable(true);
			final Element element = testClassificationDialogWindow.getElement();
			final Style style = element.getStyle();
			style.setHeight(getOffsetHeight(), Unit.PX);
			// testClassificationDialogWindow.setHeight(getOffsetHeight());
			testClassificationDialogWindow.setWidth(getOffsetWidth());
			testClassificationDialogWindow.setPosition(getAbsoluteLeft(), getAbsoluteTop());
			testClassificationDialogWindow.setResizable(false);
			controller.resizeGrid(getOffsetHeight(), getOffsetWidth());
		}
	}

	private int getOffsetHeight() {
		int listViewHeight = controller.getListPanelOffsetHeight();
		int bottomPanelHeight = controller.getBottomPanelAbsoluteHeight();
		int dialogWindowHeight = listViewHeight + bottomPanelHeight;
		return dialogWindowHeight;
	}

	private int getOffsetWidth() {
		return controller.getListPanelAbsoluteWidth();
	}

	private int getAbsoluteLeft() {
		return controller.getListPanelAbsoluteLeft();
	}

	private int getAbsoluteTop() {
		return controller.getListPanelAbsoluteTop();
	}

	public void closeDialogWindow() {
		if (null != testExtractionDialogWindow) {
			testExtractionDialogWindow.hide();
		}
		if (null != testClassificationDialogWindow) {
			testClassificationDialogWindow.hide();
		}
	}

	public DialogWindow getDialogWindow() {
		return testExtractionDialogWindow;
	}

	public void setDialogWindow(DialogWindow dialogWindow) {
		this.testExtractionDialogWindow = dialogWindow;
	}

	@EventHandler
	public void enableOpenMenuItem(OpenMenuItemEnableEvent event) {
		if (event.getPropertyAccessModel().equals(PropertyAccessModel.DOCUMENT_TYPE)) {
			// view.enableOpenDeleteMenuItem(event.isEnable());
			view.enableOpenMenuItem(event.isEnable());
		}
	}

	@EventHandler
	public void enableMenuItems(BatchClassManagementMenuItemEnableEvent event) {
		if (event.getPropertyAccessModel().equals(PropertyAccessModel.DOCUMENT_TYPE)) {
			view.enableRequiredMenuItem(event.isEnable());
		}
	}

	@EventHandler
	public void onViewLearnFiles(ViewLearnFilesEvent learnFilesEvent) {
		if (learnFilesEvent.isViewValid()) {
			view.enableAllOtherMenuItems(!learnFilesEvent.isLearnFileViewShow());
			view.enaableTestExtractionButton(!learnFilesEvent.isLearnFileViewShow());
			view.enableTestClassificationButton(!learnFilesEvent.isLearnFileViewShow());
		} else {
			view.setViewLearnFileTogglerValue(false, false);
		}
	}

}
