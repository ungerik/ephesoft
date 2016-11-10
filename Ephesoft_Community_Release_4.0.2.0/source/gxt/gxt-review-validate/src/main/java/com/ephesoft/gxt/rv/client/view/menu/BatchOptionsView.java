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

package com.ephesoft.gxt.rv.client.view.menu;

import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleConstant;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.ToggleTableViewDisplayModeEvent;
import com.ephesoft.gxt.rv.client.presenter.menu.BatchOptionsPresenter;
import com.ephesoft.gxt.rv.client.view.ReviewValidateBaseView;
import com.ephesoft.gxt.rv.client.view.ShortcutView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

public class BatchOptionsView extends ReviewValidateBaseView<BatchOptionsPresenter> {

	interface Binder extends UiBinder<Widget, BatchOptionsView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected CustomMenuBar batchOptionsMenuBar;

	protected CustomMenuBar mergeMenuBar;

	protected CustomMenuItem reviewValidateBatch;

	protected CustomMenuItem mergeBatch;

	protected CustomMenuItem nextBatch;

	protected CustomMenuItem tableViewMenuItem;

	private final CustomMenuBar shortcutsMenu;

	protected CustomMenuBar externalInterfaceMenuBar;

	@Override
	public void initialize() {
		initWidget(binder.createAndBindUi(this));
		batchOptionsMenuBar.setAutoOpen(false);
		batchOptionsMenuBar.setFocusOnHoverEnabled(false);
		reviewValidateBatch = new CustomMenuItem(LocaleConstant.REVIEW_MENU_ITEM_TEXT, this.getReviewValidateBatchCommand());
		WidgetUtil.setID(reviewValidateBatch, "rv-Review-Validate-Batch");
		mergeMenuBar = new CustomMenuBar(true);
		mergeMenuBar.setFocusOnHoverEnabled(false);
		mergeBatch = new CustomMenuItem(LocaleConstant.MERGE_MENU_TEXT, mergeMenuBar);
		WidgetUtil.setID(mergeBatch, "rv-mergeDoc-Button");
		ScheduledCommand command = getOpenNextBatchCommand();
		nextBatch = new CustomMenuItem(LocaleConstant.NEXT_BATCH_MENU_TEXT, command);
		WidgetUtil.setID(nextBatch, "rv-nextBatch-Button");
		batchOptionsMenuBar.addItem(reviewValidateBatch);
		batchOptionsMenuBar.addItem(mergeBatch);
		batchOptionsMenuBar.addItem(nextBatch);
		tableViewMenuItem = new CustomMenuItem(LocaleConstant.TABLE_VIEW_MENU_TEXT, getOpenTableViewScheduledCommand());
		tableViewMenuItem.setEnabled(false);
		WidgetUtil.setID(tableViewMenuItem, "rv-tableView-Button");
		batchOptionsMenuBar.addItem(tableViewMenuItem);
	}

	public BatchOptionsView() {
		externalInterfaceMenuBar = new CustomMenuBar(true);
		externalInterfaceMenuBar.setFocusOnHoverEnabled(false);
		externalInterfaceMenuBar.addStyleName("externalInterfaceMenu");
		shortcutsMenu = new CustomMenuBar(true);
		this.addShortcutMenu();
		CustomMenuItem moreMenuItem = new CustomMenuItem(LocaleConstant.MORE_MENU_ITEM_TEXT, externalInterfaceMenuBar);
		batchOptionsMenuBar.addItem(moreMenuItem);
		WidgetUtil.setID(moreMenuItem, "rv-moreButton-Button");
	}

	private ScheduledCommand getOpenTableViewScheduledCommand() {
		return new ScheduledCommand() {

			@Override
			public void execute() {
				ReviewValidateEventBus.fireEvent(new ToggleTableViewDisplayModeEvent());
			}
		};
	}

