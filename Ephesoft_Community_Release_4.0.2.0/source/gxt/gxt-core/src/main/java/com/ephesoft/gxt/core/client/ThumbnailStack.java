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

package com.ephesoft.gxt.core.client;

import java.util.LinkedList;
import java.util.List;

import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;

public class ThumbnailStack extends HorizontalPanel {

	private static final int MAXIMUM_STACKED_IMAGES = 5;
	private List<Image> imageList;

	public ThumbnailStack(final List<Image> imageList) {
		this.addStyleName("thumbnailStack");
		this.imageList = imageList != null ? imageList : new LinkedList<Image>();
		this.layout();
	}

	public void layout() {
		if (!CollectionUtil.isEmpty(imageList)) {
			Image firstImage = null;
			Image secondImage = null;
			Image middleImage = null;
			Image secondLastImage = null;
			Image lastImage = null;
			this.clear();
			final int thumbnailListSize = imageList.size();
			final int thumbailDisplayImages = thumbnailListSize > MAXIMUM_STACKED_IMAGES ? MAXIMUM_STACKED_IMAGES : thumbnailListSize;
			switch (thumbailDisplayImages) {
				case 1:
					firstImage = imageList.get(0);
					break;
				case 2:
					firstImage = imageList.get(0);
					lastImage = imageList.get(1);
					break;
				case 3:
					firstImage = imageList.get(0);
					secondImage = imageList.get(1);
					secondImage.addStyleName("threeImageStack");
					lastImage = imageList.get(2);
					break;
				case 4:
					firstImage = imageList.get(0);
					secondImage = imageList.get(1);
					secondLastImage = imageList.get(2);
					lastImage = imageList.get(3);
					secondLastImage.addStyleName("fourImageStack");
					break;
				case 5:
					firstImage = imageList.get(0);
					secondImage = imageList.get(1);
					middleImage = imageList.get(thumbnailListSize / 2);
					secondLastImage = imageList.get(thumbnailListSize - 2);
					lastImage = imageList.get(thumbnailListSize - 1);
					break;
			}
			addThumbnailStyle(firstImage, "thumbnailFirstImage");
			addThumbnailStyle(secondImage, "thumbnailSecondImage");
			addThumbnailStyle(middleImage, "thumbnailMiddleImage");
			addThumbnailStyle(secondLastImage, "thumbnailSecondLastImage");
			addThumbnailStyle(lastImage, "thumbnailLastImage");
		}
	}

	private void addThumbnailStyle(Image thumbnailImage, String styleName) {
		if (null != thumbnailImage && !StringUtil.isNullOrEmpty(styleName)) {
			thumbnailImage.addStyleName(styleName);
			thumbnailImage.addMouseDownHandler(new MouseDownHandler() {

				@Override
				public void onMouseDown(MouseDownEvent event) {
					event.preventDefault();
				}
			});
			this.add(thumbnailImage);
			thumbnailImage.addStyleName("stackImage");
			this.getElement().setDraggable(Element.DRAGGABLE_FALSE);
		}
	}

	public void addImage(Image imageToAdd) {
		if (null != imageToAdd) {
			imageList.add(imageToAdd);
		}
	}
}
