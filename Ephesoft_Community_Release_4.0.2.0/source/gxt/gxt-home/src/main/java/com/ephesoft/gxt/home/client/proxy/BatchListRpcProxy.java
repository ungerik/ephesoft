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

package com.ephesoft.gxt.home.client.proxy;

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.shared.dto.BatchInstanceDTO;
import com.ephesoft.gxt.core.shared.dto.DataFilter;
import com.ephesoft.gxt.home.client.BatchListServiceAsync;
import com.ephesoft.gxt.home.client.i18n.BatchListConstants;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;

public class BatchListRpcProxy  extends RpcProxy<FilterPagingLoadConfig, PagingLoadResult<BatchInstanceDTO>>{

	private BatchListServiceAsync rpcService = null;

	private List<DataFilter> dataFilterList = null;
	
	public BatchListRpcProxy() {
		dataFilterList = new ArrayList<DataFilter>(1);
		setDataFilterList("9");
	}
	

	@Override
	public void load(final FilterPagingLoadConfig loadConfig, final AsyncCallback<PagingLoadResult<BatchInstanceDTO>> callback) {
		if (rpcService != null) {
			rpcService.getBatchInstanceDTOs(dataFilterList, loadConfig, callback);
		}
	}

	/**
	 * @param rpcService the rpcService to set
	 */
	public void setRpcService(BatchListServiceAsync rpcService) {
		this.rpcService = rpcService;
	}


	public void setDataFilterList(String status)
	{
		dataFilterList.clear();
		dataFilterList.add(new DataFilter(BatchListConstants.STATUS, status));
		dataFilterList.add(new DataFilter(BatchListConstants.NAME, ""));
		dataFilterList.add(new DataFilter(BatchListConstants.IDENTIFIER, ""));
	}


	/**
	 * @return the dataFilterList
	 */
	public List<DataFilter> getDataFilterList() {
		return dataFilterList;
	}


	/**
	 * @param dataFilterList the dataFilterList to set
	 */
	public void setDataFilterList(List<DataFilter> dataFilterList) {
		this.dataFilterList = dataFilterList;
	}


	/**
	 * @return the rpcService
	 */
	public BatchListServiceAsync getRpcService() {
		return rpcService;
	}
}
