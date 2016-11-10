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

package com.ephesoft.gxt.rv.client.view.menu;

import java.util.Collections;
import java.util.List;

import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleConstant;
import com.ephesoft.gxt.rv.client.presenter.menu.DocumentOptionsPresenter;
import com.ephesoft.gxt.rv.client.view.ReviewValidateBaseView;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ListView;

public class DocumentOptionsView extends ReviewValidateBaseView<DocumentOptionsPresenter> {

	interface Binder extends UiBinder<Widget, DocumentOptionsView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected ComboBox documentTypeComboBox;

	@UiField
	protected Label documentTypeLabel;

	@Override
	public void initialize() {
	}

	public DocumentOptionsView() {
		initWidget(binder.createAndBindUi(this));
		documentTypeLabel.setText(LocaleConstant.DOCUMENT_TYPE_LABEL);
		this.addStyleName("documentOptionsView");
		WidgetUtil.setID(documentTypeComboBox, "rv-documentType-comboBox");
		documentTypeComboBox.setFinishEditOnEnter(true);
		documentTypeComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				Timer timer = new Timer() {

					public void run() {
						String documentTypeName = documentTypeComboBox.getText();
						if (ReviewValidateNavigator.isValidDocumentType(documentTypeName, true)) {
							presenter.setDocumentType(documentTypeName);
						}
					};
				};
				timer.schedule(100);
			}
		});
		resizeDocumentTypeList();
	}

	private void resizeDocumentTypeList() {
		final ListView<String, ?> listView = documentTypeComboBox.getListView();
		listView.addAttachHandler(new Handler() {

			@Override
			public void onAttachOrDetach(AttachEvent event) {
				Timer timer = new Timer() {

					@Override
					public void run() {
						listView.setWidth(320);
						listView.setBorders(true);
					}
				};
				timer.schedule(30);
			}
		});
	}

	public void setDocumentTypeComboWidth(int width) {
		documentTypeComboBox.setWidth(width);
	}

	public void addDocumentTypes(final List<String> documentTypeList) {
		documentTypeComboBox.clear();
		Collections.sort(documentTypeList);
		documentTypeComboBox.getStore().clear();
		if (null != documentTypeList) {
			documentTypeComboBox.add(documentTypeList);
		}
	}

	public void selectDocumentType(final String documentType) {
		if (null != documentType) {
			documentTypeComboBox.finishEditing();
			documentTypeComboBox.setValue(documentType, true, true);
		}
	}

	public void setFocusOnDocumentTypeField() {
		documentTypeComboBox.focus();
	}

	public void selectDocumentTypeText() {
		Timer timer = new Timer() {

			public void run() {
				documentTypeComboBox.selectText();
			}
		};
		timer.schedule(100);
	}

	public void collapseDocumentTypeSuggestions() {
		documentTypeComboBox.collapse();
	}

}
