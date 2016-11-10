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

package com.ephesoft.gxt.systemconfig.client.presenter.application.regexpool;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.AddRegexGroupEvent;
import com.ephesoft.gxt.systemconfig.client.event.RetreiveSelectedRegexGroupEvent;
import com.ephesoft.gxt.systemconfig.client.event.StartMultipleRegexGroupDeletionEvent;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigMessages;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigInlinePresenter;
import com.ephesoft.gxt.systemconfig.client.view.application.regexpool.RegexPoolOptionsView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.gwt.user.client.Window;
/**
 * This class is a presenter for RegexPatternToolBar.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 30-Dec-2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RegexPoolOptionsPresenter extends SystemConfigInlinePresenter<RegexPoolOptionsView> {

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<RegexPoolOptionsPresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * Constructor.
	 * 
	 * @param controller {@link SystemConfigController}
	 * @param view {@link RegexPoolOptionsView}
	 */
	public RegexPoolOptionsPresenter(SystemConfigController controller, RegexPoolOptionsView view) {
		super(controller, view);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.event.BindHandler#bind()
	 */
	@Override
	public void bind() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, controller.getEventBus());
	}

	public void onExportRegexPoolButtonClicked() {
		if(controller.getRegexPoolView().getRegexGroupView().getRegexGroupGrid().getGrid().isGridValidated()) {
			view.submitExportFormPanel();
		} else {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(SystemConfigConstants.ERROR_TITTLE),
					LocaleDictionary.getMessageValue(SystemConfigMessages.VALIDATE_REGEX_GROUPS_BEFORE_EXPORT), DialogIcon.ERROR);
		}
		
	}

	/**
	 * deleteMultipleRegexGroups method used to fire StartMultipleRegexGroupDeletionEvent
	 */
	public void deleteMultipleRegexGroups() {
		controller.getEventBus().fireEvent(new StartMultipleRegexGroupDeletionEvent());
	}

	/**
	 * openRegexPatternList method is used to fire RegexGroupSelectionEvent
	 */
	public void openRegexPatternList() {
		controller.getEventBus().fireEvent(new RetreiveSelectedRegexGroupEvent());
		//Commented Code to make Open functionality based on checkbox.
//		if (controller.getRegexPoolView().getRegexGroupView().getRegexGroupGrid().isValid()) {
//			if (null != controller.getSelectedRegexGroupDTO()) {
//				controller.getEventBus().fireEvent(new RegexGroupSelectionEvent(controller.getSelectedRegexGroupDTO()));
//			} else {
//				DialogUtil.showMessageDialog(SystemConfigConstants.ERROR, SystemConfigMessages.PLEASE_SOME_REGEX_GROUP_TO_OPEN,
//						DialogIcon.ERROR);
//			}
//		}
	}

	/**
	 * addRegexGroup method used to fire AddRegexGroupEvent
	 */
	public void addRegexGroup() {
		controller.getEventBus().fireEvent(new AddRegexGroupEvent());
	}
}
