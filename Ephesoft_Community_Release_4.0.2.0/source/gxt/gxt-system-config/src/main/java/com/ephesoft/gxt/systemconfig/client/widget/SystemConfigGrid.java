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

package com.ephesoft.gxt.systemconfig.client.widget;

import java.util.List;

import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.shared.dto.Selectable;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.systemconfig.client.controller.SystemConfigController.SystemConfigEventBus;
import com.ephesoft.gxt.systemconfig.client.event.ModelSelectionEvent;
import com.ephesoft.gxt.systemconfig.client.event.NavigationEvent.NavigationNodeAdditionEvent;
import com.ephesoft.gxt.systemconfig.client.event.NavigationEvent.NavigationNodeDeletionEvent;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelection;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

public class SystemConfigGrid<T extends Selectable> extends Grid<T> {

	private T currentSeletedModel;

	public SystemConfigGrid(RpcProxy<FilterPagingLoadConfig, PagingLoadResult<T>> proxy, PropertyAccessModel propertyAccessModel,
			boolean isEditable) {
		super(proxy, propertyAccessModel, isEditable);
		addCellSelectionChangeEvent();
	}

	public SystemConfigGrid(final List<T> dataToAdd, final PropertyAccessModel propertyAccessModel, final boolean isEditable,
			final RpcProxy<FilterPagingLoadConfig, PagingLoadResult<T>> proxy) {
		super(dataToAdd, propertyAccessModel, isEditable, proxy);
		addCellSelectionChangeEvent();
	}

	public SystemConfigGrid(final PropertyAccessModel propertyAccessModel) {
		super(propertyAccessModel);
		addCellSelectionChangeEvent();
	}

	public SystemConfigGrid(final List<T> dataToAdd, final PropertyAccessModel propertyAccessModel) {
		super(dataToAdd, propertyAccessModel, true, null);
		addCellSelectionChangeEvent();
	}

	private void addCellSelectionChangeEvent() {
		GridSelectionModel<T> selectionModel = getSelectionModel();
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<T> cellSelectionModel = ((CellSelectionModel<T>) selectionModel);

			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<T>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<T> event) {
					List<CellSelection<T>> cellSelection = event.getSelection();
					if (!CollectionUtil.isEmpty(cellSelection)) {
						CellSelection<T> selectedCell = cellSelection.get(0);
						if (null != selectedCell) {
							currentSeletedModel = selectedCell.getModel();
							SystemConfigEventBus.fireEvent(new ModelSelectionEvent(selectedCell.getModel()));
						}
					}
				}
			});
		}
	}

	@Override
	public boolean addNewItemInGrid(T item) {
		boolean itemAdded = super.addNewItemInGrid(item);
		if (itemAdded) {
			SystemConfigEventBus.fireEvent(new NavigationNodeAdditionEvent(item));
		}
		return itemAdded;
	}

	@Override
	public void removeItemsFromGrid(final List<T> itemList) {
		super.removeItemsFromGrid(itemList);
		if (!CollectionUtil.isEmpty(itemList)) {
			for (T deletedObject : itemList) {
				SystemConfigEventBus.fireEvent(new NavigationNodeDeletionEvent(deletedObject));
			}
		}
	}

	public void refreshGrid(boolean isRefreshCurrentRow) {

		if (currentSeletedModel != null) {
			refreshGrid(currentSeletedModel, isRefreshCurrentRow);
		}
	}

}
