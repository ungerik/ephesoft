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

import com.ephesoft.gxt.core.client.ui.widget.Message;
import com.ephesoft.gxt.core.client.view.ListPanel;
import com.ephesoft.gxt.core.shared.util.CollectionUtil;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.ephesoft.gxt.rv.client.widget.resource.bundle.FieldStateImageResource;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;

public class ValidationTabPanel extends TabPanel {

	private final List<ListPanel<?, ?>> panelList;

	public ValidationTabPanel() {
		panelList = new ArrayList<ListPanel<?, ?>>();
		this.setAnimScroll(true);
	}

	public void add(final ListPanel<?, ?> panelToAdd, final String header) {
		if (null != panelToAdd && !StringUtil.isNullOrEmpty(header)) {
			panelList.add(panelToAdd);
			super.add(panelToAdd, header);
		}
	}

	/**
	 * @return the isValid
	 */
	public boolean isValid() {
		boolean isValid = true;
		if (!CollectionUtil.isEmpty(panelList)) {
			for (final ListPanel<?, ?> panel : panelList) {
				if (null != panel && !panel.isValid()) {
					isValid = false;
					break;
				}
			}
		}
		return isValid;
	}

	public void clear() {
		final int totalElements = panelList.size();
		panelList.clear();
		for (int widgetIndex = 0; widgetIndex < totalElements; widgetIndex++) {
			this.remove(0);
		}
	}

	public void setValid(final ListPanel<?, ?> panel, final boolean isValid) {
		if (null != panel) {
			final TabItemConfig config = this.getConfig(panel);
			if (null != config) {
				ImageResource icon;
				if (isValid) {
					icon = FieldStateImageResource.FIELD_RESOURCE.valid();
				} else {
					icon = FieldStateImageResource.FIELD_RESOURCE.error();
				}
				if (config.getIcon() != icon) {
					config.setIcon(icon);
					update(panel, config);
				}
				panel.setValid(isValid);
			}
		}
	}

	public ListPanel<?, ?> getFirstInvalidListView() {
		ListPanel<?, ?> defaultPanel = null;
		if (!CollectionUtil.isEmpty(panelList)) {
			defaultPanel = panelList.get(0);
			for (final ListPanel<?, ?> viewPanel : panelList) {
				if (viewPanel != null && !viewPanel.isValid()) {
					defaultPanel = viewPanel;
					break;
				}
			}
		}
		return defaultPanel;
	}

	public ListPanel<?, ?> getSubSequentListView(final ListPanel<?, ?> panel, boolean isPrevious, final boolean errorView) {
		ListPanel<?, ?> defaultPanel = panel;
		if (!CollectionUtil.isEmpty(panelList) && null != panel) {
			int currentPanelIndex = panelList.indexOf(panel);
			final int totalElements = panelList.size();
			ListPanel<?, ?> currentPanel = null;
			for (int traversalIndex = 0; traversalIndex < totalElements; traversalIndex++) {
				int subsequentIndex = getSubsequentTabIndex(currentPanelIndex, totalElements, isPrevious);
				currentPanel = panelList.get(subsequentIndex);
				if (null != currentPanel && (!currentPanel.isValid() || !errorView)) {
					defaultPanel = currentPanel;
					break;
				}
				currentPanelIndex++;
			}
		}
		return defaultPanel;
	}

	private int getSubsequentTabIndex(final int currentTabIndex, final int totalTabs, final boolean isPrevious) {
		int nextIndex = 0;
		int tabIndex = currentTabIndex;
		if (isPrevious) {
			if (currentTabIndex == 0) {
				tabIndex = totalTabs;
			}
			nextIndex = (--tabIndex) % totalTabs;
		} else {
			nextIndex = ((++tabIndex) % totalTabs);
		}
		return nextIndex;
	}
}
