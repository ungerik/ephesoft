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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.dcma.batch.schema.DocField.AlternateValues;
import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Field;
import com.ephesoft.dcma.batch.schema.Field.CoordinatesList;
import com.ephesoft.gxt.core.client.constant.NativeKeyCodes;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.ListWidget;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.FieldValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.constant.ValidationFailureCause;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocFieldValidationChangeEvent;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.event.FieldSelectionEvent;
import com.ephesoft.gxt.rv.client.event.PageSelectionEvent;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.core.client.impl.SchedulerImpl;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent;
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent.BeforeQueryHandler;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.tips.ToolTip;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

public class DLFSuggestionBox extends ComboBox {

	private final DocField bindedField;

	private String originalValue;

	private String originalPageIdentifier;

	private CoordinatesList originalCoordinatesList;

	private static boolean valueChanged = false;

	private String onFocusValue;

	private boolean hideTrigger;

	public DLFSuggestionBox(final DocField docField) {
		this.setWidth(100);
		this.setForceSelection(false);
		this.bindedField = docField;
		this.addStyleName("suggestionBox");
		this.setHideTrigger(true);
		this.addValueChangeHandler();
		this.addKeyDownHandler();
		this.addStyleName("dlfSuggestionBox");
		this.addFocusHandler();
		this.addSelectionHandler();
		this.onBlur();
		this.addBeforeQueryHandler(new BeforeQueryHandler<String>() {

			@Override
			public void onBeforeQuery(BeforeQueryEvent<String> event) {
				if (isHideTrigger()) {
					if (!ReviewValidateNavigator.isShowSuggestions()) {
						event.setCancelled(true);
						collapse();
					}
				}
			}
		});
		// This is done because the suggestions shown takes the size of the width mentioned here. Not from the CSS file
		this.setWidth(200);
		this.addMouseMoveHandler();
		if (null != docField) {
			this.addAlternateValue(docField.getAlternateValues());
			final String docFieldValue = docField.getValue();
			setValue(docFieldValue, true);
			if (!StringUtil.isNullOrEmpty(docFieldValue)) {
				super.add(docFieldValue);
				String fieldName = docField.getName();
				WidgetUtil.setID(this, fieldName);
				this.originalValue = docFieldValue;
				this.originalCoordinatesList = docField.getCoordinatesList();
				this.originalPageIdentifier = docField.getPage();
			}
			if (docField.isReadOnly()) {
				this.setEnabled(false);
			}
		}
	}

	@Override
	public void setHideTrigger(boolean hideTrigger) {
		this.hideTrigger = hideTrigger;
		super.setHideTrigger(hideTrigger);
	}

	public boolean isHideTrigger() {
		return hideTrigger;
	}

	@Override
	public void clear() {
		super.clear();
	}

