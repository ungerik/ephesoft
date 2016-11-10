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

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.cell.core.client.form.StringComboBoxCell;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;

public class MultiSelectComboBox extends SimpleComboBox<String> {

	private static final String SEPARATOR = ";";

	public MultiSelectComboBox(final List<String> optionsList) {
		super(new ComboBoxCell(optionsList));
		this.setTriggerAction(TriggerAction.ALL);
		this.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					MultiSelectComboBox.this.finishEditing();
				}
			}
		});
	}

	public static class ComboBoxCell extends StringComboBoxCell {

		private static final String MULTISELECT_WINDOW_CSS = "multiselectWindow";
		private Window window = new Window();
		private boolean focusLost = false;
		private List<String> optionsList;
		private List<MultiSelectOptionsCheckBox> checkBoxList;
		private VerticalLayoutContainer panel;
		private int currentIndex = 0;
		private MultiSelectOptionsCheckBox lastSelectedBox;

		public ComboBoxCell(final List<String> optionsList) {
			super(getDefaultListStore(), getDefaultLabelProvider());
			panel = new VerticalLayoutContainer();
			this.setTriggerAction(TriggerAction.ALL);
			this.optionsList = optionsList;
			this.checkBoxList = new ArrayList<MultiSelectComboBox.ComboBoxCell.MultiSelectOptionsCheckBox>();
			store.add("1");
			window.add(panel);
			window.setPixelSize(-1, -1);
			window.addDomHandler(new MouseDownHandler() {

				@Override
				public void onMouseDown(MouseDownEvent event) {
					focusLost = true;
				}
			}, MouseDownEvent.getType());
		}

		@Override
		public void expand(final com.google.gwt.cell.client.Cell.Context context, final XElement parent,
				final ValueUpdater<String> updater, final String value) {
			super.expand(context, parent, updater, value);
			window.setWidth(width);
			window.setHeaderVisible(false);
			window.setHeight(height);
			window.getElement().makePositionable(true);
			window.getElement().updateZIndex(1);
			window.setResizable(false);
			window.addStyleName(MULTISELECT_WINDOW_CSS);
			panel.addStyleName("multiselectOptionsPanel");
			setSelectionParameter(context, parent);
			currentIndex = 0;
			RootPanel.get().add(window);
		}

		private void setSelectionParameter(final com.google.gwt.cell.client.Cell.Context context, final XElement parent) {
			panel.clear();
			checkBoxList.clear();
			if (!CollectionUtil.isEmpty(optionsList)) {
				for (final String option : optionsList) {
					if (!StringUtil.isNullOrEmpty(option)) {
						MultiSelectOptionsCheckBox checkBox = new MultiSelectOptionsCheckBox(option, context, parent);
						checkBox.setTitle(option);
						checkBoxList.add(checkBox);
						panel.add(checkBox, new VerticalLayoutData(0.999, 15));
					}
				}
				MultiSelectOptionsCheckBox firstCheckBox = checkBoxList.get(0);
				firstCheckBox.addStyleName("hoveredMultiSelectOption");
				lastSelectedBox = firstCheckBox;
			}
		}

		@Override
		protected void restrict(final XElement parent) {
			super.restrict(parent);
			final XElement wrapper = parent.getFirstChildElement().cast();
			window.getElement().alignTo(wrapper, new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT, true), 0, 0);
		}

		@Override
		protected void onBlur(final com.google.gwt.cell.client.Cell.Context context, final XElement parent, final String value,
				final NativeEvent event, final ValueUpdater<String> valueUpdater) {
			final Timer timer = new Timer() {

				@Override
				public void run() {
					if (!focusLost) {
						ComboBoxCell.super.onBlur(context, parent, value, event, valueUpdater);
					} else {
						Timer newTimer = new Timer() {

							@Override
							public void run() {
								focusLost = false;
							}

						};
						newTimer.schedule(190);
					}
				}
			};
			timer.schedule(100);
		}

		@Override
		protected void onKeyDown(com.google.gwt.cell.client.Cell.Context context, Element parent, String value, NativeEvent event,
				ValueUpdater<String> valueUpdater) {
			super.onKeyDown(context, parent, value, event, valueUpdater);
			int keyCode = event.getKeyCode();
			if (keyCode == KeyCodes.KEY_DOWN || keyCode == KeyCodes.KEY_UP) {
				if (keyCode == KeyCodes.KEY_DOWN) {
					int newIndex = currentIndex + 1;
					currentIndex = newIndex < checkBoxList.size() ? newIndex : currentIndex;
				}
				if (keyCode == KeyCodes.KEY_UP) {
					int newIndex = currentIndex - 1;
					currentIndex = newIndex >= 0 ? newIndex : currentIndex;
				}
				if (null != lastSelectedBox) {
					lastSelectedBox.removeStyleName("hoveredMultiSelectOption");
				}
				lastSelectedBox = checkBoxList.get(currentIndex);
				lastSelectedBox.addStyleName("hoveredMultiSelectOption");
				lastSelectedBox.getElement().scrollIntoView();
			}
			if (keyCode == KeyCodes.KEY_SPACE && null != lastSelectedBox && window.isAttached()) {
				lastSelectedBox.setValue(!lastSelectedBox.getValue(), true);
			}

		}

		@Override
		protected void onSelect(String item) {
			collapse(lastContext, lastParent);
		}

		@Override
		public void finishEditing(Element parent, String value, Object key, ValueUpdater<String> valueUpdater) {
			if (!focusLost) {
				super.finishEditing(parent, value, key, valueUpdater);
			}
		}

		@Override
		public void collapse(final com.google.gwt.cell.client.Cell.Context context, final XElement parent) {
			final Timer timer = new Timer() {

				@Override
				public void run() {
					if (!focusLost) {
						if (null != window) {
							RootPanel.get().remove(window);
						}
						ComboBoxCell.super.collapse(context, parent);
					}
				}
			};

			timer.schedule(150);
		}

		private void setText(final XElement parent) {
			InputElement inputElement = getInputElement(parent);
			if (null != inputElement) {
				int size = checkBoxList.size();
				List<String> selectedStringList = new ArrayList<String>();
				for (int i = 0; i < size; i++) {
					if (checkBoxList.get(i).getValue()) {
						selectedStringList.add(optionsList.get(i));
					}
				}
				String rolesValue = StringUtil.concatenateUsingSeperator(SEPARATOR, selectedStringList.toArray());
				setText(parent, rolesValue);
			}

		}

		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, String value) {
			int size = checkBoxList.size();
			List<String> selectedStringList = new ArrayList<String>();
			for (int i = 0; i < size; i++) {
				if (checkBoxList.get(i).getValue()) {
					selectedStringList.add(optionsList.get(i));
				}
			}
			String rolesValue = StringUtil.concatenateUsingSeperator(SEPARATOR, selectedStringList.toArray());
			super.setValue(context, parent, rolesValue);
		}

		private class MultiSelectOptionsCheckBox extends CheckBox {

			public MultiSelectOptionsCheckBox(final String label, final Context context, final XElement parent) {

				super(StringUtil.isNullOrEmpty(label) ? label : (label.length() > 9 ? label.substring(0, 8) : label));
				String[] textArray = ComboBoxCell.this.getText(parent).split(SEPARATOR);
				for (String text : textArray) {
					if (text.equalsIgnoreCase(label)) {
						this.setValue(true);
						break;
					}
				}
				this.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(final ClickEvent event) {
						focusLost = true;
						getInputElement(parent).focus();
						currentIndex = checkBoxList.indexOf(MultiSelectOptionsCheckBox.this);
						if (null != lastSelectedBox) {
							lastSelectedBox.removeStyleName("hoveredMultiSelectOption");
						}
						lastSelectedBox = checkBoxList.get(currentIndex);
						lastSelectedBox.addStyleName("hoveredMultiSelectOption");
						lastSelectedBox.getElement().scrollIntoView();
					}
				});

				this.addMouseOverHandler(new MouseOverHandler() {

					@Override
					public void onMouseOver(MouseOverEvent event) {
						currentIndex = checkBoxList.indexOf(MultiSelectOptionsCheckBox.this);
						if (null != lastSelectedBox) {
							lastSelectedBox.removeStyleName("hoveredMultiSelectOption");
						}
						lastSelectedBox = checkBoxList.get(currentIndex);
						lastSelectedBox.addStyleName("hoveredMultiSelectOption");
						lastSelectedBox.getElement().scrollIntoView();
					}
				});

				this.addMouseDownHandler(new MouseDownHandler() {

					@Override
					public void onMouseDown(MouseDownEvent event) {
						focusLost = true;
						getInputElement(parent).focus();
					};
				});

				this.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						ComboBoxCell.this.setText(parent);
						getInputElement(parent).focus();
					}
				});
			}
		}

		private static ListStore<String> getDefaultListStore() {
			return new ListStore<String>(new ModelKeyProvider<String>() {

				@Override
				public String getKey(final String item) {
					return item;
				}
			});
		}

		private static LabelProvider<String> getDefaultLabelProvider() {

			return new LabelProvider<String>() {

				@Override
				public String getLabel(final String item) {
					return item;
				}
			};
		}
	}
}
