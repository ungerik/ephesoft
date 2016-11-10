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

package com.ephesoft.gxt.admin.client.view.document.testclassification;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.document.testclassification.TestClassificationMenuPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

public class TestClassificationMenuView extends BatchClassInlineView<TestClassificationMenuPresenter> {

	interface Binder extends UiBinder<Widget, TestClassificationMenuView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected HorizontalLayoutContainer buttonContainer;

	@UiField
	protected CustomMenuBar menuBar;

	private CustomMenuItem downloadMenuItem;

	private CustomMenuItem classifyMenuItem;

	private CustomMenuItem clearMenuItem;

	private FormPanel downloadFormPanel;

	/**
	 * the action URL on the submit of form panel.
	 */
	private static final String DOWNLOAD_ACTION = "gxt-admin/downloadTestContentResult?";

	public TestClassificationMenuView() {
		super();
		initWidget(binder.createAndBindUi(this));
		downloadFormPanel = new FormPanel();
		buttonContainer.add(downloadFormPanel);
		this.buttonContainer.add(menuBar);
		initialiseMenuItems();
		menuBar.addItem(classifyMenuItem);
		menuBar.addItem(downloadMenuItem);
		menuBar.addItem(clearMenuItem);
		menuBar.setFocusOnHoverEnabled(false);
		intializeSelectionHandlers();
		setWidgetIds();
		setFormPanel();
		buttonContainer.addStyleName("testClassifcationButtonContainer");
		menuBar.addStyleName("testClassifcationMenuBar");
		downloadMenuItem.addStyleName("downloadMenuItem");
		classifyMenuItem.addStyleName("classifyMenuItem");
		clearMenuItem.addStyleName("clearMenuItem");
	}

	private void initialiseMenuItems() {
		downloadMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.DOWNLOAD);
			}
		});

		clearMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.CLEAR_BUTTON);
			}
		});
		classifyMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return "Classify";
			}
		});
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

	private void intializeSelectionHandlers() {
		downloadMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				String downloadAction = presenter.getDownloadUrl(DOWNLOAD_ACTION);
				downloadFormPanel.setAction(downloadAction);
				presenter.onDownload();
			}
		});

		clearMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onClear();
			}
		});

		classifyMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onClassify();
			}
		});

	}

	private void setWidgetIds() {
		WidgetUtil.setID(clearMenuItem, "bcm-testClassification-clear-button");
		WidgetUtil.setID(downloadMenuItem, "bcm-testClassification-download-button");
		WidgetUtil.setID(classifyMenuItem, "bcm-testClassification-classify-button");

	}

	@Override
	public void initialize() {

	}

	public void enableClearMenuItem(boolean isEnable) {
		clearMenuItem.setEnabled(isEnable);
	}

	public void enableDownloadMenuItem(boolean isEnable) {
		downloadMenuItem.setEnabled(isEnable);
	}

	public void submitExportFormPanel() {
		this.downloadFormPanel.submit();
	}

}
