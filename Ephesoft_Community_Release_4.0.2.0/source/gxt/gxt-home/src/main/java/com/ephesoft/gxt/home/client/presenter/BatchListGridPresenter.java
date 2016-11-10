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

package com.ephesoft.gxt.home.client.presenter;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.EventUtil;
import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.home.client.BatchListServiceAsync;
import com.ephesoft.gxt.home.client.controller.BatchListController;
import com.ephesoft.gxt.home.client.event.BatchListOpenedEvent;
import com.ephesoft.gxt.home.client.event.GridLoadEvent;
import com.ephesoft.gxt.home.client.event.LoadBatchListChartsEvent;
import com.ephesoft.gxt.home.client.event.ReviewValidateOpenedEvent;
import com.ephesoft.gxt.home.client.i18n.BatchListConstants;
import com.ephesoft.gxt.home.client.proxy.BatchListRpcProxy;
import com.ephesoft.gxt.home.client.view.layout.BatchListGridView;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;

public class BatchListGridPresenter extends BatchListBasePresenter<BatchListGridView> {

	interface CustomEventBinder extends EventBinder<BatchListGridPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);
	private BatchListServiceAsync rpcService;

	public BatchListGridPresenter(BatchListController controller, BatchListGridView view) {
		super(controller, view);
		rpcService = controller.getRpcService();
		view.loadGrid();
	}

	@Override
	public void bind() {

	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	/**
	 * @return the rpcService
	 */
	public BatchListServiceAsync getRpcService() {
		return rpcService;
	}

	@EventHandler
	public void getData(final BatchListOpenedEvent batchListOpenedEvent) {
		view.getBatchListGrid().clearFilter();
		getData(batchListOpenedEvent.getStatus());
	}

	public void selectBatchInstance(CellSelectionChangedEvent<BatchInstanceDTO> cellSelectionChangeEvent) {
		if (null != cellSelectionChangeEvent) {
			BatchInstanceDTO selectedBatchInstance = EventUtil.getSelectedModel(cellSelectionChangeEvent);
			if (selectedBatchInstance != null) {
				controller.setSelectedBatchInstance(selectedBatchInstance);
			}
		}
	}

	public void gotoReviewAndValidatePage() {
		if (null == controller.getSelectedBatchInstance()) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchListConstants.ERROR_TITLE),
					LocaleDictionary.getConstantValue(BatchListConstants.PLEASE_SELECT_ATLEAST_ONE_BATCH), DialogIcon.ERROR);
			return;
		}
		String batchInstanceIdentifier = controller.getSelectedBatchInstance().getBatchIdentifier();
		String href = Window.Location.getHref();
		String baseUrl = href.substring(0, href.lastIndexOf('/'));
		StringBuffer newUrl = new StringBuffer();
		newUrl.append(baseUrl).append(BatchListConstants.REVIEW_VALIDATE_HTML_URL);
		if (!("0".equals(batchInstanceIdentifier))) {
			newUrl.append("?batch_id=").append(batchInstanceIdentifier);
		}
		Window.Location.assign(newUrl.toString());
	}

	@EventHandler
	public void gotoReviewAndValidatePage(final ReviewValidateOpenedEvent reviewValidateOpenedEvent) {
		gotoReviewAndValidatePage();
	}

	private void getData(String status) {
		BatchListRpcProxy proxy = view.getProxy();
		proxy.setDataFilterList(status);
		view.reloadGrid();
	}

	public void checkRowCount() {
		int gridCount = view.getBatchListGrid().getGridRowCount();
		controller.getEventBus().fireEvent(new GridLoadEvent(gridCount));
	}

	public void loadCharts() {
		controller.getEventBus().fireEvent(new LoadBatchListChartsEvent());
	}
}
