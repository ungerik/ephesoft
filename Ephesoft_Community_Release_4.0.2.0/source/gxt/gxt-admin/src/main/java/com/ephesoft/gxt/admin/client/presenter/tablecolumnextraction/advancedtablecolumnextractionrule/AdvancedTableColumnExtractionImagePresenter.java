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

import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController.BatchClassManagementEventBus;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionHideEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionImageOverlayEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionInputPanelEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionLoadImageEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionMenuEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableColumnExtractionSaveEvent;
import com.ephesoft.gxt.admin.client.event.AdvancedTableExtractionFireColumnChangeEvent;
import com.ephesoft.gxt.admin.client.event.SetOverlayCoordinatesEvent;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.view.tablecolumnextraction.advcancedtablecolumnextractionrule.AdvancedTableColumnExtractionImageView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.OverlayImage;
import com.ephesoft.gxt.core.client.ui.widget.listener.OverlayDrawnListener;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.PointCoordinate;
import com.ephesoft.gxt.core.shared.dto.TableColumnExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableExtractionRuleDTO;
import com.ephesoft.gxt.core.shared.dto.TableInfoDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;

public class AdvancedTableColumnExtractionImagePresenter extends
		AdvancedTableColumnExtractionInlinePresenter<AdvancedTableColumnExtractionImageView> {

	private OverlayDrawnListener listener;

	interface CustomEventBinder extends EventBinder<AdvancedTableColumnExtractionImagePresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	private String displayImageName;

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);

	}

	public AdvancedTableColumnExtractionImagePresenter(BatchClassManagementController controller,
			AdvancedTableColumnExtractionImageView view) {
		super(controller, view);

		listener = new OverlayDrawnListener() {

			@Override
			public void onOverlayDraw(final PointCoordinate startCordinate, final PointCoordinate endCordinate,
					final boolean keepPreviousOverlay) {
				setCoordinates(startCordinate, endCordinate);
			}

			@Override
			public void onPointCordinateClick(PointCoordinate clickedCoordinate, boolean keepPreviousOverlay) {

			}
		};
		view.setOverlayDrawnListener(this.listener);
		if (null != view.getPageImage() && view.getPageImage().getImagePanel() == null) {
			view.getPageImage().setImagePanel(controller.getAdvTableColumnExtractionLayout().getImagePanel());
		}
	}

	private void setCoordinates(PointCoordinate startCoord, PointCoordinate endCoord) {
		BatchClassManagementEventBus.fireEvent(new SetOverlayCoordinatesEvent(startCoord, endCoord));
	}

	@Override
	public void bind() {
		view.getPageImage().setScrollIntoView(false);
		TableColumnExtractionRuleDTO selectedTableColumnExtractionRuleDTO = controller.getSelectedTableColumnExtractionRuleDTO();
		if (selectedTableColumnExtractionRuleDTO != null) {
			TableExtractionRuleDTO tableExtractionRuleDTO = selectedTableColumnExtractionRuleDTO.getTableExtractionRuleDTO();
			TableInfoDTO selectedTableInfoField = tableExtractionRuleDTO.getTableInfoDTO();
			this.displayImageName = selectedTableInfoField.getDisplayImage();
		}
		view.clearImageUpload();

		// Sets the the parameters for getting the url for the image uploaded.
		if (this.displayImageName != null && !this.displayImageName.isEmpty()) {
			BatchClassManagementEventBus.fireEvent(new AdvancedTableColumnExtractionHideEvent(false, true));
			loadImageUrl(displayImageName);
		}
	}

	@EventHandler
	public void loadImage(AdvancedTableColumnExtractionLoadImageEvent event) {
		loadImageUrl(event.getFileName());
	}

	private void loadImageUrl(String fileName) {
		if (null != view.getPageImage()) {
			view.clearOverlays();
		}
		TableInfoDTO tableInfoDTO = controller.getSelectedTableColumnExtractionRuleDTO().getTableExtractionRuleDTO().getTableInfoDTO();
		if (null != tableInfoDTO) {
			DocumentTypeDTO documentTypeDTO = tableInfoDTO.getDocTypeDTO();
			if (null != documentTypeDTO) {
				getPageImageUrl(documentTypeDTO.getBatchClass().getIdentifier(), documentTypeDTO.getName(), fileName);
			}
		}
	}

	/**
	 * Gets the url of the image to be uploaded and sets it for this view..
	 * 
	 * @param batchClassId {@link String}
	 * @param documentName {@link String}
	 * @param imageName {@link String}
	 */
	public void getPageImageUrl(final String batchClassId, final String documentName, final String imageName) {
		if (!StringUtil.isNullOrEmpty(batchClassId) && !StringUtil.isNullOrEmpty(documentName) && !StringUtil.isNullOrEmpty(imageName))
			controller.getRpcService().getAdvancedTEImageUploadPath(batchClassId, documentName, imageName,
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable paramThrowable) {
							ScreenMaskUtility.unmaskScreen();
							view.clearOverlays();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_RETRIEVING_PATH), DialogIcon.ERROR);
						}

						@Override
						public void onSuccess(String pathUrl) {
							view.clearOverlays();
							if (null == pathUrl || pathUrl.isEmpty()) {
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_RETRIEVING_PATH), DialogIcon.ERROR);
							} else {
								// ##changed for special characters in filenames.
								setDisplayImageName(imageName);
								view.setPageImageUrl(pathUrl);
								// Set Image Name in Text Field.
								BatchClassManagementEventBus.fireEvent(new AdvancedTableColumnExtractionInputPanelEvent(null,
										imageName, null, null));
								// Change States of Menu Buttons.
								BatchClassManagementEventBus.fireEvent(new AdvancedTableColumnExtractionMenuEvent(null, 0, 0));
							}
							ScreenMaskUtility.unmaskScreen();
						}
					});
	}

	@EventHandler
	public void clearOverlays(final AdvancedTableColumnExtractionHideEvent event) {
		if (event.isClearOverlays()) {
			view.clearOverlays();
		}
	}

	private void setDisplayImageName(String imageName) {
		this.displayImageName = imageName;
	}

	public void expandBottomPanel(boolean expand) {
		controller.getAdvTableColumnExtractionLayout().getBottomsPanel().setExpanded(expand);
	}

	@EventHandler
	public void createOverlays(AdvancedTableColumnExtractionImageOverlayEvent event) {
		OverlayImage overlayImage = view.getPageImage();
		if (event != null && null != event.getCoordinates() && overlayImage != null
				&& !overlayImage.getUrl().equals(AdminConstants.EMPTY_STRING)) {
			overlayImage.drawOverlay(event.getCoordinates(), false);
		}
	}

	@EventHandler
	public void saveDataInDto(AdvancedTableColumnExtractionSaveEvent event) {
		final TableExtractionRuleDTO tableExtractionRuleDTO = controller.getSelectedTableColumnExtractionRuleDTO()
				.getTableExtractionRuleDTO();
		if (null != tableExtractionRuleDTO) {
			final TableInfoDTO tableInfoDTO = tableExtractionRuleDTO.getTableInfoDTO();
			if (tableInfoDTO != null) {
				if (this.displayImageName != null && !this.displayImageName.isEmpty()) {
					tableInfoDTO.setDisplayImage(this.displayImageName);
				}
			}
		}
	}

	public void drawOverlaysIfAny() {
		BatchClassManagementEventBus.fireEvent(new AdvancedTableExtractionFireColumnChangeEvent());
	}

	public void enableApplyButton(Integer apply) {
		BatchClassManagementEventBus.fireEvent(new AdvancedTableColumnExtractionMenuEvent(apply, null, null));
	}
}
