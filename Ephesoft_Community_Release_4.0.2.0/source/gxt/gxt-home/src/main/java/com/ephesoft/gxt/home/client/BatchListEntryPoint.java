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

package com.ephesoft.gxt.home.client;


import com.ephesoft.gxt.core.client.DCMAEntryPoint;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.i18n.LocaleInfo;
import com.ephesoft.gxt.core.client.ui.widget.EphesoftManagementLayout.EphesoftLayoutManager;
import com.ephesoft.gxt.home.client.controller.BatchListController;
import com.ephesoft.gxt.home.client.i18n.BatchListConstants;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class BatchListEntryPoint extends DCMAEntryPoint<BatchListServiceAsync>{

	@Override
	public void onLoad() {
		BatchListController controller = new BatchListController(eventBus, rpcService);
		RootPanel.get().add(controller.createView());
		//addPageMenu();
	}

	@Override
	public BatchListServiceAsync createRpcService() {
		return GWT.create(BatchListService.class);
	}

	@Override
	public String getHomePage() {
		return LocaleCommonConstants.BATCH_LIST_HTML;
	}

	@Override
	public LocaleInfo createLocaleInfo(String locale) {
		return new LocaleInfo(locale, BatchListConstants.BATCH_LIST_CONSTANTS, BatchListConstants.BATCH_LIST_MESSAGES);
	}

	
	public void addPageMenu() {
		Label viewBatchesLabel = new Label();
		viewBatchesLabel.addStyleName(BatchListConstants.VIEW_BATCH_IMAGE_CSS);
		EphesoftLayoutManager.addURLForNavigation(viewBatchesLabel, LocaleDictionary.getConstantValue(BatchListConstants.TAB_LABEL_HOME),BatchListConstants.BATCH_LIST_HTML_URL);
		
		Label reviewValidateLabel = new Label();
		reviewValidateLabel.addStyleName(BatchListConstants.REVIEW_VALIDATE_IMAGE_CSS);
		EphesoftLayoutManager.addURLForNavigation(reviewValidateLabel, LocaleDictionary.getConstantValue(BatchListConstants.TAB_LABEL_BATCH_DETAIL),BatchListConstants.REVIEW_VALIDATE_HTML_URL);
		
		Label scannerLabel = new Label();
		scannerLabel.addStyleName(BatchListConstants.SCANNER_IMAGE_CSS);
		EphesoftLayoutManager.addURLForNavigation(scannerLabel, LocaleDictionary.getConstantValue(BatchListConstants.TAB_LABEL_WEB_SCANNER),BatchListConstants.WEBSCANNER_HTML_URL);
		
		Label uploadBatchLabel = new Label();
		uploadBatchLabel.addStyleName(BatchListConstants.UPLOAD_BATCH_IMAGE_CSS);
		EphesoftLayoutManager.addURLForNavigation(uploadBatchLabel, LocaleDictionary.getConstantValue(BatchListConstants.TAB_LABEL_UPLOAD_BATCH),BatchListConstants.UPLOAD_BATCH_HTML_URL);
		
	}

	@Override
	public com.ephesoft.gxt.core.client.DCMAEntryPoint.ScreenType getScreenType() {
		return ScreenType.OPERATOR;
	}
	
}
