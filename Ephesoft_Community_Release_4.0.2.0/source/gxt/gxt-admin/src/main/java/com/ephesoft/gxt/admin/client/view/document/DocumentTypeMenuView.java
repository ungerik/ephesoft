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

package com.ephesoft.gxt.admin.client.view.document;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AddDocumentTypeEvent;
import com.ephesoft.gxt.admin.client.event.BCMTreeMaskEvent;
import com.ephesoft.gxt.admin.client.event.CopyDocumentTypeEvent;
import com.ephesoft.gxt.admin.client.event.DeleteDocumentTypeEvent;
import com.ephesoft.gxt.admin.client.event.LearnFilesStartEvent;
import com.ephesoft.gxt.admin.client.event.RetrieveSelectedDocumentsListEvent;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.document.DocumentTypeMenuPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.view.BatchClassManagementMenuBar;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

public class DocumentTypeMenuView extends BatchClassInlineView<DocumentTypeMenuPresenter> {

	interface Binder extends UiBinder<Widget, DocumentTypeMenuView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	private static final String EXPORT_DOCUMENT_TYPE_DOWNLOAD = "gxt-admin/exportDocumentTypeDownload?";

	private static final String DOCUMENT_IDENTIFIER = "identifier";

	@UiField
	protected BatchClassManagementMenuBar bcmMenuBar;

	@UiField
	protected HorizontalPanel gridViewMainPanel;
	//
	protected CustomMenuItem learnFiles;

	protected CustomMenuItem add;

	protected CustomMenuItem delete;

	protected CustomMenuItem copyDocumentType;
	//
	protected CustomMenuItem exportDocumentType;
	//
	// protected MenuItem testClassification;

	@UiField
	protected ToggleButton testExtractionButton;

	@UiField
	protected ToggleButton testClassificationButton;

	@UiField
	protected ToggleButton viewLearnFilesButton;

	protected FormPanel exportFormPanel;

	protected FormPanel importFormPanel;

	/**
	 * @return the exportPresenter
	 */
	public FormPanel getExportFormPanel() {
		return exportFormPanel;
	}

	/**
	 * @param exportPresenter the exportPresenter to set
	 */
	public void setExportFormPanel(FormPanel exportFormPanel) {
		this.exportFormPanel = exportFormPanel;
	}

	/**
	 * @return the exportPresenter
	 */
	public FormPanel getImportFormPanel() {
		return importFormPanel;
	}

	/**
	 * @param exportPresenter the exportPresenter to set
	 */
	public void setImportFormPanel(FormPanel importFormPanel) {
		this.importFormPanel = importFormPanel;
	}

