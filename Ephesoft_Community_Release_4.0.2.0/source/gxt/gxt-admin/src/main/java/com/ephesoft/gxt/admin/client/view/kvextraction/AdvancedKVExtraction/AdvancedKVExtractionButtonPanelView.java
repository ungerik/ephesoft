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

import java.util.ArrayList;
import com.google.gwt.user.client.ui.Label;
import java.util.LinkedHashSet;
import java.util.List;

import com.ephesoft.gxt.admin.client.i18n.BatchClassConstants;
import com.ephesoft.gxt.admin.client.presenter.kvextraction.AdvancedKVExtraction.AdvancedKVExtractionButtonPanelPresenter;
import com.ephesoft.gxt.core.client.constant.CssConstants;
import com.ephesoft.gxt.core.client.i18n.LocaleDictionary;
import com.ephesoft.gxt.core.client.ui.widget.ComboBox;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuBar;
import com.ephesoft.gxt.core.client.ui.widget.CustomMenuItem;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;

public class AdvancedKVExtractionButtonPanelView extends AdvancedKVExtractionInlineView<AdvancedKVExtractionButtonPanelPresenter> {

	interface Binder extends UiBinder<Widget, AdvancedKVExtractionButtonPanelView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected HorizontalPanel advKVMenuPanel;
	@UiField
	protected CustomMenuBar advKVExtractionMenuBar;
	@UiField
	protected ComboBox pageNumberComboBox;
	@UiField
	protected ComboBox imageComboBox;

	@UiField
	protected Label image;
	@UiField
	protected Label pageNo;
	
	protected CustomMenuItem testAdvKVMenuItem;

	protected CustomMenuItem clearMenuItem;

	protected CustomMenuItem cancelMenuItem;
	protected CustomMenuItem applyMenuItem;
	@UiField
	protected ToggleButton viewOCRButton;
	private String imageUrl;
	private boolean isSinglePage;
	private int previousSelectedValue = -1;

	public AdvancedKVExtractionButtonPanelView() {
		super();
		initWidget(binder.createAndBindUi(this));
		intializeMenuItems();
		advKVExtractionMenuBar.addItem(applyMenuItem);
		advKVExtractionMenuBar.addItem(testAdvKVMenuItem);
		advKVExtractionMenuBar.addItem(clearMenuItem);
		advKVExtractionMenuBar.addItem(cancelMenuItem);
		imageComboBox.setEditable(false);
		pageNumberComboBox.setEditable(false);
		pageNumberComboBox.addStyleName("advKvPageNumber");
//		pageNumberComboBox.setWidth(50);
		addMenuItemActionEvents();
		addComboBoxActionEvents();
		addToggleButtonActionEvents();
		advKVMenuPanel.addStyleName(CssConstants.CSS_ADV_KV_MENU_PANEL);
		advKVExtractionMenuBar.addStyleName(CssConstants.ADV_KV_MENU_BAR);
		WidgetUtil.setID(advKVExtractionMenuBar, "advKVExtractionMenuBar");
		viewOCRButton.setStylePrimaryName("testExtractionTogglerButton");
		
		image.setText(LocaleDictionary.getConstantValue(BatchClassConstants.IMAGE));
		pageNo.setText(LocaleDictionary.getConstantValue(BatchClassConstants.PAGE_NO));
		
	}

