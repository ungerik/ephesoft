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

package com.ephesoft.gxt.admin.client.view.regexbuilder;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.regexbuilder.RegexBuilderPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.widget.RegexComboBoxHandler;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent.BeforeHideHandler;

/**
 * Shows the view of regex builder
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see RegexBuilderMainView
 * 
 */
public class RegexBuilderView extends BatchClassInlineView<RegexBuilderPresenter> {

	@UiField
	protected RegexBuilderToolBar regexBuilderToolBar;

	/**
	 * regexBuilderMainView {@link RegexBuilderMainView}
	 */

	@UiField
	protected RegexBuilderMainView regexBuilderMainView;

	protected RegexComboBoxHandler regexComboBoxHandler;

	private DialogWindow regexDialogWindow;

	/**
	 * 
	 * Interface for the Binder.
	 * 
	 * @author Ephesoft
	 * 
	 *         <b>created on</b> 19-Dec-2014 <br/>
	 * @version $LastChangedDate:$ <br/>
	 *          $LastChangedRevision:$ <br/>
	 */
	interface Binder extends UiBinder<VerticalPanel, RegexBuilderView> {
	}

	/**
	 * BINDER {@link Binder}.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * instantiate Regex Builder View
	 */

	public RegexBuilderView() {
		super();
		initWidget(BINDER.createAndBindUi(this));

	}

	/**
	 * Getter of RegexBuilderMainView
	 * 
	 * @return {@link RegexBuilderMainView}
	 */
	public RegexBuilderMainView getRegexBuilderMainView() {
		return regexBuilderMainView;
	}

	public RegexBuilderToolBar getRegexBuilderToolBar() {
		return regexBuilderToolBar;
	}

	public RegexComboBoxHandler getRegexComboBoxHandler() {
		return regexComboBoxHandler;
	}

	public void setRegexComboBoxHandler(RegexComboBoxHandler regexComboBoxHandler) {
		this.regexComboBoxHandler = regexComboBoxHandler;
	}

	public void hideRegexDialogWindow() {
		regexDialogWindow.remove(this);
		regexDialogWindow.hide();
	}

	public void showDialogWindow(String previousPattern) {
		regexDialogWindow = new DialogWindow(false, true, regexBuilderMainView.getMatchStringTextArea());
		this.dialogWindowClosingHandler();
		regexDialogWindow.addStyleName("regexDialogWindow");
		regexDialogWindow.setModal(true);
		regexDialogWindow.setOnEsc(false);
		regexDialogWindow.setResizable(true);
		regexDialogWindow.setDraggable(true);
		regexDialogWindow.setFocusWidget(regexBuilderMainView.getRegexBuilderDetailView().getRegexFieldDetailView().getStartsEndsStringComboBox());
		regexDialogWindow.add(this);
		//regexBuilderMainView.setResultTextAreaOnFormLoad(previousPattern);
		regexDialogWindow.setHeadingText(LocaleDictionary.getConstantValue(BatchClassConstants.REGEX_BUILDER));
		regexDialogWindow.show();

	}

	public void dialogWindowClosingHandler() {
		regexDialogWindow.addBeforeHideHandler(new BeforeHideHandler() {

			@Override
			public void onBeforeHide(BeforeHideEvent event) {
				presenter.beforeHideHandler();

			}
		});
	}
}
