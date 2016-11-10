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

package com.ephesoft.gxt.rv.client.listener;

import java.util.List;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.dcma.batch.schema.Page;
import com.ephesoft.gxt.core.client.DragDropFlowPanel.DragImage;
import com.ephesoft.gxt.core.client.ui.widget.listener.ImageSelectionListener;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocumentModificationEvent;
import com.ephesoft.gxt.rv.client.event.DragImageEvent;
import com.ephesoft.gxt.rv.client.event.ThumbnailSelectionEvent;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.google.gwt.user.client.Timer;

public class ReviewDragImageSelectionHandler implements ImageSelectionListener {

	private static DragImage lastSelectedImage;

	@Override
	public void onSelect(final DragImage dragImage) {
		selectImage(dragImage);
	}

	public static void selectImage(final DragImage dragImage) {
		if (null != dragImage) {
			if (null != lastSelectedImage) {
				lastSelectedImage.removeStyleName("selectedDraggableImage");
			}
			lastSelectedImage = dragImage;
			lastSelectedImage.addStyleName("selectedDraggableImage");
			ReviewValidateEventBus.fireEvent(new ThumbnailSelectionEvent(dragImage));
			Timer timer = new Timer() {

				public void run() {

					dragImage.getElement().scrollIntoView();
				}
			};
			timer.schedule(30);
		}
	}

	@Override
	public void onDrop(final DragImage dragImage, final List<String> imageURLList, final String title) {
		if (null != dragImage) {
			String thumbnailName = ReviewValidateNavigator.getThumbnailName(dragImage.getUrl());
			Document documentOfPage = ReviewValidateNavigator.getDocument(thumbnailName);
			Document curentDocument = ReviewValidateNavigator.getCurrentDocument();
			if (documentOfPage != curentDocument && null != documentOfPage) {
				changePage(thumbnailName, documentOfPage, curentDocument);
				ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(documentOfPage));
				ReviewValidateEventBus.fireEvent(new DragImageEvent(documentOfPage.getIdentifier()));
			}
			ReviewValidateNavigator.reorderPages(curentDocument, imageURLList);
			ReviewValidateEventBus.fireEvent(new DocumentModificationEvent(ReviewValidateNavigator.getCurrentDocument()));
			final Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
			final String currentDocumentIdentifier = currentDocument == null ? null : currentDocument.getIdentifier();
			ReviewValidateEventBus.fireEvent(new DragImageEvent(currentDocumentIdentifier));
			selectImage(dragImage);
		}
	}

	private void changePage(String thumbnailName, Document currentDocumentOfPage, Document currentDocument) {
		if (null != currentDocument && null != currentDocumentOfPage) {
			Page page = BatchSchemaUtil.getPage(thumbnailName, currentDocumentOfPage);
			BatchSchemaUtil.removePage(currentDocumentOfPage, page);
			BatchSchemaUtil.addPage(currentDocument, page);
		}
	}
}