	private void addToggleButtonActionEvents() {
		viewOCRButton.getUpFace().setText(LocaleDictionary.getConstantValue(BatchClassConstants.VIEW_OCR_DATA));
		viewOCRButton.getDownFace().setText(LocaleDictionary.getConstantValue(BatchClassConstants.OCR_DATA));

		viewOCRButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				presenter.onViewHOCRDataSelected(imageComboBox.getValue(), viewOCRButton.isDown());
			}
		});

	}

	private void addComboBoxActionEvents() {
		imageComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				presenter.populatePages(imageComboBox.getValue());

			}
		});

		pageNumberComboBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				int index = StringUtil.getIntegerValue(pageNumberComboBox.getValue());
				presenter.setImageNameInDTO(imageComboBox.getValue(), index);
				previousSelectedValue = index;
				presenter.enabledisablePageButtons(index != pageNumberComboBox.getStore().size(), index != 1);

			}
		});

	}

	private void addMenuItemActionEvents() {

		applyMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.onApplyButtonClicked();

			}
		});
		testAdvKVMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.testAdvKV();
			}
		});

		clearMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				presenter.clearAdvKV();
			}
		});
		cancelMenuItem.setScheduledCommand(new ScheduledCommand() {

			@Override
			public void execute() {
				clearComboBoxValues();
				if (viewOCRButton.isDown()) {
					viewOCRButton.setValue(false, true);
				}
				presenter.exitAdvKVScreen();
			}
		});

	}

	@SuppressWarnings("serial")
	private void intializeMenuItems() {
		applyMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.APPLY);
			}
		});
		testAdvKVMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.TEST_ADVANCED_KV);
			}
		});

		clearMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.CLEAR);
			}
		});
		cancelMenuItem = new CustomMenuItem(new SafeHtml() {

			@Override
			public String asString() {
				return LocaleDictionary.getConstantValue(BatchClassConstants.CANCEL);
			}
		});

	}

	@Override
	public void initialize() {
	}

	public void setDataAndReloadPage(LinkedHashSet<String> newFiles) {
		if (null != newFiles && newFiles.size() != 0) {
			setimageComboBoxValues(newFiles);
		}
	}

	public void setSelectedimageComboBoxValue(final int index) {
		if (null != imageComboBox && imageComboBox.getStore().size() != 0) {
			final ListStore<String> imageComboBoxValues = imageComboBox.getStore();
			imageComboBox.setValue(imageComboBoxValues.get(index), true);
		}
	}

	public void setimageComboBoxValues(final LinkedHashSet<String> newFiles) {
		int index = imageComboBox.getStore().size();
		if (!CollectionUtil.isEmpty(newFiles)) {
			String file = "";
			for (String newfile : newFiles) {
				if (!imageComboBox.getStore().getAll().contains(newfile)) {
					imageComboBox.add(newfile);
				} else {
					file = newfile;
				}
			}
			if (!StringUtil.isNullOrEmpty(file)) {
				if (imageComboBox.getValue().equals(file)) {
					ValueChangeEvent.fire(imageComboBox, file);
				}
				else{
					index=imageComboBox.getStore().indexOf(file);
					setSelectedimageComboBoxValue(index);
				}
			} else if (index < imageComboBox.getStore().size()) {
				setSelectedimageComboBoxValue(index);
			}
			imageComboBox.finishEditing();
		}
	}

	public void populatePageListBox(final int pageCount) {
		List<String> newItems = new ArrayList<String>();
		for (int i = 1; i <= pageCount; i++)
			newItems.add(String.valueOf(i));
		ListStore<String> listStore = pageNumberComboBox.getStore();
		listStore.replaceAll(newItems);
		pageNumberComboBox.setStore(listStore);
		setSelectedpageComboBoxValue(0);
		pageNumberComboBox.finishEditing();

	}

	public void setSelectedpageComboBoxValue(final int index) {
		if (previousSelectedValue == 1 && index == 0) {
			presenter.setImageNameInDTO(imageComboBox.getValue(), 1);
			presenter.enabledisablePageButtons(1 != pageNumberComboBox.getStore().size(), false);

		}
		if (null != pageNumberComboBox && pageNumberComboBox.getStore().size() != 0) {
			final ListStore<String> imageComboBoxValues = pageNumberComboBox.getStore();
			pageNumberComboBox.setValue(imageComboBoxValues.get(index), true);
		}
	}

	public void onNextPreviousButtonClicked(boolean isNext) {
		if (viewOCRButton.isDown()) {
			viewOCRButton.setValue(false, true);
		}
		int index = pageNumberComboBox.getSelectedIndex();
		if (isNext && (index + 1) < pageNumberComboBox.getStore().size()) {
			setSelectedpageComboBoxValue(++index);
		} else if (!isNext && index != 0) {
			setSelectedpageComboBoxValue(--index);
		}
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public boolean isSinglePage() {
		return isSinglePage;
	}

	public void setSinglePage(boolean isSinglePage) {
		this.isSinglePage = isSinglePage;
	}

	public void disableAllButtons() {
		enableDisableMenuButtons(false, false, false);
		viewOCRButton.setEnabled(false);
	}

	public void enableAllButtons() {
		enableDisableMenuButtons(true, true, true);
		viewOCRButton.setEnabled(true);
	}

	public void enableDisableMenuButtons(boolean apply, boolean testAdvKV, boolean clear) {
		applyMenuItem.setEnabled(apply);
		testAdvKVMenuItem.setEnabled(testAdvKV);
		clearMenuItem.setEnabled(clear);
	}

	public void enableHOCRButton(boolean isEnabled) {
		viewOCRButton.setEnabled(isEnabled);
	}

	public void setClearButtonStatus(boolean status) {
		clearMenuItem.setEnabled(status);
	}

	public void setTestAdvButonStatus(boolean status) {
		testAdvKVMenuItem.setEnabled(status);
	}

	public void clearComboBoxValues() {
		pageNumberComboBox.clear();
		imageComboBox.clear();
		pageNumberComboBox.getStore().clear();
		imageComboBox.getStore().clear();
	}

	public boolean isViewHOCRButtonDown() {
		return viewOCRButton.isDown();
	}

	public void setViewHOCRButtonstate(boolean state) {
		viewOCRButton.setValue(state, true);
	}

	public String getImageSelected() {
		return this.imageComboBox.getValue();
	}

	public void enableApplyButton(boolean enable) {
		applyMenuItem.setEnabled(enable);
	}

	public void setViewHOCRButtonstate(boolean state, boolean fireEvent){
		viewOCRButton.setValue(state, fireEvent);
	}
}