	private ScheduledCommand getOpenNextBatchCommand() {
		return new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.performNextBatchOpenOperation();
			}
		};
	}

	private ScheduledCommand getReviewValidateBatchCommand() {
		ScheduledCommand command = new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.handleBatchReviewValidateOperation();
			}
		};
		return command;
	}

	public void addMergeMenu(String textToDisplay, ScheduledCommand commandToExecute) {
		if (!StringUtil.isNullOrEmpty(textToDisplay) && commandToExecute != null) {
			addMergeMenuItem(new CustomMenuItem(textToDisplay, commandToExecute));
		}
	}

	public void addMergeMenuItem(CustomMenuItem menuItemToAdd) {
		if (menuItemToAdd != null) {
			mergeMenuBar.addItem(menuItemToAdd);
		}
	}

	public void setEnableMergeOptions(boolean enable) {
		mergeBatch.setEnabled(enable);
	}

	public void setReviewValidateMenuHeader(final String header) {
		reviewValidateBatch.setText(header);
	}

	public void clearMergeMenuItem() {
		mergeMenuBar.clearItems();
	}

	public void setEnableTableViewSwitch(boolean enable) {
		tableViewMenuItem.setEnabled(enable);
	}

	private void addShortcutMenu() {
		final CustomMenuItem shortcutsMenuItem = new CustomMenuItem(LocaleConstant.SHORTCUT_MENU_ITEM, shortcutsMenu);
		externalInterfaceMenuBar.addItem(shortcutsMenuItem);
		WidgetUtil.setID(shortcutsMenuItem, "rv-shortcuts-Button");
		final ScheduledCommand command = new ScheduledCommand() {

			@Override
			public void execute() {
				addShortcutMenuItem(new ShortcutView(LocaleConstant.SAVE_SHORTCUT_LABEL, LocaleConstant.SAVE_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.VALIDATE_BATCH_SHORTCUT_LABEL,
						LocaleConstant.VALIDATE_BATCH_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.SPLIT_SHORTCUT_LABEL, LocaleConstant.SPLIT_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.ZOOM_IN_SHORTCUT_LABEL, LocaleConstant.ZOOM_IN_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.ZOOM_OUT_SHORTCUT_LABEL, LocaleConstant.ZOOM_OUT_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.MOVE_CURSOR_NEXT_FIELD_LABEL,
						LocaleConstant.MOVE_CURSOR_NEXT_FIELD_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.MOVE_SHORTCUT_NEXT_ERROR_FIELD_LABEL,
						LocaleConstant.MOVE_SHORTCUT_NEXT_ERROR_FIELD_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.DUPLICATE_PAGE_SHORTCUT_LABEL,
						LocaleConstant.DUPLICATE_PAGE_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.FIT_TO_PAGE_SHORTCUT_LABEL,
						LocaleConstant.FIT_TO_PAGE_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.ROTATE_PAGE_SHORTCUT_LABEL,
						LocaleConstant.ROTATE_PAGE_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.REMOVE_PAGE_SHORTCUT_LABEL,
						LocaleConstant.REMOVE_PAGE_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.FOCUS_ON_DOC_TYPE_LABEL, LocaleConstant.FOCUS_ON_DOC_TYPE_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.MERGE_DOCUMENT_SHORTCUT_LABEL,
						LocaleConstant.MERGE_DOCUMENT_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.NEXT_BATCH_SHORTCUT_LABEL,
						LocaleConstant.NEXT_BATCH_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.INSERT_ROW_NEXT_LABEL, LocaleConstant.INSERT_ROW_NEXT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.INSERT_ROW_PREVIOUS_LABEL,
						LocaleConstant.INSERT_ROW_PREVIOUS_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.DELETE_SINGLE_ROW_LABEL, LocaleConstant.DELETE_SINGLE_ROW_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.DELETE_ALL_ROWS_LABEL, LocaleConstant.DELETE_ALL_ROW_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.TRAVERSE_TABLE_LABEL, LocaleConstant.TRAVERSE_TABLE_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.MANUAL_EXTRACTION_LABEL, LocaleConstant.MANUAL_EXTRACTION_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.MOVE_IMAGE_LEFT_LABEL, LocaleConstant.MOVE_IMAGE_LEFT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.MOVE_IMAGE_RIGHT_LABEL, LocaleConstant.MOVE_IMAGE_RIGHT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.MOVE_IMAGE_UP_LABEL, LocaleConstant.MOVE_IMAGE_UP_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.MOVE_IMAGE_DOWN_LABEL, LocaleConstant.MOVE_IMAGE_DOWN_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.NEXT_DOCUMENT_LABEL, LocaleConstant.NEXT_DOCUMENT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.PREVIOUS_DOCUMENT_LABEL, LocaleConstant.PREVIOUS_DOCUMENT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.NEXT_PAGE_LABEL, LocaleConstant.NEXT_PAGE_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.PREVIOUS_PAGE_LABEL, LocaleConstant.PREVIOUS_PAGE_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.UPDATE_DOCUMENT_LABEL, LocaleConstant.UPDATE_DOCUMENT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.TOGGLE_MANUAL_EXTRACTION_VALIDATION_LABEL,
						LocaleConstant.TOGGLE_MANUAL_EXTRACTION_VALIDATION_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.DISABLE_PAGE_JUMP_LABEL, LocaleConstant.DISABLE_PAGE_JUMP_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.FUZZY_SEARCH_FOCUS_LABEL, LocaleConstant.FUZZY_SEARCH_FOCUS_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.MOVE_CURSOR_TO_PREVIOUS_ERROR_TITLE,
						LocaleConstant.MOVE_CURSOR_TO_PREVIOUS_ERROR_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.OPEN_CLOSE_TABLE_VIEW_TITLE,
						LocaleConstant.OPEN_CLOSE_TABLE_VIEW_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.ZOOM_LOCK_SHORTCUT_TITLE, LocaleConstant.ZOOM_LOCK_SHORTCUT_VALUE));
				addShortcutMenuItem(new ShortcutView(LocaleConstant.REGEX_ACTIVATE_TITLE, LocaleConstant.REGEX_ACTIVATE_VALUE));
			}

		};
		final Scheduler scheduler = new SchedulerImpl();
		scheduler.scheduleDeferred(command);
	}

	private void addShortcutMenuItem(final ShortcutView shortcutView) {
		final String innerHTML = WidgetUtil.getInnerHTML(shortcutView);
		final SafeHtml shortcutMenuItem = SafeHtmlUtils.fromTrustedString(innerHTML);
		shortcutsMenu.addItem(new CustomMenuItem(shortcutMenuItem));
	}

	public void setTableView(final boolean valid) {
		if (valid) {
			tableViewMenuItem.removeStyleName("invalidTableViewMenu");
		} else {
			tableViewMenuItem.addStyleName("invalidTableViewMenu");
		}
	}
}
