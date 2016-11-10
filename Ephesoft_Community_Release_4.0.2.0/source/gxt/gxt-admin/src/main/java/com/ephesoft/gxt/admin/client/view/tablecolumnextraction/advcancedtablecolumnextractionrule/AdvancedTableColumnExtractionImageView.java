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

package com.ephesoft.gxt.admin.client.view.tablecolumnextraction.advcancedtablecolumnextractionrule;

import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.tablecolumnextraction.advancedtablecolumnextractionrule.AdvancedTableColumnExtractionImagePresenter;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.OverlayImage;
import com.ephesoft.gxt.core.client.ui.widget.OverlayImage.Overlay;
import com.ephesoft.gxt.core.client.ui.widget.listener.OverlayDrawnListener;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ErrorEvent;
import com.google.gwt.event.dom.client.ErrorHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class AdvancedTableColumnExtractionImageView extends
		AdvancedTableColumnExtractionInlineView<AdvancedTableColumnExtractionImagePresenter> {

	interface Binder extends UiBinder<Widget, AdvancedTableColumnExtractionImageView> {
	}

	@UiField
	protected VerticalLayoutContainer imageScrollVLayout;

	@UiField
	protected ScrollPanel imageScroll;

	@UiField
	protected OverlayImage pageImage;

	private static final Binder BINDER = GWT.create(Binder.class);

	public AdvancedTableColumnExtractionImageView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		pageImage.setVisible(false);
		pageImage.setRepositioningScroll(imageScroll);
		setWidgetIds();
		addImageLoadHandlers();
		applyCSS();
	}

	private void applyCSS() {
		this.imageScrollVLayout.addStyleName("advTCERImageScrollVLayout");
		this.imageScroll.addStyleName("advTCERImageScroll");
		this.pageImage.addStyleName("advTCERPageImage");
	}

	private void addImageLoadHandlers() {
		pageImage.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent loadEvent) {
				ScreenMaskUtility.unmaskScreen();
				if (pageImage.getUrl() == AdminConstants.EMPTY_STRING) {
					presenter.expandBottomPanel(true);
					pageImage.setVisible(false);
				} else {
					loadImage();
					presenter.expandBottomPanel(false);
					
					//Enable Apply Button
					presenter.enableApplyButton(0);
				}
			}
		});

		pageImage.addErrorHandler(new ErrorHandler() {

			@Override
			public void onError(ErrorEvent errorEvent) {
				String sourceAttribute = errorEvent.getRelativeElement().getAttribute(AdminConstants.SOURCE_ATTRIBUTE);
				if (null != sourceAttribute && !sourceAttribute.isEmpty()) {
					ScreenMaskUtility.unmaskScreen();
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
							LocaleDictionary.getMessageValue(BatchClassMessages.ERROR_UPLOAD_IMAGE), DialogIcon.ERROR);
					presenter.expandBottomPanel(true);
					pageImage.setVisible(false);
					
					//Disable Apply Button
					presenter.enableApplyButton(1);
				}
			}
		});
	}

	private void loadImage() {
		this.pageImage.setVisible(true);
		this.pageImage.setHeight(StringUtil.concatenate(this.pageImage.getHeight(), CoreCommonConstant.EMPTY_STRING));
		this.pageImage.setWidth("100%");
		ScreenMaskUtility.unmaskScreen();
		presenter.drawOverlaysIfAny();
	}

	private void setWidgetIds() {
		WidgetUtil.setID(imageScrollVLayout, "advTableColumnImageScrollVLayout");
		WidgetUtil.setID(imageScroll, "advTableColumnImageScroll");
		WidgetUtil.setID(pageImage, "advTableColumnPageImage");
	}

	public void setPageImageUrl(String pathUrl) {
		this.pageImage.setUrl(pathUrl);
	}

	public void clearImageUpload() {
		pageImage.setUrl(AdminConstants.EMPTY_STRING);
		pageImage.setVisible(false);
		
		//Disable Apply Button
		presenter.enableApplyButton(1);
	}

	public void clearOverlays() {
		Overlay.clearOverlays();
	}

	public void setOverlayDrawnListener(OverlayDrawnListener listener) {
		this.pageImage.setOverlayDrawnListener(listener);
	}

	public OverlayImage getPageImage() {
		return this.pageImage;
	}

}
