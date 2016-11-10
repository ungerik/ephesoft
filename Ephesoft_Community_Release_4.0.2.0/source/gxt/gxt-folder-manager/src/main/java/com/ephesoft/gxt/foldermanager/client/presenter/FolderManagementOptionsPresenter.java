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

package com.ephesoft.gxt.foldermanager.client.presenter;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.ui.widget.Tree;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.property.TreeNode;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.ui.widget.window.MessageDialog;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.FileType;
import com.ephesoft.gxt.core.shared.dto.FolderManagerDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.foldermanager.client.controller.FolderManagementController;
import com.ephesoft.gxt.foldermanager.client.event.FolderCutEvent;
import com.ephesoft.gxt.foldermanager.client.event.FolderTreeRefreshEvent;
import com.ephesoft.gxt.foldermanager.client.event.FolderUpEvent;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementConstants;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementMessages;
import com.ephesoft.gxt.foldermanager.client.view.FolderManagementNavigatorView;
import com.ephesoft.gxt.foldermanager.client.view.FolderManagementOptionsView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class FolderManagementOptionsPresenter extends FolderManagementBasePresenter<FolderManagementOptionsView> {

	interface CustomEventBinder extends EventBinder<FolderManagementOptionsPresenter> {
	}

	public static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public FolderManagementOptionsPresenter(final FolderManagementController controller, final FolderManagementOptionsView view,
			String baseFolderUrl) {
		super(controller, view);
		this.baseHttpURL = baseFolderUrl;

	}

	private static final String OPEN_WINDOW_OPTIONS = "menubar=yes,location=no,resizable=yes,scrollbars=yes,status=no,toolbar=true";
	private static final String HEIGHT = ",height=";
	private static final String WIDTH = ",width=";
	private List<String> cutOrCopyFileList;
	private List<FolderManagerDTO> cutFilesDTOsList;
	private List<TreeNode> selectedTreeNodes;
	private FileOperation lastFileOperation;
	private String baseHttpURL;
	final String uploadFormAction = "filesUploadDownload";

	private enum FileOperation {
		CUT, COPY, PASTE;
	}

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public void onNewFolderClicked(String folderPath) {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().createNewFolder(folderPath, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable error) {
				String errorMsg = LocaleDictionary.getMessageValue(FolderManagementMessages.FAILED_TO_CREATE_A_NEW_FOLDER);
				String localizedMessage = error.getLocalizedMessage();
				if (localizedMessage != null && !localizedMessage.isEmpty()) {
					errorMsg += localizedMessage;
				}
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE), errorMsg,
						DialogIcon.ERROR);
				ScreenMaskUtility.unmaskScreen();
			}

			@Override
			public void onSuccess(String newFolderName) {
				ScreenMaskUtility.unmaskScreen();
				Message.display(LocaleDictionary.getConstantValue(FolderManagementConstants.SUCCESS_TITLE),
						LocaleDictionary.getMessageValue(FolderManagementMessages.SUCCESSFULLY_CREATED_THE_NEW_FOLDER, newFolderName));
				controller.getEventBus().fireEvent(new FolderTreeRefreshEvent(null, newFolderName));
				TreeNode currentTreeNode = getCurrentTreeNode();
				refreshExpandedTree(currentTreeNode);
			}
		});
	}

	public void onRefreshClicked() {
		controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
		TreeNode currentTreeNode = getCurrentTreeNode();
		refreshExpandedTree(currentTreeNode);
	}

	public void onFileCopy(String absoluteFilePath) {
		cutOrCopyFileList = new ArrayList<String>();
		cutOrCopyFileList.add(absoluteFilePath);
		lastFileOperation = FileOperation.COPY;
		view.setPasteEnabled(true);
	}

	public void onCopyClicked() {
		cutOrCopyFileList = view.getSelectedFileList();
		if (cutOrCopyFileList.isEmpty() || cutOrCopyFileList == null) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE), LocaleDictionary
					.getMessageValue(FolderManagementMessages.NO_FILES_FOLDERS_SELECTED_FOR_OPERATION,
							LocaleDictionary.getConstantValue(FolderManagementConstants.COPY_PASTE)), DialogIcon.ERROR);
			view.setPasteEnabled(false);
		} else {
			view.setPasteEnabled(true);
			lastFileOperation = FileOperation.COPY;
		}
	}

	public void onFileCut(FolderManagerDTO folderManagerDTO) {
		cutOrCopyFileList = new ArrayList<String>();
		cutOrCopyFileList.add(folderManagerDTO.getPath());
		selectedTreeNodes = new ArrayList<TreeNode>();
		selectedTreeNodes.add(FolderManagementNavigatorView.treeData.get(folderManagerDTO.getPath()));
		lastFileOperation = FileOperation.CUT;
		view.setPasteEnabled(true);
	}

	public void onCutClicked() {
		cutOrCopyFileList = view.getSelectedFileList();
		cutFilesDTOsList = view.getSelectedDTOsList();
		selectedTreeNodes = getSelectedTreeNodes(cutFilesDTOsList);
		if (cutOrCopyFileList.isEmpty() || cutOrCopyFileList == null) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE), LocaleDictionary
					.getMessageValue(FolderManagementMessages.NO_FILES_FOLDERS_SELECTED_FOR_OPERATION,
							LocaleDictionary.getConstantValue(FolderManagementConstants.CUT_PASTE)), DialogIcon.ERROR);
			view.setPasteEnabled(false);
		} else {
			view.setPasteEnabled(true);
			lastFileOperation = FileOperation.CUT;
		}
	}

	private List<TreeNode> getSelectedTreeNodes(List<FolderManagerDTO> cutFilesDTOsList) {
		List<TreeNode> selectedTreeNodes = new ArrayList<TreeNode>();
		for (FolderManagerDTO dto : cutFilesDTOsList) {
			TreeNode selectedNode = FolderManagementNavigatorView.treeData.get(dto.getPath());
			selectedTreeNodes.add(selectedNode);
		}
		return selectedTreeNodes;
	}

	public List<FolderManagerDTO> getSelectedModels() {
		return controller.getFolderManagementView().getFolderManagementGridPresenter().getView().getSelectedModels();

	}

	public void onPasteClicked(String folderPath) {
		if (cutOrCopyFileList != null && !cutOrCopyFileList.isEmpty()) {
			switch (lastFileOperation) {
				case COPY:
					ScreenMaskUtility.maskScreen();
					controller.getRpcService().copyFiles(cutOrCopyFileList, folderPath, new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable caught) {
							ScreenMaskUtility.unmaskScreen();
							String localizedMessage = caught.getLocalizedMessage();
							if (localizedMessage != null && !localizedMessage.isEmpty()) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
										localizedMessage, DialogIcon.ERROR);
								// controller.getEventBus().fireEvent(new
								// FolderTreeRefreshEvent());
							} else {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
										LocaleDictionary
												.getMessageValue(FolderManagementMessages.UNABLE_TO_COMPLETE_COPY_PASTE_OPERATION),
										DialogIcon.ERROR);
								// controller.getEventBus().fireEvent(new
								// FolderTreeRefreshEvent());
							}
						}

						@Override
						public void onSuccess(List<String> result) {
							ScreenMaskUtility.unmaskScreen();
							if (null != result) {
								if (result.size() == 0) {
									controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
									TreeNode currentTreeNode = getCurrentTreeNode();
									refreshExpandedTree(currentTreeNode);
								} else if (result.size() == cutOrCopyFileList.size()) {
									showFileNotCopyOrCut(result,
											LocaleDictionary.getConstantValue(FolderManagementConstants.COPY_PASTE));
								} else {
									showFileNotCopyOrCut(result,
											LocaleDictionary.getConstantValue(FolderManagementConstants.COPY_PASTE));
									controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
									TreeNode currentTreeNode = getCurrentTreeNode();
									refreshExpandedTree(currentTreeNode);
								}
							}
						}
					});
					break;
				case CUT:
					view.setPasteEnabled(false);
					lastFileOperation = FileOperation.PASTE;
					boolean isPastingInSameFolder = checkIfPastingInSameFolder(cutOrCopyFileList, folderPath);
					if (!isPastingInSameFolder) {
						ScreenMaskUtility.maskScreen();
						controller.getRpcService().cutFiles(cutOrCopyFileList, folderPath, new AsyncCallback<List<String>>() {

							@Override
							public void onFailure(Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
								String localizedMessage = caught.getLocalizedMessage();
								if (localizedMessage != null && !localizedMessage.isEmpty()) {
									DialogUtil.showMessageDialog(
											LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
											localizedMessage, DialogIcon.ERROR);
									// controller.getEventBus().fireEvent(new
									// FolderTreeRefreshEvent());
								} else {
									DialogUtil.showMessageDialog(LocaleDictionary
											.getConstantValue(FolderManagementConstants.ERROR_TITLE), LocaleDictionary
											.getMessageValue(FolderManagementMessages.UNABLE_TO_COMPLETE_CUT_PASTE_OPERATION),
											DialogIcon.ERROR);
									// controller.getEventBus().fireEvent(new
									// FolderTreeRefreshEvent());
								}
							}

							@Override
							public void onSuccess(List<String> result) {
								ScreenMaskUtility.unmaskScreen();
								if (null != result) {
									if (result.size() == 0) {
										controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
										TreeNode currentTreeNode = getCurrentTreeNode();
										refreshExpandedTree(currentTreeNode);
										controller.getEventBus().fireEvent(new FolderCutEvent(selectedTreeNodes));
									} else if (result.size() == cutOrCopyFileList.size()) {
										showFileNotCopyOrCut(result,
												LocaleDictionary.getConstantValue(FolderManagementConstants.CUT_PASTE));
									} else {
										showFileNotCopyOrCut(result,
												LocaleDictionary.getConstantValue(FolderManagementConstants.CUT_PASTE));
										controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
										TreeNode currentTreeNode = getCurrentTreeNode();
										refreshExpandedTree(currentTreeNode);
										controller.getEventBus().fireEvent(new FolderCutEvent(selectedTreeNodes));
									}
								}
							}
						});
					} else {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(FolderManagementMessages.SOURCE_AND_DESTINATION_SAME),
								DialogIcon.ERROR);
					}
					break;
				default:
					break;
			}
		}
	}

	private boolean checkIfPastingInSameFolder(List<String> cutFilesList, String folderPath) {
		boolean isPastingInSameFolder = false;
		String changedFolderPath = folderPath;
		changedFolderPath = changedFolderPath.replace("/", "");
		changedFolderPath = changedFolderPath.replace("\\", "");
		for (String filePath : cutFilesList) {
			String changedFilePath = filePath;
			changedFilePath = changedFilePath.replace("/", "");
			changedFilePath = changedFilePath.replace("\\", "");
			if (changedFilePath.equals(changedFolderPath)) {
				isPastingInSameFolder = true;
				break;
			}
		}
		return isPastingInSameFolder;
	}

	public void onFileDelete(final String fileName, String absoluteFilePath) {
		ScreenMaskUtility.maskScreen();
		controller.getRpcService().deleteFile(absoluteFilePath, new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				ScreenMaskUtility.unmaskScreen();
				if (result) {
					controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
					TreeNode currentTreeNode = getCurrentTreeNode();
					refreshExpandedTree(currentTreeNode);
				} else {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(FolderManagementMessages.UNABLE_TO_DELETE_THE_FILE)
									+ FolderManagementConstants.QUOTES + fileName + FolderManagementConstants.QUOTES
									+ FolderManagementConstants.DOT, DialogIcon.ERROR);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				ScreenMaskUtility.unmaskScreen();
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(FolderManagementMessages.UNABLE_TO_DELETE_THE_FILE)
								+ FolderManagementConstants.QUOTES + fileName + FolderManagementConstants.QUOTES
								+ FolderManagementConstants.DOT, DialogIcon.ERROR);
			}
		});

	}

	public void onDeleteClicked() {
		final List<String> deleteFileList = view.getSelectedFileList();
		if (deleteFileList.isEmpty()) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE), LocaleDictionary
					.getMessageValue(FolderManagementMessages.NO_FILES_FOLDERS_SELECTED_FOR_OPERATION,
							LocaleDictionary.getConstantValue(FolderManagementConstants.DELETE_CONFIRM)), DialogIcon.ERROR);
		} else {
			final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(
					LocaleDictionary.getConstantValue(FolderManagementConstants.CONFIRMATION),
					LocaleDictionary.getMessageValue(FolderManagementMessages.ARE_YOU_SURE_YOU_WANT_TO_DELETE_ALL_THE_SELECTED_FILES),
					false, DialogIcon.QUESTION_MARK);
			confirmationDialog.setDialogListener(new DialogAdapter() {

				@Override
				public void onOkClick() {
					confirmationDialog.hide();
					if (!deleteFileList.isEmpty()) {
						ScreenMaskUtility.maskScreen();
						controller.getRpcService().deleteFiles(deleteFileList, new AsyncCallback<String>() {

							@Override
							public void onSuccess(String result) {
								confirmationDialog.hide();
								ScreenMaskUtility.unmaskScreen();
								if (result != null && !result.isEmpty()) {
									if (result.endsWith(FolderManagementConstants.SEMI_COLON)) {
										StringBuilder modifiedResult = new StringBuilder(result);
										modifiedResult.replace(result.lastIndexOf(FolderManagementConstants.SEMI_COLON),
												result.lastIndexOf(FolderManagementConstants.SEMI_COLON) + 1,
												FolderManagementConstants.DOT);
										result = modifiedResult.toString();
									}
									DialogUtil.showMessageDialog(
											LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
											LocaleDictionary.getMessageValue(FolderManagementMessages.UNABLE_TO_DELETE_THE_FOLLOWING)
													+ result, DialogIcon.ERROR);
								} else {
									controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
									TreeNode currentTreeNode = getCurrentTreeNode();
									refreshExpandedTree(currentTreeNode);
								}
							}

							@Override
							public void onFailure(Throwable caught) {
								confirmationDialog.hide();
								ScreenMaskUtility.unmaskScreen();
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(FolderManagementMessages.UNABLE_TO_PERFORM_DELETE_OPERATION),
										DialogIcon.ERROR);
							}
						});
					}
				}

				@Override
				public void onCloseClick() {
					confirmationDialog.hide();
				}

				@Override
				public void onCancelClick() {
					controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
				}
			});
		}
	}

	public void onFolderUpClicked() {
		controller.getEventBus().fireEvent(new FolderUpEvent());
	}

	public void refreshExpandedTree(TreeNode currentTreeNode) {
		Tree navigationTree = getFolderNavigatorView().getNavigationTree();
		if (navigationTree.isExpanded(currentTreeNode)) {
			navigationTree.setExpanded(currentTreeNode, false);
			navigationTree.setExpanded(currentTreeNode, true);
		}
	}

	public void onFileRename(final String fileName, final String newFileName, String folderPath) {
		controller.getRpcService().renameFile(fileName, newFileName, folderPath, new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
					TreeNode currentTreeNode = getCurrentTreeNode();
					refreshExpandedTree(currentTreeNode);
				} else {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(FolderManagementMessages.FAILED_TO_RENAME_FILE,
									FolderManagementConstants.QUOTES + fileName + FolderManagementConstants.QUOTES,
									FolderManagementConstants.QUOTES + newFileName + FolderManagementConstants.QUOTES),
							DialogIcon.ERROR);
					controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
					TreeNode currentTreeNode = getCurrentTreeNode();
					refreshExpandedTree(currentTreeNode);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(FolderManagementMessages.FAILED_TO_RENAME_FILE,
								FolderManagementConstants.QUOTES + fileName + FolderManagementConstants.QUOTES,
								FolderManagementConstants.QUOTES + newFileName + FolderManagementConstants.QUOTES), DialogIcon.ERROR);
			}
		});
	}

	public void onFileOpen(FolderManagerDTO selectedDTO, String parentFolderPath) {
		if (selectedDTO != null) {
			String fileName = selectedDTO.getFileName();
			String absoluteFilePath = selectedDTO.getPath();
			if (!selectedDTO.getKind().equals(FileType.DIR)) {
				String url = baseHttpURL
						+ FolderManagementConstants.URL_SEPARATOR
						+ (absoluteFilePath.substring(absoluteFilePath.lastIndexOf(parentFolderPath) + parentFolderPath.length() + 1))
								.replace(FolderManagementConstants.PATH_SEPARATOR_STRING, FolderManagementConstants.URL_SEPARATOR);
				try {
					Window.open(url, "", OPEN_WINDOW_OPTIONS + WIDTH + Window.getClientWidth() + HEIGHT + Window.getClientHeight());
				} catch (Exception e) {
					DialogUtil
							.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(FolderManagementMessages.COULD_NOT_OPEN_THE_FILE)
											+ FolderManagementConstants.QUOTES + fileName + FolderManagementConstants.QUOTES,
									DialogIcon.ERROR);
				}
			} else {
				controller.getEventBus().fireEvent(new FolderTreeRefreshEvent(selectedDTO, null));
			}
		}
	}

	public void onFileDownload(FolderManagerDTO selectedDTO) {
		if (selectedDTO != null) {
			String absoluteFilePath = selectedDTO.getPath();
			sendDownloadRequest(absoluteFilePath);
		}

	}

	private void sendDownloadRequest(String absoluteFilePath) {
		StringBuffer urlBuffer = new StringBuffer(uploadFormAction);
		if (Window.Location.getHref().contains(FolderManagementConstants.QUESTION_MARK)) {
			urlBuffer.append(FolderManagementConstants.AMPERSAND);
		} else {
			urlBuffer.append(FolderManagementConstants.QUESTION_MARK);
		}
		urlBuffer.append(FolderManagementConstants.CURRENT_FILE_DOWNLOAD_PATH);
		urlBuffer.append(FolderManagementConstants.EQUALS);
		urlBuffer.append(absoluteFilePath);
		String url = urlBuffer.toString();
		Window.open(url, null, null);
	}

	public FolderManagementGridPresenter getFolderGridPresenter() {
		return controller.getFolderManagementView().getFolderManagementGridPresenter();
	}

	private TreeNode getCurrentTreeNode() {
		return controller.getFolderManagementView().getFolderManagementNavigatorPresenter().getCurrentTreeNode();
	}

	private FolderManagementNavigatorView getFolderNavigatorView() {
		return controller.getFolderManagementView().getFolderManagementNavigatorPresenter().getView();
	}

	private void showFileNotCopyOrCut(List<String> filesNotCopyOrCut, String cutOrCopy) {
		String filesNotImported = FolderManagementConstants.ORDERED_LIST_START_TAG;
		if (!CollectionUtil.isEmpty(filesNotCopyOrCut)) {
			for (String unsupportedFileName : filesNotCopyOrCut) {
				filesNotImported = StringUtil.concatenate(filesNotImported, FolderManagementConstants.LIST_START_TAG,
						unsupportedFileName, FolderManagementConstants.LIST_END_TAG);
			}
			final String message = LocaleDictionary.getMessageValue(FolderManagementMessages.UNABLE_TO_PERFORM_PASTE_OPERATION,
					cutOrCopy, filesNotCopyOrCut.size());
			final MessageDialog dialog = new MessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
					message, DialogIcon.ERROR);
			final String filesList = StringUtil.concatenate(filesNotImported, FolderManagementConstants.ORDERED_LIST_END_TAG);
			dialog.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.YES);
			TextButton showFileList = dialog.getButton(PredefinedButton.YES);
			showFileList.setText("Show File List");

			showFileList.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.INFO_TITLE), filesList);
				}
			});

			dialog.addButton(showFileList);
			dialog.show();
		}
	}
}
