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

package com.ephesoft.gxt.admin.client.view.regexPool;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.regexPool.RegexGroupSelectionGridPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.admin.client.widget.RegexComboBoxHandler;
import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.dto.RegexGroupDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.RegexGroupProperties;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent.RowDoubleClickHandler;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

/**
 * RegexGroupSelectionGridView is the grid that shows the list of regex groups
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RegexGroupSelectionGridView extends BatchClassInlineView<RegexGroupSelectionGridPresenter> implements HasResizableGrid {

	/**
	 * 
	 * Interface for the Binder.
	 * 
	 * @author Ephesoft
	 * 
	 *         <b>created on</b> 18-Dec-2014 <br/>
	 * @version $LastChangedDate:$ <br/>
	 *          $LastChangedRevision:$ <br/>
	 */

	interface Binder extends UiBinder<Widget, RegexGroupSelectionGridView> {
	}

	/**
	 * BINDER {@link Binder}.
	 */
	private static final Binder binder = GWT.create(Binder.class);

	/**
	 * regexGroupGrid {@link SystemConfigGrid<RegexGroupDTO>} is the instance of system config grid
	 */
	@UiField(provided = true)
	protected Grid<RegexGroupDTO> regexGroupGrid;

	protected RegexComboBoxHandler regexComboBoxHandler;

	private DialogWindow regexDialogWindow;

	public RegexComboBoxHandler getRegexComboBoxHandler() {
		return regexComboBoxHandler;
	}

	/**
	 * Instantiates the RegexGroupSelectionGridView.
	 */
	public RegexGroupSelectionGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		regexGroupGrid.addStyleName("setWidth");
		regexGroupGrid.addStyleName("gridView");
		regexGroupGrid.addStyleName("regexGrid");
		regexGroupGrid.getStore().setAutoCommit(true);
		regexGroupGrid.setHasPagination(false);
	}

	/**
	 * intialize method used to intialise the Regex Group Grid
	 */
	@Override
	public void initialize() {
		regexGroupGrid = new Grid<RegexGroupDTO>(PropertyAccessModel.REGEX_GROUP_BUTTON);

		regexGroupGrid.addRowDoubleClickHandler(new RowDoubleClickHandler() {

			public void onRowDoubleClick(RowDoubleClickEvent event) {
				int index = event.getRowIndex();
				RegexGroupDTO editedRegexGroup = regexGroupGrid.getModel(index);
				presenter.handleDoubleClickEvent(editedRegexGroup);

			}
		});
		regexGroupGrid.addSelectAllFunctionality(RegexGroupProperties.INSTANCE.selected());
		this.addRegexGroupSelectionHandler(regexGroupGrid.getSelectionModel());

		regexGroupGrid.addValidators(RegexGroupProperties.INSTANCE.name(), new EmptyValueValidator<RegexGroupDTO>());
		regexGroupGrid.addValidators(RegexGroupProperties.INSTANCE.name(), new UniqueValueValidator<RegexGroupDTO, String>());
	}

	/**
	 * addRegexGroupSelectionHandler method is the handler, which is activated when some regex group is selected by the user
	 * 
	 * @param selectionModel {@link GridSelectionModel<RegexGroupDTO> }
	 */
	public void addRegexGroupSelectionHandler(GridSelectionModel<RegexGroupDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<RegexGroupDTO> cellSelectionModel = (CellSelectionModel<RegexGroupDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<RegexGroupDTO>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<RegexGroupDTO> cellSelectionEvent) {
					presenter.selectRegexGroup(cellSelectionEvent);
				}
			});
		}
	}

	/**
	 * setData method use for setting the list of regex group data into the store of the regex group grid
	 * 
	 * @param collection {@link Collection<RegexGroupDTO>} is the list of regex groups available
	 */
	public void setData(final Collection<RegexGroupDTO> collection) {
		regexGroupGrid.setMemoryData(collection);
	}

	/**
	 * Getter of regexGroup grid
	 * 
	 * @return {@link SystemConfigGrid<RegexGroupDTO>} the instance of regex group grid
	 */
	@Override
	public Grid getGrid() {
		return regexGroupGrid;
	}

	/**
	 * method to check whether Grid Data is valid or not
	 * 
	 * @return {@link boolean} whether grid is valid or not, after applying the validators
	 */
	public boolean isValid() {
		return regexGroupGrid.isGridValidated();
	}

	/**
	 * getSelectedRegexGroups method used to return the list of Regex Groups
	 * 
	 * @return {@link List<RegexGroupDTO>} list of Regex Groups contained in the grid
	 */
	public List<RegexGroupDTO> getSelectedRegexGroups() {
		return regexGroupGrid.getSelectedModels();
	}

	/**
	 * add new model of RegexGroupDTO type into the Grid
	 * 
	 * @param regexGroup {@link RegexGroupDTO}
	 */
	public void add(RegexGroupDTO regexGroup) {
		if (null != regexGroup) {
			regexGroupGrid.addNewItemInGrid(regexGroup);
		}

	}

	public void setRegexComboBoxHandler(RegexComboBoxHandler regexComboBoxHandler) {
		this.regexComboBoxHandler = regexComboBoxHandler;
	}

	public void hideRegexDialogWindow(BatchClassInlineView groupView) {
		regexDialogWindow.remove(groupView);
		regexDialogWindow.hide();
	}

	public void showDialogWindow(BatchClassInlineView groupView) {
		regexDialogWindow = new DialogWindow(false);
		regexDialogWindow.addStyleName("regexDialogWindow");
		regexDialogWindow.setModal(true);
		regexDialogWindow.setOnEsc(false);
		regexDialogWindow.setResizable(true);
		regexDialogWindow.setDraggable(true);
		regexDialogWindow.add(groupView);
		regexDialogWindow.setHeadingText(LocaleDictionary.getConstantValue(BatchClassConstants.REGEX_POOL));
		regexDialogWindow.show();

	}
}