	public void addFormPanelEvents(final String identifier) {

		if (null != exportFormPanel) {
			gridViewMainPanel.add(exportFormPanel);
		}
		exportFormPanel.setMethod(FormPanel.METHOD_POST);

		exportFormPanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {

				exportFormPanel.setAction(EXPORT_DOCUMENT_TYPE_DOWNLOAD);
				final Hidden exportIdentifierHidden = new Hidden(DOCUMENT_IDENTIFIER);
				exportIdentifierHidden.setValue(identifier);
				exportFormPanel.add(exportIdentifierHidden);
			}
		});

		exportFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {

				if (event.getResults().toLowerCase().indexOf(AdminConstants.ERROR_CODE_TEXT) > -1) {

					return;
				}
			}
		});
	}

	public DocumentTypeMenuView() {
		super();
		initWidget(binder.createAndBindUi(this));
		intializeMenuItems();
		bcmMenuBar.addItem(add);
		bcmMenuBar.addItem(delete);
		bcmMenuBar.addItem(copyDocumentType);
		// bcmMenuBar.addItem(testClassification);
		bcmMenuBar.addItem(exportDocumentType);
		bcmMenuBar.addItem(learnFiles);
		addMenuItemActionEvents();
		addToggleButtonActionEvents();
		setWidgetIds();
		testExtractionButton.setStylePrimaryName("testExtractionTogglerButton");
		testClassificationButton.setStylePrimaryName("testExtractionTogglerButton");
		viewLearnFilesButton.setStyleName("testExtractionTogglerButton");
	}

	private void setWidgetIds() {
		WidgetUtil.setID(testExtractionButton, "bcm-documentMenu-testExtraction-button");
		WidgetUtil.setID(testClassificationButton, "bcm-documentMenu-testClassification-button");
	}

	private void addToggleButtonActionEvents() {
		viewLearnFilesButton.getUpFace().setText(LocaleDictionary.getConstantValue(BatchClassConstants.VIEW_LEARN_FILES));
		viewLearnFilesButton.getDownFace().setText(LocaleDictionary.getConstantValue(BatchClassConstants.CLOSE));
		viewLearnFilesButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				RetrieveSelectedDocumentsListEvent documentsListEvent = new RetrieveSelectedDocumentsListEvent();
				documentsListEvent.setViewLearnFiles(true);
				documentsListEvent.setViewLearnFileValue(event.getValue());
				BatchClassManagementEventBus.fireEvent(documentsListEvent);
			}

		});

		testExtractionButton.getUpFace().setText(LocaleDictionary.getConstantValue(BatchClassConstants.TEST_EXTRACTION_BUTTON));
		testExtractionButton.getDownFace().setText(LocaleDictionary.getConstantValue(BatchClassConstants.CLOSE));
		testExtractionButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if (event.getValue()) {
					enableAllOtherMenuItems(false);
					enableViewLearnFilesButton(false);
					BatchClassManagementEventBus.fireEvent(new BCMTreeMaskEvent(true));
					enableTestClassificationButton(false);
					presenter.showTestExtractionView();
				} else {
					presenter.closeDialogWindow();
					enableAllOtherMenuItems(true);
					enableTestClassificationButton(true);
					enableViewLearnFilesButton(true);
					BatchClassManagementEventBus.fireEvent(new BCMTreeMaskEvent(false));
				}
			}

		});
		testClassificationButton.getUpFace().setText(LocaleDictionary.getConstantValue(BatchClassConstants.TEST_CLASSIFICATION));
		testClassificationButton.getDownFace().setText(LocaleDictionary.getConstantValue(BatchClassConstants.CLOSE));
		testClassificationButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if (event.getValue()) {
					enableAllOtherMenuItems(false);
					enaableTestExtractionButton(false);
					enableViewLearnFilesButton(false);
					presenter.showTestClassificationView();
				} else {
					presenter.closeDialogWindow();
					enableAllOtherMenuItems(true);
					enaableTestExtractionButton(true);
					enableViewLearnFilesButton(true);
					BatchClassManagementEventBus.fireEvent(new BCMTreeMaskEvent(false));
				}
			}

		});
	}

	public void enableAllOtherMenuItems(boolean isEnable) {
		learnFiles.setEnabled(isEnable);
		add.setEnabled(isEnable);
		delete.setEnabled(isEnable);
		copyDocumentType.setEnabled(isEnable);
		// testClassification.setEnabled(isEnable);
		exportDocumentType.setEnabled(isEnable);
		bcmMenuBar.enableApplyMenuItem(isEnable);
		bcmMenuBar.enableOpenMenuItem(isEnable);
		bcmMenuBar.enableCloseMenuItem(isEnable);
		bcmMenuBar.enableWorkflowDeployMenuItem(isEnable);
	}

	private void addMenuItemActionEvents() {
		learnFiles.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				BatchClassManagementEventBus.fireEvent(new LearnFilesStartEvent(false));
			}
		});
		add.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				BatchClassManagementEventBus.fireEvent(new AddDocumentTypeEvent());
			}
		});
		delete.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				BatchClassManagementEventBus.fireEvent(new DeleteDocumentTypeEvent());
			}
		});
		copyDocumentType.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				BatchClassManagementEventBus.fireEvent(new CopyDocumentTypeEvent());

			}
		});

		exportDocumentType.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.exportDocumentType();
			}
		});
	}

	/**
	 * 
	 */
	@SuppressWarnings("serial")
	private void intializeMenuItems() {
		add = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.ADD_BUTTON);
				// return
				// LocaleDictionary.getLocaleDictionary().getConstantValue(DocumentTypeConstants.ADD_DOCUMENT_TYPE);
			}
		});
		copyDocumentType = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.COPY);
				// return
				// LocaleDictionary.getLocaleDictionary().getConstantValue(DocumentTypeConstants.COPY_DOCUMENT_TYPE);
			}
		});
		delete = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.DELETE);
				// return
				// LocaleDictionary.getLocaleDictionary().getConstantValue(DocumentTypeConstants.DELETE_DOCUMENT_TYPE);
			}
		});
		learnFiles = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.LEARN_FILES);
				// return
				// LocaleDictionary.getLocaleDictionary().getConstantValue(DocumentTypeConstants.VIEW_LEARN_FILES);
			}
		});
		// testClassification = new MenuItem(new SafeHtml() {
		//
		// @Override
		// public String asString() {
		// return "Test Classification";
		// // return
		// // LocaleDictionary.getLocaleDictionary().getConstantValue(DocumentTypeConstants.TEST_CLASSIFICATION);
		// }
		// });
		exportDocumentType = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.EXPORT);
				// return
				// LocaleDictionary.getLocaleDictionary().getConstantValue(DocumentTypeConstants.EXPORT_DOCUMENT_TYPE);
			}
		});
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public void enaableTestExtractionButton(boolean isEnabled) {
		testExtractionButton.setEnabled(isEnabled);
	}

	public void enableTestClassificationButton(boolean isEnabled) {
		testClassificationButton.setEnabled(isEnabled);
	}

	public void enableViewLearnFilesButton(boolean isEnabled) {
		viewLearnFilesButton.setEnabled(isEnabled);
	}

	public void setTestExtractionButtonDown(boolean isSet) {
		if (testExtractionButton.isDown()) {
			testExtractionButton.setValue(isSet, true);
		}
	}

	public void setTestClassificationButtonDown(boolean isSet) {
		if (testClassificationButton.isDown()) {
			testClassificationButton.setValue(isSet, true);
		}
	}

	public void enableRequiredMenuItem(boolean enable) {
		delete.setEnabled(enable);
		exportDocumentType.setEnabled(enable);
		// bcmMenuBar.enableOpenMenuItem(enable);

		learnFiles.setEnabled(enable);
		viewLearnFilesButton.setEnabled(enable);
		copyDocumentType.setEnabled(enable);
		testExtractionButton.setEnabled(enable);
		testClassificationButton.setEnabled(enable);
	}

	public void setViewLearnFileTogglerValue(boolean isDown, boolean fireEvent) {
		viewLearnFilesButton.setValue(isDown, fireEvent);
	}

	public void enableOpenMenuItem(boolean enable) {
		bcmMenuBar.enableOpenMenuItem(enable);
	}

}
