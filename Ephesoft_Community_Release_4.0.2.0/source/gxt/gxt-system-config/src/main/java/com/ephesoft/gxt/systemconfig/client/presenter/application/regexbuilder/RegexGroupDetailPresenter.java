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

package com.ephesoft.gxt.systemconfig.client.presenter.application.regexbuilder;

import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController;
import com.ephesoft.gxt.systemconfig.client.event.regexbuilder.RegexEvent;
import com.ephesoft.gxt.systemconfig.client.presenter.SystemConfigInlinePresenter;
import com.ephesoft.gxt.systemconfig.client.view.application.regexbuilder.RegexGroupDetailView;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * Provides the functionality to handle events for RegexGroupDetailView. This presenter handles the events for regex group section of
 * the regex builder view.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see SystemConfigInlinePresenter
 * @see RegexGroupDetailView
 */
public class RegexGroupDetailPresenter extends SystemConfigInlinePresenter<RegexGroupDetailView> {

	/**
	 * Constructor of the class.
	 * 
	 * @param controller {@link BatchClassManagementController} The instance of BatchClassManagementController.
	 * @param view {@link RegexGroupDetailView} The regex group detail view.
	 */
	public RegexGroupDetailPresenter(final SystemConfigController controller, final RegexGroupDetailView view) {
		super(controller, view);
	}

	@Override
	public void bind() {
		// This method is called when the bind event is being fired.
	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Also if the captureGroupCheckBox is checked, this will make disable
	 * the nonCaptureGroupCheckBox otherwise it will make enabled the nonCaptureGroupCheckBox.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	public void onCaptureGroupCheckBoxChecked(final ClickEvent clickEvent) {
		if (((CheckBox) clickEvent.getSource()).getValue()) {
			view.getNonCaptureGroupCheckBox().setEnabled(false);
			view.getNonCaptureGroupCheckBox().setValue(false);
		} else {
			view.getNonCaptureGroupCheckBox().setEnabled(true);
		}
		controller.getEventBus().fireEvent(new RegexEvent());
	}

	/**
	 * Fires the RegexEvent for the generation of Regex Pattern. Also if the nonCaptureGroupCheckBox is checked, this will make disable
	 * the captureGroupCheckBox otherwise it will make enabled the captureGroupCheckBox.
	 * 
	 * @param clickEvent {@link ClickEvent} The instance of ClickEvent.
	 */
	public void onNonCaptureGroupCheckBoxChecked(final ClickEvent clickEvent) {
		if (((CheckBox) clickEvent.getSource()).getValue()) {
			view.getCaptureGroupCheckBox().setEnabled(false);
			view.getCaptureGroupCheckBox().setValue(false);
		} else {
			view.getCaptureGroupCheckBox().setEnabled(true);
		}
		controller.getEventBus().fireEvent(new RegexEvent());
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

}
