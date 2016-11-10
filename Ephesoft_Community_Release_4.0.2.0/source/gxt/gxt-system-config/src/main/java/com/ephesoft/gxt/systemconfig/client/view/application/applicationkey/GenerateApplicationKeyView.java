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

package com.ephesoft.gxt.systemconfig.client.view.application.applicationkey;

import java.util.Random;

import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.systemconfig.client.i18n.SystemConfigConstants;
import com.ephesoft.gxt.systemconfig.client.presenter.application.applicationkey.GenerateApplicationKeyPresenter;
import com.ephesoft.gxt.systemconfig.client.view.SystemConfigInlineView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class GenerateApplicationKeyView extends
		SystemConfigInlineView<GenerateApplicationKeyPresenter> {

	interface Binder extends UiBinder<Widget, GenerateApplicationKeyView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected VerticalLayoutContainer vPanel;

	@UiField
	protected HorizontalLayoutContainer hPanel;

	@UiField
	protected Label keyGenerationLabel;

	@UiField
	protected TextBox keyTextBox;

	public GenerateApplicationKeyView() {
		super();
		initWidget(binder.createAndBindUi(this));
		setWidgetIds();
		initialiseView();
		vPanel.addStyleName("keyGenerationVerticalPanel");
		hPanel.addStyleName("keyGenerationHorizontalPanel");
	}

	private void initialiseView() {
		final String applicationKeyLabel = LocaleDictionary
				.getConstantValue(SystemConfigConstants.APPLICATION_KEY_LABEL);
		keyGenerationLabel.setText(applicationKeyLabel);
	}

	/**
	 * Disables all the fields responsible for key generation.
	 */
	public void disableKeyGeneratorView() {
		this.keyTextBox.setEnabled(false);
		Random random = new Random();
		int number = random.nextInt(30);
		while (number < 7) {
			number = random.nextInt(30);
		}
		String keyToShow = getEncryptedKey("*", number);
		this.keyTextBox.setText(keyToShow);
		presenter.disableGenerateKeyMenuItem(false);
	}

	public String getEncryptedKey(String key, int repeatTimes) {
		StringBuilder encryptedKey = new StringBuilder();
		while (repeatTimes-- > 0) {
			encryptedKey.append(key);
		}
		return encryptedKey.toString();
	}

	public String getUserEnteredKey() {
		return this.keyTextBox.getText().trim();
	}

	private void setWidgetIds() {
		WidgetUtil.setID(vPanel, "GAKV_verticalPanel");
		WidgetUtil.setID(keyGenerationLabel, "GAKV_keyLabel");
		WidgetUtil.setID(keyTextBox, "GAKV_keyTextBox");
		WidgetUtil.setID(hPanel, "GAKV_horizontalPanel");
	}
}
