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

package com.ephesoft.gxt.admin.client.view.document.testextraction;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.document.testextraction.TestExtractionMenuPresenter;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

public class TestExtractionMenuView extends View<TestExtractionMenuPresenter> {

	interface Binder extends UiBinder<Widget, TestExtractionMenuView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected HorizontalLayoutContainer buttonContainer;

	@UiField
	protected CustomMenuBar menuBar;

	private CustomMenuItem extractMenuItem;
	
	private CustomMenuItem downloadMenuItem;

	private CustomMenuItem clearMenuItem;

	private FormPanel downloadFormPanel;

	/**
	 * the action URL on the submit of form panel.
	 */
	private static final String DOWNLOAD_ACTION = "../gxt-admin/downloadTestExtractionResult?";

	public TestExtractionMenuView() {
		super();
		initWidget(binder.createAndBindUi(this));
		downloadFormPanel = new FormPanel();
		this.buttonContainer.add(downloadFormPanel);
		this.buttonContainer.add(menuBar);
		menuBar.addStyleName("testExtractionMenuBar");
		buttonContainer.addStyleName("testExtractionButtonContainer");
		setWidgetIds();

		extractMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.EXTRACT_BUTTON);
			}
		});
		
		downloadMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.DOWNLOAD);
//				return LocaleDictionary.getConstantValue(BatchClassConstants.EXTRACT_BUTTON);
			}
		});

		clearMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.CLEAR_BUTTON);
			}
		});
		menuBar.addItem(extractMenuItem);
		menuBar.addItem(downloadMenuItem);
		menuBar.addItem(clearMenuItem);
		extractMenuItem.addStyleName("extractMenuItem");
		clearMenuItem.addStyleName("clearMenuItem");
		downloadMenuItem.addStyleName("downloadMenuItem");
		menuBar.setFocusOnHoverEnabled(false);
		intializeSelectionHandlers();
	}

	private void intializeSelectionHandlers() {
		extractMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onExtract();
			}
		});
		
		downloadMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				String downloadAction = presenter.getDownloadUrl(DOWNLOAD_ACTION);
				downloadFormPanel.setAction(downloadAction);
				Window.open(downloadAction, "_blank", null);
				//presenter.onDownload();
			}
		});

		clearMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onClear();
			}
		});

	}

	private void setWidgetIds() {
		WidgetUtil.setID(clearMenuItem, "bcm-testExtraction-clear-button");
		WidgetUtil.setID(downloadMenuItem, "bcm-testExtraction-download-button");
		WidgetUtil.setID(extractMenuItem, "bcm-testExtraction-extract-button");
	}

	@Override
	public void initialize() {

	}

	public void enableClearMenuItem(boolean isEnable) {
		clearMenuItem.setEnabled(isEnable);
	}

	
	private void setFormPanel() {
		downloadFormPanel.setMethod(FormPanel.METHOD_POST);
		downloadFormPanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(final SubmitEvent event) {
			}
		});

		downloadFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(final SubmitCompleteEvent event) {
				if (event.getResults().toLowerCase().indexOf(BatchClassConstants.ERROR_CODE_TEXT) > -1) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.DOWNLOAD_UNSUCCESSFUL_PLEASE_CHECK_LOGS_FOR_MORE_INFORMATION), DialogIcon.ERROR);
					return;
				}
			}
		});

	}
	
	public void submitExportFormPanel() {
		this.downloadFormPanel.submit();
	}
	
	public void enableDownloadMenuItem(boolean isEnable) {
		downloadMenuItem.setEnabled(isEnable);
	}
}
