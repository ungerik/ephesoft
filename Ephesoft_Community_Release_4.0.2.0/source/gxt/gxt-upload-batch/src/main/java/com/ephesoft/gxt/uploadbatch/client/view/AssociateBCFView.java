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

import com.ephesoft.gxt.core.client.View;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.DialogWindow;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogAdapter;
import com.ephesoft.gxt.core.client.ui.widget.listener.DialogListener;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.client.validator.form.RegexMatcherValidator;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.dto.BatchClassFieldDTO;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchConstants;
import com.ephesoft.gxt.uploadbatch.client.i18n.UploadBatchMessages;
import com.ephesoft.gxt.uploadbatch.client.presenter.AssociateBCFPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.event.FocusEvent;
import com.sencha.gxt.widget.core.client.event.FocusEvent.FocusHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.ephesoft.gxt.core.client.i18n.CoreCommonConstants;

public class AssociateBCFView extends View<AssociateBCFPresenter> {

	interface Binder extends UiBinder<Widget, AssociateBCFView> {
	}

	@UiField(provided = true)
	protected FlexTable flexEditTable;

	private ScrollPanel scrollPanel;

	private Label validationMessage;

	private FlexTable editTable;

	private DialogWindow dialogBox;

	private static final Binder binder = GWT.create(Binder.class);

	public AssociateBCFView() {
		initWidget(binder.createAndBindUi(this));
		validationMessage = new Label();
		editTable = new FlexTable();
		this.validationMessage.addStyleName(CoreCommonConstants.ERROR_STYLE_CSS);
	}

	public FlexTable getFlexEditTable() {
		return flexEditTable;
	}

	public void setView() {
		dialogBox.setDialogListener(new DialogAdapter() {

			@Override
			public void onOkClick() {
				presenter.onSave();
			}

			@Override
			public void onCloseClick() {
				dialogBox.hide();
			}

			@Override
			public void onCancelClick() {
				dialogBox.hide();
			}
		});
		scrollPanel = new ScrollPanel();
		setValidationMessage(CoreCommonConstants.EMPTY_STRING);
		editTable = new FlexTable();
		editTable.setWidth(CoreCommonConstants._100_PERCENTAGE);
		scrollPanel.add(editTable);
		flexEditTable.setWidget(1, 0, scrollPanel);
		flexEditTable.setWidget(0, 0, validationMessage);
		flexEditTable.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
	}

	public void formatRow(final int row) {
		editTable.getCellFormatter().setWidth(row, 0, CoreCommonConstants._50_PERCENTAGE);
		editTable.getFlexCellFormatter().addStyleName(row, 0, CoreCommonConstants.BOLD_TEXT_CSS);
		editTable.getFlexCellFormatter().setAlignment(row, 0, HasHorizontalAlignment.ALIGN_RIGHT, HasVerticalAlignment.ALIGN_MIDDLE);
		// editTable.getCellFormatter().setWidth(row, 1, "1%");
		editTable.getCellFormatter().setWidth(row, 1, CoreCommonConstants._50_PERCENTAGE);
		editTable.getFlexCellFormatter().addStyleName(row, 0, CoreCommonConstants.BOLD_TEXT_CSS);
	}

	public void addWidget(final int row, final int column, final Widget widget) {
		editTable.setWidget(row, column, widget);
	}

	public void addWidgetStar(final int row, final int column) {
		final Label star = new Label(UploadBatchConstants.STAR);
		editTable.setWidget(row, column, star);
		star.setStyleName(CoreCommonConstants.FONT_RED_CSS);
	}

	public TextField addTextBox(final BatchClassFieldDTO batchClassFieldDTO) {
		final TextField textBox = new TextField();
		WidgetUtil.setID(textBox, StringUtil.concatenate(batchClassFieldDTO.getName(), UploadBatchConstants.TEXT_BOX));
		//textBox.setText(batchClassFieldDTO.getValue());
		textBox.setValue(batchClassFieldDTO.getValue(),true);
		if (!StringUtil.isNullOrEmpty(batchClassFieldDTO.getValidationPattern())) {
			textBox.addValidator(new RegexMatcherValidator(batchClassFieldDTO.getValidationPattern()));
		}

		textBox.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				String validationMessage = CoreCommonConstants.EMPTY_STRING;
				if (!StringUtil.isNullOrEmpty(batchClassFieldDTO.getSampleValue())) {
					validationMessage = StringUtil.concatenate(
							LocaleDictionary.getLocaleDictionary().getMessageValue(
									UploadBatchMessages.BCF_VALIDATION_REGEX_TYPE_MESSAGE), batchClassFieldDTO.getSampleValue());
				}
				setValidationMessage(validationMessage);
			}
		});
		return textBox;
	}

	public ComboBox addDropDown(final BatchClassFieldDTO batchClassFieldDTO) {
		final ComboBox fieldValue = new ComboBox();
		fieldValue.setEditable(false);
		String firstValue = null;
		WidgetUtil.setID(fieldValue, UploadBatchConstants.COMBO_BOX);
		final String[] selectedValue = batchClassFieldDTO.getFieldOptionValueList().split(CoreCommonConstant.SEMI_COLON);
		for (final String item : selectedValue) {
			if (!item.trim().isEmpty()) {
				if (null == firstValue) {
					firstValue = item;
				}
				fieldValue.add(item);
			}
		}

		if (StringUtil.isNullOrEmpty(fieldValue.getValue())) {
			fieldValue.setValue(firstValue, true, true);
		}
		fieldValue.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				setValidationMessage(CoreCommonConstants.EMPTY_STRING);
			}
		});
		return fieldValue;
	}

	public DialogWindow getDialogBox() {
		return dialogBox;
	}

	public void setDialogBox(final DialogWindow dialogBox) {
		this.dialogBox = dialogBox;
		dialogBox.setResize(true);
		
	}

	public String getValidationMessage() {
		return validationMessage.getText();
	}

	public void setValidationMessage(final String validationMessage) {
		this.validationMessage.setText(validationMessage);
	}

	public ScrollPanel getScrollPanel() {
		return scrollPanel;
	}

	@Override
	public void initialize() {
		flexEditTable = new FlexTable() {

			@Override
			protected void onLoad() {
				super.onLoad();
				if (null != dialogBox) {
					Timer timer = new Timer() {
						
						@Override
						public void run() {
							int flexTableHeight = flexEditTable.getOffsetHeight();
							if (flexTableHeight > 200){
								scrollPanel.setHeight(CoreCommonConstants._200PX);
							}
							dialogBox.setHeight(flexEditTable.getOffsetHeight() + 100);
//							dialogBox.setPixelSize(flexEditTable.getOffsetWidth(), flexEditTable.getOffsetHeight() + 100);
						}
					};
					timer.schedule(50);
				}
			}
		};
	}

	/**
	 * Toggles the visibility of the dialogue box.
	 * 
	 * @param hide boolean- takes value true or false depending on whether the dialog box is needed to be displayed or not
	 */
	public void hideDialogBox(boolean hide) {
		if (hide) {
			this.dialogBox.hide();
		} else {
			this.dialogBox.show();
		}
	}
}
