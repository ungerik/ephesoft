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

package com.ephesoft.gxt.core.client.ui.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.validator.Validator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.DetailsDTO;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent;
import com.sencha.gxt.widget.core.client.event.BeforeStartEditEvent.BeforeStartEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.form.IsField;
import com.sencha.gxt.widget.core.client.grid.CellSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GridViewConfig;
import com.sencha.gxt.widget.core.client.grid.editing.GridInlineEditing;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.CellSelectionChangedEvent.CellSelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

@SuppressWarnings("unchecked")
public class DetailGrid extends com.sencha.gxt.widget.core.client.grid.Grid<DetailsDTO> {

	private DetailsDTO detailDTO;

	private GridInlineEditing<DetailsDTO> editingGrid;

	private CellSelectionModel<DetailsDTO> cellSelectionModel;

	private DetailsDTO currentModel;

	private Map<ValueProvider<? super DetailsDTO, ?>, List<Validator<DetailsDTO>>> validatorsMap;

	public static final DetailDTOTitleProvider TITLE_PROVIDER = new DetailDTOTitleProvider();
	public static final DetailDTOValueProvider DETAIL_VALUE_PROVIDER = new DetailDTOValueProvider();

	public DetailGrid() {
		this(false);
	}

	public void setHeaders(String key, String value) {
		int columnCount = cm.getColumnCount();
		if (columnCount >= 2) {
			cm.getColumn(0).setHeader(key);
			cm.getColumn(1).setHeader(value);
		}
	}

