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

package com.ephesoft.gxt.home.client.controller;

import com.ephesoft.gxt.core.client.Controller;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.home.client.BatchListServiceAsync;
import com.ephesoft.gxt.home.client.presenter.BatchListGridPresenter;
import com.ephesoft.gxt.home.client.presenter.BatchListLeftPanelPresenter;
import com.ephesoft.gxt.home.client.presenter.BatchListMenuPresenter;
import com.ephesoft.gxt.home.client.view.BatchListLeftPanelView;
import com.ephesoft.gxt.home.client.view.layout.BatchListGridView;
import com.ephesoft.gxt.home.client.view.layout.BatchListLayout;
import com.ephesoft.gxt.home.client.view.layout.BatchListMenuView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.binder.EventBinder;

public class BatchListController extends Controller {

	interface CustomEventBinder extends EventBinder<BatchListController> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);
	private BatchListLayout batchListLayout;

	private BatchListGridView batchListGridView;
	private BatchListMenuView batchListMenuView;
	private BatchListLeftPanelView batchListLeftPanelView;

	private BatchListMenuPresenter batchListMenuPresenter;
	private BatchListGridPresenter batchListGridPresenter;
	private BatchListLeftPanelPresenter batchListLeftPanelPresenter;
	private BatchInstanceDTO selectedBatchInstance;

	public BatchListController(EventBus eventBus, DCMARemoteServiceAsync rpcService) {
		super(eventBus, rpcService);
		batchListLayout = new BatchListLayout();
		BatchListEventBus.registerEventBus(eventBus);
		this.initializeView();
		this.initializePresenter();
	}

	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	public Widget createView() {
		return batchListLayout;
	}

	public void refresh() {

	}

	public BatchListServiceAsync getRpcService() {
		return (BatchListServiceAsync) super.getRpcService();
	}

	public static class BatchListEventBus {

		private static EventBus eventBus;

		private BatchListEventBus() {
		}

		public static void fireEvent(final GwtEvent<?> event) {
			if (null != eventBus) {
				eventBus.fireEvent(event);
			}
		}

		private static void registerEventBus(final EventBus eventBus) {
			BatchListEventBus.eventBus = eventBus;
		}
	}

	/**
	 * @return the batchListGridView
	 */
	public BatchListGridView getBatchListGridView() {
		return batchListGridView;
	}

	private void initializeView() {
		batchListGridView = batchListLayout.getBatchListGridView();
		batchListMenuView = batchListLayout.getBatchListMenuView();
		batchListLeftPanelView = batchListLayout.getBatchListLeftPanelView();

	}

	private void initializePresenter() {
		batchListMenuPresenter = new BatchListMenuPresenter(this, batchListMenuView);
		batchListLeftPanelPresenter = new BatchListLeftPanelPresenter(this, batchListLeftPanelView);
		batchListGridPresenter = new BatchListGridPresenter(this, batchListGridView);
	}

	/**
	 * @return the batchListLayout
	 */
	public BatchListLayout getBatchListLayout() {
		return batchListLayout;
	}

	/**
	 * @return the batchListMenuView
	 */
	public BatchListMenuView getBatchListMenuView() {
		return batchListMenuView;
	}

	/**
	 * @return the batchListMenuPresenter
	 */
	public BatchListMenuPresenter getBatchListMenuPresenter() {
		return batchListMenuPresenter;
	}

	/**
	 * @return the batchListGridPresenter
	 */
	public BatchListGridPresenter getBatchListGridPresenter() {
		return batchListGridPresenter;
	}

	/**
	 * Gets the batch list left panel presenter.
	 * 
	 * @return the batch list left panel presenter
	 */
	public BatchListLeftPanelPresenter getBatchListLeftPanelPresenter() {
		return batchListLeftPanelPresenter;
	}

	/**
	 * @return the selectedBatchInstance
	 */
	public BatchInstanceDTO getSelectedBatchInstance() {
		return selectedBatchInstance;
	}

	/**
	 * @param selectedBatchInstance the selectedBatchInstance to set
	 */
	public void setSelectedBatchInstance(BatchInstanceDTO selectedBatchInstance) {
		this.selectedBatchInstance = selectedBatchInstance;
	}

}
