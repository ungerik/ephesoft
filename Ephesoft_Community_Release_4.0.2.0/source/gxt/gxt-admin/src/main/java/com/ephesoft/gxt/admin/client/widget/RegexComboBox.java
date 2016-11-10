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

package com.ephesoft.gxt.admin.client.widget;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.view.regexPool.RegexGroupSelectionGridView;
import com.ephesoft.gxt.admin.client.view.regexbuilder.RegexBuilderView;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.property.Validatable;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.StringComboBox;

public class RegexComboBox extends StringComboBox implements Validatable {

	private RegexBuilderView regexBuilderView;

	private RegexGroupSelectionGridView regexGroupSelectionGridView;

	private String previousPattern;

	private RegexComboBoxHandler regexComboBoxHandler;

	public RegexComboBox() {
		super();
		this.add(LocaleDictionary.getConstantValue(BatchClassConstants.REGEX_BUILDER));
		this.add(LocaleDictionary.getConstantValue(BatchClassConstants.REGEX_POOL));
		this.addSelectionHandler();
		setToolTip();
		this.setTriggerAction(TriggerAction.ALL);
		previousPattern = BatchClassConstants.EMPTY_STRING;

		this.addBeforeSelectionHandler(new BeforeSelectionHandler<String>() {

			@Override
			public void onBeforeSelection(BeforeSelectionEvent<String> event) {

				String text = getText();
				if (!StringUtil.isNullOrEmpty(text)) {
					previousPattern = text;
				} else {
					previousPattern = BatchClassConstants.EMPTY_STRING;
				}

			}

		});
	}

	private void setToolTip() {
		final ListView<String, ?> listView = this.getCell().getListView();
		listView.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {

				Element target = event.getNativeEvent().getEventTarget().<Element> cast();
				if (null != target) {
					target = listView.findElement(target);
					if (target != null) {
						int index = listView.indexOf(target);
						if (index != -1) {
							listView.setTitle(listView.getStore().get(index));
						}
					}
				}
			}
		}, MouseMoveEvent.getType());
	}

	private void addSelectionHandler() {
		this.addSelectionHandler(new SelectionHandler<String>() {

			@Override
			public void onSelection(final SelectionEvent<String> event) {

				final String previousText = getText();
				final String newPattern = previousText;
				if (null != newPattern) {
					setText(previousPattern);
					int index = getStore().indexOf(newPattern);

					if (index == 0) {
						// if (newPattern.equalsIgnoreCase(BatchClassConstants.REGEX_BUILDER)) {
						regexBuilderView.setRegexComboBoxHandler(getRegexComboBoxHandler());
						regexBuilderView.showDialogWindow(previousPattern);

					} else if (index == 1
					/* newPattern.equalsIgnoreCase(BatchClassConstants.REGEX_POOL) */) {
						regexGroupSelectionGridView.setRegexComboBoxHandler(getRegexComboBoxHandler());
						regexGroupSelectionGridView.getPresenter().bind();
					}
				}
			}
		});
	}

	@Override
	public void enableValidation(boolean enable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void appendValue(String value, boolean append) {

	}

	@Override
	public void blur() {
		// super.blur();

	}

	public RegexComboBoxHandler getRegexComboBoxHandler() {
		return regexComboBoxHandler;
	}

	public void setRegexComboBoxHandler(RegexComboBoxHandler regexComboBoxHandler) {
		this.regexComboBoxHandler = regexComboBoxHandler;
	}

	public void setComponents(final RegexBuilderView regexBuilderView, final RegexGroupSelectionGridView regexGroupSelectionGridView) {
		this.regexBuilderView = regexBuilderView;
		this.regexGroupSelectionGridView = regexGroupSelectionGridView;
	}

}
