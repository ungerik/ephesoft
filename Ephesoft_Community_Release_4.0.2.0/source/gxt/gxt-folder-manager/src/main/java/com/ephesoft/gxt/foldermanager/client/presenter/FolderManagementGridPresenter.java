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

import java.util.List;

import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.factory.GridAttributeFactory;
import com.ephesoft.gxt.core.client.ui.widget.Tree;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.property.TreeNode;
import com.ephesoft.gxt.core.client.ui.widget.window.ConfirmationDialog;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.shared.dto.FolderManagerDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.FolderManagerProperties;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.foldermanager.client.controller.FolderManagementController;
import com.ephesoft.gxt.foldermanager.client.event.FolderGridRefreshEvent;
import com.ephesoft.gxt.foldermanager.client.event.FolderTreeRefreshEvent;
import com.ephesoft.gxt.foldermanager.client.event.GridSelectionEvent;
import com.ephesoft.gxt.foldermanager.client.event.SelectGridColumnEvent;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementConstants;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementMessages;
import com.ephesoft.gxt.foldermanager.client.view.FolderManagementGridView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

public class FolderManagementGridPresenter extends FolderManagementBasePresenter<FolderManagementGridView> {

	private String folderPath;
	private String parentFolderPath;

	public FolderManagementGridPresenter(FolderManagementController controller, FolderManagementGridView view, String parentFolderPath) {
		super(controller, view);
		this.parentFolderPath = parentFolderPath;
	}

	interface CustomEventBinder extends EventBinder<FolderManagementGridPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void onGridSelection(GridSelectionEvent gridSelectionEvent) {
		FolderManagerDTO selectedDTO = view.getCurrentSelectedModel();
		if (null != selectedDTO) {
			updateGridContent(selectedDTO);
		}
	}

	@EventHandler
	public void selectGridColumn(SelectGridColumnEvent selectGridColumnEvent) {
		String newFolder = selectGridColumnEvent.getNewFolderName();
		FolderManagerDTO dtoToSelect = new FolderManagerDTO();
		List<FolderManagerDTO> dtoList = view.getFolderManagerGrid().getStore().getAll();
		for (FolderManagerDTO dto : dtoList) {
			if (dto.getFileName().equals(newFolder)) {
				dtoToSelect = dto;
				break;
			}
		}
		selectColumn(dtoToSelect);
	}

	private void selectColumn(FolderManagerDTO dtoToSelect) {
		int rowIndex = view.getFolderManagerGrid().getStore().indexOf(dtoToSelect);
		ValueProvider<FolderManagerDTO, String> nameColumnVp = FolderManagerProperties.property.fileName();
		List<ColumnConfig<FolderManagerDTO, ?>> columnConfigList = GridAttributeFactory
				.getColumnConfig(PropertyAccessModel.FOLDER_MANAGER);
		int colId = 0;
		for (ColumnConfig<FolderManagerDTO, ?> column : columnConfigList) {
			if (column.getValueProvider().equals(nameColumnVp)) {
				colId = columnConfigList.indexOf(column);
			}
		}
		GridCell cell = new GridCell(rowIndex, colId);
		setNewlyAddedFolder();
		view.startEditing(cell);
	}

	@EventHandler
	public void onGridRefresh(FolderGridRefreshEvent gridRefreshEvent) {
		refreshContent(gridRefreshEvent.getFolderPath(), gridRefreshEvent.getSelectedNode());
	}

