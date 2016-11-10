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

package com.ephesoft.gxt.admin.client.view.tableinfo;

import com.ephesoft.gxt.admin.client.presenter.tableinfo.TableInfoPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassCompositeView;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;

/**
 * This View deals with Table Info Configuration.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.gxt.admin.client.view.tableinfo.TableInfoView
 */
public class TableInfoView extends BatchClassCompositeView<TableInfoPresenter> {

	/** The table info grid view. */
	private final TableInfoGridView tableInfoGridView;

	/** The table info menu view. */
	public TableInfoMenuView tableInfoMenuView;

	/** The table info import view. */
	public TableInfoImportView tableInfoImportView;
	
	public TableInfoImportView getTableInfoImportView() {
		return tableInfoImportView;
	}

	/**
	 * Instantiates a new table info view.
	 */
	public TableInfoView() {
		tableInfoGridView = new TableInfoGridView();
		tableInfoMenuView = new TableInfoMenuView();
		tableInfoImportView = new TableInfoImportView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.view.BatchClassCompositeView#getOptionsPanelView()
	 */
	@Override
	public BatchClassInlineView<?> getOptionsPanelView() {
		return tableInfoMenuView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.view.BatchClassCompositeView#getBottomPanelView()
	 */
	@Override
	public BatchClassInlineView<?> getBottomPanelView() {
		return tableInfoImportView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ephesoft.gxt.admin.client.view.BatchClassCompositeView#getListPanelView()
	 */
	@Override
	public BatchClassInlineView<?> getListPanelView() {
		return tableInfoGridView;
	}

	/**
	 * Gets the table info grid view.
	 * 
	 * @return the table info grid view {@link TableInfoGridView}
	 */
	public TableInfoGridView getTableInfoGridView() {
		return tableInfoGridView;
	}

	/**
	 * Gets the table info menu view.
	 * 
	 * @return the table info menu view {@link TableInfoMenuView}
	 */
	public TableInfoMenuView getTableInfoMenuView() {
		return tableInfoMenuView;
	}
	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String getBottomHeader() {
		return "Import File(s) for Test Table Extraction";
	}

}
