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

package com.ephesoft.gxt.admin.client.view.batchclass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.core.common.DefaultBatchClasses;
import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.batchclass.ImportBatchClassDialogWindowPresenter;
import com.ephesoft.gxt.admin.client.view.BatchClassInlineView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.MandatoryLabel;
//import com.sencha.gxt.widget.core.client.Slider;
import com.ephesoft.gxt.core.client.ui.widget.Slider;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.form.EmptyValueValidator;
import com.ephesoft.gxt.core.shared.constant.BatchConstants;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.BatchClassDTO;
import com.ephesoft.gxt.core.shared.importTree.Node;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextField;

public class ImportBatchClassDialogWindowView extends BatchClassInlineView<ImportBatchClassDialogWindowPresenter> {

	interface Binder extends UiBinder<Widget, ImportBatchClassDialogWindowView> {
	}

	private static final Binder BINDER = GWT.create(Binder.class);

	@UiField
	protected MandatoryLabel batchClassNameLabel;

	@UiField
	protected VerticalLayoutContainer parentContainer;

	@UiField
	protected MandatoryLabel batchClassDescriptionLabel;

	@UiField
	protected MandatoryLabel batchClassPriorityLabel;

	@UiField
	protected MandatoryLabel uncFolderLabel;

	@UiField
	protected Label importBatchClassWithLabel;

	@UiField
	protected TextField batchClassNameTextField;

	@UiField
	protected TextField batchClassDescriptionTextField;

	@UiField
	protected Slider prioritySlider;

	@UiField
	protected ComboBox uncFolderComboBox;

	@UiField
	protected CheckBox useExistingUNCCheckBox;

	@UiField
	protected CheckBox importConnectionsCheckBox;

	@UiField
	protected RadioButton withLearnFilesRadioButton;

	@UiField
	protected RadioButton withoutLearnFilesRadioButton;

	@UiField
	protected CheckBox importRolesCheckBox;

	private String userInputBatchClassName;

	private String userInputBatchClassDescription;

	private Map<String, String> uncFoldersMap;

	@UiField
	protected TextField prioritySliderTextBox;

