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

package com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction.advancedtablecolumnextractionrule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionChangeEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionClearCoordinateEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionHideEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionImageOverlayEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionInputPanelEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionSaveEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionShowEvent;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.layout.AdvancedKVExtractionLayout;
import com.ephesoft.gxt.admin.client.view.tablecolumnextraction.advcancedtablecolumnextractionrule.AdvancedTableColumnExtractionView;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.theme.gray.client.window.GrayWindowAppearance;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.Window.WindowAppearance;

public class AdvancedTableColumnExtractionPresenter extends
		AdvancedTableColumnExtractionCompositePresenter<AdvancedTableColumnExtractionView, TableInfoDTO> {

	Window window;
	protected AdvancedTableColumnExtractionMenuPresenter menuPresenter;
	protected AdvancedTableColumnExtractionImagePresenter imagePresenter;
	protected AdvancedTableColumnExtractionImportPresenter importPresenter;
	protected AdvancedTableColumnExtractionInputPresenter inputPresenter;
	protected TableColumnExtractionRuleDTO columnExtractionRuleDTO;

	private Map<String, TableColumnExtractionRuleDTO> columnNameToDTOMap = new HashMap<String, TableColumnExtractionRuleDTO>();

	public AdvancedTableColumnExtractionPresenter(BatchClassManagementController controller, AdvancedTableColumnExtractionView view) {
		super(controller, view);
		menuPresenter = new AdvancedTableColumnExtractionMenuPresenter(controller, view.getMenuView());
		imagePresenter = new AdvancedTableColumnExtractionImagePresenter(controller, view.getImageView());
		importPresenter = new AdvancedTableColumnExtractionImportPresenter(controller, view.getImportView());
		inputPresenter = new AdvancedTableColumnExtractionInputPresenter(controller, view.getInputView());

		setLayout();
	}

	private void setLayout() {
		AdvancedKVExtractionLayout layout = controller.getAdvTableColumnExtractionLayout();
		layout.setOptionsPanelView(view.getMenuPanelView());
		layout.setGridView(view.getImageView());
		layout.setBottomPanelView(view.getBottomPanelView());
		layout.setInputPanelView(view.getInputPanelView());
	}

	interface CustomEventBinder extends EventBinder<AdvancedTableColumnExtractionPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public enum Results {
		SUCCESSFUL, FAILURE, PARTIAL_SUCCESS;
	}

	@Override
	public void bind() {
		columnNameToDTOMap = new HashMap<String, TableColumnExtractionRuleDTO>();
		columnExtractionRuleDTO = controller.getSelectedTableColumnExtractionRuleDTO();
	}

	@Override
	public void init(TableInfoDTO selectionParameter) {
	}

	@Override
	public AdvancedTableColumnExtractionCompositePresenter<?, ?> getParentPresenter() {
		return null;
	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@EventHandler
	public void openAdvancedTableColumnExtractionScreen(AdvancedTableColumnExtractionShowEvent event) {

		window = new Window((WindowAppearance) GWT.create(GrayWindowAppearance.class));
		window.setPagePosition(0, 0);
		window.setWidth(com.google.gwt.user.client.Window.getClientWidth());
		window.setHeight(com.google.gwt.user.client.Window.getClientHeight());
		window.setClosable(false);
		window.setHeaderVisible(false);
		window.setDraggable(false);
		window.setResizable(false);
		window.add(controller.createAdvTableColumnExtractionView());
		window.getElement().getStyle().setZIndex(5);
		window.setBorders(false);
		window.addStyleName("advWindow");
		//Ctrl + K functionality on Set Coordinates Screen.
		window.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.isControlKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_K) {
					event.preventDefault();
					event.stopPropagation();
				}
			}
		}, KeyDownEvent.getType());
		
		WidgetUtil.setID(window, "advTableColumnExtractionView");
		
		Timer timer = new Timer() {

			@Override
			public void run() {
				RootPanel.get().add(window);
				window.forceLayout();
			}
		};
		timer.schedule(20);
		
