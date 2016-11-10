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

package com.ephesoft.gxt.rv.client.view;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.DocField;
import com.ephesoft.gxt.core.client.DomainView;
import com.ephesoft.gxt.core.client.view.ListPanel;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.presenter.ValidationDetailPresenter;
import com.ephesoft.gxt.rv.client.view.batch.DLFView;
import com.ephesoft.gxt.rv.client.widget.ValidationTabPanel;
import com.ephesoft.gxt.rv.shared.DocFieldComparator;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class ValidationDetailView extends ReviewValidateBaseView<ValidationDetailPresenter> {

	interface Binder extends UiBinder<Widget, ValidationDetailView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected ValidationTabPanel tabPanel;

	private final Map<DocField, ListPanel<DocField, DLFView>> listPanelMap;

	public ValidationDetailView() {
		initWidget(binder.createAndBindUi(this));
		tabPanel.addStyleName("viewPort");
		tabPanel.addStyleName("validationTabPanel");
		tabPanel.setTabScroll(true);
		tabPanel.setAnimScroll(true);
		listPanelMap = new HashMap<DocField, ListPanel<DocField, DLFView>>();
		this.addTabSelectionHandler();
	}

	private void addTabSelectionHandler() {
		tabPanel.addSelectionHandler(new SelectionHandler<Widget>() {

			@Override
			public void onSelection(final SelectionEvent<Widget> event) {
				final Widget selectedWidget = event.getSelectedItem();
				if (selectedWidget instanceof ListPanel<?, ?>) {
					final ListPanel<?, ?> panel = ((ListPanel<?, ?>) selectedWidget);
					setDefaultFocus(panel);
				}
			}
		});
	}

	@Override
	public void initialize() {
	}

	public void clear() {
		tabPanel.clear();
	}

	public void addCategory(final String categoryName, final List<DocField> dlfListing) {
		if (!StringUtil.isNullOrEmpty(categoryName) && !CollectionUtil.isEmpty(dlfListing)) {
			Collections.sort(dlfListing, new DocFieldComparator());
			final ListPanel<DocField, DLFView> dlfListPanel = new ListPanel<DocField, DLFView>();
			dlfListPanel.addStyleName("viewPort");
			dlfListPanel.setLayoutData(new VerticalLayoutData(0.85, 0.99));
			dlfListPanel.addStyleName("docFieldListPanel");
			tabPanel.add(dlfListPanel, categoryName);
			DLFView dlfView;
			DocField lastAddedField = null;	
			for (final DocField docLevelField : dlfListing) {
				if (null != docLevelField && !docLevelField.isHidden()) {
					listPanelMap.put(docLevelField, dlfListPanel);
					dlfView = new DLFView(docLevelField);
					dlfListPanel.add(dlfView);
					if (null != docLevelField) {
						lastAddedField = docLevelField;
					}
				}
			}
			if (null != lastAddedField) {
				this.validateListPanel(lastAddedField);
			}
		}
	}

	public void validateListPanel(final Object bindedObject) {
		final ListPanel<?, ?> bindedListView = listPanelMap.get(bindedObject);
		if (null != bindedListView) {
			final List<? extends DomainView<?, ?>> domainViewList = bindedListView.getDomainViewList();
			boolean isValid = true;
			if (!CollectionUtil.isEmpty(domainViewList)) {
				for (final DomainView<?, ?> domainView : domainViewList) {
					if (null != domainView && !domainView.isValid()) {
						isValid = false;
						break;
					}
				}
				tabPanel.setValid(bindedListView, isValid);
			}
		}
	}

	public boolean isValid() {
		return tabPanel.isValid() && presenter.isTableViewValid();
	}

	public void setDefaultFocus() {
		final ListPanel<?, ?> panel = getFirstInvalidTab();
		if (panel != null) {
			tabPanel.setActiveWidget(panel);
			setDefaultFocus(panel);
		}

	}

	private ListPanel<?, ?> getFirstInvalidTab() {
		return tabPanel.getFirstInvalidListView();
	}

	private void setDefaultFocus(final ListPanel<?, ?> panel) {
		final DomainView<?, ?> defaultDomainView = panel == null ? null : panel.getFirstView(true);
		if (null != defaultDomainView) {
			defaultDomainView.focus();
		}
	}

	public void locateSubSequentErrorField(final DocField field, final boolean isPrevious, final boolean onlyErrorView) {
		if (null != field) {
			ListPanel<DocField, DLFView> listPanel = listPanelMap.get(field);
			if (null != listPanel) {
				DLFView view = listPanel.getNextView(field, isPrevious, onlyErrorView);
				if (null == view) {
					ListPanel<?, ?> nextTab = tabPanel.getSubSequentListView(listPanel, isPrevious, onlyErrorView);
					if (null != nextTab) {
						tabPanel.setActiveWidget(nextTab);
						DomainView<?, ?> invalidView = isPrevious ? nextTab.getLastView(onlyErrorView) : nextTab
								.getFirstView(onlyErrorView);
						if (null != invalidView) {
							invalidView.focus();
						}
					}
				} else {
					view.focus();
				}
			}
		}
	}
}
