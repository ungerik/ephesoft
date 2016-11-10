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

package com.ephesoft.gxt.admin.client.presenter.kvextraction.AdvancedKVExtraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ephesoft.dcma.core.common.KVExtractZone;
import com.ephesoft.dcma.core.common.KVFetchValue;
import com.ephesoft.dcma.core.common.KVPageValue;
import com.ephesoft.gxt.admin.client.controller.BatchClassManagementController;
import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.util.KVExtractionUtil;
import com.ephesoft.gxt.admin.client.view.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionInputPanelView;
import com.ephesoft.gxt.admin.client.widget.RegexComboBox;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.ScreenMaskUtility;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.validator.form.EmptyValueValidator;
import com.ephesoft.gxt.core.client.validator.form.RangeValidator;
import com.ephesoft.gxt.core.client.validator.form.RegexPatternValidator;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.dto.DocumentTypeDTO;
import com.ephesoft.gxt.core.shared.dto.FieldTypeDTO;
import com.ephesoft.gxt.core.shared.dto.KVExtractionDTO;
import com.ephesoft.gxt.core.shared.util.ParseUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.i18n.client.NumberFormat;
//import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.binder.EventBinder;

public class AdvancedKVExtractionInputPanelPresenter extends AdvancedKVExtractionInlinePresenter<AdvancedKVExtractionInputPanelView> {

	interface CustomEventBinder extends EventBinder<AdvancedKVExtractionInputPanelPresenter> {
	}

	private static final CustomEventBinder eventBinder = GWT.create(CustomEventBinder.class);

	/**
	 * The userType {@link Integer} is used for storing user type.
	 */
	private Integer userType;

	/**
	 * Constructor.
	 * 
	 * @param controller {@link UploadBatchController}
	 * @param view {@link UploadBatchButtonPanelView}
	 */
	public AdvancedKVExtractionInputPanelPresenter(final BatchClassManagementController controller,
			final AdvancedKVExtractionInputPanelView view) {
		super(controller, view);
		setUserType();
		addValidators();
	}

	private final String HEADING_SEPARATOR = " :: ";

	@Override
	public void bind() {
		KVExtractionDTO selectedKVExtraction = controller.getSelectedKVExtraction();
		view.setKeyFieldList(null);
		resetView();
		view.setComponents(controller.getRegexBuilderView(), controller.getRegexGroupSelectionGridView());
		view.setHeadingText(getHeadingText());
		if (null != selectedKVExtraction) {
			view.setUseExistingKey(selectedKVExtraction.isUseExistingKey());

			view.setKeyPattern(selectedKVExtraction.getKeyPattern());

			view.setValuePattern(selectedKVExtraction.getValuePattern());
			if (selectedKVExtraction.getFetchValue() != null) {
				view.setFetchValue(selectedKVExtraction.getFetchValue());
			} else {
				view.setFetchValue(KVFetchValue.ALL);
			}
			if (selectedKVExtraction.getKvPageValue() != null) {
				view.setKVPageValue(selectedKVExtraction.getKvPageValue());
			} else {
				view.setKVPageValue(KVPageValue.ALL);
			}

			// Setting selected Extract zone from kv extraction zones in list
			// box.
			if (selectedKVExtraction.getExtractZone() != null) {
				KVExtractionUtil.setExtractZone(selectedKVExtraction.getExtractZone(), view.getExtractZoneComboBox());
			} else {
				KVExtractionUtil.setExtractZone(KVExtractZone.ALL, view.getExtractZoneComboBox());
			}
			view.setweightTextValue(getWeightValue(selectedKVExtraction.getWeight()));

			// Setting the proper option in the Fuzziness list box defined for
			// key pattern.
			view.getKeyFuzzinessComboBox().setValue(
					view.getKeyFuzzinessComboBox().getStore()
							.get(KVExtractionUtil.getKeyFuzzinessIndex(selectedKVExtraction.getKeyFuzziness())), true);
			view.setSpanList(null);

		}
	}

