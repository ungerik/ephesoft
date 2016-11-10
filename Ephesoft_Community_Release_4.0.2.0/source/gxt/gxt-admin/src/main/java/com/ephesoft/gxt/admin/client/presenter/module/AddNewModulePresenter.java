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

package com.ephesoft.gxt.admin.client.presenter.module;

import java.util.Set;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.view.module.AddNewModuleView;
import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.validator.form.ListContainsValueValidator;
import com.ephesoft.gxt.core.shared.dto.ModuleDTO;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Timer;

/**
 * 
 * @author Ephesoft
 *
 */
/**
 * Presenter for copying a batch class.
 */
public class AddNewModulePresenter<V extends AddNewModuleView> extends BasePresenter<BatchClassManagementController, V> {

	private ModuleDTO newModuleDTO;
	private ListContainsValueValidator listValidator = new ListContainsValueValidator(
			LocaleDictionary.getMessageValue(BatchClassMessages.MODULE_ALREADY_PRESENT), getModuleNames());

	/**
	 * @return the moduleDTO
	 */
	public ModuleDTO getModuleDTO() {
		return newModuleDTO;
	}

	/**
	 * @param moduleDTO the moduleDTO to set
	 */
	public void setModuleDTO(ModuleDTO moduleDTO) {
		this.newModuleDTO = moduleDTO;
	}

	public AddNewModulePresenter(final BatchClassManagementController controller, final V view) {
		super(controller, view);
		view.addValidators();
	}

	@Override
	public void bind() {
		if (null != listValidator) {
			listValidator.setListOfString(controller.getModulePresenter().getAvailableModules());
		}
	}

	public void showAddNewModuleView() {
		view.getDialogWindow().add(view);
		view.getDialogWindow().setHeadingText(LocaleDictionary.getConstantValue(BatchClassConstants.ADD_NEW_MODULE_LABEL));
		view.getDialogWindow().setWidth("290px");
		view.getDialogWindow().setFocusWidget(view.getNameTextBox());
		view.getDialogWindow().show();
		view.getDialogWindow().center();
		Timer timer = new Timer() {

			@Override
			public void run() {
				view.getNameTextBox().clearInvalid();
			}
		};
		timer.schedule(20);
	}

	public void onOkClicked() {
		String moduleName = view.getName();
		String moduleDescription = view.getDescription();
		newModuleDTO = new ModuleDTO();
		newModuleDTO.setName(moduleName);
		newModuleDTO.setDescription(moduleDescription);
		if (view.getNameTextBox().isValid() && view.getDescriptionTextBox().isValid()) {
			ScreenMaskUtility.maskScreen(LocaleDictionary.getConstantValue(BatchClassConstants.ADDING_NEW_MODULE));
			controller.getRpcService().createNewModule(newModuleDTO, new AsyncCallback<ModuleDTO>() {

				@Override
				public void onSuccess(ModuleDTO moduleDTO) {
					view.getDialogWindow().hide();
					ScreenMaskUtility.unmaskScreen();
					Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.ADD_NEW_MODULE_LABEL),
							LocaleDictionary.getMessageValue(BatchClassMessages.MODULE_ADDED_SUCCESFULLY));
					String newModuleName = moduleDTO.getName();
					controller.getModulePresenter().getModuleConfigurePresenter().addNewModuleToModuleMap(newModuleName, moduleDTO);
					controller.getModulePresenter().getModuleConfigurePresenter().populateDualList(true);
				}

				@Override
				public void onFailure(Throwable throwable) {
					ScreenMaskUtility.unmaskScreen();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_ADDING_NEW_MODULE), DialogIcon.ERROR);
				}
			});
		}
	}

	@Override
	public void injectEvents(EventBus eventBus) {

	}

	public Set<String> getModuleNames() {
		return controller.getModulePresenter().getAvailableModules();
	}

	public ListContainsValueValidator getListValidator() {
		return listValidator;
	}

}
