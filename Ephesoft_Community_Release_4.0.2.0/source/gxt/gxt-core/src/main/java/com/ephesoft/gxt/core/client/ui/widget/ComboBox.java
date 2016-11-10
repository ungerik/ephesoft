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

import java.util.List;

import com.ephesoft.gxt.core.client.ui.widget.property.Validatable;
import com.ephesoft.gxt.core.client.validator.FieldValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.cell.core.client.form.StringComboBoxCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

public class ComboBox extends SimpleComboBox<String> implements Validatable {

	interface ComboBoxTemplates extends XTemplates {

		@XTemplate("<div qtip=\"{toolTipTitle}\" qtitle=>{toolTipTitle}</div>")
		SafeHtml toolTip(String toolTipTitle);

	}

	protected List<FieldValidator<String>> validatorList;

	protected boolean enableValidation;

	private boolean validationRulesORing;
	
	private boolean autoCommit;

	public ComboBox() {
		super(new ComboBoxCell());
		this.addKeyUpHandler();
		this.addSelectionHandler();
		this.setTriggerAction(TriggerAction.ALL);
		this.enableValidation = true;
		autoCommit = true;
		this.getListView().setId("drop-down");
	}

	private void addSelectionHandler() {
		this.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(final SelectionEvent<String> event) {
				final String itemText = event.getSelectedItem();
				setValue(itemText, true, true);
				focus();
			}
		});
	}
	
	private void addKeyUpHandler() {
		this.addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(final KeyUpEvent event) {
				if (getText() != getValue() && isAutoCommit()) {
					setValue(getText());
				}
			}
		});
	}
	
	

	
	public boolean isAutoCommit() {
		return autoCommit;
	}

	
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	@Override
	public void setValue(final String value) {
		super.setValue(value, true, false);
	}

	@Override
	public void enableValidation(final boolean enable) {
		this.enableValidation = enable;
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		final String value = getText();
		if (enableValidation) {
			if (!CollectionUtil.isEmpty(validatorList)) {
				for (final FieldValidator<String> validator : validatorList) {
					if (null != validator) {
						isValid = validator.validate(value);
						if (!isValid && !validationRulesORing) {
							break;
						}
						if (isValid && validationRulesORing) {
							break;
						}
					}
				}
			}
		}
		return isValid;
	}

	public boolean isValidationRulesORing() {
		return validationRulesORing;
	}

	public void setValidationRulesORing(boolean validationRulesORing) {
		this.validationRulesORing = validationRulesORing;
	}

	@Override
	public void focus() {
		Timer timer = new Timer() {

			@Override
			public void run() {
				scheduleFocus();
			}
		};
		timer.schedule(50);
	}

	private void scheduleFocus() {
		super.focus();
	}

	@Override
	protected void onFocus(Event event) {
		super.onFocus(event);
	}

	@Override
	public void selectAll() {
		
	}
	
	public void selectText() {
		super.selectAll();
	}

	
	@Override
	public void appendValue(final String value, final boolean append) {
		final String previousValue = this.getText();
		int trailingStringLength = 0;
		String newValue = value;
		if (!StringUtil.isNullOrEmpty(previousValue) && append) {
			newValue = StringUtil.concatenate(previousValue, " ", value);
			trailingStringLength = previousValue.length() + 1;
		}
		if (null != value) {
			this.finishEditing();
			this.setValue(newValue, true, true);
			this.select(trailingStringLength, value.length());
		}
	}

	public static class ComboBoxCell extends StringComboBoxCell {

		private static ComboBoxCell queryCellSelector;

		private String lastQuery = null;

		public ComboBoxCell() {
			super(getDefaultListStore(), getDefaultLabelProvider());
			this.setTypeAheadDelay(10);
		}

		@Override
		protected void onFocus(com.google.gwt.cell.client.Cell.Context context, XElement parent, String value, NativeEvent event,
				ValueUpdater<String> valueUpdater) {
			super.onFocus(context, parent, value, event, valueUpdater);
		}

		@Override
		protected void onClick(XElement parent, NativeEvent event) {
			queryCellSelector = this;
			super.onClick(parent, event);
		}

		@Override
		protected void onClick(com.google.gwt.cell.client.Cell.Context context, XElement parent, NativeEvent event, String value,
				ValueUpdater<String> updater) {
			queryCellSelector = this;
			super.onClick(context, parent, event, value, updater);
			doQuery(context, parent, updater, value, CoreCommonConstant.EMPTY_STRING, true);
		}

		@Override
		protected void onBlur(com.google.gwt.cell.client.Cell.Context context, XElement parent, String value, NativeEvent event,
				ValueUpdater<String> valueUpdater) {
			queryCellSelector = null;
			super.onBlur(context, parent, value, event, valueUpdater);
		}

		@Override
		protected void onKeyDown(com.google.gwt.cell.client.Cell.Context context, Element parent, String value, NativeEvent event,
				ValueUpdater<String> valueUpdater) {
			if (null != event) {
				boolean isCtrlKey = event.getCtrlKey();
				if (isCtrlKey) {
					queryCellSelector = null;
					collapse(context, lastParent);
				} else {
					if (event.getKeyCode() == KeyCodes.KEY_DOWN) {
						queryCellSelector = this;
					}
				}
				super.onKeyDown(context, parent, value, event, valueUpdater);
			}
		}

		@Override
		protected void onKeyUp(com.google.gwt.cell.client.Cell.Context context, Element parent, String value, NativeEvent event,
				ValueUpdater<String> valueUpdater) {
			super.onKeyUp(context, parent, value, event, valueUpdater);
		}

		@Override
		protected void onTriggerClick(com.google.gwt.cell.client.Cell.Context context, XElement parent, NativeEvent event,
				String value, ValueUpdater<String> updater) {
			super.onTriggerClick(context, parent, event, value, updater);
		}

		private static ListStore<String> getDefaultListStore() {
			return new ListStore<String>(new ModelKeyProvider<String>() {

				@Override
				public String getKey(String item) {
					return item;
				}
			});
		}

		@Override
		public void selectAll(XElement parent) {
			super.selectAll(parent);
		}

		@Override
		protected void onKeyUp(com.google.gwt.cell.client.Cell.Context context, XElement parent, String value, NativeEvent event,
				ValueUpdater<String> valueUpdater) {
			super.onKeyUp(context, parent, value, event, valueUpdater);
		}

		@Override
		public void doQuery(com.google.gwt.cell.client.Cell.Context context, XElement parent, ValueUpdater<String> updater,
				String value, String query, boolean force) {
			if (queryCellSelector == this) {
				lastQuery = query;
				super.doQuery(context, parent, updater, value, query, force);
			} else {
				queryCellSelector = this;
			}
		}

		@Override
		protected void onNavigationKey(com.google.gwt.cell.client.Cell.Context context, Element parent, String value,
				NativeEvent event, ValueUpdater<String> valueUpdater) {
			int nativeKeyCode = event.getKeyCode();
			boolean shiftKeyDown = event.getShiftKey();
			if (!(shiftKeyDown)) {
				super.onNavigationKey(context, parent, value, event, valueUpdater);
			} else {
				if (!(nativeKeyCode == KeyCodes.KEY_DOWN || nativeKeyCode == KeyCodes.KEY_UP)) {
					super.onNavigationKey(context, parent, value, event, valueUpdater);
				}
			}
		}

		private static LabelProvider<String> getDefaultLabelProvider() {
			return new LabelProvider<String>() {

				@Override
				public String getLabel(String item) {
					return item;
				}
			};
		}

		@Override
		protected boolean itemMatchesQuery(String item, String query) {
			boolean matches = false;
			String value = getRenderedValue(item);
			if (value != null) {
				matches = value.toLowerCase().contains(query.toLowerCase());
			}
			return matches;
		}

		private String getRenderedValue(String item) {
			return getPropertyEditor().render(item);
		}

		@Override
		protected void initView(ListView<String, ?> listView) {
			ListView<String, String> listView1 = new ListView<String, String>(store, new IdentityValueProvider<String>());
			listView1.setCell(new SimpleSafeHtmlCell<String>(new SafeHtmlRenderer<String>() {

				final ComboBoxTemplates comboBoxTemplates = GWT.create(ComboBoxTemplates.class);

				@Override
				public SafeHtml render(final String object) {
					return comboBoxTemplates.toolTip(object);
				}

				@Override
				public void render(final String object, SafeHtmlBuilder builder) {
					if (null == builder) {
						builder = new SafeHtmlBuilder();
					}
					builder.append(comboBoxTemplates.toolTip(object));
				}
			}));

			super.initView(listView1);
		}
	}

	@Override
	public void onLoad() {
		super.onLoad();
	}

	public void blur() {
		super.blur();
	}
}