	private void addMouseMoveHandler() {
		this.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				if (hasFocus) {
					setToolTip();
				}
			}
		}, MouseMoveEvent.getType());
	}

	private void addSelectionHandler() {
		this.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(final SelectionEvent<String> event) {
				final String selectedItem = event.getSelectedItem();
				if (null != bindedField && !StringUtil.isNullOrEmpty(selectedItem)) {
					if (selectedItem.equals(originalValue)) {
						bindedField.setCoordinatesList(originalCoordinatesList);
						bindedField.setPage(originalPageIdentifier);
					} else {
						final Field alternateValue = BatchSchemaUtil.getAlternateValue(bindedField, selectedItem);
						if (null != alternateValue) {
							final CoordinatesList clonedCoordinatesList = BatchSchemaUtil.cloneCordinates(alternateValue
									.getCoordinatesList());
							bindedField.setCoordinatesList(clonedCoordinatesList);
							bindedField.setPage(alternateValue.getPage());
						}
					}
				}
				ReviewValidateEventBus.fireEvent(new FieldSelectionEvent(bindedField));
			}
		});
	}

	private void addFocusHandler() {
		this.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(final FocusEvent event) {
				setToolTip();
				onFocusValue = getText();
				getElement().scrollIntoView();
				ReviewValidateNavigator.setLastSelectedDLFWidget(DLFSuggestionBox.this);
				ReviewValidateNavigator.setCurrentSelectedDocField(bindedField, valueChanged);
				valueChanged = false;
				if (ReviewValidateNavigator.isPageSelectionEnable()) {
					ReviewValidateEventBus.fireEvent(new PageSelectionEvent(bindedField.getPage()));
				}
				ReviewValidateEventBus.fireEvent(new FieldSelectionEvent(bindedField));
			}
		});
	}

	private void addKeyDownHandler() {
		this.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(final KeyDownEvent event) {
				bindedField.setForceReview(false);
				if (event.isControlKeyDown()) {
					switch (event.getNativeKeyCode()) {
						case KeyCodes.KEY_B:
							event.preventDefault();
							forcefullyValidateField();
							enableValidation(!enableValidation);
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
					}
				}
				performValidationChanges(isValid());
				setToolTip();
			}
		});
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

	public boolean isValid() {
		boolean isValid = true;
		if (bindedField != null) {
			isValid = (!bindedField.isForceReview()) && bindedField.getOcrConfidence() >= bindedField.getOcrConfidenceThreshold()
					&& super.isValid();
		}
		if (isValid) {
			removeStyleName("invalidField");
		} else {
			addStyleName("invalidField");
		}
		return isValid;
	}

	private void addValueChangeHandler() {
		this.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				if (null != bindedField) {
					if (hasFocus) {
						setToolTip();
					}
					final String fieldText = getText();
					String fieldValue = bindedField.getValue();
					fieldValue = fieldValue == null ? CoreCommonConstant.EMPTY_STRING : fieldValue;
					if (!fieldValue.equalsIgnoreCase(getText())) {
						bindedField.setValue(fieldText);
						final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
						ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(currentDocument));
					}
					performValidationChanges(isValid());
				}
			}
		});
	}

	public DocField getBindedField() {
		return bindedField;
	}

	public void addAlternateValue(final AlternateValues alternateValues) {
		if (null != alternateValues) {
			final List<Field> alternateValueList = alternateValues.getAlternateValue();
			this.addFields(alternateValueList);
		}
	}

	protected void addFields(final List<Field> fieldsList) {
		final ScheduledCommand command = new ScheduledCommand() {

			@Override
			public void execute() {
				addFieldList(fieldsList);
			}
		};
		final Scheduler scheduler = new SchedulerImpl();
		scheduler.scheduleDeferred(command);
	}

	private void addFieldList(final List<Field> fieldsList) {
		if (!CollectionUtil.isEmpty(fieldsList)) {
			for (final Field field : fieldsList) {
				if (null != field) {
					final String fieldValue = field.getValue();
					if (getStore().indexOf(fieldValue) == -1) {
						super.add(fieldValue);
					}
				}
			}
		}
	}

	private void onBlur() {

		this.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(final BlurEvent event) {
				if (null != bindedField) {
					String onBlurValue = getText();
					if (onBlurValue == null) {
						onBlurValue = CoreCommonConstant.EMPTY_STRING;
					}
					if (!onBlurValue.equalsIgnoreCase(onFocusValue)) {
						valueChanged = true;
					}
					bindedField.setForceReview(false);
					final ToolTip toolTip = getToolTip();
					if (null != toolTip) {
						toolTip.hide();
						removeToolTip();
					}
				}
			}
		});
	}

	private void setToolTip() {
		final ListWidget listWidget = new ListWidget();
		final List<ValidationFailureCause> causeList = getValidationFailureCause();
		removeToolTip();
		if (!CollectionUtil.isEmpty(causeList)) {
			String message = null;
			for (final ValidationFailureCause validationFailure : causeList) {
				if (null != validationFailure) {
					final String validationSeverityStyleName = validationFailure.getValidationSeverity().getSeverityStyleName();
					final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
					switch (validationFailure) {
						case FORCE_REVIEW:
							listWidget.addItem(ValidationFailureCause.getForceReviewFailureMessage(), validationSeverityStyleName);
							break;
						case REGEX_VALIDATION_FAILURE:
							final DocumentTypeDTO docType = ReviewValidateNavigator.getDocumentTypeForDocument(currentDocument);
							final FieldTypeDTO bindedFieldDTO = ReviewValidateNavigator.getField(bindedField, docType);
							final String sampleValue = bindedFieldDTO == null ? null : bindedFieldDTO.getSampleValue();
							final String description = bindedFieldDTO == null ? null : bindedFieldDTO.getDescription();
							message = ValidationFailureCause.getRegexFailureMessage(description, sampleValue);
							listWidget.addItem(message, validationSeverityStyleName);
							break;
						case OCR_CONFIDENCE_FAILURE:
							message = ValidationFailureCause.getOCRConfidenceFailureMessage(bindedField.getOcrConfidence(),
									bindedField.getOcrConfidenceThreshold());
							listWidget.addItem(message, validationSeverityStyleName);
							break;
						case DOCUMENT_VALIDATION_FAILURE:
							message = ValidationFailureCause.getDocumentFailureMessage(currentDocument);
							listWidget.addItem(message, validationSeverityStyleName);
							break;
						case USER_ADDED_MESSAGE:
							message = bindedField.getMessage();
							listWidget.addItem(message, validationSeverityStyleName);
							break;
					}
				}
			}
			showToolTip(listWidget.getElement().getInnerHTML());
		}
	}

	public void removeToolTip() {
		final ToolTip tooltip = this.getToolTip();
		if (null != tooltip) {
			tooltip.hide();
			super.removeToolTip();
		}

	}

	public void setFieldValueOptions(final String fieldValueOptions) {
		if (!StringUtil.isNullOrEmpty(fieldValueOptions)) {
			this.setEditable(false);
			this.setHideTrigger(false);
			String[] optionsArray = fieldValueOptions.split(CoreCommonConstant.SEMI_COLON);
			Set<String> set = new HashSet<String>();
			for (String option : optionsArray) {
				if (!set.contains(option)) {
					this.add(option);
					set.add(option);
				}
			}
		}
	}

	public void showToolTip(final String html) {
		final ToolTipConfig config = new ToolTipConfig();
		config.setBodyHtml(html);
		this.setToolTipConfig(config);
		final ToolTip toolTip = this.getToolTip();
		toolTip.getElement().addClassName("errorMessageToolTip");
		toolTip.showAt(getAbsoluteLeft() + getOffsetWidth() + CoreCommonConstant.TOOLTIP_MARGIN, getAbsoluteTop() + getOffsetHeight()
				+ CoreCommonConstant.TOOLTIP_MARGIN);
	}

	private List<ValidationFailureCause> getValidationFailureCause() {
		List<ValidationFailureCause> failureList = null;
		if (null != bindedField) {
			failureList = new ArrayList<ValidationFailureCause>();

			Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			if (null != currentDocument) {
				String errorMessage = currentDocument.getErrorMessage();
				if (!StringUtil.isNullOrEmpty(errorMessage)) {
					failureList.add(ValidationFailureCause.DOCUMENT_VALIDATION_FAILURE);
				}
			}
			if (bindedField.isForceReview()) {
				failureList.add(ValidationFailureCause.FORCE_REVIEW);
			}

			if (!super.isValid()) {
				failureList.add(ValidationFailureCause.REGEX_VALIDATION_FAILURE);
			}

			if (bindedField.getOcrConfidence() < bindedField.getOcrConfidenceThreshold()) {
				failureList.add(ValidationFailureCause.OCR_CONFIDENCE_FAILURE);
			}
			if (bindedField.getMessage() != null) {
				failureList.add(ValidationFailureCause.USER_ADDED_MESSAGE);
			}
		}
		return failureList;
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

	private void performValidationChanges(final boolean fieldValid) {
		ReviewValidateEventBus.fireEvent(new DocFieldValidationChangeEvent(bindedField));
		if (fieldValid) {
			removeStyleName("invalidField");
		} else {
			addStyleName("invalidField");
		}
	}

	@Override
	public void appendValue(final String value, final boolean append) {
		final String previousValue = this.getText();
		int trailingStringLength = 0;
		String newValue = value;
		if (!StringUtil.isNullOrEmpty(previousValue) && append) {
			newValue = StringUtil.concatenate(previousValue, ReviewValidateNavigator.getIndexFieldSeparator(), value);
			trailingStringLength = previousValue.length() + 1;
		}
		if (null != value) {
			this.finishEditing();
			this.setValue(newValue, true, true);
			this.select(trailingStringLength, value.length());
		}
	}

	/**
	 * @return the valueChanged
	 */
	public static boolean isValueChanged() {
		return valueChanged;
	}

}
