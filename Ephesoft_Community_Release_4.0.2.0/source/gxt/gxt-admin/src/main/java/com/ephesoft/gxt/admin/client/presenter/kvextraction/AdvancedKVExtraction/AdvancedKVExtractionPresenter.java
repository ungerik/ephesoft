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

package com.ephesoft.gxt.admin.client.presenter.kvextraction.AdvancedKVExtraction;

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AdvancedKVExtractionHideEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedKVExtractionShowEvent;
import com.ephesoft.gxt.admin.client.event.ReloadKVExtractionGridEvent;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionView;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.dto.AdvancedKVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.AdvancedKVExtractionDetailDTO;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.KVExtractionDTO;
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
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.Window.WindowAppearance;
import com.sencha.gxt.widget.core.client.info.Info;

public class AdvancedKVExtractionPresenter extends AdvancedKVExtractionCompositePresenter<AdvancedKVExtractionView, BatchClassDTO> {

	AdvancedKVExtractionButtonPanelPresenter advKVButtonPanelPresenter;
	AdvancedKVExtractionImportPresenter advKVImportPresenter;
	AdvancedKVExtractionInputPanelPresenter advKVInputPanelPresenter;
	AdvancedKVExtractionImageViewPresenter advKvImageViewPresenter;
	AdvancedKVExtractionGridPresenter advKvGridPresenter;
	AdvancedKVExtractionDTO advancedKVExtractionDTO;
	Window window;
	private boolean isEdit;

	interface CustomEventBinder extends EventBinder<AdvancedKVExtractionPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	public enum Results {
		SUCCESSFUL, FAILURE, PARTIAL_SUCCESS;
	}

	/**
	 * Constructor.
	 * 
	 * @param controller {@link BatchClassManagementController}
	 * @param view {@link UploadBatchView}
	 */
	public AdvancedKVExtractionPresenter(final BatchClassManagementController controller, final AdvancedKVExtractionView view) {
		super(controller, view);
		if (view != null) {
			advKVButtonPanelPresenter = new AdvancedKVExtractionButtonPanelPresenter(controller, view.getButtonPanel());
			advKVImportPresenter = new AdvancedKVExtractionImportPresenter(controller, view.getImportPanel());
			advKvImageViewPresenter = new AdvancedKVExtractionImageViewPresenter(controller, view.getImageViewPanel());
			advKVInputPanelPresenter = new AdvancedKVExtractionInputPanelPresenter(controller, view.getInputPanel());
			advKvGridPresenter = new AdvancedKVExtractionGridPresenter(controller, view.getGridPanel());
		}
	}

