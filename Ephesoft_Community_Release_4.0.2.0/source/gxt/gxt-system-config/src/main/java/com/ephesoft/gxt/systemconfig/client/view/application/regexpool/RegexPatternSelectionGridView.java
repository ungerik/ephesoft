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

package com.ephesoft.gxt.systemconfig.client.view.application.regexpool;

import java.util.Collection;
import java.util.List;

import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.client.ui.widget.HasResizableGrid;
import com.ephesoft.gxt.core.client.validator.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.RegexPatternValidator;
import com.ephesoft.gxt.core.client.validator.UniqueValueValidator;
import com.ephesoft.gxt.core.shared.dto.RegexPatternDTO;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.RegexGroupProperties;
import com.ephesoft.gxt.core.shared.dto.propertyAccessors.RegexPatternProperties;
import com.ephesoft.gxt.systemconfig.client.presenter.application.regexpool.RegexPatternSelectionGridPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.ephesoft.gxt.systemconfig.client.widget.SystemConfigGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.GridSelectionModel;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;

/**
 * RegexPatternSelectionGridView is the grid that shows the list of regex patterns
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 18-Dec-2014 <br/>
 * @version 1.0 $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RegexPatternSelectionGridView extends SystemConfigInlineView<RegexPatternSelectionGridPresenter> implements
		HasResizableGrid {

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
	interface Binder extends UiBinder<Widget, RegexPatternSelectionGridView> {
	}

	/**
	 * BINDER {@link Binder}.
	 */
	private static final Binder binder = GWT.create(Binder.class);

	/**
	 * regexPatternGrid {@link SystemConfigGrid<RegexPatternDTO>} is the instance of system config grid
	 */

	protected SystemConfigGrid<RegexPatternDTO> regexPatternGrid;

	/**
	 * gridViewMainPanel {@link VerticalPanel } is the instance of VerticalPanel
	 */

	@UiField
	protected VerticalPanel gridViewMainPanel;

	/**
	 * regexGroupContainer {@link ContentPanel } is the instance of ContentPanel
	 */

	@UiField
	protected ContentPanel regexPatternContainer;

	/**
	 * Instantiates the RegexPatternSelectionGridView.
	 */
	public RegexPatternSelectionGridView() {
		super();
		initWidget(binder.createAndBindUi(this));
		gridViewMainPanel.addStyleName("gridViewMainPanel");
		regexPatternGrid.addStyleName("gridView");
		regexPatternGrid.addStyleName("regexGrid");
		regexPatternGrid.getStore().setAutoCommit(true);
		regexPatternGrid.setFirstRowSelectedOnLoad(true);
		regexPatternGrid.setHasPagination(false);
		regexPatternContainer.clear();
		regexPatternContainer.add(regexPatternGrid);
		regexPatternContainer.setHeaderVisible(false);
		regexPatternContainer.setHeight("50%");
		regexPatternContainer.setBorders(false);

	}

	/**
	 * intialize method used to intialise the Regex Pattern Grid
	 */
	@Override
	public void initialize() {
		regexPatternGrid = new SystemConfigGrid<RegexPatternDTO>(PropertyAccessModel.REGEX_PATTERN) {

			@Override
			public void completeEditing(CompleteEditEvent<RegexPatternDTO> completeEditEvent) {
				super.completeEditing(completeEditEvent);
				regexPatternGrid.getStore().commitChanges();
				com.sencha.gxt.widget.core.client.grid.Grid.GridCell editCell = completeEditEvent.getEditCell();
				int index = editCell.getRow();
				RegexPatternDTO editedRegexPattern = regexPatternGrid.getModel(index);
				int column = editCell.getCol();
				ValueProvider<?, ?> valueProvider = cm.getValueProvider(column);
				if (valueProvider == RegexPatternProperties.INSTANCE.pattern()) {
					presenter.validateAndSave(editedRegexPattern);
				} else {
					if (regexPatternGrid.isValid(editedRegexPattern)) {
						presenter.updateRegexPattern(editedRegexPattern);
					}
					regexPatternGrid.refreshGrid(true);
					presenter.checkNewPatternExist(regexPatternGrid.getStore().getAll());
				}
			}
		};

		regexPatternGrid.addSelectAllFunctionality(RegexPatternProperties.INSTANCE.selected());
		this.addRegexPatternSelectionHandler(regexPatternGrid.getSelectionModel());
		regexPatternGrid.addDirtyCellValueProvider(RegexPatternProperties.INSTANCE.pattern());
		regexPatternGrid.addValidators(RegexPatternProperties.INSTANCE.pattern(), new RegexPatternValidator<RegexPatternDTO>());
		regexPatternGrid.addValidators(RegexPatternProperties.INSTANCE.pattern(), new UniqueValueValidator<RegexPatternDTO, String>());
		regexPatternGrid.addValidators(RegexPatternProperties.INSTANCE.pattern(), new EmptyValueValidator<RegexPatternDTO>());
		regexPatternGrid.addValidators(RegexPatternProperties.INSTANCE.description(), new EmptyValueValidator<RegexPatternDTO>());
	}

	public void removeDirtyRegexPattern(final RegexPatternDTO regexPatternDTO) {
		regexPatternGrid.removeDirtyCell(RegexPatternProperties.INSTANCE.pattern(), regexPatternDTO);
	}

	public boolean isValid(RegexPatternDTO patternDTO) {
		return regexPatternGrid.isValid(patternDTO);
	}

	/**
	 * addRegexGroupSelectionHandler method is the handler, which is activated when some regex pattern is selected by the user
	 * 
	 * @param selectionModel {@link GridSelectionModel<RegexPatternDTO> }
	 */
	public void addRegexPatternSelectionHandler(GridSelectionModel<RegexPatternDTO> selectionModel) {
		if (selectionModel instanceof CellSelectionModel) {
			CellSelectionModel<RegexPatternDTO> cellSelectionModel = (CellSelectionModel<RegexPatternDTO>) selectionModel;
			cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<RegexPatternDTO>() {

				@Override
				public void onCellSelectionChanged(CellSelectionChangedEvent<RegexPatternDTO> cellSelectionEvent) {
					presenter.selectRegexPattern(cellSelectionEvent);
				}
			});
		}
	}

	/**
	 * Getter of regexPattern grid
	 * 
	 * @return {@link SystemConfigGrid<RegexPatternpDTO>} the instance of regex Pattern grid
	 */
	@Override
	public Grid getGrid() {
		return regexPatternGrid;
	}

	/**
	 * add new model of RegexPatternDTO type into the Grid
	 * 
	 * @param regexPattern {@link RegexPatternDTO}
	 */
	public void add(RegexPatternDTO regexPattern) {
		if (null != regexPattern) {
			regexPatternGrid.addNewItemInGrid(regexPattern);
		}

	}

	/**
	 * method to check whether Grid Data is valid or not
	 * 
	 * @return {@link boolean} whether grid is valid or not, after applying the validators
	 */
	public boolean isValid() {
		return regexPatternGrid.isGridValidated();
	}

	/**
	 * setData method use for setting the list of regex pattern data into the store of the regex pattern grid
	 * 
	 * @param collection {@link Collection<RegexPatternDTO>} is the list of regex pattern available
	 */
	public void setData(final Collection<RegexPatternDTO> collection) {
		regexPatternGrid.setMemoryData(collection);
	}

	/**
	 * getSelectedRegexPatterns method used to return the list of Regex Patterns
	 * 
	 * @return {@link List<RegexPatternDTO>} list of Regex Patterns contained in the grid
	 */
	public List<RegexPatternDTO> getSelectedRegexPatterns() {
		return regexPatternGrid.getSelectedModels();
	}

	public void refreshGrid() {
		regexPatternGrid.refreshGrid(true);
	}

	public List<RegexPatternDTO> getAllRegexPattern() {
		return regexPatternGrid.getStore().getAll();
	}
}
