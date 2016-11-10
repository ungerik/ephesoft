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

package com.ephesoft.gxt.admin.client.presenter.batchclass;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.ReloadBatchClassEvent;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.view.batchclass.KeyGeneratorView;
import com.ephesoft.gxt.core.client.BasePresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Presenter for Key Generator view.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see KeyGeneratorView
 * 
 */
public class KeyGeneratorPresenter<V extends KeyGeneratorView> extends BasePresenter<BatchClassManagementController, V> {

	public KeyGeneratorPresenter(final BatchClassManagementController controller, final V view) {
		super(controller, view);
	}

	@Override
	public void bind() {
	}

	@Override
	public void injectEvents(EventBus eventBus) {
	}

	public void showKeyGeneratorView() {
		view.getDialogWindow().add(view);
		view.getDialogWindow().center();
		view.getDialogWindow().show();
		view.getDialogWindow().setHeadingText(LocaleDictionary.getConstantValue(BatchClassConstants.KEY_GENERATOR_LABEL));
	}

	/**
	 * Generate the key with provided algorithm and batch class key for batch class identifier.
	 * 
	 * @param batchClassKey {@link String}
	 * @param selectedEncryptionAlgo
	 * @param batchClassIdentifier
	 */
	public void generateKey(String batchClassKey, String selectedEncryptionAlgo, String batchClassIdentifier) {
		if (AdminConstants.EMPTY_STRING.equals(batchClassKey) && !AdminConstants.ENCRYPTION_ALGO_NONE.equals(selectedEncryptionAlgo)) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.MANDATORY_FIELDS_ERROR_MSG), DialogIcon.ERROR);
		} else {
			view.getDialogWindow().hide();
			ScreenMaskUtility.maskScreen();
			controller.getRpcService().generateBatchClassLevelKey(batchClassKey, selectedEncryptionAlgo, batchClassIdentifier,
					new AsyncCallback<Integer>() {

						@Override
						public void onFailure(Throwable caught) {
							// error message showing the key cannot be generated.
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.PROBLEM_GENERATING_KEY), DialogIcon.ERROR);
						}

						@Override
						public void onSuccess(Integer result) {
							ScreenMaskUtility.unmaskScreen();
							if (result == 0) {
								Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.KEY_GENERATOR_LABEL),
										LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_KEY_SUCCESS));
							} else {
								Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.KEY_GENERATOR_LABEL),
										LocaleDictionary.getMessageValue(BatchClassMessages.FUZZY_INDEX_ERROR));
							}
							// refresh batch class management screen.
							controller.getEventBus().fireEvent(new ReloadBatchClassEvent());
						}
					});
		}
	}
}
