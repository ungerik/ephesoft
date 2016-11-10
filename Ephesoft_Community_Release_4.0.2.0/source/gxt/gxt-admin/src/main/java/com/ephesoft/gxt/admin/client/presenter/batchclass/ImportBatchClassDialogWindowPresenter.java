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

import java.util.List;

import com.ephesoft.gxt.admin.client.BatchClassManagementServiceAsync;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.BatchClassImportEndEvent;
import com.ephesoft.gxt.admin.client.event.BatchClassImportStartEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.batchclass.ImportBatchClassDialogWindowView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.BatchClassUtil;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.ImportBatchClassUserOptionDTO;
import com.ephesoft.gxt.core.shared.dto.UNCFolderConfig;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class ImportBatchClassDialogWindowPresenter extends BatchClassInlinePresenter<ImportBatchClassDialogWindowView> {

	public ImportBatchClassDialogWindowPresenter(BatchClassManagementController controller, ImportBatchClassDialogWindowView view) {
		super(controller, view);
	}

	private ImportBatchClassUserOptionDTO importBatchClassUserOptionsDTO;

	private List<BatchClassDTO> allBatchClasses;

	interface CustomEventBinder extends EventBinder<ImportBatchClassDialogWindowPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	@Override
	public void bind() {
		getAllBatchClassesList();
		if (null == controller.getNotAllowedBatchClassCharacters()) {
			controller.getRpcService().getNotAllowedCharactersInBCName(new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable arg0) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.WARNING_TITLE),
							LocaleDictionary
									.getMessageValue(BatchClassMessages.ERROR_WHILE_FETCHING_CHARACTER_LIST_FOR_BATCH_CLASS_NAME),
							DialogIcon.WARNING);
				}

				@Override
				public void onSuccess(String notAllowedBatchClassCharacters) {
					controller.setNotAllowedBatchClassCharacters(notAllowedBatchClassCharacters);
				}
			});
		}
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	/**
	 * @return the rpcService
	 */
	public BatchClassManagementServiceAsync getRpcService() {
		return rpcService;
	}

	@EventHandler
	public void handleBatchClassImportEvent(BatchClassImportStartEvent batchClassImportStartEvent) {
		if (null != batchClassImportStartEvent) {
			importBatchClassUserOptionsDTO = batchClassImportStartEvent.getImportBatchClassUserOptionsDTO();
			if (view.isValid()) {
				rpcService.getAllBatchClasses(new AsyncCallback<List<BatchClassDTO>>() {

					@Override
					public void onSuccess(List<BatchClassDTO> result) {
						if (getBatchClassByName(result, view.getBatchClassName(), view.getUNCPathToIgnore()) == null) {
							if (getBatchClassByUNCPath(result, view.getUNCFolderPath(), view.getUNCPathToIgnore()) == null) {
								importBatchClass();
							} else {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.UNC_PATH_NOT_UNIQUE));
							}
						} else {
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_NAME_SHOULD_BE_UNIQUE));
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_CANNNOT_BE_IMPORTED));
					}
				});
			}
		}
	}

	private void importBatchClass() {
		if (null != importBatchClassUserOptionsDTO) {
			ScreenMaskUtility.maskScreen();
			importBatchClassUserOptionsDTO.setDescription(view.getBatchClassDescription());
			importBatchClassUserOptionsDTO.setImportExisting(view.isImportExistingUNC());
			importBatchClassUserOptionsDTO.setEncryptionAlgo(null);
			importBatchClassUserOptionsDTO.setName(view.getBatchClassName());
			importBatchClassUserOptionsDTO.setPriority(view.getPriority());
			importBatchClassUserOptionsDTO.setUncFolder(view.getUNCFolderPath());
			importBatchClassUserOptionsDTO.setUiConfigRoot(view.getUiConfigNode());
			importBatchClassUserOptionsDTO.setImportConnections(view.isImportConnections());
			rpcService.importBatchClass(importBatchClassUserOptionsDTO, false, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					ScreenMaskUtility.unmaskScreen();
					if (caught.getMessage().equals(BatchClassMessages.ERROR_CODE_1)
							|| caught.getMessage().equals(BatchClassMessages.AN_ERROR_OCCURED_WHILE_DEPLOYING_THE_WORKFLOW)) {
						StringBuilder message = new StringBuilder();
						message.append(LocaleDictionary.getMessageValue(caught.getMessage()));
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								message.toString(), DialogIcon.ERROR);

					} else {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_WHILE_IMPORTING_BATCH), DialogIcon.ERROR);
					}

					// DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					// LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_WHILE_IMPORTING_BATCH), DialogIcon.ERROR);
					BatchClassManagementEventBus.fireEvent(new BatchClassImportEndEvent());
				}

				@Override
				public void onSuccess(Boolean result) {
					ScreenMaskUtility.unmaskScreen();
					if (result != null && result == true) {
						Message.display(LocaleDictionary.getConstantValue(BatchClassConstants.SUCCESS_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_IMPORTED_SUCCESSFULLY));
						BatchClassManagementEventBus.fireEvent(new BatchClassImportEndEvent());
					} else {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_WHILE_IMPORTING_BATCH), DialogIcon.ERROR);
						BatchClassManagementEventBus.fireEvent(new BatchClassImportEndEvent());
					}
				}
			});
		}
	}

	private BatchClassDTO getBatchClassByName(final List<BatchClassDTO> batchClassList, String name, String uncPathToIgnore) {
		BatchClassDTO batchClassDTO = null;
		if (!CollectionUtil.isEmpty(batchClassList) && !StringUtil.isNullOrEmpty(name)) {
			for (final BatchClassDTO traversedBatchClass : batchClassList) {
				if (null != traversedBatchClass && name.equalsIgnoreCase(traversedBatchClass.getName())) {
					String batchClassUNCFolderPath = traversedBatchClass.getUncFolder();
					if (!StringUtil.isNullOrEmpty(batchClassUNCFolderPath)
							&& !batchClassUNCFolderPath.equalsIgnoreCase(uncPathToIgnore)) {
						batchClassDTO = traversedBatchClass;
						break;
					}

				}
			}
		}
		return batchClassDTO;
	}

	private BatchClassDTO getBatchClassByUNCPath(final List<BatchClassDTO> batchClassList, String uncPath, String uncPathToIgnore) {
		BatchClassDTO batchClassDTO = null;
		if (!CollectionUtil.isEmpty(batchClassList) && !StringUtil.isNullOrEmpty(uncPath)) {
			for (final BatchClassDTO traversedBatchClass : batchClassList) {
				if (null != traversedBatchClass && uncPath.equalsIgnoreCase(traversedBatchClass.getUncFolder())) {
					String batchClassUNCFolderPath = traversedBatchClass.getUncFolder();
					if (!StringUtil.isNullOrEmpty(batchClassUNCFolderPath)
							&& !batchClassUNCFolderPath.equalsIgnoreCase(uncPathToIgnore)) {
						batchClassDTO = traversedBatchClass;
						break;
					}

				}
			}
		}
		return batchClassDTO;
	}

	public List<BatchClassDTO> getAllBatchClassesList() {
		rpcService.getAllBatchClasses(new AsyncCallback<List<BatchClassDTO>>() {

			@Override
			public void onSuccess(final List<BatchClassDTO> result) {
				// view.setUNCFoldersList(result);
				if (BatchClassUtil.isDefaultBatchClass(result)) {
					view.setUseExistingUNCCheckBox(false);
				} else {
					view.setUseExistingUNCCheckBox(true);
				}
				allBatchClasses = result;
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
		return allBatchClasses;
	}

	// private void saveBatchClass() {
	// importBatchClassUserOptionsDTO.setName(view.getBatchClassName());
	// importBatchClassUserOptionsDTO.setPriority(view.getPriority());
	// importBatchClassUserOptionsDTO.setDescription(view.getDescription());
	// importBatchClassUserOptionsDTO.setUseSource(true);
	// importBatchClassUserOptionsDTO.setUncFolder(view.getUncFolderLocation());
	// String encryptionAlgo = view.encryptionAlgoList.getValue(view.encryptionAlgoList.getSelectedIndex());
	// if (encryptionAlgo != null) {
	// importBatchClassUserOptionsDTO.setEncryptionAlgo(encryptionAlgo);
	// }
	// importBatchClassUserOptionsDTO.setImportExisting(view.getUseExistingUncFolderCheckBox());
	// ScreenMaskUtility.maskScreen("Importing Batch Class");
	// controller.getRpcService().importBatchClass(importBatchClassUserOptionsDTO, false, new AsyncCallback<Boolean>() {
	//
	// @Override
	// public void onSuccess(final Boolean isSuccess) {
	// ScreenMaskUtility.unmaskScreen();
	// if (isSuccess) {
	// if (!view.getUseExistingCheckboxValue()) {
	// generateKeyForBatchClass(view.getBatchClassKey(),
	// view.encryptionAlgoList.getValue(view.encryptionAlgoList.getSelectedIndex()));
	// } else {
	// BatchClassManagementEventBus.fireEvent(new RefreshBatchClassScreenEvent());
	// Info.display("Import Sucess", "Sucess");
	// }
	// } else {
	// Info.display("Import Sucess", "Sucess");
	// BatchClassManagementEventBus.fireEvent(new RefreshBatchClassScreenEvent());
	//
	// }
	//
	// }
	//
	// @Override
	// public void onFailure(final Throwable arg0) {
	// ScreenMaskUtility.unmaskScreen();
	// BatchClassDTO batchClass = new BatchClassDTO();
	// batchClass.setName(view.getBatchClassName());
	// batchClass.setId(Math.round(Math.random()));
	// batchClass.setDescription(view.getDescription());
	// batchClass.setPriority(view.getPriority());
	// BatchClassManagementEventBus.fireEvent(new BatchClassAdditionEvent(batchClass));
	//
	// }
	//
	// });
	//
	// }

	// private void generateKeyForBatchClass(final String batchClassKey, final String selectedEncryptionAlgo) {
	// controller.getRpcService().getBatchClassIdentifierForBatchClassName(view.getBatchClassName(), new AsyncCallback<String>() {
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// Info.display("Import Failed", "failed888");
	//
	// }
	//
	// @Override
	// public void onSuccess(String result) {
	// controller.getRpcService().generateBatchClassLevelKey(batchClassKey, selectedEncryptionAlgo, result,
	// new AsyncCallback<Integer>() {
	//
	// @Override
	// public void onFailure(Throwable caught) {
	// Info.display("Import Failed2", "failed2");
	// // refresh batch class management screen.
	// }
	//
	// @Override
	// public void onSuccess(Integer result) {
	// if (result == 0) {
	// Info.display("Import Sucessss", "Sucesssss");
	// BatchClassManagementEventBus.fireEvent(new RefreshBatchClassScreenEvent());
	//
	// } else {
	// DialogUtil.showMessageDialog("Error", "Unable to Import Batch Class", DialogIcon.ERROR);
	//
	// }
	// }
	// });
	// }
	// });
	//
	// }
	/**
	 * Returns the batch class config object for a particular unc folder.
	 * 
	 * @param selectedUNCFolder {@link String}, the name of the unc folder.
	 * @return {@link UNCFolderConfig}, the object containing batch class information used while importing the batch class.
	 */
	// public UNCFolderConfig getSelectedBatchClassConfig(final String selectedUNCFolder) {
	// UNCFolderConfig batchClassConfig = null;
	// if (uncFolderConfigList != null) {
	// for (final UNCFolderConfig uncFolderConfig : uncFolderConfigList) {
	// final String uncFolder = uncFolderConfig.getUncFolder();
	// if (uncFolder.equalsIgnoreCase(selectedUNCFolder)) {
	// batchClassConfig = uncFolderConfig;
	// break;
	// }
	// }
	// }
	// return batchClassConfig;
	// }

	public boolean validateBCNameForSpecialCharacters(String textBoxData) {
		boolean isValidBCname = true;
		String invalidSpecialChars = controller.getNotAllowedBatchClassCharacters();
		char[] invalidChars = invalidSpecialChars.toCharArray();
		if (!StringUtil.isNullOrEmpty(textBoxData)) {
			isValidBCname = !Character.isDigit(textBoxData.charAt(0));
		}
		if (textBoxData != null && !textBoxData.isEmpty() && invalidChars != null && invalidChars.length > 0) {
			for (char invalidChar : invalidChars) {
				if (textBoxData.indexOf(invalidChar) != -1) {
					isValidBCname = false;
					break;
				}
			}
		}
		return isValidBCname;
	}

	public String getNotAllowedBatchClassNameCharacters() {
		return controller.getNotAllowedBatchClassCharacters();
	}
}
