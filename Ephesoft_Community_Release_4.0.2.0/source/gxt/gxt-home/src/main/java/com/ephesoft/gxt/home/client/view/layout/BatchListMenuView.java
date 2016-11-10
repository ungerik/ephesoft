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

package com.ephesoft.gxt.home.client.view.layout;

import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.home.client.i18n.BatchListConstants;
import com.ephesoft.gxt.home.client.presenter.BatchListMenuPresenter;
import com.ephesoft.gxt.home.client.view.BatchListBaseView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Widget;

public class BatchListMenuView extends BatchListBaseView<BatchListMenuPresenter> {

	interface Binder extends UiBinder<Widget, BatchListMenuView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected CustomMenuBar batchListMenuBar;

	private CustomMenuItem openMenuItem;

	private CustomMenuItem validationMenuItem;

	private CustomMenuItem reviewMenuItem;

	public BatchListMenuView() {
		initWidget(binder.createAndBindUi(this));
		batchListMenuBar.setAutoOpen(true);
		batchListMenuBar.setFocusOnHoverEnabled(false);
		batchListMenuBar.addStyleName(BatchListConstants.EXTERNAL_INTERFACE_MENU_CSS);

		reviewMenuItem = new CustomMenuItem(LocaleDictionary.getConstantValue(BatchListConstants.BATCH_LIST_REVIEW_MENU),
				new Command() {

					@Override
					public void execute() {
						presenter.onClickMenuEvent("9");
						validationMenuItem.removeStyleName(BatchListConstants.SELECTED_TAB_CSS);
						reviewMenuItem.addStyleName(BatchListConstants.SELECTED_TAB_CSS);
					}
				});
		reviewMenuItem.addStyleName(BatchListConstants.SELECTED_TAB_CSS);
		batchListMenuBar.addItem(reviewMenuItem);
		validationMenuItem = new CustomMenuItem(LocaleDictionary.getConstantValue(BatchListConstants.BATCH_LIST_VALIDATE_MENU),
				new Command() {

					@Override
					public void execute() {
						presenter.onClickMenuEvent("10");
						reviewMenuItem.removeStyleName(BatchListConstants.SELECTED_TAB_CSS);
						validationMenuItem.addStyleName(BatchListConstants.SELECTED_TAB_CSS);
					}
				});
		batchListMenuBar.addItem(validationMenuItem);
		openMenuItem = new CustomMenuItem(LocaleDictionary.getConstantValue(BatchListConstants.BATCH_LIST_OPEN_MENU),
				new Command() {

			@Override
			public void execute() {
				presenter.gotoReviewAndValidatePage();
			}
		});
		batchListMenuBar.addItem(openMenuItem);
		setWidgetIds();
	}

	private void setWidgetIds() {
		WidgetUtil.setID(openMenuItem, "batch_list_open_menu_item");
		WidgetUtil.setID(reviewMenuItem, "batch_list_review_menu_item");
		WidgetUtil.setID(validationMenuItem, "batch_list_validation_menu_item");
		
	}

	@Override
	public void initialize() {

	}

	public void setOpenMenuEnable(boolean enable) {
		openMenuItem.setEnabled(enable);
		if (!enable) {
			openMenuItem.addStyleName(CoreCommonConstants.MENU_ITEM_DISABLE_CSS);
		} else {
			openMenuItem.removeStyleName(CoreCommonConstants.MENU_ITEM_DISABLE_CSS);
		}
	}

}
