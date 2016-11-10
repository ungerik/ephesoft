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

package com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter;
import com.ephesoft.gxt.admin.client.view.tablecolumnextraction.ColumnExtractionRuleView;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.web.bindery.event.shared.binder.EventBinder;

/**
 * This presenter deals with Table Column Extraction Rule Configuration.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction.ColumnExtractionRulePresenter
 */
public class ColumnExtractionRulePresenter extends BatchClassCompositePresenter<ColumnExtractionRuleView, TableExtractionRuleDTO> {

	/** The column extraction rule grid presenter. */
	private final ColumnExtractionRuleGridPresenter colExtrRuleGridPresenter;

	/** The column extraction rule menu presenter. */
	private final ColumnExtractionRuleMenuPresenter colExtrRuleMenuPresenter;

	/**
	 * The Interface CustomEventBinder.
	 */
	interface CustomEventBinder extends EventBinder<ColumnExtractionRulePresenter> {
	}

	/** The Constant eventBinder. */
	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);
	/**
	 * Instantiates a new column extraction rule presenter.
	 * 
	 * @param controller the controller {@link BatchClassManagementController}
	 * @param view the view {@link ColumnExtractionRuleView}
	 */
	public ColumnExtractionRulePresenter(final BatchClassManagementController controller, final ColumnExtractionRuleView view) {
		super(controller, view);
		colExtrRuleGridPresenter = new ColumnExtractionRuleGridPresenter(controller, view.getColExtrGridView());
		colExtrRuleMenuPresenter = new ColumnExtractionRuleMenuPresenter(controller, view.getColExtrMenuView());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.event.BindHandler#bind()
	 */
	@Override
	public void bind() {
		controller.collapseBottomPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter#init(java.lang.Object)
	 */
	@Override
	public void init(final TableExtractionRuleDTO selectionParameter) {
		if (null != selectionParameter) {
			controller.setSelectedTableExtrRule(selectionParameter);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter#getParentPresenter()
	 */
	@Override
	public BatchClassCompositePresenter<?, ?> getParentPresenter() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter#getChildPresenter()
	 */
	@Override
	public BatchClassCompositePresenter<?, ?> getChildPresenter() {
		return null;
	}
	
	
	/**
	 * Gets the column extraction rule grid presenter.
	 *
	 * @return the column extraction rule grid presenter
	 */
	public ColumnExtractionRuleGridPresenter getColExtrRuleGridPresenter() {
		return colExtrRuleGridPresenter;
	}

	
	/**
	 * Gets the column extraction rule menu presenter.
	 *
	 * @return the column extraction rule menu presenter
	 */
	public ColumnExtractionRuleMenuPresenter getColExtrRuleMenuPresenter() {
		return colExtrRuleMenuPresenter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.core.client.BasePresenter#injectEvents(com.google.gwt.event.shared.EventBus)
	 */
	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.presenter.BatchClassCompositePresenter#isValid()
	 */
	public boolean isValid() {
		return colExtrRuleGridPresenter.isValid();
	}

}
