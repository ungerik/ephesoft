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

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Coordinates;
import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionImageViewPresenter;
import com.ephesoft.gxt.core.client.constant.CssConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.CoordinatesDTO;
import com.ephesoft.gxt.core.shared.dto.OutputDataCarrierDTO;
import com.ephesoft.gxt.core.shared.dto.PointCoordinate;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class AdvancedKVExtractionImageView extends AdvancedKVExtractionInlineView<AdvancedKVExtractionImageViewPresenter> {

	interface Binder extends UiBinder<Widget, AdvancedKVExtractionImageView> {
	}

	@UiField
	protected HorizontalLayoutContainer uploadImage;
	@UiField
	protected OverlayImage pageImage;
	@UiField
	protected ScrollPanel imageScroll;

	@UiField
	protected VerticalLayoutContainer imageScrollVLayout;

	@UiField
	protected Button previousPage;
	@UiField
	protected Button nextPage;

	private static final int IMAGE_WIDTH_FACTOR = 72;

	/**
	 * Y factor to be used for image's y coordinate calculation.
	 */
	private static final int IMAGE_HEIGHT_FACTOR = 72;

	/**
	 * Y-Factor to be used for overlay creation.
	 */
	private static final double IMAGE_Y_FACTOR = 0.2328;

	/**
	 * X-Factor to be used for overlay creation.
	 */
	private static final double IMAGE_X_FACTOR = 0.2543;
	private Integer originalWidth = 0;
	private Integer originalHeight = 0;
	private static final Binder BINDER = GWT.create(Binder.class);
	private String length = "";
	private String width = "";
	private String xOffset = "";
	private String yOffset = "";
	private Coordinates keyCoordinates = null;
	private Coordinates valueCoordinates = null;
	private PointCoordinate mouseCoordinate = null;
	private boolean isHOCRImage = false;

	public AdvancedKVExtractionImageView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		enableDisableButtons(false, false);
		pageImage.setVisible(false);
		addImageLoadHandlers();
		addButtonActionHandlers();
		pageImage.setRepositioningScroll(imageScroll);
		WidgetUtil.setID(imageScroll, "imageScroll");
		imageScroll.addStyleName(CssConstants.CSS_ADV_KV_IMAGE_SCROLL_PANEL);
		uploadImage.addStyleName(CssConstants.CSS_ADV_KV_IMAGE_PANEL);
		previousPage.setStylePrimaryName(CssConstants.CSS_ADV_KV_PAGE_BUTTON);
		nextPage.setStylePrimaryName(CssConstants.CSS_ADV_KV_PAGE_BUTTON);
		previousPage.addStyleDependentName("previous");
		nextPage.addStyleDependentName("next");
	}

	protected void getSpanValuesAtOriginalCoordinate(PointCoordinate pointCoordinate) {
		Coordinates coordinate = null;
		final int imageLeft = pageImage.getAbsoluteLeft();
		final int imageTop = pageImage.getAbsoluteTop();
		final int naturalWidth = pageImage.getNaturalWidth();
		final int naturalHeight = pageImage.getNaturalHeight();
		final int imageWidth = pageImage.getWidth();
		final int imageHeight = pageImage.getHeight();
		if (pointCoordinate != null && imageWidth > 0 && imageHeight > 0) {

			float heightCompressionFactor = (naturalHeight * 1.0f) / imageHeight;
			float widthCompressionFactor = (naturalWidth * 1.0f) / imageWidth;
			int actualCoordinateLeft = Math.round((pointCoordinate.getxCoordinate() * widthCompressionFactor));
			int actualCoordinateTop = Math.round((pointCoordinate.getyCoordinate() * heightCompressionFactor));
			coordinate = new Coordinates();

			coordinate.setX0(BigInteger.valueOf(actualCoordinateLeft));
			coordinate.setY0(BigInteger.valueOf(actualCoordinateTop));
		}
		mouseCoordinate = new PointCoordinate(imageLeft + pointCoordinate.getxCoordinate(), imageTop
				+ pointCoordinate.getyCoordinate());
	}

	private void addButtonActionHandlers() {
		previousPage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.onPreviousPageClicked();

			}
		});
		nextPage.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				presenter.onNextPageClicked();
			}
		});
	}

	private void addImageLoadHandlers() {

		pageImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent loadEvent) {
				ScreenMaskUtility.unmaskScreen();
				originalWidth = pageImage.getWidth();
				originalHeight = pageImage.getHeight();
				loadImage();
			}
		});

		pageImage.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent errorEvent) {
				String sourceAttribute = errorEvent.getRelativeElement().getAttribute(AdminConstants.SOURCE_ATTRIBUTE);
				if (null != sourceAttribute && !sourceAttribute.isEmpty()) {
					ScreenMaskUtility.unmaskScreen();
					presenter.setEditAdvancedKV(false);

					clearInvalidImageUpload();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_UPLOAD_IMAGE), DialogIcon.ERROR);
				}
			}
		});

	}

	public void clearInvalidImageUpload() {
		// To Be Implemented
	}

	public void setPageImageUrl(final String pageImageUrl) {
		pageImage.clearOverlays();
		imageScrollVLayout.clear();
		imageScroll = new ScrollPanel();
		imageScroll.add(pageImage);
		WidgetUtil.setID(imageScroll, "imageScroll");
		imageScroll.addStyleName(CssConstants.CSS_ADV_KV_IMAGE_SCROLL_PANEL);
		imageScrollVLayout.add(imageScroll);
		this.pageImage.setUrl(pageImageUrl);
		pageImage.setRepositioningScroll(imageScroll);

	}

	private void loadImage() {
		this.pageImage.clearOverlays();
		this.pageImage.clearLabels();
		enableDisableButtons();
		
		this.pageImage.setHeight(StringUtil.concatenate(this.pageImage.getHeight(), CoreCommonConstant.EMPTY_STRING));
		this.pageImage.setWidth("100%");
		this.pageImage.setVisible(true);
//		int screenWidth = XDOM.getViewportWidth();
//		Integer imageWidth = screenWidth * IMAGE_WIDTH_FACTOR / 100;
//		Integer imageHeight = Integer.valueOf(0);
//		if (originalWidth != 0) {
//			imageHeight = originalHeight * imageWidth / originalWidth;
//		}
//		if (imageHeight == 0) {
//			int screenHeight = XDOM.getViewportHeight();
//			imageHeight = screenHeight * IMAGE_HEIGHT_FACTOR / 100;
//		}
//		pageImage.setWidth(imageWidth.toString() + "px");
//		pageImage.setHeight(imageHeight.toString() + "px");
		if (!isHOCRImage && this.pageImage.isVisible()) {
			presenter.createDefaultOverlay();
		} else if (isHOCRImage && this.pageImage.isVisible()) {
			presenter.createHOCROverlay();
		}
		ScreenMaskUtility.unmaskScreen();
	}

	private void enableDisableButtons() {
		String url = pageImage.getUrl();
		if (url == null || url.isEmpty() || !pageImage.isVisible()) {
			presenter.enableDisableMenuButtons(false, false, false);
			enableDisableButtons(false, false);
			presenter.enableDisableHOCRButton(false);
		} else {
			if (isHOCRImage) {
				presenter.enableDisableMenuButtons(false, false, false);
			} else {
				presenter.enableDisableMenuButtons(true, true, false);
			}
			presenter.enableDisableHOCRButton(true);
		}
	}

	public void createKeyValueOverlay(Coordinates keyCoordinates, Coordinates valueCoordinates, boolean isEditAndNew) {
		this.pageImage.setType(OverlayType.DEFAULT);
		pageImage.clearOverlays();
		pageImage.setRepositioningScroll(imageScroll);
		pageImage.setImageLoaded(true);
		pageImage.drawColoredOverlays(keyCoordinates, false, "#8A2908", true, false, isEditAndNew);
		pageImage.drawColoredOverlays(valueCoordinates, true, "#3B0B39", true, true, isEditAndNew);
		setValues(keyCoordinates, valueCoordinates);
	}

	public void createOverlays(List<Object> carrierDTOs) {
		int counter = 0;
		boolean drawOverlays = false;
		for (Object odto : carrierDTOs) {
			if (odto instanceof OutputDataCarrierDTO) {
				this.pageImage.setType(OverlayType.COLOR);
				OutputDataCarrierDTO dto = (OutputDataCarrierDTO) odto;
				Coordinates keyCoordinates = convertDTOToBO(dto.getKeyCoordinates());
				Coordinates valueCoordinates = convertDTOToBO(dto.getCoordinates());
				if (counter == 0)
					pageImage.drawColoredOverlays(keyCoordinates, false, dto.getColorCode(), false);
				else
					pageImage.drawColoredOverlays(keyCoordinates, true, dto.getColorCode(), false);
				counter++;
				if (counter == carrierDTOs.size())
					drawOverlays = true;
				pageImage.drawColoredOverlays(valueCoordinates, true, dto.getColorCode(), drawOverlays);
			} else if (odto instanceof Span) {
				this.pageImage.setType(OverlayType.LABEL);

				Span span = (Span) odto;
				Coordinates keyCoordinates = span.getCoordinates();
				counter++;
				if (counter == carrierDTOs.size())
					drawOverlays = true;
				if (counter == 1)
					pageImage.drawLabels(keyCoordinates, false, span.getValue(), false);
				else
					pageImage.drawLabels(keyCoordinates, true, span.getValue(), drawOverlays);

			}
		}
	}

	private Coordinates convertDTOToBO(CoordinatesDTO coordinatesDTO) {
		// TODO Auto-generated method stub
		Coordinates coordinates = new Coordinates();
		coordinates.setX0(coordinatesDTO.getX0());
		coordinates.setY0(coordinatesDTO.getY0());
		coordinates.setX1(coordinatesDTO.getX1());
		coordinates.setY1(coordinatesDTO.getY1());
		return coordinates;
	}

	public void clearImageUpload() {
		pageImage.setVisible(false);
		presenter.enableDisableMenuButtons(false, false, false);
	}

	public void setValues(Coordinates keyCoordinates, Coordinates valueCoordinates) {
		presenter.setValues(keyCoordinates, valueCoordinates);
		double aspectRatio = 1;
		double lengthOfBox = (valueCoordinates.getX1().intValue() - valueCoordinates.getX0().intValue()) * aspectRatio;
		double widthOfBox = (valueCoordinates.getY1().intValue() - valueCoordinates.getY0().intValue()) * aspectRatio;
		int lengthOfBoxInInt = (int) Math.round(lengthOfBox);
		int widthOfBoxInInt = (int) Math.round(widthOfBox);
		this.length = String.valueOf(lengthOfBoxInInt);
		this.width = String.valueOf(widthOfBoxInInt);

		// setLocation(locationType);
		final int keyX0 = (int) Math.round(keyCoordinates.getX0().intValue() * aspectRatio);
		final int keyX1 = (int) Math.round(keyCoordinates.getX1().intValue() * aspectRatio);
		final int keyY0 = (int) Math.round(keyCoordinates.getY0().intValue() * aspectRatio);
		final int keyY1 = (int) Math.round(keyCoordinates.getY1().intValue() * aspectRatio);
		final int valueX0 = (int) Math.round(valueCoordinates.getX0().intValue() * aspectRatio);
		final int valueY0 = (int) Math.round(valueCoordinates.getY0().intValue() * aspectRatio);

		final double xOffset = valueX0 - (keyX0 + keyX1) / 2.0;
		final double yOffset = valueY0 - (keyY0 + keyY1) / 2.0;

		this.xOffset = String.valueOf((int) Math.round(xOffset));
		this.yOffset = String.valueOf((int) Math.round(yOffset));

	}

	public String getLength() {
		return length;
	}

	public String getWidth() {
		return width;
	}

	public String getxOffset() {
		return xOffset;
	}

	public String getyOffset() {
		return yOffset;
	}

	public Coordinates getKeyCoordinates() {
		return keyCoordinates;
	}

	public void setKeyCoordinates(Coordinates keyCoordinates) {
		this.keyCoordinates = keyCoordinates;
	}

	public Coordinates getValueCoordinates() {
		return valueCoordinates;
	}

	public void setValueCoordinates(Coordinates valueCoordinates) {
		this.valueCoordinates = valueCoordinates;
	}

	public void setxOffset(String xOffset) {
		this.xOffset = xOffset;
	}

	public void setyOffset(String yOffset) {
		this.yOffset = yOffset;
	}

	public void setLengthOfRect(String length) {
		this.length = length;
	}

	public void setWidthOfRect(String width) {
		this.width = width;
	}

	public void setOverlayValues() {
		LinkedList<Coordinates> keyValueCoordinates = pageImage.getOverlayCoordinates();
		keyCoordinates = keyValueCoordinates.get(0);
		valueCoordinates = keyValueCoordinates.get(1);
		setValues(keyCoordinates, valueCoordinates);

	}

	public void enableDisableButtons(boolean nextButton, boolean prevButton) {
		nextPage.setVisible(nextButton);
		previousPage.setVisible(prevButton);
//		nextPage.setEnabled(nextButton);
//		previousPage.setEnabled(prevButton);
	}

	public void scrollImageToSelectedKey(CoordinatesDTO coordinate) {
		final int imageTop = pageImage.getOriginLeft();
		final BigInteger y_start_cordinate = coordinate.getY0();
		final int imageHeight = pageImage.getHeight();
		final int naturalHeight = pageImage.getNaturalHeight();
		float heightCompressionFactor = (imageHeight * 1.0f) / naturalHeight;
		int overlayTop = Math.round((imageTop) + (y_start_cordinate.intValue() * heightCompressionFactor));
		this.imageScroll.setVerticalScrollPosition(overlayTop);
	}

	public void clearOverlays() {
		pageImage.clearOverlays();

	}

	public void clearLabels() {
		pageImage.clearLabels();
	}

	public LinkedList<Integer> getPageOriginalsize() {
		LinkedList<Integer> size = new LinkedList<Integer>();
		size.add(this.pageImage.getNaturalWidth());
		size.add(this.pageImage.getNaturalHeight());
		return size;
	}

	public void setHOCRImage(boolean isHOCR) {
		this.isHOCRImage = isHOCR;
		if (isHOCR)
			this.pageImage.setType(OverlayType.LABEL);
		else
			this.pageImage.setType(OverlayType.DEFAULT);
	}

	public OverlayImage getPageImage() {
		return pageImage;
	}

	public void setPageImage(OverlayImage pageImage) {
		this.pageImage = pageImage;
	}

	public List<Coordinates> getDefaultCoordinates() {
		return this.pageImage.getDefaultCoordinates();
	}

	public VerticalLayoutContainer getImageScrollVLayout() {
		return imageScrollVLayout;
	}

	public void setImageScrollVLayout(VerticalLayoutContainer imageScrollVLayout) {
		this.imageScrollVLayout = imageScrollVLayout;
	}

	public void setButtonsHeight(int height) {
		if (height > 2) {
			this.previousPage.getElement().getStyle().setTop(height - 22, Unit.PX);
			this.nextPage.getElement().getStyle().setTop(height - 22, Unit.PX);
		}
	}

}
