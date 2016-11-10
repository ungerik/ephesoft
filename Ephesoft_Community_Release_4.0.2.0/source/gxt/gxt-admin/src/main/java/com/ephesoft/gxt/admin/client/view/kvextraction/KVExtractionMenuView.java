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

package com.ephesoft.gxt.admin.client.view.kvextraction;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.CurrentViewRegexValidationEvent;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassManagementConstants;
import com.ephesoft.gxt.admin.client.presenter.kvextraction.KVExtractionMenuPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.view.BatchClassManagementMenuBar;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class KVExtractionMenuView extends BatchClassInlineView<KVExtractionMenuPresenter> {

	interface Binder extends UiBinder<Widget, KVExtractionMenuView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	public KVExtractionMenuView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		intializeMenuItems();
		addMenuItems();
		addMenuItemActionEvents();
	}

	@UiField
	HorizontalPanel kvExtractionMenuPanel;
	@UiField
	BatchClassManagementMenuBar kvExtractionMenuBar;
	CustomMenuItem delete;
	CustomMenuItem advAdd;
	CustomMenuItem advEdit;

	private CustomMenuItem regexValidationMenuItem;

	private void intializeMenuItems() {

		delete = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.DELETE);
			}
		});
		advAdd = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.ADV_ADD);
			}
		});
		advEdit = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.ADV_EDIT);
			}
		});

		regexValidationMenuItem = new CustomMenuItem(SafeHtmlUtils.fromSafeConstant(LocaleDictionary
				.getConstantValue(BatchClassManagementConstants.VALIDATE_REGEX)));
	}

	private void addMenuItems() {
		kvExtractionMenuBar.addItem(delete);
		kvExtractionMenuBar.addItem(advAdd);
		kvExtractionMenuBar.addItem(advEdit);
		kvExtractionMenuBar.addItem(regexValidationMenuItem);
		kvExtractionMenuBar.removeOpenMenuItem();
	}

	private void addMenuItemActionEvents() {
		delete.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.deleteKVExtraction();
			}
		});
		advAdd.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.advAdd();
			}
		});
		advEdit.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.advEdit();
			}
		});

		regexValidationMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				BatchClassManagementEventBus.fireEvent(new CurrentViewRegexValidationEvent());
			}
		});
	}

	@Override
	public void refresh() {

	}

	public void enableDisableEditButton(boolean enabled) {
		advEdit.setEnabled(enabled);
	}
}