	public void refreshContent(final String path, final TreeNode selectedNode) {
		if (null != path) {
			folderPath = path;
			controller.getRpcService().getContents(path, new AsyncCallback<List<FolderManagerDTO>>() {

				@Override
				public void onFailure(Throwable throwable) {
					String message = throwable.getLocalizedMessage();
					if (message.contains(FolderManagementConstants.ERROR_TYPE_1)) {
						DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
								LocaleDictionary.getMessageValue(FolderManagementMessages.NO_CONTENT_MESSAGE), DialogIcon.ERROR);
					}
					if (null != selectedNode) {
						TreeNode parentNode = getnavigationTree().getParent(selectedNode);
						if (null != parentNode) {
							setCurrentTreeNode(parentNode);
							getnavigationTree().getStore().remove(selectedNode);
							controller.getEventBus().fireEvent(new FolderTreeRefreshEvent());
						}
					}
				}

				@Override
				public void onSuccess(List<FolderManagerDTO> result) {
					if (path.equals(folderPath)) {
						view.loadGridContent(path, result);
					}
				}
			});
		}
	}

	public void selectFolder(CellSelectionChangedEvent<FolderManagerDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			FolderManagerDTO selectedFolder = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			if (selectedFolder != null) {
				controller.setSelectedFolder(selectedFolder);
			}
		}
	}

	public FolderManagerDTO getSelectedFolder() {
		return controller.getSelectedFolder();

	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getParentFolderPath() {
		return parentFolderPath;
	}

	public void setParentFolderPath(String parentFolderPath) {
		this.parentFolderPath = parentFolderPath;
	}

	public void updateGridContent(FolderManagerDTO selectedDTO) {
		onOpenClicked(selectedDTO);
	}

	public void setUpMenuItemEnabled() {
		controller.getFolderManagementView().getFolderManagementOptionsPresenter().getView()
				.setUpEnabled(!folderPath.equals(parentFolderPath));
	}

	public void onCopyClicked(FolderManagerDTO rightClickSelectionDTO) {
		getFolderOptionPresenter().onFileCopy(rightClickSelectionDTO.getPath());
	}

	public FolderManagementOptionsPresenter getFolderOptionPresenter() {
		return controller.getFolderManagementView().getFolderManagementOptionsPresenter();
	}

	public void onCutClicked(FolderManagerDTO rightClickSelectionDTO) {
		getFolderOptionPresenter().onFileCut(rightClickSelectionDTO);
	}

	public void onDeleteClicked(final FolderManagerDTO rightClickSelectionDTO) {
		final ConfirmationDialog confirmationDialog = DialogUtil.showConfirmationDialog(LocaleDictionary
				.getConstantValue(FolderManagementConstants.CONFIRMATION), LocaleDictionary.getMessageValue(
				FolderManagementMessages.ARE_YOU_SURE_YOU_WANT_TO_DELETE_THE_FILE, FolderManagementConstants.QUOTES
						+ rightClickSelectionDTO.getFileName() + FolderManagementConstants.QUOTES), false, DialogIcon.QUESTION_MARK);
		confirmationDialog.setDialogListener(new DialogAdapter() {

			@Override
			public void onOkClick() {
				confirmationDialog.hide();
				getFolderOptionPresenter().onFileDelete(rightClickSelectionDTO.getFileName(), rightClickSelectionDTO.getPath());
			}

			@Override
			public void onCloseClick() {
				confirmationDialog.hide();
			}

			@Override
			public void onCancelClick() {
				confirmationDialog.hide();
			}
		});
	}

	public void onOpenClicked(FolderManagerDTO selectedDTO) {
		getFolderOptionPresenter().onFileOpen(selectedDTO, parentFolderPath);

	}

	public void onDownloadClicked(FolderManagerDTO selectedFolder) {
		getFolderOptionPresenter().onFileDownload(selectedFolder);
	}

	private Tree getnavigationTree() {
		return controller.getFolderManagementView().getFolderManagementNavigatorPresenter().getView().getNavigationTree();
	}

	private void setCurrentTreeNode(TreeNode currentNode) {
		controller.getFolderManagementView().getFolderManagementNavigatorPresenter().setCurrentTreeNode(currentNode);
	}

	public void renameCell(String oldName, String newName) {
		getFolderOptionPresenter().onFileRename(oldName, newName, getFolderPath());
	}

	public void selectNewlyAddedFolder(String newFolder) {
		if (!StringUtil.isNullOrEmpty(newFolder)) {
			controller.getEventBus().fireEvent(new SelectGridColumnEvent(newFolder));
		}
	}

	public String getNewlyAddedFolder() {
		return controller.getFolderManagementView().getFolderManagementNavigatorPresenter().getFolderName();
	}

	public void setNewlyAddedFolder() {
		controller.getFolderManagementView().getFolderManagementNavigatorPresenter().setFolderName("");
	}

	public int getNameColumnIndex() {
		int colId = 0;
		ValueProvider<FolderManagerDTO, String> nameColumnVp = FolderManagerProperties.property.fileName();
		List<ColumnConfig<FolderManagerDTO, ?>> columnConfigList = GridAttributeFactory
				.getColumnConfig(PropertyAccessModel.FOLDER_MANAGER);
		for (ColumnConfig<FolderManagerDTO, ?> column : columnConfigList) {
			if (column.getValueProvider().equals(nameColumnVp)) {
				colId = columnConfigList.indexOf(column);
			}
		}
		return colId;
	}
}
