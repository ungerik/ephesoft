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

package com.ephesoft.gxt.core.client.util;

import com.ephesoft.gxt.core.client.ui.widget.Grid;
import com.ephesoft.gxt.core.shared.dto.Selectable;
import com.ephesoft.gxt.core.shared.util.StringUtil;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.loader.FilterPagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.widget.core.client.toolbar.PagingToolBar;

public class WidgetUtil {

	public static <T extends Selectable> void loadGrid(Grid<T> gridToLoad, PagingToolBar pagingToolbar) {
		if (null != gridToLoad && pagingToolbar != null) {
			PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader = gridToLoad.getPaginationLoader();
			pagingToolbar.bind(loader);
			loader.load();
		}
	}

	public static <T extends Selectable> void reLoadGrid(Grid<T> gridToLoad) {
		if (null != gridToLoad) {
			PagingLoader<FilterPagingLoadConfig, PagingLoadResult<T>> loader = gridToLoad.getPaginationLoader();
			loader.load();
		}
	}

	@SuppressWarnings("deprecation")
	public static void setID(UIObject widget, String widgetID) {
		if (null != widget && !StringUtil.isNullOrEmpty(widgetID)) {
			Element element = widget.getElement();
			if (null != element) {
				element.setId(widgetID);
			}
		}
	}

	public static String getInnerHTML(final Widget widget) {
		String innerHTML = null;
		if (null != widget) {
			innerHTML = widget.getElement().getInnerHTML();
		}
		return innerHTML;
	}
}
