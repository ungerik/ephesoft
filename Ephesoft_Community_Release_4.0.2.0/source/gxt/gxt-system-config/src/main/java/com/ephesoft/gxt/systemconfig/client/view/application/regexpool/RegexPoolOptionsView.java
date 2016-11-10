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

package com.ephesoft.gxt.systemconfig.client.view.application.regexpool;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.application.regexpool.RegexPoolOptionsPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * RegexPoolOptionsView is the top panel view of Regex Pool view, that various custom items linked to Regex Group Grid
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 30-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RegexPoolOptionsView extends SystemConfigInlineView<RegexPoolOptionsPresenter> {

	/**
	 * 
	 * Interface for the Binder.
	 * 
	 * @author Ephesoft
	 * 
	 *         <b>created on</b> 30-Dec-2014 <br/>
	 * @version $LastChangedDate:$ <br/>
	 *          $LastChangedRevision:$ <br/>
	 */
	interface Binder extends UiBinder<Widget, RegexPoolOptionsView> {
	}

	/**
	 * BINDER {@link Binder}.
	 */
	private static final Binder binder = GWT.create(Binder.class);

	/**
	 * regexPoolMenuBarPanel {@link VerticalPanel} instance of VerticalPanel
	 */
	@UiField
	protected VerticalPanel regexPoolMenuBarPanel;

	/**
	 * regexPoolMenuBar {@link CustomMenuBar} contains different CustomMenuItems
	 */
	@UiField
	protected CustomMenuBar regexPoolMenuBar;

	/**
	 * exportFormPanel {@link FormPanel} instance of FormPanel
	 */
	protected FormPanel exportFormPanel;

	/**
	 * exportMenuItem {@link CustomMenuItem} used for exporting Regex Groups
	 */
	protected CustomMenuItem exportRegexPoolMenuItem;

	/**
	 * deleteMenuItem {@link CustomMenuItem} used for deleting Regex Groups
	 */
	protected CustomMenuItem deleteMenuItem;

	/**
	 * addMenuItem {@link CustomMenuItem} used for adding Regex Groups
	 */
	protected CustomMenuItem addMenuItem;

	/**
	 * openMenuItem {@link CustomMenuItem} used for opening the selected Regex Groups
	 */
	protected CustomMenuItem openMenuItem;

	/**
	 * URL for regex pool export servlet.
	 */
	private static final String EXPORT_REGEX_POOL_SERVLET = "gxt-system-config/exportRegexPool";

	/**
	 * instantiate RegexPoolOptions View
	 */
	public RegexPoolOptionsView() {
		super();
		initWidget(binder.createAndBindUi(this));
		exportFormPanel = new FormPanel();
		intializeMenuItems();
		regexPoolMenuBar.addItem(exportRegexPoolMenuItem);
		regexPoolMenuBar.addItem(openMenuItem);
		regexPoolMenuBar.addItem(addMenuItem);
		regexPoolMenuBar.addItem(deleteMenuItem);
		intializeSelectionHandlers();
		regexPoolMenuBarPanel.addStyleName("menubarPanelRegexPool");
		regexPoolMenuBarPanel.add(exportFormPanel);
		setWidgetIds();
		regexPoolMenuBar.addStyleName("menuBarRegexPool");
		regexPoolMenuBar.setFocusOnHoverEnabled(false);
		setFormPanel();
	}

	/**
	 * intializeMenuItems is used for initialising the custom menu items and adding the display name to the custom menu items
	 */
	private void intializeMenuItems() {
		exportRegexPoolMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(SystemConfigConstants.EXPORT_REGEX_POOL);
			}
		});

		deleteMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {

				return LocaleDictionary.getConstantValue(SystemConfigConstants.DELETE_BUTTON);
			}
		});

		addMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {

				return LocaleDictionary.getConstantValue(SystemConfigConstants.ADD_BUTTON);
			}
		});

		openMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {

				return LocaleDictionary.getConstantValue(SystemConfigConstants.OPEN_BUTTON);
			}
		});
	}

	/**
	 * intializeSelectionHandlers method used for adding functionality to the custom menu items which in invoked on the click of that
	 * custom menu item
	 */
	private void intializeSelectionHandlers() {
		exportRegexPoolMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				exportFormPanel.setAction(EXPORT_REGEX_POOL_SERVLET);
				presenter.onExportRegexPoolButtonClicked();
			}
		});

		deleteMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.deleteMultipleRegexGroups();
			}
		});

		addMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.addRegexGroup();
			}
		});

		openMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.openRegexPatternList();
			}
		});
	}

	private void setFormPanel() {
		exportFormPanel.setMethod(FormPanel.METHOD_POST);
		exportFormPanel.addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(final SubmitEvent event) {
			}
		});

		exportFormPanel.addSubmitCompleteHandler(new SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(final SubmitCompleteEvent event) {
				if (event.getResults().toLowerCase().indexOf(SystemConfigConstants.ERROR_CODE_TEXT) > -1) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(SystemConfigMessages.EXPORT_UNSUCCESSFUL), DialogIcon.ERROR);
					return;
				}
			}
		});
	}

	private void setWidgetIds() {
		WidgetUtil.setID(regexPoolMenuBarPanel, "RPOV_verticalPanel");
		WidgetUtil.setID(regexPoolMenuBar, "RPOV_menuBar");
		WidgetUtil.setID(exportRegexPoolMenuItem, "RPOV_exportRegexMenuItem");
		WidgetUtil.setID(exportFormPanel, "RPOV_formPanel");
		WidgetUtil.setID(addMenuItem, "RPOV_addRegexMenuItem");
		WidgetUtil.setID(deleteMenuItem, "RPOV_deleteRegexMenuItem");
		WidgetUtil.setID(openMenuItem, "RPOV_openRegexMenuItem");
	}

	public void submitExportFormPanel() {
		this.exportFormPanel.submit();
	}

}
