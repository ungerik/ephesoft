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

package com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction;

import com.ephesoft.gxt.admin.client.presenter.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionPresenter;

public class AdvancedKVExtractionView extends
		AdvancedKVExtractionCompositeView<AdvancedKVExtractionPresenter> {

	protected AdvancedKVExtractionButtonPanelView menuPanel;

	protected AdvancedKVExtractionInputPanelView formPanel;

	protected AdvancedKVExtractionImportView dropFilePanel;

	protected AdvancedKVExtractionImageView imagePanel;
	
	protected AdvanvcedKVExtractionGridView gridPanel;

	@Override
	public void initialize() {

	}

	/**
	 * Constructor.
	 */
	public AdvancedKVExtractionView() {
		super();

		menuPanel = new AdvancedKVExtractionButtonPanelView();

		formPanel = new AdvancedKVExtractionInputPanelView();

		dropFilePanel = new AdvancedKVExtractionImportView();

		imagePanel = new AdvancedKVExtractionImageView();
		
		gridPanel = new AdvanvcedKVExtractionGridView();
	}

	/**
	 * Gets Upload Batch Button Panel View.
	 * 
	 * @return {@link AdvancedKVExtractionButtonPanelView}
	 */
	public AdvancedKVExtractionButtonPanelView getButtonPanel() {
		return menuPanel;
	}

	public AdvancedKVExtractionInputPanelView getInputPanel() {
		return formPanel;
	}

	public AdvancedKVExtractionImportView getImportPanel() {
		return dropFilePanel;
	}

	public AdvancedKVExtractionImageView getImageViewPanel() {
		return imagePanel;
	}

	
	public AdvanvcedKVExtractionGridView getGridPanel() {
		return gridPanel;
	}

	/**
	 * Sets Upload Batch Button Panel View.
	 * 
	 * @param {@link AdvancedKVExtractionButtonPanelView}
	 */
/*	public void setInputPanel(
			final AdvancedKVExtractionButtonPanelView inputPanel) {
		this.menuPanel = inputPanel;
	}
*/
	@Override
	public AdvancedKVExtractionInlineView<?> getOptionsPanelView() {
		// TODO Auto-generated method stub
		return menuPanel;
	}

	@Override
	public AdvancedKVExtractionInlineView<?> getBottomPanelView() {
		// TODO Auto-generated method stub
		return gridPanel;
	}

	@Override
	public AdvancedKVExtractionInlineView<?> getListPanelView() {
		// TODO Auto-generated method stub
		return imagePanel;
	}

	@Override
	public AdvancedKVExtractionInlineView<?> getInputPanelView() {
		// TODO Auto-generated method stub
		return formPanel;
	}

	@Override
	public AdvancedKVExtractionInlineView<?> getDropFilePanelView() {
		// TODO Auto-generated method stub
		return dropFilePanel;
	}

}
