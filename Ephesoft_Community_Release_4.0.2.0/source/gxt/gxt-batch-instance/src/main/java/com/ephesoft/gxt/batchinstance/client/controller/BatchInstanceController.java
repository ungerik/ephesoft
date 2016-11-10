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

package com.ephesoft.gxt.batchinstance.client.controller;

import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.batchinstance.client.BatchInstanceManagementServiceAsync;
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstanceGridPresenter;
import com.ephesoft.gxt.batchinstance.client.presenter.BatchInstancePresenter;
import com.ephesoft.gxt.batchinstance.client.view.BatchInstanceGridView;
import com.ephesoft.gxt.batchinstance.client.view.BatchInstanceView;
import com.ephesoft.gxt.core.client.Controller;
import com.ephesoft.gxt.core.client.DCMARemoteServiceAsync;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;

public class BatchInstanceController extends Controller {

	private BatchInstancePresenter batchInstancePresenter;
	private BatchInstanceGridView batchInstanceGridView;
	private BatchInstanceGridPresenter batchInstanceGridPresenter;
	private BatchInstanceView batchInstanceView;
	private List<BatchInstanceDTO> batchInstanceList;
	private BatchInstanceDTO selectedBatchInstance;

	/**
	 * Map to store common name of all the users along with their full name.
	 * 
	 * {@link Map}<{@link String}, {@link String}>
	 */
	private Map<String, String> allUsers;
	private FilterPagingLoadConfig loadConfig;

	public BatchInstanceController(EventBus eventBus, DCMARemoteServiceAsync rpcService) {
		super(eventBus, rpcService);
	}

	@Override
	public Widget createView() {
		this.batchInstanceView = new BatchInstanceView();
		this.batchInstancePresenter = new BatchInstancePresenter(this, batchInstanceView);
		return this.batchInstanceView;
	}

	@Override
	public void injectEvents(EventBus eventBus) {

	}

	@Override
	public void refresh() {
	}

	@Override
	public BatchInstanceManagementServiceAsync getRpcService() {
		return (BatchInstanceManagementServiceAsync) super.getRpcService();
	}

	public BatchInstancePresenter getBatchInstancePresenter() {
		return batchInstancePresenter;
	}

	public BatchInstanceView getBatchInstanceView() {
		return batchInstanceView;
	}

	/**
	 * Gets the Map of all Users mapped with their full Name.
	 * 
	 * @return {@link Map}<{@link String}, {@link String}>
	 */
	public Map<String, String> getAllUsers() {
		return allUsers;
	}

	/**
	 * Sets the Map of all the Users.
	 * 
	 * @param allUsers {@link Map}<{@link String}, {@link String}>
	 */
	public void setAllUsers(final Map<String, String> allUsers) {
		this.allUsers = allUsers;
	}

	/**
	 * @return the batchInstanceGridView
	 */
	public BatchInstanceGridView getBatchInstanceGridView() {
		return batchInstanceGridView;
	}

	/**
	 * @return the batchInstanceGridPresenter
	 */
	public BatchInstanceGridPresenter getBatchInstanceGridPresenter() {
		return batchInstanceGridPresenter;
	}

	public void setBatchInstanceList(List<BatchInstanceDTO> batchInstanceList) {
		this.batchInstanceList = batchInstanceList;
	}

	/**
	 * @return the batchInstanceList
	 */
	public List<BatchInstanceDTO> getBatchInstanceList() {
		return batchInstanceList;
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

	/**
	 * Gets the left panel absolute width.
	 *
	 * @return the left panel absolute width
	 */
	public int getLeftPanelAbsoluteWidth() {
		return batchInstanceView.getBatchInstanceLeftPanelView().getOffsetWidth();
	}

	/**
	 * Sets the load config.
	 *
	 * @param loadConfig the new load config
	 */
	public void setLoadConfig(FilterPagingLoadConfig loadConfig) {
		this.loadConfig = loadConfig;
	}

	/**
	 * Gets the load config.
	 *
	 * @return the load config
	 */
	public FilterPagingLoadConfig getLoadConfig() {
		return loadConfig;
	}
}
