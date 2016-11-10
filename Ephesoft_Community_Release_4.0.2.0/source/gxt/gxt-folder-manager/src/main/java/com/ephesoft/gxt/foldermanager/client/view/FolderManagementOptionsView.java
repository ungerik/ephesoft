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

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.FolderManagerDTO;
import com.ephesoft.gxt.foldermanager.client.i18n.FolderManagementConstants;
import com.ephesoft.gxt.foldermanager.client.presenter.FolderManagementOptionsPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;

public class FolderManagementOptionsView extends View<FolderManagementOptionsPresenter> {

	interface Binder extends UiBinder<Widget, FolderManagementOptionsView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected HorizontalPanel optionsContainer;

	@UiField
	protected HorizontalLayoutContainer buttonContainer;

	@UiField
	protected CustomMenuBar menuBar;

	private CustomMenuItem newFolderMenuItem;

	private CustomMenuItem upMenuItem;

	private CustomMenuItem refreshMenuItem;

	private CustomMenuItem cutMenuItem;
	private CustomMenuItem copyMenuItem;
	private CustomMenuItem pasteMenuItem;
	private CustomMenuItem deleteMenuItem;

	@SuppressWarnings("serial")
	public FolderManagementOptionsView() {
		super();
		initWidget(binder.createAndBindUi(this));
		this.optionsContainer.add(menuBar);
		newFolderMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(FolderManagementConstants.CREATE_NEW_FOLDER);
			}
		});
		upMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(FolderManagementConstants.FOLDER_UP_BUTTON);
			}
		});
		refreshMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(FolderManagementConstants.FOLDER_REFRESH_BUTTON);
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
		pasteMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(FolderManagementConstants.FOLDER_PASTE_BUTTON);
			}
		});
		deleteMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(FolderManagementConstants.DELETE);
			}
		});
		menuBar.addItem(newFolderMenuItem);
		menuBar.addItem(upMenuItem);
		menuBar.addItem(refreshMenuItem);
		menuBar.addItem(cutMenuItem);
		menuBar.addItem(copyMenuItem);
		menuBar.addItem(pasteMenuItem);
		menuBar.addItem(deleteMenuItem);
		menuBar.setFocusOnHoverEnabled(false);
		pasteMenuItem.setEnabled(false);
		menuBar.addStyleName("menuBarFolderManager");
		buttonContainer.addStyleName("buttonContainer");
		optionsContainer.addStyleName("horizontalPanelOptionsContainer");
		intializeSelectionHandlers();
		setWidgetIds();
	}

	private void setWidgetIds() {
		WidgetUtil.setID(optionsContainer, "FMOV_optionsContainer");
		WidgetUtil.setID(menuBar, "FMOV_menuBar");
		WidgetUtil.setID(copyMenuItem, "FMOV_copyMenuItem");
		WidgetUtil.setID(newFolderMenuItem, "FMOV_newFolderMenuItem");
		WidgetUtil.setID(upMenuItem, "FMOV_upMenuItem");
		WidgetUtil.setID(refreshMenuItem, "FMOV_refreshMenuItem");
		WidgetUtil.setID(cutMenuItem, "FMOV_cutMenuItem");
		WidgetUtil.setID(pasteMenuItem, "FMOV_pasteMenuItem");
		WidgetUtil.setID(deleteMenuItem, "FMOV_deleteMenuItem");

	}

	private void intializeSelectionHandlers() {

		newFolderMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				String folderPath = presenter.getFolderGridPresenter().getFolderPath();
				presenter.onNewFolderClicked(folderPath);
			}
		});

		upMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onFolderUpClicked();
			}
		});

		refreshMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onRefreshClicked();
			}
		});

		copyMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onCopyClicked();
			}
		});

		cutMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onCutClicked();
			}
		});
		pasteMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				String folderPath = presenter.getFolderGridPresenter().getFolderPath();
				presenter.onPasteClicked(folderPath);
			}
		});

		copyMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onCopyClicked();
			}
		});
		deleteMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onDeleteClicked();
			}
		});

	}

	@Override
	public void initialize() {

	}

	public List<String> getSelectedFileList() {
		List<String> selectedFileList = new ArrayList<String>();
		List<FolderManagerDTO> selectedDTOs = presenter.getSelectedModels();
		for (FolderManagerDTO dto : selectedDTOs) {
			selectedFileList.add(dto.getPath());
		}
		return selectedFileList;
	}

	public List<FolderManagerDTO> getSelectedDTOsList() {
		List<FolderManagerDTO> selectedDTOs = presenter.getSelectedModels();
		return selectedDTOs;
	}

	public void setPasteEnabled(boolean isEnabled) {
		this.pasteMenuItem.setEnabled(isEnabled);
	}

	public void setUpEnabled(boolean isEnabled) {
		this.upMenuItem.setEnabled(isEnabled);
	}

}