	public ImportBatchClassDialogWindowView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		setElementIds();
		uncFoldersMap = new HashMap<String, String>();
		this.addStyleName("importBatchClassView");
		batchClassNameLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.BATCH_CLASS_NAME));
		batchClassDescriptionLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.BATCH_CLASS_DESCRIPTION));
		batchClassPriorityLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.PRIORITY));
		uncFolderLabel.setLabelText(LocaleDictionary.getConstantValue(BatchClassConstants.UNC_FOLDER));
		importBatchClassWithLabel.setText(LocaleDictionary.getConstantValue(BatchClassConstants.IMPORT_BATCH_CLASS));
		useExistingUNCCheckBox.setText(LocaleDictionary.getConstantValue(BatchClassConstants.USE_EXISTING));
		importConnectionsCheckBox.setText(LocaleDictionary.getConstantValue(BatchClassConstants.IMPORT_CONNECTIONS));
		withLearnFilesRadioButton.setText(LocaleDictionary.getConstantValue(BatchClassConstants.WITH_LEARNED_FILES));
		withoutLearnFilesRadioButton.setText(LocaleDictionary.getConstantValue(BatchClassConstants.WITHOUT_LEARNED_FILES));
		importRolesCheckBox.setText(LocaleDictionary.getConstantValue(BatchClassConstants.ROLES));
		withLearnFilesRadioButton.setValue(true);
		batchClassDescriptionTextField.setAutoValidate(true);
		batchClassNameTextField.setAutoValidate(true);
		batchClassDescriptionTextField.addValidator(new EmptyValueValidator(LocaleDictionary
				.getMessageValue(BatchClassMessages.BATCH_CLASS_DESCRIPTION_CANNOT_BE_EMPTY)));
		batchClassNameTextField.addValidator(new EmptyValueValidator(LocaleDictionary
				.getMessageValue(BatchClassMessages.BATCH_CLASS_NAME_CANNOT_BE_EMPTY)));
		uncFolderComboBox.addValidator(new EmptyValueValidator(LocaleDictionary
				.getMessageValue(BatchClassMessages.UNC_PATH_CANNOT_BE_EMPTY)));
		uncFolderComboBox.setAutoValidate(true);
		prioritySlider.setMinValue(1);
		prioritySlider.setMaxValue(100);
		prioritySlider.setIncrement(1);
		prioritySlider.setWidth("151");
		this.addUseExistingUNCHandler();
		this.addUNCFoderComboBoxValueChangeHandler();
		this.addStyles();

		prioritySliderTextBox.setReadOnly(true);
		prioritySliderTextBox.setEnabled(false);
		this.addPrioritySliderMoveHandler();

	}

	public void setElementIds() {
		WidgetUtil.setID(batchClassNameTextField, "batchClassNameTextFieldId");
		WidgetUtil.setID(batchClassDescriptionTextField, "batchClassDescriptionTextFieldId");
		WidgetUtil.setID(uncFolderComboBox, "uncFolderComboBoxId");
	}

	private void addStyles() {
		batchClassNameLabel.addStyleName("importBatchClassInputLabel");
		batchClassDescriptionLabel.addStyleName("importBatchClassInputLabel");
		batchClassPriorityLabel.addStyleName("importBatchClassInputLabel");
		uncFolderLabel.addStyleName("importBatchClassInputLabel");
		importBatchClassWithLabel.addStyleName("importHighlightLabel");
		importBatchClassWithLabel.addStyleName("importBatchClassInputLabel");
		batchClassNameTextField.addStyleName("batchClassFieldTextField");
		batchClassDescriptionTextField.addStyleName("batchClassDescriptionTextField");
		prioritySlider.addStyleName("batchClassPrioritySlider");
		uncFolderComboBox.addStyleName("uncFolderCombo");
		useExistingUNCCheckBox.addStyleName("useExistingUNC");
		withLearnFilesRadioButton.addStyleName("withLearnedFilesRadio");
		withoutLearnFilesRadioButton.addStyleName("withoutLearnedFilesRadio");
		importRolesCheckBox.addStyleName("importRolesCheckBox");
		importConnectionsCheckBox.addStyleName("importConnectionsCheckBox");
		prioritySliderTextBox.addStyleName("priorityTextBox");
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		uncFolderComboBox.setHideTrigger(true);
		useExistingUNCCheckBox.setValue(false);
		uncFolderComboBox.reset();
		uncFolderComboBox.getStore().clear();
		uncFolderComboBox.getStore().commitChanges();
		importRolesCheckBox.setValue(false);
		withLearnFilesRadioButton.setValue(true);
		parentContainer.forceLayout();
		presenter.getAllBatchClassesList();
	}

	private void addUseExistingUNCHandler() {
		useExistingUNCCheckBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				boolean isSelected = useExistingUNCCheckBox.getValue();
				if (isSelected) {
					userInputBatchClassName = getBatchClassName();
					userInputBatchClassDescription = getBatchClassDescription();
				}
				uncFolderComboBox.setHideTrigger(!isSelected);
				if (isSelected) {
					setUNCFoldersList(presenter.getAllBatchClassesList());
				} else {
					if (isAttached()) {
						setBatchClassDescription(userInputBatchClassDescription);
						setBatchClassName(userInputBatchClassName);
						setUNCFoldersList(null);
						uncFolderComboBox.finishEditing();
					}
				}
				uncFolderComboBox.validate();
			}
		});
	}

	private void addUNCFoderComboBoxValueChangeHandler() {

		uncFolderComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String path = uncFolderComboBox.getText();
				if (!StringUtil.isNullOrEmpty(path) && useExistingUNCCheckBox.getValue()) {
					String batchClassIdentifier = path.split(" - ")[0];
					BatchClassDTO batchClass = getBatchClassDetails(batchClassIdentifier, presenter.getAllBatchClassesList());
					if (batchClass != null) {
						setBatchClassName(batchClass.getName());
						setBatchClassDescription(batchClass.getDescription());
					}
				}

			}
		});

	}

	public void setBatchClassName(final String name) {
		batchClassNameTextField.setValue(name);
		batchClassNameTextField.finishEditing();
		batchClassNameTextField.validate();
	}

	public void setBatchClassDescription(final String description) {
		batchClassDescriptionTextField.setText(description);
		batchClassDescriptionTextField.finishEditing();
		batchClassNameTextField.validate();
	}

	public void setPriority(int priority) {
		prioritySlider.setValue(priority);
		prioritySlider.finishEditing();
	}

	public boolean isImportRoles() {
		return importRolesCheckBox.getValue();
	}

	public boolean importWithLearnFiles() {
		return withLearnFilesRadioButton.getValue();
	}

	public String getBatchClassName() {
		batchClassNameTextField.finishEditing();
		String batchClassName = batchClassNameTextField.getValue();
		return batchClassName == null ? CoreCommonConstant.EMPTY_STRING : batchClassName.trim();
	}

	public String getBatchClassDescription() {
		batchClassDescriptionTextField.finishEditing();
		String batchClassDescription = batchClassDescriptionTextField.getValue();
		return batchClassDescription == null ? CoreCommonConstant.EMPTY_STRING : batchClassDescription.trim();
	}

	public boolean isImportExistingUNC() {
		return useExistingUNCCheckBox.getValue();
	}

	public int getPriority() {
		return prioritySlider.getValue();
	}

	public boolean isValid() {
		batchClassNameTextField.finishEditing();
		batchClassDescriptionTextField.finishEditing();
		uncFolderComboBox.finishEditing();
		boolean isValid = true;
		if (!batchClassNameTextField.validate()) {
			isValid = false;
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_NAME_CANNOT_BE_EMPTY));
		} else if (!batchClassDescriptionTextField.validate()) {
			isValid = false;
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_DESCRIPTION_CANNOT_BE_EMPTY));
		} else if (!uncFolderComboBox.validate()) {
			isValid = false;
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.UNC_FOLDER_CANNOT_BE_EMPTY));
		} else if (!presenter.validateBCNameForSpecialCharacters(batchClassNameTextField.getCurrentValue())) {
			isValid = false;
			String invalidCharacters = presenter.getNotAllowedBatchClassNameCharacters();
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.INVALID_CHARACTER_IN_BCNAME_MESSAGE, invalidCharacters));
		}

		String batchClassName = getBatchClassName();
		if (isValid && !StringUtil.isNullOrEmpty(batchClassName)
				&& (batchClassName.contains(CoreCommonConstant.SPACE) || batchClassName.contains("-"))) {
			isValid = false;
			DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(BatchClassConstants.INFO_TITLE),
					LocaleDictionary.getMessageValue(BatchClassMessages.BATCH_CLASS_NAME_CANNOT_CONTAIN_SPACE_OR_HYPHEN));
		}
		return isValid;
	}

	public void setUNCFoldersList(final List<BatchClassDTO> batchClassList) {
		uncFolderComboBox.getStore().clear();
		uncFolderComboBox.getStore().commitChanges();
		uncFoldersMap.clear();
		if (!CollectionUtil.isEmpty(batchClassList)) {
			for (final BatchClassDTO batchClass : batchClassList) {
				if (null != batchClass && !DefaultBatchClasses.isDefaultBatchClass(batchClass.getIdentifier())) {
					String batchClassName = batchClass.getName();
					String batchClassIdentifier = batchClass.getIdentifier();
					String displayString = StringUtil.concatenate(batchClassIdentifier, " - ", batchClassName);
					uncFoldersMap.put(displayString, batchClass.getUncFolder());
					uncFolderComboBox.add(displayString);
				}
			}
			String value = uncFolderComboBox.getStore().get(0);
			BatchClassDTO batchClass = getBatchClassDetails(value.split(" - ")[0], batchClassList);
			if (null != batchClass) {
				setBatchClassName(batchClass.getName());
				setBatchClassDescription(batchClass.getDescription());
			}
			uncFolderComboBox.setText(value);
			uncFolderComboBox.finishEditing();
			uncFolderComboBox.validate();
		}
	}

	private BatchClassDTO getBatchClassDetails(String batchClassIdentifier, List<BatchClassDTO> batchClassList) {
		BatchClassDTO classDTO = null;
		if (!CollectionUtil.isEmpty(batchClassList)) {
			for (BatchClassDTO batchClassDTO : batchClassList) {
				if (batchClassDTO.getIdentifier().equalsIgnoreCase(batchClassIdentifier)) {
					classDTO = batchClassDTO;
					break;
				}
			}
		}
		return classDTO;
	}

	public String getUNCFolderPath() {
		String uncFolderPath = null;
		uncFolderComboBox.finishEditing();
		if (useExistingUNCCheckBox.getValue()) {
			uncFolderPath = uncFoldersMap.get(uncFolderComboBox.getValue());
		} else {
			uncFolderPath = uncFolderComboBox.getValue();
		}
		return uncFolderPath == null ? CoreCommonConstant.EMPTY_STRING : uncFolderPath.trim();
	}

	public String getUNCPathToIgnore() {
		String uncPathToIgnore = null;
		if (isImportExistingUNC()) {
			uncPathToIgnore = getUNCFolderPath();
		}
		return uncPathToIgnore;
	}

	public Node getUiConfigNode() {
		Node rootNode = new Node();
		List<Node> nodeList = rootNode.getChildren();
		Node rolesNode = new Node();
		rolesNode.setLabel(new com.ephesoft.gxt.core.shared.importTree.Label(BatchConstants.ROLES, CoreCommonConstant.EMPTY_STRING,
				isImportRoles()));

		nodeList.add(rolesNode);
		return rootNode;
	}

	public boolean isImportConnections() {
		return importConnectionsCheckBox.getValue();
	}

	public String getPrioritySliderString() {
		return prioritySlider.getValue().toString();
	}

	public TextField getPrioritySliderTextBox() {
		return prioritySliderTextBox;
	}

	public String getprioritySliderValue() {
		return prioritySliderTextBox.getText();
	}

	public void setprioritySliderValue(String priorityValue) {
		prioritySliderTextBox.setText(priorityValue);
	}

	public void addPrioritySliderMoveHandler() {

		prioritySlider.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {

				setprioritySliderValue(prioritySlider.getValue().toString());
			}
		});

	}

	public void setUseExistingUNCCheckBox(boolean value) {
		this.useExistingUNCCheckBox.setEnabled(value);
	}
}
