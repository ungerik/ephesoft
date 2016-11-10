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

import com.ephesoft.gxt.admin.client.presenter.regexbuilder.RegexBuilderDetailPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Shows the Input section of Regex Builder. This view combines the Regex field view, regex quantifier view and regex group view.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 19-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 * @see RegexGroupDetailView
 * @see RegexQuantifierDetailView
 * @see RegexFieldDetailView
 * @see RegexBuilderDetailPresenter
 */
public class RegexBuilderDetailView extends BatchClassInlineView<RegexBuilderDetailPresenter> {

	/**
	 * regexScrollPanel {@link ScrollPanel} The scroll panel to be shown.
	 */
	@UiField
	protected ScrollPanel regexScrollPanel;

	/**
	 * regexGroupDetailView {@link RegexGroupDetailView} The instance of Regex group detail view.
	 */
	@UiField
	protected RegexGroupDetailView regexGroupDetailView;

	/**
	 * regexQuantifierDetailView {@link RegexQuantifierDetailView} The instance of Regex quantifier detail view.
	 */
	@UiField
	protected RegexQuantifierDetailView regexQuantifierDetailView;

	/**
	 * regexFieldDetailView {@link RegexFieldDetailView} The instance of Regex field detail view.
	 */
	@UiField
	protected RegexFieldDetailView regexFieldDetailView;

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
	interface Binder extends UiBinder<VerticalPanel, RegexBuilderDetailView> {
	}

	/**
	 * BINDER {@link Binder}.
	 */
	private static final Binder BINDER = GWT.create(Binder.class);

	/**
	 * Instantiates the RegexBuilderDetailView.
	 */
	public RegexBuilderDetailView() {
		super();

		initWidget(BINDER.createAndBindUi(this));

		// Add CSS to the component of the view.
		addCSSStyles();
	}

	/**
	 * Add CSS to the component of the view.
	 */
	private void addCSSStyles() {

		// Add CSS for scroll panel.
		regexScrollPanel.addStyleName(CoreCommonConstants.REGEX_BUILDER_SCROLL_CSS);
	}

	/**
	 * Getter for the regexGroupDetailView.
	 * 
	 * @return {@link RegexGroupDetailView} The instance of regexGroupDetailView.
	 */
	public RegexGroupDetailView getRegexGroupDetailView() {
		return regexGroupDetailView;
	}

	/**
	 * Getter for the regexQuantifierDetailView.
	 * 
	 * @return {@link RegexQuantifierDetailView} The instance of regexQuantifierDetailView.
	 */
	public RegexQuantifierDetailView getRegexQuantifierDetailView() {
		return regexQuantifierDetailView;
	}

	/**
	 * Getter for the regexFieldDetailView.
	 * 
	 * @return {@link RegexFieldDetailView} The instance of regexFieldDetailView.
	 */
	public RegexFieldDetailView getRegexFieldDetailView() {
		return regexFieldDetailView;
	}

}
