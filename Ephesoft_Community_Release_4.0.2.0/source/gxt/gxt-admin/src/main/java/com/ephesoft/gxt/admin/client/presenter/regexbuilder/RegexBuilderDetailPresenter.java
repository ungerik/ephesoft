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

package com.ephesoft.gxt.admin.client.presenter.regexbuilder;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.presenter.BatchClassInlinePresenter;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexBuilderDetailView;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;

/**
 * Provides the functionality to handle events for RegexBuilderDetailView.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see BatchClassInlinePresenter
 * @see RegexBuilderDetailView
 * @see RegexGroupDetailPresenter
 * @see RegexQuantifierDetailPresenter
 * @see RegexFieldDetailPresenter
 */
public class RegexBuilderDetailPresenter extends BatchClassInlinePresenter<RegexBuilderDetailView> {

	/**
	 * regexGroupDetailPresenter {@link RegexGroupDetailPresenter} The instance of regexGroupDetailPresenter.
	 */
	private final RegexGroupDetailPresenter regexGroupDetailPresenter;

	/**
	 * regexQuantifierDetailPresenter {@link RegexQuantifierDetailPresenter} The instance of regexQuantifierDetailPresenter.
	 */
	private final RegexQuantifierDetailPresenter regexQuantifierDetailPresenter;

	/**
	 * regexFieldDetailPresenter {@link RegexFieldDetailPresenter} The instance of regexFieldDetailPresenter.
	 */
	private final RegexFieldDetailPresenter regexFieldDetailPresenter;

	/**
	 * Constructor of the class.
	 * 
	 * @param controller {@link BatchClassManagementController} The instance of BatchClassManagementController.
	 * @param view {@link RegexBuilderDetailView} The regex builder detail view.
	 */
	public RegexBuilderDetailPresenter(final BatchClassManagementController controller, final RegexBuilderDetailView view) {
		super(controller, view);
		regexGroupDetailPresenter = new RegexGroupDetailPresenter(controller, view.getRegexGroupDetailView());
		regexQuantifierDetailPresenter = new RegexQuantifierDetailPresenter(controller, view.getRegexQuantifierDetailView());
		regexFieldDetailPresenter = new RegexFieldDetailPresenter(controller, view.getRegexFieldDetailView());
	}

	@Override
	public void bind() {
		// This method is called when the bind event is being fired.
	}

	/**
	 * @return the regexGroupDetailPresenter
	 */
	public RegexGroupDetailPresenter getRegexGroupDetailPresenter() {
		return regexGroupDetailPresenter;
	}

	/**
	 * @return the regexQuantifierDetailPresenter
	 */
	public RegexQuantifierDetailPresenter getRegexQuantifierDetailPresenter() {
		return regexQuantifierDetailPresenter;
	}

	/**
	 * @return the regexFieldDetailPresenter
	 */
	public RegexFieldDetailPresenter getRegexFieldDetailPresenter() {
		return regexFieldDetailPresenter;
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		// TODO Auto-generated method stub

	}

}
