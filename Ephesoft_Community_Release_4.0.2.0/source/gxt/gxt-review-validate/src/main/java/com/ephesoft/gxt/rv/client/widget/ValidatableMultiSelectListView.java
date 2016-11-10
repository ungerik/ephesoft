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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.gxt.core.client.constant.NativeKeyCodes;
import com.ephesoft.gxt.core.client.ui.widget.property.Validatable;
import com.ephesoft.gxt.core.client.ui.widget.util.UniqueIDProvider;
import com.ephesoft.gxt.core.client.validator.FieldValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocFieldValidationChangeEvent;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.event.FieldSelectionChangeEvent;
import com.ephesoft.gxt.rv.client.event.PageSelectionEvent;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Timer;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.theme.base.client.listview.ListViewDefaultAppearance;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;

public class ValidatableMultiSelectListView extends ListView<String, String> implements Validatable {

	private DocField bindedField;

	protected List<FieldValidator<String>> validatorList;

	protected boolean enableValidation;

	private boolean validationRulesORing;

	private String valueOnFocus;

	public ValidatableMultiSelectListView(DocField docField) {
		super(CollectionUtil.createListStore(new UniqueIDProvider<String>()), new IdentityValueProvider<String>(),
				new ListViewDefaultAppearance<String>());
		enableValidation = true;
		this.bindedField = docField;
		this.setPixelSize(150, 100);
		this.addSelectionHandler();
		this.addStyleName("listView");
		this.addKeyDownHandler();
		addFocusHandler();
		performValidationChanges(isValid());
		this.addClickHandler();
		
		// EPHE-8996 - Priority Issue: Sticky Fields not working in 4.0.2.0
		// Setting field read Only state based on value.
		if (null != docField && docField.isReadOnly()) {
			this.setEnabled(false);
		}
	}

	private void addClickHandler() {
		this.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				focus();
			}
		}, ClickEvent.getType());
	}

	public void setValues(String value) {
		if (!StringUtil.isNullOrEmpty(value)) {
			final String values[] = value.split(CoreCommonConstant.SEMI_COLON);
			Timer timer = new Timer() {

				@Override
				public void run() {
					getSelectionModel().select(false, values);
				}
			};
			timer.schedule(100);
		}
	}

	private void addSelectionHandler() {
		this.getSelectionModel().addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(SelectionEvent<String> event) {
				if (null != event) {
					List<String> selectedItem = getSelectionModel().getSelectedItems();
					if (!CollectionUtil.isEmpty(selectedItem)) {
						String value = StringUtil.concatenateUsingSeperator(CoreCommonConstant.SEMI_COLON, selectedItem.toArray());
						bindedField.setValue(value);
					} else {
						bindedField.setValue(CoreCommonConstant.EMPTY_STRING);
					}
					performValidationChanges(isValid());
					ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(ReviewValidateNavigator.getCurrentDocument()));
				}
			}
		});
	}

	public void addValues(final List<String> listOfValues) {
		if (!CollectionUtil.isEmpty(listOfValues)) {
			Set<String> valuesSet = new HashSet<String>();
			valuesSet.addAll(listOfValues);
			store.addAll(valuesSet);
		}
	}

	public void addFieldValueOptions(final String fieldValueOptions) {
		if (!StringUtil.isNullOrEmpty(fieldValueOptions)) {
			String values[] = fieldValueOptions.split(CoreCommonConstant.SEMI_COLON);
			this.addValues(Arrays.asList(values));
		}
	}

	@Override
	public boolean isValid() {
		boolean isValid = true;
		if (bindedField != null) {
			isValid = (!bindedField.isForceReview()) && bindedField.getOcrConfidence() >= bindedField.getOcrConfidenceThreshold()
					&& this.isValidPattern();
		}
		return isValid;
	}

	public boolean isValidPattern() {
		boolean isValid = true;
		final String value = bindedField.getValue();
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

	@Override
	public void enableValidation(boolean enable) {
		this.enableValidation = enable;
	}

	@Override
	public void appendValue(String value, boolean append) {

	}

	@Override
	public void focus() {
		Timer timer = new Timer() {

			@Override
			public void run() {
				ValidatableMultiSelectListView.super.focus();
			}
		};
		timer.schedule(50);
	}

	@Override
	public void blur() {
		super.blur();
	}

	private void addKeyDownHandler() {
		this.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.isControlKeyDown()) {
					switch (event.getNativeKeyCode()) {
						case KeyCodes.KEY_B:
							event.preventDefault();
							forcefullyValidateField();
							enableValidation(!enableValidation);
							performValidationChanges(isValid());
							break;

						case NativeKeyCodes.OPENING_SQUARE_BRACE:
							event.preventDefault();
							if (null != bindedField) {
								boolean enableScript = !bindedField.isFieldValueChangeScript();
								bindedField.setFieldValueChangeScript(enableScript);
								final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
								ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
							}
							break;

						case KeyCodes.KEY_U:
							event.preventDefault();
							getSelectionModel().deselectAll();
							break;
					}
				}
			}
		}, KeyDownEvent.getType());
	}

	private void forcefullyValidateField() {
		if (null != bindedField) {
			final float ocrConfidence = bindedField.getOcrConfidence();
			final float ocrConfidenceThreshold = bindedField.getOcrConfidenceThreshold();
			if (ocrConfidence < ocrConfidenceThreshold) {
				bindedField.setOcrConfidence(100.0f);
			}
		}
	}

	private void performValidationChanges(final boolean fieldValid) {
		ReviewValidateEventBus.fireEvent(new DocFieldValidationChangeEvent(bindedField));
		if (fieldValid) {
			this.removeStyleName("invalidField");
		} else {
			this.addStyleName("invalidField");
		}
	}

	private void addFocusHandler() {
		this.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				addStyleName("listBoxFocus");
				if (null != valueOnFocus) {
					valueOnFocus = bindedField.getValue();
				}
				if (ReviewValidateNavigator.isPageSelectionEnable()) {
					ReviewValidateEventBus.fireEvent(new PageSelectionEvent(bindedField.getPage()));
				}
				Validatable lastSelectedWidget = ReviewValidateNavigator.getLastSelectedWidget();
				boolean valueChanged = false;
				if (ReviewValidateNavigator.getCurrentSelectedDocField() != null && lastSelectedWidget != null) {
					if (lastSelectedWidget instanceof DLFSuggestionBox) {
						valueChanged = DLFSuggestionBox.isValueChanged();
					}
				}

				ReviewValidateNavigator.setCurrentSelectedDocField(bindedField, valueChanged);
			}
		});

		this.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				removeStyleName("listBoxFocus");
				String valueOnBlur = bindedField.getValue();
				if (null != valueOnBlur && !valueOnBlur.equalsIgnoreCase(valueOnFocus)) {
					ReviewValidateEventBus.fireEvent(new FieldSelectionChangeEvent(bindedField));
				}
			}
		});
	}

	public void addValidator(final FieldValidator<String> validatorToAdd) {
		if (null != validatorToAdd) {
			if (validatorList == null) {
				validatorList = new ArrayList<FieldValidator<String>>();
			}
			validatorList.add(validatorToAdd);
			performValidationChanges(isValid());
		}
	}

	public void setValidationRulesORing(boolean validationRulesORing) {
		this.validationRulesORing = validationRulesORing;
	}
}
