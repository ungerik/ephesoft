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

package com.ephesoft.gxt.core.client.ui.widget;

import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.DomHelper;
import com.sencha.gxt.widget.core.client.grid.ColumnHeader;

public class GridView<T> extends com.sencha.gxt.widget.core.client.grid.GridView<T> {

	private static final String EMPTY_GRID = "emptyGrid";

	public void onColumnWidthChange() {
		// super.layout();
	}

	public void refreshGridRow(int rowIndex) {
		refreshRow(rowIndex);
	}

	@Override
	protected void refreshRow(final int row) {
		super.refreshRow(row);
	}

	@Override
	protected void initHeader() {
		super.initHeader();
		ColumnHeader<T> header = getHeader();
		if (null != header) {
			header.setHeight(33);
		}
	}

	protected void applyEmptyText() {
		if (emptyText == null) {
			emptyText = "&nbsp;";
		}
		if (!hasRows()) {
			if (GXT.isIE()) {
				dataTableBody.removeChildren();
			} else {
				dataTableBody.setInnerHTML("");
			}

			SafeHtml con = getAppearance().renderEmptyContent(emptyText);
			con = tpls.tr("", tpls.tdWrap(cm.getColumnCount(), "", EMPTY_GRID, con));
			DomHelper.append(dataTableBody, con.asString());
		}
	}

	protected boolean hasRows() {
		if (dataTable == null || dataTableBody == null || dataTableBody.getChildCount() == 0) {
			return false;
		}

		Element emptyRowElement = dataTableBody.getFirstChildElement();
		if (emptyRowElement == null) {
			return false;
		}
		emptyRowElement = emptyRowElement.getFirstChildElement();
		if (emptyRowElement == null) {
			return false;
		}
		emptyRowElement = emptyRowElement.getFirstChildElement();
		if (emptyRowElement == null) {
			return false;
		}
		return !emptyRowElement.getClassName().equals(EMPTY_GRID);
	}
}
