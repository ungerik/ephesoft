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

package com.ephesoft.gxt.rv.client.widget;

import java.util.Date;

import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.gxt.core.client.ui.widget.property.Validatable;
import com.ephesoft.gxt.core.client.ui.widget.util.CookieUtil;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.FieldSelectionChangeEvent;
import com.ephesoft.gxt.rv.client.event.FieldSelectionEvent;
import com.ephesoft.gxt.rv.client.event.PageSelectionEvent;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent.ResizeEndHandler;

public class DLFMultilineBox extends TextArea implements Validatable {

	private DocField bindedField;

	private String elementCookieIdentifier;

	private String valueOnFocus;

	public DLFMultilineBox(final DocField docField) {
		this.bindedField = docField;
		this.elementCookieIdentifier = getCookieIdentifier();
		this.addStyleName("dlfMultilineBox");
		if (null != bindedField) {
			if (bindedField.isReadOnly()) {
				this.setEnabled(false);
			}
			this.setValue(bindedField.getValue());
			if (!isValid()) {
				this.addStyleName("invalidField");
			}
		}
		this.addFocusHandler();
		this.addMouseUpHandler();
		this.addAttachHandler();
		this.addKeyDownHandler();
		this.addValueChangeHandler();
		this.setHeight("70px");
		addBlurHandler();
		Resizable resizable = new Resizable(this, Dir.S);
		resizable.setEnabled(true);
		addCookieSizeHandler(resizable);
		this.getElement().setPropertyInt("min-height", 10);
		
		// EPHE-8996 - Priority Issue: Sticky Fields not working in 4.0.2.0
		// Setting field read Only state based on value.
		if (null != docField && docField.isReadOnly()) {
			this.setEnabled(false);
		}
	}
	
	private void addCookieSizeHandler(Resizable resizable ) {
		resizable.addResizeEndHandler(new ResizeEndHandler() {
			
			@Override
			public void onResizeEnd(ResizeEndEvent event) {
					storeSizeInCookies();
			}
		});
	}

	private void addBlurHandler() {
		this.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(com.sencha.gxt.widget.core.client.event.BlurEvent event) {
				String text = getText();
				if (null != text && !text.equalsIgnoreCase(valueOnFocus)) {
					ReviewValidateEventBus.fireEvent(new FieldSelectionChangeEvent(bindedField));
				}
			}
		});
	}

	private void addValueChangeHandler() {
		this.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (null != bindedField) {
					bindedField.setValue(getText());
				}
			}
		});
	}

	private void addKeyDownHandler() {
		this.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.isControlKeyDown() && event.getNativeKeyCode() == KeyCodes.KEY_B && null != bindedField
						&& bindedField.getConfidence() < bindedField.getOcrConfidenceThreshold()) {
					bindedField.setOcrConfidence(100.0f);
					if (!isValid()) {
						addStyleName("invalidField");
					} else {
						removeStyleName("invalidField");
					}
				}
			}
		});
	}

	private void addAttachHandler() {
		this.addAttachHandler(new Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				String heightCookie = CookieUtil.getCookieNameForHeight(elementCookieIdentifier);
				String widthCookie = CookieUtil.getCookieNameForWidth(elementCookieIdentifier);
				String width = CookieUtil.getCookieValue(widthCookie);
				String height = CookieUtil.getCookieValue(heightCookie);
				if (!StringUtil.isNullOrEmpty(height) && !StringUtil.isNullOrEmpty(width)) {
					DLFMultilineBox.this.setSize(width, height);
				}
			}
		});

	}

	private void addMouseUpHandler() {
		this.addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {
				storeSizeInCookies();
			}
		}, MouseUpEvent.getType());
	}

	private void storeSizeInCookies() {
		int width = getOffsetWidth();
		int height = getOffsetHeight();
		String heightCookie = CookieUtil.getCookieNameForHeight(elementCookieIdentifier);
		String widthCookie = CookieUtil.getCookieNameForWidth(elementCookieIdentifier);
		Cookies.setCookie(heightCookie, String.valueOf(height), new Date(Long.MAX_VALUE / 2));
		Cookies.setCookie(widthCookie, String.valueOf(width), new Date(Long.MAX_VALUE / 2));
	}

	private void addFocusHandler() {
		this.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				if (null != bindedField) {
					valueOnFocus = getText();
					bindedField.setForceReview(false);
					getElement().scrollIntoView();
					if (isValid()) {
						DLFMultilineBox.this.removeStyleName("invalidField");
					}
					ReviewValidateNavigator.setLastSelectedDLFWidget(DLFMultilineBox.this);
					ReviewValidateNavigator.setCurrentSelectedDocField(bindedField);
					if (ReviewValidateNavigator.isPageSelectionEnable()) {
						ReviewValidateEventBus.fireEvent(new PageSelectionEvent(bindedField.getPage()));
					}
					ReviewValidateEventBus.fireEvent(new FieldSelectionEvent(bindedField));
				}
			}
		});
	}

	private String getCookieIdentifier() {
		String batchClassIdentifier = ReviewValidateNavigator.getBatchClassIdentifier();
		Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
		String documentType = currentDocument == null ? null : currentDocument.getType();
		String bindedFieldName = bindedField == null ? null : bindedField.getName();
		String concatenatedIdentfier = StringUtil.concatenate(batchClassIdentifier, documentType, bindedFieldName);
		return StringUtil.isNullOrEmpty(concatenatedIdentfier) ? concatenatedIdentfier : concatenatedIdentfier.replaceAll(
				CoreCommonConstant.SPACE, CoreCommonConstant.UNDERSCORE);
	}

	public DocField getBindedField() {
		return bindedField;
	}

	@Override
	public void enableValidation(boolean enable) {
	}

	public boolean isValid() {
		boolean isValid = true;
		if (null != bindedField) {
			isValid = (!bindedField.isForceReview()) && (bindedField.getOcrConfidence() >= bindedField.getOcrConfidenceThreshold());
		}
		if (isValid) {
			addStyleName("invalidField");
		} else {
			removeStyleName("invalidField");
		}
		return isValid;
	}

	@Override
	public void appendValue(String value, boolean append) {
		String newValue = value;
		if (append) {
			newValue = StringUtil.concatenate(getValue(), CoreCommonConstant.EMPTY_STRING, value);
		}
		this.setValue(newValue);
	}

	@Override
	public void focus() {
		Timer timer = new Timer() {

			@Override
			public void run() {
				setFocus();
			}
		};
		timer.schedule(50);
	}
	
	private void setFocus() {
		super.focus();
	}

	@Override
	public void blur() {
		getElement().blur();
	}

}
