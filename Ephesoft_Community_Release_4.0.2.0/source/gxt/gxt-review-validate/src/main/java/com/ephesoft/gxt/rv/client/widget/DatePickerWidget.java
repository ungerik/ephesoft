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

import java.util.Arrays;
import java.util.Date;

import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.gxt.core.client.ui.widget.property.Validatable;
import com.ephesoft.gxt.core.client.ui.widget.util.UniqueIDProvider;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleConstant;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.DatePicker;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class DatePickerWidget extends HorizontalPanel implements Validatable {

	public static final String WINDOW_SIZE_STYLE_CSS = "windowSizeStyle";
	private DLFSuggestionBox suggestionBox;
	private Label button;
	private VerticalLayoutContainer verticalLayoutContainer;
	private static Window window;
	private DatePicker datePicker;
	private ComboBox<PredefinedFormat> dateFormatter;

	public DatePickerWidget(DocField bindedDocField) {
		initialize(bindedDocField);

		if (null == window) {
			window = new Window();
			window.setAutoHide(true);
			window.setShadow(false);
			window.setResizable(false);
		}
		window.getHeader().setHeight(10);
		this.add(suggestionBox);
		this.add(verticalLayoutContainer);
		this.addStyleName("datePickerStyle");

		window.addBeforeShowHandler(new BeforeShowHandler() {

			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				window.addStyleName(WINDOW_SIZE_STYLE_CSS);
			}
		});
		// this.addDomHandler(mouseOutHandler, MouseOutEvent.getType());
	}

	private void initialize(DocField docField) {
		suggestionBox = new DLFSuggestionBox(docField);
		button = new Label();
		verticalLayoutContainer = new VerticalLayoutContainer();
		datePicker = new DatePicker();
		verticalLayoutContainer.add(button);
		datePicker.addValueChangeHandler(valueChangeHandler);
		button.addClickHandler(clickHandler);
		button.addStyleName("calendar");
		verticalLayoutContainer.addStyleName("layoutContainerStyle");
		ListStore<PredefinedFormat> formatStore = CollectionUtil.createListStore(new UniqueIDProvider<PredefinedFormat>());
		dateFormatter = new ComboBox<DateTimeFormat.PredefinedFormat>(formatStore, new LabelProvider<PredefinedFormat>() {

			@Override
			public String getLabel(PredefinedFormat item) {
				String valueToDisplay = CoreCommonConstant.EMPTY_STRING;
				switch (item) {
					case DATE_FULL:
						valueToDisplay = LocaleConstant.DATE_FULL_TEXT;
						break;
					case DATE_SHORT:
						valueToDisplay = LocaleConstant.DATE_SHORT_TEXT;
						break;
					case DATE_LONG:
						valueToDisplay = LocaleConstant.DATE_LONG_TEXT;
						break;
					case DATE_MEDIUM:
						valueToDisplay = LocaleConstant.DATE_MEDIUM_TEXT;
						break;
					default:
				}
				return valueToDisplay;
			}
		});
		formatStore.add(PredefinedFormat.DATE_FULL);
		formatStore.add(PredefinedFormat.DATE_SHORT);
		formatStore.add(PredefinedFormat.DATE_LONG);
		formatStore.add(PredefinedFormat.DATE_MEDIUM);
		dateFormatter.setText(LocaleConstant.DATE_SHORT_TEXT);
		dateFormatter.setValue(PredefinedFormat.DATE_SHORT);
		dateFormatter.setEditable(false);
		dateFormatter.setTriggerAction(TriggerAction.ALL);
		dateFormatter.finishEditing();
		addSelectionHandler();
	}

	private void addSelectionHandler() {
		dateFormatter.addSelectionHandler(new SelectionHandler<DateTimeFormat.PredefinedFormat>() {

			@Override
			public void onSelection(SelectionEvent<PredefinedFormat> event) {
				Timer timer = new Timer() {

					@Override
					public void run() {
						if (null != window) {
							window.show();
						}
					}

				};
				timer.schedule(150);
			};
		});
	}

	ValueChangeHandler<Date> valueChangeHandler = new ValueChangeHandler<Date>() {

		@Override
		public void onValueChange(ValueChangeEvent<Date> event) {

			Date d = event.getValue();
			DateTimeFormat f = DateTimeFormat.getFormat(dateFormatter.getValue());
			suggestionBox.setText(f.format(d));
			suggestionBox.setValue(f.format(d));
			suggestionBox.focus();
			suggestionBox.finishEditing();
			clearPopUp();
		}

		private void clearPopUp() {
			window.clear();
			window.hide();
		}
	};

	ClickHandler clickHandler = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			window.hide();
			window.clear();
			VerticalPanel datePickerContainer = new VerticalPanel();
			datePickerContainer.add(dateFormatter);
			if (null != datePicker) {
				datePickerContainer.add(datePicker);
			}
			window.add(datePickerContainer);
			window.show();
			int top = button.getAbsoluteTop() + button.getOffsetHeight() - window.getOffsetHeight();
			int left = button.getAbsoluteLeft() + button.getOffsetWidth();
			window.setPosition(left, top);
			window.getElement().alignTo(datePicker.getElement(), new AnchorAlignment(Anchor.LEFT), 10, 10);
			// window.setSize("100%", "100%");
		}
	};

	public String getDateValue() {
		String textBoxValue = suggestionBox.getText();
		return textBoxValue;
	}

	public Date getDate() {
		return datePicker.getValue();
	}

	public void setDate(Date date) {
		if (null != date && !date.equals("")) {
			DateTimeFormat f = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
			suggestionBox.setText(f.format(date));
			datePicker.setValue(date);
		} else {
			suggestionBox.setText("");
		}

	}

	public Label getButton() {
		return button;
	}

	/**
	 * @return the textField
	 */
	public DLFSuggestionBox getSuggestionBox() {
		return suggestionBox;
	}

	public void addStyleToPopup(String styleName) {
		window.addStyleName(styleName);
	}

	public void disableDateWidgetControls(boolean isEditable) {
		suggestionBox.setEnabled(!isEditable);
		if (!isEditable) {
			button.addStyleName("calendar");
		} else {
			button.removeStyleName("calendar");
			suggestionBox.setReadOnly(Boolean.TRUE);
		}
	}

	@Override
	public boolean isValid() {
		return suggestionBox.isValid();
	}

	@Override
	public void enableValidation(boolean enable) {
		suggestionBox.enableValidation(enable);

	}

	@Override
	public void appendValue(String value, boolean append) {
		suggestionBox.appendValue(value, append);
	}

	@Override
	public void focus() {
		suggestionBox.focus();
	}

	@Override
	public void blur() {
		suggestionBox.blur();
	}
}
