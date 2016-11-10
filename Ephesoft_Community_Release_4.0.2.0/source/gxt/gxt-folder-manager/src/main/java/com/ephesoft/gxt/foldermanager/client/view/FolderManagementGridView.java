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

package com.ephesoft.gxt.foldermanager.client.view;

import java.util.List;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.FolderNameValidator;
import com.ephesoft.gxt.core.shared.dto.FileType;
import com.ephesoft.gxt.core.shared.dto.FolderManagerDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.FolderManagerProperties;
import com.ephesoft.gxt.core.shared.util.NumberUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementConstants;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementMessages;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementGridPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class FolderManagementGridView extends View<FolderManagementGridPresenter> implements HasResizableGrid {

	@UiField(provided = true)
	protected Grid<FolderManagerDTO> folderManagerGrid;

	@UiField
	protected VerticalLayoutContainer vPanel;

	@UiField
	protected Label footer;
	private List<FolderManagerDTO> folderManagerDTOs;
	private boolean gridLoaded = false;
	private CustomMenuBar contextMenuBar;
	private Menu contextMenu;
	private CustomMenuItem downloadMenuItem;
	private CustomMenuItem openMenuItem;
	private CustomMenuItem cutMenuItem;
	private CustomMenuItem copyMenuItem;
	private CustomMenuItem deleteMenuItem;
	private FolderManagerDTO rightClickSelectionDTO;

	interface Binder extends UiBinder<Widget, FolderManagementGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	public FolderManagementGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		footer.addStyleName(FolderManagementConstants.FOOTER);
		folderManagerGrid.addStyleName("gridView");
		vPanel.addStyleName("gridViewMainPanel");
		setWidgetIds();
	}

	private void setWidgetIds() {
		WidgetUtil.setID(footer, "FMGV_currentPath");
		WidgetUtil.setID(folderManagerGrid, "FMGV_folderManagerGrid");
		WidgetUtil.setID(vPanel, "FMGV_vPanel");
		WidgetUtil.setID(contextMenu, "FMGV_contextMenu");
		WidgetUtil.setID(contextMenuBar, "FMGV_contextMenuBar");
		WidgetUtil.setID(copyMenuItem, "FMGV_copyMenuItem");
		WidgetUtil.setID(downloadMenuItem, "FMGV_downloadMenuItem");
		WidgetUtil.setID(openMenuItem, "FMGV_openMenuItem");
		WidgetUtil.setID(cutMenuItem, "FMGV_cutMenuItem");
		WidgetUtil.setID(deleteMenuItem, "FMGV_deleteMenuItem");
	}

	@Override
	public void initialize() {
		setFolderManagerGrid(new Grid<FolderManagerDTO>(PropertyAccessModel.FOLDER_MANAGER) {

			@Override
			public void onDoubleClick(Event e) {
				super.onDoubleClick(e);
				int index = getFolderManagerGrid().getView().findRowIndex(Element.as(e.getEventTarget()));
				if (index >= 0) {
					FolderManagerDTO selectedDTO = presenter.getSelectedFolder();
					updateGridContent(selectedDTO);
				}
			}

			@Override
			public void completeEditing(CompleteEditEvent<FolderManagerDTO> completeEditEvent) {
				int rowId = completeEditEvent.getEditCell().getRow();
				FolderManagerDTO beforeEdit = getFolderManagerGrid().getStore().get(rowId);
				String nameBeforeEdit = beforeEdit.getFileName();
				folderManagerGrid.getStore().commitChanges();
				FolderManagerDTO afterEditComplete = getFolderManagerGrid().getStore().get(rowId);
				if (folderManagerGrid.isGridValidated()) {
					presenter.renameCell(nameBeforeEdit, afterEditComplete.getFileName());
				} else {
					beforeEdit.setFileName(nameBeforeEdit);
					folderManagerGrid.refreshRow(rowId);
					int colId = presenter.getNameColumnIndex();
					folderManagerGrid.selectGridCell(rowId, colId);
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(FolderManagementConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(FolderManagementMessages.INVALID_NAME_ERROR_MESSAGE), DialogIcon.ERROR);
				}
			}

			@Override
			public void onMouseUp(Event e) {
				int clickedButton = e.getButton();
				if (clickedButton == Event.BUTTON_RIGHT) {
					final int index = getFolderManagerGrid().getView().findRowIndex(Element.as(e.getEventTarget()));
					e.preventDefault();
					contextMenu = new Menu();
					contextMenu.addDomHandler(new ContextMenuHandler() {

						@Override
						public void onContextMenu(ContextMenuEvent event) {
							event.preventDefault();
						}
					}, ContextMenuEvent.getType());
					if (index >= 0) {
						getFolderManagerGrid().getView().getRow(index).addClassName("gridRightSelection");
						rightClickSelectionDTO = getFolderManagerGrid().getStore().get(index);
						contextMenuBar = new CustomMenuBar(true);
						contextMenu.setBorders(true);
						contextMenu.addStyleName("contextMenu");
						addFolderGridContextMenuHandler(getFolderManagerGrid());
						contextMenu.addHideHandler(new HideHandler() {

							@Override
							public void onHide(HideEvent event) {
								getFolderManagerGrid().getView().getRow(index).removeClassName("gridRightSelection");
							}

						});
					} else {
						getFolderManagerGrid().setContextMenu(null);
					}
				}
			}

		});
		this.folderManagerGrid.setFirstRowSelectedOnLoad(true);
		this.folderManagerGrid.addSelectAllFunctionality(FolderManagerProperties.property.selected());
		this.folderManagerGrid.addValidators(FolderManagerProperties.property.fileName(),
				new FolderNameValidator<FolderManagerDTO, String>());
		this.folderManagerGrid.addValidators(FolderManagerProperties.property.fileName(), new EmptyValueValidator<FolderManagerDTO>());
		folderManagerGrid.setHasPagination(false);
		this.folderManagerGrid.setIdProvider(FolderManagerProperties.property.fileName());
		this.addFolderGridSelectionHandler(getFolderManagerGrid().getSelectionModel());
	}

	public void addFolderGridSelectionHandler(GridSelectionModel<FolderManagerDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<FolderManagerDTO> cellSelectionModel = (CellSelectionModel<FolderManagerDTO>) selectionModel;

			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<FolderManagerDTO>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<FolderManagerDTO> cellSelectionEvent) {
					presenter.selectFolder(cellSelectionEvent);
				}
			});

		}
	}

	public void addFolderGridContextMenuHandler(Grid<FolderManagerDTO> grid) {
		addMenuItems(contextMenuBar);
		addDownloadMenuItem(contextMenuBar);
		contextMenu.insert(contextMenuBar, 0);
		grid.setContextMenu(contextMenu);
	}

	private void addMenuItems(CustomMenuBar contextMenuBar) {
		openMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(FolderManagementConstants.OPEN_BUTTON);
			}
		});

		cutMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(FolderManagementConstants.CUT);
			}
		});

		copyMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(FolderManagementConstants.FOLDER_COPY_BUTTON);
			}
		});
		deleteMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(FolderManagementConstants.DELETE);
			}
		});

		contextMenuBar.addItem(openMenuItem);
		contextMenuBar.addItem(cutMenuItem);
		contextMenuBar.addItem(copyMenuItem);
		contextMenuBar.addItem(deleteMenuItem);
		intializeSelectionHandlers();
	}

	private void intializeSelectionHandlers() {

		copyMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onCopyClicked(rightClickSelectionDTO);
				contextMenu.hide();
			}
		});

		cutMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onCutClicked(rightClickSelectionDTO);
				contextMenu.hide();
			}
		});
		deleteMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onDeleteClicked(rightClickSelectionDTO);
				contextMenu.hide();
			}
		});

		openMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onOpenClicked(rightClickSelectionDTO);
				contextMenu.hide();
			}
		});
	}

	private void addDownloadMenuItem(CustomMenuBar contextMenuBar2) {
		if (!rightClickSelectionDTO.getKind().equals(FileType.DIR)) {
			downloadMenuItem = new CustomMenuItem(new SafeHtml() {

				@Override
				public String asString() {
					return LocaleDictionary.getConstantValue(FolderManagementConstants.DOWNLOAD_MENU_ITEM);
				}
			});
			contextMenuBar.addItem(downloadMenuItem);
			downloadMenuItem.setScheduledCommand(new ScheduledCommand() {

				@Override
				public void execute() {
					presenter.onDownloadClicked(rightClickSelectionDTO);
					contextMenu.hide();
				}
			});
		}
	}

	public void loadGridContent(String path, List<FolderManagerDTO> folderContents) {
		this.setFolderManagerDTOs(folderContents);
		roundOffFileSize(folderContents);
		gridLoaded = true;
		reloadGrid();
	}

	private void roundOffFileSize(List<FolderManagerDTO> folderContents) {
		for (FolderManagerDTO dto : folderContents) {
			if (null != dto.getSize()) {
				float roundedValue = NumberUtil.getRoundedValue(dto.getSize());
				dto.setSize(roundedValue);
			}
		}
	}

	public void reloadGrid() {
		if (gridLoaded) {
			getFolderManagerGrid().setMemoryData(getFolderManagerDTOs());
			WidgetUtil.reLoadGrid(getFolderManagerGrid());
			gridLoaded = true;
			setUpMenuItemEnabled();
			String newFolder = presenter.getNewlyAddedFolder();
			selectNewlyAddedFolder(newFolder);
		}
	}

	private void selectNewlyAddedFolder(String newFolder) {
		if (!StringUtil.isNullOrEmpty(newFolder)) {
			presenter.selectNewlyAddedFolder(newFolder);
		}
	}

	private void setUpMenuItemEnabled() {
		presenter.setUpMenuItemEnabled();
	}

	public List<FolderManagerDTO> getFolderManagerDTOs() {
		return folderManagerDTOs;
	}

	public void setFolderManagerDTOs(List<FolderManagerDTO> folderManagerDTOs) {
		this.folderManagerDTOs = folderManagerDTOs;
	}

	public Label getFooter() {
		return footer;
	}

	public void setFooter(Label footer) {
		this.footer = footer;
	}

	public void startEditing(GridCell cell) {
		this.folderManagerGrid.startEditing(cell);
	}

	public void updateGridContent(FolderManagerDTO selectedDTO) {
		presenter.updateGridContent(selectedDTO);
	}

	public List<FolderManagerDTO> getSelectedModels() {
		return folderManagerGrid.getSelectedModels();
	}

	public FolderManagerDTO getCurrentSelectedModel() {
		return folderManagerGrid.getCurrentSelectedModel();
	}

	public Grid<FolderManagerDTO> getFolderManagerGrid() {
		return folderManagerGrid;
	}

	public void setFolderManagerGrid(Grid<FolderManagerDTO> folderManagerGrid) {
		this.folderManagerGrid = folderManagerGrid;
	}

	@Override
	public Grid<FolderManagerDTO> getGrid() {
		return folderManagerGrid;
	}

}
