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

package com.ephesoft.gxt.rv.client.widget;

import java.util.HashMap;
import java.util.Map;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.gxt.core.client.WidgetTree;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.FocusInitializationEvent;
import com.ephesoft.gxt.rv.client.event.TreeItemSelectionEvent;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class DocumentTree extends WidgetTree<ThumbnailWidgetPanel> {

	private final Map<String, TreeItem> thumbnailDocumentMap;

	private TreeItem currentSelectedItem;

	private DocumentTree() {
		this.thumbnailDocumentMap = new HashMap<String, TreeItem>();
		this.addSelectionHandler();
		this.setScrollOnSelectEnabled(true);
		this.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(final FocusEvent event) {
				event.preventDefault();
				ReviewValidateEventBus.fireEvent(new FocusInitializationEvent());
			}
		});
	}

	private void addSelectionHandler() {
		this.addSelectionHandler(new SelectionHandler<TreeItem>() {

			@Override
			public void onSelection(final SelectionEvent<TreeItem> event) {
				if (null != event) {
					final TreeItem selectedItem = event.getSelectedItem();
					selectedItem.getElement().scrollIntoView();
					if (null != currentSelectedItem) {
						currentSelectedItem.removeStyleName("selectedTreeDocument");
					}
					selectedItem.addStyleName("selectedTreeDocument");
					currentSelectedItem = selectedItem;
					final Widget selectedWidget = selectedItem.getWidget();
					if (selectedWidget instanceof ThumbnailWidgetPanel) {
						final ThumbnailWidgetPanel selectedThumbnail = (ThumbnailWidgetPanel) selectedWidget;
						ReviewValidateEventBus.fireEvent(new TreeItemSelectionEvent(selectedThumbnail));
					}
				}
			}
		});
	}

	public TreeItem addThumbnail(final ThumbnailWidgetPanel thubnailWidget) {
		final TreeItem documentItem = super.addThumbnail(thubnailWidget);
		documentItem.addStyleName("treeDocument");
		if (null != thubnailWidget) {
			final String documentIdentifier = thubnailWidget.getDocIdentifier();
			thumbnailDocumentMap.put(documentIdentifier, documentItem);
			this.setValidDocumentType(documentIdentifier, ReviewValidateNavigator.isDocumentValid(documentIdentifier));
		}
		return documentItem;
	}

	public TreeItem addThumbnail(final ThumbnailWidgetPanel thumbnailPanel, final int index) {
		TreeItem documentItem;
		if (index > 0 && thumbnailDocumentMap.size() <= index) {
			documentItem = this.addThumbnail(thumbnailPanel);
		} else {
			documentItem = super.insertItem(index, thumbnailPanel);
			documentItem.addStyleName("treeDocument");
			final String documentIdentifier = thumbnailPanel.getDocIdentifier();
			thumbnailDocumentMap.put(documentIdentifier, documentItem);
			this.setValidDocumentType(documentIdentifier, ReviewValidateNavigator.isDocumentValid(documentIdentifier));
		}
		return documentItem;
	}

	public void selectThumbnail(final String docIdentifier) {
		if (!StringUtil.isNullOrEmpty(docIdentifier)) {
			final TreeItem documentItem = thumbnailDocumentMap.get(docIdentifier);
			if (null != documentItem) {
				super.setSelectedItem(documentItem);
			}
		}
	}

	public void refreshDocument(final String documentIdentifier) {
		if (!StringUtil.isNullOrEmpty(documentIdentifier)) {
			final TreeItem documentItem = thumbnailDocumentMap.get(documentIdentifier);
			if (null != documentItem) {
				final Widget bindedWidget = documentItem.getWidget();
				if (bindedWidget instanceof ThumbnailWidgetPanel) {
					final ThumbnailWidgetPanel thumbnailWidget = (ThumbnailWidgetPanel) bindedWidget;
					thumbnailWidget.forceResetWidget();
				}
			}
		}
	}

	public void updateDocument(final Document documentToUpdate) {
		if (null != documentToUpdate) {
			final String documentIdentifier = documentToUpdate.getIdentifier();
			if (!StringUtil.isNullOrEmpty(documentIdentifier)) {
				final TreeItem documentItem = thumbnailDocumentMap.get(documentIdentifier);
				if (null != documentItem) {
					final Widget bindedWidget = documentItem.getWidget();
					if (bindedWidget instanceof ThumbnailWidgetPanel) {
						final ThumbnailWidgetPanel thumbnailWidget = (ThumbnailWidgetPanel) bindedWidget;
						thumbnailWidget.setDocument(documentToUpdate);
						thumbnailWidget.forceResetWidget();
					}
				}
			}
		}
	}

	public void scrollInToView(final Document document) {
		if (null != document) {
			final String documentIdentifier = document.getIdentifier();
			if (!StringUtil.isNullOrEmpty(documentIdentifier)) {
				final TreeItem documentItem = thumbnailDocumentMap.get(documentIdentifier);
				if (null != documentItem) {
					documentItem.getElement().scrollIntoView();
				}
			}
		}
	}

	public ThumbnailWidgetPanel getThumbnailStackPanel(final String documentIdentifier) {
		ThumbnailWidgetPanel documentThumbnail = null;
		if (!StringUtil.isNullOrEmpty(documentIdentifier)) {
			final TreeItem documentTreeItem = thumbnailDocumentMap.get(documentIdentifier);
			final Widget treeItemWidget = documentTreeItem == null ? null : documentTreeItem.getWidget();
			documentThumbnail = treeItemWidget instanceof ThumbnailWidgetPanel ? (ThumbnailWidgetPanel) treeItemWidget : null;
		}
		return documentThumbnail;
	}

	public void removeDocument(final String documentIdentifier) {
		if (!StringUtil.isNullOrEmpty(documentIdentifier)) {
			final TreeItem documentItem = thumbnailDocumentMap.get(documentIdentifier);
			if (null != documentItem) {
				documentItem.remove();
			}
		}
	}

	public void setValidDocumentType(final String documentIdentifier, final boolean isValid) {
		final TreeItem documentTreeItem = thumbnailDocumentMap.get(documentIdentifier);
		if (documentTreeItem != null) {
			if (!isValid) {
				documentTreeItem.removeStyleName("invalidDocumentType");
				documentTreeItem.addStyleName("invalidDocumentType");
			} else {
				documentTreeItem.removeStyleName("invalidDocumentType");
			}
		}
	}

	public void setDocumentType(final String documentIdentifier, final String documentName) {
		final ThumbnailWidgetPanel thumbnailStack = this.getThumbnailStackPanel(documentIdentifier);
		if (null != thumbnailStack) {
			thumbnailStack.resetDocumentTypeName(documentName);
		}
	}
}
