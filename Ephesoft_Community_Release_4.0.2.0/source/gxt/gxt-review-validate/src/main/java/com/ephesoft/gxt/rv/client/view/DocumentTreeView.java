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

import java.util.List;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.gxt.core.client.ui.widget.util.HTMLDomUtil;
import com.ephesoft.gxt.core.client.util.WidgetUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.rv.client.constant.locale.LocaleConstant;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DefaultDocumentOpenEvent;
import com.ephesoft.gxt.rv.client.presenter.DocumentTreePresenter;
import com.ephesoft.gxt.rv.client.widget.DocumentTree;
import com.ephesoft.gxt.rv.client.widget.ThumbnailWidgetPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class DocumentTreeView extends ReviewValidateBaseView<DocumentTreePresenter> {

	interface Binder extends UiBinder<Component, DocumentTreeView> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	protected Button draggableImageSwitchButton;

	@UiField
	protected ScrollPanel treeScroll;

	@UiField
	protected DocumentTree documentTree;

	@UiField
	protected VerticalPanel documentViewPanel;

	@UiField
	protected ContentPanel documentTreeHeader;

	private boolean treeScrollVisible = true;

	@Override
	public void initialize() {
	}

	public DocumentTreeView() {
		initWidget(binder.createAndBindUi(this));
		documentTreeHeader.addStyleName("documentTreeHeader");
		documentTree.addStyleName("thumbnail-tree");
		draggableImageSwitchButton.addStyleName("iconButton");
		draggableImageSwitchButton.addStyleName("dragPanelSwitch");
		WidgetUtil.setID(draggableImageSwitchButton, "v-review-Panel-Button");
		draggableImageSwitchButton.setTitle(LocaleConstant.DOCUMENT_PAGE_VIEW_TITLE);
		if (GXT.isIE()) {
			int clientHeight = Window.getClientHeight();
			documentTree.setHeight((clientHeight-70) + Unit.PX.getType() ); 
		}
		if (HTMLDomUtil.isIE11()) {
			documentViewPanel.addStyleName("documentViewIE11");
			treeScroll.addStyleName("documentTreeScrollIE11");
		}
	}

	public void createDocumentTree(final List<String> documentIdentifiersList) {
		documentTree.clear();
		if (!CollectionUtil.isEmpty(documentIdentifiersList)) {
			ThumbnailWidgetPanel thumbnailWidgetPanel = null;
			int itemIndex = 0;
			for (final String documentIdentifier : documentIdentifiersList) {
				thumbnailWidgetPanel = new ThumbnailWidgetPanel(documentIdentifier);
				documentTree.addThumbnail(thumbnailWidgetPanel);
				this.addLazyLoadHandler(itemIndex, thumbnailWidgetPanel);
				itemIndex++;
			}
			ReviewValidateEventBus.fireEvent(new DefaultDocumentOpenEvent());
		}
	}

	public void addLazyLoadHandler(final int itemIndex, final ThumbnailWidgetPanel thumbnailPanel) {
		if (itemIndex < 3 && thumbnailPanel.getDocument() == null) {
			presenter.loadThumbnail(thumbnailPanel);
		} else {
			treeScroll.addScrollHandler(new ScrollHandler() {

				@Override
				public void onScroll(final ScrollEvent event) {
					final int thumbnailTop = thumbnailPanel.getAbsoluteTop();
					final int scrollPanelViewPort = treeScroll.getAbsoluteTop() + treeScroll.getOffsetHeight();
					if (thumbnailTop < scrollPanelViewPort && thumbnailPanel.getDocument() == null
							&& thumbnailTop > treeScroll.getAbsoluteTop()) {
						presenter.loadThumbnail(thumbnailPanel);
					}
				}
			});
		}
	}

	public void openDocument(final String documentIdentifier) {
		documentTree.selectThumbnail(documentIdentifier);
	}

	public void addThumbnail(final ThumbnailWidgetPanel thumbnailPanel, final int index) {
		documentTree.addThumbnail(thumbnailPanel, index);
	}

	public void refreshDocument(final String docIdentifier) {
		documentTree.refreshDocument(docIdentifier);
	}

	public void updateDocument(final Document document) {
		documentTree.updateDocument(document);
	}

	public void scrollInToView(final Document document) {
		documentTree.scrollInToView(document);
	}

	public ThumbnailWidgetPanel getThumbnailStackPanel(final String documentIdentifier) {
		return documentTree.getThumbnailStackPanel(documentIdentifier);
	}

	public void removeDocument(final String documentIdentifier) {
		documentTree.removeDocument(documentIdentifier);
	}

	public void setValidDocumentType(final String documentIdentifier, final boolean isValid) {
		documentTree.setValidDocumentType(documentIdentifier, isValid);
	}

	public void setDocumentType(final String documentIdentifier, final String documentType) {
		documentTree.setDocumentType(documentIdentifier, documentType);
	}

	@UiHandler("draggableImageSwitchButton")
	public void addTreeViewToggleHandler(final ClickEvent clickEvent) {
		if (treeScrollVisible) {
			documentViewPanel.remove(treeScroll);
			documentViewPanel.add(presenter.getReviewDetailView());
		} else {
			documentViewPanel.add(treeScroll);
			documentViewPanel.remove(presenter.getReviewDetailView());
			fireScrollEventToLoadDocument();
		}
		treeScrollVisible = !treeScrollVisible;
	}

	public void resetView() {
		documentViewPanel.add(treeScroll);
		documentViewPanel.remove(presenter.getReviewDetailView());
		treeScrollVisible = true;
	}

	public void fireScrollEventToLoadDocument() {
		treeScroll.setVerticalScrollPosition(treeScroll.getVerticalScrollPosition() - 1);
		treeScroll.setVerticalScrollPosition(treeScroll.getVerticalScrollPosition() + 1);
	}

	public void setEnableDragImageSwitch(final boolean enabled) {
		draggableImageSwitchButton.setVisible(enabled);
	}
}
