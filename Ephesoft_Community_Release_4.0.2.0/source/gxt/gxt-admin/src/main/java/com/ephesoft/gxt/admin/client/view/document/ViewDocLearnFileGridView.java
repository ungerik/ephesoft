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

package com.ephesoft.gxt.admin.client.view.document;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.presenter.document.ViewDocLearnFileGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.widget.BatchClassManagementGrid;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.ViewLearnFileDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.ViewLearnFilesProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;

public class ViewDocLearnFileGridView extends BatchClassInlineView<ViewDocLearnFileGridPresenter> {

	interface Binder extends UiBinder<Widget, ViewDocLearnFileGridView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected VerticalPanel gridViewMainPanel;


	@UiField(provided = true)
	protected Grid<ViewLearnFileDTO> viewLearnFileGrid;

	@Override
	public void initialize() {
		viewLearnFileGrid = new Grid<ViewLearnFileDTO>(PropertyAccessModel.LEARN_FILES);
	}

	public ViewDocLearnFileGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("viewLearnFileGridMainPanel");
		viewLearnFileGrid.addStyleName("viewLearnFileGrid");
		viewLearnFileGrid.getView().setStripeRows(false);
		viewLearnFileGrid.setHasPagination(false);
		viewLearnFileGrid.getView().setViewConfig(new GridViewConfig<ViewLearnFileDTO>() {

			@Override
			public String getRowStyle(ViewLearnFileDTO model, int rowIndex) {
				// TODO Auto-generated method stub
				return null;
			}
			@Override

			public String getColStyle(ViewLearnFileDTO model, ValueProvider<? super ViewLearnFileDTO, ?> valueProvider, int rowIndex,
					int colIndex) {
				 String learnFileStyleName="";

				if (valueProvider == ViewLearnFilesProperties.INSTANCE.isLuceneLearned()) {
					if (model.isLuceneLearned()) {
						learnFileStyleName = "greenCellStyleName";
					}
					else {
						learnFileStyleName = "redCellStyleName";

					}
				}
				if (valueProvider == ViewLearnFilesProperties.INSTANCE.isImageClassified()) {
					if (model.isImageClassified()) {
						learnFileStyleName = "greenCellStyleName";
					}
					else {
						learnFileStyleName = "redCellStyleName";
					}
				}
				return learnFileStyleName;
			}
		});
	}

	public void reloadGrid() {
		 WidgetUtil.reLoadGrid(viewLearnFileGrid);
	}

	public void setData(Collection<ViewLearnFileDTO> viewLearnFileDtls) {
		viewLearnFileGrid.getMemoryData().clear();
		viewLearnFileGrid.setMemoryData(viewLearnFileDtls);
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public Grid<ViewLearnFileDTO> getLearnFileGrid() {
		return viewLearnFileGrid;
	}

	public List<ViewLearnFileDTO> getSelectedDocumentTypes() {
		return viewLearnFileGrid.getSelectedModels();
	}

}
