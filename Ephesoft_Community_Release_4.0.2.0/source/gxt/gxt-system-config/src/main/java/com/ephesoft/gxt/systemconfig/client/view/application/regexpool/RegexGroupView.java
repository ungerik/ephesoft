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

import com.ephesoft.gxt.systemconfig.client.presenter.application.regexpool.RegexGroupViewPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigCompositeView;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

/**
 * RegexGroupView is the composite view that shows view of RegexGroup Grid in center panel
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 30-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RegexGroupView extends SystemConfigCompositeView<RegexGroupViewPresenter> {

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
	interface Binder extends UiBinder<Widget, RegexGroupView> {
	}

	/**
	 * BINDER {@link Binder}.
	 */
	private static final Binder binder = GWT.create(Binder.class);

	/**
	 * regexGroupGrid {@link RegexGroupSelectionGridView} is the instance of RegexGroupSelectionGridView
	 */
	@UiField
	protected RegexGroupSelectionGridView regexGroupGrid;

	/**
	 * instantiate RegexGroup View
	 */
	public RegexGroupView() {

		super();
		initWidget(binder.createAndBindUi(this));
	}

	/**
	 * getter for top panel view of the Regex Group View
	 */
	@Override
	public SystemConfigInlineView<?> getTopPanelView() {
		return null;
	}

	/**
	 * getter for bottom panel view of the Regex Group View
	 */
	@Override
	public SystemConfigInlineView<?> getBottomPanelView() {
		return null;
	}

	/**
	 * getter for center panel view of the Regex Group View return {@link regexGroupGrid}
	 */
	@Override
	public SystemConfigInlineView<?> getCenterPanelView() {
		return regexGroupGrid;
	}

	/**
	 * Getter of regex group grid
	 * 
	 * @return {@link RegexGroupSelectionGridView}
	 */
	public RegexGroupSelectionGridView getRegexGroupGrid() {
		return regexGroupGrid;
	}
}
