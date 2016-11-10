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

import com.ephesoft.gxt.core.client.constant.PropertyAccessModel;
import com.ephesoft.gxt.core.client.i18n.LocaleConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleMessages;
import com.ephesoft.gxt.core.client.ui.widget.util.CookieUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.core.shared.util.ValidationUtil;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.event.DisableEvent;
import com.sencha.gxt.widget.core.client.event.DisableEvent.DisableHandler;
import com.sencha.gxt.widget.core.client.event.EnableEvent;
import com.sencha.gxt.widget.core.client.event.EnableEvent.EnableHandler;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

public class PagingToolbar extends PagingToolBar {

	private final Grid<?> bindedGrid;
	private ToolBarMessage messages;
	private final Label resetLabel;

	private String BUTTON_DISABLE_CSS = "buttonDisable";

	private IntegerField pagingField;
	protected Label pageSizePrefixLabel, pageSizeSuffixLabel;

	private static final String PAGING_COOKIE_PREFIX = "GRID_";

	public PagingToolbar(final int rowsInPage, final Grid<?> bindedGrid) {
		super(getPageSize(rowsInPage, bindedGrid));
		pagingField = new IntegerField();
		this.bindedGrid = bindedGrid;
		resetLabel = new Label();
		resetLabel.setTitle(LocaleConstants.RESET);
		this.insert(resetLabel, 13);
		this.addStyleName("toolbarTextAlign");
		resetLabel.addStyleName("resetImage");
		prev.addStyleName("previousToolbarButton");
		next.addStyleName("nextToolbarButton");
		last.addStyleName("lastToolbarButton");
		first.addStyleName("firstToolbarButton");
		refresh.addStyleName("refreshToolbarButton");
		prev.addStyleName("toolbarButton");
		next.addStyleName("toolbarButton");

		last.addStyleName("toolbarButton");
		first.addStyleName("toolbarButton");
		refresh.addStyleName("toolbarButton");
		addEnableHandler(first);
		addEnableHandler(prev);
		addEnableHandler(last);
		addEnableHandler(next);
		pagingField.setText(String.valueOf(pageSize));
		pagingField.setValue(pageSize);
		pagingField.setTitle(LocaleConstants.ACCEPT_VALUES_BETWEEN_10_TO_50);
		pageSizePrefixLabel = new Label(LocaleConstants.SHOWING);
		pageSizePrefixLabel.addStyleName("pagingPrefixLabel");
		HorizontalPanel pagingPanel = new HorizontalPanel();
		pagingPanel.addStyleName("pagingPanel");
		pagingPanel.add(pageSizePrefixLabel);
		pagingPanel.add(pagingField);
		pageSizeSuffixLabel = new Label(LocaleConstants.RECORDS_PER_PAGE);
		pagingField.setWidth(30);
		pagingPanel.add(pageSizeSuffixLabel);
		this.add(pagingPanel);
		pagingField.addStyleName("pagingTextBox");
		
		//displayText.setLabel(getMessages().displayMessage(0, 0, 0));
		beforePage.setLabel(LocaleConstants.PAGE);
		
		
		pagingField.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				Integer changedValue = event.getValue();
				
				if (changedValue != null && changedValue >= 10 && changedValue <= 50) {
					pageSize = changedValue;
					String cookieName = getCookieName(bindedGrid);
					if (!StringUtil.isNullOrEmpty(cookieName)) {
						CookieUtil.storeCookie(cookieName, String.valueOf(pageSize));
					}
					refresh();
					loader.setLimit(pageSize);
				} else {
					pagingField.setText(String.valueOf(pageSize));
				}
			}
		});

		pagingField.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				pagingField.setText(String.valueOf(pageSize));
			}
		});

		resetLabel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				PagingToolbar.this.bindedGrid.clearFilter();
			}
		});

		resetLabel.addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				resetLabel.addStyleName("labelHover");
			}
		});

		resetLabel.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				resetLabel.removeStyleName("labelHover");
			}
		});

		pageText.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				String text = pageText.getText();
				int totalPages = PagingToolbar.this.getTotalPages();
				boolean isValid = ValidationUtil.isValidNumericalValue(text, Integer.class);
				if (isValid) {
					int value = Integer.parseInt(text);
					isValid = ValidationUtil.isValueInRange(value, Math.min(1, totalPages), totalPages);
				}
				if (isValid) {
					pageText.removeStyleName("invalidField");
				} else {
					pageText.addStyleName("invalidField");
				}
			}
		});

		setToolTipSize();
		
		setWidgetIDs();
	}

	private void addEnableHandler(final TextButton button) {
		button.addEnableHandler(new EnableHandler() {

			@Override
			public void onEnable(EnableEvent event) {
				button.removeStyleName(BUTTON_DISABLE_CSS);
			}
		});

		button.addDisableHandler(new DisableHandler() {

			@Override
			public void onDisable(DisableEvent event) {
				button.addStyleName(BUTTON_DISABLE_CSS);
			}
		});

	}

	private static int getPageSize(int usersPageSize, Grid<?> bindedGrid) {
		int cookiePagingSize = -1;
		final String cookieName = getCookieName(bindedGrid);
		final String cookieValue = CookieUtil.getCookieValue(cookieName);
		if (!StringUtil.isNullOrEmpty(cookieValue)) {
			cookiePagingSize = Integer.parseInt(cookieValue);
		}
		return cookiePagingSize == -1 ? usersPageSize : cookiePagingSize;
	}

	private static final String getCookieName(final Grid<?> bindedGrid) {
		String cookieName = null;
		if (null != bindedGrid) {
			PropertyAccessModel propertyAccessModel = bindedGrid.getPropertyAccessModel();
			if (null != propertyAccessModel) {
				cookieName = StringUtil.concatenate(PAGING_COOKIE_PREFIX, propertyAccessModel.toString());
			}
		}
		return cookieName;
	}

	private void setToolTipSize() {
		this.next.getToolTip().setWidth(100);
		this.prev.getToolTip().setWidth(100);
		this.next.getToolTip().setWidth(100);
		this.last.getToolTip().setWidth(100);
		this.refresh.getToolTip().setWidth(100);
	}

	@Override
	public void bind(PagingLoader<? extends PagingLoadConfig, ?> loader) {
		super.bind(loader);
	}
	
	/**
	 * Returns the toolbar messages.
	 * 
	 * @return the messages
	 */
	public ToolBarMessage getMessages() {
		if (messages == null) {
			messages = new ToolBarMessage();
		}
		return messages;
	}

	private class ToolBarMessage extends DefaultPagingToolBarMessages {

		@Override
		public String emptyMessage() {
			return LocaleConstants.DISPLAYING_ZERO_OF_ZERO;
		}
		
//		@Override
//		public String displayMessage(int start, int end, int total) {
//			
//			return LocaleConstants.DISPLAYING;
//		}

	}

	private void setWidgetIDs(){
		WidgetUtil.setID(refresh, "gridRefresh");
		WidgetUtil.setID(next, "gridNext");
		WidgetUtil.setID(prev, "gridPrev");
		WidgetUtil.setID(displayText,"pagging-toolBar-Display");
	}
	
}
