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

package com.ephesoft.gxt.admin.client.presenter;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.view.BatchClassCompositeView;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.util.ViewUtil;

@SuppressWarnings("rawtypes")
public abstract class BatchClassCompositePresenter<V extends BatchClassCompositeView, T> extends BatchClassInlinePresenter<V> {


	public BatchClassCompositePresenter(BatchClassManagementController controller, V view) {
		super(controller, view);
	}

	public final void layout(T selectionParameter) {
		this.init(selectionParameter);
		controller.nullifyExcluding(selectionParameter);
		this.init(selectionParameter);
		this.bind();
		this.refresh();
		BatchClassInlineView<?> bottomPanelView = view.getBottomPanelView();
		BatchClassInlineView<?> optionsPanelView = view.getOptionsPanelView();
		BatchClassInlineView<?> listPanelView = view.getListPanelView();
		ViewUtil.bindView(listPanelView);
		ViewUtil.bindView(optionsPanelView);
		ViewUtil.bindView(bottomPanelView);
		controller.setCurrentBindedPresenter(this);
		controller.setBottomPanelView(bottomPanelView);
		controller.setOptionsPanelView(optionsPanelView);
		controller.setListPanelView(listPanelView);
		controller.setBottomPanelHeader(view.getBottomHeader());
	}

	public abstract void init(T selectionParameter);
	
	public final void refresh() {
		BatchClassInlineView<?> bottomPanelView = view.getBottomPanelView();
		BatchClassInlineView<?> optionsPanelView = view.getOptionsPanelView();
		BatchClassInlineView<?> listPanelView = view.getListPanelView();
		if(null != bottomPanelView) {
			bottomPanelView.refresh();
		}
		if(null != optionsPanelView) {
			optionsPanelView.refresh();
		}
		if(null != listPanelView) {
			listPanelView.refresh();
		}
	}

	public abstract BatchClassCompositePresenter<?, ?> getParentPresenter();

	public abstract BatchClassCompositePresenter<?, ?> getChildPresenter();
	
	public boolean isValid(){
		return true;
	}
}
