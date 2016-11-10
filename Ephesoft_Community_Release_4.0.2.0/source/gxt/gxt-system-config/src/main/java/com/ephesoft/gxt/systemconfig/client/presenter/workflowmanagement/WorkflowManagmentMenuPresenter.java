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

package com.ephesoft.gxt.systemconfig.client.presenter.workflowmanagement;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.DeleteSelectedDependenciesEvent;
import com.ephesoft.gxt.systemconfig.client.event.ShowAddPluginDependenciesViewEvent;
import com.ephesoft.gxt.systemconfig.client.event.ShowEditPluginDependenciesViewEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigInlinePresenter;
import com.ephesoft.gxt.systemconfig.client.view.workflowmanagement.WorkflowManagmentMenuView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;

public class WorkflowManagmentMenuPresenter extends SystemConfigInlinePresenter<WorkflowManagmentMenuView> {

	interface CustomEventBinder extends EventBinder<WorkflowManagmentMenuPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public WorkflowManagmentMenuPresenter(SystemConfigController controller, WorkflowManagmentMenuView view) {
		super(controller, view);
	}

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void showAddPluginDependencyView() {
		controller.getEventBus().fireEvent(new ShowAddPluginDependenciesViewEvent());
	}

	public void showEditPluginDependencyView() {
		controller.getEventBus().fireEvent(new ShowEditPluginDependenciesViewEvent());
	}

	public void deleteSelectedPluginDependency() {
		controller.getEventBus().fireEvent(new DeleteSelectedDependenciesEvent());
	}

	public void onHelpClicked() {
		String helpContent = createHelpContent();
		final ConfirmationDialog confirmationDialog = new ConfirmationDialog(
				LocaleDictionary.getConstantValue(SystemConfigConstants.HELP_BUTTON), helpContent);
		confirmationDialog.setDialogListener(new DialogAdapter() {

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
			}

			@Override
			public void onCloseClick() {
				confirmationDialog.hide();

			}

		});
		confirmationDialog.addStyleName("helpSC");
		confirmationDialog.setPredefinedButtons(PredefinedButton.OK);
		// confirmationDialog.setPerformCancelOnEscape(true);
		confirmationDialog.show();
		confirmationDialog.center();
		// confirmationDialog.okButton.setFocus(true);
		// confirmationDialog.cancelButton.setVisible(false);
		// confirmationDialog.getPanel().getElementById(SystemConfigConstants.TEXT_PANEL).addClassName(
		// SystemConfigConstants.HELP_CONTENT);

	}

	public String createHelpContent() {

		String pluginAddingStepsLocaleMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.PLUGIN_ADDING_STEPS);
		String makeLocaleMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.MAKE);
		String pluginTwoFilesLocaleMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.PLUGIN_HAVING_TWO_FILES);
		String jarDescriptionLocaleMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.JAR_FOR_THE_PLUGIN_TO_BE_ADDED);
		String xmlDescriptionLocaleMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.FILE_INFORMATION_ABOUT_THE_PLUGIN);
		String zipContentLocaleMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.FILE_CONTAIN_TWO_FILES);
		String fileKeywordLocaleMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.FILE_AND);
		String sameNameLocaleMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.FILE_SAME_NAME);
		String jarContentWarningLocaleMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.FILE_CANNOT_BE_VERIFIED_WARNING);
		String jarRequiredContentLocaleMessage = LocaleDictionary.getMessageValue(SystemConfigMessages.REQUIRED_CONTENT);
		String tomcatRestartWarning = LocaleDictionary.getMessageValue(SystemConfigMessages.RESTART_TOMCAT_WARNING);

		String keyBoardShortcuts = "<p><b>" + pluginAddingStepsLocaleMessage + "</b></p>" + "<ol>" + SystemConfigConstants.HTML_LI
				+ makeLocaleMessage + SystemConfigConstants.BREAK_TAG_START + SystemConfigConstants.ZIP_FILE
				+ SystemConfigConstants.BREAK_TAG_END + pluginTwoFilesLocaleMessage + SystemConfigConstants.HTML_LI_END + "<ol>"
				+ " <li type=\"a\">" + SystemConfigConstants.BREAK_TAG_START + SystemConfigConstants.JAR_FILE
				+ SystemConfigConstants.BREAK_TAG_END + jarDescriptionLocaleMessage + SystemConfigConstants.HTML_LI_END
				+ " <li type=\"a\">" + SystemConfigConstants.BREAK_TAG_START + SystemConfigConstants.XML_FILE
				+ SystemConfigConstants.BREAK_TAG_END + xmlDescriptionLocaleMessage + SystemConfigConstants.HTML_LI_END + "</ol>"
				+ SystemConfigConstants.HTML_LI + SystemConfigConstants.BREAK_TAG_START + SystemConfigConstants.ZIP_FILE
				+ SystemConfigConstants.BREAK_TAG_END + zipContentLocaleMessage + SystemConfigConstants.HTML_LI_END
				+ SystemConfigConstants.HTML_LI + SystemConfigConstants.BREAK_TAG_START + SystemConfigConstants.ZIP_FILE
				+ SystemConfigConstants.BREAK_TAG_END + fileKeywordLocaleMessage + SystemConfigConstants.BREAK_TAG_START
				+ SystemConfigConstants.JAR_FILE + SystemConfigConstants.BREAK_TAG_END + sameNameLocaleMessage
				+ SystemConfigConstants.HTML_LI_END + SystemConfigConstants.HTML_LI + SystemConfigConstants.BREAK_TAG_START
				+ SystemConfigConstants.JAR_FILE + SystemConfigConstants.BREAK_TAG_END + jarContentWarningLocaleMessage
				+ jarRequiredContentLocaleMessage + SystemConfigConstants.HTML_LI_END + SystemConfigConstants.HTML_LI
				+ tomcatRestartWarning + SystemConfigConstants.HTML_LI_END + "</ol>";

		return keyBoardShortcuts;
	}
}