	private String getHeadingText() {
		KVExtractionDTO kvExtractionDTO = controller.getSelectedKVExtraction();
		StringBuilder heading = new StringBuilder();
		if (null != kvExtractionDTO) {
			FieldTypeDTO fieldTypeDTO = kvExtractionDTO.getFieldTypeDTO();
			if (null != fieldTypeDTO) {
				DocumentTypeDTO docTypeDTO = fieldTypeDTO.getDocTypeDTO();
				if (null != docTypeDTO) {
					BatchClassDTO batchClassDTO = docTypeDTO.getBatchClass();
					if (null != batchClassDTO) {
						heading.append(batchClassDTO.getIdentifier());
					}
					heading.append(HEADING_SEPARATOR);
					heading.append(docTypeDTO.getName());
				}
				heading.append(HEADING_SEPARATOR);
				heading.append(fieldTypeDTO.getName());
			}
		}
		return heading.toString();
	}

	private void addValidators() {
		view.getKeyPatternTextField().addValidator(
				new RegexPatternValidator(LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_KEY_PATTERN)));
		view.getKeyPatternTextField().addValidator(
				new EmptyValueValidator(LocaleDictionary.getConstantValue(BatchClassConstants.MANDATORY_FIELDS_ERROR_MSG)));
		view.getValuePatternTextField().addValidator(
				new RegexPatternValidator(LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_VALUE_PATTERN)));
		view.getValuePatternTextField().addValidator(
				new EmptyValueValidator(LocaleDictionary.getConstantValue(BatchClassConstants.MANDATORY_FIELDS_ERROR_MSG)));
		view.getweightTextField().addValidator(
				new RangeValidator(Float.class, LocaleDictionary.getMessageValue(BatchClassMessages.WEIGHT_ERROR),
						getRange(BatchClassConstants.MIN_WEIGHT_RANGE), getRange(BatchClassConstants.MAX_WEIGHT_RANGE)));
	}

	private Float getRange(Float val) {
		NumberFormat format = NumberFormat.getFormat(".##");
		Float range = val;
		String str = format.format(range);
		range = Float.valueOf(str);
		return range;
	}

	private String getWeightValue(Float weight) {
		String weightValue = AdminConstants.WEIGHT_END_RANGE.toString();
		if (null != weight) {
			weightValue = ParseUtil.getRoundOffValue(String.valueOf(weight), 2);
		}
		return weightValue;
	}

	@Override
	public void injectEvents(final EventBus eventBus) {
		eventBinder.bindEventHandlers(this, eventBus);

	}

	public void validateRegexField(final RegexComboBox regexBox) {
		if (null != regexBox) {
			String value = regexBox.getValue();
			if (!StringUtil.isNullOrEmpty(value)) {
				ScreenMaskUtility.maskScreen();
				rpcService.validateRegEx(value, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						ScreenMaskUtility.unmaskScreen();
						regexBox.forceInvalid("Regex Pattern is invalid.");
					}

					@Override
					public void onSuccess(Boolean result) {
						ScreenMaskUtility.unmaskScreen();
						if (Boolean.TRUE == result) {
							regexBox.clearInvalid();
						} else {
							regexBox.forceInvalid("Regex Pattern is invalid.");
						}
					}
				});
			}
		}
	}

	/**
	 * The <code>setUserType</code> method is used for setting current user type.
	 */
	private void setUserType() {
		controller.getRpcService().getUserType(new AsyncCallback<Integer>() {

			@Override
			public void onFailure(final Throwable arg0) {
				// Do nothing
			}

			@Override
			public void onSuccess(final Integer userType) {
				if (null != userType) {
					setUserType(userType);
				}
			}
		});
	}

	/**
	 * Getter for user type.
	 * 
	 * @return user type {@link Integer}
	 */
	public Integer getUserType() {
		return userType;
	}

	/**
	 * Setter for user type.
	 * 
	 * @param userType {@link Integer}
	 */
	public void setUserType(final Integer userType) {
		this.userType = userType;
	}

	public boolean validateFields() {
		boolean validFlag = true;
		view.getKeyPatternTextField().validate();
		view.getValuePatternTextField().validate();
		view.getweightTextField().validate();
		if (validFlag && view.isUseExistingKey()) {
			if (!view.isKeyFieldSelected()) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.NO_KEY_FIELD_SELECTED), DialogIcon.ERROR);
				validFlag = false;
			}
		} else if (validFlag && !view.getKeyPatternTextField().isValid()) {
			validFlag = false;
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_KEY_PATTERN), DialogIcon.ERROR);
		} else if (validFlag && !view.getValuePatternTextField().isValid()) {
			validFlag = false;
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_VALUE_PATTERN), DialogIcon.ERROR);
		} else if (validFlag && !view.getweightTextField().isValid()) {
			validFlag = false;
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.WEIGHT_ERROR), DialogIcon.ERROR);

		}
		// Check for the key pattern length in case user has selected the option
		// for fuzzy match threshold value.
		else if (validFlag && getKeyFuzzinessValue() != null) {
			int keyPatternLength = view.getKeyPattern().trim().length();
			if (keyPatternLength < AdminConstants.MINIMUM_ALLOWED_CHARACTERS_FUZZINESS) {
				DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.ERROR_TITLE),
						LocaleDictionary.getMessageValue(BatchClassMessages.KEY_PATTERN_LENGTH_FUZZINESS_ERROR), DialogIcon.ERROR);
				validFlag = false;
			}
		}
		return validFlag;
	}

	private Float getKeyFuzzinessValue() {
		final int selectedIndex = view.getKeyFuzzinessComboBox().getSelectedIndex();
		final String selectedValue = view.getKeyFuzzinessComboBox().getStore().get(selectedIndex);
		return KVExtractionUtil.getKeyFuzzinessValue(selectedValue);
	}

	public void onCancelButtonClicked() {
		// view.removeAllOverlays();
		// controller.setAdvKV(false);
		controller.createView();

	}

	public void saveDataInDTO(KVExtractionDTO selectedKVExtraction) {

		selectedKVExtraction.setUseExistingKey(view.isUseExistingKey());

		if (selectedKVExtraction.isUseExistingKey()) {
			selectedKVExtraction.setKeyPattern(view.getKeyPatternField());
		} else {
			selectedKVExtraction.setKeyPattern(view.getKeyPattern());
		}
		selectedKVExtraction.setValuePattern(view.getValuePattern());
		selectedKVExtraction.setNoOfWords(null);
		selectedKVExtraction.setUseExistingKey(view.isUseExistingKey());

		if (view.getFetchValue() != null) {
			selectedKVExtraction.setFetchValue(view.getFetchValue());
		}
		if (view.getKVPageValue() != null) {
			selectedKVExtraction.setKvPageValue(view.getKVPageValue());
		}

		// Saves selected extract zone in DTO of KVExtraction.
		KVExtractZone kvExtractZone = KVExtractionUtil.getExtractZone(view.getExtractZoneComboBox());
		// selectedKVExtraction.setLocationType(locationType);
		if (kvExtractZone != null) {
			selectedKVExtraction.setExtractZone(kvExtractZone);
		}

		// Adding changes weight value in DTO
		String weightValue = String.valueOf(view.getweightTextField().getValue());
		if (StringUtil.isNullOrEmpty(weightValue)) {
			selectedKVExtraction.setWeight(null);
		} else {
			selectedKVExtraction.setWeight(Float.valueOf(weightValue));
		}

		// Setting the fuzzy match threshold value for key pattern.
		selectedKVExtraction.setKeyFuzziness(getKeyFuzzinessValue());
		// Added in Image View.
		controller.getAdvKvExtractionPresenter().getAdvKvImageViewPresenter().saveDataInDTO(selectedKVExtraction);
	}

	public void setKeyPatternFields() {
		FieldTypeDTO fieldTypeDTO = controller.getSelectedKVExtraction().getFieldTypeDTO();
		if (null != fieldTypeDTO && null != fieldTypeDTO.getDocTypeDTO()) {
			DocumentTypeDTO docTypeDTO = fieldTypeDTO.getDocTypeDTO();
			if (docTypeDTO != null) {
				int fieldOrderNumber = fieldTypeDTO.getFieldOrderNumber();
				List<String> fieldTypeNames = new ArrayList<String>();
				if (fieldOrderNumber > -1) {
					Collection<FieldTypeDTO> fieldTypeDTOs = docTypeDTO.getFields(false);
					if (fieldTypeDTOs != null && !fieldTypeDTOs.isEmpty()) {
						for (FieldTypeDTO fieldType : fieldTypeDTOs) {
							if (fieldType.getFieldOrderNumber() < fieldOrderNumber) {
								fieldTypeNames.add(fieldType.getName());
							}
						}
					}
					view.setKeyFieldList(fieldTypeNames);
				}
			}
		}
	}

	public void resetView() {
		view.resetView();
	}

}