	public DetailGrid(boolean editable) {
		super(CollectionUtil.createListStore(new DetailDTOKeyProvider()), DetailGrid.getDetailGridColumnModel(),
				new GridView<DetailsDTO>());
		this.view.setStripeRows(true);
		this.view.setColumnLines(true);
		validatorsMap = new HashMap<ValueProvider<? super DetailsDTO, ?>, List<Validator<DetailsDTO>>>();
		setRowCSS();

		this.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				final NativeEvent natev = event.getNativeEvent();
				if (Element.is(natev.getEventTarget())) {
					final int hoverCellIdx = getView().findCellIndex(Element.as(natev.getEventTarget()), null);
					final int hoverRowIdx = getView().findRowIndex(Element.as(natev.getEventTarget()));
					final DetailsDTO model = getStore().get(hoverRowIdx);
					if (null != model) {
						final String errorList = model.getValue();
						final Element cellElement = getView().getCell(hoverRowIdx, hoverCellIdx);
						if (cellElement.getInnerText().equals(errorList)) {
							setToolTip(cellElement, errorList);
						} else {
							hideToolTip();
						}
					}
				}
			}
		}, MouseMoveEvent.getType());
		cellSelectionModel = new CellSelectionModel<DetailsDTO>();
		this.setSelectionModel(cellSelectionModel);
		this.addSelectionChangeHandler();
		if (editable) {
			setEditing();
			addEditingHandlers();
		}
		setViewConfig();
	}

	private void setEditing() {
		editingGrid = new GridInlineEditing<DetailsDTO>(this) {

			@SuppressWarnings("rawtypes")
			@Override
			protected <N, O> void doStartEditing(final com.sencha.gxt.widget.core.client.grid.Grid.GridCell cell) {
				super.doStartEditing(cell);
				final ColumnModel<DetailsDTO> columnModel = cm;
				setEditorDimension(cell, columnModel);
			}

			private void setEditorDimension(final com.sencha.gxt.widget.core.client.grid.Grid.GridCell cell,
					final ColumnModel<DetailsDTO> columnModel) {
				if (null != columnModel) {
					final ColumnConfig<DetailsDTO, ?> columnConfig = columnModel.getColumn(cell.getCol());
					if (null != columnConfig) {
						final IsField editor = getEditor(columnConfig);
						final Element cellElement = getView().getCell(cell.getRow(), cell.getCol());
						if (editor instanceof Component) {
							final Component field = (Component) editor;
							final int client_y = cellElement.getOffsetTop();
							final int client_x = cellElement.getOffsetLeft();
							final int margin = Math.abs(cellElement.getAbsoluteLeft() - client_x) / 2;
							final int width = cellElement.getAbsoluteRight() - client_x - margin;
							field.setWidth(250);
							field.setPosition(client_x, client_y);
						}
					}
				}
			}

		};
	}

	private void addEditingHandlers() {
		editingGrid.addCompleteEditHandler(new CompleteEditHandler<DetailsDTO>() {

			@Override
			public void onCompleteEdit(CompleteEditEvent<DetailsDTO> event) {
				DetailGrid.this.onCompleteEdit(event);
			}
		});

		editingGrid.addBeforeStartEditHandler(new BeforeStartEditHandler<DetailsDTO>() {

			@Override
			public void onBeforeStartEdit(BeforeStartEditEvent<DetailsDTO> event) {
				DetailGrid.this.onBeforeeEdit(event);
			}
		});
	}

	private void setViewConfig() {
		this.getView().setViewConfig(new GridViewConfig<DetailsDTO>() {

			@Override
			public String getColStyle(DetailsDTO model, ValueProvider<? super DetailsDTO, ?> valueProvider, int rowIndex, int colIndex) {
				String cssStyle = CoreCommonConstant.EMPTY_STRING;
				if (getErrors(valueProvider, model) != null) {
					cssStyle = "styleName";
				} else {
				}
				return cssStyle;
			}

			@Override
			public String getRowStyle(DetailsDTO model, int rowIndex) {
				return null;
			}
		});
	}

	private void addSelectionChangeHandler() {
		cellSelectionModel.addCellSelectionChangedHandler(new CellSelectionChangedHandler<DetailsDTO>() {

			@Override
			public void onCellSelectionChanged(CellSelectionChangedEvent<DetailsDTO> event) {
				int row = com.ephesoft.gxt.core.client.util.EventUtil.getSelectedGridRow(event);
				if (row > 0) {
					currentModel = store.get(row);
				}
			}
		});
	}

	protected void onCompleteEdit(CompleteEditEvent<DetailsDTO> event) {

	}

	protected void onBeforeeEdit(BeforeStartEditEvent<DetailsDTO> event) {

	}

	public void addValidators(ValueProvider<DetailsDTO, ?> key, Validator<DetailsDTO> value) {
		if (validatorsMap.containsKey(key)) {
			validatorsMap.get(key).add(value);
		} else {
			List<Validator<DetailsDTO>> validators = new ArrayList<Validator<DetailsDTO>>();
			validators.add(value);
			validatorsMap.put(key, validators);
		}
	}

	public boolean isValid() {
		int totalModels = store.size();
		int totalColumn = cm.getColumnCount();
		boolean isGridValidated = true;
		gridValidationLoop: for (int currentModel = 0; currentModel < totalModels; currentModel++) {
			for (int currrentColumn = 0; currrentColumn < totalColumn; currrentColumn++) {
				ValueProvider<? super DetailsDTO, Object> currentValueProvider = cm.getValueProvider(currrentColumn);
				isGridValidated = getErrors(currentValueProvider, store.get(currentModel)) == null;
				if (!isGridValidated) {
					break gridValidationLoop;
				}
			}
		}
		return isGridValidated;
	}

	/**
	 * @return the currentModel
	 */
	public DetailsDTO getCurrentModel() {
		return currentModel;
	}

	public <T> void addEditor(ValueProvider<DetailsDTO, T> valueProvider, IsField<T> fieldEditor) {
		if (null != editingGrid && null != fieldEditor) {
			int columnCount = cm.getColumnCount();
			int index = -1;
			for (int i = 0; i < columnCount; i++) {
				if (cm.getValueProvider(i) == valueProvider) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				ColumnConfig<DetailsDTO, T> columnConfig = cm.getColumn(index);
				editingGrid.addEditor(columnConfig, fieldEditor);
			}
		}
	}

	private void setRowCSS() {
		this.getView().setViewConfig(new GridViewConfig<DetailsDTO>() {

			@Override
			public String getRowStyle(DetailsDTO model, int rowIndex) {
				return rowIndex % 2 == 1 ? "evenRow" : CoreCommonConstant.EMPTY_STRING;
			}

			@Override
			public String getColStyle(DetailsDTO model, ValueProvider<? super DetailsDTO, ?> valueProvider, int rowIndex, int colIndex) {
				return null;
			}
		});
	}

	private void setToolTip(final Element cellElement, final String errorList) {
		ToolTip toolTip = this.getToolTip();
		if (null != cellElement && !StringUtil.isNullOrEmpty(errorList)) {

			if (null == toolTip) {
				final ToolTipConfig config = new ToolTipConfig();
				this.setToolTipConfig(config);
				toolTip = this.getToolTip();
			} else {
				final ToolTipConfig config = toolTip.getToolTipConfig();
				final String htmlText = errorList;
				config.setBodyHtml(htmlText);
				toolTip.update(config);
				toolTip.showAt(cellElement.getAbsoluteLeft() + 5, cellElement.getAbsoluteTop() + 40);
			}
			toolTip.addStyleName("word_wrap_css");
		} else {
			if (null != toolTip) {
				toolTip.hide();
			}
		}
	}

	public void setData(List<DetailsDTO> dataToAdd) {
		this.getStore().clear();
		this.getStore().addAll(dataToAdd);
	}

	private static ColumnModel<DetailsDTO> getDetailGridColumnModel() {
		ColumnConfig<DetailsDTO, String> titleColumn = new ColumnConfig<DetailsDTO, String>(TITLE_PROVIDER);
		ColumnConfig<DetailsDTO, String> valueColumn = new ColumnConfig<DetailsDTO, String>(DETAIL_VALUE_PROVIDER);
		titleColumn.setMenuDisabled(true);
		valueColumn.setMenuDisabled(true);
		List<ColumnConfig<DetailsDTO, ?>> columnConfigList = new ArrayList<ColumnConfig<DetailsDTO, ?>>(2);
		titleColumn.setFixed(true);
		titleColumn.setWidth(100);
		valueColumn.setWidth(250);
		columnConfigList.add(titleColumn);
		columnConfigList.add(valueColumn);
		ColumnModel<DetailsDTO> columnModel = new ColumnModel<DetailsDTO>(columnConfigList);
		return columnModel;
	}

	/**
	 * @param detailDTO the detailDTO to set
	 */
	public void setDetailDTO(DetailsDTO detailDTO) {
		this.detailDTO = detailDTO;
	}

	/**
	 * @return the detailDTO
	 */
	public DetailsDTO getDetailDTO() {
		return detailDTO;
	}

	public void refreshGrid() {
		getView().refresh(false);
	}

	private ListWidget getErrors(ValueProvider<? super DetailsDTO, ?> valueProvider, DetailsDTO model) {
		ListWidget htmlErrorList = null;
		if (null != validatorsMap && !validatorsMap.isEmpty()) {
			if (validatorsMap.containsKey(valueProvider)) {
				List<Validator<DetailsDTO>> listOfValidators = validatorsMap.get(valueProvider);
				if (null != listOfValidators && !listOfValidators.isEmpty() && null != store) {
					for (Validator<DetailsDTO> validator : listOfValidators) {
						if (!validator.validate(store.getAll(), model, (ValueProvider<DetailsDTO, ?>) valueProvider)) {
							if (htmlErrorList == null) {
								htmlErrorList = new ListWidget();
							}
							htmlErrorList.addItem(validator.getErrorMessage(), validator.getSeverity().name());
						}
					}
				}
			}
		}
		return htmlErrorList;
	}

	private static class DetailDTOKeyProvider implements ModelKeyProvider<DetailsDTO> {

		@Override
		public String getKey(DetailsDTO item) {
			return String.valueOf(item.getId());
		}
	}

	private static class DetailDTOValueProvider implements ValueProvider<DetailsDTO, String> {

		@Override
		public String getPath() {
			return "value";
		}

		@Override
		public String getValue(DetailsDTO object) {
			return object.getValue();
		}

		@Override
		public void setValue(DetailsDTO object, String value) {
			object.setValue(value);
		}
	}

	private static class DetailDTOTitleProvider implements ValueProvider<DetailsDTO, String> {

		@Override
		public String getPath() {
			return "title";
		}

		@Override
		public String getValue(DetailsDTO object) {
			return object.getTitle();
		}

		@Override
		public void setValue(DetailsDTO object, String value) {
			// TODO Auto-generated method stub

		}
	}
}
