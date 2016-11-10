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

import java.util.Arrays;
import java.util.List;

import com.ephesoft.dcma.batch.schema.HocrPages.HocrPage.Spans.Span;
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionInputPanelPresenter;
import com.ephesoft.gxt.admin.client.util.KVExtractionUtil;
import com.ephesoft.gxt.admin.client.view.regexPool.RegexGroupSelectionGridView;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexBuilderView;
import com.ephesoft.gxt.admin.client.widget.RegexComboBox;
import com.ephesoft.gxt.admin.client.widget.RegexComboBoxHandler;
import com.ephesoft.gxt.core.client.constant.CssConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.MandatoryLabel;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class AdvancedKVExtractionInputPanelView extends AdvancedKVExtractionInlineView<AdvancedKVExtractionInputPanelPresenter> {

	interface Binder extends UiBinder<Widget, AdvancedKVExtractionInputPanelView> {
	}

	@UiField
	protected FramedPanel advKVFramedPanel;
	@UiField
	protected FieldLabel useExistingKeyLabel;
	@UiField
	protected CheckBox useExistingKey;
	@UiField
	protected MandatoryLabel keyPatternLabel;
	@UiField
	protected HorizontalPanel keyPatternPanel;
	protected ComboBox keyPatternComboBox;
	// protected TextField keyPatternText;
	protected RegexComboBox keyPatternText;
	@UiField
	protected MandatoryLabel valuePatternLabel;
	@UiField
	// protected TextField valuePattern;
	protected RegexComboBox valuePattern;
	@UiField
	protected FieldLabel keyFuzzinessLabel;
	@UiField
	protected ComboBox keyFuzzinessComboBox;
	@UiField
	protected FieldLabel fetchValueLabel;
	@UiField
	protected ComboBox fetchValue;
	@UiField
	protected FieldLabel kvPageValueLabel;
	@UiField
	protected ComboBox kvPageValue;
	@UiField
	protected FieldLabel extractZoneLabel;
	@UiField
	protected ComboBox extractZoneComboBox;
	@UiField
	protected MandatoryLabel weightLabel;
	@UiField
	protected TextField weightText;
	private List<Span> spanList;

	@UiField
	protected VerticalLayoutContainer advKVInputViewPanel;

	private static final Binder BINDER = GWT.create(Binder.class);

	public AdvancedKVExtractionInputPanelView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		applyCSS();
		keyFuzzinessComboBox.setEditable(false);
		fetchValue.setEditable(false);
		kvPageValue.setEditable(false);
		extractZoneComboBox.setEditable(false);
		keyPatternComboBox.setAutoCommit(false);
		addRegexValidator(keyPatternText);
		addRegexValidator(valuePattern);
		// addListeners();
	}

	private void addRegexValidator(final RegexComboBox comboBox) {
		comboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				presenter.validateRegexField(comboBox);
			}
		});
	}

	private void applyCSS() {
		advKVFramedPanel.addStyleName("advFramedPanel");
		advKVInputViewPanel.addStyleName(CssConstants.CSS_ADV_KV_INPUT_PANEL);

		// valuePattern.addStyleName(CssConstants.CSS_ADV_KV_TEXT_BOX);
		valuePattern.addStyleName(CssConstants.CSS_ADV_KV_INPUT_COMBO_BOX);
		keyFuzzinessComboBox.addStyleName(CssConstants.CSS_ADV_KV_INPUT_COMBO_BOX);
		fetchValue.addStyleName(CssConstants.CSS_ADV_KV_INPUT_COMBO_BOX);
		kvPageValue.addStyleName(CssConstants.CSS_ADV_KV_INPUT_COMBO_BOX);
		extractZoneComboBox.addStyleName(CssConstants.CSS_ADV_KV_INPUT_COMBO_BOX);
		weightText.addStyleName(CssConstants.CSS_ADV_KV_TEXT_BOX);

		useExistingKeyLabel.setStyleName(CssConstants.CSS_ADV_KV_INPUT_LABEL);
		keyPatternLabel.setStyleName(CssConstants.CSS_ADV_KV_INPUT_LABEL);
		valuePatternLabel.setStyleName(CssConstants.CSS_ADV_KV_INPUT_LABEL);
		fetchValueLabel.setStyleName(CssConstants.CSS_ADV_KV_INPUT_LABEL);
		kvPageValueLabel.setStyleName(CssConstants.CSS_ADV_KV_INPUT_LABEL);
		extractZoneLabel.setStyleName(CssConstants.CSS_ADV_KV_INPUT_LABEL);
		weightLabel.setStyleName(CssConstants.CSS_ADV_KV_INPUT_LABEL);
		useExistingKeyLabel.setText(LocaleDictionary.getConstantValue(BatchClassConstants.USE_EXISTING_KEY_LABEL));
		keyPatternLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.KEY_PATTERN));
		setKeyPatternPanelView();
		valuePatternLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.VALUE_PATTERN));
		fetchValueLabel.setText(LocaleDictionary.getConstantValue(BatchClassConstants.FETCH_VALUE));
		kvPageValueLabel.setText(LocaleDictionary.getConstantValue(BatchClassConstants.KV_PAGE_VALUE_LABEL));

		// Setting text and style for fields of extract zone.
		extractZoneLabel.setText(StringUtil.concatenate(LocaleDictionary.getConstantValue(BatchClassConstants.EXTRACT_ZONE_LABEL)));

		addWeightTextAndCSS();

		// Adding Fuzzy matching threshold elements.
		addKeyFuzzinessTextAndCSS();
	}

	private void setKeyPatternPanelView() {
		keyPatternText = new RegexComboBox();
		// keyPatternText = new TextField();
		// keyPatternText.addStyleName(CssConstants.CSS_ADV_KV_TEXT_BOX);
		keyPatternText.addStyleName(CssConstants.CSS_ADV_KV_INPUT_COMBO_BOX);

		keyPatternComboBox = new ComboBox();
		keyPatternComboBox.addStyleName(CssConstants.CSS_ADV_KV_INPUT_COMBO_BOX);
		keyPatternComboBox.setWidth("100%");
		keyPatternComboBox.setEditable(false);
		keyPatternPanel.add(keyPatternText);
		useExistingKey.setValue(Boolean.FALSE);
		useExistingKey.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue().equals(Boolean.TRUE)) {
					keyPatternText.setVisible(false);
					keyPatternPanel.add(keyPatternComboBox);
				} else {
					keyPatternPanel.remove(keyPatternComboBox);
					keyPatternText.setVisible(true);
				}

			}
		});
	}

	/**
	 * Adds the fuzzy matching threshold elements to the KV Extarction View and applies CSS to the elements.
	 */
	private void addKeyFuzzinessTextAndCSS() {

		// Setting the text for the Label.
		keyFuzzinessLabel
				.setText(StringUtil.concatenate(LocaleDictionary.getConstantValue(BatchClassConstants.FUZZY_THRESHOLD_LABEL)));

		// Adding the CSS for the Label.
		keyFuzzinessLabel.addStyleName(CssConstants.CSS_ADV_KV_INPUT_LABEL);

		// Adding the threshold values to the fuzzy threshold list box.
		addFuzzyThresholdValues();
	}

	/**
	 * Adds the fuzzy matching threshold values to the {@code fuzzyThresholdListBox} list box.
	 */
	private void addFuzzyThresholdValues() {
		KVExtractionUtil.populateKeyFuzzinessList(keyFuzzinessComboBox);
	}

	/**
	 * Adds label and CSS to weight related widgets in the Advanced KV Extraction View.
	 */
	private void addWeightTextAndCSS() {

		// Adding text to weight labels
		weightLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.WEIGHT_LABEL));

	}

	@Override
	public void initialize() {

	}

	public MandatoryLabel getKeyPatternLabel() {
		return keyPatternLabel;
	}

	public void setKeyPatternLabel(MandatoryLabel keyPatternLabel) {
		this.keyPatternLabel = keyPatternLabel;
	}

	public MandatoryLabel getValuePatternLabel() {
		return valuePatternLabel;
	}

	public void setValuePatternLabel(MandatoryLabel valuePatternLabel) {
		this.valuePatternLabel = valuePatternLabel;
	}

	public KVFetchValue getFetchValue() {
		String selected = this.fetchValue.getStore().get(this.fetchValue.getSelectedIndex());
		KVFetchValue[] allKVFetchValue = KVFetchValue.values();
		for (KVFetchValue kvFetchValue : allKVFetchValue) {
			if (kvFetchValue.name().equals(selected)) {
				return kvFetchValue;
			}
		}
		return allKVFetchValue[0];
	}

	public void setFetchValue() {
		// this.fetchValue.setVisibleItemCount(1);
		KVFetchValue[] kvFetchValues = KVFetchValue.values();
		for (KVFetchValue kvFetchValue2 : kvFetchValues) {
			this.fetchValue.getStore().add(kvFetchValue2.name());
		}
	}

	public void setKeyPatternText(String keyPattern) {
		this.keyPatternText.setValue(keyPattern, true);

		this.keyPatternText.setText(keyPattern);
	}

	public String getKeyPattern() {
		// return keyPatternText.getValue();
		return keyPatternText.getText();
	}

	public void setValuePattern(String valuePattern) {
		this.valuePattern.setValue(valuePattern, true);

		this.valuePattern.setText(valuePattern);
	}

	public String getValuePattern() {
		// return valuePattern.getValue();
		return valuePattern.getText();
	}

	public void setFetchValue(KVFetchValue kvFetchValue) {
		if (this.fetchValue.getStore().size() == 0) {
			setFetchValue();
		}
		this.fetchValue.setValue(this.fetchValue.getStore().get(findKVIndex(kvFetchValue)), true);
	}

	// public TextField getKeyPatternTextField() {
	// return this.keyPatternText;
	// }

	public RegexComboBox getKeyPatternTextField() {
		return this.keyPatternText;
	}

	// public TextField getValuePatternTextField() {
	// return this.valuePattern;
	// }

	public RegexComboBox getValuePatternTextField() {
		return this.valuePattern;
	}

	public void setKVPageValue(final KVPageValue kvPageValue) {
		if (this.kvPageValue.getStore().size() == 0) {
			setKVPageValues();
		}
		int kvIndex = findKVIndex(kvPageValue);
		this.kvPageValue.setValue(this.kvPageValue.getStore().get(kvIndex), true);
	}

	public void setKVPageValues() {
		KVPageValue[] kvPageValues = KVPageValue.values();
		for (KVPageValue kvPageValue : kvPageValues) {
			this.kvPageValue.getStore().add((kvPageValue.name()));
		}
	}

	private int findKVIndex(final KVPageValue kvPageRange) {
		int kvIndex = 0;
		if (kvPageRange != null) {
			KVPageValue[] allLocationTypes = KVPageValue.values();
			List<KVPageValue> tempList = Arrays.asList(allLocationTypes);
			kvIndex = tempList.indexOf(kvPageRange);
		}
		return kvIndex;
	}

	private int findKVIndex(KVFetchValue kvFetchValue) {
		if (kvFetchValue == null) {
			return 0;
		}
		KVFetchValue[] allLocationTypes = KVFetchValue.values();
		List<KVFetchValue> tempList = Arrays.asList(allLocationTypes);
		return tempList.indexOf(kvFetchValue);
	}

	public void setKeyFieldList(final List<String> fieldTypeNames) {
		this.keyPatternComboBox.clear();
		if (fieldTypeNames != null && !fieldTypeNames.isEmpty()) {
			for (String fieldTypeName : fieldTypeNames) {
				if (fieldTypeName != null && !fieldTypeName.isEmpty()) {
					this.keyPatternComboBox.getStore().add(fieldTypeName);
				}
			}
		} else {
			this.keyPatternComboBox.getStore().add(LocaleDictionary.getConstantValue(BatchClassConstants.NO_FIELD_EXISTS));
			this.keyPatternComboBox.setValue(keyPatternComboBox.getStore().get(0), true);
		}
	}

	public boolean isKeyFieldSelected() {
		boolean isFieldSelected = false;
		if (this.keyPatternComboBox != null && this.keyPatternComboBox.getStore().size() != 0) {
			String selectedField = this.keyPatternComboBox.getStore().get(this.keyPatternComboBox.getSelectedIndex());
			if (selectedField != null && !selectedField.isEmpty()
					&& !selectedField.equalsIgnoreCase(LocaleDictionary.getConstantValue(BatchClassConstants.NO_FIELD_EXISTS))) {
				isFieldSelected = true;
			}
		}
		return isFieldSelected;
	}

	public String getKeyPatternField() {
		return keyPatternComboBox.getStore().get(keyPatternComboBox.getSelectedIndex());
	}

	public boolean isUseExistingKey() {
		return useExistingKey.getValue();
	}

	public void setUseExistingKey(boolean useExistingKey) {
		this.useExistingKey.setValue(useExistingKey);
	}

	public void setKeyField(final String keyField) {
		int totalFieldCount = keyPatternComboBox.getStore().size();
		for (int index = 0; index < totalFieldCount; index++) {
			if (keyPatternComboBox.getStore().get(index).equalsIgnoreCase(keyField)) {
				keyPatternComboBox.setValue(keyPatternComboBox.getStore().get(index), true);
				break;
			}
		}
	}

	public void setKeyPattern(final String keyPattern) {
		ValueChangeEvent.fire(useExistingKey, isUseExistingKey());
		presenter.setKeyPatternFields();
		if (isUseExistingKey()) {
			setKeyField(keyPattern);
		} else {
			setKeyPatternText(keyPattern);
		}
	}

	public void setweightTextValue(String weightValue) {
		this.weightText.setValue(weightValue, true);
	}

	public ComboBox getExtractZoneComboBox() {
		return extractZoneComboBox;
	}

	public ComboBox getKeyFuzzinessComboBox() {
		return keyFuzzinessComboBox;
	}

	public TextField getweightTextField() {
		return weightText;
	}

	public KVPageValue getKVPageValue() {
		KVPageValue selectedKVPageValue = null;
		String selected = this.kvPageValue.getStore().get(this.kvPageValue.getSelectedIndex());
		KVPageValue[] allKVPageValue = KVPageValue.values();
		for (KVPageValue kvPageValue : allKVPageValue) {
			if (kvPageValue.name().equals(selected)) {
				selectedKVPageValue = kvPageValue;
			}
		}
		if (selectedKVPageValue == null) {
			selectedKVPageValue = allKVPageValue[0];
		}
		return selectedKVPageValue;
	}

	public List<Span> getSpanList() {
		return spanList;
	}

	public void setSpanList(List<Span> spanList) {
		this.spanList = spanList;
	}

	public void resetView() {
		// keyPatternText.clear();
		keyPatternText.clearInvalid();
		keyPatternText.setText(BatchClassConstants.EMPTY_STRING);
		keyPatternComboBox.clear();
		keyPatternComboBox.getStore().clear();
		// valuePattern.clear();
		valuePattern.clearInvalid();
		valuePattern.setText(BatchClassConstants.EMPTY_STRING);
	}

	public void setComponents(final RegexBuilderView regexBuilderView, final RegexGroupSelectionGridView regexGroupSelectionGridView) {
		valuePattern.setComponents(regexBuilderView, regexGroupSelectionGridView);
		valuePattern.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {

				// valuePattern.setValue(newPattern, true);
				valuePattern.setText(newPattern);
				valuePattern.finishEditing();
				valuePattern.validate();

			}
		});

		keyPatternText.setComponents(regexBuilderView, regexGroupSelectionGridView);
		keyPatternText.setRegexComboBoxHandler(new RegexComboBoxHandler() {

			@Override
			// public void onRegexSelect(String newPattern) {
			public void onRegexSelect(String newPattern, Object currentSelectedObject, ValueProvider currentValueProvider) {
				// keyPatternText.setValue(newPattern, true);
				keyPatternText.setText(newPattern);
				keyPatternText.finishEditing();
				keyPatternText.validate();

			}
		});

	}

	public void setHeadingText(String heading) {
		if (!StringUtil.isNullOrEmpty(heading) && null != advKVFramedPanel) {
			advKVFramedPanel.setHeadingText(heading);
			advKVFramedPanel.getHeader().setToolTip(heading);
		}
	}
}
