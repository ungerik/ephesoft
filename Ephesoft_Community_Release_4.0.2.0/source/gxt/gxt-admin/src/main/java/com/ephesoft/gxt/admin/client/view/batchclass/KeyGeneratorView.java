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

import com.ephesoft.gxt.admin.client.i18n.AdminConstants;
import com.ephesoft.gxt.admin.client.i18n.BatchClassMessages;
import com.ephesoft.gxt.admin.client.presenter.batchclass.KeyGeneratorPresenter;
import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.form.EmptyValueValidator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.TextField;

public class KeyGeneratorView extends View<KeyGeneratorPresenter> {

	interface Binder extends UiBinder<VerticalPanel, KeyGeneratorView> {
	}

	String batchClassIdentifier;

	@UiField
	protected Label batchClassKeyLabel;

	@UiField
	protected Label batchClassMandatoryField;

	// @UiField
	// protected TextBox batchClassKeyTextBox;

	@UiField
	protected TextField batchClassKeyText;

	@UiField
	protected Label encryptionAlgoLabel;

	@UiField
	protected ComboBox encryptionAlgo;

	protected DialogWindow dialogWindow;

	@UiField
	protected VerticalPanel keyGeneratorVerticalPanel;

	private static final Binder BINDER = GWT.create(Binder.class);

	public KeyGeneratorView() {
		super();
		initWidget(BINDER.createAndBindUi(this));
		setIds();
		batchClassKeyLabel.setText(LocaleDictionary.getConstantValue(AdminConstants.BATCH_CLASS_KEY_LABEL));
		// batchClassKeyLabel.setStyleName(CoreCommonConstants.MARGIN_TOP_5_CSS);
		encryptionAlgoLabel.setText(LocaleDictionary.getConstantValue(AdminConstants.ENCRYPTION_ALGO_LABEL));
		encryptionAlgo.setName("encryptionAlgo");
		// encryptionAlgo.setWidth("146px");
		encryptionAlgo.add(AdminConstants.ENCRYPTION_ALGO_NONE);
		encryptionAlgo.add(AdminConstants.ENCRYPTION_ALGO_AES_128);
		encryptionAlgo.add(AdminConstants.ENCRYPTION_ALGO_AES_256);
		this.encryptionAlgo.setEditable(false);
		final String emptyMessage = LocaleDictionary.getMessageValue(BatchClassMessages.VALUE_CANNOT_BE_EMPTY);
		batchClassKeyText.addValidator(new EmptyValueValidator(emptyMessage));
		// batchClassKeyText.setStyleName(StyleConstant.BATCH_CLASS_KEY_TEXT_BOXX_STYLE);
		batchClassMandatoryField.setText(AdminConstants.STAR);
		batchClassMandatoryField.setStyleName(CoreCommonConstants.FONT_RED_CSS);
		this.encryptionAlgo.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				setDisabledKeyTextBox();
			}
		});
		encryptionAlgo.setValue(encryptionAlgo.getStore().get(0), true);
		setDisabledKeyTextBox();
	}

	public void setIds() {
		WidgetUtil.setID(batchClassKeyText, "batchClassKeyId");
		WidgetUtil.setID(encryptionAlgo, "encryptionAlgoId");
	}
	public void onGenerateButtonClick() {

		// Adding trim for batch class key as white spaces are not allowed for batch class key.
		String batchClassKey = batchClassKeyText.getText().trim();
		String selectedEncryptionAlgo = encryptionAlgo.getValue();
		presenter.generateKey(batchClassKey, selectedEncryptionAlgo, getBatchClassIdentifier());
	}

	public void setDisabledKeyTextBox() {
		if (AdminConstants.ENCRYPTION_ALGO_NONE.equals(encryptionAlgo.getValue())) {
			batchClassKeyText.setEnabled(false);
			batchClassMandatoryField.setVisible(false);
			clearBatchClassKeyText();
		} else {
			batchClassKeyText.setEnabled(true);
			batchClassMandatoryField.setVisible(true);
		}
	}

	/**
	 * @return the batchClassKeyTextBox
	 */
	public TextField getBatchClassKeyTextBox() {
		return batchClassKeyText;
	}

	/**
	 * @return the keyGeneratorVerticalPanel
	 */
	public VerticalPanel getKeyGeneratorVerticalPanel() {
		return keyGeneratorVerticalPanel;
	}

	/**
	 * @param keyGeneratorVerticalPanel the keyGeneratorVerticalPanel to set
	 */
	public void setKeyGeneratorVerticalPanel(VerticalPanel keyGeneratorVerticalPanel) {
		this.keyGeneratorVerticalPanel = keyGeneratorVerticalPanel;
	}

	/**
	 * @return the encryptionAlgo
	 */
	public ComboBox getEncryptionAlgo() {
		return encryptionAlgo;
	}

	/**
	 * @return the batchClassIdentifier
	 */
	public String getBatchClassIdentifier() {
		return batchClassIdentifier;
	}

	/**
	 * @param batchClassIdentifier2 the batchClassIdentifier to set
	 */
	public void setBatchClassIdentifier(String batchClassIdentifier) {
		this.batchClassIdentifier = batchClassIdentifier;
	}

	/**
	 * @return the dialogWindow
	 */
	public DialogWindow getDialogWindow() {
		return dialogWindow;
	}

	/**
	 * @param dialogWindow the dialogWindow to set
	 */
	public void setDialogWindow(DialogWindow dialogWindow) {
		this.dialogWindow = dialogWindow;
	}

	@Override
	public void initialize() {
	}

	public void clearBatchClassKeyText() {
		this.batchClassKeyText.clear();
	}

	public void setSelectedEncryptionValue(final int index) {
		if (null != encryptionAlgo && encryptionAlgo.getStore().size() != 0) {
			final ListStore<String> encryptionComboBoxValues = encryptionAlgo.getStore();
			encryptionAlgo.setValue(encryptionComboBoxValues.get(index), true);
		}

	}
}
