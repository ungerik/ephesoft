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

import java.math.BigInteger;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.event.AdvancedKVExtractionHideEvent;
import com.ephesoft.gxt.admin.client.event.ReloadImageEvent;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionImageView;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.OverlayImage;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.shared.dto.AdvancedKVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.AdvancedKVExtractionDetailDTO;
import com.ephesoft.gxt.core.shared.dto.CoordinatesDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.KVExtractionDTO;
import com.ephesoft.gxt.core.shared.dto.OutputDataCarrierDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;
import com.google.web.bindery.event.shared.binder.EventHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class AdvancedKVExtractionImageViewPresenter extends AdvancedKVExtractionInlinePresenter<AdvancedKVExtractionImageView> {

	interface CustomEventBinder extends EventBinder<AdvancedKVExtractionImageViewPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	List<Span> result;

	public AdvancedKVExtractionImageViewPresenter(BatchClassManagementController controller, AdvancedKVExtractionImageView view) {
		super(controller, view);
		controller.getAdvancedKVLayout().getImagePanel().addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				setBoundaryOnOverlays();
			}
		});
		ContentPanel imagePanel = controller.getAdvancedKVLayout().getImagePanel();
		if (null != imagePanel && null != view.getPageImage()) {
			view.getPageImage().setImagePanel(imagePanel);
		}
	}

	public void setBoundaryOnOverlays() {
		ContentPanel imagePanel = controller.getAdvancedKVLayout().getImagePanel();
		int height = imagePanel.getOffsetHeight();
		view.setButtonsHeight(Math.round(height / 2));
	}

	@Override
	public void bind() {
		view.getPageImage().setUrl(BatchClassConstants.EMPTY_STRING);
		view.getPageImage().setVisible(false);
		view.getPageImage().setCachedOverlaysToNull();
		if (controller.getAdvKvExtractionPresenter().isEdit()) {
			KVExtractionDTO selectedKVExtraction = controller.getSelectedKVExtraction();
			if (selectedKVExtraction != null) {
				if (selectedKVExtraction.getXoffset() != null) {
					view.setxOffset(String.valueOf(selectedKVExtraction.getXoffset()));
				} else {
					view.setxOffset(AdminConstants.EMPTY_STRING);
				}
				if (selectedKVExtraction.getYoffset() != null) {
					view.setyOffset(String.valueOf(selectedKVExtraction.getYoffset()));
				} else {
					view.setyOffset(AdminConstants.EMPTY_STRING);
				}
				if (selectedKVExtraction.getWidth() != null) {
					view.setWidthOfRect(String.valueOf(selectedKVExtraction.getWidth()));
				} else {
					view.setWidthOfRect(AdminConstants.EMPTY_STRING);
				}
				if (selectedKVExtraction.getLength() != null) {
					view.setLengthOfRect(String.valueOf(selectedKVExtraction.getLength()));
				} else {
					view.setLengthOfRect(AdminConstants.EMPTY_STRING);
				}
			}
		}
		// To set Buttons height
		Timer timer = new Timer() {

			@Override
			public void run() {
				setBoundaryOnOverlays();
			}
		};
		timer.schedule(100);

	}

	@Override
	public void injectEvents(EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);

	}

	@EventHandler
	public void reloadPage(final ReloadImageEvent reloadImageEvent) {
		String fileName = reloadImageEvent.getFileName();
		String imageName = fileName;
		if (fileName.contains("/")) {
			imageName = fileName.substring(fileName.lastIndexOf("/") + 1);
		}
		boolean getPageCount = false;
		FieldTypeDTO fieldTypeDTO = controller.getSelectedFieldType();
		if (null != fieldTypeDTO && fieldTypeDTO.getDocTypeDTO() != null) {
			controller.getRpcService().getAdvancedKVImageUploadPath(fieldTypeDTO.getDocTypeDTO().getBatchClass().getIdentifier(),
					fieldTypeDTO.getDocTypeDTO().getName(), imageName, getPageCount, new AsyncCallback<List<String>>() {

						@Override
						public void onFailure(Throwable paramThrowable) {
							ScreenMaskUtility.unmaskScreen();
							DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
									LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_RETRIEVING_PATH), DialogIcon.ERROR);
						}

						@Override
						public void onSuccess(final List<String> pathUrl) {
							ScreenMaskUtility.unmaskScreen();

							if (null == pathUrl || pathUrl.isEmpty() || pathUrl.get(0) == null) {
								DialogUtil.showMessageDialog(LocaleDictionary.getMessageValue(BatchClassMessages.ERROR),
										LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_RETRIEVING_PATH_OR_UPLOADING_FILE),
										DialogIcon.ERROR);
							} else {
								view.setPageImageUrl(checkOldUrlWithNewUrl(pathUrl.get(0)));
								view.getPageImage().setVisible(true);
							}
						}
					});
		}

		// Collapse Bottom Panel
		ContentPanel panel = controller.getAdvancedKVLayout().getBottomsPanel();
		panel.setExpanded(false);

	}

	/**
	 * Check if the previously uploaded image URL is the same as the newly uploaded image URL. Adds a parameter with the new URL is old
	 * and new URL are the same.
	 * 
	 * @param pageImageUrl {@link String}
	 * @return newURL {@link String}
	 */
	private String checkOldUrlWithNewUrl(String pageImageUrl) {
		if (!StringUtil.isNullOrEmpty(pageImageUrl)) {
			OverlayImage image = view.getPageImage();
			if (null != image) {
				if (image.getUrl().equalsIgnoreCase(pageImageUrl)) {
					pageImageUrl = StringUtil.concatenate(pageImageUrl, BatchClassConstants.QUESTION_MARK,
							BatchClassConstants.SAME_IMAGE_URL_PARAM, BatchClassConstants.EQUAL_TO, Boolean.TRUE);
				}
			}
		}
		return pageImageUrl;
	}

	public boolean isEditAdvancedKV() {
		return controller.getAdvKvExtractionPresenter().isEdit();
	}

	public void setEditAdvancedKV(boolean isEditAdvancedKV) {
		controller.getAdvKvExtractionPresenter().setEdit(isEditAdvancedKV);
	}

	public void createDefaultOverlay() {
		Coordinates keyCoordinates = null;
		Coordinates valueCoordinates = null;
		AdvancedKVExtractionDTO advKVDTO = controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO();
		if (null != advKVDTO && controller.getAdvKvExtractionPresenter().isEdit()) {
			keyCoordinates = getCoordinate(advKVDTO.getKeyX0Coord(), advKVDTO.getKeyY0Coord(), advKVDTO.getKeyX1Coord(),
					advKVDTO.getKeyY1Coord());
			valueCoordinates = getCoordinate(advKVDTO.getValueX0Coord(), advKVDTO.getValueY0Coord(), advKVDTO.getValueX1Coord(),
					advKVDTO.getValueY1Coord());
		}
		if (null == keyCoordinates || null == valueCoordinates) {
			keyCoordinates = view.getPageImage().getDefaultKeyCoordinates();// getCoordinate(60, 120, 200, 160);
			valueCoordinates = view.getPageImage().getDefaultValueCoordinates();// getCoordinate(300, 140, 440, 180);
		}
		if (null != keyCoordinates && null != valueCoordinates) {
			AdvancedKVExtractionDetailDTO detailDTO = advKVDTO.getAdvKVExtractionDetailByFileName(controller
					.getAdvKvExtractionPresenter().getAdvKVButtonPanelPresenter().getView().getImageSelected());
			view.createKeyValueOverlay(keyCoordinates, valueCoordinates, controller.getAdvKvExtractionPresenter().isEdit()
			/* && !detailDTO.isNew() */);
		}
	}

	public Coordinates getCoordinate(Integer x0, Integer y0, Integer x1, Integer y1) {
		Coordinates coordinates = null;
		if (null != x0 && null != y0 && null != x1 && null != y1) {
			coordinates = new Coordinates();
			coordinates.setX0(BigInteger.valueOf(x0));
			coordinates.setY0(BigInteger.valueOf(y0));
			coordinates.setX1(BigInteger.valueOf(x1));
			coordinates.setY1(BigInteger.valueOf(y1));
		}
		return coordinates;
	}

	public void createAdvKVOverlays(List<OutputDataCarrierDTO> carrierDTOs) {
		view.createOverlays(convertDTOToObjectList(carrierDTOs));
	}

	private Coordinates convertDTOToBO(CoordinatesDTO coordinatesDTO) {
		Coordinates coordinates = new Coordinates();
		coordinates.setX0(coordinatesDTO.getX0());
		coordinates.setY0(coordinatesDTO.getY0());
		coordinates.setX1(coordinatesDTO.getX1());
		coordinates.setY1(coordinatesDTO.getY1());
		return coordinates;
	}

	private native int getViewPortWidth() /*-{
											return $wnd.getViewPortWidth();
											}-*/;

	private int getViewPortHeight() {
		String currentBrowser = getUserAgent();
		if (currentBrowser != null && currentBrowser.length() > 0 && currentBrowser.contains("msie")) {
			return getViewPortHeightForIE();
		} else {
			return getViewPortHeightForFirefox();
		}
	}

	public static native String getUserAgent() /*-{
												return navigator.userAgent.toLowerCase();
												}-*/;

	private native int getViewPortHeightForIE() /*-{
												return $wnd.getViewPortHeightForIE();
												}-*/;

	private native int getViewPortHeightForFirefox() /*-{
														return $wnd.getViewPortHeight();
														}-*/;

	public void saveDataInDTO(KVExtractionDTO selectedKVExtraction) {
		// Set coordinates of Overlay into variables.
		view.setOverlayValues();

		if (view.getLength() != null && !view.getLength().isEmpty()) {
			Integer length = Integer.parseInt(view.getLength());
			selectedKVExtraction.setLength(length);
		}
		if (view.getWidth() != null && !view.getWidth().isEmpty()) {
			Integer width = Integer.parseInt(view.getWidth());
			selectedKVExtraction.setWidth(width);
		}
		if (view.getxOffset() != null && !view.getxOffset().isEmpty()) {
			Integer xoffset = Integer.parseInt(view.getxOffset());
			selectedKVExtraction.setXoffset(xoffset);
		}
		if (view.getyOffset() != null && !view.getyOffset().isEmpty()) {
			Integer yoffset = Integer.parseInt(view.getyOffset());
			selectedKVExtraction.setYoffset(yoffset);
		}
		AdvancedKVExtractionDTO advancedKVExtractionDTO = controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO();

		if (!isEmptyCoordinates(view.getKeyCoordinates()) && !isEmptyCoordinates(view.getValueCoordinates())) {
			setKeyValueCoordinates(view.getKeyCoordinates(), view.getValueCoordinates(), advancedKVExtractionDTO);
		}

		AdvancedKVExtractionDTO selectedAdvKvExtractionDTO = selectedKVExtraction.getAdvancedKVExtractionDTO();

		// Validate of stored values are not replaced by null values
		if (selectedAdvKvExtractionDTO == null && validAdvancedKVExtractionDTO(advancedKVExtractionDTO)) {
			selectedAdvKvExtractionDTO = new AdvancedKVExtractionDTO();
			selectedKVExtraction.setAdvancedKVExtractionDTO(selectedAdvKvExtractionDTO);
		}
		if (null != selectedAdvKvExtractionDTO) {
			controller.getAdvKvExtractionPresenter().mergeAdvancedKVExtractionDTO(selectedAdvKvExtractionDTO, advancedKVExtractionDTO);
			// mergeAdvancedKVExtractionDTO(selectedAdvKvExtractionDTO, advancedKVExtractionDTO);
		}
	}

	private boolean isEmptyCoordinates(Coordinates coordinates) {
		boolean returnVal = false;
		if (coordinates.getX0().intValue() == 0 && coordinates.getX1().intValue() == 0 && coordinates.getY0().intValue() == 0
				&& coordinates.getY1().intValue() == 0) {
			returnVal = true;
		}
		return returnVal;
	}

	private void setKeyValueCoordinates(final Coordinates keyCoord, final Coordinates valueCoord,
			AdvancedKVExtractionDTO advancedKVExtractionDTO) {
		double aspectWidthRatio = 1;
		double aspectHeightRatio = 1;
		advancedKVExtractionDTO.setKeyX0Coord((int) Math.round(keyCoord.getX0().intValue() / aspectWidthRatio));
		advancedKVExtractionDTO.setKeyX1Coord((int) Math.round(keyCoord.getX1().intValue() / aspectWidthRatio));
		advancedKVExtractionDTO.setKeyY0Coord((int) Math.round(keyCoord.getY0().intValue() / aspectHeightRatio));
		advancedKVExtractionDTO.setKeyY1Coord((int) Math.round(keyCoord.getY1().intValue() / aspectHeightRatio));
		advancedKVExtractionDTO.setValueX0Coord((int) Math.round(valueCoord.getX0().intValue() / aspectWidthRatio));
		advancedKVExtractionDTO.setValueX1Coord((int) Math.round(valueCoord.getX1().intValue() / aspectWidthRatio));
		advancedKVExtractionDTO.setValueY0Coord((int) Math.round(valueCoord.getY0().intValue() / aspectHeightRatio));
		advancedKVExtractionDTO.setValueY1Coord((int) Math.round(valueCoord.getY1().intValue() / aspectHeightRatio));
	}

	private boolean validAdvancedKVExtractionDTO(AdvancedKVExtractionDTO advancedKVExtractionDTO) {
		boolean isValid = false;
		isValid = null != advancedKVExtractionDTO.getSelectedImageDisplayName()
				|| null != advancedKVExtractionDTO.getSelectedImageName() || !isEmptyCoordinates(view.getKeyCoordinates())
				&& !isEmptyCoordinates(view.getValueCoordinates());
		return isValid;
	}

	private void mergeAdvancedKVExtractionDTO(AdvancedKVExtractionDTO selAdvKVExtractionDTO, AdvancedKVExtractionDTO advKVExtractionDTO) {
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
	}

	public void enableDisableMenuButtons(boolean apply, boolean testAdvKV, boolean clear) {
		controller.getAdvKvExtractionPresenter().getAdvKVButtonPanelPresenter().enableDisableMenuButtons(apply, testAdvKV, clear);
	}

	public void setImageNameInDTO(final String imageName, final String displayImageName) {
		controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO().setSelectedImageName(imageName);
		controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO().setSelectedImageDisplayName(displayImageName);
	}

	public void setValues(Coordinates keyCoordinates, Coordinates valueCoordinates) {
		AdvancedKVExtractionDTO dto = controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO();
		dto.setKeyX0Coord(keyCoordinates.getX0().intValue());
		dto.setKeyY0Coord(keyCoordinates.getY0().intValue());
		dto.setKeyX1Coord(keyCoordinates.getX1().intValue());
		dto.setKeyY1Coord(keyCoordinates.getY1().intValue());

		dto.setValueX0Coord(valueCoordinates.getX0().intValue());
		dto.setValueY0Coord(valueCoordinates.getY0().intValue());
		dto.setValueX1Coord(valueCoordinates.getX1().intValue());
		dto.setValueY1Coord(valueCoordinates.getY1().intValue());

	}

	public void onPreviousPageClicked() {
		controller.getAdvKvExtractionPresenter().getAdvKVButtonPanelPresenter().onNextPreviousButtonClicked(false);

	}

	public void onNextPageClicked() {
		controller.getAdvKvExtractionPresenter().getAdvKVButtonPanelPresenter().onNextPreviousButtonClicked(true);
	}

	public void enabledisableButtons(boolean nextButton, boolean prevButton) {
		view.enableDisableButtons(nextButton, prevButton);

	}

	public void scrollImageToSelectedKey(CoordinatesDTO coordinates) {
		view.scrollImageToSelectedKey(coordinates);
	}

	public void removeAllOverlays() {
		view.clearOverlays();
	}

	public void removeAllLabels() {
		view.clearLabels();
	}

	public void clearButtonClicked() {
		removeAllOverlays();
		createDefaultOverlay();
	}

	public void cancelButtonClicked() {
		removeAllOverlays();
		view.clearImageUpload();

	}

	public boolean validateLengthWidth() {
		boolean validFlag = false;
		int length = 0, width = 0;
		if (view.getLength() != null && !view.getLength().isEmpty()) {
			length = Integer.parseInt(view.getLength());
		}
		if (view.getWidth() != null && !view.getWidth().isEmpty()) {
			width = Integer.parseInt(view.getWidth());
		}
		if (length == 0 || width == 0) {
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.ADVANCED_KV_ERROR), DialogIcon.ERROR);
			validFlag = false;
		} else
			validFlag = true;
		return validFlag;
	}

	public void generateHOCRData(String url, boolean isSelected) {
		final String fileName = controller.getAdvKvExtractionPresenter().getAdvancedKVExtractionDTO().getSelectedImageName();
		FieldTypeDTO fieldTypeDTO = controller.getSelectedFieldType();
		if (null != fieldTypeDTO && fieldTypeDTO.getDocTypeDTO() != null) {
			if (isSelected) {

				ScreenMaskUtility.maskScreen();
				controller.getRpcService().getSpanValues(fieldTypeDTO.getDocTypeDTO().getBatchClass(), fileName,
						fieldTypeDTO.getDocTypeDTO().getName(), view.getPageOriginalsize(), new AsyncCallback<List<Span>>() {

							@Override
							public void onSuccess(List<Span> result) {
								if (result != null) {
									setResult(result);
									String emptyImageName = StringUtil.concatenate(fileName.substring(0, fileName.lastIndexOf(".")),
											"empty", FileType.PNG.getExtensionWithDot());
									view.setHOCRImage(true);
									controller.getEventBus().fireEvent(new ReloadImageEvent(emptyImageName));
								}
							}

							@Override
							public void onFailure(Throwable caught) {
								ScreenMaskUtility.unmaskScreen();
								DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
										LocaleDictionary.getMessageValue(BatchClassMessages.KV_HOCR_FAILURE), DialogIcon.ERROR);
								controller.getAdvKvExtractionPresenter().getAdvKVButtonPanelPresenter().setViewOCRTogglerState(false);
							}
						});
			} else {
				controller.getRpcService().deleteEmptyFile(fieldTypeDTO.getDocTypeDTO().getBatchClass(), fileName,
						fieldTypeDTO.getDocTypeDTO().getName(), new AsyncCallback<Void>() {

							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(Void result) {
							}
						});
				view.setHOCRImage(false);

				controller.getEventBus().fireEvent(new ReloadImageEvent(url));
				view.getPageImage().setVisible(true);
				// Collapse Bottom Panel
				ContentPanel panel = controller.getAdvancedKVLayout().getBottomsPanel();
				panel.setExpanded(false);
			}
		}

	}

	protected List<Object> convertSpanListToObjectList(List<Span> result) {
		List<Object> listObject = new LinkedList<Object>();
		for (Span object : result) {
			listObject.add(object);
		}
		return listObject;
	}

	protected List<Object> convertDTOToObjectList(List<OutputDataCarrierDTO> carrierDTOs) {
		List<Object> listObject = new LinkedList<Object>();
		for (OutputDataCarrierDTO object : carrierDTOs) {
			OutputDataCarrierDTO dto = (OutputDataCarrierDTO) object;
			listObject.add(dto);
		}
		return listObject;
	}

	public void createHOCROverlay() {
		view.createOverlays(convertSpanListToObjectList(getResult()));
	}

	public List<Span> getResult() {
		return this.result;
	}

	public void setResult(List<Span> result) {
		this.result = result;
	}

	public void setImageUrl(String pageImageUrl) {
		controller.getAdvKvExtractionPresenter().getAdvKVButtonPanelPresenter().setImageUrl(pageImageUrl);
	}

	public void enableDisableHOCRButton(boolean isEnabled) {
		controller.getAdvKvExtractionPresenter().getAdvKVButtonPanelPresenter().enableDisableHOCRButton(isEnabled);
	}

	@EventHandler
	public void hideAdvKVScreen(final AdvancedKVExtractionHideEvent event) {
		removeAllOverlays();
		removeAllLabels();
		view.getPageImage().setVisible(false);
	}

}