	@Override
	public void bind() {
		KVExtractionDTO selectedKVExtraction = controller.getSelectedKVExtraction();
		this.advancedKVExtractionDTO = new AdvancedKVExtractionDTO();
		if (selectedKVExtraction != null) {
			AdvancedKVExtractionDTO advancedKVExtractionDTO = selectedKVExtraction.getAdvancedKVExtractionDTO();
			if (advancedKVExtractionDTO != null) {
				mergeAdvancedKVExtractionDTO(this.advancedKVExtractionDTO, advancedKVExtractionDTO);
			} else {
				this.advancedKVExtractionDTO.setNew(true);
			}
		}
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);
	}

	@Override
	public void init(BatchClassDTO selectionParameter) {

	}

	@Override
	public AdvancedKVExtractionCompositePresenter<?, ?> getParentPresenter() {
		return null;
	}

	public AdvancedKVExtractionButtonPanelPresenter getAdvKVButtonPanelPresenter() {
		return advKVButtonPanelPresenter;
	}

	public AdvancedKVExtractionImportPresenter getAdvKVImportPresenter() {
		return advKVImportPresenter;
	}

	public AdvancedKVExtractionInputPanelPresenter getAdvKVInputPanelPresenter() {
		return advKVInputPanelPresenter;
	}

	public AdvancedKVExtractionImageViewPresenter getAdvKvImageViewPresenter() {
		return advKvImageViewPresenter;
	}

	public AdvancedKVExtractionGridPresenter getAdvKvGridPresenter() {
		return advKvGridPresenter;
	}

	public void mergeAdvancedKVExtractionDTO(AdvancedKVExtractionDTO selAdvKVExtractionDTO, AdvancedKVExtractionDTO advKVExtractionDTO) {

		selAdvKVExtractionDTO.setSelectedImageDisplayName(advKVExtractionDTO.getSelectedImageDisplayName());
		selAdvKVExtractionDTO.setSelectedImageName(advKVExtractionDTO.getSelectedImageName());
		selAdvKVExtractionDTO.setKeyX0Coord(advKVExtractionDTO.getKeyX0Coord());
		selAdvKVExtractionDTO.setKeyX1Coord(advKVExtractionDTO.getKeyX1Coord());
		selAdvKVExtractionDTO.setKeyY0Coord(advKVExtractionDTO.getKeyY0Coord());
		selAdvKVExtractionDTO.setKeyY1Coord(advKVExtractionDTO.getKeyY1Coord());
		selAdvKVExtractionDTO.setValueX0Coord(advKVExtractionDTO.getValueX0Coord());
		selAdvKVExtractionDTO.setValueX1Coord(advKVExtractionDTO.getValueX1Coord());
		selAdvKVExtractionDTO.setValueY0Coord(advKVExtractionDTO.getValueY0Coord());
		selAdvKVExtractionDTO.setValueY1Coord(advKVExtractionDTO.getValueY1Coord());
		if (advKVExtractionDTO.getAdvKVExtractionDetailDTO() != null) {
			for (AdvancedKVExtractionDetailDTO detailDTO : advKVExtractionDTO.getAdvKVExtractionDetailDTO()) {
				AdvancedKVExtractionDetailDTO dto = new AdvancedKVExtractionDetailDTO();
				dto.setIdentifier(detailDTO.getIdentifier());
				dto.setFileName(detailDTO.getFileName());
				dto.setMultiPage(detailDTO.isMultiPage());
				dto.setPageCount(detailDTO.getPageCount());
				dto.setNew(detailDTO.isNew());
				dto.setDeleted(detailDTO.isDeleted());
				dto.setAdvancedKVExtraction(selAdvKVExtractionDTO);
				selAdvKVExtractionDTO.addAdvKVDetail(dto);
			}
		}
	}

	public AdvancedKVExtractionDTO getAdvancedKVExtractionDTO() {
		return advancedKVExtractionDTO;
	}

	/**
	 * 
	 * @param event
	 */
	@EventHandler
	public void openAdvKVScreen(final AdvancedKVExtractionShowEvent event) {
		setEdit(event.isEdit());
		window = new Window((WindowAppearance) GWT.create(GrayWindowAppearance.class));
		window.setPagePosition(0, 0);
		window.setWidth(com.google.gwt.user.client.Window.getClientWidth());
		window.setHeight(com.google.gwt.user.client.Window.getClientHeight());
		window.setClosable(false);
		window.setHeaderVisible(false);
		window.setDraggable(false);
		window.setResizable(false);
		window.getElement().getStyle().setZIndex(5);
		window.setBorders(false);
		window.removeStyleName("noheader");
		window.addStyleName("advWindow");
		window.add(controller.createAdvKVView());
		if (!isEdit) {
			ContentPanel panel = controller.getAdvancedKVLayout().getBottomsPanel();
			if (panel != null) {
				panel.setExpanded(true);
			}
		}
		window.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.isControlKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_K) {
					event.preventDefault();
					event.stopPropagation();
				}
			}
		}, KeyDownEvent.getType());
		WidgetUtil.setID(window, "advKVScreen");
		Timer timer = new Timer() {

			@Override
			public void run() {
				RootPanel.get().add(window);
				window.forceLayout();
			}
		};
		timer.schedule(20);
		window.focus();

		addresize();
	}

	@EventHandler
	public void hideAdvKVScreen(final AdvancedKVExtractionHideEvent event) {
		window.removeFromParent();
		BatchClassManagementEventBus.fireEvent(new ReloadKVExtractionGridEvent());
	}

	public boolean isEdit() {
		return isEdit;
	}

	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
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
