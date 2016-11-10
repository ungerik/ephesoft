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

import java.util.ArrayList;
import java.util.List;

import com.ephesoft.dcma.batch.schema.Document;
import com.ephesoft.gxt.core.client.ThumbnailStack;
import com.ephesoft.gxt.core.client.DCMAEntryPoint.EphesoftUIContext;
import com.ephesoft.gxt.core.shared.constant.CoreCommonConstant;
import com.ephesoft.gxt.core.shared.util.BatchSchemaUtil;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.rv.client.controller.ReviewValidateController.ReviewValidateEventBus;
import com.ephesoft.gxt.rv.client.event.DocumentSelectionEvent;
import com.ephesoft.gxt.rv.client.view.navigator.ReviewValidateNavigator;
import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LazyPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ThumbnailWidgetPanel extends LazyPanel {

	private Document document;

	private boolean isOpenedOnValidation;

	private final String docIdentifier;

	private VerticalPanel panel = null;

	private Label documentNameLabel;

	public ThumbnailWidgetPanel(final String docIdentifier) {
		this(false, docIdentifier);
	}

	public ThumbnailWidgetPanel(final boolean isOpenedOnValidation, final String docIdentifier) {
		this.isOpenedOnValidation = isOpenedOnValidation;
		this.docIdentifier = docIdentifier;
		this.addStyleName("thumbnailStack");
		this.addDragOverHandler();
		this.addDropHandler();
	}

	@Override
	protected Widget createWidget() {
		if (null != document) {
			final List<String> thumbnailNamesList = BatchSchemaUtil.getThumbnails(document);
			List<Image> thumbnailImages = null;
			Image thumbnailImage = new Image();
			if (!CollectionUtil.isEmpty(thumbnailNamesList)) {
				thumbnailImages = new ArrayList<Image>();
				for (final String thumbnailName : thumbnailNamesList) {
					thumbnailImage = new Image(ReviewValidateNavigator.getAbsoluteURL(thumbnailName));
					thumbnailImages.add(thumbnailImage);
				}
			}
			panel = new VerticalPanel();
			panel.addStyleName("documentStack");
			documentNameLabel = new Label(getDocumentTypeDisplayText(document));
			documentNameLabel = new Label(document.getType());
			final HorizontalPanel horizontalPanel = new HorizontalPanel();
			horizontalPanel.setSpacing(5);
			documentNameLabel.addStyleName("highlightedLabel");
			horizontalPanel.add(new Label(docIdentifier));
			horizontalPanel.add(documentNameLabel);
			final ThumbnailStack thumbnailStack = new ThumbnailStack(thumbnailImages);
			panel.add(horizontalPanel);
			panel.add(thumbnailStack);
		}
		return panel;
	}
	private static String getDocumentTypeDisplayText(final Document documentToDisplay) {
		final int documentDisplayProperty = EphesoftUIContext.getDocumentDisplayProperty();
		String textToDisplay = null;
		switch (documentDisplayProperty) {
			case 1:  textToDisplay = documentToDisplay.getType();
				break;
			case 2:
				textToDisplay = String.valueOf(BatchSchemaUtil.getPageCount(documentToDisplay));
				break;
			case 3:
				textToDisplay = String.valueOf(documentToDisplay.getConfidence());
				break;
			case 4:
				textToDisplay = String.valueOf(documentToDisplay.getConfidenceThreshold());
				break;
			case 5:
				textToDisplay = documentToDisplay.getDocumentDisplayInfo();
				break;
			default:
				textToDisplay = documentToDisplay.getType();
		}
		return textToDisplay == null ? CoreCommonConstant.EMPTY_STRING : textToDisplay;
	}

	/**
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * @param document the document to set
	 */
	public void setDocument(final Document document) {
		this.document = document;
	}

	public void setUnloadedDocument(final Document document) {
		if (this.document == null) {
			this.document = document;
		}
	}

	/**
	 * @return the isOpenedOnValidation
	 */
	public boolean isOpenedOnValidation() {
		return isOpenedOnValidation;
	}

	/**
	 * @param isOpenedOnValidation the isOpenedOnValidation to set
	 */
	public void setOpenedOnValidation(final boolean isOpenedOnValidation) {
		this.isOpenedOnValidation = isOpenedOnValidation;
	}

	private void addDragOverHandler() {
		this.addDomHandler(new DragOverHandler() {

			@Override
			public void onDragOver(DragOverEvent event) {
				Document currentDocument = ReviewValidateNavigator.getCurrentDocument();
				if (null != currentDocument && currentDocument != document) {
					ReviewValidateEventBus.fireEvent(new DocumentSelectionEvent(docIdentifier));
				}
			}
		}, DragOverEvent.getType());
	}

	private void addDropHandler() {
		this.addDomHandler(new DropHandler() {
			
			@Override
			public void onDrop(DropEvent event) {
				event.preventDefault();
				event.stopPropagation();
			}
		}, DropEvent.getType());
	}

	/**
	 * @return the docIdentifier
	 */
	public String getDocIdentifier() {
		return docIdentifier;
	}

	public void setVisible(final boolean visible) {
		super.setVisible(visible);
	}

	public void forceResetWidget() {
		this.setWidget(createWidget());
		this.setVisible(true);
	}

	public void resetDocumentTypeName(final String newDocumentName) {
		documentNameLabel.setText(newDocumentName);
	}
}
