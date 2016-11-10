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

package com.ephesoft.gxt.uploadbatch.client.view;

import java.util.List;
import java.util.Map;

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.ui.widget.window.DialogIcon;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.DialogUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.gxt.uploadbatch.client.presenter.UploadBatchButtonPanelPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;

/**
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> 08-Sep-2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class UploadBatchButtonPanelView extends View<UploadBatchButtonPanelPresenter> {

	interface Binder extends UiBinder<Widget, UploadBatchButtonPanelView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected Label batchClassLabel;

	@UiField
	protected Label batchDescriptionLabel;

	@UiField
	protected TextBox batchDescriptionTextbox;

	@UiField
	protected ComboBox batchClassComboBox;

	@UiField
	protected CustomMenuBar buttonBar;

	@UiField
	protected Button batchClassSortButton;

	CustomMenuItem startButton;

	CustomMenuItem deleteButton;

	CustomMenuItem resetButton;

	CustomMenuItem fieldButton;

	private Map<String, String> batchClassMapping;

	private String batchName;

	private boolean onLoad = true;

	@Override
	public void initialize() {
	}

	/**
	 * Constructor.
	 */
	public UploadBatchButtonPanelView() {
		super();
		initWidget(binder.createAndBindUi(this));

		batchClassLabel.setText( LocaleDictionary.getConstantValue(UploadBatchConstants.BATCH_CLASS_LABEL));
		batchDescriptionLabel.setText( LocaleDictionary.getConstantValue(UploadBatchConstants.DESCRIPTION_LABEL));
		batchClassComboBox.setEditable(false);
		batchDescriptionTextbox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				final String value = event.getValue(); 
				if (value.length() >= UploadBatchConstants.BATCH_DESCRIPTION_LENGTH_LIMIT) {
					final String trimValue = value.substring(0, UploadBatchConstants.BATCH_DESCRIPTION_LENGTH_LIMIT);
					batchDescriptionTextbox.setValue(trimValue);
					batchDescriptionTextbox.setTitle(trimValue);
				} else {
					batchDescriptionTextbox.setValue(value);
					batchDescriptionTextbox.setTitle(event.getValue());
				}
			}
		});
		initializeMenuButtons();
		intializeSelectionHandlers();
		addBatchClassComboBoxValueChangeHandler();
		batchClassSortButton.setStyleName(CoreCommonConstants.SET_ICON_FOR_SORTING_CSS);
		toggleBatchClassSortButtonToolTip(false);

		batchDescriptionTextbox.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (getBatchDescriptionFromTextBox().length() >= 255) {
					event.getNativeEvent().preventDefault();
				}
			}
		});

		setWidgetIDs();
	}

	/**
	 * Initializes menu items.
	 */
	@SuppressWarnings("serial")
	private void initializeMenuButtons() {
		buttonBar.setFocusOnHoverEnabled(false);
		startButton = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(UploadBatchConstants.START_BATCH);
			}
		});

		deleteButton = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(UploadBatchConstants.DELETE_FILES);
			}
		});

		resetButton = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(UploadBatchConstants.RESET_ALL);
			}
		});

		fieldButton = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(UploadBatchConstants.FIELDS);
			}
		});

		buttonBar.addItem(startButton);
		buttonBar.addItem(deleteButton);
		buttonBar.addItem(resetButton);
		buttonBar.addItem(fieldButton);
		buttonBar.addStyleName(UploadBatchConstants.MENU_BAR_BATCH_INSTANCE_STYLE);

	}

	/**
	 * Initializes menu item selection handlers.
	 */
	private void intializeSelectionHandlers() {

		startButton.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				String selectedBatchClassId = null;
				String batchDescription = null;
				if (batchClassComboBox != null && batchClassComboBox.getSelectedIndex() >= 0) {
					selectedBatchClassId = getSelectedBatchClassComboBoxValue();
				}
				batchDescription = getBatchDescriptionFromTextBox();
				if (StringUtil.isNullOrEmpty(selectedBatchClassId)) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.ERROR_TITLE),
							LocaleDictionary.getConstantValue(UploadBatchConstants.UNABLE_TO_START_SELCT_VALID_BATCH_CLASS), DialogIcon.ERROR);
				} else if (StringUtil.isNullOrEmpty(batchDescription)) {
					DialogUtil.showMessageDialog(LocaleDictionary.getConstantValue(UploadBatchConstants.INFO_TITLE),
							LocaleDictionary.getConstantValue(UploadBatchConstants.UNABLE_TO_START_ENTER_BATCH_DESCRIPTION), DialogIcon.INFO);
				} else {
					presenter.startNewBatch(selectedBatchClassId, batchDescription);
				}
			}
		});

		deleteButton.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onDeleteFilesClicked();
			}
		});

		resetButton.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.deleteAllFiles();
			}
		});

		fieldButton.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onFieldsButtonClicked();

			}
		});

		batchDescriptionTextbox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				setBatchDescriptionInTextBox(CoreCommonConstants.EMPTY_STRING);

			}
		});
	}

	/**
	 * Adds value change handler to Batch Class combobox.
	 */
	private void addBatchClassComboBoxValueChangeHandler() {
		batchClassComboBox.enableValidation(true);
		batchClassComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				presenter.batchClassComboBoxValueChanged(getSelectedBatchClassComboBoxValue());
				batchClassComboBox.setToolTip(batchClassComboBox.getValue());
				presenter.generateBatchName();
			}
		});
	}

	/**
	 * Sorts the batch class on batch class id or description, alternatively.
	 * 
	 * @param event {@link ClickEvent}
	 */
	@UiHandler("batchClassSortButton")
	void onBatchClassSortButton(final ClickEvent event) {
		presenter.onSortButtonClicked();
	}

	/**
	 * Setting widgets IDs.
	 */
	private void setWidgetIDs() {
		WidgetUtil.setID(batchDescriptionTextbox, "batchDescription");
		WidgetUtil.setID(batchClassComboBox, "batchClassComboBox");
		WidgetUtil.setID(buttonBar, "buttonBar");
		WidgetUtil.setID(startButton, "startButton");
		WidgetUtil.setID(deleteButton, "deleteButton");
		WidgetUtil.setID(resetButton, "resetButton");
		WidgetUtil.setID(fieldButton, "fieldButton");
	}

	/**
	 * Gets the selected Batch class ID based on batch class combobox value.
	 * 
	 * @return {@link String}
	 */
	public String getSelectedBatchClassComboBoxValue() {

		String batchClassId = null;
		final String batchClassDescription = batchClassComboBox.getValue();
		if (!StringUtil.isNullOrEmpty(batchClassDescription)) {
			batchClassId = batchClassMapping.get(batchClassDescription);
		}
		return batchClassId;
	}

	/**
	 * Sets the values in batch class combobox.
	 * 
	 * @param listItems {@link List}<{@link String}>
	 */
	public void setBatchClassComboBoxValues(final List<String> listItems) {
		if (!listItems.isEmpty()) {
			batchClassComboBox.getStore().clear();
			batchClassComboBox.add(listItems);
			if (onLoad) {
				setSelectedBatchClassComboBoxValue(0);
				onLoad = !onLoad;
			}
			batchClassComboBox.finishEditing();
		}
	}

	/**
	 * Sets the batch class mappings.
	 * 
	 * @param batchClassMapping {@link Map}<{@link String},{@link String}>
	 */
	public void setBatchClassMapping(final Map<String, String> batchClassMapping) {
		this.batchClassMapping = batchClassMapping;
	}

	/**
	 * Sets the selected value in batch class combobox.
	 * 
	 * @param index
	 */
	public void setSelectedBatchClassComboBoxValue(final int index) {
		if (null != batchClassComboBox && batchClassComboBox.getStore().size() != 0) {
			final ListStore<String> batchClassComboBoxValues = batchClassComboBox.getStore();
			batchClassComboBox.setValue(batchClassComboBoxValues.get(index), true);
		}

	}

	/**
	 * Gets the Batch Name from Batch Name.
	 * 
	 * @return {@link String}
	 */
	public String getBatchName() {
		return batchName;
	}

	/**
	 * Sets the Batch Name.
	 * 
	 * @param currentBatchUploadFolder {@link String}
	 */
	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	/**
	 * Gets the Batch Description from Batch Description Textbox.
	 * 
	 * @return {@link String}
	 */
	public String getBatchDescriptionFromTextBox() {
		return this.batchDescriptionTextbox.getText();
	}

	/**
	 * Sets the Batch Description inside Batch Description Textbox.
	 * 
	 * @param currentBatchUploadFolder {@link String}
	 */
	public void setBatchDescriptionInTextBox(final String currentBatchUploadFolder) {
		batchDescriptionTextbox.setValue(currentBatchUploadFolder, true);
	}

	/**
	 * Enables/Disables Fields button based on parameter passed.
	 * 
	 * @param value
	 */
	public void enableFieldsButton(final boolean value) {
		fieldButton.setEnabled(value);
	}

	/**
	 * Toggles the tool tip of batch class sort button.
	 */
	public void toggleBatchClassSortButtonToolTip(final boolean descriptionToolTip) {
		if (descriptionToolTip) {
			batchClassSortButton.setTitle(LocaleDictionary.getLocaleDictionary().getConstantValue(LocaleCommonConstants.SORT_ON_ID));
		} else {
			batchClassSortButton.setTitle(LocaleDictionary.getLocaleDictionary().getConstantValue(
					LocaleCommonConstants.SORT_ON_DESCRIPTION));
		}
	}
}