//		RootPanel.get().add(window);
		window.focus();
		addresize();
	}

	@EventHandler
	public void hideAdvancedTableColumnExtractionScreen(final AdvancedTableColumnExtractionHideEvent event) {
		if (event.isWindowHide()) {
			window.removeFromParent();
		}
	}

	@EventHandler
	public void clearCoordinates(AdvancedTableColumnExtractionClearCoordinateEvent event) {
		// Remove Overlays
		BatchClassManagementEventBus.fireEvent(new AdvancedTableColumnExtractionHideEvent(false, true));
		if (event.isClearSelectedColumn()) {
			clearCoordinatesForSelectedColumn();
		} else {
			clearCoordinatesForAllColumns();
		}
		
	}

	public void clearCoordinatesForSelectedColumn() {
		TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO = controller.getSelectedTableColumnExtractionRuleDTO();
		if (this.columnNameToDTOMap != null && tableColumnExtractionRuleDTO != null) {
			TableColumnExtractionRuleDTO localTableColumnExtractionRuleDTO = this.columnNameToDTOMap.get(tableColumnExtractionRuleDTO
					.getColumnName());
			clear(localTableColumnExtractionRuleDTO);
		}
	}

	public void clearCoordinatesForAllColumns() {
		if (this.columnNameToDTOMap != null) {
			Set<String> columnNames = this.columnNameToDTOMap.keySet();
			if (columnNames != null && !columnNames.isEmpty()) {
				for (String columnName : columnNames) {
					TableColumnExtractionRuleDTO localTableColumnExtractionRuleDTO = this.columnNameToDTOMap.get(columnName);
					clear(localTableColumnExtractionRuleDTO);
				}
			}
		}
	}

	private void clear(TableColumnExtractionRuleDTO localTableColumnExtractionRuleDTO) {
		if (localTableColumnExtractionRuleDTO != null) {
			localTableColumnExtractionRuleDTO.setColumnStartCoordinate(AdminConstants.EMPTY_STRING);
			localTableColumnExtractionRuleDTO.setColumnEndCoordinate(AdminConstants.EMPTY_STRING);
			localTableColumnExtractionRuleDTO.setColumnCoordY0(AdminConstants.EMPTY_STRING);
			localTableColumnExtractionRuleDTO.setColumnCoordY1(AdminConstants.EMPTY_STRING);
			BatchClassManagementEventBus.fireEvent(new AdvancedTableColumnExtractionInputPanelEvent(null, null,
					AdminConstants.EMPTY_STRING, AdminConstants.EMPTY_STRING));
		}
	}

	@EventHandler
	public void setColumnCoordAndCreateOverlay(AdvancedTableColumnExtractionChangeEvent event) {
		this.columnExtractionRuleDTO = controller.getSelectedTableColumnExtractionRuleDTO();
		TableColumnExtractionRuleDTO localTableColumnExtractionRuleDTO = this.columnNameToDTOMap.get(columnExtractionRuleDTO
				.getColumnName());
		// Clear Overlays
		BatchClassManagementEventBus.fireEvent(new AdvancedTableColumnExtractionHideEvent(false, true));
		if (localTableColumnExtractionRuleDTO != null) {
			String startCoord = localTableColumnExtractionRuleDTO.getColumnStartCoordinate();
			String endCoord = localTableColumnExtractionRuleDTO.getColumnEndCoordinate();
			String colCoordY0 = localTableColumnExtractionRuleDTO.getColumnCoordY0();
			String colCoordY1 = localTableColumnExtractionRuleDTO.getColumnCoordY1();
			if (startCoord == null) {
				startCoord = AdminConstants.EMPTY_STRING;
			}
			if (endCoord == null) {
				endCoord = AdminConstants.EMPTY_STRING;
			}
			// Set Coordinate Value in Input Panel
			BatchClassManagementEventBus.fireEvent(new AdvancedTableColumnExtractionInputPanelEvent(null, null, startCoord, endCoord));
			if (startCoord != null && endCoord != null && colCoordY0 != null && !startCoord.isEmpty() && !endCoord.isEmpty()
					&& !colCoordY0.isEmpty() && !colCoordY1.isEmpty()) {
				// Create Overlay on Image
				BatchClassManagementEventBus.fireEvent(new AdvancedTableColumnExtractionImageOverlayEvent(
						Integer.parseInt(startCoord), Integer.parseInt(endCoord), Integer.parseInt(colCoordY0), Integer
								.parseInt(colCoordY1)));
			}
		}
	}

	public Map<String, TableColumnExtractionRuleDTO> getColumnNameToDTOMap() {
		return columnNameToDTOMap;
	}

	@EventHandler
	public void saveDataInDto(AdvancedTableColumnExtractionSaveEvent event) {

		final TableExtractionRuleDTO tableExtractionRuleDTO = columnExtractionRuleDTO.getTableExtractionRuleDTO();
		if (null != tableExtractionRuleDTO) {
			final TableInfoDTO tableInfoDTO = tableExtractionRuleDTO.getTableInfoDTO();
			if (tableInfoDTO != null) {
				final List<TableColumnExtractionRuleDTO> tableColumnExtractionRuleDTOs = tableExtractionRuleDTO
						.getTableColumnExtractionRuleDTOs(false);
				if (this.columnNameToDTOMap != null && !this.columnNameToDTOMap.isEmpty() && tableColumnExtractionRuleDTOs != null) {
					for (final TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO : tableColumnExtractionRuleDTOs) {
						if (tableColumnExtractionRuleDTO != null) {
							final String columnName = tableColumnExtractionRuleDTO.getColumnName();
							if (columnName != null && !columnName.isEmpty()) {
								final TableColumnExtractionRuleDTO localTableColumnDTO = this.columnNameToDTOMap.get(columnName);
								if (localTableColumnDTO != null) {
									mergeTableColumnsInfoDTOs(tableColumnExtractionRuleDTO, localTableColumnDTO);
								}
							}
						}
					}
				}
			}
		}
		BatchClassManagementEventBus.fireEvent(new AdvancedTableColumnExtractionHideEvent(true, true));
	}

	public void mergeTableColumnsInfoDTOs(final TableColumnExtractionRuleDTO tableColumnExtractionRuleDTO,
			final TableColumnExtractionRuleDTO localTableColumnDTO) {
		tableColumnExtractionRuleDTO.setBetweenLeft(localTableColumnDTO.getBetweenLeft());
		tableColumnExtractionRuleDTO.setBetweenRight(localTableColumnDTO.getBetweenRight());
		tableColumnExtractionRuleDTO.setColumnName(localTableColumnDTO.getColumnName());
		tableColumnExtractionRuleDTO.setColumnPattern(localTableColumnDTO.getColumnPattern());
		tableColumnExtractionRuleDTO.setExtractedDataColumnName(localTableColumnDTO.getExtractedDataColumnName());
		tableColumnExtractionRuleDTO.setRequired(localTableColumnDTO.isRequired());
		tableColumnExtractionRuleDTO.setMandatory(localTableColumnDTO.isMandatory());
		tableColumnExtractionRuleDTO.setColumnHeaderPattern(localTableColumnDTO.getColumnHeaderPattern());
		tableColumnExtractionRuleDTO.setColumnStartCoordinate(localTableColumnDTO.getColumnStartCoordinate());
		tableColumnExtractionRuleDTO.setColumnEndCoordinate(localTableColumnDTO.getColumnEndCoordinate());
		tableColumnExtractionRuleDTO.setColumnCoordY0(localTableColumnDTO.getColumnCoordY0());
		tableColumnExtractionRuleDTO.setColumnCoordY1(localTableColumnDTO.getColumnCoordY1());
	}

	private void addresize() {
		com.google.gwt.user.client.Window.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				if (null != window && window.isAttached()) {
					Timer timer = new Timer() {

						@Override
						public void run() {
							adjustView();
						}
					};
					timer.schedule(100);
				}
			}
		});
	}

	public void adjustView() {
		window.setWidth(com.google.gwt.user.client.Window.getClientWidth());
		window.setHeight(com.google.gwt.user.client.Window.getClientHeight());
		window.forceLayout();
	}
}
